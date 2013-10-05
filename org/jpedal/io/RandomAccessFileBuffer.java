package org.jpedal.io;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import org.jpedal.utils.LogWriter;

public class RandomAccessFileBuffer extends RandomAccessFile
  implements RandomAccessBuffer
{
  private String fileName = "";

  public RandomAccessFileBuffer(String paramString1, String paramString2)
    throws FileNotFoundException
  {
    super(paramString1, paramString2);
    this.fileName = paramString1;
  }

  public byte[] getPdfBuffer()
  {
    byte[] arrayOfByte1 = null;
    try
    {
      URL localURL = new URL("file:///" + this.fileName);
      InputStream localInputStream = localURL.openStream();
      ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
      byte[] arrayOfByte2 = new byte[4096];
      int i;
      while ((i = localInputStream.read(arrayOfByte2)) != -1)
        localByteArrayOutputStream.write(arrayOfByte2, 0, i);
      localByteArrayOutputStream.flush();
      localInputStream.close();
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
 * Qualified Name:     org.jpedal.io.RandomAccessFileBuffer
 * JD-Core Version:    0.6.2
 */