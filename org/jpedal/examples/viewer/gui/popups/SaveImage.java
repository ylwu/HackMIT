package org.jpedal.examples.viewer.gui.popups;

import java.awt.Dimension;
import java.awt.Rectangle;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import org.jpedal.utils.Messages;

public class SaveImage extends Save
{
  private ButtonGroup buttonGroup1 = new ButtonGroup();
  private JToggleButton jToggleButton2 = new JToggleButton();
  private JToggleButton jToggleButton3 = new JToggleButton();
  private JLabel OutputLabel = new JLabel();
  private JRadioButton isPNG = new JRadioButton();
  private JRadioButton isTiff = new JRadioButton();
  private JRadioButton isJPEG = new JRadioButton();
  private JRadioButton isHires = new JRadioButton();
  private JRadioButton isNormal = new JRadioButton();
  private JRadioButton isDownsampled = new JRadioButton();
  private ButtonGroup buttonGroup2 = new ButtonGroup();

  public SaveImage(String paramString, int paramInt1, int paramInt2)
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

  public final String getPrefix()
  {
    String str = "png";
    if (this.isTiff.isSelected())
      str = "tif";
    if (this.isJPEG.isSelected())
      str = "jpg";
    return str;
  }

  public final int getImageType()
  {
    int i = 32;
    if (this.isNormal.isSelected())
      i = 2;
    if (this.isDownsampled.isSelected())
      i = 4;
    return i;
  }

  private void jbInit()
    throws Exception
  {
    this.rootFilesLabel.setBounds(new Rectangle(13, 12, 400, 26));
    this.rootDir.setBounds(new Rectangle(23, 39, 232, 23));
    this.changeButton.setBounds(new Rectangle(272, 39, 101, 23));
    this.startPage.setBounds(new Rectangle(125, 99, 75, 22));
    this.pageRangeLabel.setBounds(new Rectangle(13, 70, 400, 26));
    this.startLabel.setBounds(new Rectangle(23, 100, 100, 22));
    this.endLabel.setBounds(new Rectangle(220, 99, 75, 22));
    this.endPage.setBounds(new Rectangle(285, 99, 75, 22));
    this.optionsForFilesLabel.setBounds(new Rectangle(13, 133, 600, 26));
    this.OutputLabel.setText(Messages.getMessage("PdfViewerMessage.OutputType"));
    this.OutputLabel.setBounds(new Rectangle(23, 173, 900, 24));
    this.isTiff.setText("Tiff");
    this.isTiff.setBounds(new Rectangle(180, 175, 50, 19));
    this.isJPEG.setBounds(new Rectangle(290, 174, 67, 19));
    this.isJPEG.setSelected(true);
    this.isJPEG.setText("JPEG");
    this.isPNG.setBounds(new Rectangle(360, 174, 62, 19));
    this.isPNG.setText("PNG");
    this.isHires.setText(Messages.getMessage("PdfViewerOption.Hires"));
    this.isHires.setBounds(new Rectangle(180, 200, 112, 19));
    this.isHires.setSelected(true);
    this.isNormal.setBounds(new Rectangle(290, 200, 73, 19));
    this.isNormal.setText(Messages.getMessage("PdfViewerOption.Normal"));
    this.isDownsampled.setBounds(new Rectangle(360, 200, 200, 19));
    this.isDownsampled.setText(Messages.getMessage("PdfViewerOption.Downsampled"));
    add(this.startPage, null);
    add(this.endPage, null);
    add(this.rootDir, null);
    add(this.scaling, null);
    add(this.scalingLabel, null);
    add(this.rootFilesLabel, null);
    add(this.changeButton, null);
    add(this.endLabel, null);
    add(this.startLabel, null);
    add(this.pageRangeLabel, null);
    add(this.optionsForFilesLabel, null);
    add(this.jToggleButton2, null);
    add(this.jToggleButton3, null);
    add(this.OutputLabel, null);
    add(this.isTiff, null);
    add(this.isJPEG, null);
    add(this.isPNG, null);
    this.buttonGroup1.add(this.isTiff);
    this.buttonGroup1.add(this.isJPEG);
    this.buttonGroup1.add(this.isPNG);
    add(this.isHires, null);
    add(this.isNormal, null);
    add(this.isDownsampled, null);
    this.buttonGroup2.add(this.isHires);
    this.buttonGroup2.add(this.isNormal);
    this.buttonGroup2.add(this.isDownsampled);
  }

  public final Dimension getPreferredSize()
  {
    return new Dimension(500, 250);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.gui.popups.SaveImage
 * JD-Core Version:    0.6.2
 */