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
import org.jpedal.exception.PdfSecurityException;
import org.jpedal.fonts.FontMappings;
import org.jpedal.grouping.PdfGroupingAlgorithms;
import org.jpedal.objects.PdfPageData;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.Strip;

public class ExtractTextAsWordlist
{
  public static boolean outputMessages = true;
  private int wordsExtracted = 0;
  private String user_dir = System.getProperty("user.dir");
  String separator = System.getProperty("file.separator");
  PdfDecoderInt decodePdf = null;
  private boolean isFile = true;
  private byte[] byteArray = null;
  public static boolean isTest = false;

  public ExtractTextAsWordlist(String paramString)
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

  public ExtractTextAsWordlist(byte[] paramArrayOfByte)
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
      this.decodePdf = new PdfDecoderServer(true);
      FontMappings.setFontReplacements();
      this.decodePdf.setExtractionMode(1);
      PdfDecoderServer.init(true);
      this.decodePdf.useTextExtraction();
      PdfGroupingAlgorithms.useUnrotatedCoords = false;
      if (outputMessages)
        System.out.println("Opening file :" + paramString);
      if (this.isFile)
        this.decodePdf.openPdfFile(paramString);
      else
        this.decodePdf.openPdfArray(this.byteArray);
    }
    catch (PdfSecurityException localPdfSecurityException)
    {
      System.err.println("Exception " + localPdfSecurityException + " in pdf code for wordlist" + paramString);
    }
    catch (PdfException localPdfException1)
    {
      System.err.println("Exception " + localPdfException1 + " in pdf code for wordlist" + paramString);
    }
    catch (Exception localException1)
    {
      System.err.println("Exception " + localException1 + " in pdf code for wordlist" + paramString);
      localException1.printStackTrace();
    }
    if (!this.decodePdf.isExtractionAllowed())
    {
      if (outputMessages)
        System.out.println("Text extraction not allowed");
    }
    else if ((this.decodePdf.isEncrypted()) && (!this.decodePdf.isPasswordSupplied()))
    {
      if (outputMessages)
      {
        System.out.println("Encrypted settings");
        System.out.println("Please look at Viewer for code sample to handle such files");
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
          int n = localPdfPageData.getMediaBoxX(m);
          int i1 = localPdfPageData.getMediaBoxWidth(m) + n;
          int i2 = localPdfPageData.getMediaBoxX(m);
          int i3 = localPdfPageData.getMediaBoxHeight(m) - i2;
          if (outputMessages)
            System.out.println("Page " + m + " Extracting text from rectangle (" + n + ',' + i3 + ' ' + i1 + ',' + i2 + ')');
          List localList = null;
          try
          {
            localList = localPdfGroupingAlgorithms.extractTextAsWordlist(n, i3, i1, i2, m, true, "&:=()!;.,\\/\"\"''");
          }
          catch (PdfException localPdfException2)
          {
            this.decodePdf.closePdfFile();
            System.err.println("Exception= " + localPdfException2 + " in " + paramString);
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
        localException2.printStackTrace();
      }
      this.decodePdf.flushObjectValues(true);
      if (outputMessages)
        System.out.println("Text read");
    }
    this.decodePdf.closePdfFile();
    this.decodePdf = null;
  }

  public static void main(String[] paramArrayOfString)
  {
    if (outputMessages)
      System.out.println("Simple demo to extract text objects");
    String str = "";
    if (paramArrayOfString.length == 1)
    {
      str = paramArrayOfString[0];
      if (outputMessages)
        System.out.println("File :" + str);
    }
    else
    {
      System.out.println("You must pass ONE parameter - a filename or directory in as a parameter");
      System.out.println("Make sure you put double quotes around the value if it has spaces");
      System.exit(1);
    }
    File localFile = new File(str);
    if (!localFile.exists())
      System.out.println("File " + str + " not found");
    new ExtractTextAsWordlist(str);
  }

  public int getWordsExtractedCount()
  {
    return this.wordsExtracted;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.text.ExtractTextAsWordlist
 * JD-Core Version:    0.6.2
 */