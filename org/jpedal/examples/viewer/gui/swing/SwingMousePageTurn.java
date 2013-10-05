package org.jpedal.examples.viewer.gui.swing;

import java.awt.Container;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.util.Map;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import org.jpedal.PdfDecoder;
import org.jpedal.display.Display;
import org.jpedal.display.Display.BoolValue;
import org.jpedal.display.swing.MultiDisplay;
import org.jpedal.examples.viewer.Commands;
import org.jpedal.examples.viewer.Values;
import org.jpedal.examples.viewer.gui.SwingGUI;
import org.jpedal.examples.viewer.utils.PropertiesFile;
import org.jpedal.external.AnnotationHandler;
import org.jpedal.io.PdfObjectReader;
import org.jpedal.objects.PdfPageData;
import org.jpedal.utils.DPIFactory;

public class SwingMousePageTurn
  implements SwingMouseFunctionality
{
  private PdfDecoder decode_pdf;
  private SwingGUI currentGUI;
  private Values commonValues;
  private Commands currentCommands;
  private long lastPress;
  private boolean drawingTurnover = false;
  private boolean previewTurnover = false;
  private int pageFlowCurrentPage;
  private double middleDragStartX;
  private double middleDragStartY;
  private double xVelocity;
  private double yVelocity;
  private Timer middleDragTimer;
  long timeOfLastPageChange;
  boolean altIsDown = false;

  public SwingMousePageTurn(PdfDecoder paramPdfDecoder, SwingGUI paramSwingGUI, Values paramValues, Commands paramCommands)
  {
    this.decode_pdf = paramPdfDecoder;
    this.currentGUI = paramSwingGUI;
    this.commonValues = paramValues;
    this.currentCommands = paramCommands;
  }

  public void updateRectangle()
  {
  }

  public void mouseClicked(MouseEvent paramMouseEvent)
  {
    if ((this.decode_pdf.getDisplayView() == 1) && (paramMouseEvent.getButton() == 1) && (this.decode_pdf.getExternalHandler(25) != null))
    {
      int[] arrayOfInt = updateXY(paramMouseEvent.getX(), paramMouseEvent.getY());
      checkLinks(true, this.decode_pdf.getIO(), arrayOfInt[0], arrayOfInt[1]);
    }
  }

  public void mouseEntered(MouseEvent paramMouseEvent)
  {
  }

  public void mouseExited(MouseEvent paramMouseEvent)
  {
  }

  public void mousePressed(MouseEvent paramMouseEvent)
  {
    if ((this.previewTurnover) && (this.decode_pdf.getPages().getBoolean(Display.BoolValue.TURNOVER_ON)) && (this.decode_pdf.getDisplayView() == 3) && (paramMouseEvent.getButton() == 1))
    {
      this.drawingTurnover = true;
      this.decode_pdf.setCursor(this.currentGUI.getCursor(2));
      this.lastPress = System.currentTimeMillis();
    }
    if (paramMouseEvent.getButton() == 2)
    {
      this.middleDragStartX = (paramMouseEvent.getX() - this.decode_pdf.getVisibleRect().getX());
      this.middleDragStartY = (paramMouseEvent.getY() - this.decode_pdf.getVisibleRect().getY());
      this.decode_pdf.setCursor(this.currentGUI.getCursor(4));
      if (this.middleDragTimer == null)
        this.middleDragTimer = new Timer(100, new Object()
        {
          public void actionPerformed(ActionEvent paramAnonymousActionEvent)
          {
            Rectangle localRectangle = SwingMousePageTurn.this.decode_pdf.getVisibleRect();
            localRectangle.translate((int)SwingMousePageTurn.this.xVelocity, (int)SwingMousePageTurn.this.yVelocity);
            if (SwingMousePageTurn.this.xVelocity < -2.0D)
            {
              if (SwingMousePageTurn.this.yVelocity < -2.0D)
                SwingMousePageTurn.this.decode_pdf.setCursor(SwingMousePageTurn.this.currentGUI.getCursor(6));
              else if (SwingMousePageTurn.this.yVelocity > 2.0D)
                SwingMousePageTurn.this.decode_pdf.setCursor(SwingMousePageTurn.this.currentGUI.getCursor(12));
              else
                SwingMousePageTurn.this.decode_pdf.setCursor(SwingMousePageTurn.this.currentGUI.getCursor(5));
            }
            else if (SwingMousePageTurn.this.xVelocity > 2.0D)
            {
              if (SwingMousePageTurn.this.yVelocity < -2.0D)
                SwingMousePageTurn.this.decode_pdf.setCursor(SwingMousePageTurn.this.currentGUI.getCursor(8));
              else if (SwingMousePageTurn.this.yVelocity > 2.0D)
                SwingMousePageTurn.this.decode_pdf.setCursor(SwingMousePageTurn.this.currentGUI.getCursor(10));
              else
                SwingMousePageTurn.this.decode_pdf.setCursor(SwingMousePageTurn.this.currentGUI.getCursor(9));
            }
            else if (SwingMousePageTurn.this.yVelocity < -2.0D)
              SwingMousePageTurn.this.decode_pdf.setCursor(SwingMousePageTurn.this.currentGUI.getCursor(7));
            else if (SwingMousePageTurn.this.yVelocity > 2.0D)
              SwingMousePageTurn.this.decode_pdf.setCursor(SwingMousePageTurn.this.currentGUI.getCursor(11));
            else
              SwingMousePageTurn.this.decode_pdf.setCursor(SwingMousePageTurn.this.currentGUI.getCursor(4));
            SwingMousePageTurn.this.decode_pdf.scrollRectToVisible(localRectangle);
            Display localDisplay = SwingMousePageTurn.this.decode_pdf.getPages();
            if ((localDisplay instanceof MultiDisplay))
              ((MultiDisplay)localDisplay).decodeOtherPages(SwingMousePageTurn.this.currentGUI.getPageNumber(), SwingMousePageTurn.this.decode_pdf.getPageCount());
          }
        });
      this.middleDragTimer.start();
    }
  }

  public void mouseReleased(MouseEvent paramMouseEvent)
  {
    if ((this.decode_pdf.getPages().getBoolean(Display.BoolValue.TURNOVER_ON)) && (this.decode_pdf.getDisplayView() == 3))
    {
      this.drawingTurnover = false;
      boolean bool1 = this.currentGUI.getDragLeft();
      boolean bool2 = this.currentGUI.getDragTop();
      if (this.lastPress + 200L > System.currentTimeMillis())
      {
        if (bool1)
          this.currentCommands.executeCommand(52, null);
        else
          this.currentCommands.executeCommand(53, null);
        this.previewTurnover = false;
        this.decode_pdf.setCursor(this.currentGUI.getCursor(3));
      }
      else
      {
        Point localPoint = new Point();
        localPoint.y = this.decode_pdf.getInsetH();
        if (!bool2)
        {
          Point tmp144_142 = localPoint;
          tmp144_142.y = ((int)(tmp144_142.y + this.decode_pdf.getPdfPageData().getCropBoxHeight(1) * this.decode_pdf.getScaling()));
        }
        if (bool1)
          localPoint.x = ((int)(this.decode_pdf.getVisibleRect().getWidth() / 2.0D - this.decode_pdf.getPdfPageData().getCropBoxWidth(1) * this.decode_pdf.getScaling()));
        else
          localPoint.x = ((int)(this.decode_pdf.getVisibleRect().getWidth() / 2.0D + this.decode_pdf.getPdfPageData().getCropBoxWidth(1) * this.decode_pdf.getScaling()));
        testFall(localPoint, paramMouseEvent.getPoint(), bool1);
      }
    }
    if (paramMouseEvent.getButton() == 2)
    {
      this.xVelocity = 0.0D;
      this.yVelocity = 0.0D;
      this.decode_pdf.setCursor(this.currentGUI.getCursor(3));
      this.middleDragTimer.stop();
      this.decode_pdf.repaint();
    }
  }

  public void mouseDragged(MouseEvent paramMouseEvent)
  {
    if (SwingUtilities.isLeftMouseButton(paramMouseEvent))
    {
      this.altIsDown = paramMouseEvent.isAltDown();
      if (this.decode_pdf.getExternalHandler(25) != null)
      {
        int[] arrayOfInt = updateXY(paramMouseEvent.getX(), paramMouseEvent.getY());
        checkLinks(true, this.decode_pdf.getIO(), arrayOfInt[0], arrayOfInt[1]);
      }
      if ((this.decode_pdf.getPages().getBoolean(Display.BoolValue.TURNOVER_ON)) && ((this.drawingTurnover) || (this.previewTurnover)) && (this.decode_pdf.getDisplayView() == 3))
      {
        this.decode_pdf.setCursor(this.currentGUI.getCursor(2));
        if (this.currentGUI.getDragLeft())
        {
          if (this.currentGUI.getDragTop())
            this.decode_pdf.setUserOffsets(paramMouseEvent.getX(), paramMouseEvent.getY(), 996);
          else
            this.decode_pdf.setUserOffsets(paramMouseEvent.getX(), paramMouseEvent.getY(), 998);
        }
        else if (this.currentGUI.getDragTop())
          this.decode_pdf.setUserOffsets(paramMouseEvent.getX(), paramMouseEvent.getY(), 997);
        else
          this.decode_pdf.setUserOffsets(paramMouseEvent.getX(), paramMouseEvent.getY(), 999);
      }
    }
    else if (SwingUtilities.isMiddleMouseButton(paramMouseEvent))
    {
      this.xVelocity = ((paramMouseEvent.getX() - this.decode_pdf.getVisibleRect().getX() - this.middleDragStartX) / 4.0D);
      this.yVelocity = ((paramMouseEvent.getY() - this.decode_pdf.getVisibleRect().getY() - this.middleDragStartY) / 4.0D);
    }
  }

  public void mouseMoved(MouseEvent paramMouseEvent)
  {
    Point localPoint2;
    if ((this.decode_pdf.getDisplayView() == 3) && (this.decode_pdf.getPages().getBoolean(Display.BoolValue.TURNOVER_ON)) && (((SwingGUI)this.decode_pdf.getExternalHandler(11)).getPageTurnScalingAppropriate()) && (!this.decode_pdf.getPdfPageData().hasMultipleSizes()) && (!this.currentCommands.getPageTurnAnimating()))
    {
      float f1 = this.decode_pdf.getPdfPageData().getCropBoxHeight(1) * this.decode_pdf.getScaling() - 1.0F;
      float f2 = this.decode_pdf.getPdfPageData().getCropBoxWidth(1) * this.decode_pdf.getScaling() - 1.0F;
      if ((this.decode_pdf.getPdfPageData().getRotation(1) + this.currentGUI.getRotation()) % 180 == 90)
      {
        float f3 = f1;
        f1 = f2 + 1.0F;
        f2 = f3;
      }
      Point localPoint1 = new Point();
      if (this.commonValues.getCurrentPage() + 1 < this.commonValues.getPageCount())
      {
        localPoint1.x = ((int)(this.decode_pdf.getVisibleRect().getWidth() / 2.0D + f2));
        localPoint1.y = ((int)(this.decode_pdf.getInsetH() + f1));
        localPoint2 = paramMouseEvent.getPoint();
        if ((localPoint2.x > localPoint1.x - 30) && (localPoint2.x <= localPoint1.x) && (((localPoint2.y > localPoint1.y - 30) && (localPoint2.y <= localPoint1.y)) || ((localPoint2.y >= localPoint1.y - f1) && (localPoint2.y < localPoint1.y - f1 + 30.0F))))
        {
          this.decode_pdf.setCursor(this.currentGUI.getCursor(1));
          this.previewTurnover = true;
          if ((localPoint2.y >= localPoint1.y - f1) && (localPoint2.y < localPoint1.y - f1 + 30.0F))
          {
            localPoint1.y = ((int)(localPoint1.y - f1));
            this.decode_pdf.setUserOffsets((int)localPoint2.getX(), (int)localPoint2.getY(), 997);
          }
          else
          {
            this.decode_pdf.setUserOffsets((int)localPoint2.getX(), (int)localPoint2.getY(), 999);
          }
        }
        else
        {
          if (this.currentGUI.getDragTop())
            localPoint1.y = ((int)(localPoint1.y - f1));
          testFall(localPoint1, localPoint2, false);
        }
      }
      if (this.commonValues.getCurrentPage() != 1)
      {
        localPoint1.x = ((int)(this.decode_pdf.getVisibleRect().getWidth() / 2.0D - f2));
        localPoint1.y = ((int)(this.decode_pdf.getInsetH() + f1));
        localPoint2 = paramMouseEvent.getPoint();
        if ((localPoint2.x < localPoint1.x + 30) && (localPoint2.x >= localPoint1.x) && (((localPoint2.y > localPoint1.y - 30) && (localPoint2.y <= localPoint1.y)) || ((localPoint2.y >= localPoint1.y - f1) && (localPoint2.y < localPoint1.y - f1 + 30.0F))))
        {
          this.decode_pdf.setCursor(this.currentGUI.getCursor(1));
          this.previewTurnover = true;
          if ((localPoint2.y >= localPoint1.y - f1) && (localPoint2.y < localPoint1.y - f1 + 30.0F))
          {
            localPoint1.y = ((int)(localPoint1.y - f1));
            this.decode_pdf.setUserOffsets((int)localPoint2.getX(), (int)localPoint2.getY(), 996);
          }
          else
          {
            this.decode_pdf.setUserOffsets((int)localPoint2.getX(), (int)localPoint2.getY(), 998);
          }
        }
        else
        {
          if (this.currentGUI.getDragTop())
            localPoint1.y = ((int)(localPoint1.y - f1));
          testFall(localPoint1, localPoint2, true);
        }
      }
    }
    int[] arrayOfInt;
    if (this.currentGUI.useNewLayout)
    {
      arrayOfInt = new int[2];
      arrayOfInt[0] = 1;
      if ((this.decode_pdf.getDisplayView() == 1) || (SwingMouseSelector.activateMultipageHighlight))
      {
        int i;
        int j;
        if (this.currentGUI.getRotation() % 180 == 90)
        {
          i = this.decode_pdf.getPdfPageData().getCropBoxHeight(1);
          j = this.decode_pdf.getPdfPageData().getCropBoxWidth(1);
        }
        else
        {
          i = this.decode_pdf.getPdfPageData().getCropBoxWidth(1);
          j = this.decode_pdf.getPdfPageData().getCropBoxHeight(1);
        }
        localPoint2 = paramMouseEvent.getPoint();
        int k = (int)localPoint2.getX();
        int m = (int)localPoint2.getY();
        float f4 = this.decode_pdf.getScaling();
        double d1 = f4 * j;
        double d2 = f4 * i;
        int n = this.decode_pdf.getInsetH();
        double d3 = this.decode_pdf.getWidth() / 2 - d2 / 2.0D;
        if ((k >= d3) && (k <= d3 + d2) && (m >= n) && (m <= n + d1))
          arrayOfInt[1] = 1;
        else
          arrayOfInt[1] = 0;
      }
      else
      {
        arrayOfInt[1] = 0;
      }
      this.currentGUI.setMultibox(arrayOfInt);
    }
    if (this.decode_pdf.getExternalHandler(25) != null)
    {
      arrayOfInt = updateXY(paramMouseEvent.getX(), paramMouseEvent.getY());
      checkLinks(false, this.decode_pdf.getIO(), arrayOfInt[0], arrayOfInt[1]);
    }
  }

  public void mouseWheelMoved(MouseWheelEvent paramMouseWheelEvent)
  {
    if (this.decode_pdf.getDisplayView() == 5)
      return;
    if ((this.currentGUI.getProperties().getValue("allowScrollwheelZoom").toLowerCase().equals("true")) && (paramMouseWheelEvent.isControlDown()))
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
        catch (Exception localException1)
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
            catch (Exception localException2)
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
          Thread local2 = new Thread()
          {
            public void run()
            {
              try
              {
                SwingMousePageTurn.this.decode_pdf.scrollRectToVisible(new Rectangle((int)(d1 * SwingMousePageTurn.this.decode_pdf.getWidth() - d2.getWidth() / 2.0D), (int)(this.val$y * SwingMousePageTurn.this.decode_pdf.getHeight() - d2.getHeight() / 2.0D), (int)SwingMousePageTurn.this.decode_pdf.getVisibleRect().getWidth(), (int)SwingMousePageTurn.this.decode_pdf.getVisibleRect().getHeight()));
                SwingMousePageTurn.this.decode_pdf.repaint();
              }
              catch (Exception localException)
              {
                localException.printStackTrace();
              }
            }
          };
          local2.setDaemon(true);
          local2.start();
          SwingUtilities.invokeLater(local2);
        }
      }
    }
    else
    {
      final JScrollBar localJScrollBar = ((JScrollPane)this.decode_pdf.getParent().getParent()).getVerticalScrollBar();
      if (((localJScrollBar.getValue() >= localJScrollBar.getMaximum() - localJScrollBar.getHeight()) || (localJScrollBar.getHeight() == 0)) && (paramMouseWheelEvent.getUnitsToScroll() > 0) && (this.timeOfLastPageChange + 700L < System.currentTimeMillis()) && (this.currentGUI.getPageNumber() < this.decode_pdf.getPageCount()))
      {
        this.timeOfLastPageChange = System.currentTimeMillis();
        this.currentCommands.executeCommand(53, null);
        SwingUtilities.invokeLater(new Runnable()
        {
          public void run()
          {
            localJScrollBar.setValue(localJScrollBar.getMinimum());
          }
        });
      }
      else if ((localJScrollBar.getValue() == localJScrollBar.getMinimum()) && (paramMouseWheelEvent.getUnitsToScroll() < 0) && (this.timeOfLastPageChange + 700L < System.currentTimeMillis()) && (this.currentGUI.getPageNumber() > 1))
      {
        this.timeOfLastPageChange = System.currentTimeMillis();
        this.currentCommands.executeCommand(52, null);
        SwingUtilities.invokeLater(new Runnable()
        {
          public void run()
          {
            localJScrollBar.setValue(localJScrollBar.getMaximum());
          }
        });
      }
      else
      {
        Area localArea = new Area(this.decode_pdf.getVisibleRect());
        AffineTransform localAffineTransform = new AffineTransform();
        localAffineTransform.translate(0.0D, paramMouseWheelEvent.getUnitsToScroll() * this.decode_pdf.getScrollInterval());
        localArea = localArea.createTransformedArea(localAffineTransform);
        this.decode_pdf.scrollRectToVisible(localArea.getBounds());
      }
    }
  }

  public void checkLinks(boolean paramBoolean, PdfObjectReader paramPdfObjectReader, int paramInt1, int paramInt2)
  {
    Map localMap = this.currentGUI.getHotspots();
    if (localMap != null)
      ((AnnotationHandler)this.decode_pdf.getExternalHandler(25)).checkLinks(localMap, paramBoolean, paramPdfObjectReader, paramInt1, paramInt2, this.currentGUI, this.commonValues);
  }

  protected int[] updateXY(int paramInt1, int paramInt2)
  {
    float f = this.currentGUI.getScaling();
    int i = SwingGUI.getPDFDisplayInset();
    int j = this.currentGUI.getRotation();
    int k = (int)((this.currentGUI.AdjustForAlignment(paramInt1) - i) / f);
    int m = (int)((paramInt2 - i) / f);
    if (this.commonValues.maxViewY != 0)
    {
      k = (int)((k - this.commonValues.dx * f) / this.commonValues.viewportScale);
      m = (int)((this.currentGUI.mediaH - (this.currentGUI.mediaH - m / f - this.commonValues.dy) / this.commonValues.viewportScale) * f);
    }
    int[] arrayOfInt = new int[2];
    if (j == 90)
    {
      arrayOfInt[1] = (k + this.currentGUI.cropY);
      arrayOfInt[0] = (m + this.currentGUI.cropX);
    }
    else if (j == 180)
    {
      arrayOfInt[0] = (this.currentGUI.mediaW - (k + this.currentGUI.mediaW - this.currentGUI.cropW - this.currentGUI.cropX));
      arrayOfInt[1] = (m + this.currentGUI.cropY);
    }
    else if (j == 270)
    {
      arrayOfInt[1] = (this.currentGUI.mediaH - (k + this.currentGUI.mediaH - this.currentGUI.cropH - this.currentGUI.cropY));
      arrayOfInt[0] = (this.currentGUI.mediaW - (m + this.currentGUI.mediaW - this.currentGUI.cropW - this.currentGUI.cropX));
    }
    else
    {
      arrayOfInt[0] = (k + this.currentGUI.cropX);
      arrayOfInt[1] = (this.currentGUI.mediaH - (m + this.currentGUI.mediaH - this.currentGUI.cropH - this.currentGUI.cropY));
    }
    return arrayOfInt;
  }

  public void testFall(final Point paramPoint1, final Point paramPoint2, boolean paramBoolean)
  {
    if (!this.previewTurnover)
      return;
    float f1 = this.decode_pdf.getPdfPageData().getCropBoxWidth(1) * this.decode_pdf.getScaling() - 1.0F;
    if ((this.decode_pdf.getPdfPageData().getRotation(1) + this.currentGUI.getRotation()) % 180 == 90)
      f1 = this.decode_pdf.getPdfPageData().getCropBoxHeight(1) * this.decode_pdf.getScaling();
    final float f2 = f1;
    Object localObject;
    if (!paramBoolean)
    {
      if (!this.currentGUI.getDragLeft())
      {
        this.decode_pdf.setCursor(Cursor.getDefaultCursor());
        localObject = new Thread()
        {
          public void run()
          {
            paramPoint1.x = ((int)(SwingMousePageTurn.this.decode_pdf.getVisibleRect().getWidth() / 2.0D + f2));
            int i = 1;
            if (paramPoint2.x < paramPoint1.x - f2)
            {
              paramPoint1.x = ((int)(paramPoint1.x - 2.0F * f2));
              i = 0;
            }
            int j = 1;
            if ((i != 0) && (paramPoint2.x >= paramPoint1.x))
              paramPoint2.x = (paramPoint1.x - 1);
            if ((i == 0) && (paramPoint2.x <= paramPoint1.x))
              paramPoint2.x = (paramPoint1.x + 1);
            if ((!SwingMousePageTurn.this.currentGUI.getDragTop()) && (paramPoint2.y >= paramPoint1.y))
              paramPoint2.y = (paramPoint1.y - 1);
            if ((SwingMousePageTurn.this.currentGUI.getDragTop()) && (paramPoint2.y <= paramPoint1.y))
              paramPoint2.y = (paramPoint1.y + 1);
            double d1 = paramPoint1.x - paramPoint2.x;
            double d2 = paramPoint1.y - paramPoint2.y;
            while (((i != 0) && (paramPoint2.getX() <= paramPoint1.getX())) || ((i == 0) && (paramPoint2.getX() >= paramPoint1.getX())) || ((!SwingMousePageTurn.this.currentGUI.getDragTop()) && (paramPoint2.getY() <= paramPoint1.getY())) || ((SwingMousePageTurn.this.currentGUI.getDragTop()) && (paramPoint2.getY() >= paramPoint1.getY())))
            {
              double d3 = j * d1 * 0.002D;
              double d4 = j * d2 * 0.002D;
              if (Math.abs(d3) < 1.0D)
                d3 /= Math.abs(d3);
              if (Math.abs(d4) < 1.0D)
                d4 /= Math.abs(d4);
              paramPoint2.setLocation(paramPoint2.getX() + d3, paramPoint2.getY() + d4);
              if (SwingMousePageTurn.this.currentGUI.getDragTop())
                SwingMousePageTurn.this.decode_pdf.setUserOffsets((int)paramPoint2.getX(), (int)paramPoint2.getY(), 997);
              else
                SwingMousePageTurn.this.decode_pdf.setUserOffsets((int)paramPoint2.getX(), (int)paramPoint2.getY(), 999);
              if (j < 32)
                j *= 2;
              try
              {
                Thread.sleep(50L);
              }
              catch (Exception localException)
              {
                localException.printStackTrace();
              }
            }
            if (i == 0)
            {
              int k = SwingMousePageTurn.this.commonValues.getCurrentPage() + 1;
              if ((SwingMousePageTurn.this.decode_pdf.getPages().getBoolean(Display.BoolValue.SEPARATE_COVER)) && (k % 2 == 1))
                k++;
              else if ((!SwingMousePageTurn.this.decode_pdf.getPages().getBoolean(Display.BoolValue.SEPARATE_COVER)) && (k % 2 == 0))
                k++;
              SwingMousePageTurn.this.commonValues.setCurrentPage(k);
              SwingMousePageTurn.this.currentGUI.setPageNumber();
              SwingMousePageTurn.this.decode_pdf.setPageParameters(SwingMousePageTurn.this.currentGUI.getScaling(), SwingMousePageTurn.this.commonValues.getCurrentPage());
              SwingMousePageTurn.this.currentGUI.decodePage();
            }
            SwingMousePageTurn.this.decode_pdf.setUserOffsets(0, 0, 995);
            SwingMousePageTurn.this.currentCommands.setPageTurnAnimating(false);
          }
        };
        ((Thread)localObject).setDaemon(true);
        this.currentCommands.setPageTurnAnimating(true);
        ((Thread)localObject).start();
        this.previewTurnover = false;
      }
    }
    else if ((this.previewTurnover) && (this.currentGUI.getDragLeft()))
    {
      this.decode_pdf.setCursor(Cursor.getDefaultCursor());
      localObject = new Thread()
      {
        public void run()
        {
          paramPoint1.x = ((int)(SwingMousePageTurn.this.decode_pdf.getVisibleRect().getWidth() / 2.0D - f2));
          int i = 1;
          if (paramPoint2.x > paramPoint1.x + f2)
          {
            paramPoint1.x = ((int)(paramPoint1.x + 2.0F * f2));
            i = 0;
          }
          int j = 1;
          if ((i == 0) && (paramPoint2.x >= paramPoint1.x))
            paramPoint2.x = (paramPoint1.x - 1);
          if ((i != 0) && (paramPoint2.x <= paramPoint1.x))
            paramPoint2.x = (paramPoint1.x + 1);
          if ((!SwingMousePageTurn.this.currentGUI.getDragTop()) && (paramPoint2.y >= paramPoint1.y))
            paramPoint2.y = (paramPoint1.y - 1);
          if ((SwingMousePageTurn.this.currentGUI.getDragTop()) && (paramPoint2.y <= paramPoint1.y))
            paramPoint2.y = (paramPoint1.y + 1);
          double d1 = paramPoint1.x - paramPoint2.x;
          double d2 = paramPoint1.y - paramPoint2.y;
          while (((i == 0) && (paramPoint2.getX() <= paramPoint1.getX())) || ((i != 0) && (paramPoint2.getX() >= paramPoint1.getX())) || ((!SwingMousePageTurn.this.currentGUI.getDragTop()) && (paramPoint2.getY() <= paramPoint1.getY())) || ((SwingMousePageTurn.this.currentGUI.getDragTop()) && (paramPoint2.getY() >= paramPoint1.getY())))
          {
            double d3 = j * d1 * 0.002D;
            double d4 = j * d2 * 0.002D;
            if (Math.abs(d3) < 1.0D)
              d3 /= Math.abs(d3);
            if (Math.abs(d4) < 1.0D)
              d4 /= Math.abs(d4);
            paramPoint2.setLocation(paramPoint2.getX() + d3, paramPoint2.getY() + d4);
            if (SwingMousePageTurn.this.currentGUI.getDragTop())
              SwingMousePageTurn.this.decode_pdf.setUserOffsets((int)paramPoint2.getX(), (int)paramPoint2.getY(), 996);
            else
              SwingMousePageTurn.this.decode_pdf.setUserOffsets((int)paramPoint2.getX(), (int)paramPoint2.getY(), 998);
            if (j < 32)
              j *= 2;
            try
            {
              Thread.sleep(50L);
            }
            catch (Exception localException)
            {
              localException.printStackTrace();
            }
          }
          if (i == 0)
          {
            int k = SwingMousePageTurn.this.commonValues.getCurrentPage() - 2;
            if (k == 0)
              k = 1;
            SwingMousePageTurn.this.commonValues.setCurrentPage(k);
            SwingMousePageTurn.this.currentGUI.setPageNumber();
            SwingMousePageTurn.this.decode_pdf.setPageParameters(SwingMousePageTurn.this.currentGUI.getScaling(), SwingMousePageTurn.this.commonValues.getCurrentPage());
            SwingMousePageTurn.this.currentGUI.decodePage();
          }
          SwingMousePageTurn.this.decode_pdf.setUserOffsets(0, 0, 995);
          SwingMousePageTurn.this.currentCommands.setPageTurnAnimating(false);
        }
      };
      ((Thread)localObject).setDaemon(true);
      this.currentCommands.setPageTurnAnimating(true);
      ((Thread)localObject).start();
      this.previewTurnover = false;
    }
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.gui.swing.SwingMousePageTurn
 * JD-Core Version:    0.6.2
 */