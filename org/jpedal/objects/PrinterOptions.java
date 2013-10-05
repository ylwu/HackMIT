package org.jpedal.objects;

import org.jpedal.utils.Messages;

public class PrinterOptions
{
  public static final String[] PRINT_SCALING_OPTIONS = { Messages.getMessage("PdfViewerPrint.NoScaling"), Messages.getMessage("PdfViewerPrint.FitToPrinterMargins"), Messages.getMessage("PdfViewerPrint.ReduceToPrinterMargins") };
  public static final int PAGE_SCALING_NONE = 0;
  public static final int PAGE_SCALING_FIT_TO_PRINTER_MARGINS = 1;
  public static final int PAGE_SCALING_REDUCE_TO_PRINTER_MARGINS = 2;
  public static int LAST_SCALING_CHOICE = 2;
  public static final int ALL_PAGES = 8;
  public static final int ODD_PAGES_ONLY = 16;
  public static final int EVEN_PAGES_ONLY = 32;
  public static final int PRINT_PAGES_REVERSED = 64;
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.PrinterOptions
 * JD-Core Version:    0.6.2
 */