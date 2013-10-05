package org.jpedal.examples.viewer.gui.popups;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Wizard
{
  private static final String BACK_TEXT = "< Back";
  private static final String NEXT_TEXT = "Next >";
  private static final String CANCEL_TEXT = "Cancel";
  private static final String FINISH_TEXT = "Finish";
  private JDialog wizardDialog;
  private WizardPanelModel panelManager;
  private JPanel cardPanel;
  private CardLayout cardLayout;
  private JButton backButton;
  private JButton advanceButton;
  private JButton cancelButton;
  private int returnCode = 2;

  public Wizard(Frame paramFrame, WizardPanelModel paramWizardPanelModel)
  {
    this.wizardDialog = new JDialog(paramFrame);
    this.panelManager = paramWizardPanelModel;
    initComponents();
  }

  private void initComponents()
  {
    JPanel localJPanel = new JPanel();
    Box localBox = new Box(0);
    this.wizardDialog.setDefaultCloseOperation(2);
    this.cardPanel = new JPanel();
    this.cardPanel.setBorder(new EmptyBorder(new Insets(5, 10, 5, 10)));
    this.cardLayout = new CardLayout();
    this.cardPanel.setLayout(this.cardLayout);
    Map localMap = this.panelManager.getJPanels();
    Set localSet = localMap.keySet();
    Iterator localIterator = localSet.iterator();
    while (localIterator.hasNext())
    {
      Object localObject = localIterator.next();
      String str = (String)localObject;
      this.cardPanel.add(str, (JPanel)localMap.get(str));
    }
    this.backButton = new JButton("< Back");
    this.advanceButton = new JButton("Next >");
    this.cancelButton = new JButton("Cancel");
    this.backButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        Wizard.this.previousPanel();
      }
    });
    this.advanceButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        Wizard.this.nextPanel();
      }
    });
    this.cancelButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        Wizard.this.returnCode = 2;
        Wizard.this.panelManager.close();
        Wizard.this.wizardDialog.dispose();
      }
    });
    localJPanel.setLayout(new BorderLayout());
    localJPanel.add(new JSeparator(), "North");
    localBox.setBorder(new EmptyBorder(new Insets(5, 10, 5, 10)));
    localBox.add(this.backButton);
    localBox.add(Box.createHorizontalStrut(10));
    localBox.add(this.advanceButton);
    localBox.add(Box.createHorizontalStrut(30));
    localBox.add(this.cancelButton);
    localJPanel.add(localBox, "East");
    this.wizardDialog.getContentPane().add(localJPanel, "South");
    this.wizardDialog.getContentPane().add(this.cardPanel, "Center");
    this.cardLayout.show(this.cardPanel, this.panelManager.getStartPanelID());
    setBackButtonEnabled(this.panelManager.hasPrevious());
    setNextButtonEnabled(this.panelManager.canAdvance());
    this.panelManager.registerNextChangeListeners(new buttonNextState(null));
    this.panelManager.registerNextKeyListeners(new textboxPressState(null));
  }

  private void setBackButtonEnabled(boolean paramBoolean)
  {
    this.backButton.setEnabled(paramBoolean);
  }

  private void setNextButtonEnabled(boolean paramBoolean)
  {
    this.advanceButton.setEnabled(paramBoolean);
  }

  private void nextPanel()
  {
    if (this.advanceButton.getText().equals("Finish"))
    {
      this.panelManager.close();
      this.returnCode = 0;
      this.wizardDialog.dispose();
    }
    else
    {
      this.cardLayout.show(this.cardPanel, this.panelManager.next());
      setBackButtonEnabled(this.panelManager.hasPrevious());
      setNextButtonEnabled(this.panelManager.canAdvance());
      if (this.panelManager.isFinishPanel())
        this.advanceButton.setText("Finish");
    }
  }

  private void previousPanel()
  {
    if (this.panelManager.isFinishPanel())
      this.advanceButton.setText("Next >");
    this.cardLayout.show(this.cardPanel, this.panelManager.previous());
    setBackButtonEnabled(this.panelManager.hasPrevious());
    setNextButtonEnabled(this.panelManager.canAdvance());
  }

  public int showModalDialog()
  {
    this.wizardDialog.setModal(true);
    this.wizardDialog.pack();
    this.wizardDialog.setLocationRelativeTo(null);
    this.wizardDialog.setVisible(true);
    return this.returnCode;
  }

  private class textboxPressState
    implements KeyListener
  {
    private textboxPressState()
    {
    }

    public void keyReleased(KeyEvent paramKeyEvent)
    {
    }

    public void keyPressed(KeyEvent paramKeyEvent)
    {
    }

    public void keyTyped(KeyEvent paramKeyEvent)
    {
      Wizard.this.setNextButtonEnabled(Wizard.this.panelManager.canAdvance());
    }
  }

  private class buttonNextState
    implements ChangeListener
  {
    private buttonNextState()
    {
    }

    public void stateChanged(ChangeEvent paramChangeEvent)
    {
      Wizard.this.setNextButtonEnabled(Wizard.this.panelManager.canAdvance());
    }
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.gui.popups.Wizard
 * JD-Core Version:    0.6.2
 */