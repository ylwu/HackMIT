package org.jpedal.examples.handlers;

import java.io.PrintStream;
import org.jpedal.external.CustomMessageHandler;

public class ExampleCustomMessageHandler
  implements CustomMessageHandler
{
  boolean showMessage = false;

  public boolean showMessage(Object paramObject)
  {
    if (this.showMessage)
      if ((paramObject instanceof String))
        System.out.println("Message=" + paramObject);
      else
        System.out.println("Object is a component =" + paramObject);
    return false;
  }

  public String requestInput(Object[] paramArrayOfObject)
  {
    if (this.showMessage)
      System.out.println("input requested - parameters passed in (String or components");
    return null;
  }

  public int requestConfirm(Object[] paramArrayOfObject)
  {
    if (this.showMessage)
      System.out.println("input requested - parameters passed in (String or components");
    return 0;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.handlers.ExampleCustomMessageHandler
 * JD-Core Version:    0.6.2
 */