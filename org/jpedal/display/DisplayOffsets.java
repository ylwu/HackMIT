package org.jpedal.display;

import java.awt.Point;
import org.jpedal.PdfDecoder;
import org.jpedal.examples.viewer.gui.SwingGUI;
import org.jpedal.external.ExternalHandlers;

public class DisplayOffsets
{
  private int userOffsetX = 0;
  private int userOffsetY = 0;
  private int userPrintOffsetX = 0;
  private int userPrintOffsetY = 0;
  private int facingCursorX = 10000;
  private int facingCursorY = 10000;
  PdfDecoder pdf;
  ExternalHandlers externalHandlers;

  public DisplayOffsets(PdfDecoder paramPdfDecoder, ExternalHandlers paramExternalHandlers)
  {
    this.pdf = paramPdfDecoder;
    this.externalHandlers = paramExternalHandlers;
  }

  public void setUserOffsets(int paramInt1, int paramInt2, int paramInt3)
  {
    switch (paramInt3)
    {
    case 0:
      this.userOffsetX = paramInt1;
      this.userOffsetY = paramInt2;
      break;
    case 1:
      this.userPrintOffsetX = paramInt1;
      this.userPrintOffsetY = (-paramInt2);
      break;
    case 995:
      this.facingCursorX = 0;
      this.facingCursorY = this.pdf.getHeight();
      SwingGUI localSwingGUI1 = (SwingGUI)this.externalHandlers.getExternalHandler(11);
      if (localSwingGUI1 != null)
        localSwingGUI1.setDragCorner(paramInt3);
      this.pdf.repaint();
      break;
    case 998:
      this.facingCursorX = paramInt1;
      this.facingCursorY = paramInt2;
      SwingGUI localSwingGUI2 = (SwingGUI)this.externalHandlers.getExternalHandler(11);
      if (localSwingGUI2 != null)
        localSwingGUI2.setDragCorner(paramInt3);
      this.pdf.repaint();
      break;
    case 999:
      this.facingCursorX = paramInt1;
      this.facingCursorY = paramInt2;
      SwingGUI localSwingGUI3 = (SwingGUI)this.externalHandlers.getExternalHandler(11);
      if (localSwingGUI3 != null)
        localSwingGUI3.setDragCorner(paramInt3);
      this.pdf.repaint();
      break;
    case 996:
      this.facingCursorX = paramInt1;
      this.facingCursorY = paramInt2;
      SwingGUI localSwingGUI4 = (SwingGUI)this.externalHandlers.getExternalHandler(11);
      if (localSwingGUI4 != null)
        localSwingGUI4.setDragCorner(paramInt3);
      this.pdf.repaint();
      break;
    case 997:
      this.facingCursorX = paramInt1;
      this.facingCursorY = paramInt2;
      SwingGUI localSwingGUI5 = (SwingGUI)this.externalHandlers.getExternalHandler(11);
      if (localSwingGUI5 != null)
        localSwingGUI5.setDragCorner(paramInt3);
      this.pdf.repaint();
      break;
    default:
      throw new RuntimeException("No such mode - look in org.jpedal.external.OffsetOptions for valid values");
    }
  }

  public Point getUserOffsets(int paramInt)
  {
    switch (paramInt)
    {
    case 0:
      return new Point(this.userOffsetX, this.userOffsetY);
    case 1:
      return new Point(this.userPrintOffsetX, this.userPrintOffsetY);
    case 999:
      return new Point(this.facingCursorX, this.facingCursorY);
    }
    throw new RuntimeException("No such mode - look in org.jpedal.external.OffsetOptions for valid values");
  }

  public int getUserPrintOffsetX()
  {
    return this.userPrintOffsetX;
  }

  public int getUserPrintOffsetY()
  {
    return this.userPrintOffsetY;
  }

  public int getUserOffsetX()
  {
    return this.userOffsetX;
  }

  public int getUserOffsetY()
  {
    return this.userOffsetY;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.display.DisplayOffsets
 * JD-Core Version:    0.6.2
 */