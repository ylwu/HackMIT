package org.jpedal.objects.acroforms.gui;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.security.auth.x500.X500Principal;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.jpedal.objects.acroforms.gui.certificates.CertificateHolder;
import org.jpedal.objects.raw.PdfObject;
import org.jpedal.utils.LogWriter;

public class Summary extends JPanel
{
  private JDialog frame;
  private PdfObject sigObject;
  private JTextField dateBox;
  private JButton jButton1;
  private JLabel jLabel1;
  private JLabel jLabel2;
  private JLabel jLabel3;
  private JLabel jLabel4;
  private JTextField locationBox;
  private JTextField reasonBox;
  private JButton showCertificateButton;
  private JTextField signedByBox;

  public void setValues(String paramString1, String paramString2, String paramString3)
  {
    this.signedByBox.setText(paramString1);
    this.reasonBox.setText(paramString2);
    String str = this.sigObject.getTextStreamValue(29);
    StringBuilder localStringBuilder = new StringBuilder(str);
    localStringBuilder.delete(0, 2);
    localStringBuilder.insert(4, '/');
    localStringBuilder.insert(7, '/');
    localStringBuilder.insert(10, ' ');
    localStringBuilder.insert(13, ':');
    localStringBuilder.insert(16, ':');
    localStringBuilder.insert(19, ' ');
    this.dateBox.setText(localStringBuilder.toString());
    this.locationBox.setText(paramString3);
  }

  public Summary(JDialog paramJDialog, PdfObject paramPdfObject)
  {
    this.frame = paramJDialog;
    this.sigObject = paramPdfObject;
    initComponents();
  }

  private void initComponents()
  {
    this.jLabel1 = new JLabel();
    this.jLabel2 = new JLabel();
    this.jLabel3 = new JLabel();
    this.locationBox = new JTextField();
    this.showCertificateButton = new JButton();
    this.signedByBox = new JTextField();
    this.reasonBox = new JTextField();
    this.jLabel4 = new JLabel();
    this.dateBox = new JTextField();
    this.jButton1 = new JButton();
    setLayout(null);
    this.jLabel1.setText("Location:");
    add(this.jLabel1);
    this.jLabel1.setBounds(310, 70, 70, 20);
    this.jLabel2.setText("Signed by:");
    add(this.jLabel2);
    this.jLabel2.setBounds(10, 10, 70, 20);
    this.jLabel3.setText("Reason:");
    add(this.jLabel3);
    this.jLabel3.setBounds(10, 40, 70, 20);
    this.locationBox.setEditable(false);
    add(this.locationBox);
    this.locationBox.setBounds(360, 70, 170, 20);
    this.showCertificateButton.setText("Show Certificate...");
    this.showCertificateButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        Summary.this.showCertificate();
      }
    });
    add(this.showCertificateButton);
    this.showCertificateButton.setBounds(380, 10, 150, 23);
    this.signedByBox.setEditable(false);
    add(this.signedByBox);
    this.signedByBox.setBounds(70, 10, 300, 20);
    this.reasonBox.setEditable(false);
    add(this.reasonBox);
    this.reasonBox.setBounds(70, 40, 460, 20);
    this.jLabel4.setText("Date:");
    add(this.jLabel4);
    this.jLabel4.setBounds(10, 70, 70, 20);
    this.dateBox.setEditable(false);
    add(this.dateBox);
    this.dateBox.setBounds(70, 70, 230, 20);
    this.jButton1.setText("Close");
    this.jButton1.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        Summary.this.close();
      }
    });
    add(this.jButton1);
    this.jButton1.setBounds(433, 140, 90, 23);
  }

  private void close()
  {
    this.frame.setVisible(false);
  }

  private void showCertificate()
  {
    JDialog localJDialog = new JDialog((JFrame)null, "Certificate Viewer", true);
    CertificateHolder localCertificateHolder = new CertificateHolder(localJDialog);
    try
    {
      byte[] arrayOfByte = this.sigObject.getTextStreamValueAsByte(322257476);
      ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(arrayOfByte);
      CertificateFactory localCertificateFactory = CertificateFactory.getInstance("X.509");
      X509Certificate localX509Certificate = (X509Certificate)localCertificateFactory.generateCertificate(localByteArrayInputStream);
      localByteArrayInputStream.close();
      SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss z");
      Date localDate1 = localX509Certificate.getNotBefore();
      Date localDate2 = localX509Certificate.getNotAfter();
      String str1 = byteToHex(localX509Certificate.getPublicKey().getEncoded());
      String str2 = byteToHex(localX509Certificate.getEncoded());
      String str3 = byteToHex(getDigest(arrayOfByte, "SHA1"));
      String str4 = byteToHex(getDigest(arrayOfByte, "MD5"));
      String str5 = localX509Certificate.getPublicKey().toString();
      int i = str5.indexOf('\n');
      if (i != -1)
        str5 = str5.substring(0, i);
      localCertificateHolder.setValues(this.sigObject.getTextStreamValue(506543413), localX509Certificate.getVersion(), localX509Certificate.getSigAlgName(), localX509Certificate.getSubjectX500Principal().toString(), localX509Certificate.getIssuerX500Principal().toString(), localX509Certificate.getSerialNumber(), localSimpleDateFormat.format(localDate1), localSimpleDateFormat.format(localDate2), str5, str1, str2, str3, str4);
      localJDialog.getContentPane().add(localCertificateHolder);
      localJDialog.setSize(440, 450);
      localJDialog.setLocationRelativeTo(null);
      localJDialog.setVisible(true);
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception: " + localException.getMessage());
    }
  }

  private static byte[] getDigest(byte[] paramArrayOfByte, String paramString)
    throws NoSuchAlgorithmException
  {
    MessageDigest localMessageDigest = MessageDigest.getInstance(paramString);
    localMessageDigest.update(paramArrayOfByte);
    byte[] arrayOfByte = localMessageDigest.digest();
    return arrayOfByte;
  }

  private static String byteToHex(byte[] paramArrayOfByte)
  {
    String str1 = "";
    for (int k : paramArrayOfByte)
    {
      String str2 = Integer.toHexString(k);
      if (str2.startsWith("ffffff"))
        str2 = str2.substring(6, str2.length());
      else if (str2.length() == 1)
        str2 = '0' + str2;
      str2 = str2.toUpperCase();
      str1 = str1 + str2 + ' ';
    }
    return str1;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.acroforms.gui.Summary
 * JD-Core Version:    0.6.2
 */