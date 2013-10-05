package org.jpedal.fonts.tt;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Glyf extends Table
{
  private Map charStrings = new HashMap();
  private int glyfCount = 0;
  private Map emptyCharStrings = new HashMap();
  private byte[] glyphTable;

  public Glyf(FontFile2 paramFontFile2, int paramInt, int[] paramArrayOfInt)
  {
    this.glyfCount = paramInt;
    int i = paramFontFile2.selectTable(3);
    if (i != 0)
    {
      for (int j = 0; j < paramInt; j++)
        if (paramArrayOfInt[j] == paramArrayOfInt[(j + 1)])
        {
          this.charStrings.put(Integer.valueOf(j), Integer.valueOf(-1));
          this.emptyCharStrings.put(Integer.valueOf(j), "x");
        }
        else
        {
          this.charStrings.put(Integer.valueOf(j), Integer.valueOf(paramArrayOfInt[j]));
        }
      this.glyphTable = paramFontFile2.getTableBytes(4);
    }
  }

  public int getCharString(int paramInt)
  {
    Object localObject = this.charStrings.get(Integer.valueOf(paramInt));
    if (localObject == null)
      return paramInt;
    return ((Integer)localObject).intValue();
  }

  public byte[] getTableData()
  {
    return this.glyphTable;
  }

  public int getGlypfCount()
  {
    return this.glyfCount;
  }

  public Map buildCharStringTable()
  {
    HashMap localHashMap = new HashMap();
    Iterator localIterator = this.charStrings.keySet().iterator();
    while (localIterator.hasNext())
    {
      Object localObject = localIterator.next();
      if (!this.emptyCharStrings.containsKey(localObject))
        localHashMap.put(localObject, localObject);
    }
    return localHashMap;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.fonts.tt.Glyf
 * JD-Core Version:    0.6.2
 */