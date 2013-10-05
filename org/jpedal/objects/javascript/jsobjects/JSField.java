package org.jpedal.objects.javascript.jsobjects;

import org.jpedal.objects.raw.FormObject;

public class JSField
{
  public FormObject target = null;
  public String alignment = "left";
  public int charLimit = -1;
  public String defaultValue;
  public JSDoc doc;
  public boolean hidden = false;
  public String name;
  public boolean readonly = false;
  public boolean required = false;
  public Object value;
  public String valueAsString;

  public JSField()
  {
  }

  public JSField(FormObject paramFormObject)
  {
    this.target = paramFormObject;
    syncUp();
  }

  private void syncUp()
  {
    this.name = this.target.getTextStreamValue(36);
    this.value = this.target.getValue();
    this.valueAsString = this.target.getValue();
  }

  public void syncToGUI(boolean paramBoolean)
  {
    this.target.updateValue(this.value, paramBoolean, true);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.javascript.jsobjects.JSField
 * JD-Core Version:    0.6.2
 */