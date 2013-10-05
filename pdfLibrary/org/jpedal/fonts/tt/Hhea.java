package org.jpedal.fonts.tt;

public class Hhea extends Table
{
  public static final int VERSION = 0;
  public static final int ASCENDER = 1;
  public static final int DESCENDER = 2;
  public static final int LINEGAP = 3;
  public static final int ADVANCEWIDTHMAX = 4;
  public static final int MINIMUMLEFTSIDEBEARING = 5;
  public static final int MINIMUMRIGHTSIDEBEARING = 6;
  public static final int XMAXEXTENT = 7;
  public static final int CARETSLOPERISING = 8;
  public static final int CARETSLOPERUN = 9;
  public static final int CARETOFFSET = 10;
  public static final int METRICDATAFORMAT = 11;
  public static final int NUMBEROFMETRICS = 12;
  private int version = 65536;
  private int ascender = 1;
  private int descender = -1;
  private int lineGap = 0;
  private int advancedWidthMax = 0;
  private int minimumLeftSideBearing = 0;
  private int minimumRightSideBearing = 0;
  private int xMaxExtent = 0;
  private int caretSlopeRise = 0;
  private int caretSlopeRun = 0;
  private int caretOffset = 0;
  private int metricDataFormat = 0;
  private int numberOfHMetrics = 0;

  public Hhea(FontFile2 paramFontFile2)
  {
    int i = paramFontFile2.selectTable(5);
    if (i != 0)
    {
      this.version = paramFontFile2.getNextUint32();
      this.ascender = paramFontFile2.getFWord();
      this.descender = paramFontFile2.getFWord();
      this.lineGap = paramFontFile2.getFWord();
      this.advancedWidthMax = paramFontFile2.readUFWord();
      this.minimumLeftSideBearing = paramFontFile2.getFWord();
      this.minimumRightSideBearing = paramFontFile2.getFWord();
      this.xMaxExtent = paramFontFile2.getFWord();
      this.caretSlopeRise = paramFontFile2.getNextInt16();
      this.caretSlopeRun = paramFontFile2.getNextInt16();
      this.caretOffset = paramFontFile2.getFWord();
      for (int j = 0; j < 4; j++)
        paramFontFile2.getNextUint16();
      this.metricDataFormat = paramFontFile2.getNextInt16();
      this.numberOfHMetrics = paramFontFile2.getNextUint16();
    }
  }

  public Hhea()
  {
  }

  public int getNumberOfHMetrics()
  {
    return this.numberOfHMetrics;
  }

  public int getIntValue(int paramInt)
  {
    switch (paramInt)
    {
    case 0:
      return this.version;
    case 1:
      return this.ascender;
    case 2:
      return this.descender;
    case 3:
      return this.lineGap;
    case 4:
      return this.advancedWidthMax;
    case 5:
      return this.minimumLeftSideBearing;
    case 6:
      return this.minimumRightSideBearing;
    case 7:
      return this.xMaxExtent;
    case 8:
      return this.caretSlopeRise;
    case 9:
      return this.caretSlopeRun;
    case 10:
      return this.caretOffset;
    case 11:
      return this.metricDataFormat;
    case 12:
      return this.numberOfHMetrics;
    }
    return 0;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.fonts.tt.Hhea
 * JD-Core Version:    0.6.2
 */