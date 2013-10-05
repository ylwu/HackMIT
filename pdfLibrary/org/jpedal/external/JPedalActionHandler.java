package org.jpedal.external;

import org.jpedal.examples.viewer.Commands;
import org.jpedal.examples.viewer.gui.SwingGUI;

public abstract interface JPedalActionHandler
{
  public abstract void actionPerformed(SwingGUI paramSwingGUI, Commands paramCommands);
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.external.JPedalActionHandler
 * JD-Core Version:    0.6.2
 */