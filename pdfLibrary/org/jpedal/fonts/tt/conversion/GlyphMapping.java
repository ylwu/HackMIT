package org.jpedal.fonts.tt.conversion;

public class GlyphMapping
{
  private String inputChar;
  private String outputChar;
  private int glyphNumber;
  private boolean isProper;
  private int useCount = 0;

  public GlyphMapping(String paramString1, int paramInt, String paramString2, boolean paramBoolean)
  {
    this.inputChar = paramString1;
    this.glyphNumber = paramInt;
    this.outputChar = paramString2;
    this.isProper = paramBoolean;
    this.useCount += 1;
  }

  public void use()
  {
    this.useCount += 1;
  }

  public int getGlyphNumber()
  {
    return this.glyphNumber;
  }

  public String getOutputChar()
  {
    return this.outputChar;
  }

  public String toString()
  {
    return this.outputChar + ": " + this.glyphNumber;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.fonts.tt.conversion.GlyphMapping
 * JD-Core Version:    0.6.2
 */