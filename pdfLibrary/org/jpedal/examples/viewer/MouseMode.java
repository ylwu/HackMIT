package org.jpedal.examples.viewer;

public class MouseMode
{
  public static final int MOUSE_MODE_TEXT_SELECT = 0;
  public static final int MOUSE_MODE_PANNING = 1;
  int mouseMode = 0;

  public int getMouseMode()
  {
    return this.mouseMode;
  }

  public void setMouseMode(int paramInt)
  {
    this.mouseMode = paramInt;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.MouseMode
 * JD-Core Version:    0.6.2
 */