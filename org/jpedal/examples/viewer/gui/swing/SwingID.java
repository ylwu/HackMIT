package org.jpedal.examples.viewer.gui.swing;

import java.awt.event.ActionListener;

public abstract interface SwingID
{
  public abstract int getID();

  public abstract void setID(int paramInt);

  public abstract void setToolTipText(String paramString);

  public abstract void addActionListener(ActionListener paramActionListener);
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.gui.swing.SwingID
 * JD-Core Version:    0.6.2
 */