package org.jpedal;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.Iterator;
import java.util.Map;
import org.jpedal.display.Display;
import org.jpedal.exception.PdfException;
import org.jpedal.grouping.PdfGroupingAlgorithms;
import org.jpedal.io.ObjectStore;
import org.jpedal.io.PdfObjectReader;
import org.jpedal.objects.Javascript;
import org.jpedal.objects.PdfData;
import org.jpedal.objects.PdfFileInformation;
import org.jpedal.objects.PdfImageData;
import org.jpedal.objects.PdfPageData;
import org.jpedal.objects.acroforms.AcroRenderer;
import org.jpedal.objects.outlines.OutlineData;
import org.jpedal.render.DynamicVectorRenderer;
import org.jpedal.text.TextLines;
import org.jpedal.utils.DPIFactory;
import org.w3c.dom.Document;

public abstract interface PdfDecoderInt
{
  public static final String version = "5.06b04";
  public static final int TEXT = 1;
  public static final int RAWIMAGES = 2;
  public static final int FINALIMAGES = 4;
  public static final int RAWCOMMANDS = 16;
  public static final int CLIPPEDIMAGES = 32;
  public static final int TEXTCOLOR = 64;
  public static final int CMYKIMAGES = 128;
  public static final int XFORMMETADATA = 256;
  public static final int COLOR = 512;
  public static final int RASTERIZE_FORMS = 1024;
  public static final int RENDERTEXT = 1;
  public static final int RENDERIMAGES = 2;
  public static final int REMOVE_RENDERSHAPES = 16;
  public static final int REMOVE_NOFORMS = 32;
  public static final int OCR_PDF = 32;
  public static final int NOTEXTPRINT = 0;
  public static final int TEXTGLYPHPRINT = 1;
  public static final int TEXTSTRINGPRINT = 2;
  public static final int STANDARDTEXTSTRINGPRINT = 3;
  public static final int SUBSTITUTE_FONT_USING_FILE_NAME = 1;
  public static final int SUBSTITUTE_FONT_USING_POSTSCRIPT_NAME = 2;
  public static final int SUBSTITUTE_FONT_USING_FAMILY_NAME = 3;
  public static final int SUBSTITUTE_FONT_USING_FULL_FONT_NAME = 4;
  public static final int SUBSTITUTE_FONT_USING_POSTSCRIPT_NAME_USE_FAMILY_NAME_IF_DUPLICATES = 5;

  public abstract PdfGroupingAlgorithms getGroupingObject()
    throws PdfException;

  public abstract void addExternalHandler(Object paramObject, int paramInt);

  public abstract Object getExternalHandler(int paramInt);

  public abstract PdfObjectReader getIO();

  public abstract TextLines getTextLines();

  public abstract PdfGroupingAlgorithms getBackgroundGroupingObject();

  public abstract Document getMarkedContent();

  public abstract boolean isOpen();

  public abstract int getDisplayRotation();

  public abstract int getPageNumber();

  public abstract int getlastPageDecoded();

  public abstract Iterator getPageInfo(int paramInt);

  public abstract OutlineData getOutlineData();

  public abstract boolean isLoadingLinearizedPDF();

  public abstract int getPageAlignment();

  public abstract void dispose();

  public abstract void closePdfFile();

  public abstract PdfData getPdfData()
    throws PdfException;

  public abstract boolean hasOutline();

  public abstract Document getOutlineAsXML();

  public abstract PdfPageData getPdfPageData();

  public abstract BufferedImage getPageAsHiRes(int paramInt)
    throws PdfException;

  public abstract BufferedImage getPageAsHiRes(int paramInt, Map paramMap)
    throws PdfException;

  public abstract BufferedImage getPageAsHiRes(int paramInt, Map paramMap, boolean paramBoolean)
    throws PdfException;

  public abstract BufferedImage getPageAsHiRes(int paramInt, boolean paramBoolean)
    throws PdfException;

  public abstract BufferedImage getPageAsImage(int paramInt)
    throws PdfException;

  public abstract BufferedImage getPageAsTransparentImage(int paramInt)
    throws PdfException;

  public abstract float getHiResUpscaleFactor();

  public abstract void flushObjectValues(boolean paramBoolean);

  public abstract PdfImageData getPdfImageData();

  public abstract void setRenderMode(int paramInt);

  public abstract void setExtractionMode(int paramInt);

  public abstract void modifyNonstaticJPedalParameters(Map paramMap)
    throws PdfException;

  public abstract PdfFileInformation getFileInformationData();

  public abstract void setExtractionMode(int paramInt, float paramFloat);

  public abstract DPIFactory getDPIFactory();

  public abstract void waitForDecodingToFinish();

  public abstract DynamicVectorRenderer getDynamicRenderer();

  public abstract DynamicVectorRenderer getDynamicRenderer(boolean paramBoolean);

  public abstract void decodePage(int paramInt)
    throws Exception;

  public abstract boolean isPageAvailable(int paramInt);

  public abstract boolean isHiResScreenDisplay();

  public abstract void useHiResScreenDisplay(boolean paramBoolean);

  public abstract void decodePageInBackground(int paramInt)
    throws Exception;

  public abstract int getPageCount();

  public abstract boolean isEncrypted();

  public abstract boolean isPasswordSupplied();

  public abstract boolean isForm();

  public abstract boolean isFileViewable();

  public abstract boolean isExtractionAllowed();

  public abstract void setEncryptionPassword(String paramString)
    throws PdfException;

  public abstract void openPdfArray(byte[] paramArrayOfByte)
    throws PdfException;

  public abstract void openPdfFile(String paramString, Certificate paramCertificate, PrivateKey paramPrivateKey)
    throws PdfException;

  public abstract void openPdfFileFromStream(Object paramObject, String paramString)
    throws PdfException;

  public abstract void openPdfFile(String paramString)
    throws PdfException;

  public abstract void openPdfFile(String paramString1, String paramString2)
    throws PdfException;

  public abstract boolean openPdfFileFromURL(String paramString, boolean paramBoolean)
    throws PdfException;

  public abstract boolean openPdfFileFromURL(String paramString1, boolean paramBoolean, String paramString2)
    throws PdfException;

  public abstract boolean openPdfFileFromInputStream(InputStream paramInputStream, boolean paramBoolean)
    throws PdfException;

  public abstract boolean openPdfFileFromInputStream(InputStream paramInputStream, boolean paramBoolean, String paramString)
    throws PdfException;

  public abstract Object getJPedalObject(int paramInt);

  public abstract void setPageMode(int paramInt);

  public abstract boolean isXMLExtraction();

  public abstract void useTextExtraction();

  public abstract void useXMLExtraction();

  public abstract void setStreamCacheSize(int paramInt);

  public abstract boolean hasEmbeddedFonts();

  public abstract boolean PDFContainsEmbeddedFonts()
    throws Exception;

  public abstract int getPageFromObjectRef(String paramString);

  public abstract String getInfo(int paramInt);

  public abstract AcroRenderer getFormRenderer();

  public abstract Javascript getJavaScript();

  public abstract String getPageDecodeReport();

  public abstract ObjectStore getObjectStore();

  public abstract void setObjectStore(ObjectStore paramObjectStore);

  public abstract int getDisplayView();

  public abstract float getScaling();

  public abstract void setDisplayRotation(int paramInt);

  public abstract Display getPages();

  public abstract void setPageParameters(float paramFloat, int paramInt);

  public abstract void drawAdditionalObjectsOverPage(int paramInt, int[] paramArrayOfInt, Color[] paramArrayOfColor, Object[] paramArrayOfObject)
    throws PdfException;

  public abstract void flushAdditionalObjectsOnPage(int paramInt)
    throws PdfException;

  public abstract String getFileName();
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.PdfDecoderInt
 * JD-Core Version:    0.6.2
 */