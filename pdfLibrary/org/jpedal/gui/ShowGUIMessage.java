package org.jpedal.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.util.StringTokenizer;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JViewport;
import org.jpedal.utils.LogWriter;

public class ShowGUIMessage
{
  private static Container contentPane = null;
  private static boolean outputMessages = false;

  public static final void showGUIMessage(String paramString1, JTextPane paramJTextPane, String paramString2)
  {
    paramJTextPane.setEditable(false);
    JPanel localJPanel = new JPanel();
    JScrollPane localJScrollPane = new JScrollPane();
    localJScrollPane.setHorizontalScrollBarPolicy(31);
    localJScrollPane.getViewport().add(paramJTextPane);
    localJPanel.setLayout(new BorderLayout());
    localJPanel.add(localJScrollPane, "Center");
    if (paramString1 != null)
      localJPanel.add(new JLabel("<HTML><BODY><I>" + paramString1 + "</I></BODY></HTML>", 0), "South");
    paramJTextPane.setEditable(false);
    localJPanel.setPreferredSize(new Dimension(300, 200));
    JOptionPane.showConfirmDialog(contentPane, localJPanel, paramString2, -1, -1);
  }

  public static final void showGUIMessage(String paramString1, JLabel paramJLabel, String paramString2)
  {
    JPanel localJPanel = new JPanel();
    localJPanel.setLayout(new BorderLayout());
    localJPanel.add(paramJLabel, "Center");
    if (paramString1 != null)
      localJPanel.add(new JLabel(paramString1, 0), "South");
    JOptionPane.showConfirmDialog(contentPane, localJPanel, paramString2, -1, -1);
    contentPane.setVisible(true);
  }

  public static final void setParentFrame(JFrame paramJFrame)
  {
    contentPane = paramJFrame;
  }

  public static final void showstaticGUIMessage(StringBuffer paramStringBuffer, String paramString)
  {
    JTextArea localJTextArea = new JTextArea();
    localJTextArea.setEditable(false);
    localJTextArea.setWrapStyleWord(true);
    localJTextArea.append("  " + paramStringBuffer + "  ");
    JPanel localJPanel = new JPanel();
    localJPanel.setLayout(new BorderLayout());
    localJPanel.add(localJTextArea, "Center");
    int i = (int)localJTextArea.getSize().getWidth();
    int j = (int)localJTextArea.getSize().getHeight();
    localJPanel.setSize(new Dimension(i + 10, j + 10));
    JOptionPane.showConfirmDialog(contentPane, localJPanel, paramString, -1, -1);
  }

  public static final void setClientDisplay()
  {
    outputMessages = true;
  }

  public static final void showGUIMessage(String paramString1, ImageIcon paramImageIcon, String paramString2)
  {
    JPanel localJPanel = new JPanel();
    localJPanel.setLayout(new BorderLayout());
    JLabel localJLabel = new JLabel(paramImageIcon);
    localJPanel.add(localJLabel, "Center");
    if (paramString1 != null)
      localJPanel.add(new JLabel(paramString1), "South");
    int i = (int)localJLabel.getSize().getWidth();
    int j = (int)localJLabel.getSize().getHeight();
    localJPanel.setSize(new Dimension(i + 10, j + 10));
    int k = -1;
    int m = -1;
    JOptionPane.showConfirmDialog(contentPane, localJPanel, paramString2, k, m);
  }

  public static final void showGUIMessage(String paramString1, BufferedImage paramBufferedImage, String paramString2)
  {
    if (paramBufferedImage == null)
      return;
    ImagePanel localImagePanel = new ImagePanel(paramBufferedImage);
    localImagePanel.setLayout(new BorderLayout());
    if (paramString1 != null)
      localImagePanel.add(new JLabel(paramString1), "South");
    int i = paramBufferedImage.getWidth();
    int j = paramBufferedImage.getHeight();
    localImagePanel.setSize(new Dimension(i + 10, j + 10));
    JOptionPane.showConfirmDialog(contentPane, localImagePanel, paramString2, -1, -1);
  }

  public static final void showGUIMessage(String paramString1, String paramString2, String paramString3)
  {
    BufferedImage localBufferedImage = null;
    try
    {
      FileInputStream localFileInputStream = new FileInputStream(paramString1);
      localBufferedImage = ImageIO.read(localFileInputStream);
    }
    catch (Exception localException)
    {
      LogWriter.writeLog("Exception " + localException + " getting image");
    }
    if (localBufferedImage != null)
    {
      JPanel localJPanel = new JPanel();
      JScrollPane localJScrollPane = new JScrollPane();
      localJScrollPane.setHorizontalScrollBarPolicy(31);
      localJPanel.setLayout(new BorderLayout());
      localJPanel.add(localJScrollPane, "Center");
      localJPanel.setPreferredSize(new Dimension(300, 200));
      JOptionPane.showConfirmDialog(contentPane, localJPanel, paramString2, -1, -1);
    }
  }

  public static final void showGUIMessage(String paramString1, String paramString2)
  {
    if (outputMessages)
    {
      String str = "<HTML><BODY><CENTER><FONT COLOR=black>";
      StringTokenizer localStringTokenizer = new StringTokenizer(paramString1, "\n");
      while (localStringTokenizer.hasMoreTokens())
        str = str + localStringTokenizer.nextToken() + "</FONT></CENTER><CENTER><FONT COLOR=black>";
      str = str + "</FONT></CENTER></BODY></HTML>";
      JLabel localJLabel = new JLabel(str);
      localJLabel.setBackground(Color.white);
      showGUIMessage(null, localJLabel, paramString2);
    }
  }

  public static final void showGUIMessage(StringBuffer paramStringBuffer, String paramString)
  {
    showGUIMessage(paramStringBuffer.toString(), paramString);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.gui.ShowGUIMessage
 * JD-Core Version:    0.6.2
 */