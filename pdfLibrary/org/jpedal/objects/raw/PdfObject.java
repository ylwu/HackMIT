package org.jpedal.objects.raw;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import org.jpedal.io.ObjectStore;
import org.jpedal.io.PdfFileReader;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.StringUtils;

public class PdfObject
  implements Cloneable
{
  protected boolean maybeIndirect = false;
  protected boolean isFullyResolved = true;
  protected boolean isDataExternal = false;
  private boolean streamMayBeCorrupt;
  public static final int DECODED = 0;
  public static final int UNDECODED_REF = 1;
  public static final int UNDECODED_DIRECT = 2;
  private int status = 0;
  byte[] unresolvedData = null;
  Map otherValues = new HashMap();
  protected int pageNumber = -1;
  int PDFkeyInt = -1;
  int objType = -1;
  private int id = -1;
  protected int colorspace = -1;
  protected int subtype = -1;
  protected int type = -1;
  private int BitsPerComponent = -1;
  private int BitsPerCoordinate = -1;
  private int BitsPerFlag = -1;
  private int Count = 0;
  private int FormType = -1;
  private int Length = -1;
  private int Length1 = -1;
  private int Length2 = -1;
  private int Length3 = -1;
  private int Rotate = -1;
  private float[] ArtBox;
  private float[] BBox;
  private float[] BleedBox;
  private float[] CropBox;
  private float[] Decode;
  private float[] Domain;
  private float[] Matrix;
  private float[] MediaBox;
  private float[] Range;
  private float[] TrimBox;
  protected PdfObject ColorSpace = null;
  protected PdfObject DecodeParms = null;
  protected PdfObject Encoding = null;
  protected PdfObject Function = null;
  protected PdfObject Resources = null;
  protected PdfObject Shading = null;
  protected PdfObject SMask = null;
  private boolean ignoreRecursion = false;
  private boolean ignoreStream = false;
  protected boolean isZapfDingbats = false;
  protected boolean isSymbol = false;
  private boolean isCompressedStream = false;
  protected int generalType = -1;
  private String generalTypeAsString = null;
  protected boolean includeParent = false;
  private String Creator = null;
  private String Parent = null;
  private String Name = null;
  private String S = null;
  private String Title = null;
  private byte[] rawCreator;
  private byte[] rawParent;
  private byte[] rawName = null;
  private byte[] rawS;
  private byte[] rawTitle = null;
  public static boolean debug = false;
  protected String ref = null;
  int intRef;
  int gen;
  protected boolean hasStream = false;
  public byte[] stream = null;
  private byte[] DecodedStream = null;
  private long startStreamOnDisk = -1L;
  private PdfFileReader objReader = null;
  private String cacheName = null;
  private byte[][] Filter = (byte[][])null;
  private byte[][] TR = (byte[][])null;
  private byte[][] keys;
  private byte[][] values;
  private Object[] DecodeParmsAsArray;
  private PdfObject[] objs;
  protected Object currentKey = null;
  protected int parentType = -1;
  private boolean isInCompressedStream;
  private static int newXFAFormID = 1;

  protected void setInternalReference()
  {
    this.ref = new StringBuilder().append(newXFAFormID++).append(" 0 X").toString();
  }

  protected PdfObject()
  {
  }

  public PdfObject(int paramInt1, int paramInt2)
  {
    setRef(paramInt1, paramInt2);
  }

  public void setRef(int paramInt1, int paramInt2)
  {
    this.intRef = paramInt1;
    this.gen = paramInt2;
    this.ref = null;
  }

  public String getCacheName(PdfFileReader paramPdfFileReader)
  {
    if (isCached())
    {
      this.cacheName = null;
      getCachedStreamFile(paramPdfFileReader);
    }
    return this.cacheName;
  }

  public void setRef(String paramString)
  {
    this.ref = paramString;
  }

  public PdfObject(String paramString)
  {
    this.ref = paramString;
  }

  public PdfObject(int paramInt)
  {
    this.generalType = paramInt;
  }

  protected static boolean[] deepCopy(boolean[] paramArrayOfBoolean)
  {
    if (paramArrayOfBoolean == null)
      return null;
    int i = paramArrayOfBoolean.length;
    boolean[] arrayOfBoolean = new boolean[i];
    System.arraycopy(paramArrayOfBoolean, 0, arrayOfBoolean, 0, i);
    return arrayOfBoolean;
  }

  public int getStatus()
  {
    return this.status;
  }

  public byte[] getUnresolvedData()
  {
    return this.unresolvedData;
  }

  public int getPDFkeyInt()
  {
    return this.PDFkeyInt;
  }

  public void setUnresolvedData(byte[] paramArrayOfByte, int paramInt)
  {
    this.unresolvedData = paramArrayOfByte;
    this.PDFkeyInt = paramInt;
  }

  public void setStatus(int paramInt)
  {
    this.status = paramInt;
    this.unresolvedData = null;
  }

  protected static float[] deepCopy(float[] paramArrayOfFloat)
  {
    if (paramArrayOfFloat == null)
      return null;
    int i = paramArrayOfFloat.length;
    float[] arrayOfFloat = new float[i];
    System.arraycopy(paramArrayOfFloat, 0, arrayOfFloat, 0, i);
    return arrayOfFloat;
  }

  protected static double[] deepCopy(double[] paramArrayOfDouble)
  {
    if (paramArrayOfDouble == null)
      return null;
    int i = paramArrayOfDouble.length;
    double[] arrayOfDouble = new double[i];
    System.arraycopy(paramArrayOfDouble, 0, arrayOfDouble, 0, i);
    return arrayOfDouble;
  }

  protected static int[] deepCopy(int[] paramArrayOfInt)
  {
    if (paramArrayOfInt == null)
      return null;
    int i = paramArrayOfInt.length;
    int[] arrayOfInt = new int[i];
    System.arraycopy(paramArrayOfInt, 0, arrayOfInt, 0, i);
    return arrayOfInt;
  }

  protected static byte[][] deepCopy(byte[][] paramArrayOfByte)
  {
    if (paramArrayOfByte == null)
      return (byte[][])null;
    int i = paramArrayOfByte.length;
    byte[][] arrayOfByte = new byte[i][];
    System.arraycopy(paramArrayOfByte, 0, arrayOfByte, 0, i);
    return arrayOfByte;
  }

  public PdfObject getDictionary(int paramInt)
  {
    switch (paramInt)
    {
    case 2087749783:
      return this.ColorSpace;
    case 1888135062:
      return this.DecodeParms;
    case 1518239089:
      return this.Function;
    case 2004251818:
      return this.Resources;
    case 878474856:
      return this.Shading;
    case 489767774:
      return this.SMask;
    }
    return null;
  }

  public int getGeneralType(int paramInt)
  {
    if ((paramInt == 1232564598) && (this.isZapfDingbats))
      return 5;
    if ((paramInt == 1232564598) && (this.isSymbol))
      return 4;
    if (paramInt == 608780341)
      return this.objType;
    return this.generalType;
  }

  public String getGeneralStringValue()
  {
    return this.generalTypeAsString;
  }

  public void setGeneralStringValue(String paramString)
  {
    this.generalTypeAsString = paramString;
  }

  public void setIntNumber(int paramInt1, int paramInt2)
  {
    switch (paramInt1)
    {
    case -1344207655:
      this.BitsPerComponent = paramInt2;
      break;
    case -335950113:
      this.BitsPerCoordinate = paramInt2;
      break;
    case 1500422077:
      this.BitsPerFlag = paramInt2;
      break;
    case 1061502502:
      this.Count = paramInt2;
      break;
    case 982024818:
      this.FormType = paramInt2;
      break;
    case 1043816557:
      this.Length = paramInt2;
      break;
    case 929066303:
      this.Length1 = paramInt2;
      break;
    case 929066304:
      this.Length2 = paramInt2;
      break;
    case 929066305:
      this.Length3 = paramInt2;
      break;
    case 1144088180:
      this.Rotate = paramInt2;
      break;
    }
  }

  public void setFloatNumber(int paramInt, float paramFloat)
  {
    switch (paramInt)
    {
    }
  }

  public int getInt(int paramInt)
  {
    switch (paramInt)
    {
    case -1344207655:
      return this.BitsPerComponent;
    case -335950113:
      return this.BitsPerCoordinate;
    case 1500422077:
      return this.BitsPerFlag;
    case 1061502502:
      return this.Count;
    case 982024818:
      return this.FormType;
    case 1043816557:
      return this.Length;
    case 929066303:
      return this.Length1;
    case 929066304:
      return this.Length2;
    case 929066305:
      return this.Length3;
    case 1144088180:
      return this.Rotate;
    }
    return -1;
  }

  public float getFloatNumber(int paramInt)
  {
    switch (paramInt)
    {
    }
    return -1.0F;
  }

  public boolean getBoolean(int paramInt)
  {
    switch (paramInt)
    {
    }
    return false;
  }

  public void setBoolean(int paramInt, boolean paramBoolean)
  {
    switch (paramInt)
    {
    }
  }

  public void setDictionary(int paramInt, PdfObject paramPdfObject)
  {
    paramPdfObject.id = paramInt;
    switch (paramInt)
    {
    case 2087749783:
      this.ColorSpace = paramPdfObject;
      break;
    case 1888135062:
      this.DecodeParms = paramPdfObject;
      break;
    case 1518239089:
      this.Function = paramPdfObject;
      break;
    case 2004251818:
      this.Resources = paramPdfObject;
      break;
    case 878474856:
      this.Shading = paramPdfObject;
      break;
    case 489767774:
      this.SMask = paramPdfObject;
      break;
    default:
      setOtherValues(paramPdfObject);
    }
  }

  protected void setOtherValues(PdfObject paramPdfObject)
  {
    if ((this.objType == 373244477) || (this.objType == 487790868) || (this.currentKey != null))
    {
      this.otherValues.put(this.currentKey, paramPdfObject);
      this.currentKey = null;
    }
  }

  public void setID(int paramInt)
  {
    this.id = paramInt;
  }

  public int getID()
  {
    return this.id;
  }

  public int getParentID()
  {
    return this.parentType;
  }

  public boolean hasStream()
  {
    return this.hasStream;
  }

  public int setConstant(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte)
  {
    int i = -1;
    int j = 0;
    int k = 0;
    try
    {
      for (int n = paramInt3 - 1; n > -1; n--)
      {
        int m = paramArrayOfByte[(paramInt2 + n)];
        m -= 48;
        j += (m << k);
        k += 8;
      }
      switch (j)
      {
      }
      if ((i == -1) && (debug))
      {
        byte[] arrayOfByte = new byte[paramInt3];
        System.arraycopy(paramArrayOfByte, paramInt2, arrayOfByte, 0, paramInt3);
        System.out.println(new StringBuilder().append("key=").append(new String(arrayOfByte)).append(' ').append(j).append(" not implemented in setConstant in ").append(this).toString());
        System.out.println(new StringBuilder().append("final public static int ").append(new String(arrayOfByte)).append('=').append(j).append(';').toString());
      }
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog(new StringBuilder().append("Exception: ").append(localException.getMessage()).toString());
    }
    return j;
  }

  public int getParameterConstant(int paramInt)
  {
    int i = -1;
    switch (paramInt)
    {
    case 2087749783:
      return this.colorspace;
    case 1147962727:
      return this.subtype;
    case 608780341:
      return this.type;
    }
    return i;
  }

  public int setConstant(int paramInt1, int paramInt2)
  {
    int i = paramInt2;
    switch (paramInt2)
    {
    case -1044665361:
      i = 373243460;
    }
    switch (paramInt1)
    {
    case 2087749783:
      this.colorspace = i;
      break;
    case 1147962727:
      this.subtype = i;
      break;
    case 608780341:
      this.type = i;
    }
    return i;
  }

  public float[] getFloatArray(int paramInt)
  {
    float[] arrayOfFloat = null;
    switch (paramInt)
    {
    case 1142050954:
      return deepCopy(this.ArtBox);
    case 303185736:
      return deepCopy(this.BBox);
    case 1179546749:
      return deepCopy(this.BleedBox);
    case 1076199815:
      return deepCopy(this.CropBox);
    case 859785322:
      return deepCopy(this.Decode);
    case 1026641277:
      return deepCopy(this.Domain);
    case 1145198201:
      return deepCopy(this.Matrix);
    case 1313305473:
      return deepCopy(this.MediaBox);
    case 826160983:
      return deepCopy(this.Range);
    case 1026982273:
      return deepCopy(this.TrimBox);
    }
    return deepCopy(arrayOfFloat);
  }

  public byte[][] getKeyArray(int paramInt)
  {
    switch (paramInt)
    {
    }
    return (byte[][])null;
  }

  public double[] getDoubleArray(int paramInt)
  {
    double[] arrayOfDouble = null;
    switch (paramInt)
    {
    }
    return deepCopy(arrayOfDouble);
  }

  public boolean[] getBooleanArray(int paramInt)
  {
    boolean[] arrayOfBoolean = null;
    switch (paramInt)
    {
    }
    return deepCopy(arrayOfBoolean);
  }

  public int[] getIntArray(int paramInt)
  {
    int[] arrayOfInt = null;
    switch (paramInt)
    {
    }
    return deepCopy(arrayOfInt);
  }

  public void setFloatArray(int paramInt, float[] paramArrayOfFloat)
  {
    switch (paramInt)
    {
    case 1142050954:
      this.ArtBox = paramArrayOfFloat;
      break;
    case 303185736:
      this.BBox = paramArrayOfFloat;
      break;
    case 1179546749:
      this.BleedBox = paramArrayOfFloat;
      break;
    case 1076199815:
      this.CropBox = paramArrayOfFloat;
      break;
    case 859785322:
      this.Decode = ignoreIdentity(paramArrayOfFloat);
      break;
    case 1026641277:
      this.Domain = paramArrayOfFloat;
      break;
    case 1145198201:
      this.Matrix = paramArrayOfFloat;
      break;
    case 1313305473:
      this.MediaBox = paramArrayOfFloat;
      break;
    case 826160983:
      this.Range = paramArrayOfFloat;
      break;
    case 1026982273:
      this.TrimBox = paramArrayOfFloat;
      break;
    }
  }

  private static float[] ignoreIdentity(float[] paramArrayOfFloat)
  {
    int i = 1;
    if (paramArrayOfFloat != null)
    {
      int j = paramArrayOfFloat.length;
      int k = 0;
      while (k < j)
      {
        if ((paramArrayOfFloat[k] != 0.0F) || (paramArrayOfFloat[(k + 1)] != 1.0F))
        {
          i = 0;
          k = j;
        }
        k += 2;
      }
    }
    if (i != 0)
      return null;
    return paramArrayOfFloat;
  }

  public void setIntArray(int paramInt, int[] paramArrayOfInt)
  {
    switch (paramInt)
    {
    }
  }

  public void setBooleanArray(int paramInt, boolean[] paramArrayOfBoolean)
  {
    switch (paramInt)
    {
    }
  }

  public void setDoubleArray(int paramInt, double[] paramArrayOfDouble)
  {
    switch (paramInt)
    {
    }
  }

  public void setMixedArray(int paramInt, byte[][] paramArrayOfByte)
  {
    switch (paramInt)
    {
    case 1011108731:
      this.Filter = paramArrayOfByte;
      break;
    }
  }

  public String getStringValue(int paramInt1, int paramInt2)
  {
    byte[] arrayOfByte1 = null;
    switch (paramInt1)
    {
    case 506543413:
      arrayOfByte1 = this.rawName;
    }
    switch (paramInt2)
    {
    case 0:
      if (arrayOfByte1 != null)
        return new String(arrayOfByte1);
      return null;
    case 1:
      if (arrayOfByte1 != null)
        return new String(arrayOfByte1);
      return null;
    case 2:
      if (arrayOfByte1 != null)
      {
        int i = arrayOfByte1.length;
        if ((i > 6) && (arrayOfByte1[6] == 43))
        {
          int j = i - 7;
          byte[] arrayOfByte2 = new byte[j];
          System.arraycopy(arrayOfByte1, 7, arrayOfByte2, 0, j);
          return new String(arrayOfByte2);
        }
        return new String(arrayOfByte1);
      }
      return null;
    }
    throw new RuntimeException("Value not defined in getName(int,mode)");
  }

  public int getNameAsConstant(int paramInt)
  {
    return -1;
  }

  public String getName(int paramInt)
  {
    String str = null;
    switch (paramInt)
    {
    case 506543413:
      if ((this.Name == null) && (this.rawName != null))
        this.Name = new String(this.rawName);
      return this.Name;
    case 35:
      if ((this.S == null) && (this.rawS != null))
        this.S = new String(this.rawS);
      return this.S;
    }
    return str;
  }

  public String getStringKey(int paramInt)
  {
    String str = null;
    switch (paramInt)
    {
    case 1110793845:
      if ((this.Parent == null) && (this.rawParent != null))
        this.Parent = new String(this.rawParent);
      return this.Parent;
    }
    return str;
  }

  public String getTextStreamValue(int paramInt)
  {
    String str = null;
    switch (paramInt)
    {
    case 827818359:
      if ((this.Creator == null) && (this.rawCreator != null))
        this.Creator = StringUtils.getTextString(this.rawCreator, false);
      return this.Creator;
    case 506543413:
      if ((this.Name == null) && (this.rawName != null))
        this.Name = StringUtils.getTextString(this.rawName, false);
      return this.Name;
    case 960773209:
      if ((this.Title == null) && (this.rawTitle != null))
        this.Title = StringUtils.getTextString(this.rawTitle, false);
      return this.Title;
    }
    return str;
  }

  public void setName(int paramInt, byte[] paramArrayOfByte)
  {
    switch (paramInt)
    {
    case 506543413:
      this.rawName = paramArrayOfByte;
      break;
    case 35:
      this.rawS = paramArrayOfByte;
      break;
    case 1110793845:
      if (this.includeParent)
        this.rawParent = paramArrayOfByte;
      break;
    default:
      if (this.objType == 487790868)
        this.otherValues.put(this.currentKey, paramArrayOfByte);
      break;
    }
  }

  public void setName(Object paramObject, String paramString)
  {
    this.otherValues.put(paramObject, paramString);
  }

  public void setStringKey(int paramInt, byte[] paramArrayOfByte)
  {
    switch (paramInt)
    {
    case 1110793845:
      this.rawParent = paramArrayOfByte;
      break;
    }
  }

  public void setTextStreamValue(int paramInt, byte[] paramArrayOfByte)
  {
    switch (paramInt)
    {
    case 827818359:
      this.rawCreator = paramArrayOfByte;
      break;
    case 506543413:
      this.rawName = paramArrayOfByte;
      break;
    case 960773209:
      this.rawTitle = paramArrayOfByte;
      break;
    }
  }

  public byte[] getDecodedStream()
  {
    if (isCached())
    {
      byte[] arrayOfByte = null;
      try
      {
        File localFile = new File(getCachedStreamFile(this.objReader));
        BufferedInputStream localBufferedInputStream = new BufferedInputStream(new FileInputStream(localFile));
        arrayOfByte = new byte[(int)localFile.length()];
        localBufferedInputStream.read(arrayOfByte);
        localBufferedInputStream.close();
      }
      catch (Exception localException)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog(new StringBuilder().append("Exception: ").append(localException.getMessage()).toString());
      }
      return arrayOfByte;
    }
    return this.DecodedStream;
  }

  public void setStream(byte[] paramArrayOfByte)
  {
    this.stream = paramArrayOfByte;
    if (getObjectType() == 2087749783)
      this.hasStream = true;
  }

  public void setDecodedStream(byte[] paramArrayOfByte)
  {
    this.DecodedStream = paramArrayOfByte;
  }

  public String getObjectRefAsString()
  {
    if (this.ref == null)
      this.ref = new StringBuilder().append(this.intRef).append(" ").append(this.gen).append(" R").toString();
    return this.ref;
  }

  public int getObjectRefID()
  {
    if ((this.intRef == 0) && (this.ref != null) && (!this.ref.contains("[")))
    {
      int i = this.ref.indexOf(32);
      if (i > 0)
      {
        try
        {
          this.intRef = Integer.parseInt(this.ref.substring(0, i));
        }
        catch (Exception localException1)
        {
          if (LogWriter.isOutput())
            LogWriter.writeLog(new StringBuilder().append("Exception: ").append(localException1.getMessage()).toString());
        }
        int j = this.ref.indexOf(82, i);
        if (j > 0)
          try
          {
            this.gen = Integer.parseInt(this.ref.substring(i + 1, j - 1));
          }
          catch (Exception localException2)
          {
            if (LogWriter.isOutput())
              LogWriter.writeLog(new StringBuilder().append("Exception: ").append(localException2.getMessage()).toString());
          }
      }
    }
    return this.intRef;
  }

  public int getObjectRefGeneration()
  {
    return this.gen;
  }

  public PdfArrayIterator getMixedArray(int paramInt)
  {
    switch (paramInt)
    {
    case 1011108731:
      return new PdfArrayIterator(this.Filter);
    }
    return null;
  }

  public void setDictionaryPairs(byte[][] paramArrayOfByte1, byte[][] paramArrayOfByte2, PdfObject[] paramArrayOfPdfObject)
  {
    this.keys = paramArrayOfByte1;
    this.values = paramArrayOfByte2;
    this.objs = paramArrayOfPdfObject;
  }

  public PdfKeyPairsIterator getKeyPairsIterator()
  {
    return new PdfKeyPairsIterator(this.keys, this.values, this.objs);
  }

  public void setKeyArray(int paramInt, byte[][] paramArrayOfByte)
  {
    switch (paramInt)
    {
    }
  }

  public void setStringArray(int paramInt, byte[][] paramArrayOfByte)
  {
    switch (paramInt)
    {
    case 9250:
      this.TR = paramArrayOfByte;
      break;
    }
  }

  public byte[][] getStringArray(int paramInt)
  {
    switch (paramInt)
    {
    case 9250:
      return deepCopy(this.TR);
    }
    return (byte[][])null;
  }

  public Object[] getObjectArray(int paramInt)
  {
    switch (paramInt)
    {
    case 1888135062:
      return this.DecodeParmsAsArray;
    }
    return null;
  }

  public void setObjectArray(int paramInt, Object[] paramArrayOfObject)
  {
    switch (paramInt)
    {
    case 1888135062:
      this.DecodeParmsAsArray = paramArrayOfObject;
      break;
    }
  }

  public PdfObject duplicate()
  {
    PdfObject localPdfObject = new PdfObject();
    return localPdfObject;
  }

  public Object clone()
  {
    Object localObject = null;
    try
    {
      localObject = super.clone();
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog(new StringBuilder().append("Exception: ").append(localException.getMessage()).toString());
    }
    return localObject;
  }

  public boolean decompressStreamWhenRead()
  {
    return false;
  }

  public int getObjectType()
  {
    return this.objType;
  }

  public byte[] getStringValueAsByte(int paramInt)
  {
    return null;
  }

  public boolean isCompressedStream()
  {
    return this.isCompressedStream;
  }

  public void setCompressedStream(boolean paramBoolean)
  {
    this.isCompressedStream = paramBoolean;
  }

  public boolean ignoreRecursion()
  {
    return this.ignoreRecursion;
  }

  public void ignoreRecursion(boolean paramBoolean)
  {
    this.ignoreRecursion = paramBoolean;
  }

  public byte[] getTextStreamValueAsByte(int paramInt)
  {
    return null;
  }

  public byte[][] getByteArray(int paramInt)
  {
    return (byte[][])null;
  }

  public void setTextStreamValue(int paramInt, String paramString)
  {
  }

  public Map getOtherDictionaries()
  {
    return this.otherValues;
  }

  public void setCurrentKey(Object paramObject)
  {
    this.currentKey = paramObject;
  }

  public String toString(float[] paramArrayOfFloat, boolean paramBoolean)
  {
    if (paramArrayOfFloat == null)
      return null;
    StringBuilder localStringBuilder = new StringBuilder();
    if (paramBoolean)
    {
      localStringBuilder.append(this.pageNumber);
      localStringBuilder.append(' ');
    }
    for (float f : paramArrayOfFloat)
    {
      localStringBuilder.append(f);
      localStringBuilder.append(' ');
    }
    return localStringBuilder.toString();
  }

  public int getPageNumber()
  {
    return this.pageNumber;
  }

  public void setPageNumber(int paramInt)
  {
    this.pageNumber = paramInt;
  }

  public void setPageNumber(Object paramObject)
  {
    if ((paramObject instanceof String))
      try
      {
        this.pageNumber = Integer.parseInt((String)paramObject);
      }
      catch (NumberFormatException localNumberFormatException)
      {
        this.pageNumber = 1;
      }
    else
      LogWriter.writeFormLog("{FormObject.setPageNumber} pagenumber being set to UNKNOWN type", false);
  }

  public void setCache(long paramLong, PdfFileReader paramPdfFileReader)
  {
    this.startStreamOnDisk = paramLong;
    this.objReader = paramPdfFileReader;
  }

  public boolean isCached()
  {
    return this.startStreamOnDisk != -1L;
  }

  public String getCachedStreamFile(PdfFileReader paramPdfFileReader)
  {
    File localFile = null;
    if (this.startStreamOnDisk != -1L)
      try
      {
        localFile = File.createTempFile("jpedal-", ".bin", new File(ObjectStore.temp_dir));
        localFile.deleteOnExit();
        int i = getInt(1043816557);
        if (i == -1)
          paramPdfFileReader.spoolStreamDataToDisk(localFile, this.startStreamOnDisk);
        else
          paramPdfFileReader.spoolStreamDataToDisk(localFile, this.startStreamOnDisk, i);
        this.cacheName = localFile.getAbsolutePath();
      }
      catch (Exception localException)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog(new StringBuilder().append("Exception: ").append(localException.getMessage()).toString());
      }
      finally
      {
        if (localFile != null)
          localFile.deleteOnExit();
      }
    if (getObjectType() != 979194486)
      paramPdfFileReader.readStream(this, true, true, false, getObjectType() == 1365674082, this.isCompressedStream, this.cacheName);
    return this.cacheName;
  }

  public void setInCompressedStream(boolean paramBoolean)
  {
    this.isInCompressedStream = paramBoolean;
  }

  public boolean isInCompressedStream()
  {
    return this.isInCompressedStream;
  }

  public void maybeIndirect(boolean paramBoolean)
  {
    this.maybeIndirect = paramBoolean;
  }

  public boolean maybeIndirect()
  {
    return this.maybeIndirect;
  }

  public boolean isFullyResolved()
  {
    return this.isFullyResolved;
  }

  public void setFullyResolved(boolean paramBoolean)
  {
    this.isFullyResolved = paramBoolean;
  }

  public boolean isDataExternal()
  {
    return this.isDataExternal;
  }

  public void isDataExternal(boolean paramBoolean)
  {
    this.isDataExternal = paramBoolean;
  }

  public boolean ignoreStream()
  {
    return this.ignoreStream;
  }

  public void ignoreStream(boolean paramBoolean)
  {
    this.ignoreStream = paramBoolean;
  }

  public void setStreamMayBeCorrupt(boolean paramBoolean)
  {
    this.streamMayBeCorrupt = paramBoolean;
  }

  public boolean streamMayBeCorrupt()
  {
    return this.streamMayBeCorrupt;
  }

  public void moveCacheValues(PdfObject paramPdfObject)
  {
    this.startStreamOnDisk = paramPdfObject.startStreamOnDisk;
    paramPdfObject.startStreamOnDisk = -1L;
    this.cacheName = paramPdfObject.cacheName;
    paramPdfObject.cacheName = null;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.raw.PdfObject
 * JD-Core Version:    0.6.2
 */