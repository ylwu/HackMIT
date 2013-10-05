package org.jpedal.parser;

import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D.Float;
import java.util.HashMap;
import java.util.Map;
import org.jpedal.objects.GraphicsState;
import org.jpedal.objects.layers.PdfLayerList;
import org.jpedal.objects.raw.PdfObject;
import org.jpedal.render.DynamicVectorRenderer;

public class LayerDecoder
{
  private boolean isLayerVisible = true;
  int layerLevel = 0;
  private Map layerVisibility = new HashMap(50);
  private Map layerClips = new HashMap(50);
  PdfLayerList layers;

  public boolean isLayerVisible()
  {
    return this.isLayerVisible;
  }

  public void BMC()
  {
    this.layerLevel += 1;
  }

  public void BDC(PdfObject paramPdfObject, GraphicsState paramGraphicsState, DynamicVectorRenderer paramDynamicVectorRenderer, int paramInt1, byte[] paramArrayOfByte, boolean paramBoolean, int paramInt2)
  {
    this.layerLevel += 1;
    if ((this.layers != null) && (this.isLayerVisible))
    {
      String str = "";
      if (paramBoolean)
      {
        str = paramPdfObject.getName(7955);
        if (str == null)
        {
          localObject = paramPdfObject.getDictionary(826881374);
          if (localObject != null)
            str = ((PdfObject)localObject).getTextStreamValue(960773209);
        }
        this.layerClips.put(Integer.valueOf(this.layerLevel), null);
        Object localObject = paramPdfObject.getFloatArray(303185736);
        if (localObject != null)
        {
          Area localArea1 = paramGraphicsState.getClippingShape();
          if (localArea1 != null)
            this.layerClips.put(Integer.valueOf(this.layerLevel), localArea1.clone());
          Area localArea2 = new Area(new Rectangle2D.Float(localObject[0], localObject[1], -paramGraphicsState.CTM[2][0] + (localObject[2] - localObject[0]), -paramGraphicsState.CTM[2][1] + (localObject[3] - localObject[1])));
          if ((localArea2.getBounds().getWidth() > 0.0D) && (localArea2.getBounds().getHeight() > 0.0D))
          {
            paramGraphicsState.setClippingShape(localArea2);
            paramDynamicVectorRenderer.drawClip(paramGraphicsState, localArea2, true);
          }
        }
      }
      else
      {
        str = readOPName(paramInt1, paramArrayOfByte, paramInt2, str);
      }
      if ((str != null) && (!str.isEmpty()))
        this.isLayerVisible = this.layers.decodeLayer(str, true);
      if (this.isLayerVisible)
        this.layerVisibility.put(Integer.valueOf(this.layerLevel), "x");
    }
  }

  private static String readOPName(int paramInt1, byte[] paramArrayOfByte, int paramInt2, String paramString)
  {
    for (int i = paramInt2; i < paramInt1; i++)
      if ((paramArrayOfByte[i] == 47) && (paramArrayOfByte[(i + 1)] == 79) && (paramArrayOfByte[(i + 2)] == 67))
      {
        i += 2;
        while (paramArrayOfByte[i] != 47)
          i++;
        i++;
        int j = i;
        int k = 0;
        while (i < paramInt1)
        {
          i++;
          k++;
          if ((paramArrayOfByte[i] != 13) && (paramArrayOfByte[i] != 10) && (paramArrayOfByte[i] != 32))
            if (paramArrayOfByte[i] == 47)
              break;
        }
        paramString = new String(paramArrayOfByte, j, k);
      }
    return paramString;
  }

  public void EMC(DynamicVectorRenderer paramDynamicVectorRenderer, GraphicsState paramGraphicsState)
  {
    Integer localInteger = Integer.valueOf(this.layerLevel);
    if (this.layerClips.containsKey(localInteger))
    {
      Area localArea = (Area)this.layerClips.get(localInteger);
      paramGraphicsState.setClippingShape(localArea);
      paramDynamicVectorRenderer.drawClip(paramGraphicsState, localArea, true);
    }
    this.layerLevel -= 1;
    this.isLayerVisible = ((this.layers == null) || (this.layerLevel == 0) || (this.layerVisibility.containsKey(Integer.valueOf(this.layerLevel))));
  }

  public void setPdfLayerList(PdfLayerList paramPdfLayerList)
  {
    this.layers = paramPdfLayerList;
  }

  public Object getPdfLayerList()
  {
    return this.layers;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.parser.LayerDecoder
 * JD-Core Version:    0.6.2
 */