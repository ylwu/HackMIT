package org.jpedal.utils;

import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import javax.print.DocFlavor.SERVICE_FORMATTED;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Media;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import org.jpedal.PdfDecoder;
import org.jpedal.objects.PdfPageData;

public class PdfPageFormat
{
  private static Set<PageFormat> availablePaper;

  public PdfPageFormat()
  {
    throw new AssertionError("PdfPageFormat cannot be instanced.  Use factory methods.");
  }

  public static final PageFormat createPdfPageFormat(MediaSizeName paramMediaSizeName, PrintService paramPrintService)
  {
    int i = 0;
    if (paramPrintService == null)
      paramPrintService = PrintServiceLookup.lookupDefaultPrintService();
    if ((i != 0) && (paramPrintService == null))
      System.out.println("Print Device is null");
    PageFormat localPageFormat = new PageFormat();
    MediaSize localMediaSize = MediaSize.getMediaSizeForName(paramMediaSizeName);
    if (localMediaSize == null)
      localMediaSize = MediaSize.getMediaSizeForName(getDefaultMediaSizeName(paramPrintService));
    if ((i != 0) && (localMediaSize == null))
      System.out.println("Media Size is null");
    float f1 = localMediaSize.getX(25400) * 72.0F;
    float f2 = localMediaSize.getY(25400) * 72.0F;
    Paper localPaper = new Paper();
    localPaper.setSize(f1, f2);
    MediaPrintableArea localMediaPrintableArea = (MediaPrintableArea)paramPrintService.getDefaultAttributeValue(MediaPrintableArea.class);
    if ((i != 0) && (localMediaPrintableArea == null))
      System.out.println("Area is null");
    HashPrintRequestAttributeSet localHashPrintRequestAttributeSet = new HashPrintRequestAttributeSet();
    localHashPrintRequestAttributeSet.add(paramMediaSizeName);
    MediaPrintableArea[] arrayOfMediaPrintableArea = (MediaPrintableArea[])paramPrintService.getSupportedAttributeValues(MediaPrintableArea.class, null, localHashPrintRequestAttributeSet);
    if ((i != 0) && (arrayOfMediaPrintableArea == null))
      System.out.println("Printable Area array is null");
    int j = 0;
    if (arrayOfMediaPrintableArea[j] == null)
      for (k = 0; (k != arrayOfMediaPrintableArea.length) && (arrayOfMediaPrintableArea[j] == null); k++)
        j = k;
    float[] arrayOfFloat = arrayOfMediaPrintableArea[j].getPrintableArea(25400);
    for (int k = 0; k < 4; k++)
      arrayOfFloat[k] *= 72.0F;
    if (arrayOfFloat[2] + arrayOfFloat[0] > f1)
      arrayOfFloat[2] = (f1 - arrayOfFloat[0]);
    if (arrayOfFloat[3] + arrayOfFloat[1] > f2)
      arrayOfFloat[3] = (f2 - arrayOfFloat[1]);
    localPaper.setImageableArea(arrayOfFloat[0], arrayOfFloat[1], arrayOfFloat[2], arrayOfFloat[3]);
    localPageFormat.setPaper(localPaper);
    OrientationRequested localOrientationRequested = (OrientationRequested)paramPrintService.getDefaultAttributeValue(OrientationRequested.class);
    if (localOrientationRequested != null)
      switch (localOrientationRequested.getValue())
      {
      case 3:
        localPageFormat.setOrientation(1);
        break;
      case 4:
        localPageFormat.setOrientation(0);
        break;
      case 5:
        localPageFormat.setOrientation(2);
        break;
      case 6:
        localPageFormat.setOrientation(1);
        break;
      default:
        localPageFormat.setOrientation(1);
      }
    return localPageFormat;
  }

  public static PageFormat getDefaultPage(PrintService paramPrintService)
  {
    if (paramPrintService == null)
      paramPrintService = PrintServiceLookup.lookupDefaultPrintService();
    return createPdfPageFormat(getDefaultMediaSizeName(paramPrintService), paramPrintService);
  }

  public static MediaSizeName getDefaultMediaSizeName(PrintService paramPrintService)
  {
    MediaSizeName localMediaSizeName = (MediaSizeName)paramPrintService.getDefaultAttributeValue(MediaSizeName.class);
    if ((localMediaSizeName == null) || (MediaSize.getMediaSizeForName(localMediaSizeName) == null))
    {
      Locale localLocale = Locale.getDefault();
      if (localLocale.equals(Locale.UK))
        localMediaSizeName = MediaSizeName.ISO_A4;
      else if (localLocale.equals(Locale.US))
        localMediaSizeName = MediaSizeName.NA_LETTER;
      else
        localMediaSizeName = MediaSizeName.ISO_A4;
    }
    return localMediaSizeName;
  }

  public static void findPrinterPapers(PrintService paramPrintService)
  {
    availablePaper = new HashSet();
    if (paramPrintService == null)
      paramPrintService = PrintServiceLookup.lookupDefaultPrintService();
    Media[] arrayOfMedia1 = (Media[])paramPrintService.getSupportedAttributeValues(Media.class, DocFlavor.SERVICE_FORMATTED.PAGEABLE, null);
    for (Media localMedia : arrayOfMedia1)
      if ((localMedia instanceof MediaSizeName))
      {
        MediaSize localMediaSize = MediaSize.getMediaSizeForName((MediaSizeName)localMedia);
        if (localMediaSize != null)
          availablePaper.add(createPdfPageFormat((MediaSizeName)localMedia, paramPrintService));
      }
  }

  public static PageFormat getPageFormat(int paramInt, PdfDecoder paramPdfDecoder)
  {
    PdfPageData localPdfPageData = paramPdfDecoder.getPdfPageData();
    float f1 = localPdfPageData.getCropBoxWidth(paramInt);
    float f2 = localPdfPageData.getCropBoxHeight(paramInt);
    PageFormat localPageFormat = getAppropriatePageFormat(f1, f2);
    return localPageFormat;
  }

  private static PageFormat getAppropriatePageFormat(float paramFloat1, float paramFloat2)
  {
    if (availablePaper == null)
      findPrinterPapers(null);
    PageFormat localPageFormat1 = null;
    Iterator localIterator = availablePaper.iterator();
    while (localIterator.hasNext())
    {
      PageFormat localPageFormat2 = (PageFormat)localIterator.next();
      localPageFormat1 = getClosestPageFormat(localPageFormat1, localPageFormat2, paramFloat1, paramFloat2);
    }
    if (localPageFormat1 == null)
      localPageFormat1 = getDefaultPage(null);
    return localPageFormat1;
  }

  private static PageFormat getClosestPageFormat(PageFormat paramPageFormat1, PageFormat paramPageFormat2, float paramFloat1, float paramFloat2)
  {
    double d1 = paramPageFormat2.getHeight() > paramPageFormat2.getWidth() ? paramPageFormat2.getHeight() : paramPageFormat2.getWidth();
    double d2 = paramPageFormat2.getHeight() < paramPageFormat2.getWidth() ? paramPageFormat2.getHeight() : paramPageFormat2.getWidth();
    double d3 = paramFloat2 > paramFloat1 ? paramFloat2 : paramFloat1;
    double d4 = paramFloat2 < paramFloat1 ? paramFloat2 : paramFloat1;
    if ((d2 >= d4) && (d1 >= d3))
    {
      if (paramPageFormat1 == null)
      {
        PageFormat localPageFormat1 = (PageFormat)paramPageFormat2.clone();
        return localPageFormat1;
      }
      double d5 = paramPageFormat1.getHeight() > paramPageFormat1.getWidth() ? paramPageFormat1.getHeight() : paramPageFormat1.getWidth();
      double d6 = paramPageFormat1.getHeight() < paramPageFormat1.getWidth() ? paramPageFormat1.getHeight() : paramPageFormat1.getWidth();
      if (d5 + d6 > d2 + d1)
      {
        PageFormat localPageFormat2 = (PageFormat)paramPageFormat2.clone();
        return localPageFormat2;
      }
    }
    return paramPageFormat1;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.utils.PdfPageFormat
 * JD-Core Version:    0.6.2
 */