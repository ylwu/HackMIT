package org.jpedal.objects.layers;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import org.jpedal.io.PdfObjectReader;
import org.jpedal.objects.raw.OCObject;
import org.jpedal.objects.raw.PdfKeyPairsIterator;
import org.jpedal.objects.raw.PdfObject;

public class PdfLayerList
{
  private static boolean debug = false;
  private int OCpageNumber = -1;
  private String padding = "";
  public static String deliminator = "èøø";
  private Map layerNames = new LinkedHashMap();
  private Map streamToName = new HashMap();
  private Map layersEnabled = new HashMap();
  private Map jsCommands = null;
  private Map metaData = new HashMap();
  private Map layersTested = new HashMap();
  private Map layerLocks = new HashMap();
  private boolean changesMade = false;
  private Map propertyMap;
  private Map refToPropertyID;
  private Map refTolayerName;
  private Map RBconstraints;
  private Map minScale = new HashMap();
  private Map maxScale = new HashMap();
  private int layerCount = 0;
  private Object[] order;
  private PdfObjectReader currentPdfFile = null;
  private Layer[] layers = null;

  public void init(PdfObject paramPdfObject1, PdfObject paramPdfObject2, PdfObjectReader paramPdfObjectReader, int paramInt)
  {
    this.OCpageNumber = paramInt;
    this.propertyMap = new HashMap();
    this.refToPropertyID = new HashMap();
    this.refTolayerName = new HashMap();
    this.RBconstraints = new HashMap();
    this.currentPdfFile = paramPdfObjectReader;
    if (paramPdfObject2 != null)
      setupOCMaps(paramPdfObject2, paramPdfObjectReader);
    PdfObject localPdfObject = paramPdfObject1.getDictionary(20);
    if (localPdfObject == null)
      return;
    int i = localPdfObject.getNameAsConstant(1970567530);
    if (i == -1)
      i = 7966;
    this.order = localPdfObject.getObjectArray(1110717793);
    if (debug)
    {
      System.out.println("PropertiesObj=" + paramPdfObject2);
      System.out.println("layerDict=" + localPdfObject);
      System.out.println("propertyMap=" + this.propertyMap);
      System.out.println("propertyMap=" + this.propertyMap);
      System.out.println("refToPropertyID=" + this.refToPropertyID);
      System.out.println("refTolayerName=" + this.refTolayerName);
      System.out.println("OCBaseState=" + i + " (ON=" + 7966 + ')');
      System.out.println("order=" + this.order);
      showValues("ON=", 7966, localPdfObject);
      showValues("OFF=", 2037270, localPdfObject);
      showValues("RBGroups=", 1633113989, localPdfObject);
    }
    addLayer(i, this.order, null);
    if (i != 7966)
      addLayer(7966, localPdfObject.getKeyArray(7966), null);
    if (i != 2037270)
      addLayer(2037270, localPdfObject.getKeyArray(2037270), null);
    if (i == 7966)
    {
      localObject1 = this.refToPropertyID.keySet().iterator();
      while (((Iterator)localObject1).hasNext())
      {
        localObject2 = ((Iterator)localObject1).next();
        Object localObject3 = this.refToPropertyID.get(localObject2);
        this.refTolayerName.put(localObject2, localObject3);
        if (!this.layersTested.containsKey(localObject3))
        {
          this.layersTested.put(localObject3, "x");
          this.layersEnabled.put(localObject3, "x");
        }
      }
    }
    setLocks(paramPdfObjectReader, localPdfObject.getKeyArray(859525491));
    setConstraints(localPdfObject.getKeyArray(1633113989));
    setAS(localPdfObject.getKeyArray(4387), paramPdfObjectReader);
    Object localObject1 = { 506543413, 827818359 };
    Object localObject2 = { "Name", "Creator" };
    int j = localObject1.length;
    for (int k = 0; k < j; k++)
    {
      str = localPdfObject.getTextStreamValue(localObject1[k]);
      if (str != null)
        this.metaData.put(localObject2[k], str);
    }
    String str = localPdfObject.getName(964196217);
    if (str != null)
      this.metaData.put("ListMode", str);
  }

  private static void showValues(String paramString, int paramInt, PdfObject paramPdfObject)
  {
    byte[][] arrayOfByte1 = paramPdfObject.getKeyArray(paramInt);
    if (arrayOfByte1 != null)
    {
      String str = "";
      for (byte[] arrayOfByte : arrayOfByte1)
        if (arrayOfByte == null)
          str = str + "null ";
        else
          str = str + new String(arrayOfByte) + ' ';
      System.out.println(paramString + str);
    }
  }

  public void setChangesMade(boolean paramBoolean)
  {
    this.changesMade = paramBoolean;
  }

  private void setConstraints(byte[][] paramArrayOfByte)
  {
    if (paramArrayOfByte == null)
      return;
    int i = paramArrayOfByte.length;
    String[] arrayOfString = new String[i];
    String str;
    for (int j = 0; j < i; j++)
    {
      str = new String(paramArrayOfByte[j]);
      arrayOfString[j] = ((String)this.refTolayerName.get(str));
    }
    for (j = 0; j < i; j++)
      if (isLayerName(arrayOfString[j]))
      {
        str = "";
        for (int k = 0; k < i; k++)
          if (j != k)
            str = str + arrayOfString[k] + ',';
        this.RBconstraints.put(arrayOfString[j], str);
      }
  }

  private void setupOCMaps(PdfObject paramPdfObject, PdfObjectReader paramPdfObjectReader)
  {
    PdfKeyPairsIterator localPdfKeyPairsIterator = paramPdfObject.getKeyPairsIterator();
    while (localPdfKeyPairsIterator.hasMorePairs())
    {
      String str1 = localPdfKeyPairsIterator.getNextKeyAsString();
      PdfObject localPdfObject = localPdfKeyPairsIterator.getNextValueAsDictionary();
      String str2 = localPdfObject.getObjectRefAsString();
      paramPdfObjectReader.checkResolved(localPdfObject);
      byte[][] arrayOfByte = localPdfObject.getKeyArray(521344835);
      if (arrayOfByte != null)
      {
        setupchildOCMaps(arrayOfByte, str1, paramPdfObjectReader);
      }
      else
      {
        this.propertyMap.put(str2, localPdfObject);
        String str3 = (String)this.refToPropertyID.get(str2);
        if (str3 == null)
          this.refToPropertyID.put(str2, str1);
        else
          this.refToPropertyID.put(str2, str3 + ',' + str1);
      }
      localPdfKeyPairsIterator.nextPair();
    }
  }

  private void setupchildOCMaps(byte[][] paramArrayOfByte, String paramString, PdfObjectReader paramPdfObjectReader)
  {
    for (byte[] arrayOfByte : paramArrayOfByte)
    {
      String str1 = new String(arrayOfByte);
      OCObject localOCObject = new OCObject(str1);
      paramPdfObjectReader.readObject(localOCObject);
      paramPdfObjectReader.checkResolved(localOCObject);
      byte[][] arrayOfByte2 = localOCObject.getKeyArray(521344835);
      if (arrayOfByte2 != null)
      {
        setupchildOCMaps(arrayOfByte2, paramString, paramPdfObjectReader);
      }
      else
      {
        this.propertyMap.put(str1, localOCObject);
        String str2 = (String)this.refToPropertyID.get(str1);
        if (str2 == null)
          this.refToPropertyID.put(str1, paramString);
        else
          this.refToPropertyID.put(str1, str2 + ',' + paramString);
      }
    }
  }

  private void addLayer(int paramInt, Object[] paramArrayOfObject, String paramString)
  {
    if (paramArrayOfObject == null)
      return;
    if (debug)
      this.padding += "   ";
    int i = paramArrayOfObject.length;
    String str3 = null;
    for (int j = 0; j < i; j++)
      if (!(paramArrayOfObject[j] instanceof String))
        if ((paramArrayOfObject[j] instanceof byte[]))
        {
          byte[] arrayOfByte = (byte[])paramArrayOfObject[j];
          String str1 = new String(arrayOfByte);
          String str2 = (String)this.refToPropertyID.get(str1);
          Object localObject1 = (PdfObject)this.propertyMap.get(str1);
          if (localObject1 == null)
            if ((arrayOfByte != null) && (arrayOfByte[(arrayOfByte.length - 1)] == 82))
            {
              localObject1 = new OCObject(str1);
              this.currentPdfFile.readObject((PdfObject)localObject1);
              str2 = str1;
            }
            else if (paramString == null)
            {
              paramString = str1;
            }
            else
            {
              paramString = str1 + deliminator + paramString;
            }
          if (localObject1 != null)
          {
            this.layerCount += 1;
            str3 = ((PdfObject)localObject1).getTextStreamValue(506543413);
            if (paramString != null)
              str3 = str3 + deliminator + paramString;
            if (debug)
              System.out.println(this.padding + "[layer1] add layer=" + str3 + " ref=" + str1 + " parent=" + paramString + " refToLayerName=" + this.refTolayerName.get(str1) + " ref=" + str1);
            this.refTolayerName.put(str1, str3);
            paramArrayOfObject[j] = str3;
            this.layerNames.put(str3, Integer.valueOf(paramInt));
            Object localObject2;
            if (str2.indexOf(44) == -1)
            {
              localObject2 = (String)this.streamToName.get(str2);
              if (localObject2 == null)
                this.streamToName.put(str2, str3);
              else
                this.streamToName.put(str2, (String)localObject2 + ',' + str3);
            }
            else
            {
              localObject2 = new StringTokenizer(str2, ",");
              while (((StringTokenizer)localObject2).hasMoreTokens())
              {
                str2 = ((StringTokenizer)localObject2).nextToken();
                String str4 = (String)this.streamToName.get(str2);
                if (str4 == null)
                  this.streamToName.put(str2, str3);
                else
                  this.streamToName.put(str2, str4 + ',' + str3);
              }
            }
            if (paramInt == 7966)
              this.layersEnabled.put(str3, "x");
            else
              this.layersEnabled.remove(str3);
          }
        }
        else
        {
          addLayer(paramInt, (Object[])paramArrayOfObject[j], str3);
        }
    if (debug)
    {
      j = this.padding.length();
      if (j > 3)
        this.padding = this.padding.substring(0, j - 3);
    }
  }

  private void addLayer(int paramInt, byte[][] paramArrayOfByte, String paramString)
  {
    if (paramArrayOfByte == null)
      return;
    for (byte[] arrayOfByte1 : paramArrayOfByte)
    {
      String str1 = new String(arrayOfByte1);
      String str2 = (String)this.refToPropertyID.get(str1);
      PdfObject localPdfObject = (PdfObject)this.propertyMap.get(str1);
      if (localPdfObject != null)
      {
        this.layerCount += 1;
        Object localObject1 = localPdfObject.getTextStreamValue(506543413);
        if (paramString != null)
          localObject1 = (String)localObject1 + deliminator + paramString;
        Object localObject2;
        if ((paramInt == 7966) || (paramInt == 2037270))
        {
          localObject2 = (String)this.refTolayerName.get(str1);
          if (localObject2 != null)
            localObject1 = localObject2;
        }
        if (debug)
          System.out.println(this.padding + "[layer0] add layer=" + (String)localObject1 + " ref=" + str1 + " parent=" + paramString + " refToLayerName=" + this.refTolayerName.get(str1) + " status=" + paramInt);
        if (this.refTolayerName.get(str1) == null)
        {
          this.refTolayerName.put(str1, localObject1);
          this.layerNames.put(localObject1, Integer.valueOf(paramInt));
        }
        if (this.streamToName.get(str2) == null)
          if (str2.indexOf(44) == -1)
          {
            localObject2 = (String)this.streamToName.get(str2);
            if (localObject2 == null)
              this.streamToName.put(str2, localObject1);
            else
              this.streamToName.put(str2, (String)localObject2 + ',' + (String)localObject1);
          }
          else
          {
            localObject2 = new StringTokenizer(str2, ",");
            while (((StringTokenizer)localObject2).hasMoreTokens())
            {
              str2 = ((StringTokenizer)localObject2).nextToken();
              String str3 = (String)this.streamToName.get(str2);
              if (str3 == null)
                this.streamToName.put(str2, localObject1);
              else
                this.streamToName.put(str2, str3 + ',' + (String)localObject1);
            }
          }
        if (paramInt == 7966)
          this.layersEnabled.put(localObject1, "x");
        else
          this.layersEnabled.remove(localObject1);
        this.layersTested.put(localObject1, "x");
      }
    }
  }

  private void setAS(byte[][] paramArrayOfByte, PdfObjectReader paramPdfObjectReader)
  {
    if (paramArrayOfByte == null)
      return;
    for (byte[] arrayOfByte3 : paramArrayOfByte)
      if (arrayOfByte3 != null)
      {
        String str1 = new String(arrayOfByte3);
        OCObject localOCObject = new OCObject(str1);
        if (arrayOfByte3[0] == 60)
          localOCObject.setStatus(2);
        else
          localOCObject.setStatus(1);
        localOCObject.setUnresolvedData(arrayOfByte3, 4387);
        paramPdfObjectReader.checkResolved(localOCObject);
        int i = localOCObject.getParameterConstant(1177894489);
        if ((localOCObject != null) && (i == 641283399))
        {
          byte[][] arrayOfByte1 = localOCObject.getKeyArray(521344835);
          if (arrayOfByte1 != null)
            for (byte[] arrayOfByte5 : arrayOfByte1)
            {
              str1 = new String(arrayOfByte5);
              localOCObject = new OCObject(str1);
              if (arrayOfByte5[0] == 60)
                localOCObject.setStatus(2);
              else
                localOCObject.setStatus(1);
              localOCObject.setUnresolvedData(arrayOfByte5, 521344835);
              paramPdfObjectReader.checkResolved(localOCObject);
              String str3 = localOCObject.getTextStreamValue(506543413);
              String str2 = (String)this.refToPropertyID.get(str1);
              this.streamToName.put(str2, str3);
              PdfObject localPdfObject1 = localOCObject.getDictionary(1127298906);
              if (localPdfObject1 != null)
              {
                PdfObject localPdfObject2 = localPdfObject1.getDictionary(708788029);
                if (localPdfObject2 != null)
                {
                  float f1 = localPdfObject2.getFloatNumber(4012350);
                  if (f1 != 0.0F)
                    this.minScale.put(str3, Float.valueOf(f1));
                  float f2 = localPdfObject2.getFloatNumber(4010312);
                  if (f2 != 0.0F)
                    this.maxScale.put(str3, Float.valueOf(f2));
                }
              }
            }
        }
      }
  }

  private void setLocks(PdfObjectReader paramPdfObjectReader, byte[][] paramArrayOfByte)
  {
    if (paramArrayOfByte == null)
      return;
    for (byte[] arrayOfByte1 : paramArrayOfByte)
    {
      String str1 = new String(arrayOfByte1);
      OCObject localOCObject = new OCObject(str1);
      paramPdfObjectReader.readObject(localOCObject);
      String str2 = localOCObject.getTextStreamValue(506543413);
      this.layerLocks.put(str2, "x");
    }
  }

  public Map getMetaData()
  {
    return this.metaData;
  }

  public Object[] getDisplayTree()
  {
    if (this.order != null)
      return this.order;
    return getNames();
  }

  private String[] getNames()
  {
    int i = this.layerNames.size();
    String[] arrayOfString = new String[i];
    Iterator localIterator = this.layerNames.keySet().iterator();
    for (int j = 0; localIterator.hasNext(); j++)
      arrayOfString[j] = localIterator.next().toString();
    return arrayOfString;
  }

  public void setVisibleLayers(String[] paramArrayOfString)
  {
    this.layersEnabled.clear();
    if (paramArrayOfString != null)
      for (String str : paramArrayOfString)
        this.layersEnabled.put(str, "x");
    this.changesMade = true;
  }

  public boolean decodeLayer(String paramString, boolean paramBoolean)
  {
    if (this.layerCount == 0)
      return true;
    boolean bool = false;
    Object localObject1 = paramString;
    Object localObject2;
    if (paramBoolean)
    {
      localObject2 = (String)this.streamToName.get(paramString);
      if (localObject2 != null)
        localObject1 = localObject2;
    }
    if (localObject1 == null)
      return false;
    if (((String)localObject1).indexOf(44) == -1)
    {
      bool = this.layersEnabled.containsKey(localObject1);
      if (bool)
        bool = hiddenByParent(bool, (String)localObject1);
    }
    else
    {
      localObject2 = new StringTokenizer((String)localObject1, ",");
      while (((StringTokenizer)localObject2).hasMoreTokens())
      {
        String str = ((StringTokenizer)localObject2).nextToken();
        bool = this.layersEnabled.containsKey(str);
        if (bool)
          bool = hiddenByParent(bool, str);
        if (bool)
          break;
      }
    }
    if (debug)
      System.out.println("[isVisible] " + paramString + " decode=" + bool + " enabled=" + this.layersEnabled + " layerName=" + (String)localObject1 + " isEnabled=" + this.layersEnabled);
    return bool;
  }

  private boolean hiddenByParent(boolean paramBoolean, String paramString)
  {
    int i = paramString.indexOf(deliminator);
    if ((paramBoolean) && (i != -1))
    {
      String str = paramString.substring(i + 1, paramString.length());
      while ((str != null) && (paramBoolean) && (isLayerName(str)))
      {
        paramBoolean = decodeLayer(str, false);
        paramString = str;
        i = paramString.indexOf(deliminator);
        if (i == -1)
          str = null;
        else
          str = paramString.substring(i + 1, paramString.length());
      }
    }
    return paramBoolean;
  }

  public boolean setZoom(float paramFloat)
  {
    Iterator localIterator = this.minScale.keySet().iterator();
    String str;
    while (localIterator.hasNext())
    {
      str = (String)localIterator.next();
      localObject = (Float)this.minScale.get(str);
      if (localObject != null)
        if (paramFloat < ((Float)localObject).floatValue())
        {
          this.layersEnabled.remove(str);
          this.changesMade = true;
        }
        else if (!this.layersEnabled.containsKey(str))
        {
          this.layersEnabled.put(str, "x");
          this.changesMade = true;
        }
    }
    Object localObject = this.maxScale.keySet().iterator();
    while (((Iterator)localObject).hasNext())
    {
      str = (String)localIterator.next();
      Float localFloat = (Float)this.maxScale.get(str);
      if (localFloat != null)
        if (paramFloat > localFloat.floatValue())
        {
          this.layersEnabled.remove(str);
          this.changesMade = true;
        }
        else if (!this.layersEnabled.containsKey(str))
        {
          this.layersEnabled.put(str, "x");
          this.changesMade = true;
        }
    }
    return this.changesMade;
  }

  public boolean isVisible(String paramString)
  {
    return this.layersEnabled.containsKey(paramString);
  }

  public void setVisiblity(String paramString, boolean paramBoolean)
  {
    if (debug)
      System.out.println("[layer] setVisiblity=" + paramString + " isVisible=" + paramBoolean);
    if (paramBoolean)
    {
      this.layersEnabled.put(paramString, "x");
      String str = (String)this.RBconstraints.get(paramString);
      if (str != null)
      {
        StringTokenizer localStringTokenizer = new StringTokenizer(str, ",");
        while (localStringTokenizer.hasMoreTokens())
          this.layersEnabled.remove(localStringTokenizer.nextToken());
      }
    }
    else
    {
      this.layersEnabled.remove(paramString);
    }
    this.changesMade = true;
  }

  public boolean isVisible(PdfObject paramPdfObject)
  {
    boolean bool = true;
    PdfObject localPdfObject = paramPdfObject.getDictionary(7955);
    if (localPdfObject != null)
    {
      String str1 = null;
      byte[][] arrayOfByte1 = localPdfObject.getKeyArray(521344835);
      if (arrayOfByte1 != null)
        for (byte[] arrayOfByte : arrayOfByte1)
        {
          String str2 = new String(arrayOfByte);
          str1 = getNameFromRef(str2);
        }
      if (str1 == null)
        str1 = localPdfObject.getTextStreamValue(506543413);
      if ((str1 != null) && (isLayerName(str1)))
        bool = isVisible(str1);
    }
    return bool;
  }

  public boolean isLocked(String paramString)
  {
    return this.layerLocks.containsKey(paramString);
  }

  public boolean getChangesMade()
  {
    return this.changesMade;
  }

  public boolean isLayerName(String paramString)
  {
    return this.layerNames.containsKey(paramString);
  }

  public int getLayersCount()
  {
    return this.layerCount;
  }

  public String getNameFromRef(String paramString)
  {
    return (String)this.refTolayerName.get(paramString);
  }

  public Object[] getOCGs()
  {
    if (this.layers != null)
      return this.layers;
    int i = this.layerNames.size();
    Layer[] arrayOfLayer = new Layer[i];
    Iterator localIterator = this.layerNames.keySet().iterator();
    for (int j = 0; localIterator.hasNext(); j++)
    {
      String str = (String)localIterator.next();
      arrayOfLayer[j] = new Layer(str, this);
    }
    return arrayOfLayer;
  }

  public void addJScommand(String paramString1, String paramString2)
  {
    if (this.jsCommands == null)
      this.jsCommands = new HashMap();
    this.jsCommands.put(paramString1, paramString2);
  }

  public Iterator getJSCommands()
  {
    if (this.jsCommands != null)
    {
      Iterator localIterator = this.jsCommands.keySet().iterator();
      HashMap localHashMap = new HashMap();
      while (localIterator.hasNext())
      {
        String str = (String)localIterator.next();
        if (isVisible(str))
          localHashMap.put(this.jsCommands.get(str), "x");
      }
      return localHashMap.keySet().iterator();
    }
    return null;
  }

  public int getOCpageNumber()
  {
    return this.OCpageNumber;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.layers.PdfLayerList
 * JD-Core Version:    0.6.2
 */