package org.jpedal.examples.images;

import com.sun.imageio.plugins.jpeg.JPEGImageWriter;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Iterator;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;
import org.jpedal.PdfDecoderInt;
import org.jpedal.PdfDecoderServer;
import org.jpedal.examples.viewer.Commands;
import org.jpedal.exception.PdfException;
import org.jpedal.fonts.FontMappings;
import org.jpedal.io.JAIHelper;
import org.jpedal.io.ObjectStore;
import org.jpedal.objects.PdfFileInformation;
import org.jpedal.utils.LogWriter;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ConvertPagesToImages
{
  boolean isTransparent = false;
  private String user_dir = System.getProperty("user.dir");
  private float pageScaling = 1.33F;
  public static boolean outputMessages = false;
  String output_dir = null;
  String separator = System.getProperty("file.separator");
  PdfDecoderInt decode_pdf = null;
  private String format = "png";
  private String[] ocr = { "TeleForm" };
  private boolean useHiresImage = false;
  private String test_file = "/mnt/shared/sample_pdfs/general/World Factbook.pdf";
  public static boolean isTest = false;
  public static boolean orderReversed = false;
  private int scaling = 100;
  private String password = null;
  private float JPEGcompression = -1.0F;

  public ConvertPagesToImages(String[] paramArrayOfString)
  {
    String str = setParams(paramArrayOfString);
    File localFile = new File(str);
    if (!localFile.exists())
    {
      System.out.println("File " + localFile + " not found");
      System.out.println("May need full path");
      return;
    }
    extraction(str, this.output_dir);
  }

  private void extraction(String paramString1, String paramString2)
  {
    String str1 = System.getProperty("org.jpedal.hires");
    if ((Commands.hires) || (str1 != null))
      this.useHiresImage = true;
    this.output_dir = paramString2;
    if (!this.user_dir.endsWith(this.separator))
      this.user_dir += this.separator;
    if (paramString1.toLowerCase().endsWith(".pdf"))
    {
      if ((!isTest) && (paramString2 == null))
        paramString2 = this.user_dir + "thumbnails" + this.separator;
      decodeFile(paramString1, paramString2);
    }
    else
    {
      String[] arrayOfString1 = null;
      if (!paramString1.endsWith(this.separator))
        paramString1 = paramString1 + this.separator;
      try
      {
        File localFile = new File(paramString1);
        if (!localFile.isDirectory())
          System.err.println(paramString1 + " is not a directory. Exiting program");
        else
          arrayOfString1 = localFile.list();
      }
      catch (Exception localException)
      {
        LogWriter.writeLog("Exception trying to access file " + localException.getMessage());
      }
      if (arrayOfString1 != null)
        for (String str2 : arrayOfString1)
          if (str2.toLowerCase().endsWith(".pdf"))
          {
            if (outputMessages)
              System.out.println(paramString1 + str2);
            decodeFile(paramString1 + str2, paramString2);
          }
    }
    if (outputMessages)
      System.out.println("Thumbnails created");
  }

  private void decodeFile(String paramString1, String paramString2)
  {
    String str1 = "demo";
    int i = paramString1.lastIndexOf(this.separator);
    if (i == -1)
      i = paramString1.lastIndexOf(47);
    if (i != -1)
      str1 = paramString1.substring(i + 1, paramString1.length() - 4);
    else if ((!isTest) && (paramString1.toLowerCase().endsWith(".pdf")))
      str1 = paramString1.substring(0, paramString1.length() - 4);
    if (str1.startsWith("."))
      return;
    if (paramString2 == null)
      paramString2 = this.user_dir + "thumbnails" + this.separator;
    try
    {
      this.decode_pdf = new PdfDecoderServer(true);
      if (!isTest)
        FontMappings.setFontReplacements();
      if (this.useHiresImage)
        this.decode_pdf.useHiResScreenDisplay(true);
      if (isTest)
        this.pageScaling = 1.0F;
      this.decode_pdf.setExtractionMode(0, this.pageScaling);
      if (outputMessages)
        System.out.println("Opening file :" + paramString1);
      if (this.password != null)
        this.decode_pdf.openPdfFile(paramString1, this.password);
      else
        this.decode_pdf.openPdfFile(paramString1);
    }
    catch (Exception localException)
    {
      System.err.println("8.Exception " + localException + " in pdf code in " + paramString1);
    }
    if ((this.decode_pdf.isEncrypted()) && (!this.decode_pdf.isFileViewable()))
    {
      if (!isTest)
        throw new RuntimeException("Wrong password password used=>" + this.password + '<');
    }
    else
    {
      if ((this.decode_pdf.isEncrypted()) && (!this.decode_pdf.isPasswordSupplied()) && (!this.decode_pdf.isExtractionAllowed()))
        throw new RuntimeException("Extraction not allowed");
      String str2 = System.getProperty("org.jpedal.separation");
      if (str2 != null)
      {
        Object[] arrayOfObject = { Integer.valueOf(7), "", Boolean.FALSE };
        if (str2.equals("all"))
          arrayOfObject = new Object[] { Integer.valueOf(2), "image_and_shapes", Boolean.FALSE, Integer.valueOf(18), "image_without_shapes", Boolean.FALSE, Integer.valueOf(1), "text_and_shapes", Boolean.TRUE, Integer.valueOf(7), "all", Boolean.FALSE, Integer.valueOf(17), "text_without_shapes", Boolean.TRUE };
        int j = arrayOfObject.length;
        int k = 0;
        while (k < j)
        {
          this.decode_pdf.setRenderMode(((Integer)arrayOfObject[k]).intValue());
          extractPageAsImage(paramString1, paramString2, str1 + '_' + arrayOfObject[(k + 1)], ((Boolean)arrayOfObject[(k + 2)]).booleanValue());
          k += 3;
        }
      }
      else
      {
        extractPageAsImage(paramString1, paramString2, str1, this.isTransparent);
      }
    }
    this.decode_pdf.closePdfFile();
  }

  private void extractPageAsImage(String paramString1, String paramString2, String paramString3, boolean paramBoolean)
  {
    File localFile = new File(paramString2);
    if (!localFile.exists())
      localFile.mkdirs();
    String str1 = System.getProperty("org.jpedal.multipage_tiff");
    boolean bool1 = (str1 != null) && (str1.toLowerCase().equals("true"));
    String str2 = System.getProperty("org.jpedal.compression_jpeg");
    if (str2 != null)
    {
      try
      {
        this.JPEGcompression = Float.parseFloat(str2);
      }
      catch (Exception localException1)
      {
        localException1.printStackTrace();
      }
      if ((this.JPEGcompression < 0.0F) || (this.JPEGcompression > 1.0F))
        throw new RuntimeException("Invalid value for JPEG compression - must be between 0 and 1");
    }
    String str3 = System.getProperty("org.jpedal.compress_tiff");
    String str4 = System.getProperty("org.jpedal.jpeg_dpi");
    boolean bool2 = (str3 != null) && (str3.toLowerCase().equals("true"));
    if (JAIHelper.isJAIused())
      JAIHelper.confirmJAIOnClasspath();
    int i = 1;
    int j = this.decode_pdf.getPageCount();
    if ((j > 10) && (isTest))
      j = 10;
    if (outputMessages)
      System.out.println("Thumbnails will be in  " + paramString2);
    try
    {
      BufferedImage[] arrayOfBufferedImage = new BufferedImage[1 + (j - i)];
      int k;
      if (orderReversed)
        for (k = j; k >= i; k--)
          getPage(paramString2, paramString3, paramBoolean, bool1, str2, str4, bool2, i, j, arrayOfBufferedImage, k);
      else
        for (k = i; k < j + 1; k++)
          getPage(paramString2, paramString3, paramBoolean, bool1, str2, str4, bool2, i, j, arrayOfBufferedImage, k);
    }
    catch (Exception localException2)
    {
      this.decode_pdf.closePdfFile();
      throw new RuntimeException("Exception " + localException2.getMessage() + " with thumbnails on File=" + paramString1);
    }
  }

  private void getPage(String paramString1, String paramString2, boolean paramBoolean1, boolean paramBoolean2, String paramString3, String paramString4, boolean paramBoolean3, int paramInt1, int paramInt2, BufferedImage[] paramArrayOfBufferedImage, int paramInt3)
    throws PdfException, IOException, FileNotFoundException
  {
    if (outputMessages)
      System.out.println("Page " + paramInt3);
    String str1 = String.valueOf(paramInt3);
    String str2 = String.valueOf(paramInt2);
    int i = str2.length() - str1.length();
    for (int j = 0; j < i; j++)
      str1 = '0' + str1;
    String str3;
    if (paramBoolean2)
      str3 = paramString2;
    else
      str3 = paramString2 + "_page_" + str1;
    PdfFileInformation localPdfFileInformation = this.decode_pdf.getFileInformationData();
    String[] arrayOfString1 = localPdfFileInformation.getFieldValues();
    String[] arrayOfString2 = PdfFileInformation.getFieldNames();
    Object localObject3;
    for (int k = 0; k < arrayOfString2.length; k++)
      if (arrayOfString2[k].equals("Creator"))
        for (localObject3 : this.ocr)
          if (arrayOfString1[k].equals(localObject3))
            this.decode_pdf.setRenderMode(2);
    Object localObject1;
    if (!paramBoolean1)
    {
      localObject1 = this.decode_pdf.getPageAsImage(paramInt3);
    }
    else
    {
      localObject1 = this.decode_pdf.getPageAsTransparentImage(paramInt3);
      if ((localObject1 != null) && (this.format.toLowerCase().startsWith("jp")))
      {
        ??? = localObject1;
        ??? = ((BufferedImage)???).getWidth();
        ??? = ((BufferedImage)???).getHeight();
        localObject1 = new BufferedImage(???, ???, 1);
        localObject3 = ((BufferedImage)localObject1).createGraphics();
        ((Graphics2D)localObject3).setPaint(Color.WHITE);
        ((Graphics2D)localObject3).fillRect(0, 0, ???, ???);
        ((Graphics2D)localObject3).drawImage((Image)???, 0, 0, null);
      }
    }
    ??? = this.decode_pdf.getPageInfo(1);
    for (??? = ??? != null ? 1 : 0; (??? != null) && (((Iterator)???).hasNext()); ??? = 0)
    {
      label390: ??? = ((Integer)((Iterator)???).next()).intValue();
      if ((??? == 1568372915) || (??? == 391471749))
        break label390;
    }
    if (??? != 0)
    {
      localObject3 = new BufferedImage(((BufferedImage)localObject1).getWidth(), ((BufferedImage)localObject1).getHeight(), 10);
      ((BufferedImage)localObject3).getGraphics().drawImage((Image)localObject1, 0, 0, null);
      localObject1 = localObject3;
    }
    if (paramBoolean2)
      paramArrayOfBufferedImage[(paramInt3 - paramInt1)] = localObject1;
    if (localObject1 != null)
    {
      localObject3 = System.getProperty("maxDimension");
      int i1 = -1;
      if (localObject3 != null)
        i1 = Integer.parseInt((String)localObject3);
      int i3;
      Object localObject4;
      Graphics2D localGraphics2D;
      if ((this.scaling != 100) || (i1 != -1))
      {
        int i2 = ((BufferedImage)localObject1).getWidth() * this.scaling / 100;
        i3 = ((BufferedImage)localObject1).getHeight() * this.scaling / 100;
        if ((i1 != -1) && ((i2 > i1) || (i3 > i1)))
        {
          if (i2 > i3)
          {
            i2 = i1;
            localObject4 = ((BufferedImage)localObject1).getScaledInstance(i2, -1, 4);
          }
          else
          {
            i3 = i1;
            localObject4 = ((BufferedImage)localObject1).getScaledInstance(-1, i3, 4);
          }
        }
        else
          localObject4 = ((BufferedImage)localObject1).getScaledInstance(i2, -1, 4);
        if (this.format.toLowerCase().startsWith("jp"))
          localObject1 = new BufferedImage(((Image)localObject4).getWidth(null), ((Image)localObject4).getHeight(null), 1);
        else
          localObject1 = new BufferedImage(((Image)localObject4).getWidth(null), ((Image)localObject4).getHeight(null), 2);
        localGraphics2D = ((BufferedImage)localObject1).createGraphics();
        localGraphics2D.drawImage((Image)localObject4, 0, 0, null);
      }
      String str4 = System.getProperty("org.jpedal.imageType");
      if (str4 != null)
        if (isNumber(str4))
        {
          i3 = Integer.parseInt(str4);
          if ((i3 > -1) && (i3 < 14))
          {
            localObject4 = new BufferedImage(((BufferedImage)localObject1).getWidth(), ((BufferedImage)localObject1).getHeight(), i3);
            localGraphics2D = ((BufferedImage)localObject4).createGraphics();
            localGraphics2D.drawImage((Image)localObject1, null, null);
            localObject1 = localObject4;
          }
          else
          {
            System.err.println("Image Type is not valid. Value should be a digit between 0 - 13 based on the BufferedImage TYPE variables.");
          }
        }
        else
        {
          System.err.println("Image Type provided is not an Integer. Value should be a digit between 0 - 13 based on the BufferedImage TYPE variables.");
        }
      if ((JAIHelper.isJAIused()) && (this.format.startsWith("tif")))
      {
        String str5 = null;
        boolean bool = paramInt3 == paramInt2;
        if (!paramBoolean2)
          str5 = paramString1 + str1 + str3 + ".tif";
        else if (bool)
          str5 = paramString1 + str3 + ".tif";
        JAIHelper.saveAsTiff(paramBoolean3, paramBoolean2, (BufferedImage)localObject1, str5, bool, paramArrayOfBufferedImage);
      }
      else if (((paramString4 != null) || (paramString3 != null)) && (this.format.startsWith("jp")) && (JAIHelper.isJAIused()))
      {
        saveAsJPEG(paramString4, (BufferedImage)localObject1, this.JPEGcompression, new BufferedOutputStream(new FileOutputStream(paramString1 + str1 + str3 + '.' + this.format)));
      }
      else
      {
        this.decode_pdf.getObjectStore().saveStoredImage(paramString1 + str1 + str3, (BufferedImage)localObject1, true, false, this.format);
      }
    }
    this.decode_pdf.flushObjectValues(true);
  }

  public static void main(String[] paramArrayOfString)
  {
    System.out.println("Simple demo to extract images from a page");
    int i = paramArrayOfString.length;
    int j = (i > 4) || (i == 0) ? 1 : 0;
    if (j != 0)
    {
      if (i > 0)
      {
        System.out.println("too many arguments entered - run with no values to see defaults");
        String str1 = "";
        for (String str2 : paramArrayOfString)
          str1 = str1 + str2 + '\n';
        System.out.println("you entered:\n" + str1 + "as the arguments");
      }
      showCommandLineValues();
    }
    new ConvertPagesToImages(paramArrayOfString);
  }

  private String setParams(String[] paramArrayOfString)
  {
    String str1 = this.test_file;
    int i = paramArrayOfString.length;
    if (i == 0)
    {
      showCommandLineValues();
    }
    else if (i == 1)
    {
      str1 = paramArrayOfString[0];
    }
    else if (i < 6)
    {
      str1 = paramArrayOfString[0];
      for (int j = 1; j < paramArrayOfString.length; j++)
      {
        String str2 = paramArrayOfString[j];
        boolean bool = isNumber(str2);
        if (bool)
        {
          try
          {
            this.scaling = Integer.parseInt(str2);
          }
          catch (Exception localException)
          {
            throw new RuntimeException(str2 + " is not an integer");
          }
        }
        else
        {
          String str3 = str2.toLowerCase();
          if ((str3.equals("jpg")) || (str3.equals("jpeg")))
            this.format = "jpg";
          else if ((str3.equals("tif")) || (str3.equals("tiff")))
            this.format = "tif";
          else if (str3.equals("png"))
            this.format = "png";
          else if ((str2.endsWith("/")) || (str2.endsWith("\\")))
            this.output_dir = str2;
          else
            this.password = str2;
        }
      }
    }
    return str1;
  }

  static void showCommandLineValues()
  {
    System.out.println("Example can take 1-5 parameters");
    System.out.println("Value 1 is the file name or directory of PDF files to process");
    System.out.println("4 optional values of:-\nimage type (jpeg,tiff,png), \nscaling (100 = full size), \npassword for protected file (or null) can also be added ,\noutput path (must end with / or \\ character)");
    System.exit(0);
  }

  private static boolean isNumber(String paramString)
  {
    boolean bool = true;
    int i = paramString.length();
    for (int j = 0; j < i; j++)
    {
      int k = paramString.charAt(j);
      if (((k < 48 ? 1 : 0) | (k > 57 ? 1 : 0)) != 0)
      {
        bool = false;
        j = i;
      }
    }
    return bool;
  }

  public String getOutputDir()
  {
    return this.output_dir;
  }

  private static void saveAsJPEG(String paramString, BufferedImage paramBufferedImage, float paramFloat, BufferedOutputStream paramBufferedOutputStream)
    throws IOException
  {
    JPEGImageWriter localJPEGImageWriter = (JPEGImageWriter)ImageIO.getImageWritersBySuffix("jpeg").next();
    ImageOutputStream localImageOutputStream = ImageIO.createImageOutputStream(paramBufferedOutputStream);
    localJPEGImageWriter.setOutput(localImageOutputStream);
    IIOMetadata localIIOMetadata = localJPEGImageWriter.getDefaultImageMetadata(new ImageTypeSpecifier(paramBufferedImage), null);
    if (paramString != null)
    {
      int i = 96;
      try
      {
        i = Integer.parseInt(paramString);
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
      }
      Element localElement1 = (Element)localIIOMetadata.getAsTree("javax_imageio_jpeg_image_1.0");
      Element localElement2 = (Element)localElement1.getElementsByTagName("app0JFIF").item(0);
      localElement2.setAttribute("Xdensity", Integer.toString(i));
      localElement2.setAttribute("Ydensity", Integer.toString(i));
    }
    JPEGImageWriteParam localJPEGImageWriteParam = (JPEGImageWriteParam)localJPEGImageWriter.getDefaultWriteParam();
    if ((paramFloat >= 0.0F) && (paramFloat <= 1.0F))
    {
      localJPEGImageWriteParam.setCompressionMode(2);
      localJPEGImageWriteParam.setCompressionQuality(paramFloat);
    }
    localJPEGImageWriter.write(localIIOMetadata, new IIOImage(paramBufferedImage, null, null), localJPEGImageWriteParam);
    localImageOutputStream.close();
    localJPEGImageWriter.dispose();
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.images.ConvertPagesToImages
 * JD-Core Version:    0.6.2
 */