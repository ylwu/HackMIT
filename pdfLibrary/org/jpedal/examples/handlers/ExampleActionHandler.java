package org.jpedal.examples.handlers;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.jpedal.examples.viewer.Commands;
import org.jpedal.examples.viewer.Viewer;
import org.jpedal.examples.viewer.gui.SwingGUI;
import org.jpedal.external.JPedalActionHandler;

public class ExampleActionHandler extends JFrame
{
  public static void main(String[] paramArrayOfString)
  {
    new ExampleActionHandler();
  }

  public ExampleActionHandler()
  {
    Viewer localViewer = new Viewer();
    localViewer.setRootContainer(getContentPane());
    localViewer.setupViewer();
    JPedalActionHandler local1 = new JPedalActionHandler()
    {
      public void actionPerformed(SwingGUI paramAnonymousSwingGUI, Commands paramAnonymousCommands)
      {
        JOptionPane.showMessageDialog(paramAnonymousSwingGUI.getFrame(), "Custom help dialog", "JPedal Help", 1);
      }
    };
    HashMap localHashMap = new HashMap();
    localHashMap.put(Integer.valueOf(998), local1);
    localViewer.addExternalHandler(localHashMap, 9);
    displayViewer();
  }

  private void displayViewer()
  {
    Dimension localDimension = Toolkit.getDefaultToolkit().getScreenSize();
    int i = localDimension.width / 2;
    int j = localDimension.height / 2;
    if (i < 700)
      i = 700;
    setSize(i, j);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(2);
    setVisible(true);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.handlers.ExampleActionHandler
 * JD-Core Version:    0.6.2
 */