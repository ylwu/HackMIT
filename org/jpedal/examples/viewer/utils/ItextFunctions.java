package org.jpedal.examples.viewer.utils;

import org.jpedal.PdfDecoder;
import org.jpedal.examples.viewer.gui.SwingGUI;
import org.jpedal.examples.viewer.gui.popups.AddHeaderFooterToPDFPages;
import org.jpedal.examples.viewer.gui.popups.CropPDFPages;
import org.jpedal.examples.viewer.gui.popups.DeletePDFPages;
import org.jpedal.examples.viewer.gui.popups.EncryptPDFDocument;
import org.jpedal.examples.viewer.gui.popups.ExtractPDFPagesNup;
import org.jpedal.examples.viewer.gui.popups.InsertBlankPDFPage;
import org.jpedal.examples.viewer.gui.popups.RotatePDFPages;
import org.jpedal.examples.viewer.gui.popups.SavePDF;
import org.jpedal.examples.viewer.gui.popups.StampImageToPDFPages;
import org.jpedal.examples.viewer.gui.popups.StampTextToPDFPages;
import org.jpedal.examples.viewer.objects.SignData;
import org.jpedal.objects.PdfPageData;

public class ItextFunctions
{
  public static final boolean IS_DUMMY = true;
  public static final int ROTATECLOCKWISE = 0;
  public static final int ROTATECOUNTERCLOCKWISE = 1;
  public static final int ROTATE180 = 2;
  public static final int ORDER_ACROSS = 3;
  public static final int ORDER_DOWN = 4;
  public static final int ORDER_STACK = 5;
  public static final int REPEAT_NONE = 6;
  public static final int REPEAT_AUTO = 7;
  public static final int REPEAT_SPECIFIED = 8;
  public static final int NOT_CERTIFIED = -1;
  public static final int CERTIFIED_NO_CHANGES_ALLOWED = -1;
  public static final int CERTIFIED_FORM_FILLING = -1;
  public static final int CERTIFIED_FORM_FILLING_AND_ANNOTATIONS = -1;
  public static final int ALLOW_PRINTING = -1;
  public static final int ALLOW_MODIFY_CONTENTS = -1;
  public static final int ALLOW_COPY = -1;
  public static final int ALLOW_MODIFY_ANNOTATIONS = -1;
  public static final int ALLOW_FILL_IN = -1;
  public static final int ALLOW_SCREENREADERS = -1;
  public static final int ALLOW_ASSEMBLY = -1;
  public static final int ALLOW_DEGRADED_PRINTING = -1;

  public ItextFunctions(SwingGUI paramSwingGUI, String paramString, PdfDecoder paramPdfDecoder)
  {
  }

  public static void saveFormsData(String paramString)
  {
    throw new AssertionError("Itext not on classpath");
  }

  public static void extractPagesToNewPDF(SavePDF paramSavePDF)
  {
    throw new AssertionError("Itext not on classpath");
  }

  public static void nup(int paramInt, PdfPageData paramPdfPageData, ExtractPDFPagesNup paramExtractPDFPagesNup)
  {
    throw new AssertionError("Itext not on classpath");
  }

  public static void handouts(String paramString)
  {
    throw new AssertionError("Itext not on classpath");
  }

  public static void add(int paramInt, PdfPageData paramPdfPageData, InsertBlankPDFPage paramInsertBlankPDFPage)
  {
    throw new AssertionError("Itext not on classpath");
  }

  public static void Sign(SignData paramSignData)
  {
    throw new AssertionError("Itext not on classpath");
  }

  public static void rotate(int paramInt, PdfPageData paramPdfPageData, RotatePDFPages paramRotatePDFPages)
  {
    throw new AssertionError("Itext not on classpath");
  }

  public static void setCrop(int paramInt, PdfPageData paramPdfPageData, CropPDFPages paramCropPDFPages)
  {
    throw new AssertionError("Itext not on classpath");
  }

  public static void delete(int paramInt, PdfPageData paramPdfPageData, DeletePDFPages paramDeletePDFPages)
  {
    throw new AssertionError("Itext not on classpath");
  }

  public static void stampImage(int paramInt, PdfPageData paramPdfPageData, StampImageToPDFPages paramStampImageToPDFPages)
  {
    throw new AssertionError("Itext not on classpath");
  }

  public static void stampText(int paramInt, PdfPageData paramPdfPageData, StampTextToPDFPages paramStampTextToPDFPages)
  {
    throw new AssertionError("Itext not on classpath");
  }

  public static void addHeaderFooter(int paramInt, PdfPageData paramPdfPageData, AddHeaderFooterToPDFPages paramAddHeaderFooterToPDFPages)
  {
    throw new AssertionError("Itext not on classpath");
  }

  public static void encrypt(int paramInt, PdfPageData paramPdfPageData, EncryptPDFDocument paramEncryptPDFDocument)
  {
    throw new AssertionError("Itext not on classpath");
  }

  public static String getVersion()
  {
    throw new AssertionError("Itext not on classpath");
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.utils.ItextFunctions
 * JD-Core Version:    0.6.2
 */