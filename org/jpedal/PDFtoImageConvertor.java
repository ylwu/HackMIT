package org.jpedal;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.List;
import org.jpedal.color.ColorSpaces;
import org.jpedal.display.GUIModes;
import org.jpedal.exception.PdfException;
import org.jpedal.external.ExternalHandlers;
import org.jpedal.io.ObjectStore;
import org.jpedal.io.PdfObjectReader;
import org.jpedal.objects.PdfPageData;
import org.jpedal.objects.PdfResources;
import org.jpedal.objects.acroforms.AcroRenderer;
import org.jpedal.objects.acroforms.GUIData;
import org.jpedal.objects.raw.FormObject;
import org.jpedal.objects.raw.PageObject;
import org.jpedal.objects.raw.PdfObject;
import org.jpedal.parser.DecoderOptions;
import org.jpedal.parser.DecoderResults;
import org.jpedal.parser.PdfStreamDecoder;
import org.jpedal.parser.PdfStreamDecoderForSampling;
import org.jpedal.render.DynamicVectorRenderer;
import org.jpedal.render.ImageDisplay;

public class PDFtoImageConvertor
{
  public static Boolean allowPagesSmallerThanPageSize = Boolean.FALSE;
  public static Integer bestQualityMaxScaling = null;
  private float multiplyer = 1.0F;
  Boolean instance_allowPagesSmallerThanPageSize;
  DecoderOptions options = null;
  Rectangle[] objectAreas = null;
  private Integer instance_bestQualityMaxScaling = null;

  public PDFtoImageConvertor(float paramFloat, DecoderOptions paramDecoderOptions)
  {
    this.multiplyer = paramFloat;
    this.instance_allowPagesSmallerThanPageSize = paramDecoderOptions.getInstance_allowPagesSmallerThanPageSize();
    this.instance_bestQualityMaxScaling = paramDecoderOptions.getInstance_bestQualityMaxScaling();
    this.options = paramDecoderOptions;
  }

  public BufferedImage convert(DecoderResults paramDecoderResults, int paramInt1, PdfResources paramPdfResources, ExternalHandlers paramExternalHandlers, int paramInt2, PdfPageData paramPdfPageData, AcroRenderer paramAcroRenderer, float paramFloat, PdfObjectReader paramPdfObjectReader, int paramInt3, boolean paramBoolean, String paramString)
    throws PdfException
  {
    PageObject localPageObject = new PageObject(paramString);
    paramPdfObjectReader.readObject(localPageObject);
    paramPdfObjectReader.checkParentForResources(localPageObject);
    localPageObject.setPageNumber(paramInt3);
    PdfObject localPdfObject = localPageObject.getDictionary(2004251818);
    ObjectStore localObjectStore = new ObjectStore();
    ImageDisplay localImageDisplay = new ImageDisplay(paramInt3, true, 5000, localObjectStore);
    if ((localImageDisplay.getType() != 4) && (localImageDisplay.getType() != 5) && (localImageDisplay.getType() != 7) && (localImageDisplay.getType() != 6))
    {
      if (this.options.getPageColor() != null)
        localImageDisplay.setValue(1, this.options.getPageColor().getRGB());
      if (this.options.getTextColor() != null)
      {
        localImageDisplay.setValue(2, this.options.getTextColor().getRGB());
        if (this.options.getChangeTextAndLine())
          localImageDisplay.setValue(3, 1);
        else
          localImageDisplay.setValue(3, 0);
        localImageDisplay.setValue(4, this.options.getReplacementColorThreshold());
      }
    }
    PdfStreamDecoder localPdfStreamDecoder = null;
    localPdfStreamDecoder = new PdfStreamDecoder(paramPdfObjectReader);
    localPdfStreamDecoder.setParameters(true, true, paramInt2, 1, paramExternalHandlers.getMode().equals(GUIModes.JAVAFX));
    paramExternalHandlers.addHandlers(localPdfStreamDecoder);
    localPdfStreamDecoder.setObjectValue(-8, localObjectStore);
    localPdfStreamDecoder.setFloatValue(17, this.multiplyer);
    localPdfStreamDecoder.setObjectValue(-18, paramPdfPageData);
    localPdfStreamDecoder.setIntValue(-10, paramInt3);
    localPdfStreamDecoder.setObjectValue(23, localImageDisplay);
    paramPdfResources.setupResources(localPdfStreamDecoder, true, localPdfObject, paramInt3, paramPdfObjectReader);
    if (this.multiplyer == -2.0F)
    {
      this.multiplyer = -1.0F;
      localPdfStreamDecoder.setFloatValue(17, this.multiplyer);
      localObject1 = new PdfStreamDecoderForSampling(paramPdfObjectReader);
      ((PdfStreamDecoderForSampling)localObject1).setParameters(true, true, paramInt2, 0, paramExternalHandlers.getMode().equals(GUIModes.JAVAFX));
      ((PdfStreamDecoderForSampling)localObject1).setObjectValue(-8, localObjectStore);
      ((PdfStreamDecoderForSampling)localObject1).setFloatValue(17, this.multiplyer);
      ((PdfStreamDecoderForSampling)localObject1).setObjectValue(-18, paramPdfPageData);
      ((PdfStreamDecoderForSampling)localObject1).setIntValue(-10, paramInt3);
      ((PdfStreamDecoderForSampling)localObject1).setObjectValue(23, localImageDisplay);
      paramPdfResources.setupResources((PdfStreamDecoder)localObject1, true, localPdfObject, paramInt3, paramPdfObjectReader);
      this.multiplyer = ((PdfStreamDecoderForSampling)localObject1).decodePageContentForImageSampling(localPageObject);
      i = 0;
      if (this.instance_bestQualityMaxScaling != null)
        i = this.instance_bestQualityMaxScaling.intValue();
      else if (bestQualityMaxScaling != null)
        i = bestQualityMaxScaling.intValue();
      if ((i > 0) && (this.multiplyer > i))
        this.multiplyer = i;
      ((PdfStreamDecoderForSampling)localObject1).setFloatValue(17, this.multiplyer);
      localPdfStreamDecoder.setFloatValue(17, this.multiplyer);
    }
    if ((!allowPagesSmallerThanPageSize.booleanValue()) && (!this.instance_allowPagesSmallerThanPageSize.booleanValue()) && (this.multiplyer < 1.0F) && (this.multiplyer > 0.0F))
      this.multiplyer = 1.0F;
    if (this.multiplyer == -1.0F)
      this.multiplyer = 1.0F;
    Object localObject1 = setPageParametersForImage(paramFloat * this.multiplyer, paramInt3, paramPdfPageData);
    int i = (int)(paramFloat * paramPdfPageData.getMediaBoxHeight(paramInt3));
    int j = paramPdfPageData.getRotation(paramInt3);
    float f1 = paramFloat * paramPdfPageData.getCropBoxWidth2D(paramInt3);
    float f2 = paramFloat * paramPdfPageData.getCropBoxHeight2D(paramInt3);
    float f3 = paramFloat * paramPdfPageData.getCropBoxX(paramInt3);
    float f4 = paramFloat * paramPdfPageData.getCropBoxY(paramInt3);
    int k = 0;
    int n;
    int m;
    if ((j == 90) || (j == 270))
    {
      n = (int)(f1 * this.multiplyer);
      m = (int)(f2 * this.multiplyer);
      k = 1;
    }
    else
    {
      m = (int)(f1 * this.multiplyer);
      n = (int)(f2 * this.multiplyer);
    }
    BufferedImage localBufferedImage = new BufferedImage(m, n, 2);
    Graphics localGraphics = localBufferedImage.getGraphics();
    Graphics2D localGraphics2D = (Graphics2D)localGraphics;
    if (!paramBoolean)
    {
      localGraphics2D.setColor(Color.white);
      localGraphics2D.fillRect(0, 0, m, n);
    }
    if (j == 180)
      localGraphics2D.translate(f3 * 2.0F * this.multiplyer, -(f4 * 2.0F * this.multiplyer));
    ((DynamicVectorRenderer)localPdfStreamDecoder.getObjectValue(23)).setScalingValues(f3 * this.multiplyer, f2 * this.multiplyer + f4, this.multiplyer * paramFloat);
    localGraphics2D.setRenderingHints(ColorSpaces.hints);
    localGraphics2D.transform((AffineTransform)localObject1);
    if (k != 0)
      if (j == 90)
      {
        if (this.multiplyer < 1.0F)
        {
          f4 = (int)(((AffineTransform)localObject1).getTranslateX() + f4);
          f3 = (int)(((AffineTransform)localObject1).getTranslateY() + f3);
        }
        else
        {
          f4 = (int)(((AffineTransform)localObject1).getTranslateX() / this.multiplyer + f4);
          f3 = (int)(((AffineTransform)localObject1).getTranslateY() / this.multiplyer + f3);
        }
        localGraphics2D.translate(-f3, -f4);
      }
      else if (f4 < 0.0F)
      {
        localGraphics2D.translate(-f3, i - f2 + f4);
      }
      else
      {
        localGraphics2D.translate(-f3, i - f2 - f4);
      }
    localPdfStreamDecoder.setObjectValue(-7, localGraphics2D);
    localImageDisplay.setG2(localGraphics2D);
    localPdfStreamDecoder.decodePageContent(localPageObject);
    localGraphics2D.setClip(null);
    this.objectAreas = localImageDisplay.getAreas();
    paramDecoderResults.update(localPdfStreamDecoder, false);
    if ((paramAcroRenderer != null) && (paramAcroRenderer.hasFormsOnPage(paramInt3)) && (!paramAcroRenderer.ignoreForms()))
    {
      paramDecoderResults.resetColorSpaces();
      paramAcroRenderer.createDisplayComponentsForPage(paramInt3, localPdfStreamDecoder);
      if (!paramAcroRenderer.getCompData().formsRasterizedForDisplay())
      {
        List[] arrayOfList = paramAcroRenderer.getCompData().getFormList(true);
        Iterator localIterator = arrayOfList[paramInt3].iterator();
        while (localIterator.hasNext())
        {
          Object localObject2 = localIterator.next();
          if (localObject2 != null)
            localPdfStreamDecoder.drawFlattenedForm((FormObject)localObject2, true, (PdfObject)paramAcroRenderer.getFormResources()[0]);
        }
        paramAcroRenderer.getCompData().renderFormsOntoG2(localGraphics2D, paramInt3, 0, paramInt1, null, null, paramPdfPageData.getMediaBoxHeight(paramInt3));
      }
    }
    if (localPdfStreamDecoder != null)
      localPdfStreamDecoder.dispose();
    localObjectStore.flush();
    return localBufferedImage;
  }

  public float getMultiplyer()
  {
    return this.multiplyer;
  }

  public Rectangle[] getAreas()
  {
    return this.objectAreas;
  }

  private static AffineTransform setPageParametersForImage(float paramFloat, int paramInt, PdfPageData paramPdfPageData)
  {
    AffineTransform localAffineTransform = new AffineTransform();
    int i = paramPdfPageData.getCropBoxWidth(paramInt);
    int j = paramPdfPageData.getCropBoxHeight(paramInt);
    int k = paramPdfPageData.getCropBoxX(paramInt);
    int m = paramPdfPageData.getCropBoxY(paramInt);
    int n = (int)(i * paramFloat);
    int i1 = (int)(j * paramFloat);
    int i2 = paramPdfPageData.getRotation(paramInt);
    localAffineTransform.translate(-k * paramFloat, m * paramFloat);
    double d1;
    double d2;
    if (i2 == 270)
    {
      localAffineTransform.rotate(-1.570796326794897D, n / 2, i1 / 2);
      d1 = localAffineTransform.getTranslateX();
      d2 = localAffineTransform.getTranslateY();
      localAffineTransform.translate(i1 - d2, -d1);
      localAffineTransform.translate(2 * m * paramFloat, 0.0D);
      localAffineTransform.translate(0.0D, -paramFloat * (paramPdfPageData.getCropBoxHeight(paramInt) - paramPdfPageData.getMediaBoxHeight(paramInt)));
    }
    else if (i2 == 180)
    {
      localAffineTransform.rotate(3.141592653589793D, n / 2, i1 / 2);
    }
    else if (i2 == 90)
    {
      localAffineTransform.rotate(1.570796326794897D, n / 2, i1 / 2);
      d1 = localAffineTransform.getTranslateX();
      d2 = localAffineTransform.getTranslateY();
      localAffineTransform.translate(-d2, n - d1);
    }
    if (paramFloat < 1.0F)
    {
      localAffineTransform.translate(n, i1);
      localAffineTransform.scale(1.0D, -1.0D);
      localAffineTransform.translate(-n, 0.0D);
      localAffineTransform.scale(paramFloat, paramFloat);
    }
    else
    {
      localAffineTransform.translate(n, i1);
      localAffineTransform.scale(1.0D, -1.0D);
      localAffineTransform.translate(-n, 0.0D);
      localAffineTransform.scale(paramFloat, paramFloat);
    }
    return localAffineTransform;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.PDFtoImageConvertor
 * JD-Core Version:    0.6.2
 */