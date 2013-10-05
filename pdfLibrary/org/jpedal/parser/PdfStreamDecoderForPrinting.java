package org.jpedal.parser;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import org.jpedal.PdfDecoder;
import org.jpedal.external.ColorHandler;
import org.jpedal.external.CustomPrintHintingHandler;
import org.jpedal.io.ObjectStore;
import org.jpedal.io.PdfObjectReader;
import org.jpedal.objects.layers.PdfLayerList;
import org.jpedal.render.DynamicVectorRenderer;
import org.jpedal.render.SwingDisplay;

public class PdfStreamDecoderForPrinting extends PdfStreamDecoder
{
  public PdfStreamDecoderForPrinting(PdfObjectReader paramPdfObjectReader, boolean paramBoolean, PdfLayerList paramPdfLayerList)
  {
    super(paramPdfObjectReader, paramBoolean, paramPdfLayerList);
    this.isPrinting = true;
  }

  public void print(Graphics2D paramGraphics2D, AffineTransform paramAffineTransform, int paramInt, Rectangle paramRectangle, CustomPrintHintingHandler paramCustomPrintHintingHandler, PdfDecoder paramPdfDecoder)
  {
    if (paramCustomPrintHintingHandler != null)
    {
      this.current.stopG2HintSetting(true);
      paramCustomPrintHintingHandler.preprint(paramGraphics2D, paramPdfDecoder);
    }
    this.current.setPrintPage(paramInt);
    this.current.setCustomColorHandler((ColorHandler)paramPdfDecoder.getExternalHandler(19));
    this.current.setG2(paramGraphics2D);
    this.current.paint(null, paramAffineTransform, paramRectangle);
  }

  public void setObjectValue(int paramInt, Object paramObject)
  {
    if (paramInt == -8)
    {
      this.objectStoreStreamRef = ((ObjectStore)paramObject);
      this.current = new SwingDisplay(this.pageNum, this.objectStoreStreamRef, true);
      this.current.setHiResImageForDisplayMode(this.useHiResImageForDisplay);
      if ((this.customImageHandler != null) && (this.current != null))
        this.current.setCustomImageHandler(this.customImageHandler);
    }
    else
    {
      super.setObjectValue(paramInt, paramObject);
    }
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.parser.PdfStreamDecoderForPrinting
 * JD-Core Version:    0.6.2
 */