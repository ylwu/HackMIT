package org.jpedal.examples.viewer.gui.generic;

import java.net.URL;
import javax.swing.ImageIcon;

public abstract interface GUIButton
{
  public abstract void init(URL paramURL, int paramInt, String paramString);

  public abstract void setVisible(boolean paramBoolean);

  public abstract void setEnabled(boolean paramBoolean);

  public abstract void setIcon(ImageIcon paramImageIcon);

  public abstract int getID();

  public abstract void setName(String paramString);
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.gui.generic.GUIButton
 * JD-Core Version:    0.6.2
 */