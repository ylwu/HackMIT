package org.jpedal.examples.viewer;

import java.awt.Container;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.LinkedList;
import javax.swing.JApplet;
import javax.swing.JFrame;
import org.jpedal.examples.viewer.gui.SwingGUI;
import org.jpedal.io.ObjectStore;

public class AppletViewer extends JApplet
{
  private static final long serialVersionUID = 8823940529835337414L;
  private LinkedList runnableQueue;
  Viewer current = new Viewer(1);
  boolean isInitialised = false;
  boolean destroy = false;

  public void init()
  {
    if (!this.isInitialised)
    {
      this.isInitialised = true;
      String str1 = getParameter("propertiesFile");
      if (str1 != null)
        this.current.loadProperties(str1);
      else
        this.current.loadProperties("jar:/org/jpedal/examples/viewer/res/preferences/Default.xml");
      this.current.setupViewer();
      String str2 = getParameter("org.jpedal.memory");
      if ((str2 != null) && (str2.equals("true")))
        System.setProperty("org.jpedal.memory", "true");
      if ((this.current.currentGUI.getFrame() instanceof JFrame))
        getContentPane().add(((JFrame)this.current.currentGUI.getFrame()).getContentPane());
      else
        getContentPane().add(this.current.currentGUI.getFrame());
    }
  }

  public void start()
  {
    init();
    String str = getParameter("allowJSCalls");
    if ((str != null) && (str.equals("true")))
    {
      this.runnableQueue = new LinkedList();
      localObject = new Thread("JS call runner")
      {
        public void run()
        {
          while (!AppletViewer.this.destroy)
          {
            try
            {
              Thread.sleep(200L);
            }
            catch (InterruptedException localInterruptedException)
            {
              localInterruptedException.printStackTrace();
            }
            if (!AppletViewer.this.runnableQueue.isEmpty())
            {
              Runnable localRunnable = (Runnable)AppletViewer.this.runnableQueue.removeFirst();
              if (localRunnable != null)
                localRunnable.run();
            }
          }
        }
      };
      ((Thread)localObject).setDaemon(true);
      ((Thread)localObject).start();
    }
    Object localObject = getParameter("openURL");
    if (localObject != null)
      this.current.openDefaultFile((String)localObject);
  }

  public void destroy()
  {
    this.destroy = true;
    Viewer.exitOnClose = false;
    this.current.executeCommand(7, null);
    ObjectStore.flushPages();
  }

  public void executeCommand(String paramString1, String paramString2)
  {
    if (this.runnableQueue == null)
    {
      System.out.println("Cannot call from JavaScript without setting 'allowJSCalls' parameter to true!");
      return;
    }
    paramString1 = paramString1.toLowerCase();
    int i = 0;
    if ("info".equals(paramString1))
      i = 1;
    else if ("bitmap".equals(paramString1))
      i = 2;
    else if ("images".equals(paramString1))
      i = 3;
    else if ("text".equals(paramString1))
      i = 4;
    else if ("save".equals(paramString1))
      i = 5;
    else if ("print".equals(paramString1))
      i = 6;
    else if ("exit".equals(paramString1))
      i = 7;
    else if ("autoscroll".equals(paramString1))
      i = 8;
    else if ("docinfo".equals(paramString1))
      i = 9;
    else if ("openfile".equals(paramString1))
      i = 10;
    else if ("find".equals(paramString1))
      i = 12;
    else if ("snapshot".equals(paramString1))
      i = 13;
    else if ("openurl".equals(paramString1))
      i = 14;
    else if ("visitwebsite".equals(paramString1))
      i = 15;
    else if ("previousdocument".equals(paramString1))
      i = 16;
    else if ("nextdocument".equals(paramString1))
      i = 17;
    else if ("previousresult".equals(paramString1))
      i = 18;
    else if ("nextresult".equals(paramString1))
      i = 19;
    else if ("tip".equals(paramString1))
      i = 20;
    else if ("update".equals(paramString1))
      i = 23;
    else if ("preferences".equals(paramString1))
      i = 24;
    else if ("copy".equals(paramString1))
      i = 25;
    else if ("selectall".equals(paramString1))
      i = 26;
    else if ("deselectall".equals(paramString1))
      i = 27;
    else if ("updateguilayout".equals(paramString1))
      i = 28;
    else if ("mousemode".equals(paramString1))
      i = 29;
    else if ("panmode".equals(paramString1))
      i = 30;
    else if ("textselect".equals(paramString1))
      i = 31;
    else if ("separatecover".equals(paramString1))
      i = 32;
    else if ("firstpage".equals(paramString1))
      i = 50;
    else if ("fbackpage".equals(paramString1))
      i = 51;
    else if ("backpage".equals(paramString1))
      i = 52;
    else if ("forwardpage".equals(paramString1))
      i = 53;
    else if ("fforwardpage".equals(paramString1))
      i = 54;
    else if ("lastpage".equals(paramString1))
      i = 55;
    else if ("goto".equals(paramString1))
      i = 56;
    else if ("single".equals(paramString1))
      i = 57;
    else if ("continuous".equals(paramString1))
      i = 58;
    else if ("continuous_facing".equals(paramString1))
      i = 59;
    else if ("facing".equals(paramString1))
      i = 60;
    else if ("pageflow".equals(paramString1))
      i = 62;
    else if ("fullscreen".equals(paramString1))
      i = 61;
    else if ("rss".equals(paramString1))
      i = 997;
    else if ("help".equals(paramString1))
      i = 998;
    else if ("buy".equals(paramString1))
      i = 999;
    else if ("quality".equals(paramString1))
      i = 250;
    else if ("rotation".equals(paramString1))
      i = 251;
    else if ("scaling".equals(paramString1))
      i = 252;
    else if ("saveform".equals(paramString1))
      i = 500;
    else if ("pdf".equals(paramString1))
      i = 501;
    else if ("rotate".equals(paramString1))
      i = 502;
    else if ("delete".equals(paramString1))
      i = 503;
    else if ("add".equals(paramString1))
      i = 504;
    else if ("security".equals(paramString1))
      i = 505;
    else if ("addheaderfooter".equals(paramString1))
      i = 506;
    else if ("stamptext".equals(paramString1))
      i = 507;
    else if ("stampimage".equals(paramString1))
      i = 508;
    else if ("setcrop".equals(paramString1))
      i = 509;
    else if ("nup".equals(paramString1))
      i = 510;
    else if ("handouts".equals(paramString1))
      i = 511;
    else if ("scroll".equals(paramString1))
      i = 601;
    else if ("addview".equals(paramString1))
      i = 700;
    else if ("forward".equals(paramString1))
      i = 701;
    else if ("back".equals(paramString1))
      i = 702;
    else
      try
      {
        Field localField = Commands.class.getField(paramString1.toUpperCase());
        i = localField.getInt(localField);
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
      }
    final String[] arrayOfString;
    if (paramString2 != null)
      arrayOfString = new String[] { paramString2 };
    else
      arrayOfString = null;
    final int j = i;
    Runnable local2 = new Runnable()
    {
      public void run()
      {
        AppletViewer.this.current.executeCommand(j, arrayOfString);
      }
    };
    this.runnableQueue.addLast(local2);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.AppletViewer
 * JD-Core Version:    0.6.2
 */