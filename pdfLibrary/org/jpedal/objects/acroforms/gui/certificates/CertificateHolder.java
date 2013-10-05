package org.jpedal.objects.acroforms.gui.certificates;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class CertificateHolder extends JPanel
{
  private Details detailsTab;
  private General generalTab;
  private JDialog frame;
  private JButton jButton1;
  private JTabbedPane jTabbedPane1;

  public void setValues(String paramString1, int paramInt, String paramString2, String paramString3, String paramString4, BigInteger paramBigInteger, String paramString5, String paramString6, String paramString7, String paramString8, String paramString9, String paramString10, String paramString11)
  {
    this.generalTab = new General();
    this.detailsTab = new Details();
    this.generalTab.setValues(paramString1, paramString5, paramString6);
    this.detailsTab.setValues(paramInt, paramString2, paramString3, paramString4, paramBigInteger, paramString5, paramString6, paramString7, paramString8, paramString9, paramString10, paramString11);
    this.jTabbedPane1.addTab("General", this.generalTab);
    this.jTabbedPane1.addTab("Details", this.detailsTab);
  }

  public CertificateHolder(JDialog paramJDialog)
  {
    initComponents();
    this.frame = paramJDialog;
  }

  private void initComponents()
  {
    this.jTabbedPane1 = new JTabbedPane();
    this.jButton1 = new JButton();
    setLayout(null);
    add(this.jTabbedPane1);
    this.jTabbedPane1.setBounds(10, 10, 420, 360);
    this.jButton1.setText("OK");
    this.jButton1.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        CertificateHolder.this.close(paramAnonymousActionEvent);
      }
    });
    add(this.jButton1);
    this.jButton1.setBounds(350, 390, 73, 23);
  }

  private void close(ActionEvent paramActionEvent)
  {
    this.frame.setVisible(false);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.acroforms.gui.certificates.CertificateHolder
 * JD-Core Version:    0.6.2
 */