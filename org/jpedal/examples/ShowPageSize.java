package org.jpedal.examples;

import java.io.PrintStream;
import org.jpedal.PdfDecoderInt;
import org.jpedal.PdfDecoderServer;
import org.jpedal.objects.PdfPageData;

public class ShowPageSize
{
  public ShowPageSize(String paramString)
  {
    PdfDecoderServer localPdfDecoderServer = new PdfDecoderServer(false);
    try
    {
      localPdfDecoderServer.openPdfFile(paramString);
      int i = localPdfDecoderServer.getPageCount();
      System.out.println("Page count=" + i);
      PdfPageData localPdfPageData = localPdfDecoderServer.getPdfPageData();
      for (int j = 0; j < i; j++)
      {
        if (localPdfPageData.getRotation(j) != 0)
          System.out.println("Has rotation " + localPdfPageData.getRotation(j) + " degrees");
        System.out.print("page (size in pixels) " + j + " mediaBox=" + localPdfPageData.getMediaBoxX(j) + ' ' + localPdfPageData.getMediaBoxY(j) + ' ' + localPdfPageData.getMediaBoxWidth(j) + ' ' + localPdfPageData.getMediaBoxHeight(j) + " CropBox=" + localPdfPageData.getCropBoxX(j) + ' ' + localPdfPageData.getCropBoxY(j) + ' ' + localPdfPageData.getCropBoxWidth(j) + ' ' + localPdfPageData.getCropBoxHeight(j));
        float f = 72.0F;
        System.out.print(" (size in inches) " + j + " mediaBox=" + localPdfPageData.getMediaBoxX(j) / f + ' ' + localPdfPageData.getMediaBoxY(j) / f + ' ' + localPdfPageData.getMediaBoxWidth(j) / f + ' ' + localPdfPageData.getMediaBoxHeight(j) / f + " CropBox=" + localPdfPageData.getCropBoxX(j) / f + ' ' + localPdfPageData.getCropBoxY(j) / f + localPdfPageData.getCropBoxWidth(j) / f + ' ' + localPdfPageData.getCropBoxHeight(j) / f);
        f = 28.346457F;
        System.out.print(" (size in cm) " + j + " mediaBox=" + localPdfPageData.getMediaBoxX(j) / f + ' ' + localPdfPageData.getMediaBoxY(j) / f + ' ' + localPdfPageData.getMediaBoxWidth(j) / f + ' ' + localPdfPageData.getMediaBoxHeight(j) / f + " CropBox=" + localPdfPageData.getCropBoxX(j) / f + ' ' + localPdfPageData.getCropBoxY(j) / f + localPdfPageData.getCropBoxWidth(j) / f + ' ' + localPdfPageData.getCropBoxHeight(j) / f + '\n');
      }
      localPdfDecoderServer.closePdfFile();
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }

  public static void main(String[] paramArrayOfString)
  {
    if (paramArrayOfString.length != 1)
      System.out.println("Please pass in file name (including path");
    else
      new ShowPageSize(paramArrayOfString[0]);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.ShowPageSize
 * JD-Core Version:    0.6.2
 */