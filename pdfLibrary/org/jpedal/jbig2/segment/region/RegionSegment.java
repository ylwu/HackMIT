package org.jpedal.jbig2.segment.region;

import java.io.IOException;
import org.jpedal.jbig2.JBIG2Exception;
import org.jpedal.jbig2.decoders.JBIG2StreamDecoder;
import org.jpedal.jbig2.segment.Segment;
import org.jpedal.jbig2.util.BinaryOperation;

public abstract class RegionSegment extends Segment
{
  protected int regionBitmapWidth;
  protected int regionBitmapHeight;
  protected int regionBitmapXLocation;
  protected int regionBitmapYLocation;
  protected final RegionFlags regionFlags = new RegionFlags();

  protected RegionSegment(JBIG2StreamDecoder paramJBIG2StreamDecoder)
  {
    super(paramJBIG2StreamDecoder);
  }

  public void readSegment()
    throws IOException, JBIG2Exception
  {
    short[] arrayOfShort = new short[4];
    this.decoder.readByte(arrayOfShort);
    this.regionBitmapWidth = BinaryOperation.getInt32(arrayOfShort);
    arrayOfShort = new short[4];
    this.decoder.readByte(arrayOfShort);
    this.regionBitmapHeight = BinaryOperation.getInt32(arrayOfShort);
    arrayOfShort = new short[4];
    this.decoder.readByte(arrayOfShort);
    this.regionBitmapXLocation = BinaryOperation.getInt32(arrayOfShort);
    arrayOfShort = new short[4];
    this.decoder.readByte(arrayOfShort);
    this.regionBitmapYLocation = BinaryOperation.getInt32(arrayOfShort);
    int i = this.decoder.readByte();
    this.regionFlags.setFlags(i);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.jbig2.segment.region.RegionSegment
 * JD-Core Version:    0.6.2
 */