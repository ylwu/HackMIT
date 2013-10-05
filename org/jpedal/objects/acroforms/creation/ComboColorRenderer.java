package org.jpedal.objects.acroforms.creation;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

class ComboColorRenderer extends JLabel
  implements ListCellRenderer
{
  Color color = Color.RED;

  public ComboColorRenderer(Color paramColor)
  {
    this.color = paramColor;
    setBorder(null);
    setOpaque(true);
  }

  public Component getListCellRendererComponent(JList paramJList, Object paramObject, int paramInt, boolean paramBoolean1, boolean paramBoolean2)
  {
    setBackground(this.color);
    if ((paramObject == null) || (((String)paramObject).isEmpty()))
      setText(" ");
    else
      setText((String)paramObject);
    return this;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.acroforms.creation.ComboColorRenderer
 * JD-Core Version:    0.6.2
 */