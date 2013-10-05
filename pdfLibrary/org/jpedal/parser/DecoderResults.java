package org.jpedal.parser;

import java.util.Iterator;

public class DecoderResults
{
  private boolean imagesProcessedFully = true;
  private boolean hasNonEmbeddedCIDFonts;
  private boolean hasYCCKimages;
  private boolean pageSuccessful;
  private boolean ttHintingRequired;
  private boolean timeout = false;
  private Iterator colorSpacesUsed;
  private String nonEmbeddedCIDFonts = "";
  private boolean hasEmbeddedFonts = false;

  public boolean getImagesProcessedFully()
  {
    return this.imagesProcessedFully;
  }

  public void update(PdfStreamDecoder paramPdfStreamDecoder, boolean paramBoolean)
  {
    this.colorSpacesUsed = ((Iterator)paramPdfStreamDecoder.getObjectValue(1));
    this.nonEmbeddedCIDFonts = ((String)paramPdfStreamDecoder.getObjectValue(4));
    this.hasYCCKimages = paramPdfStreamDecoder.getBooleanValue(8);
    this.pageSuccessful = paramPdfStreamDecoder.getBooleanValue(1);
    this.imagesProcessedFully = paramPdfStreamDecoder.getBooleanValue(2);
    this.hasNonEmbeddedCIDFonts = paramPdfStreamDecoder.getBooleanValue(4);
    this.ttHintingRequired = paramPdfStreamDecoder.getBooleanValue(32);
    this.timeout = paramPdfStreamDecoder.getBooleanValue(16);
    if (paramBoolean)
      this.hasEmbeddedFonts = paramPdfStreamDecoder.getBooleanValue(-1);
  }

  public void resetTimeout()
  {
    this.timeout = false;
  }

  public boolean getPageDecodeStatus(int paramInt)
  {
    switch (paramInt)
    {
    case 4:
      return this.hasNonEmbeddedCIDFonts;
    case 2:
      return this.imagesProcessedFully;
    case 1:
      return this.pageSuccessful;
    case 16:
      return this.timeout;
    case 8:
      return this.hasYCCKimages;
    case 32:
      return this.ttHintingRequired;
    }
    throw new RuntimeException("Unknown parameter " + paramInt);
  }

  public Iterator getPageInfo(int paramInt)
  {
    switch (paramInt)
    {
    case 1:
      return this.colorSpacesUsed;
    }
    return null;
  }

  public String getPageDecodeStatusReport(int paramInt)
  {
    if (paramInt == 4)
      return this.nonEmbeddedCIDFonts;
    new RuntimeException("Unknown parameter");
    return "";
  }

  public boolean hasEmbeddedFonts()
  {
    return this.hasEmbeddedFonts;
  }

  public void resetColorSpaces()
  {
    this.colorSpacesUsed = null;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.parser.DecoderResults
 * JD-Core Version:    0.6.2
 */