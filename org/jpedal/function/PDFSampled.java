package org.jpedal.function;

public class PDFSampled extends PDFGenericFunction
  implements PDFFunction
{
  private int[] size;
  private float[] samples;
  int returnValues;

  public PDFSampled(byte[] paramArrayOfByte, int paramInt, float[] paramArrayOfFloat1, float[] paramArrayOfFloat2, float[] paramArrayOfFloat3, float[] paramArrayOfFloat4, int[] paramArrayOfInt)
  {
    super(paramArrayOfFloat1, paramArrayOfFloat2);
    this.size = paramArrayOfInt;
    int i = paramInt;
    int j;
    int k;
    if (paramArrayOfFloat3 != null)
    {
      this.encode = paramArrayOfFloat3;
    }
    else
    {
      j = paramArrayOfInt.length;
      this.encode = new float[j * 2];
      for (k = 0; k < j; k++)
        this.encode[(k * 2 + 1)] = (paramArrayOfInt[k] - 1);
    }
    if (paramArrayOfFloat4 != null)
    {
      this.decode = paramArrayOfFloat4;
    }
    else
    {
      j = paramArrayOfFloat2.length;
      this.decode = new float[j];
      System.arraycopy(paramArrayOfFloat2, 0, this.decode, 0, j);
    }
    if (i == 8)
    {
      j = paramArrayOfByte.length;
      this.samples = new float[j];
      for (k = 0; k < j; k++)
        this.samples[k] = ((paramArrayOfByte[k] & 0xFF) / 256.0F);
    }
    else
    {
      int m;
      if (i == 4)
      {
        j = paramArrayOfByte.length * 2;
        this.samples = new float[j];
        k = 0;
        m = 0;
        while (m < j)
        {
          this.samples[m] = (((paramArrayOfByte[k] & 0xF0) >> 4) / 16.0F);
          this.samples[(m + 1)] = ((paramArrayOfByte[k] & 0xF) / 16.0F);
          k++;
          m += 2;
        }
      }
      else if (i == 2)
      {
        j = paramArrayOfByte.length * 4;
        this.samples = new float[j];
        k = 0;
        m = 0;
        while (m < j)
        {
          this.samples[m] = (((paramArrayOfByte[k] & 0xC0) >> 6) / 4.0F);
          this.samples[(m + 1)] = (((paramArrayOfByte[k] & 0x30) >> 4) / 4.0F);
          this.samples[(m + 2)] = (((paramArrayOfByte[k] & 0xC) >> 2) / 4.0F);
          this.samples[(m + 3)] = ((paramArrayOfByte[k] & 0x3) / 4.0F);
          k++;
          m += 4;
        }
      }
      else if (i == 1)
      {
        j = paramArrayOfByte.length * 8;
        this.samples = new float[j];
        k = 0;
        m = 0;
        while (m < j)
        {
          this.samples[m] = (((paramArrayOfByte[k] & 0x80) >> 7) / 2.0F);
          this.samples[(m + 1)] = (((paramArrayOfByte[k] & 0x40) >> 6) / 2.0F);
          this.samples[(m + 2)] = (((paramArrayOfByte[k] & 0x20) >> 5) / 2.0F);
          this.samples[(m + 3)] = (((paramArrayOfByte[k] & 0x10) >> 4) / 2.0F);
          this.samples[(m + 4)] = (((paramArrayOfByte[k] & 0x8) >> 3) / 2.0F);
          this.samples[(m + 5)] = (((paramArrayOfByte[k] & 0x4) >> 2) / 2.0F);
          this.samples[(m + 6)] = (((paramArrayOfByte[k] & 0x2) >> 1) / 2.0F);
          this.samples[(m + 7)] = ((paramArrayOfByte[k] & 0x1) / 2.0F);
          k++;
          m += 8;
        }
      }
      else
      {
        int i2;
        if (i == 12)
        {
          j = 16 / i;
          k = paramArrayOfByte.length * j * 2;
          this.samples = new float[k];
          m = 0;
          int n = 0;
          int i1 = (2 << i) - 1;
          for (i2 = 0; i2 < k; i2++)
          {
            for (int i3 = 0; i3 < j; i3++)
              this.samples[i2] = ((((paramArrayOfByte[m] << 8) + paramArrayOfByte[m] & i1 << 16 - i3 * i) >> 16 - i) / i1);
            while (n > 16)
            {
              m += 2;
              n -= 16;
            }
          }
        }
        else
        {
          j = i / 8;
          k = paramArrayOfByte.length / j;
          this.samples = new float[k];
          m = 0;
          long l1 = 1L;
          if (i == 16)
            l1 = 65536L;
          else if (i == 24)
            l1 = 16777216L;
          else if (i == 32)
            l1 = 429457408L;
          for (i2 = 0; i2 < k; i2++)
          {
            long l2 = 0L;
            for (int i4 = 0; i4 < j; i4++)
              l2 += ((paramArrayOfByte[(m + i4)] & 0xFF) << 8 * (j - i4 - 1));
            this.samples[i2] = ((float)l2 / (float)l1);
            m += j;
          }
        }
      }
    }
    this.returnValues = (paramArrayOfFloat2.length / 2);
  }

  public float[] computeStitch(float[] paramArrayOfFloat)
  {
    return compute(paramArrayOfFloat);
  }

  public float[] compute(float[] paramArrayOfFloat)
  {
    float[] arrayOfFloat1 = new float[this.returnValues];
    int i = this.domain.length / 2;
    int j = this.range.length / 2;
    if (j < i)
    {
      int k = paramArrayOfFloat.length;
      arrayOfFloat3 = new float[k];
      for (m = 0; m < k; m++)
        arrayOfFloat3[(k - m - 1)] = paramArrayOfFloat[m];
      paramArrayOfFloat = arrayOfFloat3;
    }
    float[] arrayOfFloat2 = new float[i * 2];
    float[] arrayOfFloat3 = paramArrayOfFloat;
    for (int m = 0; m < i; m++)
    {
      arrayOfFloat2[(m * 2)] = encodeInput(arrayOfFloat3[m], m);
      if (j == i)
      {
        arrayOfFloat1[m] = decodeSample(arrayOfFloat2[(m * 2)], m, j, 0);
      }
      else
      {
        int n;
        if (i < j)
          for (n = 0; n < j; n++)
            arrayOfFloat1[n] = decodeSample(arrayOfFloat2[(m * 2)], n, j, n);
        else if (j < i)
          for (n = 0; n < j; n++)
            arrayOfFloat1[n] = decodeSample(arrayOfFloat2[(m * 2)], n, j, n);
      }
    }
    return arrayOfFloat1;
  }

  private float encodeInput(float paramFloat, int paramInt)
  {
    paramFloat = min(max(paramFloat, this.domain[(paramInt * 2)]), this.domain[(paramInt * 2 + 1)]);
    paramFloat = interpolate(paramFloat, this.domain[(paramInt * 2)], this.domain[(paramInt * 2 + 1)], this.encode[(paramInt * 2)], this.encode[(paramInt * 2 + 1)]);
    paramFloat = min(max(paramFloat, 0.0F), this.size[paramInt] - 1);
    return paramFloat;
  }

  private float decodeSample(float paramFloat, int paramInt1, int paramInt2, int paramInt3)
  {
    int i = (int)paramFloat;
    if (paramFloat - (int)paramFloat > 0.0F)
      i = (int)paramFloat + 1;
    float f1 = i - paramFloat;
    float f2 = 1.0F - f1;
    int j = (int)paramFloat * paramInt2 + paramInt3;
    int k = i * paramInt2 + paramInt3;
    float f3 = f1 * this.samples[j] + f2 * this.samples[k];
    f3 = interpolate(f3, 0.0F, 1.0F, this.decode[(paramInt1 * 2)], this.decode[(paramInt1 * 2 + 1)]);
    f3 = min(max(f3, this.range[(paramInt1 * 2)]), this.range[(paramInt1 * 2 + 1)]);
    return f3;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.function.PDFSampled
 * JD-Core Version:    0.6.2
 */