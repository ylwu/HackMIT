package org.jpedal.examples.viewer.gui.popups;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import org.jpedal.utils.Messages;

public class ErrorDialog
{
  public static void showError(Throwable paramThrowable, String paramString1, final Component paramComponent, String paramString2)
  {
    JPanel localJPanel = new JPanel();
    localJPanel.setLayout(new GridBagLayout());
    GridBagConstraints localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.anchor = 17;
    localGridBagConstraints.gridx = 0;
    localGridBagConstraints.gridy = 0;
    localJPanel.add(new JLabel(paramString1), localGridBagConstraints);
    StringWriter localStringWriter = new StringWriter();
    PrintWriter localPrintWriter = new PrintWriter(localStringWriter);
    paramThrowable.printStackTrace(localPrintWriter);
    String str = localStringWriter.toString();
    JTextArea localJTextArea = new JTextArea(str);
    localJTextArea.setEditable(false);
    localJTextArea.setRows(10);
    localJTextArea.setCaretPosition(0);
    JScrollPane localJScrollPane = new JScrollPane(localJTextArea, 20, 31);
    localJScrollPane.setVisible(false);
    localGridBagConstraints.gridy = 1;
    localGridBagConstraints.ipady = 10;
    final JLabel localJLabel = new JLabel(Messages.getMessage("PdfViewerError.CopyStacktrace"));
    localJLabel.setVisible(false);
    localJPanel.add(localJLabel, localGridBagConstraints);
    localGridBagConstraints.ipady = 0;
    localGridBagConstraints.gridy = 2;
    localJPanel.add(localJScrollPane, localGridBagConstraints);
    JButton localJButton = new JButton(Messages.getMessage("PdfViewerButton.ShowDetails"));
    localJButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        JButton localJButton = (JButton)paramAnonymousActionEvent.getSource();
        if (this.val$scrollPane.isVisible())
        {
          this.val$scrollPane.setVisible(false);
          localJLabel.setVisible(false);
          localJButton.setText(Messages.getMessage("PdfViewerButton.ShowDetails"));
        }
        else
        {
          this.val$scrollPane.setVisible(true);
          localJLabel.setVisible(true);
          localJButton.setText(Messages.getMessage("PdfViewerButton.HideDetails"));
        }
        JDialog localJDialog = (JDialog)localJButton.getTopLevelAncestor();
        localJDialog.pack();
        localJDialog.setLocationRelativeTo(paramComponent);
      }
    });
    Object[] arrayOfObject = { localJButton, Messages.getMessage("PdfViewerButton.Exit") };
    JOptionPane localJOptionPane = new JOptionPane();
    localJOptionPane.setMessage(localJPanel);
    localJOptionPane.setMessageType(0);
    localJOptionPane.setOptionType(-1);
    localJOptionPane.setOptions(arrayOfObject);
    JDialog localJDialog = localJOptionPane.createDialog(paramComponent, paramString2);
    localJDialog.pack();
    localJDialog.setVisible(true);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.gui.popups.ErrorDialog
 * JD-Core Version:    0.6.2
 */