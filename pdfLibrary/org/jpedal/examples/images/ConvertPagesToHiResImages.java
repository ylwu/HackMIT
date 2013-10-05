package org.jpedal.examples.images;

import com.sun.imageio.plugins.jpeg.JPEGImageWriter;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;
import org.jpedal.PdfDecoderInt;
import org.jpedal.PdfDecoderServer;
import org.jpedal.constants.JPedalSettings;
import org.jpedal.exception.PdfException;
import org.jpedal.fonts.FontMappings;
import org.jpedal.io.ColorSpaceConvertor;
import org.jpedal.io.JAIHelper;
import org.jpedal.objects.PdfPageData;
import org.jpedal.utils.LogWriter;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ConvertPagesToHiResImages
{
  private static boolean debug = false;
  static final String separator = System.getProperty("file.separator");
  private float JPEGcompression = -1.0F;

  public static void main(String[] paramArrayOfString)
    throws Exception
  {
    if ((paramArrayOfString != null) && (paramArrayOfString.length > 1))
    {
      String str2 = paramArrayOfString[0];
      String str1 = paramArrayOfString[1];
      if ((!str2.startsWith(".")) && (str2.toLowerCase().endsWith(".pdf")) && ((str1.equals("jpg")) || (str1.equals("jpeg")) || (str1.equals("png")) || (str1.equals("tiff")) || (str1.equals("tif"))))
      {
        new ConvertPagesToHiResImages(str1, str2);
      }
      else
      {
        File localFile1 = new File(str2);
        if (localFile1.isDirectory())
        {
          String[] arrayOfString1 = null;
          if (!str2.endsWith(separator))
            str2 = str2 + separator;
          try
          {
            File localFile2 = new File(str2);
            arrayOfString1 = localFile2.list();
          }
          catch (Exception localException)
          {
            LogWriter.writeLog("Exception trying to access file " + localException.getMessage());
          }
          for (String str3 : arrayOfString1)
            if (str3.toLowerCase().endsWith(".pdf"))
              new ConvertPagesToHiResImages(str1, str2 + str3);
        }
        else
        {
          System.out.println("The file to be processed has to be a pdf and the output filetype can only be jpg,png or tiff");
        }
      }
    }
    else
    {
      System.out.println("Not enough arguments passed in! Usage: \"C:\\examples\\1.pdf\" \"jpg\"");
    }
  }

  public ConvertPagesToHiResImages(String paramString1, String paramString2)
    throws Exception
  {
    long l = System.currentTimeMillis();
    String str1 = paramString2.substring(0, paramString2.toLowerCase().indexOf(".pdf")) + separator;
    File localFile = new File(str1);
    if (((!localFile.exists()) || (!localFile.isDirectory())) && (!localFile.mkdirs()) && (debug))
      System.err.println("Can't create directory " + str1);
    PdfDecoderServer localPdfDecoderServer = new PdfDecoderServer(true);
    FontMappings.setFontReplacements();
    localPdfDecoderServer.openPdfFile(paramString2);
    HashMap localHashMap = new HashMap();
    localHashMap.put(JPedalSettings.EXTRACT_AT_BEST_QUALITY_MAXSCALING, Integer.valueOf(2));
    localHashMap.put(JPedalSettings.EXTRACT_AT_PAGE_SIZE, new String[] { "2000", "1600" });
    localHashMap.put(JPedalSettings.PAGE_SIZE_OVERRIDES_IMAGE, Boolean.TRUE);
    PdfDecoderServer.modifyJPedalParameters(localHashMap);
    if (debug)
      System.out.println("pdf : " + paramString2);
    try
    {
      String str2 = System.getProperty("org.jpedal.separation");
      if (str2 != null)
      {
        Object[] arrayOfObject = { Integer.valueOf(7), "", Boolean.FALSE };
        if (str2.equals("all"))
          arrayOfObject = new Object[] { Integer.valueOf(2), "image_and_shapes", Boolean.FALSE, Integer.valueOf(18), "image_without_shapes", Boolean.FALSE, Integer.valueOf(1), "text_and_shapes", Boolean.TRUE, Integer.valueOf(7), "all", Boolean.FALSE, Integer.valueOf(17), "text_without_shapes", Boolean.TRUE };
        int i = arrayOfObject.length;
        int j = 0;
        while (j < i)
        {
          localPdfDecoderServer.setRenderMode(((Integer)arrayOfObject[j]).intValue());
          extractPageAsImage(paramString1, str1, localPdfDecoderServer, "_" + arrayOfObject[(j + 1)], ((Boolean)arrayOfObject[(j + 2)]).booleanValue());
          j += 3;
        }
      }
      else
      {
        extractPageAsImage(paramString1, str1, localPdfDecoderServer, "", false);
      }
    }
    finally
    {
      localPdfDecoderServer.closePdfFile();
    }
    System.out.println("time=" + (System.currentTimeMillis() - l) / 1000L);
  }

  public static BufferedImage getHiresPage(int paramInt1, int paramInt2, String paramString)
  {
    BufferedImage localBufferedImage = null;
    PdfDecoderServer localPdfDecoderServer = new PdfDecoderServer(true);
    try
    {
      FontMappings.setFontReplacements();
      localPdfDecoderServer.openPdfFile(paramString);
      PdfPageData localPdfPageData = localPdfDecoderServer.getPdfPageData();
      int i = paramInt2 * localPdfPageData.getCropBoxWidth(paramInt1);
      int j = paramInt2 * localPdfPageData.getCropBoxHeight(paramInt1);
      HashMap localHashMap = new HashMap();
      localHashMap.put(JPedalSettings.EXTRACT_AT_BEST_QUALITY_MAXSCALING, Integer.valueOf(2));
      localHashMap.put(JPedalSettings.EXTRACT_AT_PAGE_SIZE, new String[] { String.valueOf(i), String.valueOf(j) });
      localHashMap.put(JPedalSettings.PAGE_SIZE_OVERRIDES_IMAGE, Boolean.TRUE);
      PdfDecoderServer.modifyJPedalParameters(localHashMap);
      localBufferedImage = localPdfDecoderServer.getPageAsHiRes(paramInt1, null, false);
    }
    catch (PdfException localPdfException)
    {
      localPdfException.printStackTrace();
    }
    finally
    {
      localPdfDecoderServer.closePdfFile();
    }
    return localBufferedImage;
  }

  private void extractPageAsImage(String paramString1, String paramString2, PdfDecoderInt paramPdfDecoderInt, String paramString3, boolean paramBoolean)
    throws PdfException, IOException
  {
    int i = 1;
    int j = paramPdfDecoderInt.getPageCount();
    BufferedImage[] arrayOfBufferedImage = new BufferedImage[1 + (j - i)];
    String str1 = System.getProperty("org.jpedal.multipage_tiff");
    boolean bool1 = (str1 != null) && (str1.toLowerCase().equals("true"));
    String str2 = System.getProperty("org.jpedal.compress_tiff");
    boolean bool2 = (str2 != null) && (str2.toLowerCase().equals("true"));
    String str3 = System.getProperty("org.jpedal.compression_jpeg");
    if (str3 != null)
    {
      try
      {
        this.JPEGcompression = Float.parseFloat(str3);
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
      }
      if ((this.JPEGcompression < 0.0F) || (this.JPEGcompression > 1.0F))
        throw new RuntimeException("Invalid value for JPEG compression - must be between 0 and 1");
    }
    String str4 = System.getProperty("org.jpedal.jpeg_dpi");
    for (int k = i; k < j + 1; k++)
    {
      if (debug)
        System.out.println("page : " + k);
      Object localObject1 = paramPdfDecoderInt.getPageAsHiRes(k, null, paramBoolean);
      String str5 = System.getProperty("org.jpedal.imageType");
      Object localObject3;
      if (str5 != null)
        if (isNumber(str5))
        {
          int m = Integer.parseInt(str5);
          if ((m > -1) && (m < 14))
          {
            localObject2 = new BufferedImage(((BufferedImage)localObject1).getWidth(), ((BufferedImage)localObject1).getHeight(), m);
            localObject3 = ((BufferedImage)localObject2).createGraphics();
            ((Graphics2D)localObject3).drawImage((Image)localObject1, null, null);
            localObject1 = localObject2;
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
      paramPdfDecoderInt.flushObjectValues(true);
      if (paramString1.equals("jpg"))
        localObject1 = ColorSpaceConvertor.convertToRGB((BufferedImage)localObject1);
      String str6;
      if (bool1)
      {
        str6 = paramString2 + "allPages" + paramString3 + '.' + paramString1;
      }
      else
      {
        localObject2 = String.valueOf(k);
        localObject3 = String.valueOf(j);
        i1 = ((String)localObject3).length() - ((String)localObject2).length();
        for (int i2 = 0; i2 < i1; i2++)
          localObject2 = '0' + (String)localObject2;
        str6 = paramString2 + "page" + (String)localObject2 + paramString3 + '.' + paramString1;
      }
      Object localObject2 = paramPdfDecoderInt.getPageInfo(1);
      for (int i1 = localObject2 != null ? 1 : 0; (localObject2 != null) && (((Iterator)localObject2).hasNext()); i1 = 0)
      {
        label507: int n = ((Integer)((Iterator)localObject2).next()).intValue();
        if ((n == 1568372915) || (n == 391471749))
          break label507;
      }
      Object localObject4;
      if (i1 != 0)
      {
        localObject4 = new BufferedImage(((BufferedImage)localObject1).getWidth(), ((BufferedImage)localObject1).getHeight(), 10);
        ((BufferedImage)localObject4).getGraphics().drawImage((Image)localObject1, 0, 0, null);
        localObject1 = localObject4;
      }
      if (bool1)
        arrayOfBufferedImage[(k - i)] = localObject1;
      if (localObject1 != null)
      {
        if (JAIHelper.isJAIused())
          JAIHelper.confirmJAIOnClasspath();
        if ((JAIHelper.isJAIused()) && (paramString1.startsWith("tif")))
          JAIHelper.saveAsTiff(bool2, bool1, (BufferedImage)localObject1, str6, k == j, arrayOfBufferedImage);
        else if (!bool1)
          if (((str4 != null) || (str3 != null)) && (paramString1.startsWith("jp")) && (JAIHelper.isJAIused()))
          {
            saveAsJPEG(str4, (BufferedImage)localObject1, this.JPEGcompression, new BufferedOutputStream(new FileOutputStream(str6)));
          }
          else
          {
            localObject4 = new BufferedOutputStream(new FileOutputStream(new File(str6)));
            ImageIO.write((RenderedImage)localObject1, paramString1, (OutputStream)localObject4);
            ((BufferedOutputStream)localObject4).flush();
            ((BufferedOutputStream)localObject4).close();
          }
        ((BufferedImage)localObject1).flush();
      }
      if (debug)
        System.out.println("Created : " + str6);
    }
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
 * Qualified Name:     org.jpedal.examples.images.ConvertPagesToHiResImages
 * JD-Core Version:    0.6.2
 */