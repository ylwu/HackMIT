package org.jpedal.display.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.AffineTransform;
import java.util.Map;
import javafx.scene.layout.Pane;
import javax.swing.JScrollBar;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import org.jpedal.PdfDecoder;
import org.jpedal.display.Display;
import org.jpedal.display.Display.BoolValue;
import org.jpedal.display.PageOffsets;
import org.jpedal.examples.viewer.gui.SwingGUI;
import org.jpedal.examples.viewer.gui.generic.GUIThumbnailPanel;
import org.jpedal.objects.PdfPageData;
import org.jpedal.objects.acroforms.AcroRenderer;
import org.jpedal.render.DynamicVectorRenderer;
import org.jpedal.text.TextLines;
import org.jpedal.utils.Messages;

public class PageFlowDisplay extends PageFlow
  implements Display
{
  private final SwingGUI swingGui;

  public PageFlowDisplay(int paramInt, Object paramObject, final PdfDecoder paramPdfDecoder)
  {
    super(paramInt, paramPdfDecoder);
    this.swingGui = ((SwingGUI)paramObject);
    setPageListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        PageFlowDisplay.this.swingGui.setPage(Integer.valueOf(paramAnonymousActionEvent.getActionCommand()).intValue());
      }
    });
    setMessageListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        PageFlowDisplay.this.swingGui.showMessageDialog(paramAnonymousActionEvent.getActionCommand());
      }
    });
    setCursors(this.swingGui.getCursorImageForFX(1), this.swingGui.getCursorImageForFX(2));
    paramPdfDecoder.removeAll();
    ((JSplitPane)this.swingGui.getDisplayPane()).getRightComponent().addComponentListener(new ComponentAdapter()
    {
      public void componentResized(ComponentEvent paramAnonymousComponentEvent)
      {
        Dimension localDimension = ((JSplitPane)PageFlowDisplay.this.swingGui.getDisplayPane()).getRightComponent().getSize();
        localDimension.setSize(localDimension.getWidth() - 2.0D, localDimension.getHeight());
        paramPdfDecoder.setSize(localDimension);
      }
    });
    Dimension localDimension = ((JSplitPane)this.swingGui.getDisplayPane()).getRightComponent().getSize();
    localDimension.setSize(localDimension.getWidth() - 2.0D, localDimension.getHeight());
    paramPdfDecoder.setSize(localDimension);
    paramPdfDecoder.setLayout(new BorderLayout());
    try
    {
      paramPdfDecoder.add("Center", this);
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      this.swingGui.showMessageDialog(Messages.getMessage("PdfViewer.PageFlowIllegalArgument"));
      if (SwingUtilities.isEventDispatchThread())
      {
        paramPdfDecoder.setDisplayView(1, 2);
      }
      else
      {
        Runnable local4 = new Runnable()
        {
          public void run()
          {
            paramPdfDecoder.setDisplayView(1, 2);
          }
        };
        SwingUtilities.invokeLater(local4);
      }
    }
  }

  public void init(float paramFloat, int paramInt1, int paramInt2, DynamicVectorRenderer paramDynamicVectorRenderer, boolean paramBoolean)
  {
    super.setRotation(paramInt1);
  }

  public int[] getPageSize(int paramInt)
  {
    int[] arrayOfInt = new int[2];
    arrayOfInt[0] = this.pdf.getSize().width;
    arrayOfInt[1] = this.pdf.getSize().height;
    return arrayOfInt;
  }

  public void stopGeneratingPage()
  {
  }

  public int getWidthForPage(int paramInt)
  {
    return this.pageData.getCropBoxWidth(paramInt);
  }

  public void setScaling(float paramFloat)
  {
    if (this.pdf.getPageNumber() != this.pageNumber)
      goTo(this.pdf.getPageNumber());
  }

  public final void decodeOtherPages(int paramInt1, int paramInt2)
  {
  }

  public void refreshDisplay()
  {
  }

  public int getXCordForPage(int paramInt)
  {
    return 0;
  }

  public int getYCordForPage(int paramInt)
  {
    return 0;
  }

  public int getYCordForPage(int paramInt, float paramFloat)
  {
    return 0;
  }

  public void resetToDefaultClip()
  {
  }

  public void flushPageCaches()
  {
  }

  public void resetCachedValues()
  {
  }

  public void clearAdditionalPages()
  {
  }

  public void setThumbnailPanel(GUIThumbnailPanel paramGUIThumbnailPanel)
  {
  }

  public void addAdditionalPage(DynamicVectorRenderer paramDynamicVectorRenderer, int paramInt1, int paramInt2)
  {
  }

  public void setAcceleration(boolean paramBoolean)
  {
  }

  public void disableScreen()
  {
  }

  public void setPageFlowBar(JScrollBar paramJScrollBar)
  {
  }

  public void completeForm(Graphics2D paramGraphics2D)
  {
  }

  public int getEndPage()
  {
    return 0;
  }

  public int getXCordForPage(int paramInt, float paramFloat)
  {
    return 0;
  }

  public int getStartPage()
  {
    return 0;
  }

  public void drawBorder()
  {
  }

  public void setPageOffsets(int paramInt)
  {
  }

  public void initRenderer(Map paramMap, Graphics2D paramGraphics2D)
  {
  }

  public void setThumbnailsDrawing(boolean paramBoolean)
  {
  }

  public void setup(boolean paramBoolean, PageOffsets paramPageOffsets)
  {
  }

  public void setObjectValue(int paramInt, Object paramObject)
  {
  }

  public int[] getHighlightedImage()
  {
    return new int[0];
  }

  public void setHighlightedImage(int[] paramArrayOfInt)
  {
  }

  public JScrollBar getScroll()
  {
    return null;
  }

  public void disposeScroll()
  {
  }

  public float getOldScaling()
  {
    return 0.0F;
  }

  public boolean getBoolean(Display.BoolValue paramBoolValue)
  {
    return false;
  }

  public void setBoolean(Display.BoolValue paramBoolValue, boolean paramBoolean)
  {
  }

  public double getIndent()
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public Rectangle getCursorBoxOnScreen()
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void setCursorBoxOnScreen(Rectangle paramRectangle, boolean paramBoolean)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void forceRedraw()
  {
  }

  public void setPageRotation(int paramInt)
  {
  }

  public void resetMultiPageForms(int paramInt)
  {
  }

  public void resetViewableArea()
  {
  }

  public void paintPage(Graphics2D paramGraphics2D, AcroRenderer paramAcroRenderer, TextLines paramTextLines)
  {
  }

  public void updateCursorBoxOnScreen(Rectangle paramRectangle, Color paramColor, int paramInt1, int paramInt2, int paramInt3)
  {
  }

  public void drawCursor(Graphics paramGraphics, float paramFloat)
  {
  }

  public AffineTransform setViewableArea(Rectangle paramRectangle)
  {
    return null;
  }

  public void drawFacing(Rectangle paramRectangle)
  {
  }

  public void paintPage(Pane paramPane, AcroRenderer paramAcroRenderer, TextLines paramTextLines)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.display.swing.PageFlowDisplay
 * JD-Core Version:    0.6.2
 */