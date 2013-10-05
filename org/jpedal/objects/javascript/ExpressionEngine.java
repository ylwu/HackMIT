package org.jpedal.objects.javascript;

import org.jpedal.objects.acroforms.AcroRenderer;
import org.jpedal.objects.raw.FormObject;

public abstract interface ExpressionEngine
{
  public abstract int execute(FormObject paramFormObject, int paramInt1, Object paramObject, int paramInt2, char paramChar);

  public abstract void closeFile();

  public abstract boolean reportError(int paramInt, Object[] paramArrayOfObject);

  public abstract int addCode(String paramString);

  public abstract void executeFunctions(String paramString, FormObject paramFormObject);

  public abstract void dispose();

  public abstract void setAcroRenderer(AcroRenderer paramAcroRenderer);
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.javascript.ExpressionEngine
 * JD-Core Version:    0.6.2
 */