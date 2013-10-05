package org.jpedal.jbig2.segment.stripes;

import java.io.IOException;
import org.jpedal.jbig2.JBIG2Exception;
import org.jpedal.jbig2.decoders.JBIG2StreamDecoder;
import org.jpedal.jbig2.segment.Segment;
import org.jpedal.jbig2.segment.SegmentHeader;

public class EndOfStripeSegment extends Segment
{
  public EndOfStripeSegment(JBIG2StreamDecoder paramJBIG2StreamDecoder)
  {
    super(paramJBIG2StreamDecoder);
  }

  public void readSegment()
    throws IOException, JBIG2Exception
  {
    for (int i = 0; i < getSegmentHeader().getSegmentDataLength(); i++)
      this.decoder.readByte();
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.jbig2.segment.stripes.EndOfStripeSegment
 * JD-Core Version:    0.6.2
 */