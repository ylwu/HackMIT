package org.jpedal.objects.javascript.jsobjects;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.jpedal.objects.acroforms.AcroRenderer;
import org.jpedal.objects.acroforms.GUIData;
import org.jpedal.objects.acroforms.ReturnValues;
import org.jpedal.objects.raw.FormObject;

public class JSDoc
{
  private AcroRenderer acroRenderer = null;
  private HashMap<String, JSField> nameTofields = new HashMap();
  private HashMap<String, JSField> refTofields = new HashMap();

  public JSDoc(AcroRenderer paramAcroRenderer)
  {
    setAcroRenderer(paramAcroRenderer);
  }

  public JSDoc()
  {
  }

  public void setAcroRenderer(AcroRenderer paramAcroRenderer)
  {
    this.acroRenderer = paramAcroRenderer;
    if (paramAcroRenderer != null)
      loadFormObjects();
  }

  public void loadFormObjects()
  {
    if (this.acroRenderer == null)
      throw new RuntimeException("No acrorender object set for Doc object.");
    List localList = this.acroRenderer.getCompData().getFormComponents(null, ReturnValues.FORMOBJECTS_FROM_REF, -1);
    Iterator localIterator = localList.iterator();
    while (localIterator.hasNext())
    {
      FormObject localFormObject = (FormObject)localIterator.next();
      JSField localJSField = new JSField(localFormObject);
      this.refTofields.put(localFormObject.getObjectRefAsString(), localJSField);
      this.nameTofields.put(localFormObject.getTextStreamValue(36), localJSField);
    }
  }

  public JSField getField(String paramString)
  {
    if (this.nameTofields.size() <= 0)
      loadFormObjects();
    if (this.nameTofields.containsKey(paramString))
      return (JSField)this.nameTofields.get(paramString);
    return null;
  }

  public JSField getFieldByRef(String paramString)
  {
    if (this.refTofields.size() <= 0)
      loadFormObjects();
    if (this.refTofields.containsKey(paramString))
      return (JSField)this.refTofields.get(paramString);
    return null;
  }

  public void flush()
  {
    this.nameTofields = new HashMap();
    this.refTofields = new HashMap();
  }

  public FormObject[] getFormObjects()
  {
    FormObject[] arrayOfFormObject = new FormObject[this.refTofields.size()];
    int i = 0;
    Iterator localIterator = this.refTofields.keySet().iterator();
    while (localIterator.hasNext())
    {
      String str = (String)localIterator.next();
      arrayOfFormObject[i] = ((JSField)this.refTofields.get(str)).target;
      i++;
    }
    return arrayOfFormObject;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.javascript.jsobjects.JSDoc
 * JD-Core Version:    0.6.2
 */