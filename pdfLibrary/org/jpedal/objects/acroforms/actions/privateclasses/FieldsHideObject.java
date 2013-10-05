package org.jpedal.objects.acroforms.actions.privateclasses;

public class FieldsHideObject
{
  private String[] fieldsToHide = new String[0];
  private boolean[] whetherToHide = new boolean[0];

  public void setFieldArray(String[] paramArrayOfString)
  {
    this.fieldsToHide = paramArrayOfString;
  }

  public void setHideArray(boolean[] paramArrayOfBoolean)
  {
    this.whetherToHide = paramArrayOfBoolean;
  }

  public String[] getFieldArray()
  {
    return this.fieldsToHide;
  }

  public boolean[] getHideArray()
  {
    return this.whetherToHide;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.acroforms.actions.privateclasses.FieldsHideObject
 * JD-Core Version:    0.6.2
 */