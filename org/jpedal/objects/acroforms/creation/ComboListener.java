package org.jpedal.objects.acroforms.creation;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JComboBox;
import org.jpedal.objects.raw.FormObject;

class ComboListener
  implements ItemListener
{
  JComboBox comp;
  FormObject form;

  public ComboListener(JComboBox paramJComboBox, FormObject paramFormObject)
  {
    this.comp = paramJComboBox;
    this.form = paramFormObject;
  }

  public void itemStateChanged(ItemEvent paramItemEvent)
  {
    int i = this.comp.getSelectedIndex();
    this.form.setSelection(this.comp.getSelectedObjects(), (String)this.comp.getSelectedItem(), new int[] { i }, i);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.acroforms.creation.ComboListener
 * JD-Core Version:    0.6.2
 */