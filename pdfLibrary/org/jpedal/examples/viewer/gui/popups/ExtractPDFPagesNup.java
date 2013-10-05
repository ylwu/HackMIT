package org.jpedal.examples.viewer.gui.popups;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.print.attribute.standard.PageRanges;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import org.jpedal.examples.viewer.Viewer;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.Messages;

public class ExtractPDFPagesNup extends Save
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
  ArrayList papers;
  ArrayList paperDimensions;
  private JSpinner horizontalSpacing;
  private JLabel jLabel1;
  private JLabel jLabel11;
  private JLabel jLabel12;
  private JLabel jLabel13;
  private JLabel jLabel14;
  private JLabel jLabel15;
  private JLabel jLabel16;
  private JLabel jLabel17;
  private JLabel jLabel2;
  private JLabel jLabel3;
  private JLabel jLabel4;
  private JSpinner layoutColumns;
  private JSpinner layoutRows;
  private JComboBox layouts;
  private JSpinner leftRightMargins;
  private JSpinner scaleHeight;
  private JCheckBox pageProportionally;
  private JComboBox pageScalings;
  private JSpinner scaleWidth;
  private JSpinner paperHeight;
  private JComboBox paperOrientation;
  private JComboBox paperSizes;
  private JSpinner paperWidth;
  private JSpinner topBottomMargins;
  private JSpinner verticalSpacing;
  private JComboBox repeat = new JComboBox();
  private JSpinner copies = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
  private JComboBox ordering = new JComboBox();
  private JComboBox doubleSided = new JComboBox();

  public ExtractPDFPagesNup(String paramString, int paramInt1, int paramInt2)
  {
    super(paramString, paramInt1, paramInt2);
    genertatePaperSizes();
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
        LogWriter.writeLog("Exception " + localIllegalArgumentException + " in exporting pdfs");
        if (Viewer.showMessages)
          JOptionPane.showMessageDialog(this, Messages.getMessage("PdfViewerError.InvalidSyntax"));
      }
    }
    return arrayOfInt;
  }

  public float getHorizontalSpacing()
  {
    return Float.parseFloat(this.horizontalSpacing.getValue().toString());
  }

  public float getVerticalSpacing()
  {
    return Float.parseFloat(this.verticalSpacing.getValue().toString());
  }

  public float getLeftRightMargin()
  {
    return Float.parseFloat(this.leftRightMargins.getValue().toString());
  }

  public float getTopBottomMargin()
  {
    return Float.parseFloat(this.topBottomMargins.getValue().toString());
  }

  public int getPaperWidth()
  {
    return Integer.parseInt(this.paperWidth.getValue().toString());
  }

  public int getPaperHeight()
  {
    return Integer.parseInt(this.paperHeight.getValue().toString());
  }

  public String getPaperOrientation()
  {
    return (String)this.paperOrientation.getSelectedItem();
  }

  public String getScale()
  {
    return (String)this.pageScalings.getSelectedItem();
  }

  public boolean isScaleProportional()
  {
    return this.pageProportionally.isSelected();
  }

  public float getScaleWidth()
  {
    return Float.parseFloat(this.scaleWidth.getValue().toString());
  }

  public float getScaleHeight()
  {
    return Float.parseFloat(this.scaleHeight.getValue().toString());
  }

  public String getSelectedLayout()
  {
    return (String)this.layouts.getSelectedItem();
  }

  public int getLayoutRows()
  {
    return Integer.parseInt(this.layoutRows.getValue().toString());
  }

  public int getLayoutColumns()
  {
    return Integer.parseInt(this.layoutColumns.getValue().toString());
  }

  public int getRepeat()
  {
    if (this.repeat.getSelectedIndex() == 0)
      return 6;
    if (this.repeat.getSelectedIndex() == 1)
      return 7;
    return 8;
  }

  public int getCopies()
  {
    return Integer.parseInt(this.copies.getValue().toString());
  }

  public int getPageOrdering()
  {
    if (this.ordering.getSelectedIndex() == 0)
      return 3;
    if (this.ordering.getSelectedIndex() == 1)
      return 4;
    return 5;
  }

  public String getDoubleSided()
  {
    return (String)this.doubleSided.getSelectedItem();
  }

  private void jbInit()
    throws Exception
  {
    this.rootFilesLabel.setBounds(new Rectangle(13, 13, 400, 26));
    this.rootDir.setBounds(new Rectangle(20, 40, 232, 23));
    this.changeButton.setBounds(new Rectangle(272, 40, 101, 23));
    JLabel localJLabel1 = new JLabel(Messages.getMessage("PdfViewerNUPLabel.PaperSize"));
    localJLabel1.setFont(new Font("Dialog", 1, 14));
    localJLabel1.setDisplayedMnemonic('0');
    localJLabel1.setBounds(new Rectangle(13, 70, 220, 26));
    JLabel localJLabel2 = new JLabel(Messages.getMessage("PdfViewerNUPLabel.Scale"));
    localJLabel2.setFont(new Font("Dialog", 1, 14));
    localJLabel2.setDisplayedMnemonic('0');
    localJLabel2.setBounds(new Rectangle(13, 140, 220, 26));
    JLabel localJLabel3 = new JLabel(Messages.getMessage("PdfViewerNUPLabel.Layout"));
    localJLabel3.setFont(new Font("Dialog", 1, 14));
    localJLabel3.setDisplayedMnemonic('0');
    localJLabel3.setBounds(new Rectangle(13, 210, 220, 26));
    JLabel localJLabel4 = new JLabel(Messages.getMessage("PdfViewerNUPLabel.Margins"));
    localJLabel4.setFont(new Font("Dialog", 1, 14));
    localJLabel4.setDisplayedMnemonic('0');
    localJLabel4.setBounds(new Rectangle(13, 280, 220, 26));
    JLabel localJLabel5 = new JLabel(Messages.getMessage("PdfViewerNUPLabel.PageSettings"));
    localJLabel5.setFont(new Font("Dialog", 1, 14));
    localJLabel5.setDisplayedMnemonic('0');
    localJLabel5.setBounds(new Rectangle(13, 400, 220, 26));
    this.layouts = new JComboBox();
    this.paperOrientation = new JComboBox();
    this.pageScalings = new JComboBox();
    this.jLabel1 = new JLabel();
    this.jLabel2 = new JLabel();
    this.topBottomMargins = new JSpinner(new SpinnerNumberModel(18.0D, -720.0D, 720.0D, 1.0D));
    this.leftRightMargins = new JSpinner(new SpinnerNumberModel(18.0D, -720.0D, 720.0D, 1.0D));
    this.pageProportionally = new JCheckBox();
    this.paperSizes = new JComboBox();
    this.jLabel11 = new JLabel();
    this.jLabel3 = new JLabel();
    this.jLabel4 = new JLabel();
    this.paperWidth = new JSpinner();
    this.paperHeight = new JSpinner();
    this.scaleWidth = new JSpinner(new SpinnerNumberModel(396.0D, 72.0D, 5184.0D, 1.0D));
    this.scaleHeight = new JSpinner(new SpinnerNumberModel(612.0D, 72.0D, 5184.0D, 1.0D));
    this.jLabel12 = new JLabel();
    this.jLabel13 = new JLabel();
    this.layoutRows = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
    this.layoutColumns = new JSpinner(new SpinnerNumberModel(2, 1, 100, 1));
    this.jLabel14 = new JLabel();
    this.verticalSpacing = new JSpinner(new SpinnerNumberModel(7.2D, 0.0D, 720.0D, 1.0D));
    this.horizontalSpacing = new JSpinner(new SpinnerNumberModel(7.2D, 0.0D, 720.0D, 1.0D));
    this.jLabel16 = new JLabel();
    this.jLabel15 = new JLabel();
    this.jLabel17 = new JLabel();
    this.layouts.setModel(new DefaultComboBoxModel(new String[] { "2 Up", "4 Up", "8 Up", Messages.getMessage("PdfViewerNUPOption.Custom") }));
    this.layouts.setSelectedIndex(0);
    this.layouts.addItemListener(new ItemListener()
    {
      public void itemStateChanged(ItemEvent paramAnonymousItemEvent)
      {
        ExtractPDFPagesNup.this.layoutsSelectionChanged();
      }
    });
    this.copies.setEnabled(false);
    this.repeat.setModel(new DefaultComboBoxModel(new String[] { Messages.getMessage("PdfViewerNUPOption.None"), Messages.getMessage("PdfViewerNUPOption.Auto"), Messages.getMessage("PdfViewerNUPOption.Specified") }));
    this.repeat.addItemListener(new ItemListener()
    {
      public void itemStateChanged(ItemEvent paramAnonymousItemEvent)
      {
        if (ExtractPDFPagesNup.this.repeat.getSelectedItem().equals("None"))
        {
          ExtractPDFPagesNup.this.copies.getModel().setValue(Integer.valueOf(1));
          ExtractPDFPagesNup.this.copies.setEnabled(false);
        }
        else if (ExtractPDFPagesNup.this.repeat.getSelectedItem().equals("Auto"))
        {
          int i = Integer.parseInt(ExtractPDFPagesNup.this.layoutRows.getValue().toString());
          int j = Integer.parseInt(ExtractPDFPagesNup.this.layoutColumns.getValue().toString());
          ExtractPDFPagesNup.this.copies.getModel().setValue(Integer.valueOf(i * j));
          ExtractPDFPagesNup.this.copies.setEnabled(false);
        }
        else if (ExtractPDFPagesNup.this.repeat.getSelectedItem().equals("Specified"))
        {
          ExtractPDFPagesNup.this.copies.setEnabled(true);
        }
      }
    });
    this.ordering.setModel(new DefaultComboBoxModel(new String[] { Messages.getMessage("PdfViewerNUPOption.Across"), Messages.getMessage("PdfViewerNUPOption.Down") }));
    this.doubleSided.setModel(new DefaultComboBoxModel(new String[] { Messages.getMessage("PdfViewerNUPOption.None"), Messages.getMessage("PdfViewerNUPOption.Front&Back"), Messages.getMessage("PdfViewerNUPOption.Gutter") }));
    this.layouts.setBounds(20, 240, 110, 23);
    this.paperOrientation.setModel(new DefaultComboBoxModel(new String[] { Messages.getMessage("PdfViewerNUPOption.Auto"), Messages.getMessage("PdfViewerNUPOption.Portrait"), Messages.getMessage("PdfViewerNUPOption.Landscape") }));
    this.paperOrientation.setBounds(510, 100, 90, 23);
    this.pageScalings.setModel(new DefaultComboBoxModel(new String[] { Messages.getMessage("PdfViewerNUPOption.OriginalSize"), Messages.getMessage("PdfViewerNUPOption.Auto"), Messages.getMessage("PdfViewerNUPOption.Specified") }));
    this.pageScalings.setSelectedIndex(1);
    this.pageScalings.addItemListener(new ItemListener()
    {
      public void itemStateChanged(ItemEvent paramAnonymousItemEvent)
      {
        ExtractPDFPagesNup.this.scalingSelectionChanged();
      }
    });
    this.pageScalings.setBounds(20, 170, 200, 23);
    this.jLabel1.setText(Messages.getMessage("PdfViewerNUPLabel.Width"));
    this.jLabel1.setBounds(148, 100, 50, 15);
    this.jLabel2.setText(Messages.getMessage("PdfViewerNUPLabel.Height"));
    this.jLabel2.setBounds(278, 100, 50, 15);
    this.pageProportionally.setSelected(true);
    this.pageProportionally.setText(Messages.getMessage("PdfViewerNUPText.Proportionally"));
    this.pageProportionally.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    this.pageProportionally.setMargin(new Insets(0, 0, 0, 0));
    this.pageProportionally.setBounds(240, 170, 120, 15);
    this.paperSizes.setModel(new DefaultComboBoxModel(getPaperSizes()));
    this.paperSizes.addItemListener(new ItemListener()
    {
      public void itemStateChanged(ItemEvent paramAnonymousItemEvent)
      {
        ExtractPDFPagesNup.this.pageSelectionChanged();
      }
    });
    this.paperSizes.setBounds(20, 100, 110, 23);
    this.jLabel11.setText(Messages.getMessage("PdfViewerNUPLabel.Orientation"));
    this.jLabel11.setBounds(408, 100, 130, 15);
    this.jLabel3.setText(Messages.getMessage("PdfViewerNUPLabel.Width"));
    this.jLabel3.setBounds(370, 170, 50, 15);
    this.jLabel4.setText(Messages.getMessage("PdfViewerNUPLabel.Height"));
    this.jLabel4.setBounds(500, 170, 50, 15);
    this.paperWidth.setEnabled(false);
    this.paperWidth.setBounds(195, 100, 70, 23);
    this.paperHeight.setEnabled(false);
    this.paperHeight.setBounds(318, 100, 70, 23);
    this.scaleWidth.setEnabled(false);
    this.scaleWidth.setBounds(420, 170, 70, 23);
    this.scaleHeight.setEnabled(false);
    this.scaleHeight.setBounds(540, 170, 70, 23);
    this.jLabel12.setText(Messages.getMessage("PdfViewerNUPLabel.Rows"));
    this.jLabel12.setBounds(148, 240, 50, 15);
    this.jLabel13.setText(Messages.getMessage("PdfViewerNUPLabel.Columns"));
    this.jLabel13.setBounds(278, 240, 50, 15);
    this.layoutRows.setEnabled(false);
    this.layoutRows.setBounds(195, 240, 70, 23);
    this.layoutColumns.setEnabled(false);
    this.layoutColumns.setBounds(328, 240, 70, 23);
    this.jLabel14.setText(Messages.getMessage("PdfViewerNUPLabel.Left&RightMargins"));
    this.jLabel14.setBounds(22, 326, 200, 15);
    this.leftRightMargins.setBounds(210, 322, 70, 23);
    this.jLabel16.setText(Messages.getMessage("PdfViewerNUPLabel.HorizontalSpacing"));
    this.jLabel16.setBounds(22, 356, 180, 15);
    this.horizontalSpacing.setBounds(210, 354, 70, 23);
    this.jLabel15.setText(Messages.getMessage("PdfViewerNUPLabel.Top&BottomMargins"));
    this.jLabel15.setBounds(300, 326, 180, 15);
    this.topBottomMargins.setBounds(480, 320, 70, 23);
    this.jLabel17.setText(Messages.getMessage("PdfViewerNUPLabel.VerticalSpacing"));
    this.jLabel17.setBounds(300, 356, 180, 15);
    this.verticalSpacing.setBounds(480, 354, 70, 23);
    JLabel localJLabel6 = new JLabel(Messages.getMessage("PdfViewerNUPLabel.Repeat"));
    localJLabel6.setBounds(22, 446, 130, 15);
    this.repeat.setBounds(140, 442, 100, 23);
    JLabel localJLabel7 = new JLabel(Messages.getMessage("PdfViewerNUPLabel.Copies"));
    localJLabel7.setBounds(300, 446, 130, 15);
    this.ordering.setBounds(140, 474, 130, 23);
    JLabel localJLabel8 = new JLabel(Messages.getMessage("PdfViewerNUPLabel.PageOrdering"));
    localJLabel8.setBounds(22, 474, 130, 15);
    this.copies.setBounds(420, 440, 70, 23);
    JLabel localJLabel9 = new JLabel(Messages.getMessage("PdfViewerNUPLabel.DoubleSided"));
    localJLabel9.setBounds(300, 476, 130, 15);
    this.doubleSided.setBounds(420, 474, 100, 23);
    this.pageRangeLabel.setText(Messages.getMessage("PdfViewerNUPLabel.PageRange"));
    this.pageRangeLabel.setBounds(new Rectangle(13, 530, 199, 26));
    this.printAll.setText(Messages.getMessage("PdfViewerNUPOption.All"));
    this.printAll.setBounds(new Rectangle(23, 560, 75, 22));
    this.printAll.setSelected(true);
    this.printCurrent.setText(Messages.getMessage("PdfViewerNUPOption.CurrentPage"));
    this.printCurrent.setBounds(new Rectangle(23, 580, 100, 22));
    this.printPages.setText(Messages.getMessage("PdfViewerNUPOption.Pages"));
    this.printPages.setBounds(new Rectangle(23, 600, 70, 22));
    this.pagesBox.setBounds(new Rectangle(95, 602, 230, 22));
    this.pagesBox.addKeyListener(new KeyListener()
    {
      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
      }

      public void keyReleased(KeyEvent paramAnonymousKeyEvent)
      {
        if (ExtractPDFPagesNup.this.pagesBox.getText().isEmpty())
          ExtractPDFPagesNup.this.printCurrent.setSelected(true);
        else
          ExtractPDFPagesNup.this.printPages.setSelected(true);
      }

      public void keyTyped(KeyEvent paramAnonymousKeyEvent)
      {
      }
    });
    JTextArea localJTextArea = new JTextArea(Messages.getMessage("PdfViewerMessage.PageNumberOrRangeLong"));
    localJTextArea.setBounds(new Rectangle(23, 640, 600, 40));
    localJTextArea.setOpaque(false);
    pageSelectionChanged();
    add(this.rootDir, null);
    add(this.rootFilesLabel, null);
    add(this.changeButton, null);
    add(this.printAll, null);
    add(this.printCurrent, null);
    add(localJLabel2);
    add(localJLabel3);
    add(localJLabel4);
    add(this.layoutColumns);
    add(this.layoutRows);
    add(this.layouts);
    add(this.leftRightMargins);
    add(this.scaleHeight);
    add(this.pageProportionally);
    add(this.pageScalings);
    add(this.scaleWidth);
    add(this.paperHeight);
    add(this.paperOrientation);
    add(this.paperSizes);
    add(this.paperWidth);
    add(this.topBottomMargins);
    add(this.verticalSpacing);
    add(this.horizontalSpacing);
    add(this.jLabel1);
    add(this.jLabel2);
    add(this.jLabel3);
    add(this.jLabel4);
    add(this.jLabel11);
    add(this.jLabel12);
    add(this.jLabel13);
    add(this.jLabel14);
    add(this.jLabel15);
    add(this.jLabel16);
    add(this.jLabel17);
    add(localJLabel5);
    add(localJLabel6);
    add(this.repeat);
    add(localJLabel8);
    add(this.copies);
    add(localJLabel7);
    add(this.ordering);
    add(this.printPages, null);
    add(this.pagesBox, null);
    add(localJTextArea, null);
    add(localJLabel1, null);
    add(this.changeButton, null);
    add(this.pageRangeLabel, null);
    add(this.jToggleButton2, null);
    add(this.jToggleButton3, null);
    this.buttonGroup1.add(this.printAll);
    this.buttonGroup1.add(this.printCurrent);
    this.buttonGroup1.add(this.printPages);
  }

  private void layoutsSelectionChanged()
  {
    String str = (String)this.layouts.getSelectedItem();
    if (str.equals("2 Up"))
    {
      this.layoutRows.getModel().setValue(Integer.valueOf(1));
      this.layoutColumns.getModel().setValue(Integer.valueOf(2));
      this.layoutRows.setEnabled(false);
      this.layoutColumns.setEnabled(false);
    }
    else if (str.equals("4 Up"))
    {
      this.layoutRows.getModel().setValue(Integer.valueOf(2));
      this.layoutColumns.getModel().setValue(Integer.valueOf(2));
      this.layoutRows.setEnabled(false);
      this.layoutColumns.setEnabled(false);
    }
    else if (str.equals("8 Up"))
    {
      this.layoutRows.getModel().setValue(Integer.valueOf(2));
      this.layoutColumns.getModel().setValue(Integer.valueOf(4));
      this.layoutRows.setEnabled(false);
      this.layoutColumns.setEnabled(false);
    }
    else if (str.equals("Custom"))
    {
      this.layoutRows.setEnabled(true);
      this.layoutColumns.setEnabled(true);
    }
  }

  private void scalingSelectionChanged()
  {
    String str = (String)this.pageScalings.getSelectedItem();
    if (str.equals("Use Original Size"))
    {
      this.pageProportionally.setEnabled(false);
      this.scaleWidth.setEnabled(false);
      this.scaleHeight.setEnabled(false);
    }
    else if (str.equals("Auto"))
    {
      this.pageProportionally.setEnabled(true);
      this.scaleWidth.setEnabled(false);
      this.scaleHeight.setEnabled(false);
    }
    else if (str.equals("Specified"))
    {
      this.pageProportionally.setEnabled(true);
      this.scaleWidth.setEnabled(true);
      this.scaleHeight.setEnabled(true);
    }
  }

  private void pageSelectionChanged()
  {
    Dimension localDimension = getPaperDimension((String)this.paperSizes.getSelectedItem());
    if (localDimension == null)
    {
      this.paperWidth.setEnabled(true);
      this.paperHeight.setEnabled(true);
    }
    else
    {
      this.paperWidth.setEnabled(false);
      this.paperHeight.setEnabled(false);
      this.paperWidth.setValue(Integer.valueOf(localDimension.width));
      this.paperHeight.setValue(Integer.valueOf(localDimension.height));
    }
  }

  private void genertatePaperSizes()
  {
    this.papers = new ArrayList();
    this.paperDimensions = new ArrayList();
    this.papers.add(Messages.getMessage("PdfViewerNUPComboBoxOption.Letter"));
    this.papers.add(Messages.getMessage("PdfViewerNUPComboBoxOption.Legal"));
    this.papers.add("11x17");
    this.papers.add(Messages.getMessage("PdfViewerNUPComboBoxOption.Ledger"));
    this.papers.add("A2");
    this.papers.add("A3");
    this.papers.add("A4");
    this.papers.add("A5");
    this.papers.add("B3");
    this.papers.add("B4");
    this.papers.add("B5");
    this.papers.add(Messages.getMessage("PdfViewerNUPComboBoxOption.Folio"));
    this.papers.add(Messages.getMessage("PdfViewerNUPComboBoxOption.Status"));
    this.papers.add(Messages.getMessage("PdfViewerNUPComboBoxOption.Note"));
    this.papers.add(Messages.getMessage("PdfViewerNUPComboBoxOption.Custom"));
    this.paperDimensions.add(new Dimension(612, 792));
    this.paperDimensions.add(new Dimension(612, 1008));
    this.paperDimensions.add(new Dimension(792, 1224));
    this.paperDimensions.add(new Dimension(1224, 792));
    this.paperDimensions.add(new Dimension(1190, 1684));
    this.paperDimensions.add(new Dimension(842, 1190));
    this.paperDimensions.add(new Dimension(595, 842));
    this.paperDimensions.add(new Dimension(421, 595));
    this.paperDimensions.add(new Dimension(1002, 1418));
    this.paperDimensions.add(new Dimension(709, 1002));
    this.paperDimensions.add(new Dimension(501, 709));
    this.paperDimensions.add(new Dimension(612, 936));
    this.paperDimensions.add(new Dimension(396, 612));
    this.paperDimensions.add(new Dimension(540, 720));
  }

  private String[] getPaperSizes()
  {
    return (String[])this.papers.toArray(new String[this.papers.size()]);
  }

  private Dimension getPaperDimension(String paramString)
  {
    if (paramString.equals("Custom"))
      return null;
    return (Dimension)this.paperDimensions.get(this.papers.indexOf(paramString));
  }

  public final Dimension getPreferredSize()
  {
    return new Dimension(620, 680);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.gui.popups.ExtractPDFPagesNup
 * JD-Core Version:    0.6.2
 */