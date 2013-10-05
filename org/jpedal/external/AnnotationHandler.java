package org.jpedal.external;

import java.util.Map;
import org.jpedal.PdfDecoder;
import org.jpedal.examples.viewer.Values;
import org.jpedal.examples.viewer.gui.SwingGUI;
import org.jpedal.io.PdfObjectReader;

public abstract interface AnnotationHandler
{
  public abstract void handleAnnotations(PdfDecoder paramPdfDecoder, Map paramMap, int paramInt);

  public abstract void checkLinks(Map paramMap, boolean paramBoolean, PdfObjectReader paramPdfObjectReader, int paramInt1, int paramInt2, SwingGUI paramSwingGUI, Values paramValues);
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.external.AnnotationHandler
 * JD-Core Version:    0.6.2
 */