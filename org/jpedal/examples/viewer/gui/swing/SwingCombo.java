package org.jpedal.examples.viewer.gui.swing;

import javax.swing.JComboBox;
import org.jpedal.examples.viewer.gui.generic.GUICombo;

public class SwingCombo extends JComboBox
  implements GUICombo
{
  private int ID;

  public SwingCombo(String[] paramArrayOfString)
  {
    super(paramArrayOfString);
    setLightWeightPopupEnabled(false);
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
 * Qualified Name:     org.jpedal.examples.viewer.gui.swing.SwingCombo
 * JD-Core Version:    0.6.2
 */