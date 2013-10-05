package org.jpedal.color;

public class DeviceRGBColorSpace extends GenericColorSpace
{
  private static final long serialVersionUID = -7269417965203263694L;

  public DeviceRGBColorSpace()
  {
    this.value = 1785221209;
  }

  public final void setColor(String[] paramArrayOfString, int paramInt)
  {
    float[] arrayOfFloat = new float[paramInt];
    for (int i = 0; i < paramInt; i++)
      arrayOfFloat[i] = Float.parseFloat(paramArrayOfString[i]);
    setColor(arrayOfFloat, paramInt);
  }

  public final void setColor(float[] paramArrayOfFloat, int paramInt)
  {
    Object localObject;
    int j;
    if (paramInt == 1)
    {
      if (this.IndexedColorMap == null)
      {
        this.currentColor = new PdfColor(paramArrayOfFloat[0], paramArrayOfFloat[0], paramArrayOfFloat[0]);
      }
      else
      {
        localObject = new int[3];
        j = (int)(paramArrayOfFloat[0] * 3.0F);
        for (int k = 0; k < 3; k++)
        {
          int i = getIndexedColorComponent(j + k);
          localObject[k] = i;
        }
        this.currentColor = new PdfColor(localObject[0], localObject[1], localObject[2]);
      }
    }
    else if (paramInt > 2)
    {
      localObject = new float[3];
      for (j = 0; j < 3; j++)
      {
        localObject[j] = paramArrayOfFloat[j];
        if (localObject[j] < 0.0F)
          localObject[j] = 0.0F;
        if (localObject[j] > 1.0F)
          localObject[j] = 1.0F;
      }
      this.currentColor = new PdfColor(localObject[0], localObject[1], localObject[2]);
    }
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.color.DeviceRGBColorSpace
 * JD-Core Version:    0.6.2
 */