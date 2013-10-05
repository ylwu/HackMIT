package org.jpedal.examples.viewer.gui.popups;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

public class EncryptPDFDocument extends Save
{
  JToggleButton jToggleButton3 = new JToggleButton();
  JToggleButton jToggleButton2 = new JToggleButton();
  JCheckBox userPasswordCheck = new JCheckBox("Password required to open document");
  JCheckBox masterPasswordCheck = new JCheckBox("Password required to chnge permissions and passwords");
  JCheckBox printing = new JCheckBox("No Printing");
  JCheckBox modifyDocument = new JCheckBox("No modifying the document");
  JCheckBox contentExtract = new JCheckBox("No content copying or extraction");
  JCheckBox modifyAnnotations = new JCheckBox("No modifying annotations");
  JCheckBox formFillIn = new JCheckBox("No form fields fill-in");
  JTextField userPasswordBox = new JTextField();
  JTextField masterPasswordBox = new JTextField();
  final String[] securityItems = { "128-bit RC4 (Acrobat 5.0 and up)", "40-bit RC4 (Acrobat 3.x, 4.x)" };
  JComboBox encryptionLevel = new JComboBox(this.securityItems);

  public EncryptPDFDocument(String paramString, int paramInt1, int paramInt2)
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
    this.pageRangeLabel.setText("Password");
    this.pageRangeLabel.setBounds(new Rectangle(13, 13, 199, 26));
    this.userPasswordCheck.setBounds(new Rectangle(23, 40, 300, 22));
    this.userPasswordCheck.addMouseListener(new MouseListener()
    {
      public void mouseClicked(MouseEvent paramAnonymousMouseEvent)
      {
        EncryptPDFDocument.this.userPasswordBox.setEditable(EncryptPDFDocument.this.userPasswordCheck.isSelected());
      }

      public void mouseEntered(MouseEvent paramAnonymousMouseEvent)
      {
      }

      public void mouseExited(MouseEvent paramAnonymousMouseEvent)
      {
      }

      public void mousePressed(MouseEvent paramAnonymousMouseEvent)
      {
      }

      public void mouseReleased(MouseEvent paramAnonymousMouseEvent)
      {
      }
    });
    JLabel localJLabel1 = new JLabel("User password:");
    localJLabel1.setBounds(new Rectangle(50, 70, 100, 22));
    this.userPasswordBox.setBounds(new Rectangle(180, 70, 150, 22));
    this.userPasswordBox.setEditable(false);
    this.masterPasswordCheck.setBounds(new Rectangle(23, 100, 440, 22));
    this.masterPasswordCheck.addMouseListener(new MouseListener()
    {
      public void mouseClicked(MouseEvent paramAnonymousMouseEvent)
      {
        EncryptPDFDocument.this.masterPasswordBox.setEditable(EncryptPDFDocument.this.masterPasswordCheck.isSelected());
      }

      public void mouseEntered(MouseEvent paramAnonymousMouseEvent)
      {
      }

      public void mouseExited(MouseEvent paramAnonymousMouseEvent)
      {
      }

      public void mousePressed(MouseEvent paramAnonymousMouseEvent)
      {
      }

      public void mouseReleased(MouseEvent paramAnonymousMouseEvent)
      {
      }
    });
    JLabel localJLabel2 = new JLabel("Master password:");
    localJLabel2.setBounds(new Rectangle(50, 130, 120, 22));
    this.masterPasswordBox.setBounds(new Rectangle(180, 130, 150, 22));
    this.masterPasswordBox.setEditable(false);
    JLabel localJLabel3 = new JLabel("Permissions");
    localJLabel3.setFont(new Font("Dialog", 1, 14));
    localJLabel3.setDisplayedMnemonic('0');
    localJLabel3.setBounds(new Rectangle(13, 180, 199, 26));
    JLabel localJLabel4 = new JLabel("Encryption Level:");
    localJLabel4.setBounds(new Rectangle(23, 210, 125, 22));
    this.encryptionLevel.setBounds(new Rectangle(150, 210, 250, 22));
    this.printing.setBounds(new Rectangle(23, 250, 200, 22));
    this.modifyDocument.setBounds(new Rectangle(23, 280, 200, 22));
    this.contentExtract.setBounds(new Rectangle(23, 310, 220, 22));
    this.modifyAnnotations.setBounds(new Rectangle(23, 340, 200, 22));
    this.formFillIn.setBounds(new Rectangle(23, 370, 200, 22));
    add(this.changeButton, null);
    add(this.pageRangeLabel, null);
    add(this.userPasswordCheck, null);
    add(this.masterPasswordCheck, null);
    add(localJLabel1);
    add(this.userPasswordBox, null);
    add(localJLabel2);
    add(this.masterPasswordBox, null);
    add(localJLabel3, null);
    add(localJLabel4, null);
    add(this.encryptionLevel, null);
    add(this.printing);
    add(this.modifyDocument);
    add(this.contentExtract);
    add(this.modifyAnnotations);
    add(this.formFillIn);
    add(this.jToggleButton2, null);
    add(this.jToggleButton3, null);
  }

  public final Dimension getPreferredSize()
  {
    return new Dimension(420, 400);
  }

  public String getPermissions()
  {
    String str = "";
    if (this.printing.isSelected())
      str = str + "1";
    else
      str = str + "0";
    if (this.modifyDocument.isSelected())
      str = str + "1";
    else
      str = str + "0";
    if (this.contentExtract.isSelected())
      str = str + "1";
    else
      str = str + "0";
    if (this.modifyAnnotations.isSelected())
      str = str + "1";
    else
      str = str + "0";
    if (this.formFillIn.isSelected())
      str = str + "1";
    else
      str = str + "0";
    return str;
  }

  public int getEncryptionLevel()
  {
    return this.encryptionLevel.getSelectedIndex();
  }

  public String getMasterPassword()
  {
    return this.masterPasswordBox.getText();
  }

  public String getUserPassword()
  {
    return this.userPasswordBox.getText();
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.gui.popups.EncryptPDFDocument
 * JD-Core Version:    0.6.2
 */