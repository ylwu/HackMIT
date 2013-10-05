package org.jpedal.io.filter;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.util.Map;
import org.jpedal.objects.raw.PdfObject;
import org.jpedal.sun.LZWDecoder;
import org.jpedal.sun.LZWDecoder2;
import org.jpedal.sun.TIFFLZWDecoder;

public class LZW extends BaseFilter
  implements PdfFilter
{
  private int predictor = 1;
  private int EarlyChange = 1;
  private int colors = 1;
  private int bitsPerComponent = 8;
  private int rows;
  private int columns;

  public LZW(PdfObject paramPdfObject, int paramInt1, int paramInt2)
  {
    super(paramPdfObject);
    this.rows = paramInt2;
    this.columns = paramInt1;
    if (paramPdfObject != null)
    {
      int i = paramPdfObject.getInt(-1344207655);
      if (i != -1)
        this.bitsPerComponent = i;
      int j = paramPdfObject.getInt(1010783618);
      if (j != -1)
        this.colors = j;
      int k = paramPdfObject.getInt(1162902911);
      if (k != -1)
        this.columns = k;
      this.EarlyChange = paramPdfObject.getInt(1838971823);
      this.predictor = paramPdfObject.getInt(1970893723);
      int m = paramPdfObject.getInt(574572355);
      if (m != -1)
        this.rows = m;
    }
  }

  public byte[] decode(byte[] paramArrayOfByte)
    throws Exception
  {
    Object localObject;
    if (this.rows * this.columns == 1)
    {
      if (paramArrayOfByte != null)
      {
        int i = 8;
        localObject = new byte[i * this.rows * (this.columns + 7 >> 3)];
        TIFFLZWDecoder localTIFFLZWDecoder = new TIFFLZWDecoder(this.columns, this.predictor, i);
        localTIFFLZWDecoder.decode(paramArrayOfByte, (byte[])localObject, this.rows);
        return applyPredictor(this.predictor, (byte[])localObject, this.colors, i, this.columns);
      }
    }
    else
    {
      if (paramArrayOfByte != null)
      {
        ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
        localObject = new LZWDecoder();
        ((LZWDecoder)localObject).decode(paramArrayOfByte, localByteArrayOutputStream, this.EarlyChange == 1);
        localByteArrayOutputStream.close();
        paramArrayOfByte = localByteArrayOutputStream.toByteArray();
      }
      paramArrayOfByte = applyPredictor(this.predictor, paramArrayOfByte, this.colors, this.bitsPerComponent, this.columns);
    }
    return paramArrayOfByte;
  }

  public void decode(BufferedInputStream paramBufferedInputStream, BufferedOutputStream paramBufferedOutputStream, String paramString, Map paramMap)
    throws Exception
  {
    if (this.rows * this.columns != 1)
    {
      if (paramBufferedInputStream != null)
      {
        LZWDecoder2 localLZWDecoder2 = new LZWDecoder2();
        localLZWDecoder2.decode(null, paramBufferedOutputStream, paramBufferedInputStream);
      }
      if ((this.predictor != 1) && (this.predictor != 10))
      {
        paramBufferedOutputStream.flush();
        paramBufferedOutputStream.close();
        if (paramString != null)
          setupCachedObjectForDecoding(paramString);
      }
      applyPredictor(this.predictor, null, this.colors, this.bitsPerComponent, this.columns);
    }
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.io.filter.LZW
 * JD-Core Version:    0.6.2
 */