package org.jpedal.io;

import java.io.PrintStream;
import org.jpedal.exception.PdfSecurityException;
import org.jpedal.objects.raw.ColorSpaceObject;
import org.jpedal.objects.raw.FunctionObject;
import org.jpedal.objects.raw.PdfObject;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.NumberUtils;

public class ColorObjectDecoder extends ObjectDecoder
{
  private static final boolean debugColorspace = false;

  public ColorObjectDecoder(PdfFileReader paramPdfFileReader)
  {
    super(paramPdfFileReader);
  }

  int handleColorSpaces(PdfObject paramPdfObject, int paramInt, byte[] paramArrayOfByte)
  {
    int i = paramArrayOfByte.length;
    while ((paramArrayOfByte[paramInt] == 10) || (paramArrayOfByte[paramInt] == 13) || (paramArrayOfByte[paramInt] == 32) || (paramArrayOfByte[paramInt] == 91) || (paramArrayOfByte[paramInt] == 93))
    {
      if (paramArrayOfByte[paramInt] == 91)
        paramPdfObject.maybeIndirect(true);
      paramInt++;
    }
    if (paramArrayOfByte[paramInt] == 47)
    {
      paramInt++;
      while ((paramArrayOfByte[paramInt] == 10) || (paramArrayOfByte[paramInt] == 13) || (paramArrayOfByte[paramInt] == 32) || (paramArrayOfByte[paramInt] == 47))
        paramInt++;
      int j = paramInt;
      int k = 0;
      while ((paramArrayOfByte[paramInt] != 10) && (paramArrayOfByte[paramInt] != 13) && (paramArrayOfByte[paramInt] != 32) && (paramArrayOfByte[paramInt] != 47) && (paramArrayOfByte[paramInt] != 60) && (paramArrayOfByte[paramInt] != 62) && (paramArrayOfByte[paramInt] != 91) && (paramArrayOfByte[paramInt] != 93))
      {
        paramInt++;
        k++;
        if (paramInt == i)
          break;
      }
      int m = paramPdfObject.setConstant(2087749783, j, k, paramArrayOfByte);
      if (m == 25)
        m = 895578984;
      paramInt = setColorspace(paramPdfObject, paramInt, paramArrayOfByte, m);
    }
    else if ((paramArrayOfByte[paramInt] == 60) && (paramArrayOfByte[(paramInt + 1)] == 60))
    {
      paramInt = readObjectValue(paramPdfObject, paramInt, paramArrayOfByte);
    }
    else
    {
      if (paramArrayOfByte[paramInt] == 37)
        while ((paramArrayOfByte[paramInt] != 10) && (paramArrayOfByte[paramInt] != 13))
          paramInt++;
      if (paramArrayOfByte[paramInt] == 60)
        paramInt = readHexValue(paramPdfObject, paramInt, paramArrayOfByte);
      else if (paramArrayOfByte[paramInt] == 40)
        paramInt = readStringValue(paramPdfObject, paramInt, paramArrayOfByte);
      else
        paramInt = readColorObjectValue(paramPdfObject, paramInt, paramArrayOfByte);
    }
    if ((paramInt < i) && ((paramArrayOfByte[paramInt] == 47) || (paramArrayOfByte[paramInt] == 62)))
      paramInt--;
    return paramInt;
  }

  private int setColorspace(PdfObject paramPdfObject, int paramInt1, byte[] paramArrayOfByte, int paramInt2)
  {
    switch (paramInt2)
    {
    case 1008872003:
      paramInt1 = handleColorSpaces(paramPdfObject, paramInt1, paramArrayOfByte);
      paramInt1++;
      break;
    case 391471749:
      paramInt1 = handleColorSpaces(paramPdfObject, paramInt1, paramArrayOfByte);
      paramInt1++;
      break;
    case 1498837125:
      break;
    case 1568372915:
      break;
    case 960981604:
      paramInt1 = readDeviceNvalue(paramPdfObject, paramInt1, paramArrayOfByte);
      break;
    case 1785221209:
      break;
    case 1247168582:
      paramInt1 = readDictionaryFromRefOrDirect(-1, paramPdfObject, "", paramInt1, paramArrayOfByte, 2087749783);
      break;
    case 895578984:
      paramInt1 = readIndexedColorspace(paramPdfObject, paramInt1, paramArrayOfByte);
      break;
    case 1847602:
      paramInt1 = handleColorSpaces(paramPdfObject, paramInt1, paramArrayOfByte);
      paramInt1++;
      break;
    case 1146450818:
      break;
    case -2073385820:
      paramInt1 = readSeparationColorspace(paramPdfObject, paramInt1, paramArrayOfByte);
    }
    return paramInt1;
  }

  private int readDeviceNvalue(PdfObject paramPdfObject, int paramInt, byte[] paramArrayOfByte)
  {
    for (int i = paramInt; (i < paramArrayOfByte.length) && (paramArrayOfByte[i] != 93); i++);
    ArrayDecoder localArrayDecoder = new ArrayDecoder(this.objectReader, paramInt, i, 20);
    for (paramInt = localArrayDecoder.readArray(false, paramArrayOfByte, paramPdfObject, 1920898752); (paramArrayOfByte[paramInt] == 93) || (paramArrayOfByte[paramInt] == 32) || (paramArrayOfByte[paramInt] == 10) || (paramArrayOfByte[paramInt] == 13); paramInt++);
    ColorSpaceObject localColorSpaceObject1 = new ColorSpaceObject(-1, 0);
    paramInt = handleColorSpaces(localColorSpaceObject1, paramInt, paramArrayOfByte);
    paramPdfObject.setDictionary(-1247101998, localColorSpaceObject1);
    paramInt++;
    FunctionObject localFunctionObject = new FunctionObject(-1, 0);
    paramInt = handleColorSpaces(localFunctionObject, paramInt, paramArrayOfByte);
    paramPdfObject.setDictionary(-1313946392, localFunctionObject);
    for (int j = paramInt; (j < paramArrayOfByte.length) && (paramArrayOfByte[j] != 93); j++)
      if ((paramArrayOfByte[j] != 32) && (paramArrayOfByte[j] != 10) && (paramArrayOfByte[j] != 13))
      {
        paramInt = j;
        ColorSpaceObject localColorSpaceObject2 = new ColorSpaceObject(-1, 0);
        paramInt = handleColorSpaces(localColorSpaceObject2, paramInt, paramArrayOfByte);
        paramPdfObject.setDictionary(861242754, localColorSpaceObject2);
        paramInt--;
        j = paramArrayOfByte.length;
      }
    paramInt++;
    return paramInt;
  }

  private int readColorObjectValue(PdfObject paramPdfObject, int paramInt, byte[] paramArrayOfByte)
  {
    int i = paramInt;
    while ((paramArrayOfByte[paramInt] != 10) && (paramArrayOfByte[paramInt] != 13) && (paramArrayOfByte[paramInt] != 32) && (paramArrayOfByte[paramInt] != 47) && (paramArrayOfByte[paramInt] != 60) && (paramArrayOfByte[paramInt] != 62) && (paramArrayOfByte[paramInt] != 93))
      paramInt++;
    int j = NumberUtils.parseInt(i, paramInt, paramArrayOfByte);
    while ((paramArrayOfByte[paramInt] == 10) || (paramArrayOfByte[paramInt] == 13) || (paramArrayOfByte[paramInt] == 32) || (paramArrayOfByte[paramInt] == 47) || (paramArrayOfByte[paramInt] == 60))
      paramInt++;
    i = paramInt;
    while ((paramArrayOfByte[paramInt] != 10) && (paramArrayOfByte[paramInt] != 13) && (paramArrayOfByte[paramInt] != 32) && (paramArrayOfByte[paramInt] != 47) && (paramArrayOfByte[paramInt] != 60) && (paramArrayOfByte[paramInt] != 62))
      paramInt++;
    int k = NumberUtils.parseInt(i, paramInt, paramArrayOfByte);
    while ((paramArrayOfByte[paramInt] == 10) || (paramArrayOfByte[paramInt] == 13) || (paramArrayOfByte[paramInt] == 32) || (paramArrayOfByte[paramInt] == 47) || (paramArrayOfByte[paramInt] == 60))
      paramInt++;
    if (paramArrayOfByte[paramInt] != 82)
      throw new RuntimeException("3. Unexpected value in file " + (char)paramArrayOfByte[paramInt] + " - please send to IDRsolutions for analysis");
    paramInt++;
    if ((paramPdfObject.getObjectRefID() == -1) || (paramPdfObject.maybeIndirect()))
      paramPdfObject.setRef(j, k);
    byte[] arrayOfByte = this.objectReader.readObjectAsByteArray(paramPdfObject, this.objectReader.isCompressed(j, k), j, k);
    if (arrayOfByte == null)
    {
      paramPdfObject.setFullyResolved(false);
      if (LogWriter.isOutput())
        LogWriter.writeLog("[Linearized] " + j + ' ' + k + " R not yet available (9)");
      paramInt = paramArrayOfByte.length;
    }
    else if (arrayOfByte[0] == 47)
    {
      handleColorSpaces(paramPdfObject, 0, arrayOfByte);
    }
    else
    {
      int m = 0;
      if ((arrayOfByte[0] != 91) && (arrayOfByte[0] != 60))
        for (m = 3; (arrayOfByte[(m - 1)] != 106) && (arrayOfByte[(m - 2)] != 98) && (arrayOfByte[(m - 3)] != 111); m++);
      handleColorSpaces(paramPdfObject, m, arrayOfByte);
    }
    return paramInt;
  }

  private int readIndexedColorspace(PdfObject paramPdfObject, int paramInt, byte[] paramArrayOfByte)
  {
    ColorSpaceObject localColorSpaceObject1 = new ColorSpaceObject(-1, 0, true);
    paramInt = handleColorSpaces(localColorSpaceObject1, paramInt, paramArrayOfByte);
    paramPdfObject.setDictionary(895578984, localColorSpaceObject1);
    while ((paramInt < paramArrayOfByte.length) && ((paramArrayOfByte[paramInt] == 32) || (paramArrayOfByte[paramInt] == 13) || (paramArrayOfByte[paramInt] == 10) || (paramArrayOfByte[paramInt] == 93) || (paramArrayOfByte[paramInt] == 62)))
      paramInt++;
    paramInt = setNumberValue(paramPdfObject, paramInt, paramArrayOfByte, 960901492);
    if (paramArrayOfByte[paramInt] != 40)
      paramInt++;
    while ((paramInt < paramArrayOfByte.length) && ((paramArrayOfByte[paramInt] == 32) || (paramArrayOfByte[paramInt] == 13) || (paramArrayOfByte[paramInt] == 10)))
      paramInt++;
    int i = (paramArrayOfByte[paramInt] == 91) || (paramArrayOfByte[paramInt] == 40) || (paramArrayOfByte[paramInt] == 60) ? 1 : 0;
    ColorSpaceObject localColorSpaceObject2;
    if (i != 0)
      localColorSpaceObject2 = new ColorSpaceObject(paramPdfObject.getObjectRefAsString());
    else
      localColorSpaceObject2 = new ColorSpaceObject(-1, 0);
    paramPdfObject.setDictionary(1060856191, localColorSpaceObject2);
    paramInt = handleColorSpaces(localColorSpaceObject2, paramInt, paramArrayOfByte);
    paramInt++;
    return paramInt;
  }

  private int readSeparationColorspace(PdfObject paramPdfObject, int paramInt, byte[] paramArrayOfByte)
  {
    for (int j = paramInt; (paramArrayOfByte[j] == 47) || (paramArrayOfByte[j] == 32) || (paramArrayOfByte[j] == 10) || (paramArrayOfByte[j] == 13); j++);
    int k = j;
    while ((j < paramArrayOfByte.length) && (paramArrayOfByte[j] != 47) && (paramArrayOfByte[j] != 32) && (paramArrayOfByte[j] != 13) && (paramArrayOfByte[j] != 10))
      j++;
    int i = j - k;
    byte[] arrayOfByte = new byte[i];
    System.arraycopy(paramArrayOfByte, k, arrayOfByte, 0, i);
    paramPdfObject.setName(506543413, arrayOfByte);
    paramInt = j;
    if (paramArrayOfByte[paramInt] != 47)
      paramInt++;
    ColorSpaceObject localColorSpaceObject = new ColorSpaceObject(-1, 0);
    paramInt = handleColorSpaces(localColorSpaceObject, paramInt, paramArrayOfByte);
    paramPdfObject.setDictionary(-1247101998, localColorSpaceObject);
    if (paramArrayOfByte[paramInt] != 60)
      paramInt++;
    FunctionObject localFunctionObject = new FunctionObject(-1, 0);
    paramInt = handleColorSpaces(localFunctionObject, paramInt, paramArrayOfByte);
    paramPdfObject.setDictionary(-1313946392, localFunctionObject);
    paramInt++;
    return paramInt;
  }

  private int readObjectValue(PdfObject paramPdfObject, int paramInt, byte[] paramArrayOfByte)
  {
    paramInt = convertDirectDictionaryToObject(paramPdfObject, "", paramInt, paramArrayOfByte, -1);
    if (paramPdfObject.hasStream())
    {
      int i = paramArrayOfByte.length;
      int j = 0;
      for (int k = paramInt; k < i - 5; k++)
      {
        if ((paramArrayOfByte[k] == 62) && (paramArrayOfByte[(k + 1)] == 62))
          j++;
        if (j == 2)
          break;
        if ((paramArrayOfByte[k] == 115) && (paramArrayOfByte[(k + 1)] == 116) && (paramArrayOfByte[(k + 2)] == 114) && (paramArrayOfByte[(k + 3)] == 101) && (paramArrayOfByte[(k + 4)] == 97) && (paramArrayOfByte[(k + 5)] == 109))
        {
          readStreamIntoObject(paramPdfObject, k, paramArrayOfByte);
          k = i;
        }
      }
    }
    return paramInt;
  }

  private int readHexValue(PdfObject paramPdfObject, int paramInt, byte[] paramArrayOfByte)
  {
    paramInt++;
    int i = paramInt;
    int j = 0;
    while (paramArrayOfByte[i] != 62)
    {
      if ((paramArrayOfByte[i] != 32) && (paramArrayOfByte[i] != 10) && (paramArrayOfByte[i] != 13))
        j++;
      i++;
    }
    int k = j >> 1;
    byte[] arrayOfByte = new byte[k];
    int m = 0;
    while (true)
      if ((paramArrayOfByte[paramInt] == 32) || (paramArrayOfByte[paramInt] == 10) || (paramArrayOfByte[paramInt] == 13))
      {
        paramInt++;
      }
      else
      {
        int n = paramArrayOfByte[paramInt];
        if ((n >= 65) && (n <= 70))
          n -= 55;
        else if ((n >= 97) && (n <= 102))
          n -= 87;
        else if ((n >= 48) && (n <= 57))
          n -= 48;
        else
          throw new RuntimeException("Unexpected number " + (char)paramArrayOfByte[paramInt]);
        paramInt++;
        while ((paramArrayOfByte[paramInt] == 32) || (paramArrayOfByte[paramInt] == 10) || (paramArrayOfByte[paramInt] == 13))
          paramInt++;
        int i1 = paramArrayOfByte[paramInt];
        if ((i1 >= 65) && (i1 <= 70))
          i1 -= 55;
        else if ((i1 >= 97) && (i1 <= 102))
          i1 -= 87;
        else if ((i1 >= 48) && (i1 <= 57))
          i1 -= 48;
        else
          throw new RuntimeException("Unexpected number " + (char)paramArrayOfByte[paramInt]);
        paramInt++;
        int i2 = i1 + (n << 4);
        arrayOfByte[m] = ((byte)i2);
        m++;
        if (m == k)
          break;
      }
    try
    {
      if (this.decryption != null)
        arrayOfByte = this.decryption.decrypt(arrayOfByte, paramPdfObject.getObjectRefAsString(), false, null, false, false);
    }
    catch (PdfSecurityException localPdfSecurityException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception: " + localPdfSecurityException.getMessage());
    }
    paramPdfObject.setDecodedStream(arrayOfByte);
    return paramInt;
  }

  private int readStringValue(PdfObject paramPdfObject, int paramInt, byte[] paramArrayOfByte)
  {
    paramInt++;
    int i = paramInt;
    while ((paramArrayOfByte[paramInt] != 41) || ((ObjectUtils.isEscaped(paramArrayOfByte, paramInt)) && (paramArrayOfByte[(paramInt - 1)] != 0)))
      paramInt++;
    byte[] arrayOfByte = ObjectUtils.readEscapedValue(paramInt, paramArrayOfByte, i, false);
    try
    {
      if (this.decryption != null)
        arrayOfByte = this.decryption.decrypt(arrayOfByte, paramPdfObject.getObjectRefAsString(), false, null, false, false);
    }
    catch (PdfSecurityException localPdfSecurityException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception: " + localPdfSecurityException.getMessage());
    }
    paramPdfObject.setDecodedStream(arrayOfByte);
    return paramInt;
  }

  private static void showData(PdfObject paramPdfObject, byte[] paramArrayOfByte, int paramInt)
  {
    System.out.println(padding + "Reading colorspace into " + paramPdfObject + " ref=" + paramPdfObject.getObjectRefAsString() + " i=" + paramInt + " chars=" + (char)paramArrayOfByte[paramInt] + (char)paramArrayOfByte[(paramInt + 1)] + (char)paramArrayOfByte[(paramInt + 2)] + (char)paramArrayOfByte[(paramInt + 3)] + (char)paramArrayOfByte[(paramInt + 4)]);
    System.out.println(padding + "------------>");
    for (int i = paramInt; i < paramArrayOfByte.length; i++)
    {
      System.out.print((char)paramArrayOfByte[i]);
      if ((i > 5) && (paramArrayOfByte[(i - 5)] == 115) && (paramArrayOfByte[(i - 4)] == 116) && (paramArrayOfByte[(i - 3)] == 114) && (paramArrayOfByte[(i - 2)] == 101) && (paramArrayOfByte[(i - 1)] == 97) && (paramArrayOfByte[i] == 109))
        i = paramArrayOfByte.length;
    }
    System.out.println("<--------");
  }

  int processColorSpace(PdfObject paramPdfObject, String paramString, int paramInt, byte[] paramArrayOfByte)
  {
    if (!paramPdfObject.ignoreRecursion())
    {
      if (paramPdfObject.getObjectType() == 2087749783)
        return handleColorSpaces(paramPdfObject, paramInt, paramArrayOfByte);
      int i = paramArrayOfByte[paramInt] == 47 ? 1 : 0;
      ColorSpaceObject localColorSpaceObject;
      if (i != 0)
        localColorSpaceObject = new ColorSpaceObject(paramString);
      else
        localColorSpaceObject = new ColorSpaceObject(-1, 0);
      paramPdfObject.setDictionary(2087749783, localColorSpaceObject);
      if (localColorSpaceObject.isDataExternal())
      {
        localColorSpaceObject.isDataExternal(true);
        if (!resolveFully(localColorSpaceObject))
          paramPdfObject.setFullyResolved(false);
      }
      return handleColorSpaces(localColorSpaceObject, paramInt, paramArrayOfByte);
    }
    return paramInt;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.io.ColorObjectDecoder
 * JD-Core Version:    0.6.2
 */