package org.jpedal.external;

import java.awt.Graphics2D;
import org.jpedal.objects.acroforms.GUIData;
import org.jpedal.objects.raw.FormObject;

public abstract interface CustomFormPrint
{
  public abstract boolean print(Graphics2D paramGraphics2D, FormObject paramFormObject, GUIData paramGUIData);
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.external.CustomFormPrint
 * JD-Core Version:    0.6.2
 */