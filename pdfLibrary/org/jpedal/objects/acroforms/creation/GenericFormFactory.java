package org.jpedal.objects.acroforms.creation;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import org.jpedal.io.PdfObjectReader;
import org.jpedal.objects.Javascript;
import org.jpedal.objects.PdfPageData;
import org.jpedal.objects.acroforms.actions.ActionHandler;
import org.jpedal.objects.raw.FormObject;
import org.jpedal.objects.raw.FormStream;
import org.jpedal.objects.raw.PdfObject;
import org.jpedal.render.DynamicVectorRenderer;

public class GenericFormFactory
{
  public Map groups = new HashMap();
  public Map firstButtons = new HashMap();
  public PdfObject AcroRes;
  public Object[] CO;
  public PdfPageData pageData;
  public PdfObjectReader currentPdfFile;
  protected ActionHandler formsActionHandler;

  public void setDVR(DynamicVectorRenderer paramDynamicVectorRenderer, Javascript paramJavascript)
  {
  }

  public void reset(Object[] paramArrayOfObject, ActionHandler paramActionHandler, PdfPageData paramPdfPageData, PdfObjectReader paramPdfObjectReader)
  {
    this.AcroRes = ((PdfObject)paramArrayOfObject[0]);
    this.CO = ((Object[])paramArrayOfObject[1]);
    this.formsActionHandler = paramActionHandler;
    this.pageData = paramPdfPageData;
    this.currentPdfFile = paramPdfObjectReader;
    this.groups.clear();
    this.firstButtons.clear();
  }

  public void syncValues(SwingFormFactory paramSwingFormFactory)
  {
    paramSwingFormFactory.AcroRes = this.AcroRes;
    paramSwingFormFactory.CO = this.CO;
    paramSwingFormFactory.formsActionHandler = this.formsActionHandler;
    paramSwingFormFactory.pageData = this.pageData;
    paramSwingFormFactory.currentPdfFile = this.currentPdfFile;
  }

  protected String readAPimagesForText(FormObject paramFormObject)
  {
    PdfObject localPdfObject = paramFormObject.getDictionary(4384).getDictionary(30);
    if (localPdfObject != null)
    {
      String str = FormStream.decipherTextFromAP(this.currentPdfFile, localPdfObject);
      return str;
    }
    return null;
  }

  protected BufferedImage createPressedLook(Image paramImage)
  {
    if (paramImage == null)
      return null;
    BufferedImage localBufferedImage = new BufferedImage(paramImage.getWidth(null) + 2, paramImage.getHeight(null) + 2, 2);
    Graphics2D localGraphics2D = (Graphics2D)localBufferedImage.getGraphics();
    localGraphics2D.drawImage(paramImage, 1, 1, null);
    localGraphics2D.dispose();
    return localBufferedImage;
  }

  public void indexAllKids()
  {
  }

  public Object getPopupComponent(FormObject paramFormObject, PdfObject paramPdfObject, int paramInt)
  {
    return null;
  }

  public void setOptions(EnumSet paramEnumSet)
  {
    throw new RuntimeException("setOptions(EnumSet formSettings) called in GenericFormFactory - not implemented in " + this);
  }

  public static boolean isTextForm(int paramInt)
  {
    return (paramInt == FormFactory.SINGLELINEPASSWORD.intValue()) || (paramInt == FormFactory.MULTILINEPASSWORD.intValue()) || (paramInt == FormFactory.SINGLELINETEXT.intValue()) || (paramInt == FormFactory.MULTILINETEXT.intValue());
  }

  public static boolean isButtonForm(int paramInt)
  {
    return (paramInt == FormFactory.RADIOBUTTON.intValue()) || (paramInt == FormFactory.CHECKBOXBUTTON.intValue());
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.acroforms.creation.GenericFormFactory
 * JD-Core Version:    0.6.2
 */