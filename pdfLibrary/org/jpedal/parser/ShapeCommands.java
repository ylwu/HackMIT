package org.jpedal.parser;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import javafx.scene.shape.Path;
import org.jpedal.color.GenericColorSpace;
import org.jpedal.color.PdfPaint;
import org.jpedal.io.PdfArray;
import org.jpedal.objects.GraphicsState;
import org.jpedal.objects.PdfPageData;
import org.jpedal.objects.PdfShape;
import org.jpedal.render.DynamicVectorRenderer;

public class ShapeCommands
{
  static Shape B(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, GraphicsState paramGraphicsState, int paramInt, PdfShape paramPdfShape, LayerDecoder paramLayerDecoder, boolean paramBoolean4, DynamicVectorRenderer paramDynamicVectorRenderer)
  {
    Object localObject = null;
    if (paramLayerDecoder.isLayerVisible())
    {
      if (paramBoolean1)
        paramPdfShape.setEVENODDWindingRule();
      else
        paramPdfShape.setNONZEROWindingRule();
      if (paramBoolean3)
        paramPdfShape.closeShape();
      Path localPath = null;
      if (paramBoolean2)
      {
        localPath = paramPdfShape.getPath();
      }
      else
      {
        localObject = paramPdfShape.generateShapeFromPath(paramGraphicsState.CTM, paramGraphicsState.getLineWidth(), 66, paramDynamicVectorRenderer.getType());
        if ((localObject != null) && (((Shape)localObject).getBounds2D().getWidth() < 1.0D) && (((Shape)localObject).getBounds2D().getHeight() < 1.0D))
          return null;
      }
      int i = (localObject != null) || (localPath != null) ? 1 : 0;
      if ((!paramBoolean2) && (!paramBoolean3) && (paramInt > 0) && (i != 0) && (paramGraphicsState.getClippingShape() != null) && (paramGraphicsState.nonstrokeColorSpace.getID() == 1498837125) && (paramGraphicsState.nonstrokeColorSpace.getColor().getRGB() == -1))
      {
        Area localArea = paramGraphicsState.getClippingShape();
        localArea.subtract(new Area((Shape)localObject));
        localObject = localArea;
      }
      if ((paramBoolean4) && (i != 0))
      {
        paramGraphicsState.setStrokeColor(paramGraphicsState.strokeColorSpace.getColor());
        paramGraphicsState.setNonstrokeColor(paramGraphicsState.nonstrokeColorSpace.getColor());
        if ((paramGraphicsState.nonstrokeColorSpace.getColor().getRGB() == -16777216) && (paramGraphicsState.getAlpha(1) == 0.0F))
          paramGraphicsState.setFillType(1);
        else
          paramGraphicsState.setFillType(3);
        if (paramBoolean2)
          paramDynamicVectorRenderer.drawShape(localPath, paramGraphicsState, 66);
        else
          paramDynamicVectorRenderer.drawShape((Shape)localObject, paramGraphicsState, 66);
      }
    }
    paramPdfShape.setClip(false);
    paramPdfShape.resetPath();
    return localObject;
  }

  static void D(CommandParser paramCommandParser, GraphicsState paramGraphicsState)
  {
    String str1 = "";
    int i = paramCommandParser.getOperandCount();
    int j;
    if (i == 1)
      str1 = paramCommandParser.generateOpAsString(0, false);
    else
      for (j = i - 1; j > -1; j--)
      {
        str1 = str1 + paramCommandParser.generateOpAsString(j, false);
        str1 = str1 + ' ';
      }
    if ((str1.equals("[ ] 0 ")) || (str1.equals("[]0")) || (str1.equals("[] 0 ")))
    {
      paramGraphicsState.setDashPhase(0);
      paramGraphicsState.setDashArray(new float[0]);
    }
    else
    {
      j = str1.indexOf(']');
      String str2 = str1.substring(0, j);
      int k = (int)Float.parseFloat(str1.substring(j + 1, str1.length()).trim());
      float[] arrayOfFloat = PdfArray.convertToFloatArray(str2);
      for (int m = 0; m < arrayOfFloat.length; m++)
        if (arrayOfFloat[m] < 0.001D)
          arrayOfFloat[m] = 0.0F;
      paramGraphicsState.setDashArray(arrayOfFloat);
      paramGraphicsState.setDashPhase(k);
    }
  }

  static void J(boolean paramBoolean, int paramInt, GraphicsState paramGraphicsState)
  {
    int i = 0;
    if (!paramBoolean)
    {
      if (paramInt == 0)
        i = 0;
      if (paramInt == 1)
        i = 1;
      if (paramInt == 2)
        i = 2;
      paramGraphicsState.setJoinStyle(i);
    }
    else
    {
      if (paramInt == 0)
        i = 0;
      if (paramInt == 1)
        i = 1;
      if (paramInt == 2)
        i = 2;
      paramGraphicsState.setCapStyle(i);
    }
  }

  static void N(boolean paramBoolean1, PdfShape paramPdfShape, GraphicsState paramGraphicsState, int paramInt1, Shape paramShape, boolean paramBoolean2, DynamicVectorRenderer paramDynamicVectorRenderer, PdfPageData paramPdfPageData, int paramInt2)
  {
    if (paramPdfShape.isClip())
    {
      paramPdfShape.closeShape();
      Object localObject = null;
      Path localPath = null;
      if (paramBoolean1)
        localPath = paramPdfShape.getPath();
      else
        localObject = paramPdfShape.generateShapeFromPath(paramGraphicsState.CTM, 0.0F, 110, paramDynamicVectorRenderer.getType());
      if (localObject != null)
        if (paramPdfShape.getComplexClipCount() < 5)
        {
          if (paramPdfShape.getSegmentCount() > 5000)
            localObject = ((Shape)localObject).getBounds();
        }
        else if (paramPdfShape.getSegmentCount() > 2500)
          localObject = ((Shape)localObject).getBounds();
      if (paramBoolean1)
        paramGraphicsState.updateClip(localPath);
      else
        paramGraphicsState.updateClip(new Area((Shape)localObject));
      if (paramInt1 == 0)
        paramGraphicsState.checkWholePageClip(paramPdfPageData.getMediaBoxHeight(paramInt2) + paramPdfPageData.getMediaBoxY(paramInt2));
      paramPdfShape.setClip(false);
      if (paramBoolean2)
        if (paramBoolean1)
          paramDynamicVectorRenderer.drawClip(paramGraphicsState, paramShape, false);
        else
          paramDynamicVectorRenderer.drawClip(paramGraphicsState, paramShape, false);
    }
    paramPdfShape.resetPath();
  }

  static Shape S(boolean paramBoolean1, boolean paramBoolean2, LayerDecoder paramLayerDecoder, GraphicsState paramGraphicsState, PdfShape paramPdfShape, DynamicVectorRenderer paramDynamicVectorRenderer, boolean paramBoolean3)
  {
    Object localObject = null;
    if (paramLayerDecoder.isLayerVisible())
    {
      if (paramBoolean1)
        paramPdfShape.closeShape();
      Path localPath = null;
      if (paramBoolean2)
        localPath = paramPdfShape.getPath();
      else
        localObject = paramPdfShape.generateShapeFromPath(paramGraphicsState.CTM, paramGraphicsState.getLineWidth(), 83, paramDynamicVectorRenderer.getType());
      int i = (localObject != null) || (localPath != null) ? 1 : 0;
      if (i != 0)
      {
        Area localArea = paramGraphicsState.getClippingShape();
        if ((localArea != null) && ((localArea.getBounds().getWidth() == 0.0D) || (localArea.getBounds().getHeight() == 0.0D)))
        {
          localObject = null;
          localPath = null;
          i = 0;
        }
      }
      if (i != 0)
      {
        if ((localObject != null) && (((Shape)localObject).getBounds().getWidth() <= 1.0D))
          localObject = ((Shape)localObject).getBounds2D();
        if (paramBoolean3)
        {
          paramGraphicsState.setStrokeColor(paramGraphicsState.strokeColorSpace.getColor());
          paramGraphicsState.setNonstrokeColor(paramGraphicsState.nonstrokeColorSpace.getColor());
          paramGraphicsState.setFillType(1);
          if (paramBoolean2)
            paramDynamicVectorRenderer.drawShape(localPath, paramGraphicsState, 83);
          else
            paramDynamicVectorRenderer.drawShape((Shape)localObject, paramGraphicsState, 83);
        }
      }
    }
    paramPdfShape.setClip(false);
    paramPdfShape.resetPath();
    return localObject;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.parser.ShapeCommands
 * JD-Core Version:    0.6.2
 */