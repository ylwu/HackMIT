package org.jpedal.objects;

import java.util.HashMap;
import java.util.Map;

public class PageLookup
{
  private Map pageLookup = new HashMap();

  public int convertObjectToPageNumber(String paramString)
  {
    Object localObject = this.pageLookup.get(paramString);
    if (localObject == null)
      return -1;
    return ((Integer)localObject).intValue();
  }

  public void put(String paramString, int paramInt)
  {
    this.pageLookup.put(paramString, Integer.valueOf(paramInt));
  }

  public void dispose()
  {
    if (this.pageLookup != null)
      this.pageLookup.clear();
    this.pageLookup = null;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.PageLookup
 * JD-Core Version:    0.6.2
 */