package org.jpedal.jbig2.segment.region.text;

import java.util.Map;
import org.jpedal.jbig2.segment.Flags;

public class TextRegionFlags extends Flags
{
  public static final String SB_HUFF = "SB_HUFF";
  public static final String SB_REFINE = "SB_REFINE";
  public static final String LOG_SB_STRIPES = "LOG_SB_STRIPES";
  public static final String REF_CORNER = "REF_CORNER";
  public static final String TRANSPOSED = "TRANSPOSED";
  public static final String SB_COMB_OP = "SB_COMB_OP";
  public static final String SB_DEF_PIXEL = "SB_DEF_PIXEL";
  public static final String SB_DS_OFFSET = "SB_DS_OFFSET";
  public static final String SB_R_TEMPLATE = "SB_R_TEMPLATE";

  public void setFlags(int paramInt)
  {
    this.flags.put("SB_HUFF", Integer.valueOf(paramInt & 0x1));
    this.flags.put("SB_REFINE", Integer.valueOf(paramInt >> 1 & 0x1));
    this.flags.put("LOG_SB_STRIPES", Integer.valueOf(paramInt >> 2 & 0x3));
    this.flags.put("REF_CORNER", Integer.valueOf(paramInt >> 4 & 0x3));
    this.flags.put("TRANSPOSED", Integer.valueOf(paramInt >> 6 & 0x1));
    this.flags.put("SB_COMB_OP", Integer.valueOf(paramInt >> 7 & 0x3));
    this.flags.put("SB_DEF_PIXEL", Integer.valueOf(paramInt >> 9 & 0x1));
    int i = paramInt >> 10 & 0x1F;
    if ((i & 0x10) != 0)
      i |= -16;
    this.flags.put("SB_DS_OFFSET", Integer.valueOf(i));
    this.flags.put("SB_R_TEMPLATE", Integer.valueOf(paramInt >> 15 & 0x1));
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.jbig2.segment.region.text.TextRegionFlags
 * JD-Core Version:    0.6.2
 */