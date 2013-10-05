package org.jpedal.examples.viewer.gui;

import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import org.jpedal.PdfDecoder;
import org.jpedal.examples.viewer.Values;
import org.jpedal.examples.viewer.Viewer;
import org.jpedal.examples.viewer.gui.generic.GUIButton;
import org.jpedal.examples.viewer.gui.generic.GUICombo;
import org.jpedal.examples.viewer.gui.generic.GUIOutline;
import org.jpedal.examples.viewer.gui.generic.GUIThumbnailPanel;
import org.jpedal.examples.viewer.gui.swing.SwingOutline;
import org.jpedal.examples.viewer.utils.PropertiesFile;
import org.jpedal.external.AnnotationHandler;
import org.jpedal.utils.DPIFactory;
import org.jpedal.utils.Messages;

public class GUI
{
  public GUIButton first;
  public GUIButton fback;
  public GUIButton back;
  public GUIButton forward;
  public GUIButton fforward;
  public GUIButton end;
  public GUIButton singleButton;
  public GUIButton continuousButton;
  public GUIButton continuousFacingButton;
  public GUIButton facingButton;
  public GUIButton pageFlowButton;
  protected boolean hiResPrinting = false;
  protected Map objs;
  protected boolean bookmarksGenerated = false;
  protected PdfDecoder decode_pdf;
  protected static final int minimumScreenWidth = 700;
  protected HashMap pagesDecoded = new HashMap();
  public GUIButton snapshotButton;
  public GUIButton buyButton;
  public GUIButton helpButton;
  public GUIButton rssButton;
  public int cropX;
  public int cropY;
  public int cropW;
  public int cropH;
  public int mediaW;
  public int mediaH;
  protected GUIOutline tree = new SwingOutline();
  protected boolean allowScrolling = true;
  protected boolean confirmClose = false;
  protected float[] scalingFloatValues = { 1.0F, 1.0F, 1.0F, 0.25F, 0.5F, 0.75F, 1.0F, 1.25F, 1.5F, 2.0F, 2.5F, 5.0F, 7.5F, 10.0F };
  protected float scaling = 1.0F;
  protected static int inset = 25;
  protected int rotation = 0;
  protected String[] rotationValues = { "0", "90", "180", "270" };
  protected GUICombo rotationBox;
  protected GUICombo qualityBox;
  protected GUICombo scalingBox;
  protected static int defaultSelection = 0;
  protected String titleMessage = null;
  protected Values commonValues;
  protected GUIThumbnailPanel thumbnails;
  protected PropertiesFile properties;

  public boolean useHiResPrinting()
  {
    return this.hiResPrinting;
  }

  public void setHiResPrinting(boolean paramBoolean)
  {
    this.hiResPrinting = paramBoolean;
  }

  public String getPropertiesFileLocation()
  {
    return this.properties.getConfigFile();
  }

  public void setPropertiesFileLocation(String paramString)
  {
    this.properties.loadProperties(paramString);
  }

  /** @deprecated */
  public void setProperties(String paramString, boolean paramBoolean)
  {
    this.properties.setValue(paramString, String.valueOf(paramBoolean));
  }

  public void setPreferences(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean1, int paramInt4, boolean paramBoolean2, int paramInt5, boolean paramBoolean3)
  {
    org.jpedal.display.swing.SingleDisplay.CURRENT_BORDER_STYLE = paramInt3;
    this.properties.setValue("borderType", String.valueOf(paramInt3));
    this.allowScrolling = paramBoolean1;
    this.properties.setValue("autoScroll", String.valueOf(paramBoolean1));
    this.decode_pdf.getDPIFactory().setDpi(paramInt1);
    this.properties.setValue("resolution", String.valueOf(paramInt1));
    if ((paramInt4 < 1) || (paramInt4 > 5))
      paramInt4 = 1;
    this.decode_pdf.setPageMode(paramInt4);
    this.properties.setValue("startView", String.valueOf(paramInt4));
    this.decode_pdf.repaint();
    String str = this.properties.getValue("searchWindowType");
    if ((!str.isEmpty()) && (!str.equals(String.valueOf(paramInt2))) && (Viewer.showMessages))
      JOptionPane.showMessageDialog(null, Messages.getMessage("PageLayoutViewMenu.ResetSearch"));
    this.properties.setValue("searchWindowType", String.valueOf(paramInt2));
    this.properties.setValue("automaticupdate", String.valueOf(paramBoolean2));
    this.commonValues.setMaxMiltiViewers(paramInt5);
    this.properties.setValue("maxmultiviewers", String.valueOf(paramInt5));
    this.hiResPrinting = paramBoolean3;
    this.properties.setValue("useHiResPrinting", String.valueOf(paramBoolean3));
  }

  public boolean allowScrolling()
  {
    return this.allowScrolling;
  }

  public boolean confirmClose()
  {
    return this.confirmClose;
  }

  public void setAutoScrolling(boolean paramBoolean)
  {
    this.allowScrolling = paramBoolean;
  }

  public void toogleAutoScrolling()
  {
    this.allowScrolling = (!this.allowScrolling);
  }

  public int getRotation()
  {
    return this.rotation;
  }

  public float getScaling()
  {
    return this.scaling;
  }

  public void setScaling(float paramFloat)
  {
    this.scaling = paramFloat;
    this.scalingBox.setSelectedIndex((int)this.scaling);
  }

  public static int getPDFDisplayInset()
  {
    return inset;
  }

  public void createUniqueAnnotationIcons()
  {
    if (this.objs == null)
      this.objs = new HashMap();
    else
      this.objs.clear();
    ((AnnotationHandler)this.decode_pdf.getExternalHandler(25)).handleAnnotations(this.decode_pdf, this.objs, this.commonValues.getCurrentPage());
  }

  public void setDpi(int paramInt)
  {
    this.decode_pdf.getDPIFactory().setDpi(paramInt);
  }

  public PropertiesFile getProperties()
  {
    return this.properties;
  }

  public void dispose()
  {
    this.first = null;
    this.fback = null;
    this.back = null;
    this.forward = null;
    this.fforward = null;
    this.end = null;
    this.singleButton = null;
    this.continuousButton = null;
    this.continuousFacingButton = null;
    this.facingButton = null;
    this.pageFlowButton = null;
    this.pagesDecoded = null;
    this.snapshotButton = null;
    this.buyButton = null;
    this.helpButton = null;
    this.rssButton = null;
    this.tree = null;
    this.pagesDecoded = null;
    this.snapshotButton = null;
    this.scalingFloatValues = null;
    this.rotationValues = null;
    this.rotationBox = null;
    this.qualityBox = null;
    this.scalingBox = null;
    this.titleMessage = null;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.gui.GUI
 * JD-Core Version:    0.6.2
 */