package org.jpedal.examples.viewer.javabean;

import java.io.File;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.jpedal.examples.viewer.Values;
import org.jpedal.examples.viewer.Viewer;

public class ViewerBean extends JPanel
{
  private Viewer viewer = new Viewer(this, "jar:/org/jpedal/examples/viewer/res/preferences/Bean.xml");
  private File document = null;
  private Integer pageNumber = null;
  private Integer rotation = null;
  private Integer zoom = null;
  private Boolean isMenuBarVisible = null;
  private Boolean isToolBarVisible = null;
  private Boolean isDisplayOptionsBarVisible = null;
  private Boolean isSideTabBarVisible = null;
  private Boolean isNavigationBarVisible = null;

  public ViewerBean()
  {
    this.viewer.setupViewer();
  }

  public Viewer getViewer()
  {
    return this.viewer;
  }

  public void setDocument(File paramFile)
  {
    this.document = paramFile;
    excuteCommand(10, new String[] { String.valueOf(paramFile) });
    if (this.pageNumber != null)
      excuteCommand(56, new String[] { String.valueOf(this.pageNumber) });
    if (this.rotation != null)
      excuteCommand(251, new String[] { String.valueOf(this.rotation) });
    if (this.zoom != null)
      excuteCommand(252, new String[] { String.valueOf(this.zoom) });
    else
      excuteCommand(252, new String[] { String.valueOf(100) });
    if (this.isMenuBarVisible != null)
      setMenuBar(this.isMenuBarVisible.booleanValue());
    if (this.isToolBarVisible != null)
      setToolBar(this.isToolBarVisible.booleanValue());
    if (this.isDisplayOptionsBarVisible != null)
      setDisplayOptionsBar(this.isDisplayOptionsBarVisible.booleanValue());
    if (this.isSideTabBarVisible != null)
      setSideTabBar(this.isSideTabBarVisible.booleanValue());
    if (this.isNavigationBarVisible != null)
      setNavigationBar(this.isNavigationBarVisible.booleanValue());
  }

  public int getPageNumber()
  {
    if (this.pageNumber == null)
      return 1;
    return this.pageNumber.intValue();
  }

  public void setPageNumber(int paramInt)
  {
    this.pageNumber = Integer.valueOf(paramInt);
    if (this.document != null)
      excuteCommand(56, new String[] { String.valueOf(paramInt) });
  }

  public int getRotation()
  {
    if (this.rotation == null)
      return 0;
    return this.rotation.intValue();
  }

  public void setRotation(int paramInt)
  {
    this.rotation = Integer.valueOf(paramInt);
    if (this.document != null)
      excuteCommand(251, new String[] { String.valueOf(paramInt) });
  }

  public int getZoom()
  {
    if (this.zoom == null)
      return 100;
    return this.zoom.intValue();
  }

  public void setZoom(int paramInt)
  {
    this.zoom = Integer.valueOf(paramInt);
    if (this.document != null)
      excuteCommand(252, new String[] { String.valueOf(paramInt) });
  }

  public void setMenuBar(boolean paramBoolean)
  {
    this.isMenuBarVisible = Boolean.valueOf(paramBoolean);
    this.viewer.executeCommand(28, new Object[] { "ShowMenubar", Boolean.valueOf(paramBoolean) });
  }

  public boolean getMenuBar()
  {
    if (this.isMenuBarVisible == null)
      return true;
    return this.isMenuBarVisible.booleanValue();
  }

  public void setToolBar(boolean paramBoolean)
  {
    this.isToolBarVisible = Boolean.valueOf(paramBoolean);
    this.viewer.executeCommand(28, new Object[] { "ShowButtons", Boolean.valueOf(paramBoolean) });
  }

  public boolean getToolBar()
  {
    if (this.isToolBarVisible == null)
      return true;
    return this.isToolBarVisible.booleanValue();
  }

  public void setDisplayOptionsBar(boolean paramBoolean)
  {
    this.isDisplayOptionsBarVisible = Boolean.valueOf(paramBoolean);
    this.viewer.executeCommand(28, new Object[] { "ShowDisplayoptions", Boolean.valueOf(paramBoolean) });
  }

  public boolean getDisplayOptionsBar()
  {
    if (this.isDisplayOptionsBarVisible == null)
      return true;
    return this.isDisplayOptionsBarVisible.booleanValue();
  }

  public void setSideTabBar(boolean paramBoolean)
  {
    this.isSideTabBarVisible = Boolean.valueOf(paramBoolean);
    this.viewer.executeCommand(28, new Object[] { "ShowSidetabbar", Boolean.valueOf(paramBoolean) });
  }

  public boolean getSideTabBar()
  {
    if (this.isSideTabBarVisible == null)
      return true;
    return this.isSideTabBarVisible.booleanValue();
  }

  public void setNavigationBar(boolean paramBoolean)
  {
    this.isNavigationBarVisible = Boolean.valueOf(paramBoolean);
    this.viewer.executeCommand(28, new Object[] { "ShowNavigationbar", Boolean.valueOf(paramBoolean) });
  }

  public boolean getNavigationBar()
  {
    if (this.isNavigationBarVisible == null)
      return true;
    return this.isNavigationBarVisible.booleanValue();
  }

  private void excuteCommand(final int paramInt, final Object[] paramArrayOfObject)
  {
    SwingUtilities.invokeLater(new Runnable()
    {
      public void run()
      {
        ViewerBean.this.viewer.executeCommand(paramInt, paramArrayOfObject);
        while (Values.isProcessing())
          try
          {
            Thread.sleep(100L);
          }
          catch (InterruptedException localInterruptedException)
          {
            localInterruptedException.printStackTrace();
          }
        ViewerBean.this.repaint();
      }
    });
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.javabean.ViewerBean
 * JD-Core Version:    0.6.2
 */