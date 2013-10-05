package org.jpedal.jbig2.segment.region.halftone;

import java.io.IOException;
import java.io.PrintStream;
import org.jpedal.jbig2.JBIG2Exception;
import org.jpedal.jbig2.decoders.ArithmeticDecoder;
import org.jpedal.jbig2.decoders.JBIG2StreamDecoder;
import org.jpedal.jbig2.image.JBIG2Bitmap;
import org.jpedal.jbig2.segment.Segment;
import org.jpedal.jbig2.segment.SegmentHeader;
import org.jpedal.jbig2.segment.pageinformation.PageInformationSegment;
import org.jpedal.jbig2.segment.pattern.PatternDictionarySegment;
import org.jpedal.jbig2.segment.region.RegionFlags;
import org.jpedal.jbig2.segment.region.RegionSegment;
import org.jpedal.jbig2.util.BinaryOperation;

public class HalftoneRegionSegment extends RegionSegment
{
  private final HalftoneRegionFlags halftoneRegionFlags = new HalftoneRegionFlags();
  private final boolean inlineImage;

  public HalftoneRegionSegment(JBIG2StreamDecoder paramJBIG2StreamDecoder, boolean paramBoolean)
  {
    super(paramJBIG2StreamDecoder);
    this.inlineImage = paramBoolean;
  }

  public void readSegment()
    throws IOException, JBIG2Exception
  {
    super.readSegment();
    readHalftoneRegionFlags();
    short[] arrayOfShort1 = new short[4];
    this.decoder.readByte(arrayOfShort1);
    int i = BinaryOperation.getInt32(arrayOfShort1);
    arrayOfShort1 = new short[4];
    this.decoder.readByte(arrayOfShort1);
    int j = BinaryOperation.getInt32(arrayOfShort1);
    arrayOfShort1 = new short[4];
    this.decoder.readByte(arrayOfShort1);
    int k = BinaryOperation.getInt32(arrayOfShort1);
    arrayOfShort1 = new short[4];
    this.decoder.readByte(arrayOfShort1);
    int m = BinaryOperation.getInt32(arrayOfShort1);
    arrayOfShort1 = new short[2];
    this.decoder.readByte(arrayOfShort1);
    int n = BinaryOperation.getInt16(arrayOfShort1);
    arrayOfShort1 = new short[2];
    this.decoder.readByte(arrayOfShort1);
    int i1 = BinaryOperation.getInt16(arrayOfShort1);
    int[] arrayOfInt1 = this.segmentHeader.getReferredToSegments();
    if (arrayOfInt1.length != 1)
      System.out.println("Error in halftone Segment. referredToSegments should == 1.");
    Segment localSegment = this.decoder.findSegment(arrayOfInt1[0]);
    if (localSegment.getSegmentHeader().getSegmentType() != 16);
    PatternDictionarySegment localPatternDictionarySegment = (PatternDictionarySegment)localSegment;
    int i2 = 0;
    int i3 = 1;
    while (i3 < localPatternDictionarySegment.getSize())
    {
      i2++;
      i3 <<= 1;
    }
    JBIG2Bitmap localJBIG2Bitmap1 = localPatternDictionarySegment.getBitmaps()[0];
    int i4 = localJBIG2Bitmap1.getWidth();
    int i5 = localJBIG2Bitmap1.getHeight();
    boolean bool1 = this.halftoneRegionFlags.getFlagValue("H_MMR") != 0;
    int i6 = this.halftoneRegionFlags.getFlagValue("H_TEMPLATE");
    if (!bool1)
    {
      this.arithmeticDecoder.resetGenericStats(i6, null);
      this.arithmeticDecoder.start();
    }
    int i7 = this.halftoneRegionFlags.getFlagValue("H_DEF_PIXEL");
    localJBIG2Bitmap1 = new JBIG2Bitmap(this.regionBitmapWidth, this.regionBitmapHeight, this.arithmeticDecoder, this.huffmanDecoder, this.mmrDecoder);
    localJBIG2Bitmap1.clear(i7);
    boolean bool2 = this.halftoneRegionFlags.getFlagValue("H_ENABLE_SKIP") != 0;
    JBIG2Bitmap localJBIG2Bitmap2 = null;
    if (bool2)
    {
      localJBIG2Bitmap2 = new JBIG2Bitmap(i, j, this.arithmeticDecoder, this.huffmanDecoder, this.mmrDecoder);
      localJBIG2Bitmap2.clear(0);
      for (int i8 = 0; i8 < j; i8++)
        for (int i9 = 0; i9 < i; i9++)
        {
          int i10 = k + i8 * i1 + i9 * n;
          int i11 = m + i8 * n - i9 * i1;
          if ((i10 + i4 >> 8 <= 0) || (i10 >> 8 >= this.regionBitmapWidth) || (i11 + i5 >> 8 <= 0) || (i11 >> 8 >= this.regionBitmapHeight))
            localJBIG2Bitmap2.setPixel(i8, i9, 1);
        }
    }
    int[] arrayOfInt2 = new int[i * j];
    short[] arrayOfShort2 = new short[4];
    short[] arrayOfShort3 = new short[4];
    arrayOfShort2[0] = ((short)(i6 <= 1 ? 3 : 2));
    arrayOfShort3[0] = -1;
    arrayOfShort2[1] = -3;
    arrayOfShort3[1] = -1;
    arrayOfShort2[2] = 2;
    arrayOfShort3[2] = -2;
    arrayOfShort2[3] = -2;
    arrayOfShort3[3] = -2;
    int i14;
    int i15;
    for (int i12 = i2 - 1; i12 >= 0; i12--)
    {
      JBIG2Bitmap localJBIG2Bitmap3 = new JBIG2Bitmap(i, j, this.arithmeticDecoder, this.huffmanDecoder, this.mmrDecoder);
      localJBIG2Bitmap3.readBitmap(bool1, i6, false, bool2, localJBIG2Bitmap2, arrayOfShort2, arrayOfShort3, -1);
      i3 = 0;
      for (i13 = 0; i13 < j; i13++)
        for (i14 = 0; i14 < i; i14++)
        {
          i15 = localJBIG2Bitmap3.getPixel(i14, i13) ^ arrayOfInt2[i3] & 0x1;
          arrayOfInt2[i3] = (arrayOfInt2[i3] << 1 | i15);
          i3++;
        }
    }
    i12 = this.halftoneRegionFlags.getFlagValue("H_COMB_OP");
    i3 = 0;
    for (int i13 = 0; i13 < j; i13++)
    {
      i14 = k + i13 * i1;
      i15 = m + i13 * n;
      for (int i16 = 0; i16 < i; i16++)
      {
        if ((!bool2) || (localJBIG2Bitmap2.getPixel(i13, i16) != 1))
        {
          JBIG2Bitmap localJBIG2Bitmap5 = localPatternDictionarySegment.getBitmaps()[arrayOfInt2[i3]];
          localJBIG2Bitmap1.combine(localJBIG2Bitmap5, i14 >> 8, i15 >> 8, i12);
        }
        i14 += n;
        i15 -= i1;
        i3++;
      }
    }
    if (this.inlineImage)
    {
      PageInformationSegment localPageInformationSegment = this.decoder.findPageSegment(this.segmentHeader.getPageAssociation());
      JBIG2Bitmap localJBIG2Bitmap4 = localPageInformationSegment.getPageBitmap();
      i15 = this.regionFlags.getFlagValue("EXTERNAL_COMBINATION_OPERATOR");
      localJBIG2Bitmap4.combine(localJBIG2Bitmap1, this.regionBitmapXLocation, this.regionBitmapYLocation, i15);
    }
    else
    {
      localJBIG2Bitmap1.setBitmapNumber(getSegmentHeader().getSegmentNumber());
      this.decoder.appendBitmap(localJBIG2Bitmap1);
    }
  }

  private void readHalftoneRegionFlags()
  {
    int i = this.decoder.readByte();
    this.halftoneRegionFlags.setFlags(i);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.jbig2.segment.region.halftone.HalftoneRegionSegment
 * JD-Core Version:    0.6.2
 */