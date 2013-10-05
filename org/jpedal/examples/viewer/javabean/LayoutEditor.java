package org.jpedal.examples.viewer.javabean;

import java.beans.PropertyEditorSupport;

public class LayoutEditor extends PropertyEditorSupport
{
  public String[] getTags()
  {
    return new String[] { "Single", "Continuous", "Continuous-Facing", "Facing" };
  }

  public void setAsText(String paramString)
  {
    setValue(String.valueOf(paramString));
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.javabean.LayoutEditor
 * JD-Core Version:    0.6.2
 */