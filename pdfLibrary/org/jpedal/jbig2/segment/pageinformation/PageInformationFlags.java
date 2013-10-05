package org.jpedal.jbig2.segment.pageinformation;

import java.util.Map;
import org.jpedal.jbig2.segment.Flags;

public class PageInformationFlags extends Flags
{
  public static final String DEFAULT_PIXEL_VALUE = "DEFAULT_PIXEL_VALUE";
  private static final String DEFAULT_COMBINATION_OPERATOR = "DEFAULT_COMBINATION_OPERATOR";

  public void setFlags(int paramInt)
  {
    this.flags.put("DEFAULT_PIXEL_VALUE", Integer.valueOf(paramInt >> 2 & 0x1));
    this.flags.put("DEFAULT_COMBINATION_OPERATOR", Integer.valueOf(paramInt >> 3 & 0x3));
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.jbig2.segment.pageinformation.PageInformationFlags
 * JD-Core Version:    0.6.2
 */