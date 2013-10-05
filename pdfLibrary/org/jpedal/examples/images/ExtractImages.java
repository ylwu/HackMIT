package org.jpedal.examples.images;

import com.sun.media.jai.codec.TIFFEncodeParam;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import javax.media.jai.JAI;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.jpedal.PdfDecoderInt;
import org.jpedal.PdfDecoderServer;
import org.jpedal.examples.handlers.DefaultImageHelper;
import org.jpedal.io.JAIHelper;
import org.jpedal.io.ObjectStore;
import org.jpedal.objects.PdfImageData;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.TimeNow;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class ExtractImages
{
  private String user_dir = System.getProperty("user.dir");
  public static boolean outputMessages = true;
  PdfDecoderInt decode_pdf = null;
  String separator = System.getProperty("file.separator");
  private String output_dir = "";
  public static String testOutputDir = "current_images/";
  public static boolean isTest;
  private static String prefix = "png";
  private static String test_file = "/mnt/shared/Poloznicel_nalozbene_test_1.pdf";

  public ExtractImages(String paramString)
  {
    if (!this.user_dir.endsWith(this.separator))
      this.user_dir += this.separator;
    if ((!paramString.startsWith(".")) && (paramString.toLowerCase().endsWith(".pdf")))
    {
      decode(paramString);
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
        else
          arrayOfString = localFile.list();
      }
      catch (Exception localException)
      {
        LogWriter.writeLog("Exception trying to access file " + localException.getMessage());
      }
      long l = arrayOfString.length;
      for (int i = 0; i < l; i++)
      {
        System.out.println(i + "/ " + l + ' ' + arrayOfString[i]);
        if (arrayOfString[i].endsWith(".pdf"))
        {
          System.out.println(paramString + arrayOfString[i]);
          decode(paramString + arrayOfString[i]);
        }
      }
    }
  }

  private void decode(String paramString)
  {
    String str1 = "demo";
    int i = paramString.lastIndexOf(this.separator);
    if (i != -1)
      str1 = paramString.substring(i + 1, paramString.length() - 4);
    try
    {
      this.decode_pdf = new PdfDecoderServer(false);
      String str2 = System.getProperty("org.jpedal.opi");
      if (str2 == null)
        this.decode_pdf.setExtractionMode(1030);
      else
        this.decode_pdf.setExtractionMode(1286);
      if (outputMessages)
        System.out.println("Opening file :" + paramString);
      this.decode_pdf.openPdfFile(paramString);
    }
    catch (Exception localException1)
    {
      System.err.println("9.Exception " + localException1 + " in pdf code");
    }
    if ((this.decode_pdf.isEncrypted()) && (!this.decode_pdf.isPasswordSupplied()) && (!this.decode_pdf.isExtractionAllowed()))
    {
      if (outputMessages)
      {
        System.out.println("Encrypted settings");
        System.out.println("Please look at Viewer for code sample to handle such files");
        System.out.println("Or get support/consultancy");
      }
    }
    else
    {
      int j = 1;
      int k = this.decode_pdf.getPageCount();
      this.output_dir = (this.user_dir + "images" + this.separator + str1 + this.separator);
      File localFile = new File(this.output_dir);
      if (!localFile.exists())
        localFile.mkdirs();
      if (outputMessages)
        System.out.println("Images will be in directory " + this.output_dir);
      try
      {
        for (int m = j; m < k + 1; m++)
        {
          this.decode_pdf.decodePage(m);
          PdfImageData localPdfImageData = this.decode_pdf.getPdfImageData();
          int n = localPdfImageData.getImageCount();
          Object localObject;
          if (n > 0)
          {
            if (outputMessages)
              System.out.println("Page " + m + " contains " + n + " images");
            String str3 = this.output_dir + this.separator + m;
            localObject = new File(str3);
            if (!((File)localObject).exists())
              ((File)localObject).mkdirs();
          }
          for (int i1 = 0; i1 < n; i1++)
          {
            localObject = localPdfImageData.getImageName(i1);
            try
            {
              BufferedImage localBufferedImage = this.decode_pdf.getObjectStore().loadStoredImage('R' + (String)localObject);
              String str4 = this.output_dir + m + this.separator;
              saveImage(localBufferedImage, str4 + 'R' + (String)localObject + '.' + prefix, prefix);
              localBufferedImage = this.decode_pdf.getObjectStore().loadStoredImage((String)localObject);
              if (localBufferedImage == null)
              {
                if (outputMessages)
                  System.out.println("No image data for " + (String)localObject);
              }
              else
                saveImage(localBufferedImage, str4 + (String)localObject + '.' + prefix, prefix);
              outputMetaDataToXML(paramString, m, localPdfImageData, i1, (String)localObject);
            }
            catch (Exception localException3)
            {
              System.err.println("Exception " + localException3 + " in extracting images");
            }
          }
          this.decode_pdf.flushObjectValues(true);
        }
      }
      catch (Exception localException2)
      {
        this.decode_pdf.closePdfFile();
        System.err.println("Exception " + localException2.getMessage());
      }
      if (outputMessages)
        System.out.println("Images read");
    }
    this.decode_pdf.closePdfFile();
  }

  private static void saveImage(BufferedImage paramBufferedImage, String paramString1, String paramString2)
  {
    if (JAIHelper.isJAIused())
      JAIHelper.confirmJAIOnClasspath();
    if ((paramString2.contains("tif")) && (JAIHelper.isJAIused()))
      try
      {
        FileOutputStream localFileOutputStream = new FileOutputStream(paramString1);
        String str = System.getProperty("org.jpedal.compress_tiff");
        int i = str != null ? 1 : 0;
        TIFFEncodeParam localTIFFEncodeParam = null;
        if (i != 0)
        {
          localTIFFEncodeParam = new TIFFEncodeParam();
          localTIFFEncodeParam.setCompression(32946);
        }
        JAI.create("encode", paramBufferedImage, localFileOutputStream, "TIFF", localTIFFEncodeParam);
        localFileOutputStream.flush();
        localFileOutputStream.close();
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
      }
    else
      try
      {
        DefaultImageHelper.write(paramBufferedImage, paramString2, paramString1);
      }
      catch (IOException localIOException)
      {
        localIOException.printStackTrace();
      }
  }

  private void outputMetaDataToXML(String paramString1, int paramInt1, PdfImageData paramPdfImageData, int paramInt2, String paramString2)
  {
    float f1 = paramPdfImageData.getImageXCoord(paramInt2);
    float f2 = paramPdfImageData.getImageYCoord(paramInt2);
    float f3 = paramPdfImageData.getImageWidth(paramInt2);
    float f4 = paramPdfImageData.getImageHeight(paramInt2);
    try
    {
      DocumentBuilderFactory localDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder localDocumentBuilder = localDocumentBuilderFactory.newDocumentBuilder();
      Document localDocument = localDocumentBuilder.newDocument();
      Element localElement1 = localDocument.createElement("meta");
      localDocument.appendChild(localElement1);
      Comment localComment1 = localDocument.createComment("Created " + TimeNow.getShortTimeNow());
      localDocument.appendChild(localComment1);
      Comment localComment2 = localDocument.createComment("Pixel Location of image x1,y1,x2,y2");
      localDocument.appendChild(localComment2);
      Comment localComment3 = localDocument.createComment("x1,y1 is top left corner origin is bottom left corner");
      localDocument.appendChild(localComment3);
      Element localElement2 = localDocument.createElement("PAGELOCATION");
      localElement2.setAttribute("x1", String.valueOf(f1));
      localElement2.setAttribute("y1", String.valueOf(f2 + f4));
      localElement2.setAttribute("x2", String.valueOf(f1 + f3));
      localElement2.setAttribute("y2", String.valueOf(f2));
      localElement1.appendChild(localElement2);
      Element localElement3 = localDocument.createElement("FILE");
      localElement3.setAttribute("value", paramString1);
      localElement1.appendChild(localElement3);
      if (!isTest)
      {
        InputStream localInputStream = getClass().getResourceAsStream("/org/jpedal/examples/viewer/res/xmlstyle.xslt");
        TransformerFactory localTransformerFactory = TransformerFactory.newInstance();
        Transformer localTransformer = localTransformerFactory.newTransformer(new StreamSource(localInputStream));
        localTransformer.transform(new DOMSource(localDocument), new StreamResult(this.output_dir + paramInt1 + this.separator + paramString2 + ".xml"));
      }
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }

  public static void main(String[] paramArrayOfString)
  {
    if (outputMessages)
      System.out.println("Simple demo to extract images from a page");
    String str1 = test_file;
    int i = 0;
    int j = paramArrayOfString.length;
    Object localObject2;
    if (j == 0)
    {
      System.out.println("Example can take 1 or 2 parameters");
      System.out.println("Value 1 is the file name or directory of PDF files to process");
      System.out.println("Value 2 is optional values of image type (jpeg,tiff,png). Default is png");
      System.exit(0);
    }
    else if (j == 1)
    {
      str1 = paramArrayOfString[0];
      System.out.println("file name=" + str1);
    }
    else if (j < 3)
    {
      str1 = paramArrayOfString[0];
      if (outputMessages)
        System.out.println("File :" + str1);
      for (int k = 1; k < paramArrayOfString.length; k++)
      {
        localObject2 = paramArrayOfString[k];
        String str2 = ((String)localObject2).toLowerCase();
        if ((str2.equals("tif") | str2.equals("tiff")))
        {
          prefix = "tif";
        }
        else if (str2.equals("png"))
        {
          prefix = "png";
        }
        else
        {
          i = 1;
          System.out.println("value args not recognised as valid parameter.");
          System.out.println("please enter \"tif\", \"tiff\" or \"png\".");
        }
      }
    }
    else
    {
      i = 1;
      System.out.println("too many arguments entered - run with no values to see defaults");
    }
    if (i != 0)
    {
      localObject1 = "";
      for (String str3 : paramArrayOfString)
        localObject1 = (String)localObject1 + str3 + '\n';
      System.out.println("you entered:\n" + (String)localObject1 + "as the arguments");
    }
    Object localObject1 = new File(str1);
    if (!((File)localObject1).exists())
      System.out.println("File " + str1 + " not found");
    new ExtractImages(str1);
  }

  public String getOutputDir()
  {
    return this.output_dir;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.images.ExtractImages
 * JD-Core Version:    0.6.2
 */