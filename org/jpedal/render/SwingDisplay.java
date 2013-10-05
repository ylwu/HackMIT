package org.jpedal.render;

import java.awt.AlphaComposite;
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
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import javax.swing.JOptionPane;
import org.jpedal.color.ColorSpaces;
import org.jpedal.color.PdfColor;
import org.jpedal.color.PdfPaint;
import org.jpedal.exception.PdfException;
import org.jpedal.external.ColorHandler;
import org.jpedal.external.JPedalCustomDrawObject;
import org.jpedal.external.JPedalHelper;
import org.jpedal.fonts.PdfFont;
import org.jpedal.fonts.glyph.GlyphFactory;
import org.jpedal.fonts.glyph.PdfGlyph;
import org.jpedal.fonts.glyph.PdfGlyphs;
import org.jpedal.fonts.glyph.PdfJavaGlyphs;
import org.jpedal.io.ColorSpaceConvertor;
import org.jpedal.io.JAIHelper;
import org.jpedal.io.ObjectStore;
import org.jpedal.objects.GraphicsState;
import org.jpedal.parser.DecoderOptions;
import org.jpedal.render.output.io.DefaultIO;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.Matrix;
import org.jpedal.utils.Messages;
import org.jpedal.utils.repositories.Vector_Double;
import org.jpedal.utils.repositories.Vector_Float;
import org.jpedal.utils.repositories.Vector_Int;
import org.jpedal.utils.repositories.Vector_Object;
import org.jpedal.utils.repositories.Vector_Rectangle;
import org.jpedal.utils.repositories.Vector_Shape;

public class SwingDisplay extends BaseDisplay
  implements DynamicVectorRenderer
{
  boolean ignoreHighlight = false;
  DynamicVectorRenderer htmlDVR = null;
  private boolean noRepaint = false;
  private int lastItemPainted = -1;
  private boolean optimsePainting = false;
  private boolean needsHorizontalInvert = false;
  private boolean needsVerticalInvert = false;
  private int pageX1 = 9999;
  private int pageX2 = -9999;
  private int pageY1 = -9999;
  private int pageY2 = 9999;
  private boolean highlightsNeedToBeGenerated = false;
  private BufferedImage singleImage = null;
  private int imageCount = 0;
  private static final int defaultSize = 5000;
  private static RenderingHints hints = null;
  private final Map cachedWidths = new HashMap(10);
  private final Map cachedHeights = new HashMap(10);
  private Map fonts = new HashMap(50);
  private Map fontsUsed = new HashMap(50);
  protected GlyphFactory factory = null;
  private PdfGlyphs glyphs;
  private Map imageID = new HashMap(10);
  private final Map imageIDtoName = new HashMap(10);
  private Map storedImageValues = new HashMap(10);
  private int[] textHighlightsX;
  private int[] textHighlightsWidth;
  private int[] textHighlightsHeight;
  boolean stopG2setting;
  private float[] x_coord;
  private float[] y_coord;
  private Map largeImages = new WeakHashMap(10);
  private Vector_Object text_color;
  private Vector_Object stroke_color;
  private Vector_Object fill_color;
  private Vector_Object stroke;
  private Vector_Object pageObjects;
  private Vector_Int shapeType;
  private Vector_Rectangle fontBounds;
  private Vector_Double af1;
  private Vector_Double af2;
  private Vector_Double af3;
  private Vector_Double af4;
  private Vector_Int imageOptions;
  private Vector_Int TRvalues;
  private Vector_Int fs;
  private Vector_Int lw;
  private Vector_Shape clips;
  private Vector_Int objectType;
  private Vector_Object javaObjects;
  private Vector_Int textFillType;
  private Vector_Float opacity;
  private int lastFillTextCol;
  private int lastFillCol;
  private int lastStrokeCol;
  private Stroke lastStroke = null;
  private double[] lastAf = new double[4];
  private int lastTR = 2;
  private int lastFS = -1;
  private int lastLW = -1;
  private boolean resetTextColors = true;
  private boolean fillSet = false;
  private boolean strokeSet = false;
  private boolean needsHighlights = true;
  private int paintThreadCount = 0;
  private int paintThreadID = 0;
  private boolean[] drawnHighlights;
  private boolean hasOCR = false;
  protected int type = 1;
  private double minX = -1.0D;
  private double minY = -1.0D;
  private double maxX = -1.0D;
  private double maxY = -1.0D;
  private boolean renderFailed;
  private Container frame = null;
  private static boolean userAlerted = false;

  public SwingDisplay()
  {
    this.currentItem = 0;
  }

  void setupArrays(int paramInt)
  {
    this.x_coord = new float[paramInt];
    this.y_coord = new float[paramInt];
    this.text_color = new Vector_Object(paramInt);
    this.textFillType = new Vector_Int(paramInt);
    this.stroke_color = new Vector_Object(paramInt);
    this.fill_color = new Vector_Object(paramInt);
    this.stroke = new Vector_Object(paramInt);
    this.pageObjects = new Vector_Object(paramInt);
    this.javaObjects = new Vector_Object(paramInt);
    this.shapeType = new Vector_Int(paramInt);
    this.areas = new Vector_Rectangle(paramInt);
    this.af1 = new Vector_Double(paramInt);
    this.af2 = new Vector_Double(paramInt);
    this.af3 = new Vector_Double(paramInt);
    this.af4 = new Vector_Double(paramInt);
    this.fontBounds = new Vector_Rectangle(paramInt);
    this.clips = new Vector_Shape(paramInt);
    this.objectType = new Vector_Int(paramInt);
    this.opacity = new Vector_Float(paramInt);
    this.currentItem = 0;
  }

  public SwingDisplay(int paramInt1, boolean paramBoolean, int paramInt2, ObjectStore paramObjectStore)
  {
    this.rawPageNumber = paramInt1;
    this.objectStoreRef = paramObjectStore;
    this.addBackground = paramBoolean;
    setupArrays(paramInt2);
  }

  public SwingDisplay(int paramInt, ObjectStore paramObjectStore, boolean paramBoolean)
  {
    this.rawPageNumber = paramInt;
    this.objectStoreRef = paramObjectStore;
    this.isPrinting = paramBoolean;
    setupArrays(5000);
  }

  public void setOptimsePainting(boolean paramBoolean)
  {
    this.optimsePainting = paramBoolean;
    this.lastItemPainted = -1;
  }

  private void renderHighlight(Rectangle paramRectangle, Graphics2D paramGraphics2D)
  {
    if ((paramRectangle != null) && (!this.ignoreHighlight))
    {
      Shape localShape = paramGraphics2D.getClip();
      paramGraphics2D.setClip(null);
      Composite localComposite = paramGraphics2D.getComposite();
      Paint localPaint = paramGraphics2D.getPaint();
      paramGraphics2D.setComposite(AlphaComposite.getInstance(3, DecoderOptions.highlightComposite));
      if (invertHighlight)
      {
        paramGraphics2D.setColor(Color.WHITE);
        paramGraphics2D.setXORMode(Color.BLACK);
      }
      else
      {
        paramGraphics2D.setPaint(DecoderOptions.highlightColor);
      }
      paramGraphics2D.fill(paramRectangle);
      paramGraphics2D.setComposite(localComposite);
      paramGraphics2D.setPaint(localPaint);
      this.needsHighlights = false;
      paramGraphics2D.setClip(localShape);
    }
  }

  public synchronized void writeCustom(int paramInt, Object paramObject)
  {
    switch (paramInt)
    {
    case 26:
      this.htmlDVR = ((DynamicVectorRenderer)paramObject);
    }
  }

  public Object getObjectValue(int paramInt)
  {
    switch (paramInt)
    {
    case 26:
      return this.htmlDVR;
    }
    return null;
  }

  public void stopG2HintSetting(boolean paramBoolean)
  {
    this.stopG2setting = paramBoolean;
  }

  public void flush()
  {
    this.singleImage = null;
    this.imageCount = 0;
    this.lastFS = -1;
    if (this.shapeType != null)
    {
      this.shapeType.clear();
      this.pageObjects.clear();
      this.objectType.clear();
      this.javaObjects.clear();
      this.areas.clear();
      this.clips.clear();
      this.x_coord = new float[5000];
      this.y_coord = new float[5000];
      this.textFillType.clear();
      this.text_color.clear();
      this.fill_color.clear();
      this.stroke_color.clear();
      this.stroke.clear();
      if (this.TRvalues != null)
        this.TRvalues = null;
      if (this.imageOptions != null)
        this.imageOptions = null;
      if (this.fs != null)
        this.fs = null;
      if (this.lw != null)
        this.lw = null;
      this.af1.clear();
      this.af2.clear();
      this.af3.clear();
      this.af4.clear();
      this.fontBounds.clear();
      if (this.opacity != null)
        this.opacity.clear();
      if (this.isPrinting)
        this.largeImages.clear();
      this.endItem = -1;
    }
    this.lastFillTextCol = 0;
    this.lastFillCol = 0;
    this.lastStrokeCol = 0;
    this.lastClip = null;
    this.hasClips = false;
    this.lastStroke = null;
    this.lastAf = new double[4];
    this.currentItem = 0;
    this.fillSet = false;
    this.strokeSet = false;
    this.fonts.clear();
    this.fontsUsed.clear();
    this.imageID.clear();
    this.pageX1 = 9999;
    this.pageX2 = -9999;
    this.pageY1 = -9999;
    this.pageY2 = 9999;
    this.lastScaling = 0.0F;
  }

  public void dispose()
  {
    this.singleImage = null;
    this.shapeType = null;
    this.pageObjects = null;
    this.objectType = null;
    this.areas = null;
    this.clips = null;
    this.x_coord = null;
    this.y_coord = null;
    this.textFillType = null;
    this.text_color = null;
    this.fill_color = null;
    this.stroke_color = null;
    this.stroke = null;
    this.TRvalues = null;
    this.imageOptions = null;
    this.fs = null;
    this.lw = null;
    this.af1 = null;
    this.af2 = null;
    this.af3 = null;
    this.af4 = null;
    this.fontBounds = null;
    this.opacity = null;
    this.largeImages = null;
    this.lastClip = null;
    this.lastStroke = null;
    this.lastAf = null;
    this.fonts = null;
    this.fontsUsed = null;
    this.imageID = null;
    this.storedImageValues = null;
  }

  public Rectangle paint(Rectangle[] paramArrayOfRectangle, AffineTransform paramAffineTransform, Rectangle paramRectangle)
  {
    Vector_Rectangle localVector_Rectangle = null;
    HashMap localHashMap = null;
    if (this.hasOCR)
    {
      localVector_Rectangle = new Vector_Rectangle(4000);
      localHashMap = new HashMap(10);
    }
    int i = ++this.paintThreadID;
    this.paintThreadCount += 1;
    if (paramArrayOfRectangle != null)
    {
      this.drawnHighlights = new boolean[paramArrayOfRectangle.length];
      for (int j = 0; j != this.drawnHighlights.length; j++)
        this.drawnHighlights[j] = false;
    }
    if (this.paintThreadCount > 1)
    {
      try
      {
        Thread.sleep(50L);
      }
      catch (InterruptedException localInterruptedException)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog(new StringBuilder().append("Exception: ").append(localInterruptedException.getMessage()).toString());
      }
      if (i != this.paintThreadID)
      {
        this.paintThreadCount -= 1;
        return null;
      }
    }
    float f1 = 0.0F;
    float f2 = 0.0F;
    float f3 = 0.0F;
    float f4 = 0.0F;
    Rectangle localRectangle1 = null;
    int[] arrayOfInt1 = this.objectType.get();
    int[] arrayOfInt2 = this.textFillType.get();
    int k = this.currentItem;
    Area[] arrayOfArea = this.clips.get();
    double[] arrayOfDouble1 = this.af1.get();
    int[] arrayOfInt3 = null;
    if (this.fs != null)
      arrayOfInt3 = this.fs.get();
    Rectangle[] arrayOfRectangle1 = this.fontBounds.get();
    int[] arrayOfInt4 = null;
    if (this.lw != null)
      arrayOfInt4 = this.lw.get();
    double[] arrayOfDouble2 = this.af2.get();
    double[] arrayOfDouble3 = this.af3.get();
    double[] arrayOfDouble4 = this.af4.get();
    Object[] arrayOfObject1 = this.text_color.get();
    Object[] arrayOfObject2 = this.fill_color.get();
    Object[] arrayOfObject3 = this.stroke_color.get();
    Object[] arrayOfObject4 = this.pageObjects.get();
    Object[] arrayOfObject5 = this.javaObjects.get();
    Object[] arrayOfObject6 = this.stroke.get();
    int[] arrayOfInt5 = this.shapeType.get();
    float[] arrayOfFloat = null;
    if (this.opacity != null)
      arrayOfFloat = this.opacity.get();
    int[] arrayOfInt6 = null;
    if (this.TRvalues != null)
      arrayOfInt6 = this.TRvalues.get();
    Rectangle[] arrayOfRectangle2 = null;
    if (this.areas != null)
      arrayOfRectangle2 = this.areas.get();
    int[] arrayOfInt7 = null;
    if (this.imageOptions != null)
      arrayOfInt7 = this.imageOptions.get();
    Shape localShape1 = this.g2.getClip();
    if (localShape1 != null)
      localRectangle1 = localShape1.getBounds();
    int m = 0;
    Shape localShape2 = this.g2.getClip();
    Area localArea = null;
    int n = 0;
    if (this.noRepaint)
      this.noRepaint = false;
    else if (this.lastItemPainted == -1)
      paintBackground(localRectangle1);
    AffineTransform localAffineTransform1 = this.g2.getTransform();
    if (paramAffineTransform != null)
    {
      this.g2.transform(paramAffineTransform);
      localShape2 = this.g2.getClip();
    }
    this.minX = -1.0D;
    this.minY = -1.0D;
    this.maxX = -1.0D;
    this.maxY = -1.0D;
    int i3 = 2;
    int i4 = 0;
    float f5 = 1.0F;
    float f6 = 1.0F;
    int i5 = 0;
    int i6 = 0;
    int i7 = 0;
    int i8 = -1;
    int i9 = 0;
    int i10 = -1;
    int i11 = 0;
    int i12 = 0;
    int i13 = 0;
    int i14 = 0;
    int i15 = 0;
    int i16 = 0;
    int i17 = 0;
    PdfPaint localPdfPaint1 = null;
    PdfPaint localPdfPaint2 = null;
    PdfPaint localPdfPaint3 = null;
    PdfPaint localPdfPaint4 = null;
    Stroke localStroke = null;
    if (this.colorsLocked)
    {
      localPdfPaint4 = this.strokeCol;
      localPdfPaint3 = this.fillCol;
    }
    if ((this.highlightsNeedToBeGenerated) && (arrayOfRectangle2 != null) && (paramArrayOfRectangle != null))
      generateHighlights(this.g2, k, arrayOfInt1, arrayOfObject4, f1, f2, f3, f4, arrayOfInt3, arrayOfRectangle1);
    Object localObject3;
    for (int i19 = 0; i19 < k; i19++)
    {
      this.itemToRender = i19;
      int i1 = arrayOfInt1[i19];
      if (i1 != 27)
      {
        localObject3 = null;
        if (i != this.paintThreadID)
        {
          this.paintThreadCount -= 1;
          return null;
        }
        if (i1 > 0)
        {
          float f7 = this.x_coord[i19];
          float f8 = this.y_coord[i19];
          Object localObject1 = arrayOfObject4[i19];
          int i18;
          if (i1 == 29)
          {
            i1 = 3;
            i18 = ((Integer)localObject1).intValue();
            localObject1 = arrayOfObject4[i18];
          }
          else
          {
            i18 = -1;
          }
          if (localObject3 == null)
            localObject3 = getObjectArea(arrayOfDouble1, arrayOfInt3, arrayOfDouble2, arrayOfDouble3, arrayOfDouble4, arrayOfObject4, arrayOfRectangle2, i1, f7, f8, i8, i10, i19);
          int i20 = 0;
          if ((localObject3 != null) && (i1 < 7) && (paramRectangle != null) && (!paramRectangle.intersects((Rectangle)localObject3)) && (((Rectangle)localObject3).width > 0))
            i20 = 1;
          if ((i20 != 0) || ((this.lastItemPainted != -1) && (i19 < this.lastItemPainted)))
          {
            switch (i1)
            {
            case 2:
              i7++;
              break;
            case 3:
              i5++;
              break;
            case 29:
              i5++;
              break;
            case 7:
              i6++;
              break;
            case 21:
              i8++;
              break;
            case 22:
              i9++;
              break;
            case 10:
              i11++;
              break;
            case 11:
              i13++;
              break;
            case 12:
              i14++;
              break;
            case 14:
              i12++;
              break;
            case 15:
              i15++;
            case 4:
            case 5:
            case 6:
            case 8:
            case 9:
            case 13:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            }
          }
          else
          {
            if ((m == 0) && (!this.stopG2setting))
            {
              if (userHints != null)
                this.g2.setRenderingHints(userHints);
              else
                this.g2.setRenderingHints(hints);
              m = 1;
            }
            if (i3 == 4)
              this.needsHighlights = true;
            Rectangle localRectangle2 = null;
            AffineTransform localAffineTransform2;
            Object localObject5;
            switch (i1)
            {
            case 2:
              if (n != 0)
              {
                RenderUtils.renderClip(localArea, localRectangle1, localShape2, this.g2);
                n = 0;
              }
              Shape localShape3 = null;
              if ((this.endItem != -1) && (this.endItem < i19))
              {
                localShape3 = this.g2.getClip();
                this.g2.setClip(localShape2);
              }
              renderShape(localShape2, arrayOfInt5[i7], localPdfPaint4, localPdfPaint3, localStroke, (Shape)localObject1, f6, f5);
              if ((this.endItem != -1) && (this.endItem < i19))
                this.g2.setClip(localShape3);
              i7++;
              break;
            case 1:
              if (n != 0)
              {
                RenderUtils.renderClip(localArea, localRectangle1, localShape2, this.g2);
                n = 0;
              }
              if (!invertHighlight)
                localRectangle2 = setHighlightForGlyph((Rectangle)localObject3, paramArrayOfRectangle);
              if ((this.hasOCR) && (localRectangle2 != null))
              {
                localObject4 = new StringBuilder().append(localRectangle2.x).append(" ").append(localRectangle2.y).toString();
                if (localHashMap.get(localObject4) == null)
                {
                  localHashMap.put(localObject4, "x");
                  localVector_Rectangle.addElement(localRectangle2);
                }
              }
              Object localObject4 = this.g2.getTransform();
              renderHighlight(localRectangle2, this.g2);
              this.g2.transform(new AffineTransform(arrayOfDouble1[i10], arrayOfDouble2[i10], -arrayOfDouble3[i10], -arrayOfDouble4[i10], f7, f8));
              renderText(f7, f8, i3, (Area)localObject1, localRectangle2, localPdfPaint1, localPdfPaint2, f6, f5);
              this.g2.setTransform((AffineTransform)localObject4);
              break;
            case 4:
              if (n != 0)
              {
                RenderUtils.renderClip(localArea, localRectangle1, localShape2, this.g2);
                n = 0;
              }
              if (i10 != -1)
              {
                localAffineTransform2 = new AffineTransform(arrayOfDouble1[i10], arrayOfDouble2[i10], arrayOfDouble3[i10], arrayOfDouble4[i10], f7, f8);
                if (!invertHighlight)
                  localRectangle2 = setHighlightForGlyph((Rectangle)localObject3, paramArrayOfRectangle);
                if ((this.hasOCR) && (localRectangle2 != null))
                {
                  localObject5 = new StringBuilder().append(localRectangle2.x).append(" ").append(localRectangle2.y).toString();
                  if (localHashMap.get(localObject5) == null)
                  {
                    localHashMap.put(localObject5, "x");
                    localVector_Rectangle.addElement(localRectangle2);
                  }
                }
                renderHighlight(localRectangle2, this.g2);
                renderEmbeddedText(i3, localObject1, 4, localAffineTransform2, localRectangle2, localPdfPaint1, localPdfPaint2, f6, f5, i4);
              }
              break;
            case 5:
              if (n != 0)
              {
                RenderUtils.renderClip(localArea, localRectangle1, localShape2, this.g2);
                n = 0;
              }
              localAffineTransform2 = new AffineTransform(arrayOfDouble1[i10], arrayOfDouble2[i10], arrayOfDouble3[i10], arrayOfDouble4[i10], f7, f8);
              if (!invertHighlight)
                localRectangle2 = setHighlightForGlyph((Rectangle)localObject3, paramArrayOfRectangle);
              if ((this.hasOCR) && (localRectangle2 != null))
              {
                localObject5 = new StringBuilder().append(localRectangle2.x).append(" ").append(localRectangle2.y).toString();
                if (localHashMap.get(localObject5) == null)
                {
                  localHashMap.put(localObject5, "x");
                  localVector_Rectangle.addElement(localRectangle2);
                }
              }
              renderHighlight(localRectangle2, this.g2);
              renderEmbeddedText(i3, localObject1, 5, localAffineTransform2, localRectangle2, localPdfPaint1, localPdfPaint2, f6, f5, i4);
              break;
            case 6:
              if (n != 0)
              {
                RenderUtils.renderClip(localArea, localRectangle1, localShape2, this.g2);
                n = 0;
              }
              localAffineTransform2 = new AffineTransform(arrayOfDouble1[i10], arrayOfDouble2[i10], arrayOfDouble3[i10], arrayOfDouble4[i10], f7, f8);
              if (!invertHighlight)
                localRectangle2 = setHighlightForGlyph((Rectangle)localObject3, paramArrayOfRectangle);
              if ((this.hasOCR) && (localRectangle2 != null))
              {
                localObject5 = new StringBuilder().append(localRectangle2.x).append(" ").append(localRectangle2.y).toString();
                if (localHashMap.get(localObject5) == null)
                {
                  localHashMap.put(localObject5, "x");
                  localVector_Rectangle.addElement(localRectangle2);
                }
              }
              renderHighlight(localRectangle2, this.g2);
              renderEmbeddedText(i3, localObject1, 6, localAffineTransform2, localRectangle2, localPdfPaint1, localPdfPaint2, f6, f5, i4);
              break;
            case 3:
              if (n != 0)
              {
                RenderUtils.renderClip(localArea, localRectangle1, localShape2, this.g2);
                n = 0;
              }
              renderImage(arrayOfDouble1, arrayOfDouble2, arrayOfDouble3, arrayOfDouble4, arrayOfObject4, arrayOfInt7, localObject1, f5, f7, f8, i5, i10, i18, i19);
              i5++;
              break;
            case 7:
              localArea = arrayOfArea[i6];
              n = 1;
              i6++;
              break;
            case 9:
              i10++;
              break;
            case 21:
              i8++;
              break;
            case 22:
              i4 = arrayOfInt4[i9];
              i9++;
              break;
            case 10:
              int i2 = arrayOfInt2[i11];
              if (i2 == 1)
                localPdfPaint1 = (PdfPaint)arrayOfObject1[i11];
              else
                localPdfPaint2 = (PdfPaint)arrayOfObject1[i11];
              i11++;
              break;
            case 11:
              if (!this.colorsLocked)
                localPdfPaint3 = (PdfPaint)arrayOfObject2[i13];
              i13++;
              break;
            case 12:
              if (!this.colorsLocked)
              {
                localPdfPaint4 = (PdfPaint)arrayOfObject3[i14];
                if (localPdfPaint4 != null)
                  localPdfPaint4.setScaling(this.cropX, this.cropH, this.scaling, 0.0F, 0.0F);
              }
              i14++;
              break;
            case 14:
              localStroke = (Stroke)arrayOfObject6[i12];
              i12++;
              break;
            case 15:
              i3 = arrayOfInt6[i15];
              i15++;
              break;
            case 17:
              f6 = arrayOfFloat[i16];
              i16++;
              break;
            case 18:
              f5 = arrayOfFloat[i16];
              i16++;
              break;
            case 16:
              localObject5 = this.g2.getClip();
              this.g2.setClip(localShape2);
              AffineTransform localAffineTransform3 = this.g2.getTransform();
              String str = (String)localObject1;
              double[] arrayOfDouble5 = new double[6];
              this.g2.getTransform().getMatrix(arrayOfDouble5);
              if (super.getMode() != DynamicVectorRenderer.Mode.XFA)
              {
                if (arrayOfDouble5[2] != 0.0D)
                  arrayOfDouble5[2] = (-arrayOfDouble5[2]);
                if (arrayOfDouble5[3] != 0.0D)
                  arrayOfDouble5[3] = (-arrayOfDouble5[3]);
              }
              this.g2.setTransform(new AffineTransform(arrayOfDouble5));
              Font localFont = (Font)arrayOfObject5[i17];
              this.g2.setFont(localFont);
              if ((i3 & 0x2) == 2)
              {
                if (localPdfPaint2 != null)
                  localPdfPaint2.setScaling(this.cropX, this.cropH, this.scaling, 0.0F, 0.0F);
                if (this.customColorHandler != null)
                  this.customColorHandler.setPaint(this.g2, localPdfPaint2, this.rawPageNumber, this.isPrinting);
                else if (DecoderOptions.Helper != null)
                  DecoderOptions.Helper.setPaint(this.g2, localPdfPaint2, this.rawPageNumber, this.isPrinting);
                else
                  this.g2.setPaint(localPdfPaint2);
              }
              if ((i3 & 0x1) == 1)
              {
                if (localPdfPaint1 != null)
                  localPdfPaint1.setScaling(this.cropX, this.cropH, this.scaling, 0.0F, 0.0F);
                if (this.customColorHandler != null)
                  this.customColorHandler.setPaint(this.g2, localPdfPaint2, this.rawPageNumber, this.isPrinting);
                else if (DecoderOptions.Helper != null)
                  DecoderOptions.Helper.setPaint(this.g2, localPdfPaint2, this.rawPageNumber, this.isPrinting);
                else
                  this.g2.setPaint(localPdfPaint2);
              }
              this.g2.drawString(str, f7, f8);
              this.g2.setTransform(localAffineTransform3);
              this.g2.setClip((Shape)localObject5);
              i17++;
              break;
            case 25:
              renderXForm((DynamicVectorRenderer)localObject1, f5);
              break;
            case 23:
              Shape localShape4 = this.g2.getClip();
              this.g2.setClip(localShape2);
              AffineTransform localAffineTransform4 = this.g2.getTransform();
              JPedalCustomDrawObject localJPedalCustomDrawObject = (JPedalCustomDrawObject)localObject1;
              if (this.isPrinting)
                localJPedalCustomDrawObject.print(this.g2, this.rawPageNumber);
              else
                localJPedalCustomDrawObject.paint(this.g2);
              this.g2.setTransform(localAffineTransform4);
              this.g2.setClip(localShape4);
            case 8:
            case 13:
            case 19:
            case 20:
            case 24:
            }
          }
        }
      }
    }
    this.itemToRender = -1;
    if ((this.needsHighlights) && (paramArrayOfRectangle != null))
      for (i19 = 0; i19 != paramArrayOfRectangle.length; i19++)
      {
        this.ignoreHighlight = false;
        renderHighlight(paramArrayOfRectangle[i19], this.g2);
      }
    Object localObject2;
    if (localVector_Rectangle != null)
    {
      localObject2 = localVector_Rectangle.get();
      Composite localComposite = this.g2.getComposite();
      localObject3 = this.g2.getPaint();
      for (int i21 = 0; i21 != localObject2.length; i21++)
      {
        if (localObject2[i21] != null)
        {
          this.g2.setComposite(AlphaComposite.getInstance(3, DecoderOptions.highlightComposite));
          this.g2.setPaint(DecoderOptions.highlightColor);
          this.g2.fill(localObject2[i21]);
        }
        this.g2.setComposite(localComposite);
        this.g2.setPaint((Paint)localObject3);
      }
    }
    this.g2.setClip(localShape2);
    this.g2.setTransform(localAffineTransform1);
    if ((this.frame != null) && (this.renderFailed) && (!userAlerted))
    {
      userAlerted = true;
      if (DecoderOptions.showErrorMessages)
      {
        localObject2 = new StringBuilder().append(Messages.getMessage("PdfViewer.ImageDisplayError")).append(Messages.getMessage("PdfViewer.ImageDisplayError1")).append(Messages.getMessage("PdfViewer.ImageDisplayError2")).append(Messages.getMessage("PdfViewer.ImageDisplayError3")).append(Messages.getMessage("PdfViewer.ImageDisplayError4")).append(Messages.getMessage("PdfViewer.ImageDisplayError5")).append(Messages.getMessage("PdfViewer.ImageDisplayError6")).append(Messages.getMessage("PdfViewer.ImageDisplayError7")).toString();
        JOptionPane.showMessageDialog(this.frame, localObject2);
        this.frame.invalidate();
        this.frame.repaint();
      }
    }
    this.paintThreadCount -= 1;
    if (this.optimsePainting)
      this.lastItemPainted = k;
    else
      this.lastItemPainted = -1;
    this.lastScaling = this.scaling;
    if (this.minX == -1.0D)
      return null;
    return new Rectangle((int)this.minX, (int)this.minY, (int)(this.maxX - this.minX), (int)(this.maxY - this.minY));
  }

  private static Rectangle getObjectArea(double[] paramArrayOfDouble1, int[] paramArrayOfInt, double[] paramArrayOfDouble2, double[] paramArrayOfDouble3, double[] paramArrayOfDouble4, Object[] paramArrayOfObject, Rectangle[] paramArrayOfRectangle, int paramInt1, float paramFloat1, float paramFloat2, int paramInt2, int paramInt3, int paramInt4)
  {
    Rectangle localRectangle = null;
    if ((paramArrayOfDouble1 != null) && (paramInt1 == 3))
    {
      if (paramArrayOfRectangle != null)
        localRectangle = paramArrayOfRectangle[paramInt4];
    }
    else if ((paramArrayOfDouble1 != null) && (paramInt1 == 2))
    {
      localRectangle = ((Shape)paramArrayOfObject[paramInt4]).getBounds();
    }
    else if ((paramInt1 == 1) && (paramInt3 > -1))
    {
      localRectangle = RenderUtils.getAreaForGlyph(new float[][] { { (float)paramArrayOfDouble1[paramInt3], (float)paramArrayOfDouble2[paramInt3], 0.0F }, { (float)paramArrayOfDouble3[paramInt3], (float)paramArrayOfDouble4[paramInt3], 0.0F }, { paramFloat1, paramFloat2, 1.0F } });
    }
    else if ((paramInt2 != -1) && (paramArrayOfDouble1 != null))
    {
      int i = paramArrayOfInt[paramInt2];
      if (i < 0)
        localRectangle = new Rectangle((int)paramFloat1 + i, (int)paramFloat2, -i, -i);
      else
        localRectangle = new Rectangle((int)paramFloat1, (int)paramFloat2, i, i);
    }
    return localRectangle;
  }

  private void renderImage(double[] paramArrayOfDouble1, double[] paramArrayOfDouble2, double[] paramArrayOfDouble3, double[] paramArrayOfDouble4, Object[] paramArrayOfObject, int[] paramArrayOfInt, Object paramObject, float paramFloat1, float paramFloat2, float paramFloat3, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = 0;
    if (paramArrayOfInt != null)
      i = paramArrayOfInt[paramInt1];
    int j = 1;
    int k = 0;
    int m = 0;
    int n = 1;
    String str = new StringBuilder().append(Integer.toString(this.rawPageNumber)).append(Integer.toString(paramInt1)).toString();
    if ((this.useHiResImageForDisplay) && (!this.isType3Font) && (this.objectStoreRef.isRawImageDataSaved(str)))
    {
      float f = this.scaling;
      if ((this.useHiResImageForDisplay) && (this.scaling < 1.0F))
        f = 1.0F;
      int i1 = ((Integer)this.objectStoreRef.getRawImageDataParameter(str, ObjectStore.IMAGE_pX)).intValue();
      int i2 = (int)(i1 * f);
      int i3 = ((Integer)this.objectStoreRef.getRawImageDataParameter(str, ObjectStore.IMAGE_pY)).intValue();
      m = (int)(i3 * f);
      k = ((Integer)this.objectStoreRef.getRawImageDataParameter(str, ObjectStore.IMAGE_WIDTH)).intValue();
      int i4 = ((Integer)this.objectStoreRef.getRawImageDataParameter(str, ObjectStore.IMAGE_HEIGHT)).intValue();
      byte[] arrayOfByte = (byte[])this.objectStoreRef.getRawImageDataParameter(str, ObjectStore.IMAGE_MASKCOL);
      int i5 = ((Integer)this.objectStoreRef.getRawImageDataParameter(str, ObjectStore.IMAGE_COLORSPACE)).intValue();
      BufferedImage localBufferedImage2 = null;
      if (i2 > 0)
      {
        int i6 = k;
        int i7 = i4;
        int i8 = m << 2;
        int i9 = i2 << 2;
        while ((i6 > i9) && (i7 > i8))
        {
          j <<= 1;
          i6 >>= 1;
          i7 >>= 1;
        }
        int i10 = k / i2;
        if (i10 < 1)
          i10 = 1;
        int i11 = i4 / m;
        if (i11 < 1)
          i11 = 1;
        j = i10;
        if (j > i11)
          j = i11;
        int i12 = k;
        int i13 = i4;
        int i14 = m << 2;
        int i15 = i2 << 2;
        while ((i12 > i15) && (i13 > i14))
        {
          n <<= 1;
          i12 >>= 1;
          i13 >>= 1;
        }
        int i16 = k / i1;
        if (i16 < 1)
          i16 = 1;
        int i17 = i4 / i3;
        if (i17 < 1)
          i17 = 1;
        n = i16;
        if (n > i17)
          n = i17;
        if (((this.scaling > 1.0F) || (this.lastScaling > 1.0F)) && (j >= 1) && (this.lastScaling != this.scaling))
        {
          i6 = k / j;
          i7 = i4 / j;
          localBufferedImage2 = resampleImageData(j, k, i4, arrayOfByte, i6, i7, str, i5);
        }
      }
      if (localBufferedImage2 != null)
      {
        if (this.singleImage != null)
          this.singleImage = localBufferedImage2;
        paramArrayOfObject[paramInt4] = localBufferedImage2;
        paramObject = localBufferedImage2;
      }
    }
    if (this.useHiResImageForDisplay)
    {
      double d1 = 1.0D;
      if ((j >= 1) && (this.scaling > 1.0F) && (k > 0))
        d1 = j / n;
      AffineTransform localAffineTransform2 = new AffineTransform(paramArrayOfDouble1[paramInt2] * d1, paramArrayOfDouble2[paramInt2] * d1, paramArrayOfDouble3[paramInt2] * d1, paramArrayOfDouble4[paramInt2] * d1, paramFloat2, paramFloat3);
      BufferedImage localBufferedImage1 = null;
      if (paramObject != null)
        localBufferedImage1 = (BufferedImage)paramObject;
      if (paramObject == null)
        localBufferedImage1 = reloadCachedImage(paramInt3, paramInt4, localBufferedImage1);
      if (localBufferedImage1 != null)
        renderImage(localAffineTransform2, localBufferedImage1, paramFloat1, null, paramFloat2, paramFloat3, i);
    }
    else
    {
      AffineTransform localAffineTransform1 = this.g2.getTransform();
      this.extraRot = false;
      if (m > 0)
      {
        double[] arrayOfDouble = new double[6];
        this.g2.getTransform().getMatrix(arrayOfDouble);
        double d2 = m / ((BufferedImage)paramObject).getHeight();
        arrayOfDouble[0] = d2;
        arrayOfDouble[1] = 0.0D;
        arrayOfDouble[2] = 0.0D;
        arrayOfDouble[3] = (-d2);
        this.g2.scale(1.0F / this.scaling, 1.0F / this.scaling);
        this.g2.setTransform(new AffineTransform(arrayOfDouble));
      }
      else
      {
        this.extraRot = true;
      }
      renderImage(null, (BufferedImage)paramObject, paramFloat1, null, paramFloat2, paramFloat3, i);
      this.g2.setTransform(localAffineTransform1);
    }
  }

  private BufferedImage resampleImageData(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte, int paramInt4, int paramInt5, String paramString, int paramInt6)
  {
    byte[] arrayOfByte1 = this.objectStoreRef.getRawImageData(paramString);
    byte[] arrayOfByte2 = null;
    if ((paramArrayOfByte != null) && (paramInt6 != 1785221209))
      arrayOfByte2 = paramArrayOfByte;
    int i = paramInt4 * paramInt5;
    if (arrayOfByte2 != null)
      i *= 3;
    byte[] arrayOfByte3 = new byte[i];
    int[] arrayOfInt1 = { 1, 2, 4, 8, 16, 32, 64, 128 };
    int j = paramInt2 + 7 >> 3;
    int k = 0;
    for (int m = 0; m < paramInt5; m++)
      for (int n = 0; n < paramInt4; n++)
      {
        i1 = 0;
        int i2 = 0;
        int i3 = paramInt1;
        i4 = paramInt1;
        int i5 = paramInt2 - n;
        int i6 = paramInt3 - m;
        if (i3 > i5)
          i3 = i5;
        if (i4 > i6)
          i4 = i6;
        for (int i9 = 0; i9 < i4; i9++)
          for (int i10 = 0; i10 < i3; i10++)
          {
            int i7 = (i9 + m * paramInt1) * j + (n * paramInt1 + i10 >> 3);
            int i8;
            if (i7 < arrayOfByte1.length)
              i8 = arrayOfByte1[i7];
            else
              i8 = -1;
            int i11 = i8 & arrayOfInt1[(7 - (n * paramInt1 + i10 & 0x7))];
            if (i11 != 0)
              i1++;
            i2++;
          }
        if (i2 > 0)
        {
          if (arrayOfByte2 == null)
            arrayOfByte3[(n + paramInt4 * m)] = ((byte)(255 * i1 / i2));
          else
            for (i9 = 0; i9 < 3; i9++)
            {
              if (i1 / i2 < 0.5F)
                arrayOfByte3[k] = ((byte)(paramArrayOfByte[i9] & 0xFF));
              else
                arrayOfByte3[k] = -1;
              k++;
            }
        }
        else if (arrayOfByte2 == null)
          arrayOfByte3[(n + paramInt4 * m)] = -1;
        else
          for (i9 = 0; i9 < 3; i9++)
          {
            arrayOfByte3[k] = -1;
            k++;
          }
      }
    int i1 = 10;
    DataBufferByte localDataBufferByte = new DataBufferByte(arrayOfByte3, arrayOfByte3.length);
    int[] arrayOfInt2 = { 0 };
    int i4 = 1;
    if ((paramArrayOfByte == null) && (paramInt2 * paramInt3 * 3 == arrayOfByte1.length))
    {
      i1 = 1;
      arrayOfInt2 = new int[] { 0, 1, 2 };
      i4 = 3;
    }
    BufferedImage localBufferedImage = new BufferedImage(paramInt4, paramInt5, i1);
    WritableRaster localWritableRaster = Raster.createInterleavedRaster(localDataBufferByte, paramInt4, paramInt5, paramInt4 * i4, i4, arrayOfInt2, null);
    localBufferedImage.setData(localWritableRaster);
    return localBufferedImage;
  }

  private BufferedImage reloadCachedImage(int paramInt1, int paramInt2, BufferedImage paramBufferedImage)
  {
    try
    {
      Object localObject;
      if (this.singleImage != null)
        localObject = this.singleImage.getSubimage(0, 0, this.singleImage.getWidth(), this.singleImage.getHeight());
      else if (this.rawKey == null)
        localObject = this.largeImages.get(new StringBuilder().append("HIRES_").append(paramInt2).toString());
      else
        localObject = this.largeImages.get(new StringBuilder().append("HIRES_").append(paramInt2).append('_').append(this.rawKey).toString());
      if (localObject == null)
      {
        int i = paramInt2;
        if (paramInt1 != -1)
          i = paramInt1;
        if (this.rawKey == null)
          localObject = this.objectStoreRef.loadStoredImage(new StringBuilder().append(this.rawPageNumber).append("_HIRES_").append(i).toString());
        else
          localObject = this.objectStoreRef.loadStoredImage(new StringBuilder().append(this.rawPageNumber).append("_HIRES_").append(i).append('_').append(this.rawKey).toString());
        if (localObject == null)
          this.renderFailed = true;
        if (!this.isPrinting)
          if (this.rawKey == null)
            this.largeImages.put(new StringBuilder().append("HIRES_").append(paramInt2).toString(), localObject);
          else
            this.largeImages.put(new StringBuilder().append("HIRES_").append(paramInt2).toString(), new StringBuilder().append(localObject).append("_").append(this.rawKey).toString());
      }
      paramBufferedImage = (BufferedImage)localObject;
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog(new StringBuilder().append("Exception: ").append(localException.getMessage()).toString());
    }
    return paramBufferedImage;
  }

  public void setMessageFrame(Container paramContainer)
  {
    this.frame = paramContainer;
  }

  private Rectangle setHighlightForGlyph(Rectangle paramRectangle, Rectangle[] paramArrayOfRectangle)
  {
    if ((paramArrayOfRectangle == null) || (this.textHighlightsX == null))
      return null;
    this.ignoreHighlight = false;
    for (int i = 0; i != paramArrayOfRectangle.length; i++)
      if ((paramArrayOfRectangle[i] != null) && (paramRectangle != null) && (paramArrayOfRectangle[i].intersects(paramRectangle)))
      {
        Rectangle localRectangle = paramArrayOfRectangle[i].intersection(paramRectangle);
        float f1 = localRectangle.width * localRectangle.height;
        float f2 = paramRectangle.width * paramRectangle.height / 4.0F;
        if (((paramArrayOfRectangle[i].contains(paramRectangle.x, paramRectangle.y)) && (f1 > f2)) || (f1 > paramRectangle.width * paramRectangle.height / 1.667F))
        {
          if (this.drawnHighlights[i] == 0)
          {
            this.ignoreHighlight = false;
            this.drawnHighlights[i] = true;
            return paramArrayOfRectangle[i];
          }
          this.ignoreHighlight = true;
          return paramArrayOfRectangle[i];
        }
      }
    return null;
  }

  public void drawText(float[][] paramArrayOfFloat, String paramString, GraphicsState paramGraphicsState, float paramFloat1, float paramFloat2, Font paramFont)
  {
    if (paramArrayOfFloat != null)
    {
      double[] arrayOfDouble = { paramArrayOfFloat[0][0], paramArrayOfFloat[0][1], paramArrayOfFloat[1][0], paramArrayOfFloat[1][1], paramArrayOfFloat[2][0], paramArrayOfFloat[2][1] };
      if ((this.lastAf[0] != arrayOfDouble[0]) || (this.lastAf[1] != arrayOfDouble[1]) || (this.lastAf[2] != arrayOfDouble[2]) || (this.lastAf[3] != arrayOfDouble[3]))
      {
        drawAffine(arrayOfDouble);
        this.lastAf[0] = arrayOfDouble[0];
        this.lastAf[1] = arrayOfDouble[1];
        this.lastAf[2] = arrayOfDouble[2];
        this.lastAf[3] = arrayOfDouble[3];
      }
    }
    int i = paramGraphicsState.getTextRenderType();
    PdfPaint localPdfPaint;
    if ((i & 0x2) == 2)
    {
      localPdfPaint = paramGraphicsState.getNonstrokeColor();
      if (localPdfPaint.isPattern())
      {
        drawColor(localPdfPaint, 2);
        this.resetTextColors = true;
      }
      else
      {
        j = localPdfPaint.getRGB();
        if ((this.resetTextColors) || (this.lastFillTextCol != j))
        {
          this.lastFillTextCol = j;
          drawColor(localPdfPaint, 2);
        }
      }
    }
    if ((i & 0x1) == 1)
    {
      localPdfPaint = paramGraphicsState.getStrokeColor();
      if (localPdfPaint.isPattern())
      {
        drawColor(localPdfPaint, 1);
        this.resetTextColors = true;
      }
      else
      {
        j = localPdfPaint.getRGB();
        if ((this.resetTextColors) || (this.lastStrokeCol != j))
        {
          this.lastStrokeCol = j;
          drawColor(localPdfPaint, 1);
        }
      }
    }
    this.pageObjects.addElement(paramString);
    this.javaObjects.addElement(paramFont);
    this.objectType.addElement(16);
    int j = paramFont.getSize();
    if (j > 100)
      this.areas.addElement(new Rectangle((int)paramFloat1, (int)paramFloat2, j, j));
    else
      this.areas.addElement(null);
    this.x_coord = RenderUtils.checkSize(this.x_coord, this.currentItem);
    this.y_coord = RenderUtils.checkSize(this.y_coord, this.currentItem);
    this.x_coord[this.currentItem] = paramFloat1;
    this.y_coord[this.currentItem] = paramFloat2;
    this.currentItem += 1;
    this.resetTextColors = false;
  }

  public Rectangle getCombinedAreas(Rectangle paramRectangle)
  {
    Rectangle localRectangle1 = null;
    if (this.areas != null)
    {
      Rectangle localRectangle2 = paramRectangle.getBounds();
      int i = localRectangle2.x;
      int j = localRectangle2.y;
      int k = i + localRectangle2.width;
      int m = j + localRectangle2.height;
      int n = 0;
      Rectangle[] arrayOfRectangle1 = this.areas.get();
      for (Rectangle localRectangle3 : arrayOfRectangle1)
        if ((localRectangle3 != null) && (paramRectangle.contains(localRectangle3)))
        {
          n = 1;
          int i3 = localRectangle3.x;
          if (k > i3)
            k = i3;
          i3 = localRectangle3.x + localRectangle3.width;
          if (i < i3)
            i = i3;
          int i4 = localRectangle3.y;
          if (m > i4)
            m = i4;
          i4 = localRectangle3.y + localRectangle3.height;
          if (j < i4)
            j = i4;
        }
      if (n != 0)
        localRectangle1 = new Rectangle(k - 1, m + 1, i - k + 2, j - m + 2);
    }
    return localRectangle1;
  }

  public int drawImage(int paramInt1, BufferedImage paramBufferedImage, GraphicsState paramGraphicsState, boolean paramBoolean, String paramString, int paramInt2, int paramInt3)
  {
    if (paramInt3 != -1)
      return redrawImage(paramInt1, paramGraphicsState, paramString, paramInt3);
    this.rawPageNumber = paramInt1;
    float[][] arrayOfFloat = paramGraphicsState.CTM;
    float f1 = paramGraphicsState.x;
    float f2 = paramGraphicsState.y;
    double[] arrayOfDouble1 = new double[6];
    int i = ((paramBufferedImage.getWidth() < 100) && (paramBufferedImage.getHeight() < 100)) || (paramBufferedImage.getHeight() == 1) ? 1 : 0;
    String str;
    if (this.rawKey == null)
      str = new StringBuilder().append(paramInt1).append("_").append(this.currentItem + 1).toString();
    else
      str = new StringBuilder().append(this.rawKey).append('_').append(this.currentItem + 1).toString();
    if (this.imageOptions == null)
    {
      this.imageOptions = new Vector_Int(5000);
      this.imageOptions.setCheckpoint();
    }
    int j = (paramInt2 == 0) && (arrayOfFloat[0][0] < 0.0F) && (arrayOfFloat[0][1] > 0.0F) && (arrayOfFloat[1][0] < 0.0F) && (arrayOfFloat[1][1] < 0.0F) && (this.pageRotation == 0) && (this.type == 1) ? 1 : 0;
    int k;
    if ((!paramBoolean) && (paramBufferedImage.getHeight() > 1) && ((paramInt2 & 0x1) != 1))
    {
      k = (this.optimisedTurnCode) && (arrayOfFloat[0][0] * arrayOfFloat[0][1] == 0.0F) && (arrayOfFloat[1][1] * arrayOfFloat[1][0] == 0.0F) && (!RenderUtils.isRotated(arrayOfFloat)) ? 1 : 0;
      if ((this.optimisedTurnCode) && (k == 0) && (arrayOfFloat[0][0] > 0.0F) && (arrayOfFloat[1][1] < 0.0F) && (arrayOfFloat[0][1] > 0.0F) && (arrayOfFloat[1][0] > 0.0F))
        k = 1;
      if (((!this.optimisedTurnCode) || (k == 0)) && (this.pageRotation != 90) && (this.pageRotation != 270) && ((this.type == 3) || (j != 0)))
        paramBufferedImage = RenderUtils.invertImage(paramBufferedImage);
      if (k != 0)
        paramInt2 += 4;
    }
    this.imageOptions.addElement(paramInt2);
    Object localObject2;
    if (this.useHiResImageForDisplay)
    {
      int m;
      if ((!paramBoolean) || (this.cachedWidths.get(str) == null))
      {
        k = paramBufferedImage.getWidth();
        m = paramBufferedImage.getHeight();
      }
      else
      {
        k = ((Integer)this.cachedWidths.get(str)).intValue();
        m = ((Integer)this.cachedHeights.get(str)).intValue();
      }
      boolean bool = RenderUtils.isRotated(arrayOfFloat);
      if (bool)
      {
        if ((paramInt2 & 0x2) != 2)
        {
          localObject1 = new AffineTransform();
          ((AffineTransform)localObject1).rotate(1.570796326794897D, k / 2, m / 2);
          ((AffineTransform)localObject1).translate(-(m - ((AffineTransform)localObject1).getTranslateX()), -((AffineTransform)localObject1).getTranslateY());
          double[] arrayOfDouble2 = new double[6];
          ((AffineTransform)localObject1).getMatrix(arrayOfDouble2);
          if (arrayOfDouble2[4] < 1.0D)
          {
            arrayOfDouble2[4] = 1.0D;
            localObject1 = new AffineTransform(arrayOfDouble2);
          }
          AffineTransformOp localAffineTransformOp1 = new AffineTransformOp((AffineTransform)localObject1, 2);
          if (paramBufferedImage != null)
          {
            if ((paramBufferedImage.getHeight() > 1) && (paramBufferedImage.getWidth() > 1))
              paramBufferedImage = localAffineTransformOp1.filter(paramBufferedImage, null);
            if ((RenderUtils.isInverted(arrayOfFloat)) && ((paramInt2 & 0x2) != 2))
            {
              localObject2 = new AffineTransform();
              ((AffineTransform)localObject2).scale(-1.0D, 1.0D);
              ((AffineTransform)localObject2).translate(-paramBufferedImage.getWidth(), 0.0D);
              AffineTransformOp localAffineTransformOp2 = new AffineTransformOp((AffineTransform)localObject2, ColorSpaces.hints);
              if (paramBufferedImage.getType() == 12)
              {
                BufferedImage localBufferedImage = paramBufferedImage;
                paramBufferedImage = new BufferedImage(localBufferedImage.getWidth(), localBufferedImage.getHeight(), localBufferedImage.getType());
                localAffineTransformOp2.filter(localBufferedImage, paramBufferedImage);
              }
              else
              {
                paramBufferedImage = localAffineTransformOp2.filter(paramBufferedImage, null);
              }
            }
          }
          localObject2 = new float[][] { { 0.0F, 1.0F / m, 0.0F }, { 1.0F / k, 0.0F, 0.0F }, { 0.0F, 0.0F, 1.0F } };
          arrayOfFloat = Matrix.multiply((float[][])localObject2, arrayOfFloat);
        }
        else
        {
          localObject1 = new float[][] { { 0.0F, 1.0F / k, 0.0F }, { 1.0F / m, 0.0F, 0.0F }, { 0.0F, 0.0F, 1.0F } };
          arrayOfFloat = Matrix.multiply((float[][])localObject1, arrayOfFloat);
        }
      }
      else
      {
        localObject1 = new float[][] { { 1.0F / k, 0.0F, 0.0F }, { 0.0F, 1.0F / m, 0.0F }, { 0.0F, 0.0F, 1.0F } };
        arrayOfFloat = Matrix.multiply((float[][])localObject1, arrayOfFloat);
      }
      Object localObject1 = new AffineTransform(arrayOfFloat[0][0], arrayOfFloat[0][1], arrayOfFloat[1][0], arrayOfFloat[1][1], 0.0F, 0.0F);
      ((AffineTransform)localObject1).getMatrix(arrayOfDouble1);
      drawAffine(arrayOfDouble1);
      this.lastAf[0] = arrayOfDouble1[0];
      this.lastAf[1] = arrayOfDouble1[1];
      this.lastAf[2] = arrayOfDouble1[2];
      this.lastAf[3] = arrayOfDouble1[3];
      if ((!paramBoolean) && (i == 0))
      {
        if (!this.isPrinting)
        {
          if (this.rawKey == null)
            this.largeImages.put(new StringBuilder().append("HIRES_").append(this.currentItem).toString(), paramBufferedImage);
          else
            this.largeImages.put(new StringBuilder().append("HIRES_").append(this.currentItem).append('_').append(this.rawKey).toString(), paramBufferedImage);
          if (this.imageCount == 0)
          {
            this.singleImage = paramBufferedImage.getSubimage(0, 0, paramBufferedImage.getWidth(), paramBufferedImage.getHeight());
            this.imageCount += 1;
          }
          else
          {
            this.singleImage = null;
          }
        }
        if (this.rawKey == null)
        {
          this.objectStoreRef.saveStoredImage(new StringBuilder().append(paramInt1).append("_HIRES_").append(this.currentItem).toString(), paramBufferedImage, false, false, "tif");
          this.imageIDtoName.put(Integer.valueOf(this.currentItem), new StringBuilder().append(paramInt1).append("_HIRES_").append(this.currentItem).toString());
        }
        else
        {
          this.objectStoreRef.saveStoredImage(new StringBuilder().append(paramInt1).append("_HIRES_").append(this.currentItem).append('_').append(this.rawKey).toString(), paramBufferedImage, false, false, "tif");
          this.imageIDtoName.put(Integer.valueOf(this.currentItem), new StringBuilder().append(paramInt1).append("_HIRES_").append(this.currentItem).append('_').append(this.rawKey).toString());
        }
        if (this.rawKey == null)
          str = new StringBuilder().append(paramInt1).append("_").append(this.currentItem).toString();
        else
          str = new StringBuilder().append(this.rawKey).append('_').append(this.currentItem).toString();
        this.cachedWidths.put(str, Integer.valueOf(k));
        this.cachedHeights.put(str, Integer.valueOf(m));
      }
    }
    this.x_coord = RenderUtils.checkSize(this.x_coord, this.currentItem);
    this.y_coord = RenderUtils.checkSize(this.y_coord, this.currentItem);
    this.x_coord[this.currentItem] = f1;
    this.y_coord[this.currentItem] = f2;
    this.objectType.addElement(3);
    float f3 = 1.0F;
    float f4 = 1.0F;
    if (this.useHiResImageForDisplay)
      if (!paramBoolean)
      {
        f3 = paramBufferedImage.getWidth();
        f4 = paramBufferedImage.getHeight();
      }
      else
      {
        f3 = ((Integer)this.cachedWidths.get(str)).intValue();
        f4 = ((Integer)this.cachedHeights.get(str)).intValue();
      }
    if ((arrayOfFloat[0][0] > 0.0F) && (arrayOfFloat[0][0] < 0.05D) && (arrayOfFloat[0][1] != 0.0F) && (arrayOfFloat[1][0] != 0.0F) && (arrayOfFloat[1][1] != 0.0F))
    {
      this.areas.addElement(null);
    }
    else
    {
      this.w = ((int)(arrayOfFloat[0][0] * f3));
      if (this.w == 0)
        this.w = ((int)(arrayOfFloat[0][1] * f3));
      this.h = ((int)(arrayOfFloat[1][1] * f4));
      if (this.h == 0)
        this.h = ((int)(arrayOfFloat[1][0] * f4));
      if ((!this.useHiResImageForDisplay) && (arrayOfFloat[1][0] < 0.0F) && (arrayOfFloat[0][1] > 0.0F) && (arrayOfFloat[0][0] == 0.0F) && (arrayOfFloat[1][1] == 0.0F))
      {
        n = this.w;
        this.w = (-this.h);
        this.h = n;
      }
      if ((this.h < 0) && (!this.useHiResImageForDisplay))
        this.h = (-this.h);
      int n = (int)paramGraphicsState.x;
      int i1 = (int)paramGraphicsState.y;
      int i2 = this.w;
      int i3 = this.h;
      if (i3 < 0)
      {
        i1 += i3;
        i3 = -i3;
      }
      if (i3 == 0)
        i3 = 1;
      localObject2 = new Rectangle(n, i1, i2, i3);
      this.areas.addElement((Rectangle)localObject2);
      checkWidth((Rectangle)localObject2);
    }
    if ((this.useHiResImageForDisplay) && (i == 0))
      this.pageObjects.addElement(null);
    else
      this.pageObjects.addElement(paramBufferedImage);
    this.imageID.put(paramString, Integer.valueOf(this.currentItem));
    this.storedImageValues.put(new StringBuilder().append("imageOptions-").append(this.currentItem).toString(), Integer.valueOf(paramInt2));
    this.storedImageValues.put(new StringBuilder().append("imageAff-").append(this.currentItem).toString(), arrayOfDouble1);
    this.currentItem += 1;
    return this.currentItem - 1;
  }

  private int redrawImage(int paramInt1, GraphicsState paramGraphicsState, String paramString, int paramInt2)
  {
    this.rawPageNumber = paramInt1;
    float f1 = paramGraphicsState.x;
    float f2 = paramGraphicsState.y;
    this.imageOptions.addElement(((Integer)this.storedImageValues.get(new StringBuilder().append("imageOptions-").append(paramInt2).toString())).intValue());
    if (this.useHiResImageForDisplay)
    {
      localObject = (double[])this.storedImageValues.get(new StringBuilder().append("imageAff-").append(paramInt2).toString());
      drawAffine((double[])localObject);
      this.lastAf[0] = localObject[0];
      this.lastAf[1] = localObject[1];
      this.lastAf[2] = localObject[2];
      this.lastAf[3] = localObject[3];
      if ((this.rawKey == null) && (this.imageIDtoName.containsKey(Integer.valueOf(paramInt2))))
        this.imageIDtoName.put(Integer.valueOf(this.currentItem), new StringBuilder().append(paramInt1).append("_HIRES_").append(paramInt2).toString());
      else
        this.imageIDtoName.put(Integer.valueOf(this.currentItem), new StringBuilder().append(paramInt1).append("_HIRES_").append(paramInt2).append('_').append(this.rawKey).toString());
    }
    this.x_coord = RenderUtils.checkSize(this.x_coord, this.currentItem);
    this.y_coord = RenderUtils.checkSize(this.y_coord, this.currentItem);
    this.x_coord[this.currentItem] = f1;
    this.y_coord[this.currentItem] = f2;
    this.objectType.addElement(29);
    Object localObject = this.areas.elementAt(paramInt2);
    Rectangle localRectangle = null;
    if (localObject != null)
      localRectangle = new Rectangle((int)f1, (int)f2, ((Rectangle)localObject).width, ((Rectangle)localObject).height);
    this.areas.addElement(localRectangle);
    if (localObject != null)
      checkWidth(localRectangle);
    this.pageObjects.addElement(Integer.valueOf(paramInt2));
    this.imageID.put(paramString, Integer.valueOf(paramInt2));
    this.currentItem += 1;
    return this.currentItem - 1;
  }

  private void checkWidth(Rectangle paramRectangle)
  {
    int i = paramRectangle.getBounds().x;
    int j = paramRectangle.getBounds().y;
    int k = j + paramRectangle.getBounds().height;
    int m = i + paramRectangle.getBounds().width;
    if (i < this.pageX1)
      this.pageX1 = i;
    if (m > this.pageX2)
      this.pageX2 = m;
    if (k > this.pageY1)
      this.pageY1 = k;
    if (j < this.pageY2)
      this.pageY2 = j;
  }

  public Rectangle getOccupiedArea()
  {
    return new Rectangle(this.pageX1, this.pageY1, this.pageX2 - this.pageX1, this.pageY1 - this.pageY2);
  }

  public void drawShape(Shape paramShape, GraphicsState paramGraphicsState, int paramInt)
  {
    int i = paramGraphicsState.getFillType();
    if ((paramShape.getBounds().getWidth() == 1.0D) && (paramShape.getBounds().getHeight() == 1.0D) && (paramGraphicsState.getLineWidth() < 1.0F))
      paramShape = new Rectangle(paramShape.getBounds().x, paramShape.getBounds().y, 1, 1);
    Object localObject;
    int j;
    if ((i == 2) || (i == 3))
    {
      localObject = paramGraphicsState.getNonstrokeColor();
      if (localObject == null)
        localObject = new PdfColor(0, 0, 0);
      if (((PdfPaint)localObject).isPattern())
      {
        drawFillColor((PdfPaint)localObject);
        this.fillSet = true;
      }
      else
      {
        j = ((PdfPaint)localObject).getRGB();
        if ((!this.fillSet) || (this.lastFillCol != j))
        {
          this.lastFillCol = j;
          drawFillColor((PdfPaint)localObject);
          this.fillSet = true;
        }
      }
    }
    if ((i == 1) || (i == 3))
    {
      localObject = paramGraphicsState.getStrokeColor();
      if ((localObject instanceof Color))
      {
        j = ((PdfPaint)localObject).getRGB();
        if ((!this.strokeSet) || (this.lastStrokeCol != j))
        {
          this.lastStrokeCol = j;
          drawStrokeColor((Paint)localObject);
          this.strokeSet = true;
        }
      }
      else
      {
        drawStrokeColor((Paint)localObject);
        this.strokeSet = true;
      }
    }
    Stroke localStroke = paramGraphicsState.getStroke();
    if ((this.lastStroke == null) || (!this.lastStroke.equals(localStroke)))
    {
      this.lastStroke = localStroke;
      drawStroke(localStroke);
    }
    this.pageObjects.addElement(paramShape);
    this.objectType.addElement(2);
    this.areas.addElement(paramShape.getBounds());
    checkWidth(paramShape.getBounds());
    this.x_coord = RenderUtils.checkSize(this.x_coord, this.currentItem);
    this.y_coord = RenderUtils.checkSize(this.y_coord, this.currentItem);
    this.x_coord[this.currentItem] = paramGraphicsState.x;
    this.y_coord[this.currentItem] = paramGraphicsState.y;
    this.shapeType.addElement(i);
    this.currentItem += 1;
    this.resetTextColors = true;
  }

  private void drawColor(PdfPaint paramPdfPaint, int paramInt)
  {
    this.areas.addElement(null);
    this.pageObjects.addElement(null);
    this.objectType.addElement(10);
    this.textFillType.addElement(paramInt);
    this.text_color.addElement(paramPdfPaint);
    this.x_coord = RenderUtils.checkSize(this.x_coord, this.currentItem);
    this.y_coord = RenderUtils.checkSize(this.y_coord, this.currentItem);
    this.x_coord[this.currentItem] = 0.0F;
    this.y_coord[this.currentItem] = 0.0F;
    this.currentItem += 1;
    this.strokeSet = false;
    this.fillSet = false;
  }

  public void drawXForm(DynamicVectorRenderer paramDynamicVectorRenderer, GraphicsState paramGraphicsState)
  {
    this.areas.addElement(null);
    this.pageObjects.addElement(paramDynamicVectorRenderer);
    this.objectType.addElement(25);
    this.x_coord = RenderUtils.checkSize(this.x_coord, this.currentItem);
    this.y_coord = RenderUtils.checkSize(this.y_coord, this.currentItem);
    this.x_coord[this.currentItem] = 0.0F;
    this.y_coord[this.currentItem] = 0.0F;
    this.currentItem += 1;
  }

  public void resetOnColorspaceChange()
  {
    this.fillSet = false;
    this.strokeSet = false;
  }

  public void drawFillColor(PdfPaint paramPdfPaint)
  {
    this.pageObjects.addElement(null);
    this.objectType.addElement(11);
    this.areas.addElement(null);
    this.fill_color.addElement(paramPdfPaint);
    this.x_coord = RenderUtils.checkSize(this.x_coord, this.currentItem);
    this.y_coord = RenderUtils.checkSize(this.y_coord, this.currentItem);
    this.x_coord[this.currentItem] = 0.0F;
    this.y_coord[this.currentItem] = 0.0F;
    this.currentItem += 1;
    this.lastFillCol = paramPdfPaint.getRGB();
  }

  public void setGraphicsState(int paramInt, float paramFloat)
  {
    if ((paramFloat != 1.0F) || (this.opacity != null))
    {
      if (this.opacity == null)
      {
        this.opacity = new Vector_Float(5000);
        this.opacity.setCheckpoint();
      }
      this.pageObjects.addElement(null);
      this.areas.addElement(null);
      if (paramInt == 1)
        this.objectType.addElement(17);
      else
        this.objectType.addElement(18);
      this.opacity.addElement(paramFloat);
      this.x_coord = RenderUtils.checkSize(this.x_coord, this.currentItem);
      this.y_coord = RenderUtils.checkSize(this.y_coord, this.currentItem);
      this.x_coord[this.currentItem] = 0.0F;
      this.y_coord[this.currentItem] = 0.0F;
      this.currentItem += 1;
    }
  }

  public void drawAdditionalObjectsOverPage(int[] paramArrayOfInt, Color[] paramArrayOfColor, Object[] paramArrayOfObject)
    throws PdfException
  {
    if (paramArrayOfObject == null)
      return;
    if (this.endItem == -1)
    {
      this.endItem = this.currentItem;
      this.objectType.setCheckpoint();
      this.shapeType.setCheckpoint();
      this.pageObjects.setCheckpoint();
      this.areas.setCheckpoint();
      this.clips.setCheckpoint();
      this.textFillType.setCheckpoint();
      this.text_color.setCheckpoint();
      this.fill_color.setCheckpoint();
      this.stroke_color.setCheckpoint();
      this.stroke.setCheckpoint();
      if (this.imageOptions == null)
        this.imageOptions = new Vector_Int(5000);
      this.imageOptions.setCheckpoint();
      if (this.TRvalues == null)
        this.TRvalues = new Vector_Int(5000);
      this.TRvalues.setCheckpoint();
      if (this.fs == null)
        this.fs = new Vector_Int(5000);
      this.fs.setCheckpoint();
      if (this.lw == null)
        this.lw = new Vector_Int(5000);
      this.lw.setCheckpoint();
      this.af1.setCheckpoint();
      this.af2.setCheckpoint();
      this.af3.setCheckpoint();
      this.af4.setCheckpoint();
      this.fontBounds.setCheckpoint();
      if (this.opacity != null)
        this.opacity.setCheckpoint();
    }
    int i = paramArrayOfInt.length;
    for (int k = 0; k < i; k++)
    {
      int j = paramArrayOfInt[k];
      GraphicsState localGraphicsState;
      switch (j)
      {
      case 18:
        setGraphicsState(2, ((Float)paramArrayOfObject[k]).floatValue());
        break;
      case 17:
        setGraphicsState(1, ((Float)paramArrayOfObject[k]).floatValue());
        break;
      case 19:
        localGraphicsState = new GraphicsState(false);
        localGraphicsState.setFillType(1);
        localGraphicsState.setStrokeColor(new PdfColor(paramArrayOfColor[k].getRed(), paramArrayOfColor[k].getGreen(), paramArrayOfColor[k].getBlue()));
        drawShape((Shape)paramArrayOfObject[k], localGraphicsState, 83);
        break;
      case 20:
        localGraphicsState = new GraphicsState(false);
        localGraphicsState.setFillType(2);
        localGraphicsState.setNonstrokeColor(new PdfColor(paramArrayOfColor[k].getRed(), paramArrayOfColor[k].getGreen(), paramArrayOfColor[k].getBlue()));
        drawShape((Shape)paramArrayOfObject[k], localGraphicsState, 70);
        break;
      case 23:
        drawCustom(paramArrayOfObject[k]);
        break;
      case 3:
        ImageObject localImageObject = (ImageObject)paramArrayOfObject[k];
        localGraphicsState = new GraphicsState(false);
        localGraphicsState.CTM = new float[][] { { localImageObject.image.getWidth(), 0.0F, 1.0F }, { 0.0F, localImageObject.image.getHeight(), 1.0F }, { 0.0F, 0.0F, 0.0F } };
        localGraphicsState.x = localImageObject.x;
        localGraphicsState.y = localImageObject.y;
        drawImage(this.rawPageNumber, localImageObject.image, localGraphicsState, false, new StringBuilder().append("extImg").append(k).toString(), 0, -1);
        break;
      case 16:
        TextObject localTextObject = (TextObject)paramArrayOfObject[k];
        localGraphicsState = new GraphicsState(false);
        float f = localTextObject.font.getSize();
        double[] arrayOfDouble = { f, 0.0D, 0.0D, f, 0.0D, 0.0D };
        drawAffine(arrayOfDouble);
        drawTR(2);
        localGraphicsState.setTextRenderType(2);
        localGraphicsState.setNonstrokeColor(new PdfColor(paramArrayOfColor[k].getRed(), paramArrayOfColor[k].getGreen(), paramArrayOfColor[k].getBlue()));
        drawText((float[][])null, localTextObject.text, localGraphicsState, localTextObject.x, -localTextObject.y, localTextObject.font);
        break;
      case 0:
        break;
      case 1:
      case 2:
      case 4:
      case 5:
      case 6:
      case 7:
      case 8:
      case 9:
      case 10:
      case 11:
      case 12:
      case 13:
      case 14:
      case 15:
      case 21:
      case 22:
      default:
        throw new PdfException(new StringBuilder().append("Unrecognised type ").append(j).toString());
      }
    }
  }

  private static String getTypeAsString(int paramInt)
  {
    String str = "Value Not set";
    switch (paramInt)
    {
    case 18:
      str = "FILLOPACITY";
      break;
    case 17:
      str = "STROKEOPACITY";
      break;
    case 19:
      str = "STROKEDSHAPE";
      break;
    case 20:
      str = "FILLEDSHAPE";
      break;
    case 23:
      str = "CUSTOM";
      break;
    case 3:
      str = "IMAGE";
      break;
    case 16:
      str = "String";
    case 4:
    case 5:
    case 6:
    case 7:
    case 8:
    case 9:
    case 10:
    case 11:
    case 12:
    case 13:
    case 14:
    case 15:
    case 21:
    case 22:
    }
    return str;
  }

  public void flushAdditionalObjOnPage()
  {
    if (this.endItem != -1)
      this.currentItem = this.endItem;
    this.endItem = -1;
    this.objectType.resetToCheckpoint();
    this.shapeType.resetToCheckpoint();
    this.pageObjects.resetToCheckpoint();
    this.areas.resetToCheckpoint();
    this.clips.resetToCheckpoint();
    this.textFillType.resetToCheckpoint();
    this.text_color.resetToCheckpoint();
    this.fill_color.resetToCheckpoint();
    this.stroke_color.resetToCheckpoint();
    this.stroke.resetToCheckpoint();
    if (this.imageOptions != null)
      this.imageOptions.resetToCheckpoint();
    if (this.TRvalues != null)
      this.TRvalues.resetToCheckpoint();
    if (this.fs != null)
      this.fs.resetToCheckpoint();
    if (this.lw != null)
      this.lw.resetToCheckpoint();
    this.af1.resetToCheckpoint();
    this.af2.resetToCheckpoint();
    this.af3.resetToCheckpoint();
    this.af4.resetToCheckpoint();
    this.fontBounds.resetToCheckpoint();
    if (this.opacity != null)
      this.opacity.resetToCheckpoint();
    this.lastFillTextCol = 0;
    this.lastFillCol = 0;
    this.lastStrokeCol = 0;
    this.lastClip = null;
    this.hasClips = false;
    this.lastStroke = null;
    this.lastAf = new double[4];
    this.fillSet = false;
    this.strokeSet = false;
  }

  public void drawStrokeColor(Paint paramPaint)
  {
    this.pageObjects.addElement(null);
    this.objectType.addElement(12);
    this.areas.addElement(null);
    this.stroke_color.addElement(paramPaint);
    this.x_coord = RenderUtils.checkSize(this.x_coord, this.currentItem);
    this.y_coord = RenderUtils.checkSize(this.y_coord, this.currentItem);
    this.x_coord[this.currentItem] = 0.0F;
    this.y_coord[this.currentItem] = 0.0F;
    this.currentItem += 1;
    this.strokeSet = false;
    this.fillSet = false;
    this.resetTextColors = true;
  }

  public void drawCustom(Object paramObject)
  {
    this.pageObjects.addElement(paramObject);
    this.objectType.addElement(23);
    this.areas.addElement(null);
    this.x_coord = RenderUtils.checkSize(this.x_coord, this.currentItem);
    this.y_coord = RenderUtils.checkSize(this.y_coord, this.currentItem);
    this.x_coord[this.currentItem] = 0.0F;
    this.y_coord[this.currentItem] = 0.0F;
    this.currentItem += 1;
  }

  public void drawTR(int paramInt)
  {
    if (paramInt != this.lastTR)
    {
      if (this.TRvalues == null)
      {
        this.TRvalues = new Vector_Int(5000);
        this.TRvalues.setCheckpoint();
      }
      this.lastTR = paramInt;
      this.pageObjects.addElement(null);
      this.objectType.addElement(15);
      this.areas.addElement(null);
      this.TRvalues.addElement(paramInt);
      this.x_coord = RenderUtils.checkSize(this.x_coord, this.currentItem);
      this.y_coord = RenderUtils.checkSize(this.y_coord, this.currentItem);
      this.x_coord[this.currentItem] = 0.0F;
      this.y_coord[this.currentItem] = 0.0F;
      this.currentItem += 1;
    }
  }

  public void drawStroke(Stroke paramStroke)
  {
    this.pageObjects.addElement(null);
    this.objectType.addElement(14);
    this.areas.addElement(null);
    this.stroke.addElement(paramStroke);
    this.x_coord = RenderUtils.checkSize(this.x_coord, this.currentItem);
    this.y_coord = RenderUtils.checkSize(this.y_coord, this.currentItem);
    this.x_coord[this.currentItem] = 0.0F;
    this.y_coord[this.currentItem] = 0.0F;
    this.currentItem += 1;
  }

  public void drawClip(GraphicsState paramGraphicsState, Shape paramShape, boolean paramBoolean)
  {
    int i = 0;
    Area localArea = paramGraphicsState.getClippingShape();
    if ((!paramBoolean) || (!this.hasClips) || (this.lastClip != null) || (localArea != null))
      if ((!paramBoolean) || (this.lastClip == null) || (localArea == null))
      {
        i = 1;
      }
      else
      {
        Rectangle localRectangle1 = localArea.getBounds();
        Rectangle localRectangle2 = this.lastClip.getBounds();
        if ((localRectangle1.x != localRectangle2.x) || (localRectangle1.y != localRectangle2.y) || (localRectangle1.width != localRectangle2.width) || (localRectangle1.height != localRectangle2.height))
        {
          i = 1;
        }
        else
        {
          int j = isRectangle(localRectangle1);
          int k = isRectangle(localRectangle2);
          if (((j != 6) || (k != 6)) && (!localArea.equals(this.lastClip)))
            i = 1;
        }
      }
    if (i != 0)
    {
      this.pageObjects.addElement(null);
      this.objectType.addElement(7);
      this.areas.addElement(null);
      this.lastClip = localArea;
      if (localArea == null)
        this.clips.addElement(null);
      else
        this.clips.addElement((Area)localArea.clone());
      this.x_coord = RenderUtils.checkSize(this.x_coord, this.currentItem);
      this.y_coord = RenderUtils.checkSize(this.y_coord, this.currentItem);
      this.x_coord[this.currentItem] = paramGraphicsState.x;
      this.y_coord[this.currentItem] = paramGraphicsState.y;
      this.currentItem += 1;
      this.hasClips = true;
    }
  }

  public void drawEmbeddedText(float[][] paramArrayOfFloat, int paramInt1, PdfGlyph paramPdfGlyph, Object paramObject, int paramInt2, GraphicsState paramGraphicsState, AffineTransform paramAffineTransform, String paramString, PdfFont paramPdfFont, float paramFloat)
  {
    if (this.htmlDVR != null)
    {
      this.htmlDVR.drawEmbeddedText(paramArrayOfFloat, paramInt1, paramPdfGlyph, paramObject, paramInt2, paramGraphicsState, paramAffineTransform, paramString, paramPdfFont, paramFloat);
      return;
    }
    int i = paramGraphicsState.getTextRenderType();
    PdfPaint localPdfPaint;
    int j;
    if ((i & 0x2) == 2)
    {
      localPdfPaint = paramGraphicsState.getNonstrokeColor();
      if (localPdfPaint.isPattern())
      {
        drawColor(localPdfPaint, 2);
        this.resetTextColors = true;
      }
      else
      {
        j = localPdfPaint.getRGB();
        if ((this.resetTextColors) || (this.lastFillTextCol != j))
        {
          this.lastFillTextCol = j;
          drawColor(localPdfPaint, 2);
          this.resetTextColors = false;
        }
      }
    }
    if ((i & 0x1) == 1)
    {
      localPdfPaint = paramGraphicsState.getStrokeColor();
      if (localPdfPaint.isPattern())
      {
        drawColor(localPdfPaint, 1);
        this.resetTextColors = true;
      }
      else
      {
        j = localPdfPaint.getRGB();
        if ((this.resetTextColors) || (this.lastStrokeCol != j))
        {
          this.resetTextColors = false;
          this.lastStrokeCol = j;
          drawColor(localPdfPaint, 1);
        }
      }
    }
    setLineWidth((int)paramGraphicsState.getLineWidth());
    drawFontSize(paramInt1);
    double[] arrayOfDouble;
    if (paramObject != null)
    {
      if (paramArrayOfFloat != null)
      {
        arrayOfDouble = new double[] { paramArrayOfFloat[0][0], paramArrayOfFloat[0][1], paramArrayOfFloat[1][0], paramArrayOfFloat[1][1], paramArrayOfFloat[2][0], paramArrayOfFloat[2][1] };
        if ((this.lastAf[0] != arrayOfDouble[0]) || (this.lastAf[1] != arrayOfDouble[1]) || (this.lastAf[2] != arrayOfDouble[2]) || (this.lastAf[3] != arrayOfDouble[3]))
        {
          drawAffine(arrayOfDouble);
          this.lastAf[0] = arrayOfDouble[0];
          this.lastAf[1] = arrayOfDouble[1];
          this.lastAf[2] = arrayOfDouble[2];
          this.lastAf[3] = arrayOfDouble[3];
        }
      }
      if (!(paramObject instanceof Area))
        paramInt2 = -paramInt2;
    }
    else
    {
      arrayOfDouble = new double[6];
      paramAffineTransform.getMatrix(arrayOfDouble);
      if ((this.lastAf[0] != arrayOfDouble[0]) || (this.lastAf[1] != arrayOfDouble[1]) || (this.lastAf[2] != arrayOfDouble[2]) || (this.lastAf[3] != arrayOfDouble[3]))
      {
        drawAffine(arrayOfDouble);
        this.lastAf[0] = arrayOfDouble[0];
        this.lastAf[1] = arrayOfDouble[1];
        this.lastAf[2] = arrayOfDouble[2];
        this.lastAf[3] = arrayOfDouble[3];
      }
    }
    if (paramPdfGlyph == null)
      this.pageObjects.addElement(paramObject);
    else
      this.pageObjects.addElement(paramPdfGlyph);
    this.objectType.addElement(paramInt2);
    if (paramInt2 < 0)
    {
      this.areas.addElement(null);
    }
    else if (paramObject != null)
    {
      this.areas.addElement(new Rectangle((int)paramArrayOfFloat[2][0], (int)paramArrayOfFloat[2][1], paramInt1, paramInt1));
      checkWidth(new Rectangle((int)paramArrayOfFloat[2][0], (int)paramArrayOfFloat[2][1], paramInt1, paramInt1));
    }
    else
    {
      int k = paramInt1;
      if (k < 0)
        k = -k;
      Rectangle localRectangle = new Rectangle((int)paramArrayOfFloat[2][0], (int)paramArrayOfFloat[2][1], k, k);
      this.areas.addElement(localRectangle);
      checkWidth(localRectangle);
    }
    this.x_coord = RenderUtils.checkSize(this.x_coord, this.currentItem);
    this.y_coord = RenderUtils.checkSize(this.y_coord, this.currentItem);
    this.x_coord[this.currentItem] = paramArrayOfFloat[2][0];
    this.y_coord[this.currentItem] = paramArrayOfFloat[2][1];
    this.currentItem += 1;
  }

  public void drawFontBounds(Rectangle paramRectangle)
  {
    this.pageObjects.addElement(null);
    this.objectType.addElement(24);
    this.areas.addElement(null);
    this.fontBounds.addElement(paramRectangle);
    this.x_coord = RenderUtils.checkSize(this.x_coord, this.currentItem);
    this.y_coord = RenderUtils.checkSize(this.y_coord, this.currentItem);
    this.x_coord[this.currentItem] = 0.0F;
    this.y_coord[this.currentItem] = 0.0F;
    this.currentItem += 1;
  }

  public void drawAffine(double[] paramArrayOfDouble)
  {
    this.pageObjects.addElement(null);
    this.objectType.addElement(9);
    this.areas.addElement(null);
    this.af1.addElement(paramArrayOfDouble[0]);
    this.af2.addElement(paramArrayOfDouble[1]);
    this.af3.addElement(paramArrayOfDouble[2]);
    this.af4.addElement(paramArrayOfDouble[3]);
    this.x_coord = RenderUtils.checkSize(this.x_coord, this.currentItem);
    this.y_coord = RenderUtils.checkSize(this.y_coord, this.currentItem);
    this.x_coord[this.currentItem] = ((float)paramArrayOfDouble[4]);
    this.y_coord[this.currentItem] = ((float)paramArrayOfDouble[5]);
    this.currentItem += 1;
  }

  public void drawFontSize(int paramInt)
  {
    int i = paramInt;
    if (i < 0)
      i = -i;
    if (i != this.lastFS)
    {
      this.pageObjects.addElement(null);
      this.objectType.addElement(21);
      this.areas.addElement(null);
      if (this.fs == null)
      {
        this.fs = new Vector_Int(5000);
        this.fs.setCheckpoint();
      }
      this.fs.addElement(paramInt);
      this.x_coord = RenderUtils.checkSize(this.x_coord, this.currentItem);
      this.y_coord = RenderUtils.checkSize(this.y_coord, this.currentItem);
      this.x_coord[this.currentItem] = 0.0F;
      this.y_coord[this.currentItem] = 0.0F;
      this.currentItem += 1;
      this.lastFS = i;
    }
  }

  public void setLineWidth(int paramInt)
  {
    if (paramInt != this.lastLW)
    {
      this.areas.addElement(null);
      this.pageObjects.addElement(null);
      this.objectType.addElement(22);
      if (this.lw == null)
      {
        this.lw = new Vector_Int(5000);
        this.lw.setCheckpoint();
      }
      this.lw.addElement(paramInt);
      this.x_coord = RenderUtils.checkSize(this.x_coord, this.currentItem);
      this.y_coord = RenderUtils.checkSize(this.y_coord, this.currentItem);
      this.x_coord[this.currentItem] = 0.0F;
      this.y_coord[this.currentItem] = 0.0F;
      this.currentItem += 1;
      this.lastLW = paramInt;
    }
  }

  public SwingDisplay(byte[] paramArrayOfByte, Map paramMap)
  {
    try
    {
      this.fonts = paramMap;
      ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(paramArrayOfByte);
      int i = localByteArrayInputStream.read();
      if (i != 1)
        throw new PdfException(new StringBuilder().append("Unknown version in serialised object ").append(i).toString());
      int j = localByteArrayInputStream.read();
      this.useHiResImageForDisplay = (j == 1);
      this.rawPageNumber = localByteArrayInputStream.read();
      this.x_coord = ((float[])RenderUtils.restoreFromStream(localByteArrayInputStream));
      this.y_coord = ((float[])RenderUtils.restoreFromStream(localByteArrayInputStream));
      this.text_color = ((Vector_Object)RenderUtils.restoreFromStream(localByteArrayInputStream));
      this.textFillType = ((Vector_Int)RenderUtils.restoreFromStream(localByteArrayInputStream));
      this.stroke_color = new Vector_Object();
      this.stroke_color.restoreFromStream(localByteArrayInputStream);
      this.fill_color = new Vector_Object();
      this.fill_color.restoreFromStream(localByteArrayInputStream);
      this.stroke = new Vector_Object();
      this.stroke.restoreFromStream(localByteArrayInputStream);
      this.pageObjects = new Vector_Object();
      this.pageObjects.restoreFromStream(localByteArrayInputStream);
      this.javaObjects = ((Vector_Object)RenderUtils.restoreFromStream(localByteArrayInputStream));
      this.shapeType = ((Vector_Int)RenderUtils.restoreFromStream(localByteArrayInputStream));
      this.af1 = ((Vector_Double)RenderUtils.restoreFromStream(localByteArrayInputStream));
      this.af2 = ((Vector_Double)RenderUtils.restoreFromStream(localByteArrayInputStream));
      this.af3 = ((Vector_Double)RenderUtils.restoreFromStream(localByteArrayInputStream));
      this.af4 = ((Vector_Double)RenderUtils.restoreFromStream(localByteArrayInputStream));
      this.fontBounds = new Vector_Rectangle();
      this.fontBounds.restoreFromStream(localByteArrayInputStream);
      this.clips = new Vector_Shape();
      this.clips.restoreFromStream(localByteArrayInputStream);
      this.objectType = ((Vector_Int)RenderUtils.restoreFromStream(localByteArrayInputStream));
      this.opacity = ((Vector_Float)RenderUtils.restoreFromStream(localByteArrayInputStream));
      this.imageOptions = ((Vector_Int)RenderUtils.restoreFromStream(localByteArrayInputStream));
      this.TRvalues = ((Vector_Int)RenderUtils.restoreFromStream(localByteArrayInputStream));
      this.fs = ((Vector_Int)RenderUtils.restoreFromStream(localByteArrayInputStream));
      this.lw = ((Vector_Int)RenderUtils.restoreFromStream(localByteArrayInputStream));
      int k = ((Integer)RenderUtils.restoreFromStream(localByteArrayInputStream)).intValue();
      Object localObject2;
      for (int m = 0; m < k; m++)
      {
        Object localObject1 = RenderUtils.restoreFromStream(localByteArrayInputStream);
        localObject2 = RenderUtils.restoreFromStream(localByteArrayInputStream);
        paramMap.put(localObject1, localObject2);
      }
      m = ((Integer)RenderUtils.restoreFromStream(localByteArrayInputStream)).intValue();
      for (int n = 0; n < m; n++)
      {
        localObject2 = RenderUtils.restoreFromStream(localByteArrayInputStream);
        PdfJavaGlyphs localPdfJavaGlyphs = (PdfJavaGlyphs)paramMap.get(localObject2);
        localPdfJavaGlyphs.setDisplayValues((Map)RenderUtils.restoreFromStream(localByteArrayInputStream));
        localPdfJavaGlyphs.setCharGlyphs((Map)RenderUtils.restoreFromStream(localByteArrayInputStream));
        localPdfJavaGlyphs.setEmbeddedEncs((Map)RenderUtils.restoreFromStream(localByteArrayInputStream));
      }
      localByteArrayInputStream.close();
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog(new StringBuilder().append("Exception: ").append(localException.getMessage()).toString());
    }
    this.currentItem = this.pageObjects.get().length;
  }

  public void stopClearOnNextRepaint(boolean paramBoolean)
  {
    this.noRepaint = paramBoolean;
  }

  public byte[] serializeToByteArray(Set paramSet)
    throws IOException
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    localByteArrayOutputStream.write(1);
    if (this.useHiResImageForDisplay)
      localByteArrayOutputStream.write(1);
    else
      localByteArrayOutputStream.write(0);
    localByteArrayOutputStream.write(this.rawPageNumber);
    this.text_color.trim();
    this.stroke_color.trim();
    this.fill_color.trim();
    this.stroke.trim();
    this.pageObjects.trim();
    this.javaObjects.trim();
    this.stroke.trim();
    this.pageObjects.trim();
    this.javaObjects.trim();
    this.shapeType.trim();
    this.af1.trim();
    this.af2.trim();
    this.af3.trim();
    this.af4.trim();
    this.fontBounds.trim();
    this.clips.trim();
    this.objectType.trim();
    if (this.opacity != null)
      this.opacity.trim();
    if (this.imageOptions != null)
      this.imageOptions.trim();
    if (this.TRvalues != null)
      this.TRvalues.trim();
    if (this.fs != null)
      this.fs.trim();
    if (this.lw != null)
      this.lw.trim();
    RenderUtils.writeToStream(localByteArrayOutputStream, this.x_coord);
    RenderUtils.writeToStream(localByteArrayOutputStream, this.y_coord);
    RenderUtils.writeToStream(localByteArrayOutputStream, this.text_color);
    RenderUtils.writeToStream(localByteArrayOutputStream, this.textFillType);
    this.stroke_color.writeToStream(localByteArrayOutputStream);
    this.fill_color.writeToStream(localByteArrayOutputStream);
    this.stroke.writeToStream(localByteArrayOutputStream);
    this.pageObjects.writeToStream(localByteArrayOutputStream);
    RenderUtils.writeToStream(localByteArrayOutputStream, this.javaObjects);
    RenderUtils.writeToStream(localByteArrayOutputStream, this.shapeType);
    RenderUtils.writeToStream(localByteArrayOutputStream, this.af1);
    RenderUtils.writeToStream(localByteArrayOutputStream, this.af2);
    RenderUtils.writeToStream(localByteArrayOutputStream, this.af3);
    RenderUtils.writeToStream(localByteArrayOutputStream, this.af4);
    this.fontBounds.writeToStream(localByteArrayOutputStream);
    this.clips.writeToStream(localByteArrayOutputStream);
    RenderUtils.writeToStream(localByteArrayOutputStream, this.objectType);
    RenderUtils.writeToStream(localByteArrayOutputStream, this.opacity);
    RenderUtils.writeToStream(localByteArrayOutputStream, this.imageOptions);
    RenderUtils.writeToStream(localByteArrayOutputStream, this.TRvalues);
    RenderUtils.writeToStream(localByteArrayOutputStream, this.fs);
    RenderUtils.writeToStream(localByteArrayOutputStream, this.lw);
    int i = 0;
    int j = 0;
    HashMap localHashMap1 = new HashMap(10);
    HashMap localHashMap2 = new HashMap(10);
    Iterator localIterator = this.fontsUsed.keySet().iterator();
    Object localObject;
    while (localIterator.hasNext())
    {
      localObject = localIterator.next();
      if (!paramSet.contains(localObject))
      {
        i++;
        localHashMap2.put(localObject, "x");
      }
      else
      {
        j++;
        localHashMap1.put(localObject, "x");
      }
    }
    RenderUtils.writeToStream(localByteArrayOutputStream, Integer.valueOf(i));
    localIterator = localHashMap2.keySet().iterator();
    while (localIterator.hasNext())
    {
      localObject = localIterator.next();
      RenderUtils.writeToStream(localByteArrayOutputStream, localObject);
      RenderUtils.writeToStream(localByteArrayOutputStream, this.fonts.get(localObject));
      paramSet.add(localObject);
    }
    RenderUtils.writeToStream(localByteArrayOutputStream, Integer.valueOf(j));
    localIterator = localHashMap1.keySet().iterator();
    while (localIterator.hasNext())
    {
      localObject = localIterator.next();
      RenderUtils.writeToStream(localByteArrayOutputStream, localObject);
      PdfJavaGlyphs localPdfJavaGlyphs = (PdfJavaGlyphs)this.fonts.get(localObject);
      RenderUtils.writeToStream(localByteArrayOutputStream, localPdfJavaGlyphs.getDisplayValues());
      RenderUtils.writeToStream(localByteArrayOutputStream, localPdfJavaGlyphs.getCharGlyphs());
      RenderUtils.writeToStream(localByteArrayOutputStream, localPdfJavaGlyphs.getEmbeddedEncs());
    }
    localByteArrayOutputStream.close();
    this.fontsUsed.clear();
    return localByteArrayOutputStream.toByteArray();
  }

  public void setneedsVerticalInvert(boolean paramBoolean)
  {
    this.needsVerticalInvert = paramBoolean;
  }

  public void setneedsHorizontalInvert(boolean paramBoolean)
  {
    this.needsHorizontalInvert = paramBoolean;
  }

  public void checkFontSaved(Object paramObject, String paramString, PdfFont paramPdfFont)
  {
    this.pageObjects.addElement(paramObject);
    this.objectType.addElement(200);
    this.areas.addElement(null);
    this.currentItem += 1;
    if ((this.fontsUsed.get(paramString) == null) || (paramPdfFont.isFontSubsetted()))
    {
      this.fonts.put(paramString, paramPdfFont.getGlyphData());
      this.fontsUsed.put(paramString, "x");
    }
  }

  public Rectangle getArea(int paramInt)
  {
    return this.areas.elementAt(paramInt);
  }

  public int isInsideImage(int paramInt1, int paramInt2)
  {
    int i = -1;
    Rectangle[] arrayOfRectangle = this.areas.get();
    Rectangle localRectangle = null;
    int j = arrayOfRectangle.length;
    int[] arrayOfInt = this.objectType.get();
    for (int k = 0; k < j; k++)
      if ((arrayOfRectangle[k] != null) && (RenderUtils.rectangleContains(arrayOfRectangle[k], paramInt1, paramInt2)) && (arrayOfInt[k] == 3))
        if (localRectangle != null)
        {
          int m = localRectangle.height * localRectangle.width;
          int n = arrayOfRectangle[k].height * arrayOfRectangle[k].width;
          if (n < m)
            localRectangle = arrayOfRectangle[k];
          i = k;
        }
        else
        {
          localRectangle = arrayOfRectangle[k];
          i = k;
        }
    return i;
  }

  public void saveImage(int paramInt, String paramString1, String paramString2)
  {
    String str = (String)this.imageIDtoName.get(Integer.valueOf(paramInt));
    BufferedImage localBufferedImage;
    if (this.useHiResImageForDisplay)
    {
      localBufferedImage = this.objectStoreRef.loadStoredImage(str);
      if (localBufferedImage == null)
        localBufferedImage = (BufferedImage)this.pageObjects.elementAt(paramInt);
    }
    else
    {
      localBufferedImage = (BufferedImage)this.pageObjects.elementAt(paramInt);
    }
    if (localBufferedImage != null)
    {
      if (!this.optimisedTurnCode)
        localBufferedImage = RenderUtils.invertImage(localBufferedImage);
      if ((localBufferedImage.getType() == 0) || ((paramString2.equals("jpg")) && (localBufferedImage.getType() == 2)))
      {
        localBufferedImage = ColorSpaceConvertor.convertToRGB(localBufferedImage);
        if ((localBufferedImage.getType() == 0) && (DecoderOptions.showErrorMessages))
          JOptionPane.showMessageDialog(null, "This is a custom Image, Java's standard libraries may not be able to save the image as a jpg correctly.\nEnabling JAI will ensure correct output. \n\nFor information on how to do this please go to http://www.idrsolutions.com/jvm-flags/");
      }
      if (this.needsHorizontalInvert)
        localBufferedImage = RenderUtils.invertImageBeforeSave(localBufferedImage, true);
      if (this.needsVerticalInvert)
        localBufferedImage = RenderUtils.invertImageBeforeSave(localBufferedImage, false);
      if ((JAIHelper.isJAIused()) && (paramString2.toLowerCase().startsWith("tif")))
      {
        JAIHelper.filestore(localBufferedImage, paramString1, paramString2);
      }
      else if (paramString2.toLowerCase().startsWith("tif"))
      {
        if (DecoderOptions.showErrorMessages)
          JOptionPane.showMessageDialog(null, "Please setup JAI library for Tiffs");
        if (LogWriter.isOutput())
          LogWriter.writeLog("Please setup JAI library for Tiffs");
      }
      else
      {
        DefaultIO.write(localBufferedImage, paramString2, paramString1);
      }
    }
  }

  public void flagDecodingFinished()
  {
    this.highlightsNeedToBeGenerated = true;
  }

  private void generateHighlights(Graphics2D paramGraphics2D, int paramInt, int[] paramArrayOfInt1, Object[] paramArrayOfObject, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, int[] paramArrayOfInt2, Rectangle[] paramArrayOfRectangle)
  {
    this.highlightsNeedToBeGenerated = false;
    int[] arrayOfInt1 = new int[paramInt];
    int i = -1;
    int j = 0;
    float[] arrayOfFloat1 = new float[paramInt];
    float[] arrayOfFloat2 = new float[paramInt];
    float[] arrayOfFloat3 = new float[paramInt];
    float[] arrayOfFloat4 = new float[paramInt];
    boolean[] arrayOfBoolean = new boolean[paramInt];
    int[] arrayOfInt2 = new int[paramInt];
    float[] arrayOfFloat5 = new float[paramInt];
    this.textHighlightsX = new int[paramInt];
    int[] arrayOfInt3 = new int[paramInt];
    this.textHighlightsWidth = new int[paramInt];
    this.textHighlightsHeight = new int[paramInt];
    int k = 0;
    int m = 1000;
    int n = 1000;
    int i1 = 1;
    int i2 = 1;
    double[] arrayOfDouble = new double[6];
    paramGraphics2D.getTransform().getMatrix(arrayOfDouble);
    int i3 = 0;
    if ((arrayOfDouble[1] < 0.0D) && (arrayOfDouble[2] < 0.0D))
      i3 = 270;
    for (int i4 = 0; i4 < paramInt; i4++)
    {
      this.type = paramArrayOfInt1[i4];
      if (this.type > 0)
      {
        float f1 = this.x_coord[i4];
        float f2 = this.y_coord[i4];
        if (i2 < 0)
          f1 += i2;
        Object localObject = paramArrayOfObject[i4];
        if (this.type == 24)
        {
          Rectangle localRectangle = paramArrayOfRectangle[j];
          m = localRectangle.height;
          k = localRectangle.y;
          n = localRectangle.width;
          j++;
        }
        else if (this.type == 21)
        {
          i++;
          i2 = paramArrayOfInt2[i];
          if (i2 < 0)
            i1 = -i2;
          else
            i1 = i2;
        }
        else if ((this.type == 4) || (this.type == 5) || (this.type == 1))
        {
          if ((this.type == 4) || (this.type == 5))
          {
            PdfGlyph localPdfGlyph = (PdfGlyph)localObject;
            float f3 = i1 / 1000.0F;
            this.textHighlightsX[i4] = localPdfGlyph.getFontBB(1);
            arrayOfInt3[i4] = k;
            this.textHighlightsWidth[i4] = localPdfGlyph.getFontBB(3);
            this.textHighlightsHeight[i4] = m;
            arrayOfBoolean[i4] = true;
            if (i3 == 90)
            {
              arrayOfFloat2[i4] = (-(arrayOfInt3[i4] * f3) + f1);
              arrayOfFloat3[i4] = (this.textHighlightsX[i4] * f3 + f2);
            }
            else if (i3 == 270)
            {
              arrayOfFloat2[i4] = (arrayOfInt3[i4] * f3 + f1);
              arrayOfFloat3[i4] = (-(this.textHighlightsX[i4] * f3 + f2));
            }
            else
            {
              arrayOfFloat2[i4] = (arrayOfInt3[i4] * f3 + f2);
              arrayOfFloat3[i4] = (this.textHighlightsX[i4] * f3 + f1);
            }
            arrayOfFloat2[i4] += this.textHighlightsHeight[i4] * f3;
            arrayOfFloat3[i4] += this.textHighlightsWidth[i4] * f3;
            arrayOfFloat5[i4] = 10.0F;
            arrayOfInt2[i4] = i1;
          }
          else
          {
            float f4 = 1000.0F / i1;
            this.textHighlightsX[i4] = ((int)f1);
            arrayOfInt3[i4] = ((int)(f2 + k / f4));
            this.textHighlightsWidth[i4] = ((int)(n / f4));
            this.textHighlightsHeight[i4] = ((int)((m - k) / f4));
            if (i3 == 90)
            {
              arrayOfFloat2[i4] = (-arrayOfInt3[i4]);
              arrayOfFloat3[i4] = this.textHighlightsX[i4];
            }
            else if (i3 == 270)
            {
              arrayOfFloat2[i4] = arrayOfInt3[i4];
              arrayOfFloat3[i4] = (-this.textHighlightsX[i4]);
            }
            else
            {
              arrayOfFloat2[i4] = arrayOfInt3[i4];
              arrayOfFloat3[i4] = this.textHighlightsX[i4];
            }
            arrayOfFloat2[i4] += this.textHighlightsHeight[i4];
            arrayOfFloat3[i4] += this.textHighlightsWidth[i4];
            arrayOfFloat5[i4] = ((Area)localObject).getBounds().width;
            arrayOfInt2[i4] = i1;
          }
          arrayOfInt1[i4] = i4;
        }
      }
    }
    i4 = -31;
    for (int i5 = 0; i5 < paramInt - 1; i5++)
    {
      int i6 = arrayOfInt1[i5];
      if (i6 == i4)
        System.out.println(new StringBuilder().append("*").append(i6).append(" = ").append(" left=").append(arrayOfFloat3[i6]).append(" bottom=").append(arrayOfFloat2[i6]).append(" right=").append(arrayOfFloat4[i6]).append(" top=").append(arrayOfFloat1[i6]).toString());
      for (int i8 = i5 + 1; i8 < paramInt; i8++)
      {
        int i9 = arrayOfInt1[i8];
        if ((arrayOfBoolean[i9] == arrayOfBoolean[i6]) && (arrayOfFloat5[i9] >= 1.0F))
        {
          if (i6 == i4)
            System.out.println(new StringBuilder().append("compare with=").append(i9).append(" left=").append(arrayOfFloat3[i9]).append(" right=").append(arrayOfFloat4[i9]).append(' ').append((arrayOfFloat3[i9] > arrayOfFloat3[i6]) && (arrayOfFloat3[i9] < arrayOfFloat4[i6])).toString());
          if (((arrayOfFloat3[i9] > arrayOfFloat3[i6]) && (arrayOfFloat3[i9] < arrayOfFloat4[i6])) || ((arrayOfFloat3[i9] > (arrayOfFloat3[i6] + arrayOfFloat4[i6]) / 2.0F) && (arrayOfFloat4[i6] < arrayOfFloat4[i9])))
          {
            int i10 = this.textHighlightsWidth[i6];
            int i11 = this.textHighlightsX[i6];
            int i7;
            if (arrayOfBoolean[i9] != 0)
            {
              float f5 = arrayOfFloat3[i9] - arrayOfFloat4[i6];
              if (f5 > 0.0F)
                f5 += 0.5F;
              else
                f5 += 0.5F;
              i7 = (int)(f5 * 1000.0F / arrayOfInt2[i6]);
              if (this.textHighlightsX[i9] > 0)
                i7 += this.textHighlightsX[i9];
            }
            else
            {
              i7 = (int)(arrayOfFloat3[i9] - arrayOfFloat4[i6]);
            }
            if (i6 == i4)
              System.out.println(new StringBuilder().append(arrayOfFloat3[i9] - arrayOfFloat4[i6]).append(" gap=").append(i7).append(' ').append((arrayOfFloat3[i9] - arrayOfFloat4[i6]) * 1000.0F / arrayOfInt2[i6]).append(" currentX=").append(i11).append(" scaling=").append(this.scaling).append(' ').append(n).toString());
            int i12 = (i7 > 0) || ((i7 < 0) && (arrayOfFloat3[i6] < arrayOfFloat3[i9]) && (arrayOfFloat4[i6] > arrayOfFloat3[i9]) && (arrayOfFloat4[i6] < arrayOfFloat4[i9]) && (arrayOfFloat3[i6] < arrayOfFloat4[i6]) && (((-i7 < arrayOfInt2[i6]) && (arrayOfBoolean[i6] == 0)) || ((-i7 < n) && (arrayOfBoolean[i6] != 0)))) ? 1 : 0;
            if ((arrayOfFloat2[i6] < arrayOfFloat1[i9]) && (arrayOfFloat2[i9] < arrayOfFloat1[i6]) && ((i7 > 0) || (i12 != 0)))
              if ((i12 != 0) && (((arrayOfBoolean[i6] == 0) && (i7 < arrayOfInt2[i6]) && (i10 + i7 < arrayOfInt2[i6])) || ((arrayOfBoolean[i6] != 0) && (i7 < n))))
              {
                if (i6 == i4)
                  System.out.println(new StringBuilder().append(i9).append(" = ").append(" left=").append(arrayOfFloat3[i9]).append(" bottom=").append(arrayOfFloat2[i9]).append(" right=").append(arrayOfFloat4[i9]).append(" top=").append(arrayOfFloat1[i9]).toString());
                if (arrayOfBoolean[i6] != 0)
                {
                  if (i7 > 0)
                    this.textHighlightsWidth[i6] = (i10 + i7);
                  else
                    this.textHighlightsWidth[i6] = (i10 - i7);
                }
                else if (i7 > 0)
                  this.textHighlightsWidth[i6] = i7;
                else
                  this.textHighlightsWidth[i6] = (i10 + i7);
                if (i6 == i4)
                  System.out.println(new StringBuilder().append("new=").append(this.textHighlightsWidth[i6]).toString());
                i8 = paramInt;
              }
              else if (i7 <= n);
          }
        }
      }
    }
  }

  public void setPrintPage(int paramInt)
  {
    this.rawPageNumber = paramInt;
  }

  public int getObjectUnderneath(int paramInt1, int paramInt2)
  {
    int i = -1;
    Rectangle[] arrayOfRectangle = this.areas.get();
    int j = arrayOfRectangle.length;
    int[] arrayOfInt = this.objectType.get();
    int k = 1;
    for (int m = j - 1; m > -1; m--)
      if ((arrayOfRectangle[m] != null) && (RenderUtils.rectangleContains(arrayOfRectangle[m], paramInt1, paramInt2)) && (arrayOfInt[m] != 2) && (arrayOfInt[m] != 7))
      {
        k = 0;
        i = arrayOfInt[m];
        m = -1;
      }
    if (k != 0)
      return -1;
    return i;
  }

  public void flagImageDeleted(int paramInt)
  {
    this.objectType.setElementAt(27, paramInt);
  }

  public void setOCR(boolean paramBoolean)
  {
    this.hasOCR = paramBoolean;
  }

  static
  {
    hints = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.render.SwingDisplay
 * JD-Core Version:    0.6.2
 */