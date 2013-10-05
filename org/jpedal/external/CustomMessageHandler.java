package org.jpedal.external;

public abstract interface CustomMessageHandler
{
  public abstract boolean showMessage(Object paramObject);

  public abstract String requestInput(Object[] paramArrayOfObject);

  public abstract int requestConfirm(Object[] paramArrayOfObject);
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.external.CustomMessageHandler
 * JD-Core Version:    0.6.2
 */