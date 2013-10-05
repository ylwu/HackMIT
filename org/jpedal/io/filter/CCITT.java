package org.jpedal.io.filter;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.util.Map;
import org.jpedal.io.JAIHelper;
import org.jpedal.io.filter.ccitt.CCITT1D;
import org.jpedal.io.filter.ccitt.CCITT2D;
import org.jpedal.io.filter.ccitt.CCITTDecoder;
import org.jpedal.io.filter.ccitt.CCITTMix;
import org.jpedal.objects.raw.PdfObject;

public class CCITT extends BaseFilter
  implements PdfFilter
{
  private final int width;
  private final int height;

  public CCITT(PdfObject paramPdfObject, int paramInt1, int paramInt2)
  {
    super(paramPdfObject);
    this.width = paramInt1;
    this.height = paramInt2;
    JAIHelper.confirmJAIOnClasspath();
  }

  public byte[] decode(byte[] paramArrayOfByte)
    throws Exception
  {
    paramArrayOfByte = decodeCCITT(paramArrayOfByte);
    return paramArrayOfByte;
  }

  public void decode(BufferedInputStream paramBufferedInputStream, BufferedOutputStream paramBufferedOutputStream, String paramString, Map paramMap)
    throws Exception
  {
    int i = paramBufferedInputStream.available();
    byte[] arrayOfByte = new byte[i];
    paramBufferedInputStream.read(arrayOfByte);
    arrayOfByte = decodeCCITT(arrayOfByte);
    paramBufferedOutputStream.write(arrayOfByte);
  }

  private byte[] decodeCCITT(byte[] paramArrayOfByte)
    throws Exception
  {
    int i = this.decodeParms.getInt(27);
    Object localObject = null;
    if (i == 0)
      localObject = new CCITT1D(paramArrayOfByte, this.width, this.height, this.decodeParms);
    else if (i < 0)
      localObject = new CCITT2D(paramArrayOfByte, this.width, this.height, this.decodeParms);
    else if (i > 0)
      localObject = new CCITTMix(paramArrayOfByte, this.width, this.height, this.decodeParms);
    byte[] arrayOfByte = ((CCITTDecoder)localObject).decode();
    return arrayOfByte;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.io.filter.CCITT
 * JD-Core Version:    0.6.2
 */