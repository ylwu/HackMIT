package org.jpedal.display;

import org.jpedal.objects.PdfPageData;

public class PageOffsets
{
  public int totalSingleWidth = 0;
  public int totalDoubleWidth = 0;
  public int gaps = 0;
  public int doubleGaps = 0;
  public int totalSingleHeight = 0;
  public int totalDoubleHeight = 0;
  protected int maxW = 0;
  protected int maxH = 0;
  public static final int pageGap = 10;
  public int doublePageWidth = 0;
  public int doublePageHeight = 0;
  public int biggestWidth = 0;
  public int biggestHeight = 0;
  public int widestPageNR;
  public int widestPageR;

  public PageOffsets(int paramInt, PdfPageData paramPdfPageData)
  {
    int m = 0;
    int n = 0;
    int i1 = 0;
    int i2 = 0;
    this.totalSingleHeight = 0;
    this.totalSingleWidth = 0;
    int i3 = 0;
    int i4 = 0;
    int i5 = 0;
    int i6 = 0;
    this.widestPageR = 0;
    this.widestPageNR = 0;
    this.totalDoubleWidth = 0;
    this.totalDoubleHeight = 0;
    this.gaps = 0;
    this.doubleGaps = 0;
    this.biggestWidth = 0;
    this.biggestHeight = 0;
    for (int i7 = 1; i7 < paramInt + 1; i7++)
    {
      int j = paramPdfPageData.getCropBoxWidth(i7);
      int i = paramPdfPageData.getCropBoxHeight(i7);
      int k = paramPdfPageData.getRotation(i7);
      if ((k == 90) || (k == 270))
      {
        int i8 = j;
        j = i;
        i = i8;
      }
      if (j > this.maxW)
        this.maxW = j;
      if (i > this.maxH)
        this.maxH = i;
      this.gaps += 10;
      this.totalSingleWidth += j;
      this.totalSingleHeight += i;
      if ((i7 & 0x1) == 1)
      {
        if (i4 < j)
          i4 = j;
        if (i6 < i)
          i6 = i;
      }
      else
      {
        if (i3 < j)
          i3 = j;
        if (i5 < i)
          i5 = i;
      }
      if (this.widestPageNR < j)
        this.widestPageNR = j;
      if (this.widestPageR < i)
        this.widestPageR = i;
      if (j > this.biggestWidth)
        this.biggestWidth = j;
      if (i > this.biggestHeight)
        this.biggestHeight = i;
      if ((i7 & 0x1) == 1)
      {
        if (i1 < j)
          i1 = j;
        if (i2 < i)
          i2 = i;
        if (i7 == 1)
        {
          this.totalDoubleWidth = j;
          this.totalDoubleHeight = i;
        }
        else
        {
          this.totalDoubleWidth += i1;
          this.totalDoubleHeight += i2;
        }
        this.doubleGaps += 10;
        m = j;
        n = i;
      }
      else
      {
        m += j;
        n += i;
        i1 = j;
        i2 = i;
        if (i7 == paramInt)
        {
          this.totalDoubleWidth = (this.totalDoubleWidth + i1 + 10);
          this.totalDoubleHeight = (this.totalDoubleHeight + i2 + 10);
        }
      }
    }
    this.doublePageWidth = (i3 + i4 + 10);
    this.doublePageHeight = (i5 + i6 + 10);
    this.totalSingleWidth -= 10;
    this.totalSingleHeight -= 10;
  }

  public int getMaxH()
  {
    return this.maxH;
  }

  public int getMaxW()
  {
    return this.maxW;
  }

  public int getWidestPageR()
  {
    return this.widestPageR;
  }

  public int getWidestPageNR()
  {
    return this.widestPageNR;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.display.PageOffsets
 * JD-Core Version:    0.6.2
 */