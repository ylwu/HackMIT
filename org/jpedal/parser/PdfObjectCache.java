package org.jpedal.parser;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.jpedal.exception.PdfException;
import org.jpedal.objects.raw.ObjectFactory;
import org.jpedal.objects.raw.PdfKeyPairsIterator;
import org.jpedal.objects.raw.PdfObject;
import org.jpedal.objects.raw.XObject;
import org.jpedal.utils.StringUtils;

public class PdfObjectCache
{
  public static final int ColorspacesUsed = 1;
  public static final int Colorspaces = 2;
  private static final int initSize = 50;
  private final Map colorspacesUsed = new HashMap(50);
  public Map colorspacesObjects = new HashMap(50);
  HashMap imageAlreadySaved = new HashMap();
  private Map colorspaces = new HashMap(50);
  private Map globalXObjects = new HashMap(50);
  private Map localXObjects = new HashMap(50);
  public final Map XObjectColorspaces = new HashMap(50);
  public Map patterns = new HashMap(50);
  public Map globalShadings = new HashMap(50);
  public Map localShadings = new HashMap(50);
  Map imposedImages = new HashMap(50);
  PdfObject groupObj = null;
  PdfObject pageGroupingObj = null;
  public Map unresolvedFonts = new HashMap(50);
  public Map directFonts = new HashMap(50);
  public Map resolvedFonts = new HashMap(50);
  Map GraphicsStates = new HashMap(50);

  public PdfObjectCache copy()
  {
    PdfObjectCache localPdfObjectCache = new PdfObjectCache();
    localPdfObjectCache.localShadings = this.localShadings;
    localPdfObjectCache.unresolvedFonts = this.unresolvedFonts;
    localPdfObjectCache.GraphicsStates = this.GraphicsStates;
    localPdfObjectCache.directFonts = this.directFonts;
    localPdfObjectCache.resolvedFonts = this.resolvedFonts;
    localPdfObjectCache.colorspaces = this.colorspaces;
    localPdfObjectCache.localXObjects = this.localXObjects;
    localPdfObjectCache.globalXObjects = this.globalXObjects;
    localPdfObjectCache.groupObj = this.groupObj;
    return localPdfObjectCache;
  }

  public void put(int paramInt1, int paramInt2, Object paramObject)
  {
    switch (paramInt1)
    {
    case 1:
      this.colorspacesUsed.put(Integer.valueOf(paramInt2), paramObject);
    }
  }

  public Iterator iterator(int paramInt)
  {
    Iterator localIterator = null;
    switch (paramInt)
    {
    case 1:
      localIterator = this.colorspacesUsed.keySet().iterator();
    }
    return localIterator;
  }

  public Object get(int paramInt, Object paramObject)
  {
    Object localObject = null;
    switch (paramInt)
    {
    case 1:
      localObject = this.colorspacesUsed.get(paramObject);
      break;
    case 2:
      localObject = this.colorspaces.get(paramObject);
    }
    return localObject;
  }

  public void resetFonts()
  {
    this.resolvedFonts.clear();
    this.unresolvedFonts.clear();
    this.directFonts.clear();
  }

  public PdfObject getXObjects(String paramString)
  {
    PdfObject localPdfObject = (PdfObject)this.localXObjects.get(paramString);
    if (localPdfObject == null)
      localPdfObject = (PdfObject)this.globalXObjects.get(paramString);
    return localPdfObject;
  }

  public void resetXObject(String paramString1, String paramString2, byte[] paramArrayOfByte)
  {
    XObject localXObject = new XObject(paramString2);
    if (paramArrayOfByte[(paramArrayOfByte.length - 1)] == 82)
      localXObject.setStatus(1);
    else
      localXObject.setStatus(2);
    localXObject.setUnresolvedData(paramArrayOfByte, 540096309);
    if (this.localXObjects.containsKey(paramString1))
      this.localXObjects.remove(paramString1);
    else
      this.globalXObjects.remove(paramString1);
  }

  public void readResources(PdfObject paramPdfObject, boolean paramBoolean)
    throws PdfException
  {
    String[] arrayOfString = { "ColorSpace", "ExtGState", "Font", "Pattern", "Shading", "XObject" };
    int[] arrayOfInt = { 2087749783, -1938465939, 373243460, 1146450818, 878474856, 979194486 };
    for (int i = 0; i < arrayOfString.length; i++)
      if ((arrayOfInt[i] == 373243460) || (arrayOfInt[i] == 979194486))
        readArrayPairs(paramPdfObject, paramBoolean, arrayOfInt[i]);
      else
        readArrayPairs(paramPdfObject, false, arrayOfInt[i]);
  }

  private void readArrayPairs(PdfObject paramPdfObject, boolean paramBoolean, int paramInt)
    throws PdfException
  {
    if (paramPdfObject != null)
    {
      PdfObject localPdfObject1 = paramPdfObject.getDictionary(paramInt);
      if (localPdfObject1 != null)
      {
        PdfKeyPairsIterator localPdfKeyPairsIterator = localPdfObject1.getKeyPairsIterator();
        while (localPdfKeyPairsIterator.hasMorePairs())
        {
          String str1 = localPdfKeyPairsIterator.getNextKeyAsString();
          String str2 = localPdfKeyPairsIterator.getNextValueAsString();
          PdfObject localPdfObject2 = localPdfKeyPairsIterator.getNextValueAsDictionary();
          if (paramPdfObject.isDataExternal())
          {
            if ((localPdfObject2 == null) && (str2 == null))
            {
              paramPdfObject.setFullyResolved(false);
              return;
            }
            if (localPdfObject2 == null)
            {
              PdfObject localPdfObject3 = ObjectFactory.createObject(paramInt, str2, paramInt, -1);
              localPdfObject3.setStatus(2);
              localPdfObject3.setUnresolvedData(StringUtils.toBytes(str2), paramInt);
              if (paramInt == 373243460)
                this.directFonts.put(str1, localPdfObject3);
            }
          }
          switch (paramInt)
          {
          case 2087749783:
            this.colorspaces.put(str1, localPdfObject2);
            break;
          case -1938465939:
            this.GraphicsStates.put(str1, localPdfObject2);
            break;
          case 373243460:
            this.unresolvedFonts.put(str1, localPdfObject2);
            break;
          case 1146450818:
            this.patterns.put(str1, localPdfObject2);
            break;
          case 878474856:
            if (paramBoolean)
              this.globalShadings.put(str1, localPdfObject2);
            else
              this.localShadings.put(str1, localPdfObject2);
            break;
          case 979194486:
            if (paramBoolean)
              this.globalXObjects.put(str1, localPdfObject2);
            else
              this.localXObjects.put(str1, localPdfObject2);
            break;
          }
          localPdfKeyPairsIterator.nextPair();
        }
      }
    }
  }

  public void reset(PdfObjectCache paramPdfObjectCache)
  {
    this.localShadings = new HashMap(50);
    this.resolvedFonts = new HashMap(50);
    this.unresolvedFonts = new HashMap(50);
    this.directFonts = new HashMap(50);
    this.colorspaces = new HashMap(50);
    this.GraphicsStates = new HashMap(50);
    this.localXObjects = new HashMap(50);
    Iterator localIterator = paramPdfObjectCache.GraphicsStates.keySet().iterator();
    Object localObject;
    while (localIterator.hasNext())
    {
      localObject = localIterator.next();
      this.GraphicsStates.put(localObject, paramPdfObjectCache.GraphicsStates.get(localObject));
    }
    localIterator = paramPdfObjectCache.colorspaces.keySet().iterator();
    while (localIterator.hasNext())
    {
      localObject = localIterator.next();
      this.colorspaces.put(localObject, paramPdfObjectCache.colorspaces.get(localObject));
    }
    localIterator = paramPdfObjectCache.localXObjects.keySet().iterator();
    while (localIterator.hasNext())
    {
      localObject = localIterator.next();
      this.localXObjects.put(localObject, paramPdfObjectCache.localXObjects.get(localObject));
    }
    localIterator = paramPdfObjectCache.globalXObjects.keySet().iterator();
    while (localIterator.hasNext())
    {
      localObject = localIterator.next();
      this.globalXObjects.put(localObject, paramPdfObjectCache.globalXObjects.get(localObject));
    }
    if (this.unresolvedFonts.isEmpty())
    {
      localIterator = paramPdfObjectCache.unresolvedFonts.keySet().iterator();
      while (localIterator.hasNext())
      {
        localObject = localIterator.next();
        this.unresolvedFonts.put(localObject, paramPdfObjectCache.unresolvedFonts.get(localObject));
      }
    }
  }

  public void restore(PdfObjectCache paramPdfObjectCache)
  {
    this.directFonts = paramPdfObjectCache.directFonts;
    this.unresolvedFonts = paramPdfObjectCache.unresolvedFonts;
    this.resolvedFonts = paramPdfObjectCache.resolvedFonts;
    this.GraphicsStates = paramPdfObjectCache.GraphicsStates;
    this.colorspaces = paramPdfObjectCache.colorspaces;
    this.localShadings = paramPdfObjectCache.localShadings;
    this.localXObjects = paramPdfObjectCache.localXObjects;
    this.globalXObjects = paramPdfObjectCache.globalXObjects;
    this.groupObj = paramPdfObjectCache.groupObj;
  }

  public int getXObjectCount()
  {
    return this.localXObjects.keySet().size() + this.globalXObjects.keySet().size();
  }

  public void setImposedKey(String paramString, int paramInt)
  {
    if (this.imposedImages != null)
      this.imposedImages.put(paramString, Integer.valueOf(paramInt));
  }

  public boolean testIfImageAlreadySaved(PdfObject paramPdfObject)
  {
    boolean bool = false;
    if (paramPdfObject.getGeneralType(-1) != 6420)
    {
      String str = paramPdfObject.getObjectRefAsString();
      bool = this.imageAlreadySaved.containsKey(str);
      if (!bool)
        this.imageAlreadySaved.put(str, "x");
    }
    return bool;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.parser.PdfObjectCache
 * JD-Core Version:    0.6.2
 */