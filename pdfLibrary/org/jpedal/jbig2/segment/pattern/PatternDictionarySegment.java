package org.jpedal.jbig2.segment.pattern;

import java.io.IOException;
import org.jpedal.jbig2.JBIG2Exception;
import org.jpedal.jbig2.decoders.ArithmeticDecoder;
import org.jpedal.jbig2.decoders.JBIG2StreamDecoder;
import org.jpedal.jbig2.image.JBIG2Bitmap;
import org.jpedal.jbig2.segment.Segment;
import org.jpedal.jbig2.segment.SegmentHeader;
import org.jpedal.jbig2.util.BinaryOperation;

public class PatternDictionarySegment extends Segment
{
  private final PatternDictionaryFlags patternDictionaryFlags = new PatternDictionaryFlags();
  private JBIG2Bitmap[] bitmaps;
  private int size;

  public PatternDictionarySegment(JBIG2StreamDecoder paramJBIG2StreamDecoder)
  {
    super(paramJBIG2StreamDecoder);
  }

  public void readSegment()
    throws IOException, JBIG2Exception
  {
    readPatternDictionaryFlags();
    int i = this.decoder.readByte();
    int j = this.decoder.readByte();
    short[] arrayOfShort1 = new short[4];
    this.decoder.readByte(arrayOfShort1);
    int k = BinaryOperation.getInt32(arrayOfShort1);
    boolean bool = this.patternDictionaryFlags.getFlagValue("HD_MMR") == 1;
    int m = this.patternDictionaryFlags.getFlagValue("HD_TEMPLATE");
    if (!bool)
    {
      this.arithmeticDecoder.resetGenericStats(m, null);
      this.arithmeticDecoder.start();
    }
    short[] arrayOfShort2 = new short[4];
    short[] arrayOfShort3 = new short[4];
    arrayOfShort2[0] = ((short)-i);
    arrayOfShort3[0] = 0;
    arrayOfShort2[1] = -3;
    arrayOfShort3[1] = -1;
    arrayOfShort2[2] = 2;
    arrayOfShort3[2] = -2;
    arrayOfShort2[3] = -2;
    arrayOfShort3[3] = -2;
    this.size = (k + 1);
    JBIG2Bitmap localJBIG2Bitmap = new JBIG2Bitmap(this.size * i, j, this.arithmeticDecoder, this.huffmanDecoder, this.mmrDecoder);
    localJBIG2Bitmap.clear(0);
    localJBIG2Bitmap.readBitmap(bool, m, false, false, null, arrayOfShort2, arrayOfShort3, this.segmentHeader.getSegmentDataLength() - 7);
    JBIG2Bitmap[] arrayOfJBIG2Bitmap = new JBIG2Bitmap[this.size];
    int n = 0;
    for (int i1 = 0; i1 < this.size; i1++)
    {
      arrayOfJBIG2Bitmap[i1] = localJBIG2Bitmap.getSlice(n, 0, i, j);
      n += i;
    }
    this.bitmaps = arrayOfJBIG2Bitmap;
  }

  public JBIG2Bitmap[] getBitmaps()
  {
    return this.bitmaps;
  }

  private void readPatternDictionaryFlags()
  {
    int i = this.decoder.readByte();
    this.patternDictionaryFlags.setFlags(i);
  }

  public int getSize()
  {
    return this.size;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.jbig2.segment.pattern.PatternDictionarySegment
 * JD-Core Version:    0.6.2
 */