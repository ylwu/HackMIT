package org.jpedal.display.swing;

import java.awt.Graphics2D;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.scene.CacheHint;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import org.jpedal.PdfDecoder;
import org.jpedal.objects.PdfPageData;
import org.jpedal.objects.acroforms.AcroRenderer;
import org.jpedal.parser.DecoderOptions;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.Messages;

public class PageFlow extends JFXPanel
{
  protected final PdfDecoder pdf;
  private boolean stopAddingPages;
  private final int pageCount;
  private int displayRotation;
  protected int pageNumber;
  protected final PdfPageData pageData;
  private int pagesToGenerate = 21;
  private static final int textureSize = 256;
  private Page[] pages;
  private double totalPageWidth;
  private Scene scene;
  private AnchorPane contents;
  private Rectangle backgroundTop;
  private Rectangle backgroundBottom;
  private NavBar navBar;
  private ZoomBar zoomBar;
  private CheckBox perspectiveCheckBox;
  private CheckBox reflectionCheckBox;
  private ActionListener pageListener;
  private ActionListener messageListener;
  private Cursor defaultCursor;
  private Cursor grabbingCursor;
  private Cursor grabCursor;
  private double sceneXOffset = 0.0D;
  private double sceneYOffset = 0.0D;
  private boolean currentlyAddingPages = false;
  private boolean memoryWarningShown = false;
  private boolean pageFlowEnding = false;
  private double scaling = 1.5D;
  private double pageFocus = 1.0D;
  private int currentZPosition = -1;
  private boolean formsIgnoredStore = false;
  private int pagesInMemory;
  private final long memoryLimit;
  private final Runtime runtime;
  private final int pageLimit;
  private boolean pageClickEvent = false;
  private boolean enableReflection = true;
  private boolean enablePerspectiveTransform = true;
  private volatile double x;
  private volatile boolean isAnimating;
  private volatile boolean stopAnimating = false;
  private int newDestination = 0;
  private double speed = 0.0D;

  public PageFlow(int paramInt, PdfDecoder paramPdfDecoder)
  {
    Platform.setImplicitExit(false);
    if (paramInt < 1)
      paramInt = 1;
    this.pdf = paramPdfDecoder;
    this.pageData = this.pdf.getPdfPageData();
    this.pageCount = this.pdf.getPageCount();
    this.pageNumber = paramInt;
    this.pageFocus = this.pageNumber;
    this.pagesInMemory = 0;
    this.runtime = Runtime.getRuntime();
    long l = this.runtime.maxMemory();
    if ((float)l * 0.25F < 36000000.0F)
      this.memoryLimit = (l - 36000000L);
    else
      this.memoryLimit = (()((float)l * 0.75F));
    AcroRenderer localAcroRenderer = this.pdf.getFormRenderer();
    if (localAcroRenderer != null)
    {
      this.formsIgnoredStore = localAcroRenderer.ignoreForms();
      localAcroRenderer.setIgnoreForms(true);
    }
    this.pages = new Page[this.pageCount];
    Platform.runLater(new Runnable()
    {
      public void run()
      {
        PageFlow.this.createScene();
      }
    });
    this.pageLimit = 50;
  }

  private void createScene()
  {
    this.contents = new AnchorPane();
    ObservableList localObservableList = this.contents.getChildren();
    this.scene = new Scene(this.contents);
    setScene(this.scene);
    this.sceneXOffset = (getWidth() / 2);
    this.sceneYOffset = (getHeight() / 2);
    this.backgroundTop = new Rectangle(0.0D, 0.0D, getWidth(), getHeight() / 2);
    this.backgroundTop.setFill(new Color(0.2156862765550613D, 0.2156862765550613D, 0.2549019753932953D, 1.0D));
    this.backgroundBottom = new Rectangle(0.0D, getHeight() / 2, getWidth(), getHeight() / 2);
    this.backgroundBottom.setFill(new Color(0.1098039224743843D, 0.1098039224743843D, 0.125490203499794D, 1.0D));
    this.navBar = new NavBar();
    this.zoomBar = new ZoomBar();
    this.perspectiveCheckBox = new CheckBox("Perspectives");
    this.perspectiveCheckBox.setLayoutX(5.0D);
    this.perspectiveCheckBox.setLayoutY(5.0D);
    this.perspectiveCheckBox.setTextFill(Color.WHITE);
    this.perspectiveCheckBox.setSelected(true);
    this.perspectiveCheckBox.setOnAction(new EventHandler()
    {
      public void handle(javafx.event.ActionEvent paramAnonymousActionEvent)
      {
        PageFlow.this.togglePerspectives();
      }
    });
    this.reflectionCheckBox = new CheckBox("Reflections");
    this.reflectionCheckBox.setLayoutX(5.0D);
    this.reflectionCheckBox.setLayoutY(25.0D);
    this.reflectionCheckBox.setTextFill(Color.WHITE);
    this.reflectionCheckBox.setSelected(true);
    this.reflectionCheckBox.setOnAction(new EventHandler()
    {
      public void handle(javafx.event.ActionEvent paramAnonymousActionEvent)
      {
        PageFlow.this.toggleReflections();
      }
    });
    if (DecoderOptions.isRunningOnLinux)
    {
      toggleReflections();
      togglePerspectives();
    }
    localObservableList.addAll(new Node[] { this.backgroundTop, this.backgroundBottom, this.navBar, this.zoomBar, this.perspectiveCheckBox, this.reflectionCheckBox });
    setupMouseHandlers();
    setupWindowResizeListeners();
    addPages();
  }

  private void setupWindowResizeListeners()
  {
    this.scene.widthProperty().addListener(new ChangeListener()
    {
      public void changed(ObservableValue<? extends Number> paramAnonymousObservableValue, Number paramAnonymousNumber1, Number paramAnonymousNumber2)
      {
        PageFlow.this.sceneXOffset = (paramAnonymousNumber2.doubleValue() / 2.0D);
        PageFlow.this.navBar.update();
        PageFlow.this.zoomBar.update();
        Platform.runLater(new Runnable()
        {
          public void run()
          {
            PageFlow.this.backgroundTop.setWidth(PageFlow.this.scene.getWidth());
            PageFlow.this.backgroundBottom.setWidth(PageFlow.this.scene.getWidth());
          }
        });
        for (PageFlow.Page localPage : PageFlow.this.pages)
          if (localPage != null)
            localPage.update();
      }
    });
    this.scene.heightProperty().addListener(new ChangeListener()
    {
      public void changed(ObservableValue<? extends Number> paramAnonymousObservableValue, Number paramAnonymousNumber1, Number paramAnonymousNumber2)
      {
        PageFlow.this.totalPageWidth = (PageFlow.this.pageCount * PageFlow.this.getPageWidthOrHeight());
        PageFlow.this.sceneYOffset = (paramAnonymousNumber2.doubleValue() / 2.0D);
        PageFlow.this.navBar.update();
        PageFlow.this.zoomBar.update();
        Platform.runLater(new Runnable()
        {
          public void run()
          {
            PageFlow.this.backgroundTop.setHeight(PageFlow.this.scene.getHeight());
            PageFlow.this.backgroundBottom.setHeight(PageFlow.this.scene.getHeight());
            PageFlow.this.backgroundBottom.setY(PageFlow.this.scene.getHeight() / 2.0D);
          }
        });
        if (PageFlow.this.pages[(PageFlow.this.pageNumber - 1)] != null)
          PageFlow.this.pages[(PageFlow.this.pageNumber - 1)].setMain(true);
        for (PageFlow.Page localPage : PageFlow.this.pages)
          if (localPage != null)
            localPage.update();
      }
    });
  }

  private void setupMouseHandlers()
  {
    this.scene.setOnMousePressed(new EventHandler()
    {
      public void handle(MouseEvent paramAnonymousMouseEvent)
      {
        if (SingleDisplay.allowChangeCursor)
          PageFlow.this.scene.setCursor(PageFlow.this.grabbingCursor);
        if ((!PageFlow.this.navBar.isNavBarPress(paramAnonymousMouseEvent)) && (!PageFlow.this.zoomBar.isZoomBarPress(paramAnonymousMouseEvent)))
        {
          if (PageFlow.this.isAnimating)
            PageFlow.this.stopAnimating = true;
          PageFlow.this.x = paramAnonymousMouseEvent.getSceneX();
        }
      }
    });
    this.scene.setOnMouseDragged(new EventHandler()
    {
      public void handle(MouseEvent paramAnonymousMouseEvent)
      {
        if ((!PageFlow.this.navBar.isNavBarDrag(paramAnonymousMouseEvent)) && (!PageFlow.this.zoomBar.isZoomBarDrag(paramAnonymousMouseEvent)))
        {
          double d = PageFlow.this.pageFocus - (paramAnonymousMouseEvent.getSceneX() - PageFlow.this.x) / PageFlow.this.totalPageWidth * 4.0D * PageFlow.this.pageCount;
          if ((d > 1.0D) && (d < PageFlow.this.pageCount))
          {
            PageFlow.this.isAnimating = true;
            PageFlow.this.reorderPages(PageFlow.this.pageFocus, false);
            PageFlow.this.pageFocus = d;
            PageFlow.this.navBar.update();
            for (PageFlow.Page localPage : PageFlow.this.pages)
              if (localPage != null)
                localPage.update();
            PageFlow.this.isAnimating = false;
          }
          int i = (int)(PageFlow.this.pageFocus + 0.5D);
          if (PageFlow.this.pageNumber != i)
          {
            PageFlow.this.pageNumber = i;
            if (PageFlow.this.pageListener != null)
              PageFlow.this.pageListener.actionPerformed(new java.awt.event.ActionEvent(this, 1001, String.valueOf(PageFlow.this.pageNumber)));
          }
          PageFlow.this.addPages();
          PageFlow.this.x = paramAnonymousMouseEvent.getSceneX();
        }
      }
    });
    this.scene.setOnMouseReleased(new EventHandler()
    {
      public void handle(MouseEvent paramAnonymousMouseEvent)
      {
        if (SingleDisplay.allowChangeCursor)
          PageFlow.this.scene.setCursor(PageFlow.this.grabCursor);
        Timer localTimer = new Timer(350, new ActionListener()
        {
          public void actionPerformed(java.awt.event.ActionEvent paramAnonymous2ActionEvent)
          {
            if (SingleDisplay.allowChangeCursor)
              PageFlow.this.scene.setCursor(PageFlow.this.defaultCursor);
          }
        });
        localTimer.setRepeats(false);
        localTimer.start();
        if ((!PageFlow.this.navBar.isNavBarRelease(paramAnonymousMouseEvent)) && (!PageFlow.this.zoomBar.isZoomBarRelease()))
          if (!PageFlow.this.pageClickEvent)
          {
            if (PageFlow.this.pageFocus < 1.0D)
              PageFlow.this.pageFocus = 1.0D;
            else if (PageFlow.this.pageFocus > PageFlow.this.pageCount)
              PageFlow.this.pageFocus = PageFlow.this.pageCount;
            PageFlow.this.goTo((int)(PageFlow.this.pageFocus + 0.5D));
          }
          else
          {
            PageFlow.this.pageClickEvent = false;
          }
      }
    });
    this.scene.setOnMouseMoved(new EventHandler()
    {
      public void handle(MouseEvent paramAnonymousMouseEvent)
      {
        if ((PageFlow.this.navBar.isNavBarHover(paramAnonymousMouseEvent)) || (PageFlow.this.zoomBar.isZoomBarHover(paramAnonymousMouseEvent)))
        {
          if (SingleDisplay.allowChangeCursor)
            PageFlow.this.scene.setCursor(PageFlow.this.grabCursor);
        }
        else if (SingleDisplay.allowChangeCursor)
          PageFlow.this.scene.setCursor(PageFlow.this.defaultCursor);
      }
    });
    this.scene.setOnKeyPressed(new EventHandler()
    {
      public void handle(KeyEvent paramAnonymousKeyEvent)
      {
        KeyCode localKeyCode = paramAnonymousKeyEvent.getCode();
        int i;
        switch (PageFlow.21.$SwitchMap$javafx$scene$input$KeyCode[localKeyCode.ordinal()])
        {
        case 1:
          i = PageFlow.this.pageNumber + 1;
          if (i <= PageFlow.this.pageCount)
            PageFlow.this.goTo(i);
          break;
        case 2:
          i = PageFlow.this.pageNumber - 1;
          if (i > 0)
            PageFlow.this.goTo(i);
          break;
        case 3:
          PageFlow.this.toggleReflections();
          break;
        case 4:
          PageFlow.this.togglePerspectives();
        }
      }
    });
    this.scene.setOnScroll(new EventHandler()
    {
      public void handle(ScrollEvent paramAnonymousScrollEvent)
      {
        double d = paramAnonymousScrollEvent.getDeltaY();
        if (paramAnonymousScrollEvent.isControlDown())
        {
          PageFlow.Page localPage;
          if (d < 0.0D)
          {
            if (PageFlow.this.scaling < 2.0D)
            {
              PageFlow.this.scaling = (PageFlow.this.scaling + 0.1D);
              if (PageFlow.this.scaling > 2.0D)
                PageFlow.this.scaling = 2.0D;
              PageFlow.this.zoomBar.update();
              for (localPage : PageFlow.this.pages)
                if (localPage != null)
                  localPage.update();
            }
          }
          else if ((d > 0.0D) && (PageFlow.this.scaling > 1.0D))
          {
            PageFlow.this.scaling = (PageFlow.this.scaling - 0.1D);
            if (PageFlow.this.scaling < 1.0D)
              PageFlow.this.scaling = 1.0D;
            PageFlow.this.zoomBar.update();
            for (localPage : PageFlow.this.pages)
              if (localPage != null)
                localPage.update();
          }
        }
        else
        {
          int i;
          if (d > 0.0D)
          {
            i = PageFlow.this.pageNumber - 1;
            if (i > 0)
              PageFlow.this.goTo(i);
          }
          else
          {
            i = PageFlow.this.pageNumber + 1;
            if (i <= PageFlow.this.pageCount)
              PageFlow.this.goTo(i);
          }
        }
      }
    });
    this.scene.setOnMouseClicked(new EventHandler()
    {
      public void handle(MouseEvent paramAnonymousMouseEvent)
      {
        if (paramAnonymousMouseEvent.getClickCount() == 2)
        {
          if (PageFlow.this.scaling != 1.0D)
            PageFlow.this.scaling = 1.0D;
          else
            PageFlow.this.scaling = 2.0D;
          PageFlow.this.zoomBar.update();
          for (PageFlow.Page localPage : PageFlow.this.pages)
            if (localPage != null)
              localPage.update();
        }
      }
    });
  }

  private void reorderPages(double paramDouble, boolean paramBoolean)
  {
    int i = (int)(paramDouble + 0.5D);
    if ((!paramBoolean) && ((this.currentZPosition == i) || (i < 1) || (i > this.pageCount)))
      return;
    this.currentZPosition = i;
    ArrayList localArrayList1 = new ArrayList();
    localArrayList1.add(this.backgroundTop);
    localArrayList1.add(this.backgroundBottom);
    for (int j = this.pageCount; j > i; j--)
      if (this.pages[(j - 1)] != null)
      {
        localArrayList1.add(this.pages[(j - 1)]);
        if (this.enableReflection)
          localArrayList1.add(this.pages[(j - 1)].getReflection());
      }
    for (j = 1; j < i; j++)
      if (this.pages[(j - 1)] != null)
      {
        localArrayList1.add(this.pages[(j - 1)]);
        if (this.enableReflection)
          localArrayList1.add(this.pages[(j - 1)].getReflection());
      }
    if (this.pages[(i - 1)] != null)
    {
      localArrayList1.add(this.pages[(i - 1)]);
      if (this.enableReflection)
        localArrayList1.add(this.pages[(i - 1)].getReflection());
    }
    localArrayList1.add(this.navBar);
    localArrayList1.add(this.zoomBar);
    localArrayList1.add(this.perspectiveCheckBox);
    localArrayList1.add(this.reflectionCheckBox);
    final ArrayList localArrayList2 = localArrayList1;
    Platform.runLater(new Runnable()
    {
      public void run()
      {
        PageFlow.this.contents.getChildren().setAll(localArrayList2);
      }
    });
  }

  public void setRotation(int paramInt)
  {
    if (this.displayRotation != paramInt)
    {
      this.displayRotation = paramInt;
      for (Page localPage : this.pages)
        if (localPage != null)
          localPage.dispose();
      stop();
      this.stopAddingPages = false;
      goTo(this.pageNumber);
    }
  }

  private void toggleReflections()
  {
    Page localPage;
    if (this.enableReflection)
    {
      this.enableReflection = false;
      Platform.runLater(new Runnable()
      {
        public void run()
        {
          PageFlow.this.reflectionCheckBox.setSelected(false);
        }
      });
      for (localPage : this.pages)
        if (localPage != null)
          localPage.disposeReflection();
      reorderPages(this.pageFocus, true);
    }
    else if (this.enablePerspectiveTransform)
    {
      this.enableReflection = true;
      Platform.runLater(new Runnable()
      {
        public void run()
        {
          PageFlow.this.reflectionCheckBox.setSelected(true);
        }
      });
      for (localPage : this.pages)
        if (localPage != null)
        {
          localPage.setupReflection();
          localPage.update();
        }
      reorderPages(this.pageFocus, true);
    }
  }

  private void togglePerspectives()
  {
    Page localPage;
    if (this.enablePerspectiveTransform)
    {
      if (this.enableReflection)
        toggleReflections();
      this.enablePerspectiveTransform = false;
      Platform.runLater(new Runnable()
      {
        public void run()
        {
          PageFlow.this.perspectiveCheckBox.setSelected(false);
          PageFlow.this.reflectionCheckBox.setDisable(true);
        }
      });
      for (localPage : this.pages)
        if (localPage != null)
        {
          localPage.disposePerspectiveTransform();
          localPage.update();
        }
    }
    else
    {
      this.enablePerspectiveTransform = true;
      Platform.runLater(new Runnable()
      {
        public void run()
        {
          PageFlow.this.perspectiveCheckBox.setSelected(true);
          PageFlow.this.reflectionCheckBox.setDisable(false);
        }
      });
      for (localPage : this.pages)
        if (localPage != null)
        {
          localPage.setupPerspectiveTransform();
          localPage.update();
        }
    }
  }

  public void setCursors(BufferedImage paramBufferedImage1, BufferedImage paramBufferedImage2)
  {
    if (paramBufferedImage1 != null)
      this.grabCursor = new ImageCursor(SwingFXUtils.toFXImage(paramBufferedImage1, new WritableImage(paramBufferedImage1.getWidth(), paramBufferedImage1.getHeight())), 8.0D, 8.0D);
    else
      this.grabCursor = Cursor.DEFAULT;
    if (paramBufferedImage2 != null)
      this.grabbingCursor = new ImageCursor(SwingFXUtils.toFXImage(paramBufferedImage2, new WritableImage(paramBufferedImage2.getWidth(), paramBufferedImage2.getHeight())), 8.0D, 8.0D);
    else
      this.grabbingCursor = Cursor.DEFAULT;
    this.defaultCursor = Cursor.DEFAULT;
  }

  public void goTo(final int paramInt)
  {
    this.pageNumber = paramInt;
    for (int i = 0; i < this.pageCount; i++)
      if ((this.pages[i] != null) && (i != this.pageNumber - 1))
        this.pages[i].setMain(false);
    if (this.pages[(this.pageNumber - 1)] != null)
      this.pages[(this.pageNumber - 1)].setMain(true);
    if (this.pageListener != null)
      this.pageListener.actionPerformed(new java.awt.event.ActionEvent(this, 1001, String.valueOf(paramInt)));
    this.pdf.setPageParameters(-100.0F, paramInt);
    addPages();
    if (this.isAnimating)
    {
      this.newDestination = paramInt;
      return;
    }
    Thread local18 = new Thread("PageFlow-goTo")
    {
      public void run()
      {
        int i = paramInt;
        while ((!PageFlow.this.stopAnimating) && ((PageFlow.this.pageFocus > i) || (PageFlow.this.pageFocus < i)))
        {
          if (PageFlow.this.newDestination != 0)
          {
            i = PageFlow.this.newDestination;
            PageFlow.this.newDestination = 0;
          }
          if (PageFlow.this.pageFocus < i)
          {
            if (PageFlow.this.speed < 0.2000000029802322D)
              PageFlow.this.speed = 0.2000000029802322D;
            PageFlow.this.speed = (PageFlow.this.speed * 1.149999976158142D);
          }
          else
          {
            if (PageFlow.this.speed > -0.2000000029802322D)
              PageFlow.this.speed = -0.2000000029802322D;
            PageFlow.this.speed = (PageFlow.this.speed * 1.149999976158142D);
          }
          double d = (i - PageFlow.this.pageFocus) / 4.0D;
          if (Math.abs(PageFlow.this.speed) > Math.abs(d))
            PageFlow.this.speed = d;
          PageFlow.this.pageFocus = (PageFlow.this.pageFocus + PageFlow.this.speed);
          if (PageFlow.this.pageFocus - (int)PageFlow.this.pageFocus > 0.99D)
            PageFlow.this.pageFocus = ((int)PageFlow.this.pageFocus + 1);
          else if (PageFlow.this.pageFocus - (int)PageFlow.this.pageFocus < 0.01D)
            PageFlow.this.pageFocus = ((int)PageFlow.this.pageFocus);
          PageFlow.this.navBar.update();
          PageFlow.this.reorderPages(PageFlow.this.pageFocus, false);
          for (PageFlow.Page localPage : PageFlow.this.pages)
            if (localPage != null)
              localPage.update();
          try
          {
            Thread.sleep(40L);
          }
          catch (Exception localException)
          {
            if (LogWriter.isOutput())
              LogWriter.writeLog("Exception: " + localException.getMessage());
          }
          if (PageFlow.this.newDestination != 0)
          {
            i = PageFlow.this.newDestination;
            PageFlow.this.newDestination = 0;
          }
        }
        PageFlow.this.stopAnimating = false;
        PageFlow.this.isAnimating = false;
      }
    };
    local18.setDaemon(true);
    this.isAnimating = true;
    local18.start();
  }

  public void setPageListener(ActionListener paramActionListener)
  {
    this.pageListener = paramActionListener;
  }

  public void setMessageListener(ActionListener paramActionListener)
  {
    this.messageListener = paramActionListener;
  }

  private synchronized Image getPageImage(int paramInt1, int paramInt2, int paramInt3)
  {
    if ((paramInt1 != this.pageNumber) && (paramInt3 > 256))
      return null;
    int i = this.pdf.getPdfPageData().getCropBoxWidth(paramInt1);
    int j = this.pdf.getPdfPageData().getCropBoxHeight(paramInt1);
    float f1;
    if (i > j)
      f1 = paramInt3 / i;
    else
      f1 = paramInt3 / j;
    try
    {
      float f2 = this.pdf.scaling;
      this.pdf.scaling = f1;
      BufferedImage localBufferedImage1 = this.pdf.getPageAsImage(paramInt1);
      this.pdf.scaling = f2;
      BufferedImage localBufferedImage2 = new BufferedImage(paramInt3, paramInt3, 2);
      Graphics2D localGraphics2D = (Graphics2D)localBufferedImage2.getGraphics();
      localGraphics2D.rotate(paramInt2 / 180.0D * 3.141592653589793D, paramInt3 / 2, paramInt3 / 2);
      int k = (paramInt3 - localBufferedImage1.getWidth()) / 2;
      int m = paramInt3 - localBufferedImage1.getHeight();
      localGraphics2D.drawImage(localBufferedImage1, k, m, localBufferedImage1.getWidth(), localBufferedImage1.getHeight(), null);
      return SwingFXUtils.toFXImage(localBufferedImage2, new WritableImage(localBufferedImage2.getWidth(), localBufferedImage2.getHeight()));
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception: " + localException.getMessage());
    }
    return null;
  }

  public void dispose()
  {
    for (Object localObject2 : this.pages)
      if (localObject2 != null)
        localObject2.dispose();
    this.pages = null;
    this.contents = null;
    this.scene = null;
    ??? = this.pdf.getFormRenderer();
    if (??? != null)
      ((AcroRenderer)???).setIgnoreForms(this.formsIgnoredStore);
    System.gc();
  }

  public void stop()
  {
    this.stopAddingPages = true;
    while (this.currentlyAddingPages)
      try
      {
        Thread.sleep(100L);
      }
      catch (InterruptedException localInterruptedException)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog("Exception: " + localInterruptedException.getMessage());
      }
  }

  private void addPages()
  {
    Thread local19 = new Thread("FX-addPages")
    {
      public void run()
      {
        PageFlow.this.currentlyAddingPages = true;
        int i = PageFlow.this.pageNumber;
        for (int j = 0; j <= PageFlow.this.pagesToGenerate; j++)
        {
          if (PageFlow.this.checkMemory())
            return;
          int k = PageFlow.this.pagesToGenerate * 2 - 1 - PageFlow.this.pagesInMemory;
          if (k < 2)
            PageFlow.this.removeFurthestPages(2 - k);
          if (j == PageFlow.this.pagesToGenerate - 1)
          {
            long l = PageFlow.this.runtime.totalMemory() - PageFlow.this.runtime.freeMemory();
            if ((l < PageFlow.this.memoryLimit) && (PageFlow.this.pagesToGenerate < PageFlow.this.pageCount) && (PageFlow.this.pagesToGenerate < PageFlow.this.pageLimit))
            {
              PageFlow.access$3308(PageFlow.this);
            }
            else
            {
              l = PageFlow.this.runtime.totalMemory() - PageFlow.this.runtime.freeMemory();
              if ((l < PageFlow.this.memoryLimit) && (PageFlow.this.pagesToGenerate < PageFlow.this.pageCount) && (PageFlow.this.pagesToGenerate < PageFlow.this.pageLimit))
                PageFlow.access$3308(PageFlow.this);
            }
          }
          if (PageFlow.this.stopAddingPages)
          {
            PageFlow.this.currentlyAddingPages = false;
            PageFlow.this.stopAddingPages = false;
            return;
          }
          int m = i + j;
          if (j > 40)
          {
            m += j - 40;
            m -= (m & 0x1);
            if (m > PageFlow.this.pageCount)
            {
              m -= PageFlow.this.pageCount - (i + 40);
              if ((m & 0x1) == 0)
                m--;
            }
          }
          if ((m <= PageFlow.this.pageCount) && (PageFlow.this.pages != null) && (PageFlow.this.pages[(m - 1)] == null))
            try
            {
              PageFlow.Page localPage1 = new PageFlow.Page(PageFlow.this, m);
              if (PageFlow.this.pages != null)
              {
                PageFlow.this.pages[(m - 1)] = localPage1;
                PageFlow.this.reorderPages(PageFlow.this.pageFocus, true);
                PageFlow.access$3508(PageFlow.this);
                if (m == PageFlow.this.pageNumber)
                  localPage1.setMain(true);
              }
            }
            catch (Exception localException)
            {
              if (PageFlow.this.pages != null)
                PageFlow.this.pages[(m - 1)] = null;
              PageFlow.access$3510(PageFlow.this);
              if (LogWriter.isOutput())
                LogWriter.writeLog("Exception: " + localException.getMessage());
            }
          if (PageFlow.this.stopAddingPages)
          {
            PageFlow.this.currentlyAddingPages = false;
            PageFlow.this.stopAddingPages = false;
            return;
          }
          m = i - j;
          if (j > 40)
          {
            m -= j - 40;
            m += (m & 0x1);
            if (m < 1)
            {
              m += i - 41;
              if ((m & 0x1) == 0)
                m--;
            }
          }
          if ((m > 0) && (PageFlow.this.pages != null) && (PageFlow.this.pages[(m - 1)] == null))
            try
            {
              PageFlow.Page localPage2 = new PageFlow.Page(PageFlow.this, m);
              if (PageFlow.this.pages != null)
              {
                PageFlow.this.pages[(m - 1)] = localPage2;
                PageFlow.this.reorderPages(PageFlow.this.pageFocus, true);
                PageFlow.access$3508(PageFlow.this);
              }
            }
            catch (NullPointerException localNullPointerException)
            {
              if (PageFlow.this.pages != null)
                PageFlow.this.pages[(m - 1)] = null;
              PageFlow.access$3510(PageFlow.this);
              if (LogWriter.isOutput())
                LogWriter.writeLog("Exception: " + localNullPointerException.getMessage());
            }
          if (i != PageFlow.this.pageNumber)
          {
            j = -1;
            i = PageFlow.this.pageNumber;
          }
          if (j > 10)
            while (((PageFlow.this.speed > 0.00499999988824129D) || (PageFlow.this.speed < -0.00499999988824129D)) && (i == PageFlow.this.pageNumber))
              try
              {
                Thread.sleep(10L);
              }
              catch (InterruptedException localInterruptedException)
              {
                if (LogWriter.isOutput())
                  LogWriter.writeLog("Exception: " + localInterruptedException.getMessage());
              }
        }
        PageFlow.this.currentlyAddingPages = false;
      }
    };
    if (!this.currentlyAddingPages)
    {
      this.currentlyAddingPages = true;
      local19.setDaemon(true);
      local19.start();
    }
  }

  private boolean checkMemory()
  {
    if (this.runtime.maxMemory() - (this.runtime.totalMemory() - this.runtime.freeMemory()) < 32000000L)
      if (this.pagesInMemory > 1)
      {
        System.gc();
        int i = 1;
        while (this.runtime.maxMemory() - (this.runtime.totalMemory() - this.runtime.freeMemory()) < 32000000L)
          if (this.pagesToGenerate > 5)
          {
            this.pagesToGenerate -= 1;
            int j = this.pagesInMemory - (this.pagesToGenerate * 2 - 1);
            if (j > 0)
              removeFurthestPages(j);
            System.gc();
          }
          else
          {
            i = 0;
          }
        if (i != 0)
          return false;
        if (this.runtime.maxMemory() - (this.runtime.totalMemory() - this.runtime.freeMemory()) < 32000000L)
        {
          if (!this.memoryWarningShown)
          {
            if ((this.messageListener != null) && (!this.pageFlowEnding))
              this.messageListener.actionPerformed(new java.awt.event.ActionEvent(this, 1001, Messages.getMessage("PdfViewer.PageFlowLowMemory")));
            this.memoryWarningShown = true;
          }
          this.currentlyAddingPages = false;
          return true;
        }
      }
      else
      {
        stop();
        if (SwingUtilities.isEventDispatchThread())
        {
          this.pdf.setDisplayView(1, 2);
        }
        else
        {
          Runnable local20 = new Runnable()
          {
            public void run()
            {
              PageFlow.this.pdf.setDisplayView(1, 2);
            }
          };
          SwingUtilities.invokeLater(local20);
        }
        if ((this.messageListener != null) && (!this.pageFlowEnding))
        {
          this.pageFlowEnding = true;
          this.messageListener.actionPerformed(new java.awt.event.ActionEvent(this, 1001, Messages.getMessage("PdfViewer.PageFlowNotEnoughMemory")));
        }
        return true;
      }
    return false;
  }

  private void removeFurthestPages(int paramInt)
  {
    int i = 0;
    int j = this.pageNumber - 1;
    int k = this.pageCount - this.pageNumber;
    int m = j > k ? j : k;
    int n = m;
    int i1;
    int i2;
    while ((i < paramInt) && (n >= 40))
    {
      i1 = this.pageNumber - n;
      i1 -= 1 - (i1 & 0x1);
      if ((i1 > 0) && (this.pages[(i1 - 1)] != null))
      {
        this.pages[(i1 - 1)].dispose();
        i++;
      }
      if (i != paramInt)
      {
        i2 = this.pageNumber + n;
        i2 -= 1 - (i2 & 0x1);
        if ((i2 <= this.pageCount) && (this.pages[(i2 - 1)] != null))
        {
          this.pages[(i2 - 1)].dispose();
          i++;
        }
        n--;
      }
    }
    n = m;
    while ((i < paramInt) && (n >= 0))
    {
      i1 = this.pageNumber - n;
      if ((i1 > 0) && (this.pages[(i1 - 1)] != null))
      {
        this.pages[(i1 - 1)].dispose();
        i++;
      }
      if (i != paramInt)
      {
        i2 = this.pageNumber + n;
        if ((i2 <= this.pageCount) && (this.pages[(i2 - 1)] != null))
        {
          this.pages[(i2 - 1)].dispose();
          i++;
        }
        n--;
      }
    }
    System.gc();
  }

  private double getFullPageWidthOrHeight()
  {
    return getHeight() / 13.0D * 12.0D;
  }

  private double getPageWidthOrHeight()
  {
    return getHeight() / (13.0D * this.scaling) * 12.0D;
  }

  private class ZoomBar extends Parent
  {
    private final Line zoomLine = new Line();
    private final Circle zoomCircle;
    private static final int distanceFromSide = 15;
    private boolean handlingMouse = false;

    public ZoomBar()
    {
      this.zoomLine.setStrokeWidth(1.5D);
      this.zoomLine.setStroke(Color.WHITESMOKE);
      this.zoomCircle = new Circle(5.0D);
      this.zoomCircle.setStrokeWidth(2.0D);
      this.zoomCircle.setStroke(Color.WHITE);
      this.zoomCircle.setFill(Color.GRAY);
      getChildren().addAll(new Node[] { this.zoomLine, this.zoomCircle });
    }

    public boolean isZoomBarHover(MouseEvent paramMouseEvent)
    {
      return (paramMouseEvent.getX() < 30.0D) && (paramMouseEvent.getY() > getStartY() - 5.0D) && (paramMouseEvent.getY() < getEndY() + 5.0D);
    }

    public boolean isZoomBarPress(MouseEvent paramMouseEvent)
    {
      if ((paramMouseEvent.getX() < 30.0D) && (paramMouseEvent.getY() > getStartY() - 5.0D) && (paramMouseEvent.getY() < getEndY() + 5.0D))
      {
        this.handlingMouse = true;
        isZoomBarDrag(paramMouseEvent);
        return true;
      }
      return false;
    }

    public boolean isZoomBarDrag(MouseEvent paramMouseEvent)
    {
      if (this.handlingMouse)
      {
        double d1 = paramMouseEvent.getY();
        double d2 = getStartY();
        double d3 = getEndY();
        if (d1 < d2)
          d1 = d2;
        if (d1 > d3)
          d1 = d3;
        if (d1 != this.zoomCircle.getCenterY())
        {
          this.zoomCircle.setCenterY(d1);
          double d4 = (d1 - d2) / (d3 - d2);
          PageFlow.this.scaling = (1.0D + d4);
          for (PageFlow.Page localPage : PageFlow.this.pages)
            if (localPage != null)
              localPage.update();
        }
        return true;
      }
      return false;
    }

    public boolean isZoomBarRelease()
    {
      if (this.handlingMouse)
      {
        this.handlingMouse = false;
        return true;
      }
      return false;
    }

    public void update()
    {
      Platform.runLater(new Runnable()
      {
        public void run()
        {
          PageFlow.ZoomBar.this.zoomCircle.setCenterX(15.0D);
          PageFlow.ZoomBar.this.zoomLine.setStartX(15.5D);
          PageFlow.ZoomBar.this.zoomLine.setStartY(PageFlow.this.getHeight() * 0.2D);
          PageFlow.ZoomBar.this.zoomLine.setEndX(15.5D);
          PageFlow.ZoomBar.this.zoomLine.setEndY(PageFlow.this.getHeight() * 0.4D);
          double d1 = 2.0D - PageFlow.this.scaling;
          double d2 = PageFlow.ZoomBar.this.getStartY();
          double d3 = PageFlow.ZoomBar.this.getEndY();
          PageFlow.ZoomBar.this.zoomCircle.setCenterY(d3 - (d3 - d2) * d1);
        }
      });
    }

    private double getStartY()
    {
      return PageFlow.this.getHeight() * 0.2D;
    }

    private double getEndY()
    {
      return PageFlow.this.getHeight() * 0.4D;
    }
  }

  private class NavBar extends Parent
  {
    private final Line navLine = new Line();
    private final Circle navCircle;
    private static final int distanceFromSides = 20;
    private static final int distanceFromBottom = 15;
    private boolean handlingMouse = false;

    public NavBar()
    {
      this.navLine.setStrokeWidth(1.5D);
      this.navLine.setStroke(Color.WHITE);
      this.navCircle = new Circle(5.0D);
      this.navCircle.setStrokeWidth(2.0D);
      this.navCircle.setStroke(Color.WHITE);
      this.navCircle.setFill(Color.GRAY);
      getChildren().addAll(new Node[] { this.navLine, this.navCircle });
    }

    public boolean isNavBarHover(MouseEvent paramMouseEvent)
    {
      return paramMouseEvent.getY() > PageFlow.this.getHeight() - 30;
    }

    public boolean isNavBarPress(MouseEvent paramMouseEvent)
    {
      if (paramMouseEvent.getY() > PageFlow.this.getHeight() - 30)
      {
        this.handlingMouse = true;
        return true;
      }
      return false;
    }

    public boolean isNavBarDrag(MouseEvent paramMouseEvent)
    {
      if (this.handlingMouse)
      {
        double d1 = paramMouseEvent.getX();
        if (d1 < 20.0D)
          d1 = 20.0D;
        if (d1 > PageFlow.this.getWidth() - 20)
          d1 = PageFlow.this.getWidth() - 20;
        if (d1 != this.navCircle.getCenterX())
        {
          this.navCircle.setCenterX(d1);
          double d2 = (d1 - 20.0D) / (PageFlow.this.getWidth() - 40);
          PageFlow.this.pageFocus = ((PageFlow.this.pageCount - 1) * d2 + 1.0D);
          int i = (int)(PageFlow.this.pageFocus + 0.5D);
          if (PageFlow.this.pageNumber != i)
          {
            PageFlow.this.pageNumber = i;
            if (PageFlow.this.pageListener != null)
              PageFlow.this.pageListener.actionPerformed(new java.awt.event.ActionEvent(this, 1001, String.valueOf(PageFlow.this.pageNumber)));
          }
          PageFlow.this.addPages();
          PageFlow.this.reorderPages(PageFlow.this.pageFocus, false);
          for (PageFlow.Page localPage : PageFlow.this.pages)
            if (localPage != null)
              localPage.update();
        }
        return true;
      }
      return false;
    }

    public boolean isNavBarRelease(MouseEvent paramMouseEvent)
    {
      if (this.handlingMouse)
      {
        double d1 = paramMouseEvent.getX();
        if (d1 < 20.0D)
          d1 = 20.0D;
        if (d1 > PageFlow.this.getWidth() - 20)
          d1 = PageFlow.this.getWidth() - 20;
        double d2 = (d1 - 20.0D) / (PageFlow.this.getWidth() - 40);
        int i = (int)((PageFlow.this.pageCount - 1) * d2 + 1.0D + 0.5D);
        if (PageFlow.this.pageNumber != i)
        {
          PageFlow.this.pageNumber = i;
          if (PageFlow.this.pageListener != null)
            PageFlow.this.pageListener.actionPerformed(new java.awt.event.ActionEvent(this, 1001, String.valueOf(PageFlow.this.pageNumber)));
        }
        PageFlow.this.goTo(PageFlow.this.pageNumber);
        this.handlingMouse = false;
        return true;
      }
      return false;
    }

    public void update()
    {
      Platform.runLater(new Runnable()
      {
        public void run()
        {
          PageFlow.NavBar.this.navCircle.setCenterY(PageFlow.this.getHeight() - 15);
          PageFlow.NavBar.this.navLine.setStartX(20.0D);
          PageFlow.NavBar.this.navLine.setStartY(PageFlow.this.getHeight() - 15 + 0.5D);
          PageFlow.NavBar.this.navLine.setEndX(PageFlow.this.getWidth() - 20);
          PageFlow.NavBar.this.navLine.setEndY(PageFlow.this.getHeight() - 15 + 0.5D);
          double d1 = (PageFlow.this.pageFocus - 1.0D) / (PageFlow.this.pageCount - 1);
          double d2 = 20.0D + (PageFlow.this.getWidth() - 40) * d1;
          PageFlow.NavBar.this.navCircle.setCenterX(d2);
        }
      });
    }
  }

  private class Page extends ImageView
  {
    private final Image lowResImage;
    private final int page;
    private final int rotation = PageFlow.this.displayRotation;
    private PerspectiveTransform trans;
    private ColorAdjust colorAdjust;
    private int mainTextureSize;
    private ImageView reflection;
    private PerspectiveTransform reflectionTransform;
    private double x = 0.0D;
    private double y = 0.0D;
    private double widthHeight = 0.0D;
    private double altWidthHeight = 0.0D;

    public Page(int arg2)
    {
      int i;
      this.page = i;
      this.lowResImage = PageFlow.this.getPageImage(i, this.rotation, 256);
      if (this.lowResImage == null)
      {
        dispose();
        this.widthHeight = 0.0D;
        return;
      }
      setImage(this.lowResImage);
      setupMouseHandlers();
      if (PageFlow.this.enableReflection)
        setupReflection();
      this.colorAdjust = new ColorAdjust();
      if (PageFlow.this.enablePerspectiveTransform)
        setupPerspectiveTransform();
      setCache(true);
      setCacheHint(CacheHint.QUALITY);
      update();
    }

    public void setupReflection()
    {
      this.reflection = new ImageView();
      this.reflectionTransform = new PerspectiveTransform();
      this.reflectionTransform.setInput(new ColorAdjust(0.0D, 0.0D, -0.75D, 0.0D));
      this.reflection.setEffect(this.reflectionTransform);
      this.reflection.setImage(this.lowResImage);
    }

    public void disposeReflection()
    {
      this.reflection = null;
      this.reflectionTransform = null;
    }

    public void setupPerspectiveTransform()
    {
      this.trans = new PerspectiveTransform();
      this.trans.setInput(this.colorAdjust);
      setEffect(this.trans);
    }

    public void disposePerspectiveTransform()
    {
      this.trans = null;
      setEffect(null);
    }

    private void setupMouseHandlers()
    {
      setOnMousePressed(new EventHandler()
      {
        public void handle(MouseEvent paramAnonymousMouseEvent)
        {
          PageFlow.this.pageClickEvent = true;
        }
      });
      setOnMouseDragged(new EventHandler()
      {
        public void handle(MouseEvent paramAnonymousMouseEvent)
        {
          PageFlow.this.pageClickEvent = false;
        }
      });
      setOnMouseReleased(new EventHandler()
      {
        public void handle(MouseEvent paramAnonymousMouseEvent)
        {
          if (PageFlow.this.pageClickEvent)
            PageFlow.this.goTo(PageFlow.Page.this.page);
        }
      });
    }

    public void update()
    {
      this.widthHeight = PageFlow.this.getPageWidthOrHeight();
      this.y = (-this.widthHeight / 40.0D);
      double d = this.page - PageFlow.this.pageFocus;
      if (d > 1.0D)
        d = (d - 1.0D) / 5.0D + 1.0D;
      else if (d < -1.0D)
        d = (d + 1.0D) / 5.0D - 1.0D;
      this.x = (d * this.widthHeight);
      redraw();
    }

    private void redraw()
    {
      int i = (!PageFlow.this.enablePerspectiveTransform) || ((PageFlow.this.scene != null) && (this.trans != null) && (((getRealX(this.x) + this.widthHeight > 0.0D) && (getRealX(this.x) < PageFlow.this.scene.getWidth())) || ((this.trans.getUrx() > 0.0D) && (this.trans.getUlx() < PageFlow.this.scene.getWidth())))) ? 1 : 0;
      if (i != 0)
        Platform.runLater(new Runnable()
        {
          public void run()
          {
            double d1 = PageFlow.Page.this.page - PageFlow.this.pageFocus;
            double d2 = Math.abs(d1);
            if (d2 > 1.0D)
              d2 = (d2 - 1.0D) / (PageFlow.this.enablePerspectiveTransform ? 16 : 8) + 1.0D;
            double d3 = Math.pow(0.7D, d2);
            if (d1 > 1.0D)
              d1 = 1.0D;
            if (d1 < -1.0D)
              d1 = -1.0D;
            int i = d1 > 0.0D ? 1 : 0;
            d1 = Math.abs(d1);
            if (d1 == 0.0D)
              PageFlow.Page.this.setEffect(null);
            else if (PageFlow.this.enablePerspectiveTransform)
              PageFlow.Page.this.setEffect(PageFlow.Page.this.trans);
            PageFlow.Page.this.colorAdjust.setBrightness(-d1 / 2.0D);
            double d4 = PageFlow.Page.this.widthHeight / 2.0D;
            double d5 = d1 / 2.0D;
            if (!PageFlow.this.enablePerspectiveTransform)
              d5 /= 8.0D;
            double d6 = d1 / 4.0D;
            double d7 = PageFlow.Page.this.getRealX(d4 + PageFlow.Page.this.x - (1.0D - d5) * d4 * d3);
            double d8 = PageFlow.Page.this.getRealX(d4 + PageFlow.Page.this.x + (1.0D - d5) * d4 * d3);
            if (PageFlow.this.enablePerspectiveTransform)
            {
              PageFlow.Page.this.trans.setLlx(d7);
              PageFlow.Page.this.trans.setUlx(d7);
              PageFlow.Page.this.trans.setLrx(d8);
              PageFlow.Page.this.trans.setUrx(d8);
              if (i != 0)
              {
                PageFlow.Page.this.trans.setLly(PageFlow.Page.this.getRealY(d4 + PageFlow.Page.this.y + (1.0D - d6) * d4 * d3));
                PageFlow.Page.this.trans.setUly(PageFlow.Page.this.getRealY(d4 + PageFlow.Page.this.y - (1.0D - d6) * d4 * d3));
                PageFlow.Page.this.trans.setLry(PageFlow.Page.this.getRealY(d4 + PageFlow.Page.this.y + d4 * d3));
                PageFlow.Page.this.trans.setUry(PageFlow.Page.this.getRealY(d4 + PageFlow.Page.this.y - d4 * d3));
              }
              else
              {
                PageFlow.Page.this.trans.setLry(PageFlow.Page.this.getRealY(d4 + PageFlow.Page.this.y + (1.0D - d6) * d4 * d3));
                PageFlow.Page.this.trans.setUry(PageFlow.Page.this.getRealY(d4 + PageFlow.Page.this.y - (1.0D - d6) * d4 * d3));
                PageFlow.Page.this.trans.setLly(PageFlow.Page.this.getRealY(d4 + PageFlow.Page.this.y + d4 * d3));
                PageFlow.Page.this.trans.setUly(PageFlow.Page.this.getRealY(d4 + PageFlow.Page.this.y - d4 * d3));
              }
            }
            if (PageFlow.this.enableReflection)
            {
              PageFlow.Page.this.reflectionTransform.setLlx(d7);
              PageFlow.Page.this.reflectionTransform.setUlx(d7);
              PageFlow.Page.this.reflectionTransform.setLrx(d8);
              PageFlow.Page.this.reflectionTransform.setUrx(d8);
              PageFlow.Page.this.reflectionTransform.setLly(PageFlow.Page.this.trans.getLly());
              PageFlow.Page.this.reflectionTransform.setLry(PageFlow.Page.this.trans.getLry());
              PageFlow.Page.this.reflectionTransform.setUly(PageFlow.Page.this.trans.getLly() + (PageFlow.Page.this.trans.getLly() - PageFlow.Page.this.trans.getUly()));
              PageFlow.Page.this.reflectionTransform.setUry(PageFlow.Page.this.trans.getLry() + (PageFlow.Page.this.trans.getLry() - PageFlow.Page.this.trans.getUry()));
            }
            if (!PageFlow.this.enablePerspectiveTransform)
            {
              PageFlow.Page.this.altWidthHeight = (d8 - d7);
              if (d1 == 0.0D)
              {
                PageFlow.Page.this.setFitWidth((int)PageFlow.Page.this.altWidthHeight);
                PageFlow.Page.this.setFitHeight((int)PageFlow.Page.this.altWidthHeight);
              }
              else
              {
                PageFlow.Page.this.setFitWidth(PageFlow.Page.this.altWidthHeight);
                PageFlow.Page.this.setFitHeight(PageFlow.Page.this.altWidthHeight);
              }
            }
            else if (d1 == 0.0D)
            {
              PageFlow.Page.this.setFitWidth((int)PageFlow.Page.this.widthHeight);
              PageFlow.Page.this.setFitHeight((int)PageFlow.Page.this.widthHeight);
            }
            else
            {
              PageFlow.Page.this.setFitWidth(PageFlow.Page.this.widthHeight);
              PageFlow.Page.this.setFitHeight(PageFlow.Page.this.widthHeight);
            }
            if (d1 == 0.0D)
            {
              PageFlow.Page.this.setX((int)PageFlow.Page.this.getRealX(PageFlow.Page.this.x));
              PageFlow.Page.this.setY((int)PageFlow.Page.this.getRealY(PageFlow.Page.this.y));
            }
            else
            {
              PageFlow.Page.this.setX(PageFlow.Page.this.getRealX(PageFlow.Page.this.x));
              PageFlow.Page.this.setY(PageFlow.Page.this.getRealY(PageFlow.Page.this.y));
            }
          }
        });
    }

    public void setMain(boolean paramBoolean)
    {
      if (paramBoolean)
      {
        Thread local5 = new Thread("FX-setMain")
        {
          public void run()
          {
            if (PageFlow.this.checkMemory())
              return;
            PageFlow.Page.this.mainTextureSize = ((int)PageFlow.this.getFullPageWidthOrHeight());
            final Image localImage = PageFlow.this.getPageImage(PageFlow.Page.this.page, PageFlow.Page.this.rotation, PageFlow.Page.this.mainTextureSize);
            if (localImage != null)
              Platform.runLater(new Runnable()
              {
                public void run()
                {
                  PageFlow.Page.this.setImage(localImage);
                }
              });
          }
        };
        local5.setDaemon(true);
        local5.start();
      }
      else
      {
        Platform.runLater(new Runnable()
        {
          public void run()
          {
            PageFlow.Page.this.setImage(PageFlow.Page.this.lowResImage);
          }
        });
      }
    }

    private ImageView getReflection()
    {
      return this.reflection;
    }

    private double getRealX(double paramDouble)
    {
      return PageFlow.this.sceneXOffset + getXOffset() + paramDouble;
    }

    private double getRealY(double paramDouble)
    {
      return PageFlow.this.sceneYOffset + getYOffset() + paramDouble;
    }

    private double getXOffset()
    {
      if (PageFlow.this.enablePerspectiveTransform)
        return -this.widthHeight / 2.0D;
      return -this.altWidthHeight / 2.0D;
    }

    private double getYOffset()
    {
      if (PageFlow.this.enablePerspectiveTransform)
        return -this.widthHeight / 2.0D;
      return -this.altWidthHeight / 2.0D;
    }

    public void dispose()
    {
      Platform.runLater(new Runnable()
      {
        public void run()
        {
          PageFlow.Page.this.setImage(null);
        }
      });
      PageFlow.access$3510(PageFlow.this);
      PageFlow.this.pages[(this.page - 1)] = null;
    }
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.display.swing.PageFlow
 * JD-Core Version:    0.6.2
 */