package org.jpedal.examples.viewer.gui;

import javax.swing.tree.DefaultMutableTreeNode;

public class CheckNode extends DefaultMutableTreeNode
{
  protected boolean isSelected;
  protected boolean isEnabled;
  private Object text;

  public CheckNode(Object paramObject)
  {
    this(paramObject, true, false);
    this.text = paramObject;
  }

  public CheckNode(Object paramObject, boolean paramBoolean1, boolean paramBoolean2)
  {
    super(paramObject, paramBoolean1);
    this.isSelected = paramBoolean2;
    this.text = paramObject;
  }

  public Object getText()
  {
    return this.text;
  }

  public void setSelected(boolean paramBoolean)
  {
    this.isSelected = paramBoolean;
  }

  public boolean isSelected()
  {
    return this.isSelected;
  }

  public void setEnabled(boolean paramBoolean)
  {
    this.isEnabled = paramBoolean;
  }

  public boolean isEnabled()
  {
    return this.isEnabled;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.gui.CheckNode
 * JD-Core Version:    0.6.2
 */