package org.jpedal.objects.structuredtext;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.jpedal.objects.raw.PdfObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class StructuredContentHandler
{
  private boolean addCoordinates = false;
  private Map markedContentProperties;
  private int markedContentLevel = 0;
  private StringBuffer markedContentSequence;
  private static final boolean debug = false;
  private boolean contentExtracted = false;
  private String currentKey;
  private Map keys;
  private Map values;
  private Map dictionaries;
  boolean buildDirectly = false;
  Document doc;
  Element root;
  private float x1;
  private float y1;
  private float x2;
  private float y2;

  public StructuredContentHandler(Object paramObject)
  {
    if ((paramObject instanceof Map))
    {
      this.buildDirectly = false;
      this.values = ((Map)paramObject);
    }
    else
    {
      this.buildDirectly = true;
      this.doc = ((Document)paramObject);
      this.root = this.doc.createElement("TaggedPDF-doc");
      this.doc.appendChild(this.root);
    }
    this.markedContentProperties = new HashMap();
    this.markedContentLevel = 0;
    this.markedContentSequence = new StringBuffer();
    this.currentKey = "";
    this.keys = new HashMap();
    this.dictionaries = new HashMap();
  }

  public void MP()
  {
  }

  public void DP(PdfObject paramPdfObject)
  {
  }

  public void BDC(PdfObject paramPdfObject)
  {
    if (this.markedContentLevel == 0)
      this.markedContentSequence = new StringBuffer();
    this.markedContentLevel += 1;
    if (this.buildDirectly)
      paramPdfObject.setIntNumber(487790868, -1);
    int i = paramPdfObject.getInt(487790868);
    if (i != -1)
      this.keys.put(Integer.valueOf(this.markedContentLevel), String.valueOf(i));
    this.dictionaries.put(String.valueOf(this.markedContentLevel), paramPdfObject);
  }

  public void BMC(String paramString)
  {
    if (paramString.startsWith("/"))
      paramString = paramString.substring(1);
    if (this.markedContentLevel == 0)
      this.markedContentSequence = new StringBuffer();
    this.markedContentProperties.put(Integer.valueOf(this.markedContentLevel), paramString);
    this.markedContentLevel += 1;
    this.keys.put(Integer.valueOf(this.markedContentLevel), paramString);
    if ((this.buildDirectly) && (paramString != null))
    {
      Element localElement = (Element)this.root.getElementsByTagName(paramString).item(0);
      if (localElement == null)
      {
        localElement = this.doc.createElement(paramString);
        this.root.appendChild(localElement);
      }
      this.root = localElement;
    }
  }

  public void EMC()
  {
    this.contentExtracted = true;
    this.currentKey = ((String)this.keys.get(Integer.valueOf(this.markedContentLevel)));
    if (this.currentKey == null)
      this.currentKey = String.valueOf(this.markedContentLevel);
    Object localObject1;
    if (this.buildDirectly)
    {
      localObject1 = (PdfObject)this.dictionaries.get(this.currentKey);
      int i = localObject1 == null ? 1 : 0;
      Object localObject2;
      Object localObject3;
      Object localObject4;
      if (localObject1 != null)
      {
        localObject2 = ((PdfObject)localObject1).getOtherDictionaries();
        if (localObject2 != null)
        {
          localObject3 = ((Map)localObject2).keySet().iterator();
          while (((Iterator)localObject3).hasNext())
          {
            localObject4 = ((Iterator)localObject3).next();
            this.root.setAttribute(((Object)localObject4).toString(), ((Map)localObject2).get(localObject4).toString());
            this.root.setAttribute("x1", String.valueOf((int)this.x1));
            this.root.setAttribute("y1", String.valueOf((int)this.y1));
            this.root.setAttribute("x2", String.valueOf((int)this.x2));
            this.root.setAttribute("y2", String.valueOf((int)this.y2));
          }
        }
      }
      if (i != 0)
      {
        if (this.currentKey != null)
        {
          localObject2 = this.doc.createTextNode(stripEscapeChars(this.markedContentSequence.toString()));
          this.root.appendChild((Node)localObject2);
          if (this.addCoordinates)
          {
            this.root.setAttribute("x1", String.valueOf((int)this.x1));
            this.root.setAttribute("y1", String.valueOf((int)this.y1));
            this.root.setAttribute("x2", String.valueOf((int)this.x2));
            this.root.setAttribute("y2", String.valueOf((int)this.y2));
          }
          localObject3 = this.root.getParentNode();
          if ((localObject3 instanceof Element))
            this.root = ((Element)localObject3);
        }
      }
      else
      {
        localObject2 = "p";
        if (localObject2 == null)
          localObject2 = "p";
        localObject3 = this.doc.createElement((String)localObject2);
        this.root.appendChild((Node)localObject3);
        if (this.addCoordinates)
        {
          ((Element)localObject3).setAttribute("x1", String.valueOf((int)this.x1));
          ((Element)localObject3).setAttribute("y1", String.valueOf((int)this.y1));
          ((Element)localObject3).setAttribute("x2", String.valueOf((int)this.x2));
          ((Element)localObject3).setAttribute("y2", String.valueOf((int)this.y2));
        }
        localObject4 = this.doc.createTextNode(this.markedContentSequence.toString());
        ((Element)localObject3).appendChild((Node)localObject4);
      }
      this.markedContentSequence = new StringBuffer();
    }
    else
    {
      localObject1 = this.markedContentSequence.toString();
      PdfObject localPdfObject = (PdfObject)this.dictionaries.get(String.valueOf(this.markedContentLevel));
      int j = -1;
      if (localPdfObject != null)
        j = localPdfObject.getInt(487790868);
      if (j != -1)
      {
        this.values.put(String.valueOf(j), localObject1);
        this.markedContentSequence = new StringBuffer();
      }
      this.dictionaries.remove(String.valueOf(this.markedContentLevel));
    }
    if (this.markedContentLevel > 0)
      this.markedContentLevel -= 1;
  }

  public void setText(StringBuffer paramStringBuffer, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    if (this.markedContentSequence.length() == 0)
    {
      this.markedContentSequence = paramStringBuffer;
      if ((this.markedContentSequence.length() > 0) && (this.markedContentSequence.charAt(0) == ' '))
        this.markedContentSequence.deleteCharAt(0);
    }
    else
    {
      int i = 32;
      int j = 32;
      if (paramStringBuffer.length() > 0)
        i = paramStringBuffer.charAt(0);
      int k = this.markedContentSequence.length() - 1;
      if (k > 0)
        j = this.markedContentSequence.charAt(k);
      if ((j != 45) && (i != 45) && (i != 46))
        this.markedContentSequence.append(' ');
      this.markedContentSequence.append(paramStringBuffer);
    }
    this.x1 = paramFloat1;
    this.y1 = paramFloat2;
    this.x2 = paramFloat3;
    this.y2 = paramFloat4;
  }

  private static String stripEscapeChars(Object paramObject)
  {
    int j = 32;
    StringBuilder localStringBuilder = new StringBuilder((String)paramObject);
    int k = localStringBuilder.length();
    for (int m = 0; m < k; m++)
    {
      int i = localStringBuilder.charAt(m);
      if ((i == 92) && (j != 92))
      {
        localStringBuilder.deleteCharAt(m);
        k--;
      }
      j = i;
    }
    return localStringBuilder.toString();
  }

  public boolean hasContent()
  {
    return this.contentExtracted;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.structuredtext.StructuredContentHandler
 * JD-Core Version:    0.6.2
 */