package org.jpedal.parser;

import java.awt.Shape;
import org.jpedal.external.GlyphTracker;
import org.jpedal.external.ShapeTracker;
import org.jpedal.io.ErrorTracker;
import org.jpedal.io.PdfObjectReader;
import org.jpedal.objects.GraphicsState;
import org.jpedal.objects.PdfPageData;
import org.jpedal.objects.structuredtext.StructuredContentHandler;
import org.jpedal.render.DynamicVectorRenderer;

public class BaseDecoder
{
  public static final int SamplingUsed = 12;
  public static final int ImageCount = 14;
  public static final int ImagesProcessedFully = 15;
  public static final int HasYCCKimages = 16;
  public static final int Multiplier = 17;
  public static final int TokenNumber = 18;
  public static final int FormLevel = 19;
  public static final int RenderDirectly = 19;
  public static final int TextPrint = 20;
  public static final int GenerateGlyphOnRender = 21;
  public static final int PageNumber = 22;
  public static final int IsFlattenedForm = 24;
  public static final int IsPrinting = 25;
  public static final int IsImage = 26;
  int imageStatus = 0;
  public static final int IMAGE_getImageFromPdfObject = 1;
  public static final int SCREEN_getImageFromPdfObject = 2;
  CommandParser parser;
  PdfObjectCache cache;
  boolean generateGlyphOnRender = false;
  boolean isPageContent;
  boolean renderPage;
  int renderMode;
  int extractionMode;
  ShapeTracker customShapeTracker = null;
  DynamicVectorRenderer current;
  LayerDecoder layerDecoder = null;
  int textPrint = 0;
  PdfObjectReader currentPdfFile;
  GraphicsState gs = new GraphicsState(false);
  float multiplyer = 1.0F;
  boolean renderDirectly;
  int formLevel;
  float samplingUsed = -1.0F;
  int tokenNumber;
  String fileName = "";
  Shape defaultClip;
  boolean imagesProcessedFully;
  boolean hasYCCKimages = false;
  int streamType = 0;
  int imageCount = 0;
  public ErrorTracker errorTracker;
  int pageNum;
  PdfPageData pageData;
  GlyphTracker customGlyphTracker;
  StructuredContentHandler contentHandler;
  boolean useJavaFX;

  public void setHandlerValue(int paramInt, Object paramObject)
  {
    switch (paramInt)
    {
    case 12:
      this.customGlyphTracker = ((GlyphTracker)paramObject);
      break;
    case 22:
      this.contentHandler = ((StructuredContentHandler)paramObject);
      break;
    case 23:
      this.errorTracker = ((ErrorTracker)paramObject);
    }
  }

  public void setRes(PdfObjectCache paramPdfObjectCache)
  {
    this.cache = paramPdfObjectCache;
  }

  public void setParameters(boolean paramBoolean1, boolean paramBoolean2, int paramInt1, int paramInt2, boolean paramBoolean3)
  {
    this.isPageContent = paramBoolean1;
    this.renderPage = paramBoolean2;
    this.renderMode = paramInt1;
    this.extractionMode = paramInt2;
    this.useJavaFX = paramBoolean3;
  }

  public void setFloatValue(int paramInt, float paramFloat)
  {
    switch (paramInt)
    {
    case 17:
      this.multiplyer = paramFloat;
      break;
    case 12:
      this.samplingUsed = paramFloat;
    }
  }

  public void setCommands(CommandParser paramCommandParser)
  {
    this.parser = paramCommandParser;
  }

  public int getIntValue(int paramInt)
  {
    int i = 0;
    switch (paramInt)
    {
    case 14:
      i = this.imageCount;
    }
    return i;
  }

  public boolean getBooleanValue(int paramInt)
  {
    boolean bool = false;
    switch (paramInt)
    {
    case 15:
      bool = this.imagesProcessedFully;
      break;
    case 16:
      bool = this.hasYCCKimages;
    }
    return bool;
  }

  public float getFloatValue(int paramInt)
  {
    float f = 0.0F;
    switch (paramInt)
    {
    case 12:
      f = this.samplingUsed;
    }
    return f;
  }

  public void setBooleanValue(int paramInt, boolean paramBoolean)
  {
    switch (paramInt)
    {
    case 19:
      this.renderDirectly = paramBoolean;
    }
  }

  public void setIntValue(int paramInt1, int paramInt2)
  {
    switch (paramInt1)
    {
    case 26:
      this.imageStatus = paramInt2;
      break;
    case 19:
      this.formLevel = paramInt2;
      break;
    case 14:
      this.imageCount = paramInt2;
      break;
    case 22:
      this.pageNum = paramInt2;
      break;
    case -12:
      this.streamType = paramInt2;
      break;
    case 18:
      this.tokenNumber = paramInt2;
    }
  }

  public void setName(String paramString)
  {
    if (paramString != null)
    {
      this.fileName = paramString.toLowerCase();
      int i = this.fileName.lastIndexOf('/');
      if (i != -1)
        this.fileName = this.fileName.substring(i + 1);
      i = this.fileName.lastIndexOf('\\');
      if (i != -1)
        this.fileName = this.fileName.substring(i + 1);
      i = this.fileName.lastIndexOf('.');
      if (i != -1)
        this.fileName = this.fileName.substring(0, i);
    }
  }

  public void setGS(GraphicsState paramGraphicsState)
  {
    this.gs = paramGraphicsState;
  }

  public void setFileHandler(PdfObjectReader paramPdfObjectReader)
  {
    this.currentPdfFile = paramPdfObjectReader;
  }

  public void setRenderer(DynamicVectorRenderer paramDynamicVectorRenderer)
  {
    this.current = paramDynamicVectorRenderer;
  }

  public Object getObjectValue(int paramInt)
  {
    return null;
  }

  public void setObjectValue(int paramInt, Object paramObject)
  {
    switch (paramInt)
    {
    case 13:
      this.customShapeTracker = ((ShapeTracker)paramObject);
    }
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.parser.BaseDecoder
 * JD-Core Version:    0.6.2
 */