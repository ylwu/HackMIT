package org.jpedal.examples.viewer.gui.popups;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.print.attribute.standard.PageRanges;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import org.jpedal.examples.viewer.Viewer;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.Messages;

public class AddHeaderFooterToPDFPages extends Save
{
  private static final long serialVersionUID = -8681143216306570454L;
  JLabel OutputLabel = new JLabel();
  ButtonGroup buttonGroup1 = new ButtonGroup();
  ButtonGroup buttonGroup2 = new ButtonGroup();
  JToggleButton jToggleButton3 = new JToggleButton();
  JToggleButton jToggleButton2 = new JToggleButton();
  JRadioButton printAll = new JRadioButton();
  JRadioButton printCurrent = new JRadioButton();
  JRadioButton printPages = new JRadioButton();
  JTextField pagesBox = new JTextField();
  JTextField leftHeaderBox = new JTextField();
  JTextField centerHeaderBox = new JTextField();
  JTextField rightHeaderBox = new JTextField();
  JTextField leftFooterBox = new JTextField();
  JTextField centerFooterBox = new JTextField();
  JTextField rightFooterBox = new JTextField();
  JComboBox fontsList = new JComboBox(new String[] { "Courier", "Courier-Bold", "Courier-Oblique", "Courier-BoldOblique", "Helvetica", "Helvetica-Bold", "Helvetica-BoldOblique", "Helvetica-Oblique", "Times-Roman", "Times-Bold", "Times-Italic", "Times-BoldItalic", "Symbol", "ZapfDingbats" });
  JSpinner fontSize = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));
  JLabel colorBox = new JLabel();
  JSpinner leftRightBox = new JSpinner(new SpinnerNumberModel(36.0D, 1.0D, 1000.0D, 1.0D));
  JSpinner topBottomBox = new JSpinner(new SpinnerNumberModel(36.0D, 1.0D, 1000.0D, 1.0D));
  JTextArea tagsList = new JTextArea();

  public AddHeaderFooterToPDFPages(String paramString, int paramInt1, int paramInt2)
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
              JOptionPane.showMessageDialog(this, Messages.getMessage("PdfViewerText.Page") + ' ' + k + ' ' + Messages.getMessage("PdfViewerError.OutOfBounds") + ' ' + Messages.getMessage("PdfViewerText.PageCount") + ' ' + this.end_page);
            return null;
          }
          arrayOfInt[j] = k;
          j++;
        }
      }
      catch (IllegalArgumentException localIllegalArgumentException)
      {
        LogWriter.writeLog(Messages.getMessage("PdfViewerError.Exception") + ' ' + localIllegalArgumentException + ' ' + Messages.getMessage("PdfViewerError.ExportPdfError"));
        if (Viewer.showMessages)
          JOptionPane.showMessageDialog(this, Messages.getMessage("PdfViewerError.InvalidSyntax"));
      }
    }
    return arrayOfInt;
  }

  public float getLeftRightMargin()
  {
    return Float.parseFloat(this.leftRightBox.getValue().toString());
  }

  public float getTopBottomMargin()
  {
    return Float.parseFloat(this.topBottomBox.getValue().toString());
  }

  public String getFontName()
  {
    return (String)this.fontsList.getSelectedItem();
  }

  public int getFontSize()
  {
    return Integer.parseInt(this.fontSize.getValue().toString());
  }

  public Color getFontColor()
  {
    return this.colorBox.getBackground();
  }

  public String getLeftHeader()
  {
    return this.leftHeaderBox.getText();
  }

  public String getCenterHeader()
  {
    return this.centerHeaderBox.getText();
  }

  public String getRightHeader()
  {
    return this.rightHeaderBox.getText();
  }

  public String getLeftFooter()
  {
    return this.leftFooterBox.getText();
  }

  public String getCenterFooter()
  {
    return this.centerFooterBox.getText();
  }

  public String getRightFooter()
  {
    return this.rightFooterBox.getText();
  }

  private void jbInit()
    throws Exception
  {
    JLabel localJLabel1 = new JLabel(Messages.getMessage("PdfViewerLabel.TextAndFont"));
    localJLabel1.setFont(new Font("Dialog", 1, 14));
    localJLabel1.setDisplayedMnemonic('0');
    localJLabel1.setBounds(new Rectangle(13, 13, 220, 26));
    JLabel localJLabel2 = new JLabel(Messages.getMessage("PdfViewerLabel.Left"));
    localJLabel2.setBounds(new Rectangle(130, 40, 50, 23));
    JLabel localJLabel3 = new JLabel(Messages.getMessage("PdfViewerLabel.Center"));
    localJLabel3.setBounds(new Rectangle(300, 40, 50, 23));
    JLabel localJLabel4 = new JLabel(Messages.getMessage("PdfViewerLabel.Right"));
    localJLabel4.setBounds(new Rectangle(475, 40, 50, 23));
    JLabel localJLabel5 = new JLabel(Messages.getMessage("PdfViewerLabel.Header"));
    localJLabel5.setBounds(new Rectangle(20, 60, 90, 23));
    JLabel localJLabel6 = new JLabel(Messages.getMessage("PdfViewerLabel.Footer"));
    localJLabel6.setBounds(new Rectangle(20, 90, 50, 23));
    this.leftHeaderBox.setBounds(new Rectangle(85, 60, 133, 23));
    this.centerHeaderBox.setBounds(new Rectangle(250, 60, 133, 23));
    this.rightHeaderBox.setBounds(new Rectangle(425, 60, 133, 23));
    this.leftFooterBox.setBounds(new Rectangle(85, 90, 133, 23));
    this.centerFooterBox.setBounds(new Rectangle(250, 90, 133, 23));
    this.rightFooterBox.setBounds(new Rectangle(425, 90, 133, 23));
    JLabel localJLabel7 = new JLabel(Messages.getMessage("PdfViewerLabel.Font"));
    localJLabel7.setBounds(new Rectangle(20, 120, 75, 23));
    this.fontsList.setBounds(new Rectangle(85, 120, 150, 23));
    this.fontsList.setSelectedItem("Helvetica");
    JLabel localJLabel8 = new JLabel(Messages.getMessage("PdfViewerLabel.Size"));
    localJLabel8.setBounds(new Rectangle(250, 120, 50, 23));
    this.fontSize.setBounds(new Rectangle(290, 120, 50, 23));
    JLabel localJLabel9 = new JLabel(Messages.getMessage("PdfViewerLabel.Color"));
    localJLabel9.setBounds(new Rectangle(360, 120, 50, 23));
    this.colorBox.setBackground(Color.black);
    this.colorBox.setOpaque(true);
    this.colorBox.setBounds(new Rectangle(410, 120, 23, 23));
    JButton localJButton = new JButton(Messages.getMessage("PdfViewerButton.ChooseColor"));
    localJButton.setBounds(new Rectangle(450, 120, 160, 23));
    localJButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        AddHeaderFooterToPDFPages.this.colorBox.setBackground(JColorChooser.showDialog(null, "Color", AddHeaderFooterToPDFPages.this.colorBox.getBackground()));
      }
    });
    this.tagsList.setText("You may use the following\ntags as part of the text.\n\n<d> - Date in short format\n<D> - Date in long format\n<t> - Time in 12-hour format\n<T> - Time in 24-hour format\n<f> - Filename\n<F> - Full path filename\n<p> - Current page number\n<P> - Total number of pages");
    this.tagsList.setOpaque(false);
    this.tagsList.setBounds(350, 160, 200, 210);
    JLabel localJLabel10 = new JLabel(Messages.getMessage("PdfViewerLabel.Margins"));
    localJLabel10.setFont(new Font("Dialog", 1, 14));
    localJLabel10.setDisplayedMnemonic('0');
    localJLabel10.setBounds(new Rectangle(13, 150, 220, 26));
    JLabel localJLabel11 = new JLabel(Messages.getMessage("PdfViewerLabel.LeftAndRight"));
    localJLabel11.setBounds(new Rectangle(20, 185, 90, 23));
    this.leftRightBox.setBounds(new Rectangle(100, 185, 70, 23));
    JLabel localJLabel12 = new JLabel(Messages.getMessage("PdfViewerLabel.TopAndBottom"));
    localJLabel12.setBounds(new Rectangle(180, 185, 120, 23));
    this.topBottomBox.setBounds(new Rectangle(300, 185, 70, 23));
    this.pageRangeLabel.setText(Messages.getMessage("PdfViewerPageRange.text"));
    this.pageRangeLabel.setBounds(new Rectangle(13, 220, 400, 26));
    this.printAll.setText(Messages.getMessage("PdfViewerRadioButton.All"));
    this.printAll.setBounds(new Rectangle(23, 250, 75, 22));
    this.printCurrent.setText(Messages.getMessage("PdfViewerRadioButton.CurrentPage"));
    this.printCurrent.setBounds(new Rectangle(23, 270, 100, 22));
    this.printCurrent.setSelected(true);
    this.printPages.setText(Messages.getMessage("PdfViewerRadioButton.Pages"));
    this.printPages.setBounds(new Rectangle(23, 292, 70, 22));
    this.pagesBox.setBounds(new Rectangle(95, 292, 230, 22));
    this.pagesBox.addKeyListener(new KeyListener()
    {
      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
      }

      public void keyReleased(KeyEvent paramAnonymousKeyEvent)
      {
        if (AddHeaderFooterToPDFPages.this.pagesBox.getText().isEmpty())
          AddHeaderFooterToPDFPages.this.printCurrent.setSelected(true);
        else
          AddHeaderFooterToPDFPages.this.printPages.setSelected(true);
      }

      public void keyTyped(KeyEvent paramAnonymousKeyEvent)
      {
      }
    });
    JTextArea localJTextArea = new JTextArea(Messages.getMessage("PdfViewerMessage.PageNumberOrRangeLong"));
    localJTextArea.setBounds(new Rectangle(23, 320, 620, 40));
    localJTextArea.setOpaque(false);
    add(this.printAll, null);
    add(this.printCurrent, null);
    add(this.printPages, null);
    add(this.pagesBox, null);
    add(localJTextArea, null);
    add(localJLabel2, null);
    add(localJLabel3, null);
    add(localJLabel4, null);
    add(localJLabel5, null);
    add(localJLabel6, null);
    add(this.leftHeaderBox, null);
    add(this.centerHeaderBox, null);
    add(this.rightHeaderBox, null);
    add(this.leftFooterBox, null);
    add(this.centerFooterBox, null);
    add(this.rightFooterBox, null);
    add(localJLabel7, null);
    add(this.fontsList, null);
    add(localJLabel8, null);
    add(this.fontSize, null);
    add(localJLabel9, null);
    add(this.colorBox, null);
    add(localJButton, null);
    add(localJLabel10, null);
    add(localJLabel11, null);
    add(this.leftRightBox, null);
    add(localJLabel12, null);
    add(this.topBottomBox, null);
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
    return new Dimension(620, 350);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.gui.popups.AddHeaderFooterToPDFPages
 * JD-Core Version:    0.6.2
 */