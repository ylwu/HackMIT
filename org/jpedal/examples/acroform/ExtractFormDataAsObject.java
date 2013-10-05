package org.jpedal.examples.acroform;

import java.io.File;
import java.io.PrintStream;
import org.jpedal.PdfDecoderInt;
import org.jpedal.PdfDecoderServer;
import org.jpedal.objects.PdfPageData;
import org.jpedal.objects.acroforms.AcroRenderer;
import org.jpedal.objects.acroforms.ReturnValues;
import org.jpedal.objects.raw.FormObject;
import org.jpedal.objects.raw.PdfArrayIterator;
import org.jpedal.objects.raw.PdfDictionary;
import org.jpedal.objects.raw.PdfObject;
import org.jpedal.utils.LogWriter;

public class ExtractFormDataAsObject
{
  private String outputDir = System.getProperty("user.dir");
  private int fileCount = 0;
  static boolean outputMessages = false;
  String separator = System.getProperty("file.separator");
  PdfDecoderInt decodePdf = null;
  private static String test_file = "/PDFdata/files-jpedal/Testdokument PDF.pdf";

  public int getFileCount()
  {
    return this.fileCount;
  }

  public ExtractFormDataAsObject(String paramString)
  {
    if (!this.outputDir.endsWith(this.separator))
      this.outputDir = (this.outputDir + this.separator + "forms" + this.separator);
    File localFile1 = new File(this.outputDir);
    if (!localFile1.exists())
      localFile1.mkdirs();
    if (paramString.toLowerCase().endsWith(".pdf"))
    {
      decodePage("", paramString);
    }
    else
    {
      String[] arrayOfString1 = null;
      if (!paramString.endsWith(this.separator))
        paramString = paramString + this.separator;
      try
      {
        File localFile2 = new File(paramString);
        if (!localFile2.isDirectory())
          System.err.println(paramString + " is not a directory. Exiting program");
        else
          arrayOfString1 = localFile2.list();
      }
      catch (Exception localException)
      {
        LogWriter.writeLog("Exception trying to access file " + localException.getMessage());
      }
      for (String str : arrayOfString1)
        if (str.toLowerCase().endsWith(".pdf"))
        {
          if (outputMessages)
            System.out.println(">>_" + paramString + str);
          decodePage(paramString, str);
        }
    }
  }

  private void decodePage(String paramString1, String paramString2)
  {
    String str1 = paramString1 + paramString2;
    try
    {
      this.decodePdf = new PdfDecoderServer(false);
      if (outputMessages)
        System.out.println("Opening file :" + str1);
      this.decodePdf.openPdfFile(str1);
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
      System.err.println("Exception " + localException + " in pdf code with " + str1);
    }
    if ((this.decodePdf.isEncrypted()) && (!this.decodePdf.isExtractionAllowed()))
    {
      if (outputMessages)
      {
        System.out.println("Encrypted settings");
        System.out.println("Please look at Viewer for code sample to handle such files");
      }
    }
    else
    {
      this.fileCount += 1;
      AcroRenderer localAcroRenderer = this.decodePdf.getFormRenderer();
      Object[] arrayOfObject1 = localAcroRenderer.getFormComponents(null, ReturnValues.FORM_NAMES, -1);
      Object localObject1;
      Object localObject2;
      for (localObject1 : arrayOfObject1)
      {
        if (outputMessages)
          System.out.println(localObject1);
        localObject2 = localAcroRenderer.getFormComponents((String)localObject1, ReturnValues.FORMOBJECTS_FROM_NAME, -1);
        for (Object localObject4 : localObject2)
          if (outputMessages)
            System.out.println("name=" + localObject1 + " rawData=" + localObject4 + " value=" + ((FormObject)localObject4).getSelectedItem());
      }
      int i = this.decodePdf.getPageCount();
      for (??? = 1; ??? < i + 1; ???++)
      {
        PdfArrayIterator localPdfArrayIterator = localAcroRenderer.getAnnotsOnPage(???);
        System.out.println("-----page " + ??? + '/' + i + ' ' + localPdfArrayIterator);
        if (localPdfArrayIterator != null)
          while (localPdfArrayIterator.hasMoreTokens())
          {
            localObject1 = localPdfArrayIterator.getNextValueAsString(true);
            localObject2 = this.decodePdf.getFormRenderer().getFormObject((String)localObject1);
            System.out.println("---------------------------------------");
            int m = ((FormObject)localObject2).getParameterConstant(1147962727);
            System.out.println("type=" + m + " " + PdfDictionary.showAsConstant(m));
            if ((m == 2308407) || (m == 473513531))
            {
              if (m == 473513531)
                System.out.println("link object");
              else
                System.out.println("Sig object");
              float[] arrayOfFloat = ((FormObject)localObject2).getFloatArray(573911876);
              System.out.println("PDF Rect= " + arrayOfFloat[0] + ' ' + arrayOfFloat[1] + ' ' + arrayOfFloat[2] + ' ' + arrayOfFloat[3]);
              ??? = this.decodePdf.getPdfPageData().getCropBoxHeight(???);
              float f1 = arrayOfFloat[0];
              float f2 = arrayOfFloat[2] - arrayOfFloat[0];
              float f3 = arrayOfFloat[3] - arrayOfFloat[1];
              float f4 = ??? - arrayOfFloat[1] - f3;
              System.out.println("Javaspace Rect x=" + f1 + " y=" + f4 + " w=" + f2 + " h=" + f3);
              PdfObject localPdfObject = ((FormObject)localObject2).getDictionary(17);
              if ((localPdfObject != null) && (localPdfObject.getNameAsConstant(35) == 2433561))
              {
                String str2 = localPdfObject.getTextStreamValue(2433561);
                System.out.println("text=" + str2);
              }
            }
          }
      }
    }
    this.decodePdf.closePdfFile();
  }

  public static void main(String[] paramArrayOfString)
  {
    if (outputMessages)
      System.out.println("Simple demo to extract form data");
    String str = test_file;
    if (paramArrayOfString.length != 1)
    {
      if (outputMessages)
        System.out.println("Default test file used");
    }
    else
    {
      str = paramArrayOfString[0];
      if (outputMessages)
        System.out.println("File :" + str);
    }
    File localFile = new File(str);
    if (!localFile.exists())
    {
      if (outputMessages)
        System.out.println("File " + str + " not found");
    }
    else
      new ExtractFormDataAsObject(str);
  }

  public String getOutputDir()
  {
    return this.outputDir;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.acroform.ExtractFormDataAsObject
 * JD-Core Version:    0.6.2
 */