package org.jpedal.jbig2.segment.region.refinement;

import java.io.IOException;
import org.jpedal.jbig2.JBIG2Exception;
import org.jpedal.jbig2.decoders.ArithmeticDecoder;
import org.jpedal.jbig2.decoders.JBIG2StreamDecoder;
import org.jpedal.jbig2.image.JBIG2Bitmap;
import org.jpedal.jbig2.segment.SegmentHeader;
import org.jpedal.jbig2.segment.pageinformation.PageInformationSegment;
import org.jpedal.jbig2.segment.region.RegionFlags;
import org.jpedal.jbig2.segment.region.RegionSegment;

public class RefinementRegionSegment extends RegionSegment
{
  private final RefinementRegionFlags refinementRegionFlags = new RefinementRegionFlags();
  private final boolean inlineImage;
  private final int noOfReferredToSegments;
  private final int[] referredToSegments;

  public RefinementRegionSegment(JBIG2StreamDecoder paramJBIG2StreamDecoder, boolean paramBoolean, int[] paramArrayOfInt, int paramInt)
  {
    super(paramJBIG2StreamDecoder);
    this.inlineImage = paramBoolean;
    this.referredToSegments = paramArrayOfInt;
    this.noOfReferredToSegments = paramInt;
  }

  public void readSegment()
    throws IOException, JBIG2Exception
  {
    super.readSegment();
    readGenericRegionFlags();
    short[] arrayOfShort1 = new short[2];
    short[] arrayOfShort2 = new short[2];
    int i = this.refinementRegionFlags.getFlagValue("GR_TEMPLATE");
    if (i == 0)
    {
      arrayOfShort1[0] = readATValue();
      arrayOfShort2[0] = readATValue();
      arrayOfShort1[1] = readATValue();
      arrayOfShort2[1] = readATValue();
    }
    Object localObject1;
    Object localObject2;
    if ((this.noOfReferredToSegments == 0) || (this.inlineImage))
    {
      localObject1 = this.decoder.findPageSegment(this.segmentHeader.getPageAssociation());
      localObject2 = ((PageInformationSegment)localObject1).getPageBitmap();
      if ((((PageInformationSegment)localObject1).getPageBitmapHeight() == -1) && (this.regionBitmapYLocation + this.regionBitmapHeight > ((JBIG2Bitmap)localObject2).getHeight()))
        ((JBIG2Bitmap)localObject2).expand(this.regionBitmapYLocation + this.regionBitmapHeight);
    }
    if (this.noOfReferredToSegments > 1)
      return;
    if (this.noOfReferredToSegments == 1)
    {
      localObject1 = this.decoder.findBitmap(this.referredToSegments[0]);
    }
    else
    {
      localObject2 = this.decoder.findPageSegment(this.segmentHeader.getPageAssociation());
      localJBIG2Bitmap1 = ((PageInformationSegment)localObject2).getPageBitmap();
      localObject1 = localJBIG2Bitmap1.getSlice(this.regionBitmapXLocation, this.regionBitmapYLocation, this.regionBitmapWidth, this.regionBitmapHeight);
    }
    this.arithmeticDecoder.resetRefinementStats(i, null);
    this.arithmeticDecoder.start();
    boolean bool = this.refinementRegionFlags.getFlagValue("TPGDON") != 0;
    JBIG2Bitmap localJBIG2Bitmap1 = new JBIG2Bitmap(this.regionBitmapWidth, this.regionBitmapHeight, this.arithmeticDecoder, this.huffmanDecoder, this.mmrDecoder);
    localJBIG2Bitmap1.readGenericRefinementRegion(i, bool, (JBIG2Bitmap)localObject1, 0, 0, arrayOfShort1, arrayOfShort2);
    if (this.inlineImage)
    {
      PageInformationSegment localPageInformationSegment = this.decoder.findPageSegment(this.segmentHeader.getPageAssociation());
      JBIG2Bitmap localJBIG2Bitmap2 = localPageInformationSegment.getPageBitmap();
      int j = this.regionFlags.getFlagValue("EXTERNAL_COMBINATION_OPERATOR");
      localJBIG2Bitmap2.combine(localJBIG2Bitmap1, this.regionBitmapXLocation, this.regionBitmapYLocation, j);
    }
    else
    {
      localJBIG2Bitmap1.setBitmapNumber(getSegmentHeader().getSegmentNumber());
      this.decoder.appendBitmap(localJBIG2Bitmap1);
    }
  }

  private void readGenericRegionFlags()
  {
    int i = this.decoder.readByte();
    this.refinementRegionFlags.setFlags(i);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.jbig2.segment.region.refinement.RefinementRegionSegment
 * JD-Core Version:    0.6.2
 */