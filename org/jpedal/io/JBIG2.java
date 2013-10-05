package org.jpedal.io;

import org.jpedal.jbig2.JBIG2Decoder;
import org.jpedal.jbig2.image.JBIG2Bitmap;

public class JBIG2
{
  public static byte[] JBIGDecode(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    throws Exception
  {
    JBIG2Decoder localJBIG2Decoder = new JBIG2Decoder();
    if ((paramArrayOfByte2 != null) && (paramArrayOfByte2.length > 0))
      localJBIG2Decoder.setGlobalData(paramArrayOfByte2);
    localJBIG2Decoder.decodeJBIG2(paramArrayOfByte1);
    paramArrayOfByte1 = localJBIG2Decoder.getPageAsJBIG2Bitmap(0).getWriteSafeData(true);
    return paramArrayOfByte1;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.io.JBIG2
 * JD-Core Version:    0.6.2
 */