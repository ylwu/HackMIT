package org.jpedal.examples.viewer.gui.popups;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import org.jpedal.utils.Messages;

public class CropPDFPages extends Save
{
  ButtonGroup buttonGroup1 = new ButtonGroup();
  JToggleButton jToggleButton3 = new JToggleButton();
  JToggleButton jToggleButton2 = new JToggleButton();
  JSpinner bottomMargin = new JSpinner(new SpinnerNumberModel(0.0D, 0.0D, 1000.0D, 1.0D));
  JSpinner topMargin = new JSpinner(new SpinnerNumberModel(0.0D, 0.0D, 1000.0D, 1.0D));
  JSpinner leftMargin = new JSpinner(new SpinnerNumberModel(0.0D, 0.0D, 1000.0D, 1.0D));
  JSpinner rightMargin = new JSpinner(new SpinnerNumberModel(0.0D, 0.0D, 1000.0D, 1.0D));
  JCheckBox applyToCurrent = new JCheckBox();
  JRadioButton printAll = new JRadioButton();
  JRadioButton printCurrent = new JRadioButton();
  JRadioButton printPages = new JRadioButton();
  JTextField pagesBox = new JTextField();

  public CropPDFPages(String paramString, int paramInt1, int paramInt2)
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

  private void jbInit()
    throws Exception
  {
    JLabel localJLabel1 = new JLabel(Messages.getMessage("PdfViewerLabel.CropMargins"));
    localJLabel1.setFont(new Font("Dialog", 1, 14));
    localJLabel1.setDisplayedMnemonic('0');
    localJLabel1.setBounds(new Rectangle(13, 13, 220, 26));
    JLabel localJLabel2 = new JLabel(Messages.getMessage("PdfViewerLabel.Top"));
    localJLabel2.setBounds(140, 50, 70, 15);
    this.topMargin.setBounds(200, 45, 60, 23);
    JLabel localJLabel3 = new JLabel(Messages.getMessage("PdfViewerLabel.Left"));
    localJLabel3.setBounds(25, 100, 50, 15);
    this.leftMargin.setBounds(70, 95, 60, 23);
    JLabel localJLabel4 = new JLabel(Messages.getMessage("PdfViewerLabel.Right"));
    localJLabel4.setBounds(295, 100, 70, 15);
    this.rightMargin.setBounds(340, 95, 60, 23);
    JLabel localJLabel5 = new JLabel(Messages.getMessage("PdfViewerLabel.Bottom"));
    localJLabel5.setBounds(140, 150, 110, 15);
    this.bottomMargin.setBounds(200, 145, 60, 23);
    this.applyToCurrent.setSelected(true);
    this.applyToCurrent.setText(Messages.getMessage("PdfViewerCheckBox.ApplyToPriorCroppingRectangle"));
    this.applyToCurrent.setBounds(5, 190, 305, 15);
    JButton localJButton = new JButton(Messages.getMessage("PdfViewerButton.Set2Zero"));
    localJButton.setBounds(310, 185, 130, 23);
    localJButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        CropPDFPages.this.leftMargin.setValue(Integer.valueOf(0));
        CropPDFPages.this.rightMargin.setValue(Integer.valueOf(0));
        CropPDFPages.this.topMargin.setValue(Integer.valueOf(0));
        CropPDFPages.this.bottomMargin.setValue(Integer.valueOf(0));
      }
    });
    this.pageRangeLabel.setText(Messages.getMessage("PdfViewerPageRange.text"));
    this.pageRangeLabel.setBounds(new Rectangle(13, 220, 199, 26));
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
        if (CropPDFPages.this.pagesBox.getText().isEmpty())
          CropPDFPages.this.printCurrent.setSelected(true);
        else
          CropPDFPages.this.printPages.setSelected(true);
      }

      public void keyTyped(KeyEvent paramAnonymousKeyEvent)
      {
      }
    });
    JTextArea localJTextArea = new JTextArea(Messages.getMessage("PdfViewerMessage.PageNumberOrRange") + '\n' + Messages.getMessage("PdfViewerMessage.PageRangeExample"));
    localJTextArea.setBounds(new Rectangle(23, 325, 400, 40));
    localJTextArea.setOpaque(false);
    add(localJLabel2);
    add(this.bottomMargin);
    add(localJLabel3);
    add(this.topMargin);
    add(this.leftMargin);
    add(this.rightMargin);
    add(localJLabel5);
    add(localJLabel4);
    add(this.applyToCurrent);
    add(localJButton);
    add(this.printAll, null);
    add(this.printCurrent, null);
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

  public final Dimension getPreferredSize()
  {
    return new Dimension(440, 400);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.gui.popups.CropPDFPages
 * JD-Core Version:    0.6.2
 */