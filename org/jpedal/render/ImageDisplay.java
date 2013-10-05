package org.jpedal.render;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import org.jpedal.color.PdfPaint;
import org.jpedal.fonts.PdfFont;
import org.jpedal.fonts.glyph.PdfGlyph;
import org.jpedal.io.ObjectStore;
import org.jpedal.objects.GraphicsState;
import org.jpedal.utils.repositories.Vector_Rectangle;

public class ImageDisplay extends BaseDisplay
  implements DynamicVectorRenderer
{
  boolean trackOutlines = false;

  public ImageDisplay(int paramInt1, boolean paramBoolean, int paramInt2, ObjectStore paramObjectStore)
  {
    this.type = 2;
    this.rawPageNumber = paramInt1;
    this.objectStoreRef = paramObjectStore;
    this.addBackground = paramBoolean;
    this.areas = new Vector_Rectangle(paramInt2);
    this.imageAndShapeAreas = new Vector_Rectangle(paramInt2);
  }

  public final int drawImage(int paramInt1, BufferedImage paramBufferedImage, GraphicsState paramGraphicsState, boolean paramBoolean, String paramString, int paramInt2, int paramInt3)
  {
    int i = (int)paramGraphicsState.CTM[0][0];
    if (i < 0)
      i = -i;
    if (i == 0)
      i = (int)paramGraphicsState.CTM[0][1];
    if (i < 0)
      i = -i;
    int j = (int)paramGraphicsState.CTM[1][1];
    if (j < 0)
      j = -j;
    if (j == 0)
      j = (int)paramGraphicsState.CTM[1][0];
    if (j < 0)
      j = -j;
    this.areas.addElement(new Rectangle((int)paramGraphicsState.CTM[2][0], (int)paramGraphicsState.CTM[2][1], i, j));
    renderImage(null, paramBufferedImage, paramGraphicsState.getAlpha(2), paramGraphicsState, paramGraphicsState.x, paramGraphicsState.y, paramInt2);
    return -1;
  }

  public void drawClip(GraphicsState paramGraphicsState, Shape paramShape, boolean paramBoolean)
  {
    Area localArea = paramGraphicsState.getClippingShape();
    if (((!paramBoolean) || (!this.hasClips) || (this.lastClip != null) || (localArea != null)) && ((!paramBoolean) || (this.lastClip == null) || (localArea == null) || (!localArea.equals(this.lastClip))))
    {
      RenderUtils.renderClip(paramGraphicsState.getClippingShape(), null, paramShape, this.g2);
      this.lastClip = localArea;
      this.hasClips = true;
    }
  }

  public void drawEmbeddedText(float[][] paramArrayOfFloat, int paramInt1, PdfGlyph paramPdfGlyph, Object paramObject, int paramInt2, GraphicsState paramGraphicsState, AffineTransform paramAffineTransform, String paramString, PdfFont paramPdfFont, float paramFloat)
  {
    PdfPaint localPdfPaint1;
    PdfPaint localPdfPaint2;
    int i;
    Stroke localStroke;
    Object localObject;
    if (paramInt2 == 1)
    {
      localPdfPaint1 = null;
      localPdfPaint2 = null;
      i = paramGraphicsState.getTextRenderType();
      if ((i & 0x2) == 2)
        localPdfPaint2 = paramGraphicsState.getNonstrokeColor();
      if ((i & 0x1) == 1)
        localPdfPaint1 = paramGraphicsState.getStrokeColor();
      localStroke = paramGraphicsState.getStroke();
      this.g2.setStroke(localStroke);
      localObject = this.g2.getTransform();
      this.g2.translate(paramArrayOfFloat[2][0], paramArrayOfFloat[2][1]);
      this.g2.transform(paramAffineTransform);
      renderText(paramArrayOfFloat[2][0], paramArrayOfFloat[2][1], i, (Area)paramObject, null, localPdfPaint1, localPdfPaint2, paramGraphicsState.getAlpha(1), paramGraphicsState.getAlpha(2));
      this.g2.setTransform((AffineTransform)localObject);
    }
    else
    {
      localPdfPaint1 = null;
      localPdfPaint2 = null;
      i = paramGraphicsState.getTextRenderType();
      if ((i & 0x2) == 2)
        localPdfPaint2 = paramGraphicsState.getNonstrokeColor();
      if ((i & 0x1) == 1)
        localPdfPaint1 = paramGraphicsState.getStrokeColor();
      localStroke = paramGraphicsState.getStroke();
      localObject = this.g2.getStroke();
      if (i == 1)
        this.g2.setStroke(localStroke);
      int j = (int)paramGraphicsState.CTM[1][1];
      if (j < 0)
        j = -j;
      if (j == 0)
        j = (int)paramGraphicsState.CTM[0][1];
      if (j < 0)
        j = -j;
      this.areas.addElement(new Rectangle((int)paramGraphicsState.CTM[2][0], (int)paramGraphicsState.CTM[2][1], j, j));
      renderEmbeddedText(i, paramPdfGlyph, paramInt2, paramAffineTransform, null, localPdfPaint1, localPdfPaint2, paramGraphicsState.getAlpha(1), paramGraphicsState.getAlpha(2), (int)paramGraphicsState.getLineWidth());
      this.g2.setStroke((Stroke)localObject);
    }
  }

  public final void drawShape(Shape paramShape, GraphicsState paramGraphicsState, int paramInt)
  {
    if ((paramInt == 70) && (paramGraphicsState.CTM[0][0] > 0.0F) && (paramGraphicsState.CTM[1][1] > 0.0F) && (paramGraphicsState.CTM[0][1] == 0.0F) && (paramGraphicsState.CTM[1][0] == 0.0F))
    {
      this.areas.addElement(paramShape.getBounds());
      if (this.trackOutlines)
        this.imageAndShapeAreas.addElement(paramShape.getBounds());
    }
    else
    {
      this.areas.addElement(null);
      if (this.trackOutlines)
        this.imageAndShapeAreas.addElement(null);
    }
    renderShape(null, paramGraphicsState.getFillType(), paramGraphicsState.getStrokeColor(), paramGraphicsState.getNonstrokeColor(), paramGraphicsState.getStroke(), paramShape, paramGraphicsState.getAlpha(1), paramGraphicsState.getAlpha(2));
  }

  public final void drawXForm(DynamicVectorRenderer paramDynamicVectorRenderer, GraphicsState paramGraphicsState)
  {
    renderXForm(paramDynamicVectorRenderer, paramGraphicsState.getAlpha(1));
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.render.ImageDisplay
 * JD-Core Version:    0.6.2
 */