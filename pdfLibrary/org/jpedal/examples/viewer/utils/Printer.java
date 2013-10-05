package org.jpedal.examples.viewer.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.color.ColorSpace;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import javax.print.DocFlavor.SERVICE_FORMATTED;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.SimpleDoc;
import javax.print.attribute.Attribute;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.SetOfIntegerSyntax;
import javax.print.attribute.standard.Chromaticity;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.JobName;
import javax.print.attribute.standard.PageRanges;
import javax.print.event.PrintJobEvent;
import javax.print.event.PrintJobListener;
import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;
import javax.swing.Timer;
import org.jpedal.PdfDecoder;
import org.jpedal.color.PdfPaint;
import org.jpedal.examples.viewer.gui.popups.PrintPanel;
import org.jpedal.exception.PdfException;
import org.jpedal.external.ColorHandler;
import org.jpedal.gui.GUIFactory;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.Messages;

public class Printer
{
  private static int printingThreads = 0;
  int rangeStart = 1;
  int rangeEnd = 1;
  int subset = 8;
  boolean wasCancelled = false;
  boolean messageShown = false;
  boolean pagesReversed = false;
  Timer updatePrinterProgress = null;
  private ProgressMonitor status = null;
  private DocPrintJob printJob = null;
  public static boolean showOptionPane = true;
  private String dots = ".";

  public void printPDF(final PdfDecoder paramPdfDecoder, final GUIFactory paramGUIFactory, final String paramString1, final String paramString2)
  {
    printingThreads += 1;
    Thread local1 = new Thread()
    {
      public void run()
      {
        boolean bool = false;
        try
        {
          PageFormat localPageFormat = PrinterJob.getPrinterJob().defaultPage();
          Paper localPaper = new Paper();
          localPaper.setSize(595.0D, 842.0D);
          localPaper.setImageableArea(43.0D, 43.0D, 509.0D, 756.0D);
          localPageFormat.setPaper(localPaper);
          SimpleDoc localSimpleDoc = new SimpleDoc(paramPdfDecoder, DocFlavor.SERVICE_FORMATTED.PAGEABLE, null);
          HashPrintRequestAttributeSet localHashPrintRequestAttributeSet = new HashPrintRequestAttributeSet();
          localHashPrintRequestAttributeSet.add(new PageRanges(1, paramPdfDecoder.getPageCount()));
          PrintPanel localPrintPanel = (PrintPanel)paramGUIFactory.printDialog(Printer.getAvailablePrinters(paramString1), paramString2);
          bool = localPrintPanel.okClicked();
          paramPdfDecoder.repaint();
          Printer.this.setPrinter(localPrintPanel.getPrinter());
          int i = 0;
          Printer.this.subset = 8;
          if (localPrintPanel.isOddPagesOnly())
          {
            i = 16;
            Printer.this.subset = 16;
          }
          else if (localPrintPanel.isEvenPagesOnly())
          {
            i = 32;
            Printer.this.subset = 32;
          }
          Printer.this.pagesReversed = localPrintPanel.isPagesReversed();
          if (Printer.this.pagesReversed)
            i += 64;
          paramPdfDecoder.setPrintPageMode(i);
          SetOfIntegerSyntax localSetOfIntegerSyntax = localPrintPanel.getPrintRange();
          Object localObject1 = paramPdfDecoder.getExternalHandler(19);
          Object localObject2;
          if (localSetOfIntegerSyntax == null)
          {
            paramGUIFactory.showMessageDialog("No pages to print");
          }
          else
          {
            paramPdfDecoder.setPagePrintRange(localSetOfIntegerSyntax);
            Printer.this.rangeStart = localSetOfIntegerSyntax.next(0);
            int j = Printer.this.rangeStart;
            Printer.this.rangeEnd = Printer.this.rangeStart;
            if (localSetOfIntegerSyntax.contains(2147483647))
            {
              Printer.this.rangeEnd = paramPdfDecoder.getPageCount();
            }
            else
            {
              while (localSetOfIntegerSyntax.next(j) != -1)
                j++;
              Printer.this.rangeEnd = j;
            }
            localHashPrintRequestAttributeSet.add(new Copies(localPrintPanel.getCopies()));
            paramPdfDecoder.setPrintAutoRotateAndCenter(localPrintPanel.isAutoRotateAndCenter());
            paramPdfDecoder.setPrintCurrentView(localPrintPanel.isPrintingCurrentView());
            paramPdfDecoder.setPrintPageScalingMode(localPrintPanel.getPageScaling());
            if (localPrintPanel.isMonochrome())
            {
              localHashPrintRequestAttributeSet.remove(Chromaticity.COLOR);
              localHashPrintRequestAttributeSet.add(Chromaticity.MONOCHROME);
              paramPdfDecoder.addExternalHandler(new ColorHandler()
              {
                public void setPaint(Graphics2D paramAnonymous2Graphics2D, PdfPaint paramAnonymous2PdfPaint, int paramAnonymous2Int, boolean paramAnonymous2Boolean)
                {
                  if ((paramAnonymous2Boolean) && (paramAnonymous2PdfPaint != null))
                  {
                    int i = paramAnonymous2PdfPaint.getRGB();
                    float[] arrayOfFloat1 = new float[3];
                    arrayOfFloat1[0] = ((i >> 16 & 0xFF) / 255.0F);
                    arrayOfFloat1[1] = ((i >> 8 & 0xFF) / 255.0F);
                    arrayOfFloat1[2] = ((i & 0xFF) / 255.0F);
                    ColorSpace localColorSpace = ColorSpace.getInstance(1003);
                    float[] arrayOfFloat2 = localColorSpace.fromRGB(arrayOfFloat1);
                    Color localColor = new Color(localColorSpace, arrayOfFloat2, 1.0F);
                    paramAnonymous2Graphics2D.setPaint(localColor);
                  }
                  else
                  {
                    paramAnonymous2Graphics2D.setPaint(paramAnonymous2PdfPaint);
                  }
                }

                public BufferedImage processImage(BufferedImage paramAnonymous2BufferedImage, int paramAnonymous2Int, boolean paramAnonymous2Boolean)
                {
                  if ((paramAnonymous2Boolean) && (paramAnonymous2BufferedImage != null))
                  {
                    BufferedImage localBufferedImage = new BufferedImage(paramAnonymous2BufferedImage.getWidth(), paramAnonymous2BufferedImage.getHeight(), 10);
                    Graphics2D localGraphics2D = localBufferedImage.createGraphics();
                    localGraphics2D.setPaint(Color.WHITE);
                    localGraphics2D.fillRect(0, 0, paramAnonymous2BufferedImage.getWidth(), paramAnonymous2BufferedImage.getHeight());
                    localGraphics2D.drawImage(paramAnonymous2BufferedImage, 0, 0, null);
                    return localBufferedImage;
                  }
                  return paramAnonymous2BufferedImage;
                }
              }
              , 19);
            }
            else
            {
              localHashPrintRequestAttributeSet.remove(Chromaticity.MONOCHROME);
              localHashPrintRequestAttributeSet.add(Chromaticity.COLOR);
            }
            if (localPrintPanel.getSelectedPaper() != null)
              localPageFormat.setPaper(localPrintPanel.getSelectedPaper());
            localPageFormat.setOrientation(localPrintPanel.getSelectedPrinterOrientation());
            paramPdfDecoder.setPageFormat(localPageFormat);
            paramPdfDecoder.setUsePDFPaperSize(localPrintPanel.isPaperSourceByPDFSize());
            localObject2 = localPrintPanel.getResolution();
            if (localObject2 != null)
              localHashPrintRequestAttributeSet.add((Attribute)localObject2);
          }
          if (Printer.showOptionPane)
          {
            Printer.this.status = new ProgressMonitor(paramGUIFactory.getFrame(), "", "", 1, 100);
            Printer.this.updatePrinterProgress = new Timer(1000, new ActionListener()
            {
              public void actionPerformed(ActionEvent paramAnonymous2ActionEvent)
              {
                int i = Printer.1.this.val$decode_pdf.getCurrentPrintPage();
                if (i > 0)
                  Printer.this.updatePrinterProgess(Printer.1.this.val$decode_pdf, i);
                if (i == -1)
                {
                  Printer.this.updatePrinterProgress.stop();
                  Printer.this.status.close();
                }
              }
            });
            Printer.this.updatePrinterProgress.setRepeats(true);
            Printer.this.updatePrinterProgress.start();
          }
          String str2 = paramPdfDecoder.getFileName();
          if (str2 == null)
          {
            str2 = "JPedal printing";
          }
          else
          {
            localObject2 = paramPdfDecoder.getFileName().split("/");
            JobName localJobName = new JobName(localObject2[(localObject2.length - 1)], null);
            if (Printer.this.printJob.getPrintService().isAttributeValueSupported(localJobName, DocFlavor.SERVICE_FORMATTED.PAGEABLE, localHashPrintRequestAttributeSet))
              localHashPrintRequestAttributeSet.add(localJobName);
          }
          if (bool)
          {
            Printer.this.printJob.addPrintJobListener(new Printer.PDFPrintJobListener(null));
            Printer.this.printJob.print(localSimpleDoc, localHashPrintRequestAttributeSet);
          }
          paramPdfDecoder.addExternalHandler(localObject1, 19);
        }
        catch (PrinterException localPrinterException)
        {
          localPrinterException.printStackTrace();
          LogWriter.writeLog("Exception " + localPrinterException + " printing");
          paramGUIFactory.showMessageDialog(localPrinterException.getMessage() + ' ' + localPrinterException + ' ' + ' ' + localPrinterException.getCause());
        }
        catch (Exception localException)
        {
          LogWriter.writeLog("Exception " + localException + " printing");
          localException.printStackTrace();
          paramGUIFactory.showMessageDialog("Exception " + localException);
        }
        catch (Error localError)
        {
          localError.printStackTrace();
          LogWriter.writeLog("Error " + localError + " printing");
          paramGUIFactory.showMessageDialog("Error " + localError);
        }
        if (Printer.this.updatePrinterProgress != null)
        {
          Printer.this.updatePrinterProgress.stop();
          Printer.this.status.close();
        }
        if ((!bool) && (!paramPdfDecoder.isPageSuccessful()))
        {
          String str1 = Messages.getMessage("PdfViewerError.ProblemsEncountered") + paramPdfDecoder.getPageFailureMessage() + '\n';
          if (paramPdfDecoder.getPageFailureMessage().toLowerCase().contains("memory"))
            str1 = str1 + Messages.getMessage("PdfViewerError.RerunJava") + Messages.getMessage("PdfViewerError.RerunJava1") + Messages.getMessage("PdfViewerError.RerunJava2");
          paramGUIFactory.showMessageDialog(str1);
        }
        Printer.access$510();
        paramPdfDecoder.resetCurrentPrintPage();
        paramPdfDecoder.invalidate();
        paramPdfDecoder.updateUI();
        paramPdfDecoder.repaint();
        if ((bool) && (!Printer.this.wasCancelled) && (Printer.showOptionPane))
          paramGUIFactory.showMessageDialog(Messages.getMessage("PdfViewerPrintingFinished"));
      }
    };
    local1.setDaemon(true);
    local1.start();
  }

  public static String[] getAvailablePrinters(String paramString)
  {
    PrintService[] arrayOfPrintService = PrinterJob.lookupPrintServices();
    int i = arrayOfPrintService.length;
    String[] arrayOfString1 = new String[i];
    if (paramString != null)
    {
      String[] arrayOfString2 = paramString.split(",");
      int k = 0;
      for (Object localObject2 : arrayOfPrintService)
      {
        int i1 = 1;
        String str1 = localObject2.getName();
        for (String str2 : arrayOfString2)
          if (str2.contains("*"))
          {
            String str3 = str2.replace("*", "").trim();
            if (str1.contains(str3))
              i1 = 0;
          }
          else if (str1.toLowerCase().equals(str2.toLowerCase()))
          {
            i1 = 0;
          }
        if (i1 != 0)
        {
          arrayOfString1[k] = str1;
          k++;
        }
      }
      ??? = arrayOfString1;
      arrayOfString1 = new String[k];
      System.arraycopy(???, 0, arrayOfString1, 0, k);
    }
    else
    {
      for (int j = 0; j < i; j++)
        arrayOfString1[j] = arrayOfPrintService[j].getName();
    }
    return arrayOfString1;
  }

  private void updatePrinterProgess(PdfDecoder paramPdfDecoder, int paramInt)
  {
    int i = this.rangeEnd - this.rangeStart + 1;
    int j = paramInt - this.rangeStart;
    int k = i;
    int m = j;
    int n = (int)(m / k * 100.0F);
    if (this.status.isCanceled())
    {
      paramPdfDecoder.stopPrinting();
      this.updatePrinterProgress.stop();
      this.status.close();
      this.wasCancelled = true;
      printingThreads -= 1;
      if (!this.messageShown)
      {
        JOptionPane.showMessageDialog(null, Messages.getMessage("PdfViewerPrint.PrintingCanceled"));
        this.messageShown = true;
      }
      return;
    }
    this.dots += '.';
    if (this.dots.length() > 8)
      this.dots = ".";
    int i1 = j <= 0 ? 1 : 0;
    if (this.rangeStart == this.rangeEnd)
      i1 = 0;
    if (i1 != 0)
      i = this.rangeStart - this.rangeEnd + 1;
    int i2 = (int)(j / i * 100.0F);
    if ((i1 == 0) && (i2 < 1))
      i2 = 1;
    if (i1 != 0)
      i2 = -i2;
    if (this.pagesReversed)
      i2 = 100 - i2;
    this.status.setProgress(i2);
    if (this.subset == 16)
    {
      k = k / 2 + 1;
      m /= 2;
    }
    else if (this.subset == 32)
    {
      k = k / 2 + 1;
      m /= 2;
    }
    if (k == 1)
    {
      n = 50;
      m = 1;
      this.status.setProgress(n);
    }
    String str = m + " " + Messages.getMessage("PdfViewerPrint.Of") + ' ' + k + ": " + n + '%' + ' ' + this.dots;
    if (this.pagesReversed)
    {
      str = k - m + " " + Messages.getMessage("PdfViewerPrint.Of") + ' ' + k + ": " + i2 + '%' + ' ' + this.dots;
      this.status.setNote(Messages.getMessage("PdfViewerPrint.ReversedPrinting") + ' ' + str);
    }
    else if (i1 != 0)
    {
      this.status.setNote(Messages.getMessage("PdfViewerPrint.ReversedPrinting") + ' ' + str);
    }
    else
    {
      this.status.setNote(Messages.getMessage("PdfViewerPrint.Printing") + ' ' + str);
    }
  }

  public static boolean isPrinting()
  {
    return printingThreads > 0;
  }

  private void setPrinter(String paramString)
    throws PrinterException, PdfException
  {
    PrintService[] arrayOfPrintService = PrinterJob.lookupPrintServices();
    int i = arrayOfPrintService.length;
    int j = 0;
    for (int k = 0; k < i; k++)
      if (arrayOfPrintService[k].getName().contains(paramString))
      {
        this.printJob = arrayOfPrintService[k].createPrintJob();
        k = i;
        j = 1;
      }
    if (j == 0)
      throw new PdfException("Unknown printer " + paramString);
  }

  private static class PDFPrintJobListener
    implements PrintJobListener
  {
    private static final boolean showMessages = false;

    public void printDataTransferCompleted(PrintJobEvent paramPrintJobEvent)
    {
    }

    public void printJobCompleted(PrintJobEvent paramPrintJobEvent)
    {
    }

    public void printJobFailed(PrintJobEvent paramPrintJobEvent)
    {
    }

    public void printJobCanceled(PrintJobEvent paramPrintJobEvent)
    {
    }

    public void printJobNoMoreEvents(PrintJobEvent paramPrintJobEvent)
    {
    }

    public void printJobRequiresAttention(PrintJobEvent paramPrintJobEvent)
    {
    }
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.utils.Printer
 * JD-Core Version:    0.6.2
 */