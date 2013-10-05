package org.jpedal.io;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import org.jpedal.utils.LogWriter;

public class StatusBar
{
  private static final int debug_level = 0;
  private int progress_size = 0;
  private String current = "";
  private static final int progress_max_size = 100;
  private JProgressBar status = null;
  private boolean showMessages = false;
  public float percentageDone = 0.0F;
  private Color masterColor = null;
  private boolean reset = false;

  public StatusBar()
  {
    initialiseStatus("");
  }

  public StatusBar(Color paramColor)
  {
    this.masterColor = paramColor;
    initialiseStatus("");
  }

  public final void initialiseStatus(String paramString)
  {
    this.progress_size = 0;
    this.status = new JProgressBar();
    if (this.masterColor != null)
      this.status.setForeground(this.masterColor);
    this.status.setStringPainted(true);
    this.status.setMaximum(100);
    this.status.setMinimum(0);
    updateStatus(paramString, 4);
  }

  public final void updateStatus(String paramString, int paramInt)
  {
    this.current = paramString;
    if (this.showMessages)
      SwingUtilities.invokeLater(new Runnable()
      {
        public void run()
        {
          StatusBar.this.status.setString(StatusBar.this.current);
          StatusBar.this.status.setValue(StatusBar.this.progress_size);
        }
      });
    if ((0 > paramInt) && (LogWriter.isOutput()))
      LogWriter.writeLog(paramString);
  }

  public final Component getStatusObject()
  {
    return this.status;
  }

  public final void setProgress(int paramInt)
  {
    this.reset = false;
    if (this.status != null)
    {
      if (paramInt == 0)
        this.progress_size = 0;
      if (this.progress_size < paramInt)
        this.progress_size = paramInt;
      SwingUtilities.invokeLater(new Runnable()
      {
        public void run()
        {
          StatusBar.this.status.setValue(StatusBar.this.progress_size);
        }
      });
    }
  }

  public final void setProgress(final String paramString, int paramInt)
  {
    this.reset = false;
    if (this.status != null)
    {
      if (paramInt == 0)
        this.progress_size = 0;
      if (this.progress_size < paramInt)
        this.progress_size = paramInt;
      SwingUtilities.invokeLater(new Runnable()
      {
        public void run()
        {
          StatusBar.this.status.setString(paramString);
          StatusBar.this.status.setValue(StatusBar.this.progress_size);
        }
      });
    }
  }

  public final void resetStatus(String paramString)
  {
    this.reset = true;
    this.progress_size = 0;
    updateStatus(paramString, 4);
  }

  public final void setClientDisplay()
  {
    this.showMessages = true;
  }

  public void setVisible(boolean paramBoolean)
  {
    this.status.setVisible(paramBoolean);
  }

  public void setEnabled(boolean paramBoolean)
  {
    this.status.setEnabled(paramBoolean);
  }

  public boolean isVisible()
  {
    return this.status.isVisible();
  }

  public boolean isEnabled()
  {
    return this.status.isEnabled();
  }

  public boolean isDone()
  {
    return (this.reset) || (this.progress_size >= 100);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.io.StatusBar
 * JD-Core Version:    0.6.2
 */