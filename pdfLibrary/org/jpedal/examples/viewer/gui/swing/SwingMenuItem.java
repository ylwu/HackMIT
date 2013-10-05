package org.jpedal.examples.viewer.gui.swing;

import javax.swing.JMenuItem;

public class SwingMenuItem extends JMenuItem
  implements SwingID
{
  private int ID;

  public SwingMenuItem(String paramString)
  {
    super(paramString);
  }

  public int getID()
  {
    return this.ID;
  }

  public void setID(int paramInt)
  {
    this.ID = paramInt;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.gui.swing.SwingMenuItem
 * JD-Core Version:    0.6.2
 */