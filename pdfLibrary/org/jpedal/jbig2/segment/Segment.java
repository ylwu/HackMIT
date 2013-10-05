package org.jpedal.jbig2.segment;

import java.io.IOException;
import org.jpedal.jbig2.JBIG2Exception;
import org.jpedal.jbig2.decoders.ArithmeticDecoder;
import org.jpedal.jbig2.decoders.HuffmanDecoder;
import org.jpedal.jbig2.decoders.JBIG2StreamDecoder;
import org.jpedal.jbig2.decoders.MMRDecoder;

public abstract class Segment
{
  public static final int SYMBOL_DICTIONARY = 0;
  public static final int INTERMEDIATE_TEXT_REGION = 4;
  public static final int IMMEDIATE_TEXT_REGION = 6;
  public static final int IMMEDIATE_LOSSLESS_TEXT_REGION = 7;
  public static final int PATTERN_DICTIONARY = 16;
  public static final int INTERMEDIATE_HALFTONE_REGION = 20;
  public static final int IMMEDIATE_HALFTONE_REGION = 22;
  public static final int IMMEDIATE_LOSSLESS_HALFTONE_REGION = 23;
  public static final int INTERMEDIATE_GENERIC_REGION = 36;
  public static final int IMMEDIATE_GENERIC_REGION = 38;
  public static final int IMMEDIATE_LOSSLESS_GENERIC_REGION = 39;
  public static final int INTERMEDIATE_GENERIC_REFINEMENT_REGION = 40;
  public static final int IMMEDIATE_GENERIC_REFINEMENT_REGION = 42;
  public static final int IMMEDIATE_LOSSLESS_GENERIC_REFINEMENT_REGION = 43;
  public static final int PAGE_INFORMATION = 48;
  public static final int END_OF_PAGE = 49;
  public static final int END_OF_STRIPE = 50;
  public static final int END_OF_FILE = 51;
  public static final int PROFILES = 52;
  public static final int TABLES = 53;
  public static final int EXTENSION = 62;
  protected SegmentHeader segmentHeader;
  protected final HuffmanDecoder huffmanDecoder;
  protected final ArithmeticDecoder arithmeticDecoder;
  protected final MMRDecoder mmrDecoder;
  protected final JBIG2StreamDecoder decoder;

  protected Segment(JBIG2StreamDecoder paramJBIG2StreamDecoder)
  {
    this.decoder = paramJBIG2StreamDecoder;
    this.huffmanDecoder = this.decoder.getHuffmanDecoder();
    this.arithmeticDecoder = this.decoder.getArithmeticDecoder();
    this.mmrDecoder = this.decoder.getMMRDecoder();
  }

  protected short readATValue()
  {
    short s;
    int i = s = this.decoder.readByte();
    if ((i & 0x80) != 0)
      s = (short)(s | 0xFFFFFF00);
    return s;
  }

  public SegmentHeader getSegmentHeader()
  {
    return this.segmentHeader;
  }

  public void setSegmentHeader(SegmentHeader paramSegmentHeader)
  {
    this.segmentHeader = paramSegmentHeader;
  }

  public abstract void readSegment()
    throws IOException, JBIG2Exception;
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.jbig2.segment.Segment
 * JD-Core Version:    0.6.2
 */