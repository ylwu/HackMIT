package org.jpedal.parser;

import java.util.Map;
import org.jpedal.color.ColorspaceFactory;
import org.jpedal.color.DeviceCMYKColorSpace;
import org.jpedal.color.DeviceGrayColorSpace;
import org.jpedal.color.DeviceRGBColorSpace;
import org.jpedal.color.GenericColorSpace;
import org.jpedal.io.PdfObjectReader;
import org.jpedal.objects.GraphicsState;
import org.jpedal.objects.PdfPageData;
import org.jpedal.objects.raw.ColorSpaceObject;
import org.jpedal.objects.raw.PdfObject;
import org.jpedal.utils.StringUtils;

public class ColorCommands
{
  static void CS(boolean paramBoolean1, String paramString, GraphicsState paramGraphicsState, PdfObjectCache paramPdfObjectCache, PdfObjectReader paramPdfObjectReader, boolean paramBoolean2, int paramInt, PdfPageData paramPdfPageData, boolean paramBoolean3)
  {
    int i = !paramBoolean1 ? 1 : 0;
    Object localObject = (PdfObject)paramPdfObjectCache.get(2, paramString);
    if (localObject == null)
      localObject = new ColorSpaceObject(StringUtils.toBytes(paramString));
    String str1 = ((PdfObject)localObject).getObjectRefAsString();
    String str2 = str1 + '-' + paramBoolean1;
    GenericColorSpace localGenericColorSpace;
    if ((!paramBoolean3) && (paramPdfObjectCache.colorspacesObjects.containsKey(str1)))
    {
      localGenericColorSpace = (GenericColorSpace)paramPdfObjectCache.colorspacesObjects.get(str1);
      localGenericColorSpace.reset();
    }
    else if ((paramBoolean3) && (paramPdfObjectCache.colorspacesObjects.containsKey(str2)))
    {
      localGenericColorSpace = (GenericColorSpace)paramPdfObjectCache.colorspacesObjects.get(str2);
      localGenericColorSpace.reset();
    }
    else
    {
      localGenericColorSpace = ColorspaceFactory.getColorSpaceInstance(paramPdfObjectReader, (PdfObject)localObject);
      localGenericColorSpace.setPrinting(paramBoolean2);
      if ((localGenericColorSpace.getID() == 1247168582) || (localGenericColorSpace.getID() == -2073385820))
        if (!paramBoolean3)
          paramPdfObjectCache.colorspacesObjects.put(str1, localGenericColorSpace);
        else
          paramPdfObjectCache.colorspacesObjects.put(str2, localGenericColorSpace);
    }
    if (localGenericColorSpace.getID() == 1146450818)
    {
      localGenericColorSpace.setPattern(paramPdfObjectCache.patterns, paramPdfPageData.getMediaBoxWidth(paramInt), paramPdfPageData.getMediaBoxHeight(paramInt), paramGraphicsState.CTM);
      localGenericColorSpace.setGS(paramGraphicsState);
    }
    paramPdfObjectCache.put(1, localGenericColorSpace.getID(), "x");
    if (i != 0)
      paramGraphicsState.strokeColorSpace = localGenericColorSpace;
    else
      paramGraphicsState.nonstrokeColorSpace = localGenericColorSpace;
  }

  static void G(boolean paramBoolean, GraphicsState paramGraphicsState, CommandParser paramCommandParser, PdfObjectCache paramPdfObjectCache)
  {
    int i = !paramBoolean ? 1 : 0;
    float[] arrayOfFloat = paramCommandParser.getValuesAsFloat();
    int j = arrayOfFloat.length;
    if (i != 0)
    {
      if (paramGraphicsState.strokeColorSpace.getID() != 1568372915)
        paramGraphicsState.strokeColorSpace = new DeviceGrayColorSpace();
      paramGraphicsState.strokeColorSpace.setColor(arrayOfFloat, j);
      paramPdfObjectCache.put(1, paramGraphicsState.strokeColorSpace.getID(), "x");
    }
    else
    {
      if (paramGraphicsState.nonstrokeColorSpace.getID() != 1568372915)
        paramGraphicsState.nonstrokeColorSpace = new DeviceGrayColorSpace();
      paramGraphicsState.nonstrokeColorSpace.setColor(arrayOfFloat, j);
      paramPdfObjectCache.put(1, paramGraphicsState.nonstrokeColorSpace.getID(), "x");
    }
  }

  static void K(boolean paramBoolean, GraphicsState paramGraphicsState, CommandParser paramCommandParser, PdfObjectCache paramPdfObjectCache)
  {
    int i = !paramBoolean ? 1 : 0;
    Object localObject = paramCommandParser.getValuesAsFloat();
    int j = localObject.length;
    if (j > 3)
    {
      float[] arrayOfFloat = new float[j];
      for (int k = 0; k < j; k++)
        arrayOfFloat[(j - k - 1)] = localObject[k];
      localObject = arrayOfFloat;
      if (i != 0)
      {
        if (paramGraphicsState.strokeColorSpace.getID() != 1498837125)
          paramGraphicsState.strokeColorSpace = new DeviceCMYKColorSpace();
        paramGraphicsState.strokeColorSpace.setColor((float[])localObject, j);
        paramPdfObjectCache.put(1, paramGraphicsState.strokeColorSpace.getID(), "x");
      }
      else
      {
        if (paramGraphicsState.nonstrokeColorSpace.getID() != 1498837125)
          paramGraphicsState.nonstrokeColorSpace = new DeviceCMYKColorSpace();
        paramGraphicsState.nonstrokeColorSpace.setColor((float[])localObject, j);
        paramPdfObjectCache.put(1, paramGraphicsState.nonstrokeColorSpace.getID(), "x");
      }
    }
  }

  static void RG(boolean paramBoolean, GraphicsState paramGraphicsState, CommandParser paramCommandParser, PdfObjectCache paramPdfObjectCache)
  {
    int i = !paramBoolean ? 1 : 0;
    Object localObject = paramCommandParser.getValuesAsFloat();
    int j = localObject.length;
    float[] arrayOfFloat = new float[j];
    for (int k = 0; k < j; k++)
      arrayOfFloat[(j - k - 1)] = localObject[k];
    localObject = arrayOfFloat;
    if (i != 0)
    {
      if (paramGraphicsState.strokeColorSpace.getID() != 1785221209)
        paramGraphicsState.strokeColorSpace = new DeviceRGBColorSpace();
      paramGraphicsState.strokeColorSpace.setColor((float[])localObject, j);
      paramPdfObjectCache.put(1, paramGraphicsState.strokeColorSpace.getID(), "x");
    }
    else
    {
      if (paramGraphicsState.nonstrokeColorSpace.getID() != 1785221209)
        paramGraphicsState.nonstrokeColorSpace = new DeviceRGBColorSpace();
      paramGraphicsState.nonstrokeColorSpace.setColor((float[])localObject, j);
      paramPdfObjectCache.put(1, paramGraphicsState.nonstrokeColorSpace.getID(), "x");
    }
  }

  static void SCN(boolean paramBoolean, GraphicsState paramGraphicsState, CommandParser paramCommandParser, PdfObjectCache paramPdfObjectCache)
  {
    Object localObject;
    float[] arrayOfFloat;
    int k;
    if (paramBoolean)
    {
      if (paramGraphicsState.nonstrokeColorSpace.getID() == 1146450818)
      {
        String[] arrayOfString1 = paramCommandParser.getValuesAsString();
        paramGraphicsState.nonstrokeColorSpace.setColor(arrayOfString1, arrayOfString1.length);
      }
      else
      {
        localObject = paramCommandParser.getValuesAsFloat();
        int i = localObject.length;
        arrayOfFloat = new float[i];
        for (k = 0; k < i; k++)
          arrayOfFloat[(i - k - 1)] = localObject[k];
        localObject = arrayOfFloat;
        paramGraphicsState.nonstrokeColorSpace.setColor((float[])localObject, i);
      }
      paramPdfObjectCache.put(1, paramGraphicsState.nonstrokeColorSpace.getID(), "x");
    }
    else
    {
      if (paramGraphicsState.strokeColorSpace.getID() == 1146450818)
      {
        String[] arrayOfString2 = paramCommandParser.getValuesAsString();
        paramGraphicsState.strokeColorSpace.setColor(arrayOfString2, arrayOfString2.length);
      }
      else
      {
        localObject = paramCommandParser.getValuesAsFloat();
        int j = localObject.length;
        arrayOfFloat = new float[j];
        for (k = 0; k < j; k++)
          arrayOfFloat[(j - k - 1)] = localObject[k];
        localObject = arrayOfFloat;
        paramGraphicsState.strokeColorSpace.setColor((float[])localObject, j);
      }
      paramPdfObjectCache.put(1, paramGraphicsState.strokeColorSpace.getID(), "x");
    }
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.parser.ColorCommands
 * JD-Core Version:    0.6.2
 */