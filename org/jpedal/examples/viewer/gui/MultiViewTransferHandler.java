package org.jpedal.examples.viewer.gui;

import java.awt.datatransfer.Transferable;
import java.io.File;
import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JComponent;
import org.jpedal.examples.viewer.Commands;
import org.jpedal.examples.viewer.Values;
import org.jpedal.utils.Messages;
import org.jpedal.utils.SwingWorker;

public class MultiViewTransferHandler extends BaseTransferHandler
{
  private int fileCount = 0;

  public MultiViewTransferHandler(Values paramValues, SwingGUI paramSwingGUI, Commands paramCommands)
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
        System.out.println((String)localObject2);
        String str1 = ((String)localObject2).toLowerCase();
        if (str1.startsWith("http:/"))
        {
          this.currentCommands.openTransferedFile(str1);
          return true;
        }
        if (str1.startsWith("file:/"))
        {
          String[] arrayOfString1 = ((String)localObject2).split("file:/");
          LinkedList localLinkedList = new LinkedList();
          for (String str2 : arrayOfString1)
            if (!str2.isEmpty())
            {
              File localFile = new File(new URL("file:/" + str2).getFile());
              System.out.println(localFile);
              localLinkedList.add(localFile);
            }
          return openFiles(localLinkedList);
        }
      }
      else if ((localObject1 instanceof List))
      {
        localObject2 = (List)localObject1;
        return openFiles((List)localObject2);
      }
    }
    catch (Exception localException)
    {
    }
    return false;
  }

  private boolean openFiles(List paramList)
  {
    this.fileCount = 0;
    List localList1 = getFlattenedFiles(paramList, new ArrayList());
    if (this.fileCount == this.commonValues.getMaxMiltiViewers())
      this.currentGUI.showMessageDialog("You have choosen to import more files than your current set maximum (" + this.commonValues.getMaxMiltiViewers() + ").  Only the first " + this.commonValues.getMaxMiltiViewers() + " files will be imported.\nYou can change this value " + "in View | Preferences", "Maximum number of files reached", 1);
    List[] arrayOfList = filterFiles(localList1);
    final List localList2 = arrayOfList[0];
    List localList3 = arrayOfList[1];
    int i = localList3.size();
    int j = localList2.size();
    if (i > 0)
    {
      localObject1 = "";
      Iterator localIterator = localList3.iterator();
      while (localIterator.hasNext())
      {
        Object localObject2 = localIterator.next();
        String str1 = (String)localObject2;
        String str2 = new File(str1).getName();
        localObject1 = (String)localObject1 + str2 + '\n';
      }
      int k = this.currentGUI.showConfirmDialog("You have selected " + localList1.size() + " files to open.  The following file(s) cannot be opened\nas they are not valid PDFs " + "or images.\n" + (String)localObject1 + "\nWould you like to open the remaining " + j + " files?", "File Import", 0, 3);
      if (k == 1)
        return false;
    }
    Object localObject1 = new SwingWorker()
    {
      public Object construct()
      {
        Iterator localIterator = localList2.iterator();
        while (localIterator.hasNext())
        {
          Object localObject = localIterator.next();
          String str = (String)localObject;
          try
          {
            MultiViewTransferHandler.this.currentCommands.openTransferedFile(str);
          }
          catch (Exception localException)
          {
            int i;
            if (localList2.size() == 1)
            {
              MultiViewTransferHandler.this.currentGUI.showMessageDialog(Messages.getMessage("PdfViewerOpenerror"), MultiViewTransferHandler.this.commonValues.getSelectedFile(), 0);
              i = 1;
            }
            else
            {
              i = MultiViewTransferHandler.this.currentGUI.showConfirmDialog(Messages.getMessage("PdfViewerOpenerror") + ". Continue opening remaining files?", MultiViewTransferHandler.this.commonValues.getSelectedFile(), 0, 3);
            }
            MultiViewTransferHandler.this.currentGUI.closeMultiViewerWindow(MultiViewTransferHandler.this.commonValues.getSelectedFile());
            if (i == 1)
              return null;
          }
        }
        return null;
      }
    };
    ((SwingWorker)localObject1).start();
    return true;
  }

  private static List[] filterFiles(List paramList)
  {
    LinkedList localLinkedList1 = new LinkedList();
    LinkedList localLinkedList2 = new LinkedList();
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      Object localObject = localIterator.next();
      String str1 = (String)localObject;
      String str2 = str1.toLowerCase();
      int i = (str2.endsWith(".pdf")) || (str2.endsWith(".fdf")) || (str2.endsWith(".tif")) || (str2.endsWith(".tiff")) || (str2.endsWith(".png")) || (str2.endsWith(".jpg")) || (str2.endsWith(".jpeg")) ? 1 : 0;
      if (i != 0)
        localLinkedList1.add(str1);
      else
        localLinkedList2.add(str1);
    }
    return new List[] { localLinkedList1, localLinkedList2 };
  }

  private List getFlattenedFiles(List paramList1, List paramList2)
  {
    Iterator localIterator = paramList1.iterator();
    while (localIterator.hasNext())
    {
      Object localObject = localIterator.next();
      if (this.fileCount == this.commonValues.getMaxMiltiViewers())
        return paramList2;
      File localFile = (File)localObject;
      if (localFile.isDirectory())
      {
        getFlattenedFiles(Arrays.asList(localFile.listFiles()), paramList2);
      }
      else
      {
        paramList2.add(localFile.getAbsolutePath());
        this.fileCount += 1;
      }
    }
    return paramList2;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.gui.MultiViewTransferHandler
 * JD-Core Version:    0.6.2
 */