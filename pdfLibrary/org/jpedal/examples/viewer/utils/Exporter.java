package org.jpedal.examples.viewer.utils;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.media.jai.JAI;
import javax.swing.ProgressMonitor;
import org.jpedal.PdfDecoder;
import org.jpedal.examples.viewer.gui.SwingGUI;
import org.jpedal.examples.viewer.gui.popups.SaveBitmap;
import org.jpedal.examples.viewer.gui.popups.SaveImage;
import org.jpedal.examples.viewer.gui.popups.SaveText;
import org.jpedal.exception.PdfException;
import org.jpedal.exception.PdfSecurityException;
import org.jpedal.grouping.PdfGroupingAlgorithms;
import org.jpedal.gui.GUIFactory;
import org.jpedal.io.ColorSpaceConvertor;
import org.jpedal.io.JAIHelper;
import org.jpedal.io.ObjectStore;
import org.jpedal.objects.PdfImageData;
import org.jpedal.objects.PdfPageData;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.Messages;
import org.jpedal.utils.Strip;
import org.jpedal.utils.SwingWorker;

public class Exporter
{
  public static final int RECTANGLE = 1;
  public static final int WORDLIST = 2;
  public static final int TABLE = 3;
  private final String separator = System.getProperty("file.separator");
  private String fileName = "";
  private GUIFactory currentGUI;
  private PdfDecoder dPDF;
  private String selectedFile;

  public Exporter(SwingGUI paramSwingGUI, String paramString, PdfDecoder paramPdfDecoder)
  {
    String str = new File(paramString).getName();
    if (str.lastIndexOf(46) != -1)
      str = str.substring(0, str.lastIndexOf(46));
    StringBuilder localStringBuilder = new StringBuilder(str);
    int i;
    while ((i = localStringBuilder.toString().indexOf("%20")) != -1)
      localStringBuilder.replace(i, i + 3, " ");
    this.fileName = localStringBuilder.toString();
    this.currentGUI = paramSwingGUI;
    this.selectedFile = paramString;
    this.dPDF = paramPdfDecoder;
  }

  public void extractPagesAsImages(SaveBitmap paramSaveBitmap)
  {
    final int i = paramSaveBitmap.getStartPage();
    final int j = paramSaveBitmap.getEndPage();
    if ((i < 1) || (j < 1))
      return;
    final String str1 = paramSaveBitmap.getPrefix();
    final int k = paramSaveBitmap.getScaling();
    final String str2 = new StringBuilder().append(paramSaveBitmap.getRootDir()).append(this.separator).append(this.fileName).append(this.separator).append("thumbnails").append(this.separator).toString();
    File localFile = new File(str2);
    if (!localFile.exists())
      localFile.mkdirs();
    final ProgressMonitor localProgressMonitor = new ProgressMonitor(this.currentGUI.getFrame(), Messages.getMessage("PdfViewerMessage.GeneratingBitmaps"), "", i, j);
    SwingWorker local1 = new SwingWorker()
    {
      public Object construct()
      {
        int i = 0;
        int j = 0;
        for (int k = i; k < j + 1; k++)
        {
          if (localProgressMonitor.isCanceled())
          {
            Exporter.this.currentGUI.showMessageDialog(Messages.getMessage("PdfViewerError.UserStoppedExport") + i + ' ' + Messages.getMessage("PdfViewerError.ReportNumberOfImagesExported"));
            return null;
          }
          BufferedImage localBufferedImage = null;
          try
          {
            localBufferedImage = Exporter.this.dPDF.getPageAsImage(k);
          }
          catch (PdfException localPdfException)
          {
            localPdfException.printStackTrace();
          }
          if (localBufferedImage != null)
          {
            if (k != 100)
            {
              int m = localBufferedImage.getWidth() * k / 100;
              Image localImage = localBufferedImage.getScaledInstance(m, -1, 4);
              localBufferedImage = new BufferedImage(localImage.getWidth(null), localImage.getHeight(null), 1);
              Graphics2D localGraphics2D = localBufferedImage.createGraphics();
              localGraphics2D.drawImage(localImage, 0, 0, null);
            }
            File localFile = new File(str2 + k + '.' + str1);
            if ((localFile.exists()) && (j == 0))
            {
              int n;
              if (j - i > 1)
              {
                n = Exporter.this.currentGUI.showOverwriteDialog(localFile.getAbsolutePath(), true);
                if (n != 0)
                  if (n == 1)
                  {
                    j = 1;
                  }
                  else
                  {
                    if (n == 2)
                    {
                      localProgressMonitor.setProgress(k);
                      continue;
                    }
                    Exporter.this.currentGUI.showMessageDialog(Messages.getMessage("PdfViewerError.UserStoppedExport") + i + ' ' + Messages.getMessage("PdfViewerError.ReportNumberOfPagesExported"));
                    localProgressMonitor.close();
                    return null;
                  }
              }
              else
              {
                n = Exporter.this.currentGUI.showOverwriteDialog(localFile.getAbsolutePath(), false);
                if (n != 0)
                  return null;
              }
            }
            Exporter.this.dPDF.getObjectStore().saveStoredImage(str2 + k, localBufferedImage, true, false, str1);
          }
          else
          {
            i++;
            localProgressMonitor.setProgress(k + 1);
          }
        }
        Exporter.this.currentGUI.showMessageDialog(Messages.getMessage("PdfViewerTitle.PagesSavedAsImages") + ' ' + str2);
        return null;
      }
    };
    local1.start();
  }

  private static void saveImage(BufferedImage paramBufferedImage, String paramString1, String paramString2)
  {
    if (JAIHelper.isJAIused())
      JAIHelper.confirmJAIOnClasspath();
    if ((paramString2.contains("tif")) && (JAIHelper.isJAIused()))
      try
      {
        FileOutputStream localFileOutputStream = new FileOutputStream(paramString1);
        JAI.create("encode", paramBufferedImage, localFileOutputStream, "TIFF", null);
      }
      catch (FileNotFoundException localFileNotFoundException)
      {
        localFileNotFoundException.printStackTrace();
      }
    else
      try
      {
        ImageIO.write(paramBufferedImage, paramString2, new File(paramString1));
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
      }
  }

  private void decodeHires(int paramInt1, int paramInt2, String paramString1, String paramString2)
  {
    PdfDecoder localPdfDecoder = null;
    String str1 = "";
    try
    {
      localPdfDecoder = new PdfDecoder(false);
      localPdfDecoder.setExtractionMode(36, 1.0F);
      localPdfDecoder.openPdfFile(this.selectedFile);
    }
    catch (Exception localException1)
    {
      localException1.printStackTrace();
    }
    if ((localPdfDecoder.isEncrypted()) && (!localPdfDecoder.isPasswordSupplied()) && (!localPdfDecoder.isExtractionAllowed()))
      return;
    ProgressMonitor localProgressMonitor = new ProgressMonitor(this.currentGUI.getFrame(), Messages.getMessage("PdfViewerMessage.ExtractImages"), "", paramInt1, paramInt2);
    try
    {
      int i = 0;
      int j = 0;
      for (int k = paramInt1; k < paramInt2 + 1; k++)
      {
        if (localProgressMonitor.isCanceled())
        {
          this.currentGUI.showMessageDialog(new StringBuilder().append(Messages.getMessage("PdfViewerError.UserStoppedExport")).append(i).append(' ').append(Messages.getMessage("PdfViewerError.ReportNumberOfImagesExported")).toString());
          return;
        }
        localPdfDecoder.decodePage(k);
        PdfImageData localPdfImageData = localPdfDecoder.getPdfImageData();
        int m = localPdfImageData.getImageCount();
        if (m > 0)
        {
          str1 = new StringBuilder().append(paramString2).append(k).append(this.separator).toString();
          File localFile = new File(str1);
          if (!localFile.exists())
            localFile.mkdir();
        }
        for (int n = 0; n < m; n++)
        {
          String str2 = localPdfImageData.getImageName(n);
          float f1 = localPdfImageData.getImageXCoord(n);
          float f2 = localPdfImageData.getImageYCoord(n);
          float f3 = localPdfImageData.getImageWidth(n);
          float f4 = localPdfImageData.getImageHeight(n);
          try
          {
            BufferedImage localBufferedImage = localPdfDecoder.getObjectStore().loadStoredImage(new StringBuilder().append("CLIP_").append(str2).toString());
            Object localObject;
            if (localBufferedImage != null)
            {
              if (paramString1.toLowerCase().startsWith("jp"))
                localBufferedImage = ColorSpaceConvertor.convertToRGB(localBufferedImage);
              localObject = new File(new StringBuilder().append(str1).append(str2).append('.').append(paramString1).toString());
              if ((((File)localObject).exists()) && (j == 0))
              {
                int i1 = this.currentGUI.showOverwriteDialog(((File)localObject).getAbsolutePath(), true);
                if (i1 != 0)
                  if (i1 == 1)
                  {
                    j = 1;
                  }
                  else
                  {
                    if (i1 == 2)
                    {
                      localProgressMonitor.setProgress(k);
                      continue;
                    }
                    this.currentGUI.showMessageDialog(new StringBuilder().append(Messages.getMessage("PdfViewerError.UserStoppedExport")).append(i).append(' ').append(Messages.getMessage("PdfViewerError.ReportNumberOfImagesExported")).toString());
                    localProgressMonitor.close();
                    return;
                  }
              }
              saveImage(localBufferedImage, new StringBuilder().append(str1).append(str2).append('.').append(paramString1).toString(), paramString1);
              i++;
            }
            else
            {
              localObject = new OutputStreamWriter(new FileOutputStream(new StringBuilder().append(str1).append(str2).append(".xml").toString()), "UTF-8");
              ((OutputStreamWriter)localObject).write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
              ((OutputStreamWriter)localObject).write("<!-- Pixel Location of image x1,y1,x2,y2\n");
              ((OutputStreamWriter)localObject).write("(x1,y1 is top left corner)\n");
              ((OutputStreamWriter)localObject).write("(origin is bottom left corner)  -->\n");
              ((OutputStreamWriter)localObject).write("\n\n<META>\n");
              ((OutputStreamWriter)localObject).write(new StringBuilder().append("<PAGELOCATION x1=\"").append(f1).append("\" ").append("y1=\"").append(f2 + f4).append("\" ").append("x2=\"").append(f1 + f3).append("\" ").append("y2=\"").append(f2).append("\" />\n").toString());
              ((OutputStreamWriter)localObject).write(new StringBuilder().append("<FILE>").append(this.fileName).append("</FILE>\n").toString());
              ((OutputStreamWriter)localObject).write("</META>\n");
              ((OutputStreamWriter)localObject).close();
            }
          }
          catch (Exception localException3)
          {
            localException3.printStackTrace();
            LogWriter.writeLog(new StringBuilder().append("Exception ").append(localException3).append(" in extracting images").toString());
          }
        }
        localPdfDecoder.flushObjectValues(true);
        localProgressMonitor.setProgress(k + 1);
      }
      localProgressMonitor.close();
      this.currentGUI.showMessageDialog(new StringBuilder().append(Messages.getMessage("PdfViewerMessage.ImagesSavedTo")).append(' ').append(paramString2).toString());
    }
    catch (Exception localException2)
    {
      localPdfDecoder.closePdfFile();
      LogWriter.writeLog(new StringBuilder().append("Exception ").append(localException2.getMessage()).toString());
    }
    localPdfDecoder.closePdfFile();
  }

  public void extractImagesOnPages(SaveImage paramSaveImage)
  {
    final int i = paramSaveImage.getStartPage();
    final int j = paramSaveImage.getEndPage();
    if ((i < 1) || (j < 1))
      return;
    final int k = paramSaveImage.getImageType();
    final String str1 = paramSaveImage.getPrefix();
    final String str2 = new StringBuilder().append(paramSaveImage.getRootDir()).append(this.separator).append(this.fileName).append(this.separator).append("images").append(this.separator).toString();
    File localFile = new File(str2);
    if (!localFile.exists())
      localFile.mkdirs();
    SwingWorker local2 = new SwingWorker()
    {
      public Object construct()
      {
        switch (k)
        {
        case 32:
          Exporter.this.decodeHires(i, j, str1, str2);
          break;
        case 2:
          Exporter.this.decodeImages(i, j, str1, str2, false);
          break;
        case 4:
          Exporter.this.decodeImages(i, j, str1, str2, true);
          break;
        default:
          System.out.println("Unknown setting");
        }
        return null;
      }
    };
    local2.start();
  }

  private void decodeImages(int paramInt1, int paramInt2, String paramString1, String paramString2, boolean paramBoolean)
  {
    PdfDecoder localPdfDecoder = null;
    try
    {
      localPdfDecoder = new PdfDecoder(false);
      localPdfDecoder.setExtractionMode(6, 1.0F);
      localPdfDecoder.openPdfFile(this.selectedFile);
    }
    catch (Exception localException1)
    {
      localException1.printStackTrace();
    }
    if ((localPdfDecoder.isEncrypted()) && (!localPdfDecoder.isPasswordSupplied()) && (!localPdfDecoder.isExtractionAllowed()))
      return;
    ProgressMonitor localProgressMonitor = new ProgressMonitor(this.currentGUI.getFrame(), Messages.getMessage("PdfViewerMessage.ExtractImages"), "", paramInt1, paramInt2);
    try
    {
      int i = 0;
      int j = 0;
      for (int k = paramInt1; k < paramInt2 + 1; k++)
      {
        if (localProgressMonitor.isCanceled())
        {
          this.currentGUI.showMessageDialog(new StringBuilder().append(Messages.getMessage("PdfViewerError.UserStoppedExport")).append(i).append(Messages.getMessage("PdfViewerError.ReportNumberOfImagesExported")).toString());
          return;
        }
        localPdfDecoder.decodePage(k);
        PdfImageData localPdfImageData = localPdfDecoder.getPdfImageData();
        int m = localPdfImageData.getImageCount();
        String str1 = new StringBuilder().append(paramString2).append(this.separator).toString();
        if (paramBoolean)
          str1 = new StringBuilder().append(str1).append("downsampled").append(this.separator).append(k).append(this.separator).toString();
        else
          str1 = new StringBuilder().append(str1).append("normal").append(this.separator).append(k).append(this.separator).toString();
        if (m > 0)
        {
          File localFile1 = new File(str1);
          if (!localFile1.exists())
            localFile1.mkdirs();
          localFile1 = new File(str1);
          if (!localFile1.exists())
            localFile1.mkdirs();
        }
        for (int n = 0; n < m; n++)
        {
          String str2 = localPdfImageData.getImageName(n);
          try
          {
            BufferedImage localBufferedImage;
            if (paramBoolean)
            {
              localBufferedImage = localPdfDecoder.getObjectStore().loadStoredImage(str2);
              if (paramString1.toLowerCase().startsWith("jp"))
                localBufferedImage = ColorSpaceConvertor.convertToRGB(localBufferedImage);
            }
            else
            {
              localBufferedImage = localPdfDecoder.getObjectStore().loadStoredImage(str2);
              if (paramString1.toLowerCase().startsWith("jp"))
                localBufferedImage = ColorSpaceConvertor.convertToRGB(localBufferedImage);
            }
            File localFile2 = new File(new StringBuilder().append(str1).append(str2).append('.').append(paramString1).toString());
            if ((localFile2.exists()) && (j == 0))
            {
              int i1 = this.currentGUI.showOverwriteDialog(localFile2.getAbsolutePath(), true);
              if (i1 != 0)
                if (i1 == 1)
                {
                  j = 1;
                }
                else
                {
                  if (i1 == 2)
                  {
                    localProgressMonitor.setProgress(k);
                    continue;
                  }
                  this.currentGUI.showMessageDialog(new StringBuilder().append(Messages.getMessage("PdfViewerError.UserStoppedExport")).append(i).append(' ').append(Messages.getMessage("PdfViewerError.ReportNumberOfImagesExported")).toString());
                  localProgressMonitor.close();
                  return;
                }
            }
            saveImage(localBufferedImage, new StringBuilder().append(str1).append(str2).append('.').append(paramString1).toString(), paramString1);
            i++;
          }
          catch (Exception localException3)
          {
            System.err.println(new StringBuilder().append("Exception ").append(localException3).append(" in extracting images").toString());
          }
        }
        localPdfDecoder.flushObjectValues(true);
        localProgressMonitor.setProgress(k + 1);
      }
      this.currentGUI.showMessageDialog(new StringBuilder().append(Messages.getMessage("PdfViewerMessage.ImagesSavedTo")).append(' ').append(paramString2).toString());
      localProgressMonitor.close();
    }
    catch (Exception localException2)
    {
      localPdfDecoder.closePdfFile();
      LogWriter.writeLog(new StringBuilder().append("Exception ").append(localException2.getMessage()).toString());
    }
    localPdfDecoder.closePdfFile();
  }

  public void extractTextOnPages(SaveText paramSaveText)
  {
    final int i = paramSaveText.getStartPage();
    final int j = paramSaveText.getEndPage();
    if ((i < 1) || (j < 1))
      return;
    final int k = paramSaveText.getTextType();
    final boolean bool = paramSaveText.isXMLExtaction();
    final String str = new StringBuilder().append(paramSaveText.getRootDir()).append(this.separator).append(this.fileName).append(this.separator).append("text").append(this.separator).toString();
    File localFile = new File(str);
    if (!localFile.exists())
      localFile.mkdirs();
    SwingWorker local3 = new SwingWorker()
    {
      public Object construct()
      {
        switch (k)
        {
        case 1:
          Exporter.this.decodeTextRectangle(i, j, str, bool);
          break;
        case 2:
          Exporter.this.decodeTextWordlist(i, j, str, bool);
          break;
        case 3:
          Exporter.this.decodeTextTable(i, j, str, bool);
          break;
        default:
          System.out.println("Unknown setting");
        }
        return null;
      }
    };
    local3.start();
  }

  private void decodeTextTable(int paramInt1, int paramInt2, String paramString, boolean paramBoolean)
  {
    PdfDecoder localPdfDecoder = null;
    try
    {
      localPdfDecoder = new PdfDecoder(false);
      localPdfDecoder.setExtractionMode(1);
      PdfDecoder.init(true);
      localPdfDecoder.openPdfFile(this.selectedFile);
    }
    catch (Exception localException1)
    {
      System.err.println(new StringBuilder().append("Exception ").append(localException1).append(" in pdf code").toString());
    }
    if ((localPdfDecoder.isEncrypted()) && (!localPdfDecoder.isPasswordSupplied()) && (!localPdfDecoder.isExtractionAllowed()))
    {
      System.out.println("Encrypted settings");
      System.out.println("Please look at Viewer for code sample to handle such files");
    }
    else
    {
      ProgressMonitor localProgressMonitor = new ProgressMonitor(this.currentGUI.getFrame(), Messages.getMessage("PdfViewerMessage.ExtractText"), "", paramInt1, paramInt2);
      try
      {
        int i = 0;
        int j = 0;
        for (int k = paramInt1; k < paramInt2 + 1; k++)
        {
          if (localProgressMonitor.isCanceled())
          {
            this.currentGUI.showMessageDialog(new StringBuilder().append(Messages.getMessage("PdfViewerError.UserStoppedExport")).append(i).append(' ').append(Messages.getMessage("PdfViewerError.ReportNumberOfPagesExported")).toString());
            return;
          }
          localPdfDecoder.decodePage(k);
          PdfGroupingAlgorithms localPdfGroupingAlgorithms = localPdfDecoder.getGroupingObject();
          PdfPageData localPdfPageData = localPdfDecoder.getPdfPageData();
          int m = localPdfPageData.getMediaBoxX(k);
          int i1 = localPdfPageData.getMediaBoxWidth(k) + m;
          int i2 = localPdfPageData.getMediaBoxY(k);
          int n = localPdfPageData.getMediaBoxHeight(k) + i2;
          String str1 = "_text.csv";
          if (paramBoolean)
            str1 = "_xml.txt";
          String str2 = null;
          try
          {
            Map localMap = localPdfGroupingAlgorithms.extractTextAsTable(m, n, i1, i2, k, !paramBoolean, false, false, false, 0);
            str2 = (String)localMap.get("content");
          }
          catch (PdfException localPdfException)
          {
            localPdfDecoder.closePdfFile();
            System.err.println(new StringBuilder().append("Exception ").append(localPdfException.getMessage()).append(" with table extraction").toString());
          }
          catch (Error localError2)
          {
            localError2.printStackTrace();
          }
          if (str2 == null)
          {
            System.out.println("No text found");
          }
          else
          {
            String str3 = new StringBuilder().append(paramString).append(this.separator).append("table").append(this.separator).toString();
            File localFile1 = new File(str3);
            if (!localFile1.exists())
              localFile1.mkdirs();
            File localFile2 = new File(new StringBuilder().append(str3).append(this.fileName).append('_').append(k).append(str1).toString());
            if ((localFile2.exists()) && (j == 0))
            {
              int i3;
              if (paramInt2 - paramInt1 > 1)
              {
                i3 = this.currentGUI.showOverwriteDialog(localFile2.getAbsolutePath(), true);
                if (i3 != 0)
                  if (i3 == 1)
                  {
                    j = 1;
                  }
                  else
                  {
                    if (i3 == 2)
                    {
                      localProgressMonitor.setProgress(k);
                      continue;
                    }
                    this.currentGUI.showMessageDialog(new StringBuilder().append(Messages.getMessage("PdfViewerError.UserStoppedExport")).append(i).append(' ').append(Messages.getMessage("PdfViewerError.ReportNumberOfPagesExported")).toString());
                    localProgressMonitor.close();
                    return;
                  }
              }
              else
              {
                i3 = this.currentGUI.showOverwriteDialog(localFile2.getAbsolutePath(), false);
                if (i3 != 0)
                  return;
              }
            }
            OutputStreamWriter localOutputStreamWriter = new OutputStreamWriter(new FileOutputStream(new StringBuilder().append(str3).append(this.fileName).append('_').append(k).append(str1).toString()), "UTF-8");
            if (paramBoolean)
              localOutputStreamWriter.write("<xml><BODY>\n\n");
            localOutputStreamWriter.write(str2);
            if (paramBoolean)
              localOutputStreamWriter.write("\n</body></xml>");
            localOutputStreamWriter.close();
          }
          i++;
          localProgressMonitor.setProgress(k + 1);
          localPdfDecoder.flushObjectValues(false);
        }
        localProgressMonitor.close();
        this.currentGUI.showMessageDialog(new StringBuilder().append(Messages.getMessage("PdfViewerMessage.TextSavedTo")).append(' ').append(paramString).toString());
      }
      catch (Exception localException2)
      {
        localPdfDecoder.closePdfFile();
        System.err.println(new StringBuilder().append("Exception ").append(localException2.getMessage()).toString());
        localException2.printStackTrace();
      }
      catch (Error localError1)
      {
        System.out.println("h34343");
        localError1.printStackTrace();
      }
      localPdfDecoder.flushObjectValues(true);
    }
    localPdfDecoder.closePdfFile();
  }

  private void decodeTextWordlist(int paramInt1, int paramInt2, String paramString, boolean paramBoolean)
  {
    PdfDecoder localPdfDecoder = null;
    try
    {
      localPdfDecoder = new PdfDecoder(false);
      localPdfDecoder.setExtractionMode(1);
      PdfDecoder.init(true);
      PdfGroupingAlgorithms.useUnrotatedCoords = false;
      localPdfDecoder.openPdfFile(this.selectedFile);
    }
    catch (PdfSecurityException localPdfSecurityException)
    {
      System.err.println(new StringBuilder().append("Exception ").append(localPdfSecurityException).append(" in pdf code for wordlist").append(this.selectedFile).toString());
    }
    catch (PdfException localPdfException1)
    {
      System.err.println(new StringBuilder().append("Exception ").append(localPdfException1).append(" in pdf code for wordlist").append(this.selectedFile).toString());
    }
    catch (Exception localException1)
    {
      System.err.println(new StringBuilder().append("Exception ").append(localException1).append(" in pdf code for wordlist").append(this.selectedFile).toString());
      localException1.printStackTrace();
    }
    if ((localPdfDecoder.isEncrypted()) && (!localPdfDecoder.isPasswordSupplied()) && (!localPdfDecoder.isExtractionAllowed()))
    {
      System.out.println("Encrypted settings");
      System.out.println("Please look at Viewer for code sample to handle such files");
    }
    else
    {
      int i = paramInt1;
      int j = paramInt2;
      int k = 0;
      ProgressMonitor localProgressMonitor = new ProgressMonitor(this.currentGUI.getFrame(), Messages.getMessage("PdfViewerMessage.ExtractText"), "", paramInt1, paramInt2);
      try
      {
        int m = 0;
        int n = 0;
        for (int i1 = i; i1 < j + 1; i1++)
        {
          if (localProgressMonitor.isCanceled())
          {
            this.currentGUI.showMessageDialog(new StringBuilder().append(Messages.getMessage("PdfViewerError.UserStoppedExport")).append(m).append(' ').append(Messages.getMessage("PdfViewerError.ReportNumberOfPagesExported")).toString());
            return;
          }
          localPdfDecoder.decodePage(i1);
          PdfGroupingAlgorithms localPdfGroupingAlgorithms = localPdfDecoder.getGroupingObject();
          PdfPageData localPdfPageData = localPdfDecoder.getPdfPageData();
          int i2 = localPdfPageData.getMediaBoxX(i1);
          int i3 = localPdfPageData.getMediaBoxWidth(i1) + i2;
          int i4 = localPdfPageData.getMediaBoxX(i1);
          int i5 = localPdfPageData.getMediaBoxHeight(i1) - i4;
          List localList = null;
          try
          {
            localList = localPdfGroupingAlgorithms.extractTextAsWordlist(i2, i5, i3, i4, i1, true, "&:=()!;.,\\/\"\"''");
          }
          catch (PdfException localPdfException2)
          {
            localPdfDecoder.closePdfFile();
            System.err.println(new StringBuilder().append("Exception= ").append(localPdfException2).append(" in ").append(this.selectedFile).toString());
            localPdfException2.printStackTrace();
          }
          catch (Error localError2)
          {
            localError2.printStackTrace();
          }
          if (localList == null)
          {
            System.out.println("No text found");
          }
          else
          {
            String str1 = new StringBuilder().append(paramString).append(this.separator).append("wordlist").append(this.separator).toString();
            File localFile1 = new File(str1);
            if (!localFile1.exists())
              localFile1.mkdirs();
            String str2 = "_text.txt";
            String str3 = System.getProperty("file.encoding");
            if (paramBoolean)
            {
              str2 = "_xml.txt";
              str3 = "UTF-8";
            }
            int i6 = localList.size() / 5;
            k += i6;
            File localFile2 = new File(new StringBuilder().append(str1).append(this.fileName).append('_').append(i1).append(str2).toString());
            if ((localFile2.exists()) && (n == 0))
            {
              int i7;
              if (paramInt2 - paramInt1 > 1)
              {
                i7 = this.currentGUI.showOverwriteDialog(localFile2.getAbsolutePath(), true);
                if (i7 != 0)
                  if (i7 == 1)
                  {
                    n = 1;
                  }
                  else
                  {
                    if (i7 == 2)
                    {
                      localProgressMonitor.setProgress(i1);
                      continue;
                    }
                    this.currentGUI.showMessageDialog(new StringBuilder().append(Messages.getMessage("PdfViewerError.UserStoppedExport")).append(m).append(' ').append(Messages.getMessage("PdfViewerError.ReportNumberOfPagesExported")).toString());
                    localProgressMonitor.close();
                    return;
                  }
              }
              else
              {
                i7 = this.currentGUI.showOverwriteDialog(localFile2.getAbsolutePath(), false);
                if (i7 != 0)
                  return;
              }
            }
            OutputStreamWriter localOutputStreamWriter = new OutputStreamWriter(new FileOutputStream(new StringBuilder().append(str1).append(this.fileName).append('_').append(i1).append(str2).toString()), str3);
            Iterator localIterator = localList.iterator();
            while (localIterator.hasNext())
            {
              String str4 = (String)localIterator.next();
              if (!paramBoolean)
                str4 = Strip.convertToText(str4, true);
              int i8 = (int)Float.parseFloat((String)localIterator.next());
              int i9 = (int)Float.parseFloat((String)localIterator.next());
              int i10 = (int)Float.parseFloat((String)localIterator.next());
              int i11 = (int)Float.parseFloat((String)localIterator.next());
              localOutputStreamWriter.write(new StringBuilder().append(str4).append(',').append(i8).append(',').append(i9).append(',').append(i10).append(',').append(i11).append('\n').toString());
            }
            localOutputStreamWriter.close();
          }
          m++;
          localProgressMonitor.setProgress(i1 + 1);
          localPdfDecoder.flushObjectValues(false);
        }
        localProgressMonitor.close();
        this.currentGUI.showMessageDialog(new StringBuilder().append(Messages.getMessage("PdfViewerMessage.TextSavedTo")).append(' ').append(paramString).toString());
      }
      catch (Exception localException2)
      {
        localPdfDecoder.closePdfFile();
        System.err.println(new StringBuilder().append("Exception ").append(localException2).append(" in ").append(this.selectedFile).toString());
        localException2.printStackTrace();
      }
      catch (Error localError1)
      {
        localError1.printStackTrace();
      }
    }
    localPdfDecoder.closePdfFile();
  }

  private void decodeTextRectangle(int paramInt1, int paramInt2, String paramString, boolean paramBoolean)
  {
    PdfDecoder localPdfDecoder = null;
    try
    {
      localPdfDecoder = new PdfDecoder(false);
      if (!paramBoolean)
        localPdfDecoder.useTextExtraction();
      localPdfDecoder.setExtractionMode(1);
      PdfDecoder.init(true);
      localPdfDecoder.openPdfFile(this.selectedFile);
    }
    catch (PdfSecurityException localPdfSecurityException)
    {
      System.err.println(new StringBuilder().append("Security Exception ").append(localPdfSecurityException).append(" in pdf code for text extraction on file ").toString());
    }
    catch (PdfException localPdfException1)
    {
      System.err.println(new StringBuilder().append("Pdf Exception ").append(localPdfException1).append(" in pdf code for text extraction on file ").toString());
    }
    catch (Exception localException1)
    {
      System.err.println(new StringBuilder().append("Exception ").append(localException1).append(" in pdf code for text extraction on file ").toString());
      localException1.printStackTrace();
    }
    if ((localPdfDecoder.isEncrypted()) && (!localPdfDecoder.isPasswordSupplied()) && (!localPdfDecoder.isExtractionAllowed()))
    {
      System.out.println("Encrypted settings");
      System.out.println("Please look at Viewer for code sample to handle such files");
    }
    else
    {
      ProgressMonitor localProgressMonitor = new ProgressMonitor(this.currentGUI.getFrame(), Messages.getMessage("PdfViewerMessage.ExtractText"), "", paramInt1, paramInt2);
      try
      {
        int i = 0;
        int j = 0;
        for (int k = paramInt1; k < paramInt2 + 1; k++)
        {
          if (localProgressMonitor.isCanceled())
          {
            this.currentGUI.showMessageDialog(new StringBuilder().append(Messages.getMessage("PdfViewerError.UserStoppedExport")).append(i).append(' ').append(Messages.getMessage("PdfViewerError.ReportNumberOfPagesExported")).toString());
            return;
          }
          localPdfDecoder.decodePage(k);
          PdfGroupingAlgorithms localPdfGroupingAlgorithms = localPdfDecoder.getGroupingObject();
          PdfPageData localPdfPageData = localPdfDecoder.getPdfPageData();
          int m = localPdfPageData.getMediaBoxX(k);
          int n = localPdfPageData.getMediaBoxWidth(k) + m;
          int i1 = localPdfPageData.getMediaBoxY(k);
          int i2 = localPdfPageData.getMediaBoxHeight(k) + i1;
          String str1 = null;
          try
          {
            str1 = localPdfGroupingAlgorithms.extractTextInRectangle(m, i2, n, i1, k, false, true);
          }
          catch (PdfException localPdfException2)
          {
            localPdfDecoder.closePdfFile();
            System.err.println(new StringBuilder().append("Exception ").append(localPdfException2.getMessage()).append(" in file ").append(localPdfDecoder.getObjectStore().fullFileName).toString());
            localPdfException2.printStackTrace();
          }
          if (str1 != null)
          {
            String str2 = new StringBuilder().append(paramString).append(this.separator).append("rectangle").append(this.separator).toString();
            File localFile1 = new File(str2);
            if (!localFile1.exists())
              localFile1.mkdirs();
            String str3 = "_text.txt";
            String str4 = System.getProperty("file.encoding");
            if (paramBoolean)
            {
              str3 = "_xml.txt";
              str4 = "UTF-8";
            }
            File localFile2 = new File(new StringBuilder().append(str2).append(this.fileName).append('_').append(k).append(str3).toString());
            if ((localFile2.exists()) && (j == 0))
            {
              int i3;
              if (paramInt2 - paramInt1 > 1)
              {
                i3 = this.currentGUI.showOverwriteDialog(localFile2.getAbsolutePath(), true);
                if (i3 != 0)
                  if (i3 == 1)
                  {
                    j = 1;
                  }
                  else
                  {
                    if (i3 == 2)
                    {
                      localProgressMonitor.setProgress(k);
                      continue;
                    }
                    this.currentGUI.showMessageDialog(new StringBuilder().append(Messages.getMessage("PdfViewerError.UserStoppedExport")).append(i).append(' ').append(Messages.getMessage("PdfViewerError.ReportNumberOfPagesExported")).toString());
                    localProgressMonitor.close();
                    return;
                  }
              }
              else
              {
                i3 = this.currentGUI.showOverwriteDialog(localFile2.getAbsolutePath(), false);
                if (i3 != 0)
                  return;
              }
            }
            OutputStreamWriter localOutputStreamWriter = new OutputStreamWriter(new FileOutputStream(new StringBuilder().append(str2).append(this.fileName).append('_').append(k).append(str3).toString()), str4);
            if (paramBoolean)
            {
              localOutputStreamWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n");
              localOutputStreamWriter.write("<!-- Pixel Location of text x1,y1,x2,y2\n");
              localOutputStreamWriter.write("(x1,y1 is top left corner)\n");
              localOutputStreamWriter.write("(x1,y1 is bottom right corner)\n");
              localOutputStreamWriter.write("(origin is bottom left corner)  -->\n");
              localOutputStreamWriter.write("\n\n<ARTICLE>\n");
              localOutputStreamWriter.write(new StringBuilder().append("<LOCATION x1=\"").append(m).append("\" ").append("y1=\"").append(i2).append("\" ").append("x2=\"").append(n).append("\" ").append("y2=\"").append(i1).append("\" />\n").toString());
              localOutputStreamWriter.write("\n\n<TEXT>\n");
              localOutputStreamWriter.write(str1);
              localOutputStreamWriter.write("\n\n</TEXT>\n");
              localOutputStreamWriter.write("\n\n</ARTICLE>\n");
            }
            else
            {
              localOutputStreamWriter.write(str1);
            }
            i++;
            localOutputStreamWriter.close();
            localProgressMonitor.setProgress(k + 1);
            localPdfDecoder.flushObjectValues(true);
          }
        }
        localProgressMonitor.close();
        this.currentGUI.showMessageDialog(new StringBuilder().append(Messages.getMessage("PdfViewerMessage.TextSavedTo")).append(' ').append(paramString).toString());
      }
      catch (Exception localException2)
      {
        localPdfDecoder.closePdfFile();
        System.err.println(new StringBuilder().append("Exception ").append(localException2.getMessage()).toString());
        localException2.printStackTrace();
        System.out.println(localPdfDecoder.getObjectStore().getCurrentFilename());
      }
    }
    localPdfDecoder.closePdfFile();
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.utils.Exporter
 * JD-Core Version:    0.6.2
 */