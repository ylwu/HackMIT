package org.jpedal.objects.acroforms.creation;

import javax.swing.AbstractButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jpedal.objects.raw.FormObject;

class RadioListener
  implements ChangeListener
{
  AbstractButton comp;
  FormObject form;

  public RadioListener(AbstractButton paramAbstractButton, FormObject paramFormObject)
  {
    this.comp = paramAbstractButton;
    this.form = paramFormObject;
  }

  public void stateChanged(ChangeEvent paramChangeEvent)
  {
    this.form.updateValue(this.comp.getText(), this.comp.isSelected(), false);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.acroforms.creation.RadioListener
 * JD-Core Version:    0.6.2
 */