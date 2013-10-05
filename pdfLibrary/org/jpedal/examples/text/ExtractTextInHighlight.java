package org.jpedal.examples.text;

import java.awt.Rectangle;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import org.jpedal.PdfDecoderInt;
import org.jpedal.PdfDecoderServer;
import org.jpedal.exception.PdfException;
import org.jpedal.exception.PdfSecurityException;
import org.jpedal.grouping.PdfGroupingAlgorithms;
import org.jpedal.io.ObjectStore;
import org.jpedal.objects.PdfPageData;
import org.jpedal.text.TextLines;
import org.jpedal.utils.LogWriter;

public class ExtractTextInHighlight
{
  public static boolean isTest = false;
  protected String text = null;
  private static boolean useXMLExtraction = false;
  protected static boolean writeToFile = true;
  protected String user_dir = System.getProperty("user.dir");
  public static boolean showMessages = true;
  protected String separator = System.getProperty("file.separator");
  protected PdfDecoderInt decodePdf = null;
  protected String outputDir = "";
  protected static int defX1 = -1;
  protected static int defX2;
  protected static int defY1;
  protected static int defY2;
  private static String testFile = "/mnt/shared/sample.pdf";

  public ExtractTextInHighlight(String paramString)
  {
    String str = System.getProperty("xml");
    if ((str != null) && (str.toLowerCase().equals("true")))
      useXMLExtraction = true;
    if (!this.user_dir.endsWith(this.separator))
      this.user_dir += this.separator;
    if (paramString.toLowerCase().endsWith(".pdf"))
    {
      decodeFile(paramString);
    }
    else
    {
      String[] arrayOfString = null;
      if (!paramString.endsWith(this.separator))
        paramString = paramString + this.separator;
      try
      {
        File localFile = new File(paramString);
        if (!localFile.isDirectory())
          System.err.println(paramString + " is not a directory. Exiting program");
        arrayOfString = localFile.list();
      }
      catch (Exception localException)
      {
        LogWriter.writeLog("Exception trying to access file " + localException.getMessage());
      }
      long l = arrayOfString.length;
      for (int i = 0; i < l; i++)
      {
        if (showMessages)
          System.out.println(i + "/ " + l + ' ' + arrayOfString[i]);
        if (arrayOfString[i].toLowerCase().endsWith(".pdf"))
        {
          if (showMessages)
            System.out.println(paramString + arrayOfString[i]);
          decodeFile(paramString + arrayOfString[i]);
        }
      }
    }
  }

  protected void decodeFile(String paramString)
  {
    String str1 = "demo";
    int i = paramString.lastIndexOf(this.separator);
    if (i != -1)
      str1 = paramString.substring(i + 1, paramString.length() - 4);
    this.outputDir = (this.user_dir + "highlight" + this.separator + str1 + this.separator);
    try
    {
      this.decodePdf = new PdfDecoderServer(false);
      if ((!isTest) && (useXMLExtraction))
        this.decodePdf.useXMLExtraction();
      this.decodePdf.setExtractionMode(1);
      PdfDecoderServer.init(true);
      if (showMessages)
        System.out.println("Opening file :" + paramString);
      this.decodePdf.openPdfFile(paramString);
    }
    catch (PdfSecurityException localPdfSecurityException)
    {
      System.err.println("Security Exception " + localPdfSecurityException + " in pdf code for text extraction on file " + this.decodePdf.getObjectStore().getCurrentFilename());
    }
    catch (PdfException localPdfException1)
    {
      System.err.println("Pdf Exception " + localPdfException1 + " in pdf code for text extraction on file " + this.decodePdf.getObjectStore().getCurrentFilename());
    }
    catch (Exception localException1)
    {
      System.err.println("Exception " + localException1 + " in pdf code for text extraction on file " + this.decodePdf.getObjectStore().getCurrentFilename());
      localException1.printStackTrace();
    }
    if (!this.decodePdf.isExtractionAllowed())
    {
      if (showMessages)
        System.out.println("Text extraction not allowed");
    }
    else if ((this.decodePdf.isEncrypted()) && (!this.decodePdf.isPasswordSupplied()))
    {
      if (showMessages)
      {
        System.out.println("Encrypted settings");
        System.out.println("Please look at Viewer for code sample to handle such files");
        System.out.println("Or get support/consultancy");
      }
    }
    else
    {
      int j = 1;
      int k = this.decodePdf.getPageCount();
      if ((k > 10) && (isTest))
        k = 10;
      try
      {
        for (int m = j; m < k + 1; m++)
        {
          this.decodePdf.decodePage(m);
          PdfGroupingAlgorithms localPdfGroupingAlgorithms = this.decodePdf.getGroupingObject();
          PdfPageData localPdfPageData = this.decodePdf.getPdfPageData();
          int n;
          int i2;
          int i3;
          int i1;
          if (defX1 == -1)
          {
            n = localPdfPageData.getMediaBoxX(m);
            i2 = localPdfPageData.getMediaBoxWidth(m) + n;
            i3 = localPdfPageData.getMediaBoxY(m);
            i1 = localPdfPageData.getMediaBoxHeight(m) + i3;
          }
          else
          {
            n = defX1;
            i1 = defY1;
            i2 = defX2;
            i3 = defY2;
          }
          if (showMessages)
            System.out.println("Extracting text from rectangle (" + n + ',' + i1 + ' ' + i2 + ',' + i3 + ')');
          this.text = null;
          Rectangle localRectangle = new Rectangle(n, i3, i2 - n, i1 - i3);
          this.decodePdf.getTextLines().clearHighlights();
          this.decodePdf.getTextLines().addHighlights(new Rectangle[] { localRectangle }, false, m);
          Rectangle[] arrayOfRectangle = this.decodePdf.getTextLines().getHighlightedAreas(m);
          String str2;
          if (arrayOfRectangle != null)
            for (int i4 = 0; i4 != arrayOfRectangle.length; i4++)
            {
              str2 = "";
              int i5 = arrayOfRectangle[i4].x - 2;
              int i6 = arrayOfRectangle[i4].x + arrayOfRectangle[i4].width + 4;
              int i7 = arrayOfRectangle[i4].y + arrayOfRectangle[i4].height + 4;
              int i8 = arrayOfRectangle[i4].y - 2;
              int i9;
              if (i7 < i8)
              {
                i9 = i8;
                i8 = i7;
                i7 = i9;
              }
              if (i5 > i6)
              {
                i9 = i6;
                i6 = i5;
                i5 = i9;
              }
              try
              {
                str2 = localPdfGroupingAlgorithms.extractTextInRectangle(i5, i7, i6, i8, m, false, true);
              }
              catch (PdfException localPdfException2)
              {
                this.decodePdf.closePdfFile();
                System.err.println("Exception " + localPdfException2.getMessage() + " in file " + this.decodePdf.getObjectStore().fullFileName);
                localPdfException2.printStackTrace();
              }
              if (str2 != null)
                if (this.text == null)
                  this.text = str2;
                else
                  this.text += str2;
            }
          if (this.text == null)
          {
            if (showMessages)
              System.out.println("No text found");
          }
          else if (writeToFile)
          {
            File localFile = new File(this.outputDir + this.separator);
            if (!localFile.exists())
              localFile.mkdirs();
            str2 = ".txt";
            String str3 = System.getProperty("file.encoding");
            if (useXMLExtraction)
            {
              str2 = ".xml";
              str3 = "UTF-8";
            }
            if (showMessages)
              System.out.println("Writing to " + this.outputDir + m + str2);
            OutputStreamWriter localOutputStreamWriter = new OutputStreamWriter(new FileOutputStream(this.outputDir + m + str2), str3);
            if (useXMLExtraction)
            {
              localOutputStreamWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n");
              localOutputStreamWriter.write("<!-- Pixel Location of text x1,y1,x2,y2\n");
              localOutputStreamWriter.write("(x1,y1 is top left corner)\n");
              localOutputStreamWriter.write("(x1,y1 is bottom right corner)\n");
              localOutputStreamWriter.write("(origin is bottom left corner)  -->\n");
              localOutputStreamWriter.write("\n\n<ARTICLE>\n");
              localOutputStreamWriter.write("<LOCATION x1=\"" + n + "\" " + "y1=\"" + i1 + "\" " + "x2=\"" + i2 + "\" " + "y2=\"" + i3 + "\" />\n");
              localOutputStreamWriter.write("\n\n<TEXT>\n");
              localOutputStreamWriter.write(this.text);
              localOutputStreamWriter.write("\n\n</TEXT>\n");
              localOutputStreamWriter.write("\n\n</ARTICLE>\n");
            }
            else
            {
              localOutputStreamWriter.write(this.text);
            }
            localOutputStreamWriter.close();
          }
          this.decodePdf.flushObjectValues(false);
        }
      }
      catch (Exception localException2)
      {
        this.decodePdf.closePdfFile();
        System.err.println("Exception " + localException2.getMessage());
        localException2.printStackTrace();
        System.out.println(this.decodePdf.getObjectStore().getCurrentFilename());
      }
      this.decodePdf.flushObjectValues(true);
      if (showMessages)
        System.out.println("Text read");
    }
    this.decodePdf.closePdfFile();
  }

  public static void main(String[] paramArrayOfString)
  {
    if (showMessages)
      System.out.println("Simple demo to extract text objects");
    String str = testFile;
    if (paramArrayOfString.length == 1)
    {
      str = paramArrayOfString[0];
      System.out.println("File :" + str);
    }
    else if (paramArrayOfString.length == 5)
    {
      str = paramArrayOfString[0];
      System.out.println("File :" + str);
      System.out.println("User coordinates supplied");
      defX1 = Integer.parseInt(paramArrayOfString[1]);
      defY1 = Integer.parseInt(paramArrayOfString[2]);
      defX2 = Integer.parseInt(paramArrayOfString[3]);
      defY2 = Integer.parseInt(paramArrayOfString[4]);
    }
    else
    {
      System.out.println("Please call with either ");
      System.out.println("FileName");
      System.out.println("or");
      System.out.println("FileName x1 y1 x2 y2");
    }
    File localFile = new File(str);
    if (!localFile.exists())
      System.out.println("File " + str + " not found");
    long l1 = System.currentTimeMillis();
    new ExtractTextInRectangle(str);
    long l2 = System.currentTimeMillis();
    if (!isTest)
      System.out.println("Time taken=" + (l2 - l1) / 1000L);
  }

  public String getExtractedText()
  {
    return this.text;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.text.ExtractTextInHighlight
 * JD-Core Version:    0.6.2
 */