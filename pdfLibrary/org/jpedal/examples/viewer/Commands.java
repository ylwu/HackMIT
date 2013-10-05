package org.jpedal.examples.viewer;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyVetoException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JViewport;
import javax.swing.ListModel;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkEvent.EventType;
import javax.swing.event.HyperlinkListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.BadLocationException;
import org.jpedal.PdfDecoder;
import org.jpedal.constants.JPedalSettings;
import org.jpedal.display.Display;
import org.jpedal.display.Display.BoolValue;
import org.jpedal.display.swing.SingleDisplay;
import org.jpedal.examples.viewer.gui.GUI;
import org.jpedal.examples.viewer.gui.SwingGUI;
import org.jpedal.examples.viewer.gui.generic.GUIButton;
import org.jpedal.examples.viewer.gui.generic.GUICombo;
import org.jpedal.examples.viewer.gui.generic.GUISearchWindow;
import org.jpedal.examples.viewer.gui.generic.GUIThumbnailPanel;
import org.jpedal.examples.viewer.gui.popups.AddHeaderFooterToPDFPages;
import org.jpedal.examples.viewer.gui.popups.CropPDFPages;
import org.jpedal.examples.viewer.gui.popups.DeletePDFPages;
import org.jpedal.examples.viewer.gui.popups.DownloadProgress;
import org.jpedal.examples.viewer.gui.popups.EncryptPDFDocument;
import org.jpedal.examples.viewer.gui.popups.ErrorDialog;
import org.jpedal.examples.viewer.gui.popups.ExtractPDFPagesNup;
import org.jpedal.examples.viewer.gui.popups.InsertBlankPDFPage;
import org.jpedal.examples.viewer.gui.popups.RotatePDFPages;
import org.jpedal.examples.viewer.gui.popups.SaveBitmap;
import org.jpedal.examples.viewer.gui.popups.SaveImage;
import org.jpedal.examples.viewer.gui.popups.SavePDF;
import org.jpedal.examples.viewer.gui.popups.SaveText;
import org.jpedal.examples.viewer.gui.popups.SignWizardModel;
import org.jpedal.examples.viewer.gui.popups.StampImageToPDFPages;
import org.jpedal.examples.viewer.gui.popups.StampTextToPDFPages;
import org.jpedal.examples.viewer.gui.popups.TipOfTheDay;
import org.jpedal.examples.viewer.gui.popups.UpdateDialog;
import org.jpedal.examples.viewer.gui.popups.Wizard;
import org.jpedal.examples.viewer.gui.swing.SearchList;
import org.jpedal.examples.viewer.gui.swing.SwingSearchWindow;
import org.jpedal.examples.viewer.objects.SignData;
import org.jpedal.examples.viewer.utils.Exporter;
import org.jpedal.examples.viewer.utils.FileFilterer;
import org.jpedal.examples.viewer.utils.IconiseImage;
import org.jpedal.examples.viewer.utils.ItextFunctions;
import org.jpedal.examples.viewer.utils.Printer;
import org.jpedal.examples.viewer.utils.PropertiesFile;
import org.jpedal.exception.PdfException;
import org.jpedal.external.JPedalActionHandler;
import org.jpedal.grouping.PdfGroupingAlgorithms;
import org.jpedal.io.JAIHelper;
import org.jpedal.io.JAITiffHelper;
import org.jpedal.io.PdfObjectReader;
import org.jpedal.linear.LinearThread;
import org.jpedal.objects.GraphicsState;
import org.jpedal.objects.PdfFileInformation;
import org.jpedal.objects.PdfPageData;
import org.jpedal.objects.acroforms.AcroRenderer;
import org.jpedal.objects.acroforms.ReturnValues;
import org.jpedal.objects.acroforms.actions.ActionHandler;
import org.jpedal.objects.raw.OutlineObject;
import org.jpedal.objects.raw.PdfObject;
import org.jpedal.parser.DecoderOptions;
import org.jpedal.render.DynamicVectorRenderer;
import org.jpedal.render.output.io.DefaultIO;
import org.jpedal.text.TextLines;
import org.jpedal.utils.BrowserLauncher;
import org.jpedal.utils.JavaFXHelper;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.Messages;
import org.jpedal.utils.Strip;
import org.jpedal.utils.SwingWorker;
import org.jpedal.utils.repositories.Vector_Rectangle;

public class Commands
{
  public static boolean hires = true;
  public boolean extractingAsImage = false;
  private int lastPageDecoded = -1;
  public static final int INFO = 1;
  public static final int BITMAP = 2;
  public static final int IMAGES = 3;
  public static final int TEXT = 4;
  public static final int SAVE = 5;
  public static final int PRINT = 6;
  public static final int EXIT = 7;
  public static final int AUTOSCROLL = 8;
  public static final int DOCINFO = 9;
  public static final int OPENFILE = 10;
  public static final int BOOKMARK = 11;
  public static final int FIND = 12;
  public static final int SNAPSHOT = 13;
  public static final int OPENURL = 14;
  public static final int VISITWEBSITE = 15;
  public static final int PREVIOUSDOCUMENT = 16;
  public static final int NEXTDOCUMENT = 17;
  public static final int PREVIOUSRESULT = 18;
  public static final int NEXTRESULT = 19;
  public static final int TIP = 20;
  public static final int CASCADE = 21;
  public static final int TILE = 22;
  public static final int UPDATE = 23;
  public static final int PREFERENCES = 24;
  public static final int COPY = 25;
  public static final int SELECTALL = 26;
  public static final int DESELECTALL = 27;
  public static final int UPDATEGUILAYOUT = 28;
  public static final int MOUSEMODE = 29;
  public static final int PANMODE = 30;
  public static final int TEXTSELECT = 31;
  public static final int SEPARATECOVER = 32;
  public static final int FIRSTPAGE = 50;
  public static final int FBACKPAGE = 51;
  public static final int BACKPAGE = 52;
  public static final int FORWARDPAGE = 53;
  public static final int FFORWARDPAGE = 54;
  public static final int LASTPAGE = 55;
  public static final int GOTO = 56;
  public static final int SINGLE = 57;
  public static final int CONTINUOUS = 58;
  public static final int CONTINUOUS_FACING = 59;
  public static final int FACING = 60;
  public static final int PAGEFLOW = 62;
  public static final int FULLSCREEN = 61;
  public static final int RSS = 997;
  public static final int HELP = 998;
  public static final int BUY = 999;
  public static final int QUALITY = 250;
  public static final int ROTATION = 251;
  public static final int SCALING = 252;
  public static final int SAVEFORM = 500;
  public static final int PDF = 501;
  public static final int ROTATE = 502;
  public static final int DELETE = 503;
  public static final int ADD = 504;
  public static final int SECURITY = 505;
  public static final int ADDHEADERFOOTER = 506;
  public static final int STAMPTEXT = 507;
  public static final int STAMPIMAGE = 508;
  public static final int SETCROP = 509;
  public static final int NUP = 510;
  public static final int HANDOUTS = 511;
  public static final int SIGN = 512;
  public static final int HIGHLIGHT = 600;
  public static final int SCROLL = 601;
  public static final int ADDVIEW = 700;
  public static final int FORWARD = 701;
  public static final int BACK = 702;
  public static final int PAGECOUNT = 703;
  public static final int CURRENTPAGE = 704;
  public static final int GETOUTLINEPANEL = 705;
  public static final int GETTHUMBNAILPANEL = 706;
  public static final int GETPAGECOUNTER = 707;
  public static final int PAGEGROUPING = 708;
  public static final int SETPAGECOLOR = 709;
  public static final int SETUNDRAWNPAGECOLOR = 710;
  public static final int REPLACETEXTCOLOR = 711;
  public static final int SETTEXTCOLOR = 712;
  public static final int CHANGELINEART = 713;
  public static final int SETDISPLAYBACKGROUND = 714;
  public static final int SETREPLACEMENTCOLORTHRESHOLD = 715;
  private boolean isPDf = false;
  public static final Integer FIRST_DOCUMENT_SEARCH_RESULT_NOW_SHOWN = Integer.valueOf(1);
  public static final Integer SEARCH_RETURNED_TO_START = Integer.valueOf(2);
  public static final Integer SEARCH_NOT_FOUND = Integer.valueOf(3);
  private boolean allHighlightsShown = false;
  private Values commonValues;
  private SwingGUI currentGUI;
  private PdfDecoder decode_pdf;
  private GUIThumbnailPanel thumbnails;
  Window win;
  private Point screenPosition = null;
  private int tiffImageToLoad = 0;
  public InputStream inputStream = null;
  private JAITiffHelper tiffHelper = null;
  private BufferedImage img = null;
  private int noOfRecentDocs;
  private RecentDocuments recent;
  private JMenuItem[] recentDocuments;
  private final Font headFont = new Font("SansSerif", 1, 10);
  private boolean display = true;
  private PropertiesFile properties;
  private final GUISearchWindow searchFrame;
  private SearchList results;
  private Printer currentPrinter;
  private boolean irregularSizesWarningShown = false;
  private boolean fileIsURL;
  private boolean openingTransferedFile;
  private MouseMode mouseMode = new MouseMode();
  private boolean executingCommand = false;
  int startX = 0;
  int startY = 0;
  private MultiViewListener multiViewListener;
  private boolean pageTurnAnimating = false;
  protected ViewStack viewStack = new ViewStack();

  /** @deprecated */
  public Display getPages()
  {
    return this.decode_pdf.getPages();
  }

  public Commands(Values paramValues, SwingGUI paramSwingGUI, PdfDecoder paramPdfDecoder, GUIThumbnailPanel paramGUIThumbnailPanel, PropertiesFile paramPropertiesFile, GUISearchWindow paramGUISearchWindow, Printer paramPrinter)
  {
    this.commonValues = paramValues;
    this.currentGUI = paramSwingGUI;
    this.decode_pdf = paramPdfDecoder;
    this.thumbnails = paramGUIThumbnailPanel;
    this.properties = paramPropertiesFile;
    this.currentPrinter = paramPrinter;
    this.noOfRecentDocs = paramPropertiesFile.getNoRecentDocumentsToDisplay();
    this.recentDocuments = new JMenuItem[this.noOfRecentDocs];
    this.recent = new RecentDocuments(this.noOfRecentDocs, paramPropertiesFile);
    this.searchFrame = paramGUISearchWindow;
  }

  public void addToRecentDocuments(String paramString)
  {
    this.recent.addToFileList(paramString);
  }

  public boolean isExecutingCommand()
  {
    return this.executingCommand;
  }

  public Object executeCommand(int paramInt, Object[] paramArrayOfObject)
  {
    if ((paramArrayOfObject != null) && (paramArrayOfObject.length == 1) && (paramArrayOfObject[0] == null))
      paramArrayOfObject = null;
    Object localObject1 = null;
    this.executingCommand = true;
    int i = 0;
    Map localMap = (Map)this.decode_pdf.getExternalHandler(24);
    Object localObject2;
    if (localMap != null)
    {
      localObject2 = (JPedalActionHandler)localMap.get(Integer.valueOf(paramInt));
      if (localObject2 != null)
      {
        ((JPedalActionHandler)localObject2).actionPerformed(this.currentGUI, this);
        return null;
      }
    }
    int k;
    Object localObject4;
    int n;
    Object localObject11;
    Object localObject3;
    Object localObject6;
    int i17;
    int i6;
    int i11;
    int i13;
    Object localObject15;
    Object localObject12;
    String str1;
    int i1;
    Object localObject14;
    Object localObject17;
    Object localObject18;
    Object localObject20;
    Object localObject16;
    switch (paramInt)
    {
    case 600:
      this.decode_pdf.getTextLines().clearHighlights();
      if (paramArrayOfObject == null)
        break label11442;
      localObject2 = (Rectangle[])paramArrayOfObject[0];
      k = ((Integer)paramArrayOfObject[1]).intValue();
      boolean bool3 = true;
      if (paramArrayOfObject.length > 2)
        bool3 = ((Boolean)paramArrayOfObject[2]).booleanValue();
      this.decode_pdf.getTextLines().addHighlights((Rectangle[])localObject2, bool3, k);
      this.decode_pdf.invalidate();
      this.decode_pdf.repaint();
      break;
    case 601:
      if (paramArrayOfObject == null)
        break label11442;
      localObject2 = (Rectangle)paramArrayOfObject[0];
      k = this.commonValues.getCurrentPage();
      if ((paramArrayOfObject.length > 1) && (paramArrayOfObject[1] != null))
        k = ((Integer)paramArrayOfObject[1]).intValue();
      if (localObject2 != null)
      {
        scrollRectToHighlight((Rectangle)localObject2, k);
        this.decode_pdf.invalidate();
        this.decode_pdf.repaint();
      }
      break;
    case 1:
      if (paramArrayOfObject != null)
        break label11442;
      this.currentGUI.getInfoBox();
      break;
    case 2:
      if (paramArrayOfObject != null)
        break label11442;
      if (this.commonValues.getSelectedFile() == null)
      {
        this.currentGUI.showMessageDialog(Messages.getMessage("PdfViewer.OpenFile"));
      }
      else
      {
        localObject2 = new SaveBitmap(this.commonValues.getInputDir(), this.commonValues.getPageCount(), this.commonValues.getCurrentPage());
        k = ((SaveBitmap)localObject2).display(this.currentGUI.getFrame(), Messages.getMessage("PdfViewer.SaveAsBitmap"));
        if (this.fileIsURL)
        {
          this.currentGUI.showMessageDialog(Messages.getMessage("PdfViewerMessage.CannotExportFromURL"));
        }
        else if (k == 0)
        {
          localObject4 = new Exporter(this.currentGUI, this.commonValues.getSelectedFile(), this.decode_pdf);
          ((Exporter)localObject4).extractPagesAsImages((SaveBitmap)localObject2);
        }
      }
      break;
    case 3:
      if (paramArrayOfObject != null)
        break label11442;
      if (this.commonValues.getSelectedFile() == null)
      {
        this.currentGUI.showMessageDialog(Messages.getMessage("PdfViewer.NoFile"));
      }
      else
      {
        localObject2 = new SaveImage(this.commonValues.getInputDir(), this.commonValues.getPageCount(), this.commonValues.getCurrentPage());
        k = ((SaveImage)localObject2).display(this.currentGUI.getFrame(), Messages.getMessage("PdfViewerTitle.SaveImagesFromPageRange"));
        if (this.fileIsURL)
        {
          this.currentGUI.showMessageDialog(Messages.getMessage("PdfViewerMessage.CannotExportFromURL"));
        }
        else if (k == 0)
        {
          localObject4 = new Exporter(this.currentGUI, this.commonValues.getSelectedFile(), this.decode_pdf);
          ((Exporter)localObject4).extractImagesOnPages((SaveImage)localObject2);
        }
      }
      break;
    case 4:
      if (paramArrayOfObject != null)
        break label11442;
      if (this.commonValues.getSelectedFile() == null)
      {
        this.currentGUI.showMessageDialog(Messages.getMessage("PdfViewer.NoFile"));
      }
      else if (!this.decode_pdf.isExtractionAllowed())
      {
        this.currentGUI.showMessageDialog("Not allowed");
      }
      else
      {
        localObject2 = new SaveText(this.commonValues.getInputDir(), this.commonValues.getPageCount(), this.commonValues.getCurrentPage());
        k = ((SaveText)localObject2).display(this.currentGUI.getFrame(), Messages.getMessage("PdfViewerTitle.SaveTextFromPageRange"));
        if (this.fileIsURL)
        {
          this.currentGUI.showMessageDialog(Messages.getMessage("PdfViewerMessage.CannotExportFromURL"));
        }
        else if (k == 0)
        {
          localObject4 = new Exporter(this.currentGUI, this.commonValues.getSelectedFile(), this.decode_pdf);
          ((Exporter)localObject4).extractTextOnPages((SaveText)localObject2);
        }
      }
      break;
    case 5:
      if (paramArrayOfObject != null)
        break label11442;
      saveFile();
      break;
    case 6:
      if (paramArrayOfObject != null)
        break label11442;
      if (this.commonValues.getSelectedFile() != null)
      {
        if (!Printer.isPrinting())
        {
          if (!this.commonValues.isPDF())
          {
            this.currentGUI.showMessageDialog(Messages.getMessage("PdfViewer.ImagePrinting"));
          }
          else
          {
            localObject2 = this.properties.getValue("defaultPrinter");
            if (((localObject2 == null) || (((String)localObject2).isEmpty())) && (PrintServiceLookup.lookupDefaultPrintService() != null))
              localObject2 = PrintServiceLookup.lookupDefaultPrintService().getName();
            this.currentPrinter.printPDF(this.decode_pdf, this.currentGUI, this.properties.getValue("printerBlacklist"), (String)localObject2);
          }
        }
        else
          this.currentGUI.showMessageDialog(Messages.getMessage("PdfViewerPrintFinish.message"));
      }
      else
        this.currentGUI.showMessageDialog(Messages.getMessage("PdfViewerNoFile.message"));
      break;
    case 7:
      if (paramArrayOfObject != null)
        break label11442;
      if (Printer.isPrinting())
        this.currentGUI.showMessageDialog(Messages.getMessage("PdfViewerStillPrinting.text"));
      else
        exit();
      break;
    case 8:
      if (paramArrayOfObject != null)
        break label11442;
      this.currentGUI.toogleAutoScrolling();
      break;
    case 9:
      if (paramArrayOfObject != null)
        break label11442;
      if (!this.commonValues.isPDF())
        this.currentGUI.showMessageDialog(Messages.getMessage("PdfViewer.ImageSearch"));
      else
        this.currentGUI.showDocumentProperties(this.commonValues.getSelectedFile(), this.commonValues.getFileSize(), this.commonValues.getPageCount(), this.commonValues.getCurrentPage());
      break;
    case 10:
      this.inputStream = null;
      if (paramArrayOfObject == null)
      {
        handleUnsaveForms();
        if (Printer.isPrinting())
        {
          this.currentGUI.showMessageDialog(Messages.getMessage("PdfViewerPrintWait.message"));
        }
        else if (Values.isProcessing())
        {
          this.currentGUI.showMessageDialog(Messages.getMessage("PdfViewerDecodeWait.message"));
        }
        else
        {
          selectFile();
          this.fileIsURL = false;
        }
      }
      else
      {
        String str6;
        if ((paramArrayOfObject.length == 2) && ((paramArrayOfObject[0] instanceof byte[])) && ((paramArrayOfObject[1] instanceof String)))
        {
          localObject2 = (byte[])paramArrayOfObject[0];
          String str3 = (String)paramArrayOfObject[1];
          this.commonValues.setFileSize(localObject2.length);
          this.commonValues.setSelectedFile(str3);
          this.currentGUI.setViewerTitle(null);
          if ((this.commonValues.getSelectedFile() != null) && (!Values.isProcessing()))
          {
            this.decode_pdf.resetViewableArea();
            try
            {
              this.isPDf = true;
              this.currentGUI.setMultiPageTiff(false);
              localObject4 = System.getProperty("org.jpedal.hires");
              if ((hires) || (localObject4 != null))
                this.commonValues.setUseHiresImage(true);
              str6 = System.getProperty("org.jpedal.memory");
              if (str6 != null)
                this.commonValues.setUseHiresImage(false);
              this.thumbnails.resetToDefault();
              this.currentGUI.setNoPagesDecoded();
              if (this.searchFrame != null)
                this.searchFrame.removeSearchWindow(false);
              this.commonValues.maxViewY = 0;
              this.currentGUI.setQualityBoxVisible(this.commonValues.isPDF());
              this.commonValues.setCurrentPage(1);
              if (this.currentGUI.isSingle())
                this.decode_pdf.closePdfFile();
              this.decode_pdf.openPdfArray((byte[])localObject2);
              this.currentGUI.updateStatusMessage("opening file");
              int i10 = 1;
              if ((this.decode_pdf.isEncrypted()) && (!this.decode_pdf.isFileViewable()))
              {
                i10 = 0;
                String str7 = System.getProperty("org.jpedal.password");
                if (str7 == null)
                  str7 = this.currentGUI.showInputDialog(Messages.getMessage("PdfViewerPassword.message"));
                if (str7 != null)
                {
                  this.decode_pdf.setEncryptionPassword(str7);
                  if (this.decode_pdf.isFileViewable())
                    i10 = 1;
                }
                if (i10 == 0)
                  this.currentGUI.showMessageDialog(Messages.getMessage("PdfViewerPasswordRequired.message"));
              }
              if (i10 != 0)
              {
                if (this.properties.getValue("Recentdocuments").equals("true"))
                {
                  this.properties.addRecentDocument(this.commonValues.getSelectedFile());
                  updateRecentDocuments(this.properties.getRecentDocuments());
                }
                addToRecentDocuments(this.commonValues.getSelectedFile());
                this.commonValues.setCurrentPage(1);
              }
              processPage();
            }
            catch (PdfException localPdfException2)
            {
            }
          }
        }
        else if (paramArrayOfObject.length >= 1)
        {
          if ((paramArrayOfObject[0] instanceof InputStream))
          {
            this.inputStream = ((InputStream)paramArrayOfObject[0]);
            this.currentGUI.resetNavBar();
            localObject2 = new StringBuilder().append("InputStream-").append(System.currentTimeMillis()).append(".pdf").toString();
            this.commonValues.setSelectedFile((String)localObject2);
            this.fileIsURL = true;
            if (this.inputStream != null)
            {
              try
              {
                this.commonValues.setFileSize(0L);
                this.currentGUI.setViewerTitle(null);
              }
              catch (Exception localException1)
              {
                localException1.printStackTrace();
              }
              if (!Values.isProcessing())
              {
                this.thumbnails.terminateDrawing();
                this.decode_pdf.flushObjectValues(true);
                this.decode_pdf.resetViewableArea();
                this.currentGUI.stopThumbnails();
                try
                {
                  openFile(this.commonValues.getSelectedFile());
                  while (Values.isProcessing())
                    Thread.sleep(1000L);
                }
                catch (Exception localException2)
                {
                }
              }
            }
            else
            {
              this.decode_pdf.repaint();
            }
          }
          else
          {
            String str4;
            Object localObject5;
            if ((paramArrayOfObject[0] instanceof File))
            {
              localObject2 = (File)paramArrayOfObject[0];
            }
            else if ((paramArrayOfObject[0] instanceof String))
            {
              str4 = (String)paramArrayOfObject[0];
              localObject5 = str4.toCharArray();
              if ((localObject5[1] == ':') || (localObject5[0] == '\\') || (localObject5[0] == '/'))
              {
                localObject2 = new File(str4);
              }
              else
              {
                str6 = new File(this.commonValues.getSelectedFile()).getParent();
                localObject2 = new File(str6, str4);
                try
                {
                  localObject2 = ((File)localObject2).getCanonicalFile();
                }
                catch (Exception localException8)
                {
                  localObject2 = new File(str6, str4);
                }
              }
            }
            else
            {
              localObject2 = null;
            }
            if (localObject2 != null)
            {
              try
              {
                this.commonValues.setInputDir(((File)localObject2).getParentFile().getCanonicalPath());
                open(((File)localObject2).getAbsolutePath());
                str4 = null;
                if ((paramArrayOfObject.length > 1) && ((paramArrayOfObject[1] instanceof String)))
                {
                  localObject5 = (String)paramArrayOfObject[1];
                  str4 = this.decode_pdf.getIO().convertNameToRef((String)localObject5);
                }
                if (str4 != null)
                {
                  localObject5 = new OutlineObject(str4);
                  this.decode_pdf.getIO().readObject((PdfObject)localObject5);
                  this.decode_pdf.getFormRenderer().getActionHandler().gotoDest((PdfObject)localObject5, 3, 339034948);
                }
                while (Values.isProcessing())
                  try
                  {
                    Thread.sleep(100L);
                  }
                  catch (InterruptedException localInterruptedException1)
                  {
                    localInterruptedException1.printStackTrace();
                  }
              }
              catch (IOException localIOException3)
              {
              }
            }
            else
            {
              this.decode_pdf.repaint();
              this.currentGUI.showMessageDialog(Messages.getMessage("PdfViewerMessage.NoSelection"));
            }
          }
        }
      }
      break;
    case 11:
      if ((paramArrayOfObject.length < 1) || (this.currentGUI == null))
        break label11442;
      localObject2 = (String)paramArrayOfObject[0];
      this.currentGUI.setBookmarks(true);
      String str5 = this.currentGUI.getBookmark((String)localObject2);
      if (str5 != null)
      {
        n = Integer.parseInt(str5);
        try
        {
          this.decode_pdf.decodePage(n);
          this.decode_pdf.repaint();
        }
        catch (Exception localException6)
        {
          localException6.printStackTrace();
        }
      }
      break;
    case 13:
      if (paramArrayOfObject != null)
        break label11442;
      if (this.decode_pdf.getDisplayView() != 1)
      {
        this.currentGUI.showMessageDialog(Messages.getMessage("PageLayoutMessage.SinglePageOnly"));
      }
      else
      {
        this.extractingAsImage = true;
        DecoderOptions.showMouseBox = true;
        this.decode_pdf.setCursor(Cursor.getPredefinedCursor(1));
      }
      break;
    case 12:
      if (paramArrayOfObject == null)
      {
        if (this.commonValues.getSelectedFile() == null)
          this.currentGUI.showMessageDialog(Messages.getMessage("PdfViewer.NoFile"));
        else if (!this.commonValues.isPDF())
          this.currentGUI.showMessageDialog(Messages.getMessage("PdfViewer.ImageSearch"));
        else if ((this.decode_pdf.getDisplayView() != 1) && (this.decode_pdf.getDisplayView() != 2) && (this.decode_pdf.getDisplayView() != 4) && (this.decode_pdf.getDisplayView() != 3))
          this.currentGUI.showMessageDialog(Messages.getMessage("PageLayoutMessage.SingleContfacingFacingPageOnly"));
        else if (!this.searchFrame.isSearchVisible())
          this.searchFrame.find(this.decode_pdf, this.commonValues);
        this.searchFrame.grabFocusInInput();
        if (this.decode_pdf.getTextLines().getHighlightedAreas(this.commonValues.getCurrentPage()) != null)
        {
          localObject2 = copySelectedText();
          ((SwingSearchWindow)this.searchFrame).setSearchText((String)localObject2);
        }
      }
      else
      {
        localObject11 = (String)paramArrayOfObject[0];
        boolean bool1;
        boolean bool2;
        if (paramArrayOfObject.length > 1)
        {
          n = ((Integer)paramArrayOfObject[1]).intValue();
          bool1 = ((Boolean)paramArrayOfObject[2]).booleanValue();
          bool2 = ((Boolean)paramArrayOfObject[3]).booleanValue();
        }
        else
        {
          n = 0;
          bool1 = false;
          bool2 = false;
        }
        this.allHighlightsShown = ((n & 0x10) == 16);
        this.searchFrame.findWithoutWindow(this.decode_pdf, this.commonValues, n, bool1, bool2, (String)localObject11);
      }
      break;
    case 708:
      if (paramArrayOfObject == null)
        break label11442;
      int j = ((Integer)paramArrayOfObject[0]).intValue();
      if (j == this.decode_pdf.getlastPageDecoded())
        try
        {
          localObject1 = this.decode_pdf.getGroupingObject();
        }
        catch (PdfException localPdfException1)
        {
          localPdfException1.printStackTrace();
        }
      else
        try
        {
          this.decode_pdf.decodePageInBackground(j);
          localObject1 = this.decode_pdf.getBackgroundGroupingObject();
        }
        catch (Exception localException3)
        {
          localException3.printStackTrace();
        }
      this.decode_pdf.waitForDecodingToFinish();
      break;
    case 14:
      this.inputStream = null;
      String str2;
      if (paramArrayOfObject == null)
      {
        handleUnsaveForms();
        this.currentGUI.resetNavBar();
        str2 = selectURL();
        if (str2 != null)
        {
          this.commonValues.setSelectedFile(str2);
          this.fileIsURL = true;
        }
      }
      else
      {
        this.currentGUI.resetNavBar();
        str2 = (String)paramArrayOfObject[0];
        if (str2 != null)
        {
          this.commonValues.setSelectedFile(str2);
          this.fileIsURL = true;
          int m = 0;
          try
          {
            URL localURL = new URL(str2);
            localObject11 = localURL.openConnection();
            if (((URLConnection)localObject11).getContent() == null)
              m = 1;
          }
          catch (Exception localException4)
          {
            m = 1;
          }
          if (m != 0)
            str2 = null;
          if (str2 != null)
          {
            this.commonValues.setFileSize(0L);
            this.currentGUI.setViewerTitle(null);
            if (!Values.isProcessing())
            {
              this.thumbnails.terminateDrawing();
              this.decode_pdf.flushObjectValues(true);
              this.decode_pdf.resetViewableArea();
              this.currentGUI.stopThumbnails();
              if (!this.currentGUI.isSingle())
                openNewMultiplePage(this.commonValues.getSelectedFile());
              try
              {
                openFile(this.commonValues.getSelectedFile());
                while (Values.isProcessing())
                  Thread.sleep(1000L);
              }
              catch (Exception localException5)
              {
              }
            }
          }
          else
          {
            this.decode_pdf.repaint();
          }
        }
      }
      break;
    case 998:
      if (paramArrayOfObject != null)
        break label11442;
      this.currentGUI.getHelpBox();
      break;
    case 999:
      if (paramArrayOfObject != null)
        break label11442;
      try
      {
        BrowserLauncher.openURL("http://www.idrsolutions.com/jpedal-pricing/");
      }
      catch (IOException localIOException1)
      {
        this.currentGUI.showMessageDialog("Please visit http://www.idrsolutions.com/jpedal-pricing/");
      }
    case 997:
      if (paramArrayOfObject != null)
        break label11442;
      this.currentGUI.getRSSBox();
      break;
    case 15:
      if (paramArrayOfObject != null)
        break label11442;
      try
      {
        BrowserLauncher.openURL(Messages.getMessage("PdfViewer.VisitWebsite"));
      }
      catch (IOException localIOException2)
      {
        this.currentGUI.showMessageDialog(Messages.getMessage("PdfViewer.ErrorWebsite"));
      }
    case 20:
      if (paramArrayOfObject != null)
        break label11442;
      localObject3 = new TipOfTheDay(this.currentGUI.getFrame(), "/org/jpedal/examples/viewer/res/tips", this.properties);
      ((TipOfTheDay)localObject3).setVisible(true);
      break;
    case 21:
      if (paramArrayOfObject != null)
        break label11442;
      cascade();
      break;
    case 22:
      if (paramArrayOfObject != null)
        break label11442;
      tile();
      break;
    case 23:
      if (paramArrayOfObject != null)
        break label11442;
      checkForUpdates(true);
      break;
    case 24:
      if (paramArrayOfObject != null)
        break label11442;
      this.currentGUI.showPreferencesDialog();
      break;
    case 25:
      localObject3 = copySelectedText();
      StringSelection localStringSelection = new StringSelection((String)localObject3);
      Toolkit.getDefaultToolkit().getSystemClipboard().setContents(localStringSelection, null);
      break;
    case 26:
      if (this.decode_pdf.getDisplayView() == 1)
      {
        localObject6 = this.decode_pdf.getTextLines().getLineAreas(this.commonValues.getCurrentPage());
        int i15;
        if (localObject6 != null)
        {
          int i16 = 0;
          i17 = 0;
          for (int i18 = 0; i18 != localObject6.length; i18++)
          {
            if (localObject6[i18].y > localObject6[i16].y)
              i16 = i18;
            if (localObject6[i18].y < localObject6[i17].y)
              i17 = i18;
          }
          i6 = localObject6[i16].x;
          i11 = localObject6[i16].y + localObject6[i16].height / 2;
          i15 = localObject6[i17].y + localObject6[i17].height / 2 - i11;
          i13 = localObject6[i17].x + localObject6[i17].width - i6;
        }
        else
        {
          i15 = this.decode_pdf.getPdfPageData().getCropBoxHeight(this.commonValues.getCurrentPage());
          i13 = this.decode_pdf.getPdfPageData().getCropBoxWidth(this.commonValues.getCurrentPage());
          i6 = this.decode_pdf.getPdfPageData().getCropBoxX(this.commonValues.getCurrentPage());
          i11 = this.decode_pdf.getPdfPageData().getCropBoxY(this.commonValues.getCurrentPage());
        }
        this.commonValues.m_x1 = i6;
        this.commonValues.m_x2 = (i6 + i13);
        this.commonValues.m_y1 = i11;
        this.commonValues.m_y2 = (i11 + i15);
        localObject15 = new Rectangle(i6, i11, i13, i15);
        this.decode_pdf.updateCursorBoxOnScreen(null, null);
        this.decode_pdf.getTextLines().clearHighlights();
        this.decode_pdf.getPages().setHighlightedImage(null);
        this.decode_pdf.updateCursorBoxOnScreen((Rectangle)localObject15, DecoderOptions.highlightColor);
        this.decode_pdf.getTextLines().addHighlights((Rectangle[])localObject6, true, this.commonValues.getCurrentPage());
        this.decode_pdf.repaint();
      }
      else
      {
        this.currentGUI.showMessageDialog(Messages.getMessage("PageLayoutMessage.SinglePageOnly"));
      }
      break;
    case 27:
      this.decode_pdf.updateCursorBoxOnScreen(null, null);
      this.decode_pdf.getTextLines().clearHighlights();
      this.decode_pdf.getPages().setHighlightedImage(null);
      break;
    case 28:
      if (paramArrayOfObject == null)
        break label11442;
      localObject6 = null;
      i6 = 0;
      if ((paramArrayOfObject[0] instanceof String))
        localObject6 = (String)paramArrayOfObject[0];
      boolean bool5;
      if ((paramArrayOfObject[1] instanceof Boolean))
        bool5 = ((Boolean)paramArrayOfObject[1]).booleanValue();
      if (localObject6 != null)
        this.currentGUI.alterProperty((String)localObject6, bool5);
      else
        throw new RuntimeException("String input was null");
      break;
    case 29:
      if (paramArrayOfObject != null)
        break label11442;
      if (this.mouseMode.getMouseMode() == 0)
      {
        this.mouseMode.setMouseMode(1);
        localObject6 = this.currentGUI.getURLForImage(new StringBuilder().append(this.currentGUI.getIconLocation()).append("mouse_pan.png").toString());
        if (localObject6 != null)
        {
          localObject12 = new ImageIcon((URL)localObject6);
          this.currentGUI.mouseMode.setIcon((ImageIcon)localObject12);
        }
        this.currentGUI.snapshotButton.setEnabled(false);
        this.decode_pdf.setDefaultCursor(this.currentGUI.getCursor(1));
      }
      else if (this.mouseMode.getMouseMode() == 1)
      {
        this.mouseMode.setMouseMode(0);
        localObject6 = this.currentGUI.getURLForImage(new StringBuilder().append(this.currentGUI.getIconLocation()).append("mouse_select.png").toString());
        if (localObject6 != null)
        {
          localObject12 = new ImageIcon((URL)localObject6);
          this.currentGUI.mouseMode.setIcon((ImageIcon)localObject12);
        }
        this.currentGUI.snapshotButton.setEnabled(true);
        this.decode_pdf.setDefaultCursor(Cursor.getPredefinedCursor(0));
      }
      break;
    case 31:
      if (paramArrayOfObject != null)
        break label11442;
      this.mouseMode.setMouseMode(0);
      localObject6 = this.currentGUI.getURLForImage(new StringBuilder().append(this.currentGUI.getIconLocation()).append("mouse_select.png").toString());
      if (localObject6 != null)
      {
        localObject12 = new ImageIcon((URL)localObject6);
        this.currentGUI.mouseMode.setIcon((ImageIcon)localObject12);
      }
      break;
    case 32:
      if (paramArrayOfObject != null)
        break label11442;
      this.decode_pdf.getPages().setBoolean(Display.BoolValue.SEPARATE_COVER, !this.decode_pdf.getPages().getBoolean(Display.BoolValue.SEPARATE_COVER));
      if (this.decode_pdf.getDisplayView() == 3)
        executeCommand(60, null);
      if (this.decode_pdf.getPages().getBoolean(Display.BoolValue.SEPARATE_COVER))
        this.properties.setValue("separateCoverOn", "true");
      else
        this.properties.setValue("separateCoverOn", "false");
      break;
    case 30:
      if (paramArrayOfObject != null)
        break label11442;
      this.mouseMode.setMouseMode(1);
      localObject6 = this.currentGUI.getURLForImage(new StringBuilder().append(this.currentGUI.getIconLocation()).append("mouse_pan.png").toString());
      if (localObject6 != null)
      {
        localObject12 = new ImageIcon((URL)localObject6);
        this.currentGUI.mouseMode.setIcon((ImageIcon)localObject12);
      }
      this.decode_pdf.setDefaultCursor(this.currentGUI.getCursor(1));
      break;
    case 16:
      if (paramArrayOfObject != null)
        break label11442;
      if (Printer.isPrinting())
      {
        this.currentGUI.showMessageDialog(Messages.getMessage("PdfViewerPrintWait.message"));
      }
      else if (Values.isProcessing())
      {
        this.currentGUI.showMessageDialog(Messages.getMessage("PdfViewerDecodeWait.message"));
      }
      else
      {
        str1 = this.recent.getPreviousDocument();
        if (str1 != null)
          open(str1);
      }
      break;
    case 17:
      if (paramArrayOfObject != null)
        break label11442;
      if (Printer.isPrinting())
      {
        this.currentGUI.showMessageDialog(Messages.getMessage("PdfViewerPrintWait.message"));
      }
      else if (Values.isProcessing())
      {
        this.currentGUI.showMessageDialog(Messages.getMessage("PdfViewerDecodeWait.message"));
      }
      else
      {
        str1 = this.recent.getNextDocument();
        if (str1 != null)
          open(str1);
      }
      break;
    case 18:
      if (paramArrayOfObject == null)
      {
        localObject1 = null;
        if (this.results == null)
          this.results = this.searchFrame.getResults(this.commonValues.getCurrentPage());
        i1 = this.results.getSelectedIndex();
        if (i1 < 0)
        {
          i1 = 0;
          this.results.setSelectedIndex(i1);
        }
        localObject12 = Integer.valueOf(this.commonValues.getCurrentPage());
        if ((i1 == 0) || (this.results.getResultCount() == 0))
        {
          i11 = this.commonValues.getCurrentPage() - 1;
          if (i11 < 1)
            i11 = this.commonValues.getPageCount();
          this.results = this.searchFrame.getResults(i11);
          while ((this.results.getResultCount() < 1) && (i11 > 0) && (this.searchFrame.getStyle() == SwingSearchWindow.SEARCH_MENU_BAR))
          {
            this.results = this.searchFrame.getResults(i11);
            i11--;
          }
          if ((this.results.getResultCount() < 1) && (i11 == 0))
          {
            i11 = this.commonValues.getPageCount();
            this.results = this.searchFrame.getResults(i11);
            localObject1 = SEARCH_RETURNED_TO_START;
            while ((this.results.getResultCount() < 1) && (i11 >= this.commonValues.getCurrentPage()) && (this.searchFrame.getStyle() == SwingSearchWindow.SEARCH_MENU_BAR))
            {
              this.results = this.searchFrame.getResults(i11);
              i11--;
            }
          }
          i1 = this.results.getResultCount() - 1;
          if (this.results.getResultCount() < 1)
            localObject1 = SEARCH_NOT_FOUND;
        }
        else
        {
          i1--;
        }
        this.currentGUI.setResults(this.results);
        this.results.setSelectedIndex(i1);
        if (!Values.isProcessing())
        {
          float f1 = this.currentGUI.getScaling();
          i13 = this.results.getSelectedIndex();
          if (!this.allHighlightsShown)
            this.decode_pdf.getTextLines().clearHighlights();
          if (i13 != -1)
          {
            localObject14 = Integer.valueOf(i13);
            localObject15 = this.results.getTextPages().get(localObject14);
            if (localObject15 != null)
            {
              i17 = ((Integer)localObject15).intValue();
              if (this.commonValues.getCurrentPage() != i17)
              {
                this.commonValues.setCurrentPage(i17);
                this.currentGUI.resetStatusMessage(new StringBuilder().append(Messages.getMessage("PdfViewer.LoadingPage")).append(' ').append(this.commonValues.getCurrentPage()).toString());
                this.decode_pdf.setPageParameters(f1, this.commonValues.getCurrentPage());
                this.currentGUI.decodePage();
                this.decode_pdf.invalidate();
              }
              while (Values.isProcessing());
              localObject17 = (Rectangle)this.searchFrame.getTextRectangles().get(localObject14);
              if ((((Integer)localObject12).intValue() != i17) && (this.allHighlightsShown))
              {
                localObject18 = new Vector_Rectangle();
                for (int i20 = 0; i20 != this.results.getModel().getSize(); i20++)
                {
                  Integer localInteger = Integer.valueOf(i20);
                  if (((Integer)this.results.getTextPages().get(localInteger)).intValue() == i17)
                  {
                    localObject20 = this.searchFrame.getTextRectangles().get(localInteger);
                    if ((localObject20 instanceof Rectangle))
                      ((Vector_Rectangle)localObject18).addElement((Rectangle)localObject20);
                    if ((localObject20 instanceof Rectangle[]))
                    {
                      Rectangle[] arrayOfRectangle2 = (Rectangle[])localObject20;
                      for (int i22 = 0; i22 != arrayOfRectangle2.length; i22++)
                        ((Vector_Rectangle)localObject18).addElement(arrayOfRectangle2[i22]);
                    }
                  }
                }
                ((Vector_Rectangle)localObject18).trim();
                Rectangle[] arrayOfRectangle1 = ((Vector_Rectangle)localObject18).get();
                this.decode_pdf.getTextLines().addHighlights(arrayOfRectangle1, true, i17);
              }
              scrollRectToHighlight((Rectangle)localObject17, this.commonValues.getCurrentPage());
              if (!this.allHighlightsShown)
                this.decode_pdf.getTextLines().addHighlights(new Rectangle[] { localObject17 }, true, i17);
              this.decode_pdf.getPages().refreshDisplay();
              this.decode_pdf.repaint();
            }
          }
        }
        this.currentGUI.hideRedundentNavButtons();
      }
      if ((this.commonValues.getCurrentPage() != this.searchFrame.getFirstPageWithResults()) || (this.results.getSelectedIndex() != 0))
        break label11442;
      localObject1 = FIRST_DOCUMENT_SEARCH_RESULT_NOW_SHOWN;
      break;
    case 19:
      if (paramArrayOfObject == null)
      {
        localObject1 = null;
        if (this.results == null)
          this.results = this.searchFrame.getResults(this.commonValues.getCurrentPage());
        i1 = this.results.getSelectedIndex();
        if (i1 < 0)
        {
          i1 = 0;
          this.results.setSelectedIndex(i1);
        }
        localObject12 = Integer.valueOf(this.commonValues.getCurrentPage());
        if ((i1 == this.results.getResultCount() - 1) || (this.results.getResultCount() == 0))
        {
          i1 = 0;
          int i12 = this.commonValues.getCurrentPage() + 1;
          if (i12 > this.commonValues.getPageCount())
            i12 = 1;
          this.results = this.searchFrame.getResults(i12);
          while ((this.results.getResultCount() < 1) && (i12 < this.commonValues.getPageCount() + 1) && (this.searchFrame.getStyle() == SwingSearchWindow.SEARCH_MENU_BAR))
          {
            this.results = this.searchFrame.getResults(i12);
            i12++;
          }
          if ((this.results.getResultCount() < 1) && (i12 == this.commonValues.getPageCount() + 1))
          {
            i12 = 1;
            localObject1 = SEARCH_RETURNED_TO_START;
            while ((this.results.getResultCount() < 1) && (i12 <= this.commonValues.getCurrentPage()) && (this.searchFrame.getStyle() == SwingSearchWindow.SEARCH_MENU_BAR))
            {
              this.results = this.searchFrame.getResults(i12);
              i12++;
            }
            if (this.results.getResultCount() < 1)
              localObject1 = SEARCH_NOT_FOUND;
          }
        }
        else
        {
          i1++;
        }
        this.currentGUI.setResults(this.results);
        this.results.setSelectedIndex(i1);
        float f2 = this.currentGUI.getScaling();
        i13 = this.results.getSelectedIndex();
        if (!this.allHighlightsShown)
          this.decode_pdf.getTextLines().clearHighlights();
        if (i13 != -1)
        {
          localObject14 = Integer.valueOf(i13);
          localObject15 = this.results.getTextPages().get(localObject14);
          if (localObject15 != null)
          {
            i17 = ((Integer)localObject15).intValue();
            if (this.commonValues.getCurrentPage() != i17)
            {
              this.commonValues.setCurrentPage(i17);
              this.currentGUI.resetStatusMessage(new StringBuilder().append(Messages.getMessage("PdfViewer.LoadingPage")).append(' ').append(this.commonValues.getCurrentPage()).toString());
              this.decode_pdf.setPageParameters(f2, this.commonValues.getCurrentPage());
              this.currentGUI.decodePage();
              this.decode_pdf.invalidate();
            }
            while (Values.isProcessing())
              try
              {
                Thread.sleep(500L);
              }
              catch (InterruptedException localInterruptedException4)
              {
                localInterruptedException4.printStackTrace();
              }
            if ((localObject12 != null) && (((Integer)localObject12).intValue() != i17) && (this.allHighlightsShown))
            {
              localObject17 = new Vector_Rectangle();
              for (int i19 = 0; i19 != this.results.getModel().getSize(); i19++)
              {
                localObject18 = Integer.valueOf(i19);
                if (((Integer)this.results.getTextPages().get(localObject18)).intValue() == i17)
                {
                  Object localObject19 = this.searchFrame.getTextRectangles().get(localObject18);
                  if ((localObject19 instanceof Rectangle))
                    ((Vector_Rectangle)localObject17).addElement((Rectangle)localObject19);
                  if ((localObject19 instanceof Rectangle[]))
                  {
                    localObject20 = (Rectangle[])localObject19;
                    for (int i21 = 0; i21 != localObject20.length; i21++)
                      ((Vector_Rectangle)localObject17).addElement(localObject20[i21]);
                  }
                }
              }
              ((Vector_Rectangle)localObject17).trim();
              localObject16 = ((Vector_Rectangle)localObject17).get();
              this.decode_pdf.getTextLines().addHighlights((Rectangle[])localObject16, true, i17);
            }
            if (!this.allHighlightsShown)
            {
              localObject17 = this.results.textAreas().get(localObject14);
              if ((localObject17 instanceof Rectangle))
                this.decode_pdf.getTextLines().addHighlights(new Rectangle[] { (Rectangle)localObject17 }, true, i17);
              else
                this.decode_pdf.getTextLines().addHighlights((Rectangle[])localObject17, true, i17);
            }
            this.decode_pdf.getPages().refreshDisplay();
            this.decode_pdf.repaint();
          }
        }
        this.currentGUI.hideRedundentNavButtons();
      }
      if ((this.commonValues.getCurrentPage() != this.searchFrame.getFirstPageWithResults()) || (this.results.getSelectedIndex() != 0))
        break label11442;
      localObject1 = FIRST_DOCUMENT_SEARCH_RESULT_NOW_SHOWN;
      break;
    case 50:
      if ((paramArrayOfObject != null) || (this.commonValues.getSelectedFile() == null) || (this.commonValues.getPageCount() <= 1) || (this.commonValues.getCurrentPage() == 1))
        break label11442;
      navigatePages(-(this.commonValues.getCurrentPage() - 1));
      break;
    case 51:
      if ((paramArrayOfObject != null) || (this.commonValues.getSelectedFile() == null))
        break label11442;
      if (this.commonValues.getCurrentPage() < 10)
        navigatePages(-(this.commonValues.getCurrentPage() - 1));
      else
        navigatePages(-10);
      break;
    case 52:
      if (paramArrayOfObject == null)
      {
        if (this.commonValues.getSelectedFile() == null)
          break label11442;
        navigatePages(-1);
        break label11442;
      }
      if (this.commonValues.getSelectedFile() != null)
        navigatePages(-Integer.parseInt((String)paramArrayOfObject[0]));
      break;
    case 53:
    case 54:
    case 55:
    case 56:
    case 57:
    case 58:
    case 59:
    case 60:
    case 62:
    case 61:
    case 250:
    case 252:
    case 251:
    case 500:
    case 501:
    case 502:
    case 509:
    case 510:
    case 511:
    case 512:
    case 503:
    case 506:
    case 507:
    case 508:
    case 504:
    case 505:
    case 702:
    case 701:
    case 700:
    case 704:
    case 703:
    case 707:
    case 706:
    case 705:
    case 709:
    case 710:
    case 711:
    case 712:
    case 714:
    case 713:
    case 715:
    }
    while (Values.isProcessing())
    {
      continue;
      if (paramArrayOfObject == null)
      {
        if (this.commonValues.getSelectedFile() != null)
          navigatePages(1);
      }
      else
      {
        if (this.commonValues.getSelectedFile() != null)
          navigatePages(Integer.parseInt((String)paramArrayOfObject[0]));
        while (Values.isProcessing())
        {
          continue;
          if ((paramArrayOfObject == null) && (this.commonValues.getSelectedFile() != null))
            if (this.commonValues.getPageCount() < this.commonValues.getCurrentPage() + 10)
            {
              navigatePages(this.commonValues.getPageCount() - this.commonValues.getCurrentPage());
            }
            else
            {
              navigatePages(10);
              break;
              if ((paramArrayOfObject == null) && (this.commonValues.getSelectedFile() != null) && (this.commonValues.getPageCount() > 1) && (this.commonValues.getPageCount() - this.commonValues.getCurrentPage() > 0))
              {
                navigatePages(this.commonValues.getPageCount() - this.commonValues.getCurrentPage());
                break;
                Object localObject7;
                if (paramArrayOfObject == null)
                {
                  localObject7 = this.currentGUI.showInputDialog(Messages.getMessage("PdfViewer.EnterPageNumber"), Messages.getMessage("PdfViewer.GotoPage"), 3);
                  if (localObject7 != null)
                    gotoPage((String)localObject7);
                }
                else
                {
                  gotoPage((String)paramArrayOfObject[0]);
                  break;
                  if (!this.decode_pdf.isOpen())
                    return null;
                  if (paramArrayOfObject == null)
                  {
                    this.currentGUI.getCombo(252).setEnabled(true);
                    this.currentGUI.getCombo(251).setEnabled(true);
                    this.currentGUI.mouseMode.setEnabled(true);
                    this.currentGUI.snapshotButton.setEnabled(true);
                    this.currentGUI.alignLayoutMenuOption(1);
                    if (SwingUtilities.isEventDispatchThread())
                    {
                      this.decode_pdf.setDisplayView(1, 2);
                    }
                    else
                    {
                      localObject7 = new Runnable()
                      {
                        public void run()
                        {
                          Commands.this.decode_pdf.setDisplayView(1, 2);
                        }
                      };
                      SwingUtilities.invokeLater((Runnable)localObject7);
                    }
                    this.currentGUI.hideRedundentNavButtons();
                    this.currentGUI.resetRotationBox();
                    this.currentGUI.getFrame().setMinimumSize(new Dimension(0, 0));
                    this.currentGUI.zoom();
                    this.currentGUI.setPageNumber();
                    break;
                    if (!this.decode_pdf.isOpen())
                      return null;
                    if (paramArrayOfObject == null)
                    {
                      this.currentGUI.getCombo(252).setEnabled(true);
                      this.currentGUI.getCombo(251).setEnabled(true);
                      this.currentGUI.mouseMode.setEnabled(true);
                      this.currentGUI.snapshotButton.setEnabled(true);
                      this.currentGUI.alignLayoutMenuOption(2);
                      if (SwingUtilities.isEventDispatchThread())
                      {
                        this.decode_pdf.setDisplayView(2, 2);
                        this.currentGUI.hideRedundentNavButtons();
                        this.currentGUI.setSelectedComboIndex(251, 0);
                        this.currentGUI.getFrame().setMinimumSize(new Dimension(0, 0));
                      }
                      else
                      {
                        i = 1;
                        localObject7 = new Runnable()
                        {
                          public void run()
                          {
                            Commands.this.decode_pdf.setDisplayView(2, 2);
                            Commands.this.currentGUI.hideRedundentNavButtons();
                            Commands.this.currentGUI.setSelectedComboIndex(251, 0);
                            Commands.this.currentGUI.getFrame().setMinimumSize(new Dimension(0, 0));
                            Commands.this.executingCommand = false;
                          }
                        };
                        SwingUtilities.invokeLater((Runnable)localObject7);
                        break;
                        if (!this.decode_pdf.isOpen())
                          return null;
                        if (paramArrayOfObject == null)
                        {
                          this.currentGUI.getCombo(252).setEnabled(true);
                          this.currentGUI.getCombo(251).setEnabled(true);
                          this.currentGUI.mouseMode.setEnabled(true);
                          this.currentGUI.snapshotButton.setEnabled(true);
                          this.currentGUI.getCombo(252).setID(-1);
                          this.currentGUI.setSelectedComboIndex(252, 0);
                          this.currentGUI.getCombo(252).setID(252);
                          this.currentGUI.zoom();
                          this.currentGUI.alignLayoutMenuOption(4);
                          if (SwingUtilities.isEventDispatchThread())
                          {
                            this.decode_pdf.setDisplayView(4, 2);
                            int i2 = this.commonValues.getCurrentPage();
                            if (((i2 & 0x1) == 1) && (i2 != 1))
                              i2--;
                            this.commonValues.setCurrentPage(i2);
                            this.currentGUI.setPage(i2);
                            this.currentGUI.hideRedundentNavButtons();
                            this.currentGUI.setSelectedComboIndex(251, 0);
                            this.currentGUI.getFrame().setMinimumSize(new Dimension(0, 0));
                          }
                          else
                          {
                            i = 1;
                            Runnable local3 = new Runnable()
                            {
                              public void run()
                              {
                                Commands.this.decode_pdf.setDisplayView(4, 2);
                                int i = Commands.this.commonValues.getCurrentPage();
                                if (((i & 0x1) == 1) && (i != 1))
                                  i--;
                                Commands.this.commonValues.setCurrentPage(i);
                                Commands.this.currentGUI.setPage(i);
                                Commands.this.currentGUI.hideRedundentNavButtons();
                                Commands.this.currentGUI.setSelectedComboIndex(251, 0);
                                Commands.this.currentGUI.getFrame().setMinimumSize(new Dimension(0, 0));
                                Commands.this.executingCommand = false;
                              }
                            };
                            SwingUtilities.invokeLater(local3);
                            break;
                            if (!this.decode_pdf.isOpen())
                              return null;
                            if (paramArrayOfObject == null)
                            {
                              if ((this.decode_pdf.getPages().getBoolean(Display.BoolValue.TURNOVER_ON)) && (this.decode_pdf.getPdfPageData().hasMultipleSizes()) && (!this.irregularSizesWarningShown))
                              {
                                this.currentGUI.showMessageDialog(Messages.getMessage("PdfViewer.PageDragIrregularSizes"));
                                this.irregularSizesWarningShown = true;
                              }
                              this.currentGUI.getCombo(252).setEnabled(true);
                              this.currentGUI.getCombo(251).setEnabled(false);
                              this.currentGUI.mouseMode.setEnabled(true);
                              this.currentGUI.snapshotButton.setEnabled(true);
                              this.currentGUI.setSelectedComboIndex(252, 0);
                              this.currentGUI.zoom();
                              this.currentGUI.alignLayoutMenuOption(3);
                              if (SwingUtilities.isEventDispatchThread())
                              {
                                this.decode_pdf.setDisplayView(3, 2);
                                int i3 = this.commonValues.getCurrentPage();
                                if ((this.decode_pdf.getPages().getBoolean(Display.BoolValue.SEPARATE_COVER)) && ((i3 & 0x1) == 1) && (i3 != 1))
                                  i3--;
                                else if ((!this.decode_pdf.getPages().getBoolean(Display.BoolValue.SEPARATE_COVER)) && ((i3 & 0x1) == 0))
                                  i3--;
                                this.commonValues.setCurrentPage(i3);
                                this.currentGUI.setPage(i3);
                                this.currentGUI.hideRedundentNavButtons();
                                this.currentGUI.decodePage();
                                this.currentGUI.setSelectedComboIndex(251, 0);
                                this.currentGUI.getFrame().setMinimumSize(new Dimension(0, 0));
                              }
                              else
                              {
                                i = 1;
                                Object localObject8 = new Runnable()
                                {
                                  public void run()
                                  {
                                    Commands.this.decode_pdf.setDisplayView(3, 2);
                                    int i = Commands.this.commonValues.getCurrentPage();
                                    if ((Commands.this.decode_pdf.getPages().getBoolean(Display.BoolValue.SEPARATE_COVER)) && ((i & 0x1) == 1) && (i != 1))
                                      i--;
                                    else if ((!Commands.this.decode_pdf.getPages().getBoolean(Display.BoolValue.SEPARATE_COVER)) && ((i & 0x1) == 0))
                                      i--;
                                    Commands.this.commonValues.setCurrentPage(i);
                                    Commands.this.currentGUI.setPage(i);
                                    Commands.this.currentGUI.hideRedundentNavButtons();
                                    Commands.this.currentGUI.decodePage();
                                    Commands.this.currentGUI.setSelectedComboIndex(251, 0);
                                    Commands.this.currentGUI.getFrame().setMinimumSize(new Dimension(0, 0));
                                    Commands.this.executingCommand = false;
                                  }
                                };
                                SwingUtilities.invokeLater((Runnable)localObject8);
                                break;
                                if (!this.decode_pdf.isOpen())
                                  return null;
                                this.searchFrame.removeSearchWindow(false);
                                if (paramArrayOfObject == null)
                                {
                                  Object localObject13;
                                  Object localObject9;
                                  if (this.commonValues.getModeOfOperation() != 1)
                                  {
                                    int i7;
                                    if (!JavaFXHelper.isJavaFXAvailable())
                                    {
                                      localObject8 = System.getProperty("org.jpedal.suppressViewerPopups");
                                      i7 = 0;
                                      if ((localObject8 != null) && (((String)localObject8).toLowerCase().equals("true")))
                                        i7 = 1;
                                      localObject13 = this.properties.getValue("showpageflowmessage");
                                      if ((i7 == 0) && (this.properties != null) && (!((String)localObject13).isEmpty()) && (((String)localObject13).equals("true")))
                                      {
                                        final JPanel localJPanel = new JPanel();
                                        localJPanel.setLayout(new BoxLayout(localJPanel, 1));
                                        localObject14 = new JLabel(Messages.getMessage("PdfViewer.PageFlowJarsNeeded.Message"));
                                        ((JLabel)localObject14).setHorizontalTextPosition(0);
                                        localJPanel.add((Component)localObject14);
                                        localObject15 = new MouseAdapter()
                                        {
                                          public void mouseEntered(MouseEvent paramAnonymousMouseEvent)
                                          {
                                            if (SingleDisplay.allowChangeCursor)
                                              localJPanel.setCursor(new Cursor(12));
                                          }

                                          public void mouseExited(MouseEvent paramAnonymousMouseEvent)
                                          {
                                            if (SingleDisplay.allowChangeCursor)
                                              localJPanel.setCursor(new Cursor(0));
                                          }

                                          public void mouseClicked(MouseEvent paramAnonymousMouseEvent)
                                          {
                                            try
                                            {
                                              BrowserLauncher.openURL(Messages.getMessage("PdfViewer.PageFlowJarsNeeded.Link"));
                                            }
                                            catch (IOException localIOException)
                                            {
                                              Commands.this.currentGUI.showMessageDialog(Messages.getMessage("PdfViewer.ErrorWebsite"));
                                            }
                                          }
                                        };
                                        JLabel localJLabel = new JLabel(new StringBuilder().append("<html><u>").append(Messages.getMessage("PdfViewer.PageFlowJarsNeeded.Download")).append("</u></html>").toString());
                                        localJLabel.setForeground(Color.BLUE);
                                        localJLabel.addMouseListener((MouseListener)localObject15);
                                        localJLabel.setHorizontalAlignment(0);
                                        localJPanel.add(localJLabel);
                                        localObject16 = new Object[] { Messages.getMessage("PdfViewer.PageFlowJarsNeeded.Continue") };
                                        JOptionPane.showOptionDialog(this.currentGUI.getFrame(), localJPanel, Messages.getMessage("PdfViewer.PageFlowJarsNeeded.Title"), -1, -1, null, (Object[])localObject16, localObject16[0]);
                                        break;
                                      }
                                    }
                                    this.currentGUI.getCombo(252).setEnabled(false);
                                    this.currentGUI.mouseMode.setEnabled(false);
                                    this.currentGUI.snapshotButton.setEnabled(false);
                                    if (SwingUtilities.isEventDispatchThread())
                                    {
                                      this.decode_pdf.setDisplayView(5, 2);
                                      this.currentGUI.hideRedundentNavButtons();
                                      this.currentGUI.decodePage();
                                      if (this.currentGUI.getSelectedComboIndex(252) != 0)
                                      {
                                        this.currentGUI.setSelectedComboIndex(252, 0);
                                        this.currentGUI.getSelectedComboItem(252);
                                      }
                                      if (this.currentGUI.getSelectedComboIndex(252) == 0)
                                      {
                                        int i4 = this.currentGUI.getPdfDecoder().getPdfPageData().getCropBoxWidth(this.currentGUI.getPageNumber()) / 2;
                                        i7 = this.currentGUI.getPdfDecoder().getPdfPageData().getCropBoxHeight(this.currentGUI.getPageNumber()) / 2;
                                        if (i4 > 800)
                                          i4 = 800;
                                        if (i7 > 600)
                                          i7 = 600;
                                        this.currentGUI.getFrame().setMinimumSize(new Dimension(i4, i7));
                                      }
                                    }
                                    else
                                    {
                                      i = 1;
                                      localObject9 = new Runnable()
                                      {
                                        public void run()
                                        {
                                          Commands.this.decode_pdf.setDisplayView(5, 2);
                                          Commands.this.currentGUI.hideRedundentNavButtons();
                                          Commands.this.currentGUI.decodePage();
                                          if (Commands.this.currentGUI.getSelectedComboIndex(252) != 0)
                                          {
                                            Commands.this.currentGUI.setSelectedComboIndex(252, 0);
                                            Commands.this.currentGUI.getSelectedComboItem(252);
                                          }
                                          if (Commands.this.currentGUI.getSelectedComboIndex(252) == 0)
                                          {
                                            int i = Commands.this.currentGUI.getPdfDecoder().getPdfPageData().getCropBoxWidth(Commands.this.currentGUI.getPageNumber()) / 2;
                                            int j = Commands.this.currentGUI.getPdfDecoder().getPdfPageData().getCropBoxHeight(Commands.this.currentGUI.getPageNumber()) / 2;
                                            if (i > 800)
                                              i = 800;
                                            if (j > 600)
                                              j = 600;
                                            Commands.this.currentGUI.getFrame().setMinimumSize(new Dimension(i, j));
                                          }
                                          Commands.this.executingCommand = false;
                                        }
                                      };
                                      SwingUtilities.invokeLater((Runnable)localObject9);
                                    }
                                  }
                                  else
                                  {
                                    if (this.commonValues.getModeOfOperation() == 1)
                                      this.currentGUI.showMessageDialog("PageFlow temporarily disabled for Applet");
                                    this.currentGUI.alignLayoutMenuOption(1);
                                    if (SwingUtilities.isEventDispatchThread())
                                    {
                                      this.decode_pdf.setDisplayView(1, 2);
                                      this.currentGUI.hideRedundentNavButtons();
                                      this.currentGUI.setSelectedComboIndex(251, 0);
                                    }
                                    else
                                    {
                                      i = 1;
                                      localObject9 = new Runnable()
                                      {
                                        public void run()
                                        {
                                          Commands.this.decode_pdf.setDisplayView(1, 2);
                                          Commands.this.currentGUI.hideRedundentNavButtons();
                                          Commands.this.currentGUI.setSelectedComboIndex(251, 0);
                                          Commands.this.executingCommand = false;
                                        }
                                      };
                                      SwingUtilities.invokeLater((Runnable)localObject9);
                                    }
                                  }
                                  this.currentGUI.getCombo(251).setEnabled(false);
                                  break;
                                  if (paramArrayOfObject == null)
                                  {
                                    localObject9 = GraphicsEnvironment.getLocalGraphicsEnvironment();
                                    GraphicsDevice localGraphicsDevice = ((GraphicsEnvironment)localObject9).getDefaultScreenDevice();
                                    if ((!localGraphicsDevice.isFullScreenSupported()) || (this.win == null))
                                    {
                                      localObject13 = new Frame(localGraphicsDevice.getDefaultConfiguration());
                                      this.win = new Window((Frame)localObject13);
                                    }
                                    else if ((localGraphicsDevice.getFullScreenWindow() != null) && (localGraphicsDevice.getFullScreenWindow().equals(this.win)))
                                    {
                                      exitFullScreen();
                                      break;
                                    }
                                    if ((this.currentGUI.getFrame() instanceof JFrame))
                                    {
                                      ((JFrame)this.currentGUI.getFrame()).getContentPane().remove(this.currentGUI.getDisplayPane());
                                      this.currentGUI.getFrame().setVisible(false);
                                    }
                                    else
                                    {
                                      this.currentGUI.getFrame().remove(this.currentGUI.getDisplayPane());
                                    }
                                    this.win.add(this.currentGUI.getDisplayPane(), "Center");
                                    localObject13 = new Button("Return");
                                    this.win.add((Component)localObject13, "North");
                                    ((Button)localObject13).addActionListener(new ActionListener()
                                    {
                                      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
                                      {
                                        Commands.this.exitFullScreen();
                                      }
                                    });
                                    try
                                    {
                                      this.screenPosition = this.currentGUI.getFrame().getLocation();
                                      localGraphicsDevice.setFullScreenWindow(this.win);
                                      this.win.validate();
                                      this.currentGUI.zoom();
                                    }
                                    catch (Error localError)
                                    {
                                      this.currentGUI.showMessageDialog("Full screen mode not supported on this machine.\nJPedal will now exit");
                                      exit();
                                    }
                                    break;
                                    if ((paramArrayOfObject == null) && (!Values.isProcessing()))
                                    {
                                      boolean bool4 = true;
                                      if (this.currentGUI.getSelectedComboIndex(250) == 0)
                                        bool4 = false;
                                      if (this.commonValues.getSelectedFile() != null)
                                      {
                                        this.currentGUI.showMessageDialog(Messages.getMessage("PdfViewerReparseWait.message"));
                                        this.decode_pdf.useHiResScreenDisplay(bool4);
                                        this.commonValues.setUseHiresImage(bool4);
                                        try
                                        {
                                          this.currentGUI.decodePage();
                                        }
                                        catch (Exception localException7)
                                        {
                                          System.err.println(new StringBuilder().append("Exception ").append(localException7).append("decoding page after image quality changes").toString());
                                          localException7.printStackTrace();
                                        }
                                      }
                                      break;
                                      if (paramArrayOfObject == null)
                                      {
                                        if ((!Values.isProcessing()) && (this.commonValues.getSelectedFile() != null))
                                        {
                                          Rectangle localRectangle = this.decode_pdf.getVisibleRect();
                                          final double d1 = (localRectangle.getX() + localRectangle.getWidth() / 2.0D) / this.decode_pdf.getBounds().getWidth();
                                          final double d2 = (localRectangle.getY() + localRectangle.getHeight() / 2.0D) / this.decode_pdf.getBounds().getHeight();
                                          this.currentGUI.zoom();
                                          localObject15 = new Thread()
                                          {
                                            public void run()
                                            {
                                              try
                                              {
                                                Rectangle localRectangle = new Rectangle((int)(d1 * Commands.this.decode_pdf.getWidth() - d2.getWidth() / 2.0D), (int)(this.val$y * Commands.this.decode_pdf.getHeight() - d2.getHeight() / 2.0D), (int)Commands.this.decode_pdf.getVisibleRect().getWidth(), (int)Commands.this.decode_pdf.getVisibleRect().getHeight());
                                                Commands.this.addAView(-1, localRectangle, null);
                                                Commands.this.decode_pdf.scrollRectToVisible(localRectangle);
                                                Commands.this.decode_pdf.repaint();
                                              }
                                              catch (Exception localException)
                                              {
                                                localException.printStackTrace();
                                              }
                                            }
                                          };
                                          ((Thread)localObject15).setDaemon(true);
                                          SwingUtilities.invokeLater((Runnable)localObject15);
                                        }
                                      }
                                      else
                                      {
                                        this.currentGUI.setScalingFromExternal((String)paramArrayOfObject[0]);
                                        this.currentGUI.zoom();
                                        while (Values.isProcessing())
                                        {
                                          try
                                          {
                                            Thread.sleep(100L);
                                          }
                                          catch (InterruptedException localInterruptedException2)
                                          {
                                            localInterruptedException2.printStackTrace();
                                          }
                                          continue;
                                          if (paramArrayOfObject == null)
                                          {
                                            if (this.commonValues.getSelectedFile() != null)
                                              this.currentGUI.rotate();
                                          }
                                          else
                                          {
                                            int i5 = Integer.parseInt((String)paramArrayOfObject[0]);
                                            while (Values.isProcessing())
                                              try
                                              {
                                                Thread.sleep(100L);
                                              }
                                              catch (InterruptedException localInterruptedException3)
                                              {
                                                localInterruptedException3.printStackTrace();
                                              }
                                            this.currentGUI.setRotationFromExternal(i5);
                                            this.currentGUI.zoom();
                                            break;
                                            if (paramArrayOfObject == null)
                                            {
                                              saveChangedForm();
                                              break;
                                              if (paramArrayOfObject == null)
                                                if (this.commonValues.getSelectedFile() == null)
                                                {
                                                  this.currentGUI.showMessageDialog(Messages.getMessage("PdfViewer.NoFile"));
                                                }
                                                else
                                                {
                                                  Object localObject10 = new SavePDF(this.commonValues.getInputDir(), this.commonValues.getPageCount(), this.commonValues.getCurrentPage());
                                                  int i8 = ((SavePDF)localObject10).display(this.currentGUI.getFrame(), Messages.getMessage("PdfViewerTitle.SavePagesAsPdf"));
                                                  if (i8 == 0)
                                                  {
                                                    localObject13 = new ItextFunctions(this.currentGUI, this.commonValues.getSelectedFile(), this.decode_pdf);
                                                    ItextFunctions.extractPagesToNewPDF((SavePDF)localObject10);
                                                  }
                                                  break;
                                                  if (paramArrayOfObject == null)
                                                    if (this.commonValues.getSelectedFile() == null)
                                                    {
                                                      this.currentGUI.showMessageDialog(Messages.getMessage("PdfViewer.NoFile"));
                                                    }
                                                    else
                                                    {
                                                      localObject10 = new RotatePDFPages(this.commonValues.getInputDir(), this.commonValues.getPageCount(), this.commonValues.getCurrentPage());
                                                      i8 = ((RotatePDFPages)localObject10).display(this.currentGUI.getFrame(), Messages.getMessage("PdfViewerRotation.text"));
                                                      ItextFunctions localItextFunctions1;
                                                      if (i8 == 0)
                                                      {
                                                        localObject13 = this.decode_pdf.getPdfPageData();
                                                        this.decode_pdf.closePdfFile();
                                                        localItextFunctions1 = new ItextFunctions(this.currentGUI, this.commonValues.getSelectedFile(), this.decode_pdf);
                                                        ItextFunctions.rotate(this.commonValues.getPageCount(), (PdfPageData)localObject13, (RotatePDFPages)localObject10);
                                                        open(this.commonValues.getSelectedFile());
                                                      }
                                                      break;
                                                      if (paramArrayOfObject == null)
                                                        if (this.commonValues.getSelectedFile() == null)
                                                        {
                                                          this.currentGUI.showMessageDialog(Messages.getMessage("PdfViewer.NoFile"));
                                                        }
                                                        else
                                                        {
                                                          localObject10 = new CropPDFPages(this.commonValues.getInputDir(), this.commonValues.getPageCount(), this.commonValues.getCurrentPage());
                                                          i8 = ((CropPDFPages)localObject10).display(this.currentGUI.getFrame(), Messages.getMessage("PdfViewerTooltip.PDFCropPages"));
                                                          if (i8 == 0)
                                                          {
                                                            localObject13 = this.decode_pdf.getPdfPageData();
                                                            this.decode_pdf.closePdfFile();
                                                            localItextFunctions1 = new ItextFunctions(this.currentGUI, this.commonValues.getSelectedFile(), this.decode_pdf);
                                                            ItextFunctions.setCrop(this.commonValues.getPageCount(), (PdfPageData)localObject13, (CropPDFPages)localObject10);
                                                            open(this.commonValues.getSelectedFile());
                                                          }
                                                          break;
                                                          if (paramArrayOfObject == null)
                                                            if (this.commonValues.getSelectedFile() == null)
                                                            {
                                                              this.currentGUI.showMessageDialog(Messages.getMessage("PdfViewer.NoFile"));
                                                            }
                                                            else
                                                            {
                                                              localObject10 = new ExtractPDFPagesNup(this.commonValues.getInputDir(), this.commonValues.getPageCount(), this.commonValues.getCurrentPage());
                                                              i8 = ((ExtractPDFPagesNup)localObject10).display(this.currentGUI.getFrame(), Messages.getMessage("PdfViewerNUP.titlebar"));
                                                              if (i8 == 0)
                                                              {
                                                                localObject13 = this.decode_pdf.getPdfPageData();
                                                                localItextFunctions1 = new ItextFunctions(this.currentGUI, this.commonValues.getSelectedFile(), this.decode_pdf);
                                                                ItextFunctions.nup(this.commonValues.getPageCount(), (PdfPageData)localObject13, (ExtractPDFPagesNup)localObject10);
                                                              }
                                                              break;
                                                              if (paramArrayOfObject == null)
                                                              {
                                                                if (this.fileIsURL)
                                                                  this.currentGUI.showMessageDialog(Messages.getMessage("PdfViewerMessage.CannotExportFromURL"));
                                                                if (this.commonValues.getSelectedFile() == null)
                                                                {
                                                                  this.currentGUI.showMessageDialog(Messages.getMessage("PdfViewer.NoFile"));
                                                                }
                                                                else if (!this.fileIsURL)
                                                                {
                                                                  localObject10 = new JFileChooser();
                                                                  ((JFileChooser)localObject10).setFileSelectionMode(0);
                                                                  i8 = ((JFileChooser)localObject10).showSaveDialog(null);
                                                                  if (i8 == 0)
                                                                  {
                                                                    localObject13 = ((JFileChooser)localObject10).getSelectedFile();
                                                                    localItextFunctions1 = new ItextFunctions(this.currentGUI, this.commonValues.getSelectedFile(), this.decode_pdf);
                                                                    ItextFunctions.handouts(((File)localObject13).getAbsolutePath());
                                                                  }
                                                                  break;
                                                                  if (paramArrayOfObject == null)
                                                                    if (this.fileIsURL)
                                                                    {
                                                                      this.currentGUI.showMessageDialog(Messages.getMessage("PdfViewerMessage.CannotExportFromURL"));
                                                                    }
                                                                    else if (this.commonValues.getSelectedFile() == null)
                                                                    {
                                                                      this.currentGUI.showMessageDialog(Messages.getMessage("PdfViewer.NoFile"));
                                                                    }
                                                                    else if (!this.decode_pdf.isExtractionAllowed())
                                                                    {
                                                                      this.currentGUI.showMessageDialog(Messages.getMessage("PdfViewerMessage.ExtractionNotAllowed"));
                                                                    }
                                                                    else
                                                                    {
                                                                      localObject10 = new SignData();
                                                                      SignWizardModel localSignWizardModel = new SignWizardModel((SignData)localObject10, this.commonValues.getSelectedFile(), this.commonValues.getInputDir());
                                                                      localObject13 = new Wizard((Frame)this.currentGUI.getFrame(), localSignWizardModel);
                                                                      if (((Wizard)localObject13).showModalDialog() == 0)
                                                                        if (!((SignData)localObject10).validate())
                                                                        {
                                                                          this.currentGUI.showMessageDialog(((SignData)localObject10).toString());
                                                                        }
                                                                        else
                                                                        {
                                                                          int i14 = JOptionPane.showConfirmDialog(this.currentGUI.getFrame(), ((SignData)localObject10).toString(), Messages.getMessage("PdfViewerGeneral.IsThisCorrect"), 2, 3);
                                                                          if (i14 == 0)
                                                                          {
                                                                            localObject14 = new ItextFunctions(this.currentGUI, this.commonValues.getSelectedFile(), this.decode_pdf);
                                                                            ItextFunctions.Sign((SignData)localObject10);
                                                                          }
                                                                          else
                                                                          {
                                                                            JOptionPane.showMessageDialog(this.currentGUI.getFrame(), Messages.getMessage("PdfViewerMessage.SigningOperationCancelled"), Messages.getMessage("PdfViewerGeneral.Warning"), 2);
                                                                          }
                                                                          break;
                                                                          if (paramArrayOfObject == null)
                                                                            if (this.commonValues.getSelectedFile() == null)
                                                                            {
                                                                              this.currentGUI.showMessageDialog(Messages.getMessage("PdfViewer.NoFile"));
                                                                            }
                                                                            else
                                                                            {
                                                                              localObject10 = new DeletePDFPages(this.commonValues.getInputDir(), this.commonValues.getPageCount(), this.commonValues.getCurrentPage());
                                                                              int i9 = ((DeletePDFPages)localObject10).display(this.currentGUI.getFrame(), Messages.getMessage("PdfViewerDelete.text"));
                                                                              ItextFunctions localItextFunctions2;
                                                                              if (i9 == 0)
                                                                              {
                                                                                localObject13 = this.decode_pdf.getPdfPageData();
                                                                                this.decode_pdf.closePdfFile();
                                                                                localItextFunctions2 = new ItextFunctions(this.currentGUI, this.commonValues.getSelectedFile(), this.decode_pdf);
                                                                                ItextFunctions.delete(this.commonValues.getPageCount(), (PdfPageData)localObject13, (DeletePDFPages)localObject10);
                                                                                open(this.commonValues.getSelectedFile());
                                                                              }
                                                                              break;
                                                                              if (paramArrayOfObject == null)
                                                                                if (this.commonValues.getSelectedFile() == null)
                                                                                {
                                                                                  this.currentGUI.showMessageDialog(Messages.getMessage("PdfViewer.NoFile"));
                                                                                }
                                                                                else
                                                                                {
                                                                                  localObject10 = new AddHeaderFooterToPDFPages(this.commonValues.getInputDir(), this.commonValues.getPageCount(), this.commonValues.getCurrentPage());
                                                                                  i9 = ((AddHeaderFooterToPDFPages)localObject10).display(this.currentGUI.getFrame(), Messages.getMessage("PdfViewerTitle.AddHeaderAndFooters"));
                                                                                  if (i9 == 0)
                                                                                  {
                                                                                    localObject13 = this.decode_pdf.getPdfPageData();
                                                                                    this.decode_pdf.closePdfFile();
                                                                                    localItextFunctions2 = new ItextFunctions(this.currentGUI, this.commonValues.getSelectedFile(), this.decode_pdf);
                                                                                    ItextFunctions.addHeaderFooter(this.commonValues.getPageCount(), (PdfPageData)localObject13, (AddHeaderFooterToPDFPages)localObject10);
                                                                                    open(this.commonValues.getSelectedFile());
                                                                                  }
                                                                                  break;
                                                                                  if (paramArrayOfObject == null)
                                                                                    if (this.commonValues.getSelectedFile() == null)
                                                                                    {
                                                                                      this.currentGUI.showMessageDialog(Messages.getMessage("PdfViewer.NoFile"));
                                                                                    }
                                                                                    else
                                                                                    {
                                                                                      localObject10 = new StampTextToPDFPages(this.commonValues.getInputDir(), this.commonValues.getPageCount(), this.commonValues.getCurrentPage());
                                                                                      i9 = ((StampTextToPDFPages)localObject10).display(this.currentGUI.getFrame(), Messages.getMessage("PdfViewerStampText.text"));
                                                                                      if (i9 == 0)
                                                                                      {
                                                                                        localObject13 = this.decode_pdf.getPdfPageData();
                                                                                        this.decode_pdf.closePdfFile();
                                                                                        localItextFunctions2 = new ItextFunctions(this.currentGUI, this.commonValues.getSelectedFile(), this.decode_pdf);
                                                                                        ItextFunctions.stampText(this.commonValues.getPageCount(), (PdfPageData)localObject13, (StampTextToPDFPages)localObject10);
                                                                                        open(this.commonValues.getSelectedFile());
                                                                                      }
                                                                                      break;
                                                                                      if (paramArrayOfObject == null)
                                                                                        if (this.commonValues.getSelectedFile() == null)
                                                                                        {
                                                                                          this.currentGUI.showMessageDialog(Messages.getMessage("PdfViewer.NoFile"));
                                                                                        }
                                                                                        else
                                                                                        {
                                                                                          localObject10 = new StampImageToPDFPages(this.commonValues.getInputDir(), this.commonValues.getPageCount(), this.commonValues.getCurrentPage());
                                                                                          i9 = ((StampImageToPDFPages)localObject10).display(this.currentGUI.getFrame(), Messages.getMessage("PdfViewerStampImage.text"));
                                                                                          if (i9 == 0)
                                                                                          {
                                                                                            localObject13 = this.decode_pdf.getPdfPageData();
                                                                                            this.decode_pdf.closePdfFile();
                                                                                            localItextFunctions2 = new ItextFunctions(this.currentGUI, this.commonValues.getSelectedFile(), this.decode_pdf);
                                                                                            ItextFunctions.stampImage(this.commonValues.getPageCount(), (PdfPageData)localObject13, (StampImageToPDFPages)localObject10);
                                                                                            open(this.commonValues.getSelectedFile());
                                                                                          }
                                                                                          break;
                                                                                          if (paramArrayOfObject == null)
                                                                                            if (this.commonValues.getSelectedFile() == null)
                                                                                            {
                                                                                              this.currentGUI.showMessageDialog(Messages.getMessage("PdfViewer.NoFile"));
                                                                                            }
                                                                                            else
                                                                                            {
                                                                                              localObject10 = new InsertBlankPDFPage(this.commonValues.getInputDir(), this.commonValues.getPageCount(), this.commonValues.getCurrentPage());
                                                                                              i9 = ((InsertBlankPDFPage)localObject10).display(this.currentGUI.getFrame(), Messages.getMessage("PdfViewer.BlankPage"));
                                                                                              if (i9 == 0)
                                                                                              {
                                                                                                localObject13 = this.decode_pdf.getPdfPageData();
                                                                                                this.decode_pdf.closePdfFile();
                                                                                                localItextFunctions2 = new ItextFunctions(this.currentGUI, this.commonValues.getSelectedFile(), this.decode_pdf);
                                                                                                ItextFunctions.add(this.commonValues.getPageCount(), (PdfPageData)localObject13, (InsertBlankPDFPage)localObject10);
                                                                                                open(this.commonValues.getSelectedFile());
                                                                                              }
                                                                                              break;
                                                                                              if (paramArrayOfObject == null)
                                                                                                if (this.commonValues.getSelectedFile() == null)
                                                                                                {
                                                                                                  this.currentGUI.showMessageDialog(Messages.getMessage("PdfViewer.NoFile"));
                                                                                                }
                                                                                                else
                                                                                                {
                                                                                                  localObject10 = new EncryptPDFDocument(this.commonValues.getInputDir(), this.commonValues.getPageCount(), this.commonValues.getCurrentPage());
                                                                                                  i9 = ((EncryptPDFDocument)localObject10).display(this.currentGUI.getFrame(), "Standard Security");
                                                                                                  if (i9 == 0)
                                                                                                  {
                                                                                                    localObject13 = this.decode_pdf.getPdfPageData();
                                                                                                    this.decode_pdf.closePdfFile();
                                                                                                    localItextFunctions2 = new ItextFunctions(this.currentGUI, this.commonValues.getSelectedFile(), this.decode_pdf);
                                                                                                    ItextFunctions.encrypt(this.commonValues.getPageCount(), (PdfPageData)localObject13, (EncryptPDFDocument)localObject10);
                                                                                                    open(this.commonValues.getSelectedFile());
                                                                                                  }
                                                                                                  break;
                                                                                                  goBackAView();
                                                                                                  break;
                                                                                                  goForwardAView();
                                                                                                  break;
                                                                                                  addAView(((Integer)paramArrayOfObject[0]).intValue(), (Rectangle)paramArrayOfObject[1], (Integer)paramArrayOfObject[2]);
                                                                                                  break;
                                                                                                  if (this.decode_pdf == null)
                                                                                                  {
                                                                                                    localObject1 = Integer.valueOf(-1);
                                                                                                  }
                                                                                                  else
                                                                                                  {
                                                                                                    localObject1 = Integer.valueOf(this.currentGUI.getPageNumber());
                                                                                                    break;
                                                                                                    if (this.decode_pdf == null)
                                                                                                    {
                                                                                                      localObject1 = Integer.valueOf(-1);
                                                                                                    }
                                                                                                    else
                                                                                                    {
                                                                                                      localObject1 = Integer.valueOf(this.decode_pdf.getPageCount());
                                                                                                      break;
                                                                                                      localObject1 = this.currentGUI.pageCounter2;
                                                                                                      break;
                                                                                                      this.currentGUI.setBookmarks(true);
                                                                                                      localObject1 = this.currentGUI.getThumbnailPanel();
                                                                                                      break;
                                                                                                      this.currentGUI.setBookmarks(true);
                                                                                                      localObject1 = this.currentGUI.getOutlinePanel();
                                                                                                      break;
                                                                                                      try
                                                                                                      {
                                                                                                        localObject10 = new HashMap();
                                                                                                        ((Map)localObject10).put(JPedalSettings.PAGE_COLOR, paramArrayOfObject[0]);
                                                                                                        this.decode_pdf.modifyNonstaticJPedalParameters((Map)localObject10);
                                                                                                      }
                                                                                                      catch (PdfException localPdfException3)
                                                                                                      {
                                                                                                        localPdfException3.printStackTrace();
                                                                                                      }
                                                                                                      try
                                                                                                      {
                                                                                                        HashMap localHashMap1 = new HashMap();
                                                                                                        localHashMap1.put(JPedalSettings.UNDRAWN_PAGE_COLOR, paramArrayOfObject[0]);
                                                                                                        this.decode_pdf.modifyNonstaticJPedalParameters(localHashMap1);
                                                                                                      }
                                                                                                      catch (PdfException localPdfException4)
                                                                                                      {
                                                                                                        localPdfException4.printStackTrace();
                                                                                                      }
                                                                                                      try
                                                                                                      {
                                                                                                        HashMap localHashMap2 = new HashMap();
                                                                                                        localHashMap2.put(JPedalSettings.REPLACE_TEXT_COLOR, paramArrayOfObject[0]);
                                                                                                        this.decode_pdf.modifyNonstaticJPedalParameters(localHashMap2);
                                                                                                      }
                                                                                                      catch (PdfException localPdfException5)
                                                                                                      {
                                                                                                        localPdfException5.printStackTrace();
                                                                                                      }
                                                                                                      try
                                                                                                      {
                                                                                                        HashMap localHashMap3 = new HashMap();
                                                                                                        localHashMap3.put(JPedalSettings.TEXT_COLOR, paramArrayOfObject[0]);
                                                                                                        this.decode_pdf.modifyNonstaticJPedalParameters(localHashMap3);
                                                                                                      }
                                                                                                      catch (PdfException localPdfException6)
                                                                                                      {
                                                                                                        localPdfException6.printStackTrace();
                                                                                                      }
                                                                                                      try
                                                                                                      {
                                                                                                        HashMap localHashMap4 = new HashMap();
                                                                                                        localHashMap4.put(JPedalSettings.DISPLAY_BACKGROUND, paramArrayOfObject[0]);
                                                                                                        this.decode_pdf.modifyNonstaticJPedalParameters(localHashMap4);
                                                                                                      }
                                                                                                      catch (PdfException localPdfException7)
                                                                                                      {
                                                                                                        localPdfException7.printStackTrace();
                                                                                                      }
                                                                                                      try
                                                                                                      {
                                                                                                        HashMap localHashMap5 = new HashMap();
                                                                                                        localHashMap5.put(JPedalSettings.CHANGE_LINEART, paramArrayOfObject[0]);
                                                                                                        this.decode_pdf.modifyNonstaticJPedalParameters(localHashMap5);
                                                                                                      }
                                                                                                      catch (PdfException localPdfException8)
                                                                                                      {
                                                                                                        localPdfException8.printStackTrace();
                                                                                                      }
                                                                                                      try
                                                                                                      {
                                                                                                        HashMap localHashMap6 = new HashMap();
                                                                                                        localHashMap6.put(JPedalSettings.REPLACEMENT_COLOR_THRESHOLD, paramArrayOfObject[0]);
                                                                                                        this.decode_pdf.modifyNonstaticJPedalParameters(localHashMap6);
                                                                                                      }
                                                                                                      catch (PdfException localPdfException9)
                                                                                                      {
                                                                                                        localPdfException9.printStackTrace();
                                                                                                      }
                                                                                                      System.out.println("No menu item set");
                                                                                                    }
                                                                                                  }
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                              }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                          }
                                        }
                                      }
                                    }
                                  }
                                }
                              }
                            }
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
        }
      }
    }
    label11442: if (i == 0)
      this.executingCommand = false;
    return localObject1;
  }

  protected void exitFullScreen()
  {
    Runnable local10 = new Runnable()
    {
      public void run()
      {
        GraphicsEnvironment localGraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice localGraphicsDevice = localGraphicsEnvironment.getDefaultScreenDevice();
        localGraphicsDevice.setFullScreenWindow(null);
        Commands.this.win.remove(Commands.this.currentGUI.getDisplayPane());
        if ((Commands.this.currentGUI.getFrame() instanceof JFrame))
        {
          ((JFrame)Commands.this.currentGUI.getFrame()).getContentPane().add(Commands.this.currentGUI.getDisplayPane(), "Center");
          Commands.this.currentGUI.getFrame().setVisible(true);
          if (Commands.this.screenPosition != null)
            Commands.this.currentGUI.getFrame().setLocation(Commands.this.screenPosition);
          Commands.this.screenPosition = null;
        }
        else
        {
          Commands.this.currentGUI.getFrame().add(Commands.this.currentGUI.getDisplayPane(), "Center");
        }
        Commands.this.currentGUI.getDisplayPane().invalidate();
        Commands.this.currentGUI.getDisplayPane().updateUI();
        if ((Commands.this.currentGUI.getFrame() instanceof JFrame))
          ((JFrame)Commands.this.currentGUI.getFrame()).getContentPane().validate();
        else
          Commands.this.currentGUI.getFrame().validate();
        Commands.this.win.dispose();
        Commands.this.win = null;
        Commands.this.currentGUI.zoom();
      }
    };
    SwingUtilities.invokeLater(local10);
  }

  private void cascade()
  {
    JDesktopPane localJDesktopPane = this.currentGUI.getMultiViewerFrames();
    JInternalFrame[] arrayOfJInternalFrame1 = localJDesktopPane.getAllFrames();
    int i = 0;
    for (int j = arrayOfJInternalFrame1.length - 1; i < j; j--)
    {
      JInternalFrame localJInternalFrame1 = arrayOfJInternalFrame1[i];
      arrayOfJInternalFrame1[i] = arrayOfJInternalFrame1[j];
      arrayOfJInternalFrame1[j] = localJInternalFrame1;
      i++;
    }
    i = 0;
    j = 0;
    int k = localJDesktopPane.getWidth() / 2;
    int m = localJDesktopPane.getHeight() / 2;
    for (JInternalFrame localJInternalFrame2 : arrayOfJInternalFrame1)
      if (!localJInternalFrame2.isIcon())
        try
        {
          localJInternalFrame2.setMaximum(false);
          localJInternalFrame2.reshape(i, j, k, m);
          localJInternalFrame2.setSelected(true);
          i += 25;
          j += 25;
          if (i + k > localJDesktopPane.getWidth())
            i = 0;
          if (j + m > localJDesktopPane.getHeight())
            j = 0;
        }
        catch (PropertyVetoException localPropertyVetoException)
        {
        }
  }

  private void tile()
  {
    JDesktopPane localJDesktopPane = this.currentGUI.getMultiViewerFrames();
    JInternalFrame[] arrayOfJInternalFrame1 = localJDesktopPane.getAllFrames();
    int i = 0;
    for (JInternalFrame localJInternalFrame1 : arrayOfJInternalFrame1)
      if (!localJInternalFrame1.isIcon())
        i++;
    int j = (int)Math.sqrt(i);
    ??? = i / j;
    ??? = i % j;
    int n = localJDesktopPane.getWidth() / ???;
    int i1 = localJDesktopPane.getHeight() / j;
    int i2 = 0;
    int i3 = 0;
    for (JInternalFrame localJInternalFrame2 : arrayOfJInternalFrame1)
      if (!localJInternalFrame2.isIcon())
        try
        {
          localJInternalFrame2.setMaximum(false);
          localJInternalFrame2.reshape(i3 * n, i2 * i1, n, i1);
          i2++;
          if (i2 == j)
          {
            i2 = 0;
            i3++;
            if (i3 == ??? - ???)
            {
              j++;
              i1 = localJDesktopPane.getHeight() / j;
            }
          }
        }
        catch (PropertyVetoException localPropertyVetoException)
        {
        }
  }

  private PdfDecoder openNewMultiplePage(String paramString)
  {
    JDesktopPane localJDesktopPane = this.currentGUI.getMultiViewerFrames();
    PdfDecoder localPdfDecoder = new PdfDecoder(true);
    this.decode_pdf = localPdfDecoder;
    this.currentGUI.setPdfDecoder(this.decode_pdf);
    if (SwingUtilities.isEventDispatchThread())
    {
      this.decode_pdf.setDisplayView(1, 2);
    }
    else
    {
      Runnable local11 = new Runnable()
      {
        public void run()
        {
          Commands.this.decode_pdf.setDisplayView(1, 2);
        }
      };
      SwingUtilities.invokeLater(local11);
    }
    PdfDecoder.init(true);
    this.decode_pdf.setExtractionMode(0, 1.0F);
    int i = GUI.getPDFDisplayInset();
    this.decode_pdf.setInset(i, i);
    if (this.decode_pdf.getDecoderOptions().getDisplayBackgroundColor() != null)
      this.decode_pdf.setBackground(this.decode_pdf.getDecoderOptions().getDisplayBackgroundColor());
    else if (this.decode_pdf.useNewGraphicsMode)
      this.decode_pdf.setBackground(new Color(55, 55, 65));
    else
      this.decode_pdf.setBackground(new Color(190, 190, 190));
    this.decode_pdf.setPageParameters(this.currentGUI.getScaling(), this.commonValues.getCurrentPage(), this.currentGUI.getRotation());
    JInternalFrame localJInternalFrame = new JInternalFrame(paramString, true, true, true, true);
    String str = String.valueOf(this.startX);
    localJInternalFrame.setName(str);
    localJInternalFrame.setSize(250, 250);
    localJInternalFrame.setVisible(true);
    localJInternalFrame.setLocation(this.startX, this.startY);
    this.startX += 25;
    this.startY += 25;
    this.multiViewListener = new MultiViewListener(this.decode_pdf, this.currentGUI, this.commonValues, this);
    localJInternalFrame.addInternalFrameListener(this.multiViewListener);
    localJInternalFrame.addComponentListener(new ComponentListener()
    {
      public void componentHidden(ComponentEvent paramAnonymousComponentEvent)
      {
      }

      public void componentMoved(ComponentEvent paramAnonymousComponentEvent)
      {
        Component localComponent = paramAnonymousComponentEvent.getComponent();
        Container localContainer = localComponent.getParent();
        if (localComponent.getLocation().y < 0)
          localComponent.setLocation(localComponent.getLocation().x, 0);
        else if (localComponent.getLocation().y + localComponent.getSize().height > localContainer.getSize().height + localComponent.getSize().height / 2)
          localComponent.setLocation(localComponent.getLocation().x, localContainer.getSize().height - localComponent.getSize().height / 2);
        if (localComponent.getLocation().x < -(localComponent.getSize().width / 2))
          localComponent.setLocation(-(localComponent.getSize().width / 2), localComponent.getLocation().y);
        else if (localComponent.getLocation().x + localComponent.getSize().width > localContainer.getSize().width + localComponent.getSize().width / 2)
          localComponent.setLocation(localContainer.getSize().width - localComponent.getSize().width / 2, localComponent.getLocation().y);
      }

      public void componentResized(ComponentEvent paramAnonymousComponentEvent)
      {
        if ((Commands.this.decode_pdf.getParent() != null) && (Commands.this.currentGUI.getSelectedComboIndex(252) < 3))
          Commands.this.currentGUI.zoom();
      }

      public void componentShown(ComponentEvent paramAnonymousComponentEvent)
      {
      }
    });
    JScrollPane localJScrollPane = new JScrollPane();
    localJScrollPane.getViewport().add(localPdfDecoder);
    localJScrollPane.setVerticalScrollBarPolicy(20);
    localJScrollPane.setHorizontalScrollBarPolicy(30);
    localJScrollPane.getVerticalScrollBar().setUnitIncrement(80);
    localJScrollPane.getHorizontalScrollBar().setUnitIncrement(80);
    localJInternalFrame.getContentPane().add(localJScrollPane);
    localJDesktopPane.add(paramString, localJInternalFrame);
    try
    {
      localJInternalFrame.setSelected(true);
    }
    catch (PropertyVetoException localPropertyVetoException)
    {
      localPropertyVetoException.printStackTrace();
    }
    localJInternalFrame.toFront();
    localJInternalFrame.requestFocusInWindow();
    return this.decode_pdf;
  }

  public void openTransferedFile(String paramString)
    throws PdfException
  {
    while ((this.openingTransferedFile) || (Values.isProcessing()))
      try
      {
        Thread.sleep(250L);
      }
      catch (InterruptedException localInterruptedException)
      {
        localInterruptedException.printStackTrace();
      }
    this.openingTransferedFile = true;
    this.currentGUI.resetNavBar();
    int i = (paramString.startsWith("http:")) || (paramString.startsWith("file:")) ? 1 : 0;
    try
    {
      if (i == 0)
      {
        this.fileIsURL = false;
        this.commonValues.setFileSize(new File(paramString).length() >> 10);
      }
      else
      {
        this.fileIsURL = true;
      }
      this.commonValues.setSelectedFile(paramString);
      this.currentGUI.setViewerTitle(null);
    }
    catch (Exception localException)
    {
      LogWriter.writeLog(new StringBuilder().append("Exception ").append(localException).append(" getting paths").toString());
    }
    File localFile = new File(this.commonValues.getSelectedFile());
    if ((i == 0) && (!localFile.exists()))
    {
      this.currentGUI.showMessageDialog(new StringBuilder().append(Messages.getMessage("PdfViewerFile.text")).append(this.commonValues.getSelectedFile()).append(Messages.getMessage("PdfViewerNotExist")).toString());
    }
    else if ((this.commonValues.getSelectedFile() != null) && (!Values.isProcessing()))
    {
      if (this.currentGUI.isSingle())
        this.decode_pdf.flushObjectValues(true);
      else
        this.decode_pdf = openNewMultiplePage(this.commonValues.getSelectedFile());
      this.decode_pdf.resetViewableArea();
      try
      {
        openFile(this.commonValues.getSelectedFile());
      }
      catch (PdfException localPdfException)
      {
        this.openingTransferedFile = false;
        throw localPdfException;
      }
      if (this.commonValues.isPDF())
        this.openingTransferedFile = false;
    }
  }

  private void saveChangedForm()
  {
    AcroRenderer localAcroRenderer = this.decode_pdf.getFormRenderer();
    if (localAcroRenderer == null)
      return;
    Object[] arrayOfObject = localAcroRenderer.getFormComponents(null, ReturnValues.FORM_NAMES, -1);
    if (arrayOfObject == null)
    {
      this.currentGUI.showMessageDialog(Messages.getMessage("PdfViewer.NoFields"));
    }
    else
    {
      String str = "";
      int i = 0;
      while (i == 0)
      {
        localObject = new JFileChooser(this.commonValues.getInputDir());
        ((JFileChooser)localObject).setSelectedFile(new File(new StringBuilder().append(this.commonValues.getInputDir()).append('/').append(this.commonValues.getSelectedFile()).toString()));
        ((JFileChooser)localObject).addChoosableFileFilter(new FileFilterer(new String[] { "pdf" }, "Pdf (*.pdf)"));
        ((JFileChooser)localObject).addChoosableFileFilter(new FileFilterer(new String[] { "fdf" }, "fdf (*.fdf)"));
        ((JFileChooser)localObject).setFileSelectionMode(0);
        int j = ((JFileChooser)localObject).showSaveDialog(null);
        if (j == 0)
        {
          File localFile = ((JFileChooser)localObject).getSelectedFile();
          str = localFile.getAbsolutePath();
          if (!str.endsWith(".pdf"))
          {
            str = new StringBuilder().append(str).append(".pdf").toString();
            localFile = new File(str);
          }
          if (str.equals(this.commonValues.getSelectedFile()))
          {
            this.currentGUI.showMessageDialog(Messages.getMessage("PdfViewerMessage.SaveError"));
          }
          else if (localFile.exists())
          {
            int k = this.currentGUI.showConfirmDialog(new StringBuilder().append(str).append('\n').append(Messages.getMessage("PdfViewerMessage.FileAlreadyExists")).append(".\n").append(Messages.getMessage("PdfViewerMessage.ConfirmResave")).toString(), Messages.getMessage("PdfViewerMessage.Resave"), 0);
            if (k == 1);
          }
          else
          {
            i = 1;
          }
        }
        else
        {
          return;
        }
      }
      Object localObject = new ItextFunctions(this.currentGUI, this.commonValues.getSelectedFile(), this.decode_pdf);
      ItextFunctions.saveFormsData(str);
      this.commonValues.setFormsChanged(false);
      this.currentGUI.setViewerTitle(null);
    }
  }

  public void handleUnsaveForms()
  {
    if (this.commonValues.isFormsChanged())
    {
      int i = this.currentGUI.showConfirmDialog(Messages.getMessage("PdfViewerFormsUnsavedOptions.message"), Messages.getMessage("PdfViewerFormsUnsavedWarning.message"), 0);
      if (i == 0)
        saveChangedForm();
    }
    this.commonValues.setFormsChanged(false);
  }

  public void extractSelectedScreenAsImage()
  {
    int i = this.commonValues.m_x1;
    int j = this.commonValues.m_x2;
    int k = this.commonValues.m_y1;
    int m = this.commonValues.m_y2;
    if (this.commonValues.m_y1 < this.commonValues.m_y2)
    {
      m = this.commonValues.m_y1;
      k = this.commonValues.m_y2;
    }
    if (this.commonValues.m_x1 > this.commonValues.m_x2)
    {
      j = this.commonValues.m_x1;
      i = this.commonValues.m_x2;
    }
    float f = 100.0F;
    if (DecoderOptions.isRunningOnWindows)
      f = 100.0F * this.currentGUI.getScaling();
    final BufferedImage localBufferedImage = this.decode_pdf.getSelectedRectangleOnscreen(i, k, j, m, f);
    JPanel localJPanel1 = new JPanel();
    localJPanel1.setLayout(new BorderLayout());
    if (localBufferedImage != null)
    {
      localObject = new IconiseImage(localBufferedImage);
      localJPanel1.add(new JLabel((Icon)localObject), "Center");
    }
    else
    {
      return;
    }
    Object localObject = new JScrollPane();
    ((JScrollPane)localObject).getViewport().add(localJPanel1);
    int n = localBufferedImage.getWidth();
    if (n < localBufferedImage.getHeight())
      n = localBufferedImage.getHeight();
    n += 50;
    if (n > 450)
      n = 450;
    Container localContainer = this.currentGUI.getFrame();
    final JDialog localJDialog = new JDialog((JFrame)null, true);
    localJDialog.setDefaultCloseOperation(2);
    if (this.commonValues.getModeOfOperation() != 1)
    {
      localJDialog.setLocationRelativeTo(null);
      localJDialog.setLocation(localContainer.getLocationOnScreen().x + 10, localContainer.getLocationOnScreen().y + 10);
    }
    localJDialog.setSize(n, n);
    localJDialog.setTitle(Messages.getMessage("PdfViewerMessage.SaveImage"));
    localJDialog.getContentPane().setLayout(new BorderLayout());
    localJDialog.getContentPane().add((Component)localObject, "Center");
    JPanel localJPanel2 = new JPanel();
    localJPanel2.setLayout(new BorderLayout());
    localJDialog.getContentPane().add(localJPanel2, "South");
    JButton localJButton1 = new JButton(Messages.getMessage("PdfMessage.Yes"));
    localJButton1.setFont(new Font("SansSerif", 0, 12));
    localJPanel2.add(localJButton1, "West");
    localJButton1.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        localJDialog.setVisible(false);
        int i = 0;
        while (i == 0)
        {
          JFileChooser localJFileChooser = new JFileChooser(System.getProperty("user.dir"));
          localJFileChooser.addChoosableFileFilter(new FileFilterer(new String[] { "tif", "tiff" }, "TIFF"));
          localJFileChooser.addChoosableFileFilter(new FileFilterer(new String[] { "jpg", "jpeg" }, "JPEG"));
          int j = localJFileChooser.showSaveDialog(this.val$image_scroll);
          if (j == 0)
          {
            File localFile = localJFileChooser.getSelectedFile();
            String str1 = localFile.getAbsolutePath();
            String str2 = localJFileChooser.getFileFilter().getDescription();
            if (str2.equals("All Files"))
              str2 = "TIFF";
            if (!str1.toLowerCase().endsWith(('.' + str2).toLowerCase()))
            {
              str1 = str1 + '.' + str2;
              localFile = new File(str1);
            }
            if (localFile.exists())
            {
              int k = Commands.this.currentGUI.showConfirmDialog(str1 + '\n' + Messages.getMessage("PdfViewerMessage.FileAlreadyExists") + ".\n" + Messages.getMessage("PdfViewerMessage.ConfirmResave"), Messages.getMessage("PdfViewerMessage.Resave"), 0);
              if (k == 1);
            }
            else
            {
              if (JAIHelper.isJAIused())
                JAIHelper.confirmJAIOnClasspath();
              if (localBufferedImage != null)
                if (JAIHelper.isJAIused())
                  JAIHelper.filestore(localBufferedImage, str1, str2);
                else if (str2.toLowerCase().startsWith("tif"))
                  Commands.this.currentGUI.showMessageDialog("Please setup JAI library for Tiffs");
                else
                  DefaultIO.write(localBufferedImage, str2, str1);
              i = 1;
            }
          }
          else
          {
            return;
          }
        }
        localJDialog.dispose();
      }
    });
    JButton localJButton2 = new JButton(Messages.getMessage("PdfMessage.No"));
    localJButton2.setFont(new Font("SansSerif", 0, 12));
    localJPanel2.add(localJButton2, "East");
    localJButton2.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        localJDialog.dispose();
      }
    });
    localJDialog.setVisible(true);
  }

  private static Rectangle adjustHighlightForExtraction(Rectangle paramRectangle)
  {
    int i = paramRectangle.x - 1;
    int j = paramRectangle.y - 3;
    int k = paramRectangle.width + 2;
    int m = paramRectangle.height + 6;
    return new Rectangle(i, j, k, m);
  }

  public String copySelectedText()
  {
    if (!this.decode_pdf.isExtractionAllowed())
    {
      this.currentGUI.showMessageDialog("Not allowed");
      return "";
    }
    String str1 = "";
    Rectangle[] arrayOfRectangle = this.decode_pdf.getTextLines().getHighlightedAreas(this.commonValues.getCurrentPage());
    if (arrayOfRectangle == null)
      return "";
    int i = arrayOfRectangle.length > 1 ? 1 : 0;
    for (int j = 0; j != arrayOfRectangle.length; j++)
    {
      arrayOfRectangle[j] = adjustHighlightForExtraction(arrayOfRectangle[j]);
      int k = arrayOfRectangle[j].x;
      int m = arrayOfRectangle[j].x + arrayOfRectangle[j].width;
      int n = arrayOfRectangle[j].y + arrayOfRectangle[j].height;
      int i1 = arrayOfRectangle[j].y;
      int i2;
      if (n < i1)
      {
        i2 = i1;
        i1 = n;
        n = i2;
      }
      if (k > m)
      {
        i2 = m;
        m = k;
        k = i2;
      }
      if (k < this.currentGUI.cropX)
        k = this.currentGUI.cropX;
      if (k > this.currentGUI.cropW - this.currentGUI.cropX)
        k = this.currentGUI.cropW - this.currentGUI.cropX;
      if (m < this.currentGUI.cropX)
        m = this.currentGUI.cropX;
      if (m > this.currentGUI.cropW - this.currentGUI.cropX)
        m = this.currentGUI.cropW - this.currentGUI.cropX;
      if (n < this.currentGUI.cropY)
        n = this.currentGUI.cropY;
      if (n > this.currentGUI.cropH - this.currentGUI.cropY)
        n = this.currentGUI.cropH - this.currentGUI.cropY;
      if (i1 < this.currentGUI.cropY)
        i1 = this.currentGUI.cropY;
      if (i1 > this.currentGUI.cropH - this.currentGUI.cropY)
        i1 = this.currentGUI.cropH - this.currentGUI.cropY;
      this.display = true;
      while (this.display)
        try
        {
          PdfGroupingAlgorithms localPdfGroupingAlgorithms = this.decode_pdf.getGroupingObject();
          this.display = false;
          String str2 = localPdfGroupingAlgorithms.extractTextInRectangle(k, n, m, i1, this.commonValues.getCurrentPage(), false, true);
          if ((str2 == null) || (str2.isEmpty()))
          {
            if (i == 0)
              this.currentGUI.showMessageDialog(Messages.getMessage("PdfViewerMessage.NoTextFound"));
          }
          else
            str2 = Strip.stripXML(str2, this.decode_pdf.isXMLExtraction()).toString();
          if (str2 != null)
            str1 = new StringBuilder().append(str1).append(str2).append('\r').append('\n').toString();
        }
        catch (PdfException localPdfException)
        {
          System.err.println(new StringBuilder().append("Exception ").append(localPdfException.getMessage()).append(" in file ").append(this.commonValues.getSelectedFile()).toString());
          localPdfException.printStackTrace();
        }
    }
    if (str1.length() > 2)
      return str1.substring(0, str1.length() - 2);
    return "";
  }

  private JScrollPane updateExtractionExample(AbstractButton paramAbstractButton, boolean paramBoolean)
    throws PdfException
  {
    Rectangle[] arrayOfRectangle = this.decode_pdf.getTextLines().getHighlightedAreas(this.commonValues.getCurrentPage());
    JScrollPane localJScrollPane = new JScrollPane();
    String str = "";
    int j;
    int n;
    for (int i = 0; i != arrayOfRectangle.length; i++)
      if (arrayOfRectangle[i] != null)
      {
        arrayOfRectangle[i] = adjustHighlightForExtraction(arrayOfRectangle[i]);
        j = arrayOfRectangle[i].x;
        int k = arrayOfRectangle[i].x + arrayOfRectangle[i].width;
        int m = arrayOfRectangle[i].y + arrayOfRectangle[i].height;
        n = arrayOfRectangle[i].y;
        int i1;
        if (m < n)
        {
          i1 = n;
          n = m;
          m = i1;
        }
        if (j > k)
        {
          i1 = k;
          k = j;
          j = i1;
        }
        if (j < this.currentGUI.cropX)
          j = this.currentGUI.cropX;
        if (j > this.currentGUI.cropX + this.currentGUI.cropW)
          j = this.currentGUI.cropX + this.currentGUI.cropW;
        if (k < this.currentGUI.cropX)
          k = this.currentGUI.cropX;
        if (k > this.currentGUI.cropX + this.currentGUI.cropW)
          k = this.currentGUI.cropX + this.currentGUI.cropW;
        if (m < this.currentGUI.cropY)
          m = this.currentGUI.cropY;
        if (m > this.currentGUI.cropY + this.currentGUI.cropH)
          m = this.currentGUI.cropY + this.currentGUI.cropH;
        if (n < this.currentGUI.cropY)
          n = this.currentGUI.cropY;
        if (n > this.currentGUI.cropY + this.currentGUI.cropH)
          n = this.currentGUI.cropY + this.currentGUI.cropH;
        if (paramAbstractButton.getText().equals("Table"))
          str = new StringBuilder().append(str).append(extractTextTable(this.decode_pdf.getGroupingObject(), paramBoolean, j, k, m, n)).toString();
        if (paramAbstractButton.getText().equals("Rectangle"))
          str = new StringBuilder().append(str).append(extractTextRectangle(this.decode_pdf.getGroupingObject(), paramBoolean, j, k, m, n)).append(' ').toString();
        if (paramAbstractButton.getText().equals("WordList"))
          str = new StringBuilder().append(str).append(extractTextList(this.decode_pdf.getGroupingObject(), paramBoolean, j, k, m, n)).toString();
      }
    if (!str.isEmpty())
    {
      if (paramAbstractButton.getText().equals("Table"))
        try
        {
          localJScrollPane = this.currentGUI.createPane(new JTextPane(), str, paramBoolean);
        }
        catch (BadLocationException localBadLocationException1)
        {
          localBadLocationException1.printStackTrace();
        }
      if (paramAbstractButton.getText().equals("Rectangle"))
        try
        {
          localJScrollPane = this.currentGUI.createPane(new JTextPane(), str, paramBoolean);
        }
        catch (BadLocationException localBadLocationException2)
        {
          localBadLocationException2.printStackTrace();
        }
      if (paramAbstractButton.getText().equals("WordList"))
        try
        {
          localJScrollPane = this.currentGUI.createPane(new JTextPane(), str, paramBoolean);
        }
        catch (BadLocationException localBadLocationException3)
        {
          localBadLocationException3.printStackTrace();
        }
      localJScrollPane.setPreferredSize(new Dimension(315, 150));
      localJScrollPane.setMinimumSize(new Dimension(315, 150));
      Component[] arrayOfComponent1 = localJScrollPane.getComponents();
      for (j = 0; j != arrayOfComponent1.length; j++)
        if ((localJScrollPane.getComponent(j) instanceof JViewport))
        {
          JViewport localJViewport = (JViewport)localJScrollPane.getComponent(j);
          Component[] arrayOfComponent2 = localJViewport.getComponents();
          for (n = 0; n != arrayOfComponent2.length; n++)
            if ((arrayOfComponent2[n] instanceof JTextPane))
              ((JTextPane)arrayOfComponent2[n]).setEditable(false);
        }
      return localJScrollPane;
    }
    return null;
  }

  public void extractSelectedText()
  {
    if (!this.decode_pdf.isExtractionAllowed())
    {
      this.currentGUI.showMessageDialog("Not allowed");
      return;
    }
    final Rectangle[] arrayOfRectangle = this.decode_pdf.getTextLines().getHighlightedAreas(this.commonValues.getCurrentPage());
    if (arrayOfRectangle == null)
    {
      JOptionPane.showMessageDialog(this.decode_pdf, "There is no text selected.\nPlease highlight the text you wish to extract.", "No Text selected", 0);
      return;
    }
    JScrollPane localJScrollPane = new JScrollPane();
    final JPanel localJPanel = new JPanel();
    final ButtonGroup localButtonGroup1 = new ButtonGroup();
    final JRadioButton localJRadioButton1 = new JRadioButton("Extract as Text");
    final JRadioButton localJRadioButton2 = new JRadioButton("Extract  as  XML");
    JRadioButton localJRadioButton3 = new JRadioButton(Messages.getMessage("PdfViewerRect.label"));
    final JRadioButton localJRadioButton4 = new JRadioButton(Messages.getMessage("PdfViewerTable.label"));
    JRadioButton localJRadioButton5 = new JRadioButton(Messages.getMessage("PdfViewerWordList.label"));
    final SpringLayout localSpringLayout = new SpringLayout();
    final JFrame localJFrame = new JFrame(new StringBuilder().append(Messages.getMessage("PdfViewerCoords.message")).append(' ').append(this.commonValues.m_x1).append(" , ").append(this.commonValues.m_y1).append(" , ").append(this.commonValues.m_x2 - this.commonValues.m_x1).append(" , ").append(this.commonValues.m_y2 - this.commonValues.m_y1).toString());
    localJFrame.setDefaultCloseOperation(2);
    JLabel localJLabel = new JLabel("                         ");
    ButtonGroup localButtonGroup2 = new ButtonGroup();
    Object[] arrayOfObject = { Messages.getMessage("PdfViewerHelpMenu.text"), Messages.getMessage("PdfViewerCancel.text"), Messages.getMessage("PdfViewerextract.text") };
    JButton localJButton1 = new JButton((String)arrayOfObject[0]);
    JButton localJButton2 = new JButton((String)arrayOfObject[1]);
    JButton localJButton3 = new JButton((String)arrayOfObject[2]);
    localJPanel.setLayout(localSpringLayout);
    final Runnable local15 = new Runnable()
    {
      public void run()
      {
        Enumeration localEnumeration = localButtonGroup1.getElements();
        while (localEnumeration.hasMoreElements())
        {
          AbstractButton localAbstractButton = (AbstractButton)localEnumeration.nextElement();
          if (localAbstractButton.isSelected())
            try
            {
              Component[] arrayOfComponent = localJPanel.getComponents();
              for (int i = 0; i != arrayOfComponent.length; i++)
                if ((arrayOfComponent[i] instanceof JScrollPane))
                  localJPanel.remove(arrayOfComponent[i]);
              JScrollPane localJScrollPane = Commands.this.updateExtractionExample(localAbstractButton, localJRadioButton2.isSelected());
              if (localJScrollPane != null)
              {
                localSpringLayout.putConstraint("East", localJScrollPane, -5, "East", localJPanel);
                localSpringLayout.putConstraint("North", localJScrollPane, 5, "South", localJRadioButton4);
                localJPanel.add(localJScrollPane);
              }
              else
              {
                JLabel localJLabel = new JLabel("No Example Available");
                Font localFont = localJLabel.getFont();
                localFont = localFont.deriveFont(localFont.getStyle(), 20.0F);
                localJLabel.setFont(localFont);
                localJLabel.setForeground(Color.RED);
                localSpringLayout.putConstraint("East", localJLabel, -75, "East", localJPanel);
                localSpringLayout.putConstraint("North", localJLabel, 50, "South", localJRadioButton4);
                localJPanel.add(localJLabel);
              }
            }
            catch (PdfException localPdfException)
            {
              localPdfException.printStackTrace();
            }
        }
        localJPanel.updateUI();
      }
    };
    localJLabel.setFont(this.headFont);
    localJLabel.setForeground(Color.red);
    localSpringLayout.putConstraint("West", localJLabel, 5, "West", localJPanel);
    localSpringLayout.putConstraint("South", localJLabel, -5, "South", localJPanel);
    localJPanel.add(localJLabel);
    localJRadioButton3.setSelected(true);
    localJRadioButton3.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        localJRadioButton2.setText("Extract  as  XML");
        localJRadioButton1.setText("Extract as Text");
        SwingUtilities.invokeLater(local15);
      }
    });
    localButtonGroup1.add(localJRadioButton3);
    localJRadioButton3.setToolTipText(Messages.getMessage("PdfViewerRect.message"));
    localSpringLayout.putConstraint("West", localJRadioButton3, 10, "West", localJPanel);
    localSpringLayout.putConstraint("North", localJRadioButton3, 5, "North", localJPanel);
    localJPanel.add(localJRadioButton3);
    localJRadioButton4.setSelected(true);
    localJRadioButton4.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        localJRadioButton2.setText("Extract as XHTML");
        localJRadioButton1.setText("Extract as CSV");
        SwingUtilities.invokeLater(local15);
      }
    });
    localButtonGroup1.add(localJRadioButton4);
    localJRadioButton4.setToolTipText(Messages.getMessage("PdfViewerTable.message"));
    localSpringLayout.putConstraint("West", localJRadioButton4, 50, "East", localJRadioButton3);
    localSpringLayout.putConstraint("North", localJRadioButton4, 5, "North", localJPanel);
    localJPanel.add(localJRadioButton4);
    localJRadioButton5.setSelected(true);
    localJRadioButton5.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        localJRadioButton2.setText("Extract  as  XML");
        localJRadioButton1.setText("Extract as Text");
        SwingUtilities.invokeLater(local15);
      }
    });
    localButtonGroup1.add(localJRadioButton5);
    localJRadioButton5.setToolTipText(Messages.getMessage("PdfViewerWordList.message"));
    localSpringLayout.putConstraint("East", localJRadioButton5, -5, "East", localJPanel);
    localSpringLayout.putConstraint("North", localJRadioButton5, 5, "North", localJPanel);
    localJPanel.add(localJRadioButton5);
    localJScrollPane.setPreferredSize(new Dimension(315, 150));
    localJScrollPane.setMinimumSize(new Dimension(315, 150));
    localSpringLayout.putConstraint("East", localJScrollPane, -5, "East", localJPanel);
    localSpringLayout.putConstraint("North", localJScrollPane, 5, "South", localJRadioButton4);
    localJPanel.add(localJScrollPane);
    localButtonGroup2.add(localJRadioButton2);
    localButtonGroup2.add(localJRadioButton1);
    localJRadioButton2.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        SwingUtilities.invokeLater(local15);
      }
    });
    localJRadioButton1.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        SwingUtilities.invokeLater(local15);
      }
    });
    localJRadioButton1.setSelected(true);
    localSpringLayout.putConstraint("West", localJRadioButton2, 5, "West", localJPanel);
    localSpringLayout.putConstraint("South", localJRadioButton2, -5, "North", localJButton3);
    localJPanel.add(localJRadioButton2);
    localSpringLayout.putConstraint("East", localJRadioButton1, -5, "East", localJPanel);
    localSpringLayout.putConstraint("South", localJRadioButton1, -5, "North", localJButton3);
    localJPanel.add(localJRadioButton1);
    localSpringLayout.putConstraint("South", localJButton3, -5, "North", localJLabel);
    localSpringLayout.putConstraint("East", localJButton3, -5, "East", localJPanel);
    localJPanel.add(localJButton3);
    localSpringLayout.putConstraint("South", localJButton2, -5, "North", localJLabel);
    localSpringLayout.putConstraint("East", localJButton2, -5, "West", localJButton3);
    localJPanel.add(localJButton2);
    localSpringLayout.putConstraint("South", localJButton1, -5, "North", localJLabel);
    localSpringLayout.putConstraint("East", localJButton1, -5, "West", localJButton2);
    localJPanel.add(localJButton1);
    localJButton1.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        JTextArea localJTextArea = new JTextArea(Messages.getMessage("PdfViewerGroupingInfo.message"));
        Commands.this.currentGUI.showMessageDialog(localJTextArea);
        Commands.this.display = true;
      }
    });
    localJButton2.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        localJFrame.setVisible(false);
        localJFrame.dispose();
      }
    });
    localJButton3.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        try
        {
          String str1 = "";
          int i = 0;
          boolean bool = true;
          Enumeration localEnumeration = localButtonGroup1.getElements();
          while ((localEnumeration.hasMoreElements()) && (!((AbstractButton)localEnumeration.nextElement()).isSelected()))
            i++;
          for (int j = 0; j != arrayOfRectangle.length; j++)
          {
            String str2 = "";
            arrayOfRectangle[j] = Commands.adjustHighlightForExtraction(arrayOfRectangle[j]);
            int k = arrayOfRectangle[j].x;
            int m = arrayOfRectangle[j].x + arrayOfRectangle[j].width;
            int n = arrayOfRectangle[j].y + arrayOfRectangle[j].height;
            int i1 = arrayOfRectangle[j].y;
            int i2;
            if (n < i1)
            {
              i2 = i1;
              i1 = n;
              n = i2;
            }
            if (k > m)
            {
              i2 = m;
              m = k;
              k = i2;
            }
            if (k < Commands.this.currentGUI.cropX)
              k = Commands.this.currentGUI.cropX;
            if (k > Commands.this.currentGUI.cropW - Commands.this.currentGUI.cropX)
              k = Commands.this.currentGUI.cropW - Commands.this.currentGUI.cropX;
            if (m < Commands.this.currentGUI.cropX)
              m = Commands.this.currentGUI.cropX;
            if (m > Commands.this.currentGUI.cropW - Commands.this.currentGUI.cropX)
              m = Commands.this.currentGUI.cropW - Commands.this.currentGUI.cropX;
            if (n < Commands.this.currentGUI.cropY)
              n = Commands.this.currentGUI.cropY;
            if (n > Commands.this.currentGUI.cropH - Commands.this.currentGUI.cropY)
              n = Commands.this.currentGUI.cropH - Commands.this.currentGUI.cropY;
            if (i1 < Commands.this.currentGUI.cropY)
              i1 = Commands.this.currentGUI.cropY;
            if (i1 > Commands.this.currentGUI.cropH - Commands.this.currentGUI.cropY)
              i1 = Commands.this.currentGUI.cropH - Commands.this.currentGUI.cropY;
            switch (i)
            {
            case 0:
              str2 = Commands.this.extractTextRectangle(Commands.this.decode_pdf.getGroupingObject(), localJRadioButton2.isSelected(), k, m, n, i1) + '\r' + '\n';
              break;
            case 1:
              str2 = Commands.this.extractTextTable(Commands.this.decode_pdf.getGroupingObject(), localJRadioButton2.isSelected(), k, m, n, i1);
              break;
            case 2:
              str2 = Commands.this.extractTextList(Commands.this.decode_pdf.getGroupingObject(), localJRadioButton2.isSelected(), k, m, n, i1);
              break;
            }
            str1 = str1 + str2;
          }
          if (str1 != null)
          {
            JScrollPane localJScrollPane = new JScrollPane();
            try
            {
              JTextPane localJTextPane = new JTextPane();
              localJScrollPane = Commands.this.currentGUI.createPane(localJTextPane, str1, bool);
            }
            catch (BadLocationException localBadLocationException)
            {
              localBadLocationException.printStackTrace();
            }
            localJScrollPane.setHorizontalScrollBarPolicy(31);
            localJScrollPane.setVerticalScrollBarPolicy(20);
            localJScrollPane.setPreferredSize(new Dimension(400, 400));
            final JDialog localJDialog = new JDialog((JFrame)null, true);
            localJDialog.setDefaultCloseOperation(2);
            if (Commands.this.commonValues.getModeOfOperation() != 1)
            {
              localObject = Commands.this.currentGUI.getFrame();
              localJDialog.setLocation(((Container)localObject).getLocationOnScreen().x + 10, ((Container)localObject).getLocationOnScreen().y + 10);
            }
            localJDialog.setSize(450, 450);
            localJDialog.setTitle(Messages.getMessage("PdfViewerExtractedText.menu"));
            localJDialog.getContentPane().setLayout(new BorderLayout());
            localJDialog.getContentPane().add(localJScrollPane, "Center");
            Object localObject = new JPanel();
            ((JPanel)localObject).setLayout(new BorderLayout());
            localJDialog.getContentPane().add((Component)localObject, "South");
            JButton localJButton1 = new JButton(Messages.getMessage("PdfViewerMenu.return"));
            localJButton1.setFont(new Font("SansSerif", 0, 12));
            ((JPanel)localObject).add(localJButton1, "West");
            localJButton1.addActionListener(new ActionListener()
            {
              public void actionPerformed(ActionEvent paramAnonymous2ActionEvent)
              {
                Commands.this.display = true;
                localJDialog.dispose();
              }
            });
            JButton localJButton2 = new JButton(Messages.getMessage("PdfViewerFileMenuExit.text"));
            localJButton2.setFont(new Font("SansSerif", 0, 12));
            ((JPanel)localObject).add(localJButton2, "East");
            localJButton2.addActionListener(new ActionListener()
            {
              public void actionPerformed(ActionEvent paramAnonymous2ActionEvent)
              {
                localJDialog.dispose();
              }
            });
            localJDialog.setVisible(true);
          }
        }
        catch (PdfException localPdfException)
        {
          localPdfException.printStackTrace();
        }
      }
    });
    localJFrame.getContentPane().add(localJPanel, "Center");
    localJFrame.setSize(350, 300);
    SwingUtilities.invokeLater(local15);
    localJFrame.setLocationRelativeTo(this.currentGUI.getFrame());
    localJFrame.setResizable(false);
    localJFrame.setVisible(true);
  }

  private String extractTextList(PdfGroupingAlgorithms paramPdfGroupingAlgorithms, boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    throws PdfException
  {
    String str1 = "";
    PdfGroupingAlgorithms.useUnrotatedCoords = true;
    PdfPageData localPdfPageData = this.decode_pdf.getPdfPageData();
    int i = localPdfPageData.getRotation(this.commonValues.getCurrentPage());
    if (i != 0)
    {
      int j = this.currentGUI.showConfirmDialog(Messages.getMessage("PdfViewerRotatedCoords.message"), Messages.getMessage("PdfViewerOutputFormat.message"), 0);
      if (j == 0)
        PdfGroupingAlgorithms.useUnrotatedCoords = false;
    }
    List localList = paramPdfGroupingAlgorithms.extractTextAsWordlist(paramInt1, paramInt3, paramInt2, paramInt4, this.commonValues.getCurrentPage(), true, null);
    if (localList == null)
      this.currentGUI.showMessageDialog(new StringBuilder().append(Messages.getMessage("PdfViewerMessage.NoTextFound")).append("\nx1:").append(paramInt1).append(" y1:").append(paramInt3).append(" x2:").append(paramInt2).append(" y2:").append(paramInt4).toString());
    if (localList != null)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      Iterator localIterator = localList.iterator();
      while (localIterator.hasNext())
      {
        String str2 = (String)localIterator.next();
        if (!paramBoolean)
          str2 = Strip.convertToText(str2, this.decode_pdf.isXMLExtraction());
        int k = (int)Float.parseFloat((String)localIterator.next());
        int m = (int)Float.parseFloat((String)localIterator.next());
        int n = (int)Float.parseFloat((String)localIterator.next());
        int i1 = (int)Float.parseFloat((String)localIterator.next());
        localStringBuilder.append(str2).append(',').append(k).append(',').append(m).append(',').append(n).append(',').append(i1).append('\n');
      }
      if (localStringBuilder.toString() != null)
        str1 = localStringBuilder.toString();
    }
    return str1;
  }

  private String extractTextTable(PdfGroupingAlgorithms paramPdfGroupingAlgorithms, boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    throws PdfException
  {
    Map localMap;
    if (!paramBoolean)
      localMap = paramPdfGroupingAlgorithms.extractTextAsTable(paramInt1, paramInt3, paramInt2, paramInt4, this.commonValues.getCurrentPage(), true, false, false, false, 0);
    else
      localMap = paramPdfGroupingAlgorithms.extractTextAsTable(paramInt1, paramInt3, paramInt2, paramInt4, this.commonValues.getCurrentPage(), false, true, true, false, 1);
    if (localMap.get("content") != null)
      return (String)localMap.get("content");
    return "";
  }

  private String extractTextRectangle(PdfGroupingAlgorithms paramPdfGroupingAlgorithms, boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    throws PdfException
  {
    String str = paramPdfGroupingAlgorithms.extractTextInRectangle(paramInt1, paramInt3, paramInt2, paramInt4, this.commonValues.getCurrentPage(), false, true);
    if (str == null)
    {
      this.currentGUI.showMessageDialog(new StringBuilder().append(Messages.getMessage("PdfViewerMessage.NoTextFound")).append("\nx1:").append(paramInt1).append(" y1:").append(paramInt3).append(" x2:").append(paramInt2).append(" y2:").append(paramInt4).toString());
      return "";
    }
    if (!paramBoolean)
      str = Strip.stripXML(str, this.decode_pdf.isXMLExtraction()).toString();
    return str;
  }

  private void decodeImage(final boolean paramBoolean)
  {
    this.decode_pdf.getTextLines().clearHighlights();
    this.currentGUI.resetComboBoxes(false);
    this.currentGUI.setPageLayoutButtonsEnabled(false);
    this.decode_pdf.getDynamicRenderer().flush();
    this.decode_pdf.getPages().refreshDisplay();
    this.thumbnails.terminateDrawing();
    Values.setProcessing(true);
    SwingWorker local24 = new SwingWorker()
    {
      public Object construct()
      {
        try
        {
          Commands.this.currentGUI.updateStatusMessage(Messages.getMessage("PdfViewerDecoding.page"));
          if (Commands.this.img != null)
            Commands.this.addImage(Commands.this.img);
          PdfPageData localPdfPageData = Commands.this.decode_pdf.getPdfPageData();
          if (Commands.this.img != null)
            localPdfPageData.setMediaBox(new float[] { 0.0F, 0.0F, Commands.this.img.getWidth(), Commands.this.img.getHeight() });
          localPdfPageData.checkSizeSet(1);
          Commands.this.currentGUI.resetRotationBox();
          if ((paramBoolean) && (Commands.this.thumbnails.isShownOnscreen()))
            Commands.this.currentGUI.zoom();
          if (Thread.interrupted())
            throw new InterruptedException();
          Commands.this.currentGUI.setPageNumber();
          Commands.this.currentGUI.setViewerTitle(null);
          Commands.this.currentGUI.hideRedundentNavButtons();
        }
        catch (Exception localException)
        {
          Commands.this.currentGUI.setViewerTitle(null);
        }
        Commands.this.currentGUI.setStatusProgress(100);
        Commands.this.currentGUI.resetComboBoxes(true);
        Commands.this.decode_pdf.repaint();
        Commands.this.openingTransferedFile = false;
        return null;
      }
    };
    local24.start();
  }

  protected boolean openUpFile(String paramString)
    throws PdfException
  {
    this.commonValues.maxViewY = 0;
    this.searchFrame.resetSearchWindow();
    this.commonValues.setMultiTiff(false);
    boolean bool1 = true;
    if (this.currentGUI.isSingle())
      this.decode_pdf.closePdfFile();
    try
    {
      Object localObject3;
      Object localObject4;
      if (this.commonValues.isPDF())
      {
        if ((this.inputStream != null) || (paramString.startsWith("http")) || (paramString.startsWith("file:")) || (paramString.startsWith("jar:")))
        {
          try
          {
            boolean bool2 = false;
            if (this.inputStream != null)
              bool2 = true;
            else if (this.commonValues.getModeOfOperation() != 1)
              bool2 = isPDFLinearized(this.commonValues.getSelectedFile());
            Object localObject1;
            Object localObject2;
            if (!bool2)
            {
              if (this.commonValues.getSelectedFile().startsWith("jar:"))
              {
                localObject1 = getClass().getResourceAsStream(this.commonValues.getSelectedFile().substring(4));
                this.decode_pdf.openPdfFileFromInputStream((InputStream)localObject1, false);
              }
              else
              {
                localObject1 = new DownloadProgress(this.commonValues.getSelectedFile());
                Thread local25 = new Thread()
                {
                  public void run()
                  {
                    while (this.val$dlp.isDownloading())
                    {
                      Commands.this.currentGUI.setDownloadProgress("download", this.val$dlp.getProgress());
                      try
                      {
                        Thread.sleep(500L);
                      }
                      catch (Exception localException)
                      {
                      }
                    }
                  }
                };
                local25.setDaemon(true);
                local25.start();
                ((DownloadProgress)localObject1).startDownload();
                localObject2 = ((DownloadProgress)localObject1).getFile();
                this.decode_pdf.openPdfFile(((File)localObject2).getCanonicalPath());
              }
            }
            else
            {
              this.currentGUI.setViewerTitle(new StringBuilder().append("Loading linearized PDF ").append(this.commonValues.getSelectedFile()).toString());
              if (this.inputStream != null)
                this.decode_pdf.openPdfFileFromInputStream(this.inputStream, true);
              else
                this.decode_pdf.openPdfFileFromURL(this.commonValues.getSelectedFile(), true);
              localObject1 = (PdfObject)this.decode_pdf.getJPedalObject(2004845231);
              int j = ((PdfObject)localObject1).getInt(28);
              localObject2 = "Downloading ";
              j /= 1024;
              if (j < 1024)
              {
                localObject2 = new StringBuilder().append((String)localObject2).append(j).append(" kB").toString();
              }
              else
              {
                j /= 1024;
                localObject2 = new StringBuilder().append((String)localObject2).append(j).append(" M").toString();
              }
              localObject3 = localObject2;
              localObject4 = new Thread()
              {
                public void run()
                {
                  LinearThread localLinearThread = (LinearThread)Commands.this.decode_pdf.getJPedalObject(-1276915978);
                  while ((localLinearThread != null) && (localLinearThread.isAlive()))
                  {
                    try
                    {
                      Thread.sleep(1000L);
                    }
                    catch (InterruptedException localInterruptedException)
                    {
                      localInterruptedException.printStackTrace();
                    }
                    Commands.this.currentGUI.setDownloadProgress(this.val$fMessage, localLinearThread.getPercentageLoaded());
                  }
                  Commands.this.currentGUI.setDownloadProgress(this.val$fMessage, 100);
                  Commands.this.processPage();
                }
              };
              ((Thread)localObject4).setDaemon(true);
              ((Thread)localObject4).start();
            }
          }
          catch (Exception localException1)
          {
            this.currentGUI.showMessageDialog(new StringBuilder().append(Messages.getMessage("PdfViewer.UrlError")).append(" file=").append(paramString).append('\n').append(localException1.getMessage()).toString());
            this.decode_pdf.closePdfFile();
            bool1 = false;
          }
        }
        else
        {
          try
          {
            this.decode_pdf.openPdfFile(this.commonValues.getSelectedFile());
          }
          catch (RuntimeException localRuntimeException)
          {
            String str2;
            if (localRuntimeException.getMessage().contains("bouncycastle"))
              str2 = localRuntimeException.getMessage();
            else
              str2 = new StringBuilder().append("Exception in code ").append(localRuntimeException.getMessage()).append(" please send to IDRsolutions").toString();
            this.currentGUI.showMessageDialog(str2);
            LogWriter.writeLog(new StringBuilder().append("Exception ").append(localRuntimeException.getMessage()).toString());
          }
          if (this.decode_pdf.getPageCount() > 1)
            this.currentGUI.setPageLayoutButtonsEnabled(true);
        }
        this.currentGUI.reinitThumbnails();
      }
      else
      {
        this.decode_pdf.resetForNonPDFPage(1);
        this.lastPageDecoded = 1;
        boolean bool3 = paramString.toLowerCase().contains(".tif");
        if (JAIHelper.isJAIused())
          JAIHelper.confirmJAIOnClasspath();
        int i = (paramString.startsWith("http:")) || (paramString.startsWith("file:")) ? 1 : 0;
        if ((bool3) && (JAIHelper.isJAIused()))
        {
          try
          {
            this.tiffHelper = new JAITiffHelper(this.commonValues.getSelectedFile());
            int k = this.tiffHelper.getTiffPageCount();
            this.tiffImageToLoad = 0;
            if (k > 1)
            {
              this.decode_pdf.resetForNonPDFPage(k);
              this.commonValues.setPageCount(k);
              this.lastPageDecoded = 1;
              this.commonValues.setMultiTiff(true);
              this.currentGUI.setMultiPageTiff(true);
            }
            drawMultiPageTiff();
          }
          catch (Exception localException2)
          {
            localException2.printStackTrace();
            LogWriter.writeLog(new StringBuilder().append("Exception ").append(localException2).append(Messages.getMessage("PdfViewerError.Loading")).append(this.commonValues.getSelectedFile()).toString());
          }
        }
        else
        {
          String str3 = this.properties.getValue("showtiffmessage");
          int m = (bool3) && (!str3.isEmpty()) && (str3.equals("true")) ? 1 : 0;
          if (m != 0)
          {
            localObject3 = new JPanel();
            ((JPanel)localObject3).setLayout(new GridBagLayout());
            localObject4 = new GridBagConstraints();
            ((GridBagConstraints)localObject4).anchor = 17;
            ((GridBagConstraints)localObject4).gridx = 0;
            ((GridBagConstraints)localObject4).gridy = 0;
            String str4 = "<html>Some Tiff images do not display correctly without JAI support turned on.  <br>See <a href=\"http://www.idrsolutions.com/jvm-flags\"> http://www.idrsolutions.com/jvm-flags</a> for information on enabling JAI.";
            JCheckBox localJCheckBox = new JCheckBox();
            localJCheckBox.setText(Messages.getMessage("PdfViewerFormsWarning.CheckBox"));
            Font localFont = localJCheckBox.getFont();
            JEditorPane localJEditorPane = new JEditorPane();
            localJEditorPane.addHyperlinkListener(new HyperlinkListener()
            {
              public void hyperlinkUpdate(HyperlinkEvent paramAnonymousHyperlinkEvent)
              {
                if (paramAnonymousHyperlinkEvent.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED))
                  try
                  {
                    BrowserLauncher.openURL(paramAnonymousHyperlinkEvent.getURL().toExternalForm());
                  }
                  catch (IOException localIOException)
                  {
                    localIOException.printStackTrace();
                  }
              }
            });
            localJEditorPane.setEditable(false);
            localJEditorPane.setContentType("text/html");
            localJEditorPane.setText(str4);
            localJEditorPane.setOpaque(false);
            localJEditorPane.setFont(localFont);
            ((GridBagConstraints)localObject4).ipady = 20;
            ((JPanel)localObject3).add(localJEditorPane, localObject4);
            ((GridBagConstraints)localObject4).ipady = 0;
            ((GridBagConstraints)localObject4).gridy = 1;
            ((JPanel)localObject3).add(localJCheckBox, localObject4);
            JOptionPane.showMessageDialog(this.currentGUI.getFrame(), localObject3);
            if (localJCheckBox.isSelected())
              this.properties.setValue("showtiffmessage", "false");
          }
          try
          {
            if (i != 0)
              this.img = ImageIO.read(new URL(paramString));
            else
              this.img = ImageIO.read(new File(paramString));
          }
          catch (Exception localException3)
          {
            LogWriter.writeLog(new StringBuilder().append("Exception ").append(localException3).append("loading ").append(this.commonValues.getSelectedFile()).toString());
          }
        }
      }
      this.currentGUI.updateStatusMessage("opening file");
      if ((bool1) && (this.decode_pdf.isEncrypted()) && (!this.decode_pdf.isFileViewable()))
      {
        bool1 = false;
        String str1 = System.getProperty("org.jpedal.password");
        if (str1 == null)
          str1 = this.currentGUI.showInputDialog(Messages.getMessage("PdfViewerPassword.message"));
        if (str1 != null)
        {
          this.decode_pdf.setEncryptionPassword(str1);
          if (this.decode_pdf.isFileViewable())
            bool1 = true;
        }
        if (!bool1)
          this.currentGUI.showMessageDialog(Messages.getMessage("PdfViewerPasswordRequired.message"));
      }
      if (bool1)
      {
        if (this.properties.getValue("Recentdocuments").equals("true"))
        {
          this.properties.addRecentDocument(this.commonValues.getSelectedFile());
          updateRecentDocuments(this.properties.getRecentDocuments());
        }
        this.recent.addToFileList(this.commonValues.getSelectedFile());
        this.commonValues.setCurrentPage(1);
      }
    }
    catch (PdfException localPdfException)
    {
      System.err.println(new StringBuilder().append("Exception ").append(localPdfException).append(" opening file").toString());
      if (this.currentGUI.isSingle())
      {
        if (Viewer.showMessages)
          ErrorDialog.showError(localPdfException, Messages.getMessage("PdfViewerOpenerror"), this.currentGUI.getFrame(), this.commonValues.getSelectedFile());
        exit();
      }
      throw localPdfException;
    }
    if ((!this.decode_pdf.isOpen()) && (this.commonValues.isPDF()) && (this.decode_pdf.getJPedalObject(2004845231) == null))
      return false;
    return bool1;
  }

  public void openFile(String paramString)
    throws PdfException
  {
    this.isPDf = false;
    this.currentGUI.setMultiPageTiff(false);
    String str1 = System.getProperty("org.jpedal.hires");
    if ((hires) || (str1 != null))
      this.commonValues.setUseHiresImage(true);
    String str2 = System.getProperty("org.jpedal.memory");
    if (str2 != null)
      this.commonValues.setUseHiresImage(false);
    this.thumbnails.resetToDefault();
    this.currentGUI.setNoPagesDecoded();
    if (this.searchFrame != null)
      this.searchFrame.removeSearchWindow(false);
    this.commonValues.maxViewY = 0;
    String str3 = paramString.toLowerCase().trim();
    this.commonValues.setPDF((str3.endsWith(".pdf")) || (str3.endsWith(".fdf")));
    this.isPDf = ((str3.endsWith(".pdf")) || (str3.endsWith(".fdf")));
    if (!this.commonValues.isPDF())
      if (SwingUtilities.isEventDispatchThread())
      {
        this.decode_pdf.setDisplayView(1, 2);
      }
      else
      {
        Runnable local28 = new Runnable()
        {
          public void run()
          {
            Commands.this.decode_pdf.setDisplayView(1, 2);
          }
        };
        SwingUtilities.invokeLater(local28);
      }
    this.currentGUI.setQualityBoxVisible(this.commonValues.isPDF());
    this.commonValues.setCurrentPage(1);
    try
    {
      boolean bool = openUpFile(this.commonValues.getSelectedFile());
      if (bool)
      {
        processPage();
      }
      else
      {
        this.currentGUI.setViewerTitle(Messages.getMessage("PdfViewer.NoFile"));
        this.decode_pdf.getDynamicRenderer().flush();
        this.decode_pdf.getPages().refreshDisplay();
        this.currentGUI.zoom();
        this.commonValues.setPageCount(1);
        this.commonValues.setCurrentPage(1);
      }
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
      System.err.println(new StringBuilder().append(Messages.getMessage("PdfViewerError.Exception")).append(' ').append(localException).append(' ').append(Messages.getMessage("PdfViewerError.DecodeFile")).toString());
    }
  }

  protected void processPage()
  {
    if ((this.commonValues.isPDF()) && ((this.decode_pdf.isOpen()) || (!this.commonValues.isPDF()) || (this.decode_pdf.getJPedalObject(2004845231) != null)))
    {
      PdfFileInformation localPdfFileInformation = this.decode_pdf.getFileInformationData();
      this.decode_pdf.setRenderMode(3);
      String[] arrayOfString1 = localPdfFileInformation.getFieldValues();
      String[] arrayOfString2 = PdfFileInformation.getFieldNames();
      String[] arrayOfString3 = { "TeleForm", "dgn2pdf" };
      for (int i = 0; i < arrayOfString2.length; i++)
        if ((arrayOfString2[i].equals("Creator")) || (arrayOfString2[i].equals("Producer")))
        {
          for (String str : arrayOfString3)
            if (arrayOfString1[i].equals(str))
              this.decode_pdf.setRenderMode(2);
          if (arrayOfString1[i].equals("ABBYY FineReader 8.0 Professional Edition"))
            this.decode_pdf.setRenderMode(35);
        }
      boolean bool = Values.isProcessing();
      Values.setProcessing(true);
      if (this.commonValues.isUseHiresImage())
      {
        this.decode_pdf.useHiResScreenDisplay(true);
        this.currentGUI.setSelectedComboIndex(250, 1);
      }
      else
      {
        this.decode_pdf.useHiResScreenDisplay(false);
        this.currentGUI.setSelectedComboIndex(250, 0);
      }
      Values.setProcessing(bool);
    }
    if (this.commonValues.isPDF())
    {
      this.commonValues.setPageCount(this.decode_pdf.getPageCount());
    }
    else if (!this.commonValues.isMultiTiff())
    {
      this.commonValues.setPageCount(1);
      this.decode_pdf.useHiResScreenDisplay(true);
    }
    if (this.commonValues.getPageCount() < this.commonValues.getCurrentPage())
    {
      this.commonValues.setCurrentPage(this.commonValues.getPageCount());
      System.err.println(new StringBuilder().append(this.commonValues.getCurrentPage()).append(" out of range. Opening on last page").toString());
      LogWriter.writeLog(new StringBuilder().append(this.commonValues.getCurrentPage()).append(" out of range. Opening on last page").toString());
    }
    this.decode_pdf.setExtractionMode(1, this.currentGUI.getScaling());
    this.currentGUI.setPageNumber();
    this.currentGUI.resetRotationBox();
    if (this.commonValues.isPDF())
    {
      this.currentGUI.messageShown = false;
      this.currentGUI.decodePage();
    }
    else
    {
      this.currentGUI.zoom();
      this.decode_pdf.setPDFBorder(BorderFactory.createLineBorder(Color.black, 1));
      decodeImage(true);
      Values.setProcessing(false);
    }
  }

  public void selectFile()
  {
    if (this.searchFrame != null)
      this.searchFrame.removeSearchWindow(false);
    JFileChooser localJFileChooser = new JFileChooser(this.commonValues.getInputDir());
    localJFileChooser.setName("chooser");
    if (this.commonValues.getSelectedFile() != null)
      localJFileChooser.setSelectedFile(new File(this.commonValues.getSelectedFile()));
    localJFileChooser.setFileSelectionMode(0);
    String[] arrayOfString1 = { "pdf" };
    String[] arrayOfString2 = { "fdf" };
    String[] arrayOfString3 = { "png", "tif", "tiff", "jpg", "jpeg" };
    localJFileChooser.addChoosableFileFilter(new FileFilterer(arrayOfString3, "Images (Tiff, Jpeg,Png)"));
    localJFileChooser.addChoosableFileFilter(new FileFilterer(arrayOfString2, "fdf (*.fdf)"));
    localJFileChooser.addChoosableFileFilter(new FileFilterer(arrayOfString1, "Pdf (*.pdf)"));
    int i = localJFileChooser.showOpenDialog(this.currentGUI.getFrame());
    File localFile = localJFileChooser.getSelectedFile();
    if ((localFile != null) && (i == 0))
    {
      this.currentGUI.resetNavBar();
      String str = localFile.getName().toLowerCase();
      int j = (str.endsWith(".pdf")) || (str.endsWith(".fdf")) || (str.endsWith(".tif")) || (str.endsWith(".tiff")) || (str.endsWith(".png")) || (str.endsWith(".jpg")) || (str.endsWith(".jpeg")) ? 1 : 0;
      if (j != 0)
      {
        try
        {
          this.commonValues.setInputDir(localJFileChooser.getCurrentDirectory().getCanonicalPath());
          open(localFile.getAbsolutePath());
        }
        catch (IOException localIOException)
        {
          localIOException.printStackTrace();
        }
      }
      else
      {
        this.decode_pdf.repaint();
        this.currentGUI.showMessageDialog(Messages.getMessage("PdfViewer.NotValidPdfWarning"));
      }
    }
    else
    {
      this.decode_pdf.repaint();
      this.currentGUI.showMessageDialog(Messages.getMessage("PdfViewerMessage.NoSelection"));
    }
  }

  private String selectURL()
  {
    String str = this.currentGUI.showInputDialog(Messages.getMessage("PdfViewerMessage.RequestURL"));
    if (str != null)
      str = str.trim();
    if ((str != null) && (!str.trim().startsWith("http://")) && (!str.trim().startsWith("https://")) && (!str.trim().startsWith("file:/")))
    {
      this.currentGUI.showMessageDialog(Messages.getMessage("PdfViewerMessage.URLMustContain"));
      str = null;
    }
    int i;
    if (str != null)
    {
      i = (str.endsWith(".pdf")) || (str.endsWith(".fdf")) || (str.endsWith(".tif")) || (str.endsWith(".tiff")) || (str.endsWith(".png")) || (str.endsWith(".jpg")) || (str.endsWith(".jpeg")) ? 1 : 0;
      if (i == 0)
      {
        this.currentGUI.showMessageDialog(Messages.getMessage("PdfViewer.NotValidPdfWarning"));
        str = null;
      }
    }
    if (str != null)
    {
      this.commonValues.setSelectedFile(str);
      i = 0;
      try
      {
        URL localURL = new URL(str);
        URLConnection localURLConnection = localURL.openConnection();
        if (localURLConnection.getContent() == null)
          i = 1;
      }
      catch (Exception localException2)
      {
        i = 1;
      }
      if (i != 0)
      {
        str = null;
        this.currentGUI.showMessageDialog(new StringBuilder().append("URL ").append(str).append(' ').append(Messages.getMessage("PdfViewerError.DoesNotExist")).toString());
      }
    }
    if (str != null)
    {
      try
      {
        this.commonValues.setFileSize(0L);
        this.currentGUI.setViewerTitle(null);
      }
      catch (Exception localException1)
      {
        System.err.println(new StringBuilder().append(Messages.getMessage("PdfViewerError.Exception")).append(' ').append(localException1).append(' ').append(Messages.getMessage("PdfViewerError.GettingPaths")).toString());
      }
      if ((str != null) && (!Values.isProcessing()))
      {
        this.thumbnails.terminateDrawing();
        this.decode_pdf.flushObjectValues(true);
        this.decode_pdf.resetViewableArea();
        this.currentGUI.stopThumbnails();
        if (!this.currentGUI.isSingle())
          openNewMultiplePage(this.commonValues.getSelectedFile());
        try
        {
          openFile(this.commonValues.getSelectedFile());
        }
        catch (PdfException localPdfException)
        {
        }
      }
    }
    else
    {
      this.decode_pdf.repaint();
      this.currentGUI.showMessageDialog(Messages.getMessage("PdfViewerMessage.NoSelection"));
    }
    return str;
  }

  public void setPageTurnAnimating(boolean paramBoolean)
  {
    this.pageTurnAnimating = paramBoolean;
    if (paramBoolean)
    {
      this.currentGUI.forward.setEnabled(false);
      this.currentGUI.back.setEnabled(false);
      this.currentGUI.fforward.setEnabled(false);
      this.currentGUI.fback.setEnabled(false);
      this.currentGUI.end.setEnabled(false);
      this.currentGUI.first.setEnabled(false);
    }
    else
    {
      this.currentGUI.hideRedundentNavButtons();
    }
  }

  public boolean getPageTurnAnimating()
  {
    return this.pageTurnAnimating;
  }

  private void navigatePages(int paramInt)
  {
    if (paramInt == 0)
      return;
    int i = this.commonValues.getCurrentPage() + paramInt;
    float f1;
    float f2;
    final Point localPoint3;
    final int j;
    Object localObject;
    if (paramInt > 0)
    {
      if (!this.decode_pdf.isPageAvailable(i))
      {
        this.currentGUI.showMessageDialog(new StringBuilder().append("Page ").append(i).append(" is not yet loaded").toString());
        return;
      }
      if (!Values.isProcessing())
      {
        if (i <= this.commonValues.getPageCount())
          if (this.commonValues.isMultiTiff())
          {
            this.tiffImageToLoad = (this.lastPageDecoded - 1 + paramInt);
            drawMultiPageTiff();
            this.commonValues.setCurrentPage(i);
            this.lastPageDecoded = (this.tiffImageToLoad + 1);
            this.currentGUI.setPageNumber();
            this.decode_pdf.repaint();
          }
          else
          {
            if ((this.decode_pdf.getDisplayView() == 3) || (this.decode_pdf.getDisplayView() == 4))
              if ((this.decode_pdf.getPages().getBoolean(Display.BoolValue.SEPARATE_COVER)) || (this.decode_pdf.getDisplayView() != 3))
              {
                i++;
                if (i > this.commonValues.getPageCount())
                  i = this.commonValues.getPageCount();
                if (((i & 0x1) == 1) && (i != 1))
                  i--;
                if (this.decode_pdf.getDisplayView() == 3)
                  paramInt = i / 2 - this.commonValues.getCurrentPage() / 2;
              }
              else
              {
                i++;
                if ((i & 0x1) == 0)
                  i--;
                paramInt = (i + 1) / 2 - (this.commonValues.getCurrentPage() + 1) / 2;
              }
            if ((paramInt == 1) && (this.decode_pdf.getDisplayView() == 3) && (this.decode_pdf.getPages().getBoolean(Display.BoolValue.TURNOVER_ON)) && (this.decode_pdf.getPageCount() != 2) && (this.currentGUI.getPageTurnScalingAppropriate()) && (i / 2 != this.commonValues.getCurrentPage() / 2) && (!this.decode_pdf.getPdfPageData().hasMultipleSizes()) && (!this.pageTurnAnimating))
            {
              f1 = this.decode_pdf.getPdfPageData().getCropBoxWidth(1);
              f2 = this.decode_pdf.getPdfPageData().getCropBoxHeight(1);
              if (this.decode_pdf.getPdfPageData().getRotation(1) % 180 == 90)
              {
                float f3 = f1;
                f1 = f2;
                f2 = f3;
              }
              final Point localPoint1 = new Point();
              localPoint1.x = ((int)(this.decode_pdf.getVisibleRect().getWidth() / 2.0D - f1));
              localPoint1.y = ((int)(this.decode_pdf.getInsetH() + f2));
              localPoint3 = new Point();
              localPoint3.x = ((int)(this.decode_pdf.getVisibleRect().getWidth() / 2.0D + f1));
              localPoint3.y = ((int)(this.decode_pdf.getInsetH() + f2));
              j = i;
              localObject = new Thread()
              {
                public void run()
                {
                  int i = 1;
                  if (localPoint3.x <= localPoint1.x)
                    localPoint3.x = (localPoint1.x - 1);
                  double d1 = localPoint1.x - localPoint3.x;
                  while (localPoint3.getX() >= localPoint1.getX())
                  {
                    double d2 = i * d1 * 0.001D;
                    if (d2 > -1.0D)
                      d2 = -1.0D;
                    localPoint3.setLocation(localPoint3.getX() + d2, localPoint3.getY());
                    Commands.this.decode_pdf.setUserOffsets((int)localPoint3.getX(), (int)localPoint3.getY(), 999);
                    if (i < 32)
                      i *= 2;
                    try
                    {
                      Thread.sleep(50L);
                    }
                    catch (Exception localException)
                    {
                      localException.printStackTrace();
                    }
                  }
                  Commands.this.commonValues.setCurrentPage(j);
                  Commands.this.currentGUI.setPageNumber();
                  Commands.this.decode_pdf.setPageParameters(Commands.this.currentGUI.getScaling(), Commands.this.commonValues.getCurrentPage());
                  Commands.this.currentGUI.decodePage();
                  Commands.this.setPageTurnAnimating(false);
                  Commands.this.decode_pdf.setUserOffsets(0, 0, 995);
                }
              };
              ((Thread)localObject).setDaemon(true);
              setPageTurnAnimating(true);
              ((Thread)localObject).start();
            }
            else
            {
              this.commonValues.setCurrentPage(i);
              if ((this.decode_pdf.getDisplayView() == 2) || (this.decode_pdf.getDisplayView() == 4))
              {
                this.currentGUI.decodePage();
                return;
              }
              this.currentGUI.resetStatusMessage(new StringBuilder().append("Loading Page ").append(this.commonValues.getCurrentPage()).toString());
              this.decode_pdf.setPageParameters(this.currentGUI.getScaling(), this.commonValues.getCurrentPage());
              if (this.commonValues.isPDF())
                this.currentGUI.decodePage();
            }
          }
      }
      else
        this.currentGUI.showMessageDialog(Messages.getMessage("PdfViewerDecodeWait.message"));
    }
    else
    {
      if (!this.decode_pdf.isPageAvailable(i))
      {
        this.currentGUI.showMessageDialog(new StringBuilder().append("Page ").append(i).append(" is not yet loaded").toString());
        return;
      }
      if (!Values.isProcessing())
      {
        if (i >= 1)
          if (this.commonValues.isMultiTiff())
          {
            this.tiffImageToLoad = (this.lastPageDecoded - 1 + paramInt);
            drawMultiPageTiff();
            this.commonValues.setCurrentPage(i);
            this.lastPageDecoded = (this.tiffImageToLoad + 1);
            this.currentGUI.setPageNumber();
            this.decode_pdf.repaint();
          }
          else
          {
            if ((this.decode_pdf.getDisplayView() == 3) || (this.decode_pdf.getDisplayView() == 4))
              if ((this.decode_pdf.getPages().getBoolean(Display.BoolValue.SEPARATE_COVER)) || (this.decode_pdf.getDisplayView() != 3))
              {
                if (paramInt == -1)
                  i--;
                if (i < 1)
                  i = 1;
                if (((i & 0x1) == 1) && (i != 1))
                  i--;
                if (this.decode_pdf.getDisplayView() == 3)
                  paramInt = i / 2 - this.commonValues.getCurrentPage() / 2;
              }
              else
              {
                if ((i & 0x1) == 0)
                  i--;
                if (this.decode_pdf.getDisplayView() == 3)
                  paramInt = (i + 1) / 2 - (this.commonValues.getCurrentPage() + 1) / 2;
              }
            if ((paramInt == -1) && (this.decode_pdf.getDisplayView() == 3) && (this.decode_pdf.getPages().getBoolean(Display.BoolValue.TURNOVER_ON)) && (this.currentGUI.getPageTurnScalingAppropriate()) && (this.decode_pdf.getPageCount() != 2) && ((i != this.commonValues.getCurrentPage() - 1) || (i == 1)) && (!this.decode_pdf.getPdfPageData().hasMultipleSizes()) && (!this.pageTurnAnimating))
            {
              f1 = this.decode_pdf.getPdfPageData().getCropBoxWidth(1);
              f2 = this.decode_pdf.getPdfPageData().getCropBoxHeight(1);
              if (this.decode_pdf.getPdfPageData().getRotation(1) % 180 == 90)
              {
                float f4 = f1;
                f1 = f2;
                f2 = f4;
              }
              final Point localPoint2 = new Point();
              localPoint2.x = ((int)(this.decode_pdf.getVisibleRect().getWidth() / 2.0D + f1));
              localPoint2.y = ((int)(this.decode_pdf.getInsetH() + f2));
              localPoint3 = new Point();
              localPoint3.x = ((int)(this.decode_pdf.getVisibleRect().getWidth() / 2.0D - f1));
              localPoint3.y = ((int)(this.decode_pdf.getInsetH() + f2));
              j = i;
              localObject = new Thread()
              {
                public void run()
                {
                  int i = 1;
                  if (localPoint3.x >= localPoint2.x)
                    localPoint3.x = (localPoint2.x - 1);
                  double d1 = localPoint2.x - localPoint3.x;
                  while (localPoint3.getX() <= localPoint2.getX())
                  {
                    double d2 = i * d1 * 0.001D;
                    if (d2 < 1.0D)
                      d2 = 1.0D;
                    localPoint3.setLocation(localPoint3.getX() + d2, localPoint3.getY());
                    Commands.this.decode_pdf.setUserOffsets((int)localPoint3.getX(), (int)localPoint3.getY(), 998);
                    if (i < 32)
                      i *= 2;
                    try
                    {
                      Thread.sleep(50L);
                    }
                    catch (Exception localException)
                    {
                      localException.printStackTrace();
                    }
                  }
                  Commands.this.commonValues.setCurrentPage(j);
                  Commands.this.currentGUI.setPageNumber();
                  Commands.this.decode_pdf.setPageParameters(Commands.this.currentGUI.getScaling(), Commands.this.commonValues.getCurrentPage());
                  Commands.this.currentGUI.decodePage();
                  Commands.this.decode_pdf.setUserOffsets(0, 0, 995);
                  Commands.this.setPageTurnAnimating(false);
                }
              };
              ((Thread)localObject).setDaemon(true);
              setPageTurnAnimating(true);
              ((Thread)localObject).start();
            }
            else
            {
              this.commonValues.setCurrentPage(i);
              if ((this.decode_pdf.getDisplayView() == 2) || (this.decode_pdf.getDisplayView() == 4))
              {
                this.currentGUI.decodePage();
                return;
              }
              this.currentGUI.resetStatusMessage(new StringBuilder().append("loading page ").append(this.commonValues.getCurrentPage()).toString());
              this.decode_pdf.setPageParameters(this.currentGUI.getScaling(), this.commonValues.getCurrentPage());
              if (this.commonValues.isPDF())
                this.currentGUI.decodePage();
            }
          }
      }
      else
        this.currentGUI.showMessageDialog(Messages.getMessage("PdfViewerDecodeWait.message"));
    }
    if (this.currentGUI.getThumbnailScrollBar() != null)
      this.currentGUI.getThumbnailScrollBar().setValue(this.commonValues.getCurrentPage() - 1);
    this.currentGUI.hideRedundentNavButtons();
    this.currentGUI.setPageNumber();
  }

  public void gotoPage(String paramString)
  {
    paramString = paramString.split("/")[0];
    int i;
    try
    {
      i = Integer.parseInt(paramString);
      if (!this.decode_pdf.isPageAvailable(i))
      {
        this.currentGUI.showMessageDialog(new StringBuilder().append("Page ").append(i).append(" is not yet loaded").toString());
        this.currentGUI.pageCounter2.setText(String.valueOf(this.commonValues.getCurrentPage()));
        return;
      }
      if ((this.decode_pdf.getDisplayView() == 3) || (this.decode_pdf.getDisplayView() == 4))
        if (((this.decode_pdf.getPages().getBoolean(Display.BoolValue.SEPARATE_COVER)) || (this.decode_pdf.getDisplayView() != 3)) && ((i & 0x1) == 1) && (i != 1))
          i--;
        else if ((!this.decode_pdf.getPages().getBoolean(Display.BoolValue.SEPARATE_COVER)) && ((i & 0x1) == 0))
          i--;
      if (((i > this.decode_pdf.getPageCount() ? 1 : 0) | (i < 1 ? 1 : 0)) != 0)
      {
        this.currentGUI.showMessageDialog(new StringBuilder().append(Messages.getMessage("PdfViewerPageLabel.text")).append(' ').append(paramString).append(' ').append(Messages.getMessage("PdfViewerOutOfRange.text")).append(' ').append(this.decode_pdf.getPageCount()).toString());
        i = this.commonValues.getCurrentPage();
        this.currentGUI.setPageNumber();
      }
    }
    catch (Exception localException)
    {
      this.currentGUI.showMessageDialog(new StringBuilder().append('>').append(paramString).append("< ").append(Messages.getMessage("PdfViewerInvalidNumber.text")).toString());
      i = this.commonValues.getCurrentPage();
      this.currentGUI.pageCounter2.setText(String.valueOf(this.commonValues.getCurrentPage()));
    }
    navigatePages(i - this.commonValues.getCurrentPage());
    if (this.decode_pdf.getDisplayView() == 5)
      navigatePages(0);
  }

  private void open(String paramString)
  {
    this.currentGUI.resetNavBar();
    int i = (paramString.startsWith("http:")) || (paramString.startsWith("file:")) ? 1 : 0;
    try
    {
      if (i == 0)
      {
        this.fileIsURL = false;
        this.commonValues.setFileSize(new File(paramString).length() >> 10);
      }
      else
      {
        this.fileIsURL = true;
      }
      this.commonValues.setSelectedFile(paramString);
      this.currentGUI.setViewerTitle(null);
    }
    catch (Exception localException)
    {
      LogWriter.writeLog(new StringBuilder().append("Exception ").append(localException).append(" getting paths").toString());
    }
    File localFile = new File(this.commonValues.getSelectedFile());
    if ((i == 0) && (!localFile.exists()))
    {
      this.currentGUI.showMessageDialog(new StringBuilder().append(Messages.getMessage("PdfViewerFile.text")).append(this.commonValues.getSelectedFile()).append(Messages.getMessage("PdfViewerNotExist")).toString());
    }
    else if ((this.commonValues.getSelectedFile() != null) && (!Values.isProcessing()))
    {
      if (this.currentGUI.isSingle())
        this.decode_pdf.flushObjectValues(true);
      else
        this.decode_pdf = openNewMultiplePage(this.commonValues.getSelectedFile());
      this.decode_pdf.resetViewableArea();
      try
      {
        openFile(this.commonValues.getSelectedFile());
      }
      catch (PdfException localPdfException)
      {
      }
    }
  }

  public void updateRecentDocuments(String[] paramArrayOfString)
  {
    if (paramArrayOfString == null)
      return;
    for (int i = 0; i < paramArrayOfString.length; i++)
      if (paramArrayOfString[i] != null)
      {
        String str = RecentDocuments.getShortenedFileName(paramArrayOfString[i]);
        if (this.recentDocuments[i] == null)
          this.recentDocuments[i] = new JMenuItem();
        this.recentDocuments[i].setText(new StringBuilder().append(i + 1).append(": ").append(str).toString());
        if (this.recentDocuments[i].getText().equals(new StringBuilder().append(i + 1).append(": ").toString()))
          this.recentDocuments[i].setVisible(false);
        else
          this.recentDocuments[i].setVisible(true);
        this.recentDocuments[i].setName(paramArrayOfString[i]);
      }
  }

  public void enableRecentDocuments(boolean paramBoolean)
  {
    if (this.recentDocuments == null)
      return;
    for (int i = 0; i < this.recentDocuments.length; i++)
      if ((this.recentDocuments[i] != null) && (!this.recentDocuments[i].getText().equals(new StringBuilder().append(i + 1).append(": ").toString())))
      {
        this.recentDocuments[i].setVisible(paramBoolean);
        this.recentDocuments[i].setEnabled(paramBoolean);
      }
  }

  private void drawMultiPageTiff()
  {
    if (this.tiffHelper != null)
    {
      this.img = this.tiffHelper.getImage(this.tiffImageToLoad);
      if (this.img != null)
      {
        this.decode_pdf.getDynamicRenderer().flush();
        this.decode_pdf.getPages().refreshDisplay();
        addImage(this.img);
      }
    }
  }

  public void addImage(BufferedImage paramBufferedImage)
  {
    GraphicsState localGraphicsState = new GraphicsState(false);
    localGraphicsState.CTM[0][0] = paramBufferedImage.getWidth();
    localGraphicsState.CTM[1][1] = paramBufferedImage.getHeight();
    this.decode_pdf.getDynamicRenderer().drawImage(1, paramBufferedImage, localGraphicsState, false, "image", 0, -1);
    if (this.currentGUI.isMultiPageTiff())
    {
      if (paramBufferedImage != null)
        this.decode_pdf.getPdfPageData().setMediaBox(new float[] { 0.0F, 0.0F, paramBufferedImage.getWidth(), paramBufferedImage.getHeight() });
      this.decode_pdf.getPdfPageData().checkSizeSet(1);
    }
  }

  public void recentDocumentsOption(JMenu paramJMenu)
  {
    String[] arrayOfString = this.properties.getRecentDocuments();
    if (arrayOfString == null)
      return;
    for (int i = 0; i < this.noOfRecentDocs; i++)
    {
      if (arrayOfString[i] == null)
        arrayOfString[i] = "";
      try
      {
        String str1 = arrayOfString[i];
        String str2 = RecentDocuments.getShortenedFileName(str1);
        this.recentDocuments[i] = new JMenuItem(new StringBuilder().append(i + 1).append(": ").append(str2).toString());
        if (this.recentDocuments[i].getText().equals(new StringBuilder().append(i + 1).append(": ").toString()))
          this.recentDocuments[i].setVisible(false);
        this.recentDocuments[i].setName(str1);
        this.recentDocuments[i].addActionListener(new ActionListener()
        {
          public void actionPerformed(ActionEvent paramAnonymousActionEvent)
          {
            if (Printer.isPrinting())
            {
              Commands.this.currentGUI.showMessageDialog(Messages.getMessage("PdfViewerPrintWait.message"));
            }
            else if (Values.isProcessing())
            {
              Commands.this.currentGUI.showMessageDialog(Messages.getMessage("PdfViewerDecodeWait.message"));
            }
            else
            {
              Commands.this.handleUnsaveForms();
              JMenuItem localJMenuItem = (JMenuItem)paramAnonymousActionEvent.getSource();
              String str = localJMenuItem.getName();
              if (!str.isEmpty())
                Commands.this.open(str);
            }
          }
        });
        paramJMenu.add(this.recentDocuments[i]);
      }
      catch (Exception localException)
      {
        LogWriter.writeLog(new StringBuilder().append("Problem with file ").append(arrayOfString[i]).toString());
      }
    }
  }

  private void saveFile()
  {
    int i = 0;
    while (i == 0)
    {
      JFileChooser localJFileChooser = new JFileChooser(this.commonValues.getInputDir());
      localJFileChooser.setSelectedFile(new File(new StringBuilder().append(this.commonValues.getInputDir()).append('/').append(this.commonValues.getSelectedFile()).toString()));
      localJFileChooser.addChoosableFileFilter(new FileFilterer(new String[] { "pdf" }, "Pdf (*.pdf)"));
      localJFileChooser.addChoosableFileFilter(new FileFilterer(new String[] { "fdf" }, "fdf (*.fdf)"));
      localJFileChooser.setFileSelectionMode(0);
      int j = localJFileChooser.showSaveDialog(null);
      if (j == 0)
      {
        FileInputStream localFileInputStream = null;
        FileOutputStream localFileOutputStream = null;
        File localFile = localJFileChooser.getSelectedFile();
        String str = localFile.getAbsolutePath();
        if (!str.endsWith(".pdf"))
        {
          str = new StringBuilder().append(str).append(".pdf").toString();
          localFile = new File(str);
        }
        if (str.equals(this.commonValues.getSelectedFile()))
          return;
        if (localFile.exists())
        {
          int k = this.currentGUI.showConfirmDialog(new StringBuilder().append(str).append('\n').append(Messages.getMessage("PdfViewerMessage.FileAlreadyExists")).append('\n').append(Messages.getMessage("PdfViewerMessage.ConfirmResave")).toString(), Messages.getMessage("PdfViewerMessage.Resave"), 0);
          if (k == 1);
        }
        else
        {
          try
          {
            localFileInputStream = new FileInputStream(this.commonValues.getSelectedFile());
            localFileOutputStream = new FileOutputStream(str);
            byte[] arrayOfByte = new byte[4096];
            int m;
            while ((m = localFileInputStream.read(arrayOfByte)) != -1)
              localFileOutputStream.write(arrayOfByte, 0, m);
          }
          catch (Exception localException1)
          {
            this.currentGUI.showMessageDialog(Messages.getMessage("PdfViewerException.NotSaveInternetFile"));
          }
          try
          {
            localFileInputStream.close();
            localFileOutputStream.close();
          }
          catch (Exception localException2)
          {
          }
          i = 1;
        }
      }
      else
      {
        return;
      }
    }
  }

  private void exit()
  {
    this.thumbnails.terminateDrawing();
    handleUnsaveForms();
    this.decode_pdf.closePdfFile();
    try
    {
      this.properties.setValue("lastDocumentPage", String.valueOf(this.commonValues.getCurrentPage()));
      if (this.properties.getValue("trackViewerSize").toLowerCase().equals("true"))
      {
        this.properties.setValue("startViewerWidth", new StringBuilder().append("").append(this.currentGUI.getFrame().getWidth()).toString());
        this.properties.setValue("startViewerHeight", new StringBuilder().append("").append(this.currentGUI.getFrame().getHeight()).toString());
      }
      if (this.properties.getValue("trackScaling").toLowerCase().equals("true"))
        this.properties.setValue("startScaling", new StringBuilder().append("").append(this.currentGUI.getSelectedComboItem(252)).toString());
      if (this.properties.getValue("trackView").toLowerCase().equals("true"))
        this.properties.setValue("startView", new StringBuilder().append("").append(this.decode_pdf.getDisplayView()).toString());
      if (this.properties.getValue("startSideTabOpen").toLowerCase().equals("true"))
        this.properties.setValue("startSideTabOpen", "true");
      if (this.properties.getValue("trackSelectedSideTab").toLowerCase().equals("true"))
        if (DecoderOptions.isRunningOnMac)
          this.properties.setValue("startSelectedSideTab", this.currentGUI.getSideTabBar().getTitleAt(this.currentGUI.getSideTabBar().getSelectedIndex()));
        else
          this.properties.setValue("startSelectedSideTab", this.currentGUI.getSideTabBar().getIconAt(this.currentGUI.getSideTabBar().getSelectedIndex()).toString());
      if (this.properties.getValue("trackSideTabExpandedSize").toLowerCase().equals("true"))
        this.properties.setValue("sideTabBarExpandLength", new StringBuilder().append("").append(this.currentGUI.getSplitDividerLocation()).toString());
    }
    catch (Exception localException)
    {
    }
    if (!Viewer.exitOnClose)
    {
      this.currentGUI.getFrame().setVisible(false);
      if ((this.currentGUI.getFrame() instanceof JFrame))
        ((JFrame)this.currentGUI.getFrame()).dispose();
    }
    else
    {
      this.currentGUI.getFrame().setVisible(false);
      if ((this.currentGUI.getFrame() instanceof JFrame))
        ((JFrame)this.currentGUI.getFrame()).dispose();
      this.decode_pdf.dispose();
      this.currentGUI.dispose();
      System.exit(0);
    }
  }

  public boolean checkForUpdates(boolean paramBoolean)
  {
    int i = 1;
    boolean bool = false;
    try
    {
      String str1 = null;
      str1 = "http://www.jpedal.org/demo_version.txt";
      if (str1 != null)
      {
        URL localURL = new URL(str1);
        URLConnection localURLConnection = localURL.openConnection();
        BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(localURLConnection.getInputStream()));
        String str2 = localBufferedReader.readLine();
        localBufferedReader.close();
        str2 = str2.substring(0, 7);
        String str3 = "5.06b04".substring(0, 7);
        int j = 0;
        String[] arrayOfString1 = str2.split("b");
        String[] arrayOfString2 = str3.split("b");
        if ((Double.parseDouble(arrayOfString1[0]) > Double.parseDouble(arrayOfString2[0])) || ((arrayOfString1[0].equals(arrayOfString2[0])) && (Integer.parseInt(arrayOfString1[1]) > Integer.parseInt(arrayOfString2[1]))))
          j = 1;
        if (j != 0)
        {
          UpdateDialog localUpdateDialog = new UpdateDialog(this.currentGUI.getFrame(), str3, str2);
          localUpdateDialog.setVisible(true);
          bool = true;
        }
        else if (paramBoolean)
        {
          this.currentGUI.showMessageDialog("The current version is up to date", "Up to date", 1);
        }
      }
    }
    catch (Exception localException)
    {
      i = 0;
    }
    finally
    {
      if ((i == 0) && (paramBoolean))
        this.currentGUI.showMessageDialog("Error making connection so unable to check for updates", "Error", 0);
    }
    return bool;
  }

  public void setPdfDecoder(PdfDecoder paramPdfDecoder)
  {
    this.decode_pdf = paramPdfDecoder;
  }

  public void setPageProperties(Object paramObject1, Object paramObject2)
  {
    if (this.multiViewListener != null)
      this.multiViewListener.setPageProperties(paramObject1, paramObject2);
  }

  public void clearRecentDocuments()
  {
    this.properties.removeRecentDocuments();
    for (int i = 0; i < this.noOfRecentDocs; i++)
    {
      this.recentDocuments[i].setText(new StringBuilder().append(i + 1).append(": ").toString());
      this.recentDocuments[i].setVisible(false);
    }
  }

  public SearchList getSearchList()
  {
    return this.searchFrame.getResults();
  }

  public MouseMode getMouseMode()
  {
    return this.mouseMode;
  }

  public void addAView(int paramInt, Rectangle paramRectangle, Integer paramInteger)
  {
    this.viewStack.add(paramInt, paramRectangle, paramInteger);
  }

  public void goBackAView()
  {
  }

  public void goForwardAView()
  {
  }

  public boolean isPDF()
  {
    return this.isPDf;
  }

  public static final boolean isPDFLinearized(String paramString)
    throws PdfException
  {
    if (paramString.startsWith("jar"))
      return false;
    boolean bool = false;
    try
    {
      URL localURL = new URL(paramString);
      InputStream localInputStream = localURL.openStream();
      byte[] arrayOfByte = new byte[''];
      localInputStream.read(arrayOfByte);
      localInputStream.close();
      int i = arrayOfByte.length;
      for (int j = 0; j < i; j++)
        if ((arrayOfByte[j] == 47) && (arrayOfByte[(j + 1)] == 76) && (arrayOfByte[(j + 2)] == 105) && (arrayOfByte[(j + 3)] == 110) && (arrayOfByte[(j + 4)] == 101) && (arrayOfByte[(j + 5)] == 97) && (arrayOfByte[(j + 6)] == 114))
        {
          bool = true;
          j = i;
        }
    }
    catch (IOException localIOException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog(new StringBuilder().append("[PDF] Exception ").append(localIOException).append(" scanning URL ").append(paramString).toString());
      localIOException.printStackTrace();
    }
    return bool;
  }

  public void scrollRectToHighlight(Rectangle paramRectangle, int paramInt)
  {
    int i = 0;
    int j = 0;
    int k = 0;
    int m = 0;
    int n = this.decode_pdf.getInsetW();
    int i1 = this.decode_pdf.getInsetH();
    float f = this.decode_pdf.getScaling();
    int i2 = this.decode_pdf.getScrollInterval();
    int i3 = this.decode_pdf.getDisplayView();
    if ((paramInt < 1) || (paramInt > this.decode_pdf.getPageCount()) || (i3 == 1))
      paramInt = this.decode_pdf.getPageNumber();
    PdfPageData localPdfPageData = this.decode_pdf.getPdfPageData();
    int i4 = localPdfPageData.getCropBoxWidth(paramInt);
    int i5 = localPdfPageData.getCropBoxHeight(paramInt);
    int i6 = localPdfPageData.getCropBoxX(paramInt);
    int i7 = localPdfPageData.getCropBoxY(paramInt);
    switch (this.decode_pdf.getDisplayRotation())
    {
    case 0:
      i = (int)((paramRectangle.x - i6) * f) + n;
      j = (int)((i5 - (paramRectangle.y - i7)) * f) + i1;
      k = (int)(paramRectangle.width * f);
      m = (int)(paramRectangle.height * f);
      break;
    case 90:
      i = (int)((paramRectangle.y - i7) * f) + i1;
      j = (int)((paramRectangle.x - i6) * f) + n;
      k = (int)(paramRectangle.height * f);
      m = (int)(paramRectangle.width * f);
      break;
    case 180:
      i = (int)((i4 - (paramRectangle.x - i6)) * f) + n;
      j = (int)((paramRectangle.y - i7) * f) + i1;
      k = (int)(paramRectangle.width * f);
      m = (int)(paramRectangle.height * f);
      break;
    case 270:
      i = (int)((i5 - (paramRectangle.y - i7)) * f) + i1;
      j = (int)((i4 - (paramRectangle.x - i6)) * f) + n;
      k = (int)(paramRectangle.height * f);
      m = (int)(paramRectangle.width * f);
    }
    if ((i3 != 1) && (i3 != 5))
    {
      i += this.decode_pdf.getPages().getXCordForPage(paramInt);
      j += this.decode_pdf.getPages().getYCordForPage(paramInt);
    }
    Rectangle localRectangle1 = this.decode_pdf.getVisibleRect();
    if (i > localRectangle1.x + localRectangle1.width / 2)
      i += localRectangle1.width / 2 - paramRectangle.width / 2;
    else
      i -= localRectangle1.width / 2 - paramRectangle.width / 2;
    if (j > localRectangle1.y + localRectangle1.height / 2)
      j += localRectangle1.height / 2 - paramRectangle.height / 2;
    else
      j -= localRectangle1.height / 2 - paramRectangle.height / 2;
    Rectangle localRectangle2 = new Rectangle(i - i2, j - i2, k + i2 * 2, m + i2 * 2);
    this.decode_pdf.scrollRectToVisible(localRectangle2);
  }

  protected class ViewStack
  {
    private ArrayList ourStack = new ArrayList();
    private int index = -1;
    private int length = 0;

    protected ViewStack()
    {
    }

    protected Viewable back()
    {
      if ((this.index - 1 > -1) && (this.index - 1 < this.length))
      {
        this.index -= 1;
        return (Viewable)this.ourStack.get(this.index);
      }
      return null;
    }

    protected Viewable forward()
    {
      if ((this.index + 1 > -1) && (this.index + 1 < this.length))
      {
        this.index += 1;
        return (Viewable)this.ourStack.get(this.index);
      }
      return null;
    }

    protected synchronized void add(int paramInt, Rectangle paramRectangle, Integer paramInteger)
    {
      this.ourStack.ensureCapacity(this.index + 2);
      this.ourStack.add(this.index + 1, new Viewable(paramInt, paramRectangle, paramInteger));
      this.index += 1;
      this.length = (this.index + 1);
    }

    protected class Viewable
    {
      private int page;
      private Rectangle location;
      private Integer type;

      protected Viewable(int paramRectangle, Rectangle paramInteger, Integer arg4)
      {
        this.page = paramRectangle;
        this.location = paramInteger;
        Object localObject;
        this.type = localObject;
      }

      protected Rectangle getLocation()
      {
        return this.location;
      }

      protected int getPage()
      {
        return this.page;
      }

      protected Integer getType()
      {
        return this.type;
      }
    }
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.Commands
 * JD-Core Version:    0.6.2
 */