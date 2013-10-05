package org.jpedal.examples.viewer.gui.popups;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FocusTraversalPolicy;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkEvent.EventType;
import javax.swing.event.HyperlinkListener;
import org.jpedal.examples.viewer.utils.PropertiesFile;
import org.jpedal.utils.BrowserLauncher;

public class TipOfTheDay extends JDialog
{
  private List tipPaths = new ArrayList();
  private boolean tipLoadingFailed = false;
  private int currentTip;
  private JEditorPane tipPane = new JEditorPane();
  private PropertiesFile propertiesFile;
  private JCheckBox showTipsOnStartup = new JCheckBox("Show Tips on Startup");

  public TipOfTheDay(Container paramContainer, String paramString, PropertiesFile paramPropertiesFile)
  {
    super((JFrame)null, "Tip of the Day", true);
    this.propertiesFile = paramPropertiesFile;
    setDefaultCloseOperation(2);
    try
    {
      populateTipsList(paramString, this.tipPaths);
    }
    catch (IOException localIOException)
    {
      this.tipLoadingFailed = true;
    }
    Random localRandom = new Random();
    this.currentTip = localRandom.nextInt(this.tipPaths.size());
    setSize(550, 350);
    init();
    setLocationRelativeTo(paramContainer);
  }

  private void init()
  {
    getContentPane().setLayout(new GridBagLayout());
    GridBagConstraints localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.gridx = 0;
    localGridBagConstraints.gridy = 0;
    localGridBagConstraints.fill = 2;
    localGridBagConstraints.anchor = 19;
    localGridBagConstraints.weighty = 0.0D;
    localGridBagConstraints.weightx = 0.0D;
    localGridBagConstraints.insets = new Insets(10, 10, 0, 10);
    addTopPanel(localGridBagConstraints);
    localGridBagConstraints.fill = 1;
    localGridBagConstraints.gridy = 1;
    localGridBagConstraints.weighty = 1.0D;
    localGridBagConstraints.weightx = 1.0D;
    addCenterTip(localGridBagConstraints);
    localGridBagConstraints.fill = 2;
    localGridBagConstraints.gridy = 2;
    localGridBagConstraints.weighty = 0.0D;
    localGridBagConstraints.weightx = 0.0D;
    localGridBagConstraints.insets = new Insets(0, 7, 0, 10);
    addDisplayOnStartup(localGridBagConstraints);
    localGridBagConstraints.gridy = 3;
    localGridBagConstraints.insets = new Insets(0, 0, 10, 10);
    addBottomButtons(localGridBagConstraints);
  }

  private void addDisplayOnStartup(GridBagConstraints paramGridBagConstraints)
  {
    String str = this.propertiesFile.getValue("displaytipsonstartup");
    if (!str.isEmpty())
      this.showTipsOnStartup.setSelected(str.equals("true"));
    this.showTipsOnStartup.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        TipOfTheDay.this.propertiesFile.setValue("displaytipsonstartup", String.valueOf(TipOfTheDay.this.showTipsOnStartup.isSelected()));
      }
    });
    getContentPane().add(this.showTipsOnStartup, paramGridBagConstraints);
  }

  private void addBottomButtons(GridBagConstraints paramGridBagConstraints)
  {
    JPanel localJPanel = new JPanel();
    localJPanel.setLayout(new BoxLayout(localJPanel, 2));
    localJPanel.add(Box.createHorizontalGlue());
    JButton localJButton1 = new JButton("Previous Tip");
    localJButton1.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        TipOfTheDay.this.changeTip(-1);
      }
    });
    localJPanel.add(localJButton1);
    localJPanel.add(Box.createRigidArea(new Dimension(5, 0)));
    JButton localJButton2 = new JButton("Next Tip");
    localJButton2.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        TipOfTheDay.this.changeTip(1);
      }
    });
    localJButton2.setPreferredSize(localJButton1.getPreferredSize());
    localJPanel.add(localJButton2);
    localJPanel.add(Box.createRigidArea(new Dimension(5, 0)));
    JButton localJButton3 = new JButton("Close");
    localJButton3.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        TipOfTheDay.this.dispose();
        TipOfTheDay.this.setVisible(false);
      }
    });
    localJButton3.setPreferredSize(localJButton1.getPreferredSize());
    setFocusTraversalPolicy(new MyFocus(getFocusTraversalPolicy(), localJButton3));
    localJButton3.addKeyListener(new KeyListener()
    {
      public void keyTyped(KeyEvent paramAnonymousKeyEvent)
      {
      }

      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
        if (paramAnonymousKeyEvent.getKeyCode() == 10)
        {
          TipOfTheDay.this.dispose();
          TipOfTheDay.this.setVisible(false);
        }
      }

      public void keyReleased(KeyEvent paramAnonymousKeyEvent)
      {
      }
    });
    localJPanel.add(localJButton3);
    getContentPane().add(localJPanel, paramGridBagConstraints);
  }

  private void changeTip(int paramInt)
  {
    this.currentTip += paramInt;
    if (this.currentTip == this.tipPaths.size())
      this.currentTip = 0;
    else if (this.currentTip == -1)
      this.currentTip = (this.tipPaths.size() - 1);
    if (!this.tipLoadingFailed)
      try
      {
        this.tipPane.setPage(getClass().getResource((String)this.tipPaths.get(this.currentTip)));
      }
      catch (IOException localIOException)
      {
        this.tipLoadingFailed = true;
      }
    if (this.tipLoadingFailed)
      this.tipPane.setText("Error displaying tips, no tip to display");
  }

  private void populateTipsList(String paramString, List paramList)
    throws IOException
  {
    try
    {
      URL localURL = getClass().getResource(paramString);
      Object localObject1;
      Object localObject2;
      if (localURL.toString().startsWith("jar"))
      {
        localObject1 = (JarURLConnection)localURL.openConnection();
        localObject2 = ((JarURLConnection)localObject1).getJarFile();
        Enumeration localEnumeration = ((JarFile)localObject2).entries();
        while (localEnumeration.hasMoreElements())
        {
          JarEntry localJarEntry = (JarEntry)localEnumeration.nextElement();
          String str = localJarEntry.getName();
          if ((!localJarEntry.isDirectory()) && (str.contains("/res/tips/")) && (str.endsWith(".html")))
            paramList.add('/' + str);
        }
      }
      else
      {
        localObject1 = new BufferedReader(new InputStreamReader(localURL.openStream()));
        while ((localObject2 = ((BufferedReader)localObject1).readLine()) != null)
          if (((String)localObject2).indexOf(46) == -1)
            populateTipsList(paramString + '/' + (String)localObject2, paramList);
          else if ((((String)localObject2).endsWith(".htm")) || (((String)localObject2).endsWith(".html")))
            paramList.add(paramString + '/' + (String)localObject2);
        ((BufferedReader)localObject1).close();
      }
    }
    catch (IOException localIOException)
    {
      throw localIOException;
    }
  }

  private void addCenterTip(GridBagConstraints paramGridBagConstraints)
  {
    this.tipPane.setEditable(false);
    this.tipPane.setAutoscrolls(true);
    this.tipPane.addHyperlinkListener(new HyperlinkListener()
    {
      public void hyperlinkUpdate(HyperlinkEvent paramAnonymousHyperlinkEvent)
      {
        if (paramAnonymousHyperlinkEvent.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED))
          try
          {
            BrowserLauncher.openURL(paramAnonymousHyperlinkEvent.getURL().toExternalForm());
          }
          catch (IOException localIOException)
          {
            localIOException.printStackTrace();
          }
      }
    });
    JScrollPane localJScrollPane = new JScrollPane();
    localJScrollPane.getViewport().add(this.tipPane);
    localJScrollPane.setBorder(BorderFactory.createBevelBorder(1));
    getContentPane().add(localJScrollPane, paramGridBagConstraints);
    changeTip(0);
  }

  private void addTopPanel(GridBagConstraints paramGridBagConstraints)
  {
    JPanel localJPanel = new JPanel();
    localJPanel.setLayout(new BoxLayout(localJPanel, 2));
    JLabel localJLabel1 = new JLabel(new ImageIcon(getClass().getResource("/org/jpedal/examples/viewer/res/tip.png")));
    localJPanel.add(localJLabel1);
    JLabel localJLabel2 = new JLabel("Did you know ... ?");
    Font localFont = localJLabel2.getFont().deriveFont(16.0F);
    localJLabel2.setFont(localFont);
    localJPanel.add(Box.createRigidArea(new Dimension(10, 0)));
    localJPanel.add(localJLabel2);
    getContentPane().add(localJPanel, paramGridBagConstraints);
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
 * Qualified Name:     org.jpedal.examples.viewer.gui.popups.TipOfTheDay
 * JD-Core Version:    0.6.2
 */