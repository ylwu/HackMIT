package org.jpedal.io.filter;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import org.jpedal.io.ObjectStore;
import org.jpedal.objects.raw.PdfObject;
import org.jpedal.utils.LogWriter;

public class BaseFilter
{
  final PdfObject decodeParms;
  BufferedInputStream bis;
  BufferedOutputStream streamCache;
  Map cachedObjects;

  BaseFilter(PdfObject paramPdfObject)
  {
    this.decodeParms = paramPdfObject;
  }

  public boolean hasError()
  {
    return false;
  }

  void setupCachedObjectForDecoding(String paramString)
    throws IOException
  {
    File localFile1 = File.createTempFile("jpedal", ".raw", new File(ObjectStore.temp_dir));
    this.cachedObjects.put(localFile1.getAbsolutePath(), "x");
    ObjectStore.copy(paramString, localFile1.getAbsolutePath());
    File localFile2 = new File(paramString);
    localFile2.delete();
    this.streamCache = new BufferedOutputStream(new FileOutputStream(paramString));
    this.bis = new BufferedInputStream(new FileInputStream(localFile1));
  }

  byte[] applyPredictor(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3, int paramInt4)
    throws Exception
  {
    if (paramInt1 == 1)
      return paramArrayOfByte;
    int i = paramArrayOfByte == null ? 1 : 0;
    if (i != 0)
    {
      applyPredictorFunction(paramInt1, this.bis, this.streamCache, paramInt2, paramInt3, paramInt4);
      return null;
    }
    ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(paramArrayOfByte);
    BufferedInputStream localBufferedInputStream = new BufferedInputStream(localByteArrayInputStream);
    int j = applyPredictorFunction2(paramInt1, localBufferedInputStream, null, paramInt2, paramInt3, paramInt4);
    localByteArrayInputStream.close();
    localBufferedInputStream.close();
    byte[] arrayOfByte = new byte[j];
    localByteArrayInputStream = new ByteArrayInputStream(paramArrayOfByte);
    localBufferedInputStream = new BufferedInputStream(localByteArrayInputStream);
    applyPredictorFunction2(paramInt1, localBufferedInputStream, arrayOfByte, paramInt2, paramInt3, paramInt4);
    localByteArrayInputStream.close();
    localBufferedInputStream.close();
    return arrayOfByte;
  }

  private static void applyPredictorFunction(int paramInt1, BufferedInputStream paramBufferedInputStream, OutputStream paramOutputStream, int paramInt2, int paramInt3, int paramInt4)
    throws Exception
  {
    int j = paramBufferedInputStream.available();
    int k = (paramInt2 * paramInt3 + 7) / 8;
    int m = (paramInt4 * paramInt2 * paramInt3 + 7) / 8 + k;
    byte[] arrayOfByte1 = new byte[m];
    byte[] arrayOfByte2 = new byte[m];
    try
    {
      int i1 = 0;
      while (j > i1)
      {
        int i = paramInt1;
        int i2 = 0;
        int i3 = k;
        int n;
        if (i >= 10)
        {
          n = paramBufferedInputStream.read();
          if (n == -1)
            break;
          n += 10;
        }
        else
        {
          n = i;
        }
        while (i3 < m)
        {
          i2 = paramBufferedInputStream.read(arrayOfByte1, i3, m - i3);
          if (i2 == -1)
            break;
          i3 += i2;
          i1 += i2;
        }
        if (i2 == -1)
          break;
        int i4;
        int i5;
        int i6;
        switch (n)
        {
        case 2:
          for (i4 = k; i4 < m; i4++)
          {
            i5 = arrayOfByte1[i4] & 0xFF;
            i6 = arrayOfByte1[(i4 - k)] & 0xFF;
            arrayOfByte1[i4] = ((byte)(i5 + i6 & 0xFF));
            paramOutputStream.write(arrayOfByte1[i4]);
          }
          break;
        case 10:
          for (i4 = k; i4 < m; i4++)
            paramOutputStream.write(arrayOfByte1[i4]);
          break;
        case 11:
          for (i4 = k; i4 < m; i4++)
          {
            i5 = arrayOfByte1[i4] & 0xFF;
            i6 = arrayOfByte1[(i4 - k)] & 0xFF;
            arrayOfByte1[i4] = ((byte)(i5 + i6));
            paramOutputStream.write(arrayOfByte1[i4]);
          }
          break;
        case 12:
          for (i4 = k; i4 < m; i4++)
          {
            i5 = (arrayOfByte2[i4] & 0xFF) + (arrayOfByte1[i4] & 0xFF);
            arrayOfByte1[i4] = ((byte)i5);
            paramOutputStream.write(arrayOfByte1[i4]);
          }
          break;
        case 13:
          for (i4 = k; i4 < m; i4++)
          {
            i5 = arrayOfByte1[i4] & 0xFF;
            i6 = (arrayOfByte1[(i4 - k)] & 0xFF) + (arrayOfByte2[i4] & 0xFF) >> 1;
            arrayOfByte1[i4] = ((byte)(i5 + i6));
            paramOutputStream.write(arrayOfByte1[i4]);
          }
          break;
        case 14:
          for (i4 = k; i4 < m; i4++)
          {
            i5 = arrayOfByte1[(i4 - k)] & 0xFF;
            i6 = arrayOfByte2[i4] & 0xFF;
            int i7 = arrayOfByte2[(i4 - k)] & 0xFF;
            int i8 = i5 + i6 - i7;
            int i9 = i8 - i5;
            int i10 = i8 - i6;
            int i11 = i8 - i7;
            if (i9 < 0)
              i9 = -i9;
            if (i10 < 0)
              i10 = -i10;
            if (i11 < 0)
              i11 = -i11;
            if ((i9 <= i10) && (i9 <= i11))
              arrayOfByte1[i4] = ((byte)(arrayOfByte1[i4] + i5));
            else if (i10 <= i11)
              arrayOfByte1[i4] = ((byte)(arrayOfByte1[i4] + i6));
            else
              arrayOfByte1[i4] = ((byte)(arrayOfByte1[i4] + i7));
            paramOutputStream.write(arrayOfByte1[i4]);
          }
          break;
        case 15:
          break;
        case 3:
        case 4:
        case 5:
        case 6:
        case 7:
        case 8:
        case 9:
        }
        System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, arrayOfByte2.length);
      }
      paramOutputStream.flush();
      paramOutputStream.close();
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception " + localException + " accessing Predictor");
    }
  }

  private static int applyPredictorFunction2(int paramInt1, BufferedInputStream paramBufferedInputStream, byte[] paramArrayOfByte, int paramInt2, int paramInt3, int paramInt4)
    throws Exception
  {
    int i = 0;
    int k = paramBufferedInputStream.available();
    int m = (paramInt2 * paramInt3 + 7) / 8;
    int n = (paramInt4 * paramInt2 * paramInt3 + 7) / 8 + m;
    byte[] arrayOfByte1 = new byte[n];
    byte[] arrayOfByte2 = new byte[n];
    try
    {
      int i2 = 0;
      while (k > i2)
      {
        int j = paramInt1;
        int i3 = 0;
        int i4 = m;
        int i1;
        if (j >= 10)
        {
          i1 = paramBufferedInputStream.read();
          if (i1 == -1)
            break;
          i1 += 10;
        }
        else
        {
          i1 = j;
        }
        while (i4 < n)
        {
          i3 = paramBufferedInputStream.read(arrayOfByte1, i4, n - i4);
          if (i3 == -1)
            break;
          i4 += i3;
          i2 += i3;
        }
        if (i3 == -1)
          break;
        int i5;
        int i6;
        int i7;
        switch (i1)
        {
        case 2:
          for (i5 = m; i5 < n; i5++)
          {
            i6 = arrayOfByte1[i5] & 0xFF;
            i7 = arrayOfByte1[(i5 - m)] & 0xFF;
            arrayOfByte1[i5] = ((byte)(i6 + i7 & 0xFF));
            if (paramArrayOfByte != null)
              paramArrayOfByte[i] = arrayOfByte1[i5];
            i++;
          }
          break;
        case 10:
          for (i5 = m; i5 < n; i5++)
          {
            if (paramArrayOfByte != null)
              paramArrayOfByte[i] = arrayOfByte1[i5];
            i++;
          }
          break;
        case 11:
          for (i5 = m; i5 < n; i5++)
          {
            i6 = arrayOfByte1[i5] & 0xFF;
            i7 = arrayOfByte1[(i5 - m)] & 0xFF;
            arrayOfByte1[i5] = ((byte)(i6 + i7));
            if (paramArrayOfByte != null)
              paramArrayOfByte[i] = arrayOfByte1[i5];
            i++;
          }
          break;
        case 12:
          for (i5 = m; i5 < n; i5++)
          {
            i6 = (arrayOfByte2[i5] & 0xFF) + (arrayOfByte1[i5] & 0xFF);
            arrayOfByte1[i5] = ((byte)i6);
            if (paramArrayOfByte != null)
              paramArrayOfByte[i] = arrayOfByte1[i5];
            i++;
          }
          break;
        case 13:
          for (i5 = m; i5 < n; i5++)
          {
            i6 = arrayOfByte1[i5] & 0xFF;
            i7 = (arrayOfByte1[(i5 - m)] & 0xFF) + (arrayOfByte2[i5] & 0xFF) >> 1;
            arrayOfByte1[i5] = ((byte)(i6 + i7));
            if (paramArrayOfByte != null)
              paramArrayOfByte[i] = arrayOfByte1[i5];
            i++;
          }
          break;
        case 14:
          for (i5 = m; i5 < n; i5++)
          {
            i6 = arrayOfByte1[(i5 - m)] & 0xFF;
            i7 = arrayOfByte2[i5] & 0xFF;
            int i8 = arrayOfByte2[(i5 - m)] & 0xFF;
            int i9 = i6 + i7 - i8;
            int i10 = i9 - i6;
            int i11 = i9 - i7;
            int i12 = i9 - i8;
            if (i10 < 0)
              i10 = -i10;
            if (i11 < 0)
              i11 = -i11;
            if (i12 < 0)
              i12 = -i12;
            if ((i10 <= i11) && (i10 <= i12))
              arrayOfByte1[i5] = ((byte)(arrayOfByte1[i5] + i6));
            else if (i11 <= i12)
              arrayOfByte1[i5] = ((byte)(arrayOfByte1[i5] + i7));
            else
              arrayOfByte1[i5] = ((byte)(arrayOfByte1[i5] + i8));
            if (paramArrayOfByte != null)
              paramArrayOfByte[i] = arrayOfByte1[i5];
            i++;
          }
          break;
        case 15:
          break;
        case 3:
        case 4:
        case 5:
        case 6:
        case 7:
        case 8:
        case 9:
        }
        System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, arrayOfByte2.length);
      }
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception " + localException + " accessing Predictor");
    }
    return i;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.io.filter.BaseFilter
 * JD-Core Version:    0.6.2
 */