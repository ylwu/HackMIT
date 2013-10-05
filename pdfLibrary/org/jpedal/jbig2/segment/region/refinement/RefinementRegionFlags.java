package org.jpedal.jbig2.segment.region.refinement;

import java.util.Map;
import org.jpedal.jbig2.segment.Flags;

public class RefinementRegionFlags extends Flags
{
  public static final String GR_TEMPLATE = "GR_TEMPLATE";
  public static final String TPGDON = "TPGDON";

  public void setFlags(int paramInt)
  {
    this.flags.put("GR_TEMPLATE", Integer.valueOf(paramInt & 0x1));
    this.flags.put("TPGDON", Integer.valueOf(paramInt >> 1 & 0x1));
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.jbig2.segment.region.refinement.RefinementRegionFlags
 * JD-Core Version:    0.6.2
 */