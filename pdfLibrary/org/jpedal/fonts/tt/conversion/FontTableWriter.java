package org.jpedal.fonts.tt.conversion;

import java.io.IOException;

public abstract interface FontTableWriter
{
  public static final int MIN_CHAR_CODE = 0;
  public static final int MAX_CHAR_CODE = 1;

  public abstract byte[] writeTable()
    throws IOException;

  public abstract int getIntValue(int paramInt);
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.fonts.tt.conversion.FontTableWriter
 * JD-Core Version:    0.6.2
 */