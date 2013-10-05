package org.jpedal.objects;

import java.io.Serializable;
import org.jpedal.utils.repositories.Vector_Int;
import org.jpedal.utils.repositories.Vector_Object;

public class PdfPageData
  implements Serializable
{
  private boolean valuesSet = false;
  private int lastPage = -1;
  private int pagesRead = -1;
  private int pageCount = 1;
  private float[] defaultMediaBox = null;
  private int rotation = 0;
  private Vector_Object mediaBoxes = new Vector_Object(500);
  private Vector_Object cropBoxes = new Vector_Object(500);
  private Vector_Int rotations = null;
  private float cropBoxX = -99999.0F;
  private float cropBoxY = -1.0F;
  private float cropBoxW = -1.0F;
  private float cropBoxH = -1.0F;
  private float mediaBoxX = -1.0F;
  private float mediaBoxY;
  private float mediaBoxW;
  private float mediaBoxH;
  private boolean hasMultipleSizes = false;
  private boolean hasMultipleSizesSet = false;
  private float scalingValue = 1.0F;
  private float[] mediaBox;
  private float[] cropBox;
  private int defaultrotation;
  private float defaultcropBoxX;
  private float defaultcropBoxY;
  private float defaultcropBoxW;
  private float defaultcropBoxH;
  private float defaultmediaBoxX;
  private float defaultmediaBoxY;
  private float defaultmediaBoxW;
  private float defaultmediaBoxH;
  private PageOrigins pageOrigin = PageOrigins.BOTTOM_LEFT;

  public void checkSizeSet(int paramInt)
  {
    if (this.mediaBox == null)
      this.mediaBox = this.defaultMediaBox;
    if ((this.cropBox != null) && ((this.cropBox[0] != this.mediaBox[0]) || (this.cropBox[1] != this.mediaBox[1]) || (this.cropBox[2] != this.mediaBox[2]) || (this.cropBox[3] != this.mediaBox[3])))
    {
      this.mediaBoxes.setElementAt(this.mediaBox, paramInt);
      if ((this.cropBox[0] >= this.mediaBox[0]) && (this.cropBox[1] >= this.mediaBox[1]) && (this.cropBox[2] - this.cropBox[0] <= this.mediaBox[2] - this.mediaBox[0]) && (this.cropBox[3] - this.cropBox[1] <= this.mediaBox[3] - this.mediaBox[1]))
        this.cropBoxes.setElementAt(this.cropBox, paramInt);
    }
    else if ((this.mediaBox != null) && ((this.defaultMediaBox[0] != this.mediaBox[0]) || (this.defaultMediaBox[1] != this.mediaBox[1]) || (this.defaultMediaBox[2] != this.mediaBox[2]) || (this.defaultMediaBox[3] != this.mediaBox[3])))
    {
      this.mediaBoxes.setElementAt(this.mediaBox, paramInt);
    }
    if (this.pagesRead < paramInt)
      this.pagesRead = paramInt;
    this.lastPage = -1;
    this.mediaBox = null;
    this.cropBox = null;
  }

  public final int getMediaBoxHeight(int paramInt)
  {
    setSizeForPage(paramInt);
    return (int)this.mediaBoxH;
  }

  public final int getMediaBoxY(int paramInt)
  {
    setSizeForPage(paramInt);
    return (int)this.mediaBoxY;
  }

  public final int getMediaBoxX(int paramInt)
  {
    setSizeForPage(paramInt);
    return (int)this.mediaBoxX;
  }

  public void setMediaBox(float[] paramArrayOfFloat)
  {
    this.mediaBox = paramArrayOfFloat;
    this.cropBox = null;
    if (this.defaultMediaBox == null)
      this.defaultMediaBox = paramArrayOfFloat;
  }

  public void setCropBox(float[] paramArrayOfFloat)
  {
    this.cropBox = paramArrayOfFloat;
    int i = 1;
    if ((i != 0) && (this.mediaBox != null) && (this.mediaBox.length >= 4))
    {
      if (paramArrayOfFloat[0] < this.mediaBox[0])
        paramArrayOfFloat[0] = this.mediaBox[0];
      if (paramArrayOfFloat[1] < this.mediaBox[1])
        paramArrayOfFloat[1] = this.mediaBox[1];
      if (paramArrayOfFloat[2] > this.mediaBox[2])
        paramArrayOfFloat[2] = this.mediaBox[2];
      if (paramArrayOfFloat[3] > this.mediaBox[3])
        paramArrayOfFloat[3] = this.mediaBox[3];
    }
  }

  public void setPageRotation(int paramInt1, int paramInt2)
  {
    int i = paramInt1;
    if (i < 0)
      i = 360 + i;
    if ((i != 0) || (this.rotations != null))
    {
      if (this.rotations == null)
        if (paramInt2 < 2000)
          this.rotations = new Vector_Int(2000);
        else
          this.rotations = new Vector_Int(paramInt2 * 2);
      this.rotations.setElementAt(i, paramInt2);
    }
  }

  public final int getMediaBoxWidth(int paramInt)
  {
    setSizeForPage(paramInt);
    return (int)this.mediaBoxW;
  }

  public String getMediaValue(int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    float[] arrayOfFloat = this.defaultMediaBox;
    if (this.mediaBoxes != null)
      arrayOfFloat = (float[])this.mediaBoxes.elementAt(paramInt);
    if (arrayOfFloat != null)
      for (int i = 0; i < 4; i++)
      {
        localStringBuilder.append(arrayOfFloat[i]);
        localStringBuilder.append(' ');
      }
    return localStringBuilder.toString();
  }

  public String getCropValue(int paramInt)
  {
    float[] arrayOfFloat = null;
    if (this.cropBoxes != null)
      arrayOfFloat = (float[])this.cropBoxes.elementAt(paramInt);
    else if (arrayOfFloat != null)
      arrayOfFloat = (float[])this.mediaBoxes.elementAt(paramInt);
    if (arrayOfFloat == null)
      arrayOfFloat = this.defaultMediaBox;
    StringBuilder localStringBuilder = new StringBuilder();
    for (int i = 0; i < 4; i++)
    {
      localStringBuilder.append(arrayOfFloat[i]);
      localStringBuilder.append(' ');
    }
    return localStringBuilder.toString();
  }

  public int getScaledCropBoxX(int paramInt)
  {
    setSizeForPage(paramInt);
    return roundFloat(this.cropBoxX * this.scalingValue);
  }

  public int getScaledCropBoxWidth(int paramInt)
  {
    setSizeForPage(paramInt);
    return roundFloat(this.cropBoxW * this.scalingValue);
  }

  public int getScaledCropBoxY(int paramInt)
  {
    setSizeForPage(paramInt);
    return roundFloat(this.cropBoxY * this.scalingValue);
  }

  public int getScaledCropBoxHeight(int paramInt)
  {
    setSizeForPage(paramInt);
    return roundFloat(this.cropBoxH * this.scalingValue);
  }

  public int getCropBoxX(int paramInt)
  {
    setSizeForPage(paramInt);
    return (int)this.cropBoxX;
  }

  public float getCropBoxX2D(int paramInt)
  {
    setSizeForPage(paramInt);
    return this.cropBoxX;
  }

  public int getCropBoxWidth(int paramInt)
  {
    setSizeForPage(paramInt);
    return (int)this.cropBoxW;
  }

  public float getCropBoxWidth2D(int paramInt)
  {
    setSizeForPage(paramInt);
    return this.cropBoxW;
  }

  public int getCropBoxY(int paramInt)
  {
    setSizeForPage(paramInt);
    return (int)this.cropBoxY;
  }

  public float getCropBoxY2D(int paramInt)
  {
    setSizeForPage(paramInt);
    return this.cropBoxY;
  }

  public int getCropBoxHeight(int paramInt)
  {
    setSizeForPage(paramInt);
    return (int)this.cropBoxH;
  }

  public float getCropBoxHeight2D(int paramInt)
  {
    setSizeForPage(paramInt);
    return this.cropBoxH;
  }

  private synchronized void setSizeForPage(int paramInt)
  {
    if (paramInt == this.lastPage)
      return;
    if (paramInt > this.pageCount)
      this.pageCount = paramInt;
    if (paramInt > this.pagesRead)
    {
      this.mediaBoxX = 0.0F;
      this.mediaBoxY = 0.0F;
      this.mediaBoxW = 0.0F;
      this.mediaBoxH = 0.0F;
      this.cropBoxX = 0.0F;
      this.cropBoxY = 0.0F;
      this.cropBoxW = 0.0F;
      this.cropBoxH = 0.0F;
      this.lastPage = paramInt;
    }
    else if ((paramInt > 0) && (this.lastPage != paramInt))
    {
      this.lastPage = paramInt;
      int i = 0;
      float[] arrayOfFloat1 = (float[])this.cropBoxes.elementAt(paramInt);
      float[] arrayOfFloat2 = (float[])this.mediaBoxes.elementAt(paramInt);
      if ((arrayOfFloat2 == null) && (this.defaultMediaBox != null))
      {
        arrayOfFloat2 = this.defaultMediaBox;
        i = 1;
      }
      if (this.rotations != null)
        this.rotation = this.rotations.elementAt(paramInt);
      for (this.rotation = this.defaultrotation; this.rotation >= 360; this.rotation -= 360);
      if ((this.valuesSet) && (i != 0))
      {
        this.cropBoxX = this.defaultcropBoxX;
        this.mediaBoxX = this.defaultmediaBoxX;
        this.cropBoxY = this.defaultcropBoxY;
        this.mediaBoxY = this.defaultmediaBoxY;
        this.cropBoxW = this.defaultcropBoxW;
        this.mediaBoxW = this.defaultmediaBoxW;
        this.cropBoxH = this.defaultcropBoxH;
        this.mediaBoxH = this.defaultmediaBoxH;
      }
      else
      {
        this.mediaBoxX = 0.0F;
        this.mediaBoxY = 0.0F;
        this.mediaBoxW = 800.0F;
        this.mediaBoxH = 800.0F;
        if (arrayOfFloat2 != null)
        {
          this.mediaBoxX = arrayOfFloat2[0];
          this.mediaBoxY = arrayOfFloat2[1];
          this.mediaBoxW = (arrayOfFloat2[2] - this.mediaBoxX);
          this.mediaBoxH = (arrayOfFloat2[3] - this.mediaBoxY);
          if ((this.mediaBoxY > 0.0F) && (this.mediaBoxH == -this.mediaBoxY))
          {
            this.mediaBoxH = (-this.mediaBoxH);
            this.mediaBoxY = 0.0F;
          }
        }
        if (arrayOfFloat1 != null)
        {
          this.cropBoxX = arrayOfFloat1[0];
          this.cropBoxY = arrayOfFloat1[1];
          this.cropBoxW = arrayOfFloat1[2];
          this.cropBoxH = arrayOfFloat1[3];
          float f;
          if (this.cropBoxX > this.cropBoxW)
          {
            f = this.cropBoxX;
            this.cropBoxX = this.cropBoxW;
            this.cropBoxW = f;
          }
          if (this.cropBoxY > this.cropBoxH)
          {
            f = this.cropBoxY;
            this.cropBoxY = this.cropBoxH;
            this.cropBoxH = f;
          }
          this.cropBoxW -= this.cropBoxX;
          this.cropBoxH -= this.cropBoxY;
          if ((this.cropBoxY > 0.0F) && (this.cropBoxH == -this.cropBoxY))
          {
            this.cropBoxH = (-this.cropBoxH);
            this.cropBoxY = 0.0F;
          }
        }
        else
        {
          this.cropBoxX = this.mediaBoxX;
          this.cropBoxY = this.mediaBoxY;
          this.cropBoxW = this.mediaBoxW;
          this.cropBoxH = this.mediaBoxH;
        }
      }
      if (this.cropBoxH < 0.0F)
      {
        this.cropBoxY += this.cropBoxH;
        this.cropBoxH = (-this.cropBoxH);
      }
      if (this.cropBoxW < 0.0F)
      {
        this.cropBoxX += this.cropBoxW;
        this.cropBoxW = (-this.cropBoxW);
      }
      if ((i != 0) && (!this.valuesSet))
      {
        this.defaultrotation = this.rotation;
        this.defaultcropBoxX = this.cropBoxX;
        this.defaultmediaBoxX = this.mediaBoxX;
        this.defaultcropBoxY = this.cropBoxY;
        this.defaultmediaBoxY = this.mediaBoxY;
        this.defaultcropBoxW = this.cropBoxW;
        this.defaultmediaBoxW = this.mediaBoxW;
        this.defaultcropBoxH = this.cropBoxH;
        this.defaultmediaBoxH = this.mediaBoxH;
        this.valuesSet = true;
      }
    }
  }

  public float getScalingValue()
  {
    return this.scalingValue;
  }

  public void setScalingValue(float paramFloat)
  {
    this.scalingValue = paramFloat;
  }

  private static int roundFloat(float paramFloat)
  {
    int i = (int)paramFloat;
    int j = 1;
    if (j != 0)
    {
      float f = paramFloat - i;
      if (f > 0.3D)
        i += 1;
    }
    return i;
  }

  public final int getPageCount()
  {
    return this.pageCount;
  }

  public final int getRotation(int paramInt)
  {
    setSizeForPage(paramInt);
    return this.rotation;
  }

  public final int getScaledMediaBoxHeight(int paramInt)
  {
    setSizeForPage(paramInt);
    return roundFloat(this.mediaBoxH * this.scalingValue);
  }

  public final int getScaledMediaBoxWidth(int paramInt)
  {
    setSizeForPage(paramInt);
    return roundFloat(this.mediaBoxW * this.scalingValue);
  }

  public final int getScaledMediaBoxX(int paramInt)
  {
    setSizeForPage(paramInt);
    return roundFloat(this.mediaBoxX * this.scalingValue);
  }

  public final int getScaledMediaBoxY(int paramInt)
  {
    setSizeForPage(paramInt);
    return roundFloat(this.mediaBoxY * this.scalingValue);
  }

  public boolean hasMultipleSizes()
  {
    if (this.hasMultipleSizesSet)
      return this.hasMultipleSizes;
    int i = this.pageCount;
    int j = getCropBoxWidth(1);
    int k = getCropBoxHeight(1);
    int m = getRotation(1);
    if (i > 1)
      for (int n = 2; n < i + 1; n++)
        if ((j != getCropBoxWidth(n)) || (k != getCropBoxHeight(n)) || (m != getRotation(n)))
        {
          n = i;
          this.hasMultipleSizes = true;
        }
    this.hasMultipleSizesSet = true;
    return this.hasMultipleSizes;
  }

  public void setOrigin(PageOrigins paramPageOrigins)
  {
    this.pageOrigin = paramPageOrigins;
  }

  public PageOrigins getOrigin()
  {
    return this.pageOrigin;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.PdfPageData
 * JD-Core Version:    0.6.2
 */