package org.jpedal.examples.samples;

import org.jpedal.examples.viewer.Viewer;

public class OpenViewer
{
  public OpenViewer()
  {
    Viewer localViewer = new Viewer();
    localViewer.setupViewer();
    Object localObject1 = null;
    Object localObject2 = null;
    if (localObject2 != null)
      localViewer.executeCommand(10, new Object[] { localObject2 });
    if (localObject1 != null)
      localViewer.executeCommand(10, new Object[] { localObject1 });
    if (localObject2 == null)
      localViewer.executeCommand(14, new Object[] { "http://my.site.org/PDF3.pdf" });
  }

  public static void main(String[] paramArrayOfString)
  {
    new OpenViewer();
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.samples.OpenViewer
 * JD-Core Version:    0.6.2
 */