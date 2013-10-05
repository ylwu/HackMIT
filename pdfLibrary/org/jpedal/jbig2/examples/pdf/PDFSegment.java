package org.jpedal.jbig2.examples.pdf;

import java.io.ByteArrayOutputStream;

class PDFSegment
{
  private final ByteArrayOutputStream header = new ByteArrayOutputStream();
  private final ByteArrayOutputStream data = new ByteArrayOutputStream();
  private int segmentDataLength;

  public void writeToHeader(short paramShort)
  {
    this.header.write(paramShort);
  }

  public void writeToHeader(short[] paramArrayOfShort)
  {
    for (int k : paramArrayOfShort)
      this.header.write(k);
  }

  public void writeToData(short paramShort)
  {
    this.data.write(paramShort);
  }

  public ByteArrayOutputStream getHeader()
  {
    return this.header;
  }

  public ByteArrayOutputStream getData()
  {
    return this.data;
  }

  public void setDataLength(int paramInt)
  {
    this.segmentDataLength = paramInt;
  }

  public int getSegmentDataLength()
  {
    return this.segmentDataLength;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.jbig2.examples.pdf.PDFSegment
 * JD-Core Version:    0.6.2
 */