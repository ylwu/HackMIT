package org.jpedal.objects.acroforms.creation;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import org.jpedal.objects.raw.FormObject;

class TextDocumentListener
  implements DocumentListener
{
  JTextComponent textcomp;
  FormObject form;

  public TextDocumentListener(JTextComponent paramJTextComponent, FormObject paramFormObject)
  {
    this.textcomp = paramJTextComponent;
    this.form = paramFormObject;
  }

  public void changedUpdate(DocumentEvent paramDocumentEvent)
  {
    updateFormValue();
  }

  public void removeUpdate(DocumentEvent paramDocumentEvent)
  {
    updateFormValue();
  }

  public void insertUpdate(DocumentEvent paramDocumentEvent)
  {
    updateFormValue();
  }

  private void updateFormValue()
  {
    this.form.updateValue(this.textcomp.getText(), false, false);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.acroforms.creation.TextDocumentListener
 * JD-Core Version:    0.6.2
 */