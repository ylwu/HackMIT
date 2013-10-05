package org.jpedal.io.filter.ccitt;

import java.util.BitSet;
import org.jpedal.exception.PdfException;
import org.jpedal.objects.raw.PdfObject;
import org.jpedal.utils.LogWriter;

public class CCITTMix extends CCITT2D
  implements CCITTDecoder
{
  private int fillBits = 0;

  public CCITTMix(byte[] paramArrayOfByte, int paramInt1, int paramInt2, PdfObject paramPdfObject)
  {
    super(paramArrayOfByte, paramInt1, paramInt2, paramPdfObject);
    this.data = paramArrayOfByte;
    this.is2D = false;
  }

  public byte[] decode()
  {
    try
    {
      Object localObject1 = new int[this.width + 1];
      Object localObject2 = new int[this.width + 1];
      int[] arrayOfInt = new int[2];
      if (readEOL(true) != 1)
        throw new PdfException("TIFFFaxDecoder3");
      decode1DRun((int[])localObject2);
      for (int j = 1; j < this.height; j++)
        if (readEOL(false) == 0)
        {
          Object localObject3 = localObject1;
          localObject1 = localObject2;
          localObject2 = localObject3;
          set2D((int[])localObject1, (int[])localObject2, this.changingElemSize, arrayOfInt);
          localObject2[(this.currIndex++)] = this.bitOffset;
          this.changingElemSize = this.currIndex;
        }
        else
        {
          decode1DRun((int[])localObject2);
        }
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception: " + localException.getMessage());
    }
    byte[] arrayOfByte = createOutputFromBitset();
    if (!this.BlackIs1)
      for (int i = 0; i < arrayOfByte.length; i++)
        arrayOfByte[i] = ((byte)(255 - arrayOfByte[i]));
    return arrayOfByte;
  }

  void decode1DRun(int[] paramArrayOfInt)
    throws Exception
  {
    int i = 0;
    int i3 = 1;
    this.changingElemSize = 0;
    while (i < this.columns)
    {
      int n;
      int i1;
      int m;
      int j;
      int k;
      while (i3 != 0)
      {
        n = get1DBits(10);
        this.bitReached += 10;
        i1 = white[n];
        m = i1 & 0x1;
        j = i1 >>> 1 & 0xF;
        if (j == 12)
        {
          int i2 = get1DBits(2);
          this.bitReached += 2;
          n = n << 2 & 0xC | i2;
          i1 = additionalMakeup[n];
          j = i1 >>> 1 & 0x7;
          k = i1 >>> 4 & 0xFFF;
          i += k;
          this.outPtr += k;
          this.bitReached -= 4 - j;
        }
        else
        {
          if ((j == 0) || (j == 15))
            throw new Exception("1Derror");
          k = i1 >>> 5 & 0x7FF;
          i += k;
          this.bitReached -= 10 - j;
          if (m == 0)
          {
            i3 = 0;
            paramArrayOfInt[(this.changingElemSize++)] = i;
          }
          this.outPtr += k;
        }
      }
      if (i != this.columns)
      {
        while (i3 == 0)
        {
          n = get1DBits(4);
          i1 = initBlack[n];
          this.bitReached += 4;
          j = i1 >>> 1 & 0xF;
          k = i1 >>> 5 & 0x7FF;
          if (k == 100)
          {
            n = get1DBits(9);
            this.bitReached += 9;
            i1 = black[n];
            m = i1 & 0x1;
            j = i1 >>> 1 & 0xF;
            k = i1 >>> 5 & 0x7FF;
            if (j == 12)
            {
              this.bitReached -= 5;
              n = get1DBits(4);
              this.bitReached += 4;
              i1 = additionalMakeup[n];
              j = i1 >>> 1 & 0x7;
              k = i1 >>> 4 & 0xFFF;
              this.out.set(this.outPtr, this.outPtr + k, true);
              this.outPtr += k;
              i += k;
              this.bitReached -= 4 - j;
            }
            else
            {
              if (j == 15)
                throw new PdfException("1D error");
              this.out.set(this.outPtr, this.outPtr + k, true);
              this.outPtr += k;
              i += k;
              this.bitReached -= 9 - j;
              if (m == 0)
              {
                i3 = 1;
                paramArrayOfInt[(this.changingElemSize++)] = i;
              }
            }
          }
          else if (k == 200)
          {
            n = get1DBits(2);
            this.bitReached += 2;
            i1 = twoBitBlack[n];
            k = i1 >>> 5 & 0x7FF;
            j = i1 >>> 1 & 0xF;
            this.out.set(this.outPtr, this.outPtr + k, true);
            this.outPtr += k;
            i += k;
            this.bitReached -= 2 - j;
            i3 = 1;
            paramArrayOfInt[(this.changingElemSize++)] = i;
          }
          else
          {
            this.out.set(this.outPtr, this.outPtr + k, true);
            this.outPtr += k;
            i += k;
            this.bitReached -= 4 - j;
            i3 = 1;
            paramArrayOfInt[(this.changingElemSize++)] = i;
          }
        }
        if (i == this.columns)
          break;
      }
    }
    paramArrayOfInt[(this.changingElemSize++)] = i;
  }

  private int readEOL(boolean paramBoolean)
    throws PdfException
  {
    int j;
    if (this.fillBits == 0)
    {
      i = get1DBits(12);
      this.bitReached += 12;
      if ((paramBoolean) && (i == 0))
      {
        j = get1DBits(4);
        this.bitReached += 4;
        if (j == 1)
        {
          this.fillBits = 1;
          this.bitReached += 1;
          return 1;
        }
      }
      if (i != 1)
        throw new PdfException("EOL error1");
    }
    else if (this.fillBits == 1)
    {
      i = 8 - (this.bitReached & 0x7);
      j = get1DBits(i);
      this.bitReached += i;
      if (j != 0)
        throw new PdfException("EOL error2");
      if (i < 4)
      {
        k = get1DBits(8);
        this.bitReached += 8;
        if (k != 0)
          throw new PdfException("EOL error3");
      }
      int k = get1DBits(8);
      for (this.bitReached += 8; k != 1; this.bitReached += 8)
      {
        if (k != 0)
          throw new PdfException("EOL error4");
        k = get1DBits(8);
      }
    }
    int i = get1DBits(1);
    this.bitReached += 1;
    return i;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.io.filter.ccitt.CCITTMix
 * JD-Core Version:    0.6.2
 */