package org.jpedal.external;

import java.util.Map;
import org.jpedal.display.GUIModes;
import org.jpedal.objects.Javascript;
import org.jpedal.objects.acroforms.AcroRenderer;
import org.jpedal.objects.acroforms.creation.FormFactory;
import org.jpedal.objects.javascript.ExpressionEngine;
import org.jpedal.parser.PdfStreamDecoder;
import org.jpedal.render.DynamicVectorRenderer;

public class ExternalHandlers
{
  FormFactory userFormFactory = null;
  private AcroRenderer formRenderer;
  public static boolean throwMissingCIDError = false;
  private DynamicVectorRenderer customDVR = null;
  ImageHandler customImageHandler = null;
  private Object customSwingHandle;
  private Object userExpressionEngine;
  private final boolean useXFA = false;
  private Javascript javascript = null;
  RenderChangeListener customRenderChangeListener = null;
  GlyphTracker customGlyphTracker = null;
  ShapeTracker customShapeTracker = null;
  AnnotationHandler annotationHandler;
  private Map jpedalActionHandlers;
  CustomPrintHintingHandler customPrintHintingHandler = null;
  ColorHandler customColorHandler = null;
  private CustomFormPrint customFormPrint;
  private CustomMessageHandler customMessageHandler = null;
  private JPedalActionHandler keyboardHandler = null;
  Object swingGUI = null;
  private boolean isServer = false;
  private Enum modeSelected = GUIModes.SWING;

  public ExternalHandlers()
  {
  }

  public ExternalHandlers(boolean paramBoolean)
  {
    this.isServer = paramBoolean;
  }

  public ExternalHandlers(GUIModes paramGUIModes)
  {
    this.modeSelected = paramGUIModes;
  }

  public void addHandlers(PdfStreamDecoder paramPdfStreamDecoder)
  {
    paramPdfStreamDecoder.setObjectValue(-6, this.customImageHandler);
    paramPdfStreamDecoder.setObjectValue(12, this.customGlyphTracker);
    paramPdfStreamDecoder.setObjectValue(13, this.customGlyphTracker);
  }

  public void addExternalHandler(Object paramObject, int paramInt)
  {
    switch (paramInt)
    {
    case 28:
      break;
    case 4:
      this.customSwingHandle = paramObject;
      break;
    case 6:
      this.userExpressionEngine = paramObject;
      break;
    case 3:
      this.userFormFactory = ((FormFactory)paramObject);
      break;
    case 11:
      this.swingGUI = paramObject;
      break;
    case 1:
      this.customImageHandler = ((ImageHandler)paramObject);
      break;
    case 19:
      this.customColorHandler = ((ColorHandler)paramObject);
      break;
    case 12:
      this.customGlyphTracker = ((GlyphTracker)paramObject);
      break;
    case 13:
      this.customShapeTracker = ((ShapeTracker)paramObject);
      break;
    case 14:
      this.customFormPrint = ((CustomFormPrint)paramObject);
      break;
    case 9:
      this.jpedalActionHandlers = ((Map)paramObject);
      break;
    case 15:
      this.customMessageHandler = ((CustomMessageHandler)paramObject);
      break;
    case 21:
      this.customRenderChangeListener = ((RenderChangeListener)paramObject);
      break;
    case 18:
      this.customPrintHintingHandler = ((CustomPrintHintingHandler)paramObject);
      break;
    case 20:
      this.customDVR = ((DynamicVectorRenderer)paramObject);
      break;
    case 25:
      this.annotationHandler = ((AnnotationHandler)paramObject);
      break;
    case 30:
      if ((paramObject instanceof JPedalActionHandler))
        this.keyboardHandler = ((JPedalActionHandler)paramObject);
      break;
    case 2:
    case 5:
    case 7:
    case 8:
    case 10:
    case 16:
    case 17:
    case 22:
    case 23:
    case 24:
    case 26:
    case 27:
    case 29:
    default:
      throw new IllegalArgumentException("Unknown type=" + paramInt);
    }
  }

  public Object getExternalHandler(int paramInt)
  {
    switch (paramInt)
    {
    case 3:
      return this.formRenderer.getFormFactory();
    case 4:
      return this.customSwingHandle;
    case 6:
      return this.userExpressionEngine;
    case 1:
      return this.customImageHandler;
    case 19:
      return this.customColorHandler;
    case 12:
      return this.customGlyphTracker;
    case 18:
      return this.customPrintHintingHandler;
    case 13:
      return this.customShapeTracker;
    case 14:
      return this.customFormPrint;
    case 11:
      return this.swingGUI;
    case 9:
      return this.jpedalActionHandlers;
    case 15:
      return this.customMessageHandler;
    case 20:
      return this.customDVR;
    case 21:
      return this.customRenderChangeListener;
    case 24:
      return this.jpedalActionHandlers;
    case 25:
      return this.annotationHandler;
    case 30:
      return this.keyboardHandler;
    case 2:
    case 5:
    case 7:
    case 8:
    case 10:
    case 16:
    case 17:
    case 22:
    case 23:
    case 26:
    case 27:
    case 28:
    case 29:
    }
    if (paramInt == 25)
      return null;
    throw new IllegalArgumentException("Unknown type " + paramInt);
  }

  public Javascript getJavaScript()
  {
    return this.javascript;
  }

  public FormFactory getUserFormFactory()
  {
    return this.userFormFactory;
  }

  public void dispose()
  {
    if (this.javascript != null)
      this.javascript.dispose();
    this.javascript = null;
    if (this.formRenderer != null)
      this.formRenderer.dispose();
    this.formRenderer = null;
  }

  public AcroRenderer getFormRenderer()
  {
    return this.formRenderer;
  }

  public void setJavaScript(Javascript paramJavascript)
  {
    this.javascript = paramJavascript;
  }

  public void setFormRenderer(AcroRenderer paramAcroRenderer)
  {
    this.formRenderer = paramAcroRenderer;
  }

  public void openPdfFile(Object paramObject)
  {
    this.formRenderer = new AcroRenderer(false);
    FormFactory localFormFactory = this.userFormFactory;
    if (localFormFactory != null)
      this.formRenderer.setFormFactory(localFormFactory);
    int i = (this.formRenderer.getFormFactory() != null) && ((this.formRenderer.getFormFactory().getType() == 3) || (this.formRenderer.getFormFactory().getType() == 4)) ? 1 : 0;
    if ((i == 0) && (this.isServer) && (i == 0));
    this.javascript = new Javascript((ExpressionEngine)paramObject, this.formRenderer);
  }

  public void setMode(Enum paramEnum)
  {
    this.modeSelected = paramEnum;
  }

  public Enum getMode()
  {
    return this.modeSelected;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.external.ExternalHandlers
 * JD-Core Version:    0.6.2
 */