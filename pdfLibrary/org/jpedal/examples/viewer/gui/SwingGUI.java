package org.jpedal.examples.viewer.gui;

import java.awt.Adjustable;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TimerTask;
import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.event.DocumentListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkEvent.EventType;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreeSelectionModel;
import org.jpedal.PdfDecoder;
import org.jpedal.display.Display;
import org.jpedal.display.Display.BoolValue;
import org.jpedal.display.PageOffsets;
import org.jpedal.display.swing.SingleDisplay;
import org.jpedal.examples.viewer.Commands;
import org.jpedal.examples.viewer.MouseMode;
import org.jpedal.examples.viewer.Values;
import org.jpedal.examples.viewer.gui.generic.GUIButton;
import org.jpedal.examples.viewer.gui.generic.GUICombo;
import org.jpedal.examples.viewer.gui.generic.GUIOutline;
import org.jpedal.examples.viewer.gui.generic.GUISearchWindow;
import org.jpedal.examples.viewer.gui.generic.GUIThumbnailPanel;
import org.jpedal.examples.viewer.gui.popups.PrintPanel;
import org.jpedal.examples.viewer.gui.popups.SwingProperties;
import org.jpedal.examples.viewer.gui.swing.CommandListener;
import org.jpedal.examples.viewer.gui.swing.FrameCloser;
import org.jpedal.examples.viewer.gui.swing.SearchList;
import org.jpedal.examples.viewer.gui.swing.SwingButton;
import org.jpedal.examples.viewer.gui.swing.SwingCheckBoxMenuItem;
import org.jpedal.examples.viewer.gui.swing.SwingCombo;
import org.jpedal.examples.viewer.gui.swing.SwingID;
import org.jpedal.examples.viewer.gui.swing.SwingMenuItem;
import org.jpedal.examples.viewer.gui.swing.SwingOutline;
import org.jpedal.examples.viewer.gui.swing.SwingSearchWindow;
import org.jpedal.examples.viewer.gui.swing.SwingThumbnailPanel;
import org.jpedal.examples.viewer.paper.PaperSizes;
import org.jpedal.examples.viewer.utils.Printer;
import org.jpedal.examples.viewer.utils.PropertiesFile;
import org.jpedal.external.CustomMessageHandler;
import org.jpedal.fonts.FontMappings;
import org.jpedal.fonts.StandardFonts;
import org.jpedal.gui.GUIFactory;
import org.jpedal.gui.ShowGUIMessage;
import org.jpedal.io.PdfObjectReader;
import org.jpedal.io.StatusBar;
import org.jpedal.linear.LinearThread;
import org.jpedal.objects.PdfFileInformation;
import org.jpedal.objects.PdfPageData;
import org.jpedal.objects.acroforms.AcroRenderer;
import org.jpedal.objects.acroforms.ReturnValues;
import org.jpedal.objects.acroforms.actions.ActionHandler;
import org.jpedal.objects.acroforms.creation.FormFactory;
import org.jpedal.objects.layers.PdfLayerList;
import org.jpedal.objects.outlines.OutlineData;
import org.jpedal.objects.raw.FormObject;
import org.jpedal.objects.raw.PdfArrayIterator;
import org.jpedal.objects.raw.PdfDictionary;
import org.jpedal.objects.raw.PdfObject;
import org.jpedal.parser.DecoderOptions;
import org.jpedal.parser.ImageCommands;
import org.jpedal.text.TextLines;
import org.jpedal.utils.BrowserLauncher;
import org.jpedal.utils.DPIFactory;
import org.jpedal.utils.JavaFXHelper;
import org.jpedal.utils.Messages;
import org.jpedal.utils.StringUtils;
import org.jpedal.utils.SwingWorker;
import org.jpedal.utils.repositories.Vector_Int;
import org.w3c.dom.Node;

public class SwingGUI extends GUI
  implements GUIFactory
{
  private boolean previewOnSingleScroll = true;
  private javax.swing.Timer memoryMonitor = null;
  private boolean debugThumbnail = false;
  ScrollListener scrollListener;
  ScrollMouseListener scrollMouseListener;
  JScrollBar thumbscroll = null;
  private boolean isMultiPageTiff = false;
  boolean finishedDecoding = false;
  static int startSize = 30;
  static int expandedSize = 190;
  boolean sideTabBarOpenByDefault = false;
  String startSelectedTab = "Pages";
  public boolean useNewLayout = true;
  public static String windowTitle;
  String pageTitle;
  String bookmarksTitle;
  String signaturesTitle;
  String layersTitle;
  boolean hasListener = false;
  private boolean isSetup = false;
  int lastTabSelected = -1;
  boolean tabsExpanded = false;
  public boolean messageShown = false;
  private Cursor grabCursor;
  private Cursor grabbingCursor;
  private Cursor panCursor;
  private Cursor panCursorL;
  private Cursor panCursorTL;
  private Cursor panCursorT;
  private Cursor panCursorTR;
  private Cursor panCursorR;
  private Cursor panCursorBR;
  private Cursor panCursorB;
  private Cursor panCursorBL;
  public static final int GRAB_CURSOR = 1;
  public static final int GRABBING_CURSOR = 2;
  public static final int DEFAULT_CURSOR = 3;
  public static final int PAN_CURSOR = 4;
  public static final int PAN_CURSORL = 5;
  public static final int PAN_CURSORTL = 6;
  public static final int PAN_CURSORT = 7;
  public static final int PAN_CURSORTR = 8;
  public static final int PAN_CURSORR = 9;
  public static final int PAN_CURSORBR = 10;
  public static final int PAN_CURSORB = 11;
  public static final int PAN_CURSORBL = 12;
  private PaperSizes paperSizes;
  private JPanel multibox = new JPanel();
  public static final int CURSOR = 1;
  ButtonGroup layoutGroup = new ButtonGroup();
  ButtonGroup searchLayoutGroup = new ButtonGroup();
  ButtonGroup borderGroup = new ButtonGroup();
  public int glowThickness = 11;
  public Color glowOuterColor = new Color(0.0F, 0.0F, 0.0F, 0.0F);
  public Color glowInnerColor = new Color(0.8F, 0.75F, 0.45F, 0.8F);
  private boolean pageTurnScalingAppropriate = true;
  private boolean dragLeft = false;
  private boolean dragTop = false;
  private CommandListener currentCommandListener;
  private JToolBar topButtons = new JToolBar();
  private JToolBar navButtons = new JToolBar();
  private JToolBar comboBoxBar = new JToolBar();
  private JMenuBar currentMenu = new JMenuBar();
  private boolean firstTimeFormMessage = true;
  private JLabel coords = new JLabel();
  private Container frame = new JFrame();
  private JDesktopPane desktopPane = new JDesktopPane();
  boolean isSingle = true;
  private JTabbedPane navOptionsPanel = new JTabbedPane();
  private JSplitPane displayPane;
  private JScrollPane scrollPane = new JScrollPane();
  private Font headFont = new Font("SansSerif", 1, 14);
  private Font textFont1 = new Font("SansSerif", 0, 12);
  private Font textFont = new Font("Serif", 0, 12);
  private StatusBar statusBar = new StatusBar(new Color(235, 154, 0));
  private StatusBar downloadBar = new StatusBar(new Color(185, 209, 0));
  private JLabel pageCounter1;
  CustomMessageHandler customMessageHandler = null;
  public JTextField pageCounter2 = new JTextField(4);
  private JLabel pageCounter3;
  private JLabel optimizationLabel;
  private JTree signaturesTree;
  private JPanel layersPanel = new JPanel();
  private String user_dir = System.getProperty("user.dir");
  private boolean tabsNotInitialised = true;
  private JToolBar navToolBar = new JToolBar();
  private JToolBar pagesToolBar = new JToolBar();
  public GUIButton nextSearch;
  public GUIButton previousSearch;
  PdfLayerList layersObject;
  private final JProgressBar memoryBar = new JProgressBar();
  JToolBar cursor = new JToolBar();
  private GUIButton openButton;
  private GUIButton printButton;
  private GUIButton searchButton;
  private GUIButton docPropButton;
  private GUIButton infoButton;
  public GUIButton mouseMode;
  private JMenu fileMenu;
  private JMenu openMenu;
  private JMenuItem open;
  private JMenuItem openUrl;
  private JMenuItem save;
  private JMenuItem reSaveAsForms;
  private JMenuItem find;
  private JMenuItem documentProperties;
  private JMenuItem signPDF;
  private JMenuItem print;
  private JMenuItem exit;
  private JMenu editMenu;
  private JMenuItem copy;
  private JMenuItem selectAll;
  private JMenuItem deselectAll;
  private JMenuItem preferences;
  private JMenu viewMenu;
  private JMenu goToMenu;
  private JMenuItem firstPage;
  private JMenuItem backPage;
  private JMenuItem forwardPage;
  private JMenuItem lastPage;
  private JMenuItem goTo;
  private JMenuItem previousDocument;
  private JMenuItem nextDocument;
  private JMenu pageLayoutMenu;
  private JMenuItem single;
  private JMenuItem continuous;
  private JMenuItem facing;
  private JMenuItem continuousFacing;
  private JMenuItem pageFlow;
  private JMenuItem textSelect;
  private JCheckBoxMenuItem separateCover;
  private JMenuItem panMode;
  private JMenuItem fullscreen;
  private JMenu windowMenu;
  private JMenuItem cascade;
  private JMenuItem tile;
  private JMenu exportMenu;
  private JMenu pdfMenu;
  private JMenuItem onePerPage;
  private JMenuItem nup;
  private JMenuItem handouts;
  private JMenu contentMenu;
  private JMenuItem images;
  private JMenuItem text;
  private JMenuItem bitmap;
  private JMenu pageToolsMenu;
  private JMenuItem rotatePages;
  private JMenuItem deletePages;
  private JMenuItem addPage;
  private JMenuItem addHeaderFooter;
  private JMenuItem stampText;
  private JMenuItem stampImage;
  private JMenuItem crop;
  private JMenu helpMenu;
  private JMenuItem visitWebsite;
  private JMenuItem tipOfTheDay;
  private JMenuItem checkUpdates;
  private JMenuItem about;
  private JMenuItem helpForum;
  private DefaultMutableTreeNode topLayer = new DefaultMutableTreeNode("Layers");
  JScrollPane fontScrollPane = new JScrollPane();
  boolean sortFontsByDir = true;
  GUISearchWindow searchFrame = null;
  boolean addSearchTab = false;
  boolean searchInMenu = false;
  JTextField searchText = null;
  SearchList results = null;
  public Commands currentCommands;
  JToggleButton options;
  JPopupMenu menu;
  private String iconLocation = "/org/jpedal/examples/viewer/res/";
  private boolean cursorOverPage = false;
  PrintPanel printPanel = null;
  private static final String flag = "11706076047632756";
  private static final Calendar cal = Calendar.getInstance();
  private static int dx = 0;

  public JScrollBar getThumbnailScrollBar()
  {
    return this.thumbscroll;
  }

  public static void setStartSize(int paramInt)
  {
    startSize = paramInt;
  }

  public SwingGUI(PdfDecoder paramPdfDecoder, Values paramValues, GUIThumbnailPanel paramGUIThumbnailPanel, PropertiesFile paramPropertiesFile)
  {
    this.decode_pdf = paramPdfDecoder;
    this.commonValues = paramValues;
    this.thumbnails = paramGUIThumbnailPanel;
    this.properties = paramPropertiesFile;
    paramPdfDecoder.addExternalHandler(this, 11);
    if (this.isSingle)
    {
      this.desktopPane.setBackground(this.frame.getBackground());
      this.desktopPane.setVisible(true);
      if ((this.frame instanceof JFrame))
        ((JFrame)this.frame).getContentPane().add(this.desktopPane, "Center");
      else
        this.frame.add(this.desktopPane, "Center");
    }
    int i = 1;
    int j = Integer.parseInt("11706076047632756".substring(3, 5));
    int k = Integer.parseInt("11706076047632756".substring(0, i + i));
    int m = Integer.parseInt("11706076047632756".substring(2, 3));
    int n = Integer.parseInt("11706076047632756".substring(5, 6));
    int i1 = Integer.parseInt("11706076047632756".substring(6, 7 * i));
    int i2 = Integer.parseInt("11706076047632756".substring(7, 8));
    int i3 = Integer.parseInt("11706076047632756".substring(8, 10));
    int i4 = Integer.parseInt("11706076047632756".substring(10, 11));
    int i5 = Integer.parseInt("11706076047632756".substring(11, 15));
    Calendar localCalendar = Calendar.getInstance();
    int i6 = i3 - i - i2;
    if (i6 < 0)
      i6 = 12 + i6;
    localCalendar.set(2, i6);
    localCalendar.set(1, 1000 * (i + i) + j + i1);
    int i7 = k - m;
    if (i7 < 1)
      i7 = 31 + i7;
    localCalendar.set(5, i7);
    long l = 86400000L;
    dx = (int)((Calendar.getInstance().getTime().getTime() - localCalendar.getTime().getTime()) / l);
    dx = 30 - dx;
  }

  public JComponent getDisplayPane()
  {
    return this.displayPane;
  }

  public JDesktopPane getMultiViewerFrames()
  {
    return this.desktopPane;
  }

  public void setPdfDecoder(PdfDecoder paramPdfDecoder)
  {
    this.decode_pdf = paramPdfDecoder;
  }

  public void closeMultiViewerWindow(String paramString)
  {
    JInternalFrame[] arrayOfJInternalFrame1 = this.desktopPane.getAllFrames();
    for (JInternalFrame localJInternalFrame : arrayOfJInternalFrame1)
      if (localJInternalFrame.getTitle().equals(paramString))
        try
        {
          localJInternalFrame.setClosed(true);
        }
        catch (PropertyVetoException localPropertyVetoException)
        {
        }
  }

  public int AdjustForAlignment(int paramInt)
  {
    if (this.decode_pdf.getPageAlignment() == 2)
    {
      int i = this.decode_pdf.getBounds().width;
      int j = this.decode_pdf.getPDFWidth();
      if (this.decode_pdf.getDisplayView() != 1)
        j = (int)this.decode_pdf.getMaximumSize().getWidth();
      if (i > j)
        paramInt -= (i - j) / 2;
    }
    return paramInt;
  }

  public String getBookmark(String paramString)
  {
    return this.tree.getPage(paramString);
  }

  public void reinitialiseTabs(boolean paramBoolean)
  {
    if (this.commonValues.getModeOfOperation() == 4)
      return;
    if (this.properties.getValue("ShowSidetabbar").toLowerCase().equals("true"))
    {
      if (!this.isSingle)
        return;
      if (!paramBoolean)
        if (this.sideTabBarOpenByDefault)
        {
          this.displayPane.setDividerLocation(expandedSize);
          this.tabsExpanded = true;
        }
        else
        {
          this.displayPane.setDividerLocation(startSize);
          this.tabsExpanded = false;
        }
      this.lastTabSelected = -1;
      if (!this.commonValues.isPDF())
      {
        this.navOptionsPanel.setVisible(false);
      }
      else
      {
        this.navOptionsPanel.setVisible(true);
        int i;
        int k;
        if (!this.decode_pdf.hasOutline())
        {
          i = -1;
          if (DecoderOptions.isRunningOnMac)
            for (k = 0; k < this.navOptionsPanel.getTabCount(); k++)
              if (this.navOptionsPanel.getTitleAt(k).equals(this.bookmarksTitle))
                i = k;
          else
            for (k = 0; k < this.navOptionsPanel.getTabCount(); k++)
              if (this.navOptionsPanel.getIconAt(k).toString().equals(this.bookmarksTitle))
                i = k;
          if (i != -1)
            this.navOptionsPanel.remove(i);
        }
        else if (this.properties.getValue("Bookmarkstab").toLowerCase().equals("true"))
        {
          i = -1;
          if (DecoderOptions.isRunningOnMac)
          {
            for (k = 0; k < this.navOptionsPanel.getTabCount(); k++)
              if (this.navOptionsPanel.getTitleAt(k).equals(this.bookmarksTitle))
                i = k;
            if (i == -1)
              this.navOptionsPanel.addTab(this.bookmarksTitle, (SwingOutline)this.tree);
          }
          else
          {
            for (k = 0; k < this.navOptionsPanel.getTabCount(); k++)
              if (this.navOptionsPanel.getIconAt(k).toString().equals(this.bookmarksTitle))
                i = k;
            if (i == -1)
            {
              localObject1 = new VTextIcon(this.navOptionsPanel, this.bookmarksTitle, 2);
              this.navOptionsPanel.addTab(null, (Icon)localObject1, (SwingOutline)this.tree);
            }
          }
        }
        AcroRenderer localAcroRenderer = this.decode_pdf.getFormRenderer();
        Object localObject1 = null;
        if (localAcroRenderer != null)
          localObject1 = localAcroRenderer.getSignatureObjects();
        DefaultMutableTreeNode localDefaultMutableTreeNode1;
        Object localObject2;
        Object localObject3;
        Object localObject4;
        Object localObject5;
        Object localObject6;
        Object localObject7;
        Object localObject8;
        if (localObject1 != null)
        {
          localDefaultMutableTreeNode1 = new DefaultMutableTreeNode("Signatures");
          localObject2 = new DefaultMutableTreeNode("The following have digitally counter-signed this document");
          localObject3 = new DefaultMutableTreeNode("The following signature fields are not signed");
          while (((Iterator)localObject1).hasNext())
          {
            localObject4 = (FormObject)((Iterator)localObject1).next();
            localObject5 = ((FormObject)localObject4).getDictionary(38);
            this.decode_pdf.getIO().checkResolved((PdfObject)localObject4);
            if (localObject5 == null)
            {
              if (!((DefaultMutableTreeNode)localObject3).isNodeChild(localDefaultMutableTreeNode1))
                localDefaultMutableTreeNode1.add((MutableTreeNode)localObject3);
              localObject6 = new DefaultMutableTreeNode(new StringBuilder().append(((FormObject)localObject4).getTextStreamValue(36)).append(" on page ").append(((FormObject)localObject4).getPageNumber()).toString());
              ((DefaultMutableTreeNode)localObject3).add((MutableTreeNode)localObject6);
            }
            else
            {
              if (!((DefaultMutableTreeNode)localObject2).isNodeChild(localDefaultMutableTreeNode1))
                localDefaultMutableTreeNode1.add((MutableTreeNode)localObject2);
              localObject6 = ((PdfObject)localObject5).getTextStreamValue(506543413);
              localObject7 = new DefaultMutableTreeNode(new StringBuilder().append("Signed by ").append((String)localObject6).toString());
              ((DefaultMutableTreeNode)localObject2).add((MutableTreeNode)localObject7);
              localObject8 = new DefaultMutableTreeNode("Type");
              ((DefaultMutableTreeNode)localObject7).add((MutableTreeNode)localObject8);
              String str1 = null;
              PdfArrayIterator localPdfArrayIterator = ((PdfObject)localObject5).getMixedArray(1011108731);
              if ((localPdfArrayIterator != null) && (localPdfArrayIterator.hasMoreTokens()))
                str1 = localPdfArrayIterator.getNextValueAsString(true);
              DefaultMutableTreeNode localDefaultMutableTreeNode2 = new DefaultMutableTreeNode(new StringBuilder().append("Filter: ").append(str1).toString());
              ((DefaultMutableTreeNode)localObject8).add(localDefaultMutableTreeNode2);
              String str2 = ((PdfObject)localObject5).getName(-2122953826);
              DefaultMutableTreeNode localDefaultMutableTreeNode3 = new DefaultMutableTreeNode(new StringBuilder().append("Sub Filter: ").append(str2).toString());
              ((DefaultMutableTreeNode)localObject8).add(localDefaultMutableTreeNode3);
              DefaultMutableTreeNode localDefaultMutableTreeNode4 = new DefaultMutableTreeNode("Details");
              ((DefaultMutableTreeNode)localObject7).add(localDefaultMutableTreeNode4);
              String str3 = ((PdfObject)localObject5).getTextStreamValue(29);
              StringBuilder localStringBuilder = new StringBuilder(str3);
              localStringBuilder.delete(0, 2);
              localStringBuilder.insert(4, '/');
              localStringBuilder.insert(7, '/');
              localStringBuilder.insert(10, ' ');
              localStringBuilder.insert(13, ':');
              localStringBuilder.insert(16, ':');
              localStringBuilder.insert(19, ' ');
              DefaultMutableTreeNode localDefaultMutableTreeNode5 = new DefaultMutableTreeNode(new StringBuilder().append("Time: ").append(localStringBuilder).toString());
              localDefaultMutableTreeNode4.add(localDefaultMutableTreeNode5);
              String str4 = ((PdfObject)localObject5).getTextStreamValue(826499443);
              DefaultMutableTreeNode localDefaultMutableTreeNode6 = new DefaultMutableTreeNode(new StringBuilder().append("Reason: ").append(str4).toString());
              localDefaultMutableTreeNode4.add(localDefaultMutableTreeNode6);
              String str5 = ((PdfObject)localObject5).getTextStreamValue(1618506351);
              DefaultMutableTreeNode localDefaultMutableTreeNode7 = new DefaultMutableTreeNode(new StringBuilder().append("Location: ").append(str5).toString());
              localDefaultMutableTreeNode4.add(localDefaultMutableTreeNode7);
              DefaultMutableTreeNode localDefaultMutableTreeNode8 = new DefaultMutableTreeNode(new StringBuilder().append("Field: ").append(((FormObject)localObject4).getTextStreamValue(36)).append(" on page ").append(((FormObject)localObject4).getPageNumber()).toString());
              localDefaultMutableTreeNode4.add(localDefaultMutableTreeNode8);
            }
          }
          if (this.signaturesTree == null)
          {
            this.signaturesTree = new JTree();
            localObject4 = new SignaturesTreeCellRenderer();
            this.signaturesTree.setCellRenderer((TreeCellRenderer)localObject4);
          }
          ((DefaultTreeModel)this.signaturesTree.getModel()).setRoot(localDefaultMutableTreeNode1);
          checkTabShown(this.signaturesTitle);
        }
        else
        {
          removeTab(this.signaturesTitle);
        }
        this.layersObject = ((PdfLayerList)this.decode_pdf.getJPedalObject(826881374));
        if ((this.layersObject != null) && (this.layersObject.getLayersCount() > 0))
        {
          this.layersPanel.removeAll();
          this.layersPanel.setLayout(new BorderLayout());
          checkTabShown(this.layersTitle);
          localDefaultMutableTreeNode1 = new DefaultMutableTreeNode("Info");
          localObject2 = this.layersObject.getMetaData();
          localObject3 = ((Map)localObject2).keySet().iterator();
          while (((Iterator)localObject3).hasNext())
          {
            localObject4 = ((Iterator)localObject3).next();
            localObject5 = ((Map)localObject2).get(localObject4);
            localDefaultMutableTreeNode1.add(new DefaultMutableTreeNode(new StringBuilder().append(localObject4).append("=").append(localObject5).toString()));
          }
          localObject6 = new JTree(localDefaultMutableTreeNode1);
          ((JTree)localObject6).setToolTipText("Double click to see any metadata");
          ((JTree)localObject6).setRootVisible(true);
          ((JTree)localObject6).collapseRow(0);
          this.layersPanel.add((Component)localObject6, "North");
          localObject7 = this.layersObject.getDisplayTree();
          if (localObject7 != null)
          {
            this.topLayer.removeAllChildren();
            localObject8 = new JTree(this.topLayer);
            ((JTree)localObject8).setName("LayersTree");
            ((JTree)localObject8).addTreeSelectionListener(new TreeSelectionListener()
            {
              public void valueChanged(TreeSelectionEvent paramAnonymousTreeSelectionEvent)
              {
                final DefaultMutableTreeNode localDefaultMutableTreeNode = (DefaultMutableTreeNode)this.val$layersTree.getLastSelectedPathComponent();
                if (localDefaultMutableTreeNode == null)
                  return;
                String str1 = (String)localDefaultMutableTreeNode.getUserObject();
                Object[] arrayOfObject = ((DefaultMutableTreeNode)localDefaultMutableTreeNode.getParent()).getUserObjectPath();
                int i = arrayOfObject.length;
                for (int j = i - 1; j > 0; j--)
                  str1 = str1 + PdfLayerList.deliminator + arrayOfObject[j].toString();
                final String str2 = str1;
                if ((SwingGUI.this.layersObject.isLayerName(str2)) && (!SwingGUI.this.layersObject.isLocked(str2)))
                {
                  Runnable local1 = new Runnable()
                  {
                    public void run()
                    {
                      SwingGUI.this.decode_pdf.setCursor(Cursor.getPredefinedCursor(3));
                      SwingGUI.this.decode_pdf.invalidate();
                      SwingGUI.this.decode_pdf.updateUI();
                      SwingGUI.this.decode_pdf.validate();
                      SwingGUI.this.scrollPane.invalidate();
                      SwingGUI.this.scrollPane.updateUI();
                      SwingGUI.this.scrollPane.validate();
                      CheckNode localCheckNode = (CheckNode)localDefaultMutableTreeNode;
                      if (!localCheckNode.isEnabled())
                      {
                        localCheckNode.setSelected(localCheckNode.isSelected());
                        ShowGUIMessage.showstaticGUIMessage(new StringBuffer("This layer has been disabled because its parent layer is disabled"), "Parent Layer disabled");
                      }
                      else
                      {
                        boolean bool = !localCheckNode.isSelected();
                        localCheckNode.setSelected(bool);
                        SwingGUI.this.layersObject.setVisiblity(str2, bool);
                        SwingGUI.this.syncTreeDisplay(SwingGUI.this.topLayer, true);
                        try
                        {
                          SwingGUI.this.decode_pdf.decodePage(SwingGUI.this.commonValues.getCurrentPage());
                        }
                        catch (Exception localException)
                        {
                          localException.printStackTrace();
                        }
                      }
                      SwingGUI.1.this.val$layersTree.invalidate();
                      SwingGUI.1.this.val$layersTree.clearSelection();
                      SwingGUI.1.this.val$layersTree.repaint();
                      SwingGUI.this.decode_pdf.setCursor(Cursor.getPredefinedCursor(0));
                    }
                  };
                  SwingUtilities.invokeLater(local1);
                }
              }
            });
            this.topLayer.removeAllChildren();
            addLayersToTree((Object[])localObject7, this.topLayer, true);
            ((JTree)localObject8).setRootVisible(true);
            ((JTree)localObject8).expandRow(0);
            ((JTree)localObject8).setCellRenderer(new CheckRenderer());
            ((JTree)localObject8).getSelectionModel().setSelectionMode(1);
            this.layersPanel.add((Component)localObject8, "Center");
          }
        }
        else
        {
          removeTab(this.layersTitle);
        }
        setBookmarks(false);
      }
      int j = this.displayPane.getDividerLocation();
      this.displayPane.setDividerLocation(0);
      this.displayPane.validate();
      this.displayPane.setDividerLocation(j);
      for (int m = 0; m != this.navOptionsPanel.getTabCount(); m++)
        if (DecoderOptions.isRunningOnMac)
        {
          if (this.navOptionsPanel.getTitleAt(m).equals(this.startSelectedTab))
          {
            this.navOptionsPanel.setSelectedIndex(m);
            break;
          }
        }
        else if (this.navOptionsPanel.getIconAt(m).toString().equals(this.startSelectedTab))
        {
          this.navOptionsPanel.setSelectedIndex(m);
          break;
        }
    }
  }

  private void syncTreeDisplay(DefaultMutableTreeNode paramDefaultMutableTreeNode, boolean paramBoolean)
  {
    int i = paramDefaultMutableTreeNode.getChildCount();
    boolean bool1 = paramBoolean;
    int j = 0;
    while (true)
    {
      paramBoolean = bool1;
      boolean bool2 = true;
      Object localObject1;
      if (i == 0)
        localObject1 = paramDefaultMutableTreeNode;
      else
        localObject1 = paramDefaultMutableTreeNode.getChildAt(j);
      Object localObject2;
      if ((localObject1 instanceof CheckNode))
      {
        localObject2 = (CheckNode)localObject1;
        String str = (String)((CheckNode)localObject2).getText();
        if (this.layersObject.isLayerName(str))
        {
          if (paramBoolean)
            paramBoolean = !this.layersObject.isLocked(str);
          bool2 = this.layersObject.isVisible(str);
          ((CheckNode)localObject2).setSelected(bool2);
          ((CheckNode)localObject2).setEnabled(paramBoolean);
        }
      }
      if (((TreeNode)localObject1).getChildCount() > 0)
      {
        localObject2 = ((TreeNode)localObject1).children();
        while (((Enumeration)localObject2).hasMoreElements())
          syncTreeDisplay((DefaultMutableTreeNode)((Enumeration)localObject2).nextElement(), (paramBoolean) && (bool2));
      }
      j++;
      if (j >= i)
        break;
    }
  }

  private void addLayersToTree(Object[] paramArrayOfObject, DefaultMutableTreeNode paramDefaultMutableTreeNode, boolean paramBoolean)
  {
    Object localObject1 = paramDefaultMutableTreeNode;
    boolean bool = paramBoolean;
    int i = 1;
    for (Object localObject2 : paramArrayOfObject)
    {
      Object localObject3;
      if ((localObject2 instanceof Object[]))
      {
        localObject3 = localObject1;
        addLayersToTree((Object[])localObject2, (DefaultMutableTreeNode)localObject1, (paramBoolean) && (i != 0));
        localObject1 = localObject3;
        paramBoolean = bool;
      }
      else
      {
        bool = paramBoolean;
        if (localObject2 != null)
        {
          String str;
          if ((localObject2 instanceof String))
            str = (String)localObject2;
          else
            str = new String((byte[])localObject2);
          localObject3 = str;
          int m = str.indexOf(PdfLayerList.deliminator);
          if (m != -1)
            localObject3 = ((String)localObject3).substring(0, m);
          if (!str.endsWith(" R"))
            if (!this.layersObject.isLayerName(str))
            {
              localObject1 = new DefaultMutableTreeNode(localObject3);
              paramDefaultMutableTreeNode.add((MutableTreeNode)localObject1);
              paramDefaultMutableTreeNode = (DefaultMutableTreeNode)localObject1;
              i = 1;
            }
            else if (paramDefaultMutableTreeNode != null)
            {
              localObject1 = new CheckNode(localObject3);
              paramDefaultMutableTreeNode.add((MutableTreeNode)localObject1);
              if (this.layersObject.isVisible(str))
              {
                ((CheckNode)localObject1).setSelected(true);
                i = 1;
              }
              else
              {
                i = 0;
              }
              if (paramBoolean)
                paramBoolean = !this.layersObject.isLocked(str);
              ((CheckNode)localObject1).setEnabled(paramBoolean);
            }
        }
      }
    }
  }

  private void checkTabShown(String paramString)
  {
    int i = -1;
    if (DecoderOptions.isRunningOnMac)
    {
      for (int j = 0; j < this.navOptionsPanel.getTabCount(); j++)
        if (this.navOptionsPanel.getTitleAt(j).equals(paramString))
          i = j;
      if (i == -1)
      {
        Object localObject;
        if ((paramString.equals(this.signaturesTitle)) && (this.properties.getValue("Signaturestab").toLowerCase().equals("true")))
        {
          if (this.signaturesTree == null)
          {
            this.signaturesTree = new JTree();
            localObject = new SignaturesTreeCellRenderer();
            this.signaturesTree.setCellRenderer((TreeCellRenderer)localObject);
          }
          this.navOptionsPanel.addTab(this.signaturesTitle, this.signaturesTree);
          this.navOptionsPanel.setTitleAt(this.navOptionsPanel.getTabCount() - 1, this.signaturesTitle);
        }
        else if ((paramString.equals(this.layersTitle)) && (this.properties.getValue("Layerstab").toLowerCase().equals("true")))
        {
          localObject = new JScrollPane();
          ((JScrollPane)localObject).getViewport().add(this.layersPanel);
          ((JScrollPane)localObject).setVerticalScrollBarPolicy(20);
          ((JScrollPane)localObject).setHorizontalScrollBarPolicy(30);
          this.navOptionsPanel.addTab(this.layersTitle, (Component)localObject);
          this.navOptionsPanel.setTitleAt(this.navOptionsPanel.getTabCount() - 1, this.layersTitle);
        }
      }
    }
    else
    {
      for (int k = 0; k < this.navOptionsPanel.getTabCount(); k++)
        if (this.navOptionsPanel.getIconAt(k).toString().equals(paramString))
          i = k;
      if (i == -1)
      {
        VTextIcon localVTextIcon;
        if ((paramString.equals(this.signaturesTitle)) && (this.properties.getValue("Signaturestab").toLowerCase().equals("true")))
        {
          localVTextIcon = new VTextIcon(this.navOptionsPanel, this.signaturesTitle, 2);
          this.navOptionsPanel.addTab(null, localVTextIcon, this.signaturesTree);
        }
        else if ((paramString.equals(this.layersTitle)) && (this.properties.getValue("Layerstab").toLowerCase().equals("true")))
        {
          localVTextIcon = new VTextIcon(this.navOptionsPanel, this.layersTitle, 2);
          JScrollPane localJScrollPane = new JScrollPane();
          localJScrollPane.getViewport().add(this.layersPanel);
          localJScrollPane.setVerticalScrollBarPolicy(20);
          localJScrollPane.setHorizontalScrollBarPolicy(30);
          this.navOptionsPanel.addTab(null, localVTextIcon, localJScrollPane);
        }
      }
    }
  }

  private void removeTab(String paramString)
  {
    int i = -1;
    int j;
    if (DecoderOptions.isRunningOnMac)
      for (j = 0; j < this.navOptionsPanel.getTabCount(); j++)
        if (this.navOptionsPanel.getTitleAt(j).equals(paramString))
          i = j;
    else
      for (j = 0; j < this.navOptionsPanel.getTabCount(); j++)
        if (this.navOptionsPanel.getIconAt(j).toString().equals(paramString))
          i = j;
    if (i != -1)
      this.navOptionsPanel.remove(i);
  }

  public void stopThumbnails()
  {
    if (!this.isSingle)
      return;
    if (this.thumbnails.isShownOnscreen())
    {
      this.thumbnails.terminateDrawing();
      this.thumbnails.removeAllListeners();
    }
  }

  public void reinitThumbnails()
  {
    this.isSetup = false;
  }

  public void resetNavBar()
  {
    if (!this.properties.getValue("consistentTabBar").toLowerCase().equals("true"))
    {
      if (!this.isSingle)
        return;
      this.displayPane.setDividerLocation(startSize);
      this.tabsNotInitialised = true;
      this.topLayer.removeAllChildren();
      setPageLayoutButtonsEnabled(false);
    }
  }

  public void setNoPagesDecoded()
  {
    this.bookmarksGenerated = false;
    resetNavBar();
    if (this.scrollListener != null)
      this.scrollListener.lastImage = null;
    this.pagesDecoded.clear();
  }

  public void setBackNavigationButtonsEnabled(boolean paramBoolean)
  {
    this.back.setEnabled(paramBoolean);
    this.first.setEnabled(paramBoolean);
    this.fback.setEnabled(paramBoolean);
  }

  public void setForwardNavigationButtonsEnabled(boolean paramBoolean)
  {
    this.forward.setEnabled(paramBoolean);
    this.end.setEnabled(paramBoolean);
    this.fforward.setEnabled(paramBoolean);
  }

  public void setPageLayoutButtonsEnabled(boolean paramBoolean)
  {
    if (!this.isSingle)
      return;
    this.continuousButton.setEnabled(paramBoolean);
    this.continuousFacingButton.setEnabled(paramBoolean);
    this.facingButton.setEnabled(paramBoolean);
    this.pageFlowButton.setEnabled(paramBoolean);
    Enumeration localEnumeration = this.layoutGroup.getElements();
    if (localEnumeration.hasMoreElements())
    {
      ((JMenuItem)localEnumeration.nextElement()).setEnabled(true);
      while (localEnumeration.hasMoreElements())
        ((JMenuItem)localEnumeration.nextElement()).setEnabled(paramBoolean);
    }
  }

  public void setSearchLayoutButtonsEnabled()
  {
    Enumeration localEnumeration = this.searchLayoutGroup.getElements();
    ((JMenuItem)localEnumeration.nextElement()).setEnabled(true);
    while (localEnumeration.hasMoreElements())
      ((JMenuItem)localEnumeration.nextElement()).setEnabled(true);
  }

  public void alignLayoutMenuOption(int paramInt)
  {
    int i = 1;
    Enumeration localEnumeration = this.layoutGroup.getElements();
    while ((localEnumeration.hasMoreElements()) && (i != paramInt))
    {
      localEnumeration.nextElement();
      i++;
    }
    ((JMenuItem)localEnumeration.nextElement()).setSelected(true);
  }

  public void setDisplayMode(Integer paramInteger)
  {
    if (paramInteger.equals(GUIFactory.MULTIPAGE))
      this.isSingle = false;
  }

  public boolean isSingle()
  {
    return this.isSingle;
  }

  public Object getThumbnailPanel()
  {
    return this.thumbnails;
  }

  public Object getOutlinePanel()
  {
    return this.tree;
  }

  public JScrollBar getVerticalScrollBar()
  {
    if (this.scrollPane.getVerticalScrollBar().isVisible())
      return this.scrollPane.getVerticalScrollBar();
    return this.thumbscroll;
  }

  public void initLayoutMenus(JMenu paramJMenu, String[] paramArrayOfString, int[] paramArrayOfInt)
  {
    int i = paramArrayOfInt.length;
    for (int j = 0; j < i; j++)
    {
      JCheckBoxMenuItem localJCheckBoxMenuItem = new JCheckBoxMenuItem(paramArrayOfString[j]);
      localJCheckBoxMenuItem.setBorder(BorderFactory.createEmptyBorder());
      this.layoutGroup.add(localJCheckBoxMenuItem);
      if (j == 0)
        localJCheckBoxMenuItem.setSelected(true);
      if (paramJMenu != null)
        switch (paramArrayOfInt[j])
        {
        case 1:
          this.single = localJCheckBoxMenuItem;
          this.single.addActionListener(new ActionListener()
          {
            public void actionPerformed(ActionEvent paramAnonymousActionEvent)
            {
              SwingGUI.this.currentCommands.executeCommand(57, null);
            }
          });
          paramJMenu.add(this.single);
          break;
        case 2:
          this.continuous = localJCheckBoxMenuItem;
          this.continuous.addActionListener(new ActionListener()
          {
            public void actionPerformed(ActionEvent paramAnonymousActionEvent)
            {
              SwingGUI.this.currentCommands.executeCommand(58, null);
            }
          });
          paramJMenu.add(this.continuous);
          break;
        case 3:
          this.facing = localJCheckBoxMenuItem;
          this.facing.addActionListener(new ActionListener()
          {
            public void actionPerformed(ActionEvent paramAnonymousActionEvent)
            {
              SwingGUI.this.currentCommands.executeCommand(60, null);
            }
          });
          paramJMenu.add(this.facing);
          break;
        case 4:
          this.continuousFacing = localJCheckBoxMenuItem;
          this.continuousFacing.addActionListener(new ActionListener()
          {
            public void actionPerformed(ActionEvent paramAnonymousActionEvent)
            {
              SwingGUI.this.currentCommands.executeCommand(59, null);
            }
          });
          paramJMenu.add(this.continuousFacing);
          break;
        case 5:
          this.pageFlow = localJCheckBoxMenuItem;
          this.pageFlow.addActionListener(new ActionListener()
          {
            public void actionPerformed(ActionEvent paramAnonymousActionEvent)
            {
              SwingGUI.this.currentCommands.executeCommand(62, null);
            }
          });
          paramJMenu.add(this.pageFlow);
        }
    }
    if (!this.isSingle)
      return;
    setPageLayoutButtonsEnabled(false);
  }

  private static JScrollPane getFontsAliasesInfoBox()
  {
    JPanel localJPanel = new JPanel();
    JScrollPane localJScrollPane = new JScrollPane();
    localJScrollPane.setPreferredSize(new Dimension(400, 300));
    localJScrollPane.getViewport().add(localJPanel);
    localJScrollPane.setVerticalScrollBarPolicy(20);
    localJScrollPane.setHorizontalScrollBarPolicy(30);
    localJPanel.setOpaque(true);
    localJPanel.setBackground(Color.white);
    localJPanel.setEnabled(false);
    localJPanel.setLayout(new BoxLayout(localJPanel, 3));
    StringBuilder localStringBuilder = new StringBuilder();
    Object localObject1 = FontMappings.fontSubstitutionAliasTable.keySet().iterator();
    Object localObject2;
    while (((Iterator)localObject1).hasNext())
    {
      localObject2 = ((Iterator)localObject1).next();
      localStringBuilder.append(localObject2);
      localStringBuilder.append(" ==> ");
      localStringBuilder.append(FontMappings.fontSubstitutionAliasTable.get(localObject2));
      localStringBuilder.append('\n');
    }
    localObject1 = localStringBuilder.toString();
    if (!((String)localObject1).isEmpty())
    {
      localObject2 = new JTextArea();
      ((JTextArea)localObject2).setLineWrap(false);
      ((JTextArea)localObject2).setText((String)localObject1);
      localJPanel.add((Component)localObject2);
      ((JTextArea)localObject2).setCaretPosition(0);
      ((JTextArea)localObject2).setOpaque(false);
      localJPanel.add(Box.createRigidArea(new Dimension(0, 5)));
    }
    return localJScrollPane;
  }

  private JPanel getFontsFoundInfoBox()
  {
    JPanel localJPanel1 = new JPanel(new BorderLayout());
    localJPanel1.setBackground(Color.WHITE);
    JPanel localJPanel2 = new JPanel(new BorderLayout());
    localJPanel2.setBackground(Color.WHITE);
    this.fontScrollPane.setBackground(Color.WHITE);
    this.fontScrollPane.getViewport().setBackground(Color.WHITE);
    this.fontScrollPane.setPreferredSize(new Dimension(400, 300));
    this.fontScrollPane.getViewport().add(localJPanel1);
    this.fontScrollPane.setVerticalScrollBarPolicy(20);
    this.fontScrollPane.setHorizontalScrollBarPolicy(30);
    JPanel localJPanel3 = new JPanel();
    localJPanel3.setBackground(Color.WHITE);
    JLabel localJLabel = new JLabel("Filter Font List");
    localJPanel3.add(localJLabel);
    ButtonGroup localButtonGroup = new ButtonGroup();
    JRadioButton localJRadioButton1 = new JRadioButton("Sort By Folder");
    localJRadioButton1.setBackground(Color.WHITE);
    JRadioButton localJRadioButton2 = new JRadioButton("Sort By Name");
    localJRadioButton2.setBackground(Color.WHITE);
    final JTextField localJTextField = new JTextField();
    if (this.sortFontsByDir)
      localJRadioButton1.setSelected(true);
    else
      localJRadioButton2.setSelected(true);
    localButtonGroup.add(localJRadioButton1);
    localButtonGroup.add(localJRadioButton2);
    JPanel localJPanel4 = new JPanel(new BorderLayout());
    localJPanel4.setBackground(Color.WHITE);
    localJPanel4.add(localJPanel3, "North");
    localJPanel4.add(localJRadioButton1, "West");
    localJPanel4.add(localJTextField, "Center");
    localJPanel4.add(localJRadioButton2, "East");
    localJRadioButton1.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        if (!SwingGUI.this.sortFontsByDir)
        {
          DefaultMutableTreeNode localDefaultMutableTreeNode = new DefaultMutableTreeNode("Fonts");
          SwingGUI.this.sortFontsByDir = true;
          localDefaultMutableTreeNode = SwingGUI.this.populateAvailableFonts(localDefaultMutableTreeNode, localJTextField.getText());
          SwingGUI.this.displayAvailableFonts(localDefaultMutableTreeNode);
        }
      }
    });
    localJRadioButton2.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        if (SwingGUI.this.sortFontsByDir)
        {
          DefaultMutableTreeNode localDefaultMutableTreeNode = new DefaultMutableTreeNode("Fonts");
          SwingGUI.this.sortFontsByDir = false;
          localDefaultMutableTreeNode = SwingGUI.this.populateAvailableFonts(localDefaultMutableTreeNode, localJTextField.getText());
          SwingGUI.this.displayAvailableFonts(localDefaultMutableTreeNode);
        }
      }
    });
    localJTextField.addKeyListener(new KeyListener()
    {
      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
      }

      public void keyReleased(KeyEvent paramAnonymousKeyEvent)
      {
        DefaultMutableTreeNode localDefaultMutableTreeNode = new DefaultMutableTreeNode("Fonts");
        SwingGUI.this.populateAvailableFonts(localDefaultMutableTreeNode, ((JTextField)paramAnonymousKeyEvent.getSource()).getText());
        SwingGUI.this.displayAvailableFonts(localDefaultMutableTreeNode);
      }

      public void keyTyped(KeyEvent paramAnonymousKeyEvent)
      {
      }
    });
    DefaultMutableTreeNode localDefaultMutableTreeNode = new DefaultMutableTreeNode("Fonts");
    localDefaultMutableTreeNode = populateAvailableFonts(localDefaultMutableTreeNode, null);
    JTree localJTree = new JTree(localDefaultMutableTreeNode);
    localJPanel1.add(localJTree, "West");
    localJPanel2.add(localJPanel4, "North");
    localJPanel2.add(this.fontScrollPane, "Center");
    localJPanel2.setPreferredSize(new Dimension(400, 300));
    return localJPanel2;
  }

  private void displayAvailableFonts(DefaultMutableTreeNode paramDefaultMutableTreeNode)
  {
    this.fontScrollPane.getViewport().removeAll();
    JPanel localJPanel = new JPanel(new BorderLayout());
    localJPanel.setBackground(Color.WHITE);
    localJPanel.add(new JTree(paramDefaultMutableTreeNode), "West");
    this.fontScrollPane.getViewport().add(localJPanel);
  }

  private DefaultMutableTreeNode populateAvailableFonts(DefaultMutableTreeNode paramDefaultMutableTreeNode, String paramString)
  {
    if (FontMappings.fontSubstitutionTable != null)
    {
      Set localSet = FontMappings.fontSubstitutionTable.keySet();
      Iterator localIterator1 = FontMappings.fontSubstitutionTable.keySet().iterator();
      int i = localSet.size();
      ArrayList localArrayList1 = new ArrayList(i);
      while (localIterator1.hasNext())
        localArrayList1.add(localIterator1.next().toString());
      Collections.sort(localArrayList1);
      Object localObject1;
      Object localObject2;
      Object localObject3;
      Object localObject5;
      if (this.sortFontsByDir)
      {
        ArrayList localArrayList2 = new ArrayList();
        localObject1 = new ArrayList();
        for (int k = 0; k < i; k++)
        {
          localObject2 = localArrayList1.get(k);
          localObject3 = (String)FontMappings.fontSubstitutionLocation.get(localObject2);
          int m = ((String)localObject3).lastIndexOf(System.getProperty("file.separator"));
          if ((m == -1) && (((String)localObject3).indexOf(47) != -1))
            m = ((String)localObject3).lastIndexOf(47);
          if (m != -1)
            localObject3 = ((String)localObject3).substring(0, m);
          if ((paramString == null) || (((String)localObject2).toLowerCase().contains(paramString.toLowerCase())))
          {
            if (!localArrayList2.contains(localObject3))
            {
              localArrayList2.add(localObject3);
              localObject5 = new DefaultMutableTreeNode(new DefaultMutableTreeNode(localObject3));
              paramDefaultMutableTreeNode.add((MutableTreeNode)localObject5);
              ((List)localObject1).add(localObject5);
            }
            localObject5 = new DefaultMutableTreeNode(new StringBuilder().append(localObject2).append(" = ").append(FontMappings.fontSubstitutionLocation.get(localObject2)).toString());
            int n = localArrayList2.indexOf(localObject3);
            ((DefaultMutableTreeNode)((List)localObject1).get(n)).add((MutableTreeNode)localObject5);
            String str = (String)FontMappings.fontPropertiesTable.get(new StringBuilder().append(localObject2).append("_path").toString());
            Integer localInteger = (Integer)FontMappings.fontPropertiesTable.get(new StringBuilder().append(localObject2).append("_type").toString());
            Map localMap = StandardFonts.getFontDetails(localInteger.intValue(), str);
            if (localMap != null)
            {
              Iterator localIterator2 = localMap.keySet().iterator();
              while (localIterator2.hasNext())
              {
                Object localObject6 = localIterator2.next();
                Object localObject7 = localMap.get(localObject6);
                DefaultMutableTreeNode localDefaultMutableTreeNode3 = new DefaultMutableTreeNode(new StringBuilder().append(localObject6).append(" = ").append(localObject7).toString());
                ((DefaultMutableTreeNode)localObject5).add(localDefaultMutableTreeNode3);
              }
            }
          }
        }
      }
      else
      {
        for (int j = 0; j < i; j++)
        {
          localObject1 = localArrayList1.get(j);
          if ((paramString == null) || (((String)localObject1).toLowerCase().contains(paramString.toLowerCase())))
          {
            DefaultMutableTreeNode localDefaultMutableTreeNode1 = new DefaultMutableTreeNode(new StringBuilder().append(localObject1).append(" = ").append(FontMappings.fontSubstitutionLocation.get(localObject1)).toString());
            paramDefaultMutableTreeNode.add(localDefaultMutableTreeNode1);
            localObject2 = (Map)FontMappings.fontPropertiesTable.get(localObject1);
            if (localObject2 != null)
            {
              localObject3 = ((Map)localObject2).keySet().iterator();
              while (((Iterator)localObject3).hasNext())
              {
                Object localObject4 = ((Iterator)localObject3).next();
                localObject5 = ((Map)localObject2).get(localObject4);
                DefaultMutableTreeNode localDefaultMutableTreeNode2 = new DefaultMutableTreeNode(new StringBuilder().append(localObject4).append(" = ").append(localObject5).toString());
                localDefaultMutableTreeNode1.add(localDefaultMutableTreeNode2);
              }
            }
          }
        }
      }
    }
    return paramDefaultMutableTreeNode;
  }

  private JScrollPane getFontInfoBox()
  {
    JPanel localJPanel = new JPanel();
    JScrollPane localJScrollPane = new JScrollPane();
    localJScrollPane.setPreferredSize(new Dimension(400, 300));
    localJScrollPane.getViewport().add(localJPanel);
    localJScrollPane.setVerticalScrollBarPolicy(20);
    localJScrollPane.setHorizontalScrollBarPolicy(30);
    localJPanel.setOpaque(true);
    localJPanel.setBackground(Color.white);
    localJPanel.setEnabled(false);
    localJPanel.setLayout(new BoxLayout(localJPanel, 3));
    String str1 = this.decode_pdf.getInfo(373243460);
    String str2 = "Font Substitution mode: ";
    switch (FontMappings.getFontSubstitutionMode())
    {
    case 1:
      str2 = new StringBuilder().append(str2).append("using file name").toString();
      break;
    case 2:
      str2 = new StringBuilder().append(str2).append("using PostScript name").toString();
      break;
    case 3:
      str2 = new StringBuilder().append(str2).append("using family name").toString();
      break;
    case 4:
      str2 = new StringBuilder().append(str2).append("using the full font name").toString();
      break;
    default:
      str2 = new StringBuilder().append(str2).append("Unknown FontSubstitutionMode").toString();
    }
    str2 = new StringBuilder().append(str2).append('\n').toString();
    if (!str1.isEmpty())
    {
      JTextArea localJTextArea = new JTextArea();
      JLabel localJLabel = new JLabel();
      localJLabel.setAlignmentX(0.5F);
      localJLabel.setText(str2);
      localJLabel.setForeground(Color.BLUE);
      localJTextArea.setLineWrap(false);
      localJTextArea.setForeground(Color.BLACK);
      localJTextArea.setText(new StringBuilder().append('\n').append(str1).toString());
      localJPanel.add(localJLabel);
      localJPanel.add(localJTextArea);
      localJTextArea.setCaretPosition(0);
      localJTextArea.setOpaque(false);
    }
    return localJScrollPane;
  }

  private JScrollPane getImageInfoBox()
  {
    JPanel localJPanel = new JPanel();
    JScrollPane localJScrollPane = new JScrollPane();
    localJScrollPane.setPreferredSize(new Dimension(400, 300));
    localJScrollPane.getViewport().add(localJPanel);
    localJScrollPane.setVerticalScrollBarPolicy(20);
    localJScrollPane.setHorizontalScrollBarPolicy(30);
    localJPanel.setOpaque(true);
    localJPanel.setBackground(Color.white);
    localJPanel.setEnabled(false);
    localJPanel.setLayout(new BoxLayout(localJPanel, 3));
    String str = this.decode_pdf.getInfo(1026635598);
    if (!str.isEmpty())
    {
      JTextArea localJTextArea = new JTextArea();
      localJTextArea.setLineWrap(false);
      localJTextArea.setForeground(Color.BLACK);
      localJTextArea.setText(new StringBuilder().append('\n').append(str).toString());
      localJPanel.add(localJTextArea);
      localJTextArea.setCaretPosition(0);
      localJTextArea.setOpaque(false);
    }
    return localJScrollPane;
  }

  public void getInfoBox()
  {
    final JPanel localJPanel = new JPanel();
    localJPanel.setPreferredSize(new Dimension(400, 260));
    localJPanel.setOpaque(false);
    localJPanel.setLayout(new BoxLayout(localJPanel, 1));
    JLabel localJLabel1 = new JLabel(Messages.getMessage("PdfViewerInfo.title"));
    localJLabel1.setOpaque(false);
    localJLabel1.setFont(this.headFont);
    localJLabel1.setAlignmentX(0.5F);
    localJPanel.add(localJLabel1);
    localJPanel.add(Box.createRigidArea(new Dimension(0, 15)));
    String str = Messages.getMessage("PdfViewerInfo1");
    if (!str.isEmpty())
    {
      localObject = new JTextArea();
      ((JTextArea)localObject).setFont(this.textFont1);
      ((JTextArea)localObject).setOpaque(false);
      ((JTextArea)localObject).setText(new StringBuilder().append(str).append("\n\nVersions\n JPedal: ").append("5.06b04").append("          ").append("Java: ").append(System.getProperty("java.version")).toString());
      ((JTextArea)localObject).setLineWrap(true);
      ((JTextArea)localObject).setWrapStyleWord(true);
      ((JTextArea)localObject).setEditable(false);
      localJPanel.add((Component)localObject);
      ((JTextArea)localObject).setAlignmentX(0.5F);
    }
    Object localObject = new ImageIcon(getClass().getResource("/org/jpedal/examples/viewer/res/logo.gif"));
    localJPanel.add(Box.createRigidArea(new Dimension(0, 25)));
    JLabel localJLabel2 = new JLabel((Icon)localObject);
    localJLabel2.setAlignmentX(0.5F);
    localJPanel.add(localJLabel2);
    final JLabel localJLabel3 = new JLabel(new StringBuilder().append("<html><center>").append(Messages.getMessage("PdfViewerJpedalLibrary.Text")).append(Messages.getMessage("PdfViewer.WebAddress")).toString());
    localJLabel3.setForeground(Color.blue);
    localJLabel3.setHorizontalAlignment(0);
    localJLabel3.setAlignmentX(0.5F);
    localJLabel3.addMouseListener(new MouseListener()
    {
      public void mouseEntered(MouseEvent paramAnonymousMouseEvent)
      {
        if (SingleDisplay.allowChangeCursor)
          localJPanel.setCursor(new Cursor(12));
        localJLabel3.setText("<html><center>" + Messages.getMessage("PdfViewerJpedalLibrary.Link") + Messages.getMessage("PdfViewerJpedalLibrary.Text") + Messages.getMessage("PdfViewer.WebAddress") + "</a></center>");
      }

      public void mouseExited(MouseEvent paramAnonymousMouseEvent)
      {
        if (SingleDisplay.allowChangeCursor)
          localJPanel.setCursor(new Cursor(0));
        localJLabel3.setText("<html><center>" + Messages.getMessage("PdfViewerJpedalLibrary.Text") + Messages.getMessage("PdfViewer.WebAddress"));
      }

      public void mouseClicked(MouseEvent paramAnonymousMouseEvent)
      {
        try
        {
          BrowserLauncher.openURL(Messages.getMessage("PdfViewer.VisitWebsite"));
        }
        catch (IOException localIOException)
        {
          SwingGUI.this.showMessageDialog(Messages.getMessage("PdfViewer.ErrorWebsite"));
        }
      }

      public void mousePressed(MouseEvent paramAnonymousMouseEvent)
      {
      }

      public void mouseReleased(MouseEvent paramAnonymousMouseEvent)
      {
      }
    });
    localJPanel.add(localJLabel3);
    localJPanel.add(Box.createRigidArea(new Dimension(0, 5)));
    localJPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    showMessageDialog(localJPanel, Messages.getMessage("PdfViewerInfo3"), -1);
  }

  public void resetRotationBox()
  {
    PdfPageData localPdfPageData = this.decode_pdf.getPdfPageData();
    if (this.decode_pdf.getDisplayView() == 1)
      this.rotation = localPdfPageData.getRotation(this.commonValues.getCurrentPage());
    if (getSelectedComboIndex(251) != this.rotation / 90)
      setSelectedComboIndex(251, this.rotation / 90);
    else if (!Values.isProcessing())
      this.decode_pdf.repaint();
  }

  private JScrollPane getPropertiesBox(String paramString1, String paramString2, String paramString3, long paramLong, int paramInt1, int paramInt2)
  {
    PdfFileInformation localPdfFileInformation = this.decode_pdf.getFileInformationData();
    if (localPdfFileInformation != null)
    {
      JPanel localJPanel = new JPanel();
      localJPanel.setOpaque(true);
      localJPanel.setBackground(Color.white);
      localJPanel.setLayout(new BoxLayout(localJPanel, 1));
      JScrollPane localJScrollPane = new JScrollPane();
      localJScrollPane.setPreferredSize(new Dimension(400, 300));
      localJScrollPane.getViewport().add(localJPanel);
      localJScrollPane.setVerticalScrollBarPolicy(20);
      localJScrollPane.setHorizontalScrollBarPolicy(30);
      JLabel localJLabel1 = new JLabel(Messages.getMessage("PdfViewerGeneral"));
      localJLabel1.setFont(this.headFont);
      localJLabel1.setOpaque(false);
      localJPanel.add(localJLabel1);
      JLabel localJLabel2 = new JLabel(new StringBuilder().append(Messages.getMessage("PdfViewerFileName")).append(paramString1).toString());
      localJLabel2.setFont(this.textFont);
      localJLabel2.setOpaque(false);
      localJPanel.add(localJLabel2);
      JLabel localJLabel3 = new JLabel(new StringBuilder().append(Messages.getMessage("PdfViewerFilePath")).append(paramString2).toString());
      localJLabel3.setFont(this.textFont);
      localJLabel3.setOpaque(false);
      localJPanel.add(localJLabel3);
      JLabel localJLabel4 = new JLabel(new StringBuilder().append(Messages.getMessage("PdfViewerCurrentWorkingDir")).append(' ').append(paramString3).toString());
      localJLabel4.setFont(this.textFont);
      localJLabel4.setOpaque(false);
      localJPanel.add(localJLabel4);
      JLabel localJLabel5 = new JLabel(new StringBuilder().append(Messages.getMessage("PdfViewerFileSize")).append(paramLong).append(" K").toString());
      localJLabel5.setFont(this.textFont);
      localJLabel5.setOpaque(false);
      localJPanel.add(localJLabel5);
      JLabel localJLabel6 = new JLabel(new StringBuilder().append(Messages.getMessage("PdfViewerPageCount")).append(paramInt1).toString());
      localJLabel6.setOpaque(false);
      localJLabel6.setFont(this.textFont);
      localJPanel.add(localJLabel6);
      String str = new StringBuilder().append("PDF ").append(this.decode_pdf.getPDFVersion()).toString();
      if (this.decode_pdf.getJPedalObject(2004845231) != null)
        str = new StringBuilder().append(str).append(" (").append(Messages.getMessage("PdfViewerLinearized.text")).append(") ").toString();
      JLabel localJLabel7 = new JLabel(str);
      localJLabel7.setOpaque(false);
      localJLabel7.setFont(this.textFont);
      localJPanel.add(localJLabel7);
      localJPanel.add(Box.createVerticalStrut(10));
      JLabel localJLabel8 = new JLabel(Messages.getMessage("PdfViewerProperties"));
      localJLabel8.setFont(this.headFont);
      localJLabel8.setOpaque(false);
      localJPanel.add(localJLabel8);
      String[] arrayOfString1 = localPdfFileInformation.getFieldValues();
      String[] arrayOfString2 = PdfFileInformation.getFieldNames();
      int i = arrayOfString2.length;
      JLabel[] arrayOfJLabel = new JLabel[i];
      for (int j = 0; j < i; j++)
        if (!arrayOfString1[j].isEmpty())
        {
          arrayOfJLabel[j] = new JLabel(new StringBuilder().append(arrayOfString2[j]).append(" = ").append(arrayOfString1[j]).toString());
          arrayOfJLabel[j].setFont(this.textFont);
          arrayOfJLabel[j].setOpaque(false);
          localJPanel.add(arrayOfJLabel[j]);
        }
      localJPanel.add(Box.createVerticalStrut(10));
      PdfPageData localPdfPageData = this.decode_pdf.getPdfPageData();
      if (localPdfPageData != null)
      {
        JLabel localJLabel9 = new JLabel(Messages.getMessage("PdfViewerCoords.text"));
        localJLabel9.setFont(this.headFont);
        localJPanel.add(localJLabel9);
        JLabel localJLabel10 = new JLabel(new StringBuilder().append(Messages.getMessage("PdfViewermediaBox.text")).append(localPdfPageData.getMediaValue(paramInt2)).toString());
        localJLabel10.setFont(this.textFont);
        localJPanel.add(localJLabel10);
        JLabel localJLabel11 = new JLabel(new StringBuilder().append(Messages.getMessage("PdfViewercropBox.text")).append(localPdfPageData.getCropValue(paramInt2)).toString());
        localJLabel11.setFont(this.textFont);
        localJPanel.add(localJLabel11);
        JLabel localJLabel12 = new JLabel(new StringBuilder().append(Messages.getMessage("PdfViewerLabel.Rotation")).append(localPdfPageData.getRotation(paramInt2)).toString());
        localJLabel12.setFont(this.textFont);
        localJPanel.add(localJLabel12);
      }
      localJPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
      return localJScrollPane;
    }
    return new JScrollPane();
  }

  private static JScrollPane getXMLInfoBox(String paramString)
  {
    JPanel localJPanel = new JPanel();
    localJPanel.setLayout(new BoxLayout(localJPanel, 3));
    localJPanel.setOpaque(true);
    localJPanel.setBackground(Color.white);
    JScrollPane localJScrollPane = new JScrollPane();
    localJScrollPane.setPreferredSize(new Dimension(400, 300));
    localJScrollPane.getViewport().add(localJPanel);
    localJScrollPane.setVerticalScrollBarPolicy(20);
    localJScrollPane.setHorizontalScrollBarPolicy(30);
    JTextArea localJTextArea = new JTextArea();
    localJTextArea.setRows(5);
    localJTextArea.setColumns(15);
    localJTextArea.setLineWrap(true);
    localJTextArea.setText(paramString);
    localJPanel.add(new JScrollPane(localJTextArea));
    localJTextArea.setCaretPosition(0);
    localJTextArea.setOpaque(true);
    localJTextArea.setBackground(Color.white);
    return localJScrollPane;
  }

  public void showDocumentProperties(String paramString, long paramLong, int paramInt1, int paramInt2)
  {
    JTabbedPane localJTabbedPane = new JTabbedPane();
    localJTabbedPane.setBackground(Color.WHITE);
    if (paramString == null)
    {
      showMessageDialog(Messages.getMessage("PdfVieweremptyFile.message"), Messages.getMessage("PdfViewerTooltip.pageSize"), -1);
    }
    else
    {
      String str1 = paramString;
      int i = str1.lastIndexOf(92);
      if (i == -1)
        i = str1.lastIndexOf(47);
      String str2 = str1.substring(i + 1, str1.length());
      String str3 = str1.substring(0, i + 1);
      int j = 0;
      localJTabbedPane.add(getPropertiesBox(str2, str3, this.user_dir, paramLong, paramInt1, paramInt2));
      localJTabbedPane.setTitleAt(j++, Messages.getMessage("PdfViewerTab.Properties"));
      localJTabbedPane.add(getFontInfoBox());
      localJTabbedPane.setTitleAt(j++, Messages.getMessage("PdfViewerTab.Fonts"));
      if (ImageCommands.trackImages)
      {
        localJTabbedPane.add(getImageInfoBox());
        localJTabbedPane.setTitleAt(j++, Messages.getMessage("PdfViewerTab.Images"));
      }
      localJTabbedPane.add(getFontsFoundInfoBox());
      localJTabbedPane.setTitleAt(j++, "Available");
      localJTabbedPane.add(getFontsAliasesInfoBox());
      localJTabbedPane.setTitleAt(j++, "Aliases");
      int k = j;
      JScrollPane localJScrollPane = getFormList();
      if (localJScrollPane != null)
      {
        localJTabbedPane.add(localJScrollPane);
        localJTabbedPane.setTitleAt(k, "Forms");
        k++;
      }
      PdfFileInformation localPdfFileInformation = this.decode_pdf.getFileInformationData();
      String str4 = localPdfFileInformation.getFileXMLMetaData();
      if (!str4.isEmpty())
      {
        localJTabbedPane.add(getXMLInfoBox(str4));
        localJTabbedPane.setTitleAt(k, "XML");
      }
      showMessageDialog(localJTabbedPane, Messages.getMessage("PdfViewerTab.DocumentProperties"), -1);
    }
  }

  private JScrollPane getFormList()
  {
    JScrollPane localJScrollPane = null;
    AcroRenderer localAcroRenderer1 = this.decode_pdf.getFormRenderer();
    if (localAcroRenderer1 != null)
    {
      Object[] arrayOfObject1 = localAcroRenderer1.getFormComponents(null, ReturnValues.FORM_NAMES, this.commonValues.getCurrentPage());
      if (arrayOfObject1 != null)
      {
        int i = arrayOfObject1.length;
        JPanel localJPanel = new JPanel();
        localJScrollPane = new JScrollPane();
        localJScrollPane.setPreferredSize(new Dimension(400, 300));
        localJScrollPane.getViewport().add(localJPanel);
        localJScrollPane.setVerticalScrollBarPolicy(20);
        localJScrollPane.setHorizontalScrollBarPolicy(30);
        localJPanel.setLayout(new BoxLayout(localJPanel, 1));
        JLabel localJLabel1 = new JLabel(new StringBuilder().append("This page contains ").append(i).append(" form objects").toString());
        localJLabel1.setFont(this.headFont);
        localJPanel.add(localJLabel1);
        localJPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        AcroRenderer localAcroRenderer2 = this.decode_pdf.getFormRenderer();
        if (localAcroRenderer2 == null)
          return localJScrollPane;
        for (Object localObject1 : arrayOfObject1)
        {
          String str1 = (String)localObject1;
          Object[] arrayOfObject3 = localAcroRenderer1.getFormComponents(str1, ReturnValues.GUI_FORMS_FROM_NAME, -1);
          if (arrayOfObject3 != null)
          {
            FormObject localFormObject = null;
            String str2 = "PDF ref=";
            JLabel localJLabel2 = new JLabel();
            Object[] arrayOfObject4 = localAcroRenderer1.getFormComponents(str1, ReturnValues.FORMOBJECTS_FROM_NAME, -1);
            for (str3 : arrayOfObject4)
            {
              localFormObject = (FormObject)str3;
              str2 = new StringBuilder().append(str2).append(' ').append(localFormObject.getObjectRefAsString()).toString();
            }
            localJLabel2.setText(str2);
            ??? = str1;
            JLabel localJLabel3 = new JLabel((String)???);
            JLabel localJLabel4 = new JLabel();
            localJLabel4.setText(new StringBuilder().append("Type=").append(PdfDictionary.showAsConstant(localFormObject.getParameterConstant(608780341))).append(" Subtype=").append(PdfDictionary.showAsConstant(localFormObject.getParameterConstant(1147962727))).toString());
            String str3 = new StringBuilder().append("java class=").append(arrayOfObject3[0].getClass()).toString();
            JLabel localJLabel5 = new JLabel(str3);
            localJLabel3.setFont(this.headFont);
            localJLabel3.setForeground(Color.blue);
            localJLabel4.setFont(this.textFont);
            localJLabel4.setForeground(Color.blue);
            localJLabel5.setFont(this.textFont);
            localJLabel5.setForeground(Color.blue);
            localJLabel2.setFont(this.textFont);
            localJLabel2.setForeground(Color.blue);
            localJPanel.add(localJLabel3);
            localJPanel.add(localJLabel4);
            localJPanel.add(localJLabel5);
            localJPanel.add(localJLabel2);
          }
        }
      }
    }
    return localJScrollPane;
  }

  public void searchInTab(GUISearchWindow paramGUISearchWindow)
  {
    this.searchFrame = paramGUISearchWindow;
    this.searchFrame.init(this.decode_pdf, this.commonValues);
    if (DecoderOptions.isRunningOnMac)
    {
      if (this.thumbnails.isShownOnscreen())
        this.navOptionsPanel.addTab("Search", paramGUISearchWindow.getContentPanel());
    }
    else
    {
      VTextIcon localVTextIcon = new VTextIcon(this.navOptionsPanel, "Search", 2);
      this.navOptionsPanel.addTab(null, localVTextIcon, paramGUISearchWindow.getContentPanel());
    }
    this.addSearchTab = true;
  }

  private JToggleButton createMenuBarSearchOptions()
  {
    if (this.options == null)
    {
      this.options = new JToggleButton(new ImageIcon(getURLForImage(new StringBuilder().append(this.iconLocation).append("menuSearchOptions.png").toString())));
      this.menu = new JPopupMenu();
      this.options.addItemListener(new ItemListener()
      {
        public void itemStateChanged(ItemEvent paramAnonymousItemEvent)
        {
          if (paramAnonymousItemEvent.getStateChange() == 1)
            SwingGUI.this.menu.show((JComponent)paramAnonymousItemEvent.getSource(), 0, ((JComponent)paramAnonymousItemEvent.getSource()).getHeight());
        }
      });
      this.options.setFocusable(false);
      this.options.setToolTipText(Messages.getMessage("PdfViewerSearch.Options"));
      JCheckBoxMenuItem localJCheckBoxMenuItem1 = new JCheckBoxMenuItem(Messages.getMessage("PdfViewerSearch.WholeWords"));
      localJCheckBoxMenuItem1.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent paramAnonymousActionEvent)
        {
          SwingGUI.this.searchFrame.setWholeWords(((JCheckBoxMenuItem)paramAnonymousActionEvent.getSource()).isSelected());
        }
      });
      JCheckBoxMenuItem localJCheckBoxMenuItem2 = new JCheckBoxMenuItem(Messages.getMessage("PdfViewerSearch.CaseSense"));
      localJCheckBoxMenuItem2.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent paramAnonymousActionEvent)
        {
          SwingGUI.this.searchFrame.setCaseSensitive(((JCheckBoxMenuItem)paramAnonymousActionEvent.getSource()).isSelected());
        }
      });
      JCheckBoxMenuItem localJCheckBoxMenuItem3 = new JCheckBoxMenuItem(Messages.getMessage("PdfViewerSearch.MultiLine"));
      localJCheckBoxMenuItem3.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent paramAnonymousActionEvent)
        {
          SwingGUI.this.searchFrame.setMultiLine(((JCheckBoxMenuItem)paramAnonymousActionEvent.getSource()).isSelected());
        }
      });
      this.menu.add(localJCheckBoxMenuItem1);
      this.menu.add(localJCheckBoxMenuItem2);
      this.menu.add(localJCheckBoxMenuItem3);
      this.menu.addPopupMenuListener(new PopupMenuListener()
      {
        public void popupMenuWillBecomeVisible(PopupMenuEvent paramAnonymousPopupMenuEvent)
        {
        }

        public void popupMenuWillBecomeInvisible(PopupMenuEvent paramAnonymousPopupMenuEvent)
        {
          SwingGUI.this.options.setSelected(false);
        }

        public void popupMenuCanceled(PopupMenuEvent paramAnonymousPopupMenuEvent)
        {
          SwingGUI.this.options.setSelected(false);
        }
      });
    }
    return this.options;
  }

  public void searchInMenu(GUISearchWindow paramGUISearchWindow)
  {
    this.searchFrame = paramGUISearchWindow;
    this.searchInMenu = true;
    paramGUISearchWindow.find(this.decode_pdf, this.commonValues);
    this.topButtons.add(this.searchText);
    this.topButtons.add(createMenuBarSearchOptions());
    addButton(0, Messages.getMessage("PdfViewerSearch.Previous"), new StringBuilder().append(this.iconLocation).append("search_previous.gif").toString(), 18);
    addButton(0, Messages.getMessage("PdfViewerSearch.Next"), new StringBuilder().append(this.iconLocation).append("search_next.gif").toString(), 19);
    this.nextSearch.setEnabled(false);
    this.previousSearch.setEnabled(false);
    this.nextSearch.setVisible(true);
    this.previousSearch.setVisible(true);
  }

  public Commands getCommand()
  {
    return this.currentCommands;
  }

  public void clearRecentDocuments()
  {
    this.currentCommands.clearRecentDocuments();
  }

  public void addPageChangeListener(DocumentListener paramDocumentListener)
  {
    if (this.pageCounter2 != null)
      this.pageCounter2.getDocument().addDocumentListener(paramDocumentListener);
  }

  public void init(String[] paramArrayOfString, final Object paramObject1, Object paramObject2)
  {
    this.customMessageHandler = ((CustomMessageHandler)this.decode_pdf.getExternalHandler(15));
    if (this.customMessageHandler != null)
    {
      DecoderOptions.showErrorMessages = false;
      org.jpedal.examples.viewer.Viewer.showMessages = false;
    }
    Object localObject2;
    try
    {
      String str1 = this.properties.getValue("pageInsets");
      if (!str1.isEmpty())
        inset = Integer.parseInt(str1);
      str1 = this.properties.getValue("useHinting");
      localObject2 = System.getProperty("org.jpedal.useTTFontHinting");
      if (localObject2 != null)
      {
        if ((!str1.isEmpty()) && (!((String)localObject2).toLowerCase().equals(str1.toLowerCase())))
          JOptionPane.showMessageDialog(null, Messages.getMessage("PdfCustomGui.hintingFlagFileConflict"));
        org.jpedal.fonts.tt.TTGlyph.useHinting = ((String)localObject2).toLowerCase().equals("true");
      }
      else
      {
        org.jpedal.fonts.tt.TTGlyph.useHinting = (!str1.isEmpty()) && (str1.toLowerCase().equals("true"));
      }
      str1 = this.properties.getValue("changeTextAndLineart");
      if ((!str1.isEmpty()) && (str1.toLowerCase().equals("true")))
        ((Commands)paramObject1).executeCommand(713, new Object[] { Boolean.valueOf(Boolean.parseBoolean(str1)) });
      str1 = this.properties.getValue("vbgColor");
      if (!str1.isEmpty())
        ((Commands)paramObject1).executeCommand(709, new Object[] { Integer.valueOf(Integer.parseInt(str1)) });
      str1 = this.properties.getValue("sbbgColor");
      if (!str1.isEmpty());
      str1 = this.properties.getValue("replaceDocumentTextColors");
      if ((!str1.isEmpty()) && (str1.toLowerCase().equals("true")))
      {
        str1 = this.properties.getValue("vfgColor");
        if (!str1.isEmpty())
          ((Commands)paramObject1).executeCommand(712, new Object[] { Integer.valueOf(Integer.parseInt(str1)) });
      }
      str1 = this.properties.getValue("TextColorThreshold");
      if (!str1.isEmpty())
        ((Commands)paramObject1).executeCommand(715, new Object[] { Integer.valueOf(Integer.parseInt(str1)) });
      str1 = this.properties.getValue("iconLocation");
      if (!str1.isEmpty())
        this.iconLocation = str1;
      str1 = this.properties.getValue("borderType");
      if (!str1.isEmpty())
        SingleDisplay.CURRENT_BORDER_STYLE = Integer.parseInt(str1);
      str1 = this.properties.getValue("autoScroll");
      if (!str1.isEmpty())
        this.allowScrolling = Boolean.getBoolean(str1);
      str1 = this.properties.getValue("confirmClose");
      if (!str1.isEmpty())
        this.confirmClose = str1.equals("true");
      str1 = this.properties.getValue("resolution");
      if (!str1.isEmpty())
        this.decode_pdf.getDPIFactory().setDpi(Integer.parseInt(str1));
      str1 = this.properties.getValue("allowCursorToChange");
      if (!str1.isEmpty())
        SingleDisplay.allowChangeCursor = str1.toLowerCase().equals("true");
      str1 = this.properties.getValue("startView");
      if (!str1.isEmpty())
      {
        int i = Integer.parseInt(str1);
        if ((i < 1) || (i > 5))
          i = 1;
        this.decode_pdf.setPageMode(i);
      }
      str1 = this.properties.getValue("maxmuliviewers");
      if (!str1.isEmpty())
        this.commonValues.setMaxMiltiViewers(Integer.parseInt(str1));
      str1 = this.properties.getValue("useHiResPrinting");
      if (!str1.isEmpty())
        this.hiResPrinting = Boolean.valueOf(str1).booleanValue();
      str2 = this.properties.getValue("highlightBoxColor");
      if (!str2.isEmpty())
        DecoderOptions.highlightColor = new Color(Integer.parseInt(str2));
      str1 = this.properties.getValue("highlightTextColor");
      if (!str1.isEmpty())
        DecoderOptions.backgroundColor = new Color(Integer.parseInt(str1));
      str1 = this.properties.getValue("invertHighlights");
      if (!str1.isEmpty())
        org.jpedal.render.SwingDisplay.invertHighlight = Boolean.valueOf(str1).booleanValue();
      str1 = this.properties.getValue("showMouseSelectionBox");
      if (!str1.isEmpty())
        DecoderOptions.showMouseBox = Boolean.valueOf(str1).booleanValue();
      str1 = this.properties.getValue("enhancedViewerMode");
      if (!str1.isEmpty())
        this.decode_pdf.useNewGraphicsMode = Boolean.valueOf(str1).booleanValue();
      str1 = this.properties.getValue("enhancedFacingMode");
      if (!str1.isEmpty())
        SingleDisplay.default_turnoverOn = Boolean.valueOf(str1).booleanValue();
      str1 = this.properties.getValue("enhancedGUI");
      if (!str1.isEmpty())
        this.useNewLayout = Boolean.valueOf(str1).booleanValue();
      str1 = this.properties.getValue("highlightComposite");
      if (!str1.isEmpty())
      {
        float f = Float.parseFloat(str1);
        if (f > 1.0F)
          f = 1.0F;
        if (f < 0.0F)
          f = 0.0F;
        DecoderOptions.highlightComposite = f;
      }
      str1 = this.properties.getValue("windowTitle");
      if (!str1.isEmpty())
        windowTitle = str1;
      else
        windowTitle = new StringBuilder().append(Messages.getMessage("PdfViewer.titlebar")).append(' ').append("5.06b04").toString();
    }
    catch (Exception localException1)
    {
      localException1.printStackTrace();
    }
    this.currentCommands = ((Commands)paramObject1);
    this.currentCommandListener = new CommandListener((Commands)paramObject1);
    setViewerTitle(windowTitle);
    if ((this.frame instanceof JFrame))
    {
      localObject1 = getURLForImage(new StringBuilder().append(this.iconLocation).append("icon.png").toString());
      if (localObject1 != null)
        try
        {
          localObject2 = ImageIO.read((URL)localObject1);
          ((JFrame)this.frame).setIconImage((Image)localObject2);
        }
        catch (Exception localException2)
        {
        }
    }
    this.decode_pdf.setInset(inset, inset);
    Object localObject1 = this.properties.getValue("replacePdfDisplayBackground");
    if ((!((String)localObject1).isEmpty()) && (((String)localObject1).toLowerCase().equals("true")))
    {
      localObject1 = this.properties.getValue("pdfDisplayBackground");
      if (!((String)localObject1).isEmpty())
        ((Commands)paramObject1).executeCommand(714, new Object[] { Integer.valueOf(Integer.parseInt((String)localObject1)) });
      this.decode_pdf.setBackground(new Color(Integer.parseInt((String)localObject1)));
    }
    else if (this.decode_pdf.getDecoderOptions().getDisplayBackgroundColor() != null)
    {
      this.decode_pdf.setBackground(this.decode_pdf.getDecoderOptions().getDisplayBackgroundColor());
    }
    else if (this.decode_pdf.useNewGraphicsMode)
    {
      this.decode_pdf.setBackground(new Color(55, 55, 65));
    }
    else
    {
      this.decode_pdf.setBackground(new Color(190, 190, 190));
    }
    String[] arrayOfString = new String[2];
    arrayOfString[1] = Messages.getMessage("PdfViewerToolbarComboBox.imageQual");
    arrayOfString[0] = Messages.getMessage("PdfViewerTooltipComboBox.imageMem");
    this.qualityBox = new SwingCombo(arrayOfString);
    this.qualityBox.setBackground(Color.white);
    this.qualityBox.setSelectedIndex(0);
    String str2 = System.getProperty("org.jpedal.defaultViewerScaling");
    if (str2 == null)
      str2 = this.properties.getValue("startScaling");
    if (str2 != null)
    {
      int j = paramArrayOfString.length;
      for (int k = 0; k < j; k++)
        if (paramArrayOfString[k].equals(str2))
        {
          defaultSelection = k;
          k = j;
        }
    }
    this.scalingBox = new SwingCombo(paramArrayOfString);
    this.scalingBox.setBackground(Color.white);
    this.scalingBox.setEditable(true);
    this.scalingBox.setSelectedIndex(defaultSelection);
    this.rotationBox = new SwingCombo(this.rotationValues);
    this.rotationBox.setBackground(Color.white);
    this.rotationBox.setSelectedIndex(0);
    JPanel localJPanel = new JPanel();
    if (this.isSingle)
    {
      this.previewOnSingleScroll = this.properties.getValue("previewOnSingleScroll").toLowerCase().equals("true");
      if (this.previewOnSingleScroll)
      {
        this.thumbscroll = new JScrollBar(1, 0, 1, 0, 1);
        this.thumbscroll.setName("ThumbnailScroll");
        if (this.scrollListener == null)
        {
          this.scrollListener = new ScrollListener(null);
          this.scrollMouseListener = new ScrollMouseListener(null);
        }
        this.thumbscroll.addAdjustmentListener(this.scrollListener);
        this.thumbscroll.addMouseListener(this.scrollMouseListener);
        localJPanel.setLayout(new BorderLayout());
        localJPanel.add(this.thumbscroll, "East");
        this.scrollPane.getViewport().add(this.decode_pdf);
        localJPanel.add(this.scrollPane, "Center");
        this.scrollPane.setVerticalScrollBarPolicy(21);
        this.scrollPane.setHorizontalScrollBarPolicy(31);
      }
      else
      {
        this.scrollPane.getViewport().add(this.decode_pdf);
        this.scrollPane.setVerticalScrollBarPolicy(20);
        this.scrollPane.setHorizontalScrollBarPolicy(30);
      }
      this.scrollPane.getVerticalScrollBar().setUnitIncrement(80);
      this.scrollPane.getHorizontalScrollBar().setUnitIncrement(80);
      this.decode_pdf.addKeyListener(new KeyAdapter()
      {
        int count = 0;
        int pageChange = 0;
        java.util.Timer t2;

        public void keyPressed(KeyEvent paramAnonymousKeyEvent)
        {
          final JScrollBar localJScrollBar = SwingGUI.this.scrollPane.getVerticalScrollBar();
          if ((paramAnonymousKeyEvent.getKeyCode() == 37) || (paramAnonymousKeyEvent.getKeyCode() == 39))
          {
            if (SwingGUI.this.scrollPane.getWidth() > SwingGUI.this.decode_pdf.getWidth())
              if (paramAnonymousKeyEvent.getKeyCode() == 37)
                this.pageChange -= 1;
              else
                this.pageChange += 1;
          }
          else if (((paramAnonymousKeyEvent.getKeyCode() == 38) || (paramAnonymousKeyEvent.getKeyCode() == 40)) && (this.count == 0))
            if ((paramAnonymousKeyEvent.getKeyCode() == 38) && (localJScrollBar.getValue() == localJScrollBar.getMinimum()) && (SwingGUI.this.getCurrentPage() > 1))
              this.pageChange -= 1;
            else if ((paramAnonymousKeyEvent.getKeyCode() == 40) && ((localJScrollBar.getValue() == localJScrollBar.getMaximum() - localJScrollBar.getHeight()) || (localJScrollBar.getHeight() == 0)) && (SwingGUI.this.getCurrentPage() < SwingGUI.this.decode_pdf.getPageCount()))
              this.pageChange += 1;
          this.count += 1;
          if (this.pageChange != 0)
          {
            if (this.t2 != null)
              this.t2.cancel();
            TimerTask local1 = new TimerTask()
            {
              public void run()
              {
                int i = SwingGUI.this.commonValues.getCurrentPage() + SwingGUI.16.this.pageChange;
                if (i < 1)
                  i = 1;
                else if (i > SwingGUI.this.decode_pdf.getPageCount())
                  i = SwingGUI.this.decode_pdf.getPageCount();
                if (i != SwingGUI.this.commonValues.getCurrentPage())
                {
                  String str = "" + i;
                  ((Commands)SwingGUI.16.this.val$currentCommands).executeCommand(56, new Object[] { str });
                  if ((localJScrollBar.getValue() == localJScrollBar.getMinimum()) && (SwingGUI.this.getCurrentPage() > 1))
                    SwingUtilities.invokeLater(new Runnable()
                    {
                      public void run()
                      {
                        SwingGUI.16.1.this.val$scroll.setValue(SwingGUI.16.1.this.val$scroll.getMaximum());
                      }
                    });
                  else if (((localJScrollBar.getValue() == localJScrollBar.getMaximum() - localJScrollBar.getHeight()) || (localJScrollBar.getHeight() == 0)) && (SwingGUI.this.getCurrentPage() < SwingGUI.this.decode_pdf.getPageCount()))
                    SwingUtilities.invokeLater(new Runnable()
                    {
                      public void run()
                      {
                        SwingGUI.16.1.this.val$scroll.setValue(SwingGUI.16.1.this.val$scroll.getMinimum());
                      }
                    });
                }
                SwingGUI.16.this.pageChange = 0;
                if (SwingGUI.16.this.t2 != null)
                  SwingGUI.16.this.t2.cancel();
              }
            };
            this.t2 = new java.util.Timer();
            this.t2.schedule(local1, 500L);
          }
        }

        public void keyReleased(KeyEvent paramAnonymousKeyEvent)
        {
          this.count = 0;
        }
      });
    }
    this.comboBoxBar.setBorder(BorderFactory.createEmptyBorder());
    this.comboBoxBar.setLayout(new FlowLayout(3));
    this.comboBoxBar.setFloatable(false);
    this.comboBoxBar.setFont(new Font("SansSerif", 0, 8));
    if (this.isSingle)
    {
      this.navOptionsPanel.setTabPlacement(2);
      this.navOptionsPanel.setOpaque(true);
      this.navOptionsPanel.setMinimumSize(new Dimension(startSize, 100));
      this.navOptionsPanel.setName("NavPanel");
      this.navOptionsPanel.setFocusable(false);
      this.pageTitle = Messages.getMessage("PdfViewerJPanel.thumbnails");
      this.bookmarksTitle = Messages.getMessage("PdfViewerJPanel.bookmarks");
      this.layersTitle = Messages.getMessage("PdfViewerJPanel.layers");
      this.signaturesTitle = "Signatures";
      if (this.previewOnSingleScroll)
        this.displayPane = new JSplitPane(1, this.navOptionsPanel, localJPanel);
      else
        this.displayPane = new JSplitPane(1, this.navOptionsPanel, this.scrollPane);
      this.displayPane.setOneTouchExpandable(false);
      this.displayPane.addPropertyChangeListener("dividerLocation", new PropertyChangeListener()
      {
        public void propertyChange(PropertyChangeEvent paramAnonymousPropertyChangeEvent)
        {
          SwingGUI.this.scrollPane.getViewport().setSize(SwingGUI.this.scrollPane.getViewport().getWidth() + ((Integer)paramAnonymousPropertyChangeEvent.getOldValue()).intValue() - ((Integer)paramAnonymousPropertyChangeEvent.getNewValue()).intValue(), SwingGUI.this.scrollPane.getViewport().getHeight());
          SwingGUI.this.desktopPane.setSize(SwingGUI.this.desktopPane.getWidth() + ((Integer)paramAnonymousPropertyChangeEvent.getOldValue()).intValue() - ((Integer)paramAnonymousPropertyChangeEvent.getNewValue()).intValue(), SwingGUI.this.desktopPane.getHeight());
          int i = ((Integer)paramAnonymousPropertyChangeEvent.getNewValue()).intValue();
          if ((SwingGUI.this.tabsExpanded) && (i > SwingGUI.startSize))
            SwingGUI.expandedSize = i;
          SwingGUI.this.zoom();
        }
      });
      if (DecoderOptions.isRunningOnMac)
      {
        this.navOptionsPanel.addTab(this.pageTitle, (Component)this.thumbnails);
        this.navOptionsPanel.setTitleAt(this.navOptionsPanel.getTabCount() - 1, this.pageTitle);
        if (this.thumbnails.isShownOnscreen())
        {
          this.navOptionsPanel.addTab(this.bookmarksTitle, (SwingOutline)this.tree);
          this.navOptionsPanel.setTitleAt(this.navOptionsPanel.getTabCount() - 1, this.bookmarksTitle);
        }
      }
      else
      {
        ((SwingOutline)this.tree).setBorder(null);
        ((SwingThumbnailPanel)this.thumbnails).setBorder(null);
        if (this.thumbnails.isShownOnscreen())
        {
          localObject3 = new VTextIcon(this.navOptionsPanel, this.pageTitle, 2);
          this.navOptionsPanel.addTab(null, (Icon)localObject3, (Component)this.thumbnails);
        }
        localObject3 = new VTextIcon(this.navOptionsPanel, this.bookmarksTitle, 2);
        this.navOptionsPanel.addTab(null, (Icon)localObject3, (SwingOutline)this.tree);
      }
      localObject1 = this.properties.getValue("startSideTabOpen");
      if (!((String)localObject1).isEmpty())
        this.sideTabBarOpenByDefault = ((String)localObject1).toLowerCase().equals("true");
      localObject1 = this.properties.getValue("startSelectedSideTab");
      if (!((String)localObject1).isEmpty())
        this.startSelectedTab = ((String)localObject1);
      if (!this.hasListener)
      {
        this.hasListener = true;
        this.navOptionsPanel.addMouseListener(new MouseListener()
        {
          public void mouseClicked(MouseEvent paramAnonymousMouseEvent)
          {
            SwingGUI.this.handleTabbedPanes();
          }

          public void mousePressed(MouseEvent paramAnonymousMouseEvent)
          {
          }

          public void mouseReleased(MouseEvent paramAnonymousMouseEvent)
          {
          }

          public void mouseEntered(MouseEvent paramAnonymousMouseEvent)
          {
          }

          public void mouseExited(MouseEvent paramAnonymousMouseEvent)
          {
          }
        });
      }
    }
    this.first = new SwingButton();
    this.fback = new SwingButton();
    this.back = new SwingButton();
    this.forward = new SwingButton();
    this.fforward = new SwingButton();
    this.end = new SwingButton();
    this.snapshotButton = new SwingButton();
    this.buyButton = new SwingButton();
    this.helpButton = new SwingButton();
    this.rssButton = new SwingButton();
    this.singleButton = new SwingButton();
    this.continuousButton = new SwingButton();
    this.continuousFacingButton = new SwingButton();
    this.facingButton = new SwingButton();
    this.pageFlowButton = new SwingButton();
    this.openButton = new SwingButton();
    this.printButton = new SwingButton();
    this.searchButton = new SwingButton();
    this.docPropButton = new SwingButton();
    this.infoButton = new SwingButton();
    this.mouseMode = new SwingButton();
    this.previousSearch = new SwingButton();
    this.nextSearch = new SwingButton();
    this.pageCounter2.setEditable(true);
    this.pageCounter2.setToolTipText(Messages.getMessage("PdfViewerTooltip.goto"));
    this.pageCounter2.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1), this.pageCounter2.getBorder()));
    this.pageCounter2.setColumns(2);
    this.pageCounter2.setMaximumSize(this.pageCounter2.getPreferredSize());
    this.pageCounter2.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        String str = SwingGUI.this.pageCounter2.getText().trim();
        ((Commands)paramObject1).gotoPage(str);
      }
    });
    this.pageCounter2.setHorizontalAlignment(0);
    this.pageCounter2.setForeground(Color.black);
    setPageNumber();
    this.pageCounter3 = new JLabel(new StringBuilder().append(Messages.getMessage("PdfViewerOfLabel.text")).append(' ').toString());
    this.pageCounter3.setOpaque(false);
    Object localObject3 = new JPanel();
    ((JPanel)localObject3).setLayout(new BorderLayout());
    if ((this.frame instanceof JFrame))
      ((JFrame)this.frame).getContentPane().add((Component)localObject3, "North");
    else
      this.frame.add((Component)localObject3, "North");
    this.navToolBar.setLayout(new BoxLayout(this.navToolBar, 2));
    this.navToolBar.setFloatable(false);
    this.pagesToolBar.setFloatable(false);
    this.navButtons.setBorder(BorderFactory.createEmptyBorder());
    this.navButtons.setLayout(new BorderLayout());
    this.navButtons.setFloatable(false);
    this.navButtons.setPreferredSize(new Dimension(5, 24));
    ((JPanel)localObject3).add(this.currentMenu, "North");
    this.topButtons.setBorder(BorderFactory.createEmptyBorder());
    this.topButtons.setLayout(new FlowLayout(3));
    this.topButtons.setFloatable(false);
    this.topButtons.setFont(new Font("SansSerif", 0, 8));
    ((JPanel)localObject3).add(this.topButtons, "Center");
    if (!this.useNewLayout)
      ((JPanel)localObject3).add(this.comboBoxBar, "South");
    if ((this.frame instanceof JFrame))
      ((JFrame)this.frame).getContentPane().add(this.navButtons, "South");
    else
      this.frame.add(this.navButtons, "South");
    if (this.displayPane != null)
      if ((this.frame instanceof JFrame))
        ((JFrame)this.frame).getContentPane().add(this.displayPane, "Center");
      else
        this.frame.add(this.displayPane, "Center");
    createMainMenu(true);
    addButton(0, Messages.getMessage("PdfViewerToolbarTooltip.openFile"), new StringBuilder().append(this.iconLocation).append("open.gif").toString(), 10);
    addButton(0, Messages.getMessage("PdfViewerToolbarTooltip.print"), new StringBuilder().append(this.iconLocation).append("print.gif").toString(), 6);
    if ((this.searchFrame != null) && ((this.searchFrame.getStyle() == SwingSearchWindow.SEARCH_EXTERNAL_WINDOW) || ((this.searchFrame.getStyle() == SwingSearchWindow.SEARCH_MENU_BAR) && (!this.isSingle))))
    {
      this.searchFrame.setStyle(SwingSearchWindow.SEARCH_EXTERNAL_WINDOW);
      addButton(0, Messages.getMessage("PdfViewerToolbarTooltip.search"), new StringBuilder().append(this.iconLocation).append("find.gif").toString(), 12);
    }
    addButton(0, Messages.getMessage("PdfViewerToolbarTooltip.properties"), new StringBuilder().append(this.iconLocation).append("properties.gif").toString(), 9);
    if ((!this.useNewLayout) || (this.commonValues.getModeOfOperation() == 4))
      addButton(0, Messages.getMessage("PdfViewerToolbarTooltip.about"), new StringBuilder().append(this.iconLocation).append("about.gif").toString(), 1);
    addButton(0, Messages.getMessage("PdfViewerToolbarTooltip.snapshot"), new StringBuilder().append(this.iconLocation).append("snapshot.gif").toString(), 13);
    JSeparator localJSeparator;
    if (this.useNewLayout)
    {
      localJSeparator = new JSeparator(1);
      localJSeparator.setPreferredSize(new Dimension(5, 32));
      this.topButtons.add(localJSeparator);
    }
    addCombo(Messages.getMessage("PdfViewerToolbarScaling.text"), Messages.getMessage("PdfViewerToolbarTooltip.zoomin"), 252);
    addCombo(Messages.getMessage("PdfViewerToolbarRotation.text"), Messages.getMessage("PdfViewerToolbarTooltip.rotation"), 251);
    addCombo(Messages.getMessage("PdfViewerToolbarImageOp.text"), Messages.getMessage("PdfViewerToolbarTooltip.imageOp"), 250);
    addButton(0, Messages.getMessage("PdfViewerToolbarTooltip.mouseMode"), new StringBuilder().append(this.iconLocation).append("mouse_select.png").toString(), 29);
    if (this.useNewLayout)
    {
      localJSeparator = new JSeparator(1);
      localJSeparator.setPreferredSize(new Dimension(5, 32));
      this.topButtons.add(localJSeparator);
    }
    if (this.commonValues.getModeOfOperation() != 4)
      addButton(0, Messages.getMessage("PdfViewerToolbarTooltip.help"), new StringBuilder().append(this.iconLocation).append("help.png").toString(), 998);
    if (this.commonValues.getModeOfOperation() != 4)
      addButton(0, "RSS", new StringBuilder().append(this.iconLocation).append("rss.gif").toString(), 997);
    createNavbar();
    addCursor();
    if ((this.searchFrame != null) && (this.searchFrame.getStyle() == SwingSearchWindow.SEARCH_MENU_BAR) && (this.isSingle))
      searchInMenu(this.searchFrame);
    initStatus();
    this.frame.invalidate();
    this.frame.validate();
    this.frame.repaint();
    try
    {
      loadProperties();
    }
    catch (Exception localException3)
    {
      localException3.printStackTrace();
    }
    if (this.commonValues.getModeOfOperation() != 1)
    {
      int m = Integer.parseInt(this.properties.getValue("startViewerWidth"));
      int n = Integer.parseInt(this.properties.getValue("startViewerHeight"));
      Dimension localDimension = Toolkit.getDefaultToolkit().getScreenSize();
      if (m < 0)
      {
        m = localDimension.width / 2;
        if (m < 700)
          m = 700;
        this.properties.setValue("startViewerWidth", new StringBuilder().append("").append(m).toString());
      }
      if (n < 0)
      {
        n = localDimension.height / 2;
        this.properties.setValue("startViewerHeight", new StringBuilder().append("").append(n).toString());
      }
      String str3 = System.getProperty("org.jpedal.startWindowSize");
      if (str3 != null)
      {
        StringTokenizer localStringTokenizer = new StringTokenizer(str3, "x");
        System.out.println(localStringTokenizer.countTokens());
        if (localStringTokenizer.countTokens() != 2)
          throw new RuntimeException(new StringBuilder().append("Unable to use value for org.jpedal.startWindowSize=").append(str3).append("\nValue should be in format org.jpedal.startWindowSize=200x300").toString());
        try
        {
          m = Integer.parseInt(localStringTokenizer.nextToken().trim());
          n = Integer.parseInt(localStringTokenizer.nextToken().trim());
        }
        catch (Exception localException4)
        {
          throw new RuntimeException(new StringBuilder().append("Unable to use value for org.jpedal.startWindowSize=").append(str3).append("\nValue should be in format org.jpedal.startWindowSize=200x300").toString());
        }
      }
      if ((this.frame instanceof JFrame))
      {
        this.frame.setSize(m, n);
        ((JFrame)this.frame).setLocationRelativeTo(null);
        ((JFrame)this.frame).setDefaultCloseOperation(0);
        ((JFrame)this.frame).addWindowListener(new FrameCloser((Commands)paramObject1, this, this.decode_pdf, (Printer)paramObject2, this.thumbnails, this.commonValues, this.properties));
        this.frame.setVisible(true);
      }
    }
    this.frame.addComponentListener(new ComponentListener()
    {
      public void componentHidden(ComponentEvent paramAnonymousComponentEvent)
      {
      }

      public void componentMoved(ComponentEvent paramAnonymousComponentEvent)
      {
      }

      public void componentResized(ComponentEvent paramAnonymousComponentEvent)
      {
        if ((SwingGUI.this.decode_pdf.getParent() != null) && ((SwingGUI.this.getSelectedComboIndex(252) < 3) || (SwingGUI.this.decode_pdf.getDisplayView() == 3)))
          SwingGUI.this.zoom();
      }

      public void componentShown(ComponentEvent paramAnonymousComponentEvent)
      {
      }
    });
    if (this.decode_pdf.useNewGraphicsMode)
      this.decode_pdf.setPDFBorder(new AbstractBorder()
      {
        public void paintBorder(Component paramAnonymousComponent, Graphics paramAnonymousGraphics, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3, int paramAnonymousInt4)
        {
          Graphics2D localGraphics2D = (Graphics2D)paramAnonymousGraphics;
          int i = SwingGUI.this.glowThickness / 2 + 1;
          localGraphics2D.setPaint(new GradientPaint(paramAnonymousInt1, 0.0F, SwingGUI.this.glowOuterColor, paramAnonymousInt1 + SwingGUI.this.glowThickness, 0.0F, SwingGUI.this.glowInnerColor));
          localGraphics2D.fillRect(paramAnonymousInt1, paramAnonymousInt2 + SwingGUI.this.glowThickness, SwingGUI.this.glowThickness, paramAnonymousInt4 - SwingGUI.this.glowThickness * 2);
          localGraphics2D.setPaint(new GradientPaint(paramAnonymousInt1 - i + SwingGUI.this.glowThickness, paramAnonymousInt2 + paramAnonymousInt4 + i - SwingGUI.this.glowThickness, SwingGUI.this.glowOuterColor, paramAnonymousInt1 + SwingGUI.this.glowThickness, paramAnonymousInt2 + paramAnonymousInt4 - SwingGUI.this.glowThickness, SwingGUI.this.glowInnerColor));
          localGraphics2D.fillRect(paramAnonymousInt1, paramAnonymousInt2 + paramAnonymousInt4 - SwingGUI.this.glowThickness, SwingGUI.this.glowThickness, SwingGUI.this.glowThickness);
          localGraphics2D.setPaint(new GradientPaint(0.0F, paramAnonymousInt2 + paramAnonymousInt4, SwingGUI.this.glowOuterColor, 0.0F, paramAnonymousInt2 + paramAnonymousInt4 - SwingGUI.this.glowThickness, SwingGUI.this.glowInnerColor));
          localGraphics2D.fillRect(paramAnonymousInt1 + SwingGUI.this.glowThickness, paramAnonymousInt2 + paramAnonymousInt4 - SwingGUI.this.glowThickness, paramAnonymousInt3 - SwingGUI.this.glowThickness * 2, SwingGUI.this.glowThickness);
          localGraphics2D.setPaint(new GradientPaint(paramAnonymousInt1 + paramAnonymousInt3 + i - SwingGUI.this.glowThickness, paramAnonymousInt2 + paramAnonymousInt4 + i - SwingGUI.this.glowThickness, SwingGUI.this.glowOuterColor, paramAnonymousInt1 + paramAnonymousInt3 - SwingGUI.this.glowThickness, paramAnonymousInt2 + paramAnonymousInt4 - SwingGUI.this.glowThickness, SwingGUI.this.glowInnerColor));
          localGraphics2D.fillRect(paramAnonymousInt1 + paramAnonymousInt3 - SwingGUI.this.glowThickness, paramAnonymousInt2 + paramAnonymousInt4 - SwingGUI.this.glowThickness, SwingGUI.this.glowThickness, SwingGUI.this.glowThickness);
          localGraphics2D.setPaint(new GradientPaint(paramAnonymousInt1 + paramAnonymousInt3, 0.0F, SwingGUI.this.glowOuterColor, paramAnonymousInt1 + paramAnonymousInt3 - SwingGUI.this.glowThickness, 0.0F, SwingGUI.this.glowInnerColor));
          localGraphics2D.fillRect(paramAnonymousInt1 + paramAnonymousInt3 - SwingGUI.this.glowThickness, paramAnonymousInt2 + SwingGUI.this.glowThickness, SwingGUI.this.glowThickness, paramAnonymousInt4 - SwingGUI.this.glowThickness * 2);
          localGraphics2D.setPaint(new GradientPaint(paramAnonymousInt1 + paramAnonymousInt3 - SwingGUI.this.glowThickness + i, paramAnonymousInt2 + SwingGUI.this.glowThickness - i, SwingGUI.this.glowOuterColor, paramAnonymousInt1 + paramAnonymousInt3 - SwingGUI.this.glowThickness, paramAnonymousInt2 + SwingGUI.this.glowThickness, SwingGUI.this.glowInnerColor));
          localGraphics2D.fillRect(paramAnonymousInt1 + paramAnonymousInt3 - SwingGUI.this.glowThickness, paramAnonymousInt2, SwingGUI.this.glowThickness, SwingGUI.this.glowThickness);
          localGraphics2D.setPaint(new GradientPaint(0.0F, paramAnonymousInt2, SwingGUI.this.glowOuterColor, 0.0F, paramAnonymousInt2 + SwingGUI.this.glowThickness, SwingGUI.this.glowInnerColor));
          localGraphics2D.fillRect(paramAnonymousInt1 + SwingGUI.this.glowThickness, paramAnonymousInt2, paramAnonymousInt3 - SwingGUI.this.glowThickness * 2, SwingGUI.this.glowThickness);
          localGraphics2D.setPaint(new GradientPaint(paramAnonymousInt1 - i + SwingGUI.this.glowThickness, paramAnonymousInt2 - i + SwingGUI.this.glowThickness, SwingGUI.this.glowOuterColor, paramAnonymousInt1 + SwingGUI.this.glowThickness, paramAnonymousInt2 + SwingGUI.this.glowThickness, SwingGUI.this.glowInnerColor));
          localGraphics2D.fillRect(paramAnonymousInt1, paramAnonymousInt2, SwingGUI.this.glowThickness, SwingGUI.this.glowThickness);
          localGraphics2D.setPaint(Color.black);
          localGraphics2D.drawRect(paramAnonymousInt1 + SwingGUI.this.glowThickness, paramAnonymousInt2 + SwingGUI.this.glowThickness, paramAnonymousInt3 - SwingGUI.this.glowThickness * 2, paramAnonymousInt4 - SwingGUI.this.glowThickness * 2);
        }

        public Insets getBorderInsets(Component paramAnonymousComponent, Insets paramAnonymousInsets)
        {
          paramAnonymousInsets.set(SwingGUI.this.glowThickness, SwingGUI.this.glowThickness, SwingGUI.this.glowThickness, SwingGUI.this.glowThickness);
          return paramAnonymousInsets;
        }
      });
    else
      this.decode_pdf.setPDFBorder(BorderFactory.createLineBorder(Color.black, 1));
  }

  public PdfDecoder getPdfDecoder()
  {
    return this.decode_pdf;
  }

  private void handleTabbedPanes()
  {
    if (this.tabsNotInitialised)
      return;
    int i = this.displayPane.getDividerLocation();
    int j = this.navOptionsPanel.getSelectedIndex();
    if (j == -1)
      return;
    if (!this.tabsExpanded)
    {
      setupThumbnailPanel();
      setBookmarks(true);
      this.displayPane.setDividerLocation(expandedSize);
      this.tabsExpanded = true;
    }
    else if (this.lastTabSelected == j)
    {
      this.displayPane.setDividerLocation(startSize);
      this.tabsExpanded = false;
    }
    this.lastTabSelected = j;
  }

  public void addCursor()
  {
    if (!this.useNewLayout)
    {
      this.cursor.setBorder(BorderFactory.createEmptyBorder());
      this.cursor.setLayout(new FlowLayout(3));
      this.cursor.setFloatable(false);
      this.cursor.setFont(new Font("SansSerif", 2, 10));
      this.cursor.add(new JLabel(Messages.getMessage("PdfViewerToolbarCursorLoc.text")));
      this.cursor.add(initCoordBox());
      this.cursor.setPreferredSize(new Dimension(200, 32));
      this.topButtons.add(this.cursor);
    }
    else
    {
      initCoordBox();
    }
  }

  public void setMultibox(int[] paramArrayOfInt)
  {
    if ((paramArrayOfInt.length > 1) && (paramArrayOfInt[0] == 1))
      if (this.cursorOverPage != (paramArrayOfInt[1] == 1))
        this.cursorOverPage = (paramArrayOfInt[1] == 1);
      else
        return;
    if ((this.statusBar.isEnabled()) && (this.statusBar.isVisible()) && (!this.statusBar.isDone()))
    {
      this.multibox.removeAll();
      this.statusBar.getStatusObject().setSize(this.multibox.getSize());
      this.multibox.add(this.statusBar.getStatusObject(), "Center");
      this.multibox.repaint();
      return;
    }
    if ((this.cursor.isEnabled()) && (this.cursor.isVisible()) && (this.cursorOverPage) && (this.decode_pdf.isOpen()))
    {
      this.multibox.removeAll();
      this.multibox.add(this.coords, "Center");
      this.multibox.repaint();
      return;
    }
    if ((this.downloadBar.isEnabled()) && (this.downloadBar.isVisible()) && (!this.downloadBar.isDone()) && ((this.decode_pdf.isLoadingLinearizedPDF()) || (!this.decode_pdf.isOpen())))
    {
      this.multibox.removeAll();
      this.downloadBar.getStatusObject().setSize(this.multibox.getSize());
      this.multibox.add(this.downloadBar.getStatusObject(), "Center");
      this.multibox.repaint();
      return;
    }
    if ((this.memoryBar.isEnabled()) && (this.memoryBar.isVisible()))
    {
      this.multibox.removeAll();
      this.memoryBar.setSize(this.multibox.getSize());
      this.memoryBar.setForeground(new Color(125, 145, 255));
      this.multibox.add(this.memoryBar, "Center");
      this.multibox.repaint();
      return;
    }
  }

  private void setKeyAccelerators(int paramInt, JMenuItem paramJMenuItem)
  {
    int i = 2;
    if (DecoderOptions.isRunningOnMac)
      i = 4;
    switch (paramInt)
    {
    case 12:
      paramJMenuItem.setAccelerator(KeyStroke.getKeyStroke(70, i));
      break;
    case 5:
      paramJMenuItem.setAccelerator(KeyStroke.getKeyStroke(83, i));
      break;
    case 6:
      paramJMenuItem.setAccelerator(KeyStroke.getKeyStroke(80, i));
      break;
    case 7:
      paramJMenuItem.setAccelerator(KeyStroke.getKeyStroke(81, i));
      break;
    case 9:
      paramJMenuItem.setAccelerator(KeyStroke.getKeyStroke(68, i));
      break;
    case 10:
      paramJMenuItem.setAccelerator(KeyStroke.getKeyStroke(79, i));
      break;
    case 14:
      paramJMenuItem.setAccelerator(KeyStroke.getKeyStroke(85, i));
      break;
    case 16:
      paramJMenuItem.setAccelerator(KeyStroke.getKeyStroke(37, 9));
      break;
    case 17:
      paramJMenuItem.setAccelerator(KeyStroke.getKeyStroke(39, 9));
      break;
    case 50:
      paramJMenuItem.setAccelerator(KeyStroke.getKeyStroke(36, i));
      break;
    case 52:
      paramJMenuItem.setAccelerator(KeyStroke.getKeyStroke(38, 2));
      break;
    case 53:
      paramJMenuItem.setAccelerator(KeyStroke.getKeyStroke(40, 2));
      break;
    case 55:
      paramJMenuItem.setAccelerator(KeyStroke.getKeyStroke(35, i));
      break;
    case 56:
      paramJMenuItem.setAccelerator(KeyStroke.getKeyStroke(78, i | 0x1));
      break;
    case 2:
      paramJMenuItem.setAccelerator(KeyStroke.getKeyStroke(66, 8));
      break;
    case 25:
      paramJMenuItem.setAccelerator(KeyStroke.getKeyStroke(67, i));
      break;
    case 26:
      paramJMenuItem.setAccelerator(KeyStroke.getKeyStroke(65, i));
      break;
    case 27:
      paramJMenuItem.setAccelerator(KeyStroke.getKeyStroke(65, i + 64));
      break;
    case 24:
      paramJMenuItem.setAccelerator(KeyStroke.getKeyStroke(75, i));
    case 3:
    case 4:
    case 8:
    case 11:
    case 13:
    case 15:
    case 18:
    case 19:
    case 20:
    case 21:
    case 22:
    case 23:
    case 28:
    case 29:
    case 30:
    case 31:
    case 32:
    case 33:
    case 34:
    case 35:
    case 36:
    case 37:
    case 38:
    case 39:
    case 40:
    case 41:
    case 42:
    case 43:
    case 44:
    case 45:
    case 46:
    case 47:
    case 48:
    case 49:
    case 51:
    case 54:
    }
  }

  public void addButton(int paramInt1, String paramString1, String paramString2, int paramInt2)
  {
    Object localObject = new SwingButton();
    switch (paramInt2)
    {
    case 998:
      localObject = this.helpButton;
      break;
    case 997:
      localObject = this.rssButton;
      break;
    case 999:
      localObject = this.buyButton;
      break;
    case 50:
      localObject = this.first;
      break;
    case 51:
      localObject = this.fback;
      break;
    case 52:
      localObject = this.back;
      break;
    case 53:
      localObject = this.forward;
      break;
    case 54:
      localObject = this.fforward;
      break;
    case 55:
      localObject = this.end;
      break;
    case 13:
      localObject = this.snapshotButton;
      break;
    case 57:
      localObject = this.singleButton;
      ((GUIButton)localObject).setName("SINGLE");
      break;
    case 58:
      localObject = this.continuousButton;
      ((GUIButton)localObject).setName("CONTINUOUS");
      break;
    case 59:
      localObject = this.continuousFacingButton;
      ((GUIButton)localObject).setName("CONTINUOUS_FACING");
      break;
    case 60:
      localObject = this.facingButton;
      ((GUIButton)localObject).setName("FACING");
      break;
    case 62:
      localObject = this.pageFlowButton;
      ((GUIButton)localObject).setName("PAGEFLOW");
      break;
    case 18:
      localObject = this.previousSearch;
      ((GUIButton)localObject).setName("PREVIOUSRESULT");
      break;
    case 19:
      localObject = this.nextSearch;
      ((GUIButton)localObject).setName("NEXTRESULT");
      break;
    case 10:
      localObject = this.openButton;
      ((GUIButton)localObject).setName("open");
      break;
    case 6:
      localObject = this.printButton;
      ((GUIButton)localObject).setName("print");
      break;
    case 12:
      localObject = this.searchButton;
      ((GUIButton)localObject).setName("search");
      break;
    case 9:
      localObject = this.docPropButton;
      break;
    case 1:
      localObject = this.infoButton;
      break;
    case 29:
      localObject = this.mouseMode;
      ((SwingButton)localObject).addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent paramAnonymousActionEvent)
        {
          int i = SwingGUI.this.currentCommands.getMouseMode().getMouseMode();
          switch (i)
          {
          case 0:
            SwingGUI.this.textSelect.setSelected(true);
            SwingGUI.this.panMode.setSelected(false);
            break;
          case 1:
            SwingGUI.this.textSelect.setSelected(false);
            SwingGUI.this.panMode.setSelected(true);
          }
        }
      });
      ((GUIButton)localObject).setName("mousemode");
    }
    ((SwingButton)localObject).addMouseListener(new MouseListener()
    {
      public void mouseClicked(MouseEvent paramAnonymousMouseEvent)
      {
      }

      public void mousePressed(MouseEvent paramAnonymousMouseEvent)
      {
      }

      public void mouseReleased(MouseEvent paramAnonymousMouseEvent)
      {
      }

      public void mouseEntered(MouseEvent paramAnonymousMouseEvent)
      {
        if (SingleDisplay.allowChangeCursor)
          ((SwingButton)paramAnonymousMouseEvent.getSource()).setCursor(Cursor.getPredefinedCursor(12));
      }

      public void mouseExited(MouseEvent paramAnonymousMouseEvent)
      {
        if (SingleDisplay.allowChangeCursor)
          ((SwingButton)paramAnonymousMouseEvent.getSource()).setCursor(Cursor.getPredefinedCursor(0));
      }
    });
    ((GUIButton)localObject).init(getURLForImage(paramString2), paramInt2, paramString1);
    ((AbstractButton)localObject).addActionListener(this.currentCommandListener);
    int i = this.commonValues.getModeOfOperation();
    if (i == 1)
      ((AbstractButton)localObject).setContentAreaFilled(false);
    if ((paramInt1 == 0) || (i == 4))
    {
      this.topButtons.add((AbstractButton)localObject);
      if ((i == 4) && ((i == 55) || (i == 62)))
        this.topButtons.add(Box.createHorizontalGlue());
    }
    else if (paramInt1 == 1)
    {
      this.navToolBar.add((AbstractButton)localObject);
    }
    else if (paramInt1 == 2)
    {
      this.pagesToolBar.add((AbstractButton)localObject, "Center");
    }
  }

  public void addMenuItem(JMenu paramJMenu, String paramString1, String paramString2, int paramInt)
  {
    addMenuItem(paramJMenu, paramString1, paramString2, paramInt, false);
  }

  public void addMenuItem(JMenu paramJMenu, String paramString1, String paramString2, int paramInt, boolean paramBoolean)
  {
    Object localObject;
    if (paramBoolean)
      localObject = new SwingCheckBoxMenuItem(paramString1);
    else
      localObject = new SwingMenuItem(paramString1);
    if (!paramString2.isEmpty())
      ((SwingID)localObject).setToolTipText(paramString2);
    ((SwingID)localObject).setID(paramInt);
    setKeyAccelerators(paramInt, (JMenuItem)localObject);
    ((SwingID)localObject).addActionListener(this.currentCommandListener);
    switch (paramInt)
    {
    case 10:
      this.open = ((JMenuItem)localObject);
      paramJMenu.add(this.open);
      break;
    case 14:
      this.openUrl = ((JMenuItem)localObject);
      paramJMenu.add(this.openUrl);
      break;
    case 5:
      this.save = ((JMenuItem)localObject);
      paramJMenu.add(this.save);
      break;
    case 500:
      this.reSaveAsForms = ((JMenuItem)localObject);
      this.reSaveAsForms.setName("resaveForms");
      paramJMenu.add(this.reSaveAsForms);
      break;
    case 12:
      this.find = ((JMenuItem)localObject);
      paramJMenu.add(this.find);
      break;
    case 9:
      this.documentProperties = ((JMenuItem)localObject);
      paramJMenu.add(this.documentProperties);
      break;
    case 512:
      this.signPDF = ((JMenuItem)localObject);
      paramJMenu.add(this.signPDF);
      break;
    case 6:
      this.print = ((JMenuItem)localObject);
      paramJMenu.add(this.print);
      break;
    case 7:
      this.exit = ((JMenuItem)localObject);
      this.exit.setName("exit");
      paramJMenu.add(this.exit);
      break;
    case 25:
      this.copy = ((JMenuItem)localObject);
      paramJMenu.add(this.copy);
      break;
    case 26:
      this.selectAll = ((JMenuItem)localObject);
      paramJMenu.add(this.selectAll);
      break;
    case 27:
      this.deselectAll = ((JMenuItem)localObject);
      paramJMenu.add(this.deselectAll);
      break;
    case 24:
      this.preferences = ((JMenuItem)localObject);
      paramJMenu.add(this.preferences);
      break;
    case 50:
      this.firstPage = ((JMenuItem)localObject);
      paramJMenu.add(this.firstPage);
      break;
    case 52:
      this.backPage = ((JMenuItem)localObject);
      paramJMenu.add(this.backPage);
      break;
    case 53:
      this.forwardPage = ((JMenuItem)localObject);
      paramJMenu.add(this.forwardPage);
      break;
    case 55:
      this.lastPage = ((JMenuItem)localObject);
      paramJMenu.add(this.lastPage);
      break;
    case 56:
      this.goTo = ((JMenuItem)localObject);
      paramJMenu.add(this.goTo);
      break;
    case 16:
      this.previousDocument = ((JMenuItem)localObject);
      paramJMenu.add(this.previousDocument);
      break;
    case 17:
      this.nextDocument = ((JMenuItem)localObject);
      paramJMenu.add(this.nextDocument);
      break;
    case 61:
      this.fullscreen = ((JMenuItem)localObject);
      paramJMenu.add(this.fullscreen);
      break;
    case 29:
      this.fullscreen = ((JMenuItem)localObject);
      paramJMenu.add(this.fullscreen);
      break;
    case 30:
      this.panMode = ((JMenuItem)localObject);
      this.panMode.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent paramAnonymousActionEvent)
        {
          SwingGUI.this.panMode.setSelected(true);
          SwingGUI.this.textSelect.setSelected(false);
        }
      });
      paramJMenu.add(this.panMode);
      break;
    case 31:
      this.textSelect = ((JMenuItem)localObject);
      this.textSelect.setSelected(true);
      this.textSelect.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent paramAnonymousActionEvent)
        {
          SwingGUI.this.textSelect.setSelected(true);
          SwingGUI.this.panMode.setSelected(false);
        }
      });
      paramJMenu.add(this.textSelect);
      break;
    case 32:
      this.separateCover = ((JCheckBoxMenuItem)localObject);
      boolean bool = this.properties.getValue("separateCoverOn").toLowerCase().equals("true");
      this.separateCover.setState(bool);
      SingleDisplay.default_separateCover = bool;
      paramJMenu.add(this.separateCover);
      break;
    case 21:
      this.cascade = ((JMenuItem)localObject);
      paramJMenu.add(this.cascade);
      break;
    case 22:
      this.tile = ((JMenuItem)localObject);
      paramJMenu.add(this.tile);
      break;
    case 501:
      this.onePerPage = ((JMenuItem)localObject);
      paramJMenu.add(this.onePerPage);
      break;
    case 510:
      this.nup = ((JMenuItem)localObject);
      paramJMenu.add(this.nup);
      break;
    case 511:
      this.handouts = ((JMenuItem)localObject);
      paramJMenu.add(this.handouts);
      break;
    case 3:
      this.images = ((JMenuItem)localObject);
      paramJMenu.add(this.images);
      break;
    case 4:
      this.text = ((JMenuItem)localObject);
      paramJMenu.add(this.text);
      break;
    case 2:
      this.bitmap = ((JMenuItem)localObject);
      paramJMenu.add(this.bitmap);
      break;
    case 502:
      this.rotatePages = ((JMenuItem)localObject);
      paramJMenu.add(this.rotatePages);
      break;
    case 503:
      this.deletePages = ((JMenuItem)localObject);
      paramJMenu.add(this.deletePages);
      break;
    case 504:
      this.addPage = ((JMenuItem)localObject);
      paramJMenu.add(this.addPage);
      break;
    case 506:
      this.addHeaderFooter = ((JMenuItem)localObject);
      paramJMenu.add(this.addHeaderFooter);
      break;
    case 507:
      this.stampText = ((JMenuItem)localObject);
      paramJMenu.add(this.stampText);
      break;
    case 508:
      this.stampImage = ((JMenuItem)localObject);
      paramJMenu.add(this.stampImage);
      break;
    case 509:
      this.crop = ((JMenuItem)localObject);
      paramJMenu.add(this.crop);
      break;
    case 15:
      this.visitWebsite = ((JMenuItem)localObject);
      paramJMenu.add(this.visitWebsite);
      break;
    case 20:
      this.tipOfTheDay = ((JMenuItem)localObject);
      paramJMenu.add(this.tipOfTheDay);
      break;
    case 23:
      this.checkUpdates = ((JMenuItem)localObject);
      paramJMenu.add(this.checkUpdates);
      break;
    case 1:
      this.about = ((JMenuItem)localObject);
      paramJMenu.add(this.about);
      break;
    case 998:
      this.helpForum = ((JMenuItem)localObject);
      paramJMenu.add(this.helpForum);
      break;
    default:
      if ((localObject instanceof JMenuItem))
        paramJMenu.add((JMenuItem)localObject);
      else if ((localObject instanceof JCheckBoxMenuItem))
        paramJMenu.add((JCheckBoxMenuItem)localObject);
      break;
    }
  }

  public String getIconLocation()
  {
    return this.iconLocation;
  }

  public URL getURLForImage(String paramString)
  {
    String str = paramString.substring(paramString.lastIndexOf(47) + 1);
    paramString = new StringBuilder().append(paramString.substring(0, paramString.indexOf(46))).append(".gif").toString();
    File localFile = new File(paramString);
    URL localURL = getClass().getResource(paramString);
    if (localFile.exists())
      try
      {
        localURL = localFile.toURI().toURL();
      }
      catch (MalformedURLException localMalformedURLException1)
      {
      }
    if (localURL == null)
    {
      paramString = new StringBuilder().append(paramString.substring(0, paramString.indexOf(46))).append(".png").toString();
      localFile = new File(paramString);
      localURL = getClass().getResource(paramString);
      if (localFile.exists())
        try
        {
          localURL = localFile.toURI().toURL();
        }
        catch (MalformedURLException localMalformedURLException2)
        {
        }
    }
    if (localURL != null)
      return localURL;
    paramString = new StringBuilder().append("/org/jpedal/examples/viewer/res/").append(str).toString();
    localURL = getClass().getResource(paramString);
    return localURL;
  }

  public void addCombo(String paramString1, String paramString2, int paramInt)
  {
    GUICombo localGUICombo = null;
    switch (paramInt)
    {
    case 250:
      localGUICombo = this.qualityBox;
      break;
    case 252:
      localGUICombo = this.scalingBox;
      break;
    case 251:
      localGUICombo = this.rotationBox;
    }
    localGUICombo.setID(paramInt);
    this.optimizationLabel = new JLabel(paramString1);
    if (!paramString2.isEmpty())
      localGUICombo.setToolTipText(paramString2);
    if (this.useNewLayout)
    {
      this.topButtons.add((SwingCombo)localGUICombo);
    }
    else
    {
      this.comboBoxBar.add(this.optimizationLabel);
      this.comboBoxBar.add((SwingCombo)localGUICombo);
    }
    ((SwingCombo)localGUICombo).addActionListener(this.currentCommandListener);
  }

  public void setViewerTitle(String paramString)
  {
    if (paramString != null)
    {
      paramString = new StringBuilder().append("(").append(dx).append(" days left) ").append(paramString).toString();
      if ((this.frame instanceof JFrame))
        ((JFrame)this.frame).setTitle(paramString);
    }
    else
    {
      if (this.titleMessage == null)
        str = new StringBuilder().append(windowTitle).append(' ').append(this.commonValues.getSelectedFile()).toString();
      else
        str = new StringBuilder().append(this.titleMessage).append(this.commonValues.getSelectedFile()).toString();
      PdfObject localPdfObject = (PdfObject)this.decode_pdf.getJPedalObject(2004845231);
      if (localPdfObject != null)
      {
        LinearThread localLinearThread = (LinearThread)this.decode_pdf.getJPedalObject(-1276915978);
        if ((localLinearThread != null) && (localLinearThread.isAlive()))
          str = new StringBuilder().append(str).append(" (still loading)").toString();
        else
          str = new StringBuilder().append(str).append(" (Linearized)").toString();
      }
      String str = new StringBuilder().append("(").append(dx).append(" days left) ").append(str).toString();
      if (this.commonValues.isFormsChanged())
        str = new StringBuilder().append("* ").append(str).toString();
      if ((this.frame instanceof JFrame))
        ((JFrame)this.frame).setTitle(str);
    }
  }

  public void resetComboBoxes(boolean paramBoolean)
  {
    if (this.properties.getValue("Imageopdisplay").toLowerCase().equals("true"))
      this.qualityBox.setEnabled(paramBoolean);
    this.scalingBox.setEnabled(paramBoolean);
    this.rotationBox.setEnabled(paramBoolean);
  }

  public final JScrollPane createPane(JTextPane paramJTextPane, String paramString, boolean paramBoolean)
    throws BadLocationException
  {
    paramJTextPane.setEditable(true);
    paramJTextPane.setFont(new Font("Lucida", 0, 14));
    paramJTextPane.setToolTipText(Messages.getMessage("PdfViewerTooltip.text"));
    javax.swing.text.Document localDocument = paramJTextPane.getDocument();
    paramJTextPane.setBorder(BorderFactory.createTitledBorder(new EtchedBorder(), Messages.getMessage("PdfViewerTitle.text")));
    paramJTextPane.setForeground(Color.black);
    SimpleAttributeSet localSimpleAttributeSet1 = new SimpleAttributeSet();
    SimpleAttributeSet localSimpleAttributeSet2 = new SimpleAttributeSet();
    SimpleAttributeSet localSimpleAttributeSet3 = new SimpleAttributeSet();
    StyleConstants.setForeground(localSimpleAttributeSet1, Color.blue);
    StyleConstants.setForeground(localSimpleAttributeSet2, Color.black);
    StyleConstants.setForeground(localSimpleAttributeSet3, Color.black);
    int i = 0;
    if ((paramBoolean) && (paramString != null))
    {
      localObject = new StringTokenizer(paramString, "<>", true);
      while (((StringTokenizer)localObject).hasMoreTokens())
      {
        String str1 = ((StringTokenizer)localObject).nextToken();
        if ((str1.equals("<")) && (((StringTokenizer)localObject).hasMoreTokens()))
        {
          String str2 = new StringBuilder().append(str1).append(((StringTokenizer)localObject).nextToken()).append(((StringTokenizer)localObject).nextToken()).toString();
          localDocument.insertString(i, str2, localSimpleAttributeSet1);
          i += str2.length();
        }
        else
        {
          localDocument.insertString(i, str1, localSimpleAttributeSet2);
          i += str1.length();
        }
      }
    }
    else
    {
      localDocument.insertString(i, paramString, localSimpleAttributeSet3);
    }
    Object localObject = new JScrollPane();
    ((JScrollPane)localObject).getViewport().add(paramJTextPane);
    ((JScrollPane)localObject).setHorizontalScrollBarPolicy(31);
    ((JScrollPane)localObject).setVerticalScrollBarPolicy(20);
    return localObject;
  }

  public int getSelectedComboIndex(int paramInt)
  {
    switch (paramInt)
    {
    case 250:
      return this.qualityBox.getSelectedIndex();
    case 252:
      return this.scalingBox.getSelectedIndex();
    case 251:
      return this.rotationBox.getSelectedIndex();
    }
    return -1;
  }

  public void setSelectedComboIndex(int paramInt1, int paramInt2)
  {
    switch (paramInt1)
    {
    case 250:
      this.qualityBox.setSelectedIndex(paramInt2);
      break;
    case 252:
      this.scalingBox.setSelectedIndex(paramInt2);
      break;
    case 251:
      this.rotationBox.setSelectedIndex(paramInt2);
    }
  }

  public GUICombo getCombo(int paramInt)
  {
    switch (paramInt)
    {
    case 250:
      return this.qualityBox;
    case 252:
      return this.scalingBox;
    case 251:
      return this.rotationBox;
    }
    return null;
  }

  public void setSelectedComboItem(int paramInt, String paramString)
  {
    switch (paramInt)
    {
    case 250:
      this.qualityBox.setSelectedItem(paramString);
      break;
    case 252:
      if (StringUtils.isNumber(paramString))
        paramString = new StringBuilder().append(paramString).append('%').toString();
      this.scalingBox.setSelectedItem(paramString);
      break;
    case 251:
      this.rotationBox.setSelectedItem(paramString);
    }
  }

  public Object getSelectedComboItem(int paramInt)
  {
    switch (paramInt)
    {
    case 250:
      return this.qualityBox.getSelectedItem();
    case 252:
      return this.scalingBox.getSelectedItem();
    case 251:
      return this.rotationBox.getSelectedItem();
    }
    return null;
  }

  public void zoom()
  {
    scaleAndRotate();
  }

  private void scaleAndRotate()
  {
    if (this.decode_pdf.getDisplayView() == 5)
    {
      this.decode_pdf.setPageParameters(this.scaling, this.commonValues.getCurrentPage(), this.rotation);
      return;
    }
    if ((!this.decode_pdf.isOpen()) && (this.currentCommands.isPDF()))
      return;
    float f1;
    float f2;
    if (this.isSingle)
    {
      f1 = this.scrollPane.getViewport().getWidth() - inset - inset;
      f2 = this.scrollPane.getViewport().getHeight() - inset - inset;
    }
    else
    {
      f1 = this.desktopPane.getSelectedFrame().getWidth();
      f2 = this.desktopPane.getSelectedFrame().getHeight();
    }
    if (this.decode_pdf != null)
    {
      int i = getSelectedComboIndex(252);
      if (this.decode_pdf.getDisplayView() == 5)
      {
        setSelectedComboIndex(252, 0);
        i = 0;
        this.scalingBox.setEnabled(false);
      }
      else if (this.decode_pdf.getDisplayView() != 5)
      {
        this.scalingBox.setEnabled(true);
      }
      int m;
      int n;
      if (i == -1)
      {
        String str = (String)getSelectedComboItem(252);
        float f3 = -1.0F;
        if ((str != null) && (!str.isEmpty()))
        {
          try
          {
            f3 = Float.parseFloat(str);
          }
          catch (Exception localException1)
          {
            f3 = -1.0F;
            m = str.length();
            for (n = 0; n < m; n++)
            {
              int i1 = str.charAt(n);
              if ((((i1 >= 48) && (i1 <= 57) ? 1 : 0) | (i1 == 46 ? 1 : 0)) == 0)
                break;
            }
            if (n > 0)
              str = str.substring(0, n);
            if (f3 == -1.0F)
              try
              {
                f3 = Float.parseFloat(str);
              }
              catch (Exception localException2)
              {
                f3 = -1.0F;
              }
          }
          if (f3 > 1000.0F)
            f3 = 1000.0F;
        }
        if (f3 == -1.0F)
        {
          i = defaultSelection;
          setSelectedComboIndex(252, i);
        }
        else
        {
          this.scaling = this.decode_pdf.getDPIFactory().adjustScaling(f3 / 100.0F);
          setSelectedComboItem(252, String.valueOf(f3));
        }
      }
      int j = this.commonValues.getCurrentPage();
      if (this.isMultiPageTiff)
        j = 1;
      if ((i != -1) || (this.decode_pdf.getDisplayView() == 1) || ((this.decode_pdf.getDisplayView() == 3) && (this.decode_pdf.getPages().getBoolean(Display.BoolValue.TURNOVER_ON))))
      {
        localObject = this.decode_pdf.getPdfPageData();
        n = 0;
        if (this.decode_pdf.getDisplayView() == 3)
          n = ((PdfPageData)localObject).getRotation(j);
        int i2 = (this.rotation + n) % 180 == 90 ? 1 : 0;
        PageOffsets localPageOffsets = (PageOffsets)this.decode_pdf.getExternalHandler(17);
        int k;
        switch (this.decode_pdf.getDisplayView())
        {
        case 4:
          if (i2 != 0)
          {
            k = localPageOffsets.getMaxH() * 2;
            m = localPageOffsets.getMaxW();
          }
          else
          {
            k = localPageOffsets.getMaxW() * 2;
            m = localPageOffsets.getMaxH();
          }
          break;
        case 2:
          if (i2 != 0)
          {
            k = localPageOffsets.getMaxH();
            m = localPageOffsets.getMaxW();
          }
          else
          {
            k = localPageOffsets.getMaxW();
            m = localPageOffsets.getMaxH();
          }
          break;
        case 3:
          int i3;
          if (this.decode_pdf.getPages().getBoolean(Display.BoolValue.SEPARATE_COVER))
          {
            i3 = j / 2 * 2;
            if (this.commonValues.getPageCount() == 2)
              i3 = 1;
          }
          else
          {
            i3 = j;
            if ((i3 & 0x1) == 0)
              i3--;
          }
          if (i2 != 0)
          {
            k = ((PdfPageData)localObject).getCropBoxHeight(i3);
            if ((i3 + 1 > this.commonValues.getPageCount()) || (i3 == 1))
              k *= 2;
            else
              k += ((PdfPageData)localObject).getCropBoxHeight(i3 + 1);
            m = ((PdfPageData)localObject).getCropBoxWidth(i3);
            if ((i3 + 1 <= this.commonValues.getPageCount()) && (m < ((PdfPageData)localObject).getCropBoxWidth(i3 + 1)))
              m = ((PdfPageData)localObject).getCropBoxWidth(i3 + 1);
          }
          else
          {
            k = ((PdfPageData)localObject).getCropBoxWidth(i3);
            if (i3 + 1 > this.commonValues.getPageCount())
              k *= 2;
            else
              k += ((PdfPageData)localObject).getCropBoxWidth(i3 + 1);
            m = ((PdfPageData)localObject).getCropBoxHeight(i3);
            if ((i3 + 1 <= this.commonValues.getPageCount()) && (m < ((PdfPageData)localObject).getCropBoxHeight(i3 + 1)))
              m = ((PdfPageData)localObject).getCropBoxHeight(i3 + 1);
          }
          break;
        default:
          if (i2 != 0)
          {
            k = ((PdfPageData)localObject).getCropBoxHeight(j);
            m = ((PdfPageData)localObject).getCropBoxWidth(j);
          }
          else
          {
            k = ((PdfPageData)localObject).getCropBoxWidth(j);
            m = ((PdfPageData)localObject).getCropBoxHeight(j);
          }
          break;
        }
        if ((this.isSingle) && (this.displayPane != null))
          f1 -= this.displayPane.getDividerSize();
        float f4 = f1 / k;
        float f5 = f2 / m;
        float f6;
        if (f4 < f5)
          f6 = f4;
        else
          f6 = f5;
        if (i != -1)
          if (i < 3)
          {
            if (i == 0)
              this.scaling = f6;
            else if (i == 1)
              this.scaling = f5;
            else if (i == 2)
              this.scaling = f4;
          }
          else
            this.scaling = this.decode_pdf.getDPIFactory().adjustScaling(this.scalingFloatValues[i]);
        if (this.decode_pdf.getDisplayView() == 3)
          this.pageTurnScalingAppropriate = (this.scaling <= f6);
        if (this.thumbscroll != null)
          if ((this.decode_pdf.getDisplayView() == 1) && (this.scaling <= f6))
          {
            this.scrollPane.setVerticalScrollBarPolicy(21);
            this.scrollPane.setHorizontalScrollBarPolicy(31);
            this.thumbscroll.setVisible(true);
          }
          else
          {
            this.scrollPane.setVerticalScrollBarPolicy(20);
            this.scrollPane.setHorizontalScrollBarPolicy(30);
            this.thumbscroll.setVisible(false);
          }
      }
      this.decode_pdf.setPageParameters(this.scaling, j, this.rotation);
      setRotation();
      Object localObject = new Runnable()
      {
        public void run()
        {
          SwingGUI.this.decode_pdf.invalidate();
          SwingGUI.this.decode_pdf.updateUI();
          SwingGUI.this.decode_pdf.validate();
          SwingGUI.this.scrollPane.invalidate();
          SwingGUI.this.scrollPane.updateUI();
          SwingGUI.this.scrollPane.validate();
        }
      };
      SwingUtilities.invokeLater((Runnable)localObject);
    }
  }

  public void snapScalingToDefaults(float paramFloat)
  {
    paramFloat = this.decode_pdf.getDPIFactory().adjustScaling(paramFloat / 100.0F);
    float f1;
    float f2;
    if (this.isSingle)
    {
      f1 = this.scrollPane.getViewport().getWidth() - inset - inset;
      f2 = this.scrollPane.getViewport().getHeight() - inset - inset;
    }
    else
    {
      f1 = this.desktopPane.getWidth();
      f2 = this.desktopPane.getHeight();
    }
    PdfPageData localPdfPageData = this.decode_pdf.getPdfPageData();
    int k = 0;
    if (this.decode_pdf.getDisplayView() == 3)
      k = localPdfPageData.getRotation(this.commonValues.getCurrentPage());
    int m = (this.rotation + k) % 180 == 90 ? 1 : 0;
    PageOffsets localPageOffsets = (PageOffsets)this.decode_pdf.getExternalHandler(17);
    int i;
    int j;
    switch (this.decode_pdf.getDisplayView())
    {
    case 4:
      if (m != 0)
      {
        i = localPageOffsets.getMaxH() * 2;
        j = localPageOffsets.getMaxW();
      }
      else
      {
        i = localPageOffsets.getMaxW() * 2;
        j = localPageOffsets.getMaxH();
      }
      break;
    case 2:
      if (m != 0)
      {
        i = localPageOffsets.getMaxH();
        j = localPageOffsets.getMaxW();
      }
      else
      {
        i = localPageOffsets.getMaxW();
        j = localPageOffsets.getMaxH();
      }
      break;
    case 3:
      int n;
      if (this.decode_pdf.getPages().getBoolean(Display.BoolValue.SEPARATE_COVER))
      {
        n = this.commonValues.getCurrentPage() / 2 * 2;
        if (this.commonValues.getPageCount() == 2)
          n = 1;
      }
      else
      {
        n = this.commonValues.getCurrentPage();
        if ((n & 0x1) == 0)
          n--;
      }
      if (m != 0)
      {
        i = localPdfPageData.getCropBoxHeight(n);
        if ((n + 1 > this.commonValues.getPageCount()) || (n == 1))
          i *= 2;
        else
          i += localPdfPageData.getCropBoxHeight(n + 1);
        j = localPdfPageData.getCropBoxWidth(n);
        if ((n + 1 <= this.commonValues.getPageCount()) && (j < localPdfPageData.getCropBoxWidth(n + 1)))
          j = localPdfPageData.getCropBoxWidth(n + 1);
      }
      else
      {
        i = localPdfPageData.getCropBoxWidth(n);
        if (n + 1 > this.commonValues.getPageCount())
          i *= 2;
        else
          i += localPdfPageData.getCropBoxWidth(n + 1);
        j = localPdfPageData.getCropBoxHeight(n);
        if ((n + 1 <= this.commonValues.getPageCount()) && (j < localPdfPageData.getCropBoxHeight(n + 1)))
          j = localPdfPageData.getCropBoxHeight(n + 1);
      }
      break;
    default:
      if (m != 0)
      {
        i = localPdfPageData.getCropBoxHeight(this.commonValues.getCurrentPage());
        j = localPdfPageData.getCropBoxWidth(this.commonValues.getCurrentPage());
      }
      else
      {
        i = localPdfPageData.getCropBoxWidth(this.commonValues.getCurrentPage());
        j = localPdfPageData.getCropBoxHeight(this.commonValues.getCurrentPage());
      }
      break;
    }
    if ((this.isSingle) && (this.displayPane != null))
      f1 -= this.displayPane.getDividerSize();
    float f3 = f1 / i;
    float f4 = f2 / j;
    float f5;
    if (f3 < f4)
    {
      f5 = f3;
      f3 = -1.0F;
    }
    else
    {
      f5 = f4;
      f4 = -1.0F;
    }
    if ((getSelectedComboIndex(252) != 0) && (((paramFloat < f5 * 1.1D) && (paramFloat > f5 * 0.91D)) || ((f5 > this.scaling) && (f5 < paramFloat)) || ((f5 < this.scaling) && (f5 > paramFloat))))
    {
      setSelectedComboIndex(252, 0);
      this.scaling = f5;
    }
    else if ((f4 != -1.0F) && (getSelectedComboIndex(252) != 1) && (((paramFloat < f4 * 1.1D) && (paramFloat > f4 * 0.91D)) || ((f4 > this.scaling) && (f4 < paramFloat)) || ((f4 < this.scaling) && (f4 > paramFloat))))
    {
      setSelectedComboIndex(252, 1);
      this.scaling = f4;
    }
    else if ((f3 != -1.0F) && (getSelectedComboIndex(252) != 2) && (((paramFloat < f3 * 1.1D) && (paramFloat > f3 * 0.91D)) || ((f3 > this.scaling) && (f3 < paramFloat)) || ((f3 < this.scaling) && (f3 > paramFloat))))
    {
      setSelectedComboIndex(252, 2);
      this.scaling = f3;
    }
    else
    {
      setSelectedComboItem(252, String.valueOf((int)this.decode_pdf.getDPIFactory().removeScaling(paramFloat * 100.0F)));
      this.scaling = paramFloat;
    }
  }

  public void rotate()
  {
    this.rotation = Integer.parseInt((String)getSelectedComboItem(251));
    scaleAndRotate();
    this.decode_pdf.updateUI();
  }

  public void scrollToPage(int paramInt)
  {
    this.commonValues.setCurrentPage(paramInt);
    if (this.commonValues.getCurrentPage() > 0)
    {
      int i = 0;
      int j = 0;
      if (this.decode_pdf.getDisplayView() != 1)
      {
        i = this.decode_pdf.getPages().getYCordForPage(this.commonValues.getCurrentPage(), this.scaling);
        j = 0;
      }
      PdfPageData localPdfPageData = this.decode_pdf.getPdfPageData();
      int k = (int)(localPdfPageData.getCropBoxHeight(this.commonValues.getCurrentPage()) * this.scaling);
      int m = (int)(localPdfPageData.getCropBoxWidth(this.commonValues.getCurrentPage()) * this.scaling);
      int n = j + (m - this.scrollPane.getHorizontalScrollBar().getVisibleAmount()) / 2;
      int i1 = i + (k - this.scrollPane.getVerticalScrollBar().getVisibleAmount()) / 2;
      this.scrollPane.getHorizontalScrollBar().setValue(n);
      this.scrollPane.getVerticalScrollBar().setValue(i1);
    }
    if (this.decode_pdf.getPageCount() > 1)
      setPageLayoutButtonsEnabled(true);
  }

  public void decodePage()
  {
    this.decode_pdf.getPages().setHighlightedImage(null);
    resetRotationBox();
    if (this.thumbnails.isShownOnscreen())
      this.thumbnails.terminateDrawing();
    if (this.thumbnails.isShownOnscreen())
    {
      LinearThread localLinearThread = (LinearThread)this.decode_pdf.getJPedalObject(-1276915978);
      if ((localLinearThread == null) || ((localLinearThread != null) && (!localLinearThread.isAlive())))
        setupThumbnailPanel();
    }
    if (this.decode_pdf.getDisplayView() == 1)
    {
      this.pageCounter2.setForeground(Color.black);
      this.pageCounter2.setText(String.valueOf(this.commonValues.getCurrentPage()));
      this.pageCounter3.setText(new StringBuilder().append(Messages.getMessage("PdfViewerOfLabel.text")).append(' ').append(this.commonValues.getPageCount()).toString());
    }
    int i = new StringBuilder().append("").append(this.commonValues.getPageCount()).toString().length();
    if ((this.decode_pdf.getDisplayView() == 3) || (this.decode_pdf.getDisplayView() == 4))
      i *= 2;
    if (i < 2)
      i = 2;
    if (i > 10)
      i = 10;
    this.pageCounter2.setColumns(i);
    this.pageCounter2.setMaximumSize(this.pageCounter2.getPreferredSize());
    this.navToolBar.invalidate();
    this.navToolBar.doLayout();
    this.tabsNotInitialised = false;
    this.decode_pdf.setExtractionMode(65);
    this.decode_pdf.getTextLines().clearHighlights();
    if (this.decode_pdf.getDisplayView() == 3)
    {
      zoom();
      scrollToPage(this.commonValues.getCurrentPage());
      this.decode_pdf.getPages().decodeOtherPages(this.commonValues.getCurrentPage(), this.commonValues.getPageCount());
      return;
    }
    if ((this.decode_pdf.getDisplayView() == 2) || (this.decode_pdf.getDisplayView() == 4))
    {
      zoom();
      scrollToPage(this.commonValues.getCurrentPage());
      return;
    }
    if (this.decode_pdf.getDisplayView() == 5)
      return;
    resetComboBoxes(false);
    setPageLayoutButtonsEnabled(false);
    Values.setProcessing(true);
    this.decode_pdf.setCursor(Cursor.getPredefinedCursor(3));
    try
    {
      this.statusBar.updateStatus("Decoding Page", 0);
      Object localObject2;
      try
      {
        this.decode_pdf.decodePage(this.commonValues.getCurrentPage());
        this.decode_pdf.waitForDecodingToFinish();
        if (!this.decode_pdf.getPageDecodeStatus(2))
        {
          localObject1 = new StringBuilder().append(Messages.getMessage("PdfViewer.ImageDisplayError")).append(Messages.getMessage("PdfViewer.ImageDisplayError1")).append(Messages.getMessage("PdfViewer.ImageDisplayError2")).append(Messages.getMessage("PdfViewer.ImageDisplayError3")).append(Messages.getMessage("PdfViewer.ImageDisplayError4")).append(Messages.getMessage("PdfViewer.ImageDisplayError5")).append(Messages.getMessage("PdfViewer.ImageDisplayError6")).append(Messages.getMessage("PdfViewer.ImageDisplayError7")).toString();
          showMessageDialog(localObject1);
        }
        if (this.decode_pdf.getPageDecodeStatus(32))
        {
          localObject1 = Messages.getMessage("PdfCustomGui.ttHintingRequired");
          showMessageDialog(localObject1);
        }
        if (this.decode_pdf.getPageDecodeStatus(4))
        {
          localObject1 = new StringBuilder().append("This page contains non-embedded CID fonts \n").append(this.decode_pdf.getPageDecodeStatusReport(4)).append("\nwhich may need mapping to display correctly.\n").append("See http://www.idrsolutions.com/how-do-fonts-work").toString();
          showMessageDialog(localObject1);
        }
        Object localObject1 = this.decode_pdf.getPdfPageData();
        this.mediaW = ((PdfPageData)localObject1).getMediaBoxWidth(this.commonValues.getCurrentPage());
        this.mediaH = ((PdfPageData)localObject1).getMediaBoxHeight(this.commonValues.getCurrentPage());
        this.cropX = ((PdfPageData)localObject1).getCropBoxX(this.commonValues.getCurrentPage());
        this.cropY = ((PdfPageData)localObject1).getCropBoxY(this.commonValues.getCurrentPage());
        this.cropW = ((PdfPageData)localObject1).getCropBoxWidth(this.commonValues.getCurrentPage());
        this.cropH = ((PdfPageData)localObject1).getCropBoxHeight(this.commonValues.getCurrentPage());
        if (this.decode_pdf.getExternalHandler(25) != null)
        {
          localObject2 = this.decode_pdf.getFormRenderer().getFormFactory();
          if (((FormFactory)localObject2).getType() == 1)
          {
            Runnable local27 = new Runnable()
            {
              public void run()
              {
                SwingGUI.this.createUniqueAnnotationIcons();
              }
            };
            SwingUtilities.invokeLater(local27);
          }
          else
          {
            createUniqueAnnotationIcons();
          }
        }
        this.statusBar.updateStatus("Displaying Page", 0);
      }
      catch (Exception localException1)
      {
        System.err.println(new StringBuilder().append(Messages.getMessage("PdfViewerError.Exception")).append(' ').append(localException1).append(' ').append(Messages.getMessage("PdfViewerError.DecodePage")).toString());
        localException1.printStackTrace();
        Values.setProcessing(false);
      }
      if (DecoderOptions.showErrorMessages)
      {
        String str = this.decode_pdf.getPageDecodeReport();
        if (str.contains("java.lang.OutOfMemoryError"))
        {
          str = new StringBuilder().append(Messages.getMessage("PdfViewer.OutOfMemoryDisplayError")).append(Messages.getMessage("PdfViewer.OutOfMemoryDisplayError1")).append(Messages.getMessage("PdfViewer.OutOfMemoryDisplayError2")).append(Messages.getMessage("PdfViewer.OutOfMemoryDisplayError3")).append(Messages.getMessage("PdfViewer.OutOfMemoryDisplayError4")).append(Messages.getMessage("PdfViewer.OutOfMemoryDisplayError5")).toString();
          showMessageDialog(str);
        }
        else if (str.contains("JPeg 2000"))
        {
          str = new StringBuilder().append(Messages.getMessage("PdfViewer.jpeg2000")).append(Messages.getMessage("PdfViewer.jpeg2000_1")).append(Messages.getMessage("PdfViewer.jpeg2000_2")).append(Messages.getMessage("PdfViewer.jpeg2000_3")).append(Messages.getMessage("PdfViewer.jpeg2000_4")).toString();
          localObject2 = new Object[] { "Ok", "Open website help page" };
          int j = showMessageDialog(str, (Object[])localObject2, 0);
          if (j == 1)
            try
            {
              BrowserLauncher.openURL("http://www.idrsolutions.com/additional-jars/");
            }
            catch (Exception localException4)
            {
            }
            catch (Error localError)
            {
            }
        }
      }
      Values.setProcessing(false);
      setViewerTitle(null);
      this.currentCommands.setPageProperties(getSelectedComboItem(251), getSelectedComboItem(252));
      if ((this.decode_pdf.getPageCount() > 0) && (this.thumbnails.isShownOnscreen()) && (this.decode_pdf.getDisplayView() == 1))
        this.thumbnails.generateOtherVisibleThumbnails(this.commonValues.getCurrentPage());
    }
    catch (Exception localException2)
    {
      localException2.printStackTrace();
      Values.setProcessing(false);
      setViewerTitle(null);
    }
    selectBookmark();
    this.statusBar.setProgress(100);
    if (this.useNewLayout)
    {
      try
      {
        Thread.sleep(800L);
      }
      catch (Exception localException3)
      {
      }
      setMultibox(new int[0]);
    }
    resetComboBoxes(true);
    if (this.decode_pdf.getPageCount() > 1)
      setPageLayoutButtonsEnabled(true);
    addFormsListeners();
    if (this.displayPane != null)
      reinitialiseTabs(this.displayPane.getDividerLocation() > startSize);
    this.finishedDecoding = true;
    zoom();
    this.decode_pdf.setCursor(Cursor.getPredefinedCursor(0));
  }

  public void addFormsListeners()
  {
    this.commonValues.setFormsChanged(false);
    int i = 0;
    String str = System.getProperty("org.jpedal.listenforms");
    if (str != null)
      i = 1;
    AcroRenderer localAcroRenderer = this.decode_pdf.getFormRenderer();
    if (localAcroRenderer == null)
      return;
    Object[] arrayOfObject = localAcroRenderer.getFormComponents(null, ReturnValues.FORM_NAMES, this.commonValues.getCurrentPage());
    if (arrayOfObject == null)
    {
      if (i != 0)
        showMessageDialog(Messages.getMessage("PdfViewer.NoFields"));
      return;
    }
    int j = arrayOfObject.length;
    JPanel localJPanel1 = new JPanel();
    Object localObject1;
    Object localObject2;
    if (i != 0)
    {
      localJPanel1.setLayout(new BoxLayout(localJPanel1, 1));
      localObject1 = new JLabel(new StringBuilder().append("This page contains ").append(j).append(" form objects").toString());
      ((JLabel)localObject1).setFont(this.headFont);
      localJPanel1.add((Component)localObject1);
      localJPanel1.add(Box.createRigidArea(new Dimension(10, 10)));
      localObject2 = new JTextPane();
      ((JTextPane)localObject2).setPreferredSize(new Dimension(450, 180));
      ((JTextPane)localObject2).setEditable(false);
      ((JTextPane)localObject2).setText("This provides a simple example of Forms handling. We have added a listener to each form so clicking on it shows the form name.\n\nCode is in addExampleListeners() in org.examples.viewer.Viewer\n\nThis could be easily be extended to interface with a database directly or collect results on an action and write back using itext.\n\nForms have been converted into Swing components and are directly accessible (as is the original data).\n\nIf you don't like the standard SwingSet you can replace with your own set.");
      ((JTextPane)localObject2).setFont(this.textFont);
      localJPanel1.add((Component)localObject2);
      localJPanel1.add(Box.createRigidArea(new Dimension(10, 10)));
    }
    if (i != 0)
    {
      localObject1 = new JDialog((JFrame)null, true);
      ((JDialog)localObject1).setDefaultCloseOperation(2);
      if (this.commonValues.getModeOfOperation() != 1)
      {
        ((JDialog)localObject1).setLocationRelativeTo(null);
        ((JDialog)localObject1).setLocation(this.frame.getLocationOnScreen().x + 10, this.frame.getLocationOnScreen().y + 10);
      }
      localObject2 = new JScrollPane();
      ((JScrollPane)localObject2).getViewport().add(localJPanel1);
      ((JScrollPane)localObject2).setHorizontalScrollBarPolicy(30);
      ((JScrollPane)localObject2).setVerticalScrollBarPolicy(20);
      ((JDialog)localObject1).setSize(500, 500);
      ((JDialog)localObject1).setTitle("List of forms on this page");
      ((JDialog)localObject1).getContentPane().setLayout(new BorderLayout());
      ((JDialog)localObject1).getContentPane().add((Component)localObject2, "Center");
      JPanel localJPanel2 = new JPanel();
      localJPanel2.setLayout(new BorderLayout());
      ((JDialog)localObject1).getContentPane().add(localJPanel2, "South");
      JButton localJButton = new JButton(Messages.getMessage("PdfViewerButton.Close"));
      localJButton.setFont(new Font("SansSerif", 0, 12));
      localJPanel2.add(localJButton, "East");
      localJButton.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent paramAnonymousActionEvent)
        {
          this.val$displayFrame.dispose();
        }
      });
      ((JDialog)localObject1).setVisible(true);
    }
  }

  public void setupThumbnailPanel()
  {
    this.decode_pdf.addExternalHandler(this.thumbnails, 8);
    if (this.isSetup)
      return;
    this.isSetup = true;
    if (this.thumbnails.isShownOnscreen())
    {
      int i = this.decode_pdf.getPageCount();
      this.thumbnails.setupThumbnails(i, this.textFont, Messages.getMessage("PdfViewerPageLabel.text"), this.decode_pdf.getPdfPageData());
      Object[] arrayOfObject = this.thumbnails.getButtons();
      for (int j = 0; j < i; j++)
        ((JButton)arrayOfObject[j]).addActionListener(new PageChanger(j));
      this.thumbnails.addComponentListener();
    }
  }

  public void setBookmarks(boolean paramBoolean)
  {
    int i = this.displayPane.getDividerLocation();
    if ((i == startSize) && (!paramBoolean))
      return;
    if (this.bookmarksGenerated)
      return;
    this.bookmarksGenerated = true;
    org.w3c.dom.Document localDocument = this.decode_pdf.getOutlineAsXML();
    Node localNode = null;
    if (localDocument != null)
      localNode = localDocument.getFirstChild();
    if (localNode != null)
    {
      this.tree.reset(localNode);
      ((JTree)this.tree.getTree()).addTreeSelectionListener(new TreeSelectionListener()
      {
        public void valueChanged(TreeSelectionEvent paramAnonymousTreeSelectionEvent)
        {
          DefaultMutableTreeNode localDefaultMutableTreeNode = SwingGUI.this.tree.getLastSelectedPathComponent();
          if (localDefaultMutableTreeNode == null)
            return;
          JTree localJTree = (JTree)SwingGUI.this.tree.getTree();
          DefaultTreeModel localDefaultTreeModel = (DefaultTreeModel)localJTree.getModel();
          ArrayList localArrayList = new ArrayList();
          SwingGUI.getFlattenedTreeNodes((TreeNode)localDefaultTreeModel.getRoot(), localArrayList);
          localArrayList.remove(0);
          int i = localArrayList.indexOf(localDefaultMutableTreeNode);
          String str = SwingGUI.this.tree.convertNodeIDToRef(i);
          PdfObject localPdfObject = SwingGUI.this.decode_pdf.getOutlineData().getAobj(str);
          if (localPdfObject != null)
            SwingGUI.this.decode_pdf.getFormRenderer().getActionHandler().gotoDest(localPdfObject, 3, 339034948);
        }
      });
    }
    else
    {
      this.tree.reset(null);
    }
  }

  private static void getFlattenedTreeNodes(TreeNode paramTreeNode, List paramList)
  {
    paramList.add(paramTreeNode);
    Enumeration localEnumeration = paramTreeNode.children();
    while (localEnumeration.hasMoreElements())
      getFlattenedTreeNodes((TreeNode)localEnumeration.nextElement(), paramList);
  }

  private void selectBookmark()
  {
    if ((this.decode_pdf.hasOutline()) && (this.tree != null))
      this.tree.selectBookmark();
  }

  public void initStatus()
  {
    this.decode_pdf.setStatusBarObject(this.statusBar);
    if (!this.useNewLayout)
      this.comboBoxBar.add(this.statusBar.getStatusObject());
    else
      setMultibox(new int[0]);
  }

  public void initThumbnails(int paramInt, Vector_Int paramVector_Int)
  {
    this.navOptionsPanel.removeAll();
    if (this.thumbnails.isShownOnscreen())
      this.thumbnails.setupThumbnails(paramInt - 1, paramVector_Int.get(), this.commonValues.getPageCount());
    if (DecoderOptions.isRunningOnMac)
    {
      this.navOptionsPanel.add((Component)this.thumbnails, "Extracted items");
    }
    else
    {
      VTextIcon localVTextIcon = new VTextIcon(this.navOptionsPanel, "Extracted items", 2);
      this.navOptionsPanel.addTab(null, localVTextIcon, (Component)this.thumbnails);
    }
    if (this.sideTabBarOpenByDefault)
      this.displayPane.setDividerLocation(expandedSize);
  }

  public void setCoordText(String paramString)
  {
    this.coords.setText(paramString);
  }

  private JLabel initCoordBox()
  {
    this.coords.setBackground(Color.white);
    this.coords.setOpaque(true);
    if (this.useNewLayout)
    {
      this.coords.setBorder(BorderFactory.createEtchedBorder());
      this.coords.setPreferredSize(this.memoryBar.getPreferredSize());
    }
    else
    {
      this.coords.setPreferredSize(new Dimension(120, 20));
      this.coords.setBorder(BorderFactory.createLineBorder(Color.black, 1));
    }
    this.coords.setText("  X:  Y:   ");
    return this.coords;
  }

  public void hideRedundentNavButtons()
  {
    int i = this.decode_pdf.getPageCount();
    if (this.commonValues.isMultiTiff())
      i = this.commonValues.getPageCount();
    if (((this.decode_pdf.getDisplayView() == 3) && (this.decode_pdf.getPages().getBoolean(Display.BoolValue.SEPARATE_COVER))) || ((this.decode_pdf.getDisplayView() == 4) && ((i & 0x1) == 1)))
      i--;
    if (this.commonValues.getCurrentPage() == 1)
      setBackNavigationButtonsEnabled(false);
    else
      setBackNavigationButtonsEnabled(true);
    if (this.commonValues.getCurrentPage() == i)
      setForwardNavigationButtonsEnabled(false);
    else
      setForwardNavigationButtonsEnabled(true);
    if (this.thumbscroll != null)
      if (this.decode_pdf.getDisplayView() == 1)
      {
        this.scrollPane.setVerticalScrollBarPolicy(21);
        this.scrollPane.setHorizontalScrollBarPolicy(31);
        this.thumbscroll.setVisible(true);
      }
      else if (this.decode_pdf.getDisplayView() == 5)
      {
        this.scrollPane.setVerticalScrollBarPolicy(21);
        this.scrollPane.setHorizontalScrollBarPolicy(31);
        this.thumbscroll.setVisible(false);
      }
      else
      {
        this.scrollPane.setVerticalScrollBarPolicy(20);
        this.scrollPane.setHorizontalScrollBarPolicy(30);
        this.thumbscroll.setVisible(false);
      }
  }

  public void setPageNumber()
  {
    if (SwingUtilities.isEventDispatchThread())
    {
      setPageNumberWorker();
    }
    else
    {
      Runnable local30 = new Runnable()
      {
        public void run()
        {
          SwingGUI.this.setPageNumberWorker();
        }
      };
      SwingUtilities.invokeLater(local30);
    }
  }

  public boolean isMultiPageTiff()
  {
    return this.isMultiPageTiff;
  }

  public void setMultiPageTiff(boolean paramBoolean)
  {
    this.isMultiPageTiff = paramBoolean;
  }

  private void setPageNumberWorker()
  {
    if (this.pageCounter2 == null)
      return;
    if ((!this.decode_pdf.isOpen()) && (!this.isMultiPageTiff))
    {
      this.pageCounter2.setText(" ");
    }
    else
    {
      if ((this.previewOnSingleScroll) && (this.thumbscroll != null))
      {
        this.scrollListener.ignoreChange = true;
        this.thumbscroll.setMaximum(this.decode_pdf.getPageCount());
        this.scrollListener.ignoreChange = true;
        this.thumbscroll.setValue(this.commonValues.getCurrentPage() - 1);
        this.scrollListener.ignoreChange = false;
        if (this.debugThumbnail)
          System.out.println(new StringBuilder().append("setpage=").append(this.commonValues.getCurrentPage()).toString());
      }
      int i = this.commonValues.getCurrentPage();
      if ((this.decode_pdf.getDisplayView() == 3) || (this.decode_pdf.getDisplayView() == 4))
      {
        if (this.decode_pdf.getPageCount() == 2)
        {
          this.pageCounter2.setText("1/2");
        }
        else
        {
          int j;
          if ((this.decode_pdf.getPages().getBoolean(Display.BoolValue.SEPARATE_COVER)) || (this.decode_pdf.getDisplayView() == 4))
          {
            j = i & 0xFFFFFFFE;
            if ((j != this.decode_pdf.getPageCount()) && (j != 0))
              this.pageCounter2.setText(new StringBuilder().append(j).append("/").append(j + 1).toString());
            else
              this.pageCounter2.setText(String.valueOf(i));
          }
          else
          {
            j = i - (1 - (i & 0x1));
            if (j != this.decode_pdf.getPageCount())
              this.pageCounter2.setText(new StringBuilder().append(j).append("/").append(j + 1).toString());
            else
              this.pageCounter2.setText(String.valueOf(i));
          }
        }
      }
      else
        this.pageCounter2.setText(String.valueOf(i));
      this.pageCounter3.setText(new StringBuilder().append(Messages.getMessage("PdfViewerOfLabel.text")).append(' ').append(this.commonValues.getPageCount()).toString());
      hideRedundentNavButtons();
    }
  }

  public int getPageNumber()
  {
    return this.commonValues.getCurrentPage();
  }

  private void createNavbar()
  {
    ArrayList localArrayList = new ArrayList();
    if (this.memoryMonitor == null)
    {
      this.memoryMonitor = new javax.swing.Timer(500, new ActionListener()
      {
        public void actionPerformed(ActionEvent paramAnonymousActionEvent)
        {
          int i = (int)(Runtime.getRuntime().freeMemory() / 1048576L);
          int j = (int)(Runtime.getRuntime().totalMemory() / 1048576L);
          if (SwingGUI.this.finishedDecoding)
            SwingGUI.this.finishedDecoding = false;
          SwingGUI.this.memoryBar.setMaximum(j);
          SwingGUI.this.memoryBar.setValue(j - i);
          SwingGUI.this.memoryBar.setStringPainted(true);
          SwingGUI.this.memoryBar.setString(j - i + "M of " + j + 'M');
        }
      });
      this.memoryMonitor.start();
    }
    if (!this.useNewLayout)
    {
      this.navButtons.add(this.memoryBar, "West");
    }
    else
    {
      this.multibox.setLayout(new BorderLayout());
      if (this.commonValues.getModeOfOperation() != 4)
        this.navButtons.add(this.multibox, "West");
    }
    this.navButtons.add(Box.createHorizontalGlue());
    this.navToolBar.add(Box.createHorizontalGlue());
    addButton(1, Messages.getMessage("PdfViewerNavBar.RewindToStart"), new StringBuilder().append(this.iconLocation).append("start.gif").toString(), 50);
    addButton(1, Messages.getMessage("PdfViewerNavBar.Rewind10"), new StringBuilder().append(this.iconLocation).append("fback.gif").toString(), 51);
    addButton(1, Messages.getMessage("PdfViewerNavBar.Rewind1"), new StringBuilder().append(this.iconLocation).append("back.gif").toString(), 52);
    this.pageCounter1 = new JLabel(Messages.getMessage("PdfViewerPageLabel.text"));
    this.pageCounter1.setOpaque(false);
    this.navToolBar.add(this.pageCounter1);
    this.navToolBar.add(this.pageCounter2);
    this.navToolBar.add(this.pageCounter3);
    addButton(1, Messages.getMessage("PdfViewerNavBar.Forward1"), new StringBuilder().append(this.iconLocation).append("forward.gif").toString(), 53);
    addButton(1, Messages.getMessage("PdfViewerNavBar.Forward10"), new StringBuilder().append(this.iconLocation).append("fforward.gif").toString(), 54);
    addButton(1, Messages.getMessage("PdfViewerNavBar.ForwardLast"), new StringBuilder().append(this.iconLocation).append("end.gif").toString(), 55);
    this.navToolBar.add(Box.createHorizontalGlue());
    if (this.isSingle)
    {
      addButton(2, Messages.getMessage("PageLayoutButton.SinglePage"), new StringBuilder().append(this.iconLocation).append("single.gif").toString(), 57);
      addButton(2, Messages.getMessage("PageLayoutButton.Continuous"), new StringBuilder().append(this.iconLocation).append("continuous.gif").toString(), 58);
      addButton(2, Messages.getMessage("PageLayoutButton.ContinousFacing"), new StringBuilder().append(this.iconLocation).append("continuous_facing.gif").toString(), 59);
      addButton(2, Messages.getMessage("PageLayoutButton.Facing"), new StringBuilder().append(this.iconLocation).append("facing.gif").toString(), 60);
      addButton(2, Messages.getMessage("PageLayoutButton.PageFlow"), new StringBuilder().append(this.iconLocation).append("pageflow.gif").toString(), 62);
    }
    if (this.commonValues.getModeOfOperation() == 4)
      this.topButtons.add(this.pagesToolBar, "East");
    else
      this.navButtons.add(this.pagesToolBar, "East");
    Dimension localDimension = this.pagesToolBar.getPreferredSize();
    if (this.useNewLayout)
      this.multibox.setPreferredSize(localDimension);
    else
      this.memoryBar.setPreferredSize(localDimension);
    boolean[] arrayOfBoolean = new boolean[localArrayList.size()];
    for (int i = 0; i != localArrayList.size(); i++)
      arrayOfBoolean[i] = localArrayList.get(i).equals(Boolean.TRUE);
    if (this.commonValues.getModeOfOperation() == 4)
      this.topButtons.add(this.navToolBar, "Center");
    else
      this.navButtons.add(this.navToolBar, "Center");
  }

  public void setPage(int paramInt)
  {
    if (((this.decode_pdf.getDisplayView() == 3) && (this.decode_pdf.getPages().getBoolean(Display.BoolValue.SEPARATE_COVER))) || ((this.decode_pdf.getDisplayView() == 4) && ((paramInt & 0x1) == 1) && (paramInt != 1)))
      paramInt--;
    else if ((this.decode_pdf.getDisplayView() == 3) && (!this.decode_pdf.getPages().getBoolean(Display.BoolValue.SEPARATE_COVER)) && ((paramInt & 0x1) == 0))
      paramInt--;
    this.commonValues.setCurrentPage(paramInt);
    setPageNumber();
    setThumbnails();
  }

  public void resetPageNav()
  {
    this.pageCounter2.setText("");
    this.pageCounter3.setText("");
  }

  public void setRotation()
  {
    if (this.rotation > 360)
      this.rotation -= 360;
    if (getSelectedComboIndex(251) != this.rotation / 90)
      setSelectedComboIndex(251, this.rotation / 90);
    else if (!Values.isProcessing())
      this.decode_pdf.repaint();
  }

  public void setRotationFromExternal(int paramInt)
  {
    this.rotation = paramInt;
    this.rotationBox.setSelectedIndex(this.rotation / 90);
    if (!Values.isProcessing())
      this.decode_pdf.repaint();
  }

  public void setScalingFromExternal(String paramString)
  {
    if (paramString.startsWith("Fit "))
    {
      this.scalingBox.setSelectedItem(paramString);
    }
    else
    {
      this.scaling = Float.parseFloat(paramString);
      this.scalingBox.setSelectedItem(new StringBuilder().append(paramString).append('%').toString());
    }
    if (!Values.isProcessing())
      this.decode_pdf.repaint();
  }

  public void createMainMenu(boolean paramBoolean)
  {
    this.fileMenu = new JMenu(Messages.getMessage("PdfViewerFileMenu.text"));
    addToMainMenu(this.fileMenu);
    this.openMenu = new JMenu(Messages.getMessage("PdfViewerFileMenuOpen.text"));
    this.openMenu.getPopupMenu().setLightWeightPopupEnabled(!JavaFXHelper.isJavaFXAvailable());
    this.fileMenu.add(this.openMenu);
    addMenuItem(this.openMenu, Messages.getMessage("PdfViewerFileMenuOpen.text"), Messages.getMessage("PdfViewerFileMenuTooltip.open"), 10);
    addMenuItem(this.openMenu, Messages.getMessage("PdfViewerFileMenuOpenurl.text"), Messages.getMessage("PdfViewerFileMenuTooltip.openurl"), 14);
    String str = new StringBuilder().append(this.properties.getValue("Save")).append(this.properties.getValue("Resaveasforms")).append(this.properties.getValue("Find")).toString();
    if ((!str.isEmpty()) && (str.toLowerCase().contains("true")))
      this.fileMenu.addSeparator();
    addMenuItem(this.fileMenu, Messages.getMessage("PdfViewerFileMenuSave.text"), Messages.getMessage("PdfViewerFileMenuTooltip.save"), 5);
    if (paramBoolean)
      addMenuItem(this.fileMenu, Messages.getMessage("PdfViewerFileMenuResaveForms.text"), Messages.getMessage("PdfViewerFileMenuTooltip.saveForms"), 500);
    addMenuItem(this.fileMenu, Messages.getMessage("PdfViewerFileMenuFind.text"), Messages.getMessage("PdfViewerFileMenuTooltip.find"), 12);
    str = this.properties.getValue("Documentproperties");
    if ((!str.isEmpty()) && (str.toLowerCase().equals("true")))
      this.fileMenu.addSeparator();
    addMenuItem(this.fileMenu, Messages.getMessage("PdfViewerFileMenuDocProperties.text"), Messages.getMessage("PdfViewerFileMenuTooltip.props"), 9);
    if (this.commonValues.isEncrypOnClasspath())
      addMenuItem(this.fileMenu, Messages.getMessage("PdfViewerFileMenuSignPDF.text"), Messages.getMessage("PdfViewerFileMenuTooltip.sign"), 512);
    else
      addMenuItem(this.fileMenu, Messages.getMessage("PdfViewerFileMenuSignPDF.text"), Messages.getMessage("PdfViewerFileMenuSignPDF.NotPath"), 512);
    str = this.properties.getValue("Print");
    if ((!str.isEmpty()) && (str.toLowerCase().equals("true")))
      this.fileMenu.addSeparator();
    addMenuItem(this.fileMenu, Messages.getMessage("PdfViewerFileMenuPrint.text"), Messages.getMessage("PdfViewerFileMenuTooltip.print"), 6);
    str = this.properties.getValue("Recentdocuments");
    if ((!str.isEmpty()) && (str.toLowerCase().equals("true")))
    {
      this.fileMenu.addSeparator();
      this.currentCommands.recentDocumentsOption(this.fileMenu);
    }
    str = this.properties.getValue("Exit");
    if ((!str.isEmpty()) && (str.toLowerCase().equals("true")))
      this.fileMenu.addSeparator();
    addMenuItem(this.fileMenu, Messages.getMessage("PdfViewerFileMenuExit.text"), Messages.getMessage("PdfViewerFileMenuTooltip.exit"), 7);
    this.editMenu = new JMenu(Messages.getMessage("PdfViewerEditMenu.text"));
    addToMainMenu(this.editMenu);
    addMenuItem(this.editMenu, Messages.getMessage("PdfViewerEditMenuCopy.text"), Messages.getMessage("PdfViewerEditMenuTooltip.Copy"), 25);
    addMenuItem(this.editMenu, Messages.getMessage("PdfViewerEditMenuSelectall.text"), Messages.getMessage("PdfViewerEditMenuTooltip.Selectall"), 26);
    addMenuItem(this.editMenu, Messages.getMessage("PdfViewerEditMenuDeselectall.text"), Messages.getMessage("PdfViewerEditMenuTooltip.Deselectall"), 27);
    str = this.properties.getValue("Preferences");
    if ((!str.isEmpty()) && (str.toLowerCase().equals("true")))
      this.editMenu.addSeparator();
    addMenuItem(this.editMenu, Messages.getMessage("PdfViewerEditMenuPreferences.text"), Messages.getMessage("PdfViewerEditMenuTooltip.Preferences"), 24);
    this.viewMenu = new JMenu(Messages.getMessage("PdfViewerViewMenu.text"));
    addToMainMenu(this.viewMenu);
    this.goToMenu = new JMenu(Messages.getMessage("GoToViewMenuGoto.text"));
    this.goToMenu.getPopupMenu().setLightWeightPopupEnabled(!JavaFXHelper.isJavaFXAvailable());
    this.viewMenu.add(this.goToMenu);
    addMenuItem(this.goToMenu, Messages.getMessage("GoToViewMenuGoto.FirstPage"), "", 50);
    addMenuItem(this.goToMenu, Messages.getMessage("GoToViewMenuGoto.BackPage"), "", 52);
    addMenuItem(this.goToMenu, Messages.getMessage("GoToViewMenuGoto.ForwardPage"), "", 53);
    addMenuItem(this.goToMenu, Messages.getMessage("GoToViewMenuGoto.LastPage"), "", 55);
    addMenuItem(this.goToMenu, Messages.getMessage("GoToViewMenuGoto.GoTo"), "", 56);
    str = new StringBuilder().append(this.properties.getValue("Previousdocument")).append(this.properties.getValue("Nextdocument")).toString();
    if ((!str.isEmpty()) && (str.toLowerCase().contains("true")))
      this.goToMenu.addSeparator();
    addMenuItem(this.goToMenu, Messages.getMessage("GoToViewMenuGoto.PreviousDoucment"), "", 16);
    addMenuItem(this.goToMenu, Messages.getMessage("GoToViewMenuGoto.NextDoucment"), "", 17);
    if (this.isSingle)
    {
      this.pageLayoutMenu = new JMenu(Messages.getMessage("PageLayoutViewMenu.PageLayout"));
      this.pageLayoutMenu.getPopupMenu().setLightWeightPopupEnabled(!JavaFXHelper.isJavaFXAvailable());
      this.viewMenu.add(this.pageLayoutMenu);
    }
    String[] arrayOfString = { Messages.getMessage("PageLayoutViewMenu.SinglePage"), Messages.getMessage("PageLayoutViewMenu.Continuous"), Messages.getMessage("PageLayoutViewMenu.ContinousFacing"), Messages.getMessage("PageLayoutViewMenu.Facing"), Messages.getMessage("PageLayoutViewMenu.PageFlow") };
    int[] arrayOfInt = { 1, 2, 4, 3, 5 };
    if (this.isSingle)
      initLayoutMenus(this.pageLayoutMenu, arrayOfString, arrayOfInt);
    if (this.properties.getValue("separateCover").equals("true"))
      addMenuItem(this.viewMenu, Messages.getMessage("PdfViewerViewMenuSeparateCover.text"), Messages.getMessage("PdfViewerViewMenuTooltip.separateCover"), 32, true);
    if ((this.properties.getValue("panMode").equals("true")) || (this.properties.getValue("textSelect").equals("true")))
    {
      this.viewMenu.addSeparator();
      if (this.properties.getValue("panMode").equals("true"))
        addMenuItem(this.viewMenu, Messages.getMessage("PdfViewerViewMenuPanMode.text"), Messages.getMessage("PdfViewerViewMenuTooltip.panMode"), 30, true);
      if (this.properties.getValue("textSelect").equals("true"))
        addMenuItem(this.viewMenu, Messages.getMessage("PdfViewerViewMenuTextSelectMode.text"), Messages.getMessage("PdfViewerViewMenuTooltip.textSelect"), 31, true);
      this.viewMenu.addSeparator();
    }
    str = this.properties.getValue("Fullscreen");
    if ((!str.isEmpty()) && (str.toLowerCase().contains("true")))
      this.goToMenu.addSeparator();
    addMenuItem(this.viewMenu, Messages.getMessage("PdfViewerViewMenuFullScreenMode.text"), Messages.getMessage("PdfViewerViewMenuTooltip.fullScreenMode"), 61);
    if (!this.isSingle)
    {
      this.windowMenu = new JMenu(Messages.getMessage("PdfViewerWindowMenu.text"));
      addToMainMenu(this.windowMenu);
      addMenuItem(this.windowMenu, Messages.getMessage("PdfViewerWindowMenuCascade.text"), "", 21);
      addMenuItem(this.windowMenu, Messages.getMessage("PdfViewerWindowMenuTile.text"), "", 22);
    }
    this.exportMenu = new JMenu(Messages.getMessage("PdfViewerExportMenu.text"));
    addToMainMenu(this.exportMenu);
    this.pdfMenu = new JMenu(Messages.getMessage("PdfViewerExportMenuPDF.text"));
    this.pdfMenu.getPopupMenu().setLightWeightPopupEnabled(!JavaFXHelper.isJavaFXAvailable());
    this.exportMenu.add(this.pdfMenu);
    addMenuItem(this.pdfMenu, Messages.getMessage("PdfViewerExportMenuOnePerPage.text"), "", 501);
    addMenuItem(this.pdfMenu, Messages.getMessage("PdfViewerExportMenuNUp.text"), "", 510);
    addMenuItem(this.pdfMenu, Messages.getMessage("PdfViewerExportMenuHandouts.text"), "", 511);
    this.contentMenu = new JMenu(Messages.getMessage("PdfViewerExportMenuContent.text"));
    this.contentMenu.getPopupMenu().setLightWeightPopupEnabled(!JavaFXHelper.isJavaFXAvailable());
    this.exportMenu.add(this.contentMenu);
    addMenuItem(this.contentMenu, Messages.getMessage("PdfViewerExportMenuImages.text"), "", 3);
    addMenuItem(this.contentMenu, Messages.getMessage("PdfViewerExportMenuText.text"), "", 4);
    addMenuItem(this.exportMenu, Messages.getMessage("PdfViewerExportMenuBitmap.text"), "", 2);
    this.pageToolsMenu = new JMenu(Messages.getMessage("PdfViewerPageToolsMenu.text"));
    addToMainMenu(this.pageToolsMenu);
    addMenuItem(this.pageToolsMenu, Messages.getMessage("PdfViewerPageToolsMenuRotate.text"), "", 502);
    addMenuItem(this.pageToolsMenu, Messages.getMessage("PdfViewerPageToolsMenuDelete.text"), "", 503);
    addMenuItem(this.pageToolsMenu, Messages.getMessage("PdfViewerPageToolsMenuAddPage.text"), "", 504);
    addMenuItem(this.pageToolsMenu, Messages.getMessage("PdfViewerPageToolsMenuAddHeaderFooter.text"), "", 506);
    addMenuItem(this.pageToolsMenu, Messages.getMessage("PdfViewerPageToolsMenuStampText.text"), "", 507);
    addMenuItem(this.pageToolsMenu, Messages.getMessage("PdfViewerPageToolsMenuStampImage.text"), "", 508);
    addMenuItem(this.pageToolsMenu, Messages.getMessage("PdfViewerPageToolsMenuSetCrop.text"), "", 509);
    this.helpMenu = new JMenu(Messages.getMessage("PdfViewerHelpMenu.text"));
    addToMainMenu(this.helpMenu);
    addMenuItem(this.helpMenu, Messages.getMessage("PdfViewerHelpMenu.VisitWebsite"), "", 15);
    addMenuItem(this.helpMenu, Messages.getMessage("PdfViewerHelpMenuTip.text"), "", 20);
    addMenuItem(this.helpMenu, Messages.getMessage("PdfViewerHelpMenuUpdates.text"), "", 23);
    addMenuItem(this.helpMenu, Messages.getMessage("PdfViewerHelpMenuabout.text"), Messages.getMessage("PdfViewerHelpMenuTooltip.about"), 1);
    addMenuItem(this.helpMenu, Messages.getMessage("PdfViewerHelpMenuTutorial.text"), "Visit http://www.idrsolutions.com/java-pdf-library-support/", 998);
  }

  public void addToMainMenu(JMenu paramJMenu)
  {
    paramJMenu.getPopupMenu().setLightWeightPopupEnabled(!JavaFXHelper.isJavaFXAvailable());
    this.currentMenu.add(paramJMenu);
  }

  public Container getFrame()
  {
    return this.frame;
  }

  public JToolBar getTopButtonBar()
  {
    return this.topButtons;
  }

  public JToolBar getDisplaySettingsBar()
  {
    return this.comboBoxBar;
  }

  public JMenuBar getMenuBar()
  {
    return this.currentMenu;
  }

  public void showMessageDialog(Object paramObject)
  {
    boolean bool = true;
    if (this.customMessageHandler != null)
      bool = this.customMessageHandler.showMessage(paramObject);
    if (bool)
      JOptionPane.showMessageDialog(this.frame, paramObject);
  }

  public int showMessageDialog(Object paramObject, Object[] paramArrayOfObject, int paramInt)
  {
    int i = 0;
    boolean bool = true;
    if (this.customMessageHandler != null)
      bool = this.customMessageHandler.showMessage(paramObject);
    if (bool)
      i = JOptionPane.showOptionDialog(this.frame, paramObject, "Message", 1, 3, null, paramArrayOfObject, paramArrayOfObject[paramInt]);
    return i;
  }

  public void showMessageDialog(Object paramObject, String paramString, int paramInt)
  {
    boolean bool = true;
    if (this.customMessageHandler != null)
      bool = this.customMessageHandler.showMessage(paramObject);
    if (bool)
      JOptionPane.showMessageDialog(this.frame, paramObject, paramString, paramInt);
  }

  public String showInputDialog(Object paramObject, String paramString, int paramInt)
  {
    String str = null;
    if (this.customMessageHandler != null)
      str = this.customMessageHandler.requestInput(new Object[] { paramObject, paramString, paramString });
    if (str == null)
      return JOptionPane.showInputDialog(this.frame, paramObject, paramString, paramInt);
    return str;
  }

  public String showInputDialog(String paramString)
  {
    String str = null;
    if (this.customMessageHandler != null)
      str = this.customMessageHandler.requestInput(new String[] { paramString });
    if (str == null)
      return JOptionPane.showInputDialog(this.frame, paramString);
    return str;
  }

  public int showOptionDialog(Object paramObject1, String paramString, int paramInt1, int paramInt2, Object paramObject2, Object[] paramArrayOfObject, Object paramObject3)
  {
    int i = -1;
    if (this.customMessageHandler != null)
      i = this.customMessageHandler.requestConfirm(new Object[] { paramObject1, paramString, String.valueOf(paramInt1), String.valueOf(paramInt2), paramObject2, paramArrayOfObject, paramObject3 });
    if (i == -1)
      return JOptionPane.showOptionDialog(this.frame, paramObject1, paramString, paramInt1, paramInt2, (Icon)paramObject2, paramArrayOfObject, paramObject3);
    return i;
  }

  public int showConfirmDialog(String paramString1, String paramString2, int paramInt)
  {
    int i = -1;
    if (this.customMessageHandler != null)
      i = this.customMessageHandler.requestConfirm(new Object[] { paramString1, paramString2, String.valueOf(paramInt) });
    if (i == -1)
      return JOptionPane.showConfirmDialog(this.frame, paramString1, paramString2, paramInt);
    return i;
  }

  public int showOverwriteDialog(String paramString, boolean paramBoolean)
  {
    int j = -1;
    if (this.customMessageHandler != null)
      j = this.customMessageHandler.requestConfirm(new Object[] { paramString, String.valueOf(paramBoolean) });
    if (j != -1)
      return j;
    int i;
    if (paramBoolean)
    {
      Object[] arrayOfObject = { Messages.getMessage("PdfViewerConfirmButton.Yes"), Messages.getMessage("PdfViewerConfirmButton.YesToAll"), Messages.getMessage("PdfViewerConfirmButton.No"), Messages.getMessage("PdfViewerConfirmButton.Cancel") };
      i = JOptionPane.showOptionDialog(this.frame, new StringBuilder().append(paramString).append('\n').append(Messages.getMessage("PdfViewerMessage.FileAlreadyExists")).append('\n').append(Messages.getMessage("PdfViewerMessage.ConfirmResave")).toString(), Messages.getMessage("PdfViewerMessage.Overwrite"), -1, 3, null, arrayOfObject, arrayOfObject[0]);
    }
    else
    {
      i = JOptionPane.showOptionDialog(this.frame, new StringBuilder().append(paramString).append('\n').append(Messages.getMessage("PdfViewerMessage.FileAlreadyExists")).append('\n').append(Messages.getMessage("PdfViewerMessage.ConfirmResave")).toString(), Messages.getMessage("PdfViewerMessage.Overwrite"), 0, 3, null, null, null);
    }
    return i;
  }

  public void showMessageDialog(JTextArea paramJTextArea)
  {
    boolean bool = true;
    if (this.customMessageHandler != null)
      bool = this.customMessageHandler.showMessage(paramJTextArea);
    if (bool)
      JOptionPane.showMessageDialog(this.frame, paramJTextArea);
  }

  public void showItextPopup()
  {
    JEditorPane localJEditorPane = new JEditorPane("text/html", "Itext is not on the classpath.<BR>JPedal includes code to take advantage of itext and<BR>provide additional functionality with options<BR>to spilt pdf files, and resave forms data<BR>\nItext website - <a href=http://itextpdf.com/>http://itextpdf.com</a>");
    localJEditorPane.setEditable(false);
    localJEditorPane.setOpaque(false);
    localJEditorPane.addHyperlinkListener(new HyperlinkListener()
    {
      public void hyperlinkUpdate(HyperlinkEvent paramAnonymousHyperlinkEvent)
      {
        if (paramAnonymousHyperlinkEvent.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED))
          try
          {
            BrowserLauncher.openURL("http://itextpdf.com/");
          }
          catch (IOException localIOException)
          {
            SwingGUI.this.showMessageDialog(Messages.getMessage("PdfViewer.ErrorWebsite"));
          }
      }
    });
    boolean bool = true;
    if (this.customMessageHandler != null)
      bool = this.customMessageHandler.showMessage(localJEditorPane);
    if (bool)
      showMessageDialog(localJEditorPane);
  }

  public void showFirstTimePopup()
  {
    int i = ((this.customMessageHandler != null) && (this.customMessageHandler.showMessage("first time popup"))) || (this.customMessageHandler == null) ? 1 : 0;
    if ((i == 0) || (this.commonValues.getModeOfOperation() == 1))
      return;
    try
    {
      final JPanel localJPanel = new JPanel();
      localJPanel.setLayout(new BoxLayout(localJPanel, 1));
      MouseAdapter local33 = new MouseAdapter()
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
            BrowserLauncher.openURL(Messages.getMessage("PdfViewer.SupportLink.Link"));
          }
          catch (IOException localIOException)
          {
            SwingGUI.this.showMessageDialog(Messages.getMessage("PdfViewer.ErrorWebsite"));
          }
        }
      };
      JLabel localJLabel1 = new JLabel(new ImageIcon(getClass().getResource("/org/jpedal/examples/viewer/res/supportScreenshot.png")));
      localJLabel1.setBorder(BorderFactory.createRaisedBevelBorder());
      localJLabel1.setAlignmentX(0.5F);
      localJLabel1.addMouseListener(local33);
      localJPanel.add(localJLabel1);
      JLabel localJLabel2 = new JLabel(new StringBuilder().append("<html><center><u>").append(Messages.getMessage("PdfViewer.SupportLink.Text1")).append(' ').append(Messages.getMessage("PdfViewer.SupportLink.Text2")).append("</u></html>").toString());
      localJLabel2.setMaximumSize(new Dimension(245, 60));
      localJLabel2.setForeground(Color.BLUE);
      localJLabel2.addMouseListener(local33);
      localJLabel2.setAlignmentX(0.5F);
      localJPanel.add(localJLabel2);
      localJPanel.add(Box.createRigidArea(new Dimension(10, 10)));
      JOptionPane.showMessageDialog(this.frame, localJPanel, Messages.getMessage("PdfViewerTitle.RunningFirstTime"), -1);
    }
    catch (Exception localException)
    {
      System.err.println(Messages.getMessage("PdfViewerFirstRunDialog.Error"));
    }
    catch (Error localError)
    {
      System.err.println(Messages.getMessage("PdfViewerFirstRunDialog.Error"));
    }
  }

  public int showConfirmDialog(Object paramObject, String paramString, int paramInt1, int paramInt2)
  {
    int i = -1;
    if (this.customMessageHandler != null)
      i = this.customMessageHandler.requestConfirm(new Object[] { paramObject, paramString, String.valueOf(paramInt1), String.valueOf(paramInt2) });
    if (i == -1)
      return JOptionPane.showConfirmDialog(this.frame, paramObject, paramString, paramInt1, paramInt2);
    return i;
  }

  public void setDownloadProgress(String paramString, int paramInt)
  {
    this.downloadBar.setProgress(paramString, paramInt);
    if (this.useNewLayout)
      setMultibox(new int[0]);
  }

  public void updateStatusMessage(String paramString)
  {
    this.statusBar.updateStatus(paramString, 0);
  }

  public void resetStatusMessage(String paramString)
  {
    this.statusBar.resetStatus(paramString);
  }

  public void setStatusProgress(int paramInt)
  {
    this.statusBar.setProgress(paramInt);
    if (this.useNewLayout)
      setMultibox(new int[0]);
  }

  public boolean isPDFOutlineVisible()
  {
    return this.navOptionsPanel.isVisible();
  }

  public void setPDFOutlineVisible(boolean paramBoolean)
  {
    this.navOptionsPanel.setVisible(paramBoolean);
  }

  public void setSplitDividerLocation(int paramInt)
  {
    this.displayPane.setDividerLocation(paramInt);
  }

  public int getSplitDividerLocation()
  {
    return this.displayPane.getDividerLocation();
  }

  public Object printDialog(String[] paramArrayOfString, String paramString)
  {
    JDialog localJDialog = new JDialog((JFrame)null, "Print", true);
    localJDialog.setDefaultCloseOperation(2);
    String str = this.properties.getValue("defaultDPI");
    int i = -1;
    if ((str != null) && (!str.isEmpty()))
      try
      {
        str = str.replaceAll("[^0-9]", "");
        i = Integer.parseInt(str);
      }
      catch (Exception localException)
      {
      }
    if (this.printPanel == null)
      this.printPanel = new PrintPanel(paramArrayOfString, paramString, getPaperSizes(), i, getPageNumber(), this.decode_pdf);
    else
      this.printPanel.resetDefaults(paramArrayOfString, paramString, this.commonValues.getPageCount(), this.commonValues.getCurrentPage());
    localJDialog.getContentPane().add(this.printPanel);
    localJDialog.setSize(670, 415);
    localJDialog.setResizable(false);
    localJDialog.setLocationRelativeTo(this.frame);
    localJDialog.setName("printDialog");
    localJDialog.setVisible(true);
    localJDialog.remove(this.printPanel);
    return this.printPanel;
  }

  public PaperSizes getPaperSizes()
  {
    if (this.paperSizes == null)
      this.paperSizes = new PaperSizes(this.properties.getValue("defaultPagesize"));
    return this.paperSizes;
  }

  public void setQualityBoxVisible(boolean paramBoolean)
  {
    if ((this.properties.getValue("Imageopdisplay").toLowerCase().equals("true")) && (this.qualityBox != null) && (this.optimizationLabel != null))
    {
      this.qualityBox.setVisible(paramBoolean);
      this.optimizationLabel.setVisible(paramBoolean);
    }
  }

  private void setThumbnails()
  {
    SwingWorker local34 = new SwingWorker()
    {
      public Object construct()
      {
        if (SwingGUI.this.thumbnails.isShownOnscreen())
        {
          SwingGUI.this.setupThumbnailPanel();
          if (SwingGUI.this.decode_pdf.getDisplayView() == 1)
            SwingGUI.this.thumbnails.generateOtherVisibleThumbnails(SwingGUI.this.commonValues.getCurrentPage());
        }
        return null;
      }
    };
    local34.start();
  }

  public void setSearchText(JTextField paramJTextField)
  {
    this.searchText = paramJTextField;
  }

  public void setResults(SearchList paramSearchList)
  {
    this.results = paramSearchList;
    if ((this.searchInMenu) && (this.results.getResultCount() == 0))
      showMessageDialog(new StringBuilder().append(Messages.getMessage("PdfViewerFileMenuFind.noResultText")).append(" \"").append(paramSearchList.getSearchTerm()).append("\"").toString(), Messages.getMessage("PdfViewerFileMenuFind.noResultTitle"), 1);
  }

  public SearchList getResults()
  {
    return this.results;
  }

  public JToolBar getNavigationBar()
  {
    return this.navButtons;
  }

  public JTabbedPane getSideTabBar()
  {
    return this.navOptionsPanel;
  }

  public ButtonGroup getSearchLayoutGroup()
  {
    return this.searchLayoutGroup;
  }

  public void setSearchFrame(GUISearchWindow paramGUISearchWindow)
  {
    this.searchFrame = paramGUISearchWindow;
  }

  public void removeSearchWindow(boolean paramBoolean)
  {
    this.searchFrame.removeSearchWindow(paramBoolean);
  }

  public void showPreferencesDialog()
  {
    SwingProperties localSwingProperties = new SwingProperties();
    localSwingProperties.setParent(this.frame);
    localSwingProperties.showPreferenceWindow(this);
  }

  public void setFrame(Container paramContainer)
  {
    this.frame = paramContainer;
  }

  public void getRSSBox()
  {
    final JPanel localJPanel1 = new JPanel();
    JPanel localJPanel2 = new JPanel();
    localJPanel2.setLayout(new BoxLayout(localJPanel2, 1));
    JPanel localJPanel3 = new JPanel();
    localJPanel3.setLayout(new BoxLayout(localJPanel3, 0));
    localJPanel3.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    JLabel localJLabel1 = new JLabel("Click on the link below to load a web browser and sign up to our RSS feed.");
    localJLabel1.setAlignmentX(0.0F);
    localJPanel3.add(localJLabel1);
    localJPanel3.add(Box.createHorizontalGlue());
    localJPanel2.add(localJPanel3);
    JPanel localJPanel4 = new JPanel();
    localJPanel4.setLayout(new BoxLayout(localJPanel4, 0));
    localJPanel4.add(Box.createHorizontalGlue());
    localJPanel4.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    final JLabel localJLabel2 = new JLabel("<html><center>http://www.jpedal.org/jpedal.rss");
    localJLabel2.setAlignmentX(0.0F);
    localJLabel2.setForeground(Color.blue);
    localJLabel2.setHorizontalAlignment(0);
    localJLabel2.addMouseListener(new MouseListener()
    {
      public void mouseEntered(MouseEvent paramAnonymousMouseEvent)
      {
        if (SingleDisplay.allowChangeCursor)
          localJPanel1.getTopLevelAncestor().setCursor(new Cursor(12));
        localJLabel2.setText("<html><center><a>http://www.jpedal.org/jpedal.rss</a></center>");
      }

      public void mouseExited(MouseEvent paramAnonymousMouseEvent)
      {
        if (SingleDisplay.allowChangeCursor)
          localJPanel1.getTopLevelAncestor().setCursor(new Cursor(0));
        localJLabel2.setText("<html><center>http://www.jpedal.org/jpedal.rss");
      }

      public void mouseClicked(MouseEvent paramAnonymousMouseEvent)
      {
        try
        {
          BrowserLauncher.openURL("http://www.jpedal.org/jpedal.rss");
        }
        catch (IOException localIOException)
        {
          JPanel localJPanel = new JPanel();
          localJPanel.setLayout(new BoxLayout(localJPanel, 1));
          JLabel localJLabel = new JLabel("Your web browser could not be successfully loaded.  Please copy and paste the URL below, manually into your web browser.");
          localJLabel.setAlignmentX(0.0F);
          localJLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
          JTextArea localJTextArea = new JTextArea("http://www.jpedal.org/jpedal.rss");
          localJTextArea.setEditable(false);
          localJTextArea.setRows(5);
          localJTextArea.setBorder(BorderFactory.createBevelBorder(1));
          localJTextArea.setAlignmentX(0.0F);
          localJPanel.add(localJLabel);
          localJPanel.add(localJTextArea);
          SwingGUI.this.showMessageDialog(localJPanel, "Error loading web browser", -1);
        }
      }

      public void mousePressed(MouseEvent paramAnonymousMouseEvent)
      {
      }

      public void mouseReleased(MouseEvent paramAnonymousMouseEvent)
      {
      }
    });
    localJPanel4.add(localJLabel2);
    localJPanel4.add(Box.createHorizontalGlue());
    localJPanel2.add(localJPanel4);
    JLabel localJLabel3 = new JLabel(new ImageIcon(getClass().getResource("/org/jpedal/examples/viewer/res/rss.png")));
    localJLabel3.setBorder(BorderFactory.createBevelBorder(1));
    JPanel localJPanel5 = new JPanel();
    localJPanel5.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    localJPanel5.setLayout(new BoxLayout(localJPanel5, 0));
    localJPanel5.add(Box.createHorizontalGlue());
    localJPanel5.add(localJLabel3);
    localJPanel5.add(Box.createHorizontalGlue());
    localJPanel1.setLayout(new BoxLayout(localJPanel1, 1));
    localJPanel1.add(localJPanel2);
    localJPanel1.add(localJPanel5);
    showMessageDialog(localJPanel1, "Subscribe to JPedal RSS Feed", -1);
  }

  private void loadProperties()
  {
    try
    {
      Component[] arrayOfComponent = this.comboBoxBar.getComponents();
      String str2 = this.properties.getValue("sideTabBarCollapseLength");
      if (!str2.isEmpty())
      {
        i = Integer.parseInt(str2);
        startSize = i;
        reinitialiseTabs(false);
      }
      str2 = this.properties.getValue("sideTabBarExpandLength");
      if (!str2.isEmpty())
      {
        i = Integer.parseInt(str2);
        expandedSize = i;
        reinitialiseTabs(false);
      }
      str2 = this.properties.getValue("ShowMenubar");
      boolean bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
      this.currentMenu.setEnabled(bool);
      this.currentMenu.setVisible(bool);
      str2 = this.properties.getValue("ShowButtons");
      bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
      this.topButtons.setEnabled(bool);
      this.topButtons.setVisible(bool);
      str2 = this.properties.getValue("ShowDisplayoptions");
      bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
      this.comboBoxBar.setEnabled(bool);
      this.comboBoxBar.setVisible(bool);
      str2 = this.properties.getValue("ShowNavigationbar");
      bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
      this.navButtons.setEnabled(bool);
      this.navButtons.setVisible(bool);
      if (this.displayPane != null)
      {
        str2 = this.properties.getValue("ShowSidetabbar");
        bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
        if (!bool)
          this.displayPane.setDividerSize(0);
        else
          this.displayPane.setDividerSize(5);
        this.displayPane.getLeftComponent().setEnabled(bool);
        this.displayPane.getLeftComponent().setVisible(bool);
      }
      str2 = this.properties.getValue("Firstbottom");
      bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
      this.first.setEnabled(bool);
      this.first.setVisible(bool);
      str2 = this.properties.getValue("Back10bottom");
      bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
      this.fback.setEnabled(bool);
      this.fback.setVisible(bool);
      str2 = this.properties.getValue("Backbottom");
      bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
      this.back.setEnabled(bool);
      this.back.setVisible(bool);
      str2 = this.properties.getValue("Gotobottom");
      bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
      this.pageCounter1.setEnabled(bool);
      this.pageCounter1.setVisible(bool);
      this.pageCounter2.setEnabled(bool);
      this.pageCounter2.setVisible(bool);
      this.pageCounter3.setEnabled(bool);
      this.pageCounter3.setVisible(bool);
      str2 = this.properties.getValue("Forwardbottom");
      bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
      this.forward.setEnabled(bool);
      this.forward.setVisible(bool);
      str2 = this.properties.getValue("Forward10bottom");
      bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
      this.fforward.setEnabled(bool);
      this.fforward.setVisible(bool);
      str2 = this.properties.getValue("Lastbottom");
      bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
      this.end.setEnabled(bool);
      this.end.setVisible(bool);
      str2 = this.properties.getValue("Singlebottom");
      bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
      this.singleButton.setVisible(bool);
      str2 = this.properties.getValue("Continuousbottom");
      bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
      this.continuousButton.setVisible(bool);
      str2 = this.properties.getValue("Continuousfacingbottom");
      bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
      this.continuousFacingButton.setVisible(bool);
      str2 = this.properties.getValue("Facingbottom");
      bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
      this.facingButton.setVisible(bool);
      str2 = this.properties.getValue("PageFlowbottom");
      bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
      this.pageFlowButton.setVisible(bool);
      str2 = this.properties.getValue("Memorybottom");
      bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
      this.memoryBar.setEnabled(bool);
      this.memoryBar.setVisible(bool);
      str2 = this.properties.getValue("Scalingdisplay");
      bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
      this.scalingBox.setEnabled(bool);
      this.scalingBox.setVisible(bool);
      for (int i = 0; i != arrayOfComponent.length; i++)
        if (((arrayOfComponent[i] instanceof JLabel)) && (((JLabel)arrayOfComponent[i]).getText().equals(Messages.getMessage("PdfViewerToolbarScaling.text"))))
        {
          arrayOfComponent[i].setEnabled(bool);
          arrayOfComponent[i].setVisible(bool);
        }
      str2 = this.properties.getValue("Rotationdisplay");
      bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
      this.rotationBox.setEnabled(bool);
      this.rotationBox.setVisible(bool);
      for (i = 0; i != arrayOfComponent.length; i++)
        if (((arrayOfComponent[i] instanceof JLabel)) && (((JLabel)arrayOfComponent[i]).getText().equals(Messages.getMessage("PdfViewerToolbarRotation.text"))))
        {
          arrayOfComponent[i].setEnabled(bool);
          arrayOfComponent[i].setVisible(bool);
        }
      str2 = this.properties.getValue("Imageopdisplay");
      bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
      if (this.qualityBox != null)
      {
        this.qualityBox.setVisible(bool);
        this.qualityBox.setEnabled(bool);
      }
      for (i = 0; i != arrayOfComponent.length; i++)
        if (((arrayOfComponent[i] instanceof JLabel)) && (((JLabel)arrayOfComponent[i]).getText().equals(Messages.getMessage("PdfViewerToolbarImageOp.text"))))
        {
          arrayOfComponent[i].setVisible(bool);
          arrayOfComponent[i].setEnabled(bool);
        }
      str2 = this.properties.getValue("Progressdisplay");
      bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
      this.statusBar.setEnabled(bool);
      this.statusBar.setVisible(bool);
      str2 = this.properties.getValue("Downloadprogressdisplay");
      bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
      this.downloadBar.setEnabled(bool);
      this.downloadBar.setVisible(bool);
      str2 = this.properties.getValue("Openfilebutton");
      bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
      this.openButton.setEnabled(bool);
      this.openButton.setVisible(bool);
      str2 = this.properties.getValue("Printbutton");
      bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
      this.printButton.setEnabled(bool);
      this.printButton.setVisible(bool);
      str2 = this.properties.getValue("Searchbutton");
      bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
      this.searchButton.setEnabled(bool);
      this.searchButton.setVisible(bool);
      str2 = this.properties.getValue("Propertiesbutton");
      bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
      this.docPropButton.setEnabled(bool);
      this.docPropButton.setVisible(bool);
      str2 = this.properties.getValue("Aboutbutton");
      bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
      this.infoButton.setEnabled(bool);
      this.infoButton.setVisible(bool);
      str2 = this.properties.getValue("Snapshotbutton");
      bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
      this.snapshotButton.setEnabled(bool);
      this.snapshotButton.setVisible(bool);
      str2 = this.properties.getValue("Helpbutton");
      bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
      this.helpButton.setEnabled(bool);
      this.helpButton.setVisible(bool);
      str2 = this.properties.getValue("RSSbutton");
      bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
      this.rssButton.setEnabled(bool);
      this.rssButton.setVisible(bool);
      str2 = this.properties.getValue("CursorButton");
      bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
      this.cursor.setEnabled(bool);
      this.cursor.setVisible(bool);
      str2 = this.properties.getValue("MouseModeButton");
      bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
      this.mouseMode.setEnabled(bool);
      this.mouseMode.setVisible(bool);
      if (DecoderOptions.isRunningOnMac)
      {
        bool = (this.properties.getValue("Pagetab").toLowerCase().equals("true")) && (this.navOptionsPanel.getTabCount() != 0);
        for (i = 0; i < this.navOptionsPanel.getTabCount(); i++)
          if ((this.navOptionsPanel.getTitleAt(i).equals(this.pageTitle)) && (!bool))
            this.navOptionsPanel.remove(i);
        bool = (this.properties.getValue("Bookmarkstab").toLowerCase().equals("true")) && (this.navOptionsPanel.getTabCount() != 0);
        for (i = 0; i < this.navOptionsPanel.getTabCount(); i++)
          if ((this.navOptionsPanel.getTitleAt(i).equals(this.bookmarksTitle)) && (!bool))
            this.navOptionsPanel.remove(i);
        bool = (this.properties.getValue("Layerstab").toLowerCase().equals("true")) && (this.navOptionsPanel.getTabCount() != 0);
        for (i = 0; i < this.navOptionsPanel.getTabCount(); i++)
          if ((this.navOptionsPanel.getTitleAt(i).equals(this.layersTitle)) && (!bool))
            this.navOptionsPanel.remove(i);
        bool = (this.properties.getValue("Signaturestab").toLowerCase().equals("true")) && (this.navOptionsPanel.getTabCount() != 0);
        for (i = 0; i < this.navOptionsPanel.getTabCount(); i++)
          if ((this.navOptionsPanel.getTitleAt(i).equals(this.signaturesTitle)) && (!bool))
            this.navOptionsPanel.remove(i);
      }
      else
      {
        bool = (this.properties.getValue("Pagetab").toLowerCase().equals("true")) && (this.navOptionsPanel.getTabCount() != 0);
        for (i = 0; i < this.navOptionsPanel.getTabCount(); i++)
          if ((this.navOptionsPanel.getIconAt(i).toString().equals(this.pageTitle)) && (!bool))
            this.navOptionsPanel.remove(i);
        bool = (this.properties.getValue("Bookmarkstab").toLowerCase().equals("true")) && (this.navOptionsPanel.getTabCount() != 0);
        for (i = 0; i < this.navOptionsPanel.getTabCount(); i++)
          if ((this.navOptionsPanel.getIconAt(i).toString().equals(this.bookmarksTitle)) && (!bool))
            this.navOptionsPanel.remove(i);
        bool = (this.properties.getValue("Layerstab").toLowerCase().equals("true")) && (this.navOptionsPanel.getTabCount() != 0);
        for (i = 0; i < this.navOptionsPanel.getTabCount(); i++)
          if ((this.navOptionsPanel.getIconAt(i).toString().equals(this.layersTitle)) && (!bool))
            this.navOptionsPanel.remove(i);
        bool = (this.properties.getValue("Signaturestab").toLowerCase().equals("true")) && (this.navOptionsPanel.getTabCount() != 0);
        for (i = 0; i < this.navOptionsPanel.getTabCount(); i++)
          if ((this.navOptionsPanel.getIconAt(i).toString().equals(this.signaturesTitle)) && (!bool))
            this.navOptionsPanel.remove(i);
      }
      if (this.fileMenu != null)
      {
        str2 = this.properties.getValue("FileMenu");
        bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
        this.fileMenu.setEnabled(bool);
        this.fileMenu.setVisible(bool);
        str2 = this.properties.getValue("OpenMenu");
        bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
        this.openMenu.setEnabled(bool);
        this.openMenu.setVisible(bool);
        str2 = this.properties.getValue("Open");
        bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
        this.open.setEnabled(bool);
        this.open.setVisible(bool);
        str2 = this.properties.getValue("Openurl");
        bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
        this.openUrl.setEnabled(bool);
        this.openUrl.setVisible(bool);
        str2 = this.properties.getValue("Save");
        bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
        this.save.setEnabled(bool);
        this.save.setVisible(bool);
        str2 = this.properties.getValue("Resaveasforms");
        bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
        this.reSaveAsForms.setEnabled(bool);
        this.reSaveAsForms.setVisible(bool);
        str2 = this.properties.getValue("Find");
        bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
        this.find.setEnabled(bool);
        this.find.setVisible(bool);
        str2 = this.properties.getValue("Documentproperties");
        bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
        this.documentProperties.setEnabled(bool);
        this.documentProperties.setVisible(bool);
        if (this.signPDF != null)
        {
          str2 = this.properties.getValue("Signpdf");
          bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
          this.signPDF.setEnabled(this.commonValues.isEncrypOnClasspath());
          this.signPDF.setVisible(bool);
        }
        str2 = this.properties.getValue("Print");
        bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
        this.print.setEnabled(bool);
        this.print.setVisible(bool);
        str2 = this.properties.getValue("Recentdocuments");
        bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
        this.currentCommands.enableRecentDocuments(bool);
        str2 = this.properties.getValue("Exit");
        bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
        this.exit.setEnabled(bool);
        this.exit.setVisible(bool);
        str2 = this.properties.getValue("EditMenu");
        bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
        this.editMenu.setEnabled(bool);
        this.editMenu.setVisible(bool);
        str2 = this.properties.getValue("Copy");
        bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
        this.copy.setEnabled(bool);
        this.copy.setVisible(bool);
        str2 = this.properties.getValue("Selectall");
        bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
        this.selectAll.setEnabled(bool);
        this.selectAll.setVisible(bool);
        str2 = this.properties.getValue("Deselectall");
        bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
        this.deselectAll.setEnabled(bool);
        this.deselectAll.setVisible(bool);
        str2 = this.properties.getValue("Preferences");
        bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true")) && (!this.properties.getValue("readOnly").toLowerCase().equals("true"));
        this.preferences.setEnabled(bool);
        this.preferences.setVisible(bool);
        str2 = this.properties.getValue("ViewMenu");
        bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
        this.viewMenu.setEnabled(bool);
        this.viewMenu.setVisible(bool);
        str2 = this.properties.getValue("GotoMenu");
        bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
        this.goToMenu.setEnabled(bool);
        this.goToMenu.setVisible(bool);
        str2 = this.properties.getValue("Firstpage");
        bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
        this.firstPage.setEnabled(bool);
        this.firstPage.setVisible(bool);
        str2 = this.properties.getValue("Backpage");
        bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
        this.backPage.setEnabled(bool);
        this.backPage.setVisible(bool);
        str2 = this.properties.getValue("Forwardpage");
        bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
        this.forwardPage.setEnabled(bool);
        this.forwardPage.setVisible(bool);
        str2 = this.properties.getValue("Lastpage");
        bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
        this.lastPage.setEnabled(bool);
        this.lastPage.setVisible(bool);
        str2 = this.properties.getValue("Goto");
        bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
        this.goTo.setEnabled(bool);
        this.goTo.setVisible(bool);
        str2 = this.properties.getValue("Previousdocument");
        bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
        this.previousDocument.setEnabled(bool);
        this.previousDocument.setVisible(bool);
        str2 = this.properties.getValue("Nextdocument");
        bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
        this.nextDocument.setEnabled(bool);
        this.nextDocument.setVisible(bool);
        if (this.pageLayoutMenu != null)
        {
          str2 = this.properties.getValue("PagelayoutMenu");
          bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
          this.pageLayoutMenu.setEnabled(bool);
          this.pageLayoutMenu.setVisible(bool);
        }
        if (this.single != null)
        {
          str2 = this.properties.getValue("Single");
          bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
          this.single.setEnabled(bool);
          this.single.setVisible(bool);
        }
        if (this.continuous != null)
        {
          str2 = this.properties.getValue("Continuous");
          bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
          this.continuous.setEnabled(bool);
          this.continuous.setVisible(bool);
        }
        if (this.facing != null)
        {
          str2 = this.properties.getValue("Facing");
          bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
          this.facing.setEnabled(bool);
          this.facing.setVisible(bool);
        }
        if (this.continuousFacing != null)
        {
          str2 = this.properties.getValue("Continuousfacing");
          bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
          this.continuousFacing.setEnabled(bool);
          this.continuousFacing.setVisible(bool);
        }
        if (this.pageFlow != null)
        {
          str2 = this.properties.getValue("PageFlow");
          bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
          this.pageFlow.setEnabled(bool);
          this.pageFlow.setVisible(bool);
        }
        if (this.textSelect != null)
        {
          str2 = this.properties.getValue("textSelect");
          bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
          this.textSelect.setEnabled(bool);
          this.textSelect.setVisible(bool);
        }
        if (this.separateCover != null)
        {
          str2 = this.properties.getValue("separateCover");
          bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
          this.separateCover.setEnabled(bool);
          this.separateCover.setVisible(bool);
        }
        if (this.panMode != null)
        {
          str2 = this.properties.getValue("panMode");
          bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
          this.panMode.setEnabled(bool);
          this.panMode.setVisible(bool);
        }
        if (this.fullscreen != null)
        {
          str2 = this.properties.getValue("Fullscreen");
          bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
          this.fullscreen.setEnabled(bool);
          this.fullscreen.setVisible(bool);
        }
        if (this.windowMenu != null)
        {
          str2 = this.properties.getValue("WindowMenu");
          bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
          this.windowMenu.setEnabled(bool);
          this.windowMenu.setVisible(bool);
          str2 = this.properties.getValue("Cascade");
          bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
          this.cascade.setEnabled(bool);
          this.cascade.setVisible(bool);
          str2 = this.properties.getValue("Tile");
          bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
          this.tile.setEnabled(bool);
          this.tile.setVisible(bool);
        }
        str2 = this.properties.getValue("ExportMenu");
        bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
        this.exportMenu.setEnabled(bool);
        this.exportMenu.setVisible(bool);
        str2 = this.properties.getValue("PdfMenu");
        bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
        this.pdfMenu.setEnabled(bool);
        this.pdfMenu.setVisible(bool);
        str2 = this.properties.getValue("Oneperpage");
        bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
        this.onePerPage.setEnabled(bool);
        this.onePerPage.setVisible(bool);
        str2 = this.properties.getValue("Nup");
        bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
        this.nup.setEnabled(bool);
        this.nup.setVisible(bool);
        str2 = this.properties.getValue("Handouts");
        bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
        this.handouts.setEnabled(bool);
        this.handouts.setVisible(bool);
        str2 = this.properties.getValue("ContentMenu");
        bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
        this.contentMenu.setEnabled(bool);
        this.contentMenu.setVisible(bool);
        str2 = this.properties.getValue("Images");
        bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
        this.images.setEnabled(bool);
        this.images.setVisible(bool);
        str2 = this.properties.getValue("Text");
        bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
        this.text.setEnabled(bool);
        this.text.setVisible(bool);
        str2 = this.properties.getValue("Bitmap");
        bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
        this.bitmap.setEnabled(bool);
        this.bitmap.setVisible(bool);
        str2 = this.properties.getValue("PagetoolsMenu");
        bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
        this.pageToolsMenu.setEnabled(bool);
        this.pageToolsMenu.setVisible(bool);
        str2 = this.properties.getValue("Rotatepages");
        bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
        this.rotatePages.setEnabled(bool);
        this.rotatePages.setVisible(bool);
        str2 = this.properties.getValue("Deletepages");
        bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
        this.deletePages.setEnabled(bool);
        this.deletePages.setVisible(bool);
        str2 = this.properties.getValue("Addpage");
        bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
        this.addPage.setEnabled(bool);
        this.addPage.setVisible(bool);
        str2 = this.properties.getValue("Addheaderfooter");
        bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
        this.addHeaderFooter.setEnabled(bool);
        this.addHeaderFooter.setVisible(bool);
        str2 = this.properties.getValue("Stamptext");
        bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
        this.stampText.setEnabled(bool);
        this.stampText.setVisible(bool);
        str2 = this.properties.getValue("Stampimage");
        bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
        this.stampImage.setEnabled(bool);
        this.stampImage.setVisible(bool);
        str2 = this.properties.getValue("Crop");
        bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
        this.crop.setEnabled(bool);
        this.crop.setVisible(bool);
        str2 = this.properties.getValue("HelpMenu");
        bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
        this.helpMenu.setEnabled(bool);
        this.helpMenu.setVisible(bool);
        str2 = this.properties.getValue("Visitwebsite");
        bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
        this.visitWebsite.setEnabled(bool);
        this.visitWebsite.setVisible(bool);
        str2 = this.properties.getValue("Tipoftheday");
        bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
        this.tipOfTheDay.setEnabled(bool);
        this.tipOfTheDay.setVisible(bool);
        str2 = this.properties.getValue("Checkupdates");
        bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
        this.checkUpdates.setEnabled(bool);
        this.checkUpdates.setVisible(bool);
        str2 = this.properties.getValue("About");
        bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
        this.about.setEnabled(bool);
        this.about.setVisible(bool);
        str2 = this.properties.getValue("Helpforum");
        bool = (!str2.isEmpty()) && (str2.toLowerCase().equals("true"));
        this.helpForum.setEnabled(bool);
        this.helpForum.setVisible(bool);
        for (i = 0; i != this.fileMenu.getMenuComponentCount(); i++)
          if (this.fileMenu.getMenuComponent(i).isVisible())
          {
            if (!(this.fileMenu.getMenuComponent(i) instanceof JSeparator))
              break;
            this.fileMenu.remove(this.fileMenu.getMenuComponent(i));
            break;
          }
        for (i = 0; i != this.editMenu.getMenuComponentCount(); i++)
          if (this.editMenu.getMenuComponent(i).isVisible())
          {
            if (!(this.editMenu.getMenuComponent(i) instanceof JSeparator))
              break;
            this.editMenu.remove(this.editMenu.getMenuComponent(i));
            break;
          }
        for (i = 0; i != this.viewMenu.getMenuComponentCount(); i++)
          if (this.viewMenu.getMenuComponent(i).isVisible())
          {
            if (!(this.viewMenu.getMenuComponent(i) instanceof JSeparator))
              break;
            this.viewMenu.remove(this.viewMenu.getMenuComponent(i));
            break;
          }
        for (i = 0; i != this.goToMenu.getMenuComponentCount(); i++)
          if (this.goToMenu.getMenuComponent(i).isVisible())
          {
            if (!(this.goToMenu.getMenuComponent(i) instanceof JSeparator))
              break;
            this.goToMenu.remove(this.goToMenu.getMenuComponent(i));
            break;
          }
      }
      checkButtonSeparators();
    }
    catch (Exception localException1)
    {
      localException1.printStackTrace();
    }
    String str1 = this.properties.getValue("daysLeft");
    if ((str1.length() == 0) || (Integer.parseInt(str1) != dx))
    {
      if ((dx < 14) && (dx > 0))
        ShowGUIMessage.showstaticGUIMessage(new StringBuffer(new StringBuilder().append("You have ").append(dx).append(" days left on your trial").toString()), "JPedal trial");
      this.properties.setValue("daysLeft", new StringBuilder().append("").append(dx).toString());
      try
      {
        this.properties.writeDoc();
      }
      catch (Exception localException2)
      {
        localException2.printStackTrace();
      }
    }
    if (dx < 1)
    {
      ShowGUIMessage.showstaticGUIMessage(new StringBuffer("Your trial has expired"), "JPedal trial");
      System.exit(1);
    }
  }

  public void alterProperty(String paramString, boolean paramBoolean)
  {
    Component[] arrayOfComponent = this.comboBoxBar.getComponents();
    if (paramString.equals("ShowMenubar"))
    {
      this.currentMenu.setEnabled(paramBoolean);
      this.currentMenu.setVisible(paramBoolean);
      this.properties.setValue("ShowMenubar", String.valueOf(paramBoolean));
    }
    if (paramString.equals("ShowButtons"))
    {
      this.topButtons.setEnabled(paramBoolean);
      this.topButtons.setVisible(paramBoolean);
      this.properties.setValue("ShowButtons", String.valueOf(paramBoolean));
    }
    if (paramString.equals("ShowDisplayoptions"))
    {
      this.comboBoxBar.setEnabled(paramBoolean);
      this.comboBoxBar.setVisible(paramBoolean);
      this.properties.setValue("ShowDisplayoptions", String.valueOf(paramBoolean));
    }
    if (paramString.equals("ShowNavigationbar"))
    {
      this.navButtons.setEnabled(paramBoolean);
      this.navButtons.setVisible(paramBoolean);
      this.properties.setValue("ShowNavigationbar", String.valueOf(paramBoolean));
    }
    if ((this.isSingle) && (paramString.equals("ShowSidetabbar")))
    {
      if (!paramBoolean)
        this.displayPane.setDividerSize(0);
      else
        this.displayPane.setDividerSize(5);
      this.displayPane.getLeftComponent().setEnabled(paramBoolean);
      this.displayPane.getLeftComponent().setVisible(paramBoolean);
      this.properties.setValue("ShowSidetabbar", String.valueOf(paramBoolean));
    }
    if (paramString.equals("Firstbottom"))
    {
      this.first.setEnabled(paramBoolean);
      this.first.setVisible(paramBoolean);
    }
    if (paramString.equals("Back10bottom"))
    {
      this.fback.setEnabled(paramBoolean);
      this.fback.setVisible(paramBoolean);
    }
    if (paramString.equals("Backbottom"))
    {
      this.back.setEnabled(paramBoolean);
      this.back.setVisible(paramBoolean);
    }
    if (paramString.equals("Gotobottom"))
    {
      this.pageCounter1.setEnabled(paramBoolean);
      this.pageCounter1.setVisible(paramBoolean);
      this.pageCounter2.setEnabled(paramBoolean);
      this.pageCounter2.setVisible(paramBoolean);
      this.pageCounter3.setEnabled(paramBoolean);
      this.pageCounter3.setVisible(paramBoolean);
    }
    if (paramString.equals("Forwardbottom"))
    {
      this.forward.setEnabled(paramBoolean);
      this.forward.setVisible(paramBoolean);
    }
    if (paramString.equals("Forward10bottom"))
    {
      this.fforward.setEnabled(paramBoolean);
      this.fforward.setVisible(paramBoolean);
    }
    if (paramString.equals("Lastbottom"))
    {
      this.end.setEnabled(paramBoolean);
      this.end.setVisible(paramBoolean);
    }
    if (paramString.equals("Singlebottom"))
      this.singleButton.setVisible(paramBoolean);
    if (paramString.equals("Continuousbottom"))
      this.continuousButton.setVisible(paramBoolean);
    if (paramString.equals("Continuousfacingbottom"))
      this.continuousFacingButton.setVisible(paramBoolean);
    if (paramString.equals("Facingbottom"))
      this.facingButton.setVisible(paramBoolean);
    if (paramString.equals("PageFlowbottom"))
      this.pageFlowButton.setVisible(paramBoolean);
    if (paramString.equals("Memorybottom"))
    {
      this.memoryBar.setEnabled(paramBoolean);
      this.memoryBar.setVisible(paramBoolean);
    }
    int i;
    if (paramString.equals("Scalingdisplay"))
    {
      this.scalingBox.setEnabled(paramBoolean);
      this.scalingBox.setVisible(paramBoolean);
      for (i = 0; i != arrayOfComponent.length; i++)
        if (((arrayOfComponent[i] instanceof JLabel)) && (((JLabel)arrayOfComponent[i]).getText().equals(Messages.getMessage("PdfViewerToolbarScaling.text"))))
        {
          arrayOfComponent[i].setEnabled(paramBoolean);
          arrayOfComponent[i].setVisible(paramBoolean);
          this.properties.setValue("Scalingdisplay", String.valueOf(paramBoolean));
        }
    }
    if (paramString.equals("Rotationdisplay"))
    {
      this.rotationBox.setEnabled(paramBoolean);
      this.rotationBox.setVisible(paramBoolean);
      for (i = 0; i != arrayOfComponent.length; i++)
        if (((arrayOfComponent[i] instanceof JLabel)) && (((JLabel)arrayOfComponent[i]).getText().equals(Messages.getMessage("PdfViewerToolbarRotation.text"))))
        {
          arrayOfComponent[i].setEnabled(paramBoolean);
          arrayOfComponent[i].setVisible(paramBoolean);
          this.properties.setValue("Rotationdisplay", String.valueOf(paramBoolean));
        }
    }
    if (paramString.equals("Imageopdisplay"))
    {
      this.qualityBox.setVisible(paramBoolean);
      this.qualityBox.setEnabled(paramBoolean);
      for (i = 0; i != arrayOfComponent.length; i++)
        if (((arrayOfComponent[i] instanceof JLabel)) && (((JLabel)arrayOfComponent[i]).getText().equals(Messages.getMessage("PdfViewerToolbarImageOp.text"))))
        {
          arrayOfComponent[i].setVisible(paramBoolean);
          arrayOfComponent[i].setEnabled(paramBoolean);
          this.properties.setValue("Imageopdisplay", String.valueOf(paramBoolean));
        }
    }
    if (paramString.equals("Progressdisplay"))
    {
      this.statusBar.setEnabled(paramBoolean);
      this.statusBar.setVisible(paramBoolean);
      this.properties.setValue("Progressdisplay", String.valueOf(paramBoolean));
    }
    if (paramString.equals("Downloadprogressdisplay"))
    {
      this.downloadBar.setEnabled(paramBoolean);
      this.downloadBar.setVisible(paramBoolean);
      this.properties.setValue("Downloadprogressdisplay", String.valueOf(paramBoolean));
    }
    if (paramString.equals("Openfilebutton"))
    {
      this.openButton.setEnabled(paramBoolean);
      this.openButton.setVisible(paramBoolean);
    }
    if (paramString.equals("Printbutton"))
    {
      this.printButton.setEnabled(paramBoolean);
      this.printButton.setVisible(paramBoolean);
    }
    if (paramString.equals("Searchbutton"))
    {
      this.searchButton.setEnabled(paramBoolean);
      this.searchButton.setVisible(paramBoolean);
    }
    if (paramString.equals("Propertiesbutton"))
    {
      this.docPropButton.setEnabled(paramBoolean);
      this.docPropButton.setVisible(paramBoolean);
    }
    if (paramString.equals("Aboutbutton"))
    {
      this.infoButton.setEnabled(paramBoolean);
      this.infoButton.setVisible(paramBoolean);
    }
    if (paramString.equals("Snapshotbutton"))
    {
      this.snapshotButton.setEnabled(paramBoolean);
      this.snapshotButton.setVisible(paramBoolean);
    }
    if (paramString.equals("Helpbutton"))
    {
      this.helpButton.setEnabled(paramBoolean);
      this.helpButton.setVisible(paramBoolean);
    }
    if (paramString.equals("RSSbutton"))
    {
      this.rssButton.setEnabled(paramBoolean);
      this.rssButton.setVisible(paramBoolean);
    }
    if (paramString.equals("CursorButton"))
    {
      this.cursor.setEnabled(paramBoolean);
      this.cursor.setVisible(paramBoolean);
    }
    if (paramString.equals("MouseModeButton"))
    {
      this.mouseMode.setEnabled(paramBoolean);
      this.mouseMode.setVisible(paramBoolean);
    }
    if (DecoderOptions.isRunningOnMac)
    {
      if ((paramString.equals("Pagetab")) && (this.navOptionsPanel.getTabCount() != 0))
        for (i = 0; i < this.navOptionsPanel.getTabCount(); i++)
          if ((this.navOptionsPanel.getTitleAt(i).equals(this.pageTitle)) && (!paramBoolean))
            this.navOptionsPanel.remove(i);
      if ((paramString.equals("Bookmarkstab")) && (this.navOptionsPanel.getTabCount() != 0))
        for (i = 0; i < this.navOptionsPanel.getTabCount(); i++)
          if ((this.navOptionsPanel.getTitleAt(i).equals(this.bookmarksTitle)) && (!paramBoolean))
            this.navOptionsPanel.remove(i);
      if ((paramString.equals("Layerstab")) && (this.navOptionsPanel.getTabCount() != 0))
        for (i = 0; i < this.navOptionsPanel.getTabCount(); i++)
          if ((this.navOptionsPanel.getTitleAt(i).equals(this.layersTitle)) && (!paramBoolean))
            this.navOptionsPanel.remove(i);
      if ((paramString.equals("Signaturestab")) && (this.navOptionsPanel.getTabCount() != 0))
        for (i = 0; i < this.navOptionsPanel.getTabCount(); i++)
          if ((this.navOptionsPanel.getTitleAt(i).equals(this.signaturesTitle)) && (!paramBoolean))
            this.navOptionsPanel.remove(i);
    }
    else
    {
      if ((paramString.equals("Pagetab")) && (this.navOptionsPanel.getTabCount() != 0))
        for (i = 0; i < this.navOptionsPanel.getTabCount(); i++)
          if ((this.navOptionsPanel.getIconAt(i).toString().equals(this.pageTitle)) && (!paramBoolean))
            this.navOptionsPanel.remove(i);
      if ((paramString.equals("Bookmarkstab")) && (this.navOptionsPanel.getTabCount() != 0))
        for (i = 0; i < this.navOptionsPanel.getTabCount(); i++)
          if ((this.navOptionsPanel.getIconAt(i).toString().equals(this.bookmarksTitle)) && (!paramBoolean))
            this.navOptionsPanel.remove(i);
      if ((paramString.equals("Layerstab")) && (this.navOptionsPanel.getTabCount() != 0))
        for (i = 0; i < this.navOptionsPanel.getTabCount(); i++)
          if ((this.navOptionsPanel.getIconAt(i).toString().equals(this.layersTitle)) && (!paramBoolean))
            this.navOptionsPanel.remove(i);
      if ((paramString.equals("Signaturestab")) && (this.navOptionsPanel.getTabCount() != 0))
        for (i = 0; i < this.navOptionsPanel.getTabCount(); i++)
          if ((this.navOptionsPanel.getIconAt(i).toString().equals(this.signaturesTitle)) && (!paramBoolean))
            this.navOptionsPanel.remove(i);
    }
    if (paramString.equals("FileMenu"))
    {
      this.fileMenu.setEnabled(paramBoolean);
      this.fileMenu.setVisible(paramBoolean);
    }
    if (paramString.equals("OpenMenu"))
    {
      this.openMenu.setEnabled(paramBoolean);
      this.openMenu.setVisible(paramBoolean);
    }
    if (paramString.equals("Open"))
    {
      this.open.setEnabled(paramBoolean);
      this.open.setVisible(paramBoolean);
    }
    if (paramString.equals("Openurl"))
    {
      this.openUrl.setEnabled(paramBoolean);
      this.openUrl.setVisible(paramBoolean);
    }
    if (paramString.equals("Save"))
    {
      this.save.setEnabled(paramBoolean);
      this.save.setVisible(paramBoolean);
    }
    if ((paramString.equals("Resaveasforms")) && (this.reSaveAsForms != null))
    {
      this.reSaveAsForms.setEnabled(paramBoolean);
      this.reSaveAsForms.setVisible(paramBoolean);
    }
    if (paramString.equals("Find"))
    {
      this.find.setEnabled(paramBoolean);
      this.find.setVisible(paramBoolean);
    }
    if (paramString.equals("Documentproperties"))
    {
      this.documentProperties.setEnabled(paramBoolean);
      this.documentProperties.setVisible(paramBoolean);
    }
    if (paramString.equals("Signpdf"))
    {
      this.signPDF.setEnabled(paramBoolean);
      this.signPDF.setVisible(paramBoolean);
    }
    if (paramString.equals("Print"))
    {
      this.print.setEnabled(paramBoolean);
      this.print.setVisible(paramBoolean);
    }
    if (paramString.equals("Recentdocuments"))
      this.currentCommands.enableRecentDocuments(paramBoolean);
    if (paramString.equals("Exit"))
    {
      this.exit.setEnabled(paramBoolean);
      this.exit.setVisible(paramBoolean);
    }
    if (paramString.equals("EditMenu"))
    {
      this.editMenu.setEnabled(paramBoolean);
      this.editMenu.setVisible(paramBoolean);
    }
    if (paramString.equals("Copy"))
    {
      this.copy.setEnabled(paramBoolean);
      this.copy.setVisible(paramBoolean);
    }
    if (paramString.equals("Selectall"))
    {
      this.selectAll.setEnabled(paramBoolean);
      this.selectAll.setVisible(paramBoolean);
    }
    if (paramString.equals("Deselectall"))
    {
      this.deselectAll.setEnabled(paramBoolean);
      this.deselectAll.setVisible(paramBoolean);
    }
    if (paramString.equals("Preferences"))
    {
      this.preferences.setEnabled(paramBoolean);
      this.preferences.setVisible(paramBoolean);
    }
    if (paramString.equals("ViewMenu"))
    {
      this.viewMenu.setEnabled(paramBoolean);
      this.viewMenu.setVisible(paramBoolean);
    }
    if (paramString.equals("GotoMenu"))
    {
      this.goToMenu.setEnabled(paramBoolean);
      this.goToMenu.setVisible(paramBoolean);
    }
    if (paramString.equals("Firstpage"))
    {
      this.firstPage.setEnabled(paramBoolean);
      this.firstPage.setVisible(paramBoolean);
    }
    if (paramString.equals("Backpage"))
    {
      this.backPage.setEnabled(paramBoolean);
      this.backPage.setVisible(paramBoolean);
    }
    if (paramString.equals("Forwardpage"))
    {
      this.forwardPage.setEnabled(paramBoolean);
      this.forwardPage.setVisible(paramBoolean);
    }
    if (paramString.equals("Lastpage"))
    {
      this.lastPage.setEnabled(paramBoolean);
      this.lastPage.setVisible(paramBoolean);
    }
    if (paramString.equals("Goto"))
    {
      this.goTo.setEnabled(paramBoolean);
      this.goTo.setVisible(paramBoolean);
    }
    if (paramString.equals("Previousdocument"))
    {
      this.previousDocument.setEnabled(paramBoolean);
      this.previousDocument.setVisible(paramBoolean);
    }
    if (paramString.equals("Nextdocument"))
    {
      this.nextDocument.setEnabled(paramBoolean);
      this.nextDocument.setVisible(paramBoolean);
    }
    if (this.isSingle)
    {
      if (paramString.equals("PagelayoutMenu"))
      {
        this.pageLayoutMenu.setEnabled(paramBoolean);
        this.pageLayoutMenu.setVisible(paramBoolean);
      }
      if (paramString.equals("Single"))
      {
        this.single.setEnabled(paramBoolean);
        this.single.setVisible(paramBoolean);
      }
      if (paramString.equals("Continuous"))
      {
        this.continuous.setEnabled(paramBoolean);
        this.continuous.setVisible(paramBoolean);
      }
      if (paramString.equals("Facing"))
      {
        this.facing.setEnabled(paramBoolean);
        this.facing.setVisible(paramBoolean);
      }
      if (paramString.equals("Continuousfacing"))
      {
        this.continuousFacing.setEnabled(paramBoolean);
        this.continuousFacing.setVisible(paramBoolean);
      }
      if ((this.pageFlow != null) && (paramString.equals("PageFlow")))
      {
        this.pageFlow.setEnabled(paramBoolean);
        this.pageFlow.setVisible(paramBoolean);
      }
    }
    if ((this.panMode != null) && (paramString.equals("panMode")))
    {
      this.panMode.setEnabled(paramBoolean);
      this.panMode.setVisible(paramBoolean);
    }
    if ((this.textSelect != null) && (paramString.equals("textSelect")))
    {
      this.textSelect.setEnabled(paramBoolean);
      this.textSelect.setVisible(paramBoolean);
    }
    if (paramString.equals("Fullscreen"))
    {
      this.fullscreen.setEnabled(paramBoolean);
      this.fullscreen.setVisible(paramBoolean);
    }
    if ((this.separateCover != null) && (paramString.equals("separateCover")))
    {
      this.separateCover.setEnabled(paramBoolean);
      this.separateCover.setVisible(paramBoolean);
    }
    if (this.windowMenu != null)
    {
      if (paramString.equals("WindowMenu"))
      {
        this.windowMenu.setEnabled(paramBoolean);
        this.windowMenu.setVisible(paramBoolean);
      }
      if (paramString.equals("Cascade"))
      {
        this.cascade.setEnabled(paramBoolean);
        this.cascade.setVisible(paramBoolean);
      }
      if (paramString.equals("Tile"))
      {
        this.tile.setEnabled(paramBoolean);
        this.tile.setVisible(paramBoolean);
      }
    }
    if (paramString.equals("ExportMenu"))
    {
      this.exportMenu.setEnabled(paramBoolean);
      this.exportMenu.setVisible(paramBoolean);
    }
    if (paramString.equals("PdfMenu"))
    {
      this.pdfMenu.setEnabled(paramBoolean);
      this.pdfMenu.setVisible(paramBoolean);
    }
    if (paramString.equals("Oneperpage"))
    {
      this.onePerPage.setEnabled(paramBoolean);
      this.onePerPage.setVisible(paramBoolean);
    }
    if (paramString.equals("Nup"))
    {
      this.nup.setEnabled(paramBoolean);
      this.nup.setVisible(paramBoolean);
    }
    if (paramString.equals("Handouts"))
    {
      this.handouts.setEnabled(paramBoolean);
      this.handouts.setVisible(paramBoolean);
    }
    if (paramString.equals("ContentMenu"))
    {
      this.contentMenu.setEnabled(paramBoolean);
      this.contentMenu.setVisible(paramBoolean);
    }
    if (paramString.equals("Images"))
    {
      this.images.setEnabled(paramBoolean);
      this.images.setVisible(paramBoolean);
    }
    if (paramString.equals("Text"))
    {
      this.text.setEnabled(paramBoolean);
      this.text.setVisible(paramBoolean);
    }
    if (paramString.equals("Bitmap"))
    {
      this.bitmap.setEnabled(paramBoolean);
      this.bitmap.setVisible(paramBoolean);
    }
    if (paramString.equals("PagetoolsMenu"))
    {
      this.pageToolsMenu.setEnabled(paramBoolean);
      this.pageToolsMenu.setVisible(paramBoolean);
    }
    if (paramString.equals("Rotatepages"))
    {
      this.rotatePages.setEnabled(paramBoolean);
      this.rotatePages.setVisible(paramBoolean);
    }
    if (paramString.equals("Deletepages"))
    {
      this.deletePages.setEnabled(paramBoolean);
      this.deletePages.setVisible(paramBoolean);
    }
    if (paramString.equals("Addpage"))
    {
      this.addPage.setEnabled(paramBoolean);
      this.addPage.setVisible(paramBoolean);
    }
    if (paramString.equals("Addheaderfooter"))
    {
      this.addHeaderFooter.setEnabled(paramBoolean);
      this.addHeaderFooter.setVisible(paramBoolean);
    }
    if (paramString.equals("Stamptext"))
    {
      this.stampText.setEnabled(paramBoolean);
      this.stampText.setVisible(paramBoolean);
    }
    if (paramString.equals("Stampimage"))
    {
      this.stampImage.setEnabled(paramBoolean);
      this.stampImage.setVisible(paramBoolean);
    }
    if (paramString.equals("Crop"))
    {
      this.crop.setEnabled(paramBoolean);
      this.crop.setVisible(paramBoolean);
    }
    if (paramString.equals("HelpMenu"))
    {
      this.helpMenu.setEnabled(paramBoolean);
      this.helpMenu.setVisible(paramBoolean);
    }
    if (paramString.equals("Visitwebsite"))
    {
      this.visitWebsite.setEnabled(paramBoolean);
      this.visitWebsite.setVisible(paramBoolean);
    }
    if (paramString.equals("Tipoftheday"))
    {
      this.tipOfTheDay.setEnabled(paramBoolean);
      this.tipOfTheDay.setVisible(paramBoolean);
    }
    if (paramString.equals("Checkupdates"))
    {
      this.checkUpdates.setEnabled(paramBoolean);
      this.checkUpdates.setVisible(paramBoolean);
    }
    if (paramString.equals("About"))
    {
      this.about.setEnabled(paramBoolean);
      this.about.setVisible(paramBoolean);
    }
    if (paramString.equals("Helpforum"))
    {
      this.helpForum.setEnabled(paramBoolean);
      this.helpForum.setVisible(paramBoolean);
    }
    checkButtonSeparators();
  }

  private void checkButtonSeparators()
  {
    int i = 0;
    int j = 0;
    JSeparator localJSeparator = null;
    for (int k = 0; k != this.topButtons.getComponentCount(); k++)
      if ((this.topButtons.getComponent(k) instanceof JSeparator))
      {
        if (localJSeparator == null)
        {
          localJSeparator = (JSeparator)this.topButtons.getComponent(k);
        }
        else
        {
          if ((i == 0) || (j == 0))
            localJSeparator.setVisible(false);
          else
            localJSeparator.setVisible(true);
          i = (i != 0) || (j != 0) ? 1 : 0;
          j = 0;
          localJSeparator = (JSeparator)this.topButtons.getComponent(k);
        }
      }
      else if (this.topButtons.getComponent(k).isVisible())
        if (localJSeparator == null)
          i = 1;
        else
          j = 1;
    if (localJSeparator != null)
      if ((i == 0) || (j == 0))
        localJSeparator.setVisible(false);
      else
        localJSeparator.setVisible(true);
  }

  public void getHelpBox()
  {
    final JPanel localJPanel1 = new JPanel();
    JPanel localJPanel2 = new JPanel();
    localJPanel2.setLayout(new BoxLayout(localJPanel2, 1));
    JPanel localJPanel3 = new JPanel();
    localJPanel3.setLayout(new BoxLayout(localJPanel3, 0));
    localJPanel3.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    JLabel localJLabel1 = new JLabel("<html><p>Please click on this link for lots of tutorials and documentation</p>");
    localJLabel1.setAlignmentX(0.0F);
    localJPanel3.add(localJLabel1);
    localJPanel3.add(Box.createHorizontalGlue());
    localJPanel2.add(localJPanel3);
    JPanel localJPanel4 = new JPanel();
    localJPanel4.setLayout(new BoxLayout(localJPanel4, 0));
    localJPanel4.add(Box.createHorizontalGlue());
    localJPanel4.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    final JLabel localJLabel2 = new JLabel("<html><center>http://www.idrsolutions.com/java-pdf-library-support/");
    localJLabel2.setAlignmentX(0.0F);
    localJLabel2.setForeground(Color.blue);
    localJLabel2.setHorizontalAlignment(0);
    localJLabel2.addMouseListener(new MouseListener()
    {
      public void mouseEntered(MouseEvent paramAnonymousMouseEvent)
      {
        if (SingleDisplay.allowChangeCursor)
          localJPanel1.getTopLevelAncestor().setCursor(new Cursor(12));
        localJLabel2.setText("<html><center><a>http://www.idrsolutions.com/java-pdf-library-support/</a></center>");
      }

      public void mouseExited(MouseEvent paramAnonymousMouseEvent)
      {
        if (SingleDisplay.allowChangeCursor)
          localJPanel1.getTopLevelAncestor().setCursor(new Cursor(0));
        localJLabel2.setText("<html><center>http://www.idrsolutions.com/java-pdf-library-support/");
      }

      public void mouseClicked(MouseEvent paramAnonymousMouseEvent)
      {
        try
        {
          BrowserLauncher.openURL("http://www.idrsolutions.com/java-pdf-library-support/");
        }
        catch (IOException localIOException)
        {
          JPanel localJPanel = new JPanel();
          localJPanel.setLayout(new BoxLayout(localJPanel, 1));
          JLabel localJLabel = new JLabel("Your web browser could not be successfully loaded.  Please copy and paste the URL below, manually into your web browser.");
          localJLabel.setAlignmentX(0.0F);
          localJLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
          JTextArea localJTextArea = new JTextArea("http://www.idrsolutions.com/java-pdf-library-support/");
          localJTextArea.setEditable(false);
          localJTextArea.setRows(5);
          localJTextArea.setBorder(BorderFactory.createBevelBorder(1));
          localJTextArea.setAlignmentX(0.0F);
          localJPanel.add(localJLabel);
          localJPanel.add(localJTextArea);
          SwingGUI.this.showMessageDialog(localJPanel, "Error loading web browser", -1);
        }
      }

      public void mousePressed(MouseEvent paramAnonymousMouseEvent)
      {
      }

      public void mouseReleased(MouseEvent paramAnonymousMouseEvent)
      {
      }
    });
    localJPanel4.add(localJLabel2);
    localJPanel4.add(Box.createHorizontalGlue());
    localJPanel2.add(localJPanel4);
    localJPanel1.setLayout(new BoxLayout(localJPanel1, 1));
    localJPanel1.add(localJPanel2);
    showMessageDialog(localJPanel1, "JPedal Tutorials and documentation", -1);
  }

  public void dispose()
  {
    super.dispose();
    this.pageTitle = null;
    this.bookmarksTitle = null;
    this.signaturesTitle = null;
    this.layersTitle = null;
    this.layoutGroup = null;
    this.searchLayoutGroup = null;
    this.borderGroup = null;
    this.currentCommandListener = null;
    if (this.topButtons != null)
      this.topButtons.removeAll();
    this.topButtons = null;
    if (this.navButtons != null)
      this.navButtons.removeAll();
    this.navButtons = null;
    if (this.comboBoxBar != null)
      this.comboBoxBar.removeAll();
    this.comboBoxBar = null;
    if (this.currentMenu != null)
      this.currentMenu.removeAll();
    this.currentMenu = null;
    if (this.coords != null)
      this.coords.removeAll();
    this.coords = null;
    if (this.frame != null)
      this.frame.removeAll();
    this.frame = null;
    if (this.desktopPane != null)
      this.desktopPane.removeAll();
    this.desktopPane = null;
    if (this.navOptionsPanel != null)
      this.navOptionsPanel.removeAll();
    this.navOptionsPanel = null;
    if (this.scrollPane != null)
      this.scrollPane.removeAll();
    this.scrollPane = null;
    this.headFont = null;
    this.textFont = null;
    this.statusBar = null;
    this.downloadBar = null;
    this.pageCounter2 = null;
    this.pageCounter3 = null;
    this.optimizationLabel = null;
    if (this.signaturesTree != null)
    {
      this.signaturesTree.setCellRenderer(null);
      this.signaturesTree.removeAll();
    }
    this.signaturesTree = null;
    if (this.layersPanel != null)
      this.layersPanel.removeAll();
    this.layersPanel = null;
    this.user_dir = null;
    if (this.navToolBar != null)
      this.navToolBar.removeAll();
    this.navToolBar = null;
    if (this.pagesToolBar != null)
      this.pagesToolBar.removeAll();
    this.pagesToolBar = null;
    this.nextSearch = null;
    this.previousSearch = null;
    this.layersObject = null;
    if (this.memoryMonitor != null)
      this.memoryMonitor.stop();
  }

  public Map getHotspots()
  {
    return this.objs;
  }

  public Point convertPDFto2D(int paramInt1, int paramInt2)
  {
    float f = getScaling();
    int i = getPDFDisplayInset();
    int j = getRotation();
    int k;
    if (this.decode_pdf.getDisplayView() == 1)
      if (j == 90)
      {
        k = paramInt1 - this.cropY;
        paramInt1 = paramInt2 - this.cropX;
        paramInt2 = k;
      }
      else if (j == 180)
      {
        paramInt1 = -paramInt1 - (this.cropW + this.cropX);
        paramInt2 -= this.cropY;
      }
      else if (j == 270)
      {
        k = -(this.cropH + this.cropY) - paramInt1;
        paramInt1 = this.cropW + this.cropX + paramInt2;
        paramInt2 = k;
      }
      else
      {
        paramInt1 -= this.cropX;
        paramInt2 = this.cropH + this.cropY - paramInt2;
      }
    paramInt1 = (int)(paramInt1 * f);
    paramInt2 = (int)(paramInt2 * f);
    if (this.decode_pdf.getPageAlignment() == 2)
    {
      k = this.decode_pdf.getBounds().width;
      int m = this.decode_pdf.getPDFWidth();
      if (this.decode_pdf.getDisplayView() != 1)
        m = (int)this.decode_pdf.getMaximumSize().getWidth();
      if (k > m)
        paramInt1 += (k - m) / 2;
    }
    paramInt1 += i;
    paramInt2 += i;
    return new Point(paramInt1, paramInt2);
  }

  public boolean getFormsDirtyFlag()
  {
    return this.commonValues.isFormsChanged();
  }

  public void setFormsDirtyFlag(boolean paramBoolean)
  {
    this.commonValues.setFormsChanged(paramBoolean);
  }

  public int getCurrentPage()
  {
    return this.commonValues.getCurrentPage();
  }

  public boolean getPageTurnScalingAppropriate()
  {
    return this.pageTurnScalingAppropriate;
  }

  public boolean getDragLeft()
  {
    return this.dragLeft;
  }

  public boolean getDragTop()
  {
    return this.dragTop;
  }

  public void setDragCorner(int paramInt)
  {
    this.dragLeft = ((paramInt == 998) || (paramInt == 996) || (paramInt == 995));
    this.dragTop = ((paramInt == 996) || (paramInt == 997));
  }

  public void setCursor(int paramInt)
  {
    this.decode_pdf.setCursor(getCursor(paramInt));
  }

  public BufferedImage getCursorImageForFX(int paramInt)
  {
    switch (paramInt)
    {
    case 1:
      try
      {
        return ImageIO.read(getURLForImage(new StringBuilder().append(this.iconLocation).append("grab32.png").toString()));
      }
      catch (Exception localException1)
      {
        return null;
      }
    case 2:
      try
      {
        return ImageIO.read(getURLForImage(new StringBuilder().append(this.iconLocation).append("grabbing32.png").toString()));
      }
      catch (Exception localException2)
      {
        return null;
      }
    }
    return null;
  }

  public Cursor getCursor(int paramInt)
  {
    Toolkit localToolkit;
    Image localImage;
    switch (paramInt)
    {
    case 1:
      if (this.grabCursor == null)
      {
        localToolkit = Toolkit.getDefaultToolkit();
        localImage = localToolkit.getImage(getURLForImage(new StringBuilder().append(this.iconLocation).append("grab32.png").toString()));
        this.grabCursor = localToolkit.createCustomCursor(localImage, new Point(8, 8), "grab");
      }
      return this.grabCursor;
    case 2:
      if (this.grabbingCursor == null)
      {
        localToolkit = Toolkit.getDefaultToolkit();
        localImage = localToolkit.getImage(getURLForImage(new StringBuilder().append(this.iconLocation).append("grabbing32.png").toString()));
        this.grabbingCursor = localToolkit.createCustomCursor(localImage, new Point(8, 8), "grabbing");
      }
      return this.grabbingCursor;
    case 4:
      if (this.panCursor == null)
      {
        localToolkit = Toolkit.getDefaultToolkit();
        localImage = localToolkit.getImage(getURLForImage(new StringBuilder().append(this.iconLocation).append("pan32.png").toString()));
        this.panCursor = localToolkit.createCustomCursor(localImage, new Point(10, 10), "pan");
      }
      return this.panCursor;
    case 5:
      if (this.panCursorL == null)
      {
        localToolkit = Toolkit.getDefaultToolkit();
        localImage = localToolkit.getImage(getURLForImage(new StringBuilder().append(this.iconLocation).append("panl32.png").toString()));
        this.panCursorL = localToolkit.createCustomCursor(localImage, new Point(11, 10), "panl");
      }
      return this.panCursorL;
    case 6:
      if (this.panCursorTL == null)
      {
        localToolkit = Toolkit.getDefaultToolkit();
        localImage = localToolkit.getImage(getURLForImage(new StringBuilder().append(this.iconLocation).append("pantl32.png").toString()));
        this.panCursorTL = localToolkit.createCustomCursor(localImage, new Point(10, 10), "pantl");
      }
      return this.panCursorTL;
    case 7:
      if (this.panCursorT == null)
      {
        localToolkit = Toolkit.getDefaultToolkit();
        localImage = localToolkit.getImage(getURLForImage(new StringBuilder().append(this.iconLocation).append("pant32.png").toString()));
        this.panCursorT = localToolkit.createCustomCursor(localImage, new Point(10, 11), "pant");
      }
      return this.panCursorT;
    case 8:
      if (this.panCursorTR == null)
      {
        localToolkit = Toolkit.getDefaultToolkit();
        localImage = localToolkit.getImage(getURLForImage(new StringBuilder().append(this.iconLocation).append("pantr32.png").toString()));
        this.panCursorTR = localToolkit.createCustomCursor(localImage, new Point(10, 10), "pantr");
      }
      return this.panCursorTR;
    case 9:
      if (this.panCursorR == null)
      {
        localToolkit = Toolkit.getDefaultToolkit();
        localImage = localToolkit.getImage(getURLForImage(new StringBuilder().append(this.iconLocation).append("panr32.png").toString()));
        this.panCursorR = localToolkit.createCustomCursor(localImage, new Point(10, 10), "panr");
      }
      return this.panCursorR;
    case 10:
      if (this.panCursorBR == null)
      {
        localToolkit = Toolkit.getDefaultToolkit();
        localImage = localToolkit.getImage(getURLForImage(new StringBuilder().append(this.iconLocation).append("panbr32.png").toString()));
        this.panCursorBR = localToolkit.createCustomCursor(localImage, new Point(10, 10), "panbr");
      }
      return this.panCursorBR;
    case 11:
      if (this.panCursorB == null)
      {
        localToolkit = Toolkit.getDefaultToolkit();
        localImage = localToolkit.getImage(getURLForImage(new StringBuilder().append(this.iconLocation).append("panb32.png").toString()));
        this.panCursorB = localToolkit.createCustomCursor(localImage, new Point(10, 10), "panb");
      }
      return this.panCursorB;
    case 12:
      if (this.panCursorBL == null)
      {
        localToolkit = Toolkit.getDefaultToolkit();
        localImage = localToolkit.getImage(getURLForImage(new StringBuilder().append(this.iconLocation).append("panbl32.png").toString()));
        this.panCursorBL = localToolkit.createCustomCursor(localImage, new Point(10, 10), "panbl");
      }
      return this.panCursorBL;
    case 3:
    }
    return Cursor.getDefaultCursor();
  }

  public void rescanPdfLayers()
  {
    try
    {
      if (SwingUtilities.isEventDispatchThread())
      {
        syncTreeDisplay(this.topLayer, true);
        this.layersPanel.invalidate();
        this.layersPanel.repaint();
      }
      else
      {
        Runnable local37 = new Runnable()
        {
          public void run()
          {
            SwingGUI.this.syncTreeDisplay(SwingGUI.this.topLayer, true);
            SwingGUI.this.layersPanel.invalidate();
            SwingGUI.this.layersPanel.repaint();
          }
        };
        SwingUtilities.invokeAndWait(local37);
      }
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }

  private class ScrollListener
    implements AdjustmentListener
  {
    java.util.Timer t = null;
    int pNum = -1;
    int lastPageSent = -1;
    boolean usingMouseClick = false;
    boolean mousePressed = false;
    boolean showLast = false;
    public BufferedImage lastImage;
    public boolean ignoreChange = false;

    private ScrollListener()
    {
    }

    private void startTimer()
    {
      if (this.t != null)
        this.t.cancel();
      if (SwingGUI.this.thumbnails.isShownOnscreen())
      {
        long l = 175L;
        if (!this.usingMouseClick)
          l = 500L;
        PageListener localPageListener = new PageListener();
        this.t = new java.util.Timer();
        this.t.schedule(localPageListener, l);
      }
    }

    public void adjustmentValueChanged(AdjustmentEvent paramAdjustmentEvent)
    {
      this.pNum = (paramAdjustmentEvent.getAdjustable().getValue() + 1);
      if (!this.mousePressed)
      {
        this.showLast = false;
        this.lastImage = null;
      }
      if (this.showLast)
      {
        SwingGUI.this.decode_pdf.setPreviewThumbnail(this.lastImage, "Page " + this.pNum + " of " + SwingGUI.this.decode_pdf.getPageCount());
        SwingGUI.this.decode_pdf.repaint();
      }
      else if (this.lastImage != null)
      {
        BufferedImage localBufferedImage = new BufferedImage(this.lastImage.getWidth(), this.lastImage.getHeight(), this.lastImage.getType());
        Graphics2D localGraphics2D = (Graphics2D)localBufferedImage.getGraphics();
        localGraphics2D.drawImage(this.lastImage, 0, 0, null);
        localGraphics2D.setPaint(new Color(0, 0, 0, 130));
        localGraphics2D.fillRect(0, 0, this.lastImage.getWidth(), this.lastImage.getHeight());
        String str = "Loading...";
        int i = localGraphics2D.getFontMetrics().stringWidth(str);
        int j = localGraphics2D.getFontMetrics().getHeight();
        localGraphics2D.setPaint(Color.WHITE);
        localGraphics2D.drawString(str, this.lastImage.getWidth() / 2 - i / 2, this.lastImage.getHeight() / 2 + j / 2);
        this.lastImage = localBufferedImage;
        this.showLast = true;
        SwingGUI.this.decode_pdf.setPreviewThumbnail(localBufferedImage, "Page " + this.pNum + " of " + SwingGUI.this.decode_pdf.getPageCount());
        SwingGUI.this.decode_pdf.repaint();
      }
      startTimer();
      this.ignoreChange = false;
    }

    public synchronized void setThumbnail()
    {
      if (this.lastPageSent != this.pNum)
      {
        this.lastPageSent = this.pNum;
        try
        {
          BufferedImage localBufferedImage = SwingGUI.this.thumbnails.getImage(this.pNum);
          if (SwingGUI.this.debugThumbnail)
            System.out.println(this.pNum + " " + localBufferedImage);
          this.lastImage = localBufferedImage;
          this.showLast = false;
          SwingGUI.this.decode_pdf.setPreviewThumbnail(localBufferedImage, "Page " + this.pNum + " of " + SwingGUI.this.decode_pdf.getPageCount());
          SwingGUI.this.decode_pdf.repaint();
        }
        catch (Exception localException)
        {
        }
      }
    }

    public void releaseAndUpdate()
    {
      if (this.usingMouseClick)
        this.usingMouseClick = false;
      if (this.t != null)
        this.t.cancel();
      if (SwingGUI.this.decode_pdf.getDisplayView() != 5)
        SwingGUI.this.decode_pdf.setPreviewThumbnail(null, "Page " + this.pNum + " of " + SwingGUI.this.decode_pdf.getPageCount());
      SwingGUI.this.currentCommands.gotoPage(Integer.toString(this.pNum));
      SwingGUI.this.decode_pdf.repaint();
    }

    class PageListener extends TimerTask
    {
      PageListener()
      {
      }

      public void run()
      {
        if (SwingGUI.ScrollListener.this.mousePressed)
        {
          SwingGUI.ScrollListener.this.setThumbnail();
        }
        else
        {
          SwingGUI.ScrollListener.this.usingMouseClick = false;
          SwingGUI.ScrollListener.this.releaseAndUpdate();
        }
        if (SwingGUI.ScrollListener.this.t != null)
          SwingGUI.ScrollListener.this.t.cancel();
      }
    }
  }

  private class ScrollMouseListener extends MouseAdapter
  {
    private ScrollMouseListener()
    {
    }

    public void mousePressed(MouseEvent paramMouseEvent)
    {
      if (SwingGUI.this.debugThumbnail)
        System.out.println("pressed");
      SwingGUI.this.scrollListener.mousePressed = true;
      SwingGUI.this.scrollListener.usingMouseClick = true;
      SwingGUI.this.scrollListener.startTimer();
    }

    public void mouseReleased(MouseEvent paramMouseEvent)
    {
      if (SwingGUI.this.debugThumbnail)
        System.out.println("release");
      if (SwingGUI.this.scrollListener.mousePressed)
        SwingGUI.this.scrollListener.releaseAndUpdate();
      SwingGUI.this.scrollListener.mousePressed = false;
    }
  }

  private class PageChanger
    implements ActionListener
  {
    int page;

    public PageChanger(int arg2)
    {
      int i;
      i++;
      this.page = i;
    }

    public void actionPerformed(ActionEvent paramActionEvent)
    {
      if ((!Values.isProcessing()) && (SwingGUI.this.commonValues.getCurrentPage() != this.page))
      {
        if (!SwingGUI.this.decode_pdf.isPageAvailable(this.page))
        {
          SwingGUI.this.showMessageDialog("Page " + this.page + " is not yet loaded");
          return;
        }
        SwingGUI.this.statusBar.resetStatus("");
        SwingGUI.this.currentCommands.gotoPage(Integer.toString(this.page));
      }
    }
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.gui.SwingGUI
 * JD-Core Version:    0.6.2
 */