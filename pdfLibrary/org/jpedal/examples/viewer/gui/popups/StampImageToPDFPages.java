package org.jpedal.examples.viewer.gui.popups;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import javax.print.attribute.standard.PageRanges;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import org.jpedal.examples.viewer.Viewer;
import org.jpedal.examples.viewer.utils.FileFilterer;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.Messages;

public class StampImageToPDFPages extends Save
{
  JLabel OutputLabel = new JLabel();
  ButtonGroup buttonGroup1 = new ButtonGroup();
  ButtonGroup buttonGroup2 = new ButtonGroup();
  JToggleButton jToggleButton3 = new JToggleButton();
  JToggleButton jToggleButton2 = new JToggleButton();
  JRadioButton printAll = new JRadioButton();
  JRadioButton printCurrent = new JRadioButton();
  JRadioButton printPages = new JRadioButton();
  JTextField pagesBox = new JTextField();
  JTextField imageBox = new JTextField();
  JSpinner rotationBox = new JSpinner(new SpinnerNumberModel(0, 0, 360, 1));
  JComboBox placementBox = new JComboBox(new String[] { Messages.getMessage("PdfViewerLabel.Overlay"), Messages.getMessage("PdfViewerLabel.Underlay") });
  JSpinner heightScale = new JSpinner(new SpinnerNumberModel(100, 1, 1000, 1));
  JSpinner widthScale = new JSpinner(new SpinnerNumberModel(100, 1, 1000, 1));
  JComboBox horizontalBox = new JComboBox(new String[] { Messages.getMessage("PdfViewerLabel.FromLeft"), Messages.getMessage("PdfViewerLabel.Centered"), Messages.getMessage("PdfViewerLabel.FromRight") });
  JComboBox verticalBox = new JComboBox(new String[] { Messages.getMessage("PdfViewerLabel.FromTop"), Messages.getMessage("PdfViewerLabel.Centered"), Messages.getMessage("PdfViewerLabel.FromBottom") });
  JSpinner horizontalOffset = new JSpinner(new SpinnerNumberModel(0.0D, -1000.0D, 1000.0D, 1.0D));
  JSpinner verticalOffset = new JSpinner(new SpinnerNumberModel(0.0D, -1000.0D, 1000.0D, 1.0D));

  public StampImageToPDFPages(String paramString, int paramInt1, int paramInt2)
  {
    super(paramString, paramInt1, paramInt2);
    try
    {
      jbInit();
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }

  public final int[] getPages()
  {
    int[] arrayOfInt = null;
    if (this.printAll.isSelected())
    {
      arrayOfInt = new int[this.end_page];
      for (int i = 0; i < this.end_page; i++)
        arrayOfInt[i] = (i + 1);
    }
    else if (this.printCurrent.isSelected())
    {
      arrayOfInt = new int[1];
      arrayOfInt[0] = this.currentPage;
    }
    else if (this.printPages.isSelected())
    {
      try
      {
        PageRanges localPageRanges = new PageRanges(this.pagesBox.getText());
        int j = 0;
        int k = -1;
        while ((k = localPageRanges.next(k)) != -1)
          j++;
        arrayOfInt = new int[j];
        j = 0;
        k = -1;
        while ((k = localPageRanges.next(k)) != -1)
        {
          if (k > this.end_page)
          {
            if (Viewer.showMessages)
              JOptionPane.showMessageDialog(this, Messages.getMessage("PdfViewerText.Page") + ' ' + localPageRanges + ' ' + Messages.getMessage("PdfViewerError.OutOfBounds") + ' ' + Messages.getMessage("PdfViewerText.PageCount") + ' ' + this.end_page);
            return null;
          }
          arrayOfInt[j] = k;
          j++;
        }
      }
      catch (IllegalArgumentException localIllegalArgumentException)
      {
        LogWriter.writeLog("Exception " + localIllegalArgumentException + " in exporting pdfs");
        if (Viewer.showMessages)
          JOptionPane.showMessageDialog(this, Messages.getMessage("PdfViewerError.InvalidSyntax"));
      }
    }
    return arrayOfInt;
  }

  public float getHorizontalOffset()
  {
    return Float.parseFloat(this.horizontalOffset.getValue().toString());
  }

  public float getVerticalOffset()
  {
    return Float.parseFloat(this.verticalOffset.getValue().toString());
  }

  public String getHorizontalPosition()
  {
    return (String)this.horizontalBox.getSelectedItem();
  }

  public String getVerticalPosition()
  {
    return (String)this.verticalBox.getSelectedItem();
  }

  public int getRotation()
  {
    return Integer.parseInt(this.rotationBox.getValue().toString());
  }

  public String getPlacement()
  {
    return (String)this.placementBox.getSelectedItem();
  }

  public int getHeightScale()
  {
    return Integer.parseInt(this.heightScale.getValue().toString());
  }

  public int getWidthScale()
  {
    return Integer.parseInt(this.widthScale.getValue().toString());
  }

  public String getImageLocation()
  {
    return this.imageBox.getText();
  }

  private void jbInit()
    throws Exception
  {
    JLabel localJLabel1 = new JLabel(Messages.getMessage("PdfViewerLabel.TextAndFont"));
    localJLabel1.setFont(new Font("Dialog", 1, 14));
    localJLabel1.setDisplayedMnemonic('0');
    localJLabel1.setBounds(new Rectangle(13, 13, 220, 26));
    JLabel localJLabel2 = new JLabel(Messages.getMessage("PdfViewerLabel.Image"));
    localJLabel2.setBounds(new Rectangle(20, 40, 50, 23));
    this.imageBox.setBounds(new Rectangle(55, 40, 295, 23));
    JButton localJButton = new JButton("...");
    localJButton.setBounds(new Rectangle(360, 40, 23, 23));
    localJButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        JFileChooser localJFileChooser = new JFileChooser(StampImageToPDFPages.this.root_dir);
        localJFileChooser.setFileSelectionMode(0);
        String[] arrayOfString = { "png", "tif", "tiff", "jpg", "jpeg" };
        localJFileChooser.addChoosableFileFilter(new FileFilterer(arrayOfString, "Images (Tiff, Jpeg,Png)"));
        int i = localJFileChooser.showOpenDialog(null);
        File localFile = localJFileChooser.getSelectedFile();
        if ((localFile != null) && (i == 0))
          StampImageToPDFPages.this.imageBox.setText(localFile.getAbsolutePath());
      }
    });
    JLabel localJLabel3 = new JLabel(Messages.getMessage("PdfViewerLabel.Rotation"));
    localJLabel3.setBounds(new Rectangle(20, 80, 90, 23));
    this.rotationBox.setBounds(new Rectangle(80, 80, 50, 23));
    JLabel localJLabel4 = new JLabel(Messages.getMessage("PdfViewerText.Degrees"));
    localJLabel4.setBounds(new Rectangle(140, 80, 50, 23));
    JLabel localJLabel5 = new JLabel(Messages.getMessage("PdfViewerLabel.Placement"));
    localJLabel5.setBounds(new Rectangle(240, 80, 70, 23));
    this.placementBox.setBounds(new Rectangle(300, 80, 83, 23));
    JLabel localJLabel6 = new JLabel(Messages.getMessage("PdfViewerLabel.WidthScale"));
    localJLabel6.setBounds(new Rectangle(20, 120, 100, 23));
    this.widthScale.setBounds(new Rectangle(120, 120, 60, 23));
    JLabel localJLabel7 = new JLabel(Messages.getMessage("PdfViewerLabel.HeightScale"));
    localJLabel7.setBounds(new Rectangle(240, 120, 110, 23));
    this.heightScale.setBounds(new Rectangle(330, 120, 60, 23));
    JLabel localJLabel8 = new JLabel(Messages.getMessage("PdfViewerLabel.PositionAndOffset"));
    localJLabel8.setFont(new Font("Dialog", 1, 14));
    localJLabel8.setDisplayedMnemonic('0');
    localJLabel8.setBounds(new Rectangle(13, 150, 220, 26));
    JLabel localJLabel9 = new JLabel(Messages.getMessage("PdfViewerLabel.Horizontal"));
    localJLabel9.setBounds(new Rectangle(20, 185, 90, 23));
    this.horizontalBox.setBounds(new Rectangle(80, 185, 120, 23));
    this.horizontalBox.setSelectedItem(Messages.getMessage("PdfViewerLabel.Centered"));
    JLabel localJLabel10 = new JLabel(Messages.getMessage("PdfViewerLabel.Vertical"));
    localJLabel10.setBounds(new Rectangle(20, 215, 90, 23));
    this.verticalBox.setBounds(new Rectangle(80, 215, 120, 23));
    this.verticalBox.setSelectedItem(Messages.getMessage("PdfViewerLabel.Centered"));
    JLabel localJLabel11 = new JLabel(Messages.getMessage("PdfViewerLabel.Offset"));
    localJLabel11.setBounds(new Rectangle(250, 185, 90, 23));
    this.horizontalOffset.setBounds(new Rectangle(320, 185, 70, 23));
    JLabel localJLabel12 = new JLabel(Messages.getMessage("PdfViewerLabel.Offset"));
    localJLabel12.setBounds(new Rectangle(250, 215, 90, 23));
    this.verticalOffset.setBounds(new Rectangle(320, 215, 70, 23));
    this.pageRangeLabel.setText(Messages.getMessage("PdfViewerPageRange.text"));
    this.pageRangeLabel.setBounds(new Rectangle(13, 250, 400, 26));
    this.printAll.setText(Messages.getMessage("PdfViewerRadioButton.All"));
    this.printAll.setBounds(new Rectangle(23, 280, 75, 22));
    this.printCurrent.setText(Messages.getMessage("PdfViewerRadioButton.CurrentPage"));
    this.printCurrent.setBounds(new Rectangle(23, 300, 100, 22));
    this.printCurrent.setSelected(true);
    this.printPages.setText(Messages.getMessage("PdfViewerRadioButton.Pages"));
    this.printPages.setBounds(new Rectangle(23, 322, 70, 22));
    this.pagesBox.setBounds(new Rectangle(95, 322, 230, 22));
    this.pagesBox.addKeyListener(new KeyListener()
    {
      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
      }

      public void keyReleased(KeyEvent paramAnonymousKeyEvent)
      {
        if (StampImageToPDFPages.this.pagesBox.getText().isEmpty())
          StampImageToPDFPages.this.printCurrent.setSelected(true);
        else
          StampImageToPDFPages.this.printPages.setSelected(true);
      }

      public void keyTyped(KeyEvent paramAnonymousKeyEvent)
      {
      }
    });
    JTextArea localJTextArea = new JTextArea(Messages.getMessage("PdfViewerMessage.PageNumberOrRange") + '\n' + Messages.getMessage("PdfViewerMessage.PageRangeExample"));
    localJTextArea.setBounds(new Rectangle(23, 355, 400, 40));
    localJTextArea.setOpaque(false);
    add(this.printAll, null);
    add(this.printCurrent, null);
    add(this.printPages, null);
    add(this.pagesBox, null);
    add(localJTextArea, null);
    add(localJLabel2, null);
    add(localJButton, null);
    add(this.imageBox, null);
    add(localJLabel3, null);
    add(this.rotationBox, null);
    add(localJLabel4, null);
    add(localJLabel5, null);
    add(this.placementBox, null);
    add(localJLabel8, null);
    add(localJLabel9, null);
    add(this.horizontalBox, null);
    add(localJLabel10, null);
    add(this.verticalBox, null);
    add(localJLabel11, null);
    add(this.horizontalOffset, null);
    add(localJLabel12, null);
    add(this.verticalOffset, null);
    add(localJLabel6, null);
    add(this.widthScale, null);
    add(localJLabel7, null);
    add(this.heightScale, null);
    add(localJLabel1, null);
    add(this.changeButton, null);
    add(this.pageRangeLabel, null);
    add(this.jToggleButton2, null);
    add(this.jToggleButton3, null);
    this.buttonGroup1.add(this.printAll);
    this.buttonGroup1.add(this.printCurrent);
    this.buttonGroup1.add(this.printPages);
  }

  public final Dimension getPreferredSize()
  {
    return new Dimension(440, 400);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.gui.popups.StampImageToPDFPages
 * JD-Core Version:    0.6.2
 */