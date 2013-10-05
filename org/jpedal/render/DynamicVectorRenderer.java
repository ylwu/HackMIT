package org.jpedal.render;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Set;
import javafx.scene.shape.Path;
import org.jpedal.color.PdfPaint;
import org.jpedal.exception.PdfException;
import org.jpedal.external.ColorHandler;
import org.jpedal.external.ImageHandler;
import org.jpedal.fonts.PdfFont;
import org.jpedal.fonts.glyph.PdfGlyph;
import org.jpedal.io.ObjectStore;
import org.jpedal.objects.GraphicsState;

public abstract interface DynamicVectorRenderer
{
  public static final int TEXT = 1;
  public static final int SHAPE = 2;
  public static final int IMAGE = 3;
  public static final int TRUETYPE = 4;
  public static final int TYPE1C = 5;
  public static final int TYPE3 = 6;
  public static final int CLIP = 7;
  public static final int COLOR = 8;
  public static final int AF = 9;
  public static final int TEXTCOLOR = 10;
  public static final int FILLCOLOR = 11;
  public static final int STROKECOLOR = 12;
  public static final int STROKE = 14;
  public static final int TR = 15;
  public static final int STRING = 16;
  public static final int STROKEOPACITY = 17;
  public static final int FILLOPACITY = 18;
  public static final int STROKEDSHAPE = 19;
  public static final int FILLEDSHAPE = 20;
  public static final int FONTSIZE = 21;
  public static final int LINEWIDTH = 22;
  public static final int CUSTOM = 23;
  public static final int fontBB = 24;
  public static final int XFORM = 25;
  public static final int DELETED_IMAGE = 27;
  public static final int REUSED_IMAGE = 29;
  public static final int MARKER = 200;
  public static final boolean debugPaint = false;
  public static final int DISPLAY_SCREEN = 1;
  public static final int DISPLAY_IMAGE = 2;
  public static final int CREATE_PATTERN = 3;
  public static final int CREATE_HTML = 4;
  public static final int CREATE_SVG = 5;
  public static final int CREATE_JAVAFX = 6;
  public static final int CREATE_EPOS = 7;
  public static final int ALT_BACKGROUND_COLOR = 1;
  public static final int ALT_FOREGROUND_COLOR = 2;
  public static final int FOREGROUND_INCLUDE_LINEART = 3;
  public static final int COLOR_REPLACEMENT_THRESHOLD = 4;

  public abstract void setG2(Graphics2D paramGraphics2D);

  public abstract void setOptimsePainting(boolean paramBoolean);

  public abstract void flush();

  public abstract void dispose();

  public abstract void setInset(int paramInt1, int paramInt2);

  public abstract Rectangle paint(Rectangle[] paramArrayOfRectangle, AffineTransform paramAffineTransform, Rectangle paramRectangle);

  public abstract void setMessageFrame(Container paramContainer);

  public abstract void paintBackground(Shape paramShape);

  public abstract void drawText(float[][] paramArrayOfFloat, String paramString, GraphicsState paramGraphicsState, float paramFloat1, float paramFloat2, Font paramFont);

  public abstract void init(int paramInt1, int paramInt2, int paramInt3, Color paramColor);

  public abstract int drawImage(int paramInt1, BufferedImage paramBufferedImage, GraphicsState paramGraphicsState, boolean paramBoolean, String paramString, int paramInt2, int paramInt3);

  public abstract Rectangle getOccupiedArea();

  public abstract void drawShape(Shape paramShape, GraphicsState paramGraphicsState, int paramInt);

  public abstract void drawShape(Path paramPath, GraphicsState paramGraphicsState, int paramInt);

  public abstract void drawXForm(DynamicVectorRenderer paramDynamicVectorRenderer, GraphicsState paramGraphicsState);

  public abstract void resetOnColorspaceChange();

  public abstract void drawFillColor(PdfPaint paramPdfPaint);

  public abstract void setGraphicsState(int paramInt, float paramFloat);

  public abstract void drawAdditionalObjectsOverPage(int[] paramArrayOfInt, Color[] paramArrayOfColor, Object[] paramArrayOfObject)
    throws PdfException;

  public abstract void flushAdditionalObjOnPage();

  public abstract void drawStrokeColor(Paint paramPaint);

  public abstract void drawCustom(Object paramObject);

  public abstract void drawTR(int paramInt);

  public abstract void drawStroke(Stroke paramStroke);

  public abstract void drawClip(GraphicsState paramGraphicsState, Shape paramShape, boolean paramBoolean);

  public abstract void drawEmbeddedText(float[][] paramArrayOfFloat, int paramInt1, PdfGlyph paramPdfGlyph, Object paramObject, int paramInt2, GraphicsState paramGraphicsState, AffineTransform paramAffineTransform, String paramString, PdfFont paramPdfFont, float paramFloat);

  public abstract void drawFontBounds(Rectangle paramRectangle);

  public abstract void drawAffine(double[] paramArrayOfDouble);

  public abstract void drawFontSize(int paramInt);

  public abstract void setLineWidth(int paramInt);

  public abstract void setHiResImageForDisplayMode(boolean paramBoolean);

  public abstract void setScalingValues(double paramDouble1, double paramDouble2, float paramFloat);

  public abstract void stopClearOnNextRepaint(boolean paramBoolean);

  public abstract void setCustomImageHandler(ImageHandler paramImageHandler);

  public abstract void setCustomColorHandler(ColorHandler paramColorHandler);

  public abstract void flagDecodingFinished();

  public abstract ObjectStore getObjectStore();

  public abstract void flagImageDeleted(int paramInt);

  public abstract void setOCR(boolean paramBoolean);

  public abstract byte[] serializeToByteArray(Set paramSet)
    throws IOException;

  public abstract void checkFontSaved(Object paramObject, String paramString, PdfFont paramPdfFont);

  public abstract boolean hasObjectsBehind(float[][] paramArrayOfFloat);

  public abstract Rectangle getArea(int paramInt);

  public abstract int isInsideImage(int paramInt1, int paramInt2);

  public abstract void saveImage(int paramInt, String paramString1, String paramString2);

  public abstract int getObjectUnderneath(int paramInt1, int paramInt2);

  public abstract void setneedsVerticalInvert(boolean paramBoolean);

  public abstract void setneedsHorizontalInvert(boolean paramBoolean);

  public abstract void stopG2HintSetting(boolean paramBoolean);

  public abstract void setPrintPage(int paramInt);

  public abstract void writeCustom(int paramInt, Object paramObject);

  public abstract int getType();

  public abstract void flagCommand(int paramInt1, int paramInt2);

  public abstract void setValue(int paramInt1, int paramInt2);

  public abstract int getValue(int paramInt);

  public abstract BufferedImage getSingleImagePattern();

  public abstract boolean isScalingControlledByUser();

  public abstract boolean avoidDownSamplingImage();

  public abstract boolean getBooleanValue(int paramInt);

  public abstract float getScaling();

  public abstract void saveAdvanceWidth(String paramString1, String paramString2, int paramInt);

  public abstract void setMode(Mode paramMode);

  public abstract Mode getMode();

  public abstract Rectangle[] getAreas();

  public abstract Object getObjectValue(int paramInt);

  public static enum Mode
  {
    PDF, XFA;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.render.DynamicVectorRenderer
 * JD-Core Version:    0.6.2
 */