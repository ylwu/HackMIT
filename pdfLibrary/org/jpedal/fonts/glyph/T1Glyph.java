package org.jpedal.fonts.glyph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.io.IOException;
import java.io.ObjectOutput;
import java.util.Map;
import org.jpedal.color.PdfTexturePaint;
import org.jpedal.io.PathSerializer;
import org.jpedal.utils.repositories.Vector_Path;

public class T1Glyph extends BaseT1Glyph
  implements PdfGlyph
{
  private transient Vector_Path cached_current_path = null;
  private Paint strokePaint;
  Area glyphShape = null;
  int minX = 0;
  int minY = 0;
  int maxX = 0;
  int maxY = 0;

  public T1Glyph()
  {
  }

  public T1Glyph(Vector_Path paramVector_Path)
  {
    this.cached_current_path = paramVector_Path;
  }

  public void render(int paramInt, Graphics2D paramGraphics2D, float paramFloat, boolean paramBoolean)
  {
    if (this.cached_current_path != null)
    {
      GeneralPath[] arrayOfGeneralPath = this.cached_current_path.get();
      int i = arrayOfGeneralPath.length;
      for (int j = 0; (j < i) && (arrayOfGeneralPath[j] != null); j++)
      {
        if (paramInt == 2)
        {
          if (this.isStroked)
          {
            Paint localPaint = paramGraphics2D.getPaint();
            if ((!(localPaint instanceof PdfTexturePaint)) && (((Color)this.strokePaint).getRGB() != ((Color)localPaint).getRGB()) && (this.strokedPositions.containsKey(String.valueOf((int)paramGraphics2D.getTransform().getTranslateX()) + '-' + (int)paramGraphics2D.getTransform().getTranslateY())))
            {
              Stroke localStroke = paramGraphics2D.getStroke();
              paramGraphics2D.setPaint(this.strokePaint);
              float f2 = (float)(paramFloat / paramGraphics2D.getTransform().getScaleX());
              if (f2 < 0.0F)
                f2 = -f2;
              paramGraphics2D.setStroke(new BasicStroke(f2));
              paramGraphics2D.draw(arrayOfGeneralPath[j]);
              paramGraphics2D.setPaint(localPaint);
              paramGraphics2D.setStroke(localStroke);
            }
          }
          paramGraphics2D.fill(arrayOfGeneralPath[j]);
        }
        if (paramInt == 1)
        {
          if ((paramInt != 2) && (paramFloat > 1.0F))
          {
            float f1 = (float)(paramFloat / paramGraphics2D.getTransform().getScaleX());
            if (f1 < 0.0F)
              f1 = -f1;
            paramGraphics2D.setStroke(new BasicStroke(f1));
          }
          paramGraphics2D.draw(arrayOfGeneralPath[j]);
          this.strokePaint = paramGraphics2D.getPaint();
          this.strokedPositions.put(String.valueOf((int)paramGraphics2D.getTransform().getTranslateX()) + '-' + (int)paramGraphics2D.getTransform().getTranslateY(), "x");
        }
      }
    }
  }

  public Area getShape()
  {
    if ((this.cached_current_path != null) && (this.glyphShape == null))
    {
      GeneralPath[] arrayOfGeneralPath = this.cached_current_path.get();
      int i = arrayOfGeneralPath.length;
      for (int j = 1; (j < i) && (arrayOfGeneralPath[j] != null); j++)
        arrayOfGeneralPath[0].append(arrayOfGeneralPath[j], false);
      if ((arrayOfGeneralPath != null) && (arrayOfGeneralPath[0] != null))
        this.glyphShape = new Area(arrayOfGeneralPath[0]);
    }
    return this.glyphShape;
  }

  public void setPaths(Vector_Path paramVector_Path)
  {
    this.cached_current_path = paramVector_Path;
  }

  public void writePathsToStream(ObjectOutput paramObjectOutput)
    throws IOException
  {
    if (this.cached_current_path != null)
    {
      GeneralPath[] arrayOfGeneralPath = this.cached_current_path.get();
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

  public void flushArea()
  {
    this.glyphShape = null;
  }

  public int getFontBB(int paramInt)
  {
    if ((this.minX == 0) && (this.minY == 0) && (this.maxX == 0) && (this.maxY == 0) && (this.cached_current_path != null))
    {
      GeneralPath[] arrayOfGeneralPath = this.cached_current_path.get();
      int i = arrayOfGeneralPath.length;
      for (int j = 0; (j < i) && (arrayOfGeneralPath[j] != null); j++)
      {
        Rectangle localRectangle = arrayOfGeneralPath[j].getBounds();
        if (j == 0)
        {
          this.minX = localRectangle.x;
          this.minY = localRectangle.y;
          this.maxX = localRectangle.width;
          this.maxY = localRectangle.height;
        }
        else
        {
          if (this.minX > localRectangle.x)
            this.minX = localRectangle.x;
          if (this.minY > localRectangle.y)
            this.minY = localRectangle.y;
          if (this.maxX < localRectangle.width)
            this.maxX = localRectangle.width;
          if (this.maxY < localRectangle.height)
            this.maxY = localRectangle.height;
        }
      }
    }
    if (paramInt == 1)
      return this.minX;
    if (paramInt == 2)
      return this.minY;
    if (paramInt == 3)
      return this.maxX;
    if (paramInt == 4)
      return this.minY;
    return 0;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.fonts.glyph.T1Glyph
 * JD-Core Version:    0.6.2
 */