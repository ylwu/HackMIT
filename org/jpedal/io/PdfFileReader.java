package org.jpedal.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import org.jpedal.exception.PdfException;
import org.jpedal.exception.PdfSecurityException;
import org.jpedal.objects.raw.CompressedObject;
import org.jpedal.objects.raw.EncryptionObject;
import org.jpedal.objects.raw.PageObject;
import org.jpedal.objects.raw.PdfArrayIterator;
import org.jpedal.objects.raw.PdfObject;
import org.jpedal.objects.raw.StreamObject;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.NumberUtils;
import org.jpedal.utils.Sorts;
import org.jpedal.utils.repositories.Vector_Int;
import org.jpedal.utils.repositories.Vector_boolean;

public class PdfFileReader
{
  private static final int UNSET = -1;
  private static final int COMPRESSED = 1;
  private static final int LEGACY = 2;
  private int newCacheSize = -1;
  private LinearizedHintTable linHintTable;
  private boolean refTableInvalid = false;
  private static final byte[] endPattern = { 101, 110, 100, 111, 98, 106 };
  private byte[] lastCompressedStream = null;
  private Map lastOffsetStart;
  private Map lastOffsetEnd;
  private PdfObject compressedObj;
  private int lastFirst = -1;
  private int lastCompressedID = -1;
  private PdfObject linearObj;
  private boolean fileIsBroken = false;
  private Certificate certificate;
  private PrivateKey key;
  private PdfObject infoObject = null;
  private byte[] ID = null;
  PdfObject encyptionObj = null;
  private static final String pattern = "obj";
  private Vector_Int xref = new Vector_Int(100);
  private DecryptionFactory decryption = null;
  private byte[] encryptionPassword = null;
  private static final byte[] oldPattern = { 120, 114, 101, 102 };
  private static final byte[] EOFpattern = { 37, 37, 69, 79, 70 };
  private static final byte[] trailerpattern = { 116, 114, 97, 105, 108, 101, 114 };
  private RandomAccessBuffer pdf_datafile = null;
  private static final byte[] endObj = { 32, 111, 98, 106 };
  private static final byte[] lengthString = { 47, 76, 101, 110, 103, 116, 104 };
  private static final byte[] startStream = { 115, 116, 114, 101, 97, 109 };
  private Vector_Int offset = new Vector_Int(2000);
  private Vector_boolean isCompressed = new Vector_boolean(2000);
  private Vector_Int generation = new Vector_Int(2000);
  public static int alwaysCacheInMemory = 16384;
  private long eof;
  private int[] ObjLengthTable;

  public PdfObject getInfoObject()
  {
    return this.infoObject;
  }

  public void setCacheSize(int paramInt)
  {
    this.newCacheSize = paramInt;
  }

  public void init(RandomAccessBuffer paramRandomAccessBuffer)
  {
    this.pdf_datafile = paramRandomAccessBuffer;
    try
    {
      this.eof = paramRandomAccessBuffer.length();
    }
    catch (IOException localIOException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog(new StringBuilder().append("Exception: ").append(localIOException.getMessage()).toString());
    }
  }

  public final void readObject(PdfObject paramPdfObject)
  {
    if ((paramPdfObject.isDataExternal()) && (this.linHintTable != null))
    {
      readExternalObject(paramPdfObject);
    }
    else
    {
      String str = paramPdfObject.getObjectRefAsString();
      boolean bool = isCompressed(str);
      paramPdfObject.setCompressedStream(bool);
      byte[] arrayOfByte;
      if (bool)
      {
        arrayOfByte = readCompressedObject(paramPdfObject, str);
      }
      else
      {
        movePointer(str);
        if (str.charAt(0) == '<')
        {
          arrayOfByte = readObjectData(-1, paramPdfObject);
        }
        else
        {
          int i = str.indexOf(32);
          int j = Integer.parseInt(str.substring(0, i));
          if ((this.ObjLengthTable == null) || (this.refTableInvalid))
          {
            if (getPointer() == 0L)
              arrayOfByte = new byte[0];
            else
              arrayOfByte = readObjectData(-1, paramPdfObject);
          }
          else if ((j > this.ObjLengthTable.length) || (this.ObjLengthTable[j] == 0))
          {
            if (LogWriter.isOutput())
              LogWriter.writeLog(new StringBuilder().append(str).append(" cannot have offset 0").toString());
            arrayOfByte = new byte[0];
          }
          else
          {
            arrayOfByte = readObjectData(this.ObjLengthTable[j], paramPdfObject);
          }
        }
      }
      if (arrayOfByte.length > 1)
      {
        ObjectDecoder localObjectDecoder = new ObjectDecoder(this);
        localObjectDecoder.readDictionaryAsObject(paramPdfObject, 0, arrayOfByte);
      }
    }
  }

  private void readExternalObject(PdfObject paramPdfObject)
  {
    int i = paramPdfObject.getObjectRefID();
    int j = paramPdfObject.getObjectRefGeneration();
    byte[] arrayOfByte = readObjectAsByteArray(paramPdfObject, isCompressed(i, j), i, j);
    paramPdfObject.setStatus(2);
    paramPdfObject.setUnresolvedData(arrayOfByte, 540096309);
    ObjectDecoder localObjectDecoder = new ObjectDecoder(this);
    localObjectDecoder.resolveFully(paramPdfObject);
  }

  private byte[] readCompressedObject(PdfObject paramPdfObject, String paramString)
  {
    int i = Integer.parseInt(paramString.substring(0, paramString.indexOf(32)));
    int j = getCompressedStreamObject(paramString);
    String str1 = new StringBuilder().append(j).append(" 0 R").toString();
    String str2 = null;
    int k = this.lastFirst;
    int m = 1;
    byte[] arrayOfByte2 = this.lastCompressedStream;
    Object localObject1 = this.lastOffsetStart;
    Object localObject2 = this.lastOffsetEnd;
    PdfObject localPdfObject = null;
    if ((this.lastOffsetStart != null) && (j == this.lastCompressedID))
      str2 = (String)this.lastOffsetStart.get(String.valueOf(i));
    while (str2 == null)
    {
      if (localPdfObject != null)
      {
        this.compressedObj = localPdfObject;
      }
      else if (j != this.lastCompressedID)
      {
        m = 0;
        movePointer(str1);
        arrayOfByte1 = readObjectData(this.ObjLengthTable[j], null);
        this.compressedObj = new CompressedObject(str1);
        ObjectDecoder localObjectDecoder = new ObjectDecoder(this);
        localObjectDecoder.readDictionaryAsObject(this.compressedObj, 0, arrayOfByte1);
      }
      localObject1 = new HashMap();
      localObject2 = new HashMap();
      k = this.compressedObj.getInt(960643930);
      arrayOfByte2 = this.compressedObj.getDecodedStream();
      extractCompressedObjectOffset((Map)localObject1, (Map)localObject2, k, arrayOfByte2, j);
      str2 = (String)((Map)localObject1).get(String.valueOf(i));
      localPdfObject = this.compressedObj.getDictionary(894663815);
      if (localPdfObject == null)
        break;
    }
    if (m == 0)
    {
      this.lastCompressedStream = arrayOfByte2;
      this.lastCompressedID = j;
      this.lastOffsetStart = ((Map)localObject1);
      this.lastOffsetEnd = ((Map)localObject2);
      this.lastFirst = k;
    }
    int n = k + Integer.parseInt(str2);
    int i1 = arrayOfByte2.length;
    String str3 = (String)((Map)localObject2).get(String.valueOf(i));
    if (str3 != null)
      i1 = k + Integer.parseInt(str3);
    int i2 = i1 - n;
    byte[] arrayOfByte1 = new byte[i2];
    System.arraycopy(arrayOfByte2, n, arrayOfByte1, 0, i2);
    paramPdfObject.setInCompressedStream(true);
    return arrayOfByte1;
  }

  private PdfObject readCompressedStream(int paramInt)
    throws PdfException
  {
    PdfObject localPdfObject1 = null;
    PdfObject localPdfObject2 = null;
    while (paramInt != -1)
    {
      movePointer(paramInt);
      byte[] arrayOfByte1 = readObjectData(-1, null);
      StringBuilder localStringBuilder = new StringBuilder();
      int i = 32;
      int j = 0;
      for (int k = 0; k < arrayOfByte1.length; k++)
      {
        char c = (char)arrayOfByte1[k];
        if ((c == '\n') || (c == '\r'))
          c = ' ';
        if ((c == ' ') && (i == 32))
        {
          j = 0;
        }
        else if (c == "obj".charAt(j))
        {
          j++;
        }
        else
        {
          j = 0;
          localStringBuilder.append(c);
        }
        if (j == 3)
          break;
        i = c;
      }
      localStringBuilder.append('R');
      CompressedObject localCompressedObject = new CompressedObject(localStringBuilder.toString());
      localCompressedObject.setCompressedStream(true);
      ObjectDecoder localObjectDecoder = new ObjectDecoder(this);
      localObjectDecoder.readDictionaryAsObject(localCompressedObject, 0, arrayOfByte1);
      int[] arrayOfInt1 = localCompressedObject.getIntArray(39);
      byte[] arrayOfByte2 = localCompressedObject.getDecodedStream();
      int m = localCompressedObject.getObjectRefID();
      if (arrayOfByte2 == null)
        arrayOfByte2 = readStream(localCompressedObject, true, true, false, false, true, null);
      int[] arrayOfInt2 = localCompressedObject.getIntArray(1043608929);
      if (arrayOfInt2 == null)
      {
        readCompressedOffsets(0, 0, localCompressedObject.getInt(590957109), arrayOfInt1, arrayOfByte2, m);
      }
      else
      {
        int n = arrayOfInt2.length;
        int i1 = 0;
        int i2 = 0;
        while (i2 < n)
        {
          i1 = readCompressedOffsets(i1, arrayOfInt2[i2], arrayOfInt2[(i2 + 1)], arrayOfInt1, arrayOfByte2, m);
          i2 += 2;
        }
      }
      if (localPdfObject2 == null)
      {
        localPdfObject2 = localCompressedObject.getDictionary(574570308);
        localPdfObject1 = localCompressedObject.getDictionary(1113489015);
        if (localPdfObject1 != null)
        {
          byte[][] arrayOfByte = localCompressedObject.getStringArray(6420);
          if (arrayOfByte != null)
            this.ID = arrayOfByte[0];
        }
        this.infoObject = localCompressedObject.getDictionary(423507519);
      }
      if (this.linearObj != null)
        paramInt = -1;
      else
        paramInt = localCompressedObject.getInt(541209926);
    }
    if (localPdfObject1 != null)
      setupDecryption(localPdfObject1);
    this.ObjLengthTable = calculateObjectLength((int)this.eof);
    return localPdfObject2;
  }

  public final byte[] readStream(PdfObject paramPdfObject, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, boolean paramBoolean5, String paramString)
  {
    boolean bool = paramPdfObject.isCached();
    byte[] arrayOfByte1 = null;
    if (!bool)
      arrayOfByte1 = paramPdfObject.getDecodedStream();
    Object localObject1;
    if (arrayOfByte1 == null)
    {
      localObject1 = paramPdfObject.stream;
      if (bool)
        try
        {
          if ((this.decryption != null) && (!paramBoolean5) && ((this.decryption.getBooleanValue(102)) || (!paramBoolean4)))
            this.decryption.decrypt(null, paramPdfObject.getObjectRefAsString(), false, paramString, false, false);
        }
        catch (Exception localException1)
        {
          localException1.printStackTrace();
          localObject1 = null;
          if (LogWriter.isOutput())
            LogWriter.writeLog(new StringBuilder().append("Exception ").append(localException1).toString());
        }
      if (localObject1 != null)
        try
        {
          if ((this.decryption != null) && (!paramBoolean5) && ((this.decryption.getBooleanValue(102)) || (!paramBoolean4)) && ((paramPdfObject.getObjectType() != 2087749783) || (!paramPdfObject.getObjectRefAsString().startsWith("["))))
            localObject1 = this.decryption.decrypt((byte[])localObject1, paramPdfObject.getObjectRefAsString(), false, null, false, false);
        }
        catch (Exception localException2)
        {
          localException2.printStackTrace();
          localObject1 = null;
          if (LogWriter.isOutput())
            LogWriter.writeLog(new StringBuilder().append("Exception ").append(localException2).append(" with ").append(paramPdfObject.getObjectRefAsString()).toString());
        }
      if (paramBoolean3)
        paramPdfObject.stream = null;
      i = 1;
      int j;
      if ((localObject1 != null) || (bool))
      {
        j = 1;
        int k = 1;
        int m = paramPdfObject.getInt(959926393);
        if (m != -1)
          j = m;
        int n = paramPdfObject.getInt(959726687);
        if (n != -1)
          k = n;
        int i1 = paramPdfObject.getInt(1043816557);
        if (i1 != -1)
          i = i1;
        if (j * k == 1)
          k = i;
        PdfArrayIterator localPdfArrayIterator = paramPdfObject.getMixedArray(1011108731);
        int i2 = -1;
        if ((localPdfArrayIterator != null) && (localPdfArrayIterator.hasMoreTokens()))
          i2 = localPdfArrayIterator.getNextValueAsConstant(false);
        if ((localPdfArrayIterator != null) && (i2 != -1) && (i2 != 1399277700) && (i2 != 1180911742))
        {
          try
          {
            PdfFilteredReader localPdfFilteredReader = new PdfFilteredReader();
            localObject1 = localPdfFilteredReader.decodeFilters(ObjectUtils.setupDecodeParms(paramPdfObject, this), (byte[])localObject1, localPdfArrayIterator, k, j, paramString);
            paramPdfObject.setStreamMayBeCorrupt(localPdfFilteredReader.hasError());
          }
          catch (Exception localException4)
          {
            if (LogWriter.isOutput())
              LogWriter.writeLog(new StringBuilder().append("[PDF] Problem ").append(localException4).append(" decompressing stream ").toString());
            localObject1 = null;
            bool = false;
          }
          i = 1;
        }
        else if ((localObject1 != null) && (i != -1) && (i < localObject1.length))
        {
          if ((localObject1.length != i) && (i > 0))
          {
            byte[] arrayOfByte4 = new byte[i];
            System.arraycopy(localObject1, 0, arrayOfByte4, 0, i);
            localObject1 = arrayOfByte4;
          }
          else if ((localObject1.length == 1) && (i == 0))
          {
            localObject1 = new byte[0];
          }
        }
      }
      if ((localObject1 != null) && (paramBoolean1))
        paramPdfObject.setDecodedStream((byte[])localObject1);
      if ((paramBoolean2) && (bool))
      {
        j = (int)new File(paramString).length();
        Object localObject2 = new byte[j];
        try
        {
          new BufferedInputStream(new FileInputStream(paramString)).read((byte[])localObject2);
        }
        catch (Exception localException3)
        {
          if (LogWriter.isOutput())
            LogWriter.writeLog(new StringBuilder().append("Exception: ").append(localException3.getMessage()).toString());
        }
        if ((i != 1) && (i < j))
        {
          byte[] arrayOfByte3 = new byte[i];
          System.arraycopy(localObject2, 0, arrayOfByte3, 0, i);
          localObject2 = arrayOfByte3;
        }
        return localObject2;
      }
    }
    else
    {
      localObject1 = arrayOfByte1;
    }
    if (localObject1 == null)
      return null;
    int i = localObject1.length;
    byte[] arrayOfByte2 = new byte[i];
    System.arraycopy(localObject1, 0, arrayOfByte2, 0, i);
    return arrayOfByte2;
  }

  private PdfObject readLegacyReferenceTable(int paramInt1, int paramInt2)
    throws PdfException
  {
    int j = 0;
    int k = 1024;
    PdfObject localPdfObject = null;
    Object localObject = null;
    while (true)
    {
      byte[] arrayOfByte;
      try
      {
        arrayOfByte = readTrailer(k, paramInt1, paramInt2);
      }
      catch (Exception localException)
      {
        try
        {
          closeFile();
        }
        catch (IOException localIOException)
        {
          if (LogWriter.isOutput())
            LogWriter.writeLog(new StringBuilder().append("Exception ").append(localException).append(" closing file").toString());
        }
        throw new PdfException(new StringBuilder().append("Exception ").append(localException).append(" reading trailer").toString());
      }
      if (arrayOfByte == null)
        break;
      int m = 0;
      int n = arrayOfByte.length;
      while ((m < n) && ((arrayOfByte[m] != 116) || (arrayOfByte[(m + 1)] != 114) || (arrayOfByte[(m + 2)] != 97) || (arrayOfByte[(m + 3)] != 105) || (arrayOfByte[(m + 4)] != 108) || (arrayOfByte[(m + 5)] != 101) || (arrayOfByte[(m + 6)] != 114)))
        m++;
      int i = m;
      if (m == arrayOfByte.length)
        break;
      while ((arrayOfByte[m] != 60) && (arrayOfByte[(m - 1)] != 60))
        m++;
      m++;
      CompressedObject localCompressedObject = new CompressedObject("1 0 R");
      ObjectDecoder localObjectDecoder = new ObjectDecoder(this);
      localObjectDecoder.readDictionary(localCompressedObject, m, arrayOfByte, -1, true);
      int i1 = 0;
      while (true)
      {
        if ((arrayOfByte[m] == 60) && (arrayOfByte[(m - 1)] == 60))
        {
          i1++;
          m++;
        }
        else if (arrayOfByte[m] == 91)
        {
          m++;
          do
          {
            if (arrayOfByte[m] == 93)
              break;
            m++;
          }
          while (m != arrayOfByte.length);
        }
        else if ((arrayOfByte[m] == 62) && (arrayOfByte[(m - 1)] == 62))
        {
          i1--;
          m++;
        }
        if (i1 == 0)
          break;
        m++;
      }
      int i2 = localCompressedObject.getInt(910911090);
      if (i2 != -1)
      {
        paramInt1 = i2;
      }
      else
      {
        int i3 = 1;
        while ((arrayOfByte[m] == 10) || (arrayOfByte[m] == 13))
          m++;
        while (arrayOfByte[m] == 37)
        {
          while (arrayOfByte[m] != 10)
            m++;
          m++;
        }
        while ((arrayOfByte[m] != 116) && (arrayOfByte[(m + 1)] != 120) && (arrayOfByte[(m + 2)] != 114) && (arrayOfByte[(m + 3)] != 101) && (arrayOfByte[(m + 4)] != 102))
        {
          if ((arrayOfByte[m] == 111) && (arrayOfByte[(m + 1)] == 98) && (arrayOfByte[(m + 2)] == 106))
          {
            i3 = 0;
            break;
          }
          m++;
        }
        if (i3 != 0)
        {
          m += 8;
          while ((m < n) && ((arrayOfByte[m] == 10) || (arrayOfByte[m] == 32) || (arrayOfByte[m] == 13)))
            m++;
          int i4 = m;
          while ((m < n) && (arrayOfByte[m] != 10) && (arrayOfByte[m] != 32) && (arrayOfByte[m] != 13))
            m++;
          if (i4 != m)
            paramInt1 = NumberUtils.parseInt(i4, m, arrayOfByte);
        }
      }
      for (m = 0; (arrayOfByte[m] == 13) || (arrayOfByte[m] == 10) || (arrayOfByte[m] == 9); m++);
      if (paramInt1 == -1)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog("No startRef");
      }
      else if ((arrayOfByte[m] == 120) && (arrayOfByte[(m + 1)] == 114) && (arrayOfByte[(m + 2)] == 101) && (arrayOfByte[(m + 3)] == 102))
      {
        for (m = 5; (arrayOfByte[m] == 10) || (arrayOfByte[m] == 32) || (arrayOfByte[m] == 13); m++);
        j = readXRefs(this.xref, j, arrayOfByte, i, m);
        if (localObject == null)
        {
          localObject = localCompressedObject.getDictionary(574570308);
          localPdfObject = localCompressedObject.getDictionary(1113489015);
          if (localPdfObject != null)
          {
            byte[][] arrayOfByte1 = localCompressedObject.getStringArray(6420);
            if (arrayOfByte1 != null)
              this.ID = arrayOfByte1[0];
          }
          this.infoObject = localCompressedObject.getDictionary(423507519);
        }
        paramInt1 = localCompressedObject.getInt(541209926);
        if ((paramInt1 != -1) && (paramInt1 < this.eof))
        {
          k = 1024;
          this.xref.addElement(paramInt1);
        }
        else
        {
          paramInt1 = -1;
        }
      }
      else
      {
        paramInt1 = -1;
        localObject = new PageObject(findOffsets());
        readObject((PdfObject)localObject);
        this.refTableInvalid = true;
      }
      if (paramInt1 == -1)
        break;
    }
    if (localPdfObject != null)
      setupDecryption(localPdfObject);
    if (!this.refTableInvalid)
      this.ObjLengthTable = calculateObjectLength(paramInt2);
    return localObject;
  }

  private void setupDecryption(PdfObject paramPdfObject)
    throws PdfSecurityException
  {
    try
    {
      if (this.certificate != null)
        this.decryption = new DecryptionFactory(this.ID, this.certificate, this.key);
      else
        this.decryption = new DecryptionFactory(this.ID, this.encryptionPassword);
      if (this.encyptionObj == null)
      {
        this.encyptionObj = new EncryptionObject(new String(paramPdfObject.getUnresolvedData()));
        readObject(this.encyptionObj);
      }
      this.decryption.readEncryptionObject(this.encyptionObj);
    }
    catch (Error localError)
    {
      throw new RuntimeException("This PDF file is encrypted and JPedal needs an additional library to \ndecode on the classpath (we recommend bouncycastle library).\nThere is additional explanation at http://www.idrsolutions.com/additional-jars\n");
    }
  }

  public int getPDFflag(Integer paramInteger)
  {
    if (this.decryption == null)
      return -1;
    return this.decryption.getPDFflag(paramInteger);
  }

  private int readFirstStartRef()
    throws PdfException
  {
    this.refTableInvalid = false;
    int i = -1;
    int j = 1019;
    StringBuilder localStringBuilder = new StringBuilder(10);
    int k = 1024;
    byte[] arrayOfByte1 = new byte[k];
    int[] arrayOfInt = { 37, 37, 69, 79 };
    int m = 3;
    int n = 0;
    try
    {
      long l = this.eof;
      int i1 = 255;
      int i4;
      while (true)
      {
        byte[] arrayOfByte2 = getBytes(l - i1, i1);
        i4 = 0;
        for (int i5 = i1 - 1; i5 > -1; i5--)
        {
          if (n == 0)
            m = 3;
          if (arrayOfByte2[i5] == arrayOfInt[m])
          {
            m--;
            n = 1;
          }
          else
          {
            n = 0;
          }
          i4--;
          if (m < 0)
            i5 = -1;
        }
        if (m < 0)
        {
          l -= i4;
          break;
        }
        l -= i1;
        if (l < 0L)
        {
          l = this.eof;
          break;
        }
      }
      int i3 = (int)(l - k);
      if (i3 < 0)
      {
        i3 = 0;
        i4 = (int)this.eof;
        arrayOfByte1 = new byte[i4];
        j = i4 + 3;
      }
      arrayOfByte1 = getBytes(i3, arrayOfByte1.length);
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog(new StringBuilder().append("Exception ").append(localException).append(" reading last 1024 bytes").toString());
      throw new PdfException(new StringBuilder().append(localException).append(" reading last 1024 bytes").toString());
    }
    int i2 = arrayOfByte1.length;
    if (j > i2);
    for (j = i2 - 5; (j > -1) && (((arrayOfByte1[j] == 116) && (arrayOfByte1[(j + 1)] == 120)) || ((arrayOfByte1[j] != 114) || (arrayOfByte1[(j + 1)] != 116) || (arrayOfByte1[(j + 2)] != 114) || (arrayOfByte1[(j + 3)] != 101) || (arrayOfByte1[(j + 4)] != 102))); j--);
    if (j == -1)
    {
      try
      {
        closeFile();
      }
      catch (IOException localIOException1)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog(new StringBuilder().append("Exception ").append(localIOException1).append(" closing file").toString());
      }
      throw new PdfException("No Startxref found in last 1024 bytes ");
    }
    j += 5;
    while ((j < 1024) && ((arrayOfByte1[j] == 10) || (arrayOfByte1[j] == 32) || (arrayOfByte1[j] == 13)))
      j++;
    while ((j < 1024) && (arrayOfByte1[j] != 10) && (arrayOfByte1[j] != 32) && (arrayOfByte1[j] != 13))
    {
      localStringBuilder.append((char)arrayOfByte1[j]);
      j++;
    }
    if (localStringBuilder.length() > 0)
      i = Integer.parseInt(localStringBuilder.toString());
    if (i == -1)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("No Startref found in last 1024 bytes ");
      try
      {
        closeFile();
      }
      catch (IOException localIOException2)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog(new StringBuilder().append("Exception ").append(localIOException2).append(" closing file").toString());
      }
      throw new PdfException("No Startref found in last 1024 bytes ");
    }
    return i;
  }

  public final PdfObject readReferenceTable(PdfObject paramPdfObject)
    throws PdfException
  {
    this.linearObj = paramPdfObject;
    int i = -1;
    int j = (int)this.eof;
    int k = 0;
    Object localObject;
    if (paramPdfObject == null)
    {
      i = readFirstStartRef();
    }
    else
    {
      localObject = getBuffer();
      int m = localObject.length;
      int n = 5;
      for (int i1 = 0; i1 < m; i1++)
      {
        if ((localObject[i1] == 101) && (localObject[(i1 + 1)] == 110) && (localObject[(i1 + 2)] == 100) && (localObject[(i1 + 3)] == 111) && (localObject[(i1 + 4)] == 98) && (localObject[(i1 + 5)] == 106))
          n = i1 + 6;
        if ((localObject[i1] == 120) && (localObject[(i1 + 1)] == 114) && (localObject[(i1 + 2)] == 101) && (localObject[(i1 + 3)] == 102))
        {
          i = i1;
          i1 = m;
        }
        else if ((localObject[i1] == 88) && (localObject[(i1 + 1)] == 82) && (localObject[(i1 + 2)] == 101) && (localObject[(i1 + 3)] == 102))
        {
          k = 1;
          for (i = n; (localObject[i] == 10) || (localObject[i] == 13) || (localObject[i] == 32); i++);
          i1 = m;
        }
      }
    }
    this.xref.addElement(i);
    if (i >= j)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Pointer not if file - trying to manually find startref");
      this.refTableInvalid = true;
      localObject = new PageObject(findOffsets());
      readObject((PdfObject)localObject);
      return localObject;
    }
    if ((k != 0) || (isCompressedStream(i, j)))
      return readCompressedStream(i);
    return readLegacyReferenceTable(i, j);
  }

  public void spoolStreamDataToDisk(File paramFile, long paramLong, int paramInt)
    throws Exception
  {
    movePointer(paramLong);
    int i = 0;
    int j = 0;
    int k = 0;
    BufferedOutputStream localBufferedOutputStream = new BufferedOutputStream(new FileOutputStream(paramFile));
    int m = -1;
    int n = 0;
    int i1 = 0;
    int i2 = 0;
    if (m < 1)
      m = 128;
    int i3 = 0;
    int i4 = m;
    Object localObject = new byte[i4];
    int i5 = 0;
    int i6 = 0;
    int i7 = 0;
    byte[] arrayOfByte1 = null;
    int i8 = 0;
    try
    {
      int i9 = 0;
      int i10 = 0;
      int i11 = 0;
      int i12 = m - 1;
      int i13 = -m;
      while (true)
      {
        i12++;
        if (i12 == m)
        {
          long l = getPointer();
          if (paramLong == -1L)
            paramLong = l;
          if (l + m > this.eof)
            m = (int)(this.eof - l);
          m += 6;
          arrayOfByte1 = new byte[m];
          read(arrayOfByte1);
          i13 += i12;
          i12 = 0;
        }
        i10 = i9;
        i9 = arrayOfByte1[i12];
        if ((i2 > 0) && (i9 == ObjectDecoder.endPattern[i5]) && (i8 == 0))
          i5++;
        else
          i5 = 0;
        if ((j != 0) && (i9 == endObj[i6]) && (i8 == 0))
          i6++;
        else
          i6 = 0;
        if ((k != 0) && ((i != 0) || ((i9 != 13) && (i9 != 10))))
        {
          localBufferedOutputStream.write(i9);
          i = 1;
          i1++;
        }
        if ((n < 6) && (i9 == startStream[n]))
        {
          n++;
          if (n == 6)
            k = 1;
        }
        else
        {
          n = 0;
        }
        if (i1 >= paramInt)
          break;
        if (i8 == 0)
        {
          localObject[i3] = i9;
          i3++;
          if (i3 == i4)
          {
            if (i4 < 2097152)
              i4 *= 2;
            else
              i4 += 100000;
            byte[] arrayOfByte2 = new byte[i4];
            System.arraycopy(localObject, 0, arrayOfByte2, 0, localObject.length);
            localObject = arrayOfByte2;
          }
        }
      }
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
      if (LogWriter.isOutput())
        LogWriter.writeLog(new StringBuilder().append("Exception ").append(localException).append(" reading object").toString());
    }
    if (localBufferedOutputStream != null)
    {
      localBufferedOutputStream.flush();
      localBufferedOutputStream.close();
    }
  }

  public void spoolStreamDataToDisk(File paramFile, long paramLong)
    throws Exception
  {
    movePointer(paramLong);
    int i = 0;
    BufferedOutputStream localBufferedOutputStream = new BufferedOutputStream(new FileOutputStream(paramFile));
    int j = -1;
    int k = 0;
    int m = 0;
    int n = j;
    int i1 = 0;
    int i2 = 0;
    int i3 = 0;
    if (j < 1)
      j = 128;
    int i4 = 0;
    int i5 = j;
    Object localObject = new byte[i5];
    int i6 = 0;
    int i7 = 0;
    int i8 = 0;
    byte[] arrayOfByte1 = null;
    int i9 = 0;
    try
    {
      int i12 = j - 1;
      int i13 = -j;
      while (true)
      {
        i12++;
        if (i12 == j)
        {
          long l = getPointer();
          if (paramLong == -1L)
            paramLong = l;
          if (l + j > this.eof)
            j = (int)(this.eof - l);
          j += 6;
          arrayOfByte1 = new byte[j];
          read(arrayOfByte1);
          i13 += i12;
          i12 = 0;
        }
        int i11 = arrayOfByte1[i12];
        int i10 = 0;
        if ((i11 == ObjectDecoder.endPattern[i6]) && (i9 == 0))
          i6++;
        else
          i6 = 0;
        if ((i3 != 0) && (i11 == endObj[i7]) && (i9 == 0))
          i7++;
        else
          i7 = 0;
        if ((m != 0) && ((i != 0) || ((i11 != 13) && (i11 != 10))))
        {
          localBufferedOutputStream.write(i11);
          i = 1;
        }
        if ((k < 6) && (i11 == startStream[k]))
          k++;
        else
          k = 0;
        if ((m == 0) && (k == 6))
          m = 1;
        if ((i2 == 0) && (n != -1))
          if ((i3 == 0) && (i11 == lengthString[i8]) && (i9 == 0))
          {
            i8++;
            if (i8 == 6)
              i2 = 1;
          }
          else
          {
            i8 = 0;
          }
        if ((i6 == 6) || (i7 == 4))
        {
          if (i2 == 0)
            break;
          i6 = 0;
          i7 = 0;
        }
        if ((i2 != 0) && (i1 >= n))
          break;
        if ((i10 == 0) && (i9 == 0))
        {
          localObject[i4] = i11;
          i4++;
          if (i4 == i5)
          {
            if (i5 < 2097152)
              i5 *= 2;
            else
              i5 += 100000;
            byte[] arrayOfByte2 = new byte[i5];
            System.arraycopy(localObject, 0, arrayOfByte2, 0, localObject.length);
            localObject = arrayOfByte2;
          }
        }
        i1++;
      }
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
      if (LogWriter.isOutput())
        LogWriter.writeLog(new StringBuilder().append("Exception ").append(localException).append(" reading object").toString());
    }
    if (localBufferedOutputStream != null)
    {
      localBufferedOutputStream.flush();
      localBufferedOutputStream.close();
    }
  }

  private boolean isCompressedStream(int paramInt1, int paramInt2)
    throws PdfException
  {
    int i = 50;
    int j = 0;
    int k = 0;
    int m = 0;
    int[] arrayOfInt1 = { 79, 98, 106, 83, 116, 109 };
    int[] arrayOfInt2 = { 88, 82, 101, 102 };
    int n = -1;
    int i1 = 1;
    while (true)
    {
      if (paramInt1 + i > paramInt2)
        i = paramInt2 - paramInt1;
      if (i < 0)
        i = 50;
      byte[] arrayOfByte = getBytes(paramInt1, i);
      if ((i1 != 0) && (arrayOfByte[0] == 114) && (arrayOfByte[1] == 101) && (arrayOfByte[2] == 102))
        j = 1;
      i1 = 0;
      for (int i2 = 0; i2 < i; i2++)
      {
        int i3 = arrayOfByte[i2];
        if ((i3 == oldPattern[j]) && (n != 1))
        {
          j++;
          n = 2;
        }
        else if ((i3 == arrayOfInt1[k]) && ((k == 0) || (n == 1)))
        {
          k++;
          n = 1;
        }
        else if ((i3 == arrayOfInt2[m]) && ((m == 0) || (n == 1)))
        {
          m++;
          n = 1;
        }
        else
        {
          j = 0;
          k = 0;
          m = 0;
          n = -1;
        }
        if ((j == 3) || (k == 4) || (m == 3))
          break;
      }
      if ((j == 3) || (k == 4) || (m == 3))
        break;
      paramInt1 += i;
    }
    if (n == -1)
    {
      try
      {
        closeFile();
      }
      catch (IOException localIOException)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog("Exception 1 closing file");
      }
      throw new PdfException("Exception unable to find ref or obj in trailer");
    }
    return n == 1;
  }

  void closeFile()
    throws IOException
  {
    if (this.pdf_datafile != null)
    {
      this.pdf_datafile.close();
      this.pdf_datafile = null;
    }
  }

  private byte[] readTrailer(int paramInt1, int paramInt2, int paramInt3)
    throws IOException
  {
    int i = 0;
    int j = 0;
    int k = 0;
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    while (true)
    {
      if (paramInt2 + paramInt1 > paramInt3)
        paramInt1 = paramInt3 - paramInt2;
      if (paramInt1 == 0)
        break;
      byte[] arrayOfByte = getBytes(paramInt2, paramInt1);
      int m = 0;
      for (int n = 0; n < paramInt1; n++)
      {
        int i1 = arrayOfByte[n];
        if (i1 == EOFpattern[i])
          i++;
        else
          i = 0;
        if (i1 == trailerpattern[j])
          j++;
        else
          j = 0;
        if (j == 7)
        {
          k++;
          j = 0;
        }
        if ((i == 4) || (k == 2))
        {
          for (int i2 = 0; i2 < n + 1; i2++)
            localByteArrayOutputStream.write(arrayOfByte[i2]);
          n = paramInt1;
          m = 1;
        }
      }
      if (m == 0)
        localByteArrayOutputStream.write(arrayOfByte);
      paramInt2 += paramInt1;
      if ((i == 4) || (k == 2))
        break;
    }
    localByteArrayOutputStream.close();
    return localByteArrayOutputStream.toByteArray();
  }

  public byte[] getBuffer()
  {
    return this.pdf_datafile.getPdfBuffer();
  }

  private static int getWord(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    int i = 0;
    for (int j = 0; j < paramInt2; j++)
      i = (i << 8) + (paramArrayOfByte[(paramInt1 + j)] & 0xFF);
    return i;
  }

  private int readXRefs(Vector_Int paramVector_Int, int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3)
  {
    int i3 = 0;
    int i4 = 1;
    int[] arrayOfInt1 = new int[5];
    int[] arrayOfInt2 = new int[5];
    while (paramInt3 < paramInt2)
    {
      int i1 = paramInt3;
      int i2 = -1;
      while ((paramArrayOfByte[paramInt3] != 10) && (paramArrayOfByte[paramInt3] != 13))
      {
        if ((i2 == -1) && (paramArrayOfByte[paramInt3] == 37))
          i2 = paramInt3 - 1;
        paramInt3++;
      }
      if (i2 == -1)
        i2 = paramInt3 - 1;
      while (paramArrayOfByte[i1] == 32)
        i1++;
      while (paramArrayOfByte[i2] == 32)
        i2--;
      paramInt3++;
      int k = 0;
      int n = i2 - i1 + 1;
      if (n > 0)
      {
        int i5 = 1;
        for (int i7 = 1; i7 < n; i7++)
        {
          int i6 = paramArrayOfByte[(i1 + i7)];
          if ((i6 == 32) && (i5 != 32))
          {
            arrayOfInt1[k] = i7;
            k++;
          }
          else if ((i6 != 32) && (i5 == 32))
          {
            arrayOfInt2[k] = i7;
          }
          i5 = i6;
        }
        arrayOfInt1[k] = n;
        k++;
        if (k == 1)
        {
          if (i3 != 0)
          {
            i3 = 0;
          }
          else
          {
            paramInt1 = NumberUtils.parseInt(i1, i1 + arrayOfInt1[0], paramArrayOfByte);
            i3 = 1;
          }
        }
        else if (k == 2)
        {
          paramInt1 = NumberUtils.parseInt(i1, i1 + arrayOfInt1[0], paramArrayOfByte);
        }
        else
        {
          int j = NumberUtils.parseInt(i1, i1 + arrayOfInt1[0], paramArrayOfByte);
          int m = NumberUtils.parseInt(i1 + arrayOfInt2[1], i1 + arrayOfInt1[1], paramArrayOfByte);
          int i = (char)paramArrayOfByte[(i1 + arrayOfInt2[2])];
          if (i == 110)
          {
            i7 = 0;
            int i8 = 20;
            if (j + i8 > this.eof)
              i8 = (int)(this.eof - j);
            if (i8 > 0)
            {
              byte[] arrayOfByte = getBytes(j, i8);
              for (int i9 = 4; i9 < i8; i9++)
                if (((arrayOfByte[(i9 - 3)] == 32) || (arrayOfByte[(i9 - 3)] == 10)) && (arrayOfByte[(i9 - 2)] == 111) && (arrayOfByte[(i9 - 1)] == 98) && (arrayOfByte[i9] == 106))
                {
                  i7 = 1;
                  i9 = i8;
                }
              if ((i7 != 0) && (i4 != 0))
              {
                i4 = 0;
                if ((arrayOfByte[0] == 48) && (arrayOfByte[1] != 48) && (paramInt1 == 1))
                  paramInt1 = 0;
                else if ((arrayOfByte[0] == 49) && (arrayOfByte[1] == 32))
                  paramInt1 = 1;
              }
              if (i7 != 0)
              {
                storeObjectOffset(paramInt1, j, m, false, false);
                paramVector_Int.addElement(j);
              }
            }
            paramInt1++;
          }
          else if (i == 102)
          {
            paramInt1++;
          }
        }
      }
    }
    return paramInt1;
  }

  private void extractCompressedObjectOffset(Map paramMap1, Map paramMap2, int paramInt1, byte[] paramArrayOfByte, int paramInt2)
  {
    Object localObject = null;
    for (int i1 = 0; i1 < paramInt1; i1++)
      if (paramArrayOfByte.length != 0)
      {
        while ((paramArrayOfByte[i1] == 10) || (paramArrayOfByte[i1] == 13) || (paramArrayOfByte[i1] == 32))
          i1++;
        int i = i1;
        if (i != paramInt1)
        {
          while ((paramArrayOfByte[i1] != 32) && (paramArrayOfByte[i1] != 13) && (paramArrayOfByte[i1] != 10))
            i1++;
          int j = i1 - 1;
          int i2 = j - i + 1;
          char[] arrayOfChar = new char[i2];
          for (int i3 = 0; i3 < i2; i3++)
            arrayOfChar[i3] = ((char)paramArrayOfByte[(i + i3)]);
          String str1 = new String(arrayOfChar);
          int n = NumberUtils.parseInt(i, i + i2, paramArrayOfByte);
          while ((paramArrayOfByte[i1] == 32) || (paramArrayOfByte[i1] == 13) || (paramArrayOfByte[i1] == 10))
            i1++;
          int k = i1;
          while ((paramArrayOfByte[i1] != 32) && (paramArrayOfByte[i1] != 13) && (paramArrayOfByte[i1] != 10) && (i1 < paramInt1))
            i1++;
          int m = i1 - 1;
          i2 = m - k + 1;
          arrayOfChar = new char[i2];
          for (i3 = 0; i3 < i2; i3++)
            arrayOfChar[i3] = ((char)paramArrayOfByte[(k + i3)]);
          String str2 = new String(arrayOfChar);
          if (paramInt2 == getOffset(n))
          {
            paramMap1.put(str1, str2);
            if (localObject != null)
              paramMap2.put(localObject, str2);
            localObject = str1;
          }
        }
      }
  }

  private int readCompressedOffsets(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt, byte[] paramArrayOfByte, int paramInt4)
    throws PdfException
  {
    int[] arrayOfInt1 = { 1, 0, 0 };
    int i = 0;
    for (int j = 0; j < paramInt3; j++)
    {
      int[] arrayOfInt2 = new int[3];
      for (int k = 0; k < 3; k++)
        if (paramArrayOfInt[k] == 0)
        {
          arrayOfInt2[k] = arrayOfInt1[k];
        }
        else
        {
          arrayOfInt2[k] = getWord(paramArrayOfByte, paramInt1, paramArrayOfInt[k]);
          paramInt1 += paramArrayOfInt[k];
        }
      switch (arrayOfInt2[0])
      {
      case 0:
        paramInt2++;
        i = (arrayOfInt2[1] == 0) && (arrayOfInt2[2] == 0) ? 1 : 0;
        break;
      case 1:
        k = arrayOfInt2[1];
        int m = arrayOfInt2[2];
        int n = 1;
        if (paramInt2 == k)
        {
          n = 0;
          try
          {
            int i1 = 20;
            byte[] arrayOfByte = getBytes(paramInt2, i1);
            int i2 = 0;
            for (int i3 = 0; i3 < i1; i3++)
              if ((arrayOfByte[i3] == 32) || (arrayOfByte[i3] == 10) || (arrayOfByte[i3] == 13))
              {
                i2 = i3;
                i3 = i1;
              }
            if (i2 > 0)
            {
              i3 = NumberUtils.parseInt(0, i2, arrayOfByte);
              if (i3 == paramInt2)
                n = 1;
            }
          }
          catch (Exception localException)
          {
            n = 0;
          }
        }
        if ((n != 0) || (i == 0))
          storeObjectOffset(paramInt2, k, m, false, false);
        paramInt2++;
        break;
      case 2:
        k = arrayOfInt2[1];
        storeObjectOffset(paramInt2, k, 0, true, false);
        paramInt2++;
        break;
      default:
        throw new PdfException(new StringBuilder().append("Exception Unsupported Compression mode with value ").append(arrayOfInt2[0]).toString());
      }
    }
    return paramInt1;
  }

  byte[] readFDFData()
    throws IOException
  {
    int i = (int)this.pdf_datafile.length();
    this.pdf_datafile.readLine();
    int j = (int)this.pdf_datafile.getFilePointer();
    i -= j;
    byte[] arrayOfByte = new byte[i];
    this.pdf_datafile.read(arrayOfByte);
    return arrayOfByte;
  }

  private int[] calculateObjectLength(int paramInt)
  {
    this.xref.addElement(paramInt);
    int[] arrayOfInt1 = this.xref.get();
    int i = arrayOfInt1.length;
    int[] arrayOfInt2 = new int[i];
    for (int j = 0; j < i; j++)
      arrayOfInt2[j] = j;
    arrayOfInt2 = Sorts.quicksort(arrayOfInt1, arrayOfInt2);
    j = this.offset.getCapacity();
    int[] arrayOfInt3 = new int[j];
    int[] arrayOfInt4 = new int[j];
    int[] arrayOfInt5 = this.offset.get();
    boolean[] arrayOfBoolean = this.isCompressed.get();
    for (int k = 0; k < j; k++)
      if (arrayOfBoolean[k] == 0)
      {
        arrayOfInt4[k] = arrayOfInt5[k];
        arrayOfInt3[k] = k;
      }
    arrayOfInt3 = Sorts.quicksort(arrayOfInt4, arrayOfInt3);
    for (k = 0; arrayOfInt4[arrayOfInt3[k]] == 0; k++);
    int m = arrayOfInt4[arrayOfInt3[k]];
    for (int i1 = 0; arrayOfInt1[arrayOfInt2[i1]] < m + 1; i1++);
    int[] arrayOfInt6 = new int[j];
    while (k < j - 1)
    {
      int n = arrayOfInt4[arrayOfInt3[(k + 1)]];
      int i2 = n - m - 1;
      if (arrayOfInt1[arrayOfInt2[i1]] < n)
      {
        i2 = arrayOfInt1[arrayOfInt2[i1]] - m - 1;
        while (arrayOfInt1[arrayOfInt2[i1]] < n + 1)
          i1++;
      }
      arrayOfInt6[arrayOfInt3[k]] = i2;
      m = n;
      while (arrayOfInt1[arrayOfInt2[i1]] < m + 1)
        i1++;
      k++;
    }
    arrayOfInt6[arrayOfInt3[k]] = (arrayOfInt1[arrayOfInt2[i1]] - m - 1);
    return arrayOfInt6;
  }

  public long getOffset(int paramInt)
  {
    return this.offset.elementAt(paramInt);
  }

  public byte[] getBytes(long paramLong, int paramInt)
  {
    byte[] arrayOfByte = new byte[paramInt];
    movePointer(paramLong);
    try
    {
      this.pdf_datafile.read(arrayOfByte);
    }
    catch (IOException localIOException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog(new StringBuilder().append("Exception: ").append(localIOException.getMessage()).toString());
    }
    return arrayOfByte;
  }

  private String findOffsets()
    throws PdfSecurityException
  {
    if (LogWriter.isOutput())
      LogWriter.writeLog("Corrupt xref table - trying to find objects manually");
    String str1 = "";
    String str2 = null;
    movePointer(0L);
    do
      while (true)
      {
        int j = (int)getPointer();
        try
        {
          str2 = this.pdf_datafile.readLine();
        }
        catch (Exception localException)
        {
          if (LogWriter.isOutput())
            LogWriter.writeLog(new StringBuilder().append("Exception ").append(localException).append(" reading line").toString());
        }
        if (str2 == null)
          break label194;
        int i;
        if (str2.contains(" obj"))
        {
          i = str2.indexOf(32);
          if (i > -1)
            storeObjectOffset(Integer.parseInt(str2.substring(0, i)), j, 1, false, true);
        }
        else
        {
          if (!str2.contains("/Root"))
            break;
          int k = str2.indexOf("/Root") + 5;
          i = str2.indexOf(82, k);
          if (i > -1)
            str1 = str2.substring(k, i + 1).trim();
        }
      }
    while (!str2.contains("/Encrypt"));
    throw new PdfSecurityException("Corrupted, encrypted file");
    label194: return str1;
  }

  public void storeLinearizedTables(LinearizedHintTable paramLinearizedHintTable)
  {
    this.linHintTable = paramLinearizedHintTable;
  }

  public void dispose()
  {
    if (this.decryption != null)
    {
      this.decryption.flush();
      this.decryption.dispose();
    }
    if (this.decryption != null)
      this.decryption.cipher = null;
    this.decryption = null;
    this.compressedObj = null;
    if (this.linHintTable != null)
      this.linHintTable = null;
    this.offset = null;
    this.generation = null;
    this.isCompressed = null;
    try
    {
      if (this.pdf_datafile != null)
        this.pdf_datafile.close();
    }
    catch (IOException localIOException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog(new StringBuilder().append("Exception: ").append(localIOException.getMessage()).toString());
    }
    this.pdf_datafile = null;
    this.xref = null;
  }

  private void movePointer(String paramString)
  {
    long l = getOffset(paramString);
    movePointer(l);
  }

  public final String getType()
  {
    String str = "";
    try
    {
      movePointer(0L);
      str = this.pdf_datafile.readLine();
      int i = str.indexOf("%PDF");
      if (i != -1)
        str = str.substring(i + 5);
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog(new StringBuilder().append("Exception ").append(localException).append(" in reading type").toString());
    }
    return str;
  }

  private void movePointer(long paramLong)
  {
    try
    {
      if (paramLong > this.pdf_datafile.length())
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog("Attempting to access ref outside file");
      }
      else
        this.pdf_datafile.seek(paramLong);
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog(new StringBuilder().append("Exception ").append(localException).append(" moving pointer to  ").append(paramLong).append(" in file.").toString());
    }
  }

  private void read(byte[] paramArrayOfByte)
    throws IOException
  {
    this.pdf_datafile.read(paramArrayOfByte);
  }

  private long getPointer()
  {
    long l = 0L;
    try
    {
      l = this.pdf_datafile.getFilePointer();
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog(new StringBuilder().append("Exception ").append(localException).append(" getting pointer in file").toString());
    }
    return l;
  }

  private void storeObjectOffset(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean1, boolean paramBoolean2)
  {
    int i = 0;
    int j = 0;
    if (paramInt1 < this.generation.getCapacity())
    {
      i = this.generation.elementAt(paramInt1);
      j = this.offset.elementAt(paramInt1);
    }
    if ((i < paramInt3) || (j == 0) || ((paramBoolean2) && (paramInt2 > this.offset.elementAt(paramInt1))))
    {
      this.offset.setElementAt(paramInt2, paramInt1);
      this.generation.setElementAt(paramInt3, paramInt1);
      this.isCompressed.setElementAt(paramBoolean1, paramInt1);
    }
  }

  private int getCompressedStreamObject(String paramString)
  {
    int i = 0;
    if (paramString.endsWith("R"))
    {
      StringTokenizer localStringTokenizer = new StringTokenizer(paramString);
      i = Integer.parseInt(localStringTokenizer.nextToken());
    }
    else if (LogWriter.isOutput())
    {
      LogWriter.writeLog(new StringBuilder().append("Error with reference ..value=").append(paramString).append('<').toString());
    }
    return this.offset.elementAt(i);
  }

  private int getOffset(String paramString)
  {
    int i = 0;
    if (paramString.endsWith("R"))
    {
      StringTokenizer localStringTokenizer = new StringTokenizer(paramString);
      i = Integer.parseInt(localStringTokenizer.nextToken());
    }
    else if (LogWriter.isOutput())
    {
      LogWriter.writeLog(new StringBuilder().append("2. Error with reference ..").append(paramString).append("<<").toString());
    }
    return this.offset.elementAt(i);
  }

  public final boolean isCompressed(int paramInt1, int paramInt2)
  {
    return this.isCompressed.elementAt(paramInt1);
  }

  private boolean isCompressed(String paramString)
  {
    int i = 0;
    if (paramString.endsWith("R"))
    {
      StringTokenizer localStringTokenizer = new StringTokenizer(paramString);
      i = Integer.parseInt(localStringTokenizer.nextToken());
    }
    else if (LogWriter.isOutput())
    {
      LogWriter.writeLog(new StringBuilder().append("5.Error with reference ..").append(paramString).append('<').toString());
    }
    return this.isCompressed.elementAt(i);
  }

  public DecryptionFactory getDecryptionObject()
  {
    return this.decryption;
  }

  public void setPassword(String paramString)
  {
    this.encryptionPassword = paramString.getBytes();
    if (this.decryption != null)
      this.decryption.reset(this.encryptionPassword);
  }

  byte[] readObjectData(PdfObject paramPdfObject)
  {
    String str = paramPdfObject.getObjectRefAsString();
    if (paramPdfObject.isDataExternal())
    {
      byte[] arrayOfByte1 = readObjectAsByteArray(paramPdfObject, false, paramPdfObject.getObjectRefID(), 0);
      if (arrayOfByte1 == null)
      {
        paramPdfObject.setFullyResolved(false);
        if (LogWriter.isOutput())
          LogWriter.writeLog(new StringBuilder().append("[Linearized] ").append(paramPdfObject.getObjectRefAsString()).append(" not yet available (15)").toString());
        return arrayOfByte1;
      }
    }
    boolean bool = isCompressed(str);
    paramPdfObject.setCompressedStream(bool);
    byte[] arrayOfByte2;
    if (bool)
    {
      arrayOfByte2 = readCompressedObjectData(paramPdfObject, str);
    }
    else
    {
      movePointer(str);
      if (str.charAt(0) == '<')
      {
        arrayOfByte2 = readObjectData(-1, paramPdfObject);
      }
      else
      {
        int i = str.indexOf(32);
        int j = Integer.parseInt(str.substring(0, i));
        if ((this.ObjLengthTable == null) || (this.refTableInvalid))
        {
          if (getPointer() == 0L)
            arrayOfByte2 = new byte[0];
          else
            arrayOfByte2 = readObjectData(-1, paramPdfObject);
        }
        else if ((j > this.ObjLengthTable.length) || (this.ObjLengthTable[j] == 0))
        {
          if (LogWriter.isOutput())
            LogWriter.writeLog(new StringBuilder().append(str).append(" cannot have offset 0").toString());
          arrayOfByte2 = new byte[0];
        }
        else
        {
          arrayOfByte2 = readObjectData(this.ObjLengthTable[j], paramPdfObject);
        }
      }
    }
    return arrayOfByte2;
  }

  private byte[] readCompressedObjectData(PdfObject paramPdfObject, String paramString)
  {
    int i = Integer.parseInt(paramString.substring(0, paramString.indexOf(32)));
    int j = getCompressedStreamObject(paramString);
    String str1 = new StringBuilder().append(j).append(" 0 R").toString();
    String str2 = null;
    int k = this.lastFirst;
    int m = 1;
    byte[] arrayOfByte2 = this.lastCompressedStream;
    Object localObject1 = this.lastOffsetStart;
    Object localObject2 = this.lastOffsetEnd;
    PdfObject localPdfObject = null;
    if (this.lastOffsetStart != null)
      str2 = (String)this.lastOffsetStart.get(String.valueOf(i));
    while (str2 == null)
    {
      if (localPdfObject != null)
      {
        this.compressedObj = localPdfObject;
      }
      else if (j != this.lastCompressedID)
      {
        m = 0;
        movePointer(str1);
        arrayOfByte1 = readObjectData(this.ObjLengthTable[j], null);
        this.compressedObj = new CompressedObject(str1);
        ObjectDecoder localObjectDecoder = new ObjectDecoder(this);
        localObjectDecoder.readDictionaryAsObject(this.compressedObj, 0, arrayOfByte1);
      }
      localObject1 = new HashMap();
      localObject2 = new HashMap();
      k = this.compressedObj.getInt(960643930);
      arrayOfByte2 = this.compressedObj.getDecodedStream();
      extractCompressedObjectOffset((Map)localObject1, (Map)localObject2, k, arrayOfByte2, j);
      str2 = (String)((Map)localObject1).get(String.valueOf(i));
      localPdfObject = this.compressedObj.getDictionary(894663815);
      if (localPdfObject == null)
        break;
    }
    if (m == 0)
    {
      this.lastCompressedStream = arrayOfByte2;
      this.lastCompressedID = j;
      this.lastOffsetStart = ((Map)localObject1);
      this.lastOffsetEnd = ((Map)localObject2);
      this.lastFirst = k;
    }
    int n = k + Integer.parseInt(str2);
    int i1 = arrayOfByte2.length;
    String str3 = (String)((Map)localObject2).get(String.valueOf(i));
    if (str3 != null)
      i1 = k + Integer.parseInt(str3);
    int i2 = i1 - n;
    byte[] arrayOfByte1 = new byte[i2];
    System.arraycopy(arrayOfByte2, n, arrayOfByte1, 0, i2);
    paramPdfObject.setInCompressedStream(true);
    return arrayOfByte1;
  }

  private byte[] readObjectData(int paramInt, PdfObject paramPdfObject)
  {
    if ((paramInt < 1) || (this.newCacheSize != -1) || (this.fileIsBroken))
      return readObjectDataXX(paramInt, paramPdfObject);
    byte[] arrayOfByte = null;
    if (paramInt > 0)
    {
      paramInt += 6;
      arrayOfByte = new byte[paramInt];
      fillBuffer(arrayOfByte);
    }
    return arrayOfByte;
  }

  private void fillBuffer(byte[] paramArrayOfByte)
  {
    try
    {
      read(paramArrayOfByte);
    }
    catch (IOException localIOException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog(new StringBuilder().append("Exception: ").append(localIOException.getMessage()).toString());
    }
  }

  private byte[] readObjectDataXX(int paramInt, PdfObject paramPdfObject)
  {
    int i = -1;
    int j = 0;
    int k = 0;
    int m = 0;
    int n = 0;
    int i1 = 0;
    int i2 = 0;
    int i3 = 1;
    long l1 = getPointer();
    if (paramPdfObject != null)
      i = this.newCacheSize;
    int i4 = paramInt;
    int i5 = 0;
    int i6 = 0;
    if (paramInt < 1)
      paramInt = 128;
    if ((i != -1) && (paramInt > i))
      paramInt = i;
    Object localObject = null;
    int i8 = paramInt - 1;
    while (i3 != 0)
    {
      i8++;
      if (i8 == paramInt)
      {
        long l2 = getPointer();
        if (l2 + paramInt > this.eof)
          paramInt = (int)(this.eof - l2);
        if (paramInt == 0)
          break;
        paramInt += 6;
        byte[] arrayOfByte1 = new byte[paramInt];
        fillBuffer(arrayOfByte1);
        if (localObject == null)
        {
          int i9 = 0;
          for (int i10 = 0; i10 < 10; i10++)
            if ((arrayOfByte1[i10] == 101) && (arrayOfByte1[(i10 + 1)] == 110) && (arrayOfByte1[(i10 + 2)] == 100) && (arrayOfByte1[(i10 + 3)] == 111) && (arrayOfByte1[(i10 + 4)] == 98) && (arrayOfByte1[(i10 + 5)] == 106))
            {
              i9 = i10;
              break;
            }
          while ((arrayOfByte1[i9] == 101) || (arrayOfByte1[i9] == 110) || (arrayOfByte1[i9] == 100) || (arrayOfByte1[i9] == 111) || (arrayOfByte1[i9] == 98) || (arrayOfByte1[i9] == 106))
            i9++;
          if (i9 > 0)
          {
            byte[] arrayOfByte2 = arrayOfByte1;
            int i11 = arrayOfByte1.length - i9;
            arrayOfByte1 = new byte[i11];
            System.arraycopy(arrayOfByte2, i9, arrayOfByte1, 0, i11);
            paramInt = arrayOfByte1.length;
          }
          localObject = arrayOfByte1;
        }
        else
        {
          localObject = appendDataBlock(arrayOfByte1.length, arrayOfByte1, (byte[])localObject);
        }
        i8 = 0;
      }
      int i7 = localObject[i5];
      if (i2 == 0)
        if (i7 == endPattern[k])
          k++;
        else
          k = 0;
      if ((n == 0) && (i != -1) && (i1 == 0))
        if ((j < 6) && (i7 == startStream[j]))
        {
          j++;
          if (j == 6)
            n = 1;
        }
        else
        {
          j = 0;
        }
      if ((n != 0) && (localObject != null) && (localObject.length > i))
      {
        paramPdfObject.setCache(l1, this);
        i1 = 1;
      }
      if ((n == 0) && (i6 == 0) && (i4 != -1))
        if ((i7 == lengthString[m]) && (i2 == 0))
        {
          m++;
          if (m == 6)
            i6 = 1;
        }
        else
        {
          m = 0;
        }
      i5++;
      if (k == 6)
      {
        if (i6 == 0)
          i3 = 0;
        k = 0;
      }
      if ((i6 != 0) && (i5 > i4))
        i3 = 0;
    }
    if (i6 == 0)
      localObject = ObjectUtils.checkEndObject((byte[])localObject);
    return localObject;
  }

  private static byte[] appendDataBlock(int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
  {
    int i = paramArrayOfByte2.length;
    byte[] arrayOfByte = new byte[i + paramInt];
    System.arraycopy(paramArrayOfByte2, 0, arrayOfByte, 0, i);
    System.arraycopy(paramArrayOfByte1, 0, arrayOfByte, i, paramInt);
    return arrayOfByte;
  }

  public byte[] readObjectAsByteArray(PdfObject paramPdfObject, boolean paramBoolean, int paramInt1, int paramInt2)
  {
    byte[] arrayOfByte1 = null;
    if (this.linHintTable != null)
      arrayOfByte1 = this.linHintTable.getObjData(paramInt1);
    if (arrayOfByte1 == null)
      if (paramBoolean)
      {
        arrayOfByte1 = readCompressedObjectAsByteArray(paramPdfObject, paramInt1, paramInt2);
      }
      else
      {
        movePointer(this.offset.elementAt(paramInt1));
        if ((this.ObjLengthTable == null) || (this.refTableInvalid))
        {
          arrayOfByte1 = readObjectData(-1, paramPdfObject);
        }
        else
        {
          if (paramInt1 > this.ObjLengthTable.length)
            return null;
          arrayOfByte1 = readObjectData(this.ObjLengthTable[paramInt1], paramPdfObject);
        }
      }
    int i = 0;
    if (arrayOfByte1.length > 15)
      for (int j = 0; (j < 10) && ((arrayOfByte1[j] != 111) || (arrayOfByte1[(j + 1)] != 98) || (arrayOfByte1[(j + 2)] != 106)); j++)
        if ((arrayOfByte1[j] == 101) && (arrayOfByte1[(j + 1)] == 110) && (arrayOfByte1[(j + 2)] == 100) && (arrayOfByte1[(j + 3)] == 111) && (arrayOfByte1[(j + 4)] == 98) && (arrayOfByte1[(j + 5)] == 106))
        {
          i = j + 6;
          this.fileIsBroken = true;
          break;
        }
    while ((arrayOfByte1[i] == 10) || (arrayOfByte1[i] == 12) || (arrayOfByte1[i] == 32))
      i++;
    if (i > 0)
    {
      byte[] arrayOfByte2 = arrayOfByte1;
      int k = arrayOfByte1.length - i;
      arrayOfByte1 = new byte[k];
      System.arraycopy(arrayOfByte2, i, arrayOfByte1, 0, k);
    }
    return arrayOfByte1;
  }

  private byte[] readCompressedObjectAsByteArray(PdfObject paramPdfObject, int paramInt1, int paramInt2)
  {
    int i = this.offset.elementAt(paramInt1);
    String str1 = null;
    Object localObject1 = this.lastOffsetStart;
    Object localObject2 = this.lastOffsetEnd;
    int j = this.lastFirst;
    int k = 1;
    byte[] arrayOfByte2 = this.lastCompressedStream;
    if (this.lastOffsetStart != null)
      str1 = (String)this.lastOffsetStart.get(String.valueOf(paramInt1));
    while (str1 == null)
    {
      k = 0;
      movePointer(this.offset.elementAt(i));
      arrayOfByte1 = readObjectData(this.ObjLengthTable[i], null);
      String str3 = new StringBuilder().append(i).append(" ").append(paramInt2).append(" R").toString();
      CompressedObject localCompressedObject = new CompressedObject(str3);
      ObjectDecoder localObjectDecoder = new ObjectDecoder(this);
      localObjectDecoder.readDictionaryAsObject(localCompressedObject, 0, arrayOfByte1);
      localObject1 = new HashMap();
      localObject2 = new HashMap();
      j = localCompressedObject.getInt(960643930);
      arrayOfByte2 = localCompressedObject.getDecodedStream();
      extractCompressedObjectOffset((Map)localObject1, (Map)localObject2, j, arrayOfByte2, i);
      str1 = (String)((Map)localObject1).get(String.valueOf(paramInt1));
      PdfObject localPdfObject = localCompressedObject.getDictionary(894663815);
      String str2;
      if (localPdfObject == null)
        str2 = null;
      else
        str2 = localPdfObject.getObjectRefAsString();
      if (str2 != null)
        i = Integer.parseInt(str2.substring(0, str2.indexOf(32)));
    }
    if (k == 0)
    {
      this.lastCompressedStream = arrayOfByte2;
      this.lastOffsetStart = ((Map)localObject1);
      this.lastOffsetEnd = ((Map)localObject2);
      this.lastFirst = j;
    }
    int m = j + Integer.parseInt(str1);
    int n = arrayOfByte2.length;
    String str4 = (String)((Map)localObject2).get(String.valueOf(paramInt1));
    if (str4 != null)
      n = j + Integer.parseInt(str4);
    int i1 = n - m;
    byte[] arrayOfByte1 = new byte[i1];
    System.arraycopy(arrayOfByte2, m, arrayOfByte1, 0, i1);
    paramPdfObject.setInCompressedStream(true);
    return arrayOfByte1;
  }

  public byte[] readPageIntoStream(PdfObject paramPdfObject)
  {
    byte[][] arrayOfByte = paramPdfObject.getKeyArray(1216184967);
    Object localObject = new byte[0];
    if ((arrayOfByte == null) || ((arrayOfByte != null) && (arrayOfByte.length > 0) && (arrayOfByte[0] == null)))
      return localObject;
    if (arrayOfByte != null)
    {
      int i = arrayOfByte.length;
      for (int j = 0; j < i; j++)
      {
        StreamObject localStreamObject = new StreamObject(new String(arrayOfByte[j]));
        localStreamObject.isDataExternal(paramPdfObject.isDataExternal());
        readObject(localStreamObject);
        byte[] arrayOfByte1 = localStreamObject.getDecodedStream();
        if ((j == 0) && (arrayOfByte1 != null))
          localObject = arrayOfByte1;
        else
          localObject = appendData((byte[])localObject, arrayOfByte1);
      }
    }
    return localObject;
  }

  private static byte[] appendData(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
  {
    if (paramArrayOfByte2 != null)
    {
      int i = paramArrayOfByte1.length + 1;
      int j = paramArrayOfByte2.length;
      if (j > 0)
      {
        while (paramArrayOfByte2[(j - 1)] == 0)
          j--;
        byte[] arrayOfByte = new byte[i];
        System.arraycopy(paramArrayOfByte1, 0, arrayOfByte, 0, i - 1);
        arrayOfByte[(i - 1)] = 32;
        paramArrayOfByte1 = new byte[i + j];
        System.arraycopy(arrayOfByte, 0, paramArrayOfByte1, 0, i);
        System.arraycopy(paramArrayOfByte2, 0, paramArrayOfByte1, i, j);
      }
    }
    return paramArrayOfByte1;
  }

  public void setCertificate(Certificate paramCertificate, PrivateKey paramPrivateKey)
  {
    this.certificate = paramCertificate;
    this.key = paramPrivateKey;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.io.PdfFileReader
 * JD-Core Version:    0.6.2
 */