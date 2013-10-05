package org.jpedal.objects.acroforms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jpedal.exception.PdfException;
import org.jpedal.io.PdfObjectReader;
import org.jpedal.objects.Javascript;
import org.jpedal.objects.PdfPageData;
import org.jpedal.objects.acroforms.actions.ActionHandler;
import org.jpedal.objects.acroforms.creation.FormFactory;
import org.jpedal.objects.acroforms.creation.SwingFormFactory;
import org.jpedal.objects.acroforms.utils.FormUtils;
import org.jpedal.objects.raw.FormObject;
import org.jpedal.objects.raw.FormStream;
import org.jpedal.objects.raw.PageObject;
import org.jpedal.objects.raw.PdfArrayIterator;
import org.jpedal.objects.raw.PdfObject;
import org.jpedal.parser.PdfStreamDecoder;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.StringUtils;

public class AcroRenderer
{
  private FormObject[] Fforms;
  private FormObject[] Aforms;
  private PdfObject AcroRes = null;
  private float dpi = 72.0F;
  private Object[] CO = null;
  private PdfArrayIterator fieldList = null;
  private PdfArrayIterator[] annotList = null;
  private boolean ignoreForms = false;
  private FormFactory formFactory;
  private GUIData compData = new SwingData();
  private Set sigObject = null;
  private PdfPageData pageData;
  private int[] AfieldCount = null;
  private int ATotalCount;
  private int FfieldCount = 0;
  private int pageCount = 0;
  private PdfObjectReader currentPdfFile;
  private FormStream fDecoder;
  private ActionHandler formsActionHandler;
  private Javascript javascript;
  private PdfObject acroObj;
  private boolean hasXFA = false;
  private boolean useXFA = false;
  private boolean noOutput;

  public AcroRenderer(AcroRenderer paramAcroRenderer, PdfPageData paramPdfPageData, PdfObjectReader paramPdfObjectReader, boolean paramBoolean)
  {
    this.useXFA = paramAcroRenderer.useXFA;
    this.fDecoder = paramAcroRenderer.fDecoder;
    this.hasXFA = paramAcroRenderer.hasXFA;
    this.formFactory = paramAcroRenderer.formFactory;
    this.pageData = paramPdfPageData;
    this.currentPdfFile = paramPdfObjectReader;
    this.noOutput = paramBoolean;
  }

  public AcroRenderer(boolean paramBoolean)
  {
    this.useXFA = paramBoolean;
  }

  public void resetHandler(ActionHandler paramActionHandler, float paramFloat, Javascript paramJavascript)
  {
    this.formsActionHandler = paramActionHandler;
    this.dpi = paramFloat;
    this.javascript = paramJavascript;
    if (this.formFactory != null)
      this.formFactory.reset(getFormResources(), paramActionHandler, this.pageData, this.currentPdfFile);
  }

  public void removeDisplayComponentsFromScreen()
  {
    if (this.compData != null)
      this.compData.removeAllComponentsFromScreen();
  }

  public void openFile(int paramInt1, int paramInt2, int paramInt3, PdfPageData paramPdfPageData, PdfObjectReader paramPdfObjectReader, PdfObject paramPdfObject)
  {
    this.pageCount = paramInt1;
    this.currentPdfFile = paramPdfObjectReader;
    this.pageData = paramPdfPageData;
    this.acroObj = paramPdfObject;
    this.compData.flushFormData();
    this.sigObject = null;
    this.compData.setPageData(paramPdfPageData, paramInt2, paramInt3);
    if (paramPdfObject == null)
    {
      this.FfieldCount = 0;
      this.fieldList = null;
    }
    else
    {
      PdfArrayIterator localPdfArrayIterator = null;
      PdfObject localPdfObject = paramPdfObject.getDictionary(2627089);
      if (localPdfObject == null)
      {
        localPdfArrayIterator = paramPdfObject.getMixedArray(2627089);
        if ((localPdfArrayIterator != null) && (localPdfArrayIterator.getTokenCount() == 0))
          localPdfArrayIterator = null;
      }
      this.hasXFA = ((localPdfObject != null) || (localPdfArrayIterator != null));
      this.fDecoder = new FormStream();
      this.fieldList = paramPdfObject.getMixedArray(893143676);
      this.CO = paramPdfObject.getObjectArray(4895);
      if (this.fieldList != null)
      {
        this.FfieldCount = this.fieldList.getTokenCount();
        this.AcroRes = paramPdfObject.getDictionary(5154);
        if (this.AcroRes != null)
          paramPdfObjectReader.checkResolved(this.AcroRes);
      }
      else
      {
        this.FfieldCount = 0;
        this.AcroRes = null;
      }
      while (this.FfieldCount == 1)
      {
        String str = this.fieldList.getNextValueAsString(false);
        FormObject localFormObject = new FormObject(str);
        paramPdfObjectReader.readObject(localFormObject);
        byte[][] arrayOfByte = getKid(localFormObject);
        if (arrayOfByte == null)
          break;
        this.fieldList = new PdfArrayIterator(arrayOfByte);
        this.FfieldCount = this.fieldList.getTokenCount();
      }
    }
    resetContainers(true);
  }

  public void resetAnnotData(int paramInt1, int paramInt2, PdfPageData paramPdfPageData, int paramInt3, PdfObjectReader paramPdfObjectReader, byte[][] paramArrayOfByte)
  {
    this.currentPdfFile = paramPdfObjectReader;
    this.pageData = paramPdfPageData;
    boolean bool = true;
    this.compData.setPageData(paramPdfPageData, paramInt1, paramInt2);
    if (paramArrayOfByte == null)
    {
      this.AfieldCount = null;
      this.ATotalCount = 0;
      if (this.annotList != null)
        this.annotList[paramInt3] = null;
      this.annotList = null;
    }
    else
    {
      int i = paramPdfPageData.getPageCount() + 1;
      if (i <= paramInt3)
        i = paramInt3 + 1;
      if (this.annotList == null)
      {
        this.annotList = new PdfArrayIterator[i];
        this.AfieldCount = new int[i];
      }
      else if (paramInt3 >= this.annotList.length)
      {
        PdfArrayIterator[] arrayOfPdfArrayIterator = this.annotList;
        int[] arrayOfInt = this.AfieldCount;
        this.AfieldCount = new int[i];
        this.annotList = new PdfArrayIterator[i];
        for (int k = 0; k < arrayOfPdfArrayIterator.length; k++)
        {
          this.AfieldCount[k] = arrayOfInt[k];
          this.annotList[k] = arrayOfPdfArrayIterator[k];
        }
      }
      else if (this.AfieldCount == null)
      {
        this.AfieldCount = new int[i];
      }
      this.annotList[paramInt3] = new PdfArrayIterator(paramArrayOfByte);
      int j = this.annotList[paramInt3].getTokenCount();
      this.AfieldCount[paramInt3] = j;
      this.ATotalCount += j;
      bool = false;
      if (this.fDecoder == null)
        this.fDecoder = new FormStream();
    }
    resetContainers(bool);
  }

  protected void resetContainers(boolean paramBoolean)
  {
    if (paramBoolean)
      this.compData.resetComponents(this.ATotalCount + this.FfieldCount, this.pageCount, false);
    else
      this.compData.resetComponents(this.ATotalCount + this.FfieldCount, this.pageCount, true);
    if (this.formFactory == null)
    {
      this.formFactory = new SwingFormFactory();
      this.formFactory.reset(getFormResources(), this.formsActionHandler, this.pageData, this.currentPdfFile);
    }
    else
    {
      this.formFactory.reset(getFormResources(), this.formsActionHandler, this.pageData, this.currentPdfFile);
    }
  }

  public void createDisplayComponentsForPage(int paramInt, PdfStreamDecoder paramPdfStreamDecoder)
  {
    HashMap localHashMap1 = new HashMap();
    String str1 = System.getProperty("org.jpedal.flattenForm");
    if ((str1 != null) && (str1.toLowerCase().equals("true")))
      this.compData.setRasterizeForms(true);
    if (!this.compData.hasformsOnPageDecoded(paramInt))
    {
      this.compData.initParametersForPage(this.pageData, paramInt, this.formFactory, this.dpi);
      HashMap localHashMap2 = new HashMap();
      int i = 0;
      if ((this.AfieldCount != null) && (this.AfieldCount.length > paramInt))
        i = this.AfieldCount[paramInt];
      this.Fforms = new FormObject[this.FfieldCount];
      Object localObject1 = null;
      this.Aforms = new FormObject[i];
      int k = 2;
      Object localObject2;
      FormObject localFormObject1;
      for (int m = 0; m < k; m++)
      {
        int j = 0;
        if (m == 0)
        {
          localObject2 = 0;
          if (this.fieldList != null)
          {
            this.fieldList.resetToStart();
            localObject2 = this.fieldList.getTokenCount() - 1;
          }
        }
        else
        {
          if ((this.annotList != null) && (this.annotList.length > paramInt) && (this.annotList[paramInt] != null))
          {
            this.annotList[paramInt].resetToStart();
            if (this.formFactory.getType() == 3)
            {
              localObject3 = new HashMap();
              int n = this.annotList[paramInt].getTokenCount();
              for (int i3 = 0; i3 < n; i3++)
              {
                String str3 = this.annotList[paramInt].getNextValueAsString(true);
                ((Map)localObject3).put(str3, String.valueOf(i3 + 1));
              }
              this.formFactory.setAnnotOrder((Map)localObject3);
            }
            this.annotList[paramInt].resetToStart();
          }
          localObject2 = i - 1;
        }
        for (localObject3 = localObject2; localObject3 > -1; localObject3--)
        {
          String str2 = null;
          if (m == 0)
          {
            if (this.fieldList != null)
              str2 = this.fieldList.getNextValueAsString(true);
          }
          else if ((this.annotList.length > paramInt) && (this.annotList[paramInt] != null))
            str2 = this.annotList[paramInt].getNextValueAsString(true);
          if ((str2 != null) && ((str2 == null) || ((localHashMap2.get(str2) == null) && (!str2.isEmpty()))))
          {
            localFormObject1 = (FormObject)this.compData.getRawFormData().get(str2);
            if (localFormObject1 == null)
            {
              localFormObject1 = new FormObject(str2);
              localFormObject1.setPageRotation(this.pageData.getRotation(paramInt - 1));
              if (str2.charAt(str2.length() - 1) == 'R')
              {
                this.currentPdfFile.readObject(localFormObject1);
              }
              else
              {
                localFormObject1.setStatus(1);
                localFormObject1.setUnresolvedData(StringUtils.toBytes(str2), -1);
                this.currentPdfFile.checkResolved(localFormObject1);
              }
              this.compData.storeRawData(localFormObject1);
            }
            if ((m != 0) || (localFormObject1 == null) || (localFormObject1.getFormType() != -1))
            {
              byte[][] arrayOfByte = localFormObject1.getKeyArray(456733763);
              if (arrayOfByte != null)
                j = flattenKids(paramInt, localHashMap2, localFormObject1, j, m);
              else
                j = processFormObject(paramInt, localHashMap2, localFormObject1, str2, j, m);
            }
          }
        }
      }
      ArrayList localArrayList = new ArrayList();
      Object localObject3 = new ArrayList();
      this.compData.setListForPage(paramInt, localArrayList, false);
      this.compData.setListForPage(paramInt, (List)localObject3, true);
      int i1 = 3;
      for (int i2 = 0; i2 < i1; i2++)
      {
        localObject2 = 0;
        if (i2 == 0)
        {
          if (localObject1 != null)
            localObject2 = localObject1.length;
        }
        else
        {
          FormObject localFormObject2;
          if (i2 == 1)
          {
            for (localFormObject2 : this.Fforms)
              if (localFormObject2 != null)
                localArrayList.add(localFormObject2);
            this.Fforms = FormUtils.sortGroupLargestFirst(this.Fforms);
            localObject2 = this.Fforms.length;
          }
          else
          {
            for (localFormObject2 : this.Aforms)
              if (localFormObject2 != null)
                localArrayList.add(localFormObject2);
            this.Aforms = FormUtils.sortGroupLargestFirst(this.Aforms);
            localObject2 = this.Aforms.length;
          }
        }
        for (int i4 = 0; i4 < localObject2; i4++)
        {
          if (i2 == 0)
            localFormObject1 = localObject1[i4];
          else if (i2 == 1)
            localFormObject1 = this.Fforms[i4];
          else
            localFormObject1 = this.Aforms[i4];
          if ((localFormObject1 != null) && (localHashMap1.get(localFormObject1.getObjectRefAsString()) == null) && (paramInt == localFormObject1.getPageNumber()))
            if ((formsRasterizedForDisplay()) && (paramPdfStreamDecoder != null))
            {
              try
              {
                ??? = this.formFactory.getType();
                paramPdfStreamDecoder.drawFlattenedForm(localFormObject1, (??? == 3) || (??? == 4), (PdfObject)getFormResources()[0]);
              }
              catch (PdfException localPdfException)
              {
                if (LogWriter.isOutput())
                  LogWriter.writeLog("Exception: " + localPdfException.getMessage());
              }
            }
            else
            {
              createField(localFormObject1);
              this.compData.storeRawData(localFormObject1);
              localHashMap1.put(localFormObject1.getObjectRefAsString(), "x");
              ((List)localObject3).add(localFormObject1);
            }
        }
      }
      if (!formsRasterizedForDisplay())
        try
        {
          String str4 = this.currentPdfFile.getReferenceforPage(paramInt);
          PageObject localPageObject = new PageObject(str4);
          this.currentPdfFile.readObject(localPageObject);
          if ((this.javascript != null) && (this.formsActionHandler != null))
          {
            this.formsActionHandler.O(localPageObject, 4369);
            this.formsActionHandler.O(localPageObject, 17);
            this.formsActionHandler.PO(localPageObject, 4369);
            this.formsActionHandler.PO(localPageObject, 17);
          }
          if ((this.formFactory.getType() != 3) && (this.formFactory.getType() != 4))
            initJSonFields(localHashMap1);
        }
        catch (Exception localException)
        {
          if (LogWriter.isOutput())
            LogWriter.writeLog("Exception: " + localException.getMessage());
        }
    }
  }

  private int flattenKids(int paramInt1, Map paramMap, FormObject paramFormObject, int paramInt2, int paramInt3)
  {
    byte[][] arrayOfByte1 = paramFormObject.getKeyArray(456733763);
    int i = arrayOfByte1.length;
    int j;
    FormObject[] arrayOfFormObject;
    if (paramInt3 == 0)
    {
      j = this.Fforms.length;
      arrayOfFormObject = this.Fforms;
      this.Fforms = new FormObject[j + i - 1];
      System.arraycopy(arrayOfFormObject, 0, this.Fforms, 0, j);
    }
    else
    {
      j = this.Aforms.length;
      arrayOfFormObject = this.Aforms;
      this.Aforms = new FormObject[j + i - 1];
      System.arraycopy(arrayOfFormObject, 0, this.Aforms, 0, j);
    }
    for (byte[] arrayOfByte : arrayOfByte1)
    {
      String str = new String(arrayOfByte);
      FormObject localFormObject = new FormObject(str);
      if (paramFormObject != null)
        localFormObject.copyInheritedValuesFromParent(paramFormObject);
      this.currentPdfFile.readObject(localFormObject);
      localFormObject.setRef(str);
      if (localFormObject.getKeyArray(456733763) == null)
      {
        if (!localFormObject.isAppearanceUsed())
          new FormStream().createAppearanceString(localFormObject, this.currentPdfFile);
        paramInt2 = processFormObject(paramInt1, paramMap, localFormObject, str, paramInt2, paramInt3);
      }
      else
      {
        paramInt2 = flattenKids(paramInt1, paramMap, localFormObject, paramInt2, paramInt3);
      }
    }
    return paramInt2;
  }

  private int processFormObject(int paramInt1, Map paramMap, FormObject paramFormObject, Object paramObject, int paramInt2, int paramInt3)
  {
    int i = 0;
    Object localObject1;
    Object localObject2;
    if (paramInt3 == 0)
    {
      localObject1 = paramFormObject.getDictionary(32);
      localObject2 = null;
      if (localObject1 != null)
        localObject2 = ((PdfObject)localObject1).getUnresolvedData();
      Object localObject3;
      if ((localObject2 == null) || (localObject1 == null))
      {
        localObject3 = paramFormObject.getStringKey(1110793845);
        if (localObject3 != null)
        {
          FormObject localFormObject1 = getParent((String)localObject3);
          localObject1 = localFormObject1.getDictionary(32);
          if (localObject1 != null)
            localObject2 = ((PdfObject)localObject1).getUnresolvedData();
        }
      }
      if (localObject2 == null)
      {
        localObject3 = getKid(paramFormObject);
        int k = (localObject3 != null) && (localObject3.length > 0) ? 1 : 0;
        if (k != 0)
        {
          int m = localObject3.length;
          for (int n = 0; n < m; n++)
          {
            String str = new String(localObject3[n]);
            FormObject localFormObject2 = (FormObject)this.compData.getRawFormData().get(str);
            if (localFormObject2 == null)
            {
              localFormObject2 = new FormObject(str);
              this.currentPdfFile.readObject(localFormObject2);
              this.compData.storeRawData(localFormObject2);
            }
            localObject1 = localFormObject2.getDictionary(32);
            if (localObject1 != null)
              localObject2 = ((PdfObject)localObject1).getUnresolvedData();
            if (localObject2 != null)
              n = m;
          }
        }
      }
      int j = -1;
      if (localObject2 != null)
        j = this.currentPdfFile.convertObjectToPageNumber(new String((byte[])localObject2));
      i = j == paramInt1 ? 1 : 0;
    }
    if ((paramInt3 == 1) || (i != 0))
    {
      paramFormObject.setPageNumber(paramInt1);
      localObject1 = paramFormObject.getStringKey(1110793845);
      if (localObject1 != null)
      {
        localObject2 = getParent((String)localObject1);
        if (localObject2 != null)
          paramFormObject.setParent((String)localObject1, (FormObject)localObject2, true);
      }
      if (!paramFormObject.isAppearanceUsed())
        this.fDecoder.createAppearanceString(paramFormObject, this.currentPdfFile);
      if (paramFormObject != null)
      {
        if (localObject1 != null)
          paramFormObject.setParent((String)localObject1);
        if (paramInt3 == 0)
          this.Fforms[(paramInt2++)] = paramFormObject;
        else
          this.Aforms[(paramInt2++)] = paramFormObject;
        if (paramObject != null)
          paramMap.put(paramObject, "x");
      }
    }
    return paramInt2;
  }

  private FormObject getParent(String paramString)
  {
    FormObject localFormObject = (FormObject)this.compData.getRawFormData().get(paramString);
    if ((localFormObject == null) && (paramString != null))
    {
      localFormObject = new FormObject(paramString);
      this.currentPdfFile.readObject(localFormObject);
      localFormObject.setKeyArray(456733763, (byte[][])null);
      this.compData.storeRawData(localFormObject);
    }
    return localFormObject;
  }

  private byte[][] getKid(FormObject paramFormObject)
  {
    int i = paramFormObject.getParameterConstant(1147962727);
    if ((i == 9288) || (i == 1197118))
      return (byte[][])null;
    byte[][] arrayOfByte = paramFormObject.getKeyArray(456733763);
    if (arrayOfByte != null)
    {
      String str = paramFormObject.getStringKey(1110793845);
      FormObject localFormObject = getFormObject(str);
      if ((localFormObject != null) && (localFormObject.getKeyArray(456733763) != null))
        arrayOfByte = (byte[][])null;
    }
    return arrayOfByte;
  }

  public void displayComponentsOnscreen(int paramInt1, int paramInt2)
  {
    paramInt2++;
    this.compData.displayComponents(paramInt1, paramInt2);
  }

  private void initJSonFields(Map paramMap)
  {
    Iterator localIterator = paramMap.keySet().iterator();
    while (localIterator.hasNext())
    {
      Object localObject = localIterator.next();
      FormObject localFormObject = getFormObject((String)localObject);
      this.javascript.execute(localFormObject, 27, 6, ' ');
    }
  }

  private void createField(FormObject paramFormObject)
  {
    if (this.noOutput)
      return;
    Object localObject = null;
    int i = paramFormObject.getParameterConstant(1147962727);
    if (i == 2308407)
    {
      if (this.sigObject == null)
        this.sigObject = new HashSet();
      this.sigObject.add(paramFormObject);
    }
    if (paramFormObject.getDictionary(1061176672) != null)
      paramFormObject.setActionFlag(1);
    boolean[] arrayOfBoolean = paramFormObject.getFieldFlags();
    if (arrayOfBoolean != null)
    {
      int j = arrayOfBoolean[1];
      int k = arrayOfBoolean[2];
      int m = arrayOfBoolean[3];
    }
    int n;
    int i1;
    Integer localInteger;
    if (i == 1197118)
    {
      n = 0;
      i1 = 0;
      if (arrayOfBoolean != null)
      {
        n = arrayOfBoolean[17];
        i1 = arrayOfBoolean[16];
      }
      if (n != 0)
        localInteger = FormFactory.PUSHBUTTON;
      else if (i1 != 0)
        localInteger = FormFactory.RADIOBUTTON;
      else
        localInteger = FormFactory.CHECKBOXBUTTON;
    }
    else if (i == 9288)
    {
      n = 0;
      i1 = 0;
      if (arrayOfBoolean != null)
      {
        n = arrayOfBoolean[13];
        i1 = arrayOfBoolean[14];
      }
      if (n != 0)
      {
        if (i1 != 0)
          localInteger = FormFactory.MULTILINEPASSWORD;
        else
          localInteger = FormFactory.MULTILINETEXT;
      }
      else if (i1 != 0)
        localInteger = FormFactory.SINGLELINEPASSWORD;
      else
        localInteger = FormFactory.SINGLELINETEXT;
    }
    else if (i == 4920)
    {
      n = 0;
      if (arrayOfBoolean != null)
        n = arrayOfBoolean[18];
      if (n != 0)
        localInteger = FormFactory.COMBOBOX;
      else
        localInteger = FormFactory.LIST;
    }
    else if (i == 2308407)
    {
      localInteger = FormFactory.SIGNATURE;
    }
    else
    {
      localInteger = FormFactory.ANNOTATION;
    }
    paramFormObject.setFormType(localInteger.intValue());
    if ((localObject != null) && (this.formFactory.getType() != 1))
    {
      paramFormObject.setGUIComponent(localObject);
      this.compData.setGUIComp(paramFormObject, localObject);
    }
  }

  public Object[] getFormComponents(String paramString, ReturnValues paramReturnValues, int paramInt)
  {
    if (paramInt == -1)
      for (int i = 1; i < this.pageCount + 1; i++)
        createDisplayComponentsForPage(i, null);
    else
      createDisplayComponentsForPage(paramInt, null);
    return this.compData.getFormComponents(paramString, paramReturnValues, paramInt).toArray();
  }

  public void setFormFactory(FormFactory paramFormFactory)
  {
    this.formFactory = paramFormFactory;
    this.compData = this.formFactory.getCustomCompData();
  }

  public void substituteFormFactory(FormFactory paramFormFactory)
  {
    this.formFactory = paramFormFactory;
    this.compData.changeFormFactory(this.formFactory);
  }

  public GUIData getCompData()
  {
    return this.compData;
  }

  public Iterator getSignatureObjects()
  {
    if (this.sigObject == null)
      return null;
    return this.sigObject.iterator();
  }

  public ActionHandler getActionHandler()
  {
    return this.formsActionHandler;
  }

  public FormFactory getFormFactory()
  {
    return this.formFactory;
  }

  public void setIgnoreForms(boolean paramBoolean)
  {
    this.ignoreForms = paramBoolean;
  }

  public boolean ignoreForms()
  {
    return (this.ignoreForms) || (this.noOutput);
  }

  public void dispose()
  {
    this.AfieldCount = null;
    this.fDecoder = null;
    this.formsActionHandler = null;
    this.javascript = null;
    this.Fforms = null;
    this.Aforms = null;
    this.fieldList = null;
    this.annotList = null;
    this.formFactory = null;
    this.compData.dispose();
    this.compData = null;
    this.sigObject = null;
    this.pageData = null;
    this.currentPdfFile = null;
    this.fDecoder = null;
    this.acroObj = null;
  }

  /** @deprecated */
  public PdfArrayIterator getAnnotsOnPage(int paramInt)
  {
    createDisplayComponentsForPage(paramInt, null);
    if ((this.annotList != null) && (this.annotList.length > paramInt) && (this.annotList[paramInt] != null))
    {
      this.annotList[paramInt].resetToStart();
      return this.annotList[paramInt];
    }
    return null;
  }

  public boolean isXFA()
  {
    return this.hasXFA;
  }

  public boolean useXFA()
  {
    return this.useXFA;
  }

  public boolean hasFormsOnPage(int paramInt)
  {
    int i = (this.annotList != null) && (this.annotList.length > paramInt) && (this.annotList[paramInt] != null) ? 1 : 0;
    int j = ((this.hasXFA) && (this.useXFA) && (this.fDecoder.hasXFADataSet())) || (this.fieldList != null) ? 1 : 0;
    return (i != 0) || (j != 0);
  }

  public Object[] getFormResources()
  {
    return new Object[] { this.AcroRes, this.CO };
  }

  public boolean formsRasterizedForDisplay()
  {
    return this.compData.formsRasterizedForDisplay();
  }

  public FormObject getFormObject(String paramString)
  {
    FormObject localFormObject = (FormObject)this.compData.getRawFormData().get(paramString);
    if ((localFormObject == null) && (this.formFactory.getType() != 3) && (this.formFactory.getType() != 4))
      for (int i = 1; i < this.pageCount; i++)
      {
        createDisplayComponentsForPage(i, null);
        localFormObject = (FormObject)this.compData.getRawFormData().get(paramString);
        if (localFormObject != null)
          break;
      }
    return localFormObject;
  }

  public void setInsets(int paramInt1, int paramInt2)
  {
    this.compData.setPageData(this.compData.pageData, paramInt1, paramInt2);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.acroforms.AcroRenderer
 * JD-Core Version:    0.6.2
 */