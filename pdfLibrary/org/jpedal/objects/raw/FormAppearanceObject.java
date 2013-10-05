package org.jpedal.objects.raw;

public class FormAppearanceObject extends PdfObject
{
  private int width;
  private int height;
  boolean isSelected = false;
  boolean isCheckBox = false;
  boolean isRadio = false;
  boolean isRollOver = false;
  private int borderStroke = -1;

  public FormAppearanceObject(String paramString)
  {
    super(paramString);
    this.objType = 129;
  }

  public int getWidth()
  {
    return this.width;
  }

  public void setWidth(int paramInt)
  {
    this.width = paramInt;
  }

  public int getHeight()
  {
    return this.height;
  }

  public void setHeight(int paramInt)
  {
    this.height = paramInt;
  }

  public int getBorderStroke()
  {
    return this.borderStroke;
  }

  public void serBorderStroke(int paramInt)
  {
    this.borderStroke = paramInt;
  }

  public void setSelected(boolean paramBoolean)
  {
    this.isSelected = paramBoolean;
  }

  public boolean isSelected()
  {
    return this.isSelected;
  }

  public void setCheckBox(boolean paramBoolean)
  {
    this.isCheckBox = paramBoolean;
  }

  public boolean isCheckBox()
  {
    return this.isCheckBox;
  }

  public void setRadio(boolean paramBoolean)
  {
    this.isRadio = paramBoolean;
  }

  public boolean isRadio()
  {
    return this.isRadio;
  }

  public boolean isRollover()
  {
    return this.isRollOver;
  }

  public void setRollover(boolean paramBoolean)
  {
    this.isRollOver = true;
  }

  public void setBorderStroke(int paramInt)
  {
    this.borderStroke = paramInt;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.raw.FormAppearanceObject
 * JD-Core Version:    0.6.2
 */