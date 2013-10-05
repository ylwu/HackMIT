package org.jpedal.examples.jpaneldemo;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.PrintStream;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import org.jpedal.PdfDecoder;
import org.jpedal.examples.viewer.utils.FileFilterer;
import org.jpedal.exception.PdfException;
import org.jpedal.fonts.FontMappings;

public class JPanelDemo extends JFrame
{
  private String viewerTitle = "Jpanel Demo";
  private PdfDecoder pdfDecoder;
  private String currentFile = null;
  private int currentPage = 1;
  private final JLabel pageCounter1 = new JLabel("Page ");
  private JTextField pageCounter2 = new JTextField(4);
  private JLabel pageCounter3 = new JLabel("of");

  public JPanelDemo(String paramString)
  {
    this.pdfDecoder = new PdfDecoder(true);
    FontMappings.setFontReplacements();
    this.currentFile = paramString;
    try
    {
      this.pdfDecoder.openPdfFile(this.currentFile);
      this.pdfDecoder.decodePage(this.currentPage);
      this.pdfDecoder.setPageParameters(1.0F, 1);
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    initializeViewer();
    this.pageCounter2.setText(String.valueOf(this.currentPage));
    this.pageCounter3.setText("of " + this.pdfDecoder.getPageCount());
  }

  public JPanelDemo()
  {
    setTitle(this.viewerTitle);
    this.pdfDecoder = new PdfDecoder(true);
    FontMappings.setFontReplacements();
    initializeViewer();
  }

  private void selectFile()
  {
    JFileChooser localJFileChooser = new JFileChooser(".");
    localJFileChooser.setFileSelectionMode(0);
    String[] arrayOfString = { "pdf" };
    localJFileChooser.addChoosableFileFilter(new FileFilterer(arrayOfString, "Pdf (*.pdf)"));
    int i = -1;
    while (i == -1)
    {
      i = localJFileChooser.showOpenDialog(this);
      if (i == -1)
        System.err.println("JFileChooser error");
      if (i == 0)
      {
        this.currentFile = localJFileChooser.getSelectedFile().getAbsolutePath();
        this.currentPage = 1;
        try
        {
          this.pdfDecoder.closePdfFile();
          this.pdfDecoder.openPdfFile(this.currentFile);
          if (!checkEncryption())
            i = 1;
          this.pdfDecoder.decodePage(this.currentPage);
          this.pdfDecoder.setPageParameters(1.0F, 1);
          this.pdfDecoder.invalidate();
        }
        catch (Exception localException)
        {
          localException.printStackTrace();
        }
        this.pageCounter2.setText(String.valueOf(this.currentPage));
        this.pageCounter3.setText("of " + this.pdfDecoder.getPageCount());
        setTitle(this.viewerTitle + " - " + this.currentFile);
        repaint();
      }
    }
  }

  private boolean checkEncryption()
  {
    if (this.pdfDecoder.isEncrypted())
    {
      while (!this.pdfDecoder.isFileViewable())
      {
        String str = JOptionPane.showInputDialog(this, "Please enter password");
        if (str != null)
          try
          {
            this.pdfDecoder.setEncryptionPassword(str);
          }
          catch (PdfException localPdfException)
          {
            localPdfException.printStackTrace();
          }
      }
      return true;
    }
    return true;
  }

  private void initializeViewer()
  {
    setDefaultCloseOperation(3);
    Container localContainer = getContentPane();
    localContainer.setLayout(new BorderLayout());
    JButton localJButton = initOpenBut();
    Component[] arrayOfComponent = initChangerPanel();
    JPanel localJPanel = new JPanel();
    localJPanel.setLayout(new FlowLayout(0, 0, 0));
    localJPanel.add(localJButton);
    for (Component localComponent : arrayOfComponent)
      localJPanel.add(localComponent);
    localContainer.add(localJPanel, "North");
    ??? = initPDFDisplay();
    localContainer.add((Component)???, "Center");
    pack();
    Dimension localDimension = Toolkit.getDefaultToolkit().getScreenSize();
    setSize(localDimension.width / 2, localDimension.height / 2);
    setLocationRelativeTo(null);
    setVisible(true);
  }

  private JButton initOpenBut()
  {
    JButton localJButton = new JButton();
    localJButton.setIcon(new ImageIcon(getClass().getResource("/org/jpedal/examples/viewer/res/open.gif")));
    localJButton.setText("Open");
    localJButton.setToolTipText("Open a file");
    localJButton.setBorderPainted(false);
    localJButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        JPanelDemo.this.selectFile();
      }
    });
    return localJButton;
  }

  private JScrollPane initPDFDisplay()
  {
    JScrollPane localJScrollPane = new JScrollPane();
    localJScrollPane.setHorizontalScrollBarPolicy(30);
    localJScrollPane.setVerticalScrollBarPolicy(20);
    localJScrollPane.setViewportView(this.pdfDecoder);
    return localJScrollPane;
  }

  private Component[] initChangerPanel()
  {
    Component[] arrayOfComponent = new Component[11];
    JButton localJButton1 = new JButton();
    localJButton1.setBorderPainted(false);
    URL localURL1 = getClass().getResource("/org/jpedal/examples/viewer/res/start.gif");
    localJButton1.setIcon(new ImageIcon(localURL1));
    localJButton1.setToolTipText("Rewind to page 1");
    arrayOfComponent[0] = localJButton1;
    localJButton1.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        if ((JPanelDemo.this.currentFile != null) && (JPanelDemo.this.currentPage != 1))
        {
          JPanelDemo.this.currentPage = 1;
          try
          {
            JPanelDemo.this.pdfDecoder.decodePage(JPanelDemo.this.currentPage);
            JPanelDemo.this.pdfDecoder.invalidate();
            JPanelDemo.this.repaint();
          }
          catch (Exception localException)
          {
            System.err.println("back to page 1");
            localException.printStackTrace();
          }
          JPanelDemo.this.pageCounter2.setText(String.valueOf(JPanelDemo.this.currentPage));
        }
      }
    });
    JButton localJButton2 = new JButton();
    localJButton2.setBorderPainted(false);
    URL localURL2 = getClass().getResource("/org/jpedal/examples/viewer/res/fback.gif");
    localJButton2.setIcon(new ImageIcon(localURL2));
    localJButton2.setToolTipText("Rewind 10 pages");
    arrayOfComponent[1] = localJButton2;
    localJButton2.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        if ((JPanelDemo.this.currentFile != null) && (JPanelDemo.this.currentPage > 10))
        {
          JPanelDemo.access$220(JPanelDemo.this, 10);
          try
          {
            JPanelDemo.this.pdfDecoder.decodePage(JPanelDemo.this.currentPage);
            JPanelDemo.this.pdfDecoder.invalidate();
            JPanelDemo.this.repaint();
          }
          catch (Exception localException)
          {
            System.err.println("back 10 pages");
            localException.printStackTrace();
          }
          JPanelDemo.this.pageCounter2.setText(String.valueOf(JPanelDemo.this.currentPage));
        }
      }
    });
    JButton localJButton3 = new JButton();
    localJButton3.setBorderPainted(false);
    URL localURL3 = getClass().getResource("/org/jpedal/examples/viewer/res/back.gif");
    localJButton3.setIcon(new ImageIcon(localURL3));
    localJButton3.setToolTipText("Rewind one page");
    arrayOfComponent[2] = localJButton3;
    localJButton3.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        if ((JPanelDemo.this.currentFile != null) && (JPanelDemo.this.currentPage > 1))
        {
          JPanelDemo.access$220(JPanelDemo.this, 1);
          try
          {
            JPanelDemo.this.pdfDecoder.decodePage(JPanelDemo.this.currentPage);
            JPanelDemo.this.pdfDecoder.invalidate();
            JPanelDemo.this.repaint();
          }
          catch (Exception localException)
          {
            System.err.println("back 1 page");
            localException.printStackTrace();
          }
          JPanelDemo.this.pageCounter2.setText(String.valueOf(JPanelDemo.this.currentPage));
        }
      }
    });
    this.pageCounter2.setEditable(true);
    this.pageCounter2.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        String str = JPanelDemo.this.pageCounter2.getText().trim();
        try
        {
          int i = Integer.parseInt(str);
          if (((i > JPanelDemo.this.pdfDecoder.getPageCount() ? 1 : 0) | (i < 1 ? 1 : 0)) != 0)
            return;
          JPanelDemo.this.currentPage = i;
          try
          {
            JPanelDemo.this.pdfDecoder.decodePage(JPanelDemo.this.currentPage);
            JPanelDemo.this.pdfDecoder.invalidate();
            JPanelDemo.this.repaint();
          }
          catch (Exception localException1)
          {
            System.err.println("page number entered");
            localException1.printStackTrace();
          }
        }
        catch (Exception localException2)
        {
          JOptionPane.showMessageDialog(null, '>' + str + "< is Not a valid Value.\nPlease enter a number between 1 and " + JPanelDemo.this.pdfDecoder.getPageCount());
        }
      }
    });
    arrayOfComponent[3] = this.pageCounter1;
    arrayOfComponent[4] = new JPanel();
    arrayOfComponent[5] = this.pageCounter2;
    arrayOfComponent[6] = new JPanel();
    arrayOfComponent[7] = this.pageCounter3;
    JButton localJButton4 = new JButton();
    localJButton4.setBorderPainted(false);
    URL localURL4 = getClass().getResource("/org/jpedal/examples/viewer/res/forward.gif");
    localJButton4.setIcon(new ImageIcon(localURL4));
    localJButton4.setToolTipText("forward 1 page");
    arrayOfComponent[8] = localJButton4;
    localJButton4.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        if ((JPanelDemo.this.currentFile != null) && (JPanelDemo.this.currentPage < JPanelDemo.this.pdfDecoder.getPageCount()))
        {
          JPanelDemo.access$212(JPanelDemo.this, 1);
          try
          {
            JPanelDemo.this.pdfDecoder.decodePage(JPanelDemo.this.currentPage);
            JPanelDemo.this.pdfDecoder.invalidate();
            JPanelDemo.this.repaint();
          }
          catch (Exception localException)
          {
            System.err.println("forward 1 page");
            localException.printStackTrace();
          }
          JPanelDemo.this.pageCounter2.setText(String.valueOf(JPanelDemo.this.currentPage));
        }
      }
    });
    JButton localJButton5 = new JButton();
    localJButton5.setBorderPainted(false);
    URL localURL5 = getClass().getResource("/org/jpedal/examples/viewer/res/fforward.gif");
    localJButton5.setIcon(new ImageIcon(localURL5));
    localJButton5.setToolTipText("Fast forward 10 pages");
    arrayOfComponent[9] = localJButton5;
    localJButton5.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        if ((JPanelDemo.this.currentFile != null) && (JPanelDemo.this.currentPage < JPanelDemo.this.pdfDecoder.getPageCount() - 9))
        {
          JPanelDemo.access$212(JPanelDemo.this, 10);
          try
          {
            JPanelDemo.this.pdfDecoder.decodePage(JPanelDemo.this.currentPage);
            JPanelDemo.this.pdfDecoder.invalidate();
            JPanelDemo.this.repaint();
          }
          catch (Exception localException)
          {
            System.err.println("forward 10 pages");
            localException.printStackTrace();
          }
          JPanelDemo.this.pageCounter2.setText(String.valueOf(JPanelDemo.this.currentPage));
        }
      }
    });
    JButton localJButton6 = new JButton();
    localJButton6.setBorderPainted(false);
    URL localURL6 = getClass().getResource("/org/jpedal/examples/viewer/res/end.gif");
    localJButton6.setIcon(new ImageIcon(localURL6));
    localJButton6.setToolTipText("Fast forward to last page");
    arrayOfComponent[10] = localJButton6;
    localJButton6.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        if ((JPanelDemo.this.currentFile != null) && (JPanelDemo.this.currentPage < JPanelDemo.this.pdfDecoder.getPageCount()))
        {
          JPanelDemo.this.currentPage = JPanelDemo.this.pdfDecoder.getPageCount();
          try
          {
            JPanelDemo.this.pdfDecoder.decodePage(JPanelDemo.this.currentPage);
            JPanelDemo.this.pdfDecoder.invalidate();
            JPanelDemo.this.repaint();
          }
          catch (Exception localException)
          {
            System.err.println("forward to last page");
            localException.printStackTrace();
          }
          JPanelDemo.this.pageCounter2.setText(String.valueOf(JPanelDemo.this.currentPage));
        }
      }
    });
    return arrayOfComponent;
  }

  public static void main(String[] paramArrayOfString)
  {
    if (paramArrayOfString.length > 0)
      new JPanelDemo(paramArrayOfString[0]);
    else
      new JPanelDemo();
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.jpaneldemo.JPanelDemo
 * JD-Core Version:    0.6.2
 */