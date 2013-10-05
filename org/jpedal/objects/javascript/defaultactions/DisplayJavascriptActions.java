package org.jpedal.objects.javascript.defaultactions;

import java.util.StringTokenizer;

public class DisplayJavascriptActions
{
  public static final int visible = 0;
  public static final int hidden = 1;
  public static final int noPrint = 2;
  public static final int noView = 3;
  public static final int notHidden = 4;
  public static final float[] transparent = new float[0];
  public static final float[] black = { 0.0F };
  public static final float[] white = { 1.0F };
  public static final float[] red = { 1.0F, 0.0F, 0.0F };
  public static final float[] green = { 0.0F, 1.0F, 0.0F };
  public static final float[] blue = { 0.0F, 0.0F, 1.0F };
  public static final float[] cyan = { 1.0F, 0.0F, 0.0F, 0.0F };
  public static final float[] magenta = { 0.0F, 1.0F, 0.0F, 0.0F };
  public static final float[] yellow = { 0.0F, 0.0F, 1.0F, 0.0F };
  public static final float[] dkGray = { 0.25F };
  public static final float[] gray = { 0.5F };
  public static final float[] ltGray = { 0.75F };

  public static float[] convertToColorFloatArray(String paramString)
  {
    StringTokenizer localStringTokenizer = new StringTokenizer(paramString, "[,]\"");
    float[] arrayOfFloat = new float[localStringTokenizer.countTokens() - 1];
    for (int i = 0; i < arrayOfFloat.length; i++)
      arrayOfFloat[i] = Float.parseFloat(localStringTokenizer.nextToken());
    return arrayOfFloat;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.javascript.defaultactions.DisplayJavascriptActions
 * JD-Core Version:    0.6.2
 */