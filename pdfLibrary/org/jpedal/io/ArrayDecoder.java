package org.jpedal.io;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import org.jpedal.exception.PdfSecurityException;
import org.jpedal.objects.raw.OCObject;
import org.jpedal.objects.raw.PdfObject;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.NumberUtils;
import org.jpedal.utils.StringUtils;

public class ArrayDecoder extends ObjectDecoder
{
  private float[] floatValues = null;
  private int[] intValues = null;
  private double[] doubleValues = null;
  private byte[][] mixedValues = (byte[][])null;
  private byte[][] keyValues = (byte[][])null;
  private byte[][] stringValues = (byte[][])null;
  private boolean[] booleanValues = null;
  private Object[] objectValues = null;
  private int i;
  private int endPoint;
  private int type;
  private int keyReached = -1;
  private Object[] objectValuesArray = null;

  public ArrayDecoder(PdfFileReader paramPdfFileReader, int paramInt1, int paramInt2, int paramInt3)
  {
    super(paramPdfFileReader);
    this.i = paramInt1;
    this.endPoint = paramInt2;
    this.type = paramInt3;
  }

  public ArrayDecoder(PdfFileReader paramPdfFileReader, int paramInt1, int paramInt2, int paramInt3, Object[] paramArrayOfObject, int paramInt4)
  {
    super(paramPdfFileReader);
    this.i = paramInt1;
    this.endPoint = paramInt2;
    this.type = paramInt3;
    this.objectValuesArray = paramArrayOfObject;
    this.keyReached = paramInt4;
  }

  public int readArray(boolean paramBoolean, byte[] paramArrayOfByte, PdfObject paramPdfObject, int paramInt)
  {
    if (((this.type != 14) || (paramArrayOfByte[this.i] != 60)) && (paramArrayOfByte[this.i] != 91) && (paramArrayOfByte[this.i] != 60))
      this.i += 1;
    if ((paramArrayOfByte[this.i] == 91) && (paramArrayOfByte[(this.i + 1)] == 93))
      return this.i + 1;
    HashMap localHashMap = new HashMap();
    int j = (paramInt == 456733763) || (paramInt == 1044338049) ? 1 : 0;
    int k = 0;
    int m = 0;
    while ((paramArrayOfByte[this.i] == 10) || (paramArrayOfByte[this.i] == 13) || (paramArrayOfByte[this.i] == 32))
      this.i += 1;
    if (paramArrayOfByte[this.i] == 37)
      skipComment(paramArrayOfByte);
    int n = this.i;
    int i1 = this.i;
    byte[] arrayOfByte = paramArrayOfByte;
    boolean bool1 = (paramArrayOfByte[this.i] != 91) && (paramArrayOfByte[this.i] != 40) && (paramInt != 489767739) && (paramInt != 9250) && (paramArrayOfByte[0] != 0);
    if ((paramArrayOfByte[this.i] == 110) && (paramArrayOfByte[(this.i + 1)] == 117) && (paramArrayOfByte[(this.i + 2)] == 108) && (paramArrayOfByte[(this.i + 2)] == 108))
    {
      bool1 = false;
      m = 1;
    }
    if (bool1)
      bool1 = handleIndirect(this.endPoint, paramArrayOfByte);
    boolean bool2 = false;
    int i2 = 0;
    int i3 = 1;
    int i4 = -1;
    if (((paramArrayOfByte[this.i] == 47) || (paramArrayOfByte[this.i] == 40) || (paramArrayOfByte[this.i] == 60) || ((paramArrayOfByte[this.i] == 60) && (paramArrayOfByte[(this.i + 1)] == 102) && (paramArrayOfByte[(this.i + 2)] == 101) && (paramArrayOfByte[(this.i + 3)] == 102) && (paramArrayOfByte[(this.i + 4)] == 102))) && (this.type != 20) && (paramInt != 9250))
    {
      m = 1;
      bool2 = true;
    }
    else
    {
      int i5 = -1;
      int i7;
      if (bool1)
      {
        i6 = this.i;
        while ((paramArrayOfByte[this.i] != 10) && (paramArrayOfByte[this.i] != 13) && (paramArrayOfByte[this.i] != 32) && (paramArrayOfByte[this.i] != 47) && (paramArrayOfByte[this.i] != 60) && (paramArrayOfByte[this.i] != 62))
          this.i += 1;
        i7 = NumberUtils.parseInt(n, this.i, paramArrayOfByte);
        while ((paramArrayOfByte[this.i] == 10) || (paramArrayOfByte[this.i] == 13) || (paramArrayOfByte[this.i] == 32) || (paramArrayOfByte[this.i] == 47) || (paramArrayOfByte[this.i] == 60))
          this.i += 1;
        n = this.i;
        while ((paramArrayOfByte[this.i] != 10) && (paramArrayOfByte[this.i] != 13) && (paramArrayOfByte[this.i] != 32) && (paramArrayOfByte[this.i] != 47) && (paramArrayOfByte[this.i] != 60) && (paramArrayOfByte[this.i] != 62))
          this.i += 1;
        i8 = NumberUtils.parseInt(n, this.i, paramArrayOfByte);
        while ((paramArrayOfByte[this.i] == 10) || (paramArrayOfByte[this.i] == 13) || (paramArrayOfByte[this.i] == 32) || (paramArrayOfByte[this.i] == 47) || (paramArrayOfByte[this.i] == 60))
          this.i += 1;
        if (paramArrayOfByte[this.i] != 82)
          throw new RuntimeException(padding + "4. Unexpected value " + (char)paramArrayOfByte[this.i] + " in file - please send to IDRsolutions for analysis");
        if ((paramBoolean) && (j == 0))
          return this.i;
        arrayOfByte = this.objectReader.readObjectAsByteArray(paramPdfObject, this.objectReader.isCompressed(i7, i8), i7, i8);
        if (arrayOfByte == null)
        {
          paramPdfObject.setFullyResolved(false);
          if (LogWriter.isOutput())
            LogWriter.writeLog("[Linearized] " + paramPdfObject.getObjectRefAsString() + " not yet available (14)");
          return paramArrayOfByte.length;
        }
        for (i1 = 0; arrayOfByte[i1] != 91; i1++)
        {
          if (arrayOfByte[i1] == 37)
          {
            while (true)
            {
              i1++;
              if (arrayOfByte[i1] != 13)
                if (arrayOfByte[i1] == 10)
                  break;
            }
            while ((arrayOfByte[i1] == 13) || (arrayOfByte[i1] == 10))
              i1++;
            i1--;
          }
          if (((arrayOfByte[i1] == 110) && (arrayOfByte[(i1 + 1)] == 117) && (arrayOfByte[(i1 + 2)] == 108) && (arrayOfByte[(i1 + 3)] == 108)) || ((arrayOfByte[i1] == 101) && (arrayOfByte[(i1 + 1)] == 110) && (arrayOfByte[(i1 + 2)] == 100) && (arrayOfByte[(i1 + 3)] == 111)))
            break;
          if (arrayOfByte[i1] == 47)
          {
            i1--;
            i2 = 1;
            break;
          }
          if (((arrayOfByte[i1] == 60) && (arrayOfByte[(i1 + 1)] == 60)) || ((i1 + 4 < arrayOfByte.length) && (arrayOfByte[(i1 + 3)] == 60) && (arrayOfByte[(i1 + 4)] == 60)))
          {
            i5 = this.i;
            i1 = i6;
            arrayOfByte = paramArrayOfByte;
            break;
          }
        }
      }
      if (i1 < 0)
        i1 = 0;
      int i6 = 0;
      while ((arrayOfByte[i1] == 10) || (arrayOfByte[i1] == 13) || (arrayOfByte[i1] == 32) || ((arrayOfByte[i1] == 91) && (i6 == 0)))
      {
        if (arrayOfByte[i1] == 91)
          i6 = 1;
        i1++;
      }
      i4 = i1;
      int i8 = 1;
      int i10 = arrayOfByte.length;
      while ((i4 < i10) && (arrayOfByte[i4] != 93) && ((arrayOfByte[i1] != 101) || (arrayOfByte[(i1 + 1)] != 110) || (arrayOfByte[(i1 + 2)] != 100) || (arrayOfByte[(i1 + 3)] != 111)))
      {
        int i9 = 0;
        int i11;
        while ((arrayOfByte[i4] == 60) && (arrayOfByte[(i4 + 1)] == 60))
        {
          i11 = 1;
          m++;
          while (i11 > 0)
          {
            i4++;
            if ((arrayOfByte[i4] == 60) && (arrayOfByte[(i4 + 1)] == 60))
            {
              i4++;
              i11++;
            }
            else if ((arrayOfByte[i4] == 62) && (arrayOfByte[(i4 - 1)] == 62))
            {
              i4++;
              i11--;
            }
          }
          if (this.type == 14)
            i4--;
        }
        if ((this.type != 18) && (arrayOfByte[i4] == 110) && (arrayOfByte[(i4 + 1)] == 117) && (arrayOfByte[(i4 + 2)] == 108) && (arrayOfByte[(i4 + 3)] == 108))
        {
          i11 = 93;
          if ((paramInt == 826881374) || (paramInt == 1110717793))
            for (int i12 = i4 + 3; i12 < arrayOfByte.length; i12++)
              if ((arrayOfByte[i12] != 10) && (arrayOfByte[i12] != 13) && (arrayOfByte[i12] != 32) && (arrayOfByte[i12] != 9))
              {
                i11 = arrayOfByte[i12];
                i12 = arrayOfByte.length;
              }
          if (i11 == 93)
          {
            i3 = 1;
            m = 1;
            break;
          }
          i3 = 0;
          i4 += 4;
          i8 = 1;
        }
        else
        {
          if (((i2 != 0) && ((arrayOfByte[i4] == 32) || (arrayOfByte[i4] == 13) || (arrayOfByte[i4] == 10))) || ((i5 != -1) && (i4 > i5)))
            break;
          if (this.type == 14)
          {
            if ((arrayOfByte[i4] == 82) || (((paramInt == 9250) || (paramInt == 1248888446)) && (arrayOfByte[i4] == 47)))
              m++;
          }
          else
          {
            if (arrayOfByte[i4] == 40)
            {
              m++;
              while ((arrayOfByte[i4] != 41) || (ObjectUtils.isEscaped(arrayOfByte, i4)))
              {
                i4++;
                i8 = 1;
              }
            }
            if (arrayOfByte[i4] == 60)
            {
              m++;
              while (arrayOfByte[i4] != 62)
              {
                i4++;
                i8 = 1;
              }
            }
            if (arrayOfByte[i4] == 91)
            {
              m++;
              i11 = 1;
              while (true)
              {
                i4++;
                if (i4 != arrayOfByte.length)
                {
                  if (arrayOfByte[i4] == 93)
                    i11--;
                  else if (arrayOfByte[i4] == 91)
                    i11++;
                  if (i11 == 0)
                    break;
                }
              }
              i9 = 1;
              i8 = 1;
            }
            else
            {
              i7 = (arrayOfByte[i4] == 10) || (arrayOfByte[i4] == 13) || (arrayOfByte[i4] == 32) || (arrayOfByte[i4] == 47) ? 1 : 0;
              if ((i8 != 0) && (i7 == 0))
                if (((this.type == 18) || (this.type == 22)) && (arrayOfByte[i4] == 82) && (arrayOfByte[(i4 - 1)] != 47))
                {
                  m--;
                  localHashMap.put(Integer.valueOf(m - 1), "x");
                }
                else
                {
                  m++;
                }
              i8 = i7;
            }
          }
          if ((i9 == 0) && (i4 < i10) && (arrayOfByte[i4] == 93) && (this.type != 14))
          {
            for (i11 = i4 - 1; (arrayOfByte[i11] == 13) || (arrayOfByte[i11] == 10) || (arrayOfByte[i11] == 32); i11--);
            if (arrayOfByte[i11] != 91)
              break;
            m = 0;
            break;
          }
          i4++;
        }
      }
      if (m != 0);
    }
    if ((paramBoolean) && (j == 0))
      return i4;
    initObjectArray(m);
    if ((arrayOfByte[i1] == 110) && (arrayOfByte[(i1 + 1)] == 117) && (arrayOfByte[(i1 + 2)] == 108) && (arrayOfByte[(i1 + 3)] == 108) && (i3 != 0) && ((this.type != 22) || (m == 1)))
    {
      i1 += 3;
      if (this.type == 18)
        this.mixedValues[k] = null;
      else if (this.type == 14)
        this.keyValues[k] = null;
      else if (this.type == 20)
        this.stringValues[k] = null;
      else if (this.type == 22)
        this.objectValues[k] = null;
    }
    else
    {
      i1 = setValue(paramBoolean, paramArrayOfByte, paramPdfObject, paramInt, localHashMap, k, m, i1, arrayOfByte, bool2, i4);
    }
    if (!bool1)
      this.i = i1;
    if (this.type == 10)
      paramPdfObject.setFloatArray(paramInt, this.floatValues);
    else if (this.type == 9)
      paramPdfObject.setIntArray(paramInt, this.intValues);
    else if (this.type == 12)
      paramPdfObject.setBooleanArray(paramInt, this.booleanValues);
    else if (this.type == 16)
      paramPdfObject.setDoubleArray(paramInt, this.doubleValues);
    else if (this.type == 18)
      paramPdfObject.setMixedArray(paramInt, this.mixedValues);
    else if (this.type == 14)
      setKeyArrayValue(paramPdfObject, paramInt, m);
    else if (this.type == 20)
      paramPdfObject.setStringArray(paramInt, this.stringValues);
    else if (this.type == 22)
      setObjectArrayValue(paramPdfObject, paramInt, this.objectValuesArray, this.keyReached);
    if ((this.i < paramArrayOfByte.length) && ((paramArrayOfByte[this.i] == 47) || (paramArrayOfByte[this.i] == 62) || ((paramArrayOfByte[this.i] >= 48) && (paramArrayOfByte[this.i] <= 57))))
      this.i -= 1;
    return this.i;
  }

  private int setValue(boolean paramBoolean1, byte[] paramArrayOfByte1, PdfObject paramPdfObject, int paramInt1, Map paramMap, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte2, boolean paramBoolean2, int paramInt5)
  {
    while (paramArrayOfByte2[paramInt4] != 93)
    {
      boolean bool = false;
      if ((paramInt5 > -1) && (paramInt4 >= paramInt5))
        break;
      while ((paramArrayOfByte2[paramInt4] == 10) || (paramArrayOfByte2[paramInt4] == 13) || (paramArrayOfByte2[paramInt4] == 32) || (paramArrayOfByte2[paramInt4] == 47))
        paramInt4++;
      int j = paramInt4;
      int k = paramArrayOfByte2[(paramInt4 - 1)] == 47 ? 1 : 0;
      int m = 0;
      int n;
      if ((this.type == 14) || (((this.type == 18) || (this.type == 22)) && ((paramMap.containsKey(Integer.valueOf(paramInt2))) || ((paramInt1 == 1110717793) && (paramArrayOfByte2[paramInt4] >= 48) && (paramArrayOfByte2[paramInt4] <= 57)) || ((paramArrayOfByte2[paramInt4] == 60) && (paramArrayOfByte2[(paramInt4 + 1)] == 60)))))
      {
        while ((paramArrayOfByte2[paramInt4] != 82) && (paramArrayOfByte2[paramInt4] != 93))
        {
          if ((paramArrayOfByte2[paramInt4] == 60) && (paramArrayOfByte2[(paramInt4 + 1)] == 60))
          {
            n = 1;
            while (n > 0)
            {
              paramInt4++;
              if ((paramArrayOfByte2[paramInt4] == 60) && (paramArrayOfByte2[(paramInt4 + 1)] == 60))
              {
                paramInt4++;
                n++;
              }
              else if ((paramArrayOfByte2[paramInt4] == 62) && (paramArrayOfByte2[(paramInt4 + 1)] == 62))
              {
                paramInt4++;
                n--;
              }
            }
          }
          if ((k != 0) && (paramInt1 == 9250) && (paramArrayOfByte2[(paramInt4 + 1)] == 32))
            break;
          paramInt4++;
        }
        paramInt4++;
      }
      else if (paramArrayOfByte2[paramInt4] == 40)
      {
        j = paramInt4 + 1;
        while ((paramArrayOfByte2[paramInt4] != 41) || (ObjectUtils.isEscaped(paramArrayOfByte2, paramInt4)))
          paramInt4++;
        bool = false;
      }
      else if ((paramArrayOfByte2[paramInt4] == 91) && (this.type == 18) && (paramInt1 == 826094945))
      {
        j = paramInt4;
        while (paramArrayOfByte2[paramInt4] != 93)
          paramInt4++;
        paramInt4++;
      }
      else
      {
        if (paramArrayOfByte2[paramInt4] == 60)
        {
          bool = true;
          j = paramInt4 + 1;
          while (paramArrayOfByte2[paramInt4] != 62)
          {
            if (paramArrayOfByte2[paramInt4] == 47)
              bool = false;
            paramInt4++;
          }
        }
        if ((paramArrayOfByte2[paramInt4] == 91) && (this.type == 22))
        {
          n = paramInt4 + 1;
          int i1 = 1;
          while (true)
          {
            n++;
            if (n != paramArrayOfByte2.length)
            {
              if (paramArrayOfByte2[n] == 93)
                i1--;
              else if (paramArrayOfByte2[n] == 91)
                i1++;
              if (i1 == 0)
                break;
            }
          }
          n++;
          ArrayDecoder localArrayDecoder = new ArrayDecoder(this.objectReader, paramInt4, n, this.type, this.objectValues, paramInt2);
          paramInt4 = localArrayDecoder.readArray(paramBoolean1, paramArrayOfByte2, paramPdfObject, paramInt1);
          if (paramArrayOfByte2[paramInt4] != 91)
            paramInt4++;
          m = 1;
          while ((paramInt4 < paramArrayOfByte2.length) && (paramArrayOfByte2[paramInt4] == 93))
            paramInt4++;
        }
        else
        {
          if ((k == 0) && (paramInt3 - paramInt2 == 1) && (this.type == 18));
          while (true)
            if ((paramArrayOfByte2[paramInt4] != 93) && (paramArrayOfByte2[paramInt4] != 47) && ((paramArrayOfByte2[paramInt4] != 62) || (paramArrayOfByte2[(paramInt4 + 1)] != 62)))
            {
              paramInt4++;
              continue;
              if ((this.type == 22) && (paramArrayOfByte2[paramInt4] == 110) && (paramArrayOfByte2[(paramInt4 + 1)] == 117) && (paramArrayOfByte2[(paramInt4 + 2)] == 108) && (paramArrayOfByte2[(paramInt4 + 3)] == 108))
              {
                paramInt4 += 4;
                this.objectValues[paramInt2] = null;
                paramInt2++;
                break;
              }
              while ((paramArrayOfByte2[paramInt4] != 10) && (paramArrayOfByte2[paramInt4] != 13) && (paramArrayOfByte2[paramInt4] != 32) && (paramArrayOfByte2[paramInt4] != 93) && (paramArrayOfByte2[paramInt4] != 47) && ((paramArrayOfByte2[paramInt4] != 62) || (paramArrayOfByte2[(paramInt4 + 1)] != 62)))
              {
                paramInt4++;
                if (paramInt4 == paramArrayOfByte2.length)
                  break;
              }
            }
        }
      }
      if (this.type == 10)
        this.floatValues[paramInt2] = NumberUtils.parseFloat(j, paramInt4, paramArrayOfByte2);
      else if (this.type == 9)
        this.intValues[paramInt2] = NumberUtils.parseInt(j, paramInt4, paramArrayOfByte2);
      else if (this.type == 12)
      {
        if ((paramArrayOfByte1[j] == 116) && (paramArrayOfByte1[(j + 1)] == 114) && (paramArrayOfByte1[(j + 2)] == 117) && (paramArrayOfByte1[(j + 3)] == 101))
          this.booleanValues[paramInt2] = true;
      }
      else if (this.type == 16)
        this.doubleValues[paramInt2] = NumberUtils.parseFloat(j, paramInt4, paramArrayOfByte2);
      else if (m == 0)
        paramInt4 = setObjectArrayValue(paramPdfObject, paramInt1, bool, paramInt2, paramInt3, paramInt4, paramArrayOfByte2, paramBoolean2, j);
      paramInt2++;
      if (paramInt2 == paramInt3)
        break;
    }
    return paramInt4;
  }

  private int setObjectArrayValue(PdfObject paramPdfObject, int paramInt1, boolean paramBoolean1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte, boolean paramBoolean2, int paramInt5)
  {
    if ((paramInt5 > 0) && (paramArrayOfByte[(paramInt5 - 1)] == 47))
      paramInt5--;
    if ((paramInt5 > 0) && (paramArrayOfByte[paramInt5] == 91) && (paramInt1 != 826094945))
      paramInt5++;
    while (((paramInt1 == 1110717793) || (paramInt1 == 826881374)) && (paramArrayOfByte[paramInt5] == 110) && (paramArrayOfByte[(paramInt5 + 1)] == 117) && (paramArrayOfByte[(paramInt5 + 2)] == 108) && (paramArrayOfByte[(paramInt5 + 3)] == 108))
    {
      paramInt5 += 4;
      while ((paramInt5 >= 0) && ((paramArrayOfByte[paramInt5] == 32) || (paramArrayOfByte[paramInt5] == 10) || (paramArrayOfByte[paramInt5] == 13) || (paramArrayOfByte[paramInt5] == 9)))
        paramInt5++;
    }
    while ((paramInt5 >= 0) && ((paramArrayOfByte[paramInt5] == 32) || (paramArrayOfByte[paramInt5] == 10) || (paramArrayOfByte[paramInt5] == 13) || (paramArrayOfByte[paramInt5] == 9)))
      paramInt5++;
    byte[] arrayOfByte = ObjectUtils.readEscapedValue(paramInt4, paramArrayOfByte, paramInt5, paramInt1 == 6420);
    if (paramInt4 != paramArrayOfByte.length)
      if (paramArrayOfByte[paramInt4] == 62)
      {
        paramInt4++;
      }
      else if (paramArrayOfByte[paramInt4] == 41)
      {
        paramInt4++;
        try
        {
          if ((!paramPdfObject.isInCompressedStream()) && (this.decryption != null))
            arrayOfByte = this.decryption.decrypt(arrayOfByte, paramPdfObject.getObjectRefAsString(), false, null, false, false);
        }
        catch (PdfSecurityException localPdfSecurityException)
        {
          if (LogWriter.isOutput())
            LogWriter.writeLog("Exception: " + localPdfSecurityException.getMessage());
        }
        if (paramInt1 == 1110717793)
          arrayOfByte = StringUtils.toBytes(StringUtils.getTextString(arrayOfByte, false));
      }
    if (paramBoolean2)
      this.i = paramInt4;
    if (this.type == 18)
      this.mixedValues[paramInt2] = arrayOfByte;
    else if (this.type == 14)
      this.keyValues[paramInt2] = ObjectUtils.convertReturnsToSpaces(arrayOfByte);
    else if (this.type == 20)
    {
      if (paramBoolean1)
        this.stringValues[paramInt2] = handleHexString(arrayOfByte);
      else
        this.stringValues[paramInt2] = arrayOfByte;
    }
    else if (this.type == 22)
      if (paramBoolean1)
        this.objectValues[paramInt2] = handleHexString(arrayOfByte);
      else
        this.objectValues[paramInt2] = arrayOfByte;
    return paramInt4;
  }

  private static byte[] handleHexString(byte[] paramArrayOfByte)
  {
    String str2 = new String(paramArrayOfByte);
    byte[] arrayOfByte = new byte[paramArrayOfByte.length / 2];
    int j = 0;
    while (j < paramArrayOfByte.length)
    {
      if (j + 2 <= paramArrayOfByte.length)
      {
        if (str2.charAt(j) == '\n')
          j++;
        String str1 = str2.substring(j, j + 2);
        arrayOfByte[(j / 2)] = ((byte)Integer.parseInt(str1, 16));
      }
      j += 2;
    }
    paramArrayOfByte = arrayOfByte;
    return paramArrayOfByte;
  }

  private void initObjectArray(int paramInt)
  {
    if (this.type == 10)
      this.floatValues = new float[paramInt];
    else if (this.type == 9)
      this.intValues = new int[paramInt];
    else if (this.type == 12)
      this.booleanValues = new boolean[paramInt];
    else if (this.type == 16)
      this.doubleValues = new double[paramInt];
    else if (this.type == 18)
      this.mixedValues = new byte[paramInt][];
    else if (this.type == 14)
      this.keyValues = new byte[paramInt][];
    else if (this.type == 20)
      this.stringValues = new byte[paramInt][];
    else if (this.type == 22)
      this.objectValues = new Object[paramInt];
  }

  private boolean handleIndirect(int paramInt, byte[] paramArrayOfByte)
  {
    boolean bool = true;
    int j = this.i;
    int k = paramArrayOfByte.length;
    while (paramArrayOfByte[j] != 93)
    {
      j++;
      if ((j < paramInt) && (j < k) && ((paramArrayOfByte[j] != 82) || ((paramArrayOfByte[(j - 1)] != 32) && (paramArrayOfByte[(j - 1)] != 10) && (paramArrayOfByte[(j - 1)] != 13))))
        if ((paramArrayOfByte[j] == 62) && (paramArrayOfByte[(j - 1)] == 62))
          bool = false;
        else if (paramArrayOfByte[j] == 47)
          bool = false;
    }
    return bool;
  }

  private void skipComment(byte[] paramArrayOfByte)
  {
    while ((paramArrayOfByte[this.i] != 10) && (paramArrayOfByte[this.i] != 13))
      this.i += 1;
    while ((paramArrayOfByte[this.i] == 10) || (paramArrayOfByte[this.i] == 13) || (paramArrayOfByte[this.i] == 32))
      this.i += 1;
  }

  private void setKeyArrayValue(PdfObject paramPdfObject, int paramInt1, int paramInt2)
  {
    if ((this.type == 14) && (paramInt2 == 1) && (paramInt1 == 1044338049))
    {
      byte[] arrayOfByte1 = this.keyValues[0];
      if (arrayOfByte1 != null)
      {
        int j = arrayOfByte1.length;
        if (arrayOfByte1[(j - 1)] == 82)
        {
          PdfObject localPdfObject = new PdfObject(new String(arrayOfByte1));
          byte[] arrayOfByte2 = this.objectReader.readObjectData(localPdfObject);
          if (arrayOfByte2 != null)
          {
            int k = 0;
            int m = arrayOfByte2.length;
            int n = 0;
            while (k < m)
            {
              k++;
              if (k != arrayOfByte2.length)
                if (arrayOfByte2[k] == 91)
                  n = 1;
                else if ((arrayOfByte2[(k - 1)] == 60) && (arrayOfByte2[k] == 60))
                  n = 0;
            }
            if (n != 0)
            {
              ArrayDecoder localArrayDecoder = new ArrayDecoder(this.objectReader, k, m, 14);
              localArrayDecoder.readArray(false, arrayOfByte2, paramPdfObject, paramInt1);
            }
            else
            {
              paramPdfObject.setKeyArray(paramInt1, this.keyValues);
            }
          }
        }
      }
    }
    else
    {
      paramPdfObject.setKeyArray(paramInt1, this.keyValues);
    }
  }

  private void setObjectArrayValue(PdfObject paramPdfObject, int paramInt1, Object[] paramArrayOfObject, int paramInt2)
  {
    if ((paramInt1 == 1110717793) && (this.objectValues != null) && (this.objectValues.length == 1) && ((this.objectValues[0] instanceof byte[])))
    {
      byte[] arrayOfByte1 = (byte[])this.objectValues[0];
      int j = arrayOfByte1.length;
      if (arrayOfByte1[(j - 1)] == 82)
      {
        OCObject localOCObject = new OCObject(new String(arrayOfByte1));
        byte[] arrayOfByte2 = this.objectReader.readObjectData(localOCObject);
        int k = 0;
        int m = arrayOfByte2.length;
        int n = 0;
        while (k < m)
        {
          k++;
          if (k != arrayOfByte2.length)
            if (arrayOfByte2[k] == 91)
              n = 1;
        }
        if (n != 0)
        {
          ArrayDecoder localArrayDecoder = new ArrayDecoder(this.objectReader, k, m, 22);
          localArrayDecoder.readArray(false, arrayOfByte2, paramPdfObject, paramInt1);
        }
        this.objectValues = null;
      }
    }
    if (paramArrayOfObject != null)
      paramArrayOfObject[paramInt2] = this.objectValues;
    else if (this.objectValues != null)
      paramPdfObject.setObjectArray(paramInt1, this.objectValues);
  }

  private void showValues()
  {
    String str = "[";
    if (this.type == 10)
    {
      for (float f : this.floatValues)
        str = str + f + ' ';
    }
    else if (this.type == 16)
    {
      for (double d : this.doubleValues)
        str = str + d + ' ';
    }
    else
    {
      int m;
      if (this.type == 9)
      {
        for (m : this.intValues)
          str = str + m + ' ';
      }
      else if (this.type == 12)
      {
        for (m : this.booleanValues)
          str = str + m + ' ';
      }
      else
      {
        byte[] arrayOfByte;
        if (this.type == 18)
          for (arrayOfByte : this.mixedValues)
            if (arrayOfByte == null)
              str = str + "null ";
            else
              str = str + new String(arrayOfByte) + ' ';
        else if (this.type == 14)
          for (arrayOfByte : this.keyValues)
            if (arrayOfByte == null)
              str = str + "null ";
            else
              str = str + new String(arrayOfByte) + ' ';
        else if (this.type == 20)
          for (arrayOfByte : this.stringValues)
            if (arrayOfByte == null)
              str = str + "null ";
            else
              str = str + new String(arrayOfByte) + ' ';
        else if (this.type == 22)
          str = ObjectUtils.showMixedValuesAsString(this.objectValues, "");
      }
    }
    str = str + " ]";
    System.out.println(padding + "values=" + str);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.io.ArrayDecoder
 * JD-Core Version:    0.6.2
 */