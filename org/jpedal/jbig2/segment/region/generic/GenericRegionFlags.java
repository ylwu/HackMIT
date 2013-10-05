package org.jpedal.jbig2.segment.region.generic;

import java.util.Map;
import org.jpedal.jbig2.segment.Flags;

public class GenericRegionFlags extends Flags
{
  public static final String MMR = "MMR";
  public static final String GB_TEMPLATE = "GB_TEMPLATE";
  public static final String TPGDON = "TPGDON";

  public void setFlags(int paramInt)
  {
    this.flags.put("MMR", Integer.valueOf(paramInt & 0x1));
    this.flags.put("GB_TEMPLATE", Integer.valueOf(paramInt >> 1 & 0x3));
    this.flags.put("TPGDON", Integer.valueOf(paramInt >> 3 & 0x1));
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.jbig2.segment.region.generic.GenericRegionFlags
 * JD-Core Version:    0.6.2
 */