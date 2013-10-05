package org.jpedal.text;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;
import org.jpedal.utils.repositories.Vector_Rectangle;

public class TextLines
{
  private Map lineAreas = new HashMap();
  private Map lineWritingMode = new HashMap();
  public Map areas = new HashMap();

  public Rectangle setFoundParagraph(int paramInt1, int paramInt2, int paramInt3)
  {
    Rectangle[] arrayOfRectangle = getLineAreas(paramInt3);
    if (arrayOfRectangle != null)
    {
      Rectangle localRectangle1 = new Rectangle(paramInt1, paramInt2, 1, 1);
      Rectangle localRectangle2 = new Rectangle(0, 0, 0, 0);
      int i = 0;
      int j = 0;
      for (int k = 0; k != arrayOfRectangle.length; k++)
        if (arrayOfRectangle[k].intersects(localRectangle1))
        {
          j = k;
          i = 1;
          break;
        }
      if (i != 0)
      {
        double d1 = arrayOfRectangle[j].x;
        double d2 = arrayOfRectangle[j].getCenterX();
        double d3 = arrayOfRectangle[j].x + arrayOfRectangle[j].width;
        double d4 = arrayOfRectangle[j].getCenterY();
        int m = arrayOfRectangle[j].height;
        localRectangle2.x = arrayOfRectangle[j].x;
        localRectangle2.y = arrayOfRectangle[j].y;
        localRectangle2.width = arrayOfRectangle[j].width;
        localRectangle2.height = arrayOfRectangle[j].height;
        int n = 1;
        int i1 = 1;
        Vector_Rectangle localVector_Rectangle = new Vector_Rectangle(0);
        localVector_Rectangle.addElement(arrayOfRectangle[j]);
        int i2;
        while (n != 0)
        {
          n = 0;
          for (i2 = 0; i2 != arrayOfRectangle.length; i2++)
            if ((arrayOfRectangle[i2].contains(d1, d4 + m)) || (arrayOfRectangle[i2].contains(d2, d4 + m)) || (arrayOfRectangle[i2].contains(d3, d4 + m)))
            {
              localVector_Rectangle.addElement(arrayOfRectangle[i2]);
              n = 1;
              d4 = arrayOfRectangle[i2].getCenterY();
              m = arrayOfRectangle[i2].height;
              if (localRectangle2.x > arrayOfRectangle[i2].x)
              {
                localRectangle2.width = (localRectangle2.x + localRectangle2.width - arrayOfRectangle[i2].x);
                localRectangle2.x = arrayOfRectangle[i2].x;
              }
              if (localRectangle2.x + localRectangle2.width < arrayOfRectangle[i2].x + arrayOfRectangle[i2].width)
                localRectangle2.width = (arrayOfRectangle[i2].x + arrayOfRectangle[i2].width - localRectangle2.x);
              if (localRectangle2.y > arrayOfRectangle[i2].y)
              {
                localRectangle2.height = (localRectangle2.y + localRectangle2.height - arrayOfRectangle[i2].y);
                localRectangle2.y = arrayOfRectangle[i2].y;
              }
              if (localRectangle2.y + localRectangle2.height >= arrayOfRectangle[i2].y + arrayOfRectangle[i2].height)
                break;
              localRectangle2.height = (arrayOfRectangle[i2].y + arrayOfRectangle[i2].height - localRectangle2.y);
              break;
            }
        }
        d1 = arrayOfRectangle[j].x;
        d2 = arrayOfRectangle[j].getCenterX();
        d3 = arrayOfRectangle[j].x + arrayOfRectangle[j].width;
        d4 = arrayOfRectangle[j].getCenterY();
        m = arrayOfRectangle[j].height;
        while (i1 != 0)
        {
          i1 = 0;
          for (i2 = 0; i2 != arrayOfRectangle.length; i2++)
            if ((arrayOfRectangle[i2].contains(d1, d4 - m)) || (arrayOfRectangle[i2].contains(d2, d4 - m)) || (arrayOfRectangle[i2].contains(d3, d4 - m)))
            {
              localVector_Rectangle.addElement(arrayOfRectangle[i2]);
              i1 = 1;
              d4 = arrayOfRectangle[i2].getCenterY();
              m = arrayOfRectangle[i2].height;
              if (localRectangle2.x > arrayOfRectangle[i2].x)
              {
                localRectangle2.width = (localRectangle2.x + localRectangle2.width - arrayOfRectangle[i2].x);
                localRectangle2.x = arrayOfRectangle[i2].x;
              }
              if (localRectangle2.x + localRectangle2.width < arrayOfRectangle[i2].x + arrayOfRectangle[i2].width)
                localRectangle2.width = (arrayOfRectangle[i2].x + arrayOfRectangle[i2].width - localRectangle2.x);
              if (localRectangle2.y > arrayOfRectangle[i2].y)
              {
                localRectangle2.height = (localRectangle2.y + localRectangle2.height - arrayOfRectangle[i2].y);
                localRectangle2.y = arrayOfRectangle[i2].y;
              }
              if (localRectangle2.y + localRectangle2.height >= arrayOfRectangle[i2].y + arrayOfRectangle[i2].height)
                break;
              localRectangle2.height = (arrayOfRectangle[i2].y + arrayOfRectangle[i2].height - localRectangle2.y);
              break;
            }
        }
        localVector_Rectangle.trim();
        addHighlights(localVector_Rectangle.get(), true, paramInt3);
        return localRectangle2;
      }
      return null;
    }
    return null;
  }

  public void addToLineAreas(Rectangle paramRectangle, int paramInt1, int paramInt2)
  {
    int i = 1;
    if (this.lineAreas == null)
    {
      this.lineAreas = new HashMap();
      this.lineAreas.put(Integer.valueOf(paramInt2), new Rectangle[] { paramRectangle });
      this.lineWritingMode = new HashMap();
      this.lineWritingMode.put(Integer.valueOf(paramInt2), new int[] { paramInt1 });
    }
    else
    {
      Rectangle[] arrayOfRectangle1 = (Rectangle[])this.lineAreas.get(Integer.valueOf(paramInt2));
      int[] arrayOfInt1 = (int[])this.lineWritingMode.get(Integer.valueOf(paramInt2));
      if (paramRectangle != null)
      {
        int m;
        if (arrayOfRectangle1 != null)
          for (int j = 0; j != arrayOfRectangle1.length; j++)
          {
            int k = arrayOfInt1[j];
            m = paramInt1;
            int n = paramRectangle.x;
            int i1 = paramRectangle.y;
            int i2 = paramRectangle.width;
            int i3 = paramRectangle.height;
            int i4 = arrayOfRectangle1[j].x;
            int i5 = arrayOfRectangle1[j].y;
            int i6 = arrayOfRectangle1[j].width;
            int i7 = arrayOfRectangle1[j].height;
            float f1 = 5.0F;
            float f2 = 1.1F;
            switch (paramInt1)
            {
            case 0:
              if ((k == m) && (i5 > i1 - i3 / f1) && (i5 < i1 + i3 / f1) && (i7 < i3 + i3 / f1) && (i7 > i3 - i3 / f1) && (((i4 > n + i2 - i3 * f2) && (i4 < n + i2 + i3 * f2)) || ((i4 + i6 > n - i3 * f2) && (i4 + i6 < n + i3 * f2)) || (arrayOfRectangle1[j].intersects(paramRectangle))))
              {
                i = 0;
                arrayOfRectangle1[j] = mergePartLines(arrayOfRectangle1[j], paramRectangle);
              }
              break;
            case 1:
              i4 = arrayOfRectangle1[j].x;
              i5 = arrayOfRectangle1[j].y;
              i6 = arrayOfRectangle1[j].width;
              i7 = arrayOfRectangle1[j].height;
              n = paramRectangle.x;
              i1 = paramRectangle.y;
              i2 = paramRectangle.width;
              i3 = paramRectangle.height;
              if ((k == m) && (i5 > i1 - 5) && (i5 < i1 + 5) && (i7 <= i3 + i3 / 5) && (i7 >= i3 - i3 / 5) && (((i4 > n + i2 - i3 * 0.6D) && (i4 < n + i2 + i3 * 0.6D)) || ((i4 + i6 > n - i3 * 0.6D) && (i4 + i6 < n + i3 * 0.6D)) || (arrayOfRectangle1[j].intersects(paramRectangle))))
              {
                i = 0;
                arrayOfRectangle1[j] = mergePartLines(arrayOfRectangle1[j], paramRectangle);
              }
              break;
            case 2:
              i4 = arrayOfRectangle1[j].y;
              i5 = arrayOfRectangle1[j].x;
              i6 = arrayOfRectangle1[j].height;
              i7 = arrayOfRectangle1[j].width;
              n = paramRectangle.y;
              i1 = paramRectangle.x;
              i2 = paramRectangle.height;
              i3 = paramRectangle.width;
              if ((k == m) && (i5 > i1 - 5) && (i5 < i1 + 5) && (i7 <= i3 + i3 / 5) && (i7 >= i3 - i3 / 5) && (((i4 > n + i2 - i3 * 0.6D) && (i4 < n + i2 + i3 * 0.6D)) || ((i4 + i6 > n - i3 * 0.6D) && (i4 + i6 < n + i3 * 0.6D)) || (arrayOfRectangle1[j].intersects(paramRectangle))))
              {
                i = 0;
                arrayOfRectangle1[j] = mergePartLines(arrayOfRectangle1[j], paramRectangle);
              }
              break;
            case 3:
              int i8 = n + i2;
              int i9 = i4 + i6;
              if ((k == m) && (i8 >= i9 - i6 / 3) && (i8 <= i9 + i6 / 3) && (((i5 + (i7 + i6 * 0.6D) > i1) && (i5 + (i7 - i6 * 0.6D) < i1)) || ((i5 + i6 * 0.6D > i1 + i3) && (i5 - i6 * 0.6D < i1 + i3)) || (paramRectangle.intersects(arrayOfRectangle1[j]))))
              {
                i = 0;
                arrayOfRectangle1[j] = mergePartLines(arrayOfRectangle1[j], paramRectangle);
              }
              break;
            }
          }
        else
          i = 1;
        if (i != 0)
        {
          Rectangle[] arrayOfRectangle2;
          int[] arrayOfInt2;
          if (arrayOfRectangle1 != null)
          {
            arrayOfRectangle2 = new Rectangle[arrayOfRectangle1.length + 1];
            for (m = 0; m != arrayOfRectangle1.length; m++)
              arrayOfRectangle2[m] = arrayOfRectangle1[m];
            arrayOfRectangle2[(arrayOfRectangle2.length - 1)] = paramRectangle;
            arrayOfInt2 = new int[arrayOfInt1.length + 1];
            for (m = 0; m != arrayOfInt1.length; m++)
              arrayOfInt2[m] = arrayOfInt1[m];
            arrayOfInt2[(arrayOfInt2.length - 1)] = paramInt1;
          }
          else
          {
            arrayOfRectangle2 = new Rectangle[1];
            arrayOfRectangle2[0] = paramRectangle;
            arrayOfInt2 = new int[1];
            arrayOfInt2[0] = paramInt1;
          }
          this.lineAreas.put(Integer.valueOf(paramInt2), arrayOfRectangle2);
          this.lineWritingMode.put(Integer.valueOf(paramInt2), arrayOfInt2);
        }
      }
    }
  }

  public void removeFoundTextArea(Rectangle paramRectangle, int paramInt)
  {
    if ((paramRectangle == null) || (this.areas == null))
      return;
    Integer localInteger = Integer.valueOf(paramInt);
    Rectangle[] arrayOfRectangle = (Rectangle[])this.areas.get(localInteger);
    if (arrayOfRectangle != null)
    {
      int i = arrayOfRectangle.length;
      for (int j = 0; j < i; j++)
        if ((arrayOfRectangle[j] != null) && ((arrayOfRectangle[j].contains(paramRectangle)) || ((arrayOfRectangle[j].x == paramRectangle.x) && (arrayOfRectangle[j].y == paramRectangle.y) && (arrayOfRectangle[j].width == paramRectangle.width) && (arrayOfRectangle[j].height == paramRectangle.height))))
        {
          arrayOfRectangle[j] = null;
          j = i;
        }
      this.areas.put(localInteger, arrayOfRectangle);
    }
  }

  public void removeFoundTextAreas(Rectangle[] paramArrayOfRectangle, int paramInt)
  {
    if (paramArrayOfRectangle == null)
    {
      this.areas = null;
    }
    else
    {
      for (Rectangle localRectangle : paramArrayOfRectangle)
        removeFoundTextArea(localRectangle, paramInt);
      int i = 1;
      Integer localInteger = Integer.valueOf(paramInt);
      Rectangle[] arrayOfRectangle2 = (Rectangle[])this.areas.get(localInteger);
      if (arrayOfRectangle2 != null)
      {
        for (int m = 0; m < arrayOfRectangle2.length; m++)
          if (arrayOfRectangle2[m] != null)
          {
            i = 0;
            m = arrayOfRectangle2.length;
          }
        if (i != 0)
        {
          arrayOfRectangle2 = null;
          this.areas.put(localInteger, arrayOfRectangle2);
        }
      }
    }
  }

  public void clearHighlights()
  {
    this.areas = null;
  }

  public void addHighlights(Rectangle[] paramArrayOfRectangle, boolean paramBoolean, int paramInt)
  {
    if (paramArrayOfRectangle != null)
    {
      int i;
      Object localObject1;
      Object localObject2;
      int m;
      int i1;
      if (!paramBoolean)
        for (i = 0; i != paramArrayOfRectangle.length; i++)
          if (paramArrayOfRectangle[i] != null)
          {
            localObject1 = new Point(paramArrayOfRectangle[i].x + 1, paramArrayOfRectangle[i].y + 1);
            localObject2 = new Point(paramArrayOfRectangle[i].x + paramArrayOfRectangle[i].width - 1, paramArrayOfRectangle[i].y + paramArrayOfRectangle[i].height - 1);
            if (this.areas == null)
              this.areas = new HashMap();
            Rectangle[] arrayOfRectangle1 = getLineAreas(paramInt);
            int[] arrayOfInt = getLineWritingMode(paramInt);
            m = -1;
            int n = -1;
            i1 = 0;
            if (arrayOfRectangle1 != null)
            {
              for (int i2 = 0; i2 != arrayOfRectangle1.length; i2++)
              {
                if (arrayOfRectangle1[i2].contains((Point)localObject1))
                  m = i2;
                if (arrayOfRectangle1[i2].contains((Point)localObject2))
                  n = i2;
                if ((m != -1) && (n != -1))
                  break;
              }
              if (m > n)
              {
                i2 = m;
                m = n;
                n = i2;
                i1 = 1;
              }
              Object localObject3;
              if ((m == n) && (((Point)localObject1).x > ((Point)localObject2).x))
              {
                localObject3 = localObject1;
                localObject1 = localObject2;
                localObject2 = localObject3;
              }
              if ((m != -1) && (n != -1))
              {
                localObject3 = Integer.valueOf(paramInt);
                Rectangle[] arrayOfRectangle3 = new Rectangle[n - m + 1];
                System.arraycopy(arrayOfRectangle1, m + 0, arrayOfRectangle3, 0, n - m + 1);
                if (arrayOfRectangle3.length > 0)
                {
                  int i3 = 0;
                  int i4 = arrayOfRectangle3.length - 1;
                  if ((arrayOfRectangle3[i3] != null) && (arrayOfRectangle3[i4] != null))
                  {
                    switch (arrayOfInt[m])
                    {
                    case 0:
                      if (i1 != 0)
                      {
                        if (((Point)localObject2).x - 15 > arrayOfRectangle3[i3].x)
                        {
                          arrayOfRectangle3[i3].width -= ((Point)localObject2).x - arrayOfRectangle3[i3].x;
                          arrayOfRectangle3[i3].x = ((Point)localObject2).x;
                        }
                      }
                      else if (((Point)localObject1).x - 15 > arrayOfRectangle3[i3].x)
                      {
                        arrayOfRectangle3[i3].width -= ((Point)localObject1).x - arrayOfRectangle3[i3].x;
                        arrayOfRectangle3[i3].x = ((Point)localObject1).x;
                      }
                      break;
                    case 1:
                      break;
                    case 2:
                      if (i1 != 0)
                      {
                        if (((Point)localObject2).y - 15 > arrayOfRectangle3[i3].y)
                        {
                          arrayOfRectangle3[i3].height -= ((Point)localObject2).y - arrayOfRectangle3[i3].y;
                          arrayOfRectangle3[i3].y = ((Point)localObject2).y;
                        }
                      }
                      else if (((Point)localObject1).y - 15 > arrayOfRectangle3[i3].y)
                      {
                        arrayOfRectangle3[i3].height -= ((Point)localObject1).y - arrayOfRectangle3[i3].y;
                        arrayOfRectangle3[i3].y = ((Point)localObject1).y;
                      }
                      break;
                    case 3:
                      if (i1 != 0)
                      {
                        if (((Point)localObject2).y - 15 > arrayOfRectangle3[i3].y)
                        {
                          arrayOfRectangle3[i3].height -= ((Point)localObject2).y - arrayOfRectangle3[i3].y;
                          arrayOfRectangle3[i3].y = ((Point)localObject2).y;
                        }
                      }
                      else if (((Point)localObject1).y - 15 > arrayOfRectangle3[i3].y)
                      {
                        arrayOfRectangle3[i3].height -= ((Point)localObject1).y - arrayOfRectangle3[i3].y;
                        arrayOfRectangle3[i3].y = ((Point)localObject1).y;
                      }
                      break;
                    }
                    switch (arrayOfInt[n])
                    {
                    case 0:
                      if (i1 != 0)
                      {
                        if (((Point)localObject1).x + 15 < arrayOfRectangle3[i4].x + arrayOfRectangle3[i4].width)
                          arrayOfRectangle3[i4].width = (((Point)localObject1).x - arrayOfRectangle3[i4].x);
                      }
                      else if (((Point)localObject2).x + 15 < arrayOfRectangle3[i4].x + arrayOfRectangle3[i4].width)
                        arrayOfRectangle3[i4].width = (((Point)localObject2).x - arrayOfRectangle3[i4].x);
                      break;
                    case 1:
                      break;
                    case 2:
                      if (i1 != 0)
                      {
                        if (((Point)localObject1).y + 15 < arrayOfRectangle3[i4].y + arrayOfRectangle3[i4].height)
                          arrayOfRectangle3[i4].height = (((Point)localObject1).y - arrayOfRectangle3[i4].y);
                      }
                      else if (((Point)localObject2).y + 15 < arrayOfRectangle3[i4].y + arrayOfRectangle3[i4].height)
                        arrayOfRectangle3[i4].height = (((Point)localObject2).y - arrayOfRectangle3[i4].y);
                      break;
                    case 3:
                      if (i1 != 0)
                      {
                        if (((Point)localObject1).y + 15 < arrayOfRectangle3[i4].y + arrayOfRectangle3[i4].height)
                          arrayOfRectangle3[i4].height = (((Point)localObject1).y - arrayOfRectangle3[i4].y);
                      }
                      else if (((Point)localObject2).y + 15 < arrayOfRectangle3[i4].y + arrayOfRectangle3[i4].height)
                        arrayOfRectangle3[i4].height = (((Point)localObject2).y - arrayOfRectangle3[i4].y);
                      break;
                    }
                  }
                }
                this.areas.put(localObject3, arrayOfRectangle3);
              }
            }
          }
      else
        for (i = 0; i != paramArrayOfRectangle.length; i++)
          if (paramArrayOfRectangle[i] != null)
          {
            if (paramArrayOfRectangle[i].width < 0)
            {
              paramArrayOfRectangle[i].width = (-paramArrayOfRectangle[i].width);
              paramArrayOfRectangle[i].x -= paramArrayOfRectangle[i].width;
            }
            if (paramArrayOfRectangle[i].height < 0)
            {
              paramArrayOfRectangle[i].height = (-paramArrayOfRectangle[i].height);
              paramArrayOfRectangle[i].y -= paramArrayOfRectangle[i].height;
            }
            if (this.areas != null)
            {
              localObject1 = Integer.valueOf(paramInt);
              localObject2 = (Rectangle[])this.areas.get(localObject1);
              if (localObject2 != null)
              {
                int j = 0;
                int k = localObject2.length;
                for (m = 0; m < k; m++)
                  if ((localObject2[m] != null) && (localObject2[m] != null) && (localObject2[m].x == paramArrayOfRectangle[i].x) && (localObject2[m].y == paramArrayOfRectangle[i].y) && (localObject2[m].width == paramArrayOfRectangle[i].width) && (localObject2[m].height == paramArrayOfRectangle[i].height))
                  {
                    j = 1;
                    m = k;
                  }
                if (j == 0)
                {
                  m = localObject2.length + 1;
                  Rectangle[] arrayOfRectangle2 = new Rectangle[m];
                  for (i1 = 0; i1 < localObject2.length; i1++)
                    if (localObject2[i1] != null)
                      arrayOfRectangle2[(i1 + 1)] = new Rectangle(localObject2[i1].x, localObject2[i1].y, localObject2[i1].width, localObject2[i1].height);
                  localObject2 = arrayOfRectangle2;
                  localObject2[0] = paramArrayOfRectangle[i];
                }
                this.areas.put(localObject1, localObject2);
              }
              else
              {
                this.areas.put(localObject1, paramArrayOfRectangle);
              }
            }
            else
            {
              this.areas = new HashMap();
              localObject1 = Integer.valueOf(paramInt);
              localObject2 = new Rectangle[1];
              localObject2[0] = paramArrayOfRectangle[i];
              this.areas.put(localObject1, localObject2);
            }
          }
    }
  }

  public Rectangle[] getHighlightedAreas(int paramInt)
  {
    if (this.areas == null)
      return null;
    Integer localInteger = Integer.valueOf(paramInt);
    Rectangle[] arrayOfRectangle1 = (Rectangle[])this.areas.get(localInteger);
    if (arrayOfRectangle1 != null)
    {
      int i = arrayOfRectangle1.length;
      Rectangle[] arrayOfRectangle2 = new Rectangle[i];
      for (int j = 0; j < i; j++)
        if (arrayOfRectangle1[j] == null)
          arrayOfRectangle2[j] = null;
        else
          arrayOfRectangle2[j] = new Rectangle(arrayOfRectangle1[j].x, arrayOfRectangle1[j].y, arrayOfRectangle1[j].width, arrayOfRectangle1[j].height);
      return arrayOfRectangle2;
    }
    return null;
  }

  public void setLineAreas(Map paramMap)
  {
    this.lineAreas = paramMap;
  }

  public void setLineWritingMode(Map paramMap)
  {
    this.lineWritingMode = paramMap;
  }

  public Rectangle[] getLineAreas(int paramInt)
  {
    if (this.lineAreas == null)
      return null;
    Rectangle[] arrayOfRectangle1 = (Rectangle[])this.lineAreas.get(Integer.valueOf(paramInt));
    if (arrayOfRectangle1 == null)
      return null;
    int i = arrayOfRectangle1.length;
    Rectangle[] arrayOfRectangle2 = new Rectangle[i];
    for (int j = 0; j < i; j++)
      if (arrayOfRectangle1[j] == null)
        arrayOfRectangle2[j] = null;
      else
        arrayOfRectangle2[j] = new Rectangle(arrayOfRectangle1[j].x, arrayOfRectangle1[j].y, arrayOfRectangle1[j].width, arrayOfRectangle1[j].height);
    return arrayOfRectangle2;
  }

  public int[] getLineWritingMode(int paramInt)
  {
    if (this.lineWritingMode == null)
      return null;
    int[] arrayOfInt1 = (int[])this.lineWritingMode.get(Integer.valueOf(paramInt));
    if (arrayOfInt1 == null)
      return null;
    int i = arrayOfInt1.length;
    int[] arrayOfInt2 = new int[i];
    System.arraycopy(arrayOfInt1, 0, arrayOfInt2, 0, i);
    return arrayOfInt2;
  }

  private static Rectangle mergePartLines(Rectangle paramRectangle1, Rectangle paramRectangle2)
  {
    int i = paramRectangle2.x;
    int j = paramRectangle2.x + paramRectangle2.width;
    int k = paramRectangle2.y;
    int m = paramRectangle2.y + paramRectangle2.height;
    int n = paramRectangle1.x;
    int i1 = paramRectangle1.x + paramRectangle1.width;
    int i2 = paramRectangle1.y;
    int i3 = paramRectangle1.y + paramRectangle1.height;
    if (i < n)
      paramRectangle2.x = i;
    else
      paramRectangle2.x = n;
    if (k < i2)
      paramRectangle2.y = k;
    else
      paramRectangle2.y = i2;
    if (m > i3)
      paramRectangle2.height = (m - paramRectangle2.y);
    else
      paramRectangle2.height = (i3 - paramRectangle2.y);
    if (j > i1)
      paramRectangle2.width = (j - paramRectangle2.x);
    else
      paramRectangle2.width = (i1 - paramRectangle2.x);
    return paramRectangle2;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.text.TextLines
 * JD-Core Version:    0.6.2
 */