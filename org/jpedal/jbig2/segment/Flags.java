package org.jpedal.jbig2.segment;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class Flags
{
  protected final Map<String, Integer> flags = new LinkedHashMap();

  public int getFlagValue(String paramString)
  {
    return ((Integer)this.flags.get(paramString)).intValue();
  }

  public abstract void setFlags(int paramInt);
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.jbig2.segment.Flags
 * JD-Core Version:    0.6.2
 */