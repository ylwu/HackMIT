package org.jpedal.jbig2.segment.region.halftone;

import java.util.Map;
import org.jpedal.jbig2.segment.Flags;

public class HalftoneRegionFlags extends Flags
{
  public static final String H_MMR = "H_MMR";
  public static final String H_TEMPLATE = "H_TEMPLATE";
  public static final String H_ENABLE_SKIP = "H_ENABLE_SKIP";
  public static final String H_COMB_OP = "H_COMB_OP";
  public static final String H_DEF_PIXEL = "H_DEF_PIXEL";

  public void setFlags(int paramInt)
  {
    this.flags.put("H_MMR", Integer.valueOf(paramInt & 0x1));
    this.flags.put("H_TEMPLATE", Integer.valueOf(paramInt >> 1 & 0x3));
    this.flags.put("H_ENABLE_SKIP", Integer.valueOf(paramInt >> 3 & 0x1));
    this.flags.put("H_COMB_OP", Integer.valueOf(paramInt >> 4 & 0x7));
    this.flags.put("H_DEF_PIXEL", Integer.valueOf(paramInt >> 7 & 0x1));
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.jbig2.segment.region.halftone.HalftoneRegionFlags
 * JD-Core Version:    0.6.2
 */