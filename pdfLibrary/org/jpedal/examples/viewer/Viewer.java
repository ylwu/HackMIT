package org.jpedal.examples.viewer;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.event.DocumentListener;
import org.jpedal.PdfDecoder;
import org.jpedal.examples.viewer.gui.MultiViewTransferHandler;
import org.jpedal.examples.viewer.gui.SingleViewTransferHandler;
import org.jpedal.examples.viewer.gui.SwingGUI;
import org.jpedal.examples.viewer.gui.generic.GUIMouseHandler;
import org.jpedal.examples.viewer.gui.generic.GUISearchWindow;
import org.jpedal.examples.viewer.gui.generic.GUIThumbnailPanel;
import org.jpedal.examples.viewer.gui.popups.TipOfTheDay;
import org.jpedal.examples.viewer.gui.swing.SearchList;
import org.jpedal.examples.viewer.gui.swing.SwingMouseListener;
import org.jpedal.examples.viewer.gui.swing.SwingSearchWindow;
import org.jpedal.examples.viewer.gui.swing.SwingThumbnailPanel;
import org.jpedal.examples.viewer.utils.Printer;
import org.jpedal.examples.viewer.utils.PropertiesFile;
import org.jpedal.exception.PdfException;
import org.jpedal.fonts.FontMappings;
import org.jpedal.io.JAIHelper;
import org.jpedal.io.PdfObjectReader;
import org.jpedal.objects.acroforms.AcroRenderer;
import org.jpedal.objects.acroforms.actions.ActionHandler;
import org.jpedal.objects.raw.OutlineObject;
import org.jpedal.objects.raw.PdfObject;
import org.jpedal.render.DynamicVectorRenderer;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.Messages;
import org.w3c.dom.Document;

public class Viewer
{
  public static boolean showMessages = true;
  protected Values commonValues = new Values();
  protected Printer currentPrinter = new Printer();
  protected PdfDecoder decode_pdf;
  protected GUIThumbnailPanel thumbnails;
  private PropertiesFile properties = new PropertiesFile();
  public SwingGUI currentGUI;
  private GUISearchWindow searchFrame;
  protected Commands currentCommands;
  protected GUIMouseHandler mouseHandler;
  protected String[] scalingValues;
  private boolean isSetup;
  public static final String PREFERENCES_DEFAULT = "jar:/org/jpedal/examples/viewer/res/preferences/Default.xml";
  public static final String PREFERENCES_NO_GUI = "jar:/org/jpedal/examples/viewer/res/preferences/NoGUI.xml";
  public static final String PREFERENCES_NO_SIDE_BAR = "jar:/org/jpedal/examples/viewer/res/preferences/NoSideTabOrTopButtons.xml";
  public static final String PREFERENCES_OPEN_AND_NAV_ONLY = "jar:/org/jpedal/examples/viewer/res/preferences/OpenAndNavOnly.xml";
  public static final String PREFERENCES_BEAN = "jar:/org/jpedal/examples/viewer/res/preferences/Bean.xml";
  public static boolean exitOnClose = true;

  public SwingGUI getSwingGUI()
  {
    return this.currentGUI;
  }

  public void openDefaultFile(String paramString)
  {
    String str1 = System.getProperty("org.jpedal.hires");
    if ((Commands.hires) || (str1 != null))
      this.commonValues.setUseHiresImage(true);
    String str2 = System.getProperty("org.jpedal.memory");
    if (str2 != null)
      this.commonValues.setUseHiresImage(false);
    if (this.thumbnails.isShownOnscreen())
      this.thumbnails.resetToDefault();
    this.commonValues.maxViewY = 0;
    if (paramString != null)
    {
      File localFile = new File(paramString);
      int i = 0;
      if ((paramString.startsWith("http:")) || (paramString.startsWith("jar:")) || (paramString.startsWith("file:")))
      {
        LogWriter.writeLog("Opening http connection");
        i = 1;
      }
      if ((i == 0) && (!localFile.exists()))
      {
        this.currentGUI.showMessageDialog(paramString + '\n' + Messages.getMessage("PdfViewerdoesNotExist.message"));
      }
      else if ((i == 0) && (localFile.isDirectory()))
      {
        this.currentGUI.showMessageDialog(paramString + '\n' + Messages.getMessage("PdfViewerFileIsDirectory.message"));
      }
      else
      {
        this.commonValues.setFileSize(localFile.length() >> 10);
        this.commonValues.setSelectedFile(paramString);
        this.currentGUI.setViewerTitle(null);
        String str3 = System.getProperty("org.jpedal.page");
        String str4 = System.getProperty("org.jpedal.bookmark");
        if ((str3 != null) && (i == 0))
          try
          {
            int j = Integer.parseInt(str3);
            if (j < 1)
            {
              j = -1;
              System.err.println(str3 + " must be 1 or larger. Opening on page 1");
              LogWriter.writeLog(str3 + " must be 1 or larger. Opening on page 1");
            }
            if (j != -1)
              openFile(localFile, j);
          }
          catch (Exception localException)
          {
            System.err.println(str3 + "is not a valid number for a page number. Opening on page 1");
            LogWriter.writeLog(str3 + "is not a valid number for a page number. Opening on page 1");
          }
        else if (str4 != null)
          openFile(localFile, str4);
        else
          try
          {
            this.currentCommands.openFile(paramString);
          }
          catch (PdfException localPdfException)
          {
          }
      }
    }
  }

  public void openDefaultFileAtPage(String paramString, int paramInt)
  {
    String str1 = System.getProperty("org.jpedal.hires");
    if ((Commands.hires) || (str1 != null))
      this.commonValues.setUseHiresImage(true);
    String str2 = System.getProperty("org.jpedal.memory");
    if (str2 != null)
      this.commonValues.setUseHiresImage(false);
    if (this.thumbnails.isShownOnscreen())
      this.thumbnails.resetToDefault();
    this.commonValues.maxViewY = 0;
    if (paramString != null)
    {
      File localFile = new File(paramString);
      int i = 0;
      if ((paramString.startsWith("http:")) || (paramString.startsWith("jar:")))
      {
        LogWriter.writeLog("Opening http connection");
        i = 1;
      }
      if ((i == 0) && (!localFile.exists()))
      {
        this.currentGUI.showMessageDialog(paramString + '\n' + Messages.getMessage("PdfViewerdoesNotExist.message"));
      }
      else if ((i == 0) && (localFile.isDirectory()))
      {
        this.currentGUI.showMessageDialog(paramString + '\n' + Messages.getMessage("PdfViewerFileIsDirectory.message"));
      }
      else
      {
        this.commonValues.setSelectedFile(paramString);
        this.commonValues.setFileSize(localFile.length() >> 10);
        this.currentGUI.setViewerTitle(null);
        openFile(localFile, paramInt);
      }
    }
  }

  private void init()
  {
    this.decode_pdf = new PdfDecoder(true);
    this.thumbnails = new SwingThumbnailPanel(this.commonValues, this.decode_pdf);
    this.currentGUI = new SwingGUI(this.decode_pdf, this.commonValues, this.thumbnails, this.properties);
    this.searchFrame = new SwingSearchWindow(this.currentGUI);
    this.currentCommands = new Commands(this.commonValues, this.currentGUI, this.decode_pdf, this.thumbnails, this.properties, this.searchFrame, this.currentPrinter);
    this.mouseHandler = new SwingMouseListener(this.decode_pdf, this.currentGUI, this.commonValues, this.currentCommands);
  }

  public Viewer()
  {
    init();
    org.jpedal.parser.DecoderOptions.showErrorMessages = true;
    String str = System.getProperty("org.jpedal.Viewer.Prefs");
    if (str != null)
      this.properties.loadProperties(str);
    else
      this.properties.loadProperties();
  }

  public Viewer(int paramInt)
  {
    init();
    org.jpedal.parser.DecoderOptions.showErrorMessages = true;
    String str = System.getProperty("org.jpedal.Viewer.Prefs");
    if (str != null)
      this.properties.loadProperties(str);
    else
      this.properties.loadProperties();
    this.commonValues.setModeOfOperation(paramInt);
  }

  public Viewer(String paramString)
  {
    init();
    org.jpedal.parser.DecoderOptions.showErrorMessages = true;
    try
    {
      this.properties.loadProperties(paramString);
    }
    catch (Exception localException)
    {
      System.err.println("Specified Preferrences file not found at " + paramString + ". If this file is within a jar ensure filename has jar: at the begining.\n\nLoading default properties.");
      this.properties.loadProperties();
    }
  }

  public Viewer(Container paramContainer, String paramString)
  {
    init();
    org.jpedal.parser.DecoderOptions.showErrorMessages = true;
    if ((paramString != null) && (!paramString.isEmpty()))
      try
      {
        this.properties.loadProperties(paramString);
      }
      catch (Exception localException)
      {
        System.err.println("Specified Preferrences file not found at " + paramString + ". If this file is within a jar ensure filename has jar: at the begining.\n\nLoading default properties.");
        this.properties.loadProperties();
      }
    else
      this.properties.loadProperties();
    setRootContainer(paramContainer);
  }

  public void addPageChangeListener(DocumentListener paramDocumentListener)
  {
    if (this.currentGUI != null)
      this.currentGUI.addPageChangeListener(paramDocumentListener);
  }

  public void setRootContainer(Container paramContainer)
  {
    if (paramContainer == null)
      throw new RuntimeException("Null containers not allowed.");
    Object localObject = paramContainer;
    JPanel localJPanel;
    if ((paramContainer instanceof JTabbedPane))
    {
      localJPanel = new JPanel(new BorderLayout());
      paramContainer.add(localJPanel);
      localObject = localJPanel;
    }
    else if ((paramContainer instanceof JScrollPane))
    {
      localJPanel = new JPanel(new BorderLayout());
      ((JScrollPane)paramContainer).getViewport().add(localJPanel);
      localObject = localJPanel;
    }
    else if ((paramContainer instanceof JSplitPane))
    {
      throw new RuntimeException("To add the viewer to a split pane please pass through either JSplitPane.getLeftComponent() or JSplitPane.getRightComponent()");
    }
    if (!(paramContainer instanceof JFrame))
      ((Container)localObject).setLayout(new BorderLayout());
    int i = Integer.parseInt(this.properties.getValue("startViewerWidth"));
    int j = Integer.parseInt(this.properties.getValue("startViewerHeight"));
    Dimension localDimension = Toolkit.getDefaultToolkit().getScreenSize();
    if (i < 0)
    {
      i = localDimension.width / 2;
      if (i < 700)
        i = 700;
      this.properties.setValue("startViewerWidth", "" + i);
    }
    if (j < 0)
    {
      j = localDimension.height / 2;
      this.properties.setValue("startViewerHeight", "" + j);
    }
    String str = System.getProperty("org.jpedal.startWindowSize");
    if (str != null)
    {
      StringTokenizer localStringTokenizer = new StringTokenizer(str, "x");
      System.out.println(localStringTokenizer.countTokens());
      if (localStringTokenizer.countTokens() != 2)
        throw new RuntimeException("Unable to use value for org.jpedal.startWindowSize=" + str + "\nValue should be in format org.jpedal.startWindowSize=200x300");
      try
      {
        i = Integer.parseInt(localStringTokenizer.nextToken().trim());
        j = Integer.parseInt(localStringTokenizer.nextToken().trim());
      }
      catch (Exception localException)
      {
        throw new RuntimeException("Unable to use value for org.jpedal.startWindowSize=" + str + "\nValue should be in format org.jpedal.startWindowSize=200x300");
      }
    }
    ((Container)localObject).setPreferredSize(new Dimension(i, j));
    this.currentGUI.setFrame((Container)localObject);
  }

  public void loadProperties(String paramString)
  {
    this.properties.loadProperties(paramString);
  }

  public void loadProperties(InputStream paramInputStream)
  {
    this.properties.loadProperties(paramInputStream);
  }

  public void setupViewer()
  {
    String str1 = System.getProperty("org.jpedal.suppressViewerPopups");
    int i = 0;
    if ((str1 != null) && (str1.toLowerCase().equals("true")))
      i = 1;
    String str2 = this.properties.getValue("searchWindowType");
    if ((str2 != null) && (!str2.isEmpty()))
    {
      int j = Integer.parseInt(str2);
      this.searchFrame.setStyle(j);
    }
    else
    {
      this.searchFrame.setStyle(SwingSearchWindow.SEARCH_MENU_BAR);
    }
    if ((this.searchFrame instanceof SwingSearchWindow))
      ((SwingSearchWindow)this.searchFrame).setDefaultCloseOperation(2);
    this.currentGUI.setSearchFrame(this.searchFrame);
    String str3 = System.getProperty("org.jpedal.thumbnail");
    if (str3 != null)
    {
      if (str3.equals("true"))
        this.thumbnails.setThumbnailsEnabled(true);
      else if (str3.equals("false"))
        this.thumbnails.setThumbnailsEnabled(false);
    }
    else
      this.thumbnails.setThumbnailsEnabled(true);
    String str4 = System.getProperty("org.jpedal.bundleLocation");
    Object localObject2;
    if (str4 != null)
    {
      ClassLoader localClassLoader = Messages.class.getClassLoader();
      localObject2 = str4.replaceAll("\\.", "/") + '_' + Locale.getDefault().getLanguage() + ".properties";
      try
      {
        localObject1 = new BufferedReader(new InputStreamReader(localClassLoader.getResourceAsStream((String)localObject2)));
        ((BufferedReader)localObject1).close();
      }
      catch (IOException localIOException)
      {
        Locale.setDefault(new Locale("en", "EN"));
        this.currentGUI.showMessageDialog("No locale file " + (String)localObject2 + " has been defined for this Locale - using English as Default" + "\n Format is path, using '.' as break ie org.jpedal.international.messages");
      }
      init(ResourceBundle.getBundle(str4));
    }
    else
    {
      init(null);
    }
    this.currentGUI.init(this.scalingValues, this.currentCommands, this.currentPrinter);
    this.mouseHandler.setupMouse();
    if (this.searchFrame.getStyle() == SwingSearchWindow.SEARCH_TABBED_PANE)
      this.currentGUI.searchInTab(this.searchFrame);
    this.decode_pdf.getDynamicRenderer().setMessageFrame(this.currentGUI.getFrame());
    Object localObject1 = this.properties.getValue("showfirsttimepopup");
    int k = (i == 0) && (!((String)localObject1).isEmpty()) && (((String)localObject1).equals("true")) ? 1 : 0;
    if (k != 0)
    {
      this.currentGUI.showFirstTimePopup();
      this.properties.setValue("showfirsttimepopup", "false");
    }
    if ((i == 0) && (JAIHelper.isJAIused()))
    {
      localObject1 = this.properties.getValue("showddmessage");
      if ((this.properties != null) && (!((String)localObject1).isEmpty()) && (((String)localObject1).equals("true")))
      {
        this.currentGUI.showMessageDialog(Messages.getMessage("PdfViewer.JAIWarning") + Messages.getMessage("PdfViewer.JAIWarning1") + Messages.getMessage("PdfViewer.JAIWarning2") + Messages.getMessage("PdfViewer.JAIWarning3") + Messages.getMessage("PdfViewer.JAIWarning4"));
        this.properties.setValue("showddmessage", "false");
      }
    }
    if (this.currentGUI.isSingle())
    {
      localObject2 = new SingleViewTransferHandler(this.commonValues, this.currentGUI, this.currentCommands);
      this.decode_pdf.setTransferHandler((TransferHandler)localObject2);
    }
    else
    {
      localObject2 = new MultiViewTransferHandler(this.commonValues, this.currentGUI, this.currentCommands);
      this.currentGUI.getMultiViewerFrames().setTransferHandler((TransferHandler)localObject2);
    }
    boolean bool = false;
    localObject1 = this.properties.getValue("automaticupdate");
    if ((i == 0) && (!((String)localObject1).isEmpty()) && (((String)localObject1).equals("true")))
      bool = this.currentCommands.checkForUpdates(false);
    localObject1 = this.properties.getValue("displaytipsonstartup");
    if ((i == 0) && (!bool) && (!((String)localObject1).isEmpty()) && (((String)localObject1).equals("true")))
      SwingUtilities.invokeLater(new Runnable()
      {
        public void run()
        {
          TipOfTheDay localTipOfTheDay = new TipOfTheDay(Viewer.this.currentGUI.getFrame(), "/org/jpedal/examples/viewer/res/tips", Viewer.this.properties);
          localTipOfTheDay.setVisible(true);
        }
      });
    this.isSetup = true;
  }

  protected void init(ResourceBundle paramResourceBundle)
  {
    if (paramResourceBundle == null)
      try
      {
        Messages.setBundle(ResourceBundle.getBundle("org.jpedal.international.messages"));
      }
      catch (Exception localException1)
      {
        LogWriter.writeLog("Exception " + localException1 + " loading resource bundle.\n" + "Also check you have a file in org.jpedal.international.messages to support Locale=" + Locale.getDefault());
      }
    else
      try
      {
        Messages.setBundle(paramResourceBundle);
      }
      catch (Exception localException2)
      {
        LogWriter.writeLog("Exception with bundle " + paramResourceBundle);
        localException2.printStackTrace();
      }
    this.scalingValues = new String[] { Messages.getMessage("PdfViewerScaleWindow.text"), Messages.getMessage("PdfViewerScaleHeight.text"), Messages.getMessage("PdfViewerScaleWidth.text"), "25%", "50%", "75%", "100%", "125%", "150%", "200%", "250%", "500%", "750%", "1000%" };
    if (SwingUtilities.isEventDispatchThread())
    {
      this.decode_pdf.setDisplayView(1, 2);
    }
    else
    {
      Runnable local2 = new Runnable()
      {
        public void run()
        {
          Viewer.this.decode_pdf.setDisplayView(1, 2);
        }
      };
      SwingUtilities.invokeLater(local2);
    }
    this.decode_pdf.addExternalHandler(this.currentGUI, 4);
    PdfDecoder.init(true);
    this.decode_pdf.setExtractionMode(0, 1.0F);
    FontMappings.setFontReplacements();
  }

  protected void createSwingMenu(boolean paramBoolean)
  {
    this.currentGUI.createMainMenu(paramBoolean);
  }

  public static void main(String[] paramArrayOfString)
  {
    try
    {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
    catch (Exception localException)
    {
      LogWriter.writeLog("Exception " + localException + " setting look and feel");
    }
    Viewer localViewer = new Viewer();
    localViewer.setupViewer();
    if (paramArrayOfString.length > 0)
    {
      localViewer.openDefaultFile(paramArrayOfString[0]);
    }
    else if ((localViewer.properties.getValue("openLastDocument").toLowerCase().equals("true")) && (localViewer.properties.getRecentDocuments() != null) && (localViewer.properties.getRecentDocuments().length > 1))
    {
      int i = Integer.parseInt(localViewer.properties.getValue("lastDocumentPage"));
      if (i < 0)
        i = 1;
      localViewer.openDefaultFileAtPage(localViewer.properties.getRecentDocuments()[0], i);
    }
  }

  private void openFile(File paramFile, String paramString)
  {
    try
    {
      boolean bool = this.currentCommands.openUpFile(paramFile.getCanonicalPath());
      String str = null;
      int i = -1;
      Object localObject;
      if (this.decode_pdf.getOutlineAsXML() != null)
      {
        localObject = this.decode_pdf.getOutlineAsXML().getFirstChild();
        if (localObject != null)
          str = this.currentGUI.getBookmark(paramString);
        if (str != null)
          i = Integer.parseInt((String)str);
      }
      if (str == null)
      {
        str = this.decode_pdf.getIO().convertNameToRef(paramString);
        if (str != null)
        {
          localObject = new OutlineObject((String)str);
          this.decode_pdf.getIO().readObject((PdfObject)localObject);
          if (bool)
            this.currentCommands.processPage();
          this.decode_pdf.getFormRenderer().getActionHandler().gotoDest((PdfObject)localObject, 3, 339034948);
        }
      }
      if (str == null)
        throw new PdfException("Unknown bookmark " + paramString);
      if (i > -1)
      {
        this.commonValues.setCurrentPage(i);
        if (bool)
          this.currentCommands.processPage();
      }
    }
    catch (Exception localException)
    {
      System.err.println("Exception " + localException + " processing file");
      Values.setProcessing(false);
    }
  }

  private void openFile(File paramFile, int paramInt)
  {
    try
    {
      boolean bool = this.currentCommands.openUpFile(paramFile.getCanonicalPath());
      this.commonValues.setCurrentPage(paramInt);
      if (bool)
        this.currentCommands.processPage();
    }
    catch (Exception localException)
    {
      System.err.println("Exception " + localException + " processing file");
      Values.setProcessing(false);
    }
  }

  public Object executeCommand(int paramInt, Object[] paramArrayOfObject)
  {
    if (!this.isSetup)
      throw new RuntimeException("You must call viewer.setupViewer(); before you call any commands");
    return this.currentCommands.executeCommand(paramInt, paramArrayOfObject);
  }

  public SearchList getSearchResults()
  {
    return this.currentCommands.getSearchList();
  }

  public static boolean isProcessing()
  {
    return Values.isProcessing();
  }

  public boolean isExecutingCommand()
  {
    return this.currentCommands.isExecutingCommand();
  }

  public void addExternalHandler(Object paramObject, int paramInt)
  {
    this.decode_pdf.addExternalHandler(paramObject, paramInt);
  }

  public void dispose()
  {
    this.commonValues = null;
    this.currentPrinter = null;
    if (this.thumbnails != null)
      this.thumbnails.dispose();
    this.thumbnails = null;
    if (this.properties != null)
      this.properties.dispose();
    this.properties = null;
    if (this.currentGUI != null)
      this.currentGUI.dispose();
    this.currentGUI = null;
    this.searchFrame = null;
    this.currentCommands = null;
    this.mouseHandler = null;
    this.scalingValues = null;
    if (this.decode_pdf != null)
      this.decode_pdf.dispose();
    this.decode_pdf = null;
    Messages.dispose();
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.Viewer
 * JD-Core Version:    0.6.2
 */