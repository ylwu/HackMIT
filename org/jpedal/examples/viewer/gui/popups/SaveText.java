package org.jpedal.examples.viewer.gui.popups;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import org.jpedal.utils.Messages;

public class SaveText extends Save
{
  JLabel outputFileTypeLabel = new JLabel();
  JLabel outputFormat = new JLabel();
  ButtonGroup buttonGroup1 = new ButtonGroup();
  ButtonGroup buttonGroup2 = new ButtonGroup();
  JToggleButton jToggleButton3 = new JToggleButton();
  JToggleButton jToggleButton2 = new JToggleButton();
  JRadioButton isPlainText = new JRadioButton();
  JRadioButton isXML = new JRadioButton();
  JRadioButton isWordlist = new JRadioButton();
  JRadioButton isTable = new JRadioButton();
  JRadioButton isRectangle = new JRadioButton();

  public SaveText(String paramString, int paramInt1, int paramInt2)
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

  public boolean isXMLExtaction()
  {
    return this.isXML.isSelected();
  }

  public int getTextType()
  {
    int i = 1;
    if (this.isWordlist.isSelected())
      i = 2;
    if (this.isTable.isSelected())
      i = 3;
    return i;
  }

  public final Dimension getPreferredSize()
  {
    return new Dimension(490, 280);
  }

  private void jbInit()
    throws Exception
  {
    this.rootFilesLabel.setBounds(new Rectangle(13, 13, 400, 26));
    this.rootDir.setBounds(new Rectangle(23, 40, 232, 23));
    this.changeButton.setBounds(new Rectangle(272, 39, 101, 23));
    this.pageRangeLabel.setBounds(new Rectangle(13, 71, 400, 26));
    this.startLabel.setBounds(new Rectangle(23, 100, 150, 22));
    this.startPage.setBounds(new Rectangle(150, 100, 75, 22));
    this.endLabel.setBounds(new Rectangle(260, 100, 75, 22));
    this.endPage.setBounds(new Rectangle(320, 100, 75, 22));
    this.optionsForFilesLabel.setBounds(new Rectangle(13, 134, 400, 26));
    this.outputFileTypeLabel.setText(Messages.getMessage("PdfViewerMessage.OutputType"));
    this.outputFileTypeLabel.setBounds(new Rectangle(23, 174, 164, 19));
    this.isPlainText.setText(Messages.getMessage("PdfViewerOption.PlainText"));
    this.isPlainText.setBounds(new Rectangle(180, 174, 100, 19));
    this.isXML.setBounds(new Rectangle(280, 174, 95, 19));
    this.isXML.setSelected(true);
    this.isXML.setText(Messages.getMessage("PdfViewerOption.XML"));
    this.outputFormat.setText(Messages.getMessage("PdfViewerMessage.OutputFormat"));
    this.outputFormat.setBounds(new Rectangle(23, 214, 164, 19));
    this.isRectangle.setText(Messages.getMessage("PdfViewerOption.Rectangle"));
    this.isRectangle.setBounds(new Rectangle(180, 214, 75, 19));
    this.isRectangle.setSelected(true);
    this.isRectangle.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        SaveText.this.isPlainText.setText(Messages.getMessage("PdfViewerOption.PlainText"));
      }
    });
    this.isWordlist.setText(Messages.getMessage("PdfViewerOption.Wordlist"));
    this.isWordlist.setBounds(new Rectangle(280, 214, 100, 19));
    this.isWordlist.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        SaveText.this.isPlainText.setText(Messages.getMessage("PdfViewerOption.PlainText"));
      }
    });
    this.isTable.setText(Messages.getMessage("PdfViewerOption.Table"));
    this.isTable.setBounds(new Rectangle(225, 214, 75, 19));
    this.isTable.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        SaveText.this.isPlainText.setText(Messages.getMessage("PdfViewerOption.CSV"));
      }
    });
    add(this.startPage, null);
    add(this.endPage, null);
    add(this.rootDir, null);
    add(this.rootFilesLabel, null);
    add(this.changeButton, null);
    add(this.endLabel, null);
    add(this.startLabel, null);
    add(this.pageRangeLabel, null);
    add(this.optionsForFilesLabel, null);
    add(this.outputFileTypeLabel, null);
    add(this.jToggleButton2, null);
    add(this.jToggleButton3, null);
    add(this.isPlainText, null);
    add(this.isXML, null);
    add(this.outputFormat, null);
    add(this.isRectangle, null);
    add(this.isWordlist, null);
    this.buttonGroup1.add(this.isXML);
    this.buttonGroup1.add(this.isPlainText);
    this.buttonGroup2.add(this.isRectangle);
    this.buttonGroup2.add(this.isTable);
    this.buttonGroup2.add(this.isWordlist);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.gui.popups.SaveText
 * JD-Core Version:    0.6.2
 */