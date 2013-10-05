package org.jpedal.jbig2;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import org.jpedal.jbig2.decoders.JBIG2StreamDecoder;
import org.jpedal.jbig2.image.JBIG2Bitmap;
import org.jpedal.jbig2.segment.Segment;
import org.jpedal.jbig2.segment.SegmentHeader;
import org.jpedal.jbig2.segment.pageinformation.PageInformationSegment;

public class JBIG2Decoder
{
  public static final long INT_MASK = 4294967295L;
  private final JBIG2StreamDecoder streamDecoder = new JBIG2StreamDecoder();

  public void setGlobalData(byte[] paramArrayOfByte)
  {
    this.streamDecoder.setGlobalData(paramArrayOfByte);
  }

  public void decodeJBIG2(File paramFile)
    throws IOException, JBIG2Exception
  {
    decodeJBIG2(paramFile.getAbsolutePath());
  }

  void decodeJBIG2(String paramString)
    throws IOException, JBIG2Exception
  {
    decodeJBIG2(new FileInputStream(paramString));
  }

  void decodeJBIG2(InputStream paramInputStream)
    throws IOException, JBIG2Exception
  {
    int i = paramInputStream.available();
    byte[] arrayOfByte = new byte[i];
    paramInputStream.read(arrayOfByte);
    decodeJBIG2(arrayOfByte);
  }

  public void decodeJBIG2(byte[] paramArrayOfByte)
    throws IOException, JBIG2Exception
  {
    this.streamDecoder.decodeJBIG2(paramArrayOfByte);
  }

  public BufferedImage getPageAsBufferedImage(int paramInt)
  {
    paramInt++;
    JBIG2Bitmap localJBIG2Bitmap = this.streamDecoder.findPageSegment(paramInt).getPageBitmap();
    byte[] arrayOfByte = localJBIG2Bitmap.getWriteSafeData(true);
    if (arrayOfByte == null)
      return null;
    int i = localJBIG2Bitmap.getWidth();
    int j = localJBIG2Bitmap.getHeight();
    BufferedImage localBufferedImage = new BufferedImage(i, j, 12);
    DataBufferByte localDataBufferByte = new DataBufferByte(arrayOfByte, arrayOfByte.length);
    WritableRaster localWritableRaster = Raster.createPackedRaster(localDataBufferByte, i, j, 1, null);
    localBufferedImage.setData(localWritableRaster);
    return localBufferedImage;
  }

  public int getNumberOfPages()
  {
    int i = this.streamDecoder.getNumberOfPages();
    if ((this.streamDecoder.isNumberOfPagesKnown()) && (i != 0))
      return i;
    int j = 0;
    ArrayList localArrayList = getAllSegments();
    Iterator localIterator = localArrayList.iterator();
    while (localIterator.hasNext())
    {
      Segment localSegment = (Segment)localIterator.next();
      if (localSegment.getSegmentHeader().getSegmentType() == 48)
        j++;
    }
    return j;
  }

  ArrayList<Segment> getAllSegments()
  {
    return this.streamDecoder.getAllSegments();
  }

  public JBIG2Bitmap getPageAsJBIG2Bitmap(int paramInt)
  {
    paramInt++;
    return this.streamDecoder.findPageSegment(paramInt).getPageBitmap();
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.jbig2.JBIG2Decoder
 * JD-Core Version:    0.6.2
 */