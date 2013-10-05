package org.jpedal.display;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import org.jpedal.exception.PdfException;
import org.jpedal.render.DynamicVectorRenderer;

public class Overlays
{
  private final Map overlayType = new HashMap();
  private final Map overlayColors = new HashMap();
  private final Map overlayObj = new HashMap();
  private final Map overlayTypeG = new HashMap();
  private final Map overlayColorsG = new HashMap();
  private final Map overlayObjG = new HashMap();

  public void printAdditionalObjectsOverPage(int paramInt, int[] paramArrayOfInt, Color[] paramArrayOfColor, Object[] paramArrayOfObject)
    throws PdfException
  {
    Integer localInteger = Integer.valueOf(paramInt);
    if (paramArrayOfObject == null)
    {
      this.overlayType.remove(localInteger);
      this.overlayColors.remove(localInteger);
      this.overlayObj.remove(localInteger);
    }
    else
    {
      int[] arrayOfInt1 = (int[])this.overlayType.get(localInteger);
      int j;
      if (arrayOfInt1 == null)
      {
        this.overlayType.put(localInteger, paramArrayOfInt);
      }
      else
      {
        int i = arrayOfInt1.length;
        j = paramArrayOfInt.length;
        int[] arrayOfInt2 = new int[i + j];
        System.arraycopy(arrayOfInt1, 0, arrayOfInt2, 0, i);
        System.arraycopy(paramArrayOfInt, 0, arrayOfInt2, i, j);
        this.overlayType.put(localInteger, arrayOfInt2);
      }
      Color[] arrayOfColor1 = (Color[])this.overlayColors.get(localInteger);
      int k;
      if (arrayOfColor1 == null)
      {
        this.overlayColors.put(localInteger, paramArrayOfColor);
      }
      else
      {
        j = arrayOfColor1.length;
        k = paramArrayOfColor.length;
        Color[] arrayOfColor2 = new Color[j + k];
        System.arraycopy(arrayOfColor1, 0, arrayOfColor2, 0, j);
        System.arraycopy(paramArrayOfColor, 0, arrayOfColor2, j, k);
        this.overlayColors.put(localInteger, arrayOfColor2);
      }
      Object[] arrayOfObject1 = (Object[])this.overlayObj.get(localInteger);
      if (arrayOfInt1 == null)
      {
        this.overlayObj.put(localInteger, paramArrayOfObject);
      }
      else
      {
        k = arrayOfObject1.length;
        int m = paramArrayOfObject.length;
        Object[] arrayOfObject2 = new Object[k + m];
        System.arraycopy(arrayOfObject1, 0, arrayOfObject2, 0, k);
        System.arraycopy(paramArrayOfObject, 0, arrayOfObject2, k, m);
        this.overlayObj.put(localInteger, arrayOfObject2);
      }
    }
  }

  public void printAdditionalObjectsOverAllPages(int[] paramArrayOfInt, Color[] paramArrayOfColor, Object[] paramArrayOfObject)
    throws PdfException
  {
    Integer localInteger = Integer.valueOf(-1);
    if (paramArrayOfObject == null)
    {
      this.overlayTypeG.remove(localInteger);
      this.overlayColorsG.remove(localInteger);
      this.overlayObjG.remove(localInteger);
    }
    else
    {
      int[] arrayOfInt1 = (int[])this.overlayTypeG.get(localInteger);
      int j;
      if (arrayOfInt1 == null)
      {
        this.overlayTypeG.put(localInteger, paramArrayOfInt);
      }
      else
      {
        int i = arrayOfInt1.length;
        j = paramArrayOfInt.length;
        int[] arrayOfInt2 = new int[i + j];
        System.arraycopy(arrayOfInt1, 0, arrayOfInt2, 0, i);
        System.arraycopy(paramArrayOfInt, 0, arrayOfInt2, i, j);
        this.overlayTypeG.put(localInteger, arrayOfInt2);
      }
      Color[] arrayOfColor1 = (Color[])this.overlayColorsG.get(localInteger);
      int k;
      if (arrayOfColor1 == null)
      {
        this.overlayColorsG.put(localInteger, paramArrayOfColor);
      }
      else
      {
        j = arrayOfColor1.length;
        k = paramArrayOfColor.length;
        Color[] arrayOfColor2 = new Color[j + k];
        System.arraycopy(arrayOfColor1, 0, arrayOfColor2, 0, j);
        System.arraycopy(paramArrayOfColor, 0, arrayOfColor2, j, k);
        this.overlayColorsG.put(localInteger, arrayOfColor2);
      }
      Object[] arrayOfObject1 = (Object[])this.overlayObjG.get(localInteger);
      if (arrayOfInt1 == null)
      {
        this.overlayObjG.put(localInteger, paramArrayOfObject);
      }
      else
      {
        k = arrayOfObject1.length;
        int m = paramArrayOfObject.length;
        Object[] arrayOfObject2 = new Object[k + m];
        System.arraycopy(arrayOfObject1, 0, arrayOfObject2, 0, k);
        System.arraycopy(paramArrayOfObject, 0, arrayOfObject2, k, m);
        this.overlayObjG.put(localInteger, arrayOfObject2);
      }
    }
  }

  public void clear()
  {
    this.overlayType.clear();
    this.overlayColors.clear();
    this.overlayObj.clear();
    this.overlayTypeG.clear();
    this.overlayColorsG.clear();
    this.overlayObjG.clear();
  }

  public void printOverlays(DynamicVectorRenderer paramDynamicVectorRenderer, int paramInt)
    throws PdfException
  {
    Integer localInteger1 = Integer.valueOf(-1);
    int[] arrayOfInt1 = (int[])this.overlayTypeG.get(localInteger1);
    Color[] arrayOfColor1 = (Color[])this.overlayColorsG.get(localInteger1);
    Object[] arrayOfObject1 = (Object[])this.overlayObjG.get(localInteger1);
    paramDynamicVectorRenderer.drawAdditionalObjectsOverPage(arrayOfInt1, arrayOfColor1, arrayOfObject1);
    Integer localInteger2 = Integer.valueOf(paramInt);
    int[] arrayOfInt2 = (int[])this.overlayType.get(localInteger2);
    Color[] arrayOfColor2 = (Color[])this.overlayColors.get(localInteger2);
    Object[] arrayOfObject2 = (Object[])this.overlayObj.get(localInteger2);
    paramDynamicVectorRenderer.drawAdditionalObjectsOverPage(arrayOfInt2, arrayOfColor2, arrayOfObject2);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.display.Overlays
 * JD-Core Version:    0.6.2
 */