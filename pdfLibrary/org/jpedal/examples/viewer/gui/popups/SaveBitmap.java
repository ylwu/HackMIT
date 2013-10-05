package org.jpedal.examples.viewer.gui.popups;

import java.awt.Dimension;
import java.awt.Rectangle;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import org.jpedal.utils.Messages;

public class SaveBitmap extends Save
{
  JLabel OutputLabel = new JLabel();
  ButtonGroup buttonGroup1 = new ButtonGroup();
  JToggleButton jToggleButton3 = new JToggleButton();
  JToggleButton jToggleButton2 = new JToggleButton();
  JRadioButton isPNG = new JRadioButton();
  JRadioButton isTiff = new JRadioButton();
  JRadioButton isJPEG = new JRadioButton();

  public SaveBitmap(String paramString, int paramInt1, int paramInt2)
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

  public final Dimension getPreferredSize()
  {
    return new Dimension(490, 280);
  }

  private void jbInit()
    throws Exception
  {
    this.scalingLabel.setBounds(new Rectangle(13, 12, 400, 19));
    this.scaling.setBounds(new Rectangle(400, 12, 69, 23));
    this.rootFilesLabel.setBounds(new Rectangle(13, 55, 400, 26));
    this.rootDir.setBounds(new Rectangle(23, 82, 232, 23));
    this.changeButton.setBounds(new Rectangle(272, 82, 101, 23));
    this.OutputLabel.setText(Messages.getMessage("PdfViewerMessage.OutputType"));
    this.OutputLabel.setBounds(new Rectangle(23, 216, 300, 24));
    this.isTiff.setText("Tiff");
    this.isTiff.setBounds(new Rectangle(180, 218, 50, 19));
    this.isJPEG.setBounds(new Rectangle(240, 217, 67, 19));
    this.isJPEG.setSelected(true);
    this.isJPEG.setText("JPEG");
    this.isPNG.setBounds(new Rectangle(305, 217, 62, 19));
    this.isPNG.setText("PNG");
    this.isPNG.setName("radioPNG");
    this.optionsForFilesLabel.setBounds(new Rectangle(13, 176, 600, 26));
    this.startPage.setBounds(new Rectangle(125, 142, 75, 22));
    this.pageRangeLabel.setBounds(new Rectangle(13, 113, 400, 26));
    this.startLabel.setBounds(new Rectangle(23, 142, 100, 22));
    this.endLabel.setBounds(new Rectangle(220, 142, 75, 22));
    this.endPage.setBounds(new Rectangle(285, 142, 75, 22));
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
    add(this.OutputLabel, null);
    add(this.jToggleButton2, null);
    add(this.jToggleButton3, null);
    add(this.isTiff, null);
    add(this.isJPEG, null);
    add(this.isPNG, null);
    this.buttonGroup1.add(this.isTiff);
    this.buttonGroup1.add(this.isJPEG);
    this.buttonGroup1.add(this.isPNG);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.gui.popups.SaveBitmap
 * JD-Core Version:    0.6.2
 */