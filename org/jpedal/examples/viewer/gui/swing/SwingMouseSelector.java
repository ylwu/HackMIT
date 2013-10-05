package org.jpedal.examples.viewer.gui.swing;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.PrintStream;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.RepaintManager;
import javax.swing.filechooser.FileFilter;
import org.jpedal.PdfDecoder;
import org.jpedal.display.Display;
import org.jpedal.display.swing.SingleDisplay;
import org.jpedal.examples.viewer.Commands;
import org.jpedal.examples.viewer.Values;
import org.jpedal.examples.viewer.Viewer;
import org.jpedal.examples.viewer.gui.GUI;
import org.jpedal.examples.viewer.gui.SwingGUI;
import org.jpedal.examples.viewer.utils.PropertiesFile;
import org.jpedal.exception.PdfException;
import org.jpedal.grouping.PdfGroupingAlgorithms;
import org.jpedal.io.Speech;
import org.jpedal.objects.PdfPageData;
import org.jpedal.parser.DecoderOptions;
import org.jpedal.render.DynamicVectorRenderer;
import org.jpedal.text.TextLines;
import org.jpedal.utils.Messages;

public class SwingMouseSelector
  implements SwingMouseFunctionality
{
  private PdfDecoder decode_pdf;
  private SwingGUI currentGUI;
  private Values commonValues;
  private Commands currentCommands;
  public static boolean activateMultipageHighlight = true;
  private int clickCount = 0;
  private long lastTime = -1L;
  private int pageMouseIsOver = -1;
  private int pageOfHighlight = -1;
  private boolean startHighlighting = false;
  public int id = -1;
  public int lastId = -1;
  private int old_m_x2 = -1;
  private int old_m_y2 = -1;
  boolean altIsDown = false;
  private JPopupMenu rightClick = new JPopupMenu();
  private boolean menuCreated = false;
  JMenuItem copy;
  JMenuItem selectAll;
  JMenuItem deselectall;
  JMenu extract;
  JMenuItem extractText;
  JMenuItem extractImage;
  ImageIcon snapshotIcon;
  JMenuItem snapShot;
  JMenuItem find;
  JMenuItem speakHighlighted;

  public SwingMouseSelector(PdfDecoder paramPdfDecoder, SwingGUI paramSwingGUI, Values paramValues, Commands paramCommands)
  {
    this.decode_pdf = paramPdfDecoder;
    this.currentGUI = paramSwingGUI;
    this.commonValues = paramValues;
    this.currentCommands = paramCommands;
  }

  public void mouseClicked(MouseEvent paramMouseEvent)
  {
    if ((this.decode_pdf.getDisplayView() == 1) || (activateMultipageHighlight))
    {
      long l = new Date().getTime();
      if (this.lastTime + 500L < l)
        this.clickCount = 0;
      this.lastTime = l;
      if ((paramMouseEvent.getButton() == 1) || (paramMouseEvent.getButton() == 0))
      {
        if (this.clickCount != 4)
          this.clickCount += 1;
        Point localPoint = getCoordsOnPage(paramMouseEvent.getX(), paramMouseEvent.getY(), this.commonValues.getCurrentPage());
        int i = (int)localPoint.getX();
        int j = (int)localPoint.getY();
        if (this.decode_pdf.getDisplayView() == 1)
          this.id = this.decode_pdf.getDynamicRenderer().isInsideImage(i, j);
        else
          this.id = -1;
        Object localObject;
        int m;
        if ((this.lastId != this.id) && (this.id != -1))
        {
          localObject = this.decode_pdf.getDynamicRenderer().getArea(this.id);
          if (localObject != null)
          {
            int k = ((Rectangle)localObject).height;
            m = ((Rectangle)localObject).width;
            int n = ((Rectangle)localObject).x;
            int i1 = ((Rectangle)localObject).y;
            this.decode_pdf.getDynamicRenderer().setneedsHorizontalInvert(false);
            this.decode_pdf.getDynamicRenderer().setneedsVerticalInvert(false);
            if (m < 0)
            {
              this.decode_pdf.getDynamicRenderer().setneedsHorizontalInvert(true);
              m = -m;
              n -= m;
            }
            if (k < 0)
            {
              this.decode_pdf.getDynamicRenderer().setneedsVerticalInvert(true);
              k = -k;
              i1 -= k;
            }
            this.decode_pdf.getPages().setHighlightedImage(new int[] { n, i1, m, k });
            this.decode_pdf.repaint(n, i1, m, k);
          }
          this.lastId = this.id;
        }
        else
        {
          this.decode_pdf.getPages().setHighlightedImage(null);
          this.decode_pdf.repaint();
          this.lastId = -1;
        }
        if ((this.id == -1) && (this.clickCount > 1))
          switch (this.clickCount)
          {
          case 1:
            break;
          case 2:
            localObject = this.decode_pdf.getTextLines().getLineAreas(this.pageMouseIsOver);
            Rectangle localRectangle1 = new Rectangle(i, j, 1, 1);
            if (localObject != null)
              for (m = 0; m != localObject.length; m++)
                if (localObject[m].intersects(localRectangle1))
                {
                  this.decode_pdf.updateCursorBoxOnScreen(localObject[m], DecoderOptions.highlightColor);
                  this.decode_pdf.getTextLines().addHighlights(new Rectangle[] { localObject[m] }, false, this.pageMouseIsOver);
                }
            break;
          case 3:
            Rectangle localRectangle2 = this.decode_pdf.getTextLines().setFoundParagraph(i, j, this.pageMouseIsOver);
            if (localRectangle2 != null)
              this.decode_pdf.updateCursorBoxOnScreen(localRectangle2, DecoderOptions.highlightColor);
            break;
          case 4:
            this.currentGUI.currentCommands.executeCommand(26, null);
          }
      }
      else if ((paramMouseEvent.getButton() == 2) || (paramMouseEvent.getButton() != 3));
    }
  }

  public void mousePressed(MouseEvent paramMouseEvent)
  {
    if (((this.decode_pdf.getDisplayView() == 1) || (activateMultipageHighlight)) && ((paramMouseEvent.getButton() == 1) || (paramMouseEvent.getButton() == 0)))
    {
      this.decode_pdf.updateCursorBoxOnScreen(null, null);
      this.decode_pdf.getPages().setHighlightedImage(null);
      this.decode_pdf.getTextLines().clearHighlights();
      this.decode_pdf.grabFocus();
      Point localPoint = getCoordsOnPage(paramMouseEvent.getX(), paramMouseEvent.getY(), this.commonValues.getCurrentPage());
      this.commonValues.m_x1 = ((int)localPoint.getX());
      this.commonValues.m_y1 = ((int)localPoint.getY());
    }
  }

  public void mouseReleased(MouseEvent paramMouseEvent)
  {
    if ((this.decode_pdf.getDisplayView() == 1) || (activateMultipageHighlight))
      if ((paramMouseEvent.getButton() == 1) || (paramMouseEvent.getButton() == 0))
      {
        if (this.startHighlighting)
          this.startHighlighting = false;
        repaintArea(new Rectangle(this.commonValues.m_x1 - this.currentGUI.cropX, this.commonValues.m_y2 + this.currentGUI.cropY, this.commonValues.m_x2 - this.commonValues.m_x1 + this.currentGUI.cropX, this.commonValues.m_y1 - this.commonValues.m_y2 + this.currentGUI.cropY), this.currentGUI.mediaH);
        this.decode_pdf.repaint();
        if (this.currentCommands.extractingAsImage)
        {
          this.decode_pdf.updateCursorBoxOnScreen(null, null);
          this.decode_pdf.getTextLines().clearHighlights();
          this.decode_pdf.getPages().setHighlightedImage(null);
          this.decode_pdf.setCursor(Cursor.getPredefinedCursor(0));
          this.currentGUI.currentCommands.extractSelectedScreenAsImage();
          this.currentCommands.extractingAsImage = false;
          DecoderOptions.showMouseBox = false;
        }
        this.pageOfHighlight = -1;
      }
      else if ((paramMouseEvent.getButton() == 3) && (this.currentGUI.getProperties().getValue("allowRightClick").toLowerCase().equals("true")))
      {
        if (!this.menuCreated)
          createRightClickMenu();
        if (this.decode_pdf.getPages().getHighlightedImage() == null)
          this.extractImage.setEnabled(false);
        else
          this.extractImage.setEnabled(true);
        if (this.decode_pdf.getTextLines().getHighlightedAreas(this.commonValues.getCurrentPage()) == null)
        {
          this.extractText.setEnabled(false);
          this.find.setEnabled(false);
          this.speakHighlighted.setEnabled(false);
          this.copy.setEnabled(false);
        }
        else
        {
          this.extractText.setEnabled(true);
          this.find.setEnabled(true);
          this.speakHighlighted.setEnabled(true);
          this.copy.setEnabled(true);
        }
        if ((this.decode_pdf != null) && (this.decode_pdf.isOpen()))
          this.rightClick.show(this.decode_pdf, paramMouseEvent.getX(), paramMouseEvent.getY());
      }
  }

  public void mouseEntered(MouseEvent paramMouseEvent)
  {
  }

  public void mouseExited(MouseEvent paramMouseEvent)
  {
  }

  public void mouseDragged(MouseEvent paramMouseEvent)
  {
    if ((paramMouseEvent.getButton() == 1) || (paramMouseEvent.getButton() == 0))
    {
      this.altIsDown = paramMouseEvent.isAltDown();
      if (!this.startHighlighting)
        this.startHighlighting = true;
      Point localPoint = getCoordsOnPage(paramMouseEvent.getX(), paramMouseEvent.getY(), this.commonValues.getCurrentPage());
      if (this.pageMouseIsOver == this.pageOfHighlight)
      {
        this.commonValues.m_x2 = ((int)localPoint.getX());
        this.commonValues.m_y2 = ((int)localPoint.getY());
      }
      if (this.commonValues.isPDF())
        generateNewCursorBox();
    }
  }

  public void mouseMoved(MouseEvent paramMouseEvent)
  {
  }

  protected int[] updateXY(int paramInt1, int paramInt2)
  {
    float f = this.currentGUI.getScaling();
    int i = GUI.getPDFDisplayInset();
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

  private void createRightClickMenu()
  {
    this.copy = new JMenuItem(Messages.getMessage("PdfRightClick.copy"));
    this.selectAll = new JMenuItem(Messages.getMessage("PdfRightClick.selectAll"));
    this.deselectall = new JMenuItem(Messages.getMessage("PdfRightClick.deselectAll"));
    this.extract = new JMenu(Messages.getMessage("PdfRightClick.extract"));
    this.extractText = new JMenuItem(Messages.getMessage("PdfRightClick.extractText"));
    this.extractImage = new JMenuItem(Messages.getMessage("PdfRightClick.extractImage"));
    this.snapshotIcon = new ImageIcon(getClass().getResource("/org/jpedal/examples/viewer/res/snapshot_menu.gif"));
    this.snapShot = new JMenuItem(Messages.getMessage("PdfRightClick.snapshot"), this.snapshotIcon);
    this.find = new JMenuItem(Messages.getMessage("PdfRightClick.find"));
    this.speakHighlighted = new JMenuItem("Speak Highlighted text");
    this.rightClick.add(this.copy);
    this.copy.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        if (SwingMouseSelector.this.decode_pdf.getDisplayView() == 1)
          SwingMouseSelector.this.currentGUI.currentCommands.executeCommand(25, null);
        else if (Viewer.showMessages)
          JOptionPane.showMessageDialog(SwingMouseSelector.this.currentGUI.getFrame(), "Copy is only avalible in single page display mode");
      }
    });
    this.rightClick.addSeparator();
    this.rightClick.add(this.selectAll);
    this.selectAll.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        SwingMouseSelector.this.currentGUI.currentCommands.executeCommand(26, null);
      }
    });
    this.rightClick.add(this.deselectall);
    this.deselectall.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        SwingMouseSelector.this.currentGUI.currentCommands.executeCommand(27, null);
      }
    });
    this.rightClick.addSeparator();
    this.rightClick.add(this.extract);
    this.extract.add(this.extractText);
    this.extractText.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        if (SwingMouseSelector.this.decode_pdf.getDisplayView() == 1)
          SwingMouseSelector.this.currentGUI.currentCommands.extractSelectedText();
        else if (Viewer.showMessages)
          JOptionPane.showMessageDialog(SwingMouseSelector.this.currentGUI.getFrame(), "Text Extraction is only avalible in single page display mode");
      }
    });
    this.extract.add(this.extractImage);
    this.extractImage.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        if (SwingMouseSelector.this.decode_pdf.getPages().getHighlightedImage() == null)
        {
          if (Viewer.showMessages)
            JOptionPane.showMessageDialog(SwingMouseSelector.this.decode_pdf, "No image has been selected for extraction.", "No image selected", 0);
        }
        else if (SwingMouseSelector.this.decode_pdf.getDisplayView() == 1)
        {
          JFileChooser localJFileChooser = new JFileChooser();
          FileFilter local1 = new FileFilter()
          {
            public boolean accept(File paramAnonymous2File)
            {
              return (paramAnonymous2File.isDirectory()) || (paramAnonymous2File.getName().toLowerCase().endsWith(".jpg")) || (paramAnonymous2File.getName().toLowerCase().endsWith(".jpeg"));
            }

            public String getDescription()
            {
              return "JPG (*.jpg)";
            }
          };
          FileFilter local2 = new FileFilter()
          {
            public boolean accept(File paramAnonymous2File)
            {
              return (paramAnonymous2File.isDirectory()) || (paramAnonymous2File.getName().toLowerCase().endsWith(".png"));
            }

            public String getDescription()
            {
              return "PNG (*.png)";
            }
          };
          FileFilter local3 = new FileFilter()
          {
            public boolean accept(File paramAnonymous2File)
            {
              return (paramAnonymous2File.isDirectory()) || (paramAnonymous2File.getName().toLowerCase().endsWith(".tif")) || (paramAnonymous2File.getName().toLowerCase().endsWith(".tiff"));
            }

            public String getDescription()
            {
              return "TIF (*.tiff)";
            }
          };
          localJFileChooser.addChoosableFileFilter(local3);
          localJFileChooser.addChoosableFileFilter(local2);
          localJFileChooser.addChoosableFileFilter(local1);
          localJFileChooser.showSaveDialog(null);
          File localFile = localJFileChooser.getSelectedFile();
          int i = 0;
          if (localFile != null)
          {
            String str1 = localFile.getAbsolutePath();
            String str2 = localJFileChooser.getFileFilter().getDescription().substring(0, 3).toLowerCase();
            if (str1.indexOf('.') != -1)
            {
              String str3 = str1.substring(str1.indexOf('.') + 1).toLowerCase();
              if ((str3.equals("jpg")) || (str3.equals("jpeg")))
              {
                str2 = "jpg";
              }
              else if (str3.equals("png"))
              {
                str2 = "png";
              }
              else if ((str3.equals("tif")) || (str3.equals("tiff")))
              {
                str2 = "tiff";
              }
              else
              {
                if (Viewer.showMessages)
                  JOptionPane.showMessageDialog(null, "Sorry, we can not currently save images to ." + str3 + " files.");
                i = 1;
              }
            }
            if (str2.equals("tif"))
              str2 = "tiff";
            if (str2.equals("all"))
              str2 = "png";
            if (!str1.toLowerCase().endsWith('.' + str2))
              str1 = str1 + '.' + str2;
            if (i == 0)
              SwingMouseSelector.this.decode_pdf.getDynamicRenderer().saveImage(SwingMouseSelector.this.id, str1, str2);
          }
        }
      }
    });
    this.extract.add(this.snapShot);
    this.snapShot.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        SwingMouseSelector.this.currentGUI.currentCommands.executeCommand(13, null);
      }
    });
    this.rightClick.addSeparator();
    this.rightClick.add(this.find);
    this.find.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        Rectangle localRectangle = SwingMouseSelector.this.decode_pdf.getCursorBoxOnScreen();
        if (localRectangle == null)
        {
          if (Viewer.showMessages)
            JOptionPane.showMessageDialog(SwingMouseSelector.this.decode_pdf, "There is no text selected.\nPlease highlight the text you wish to search.", "No Text selected", 0);
          return;
        }
        String str1 = SwingMouseSelector.this.currentGUI.showInputDialog(Messages.getMessage("PdfViewerMessage.GetUserInput"));
        if ((str1 == null) || (str1.length() < 1))
          return;
        int i = localRectangle.x;
        int j = localRectangle.x + localRectangle.width;
        int k = localRectangle.y;
        int m = localRectangle.y + localRectangle.height;
        if (k < m)
        {
          n = m;
          m = k;
          k = n;
        }
        if (i > j)
        {
          n = j;
          j = i;
          i = n;
        }
        if (i < SwingMouseSelector.this.currentGUI.cropX)
          i = SwingMouseSelector.this.currentGUI.cropX;
        if (i > SwingMouseSelector.this.currentGUI.mediaW - SwingMouseSelector.this.currentGUI.cropX)
          i = SwingMouseSelector.this.currentGUI.mediaW - SwingMouseSelector.this.currentGUI.cropX;
        if (j < SwingMouseSelector.this.currentGUI.cropX)
          j = SwingMouseSelector.this.currentGUI.cropX;
        if (j > SwingMouseSelector.this.currentGUI.mediaW - SwingMouseSelector.this.currentGUI.cropX)
          j = SwingMouseSelector.this.currentGUI.mediaW - SwingMouseSelector.this.currentGUI.cropX;
        if (k < SwingMouseSelector.this.currentGUI.cropY)
          k = SwingMouseSelector.this.currentGUI.cropY;
        if (k > SwingMouseSelector.this.currentGUI.mediaH - SwingMouseSelector.this.currentGUI.cropY)
          k = SwingMouseSelector.this.currentGUI.mediaH - SwingMouseSelector.this.currentGUI.cropY;
        if (m < SwingMouseSelector.this.currentGUI.cropY)
          m = SwingMouseSelector.this.currentGUI.cropY;
        if (m > SwingMouseSelector.this.currentGUI.mediaH - SwingMouseSelector.this.currentGUI.cropY)
          m = SwingMouseSelector.this.currentGUI.mediaH - SwingMouseSelector.this.currentGUI.cropY;
        if (Viewer.showMessages)
          JOptionPane.showMessageDialog(SwingMouseSelector.this.currentGUI.getFrame(), Messages.getMessage("PdfViewerMessage.FindDemo"));
        str1 = null;
        int n = 0;
        int i1 = SwingMouseSelector.this.currentGUI.showConfirmDialog(Messages.getMessage("PdfViewercase.message"), null, 0);
        if (i1 == 0)
          n |= 2;
        int i2 = SwingMouseSelector.this.currentGUI.showConfirmDialog(Messages.getMessage("PdfViewerfindAll.message"), null, 0);
        if (i2 == 1)
          n |= 4;
        int i3 = SwingMouseSelector.this.currentGUI.showConfirmDialog(Messages.getMessage("PdfViewerfindHyphen.message"), null, 0);
        if (i3 == 0)
          n |= 8;
        if (str1 != null)
          try
          {
            float[] arrayOfFloat = SwingMouseSelector.this.decode_pdf.getGroupingObject().findText(new Rectangle(i, k, j - i, m - k), SwingMouseSelector.this.commonValues.getCurrentPage(), new String[] { str1 }, n);
            if (arrayOfFloat != null)
            {
              if (arrayOfFloat.length < 3)
              {
                SwingMouseSelector.this.currentGUI.showMessageDialog(new StringBuilder().append(Messages.getMessage("PdfViewerMessage.Found")).append(' ').append(arrayOfFloat[0]).append(',').append(arrayOfFloat[1]).toString());
              }
              else
              {
                StringBuilder localStringBuilder = new StringBuilder();
                String str2 = Messages.getMessage("PdfViewerMessage.FoundAt");
                int i4 = 0;
                while (i4 < arrayOfFloat.length)
                {
                  localStringBuilder.append(str2).append(' ');
                  localStringBuilder.append(arrayOfFloat[i4]);
                  localStringBuilder.append(',');
                  localStringBuilder.append(arrayOfFloat[(i4 + 1)]);
                  localStringBuilder.append('\n');
                  if (arrayOfFloat[(i4 + 4)] == -101.0F)
                    str2 = Messages.getMessage("PdfViewerMessage.FoundAtHyphen");
                  else
                    str2 = Messages.getMessage("PdfViewerMessage.FoundAt");
                  i4 += 5;
                }
                SwingMouseSelector.this.currentGUI.showMessageDialog(localStringBuilder.toString());
              }
            }
            else
              SwingMouseSelector.this.currentGUI.showMessageDialog(Messages.getMessage("PdfViewerMessage.NotFound"));
          }
          catch (PdfException localPdfException)
          {
            localPdfException.printStackTrace();
          }
      }
    });
    final boolean bool = Speech.speechAvailible();
    String str = this.currentGUI.getProperties().getValue("voice");
    str = str.substring(0, str.indexOf('('));
    Speech.selectedVoice = str;
    if (bool)
      this.rightClick.addSeparator();
    this.speakHighlighted.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        if (bool)
          if (SwingMouseSelector.this.decode_pdf.getDisplayView() == 1)
          {
            Thread localThread = new Thread(new Runnable()
            {
              public void run()
              {
                String str = SwingMouseSelector.this.currentGUI.currentCommands.copySelectedText();
                Speech.speakText(str);
              }
            });
            localThread.setDaemon(true);
            localThread.start();
          }
          else if (Viewer.showMessages)
          {
            JOptionPane.showMessageDialog(SwingMouseSelector.this.currentGUI.getFrame(), "Speak text is only avalible in single page display mode");
          }
      }
    });
    if (bool)
      this.rightClick.add(this.speakHighlighted);
    this.menuCreated = true;
    this.decode_pdf.add(this.rightClick);
  }

  protected void generateNewCursorBox()
  {
    if (((this.old_m_x2 != -1 ? 1 : 0) | (this.old_m_y2 != -1 ? 1 : 0) | (Math.abs(this.commonValues.m_x2 - this.old_m_x2) > 5 ? 1 : 0) | (Math.abs(this.commonValues.m_y2 - this.old_m_y2) > 5 ? 1 : 0)) != 0)
    {
      int i = this.commonValues.m_x1;
      if (this.commonValues.m_x1 > this.commonValues.m_x2)
        i = this.commonValues.m_x2;
      int j = this.commonValues.m_y1;
      if (this.commonValues.m_y1 > this.commonValues.m_y2)
        j = this.commonValues.m_y2;
      int k = Math.abs(this.commonValues.m_x2 - this.commonValues.m_x1);
      int m = Math.abs(this.commonValues.m_y2 - this.commonValues.m_y1);
      Rectangle localRectangle1 = new Rectangle(i, j, k, m);
      this.decode_pdf.updateCursorBoxOnScreen(localRectangle1, DecoderOptions.highlightColor);
      if (!this.currentCommands.extractingAsImage)
      {
        int n = this.decode_pdf.getDynamicRenderer().getObjectUnderneath(this.commonValues.m_x1, this.commonValues.m_y1);
        if ((this.altIsDown) && (n != 1) && (n != 4) && (n != 5) && (n != 6))
        {
          this.decode_pdf.getTextLines().addHighlights(new Rectangle[] { localRectangle1 }, true, this.pageOfHighlight);
        }
        else
        {
          Rectangle localRectangle2 = new Rectangle(this.commonValues.m_x1, this.commonValues.m_y1, this.commonValues.m_x2 - this.commonValues.m_x1, this.commonValues.m_y2 - this.commonValues.m_y1);
          this.decode_pdf.getTextLines().addHighlights(new Rectangle[] { localRectangle2 }, false, this.pageOfHighlight);
        }
      }
      this.old_m_x2 = this.commonValues.m_x2;
      this.old_m_y2 = this.commonValues.m_y2;
    }
    ((SingleDisplay)this.decode_pdf.getPages()).refreshDisplay();
    this.decode_pdf.repaint();
  }

  private Point getPageCoordsInSingleDisplayMode(int paramInt1, int paramInt2, int paramInt3)
  {
    if (this.currentGUI.useNewLayout)
    {
      int[] arrayOfInt = new int[2];
      arrayOfInt[0] = 1;
      arrayOfInt[1] = 0;
      if (this.currentGUI.getRotation() % 180 == 90)
      {
        i = this.decode_pdf.getPdfPageData().getScaledCropBoxHeight(paramInt3);
        j = this.decode_pdf.getPdfPageData().getScaledCropBoxWidth(paramInt3);
      }
      else
      {
        i = this.decode_pdf.getPdfPageData().getScaledCropBoxWidth(paramInt3);
        j = this.decode_pdf.getPdfPageData().getScaledCropBoxHeight(paramInt3);
      }
      Rectangle localRectangle = new Rectangle(this.decode_pdf.getWidth() / 2 - i / 2, this.decode_pdf.getInsetH(), i, j);
      if (localRectangle.contains(paramInt1, paramInt2))
        arrayOfInt[1] = 1;
      else
        arrayOfInt[1] = 0;
      if ((this.pageOfHighlight == -1) && (this.startHighlighting))
        this.pageOfHighlight = paramInt3;
      this.pageMouseIsOver = paramInt3;
      this.currentGUI.setMultibox(arrayOfInt);
    }
    float f = this.currentGUI.getScaling();
    int i = GUI.getPDFDisplayInset();
    int j = this.currentGUI.getRotation();
    int k = this.currentGUI.AdjustForAlignment(paramInt1) - i;
    int m = paramInt2 - i;
    if (this.commonValues.maxViewY != 0)
    {
      k = (int)((k - this.commonValues.dx * f) / this.commonValues.viewportScale);
      m = (int)((this.currentGUI.mediaH - (this.currentGUI.mediaH - m / f - this.commonValues.dy) / this.commonValues.viewportScale) * f);
    }
    paramInt1 = (int)(k / f);
    paramInt2 = (int)(m / f);
    int n;
    if (j == 90)
    {
      n = paramInt1 + this.currentGUI.cropY;
      paramInt1 = paramInt2 + this.currentGUI.cropX;
      paramInt2 = n;
    }
    else if (j == 180)
    {
      paramInt1 = this.currentGUI.cropW + this.currentGUI.cropX - paramInt1;
      paramInt2 += this.currentGUI.cropY;
    }
    else if (j == 270)
    {
      n = this.currentGUI.cropH + this.currentGUI.cropY - paramInt1;
      paramInt1 = this.currentGUI.cropW + this.currentGUI.cropX - paramInt2;
      paramInt2 = n;
    }
    else
    {
      paramInt1 += this.currentGUI.cropX;
      if (this.decode_pdf.getDisplayView() == 1)
        paramInt2 = this.currentGUI.cropH + this.currentGUI.cropY - paramInt2;
      else
        paramInt2 = this.currentGUI.cropY + paramInt2;
    }
    return new Point(paramInt1, paramInt2);
  }

  private Point getPageCoordsInContinuousDisplayMode(int paramInt1, int paramInt2, int paramInt3)
  {
    SingleDisplay localSingleDisplay = (SingleDisplay)this.decode_pdf.getPages();
    if (this.currentGUI.useNewLayout)
    {
      int[] arrayOfInt = new int[2];
      arrayOfInt[0] = 1;
      arrayOfInt[1] = 0;
      i = this.decode_pdf.getWidth() / 2 - this.decode_pdf.getPdfPageData().getScaledCropBoxWidth(paramInt3) / 2;
      if (i < 0)
        i = 0;
      else
        i -= localSingleDisplay.getXCordForPage(paramInt3);
      Rectangle localRectangle = new Rectangle(localSingleDisplay.getXCordForPage(paramInt3) + i, localSingleDisplay.getYCordForPage(paramInt3), this.decode_pdf.getPdfPageData().getScaledCropBoxWidth(paramInt3), this.decode_pdf.getPdfPageData().getScaledCropBoxHeight(paramInt3));
      if (localRectangle.contains(paramInt1, paramInt2))
        arrayOfInt[1] = 1;
      if (arrayOfInt[1] == 0)
      {
        if ((paramInt2 < localRectangle.y) && (paramInt3 > 1));
        while ((arrayOfInt[1] == 0) && (paramInt3 > 1))
        {
          paramInt3--;
          localRectangle = new Rectangle(localSingleDisplay.getXCordForPage(paramInt3) + i, localSingleDisplay.getYCordForPage(paramInt3), this.decode_pdf.getPdfPageData().getScaledCropBoxWidth(paramInt3), this.decode_pdf.getPdfPageData().getScaledCropBoxHeight(paramInt3));
          if (localRectangle.contains(paramInt1, paramInt2))
          {
            arrayOfInt[1] = 1;
            continue;
            if ((paramInt2 > localRectangle.getMaxY()) && (paramInt3 < this.commonValues.getPageCount()))
              while ((arrayOfInt[1] == 0) && (paramInt3 < this.commonValues.getPageCount()))
              {
                paramInt3++;
                localRectangle = new Rectangle(localSingleDisplay.getXCordForPage(paramInt3) + i, localSingleDisplay.getYCordForPage(paramInt3), this.decode_pdf.getPdfPageData().getScaledCropBoxWidth(paramInt3), this.decode_pdf.getPdfPageData().getScaledCropBoxHeight(paramInt3));
                if (localRectangle.contains(paramInt1, paramInt2))
                  arrayOfInt[1] = 1;
              }
          }
        }
      }
      if ((this.pageOfHighlight == -1) && (this.startHighlighting))
        this.pageOfHighlight = paramInt3;
      this.pageMouseIsOver = paramInt3;
      paramInt2 = localSingleDisplay.getYCordForPage(paramInt3) + this.decode_pdf.getPdfPageData().getScaledCropBoxHeight(paramInt3) + this.decode_pdf.getInsetH() - paramInt2;
      this.currentGUI.setMultibox(arrayOfInt);
    }
    float f = this.currentGUI.getScaling();
    int i = GUI.getPDFDisplayInset();
    int j = this.currentGUI.getRotation();
    int k = this.currentGUI.AdjustForAlignment(paramInt1) - i;
    int m = paramInt2 - i;
    if (this.commonValues.maxViewY != 0)
    {
      k = (int)((k - this.commonValues.dx * f) / this.commonValues.viewportScale);
      m = (int)((this.currentGUI.mediaH - (this.currentGUI.mediaH - m / f - this.commonValues.dy) / this.commonValues.viewportScale) * f);
    }
    paramInt1 = (int)(k / f);
    paramInt2 = (int)(m / f);
    int n;
    if (j == 90)
    {
      n = paramInt1 + this.currentGUI.cropY;
      paramInt1 = paramInt2 + this.currentGUI.cropX;
      paramInt2 = n;
    }
    else if (j == 180)
    {
      paramInt1 = this.currentGUI.cropW + this.currentGUI.cropX - paramInt1;
      paramInt2 += this.currentGUI.cropY;
    }
    else if (j == 270)
    {
      n = this.currentGUI.cropH + this.currentGUI.cropY - paramInt1;
      paramInt1 = this.currentGUI.cropW + this.currentGUI.cropX - paramInt2;
      paramInt2 = n;
    }
    else
    {
      paramInt1 += this.currentGUI.cropX;
      if (this.decode_pdf.getDisplayView() == 1)
        paramInt2 = this.currentGUI.cropH + this.currentGUI.cropY - paramInt2;
      else
        paramInt2 = this.currentGUI.cropY + paramInt2;
    }
    return new Point(paramInt1, paramInt2);
  }

  private Point getPageCoordsInContinuousFacingDisplayMode(int paramInt1, int paramInt2, int paramInt3)
  {
    SingleDisplay localSingleDisplay = (SingleDisplay)this.decode_pdf.getPages();
    if (this.currentGUI.useNewLayout)
    {
      int[] arrayOfInt = new int[2];
      arrayOfInt[0] = 1;
      arrayOfInt[1] = 0;
      if ((paramInt3 != 1) && (paramInt1 > this.decode_pdf.getWidth() / 2) && (paramInt3 < this.commonValues.getPageCount()))
        paramInt3++;
      i = this.decode_pdf.getWidth() / 2 - this.decode_pdf.getPdfPageData().getScaledCropBoxWidth(paramInt3) - this.decode_pdf.getInsetW();
      if (i < 0)
      {
        System.err.println("x adjustment is less than 0");
        i = 0;
      }
      Rectangle localRectangle = new Rectangle(localSingleDisplay.getXCordForPage(paramInt3) + i, localSingleDisplay.getYCordForPage(paramInt3), this.decode_pdf.getPdfPageData().getScaledCropBoxWidth(paramInt3), this.decode_pdf.getPdfPageData().getScaledCropBoxHeight(paramInt3));
      if (localRectangle.contains(paramInt1, paramInt2))
        arrayOfInt[1] = 1;
      if (arrayOfInt[1] == 0)
      {
        if ((paramInt2 < localRectangle.y) && (paramInt3 > 1));
        while ((arrayOfInt[1] == 0) && (paramInt3 > 1))
        {
          paramInt3--;
          i = this.decode_pdf.getWidth() / 2 - this.decode_pdf.getPdfPageData().getScaledCropBoxWidth(paramInt3) - this.decode_pdf.getInsetW();
          if (i < 0)
            i = 0;
          localRectangle = new Rectangle(localSingleDisplay.getXCordForPage(paramInt3) + i, localSingleDisplay.getYCordForPage(paramInt3), this.decode_pdf.getPdfPageData().getScaledCropBoxWidth(paramInt3), this.decode_pdf.getPdfPageData().getScaledCropBoxHeight(paramInt3));
          if (localRectangle.contains(paramInt1, paramInt2))
          {
            arrayOfInt[1] = 1;
            continue;
            if ((paramInt2 > localRectangle.getMaxY()) && (paramInt3 < this.commonValues.getPageCount()))
              while ((arrayOfInt[1] == 0) && (paramInt3 < this.commonValues.getPageCount()))
              {
                paramInt3++;
                i = this.decode_pdf.getWidth() / 2 - this.decode_pdf.getPdfPageData().getScaledCropBoxWidth(paramInt3) - this.decode_pdf.getInsetW();
                if (i < 0)
                  i = 0;
                localRectangle = new Rectangle(localSingleDisplay.getXCordForPage(paramInt3) + i, localSingleDisplay.getYCordForPage(paramInt3), this.decode_pdf.getPdfPageData().getScaledCropBoxWidth(paramInt3), this.decode_pdf.getPdfPageData().getScaledCropBoxHeight(paramInt3));
                if (localRectangle.contains(paramInt1, paramInt2))
                  arrayOfInt[1] = 1;
              }
          }
        }
      }
      if ((this.pageOfHighlight == -1) && (this.startHighlighting))
        this.pageOfHighlight = paramInt3;
      this.pageMouseIsOver = paramInt3;
      paramInt2 = localSingleDisplay.getYCordForPage(paramInt3) + this.decode_pdf.getPdfPageData().getScaledCropBoxHeight(paramInt3) + this.decode_pdf.getInsetH() - paramInt2;
      paramInt1 -= localSingleDisplay.getXCordForPage(paramInt3) - this.decode_pdf.getInsetW();
      this.currentGUI.setMultibox(arrayOfInt);
    }
    float f = this.currentGUI.getScaling();
    int i = GUI.getPDFDisplayInset();
    int j = this.currentGUI.getRotation();
    int k = this.currentGUI.AdjustForAlignment(paramInt1) - i;
    int m = paramInt2 - i;
    if (this.commonValues.maxViewY != 0)
    {
      k = (int)((k - this.commonValues.dx * f) / this.commonValues.viewportScale);
      m = (int)((this.currentGUI.mediaH - (this.currentGUI.mediaH - m / f - this.commonValues.dy) / this.commonValues.viewportScale) * f);
    }
    paramInt1 = (int)(k / f);
    paramInt2 = (int)(m / f);
    int n;
    if (j == 90)
    {
      n = paramInt1 + this.currentGUI.cropY;
      paramInt1 = paramInt2 + this.currentGUI.cropX;
      paramInt2 = n;
    }
    else if (j == 180)
    {
      paramInt1 = this.currentGUI.cropW + this.currentGUI.cropX - paramInt1;
      paramInt2 += this.currentGUI.cropY;
    }
    else if (j == 270)
    {
      n = this.currentGUI.cropH + this.currentGUI.cropY - paramInt1;
      paramInt1 = this.currentGUI.cropW + this.currentGUI.cropX - paramInt2;
      paramInt2 = n;
    }
    else
    {
      paramInt1 += this.currentGUI.cropX;
      if (this.decode_pdf.getDisplayView() == 1)
        paramInt2 = this.currentGUI.cropH + this.currentGUI.cropY - paramInt2;
      else
        paramInt2 = this.currentGUI.cropY + paramInt2;
    }
    return new Point(paramInt1, paramInt2);
  }

  private Point getPageCoordsInFacingDisplayMode(int paramInt1, int paramInt2)
  {
    if (this.currentGUI.useNewLayout)
    {
      int[] arrayOfInt = new int[2];
      arrayOfInt[0] = 1;
      arrayOfInt[1] = 0;
      arrayOfInt[1] = 0;
      this.currentGUI.setMultibox(arrayOfInt);
    }
    float f = this.currentGUI.getScaling();
    int i = GUI.getPDFDisplayInset();
    int j = this.currentGUI.getRotation();
    int k = this.currentGUI.AdjustForAlignment(paramInt1) - i;
    int m = paramInt2 - i;
    if (this.commonValues.maxViewY != 0)
    {
      k = (int)((k - this.commonValues.dx * f) / this.commonValues.viewportScale);
      m = (int)((this.currentGUI.mediaH - (this.currentGUI.mediaH - m / f - this.commonValues.dy) / this.commonValues.viewportScale) * f);
    }
    paramInt1 = (int)(k / f);
    paramInt2 = (int)(m / f);
    int n;
    if (j == 90)
    {
      n = paramInt1 + this.currentGUI.cropY;
      paramInt1 = paramInt2 + this.currentGUI.cropX;
      paramInt2 = n;
    }
    else if (j == 180)
    {
      paramInt1 = this.currentGUI.cropW + this.currentGUI.cropX - paramInt1;
      paramInt2 += this.currentGUI.cropY;
    }
    else if (j == 270)
    {
      n = this.currentGUI.cropH + this.currentGUI.cropY - paramInt1;
      paramInt1 = this.currentGUI.cropW + this.currentGUI.cropX - paramInt2;
      paramInt2 = n;
    }
    else
    {
      paramInt1 += this.currentGUI.cropX;
      if (this.decode_pdf.getDisplayView() == 1)
        paramInt2 = this.currentGUI.cropH + this.currentGUI.cropY - paramInt2;
      else
        paramInt2 = this.currentGUI.cropY + paramInt2;
    }
    return new Point(paramInt1, paramInt2);
  }

  public Point getCoordsOnPage(int paramInt1, int paramInt2, int paramInt3)
  {
    Point localPoint;
    switch (this.decode_pdf.getDisplayView())
    {
    case 1:
      localPoint = getPageCoordsInSingleDisplayMode(paramInt1, paramInt2, paramInt3);
      paramInt1 = localPoint.x;
      paramInt2 = localPoint.y;
      break;
    case 2:
      localPoint = getPageCoordsInContinuousDisplayMode(paramInt1, paramInt2, paramInt3);
      paramInt1 = localPoint.x;
      paramInt2 = localPoint.y;
      break;
    case 3:
      localPoint = getPageCoordsInFacingDisplayMode(paramInt1, paramInt2);
      paramInt1 = localPoint.x;
      paramInt2 = localPoint.y;
      break;
    case 4:
      localPoint = getPageCoordsInContinuousFacingDisplayMode(paramInt1, paramInt2, paramInt3);
      paramInt1 = localPoint.x;
      paramInt2 = localPoint.y;
      break;
    }
    return new Point(paramInt1, paramInt2);
  }

  public void repaintArea(Rectangle paramRectangle, int paramInt)
  {
    int i = 10;
    float f = this.decode_pdf.getScaling();
    int j = (int)(paramRectangle.x * f) - i;
    int k = (int)((paramInt - paramRectangle.y - paramRectangle.height) * f) - i;
    int m = (int)((paramRectangle.x + paramRectangle.width) * f) + i + i;
    int n = (int)((paramRectangle.y + paramRectangle.height) * f) + i + i;
    RepaintManager localRepaintManager = RepaintManager.currentManager(this.decode_pdf);
    localRepaintManager.addDirtyRegion(this.decode_pdf, j, k, m, n);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.gui.swing.SwingMouseSelector
 * JD-Core Version:    0.6.2
 */