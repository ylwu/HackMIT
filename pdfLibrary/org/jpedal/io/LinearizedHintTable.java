package org.jpedal.io;

import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;
import org.jpedal.objects.raw.PdfObject;
import org.jpedal.utils.LogWriter;

public class LinearizedHintTable
{
  private final Map startRefs = new HashMap();
  private final Map endRefs = new HashMap();
  private static final int[] mask = { 255, 127, 63, 31, 15, 7, 3, 1 };
  private static final int[] shift = { 0, 8, 16, 24 };
  private int[] pageObjectCount = null;
  private int[] obj = null;
  private int[] pageLength;
  private long[] pageStart;
  private FileChannel fos = null;
  private boolean finishedReading = false;

  public LinearizedHintTable(FileChannel paramFileChannel)
  {
    this.fos = paramFileChannel;
  }

  public void readTable(PdfObject paramPdfObject1, PdfObject paramPdfObject2, int paramInt, long paramLong)
  {
    byte[] arrayOfByte = paramPdfObject1.getDecodedStream();
    int i = paramPdfObject2.getInt(30);
    if (arrayOfByte != null)
      parseHintTable(i, paramInt, paramLong, arrayOfByte);
  }

  private void parseHintTable(int paramInt1, int paramInt2, long paramLong, byte[] paramArrayOfByte)
  {
    int i = 0;
    this.pageObjectCount = new int[paramInt1 + 1];
    this.obj = new int[paramInt1 + 1];
    this.pageStart = new long[paramInt1 + 1];
    this.pageLength = new int[paramInt1 + 1];
    int[] arrayOfInt = new int[paramInt1 + 1];
    int j = ((paramArrayOfByte[i] & 0xFF) << 24) + ((paramArrayOfByte[(i + 1)] & 0xFF) << 16) + ((paramArrayOfByte[(i + 2)] & 0xFF) << 8) + (paramArrayOfByte[(i + 3)] & 0xFF);
    i += 4;
    i += 4;
    int k = ((paramArrayOfByte[i] & 0xFF) << 8) + (paramArrayOfByte[(i + 1)] & 0xFF);
    i += 2;
    int m = ((paramArrayOfByte[i] & 0xFF) << 24) + ((paramArrayOfByte[(i + 1)] & 0xFF) << 16) + ((paramArrayOfByte[(i + 2)] & 0xFF) << 8) + (paramArrayOfByte[(i + 3)] & 0xFF);
    i += 4;
    int n = ((paramArrayOfByte[i] & 0xFF) << 8) + (paramArrayOfByte[(i + 1)] & 0xFF);
    i += 2;
    i += 4;
    i += 2;
    i += 4;
    i += 2;
    int i1 = ((paramArrayOfByte[i] & 0xFF) << 8) + (paramArrayOfByte[(i + 1)] & 0xFF);
    i += 2;
    i += 2;
    i += 2;
    i += 2;
    int i2 = i << 3;
    int i3 = 0;
    for (int i4 = 0; i4 < paramInt1; i4++)
    {
      this.pageObjectCount[(i4 + 1)] = (j + getBitsFromByteStream(i2, k, paramArrayOfByte));
      i3 += this.pageObjectCount[(i4 + 1)];
      i2 += k;
    }
    this.obj[1] = paramInt2;
    if (paramInt1 > 1)
      this.obj[2] = 1;
    for (i4 = 3; i4 < paramInt1; i4++)
      this.obj[i4] = (this.obj[(i4 - 1)] + this.pageObjectCount[(i4 - 1)]);
    i2 = i2 + 7 >> 3 << 3;
    for (i4 = 0; i4 < paramInt1; i4++)
    {
      this.pageLength[(i4 + 1)] = (m + getBitsFromByteStream(i2, n, paramArrayOfByte));
      i2 += n;
    }
    for (i4 = 0; i4 < paramInt1; i4++)
      if (i4 == 0)
        this.pageStart[(i4 + 1)] = paramLong;
      else
        this.pageStart[(i4 + 1)] = (this.pageStart[i4] + this.pageLength[i4]);
    i2 = i2 + 7 >> 3 << 3;
    for (i4 = 0; i4 < paramInt1; i4++)
    {
      arrayOfInt[(i4 + 1)] = getBitsFromByteStream(i2, i1, paramArrayOfByte);
      i2 += n;
    }
  }

  private static int getBitsFromByteStream(int paramInt1, int paramInt2, byte[] paramArrayOfByte)
  {
    int i = 0;
    int j = paramInt1 >> 3;
    int k = paramInt1 & 0x7;
    int m = (paramInt2 + k >> 3) + 1;
    int n = (m << 3) - k - paramInt2;
    if (k == 0)
      n = (m << 3) - paramInt2 & 0x7;
    if (m > 4)
      return 0;
    for (int i1 = 0; i1 < m; i1++)
    {
      int i2 = paramArrayOfByte[(j + i1)] & 0xFF;
      if (i1 == 0)
        i2 &= mask[k];
      i += (i2 << shift[(m - (i1 + 1))]);
    }
    i >>= n;
    return i;
  }

  public int getPageObjectRef(int paramInt)
  {
    if ((this.obj == null) || (this.obj.length <= paramInt))
      return -1;
    return this.obj[paramInt];
  }

  public synchronized byte[] getObjData(int paramInt)
  {
    if (this.finishedReading)
      return null;
    Integer localInteger = Integer.valueOf(paramInt);
    if ((!this.startRefs.containsKey(localInteger)) || (!this.endRefs.containsKey(localInteger)))
      return null;
    int i = ((Integer)this.startRefs.get(localInteger)).intValue();
    int j = ((Integer)this.endRefs.get(localInteger)).intValue();
    int k = j - i + 1;
    long l = 0L;
    try
    {
      if (this.fos.isOpen())
        l = this.fos.size() - 200L;
    }
    catch (Exception localException1)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception: " + localException1.getMessage());
      l = 0L;
    }
    if ((l < j) || (j - i < 1))
      return null;
    byte[] arrayOfByte;
    try
    {
      synchronized (this.fos)
      {
        ByteBuffer localByteBuffer = ByteBuffer.allocateDirect(k);
        this.fos.read(localByteBuffer, i);
        localByteBuffer.clear();
        arrayOfByte = new byte[localByteBuffer.capacity()];
        localByteBuffer.get(arrayOfByte, 0, arrayOfByte.length);
      }
    }
    catch (Exception localException2)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception: " + localException2.getMessage());
      arrayOfByte = null;
    }
    return arrayOfByte;
  }

  public void storeOffset(int paramInt1, int paramInt2, int paramInt3)
  {
    this.startRefs.put(Integer.valueOf(paramInt1), Integer.valueOf(paramInt2));
    this.endRefs.put(Integer.valueOf(paramInt1), Integer.valueOf(paramInt3));
  }

  public void setFinishedReading()
  {
    this.finishedReading = true;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.io.LinearizedHintTable
 * JD-Core Version:    0.6.2
 */