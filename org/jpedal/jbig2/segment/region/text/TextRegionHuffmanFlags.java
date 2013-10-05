package org.jpedal.jbig2.segment.region.text;

import java.util.Map;
import org.jpedal.jbig2.segment.Flags;

public class TextRegionHuffmanFlags extends Flags
{
  public static final String SB_HUFF_FS = "SB_HUFF_FS";
  public static final String SB_HUFF_DS = "SB_HUFF_DS";
  public static final String SB_HUFF_DT = "SB_HUFF_DT";
  public static final String SB_HUFF_RDW = "SB_HUFF_RDW";
  public static final String SB_HUFF_RDH = "SB_HUFF_RDH";
  public static final String SB_HUFF_RDX = "SB_HUFF_RDX";
  public static final String SB_HUFF_RDY = "SB_HUFF_RDY";
  public static final String SB_HUFF_RSIZE = "SB_HUFF_RSIZE";

  public void setFlags(int paramInt)
  {
    this.flags.put("SB_HUFF_FS", Integer.valueOf(paramInt & 0x3));
    this.flags.put("SB_HUFF_DS", Integer.valueOf(paramInt >> 2 & 0x3));
    this.flags.put("SB_HUFF_DT", Integer.valueOf(paramInt >> 4 & 0x3));
    this.flags.put("SB_HUFF_RDW", Integer.valueOf(paramInt >> 6 & 0x3));
    this.flags.put("SB_HUFF_RDH", Integer.valueOf(paramInt >> 8 & 0x3));
    this.flags.put("SB_HUFF_RDX", Integer.valueOf(paramInt >> 10 & 0x3));
    this.flags.put("SB_HUFF_RDY", Integer.valueOf(paramInt >> 12 & 0x3));
    this.flags.put("SB_HUFF_RSIZE", Integer.valueOf(paramInt >> 14 & 0x1));
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.jbig2.segment.region.text.TextRegionHuffmanFlags
 * JD-Core Version:    0.6.2
 */