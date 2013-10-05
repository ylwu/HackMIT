package org.jpedal.fonts.glyph;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import org.jpedal.fonts.StandardFonts;
import org.jpedal.fonts.objects.FontData;
import org.jpedal.gui.ShowGUIMessage;
import org.jpedal.render.DynamicVectorRenderer;
import org.jpedal.utils.LogWriter;

public class T1Glyphs extends PdfJavaGlyphs
{
  private static String nybChars = "0123456789.ee -";
  public boolean is1C = false;
  private String[] charForGlyphIndex;
  protected Map charStrings = new HashMap();
  protected Map glyphNumbers = new HashMap();
  int max = 100;
  double[] operandsRead = new double[this.max];
  int operandReached = 0;
  float[] pt;
  private double xs = -1.0D;
  private double ys = -1.0D;
  private double x = 0.0D;
  private double y = 0.0D;
  private int ptCount = 0;
  int currentOp = 0;
  private int hintCount = 0;
  private boolean allowAll = false;
  private double h;
  private boolean isCID;
  private int[] nominalWidthX = { 0 };
  private int[] defaultWidthX = { 0 };
  private boolean defaultWidthsPassed = false;
  private int[] fdSelect = null;
  DynamicVectorRenderer dynamicVectorRenderer = null;

  public T1Glyphs(boolean paramBoolean)
  {
    this.isCID = paramBoolean;
  }

  public T1Glyphs(boolean paramBoolean1, boolean paramBoolean2)
  {
    this.charForGlyphIndex = new String[65536];
  }

  public static String[] readFontNames(FontData paramFontData)
  {
    String[] arrayOfString = new String[1];
    arrayOfString[0] = null;
    BufferedReader localBufferedReader = new BufferedReader(new StringReader(new String(paramFontData.getBytes(0, paramFontData.length()))));
    String str1 = null;
    while (true)
    {
      try
      {
        str1 = localBufferedReader.readLine();
      }
      catch (IOException localIOException)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog("Exception: " + localIOException.getMessage());
      }
      if (str1 == null)
        break;
      if (str1.startsWith("/FontName"))
      {
        int i = str1.indexOf(47, 9);
        if (i != -1)
        {
          int j = str1.indexOf(32, i);
          if (j != -1)
          {
            String str2 = str1.substring(i + 1, j);
            arrayOfString[0] = str2.toLowerCase();
            break;
          }
        }
      }
    }
    if (localBufferedReader != null)
      try
      {
        localBufferedReader.close();
      }
      catch (Exception localException)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog("Exception " + localException + " closing stream");
      }
    if (paramFontData != null)
      paramFontData.close();
    return arrayOfString;
  }

  private boolean processFlex(GlyphFactory paramGlyphFactory, boolean paramBoolean1, int paramInt1, boolean paramBoolean2, int paramInt2)
  {
    if ((paramBoolean2) && (this.ptCount == 14) && (paramInt2 == 0))
    {
      paramBoolean2 = false;
      int i = 0;
      while (i < 12)
      {
        paramGlyphFactory.curveTo(this.pt[i], this.pt[(i + 1)], this.pt[(i + 2)], this.pt[(i + 3)], this.pt[(i + 4)], this.pt[(i + 5)]);
        if (paramBoolean1)
          System.out.println("t1 flex " + this.pt[i] + ' ' + this.pt[(i + 1)] + ' ' + this.pt[(i + 2)] + ' ' + this.pt[(i + 3)] + ' ' + this.pt[(i + 4)] + ' ' + this.pt[(i + 5)]);
        i += 6;
      }
    }
    else if ((!paramBoolean2) && (paramInt2 >= 0) && (paramInt2 <= 2))
    {
      paramBoolean2 = true;
      this.ptCount = 0;
      this.pt = new float[16];
      if (paramBoolean1)
        System.out.println("flex on " + paramInt1 + ' ' + paramInt2);
    }
    return paramBoolean2;
  }

  private void endchar(GlyphFactory paramGlyphFactory, int paramInt, boolean paramBoolean)
  {
    if (paramBoolean)
      System.out.println("Endchar");
    if (this.operandReached == 5)
    {
      this.operandReached -= 1;
      this.currentOp += 1;
    }
    if (this.operandReached == 4)
      endchar(paramGlyphFactory, paramInt);
    else
      paramGlyphFactory.closePath();
  }

  private int mask(boolean paramBoolean, int paramInt1, int paramInt2)
  {
    if (paramBoolean)
      System.out.println("hintmask/cntrmask " + paramInt2);
    this.hintCount += this.operandReached / 2;
    if (paramBoolean)
      System.out.println("hintCount=" + this.hintCount);
    int i = this.hintCount;
    while (i > 0)
    {
      paramInt1++;
      i -= 8;
    }
    return paramInt1;
  }

  private double sbw(boolean paramBoolean)
  {
    double d2 = this.operandsRead[(this.operandReached - 2)];
    this.y = d2;
    d2 = this.operandsRead[(this.operandReached - 1)];
    this.x = d2;
    this.xs = this.x;
    this.ys = this.y;
    this.allowAll = true;
    double d1 = this.y;
    this.h = this.operandsRead[(this.operandReached - 3)];
    if (paramBoolean)
      System.out.println("sbw xs,ys set to " + this.x + ' ' + this.y);
    return d1;
  }

  private void hmoveto(GlyphFactory paramGlyphFactory, boolean paramBoolean1, boolean paramBoolean2)
  {
    if ((paramBoolean2) && (this.operandReached == 2))
      this.currentOp += 1;
    double d = this.operandsRead[this.currentOp];
    this.x += d;
    paramGlyphFactory.moveTo((float)this.x, (float)this.y);
    this.xs = this.x;
    this.ys = this.y;
    if (paramBoolean1)
      System.out.println("reset xs,ys to " + this.x + ' ' + this.y);
    if (paramBoolean1)
      System.out.println("hmoveto " + this.x + ' ' + this.y);
  }

  private void rmoveto(GlyphFactory paramGlyphFactory, boolean paramBoolean1, boolean paramBoolean2)
  {
    if ((paramBoolean2) && (this.operandReached == 3))
      this.currentOp += 1;
    if (paramBoolean1)
      System.out.println(this.currentOp + " " + this.operandReached + ' ' + paramBoolean2 + " x,y=(" + this.x + ' ' + this.y + ") xs,ys=(" + this.xs + ' ' + this.ys + ") rmoveto " + this.operandsRead[this.currentOp] + ' ' + this.operandsRead[(this.currentOp + 1)]);
    double d = this.operandsRead[(this.currentOp + 1)];
    this.y += d;
    d = this.operandsRead[this.currentOp];
    this.x += d;
    paramGlyphFactory.moveTo((float)this.x, (float)this.y);
    this.xs = this.x;
    this.ys = this.y;
    if (paramBoolean1)
      System.out.println("xs,ys=(" + this.xs + ' ' + this.ys + ") x=" + this.x + " y=" + this.y);
  }

  private void vhhvcurveto(GlyphFactory paramGlyphFactory, boolean paramBoolean, int paramInt)
  {
    for (int i = paramInt == 31 ? 1 : 0; this.operandReached >= 4; i = i == 0 ? 1 : 0)
    {
      this.operandReached -= 4;
      if (i != 0)
        this.x += this.operandsRead[this.currentOp];
      else
        this.y += this.operandsRead[this.currentOp];
      this.pt[0] = ((float)this.x);
      this.pt[1] = ((float)this.y);
      this.x += this.operandsRead[(this.currentOp + 1)];
      this.y += this.operandsRead[(this.currentOp + 2)];
      this.pt[2] = ((float)this.x);
      this.pt[3] = ((float)this.y);
      if (i != 0)
      {
        this.y += this.operandsRead[(this.currentOp + 3)];
        if (this.operandReached == 1)
          this.x += this.operandsRead[(this.currentOp + 4)];
      }
      else
      {
        this.x += this.operandsRead[(this.currentOp + 3)];
        if (this.operandReached == 1)
          this.y += this.operandsRead[(this.currentOp + 4)];
      }
      this.pt[4] = ((float)this.x);
      this.pt[5] = ((float)this.y);
      paramGlyphFactory.curveTo(this.pt[0], this.pt[1], this.pt[2], this.pt[3], this.pt[4], this.pt[5]);
      if (paramBoolean)
        System.out.println(this.currentOp + "vh/hvCurveto " + this.operandsRead[this.currentOp] + ' ' + this.operandsRead[(this.currentOp + 1)] + ' ' + this.operandsRead[(this.currentOp + 2)] + ' ' + this.operandsRead[(this.currentOp + 3)] + ' ' + this.operandsRead[(this.currentOp + 4)] + ' ' + this.operandsRead[(this.currentOp + 5)]);
      this.currentOp += 4;
    }
  }

  private void vvhhcurveto(GlyphFactory paramGlyphFactory, boolean paramBoolean, int paramInt)
  {
    int i = paramInt == 26 ? 1 : 0;
    if ((this.operandReached & 0x1) == 1)
    {
      if (i != 0)
        this.x += this.operandsRead[0];
      else
        this.y += this.operandsRead[0];
      this.currentOp += 1;
    }
    while (this.currentOp < this.operandReached)
    {
      if (i != 0)
        this.y += this.operandsRead[this.currentOp];
      else
        this.x += this.operandsRead[this.currentOp];
      this.pt[0] = ((float)this.x);
      this.pt[1] = ((float)this.y);
      this.x += this.operandsRead[(this.currentOp + 1)];
      this.y += this.operandsRead[(this.currentOp + 2)];
      this.pt[2] = ((float)this.x);
      this.pt[3] = ((float)this.y);
      if (i != 0)
        this.y += this.operandsRead[(this.currentOp + 3)];
      else
        this.x += this.operandsRead[(this.currentOp + 3)];
      this.pt[4] = ((float)this.x);
      this.pt[5] = ((float)this.y);
      this.currentOp += 4;
      paramGlyphFactory.curveTo(this.pt[0], this.pt[1], this.pt[2], this.pt[3], this.pt[4], this.pt[5]);
      if (paramBoolean)
        System.out.println("vv/hhCurveto " + this.operandsRead[0] + ' ' + this.operandsRead[1] + ' ' + this.operandsRead[2] + ' ' + this.operandsRead[3] + ' ' + this.operandsRead[4] + ' ' + this.operandsRead[5]);
    }
  }

  private void rlinecurve(GlyphFactory paramGlyphFactory, boolean paramBoolean)
  {
    for (int i = (this.operandReached - 6) / 2; i > 0; i--)
    {
      this.x += this.operandsRead[this.currentOp];
      this.y += this.operandsRead[(this.currentOp + 1)];
      paramGlyphFactory.lineTo((float)this.x, (float)this.y);
      if (paramBoolean)
        System.out.println("rlineCurve " + this.operandsRead[0] + ' ' + this.operandsRead[1]);
      this.currentOp += 2;
    }
    float[] arrayOfFloat = new float[6];
    this.x += this.operandsRead[this.currentOp];
    this.y += this.operandsRead[(this.currentOp + 1)];
    arrayOfFloat[0] = ((float)this.x);
    arrayOfFloat[1] = ((float)this.y);
    this.x += this.operandsRead[(this.currentOp + 2)];
    this.y += this.operandsRead[(this.currentOp + 3)];
    arrayOfFloat[2] = ((float)this.x);
    arrayOfFloat[3] = ((float)this.y);
    this.x += this.operandsRead[(this.currentOp + 4)];
    this.y += this.operandsRead[(this.currentOp + 5)];
    arrayOfFloat[4] = ((float)this.x);
    arrayOfFloat[5] = ((float)this.y);
    paramGlyphFactory.curveTo(arrayOfFloat[0], arrayOfFloat[1], arrayOfFloat[2], arrayOfFloat[3], arrayOfFloat[4], arrayOfFloat[5]);
    if (paramBoolean)
      System.out.println("rlineCurve " + this.operandsRead[0] + ' ' + this.operandsRead[1] + ' ' + this.operandsRead[2] + ' ' + this.operandsRead[3] + ' ' + this.operandsRead[4] + ' ' + this.operandsRead[5]);
    this.currentOp += 6;
  }

  private void closepath(GlyphFactory paramGlyphFactory, boolean paramBoolean)
  {
    if (this.xs != -1.0D)
      paramGlyphFactory.lineTo((float)this.xs, (float)this.ys);
    if (paramBoolean)
      System.out.println("close to xs=" + this.xs + " ys=" + this.ys + ' ' + this.x + ',' + this.y);
    this.xs = -1.0D;
  }

  private void hsbw(GlyphFactory paramGlyphFactory, String paramString, boolean paramBoolean)
  {
    this.x += this.operandsRead[0];
    paramGlyphFactory.moveTo((float)this.x, 0.0F);
    if (paramBoolean)
      System.out.println("hsbw " + this.x + " xs,ys=" + this.xs + ' ' + this.ys);
    if ((this.baseFontName != null) && (this.dynamicVectorRenderer != null) && ((this.dynamicVectorRenderer.getType() == 4) || (this.dynamicVectorRenderer.getType() == 6) || (this.dynamicVectorRenderer.getType() == 5) || (this.dynamicVectorRenderer.getType() == 7)))
      this.dynamicVectorRenderer.saveAdvanceWidth(this.baseFontName, paramString, (int)this.ys);
    this.allowAll = true;
  }

  private void pop(boolean paramBoolean)
  {
    if (this.operandReached > 0)
      this.operandReached -= 1;
    if (paramBoolean)
      System.out.println("POP");
    if (paramBoolean)
      for (int i = 0; i < 6; i++)
        System.out.println(i + " == " + this.operandsRead[i] + ' ' + this.operandReached);
  }

  private void setcurrentpoint(boolean paramBoolean)
  {
    if (paramBoolean)
      System.out.println("setCurrentPoint " + this.operandsRead[0] + ' ' + this.operandsRead[1]);
  }

  private void div(boolean paramBoolean)
  {
    if (paramBoolean)
      for (int i = 0; i < 6; i++)
        System.out.println(i + " " + this.currentOp + ' ' + this.operandsRead[i] + ' ' + this.operandReached);
    double d = this.operandsRead[(this.operandReached - 2)] / this.operandsRead[(this.operandReached - 1)];
    if (this.operandReached > 0)
      this.operandReached -= 1;
    this.operandsRead[(this.operandReached - 1)] = d;
    if (paramBoolean)
      for (int j = 0; j < 6; j++)
        System.out.println("after====" + j + " == " + this.operandsRead[j] + ' ' + this.operandReached);
    if (paramBoolean)
      System.out.println("DIV");
  }

  private void vmoveto(GlyphFactory paramGlyphFactory, boolean paramBoolean1, boolean paramBoolean2)
  {
    if ((paramBoolean2) && (this.operandReached == 2))
      this.currentOp += 1;
    this.y += this.operandsRead[this.currentOp];
    paramGlyphFactory.moveTo((float)this.x, (float)this.y);
    this.xs = this.x;
    this.ys = this.y;
    if (paramBoolean1)
      System.out.println("Set xs,ys= " + this.xs + ' ' + this.ys);
    if (paramBoolean1)
      System.out.println("vmoveto " + this.operandsRead[0] + ' ' + this.operandsRead[1] + " currentOp" + this.currentOp + " y=" + this.y + ' ' + paramBoolean2);
  }

  private void rlineto(GlyphFactory paramGlyphFactory, boolean paramBoolean)
  {
    int i = this.operandReached / 2;
    while (i > 0)
    {
      this.x += this.operandsRead[this.currentOp];
      this.y += this.operandsRead[(this.currentOp + 1)];
      paramGlyphFactory.lineTo((float)this.x, (float)this.y);
      this.currentOp += 2;
      i--;
      if (paramBoolean)
        System.out.println("x,y= (" + this.x + ' ' + this.y + ") rlineto " + this.operandsRead[0] + ' ' + this.operandsRead[1]);
    }
  }

  private void hvlineto(GlyphFactory paramGlyphFactory, boolean paramBoolean, int paramInt)
  {
    int i = paramInt == 6 ? 1 : 0;
    int j = 0;
    while (j < this.operandReached)
    {
      if (i != 0)
        this.x += this.operandsRead[j];
      else
        this.y += this.operandsRead[j];
      paramGlyphFactory.lineTo((float)this.x, (float)this.y);
      if (paramBoolean)
        System.out.println("h/vlineto " + this.operandsRead[0] + ' ' + this.operandsRead[1]);
      j++;
      i = i == 0 ? 1 : 0;
    }
  }

  private void rrcurveto(GlyphFactory paramGlyphFactory, boolean paramBoolean)
  {
    int i = this.operandReached / 6;
    if ((paramBoolean) && (i > 1))
      System.out.println("**********currentOp=" + this.currentOp + " curves=" + i);
    while (i > 0)
    {
      float[] arrayOfFloat = new float[6];
      this.x += this.operandsRead[this.currentOp];
      this.y += this.operandsRead[(this.currentOp + 1)];
      arrayOfFloat[0] = ((float)this.x);
      arrayOfFloat[1] = ((float)this.y);
      this.x += this.operandsRead[(this.currentOp + 2)];
      this.y += this.operandsRead[(this.currentOp + 3)];
      arrayOfFloat[2] = ((float)this.x);
      arrayOfFloat[3] = ((float)this.y);
      this.x += this.operandsRead[(this.currentOp + 4)];
      this.y += this.operandsRead[(this.currentOp + 5)];
      arrayOfFloat[4] = ((float)this.x);
      arrayOfFloat[5] = ((float)this.y);
      paramGlyphFactory.curveTo(arrayOfFloat[0], arrayOfFloat[1], arrayOfFloat[2], arrayOfFloat[3], arrayOfFloat[4], arrayOfFloat[5]);
      if (paramBoolean)
        System.out.println("rrcurveto " + this.operandsRead[this.currentOp] + ' ' + this.operandsRead[(this.currentOp + 1)] + ' ' + this.operandsRead[(this.currentOp + 2)] + ' ' + this.operandsRead[(this.currentOp + 3)] + ' ' + this.operandsRead[(this.currentOp + 4)] + ' ' + this.operandsRead[(this.currentOp + 5)]);
      this.currentOp += 6;
      i--;
    }
  }

  private void endchar(GlyphFactory paramGlyphFactory, int paramInt)
  {
    StandardFonts.checkLoaded(1);
    float f1 = (float)(this.x + this.operandsRead[this.currentOp]);
    float f2 = (float)(this.y + this.operandsRead[(this.currentOp + 1)]);
    String str1 = StandardFonts.getUnicodeChar(1, (int)this.operandsRead[(this.currentOp + 2)]);
    String str2 = StandardFonts.getUnicodeChar(1, (int)this.operandsRead[(this.currentOp + 3)]);
    this.x = 0.0D;
    this.y = 0.0D;
    decodeGlyph(null, paramGlyphFactory, str1, paramInt, "", true);
    paramGlyphFactory.closePath();
    paramGlyphFactory.moveTo(f1, f2);
    this.x = f1;
    this.y = f2;
    decodeGlyph(null, paramGlyphFactory, str2, paramInt, "", true);
    if (this.xs == -1.0D)
    {
      this.xs = this.x;
      this.ys = this.y;
      System.out.println("ENDCHAR Set xs,ys= " + this.xs + ' ' + this.ys);
    }
  }

  private void rcurveline(GlyphFactory paramGlyphFactory, boolean paramBoolean)
  {
    for (int i = (this.operandReached - 2) / 6; i > 0; i--)
    {
      float[] arrayOfFloat = new float[6];
      this.x += this.operandsRead[this.currentOp];
      this.y += this.operandsRead[(this.currentOp + 1)];
      arrayOfFloat[0] = ((float)this.x);
      arrayOfFloat[1] = ((float)this.y);
      this.x += this.operandsRead[(this.currentOp + 2)];
      this.y += this.operandsRead[(this.currentOp + 3)];
      arrayOfFloat[2] = ((float)this.x);
      arrayOfFloat[3] = ((float)this.y);
      this.x += this.operandsRead[(this.currentOp + 4)];
      this.y += this.operandsRead[(this.currentOp + 5)];
      arrayOfFloat[4] = ((float)this.x);
      arrayOfFloat[5] = ((float)this.y);
      paramGlyphFactory.curveTo(arrayOfFloat[0], arrayOfFloat[1], arrayOfFloat[2], arrayOfFloat[3], arrayOfFloat[4], arrayOfFloat[5]);
      if (paramBoolean)
        System.out.println("rCurveline " + this.operandsRead[0] + ' ' + this.operandsRead[1] + ' ' + this.operandsRead[2] + ' ' + this.operandsRead[3] + ' ' + this.operandsRead[4] + ' ' + this.operandsRead[5]);
      this.currentOp += 6;
    }
    this.x += this.operandsRead[this.currentOp];
    this.y += this.operandsRead[(this.currentOp + 1)];
    paramGlyphFactory.lineTo((float)this.x, (float)this.y);
    this.currentOp += 2;
    if (paramBoolean)
      System.out.println("rCurveline " + this.operandsRead[0] + ' ' + this.operandsRead[1]);
  }

  private void seac(GlyphFactory paramGlyphFactory, int paramInt1, int paramInt2)
  {
    StandardFonts.checkLoaded(1);
    float f1 = (float)this.operandsRead[(paramInt2 + 1)];
    float f2 = (float)this.operandsRead[(paramInt2 + 2)];
    String str1 = StandardFonts.getUnicodeChar(1, (int)this.operandsRead[(paramInt2 + 3)]);
    String str2 = StandardFonts.getUnicodeChar(1, (int)this.operandsRead[(paramInt2 + 4)]);
    double d = this.x;
    this.y = 0.0D;
    decodeGlyph(null, paramGlyphFactory, str1, paramInt1, "", true);
    paramGlyphFactory.closePath();
    paramGlyphFactory.moveTo(0.0F, 0.0F);
    this.x = (f1 + d);
    this.y = f2;
    decodeGlyph(null, paramGlyphFactory, str2, paramInt1, "", true);
  }

  private void flex1(GlyphFactory paramGlyphFactory, boolean paramBoolean)
  {
    double d1 = 0.0D;
    double d2 = 0.0D;
    double d3 = this.x;
    double d4 = this.y;
    int i = 0;
    while (i < 10)
    {
      d1 += this.operandsRead[i];
      d2 += this.operandsRead[(i + 1)];
      i += 2;
    }
    i = Math.abs(d1) > Math.abs(d2) ? 1 : 0;
    int j = 0;
    while (j < 6)
    {
      this.x += this.operandsRead[j];
      this.y += this.operandsRead[(j + 1)];
      this.pt[j] = ((float)this.x);
      this.pt[(j + 1)] = ((float)this.y);
      j += 2;
    }
    paramGlyphFactory.curveTo(this.pt[0], this.pt[1], this.pt[2], this.pt[3], this.pt[4], this.pt[5]);
    if (paramBoolean)
      System.out.println("flex1 first curve " + this.operandsRead[0] + ' ' + this.operandsRead[1] + ' ' + this.operandsRead[2] + ' ' + this.operandsRead[3] + ' ' + this.operandsRead[4] + ' ' + this.operandsRead[5]);
    j = 0;
    while (j < 4)
    {
      this.x += this.operandsRead[(j + 6)];
      this.y += this.operandsRead[(j + 7)];
      this.pt[j] = ((float)this.x);
      this.pt[(j + 1)] = ((float)this.y);
      j += 2;
    }
    if (i != 0)
    {
      this.x += this.operandsRead[10];
      this.y = d4;
    }
    else
    {
      this.x = d3;
      this.y += this.operandsRead[10];
    }
    this.pt[4] = ((float)this.x);
    this.pt[5] = ((float)this.y);
    paramGlyphFactory.curveTo(this.pt[0], this.pt[1], this.pt[2], this.pt[3], this.pt[4], this.pt[5]);
    if (paramBoolean)
      System.out.println("flex1 second curve " + this.operandsRead[0] + ' ' + this.operandsRead[1] + ' ' + this.operandsRead[2] + ' ' + this.operandsRead[3] + ' ' + this.operandsRead[4] + ' ' + this.operandsRead[5]);
  }

  private void flex(GlyphFactory paramGlyphFactory, boolean paramBoolean)
  {
    int i = 0;
    while (i < 12)
    {
      int j = 0;
      while (j < 6)
      {
        this.x += this.operandsRead[(i + j)];
        this.y += this.operandsRead[(i + j + 1)];
        this.pt[j] = ((float)this.x);
        this.pt[(j + 1)] = ((float)this.y);
        j += 2;
      }
      paramGlyphFactory.curveTo(this.pt[0], this.pt[1], this.pt[2], this.pt[3], this.pt[4], this.pt[5]);
      if (paramBoolean)
        System.out.println("flex " + this.pt[0] + ' ' + this.pt[1] + ' ' + this.pt[2] + ' ' + this.pt[3] + ' ' + this.pt[4] + ' ' + this.pt[5]);
      i += 6;
    }
  }

  private void hflex(GlyphFactory paramGlyphFactory, boolean paramBoolean)
  {
    this.x += this.operandsRead[0];
    this.pt[0] = ((float)this.x);
    this.pt[1] = ((float)this.y);
    this.x += this.operandsRead[1];
    this.y += this.operandsRead[2];
    this.pt[2] = ((float)this.x);
    this.pt[3] = ((float)this.y);
    this.x += this.operandsRead[3];
    this.pt[4] = ((float)this.x);
    this.pt[5] = ((float)this.y);
    paramGlyphFactory.curveTo(this.pt[0], this.pt[1], this.pt[2], this.pt[3], this.pt[4], this.pt[5]);
    if (paramBoolean)
      System.out.println("hflex first curve " + this.operandsRead[0] + ' ' + this.operandsRead[1] + ' ' + this.operandsRead[2] + ' ' + this.operandsRead[3] + ' ' + this.operandsRead[4] + ' ' + this.operandsRead[5]);
    this.x += this.operandsRead[4];
    this.pt[0] = ((float)this.x);
    this.pt[1] = ((float)this.y);
    this.x += this.operandsRead[5];
    this.pt[2] = ((float)this.x);
    this.pt[3] = ((float)this.y);
    this.x += this.operandsRead[6];
    this.pt[4] = ((float)this.x);
    this.pt[5] = ((float)this.y);
    paramGlyphFactory.curveTo(this.pt[0], this.pt[1], this.pt[2], this.pt[3], this.pt[4], this.pt[5]);
    if (paramBoolean)
      System.out.println("hflex second curve " + this.operandsRead[0] + ' ' + this.operandsRead[1] + ' ' + this.operandsRead[2] + ' ' + this.operandsRead[3] + ' ' + this.operandsRead[4] + ' ' + this.operandsRead[5]);
  }

  private void hflex1(GlyphFactory paramGlyphFactory, boolean paramBoolean)
  {
    this.x += this.operandsRead[0];
    this.y += this.operandsRead[1];
    this.pt[0] = ((float)this.x);
    this.pt[1] = ((float)this.y);
    this.x += this.operandsRead[2];
    this.y += this.operandsRead[3];
    this.pt[2] = ((float)this.x);
    this.pt[3] = ((float)this.y);
    this.x += this.operandsRead[4];
    this.pt[4] = ((float)this.x);
    this.pt[5] = ((float)this.y);
    paramGlyphFactory.curveTo(this.pt[0], this.pt[1], this.pt[2], this.pt[3], this.pt[4], this.pt[5]);
    if (paramBoolean)
      System.out.println("36 first curve " + this.operandsRead[0] + ' ' + this.operandsRead[1] + ' ' + this.operandsRead[2] + ' ' + this.operandsRead[3] + ' ' + this.operandsRead[4] + ' ' + this.operandsRead[5]);
    this.x += this.operandsRead[5];
    this.pt[0] = ((float)this.x);
    this.pt[1] = ((float)this.y);
    this.x += this.operandsRead[6];
    this.y += this.operandsRead[7];
    this.pt[2] = ((float)this.x);
    this.pt[3] = ((float)this.y);
    this.x += this.operandsRead[8];
    this.pt[4] = ((float)this.x);
    this.pt[5] = ((float)this.y);
    paramGlyphFactory.curveTo(this.pt[0], this.pt[1], this.pt[2], this.pt[3], this.pt[4], this.pt[5]);
    if (paramBoolean)
      System.out.println("36 second curve " + this.operandsRead[0] + ' ' + this.operandsRead[1] + ' ' + this.operandsRead[2] + ' ' + this.operandsRead[3] + ' ' + this.operandsRead[4] + ' ' + this.operandsRead[5]);
  }

  public PdfGlyph getEmbeddedGlyph(GlyphFactory paramGlyphFactory, String paramString1, float[][] paramArrayOfFloat, int paramInt, String paramString2, float paramFloat, String paramString3)
  {
    if (((this.lastTrm[0][0] != paramArrayOfFloat[0][0] ? 1 : 0) | (this.lastTrm[1][0] != paramArrayOfFloat[1][0] ? 1 : 0) | (this.lastTrm[0][1] != paramArrayOfFloat[0][1] ? 1 : 0) | (this.lastTrm[1][1] != paramArrayOfFloat[1][1] ? 1 : 0)) != 0)
    {
      this.lastTrm = paramArrayOfFloat;
      flush();
    }
    PdfGlyph localPdfGlyph = getEmbeddedCachedShape(paramInt);
    if (localPdfGlyph == null)
    {
      this.operandsRead = new double[this.max];
      this.operandReached = 0;
      this.x = (-paramGlyphFactory.getLSB());
      this.y = 0.0D;
      decodeGlyph(paramString3, paramGlyphFactory, paramString1, paramInt, paramString2, false);
      localPdfGlyph = paramGlyphFactory.getGlyph();
      if (localPdfGlyph != null)
        localPdfGlyph.setID(paramInt);
      setEmbeddedCachedShape(paramInt, localPdfGlyph);
    }
    Object localObject = this.glyphNumbers.get(paramString1);
    if (localObject == null)
      localObject = this.glyphNumbers.get(Integer.toString(paramInt));
    if (localObject == null)
      localObject = this.glyphNumbers.get(paramString3);
    if (localObject != null)
      localPdfGlyph.setGlyphNumber(((Integer)localObject).intValue());
    return localPdfGlyph;
  }

  public final int getNumber(byte[] paramArrayOfByte, int paramInt1, double[] paramArrayOfDouble, int paramInt2, boolean paramBoolean)
  {
    double d = 0.0D;
    int i = paramArrayOfByte[paramInt1] & 0xFF;
    if (((i < 28 ? 1 : 0) | (i == 31 ? 1 : 0)) != 0)
    {
      System.err.println("!!!!Incorrect type1C operand");
    }
    else if (i == 28)
    {
      d = (paramArrayOfByte[(paramInt1 + 1)] << 8) + (paramArrayOfByte[(paramInt1 + 2)] & 0xFF);
      paramInt1 += 3;
    }
    else if (i == 255)
    {
      if (this.is1C)
      {
        d = ((paramArrayOfByte[(paramInt1 + 1)] & 0xFF) << 8) + (paramArrayOfByte[(paramInt1 + 2)] & 0xFF);
        d += (((paramArrayOfByte[(paramInt1 + 3)] & 0xFF) << 8) + (paramArrayOfByte[(paramInt1 + 4)] & 0xFF)) / 65536.0D;
        if (paramArrayOfByte[(paramInt1 + 1)] < 0)
          d -= 65536.0D;
        if (paramBoolean)
        {
          System.out.println("x=" + d);
          for (int k = 0; k < 5; k++)
            System.out.println(k + " " + paramArrayOfByte[(paramInt1 + k)] + ' ' + (paramArrayOfByte[(paramInt1 + k)] & 0xFF) + ' ' + (paramArrayOfByte[(paramInt1 + k)] & 0x7F));
        }
      }
      else
      {
        d = ((paramArrayOfByte[(paramInt1 + 1)] & 0xFF) << 24) + ((paramArrayOfByte[(paramInt1 + 2)] & 0xFF) << 16) + ((paramArrayOfByte[(paramInt1 + 3)] & 0xFF) << 8) + (paramArrayOfByte[(paramInt1 + 4)] & 0xFF);
      }
      paramInt1 += 5;
    }
    else if (i == 29)
    {
      d = ((paramArrayOfByte[(paramInt1 + 1)] & 0xFF) << 24) + ((paramArrayOfByte[(paramInt1 + 2)] & 0xFF) << 16) + ((paramArrayOfByte[(paramInt1 + 3)] & 0xFF) << 8) + (paramArrayOfByte[(paramInt1 + 4)] & 0xFF);
      paramInt1 += 5;
    }
    else if (i == 30)
    {
      char[] arrayOfChar = new char[65];
      paramInt1++;
      int j = 0;
      while (j < 64)
      {
        int m = paramArrayOfByte[(paramInt1++)] & 0xFF;
        int n = m >> 4 & 0xF;
        int i1 = m & 0xF;
        if (n == 15)
          break;
        arrayOfChar[(j++)] = nybChars.charAt(n);
        if (j == 64)
          break;
        if (n == 12)
          arrayOfChar[(j++)] = '-';
        if ((j == 64) || (i1 == 15))
          break;
        arrayOfChar[(j++)] = nybChars.charAt(i1);
        if (j == 64)
          break;
        if (i1 == 12)
          arrayOfChar[(j++)] = '-';
      }
      d = Double.valueOf(new String(arrayOfChar, 0, j)).doubleValue();
    }
    else if (i < 247)
    {
      d = i - 139;
      paramInt1++;
    }
    else if (i < 251)
    {
      d = (i - 247 << 8) + (paramArrayOfByte[(paramInt1 + 1)] & 0xFF) + 108;
      paramInt1 += 2;
    }
    else
    {
      d = -(i - 251 << 8) - (paramArrayOfByte[(paramInt1 + 1)] & 0xFF) - 108;
      paramInt1 += 2;
    }
    paramArrayOfDouble[paramInt2] = d;
    return paramInt1;
  }

  public final int getNumber(FontData paramFontData, int paramInt1, double[] paramArrayOfDouble, int paramInt2, boolean paramBoolean)
  {
    double d1 = 0.0D;
    int i = paramFontData.getByte(paramInt1) & 0xFF;
    if (((i < 28 ? 1 : 0) | (i == 31 ? 1 : 0)) != 0)
    {
      System.err.println("!!!!Incorrect type1C operand");
    }
    else if (i == 28)
    {
      d1 = (paramFontData.getByte(paramInt1 + 1) << 8) + (paramFontData.getByte(paramInt1 + 2) & 0xFF);
      paramInt1 += 3;
    }
    else if (i == 255)
    {
      if (this.is1C)
      {
        int k = ((paramFontData.getByte(paramInt1 + 1) & 0xFF) << 8) + (paramFontData.getByte(paramInt1 + 2) & 0xFF);
        if (k > 32768)
          k = 65536 - k;
        double d2 = k;
        double d3 = ((paramFontData.getByte(paramInt1 + 3) & 0xFF) << 8) + (paramFontData.getByte(paramInt1 + 4) & 0xFF);
        d1 = d2 + d3 / 65536.0D;
        if (paramFontData.getByte(paramInt1 + 1) < 0)
        {
          if (paramBoolean)
            System.out.println("Negative " + d1);
          d1 = -d1;
        }
        if (paramBoolean)
        {
          System.out.println("x=" + d1);
          for (int i2 = 0; i2 < 5; i2++)
            System.out.println(i2 + " " + paramFontData.getByte(paramInt1 + i2) + ' ' + (paramFontData.getByte(paramInt1 + i2) & 0xFF) + ' ' + (paramFontData.getByte(paramInt1 + i2) & 0x7F));
        }
      }
      else
      {
        d1 = ((paramFontData.getByte(paramInt1 + 1) & 0xFF) << 24) + ((paramFontData.getByte(paramInt1 + 2) & 0xFF) << 16) + ((paramFontData.getByte(paramInt1 + 3) & 0xFF) << 8) + (paramFontData.getByte(paramInt1 + 4) & 0xFF);
      }
      paramInt1 += 5;
    }
    else if (i == 29)
    {
      d1 = ((paramFontData.getByte(paramInt1 + 1) & 0xFF) << 24) + ((paramFontData.getByte(paramInt1 + 2) & 0xFF) << 16) + ((paramFontData.getByte(paramInt1 + 3) & 0xFF) << 8) + (paramFontData.getByte(paramInt1 + 4) & 0xFF);
      paramInt1 += 5;
    }
    else if (i == 30)
    {
      char[] arrayOfChar = new char[65];
      paramInt1++;
      int j = 0;
      while (j < 64)
      {
        int m = paramFontData.getByte(paramInt1++) & 0xFF;
        int n = m >> 4 & 0xF;
        int i1 = m & 0xF;
        if (n == 15)
          break;
        arrayOfChar[(j++)] = nybChars.charAt(n);
        if (j == 64)
          break;
        if (n == 12)
          arrayOfChar[(j++)] = '-';
        if ((j == 64) || (i1 == 15))
          break;
        arrayOfChar[(j++)] = nybChars.charAt(i1);
        if (j == 64)
          break;
        if (i1 == 12)
          arrayOfChar[(j++)] = '-';
      }
      d1 = Double.valueOf(new String(arrayOfChar, 0, j)).doubleValue();
    }
    else if (i < 247)
    {
      d1 = i - 139;
      paramInt1++;
    }
    else if (i < 251)
    {
      d1 = (i - 247 << 8) + (paramFontData.getByte(paramInt1 + 1) & 0xFF) + 108;
      paramInt1 += 2;
    }
    else
    {
      d1 = -(i - 251 << 8) - (paramFontData.getByte(paramInt1 + 1) & 0xFF) - 108;
      paramInt1 += 2;
    }
    paramArrayOfDouble[paramInt2] = d1;
    return paramInt1;
  }

  protected void decodeGlyph(String paramString1, GlyphFactory paramGlyphFactory, String paramString2, int paramInt, String paramString3, boolean paramBoolean)
  {
    boolean bool1 = false;
    this.allowAll = false;
    Object localObject1;
    if (this.isCID)
    {
      localObject1 = (byte[])this.charStrings.get(String.valueOf(paramInt));
    }
    else
    {
      if (paramString2 == null)
        paramString2 = paramString3;
      if (paramString2 == null)
      {
        paramString2 = paramString1;
        if (paramString2 == null)
          paramString2 = ".notdef";
      }
      localObject1 = (byte[])this.charStrings.get(paramString2);
      if (localObject1 == null)
      {
        if (paramString1 != null)
          localObject1 = (byte[])this.charStrings.get(paramString1);
        if (localObject1 == null)
          localObject1 = (byte[])this.charStrings.get(".notdef");
      }
    }
    if (localObject1 != null)
    {
      boolean bool2 = true;
      this.ptCount = 0;
      int j = -1;
      int k = 0;
      int m = 0;
      int i1 = 0;
      int i3 = localObject1.length;
      int i4 = 0;
      this.currentOp = 0;
      this.hintCount = 0;
      double d1 = 999999.0D;
      double d2 = 0.0D;
      double d3 = 1000.0D;
      boolean bool3 = false;
      this.pt = new float[6];
      int i5 = 0;
      this.h = 100000.0D;
      if (this.is1C)
      {
        this.operandsRead = new double[this.max];
        this.operandReached = 0;
        this.allowAll = true;
      }
      int i6;
      if (bool1)
      {
        System.out.println("****************** " + paramString3 + ' ' + paramString2);
        for (i6 = 0; i6 < i3; i6++)
          System.out.println(i6 + " " + (localObject1[i6] & 0xFF));
        System.out.println("=====xs=" + this.xs + " ys=" + this.ys);
      }
      int i11;
      Object localObject2;
      Object localObject3;
      PdfGlyph localPdfGlyph;
      while (k < i3)
      {
        int n = localObject1[k] & 0xFF;
        if ((n > 31) || (n == 28))
        {
          m = k;
          k = getNumber((byte[])localObject1, k, this.operandsRead, this.operandReached, bool1);
          i4 = (int)this.operandsRead[this.operandReached];
          this.operandReached += 1;
          if (m == 0)
            if (this.nominalWidthX.length == 1)
            {
              i5 = this.nominalWidthX[0] + i4;
            }
            else
            {
              i6 = ((Integer)this.glyphNumbers.get(String.valueOf(paramInt))).intValue() - 1;
              if (i6 < this.fdSelect.length)
                i5 = this.nominalWidthX[this.fdSelect[i6]] + i4;
            }
          if ((n == 28) && (bool1))
            System.out.println("Shortint " + i4);
        }
        else
        {
          i6 = 0;
          if ((n == 22) || (n == 4) || (n == 10) || (n == 29) || ((n == 12) && ((localObject1[(k + 1)] == 9) || (localObject1[(k + 1)] == 14) || (localObject1[(k + 1)] == 26) || (localObject1[(k + 1)] == 18) || (localObject1[(k + 1)] == 27) || (localObject1[(k + 1)] == 21) || (localObject1[(k + 1)] == 5))))
            i6 = 1;
          if ((j == -1) && (this.baseFontName != null) && (this.dynamicVectorRenderer != null) && ((this.dynamicVectorRenderer.getType() == 4) || (this.dynamicVectorRenderer.getType() == 6) || (this.dynamicVectorRenderer.getType() == 5) || (this.dynamicVectorRenderer.getType() == 7)))
            if (((i6 == 0) && (this.operandReached % 2 == 1)) || ((i6 != 0) && (this.operandReached % 2 == 0)))
            {
              if ("notdef".equals(paramString2))
                this.dynamicVectorRenderer.saveAdvanceWidth(this.baseFontName, String.valueOf(paramInt), i5);
              else
                this.dynamicVectorRenderer.saveAdvanceWidth(this.baseFontName, paramString2, i5);
            }
            else if (!this.defaultWidthsPassed)
            {
              for (int i7 = 0; i7 < this.defaultWidthX.length; i7++)
                this.dynamicVectorRenderer.saveAdvanceWidth(this.baseFontName, "JPedalDefaultWidth" + i7, this.defaultWidthX[i7]);
              this.defaultWidthsPassed = true;
            }
          j++;
          int i = 0;
          int i2 = i1;
          i1 = n;
          k++;
          this.currentOp = 0;
          if (i1 == 12)
          {
            i1 = localObject1[k] & 0xFF;
            k++;
            if (i1 == 7)
            {
              d3 = sbw(bool1);
              this.operandReached = 0;
            }
            else if (this.allowAll)
            {
              if (i1 == 16)
              {
                bool3 = processFlex(paramGlyphFactory, bool1, i2, bool3, i4);
                this.operandReached = 0;
              }
              else if (i1 == 33)
              {
                setcurrentpoint(bool1);
                this.operandReached = 0;
              }
              else if (i1 == 34)
              {
                hflex(paramGlyphFactory, bool1);
                this.operandReached = 0;
              }
              else if (i1 == 35)
              {
                flex(paramGlyphFactory, bool1);
                this.operandReached = 0;
              }
              else if (i1 == 36)
              {
                hflex1(paramGlyphFactory, bool1);
                this.operandReached = 0;
              }
              else if (i1 == 37)
              {
                flex1(paramGlyphFactory, bool1);
                this.operandReached = 0;
              }
              else if (i1 == 6)
              {
                seac(paramGlyphFactory, paramInt, this.currentOp);
                this.operandReached = 0;
              }
              else if (i1 == 12)
              {
                div(bool1);
              }
              else if (i1 == 17)
              {
                pop(bool1);
              }
              else if (i1 == 0)
              {
                this.operandReached = 0;
                if (bool1)
                  System.out.println("Dot section");
              }
              else if (bool1)
              {
                this.operandReached = 0;
                System.out.println("1 Not implemented " + k + " id=" + i1 + " op=" + org.jpedal.fonts.Type1.T1C[i1]);
              }
              else
              {
                this.operandReached = 0;
              }
            }
          }
          else if (i1 == 13)
          {
            hsbw(paramGlyphFactory, paramString2, bool1);
            this.operandReached = 0;
          }
          else if (this.allowAll)
          {
            int i10;
            if (i1 != 0)
              if (((i1 == 1 ? 1 : 0) | (i1 == 3 ? 1 : 0) | (i1 == 18 ? 1 : 0) | (i1 == 23 ? 1 : 0)) != 0)
              {
                this.hintCount += this.operandReached / 2;
                this.operandReached = 0;
                if (bool1)
                  System.out.println("One of hstem vstem hstemhm vstemhm " + i1 + ' ' + this.xs + ' ' + this.ys);
              }
              else if (i1 == 4)
              {
                if (bool3)
                {
                  double d4 = this.operandsRead[this.currentOp];
                  this.y += d4;
                  this.pt[this.ptCount] = ((float)this.x);
                  this.ptCount += 1;
                  this.pt[this.ptCount] = ((float)this.y);
                  this.ptCount += 1;
                  if (bool1)
                    System.out.println("flex value " + this.x + ' ' + this.y);
                }
                else
                {
                  vmoveto(paramGlyphFactory, bool1, bool2);
                }
                this.operandReached = 0;
              }
              else if (i1 == 5)
              {
                rlineto(paramGlyphFactory, bool1);
                this.operandReached = 0;
              }
              else if (((i1 == 6 ? 1 : 0) | (i1 == 7 ? 1 : 0)) != 0)
              {
                hvlineto(paramGlyphFactory, bool1, i1);
                this.operandReached = 0;
              }
              else if (i1 == 8)
              {
                rrcurveto(paramGlyphFactory, bool1);
                this.operandReached = 0;
              }
              else if (i1 == 9)
              {
                closepath(paramGlyphFactory, bool1);
                this.operandReached = 0;
              }
              else if ((i1 == 10) || (i1 == 29))
              {
                if (bool1)
                  System.out.println(i1 + " -------------- last Value=" + i4 + ' ' + this.allowAll + " commandCount=" + j + " operandReached=" + this.operandReached + ' ' + bool2);
                if ((!this.is1C) && (i1 == 10) && (i4 >= 0) && (i4 <= 2) && (i2 != 11) && (this.operandReached > 5))
                {
                  bool3 = processFlex(paramGlyphFactory, bool1, i2, bool3, i4);
                  this.operandReached = 0;
                }
                else
                {
                  if (i1 == 10)
                    i4 += this.localBias;
                  else
                    i4 += this.globalBias;
                  byte[] arrayOfByte1;
                  if (i1 == 10)
                  {
                    arrayOfByte1 = (byte[])this.charStrings.get("subrs" + i4);
                    if (bool1)
                      System.out.println("=================callsubr " + i4);
                  }
                  else
                  {
                    if (bool1)
                      System.out.println("=================callgsubr " + i4);
                    arrayOfByte1 = (byte[])this.charStrings.get("global" + i4);
                  }
                  if (arrayOfByte1 != null)
                  {
                    if (bool1)
                      System.out.println("Subroutine=============" + i4 + " op=" + this.currentOp + ' ' + this.operandReached);
                    int i9 = arrayOfByte1.length;
                    i10 = localObject1.length;
                    i11 = i9 + i10 - 2;
                    i3 = i3 + i9 - 2;
                    byte[] arrayOfByte2 = new byte[i11];
                    System.arraycopy(localObject1, 0, arrayOfByte2, 0, m);
                    System.arraycopy(arrayOfByte1, 0, arrayOfByte2, m, i9);
                    System.arraycopy(localObject1, k, arrayOfByte2, m + i9, i10 - k);
                    localObject1 = arrayOfByte2;
                    k = m;
                    if (this.operandReached > 0)
                      this.operandReached -= 1;
                  }
                  else if (bool1)
                  {
                    System.out.println("No data found for sub-routine " + this.charStrings);
                  }
                }
              }
              else if (i1 == 11)
              {
                if (bool1)
                  System.out.println("return=============" + k);
              }
              else if (i1 == 14)
              {
                endchar(paramGlyphFactory, paramInt, bool1);
                this.operandReached = 0;
                k = i3 + 1;
              }
              else if (i1 == 16)
              {
                if (bool1)
                  System.out.println("Blend");
                this.operandReached = 0;
              }
              else if (((i1 == 19 ? 1 : 0) | (i1 == 20 ? 1 : 0)) != 0)
              {
                k = mask(bool1, k, i2);
                this.operandReached = 0;
              }
              else
              {
                double d5;
                if (i1 == 21)
                {
                  if (bool3)
                  {
                    if (bool1)
                      System.out.println(this.currentOp + " " + this.ptCount + ' ' + this.pt.length);
                    d5 = this.operandsRead[(this.currentOp + 1)];
                    this.y += d5;
                    d5 = this.operandsRead[this.currentOp];
                    this.x += d5;
                    this.pt[this.ptCount] = ((float)this.x);
                    this.ptCount += 1;
                    this.pt[this.ptCount] = ((float)this.y);
                    this.ptCount += 1;
                    if (bool1)
                      System.out.println("flex value " + this.pt[(this.ptCount - 2)] + ' ' + this.pt[(this.ptCount - 1)] + " count=" + this.ptCount);
                  }
                  else
                  {
                    rmoveto(paramGlyphFactory, bool1, bool2);
                  }
                  this.operandReached = 0;
                }
                else if (i1 == 22)
                {
                  if (bool3)
                  {
                    d5 = this.operandsRead[this.currentOp];
                    this.x += d5;
                    this.pt[this.ptCount] = ((float)this.x);
                    this.ptCount += 1;
                    this.pt[this.ptCount] = ((float)this.y);
                    this.ptCount += 1;
                    if (bool1)
                      System.out.println("flex value " + this.x + ' ' + this.y);
                  }
                  else
                  {
                    hmoveto(paramGlyphFactory, bool1, bool2);
                  }
                  this.operandReached = 0;
                }
                else if (i1 == 24)
                {
                  rcurveline(paramGlyphFactory, bool1);
                  this.operandReached = 0;
                }
                else if (i1 == 25)
                {
                  rlinecurve(paramGlyphFactory, bool1);
                  this.operandReached = 0;
                }
                else if (((i1 == 26 ? 1 : 0) | (i1 == 27 ? 1 : 0)) != 0)
                {
                  vvhhcurveto(paramGlyphFactory, bool1, i1);
                  this.operandReached = 0;
                }
                else if (((i1 == 30 ? 1 : 0) | (i1 == 31 ? 1 : 0)) != 0)
                {
                  vhhvcurveto(paramGlyphFactory, bool1, i1);
                  this.operandReached = 0;
                }
                else if (bool1)
                {
                  this.operandReached = 0;
                  System.out.println("Unsupported command " + k + ">>>>>" + this.hintCount + ">>>>>>key=" + i1 + ' ' + org.jpedal.fonts.Type1.T1CcharCodes1Byte[i1] + " <1<<" + this.operandsRead);
                  for (int i8 = 0; i8 < i3; i8++)
                    System.out.println(i8 + " " + (localObject1[i8] & 0xFF));
                }
              }
            if ((bool1) && (i == 0))
            {
              localObject2 = new BufferedImage(1000, 1000, 2);
              localObject3 = ((BufferedImage)localObject2).createGraphics();
              ((Graphics2D)localObject3).setColor(Color.red);
              for (i10 = 0; i10 < 7; i10++)
              {
                ((Graphics2D)localObject3).drawLine(i10 * 100, 0, i10 * 100, 1000);
                ((Graphics2D)localObject3).drawLine(0, i10 * 100, 1000, i10 * 100);
              }
              ((Graphics2D)localObject3).setColor(Color.black);
              localPdfGlyph = paramGlyphFactory.getGlyph();
              if (localPdfGlyph.getShape() != null)
              {
                localPdfGlyph.render(1, (Graphics2D)localObject3, 1.0F, false);
                ShowGUIMessage.showGUIMessage(k + " x " + " x,y=" + this.x + ' ' + this.y, (BufferedImage)localObject2, k + " x ");
              }
            }
          }
          if (d1 > this.y)
            d1 = this.y;
          if (d2 < this.y)
            d2 = this.y;
          if ((i1 != 19) && (i1 != 29) && (i1 != 10))
            bool2 = false;
        }
      }
      if (d3 > this.h)
        d1 = d3 - this.h;
      if (d2 < d3)
      {
        d1 = 0.0D;
      }
      else if (d3 != d2)
      {
        float f = (float)(d2 - (d3 - d1));
        if (f < 0.0F)
        {
          if (d3 - d2 <= f)
            d1 = f;
          else
            d1 -= f;
        }
        else
          d1 = 0.0D;
        if (d1 < 0.0D)
          d1 = 0.0D;
      }
      paramGlyphFactory.setYMin((float)d1);
      if ((bool1 & !paramBoolean))
      {
        BufferedImage localBufferedImage = new BufferedImage(600, 600, 2);
        localObject2 = localBufferedImage.createGraphics();
        ((Graphics2D)localObject2).setColor(Color.red);
        localObject3 = new AffineTransform();
        ((AffineTransform)localObject3).scale(0.25D, 0.25D);
        ((Graphics2D)localObject2).transform((AffineTransform)localObject3);
        localPdfGlyph = paramGlyphFactory.getGlyph();
        ((Graphics2D)localObject2).setColor(Color.green);
        for (i11 = 0; i11 < 7; i11++)
          ((Graphics2D)localObject2).drawLine(0, i11 * 50, 1000, i11 * 50);
        localPdfGlyph.render(1, (Graphics2D)localObject2, 1.0F, false);
      }
    }
  }

  public void setCharString(String paramString, byte[] paramArrayOfByte, int paramInt)
  {
    this.charStrings.put(paramString, paramArrayOfByte);
    this.glyphNumbers.put(paramString, Integer.valueOf(paramInt));
  }

  public void setIndexForCharString(int paramInt, String paramString)
  {
    if (this.charForGlyphIndex == null)
      this.charForGlyphIndex = new String[65536];
    if (paramInt < this.charForGlyphIndex.length)
      this.charForGlyphIndex[paramInt] = paramString;
  }

  public String getIndexForCharString(int paramInt)
  {
    return this.charForGlyphIndex[paramInt];
  }

  public boolean is1C()
  {
    return this.is1C;
  }

  public void setWidthValues(int[] paramArrayOfInt1, int[] paramArrayOfInt2)
  {
    this.nominalWidthX = paramArrayOfInt2;
    this.defaultWidthX = paramArrayOfInt1;
  }

  public void setis1C(boolean paramBoolean)
  {
    this.is1C = paramBoolean;
  }

  public void setRenderer(DynamicVectorRenderer paramDynamicVectorRenderer)
  {
    this.dynamicVectorRenderer = paramDynamicVectorRenderer;
  }

  public void setFDSelect(int[] paramArrayOfInt)
  {
    this.fdSelect = paramArrayOfInt;
  }

  public Map getCharStrings()
  {
    return this.charStrings;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.fonts.glyph.T1Glyphs
 * JD-Core Version:    0.6.2
 */