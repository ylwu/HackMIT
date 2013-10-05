package org.jpedal.render;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Set;
import javafx.scene.shape.Path;
import org.jpedal.color.ColorSpaces;
import org.jpedal.color.PdfColor;
import org.jpedal.color.PdfPaint;
import org.jpedal.exception.PdfException;
import org.jpedal.external.ColorHandler;
import org.jpedal.external.ImageHandler;
import org.jpedal.external.JPedalHelper;
import org.jpedal.fonts.PdfFont;
import org.jpedal.fonts.glyph.PdfGlyph;
import org.jpedal.io.ObjectStore;
import org.jpedal.objects.GraphicsState;
import org.jpedal.parser.DecoderOptions;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.Matrix;
import org.jpedal.utils.repositories.Vector_Rectangle;

public class BaseDisplay
  implements DynamicVectorRenderer
{
  protected int type;
  private int formNum = 0;
  boolean isType3Font = false;
  protected boolean addBackground = true;
  protected Vector_Rectangle areas;
  protected Vector_Rectangle imageAndShapeAreas;
  protected ObjectStore objectStoreRef;
  protected int currentItem = -1;
  protected int itemToRender = -1;
  protected int endItem = -1;
  protected int pageRotation = 0;
  Area lastClip = null;
  boolean hasClips = false;
  boolean colorsLocked;
  Graphics2D g2;
  boolean optimisedTurnCode = true;
  boolean useHiResImageForDisplay = false;
  boolean extraRot = false;
  String rawKey = null;
  PdfPaint fillCol = null;
  PdfPaint strokeCol = null;
  public int rawPageNumber = 0;
  int xx = 0;
  int yy = 0;
  public static boolean invertHighlight = false;
  boolean isPrinting;
  ImageHandler customImageHandler = null;
  ColorHandler customColorHandler = null;
  double cropX;
  double cropH;
  float scaling;
  float lastScaling;
  int w = 0;
  int h = 0;
  protected Color backgroundColor = Color.WHITE;
  protected Color textColor = null;
  protected int colorThresholdToReplace = 255;
  protected boolean changeLineArtAndText = false;
  public static RenderingHints userHints = null;
  private DynamicVectorRenderer.Mode mode = DynamicVectorRenderer.Mode.PDF;

  public void setInset(int paramInt1, int paramInt2)
  {
    this.xx = paramInt1;
    this.yy = paramInt2;
  }

  public void setG2(Graphics2D paramGraphics2D)
  {
    this.g2 = paramGraphics2D;
    if (userHints != null)
      this.g2.setRenderingHints(userHints);
  }

  public void init(int paramInt1, int paramInt2, int paramInt3, Color paramColor)
  {
    this.w = paramInt1;
    this.h = paramInt2;
    this.pageRotation = paramInt3;
    this.backgroundColor = paramColor;
  }

  public void paintBackground(Shape paramShape)
  {
    if ((this.addBackground) && (this.g2 != null))
    {
      this.g2.setColor(this.backgroundColor);
      if (paramShape == null)
        this.g2.fill(new Rectangle(this.xx, this.yy, (int)(this.w * this.scaling), (int)(this.h * this.scaling)));
      else
        this.g2.fill(paramShape);
    }
  }

  protected boolean checkColorThreshold(int paramInt)
  {
    int i = paramInt & 0xFF;
    int j = paramInt >> 8 & 0xFF;
    int k = paramInt >> 16 & 0xFF;
    return (i <= this.colorThresholdToReplace) && (j <= this.colorThresholdToReplace) && (k <= this.colorThresholdToReplace);
  }

  void renderEmbeddedText(int paramInt1, Object paramObject, int paramInt2, AffineTransform paramAffineTransform, Rectangle paramRectangle, PdfPaint paramPdfPaint1, PdfPaint paramPdfPaint2, float paramFloat1, float paramFloat2, int paramInt3)
  {
    float f1 = 0.0F;
    if ((paramInt1 == 1) && (paramInt3 >= 1.0D))
      f1 = this.scaling;
    PdfGlyph localPdfGlyph = (PdfGlyph)paramObject;
    AffineTransform localAffineTransform = this.g2.getTransform();
    double[] arrayOfDouble = new double[6];
    localAffineTransform.getMatrix(arrayOfDouble);
    if (localPdfGlyph != null)
    {
      Stroke localStroke = this.g2.getStroke();
      if (paramInt3 != 0)
      {
        float f2 = paramInt3 * (float)this.g2.getTransform().getScaleX();
        if (f2 < 0.0F)
          f2 = -f2;
        this.g2.setStroke(new BasicStroke(f2));
      }
      this.g2.transform(paramAffineTransform);
      Composite localComposite = this.g2.getComposite();
      Color localColor;
      if ((paramInt1 & 0x2) == 2)
      {
        if ((this.textColor != null) && ((this.itemToRender == -1) || (this.endItem == -1) || (this.itemToRender <= this.endItem)) && (checkColorThreshold(paramPdfPaint2.getRGB())))
          paramPdfPaint2 = new PdfColor(this.textColor.getRed(), this.textColor.getGreen(), this.textColor.getBlue());
        paramPdfPaint2.setScaling(this.cropX, this.cropH, this.scaling, 0.0F, 0.0F);
        if (this.customColorHandler != null)
          this.customColorHandler.setPaint(this.g2, paramPdfPaint2, this.rawPageNumber, this.isPrinting);
        else if (DecoderOptions.Helper != null)
          DecoderOptions.Helper.setPaint(this.g2, paramPdfPaint2, this.rawPageNumber, this.isPrinting);
        else
          this.g2.setPaint(paramPdfPaint2);
        if (paramFloat2 != 1.0F)
          this.g2.setComposite(AlphaComposite.getInstance(3, paramFloat2));
        if (paramRectangle != null)
          if (invertHighlight)
          {
            localColor = this.g2.getColor();
            this.g2.setColor(new Color(255 - localColor.getRed(), 255 - localColor.getGreen(), 255 - localColor.getBlue()));
          }
          else if (DecoderOptions.backgroundColor != null)
          {
            this.g2.setColor(DecoderOptions.backgroundColor);
          }
        if ((paramInt2 == 6) && (!localPdfGlyph.ignoreColors()))
          localPdfGlyph.setT3Colors(paramPdfPaint1, paramPdfPaint2, false);
        localPdfGlyph.render(2, this.g2, this.scaling, false);
        this.g2.setComposite(localComposite);
      }
      if (paramInt1 == 1)
        localPdfGlyph.setStrokedOnly(true);
      if (((!DecoderOptions.isRunningOnMac) || (!this.isPrinting) || (paramInt1 != 3)) && ((paramInt1 & 0x1) == 1))
      {
        if (paramPdfPaint1 != null)
        {
          if ((this.textColor != null) && ((this.itemToRender == -1) || (this.endItem == -1) || (this.itemToRender <= this.endItem)) && (checkColorThreshold(paramPdfPaint1.getRGB())))
            paramPdfPaint1 = new PdfColor(this.textColor.getRed(), this.textColor.getGreen(), this.textColor.getBlue());
          paramPdfPaint1.setScaling(this.cropX, this.cropH, this.scaling, 0.0F, 0.0F);
        }
        if (this.customColorHandler != null)
          this.customColorHandler.setPaint(this.g2, paramPdfPaint1, this.rawPageNumber, this.isPrinting);
        else if (DecoderOptions.Helper != null)
          DecoderOptions.Helper.setPaint(this.g2, paramPdfPaint1, this.rawPageNumber, this.isPrinting);
        else
          this.g2.setPaint(paramPdfPaint1);
        if (paramFloat1 != 1.0F)
          this.g2.setComposite(AlphaComposite.getInstance(3, paramFloat1));
        if (paramRectangle != null)
          if (invertHighlight)
          {
            localColor = this.g2.getColor();
            this.g2.setColor(new Color(255 - localColor.getRed(), 255 - localColor.getGreen(), 255 - localColor.getBlue()));
          }
          else if (DecoderOptions.backgroundColor != null)
          {
            this.g2.setColor(DecoderOptions.backgroundColor);
          }
        try
        {
          localPdfGlyph.render(1, this.g2, f1, false);
        }
        catch (Exception localException)
        {
          if (LogWriter.isOutput())
            LogWriter.writeLog("Exception: " + localException.getMessage());
        }
        this.g2.setComposite(localComposite);
      }
      this.g2.setTransform(localAffineTransform);
      if (paramInt3 != 0)
        this.g2.setStroke(localStroke);
    }
  }

  public void renderXForm(DynamicVectorRenderer paramDynamicVectorRenderer, float paramFloat)
  {
    Shape localShape = null;
    if (this.g2 != null)
      localShape = this.g2.getClip();
    Rectangle localRectangle = paramDynamicVectorRenderer.getOccupiedArea();
    int i = localRectangle.x;
    int j = localRectangle.width;
    int k = localRectangle.y;
    int m = localRectangle.height;
    if (i < 0)
      i = 0;
    if (k > 0)
      m += k;
    if ((j <= 0) || (m <= 0))
      return;
    BufferedImage localBufferedImage = new BufferedImage(j, m, 2);
    Graphics2D localGraphics2D = localBufferedImage.createGraphics();
    if (this.g2 == null)
    {
      localGraphics2D.scale(1.0D, -1.0D);
      localGraphics2D.translate(0, -m);
    }
    localGraphics2D.translate(-i, 0);
    paramDynamicVectorRenderer.setG2(localGraphics2D);
    paramDynamicVectorRenderer.paint(null, null, null);
    if (this.g2 != null)
      this.g2.setClip(null);
    GraphicsState localGraphicsState = new GraphicsState(false);
    localGraphicsState.CTM[0][0] = j;
    localGraphicsState.CTM[1][0] = 0.0F;
    localGraphicsState.CTM[2][0] = 0.0F;
    localGraphicsState.CTM[0][1] = 0.0F;
    localGraphicsState.CTM[1][1] = m;
    localGraphicsState.CTM[2][1] = 0.0F;
    localGraphicsState.CTM[0][2] = 0.0F;
    localGraphicsState.CTM[1][2] = 0.0F;
    localGraphicsState.CTM[2][2] = 1.0F;
    if (this.g2 != null)
    {
      this.g2.translate(i, 0);
      renderImage(new AffineTransform(), localBufferedImage, paramFloat, localGraphicsState, localRectangle.x, localRectangle.y, 1);
      this.g2.translate(-i, 0);
      this.g2.setClip(localShape);
    }
    else
    {
      localGraphicsState.CTM[2][0] = localRectangle.x;
      localGraphicsState.CTM[2][1] = (localRectangle.y - m + localRectangle.height);
      drawImage(this.rawPageNumber, localBufferedImage, localGraphicsState, false, "xf" + this.formNum, 0, -1);
      this.formNum += 1;
    }
  }

  final void renderShape(Shape paramShape1, int paramInt, PdfPaint paramPdfPaint1, PdfPaint paramPdfPaint2, Stroke paramStroke, Shape paramShape2, float paramFloat1, float paramFloat2)
  {
    int i = 0;
    Shape localShape = this.g2.getClip();
    Composite localComposite = this.g2.getComposite();
    if ((paramInt == 2) || (paramInt == 3))
    {
      if ((paramPdfPaint2.getRGB() != -1) && (this.changeLineArtAndText) && (this.textColor != null) && (!paramPdfPaint2.isPattern()) && ((this.itemToRender == -1) || (this.endItem == -1) || (this.itemToRender <= this.endItem)) && (checkColorThreshold(paramPdfPaint2.getRGB())))
        paramPdfPaint2 = new PdfColor(this.textColor.getRed(), this.textColor.getGreen(), this.textColor.getBlue());
      paramPdfPaint2.setScaling(this.cropX, this.cropH, this.scaling, 0.0F, 0.0F);
      if (this.customColorHandler != null)
        this.customColorHandler.setPaint(this.g2, paramPdfPaint2, this.rawPageNumber, this.isPrinting);
      else if (DecoderOptions.Helper != null)
        DecoderOptions.Helper.setPaint(this.g2, paramPdfPaint2, this.rawPageNumber, this.isPrinting);
      else
        this.g2.setPaint(paramPdfPaint2);
      if (paramFloat2 != 1.0F)
        this.g2.setComposite(AlphaComposite.getInstance(3, paramFloat2));
      try
      {
        double d1 = paramShape2.getBounds2D().getWidth();
        double d2 = paramShape2.getBounds2D().getHeight();
        if (((d2 == 0.0D) || (d1 == 0.0D)) && (((BasicStroke)this.g2.getStroke()).getLineWidth() <= 1.0F))
          this.g2.fillRect(paramShape2.getBounds().x, paramShape2.getBounds().y, paramShape2.getBounds().width, paramShape2.getBounds().height);
        else
          this.g2.fill(paramShape2);
      }
      catch (Exception localException)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog("Exception " + localException + " filling shape");
      }
      this.g2.setComposite(localComposite);
    }
    if ((paramInt == 1) || (paramInt == 3))
    {
      Stroke localStroke = this.g2.getStroke();
      if ((paramShape2.getBounds2D().getWidth() < 1.0D) && (((BasicStroke)paramStroke).getLineWidth() > 10.0F))
        this.g2.setStroke(new BasicStroke(1.0F));
      else
        this.g2.setStroke(paramStroke);
      if ((this.changeLineArtAndText) && (this.textColor != null) && (!paramPdfPaint1.isPattern()) && ((this.itemToRender == -1) || (this.endItem == -1) || (this.itemToRender <= this.endItem)) && (checkColorThreshold(paramPdfPaint1.getRGB())))
        paramPdfPaint1 = new PdfColor(this.textColor.getRed(), this.textColor.getGreen(), this.textColor.getBlue());
      paramPdfPaint1.setScaling(this.cropX, this.cropH, this.scaling, 0.0F, 0.0F);
      if (this.customColorHandler != null)
        this.customColorHandler.setPaint(this.g2, paramPdfPaint1, this.rawPageNumber, this.isPrinting);
      else if (DecoderOptions.Helper != null)
        DecoderOptions.Helper.setPaint(this.g2, paramPdfPaint1, this.rawPageNumber, this.isPrinting);
      else
        this.g2.setPaint(paramPdfPaint1);
      if (paramFloat1 != 1.0F)
        this.g2.setComposite(AlphaComposite.getInstance(3, paramFloat1));
      if ((!this.isPrinting) && (localShape != null) && ((localShape.getBounds2D().getHeight() < 1.0D) || (localShape.getBounds2D().getWidth() < 1.0D)))
      {
        this.g2.setClip(paramShape1);
        i = 1;
      }
      this.g2.draw(paramShape2);
      this.g2.setStroke(localStroke);
      this.g2.setComposite(localComposite);
    }
    if (i != 0)
      this.g2.setClip(localShape);
  }

  final void renderImage(AffineTransform paramAffineTransform, BufferedImage paramBufferedImage, float paramFloat1, GraphicsState paramGraphicsState, float paramFloat2, float paramFloat3, int paramInt)
  {
    boolean bool1 = paramGraphicsState != null;
    if (paramBufferedImage == null)
      return;
    int i = paramBufferedImage.getWidth();
    int j = paramBufferedImage.getHeight();
    AffineTransform localAffineTransform1 = new AffineTransform();
    int k = 0;
    float[][] arrayOfFloat = new float[3][3];
    if (paramGraphicsState != null)
      arrayOfFloat = paramGraphicsState.CTM;
    if ((arrayOfFloat[0][0] < 0.0F) && (arrayOfFloat[1][1] < 0.0F) && (arrayOfFloat[1][0] > -2.0F) && (arrayOfFloat[1][0] < 0.0F) && (arrayOfFloat[0][1] > 0.0F) && (arrayOfFloat[0][1] < 10.0F))
    {
      arrayOfFloat[0][1] = 0.0F;
      arrayOfFloat[1][0] = 0.0F;
    }
    AffineTransform localAffineTransform2 = this.g2.getTransform();
    int m = 0;
    float f1 = 0.0F;
    float f2 = 0.0F;
    if ((bool1) || (this.useHiResImageForDisplay))
    {
      if (bool1)
      {
        localAffineTransform1 = null;
        if ((paramInt & 0x1) != 1)
          if (!this.optimisedTurnCode)
          {
            paramBufferedImage = RenderUtils.invertImage(paramBufferedImage);
          }
          else if ((arrayOfFloat[0][1] < 0.0F) && (arrayOfFloat[1][0] > 0.0F) && (arrayOfFloat[0][0] * arrayOfFloat[1][1] == 0.0F))
          {
            localAffineTransform1 = new AffineTransform(arrayOfFloat[0][0] / i, arrayOfFloat[0][1] / i, -arrayOfFloat[1][0] / j, arrayOfFloat[1][1] / j, arrayOfFloat[2][0] + arrayOfFloat[1][0], arrayOfFloat[2][1]);
          }
          else if ((arrayOfFloat[0][1] < 0.0F) || (arrayOfFloat[1][0] < 0.0F))
          {
            localObject1 = new float[][] { { 1.0F / i, 0.0F, 0.0F }, { 0.0F, -1.0F / j, 0.0F }, { 0.0F, 1.0F / j, 1.0F } };
            localObject2 = new float[][] { { arrayOfFloat[0][0], arrayOfFloat[0][1], 0.0F }, { arrayOfFloat[1][0], arrayOfFloat[1][1], 0.0F }, { 0.0F, 0.0F, 1.0F } };
            localObject1 = Matrix.multiply((float[][])localObject1, (float[][])localObject2);
            localAffineTransform1 = new AffineTransform(localObject1[0][0], localObject1[0][1], localObject1[1][0], localObject1[1][1], localObject1[2][0], localObject1[2][1]);
            f1 = arrayOfFloat[2][0] - paramBufferedImage.getHeight() * localObject1[1][0];
            f2 = arrayOfFloat[2][1];
            f2 += arrayOfFloat[1][1];
          }
          else if ((arrayOfFloat[0][0] * arrayOfFloat[0][1] == 0.0F) && (arrayOfFloat[1][1] * arrayOfFloat[1][0] == 0.0F) && (arrayOfFloat[0][1] > 0.0F) && (arrayOfFloat[1][0] > 0.0F))
          {
            localObject1 = new float[][] { { -1.0F / i, 0.0F, 0.0F }, { 0.0F, 1.0F / j, 0.0F }, { 1.0F / i, 0.0F, 1.0F } };
            localObject2 = new float[][] { { arrayOfFloat[0][0], arrayOfFloat[0][1], 0.0F }, { arrayOfFloat[1][0], arrayOfFloat[1][1], 0.0F }, { 0.0F, 0.0F, 1.0F } };
            localObject1 = Matrix.multiply((float[][])localObject1, (float[][])localObject2);
            localAffineTransform1 = new AffineTransform(localObject1[0][0], localObject1[1][0], localObject1[0][1], localObject1[1][1], localObject1[2][0], localObject1[2][1]);
            f1 = arrayOfFloat[2][0] - paramBufferedImage.getHeight() * localObject1[0][1];
            f2 = arrayOfFloat[2][1];
          }
          else if (arrayOfFloat[1][1] != 0.0F)
          {
            m = 1;
          }
        if (localAffineTransform1 == null)
          localAffineTransform1 = new AffineTransform(arrayOfFloat[0][0] / i, arrayOfFloat[0][1] / i, arrayOfFloat[1][0] / j, arrayOfFloat[1][1] / j, arrayOfFloat[2][0], arrayOfFloat[2][1]);
      }
      else
      {
        localAffineTransform1 = paramAffineTransform;
        m = (paramInt & 0x4) == 4 ? 1 : 0;
      }
      k = 1;
    }
    Object localObject1 = this.g2.getComposite();
    Object localObject2 = this.g2.getClip();
    int n = 0;
    if (paramFloat1 != 1.0F)
      this.g2.setComposite(AlphaComposite.getInstance(3, paramFloat1));
    Object localObject3;
    if ((this.isType3Font) && (this.fillCol != null))
    {
      localObject3 = new int[4];
      int i1 = this.fillCol.getRGB();
      localObject3[0] = (i1 >> 16 & 0xFF);
      localObject3[1] = (i1 >> 8 & 0xFF);
      localObject3[2] = (i1 & 0xFF);
      localObject3[3] = 255;
      if ((localObject3[0] != 0) || (localObject3[1] != 0) || (localObject3[2] != 0))
      {
        if ((paramBufferedImage.getType() == 10) && (localObject3[0] > 250) && (localObject3[1] > 250) && (localObject3[2] > 250))
          return;
        BufferedImage localBufferedImage = new BufferedImage(paramBufferedImage.getWidth(), paramBufferedImage.getHeight(), paramBufferedImage.getType());
        WritableRaster localWritableRaster1 = paramBufferedImage.getRaster();
        WritableRaster localWritableRaster2 = localBufferedImage.getRaster();
        int[] arrayOfInt = new int[4];
        for (int i3 = 0; i3 < paramBufferedImage.getHeight(); i3++)
          for (int i4 = 0; i4 < paramBufferedImage.getWidth(); i4++)
          {
            localWritableRaster1.getPixel(i4, i3, arrayOfInt);
            if (arrayOfInt[3] > 2)
              localWritableRaster2.setPixel(i4, i3, (int[])localObject3);
          }
        paramBufferedImage = localBufferedImage;
      }
    }
    Object localObject5;
    if ((bool1) || (this.useHiResImageForDisplay))
      try
      {
        if ((this.optimisedTurnCode) && (m != 0) && ((paramInt & 0x1) != 1))
        {
          localObject3 = new double[6];
          localAffineTransform1.getMatrix((double[])localObject3);
          f1 = (float)(localObject3[4] + localObject3[1] * paramBufferedImage.getWidth());
          f2 = (float)(localObject3[5] + paramBufferedImage.getHeight() * localObject3[3]);
          if ((localObject3[0] > 0.0D) && (localObject3[1] > 0.0D) && (localObject3[2] > 0.0D) && (localObject3[3] < 0.0D))
            localObject3[2] = (-localObject3[2]);
          localObject3[3] = (-localObject3[3]);
          localObject3[4] = 0.0D;
          localObject3[5] = 0.0D;
          localAffineTransform1 = new AffineTransform((double[])localObject3);
        }
        boolean bool2 = this.customImageHandler != null;
        if (bool2)
          bool2 = this.customImageHandler.drawImageOnscreen(paramBufferedImage, paramInt, localAffineTransform1, null, this.g2, bool1, this.objectStoreRef, this.isPrinting);
        if (bool2)
        {
          this.g2.setComposite((Composite)localObject1);
          return;
        }
        this.g2.translate(f1, f2);
        if (this.customColorHandler != null)
        {
          localObject5 = this.customColorHandler.processImage(paramBufferedImage, this.rawPageNumber, this.isPrinting);
          if (localObject5 != null)
            paramBufferedImage = (BufferedImage)localObject5;
        }
        else if (DecoderOptions.Helper != null)
        {
          localObject5 = DecoderOptions.Helper.processImage(paramBufferedImage, this.rawPageNumber, this.isPrinting);
          if (localObject5 != null)
            paramBufferedImage = (BufferedImage)localObject5;
        }
        localObject5 = this.g2.getClip();
        int i2 = 0;
        if (localObject5 != null)
        {
          double d1 = this.g2.getClip().getBounds2D().getY();
          double d2 = this.g2.getClip().getBounds2D().getHeight();
          double d3 = paramBufferedImage.getHeight() - d2;
          if (d3 < 0.0D)
            d3 = -d3;
          if ((d3 > 0.0D) && (d3 < 1.0D) && (d1 < 0.0D) && (paramBufferedImage.getHeight() > 1) && (paramBufferedImage.getHeight() < 10))
          {
            int i5 = 0;
            PathIterator localPathIterator = this.g2.getClip().getPathIterator(null);
            while ((!localPathIterator.isDone()) && (i5 < 6))
            {
              localPathIterator.next();
              i5++;
            }
            if (i5 < 6)
            {
              double d4 = this.g2.getClip().getBounds2D().getX();
              double d5 = this.g2.getClip().getBounds2D().getWidth();
              this.g2.setClip(new Rectangle((int)d4, (int)d1, (int)d5, (int)d2));
              i2 = 0;
            }
          }
        }
        this.g2.drawImage(paramBufferedImage, localAffineTransform1, null);
        if (i2 != 0)
          this.g2.setClip((Shape)localObject5);
      }
      catch (Exception localException1)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog("Exception: " + localException1.getMessage());
      }
    else
      try
      {
        Object localObject4;
        if (k != 0)
        {
          localObject4 = new AffineTransformOp(localAffineTransform1, ColorSpaces.hints);
          paramBufferedImage = ((AffineTransformOp)localObject4).filter(paramBufferedImage, null);
        }
        this.g2.translate(paramFloat2, paramFloat3);
        if ((this.optimisedTurnCode) && ((paramInt & 0x4) == 4))
        {
          localObject5 = new float[] { 1.0F, 0.0F, 0.0F, -1.0F, 0.0F, paramBufferedImage.getHeight() };
          if (this.pageRotation == 0)
          {
            localObject4 = new AffineTransform((float[])localObject5);
          }
          else
          {
            AffineTransform localAffineTransform3;
            if (this.pageRotation == 90)
            {
              localObject4 = new AffineTransform();
              if (this.extraRot)
                ((AffineTransform)localObject4).rotate(3.141592653589793D, 0.0D, 0.0D);
              else
                ((AffineTransform)localObject4).rotate(1.570796326794897D, 0.0D, 0.0D);
              ((AffineTransform)localObject4).translate(-paramBufferedImage.getWidth(), -paramBufferedImage.getHeight());
              localObject5 = new float[] { -1.0F, 0.0F, 0.0F, 1.0F, paramBufferedImage.getWidth(), 0.0F };
              localAffineTransform3 = new AffineTransform((float[])localObject5);
              ((AffineTransform)localObject4).concatenate(localAffineTransform3);
            }
            else if (this.pageRotation == 180)
            {
              localObject4 = new AffineTransform();
              if (this.extraRot)
                ((AffineTransform)localObject4).rotate(3.141592653589793D, 0.0D, 0.0D);
              ((AffineTransform)localObject4).translate(-paramBufferedImage.getWidth(), -paramBufferedImage.getHeight());
              localObject5 = new float[] { -1.0F, 0.0F, 0.0F, 1.0F, paramBufferedImage.getWidth(), 0.0F };
              localAffineTransform3 = new AffineTransform((float[])localObject5);
              ((AffineTransform)localObject4).concatenate(localAffineTransform3);
            }
            else
            {
              localObject4 = new AffineTransform();
              if (this.extraRot)
                ((AffineTransform)localObject4).rotate(3.141592653589793D, 0.0D, 0.0D);
              else
                ((AffineTransform)localObject4).rotate(4.71238898038469D, 0.0D, 0.0D);
              ((AffineTransform)localObject4).translate(-paramBufferedImage.getWidth(), -paramBufferedImage.getHeight());
              localObject5 = new float[] { -1.0F, 0.0F, 0.0F, 1.0F, paramBufferedImage.getWidth(), 0.0F };
              localAffineTransform3 = new AffineTransform((float[])localObject5);
              ((AffineTransform)localObject4).concatenate(localAffineTransform3);
            }
          }
          this.g2.drawImage(paramBufferedImage, (AffineTransform)localObject4, null);
        }
        else
        {
          this.g2.drawImage(paramBufferedImage, 0, 0, null);
        }
        this.g2.translate(-paramFloat2, -paramFloat3);
      }
      catch (Exception localException2)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog("Exception: " + localException2.getMessage());
      }
    this.g2.setTransform(localAffineTransform2);
    if ((n != 0) && (DecoderOptions.isRunningOnMac) && (localObject2 != null))
      this.g2.setClip((Shape)localObject2);
    this.g2.setComposite((Composite)localObject1);
  }

  final void renderText(float paramFloat1, float paramFloat2, int paramInt, Area paramArea, Rectangle paramRectangle, PdfPaint paramPdfPaint1, PdfPaint paramPdfPaint2, float paramFloat3, float paramFloat4)
  {
    Paint localPaint = this.g2.getPaint();
    Composite localComposite = this.g2.getComposite();
    Color localColor;
    if ((paramInt & 0x2) == 2)
    {
      if (paramPdfPaint2 != null)
      {
        if ((this.textColor != null) && ((this.itemToRender == -1) || (this.endItem == -1) || (this.itemToRender <= this.endItem)) && (checkColorThreshold(paramPdfPaint2.getRGB())))
          paramPdfPaint2 = new PdfColor(this.textColor.getRed(), this.textColor.getGreen(), this.textColor.getBlue());
        paramPdfPaint2.setScaling(this.cropX, this.cropH, this.scaling, paramFloat1, paramFloat2);
      }
      if (this.customColorHandler != null)
        this.customColorHandler.setPaint(this.g2, paramPdfPaint2, this.rawPageNumber, this.isPrinting);
      else if (DecoderOptions.Helper != null)
        DecoderOptions.Helper.setPaint(this.g2, paramPdfPaint2, this.rawPageNumber, this.isPrinting);
      else
        this.g2.setPaint(paramPdfPaint2);
      if (paramFloat4 != 1.0F)
        this.g2.setComposite(AlphaComposite.getInstance(3, paramFloat4));
      if (paramRectangle != null)
        if (invertHighlight)
        {
          localColor = this.g2.getColor();
          this.g2.setColor(new Color(255 - localColor.getRed(), 255 - localColor.getGreen(), 255 - localColor.getBlue()));
        }
        else if (DecoderOptions.backgroundColor != null)
        {
          this.g2.setColor(DecoderOptions.backgroundColor);
        }
      this.g2.fill(paramArea);
      this.g2.setComposite(localComposite);
    }
    if ((paramInt & 0x1) == 1)
    {
      if (paramPdfPaint1 != null)
      {
        if ((this.textColor != null) && ((this.itemToRender == -1) || (this.endItem == -1) || (this.itemToRender <= this.endItem)) && (checkColorThreshold(paramPdfPaint1.getRGB())))
          paramPdfPaint1 = new PdfColor(this.textColor.getRed(), this.textColor.getGreen(), this.textColor.getBlue());
        paramPdfPaint1.setScaling(this.cropX + paramFloat1, this.cropH + paramFloat2, this.scaling, paramFloat1, paramFloat2);
      }
      if (this.customColorHandler != null)
        this.customColorHandler.setPaint(this.g2, paramPdfPaint1, this.rawPageNumber, this.isPrinting);
      else if (DecoderOptions.Helper != null)
        DecoderOptions.Helper.setPaint(this.g2, paramPdfPaint1, this.rawPageNumber, this.isPrinting);
      else
        this.g2.setPaint(paramPdfPaint1);
      if (paramFloat3 != 1.0F)
        this.g2.setComposite(AlphaComposite.getInstance(3, paramFloat3));
      if (paramRectangle != null)
        if (invertHighlight)
        {
          localColor = this.g2.getColor();
          this.g2.setColor(new Color(255 - localColor.getRed(), 255 - localColor.getGreen(), 255 - localColor.getBlue()));
        }
        else if (DecoderOptions.backgroundColor != null)
        {
          this.g2.setColor(DecoderOptions.backgroundColor);
        }
      float f = (float)(1.0D / this.g2.getTransform().getScaleX());
      if (f < 0.0F)
        f = -f;
      this.g2.setStroke(new BasicStroke(f));
      if (f < 0.1F)
        this.g2.draw(paramArea);
      else
        this.g2.fill(paramArea);
      this.g2.setComposite(localComposite);
    }
    this.g2.setPaint(localPaint);
  }

  public ObjectStore getObjectStore()
  {
    return this.objectStoreRef;
  }

  public void setHiResImageForDisplayMode(boolean paramBoolean)
  {
    this.useHiResImageForDisplay = paramBoolean;
  }

  public void setScalingValues(double paramDouble1, double paramDouble2, float paramFloat)
  {
    this.cropX = paramDouble1;
    this.cropH = paramDouble2;
    this.scaling = paramFloat;
  }

  public void setCustomImageHandler(ImageHandler paramImageHandler)
  {
    this.customImageHandler = paramImageHandler;
  }

  public void setCustomColorHandler(ColorHandler paramColorHandler)
  {
    this.customColorHandler = paramColorHandler;
  }

  public void resetOnColorspaceChange()
  {
  }

  public void drawFontBounds(Rectangle paramRectangle)
  {
  }

  public void drawAffine(double[] paramArrayOfDouble)
  {
  }

  public void drawFontSize(int paramInt)
  {
  }

  public void setLineWidth(int paramInt)
  {
  }

  public void stopClearOnNextRepaint(boolean paramBoolean)
  {
  }

  public boolean hasObjectsBehind(float[][] paramArrayOfFloat)
  {
    boolean bool = false;
    int i = (int)paramArrayOfFloat[2][0];
    int j = (int)paramArrayOfFloat[2][1];
    int k = (int)paramArrayOfFloat[0][0];
    if (k == 0)
      k = (int)paramArrayOfFloat[0][1];
    int m = (int)paramArrayOfFloat[1][1];
    if (m == 0)
      m = (int)paramArrayOfFloat[1][0];
    if (m < 0)
      j += m;
    if (k < 0)
    {
      i += k;
      k = i - k;
    }
    if (this.areas == null)
      return true;
    Rectangle[] arrayOfRectangle = this.areas.get();
    int n = arrayOfRectangle.length;
    for (int i5 = 0; i5 < n; i5++)
      if (arrayOfRectangle[i5] != null)
      {
        int i1 = arrayOfRectangle[i5].x;
        int i2 = arrayOfRectangle[i5].y;
        int i3 = arrayOfRectangle[i5].width;
        int i4 = arrayOfRectangle[i5].height;
        int i6 = (valueInRange(i, i1, i1 + i3)) || (valueInRange(i1, i, i + k)) ? 1 : 0;
        int i7 = ((i6 != 0) && (valueInRange(j, i2, i2 + i4))) || (valueInRange(i2, j, j + i4)) ? 1 : 0;
        if ((i6 != 0) && (i7 != 0))
        {
          i5 = n;
          bool = true;
        }
      }
    return bool;
  }

  private static boolean valueInRange(int paramInt1, int paramInt2, int paramInt3)
  {
    return (paramInt1 >= paramInt2) && (paramInt1 <= paramInt3);
  }

  public void flagDecodingFinished()
  {
  }

  public void flagImageDeleted(int paramInt)
  {
  }

  public void setOCR(boolean paramBoolean)
  {
  }

  public byte[] serializeToByteArray(Set paramSet)
    throws IOException
  {
    return new byte[0];
  }

  public void checkFontSaved(Object paramObject, String paramString, PdfFont paramPdfFont)
  {
  }

  public Rectangle getArea(int paramInt)
  {
    return null;
  }

  public int isInsideImage(int paramInt1, int paramInt2)
  {
    return 0;
  }

  public void saveImage(int paramInt, String paramString1, String paramString2)
  {
  }

  public int getType()
  {
    return this.type;
  }

  public int getObjectUnderneath(int paramInt1, int paramInt2)
  {
    return 0;
  }

  public void setneedsVerticalInvert(boolean paramBoolean)
  {
  }

  public void setneedsHorizontalInvert(boolean paramBoolean)
  {
  }

  public void stopG2HintSetting(boolean paramBoolean)
  {
  }

  public void setPrintPage(int paramInt)
  {
  }

  public void drawShape(Shape paramShape, GraphicsState paramGraphicsState, int paramInt)
  {
  }

  public void drawCustom(Object paramObject)
  {
  }

  public void drawEmbeddedText(float[][] paramArrayOfFloat, int paramInt1, PdfGlyph paramPdfGlyph, Object paramObject, int paramInt2, GraphicsState paramGraphicsState, AffineTransform paramAffineTransform, String paramString, PdfFont paramPdfFont, float paramFloat)
  {
  }

  public Rectangle paint(Rectangle[] paramArrayOfRectangle, AffineTransform paramAffineTransform, Rectangle paramRectangle)
  {
    return null;
  }

  public void setMessageFrame(Container paramContainer)
  {
  }

  public void dispose()
  {
  }

  public int drawImage(int paramInt1, BufferedImage paramBufferedImage, GraphicsState paramGraphicsState, boolean paramBoolean, String paramString, int paramInt2, int paramInt3)
  {
    return -1;
  }

  public void drawXForm(DynamicVectorRenderer paramDynamicVectorRenderer, GraphicsState paramGraphicsState)
  {
  }

  public void drawFillColor(PdfPaint paramPdfPaint)
  {
  }

  public void drawAdditionalObjectsOverPage(int[] paramArrayOfInt, Color[] paramArrayOfColor, Object[] paramArrayOfObject)
    throws PdfException
  {
  }

  public void flushAdditionalObjOnPage()
  {
  }

  public void setOptimsePainting(boolean paramBoolean)
  {
  }

  public void flush()
  {
  }

  public void drawText(float[][] paramArrayOfFloat, String paramString, GraphicsState paramGraphicsState, float paramFloat1, float paramFloat2, Font paramFont)
  {
  }

  public Rectangle getOccupiedArea()
  {
    return null;
  }

  public void setGraphicsState(int paramInt, float paramFloat)
  {
  }

  public void drawStrokeColor(Paint paramPaint)
  {
  }

  public void drawTR(int paramInt)
  {
  }

  public void drawStroke(Stroke paramStroke)
  {
  }

  public void drawClip(GraphicsState paramGraphicsState, Shape paramShape, boolean paramBoolean)
  {
  }

  public void writeCustom(int paramInt, Object paramObject)
  {
  }

  public void flagCommand(int paramInt1, int paramInt2)
  {
  }

  public void setValue(int paramInt1, int paramInt2)
  {
    switch (paramInt1)
    {
    case 1:
      this.backgroundColor = new Color(paramInt2);
      break;
    case 2:
      this.textColor = new Color(paramInt2);
      break;
    case 3:
      this.changeLineArtAndText = (paramInt2 > 0);
      break;
    case 4:
      this.colorThresholdToReplace = paramInt2;
    }
  }

  public int getValue(int paramInt)
  {
    return -1;
  }

  public BufferedImage getSingleImagePattern()
  {
    return null;
  }

  public boolean isScalingControlledByUser()
  {
    return false;
  }

  public boolean avoidDownSamplingImage()
  {
    return false;
  }

  public boolean getBooleanValue(int paramInt)
  {
    return false;
  }

  public float getScaling()
  {
    return this.scaling;
  }

  public void saveAdvanceWidth(String paramString1, String paramString2, int paramInt)
  {
  }

  public static int isRectangle(Shape paramShape)
  {
    int i = 0;
    PathIterator localPathIterator = paramShape.getPathIterator(null);
    while ((!localPathIterator.isDone()) && (i < 8))
    {
      localPathIterator.next();
      i++;
    }
    return i;
  }

  public void setMode(DynamicVectorRenderer.Mode paramMode)
  {
    this.mode = paramMode;
  }

  public DynamicVectorRenderer.Mode getMode()
  {
    return this.mode;
  }

  public Rectangle[] getAreas()
  {
    return this.imageAndShapeAreas.get();
  }

  public Object getObjectValue(int paramInt)
  {
    return null;
  }

  public void drawShape(Path paramPath, GraphicsState paramGraphicsState, int paramInt)
  {
    System.out.println("Should never be called");
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.render.BaseDisplay
 * JD-Core Version:    0.6.2
 */