package org.jpedal.objects.acroforms.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractButton;
import org.jpedal.objects.acroforms.overridingImplementations.FixImageIcon;

public class SwingDownIconListener
  implements ActionListener
{
  public void actionPerformed(ActionEvent paramActionEvent)
  {
    AbstractButton localAbstractButton = (AbstractButton)paramActionEvent.getSource();
    FixImageIcon localFixImageIcon = (FixImageIcon)localAbstractButton.getPressedIcon();
    localFixImageIcon.swapImage(localAbstractButton.isSelected());
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.acroforms.actions.SwingDownIconListener
 * JD-Core Version:    0.6.2
 */