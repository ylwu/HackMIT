package org.jpedal.color;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.PaintContext;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.TexturePaint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.IOException;
import java.io.PrintStream;
import org.jpedal.examples.handlers.DefaultImageHelper;
import org.jpedal.io.ColorSpaceConvertor;
import org.jpedal.objects.raw.PdfObject;
import org.jpedal.render.DynamicVectorRenderer;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.Matrix;

public class RotatedTexturePaint
  implements PdfPaint
{
  DynamicVectorRenderer patternOnTile = null;
  private float[][] matrix;
  private float xStep;
  private float yStep;
  private float offsetXOnCanvas;
  private float offsetYOnCanvas;
  private float tileXoffset;
  private float tileYoffset;
  private AffineTransform imageScale;
  boolean cacheToTile;
  boolean isSideways;
  boolean isSkewed = false;
  boolean debug = false;
  BufferedImage img;
  PdfObject patternObj = null;

  public RotatedTexturePaint(boolean paramBoolean1, float[][] paramArrayOfFloat1, PdfObject paramPdfObject, boolean paramBoolean2, DynamicVectorRenderer paramDynamicVectorRenderer, float[][] paramArrayOfFloat2, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, AffineTransform paramAffineTransform)
  {
    this.patternObj = paramPdfObject;
    this.isSideways = paramBoolean1;
    this.isSkewed = ((paramArrayOfFloat1 != null) && (paramArrayOfFloat1[0][0] > 0.0F) && (paramArrayOfFloat1[0][1] < 0.0F) && (paramArrayOfFloat1[1][0] > 0.0F) && (paramArrayOfFloat1[1][1] > 0.0F));
    this.cacheToTile = paramBoolean2;
    this.patternOnTile = paramDynamicVectorRenderer;
    this.matrix = paramArrayOfFloat2;
    this.xStep = paramFloat1;
    this.yStep = paramFloat2;
    this.offsetXOnCanvas = paramFloat3;
    this.offsetYOnCanvas = paramFloat4;
    this.imageScale = paramAffineTransform;
    if (this.debug)
    {
      System.out.println("=======PatternObj=" + paramPdfObject.getObjectRefAsString());
      System.out.println("isSideways=" + paramBoolean1 + " isSkewed=" + this.isSkewed);
      System.out.println("cacheToTile=" + paramBoolean2);
      System.out.println("patternOnTile=" + paramDynamicVectorRenderer);
      System.out.println("xStep=" + paramFloat1 + " yStep=" + paramFloat2);
      System.out.println("offsetXOnCanvas=" + paramFloat3 + " offsetYOnCanvas=" + paramFloat4);
      System.out.println("imageScale=" + paramAffineTransform);
      Matrix.show(paramArrayOfFloat2);
    }
    if ((paramArrayOfFloat2[0][0] != 0.0F) && (paramArrayOfFloat2[1][1] != 0.0F))
    {
      this.tileXoffset = (paramFloat1 * paramArrayOfFloat2[0][1]);
      this.tileYoffset = Math.abs(paramFloat2 * paramArrayOfFloat2[1][0]);
      if (this.debug)
        System.out.println("set tileXoffset=" + this.tileXoffset + " tileYoffset=" + this.tileYoffset);
    }
  }

  public PaintContext createContext(ColorModel paramColorModel, Rectangle paramRectangle, Rectangle2D paramRectangle2D, AffineTransform paramAffineTransform, RenderingHints paramRenderingHints)
  {
    Graphics2D localGraphics2D1 = null;
    AffineTransform localAffineTransform1 = null;
    int i = (int)paramRectangle2D.getWidth();
    int j = (int)paramRectangle2D.getHeight();
    if (this.debug)
      System.out.println("area to fill w=" + i + " h=" + j);
    BufferedImage localBufferedImage1 = new BufferedImage(i, j, 2);
    Graphics2D localGraphics2D2 = localBufferedImage1.createGraphics();
    AffineTransform localAffineTransform2 = localGraphics2D2.getTransform();
    if (this.debug)
    {
      this.img = new BufferedImage(1000, 1000, 1);
      localGraphics2D1 = this.img.createGraphics();
      localGraphics2D1.translate(paramRectangle2D.getX(), paramRectangle2D.getY());
      localGraphics2D1.setPaint(Color.GREEN);
      int k = -1000;
      while (k < 1000)
      {
        localGraphics2D1.drawLine(-1000, k, 1000, k);
        localGraphics2D1.drawLine(k, -1000, k, 1000);
        k += 50;
      }
      localGraphics2D1.setPaint(Color.WHITE);
      localGraphics2D1.drawRect(0, 0, (int)paramRectangle2D.getWidth(), (int)paramRectangle2D.getHeight());
      localAffineTransform1 = localGraphics2D1.getTransform();
    }
    float f3;
    float f4;
    if (this.isSkewed)
    {
      f3 = this.xStep * this.matrix[0][0] + this.yStep * this.matrix[1][0];
      f4 = this.yStep * this.matrix[1][1] - this.xStep * this.matrix[0][1];
    }
    else
    {
      f3 = this.xStep * this.matrix[0][0] - this.yStep * this.matrix[1][0];
      if ((this.matrix[1][1] > 0.0F) && (this.matrix[0][1] > 0.0F))
        f4 = this.yStep * this.matrix[1][1];
      else
        f4 = -(this.yStep * this.matrix[1][1]) - this.xStep * this.matrix[0][1];
    }
    float f5 = paramRectangle2D.getBounds().width;
    float f6 = paramRectangle2D.getBounds().height;
    int m = (int)(f6 / f4);
    if (this.debug)
      System.out.println(m + " shapeW=" + f5 + " shapeH=" + f6 + " rotatedWidth=" + f3 + " rotatedHeight=" + f4);
    float f1;
    float f2;
    if (m > 1)
    {
      f1 = f5 - f4 * m;
      f2 = 5.0F - (f6 - f3 * m);
      if (this.debug)
        System.out.println("Multiple Rows OffsetX=" + f1 + " offsetY=" + f2 + " numberOfRows" + m + " rotatedWidth=" + f3 + " shapeH=" + f6);
    }
    else if (f4 > f5)
    {
      f1 = f4 - f5;
      f2 = f6 - f3;
      if (this.debug)
        System.out.println("rotatedHeight>shapeW OffsetX=" + f1 + " offsetY=" + f2);
    }
    else
    {
      f1 = f6 - f4;
      f2 = f5 - f3;
      if (this.debug)
        System.out.println("general case OffsetX=" + f1 + " offsetY=" + f2);
    }
    Rectangle localRectangle1 = this.patternOnTile.getOccupiedArea().getBounds();
    int i2 = 0;
    int i3 = 0;
    int n;
    if (localRectangle1.x < 0)
    {
      n = localRectangle1.width - localRectangle1.x;
      i2 = localRectangle1.x;
    }
    else
    {
      n = localRectangle1.width + localRectangle1.x;
    }
    int i1;
    if (localRectangle1.y < 0)
    {
      i1 = localRectangle1.height - localRectangle1.y;
      i3 = localRectangle1.y;
    }
    else
    {
      i1 = localRectangle1.height + localRectangle1.y;
    }
    BufferedImage localBufferedImage2 = null;
    if (this.isSideways)
    {
      this.imageScale = new AffineTransform();
      this.imageScale.scale(-1.0D, 1.0D);
      this.imageScale.translate(-(this.offsetXOnCanvas / paramAffineTransform.getScaleX()), 0.0D);
      if (this.debug)
        System.out.println("isSideWay imageScale=" + this.imageScale);
    }
    else if ((this.matrix[0][0] >= 0.0F) && (this.matrix[1][0] >= 0.0F) && (this.matrix[0][1] <= 0.0F) && (this.matrix[1][1] >= 0.0F))
    {
      this.imageScale = new AffineTransform();
      this.imageScale.scale(-1.0D, -1.0D);
      if (!this.isSkewed)
        this.imageScale.translate(0.0D, this.patternOnTile.getOccupiedArea().height - this.patternOnTile.getOccupiedArea().y);
      else
        this.imageScale.translate(0.0D, -(this.patternOnTile.getOccupiedArea().height - this.patternOnTile.getOccupiedArea().y));
      if (this.debug)
        System.out.println("LHS fit imageScale=" + this.imageScale);
    }
    else if ((this.matrix[0][0] >= 0.0F) && (this.matrix[1][0] <= 0.0F) && (this.matrix[0][1] >= 0.0F) && (this.matrix[1][1] >= 0.0F))
    {
      this.imageScale = new AffineTransform();
      this.imageScale.rotate(1.570796326794897D);
      this.imageScale.translate(-224.0D, -227.0D);
    }
    if ((paramRectangle2D.getBounds().width < this.patternOnTile.getOccupiedArea().width) && (paramRectangle2D.getBounds().height < this.patternOnTile.getOccupiedArea().height))
    {
      double d1;
      double d2;
      if (this.isSideways)
      {
        d1 = -localGraphics2D2.getTransform().getTranslateX() - (this.offsetXOnCanvas - paramRectangle.width);
        d2 = -localGraphics2D2.getTransform().getTranslateY() + (this.tileYoffset - this.offsetYOnCanvas);
        localGraphics2D2.translate(d1, d2);
        if (this.debug)
        {
          System.out.println("isSideways translate=" + d1 + ' ' + d2);
          localGraphics2D1.translate(d1, d2);
        }
      }
      else
      {
        d1 = n - f1 + 5.0F;
        d2 = -localGraphics2D2.getTransform().getTranslateY() + (this.tileYoffset - this.offsetYOnCanvas);
        localGraphics2D2.translate(d1, d2);
        if (this.debug)
        {
          System.out.println("translate=" + d1 + ' ' + d2);
          localGraphics2D1.translate(d1, d2);
        }
      }
      this.patternOnTile.setG2(localGraphics2D2);
      this.patternOnTile.paint(null, this.imageScale, null);
      if (this.debug)
      {
        this.patternOnTile.setG2(localGraphics2D1);
        this.patternOnTile.paint(null, this.imageScale, null);
      }
    }
    else
    {
      if (this.cacheToTile)
      {
        if (this.debug)
          System.out.println("cached to tile size " + n + ' ' + i1 + ' ' + this.xStep + ' ' + this.yStep);
        localBufferedImage2 = new BufferedImage(n, i1, 2);
        Graphics2D localGraphics2D3 = localBufferedImage2.createGraphics();
        localGraphics2D3.translate(-i2, -i3);
        this.patternOnTile.setG2(localGraphics2D3);
        this.patternOnTile.paint(null, null, null);
      }
      float f7 = j + this.yStep + this.offsetYOnCanvas;
      if (this.cacheToTile)
      {
        f7 += localBufferedImage2.getHeight() * 2;
        if (this.debug)
          System.out.println("adjust maxYY to " + f7);
      }
      float f8 = 0.0F;
      for (float f10 = 0.0F; f10 < f7; f10 += this.offsetYOnCanvas)
      {
        float f9 = -this.tileYoffset - this.tileYoffset;
        for (float f11 = -this.offsetXOnCanvas; f11 < i + this.xStep + this.offsetXOnCanvas; f11 += this.offsetXOnCanvas)
        {
          localGraphics2D2.translate(f1 + f11 + f8, f2 + f10 + f9);
          if (this.debug)
            localGraphics2D1.translate(f1 + f11 + f8, f2 + f10 + f9);
          Object localObject;
          if (this.cacheToTile)
          {
            localObject = new AffineTransform();
            ((AffineTransform)localObject).scale(1.0D, -1.0D);
            ((AffineTransform)localObject).translate(0.0D, localBufferedImage2.getHeight());
            ColorSpaceConvertor.drawImage(localGraphics2D2, localBufferedImage2, (AffineTransform)localObject, null);
            if (this.debug)
              ColorSpaceConvertor.drawImage(localGraphics2D1, localBufferedImage2, (AffineTransform)localObject, null);
          }
          else
          {
            localObject = this.patternOnTile.getOccupiedArea();
            float f12 = f1 + f11 + f8;
            float f13 = f2 + f10 + f9 - this.offsetYOnCanvas;
            float[] arrayOfFloat1 = { f12, f13, f12 + ((Rectangle)localObject).width, f13 + ((Rectangle)localObject).height };
            float[] arrayOfFloat2 = new float[arrayOfFloat1.length];
            AffineTransform localAffineTransform3 = new AffineTransform();
            localAffineTransform3.translate(paramRectangle2D.getX(), paramRectangle2D.getY());
            localAffineTransform3.transform(arrayOfFloat1, 0, arrayOfFloat2, 0, 2);
            Rectangle2D.Float localFloat = new Rectangle2D.Float(arrayOfFloat2[0], arrayOfFloat2[1], (float)((Rectangle)localObject).getWidth() + this.tileXoffset, (float)((Rectangle)localObject).getHeight() + this.tileYoffset);
            this.patternOnTile.setG2(localGraphics2D2);
            if ((paramRectangle2D.intersects(localFloat)) || ((this.matrix[0][0] >= 0.0F) && (this.matrix[1][0] <= 0.0F) && (this.matrix[0][1] >= 0.0F) && (this.matrix[1][1] >= 0.0F)))
            {
              this.patternOnTile.paint(null, this.imageScale, null);
              if (this.debug)
              {
                this.patternOnTile.setG2(localGraphics2D1);
                this.patternOnTile.paint(null, this.imageScale, null);
              }
            }
            else if (this.debug)
            {
              localGraphics2D1.setTransform(new AffineTransform());
              localGraphics2D1.setPaint(Color.ORANGE);
              localGraphics2D1.fill(localFloat);
            }
            if (this.debug)
            {
              localGraphics2D1.setTransform(new AffineTransform());
              localGraphics2D1.setPaint(Color.WHITE);
              localGraphics2D1.draw(localFloat);
            }
          }
          localGraphics2D2.setTransform(localAffineTransform2);
          if (this.debug)
            localGraphics2D1.setTransform(localAffineTransform1);
          if (this.isSkewed)
            f9 -= this.tileYoffset;
          else
            f9 += this.tileYoffset;
        }
        f8 -= this.tileXoffset;
      }
    }
    Rectangle localRectangle2 = paramRectangle2D.getBounds();
    TexturePaint localTexturePaint = new TexturePaint(localBufferedImage1, paramRectangle2D.getBounds());
    if (this.debug)
    {
      localGraphics2D1.setPaint(Color.BLUE);
      localGraphics2D1.setTransform(new AffineTransform());
      localGraphics2D1.draw(paramRectangle2D);
      String str = "/Users/markee/Desktop/Cases/";
      try
      {
        DefaultImageHelper.write(localBufferedImage1, "PNG", str + "wholeImage-" + this.patternObj.getObjectRefAsString() + '-' + localRectangle2.x + '-' + localRectangle2.y + ".png");
        DefaultImageHelper.write(this.img, "PNG", str + "Pattern-" + this.patternObj.getObjectRefAsString() + '-' + localRectangle2.x + '-' + localRectangle2.y + ".png");
      }
      catch (IOException localIOException1)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog("Exception: " + localIOException1.getMessage());
      }
      if (localBufferedImage2 != null)
      {
        BufferedImage localBufferedImage3 = new BufferedImage(1000, 1000, 1);
        Graphics2D localGraphics2D4 = localBufferedImage3.createGraphics();
        localGraphics2D4.setPaint(Color.CYAN);
        localGraphics2D4.fill(paramRectangle2D);
        localGraphics2D4.setPaint(Color.RED);
        localGraphics2D4.draw(paramRectangle2D);
        localGraphics2D4.drawImage(localBufferedImage1, localRectangle2.x, localRectangle2.y, localRectangle2.width, localRectangle2.height, null);
        try
        {
          DefaultImageHelper.write(localBufferedImage3, "PNG", str + "PatternOrig-" + this.patternObj.getObjectRefAsString() + '-' + localRectangle2.x + '-' + localRectangle2.y + "-wholeImage.png");
        }
        catch (IOException localIOException2)
        {
          if (LogWriter.isOutput())
            LogWriter.writeLog("Exception: " + localIOException2.getMessage());
        }
      }
    }
    return localTexturePaint.createContext(paramColorModel, paramRectangle, paramRectangle2D, paramAffineTransform, new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
  }

  public void setScaling(double paramDouble1, double paramDouble2, float paramFloat1, float paramFloat2, float paramFloat3)
  {
  }

  public boolean isPattern()
  {
    return false;
  }

  public void setPattern(int paramInt)
  {
  }

  public int getRGB()
  {
    return 0;
  }

  public int getTransparency()
  {
    return 0;
  }

  public void setRenderingType(int paramInt)
  {
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.color.RotatedTexturePaint
 * JD-Core Version:    0.6.2
 */