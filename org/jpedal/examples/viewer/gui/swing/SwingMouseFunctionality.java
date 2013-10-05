package org.jpedal.examples.viewer.gui.swing;

import java.awt.event.MouseEvent;

public abstract interface SwingMouseFunctionality
{
  public abstract void mouseClicked(MouseEvent paramMouseEvent);

  public abstract void mouseEntered(MouseEvent paramMouseEvent);

  public abstract void mouseExited(MouseEvent paramMouseEvent);

  public abstract void mousePressed(MouseEvent paramMouseEvent);

  public abstract void mouseReleased(MouseEvent paramMouseEvent);

  public abstract void mouseDragged(MouseEvent paramMouseEvent);

  public abstract void mouseMoved(MouseEvent paramMouseEvent);
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.gui.swing.SwingMouseFunctionality
 * JD-Core Version:    0.6.2
 */