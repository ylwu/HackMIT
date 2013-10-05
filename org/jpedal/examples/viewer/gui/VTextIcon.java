package org.jpedal.examples.viewer.gui;

import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Icon;

public class VTextIcon
  implements Icon, PropertyChangeListener
{
  String fLabel;
  String[] fCharStrings;
  int[] fCharWidths;
  int[] fPosition;
  int fWidth;
  int fHeight;
  int fCharHeight;
  int fDescent;
  int fRotation;
  Component fComponent;
  static final int POSITION_NORMAL = 0;
  static final int POSITION_TOP_RIGHT = 1;
  static final int POSITION_FAR_TOP_RIGHT = 2;
  public static final int ROTATE_DEFAULT = 0;
  public static final int ROTATE_NONE = 1;
  public static final int ROTATE_LEFT = 2;
  public static final int ROTATE_RIGHT = 4;
  static final String sDrawsInTopRight = "ぁぃぅぇぉっゃゅょゎァィゥェォッャュョヮヵヶ";
  static final String sDrawsInFarTopRight = "、。";
  static final int DEFAULT_CJK = 1;
  static final int LEGAL_ROMAN = 7;
  static final int DEFAULT_ROMAN = 4;
  static final int LEGAL_MUST_ROTATE = 6;
  static final int DEFAULT_MUST_ROTATE = 2;
  static final double NINETY_DEGREES = Math.toRadians(90.0D);
  static final int kBufferSpace = 5;

  public VTextIcon(Component paramComponent, String paramString)
  {
    this(paramComponent, paramString, 0);
  }

  public VTextIcon(Component paramComponent, String paramString, int paramInt)
  {
    this.fComponent = paramComponent;
    this.fLabel = paramString;
    this.fRotation = verifyRotation(paramString, paramInt);
    calcDimensions();
    this.fComponent.addPropertyChangeListener(this);
  }

  public String toString()
  {
    return this.fLabel;
  }

  public void setLabel(String paramString)
  {
    this.fLabel = paramString;
    this.fRotation = verifyRotation(paramString, this.fRotation);
    recalcDimensions();
  }

  public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
  {
    String str = paramPropertyChangeEvent.getPropertyName();
    if ("font".equals(str))
      recalcDimensions();
  }

  void recalcDimensions()
  {
    int i = this.fWidth;
    int j = this.fHeight;
    calcDimensions();
    if ((i != this.fWidth) || (j != this.fHeight))
      this.fComponent.invalidate();
  }

  void calcDimensions()
  {
    FontMetrics localFontMetrics = this.fComponent.getFontMetrics(this.fComponent.getFont());
    this.fCharHeight = (localFontMetrics.getAscent() + localFontMetrics.getDescent());
    this.fDescent = localFontMetrics.getDescent();
    if (this.fRotation == 1)
    {
      int i = this.fLabel.length();
      char[] arrayOfChar = new char[i];
      this.fLabel.getChars(0, i, arrayOfChar, 0);
      this.fWidth = 0;
      this.fCharStrings = new String[i];
      this.fCharWidths = new int[i];
      this.fPosition = new int[i];
      for (int j = 0; j < i; j++)
      {
        char c = arrayOfChar[j];
        this.fCharWidths[j] = localFontMetrics.charWidth(c);
        if (this.fCharWidths[j] > this.fWidth)
          this.fWidth = this.fCharWidths[j];
        this.fCharStrings[j] = new String(arrayOfChar, j, 1);
        if ("ぁぃぅぇぉっゃゅょゎァィゥェォッャュョヮヵヶ".indexOf(c) >= 0)
          this.fPosition[j] = 1;
        else if ("、。".indexOf(c) >= 0)
          this.fPosition[j] = 2;
        else
          this.fPosition[j] = 0;
      }
      this.fHeight = (this.fCharHeight * i + this.fDescent);
    }
    else
    {
      this.fWidth = this.fCharHeight;
      this.fHeight = (localFontMetrics.stringWidth(this.fLabel) + 10);
    }
  }

  public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2)
  {
    paramGraphics.setColor(paramComponent.getForeground());
    paramGraphics.setFont(paramComponent.getFont());
    if (this.fRotation == 1)
    {
      int i = paramInt2 + this.fCharHeight;
      for (int j = 0; j < this.fCharStrings.length; j++)
      {
        int k;
        switch (this.fPosition[j])
        {
        case 0:
          paramGraphics.drawString(this.fCharStrings[j], paramInt1 + (this.fWidth - this.fCharWidths[j]) / 2, i);
          break;
        case 1:
          k = this.fCharHeight / 3;
          paramGraphics.drawString(this.fCharStrings[j], paramInt1 + k / 2, i - k);
          break;
        case 2:
          k = this.fCharHeight - this.fCharHeight / 3;
          paramGraphics.drawString(this.fCharStrings[j], paramInt1 + k / 2, i - k);
        }
        i += this.fCharHeight;
      }
    }
    else if (this.fRotation == 2)
    {
      paramGraphics.translate(paramInt1 + this.fWidth, paramInt2 + this.fHeight);
      ((Graphics2D)paramGraphics).rotate(-NINETY_DEGREES);
      paramGraphics.drawString(this.fLabel, 5, -this.fDescent);
      ((Graphics2D)paramGraphics).rotate(NINETY_DEGREES);
      paramGraphics.translate(-(paramInt1 + this.fWidth), -(paramInt2 + this.fHeight));
    }
    else if (this.fRotation == 4)
    {
      paramGraphics.translate(paramInt1, paramInt2);
      ((Graphics2D)paramGraphics).rotate(NINETY_DEGREES);
      paramGraphics.drawString(this.fLabel, 5, -this.fDescent);
      ((Graphics2D)paramGraphics).rotate(-NINETY_DEGREES);
      paramGraphics.translate(-paramInt1, -paramInt2);
    }
  }

  public int getIconWidth()
  {
    return this.fWidth;
  }

  public int getIconHeight()
  {
    return this.fHeight;
  }

  public static int verifyRotation(String paramString, int paramInt)
  {
    int i = 0;
    int j = 0;
    int k = paramString.length();
    char[] arrayOfChar = new char[k];
    paramString.getChars(0, k, arrayOfChar, 0);
    for (int n = 0; n < k; n++)
    {
      int m = arrayOfChar[n];
      if (((m >= 19968) && (m <= 40959)) || ((m >= 13312) && (m <= 19967)) || ((m >= 63744) && (m <= 64255)) || ((m >= 12352) && (m <= 12447)) || ((m >= 12448) && (m <= 12543)))
        i = 1;
      if (((m >= 1424) && (m <= 1535)) || ((m >= 1536) && (m <= 1791)) || ((m >= 1792) && (m <= 1871)))
        j = 1;
    }
    if (i != 0)
      return 1;
    n = j != 0 ? 6 : 7;
    if ((paramInt & n) > 0)
      return paramInt;
    return j != 0 ? 2 : 4;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.gui.VTextIcon
 * JD-Core Version:    0.6.2
 */