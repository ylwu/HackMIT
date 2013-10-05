package org.jpedal.examples.viewer.gui.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.jpedal.examples.viewer.Commands;
import org.jpedal.examples.viewer.gui.generic.GUIButton;

public class CommandListener
  implements ActionListener
{
  Commands currentCommands;

  public CommandListener(Commands paramCommands)
  {
    this.currentCommands = paramCommands;
  }

  public void actionPerformed(ActionEvent paramActionEvent)
  {
    Object localObject = paramActionEvent.getSource();
    int i;
    if ((localObject instanceof GUIButton))
      i = ((GUIButton)localObject).getID();
    else if ((localObject instanceof SwingMenuItem))
      i = ((SwingMenuItem)localObject).getID();
    else if ((localObject instanceof SwingCombo))
      i = ((SwingCombo)localObject).getID();
    else
      i = ((SwingID)localObject).getID();
    this.currentCommands.executeCommand(i, null);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.gui.swing.CommandListener
 * JD-Core Version:    0.6.2
 */