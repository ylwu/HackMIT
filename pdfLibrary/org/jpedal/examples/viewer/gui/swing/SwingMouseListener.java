package org.jpedal.examples.viewer.gui.swing;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import org.jpedal.PdfDecoder;
import org.jpedal.display.Display;
import org.jpedal.display.Display.BoolValue;
import org.jpedal.examples.viewer.Commands;
import org.jpedal.examples.viewer.MouseMode;
import org.jpedal.examples.viewer.Values;
import org.jpedal.examples.viewer.gui.SwingGUI;
import org.jpedal.examples.viewer.gui.generic.GUIMouseHandler;
import org.jpedal.examples.viewer.utils.PropertiesFile;
import org.jpedal.io.PdfObjectReader;
import org.jpedal.render.DynamicVectorRenderer;
import org.jpedal.utils.DPIFactory;

public class SwingMouseListener
  implements GUIMouseHandler, MouseListener, MouseMotionListener, MouseWheelListener
{
  private PdfDecoder decode_pdf;
  private SwingGUI currentGUI;
  private Values commonValues;
  private Commands currentCommands;
  SwingMouseSelector selectionFunctions;
  SwingMousePanMode panningFunctions;
  SwingMousePageTurn pageTurnFunctions;
  private static SwingMouseFunctionality customMouseFunctions;
  private boolean scrollPageChanging = false;
  private int cx;
  private int cy;
  private String message = "";
  private MouseMode mouseMode = new MouseMode();
  private double middleDragStartX;
  private double middleDragStartY;
  private double xVelocity;
  private double yVelocity;
  private Timer middleDragTimer;
  int scrollToPage = -1;

  public SwingMouseListener(PdfDecoder paramPdfDecoder, SwingGUI paramSwingGUI, Values paramValues, Commands paramCommands)
  {
    this.decode_pdf = paramPdfDecoder;
    this.currentGUI = paramSwingGUI;
    this.commonValues = paramValues;
    this.currentCommands = paramCommands;
    this.mouseMode = paramCommands.getMouseMode();
    this.selectionFunctions = new SwingMouseSelector(paramPdfDecoder, paramSwingGUI, paramValues, paramCommands);
    this.panningFunctions = new SwingMousePanMode(paramPdfDecoder);
    this.pageTurnFunctions = new SwingMousePageTurn(paramPdfDecoder, paramSwingGUI, paramValues, paramCommands);
  }

  public void setupMouse()
  {
    this.decode_pdf.addMouseMotionListener(this);
    this.decode_pdf.addMouseListener(this);
    this.decode_pdf.addMouseWheelListener(this);
    this.decode_pdf.setDefaultCursor(Cursor.getPredefinedCursor(0));
  }

  public void mouseClicked(MouseEvent paramMouseEvent)
  {
    switch (this.mouseMode.getMouseMode())
    {
    case 0:
      this.selectionFunctions.mouseClicked(paramMouseEvent);
      break;
    case 1:
    }
    if ((this.decode_pdf.getPages().getBoolean(Display.BoolValue.TURNOVER_ON)) && (this.decode_pdf.getDisplayView() == 3))
      this.pageTurnFunctions.mouseClicked(paramMouseEvent);
    if (customMouseFunctions != null)
      customMouseFunctions.mouseClicked(paramMouseEvent);
  }

  public void mouseEntered(MouseEvent paramMouseEvent)
  {
    switch (this.mouseMode.getMouseMode())
    {
    case 0:
      break;
    case 1:
    }
    if ((this.decode_pdf.getPages().getBoolean(Display.BoolValue.TURNOVER_ON)) && (this.decode_pdf.getDisplayView() == 3))
      this.pageTurnFunctions.mouseEntered(paramMouseEvent);
    if (customMouseFunctions != null)
      customMouseFunctions.mouseEntered(paramMouseEvent);
  }

  public void mouseExited(MouseEvent paramMouseEvent)
  {
    int[] arrayOfInt = { 1, 0 };
    this.currentGUI.setMultibox(arrayOfInt);
    switch (this.mouseMode.getMouseMode())
    {
    case 0:
      this.selectionFunctions.mouseExited(paramMouseEvent);
      break;
    case 1:
    }
    if ((this.decode_pdf.getPages().getBoolean(Display.BoolValue.TURNOVER_ON)) && (this.decode_pdf.getDisplayView() == 3))
      this.pageTurnFunctions.mouseExited(paramMouseEvent);
    if (customMouseFunctions != null)
      customMouseFunctions.mouseExited(paramMouseEvent);
  }

  public void mousePressed(MouseEvent paramMouseEvent)
  {
    if (SwingUtilities.isMiddleMouseButton(paramMouseEvent))
    {
      this.panningFunctions.mousePressed(paramMouseEvent);
    }
    else
    {
      switch (this.mouseMode.getMouseMode())
      {
      case 0:
        this.selectionFunctions.mousePressed(paramMouseEvent);
        break;
      case 1:
        this.panningFunctions.mousePressed(paramMouseEvent);
      }
      if ((!SwingUtilities.isMiddleMouseButton(paramMouseEvent)) && (this.decode_pdf.getPages().getBoolean(Display.BoolValue.TURNOVER_ON)) && (this.decode_pdf.getDisplayView() == 3))
        this.pageTurnFunctions.mousePressed(paramMouseEvent);
      if (customMouseFunctions != null)
        customMouseFunctions.mousePressed(paramMouseEvent);
    }
  }

  public void mouseReleased(MouseEvent paramMouseEvent)
  {
    if (SwingUtilities.isMiddleMouseButton(paramMouseEvent))
    {
      this.panningFunctions.mouseReleased(paramMouseEvent);
    }
    else
    {
      switch (this.mouseMode.getMouseMode())
      {
      case 0:
        this.selectionFunctions.mouseReleased(paramMouseEvent);
        break;
      case 1:
        this.panningFunctions.mouseReleased(paramMouseEvent);
      }
      if ((!SwingUtilities.isMiddleMouseButton(paramMouseEvent)) && (this.decode_pdf.getPages().getBoolean(Display.BoolValue.TURNOVER_ON)) && (this.decode_pdf.getDisplayView() == 3))
        this.pageTurnFunctions.mouseReleased(paramMouseEvent);
      if (customMouseFunctions != null)
        customMouseFunctions.mouseReleased(paramMouseEvent);
    }
  }

  public void mouseDragged(MouseEvent paramMouseEvent)
  {
    scrollAndUpdateCoords(paramMouseEvent);
    if (SwingUtilities.isMiddleMouseButton(paramMouseEvent))
    {
      this.panningFunctions.mouseDragged(paramMouseEvent);
    }
    else
    {
      switch (this.mouseMode.getMouseMode())
      {
      case 0:
        this.selectionFunctions.mouseDragged(paramMouseEvent);
        break;
      case 1:
        this.panningFunctions.mouseDragged(paramMouseEvent);
      }
      if (SwingUtilities.isMiddleMouseButton(paramMouseEvent))
      {
        this.xVelocity = ((paramMouseEvent.getX() - this.decode_pdf.getVisibleRect().getX() - this.middleDragStartX) / 4.0D);
        this.yVelocity = ((paramMouseEvent.getY() - this.decode_pdf.getVisibleRect().getY() - this.middleDragStartY) / 4.0D);
      }
      if ((!SwingUtilities.isMiddleMouseButton(paramMouseEvent)) && (this.decode_pdf.getPages().getBoolean(Display.BoolValue.TURNOVER_ON)) && (this.decode_pdf.getDisplayView() == 3))
        this.pageTurnFunctions.mouseDragged(paramMouseEvent);
      if (customMouseFunctions != null)
        customMouseFunctions.mouseDragged(paramMouseEvent);
    }
  }

  public static void setCustomMouseFunctions(SwingMouseFunctionality paramSwingMouseFunctionality)
  {
    customMouseFunctions = paramSwingMouseFunctionality;
  }

  public void mouseMoved(MouseEvent paramMouseEvent)
  {
    int i = this.commonValues.getCurrentPage();
    Point localPoint = this.selectionFunctions.getCoordsOnPage(paramMouseEvent.getX(), paramMouseEvent.getY(), i);
    int j = (int)localPoint.getX();
    int k = (int)localPoint.getY();
    updateCoords(j, k);
    switch (this.mouseMode.getMouseMode())
    {
    case 0:
      int[] arrayOfInt = this.selectionFunctions.updateXY(paramMouseEvent.getX(), paramMouseEvent.getY());
      j = arrayOfInt[0];
      k = arrayOfInt[1];
      if (!this.currentCommands.extractingAsImage)
        getObjectUnderneath(j, k);
      this.selectionFunctions.mouseMoved(paramMouseEvent);
      break;
    case 1:
    }
    if ((this.decode_pdf.getPages().getBoolean(Display.BoolValue.TURNOVER_ON)) && (this.decode_pdf.getDisplayView() == 3))
      this.pageTurnFunctions.mouseMoved(paramMouseEvent);
    if (customMouseFunctions != null)
      customMouseFunctions.mouseMoved(paramMouseEvent);
  }

  private void getObjectUnderneath(int paramInt1, int paramInt2)
  {
    if (this.decode_pdf.getDisplayView() == 1)
    {
      int i = this.decode_pdf.getDynamicRenderer().getObjectUnderneath(paramInt1, paramInt2);
      switch (i)
      {
      case -1:
        this.decode_pdf.setCursor(Cursor.getPredefinedCursor(0));
        break;
      case 1:
        this.decode_pdf.setCursor(Cursor.getPredefinedCursor(2));
        break;
      case 3:
        this.decode_pdf.setCursor(Cursor.getPredefinedCursor(1));
        break;
      case 4:
        this.decode_pdf.setCursor(Cursor.getPredefinedCursor(2));
        break;
      case 5:
        this.decode_pdf.setCursor(Cursor.getPredefinedCursor(2));
        break;
      case 6:
        this.decode_pdf.setCursor(Cursor.getPredefinedCursor(2));
      case 0:
      case 2:
      }
    }
  }

  public void mouseWheelMoved(MouseWheelEvent paramMouseWheelEvent)
  {
    switch (this.decode_pdf.getDisplayView())
    {
    case 5:
      break;
    case 3:
      if (!this.decode_pdf.getPages().getBoolean(Display.BoolValue.TURNOVER_ON))
        return;
      this.pageTurnFunctions.mouseWheelMoved(paramMouseWheelEvent);
      break;
    case 1:
      if ((this.currentGUI.getProperties().getValue("allowScrollwheelZoom").toLowerCase().equals("true")) && ((paramMouseWheelEvent.isMetaDown()) || (paramMouseWheelEvent.isControlDown())))
      {
        int i = this.currentGUI.getSelectedComboIndex(252);
        if (i != -1)
        {
          i = (int)this.decode_pdf.getDPIFactory().removeScaling(this.decode_pdf.getScaling() * 100.0F);
        }
        else
        {
          String str = (String)this.currentGUI.getSelectedComboItem(252);
          try
          {
            i = (int)Float.parseFloat(str);
          }
          catch (Exception localException3)
          {
            i = -1;
            int j = str.length();
            for (int k = 0; k < j; k++)
            {
              int m = str.charAt(k);
              if ((((m >= 48) && (m <= 57) ? 1 : 0) | (m == 46 ? 1 : 0)) == 0)
                break;
            }
            if (k > 0)
              str = str.substring(0, k);
            if (i == -1)
              try
              {
                i = (int)Float.parseFloat(str);
              }
              catch (Exception localException4)
              {
                i = -1;
              }
          }
        }
        float f1 = paramMouseWheelEvent.getWheelRotation();
        if ((i != 1) || (f1 < 0.0F))
        {
          if (f1 < 0.0F)
            f1 = 1.25F;
          else
            f1 = 0.8F;
          if (i + f1 >= 0.0F)
          {
            float f2 = i * f1;
            if ((int)f2 == i)
              f2 = i + 1;
            else
              f2 = (int)f2;
            if (f2 < 1.0F)
              f2 = 1.0F;
            if (f2 > 1000.0F)
              f2 = 1000.0F;
            Rectangle localRectangle = this.decode_pdf.getVisibleRect();
            final double d1 = paramMouseWheelEvent.getX() / this.decode_pdf.getBounds().getWidth();
            final double d2 = paramMouseWheelEvent.getY() / this.decode_pdf.getBounds().getHeight();
            this.currentGUI.snapScalingToDefaults(f2);
            Thread local1 = new Thread()
            {
              public void run()
              {
                try
                {
                  SwingMouseListener.this.decode_pdf.scrollRectToVisible(new Rectangle((int)(d1 * SwingMouseListener.this.decode_pdf.getWidth() - d2.getWidth() / 2.0D), (int)(this.val$y * SwingMouseListener.this.decode_pdf.getHeight() - d2.getHeight() / 2.0D), (int)SwingMouseListener.this.decode_pdf.getVisibleRect().getWidth(), (int)SwingMouseListener.this.decode_pdf.getVisibleRect().getHeight()));
                  SwingMouseListener.this.decode_pdf.repaint();
                }
                catch (Exception localException)
                {
                  localException.printStackTrace();
                }
              }
            };
            local1.setDaemon(true);
            local1.start();
            SwingUtilities.invokeLater(local1);
          }
        }
      }
      else
      {
        localObject = this.currentGUI.getVerticalScrollBar();
        this.scrollToPage = ((JScrollBar)localObject).getValue();
        if ((((JScrollBar)localObject).getValue() <= ((JScrollBar)localObject).getMaximum()) && (paramMouseWheelEvent.getUnitsToScroll() > 0) && (this.scrollToPage <= this.decode_pdf.getPageCount()))
        {
          if (this.scrollPageChanging)
            return;
          this.scrollPageChanging = true;
          if (this.scrollToPage < this.decode_pdf.getPageCount())
            this.scrollToPage += 1;
          if (SwingUtilities.isEventDispatchThread())
            ((JScrollBar)localObject).setValue(this.scrollToPage);
          else
            try
            {
              SwingUtilities.invokeAndWait(new Runnable()
              {
                public void run()
                {
                  this.val$scroll.setValue(SwingMouseListener.this.scrollToPage);
                }
              });
            }
            catch (Exception localException1)
            {
              SwingUtilities.invokeLater(new Runnable()
              {
                public void run()
                {
                  this.val$scroll.setValue(SwingMouseListener.this.scrollToPage);
                }
              });
            }
          this.scrollPageChanging = false;
        }
        else if ((((JScrollBar)localObject).getValue() >= ((JScrollBar)localObject).getMinimum()) && (paramMouseWheelEvent.getUnitsToScroll() < 0) && (this.scrollToPage >= 1))
        {
          if (this.scrollPageChanging)
            return;
          this.scrollPageChanging = true;
          if (this.scrollToPage >= 1)
            this.scrollToPage -= 1;
          if (SwingUtilities.isEventDispatchThread())
            ((JScrollBar)localObject).setValue(this.scrollToPage);
          else
            try
            {
              SwingUtilities.invokeAndWait(new Runnable()
              {
                public void run()
                {
                  this.val$scroll.setValue(SwingMouseListener.this.scrollToPage);
                }
              });
            }
            catch (Exception localException2)
            {
              SwingUtilities.invokeLater(new Runnable()
              {
                public void run()
                {
                  this.val$scroll.setValue(SwingMouseListener.this.scrollToPage);
                }
              });
            }
          this.scrollPageChanging = false;
        }
      }
      break;
    case 2:
    case 4:
    }
    Object localObject = new Area(this.decode_pdf.getVisibleRect());
    AffineTransform localAffineTransform = new AffineTransform();
    localAffineTransform.translate(0.0D, paramMouseWheelEvent.getUnitsToScroll() * this.decode_pdf.getScrollInterval());
    localObject = ((Area)localObject).createTransformedArea(localAffineTransform);
    this.decode_pdf.scrollRectToVisible(((Area)localObject).getBounds());
  }

  protected void scrollAndUpdateCoords(MouseEvent paramMouseEvent)
  {
    int i = this.decode_pdf.getScrollInterval();
    Rectangle localRectangle = new Rectangle(this.currentGUI.AdjustForAlignment(paramMouseEvent.getX()), paramMouseEvent.getY(), i, i);
    if ((this.currentGUI.allowScrolling()) && (!this.decode_pdf.getVisibleRect().contains(localRectangle)))
      this.decode_pdf.scrollRectToVisible(localRectangle);
    int j = this.commonValues.getCurrentPage();
    Point localPoint = this.selectionFunctions.getCoordsOnPage(paramMouseEvent.getX(), paramMouseEvent.getY(), j);
    int k = (int)localPoint.getX();
    int m = (int)localPoint.getY();
    updateCoords(k, m);
  }

  public void updateCoords(int paramInt1, int paramInt2)
  {
    this.cx = paramInt1;
    this.cy = paramInt2;
    if (this.decode_pdf.getDisplayView() != 1)
      if (SwingMouseSelector.activateMultipageHighlight)
      {
        if (this.decode_pdf.getDisplayView() == 3)
        {
          this.cx = 0;
          this.cy = 0;
        }
      }
      else
      {
        this.cx = 0;
        this.cy = 0;
      }
    if ((Values.isProcessing() | this.commonValues.getSelectedFile() == null))
      this.currentGUI.setCoordText("  X:  Y:   ");
    else
      this.currentGUI.setCoordText("  X: " + this.cx + " Y: " + this.cy + ' ' + ' ' + this.message);
  }

  public int[] getCursorLocation()
  {
    return new int[] { this.cx, this.cy };
  }

  public void checkLinks(boolean paramBoolean, PdfObjectReader paramPdfObjectReader)
  {
    this.pageTurnFunctions.checkLinks(paramBoolean, paramPdfObjectReader, this.cx, this.cy);
  }

  public void updateCordsFromFormComponent(MouseEvent paramMouseEvent)
  {
    JComponent localJComponent = (JComponent)paramMouseEvent.getSource();
    int i = localJComponent.getX() + paramMouseEvent.getX();
    int j = localJComponent.getY() + paramMouseEvent.getY();
    Point localPoint = this.selectionFunctions.getCoordsOnPage(i, j, this.commonValues.getCurrentPage());
    i = (int)localPoint.getX();
    j = (int)localPoint.getY();
    updateCoords(i, j);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.gui.swing.SwingMouseListener
 * JD-Core Version:    0.6.2
 */