package org.jpedal.io;

import java.io.Serializable;
import org.jpedal.exception.PdfSecurityException;
import org.jpedal.objects.raw.ColorSpaceObject;
import org.jpedal.objects.raw.FormObject;
import org.jpedal.objects.raw.NamesObject;
import org.jpedal.objects.raw.ObjectFactory;
import org.jpedal.objects.raw.PdfArrayIterator;
import org.jpedal.objects.raw.PdfDictionary;
import org.jpedal.objects.raw.PdfObject;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.NumberUtils;
import org.jpedal.utils.StringUtils;

public class ObjectDecoder
  implements Serializable
{
  PdfFileReader objectReader = null;
  DecryptionFactory decryption = null;
  private Object PDFkey;
  static final byte[] endPattern = { 101, 110, 100, 111, 98, 106 };
  static final boolean debugFastCode = false;
  private int pdfKeyType;
  private int PDFkeyInt;
  static String padding = "";
  boolean isInlineImage = false;
  private int endPt = -1;

  public ObjectDecoder(PdfFileReader paramPdfFileReader)
  {
    init(paramPdfFileReader);
  }

  private ObjectDecoder()
  {
  }

  private void init(PdfFileReader paramPdfFileReader)
  {
    this.objectReader = paramPdfFileReader;
    this.decryption = paramPdfFileReader.getDecryptionObject();
  }

  public int readDictionaryAsObject(PdfObject paramPdfObject, int paramInt, byte[] paramArrayOfByte)
  {
    if (this.endPt == -1)
      this.endPt = paramArrayOfByte.length;
    int i = paramArrayOfByte.length;
    paramInt = readObjectDataValues(paramPdfObject, paramInt, paramArrayOfByte, i);
    if ((!paramPdfObject.ignoreStream()) && (paramPdfObject.getGeneralType(-1) != 6420))
      readStreamData(paramPdfObject, paramInt, paramArrayOfByte, i);
    if (paramPdfObject.getObjectType() == 373244477)
      setFieldNames(paramPdfObject);
    return paramInt;
  }

  private int readObjectDataValues(PdfObject paramPdfObject, int paramInt1, byte[] paramArrayOfByte, int paramInt2)
  {
    int i = 0;
    if (this.isInlineImage)
      i = 1;
    while (true)
    {
      if ((paramInt1 < paramInt2) && (paramArrayOfByte[paramInt1] == 37))
        paramInt1 = stripComment(paramInt2, paramInt1, paramArrayOfByte);
      if ((paramInt1 >= paramInt2) || ((this.endPt != -1) && (paramInt1 >= this.endPt)) || ((paramArrayOfByte[paramInt1] == 101) && (paramArrayOfByte[(paramInt1 + 1)] == 110) && (paramArrayOfByte[(paramInt1 + 2)] == 100) && (paramArrayOfByte[(paramInt1 + 3)] == 111)) || ((paramArrayOfByte[paramInt1] == 115) && (paramArrayOfByte[(paramInt1 + 1)] == 116) && (paramArrayOfByte[(paramInt1 + 2)] == 114) && (paramArrayOfByte[(paramInt1 + 3)] == 101) && (paramArrayOfByte[(paramInt1 + 4)] == 97) && (paramArrayOfByte[(paramInt1 + 5)] == 109)))
        break;
      if ((paramArrayOfByte[paramInt1] == 60) && (paramArrayOfByte[(paramInt1 + 1)] == 60))
      {
        paramInt1++;
        i++;
      }
      else if ((paramArrayOfByte[paramInt1] == 62) && (paramInt1 + 1 != paramInt2) && (paramArrayOfByte[(paramInt1 + 1)] == 62) && (paramArrayOfByte[(paramInt1 - 1)] != 62))
      {
        paramInt1++;
        i--;
        if (i == 0)
          break;
      }
      else if ((paramArrayOfByte[paramInt1] == 47) && ((paramArrayOfByte[(paramInt1 + 1)] == 47) || (paramArrayOfByte[(paramInt1 + 1)] == 32)))
      {
        paramInt1++;
      }
      else if (paramArrayOfByte[paramInt1] == 47)
      {
        paramInt1++;
        int j = paramInt1;
        int k = findDictionaryEnd(paramInt1, paramArrayOfByte, paramInt2);
        paramInt1 += k;
        if (paramInt1 == paramInt2)
          break;
        boolean bool1 = false;
        if (paramPdfObject.getID() == 1184787)
          bool1 = isStringPair(paramInt1, paramArrayOfByte, bool1);
        int m = paramPdfObject.getObjectType();
        boolean bool2 = isMapObject(paramPdfObject, paramInt1, paramArrayOfByte, paramInt2, j, k, bool1, m);
        if ((paramArrayOfByte[paramInt1] == 47) || (paramArrayOfByte[paramInt1] == 40) || ((paramArrayOfByte[paramInt1] == 91) && (paramArrayOfByte[(paramInt1 + 1)] != 93)))
          paramInt1--;
        if (this.pdfKeyType == -1)
          paramInt1 = ObjectUtils.handleUnknownType(paramInt1, paramArrayOfByte, paramInt2);
        if ((this.PDFkeyInt != -1) && (this.pdfKeyType != -1))
          paramInt1 = setValue(paramPdfObject, paramInt1, paramArrayOfByte, paramInt2, bool2);
      }
      else if ((paramArrayOfByte[paramInt1] == 91) && (i == 0) && (paramPdfObject.getObjectType() == 1485011327))
      {
        ArrayDecoder localArrayDecoder = new ArrayDecoder(this.objectReader, paramInt1, paramArrayOfByte.length, 18, null, 826094945);
        localArrayDecoder.readArray(false, paramArrayOfByte, paramPdfObject, 339034948);
      }
      paramInt1++;
    }
    return paramInt1;
  }

  private boolean isMapObject(PdfObject paramPdfObject, int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean, int paramInt5)
  {
    boolean bool;
    if ((paramInt5 == 487790868) && ((paramPdfObject.getID() == 893350012) || ((paramPdfObject.getID() == 1184787) && (paramBoolean)) || ((paramPdfObject.getID() == 17) && (paramArrayOfByte[(paramInt1 - 2)] == 47))))
    {
      this.pdfKeyType = 30;
      this.PDFkey = PdfDictionary.getKey(paramInt3, paramInt4, paramArrayOfByte);
      this.PDFkeyInt = 487790868;
      bool = true;
    }
    else
    {
      bool = false;
      this.PDFkey = null;
      getKeyType(paramPdfObject, paramInt1, paramArrayOfByte, paramInt2, paramInt4, paramInt3, paramInt5);
    }
    return bool;
  }

  private static int findDictionaryEnd(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
  {
    int i = 0;
    while ((paramArrayOfByte[paramInt1] != 32) && (paramArrayOfByte[paramInt1] != 13) && (paramArrayOfByte[paramInt1] != 9) && (paramArrayOfByte[paramInt1] != 10) && (paramArrayOfByte[paramInt1] != 91) && (paramArrayOfByte[paramInt1] != 47) && (paramArrayOfByte[paramInt1] != 40) && (paramArrayOfByte[paramInt1] != 60) && (paramArrayOfByte[paramInt1] != 62))
    {
      paramInt1++;
      i++;
      if (paramInt1 == paramInt2)
        break;
    }
    return i;
  }

  private void getKeyType(PdfObject paramPdfObject, int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    this.PDFkeyInt = PdfDictionary.getIntKey(paramInt4, paramInt3, paramArrayOfByte);
    if ((this.PDFkeyInt == 895578984) && ((paramInt5 == 7451) || (paramInt5 == 373244477) || (paramInt5 == 2004845231)))
      this.PDFkeyInt = 25;
    if (this.isInlineImage)
      this.PDFkeyInt = PdfObjectFactory.getInlineID(this.PDFkeyInt);
    int i = paramPdfObject.getID();
    if ((paramInt5 == 2004251818) && ((this.PDFkeyInt == 2087749783) || (this.PDFkeyInt == -1938465939) || (this.PDFkeyInt == 878474856) || (this.PDFkeyInt == 979194486) || (this.PDFkeyInt == 373243460) || (this.PDFkeyInt == 1146450818)))
    {
      this.pdfKeyType = 2;
    }
    else if ((paramInt5 == 1485011327) && (this.PDFkeyInt == 20))
    {
      this.PDFkeyInt = 339034948;
      this.pdfKeyType = 18;
    }
    else if (((paramInt5 == 373244477) || (paramInt5 == 7451)) && (this.PDFkeyInt == 20))
    {
      if ((i == 4384) || (i == 4369))
      {
        this.pdfKeyType = 40;
      }
      else if (i == 2570558)
      {
        this.pdfKeyType = 25;
      }
      else
      {
        this.PDFkeyInt = 339034948;
        this.pdfKeyType = 18;
      }
    }
    else if (((paramInt5 == 373244477) || (paramInt5 == 7451)) && ((i == 4384) || (i == 4369)) && (this.PDFkeyInt == 17))
    {
      this.pdfKeyType = 40;
    }
    else if ((this.PDFkeyInt == 1110717793) && (paramInt5 == -1567847737))
    {
      this.pdfKeyType = 22;
    }
    else if ((this.PDFkeyInt == 506543413) && (paramInt5 == -1567847737))
    {
      this.pdfKeyType = 25;
    }
    else if (((paramInt5 == 2087749783) || (paramInt5 == 1518239089)) && (this.PDFkeyInt == 30))
    {
      this.pdfKeyType = 7;
    }
    else if ((this.PDFkeyInt == 826096968) && (paramInt5 == 2087749783) && (paramPdfObject.getParameterConstant(2087749783) == 391471749))
    {
      this.pdfKeyType = 7;
    }
    else if ((i == 2570558) && (paramPdfObject.getObjectType() == 373244477) && ((this.PDFkeyInt == 32) || (this.PDFkeyInt == 31)))
    {
      this.pdfKeyType = 25;
    }
    else if ((this.isInlineImage) && (this.PDFkeyInt == 2087749783))
    {
      this.pdfKeyType = 1;
    }
    else
    {
      this.pdfKeyType = PdfDictionary.getKeyType(this.PDFkeyInt, paramInt5);
    }
    int j;
    if ((i == 878474856) && (this.PDFkeyInt == 1518239089))
    {
      for (j = paramInt1; ((paramArrayOfByte[j] >= 48) && (paramArrayOfByte[j] < 58)) || (paramArrayOfByte[j] == 32); j++);
      if (paramArrayOfByte[j] == 91)
        this.pdfKeyType = 14;
    }
    if ((this.pdfKeyType == -1) && (i == 1448698499))
    {
      this.pdfKeyType = getPairedValues(paramPdfObject, paramInt1, paramArrayOfByte, this.pdfKeyType, paramInt2, paramInt3, paramInt4);
    }
    else if (((i == 30) || (i == 20) || (i == 34)) && (paramPdfObject.getParentID() == 4384) && (paramPdfObject.getObjectType() == 373244477) && (paramArrayOfByte[paramInt1] != 91))
    {
      for (j = paramInt1; ((paramArrayOfByte[j] >= 48) && (paramArrayOfByte[j] < 58)) || (paramArrayOfByte[j] == 32); j++);
      if (((paramArrayOfByte[paramInt4] != 76) || (paramArrayOfByte[(paramInt4 + 1)] != 101) || (paramArrayOfByte[(paramInt4 + 2)] != 110) || (paramArrayOfByte[(paramInt4 + 3)] != 103) || (paramArrayOfByte[(paramInt4 + 4)] != 116) || (paramArrayOfByte[(paramInt4 + 5)] != 104)) && ((paramArrayOfByte[paramInt4] != 79) || (paramArrayOfByte[(paramInt4 + 1)] != 110)) && ((paramArrayOfByte[paramInt4] != 79) || (paramArrayOfByte[(paramInt4 + 1)] != 102) || (paramArrayOfByte[(paramInt4 + 2)] != 102)) && (paramArrayOfByte[j] == 82))
        this.pdfKeyType = getPairedValues(paramPdfObject, paramInt1, paramArrayOfByte, this.pdfKeyType, paramInt2, paramInt3, paramInt4);
    }
    if (this.PDFkeyInt == 1888135062)
      this.pdfKeyType = setTypeForDecodeParams(paramInt1, paramArrayOfByte, paramInt2, this.pdfKeyType);
  }

  private int setValue(PdfObject paramPdfObject, int paramInt1, byte[] paramArrayOfByte, int paramInt2, boolean paramBoolean)
  {
    boolean bool = paramPdfObject.ignoreRecursion();
    if ((this.pdfKeyType == 5) && (paramPdfObject.isDataExternal()))
      this.pdfKeyType = 1;
    ArrayDecoder localArrayDecoder;
    switch (this.pdfKeyType)
    {
    case 25:
      paramInt1 = setTextStreamValue(paramPdfObject, paramInt1, paramArrayOfByte, bool);
      break;
    case 35:
      paramInt1 = setNameTreeValue(paramPdfObject, paramInt1, paramArrayOfByte, paramInt2, bool);
      break;
    case 2:
      paramInt1 = setDictionaryValue(paramPdfObject, paramInt1, paramArrayOfByte, paramInt2, bool);
      break;
    case 20:
      localArrayDecoder = new ArrayDecoder(this.objectReader, paramInt1, this.endPt, 20);
      paramInt1 = localArrayDecoder.readArray(bool, paramArrayOfByte, paramPdfObject, this.PDFkeyInt);
      break;
    case 12:
      localArrayDecoder = new ArrayDecoder(this.objectReader, paramInt1, this.endPt, 12);
      paramInt1 = localArrayDecoder.readArray(false, paramArrayOfByte, paramPdfObject, this.PDFkeyInt);
      break;
    case 14:
      localArrayDecoder = new ArrayDecoder(this.objectReader, paramInt1, this.endPt, 14);
      paramInt1 = localArrayDecoder.readArray(bool, paramArrayOfByte, paramPdfObject, this.PDFkeyInt);
      break;
    case 18:
      localArrayDecoder = new ArrayDecoder(this.objectReader, paramInt1, this.endPt, 18);
      paramInt1 = localArrayDecoder.readArray(bool, paramArrayOfByte, paramPdfObject, this.PDFkeyInt);
      break;
    case 22:
      localArrayDecoder = new ArrayDecoder(this.objectReader, paramInt1, this.endPt, 22);
      paramInt1 = localArrayDecoder.readArray(false, paramArrayOfByte, paramPdfObject, this.PDFkeyInt);
      break;
    case 16:
      localArrayDecoder = new ArrayDecoder(this.objectReader, paramInt1, this.endPt, 16);
      paramInt1 = localArrayDecoder.readArray(false, paramArrayOfByte, paramPdfObject, this.PDFkeyInt);
      break;
    case 9:
      localArrayDecoder = new ArrayDecoder(this.objectReader, paramInt1, this.endPt, 9);
      paramInt1 = localArrayDecoder.readArray(false, paramArrayOfByte, paramPdfObject, this.PDFkeyInt);
      break;
    case 10:
      localArrayDecoder = new ArrayDecoder(this.objectReader, paramInt1, this.endPt, 10);
      paramInt1 = localArrayDecoder.readArray(false, paramArrayOfByte, paramPdfObject, this.PDFkeyInt);
      break;
    case 30:
      paramInt1 = setNameStringValue(paramPdfObject, paramInt1, paramArrayOfByte, paramBoolean);
      break;
    case 8:
      paramInt1 = setBooleanValue(paramPdfObject, paramInt1, paramArrayOfByte, this.PDFkeyInt);
      break;
    case 3:
      paramInt1 = setStringConstantValue(paramPdfObject, paramInt1, paramArrayOfByte);
      break;
    case 4:
      paramInt1 = setStringKeyValue(paramPdfObject, paramInt1, paramArrayOfByte);
      break;
    case 6:
      paramInt1++;
      while ((paramArrayOfByte[paramInt1] == 10) || (paramArrayOfByte[paramInt1] == 13) || (paramArrayOfByte[paramInt1] == 32) || (paramArrayOfByte[paramInt1] == 47))
        paramInt1++;
      paramInt1 = setNumberValue(paramPdfObject, paramInt1, paramArrayOfByte, this.PDFkeyInt);
      break;
    case 7:
      paramInt1 = setFloatValue(paramPdfObject, paramInt1, paramArrayOfByte, paramInt2);
      break;
    case 5:
      paramInt1 = setUnreadDictionaryValue(paramPdfObject, paramInt1, paramArrayOfByte);
      break;
    case 40:
      if ((paramArrayOfByte.length - 5 > 0) && (paramArrayOfByte[(paramInt1 + 1)] == 110) && (paramArrayOfByte[(paramInt1 + 2)] == 117) && (paramArrayOfByte[(paramInt1 + 3)] == 108) && (paramArrayOfByte[(paramInt1 + 4)] == 108))
        paramInt1 += 5;
      else
        paramInt1 = setVariousValue(paramPdfObject, paramInt1, paramArrayOfByte, paramInt2, this.PDFkeyInt, paramBoolean, bool);
      break;
    case 1:
      paramInt1 = setDictionaryValue(paramPdfObject, paramInt1, paramArrayOfByte, bool);
    case 11:
    case 13:
    case 15:
    case 17:
    case 19:
    case 21:
    case 23:
    case 24:
    case 26:
    case 27:
    case 28:
    case 29:
    case 31:
    case 32:
    case 33:
    case 34:
    case 36:
    case 37:
    case 38:
    case 39:
    }
    return paramInt1;
  }

  private static boolean isStringPair(int paramInt, byte[] paramArrayOfByte, boolean paramBoolean)
  {
    int i = paramArrayOfByte.length;
    for (int j = paramInt; j < i; j++)
      if (paramArrayOfByte[j] == 40)
      {
        j = i;
        paramBoolean = true;
      }
      else if ((paramArrayOfByte[j] == 47) || (paramArrayOfByte[j] == 62) || (paramArrayOfByte[j] == 60) || (paramArrayOfByte[j] == 91) || (paramArrayOfByte[j] == 82))
      {
        j = i;
      }
      else if ((paramArrayOfByte[j] == 77) && (paramArrayOfByte[(j + 1)] == 67) && (paramArrayOfByte[(j + 2)] == 73) && (paramArrayOfByte[(j + 3)] == 68))
      {
        j = i;
      }
    return paramBoolean;
  }

  private static int stripComment(int paramInt1, int paramInt2, byte[] paramArrayOfByte)
  {
    while ((paramInt2 < paramInt1) && (paramArrayOfByte[paramInt2] != 10) && (paramArrayOfByte[paramInt2] != 13))
      paramInt2++;
    while ((paramInt2 < paramInt1) && ((paramArrayOfByte[paramInt2] == 9) || (paramArrayOfByte[paramInt2] == 10) || (paramArrayOfByte[paramInt2] == 13) || (paramArrayOfByte[paramInt2] == 32) || (paramArrayOfByte[paramInt2] == 60)))
      paramInt2++;
    return paramInt2;
  }

  private int setVariousValue(PdfObject paramPdfObject, int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3, boolean paramBoolean1, boolean paramBoolean2)
  {
    if (paramArrayOfByte[paramInt1] != 60)
      paramInt1++;
    if (paramArrayOfByte[paramInt1] == 47)
    {
      paramInt1 = setNameStringValue(paramPdfObject, paramInt1, paramArrayOfByte, paramBoolean1);
    }
    else if ((paramArrayOfByte[paramInt1] == 102) && (paramArrayOfByte[(paramInt1 + 1)] == 97) && (paramArrayOfByte[(paramInt1 + 2)] == 108) && (paramArrayOfByte[(paramInt1 + 3)] == 115) && (paramArrayOfByte[(paramInt1 + 4)] == 101))
    {
      paramPdfObject.setBoolean(paramInt3, false);
      paramInt1 += 4;
    }
    else if ((paramArrayOfByte[paramInt1] == 116) && (paramArrayOfByte[(paramInt1 + 1)] == 114) && (paramArrayOfByte[(paramInt1 + 2)] == 117) && (paramArrayOfByte[(paramInt1 + 3)] == 101))
    {
      paramPdfObject.setBoolean(paramInt3, true);
      paramInt1 += 3;
    }
    else if ((paramArrayOfByte[paramInt1] == 40) || ((paramArrayOfByte[paramInt1] == 60) && (paramArrayOfByte[(paramInt1 - 1)] != 60) && (paramArrayOfByte[(paramInt1 + 1)] != 60)))
    {
      paramInt1 = readTextStream(paramPdfObject, paramInt1, paramArrayOfByte, paramInt3, paramBoolean2);
    }
    else if (paramArrayOfByte[paramInt1] == 91)
    {
      ArrayDecoder localArrayDecoder1;
      if (paramInt3 == 2627089)
      {
        localArrayDecoder1 = new ArrayDecoder(this.objectReader, paramInt1, this.endPt, 18);
        paramInt1 = localArrayDecoder1.readArray(paramBoolean2, paramArrayOfByte, paramPdfObject, paramInt3);
      }
      else if (paramInt3 == 27)
      {
        localArrayDecoder1 = new ArrayDecoder(this.objectReader, paramInt1, this.endPt, 20);
        paramInt1 = localArrayDecoder1.readArray(paramBoolean2, paramArrayOfByte, paramPdfObject, paramInt3);
      }
      else if (paramInt3 == 19)
      {
        localArrayDecoder1 = new ArrayDecoder(this.objectReader, paramInt1, this.endPt, 10);
        paramInt1 = localArrayDecoder1.readArray(paramBoolean2, paramArrayOfByte, paramPdfObject, paramInt3);
      }
      else
      {
        localArrayDecoder1 = new ArrayDecoder(this.objectReader, paramInt1, this.endPt, 20);
        paramInt1 = localArrayDecoder1.readArray(paramBoolean2, paramArrayOfByte, paramPdfObject, paramInt3);
      }
    }
    else if ((paramArrayOfByte[paramInt1] == 60) && (paramArrayOfByte[(paramInt1 + 1)] == 60))
    {
      paramInt1 = readDictionary(paramPdfObject, paramInt1, paramArrayOfByte, paramInt3, paramBoolean2);
    }
    else
    {
      int j = paramInt1;
      int k = paramInt1 + 1;
      Object localObject = paramArrayOfByte;
      int m = 0;
      int n = 1;
      int i1 = 0;
      int i2 = 0;
      String str = paramPdfObject.getObjectRefAsString();
      while (true)
      {
        if ((localObject[k] == 82) && (i2 == 0))
        {
          i1 = 1;
          i3 = k;
          k = paramInt1;
          paramInt1 = i3;
          while ((localObject[k] == 91) || (localObject[k] == 32) || (localObject[k] == 13) || (localObject[k] == 10))
            k++;
          int i = k;
          int i6 = k;
          while ((localObject[k] != 10) && (localObject[k] != 13) && (localObject[k] != 32) && (localObject[k] != 47) && (localObject[k] != 60) && (localObject[k] != 62))
            k++;
          i4 = NumberUtils.parseInt(i, k, (byte[])localObject);
          while ((localObject[k] == 10) || (localObject[k] == 13) || (localObject[k] == 32))
            k++;
          i = k;
          while ((localObject[k] != 10) && (localObject[k] != 13) && (localObject[k] != 32) && (localObject[k] != 47) && (localObject[k] != 60) && (localObject[k] != 62))
            k++;
          int i5 = NumberUtils.parseInt(i, k, (byte[])localObject);
          while ((localObject[k] == 10) || (localObject[k] == 13) || (localObject[k] == 32) || (localObject[k] == 47) || (localObject[k] == 60))
            k++;
          if (localObject[k] != 82)
            throw new RuntimeException("ref=" + i4 + " gen=" + i4 + " 1. Unexpected value " + localObject[k] + " in file - please send to IDRsolutions for analysis char=" + (char)localObject[k]);
          str = new String((byte[])localObject, i6, 1 + k - i6);
          byte[] arrayOfByte = this.objectReader.readObjectAsByteArray(paramPdfObject, this.objectReader.isCompressed(i4, i5), i4, i5);
          int i7 = 0;
          if ((arrayOfByte == null) || (arrayOfByte.length <= 2) || (arrayOfByte[0] != 60) || (arrayOfByte[1] != 60))
          {
            int i8 = arrayOfByte.length - 3;
            for (int i10 = 3; i10 < i8; i10++)
              if ((arrayOfByte[(i10 - 2)] == 111) && (arrayOfByte[(i10 - 1)] == 98) && (arrayOfByte[i10] == 106))
              {
                for (i7 = i10 + 1; (i7 < i8) && ((arrayOfByte[i7] == 10) || (arrayOfByte[i7] == 13) || (arrayOfByte[i7] == 32) || (arrayOfByte[i7] == 9)); i7++);
                i10 = i8;
              }
              else if (((arrayOfByte[i10] <= 47) || (arrayOfByte[i10] >= 58)) && (arrayOfByte[i10] != 111) && (arrayOfByte[i10] != 98) && (arrayOfByte[i10] != 106) && (arrayOfByte[i10] != 82) && (arrayOfByte[i10] != 32) && (arrayOfByte[i10] != 10) && (arrayOfByte[i10] != 13))
              {
                i10 = i8;
                i7 = 0;
              }
          }
          i2 = arrayOfByte[i7] == 40 ? 1 : 0;
          if (((paramPdfObject.getID() == 4369) || (paramPdfObject.getID() == 17)) && (arrayOfByte[0] == 60) && (arrayOfByte[1] == 60))
          {
            PdfObject localPdfObject = ObjectFactory.createObject(paramInt3, str, paramPdfObject.getObjectType(), paramPdfObject.getID());
            localPdfObject.setID(paramInt3);
            paramPdfObject.setDictionary(paramInt3, localPdfObject);
            localPdfObject.setStatus(2);
            localPdfObject.setUnresolvedData(arrayOfByte, 2);
            n = 0;
            m = 4;
            paramInt1 = k;
            break;
          }
          if ((paramPdfObject.getID() == -1) && (arrayOfByte[0] == 60) && (arrayOfByte[1] == 60))
          {
            n = 0;
            m = 0;
            paramInt1 = k;
            break;
          }
          if ((paramInt3 == 2037870513) && (localObject[paramInt1] == 82))
          {
            int i9 = 0;
            while (arrayOfByte[i9] != 91)
            {
              i9++;
              if ((arrayOfByte[i9] == 60) && (arrayOfByte[(i9 + 1)] != 60))
                break;
            }
            ArrayDecoder localArrayDecoder3 = new ArrayDecoder(this.objectReader, i9, this.endPt, 18);
            localArrayDecoder3.readArray(paramBoolean2, arrayOfByte, paramPdfObject, paramInt3);
            paramInt1 = k;
            break;
          }
          localObject = arrayOfByte;
          if (localObject == null)
          {
            paramPdfObject.setFullyResolved(false);
            if (LogWriter.isOutput())
              LogWriter.writeLog("[Linearized] " + str + " not yet available (4)");
            paramInt1 = paramInt2;
            break;
          }
          j = 3;
          if (localObject.length <= 3)
            j = 0;
          else
            while ((localObject[(j - 2)] != 111) || (localObject[(j - 1)] != 98) || (localObject[j] != 106))
            {
              j++;
              if (j == localObject.length)
                j = 0;
            }
          if (localObject[j] != 40)
            j++;
          while ((localObject[j] == 10) || (localObject[j] == 13) || (localObject[j] == 32))
            j++;
          k = j;
        }
        else
        {
          if ((localObject[k] == 91) || (localObject[k] == 40))
            break;
          if (localObject[k] == 60)
          {
            m = 0;
            break;
          }
          if ((localObject[k] == 62) || (localObject[k] == 47))
          {
            m = 1;
            break;
          }
          if ((localObject[k] != 32) && (localObject[k] != 10) && (localObject[k] != 13) && ((localObject[k] < 48) || (localObject[k] > 57)) && (localObject[k] != 46))
            n = 0;
        }
        k++;
        if (k == localObject.length)
          break;
      }
      int i3 = 0;
      for (int i4 = j + 1; i4 < localObject.length; i4++)
        if (localObject[i4] == 47)
          i3++;
      while ((localObject[j] == 10) || (localObject[j] == 13) || (localObject[j] == 32))
        j++;
      if (m != 4)
        if ((i3 == 0) && (localObject[j] == 47))
        {
          j = setNameStringValue(paramPdfObject, j, (byte[])localObject, paramBoolean1);
        }
        else if (localObject[j] == 40)
        {
          j = readTextStream(paramPdfObject, j, (byte[])localObject, paramInt3, paramBoolean2);
        }
        else if (localObject[j] == 91)
        {
          ArrayDecoder localArrayDecoder2 = new ArrayDecoder(this.objectReader, j, this.endPt, 20);
          j = localArrayDecoder2.readArray(paramBoolean2, (byte[])localObject, paramPdfObject, paramInt3);
        }
        else if (m == 0)
        {
          try
          {
            j = readDictionaryFromRefOrDirect(-1, paramPdfObject, str, j, (byte[])localObject, paramInt3);
          }
          catch (Exception localException)
          {
            if (LogWriter.isOutput())
              LogWriter.writeLog("Exception: " + localException.getMessage());
          }
        }
        else if (n != 0)
        {
          j = setNumberValue(paramPdfObject, j, (byte[])localObject, paramInt3);
        }
        else if (m == 1)
        {
          j = setNameStringValue(paramPdfObject, j, (byte[])localObject, paramBoolean1);
        }
      if (i1 == 0)
        paramInt1 = j;
    }
    return paramInt1;
  }

  private static int setTypeForDecodeParams(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3)
  {
    for (int i = paramInt1; (i < paramInt2) && ((paramArrayOfByte[i] == 32) || (paramArrayOfByte[i] == 9) || (paramArrayOfByte[i] == 13) || (paramArrayOfByte[i] == 10)); i++);
    if (paramArrayOfByte[i] != 60)
    {
      while ((i < paramInt2) && ((paramArrayOfByte[i] == 32) || (paramArrayOfByte[i] == 9) || (paramArrayOfByte[i] == 13) || (paramArrayOfByte[i] == 10) || (paramArrayOfByte[i] == 91)))
        i++;
      if ((paramArrayOfByte[i] == 60) || ((paramArrayOfByte[i] >= 48) && (paramArrayOfByte[i] <= 57)))
        paramInt3 = 22;
    }
    return paramInt3;
  }

  private int setNameTreeValue(PdfObject paramPdfObject, int paramInt1, byte[] paramArrayOfByte, int paramInt2, boolean paramBoolean)
  {
    int i = 0;
    while (paramArrayOfByte[paramInt1] != 91)
    {
      if (paramArrayOfByte[paramInt1] == 40)
      {
        i = 0;
        break;
      }
      if ((paramArrayOfByte[paramInt1] >= 48) && (paramArrayOfByte[paramInt1] <= 57))
      {
        i = 1;
        break;
      }
      paramInt1++;
    }
    byte[] arrayOfByte1 = paramArrayOfByte;
    int j = paramInt1;
    int k = paramInt1;
    int m = 0;
    int n;
    if (i != 0)
    {
      n = paramInt1;
      while ((paramArrayOfByte[paramInt1] != 10) && (paramArrayOfByte[paramInt1] != 13) && (paramArrayOfByte[paramInt1] != 32) && (paramArrayOfByte[paramInt1] != 47) && (paramArrayOfByte[paramInt1] != 60) && (paramArrayOfByte[paramInt1] != 62))
        paramInt1++;
      int i1 = NumberUtils.parseInt(n, paramInt1, paramArrayOfByte);
      while ((paramArrayOfByte[paramInt1] == 10) || (paramArrayOfByte[paramInt1] == 13) || (paramArrayOfByte[paramInt1] == 32) || (paramArrayOfByte[paramInt1] == 47) || (paramArrayOfByte[paramInt1] == 60))
        paramInt1++;
      n = paramInt1;
      while ((paramArrayOfByte[paramInt1] != 10) && (paramArrayOfByte[paramInt1] != 13) && (paramArrayOfByte[paramInt1] != 32) && (paramArrayOfByte[paramInt1] != 47) && (paramArrayOfByte[paramInt1] != 60) && (paramArrayOfByte[paramInt1] != 62))
        paramInt1++;
      int i2 = NumberUtils.parseInt(n, paramInt1, paramArrayOfByte);
      while ((paramArrayOfByte[paramInt1] == 10) || (paramArrayOfByte[paramInt1] == 13) || (paramArrayOfByte[paramInt1] == 32) || (paramArrayOfByte[paramInt1] == 47) || (paramArrayOfByte[paramInt1] == 60))
        paramInt1++;
      if (paramArrayOfByte[paramInt1] != 82)
        throw new RuntimeException("3. Unexpected value in file " + paramArrayOfByte[paramInt1] + " - please send to IDRsolutions for analysis");
      if (!paramBoolean)
      {
        arrayOfByte1 = this.objectReader.readObjectAsByteArray(paramPdfObject, this.objectReader.isCompressed(i1, i2), i1, i2);
        if (arrayOfByte1 == null)
        {
          paramPdfObject.setFullyResolved(false);
          if (LogWriter.isOutput())
            LogWriter.writeLog("[Linearized] " + paramPdfObject.getObjectRefAsString() + " not yet available (1)");
          paramInt1 = paramInt2;
          return paramInt1;
        }
        for (k = 3; (arrayOfByte1[(k - 1)] != 106) && (arrayOfByte1[(k - 2)] != 98) && (arrayOfByte1[(k - 3)] != 111); k++);
        while ((arrayOfByte1[k] == 10) || (arrayOfByte1[k] == 13) || (arrayOfByte1[k] == 32))
          k++;
        j = k;
      }
    }
    while (k < arrayOfByte1.length)
    {
      if ((arrayOfByte1[k] == 91) || (arrayOfByte1[k] == 40))
        m++;
      else if ((arrayOfByte1[k] == 93) || (arrayOfByte1[k] == 41))
        m--;
      if (m == 0)
        break;
      k++;
    }
    if (!paramBoolean)
    {
      n = k - j + 1;
      byte[] arrayOfByte2 = new byte[n];
      System.arraycopy(arrayOfByte1, j, arrayOfByte2, 0, n);
      if ((paramPdfObject.getObjectType() != 1113489015) && (this.decryption != null))
        try
        {
          arrayOfByte2 = this.decryption.decrypt(arrayOfByte2, paramPdfObject.getObjectRefAsString(), false, null, false, false);
        }
        catch (PdfSecurityException localPdfSecurityException)
        {
          if (LogWriter.isOutput())
            LogWriter.writeLog("Exception: " + localPdfSecurityException.getMessage());
        }
      paramPdfObject.setTextStreamValue(this.PDFkeyInt, arrayOfByte2);
    }
    if (i == 0)
      paramInt1 = k;
    return paramInt1;
  }

  private int setDictionaryValue(PdfObject paramPdfObject, int paramInt, byte[] paramArrayOfByte, boolean paramBoolean)
  {
    int i = paramInt;
    for (int j = paramInt; (paramArrayOfByte[j] == 10) || (paramArrayOfByte[j] == 32) || (paramArrayOfByte[j] == 9); j++);
    if ((paramArrayOfByte[j] == 110) && (paramArrayOfByte[(j + 1)] == 117) && (paramArrayOfByte[(j + 2)] == 108) && (paramArrayOfByte[(j + 3)] == 108))
    {
      paramInt = j + 4;
      return paramInt;
    }
    if ((paramArrayOfByte[j] == 91) && (paramArrayOfByte[(j + 1)] == 93))
    {
      paramInt = j;
      return paramInt;
    }
    if ((paramArrayOfByte[paramInt] != 60) && (paramArrayOfByte[(paramInt + 1)] != 60))
      i += 2;
    int k = 1;
    int m = paramArrayOfByte[(i - 1)] == 47 ? 1 : 0;
    while (k != 0)
      if ((paramArrayOfByte[i] == 60) && (paramArrayOfByte[(i + 1)] == 60))
      {
        int n = 1;
        i++;
        while (n > 0)
          if ((paramArrayOfByte[i] == 60) && (paramArrayOfByte[(i + 1)] == 60))
          {
            n++;
            i += 2;
          }
          else if ((paramArrayOfByte[(i - 1)] == 62) && (paramArrayOfByte[i] == 62))
          {
            n--;
            if (n > 0)
              i += 2;
          }
          else
          {
            i++;
          }
        k = 0;
      }
      else if (paramArrayOfByte[i] == 82)
      {
        k = 0;
      }
      else if ((m != 0) && ((paramArrayOfByte[i] == 32) || (paramArrayOfByte[i] == 13) || (paramArrayOfByte[i] == 10) || (paramArrayOfByte[i] == 9)))
      {
        k = 0;
      }
      else if (paramArrayOfByte[i] == 47)
      {
        k = 0;
        i--;
      }
      else if ((paramArrayOfByte[i] == 62) && (paramArrayOfByte[(i + 1)] == 62))
      {
        k = 0;
        i--;
      }
      else
      {
        i++;
      }
    readDictionary(paramPdfObject, paramInt, paramArrayOfByte, this.PDFkeyInt, paramBoolean);
    return i;
  }

  private int setFloatValue(PdfObject paramPdfObject, int paramInt1, byte[] paramArrayOfByte, int paramInt2)
  {
    paramInt1++;
    while ((paramArrayOfByte[paramInt1] == 10) || (paramArrayOfByte[paramInt1] == 13) || (paramArrayOfByte[paramInt1] == 32) || (paramArrayOfByte[paramInt1] == 47))
      paramInt1++;
    int i = paramInt1;
    while ((paramArrayOfByte[paramInt1] != 10) && (paramArrayOfByte[paramInt1] != 13) && (paramArrayOfByte[paramInt1] != 32) && (paramArrayOfByte[paramInt1] != 47) && (paramArrayOfByte[paramInt1] != 60) && (paramArrayOfByte[paramInt1] != 62))
      paramInt1++;
    float f = NumberUtils.parseFloat(i, paramInt1, paramArrayOfByte);
    for (int j = paramInt1; (j < paramInt2) && ((paramArrayOfByte[j] == 32) || (paramArrayOfByte[j] == 13) || (paramArrayOfByte[j] == 10)); j++);
    if ((paramArrayOfByte[j] >= 48) && (paramArrayOfByte[j] <= 57))
    {
      while ((paramArrayOfByte[paramInt1] == 10) || (paramArrayOfByte[paramInt1] == 13) || (paramArrayOfByte[paramInt1] == 32) || (paramArrayOfByte[paramInt1] == 47) || (paramArrayOfByte[paramInt1] == 60))
        paramInt1++;
      i = paramInt1;
      while ((paramArrayOfByte[paramInt1] != 10) && (paramArrayOfByte[paramInt1] != 13) && (paramArrayOfByte[paramInt1] != 32) && (paramArrayOfByte[paramInt1] != 47) && (paramArrayOfByte[paramInt1] != 60) && (paramArrayOfByte[paramInt1] != 62))
        paramInt1++;
      int k = NumberUtils.parseInt(i, paramInt1, paramArrayOfByte);
      while ((paramArrayOfByte[paramInt1] == 10) || (paramArrayOfByte[paramInt1] == 13) || (paramArrayOfByte[paramInt1] == 32) || (paramArrayOfByte[paramInt1] == 47) || (paramArrayOfByte[paramInt1] == 60))
        paramInt1++;
      if (paramArrayOfByte[paramInt1] != 82)
        throw new RuntimeException("3. Unexpected value in file - please send to IDRsolutions for analysis");
      byte[] arrayOfByte = this.objectReader.readObjectAsByteArray(paramPdfObject, this.objectReader.isCompressed((int)f, k), (int)f, k);
      if (arrayOfByte == null)
      {
        paramPdfObject.setFullyResolved(false);
        if (LogWriter.isOutput())
          LogWriter.writeLog("[Linearized] " + paramPdfObject.getObjectRefAsString() + " not yet available (3)");
        paramInt1 = paramInt2;
        return paramInt1;
      }
      for (int m = 3; (arrayOfByte[(m - 1)] != 106) && (arrayOfByte[(m - 2)] != 98) && (arrayOfByte[(m - 3)] != 111); m++);
      while ((arrayOfByte[m] == 10) || (arrayOfByte[m] == 13) || (arrayOfByte[m] == 32))
        m++;
      for (int n = m; (arrayOfByte[n] != 10) && (arrayOfByte[n] != 13) && (arrayOfByte[n] != 32); n++);
      f = NumberUtils.parseFloat(m, n, arrayOfByte);
    }
    paramPdfObject.setFloatNumber(this.PDFkeyInt, f);
    paramInt1--;
    return paramInt1;
  }

  private int setStringKeyValue(PdfObject paramPdfObject, int paramInt, byte[] paramArrayOfByte)
  {
    paramInt++;
    while ((paramArrayOfByte[paramInt] == 10) || (paramArrayOfByte[paramInt] == 13) || (paramArrayOfByte[paramInt] == 32) || (paramArrayOfByte[paramInt] == 47))
      paramInt++;
    int i = paramInt;
    int j = 1;
    int k = 0;
    while ((paramArrayOfByte[paramInt] != 82) && (k == 0))
    {
      if ((this.PDFkeyInt == 1110793845) && (paramArrayOfByte[paramInt] == 110) && (paramArrayOfByte[(paramInt + 1)] == 117) && (paramArrayOfByte[(paramInt + 2)] == 108) && (paramArrayOfByte[(paramInt + 3)] == 108))
        k = 1;
      paramInt++;
      j++;
    }
    paramInt--;
    if (k == 0)
    {
      byte[] arrayOfByte = new byte[j];
      System.arraycopy(paramArrayOfByte, i, arrayOfByte, 0, j);
      paramPdfObject.setStringKey(this.PDFkeyInt, arrayOfByte);
    }
    return paramInt;
  }

  private int setDictionaryValue(PdfObject paramPdfObject, int paramInt1, byte[] paramArrayOfByte, int paramInt2, boolean paramBoolean)
  {
    while ((paramArrayOfByte[paramInt1] == 10) || (paramArrayOfByte[paramInt1] == 13) || (paramArrayOfByte[paramInt1] == 32) || (paramArrayOfByte[paramInt1] == 47))
      paramInt1++;
    byte[] arrayOfByte = paramArrayOfByte;
    int i = paramInt1;
    int j = arrayOfByte[i] != 60 ? 1 : 0;
    int n;
    if (j != 0)
    {
      k = paramInt1;
      while ((paramArrayOfByte[paramInt1] != 10) && (paramArrayOfByte[paramInt1] != 13) && (paramArrayOfByte[paramInt1] != 32) && (paramArrayOfByte[paramInt1] != 47) && (paramArrayOfByte[paramInt1] != 60) && (paramArrayOfByte[paramInt1] != 62))
        paramInt1++;
      int m = NumberUtils.parseInt(k, paramInt1, paramArrayOfByte);
      while ((paramArrayOfByte[paramInt1] == 10) || (paramArrayOfByte[paramInt1] == 13) || (paramArrayOfByte[paramInt1] == 32) || (paramArrayOfByte[paramInt1] == 47) || (paramArrayOfByte[paramInt1] == 60))
        paramInt1++;
      k = paramInt1;
      while ((paramArrayOfByte[paramInt1] != 10) && (paramArrayOfByte[paramInt1] != 13) && (paramArrayOfByte[paramInt1] != 32) && (paramArrayOfByte[paramInt1] != 47) && (paramArrayOfByte[paramInt1] != 60) && (paramArrayOfByte[paramInt1] != 62))
        paramInt1++;
      n = NumberUtils.parseInt(k, paramInt1, paramArrayOfByte);
      while ((paramArrayOfByte[paramInt1] == 10) || (paramArrayOfByte[paramInt1] == 13) || (paramArrayOfByte[paramInt1] == 32) || (paramArrayOfByte[paramInt1] == 47) || (paramArrayOfByte[paramInt1] == 60))
        paramInt1++;
      if (paramArrayOfByte[paramInt1] != 82)
        throw new RuntimeException("3. Unexpected value in file " + paramArrayOfByte[paramInt1] + " - please send to IDRsolutions for analysis");
      if (!paramBoolean)
      {
        arrayOfByte = this.objectReader.readObjectAsByteArray(paramPdfObject, this.objectReader.isCompressed(m, n), m, n);
        if (arrayOfByte == null)
        {
          paramPdfObject.setFullyResolved(false);
          if (LogWriter.isOutput())
            LogWriter.writeLog("[Linearized] " + paramPdfObject.getObjectRefAsString() + " not yet available (2)");
          paramInt1 = paramInt2;
          return paramInt1;
        }
        if ((arrayOfByte[0] == 60) && (arrayOfByte[1] == 60))
        {
          i = 0;
        }
        else
        {
          i = 3;
          while ((arrayOfByte[(i - 1)] != 106) && (arrayOfByte[(i - 2)] != 98) && (arrayOfByte[(i - 3)] != 111))
            if (arrayOfByte[i] == 47)
            {
              i = 0;
            }
            else
            {
              i++;
              if (i == arrayOfByte.length)
                i = 0;
            }
          while ((arrayOfByte[i] == 10) || (arrayOfByte[i] == 13) || (arrayOfByte[i] == 32))
            i++;
        }
      }
    }
    for (int k = i; (arrayOfByte[k] == 60) || (arrayOfByte[k] == 32) || (arrayOfByte[k] == 13) || (arrayOfByte[k] == 10); k++);
    if (arrayOfByte[k] == 62)
    {
      i = k + 1;
    }
    else
    {
      PdfObject localPdfObject = ObjectFactory.createObject(this.PDFkeyInt, paramPdfObject.getObjectRefAsString(), paramPdfObject.getObjectType(), paramPdfObject.getID());
      localPdfObject.setID(this.PDFkeyInt);
      if (paramBoolean)
      {
        i = readKeyPairs(this.PDFkeyInt, arrayOfByte, i, -2, null);
      }
      else
      {
        n = readKeyPairs(this.PDFkeyInt, arrayOfByte, i, -1, null);
        i = readKeyPairs(this.PDFkeyInt, arrayOfByte, i, n, localPdfObject);
        paramPdfObject.setDictionary(this.PDFkeyInt, localPdfObject);
      }
    }
    if (j == 0)
      paramInt1 = i;
    return paramInt1;
  }

  private int setStringConstantValue(PdfObject paramPdfObject, int paramInt, byte[] paramArrayOfByte)
  {
    paramInt++;
    while ((paramArrayOfByte[paramInt] == 10) || (paramArrayOfByte[paramInt] == 13) || (paramArrayOfByte[paramInt] == 32) || (paramArrayOfByte[paramInt] == 47))
      paramInt++;
    int i = paramInt;
    for (int j = 0; (paramArrayOfByte[paramInt] != 10) && (paramArrayOfByte[paramInt] != 13) && (paramArrayOfByte[paramInt] != 32) && (paramArrayOfByte[paramInt] != 47) && (paramArrayOfByte[paramInt] != 60) && (paramArrayOfByte[paramInt] != 62); j++)
      paramInt++;
    paramInt--;
    paramPdfObject.setConstant(this.PDFkeyInt, i, j, paramArrayOfByte);
    return paramInt;
  }

  private int setBooleanValue(PdfObject paramPdfObject, int paramInt1, byte[] paramArrayOfByte, int paramInt2)
  {
    paramInt1++;
    while ((paramArrayOfByte[paramInt1] == 10) || (paramArrayOfByte[paramInt1] == 13) || (paramArrayOfByte[paramInt1] == 32) || (paramArrayOfByte[paramInt1] == 47))
      paramInt1++;
    int i = paramInt1;
    while ((paramArrayOfByte[paramInt1] != 10) && (paramArrayOfByte[paramInt1] != 13) && (paramArrayOfByte[paramInt1] != 32) && (paramArrayOfByte[paramInt1] != 47) && (paramArrayOfByte[paramInt1] != 60) && (paramArrayOfByte[paramInt1] != 62))
      paramInt1++;
    paramInt1--;
    if ((paramArrayOfByte[i] == 116) && (paramArrayOfByte[(i + 1)] == 114) && (paramArrayOfByte[(i + 2)] == 117) && (paramArrayOfByte[(i + 3)] == 101))
      paramPdfObject.setBoolean(paramInt2, true);
    else if ((paramArrayOfByte[i] == 102) && (paramArrayOfByte[(i + 1)] == 97) && (paramArrayOfByte[(i + 2)] == 108) && (paramArrayOfByte[(i + 3)] == 115) && (paramArrayOfByte[(i + 4)] == 101))
      paramPdfObject.setBoolean(paramInt2, false);
    else
      throw new RuntimeException("Unexpected value for Boolean value for" + paramInt2 + '=' + this.PDFkey);
    return paramInt1;
  }

  private int setTextStreamValue(PdfObject paramPdfObject, int paramInt, byte[] paramArrayOfByte, boolean paramBoolean)
  {
    if ((paramArrayOfByte[(paramInt + 1)] == 40) && (paramArrayOfByte[(paramInt + 2)] == 41))
    {
      paramInt += 3;
      paramPdfObject.setTextStreamValue(this.PDFkeyInt, new byte[1]);
      if (paramArrayOfByte[paramInt] == 47)
        paramInt--;
    }
    else
    {
      paramInt = readTextStream(paramPdfObject, paramInt, paramArrayOfByte, this.PDFkeyInt, paramBoolean);
    }
    return paramInt;
  }

  private void setFieldNames(PdfObject paramPdfObject)
  {
    String str1 = paramPdfObject.getTextStreamValue(36);
    if (str1 != null)
    {
      String str2 = paramPdfObject.getStringKey(1110793845);
      int i = 0;
      while (str2 != null)
      {
        FormObject localFormObject = new FormObject(str2, false);
        this.objectReader.readObject(localFormObject);
        String str3 = localFormObject.getTextStreamValue(36);
        if (str3 == null)
          break;
        if ((!str1.equals(str3)) || (!str2.equals(paramPdfObject.getObjectRefAsString())))
        {
          str1 = str3 + '.' + str1;
          i = 1;
        }
        str2 = localFormObject.getParentRef();
      }
      if (i != 0)
        paramPdfObject.setTextStreamValue(36, StringUtils.toBytes(str1));
    }
  }

  private void readStreamData(PdfObject paramPdfObject, int paramInt1, byte[] paramArrayOfByte, int paramInt2)
  {
    for (int i = paramInt1; (i < paramInt2 - 5) && ((paramArrayOfByte[i] != 62) || (paramArrayOfByte[(i + 1)] != 62)); i++)
      if ((paramArrayOfByte[i] == 115) && (paramArrayOfByte[(i + 1)] == 116) && (paramArrayOfByte[(i + 2)] == 114) && (paramArrayOfByte[(i + 3)] == 101) && (paramArrayOfByte[(i + 4)] == 97) && (paramArrayOfByte[(i + 5)] == 109))
      {
        if (!paramPdfObject.isCached())
          readStreamIntoObject(paramPdfObject, i, paramArrayOfByte);
        i = paramInt2;
      }
  }

  private static int getPairedValues(PdfObject paramPdfObject, int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    int i = 0;
    int j = paramInt1;
    while (j < paramInt3)
    {
      while ((j < paramInt3) && ((paramArrayOfByte[j] == 32) || (paramArrayOfByte[j] == 10) || (paramArrayOfByte[j] == 13) || (paramArrayOfByte[j] == 10)))
        j++;
      if ((j < paramInt3) && (paramArrayOfByte[j] >= 48) && (paramArrayOfByte[j] <= 57))
      {
        while ((j < paramInt3) && (paramArrayOfByte[j] >= 48) && (paramArrayOfByte[j] <= 57))
          j++;
        while ((j < paramInt3) && ((paramArrayOfByte[j] == 32) || (paramArrayOfByte[j] == 10) || (paramArrayOfByte[j] == 13) || (paramArrayOfByte[j] == 10)))
          j++;
        while ((j < paramInt3) && (((paramArrayOfByte[j] >= 48) && (paramArrayOfByte[j] <= 57)) || (paramArrayOfByte[j] == 32) || (paramArrayOfByte[j] == 10) || (paramArrayOfByte[j] == 13) || (paramArrayOfByte[j] == 10)))
          j++;
        if ((j < paramInt3) && (paramArrayOfByte[j] == 82))
          j++;
      }
      else
      {
        while ((j < paramInt3) && ((paramArrayOfByte[j] == 32) || (paramArrayOfByte[j] == 10) || (paramArrayOfByte[j] == 13) || (paramArrayOfByte[j] == 10)))
          j++;
        if ((paramArrayOfByte[j] == 62) && (paramArrayOfByte[(j + 1)] == 62))
        {
          i = 1;
        }
        else if (paramArrayOfByte[j] == 47)
        {
          j++;
          while ((j < paramInt3) && (paramArrayOfByte[j] != 32) && (paramArrayOfByte[j] != 10) && (paramArrayOfByte[j] != 13) && (paramArrayOfByte[j] != 10))
            j++;
        }
      }
    }
    if (i != 0)
    {
      paramPdfObject.setCurrentKey(PdfDictionary.getKey(paramInt5, paramInt4, paramArrayOfByte));
      return 5;
    }
    return paramInt2;
  }

  private int setUnreadDictionaryValue(PdfObject paramPdfObject, int paramInt, byte[] paramArrayOfByte)
  {
    if (paramArrayOfByte[paramInt] != 60)
      paramInt++;
    while ((paramArrayOfByte[paramInt] == 10) || (paramArrayOfByte[paramInt] == 13) || (paramArrayOfByte[paramInt] == 32) || (paramArrayOfByte[paramInt] == 9))
      paramInt++;
    int i = paramInt;
    PdfObject localPdfObject = ObjectFactory.createObject(this.PDFkeyInt, paramPdfObject.getObjectRefAsString(), paramPdfObject.getObjectType(), paramPdfObject.getID());
    localPdfObject.setID(this.PDFkeyInt);
    if ((paramArrayOfByte[paramInt] != 110) || (paramArrayOfByte[(paramInt + 1)] != 117) || (paramArrayOfByte[(paramInt + 2)] != 108) || (paramArrayOfByte[(paramInt + 3)] != 108))
      paramPdfObject.setDictionary(this.PDFkeyInt, localPdfObject);
    int m = 2;
    int n;
    byte[] arrayOfByte;
    if (paramArrayOfByte[paramInt] == 47)
    {
      while ((paramArrayOfByte[paramInt] == 10) || (paramArrayOfByte[paramInt] == 13) || (paramArrayOfByte[paramInt] == 32) || (paramArrayOfByte[paramInt] == 47) || (paramArrayOfByte[paramInt] == 60))
        paramInt++;
      int j = paramInt;
      for (int k = 0; (paramArrayOfByte[paramInt] != 10) && (paramArrayOfByte[paramInt] != 13) && (paramArrayOfByte[paramInt] != 32) && (paramArrayOfByte[paramInt] != 47) && (paramArrayOfByte[paramInt] != 60) && (paramArrayOfByte[paramInt] != 62); k++)
        paramInt++;
      paramInt--;
      n = localPdfObject.setConstant(this.PDFkeyInt, j, k, paramArrayOfByte);
      if ((n == -1) || (this.isInlineImage))
      {
        arrayOfByte = new byte[k];
        System.arraycopy(paramArrayOfByte, j, arrayOfByte, 0, k);
        String str = new String(arrayOfByte);
        localPdfObject.setGeneralStringValue(str);
      }
      m = 0;
    }
    else if ((paramArrayOfByte[paramInt] != 101) || (paramArrayOfByte[(paramInt + 1)] != 110) || (paramArrayOfByte[(paramInt + 2)] != 100) || (paramArrayOfByte[(paramInt + 3)] != 111) || (paramArrayOfByte[(paramInt + 4)] != 98))
    {
      while (((paramArrayOfByte[paramInt] == 91) && (this.PDFkeyInt != 2087749783)) || (paramArrayOfByte[paramInt] == 32) || (paramArrayOfByte[paramInt] == 13) || (paramArrayOfByte[paramInt] == 10))
        paramInt++;
      if ((paramArrayOfByte[paramInt] == 60) && (paramArrayOfByte[(paramInt + 1)] == 60))
      {
        paramInt += 2;
        n = 1;
        while (n > 0)
          if ((paramArrayOfByte[paramInt] == 60) && (paramArrayOfByte[(paramInt + 1)] == 60))
          {
            paramInt += 2;
            n++;
          }
          else if ((paramArrayOfByte[paramInt] == 62) && (paramInt + 1 == paramArrayOfByte.length))
          {
            n = 0;
          }
          else if ((paramArrayOfByte[paramInt] == 62) && (paramArrayOfByte[(paramInt + 1)] == 62))
          {
            paramInt += 2;
            n--;
          }
          else
          {
            paramInt++;
          }
      }
      else if (paramArrayOfByte[paramInt] == 91)
      {
        paramInt++;
        n = 1;
        while (n > 0)
        {
          if (paramArrayOfByte[paramInt] == 40)
          {
            paramInt++;
            while ((paramArrayOfByte[paramInt] != 41) || (ObjectUtils.isEscaped(paramArrayOfByte, paramInt)))
              paramInt++;
          }
          if (paramArrayOfByte[paramInt] == 91)
            n++;
          else if (paramArrayOfByte[paramInt] == 93)
            n--;
          paramInt++;
        }
        paramInt--;
      }
      else if ((paramArrayOfByte[paramInt] == 110) && (paramArrayOfByte[(paramInt + 1)] == 117) && (paramArrayOfByte[(paramInt + 2)] == 108) && (paramArrayOfByte[(paramInt + 3)] == 108))
      {
        paramInt += 4;
      }
      else
      {
        m = 1;
        while ((paramArrayOfByte[paramInt] != 82) || (paramArrayOfByte[(paramInt - 1)] == 101))
        {
          paramInt++;
          if (paramInt == paramArrayOfByte.length)
            break;
        }
        paramInt++;
        if (paramInt >= paramArrayOfByte.length)
          paramInt = paramArrayOfByte.length - 1;
      }
    }
    localPdfObject.setStatus(m);
    if (m != 0)
    {
      n = paramInt - i;
      arrayOfByte = new byte[n];
      System.arraycopy(paramArrayOfByte, i, arrayOfByte, 0, n);
      if (arrayOfByte[(n - 1)] == 82)
        for (int i1 = 0; i1 < n; i1++)
          if ((arrayOfByte[i1] == 10) || (arrayOfByte[i1] == 13))
            arrayOfByte[i1] = 32;
      localPdfObject.setUnresolvedData(arrayOfByte, this.PDFkeyInt);
    }
    if ((paramArrayOfByte[paramInt] == 47) || (paramArrayOfByte[paramInt] == 62))
      paramInt--;
    return paramInt;
  }

  int readDictionary(PdfObject paramPdfObject, int paramInt1, byte[] paramArrayOfByte, int paramInt2, boolean paramBoolean)
  {
    String str1 = paramPdfObject.getObjectRefAsString();
    if (paramArrayOfByte[paramInt1] != 60)
      paramInt1++;
    while ((paramArrayOfByte[paramInt1] == 10) || (paramArrayOfByte[paramInt1] == 13) || (paramArrayOfByte[paramInt1] == 32))
      paramInt1++;
    Object localObject;
    if (paramArrayOfByte[paramInt1] == 47)
    {
      while ((paramArrayOfByte[paramInt1] == 10) || (paramArrayOfByte[paramInt1] == 13) || (paramArrayOfByte[paramInt1] == 32) || (paramArrayOfByte[paramInt1] == 47) || (paramArrayOfByte[paramInt1] == 60))
        paramInt1++;
      int j = paramInt1;
      for (int i = 0; (paramArrayOfByte[paramInt1] != 10) && (paramArrayOfByte[paramInt1] != 13) && (paramArrayOfByte[paramInt1] != 32) && (paramArrayOfByte[paramInt1] != 47) && (paramArrayOfByte[paramInt1] != 60) && (paramArrayOfByte[paramInt1] != 62); i++)
        paramInt1++;
      paramInt1--;
      if (!paramBoolean)
      {
        PdfObject localPdfObject = ObjectFactory.createObject(paramInt2, str1, paramPdfObject.getObjectType(), paramPdfObject.getID());
        localPdfObject.setID(paramInt2);
        int m = localPdfObject.setConstant(paramInt2, j, i, paramArrayOfByte);
        if ((m == -1) || (this.isInlineImage))
        {
          localObject = new byte[i];
          System.arraycopy(paramArrayOfByte, j, localObject, 0, i);
          String str2 = new String((byte[])localObject);
          localPdfObject.setGeneralStringValue(str2);
        }
        paramPdfObject.setDictionary(paramInt2, localPdfObject);
        if (paramPdfObject.isDataExternal())
        {
          localPdfObject.isDataExternal(true);
          if (!resolveFully(localPdfObject))
            paramPdfObject.setFullyResolved(false);
        }
      }
    }
    else if ((paramArrayOfByte[paramInt1] != 101) || (paramArrayOfByte[(paramInt1 + 1)] != 110) || (paramArrayOfByte[(paramInt1 + 2)] != 100) || (paramArrayOfByte[(paramInt1 + 3)] != 111) || (paramArrayOfByte[(paramInt1 + 4)] != 98))
    {
      int k;
      if ((paramArrayOfByte[paramInt1] == 40) && (paramInt2 == 6691))
      {
        paramInt1++;
        k = paramInt1;
        while (paramInt1 < paramArrayOfByte.length)
        {
          paramInt1++;
          if ((paramArrayOfByte[paramInt1] == 41) && (!ObjectUtils.isEscaped(paramArrayOfByte, paramInt1)))
            break;
        }
        byte[] arrayOfByte = ObjectUtils.readEscapedValue(paramInt1, paramArrayOfByte, k, false);
        localObject = new NamesObject(str1);
        ((NamesObject)localObject).setDecodedStream(arrayOfByte);
        paramPdfObject.setDictionary(6691, (PdfObject)localObject);
      }
      else if (paramBoolean)
      {
        while ((paramArrayOfByte[paramInt1] == 91) || (paramArrayOfByte[paramInt1] == 32) || (paramArrayOfByte[paramInt1] == 13) || (paramArrayOfByte[paramInt1] == 10))
          paramInt1++;
        if ((paramArrayOfByte[paramInt1] == 60) && (paramArrayOfByte[(paramInt1 + 1)] == 60))
        {
          paramInt1 += 2;
          k = 1;
          while (k > 0)
            if ((paramArrayOfByte[paramInt1] == 60) && (paramArrayOfByte[(paramInt1 + 1)] == 60))
            {
              paramInt1 += 2;
              k++;
            }
            else if ((paramArrayOfByte[paramInt1] == 62) && (paramArrayOfByte[(paramInt1 + 1)] == 62))
            {
              paramInt1 += 2;
              k--;
            }
            else
            {
              paramInt1++;
            }
          paramInt1--;
        }
        else
        {
          paramInt1 = readDictionaryFromRefOrDirect(paramInt2, paramPdfObject, str1, paramInt1, paramArrayOfByte, paramInt2);
        }
        if ((paramInt1 < paramArrayOfByte.length) && (paramArrayOfByte[paramInt1] == 47))
          paramInt1--;
      }
      else
      {
        paramInt1 = readDictionaryFromRefOrDirect(paramInt2, paramPdfObject, str1, paramInt1, paramArrayOfByte, paramInt2);
      }
    }
    return paramInt1;
  }

  private int readTextStream(PdfObject paramPdfObject, int paramInt1, byte[] paramArrayOfByte, int paramInt2, boolean paramBoolean)
  {
    int k;
    int m;
    int n;
    int i1;
    int i4;
    if ((paramInt2 == 39) || (paramInt2 == 9986))
    {
      if (paramInt2 == 9986)
        paramInt1++;
      int i = 0;
      while (paramArrayOfByte[paramInt1] != 91)
      {
        if (paramArrayOfByte[paramInt1] == 40)
        {
          i = 0;
          break;
        }
        if ((paramArrayOfByte[paramInt1] >= 48) && (paramArrayOfByte[paramInt1] <= 57))
        {
          i = 1;
          break;
        }
        paramInt1++;
      }
      byte[] arrayOfByte2 = paramArrayOfByte;
      k = paramInt1;
      m = paramInt1;
      n = 0;
      if (i != 0)
      {
        i1 = paramInt1;
        while ((paramArrayOfByte[paramInt1] != 10) && (paramArrayOfByte[paramInt1] != 13) && (paramArrayOfByte[paramInt1] != 32) && (paramArrayOfByte[paramInt1] != 47) && (paramArrayOfByte[paramInt1] != 60) && (paramArrayOfByte[paramInt1] != 62))
          paramInt1++;
        int i2 = NumberUtils.parseInt(i1, paramInt1, paramArrayOfByte);
        while ((paramArrayOfByte[paramInt1] == 10) || (paramArrayOfByte[paramInt1] == 13) || (paramArrayOfByte[paramInt1] == 32) || (paramArrayOfByte[paramInt1] == 47) || (paramArrayOfByte[paramInt1] == 60))
          paramInt1++;
        i1 = paramInt1;
        while ((paramArrayOfByte[paramInt1] != 10) && (paramArrayOfByte[paramInt1] != 13) && (paramArrayOfByte[paramInt1] != 32) && (paramArrayOfByte[paramInt1] != 47) && (paramArrayOfByte[paramInt1] != 60) && (paramArrayOfByte[paramInt1] != 62))
          paramInt1++;
        i4 = NumberUtils.parseInt(i1, paramInt1, paramArrayOfByte);
        while ((paramArrayOfByte[paramInt1] == 10) || (paramArrayOfByte[paramInt1] == 13) || (paramArrayOfByte[paramInt1] == 32) || (paramArrayOfByte[paramInt1] == 47) || (paramArrayOfByte[paramInt1] == 60))
          paramInt1++;
        if (paramArrayOfByte[paramInt1] != 82)
          throw new RuntimeException("3. Unexpected value in file " + paramArrayOfByte[paramInt1] + " - please send to IDRsolutions for analysis");
        if (!paramBoolean)
        {
          arrayOfByte2 = this.objectReader.readObjectAsByteArray(paramPdfObject, this.objectReader.isCompressed(i2, i4), i2, i4);
          if (arrayOfByte2 == null)
          {
            paramPdfObject.setFullyResolved(false);
            if (LogWriter.isOutput())
              LogWriter.writeLog("[Linearized] " + paramPdfObject.getObjectRefAsString() + " not yet available (6)");
            return paramArrayOfByte.length;
          }
          m = 3;
          while ((arrayOfByte2[(m - 1)] != 106) && (arrayOfByte2[(m - 2)] != 98) && (arrayOfByte2[(m - 3)] != 111))
          {
            m++;
            if (m == arrayOfByte2.length)
              m = 0;
          }
          while ((arrayOfByte2[m] == 10) || (arrayOfByte2[m] == 13) || (arrayOfByte2[m] == 32))
            m++;
          k = m;
        }
      }
      while (m < arrayOfByte2.length)
      {
        if ((arrayOfByte2[m] == 91) || (arrayOfByte2[m] == 40))
          n++;
        else if ((arrayOfByte2[m] == 93) || (arrayOfByte2[m] == 41))
          n--;
        if (n == 0)
          break;
        m++;
      }
      if (!paramBoolean)
      {
        i1 = m - k + 1;
        byte[] arrayOfByte4 = new byte[i1];
        System.arraycopy(arrayOfByte2, k, arrayOfByte4, 0, i1);
        if (paramInt2 != 6691)
          for (i4 = 0; i4 < i1; i4++)
            if ((arrayOfByte4[i4] == 10) || (arrayOfByte4[i4] == 13))
              arrayOfByte4[i4] = 32;
        paramPdfObject.setTextStreamValue(paramInt2, arrayOfByte4);
      }
      if (i == 0)
        paramInt1 = m;
    }
    else
    {
      try
      {
        if ((paramArrayOfByte[paramInt1] != 60) && (paramArrayOfByte[paramInt1] != 40))
          paramInt1++;
        while ((paramArrayOfByte[paramInt1] == 10) || (paramArrayOfByte[paramInt1] == 13) || (paramArrayOfByte[paramInt1] == 32))
          paramInt1++;
        if (paramArrayOfByte[paramInt1] == 47)
        {
          paramPdfObject.setTextStreamValue(paramInt2, new byte[1]);
          paramInt1--;
          return paramInt1;
        }
        int j = (paramArrayOfByte[paramInt1] != 60) && (paramArrayOfByte[paramInt1] != 40) ? 1 : 0;
        k = paramInt1;
        byte[] arrayOfByte1 = paramArrayOfByte;
        if (j != 0)
        {
          m = paramInt1;
          while ((paramArrayOfByte[paramInt1] != 10) && (paramArrayOfByte[paramInt1] != 13) && (paramArrayOfByte[paramInt1] != 32) && (paramArrayOfByte[paramInt1] != 47) && (paramArrayOfByte[paramInt1] != 60) && (paramArrayOfByte[paramInt1] != 62))
            paramInt1++;
          n = NumberUtils.parseInt(m, paramInt1, paramArrayOfByte);
          while ((paramArrayOfByte[paramInt1] == 10) || (paramArrayOfByte[paramInt1] == 13) || (paramArrayOfByte[paramInt1] == 32) || (paramArrayOfByte[paramInt1] == 47) || (paramArrayOfByte[paramInt1] == 60))
            paramInt1++;
          m = paramInt1;
          while ((paramArrayOfByte[paramInt1] != 10) && (paramArrayOfByte[paramInt1] != 13) && (paramArrayOfByte[paramInt1] != 32) && (paramArrayOfByte[paramInt1] != 47) && (paramArrayOfByte[paramInt1] != 60) && (paramArrayOfByte[paramInt1] != 62))
            paramInt1++;
          i1 = NumberUtils.parseInt(m, paramInt1, paramArrayOfByte);
          while ((paramArrayOfByte[paramInt1] == 10) || (paramArrayOfByte[paramInt1] == 13) || (paramArrayOfByte[paramInt1] == 32) || (paramArrayOfByte[paramInt1] == 47) || (paramArrayOfByte[paramInt1] == 60))
            paramInt1++;
          if (paramArrayOfByte[paramInt1] != 82)
            return paramArrayOfByte.length;
          if (!paramBoolean)
          {
            arrayOfByte1 = this.objectReader.readObjectAsByteArray(paramPdfObject, this.objectReader.isCompressed(n, i1), n, i1);
            if (arrayOfByte1 == null)
            {
              paramPdfObject.setFullyResolved(false);
              if (LogWriter.isOutput())
                LogWriter.writeLog("[Linearized] " + paramPdfObject.getObjectRefAsString() + " not yet available (7)");
              return paramArrayOfByte.length;
            }
            if (arrayOfByte1[0] == 40)
            {
              k = 0;
            }
            else
            {
              for (k = 3; (arrayOfByte1[(k - 1)] != 106) && (arrayOfByte1[(k - 2)] != 98) && (arrayOfByte1[(k - 3)] != 111); k++);
              while ((arrayOfByte1[k] == 10) || (arrayOfByte1[k] == 13) || (arrayOfByte1[k] == 32))
                k++;
            }
          }
        }
        m = 0;
        if ((j == 0) || (!paramBoolean))
        {
          while ((arrayOfByte1[k] != 40) && (arrayOfByte1[k] != 60))
            k++;
          n = arrayOfByte1[k];
          m = k;
          i1 = 1;
          while (k < arrayOfByte1.length)
          {
            k++;
            if ((n == 40) && ((arrayOfByte1[k] == 41) || (arrayOfByte1[k] == 40)) && (!ObjectUtils.isEscaped(arrayOfByte1, k)))
            {
              if (arrayOfByte1[k] == 40)
                i1++;
              else if (arrayOfByte1[k] == 41)
                i1--;
              if (i1 == 0)
                break;
            }
            else if (n == 60)
            {
              if (arrayOfByte1[k] != 62)
                if (arrayOfByte1[k] == 0)
                  break;
            }
          }
        }
        if (!paramBoolean)
        {
          byte[] arrayOfByte3;
          if (arrayOfByte1[m] == 60)
          {
            m++;
            i1 = k - m >> 1;
            arrayOfByte3 = new byte[i1];
            for (int i3 = 0; m != k; i3++)
            {
              while ((arrayOfByte1[m] == 32) || (arrayOfByte1[m] == 10) || (arrayOfByte1[m] == 13))
                m++;
              i4 = arrayOfByte1[m];
              if ((i4 >= 65) && (i4 <= 70))
                i4 -= 55;
              else if ((i4 >= 97) && (i4 <= 102))
                i4 -= 87;
              else if ((i4 >= 48) && (i4 <= 57))
                i4 -= 48;
              else if (LogWriter.isOutput())
                LogWriter.writeLog("Unexpected number " + (char)arrayOfByte1[m]);
              m++;
              while ((arrayOfByte1[m] == 32) || (arrayOfByte1[m] == 10) || (arrayOfByte1[m] == 13))
                m++;
              int i5 = arrayOfByte1[m];
              if ((i5 >= 65) && (i5 <= 70))
              {
                i5 -= 55;
              }
              else if ((i5 >= 97) && (i5 <= 102))
              {
                i5 -= 87;
              }
              else if ((i5 >= 48) && (i5 <= 57))
              {
                i5 -= 48;
              }
              else
              {
                if (LogWriter.isOutput())
                  LogWriter.writeLog("Unexpected number " + (char)arrayOfByte1[m]);
                return paramInt1;
              }
              m++;
              int i6 = i5 + (i4 << 4);
              arrayOfByte3[i3] = ((byte)i6);
            }
          }
          else
          {
            if (arrayOfByte1[m] == 40)
              m++;
            arrayOfByte3 = ObjectUtils.readEscapedValue(k, arrayOfByte1, m, paramInt2 == 6420);
          }
          if (paramPdfObject.getObjectType() != 1113489015)
            try
            {
              if ((this.decryption != null) && (!paramPdfObject.isInCompressedStream()))
                arrayOfByte3 = this.decryption.decryptString(arrayOfByte3, paramPdfObject.getObjectRefAsString());
            }
            catch (PdfSecurityException localPdfSecurityException)
            {
              if (LogWriter.isOutput())
                LogWriter.writeLog("Exception: " + localPdfSecurityException.getMessage());
            }
          paramPdfObject.setTextStreamValue(paramInt2, arrayOfByte3);
        }
        if (j == 0)
          paramInt1 = k;
      }
      catch (Exception localException)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog("Exception: " + localException.getMessage());
      }
    }
    return paramInt1;
  }

  int setNumberValue(PdfObject paramPdfObject, int paramInt1, byte[] paramArrayOfByte, int paramInt2)
  {
    int i = paramInt1;
    int j = paramArrayOfByte.length;
    while ((paramArrayOfByte[paramInt1] != 10) && (paramArrayOfByte[paramInt1] != 13) && (paramArrayOfByte[paramInt1] != 32) && (paramArrayOfByte[paramInt1] != 47) && (paramArrayOfByte[paramInt1] != 60) && (paramArrayOfByte[paramInt1] != 62) && (paramArrayOfByte[paramInt1] != 40) && (paramArrayOfByte[paramInt1] != 46))
      paramInt1++;
    int k = NumberUtils.parseInt(i, paramInt1, paramArrayOfByte);
    for (int m = paramInt1; (m < j) && ((paramArrayOfByte[m] == 32) || (paramArrayOfByte[m] == 13) || (paramArrayOfByte[m] == 10)); m++);
    int n = 0;
    int i1;
    if ((paramArrayOfByte[m] >= 48) && (paramArrayOfByte[m] <= 57))
    {
      for (i1 = m; (paramArrayOfByte[i1] != 10) && (paramArrayOfByte[i1] != 13) && (paramArrayOfByte[i1] != 32) && (paramArrayOfByte[i1] != 47) && (paramArrayOfByte[i1] != 60) && (paramArrayOfByte[i1] != 62); i1++);
      while ((i1 < j) && ((paramArrayOfByte[i1] == 10) || (paramArrayOfByte[i1] == 13) || (paramArrayOfByte[i1] == 32) || (paramArrayOfByte[i1] == 47)))
        i1++;
      n = (i1 < j) && (paramArrayOfByte[i1] == 82) ? 1 : 0;
    }
    if (n != 0)
    {
      while ((paramArrayOfByte[paramInt1] == 10) || (paramArrayOfByte[paramInt1] == 13) || (paramArrayOfByte[paramInt1] == 32) || (paramArrayOfByte[paramInt1] == 47) || (paramArrayOfByte[paramInt1] == 60))
        paramInt1++;
      i = paramInt1;
      while ((paramArrayOfByte[paramInt1] != 10) && (paramArrayOfByte[paramInt1] != 13) && (paramArrayOfByte[paramInt1] != 32) && (paramArrayOfByte[paramInt1] != 47) && (paramArrayOfByte[paramInt1] != 60) && (paramArrayOfByte[paramInt1] != 62))
        paramInt1++;
      i1 = NumberUtils.parseInt(i, paramInt1, paramArrayOfByte);
      while ((paramArrayOfByte[paramInt1] == 10) || (paramArrayOfByte[paramInt1] == 13) || (paramArrayOfByte[paramInt1] == 32) || (paramArrayOfByte[paramInt1] == 47) || (paramArrayOfByte[paramInt1] == 60))
        paramInt1++;
      if (paramArrayOfByte[paramInt1] != 82)
        throw new RuntimeException("3. Unexpected value in file - please send to IDRsolutions for analysis");
      byte[] arrayOfByte = this.objectReader.readObjectAsByteArray(paramPdfObject, this.objectReader.isCompressed(k, i1), k, i1);
      if (arrayOfByte == null)
      {
        paramPdfObject.setFullyResolved(false);
        if (LogWriter.isOutput())
          LogWriter.writeLog("[Linearized] " + paramPdfObject.getObjectRefAsString() + " not yet available (8)");
        return j;
      }
      int i2 = 0;
      int i3 = arrayOfByte.length;
      if (((i3 <= 1) || (arrayOfByte[0] != 60) || (arrayOfByte[1] != 60)) && (i3 > 3))
      {
        i2 = 3;
        if (i3 > 3)
          while ((arrayOfByte[(i2 - 1)] != 106) && (arrayOfByte[(i2 - 2)] != 98) && (arrayOfByte[(i2 - 3)] != 111))
          {
            i2++;
            if (i2 == i3)
              i2 = 0;
          }
      }
      if (i3 > 1)
        while ((i2 < arrayOfByte.length) && ((arrayOfByte[i2] == 9) || (arrayOfByte[i2] == 10) || (arrayOfByte[i2] == 13) || (arrayOfByte[i2] == 32)))
          i2++;
      for (int i4 = i2; (i4 < i3) && (arrayOfByte[i4] != 9) && (arrayOfByte[i4] != 10) && (arrayOfByte[i4] != 13) && (arrayOfByte[i4] != 32); i4++);
      k = NumberUtils.parseInt(i2, i4, arrayOfByte);
    }
    paramPdfObject.setIntNumber(paramInt2, k);
    paramInt1--;
    return paramInt1;
  }

  int readDictionaryFromRefOrDirect(int paramInt1, PdfObject paramPdfObject, String paramString, int paramInt2, byte[] paramArrayOfByte, int paramInt3)
  {
    int j = -1;
    while ((paramArrayOfByte[paramInt2] == 91) || (paramArrayOfByte[paramInt2] == 32) || (paramArrayOfByte[paramInt2] == 13) || (paramArrayOfByte[paramInt2] == 10))
    {
      if (paramArrayOfByte[paramInt2] == 91)
        j = paramInt2;
      paramInt2++;
    }
    if ((paramInt3 == 2087749783) || (paramInt1 == 2087749783) || (paramPdfObject.getPDFkeyInt() == 2087749783))
    {
      ColorObjectDecoder localColorObjectDecoder = new ColorObjectDecoder(this.objectReader);
      return localColorObjectDecoder.processColorSpace(paramPdfObject, paramPdfObject.getObjectRefAsString(), paramInt2, paramArrayOfByte);
    }
    if ((j != -1) && ((paramInt3 == 489767739) || (paramInt3 == 9250) || (paramInt3 == 2037870513)))
      return processArray(paramPdfObject, paramArrayOfByte, paramInt3, j);
    if (paramArrayOfByte[paramInt2] == 37)
    {
      while ((paramArrayOfByte[paramInt2] != 13) && (paramArrayOfByte[paramInt2] != 10))
        paramInt2++;
      while ((paramArrayOfByte[paramInt2] == 91) || (paramArrayOfByte[paramInt2] == 32) || (paramArrayOfByte[paramInt2] == 13) || (paramArrayOfByte[paramInt2] == 10))
        paramInt2++;
    }
    if (paramArrayOfByte[paramInt2] == 60)
    {
      paramInt2 = convertDirectDictionaryToObject(paramPdfObject, paramString, paramInt2, paramArrayOfByte, paramInt3);
    }
    else if (paramArrayOfByte[paramInt2] == 47)
    {
      paramInt2 = ObjectUtils.setDirectValue(paramPdfObject, paramInt2, paramArrayOfByte, paramInt3);
    }
    else
    {
      int k = paramInt2;
      byte[] arrayOfByte = paramArrayOfByte;
      int i1;
      int i;
      int m;
      int n;
      int i2;
      while (true)
        if ((arrayOfByte[k] == 91) || (arrayOfByte[k] == 32) || (arrayOfByte[k] == 13) || (arrayOfByte[k] == 10))
        {
          k++;
        }
        else
        {
          if (arrayOfByte[k] == 93)
            return k;
          i1 = 0;
          do
          {
            if ((i1 != 0) && (arrayOfByte[k] == 93))
              return k;
            i = k;
            while ((arrayOfByte[k] != 10) && (arrayOfByte[k] != 13) && (arrayOfByte[k] != 32) && (arrayOfByte[k] != 47) && (arrayOfByte[k] != 60) && (arrayOfByte[k] != 62))
            {
              if ((arrayOfByte[k] == 108) && (arrayOfByte[(k - 1)] == 108) && (arrayOfByte[(k - 2)] == 117) && (arrayOfByte[(k - 3)] == 110))
                i1 = 1;
              if ((i1 != 0) && (arrayOfByte[k] == 93))
                return k;
              k++;
            }
            m = NumberUtils.parseInt(i, k, arrayOfByte);
            while ((arrayOfByte[k] == 10) || (arrayOfByte[k] == 13) || (arrayOfByte[k] == 32))
              k++;
            if ((m != 69560) || (arrayOfByte[i] != 110))
              break;
            i1 = 1;
          }
          while (arrayOfByte[k] != 60);
          paramInt2 = k;
          break;
          i = k;
          while ((arrayOfByte[k] != 10) && (arrayOfByte[k] != 13) && (arrayOfByte[k] != 32) && (arrayOfByte[k] != 47) && (arrayOfByte[k] != 60) && (arrayOfByte[k] != 62))
            k++;
          n = NumberUtils.parseInt(i, k, arrayOfByte);
          while ((arrayOfByte[k] == 10) || (arrayOfByte[k] == 13) || (arrayOfByte[k] == 32) || (arrayOfByte[k] == 47) || (arrayOfByte[k] == 60))
            k++;
          if (arrayOfByte[k] != 82)
            throw new RuntimeException("ref=" + m + " gen=" + m + " 1. Unexpected value " + arrayOfByte[k] + " in file - please send to IDRsolutions for analysis char=" + (char)arrayOfByte[k]);
          arrayOfByte = this.objectReader.readObjectAsByteArray(paramPdfObject, this.objectReader.isCompressed(m, n), m, n);
          if (arrayOfByte == null)
          {
            paramPdfObject.setFullyResolved(false);
            if (LogWriter.isOutput())
              LogWriter.writeLog("[Linearized] " + paramPdfObject.getObjectRefAsString() + " not yet available (11)");
            return paramArrayOfByte.length;
          }
          if ((arrayOfByte != null) && (arrayOfByte.length > 4) && (arrayOfByte[0] == 37) && (arrayOfByte[1] == 80) && (arrayOfByte[2] == 68) && (arrayOfByte[3] == 70))
            arrayOfByte = null;
          if (arrayOfByte == null)
            break label1112;
          i2 = 0;
          if ((arrayOfByte[i2] != 91) && (arrayOfByte[0] != 60) && (arrayOfByte[1] != 60))
          {
            while (((i2 < 3) || ((i2 > 2) && (arrayOfByte[(i2 - 1)] != 106) && (arrayOfByte[(i2 - 2)] != 98) && (arrayOfByte[(i2 - 3)] != 111))) && (arrayOfByte[i2] != 47))
              i2++;
            while ((arrayOfByte[i2] != 91) && ((arrayOfByte[i2] == 10) || (arrayOfByte[i2] == 13) || (arrayOfByte[i2] == 32)))
              i2++;
          }
          if (arrayOfByte[i2] != 91)
          {
            k = 0;
            break label1112;
          }
          k = i2;
        }
      label1112: if (arrayOfByte != null)
      {
        while ((arrayOfByte[k] == 10) || (arrayOfByte[k] == 13) || (arrayOfByte[k] == 32))
          k++;
        i1 = (k < paramArrayOfByte.length) && (paramArrayOfByte[k] == 60) ? 1 : 0;
        if (i1 != 0)
        {
          for (i2 = k; (i2 < arrayOfByte.length) && ((paramArrayOfByte[i2] == 60) || (paramArrayOfByte[i2] == 10) || (paramArrayOfByte[i2] == 13) || (paramArrayOfByte[i2] == 32)); i2++);
          if (paramArrayOfByte[i2] == 47)
            i1 = 0;
        }
        if (i1 != 0)
        {
          i = k;
          while ((arrayOfByte[k] != 10) && (arrayOfByte[k] != 13) && (arrayOfByte[k] != 32) && (arrayOfByte[k] != 47) && (arrayOfByte[k] != 60) && (arrayOfByte[k] != 62))
            k++;
          m = NumberUtils.parseInt(i, k, arrayOfByte);
          while ((arrayOfByte[k] == 10) || (arrayOfByte[k] == 13) || (arrayOfByte[k] == 32) || (arrayOfByte[k] == 47) || (arrayOfByte[k] == 60))
            k++;
          i = k;
          while ((arrayOfByte[k] != 10) && (arrayOfByte[k] != 13) && (arrayOfByte[k] != 32) && (arrayOfByte[k] != 47) && (arrayOfByte[k] != 60) && (arrayOfByte[k] != 62))
            k++;
          n = NumberUtils.parseInt(i, k, arrayOfByte);
          while ((arrayOfByte[(k - 1)] != 106) && (arrayOfByte[(k - 2)] != 98) && (arrayOfByte[(k - 3)] != 111) && (arrayOfByte[k] != 60))
            k++;
        }
        while ((arrayOfByte[k] == 10) || (arrayOfByte[k] == 13) || (arrayOfByte[k] == 32) || (arrayOfByte[k] == 9))
          k++;
        if (arrayOfByte[0] != 60)
          while ((arrayOfByte[k] != 60) && (arrayOfByte[(k + 1)] != 60))
          {
            if ((arrayOfByte[k] == 110) && (arrayOfByte[(k + 1)] == 117) && (arrayOfByte[(k + 2)] == 108) && (arrayOfByte[(k + 3)] == 108))
              return paramInt2;
            if (arrayOfByte[k] == 47)
              break;
            if (arrayOfByte[k] == 40)
            {
              k = readTextStream(paramPdfObject, k, arrayOfByte, paramInt3, true);
              break;
            }
            k++;
          }
        paramInt2 = handleValue(paramPdfObject, paramInt2, paramInt3, k, m, n, arrayOfByte);
      }
    }
    return paramInt2;
  }

  private int processArray(PdfObject paramPdfObject, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    for (int j = paramInt2; (paramArrayOfByte[j] != 93) && (j <= paramArrayOfByte.length); j++);
    PdfObject localPdfObject = ObjectFactory.createObject(paramInt1, null, paramPdfObject.getObjectType(), paramPdfObject.getID());
    localPdfObject.setID(paramInt1);
    paramPdfObject.setDictionary(paramInt1, localPdfObject);
    localPdfObject.ignoreRecursion(paramPdfObject.ignoreRecursion());
    if (localPdfObject.isDataExternal())
    {
      localPdfObject.isDataExternal(true);
      if (!resolveFully(localPdfObject))
        paramPdfObject.setFullyResolved(false);
    }
    int k = 9;
    if (paramInt1 == 9250)
      k = 14;
    ArrayDecoder localArrayDecoder = new ArrayDecoder(this.objectReader, paramInt2, j, k);
    int i = localArrayDecoder.readArray(paramPdfObject.ignoreRecursion(), paramArrayOfByte, localPdfObject, paramInt1);
    return i;
  }

  private int handleValue(PdfObject paramPdfObject, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, byte[] paramArrayOfByte)
  {
    int k = paramArrayOfByte.length;
    PdfObject localPdfObject;
    if (paramArrayOfByte[paramInt3] == 47)
    {
      paramInt3++;
      int i = paramInt3;
      for (int j = 0; (paramInt3 < k) && (paramArrayOfByte[paramInt3] != 10) && (paramArrayOfByte[paramInt3] != 13) && (paramArrayOfByte[paramInt3] != 32) && (paramArrayOfByte[paramInt3] != 47) && (paramArrayOfByte[paramInt3] != 60) && (paramArrayOfByte[paramInt3] != 62); j++)
        paramInt3++;
      paramInt1--;
      if (paramInt2 == -1)
      {
        paramPdfObject.setConstant(paramInt2, i, j, paramArrayOfByte);
      }
      else
      {
        localPdfObject = ObjectFactory.createObject(paramInt2, null, paramPdfObject.getObjectType(), paramPdfObject.getID());
        localPdfObject.setID(paramInt2);
        localPdfObject.setConstant(paramInt2, i, j, paramArrayOfByte);
        paramPdfObject.setDictionary(paramInt2, localPdfObject);
        if (paramPdfObject.isDataExternal())
        {
          localPdfObject.isDataExternal(true);
          if (!resolveFully(localPdfObject))
            paramPdfObject.setFullyResolved(false);
        }
      }
    }
    else
    {
      if (paramInt2 == -1)
      {
        localPdfObject = paramPdfObject;
      }
      else
      {
        localPdfObject = ObjectFactory.createObject(paramInt2, paramInt4, paramInt5, paramPdfObject.getObjectType());
        localPdfObject.setID(paramInt2);
        localPdfObject.setInCompressedStream(paramPdfObject.isInCompressedStream());
        if (paramPdfObject.isDataExternal())
        {
          localPdfObject.isDataExternal(true);
          if (!resolveFully(localPdfObject))
            paramPdfObject.setFullyResolved(false);
        }
        if (paramInt2 != 2004251818)
          localPdfObject.ignoreRecursion(paramPdfObject.ignoreRecursion());
      }
      ObjectDecoder localObjectDecoder = new ObjectDecoder(this.objectReader);
      localObjectDecoder.readDictionaryAsObject(localPdfObject, paramInt3, paramArrayOfByte);
      if (paramInt2 != -1)
        paramPdfObject.setDictionary(paramInt2, localPdfObject);
    }
    return paramInt1;
  }

  int convertDirectDictionaryToObject(PdfObject paramPdfObject, String paramString, int paramInt1, byte[] paramArrayOfByte, int paramInt2)
  {
    PdfObject localPdfObject;
    if (paramInt2 == -1)
    {
      localPdfObject = paramPdfObject;
      int i = 0;
      j = -1;
      int k = -1;
      if (paramArrayOfByte[0] == 60)
      {
        for (int m = 0; (m < paramArrayOfByte.length) && ((paramArrayOfByte[m] != 115) || (paramArrayOfByte[(m + 1)] != 116) || (paramArrayOfByte[(m + 2)] != 114) || (paramArrayOfByte[(m + 3)] != 101) || (paramArrayOfByte[(m + 4)] != 97) || (paramArrayOfByte[(m + 5)] != 109)); m++)
        {
          if ((paramArrayOfByte[m] == 47) && (paramArrayOfByte[(m + 1)] != 76) && (paramArrayOfByte[(m + 2)] != 101) && (paramArrayOfByte[(m + 3)] != 110))
            i++;
          if (i == 1)
            if (j == -1)
            {
              if ((paramArrayOfByte[m] > 48) && (paramArrayOfByte[m] < 57))
                j = m;
            }
            else if (paramArrayOfByte[m] == 82)
              k = m + 1;
        }
        if ((i == 1) && (j != -1) && (k != -1))
        {
          paramString = new String(paramArrayOfByte, j, k - j);
          localPdfObject.setRef(paramString);
        }
      }
    }
    else
    {
      localPdfObject = ObjectFactory.createObject(paramInt2, paramString, paramPdfObject.getObjectType(), paramPdfObject.getID());
      localPdfObject.setInCompressedStream(paramPdfObject.isInCompressedStream());
      localPdfObject.setID(paramInt2);
      if (paramPdfObject.isCached())
        localPdfObject.moveCacheValues(paramPdfObject);
    }
    ObjectDecoder localObjectDecoder = new ObjectDecoder(this.objectReader);
    paramInt1 = localObjectDecoder.readDictionaryAsObject(localPdfObject, paramInt1, paramArrayOfByte);
    if ((paramInt1 < paramArrayOfByte.length) && (paramArrayOfByte[paramInt1] == 62))
      paramInt1--;
    if (paramInt2 != -1)
      paramPdfObject.setDictionary(paramInt2, localPdfObject);
    int j = paramArrayOfByte.length;
    while ((paramInt1 < j - 1) && (paramArrayOfByte[paramInt1] == 62) && (paramArrayOfByte[(paramInt1 + 1)] == 62))
    {
      paramInt1++;
      if ((paramInt1 + 1 < paramArrayOfByte.length) && (paramArrayOfByte[(paramInt1 + 1)] == 62))
        break;
    }
    return paramInt1;
  }

  private int readKeyPairs(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3, PdfObject paramPdfObject)
  {
    String str1 = paramInt2;
    int j = paramInt3;
    int k = 0;
    int m = 0;
    Object localObject1 = (byte[][])null;
    Object localObject2 = (byte[][])null;
    PdfObject[] arrayOfPdfObject = null;
    if (paramInt3 == -1)
    {
      k = 1;
    }
    else if (paramInt3 == -2)
    {
      k = 1;
      m = 1;
    }
    else
    {
      localObject1 = new byte[j][];
      localObject2 = new byte[j][];
      arrayOfPdfObject = new PdfObject[j];
    }
    paramInt3 = 0;
    while (true)
      if ((paramArrayOfByte[str1] == 9) || (paramArrayOfByte[str1] == 10) || (paramArrayOfByte[str1] == 13) || (paramArrayOfByte[str1] == 32) || (paramArrayOfByte[str1] == 60))
      {
        str1++;
      }
      else
      {
        if (paramArrayOfByte[str1] == 37)
        {
          while ((paramArrayOfByte[str1] != 10) && (paramArrayOfByte[str1] != 13))
            str1++;
          while ((paramArrayOfByte[str1] == 9) || (paramArrayOfByte[str1] == 10) || (paramArrayOfByte[str1] == 13) || (paramArrayOfByte[str1] == 32) || (paramArrayOfByte[str1] == 60))
            str1++;
        }
        if (paramArrayOfByte[str1] == 62)
          break;
        if (paramArrayOfByte[str1] == 47)
        {
          paramInt3++;
          str1++;
        }
        else
        {
          throw new RuntimeException("Unexpected value " + paramArrayOfByte[str1] + " - not key pair");
        }
        int n = str1;
        while ((paramArrayOfByte[str1] != 32) && (paramArrayOfByte[str1] != 10) && (paramArrayOfByte[str1] != 13) && (paramArrayOfByte[str1] != 91) && (paramArrayOfByte[str1] != 60) && (paramArrayOfByte[str1] != 47))
          str1++;
        int i1 = str1 - n;
        byte[] arrayOfByte1 = new byte[i1];
        System.arraycopy(paramArrayOfByte, n, arrayOfByte1, 0, i1);
        if (k == 0)
          localObject1[(paramInt3 - 1)] = arrayOfByte1;
        while ((paramArrayOfByte[str1] == 10) || (paramArrayOfByte[str1] == 13) || (paramArrayOfByte[str1] == 32))
          str1++;
        int i2 = (paramArrayOfByte[str1] == 60) || (paramArrayOfByte[str1] == 91) || (paramArrayOfByte[str1] == 47) ? 1 : 0;
        int i3;
        int i4;
        int i5;
        byte[] arrayOfByte2;
        String str2;
        Object localObject4;
        if (i2 != 0)
        {
          while ((paramArrayOfByte[(str1 - 1)] != 60) && (paramArrayOfByte[str1] != 60) && (paramArrayOfByte[str1] != 91) && (paramArrayOfByte[str1] != 47))
            str1++;
          i3 = str1;
          i4 = 1;
          int i;
          if (paramArrayOfByte[str1] == 60)
          {
            str1 += 2;
            i = 1;
            while (i > 0)
              if ((paramArrayOfByte[str1] == 60) && (paramArrayOfByte[(str1 + 1)] == 60))
              {
                str1 += 2;
                i++;
              }
              else if ((paramArrayOfByte[str1] == 62) && (paramArrayOfByte[(str1 + 1)] == 62))
              {
                str1 += 2;
                i--;
              }
              else
              {
                str1++;
              }
          }
          if (paramArrayOfByte[str1] == 91)
          {
            i = 1;
            str1++;
            i5 = 0;
            while (i > 0)
            {
              if ((i5 == 0) && (paramArrayOfByte[str1] == 40))
                i5 = 1;
              else if ((i5 != 0) && (paramArrayOfByte[str1] == 41) && ((paramArrayOfByte[(str1 - 1)] != 92) || (paramArrayOfByte[(str1 - 2)] == 92)))
                i5 = 0;
              if (i5 == 0)
                if (paramArrayOfByte[str1] == 91)
                  i++;
                else if (paramArrayOfByte[str1] == 93)
                  i--;
              str1++;
            }
            i4 = 0;
          }
          else if (paramArrayOfByte[str1] == 47)
          {
            str1++;
            while ((paramArrayOfByte[str1] != 47) && (paramArrayOfByte[str1] != 10) && (paramArrayOfByte[str1] != 13) && (paramArrayOfByte[str1] != 32))
            {
              str1++;
              if ((str1 < paramArrayOfByte.length - 1) && (paramArrayOfByte[str1] == 62) && (paramArrayOfByte[(str1 + 1)] == 62))
                break;
            }
          }
          if (k == 0)
          {
            i5 = str1 - i3;
            arrayOfByte2 = new byte[i5];
            System.arraycopy(paramArrayOfByte, i3, arrayOfByte2, 0, i5);
            localObject2[(paramInt3 - 1)] = arrayOfByte2;
            str2 = paramPdfObject.getObjectRefAsString();
            Object localObject3;
            if (paramPdfObject.getObjectType() == 2087749783)
            {
              localObject3 = new ColorObjectDecoder(this.objectReader);
              if ((i4 != 0) && (i2 == 0))
              {
                ((ColorObjectDecoder)localObject3).handleColorSpaces(paramPdfObject, 0, arrayOfByte2);
                arrayOfPdfObject[(paramInt3 - 1)] = paramPdfObject;
              }
              else
              {
                localObject4 = new ColorSpaceObject(str2);
                if (i2 != 0)
                  ((ColorSpaceObject)localObject4).setRef(-1, 0);
                ((ColorObjectDecoder)localObject3).handleColorSpaces((PdfObject)localObject4, 0, arrayOfByte2);
                arrayOfPdfObject[(paramInt3 - 1)] = localObject4;
              }
            }
            else if (i4 != 0)
            {
              localObject3 = ObjectFactory.createObject(paramInt1, str2, paramPdfObject.getObjectType(), paramPdfObject.getID());
              ((PdfObject)localObject3).setID(paramInt1);
              readDictionaryFromRefOrDirect(paramInt1, (PdfObject)localObject3, str2, 0, arrayOfByte2, -1);
              arrayOfPdfObject[(paramInt3 - 1)] = localObject3;
            }
          }
        }
        else
        {
          i3 = 0;
          i4 = 0;
          i5 = str1;
          str2 = str1;
          int i6 = 0;
          if ((paramArrayOfByte[str1] == 110) && (paramArrayOfByte[(str1 + 1)] == 117) && (paramArrayOfByte[(str1 + 2)] == 108) && (paramArrayOfByte[(str1 + 3)] == 108))
          {
            str1 += 4;
            i6 = 1;
          }
          else
          {
            while ((paramArrayOfByte[str1] != 10) && (paramArrayOfByte[str1] != 13) && (paramArrayOfByte[str1] != 32) && (paramArrayOfByte[str1] != 47) && (paramArrayOfByte[str1] != 60) && (paramArrayOfByte[str1] != 62))
              str1++;
            i3 = NumberUtils.parseInt(str2, str1, paramArrayOfByte);
            while ((paramArrayOfByte[str1] == 10) || (paramArrayOfByte[str1] == 13) || (paramArrayOfByte[str1] == 32) || (paramArrayOfByte[str1] == 47) || (paramArrayOfByte[str1] == 60))
              str1++;
            str2 = str1;
            while ((paramArrayOfByte[str1] != 10) && (paramArrayOfByte[str1] != 13) && (paramArrayOfByte[str1] != 32) && (paramArrayOfByte[str1] != 47) && (paramArrayOfByte[str1] != 60) && (paramArrayOfByte[str1] != 62))
              str1++;
            i4 = NumberUtils.parseInt(str2, str1, paramArrayOfByte);
            while ((paramArrayOfByte[str1] == 10) || (paramArrayOfByte[str1] == 13) || (paramArrayOfByte[str1] == 32) || (paramArrayOfByte[str1] == 47) || (paramArrayOfByte[str1] == 60))
              str1++;
          }
          if ((i6 == 0) && (paramArrayOfByte[str1] != 82))
            throw new RuntimeException((char)paramArrayOfByte[(str1 - 1)] + " " + (char)paramArrayOfByte[str1] + " " + (char)paramArrayOfByte[(str1 + 1)] + " 3. Unexpected value in file - please send to IDRsolutions for analysis");
          str1++;
          if (k == 0)
            if (i6 != 0)
            {
              arrayOfPdfObject[(paramInt3 - 1)] = null;
            }
            else if (PdfDictionary.getKeyType(paramInt1, paramPdfObject.getObjectType()) == 5)
            {
              localObject4 = new String(paramArrayOfByte, i5, str1 - i5);
              PdfObject localPdfObject1 = ObjectFactory.createObject(paramInt1, (String)localObject4, paramPdfObject.getObjectType(), paramPdfObject.getID());
              localPdfObject1.setStatus(1);
              localPdfObject1.setUnresolvedData(StringUtils.toBytes((String)localObject4), paramInt1);
              arrayOfPdfObject[(paramInt3 - 1)] = localPdfObject1;
            }
            else
            {
              localObject4 = this.objectReader.readObjectAsByteArray(paramPdfObject, this.objectReader.isCompressed(i3, i4), i3, i4);
              if (localObject4 == null)
              {
                paramPdfObject.setFullyResolved(false);
                if (LogWriter.isOutput())
                  LogWriter.writeLog("[Linearized] " + paramPdfObject.getObjectRefAsString() + " not yet available (12)");
                return paramArrayOfByte.length;
              }
              int i7 = 0;
              while (((i7 < 3) || ((localObject4[(i7 - 1)] != 106) && (localObject4[(i7 - 2)] != 98) && (localObject4[(i7 - 3)] != 111))) && (localObject4[i7] != 47) && (localObject4[i7] != 91) && (localObject4[i7] != 60))
              {
                i7++;
                if (i7 == localObject4.length)
                  i7 = 0;
              }
              while ((localObject4[i7] == 10) || (localObject4[i7] == 13) || (localObject4[i7] == 32))
                i7++;
              int i8 = localObject4.length - i7;
              arrayOfByte2 = new byte[i8];
              System.arraycopy(localObject4, i7, arrayOfByte2, 0, i8);
              localObject2[(paramInt3 - 1)] = arrayOfByte2;
              String str3 = i3 + " " + i4 + " R";
              if ((paramPdfObject.getObjectType() == 373243460) && (paramInt1 == 373243460))
              {
                arrayOfPdfObject[(paramInt3 - 1)] = null;
                localObject2[(paramInt3 - 1)] = StringUtils.toBytes(str3);
              }
              else
              {
                PdfObject localPdfObject2;
                if (paramPdfObject.getObjectType() == 979194486)
                {
                  localPdfObject2 = ObjectFactory.createObject(paramInt1, str3, 979194486, 979194486);
                  localPdfObject2.setStatus(1);
                  localPdfObject2.setUnresolvedData(StringUtils.toBytes(str3), paramInt1);
                  arrayOfPdfObject[(paramInt3 - 1)] = localPdfObject2;
                }
                else
                {
                  localPdfObject2 = ObjectFactory.createObject(paramInt1, str3, paramPdfObject.getObjectType(), paramPdfObject.getID());
                  localPdfObject2.setID(paramInt1);
                  if (localPdfObject2.getObjectType() == 2087749783)
                  {
                    ColorObjectDecoder localColorObjectDecoder = new ColorObjectDecoder(this.objectReader);
                    localColorObjectDecoder.handleColorSpaces(localPdfObject2, 0, arrayOfByte2);
                  }
                  else
                  {
                    readDictionaryFromRefOrDirect(paramInt1, localPdfObject2, str3, 0, arrayOfByte2, -1);
                  }
                  arrayOfPdfObject[(paramInt3 - 1)] = localPdfObject2;
                }
              }
            }
        }
      }
    if (k == 0)
      paramPdfObject.setDictionaryPairs((byte[][])localObject1, (byte[][])localObject2, arrayOfPdfObject);
    if ((m != 0) || (k == 0))
      return str1;
    return paramInt3;
  }

  void readStreamIntoObject(PdfObject paramPdfObject, int paramInt, byte[] paramArrayOfByte)
  {
    int i = paramArrayOfByte.length;
    byte[] arrayOfByte = null;
    PdfArrayIterator localPdfArrayIterator = paramPdfObject.getMixedArray(1011108731);
    int j = 0;
    int k = -1;
    if ((localPdfArrayIterator != null) && (localPdfArrayIterator.hasMoreTokens()))
    {
      k = localPdfArrayIterator.getNextValueAsConstant(true);
      if (k == 1247500931)
        j = 1;
      while ((localPdfArrayIterator.hasMoreTokens()) && (j == 0))
      {
        k = localPdfArrayIterator.getNextValueAsConstant(true);
        if (k == 1247500931)
          j = 1;
      }
    }
    for (int m = paramInt; m < i; m++)
      if ((paramArrayOfByte[m] == 115) && (paramArrayOfByte[(m + 1)] == 116) && (paramArrayOfByte[(m + 2)] == 114) && (paramArrayOfByte[(m + 3)] == 101) && (paramArrayOfByte[(m + 4)] == 97) && (paramArrayOfByte[(m + 5)] == 109))
      {
        m += 6;
        while (paramArrayOfByte[m] == 32)
          m++;
        if ((paramArrayOfByte[m] == 13) && (paramArrayOfByte[(m + 1)] == 10))
          m += 2;
        else if ((paramArrayOfByte[m] == 10) && (paramArrayOfByte[(m + 1)] == 10) && (paramArrayOfByte[(m + 2)] == 10) && (paramArrayOfByte[(m + 3)] == -1) && (k == 1180911742))
          m += 3;
        else if ((paramArrayOfByte[m] == 10) && (paramArrayOfByte[(m + 1)] == 10) && (paramArrayOfByte[(m + 2)] == -1) && (k == 1180911742))
          m += 2;
        else if ((paramArrayOfByte[m] == 10) || (paramArrayOfByte[m] == 13))
          m++;
        int n = m;
        m--;
        int i1 = 0;
        int i2 = paramPdfObject.getInt(1043816557);
        int i3 = 0;
        int i4;
        if (i2 != -1)
        {
          i1 = i2;
          m = n + i1;
          if ((m < i) && (paramArrayOfByte[m] == 13) && (m + 1 < i) && (paramArrayOfByte[(m + 1)] == 10))
            m += 2;
          if ((i <= m + 9) || (paramArrayOfByte[m] != 101) || (paramArrayOfByte[(m + 1)] != 110) || (paramArrayOfByte[(m + 2)] != 100) || (paramArrayOfByte[(m + 3)] != 115) || (paramArrayOfByte[(m + 4)] != 116) || (paramArrayOfByte[(m + 5)] != 114) || (paramArrayOfByte[(m + 6)] != 101) || (paramArrayOfByte[(m + 7)] != 97) || (paramArrayOfByte[(m + 8)] != 109))
          {
            i4 = m;
            if (m < i)
              while (true)
              {
                m++;
                if ((i3 != 0) || (m == i))
                  break;
                if ((paramArrayOfByte[m] == 101) && (paramArrayOfByte[(m + 1)] == 110) && (paramArrayOfByte[(m + 2)] == 100) && (paramArrayOfByte[(m + 3)] == 115) && (paramArrayOfByte[(m + 4)] == 116) && (paramArrayOfByte[(m + 5)] == 114) && (paramArrayOfByte[(m + 6)] == 101) && (paramArrayOfByte[(m + 7)] == 97) && (paramArrayOfByte[(m + 8)] == 109))
                {
                  i1 = m - n;
                  i3 = 1;
                }
              }
            if (i3 == 0)
            {
              m = i4;
              if (m > i)
                m = i;
              while (true)
              {
                m--;
                if ((i3 != 0) || (m < 0))
                  break;
                if ((paramArrayOfByte[m] == 101) && (paramArrayOfByte[(m + 1)] == 110) && (paramArrayOfByte[(m + 2)] == 100) && (paramArrayOfByte[(m + 3)] == 115) && (paramArrayOfByte[(m + 4)] == 116) && (paramArrayOfByte[(m + 5)] == 114) && (paramArrayOfByte[(m + 6)] == 101) && (paramArrayOfByte[(m + 7)] == 97) && (paramArrayOfByte[(m + 8)] == 109))
                {
                  i1 = m - n;
                  i3 = 1;
                }
              }
            }
          }
          if ((this.decryption != null) && (this.decryption.getBooleanValue(101)))
            i1 = i2;
        }
        else
        {
          while (true)
          {
            m++;
            if (m != i)
              if ((paramArrayOfByte[m] == 101) && (paramArrayOfByte[(m + 1)] == 110) && (paramArrayOfByte[(m + 2)] == 100) && (paramArrayOfByte[(m + 3)] == 115) && (paramArrayOfByte[(m + 4)] == 116) && (paramArrayOfByte[(m + 5)] == 114) && (paramArrayOfByte[(m + 6)] == 101) && (paramArrayOfByte[(m + 7)] == 97) && (paramArrayOfByte[(m + 8)] == 109))
                break;
          }
          i4 = m - 1;
          if (i4 > n)
            i1 = i4 - n + 1;
        }
        if ((i1 > 1) && ((this.decryption == null) || (!this.decryption.getBooleanValue(101))))
        {
          i4 = n + i1 - 1;
          if ((i4 < paramArrayOfByte.length) && (i4 > 0) && ((paramArrayOfByte[i4] == 10) || ((paramArrayOfByte[i4] == 13) && (((paramPdfObject != null) && (j != 0)) || ((i4 > 0) && (paramArrayOfByte[(i4 - 1)] == 10))))))
            i1--;
        }
        if (n + i1 > i)
          i1 = i - n;
        if (i1 < 0)
          return;
        if (i1 < 0)
          throw new RuntimeException("Negative stream length " + i1 + " start=" + n + " count=" + i);
        arrayOfByte = new byte[i1];
        System.arraycopy(paramArrayOfByte, n, arrayOfByte, 0, i1);
        m = i;
      }
    if (paramPdfObject != null)
    {
      paramPdfObject.setStream(arrayOfByte);
      if (paramPdfObject.decompressStreamWhenRead())
        this.objectReader.readStream(paramPdfObject, true, true, false, paramPdfObject.getObjectType() == 1365674082, paramPdfObject.isCompressedStream(), null);
    }
  }

  private int setNameStringValue(PdfObject paramPdfObject, int paramInt, byte[] paramArrayOfByte, boolean paramBoolean)
  {
    while ((paramArrayOfByte[paramInt] != 10) && (paramArrayOfByte[paramInt] != 13) && (paramArrayOfByte[paramInt] != 32) && (paramArrayOfByte[paramInt] != 47) && (paramArrayOfByte[paramInt] != 40))
      paramInt++;
    while ((paramArrayOfByte[paramInt] == 10) || (paramArrayOfByte[paramInt] == 13) || (paramArrayOfByte[paramInt] == 32))
      paramInt++;
    int j = paramInt;
    byte[] arrayOfByte2 = paramArrayOfByte;
    int k = (paramArrayOfByte[paramInt] != 47) && (paramArrayOfByte[paramInt] != 40) && (paramArrayOfByte[paramInt] != 60) ? 1 : 0;
    int m = paramArrayOfByte[paramInt] == 40 ? 1 : 0;
    int n = 0;
    if (k != 0)
    {
      for (i1 = paramInt + 1; (i1 < paramArrayOfByte.length) && ((paramArrayOfByte[i1] == 10) || (paramArrayOfByte[i1] == 13) || (paramArrayOfByte[i1] == 32)); i1++);
      if ((paramArrayOfByte[i1] == 47) || (paramArrayOfByte[i1] == 93))
      {
        k = 0;
        paramInt = i1 + 1;
        n = 1;
      }
    }
    int i2;
    if (k != 0)
    {
      int i = paramInt;
      while ((paramArrayOfByte[paramInt] != 10) && (paramArrayOfByte[paramInt] != 13) && (paramArrayOfByte[paramInt] != 32) && (paramArrayOfByte[paramInt] != 47) && (paramArrayOfByte[paramInt] != 60) && (paramArrayOfByte[paramInt] != 62))
        paramInt++;
      i1 = NumberUtils.parseInt(i, paramInt, paramArrayOfByte);
      while ((paramArrayOfByte[paramInt] == 10) || (paramArrayOfByte[paramInt] == 13) || (paramArrayOfByte[paramInt] == 32) || (paramArrayOfByte[paramInt] == 47) || (paramArrayOfByte[paramInt] == 60))
        paramInt++;
      i = paramInt;
      while ((paramArrayOfByte[paramInt] != 10) && (paramArrayOfByte[paramInt] != 13) && (paramArrayOfByte[paramInt] != 32) && (paramArrayOfByte[paramInt] != 47) && (paramArrayOfByte[paramInt] != 60) && (paramArrayOfByte[paramInt] != 62))
        paramInt++;
      i2 = NumberUtils.parseInt(i, paramInt, paramArrayOfByte);
      while ((paramArrayOfByte[paramInt] == 10) || (paramArrayOfByte[paramInt] == 13) || (paramArrayOfByte[paramInt] == 32) || (paramArrayOfByte[paramInt] == 47) || (paramArrayOfByte[paramInt] == 60))
        paramInt++;
      if (paramArrayOfByte[paramInt] != 82)
        throw new RuntimeException(padding + "2. Unexpected value in file - please send to IDRsolutions for analysis");
      arrayOfByte2 = this.objectReader.readObjectAsByteArray(paramPdfObject, this.objectReader.isCompressed(i1, i2), i1, i2);
      if (arrayOfByte2 == null)
      {
        paramPdfObject.setFullyResolved(false);
        if (LogWriter.isOutput())
          LogWriter.writeLog("[Linearized] " + paramPdfObject.getObjectRefAsString() + " not yet available (13)");
        return paramArrayOfByte.length;
      }
      if (arrayOfByte2[0] == 47)
        j = 0;
      else
        for (j = 3; arrayOfByte2[j] != 47; j++);
    }
    j++;
    if (arrayOfByte2[j] == 47)
      return j - 1;
    int i1 = j + 1;
    byte[] arrayOfByte1;
    if (n != 0)
    {
      while ((arrayOfByte2[j] == 10) || (arrayOfByte2[j] == 13) || (arrayOfByte2[j] == 32) || (arrayOfByte2[j] == 47))
        j++;
      i2 = 0;
      int i3 = 0;
      while (arrayOfByte2[i1] != 93)
      {
        if ((arrayOfByte2[i1] == 47) && ((i3 == 32) || (i3 == 10) || (i3 == 13)))
          i2++;
        i3 = arrayOfByte2[i1];
        i1++;
        if (i1 == arrayOfByte2.length)
          break;
      }
      int i4 = i1 - i2;
      int i5 = 0;
      arrayOfByte1 = new byte[i4 - j];
      int i7 = 0;
      for (int i8 = j; i8 < i4; i8++)
      {
        int i6 = arrayOfByte2[i8];
        if (i6 == 47)
        {
          if ((i7 != 32) && (i7 != 10) && (i7 != 13))
          {
            arrayOfByte1[i5] = 32;
            i5++;
          }
        }
        else
        {
          arrayOfByte1[i5] = i6;
          i5++;
        }
        i7 = i6;
      }
    }
    else
    {
      while (m != 0 ? arrayOfByte2[i1] != 41 : (arrayOfByte2[i1] != 32) && (arrayOfByte2[i1] != 10) && (arrayOfByte2[i1] != 13) && (arrayOfByte2[i1] != 47) && (arrayOfByte2[i1] != 62))
      {
        i1++;
        if (i1 == arrayOfByte2.length)
          break;
      }
      i2 = i1 - j;
      arrayOfByte1 = new byte[i2];
      System.arraycopy(arrayOfByte2, j, arrayOfByte1, 0, i2);
    }
    if (paramBoolean)
      paramPdfObject.setName(this.PDFkey, StringUtils.getTextString(arrayOfByte1, false));
    else
      paramPdfObject.setName(this.PDFkeyInt, arrayOfByte1);
    if (k == 0)
      paramInt = i1 - 1;
    return paramInt;
  }

  public synchronized boolean resolveFully(PdfObject paramPdfObject)
  {
    boolean bool = paramPdfObject != null;
    if (bool)
    {
      byte[] arrayOfByte;
      if (paramPdfObject.getStatus() == 0)
        arrayOfByte = StringUtils.toBytes(paramPdfObject.getObjectRefAsString());
      else
        arrayOfByte = paramPdfObject.getUnresolvedData();
      paramPdfObject.setStatus(0);
      if ((arrayOfByte[0] != 101) && (arrayOfByte[1] != 110) && (arrayOfByte[2] != 100) && (arrayOfByte[3] != 111) && (arrayOfByte[4] != 98))
      {
        for (int i = 0; (arrayOfByte[i] == 91) || (arrayOfByte[i] == 32) || (arrayOfByte[i] == 13) || (arrayOfByte[i] == 10); i++);
        int j = i;
        while ((arrayOfByte[i] != 10) && (arrayOfByte[i] != 13) && (arrayOfByte[i] != 32) && (arrayOfByte[i] != 47) && (arrayOfByte[i] != 60) && (arrayOfByte[i] != 62))
          i++;
        int k = NumberUtils.parseInt(j, i, arrayOfByte);
        while ((arrayOfByte[i] == 10) || (arrayOfByte[i] == 13) || (arrayOfByte[i] == 32))
          i++;
        j = i;
        while ((arrayOfByte[i] != 10) && (arrayOfByte[i] != 13) && (arrayOfByte[i] != 32) && (arrayOfByte[i] != 47) && (arrayOfByte[i] != 60) && (arrayOfByte[i] != 62))
          i++;
        int m = NumberUtils.parseInt(j, i, arrayOfByte);
        if (arrayOfByte[(arrayOfByte.length - 1)] == 82)
          bool = resolveFullyChildren(paramPdfObject, bool, arrayOfByte, k, m);
        if (bool)
        {
          paramPdfObject.ignoreRecursion(false);
          ObjectDecoder localObjectDecoder = new ObjectDecoder(this.objectReader);
          localObjectDecoder.readDictionaryAsObject(paramPdfObject, i, arrayOfByte);
        }
      }
    }
    return bool;
  }

  private boolean resolveFullyChildren(PdfObject paramPdfObject, boolean paramBoolean, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    paramPdfObject.setRef(new String(paramArrayOfByte));
    paramPdfObject.isDataExternal(true);
    byte[] arrayOfByte = this.objectReader.readObjectAsByteArray(paramPdfObject, this.objectReader.isCompressed(paramInt1, paramInt2), paramInt1, paramInt2);
    if (arrayOfByte == null)
    {
      paramPdfObject.setFullyResolved(false);
      paramBoolean = false;
    }
    else
    {
      paramPdfObject.setStatus(2);
      paramPdfObject.setUnresolvedData(arrayOfByte, 2004845231);
      paramPdfObject.isDataExternal(true);
      if (!resolveFully(paramPdfObject))
        paramPdfObject.setFullyResolved(false);
    }
    return paramBoolean;
  }

  public void checkResolved(PdfObject paramPdfObject)
  {
    if ((paramPdfObject != null) && (paramPdfObject.getStatus() != 0))
    {
      byte[] arrayOfByte = paramPdfObject.getUnresolvedData();
      paramPdfObject.setStatus(0);
      if (((arrayOfByte[0] != 101) || (arrayOfByte[1] != 110) || (arrayOfByte[2] != 100) || (arrayOfByte[3] != 111) || (arrayOfByte[4] != 98)) && ((arrayOfByte[0] != 110) || (arrayOfByte[1] != 117) || (arrayOfByte[2] != 108) || (arrayOfByte[3] != 108)))
      {
        String str = paramPdfObject.getObjectRefAsString();
        if (arrayOfByte[0] == 91)
        {
          int i = 0;
          int j = arrayOfByte.length;
          for (int k = 0; k < j; k++)
            if ((arrayOfByte[k] >= 48) && (arrayOfByte[k] <= 57))
            {
              i = k;
              k = j;
            }
          for (k = i; ((arrayOfByte[k] >= 48) && (arrayOfByte[k] <= 57)) || (arrayOfByte[k] == 32) || (arrayOfByte[k] == 10) || (arrayOfByte[k] == 13) || (arrayOfByte[k] == 9); k++);
          if (arrayOfByte[k] == 82)
            paramPdfObject.setRef(new String(arrayOfByte, i, j - i));
        }
        else if (arrayOfByte[(arrayOfByte.length - 1)] == 82)
        {
          str = new String(arrayOfByte);
          paramPdfObject.setRef(str);
        }
        ObjectDecoder localObjectDecoder = new ObjectDecoder(this.objectReader);
        localObjectDecoder.readDictionaryFromRefOrDirect(-1, paramPdfObject, str, 0, arrayOfByte, -1);
      }
    }
  }

  public void setEndPt(int paramInt)
  {
    this.endPt = paramInt;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.io.ObjectDecoder
 * JD-Core Version:    0.6.2
 */