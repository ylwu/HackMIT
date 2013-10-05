package org.jpedal.examples.printing;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.PrintStream;
import javax.print.DocFlavor.SERVICE_FORMATTED;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.SimpleDoc;
import javax.print.attribute.Attribute;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.JobName;
import javax.print.attribute.standard.PageRanges;
import javax.print.attribute.standard.SheetCollate;
import javax.print.event.PrintJobEvent;
import javax.print.event.PrintJobListener;
import org.jpedal.PdfDecoder;
import org.jpedal.fonts.FontMappings;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.PdfBook;

public class SilentPrint
{
  String printerName;
  private static boolean debugCode = false;
  private final String separator = System.getProperty("file.separator");
  private PdfDecoder pdfDecoder = null;
  public static boolean customSetting = false;
  public static int pageMark = 1;
  public static DocPrintJob printJob = null;

  public SilentPrint()
  {
  }

  public SilentPrint(String paramString1, String paramString2)
  {
    this.printerName = paramString2;
    boolean bool = false;
    try
    {
      bool = validatePrinter(paramString2);
    }
    catch (PrinterException localPrinterException1)
    {
      localPrinterException1.printStackTrace();
    }
    if (bool)
    {
      if (paramString1.toLowerCase().endsWith(".pdf"))
      {
        decodeAndPrintFile(paramString1);
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
        for (String str : arrayOfString1)
          if (str.toLowerCase().endsWith(".pdf"))
          {
            logMessage(paramString1 + str);
            decodeAndPrintFile(paramString1 + str);
            try
            {
              validatePrinter(paramString2);
            }
            catch (PrinterException localPrinterException2)
            {
              localPrinterException2.printStackTrace();
            }
          }
      }
    }
    else
      logMessage("FAILED TO IDENTIFY PRINTER");
  }

  private void decodeAndPrintFile(String paramString)
  {
    try
    {
      logMessage("Opening file :" + paramString + " to print.");
      this.pdfDecoder = new PdfDecoder(true);
      this.pdfDecoder.openPdfFile(paramString);
    }
    catch (Exception localException1)
    {
      reportError("Exception " + localException1 + " in pdf code");
    }
    if ((this.pdfDecoder.isEncrypted()) && (!this.pdfDecoder.isFileViewable()))
    {
      String str = null;
      try
      {
        if (str == null)
          this.pdfDecoder.setEncryptionPassword(str);
        else
          this.pdfDecoder.setEncryptionPassword("");
      }
      catch (Exception localException2)
      {
        localException2.printStackTrace();
      }
    }
    if ((this.pdfDecoder.isEncrypted()) && (!this.pdfDecoder.isFileViewable()))
    {
      logMessage("Encrypted settings");
    }
    else
    {
      FontMappings.setFontReplacements();
      printPages();
    }
    this.pdfDecoder.closePdfFile();
  }

  private void printPages()
  {
    PrintRequestAttributeSet localPrintRequestAttributeSet = getPrintRequestAttributeSet();
    this.pdfDecoder.setPrintAutoRotateAndCenter(true);
    this.pdfDecoder.setPrintPageScalingMode(2);
    PdfBook localPdfBook = new PdfBook(this.pdfDecoder, printJob.getPrintService(), localPrintRequestAttributeSet);
    localPdfBook.setChooseSourceByPdfPageSize(false);
    SimpleDoc localSimpleDoc = new SimpleDoc(localPdfBook, DocFlavor.SERVICE_FORMATTED.PAGEABLE, null);
    printJob.addPrintJobListener(new PDFPrintJobListener(null));
    try
    {
      printJob.print(localSimpleDoc, localPrintRequestAttributeSet);
    }
    catch (Exception localException)
    {
      LogWriter.writeLog("Exception " + localException + " printing");
    }
    if (!this.pdfDecoder.getPageFailureMessage().isEmpty())
      System.out.println("Errors reported from JPedal=" + this.pdfDecoder.getPageFailureMessage());
  }

  private PrintRequestAttributeSet getPrintRequestAttributeSet()
  {
    HashPrintRequestAttributeSet localHashPrintRequestAttributeSet = new HashPrintRequestAttributeSet();
    String[] arrayOfString = this.pdfDecoder.getFileName().split("/");
    JobName localJobName = new JobName(arrayOfString[(arrayOfString.length - 1)], null);
    if (validateAttribute(localJobName, localHashPrintRequestAttributeSet))
      localHashPrintRequestAttributeSet.add(localJobName);
    if (pageMark > 1)
    {
      localObject = new Copies(pageMark);
      SheetCollate localSheetCollate = SheetCollate.COLLATED;
      if ((validateAttribute((Attribute)localObject, localHashPrintRequestAttributeSet)) && (validateAttribute(localSheetCollate, localHashPrintRequestAttributeSet)))
      {
        localHashPrintRequestAttributeSet.add((Attribute)localObject);
        localHashPrintRequestAttributeSet.add(localSheetCollate);
      }
    }
    Object localObject = new PageRanges("1-10");
    if (validateAttribute((Attribute)localObject, localHashPrintRequestAttributeSet))
      localHashPrintRequestAttributeSet.add((Attribute)localObject);
    return localHashPrintRequestAttributeSet;
  }

  private static boolean validateAttribute(Attribute paramAttribute, PrintRequestAttributeSet paramPrintRequestAttributeSet)
  {
    return printJob.getPrintService().isAttributeValueSupported(paramAttribute, DocFlavor.SERVICE_FORMATTED.PAGEABLE, paramPrintRequestAttributeSet);
  }

  private static boolean validatePrinter(String paramString)
    throws PrinterException
  {
    boolean bool = false;
    PrintService[] arrayOfPrintService1 = PrinterJob.lookupPrintServices();
    int i = arrayOfPrintService1.length;
    for (int j = 0; j < i; j++)
      if (arrayOfPrintService1[j].getName().contains(paramString))
      {
        printJob = arrayOfPrintService1[j].createPrintJob();
        j = i;
        bool = true;
      }
    if (!bool)
    {
      String str = "";
      for (PrintService localPrintService : arrayOfPrintService1)
        str = str + '"' + localPrintService.getName() + "\",";
      reportError("Printer " + paramString + " not supported. Options=" + str);
    }
    return bool;
  }

  private static void logMessage(String paramString)
  {
    if (debugCode)
    {
      System.out.println(paramString);
      LogWriter.writeLog(paramString);
    }
  }

  private static void reportError(String paramString)
  {
    System.err.println(paramString);
    LogWriter.writeLog(paramString);
  }

  public static void main(String[] paramArrayOfString)
  {
    logMessage("Simple demo to print pages");
    if (paramArrayOfString.length != 2)
    {
      System.out.println("Printing needs 2 parameters");
      System.out.println("Parameter 1 - File name or directory (put in quotes if it contains spaces");
      System.out.println("Parameter 2- a printer name");
      System.out.println("---Available printers are---");
      try
      {
        PrintService[] arrayOfPrintService = PrinterJob.lookupPrintServices();
        for (Object localObject2 : arrayOfPrintService)
          System.out.println('"' + localObject2.getName() + '"');
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
      }
    }
    else
    {
      String str = paramArrayOfString[0];
      ??? = paramArrayOfString[1];
      logMessage("File :" + str);
      logMessage("Printer :" + (String)???);
      ??? = 0;
      boolean bool;
      try
      {
        bool = validatePrinter((String)???);
      }
      catch (PrinterException localPrinterException)
      {
        localPrinterException.printStackTrace();
      }
      File localFile = new File(str);
      if (!localFile.exists())
        logMessage("File " + str + " not found");
      else if (!bool)
        logMessage("Printer " + (String)??? + " not found");
      else
        new SilentPrint(str, (String)???);
    }
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
 * Qualified Name:     org.jpedal.examples.printing.SilentPrint
 * JD-Core Version:    0.6.2
 */