package org.jpedal.examples.images;

import com.sun.imageio.plugins.jpeg.JPEGImageWriter;
import com.sun.media.jai.codec.TIFFEncodeParam;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.ResourceBundle;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;
import javax.media.jai.JAI;
import org.jpedal.PdfDecoderInt;
import org.jpedal.PdfDecoderServer;
import org.jpedal.io.JAIHelper;
import org.jpedal.io.ObjectStore;
import org.jpedal.objects.PdfImageData;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.Messages;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ExtractClippedImages
{
  public static boolean outputMessages = true;
  private static String processed_dir = "processed";
  public static boolean testing = false;
  private static String inputDir = "";
  private static int outputCount;
  private static float[] outputSizes;
  private static String[] outputDirectories;
  PdfDecoderInt decode_pdf = null;
  private static final String separator = System.getProperty("file.separator");
  private String output_dir = "clippedImages";
  private String imageType = "tiff";
  private Color backgroundColor = Color.WHITE;

  public ExtractClippedImages(String paramString1, String paramString2)
  {
    try
    {
      Messages.setBundle(ResourceBundle.getBundle("org.jpedal.international.messages"));
    }
    catch (Exception localException1)
    {
      localException1.printStackTrace();
      System.out.println("Exception loading resource bundle");
    }
    String[] arrayOfString = { "500", "high" };
    outputCount = arrayOfString.length / 2;
    outputSizes = new float[outputCount];
    outputDirectories = new String[outputCount];
    for (int i = 0; i < outputCount; i++)
    {
      try
      {
        outputSizes[i] = Float.parseFloat(arrayOfString[(i * 2)]);
      }
      catch (Exception localException2)
      {
        exit("Exception " + localException2 + " reading integer " + arrayOfString[(i * 2)]);
      }
      try
      {
        outputDirectories[i] = paramString2;
        if ((!outputDirectories[i].endsWith("\\")) && (!outputDirectories[i].endsWith("/")))
          outputDirectories[i] = (outputDirectories[i] + separator);
        File localFile = new File(outputDirectories[i]);
        if (!localFile.exists())
          localFile.mkdirs();
      }
      catch (Exception localException3)
      {
        exit("Exception " + localException3 + " with directory " + arrayOfString[(4 + i * 2)]);
      }
    }
    processFiles(paramString1);
  }

  public ExtractClippedImages(String paramString)
  {
    String str = System.getProperty("org.jpedal.imageType");
    if (str != null)
    {
      this.imageType = str.toLowerCase();
      System.out.println(this.imageType);
      if ((this.imageType.equals("tif")) || (this.imageType.equals("tiff")))
        this.imageType = "tiff";
      else if ((this.imageType.equals("jpg")) || (this.imageType.equals("jpeg")))
        this.imageType = "jpg";
      else if (!this.imageType.equals("png"))
        exit("Imagetype " + this.imageType + " not supported");
    }
    else
    {
      this.imageType = "png";
    }
    processFiles(paramString);
  }

  private void processFiles(String paramString)
  {
    if (!processed_dir.endsWith(separator))
      processed_dir += separator;
    if (paramString.toLowerCase().endsWith(".pdf"))
    {
      if (outputMessages)
        System.out.println(paramString);
      decode(paramString);
    }
    else
    {
      if ((!paramString.endsWith("\\")) && (!paramString.endsWith("/")))
        paramString = paramString + separator;
      File localFile1 = new File(paramString);
      if (!localFile1.isDirectory())
        exit("No root directory " + paramString);
      String[] arrayOfString1 = null;
      try
      {
        File localFile2 = new File(paramString);
        if (!localFile2.isDirectory())
          System.err.println(paramString + " is not a directory. Exiting program");
        else
          arrayOfString1 = localFile2.list();
      }
      catch (Exception localException)
      {
        exit("Exception trying to access file " + localException.getMessage());
      }
      for (String str : arrayOfString1)
        if (str.toLowerCase().endsWith(".pdf"))
        {
          if (outputMessages)
            System.out.println(paramString + str);
          decode(paramString + str);
          File localFile3 = new File(paramString + str);
          localFile3.renameTo(new File(processed_dir + str));
        }
    }
  }

  private static void exit(String paramString)
  {
    System.out.println("Exit message " + paramString);
    LogWriter.writeLog("Exit message " + paramString);
  }

  private void decode(String paramString)
  {
    LogWriter.writeLog("==================");
    LogWriter.writeLog("File " + paramString);
    try
    {
      this.decode_pdf = new PdfDecoderServer(false);
      this.decode_pdf.setExtractionMode(36, 1.0F);
      this.decode_pdf.useHiResScreenDisplay(false);
      this.decode_pdf.openPdfFile(paramString);
    }
    catch (Exception localException1)
    {
      exit(Messages.getMessage("PdfViewerError.Exception") + ' ' + localException1 + ' ' + Messages.getMessage("PdfViewerError.OpeningPdfFiles"));
    }
    if ((this.decode_pdf.isEncrypted()) && (!this.decode_pdf.isPasswordSupplied()) && (!this.decode_pdf.isExtractionAllowed()))
    {
      exit(Messages.getMessage("PdfViewerError.EncryptedNotSupported"));
    }
    else
    {
      int i = 1;
      int j = this.decode_pdf.getPageCount();
      try
      {
        for (int k = i; k < j + 1; k++)
        {
          LogWriter.writeLog(Messages.getMessage("PdfViewerDecoding.page") + ' ' + k);
          this.decode_pdf.decodePage(k);
          PdfImageData localPdfImageData = this.decode_pdf.getPdfImageData();
          int m = localPdfImageData.getImageCount();
          if (m > 0)
            LogWriter.writeLog("page " + k + "contains " + m + " images");
          else
            LogWriter.writeLog("No bitmapped images on page " + k);
          LogWriter.writeLog("Writing out images");
          float[] arrayOfFloat1 = new float[m];
          float[] arrayOfFloat2 = new float[m];
          float[] arrayOfFloat3 = new float[m];
          float[] arrayOfFloat4 = new float[m];
          float[] arrayOfFloat5 = new float[m];
          float[] arrayOfFloat6 = new float[m];
          float[] arrayOfFloat7 = new float[m];
          String[] arrayOfString = new String[m];
          BufferedImage[] arrayOfBufferedImage = new BufferedImage[m];
          boolean[] arrayOfBoolean1 = new boolean[m];
          for (int n = 0; n < m; n++)
          {
            arrayOfString[n] = localPdfImageData.getImageName(n);
            arrayOfFloat1[n] = localPdfImageData.getImageXCoord(n);
            arrayOfFloat5[n] = localPdfImageData.getImageXCoord(n);
            arrayOfFloat2[n] = localPdfImageData.getImageYCoord(n);
            arrayOfFloat6[n] = localPdfImageData.getImageYCoord(n);
            arrayOfFloat3[n] = localPdfImageData.getImageWidth(n);
            arrayOfFloat4[n] = localPdfImageData.getImageHeight(n);
            arrayOfFloat7[n] = localPdfImageData.getImageHeight(n);
            arrayOfBufferedImage[n] = this.decode_pdf.getObjectStore().loadStoredImage("CLIP_" + arrayOfString[n]);
          }
          n = 0;
          if (n != 0)
          {
            i1 = 1;
            boolean[] arrayOfBoolean2 = new boolean[m];
            ArrayList[] arrayOfArrayList = new ArrayList[m];
            float f2;
            float f3;
            while (i1 != 0)
            {
              i1 = 0;
              for (i2 = 0; i2 < m; i2++)
                for (int i3 = 0; i3 < m; i3++)
                  if ((i2 != i3) && (arrayOfBoolean2[i2] == 0) && (arrayOfBoolean2[i3] == 0) && (arrayOfBufferedImage[i2] != null) && (arrayOfBufferedImage[i3] != null) && (arrayOfFloat1[i2] >= arrayOfFloat1[i3]) && (arrayOfFloat1[i2] <= arrayOfFloat1[i3] + arrayOfFloat3[i3]) && (arrayOfFloat2[i2] >= arrayOfFloat2[i3]) && (arrayOfFloat2[i2] <= arrayOfFloat2[i3] + arrayOfFloat4[i3]))
                  {
                    f2 = arrayOfFloat1[i3];
                    f3 = arrayOfFloat2[i3];
                    float f4 = arrayOfFloat1[i2] + arrayOfFloat3[i2];
                    float f5 = arrayOfFloat1[i3] + arrayOfFloat3[i3];
                    if (f4 < f5)
                      f4 = f5;
                    float f6 = arrayOfFloat2[i2] + arrayOfFloat4[i2];
                    float f7 = arrayOfFloat2[i3] + arrayOfFloat4[i3];
                    if (f6 < f7)
                      f6 = f7;
                    float f8 = f4 - f2;
                    float f9 = f6 - f3;
                    arrayOfFloat1[i3] = f2;
                    arrayOfFloat2[i3] = f3;
                    arrayOfFloat3[i3] = f8;
                    arrayOfFloat4[i3] = f9;
                    arrayOfBoolean1[i3] = true;
                    if (arrayOfArrayList[i3] == null)
                    {
                      arrayOfArrayList[i3] = new ArrayList(m);
                      arrayOfArrayList[i3].add(Integer.valueOf(i3));
                    }
                    arrayOfArrayList[i3].add(Integer.valueOf(i2));
                    arrayOfBoolean2[i2] = true;
                    if (arrayOfArrayList[i2] != null)
                    {
                      arrayOfArrayList[i3].addAll(Arrays.asList(arrayOfArrayList[i2].toArray()));
                      arrayOfBoolean1[i2] = false;
                      arrayOfArrayList[i2] = null;
                    }
                    i1 = 1;
                    i2 = m;
                    i3 = m;
                  }
            }
            for (int i2 = 0; i2 < m; i2++)
              if (arrayOfArrayList[i2] != null)
              {
                float f1 = arrayOfFloat1[i2];
                f2 = arrayOfFloat2[i2];
                f3 = arrayOfFloat4[i2];
                BufferedImage localBufferedImage = new BufferedImage((int)arrayOfFloat3[i2], (int)arrayOfFloat4[i2], 2);
                Graphics2D localGraphics2D = localBufferedImage.createGraphics();
                Collections.sort(arrayOfArrayList[i2]);
                int i4 = -1;
                for (Object localObject : arrayOfArrayList[i2].toArray())
                {
                  int i7 = ((Integer)localObject).intValue();
                  if (i4 != i7)
                  {
                    localGraphics2D.drawImage(arrayOfBufferedImage[i7], (int)(arrayOfFloat1[i7] - f1), (int)(f3 - arrayOfFloat2[i7] - arrayOfBufferedImage[i7].getHeight() + f2), null);
                    i4 = i7;
                    arrayOfBufferedImage[i7] = null;
                  }
                }
                arrayOfBufferedImage[i2] = localBufferedImage;
              }
          }
          for (int i1 = 0; i1 < m; i1++)
            if (arrayOfBufferedImage[i1] != null)
              generateVersions(paramString, k, "<PAGELOCATION x1=\"" + arrayOfFloat1[i1] + "\" " + "y1=\"" + (arrayOfFloat2[i1] + arrayOfFloat4[i1]) + "\" " + "x2=\"" + (arrayOfFloat1[i1] + arrayOfFloat3[i1]) + "\" " + "y2=\"" + arrayOfFloat2[i1] + "\" />\n", arrayOfString[i1], arrayOfBufferedImage[i1], i1);
          this.decode_pdf.flushObjectValues(true);
        }
      }
      catch (Exception localException2)
      {
        this.decode_pdf.closePdfFile();
        LogWriter.writeLog("Exception " + localException2.getMessage());
        localException2.printStackTrace();
      }
    }
    this.decode_pdf.closePdfFile();
  }

  private void generateVersions(String paramString1, int paramInt1, String paramString2, String paramString3, BufferedImage paramBufferedImage, int paramInt2)
  {
    for (int i = 0; i < outputCount; i++)
      try
      {
        Object localObject1 = paramBufferedImage;
        if (localObject1 != null)
        {
          int j = paramString1.lastIndexOf(92);
          if (j == -1)
            j = paramString1.lastIndexOf(47);
          if (j == -1)
            j = 0;
          String str1 = paramString1.substring(j, paramString1.length() - 4);
          String str2 = outputDirectories[i] + str1 + '_' + paramInt1 + '_' + paramInt2;
          float f = 1.0F;
          int k = ((BufferedImage)localObject1).getHeight();
          if (outputSizes[i] > 0.0F)
          {
            f = outputSizes[i] / k;
            if (f > 1.0F)
            {
              f = 1.0F;
            }
            else
            {
              localObject2 = ((BufferedImage)localObject1).getScaledInstance(-1, (int)outputSizes[i], 4);
              localObject1 = new BufferedImage(((Image)localObject2).getWidth(null), ((Image)localObject2).getHeight(null), 2);
              localObject3 = ((BufferedImage)localObject1).createGraphics();
              ((Graphics2D)localObject3).drawImage((Image)localObject2, 0, 0, null);
            }
          }
          Object localObject2 = System.getProperty("org.jpedal.compress_tiff");
          Object localObject3 = System.getProperty("org.jpedal.jpeg_dpi");
          int m = localObject2 != null ? 1 : 0;
          JAIHelper.confirmJAIOnClasspath();
          if (this.imageType.startsWith("jp"))
          {
            int n = ((BufferedImage)localObject1).getWidth();
            int i1 = ((BufferedImage)localObject1).getHeight();
            BufferedImage localBufferedImage = new BufferedImage(n, i1, 1);
            Graphics2D localGraphics2D = (Graphics2D)localBufferedImage.getGraphics();
            localGraphics2D.setPaint(this.backgroundColor);
            localGraphics2D.fillRect(0, 0, n, i1);
            localGraphics2D.drawImage((Image)localObject1, 0, 0, null);
            localObject1 = localBufferedImage;
          }
          if (testing)
          {
            this.decode_pdf.getObjectStore().saveStoredImage(str2, (BufferedImage)localObject1, true, false, this.imageType);
          }
          else if ((JAIHelper.isJAIused()) && (this.imageType.startsWith("tif")))
          {
            LogWriter.writeLog("Saving image with JAI " + str2 + '.' + this.imageType);
            localObject4 = new FileOutputStream(str2 + ".tif");
            TIFFEncodeParam localTIFFEncodeParam = null;
            if (m != 0)
            {
              localTIFFEncodeParam = new TIFFEncodeParam();
              localTIFFEncodeParam.setCompression(32946);
            }
            JAI.create("encode", (RenderedImage)localObject1, localObject4, "TIFF", localTIFFEncodeParam);
            ((FileOutputStream)localObject4).flush();
            ((FileOutputStream)localObject4).close();
          }
          else if ((localObject3 != null) && (this.imageType.startsWith("jp")) && (JAIHelper.isJAIused()))
          {
            saveAsJPEG((String)localObject3, (BufferedImage)localObject1, 1.0F, new FileOutputStream(this.output_dir + paramInt1 + paramString3 + '.' + this.imageType));
          }
          else
          {
            LogWriter.writeLog("Saving image " + str2 + '.' + this.imageType);
            localObject4 = new BufferedOutputStream(new FileOutputStream(new File(str2 + '.' + this.imageType)));
            ImageIO.write((RenderedImage)localObject1, this.imageType, (OutputStream)localObject4);
            ((BufferedOutputStream)localObject4).flush();
            ((BufferedOutputStream)localObject4).close();
          }
          Object localObject4 = new OutputStreamWriter(new FileOutputStream(str2 + ".xml"), "UTF-8");
          ((OutputStreamWriter)localObject4).write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
          ((OutputStreamWriter)localObject4).write("<!-- Pixel Location of image x1,y1,x2,y2\n");
          ((OutputStreamWriter)localObject4).write("(x1,y1 is top left corner)\n");
          ((OutputStreamWriter)localObject4).write("(origin is bottom left corner)  -->\n");
          ((OutputStreamWriter)localObject4).write("\n\n<META>\n");
          ((OutputStreamWriter)localObject4).write(paramString2);
          ((OutputStreamWriter)localObject4).write("<FILE>" + paramString1 + "</FILE>\n");
          ((OutputStreamWriter)localObject4).write("<ORIGINALHEIGHT>" + k + "</ORIGINALHEIGHT>\n");
          ((OutputStreamWriter)localObject4).write("<SCALEDHEIGHT>" + ((BufferedImage)localObject1).getHeight() + "</SCALEDHEIGHT>\n");
          ((OutputStreamWriter)localObject4).write("<SCALING>" + f + "</SCALING>\n");
          ((OutputStreamWriter)localObject4).write("</META>\n");
          ((OutputStreamWriter)localObject4).close();
        }
      }
      catch (Exception localException)
      {
        LogWriter.writeLog("Exception " + localException + " in extracting images");
      }
  }

  public String getOutputDir()
  {
    return this.output_dir;
  }

  public static void main(String[] paramArrayOfString)
  {
    long l1 = System.currentTimeMillis();
    Messages.setBundle(ResourceBundle.getBundle("org.jpedal.international.messages"));
    if (outputMessages)
      System.out.println("Simple demo to extract images from a page at various heights");
    if ((paramArrayOfString.length >= 5) && (paramArrayOfString.length % 2 == 1))
    {
      LogWriter.writeLog("Values read");
      LogWriter.writeLog("inputDir=" + paramArrayOfString[0]);
      LogWriter.writeLog("processedDir=" + paramArrayOfString[1]);
      LogWriter.writeLog("logFile=" + paramArrayOfString[2]);
      LogWriter.writeLog("Directory and height pair values" + paramArrayOfString[3] + " <> " + paramArrayOfString[4] + '<');
      outputCount = (paramArrayOfString.length - 3) / 2;
      for (int i = 0; i < outputCount; i++)
      {
        LogWriter.writeLog(paramArrayOfString[(i + 3)]);
        if ((i % 2 == 0) && (!paramArrayOfString[(i + 3)].matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+")))
          exit("Invalid value: " + paramArrayOfString[(i + 3)]);
      }
    }
    else if ((paramArrayOfString.length - 3) % 2 == 1)
    {
      exit("Value/Directory pairs invalid");
    }
    else
    {
      System.out.println("Requires");
      System.out.println("inputDir processedDir logFile");
      System.out.println("height Directory (as many pairs as you like)");
      exit("Not enough parameters passed to software");
    }
    inputDir = paramArrayOfString[0];
    processed_dir = paramArrayOfString[1];
    String str = System.getProperty("org.jpedal.logging");
    if ((str != null) && (str.toLowerCase().equals("true")))
    {
      LogWriter.log_name = paramArrayOfString[2];
      LogWriter.setupLogFile("");
    }
    File localFile1 = new File(inputDir);
    File localFile2 = new File(processed_dir);
    if (!localFile2.exists())
      localFile2.mkdirs();
    if (!localFile1.exists())
      exit("Directory " + inputDir + " not found");
    outputSizes = new float[outputCount];
    outputDirectories = new String[outputCount];
    for (int j = 0; j < outputCount; j++)
    {
      try
      {
        outputSizes[j] = Float.parseFloat(paramArrayOfString[(3 + j * 2)]);
      }
      catch (Exception localException1)
      {
        exit("Exception " + localException1 + " reading integer " + paramArrayOfString[(3 + j * 2)]);
      }
      try
      {
        outputDirectories[j] = paramArrayOfString[(4 + j * 2)];
        if ((!outputDirectories[j].endsWith("\\")) && (!outputDirectories[j].endsWith("/")))
          outputDirectories[j] = (outputDirectories[j] + separator);
        File localFile3 = new File(outputDirectories[j]);
        if (!localFile3.exists())
          localFile3.mkdirs();
      }
      catch (Exception localException2)
      {
        exit("Exception " + localException2 + " with directory " + paramArrayOfString[(4 + j * 2)]);
      }
    }
    new ExtractClippedImages(inputDir);
    LogWriter.writeLog("Process completed");
    long l2 = System.currentTimeMillis();
    System.out.println("Took " + (l2 - l1) / 1000L + " seconds");
  }

  private static void saveAsJPEG(String paramString, BufferedImage paramBufferedImage, float paramFloat, FileOutputStream paramFileOutputStream)
    throws IOException
  {
    JPEGImageWriter localJPEGImageWriter = (JPEGImageWriter)ImageIO.getImageWritersBySuffix("jpeg").next();
    ImageOutputStream localImageOutputStream = ImageIO.createImageOutputStream(paramFileOutputStream);
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
 * Qualified Name:     org.jpedal.examples.images.ExtractClippedImages
 * JD-Core Version:    0.6.2
 */