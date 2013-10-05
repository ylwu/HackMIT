package org.jpedal.jbig2.segment.region;

import java.util.Map;
import org.jpedal.jbig2.segment.Flags;

public class RegionFlags extends Flags
{
  public static final String EXTERNAL_COMBINATION_OPERATOR = "EXTERNAL_COMBINATION_OPERATOR";

  public void setFlags(int paramInt)
  {
    this.flags.put("EXTERNAL_COMBINATION_OPERATOR", Integer.valueOf(paramInt & 0x7));
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.jbig2.segment.region.RegionFlags
 * JD-Core Version:    0.6.2
 */