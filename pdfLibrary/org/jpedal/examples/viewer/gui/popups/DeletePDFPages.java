package org.jpedal.examples.viewer.gui.popups;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.print.attribute.standard.PageRanges;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import org.jpedal.examples.viewer.Viewer;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.Messages;

public class DeletePDFPages extends Save
{
  private static final long serialVersionUID = 7720446319401470639L;
  ButtonGroup buttonGroup1 = new ButtonGroup();
  JToggleButton jToggleButton3 = new JToggleButton();
  JToggleButton jToggleButton2 = new JToggleButton();
  JRadioButton printAll = new JRadioButton();
  JRadioButton printCurrent = new JRadioButton();
  JRadioButton printPages = new JRadioButton();
  JTextField pagesBox = new JTextField();

  public DeletePDFPages(String paramString, int paramInt1, int paramInt2)
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

  public final int[] getDeletedPages()
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

  private void jbInit()
    throws Exception
  {
    this.pageRangeLabel.setText(Messages.getMessage("PdfViewerPageRange.text"));
    this.pageRangeLabel.setBounds(new Rectangle(13, 13, 199, 26));
    this.printCurrent.setText(Messages.getMessage("PdfViewerRadioButton.CurrentPage"));
    this.printCurrent.setBounds(new Rectangle(23, 62, 100, 22));
    this.printCurrent.setSelected(true);
    this.printPages.setText(Messages.getMessage("PdfViewerRadioButton.Pages"));
    this.printPages.setBounds(new Rectangle(23, 84, 70, 22));
    this.pagesBox.setBounds(new Rectangle(95, 84, 200, 22));
    this.pagesBox.addKeyListener(new KeyListener()
    {
      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
        DeletePDFPages.this.jToggleButton2.setSelected(true);
      }

      public void keyReleased(KeyEvent paramAnonymousKeyEvent)
      {
        if (DeletePDFPages.this.pagesBox.getText().isEmpty())
          DeletePDFPages.this.printCurrent.setSelected(true);
        else
          DeletePDFPages.this.printPages.setSelected(true);
      }

      public void keyTyped(KeyEvent paramAnonymousKeyEvent)
      {
      }
    });
    JTextArea localJTextArea = new JTextArea(Messages.getMessage("PdfViewerMessage.PageNumberOrRange") + '\n' + Messages.getMessage("PdfViewerMessage.PageRangeExample"));
    localJTextArea.setBounds(new Rectangle(15, 115, 400, 40));
    localJTextArea.setOpaque(false);
    this.optionsForFilesLabel.setBounds(new Rectangle(13, 168, 199, 26));
    add(this.printAll, null);
    add(this.printCurrent, null);
    add(this.printPages, null);
    add(this.pagesBox, null);
    add(localJTextArea, null);
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
    return new Dimension(370, 180);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.gui.popups.DeletePDFPages
 * JD-Core Version:    0.6.2
 */