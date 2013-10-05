package org.jpedal.jbig2.segment.pattern;

import java.util.Map;
import org.jpedal.jbig2.segment.Flags;

public class PatternDictionaryFlags extends Flags
{
  public static final String HD_MMR = "HD_MMR";
  public static final String HD_TEMPLATE = "HD_TEMPLATE";

  public void setFlags(int paramInt)
  {
    this.flags.put("HD_MMR", Integer.valueOf(paramInt & 0x1));
    this.flags.put("HD_TEMPLATE", Integer.valueOf(paramInt >> 1 & 0x3));
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.jbig2.segment.pattern.PatternDictionaryFlags
 * JD-Core Version:    0.6.2
 */