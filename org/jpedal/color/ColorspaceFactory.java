package org.jpedal.color;

import java.util.Map;
import org.jpedal.io.PdfObjectReader;
import org.jpedal.objects.raw.PdfObject;

public class ColorspaceFactory
{
  public static final GenericColorSpace getColorSpaceInstance(PdfObjectReader paramPdfObjectReader, PdfObject paramPdfObject, Map paramMap)
  {
    String str;
    if (paramPdfObject.getStatus() == 0)
      str = paramPdfObject.getObjectRefAsString();
    else
      str = new String(paramPdfObject.getUnresolvedData());
    GenericColorSpace localGenericColorSpace = null;
    Object localObject = paramMap.get(str);
    if (localObject != null)
    {
      localGenericColorSpace = (GenericColorSpace)localObject;
      localGenericColorSpace.reset();
    }
    if (localGenericColorSpace == null)
    {
      localGenericColorSpace = getColorSpaceInstance(paramPdfObjectReader, paramPdfObject);
      if (localGenericColorSpace.getID() == 1247168582)
        paramMap.put(str, localGenericColorSpace);
    }
    return localGenericColorSpace;
  }

  public static final GenericColorSpace getColorSpaceInstance(PdfObjectReader paramPdfObjectReader, PdfObject paramPdfObject)
  {
    paramPdfObjectReader.checkResolved(paramPdfObject);
    int i = paramPdfObject.getParameterConstant(2087749783);
    if (i == 320678171)
      i = 1498837125;
    int j = 0;
    int k = 0;
    byte[] arrayOfByte = null;
    int m = -1;
    if ((i == 895578984) || (i == 25))
    {
      j = 1;
      k = paramPdfObject.getInt(960901492);
      arrayOfByte = paramPdfObject.getDictionary(1060856191).getDecodedStream();
      paramPdfObject = paramPdfObject.getDictionary(895578984);
      i = paramPdfObject.getParameterConstant(2087749783);
      m = i;
    }
    Object localObject = getColorspace(paramPdfObjectReader, paramPdfObject, i);
    if (j != 0)
    {
      if ((m == 1247168582) && (arrayOfByte.length < 3))
        localObject = new DeviceGrayColorSpace();
      ((GenericColorSpace)localObject).setIndex(arrayOfByte, k);
    }
    ((GenericColorSpace)localObject).setAlternateColorSpace(paramPdfObject.getParameterConstant(2054519176));
    return localObject;
  }

  private static GenericColorSpace getColorspace(PdfObjectReader paramPdfObjectReader, PdfObject paramPdfObject, int paramInt)
  {
    Object localObject = new DeviceRGBColorSpace();
    switch (paramInt)
    {
    case -2073385820:
      localObject = new SeparationColorSpace(paramPdfObjectReader, paramPdfObject);
      break;
    case 960981604:
      localObject = new DeviceNColorSpace(paramPdfObjectReader, paramPdfObject);
      break;
    case 1568372915:
      localObject = new DeviceGrayColorSpace();
      break;
    case 1498837125:
      localObject = new DeviceCMYKColorSpace();
      break;
    case 391471749:
      localObject = getCalGrayColorspace(paramPdfObject);
      break;
    case 1008872003:
      localObject = getCalRGBColorspace(paramPdfObject);
      break;
    case 1847602:
      localObject = getLabColorspace(paramPdfObject);
      break;
    case 1247168582:
      localObject = getICCColorspace(paramPdfObject);
      break;
    case 1146450818:
      localObject = new PatternColorSpace(paramPdfObjectReader);
    }
    return localObject;
  }

  private static GenericColorSpace getICCColorspace(PdfObject paramPdfObject)
  {
    Object localObject = new DeviceRGBColorSpace();
    int i = paramPdfObject.getParameterConstant(2054519176);
    if (i != 1568372915)
      localObject = new ICCColorSpace(paramPdfObject);
    if ((i == 1498837125) && (((GenericColorSpace)localObject).isInvalid()))
      localObject = new DeviceCMYKColorSpace();
    return localObject;
  }

  private static GenericColorSpace getLabColorspace(PdfObject paramPdfObject)
  {
    Object localObject1 = { -100.0F, 100.0F, -100.0F, 100.0F };
    Object localObject2 = { 0.0F, 1.0F, 0.0F };
    float[] arrayOfFloat1 = paramPdfObject.getFloatArray(2021497500);
    float[] arrayOfFloat2 = paramPdfObject.getFloatArray(826160983);
    if (arrayOfFloat1 != null)
      localObject2 = arrayOfFloat1;
    if (arrayOfFloat2 != null)
      localObject1 = arrayOfFloat2;
    return new LabColorSpace((float[])localObject2, (float[])localObject1);
  }

  private static GenericColorSpace getCalRGBColorspace(PdfObject paramPdfObject)
  {
    Object localObject1 = { 0.0F, 1.0F, 0.0F };
    Object localObject2 = { 1.0F, 1.0F, 1.0F };
    Object localObject3 = { 1.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 1.0F };
    float[] arrayOfFloat1 = paramPdfObject.getFloatArray(826096968);
    float[] arrayOfFloat2 = paramPdfObject.getFloatArray(2021497500);
    float[] arrayOfFloat3 = paramPdfObject.getFloatArray(1145198201);
    if (arrayOfFloat2 != null)
      localObject1 = arrayOfFloat2;
    if (arrayOfFloat1 != null)
      localObject2 = arrayOfFloat1;
    if (arrayOfFloat3 != null)
      localObject3 = arrayOfFloat3;
    return new CalRGBColorSpace((float[])localObject1, (float[])localObject3, (float[])localObject2);
  }

  private static GenericColorSpace getCalGrayColorspace(PdfObject paramPdfObject)
  {
    Object localObject1 = { 0.0F, 1.0F, 0.0F };
    Object localObject2 = { 1.0F, 1.0F, 1.0F };
    float[] arrayOfFloat1 = null;
    float[] arrayOfFloat2 = paramPdfObject.getFloatArray(2021497500);
    float f = paramPdfObject.getFloatNumber(826096968);
    if (f != -1.0F)
    {
      arrayOfFloat1 = new float[1];
      arrayOfFloat1[0] = f;
    }
    if (arrayOfFloat2 != null)
      localObject1 = arrayOfFloat2;
    if (arrayOfFloat1 != null)
      localObject2 = arrayOfFloat1;
    return new CalGrayColorSpace((float[])localObject1, (float[])localObject2);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.color.ColorspaceFactory
 * JD-Core Version:    0.6.2
 */