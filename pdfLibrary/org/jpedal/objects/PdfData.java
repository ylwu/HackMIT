package org.jpedal.objects;

import org.jpedal.utils.Fonts;

public class PdfData
{
  public static final int HORIZONTAL_LEFT_TO_RIGHT = 0;
  public static final int HORIZONTAL_RIGHT_TO_LEFT = 1;
  public static final int VERTICAL_TOP_TO_BOTTOM = 2;
  public static final int VERTICAL_BOTTOM_TO_TOP = 3;
  private int pointer = 0;
  private boolean widthIsEmbedded = false;
  public static final String marker = String.valueOf('\000');
  protected int max = 2000;
  public String[] contents = new String[this.max];
  public int[] f_writingMode = new int[this.max];
  public int[] text_length = new int[this.max];
  public int[] move_command = new int[this.max];
  public float[] f_character_spacing = new float[this.max];
  public int[] f_end_font_size = new int[this.max];
  public float[] space_width = new float[this.max];
  public float[] f_x1 = new float[this.max];
  public String[] colorTag = new String[this.max];
  public float[] f_x2 = new float[this.max];
  public float[] f_y1 = new float[this.max];
  public float[] f_y2 = new float[this.max];
  boolean isColorExtracted;

  public final int getRawTextElementCount()
  {
    return this.pointer;
  }

  public final void flushTextList(boolean paramBoolean)
  {
    if (!paramBoolean)
    {
      this.pointer = 0;
      this.max = 2000;
      this.contents = new String[this.max];
      this.f_writingMode = new int[this.max];
      this.text_length = new int[this.max];
      this.move_command = new int[this.max];
      this.f_character_spacing = new float[this.max];
      this.f_end_font_size = new int[this.max];
      this.space_width = new float[this.max];
      this.f_x1 = new float[this.max];
      this.f_x2 = new float[this.max];
      this.f_y1 = new float[this.max];
      this.f_y2 = new float[this.max];
      this.colorTag = new String[this.max];
    }
  }

  public final void addRawTextElement(float paramFloat1, int paramInt1, String paramString1, float paramFloat2, int paramInt2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, int paramInt3, StringBuffer paramStringBuffer, int paramInt4, String paramString2, boolean paramBoolean)
  {
    if (paramStringBuffer.length() > 0)
    {
      if (paramBoolean)
      {
        paramStringBuffer.insert(0, paramString1);
        paramStringBuffer.append(Fonts.fe);
      }
      if (this.isColorExtracted)
      {
        paramStringBuffer.insert(0, paramString2);
        paramStringBuffer.append("</color>");
      }
      this.f_writingMode[this.pointer] = paramInt1;
      this.text_length[this.pointer] = paramInt4;
      this.move_command[this.pointer] = paramInt3;
      this.f_character_spacing[this.pointer] = paramFloat1;
      this.f_x1[this.pointer] = paramFloat3;
      this.colorTag[this.pointer] = paramString2;
      this.f_x2[this.pointer] = paramFloat5;
      this.f_y1[this.pointer] = paramFloat4;
      this.f_y2[this.pointer] = paramFloat6;
      this.contents[this.pointer] = paramStringBuffer.toString();
      this.f_end_font_size[this.pointer] = paramInt2;
      this.space_width[this.pointer] = (paramFloat2 * 1000.0F);
      this.pointer += 1;
      if (this.pointer == this.max)
        resizeArrays(0);
    }
  }

  private void resizeArrays(int paramInt)
  {
    if (paramInt < 0)
    {
      this.max = (-paramInt);
      this.pointer = this.max;
    }
    else if (paramInt == 0)
    {
      if (this.max < 5000)
        this.max *= 5;
      else if (this.max < 10000)
        this.max *= 2;
      else
        this.max += 1000;
    }
    else
    {
      this.max = (this.contents.length + paramInt - 1);
      this.pointer = this.contents.length;
    }
    String[] arrayOfString = this.contents;
    this.contents = new String[this.max];
    System.arraycopy(arrayOfString, 0, this.contents, 0, this.pointer);
    int[] arrayOfInt = this.f_writingMode;
    this.f_writingMode = new int[this.max];
    this.f_writingMode = new int[this.max];
    System.arraycopy(arrayOfInt, 0, this.f_writingMode, 0, this.pointer);
    arrayOfString = this.colorTag;
    this.colorTag = new String[this.max];
    System.arraycopy(arrayOfString, 0, this.colorTag, 0, this.pointer);
    arrayOfInt = this.text_length;
    this.text_length = new int[this.max];
    System.arraycopy(arrayOfInt, 0, this.text_length, 0, this.pointer);
    arrayOfInt = this.move_command;
    this.move_command = new int[this.max];
    System.arraycopy(arrayOfInt, 0, this.move_command, 0, this.pointer);
    float[] arrayOfFloat = this.f_character_spacing;
    this.f_character_spacing = new float[this.max];
    System.arraycopy(arrayOfFloat, 0, this.f_character_spacing, 0, this.pointer);
    arrayOfInt = this.f_end_font_size;
    this.f_end_font_size = new int[this.max];
    System.arraycopy(arrayOfInt, 0, this.f_end_font_size, 0, this.pointer);
    arrayOfFloat = this.space_width;
    this.space_width = new float[this.max];
    System.arraycopy(arrayOfFloat, 0, this.space_width, 0, this.pointer);
    arrayOfFloat = this.f_x1;
    this.f_x1 = new float[this.max];
    System.arraycopy(arrayOfFloat, 0, this.f_x1, 0, this.pointer);
    arrayOfFloat = this.f_x2;
    this.f_x2 = new float[this.max];
    System.arraycopy(arrayOfFloat, 0, this.f_x2, 0, this.pointer);
    arrayOfFloat = this.f_y1;
    this.f_y1 = new float[this.max];
    System.arraycopy(arrayOfFloat, 0, this.f_y1, 0, this.pointer);
    arrayOfFloat = this.f_y2;
    this.f_y2 = new float[this.max];
    System.arraycopy(arrayOfFloat, 0, this.f_y2, 0, this.pointer);
  }

  public void widthIsEmbedded()
  {
    this.widthIsEmbedded = true;
  }

  public boolean IsEmbedded()
  {
    return this.widthIsEmbedded;
  }

  public void enableTextColorDataExtraction()
  {
    this.isColorExtracted = true;
  }

  public boolean isColorExtracted()
  {
    return this.isColorExtracted;
  }

  public void dispose()
  {
    this.contents = null;
    this.f_writingMode = null;
    this.text_length = null;
    this.move_command = null;
    this.f_character_spacing = null;
    this.f_end_font_size = null;
    this.space_width = null;
    this.f_x1 = null;
    this.colorTag = null;
    this.f_x2 = null;
    this.f_y1 = null;
    this.f_y2 = null;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.PdfData
 * JD-Core Version:    0.6.2
 */