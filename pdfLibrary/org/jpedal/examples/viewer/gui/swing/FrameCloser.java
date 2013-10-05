package org.jpedal.examples.viewer.gui.swing;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import org.jpedal.PdfDecoder;
import org.jpedal.examples.viewer.Commands;
import org.jpedal.examples.viewer.Values;
import org.jpedal.examples.viewer.gui.generic.GUIThumbnailPanel;
import org.jpedal.examples.viewer.utils.Printer;
import org.jpedal.examples.viewer.utils.PropertiesFile;
import org.jpedal.gui.GUIFactory;
import org.jpedal.utils.Messages;

public class FrameCloser extends WindowAdapter
{
  private Commands currentCommands;
  GUIFactory currentGUI;
  PdfDecoder decode_pdf;
  GUIThumbnailPanel thumbnails;
  Values commonValues;
  PropertiesFile properties;

  public FrameCloser(Commands paramCommands, GUIFactory paramGUIFactory, PdfDecoder paramPdfDecoder, Printer paramPrinter, GUIThumbnailPanel paramGUIThumbnailPanel, Values paramValues, PropertiesFile paramPropertiesFile)
  {
    this.currentCommands = paramCommands;
    this.currentGUI = paramGUIFactory;
    this.decode_pdf = paramPdfDecoder;
    this.thumbnails = paramGUIThumbnailPanel;
    this.commonValues = paramValues;
    this.properties = paramPropertiesFile;
  }

  public void windowClosing(WindowEvent paramWindowEvent)
  {
    try
    {
      this.properties.setValue("lastDocumentPage", String.valueOf(this.commonValues.getCurrentPage()));
    }
    catch (Exception localException)
    {
    }
    if (Printer.isPrinting())
      this.currentGUI.showMessageDialog(Messages.getMessage("PdfViewerBusyPrinting.message"));
    if (!Values.isProcessing())
    {
      this.thumbnails.terminateDrawing();
      this.decode_pdf.closePdfFile();
      this.currentCommands.executeCommand(7, null);
    }
    else
    {
      this.currentGUI.showMessageDialog(Messages.getMessage("PdfViewerDecodeWait.message"));
    }
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.gui.swing.FrameCloser
 * JD-Core Version:    0.6.2
 */