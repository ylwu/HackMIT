package org.jpedal.utils;

public class Sorts
{
  public static final int[] quicksort(int[] paramArrayOfInt1, float[] paramArrayOfFloat1, float[] paramArrayOfFloat2, int[] paramArrayOfInt2)
  {
    int[] arrayOfInt = ObjectCloneFactory.cloneArray(paramArrayOfInt1);
    float[] arrayOfFloat1 = ObjectCloneFactory.cloneArray(paramArrayOfFloat1);
    float[] arrayOfFloat2 = ObjectCloneFactory.cloneArray(paramArrayOfFloat2);
    int i = 0;
    int j = paramArrayOfInt2.length - 1;
    for (int k = i + 1; k <= j; k++)
    {
      int i1 = arrayOfInt[k];
      float f1 = arrayOfFloat1[k];
      float f2 = arrayOfFloat2[k];
      int i2 = paramArrayOfInt2[k];
      int n = i;
      for (int m = k - 1; m >= i; m--)
        if (((i1 < arrayOfInt[m] ? 1 : 0) | (i1 == arrayOfInt[m] ? 1 : 0) & ((f1 > arrayOfFloat1[m] ? 1 : 0) | (i1 == arrayOfInt[m] ? 1 : 0) & (f1 == arrayOfFloat1[m] ? 1 : 0) & (f2 > arrayOfFloat2[m] ? 1 : 0))) != 0)
        {
          arrayOfInt[(m + 1)] = arrayOfInt[m];
          arrayOfFloat1[(m + 1)] = arrayOfFloat1[m];
          arrayOfFloat2[(m + 1)] = arrayOfFloat2[m];
          paramArrayOfInt2[(m + 1)] = paramArrayOfInt2[m];
        }
        else
        {
          n = m + 1;
          break;
        }
      arrayOfInt[n] = i1;
      arrayOfFloat1[n] = f1;
      arrayOfFloat2[n] = f2;
      paramArrayOfInt2[n] = i2;
    }
    return paramArrayOfInt2;
  }

  private static void sift(int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt1, int paramInt2)
  {
    int i = paramInt1;
    int j = paramArrayOfInt1[i];
    int k = paramArrayOfInt2[i];
    int m = 2 * paramInt1 + 1;
    if ((m < paramInt2) && (paramArrayOfInt1[m] < paramArrayOfInt1[(m + 1)]))
      m += 1;
    while ((m <= paramInt2) && (j < paramArrayOfInt1[m]))
    {
      paramArrayOfInt1[i] = paramArrayOfInt1[m];
      paramArrayOfInt2[i] = paramArrayOfInt2[m];
      i = m;
      m = 2 * m + 1;
      if ((m < paramInt2) && (paramArrayOfInt1[m] < paramArrayOfInt1[(m + 1)]))
        m += 1;
    }
    paramArrayOfInt1[i] = j;
    paramArrayOfInt2[i] = k;
  }

  public static final int[] quicksort(int[] paramArrayOfInt1, int[] paramArrayOfInt2)
  {
    int i = paramArrayOfInt1.length;
    int[] arrayOfInt = new int[i];
    System.arraycopy(paramArrayOfInt1, 0, arrayOfInt, 0, i);
    int j = arrayOfInt.length / 2;
    int k = arrayOfInt.length - 1;
    while (j > 0)
    {
      j -= 1;
      sift(arrayOfInt, paramArrayOfInt2, j, k);
    }
    while (k > 0)
    {
      int m = arrayOfInt[0];
      int n = paramArrayOfInt2[0];
      arrayOfInt[0] = arrayOfInt[k];
      paramArrayOfInt2[0] = paramArrayOfInt2[k];
      arrayOfInt[k] = m;
      paramArrayOfInt2[k] = n;
      k -= 1;
      sift(arrayOfInt, paramArrayOfInt2, j, k);
    }
    return paramArrayOfInt2;
  }

  public static final int[] quicksort(int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3)
  {
    int i = paramArrayOfInt3.length;
    int[] arrayOfInt1 = new int[i];
    int[] arrayOfInt2 = new int[i];
    for (int j = 0; j < i; j++)
    {
      arrayOfInt1[j] = paramArrayOfInt1[j];
      arrayOfInt2[j] = paramArrayOfInt2[j];
    }
    j = 0;
    int k = i;
    for (int m = j + 1; m < k; m++)
    {
      int i2 = arrayOfInt1[m];
      int i3 = arrayOfInt2[m];
      int i4 = paramArrayOfInt3[m];
      int i1 = j;
      for (int n = m - 1; n >= j; n--)
        if (((i2 < arrayOfInt1[n] ? 1 : 0) | (i2 == arrayOfInt1[n] ? 1 : 0) & (i3 < arrayOfInt2[n] ? 1 : 0)) != 0)
        {
          arrayOfInt1[(n + 1)] = arrayOfInt1[n];
          arrayOfInt2[(n + 1)] = arrayOfInt2[n];
          paramArrayOfInt3[(n + 1)] = paramArrayOfInt3[n];
        }
        else
        {
          i1 = n + 1;
          break;
        }
      arrayOfInt1[i1] = i2;
      arrayOfInt2[i1] = i3;
      paramArrayOfInt3[i1] = i4;
    }
    return paramArrayOfInt3;
  }

  public static final String[] quicksort(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3, String[] paramArrayOfString)
  {
    int[] arrayOfInt1 = ObjectCloneFactory.cloneArray(paramArrayOfInt1);
    int[] arrayOfInt2 = ObjectCloneFactory.cloneArray(paramArrayOfInt2);
    int[] arrayOfInt3 = ObjectCloneFactory.cloneArray(paramArrayOfInt3);
    for (int i = paramInt1 + 1; i <= paramInt2; i++)
    {
      int m = arrayOfInt1[i];
      int n = arrayOfInt2[i];
      int i1 = arrayOfInt3[i];
      String str = paramArrayOfString[i];
      int k = paramInt1;
      for (int j = i - 1; j >= paramInt1; j--)
        if (((m < arrayOfInt1[j] ? 1 : 0) | (m == arrayOfInt1[j] ? 1 : 0) & ((n < arrayOfInt2[j] ? 1 : 0) | (m == arrayOfInt1[j] ? 1 : 0) & (n == arrayOfInt2[j] ? 1 : 0) & (i1 < arrayOfInt3[j] ? 1 : 0))) != 0)
        {
          arrayOfInt1[(j + 1)] = arrayOfInt1[j];
          arrayOfInt2[(j + 1)] = arrayOfInt2[j];
          arrayOfInt3[(j + 1)] = arrayOfInt3[j];
          paramArrayOfString[(j + 1)] = paramArrayOfString[j];
        }
        else
        {
          k = j + 1;
          break;
        }
      arrayOfInt1[k] = m;
      arrayOfInt2[k] = n;
      arrayOfInt3[k] = i1;
      paramArrayOfString[k] = str;
    }
    return paramArrayOfString;
  }

  public static final int[] quicksort(int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3, int[] paramArrayOfInt4)
  {
    int[] arrayOfInt1 = ObjectCloneFactory.cloneArray(paramArrayOfInt1);
    int[] arrayOfInt2 = ObjectCloneFactory.cloneArray(paramArrayOfInt2);
    int[] arrayOfInt3 = ObjectCloneFactory.cloneArray(paramArrayOfInt3);
    int i = 0;
    int j = paramArrayOfInt4.length - 1;
    for (int k = i + 1; k <= j; k++)
    {
      int i1 = arrayOfInt1[k];
      int i2 = arrayOfInt2[k];
      int i3 = arrayOfInt3[k];
      int i4 = paramArrayOfInt4[k];
      int n = i;
      for (int m = k - 1; m >= i; m--)
        if (((i1 < arrayOfInt1[m] ? 1 : 0) | (i1 == arrayOfInt1[m] ? 1 : 0) & ((i2 > arrayOfInt2[m] ? 1 : 0) | (i1 == arrayOfInt1[m] ? 1 : 0) & (i2 == arrayOfInt2[m] ? 1 : 0) & (i3 > arrayOfInt3[m] ? 1 : 0))) != 0)
        {
          arrayOfInt1[(m + 1)] = arrayOfInt1[m];
          arrayOfInt2[(m + 1)] = arrayOfInt2[m];
          arrayOfInt3[(m + 1)] = arrayOfInt3[m];
          paramArrayOfInt4[(m + 1)] = paramArrayOfInt4[m];
        }
        else
        {
          n = m + 1;
          break;
        }
      arrayOfInt1[n] = i1;
      arrayOfInt2[n] = i2;
      arrayOfInt3[n] = i3;
      paramArrayOfInt4[n] = i4;
    }
    return paramArrayOfInt4;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.utils.Sorts
 * JD-Core Version:    0.6.2
 */