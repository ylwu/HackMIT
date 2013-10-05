package org.jpedal.examples.viewer.gui;

import java.awt.datatransfer.Transferable;
import java.io.File;
import java.util.List;
import javax.swing.JComponent;
import org.jpedal.examples.viewer.Commands;
import org.jpedal.examples.viewer.Values;
import org.jpedal.exception.PdfException;

public class SingleViewTransferHandler extends BaseTransferHandler
{
  public SingleViewTransferHandler(Values paramValues, SwingGUI paramSwingGUI, Commands paramCommands)
  {
    super(paramValues, paramSwingGUI, paramCommands);
  }

  public boolean importData(JComponent paramJComponent, Transferable paramTransferable)
  {
    try
    {
      Object localObject1 = getImport(paramTransferable);
      Object localObject2;
      if ((localObject1 instanceof String))
      {
        localObject2 = (String)localObject1;
        if (((String)localObject2).indexOf("file:/") != ((String)localObject2).lastIndexOf("file:/"))
          this.currentGUI.showMessageDialog("You may only import 1 file at a time");
        else
          openFile((String)localObject2);
      }
      else if ((localObject1 instanceof List))
      {
        localObject2 = (List)localObject1;
        if (((List)localObject2).size() == 1)
        {
          File localFile = (File)((List)localObject2).get(0);
          openFile(localFile.getAbsolutePath());
        }
        else
        {
          this.currentGUI.showMessageDialog("You may only import 1 file at a time");
        }
      }
    }
    catch (Exception localException)
    {
      return false;
    }
    return true;
  }

  protected void openFile(String paramString)
    throws PdfException
  {
    String str = paramString.toLowerCase();
    int i = (str.endsWith(".pdf")) || (str.endsWith(".fdf")) || (str.endsWith(".tif")) || (str.endsWith(".tiff")) || (str.endsWith(".png")) || (str.endsWith(".jpg")) || (str.endsWith(".jpeg")) ? 1 : 0;
    if (i != 0)
      this.currentCommands.openTransferedFile(paramString);
    else
      this.currentGUI.showMessageDialog("You may only import a valid PDF or image");
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.gui.SingleViewTransferHandler
 * JD-Core Version:    0.6.2
 */