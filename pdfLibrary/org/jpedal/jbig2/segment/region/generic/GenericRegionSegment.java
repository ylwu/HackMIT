package org.jpedal.jbig2.segment.region.generic;

import java.io.IOException;
import org.jpedal.jbig2.JBIG2Exception;
import org.jpedal.jbig2.decoders.ArithmeticDecoder;
import org.jpedal.jbig2.decoders.JBIG2StreamDecoder;
import org.jpedal.jbig2.image.JBIG2Bitmap;
import org.jpedal.jbig2.segment.SegmentHeader;
import org.jpedal.jbig2.segment.pageinformation.PageInformationSegment;
import org.jpedal.jbig2.segment.region.RegionFlags;
import org.jpedal.jbig2.segment.region.RegionSegment;

public class GenericRegionSegment extends RegionSegment
{
  private final GenericRegionFlags genericRegionFlags = new GenericRegionFlags();
  private final boolean inlineImage;
  private boolean unknownLength = false;

  public GenericRegionSegment(JBIG2StreamDecoder paramJBIG2StreamDecoder, boolean paramBoolean)
  {
    super(paramJBIG2StreamDecoder);
    this.inlineImage = paramBoolean;
  }

  public void readSegment()
    throws IOException, JBIG2Exception
  {
    super.readSegment();
    readGenericRegionFlags();
    boolean bool1 = this.genericRegionFlags.getFlagValue("MMR") != 0;
    int i = this.genericRegionFlags.getFlagValue("GB_TEMPLATE");
    short[] arrayOfShort1 = new short[4];
    short[] arrayOfShort2 = new short[4];
    if (!bool1)
    {
      if (i == 0)
      {
        arrayOfShort1[0] = readATValue();
        arrayOfShort2[0] = readATValue();
        arrayOfShort1[1] = readATValue();
        arrayOfShort2[1] = readATValue();
        arrayOfShort1[2] = readATValue();
        arrayOfShort2[2] = readATValue();
        arrayOfShort1[3] = readATValue();
        arrayOfShort2[3] = readATValue();
      }
      else
      {
        arrayOfShort1[0] = readATValue();
        arrayOfShort2[0] = readATValue();
      }
      this.arithmeticDecoder.resetGenericStats(i, null);
      this.arithmeticDecoder.start();
    }
    boolean bool2 = this.genericRegionFlags.getFlagValue("TPGDON") != 0;
    int j = this.segmentHeader.getSegmentDataLength();
    int i1;
    if (j == -1)
    {
      this.unknownLength = true;
      int k;
      int m;
      if (bool1)
      {
        k = 0;
        m = 0;
      }
      else
      {
        k = 255;
        m = 172;
      }
      int n = 0;
      while (true)
      {
        i1 = this.decoder.readByte();
        n++;
        if (i1 == k)
        {
          int i2 = this.decoder.readByte();
          n++;
          if (i2 == m)
          {
            j = n - 2;
            break;
          }
        }
      }
      this.decoder.movePointer(-n);
    }
    JBIG2Bitmap localJBIG2Bitmap1 = new JBIG2Bitmap(this.regionBitmapWidth, this.regionBitmapHeight, this.arithmeticDecoder, this.huffmanDecoder, this.mmrDecoder);
    localJBIG2Bitmap1.clear(0);
    localJBIG2Bitmap1.readBitmap(bool1, i, bool2, false, null, arrayOfShort1, arrayOfShort2, bool1 ? 0 : j - 18);
    if (this.inlineImage)
    {
      PageInformationSegment localPageInformationSegment = this.decoder.findPageSegment(this.segmentHeader.getPageAssociation());
      JBIG2Bitmap localJBIG2Bitmap2 = localPageInformationSegment.getPageBitmap();
      i1 = this.regionFlags.getFlagValue("EXTERNAL_COMBINATION_OPERATOR");
      if ((localPageInformationSegment.getPageBitmapHeight() == -1) && (this.regionBitmapYLocation + this.regionBitmapHeight > localJBIG2Bitmap2.getHeight()))
        localJBIG2Bitmap2.expand(this.regionBitmapYLocation + this.regionBitmapHeight);
      localJBIG2Bitmap2.combine(localJBIG2Bitmap1, this.regionBitmapXLocation, this.regionBitmapYLocation, i1);
    }
    else
    {
      localJBIG2Bitmap1.setBitmapNumber(getSegmentHeader().getSegmentNumber());
      this.decoder.appendBitmap(localJBIG2Bitmap1);
    }
    if (this.unknownLength)
      this.decoder.movePointer(4);
  }

  private void readGenericRegionFlags()
  {
    int i = this.decoder.readByte();
    this.genericRegionFlags.setFlags(i);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.jbig2.segment.region.generic.GenericRegionSegment
 * JD-Core Version:    0.6.2
 */