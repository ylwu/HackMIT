package org.jpedal.grouping;

public class DefaultSearchListener
  implements SearchListener
{
  private boolean isCanceled;

  public boolean isCanceled()
  {
    return this.isCanceled;
  }

  public void requestCancel()
  {
    this.isCanceled = true;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.grouping.DefaultSearchListener
 * JD-Core Version:    0.6.2
 */