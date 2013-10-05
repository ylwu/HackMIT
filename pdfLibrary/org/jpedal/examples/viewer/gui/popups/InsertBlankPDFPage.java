package org.jpedal.examples.viewer.gui.popups;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import org.jpedal.examples.viewer.Viewer;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.Messages;

public class InsertBlankPDFPage extends Save
{
  ButtonGroup buttonGroup1 = new ButtonGroup();
  JToggleButton jToggleButton3 = new JToggleButton();
  JToggleButton jToggleButton2 = new JToggleButton();
  JRadioButton addToEnd = new JRadioButton();
  JRadioButton addBeforePage = new JRadioButton();

  public InsertBlankPDFPage(String paramString, int paramInt1, int paramInt2)
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

  public final int getInsertBefore()
  {
    int i = -1;
    if (this.addBeforePage.isSelected())
    {
      try
      {
        i = Integer.parseInt(this.startPage.getText());
      }
      catch (Exception localException)
      {
        LogWriter.writeLog(Messages.getMessage("PdfViewerError.Exception") + ' ' + localException + ' ' + Messages.getMessage("PdfViewerError.ExportError"));
        if (Viewer.showMessages)
          JOptionPane.showMessageDialog(this, Messages.getMessage("PdfViewerError.InvalidSyntax"));
      }
      if ((i < 1) && (Viewer.showMessages))
        JOptionPane.showMessageDialog(this, Messages.getMessage("PdfViewerError.NegativePageValue"));
      if (i > this.end_page)
      {
        if (Viewer.showMessages)
          JOptionPane.showMessageDialog(this, Messages.getMessage("PdfViewerText.Page") + ' ' + i + ' ' + Messages.getMessage("PdfViewerError.OutOfBounds") + ' ' + Messages.getMessage("PdfViewerText.PageCount") + ' ' + this.end_page);
        i = -1;
      }
    }
    else
    {
      i = -2;
    }
    return i;
  }

  private void jbInit()
    throws Exception
  {
    this.pageRangeLabel.setText(Messages.getMessage("PdfViewerTitle.Location"));
    this.pageRangeLabel.setBounds(new Rectangle(13, 13, 199, 26));
    this.addToEnd.setText(Messages.getMessage("PdfViewerTitle.AddPageToEnd"));
    this.addToEnd.setBounds(new Rectangle(23, 42, 400, 22));
    this.addToEnd.setSelected(true);
    this.addBeforePage.setText(Messages.getMessage("PdfViewerTitle.InsertBeforePage"));
    this.addBeforePage.setBounds(new Rectangle(23, 70, 150, 22));
    this.startPage.setBounds(new Rectangle(175, 70, 75, 22));
    this.startPage.setText("");
    this.startPage.addKeyListener(new KeyListener()
    {
      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
      }

      public void keyReleased(KeyEvent paramAnonymousKeyEvent)
      {
        if (InsertBlankPDFPage.this.startPage.getText().isEmpty())
          InsertBlankPDFPage.this.addToEnd.setSelected(true);
        else
          InsertBlankPDFPage.this.addBeforePage.setSelected(true);
      }

      public void keyTyped(KeyEvent paramAnonymousKeyEvent)
      {
      }
    });
    add(this.changeButton, null);
    add(this.pageRangeLabel, null);
    add(this.addToEnd, null);
    add(this.addBeforePage, null);
    add(this.startPage, null);
    add(this.jToggleButton2, null);
    add(this.jToggleButton3, null);
    this.buttonGroup1.add(this.addToEnd);
    this.buttonGroup1.add(this.addBeforePage);
  }

  public final Dimension getPreferredSize()
  {
    return new Dimension(350, 180);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.gui.popups.InsertBlankPDFPage
 * JD-Core Version:    0.6.2
 */