package org.jpedal.examples.text;

import java.awt.Rectangle;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import org.jpedal.PdfDecoderInt;
import org.jpedal.PdfDecoderServer;
import org.jpedal.exception.PdfException;
import org.jpedal.grouping.PdfGroupingAlgorithms;
import org.jpedal.objects.PdfPageData;

public class FindTextInRectangle
{
  ArrayList co_ords = null;
  int[] areaToScan = null;
  String separator = System.getProperty("file.separator");
  PdfDecoderInt decodePdf = null;
  private String textToFind = null;
  private File xmlOutputFile;
  private static String xmlOutputPath;
  private static boolean enableXML = false;
  private static boolean enableSTDout = true;

  public FindTextInRectangle(String paramString1, String paramString2)
  {
    this.textToFind = paramString2;
    findText(paramString1);
  }

  public FindTextInRectangle(String paramString1, String paramString2, int[] paramArrayOfInt)
  {
    this.textToFind = paramString2;
    this.areaToScan = paramArrayOfInt;
    findText(paramString1);
  }

  private void findText(String paramString)
  {
    createXMLFile(true);
    this.co_ords = new ArrayList();
    File localFile = new File(paramString);
    if (paramString.toLowerCase().endsWith(".pdf"))
    {
      decodeFile(paramString);
    }
    else if (localFile.isDirectory())
    {
      String[] arrayOfString = localFile.list();
      if (!paramString.endsWith(this.separator))
        paramString = paramString + this.separator;
      long l = arrayOfString.length;
      for (int i = 0; i < l; i++)
      {
        if (enableSTDout)
          System.out.println("File " + i + " of " + l + ' ' + arrayOfString[i]);
        if (arrayOfString[i].toLowerCase().endsWith(".pdf"))
        {
          if (enableSTDout)
            System.out.println(paramString + arrayOfString[i]);
          decodeFile(paramString + arrayOfString[i]);
        }
      }
    }
    else
    {
      System.err.println(paramString + " is not a directory. Exiting program");
    }
    createXMLFile(false);
  }

  private void decodeFile(String paramString)
  {
    createFileXMLElement(paramString, true);
    try
    {
      this.decodePdf = new PdfDecoderServer(false);
      this.decodePdf.setExtractionMode(1);
      PdfDecoderServer.init(true);
      if (enableSTDout)
        System.out.println("Opening file: " + paramString);
      this.decodePdf.openPdfFile(paramString);
    }
    catch (PdfException localPdfException1)
    {
      System.err.println("Ignoring " + paramString);
      System.err.println("Due to: " + localPdfException1);
      createFileXMLElement(paramString, false);
      return;
    }
    if ((this.decodePdf.isEncrypted()) && (!this.decodePdf.isPasswordSupplied()) && (!this.decodePdf.isExtractionAllowed()))
    {
      System.out.println("Encrypted settings");
      System.out.println("Please look at Viewer for code sample to handle such files");
      System.out.println("Or get support/consultancy");
    }
    else
    {
      int i = 1;
      int j = this.decodePdf.getPageCount();
      try
      {
        for (int k = i; k <= j; k++)
        {
          if (enableSTDout)
          {
            System.out.println("=========================");
            System.out.println("Page " + k);
            System.out.println("=========================");
          }
          this.decodePdf.decodePage(k);
          PdfGroupingAlgorithms localPdfGroupingAlgorithms = this.decodePdf.getGroupingObject();
          if (localPdfGroupingAlgorithms != null)
          {
            Object localObject;
            int m;
            int i1;
            int i2;
            int n;
            if (this.areaToScan == null)
            {
              localObject = this.decodePdf.getPdfPageData();
              m = ((PdfPageData)localObject).getMediaBoxX(k);
              i1 = ((PdfPageData)localObject).getMediaBoxWidth(k) + m;
              i2 = ((PdfPageData)localObject).getMediaBoxY(k);
              n = ((PdfPageData)localObject).getMediaBoxHeight(k) + i2;
            }
            else
            {
              m = this.areaToScan[0];
              n = this.areaToScan[1];
              i1 = this.areaToScan[2];
              i2 = this.areaToScan[3];
            }
            if (enableSTDout)
              System.out.println("Scanning for text (" + this.textToFind + ") rectangle (" + m + ',' + n + ' ' + i1 + ',' + i2 + ')');
            try
            {
              localObject = localPdfGroupingAlgorithms.findText(new Rectangle(m, i2, i1 - m, n - i2), k, new String[] { this.textToFind }, 8);
              this.co_ords.add(localObject);
            }
            catch (PdfException localPdfException2)
            {
              this.decodePdf.closePdfFile();
              System.err.println("Ignoring " + paramString);
              System.err.println("Due to: " + localPdfException2);
              createFileXMLElement(paramString, false);
              return;
            }
            if (localObject == null)
            {
              if (enableSTDout)
                System.out.println("Text not found on page.");
            }
            else
            {
              if (enableSTDout)
                System.out.println("Found " + localObject.length / 5 + " on page.");
              for (int i3 = 0; i3 < localObject.length; i3 += 5)
              {
                if (enableSTDout)
                  System.out.println("Text found at " + localObject[i3] + ", " + localObject[(i3 + 1)]);
                createFindXMLElement(localObject[i3], localObject[(i3 + 1)], k);
              }
            }
          }
        }
        this.decodePdf.flushObjectValues(false);
      }
      catch (Exception localException)
      {
        this.decodePdf.closePdfFile();
        System.err.println("Exception: " + localException.getMessage());
        createFileXMLElement(paramString, false);
        return;
      }
      this.decodePdf.flushObjectValues(true);
      if (enableSTDout)
        System.out.println("File read...");
    }
    createFileXMLElement(paramString, false);
    this.decodePdf.closePdfFile();
  }

  public ArrayList getCoords()
  {
    return this.co_ords;
  }

  public float[] getCoords(int paramInt)
  {
    return (float[])this.co_ords.get(paramInt - 1);
  }

  public void createXMLFile(boolean paramBoolean)
  {
    if (enableXML)
      if (paramBoolean)
      {
        this.xmlOutputFile = new File(xmlOutputPath);
        if (this.xmlOutputFile.exists())
        {
          this.xmlOutputFile.delete();
          try
          {
            this.xmlOutputFile.createNewFile();
          }
          catch (Exception localException1)
          {
            enableXML = false;
            System.err.println("Unable to create XML file: " + localException1 + '\n');
          }
        }
        if (enableXML)
          try
          {
            PrintWriter localPrintWriter1 = new PrintWriter(new FileWriter(this.xmlOutputFile));
            localPrintWriter1.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            localPrintWriter1.println("<search>");
            localPrintWriter1.println("<term>" + this.textToFind + "</term>");
            localPrintWriter1.close();
          }
          catch (Exception localException2)
          {
            enableXML = false;
            System.err.print("Failed to write to XML file: " + localException2 + '\n');
          }
      }
      else
      {
        try
        {
          PrintWriter localPrintWriter2 = new PrintWriter(new FileWriter(this.xmlOutputFile, true));
          localPrintWriter2.println("</search>");
          localPrintWriter2.close();
        }
        catch (Exception localException3)
        {
          System.err.print("Exception creating closing XML file: " + localException3 + '\n');
        }
      }
  }

  public void createFileXMLElement(String paramString, boolean paramBoolean)
  {
    if (enableXML)
      try
      {
        PrintWriter localPrintWriter = new PrintWriter(new FileWriter(this.xmlOutputFile, true));
        if (paramBoolean)
        {
          localPrintWriter.println("<file>");
          localPrintWriter.println("<path>" + paramString + "</path>");
        }
        else
        {
          localPrintWriter.println("</file>");
        }
        localPrintWriter.close();
      }
      catch (Exception localException)
      {
        System.out.print("Creating new outputFile: " + localException);
      }
  }

  public void createFindXMLElement(float paramFloat1, float paramFloat2, int paramInt)
  {
    if (enableXML)
      try
      {
        PrintWriter localPrintWriter = new PrintWriter(new FileWriter(this.xmlOutputFile, true));
        localPrintWriter.println("<found>");
        localPrintWriter.println("<pageNo>" + paramInt + "</pageNo>");
        localPrintWriter.println("<x>" + paramFloat1 + "</x>");
        localPrintWriter.println("<y>" + paramFloat2 + "</y>");
        localPrintWriter.println("</found>");
        localPrintWriter.close();
      }
      catch (Exception localException)
      {
        System.out.print("Creating new outputFile: " + localException);
      }
  }

  public static void main(String[] paramArrayOfString)
  {
    Object localObject1 = null;
    Object localObject2 = null;
    int i = 0;
    if ((paramArrayOfString.length > 1) && (paramArrayOfString.length <= 4))
      for (Object localObject4 : paramArrayOfString)
        if (localObject4.toLowerCase().equals("-c"))
        {
          enableSTDout = false;
        }
        else
        {
          switch (i)
          {
          case 0:
            localObject1 = localObject4;
            break;
          case 1:
            localObject2 = localObject4;
            break;
          case 2:
            xmlOutputPath = localObject4;
            enableXML = true;
          }
          i++;
        }
    if ((i < 2) || (i > 3))
    {
      System.out.println("Usage: FindTextInRectangle input string [xmlFile] [-c]");
      System.out.println("\t\tinput\tThe pdf file or directory you wish to search.");
      System.out.println("\t\tstring\tThe string to search for (Use quotes if it contains spaces).");
      System.out.println("\nOptional parameters:");
      System.out.println("\txmlOutput\tThe name you want to give the XML search output file");
      System.out.println("\t[-c]\t\tadd -c to suppress output to console");
      System.exit(1);
    }
    if (enableSTDout)
    {
      System.out.println("Search Target: " + localObject1 + " Searching for: " + localObject2);
      if (enableXML)
        System.out.println("XML File: " + xmlOutputPath);
    }
    ??? = new File(localObject1);
    if (!((File)???).exists())
    {
      System.out.println("File " + localObject1 + " not found");
      System.exit(1);
    }
    else
    {
      new FindTextInRectangle(localObject1, localObject2);
    }
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.text.FindTextInRectangle
 * JD-Core Version:    0.6.2
 */