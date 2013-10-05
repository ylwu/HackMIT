package org.jpedal.examples.viewer.gui.popups;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FocusTraversalPolicy;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import org.jpedal.utils.BrowserLauncher;

public class UpdateDialog extends JDialog
{
  private String availableVersion;
  private String currentVersion;

  public UpdateDialog(Container paramContainer, String paramString1, String paramString2)
  {
    super((JFrame)null, "Update Info", true);
    setDefaultCloseOperation(2);
    this.currentVersion = paramString1;
    this.availableVersion = paramString2;
    setSize(550, 350);
    init();
    setLocationRelativeTo(paramContainer);
  }

  private void init()
  {
    getContentPane().setLayout(new GridBagLayout());
    GridBagConstraints localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.fill = 1;
    localGridBagConstraints.weighty = 1.0D;
    localGridBagConstraints.weightx = 1.0D;
    localGridBagConstraints.insets = new Insets(10, 10, 10, 10);
    addCenterPanel(localGridBagConstraints);
    localGridBagConstraints.weighty = 0.0D;
    localGridBagConstraints.gridy = 1;
    addBottomButtons(localGridBagConstraints);
  }

  private void addCenterPanel(GridBagConstraints paramGridBagConstraints)
  {
    JPanel localJPanel = new JPanel(new GridBagLayout());
    localJPanel.setBorder(BorderFactory.createEtchedBorder());
    add(localJPanel, paramGridBagConstraints);
    GridBagConstraints localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.gridx = 0;
    localGridBagConstraints.gridy = 0;
    localGridBagConstraints.fill = 2;
    localGridBagConstraints.anchor = 19;
    localGridBagConstraints.weighty = 0.0D;
    localGridBagConstraints.weightx = 0.0D;
    localGridBagConstraints.insets = new Insets(10, 10, 10, 10);
    localGridBagConstraints.gridwidth = 2;
    SimpleAttributeSet localSimpleAttributeSet1 = new SimpleAttributeSet();
    StyleConstants.setForeground(localSimpleAttributeSet1, Color.black);
    StyleConstants.setBold(localSimpleAttributeSet1, false);
    SimpleAttributeSet localSimpleAttributeSet2 = new SimpleAttributeSet();
    StyleConstants.setForeground(localSimpleAttributeSet2, Color.black);
    StyleConstants.setBold(localSimpleAttributeSet2, true);
    JTextPane localJTextPane1 = new JTextPane();
    localJTextPane1.setEditable(false);
    localJTextPane1.setOpaque(false);
    Document localDocument = localJTextPane1.getDocument();
    try
    {
      localDocument.insertString(0, "A new version of JPedal is available.", localSimpleAttributeSet2);
    }
    catch (BadLocationException localBadLocationException1)
    {
      localBadLocationException1.printStackTrace();
    }
    localJPanel.add(localJTextPane1, localGridBagConstraints);
    localGridBagConstraints.gridwidth = 1;
    localGridBagConstraints.gridy = 1;
    localGridBagConstraints.insets = new Insets(10, 10, 0, 10);
    JTextPane localJTextPane2 = new JTextPane();
    localJTextPane2.setEditable(false);
    localJTextPane2.setOpaque(false);
    localDocument = localJTextPane2.getDocument();
    try
    {
      localDocument.insertString(0, "Your current version:", localSimpleAttributeSet1);
    }
    catch (BadLocationException localBadLocationException2)
    {
      localBadLocationException2.printStackTrace();
    }
    localJTextPane2.setMinimumSize(localJTextPane2.getPreferredSize());
    localJPanel.add(localJTextPane2, localGridBagConstraints);
    localGridBagConstraints.weightx = 1.0D;
    localGridBagConstraints.gridx = 1;
    JTextPane localJTextPane3 = new JTextPane();
    localJTextPane3.setEditable(false);
    localJTextPane3.setOpaque(false);
    localDocument = localJTextPane3.getDocument();
    try
    {
      localDocument.insertString(0, this.currentVersion, localSimpleAttributeSet1);
    }
    catch (BadLocationException localBadLocationException3)
    {
      localBadLocationException3.printStackTrace();
    }
    localJPanel.add(localJTextPane3, localGridBagConstraints);
    localGridBagConstraints.insets = new Insets(0, 10, 10, 10);
    localGridBagConstraints.weightx = 0.0D;
    localGridBagConstraints.gridx = 0;
    localGridBagConstraints.gridy = 2;
    JTextPane localJTextPane4 = new JTextPane();
    localJTextPane4.setEditable(false);
    localJTextPane4.setOpaque(false);
    localDocument = localJTextPane4.getDocument();
    try
    {
      localDocument.insertString(0, "Available Version:", localSimpleAttributeSet1);
    }
    catch (BadLocationException localBadLocationException4)
    {
      localBadLocationException4.printStackTrace();
    }
    localJPanel.add(localJTextPane4, localGridBagConstraints);
    localGridBagConstraints.gridwidth = 2;
    localGridBagConstraints.weightx = 1.0D;
    localGridBagConstraints.gridx = 1;
    JTextPane localJTextPane5 = new JTextPane();
    localJTextPane5.setEditable(false);
    localJTextPane5.setOpaque(false);
    localDocument = localJTextPane5.getDocument();
    try
    {
      localDocument.insertString(0, this.availableVersion, localSimpleAttributeSet1);
    }
    catch (BadLocationException localBadLocationException5)
    {
      localBadLocationException5.printStackTrace();
    }
    localJPanel.add(localJTextPane5, localGridBagConstraints);
    localGridBagConstraints.insets = new Insets(10, 10, 10, 10);
    localGridBagConstraints.weightx = 0.0D;
    localGridBagConstraints.gridx = 0;
    localGridBagConstraints.gridy = 3;
    JTextPane localJTextPane6 = new JTextPane();
    localJTextPane6.setEditable(false);
    localJTextPane6.setOpaque(false);
    localDocument = localJTextPane6.getDocument();
    try
    {
      localDocument.insertString(0, "Press ", localSimpleAttributeSet1);
      localDocument.insertString(localDocument.getLength(), "More info... ", localSimpleAttributeSet2);
      localDocument.insertString(localDocument.getLength(), "to open a web page where you can download JPedal or learn more about the new version", localSimpleAttributeSet1);
    }
    catch (BadLocationException localBadLocationException6)
    {
      localBadLocationException6.printStackTrace();
    }
    localJPanel.add(localJTextPane6, localGridBagConstraints);
    localGridBagConstraints.gridy = 4;
    JTextPane localJTextPane7 = new JTextPane();
    localDocument = localJTextPane7.getDocument();
    try
    {
      localDocument.insertString(0, "To configure automatic updates settings, see ", localSimpleAttributeSet1);
      localDocument.insertString(localDocument.getLength(), "View | Preferences", localSimpleAttributeSet2);
    }
    catch (BadLocationException localBadLocationException7)
    {
      localBadLocationException7.printStackTrace();
    }
    localJTextPane7.setEditable(false);
    localJTextPane7.setOpaque(false);
    localJPanel.add(localJTextPane7, localGridBagConstraints);
    localGridBagConstraints.weighty = 1.0D;
    localGridBagConstraints.gridy = 5;
    JTextPane localJTextPane8 = new JTextPane();
    localDocument = localJTextPane8.getDocument();
    try
    {
      localDocument.insertString(0, "To check for new updates manually, use ", localSimpleAttributeSet1);
      localDocument.insertString(localDocument.getLength(), "Help | Check for Updates", localSimpleAttributeSet2);
    }
    catch (BadLocationException localBadLocationException8)
    {
      localBadLocationException8.printStackTrace();
    }
    localJTextPane8.setEditable(false);
    localJTextPane8.setOpaque(false);
    localJPanel.add(localJTextPane8, localGridBagConstraints);
  }

  private void addBottomButtons(GridBagConstraints paramGridBagConstraints)
  {
    JPanel localJPanel = new JPanel();
    localJPanel.setLayout(new BoxLayout(localJPanel, 2));
    localJPanel.add(Box.createHorizontalGlue());
    JButton localJButton1 = new JButton("More Info");
    localJButton1.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        try
        {
          BrowserLauncher.openURL("http://www.idrsolutions.com/jpedal-builds/");
        }
        catch (IOException localIOException)
        {
        }
      }
    });
    JButton localJButton2 = new JButton("Close");
    localJButton2.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        UpdateDialog.this.dispose();
        UpdateDialog.this.setVisible(false);
      }
    });
    localJButton2.setPreferredSize(localJButton1.getPreferredSize());
    localJPanel.add(localJButton2);
    localJPanel.add(Box.createRigidArea(new Dimension(5, 0)));
    setFocusTraversalPolicy(new MyFocus(getFocusTraversalPolicy(), localJButton1));
    localJButton1.addKeyListener(new KeyListener()
    {
      public void keyTyped(KeyEvent paramAnonymousKeyEvent)
      {
      }

      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
        if (paramAnonymousKeyEvent.getKeyCode() == 10)
          try
          {
            BrowserLauncher.openURL("http://www.idrsolutions.com/jpedal-builds/");
          }
          catch (IOException localIOException)
          {
          }
      }

      public void keyReleased(KeyEvent paramAnonymousKeyEvent)
      {
      }
    });
    localJPanel.add(localJButton1);
    add(localJPanel, paramGridBagConstraints);
  }

  public static void main(String[] paramArrayOfString)
  {
    UpdateDialog localUpdateDialog = new UpdateDialog(null, "3.2", "3.3");
    localUpdateDialog.setVisible(true);
  }

  static class MyFocus extends FocusTraversalPolicy
  {
    FocusTraversalPolicy original;
    JButton close;

    MyFocus(FocusTraversalPolicy paramFocusTraversalPolicy, JButton paramJButton)
    {
      this.original = paramFocusTraversalPolicy;
      this.close = paramJButton;
    }

    public Component getComponentAfter(Container paramContainer, Component paramComponent)
    {
      return this.original.getComponentAfter(paramContainer, paramComponent);
    }

    public Component getComponentBefore(Container paramContainer, Component paramComponent)
    {
      return this.original.getComponentBefore(paramContainer, paramComponent);
    }

    public Component getFirstComponent(Container paramContainer)
    {
      return this.original.getFirstComponent(paramContainer);
    }

    public Component getLastComponent(Container paramContainer)
    {
      return this.original.getLastComponent(paramContainer);
    }

    public Component getDefaultComponent(Container paramContainer)
    {
      return this.close;
    }
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.gui.popups.UpdateDialog
 * JD-Core Version:    0.6.2
 */