package org.jpedal.examples.viewer.gui.swing;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;
import org.jpedal.PdfDecoder;
import org.jpedal.examples.viewer.gui.SwingGUI;

public class SwingMousePanMode
  implements SwingMouseFunctionality
{
  private Point currentPoint;
  private PdfDecoder decode_pdf;
  private Rectangle currentView;

  public SwingMousePanMode(PdfDecoder paramPdfDecoder)
  {
    this.decode_pdf = paramPdfDecoder;
  }

  public void mouseClicked(MouseEvent paramMouseEvent)
  {
  }

  public void mouseEntered(MouseEvent paramMouseEvent)
  {
  }

  public void mouseExited(MouseEvent paramMouseEvent)
  {
  }

  public void mousePressed(MouseEvent paramMouseEvent)
  {
    if ((SwingUtilities.isLeftMouseButton(paramMouseEvent)) || (SwingUtilities.isMiddleMouseButton(paramMouseEvent)))
    {
      this.currentPoint = paramMouseEvent.getPoint();
      this.currentView = this.decode_pdf.getVisibleRect();
      SwingGUI localSwingGUI = (SwingGUI)this.decode_pdf.getExternalHandler(11);
      this.decode_pdf.setCursor(localSwingGUI.getCursor(2));
    }
  }

  public void mouseReleased(MouseEvent paramMouseEvent)
  {
    SwingGUI localSwingGUI = (SwingGUI)this.decode_pdf.getExternalHandler(11);
    this.decode_pdf.setCursor(localSwingGUI.getCursor(1));
  }

  public void mouseDragged(MouseEvent paramMouseEvent)
  {
    if ((SwingUtilities.isLeftMouseButton(paramMouseEvent)) || (SwingUtilities.isMiddleMouseButton(paramMouseEvent)))
    {
      Point localPoint = paramMouseEvent.getPoint();
      int i = this.currentPoint.x - localPoint.x;
      int j = this.currentPoint.y - localPoint.y;
      Rectangle localRectangle = this.currentView;
      localRectangle.x += i;
      localRectangle.y += j;
      if (!localRectangle.contains(this.decode_pdf.getVisibleRect()))
        this.decode_pdf.scrollRectToVisible(localRectangle);
    }
  }

  public void mouseMoved(MouseEvent paramMouseEvent)
  {
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.gui.swing.SwingMousePanMode
 * JD-Core Version:    0.6.2
 */