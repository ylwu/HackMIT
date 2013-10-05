package org.jpedal.objects;

import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.scene.shape.Shape;

public class FXClip
  implements PdfClip
{
  private static final boolean debugClip = false;
  private Shape current_clipping_shape = null;

  public boolean updateClip(Object paramObject)
  {
    if (!(paramObject instanceof Shape))
      return false;
    Path localPath = (Path)paramObject;
    boolean bool = false;
    if (localPath != null)
      localPath.setFill(Color.WHITE);
    if ((this.current_clipping_shape == null) || (localPath == null))
    {
      this.current_clipping_shape = localPath;
      bool = true;
    }
    else
    {
      this.current_clipping_shape = Shape.intersect(this.current_clipping_shape, localPath);
      bool = true;
    }
    return bool;
  }

  public Object getClippingShape()
  {
    return this.current_clipping_shape;
  }

  public boolean setClippingShape(Object paramObject)
  {
    if (!(paramObject instanceof Shape))
      return false;
    this.current_clipping_shape = ((Shape)paramObject);
    return true;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.FXClip
 * JD-Core Version:    0.6.2
 */