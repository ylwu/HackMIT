package org.jpedal.fonts.tt;

import java.io.PrintStream;
import java.io.Serializable;
import javafx.collections.ObservableList;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import org.jpedal.fonts.glyph.PdfGlyph;
import org.jpedal.fonts.tt.hinting.TTVM;

public class TTGlyphFX extends BaseTTGlyph
  implements PdfGlyph, Serializable
{
  public TTGlyphFX(String paramString1, Glyf paramGlyf, FontFile2 paramFontFile2, Hmtx paramHmtx, int paramInt, float paramFloat, String paramString2)
  {
    super(paramString1, paramGlyf, paramFontFile2, paramHmtx, paramInt, paramFloat, paramString2, true);
  }

  public TTGlyphFX(String paramString, Glyf paramGlyf, FontFile2 paramFontFile2, Hmtx paramHmtx, int paramInt, float paramFloat, TTVM paramTTVM)
  {
    super(paramString, paramGlyf, paramFontFile2, paramHmtx, paramInt, paramFloat, paramTTVM, true);
  }

  void clearPaths()
  {
  }

  public void createPaths(int[] paramArrayOfInt1, int[] paramArrayOfInt2, boolean[] paramArrayOfBoolean1, boolean[] paramArrayOfBoolean2, int paramInt)
  {
    if (paramArrayOfBoolean2 == null)
      return;
    int i = paramArrayOfBoolean2.length;
    int j = 0;
    int k = -1;
    for (int m = 0; m < i; m++)
      if (paramArrayOfBoolean2[m] != 0)
      {
        if ((k != -1) && ((paramArrayOfBoolean1[j] == 0) || (paramArrayOfBoolean1[m] == 0)))
        {
          n = k - j;
          i2 = paramArrayOfInt1.length;
          int[] arrayOfInt1 = new int[i2];
          System.arraycopy(paramArrayOfInt1, 0, arrayOfInt1, 0, i2);
          int[] arrayOfInt2 = new int[i2];
          System.arraycopy(paramArrayOfInt2, 0, arrayOfInt2, 0, i2);
          boolean[] arrayOfBoolean = new boolean[i2];
          System.arraycopy(paramArrayOfBoolean1, 0, arrayOfBoolean, 0, i2);
          for (i6 = j; i6 < m + 1; i6++)
          {
            i1 = i6 + n;
            if (i1 > m)
              i1 -= m - j + 1;
            paramArrayOfInt1[i6] = arrayOfInt1[i1];
            paramArrayOfInt2[i6] = arrayOfInt2[i1];
            paramArrayOfBoolean1[i6] = arrayOfBoolean[i1];
          }
        }
        j = m + 1;
        k = -1;
      }
      else if ((paramArrayOfBoolean1[m] != 0) && (k == -1))
      {
        k = m;
      }
    m = 1;
    if (this.pathsFX == null)
    {
      this.pathsFX = new Path();
      this.pathsFX.setFillRule(FillRule.NON_ZERO);
    }
    int n = paramArrayOfInt1.length;
    int i1 = -1;
    for (int i2 = 0; i2 < n; i2++)
      if (paramArrayOfBoolean2[i2] != 0)
      {
        i1 = i2 + 1;
        i2 = n;
      }
    int i4 = 0;
    int i5 = 0;
    int i6 = 0;
    int i7 = 0;
    i2 = paramArrayOfInt1[0];
    int i3 = paramArrayOfInt2[0];
    if (debug)
      System.out.println(paramArrayOfInt1[0] + " " + paramArrayOfInt2[0] + " move to x1,y1=" + i2 + ' ' + i3);
    this.pathsFX.getElements().add(new MoveTo(i2, i3));
    if (debug)
      System.out.println("first contour=" + i1 + "====================================" + paramArrayOfInt1[0] + ' ' + paramArrayOfInt2[0]);
    int i8 = 0;
    int i9 = 0;
    int i10 = 0;
    boolean bool = false;
    for (int i11 = 0; i11 < paramInt; i11++)
    {
      int i12 = i11 % i1;
      int i13 = (i11 + 1) % i1;
      int i14 = (i11 + 2) % i1;
      int i15 = (i11 - 1) % i1;
      if (i11 == 0)
        i15 = i1 - 1;
      if (i13 < i10)
        i13 += i10;
      if (i14 < i10)
        i14 += i10;
      if (debug)
        System.out.println("points=" + i10 + '/' + i1 + ' ' + i15 + ' ' + i12 + ' ' + i13 + ' ' + i14 + " j=" + i11 + " endOfContour[j]=" + paramArrayOfBoolean2[i11]);
      int i16;
      if (paramArrayOfBoolean2[i11] != 0)
      {
        bool = true;
        if (paramArrayOfBoolean1[i1] != 0)
        {
          i8 = paramArrayOfInt1[i1];
          i9 = paramArrayOfInt2[i1];
        }
        else
        {
          i8 = paramArrayOfInt1[(i11 + 1)];
          i9 = paramArrayOfInt2[(i11 + 1)];
        }
        i10 = i1;
        for (i16 = i11 + 1; i16 < n; i16++)
          if (paramArrayOfBoolean2[i16] != 0)
          {
            i1 = i16 + 1;
            i16 = n;
          }
        if (debug)
          System.out.println("End of contour. next=" + i11 + ' ' + i1 + ' ' + i10);
      }
      if (debug)
        if (i11 > 0)
          System.out.println("curves=" + paramArrayOfBoolean1[i12] + ' ' + paramArrayOfBoolean1[i13] + ' ' + paramArrayOfBoolean1[i14] + " EndOfContour j-1=" + paramArrayOfBoolean2[(i11 - 1)] + " j=" + paramArrayOfBoolean2[i11] + " j+1=" + paramArrayOfBoolean2[(i11 + 1)]);
        else
          System.out.println("curves=" + paramArrayOfBoolean1[i12] + ' ' + paramArrayOfBoolean1[i13] + ' ' + paramArrayOfBoolean1[i14] + " EndOfContour j=" + paramArrayOfBoolean2[i11] + " j+1=" + paramArrayOfBoolean2[(i11 + 1)]);
      if ((i10 == i1) && (paramArrayOfBoolean1[i12] != 0))
      {
        i11 = n;
        if (debug)
          System.out.println("last 2 match");
      }
      else
      {
        if (debug)
          System.out.println(i1 + " " + i15 + ' ' + i12 + ' ' + i13 + ' ' + i14);
        if ((paramArrayOfBoolean1[i12] != 0) && (paramArrayOfBoolean1[i13] != 0))
        {
          i6 = paramArrayOfInt1[i13];
          i7 = paramArrayOfInt2[i13];
          this.pathsFX.getElements().add(new LineTo(i6, i7));
          if (debug)
            System.out.println(i12 + " pt,pt " + i6 + ' ' + i7 + " (lineTo)");
          m = 0;
        }
        else if ((i11 < n - 3) && ((i1 - i10 > 1) || (i1 == i10)))
        {
          i16 = 0;
          if ((paramArrayOfBoolean1[i12] != 0) && (paramArrayOfBoolean1[i13] == 0) && (paramArrayOfBoolean1[i14] != 0))
          {
            i2 = paramArrayOfInt1[i12];
            i3 = paramArrayOfInt2[i12];
            i4 = paramArrayOfInt1[i13];
            i5 = paramArrayOfInt2[i13];
            i6 = paramArrayOfInt1[i14];
            i7 = paramArrayOfInt2[i14];
            i11++;
            i16 = 1;
            if (debug)
              System.out.println(i12 + " pt,cv,pt " + i2 + ' ' + i3 + ' ' + i4 + ' ' + i5 + ' ' + i6 + ' ' + i7);
          }
          else if ((paramArrayOfBoolean1[i12] != 0) && (paramArrayOfBoolean1[i13] == 0) && (paramArrayOfBoolean1[i14] == 0))
          {
            i2 = paramArrayOfInt1[i12];
            i3 = paramArrayOfInt2[i12];
            i4 = paramArrayOfInt1[i13];
            i5 = paramArrayOfInt2[i13];
            i6 = midPt(paramArrayOfInt1[i13], paramArrayOfInt1[i14]);
            i7 = midPt(paramArrayOfInt2[i13], paramArrayOfInt2[i14]);
            i11++;
            i16 = 1;
            if (debug)
              System.out.println(i12 + " pt,cv,cv " + i2 + ' ' + i3 + ' ' + i4 + ' ' + i5 + ' ' + i6 + ' ' + i7);
          }
          else if ((paramArrayOfBoolean1[i12] == 0) && (paramArrayOfBoolean1[i13] == 0) && ((paramArrayOfBoolean2[i14] == 0) || (i1 - i14 == 1)))
          {
            i2 = midPt(paramArrayOfInt1[i15], paramArrayOfInt1[i12]);
            i3 = midPt(paramArrayOfInt2[i15], paramArrayOfInt2[i12]);
            i4 = paramArrayOfInt1[i12];
            i5 = paramArrayOfInt2[i12];
            i6 = midPt(paramArrayOfInt1[i12], paramArrayOfInt1[i13]);
            i7 = midPt(paramArrayOfInt2[i12], paramArrayOfInt2[i13]);
            if (debug)
              System.out.println(i12 + " cv,cv1 " + i2 + ' ' + i3 + ' ' + i4 + ' ' + i5 + ' ' + i6 + ' ' + i7);
          }
          else if ((paramArrayOfBoolean1[i12] == 0) && (paramArrayOfBoolean1[i13] != 0))
          {
            i2 = midPt(paramArrayOfInt1[i15], paramArrayOfInt1[i12]);
            i3 = midPt(paramArrayOfInt2[i15], paramArrayOfInt2[i12]);
            i4 = paramArrayOfInt1[i12];
            i5 = paramArrayOfInt2[i12];
            i6 = paramArrayOfInt1[i13];
            i7 = paramArrayOfInt2[i13];
            if (debug)
              System.out.println(i12 + " cv,pt " + i2 + ' ' + i3 + ' ' + i4 + ' ' + i5 + ' ' + i6 + ' ' + i7);
          }
          if (m != 0)
          {
            this.pathsFX.getElements().add(new MoveTo(i2, i3));
            m = 0;
            if (debug)
              System.out.println("first draw move to " + i2 + ' ' + i3);
          }
          if ((paramArrayOfBoolean2[i12] == 0) || (i12 <= 0) || (paramArrayOfBoolean2[(i12 - 1)] == 0))
            this.pathsFX.getElements().add(new CubicCurveTo(i2, i3, i4, i5, i6, i7));
          if (debug)
            System.out.println("curveto " + i2 + ' ' + i3 + ' ' + i4 + ' ' + i5 + ' ' + i6 + ' ' + i7);
          if ((i16 != 0) && (paramArrayOfBoolean2[i11] != 0))
          {
            bool = true;
            i8 = paramArrayOfInt1[i1];
            i9 = paramArrayOfInt2[i1];
            i10 = i1;
            for (int i17 = i11 + 1; i17 < n; i17++)
              if (paramArrayOfBoolean2[i17] != 0)
              {
                i1 = i17 + 1;
                i17 = n;
              }
            if (debug)
              System.out.println("Curve");
          }
        }
        if (paramArrayOfBoolean2[i12] != 0)
          this.pathsFX.getElements().add(new ClosePath());
        if (debug)
          System.out.println("x2 " + i8 + ' ' + i9 + ' ' + bool);
        if (bool)
        {
          this.pathsFX.getElements().add(new MoveTo(i8, i9));
          bool = false;
          if (debug)
            System.out.println("Move to " + i8 + ' ' + i9);
        }
      }
    }
    if (debug)
      System.out.println("Ends at " + i2 + ' ' + i3 + " x=" + this.minX + ',' + this.maxX + " y=" + this.minY + ',' + this.maxY + " glyph x=" + this.compMinX + ',' + this.compMaxX + " y=" + this.compMinY + ',' + this.compMaxY);
  }

  public Path getPath()
  {
    this.pathsFX = null;
    createGlyph(false);
    return this.pathsFX;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.fonts.tt.TTGlyphFX
 * JD-Core Version:    0.6.2
 */