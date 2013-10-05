package org.jpedal.render.output;

public abstract interface FontMapper
{
  public static final int FAIL_ON_UNMAPPED = 2;
  public static final int DEFAULT_ON_UNMAPPED = 3;
  public static final int EMBED_ALL = 6;
  public static final int EMBED_ALL_EXCEPT_BASE_FAMILIES = 7;

  public abstract String getFont(boolean paramBoolean);

  public abstract String getWeight();

  public abstract String getStyle();
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.render.output.FontMapper
 * JD-Core Version:    0.6.2
 */