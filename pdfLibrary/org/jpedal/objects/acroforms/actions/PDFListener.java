package org.jpedal.objects.acroforms.actions;

import org.jpedal.objects.raw.FormObject;

public class PDFListener
{
  public static final boolean debugMouseActions = false;
  public FormObject formObject;
  public ActionHandler handler;

  protected PDFListener(FormObject paramFormObject, ActionHandler paramActionHandler)
  {
    this.formObject = paramFormObject;
    this.handler = paramActionHandler;
  }

  public void mouseReleased(Object paramObject)
  {
    if (this.handler != null)
    {
      this.handler.A(paramObject, this.formObject, 2);
      this.handler.U(paramObject, this.formObject);
    }
  }

  public void mouseClicked(Object paramObject)
  {
    if (this.handler != null)
      this.handler.A(paramObject, this.formObject, 3);
  }

  public void mousePressed(Object paramObject)
  {
    if (this.handler != null)
    {
      this.handler.A(paramObject, this.formObject, 1);
      this.handler.D(paramObject, this.formObject);
    }
  }

  public void keyReleased(Object paramObject)
  {
    if (this.handler != null)
    {
      this.handler.K(paramObject, this.formObject, 2);
      this.handler.V(paramObject, this.formObject, 2);
    }
  }

  public void focusLost(Object paramObject)
  {
    if (this.handler != null)
    {
      this.handler.Bl(paramObject, this.formObject);
      this.handler.K(paramObject, this.formObject, 6);
      this.handler.V(paramObject, this.formObject, 6);
      this.handler.F(this.formObject);
    }
  }

  public void focusGained(Object paramObject)
  {
    if (this.handler != null)
      this.handler.Fo(paramObject, this.formObject);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.acroforms.actions.PDFListener
 * JD-Core Version:    0.6.2
 */