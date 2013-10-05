package org.jpedal.examples.easyintegration;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import org.jpedal.examples.viewer.Viewer;

public class PDFViewer
{
  public static void main(String[] paramArrayOfString)
  {
    JFrame localJFrame = new JFrame();
    localJFrame.getContentPane().setLayout(new BorderLayout());
    JInternalFrame localJInternalFrame = new JInternalFrame("INTERNAL FRAME 1");
    JLabel localJLabel = new JLabel("This is a very simple program.");
    localJLabel.setFont(new Font("Lucida", 1, 20));
    localJLabel.setForeground(Color.RED);
    localJFrame.add(localJLabel, "North");
    Viewer localViewer = new Viewer(localJInternalFrame, null);
    localViewer.setupViewer();
    localJFrame.add(localJInternalFrame, "Center");
    localJInternalFrame.setVisible(true);
    localJFrame.setTitle("Viewer in External Viewer");
    localJFrame.setSize(800, 600);
    localJFrame.addWindowListener(new WindowListener()
    {
      public void windowActivated(WindowEvent paramAnonymousWindowEvent)
      {
      }

      public void windowClosed(WindowEvent paramAnonymousWindowEvent)
      {
      }

      public void windowClosing(WindowEvent paramAnonymousWindowEvent)
      {
        System.exit(1);
      }

      public void windowDeactivated(WindowEvent paramAnonymousWindowEvent)
      {
      }

      public void windowDeiconified(WindowEvent paramAnonymousWindowEvent)
      {
      }

      public void windowIconified(WindowEvent paramAnonymousWindowEvent)
      {
      }

      public void windowOpened(WindowEvent paramAnonymousWindowEvent)
      {
      }
    });
    localJFrame.setVisible(true);
    Object[] arrayOfObject = { "/PDFData/Hand_Test/crbtrader.pdf" };
    localViewer.executeCommand(10, arrayOfObject);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.easyintegration.PDFViewer
 * JD-Core Version:    0.6.2
 */