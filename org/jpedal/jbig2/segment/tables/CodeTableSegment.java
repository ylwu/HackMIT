package org.jpedal.jbig2.segment.tables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import org.jpedal.jbig2.decoders.JBIG2StreamDecoder;
import org.jpedal.jbig2.segment.Segment;

public class CodeTableSegment extends Segment
{
  private int[][] table;

  public CodeTableSegment(JBIG2StreamDecoder paramJBIG2StreamDecoder)
  {
    super(paramJBIG2StreamDecoder);
  }

  public void readSegment()
  {
    HashMap localHashMap1 = new HashMap();
    HashMap localHashMap2 = new HashMap();
    HashMap localHashMap3 = new HashMap();
    this.decoder.readBit();
    int k = this.decoder.readBits(3) + 1;
    int j = this.decoder.readBits(3) + 1;
    int i = this.decoder.readBit() == 1 ? 1 : 0;
    int m = (this.decoder.readByte() & 0xFF) << 24 | (this.decoder.readByte() & 0xFF) << 16 | (this.decoder.readByte() & 0xFF) << 8 | this.decoder.readByte() & 0xFF;
    int n = (this.decoder.readByte() & 0xFF) << 24 | (this.decoder.readByte() & 0xFF) << 16 | (this.decoder.readByte() & 0xFF) << 8 | this.decoder.readByte() & 0xFF;
    int i1 = m;
    int i2 = 0;
    int i5 = 1;
    while (i5 != 0)
    {
      localHashMap3.put(Integer.valueOf(i2), Integer.valueOf(this.decoder.readBits(j)));
      localHashMap2.put(Integer.valueOf(i2), Integer.valueOf(this.decoder.readBits(k)));
      localHashMap1.put(Integer.valueOf(i2), Integer.valueOf(i1));
      i1 += (int)Math.pow(2.0D, ((Integer)localHashMap2.get(Integer.valueOf(i2))).intValue());
      i2 += 1;
      if (i1 >= n)
        i5 = 0;
    }
    int i3 = this.decoder.readBits(j);
    localHashMap3.put(Integer.valueOf(i2), Integer.valueOf(i3));
    localHashMap2.put(Integer.valueOf(i2), Integer.valueOf(32));
    localHashMap1.put(Integer.valueOf(i2), Integer.valueOf(n - 1));
    i2 += 1;
    int i4 = this.decoder.readBits(j);
    localHashMap3.put(Integer.valueOf(i2), Integer.valueOf(i4));
    localHashMap2.put(Integer.valueOf(i2), Integer.valueOf(32));
    localHashMap1.put(Integer.valueOf(i2), Integer.valueOf(n));
    i2 += 1;
    if (i != 0)
    {
      int i6 = this.decoder.readBits(j);
      localHashMap3.put(Integer.valueOf(i2), Integer.valueOf(i6));
      localHashMap2.put(Integer.valueOf(i2), Integer.valueOf(-2));
      i2 += 1;
    }
    this.decoder.consumeRemainingBits();
    HashMap localHashMap4 = new HashMap();
    HashMap localHashMap5 = new HashMap();
    HashMap localHashMap6 = new HashMap();
    int i7 = 0;
    Iterator localIterator = localHashMap3.values().iterator();
    while (localIterator.hasNext())
    {
      localObject = (Integer)localIterator.next();
      Integer localInteger1 = (Integer)localHashMap4.get(localObject);
      Integer localInteger2;
      if (localInteger1 == null)
      {
        localInteger1 = Integer.valueOf(1);
      }
      else
      {
        localInteger2 = localInteger1;
        Integer localInteger3 = localInteger1 = Integer.valueOf(localInteger1.intValue() + 1);
      }
      localHashMap4.put(localObject, localInteger1);
      if (((Integer)localObject).intValue() > i7)
        i7 = ((Integer)localObject).intValue();
    }
    int i8 = 1;
    localHashMap5.put(Integer.valueOf(0), Integer.valueOf(0));
    localHashMap4.put(Integer.valueOf(0), Integer.valueOf(0));
    while (i8 <= i7)
    {
      int i11 = 0;
      if (localHashMap4.get(Integer.valueOf(i8 - 1)) != null)
        i11 = ((Integer)localHashMap4.get(Integer.valueOf(i8 - 1))).intValue();
      localHashMap5.put(Integer.valueOf(i8), Integer.valueOf((((Integer)localHashMap5.get(Integer.valueOf(i8 - 1))).intValue() + i11) * 2));
      int i9 = ((Integer)localHashMap5.get(Integer.valueOf(i8))).intValue();
      int i10 = 0;
      while (i10 < i2)
      {
        if (((Integer)localHashMap3.get(Integer.valueOf(i10))).intValue() == i8)
        {
          localHashMap6.put(Integer.valueOf(i10), Integer.valueOf(i9));
          i9 += 1;
        }
        i10 += 1;
      }
      i8 += 1;
    }
    this.table = new int[localHashMap3.size() + 1][];
    ArrayList localArrayList = new ArrayList();
    Object localObject = localHashMap3.values().toArray();
    Arrays.sort((Object[])localObject);
    for (int i12 = 0; i12 < localObject.length; i12++)
    {
      int i13 = 1;
      int i14 = ((Integer)localObject[i12]).intValue();
      while ((i12 + 1 < localObject.length) && (localObject[(i12 + 1)] == localObject[i12]))
      {
        i12++;
        i13++;
      }
      if (i14 != 0)
      {
        int i15 = 0;
        int i16 = 0;
        int[] arrayOfInt1 = new int[i13];
        while ((i15 < i13) && (i16 < localHashMap3.size()))
        {
          if (((Integer)localHashMap3.get(Integer.valueOf(i16))).intValue() == i14)
          {
            arrayOfInt1[i15] = i16;
            i15++;
          }
          i16++;
        }
        if (arrayOfInt1.length == 1)
        {
          int i17 = arrayOfInt1[0];
          localArrayList.add(new int[] { ((Integer)localHashMap1.get(Integer.valueOf(i17))).intValue(), ((Integer)localHashMap3.get(Integer.valueOf(i17))).intValue(), ((Integer)localHashMap2.get(Integer.valueOf(i17))).intValue(), localHashMap6.get(Integer.valueOf(i17)) == null ? 0 : ((Integer)localHashMap6.get(Integer.valueOf(i17))).intValue() });
        }
        else
        {
          int[] arrayOfInt2 = new int[i13];
          i16 = 0;
          int i20;
          for (i20 : arrayOfInt1)
          {
            arrayOfInt2[i16] = ((Integer)localHashMap1.get(Integer.valueOf(i20))).intValue();
            i16++;
          }
          Arrays.sort(arrayOfInt2);
          for (i20 : arrayOfInt2)
          {
            for (i16 = 0; ((Integer)localHashMap1.get(Integer.valueOf(arrayOfInt1[i16]))).intValue() != i20; i16++);
            int i21 = arrayOfInt1[i16];
            localArrayList.add(new int[] { ((Integer)localHashMap1.get(Integer.valueOf(i21))).intValue(), ((Integer)localHashMap3.get(Integer.valueOf(i21))).intValue(), ((Integer)localHashMap2.get(Integer.valueOf(i21))).intValue(), localHashMap6.get(Integer.valueOf(i21)) == null ? 0 : ((Integer)localHashMap6.get(Integer.valueOf(i21))).intValue() });
          }
        }
      }
    }
    localArrayList.add(new int[] { 0, 0, -1, 0 });
    this.table = new int[localArrayList.size()][];
    System.arraycopy(localArrayList.toArray(this.table), 0, this.table, 0, localArrayList.size());
  }

  public int[][] getHuffTable()
  {
    return this.table;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.jbig2.segment.tables.CodeTableSegment
 * JD-Core Version:    0.6.2
 */