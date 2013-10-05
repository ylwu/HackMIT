package org.jpedal.jbig2.segment;

public class SegmentHeader
{
  private int segmentNumber;
  private int segmentType;
  private boolean pageAssociationSizeSet;
  private int referredToSegmentCount;
  private int[] referredToSegments;
  private int pageAssociation;
  private int dataLength;

  public void setSegmentNumber(int paramInt)
  {
    this.segmentNumber = paramInt;
  }

  public void setSegmentHeaderFlags(short paramShort)
  {
    this.segmentType = (paramShort & 0x3F);
    this.pageAssociationSizeSet = ((paramShort & 0x40) == 64);
    int i = (paramShort & 0x50) == 80 ? 1 : 0;
  }

  public void setReferredToSegmentCount(int paramInt)
  {
    this.referredToSegmentCount = paramInt;
  }

  public void setReferredToSegments(int[] paramArrayOfInt)
  {
    this.referredToSegments = paramArrayOfInt;
  }

  public int[] getReferredToSegments()
  {
    return this.referredToSegments;
  }

  public int getSegmentType()
  {
    return this.segmentType;
  }

  public int getSegmentNumber()
  {
    return this.segmentNumber;
  }

  public boolean isPageAssociationSizeSet()
  {
    return this.pageAssociationSizeSet;
  }

  public int getReferredToSegmentCount()
  {
    return this.referredToSegmentCount;
  }

  public int getPageAssociation()
  {
    return this.pageAssociation;
  }

  public void setPageAssociation(int paramInt)
  {
    this.pageAssociation = paramInt;
  }

  public void setDataLength(int paramInt)
  {
    this.dataLength = paramInt;
  }

  public int getSegmentDataLength()
  {
    return this.dataLength;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.jbig2.segment.SegmentHeader
 * JD-Core Version:    0.6.2
 */