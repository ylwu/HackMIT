package org.jpedal.render;

public abstract interface ShapeFactory
{
  public abstract Object getContent();

  public abstract boolean isEmpty();

  public abstract int getShapeColor();

  public abstract void setShapeNumber(int paramInt);

  public abstract double getMinXcoord();

  public abstract double getMinYcoord();
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.render.ShapeFactory
 * JD-Core Version:    0.6.2
 */