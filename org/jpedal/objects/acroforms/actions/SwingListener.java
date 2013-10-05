package org.jpedal.objects.acroforms.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.JTextComponent;
import org.jpedal.objects.raw.FormObject;

public class SwingListener extends PDFListener
  implements MouseListener, KeyListener, FocusListener, MouseMotionListener, ActionListener, ListSelectionListener
{
  public SwingListener(FormObject paramFormObject, ActionHandler paramActionHandler)
  {
    super(paramFormObject, paramActionHandler);
  }

  public void mouseClicked(MouseEvent paramMouseEvent)
  {
    super.mouseClicked(paramMouseEvent);
  }

  public void mousePressed(MouseEvent paramMouseEvent)
  {
    super.mousePressed(paramMouseEvent);
  }

  public void mouseReleased(MouseEvent paramMouseEvent)
  {
    super.mouseReleased(paramMouseEvent);
  }

  public void mouseEntered(MouseEvent paramMouseEvent)
  {
    this.handler.A(paramMouseEvent, this.formObject, 4);
    this.handler.E(paramMouseEvent, this.formObject);
    if (this.formObject.getCharacteristics()[8] != 0)
      ((Component)this.formObject.getGUIComponent()).setVisible(true);
  }

  public void mouseExited(MouseEvent paramMouseEvent)
  {
    this.handler.A(paramMouseEvent, this.formObject, 5);
    this.handler.X(paramMouseEvent, this.formObject);
    if (this.formObject.getCharacteristics()[8] != 0)
      ((Component)this.formObject.getGUIComponent()).setVisible(false);
  }

  public void keyTyped(KeyEvent paramKeyEvent)
  {
    int i = 0;
    int j = this.formObject.getInt(1209815663);
    int k;
    if (j != -1)
    {
      k = paramKeyEvent.getKeyChar();
      if ((k != 8) && (k != 127))
      {
        JTextComponent localJTextComponent = (JTextComponent)paramKeyEvent.getSource();
        String str = localJTextComponent.getText();
        int m = str.length();
        if (m >= j)
        {
          paramKeyEvent.consume();
          i = 1;
        }
        if (m > j)
          localJTextComponent.setText(str.substring(0, j));
      }
    }
    if (i == 0)
    {
      if ((paramKeyEvent.getKeyChar() == '\n') && (!(paramKeyEvent.getSource() instanceof JTextArea)))
        ((JComponent)paramKeyEvent.getSource()).transferFocus();
      k = this.handler.K(paramKeyEvent, this.formObject, 1);
      if (k == 1)
        paramKeyEvent.consume();
      this.handler.V(paramKeyEvent, this.formObject, 1);
    }
  }

  public void keyPressed(KeyEvent paramKeyEvent)
  {
  }

  public void keyReleased(KeyEvent paramKeyEvent)
  {
    super.keyReleased(paramKeyEvent);
  }

  public void focusGained(FocusEvent paramFocusEvent)
  {
    super.focusGained(paramFocusEvent);
  }

  public void focusLost(FocusEvent paramFocusEvent)
  {
    super.focusLost(paramFocusEvent);
  }

  public void mouseDragged(MouseEvent paramMouseEvent)
  {
  }

  public void mouseMoved(MouseEvent paramMouseEvent)
  {
  }

  public void actionPerformed(ActionEvent paramActionEvent)
  {
    mouseClicked(paramActionEvent);
  }

  public void valueChanged(ListSelectionEvent paramListSelectionEvent)
  {
    mouseClicked(paramListSelectionEvent);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.acroforms.actions.SwingListener
 * JD-Core Version:    0.6.2
 */