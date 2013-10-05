package org.jpedal.objects.acroforms;

import java.awt.Component;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jpedal.external.CustomFormPrint;
import org.jpedal.objects.PdfPageData;
import org.jpedal.objects.acroforms.creation.FormFactory;
import org.jpedal.objects.layers.PdfLayerList;
import org.jpedal.objects.raw.FormObject;

public class GUIData
{
  protected boolean rasterizeForms = false;
  protected boolean forceRedraw = false;
  protected int userX;
  protected int userY;
  protected int widestPageNR;
  protected int widestPageR;
  protected int displayView;
  float dpi = 72.0F;
  protected Map rawFormData = new HashMap();
  protected Map componentsToIgnore = new HashMap();
  protected int insetW;
  protected int insetH;
  protected PdfPageData pageData;
  protected int indent;
  protected int[] cropOtherY;
  protected float displayScaling;
  protected int rotation;
  protected float lastScaling = -1.0F;
  protected float oldRotation = 0.0F;
  protected float oldIndent = 0.0F;
  protected int startPage;
  protected int currentPage;
  protected int[] xReached;
  protected int[] yReached;
  protected List[] formsUnordered;
  protected List[] formsOrdered;
  private int formCount;
  protected PdfLayerList layers;
  protected FormFactory formFactory;

  public void setLayerData(PdfLayerList paramPdfLayerList)
  {
    this.layers = paramPdfLayerList;
  }

  public void setRasterizeForms(boolean paramBoolean)
  {
    this.rasterizeForms = paramBoolean;
  }

  protected void setListForPage(int paramInt, List paramList, boolean paramBoolean)
  {
    if (paramBoolean)
      this.formsOrdered[paramInt] = paramList;
    else
      this.formsUnordered[paramInt] = paramList;
  }

  protected Object checkGUIObjectResolved(FormObject paramFormObject)
  {
    Object localObject = null;
    if (paramFormObject != null)
      localObject = paramFormObject.getGUIComponent();
    if ((paramFormObject != null) && (localObject == null))
    {
      localObject = resolveGUIComponent(paramFormObject);
      if (localObject != null)
        setGUIComp(paramFormObject, (Component)localObject);
    }
    return localObject;
  }

  protected Object resolveGUIComponent(FormObject paramFormObject)
  {
    Object localObject = null;
    int i = paramFormObject.getParameterConstant(1147962727);
    boolean[] arrayOfBoolean = paramFormObject.getFieldFlags();
    int j;
    int k;
    if (i == 1197118)
    {
      j = 0;
      k = 0;
      if (arrayOfBoolean != null)
      {
        j = arrayOfBoolean[17];
        k = arrayOfBoolean[16];
      }
      if (j != 0)
        localObject = this.formFactory.pushBut(paramFormObject);
      else if (k != 0)
        localObject = this.formFactory.radioBut(paramFormObject);
      else
        localObject = this.formFactory.checkBoxBut(paramFormObject);
    }
    else if (i == 9288)
    {
      j = 0;
      k = 0;
      if (arrayOfBoolean != null)
      {
        j = arrayOfBoolean[13];
        k = arrayOfBoolean[14];
      }
      if (j != 0)
      {
        if (k != 0)
          localObject = this.formFactory.multiLinePassword(paramFormObject);
        else
          localObject = this.formFactory.multiLineText(paramFormObject);
      }
      else if (k != 0)
        localObject = this.formFactory.singleLinePassword(paramFormObject);
      else
        localObject = this.formFactory.singleLineText(paramFormObject);
    }
    else if (i == 4920)
    {
      j = 0;
      if (arrayOfBoolean != null)
        j = arrayOfBoolean[18];
      if (j != 0)
        localObject = this.formFactory.comboBox(paramFormObject);
      else
        localObject = this.formFactory.listField(paramFormObject);
    }
    else if (i == 2308407)
    {
      localObject = this.formFactory.signature(paramFormObject);
    }
    else
    {
      localObject = this.formFactory.annotationButton(paramFormObject);
    }
    if (localObject != null)
    {
      paramFormObject.setGUIComponent(localObject);
      setGUIComp(paramFormObject, localObject);
    }
    return localObject;
  }

  public void dispose()
  {
  }

  protected void displayComponent(FormObject paramFormObject, Object paramObject)
  {
    throw new RuntimeException("base method displayComponent( ) should not be called");
  }

  protected void displayComponents(int paramInt1, int paramInt2)
  {
    if ((this.rasterizeForms) || (this.formsOrdered == null) || (this.formsOrdered[paramInt1] == null))
      return;
    this.startPage = paramInt1;
    for (int i = paramInt1; i < paramInt2; i++)
      if (this.formsOrdered[i] != null)
      {
        Iterator localIterator = this.formsOrdered[i].iterator();
        while (localIterator.hasNext())
        {
          Object localObject2 = localIterator.next();
          if (localObject2 != null)
          {
            FormObject localFormObject = (FormObject)localObject2;
            Object localObject1 = checkGUIObjectResolved(localFormObject);
            if (localObject1 != null)
              displayComponent(localFormObject, localObject1);
          }
        }
      }
  }

  public boolean hasformsOnPageDecoded(int paramInt)
  {
    return (this.formsOrdered != null) && (this.formsOrdered.length > paramInt) && (this.formsOrdered[paramInt] != null);
  }

  protected void initParametersForPage(PdfPageData paramPdfPageData, int paramInt, FormFactory paramFormFactory, float paramFloat)
  {
    if ((this.cropOtherY == null) || (this.cropOtherY.length <= paramInt))
      resetComponents(0, paramInt + 1, false);
    int i = paramPdfPageData.getMediaBoxHeight(paramInt);
    int j = paramPdfPageData.getCropBoxHeight(paramInt) + paramPdfPageData.getCropBoxY(paramInt);
    if (i != j)
      this.cropOtherY[paramInt] = (i - j);
    else
      this.cropOtherY[paramInt] = 0;
    this.currentPage = paramInt;
    this.formFactory = paramFormFactory;
    this.dpi = paramFloat;
  }

  public void changeFormFactory(FormFactory paramFormFactory)
  {
    this.formFactory = paramFormFactory;
  }

  public void resetComponents(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    if ((paramBoolean) && (this.formCount > paramInt1))
      return;
    this.formCount = paramInt1;
    if (!paramBoolean)
    {
      this.formsUnordered = new List[paramInt2 + 1];
      this.formsOrdered = new List[paramInt2 + 1];
      this.cropOtherY = new int[paramInt2 + 1];
      this.xReached = (this.yReached = null);
    }
  }

  public void setPageValues(float paramFloat, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7)
  {
    this.rotation = paramInt1;
    this.displayScaling = paramFloat;
    this.indent = paramInt2;
    this.userX = paramInt3;
    this.userY = paramInt4;
    this.displayView = paramInt5;
    this.widestPageNR = paramInt6;
    this.widestPageR = paramInt7;
  }

  protected void setPageData(PdfPageData paramPdfPageData, int paramInt1, int paramInt2)
  {
    this.insetW = paramInt1;
    this.insetH = paramInt2;
    this.pageData = paramPdfPageData;
  }

  public void setPageDisplacements(int[] paramArrayOfInt1, int[] paramArrayOfInt2)
  {
    this.xReached = paramArrayOfInt1;
    this.yReached = paramArrayOfInt2;
    this.forceRedraw = true;
  }

  public void setForceRedraw(boolean paramBoolean)
  {
    this.forceRedraw = paramBoolean;
  }

  protected void storeRawData(FormObject paramFormObject)
  {
    String str = paramFormObject.getObjectRefAsString();
    this.rawFormData.put(str, paramFormObject);
  }

  protected void flushFormData()
  {
    this.rawFormData.clear();
    this.oldIndent = (-this.oldIndent);
  }

  protected Map getRawFormData()
  {
    return this.rawFormData;
  }

  protected void setGUIComp(FormObject paramFormObject, Object paramObject)
  {
    throw new RuntimeException("Should never be called");
  }

  public List getFormComponents(String paramString, ReturnValues paramReturnValues, int paramInt)
  {
    Iterator localIterator = this.rawFormData.keySet().iterator();
    ArrayList localArrayList = new ArrayList();
    while (localIterator.hasNext())
    {
      FormObject localFormObject = (FormObject)this.rawFormData.get(localIterator.next());
      int i = (paramInt == -1) || (localFormObject.getPageNumber() == paramInt) ? 1 : 0;
      String str = localFormObject.getTextStreamValue(36);
      switch (1.$SwitchMap$org$jpedal$objects$acroforms$ReturnValues[paramReturnValues.ordinal()])
      {
      case 1:
        if ((i != 0) && ((paramString == null) || ((str != null) && (str.equals(paramString)))))
          localArrayList.add(checkGUIObjectResolved(localFormObject));
        break;
      case 2:
        if ((i != 0) && ((paramString == null) || ((str != null) && (str.equals(paramString)))))
          localArrayList.add(localFormObject);
        break;
      case 3:
        if ((i != 0) && ((paramString == null) || (localFormObject.getObjectRefAsString().equals(paramString))))
          localArrayList.add(localFormObject);
        break;
      case 4:
        if ((i != 0) && (str != null) && (!str.isEmpty()) && (!localArrayList.contains(str)))
          localArrayList.add(str);
        break;
      default:
        throw new RuntimeException("value " + paramReturnValues + " not implemented");
      }
    }
    return localArrayList;
  }

  public void resetAfterPrinting()
  {
    this.forceRedraw = true;
  }

  public static int calculateFontSize(int paramInt1, int paramInt2, boolean paramBoolean, String paramString)
  {
    float f = 0.8F;
    double d1 = paramInt1 * f;
    if ((paramString == null) || (paramString.isEmpty()))
      return (int)d1;
    char[] arrayOfChar1 = paramString.toCharArray();
    int j = 0;
    int k = 0;
    int m = 1;
    int n = 0;
    for (int i3 : arrayOfChar1)
    {
      switch (i3)
      {
      case 10:
      case 13:
        if (((i3 != 13) || (n != 10)) && (i3 == 10) && (n == 13));
        m++;
        if (j < k)
          j = k;
        k = 0;
        break;
      default:
        k++;
      }
      n = i3;
    }
    if (j < k)
      j = k;
    double d2 = paramInt2 * f;
    double d3 = d2 / j;
    double d4 = d1 / m;
    int i;
    if (d4 > d3 * 2.0D)
    {
      i = (int)d3 * 2;
    }
    else
    {
      if ((paramBoolean) && (d4 > 14.0D))
        for (double d5 = 14.0D; ; d5 *= 1.1D)
        {
          double d6 = paramInt1 / d5;
          if (d6 < 5.0D)
            return (int)d5;
        }
      i = (int)d4;
    }
    if ((paramBoolean) && ((i < 4) || (i > 14)))
      i = 12;
    return i;
  }

  public boolean formsRasterizedForDisplay()
  {
    return this.rasterizeForms;
  }

  protected static int getFontSize(FormObject paramFormObject, int paramInt, float paramFloat)
  {
    int i = paramFormObject.getTextSize();
    if (i == -1)
      i = 0;
    if (i == 0)
    {
      Rectangle localRectangle = paramFormObject.getBoundingRectangle();
      int k = localRectangle.width;
      int m = localRectangle.height;
      if ((paramInt == 90) || (paramInt == 270))
      {
        int n = m;
        m = k;
        k = n;
      }
      i = (int)(m * 0.85D);
      String str = paramFormObject.getTextString();
      int i1 = paramFormObject.getFormType();
      if ((i1 == FormFactory.MULTILINETEXT.intValue()) || (i1 == FormFactory.MULTILINEPASSWORD.intValue()))
        i = calculateFontSize(m, k, true, str);
      else if ((i1 == FormFactory.SINGLELINETEXT.intValue()) || (i1 == FormFactory.SINGLELINEPASSWORD.intValue()))
        i = calculateFontSize(m, k, false, str);
      else if (str != null)
        i = calculateFontSize(m, k, false, str);
    }
    int j = (int)(i * paramFloat);
    if (j < 1)
      j = 1;
    return j;
  }

  protected void removeAllComponentsFromScreen()
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void setAutoFontSize(FormObject paramFormObject)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void renderFormsOntoG2(Object paramObject, int paramInt1, int paramInt2, int paramInt3, Map paramMap, FormFactory paramFormFactory, int paramInt4)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void setCustomPrintInterface(CustomFormPrint paramCustomFormPrint)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void resetScaledLocation(float paramFloat, int paramInt1, int paramInt2)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void setRootDisplayComponent(Object paramObject)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public List[] getFormList(boolean paramBoolean)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.acroforms.GUIData
 * JD-Core Version:    0.6.2
 */