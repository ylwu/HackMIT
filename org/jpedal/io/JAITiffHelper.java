package org.jpedal.io;

import com.sun.media.jai.codec.FileSeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.media.jai.InterpolationBicubic;
import javax.media.jai.JAI;
import javax.media.jai.NullOpImage;
import javax.media.jai.RenderedOp;
import org.jpedal.utils.LogWriter;

public class JAITiffHelper
{
  private ImageDecoder dec;
  private int pageCount = 0;

  public JAITiffHelper(String paramString)
  {
    try
    {
      File localFile = new File(paramString);
      FileSeekableStream localFileSeekableStream = new FileSeekableStream(localFile);
      this.dec = ImageCodec.createImageDecoder("tiff", localFileSeekableStream, null);
      this.pageCount = this.dec.getNumPages();
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception: " + localException.getMessage());
    }
  }

  public int getTiffPageCount()
  {
    return this.pageCount;
  }

  public BufferedImage getImage(int paramInt)
  {
    BufferedImage localBufferedImage = null;
    try
    {
      NullOpImage localNullOpImage = new NullOpImage(this.dec.decodeAsRenderedImage(paramInt), null, null, 2);
      localBufferedImage = JAI.create("affine", localNullOpImage, null, new InterpolationBicubic(1)).getAsBufferedImage();
      localBufferedImage = ColorSpaceConvertor.convertColorspace(localBufferedImage, 10);
    }
    catch (IOException localIOException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception: " + localIOException.getMessage());
    }
    return localBufferedImage;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.io.JAITiffHelper
 * JD-Core Version:    0.6.2
 */