package org.jpedal.examples.viewer.gui.swing;

import javax.swing.JCheckBoxMenuItem;

public class SwingCheckBoxMenuItem extends JCheckBoxMenuItem
  implements SwingID
{
  private int ID;

  public SwingCheckBoxMenuItem(String paramString)
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
 * Qualified Name:     org.jpedal.examples.viewer.gui.swing.SwingCheckBoxMenuItem
 * JD-Core Version:    0.6.2
 */