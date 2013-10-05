package org.jpedal.examples;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.PrintStream;
import org.jpedal.PdfDecoderInt;
import org.jpedal.PdfDecoderServer;
import org.jpedal.examples.handlers.DefaultImageHelper;
import org.jpedal.objects.PdfPageData;
import org.jpedal.objects.acroforms.AcroRenderer;
import org.jpedal.objects.raw.FormObject;
import org.jpedal.objects.raw.PdfArrayIterator;
import org.jpedal.objects.raw.PdfObject;

public class ShowLinks
{
  private boolean includeImages = true;

  public ShowLinks(String paramString)
  {
    BufferedImage localBufferedImage = null;
    PdfDecoderServer localPdfDecoderServer;
    if (this.includeImages)
      localPdfDecoderServer = new PdfDecoderServer(true);
    else
      localPdfDecoderServer = new PdfDecoderServer(false);
    try
    {
      localPdfDecoderServer.openPdfFile(paramString);
      for (int i = 1; i < localPdfDecoderServer.getPageCount() + 1; i++)
      {
        PdfArrayIterator localPdfArrayIterator = localPdfDecoderServer.getFormRenderer().getAnnotsOnPage(i);
        if ((localPdfArrayIterator != null) && (localPdfArrayIterator.getTokenCount() > 0))
        {
          if (this.includeImages)
            localBufferedImage = localPdfDecoderServer.getPageAsImage(i);
          while (localPdfArrayIterator.hasMoreTokens())
          {
            String str1 = localPdfArrayIterator.getNextValueAsString(true);
            FormObject localFormObject = localPdfDecoderServer.getFormRenderer().getFormObject(str1);
            int j = localFormObject.getParameterConstant(1147962727);
            if (j == 473513531)
            {
              System.out.println("link object");
              float[] arrayOfFloat = localFormObject.getFloatArray(573911876);
              System.out.println("PDF Rect= " + arrayOfFloat[0] + ' ' + arrayOfFloat[1] + ' ' + arrayOfFloat[2] + ' ' + arrayOfFloat[3]);
              int k = localPdfDecoderServer.getPdfPageData().getCropBoxHeight(i);
              float f1 = arrayOfFloat[0];
              float f2 = arrayOfFloat[2] - arrayOfFloat[0];
              float f3 = arrayOfFloat[3] - arrayOfFloat[1];
              float f4 = k - arrayOfFloat[1] - f3;
              System.out.println("Javaspace Rect x=" + f1 + " y=" + f4 + " w=" + f2 + " h=" + f3);
              if (this.includeImages)
              {
                localObject = (Graphics2D)localBufferedImage.getGraphics();
                ((Graphics2D)localObject).setPaint(Color.RED);
                ((Graphics2D)localObject).drawRect((int)f1, (int)f4, (int)f2, (int)f3);
              }
              Object localObject = localFormObject.getDictionary(17);
              if ((localObject != null) && (((PdfObject)localObject).getNameAsConstant(35) == 2433561))
              {
                String str2 = ((PdfObject)localObject).getTextStreamValue(2433561);
                System.out.println("text=" + str2);
              }
            }
          }
        }
        if (this.includeImages)
          DefaultImageHelper.write(localBufferedImage, "PNG", "image-" + i + ".png");
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
    System.out.println("Simple demo to extract pdf file links if any");
    if (paramArrayOfString.length != 1)
    {
      System.out.println("No filename given or  wrong number of values");
    }
    else
    {
      String str = paramArrayOfString[0];
      System.out.println("File :" + str);
      File localFile = new File(str);
      if (!localFile.exists())
        System.out.println("File " + str + " not found");
      else
        new ShowLinks(str);
    }
    System.exit(1);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.ShowLinks
 * JD-Core Version:    0.6.2
 */