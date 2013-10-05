package org.jpedal.fonts.tt.conversion;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.jpedal.fonts.tt.Head;

public class HeadWriter extends Head
  implements FontTableWriter
{
  public HeadWriter(float[] paramArrayOfFloat, double paramDouble)
  {
    this.FontBBox = paramArrayOfFloat;
    if (paramDouble > 1024.0D)
      this.unitsPerEm = ((int)paramDouble);
  }

  public byte[] writeTable()
    throws IOException
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    localByteArrayOutputStream.write(TTFontWriter.setNextUint32(65536));
    localByteArrayOutputStream.write(TTFontWriter.setNextUint32(65536));
    localByteArrayOutputStream.write(TTFontWriter.setNextUint32(0));
    localByteArrayOutputStream.write(TTFontWriter.setNextUint32(1594834165));
    localByteArrayOutputStream.write(TTFontWriter.setNextUint16(this.flags));
    localByteArrayOutputStream.write(TTFontWriter.setNextUint16(this.unitsPerEm));
    localByteArrayOutputStream.write(TTFontWriter.setNextUint64(3405888000L));
    localByteArrayOutputStream.write(TTFontWriter.setNextUint64(3405888000L));
    for (int i = 0; i < 4; i++)
      localByteArrayOutputStream.write(TTFontWriter.setNextSignedInt16((short)(int)this.FontBBox[i]));
    localByteArrayOutputStream.write(TTFontWriter.setNextUint16(0));
    localByteArrayOutputStream.write(TTFontWriter.setNextUint16(7));
    localByteArrayOutputStream.write(TTFontWriter.setNextUint16(2));
    localByteArrayOutputStream.write(TTFontWriter.setNextUint16(this.format));
    localByteArrayOutputStream.write(TTFontWriter.setNextUint16(0));
    localByteArrayOutputStream.flush();
    localByteArrayOutputStream.close();
    return localByteArrayOutputStream.toByteArray();
  }

  public int getIntValue(int paramInt)
  {
    return 0;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.fonts.tt.conversion.HeadWriter
 * JD-Core Version:    0.6.2
 */