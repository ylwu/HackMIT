package org.jpedal.parser;

import com.idrsolutions.pdf.color.shading.ShadingFactory;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Rectangle2D.Double;
import java.util.Map;
import org.jpedal.color.ColorspaceFactory;
import org.jpedal.color.GenericColorSpace;
import org.jpedal.color.PdfPaint;
import org.jpedal.io.PdfObjectReader;
import org.jpedal.objects.GraphicsState;
import org.jpedal.objects.PdfPageData;
import org.jpedal.objects.raw.PdfObject;
import org.jpedal.render.DynamicVectorRenderer;
import org.jpedal.utils.LogWriter;

public class ShadingCommands
{
  public static void sh(String paramString, PdfObjectCache paramPdfObjectCache, GraphicsState paramGraphicsState, boolean paramBoolean, Map paramMap, int paramInt, PdfObjectReader paramPdfObjectReader, PdfPageData paramPdfPageData, DynamicVectorRenderer paramDynamicVectorRenderer)
  {
    PdfObject localPdfObject1 = (PdfObject)paramPdfObjectCache.localShadings.get(paramString);
    if (localPdfObject1 == null)
      localPdfObject1 = (PdfObject)paramPdfObjectCache.globalShadings.get(paramString);
    Object localObject = null;
    if (localObject == null)
      localObject = paramGraphicsState.getClippingShape();
    double d1;
    double d2;
    double d3;
    double d4;
    if ((localObject == null) && (paramGraphicsState.CTM != null) && (paramGraphicsState.CTM[0][1] > 0.0F) && (paramGraphicsState.CTM[0][0] == 0.0F) && (paramGraphicsState.CTM[1][1] == 0.0F))
    {
      d1 = paramGraphicsState.CTM[2][0];
      d2 = paramGraphicsState.CTM[2][1];
      d3 = paramGraphicsState.CTM[0][0];
      if (d3 == 0.0D)
        d3 = paramGraphicsState.CTM[0][1];
      if (d3 < 0.0D)
        d3 = -d3;
      d4 = paramGraphicsState.CTM[1][1];
      if (d4 == 0.0D)
        d4 = paramGraphicsState.CTM[1][0];
      if (d4 < 0.0D)
        d4 = -d4;
      if (paramGraphicsState.CTM[1][0] < 0.0F)
      {
        d1 += paramGraphicsState.CTM[1][0];
        d1 = -d1;
        d3 = paramGraphicsState.CTM[2][0] - d1;
      }
      localObject = new Rectangle2D.Double(d1, d2, d3, d4);
    }
    if ((localObject == null) && (paramGraphicsState.CTM[0][1] < 0.0F) && (paramGraphicsState.CTM[1][0] < 0.0F))
    {
      d1 = -paramGraphicsState.CTM[0][1];
      d2 = paramGraphicsState.CTM[2][1] + paramGraphicsState.CTM[1][0];
      d3 = paramGraphicsState.CTM[2][0] - d1;
      d4 = -paramGraphicsState.CTM[1][0];
      localObject = new Rectangle2D.Double(d1, d2, d3, d4);
    }
    if ((localObject == null) && (paramGraphicsState.CTM[0][0] > 0.0F) && (paramGraphicsState.CTM[1][1] < 0.0F))
    {
      d1 = paramGraphicsState.CTM[2][0];
      d2 = paramGraphicsState.CTM[1][1];
      d3 = paramGraphicsState.CTM[2][1];
      d4 = paramGraphicsState.CTM[0][0];
      localObject = new Rectangle2D.Double(d1, d3, d4, d2);
    }
    if ((localObject == null) && (paramGraphicsState.CTM[0][0] < 0.0F) && (paramGraphicsState.CTM[1][1] > 0.0F))
    {
      d1 = paramGraphicsState.CTM[2][0];
      d2 = paramGraphicsState.CTM[1][1];
      d3 = paramGraphicsState.CTM[2][1];
      d4 = paramGraphicsState.CTM[0][0];
      localObject = new Rectangle2D.Double(d1, d3, d4, d2);
    }
    if (localObject == null)
      localObject = new Rectangle(paramPdfPageData.getMediaBoxX(paramInt), paramPdfPageData.getMediaBoxY(paramInt), paramPdfPageData.getMediaBoxWidth(paramInt), paramPdfPageData.getMediaBoxHeight(paramInt));
    try
    {
      PdfObject localPdfObject2 = localPdfObject1.getDictionary(2087749783);
      GenericColorSpace localGenericColorSpace = ColorspaceFactory.getColorSpaceInstance(paramPdfObjectReader, localPdfObject2, paramMap);
      localGenericColorSpace.setPrinting(paramBoolean);
      PdfPaint localPdfPaint = null;
      int i = paramPdfPageData.getMediaBoxHeight(paramInt);
      localPdfPaint = ShadingFactory.createShading(localPdfObject1, paramBoolean, localGenericColorSpace, paramPdfObjectReader, paramGraphicsState.CTM, i, false, paramGraphicsState.CTM);
      if (localPdfPaint != null)
      {
        paramGraphicsState.setFillType(2);
        paramGraphicsState.setNonstrokeColor(localPdfPaint);
        paramPdfObjectCache.put(1, localGenericColorSpace.getID(), "x");
        paramDynamicVectorRenderer.drawShape((Shape)localObject, paramGraphicsState, 70);
      }
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception: " + localException.getMessage());
    }
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.parser.ShadingCommands
 * JD-Core Version:    0.6.2
 */