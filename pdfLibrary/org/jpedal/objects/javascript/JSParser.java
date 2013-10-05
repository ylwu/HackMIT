package org.jpedal.objects.javascript;

import org.jpedal.objects.acroforms.AcroRenderer;
import org.jpedal.objects.raw.FormObject;

public abstract interface JSParser
{
  public abstract void execute(String paramString, FormObject paramFormObject, AcroRenderer paramAcroRenderer);

  public abstract void flush();

  public abstract void addCode(String paramString);

  public abstract void setJavaScriptEnded();

  public abstract void executeJS(String paramString, FormObject paramFormObject, AcroRenderer paramAcroRenderer);

  public abstract void flushJS();

  public abstract Object generateJStype(String paramString, boolean paramBoolean);
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.javascript.JSParser
 * JD-Core Version:    0.6.2
 */