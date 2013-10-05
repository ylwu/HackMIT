package org.jpedal.io.filter;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
import org.jpedal.objects.raw.PdfObject;
import org.jpedal.utils.LogWriter;

public class Flate extends BaseFilter
  implements PdfFilter
{
  private int predictor = 1;
  private int colors = 1;
  private int bitsPerComponent = 8;
  private int columns = 1;
  private boolean hasError = false;

  public Flate(PdfObject paramPdfObject)
  {
    super(paramPdfObject);
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
      this.predictor = paramPdfObject.getInt(1970893723);
    }
  }

  public byte[] decode(byte[] paramArrayOfByte)
    throws Exception
  {
    int i = 512000;
    ByteArrayOutputStream localByteArrayOutputStream = null;
    int j = 1;
    int k = paramArrayOfByte.length;
    if (paramArrayOfByte != null)
    {
      while (j != 0)
      {
        Inflater localInflater = new Inflater();
        localInflater.setInput(paramArrayOfByte);
        int m = paramArrayOfByte.length;
        localByteArrayOutputStream = new ByteArrayOutputStream(m);
        if (m < i)
          i = m;
        byte[] arrayOfByte1 = new byte[i];
        try
        {
          while (!localInflater.finished())
          {
            int n = localInflater.inflate(arrayOfByte1);
            localByteArrayOutputStream.write(arrayOfByte1, 0, n);
            if (localInflater.getRemaining() == 0)
              break;
          }
          j = 0;
        }
        catch (Exception localException)
        {
          if (LogWriter.isOutput())
            LogWriter.writeLog("Exception in Flate " + localException);
          j = 1;
          this.hasError = true;
          if ((paramArrayOfByte.length == k) && (paramArrayOfByte.length > 10000))
          {
            j = 0;
          }
          else if (paramArrayOfByte.length > 10)
          {
            byte[] arrayOfByte2 = new byte[paramArrayOfByte.length - 1];
            System.arraycopy(paramArrayOfByte, 0, arrayOfByte2, 0, paramArrayOfByte.length - 1);
            paramArrayOfByte = arrayOfByte2;
          }
          else
          {
            j = 0;
          }
        }
      }
      paramArrayOfByte = localByteArrayOutputStream.toByteArray();
      return applyPredictor(this.predictor, paramArrayOfByte, this.colors, this.bitsPerComponent, this.columns);
    }
    return paramArrayOfByte;
  }

  public void decode(BufferedInputStream paramBufferedInputStream, BufferedOutputStream paramBufferedOutputStream, String paramString, Map paramMap)
    throws Exception
  {
    this.bis = paramBufferedInputStream;
    this.streamCache = paramBufferedOutputStream;
    this.cachedObjects = paramMap;
    if (paramBufferedInputStream != null)
    {
      InflaterInputStream localInflaterInputStream = null;
      try
      {
        localInflaterInputStream = new InflaterInputStream(paramBufferedInputStream);
        while (true)
        {
          int i = localInflaterInputStream.read();
          if ((localInflaterInputStream.available() == 0) || (i == -1))
            break;
          paramBufferedOutputStream.write(i);
        }
        if ((this.predictor != 1) && (this.predictor != 10))
        {
          paramBufferedOutputStream.flush();
          paramBufferedOutputStream.close();
          if (paramString != null)
            setupCachedObjectForDecoding(paramString);
        }
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
        if (LogWriter.isOutput())
          LogWriter.writeLog("Exception " + localException + " accessing Flate filter ");
      }
      finally
      {
        if (localInflaterInputStream != null)
          localInflaterInputStream.close();
      }
    }
    applyPredictor(this.predictor, null, this.colors, this.bitsPerComponent, this.columns);
  }

  public boolean hasError()
  {
    return this.hasError;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.io.filter.Flate
 * JD-Core Version:    0.6.2
 */