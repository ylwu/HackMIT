package org.jpedal.examples.text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;
import org.jpedal.PdfDecoderInt;
import org.jpedal.PdfDecoderServer;
import org.jpedal.exception.PdfException;
import org.jpedal.grouping.PdfGroupingAlgorithms;
import org.jpedal.objects.PdfData;
import org.jpedal.objects.PdfPageData;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.Strip;

public class TextAsWordlistInBackground
{
  static boolean outputMessages = false;
  private int wordsExtracted = 0;
  private String user_dir = System.getProperty("user.dir");
  String separator = System.getProperty("file.separator");
  PdfDecoderInt decodePdf = null;
  private static String testFile = "/mnt/shared/sample.pdf";
  private String password = null;
  private boolean isFile = true;
  private byte[] byteArray = null;

  public TextAsWordlistInBackground()
  {
  }

  public TextAsWordlistInBackground(String paramString)
  {
    if (outputMessages)
      System.out.println("processing " + paramString);
    if (!this.user_dir.endsWith(this.separator))
      this.user_dir += this.separator;
    if (paramString.toLowerCase().endsWith(".pdf"))
    {
      decodeFile(paramString);
    }
    else
    {
      String[] arrayOfString1 = null;
      if (!paramString.endsWith(this.separator))
        paramString = paramString + this.separator;
      try
      {
        File localFile = new File(paramString);
        if (!localFile.isDirectory())
          System.err.println(paramString + " is not a directory. Exiting program");
        arrayOfString1 = localFile.list();
      }
      catch (Exception localException)
      {
        LogWriter.writeLog("Exception trying to access file " + localException.getMessage());
      }
      for (String str : arrayOfString1)
        if (str.toLowerCase().endsWith(".pdf"))
        {
          if (outputMessages)
            System.out.println(paramString + str);
          decodeFile(paramString + str);
        }
    }
  }

  public TextAsWordlistInBackground(byte[] paramArrayOfByte)
  {
    if (outputMessages)
      System.out.println("processing byte array");
    if (!this.user_dir.endsWith(this.separator))
      this.user_dir += this.separator;
    this.byteArray = paramArrayOfByte;
    this.isFile = false;
    decodeFile("byteArray");
  }

  private void decodeFile(String paramString)
  {
    String str1 = "demo";
    int i = paramString.lastIndexOf(this.separator);
    if (i != -1)
      str1 = paramString.substring(i + 1, paramString.length() - 4);
    String str2 = this.user_dir + "text" + this.separator + str1 + this.separator;
    try
    {
      this.decodePdf = new PdfDecoderServer(false);
      this.decodePdf.setExtractionMode(1);
      PdfDecoderServer.init(true);
      if (outputMessages)
        System.out.println("Opening file :" + paramString);
      if (this.isFile)
      {
        if (this.password != null)
          this.decodePdf.openPdfFile(paramString, this.password);
        else
          this.decodePdf.openPdfFile(paramString);
      }
      else
        this.decodePdf.openPdfArray(this.byteArray);
    }
    catch (Exception localException1)
    {
      System.err.println("Exception " + localException1 + " in pdf code " + paramString);
    }
    if ((this.decodePdf.isEncrypted()) && (!this.decodePdf.isFileViewable()))
      throw new RuntimeException("Wrong password password used=>" + this.password + '<');
    if ((this.decodePdf.isEncrypted()) && (!this.decodePdf.isPasswordSupplied()) && (!this.decodePdf.isExtractionAllowed()))
      throw new RuntimeException("Extraction not allowed");
    int j = 1;
    int k = this.decodePdf.getPageCount();
    try
    {
      for (int m = j; m < k + 1; m++)
      {
        this.decodePdf.decodePage(m);
        PdfData localPdfData = this.decodePdf.getPdfData();
        PdfGroupingAlgorithms localPdfGroupingAlgorithms = new PdfGroupingAlgorithms(localPdfData, this.decodePdf.getPdfPageData(), this.decodePdf.isXMLExtraction());
        PdfPageData localPdfPageData = this.decodePdf.getPdfPageData();
        int n = localPdfPageData.getMediaBoxX(m);
        int i1 = localPdfPageData.getMediaBoxWidth(m) + n;
        int i2 = localPdfPageData.getMediaBoxY(m);
        int i3 = localPdfPageData.getMediaBoxHeight(m) + i2;
        if (outputMessages)
          System.out.println("Page " + m + " Extracting text from rectangle (" + n + ',' + i3 + ' ' + i1 + ',' + i2 + ')');
        List localList = null;
        try
        {
          localList = localPdfGroupingAlgorithms.extractTextAsWordlist(n, i3, i1, i2, m, true, "!.,\"\"''");
        }
        catch (PdfException localPdfException)
        {
          this.decodePdf.closePdfFile();
          System.err.println("Exception= " + localPdfException + " in " + paramString);
        }
        if (localList == null)
        {
          if (outputMessages)
            System.out.println("No text found");
        }
        else
        {
          File localFile = new File(str2);
          if (!localFile.exists())
            localFile.mkdirs();
          int i4 = localList.size() / 5;
          this.wordsExtracted += i4;
          if (outputMessages)
            System.out.println("Page contains " + i4 + " words.");
          if (outputMessages)
            System.out.println("Writing to " + str2 + "words-" + m + ".txt");
          OutputStreamWriter localOutputStreamWriter = new OutputStreamWriter(new FileOutputStream(str2 + "words-" + m + ".txt"), "UTF-8");
          Iterator localIterator = localList.iterator();
          while (localIterator.hasNext())
          {
            String str3 = (String)localIterator.next();
            str3 = Strip.convertToText(str3, this.decodePdf.isXMLExtraction());
            int i5 = (int)Float.parseFloat((String)localIterator.next());
            int i6 = (int)Float.parseFloat((String)localIterator.next());
            int i7 = (int)Float.parseFloat((String)localIterator.next());
            int i8 = (int)Float.parseFloat((String)localIterator.next());
            localOutputStreamWriter.write(str3 + ',' + i5 + ',' + i6 + ',' + i7 + ',' + i8 + '\n');
          }
          localOutputStreamWriter.close();
        }
        this.decodePdf.flushObjectValues(false);
      }
    }
    catch (Exception localException2)
    {
      this.decodePdf.closePdfFile();
      System.err.println("Exception " + localException2 + " in " + paramString);
    }
    this.decodePdf.flushObjectValues(true);
    if (outputMessages)
      System.out.println("Text read");
    this.decodePdf.closePdfFile();
    this.decodePdf = null;
  }

  public static void main(String[] paramArrayOfString)
  {
    if (outputMessages)
      System.out.println("Simple demo to extract text objects using background calls");
    String str = testFile;
    if (paramArrayOfString.length == 0)
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
      System.out.println("File " + str + " not found");
    new TextAsWordlistInBackground(str);
  }

  public int getWordsExtractedCount()
  {
    return this.wordsExtracted;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.text.TextAsWordlistInBackground
 * JD-Core Version:    0.6.2
 */