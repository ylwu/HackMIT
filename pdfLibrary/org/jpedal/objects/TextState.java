package org.jpedal.objects;

import org.jpedal.utils.StringUtils;

public class TextState
{
  float kerningAdded = 0.0F;
  public int writingMode = 0;
  private float[][] TmAtStart = new float[3][3];
  private float[][] TmAtStartNoRotation = new float[3][3];
  public float[][] Tm = new float[3][3];
  public float[][] TmNoRotation = new float[3][3];
  private String font_ID = "";
  private float TL = 0.0F;
  private float character_spacing = 0.0F;
  private float Tfs = 0.0F;
  private float text_rise = 0.0F;
  private float th = 1.0F;
  private float word_spacing;
  private boolean hasFontChanged = false;

  public TextState()
  {
    this.Tm[0][0] = 1.0F;
    this.Tm[0][1] = 0.0F;
    this.Tm[0][2] = 0.0F;
    this.Tm[1][0] = 0.0F;
    this.Tm[1][1] = 1.0F;
    this.Tm[1][2] = 0.0F;
    this.Tm[2][0] = 0.0F;
    this.Tm[2][1] = 0.0F;
    this.Tm[2][2] = 1.0F;
    this.TmAtStart[0][0] = 1.0F;
    this.TmAtStart[0][1] = 0.0F;
    this.TmAtStart[0][2] = 0.0F;
    this.TmAtStart[1][0] = 0.0F;
    this.TmAtStart[1][1] = 1.0F;
    this.TmAtStart[1][2] = 0.0F;
    this.TmAtStart[2][0] = 0.0F;
    this.TmAtStart[2][1] = 0.0F;
    this.TmAtStart[2][2] = 1.0F;
    this.TmNoRotation[0][0] = 1.0F;
    this.TmNoRotation[0][1] = 0.0F;
    this.TmNoRotation[0][2] = 0.0F;
    this.TmNoRotation[1][0] = 0.0F;
    this.TmNoRotation[1][1] = 1.0F;
    this.TmNoRotation[1][2] = 0.0F;
    this.TmNoRotation[2][0] = 0.0F;
    this.TmNoRotation[2][1] = 0.0F;
    this.TmNoRotation[2][2] = 1.0F;
  }

  public float[][] getTMAtLineStart()
  {
    return this.TmAtStart;
  }

  public void setTMAtLineStart()
  {
    this.TmAtStart[0][0] = this.Tm[0][0];
    this.TmAtStart[0][1] = this.Tm[0][1];
    this.TmAtStart[0][2] = this.Tm[0][2];
    this.TmAtStart[1][0] = this.Tm[1][0];
    this.TmAtStart[1][1] = this.Tm[1][1];
    this.TmAtStart[1][2] = this.Tm[1][2];
    this.TmAtStart[2][0] = this.Tm[2][0];
    this.TmAtStart[2][1] = this.Tm[2][1];
    this.TmAtStart[2][2] = this.Tm[2][2];
  }

  public float[][] getTMAtLineStartNoRotation()
  {
    return this.TmAtStartNoRotation;
  }

  public void setTMAtLineStartNoRotation()
  {
    this.TmAtStartNoRotation[0][0] = this.TmNoRotation[0][0];
    this.TmAtStartNoRotation[0][1] = this.TmNoRotation[0][1];
    this.TmAtStartNoRotation[0][2] = this.TmNoRotation[0][2];
    this.TmAtStartNoRotation[1][0] = this.TmNoRotation[1][0];
    this.TmAtStartNoRotation[1][1] = this.TmNoRotation[1][1];
    this.TmAtStartNoRotation[1][2] = this.TmNoRotation[1][2];
    this.TmAtStartNoRotation[2][0] = this.TmNoRotation[2][0];
    this.TmAtStartNoRotation[2][1] = this.TmNoRotation[2][1];
    this.TmAtStartNoRotation[2][2] = this.TmNoRotation[2][2];
  }

  public final void setHorizontalScaling(float paramFloat)
  {
    this.th = paramFloat;
  }

  public final String getFontID()
  {
    return this.font_ID;
  }

  public final float getTextRise()
  {
    return this.text_rise;
  }

  public final float getCharacterSpacing()
  {
    return this.character_spacing;
  }

  public final float getWordSpacing()
  {
    return this.word_spacing;
  }

  public final void setLeading(float paramFloat)
  {
    this.TL = paramFloat;
  }

  public final float getTfs()
  {
    return this.Tfs;
  }

  public final float setTfs(float paramFloat)
  {
    return this.Tfs = paramFloat;
  }

  public final float getHorizontalScaling()
  {
    return this.th;
  }

  public final void setTextRise(float paramFloat)
  {
    this.text_rise = paramFloat;
  }

  public final float getLeading()
  {
    return this.TL;
  }

  public final Object clone()
  {
    TextState localTextState = new TextState();
    localTextState.writingMode = this.writingMode;
    int i;
    if (this.TmAtStart != null)
      for (i = 0; i < 3; i++)
        System.arraycopy(this.TmAtStart[i], 0, localTextState.TmAtStart[i], 0, 3);
    if (this.TmAtStartNoRotation != null)
      for (i = 0; i < 3; i++)
        System.arraycopy(this.TmAtStartNoRotation[i], 0, localTextState.TmAtStartNoRotation[i], 0, 3);
    if (this.Tm != null)
      for (i = 0; i < 3; i++)
        System.arraycopy(this.Tm[i], 0, localTextState.Tm[i], 0, 3);
    if (this.TmNoRotation != null)
      for (i = 0; i < 3; i++)
        System.arraycopy(this.TmNoRotation[i], 0, localTextState.TmNoRotation[i], 0, 3);
    if (this.font_ID != null)
      localTextState.font_ID = new String(StringUtils.toBytes(this.font_ID));
    localTextState.TL = this.TL;
    localTextState.character_spacing = this.character_spacing;
    localTextState.Tfs = this.Tfs;
    localTextState.text_rise = this.text_rise;
    localTextState.th = this.th;
    localTextState.word_spacing = this.word_spacing;
    localTextState.hasFontChanged = this.hasFontChanged;
    return localTextState;
  }

  public final void setWordSpacing(float paramFloat)
  {
    this.word_spacing = paramFloat;
  }

  public final void setCharacterSpacing(float paramFloat)
  {
    this.character_spacing = paramFloat;
  }

  public final void resetTm()
  {
    this.Tm[0][0] = 1.0F;
    this.Tm[0][1] = 0.0F;
    this.Tm[0][2] = 0.0F;
    this.Tm[1][0] = 0.0F;
    this.Tm[1][1] = 1.0F;
    this.Tm[1][2] = 0.0F;
    this.Tm[2][0] = 0.0F;
    this.Tm[2][1] = 0.0F;
    this.Tm[2][2] = 1.0F;
    this.TmNoRotation[0][0] = 1.0F;
    this.TmNoRotation[0][1] = 0.0F;
    this.TmNoRotation[0][2] = 0.0F;
    this.TmNoRotation[1][0] = 0.0F;
    this.TmNoRotation[1][1] = 1.0F;
    this.TmNoRotation[1][2] = 0.0F;
    this.TmNoRotation[2][0] = 0.0F;
    this.TmNoRotation[2][1] = 0.0F;
    this.TmNoRotation[2][2] = 1.0F;
    setTMAtLineStart();
  }

  public boolean hasFontChanged()
  {
    return this.hasFontChanged;
  }

  public void setFontChanged(boolean paramBoolean)
  {
    this.hasFontChanged = paramBoolean;
  }

  public void TF(float paramFloat, String paramString)
  {
    this.Tfs = paramFloat;
    this.font_ID = paramString;
    this.hasFontChanged = true;
  }

  public void setLastKerningAdded(float paramFloat)
  {
    this.kerningAdded = paramFloat;
  }

  public float getLastKerningAdded()
  {
    return this.kerningAdded;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.TextState
 * JD-Core Version:    0.6.2
 */