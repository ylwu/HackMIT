package org.jpedal.objects.javascript.jsobjects;

public class JSEvent
{
  public String change;
  public Object changeEx;
  public int commitKey = 0;
  public String name;
  public boolean rc = true;
  public Object[] richChange;
  public Object[] richChangeEx;
  public Object[] richValue;
  public Object source;
  public Object target;
  public String targetName;
  public String type;
  public Object value;
  public boolean willCommit = false;

  public JSEvent()
  {
  }

  public JSEvent(int paramInt)
  {
    setNameAndType(paramInt);
  }

  public JSEvent(int paramInt, Object paramObject1, Object paramObject2)
  {
    setNameAndType(paramInt);
    this.source = paramObject1;
    this.target = paramObject2;
  }

  private void setNameAndType(int paramInt)
  {
    switch (paramInt)
    {
    case 31:
      this.name = "Open";
      this.type = "Page";
      break;
    case 19:
      this.name = "Close";
      this.type = "Page";
      break;
    case 27:
      this.name = "Keystroke";
      this.type = "Field";
      break;
    case 38:
      this.name = "Validate";
      this.type = "Field";
      break;
    case 4866:
      this.name = "Calculate";
      this.type = "Field";
      break;
    case 22:
      this.name = "Format";
      this.type = "Field";
    }
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.javascript.jsobjects.JSEvent
 * JD-Core Version:    0.6.2
 */