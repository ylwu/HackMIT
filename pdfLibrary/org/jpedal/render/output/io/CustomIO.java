package org.jpedal.render.output.io;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public abstract interface CustomIO
{
  public abstract void writeFont(String paramString, byte[] paramArrayOfByte);

  public abstract void writePlainTextFile(String paramString, StringBuilder paramStringBuilder);

  public abstract boolean isOutputOpen();

  public abstract void setupOutput(String paramString1, boolean paramBoolean, String paramString2)
    throws FileNotFoundException, UnsupportedEncodingException;

  public abstract void setupOutput(OutputStream paramOutputStream, boolean paramBoolean, String paramString)
    throws FileNotFoundException, UnsupportedEncodingException;

  public abstract void flush();

  public abstract void writeString(String paramString);

  public abstract String writeImage(String paramString1, String paramString2, BufferedImage paramBufferedImage);

  public abstract String getImageTypeUsed();
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.render.output.io.CustomIO
 * JD-Core Version:    0.6.2
 */