package org.jpedal.examples.viewer.gui.popups;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.jpedal.examples.viewer.Viewer;
import org.jpedal.examples.viewer.utils.FileFilterer;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.Messages;

public class Save extends JComponent
{
  protected JTextField startPage = new JTextField();
  protected JTextField endPage = new JTextField();
  protected JTextField rootDir = new JTextField();
  protected Object[] scales = { "10", "25", "50", "75", "100" };
  protected JComboBox scaling = new JComboBox(this.scales);
  protected JLabel scalingLabel = new JLabel();
  protected JLabel rootFilesLabel = new JLabel();
  protected JButton changeButton = new JButton();
  protected JLabel endLabel = new JLabel();
  protected JLabel startLabel = new JLabel();
  protected JLabel pageRangeLabel = new JLabel();
  protected String root_dir;
  protected int end_page;
  protected int currentPage;
  protected JLabel optionsForFilesLabel = new JLabel();

  public Save(final String paramString, int paramInt1, int paramInt2)
  {
    this.currentPage = paramInt2;
    this.root_dir = paramString;
    this.end_page = paramInt1;
    this.scalingLabel.setFont(new Font("Dialog", 1, 14));
    this.scalingLabel.setText(Messages.getMessage("PdfViewerOption.Scaling") + '\n');
    this.scaling.setSelectedItem("100");
    this.scaling.setName("exportScaling");
    this.rootFilesLabel.setFont(new Font("Dialog", 1, 14));
    this.rootFilesLabel.setDisplayedMnemonic('0');
    this.rootFilesLabel.setText(Messages.getMessage("PdfViewerOption.RootDir"));
    this.rootDir.setText(paramString);
    this.rootDir.setName("extRootDir");
    this.changeButton.setText(Messages.getMessage("PdfViewerOption.Browse"));
    this.changeButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        JFileChooser localJFileChooser = new JFileChooser(paramString);
        localJFileChooser.setFileSelectionMode(0);
        String[] arrayOfString = { "png", "tif", "tiff", "jpg", "jpeg" };
        localJFileChooser.addChoosableFileFilter(new FileFilterer(arrayOfString, "Images (Tiff, Jpeg,Png)"));
        localJFileChooser.setFileSelectionMode(1);
        int i = localJFileChooser.showOpenDialog(null);
        File localFile = localJFileChooser.getSelectedFile();
        if ((localFile != null) && (i == 0))
          Save.this.rootDir.setText(localFile.getAbsolutePath());
      }
    });
    this.optionsForFilesLabel.setText(Messages.getMessage("PdfViewerOption.Output"));
    this.optionsForFilesLabel.setFont(new Font("Dialog", 1, 14));
    this.optionsForFilesLabel.setDisplayedMnemonic('0');
    this.pageRangeLabel.setText(Messages.getMessage("PdfViewerOption.PageRange"));
    this.pageRangeLabel.setFont(new Font("Dialog", 1, 14));
    this.pageRangeLabel.setDisplayedMnemonic('0');
    this.startLabel.setText(Messages.getMessage("PdfViewerOption.StartPage"));
    this.endLabel.setText(Messages.getMessage("PdfViewerOption.EndPage"));
    this.startPage.setText("1");
    this.endPage.setText(String.valueOf(paramInt1));
  }

  public final int getScaling()
  {
    return Integer.parseInt((String)this.scaling.getSelectedItem());
  }

  public int display(Component paramComponent, String paramString)
  {
    setSize(400, 200);
    JPanel localJPanel = new JPanel();
    localJPanel.setLayout(new BorderLayout());
    localJPanel.add(this, "Center");
    localJPanel.setSize(400, 200);
    Object[] arrayOfObject = { Messages.getMessage("PdfMessage.Ok"), Messages.getMessage("PdfMessage.Cancel") };
    if (Viewer.showMessages)
      return JOptionPane.showOptionDialog(paramComponent, localJPanel, paramString, -1, -1, null, arrayOfObject, arrayOfObject[0]);
    return 0;
  }

  public final int getStartPage()
  {
    int i = -1;
    try
    {
      i = Integer.parseInt(this.startPage.getText());
    }
    catch (Exception localException)
    {
      LogWriter.writeLog("Exception " + localException + " in exporting");
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
    return i;
  }

  public final String getRootDir()
  {
    return this.rootDir.getText();
  }

  public final int getEndPage()
  {
    int i = -1;
    try
    {
      i = Integer.parseInt(this.endPage.getText());
    }
    catch (Exception localException)
    {
      LogWriter.writeLog("Exception " + localException + " in exporting");
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
    return i;
  }

  public final Dimension getSize()
  {
    return getPreferredSize();
  }

  public Dimension getPreferredSize()
  {
    return new Dimension(400, 330);
  }

  public final Dimension getMinimumSize()
  {
    return getPreferredSize();
  }

  public final Dimension getMaximumSize()
  {
    return getPreferredSize();
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.gui.popups.Save
 * JD-Core Version:    0.6.2
 */