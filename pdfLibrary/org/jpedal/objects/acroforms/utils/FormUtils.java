package org.jpedal.objects.acroforms.utils;

import java.awt.Rectangle;
import org.jpedal.objects.raw.FormObject;

public class FormUtils
{
  public static FormObject[] sortGroupLargestFirst(FormObject[] paramArrayOfFormObject)
  {
    return sortCompsDesending(paramArrayOfFormObject);
  }

  private static FormObject[] sortCompsDesending(FormObject[] paramArrayOfFormObject)
  {
    int i = paramArrayOfFormObject.length;
    int j = i / 2;
    int k = i - 1;
    while (j > 0)
    {
      j--;
      siftCompsDesending(paramArrayOfFormObject, j, k);
    }
    while (k > 0)
    {
      FormObject localFormObject = paramArrayOfFormObject[0];
      paramArrayOfFormObject[0] = paramArrayOfFormObject[k];
      paramArrayOfFormObject[k] = localFormObject;
      k--;
      siftCompsDesending(paramArrayOfFormObject, j, k);
    }
    return paramArrayOfFormObject;
  }

  private static void siftCompsDesending(FormObject[] paramArrayOfFormObject, int paramInt1, int paramInt2)
  {
    int i = paramInt1;
    FormObject localFormObject = paramArrayOfFormObject[i];
    int j = 2 * paramInt1 + 1;
    if ((j < paramInt2) && (shouldSwapControlDesending(paramArrayOfFormObject[j], paramArrayOfFormObject[(j + 1)])))
      j++;
    while ((j <= paramInt2) && (shouldSwapControlDesending(localFormObject, paramArrayOfFormObject[j])))
    {
      paramArrayOfFormObject[i] = paramArrayOfFormObject[j];
      i = j;
      j = 2 * j + 1;
      if ((j < paramInt2) && (shouldSwapControlDesending(paramArrayOfFormObject[j], paramArrayOfFormObject[(j + 1)])))
        j++;
    }
    paramArrayOfFormObject[i] = localFormObject;
  }

  private static boolean shouldSwapControlDesending(FormObject paramFormObject1, FormObject paramFormObject2)
  {
    if (paramFormObject1 == null)
      return paramFormObject2 != null;
    if (paramFormObject2 == null)
      return false;
    Rectangle localRectangle1 = paramFormObject1.getBoundingRectangle();
    Rectangle localRectangle2 = paramFormObject2.getBoundingRectangle();
    return localRectangle1.width * localRectangle1.height < localRectangle2.width * localRectangle2.height;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.acroforms.utils.FormUtils
 * JD-Core Version:    0.6.2
 */