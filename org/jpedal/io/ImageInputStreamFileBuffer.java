package org.jpedal.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.stream.ImageInputStream;
import org.jpedal.utils.LogWriter;

public class ImageInputStreamFileBuffer
  implements RandomAccessBuffer
{
  private String fileName = "";
  ImageInputStream iis;

  public ImageInputStreamFileBuffer(ImageInputStream paramImageInputStream)
  {
    this.iis = paramImageInputStream;
  }

  public long getFilePointer()
    throws IOException
  {
    return this.iis.getStreamPosition();
  }

  public void seek(long paramLong)
    throws IOException
  {
    this.iis.seek(paramLong);
  }

  public int read()
    throws IOException
  {
    return this.iis.read();
  }

  public String readLine()
    throws IOException
  {
    return this.iis.readLine();
  }

  public long length()
    throws IOException
  {
    return this.iis.length();
  }

  public void close()
    throws IOException
  {
    this.iis.close();
  }

  public int read(byte[] paramArrayOfByte)
    throws IOException
  {
    return this.iis.read(paramArrayOfByte);
  }

  public byte[] getPdfBuffer()
  {
    byte[] arrayOfByte1 = null;
    try
    {
      ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
      byte[] arrayOfByte2 = new byte[4096];
      int i;
      while ((i = this.iis.read(arrayOfByte2)) != -1)
        localByteArrayOutputStream.write(arrayOfByte2, 0, i);
      localByteArrayOutputStream.flush();
      localByteArrayOutputStream.close();
      arrayOfByte1 = localByteArrayOutputStream.toByteArray();
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
      if (LogWriter.isOutput())
        LogWriter.writeLog("[PDF] Exception " + localIOException + " getting byte[] for " + this.fileName);
    }
    return arrayOfByte1;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.io.ImageInputStreamFileBuffer
 * JD-Core Version:    0.6.2
 */