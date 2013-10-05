package org.jpedal.examples.text;

import java.io.File;
import java.io.InputStream;
import java.io.PrintStream;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.jpedal.PdfDecoderInt;
import org.jpedal.PdfDecoderServer;
import org.jpedal.exception.PdfException;
import org.jpedal.exception.PdfSecurityException;
import org.jpedal.io.ObjectStore;
import org.jpedal.utils.LogWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ExtractStructuredText
{
  public static boolean isTest = false;
  protected static String output = System.getProperty("user.dir") + "xml";
  public static boolean showMessages = true;
  protected String separator = System.getProperty("file.separator");
  protected PdfDecoderInt decodePdf = null;
  protected String outputFile = "";

  public ExtractStructuredText(String paramString1, String paramString2)
  {
    output = paramString2;
    if (!output.endsWith(this.separator))
      output += this.separator;
    File localFile1 = new File(output);
    if (!localFile1.exists())
      localFile1.mkdirs();
    if (paramString1.toLowerCase().endsWith(".pdf"))
    {
      decodeFile(paramString1);
    }
    else
    {
      String[] arrayOfString = null;
      if (!paramString1.endsWith(this.separator))
        paramString1 = paramString1 + this.separator;
      try
      {
        File localFile2 = new File(paramString1);
        if (!localFile2.isDirectory())
          System.err.println(paramString1 + " is not a directory. Exiting program");
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
            System.out.println(paramString1 + arrayOfString[i]);
          decodeFile(paramString1 + arrayOfString[i]);
        }
      }
    }
  }

  protected void decodeFile(String paramString)
  {
    System.out.println("Processing " + paramString);
    int i = paramString.lastIndexOf('/');
    int j = paramString.lastIndexOf('\\');
    if (j > i)
      i = j;
    String str = paramString.substring(i + 1, paramString.length() - 4);
    this.outputFile = (output + this.separator + str + ".xml");
    try
    {
      this.decodePdf = new PdfDecoderServer(false);
      if (showMessages)
        System.out.println("\n----------------------------");
      if (showMessages)
        System.out.println("Opening file :" + paramString);
      this.decodePdf.openPdfFile(paramString);
    }
    catch (PdfSecurityException localPdfSecurityException)
    {
      System.err.println("Security Exception " + localPdfSecurityException + " in pdf code for text extraction on file " + this.decodePdf.getObjectStore().getCurrentFilename());
    }
    catch (PdfException localPdfException)
    {
      System.err.println("Pdf Exception " + localPdfException + " in pdf code for text extraction on file " + this.decodePdf.getObjectStore().getCurrentFilename());
    }
    catch (Exception localException1)
    {
      System.err.println("Exception " + localException1 + " in pdf code for text extraction on file " + this.decodePdf.getObjectStore().getCurrentFilename());
    }
    if ((this.decodePdf.isEncrypted()) && (!this.decodePdf.isPasswordSupplied()) && (!this.decodePdf.isExtractionAllowed()))
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
      try
      {
        Document localDocument = this.decodePdf.getMarkedContent();
        if (localDocument == null)
        {
          System.out.println("No text found");
        }
        else
        {
          InputStream localInputStream = getClass().getResourceAsStream("/org/jpedal/examples/text/xmlstyle.xslt");
          TransformerFactory localTransformerFactory = TransformerFactory.newInstance();
          try
          {
            Transformer localTransformer = localTransformerFactory.newTransformer(new StreamSource(localInputStream));
            if ((localDocument == null) || (!localDocument.hasChildNodes()))
            {
              System.out.println("No tree data " + localDocument);
              return;
            }
            if (!localDocument.getDocumentElement().hasChildNodes())
            {
              localDocument.appendChild(localDocument.createComment("There is NO Structured text in the file to extract!!"));
              localDocument.appendChild(localDocument.createComment("JPedal can only extract it if it has been added when PDF created"));
              localDocument.appendChild(localDocument.createComment("Please read our blog post at http://www.jpedal.org/PDFblog/2010/09/the-easy-way-to-discover-if-a-pdf-file-contains-structured-content/ "));
            }
            localTransformer.transform(new DOMSource(localDocument), new StreamResult(this.outputFile));
          }
          catch (Exception localException3)
          {
            localException3.printStackTrace();
            System.exit(1);
          }
          catch (Error localError)
          {
            localError.printStackTrace();
            System.exit(1);
          }
          if (showMessages)
            System.out.println("Writing to " + this.outputFile);
        }
        if (showMessages)
          System.out.println("\n----------done--------------");
        this.decodePdf.flushObjectValues(false);
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
      this.decodePdf.closePdfFile();
    }
  }

  public static void main(String[] paramArrayOfString)
  {
    if (showMessages)
      System.out.println("Simple demo to extract text objects");
    if (paramArrayOfString.length == 2)
    {
      String str = paramArrayOfString[0];
      output = paramArrayOfString[1];
      System.out.println("File :" + str);
      File localFile = new File(str);
      if (!localFile.exists())
        System.out.println("File " + str + " not found");
      long l1 = System.currentTimeMillis();
      new ExtractStructuredText(str, output);
      long l2 = System.currentTimeMillis();
      if (!isTest)
        System.out.println("Time taken=" + (l2 - l1) / 1000L);
    }
    else
    {
      System.out.println("Please call with parameters :-");
      System.out.println("FileName");
      System.out.println("outputDir");
    }
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.text.ExtractStructuredText
 * JD-Core Version:    0.6.2
 */