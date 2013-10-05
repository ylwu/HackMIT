package org.jpedal.jbig2.segment.pageinformation;

import java.io.IOException;
import org.jpedal.jbig2.JBIG2Exception;
import org.jpedal.jbig2.decoders.JBIG2StreamDecoder;
import org.jpedal.jbig2.image.JBIG2Bitmap;
import org.jpedal.jbig2.segment.Segment;
import org.jpedal.jbig2.util.BinaryOperation;

public class PageInformationSegment extends Segment
{
  private int pageBitmapHeight;
  private final PageInformationFlags pageInformationFlags = new PageInformationFlags();
  private JBIG2Bitmap pageBitmap;

  public PageInformationSegment(JBIG2StreamDecoder paramJBIG2StreamDecoder)
  {
    super(paramJBIG2StreamDecoder);
  }

  public JBIG2Bitmap getPageBitmap()
  {
    return this.pageBitmap;
  }

  public void readSegment()
    throws IOException, JBIG2Exception
  {
    short[] arrayOfShort = new short[4];
    this.decoder.readByte(arrayOfShort);
    int i = BinaryOperation.getInt32(arrayOfShort);
    arrayOfShort = new short[4];
    this.decoder.readByte(arrayOfShort);
    this.pageBitmapHeight = BinaryOperation.getInt32(arrayOfShort);
    arrayOfShort = new short[4];
    this.decoder.readByte(arrayOfShort);
    int j = BinaryOperation.getInt32(arrayOfShort);
    arrayOfShort = new short[4];
    this.decoder.readByte(arrayOfShort);
    int k = BinaryOperation.getInt32(arrayOfShort);
    int m = this.decoder.readByte();
    this.pageInformationFlags.setFlags(m);
    arrayOfShort = new short[2];
    this.decoder.readByte(arrayOfShort);
    int n = BinaryOperation.getInt16(arrayOfShort);
    int i1 = this.pageInformationFlags.getFlagValue("DEFAULT_PIXEL_VALUE");
    int i2;
    if (this.pageBitmapHeight == -1)
      i2 = n & 0x7FFF;
    else
      i2 = this.pageBitmapHeight;
    this.pageBitmap = new JBIG2Bitmap(i, i2, this.arithmeticDecoder, this.huffmanDecoder, this.mmrDecoder);
    this.pageBitmap.clear(i1);
  }

  public int getPageBitmapHeight()
  {
    return this.pageBitmapHeight;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.jbig2.segment.pageinformation.PageInformationSegment
 * JD-Core Version:    0.6.2
 */