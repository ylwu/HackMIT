package org.jpedal.jbig2.segment.symboldictionary;

import java.util.Map;
import org.jpedal.jbig2.segment.Flags;

public class SymbolDictionaryFlags extends Flags
{
  public static final String SD_HUFF = "SD_HUFF";
  public static final String SD_REF_AGG = "SD_REF_AGG";
  public static final String SD_HUFF_DH = "SD_HUFF_DH";
  public static final String SD_HUFF_DW = "SD_HUFF_DW";
  public static final String SD_HUFF_BM_SIZE = "SD_HUFF_BM_SIZE";
  public static final String SD_HUFF_AGG_INST = "SD_HUFF_AGG_INST";
  public static final String BITMAP_CC_USED = "BITMAP_CC_USED";
  public static final String BITMAP_CC_RETAINED = "BITMAP_CC_RETAINED";
  public static final String SD_TEMPLATE = "SD_TEMPLATE";
  public static final String SD_R_TEMPLATE = "SD_R_TEMPLATE";

  public void setFlags(int paramInt)
  {
    this.flags.put("SD_HUFF", Integer.valueOf(paramInt & 0x1));
    this.flags.put("SD_REF_AGG", Integer.valueOf(paramInt >> 1 & 0x1));
    this.flags.put("SD_HUFF_DH", Integer.valueOf(paramInt >> 2 & 0x3));
    this.flags.put("SD_HUFF_DW", Integer.valueOf(paramInt >> 4 & 0x3));
    this.flags.put("SD_HUFF_BM_SIZE", Integer.valueOf(paramInt >> 6 & 0x1));
    this.flags.put("SD_HUFF_AGG_INST", Integer.valueOf(paramInt >> 7 & 0x1));
    this.flags.put("BITMAP_CC_USED", Integer.valueOf(paramInt >> 8 & 0x1));
    this.flags.put("BITMAP_CC_RETAINED", Integer.valueOf(paramInt >> 9 & 0x1));
    this.flags.put("SD_TEMPLATE", Integer.valueOf(paramInt >> 10 & 0x3));
    this.flags.put("SD_R_TEMPLATE", Integer.valueOf(paramInt >> 12 & 0x1));
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.jbig2.segment.symboldictionary.SymbolDictionaryFlags
 * JD-Core Version:    0.6.2
 */