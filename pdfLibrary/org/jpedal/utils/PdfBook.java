package org.jpedal.utils;

import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import javax.print.PrintService;
import javax.print.attribute.Attribute;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.MediaSizeName;
import org.jpedal.PdfDecoder;
import org.jpedal.objects.PdfPageData;

public class PdfBook extends Book
{
  private MediaSizeName pageSize;
  private boolean chooseSourceByPdfPageSize;
  private boolean useExactPdfPageSize;
  private PdfDecoder pdfDecoder;
  private PrintService printingDevice;

  public PdfBook(PdfDecoder paramPdfDecoder, PrintService paramPrintService, PrintRequestAttributeSet paramPrintRequestAttributeSet)
  {
    paramPdfDecoder.swingPrinter.legacyPrintMode = false;
    this.pdfDecoder = paramPdfDecoder;
    this.printingDevice = paramPrintService;
    PdfPageFormat.findPrinterPapers(paramPrintService);
    int i = 0;
    if (paramPrintRequestAttributeSet != null)
    {
      Attribute[] arrayOfAttribute = paramPrintRequestAttributeSet.toArray();
      for (int k = 0; k < arrayOfAttribute.length; k++)
        if ((arrayOfAttribute[k] instanceof MediaSizeName))
        {
          this.pageSize = ((MediaSizeName)arrayOfAttribute[k]);
          i = 1;
          k = arrayOfAttribute.length;
        }
    }
    if (i == 0)
      this.pageSize = PdfPageFormat.getDefaultMediaSizeName(paramPrintService);
    this.chooseSourceByPdfPageSize = false;
    this.useExactPdfPageSize = false;
    paramPdfDecoder.setUsePDFPaperSize(this.chooseSourceByPdfPageSize);
    for (int j = 0; j < paramPdfDecoder.getPageCount(); j++)
      if (this.pdfDecoder.getUserSetPageFormat(j) == null)
        append(paramPdfDecoder, PdfPageFormat.createPdfPageFormat(this.pageSize, paramPrintService));
      else
        append(paramPdfDecoder, this.pdfDecoder.getUserSetPageFormat(j));
  }

  public void setChooseSourceByPdfPageSize(boolean paramBoolean)
  {
    this.pdfDecoder.setUsePDFPaperSize(paramBoolean);
    this.chooseSourceByPdfPageSize = paramBoolean;
    updatePages();
  }

  public void setUseExactPdfPageSize(boolean paramBoolean)
  {
    this.pdfDecoder.setUsePDFPaperSize(paramBoolean);
    this.useExactPdfPageSize = paramBoolean;
    updatePages();
  }

  public void setPageSize(MediaSizeName paramMediaSizeName)
  {
    this.pageSize = paramMediaSizeName;
    updatePages();
  }

  private void updatePages()
  {
    for (int i = 0; i < this.pdfDecoder.getPageCount(); i++)
    {
      PageFormat localPageFormat;
      if (this.chooseSourceByPdfPageSize)
      {
        localPageFormat = PdfPageFormat.getPageFormat(i + 1, this.pdfDecoder);
      }
      else if (this.useExactPdfPageSize)
      {
        Paper localPaper = new Paper();
        localPageFormat = new PageFormat();
        int j = this.pdfDecoder.getPdfPageData().getCropBoxWidth(i);
        int k = this.pdfDecoder.getPdfPageData().getCropBoxHeight(i);
        localPaper.setSize(j, k);
        localPaper.setImageableArea(0.0D, 0.0D, j, k);
        localPageFormat.setPaper(localPaper);
      }
      else if (this.pdfDecoder.getUserSetPageFormat(i) == null)
      {
        localPageFormat = PdfPageFormat.createPdfPageFormat(this.pageSize, this.printingDevice);
      }
      else
      {
        localPageFormat = this.pdfDecoder.getUserSetPageFormat(i);
      }
      setPage(i, this.pdfDecoder, localPageFormat);
    }
  }

  public boolean getChooseSourceByPdfPageSize()
  {
    return this.chooseSourceByPdfPageSize;
  }

  public boolean getUseExactPdfPageSize()
  {
    return this.useExactPdfPageSize;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.utils.PdfBook
 * JD-Core Version:    0.6.2
 */