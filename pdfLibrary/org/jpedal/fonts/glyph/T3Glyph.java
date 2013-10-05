package org.jpedal.fonts.glyph;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import javafx.scene.shape.Path;
import org.jpedal.color.PdfPaint;
import org.jpedal.render.T3Display;
import org.jpedal.render.T3Renderer;
import org.jpedal.utils.LogWriter;

public class T3Glyph
  implements PdfGlyph
{
  private boolean lockColours = false;
  T3Renderer glyphDisplay;
  private int maxWidth;
  private int maxHeight;
  String stringName = "";
  float glyphScale = 1.0F;
  private int glyphNumber = -1;
  int id = 0;

  public T3Glyph()
  {
  }

  public String getGlyphName()
  {
    return this.stringName;
  }

  public void setStringName(String paramString)
  {
    this.stringName = paramString;
  }

  public T3Glyph(T3Renderer paramT3Renderer, int paramInt1, int paramInt2, boolean paramBoolean, String paramString)
  {
    this.glyphDisplay = paramT3Renderer;
    this.maxWidth = paramInt1;
    this.maxHeight = paramInt2;
    this.lockColours = paramBoolean;
    this.stringName = paramString;
  }

  public void setScaling(float paramFloat)
  {
    this.glyphScale = paramFloat;
  }

  public Area getShape()
  {
    return null;
  }

  public void render(int paramInt, Graphics2D paramGraphics2D, float paramFloat, boolean paramBoolean)
  {
    this.glyphDisplay.setScalingValues(0.0D, 0.0D, paramFloat);
    float f = this.glyphScale;
    if (paramBoolean)
      this.glyphScale = (paramFloat * this.glyphScale);
    AffineTransform localAffineTransform = null;
    if (this.glyphScale != 1.0F)
    {
      localAffineTransform = paramGraphics2D.getTransform();
      paramGraphics2D.scale(this.glyphScale, this.glyphScale);
    }
    this.glyphDisplay.setG2(paramGraphics2D);
    this.glyphDisplay.paint(null, null, null);
    if (localAffineTransform != null)
      paramGraphics2D.setTransform(localAffineTransform);
    this.glyphScale = f;
  }

  public float getmaxWidth()
  {
    if ((this.maxWidth == 0) && (this.glyphScale < 1.0F))
      return 1.0F / this.glyphScale;
    return this.maxWidth;
  }

  public int getmaxHeight()
  {
    return this.maxHeight;
  }

  public void setT3Colors(PdfPaint paramPdfPaint1, PdfPaint paramPdfPaint2, boolean paramBoolean)
  {
    this.glyphDisplay.lockColors(paramPdfPaint1, paramPdfPaint2, paramBoolean);
  }

  public boolean ignoreColors()
  {
    return this.lockColours;
  }

  public int getID()
  {
    return this.id;
  }

  public void setID(int paramInt)
  {
    this.id = paramInt;
  }

  public int getGlyphNumber()
  {
    return this.glyphNumber;
  }

  public void setGlyphNumber(int paramInt)
  {
    this.glyphNumber = paramInt;
  }

  public void writePathsToStream(ObjectOutput paramObjectOutput)
    throws IOException
  {
    byte[] arrayOfByte = this.glyphDisplay.serializeToByteArray(null);
    paramObjectOutput.writeObject(arrayOfByte);
    paramObjectOutput.writeInt(this.maxWidth);
    paramObjectOutput.writeInt(this.maxHeight);
    paramObjectOutput.writeBoolean(this.lockColours);
  }

  public T3Glyph(ObjectInput paramObjectInput)
  {
    try
    {
      byte[] arrayOfByte = (byte[])paramObjectInput.readObject();
      this.glyphDisplay = new T3Display(arrayOfByte, null);
      this.maxWidth = paramObjectInput.readInt();
      this.maxHeight = paramObjectInput.readInt();
      this.lockColours = paramObjectInput.readBoolean();
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception: " + localException.getMessage());
    }
  }

  public void flushArea()
  {
  }

  public void setWidth(float paramFloat)
  {
  }

  public int getFontBB(int paramInt)
  {
    return 0;
  }

  public void setStrokedOnly(boolean paramBoolean)
  {
  }

  public boolean containsBrokenData()
  {
    return false;
  }

  public Path getPath()
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.fonts.glyph.T3Glyph
 * JD-Core Version:    0.6.2
 */