package org.jpedal.objects.acroforms.gui.certificates;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class General extends JPanel
{
  private JLabel issuedToBox;
  private JLabel jLabel1;
  private JLabel jLabel3;
  private JLabel jLabel4;
  private JLabel validFromBox;
  private JLabel validToBox;

  public General()
  {
    initComponents();
  }

  public void setValues(String paramString1, String paramString2, String paramString3)
  {
    this.issuedToBox.setText(paramString1);
    this.validFromBox.setText(paramString2);
    this.validToBox.setText(paramString3);
  }

  private void initComponents()
  {
    this.jLabel1 = new JLabel();
    this.validToBox = new JLabel();
    this.jLabel3 = new JLabel();
    this.jLabel4 = new JLabel();
    this.issuedToBox = new JLabel();
    this.validFromBox = new JLabel();
    setLayout(null);
    this.jLabel1.setText("Valid to:");
    add(this.jLabel1);
    this.jLabel1.setBounds(10, 70, 80, 20);
    add(this.validToBox);
    this.validToBox.setBounds(80, 70, 310, 20);
    this.jLabel3.setText("Valid from:");
    add(this.jLabel3);
    this.jLabel3.setBounds(10, 40, 80, 20);
    this.jLabel4.setText("Issued to:");
    add(this.jLabel4);
    this.jLabel4.setBounds(10, 10, 80, 20);
    add(this.issuedToBox);
    this.issuedToBox.setBounds(80, 10, 310, 20);
    add(this.validFromBox);
    this.validFromBox.setBounds(80, 40, 310, 20);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.acroforms.gui.certificates.General
 * JD-Core Version:    0.6.2
 */