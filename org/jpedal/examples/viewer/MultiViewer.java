package org.jpedal.examples.viewer;

import javax.swing.UIManager;
import org.jpedal.examples.viewer.gui.SwingGUI;
import org.jpedal.gui.GUIFactory;
import org.jpedal.utils.LogWriter;

public class MultiViewer extends Viewer
{
  public MultiViewer()
  {
    this.currentGUI.setDisplayMode(GUIFactory.MULTIPAGE);
    org.jpedal.parser.DecoderOptions.showErrorMessages = true;
  }

  public MultiViewer(int paramInt)
  {
    this.currentGUI.setDisplayMode(GUIFactory.MULTIPAGE);
    org.jpedal.parser.DecoderOptions.showErrorMessages = true;
    this.commonValues.setModeOfOperation(paramInt);
  }

  public static void main(String[] paramArrayOfString)
  {
    try
    {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
    catch (Exception localException)
    {
      LogWriter.writeLog("Exception " + localException + " setting look and feel");
    }
    MultiViewer localMultiViewer = new MultiViewer();
    localMultiViewer.setupViewer();
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.MultiViewer
 * JD-Core Version:    0.6.2
 */