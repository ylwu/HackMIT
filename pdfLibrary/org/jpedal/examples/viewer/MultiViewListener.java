package org.jpedal.examples.viewer;

import java.io.File;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import org.jpedal.PdfDecoder;
import org.jpedal.examples.viewer.gui.SwingGUI;

public class MultiViewListener
  implements InternalFrameListener
{
  Object pageScaling = null;
  Object pageRotation = null;
  private PdfDecoder decode_pdf;
  private SwingGUI currentGUI;
  private Values commonValues;
  private Commands currentCommands;

  public MultiViewListener(PdfDecoder paramPdfDecoder, SwingGUI paramSwingGUI, Values paramValues, Commands paramCommands)
  {
    this.decode_pdf = paramPdfDecoder;
    this.currentGUI = paramSwingGUI;
    this.commonValues = paramValues;
    this.currentCommands = paramCommands;
  }

  public void internalFrameOpened(InternalFrameEvent paramInternalFrameEvent)
  {
  }

  public void internalFrameClosing(InternalFrameEvent paramInternalFrameEvent)
  {
    this.currentGUI.setBackNavigationButtonsEnabled(false);
    this.currentGUI.setForwardNavigationButtonsEnabled(false);
    this.currentGUI.resetPageNav();
  }

  public void internalFrameClosed(InternalFrameEvent paramInternalFrameEvent)
  {
    this.decode_pdf.flushObjectValues(true);
    this.decode_pdf.closePdfFile();
  }

  public void internalFrameIconified(InternalFrameEvent paramInternalFrameEvent)
  {
  }

  public void internalFrameDeiconified(InternalFrameEvent paramInternalFrameEvent)
  {
  }

  public void internalFrameActivated(InternalFrameEvent paramInternalFrameEvent)
  {
    this.currentGUI.setPdfDecoder(this.decode_pdf);
    this.currentCommands.setPdfDecoder(this.decode_pdf);
    int i = this.decode_pdf.getlastPageDecoded();
    this.commonValues.setPageCount(this.decode_pdf.getPageCount());
    this.commonValues.setCurrentPage(i);
    String str = this.decode_pdf.getFileName();
    if (str != null)
    {
      this.commonValues.setSelectedFile(str);
      File localFile = new File(str);
      this.commonValues.setInputDir(localFile.getParent());
      this.commonValues.setFileSize(localFile.length() >> 10);
    }
    this.commonValues.setPDF(this.currentCommands.isPDF());
    this.commonValues.setMultiTiff(this.currentGUI.isMultiPageTiff());
    if (this.pageScaling != null)
      this.currentGUI.setSelectedComboItem(252, this.pageScaling.toString());
    if (this.pageRotation != null)
      this.currentGUI.setSelectedComboItem(251, this.pageRotation.toString());
    this.currentGUI.setPageNumber();
    this.decode_pdf.updateUI();
    this.currentGUI.removeSearchWindow(false);
    this.currentGUI.hideRedundentNavButtons();
  }

  public void internalFrameDeactivated(InternalFrameEvent paramInternalFrameEvent)
  {
    if (this.pageScaling != null)
      this.pageScaling = this.currentGUI.getSelectedComboItem(252);
    if (this.pageRotation != null)
      this.pageRotation = this.currentGUI.getSelectedComboItem(251);
  }

  public void setPageProperties(Object paramObject1, Object paramObject2)
  {
    this.pageRotation = paramObject1;
    this.pageScaling = paramObject2;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.MultiViewListener
 * JD-Core Version:    0.6.2
 */