package org.jpedal.objects.acroforms.actions;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.AbstractButton;

public class SwingFormButtonListener
  implements MouseListener
{
  private static final boolean showMethods = false;
  private Map captionChanger = null;

  public SwingFormButtonListener(String paramString1, String paramString2, String paramString3)
  {
    int i = 0;
    this.captionChanger = new HashMap();
    if ((paramString2 != null) && (!paramString2.isEmpty()))
    {
      this.captionChanger.put("rollover", paramString2);
      i++;
    }
    if ((paramString3 != null) && (!paramString3.isEmpty()))
    {
      this.captionChanger.put("down", paramString3);
      i++;
    }
    if ((paramString1 != null) && (!paramString1.isEmpty()))
    {
      this.captionChanger.put("normal", paramString1);
      i++;
    }
    if (i == 0)
      this.captionChanger = null;
  }

  public void mouseEntered(MouseEvent paramMouseEvent)
  {
    if ((this.captionChanger != null) && ((paramMouseEvent.getSource() instanceof AbstractButton)) && (this.captionChanger.containsKey("rollover")))
      ((AbstractButton)paramMouseEvent.getSource()).setText((String)this.captionChanger.get("rollover"));
  }

  public void mouseExited(MouseEvent paramMouseEvent)
  {
    if ((this.captionChanger != null) && ((paramMouseEvent.getSource() instanceof AbstractButton)) && (this.captionChanger.containsKey("normal")))
      ((AbstractButton)paramMouseEvent.getSource()).setText((String)this.captionChanger.get("normal"));
  }

  public void mouseClicked(MouseEvent paramMouseEvent)
  {
  }

  public void mousePressed(MouseEvent paramMouseEvent)
  {
    if ((this.captionChanger != null) && ((paramMouseEvent.getSource() instanceof AbstractButton)) && (this.captionChanger.containsKey("down")))
      ((AbstractButton)paramMouseEvent.getSource()).setText((String)this.captionChanger.get("down"));
  }

  public void mouseReleased(MouseEvent paramMouseEvent)
  {
    if ((this.captionChanger != null) && ((paramMouseEvent.getSource() instanceof AbstractButton)))
      if (this.captionChanger.containsKey("rollover"))
        ((AbstractButton)paramMouseEvent.getSource()).setText((String)this.captionChanger.get("rollover"));
      else if (this.captionChanger.containsKey("normal"))
        ((AbstractButton)paramMouseEvent.getSource()).setText((String)this.captionChanger.get("normal"));
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.acroforms.actions.SwingFormButtonListener
 * JD-Core Version:    0.6.2
 */