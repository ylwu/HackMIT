package org.jpedal.fonts.tt;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.PrintStream;
import java.io.Serializable;
import org.jpedal.fonts.glyph.PdfGlyph;
import org.jpedal.fonts.tt.hinting.TTVM;
import org.jpedal.gui.ShowGUIMessage;
import org.jpedal.io.PathSerializer;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.repositories.Vector_Path;

public class TTGlyph extends BaseTTGlyph
  implements PdfGlyph, Serializable
{
  Area glyphShape = null;
  BufferedImage img = null;

  public void setPaths(Vector_Path paramVector_Path)
  {
    this.paths = paramVector_Path;
  }

  public void writePathsToStream(ObjectOutput paramObjectOutput)
    throws IOException
  {
    if (this.paths != null)
    {
      GeneralPath[] arrayOfGeneralPath = this.paths.get();
      int i = 0;
      for (int j = 0; j < arrayOfGeneralPath.length; j++)
        if (arrayOfGeneralPath[j] == null)
        {
          i = j;
          break;
        }
      paramObjectOutput.writeObject(Integer.valueOf(i));
      for (j = 0; j < i; j++)
      {
        PathIterator localPathIterator = arrayOfGeneralPath[j].getPathIterator(new AffineTransform());
        PathSerializer.serializePath(paramObjectOutput, localPathIterator);
      }
    }
  }

  public TTGlyph(String paramString1, Glyf paramGlyf, FontFile2 paramFontFile2, Hmtx paramHmtx, int paramInt, float paramFloat, String paramString2)
  {
    super(paramString1, paramGlyf, paramFontFile2, paramHmtx, paramInt, paramFloat, paramString2, false);
    if (debug)
      try
      {
        System.out.println("debugging" + paramInt);
        BufferedImage localBufferedImage = new BufferedImage(700, 700, 2);
        Graphics2D localGraphics2D = localBufferedImage.createGraphics();
        for (int i = 0; i < this.paths.size() - 1; i++)
        {
          if (i == 0)
            localGraphics2D.setColor(Color.red);
          else
            localGraphics2D.setColor(Color.blue);
          localGraphics2D.fill(this.paths.elementAt(i));
          localGraphics2D.draw(this.paths.elementAt(i).getBounds());
        }
      }
      catch (Exception localException)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog("Exception: " + localException.getMessage());
      }
  }

  public TTGlyph(String paramString, Glyf paramGlyf, FontFile2 paramFontFile2, Hmtx paramHmtx, int paramInt, float paramFloat, TTVM paramTTVM)
  {
    super(paramString, paramGlyf, paramFontFile2, paramHmtx, paramInt, paramFloat, paramTTVM, false);
    if (debug)
      try
      {
        System.out.println("debugging" + paramInt);
        BufferedImage localBufferedImage = new BufferedImage(700, 700, 2);
        Graphics2D localGraphics2D = localBufferedImage.createGraphics();
        for (int i = 0; i < this.paths.size() - 1; i++)
        {
          if (i == 0)
            localGraphics2D.setColor(Color.red);
          else
            localGraphics2D.setColor(Color.blue);
          localGraphics2D.fill(this.paths.elementAt(i));
          localGraphics2D.draw(this.paths.elementAt(i).getBounds());
        }
      }
      catch (Exception localException)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog("Exception: " + localException.getMessage());
      }
  }

  public void render(int paramInt, Graphics2D paramGraphics2D, float paramFloat, boolean paramBoolean)
  {
    AffineTransform localAffineTransform = paramGraphics2D.getTransform();
    BasicStroke localBasicStroke = (BasicStroke)paramGraphics2D.getStroke();
    float f = localBasicStroke.getLineWidth();
    if (f < 0.0F)
      f = -f;
    if (useHinting)
    {
      paramGraphics2D.scale(0.01D, 0.01D);
      f *= 100.0F;
    }
    paramGraphics2D.setStroke(new BasicStroke(f, 0, 1, localBasicStroke.getMiterLimit(), localBasicStroke.getDashArray(), localBasicStroke.getDashPhase()));
    for (int i = 0; i < this.paths.size() - 1; i++)
      if ((paramInt & 0x2) == 2)
        paramGraphics2D.fill(this.paths.elementAt(i));
      else if ((paramInt & 0x1) == 1)
        paramGraphics2D.draw(this.paths.elementAt(i));
    if (useHinting)
    {
      paramGraphics2D.setStroke(localBasicStroke);
      paramGraphics2D.setTransform(localAffineTransform);
    }
  }

  public Area getShape()
  {
    if (this.glyphShape == null)
    {
      GeneralPath localGeneralPath = this.paths.elementAt(0);
      for (int i = 1; i < this.paths.size() - 1; i++)
        localGeneralPath.append(this.paths.elementAt(i), false);
      if (localGeneralPath == null)
        return null;
      this.glyphShape = new Area(localGeneralPath);
    }
    return this.glyphShape;
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
          int n = k - j;
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
    GeneralPath localGeneralPath = new GeneralPath(1);
    int i1 = paramArrayOfInt1.length;
    int i2 = -1;
    for (int i3 = 0; i3 < i1; i3++)
      if (paramArrayOfBoolean2[i3] != 0)
      {
        i2 = i3 + 1;
        i3 = i1;
      }
    int i5 = 0;
    int i6 = 0;
    int i7 = 0;
    int i8 = 0;
    i3 = paramArrayOfInt1[0];
    int i4 = paramArrayOfInt2[0];
    if (debug)
      System.out.println(paramArrayOfInt1[0] + " " + paramArrayOfInt2[0] + " move to x1,y1=" + i3 + ' ' + i4);
    localGeneralPath.moveTo(i3, i4);
    if (debug)
      System.out.println("first contour=" + i2 + "====================================" + paramArrayOfInt1[0] + ' ' + paramArrayOfInt2[0]);
    int i9 = 0;
    int i10 = 0;
    int i11 = 0;
    boolean bool = false;
    for (int i12 = 0; i12 < paramInt; i12++)
    {
      int i13 = i12 % i2;
      int i14 = (i12 + 1) % i2;
      int i15 = (i12 + 2) % i2;
      int i16 = (i12 - 1) % i2;
      if (i12 == 0)
        i16 = i2 - 1;
      if (i14 < i11)
        i14 += i11;
      if (i15 < i11)
        i15 += i11;
      if (debug)
        System.out.println("points=" + i11 + '/' + i2 + ' ' + i16 + ' ' + i13 + ' ' + i14 + ' ' + i15 + " j=" + i12 + " endOfContour[j]=" + paramArrayOfBoolean2[i12]);
      int i17;
      if (paramArrayOfBoolean2[i12] != 0)
      {
        bool = true;
        if (paramArrayOfBoolean1[i2] != 0)
        {
          i9 = paramArrayOfInt1[i2];
          i10 = paramArrayOfInt2[i2];
        }
        else
        {
          i9 = paramArrayOfInt1[(i12 + 1)];
          i10 = paramArrayOfInt2[(i12 + 1)];
        }
        i11 = i2;
        for (i17 = i12 + 1; i17 < i1; i17++)
          if (paramArrayOfBoolean2[i17] != 0)
          {
            i2 = i17 + 1;
            i17 = i1;
          }
        if (debug)
          System.out.println("End of contour. next=" + i12 + ' ' + i2 + ' ' + i11);
      }
      if (debug)
        if (i12 > 0)
          System.out.println("curves=" + paramArrayOfBoolean1[i13] + ' ' + paramArrayOfBoolean1[i14] + ' ' + paramArrayOfBoolean1[i15] + " EndOfContour j-1=" + paramArrayOfBoolean2[(i12 - 1)] + " j=" + paramArrayOfBoolean2[i12] + " j+1=" + paramArrayOfBoolean2[(i12 + 1)]);
        else
          System.out.println("curves=" + paramArrayOfBoolean1[i13] + ' ' + paramArrayOfBoolean1[i14] + ' ' + paramArrayOfBoolean1[i15] + " EndOfContour j=" + paramArrayOfBoolean2[i12] + " j+1=" + paramArrayOfBoolean2[(i12 + 1)]);
      if ((i11 == i2) && (paramArrayOfBoolean1[i13] != 0))
      {
        i12 = i1;
        if (debug)
          System.out.println("last 2 match");
      }
      else
      {
        if (debug)
          System.out.println(i2 + " " + i16 + ' ' + i13 + ' ' + i14 + ' ' + i15);
        if ((paramArrayOfBoolean1[i13] != 0) && (paramArrayOfBoolean1[i14] != 0))
        {
          i7 = paramArrayOfInt1[i14];
          i8 = paramArrayOfInt2[i14];
          localGeneralPath.lineTo(i7, i8);
          if (debug)
            System.out.println(i13 + " pt,pt " + i7 + ' ' + i8 + " (lineTo)");
          m = 0;
        }
        else if ((i12 < i1 - 3) && ((i2 - i11 > 1) || (i2 == i11)))
        {
          i17 = 0;
          if ((paramArrayOfBoolean1[i13] != 0) && (paramArrayOfBoolean1[i14] == 0) && (paramArrayOfBoolean1[i15] != 0))
          {
            i3 = paramArrayOfInt1[i13];
            i4 = paramArrayOfInt2[i13];
            i5 = paramArrayOfInt1[i14];
            i6 = paramArrayOfInt2[i14];
            i7 = paramArrayOfInt1[i15];
            i8 = paramArrayOfInt2[i15];
            i12++;
            i17 = 1;
            if (debug)
              System.out.println(i13 + " pt,cv,pt " + i3 + ' ' + i4 + ' ' + i5 + ' ' + i6 + ' ' + i7 + ' ' + i8);
          }
          else if ((paramArrayOfBoolean1[i13] != 0) && (paramArrayOfBoolean1[i14] == 0) && (paramArrayOfBoolean1[i15] == 0))
          {
            i3 = paramArrayOfInt1[i13];
            i4 = paramArrayOfInt2[i13];
            i5 = paramArrayOfInt1[i14];
            i6 = paramArrayOfInt2[i14];
            i7 = midPt(paramArrayOfInt1[i14], paramArrayOfInt1[i15]);
            i8 = midPt(paramArrayOfInt2[i14], paramArrayOfInt2[i15]);
            i12++;
            i17 = 1;
            if (debug)
              System.out.println(i13 + " pt,cv,cv " + i3 + ' ' + i4 + ' ' + i5 + ' ' + i6 + ' ' + i7 + ' ' + i8);
          }
          else if ((paramArrayOfBoolean1[i13] == 0) && (paramArrayOfBoolean1[i14] == 0) && ((paramArrayOfBoolean2[i15] == 0) || (i2 - i15 == 1)))
          {
            i3 = midPt(paramArrayOfInt1[i16], paramArrayOfInt1[i13]);
            i4 = midPt(paramArrayOfInt2[i16], paramArrayOfInt2[i13]);
            i5 = paramArrayOfInt1[i13];
            i6 = paramArrayOfInt2[i13];
            i7 = midPt(paramArrayOfInt1[i13], paramArrayOfInt1[i14]);
            i8 = midPt(paramArrayOfInt2[i13], paramArrayOfInt2[i14]);
            if (debug)
              System.out.println(i13 + " cv,cv1 " + i3 + ' ' + i4 + ' ' + i5 + ' ' + i6 + ' ' + i7 + ' ' + i8);
          }
          else if ((paramArrayOfBoolean1[i13] == 0) && (paramArrayOfBoolean1[i14] != 0))
          {
            i3 = midPt(paramArrayOfInt1[i16], paramArrayOfInt1[i13]);
            i4 = midPt(paramArrayOfInt2[i16], paramArrayOfInt2[i13]);
            i5 = paramArrayOfInt1[i13];
            i6 = paramArrayOfInt2[i13];
            i7 = paramArrayOfInt1[i14];
            i8 = paramArrayOfInt2[i14];
            if (debug)
              System.out.println(i13 + " cv,pt " + i3 + ' ' + i4 + ' ' + i5 + ' ' + i6 + ' ' + i7 + ' ' + i8);
          }
          if (m != 0)
          {
            localGeneralPath.moveTo(i3, i4);
            m = 0;
            if (debug)
              System.out.println("first draw move to " + i3 + ' ' + i4);
          }
          if ((paramArrayOfBoolean2[i13] == 0) || (i13 <= 0) || (paramArrayOfBoolean2[(i13 - 1)] == 0))
            localGeneralPath.curveTo(i3, i4, i5, i6, i7, i8);
          if (debug)
            System.out.println("curveto " + i3 + ' ' + i4 + ' ' + i5 + ' ' + i6 + ' ' + i7 + ' ' + i8);
          if ((i17 != 0) && (paramArrayOfBoolean2[i12] != 0))
          {
            bool = true;
            i9 = paramArrayOfInt1[i2];
            i10 = paramArrayOfInt2[i2];
            i11 = i2;
            for (int i18 = i12 + 1; i18 < i1; i18++)
              if (paramArrayOfBoolean2[i18] != 0)
              {
                i2 = i18 + 1;
                i18 = i1;
              }
            if (debug)
              System.out.println("Curve");
          }
        }
        if (paramArrayOfBoolean2[i13] != 0)
          localGeneralPath.closePath();
        if (debug)
          System.out.println("x2 " + i9 + ' ' + i10 + ' ' + bool);
        if (bool)
        {
          localGeneralPath.moveTo(i9, i10);
          bool = false;
          if (debug)
            System.out.println("Move to " + i9 + ' ' + i10);
        }
        if (debug)
          try
          {
            if (this.img == null)
              this.img = new BufferedImage(800, 800, 2);
            Graphics2D localGraphics2D = this.img.createGraphics();
            localGraphics2D.setColor(Color.green);
            localGraphics2D.draw(localGeneralPath);
            String str = String.valueOf(i13);
            ShowGUIMessage.showGUIMessage(str, this.img, str);
          }
          catch (Exception localException)
          {
            if (LogWriter.isOutput())
              LogWriter.writeLog("Exception: " + localException.getMessage());
          }
      }
    }
    this.paths.addElement(localGeneralPath);
    if (debug)
      System.out.println("Ends at " + i3 + ' ' + i4 + " x=" + this.minX + ',' + this.maxX + " y=" + this.minY + ',' + this.maxY + " glyph x=" + this.compMinX + ',' + this.compMaxX + " y=" + this.compMinY + ',' + this.compMaxY);
  }

  public void flushArea()
  {
    this.glyphShape = null;
  }

  void clearPaths()
  {
    this.paths.clear();
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.fonts.tt.TTGlyph
 * JD-Core Version:    0.6.2
 */