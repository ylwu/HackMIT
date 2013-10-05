package org.jpedal.utils;

import javax.swing.SwingUtilities;

public abstract class SwingWorker
{
  private Object value;
  private ThreadVar threadVar;

  protected synchronized Object getValue()
  {
    return this.value;
  }

  private synchronized void setValue(Object paramObject)
  {
    this.value = paramObject;
  }

  public abstract Object construct();

  public void finished()
  {
  }

  public void interrupt()
  {
    Thread localThread = this.threadVar.get();
    if (localThread != null)
    {
      localThread.interrupt();
      while (localThread.isAlive())
        try
        {
          Thread.sleep(20L);
        }
        catch (InterruptedException localInterruptedException)
        {
          if (LogWriter.isOutput())
            LogWriter.writeLog("Exception: " + localInterruptedException.getMessage());
        }
    }
    this.threadVar.clear();
  }

  public Object get()
  {
    while (true)
    {
      Thread localThread = this.threadVar.get();
      if (localThread == null)
        return getValue();
      try
      {
        localThread.join();
      }
      catch (InterruptedException localInterruptedException)
      {
        Thread.currentThread().interrupt();
        return null;
      }
    }
  }

  public SwingWorker()
  {
    final Runnable local1 = new Runnable()
    {
      public void run()
      {
        SwingWorker.this.finished();
      }
    };
    Runnable local2 = new Runnable()
    {
      public void run()
      {
        try
        {
          SwingWorker.this.setValue(SwingWorker.this.construct());
        }
        catch (Exception localException)
        {
        }
        finally
        {
          SwingWorker.this.threadVar.clear();
        }
        SwingUtilities.invokeLater(local1);
      }
    };
    Thread localThread = new Thread(local2);
    localThread.setDaemon(true);
    this.threadVar = new ThreadVar(localThread);
  }

  public void start()
  {
    Thread localThread = this.threadVar.get();
    if (localThread != null)
      localThread.start();
  }

  private static class ThreadVar
  {
    private Thread thread;

    ThreadVar(Thread paramThread)
    {
      this.thread = paramThread;
    }

    synchronized Thread get()
    {
      return this.thread;
    }

    synchronized void clear()
    {
      this.thread = null;
    }
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.utils.SwingWorker
 * JD-Core Version:    0.6.2
 */