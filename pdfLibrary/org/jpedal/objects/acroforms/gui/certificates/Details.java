package org.jpedal.objects.acroforms.gui.certificates;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigInteger;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class Details extends JPanel
{
  private String publicKey;
  private JTextArea descriptionBox;
  private JLabel jLabel1;
  private JScrollPane jScrollPane1;
  private JScrollPane jScrollPane2;
  private JTable jTable1;

  public void setValues(int paramInt, String paramString1, String paramString2, String paramString3, BigInteger paramBigInteger, String paramString4, String paramString5, String paramString6, String paramString7, String paramString8, String paramString9, String paramString10)
  {
    TableModel localTableModel = this.jTable1.getModel();
    this.publicKey = paramString7;
    localTableModel.setValueAt(String.valueOf(paramInt), 0, 1);
    localTableModel.setValueAt(paramString1, 1, 1);
    localTableModel.setValueAt(paramString2, 2, 1);
    localTableModel.setValueAt(paramString3, 3, 1);
    localTableModel.setValueAt(Long.toHexString(paramBigInteger.longValue()).toUpperCase(), 4, 1);
    localTableModel.setValueAt(paramString4, 5, 1);
    localTableModel.setValueAt(paramString5, 6, 1);
    localTableModel.setValueAt(paramString6, 7, 1);
    localTableModel.setValueAt(paramString8, 8, 1);
    localTableModel.setValueAt(paramString9, 9, 1);
    localTableModel.setValueAt(paramString10, 10, 1);
  }

  public Details()
  {
    initComponents();
    this.jTable1.addMouseListener(new MouseListener()
    {
      public void mouseClicked(MouseEvent paramAnonymousMouseEvent)
      {
        int i = Details.this.jTable1.getSelectedRow();
        if (i == 7)
          Details.this.descriptionBox.setText(Details.this.publicKey);
        else
          Details.this.descriptionBox.setText((String)Details.this.jTable1.getModel().getValueAt(i, 1));
        Details.this.descriptionBox.setCaretPosition(0);
      }

      public void mousePressed(MouseEvent paramAnonymousMouseEvent)
      {
      }

      public void mouseReleased(MouseEvent paramAnonymousMouseEvent)
      {
      }

      public void mouseEntered(MouseEvent paramAnonymousMouseEvent)
      {
      }

      public void mouseExited(MouseEvent paramAnonymousMouseEvent)
      {
      }
    });
  }

  private void initComponents()
  {
    this.jLabel1 = new JLabel();
    this.jScrollPane1 = new JScrollPane();
    this.jTable1 = new JTable();
    this.jScrollPane2 = new JScrollPane();
    this.descriptionBox = new JTextArea();
    setLayout(null);
    this.jLabel1.setText("Certificate data:");
    add(this.jLabel1);
    this.jLabel1.setBounds(10, 10, 220, 14);
    this.jTable1.setModel(new DefaultTableModel(new Object[][] { { "Version", null }, { "Signature algorithum", null }, { "Subject", null }, { "Issuer", null }, { "Serial number", null }, { "Validity starts", null }, { "Validity ends", null }, { "Public key", null }, { "X.509 data", null }, { "SHA-1 digest", null }, { "MD5 digest", null } }, new String[] { "Name", "Value" })
    {
      Class[] types = { String.class, String.class };
      boolean[] canEdit = { false, false };

      public Class getColumnClass(int paramAnonymousInt)
      {
        return this.types[paramAnonymousInt];
      }

      public boolean isCellEditable(int paramAnonymousInt1, int paramAnonymousInt2)
      {
        return this.canEdit[paramAnonymousInt2];
      }
    });
    this.jScrollPane1.setViewportView(this.jTable1);
    add(this.jScrollPane1);
    this.jScrollPane1.setBounds(10, 30, 380, 150);
    this.descriptionBox.setColumns(20);
    this.descriptionBox.setEditable(false);
    this.descriptionBox.setLineWrap(true);
    this.descriptionBox.setRows(5);
    this.descriptionBox.setWrapStyleWord(true);
    this.jScrollPane2.setViewportView(this.descriptionBox);
    add(this.jScrollPane2);
    this.jScrollPane2.setBounds(10, 190, 380, 120);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.acroforms.gui.certificates.Details
 * JD-Core Version:    0.6.2
 */