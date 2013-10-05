package org.jpedal.examples.text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.jpedal.PdfDecoderInt;
import org.jpedal.PdfDecoderServer;
import org.jpedal.exception.PdfException;
import org.jpedal.exception.PdfSecurityException;
import org.jpedal.utils.LogWriter;
import org.w3c.dom.Document;

public class ExtractOutline
{
  public static boolean showMessages = true;
  private String user_dir = System.getProperty("user.dir");
  String separator = System.getProperty("file.separator");
  PdfDecoderInt decodePdf = null;
  private boolean isFile = true;
  private byte[] byteArray = null;
  public static boolean isTest = false;

  public ExtractOutline(String paramString)
  {
    if (showMessages)
      System.out.println("processing " + paramString);
    if (!this.user_dir.endsWith(this.separator))
      this.user_dir += this.separator;
    sortFiles(paramString);
  }

  private void sortFiles(String paramString)
  {
    String[] arrayOfString1 = null;
    File localFile = new File(paramString);
    if (paramString.toLowerCase().endsWith(".pdf"))
    {
      decodeFile(paramString);
    }
    else if (localFile.isDirectory())
    {
      if (!paramString.endsWith(this.separator))
        paramString = paramString + this.separator;
      try
      {
        localFile = new File(paramString);
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
          if (showMessages)
            System.out.println(paramString + str);
          decodeFile(paramString + str);
        }
        else
        {
          sortFiles(paramString + str);
        }
    }
  }

  public ExtractOutline(byte[] paramArrayOfByte)
  {
    if (showMessages)
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
      this.decodePdf.useTextExtraction();
      org.jpedal.grouping.PdfGroupingAlgorithms.useUnrotatedCoords = false;
      if (showMessages)
        System.out.println("Opening file :" + paramString);
      if (this.isFile)
        this.decodePdf.openPdfFile(paramString);
      else
        this.decodePdf.openPdfArray(this.byteArray);
    }
    catch (PdfSecurityException localPdfSecurityException)
    {
      System.err.println("Exception " + localPdfSecurityException + " in pdf code " + paramString);
    }
    catch (PdfException localPdfException)
    {
      System.err.println("Exception " + localPdfException + " in pdf code " + paramString);
    }
    catch (Exception localException)
    {
      System.err.println("Exception " + localException + " in pdf code " + paramString);
      localException.printStackTrace();
    }
    Document localDocument = this.decodePdf.getOutlineAsXML();
    try
    {
      InputStream localInputStream = getClass().getResourceAsStream("/org/jpedal/examples/viewer/res/xmlstyle.xslt");
      TransformerFactory localTransformerFactory = TransformerFactory.newInstance();
      Transformer localTransformer = localTransformerFactory.newTransformer(new StreamSource(localInputStream));
      DOMSource localDOMSource = new DOMSource(localDocument);
      if (localDOMSource.getNode() != null)
      {
        String str3 = str2;
        File localFile = new File(str2 + "outline.txt");
        if (!localFile.exists())
        {
          localObject = new File(str3);
          ((File)localObject).mkdirs();
          localFile.createNewFile();
        }
        Object localObject = new FileOutputStream(localFile);
        StreamResult localStreamResult = new StreamResult((OutputStream)localObject);
        localTransformer.transform(localDOMSource, localStreamResult);
      }
    }
    catch (TransformerConfigurationException localTransformerConfigurationException)
    {
      localTransformerConfigurationException.printStackTrace();
    }
    catch (TransformerException localTransformerException)
    {
      localTransformerException.printStackTrace();
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
    if (showMessages)
    {
      System.out.println("Outline read to " + str2 + "outline.txt");
      System.out.println("source=" + localDocument);
    }
    this.decodePdf.flushObjectValues(true);
    if (showMessages)
      System.out.println("Outline read");
    this.decodePdf.closePdfFile();
    this.decodePdf = null;
  }

  public static void main(String[] paramArrayOfString)
  {
    if (showMessages)
      System.out.println("Simple demo to extract the outLine of a pdf");
    String str = "";
    if (paramArrayOfString.length == 1)
    {
      str = paramArrayOfString[0];
      if (showMessages)
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
    new ExtractOutline(str);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.text.ExtractOutline
 * JD-Core Version:    0.6.2
 */