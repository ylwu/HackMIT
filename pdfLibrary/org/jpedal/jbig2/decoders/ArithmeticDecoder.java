package org.jpedal.jbig2.decoders;

import org.jpedal.jbig2.io.StreamReader;

public class ArithmeticDecoder
{
  private final StreamReader reader;
  public ArithmeticDecoderStats genericRegionStats;
  public ArithmeticDecoderStats refinementRegionStats;
  public final ArithmeticDecoderStats iadhStats;
  public final ArithmeticDecoderStats iadwStats;
  public final ArithmeticDecoderStats iaexStats;
  public final ArithmeticDecoderStats iaaiStats;
  public final ArithmeticDecoderStats iadtStats;
  public final ArithmeticDecoderStats iaitStats;
  public final ArithmeticDecoderStats iafsStats;
  public final ArithmeticDecoderStats iadsStats;
  public final ArithmeticDecoderStats iardxStats;
  public final ArithmeticDecoderStats iardyStats;
  public final ArithmeticDecoderStats iardwStats;
  public final ArithmeticDecoderStats iardhStats;
  public final ArithmeticDecoderStats iariStats;
  public ArithmeticDecoderStats iaidStats;
  private final int[] contextSize = { 16, 13, 10, 10 };
  private final int[] referredToContextSize = { 13, 10 };
  private long buffer0;
  private long buffer1;
  private long c;
  private long a;
  private long previous;
  private int counter;
  private final int[] qeTable = { 1442906112, 872480768, 402718720, 180420608, 86048768, 35717120, 1442906112, 1409351680, 1208025088, 939589632, 805371904, 604045312, 469827584, 369164288, 1442906112, 1409351680, 1359020032, 1208025088, 939589632, 872480768, 805371904, 671154176, 604045312, 570490880, 469827584, 402718720, 369164288, 335609856, 302055424, 285278208, 180420608, 163643392, 144769024, 86048768, 71368704, 44105728, 35717120, 21037056, 17891328, 8716288, 4784128, 2424832, 1376256, 589824, 327680, 65536, 1442906112 };
  private final int[] nmpsTable = { 1, 2, 3, 4, 5, 38, 7, 8, 9, 10, 11, 12, 13, 29, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 45, 46 };
  private final int[] nlpsTable = { 1, 6, 9, 12, 29, 33, 6, 14, 14, 14, 17, 18, 20, 21, 14, 14, 15, 16, 17, 18, 19, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 46 };
  private final int[] switchTable = { 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

  public ArithmeticDecoder(StreamReader paramStreamReader)
  {
    this.reader = paramStreamReader;
    this.genericRegionStats = new ArithmeticDecoderStats(2);
    this.refinementRegionStats = new ArithmeticDecoderStats(2);
    this.iadhStats = new ArithmeticDecoderStats(512);
    this.iadwStats = new ArithmeticDecoderStats(512);
    this.iaexStats = new ArithmeticDecoderStats(512);
    this.iaaiStats = new ArithmeticDecoderStats(512);
    this.iadtStats = new ArithmeticDecoderStats(512);
    this.iaitStats = new ArithmeticDecoderStats(512);
    this.iafsStats = new ArithmeticDecoderStats(512);
    this.iadsStats = new ArithmeticDecoderStats(512);
    this.iardxStats = new ArithmeticDecoderStats(512);
    this.iardyStats = new ArithmeticDecoderStats(512);
    this.iardwStats = new ArithmeticDecoderStats(512);
    this.iardhStats = new ArithmeticDecoderStats(512);
    this.iariStats = new ArithmeticDecoderStats(512);
    this.iaidStats = new ArithmeticDecoderStats(2);
  }

  public void resetIntStats(int paramInt)
  {
    this.iadhStats.reset();
    this.iadwStats.reset();
    this.iaexStats.reset();
    this.iaaiStats.reset();
    this.iadtStats.reset();
    this.iaitStats.reset();
    this.iafsStats.reset();
    this.iadsStats.reset();
    this.iardxStats.reset();
    this.iardyStats.reset();
    this.iardwStats.reset();
    this.iardhStats.reset();
    this.iariStats.reset();
    if (this.iaidStats.getContextSize() == 1 << paramInt + 1)
      this.iaidStats.reset();
    else
      this.iaidStats = new ArithmeticDecoderStats(1 << paramInt + 1);
  }

  public void resetGenericStats(int paramInt, ArithmeticDecoderStats paramArithmeticDecoderStats)
  {
    int i = this.contextSize[paramInt];
    if ((paramArithmeticDecoderStats != null) && (paramArithmeticDecoderStats.getContextSize() == i))
    {
      if (this.genericRegionStats.getContextSize() == i)
        this.genericRegionStats.overwrite(paramArithmeticDecoderStats);
      else
        this.genericRegionStats = paramArithmeticDecoderStats.copy();
    }
    else if (this.genericRegionStats.getContextSize() == i)
      this.genericRegionStats.reset();
    else
      this.genericRegionStats = new ArithmeticDecoderStats(1 << i);
  }

  public void resetRefinementStats(int paramInt, ArithmeticDecoderStats paramArithmeticDecoderStats)
  {
    int i = this.referredToContextSize[paramInt];
    if ((paramArithmeticDecoderStats != null) && (paramArithmeticDecoderStats.getContextSize() == i))
    {
      if (this.refinementRegionStats.getContextSize() == i)
        this.refinementRegionStats.overwrite(paramArithmeticDecoderStats);
      else
        this.refinementRegionStats = paramArithmeticDecoderStats.copy();
    }
    else if (this.refinementRegionStats.getContextSize() == i)
      this.refinementRegionStats.reset();
    else
      this.refinementRegionStats = new ArithmeticDecoderStats(1 << i);
  }

  public void start()
  {
    this.buffer0 = this.reader.readByte();
    this.buffer1 = this.reader.readByte();
    this.c = ((this.buffer0 ^ 0xFF) << 16 & 0xFFFFFFFF);
    readByte();
    this.c = (this.c << 7 & 0xFFFFFFFF);
    this.counter -= 7;
    this.a = 2147483648L;
  }

  public DecodeIntResult decodeInt(ArithmeticDecoderStats paramArithmeticDecoderStats)
  {
    this.previous = 1L;
    int i = decodeIntBit(paramArithmeticDecoderStats);
    long l;
    int j;
    if (decodeIntBit(paramArithmeticDecoderStats) != 0)
    {
      if (decodeIntBit(paramArithmeticDecoderStats) != 0)
      {
        if (decodeIntBit(paramArithmeticDecoderStats) != 0)
        {
          if (decodeIntBit(paramArithmeticDecoderStats) != 0)
          {
            if (decodeIntBit(paramArithmeticDecoderStats) != 0)
            {
              l = 0L;
              for (j = 0; j < 32; j++)
                l = l << 1 & 0xFFFFFFFF | decodeIntBit(paramArithmeticDecoderStats);
              l += 4436L;
            }
            else
            {
              l = 0L;
              for (j = 0; j < 12; j++)
                l = l << 1 & 0xFFFFFFFF | decodeIntBit(paramArithmeticDecoderStats);
              l += 340L;
            }
          }
          else
          {
            l = 0L;
            for (j = 0; j < 8; j++)
              l = l << 1 & 0xFFFFFFFF | decodeIntBit(paramArithmeticDecoderStats);
            l += 84L;
          }
        }
        else
        {
          l = 0L;
          for (j = 0; j < 6; j++)
            l = l << 1 & 0xFFFFFFFF | decodeIntBit(paramArithmeticDecoderStats);
          l += 20L;
        }
      }
      else
      {
        l = decodeIntBit(paramArithmeticDecoderStats);
        l = l << 1 & 0xFFFFFFFF | decodeIntBit(paramArithmeticDecoderStats);
        l = l << 1 & 0xFFFFFFFF | decodeIntBit(paramArithmeticDecoderStats);
        l = l << 1 & 0xFFFFFFFF | decodeIntBit(paramArithmeticDecoderStats);
        l += 4L;
      }
    }
    else
    {
      l = decodeIntBit(paramArithmeticDecoderStats);
      l = l << 1 & 0xFFFFFFFF | decodeIntBit(paramArithmeticDecoderStats);
    }
    if (i != 0)
    {
      if (l == 0L)
        return new DecodeIntResult((int)l, false);
      j = (int)-l;
    }
    else
    {
      j = (int)l;
    }
    return new DecodeIntResult(j, true);
  }

  public long decodeIAID(long paramLong, ArithmeticDecoderStats paramArithmeticDecoderStats)
  {
    this.previous = 1L;
    for (long l = 0L; l < paramLong; l += 1L)
    {
      int i = decodeBit(this.previous, paramArithmeticDecoderStats);
      this.previous = (this.previous << 1 & 0xFFFFFFFF | i);
    }
    return this.previous - (1 << (int)paramLong);
  }

  public int decodeBit(long paramLong, ArithmeticDecoderStats paramArithmeticDecoderStats)
  {
    int i = paramArithmeticDecoderStats.getContextCodingTableValue((int)paramLong) >> 1 & 0xFF;
    int j = paramArithmeticDecoderStats.getContextCodingTableValue((int)paramLong) & 0x1;
    int k = this.qeTable[i];
    this.a -= k;
    int m;
    if (this.c < this.a)
    {
      if ((this.a & 0x80000000) != 0L)
      {
        m = j;
      }
      else
      {
        if (this.a < k)
        {
          m = 1 - j;
          if (this.switchTable[i] != 0)
            paramArithmeticDecoderStats.setContextCodingTableValue((int)paramLong, this.nlpsTable[i] << 1 | 1 - j);
          else
            paramArithmeticDecoderStats.setContextCodingTableValue((int)paramLong, this.nlpsTable[i] << 1 | j);
        }
        else
        {
          m = j;
          paramArithmeticDecoderStats.setContextCodingTableValue((int)paramLong, this.nmpsTable[i] << 1 | j);
        }
        do
        {
          if (this.counter == 0)
            readByte();
          this.a = (this.a << 1 & 0xFFFFFFFF);
          this.c = (this.c << 1 & 0xFFFFFFFF);
          this.counter -= 1;
        }
        while ((this.a & 0x80000000) == 0L);
      }
    }
    else
    {
      this.c -= this.a;
      if (this.a < k)
      {
        m = j;
        paramArithmeticDecoderStats.setContextCodingTableValue((int)paramLong, this.nmpsTable[i] << 1 | j);
      }
      else
      {
        m = 1 - j;
        if (this.switchTable[i] != 0)
          paramArithmeticDecoderStats.setContextCodingTableValue((int)paramLong, this.nlpsTable[i] << 1 | 1 - j);
        else
          paramArithmeticDecoderStats.setContextCodingTableValue((int)paramLong, this.nlpsTable[i] << 1 | j);
      }
      this.a = k;
      do
      {
        if (this.counter == 0)
          readByte();
        this.a = (this.a << 1 & 0xFFFFFFFF);
        this.c = (this.c << 1 & 0xFFFFFFFF);
        this.counter -= 1;
      }
      while ((this.a & 0x80000000) == 0L);
    }
    return m;
  }

  private void readByte()
  {
    if (this.buffer0 == 255L)
    {
      if (this.buffer1 > 143L)
      {
        this.counter = 8;
      }
      else
      {
        this.buffer0 = this.buffer1;
        this.buffer1 = this.reader.readByte();
        this.c = (this.c + 65024L - (this.buffer0 << 9 & 0xFFFFFFFF));
        this.counter = 7;
      }
    }
    else
    {
      this.buffer0 = this.buffer1;
      this.buffer1 = this.reader.readByte();
      this.c = (this.c + 65280L - (this.buffer0 << 8 & 0xFFFFFFFF));
      this.counter = 8;
    }
  }

  private int decodeIntBit(ArithmeticDecoderStats paramArithmeticDecoderStats)
  {
    int i = decodeBit(this.previous, paramArithmeticDecoderStats);
    if (this.previous < 256L)
      this.previous = (this.previous << 1 & 0xFFFFFFFF | i);
    else
      this.previous = ((this.previous << 1 & 0xFFFFFFFF | i) & 0x1FF | 0x100);
    return i;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.jbig2.decoders.ArithmeticDecoder
 * JD-Core Version:    0.6.2
 */