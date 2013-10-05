package org.jpedal.examples.viewer.gui.generic;

import java.awt.Color;
import java.awt.Dimension;

public abstract interface GUICombo
{
  public abstract void setSelectedIndex(int paramInt);

  public abstract void setBackground(Color paramColor);

  public abstract void setEditable(boolean paramBoolean);

  public abstract void setPreferredSize(Dimension paramDimension);

  public abstract void setID(int paramInt);

  public abstract void setToolTipText(String paramString);

  public abstract void setEnabled(boolean paramBoolean);

  public abstract int getSelectedIndex();

  public abstract void setSelectedItem(Object paramObject);

  public abstract Object getSelectedItem();

  public abstract void setVisible(boolean paramBoolean);
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.gui.generic.GUICombo
 * JD-Core Version:    0.6.2
 */