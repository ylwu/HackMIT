package org.jpedal.utils;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.Map;

public class ObjectCloneFactory
{
  public static int[] cloneArray(int[] paramArrayOfInt)
  {
    if (paramArrayOfInt == null)
      return null;
    int i = paramArrayOfInt.length;
    int[] arrayOfInt = new int[i];
    System.arraycopy(paramArrayOfInt, 0, arrayOfInt, 0, i);
    return arrayOfInt;
  }

  public static float[] cloneArray(float[] paramArrayOfFloat)
  {
    if (paramArrayOfFloat == null)
      return null;
    int i = paramArrayOfFloat.length;
    float[] arrayOfFloat = new float[i];
    System.arraycopy(paramArrayOfFloat, 0, arrayOfFloat, 0, i);
    return arrayOfFloat;
  }

  public static byte[] cloneArray(byte[] paramArrayOfByte)
  {
    if (paramArrayOfByte == null)
      return null;
    int i = paramArrayOfByte.length;
    byte[] arrayOfByte = new byte[i];
    System.arraycopy(paramArrayOfByte, 0, arrayOfByte, 0, i);
    return arrayOfByte;
  }

  public static byte[][] cloneDoubleArray(byte[][] paramArrayOfByte)
  {
    if (paramArrayOfByte == null)
      return (byte[][])null;
    byte[][] arrayOfByte = new byte[paramArrayOfByte.length][];
    for (int i = 0; i < paramArrayOfByte.length; i++)
      arrayOfByte[i] = ((byte[])paramArrayOfByte[i].clone());
    return arrayOfByte;
  }

  public static BufferedImage deepCopy(BufferedImage paramBufferedImage)
  {
    if (paramBufferedImage == null)
      return null;
    ColorModel localColorModel = paramBufferedImage.getColorModel();
    boolean bool = localColorModel.isAlphaPremultiplied();
    WritableRaster localWritableRaster = paramBufferedImage.copyData(null);
    return new BufferedImage(localColorModel, localWritableRaster, bool, null);
  }

  public static Map cloneMap(Map paramMap)
  {
    if (paramMap != null)
      try
      {
        Map localMap = (Map)paramMap.getClass().newInstance();
        localMap.putAll(paramMap);
        return localMap;
      }
      catch (Exception localException)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog("Exception: " + localException.getMessage());
      }
    return null;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.utils.ObjectCloneFactory
 * JD-Core Version:    0.6.2
 */