package org.jpedal.parser;

import org.jpedal.exception.PdfException;
import org.jpedal.external.ExternalHandlers;
import org.jpedal.io.ErrorTracker;
import org.jpedal.io.PdfFileReader;
import org.jpedal.io.PdfObjectReader;
import org.jpedal.objects.GraphicsState;
import org.jpedal.objects.raw.PdfObject;

public class PdfStreamDecoderForSampling extends PdfStreamDecoder
{
  public PdfStreamDecoderForSampling(PdfObjectReader paramPdfObjectReader)
  {
    super(paramPdfObjectReader);
  }

  public final float decodePageContentForImageSampling(PdfObject paramPdfObject)
    throws PdfException
  {
    try
    {
      this.renderDirectly = true;
      this.imagesProcessedFully = true;
      this.imageCount = 0;
      this.gs = new GraphicsState(false, 0, 0);
      byte[][] arrayOfByte1 = (byte[][])null;
      if (paramPdfObject != null)
      {
        arrayOfByte1 = paramPdfObject.getKeyArray(1216184967);
        this.isDataValid = paramPdfObject.streamMayBeCorrupt();
      }
      byte[] arrayOfByte;
      if ((paramPdfObject != null) && (arrayOfByte1 == null))
        arrayOfByte = this.currentPdfFile.readStream(paramPdfObject, true, true, false, false, false, paramPdfObject.getCacheName(this.currentPdfFile.getObjectReader()));
      else if (this.pageStream != null)
        arrayOfByte = this.pageStream;
      else
        arrayOfByte = this.currentPdfFile.getObjectReader().readPageIntoStream(paramPdfObject);
      if ((arrayOfByte != null) && (arrayOfByte.length > 0))
      {
        this.getSamplingOnly = true;
        decodeStreamIntoObjects(arrayOfByte, false);
      }
      this.cache.resetFonts();
      return this.samplingUsed;
    }
    catch (Error localError)
    {
      this.errorTracker.addPageFailureMessage("Problem decoding page " + localError);
      if ((ExternalHandlers.throwMissingCIDError) && (localError.getMessage().contains("kochi")))
        throw localError;
    }
    return -1.0F;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.parser.PdfStreamDecoderForSampling
 * JD-Core Version:    0.6.2
 */