package org.jpedal.examples.viewer.gui.popups;

import java.awt.event.KeyListener;
import java.util.Map;
import javax.swing.event.ChangeListener;

public abstract interface WizardPanelModel
{
  public abstract boolean isFinishPanel();

  public abstract String getStartPanelID();

  public abstract String next();

  public abstract String previous();

  public abstract void close();

  public abstract boolean hasPrevious();

  public abstract boolean canAdvance();

  public abstract Map getJPanels();

  public abstract void registerNextChangeListeners(ChangeListener paramChangeListener);

  public abstract void registerNextKeyListeners(KeyListener paramKeyListener);
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.gui.popups.WizardPanelModel
 * JD-Core Version:    0.6.2
 */