package org.jpedal.objects.layers;

import java.io.PrintStream;

public class Layer
{
  public static boolean debugLayer = false;
  private PdfLayerList layerList;
  public String name;

  Layer(String paramString, PdfLayerList paramPdfLayerList)
  {
    this.name = paramString;
    this.layerList = paramPdfLayerList;
  }

  public void setAction(String paramString)
  {
    this.layerList.addJScommand(this.name, paramString);
  }

  public boolean getState()
  {
    return this.layerList.isVisible(this.name);
  }

  public void setState(boolean paramBoolean)
  {
    boolean bool = this.layerList.isVisible(this.name);
    this.layerList.setVisiblity(this.name, paramBoolean);
    if (bool != paramBoolean)
    {
      if (debugLayer)
        System.out.println(this.name + ' ' + paramBoolean);
      this.layerList.setChangesMade(true);
    }
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.layers.Layer
 * JD-Core Version:    0.6.2
 */