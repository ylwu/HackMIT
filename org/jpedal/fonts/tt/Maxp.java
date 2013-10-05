package org.jpedal.fonts.tt;

public class Maxp extends Table
{
  private int numGlyphs = 0;
  private int maxPoints = 0;
  private int maxContours = 0;
  private int maxTwilightPoints = 0;
  private int maxStorage = 0;

  public Maxp(FontFile2 paramFontFile2)
  {
    init(paramFontFile2);
  }

  public Maxp()
  {
  }

  private void init(FontFile2 paramFontFile2)
  {
    int i = paramFontFile2.selectTable(1);
    if (i != 0)
    {
      paramFontFile2.getNextUint32();
      this.numGlyphs = paramFontFile2.getNextUint16();
      this.maxPoints = paramFontFile2.getNextUint16();
      this.maxContours = paramFontFile2.getNextUint16();
      paramFontFile2.getNextUint16();
      paramFontFile2.getNextUint16();
      paramFontFile2.getNextUint16();
      this.maxTwilightPoints = paramFontFile2.getNextUint16();
      this.maxStorage = paramFontFile2.getNextUint16();
      paramFontFile2.getNextUint16();
      paramFontFile2.getNextUint16();
      paramFontFile2.getNextUint16();
      paramFontFile2.getNextUint16();
      paramFontFile2.getNextUint16();
      paramFontFile2.getNextUint16();
    }
  }

  public int getGlyphCount()
  {
    return this.numGlyphs;
  }

  public int getMaxPoints()
  {
    return this.maxPoints;
  }

  public int getMaxTwilightPoints()
  {
    return this.maxTwilightPoints;
  }

  public int getMaxStorage()
  {
    return this.maxStorage;
  }

  public int getMaxContours()
  {
    return this.maxContours;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.fonts.tt.Maxp
 * JD-Core Version:    0.6.2
 */