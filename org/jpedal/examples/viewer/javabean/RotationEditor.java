package org.jpedal.examples.viewer.javabean;

import java.beans.PropertyEditorSupport;

public class RotationEditor extends PropertyEditorSupport
{
  public String[] getTags()
  {
    return new String[] { "0", "90", "180", "270" };
  }

  public void setAsText(String paramString)
  {
    if (paramString.equals("0"))
      setValue(new Integer(0));
    else if (paramString.equals("90"))
      setValue(new Integer(90));
    else if (paramString.equals("180"))
      setValue(new Integer(180));
    else if (paramString.equals("270"))
      setValue(new Integer(270));
    else
      throw new IllegalArgumentException(paramString);
  }

  public String getJavaInitializationString()
  {
    switch (((Number)getValue()).intValue())
    {
    case 0:
    default:
      return "0";
    case 90:
      return "90";
    case 180:
      return "180";
    case 270:
    }
    return "270";
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.javabean.RotationEditor
 * JD-Core Version:    0.6.2
 */