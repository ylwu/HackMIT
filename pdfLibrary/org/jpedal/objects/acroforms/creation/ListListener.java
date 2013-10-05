package org.jpedal.objects.acroforms.creation;

import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.jpedal.objects.raw.FormObject;

class ListListener
  implements ListSelectionListener
{
  JList comp;
  FormObject form;

  public ListListener(JList paramJList, FormObject paramFormObject)
  {
    this.comp = paramJList;
    this.form = paramFormObject;
  }

  public void valueChanged(ListSelectionEvent paramListSelectionEvent)
  {
    this.form.setSelection(this.comp.getSelectedValues(), (String)this.comp.getSelectedValue(), this.comp.getSelectedIndices(), this.comp.getSelectedIndex());
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.acroforms.creation.ListListener
 * JD-Core Version:    0.6.2
 */