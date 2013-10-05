package org.jpedal.fonts.tt.conversion;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.jpedal.fonts.tt.Post;

public class PostWriter extends Post
  implements FontTableWriter
{
  public byte[] writeTable()
    throws IOException
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    localByteArrayOutputStream.write(TTFontWriter.setNextUint32(196608));
    localByteArrayOutputStream.write(TTFontWriter.setNextUint32(0));
    localByteArrayOutputStream.write(TTFontWriter.setNextInt16(-125));
    localByteArrayOutputStream.write(TTFontWriter.setNextInt16(54));
    localByteArrayOutputStream.write(TTFontWriter.setNextUint32(0));
    localByteArrayOutputStream.write(TTFontWriter.setNextUint32(0));
    localByteArrayOutputStream.write(TTFontWriter.setNextUint32(0));
    localByteArrayOutputStream.write(TTFontWriter.setNextUint32(0));
    localByteArrayOutputStream.write(TTFontWriter.setNextUint32(0));
    localByteArrayOutputStream.flush();
    localByteArrayOutputStream.close();
    return localByteArrayOutputStream.toByteArray();
  }

  public int getIntValue(int paramInt)
  {
    int i = 0;
    switch (paramInt)
    {
    }
    return i;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.fonts.tt.conversion.PostWriter
 * JD-Core Version:    0.6.2
 */