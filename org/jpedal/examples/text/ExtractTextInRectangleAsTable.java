package org.jpedal.examples.text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.Map;
import org.jpedal.PdfDecoderInt;
import org.jpedal.PdfDecoderServer;
import org.jpedal.exception.PdfException;
import org.jpedal.grouping.PdfGroupingAlgorithms;
import org.jpedal.objects.PdfPageData;
import org.jpedal.utils.LogWriter;

public class ExtractTextInRectangleAsTable
{
  private String user_dir = System.getProperty("user.dir");
  String separator = System.getProperty("file.separator");
  PdfDecoderInt decodePdf = null;
  public static boolean showMessages = true;
  private String outputDir = "";
  private static int defX1 = -1;
  private static int defX2;
  private static int defY1;
  private static int defY2;
  private boolean isCSV = true;
  private static String testFile = "/mnt/shared/storypad/input/samples_table_grouping/sampleTable.pdf";

  public ExtractTextInRectangleAsTable(String paramString)
  {
    String str = System.getProperty("xml");
    if (str != null)
      this.isCSV = false;
    if (paramString == null)
      paramString = testFile;
    if (!this.user_dir.endsWith(this.separator))
      this.user_dir += this.separator;
    File localFile1 = new File(this.outputDir);
    if (!localFile1.exists())
      localFile1.mkdirs();
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
        File localFile2 = new File(paramString);
        if (!localFile2.isDirectory())
          System.err.println(paramString + " is not a directory. Exiting program");
        arrayOfString = localFile2.list();
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

  private void decodeFile(String paramString)
  {
    String str1 = "demo";
    int i = paramString.lastIndexOf(this.separator);
    if (i != -1)
      str1 = paramString.substring(i + 1, paramString.length() - 4);
    this.outputDir = (this.user_dir + "tables" + this.separator + str1 + this.separator);
    File localFile = new File(this.outputDir + this.separator);
    if (!localFile.exists())
      localFile.mkdirs();
    try
    {
      this.decodePdf = new PdfDecoderServer(false);
      this.decodePdf.setExtractionMode(1);
      PdfDecoderServer.init(true);
      if (showMessages)
        System.out.println("Opening file :" + paramString);
      this.decodePdf.openPdfFile(paramString);
    }
    catch (Exception localException1)
    {
      System.err.println("7.Exception " + localException1 + " in pdf code");
    }
    if (!this.decodePdf.isExtractionAllowed())
    {
      System.out.println("Text extraction not allowed");
    }
    else if ((this.decodePdf.isEncrypted()) && (!this.decodePdf.isPasswordSupplied()))
    {
      System.out.println("Encrypted settings");
      System.out.println("Please look at Viewer for code sample to handle such files");
      System.out.println("Or get support/consultancy");
    }
    else
    {
      int j = 1;
      int k = this.decodePdf.getPageCount();
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
            System.out.println("Extracting text from rectangle as table(" + n + ',' + i1 + ' ' + i2 + ',' + i3 + ')');
          String str2 = ".xml";
          if (this.isCSV)
          {
            if (showMessages)
              System.out.println("Table will be in CSV format");
            str2 = ".csv";
          }
          else if (showMessages)
          {
            System.out.println("Table will be in xml format");
          }
          String str3 = null;
          try
          {
            Map localMap = localPdfGroupingAlgorithms.extractTextAsTable(n, i1, i2, i3, m, this.isCSV, false, false, false, 0);
            str3 = (String)localMap.get("content");
          }
          catch (PdfException localPdfException)
          {
            this.decodePdf.closePdfFile();
            System.err.println("Exception " + localPdfException.getMessage() + " with table extraction");
          }
          if (str3 == null)
          {
            if (showMessages)
              System.out.println("No text found");
          }
          else
          {
            OutputStreamWriter localOutputStreamWriter = new OutputStreamWriter(new FileOutputStream(this.outputDir + m + str2), "UTF-8");
            if (showMessages)
              System.out.println("Writing to " + this.outputDir + m + str2);
            if (!this.isCSV)
              localOutputStreamWriter.write("<xml><BODY>\n\n");
            localOutputStreamWriter.write(str3);
            if (!this.isCSV)
              localOutputStreamWriter.write("\n</body></xml>");
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
      }
      this.decodePdf.flushObjectValues(true);
      if (showMessages)
        System.out.println("Text read as table");
    }
    this.decodePdf.closePdfFile();
  }

  public static void main(String[] paramArrayOfString)
  {
    System.out.println("Simple demo to extract text objects as CSV or xml tables");
    String str = testFile;
    if (paramArrayOfString.length == 1)
    {
      str = paramArrayOfString[0];
      System.out.println("File :" + str);
    }
    else if (paramArrayOfString.length != 2)
    {
      if (paramArrayOfString.length == 5)
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
    }
    File localFile = new File(str);
    if (!localFile.exists())
      System.out.println("File " + str + " not found");
    new ExtractTextInRectangleAsTable(str);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.text.ExtractTextInRectangleAsTable
 * JD-Core Version:    0.6.2
 */