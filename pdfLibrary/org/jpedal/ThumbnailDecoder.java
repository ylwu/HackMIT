package org.jpedal;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import org.jpedal.exception.PdfException;
import org.jpedal.utils.LogWriter;

public class ThumbnailDecoder
{
  private PdfDecoder decode_pdf;

  public ThumbnailDecoder(PdfDecoder paramPdfDecoder)
  {
    this.decode_pdf = paramPdfDecoder;
  }

  public final synchronized BufferedImage getPageAsThumbnail(int paramInt1, int paramInt2)
  {
    BufferedImage localBufferedImage1 = null;
    try
    {
      BufferedImage localBufferedImage2 = this.decode_pdf.getPageAsImage(paramInt1);
      int i = localBufferedImage2.getHeight();
      double d = paramInt2 / i;
      int j = (int)(localBufferedImage2.getWidth() * d);
      localBufferedImage1 = new BufferedImage(j, paramInt2, 2);
      Graphics localGraphics = localBufferedImage1.getGraphics();
      localGraphics.drawImage(localBufferedImage2, 0, 0, j, paramInt2, null);
      localGraphics.dispose();
    }
    catch (PdfException localPdfException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception: " + localPdfException.getMessage());
    }
    return localBufferedImage1;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.ThumbnailDecoder
 * JD-Core Version:    0.6.2
 */