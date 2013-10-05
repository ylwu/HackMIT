package org.jpedal.examples.viewer.gui.swing;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import org.jpedal.PdfDecoder;
import org.jpedal.ThumbnailDecoder;
import org.jpedal.examples.viewer.Values;
import org.jpedal.examples.viewer.gui.generic.GUIThumbnailPanel;
import org.jpedal.objects.PdfPageData;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.SwingWorker;
import org.jpedal.utils.repositories.Vector_Object;

public class SwingThumbnailPanel extends JScrollPane
  implements GUIThumbnailPanel
{
  static final boolean debugThumbnails = false;
  SwingWorker worker = null;
  JPanel panel = new JPanel();
  private ThumbPainter painter = new ThumbPainter(null);
  private boolean showThumbnailsdefault = true;
  private boolean showThumbnails = this.showThumbnailsdefault;
  public boolean interrupt = false;
  public boolean drawing;
  public boolean generateOtherVisibleThumbnails = false;
  public ThumbnailDecoder thumbDecoder;
  private JButton[] pageButton;
  private BufferedImage[] images;
  private boolean[] buttonDrawn;
  private boolean[] isLandscape;
  private int[] pageHeight;
  private static final int thumbH = 100;
  private static final int thumbW = 70;
  Values commonValues;
  final PdfDecoder decode_pdf;
  boolean isExtractor = false;
  private int lastPage = -1;

  public SwingThumbnailPanel(Values paramValues, final PdfDecoder paramPdfDecoder)
  {
    setHorizontalScrollBarPolicy(30);
    setVerticalScrollBarPolicy(20);
    this.commonValues = paramValues;
    this.decode_pdf = paramPdfDecoder;
    this.thumbDecoder = new ThumbnailDecoder(paramPdfDecoder);
    addComponentListener(new ComponentListener()
    {
      public void componentResized(ComponentEvent paramAnonymousComponentEvent)
      {
        if (!SwingThumbnailPanel.this.isExtractor)
        {
          if (SwingThumbnailPanel.this.drawing)
            SwingThumbnailPanel.this.terminateDrawing();
          paramPdfDecoder.waitForDecodingToFinish();
          if (paramPdfDecoder.isOpen())
            SwingThumbnailPanel.this.drawThumbnails();
        }
      }

      public void componentMoved(ComponentEvent paramAnonymousComponentEvent)
      {
      }

      public void componentShown(ComponentEvent paramAnonymousComponentEvent)
      {
      }

      public void componentHidden(ComponentEvent paramAnonymousComponentEvent)
      {
      }
    });
  }

  public void generateOtherThumbnails(String[] paramArrayOfString, Vector_Object paramVector_Object)
  {
    this.drawing = true;
    getViewport().removeAll();
    this.panel.removeAll();
    int i = paramArrayOfString.length;
    getViewport().add(this.panel);
    this.panel.setLayout(new GridLayout(i, 1, 0, 10));
    for (int j = 0; j < i; j++)
      try
      {
        if (paramArrayOfString[j] != null)
        {
          Object localObject = paramVector_Object.elementAt(j);
          this.decode_pdf.waitForDecodingToFinish();
          BufferedImage localBufferedImage;
          if (localObject == null)
          {
            localBufferedImage = ImageIO.read(new File(paramArrayOfString[j]));
            paramVector_Object.addElement(localBufferedImage);
          }
          else
          {
            localBufferedImage = (BufferedImage)localObject;
          }
          if (localBufferedImage != null)
          {
            int k = localBufferedImage.getWidth();
            int m = localBufferedImage.getHeight();
            Graphics2D localGraphics2D = (Graphics2D)localBufferedImage.getGraphics();
            localGraphics2D.setColor(Color.black);
            localGraphics2D.draw(new Rectangle(0, 0, k - 1, m - 1));
            ImageIcon localImageIcon;
            if (m > k)
              localImageIcon = new ImageIcon(localBufferedImage.getScaledInstance(-1, 100, 2));
            else
              localImageIcon = new ImageIcon(localBufferedImage.getScaledInstance(100, -1, 2));
            this.pageButton[j].setIcon(localImageIcon);
            this.pageButton[j].setVisible(true);
            this.buttonDrawn[j] = true;
            this.panel.add(this.pageButton[j]);
          }
        }
      }
      catch (Exception localException)
      {
        LogWriter.writeLog("Exception " + localException + " loading " + paramArrayOfString[j]);
      }
    this.drawing = false;
    this.panel.setVisible(true);
  }

  public synchronized void generateOtherVisibleThumbnails(final int paramInt)
  {
    try
    {
      this.generateOtherVisibleThumbnails = true;
      if ((paramInt == -1) || (paramInt == this.lastPage) || (this.pageButton == null))
        return;
      this.lastPage = paramInt;
      int i = this.decode_pdf.getPageCount();
      for (int j = 0; j < i; j++)
      {
        if (!this.generateOtherVisibleThumbnails)
          return;
        if ((j != paramInt - 1) && (j < this.pageButton.length))
          this.pageButton[j].setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
      }
      if (paramInt - 1 < this.pageButton.length)
      {
        if ((i > 1) && (paramInt > 0))
          this.pageButton[(paramInt - 1)].setBorder(BorderFactory.createLineBorder(Color.red));
        Rectangle localRectangle1 = this.panel.getVisibleRect();
        if (!localRectangle1.contains(this.pageButton[(paramInt - 1)].getLocation()))
          if (SwingUtilities.isEventDispatchThread())
          {
            Rectangle localRectangle2 = new Rectangle(this.pageButton[(paramInt - 1)].getLocation().x, this.pageButton[(paramInt - 1)].getLocation().y, this.pageButton[(paramInt - 1)].getBounds().width, this.pageButton[(paramInt - 1)].getBounds().height);
            this.panel.scrollRectToVisible(localRectangle2);
          }
          else
          {
            SwingUtilities.invokeAndWait(new Runnable()
            {
              public void run()
              {
                if (!SwingThumbnailPanel.this.generateOtherVisibleThumbnails)
                  return;
                Rectangle localRectangle = new Rectangle(SwingThumbnailPanel.this.pageButton[(paramInt - 1)].getLocation().x, SwingThumbnailPanel.this.pageButton[(paramInt - 1)].getLocation().y, SwingThumbnailPanel.this.pageButton[(paramInt - 1)].getBounds().width, SwingThumbnailPanel.this.pageButton[(paramInt - 1)].getBounds().height);
                if (!SwingThumbnailPanel.this.generateOtherVisibleThumbnails)
                  return;
                SwingThumbnailPanel.this.panel.scrollRectToVisible(localRectangle);
              }
            });
          }
      }
      if (!this.generateOtherVisibleThumbnails)
        return;
      if (this.drawing)
        terminateDrawing();
      if (!this.generateOtherVisibleThumbnails)
        return;
      drawThumbnails();
    }
    catch (InterruptedException localInterruptedException)
    {
    }
    catch (InvocationTargetException localInvocationTargetException)
    {
    }
  }

  public void drawThumbnails()
  {
    if (!isEnabled())
      return;
    if (this.decode_pdf.isLoadingLinearizedPDF())
      return;
    if (this.drawing)
      terminateDrawing();
    this.worker = new SwingWorker()
    {
      public Object construct()
      {
        SwingThumbnailPanel.this.drawing = true;
        try
        {
          Rectangle localRectangle = SwingThumbnailPanel.this.panel.getVisibleRect();
          int i = SwingThumbnailPanel.this.decode_pdf.getPageCount();
          for (int j = 0; j < i; j++)
          {
            SwingThumbnailPanel.this.decode_pdf.waitForDecodingToFinish();
            if (SwingThumbnailPanel.this.interrupt)
            {
              j = i;
            }
            else if ((SwingThumbnailPanel.this.buttonDrawn != null) && (SwingThumbnailPanel.this.pageButton != null) && (localRectangle != null) && (SwingThumbnailPanel.this.buttonDrawn[j] == 0) && (SwingThumbnailPanel.this.pageButton[j] != null) && (localRectangle.intersects(SwingThumbnailPanel.this.pageButton[j].getBounds())))
            {
              int k = 100;
              if (SwingThumbnailPanel.this.isLandscape[j] != 0)
                k = 70;
              BufferedImage localBufferedImage = SwingThumbnailPanel.this.thumbDecoder.getPageAsThumbnail(j + 1, k);
              if (!SwingThumbnailPanel.this.interrupt)
                SwingThumbnailPanel.this.createThumbnail(localBufferedImage, j + 1);
            }
          }
        }
        catch (Exception localException)
        {
          localException.printStackTrace();
        }
        SwingThumbnailPanel.this.interrupt = false;
        SwingThumbnailPanel.this.drawing = false;
        return null;
      }
    };
    this.worker.start();
  }

  private static BufferedImage createBlankThumbnail(int paramInt1, int paramInt2)
  {
    BufferedImage localBufferedImage = new BufferedImage(paramInt1 + 1, paramInt2 + 1, 1);
    Graphics2D localGraphics2D = (Graphics2D)localBufferedImage.getGraphics();
    localGraphics2D.setColor(Color.white);
    localGraphics2D.fill(new Rectangle(0, 0, paramInt1, paramInt2));
    localGraphics2D.setColor(Color.black);
    localGraphics2D.draw(new Rectangle(0, 0, paramInt1, paramInt2));
    localGraphics2D.drawLine(0, 0, paramInt1, paramInt2);
    localGraphics2D.drawLine(0, paramInt2, paramInt1, 0);
    return localBufferedImage;
  }

  public BufferedImage getThumbnail(int paramInt)
  {
    if ((this.pageButton == null) || (this.pageButton[paramInt] == null))
      return null;
    return (BufferedImage)((ImageIcon)this.pageButton[paramInt].getIcon()).getImage();
  }

  public synchronized BufferedImage getImage(int paramInt)
  {
    paramInt--;
    if ((this.images == null) || (this.images[paramInt] == null))
    {
      if (paramInt > -1)
      {
        int i = 100;
        if (this.isLandscape[paramInt] != 0)
          i = 70;
        BufferedImage localBufferedImage = this.thumbDecoder.getPageAsThumbnail(paramInt + 1, i);
        this.images[paramInt] = localBufferedImage;
        return localBufferedImage;
      }
      return null;
    }
    return this.images[paramInt];
  }

  private void createThumbnail(BufferedImage paramBufferedImage, int paramInt)
  {
    paramInt--;
    if (paramBufferedImage != null)
    {
      Graphics2D localGraphics2D = (Graphics2D)paramBufferedImage.getGraphics();
      localGraphics2D.setColor(Color.black);
      localGraphics2D.draw(new Rectangle(0, 0, paramBufferedImage.getWidth() - 1, paramBufferedImage.getHeight() - 1));
      final ImageIcon localImageIcon = new ImageIcon(paramBufferedImage);
      if (SwingUtilities.isEventDispatchThread())
      {
        this.pageButton[paramInt].setIcon(localImageIcon);
        this.buttonDrawn[paramInt] = true;
      }
      else
      {
        final int i = paramInt;
        Runnable local4 = new Runnable()
        {
          public void run()
          {
            SwingThumbnailPanel.this.pageButton[i].setIcon(localImageIcon);
            SwingThumbnailPanel.this.buttonDrawn[i] = 1;
          }
        };
        SwingUtilities.invokeLater(local4);
      }
    }
  }

  public void setupThumbnails(int paramInt1, int[] paramArrayOfInt, int paramInt2)
  {
    this.isExtractor = true;
    this.lastPage = -1;
    Font localFont = new Font("Serif", 0, 12);
    getVerticalScrollBar().setUnitIncrement(80);
    BufferedImage localBufferedImage = createBlankThumbnail(70, 100);
    ImageIcon localImageIcon = new ImageIcon(localBufferedImage.getScaledInstance(-1, 100, 4));
    this.isLandscape = new boolean[paramInt1];
    this.pageHeight = new int[paramInt1];
    this.pageButton = new JButton[paramInt1];
    this.images = new BufferedImage[paramInt1];
    this.buttonDrawn = new boolean[paramInt1];
    for (int i = 0; i < paramInt1; i++)
    {
      int j = i + 1;
      if (paramInt2 < 2)
        this.pageButton[i] = new JButton(String.valueOf(j), localImageIcon);
      else
        this.pageButton[i] = new JButton(String.valueOf(j) + " ( Page " + paramArrayOfInt[i] + " )", localImageIcon);
      this.isLandscape[i] = false;
      this.pageHeight[i] = 100;
      this.pageButton[i].setVerticalTextPosition(3);
      this.pageButton[i].setHorizontalTextPosition(0);
      if ((i == 0) && (paramInt1 > 1))
        this.pageButton[0].setBorder(BorderFactory.createLineBorder(Color.red));
      else
        this.pageButton[i].setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
      this.pageButton[i].setFont(localFont);
    }
  }

  public void resetHighlightedThumbnail(int paramInt)
  {
    if (this.pageButton != null)
    {
      int i = this.pageButton.length;
      for (int j = 0; j < i; j++)
        if (j == paramInt)
          this.pageButton[j].setBorder(BorderFactory.createLineBorder(Color.red));
        else
          this.pageButton[j].setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }
  }

  public void setupThumbnails(int paramInt, Font paramFont, String paramString, PdfPageData paramPdfPageData)
  {
    this.lastPage = -1;
    getViewport().removeAll();
    this.panel.removeAll();
    getViewport().add(this.panel);
    this.panel.setLayout(new GridLayout(paramInt, 1, 0, 10));
    this.panel.scrollRectToVisible(new Rectangle(0, 0, 1, 1));
    getVerticalScrollBar().setUnitIncrement(80);
    BufferedImage localBufferedImage1 = createBlankThumbnail(70, 100);
    BufferedImage localBufferedImage2 = createBlankThumbnail(100, 70);
    ImageIcon localImageIcon1 = new ImageIcon(localBufferedImage2.getScaledInstance(-1, 70, 4));
    ImageIcon localImageIcon2 = new ImageIcon(localBufferedImage1.getScaledInstance(-1, 100, 4));
    this.isLandscape = new boolean[paramInt];
    this.pageHeight = new int[paramInt];
    this.pageButton = new JButton[paramInt];
    this.images = new BufferedImage[paramInt];
    this.buttonDrawn = new boolean[paramInt];
    for (int i = 0; i < paramInt; i++)
    {
      int j = i + 1;
      int m = paramPdfPageData.getCropBoxWidth(j);
      int n = paramPdfPageData.getCropBoxHeight(j);
      int i1 = paramPdfPageData.getRotation(j);
      int k;
      ImageIcon localImageIcon3;
      ImageIcon localImageIcon4;
      if (((i1 == 0 ? 1 : 0) | (i1 == 180 ? 1 : 0)) != 0)
      {
        k = paramPdfPageData.getMediaBoxHeight(j);
        localImageIcon3 = localImageIcon1;
        localImageIcon4 = localImageIcon2;
      }
      else
      {
        k = paramPdfPageData.getMediaBoxWidth(j);
        localImageIcon3 = localImageIcon2;
        localImageIcon4 = localImageIcon1;
      }
      if (m > n)
      {
        this.pageButton[i] = new JButton(paramString + ' ' + j, localImageIcon3);
        this.isLandscape[i] = true;
        this.pageHeight[i] = k;
      }
      else
      {
        this.pageButton[i] = new JButton(paramString + ' ' + j, localImageIcon4);
        this.isLandscape[i] = false;
        this.pageHeight[i] = k;
      }
      this.pageButton[i].setVerticalTextPosition(3);
      this.pageButton[i].setHorizontalTextPosition(0);
      if ((i == 0) && (paramInt > 1))
        this.pageButton[0].setBorder(BorderFactory.createLineBorder(Color.red));
      else
        this.pageButton[i].setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
      this.pageButton[i].setFont(paramFont);
      this.panel.add(this.pageButton[i], "Center");
    }
  }

  public Object[] getButtons()
  {
    return this.pageButton;
  }

  public void setThumbnailsEnabled(boolean paramBoolean)
  {
    this.showThumbnailsdefault = paramBoolean;
    this.showThumbnails = paramBoolean;
  }

  public boolean isShownOnscreen()
  {
    return this.showThumbnails;
  }

  public void resetToDefault()
  {
    this.showThumbnails = this.showThumbnailsdefault;
  }

  public void setIsDisplayedOnscreen(boolean paramBoolean)
  {
    this.showThumbnails = paramBoolean;
  }

  public void addComponentListener()
  {
    this.panel.addComponentListener(this.painter);
  }

  public void removeAllListeners()
  {
    this.panel.removeComponentListener(this.painter);
    Object[] arrayOfObject1 = getButtons();
    if (arrayOfObject1 != null)
      for (Object localObject : arrayOfObject1)
      {
        ActionListener[] arrayOfActionListener1 = ((JButton)localObject).getActionListeners();
        for (ActionListener localActionListener : arrayOfActionListener1)
          ((JButton)localObject).removeActionListener(localActionListener);
      }
  }

  public void terminateDrawing()
  {
    this.generateOtherVisibleThumbnails = false;
    if (this.drawing)
    {
      this.interrupt = true;
      while (this.drawing)
        try
        {
          Thread.sleep(20L);
        }
        catch (InterruptedException localInterruptedException)
        {
          localInterruptedException.printStackTrace();
        }
      this.interrupt = false;
    }
  }

  public void refreshDisplay()
  {
    validate();
  }

  public void dispose()
  {
    removeAll();
    this.worker = null;
    if (this.panel != null)
      this.panel.removeAll();
    this.panel = null;
    this.painter = null;
    this.thumbDecoder = null;
    this.pageButton = null;
    this.buttonDrawn = null;
    this.isLandscape = null;
    this.pageHeight = null;
    this.images = null;
  }

  private class ThumbPainter extends ComponentAdapter
  {
    boolean requestMade = false;
    Timer trapMultipleMoves = new Timer(250, new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        if (!SwingThumbnailPanel.ThumbPainter.this.requestMade)
        {
          SwingThumbnailPanel.ThumbPainter.this.requestMade = true;
          if (!Values.isProcessing())
          {
            if (SwingThumbnailPanel.this.drawing)
              SwingThumbnailPanel.this.terminateDrawing();
            SwingThumbnailPanel.ThumbPainter.this.requestMade = false;
            SwingThumbnailPanel.this.drawThumbnails();
          }
        }
      }
    });

    private ThumbPainter()
    {
    }

    public void componentMoved(ComponentEvent paramComponentEvent)
    {
      if (this.trapMultipleMoves.isRunning())
        this.trapMultipleMoves.stop();
      this.trapMultipleMoves.setRepeats(false);
      this.trapMultipleMoves.start();
    }
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.gui.swing.SwingThumbnailPanel
 * JD-Core Version:    0.6.2
 */