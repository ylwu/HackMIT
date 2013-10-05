package org.jpedal.objects;

import org.jpedal.io.ObjectStore;
import org.jpedal.utils.repositories.Vector_Float;
import org.jpedal.utils.repositories.Vector_Int;
import org.jpedal.utils.repositories.Vector_String;

public class PdfImageData
{
  private Vector_Int object_page_id = new Vector_Int(100);
  private Vector_Float x = new Vector_Float(100);
  private Vector_Float y = new Vector_Float(100);
  private Vector_Float w = new Vector_Float(100);
  private Vector_Float h = new Vector_Float(100);
  private Vector_String object_image_name = new Vector_String(100);
  private int current_item = 0;

  public final void setImageInfo(String paramString, int paramInt, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    paramString = ObjectStore.removeIllegalFileNameCharacters(paramString);
    this.object_page_id.addElement(paramInt);
    this.object_image_name.addElement(paramString);
    this.x.addElement(paramFloat1);
    this.y.addElement(paramFloat2);
    this.h.addElement(paramFloat4);
    this.w.addElement(paramFloat3);
    this.current_item += 1;
  }

  public final float getImageYCoord(int paramInt)
  {
    return this.y.elementAt(paramInt);
  }

  public final float getImageWidth(int paramInt)
  {
    return this.w.elementAt(paramInt);
  }

  public final float getImageHeight(int paramInt)
  {
    return this.h.elementAt(paramInt);
  }

  public final int getImagePageID(int paramInt)
  {
    return this.object_page_id.elementAt(paramInt);
  }

  public final String getImageName(int paramInt)
  {
    return this.object_image_name.elementAt(paramInt);
  }

  public final void clearImageData()
  {
    this.object_image_name.clear();
    this.object_page_id.clear();
    this.x.clear();
    this.y.clear();
    this.w.clear();
    this.h.clear();
    this.current_item = 0;
  }

  public final float getImageXCoord(int paramInt)
  {
    return this.x.elementAt(paramInt);
  }

  public final int getImageCount()
  {
    return this.current_item;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.PdfImageData
 * JD-Core Version:    0.6.2
 */