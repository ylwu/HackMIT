package org.jpedal.examples.viewer.gui.popups;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import javax.media.jai.JAI;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.Box.Filler;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreeSelectionModel;
import org.jpedal.display.swing.SingleDisplay;
import org.jpedal.examples.viewer.Viewer;
import org.jpedal.examples.viewer.gui.CheckNode;
import org.jpedal.examples.viewer.gui.CheckRenderer;
import org.jpedal.examples.viewer.gui.SwingGUI;
import org.jpedal.examples.viewer.paper.PaperSizes;
import org.jpedal.examples.viewer.utils.Printer;
import org.jpedal.examples.viewer.utils.PropertiesFile;
import org.jpedal.io.JAIHelper;
import org.jpedal.io.Speech;
import org.jpedal.objects.javascript.DefaultParser;
import org.jpedal.parser.DecoderOptions;
import org.jpedal.utils.BrowserLauncher;
import org.jpedal.utils.JavaFXHelper;
import org.jpedal.utils.Messages;
import org.jpedal.utils.SwingWorker;
import org.mozilla.javascript.ScriptRuntime;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SwingProperties extends JPanel
{
  Map reverseMessage = new HashMap();
  String[] menuTabs = { "ShowMenubar", "ShowButtons", "ShowDisplayoptions", "ShowNavigationbar", "ShowSidetabbar" };
  String propertiesLocation = "";
  PropertiesFile properties = null;
  JDialog propertiesDialog;
  JButton confirm = new JButton("OK");
  JButton cancel = new JButton("Cancel");
  JTabbedPane tabs = new JTabbedPane();
  JTextField resolution;
  JComboBox searchStyle;
  JCheckBox border;
  JCheckBox HiResPrint;
  JCheckBox constantTabs;
  JCheckBox enhancedViewer;
  JCheckBox enhancedFacing;
  JCheckBox thumbnailScroll;
  JCheckBox enhancedGUI;
  JCheckBox rightClick;
  JCheckBox scrollwheelZoom;
  JCheckBox update = new JCheckBox(Messages.getMessage("PdfPreferences.CheckForUpdate"));
  JTextField maxMultiViewers;
  JTextField pageInsets;
  JLabel pageInsetsText;
  JTextField windowTitle;
  JLabel windowTitleText;
  JTextField iconLocation;
  JLabel iconLocationText;
  JTextField printerBlacklist;
  JLabel printerBlacklistText;
  JComboBox defaultPrinter;
  JLabel defaultPrinterText;
  JComboBox defaultPagesize;
  JLabel defaultPagesizeText;
  JTextField defaultDPI;
  JLabel defaultDPIText;
  JTextField sideTabLength;
  JLabel sideTabLengthText;
  JCheckBox useHinting;
  JCheckBox autoScroll;
  JCheckBox confirmClose;
  JCheckBox openLastDoc;
  JComboBox pageLayout = new JComboBox(new String[] { "Single Page", "Continuous", "Continuous Facing", "Facing", "PageFlow" });
  JComboBox voiceSelect;
  JPanel highlightBoxColor = new JPanel();
  JPanel highlightTextColor = new JPanel();
  JPanel viewBGColor = new JPanel();
  JPanel pdfDecoderBackground = new JPanel();
  JPanel foreGroundColor = new JPanel();
  JCheckBox invertHighlight = new JCheckBox("Highlight Inverts Page");
  JCheckBox replaceDocTextCol = new JCheckBox("Replace Document Text Colors");
  JCheckBox replaceDisplayBGCol = new JCheckBox("Replace Display Background Color");
  JCheckBox changeTextAndLineArt = new JCheckBox("Change Color of Text and Line art");
  JCheckBox showMouseSelectionBox = new JCheckBox("Show Mouse Selection Box");
  JTextField highlightComposite = new JTextField(String.valueOf(DecoderOptions.highlightComposite));
  private Container parent;
  private boolean preferencesSetup = false;
  private JButton clearHistory;
  private JLabel historyClearedLabel;
  KeyListener numericalKeyListener = new KeyListener()
  {
    boolean consume = false;

    public void keyPressed(KeyEvent paramAnonymousKeyEvent)
    {
      this.consume = (((paramAnonymousKeyEvent.getKeyChar() < '0') || (paramAnonymousKeyEvent.getKeyChar() > '9')) && ((paramAnonymousKeyEvent.getKeyCode() != 8) || (paramAnonymousKeyEvent.getKeyCode() != 127)));
    }

    public void keyReleased(KeyEvent paramAnonymousKeyEvent)
    {
    }

    public void keyTyped(KeyEvent paramAnonymousKeyEvent)
    {
      if (this.consume)
        paramAnonymousKeyEvent.consume();
    }
  };

  public void showPreferenceWindow(SwingGUI paramSwingGUI)
  {
    if ((this.parent instanceof JFrame))
      this.propertiesDialog = new JDialog((JFrame)this.parent);
    else
      this.propertiesDialog = new JDialog();
    this.propertiesDialog.setModal(true);
    this.propertiesDialog.setDefaultCloseOperation(2);
    if (!this.preferencesSetup)
    {
      this.preferencesSetup = true;
      createPreferenceWindow(paramSwingGUI);
    }
    if (this.properties.getValue("readOnly").toLowerCase().equals("true"))
      JOptionPane.showMessageDialog(this, "You do not have permission alter jPedal properties.\nAccess to the properties window has therefore been disabled.", "Can not write to properties file", 1);
    if (this.properties.isReadOnly())
    {
      JOptionPane.showMessageDialog(this, "Current properties file is read only.\nAny alteration can only be saved as another properties file.", "Properties file is read only", 1);
      this.confirm.setEnabled(false);
    }
    else
    {
      this.confirm.setEnabled(true);
    }
    this.propertiesDialog.setLocationRelativeTo(this.parent);
    this.propertiesDialog.setVisible(true);
  }

  private void saveGUIPreferences(SwingGUI paramSwingGUI)
  {
    Component[] arrayOfComponent1 = this.tabs.getComponents();
    for (int i = 0; i != arrayOfComponent1.length; i++)
      if ((arrayOfComponent1[i] instanceof JPanel))
      {
        Component[] arrayOfComponent2 = ((JPanel)arrayOfComponent1[i]).getComponents();
        for (int j = 0; j != arrayOfComponent2.length; j++)
        {
          Object localObject;
          if ((arrayOfComponent2[j] instanceof JScrollPane))
          {
            localObject = ((JScrollPane)arrayOfComponent2[j]).getComponents();
            for (int k = 0; k != localObject.length; k++)
              if ((localObject[k] instanceof JViewport))
              {
                Component[] arrayOfComponent3 = ((JViewport)localObject[k]).getComponents();
                for (int m = 0; m != arrayOfComponent3.length; m++)
                  if ((arrayOfComponent3[m] instanceof JTree))
                  {
                    JTree localJTree = (JTree)arrayOfComponent3[m];
                    CheckNode localCheckNode = (CheckNode)localJTree.getModel().getRoot();
                    if (localCheckNode.getChildCount() > 0)
                      saveMenuPreferencesChildren(localCheckNode, paramSwingGUI);
                  }
              }
          }
          if ((arrayOfComponent2[j] instanceof JButton))
          {
            localObject = (JButton)arrayOfComponent2[j];
            String str = (String)this.reverseMessage.get(((JButton)localObject).getText().substring((Messages.getMessage("PdfCustomGui.HideGuiSection") + ' ').length()));
            if (((JButton)localObject).getText().startsWith(Messages.getMessage("PdfCustomGui.HideGuiSection") + ' '))
            {
              this.properties.setValue(str, "true");
              paramSwingGUI.alterProperty(str, true);
            }
            else
            {
              this.properties.setValue(str, "false");
              paramSwingGUI.alterProperty(str, false);
            }
          }
        }
      }
  }

  private void saveMenuPreferencesChildren(CheckNode paramCheckNode, SwingGUI paramSwingGUI)
  {
    for (int i = 0; i != paramCheckNode.getChildCount(); i++)
    {
      CheckNode localCheckNode = (CheckNode)paramCheckNode.getChildAt(i);
      String str = (String)this.reverseMessage.get(localCheckNode.getText());
      if (localCheckNode.isSelected())
      {
        this.properties.setValue(str, "true");
        paramSwingGUI.alterProperty(str, true);
      }
      else
      {
        this.properties.setValue(str, "false");
        paramSwingGUI.alterProperty(str, false);
      }
      if (localCheckNode.getChildCount() > 0)
        saveMenuPreferencesChildren(localCheckNode, paramSwingGUI);
    }
  }

  private void createPreferenceWindow(final SwingGUI paramSwingGUI)
  {
    this.properties = paramSwingGUI.getProperties();
    this.propertiesLocation = paramSwingGUI.getPropertiesFileLocation();
    this.propertiesDialog.setTitle(Messages.getMessage("PdfPreferences.windowTitle"));
    this.update.setToolTipText(Messages.getMessage("PdfPreferences.update.toolTip"));
    this.invertHighlight.setText(Messages.getMessage("PdfPreferences.InvertHighlight"));
    this.showMouseSelectionBox.setText(Messages.getMessage("PdfPreferences.ShowSelectionBow"));
    this.invertHighlight.setToolTipText(Messages.getMessage("PdfPreferences.invertHighlight.toolTip"));
    this.showMouseSelectionBox.setToolTipText(Messages.getMessage("PdfPreferences.showMouseSelection.toolTip"));
    this.highlightBoxColor.setToolTipText(Messages.getMessage("PdfPreferences.highlightBox.toolTip"));
    this.highlightTextColor.setToolTipText(Messages.getMessage("PdfPreferences.highlightText.toolTip"));
    String str = this.properties.getValue("resolution");
    if (!str.isEmpty())
      this.resolution = new JTextField(str);
    else
      this.resolution = new JTextField(72);
    this.resolution.setToolTipText(Messages.getMessage("PdfPreferences.resolutionInput.toolTip"));
    str = this.properties.getValue("maxmultiviewers");
    if (!str.isEmpty())
      this.maxMultiViewers = new JTextField(str);
    else
      this.maxMultiViewers = new JTextField(20);
    this.maxMultiViewers.setToolTipText(Messages.getMessage("PdfPreferences.maxMultiViewer.toolTip"));
    if (paramSwingGUI.isSingle())
      this.searchStyle = new JComboBox(new String[] { Messages.getMessage("PageLayoutViewMenu.WindowSearch"), Messages.getMessage("PageLayoutViewMenu.TabbedSearch"), Messages.getMessage("PageLayoutViewMenu.MenuSearch") });
    else
      this.searchStyle = new JComboBox(new String[] { Messages.getMessage("PageLayoutViewMenu.WindowSearch"), Messages.getMessage("PageLayoutViewMenu.TabbedSearch") });
    this.searchStyle.setToolTipText(Messages.getMessage("PdfPreferences.searchStyle.toolTip"));
    this.pageLayout = new JComboBox(new String[] { Messages.getMessage("PageLayoutViewMenu.SinglePage"), Messages.getMessage("PageLayoutViewMenu.Continuous"), Messages.getMessage("PageLayoutViewMenu.Facing"), Messages.getMessage("PageLayoutViewMenu.ContinousFacing"), Messages.getMessage("PageLayoutViewMenu.PageFlow") });
    this.pageLayout.setToolTipText(Messages.getMessage("PdfPreferences.pageLayout.toolTip"));
    this.pageInsetsText = new JLabel(Messages.getMessage("PdfViewerViewMenu.pageInsets"));
    this.pageInsets = new JTextField();
    this.pageInsets.setToolTipText(Messages.getMessage("PdfPreferences.pageInsets.toolTip"));
    this.windowTitleText = new JLabel(Messages.getMessage("PdfCustomGui.windowTitle"));
    this.windowTitle = new JTextField();
    this.windowTitle.setToolTipText(Messages.getMessage("PdfPreferences.windowTitle.toolTip"));
    this.iconLocationText = new JLabel(Messages.getMessage("PdfViewerViewMenu.iconLocation"));
    this.iconLocation = new JTextField();
    this.iconLocation.setToolTipText(Messages.getMessage("PdfPreferences.iconLocation.toolTip"));
    this.printerBlacklistText = new JLabel(Messages.getMessage("PdfViewerPrint.blacklist"));
    this.printerBlacklist = new JTextField();
    this.printerBlacklist.setToolTipText(Messages.getMessage("PdfPreferences.printerBlackList.toolTip"));
    this.defaultPrinterText = new JLabel(Messages.getMessage("PdfViewerPrint.defaultPrinter"));
    this.defaultPrinter = new JComboBox(Printer.getAvailablePrinters(this.properties.getValue("printerBlacklist")));
    PrintService localPrintService = PrintServiceLookup.lookupDefaultPrintService();
    if (localPrintService != null)
      this.defaultPrinter.addItem(Messages.getMessage("PdfPreferences.systemDefault.text") + " (" + localPrintService.getName() + ')');
    else
      this.defaultPrinter.addItem(Messages.getMessage("PdfPreferences.systemDefault.text"));
    this.defaultPrinter.setToolTipText(Messages.getMessage("PdfPreferences.defaultPrinter.toolTip"));
    this.defaultPagesizeText = new JLabel(Messages.getMessage("PdfViewerPrint.defaultPagesize"));
    this.defaultPagesize = new JComboBox();
    this.defaultPagesize.setModel(new DefaultComboBoxModel(paramSwingGUI.getPaperSizes().getPaperSizes()));
    this.defaultPagesize.setSelectedIndex(paramSwingGUI.getPaperSizes().getDefaultPageIndex());
    this.defaultPagesize.setToolTipText(Messages.getMessage("PdfPreferences.defaultPageSize.toolTip"));
    this.defaultDPIText = new JLabel(Messages.getMessage("PdfViewerPrint.defaultDPI"));
    this.defaultDPI = new JTextField();
    this.defaultDPI.setToolTipText(Messages.getMessage("PdfPreferences.defaultDPI.toolTip"));
    this.sideTabLengthText = new JLabel(Messages.getMessage("PdfCustomGui.SideTabLength"));
    this.sideTabLength = new JTextField();
    this.sideTabLength.setToolTipText(Messages.getMessage("PdfPreferences.sideTabLength.toolTip"));
    this.useHinting = new JCheckBox(Messages.getMessage("PdfCustomGui.useHinting"));
    this.useHinting.setToolTipText(Messages.getMessage("PdfPreferences.useHinting.toolTip"));
    this.autoScroll = new JCheckBox(Messages.getMessage("PdfViewerViewMenuAutoscrollSet.text"));
    this.autoScroll.setToolTipText("Set if autoscroll should be enabled / disabled");
    this.confirmClose = new JCheckBox(Messages.getMessage("PfdViewerViewMenuConfirmClose.text"));
    this.confirmClose.setToolTipText("Set if we should confirm closing the viewer");
    this.openLastDoc = new JCheckBox(Messages.getMessage("PdfViewerViewMenuOpenLastDoc.text"));
    this.openLastDoc.setToolTipText("Set if last document should be opened upon start up");
    this.border = new JCheckBox(Messages.getMessage("PageLayoutViewMenu.Borders_Show"));
    this.border.setToolTipText("Set if we should display a border for the page");
    this.HiResPrint = new JCheckBox(Messages.getMessage("Printing.HiRes"));
    this.HiResPrint.setToolTipText("Set if hi res printing should be enabled / disabled");
    this.constantTabs = new JCheckBox(Messages.getMessage("PdfCustomGui.consistentTabs"));
    this.constantTabs.setToolTipText("Set to keep sidetabs consistant between files");
    this.enhancedViewer = new JCheckBox(Messages.getMessage("PdfCustomGui.enhancedViewer"));
    this.enhancedViewer.setToolTipText("Set to use enahnced viewer mode");
    this.enhancedFacing = new JCheckBox(Messages.getMessage("PdfCustomGui.enhancedFacing"));
    this.enhancedFacing.setToolTipText("Set to turn facing mode to page turn mode");
    this.thumbnailScroll = new JCheckBox(Messages.getMessage("PdfCustomGui.thumbnailScroll"));
    this.thumbnailScroll.setToolTipText("Set to show thumbnail whilst scrolling");
    this.enhancedGUI = new JCheckBox(Messages.getMessage("PdfCustomGui.enhancedGUI"));
    this.enhancedGUI.setToolTipText("Set to enabled the enhanced gui");
    this.rightClick = new JCheckBox(Messages.getMessage("PdfCustomGui.allowRightClick"));
    this.rightClick.setToolTipText("Set to enable / disable the right click functionality");
    this.scrollwheelZoom = new JCheckBox(Messages.getMessage("PdfCustomGui.allowScrollwheelZoom"));
    this.scrollwheelZoom.setToolTipText("Set to enable zooming when scrolling with ctrl pressed");
    this.historyClearedLabel = new JLabel(Messages.getMessage("PageLayoutViewMenu.HistoryCleared"));
    this.historyClearedLabel.setForeground(Color.red);
    this.historyClearedLabel.setVisible(false);
    this.clearHistory = new JButton(Messages.getMessage("PageLayoutViewMenu.ClearHistory"));
    this.clearHistory.setToolTipText("Clears the history of previous files");
    this.clearHistory.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        paramSwingGUI.clearRecentDocuments();
        SwingWorker local1 = new SwingWorker()
        {
          public Object construct()
          {
            for (int i = 0; i < 6; i++)
            {
              SwingProperties.this.historyClearedLabel.setVisible(!SwingProperties.this.historyClearedLabel.isVisible());
              try
              {
                Thread.sleep(300L);
              }
              catch (InterruptedException localInterruptedException)
              {
              }
            }
            return null;
          }
        };
        local1.start();
      }
    });
    JButton localJButton1 = new JButton(Messages.getMessage("PdfPreferences.SaveAs"));
    localJButton1.setToolTipText("Save preferences in a new file");
    JButton localJButton2 = new JButton(Messages.getMessage("PdfPreferences.ResetToDefault"));
    localJButton2.setToolTipText("Reset  and save preferences to program defaults");
    this.propertiesDialog.getContentPane().setLayout(new BorderLayout());
    this.propertiesDialog.getContentPane().add(this, "Center");
    this.propertiesDialog.pack();
    if (DecoderOptions.isRunningOnMac)
      this.propertiesDialog.setSize(600, 475);
    else
      this.propertiesDialog.setSize(550, 450);
    this.confirm.setText(Messages.getMessage("PdfPreferences.OK"));
    this.cancel.setText(Messages.getMessage("PdfPreferences.Cancel"));
    this.confirm.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        SwingProperties.this.setPreferences(paramSwingGUI);
        if (Viewer.showMessages)
          JOptionPane.showMessageDialog(null, Messages.getMessage("PdfPreferences.savedTo") + SwingProperties.this.propertiesLocation + '\n' + Messages.getMessage("PdfPreferences.restart"), "Restart Jpedal", 1);
        SwingProperties.this.propertiesDialog.setVisible(false);
      }
    });
    this.confirm.setToolTipText("Save the preferences in the current loaded preferences file");
    this.cancel.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        SwingProperties.this.propertiesDialog.setVisible(false);
      }
    });
    this.cancel.setToolTipText("Leave preferences window without saving changes");
    localJButton1.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        String str = paramSwingGUI.getPropertiesFileLocation();
        JFileChooser localJFileChooser = new JFileChooser();
        int i = localJFileChooser.showSaveDialog(SwingProperties.this.propertiesDialog);
        if ((i != 1) && (i != -1) && (i == 0))
        {
          File localFile = localJFileChooser.getSelectedFile();
          if (localFile.exists())
            localFile.delete();
          paramSwingGUI.setPropertiesFileLocation(localFile.getAbsolutePath());
          SwingProperties.this.setPreferences(paramSwingGUI);
        }
        paramSwingGUI.setPropertiesFileLocation(str);
      }
    });
    localJButton2.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        int i = JOptionPane.showConfirmDialog(SwingProperties.this.propertiesDialog, Messages.getMessage("PdfPreferences.reset"), "Reset to Default", 0);
        if (i == 0)
        {
          String str = paramSwingGUI.getPropertiesFileLocation();
          File localFile = new File(str);
          if (localFile.exists())
            localFile.delete();
          paramSwingGUI.getProperties().loadProperties(str);
          if (Viewer.showMessages)
            JOptionPane.showMessageDialog(SwingProperties.this.propertiesDialog, Messages.getMessage("PdfPreferences.restart"));
          SwingProperties.this.propertiesDialog.setVisible(false);
        }
      }
    });
    this.highlightComposite.addKeyListener(new KeyListener()
    {
      boolean consume = false;

      public void keyPressed(KeyEvent paramAnonymousKeyEvent)
      {
        this.consume = ((((JTextField)paramAnonymousKeyEvent.getSource()).getText().contains(".")) && (paramAnonymousKeyEvent.getKeyChar() == '.') && ((paramAnonymousKeyEvent.getKeyChar() < '0') || (paramAnonymousKeyEvent.getKeyChar() > '9')) && ((paramAnonymousKeyEvent.getKeyCode() != 8) || (paramAnonymousKeyEvent.getKeyCode() != 127)));
      }

      public void keyReleased(KeyEvent paramAnonymousKeyEvent)
      {
      }

      public void keyTyped(KeyEvent paramAnonymousKeyEvent)
      {
        if (this.consume)
          paramAnonymousKeyEvent.consume();
      }
    });
    this.highlightComposite.setToolTipText("Set the transparency of the highlight");
    this.resolution.addKeyListener(this.numericalKeyListener);
    this.maxMultiViewers.addKeyListener(this.numericalKeyListener);
    setLayout(new BorderLayout());
    JPanel localJPanel1 = new JPanel();
    BoxLayout localBoxLayout = new BoxLayout(localJPanel1, 1);
    localJPanel1.setLayout(localBoxLayout);
    add(new ButtonBarPanel(localJPanel1), "Center");
    localJPanel1.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.gray));
    JPanel localJPanel2 = new JPanel();
    localJPanel2.setLayout(new BoxLayout(localJPanel2, 0));
    Dimension localDimension = new Dimension(5, 40);
    Box.Filler localFiller = new Box.Filler(localDimension, localDimension, localDimension);
    this.confirm.setPreferredSize(this.cancel.getPreferredSize());
    if (this.properties.isReadOnly())
      this.confirm.setEnabled(false);
    else
      this.confirm.setEnabled(true);
    localJPanel2.add(localJButton2);
    localJPanel2.add(Box.createHorizontalGlue());
    localJPanel2.add(this.confirm);
    localJPanel2.add(localJButton1);
    getRootPane().setDefaultButton(this.confirm);
    localJPanel2.add(localFiller);
    localJPanel2.add(this.cancel);
    localJPanel2.add(localFiller);
    localJPanel2.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.gray));
    add(localJPanel2, "South");
  }

  public void setPreferences(SwingGUI paramSwingGUI)
  {
    int i = 0;
    int j = this.pageLayout.getSelectedIndex() + 1;
    if ((j < 1) || (j > 5))
      j = 1;
    if (this.border.isSelected())
      i = 1;
    int k = this.highlightBoxColor.getBackground().getRGB();
    int m = this.highlightTextColor.getBackground().getRGB();
    int n = this.viewBGColor.getBackground().getRGB();
    int i1 = this.pdfDecoderBackground.getBackground().getRGB();
    int i2 = this.foreGroundColor.getBackground().getRGB();
    boolean bool1 = this.changeTextAndLineArt.isSelected();
    boolean bool2 = this.invertHighlight.isSelected();
    boolean bool3 = this.replaceDocTextCol.isSelected();
    boolean bool4 = this.replaceDisplayBGCol.isSelected();
    boolean bool5 = this.showMouseSelectionBox.isSelected();
    this.properties.setValue("borderType", String.valueOf(i));
    this.properties.setValue("useHinting", String.valueOf(this.useHinting.isSelected()));
    this.properties.setValue("startView", String.valueOf(j));
    this.properties.setValue("pageInsets", String.valueOf(this.pageInsets.getText()));
    this.properties.setValue("windowTitle", String.valueOf(this.windowTitle.getText()));
    String str = this.iconLocation.getText();
    if ((!str.endsWith("/")) && (!str.endsWith("\\")))
      str = str + '/';
    this.properties.setValue("iconLocation", String.valueOf(str));
    this.properties.setValue("sideTabBarCollapseLength", String.valueOf(this.sideTabLength.getText()));
    this.properties.setValue("autoScroll", String.valueOf(this.autoScroll.isSelected()));
    this.properties.setValue("confirmClose", String.valueOf(this.confirmClose.isSelected()));
    this.properties.setValue("openLastDocument", String.valueOf(this.openLastDoc.isSelected()));
    this.properties.setValue("resolution", String.valueOf(this.resolution.getText()));
    this.properties.setValue("searchWindowType", String.valueOf(this.searchStyle.getSelectedIndex()));
    this.properties.setValue("automaticupdate", String.valueOf(this.update.isSelected()));
    this.properties.setValue("maxmultiviewers", String.valueOf(this.maxMultiViewers.getText()));
    this.properties.setValue("useHiResPrinting", String.valueOf(this.HiResPrint.isSelected()));
    this.properties.setValue("consistentTabBar", String.valueOf(this.constantTabs.isSelected()));
    this.properties.setValue("highlightComposite", String.valueOf(this.highlightComposite.getText()));
    this.properties.setValue("highlightBoxColor", String.valueOf(k));
    this.properties.setValue("highlightTextColor", String.valueOf(m));
    this.properties.setValue("vbgColor", String.valueOf(n));
    this.properties.setValue("pdfDisplayBackground", String.valueOf(i1));
    this.properties.setValue("vfgColor", String.valueOf(i2));
    this.properties.setValue("replaceDocumentTextColors", String.valueOf(bool3));
    this.properties.setValue("replacePdfDisplayBackground", String.valueOf(bool4));
    this.properties.setValue("changeTextAndLineart", String.valueOf(bool1));
    this.properties.setValue("invertHighlights", String.valueOf(bool2));
    this.properties.setValue("showMouseSelectionBox", String.valueOf(bool5));
    this.properties.setValue("allowRightClick", String.valueOf(this.rightClick.isSelected()));
    this.properties.setValue("allowScrollwheelZoom", String.valueOf(this.scrollwheelZoom.isSelected()));
    this.properties.setValue("enhancedViewerMode", String.valueOf(this.enhancedViewer.isSelected()));
    this.properties.setValue("enhancedFacingMode", String.valueOf(this.enhancedFacing.isSelected()));
    this.properties.setValue("previewOnSingleScroll", String.valueOf(this.thumbnailScroll.isSelected()));
    this.properties.setValue("enhancedGUI", String.valueOf(this.enhancedGUI.isSelected()));
    this.properties.setValue("printerBlacklist", String.valueOf(this.printerBlacklist.getText()));
    if (((String)this.defaultPrinter.getSelectedItem()).startsWith("System Default"))
      this.properties.setValue("defaultPrinter", "");
    else
      this.properties.setValue("defaultPrinter", String.valueOf(this.defaultPrinter.getSelectedItem()));
    this.properties.setValue("defaultDPI", String.valueOf(this.defaultDPI.getText()));
    this.properties.setValue("defaultPagesize", String.valueOf(this.defaultPagesize.getSelectedItem()));
    if (hasFreetts())
      this.properties.setValue("voice", String.valueOf(this.voiceSelect.getSelectedItem()));
    saveGUIPreferences(paramSwingGUI);
  }

  public void setParent(Container paramContainer)
  {
    this.parent = paramContainer;
  }

  private static boolean hasFreetts()
  {
    return SwingProperties.class.getResourceAsStream("/com/sun/speech/freetts") != null;
  }

  class ButtonBarPanel extends JPanel
  {
    private Component currentComponent;

    public ButtonBarPanel(JPanel arg2)
    {
      setLayout(new BorderLayout());
      JScrollPane localJScrollPane = new JScrollPane();
      Component localComponent;
      localJScrollPane.getViewport().add(localComponent);
      localJScrollPane.setHorizontalScrollBarPolicy(31);
      localJScrollPane.setVerticalScrollBarPolicy(22);
      add(localJScrollPane, "West");
      ButtonGroup localButtonGroup = new ButtonGroup();
      addButton(Messages.getMessage("PdfPreferences.GeneralTitle"), "/org/jpedal/examples/viewer/res/display.png", createGeneralSettings(), localComponent, localButtonGroup);
      addButton(Messages.getMessage("PdfPreferences.PageDisplayTitle"), "/org/jpedal/examples/viewer/res/pagedisplay.png", createPageDisplaySettings(), localComponent, localButtonGroup);
      addButton(Messages.getMessage("PdfPreferences.InterfaceTitle"), "/org/jpedal/examples/viewer/res/interface.png", createInterfaceSettings(), localComponent, localButtonGroup);
      addButton(Messages.getMessage("PdfPreferences.ColorTitle"), "/org/jpedal/examples/viewer/res/color.png", createColorSettings(), localComponent, localButtonGroup);
      addButton(Messages.getMessage("PdfPreferences.MenuTitle"), "/org/jpedal/examples/viewer/res/menu.png", createMenuSettings(), localComponent, localButtonGroup);
      addButton(Messages.getMessage("PdfPreferences.PrintingTitle"), "/org/jpedal/examples/viewer/res/printing.png", createPrintingSettings(), localComponent, localButtonGroup);
      addButton(Messages.getMessage("PdfPreferences.ExtensionsTitle"), "/org/jpedal/examples/viewer/res/extensions.png", createExtensionsPane(), localComponent, localButtonGroup);
    }

    private JPanel makePanel(String paramString)
    {
      JPanel localJPanel1 = new JPanel(new BorderLayout());
      JLabel localJLabel = new JLabel(paramString);
      localJLabel.setFont(localJLabel.getFont().deriveFont(1));
      localJLabel.setOpaque(true);
      localJLabel.setBackground(localJPanel1.getBackground().brighter());
      JPanel localJPanel2 = new JPanel(new BorderLayout());
      localJPanel2.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
      localJPanel2.setFont(localJPanel2.getFont().deriveFont(1));
      localJPanel2.setOpaque(true);
      localJPanel2.setBackground(localJPanel1.getBackground().brighter());
      localJPanel2.add(localJLabel, "West");
      localJPanel1.add(localJPanel2, "North");
      localJPanel1.setPreferredSize(new Dimension(400, 300));
      localJPanel1.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
      return localJPanel1;
    }

    private JPanel createGeneralSettings()
    {
      String str = SwingProperties.this.properties.getValue("resolution");
      if (!str.isEmpty())
        SwingProperties.this.resolution.setText(str);
      str = SwingProperties.this.properties.getValue("useHinting");
      if ((!str.isEmpty()) && (str.equals("true")))
        SwingProperties.this.useHinting.setSelected(true);
      else
        SwingProperties.this.useHinting.setSelected(false);
      str = SwingProperties.this.properties.getValue("autoScroll");
      if (str.equals("true"))
        SwingProperties.this.autoScroll.setSelected(true);
      else
        SwingProperties.this.autoScroll.setSelected(false);
      str = SwingProperties.this.properties.getValue("confirmClose");
      if (str.equals("true"))
        SwingProperties.this.confirmClose.setSelected(true);
      else
        SwingProperties.this.confirmClose.setSelected(false);
      str = SwingProperties.this.properties.getValue("automaticupdate");
      if (str.equals("true"))
        SwingProperties.this.update.setSelected(true);
      else
        SwingProperties.this.update.setSelected(false);
      str = SwingProperties.this.properties.getValue("openLastDocument");
      if (str.equals("true"))
        SwingProperties.this.openLastDoc.setSelected(true);
      else
        SwingProperties.this.openLastDoc.setSelected(false);
      JPanel localJPanel1 = makePanel(Messages.getMessage("PdfPreferences.GeneralTitle"));
      JPanel localJPanel2 = new JPanel();
      JScrollPane localJScrollPane = new JScrollPane(localJPanel2);
      localJScrollPane.setBorder(BorderFactory.createEmptyBorder());
      localJPanel2.setLayout(new GridBagLayout());
      GridBagConstraints localGridBagConstraints = new GridBagConstraints();
      localGridBagConstraints.fill = 1;
      localGridBagConstraints.insets = new Insets(5, 0, 0, 5);
      localGridBagConstraints.weighty = 0.0D;
      localGridBagConstraints.weightx = 0.0D;
      localGridBagConstraints.gridx = 0;
      localGridBagConstraints.gridy = 0;
      JLabel localJLabel1 = new JLabel(Messages.getMessage("PdfPreferences.GeneralSection"));
      localJLabel1.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
      localJLabel1.setFont(localJLabel1.getFont().deriveFont(1));
      localJPanel2.add(localJLabel1, localGridBagConstraints);
      localGridBagConstraints.gridy += 1;
      localGridBagConstraints.insets = new Insets(10, 0, 0, 5);
      localGridBagConstraints.gridx = 0;
      JLabel localJLabel2 = new JLabel(Messages.getMessage("PdfViewerViewMenu.Resolution"));
      localJLabel2.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
      localJPanel2.add(localJLabel2, localGridBagConstraints);
      localGridBagConstraints.insets = new Insets(10, 0, 0, 0);
      localGridBagConstraints.weightx = 1.0D;
      localGridBagConstraints.gridx = 1;
      localJPanel2.add(SwingProperties.this.resolution, localGridBagConstraints);
      localGridBagConstraints.gridy += 1;
      localGridBagConstraints.gridwidth = 2;
      localGridBagConstraints.gridx = 0;
      SwingProperties.this.useHinting.setMargin(new Insets(0, 0, 0, 0));
      SwingProperties.this.useHinting.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
      localJPanel2.add(SwingProperties.this.useHinting, localGridBagConstraints);
      localGridBagConstraints.gridy += 1;
      localGridBagConstraints.gridwidth = 2;
      localGridBagConstraints.gridx = 0;
      SwingProperties.this.autoScroll.setMargin(new Insets(0, 0, 0, 0));
      SwingProperties.this.autoScroll.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
      localJPanel2.add(SwingProperties.this.autoScroll, localGridBagConstraints);
      localGridBagConstraints.gridy += 1;
      localGridBagConstraints.gridwidth = 2;
      localGridBagConstraints.gridx = 0;
      SwingProperties.this.confirmClose.setMargin(new Insets(0, 0, 0, 0));
      SwingProperties.this.confirmClose.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
      localJPanel2.add(SwingProperties.this.confirmClose, localGridBagConstraints);
      localGridBagConstraints.gridy += 1;
      localGridBagConstraints.insets = new Insets(15, 0, 0, 5);
      localGridBagConstraints.weighty = 0.0D;
      localGridBagConstraints.weightx = 0.0D;
      localGridBagConstraints.gridx = 0;
      JLabel localJLabel3 = new JLabel(Messages.getMessage("PdfPreferences.StartUp"));
      localJLabel3.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
      localJLabel3.setFont(localJLabel3.getFont().deriveFont(1));
      localJPanel2.add(localJLabel3, localGridBagConstraints);
      localGridBagConstraints.gridy += 1;
      localGridBagConstraints.insets = new Insets(10, 0, 0, 0);
      localGridBagConstraints.weighty = 0.0D;
      localGridBagConstraints.weightx = 1.0D;
      localGridBagConstraints.gridwidth = 2;
      localGridBagConstraints.gridx = 0;
      SwingProperties.this.update.setMargin(new Insets(0, 0, 0, 0));
      SwingProperties.this.update.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
      localJPanel2.add(SwingProperties.this.update, localGridBagConstraints);
      localGridBagConstraints.gridy += 1;
      localGridBagConstraints.gridwidth = 2;
      localGridBagConstraints.gridx = 0;
      SwingProperties.this.openLastDoc.setMargin(new Insets(0, 0, 0, 0));
      SwingProperties.this.openLastDoc.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
      localJPanel2.add(SwingProperties.this.openLastDoc, localGridBagConstraints);
      localGridBagConstraints.gridy += 1;
      localGridBagConstraints.gridwidth = 2;
      localGridBagConstraints.gridx = 0;
      JPanel localJPanel3 = new JPanel();
      localJPanel3.setLayout(new BoxLayout(localJPanel3, 0));
      localJPanel3.add(SwingProperties.this.clearHistory);
      localJPanel3.add(Box.createHorizontalGlue());
      localJPanel3.add(SwingProperties.this.historyClearedLabel);
      localJPanel3.add(Box.createHorizontalGlue());
      localJPanel2.add(localJPanel3, localGridBagConstraints);
      localGridBagConstraints.gridy += 1;
      localGridBagConstraints.weighty = 1.0D;
      localGridBagConstraints.gridx = 0;
      localJPanel2.add(Box.createVerticalGlue(), localGridBagConstraints);
      localJPanel1.add(localJScrollPane, "Center");
      return localJPanel1;
    }

    private JPanel createPageDisplaySettings()
    {
      String str = SwingProperties.this.properties.getValue("enhancedViewerMode");
      if ((!str.isEmpty()) && (str.equals("true")))
        SwingProperties.this.enhancedViewer.setSelected(true);
      else
        SwingProperties.this.enhancedViewer.setSelected(false);
      str = SwingProperties.this.properties.getValue("borderType");
      if (!str.isEmpty())
        if (Integer.parseInt(str) == 1)
          SwingProperties.this.border.setSelected(true);
        else
          SwingProperties.this.border.setSelected(false);
      str = SwingProperties.this.properties.getValue("pageInsets");
      if ((str != null) && (!str.isEmpty()))
        SwingProperties.this.pageInsets.setText(str);
      else
        SwingProperties.this.pageInsets.setText("25");
      str = SwingProperties.this.properties.getValue("startView");
      if (!str.isEmpty())
      {
        int i = Integer.parseInt(str);
        if ((i < 1) || (i > 5))
          i = 1;
        SwingProperties.this.pageLayout.setSelectedIndex(i - 1);
      }
      str = SwingProperties.this.properties.getValue("enhancedFacingMode");
      if ((!str.isEmpty()) && (str.equals("true")))
        SwingProperties.this.enhancedFacing.setSelected(true);
      else
        SwingProperties.this.enhancedFacing.setSelected(false);
      str = SwingProperties.this.properties.getValue("previewOnSingleScroll");
      if ((!str.isEmpty()) && (str.equals("true")))
        SwingProperties.this.thumbnailScroll.setSelected(true);
      else
        SwingProperties.this.thumbnailScroll.setSelected(false);
      JPanel localJPanel1 = makePanel(Messages.getMessage("PdfPreferences.PageDisplayTitle"));
      JPanel localJPanel2 = new JPanel();
      JScrollPane localJScrollPane = new JScrollPane(localJPanel2);
      localJScrollPane.setBorder(BorderFactory.createEmptyBorder());
      localJPanel2.setLayout(new GridBagLayout());
      GridBagConstraints localGridBagConstraints = new GridBagConstraints();
      localGridBagConstraints.fill = 1;
      localGridBagConstraints.insets = new Insets(5, 0, 0, 5);
      localGridBagConstraints.weighty = 0.0D;
      localGridBagConstraints.weightx = 0.0D;
      localGridBagConstraints.gridx = 0;
      localGridBagConstraints.gridy = 0;
      JLabel localJLabel1 = new JLabel(Messages.getMessage("PdfPreferences.GeneralSection"));
      localJLabel1.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
      localJLabel1.setFont(localJLabel1.getFont().deriveFont(1));
      localJPanel2.add(localJLabel1, localGridBagConstraints);
      localGridBagConstraints.gridy += 1;
      localGridBagConstraints.insets = new Insets(5, 0, 0, 0);
      localGridBagConstraints.gridwidth = 2;
      localGridBagConstraints.gridx = 0;
      SwingProperties.this.enhancedViewer.setMargin(new Insets(0, 0, 0, 0));
      SwingProperties.this.enhancedViewer.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
      localJPanel2.add(SwingProperties.this.enhancedViewer, localGridBagConstraints);
      localGridBagConstraints.gridy += 1;
      localGridBagConstraints.gridwidth = 2;
      localGridBagConstraints.gridx = 0;
      SwingProperties.this.border.setMargin(new Insets(0, 0, 0, 0));
      SwingProperties.this.border.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
      localJPanel2.add(SwingProperties.this.border, localGridBagConstraints);
      localGridBagConstraints.gridy += 1;
      localGridBagConstraints.insets = new Insets(5, 0, 0, 0);
      localGridBagConstraints.gridwidth = 2;
      localGridBagConstraints.gridx = 0;
      localJPanel2.add(SwingProperties.this.pageInsetsText, localGridBagConstraints);
      localGridBagConstraints.gridwidth = 2;
      localGridBagConstraints.gridx = 1;
      localJPanel2.add(SwingProperties.this.pageInsets, localGridBagConstraints);
      localGridBagConstraints.gridy += 1;
      localGridBagConstraints.insets = new Insets(15, 0, 0, 5);
      localGridBagConstraints.weighty = 0.0D;
      localGridBagConstraints.weightx = 0.0D;
      localGridBagConstraints.gridx = 0;
      JLabel localJLabel2 = new JLabel(Messages.getMessage("PdfPreferences.DisplayModes"));
      localJLabel2.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
      localJLabel2.setFont(localJLabel2.getFont().deriveFont(1));
      localJPanel2.add(localJLabel2, localGridBagConstraints);
      localGridBagConstraints.gridy += 1;
      localGridBagConstraints.insets = new Insets(5, 0, 0, 5);
      localGridBagConstraints.weighty = 0.0D;
      localGridBagConstraints.weightx = 0.0D;
      localGridBagConstraints.gridx = 0;
      JLabel localJLabel3 = new JLabel(Messages.getMessage("PageLayoutViewMenu.PageLayout"));
      localJLabel3.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
      localJPanel2.add(localJLabel3, localGridBagConstraints);
      localGridBagConstraints.insets = new Insets(5, 0, 0, 0);
      localGridBagConstraints.weightx = 1.0D;
      localGridBagConstraints.gridx = 1;
      localJPanel2.add(SwingProperties.this.pageLayout, localGridBagConstraints);
      localGridBagConstraints.gridy += 1;
      localGridBagConstraints.gridwidth = 2;
      localGridBagConstraints.gridx = 0;
      SwingProperties.this.enhancedFacing.setMargin(new Insets(0, 0, 0, 0));
      SwingProperties.this.enhancedFacing.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
      localJPanel2.add(SwingProperties.this.enhancedFacing, localGridBagConstraints);
      localGridBagConstraints.gridy += 1;
      localGridBagConstraints.gridwidth = 2;
      localGridBagConstraints.gridx = 0;
      SwingProperties.this.thumbnailScroll.setMargin(new Insets(0, 0, 0, 0));
      SwingProperties.this.thumbnailScroll.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
      localJPanel2.add(SwingProperties.this.thumbnailScroll, localGridBagConstraints);
      localGridBagConstraints.gridy += 1;
      localGridBagConstraints.weighty = 1.0D;
      localGridBagConstraints.gridx = 0;
      localJPanel2.add(Box.createVerticalGlue(), localGridBagConstraints);
      localJPanel1.add(localJScrollPane, "Center");
      return localJPanel1;
    }

    private JPanel createInterfaceSettings()
    {
      String str1 = SwingProperties.this.properties.getValue("enhancedGUI");
      if ((!str1.isEmpty()) && (str1.equals("true")))
        SwingProperties.this.enhancedGUI.setSelected(true);
      else
        SwingProperties.this.enhancedGUI.setSelected(false);
      str1 = SwingProperties.this.properties.getValue("allowRightClick");
      if ((!str1.isEmpty()) && (str1.equals("true")))
        SwingProperties.this.rightClick.setSelected(true);
      else
        SwingProperties.this.rightClick.setSelected(false);
      str1 = SwingProperties.this.properties.getValue("allowScrollwheelZoom");
      if ((!str1.isEmpty()) && (str1.equals("true")))
        SwingProperties.this.scrollwheelZoom.setSelected(true);
      else
        SwingProperties.this.scrollwheelZoom.setSelected(false);
      str1 = SwingProperties.this.properties.getValue("windowTitle");
      if ((str1 != null) && (!str1.isEmpty()))
        SwingProperties.this.windowTitle.setText(str1);
      str1 = SwingProperties.this.properties.getValue("iconLocation");
      if ((str1 != null) && (!str1.isEmpty()))
        SwingProperties.this.iconLocation.setText(str1);
      else
        SwingProperties.this.iconLocation.setText("/org/jpedal/examples/viewer/res/");
      str1 = SwingProperties.this.properties.getValue("searchWindowType");
      if (!str1.isEmpty())
      {
        int i = Integer.parseInt(str1);
        if (i < SwingProperties.this.searchStyle.getItemCount())
          SwingProperties.this.searchStyle.setSelectedIndex(Integer.parseInt(str1));
        else
          SwingProperties.this.searchStyle.setSelectedIndex(0);
      }
      else
      {
        SwingProperties.this.searchStyle.setSelectedIndex(0);
      }
      str1 = SwingProperties.this.properties.getValue("maxmultiviewers");
      if ((str1 != null) && (!str1.isEmpty()))
        SwingProperties.this.maxMultiViewers.setText(str1);
      str1 = SwingProperties.this.properties.getValue("sideTabBarCollapseLength");
      if ((str1 != null) && (!str1.isEmpty()))
        SwingProperties.this.sideTabLength.setText(str1);
      else
        SwingProperties.this.sideTabLength.setText("30");
      str1 = SwingProperties.this.properties.getValue("consistentTabBar");
      if ((!str1.isEmpty()) && (str1.equals("true")))
        SwingProperties.this.constantTabs.setSelected(true);
      else
        SwingProperties.this.constantTabs.setSelected(false);
      String str2 = SwingProperties.this.properties.getValue("showMouseSelectionBox");
      if ((!str2.isEmpty()) && (str2.toLowerCase().equals("true")))
        SwingProperties.this.showMouseSelectionBox.setSelected(true);
      else
        SwingProperties.this.showMouseSelectionBox.setSelected(false);
      JPanel localJPanel1 = makePanel(Messages.getMessage("PdfPreferences.InterfaceTitle"));
      JTabbedPane localJTabbedPane = new JTabbedPane();
      JPanel localJPanel2 = new JPanel();
      JScrollPane localJScrollPane1 = new JScrollPane(localJPanel2);
      localJScrollPane1.setBorder(BorderFactory.createEmptyBorder());
      localJPanel2.setLayout(new GridBagLayout());
      GridBagConstraints localGridBagConstraints = new GridBagConstraints();
      localGridBagConstraints.fill = 1;
      localGridBagConstraints.insets = new Insets(5, 0, 0, 5);
      localGridBagConstraints.gridwidth = 1;
      localGridBagConstraints.gridy = 0;
      localGridBagConstraints.weighty = 0.0D;
      localGridBagConstraints.weightx = 0.0D;
      localGridBagConstraints.gridx = 0;
      JLabel localJLabel1 = new JLabel(Messages.getMessage("PdfPreferences.GeneralTitle"));
      localJLabel1.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
      localJLabel1.setFont(localJLabel1.getFont().deriveFont(1));
      localJPanel2.add(localJLabel1, localGridBagConstraints);
      localGridBagConstraints.gridy += 1;
      localGridBagConstraints.insets = new Insets(5, 0, 0, 5);
      localGridBagConstraints.gridx = 0;
      localGridBagConstraints.gridwidth = 2;
      SwingProperties.this.enhancedGUI.setMargin(new Insets(0, 0, 0, 0));
      SwingProperties.this.enhancedGUI.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
      localJPanel2.add(SwingProperties.this.enhancedGUI, localGridBagConstraints);
      localGridBagConstraints.gridy += 1;
      localGridBagConstraints.insets = new Insets(3, 0, 0, 0);
      localGridBagConstraints.gridwidth = 1;
      localGridBagConstraints.gridx = 0;
      localJPanel2.add(SwingProperties.this.windowTitleText, localGridBagConstraints);
      localGridBagConstraints.gridx = 1;
      localJPanel2.add(SwingProperties.this.windowTitle, localGridBagConstraints);
      localGridBagConstraints.gridy += 1;
      localGridBagConstraints.insets = new Insets(5, 0, 0, 0);
      localGridBagConstraints.gridwidth = 1;
      localGridBagConstraints.gridx = 0;
      localJPanel2.add(SwingProperties.this.iconLocationText, localGridBagConstraints);
      localGridBagConstraints.gridx = 1;
      localJPanel2.add(SwingProperties.this.iconLocation, localGridBagConstraints);
      localGridBagConstraints.gridy += 1;
      localGridBagConstraints.insets = new Insets(5, 0, 0, 5);
      localGridBagConstraints.gridx = 0;
      JLabel localJLabel2 = new JLabel(Messages.getMessage("PageLayoutViewMenu.SearchLayout"));
      localJLabel2.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
      localJPanel2.add(localJLabel2, localGridBagConstraints);
      localGridBagConstraints.insets = new Insets(5, 0, 0, 0);
      localGridBagConstraints.weightx = 1.0D;
      localGridBagConstraints.gridx = 1;
      localJPanel2.add(SwingProperties.this.searchStyle, localGridBagConstraints);
      localGridBagConstraints.gridy += 1;
      localGridBagConstraints.insets = new Insets(10, 0, 0, 5);
      localGridBagConstraints.weighty = 0.0D;
      localGridBagConstraints.weightx = 0.0D;
      localGridBagConstraints.gridx = 0;
      JLabel localJLabel3 = new JLabel(Messages.getMessage("PdfPreferences.MaxMultiViewers"));
      localJLabel3.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
      localJPanel2.add(localJLabel3, localGridBagConstraints);
      localGridBagConstraints.insets = new Insets(5, 0, 0, 0);
      localGridBagConstraints.weightx = 1.0D;
      localGridBagConstraints.gridx = 1;
      localJPanel2.add(SwingProperties.this.maxMultiViewers, localGridBagConstraints);
      localGridBagConstraints.gridy += 1;
      localGridBagConstraints.insets = new Insets(15, 0, 0, 5);
      localGridBagConstraints.gridwidth = 1;
      localGridBagConstraints.weighty = 0.0D;
      localGridBagConstraints.weightx = 0.0D;
      localGridBagConstraints.gridx = 0;
      JLabel localJLabel4 = new JLabel(Messages.getMessage("PdfPreferences.SideTab"));
      localJLabel4.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
      localJLabel4.setFont(localJLabel4.getFont().deriveFont(1));
      localJPanel2.add(localJLabel4, localGridBagConstraints);
      localGridBagConstraints.gridy += 1;
      localGridBagConstraints.insets = new Insets(5, 0, 0, 0);
      localGridBagConstraints.gridwidth = 1;
      localGridBagConstraints.gridx = 0;
      SwingProperties.this.sideTabLengthText.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
      localJPanel2.add(SwingProperties.this.sideTabLengthText, localGridBagConstraints);
      localGridBagConstraints.insets = new Insets(5, 0, 0, 0);
      localGridBagConstraints.weightx = 1.0D;
      localGridBagConstraints.gridx = 1;
      localJPanel2.add(SwingProperties.this.sideTabLength, localGridBagConstraints);
      localGridBagConstraints.gridy += 1;
      localGridBagConstraints.insets = new Insets(5, 0, 0, 0);
      localGridBagConstraints.gridwidth = 2;
      localGridBagConstraints.gridx = 0;
      SwingProperties.this.constantTabs.setMargin(new Insets(0, 0, 0, 0));
      SwingProperties.this.constantTabs.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
      localJPanel2.add(SwingProperties.this.constantTabs, localGridBagConstraints);
      localGridBagConstraints.gridy += 1;
      localGridBagConstraints.weighty = 1.0D;
      localGridBagConstraints.gridx = 0;
      localJPanel2.add(Box.createVerticalGlue(), localGridBagConstraints);
      localJTabbedPane.add(Messages.getMessage("PdfPreferences.AppearanceTab"), localJScrollPane1);
      JPanel localJPanel3 = new JPanel();
      JScrollPane localJScrollPane2 = new JScrollPane(localJPanel3);
      localJScrollPane2.setBorder(BorderFactory.createEmptyBorder());
      localJPanel3.setLayout(new GridBagLayout());
      localGridBagConstraints = new GridBagConstraints();
      localGridBagConstraints.fill = 1;
      localGridBagConstraints.insets = new Insets(5, 0, 0, 5);
      localGridBagConstraints.gridwidth = 1;
      localGridBagConstraints.gridy = 0;
      localGridBagConstraints.weighty = 0.0D;
      localGridBagConstraints.weightx = 0.0D;
      localGridBagConstraints.gridx = 0;
      JLabel localJLabel5 = new JLabel(Messages.getMessage("PdfPreferences.GeneralTitle"));
      localJLabel5.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
      localJLabel5.setFont(localJLabel5.getFont().deriveFont(1));
      localJPanel3.add(localJLabel5, localGridBagConstraints);
      localGridBagConstraints.gridy += 1;
      localGridBagConstraints.gridwidth = 2;
      localGridBagConstraints.gridx = 0;
      SwingProperties.this.rightClick.setMargin(new Insets(0, 0, 0, 0));
      SwingProperties.this.rightClick.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
      localJPanel3.add(SwingProperties.this.rightClick, localGridBagConstraints);
      localGridBagConstraints.gridy += 1;
      localGridBagConstraints.gridwidth = 2;
      localGridBagConstraints.gridx = 0;
      SwingProperties.this.scrollwheelZoom.setMargin(new Insets(0, 0, 0, 0));
      SwingProperties.this.scrollwheelZoom.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
      localJPanel3.add(SwingProperties.this.scrollwheelZoom, localGridBagConstraints);
      localGridBagConstraints.gridy += 1;
      localGridBagConstraints.insets = new Insets(0, 0, 0, 5);
      localGridBagConstraints.gridwidth = 1;
      localGridBagConstraints.gridx = 0;
      localJPanel3.add(SwingProperties.this.showMouseSelectionBox, localGridBagConstraints);
      localGridBagConstraints.gridy += 1;
      localGridBagConstraints.weighty = 1.0D;
      localGridBagConstraints.gridx = 0;
      localJPanel3.add(Box.createVerticalGlue(), localGridBagConstraints);
      localJTabbedPane.add(Messages.getMessage("PdfPreferences.Mouse"), localJScrollPane2);
      JPanel localJPanel4 = new JPanel();
      JScrollPane localJScrollPane3 = new JScrollPane(localJPanel4);
      localJScrollPane3.setBorder(BorderFactory.createEmptyBorder());
      localJPanel4.setLayout(new GridBagLayout());
      localGridBagConstraints = new GridBagConstraints();
      localGridBagConstraints.fill = 1;
      localGridBagConstraints.insets = new Insets(5, 0, 0, 5);
      localGridBagConstraints.gridwidth = 1;
      localGridBagConstraints.gridy = 0;
      localGridBagConstraints.weighty = 0.0D;
      localGridBagConstraints.weightx = 0.0D;
      localGridBagConstraints.gridx = 0;
      JLabel localJLabel6 = new JLabel(Messages.getMessage("PdfPreferences.GeneralTitle"));
      localJLabel6.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
      localJLabel6.setFont(localJLabel6.getFont().deriveFont(1));
      localJPanel4.add(localJLabel6, localGridBagConstraints);
      if (SwingProperties.access$200())
      {
        SwingProperties.this.voiceSelect = new JComboBox(Speech.listVoices());
        localGridBagConstraints.gridy += 1;
        JLabel localJLabel7 = new JLabel(Messages.getMessage("PdfPreferences.Voice"));
        localJLabel7.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        localJPanel4.add(localJLabel7, localGridBagConstraints);
        SwingProperties.this.voiceSelect.setSelectedItem(SwingProperties.this.properties.getValue("voice"));
        localGridBagConstraints.insets = new Insets(5, 0, 0, 0);
        localGridBagConstraints.weightx = 1.0D;
        localGridBagConstraints.gridx = 1;
        localJPanel4.add(SwingProperties.this.voiceSelect, localGridBagConstraints);
        localGridBagConstraints.gridy += 1;
        localGridBagConstraints.weighty = 1.0D;
        localGridBagConstraints.gridx = 0;
        localJPanel4.add(Box.createVerticalGlue(), localGridBagConstraints);
        localJTabbedPane.add(Messages.getMessage("PdfPreferences.Speech"), localJScrollPane3);
      }
      localJPanel1.add(localJTabbedPane, "Center");
      return localJPanel1;
    }

    private JPanel createPrintingSettings()
    {
      String str = SwingProperties.this.properties.getValue("useHiResPrinting");
      if ((!str.isEmpty()) && (str.equals("true")))
        SwingProperties.this.HiResPrint.setSelected(true);
      else
        SwingProperties.this.HiResPrint.setSelected(false);
      str = SwingProperties.this.properties.getValue("defaultPrinter");
      if ((str != null) && (!str.isEmpty()))
      {
        SwingProperties.this.defaultPrinter.setSelectedItem(str);
      }
      else
      {
        PrintService localPrintService = PrintServiceLookup.lookupDefaultPrintService();
        if (localPrintService != null)
          SwingProperties.this.defaultPrinter.setSelectedItem("System Default (" + localPrintService.getName() + ')');
        else
          SwingProperties.this.defaultPrinter.setSelectedItem("System Default");
      }
      str = SwingProperties.this.properties.getValue("printerBlacklist");
      if ((str != null) && (!str.isEmpty()))
        SwingProperties.this.printerBlacklist.setText(str);
      str = SwingProperties.this.properties.getValue("defaultDPI");
      if ((str != null) && (!str.isEmpty()))
        try
        {
          str = str.replaceAll("[^0-9]", "");
          SwingProperties.this.defaultDPI.setText(Integer.parseInt(str) + "dpi");
        }
        catch (Exception localException)
        {
        }
      JPanel localJPanel1 = makePanel(Messages.getMessage("PdfPreferences.PrintingTitle"));
      JPanel localJPanel2 = new JPanel();
      JScrollPane localJScrollPane = new JScrollPane(localJPanel2);
      localJScrollPane.setBorder(BorderFactory.createEmptyBorder());
      localJPanel2.setLayout(new GridBagLayout());
      GridBagConstraints localGridBagConstraints = new GridBagConstraints();
      localGridBagConstraints.fill = 1;
      localGridBagConstraints.insets = new Insets(5, 0, 0, 5);
      localGridBagConstraints.weighty = 0.0D;
      localGridBagConstraints.weightx = 0.0D;
      localGridBagConstraints.gridx = 0;
      localGridBagConstraints.gridy = 0;
      JLabel localJLabel = new JLabel(Messages.getMessage("PdfPreferences.GeneralSection"));
      localJLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
      localJLabel.setFont(localJLabel.getFont().deriveFont(1));
      localJPanel2.add(localJLabel, localGridBagConstraints);
      localGridBagConstraints.gridy += 1;
      localGridBagConstraints.gridwidth = 2;
      localGridBagConstraints.gridx = 0;
      SwingProperties.this.HiResPrint.setMargin(new Insets(0, 0, 0, 0));
      SwingProperties.this.HiResPrint.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
      localJPanel2.add(SwingProperties.this.HiResPrint, localGridBagConstraints);
      localGridBagConstraints.gridy += 1;
      localGridBagConstraints.insets = new Insets(5, 0, 0, 0);
      localGridBagConstraints.gridwidth = 1;
      localGridBagConstraints.gridx = 0;
      localJPanel2.add(SwingProperties.this.defaultPrinterText, localGridBagConstraints);
      localGridBagConstraints.gridx = 1;
      localJPanel2.add(SwingProperties.this.defaultPrinter, localGridBagConstraints);
      localGridBagConstraints.gridy += 1;
      localGridBagConstraints.insets = new Insets(5, 0, 0, 0);
      localGridBagConstraints.gridwidth = 1;
      localGridBagConstraints.gridx = 0;
      localJPanel2.add(SwingProperties.this.defaultPagesizeText, localGridBagConstraints);
      localGridBagConstraints.gridx = 1;
      localJPanel2.add(SwingProperties.this.defaultPagesize, localGridBagConstraints);
      localGridBagConstraints.gridy += 1;
      localGridBagConstraints.insets = new Insets(5, 0, 0, 0);
      localGridBagConstraints.gridwidth = 1;
      localGridBagConstraints.gridx = 0;
      localJPanel2.add(SwingProperties.this.defaultDPIText, localGridBagConstraints);
      localGridBagConstraints.gridx = 1;
      localJPanel2.add(SwingProperties.this.defaultDPI, localGridBagConstraints);
      localGridBagConstraints.gridy += 1;
      localGridBagConstraints.insets = new Insets(5, 0, 0, 0);
      localGridBagConstraints.gridwidth = 1;
      localGridBagConstraints.gridx = 0;
      localJPanel2.add(SwingProperties.this.printerBlacklistText, localGridBagConstraints);
      localGridBagConstraints.gridx = 1;
      localGridBagConstraints.weightx = 1.0D;
      localJPanel2.add(SwingProperties.this.printerBlacklist, localGridBagConstraints);
      localGridBagConstraints.weightx = 0.0D;
      localGridBagConstraints.gridy += 1;
      localGridBagConstraints.weighty = 1.0D;
      localGridBagConstraints.gridx = 0;
      localJPanel2.add(Box.createVerticalGlue(), localGridBagConstraints);
      localJPanel1.add(localJScrollPane);
      return localJPanel1;
    }

    private JPanel createColorSettings()
    {
      JPanel localJPanel1 = makePanel(Messages.getMessage("PdfPreferences.MenuTitle"));
      JPanel localJPanel2 = new JPanel(new GridBagLayout());
      String str1 = SwingProperties.this.properties.getValue("highlightBoxColor");
      int i;
      if (!str1.isEmpty())
        i = Integer.parseInt(str1);
      else
        i = DecoderOptions.highlightColor.getRGB();
      final Color localColor1 = new Color(i);
      SwingProperties.this.highlightBoxColor.setBackground(localColor1);
      str1 = SwingProperties.this.properties.getValue("highlightTextColor");
      int j = 0;
      if (!str1.isEmpty())
        j = Integer.parseInt(str1);
      else if (DecoderOptions.backgroundColor != null)
        j = DecoderOptions.backgroundColor.getRGB();
      final Color localColor2 = new Color(j);
      SwingProperties.this.highlightTextColor.setBackground(localColor2);
      final JButton localJButton1 = new JButton(Messages.getMessage("PdfPreferences.ChangeHighlightColor"));
      localJButton1.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent paramAnonymousActionEvent)
        {
          Color localColor = JColorChooser.showDialog(null, "Highlight Color", localColor1);
          SwingProperties.this.highlightBoxColor.setBackground(localColor);
        }
      });
      String str2 = SwingProperties.this.properties.getValue("highlightComposite");
      if (!str2.isEmpty())
        SwingProperties.this.highlightComposite.setText(str2);
      String str3 = SwingProperties.this.properties.getValue("invertHighlights");
      if ((!str3.isEmpty()) && (str3.toLowerCase().equals("true")))
        SwingProperties.this.invertHighlight.setSelected(true);
      else
        SwingProperties.this.invertHighlight.setSelected(false);
      final JButton localJButton2 = new JButton(Messages.getMessage("PdfPreferences.ChangeHighlightTextColor"));
      localJButton2.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent paramAnonymousActionEvent)
        {
          Color localColor = JColorChooser.showDialog(null, "Highlighted Text Color", localColor2);
          SwingProperties.this.highlightTextColor.setBackground(localColor);
        }
      });
      final JLabel localJLabel1 = new JLabel(Messages.getMessage("PdfPreferences.ChangeHighlightTransparency"));
      if (SwingProperties.this.invertHighlight.isSelected())
      {
        SwingProperties.this.highlightBoxColor.setEnabled(false);
        SwingProperties.this.highlightTextColor.setEnabled(false);
        SwingProperties.this.highlightComposite.setEnabled(false);
        localJButton2.setEnabled(false);
        localJButton1.setEnabled(false);
        localJLabel1.setEnabled(false);
      }
      else
      {
        SwingProperties.this.highlightBoxColor.setEnabled(true);
        SwingProperties.this.highlightTextColor.setEnabled(true);
        SwingProperties.this.highlightComposite.setEnabled(true);
        localJButton2.setEnabled(true);
        localJButton1.setEnabled(true);
        localJLabel1.setEnabled(true);
      }
      SwingProperties.this.invertHighlight.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent paramAnonymousActionEvent)
        {
          if (((JCheckBox)paramAnonymousActionEvent.getSource()).isSelected())
          {
            SwingProperties.this.highlightBoxColor.setEnabled(false);
            SwingProperties.this.highlightTextColor.setEnabled(false);
            SwingProperties.this.highlightComposite.setEnabled(false);
            localJButton2.setEnabled(false);
            localJButton1.setEnabled(false);
            localJLabel1.setEnabled(false);
          }
          else
          {
            SwingProperties.this.highlightBoxColor.setEnabled(true);
            SwingProperties.this.highlightTextColor.setEnabled(true);
            SwingProperties.this.highlightComposite.setEnabled(true);
            localJButton2.setEnabled(true);
            localJButton1.setEnabled(true);
            localJLabel1.setEnabled(true);
          }
        }
      });
      str1 = SwingProperties.this.properties.getValue("vbgColor");
      int k = 0;
      if (!str1.isEmpty())
        k = Integer.parseInt(str1);
      else if (DecoderOptions.backgroundColor != null)
        k = DecoderOptions.backgroundColor.getRGB();
      final Color localColor3 = new Color(k);
      SwingProperties.this.viewBGColor.setBackground(localColor3);
      JButton localJButton3 = new JButton(Messages.getMessage("PdfPreferences.ChangeBackgroundColor"));
      localJButton3.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent paramAnonymousActionEvent)
        {
          Color localColor = JColorChooser.showDialog(null, "BackGround Color", localColor3);
          SwingProperties.this.viewBGColor.setBackground(localColor);
        }
      });
      str1 = SwingProperties.this.properties.getValue("vfgColor");
      int m = 0;
      if (!str1.isEmpty())
        m = Integer.parseInt(str1);
      final Color localColor4 = new Color(m);
      SwingProperties.this.foreGroundColor.setBackground(localColor4);
      final JButton localJButton4 = new JButton(Messages.getMessage("PdfPreferences.ChangeForegroundColor"));
      localJButton4.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent paramAnonymousActionEvent)
        {
          Color localColor = JColorChooser.showDialog(null, "Foreground Color", localColor4);
          SwingProperties.this.foreGroundColor.setBackground(localColor);
        }
      });
      String str4 = SwingProperties.this.properties.getValue("changeTextAndLineart");
      if ((!str4.isEmpty()) && (str4.toLowerCase().equals("true")))
        SwingProperties.this.changeTextAndLineArt.setSelected(true);
      else
        SwingProperties.this.changeTextAndLineArt.setSelected(false);
      String str5 = SwingProperties.this.properties.getValue("replaceDocumentTextColors");
      if ((!str5.isEmpty()) && (str5.toLowerCase().equals("true")))
        SwingProperties.this.replaceDocTextCol.setSelected(true);
      else
        SwingProperties.this.replaceDocTextCol.setSelected(false);
      if (SwingProperties.this.replaceDocTextCol.isSelected())
      {
        localJButton4.setEnabled(true);
        SwingProperties.this.foreGroundColor.setEnabled(true);
        SwingProperties.this.changeTextAndLineArt.setEnabled(true);
      }
      else
      {
        localJButton4.setEnabled(false);
        SwingProperties.this.foreGroundColor.setEnabled(false);
        SwingProperties.this.changeTextAndLineArt.setEnabled(false);
      }
      SwingProperties.this.replaceDocTextCol.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent paramAnonymousActionEvent)
        {
          if (((JCheckBox)paramAnonymousActionEvent.getSource()).isSelected())
          {
            localJButton4.setEnabled(true);
            SwingProperties.this.foreGroundColor.setEnabled(true);
            SwingProperties.this.changeTextAndLineArt.setEnabled(true);
          }
          else
          {
            localJButton4.setEnabled(false);
            SwingProperties.this.foreGroundColor.setEnabled(false);
            SwingProperties.this.changeTextAndLineArt.setEnabled(false);
          }
        }
      });
      str1 = SwingProperties.this.properties.getValue("pdfDisplayBackground");
      int n = 0;
      if (!str1.isEmpty())
        n = Integer.parseInt(str1);
      final Color localColor5 = new Color(n);
      SwingProperties.this.pdfDecoderBackground.setBackground(localColor5);
      final JButton localJButton5 = new JButton(Messages.getMessage("PdfPreferences.ChangeDisplayBackgroundColor"));
      localJButton5.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent paramAnonymousActionEvent)
        {
          Color localColor = JColorChooser.showDialog(null, "Display Background Color", localColor5);
          SwingProperties.this.pdfDecoderBackground.setBackground(localColor);
        }
      });
      String str6 = SwingProperties.this.properties.getValue("replacePdfDisplayBackground");
      if ((!str6.isEmpty()) && (str6.toLowerCase().equals("true")))
        SwingProperties.this.replaceDisplayBGCol.setSelected(true);
      else
        SwingProperties.this.replaceDisplayBGCol.setSelected(false);
      if (SwingProperties.this.replaceDisplayBGCol.isSelected())
      {
        localJButton5.setEnabled(true);
        SwingProperties.this.pdfDecoderBackground.setEnabled(true);
      }
      else
      {
        localJButton5.setEnabled(false);
        SwingProperties.this.pdfDecoderBackground.setEnabled(false);
      }
      SwingProperties.this.replaceDisplayBGCol.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent paramAnonymousActionEvent)
        {
          if (((JCheckBox)paramAnonymousActionEvent.getSource()).isSelected())
          {
            localJButton5.setEnabled(true);
            SwingProperties.this.pdfDecoderBackground.setEnabled(true);
          }
          else
          {
            localJButton5.setEnabled(false);
            SwingProperties.this.pdfDecoderBackground.setEnabled(false);
          }
        }
      });
      GridBagConstraints localGridBagConstraints = new GridBagConstraints();
      localGridBagConstraints.fill = 1;
      localGridBagConstraints.insets = new Insets(5, 0, 0, 5);
      localGridBagConstraints.gridwidth = 1;
      localGridBagConstraints.gridy = 0;
      localGridBagConstraints.weighty = 0.0D;
      localGridBagConstraints.weightx = 0.0D;
      localGridBagConstraints.gridx = 0;
      JLabel localJLabel2 = new JLabel("Highlights");
      localJLabel2.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
      localJLabel2.setFont(localJLabel2.getFont().deriveFont(1));
      localJPanel2.add(localJLabel2, localGridBagConstraints);
      localGridBagConstraints.gridy += 1;
      localGridBagConstraints.insets = new Insets(5, 0, 0, 5);
      localGridBagConstraints.gridx = 0;
      SwingProperties.this.highlightBoxColor.setBorder(BorderFactory.createLineBorder(Color.BLACK));
      localJPanel2.add(SwingProperties.this.highlightBoxColor, localGridBagConstraints);
      localGridBagConstraints.insets = new Insets(5, 0, 0, 0);
      localGridBagConstraints.gridwidth = 1;
      localGridBagConstraints.gridx = 1;
      localGridBagConstraints.weightx = 1.0D;
      localJPanel2.add(localJButton1, localGridBagConstraints);
      localGridBagConstraints.gridy += 1;
      localGridBagConstraints.insets = new Insets(5, 0, 0, 5);
      localGridBagConstraints.gridwidth = 1;
      localGridBagConstraints.gridx = 0;
      localGridBagConstraints.weightx = 0.0D;
      SwingProperties.this.highlightTextColor.setBorder(BorderFactory.createLineBorder(Color.BLACK));
      localJPanel2.add(SwingProperties.this.highlightTextColor, localGridBagConstraints);
      localGridBagConstraints.insets = new Insets(5, 0, 0, 0);
      localGridBagConstraints.gridwidth = 1;
      localGridBagConstraints.gridx = 1;
      localJPanel2.add(localJButton2, localGridBagConstraints);
      localGridBagConstraints.gridy += 1;
      localGridBagConstraints.insets = new Insets(5, 0, 0, 5);
      localGridBagConstraints.gridwidth = 1;
      localGridBagConstraints.gridx = 0;
      localJPanel2.add(SwingProperties.this.highlightComposite, localGridBagConstraints);
      localGridBagConstraints.insets = new Insets(5, 0, 0, 0);
      localGridBagConstraints.gridwidth = 1;
      localGridBagConstraints.gridx = 1;
      localJPanel2.add(localJLabel1, localGridBagConstraints);
      localGridBagConstraints.gridy += 1;
      localGridBagConstraints.insets = new Insets(0, 0, 0, 5);
      localGridBagConstraints.gridwidth = 1;
      localGridBagConstraints.gridx = 0;
      localJPanel2.add(SwingProperties.this.invertHighlight, localGridBagConstraints);
      localGridBagConstraints.gridy += 1;
      localGridBagConstraints.insets = new Insets(15, 0, 0, 5);
      localGridBagConstraints.gridwidth = 1;
      localGridBagConstraints.weighty = 0.0D;
      localGridBagConstraints.weightx = 0.0D;
      localGridBagConstraints.gridx = 0;
      JLabel localJLabel3 = new JLabel("Dispaly Colors");
      localJLabel3.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
      localJLabel3.setFont(localJLabel3.getFont().deriveFont(1));
      localJPanel2.add(localJLabel3, localGridBagConstraints);
      localGridBagConstraints.gridy += 1;
      localGridBagConstraints.insets = new Insets(5, 0, 0, 5);
      localGridBagConstraints.gridx = 0;
      SwingProperties.this.viewBGColor.setBorder(BorderFactory.createLineBorder(Color.BLACK));
      localJPanel2.add(SwingProperties.this.viewBGColor, localGridBagConstraints);
      localGridBagConstraints.insets = new Insets(5, 0, 0, 0);
      localGridBagConstraints.gridwidth = 1;
      localGridBagConstraints.gridx = 1;
      localGridBagConstraints.weightx = 1.0D;
      localJPanel2.add(localJButton3, localGridBagConstraints);
      localGridBagConstraints.gridy += 1;
      localGridBagConstraints.insets = new Insets(5, 0, 0, 0);
      localGridBagConstraints.gridwidth = 1;
      localGridBagConstraints.gridx = 0;
      localGridBagConstraints.weightx = 1.0D;
      localJPanel2.add(SwingProperties.this.replaceDocTextCol, localGridBagConstraints);
      localGridBagConstraints.gridy += 1;
      localGridBagConstraints.insets = new Insets(5, 0, 0, 5);
      localGridBagConstraints.gridx = 0;
      SwingProperties.this.foreGroundColor.setBorder(BorderFactory.createLineBorder(Color.BLACK));
      localJPanel2.add(SwingProperties.this.foreGroundColor, localGridBagConstraints);
      localGridBagConstraints.insets = new Insets(5, 0, 0, 0);
      localGridBagConstraints.gridwidth = 1;
      localGridBagConstraints.gridx = 1;
      localGridBagConstraints.weightx = 1.0D;
      localJPanel2.add(localJButton4, localGridBagConstraints);
      localGridBagConstraints.gridy += 1;
      localGridBagConstraints.insets = new Insets(5, 0, 0, 5);
      localGridBagConstraints.gridwidth = 1;
      localGridBagConstraints.gridx = 0;
      localJPanel2.add(SwingProperties.this.changeTextAndLineArt, localGridBagConstraints);
      localGridBagConstraints.gridy += 1;
      localGridBagConstraints.insets = new Insets(5, 0, 0, 0);
      localGridBagConstraints.gridwidth = 1;
      localGridBagConstraints.gridx = 0;
      localGridBagConstraints.weightx = 1.0D;
      localJPanel2.add(SwingProperties.this.replaceDisplayBGCol, localGridBagConstraints);
      localGridBagConstraints.gridy += 1;
      localGridBagConstraints.insets = new Insets(5, 0, 0, 5);
      localGridBagConstraints.gridx = 0;
      SwingProperties.this.pdfDecoderBackground.setBorder(BorderFactory.createLineBorder(Color.BLACK));
      localJPanel2.add(SwingProperties.this.pdfDecoderBackground, localGridBagConstraints);
      localGridBagConstraints.insets = new Insets(5, 0, 0, 0);
      localGridBagConstraints.gridwidth = 1;
      localGridBagConstraints.gridx = 1;
      localGridBagConstraints.weightx = 1.0D;
      localJPanel2.add(localJButton5, localGridBagConstraints);
      localJPanel1.add(localJPanel2, "Center");
      return localJPanel1;
    }

    private JPanel createMenuSettings()
    {
      JPanel localJPanel1 = makePanel(Messages.getMessage("PdfPreferences.MenuTitle"));
      JPanel localJPanel2 = new JPanel(new BorderLayout());
      SwingProperties.this.tabs = new JTabbedPane();
      for (int i = 0; i != SwingProperties.this.menuTabs.length; i++)
      {
        SwingProperties.this.reverseMessage.put(Messages.getMessage("PdfCustomGui." + SwingProperties.this.menuTabs[i]), SwingProperties.this.menuTabs[i]);
        CheckNode localCheckNode = new CheckNode(Messages.getMessage("PdfCustomGui." + SwingProperties.this.menuTabs[i]));
        localCheckNode.setEnabled(true);
        localCheckNode.setSelected(true);
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(localCheckNode);
        NodeList localNodeList = SwingProperties.this.properties.getChildren(Messages.getMessage(new StringBuilder().append("PdfCustomGui.").append(SwingProperties.this.menuTabs[i]).toString()) + "Menu");
        addMenuToTree(i, localNodeList, localCheckNode, localArrayList);
        final JTree localJTree = new JTree(localCheckNode);
        JScrollPane localJScrollPane = new JScrollPane(localJTree);
        localJTree.setCellRenderer(new CheckRenderer());
        localJTree.getSelectionModel().setSelectionMode(1);
        localJTree.addTreeSelectionListener(new TreeSelectionListener()
        {
          private void setChildrenValue(CheckNode paramAnonymousCheckNode, boolean paramAnonymousBoolean)
          {
            for (int i = 0; i != paramAnonymousCheckNode.getChildCount(); i++)
            {
              ((CheckNode)paramAnonymousCheckNode.getChildAt(i)).setSelected(paramAnonymousBoolean);
              if (paramAnonymousCheckNode.getChildAt(i).getChildCount() > 0)
                setChildrenValue((CheckNode)paramAnonymousCheckNode.getChildAt(i), paramAnonymousBoolean);
            }
          }

          private void setParentValue(CheckNode paramAnonymousCheckNode, boolean paramAnonymousBoolean)
          {
            paramAnonymousCheckNode.setSelected(paramAnonymousBoolean);
            if (paramAnonymousCheckNode.getParent() != null)
              setParentValue((CheckNode)paramAnonymousCheckNode.getParent(), paramAnonymousBoolean);
          }

          public void valueChanged(TreeSelectionEvent paramAnonymousTreeSelectionEvent)
          {
            final DefaultMutableTreeNode localDefaultMutableTreeNode = (DefaultMutableTreeNode)localJTree.getLastSelectedPathComponent();
            Runnable local1 = new Runnable()
            {
              public void run()
              {
                CheckNode localCheckNode = (CheckNode)localDefaultMutableTreeNode;
                if (localCheckNode != null)
                {
                  boolean bool = !localCheckNode.isSelected();
                  if ((SwingProperties.this.reverseMessage.get(localCheckNode.getText()).equals("Preferences")) && (!bool))
                  {
                    int i = JOptionPane.showConfirmDialog(SwingProperties.this.propertiesDialog, "Disabling this option will mean you can not acces this menu using this properties file. Do you want to continue?", "Preferences Access", 0);
                    if (i == 1)
                      bool = !bool;
                  }
                  if (localCheckNode.getChildCount() > 0)
                    SwingProperties.ButtonBarPanel.9.this.setChildrenValue(localCheckNode, bool);
                  if ((localCheckNode.getParent() != null) && (bool))
                    SwingProperties.ButtonBarPanel.9.this.setParentValue((CheckNode)localCheckNode.getParent(), bool);
                  localCheckNode.setSelected(bool);
                  SwingProperties.ButtonBarPanel.9.this.val$tree.invalidate();
                  SwingProperties.ButtonBarPanel.9.this.val$tree.clearSelection();
                  SwingProperties.ButtonBarPanel.9.this.val$tree.repaint();
                }
              }
            };
            SwingUtilities.invokeLater(local1);
          }
        });
        JPanel localJPanel3 = new JPanel(new BorderLayout());
        final JButton localJButton = new JButton();
        String str = SwingProperties.this.properties.getValue(SwingProperties.this.menuTabs[i]);
        if (str.toLowerCase().equals("true"))
          localJButton.setText(Messages.getMessage("PdfCustomGui.HideGuiSection") + ' ' + Messages.getMessage(new StringBuilder().append("PdfCustomGui.").append(SwingProperties.this.menuTabs[i]).toString()));
        else
          localJButton.setText(Messages.getMessage("PdfCustomGui.ShowGuiSection") + ' ' + Messages.getMessage(new StringBuilder().append("PdfCustomGui.").append(SwingProperties.this.menuTabs[i]).toString()));
        final int j = i;
        localJButton.addActionListener(new ActionListener()
        {
          public void actionPerformed(ActionEvent paramAnonymousActionEvent)
          {
            if (localJButton.getText().startsWith("Click here to show "))
              localJButton.setText(Messages.getMessage("PdfCustomGui.HideGuiSection") + ' ' + Messages.getMessage(new StringBuilder().append("PdfCustomGui.").append(SwingProperties.this.menuTabs[j]).toString()));
            else
              localJButton.setText(Messages.getMessage("PdfCustomGui.ShowGuiSection") + ' ' + Messages.getMessage(new StringBuilder().append("PdfCustomGui.").append(SwingProperties.this.menuTabs[j]).toString()));
          }
        });
        localJPanel3.add(localJScrollPane, "Center");
        localJPanel3.add(localJButton, "South");
        SwingProperties.this.tabs.add(localJPanel3, Messages.getMessage("PdfCustomGui." + SwingProperties.this.menuTabs[i]));
      }
      localJPanel2.add(SwingProperties.this.tabs, "Center");
      localJPanel1.add(localJPanel2, "Center");
      return localJPanel1;
    }

    private boolean removeOption(String paramString)
    {
      return (paramString.equals("ExportMenu")) || (paramString.equals("PagetoolsMenu"));
    }

    private void addMenuToTree(int paramInt, NodeList paramNodeList, CheckNode paramCheckNode, List paramList)
    {
      for (int i = 0; i != paramNodeList.getLength(); i++)
        if (i < paramNodeList.getLength())
        {
          String str1 = paramNodeList.item(i).getNodeName();
          if ((!removeOption(str1)) && (!str1.startsWith("#")))
          {
            CheckNode localCheckNode = new CheckNode(Messages.getMessage("PdfCustomGui." + str1));
            localCheckNode.setEnabled(true);
            SwingProperties.this.reverseMessage.put(Messages.getMessage("PdfCustomGui." + str1), str1);
            String str2 = SwingProperties.this.properties.getValue(str1);
            if ((!str2.isEmpty()) && (str2.equals("true")))
              localCheckNode.setSelected(true);
            else
              localCheckNode.setSelected(false);
            if (paramNodeList.item(i).hasChildNodes())
            {
              paramList.add(paramCheckNode);
              paramCheckNode.add(localCheckNode);
              addMenuToTree(paramInt, paramNodeList.item(i).getChildNodes(), localCheckNode, paramList);
            }
            else
            {
              paramCheckNode.add(localCheckNode);
            }
          }
        }
    }

    private JPanel createExtensionsPane()
    {
      JPanel localJPanel1 = makePanel(Messages.getMessage("PdfPreferences.ExtensionsTitle"));
      final JPanel localJPanel2 = new JPanel();
      localJPanel2.setLayout(new GridBagLayout());
      GridBagConstraints localGridBagConstraints = new GridBagConstraints();
      localGridBagConstraints.fill = 2;
      localGridBagConstraints.gridy = 0;
      localGridBagConstraints.insets = new Insets(12, 2, 5, 2);
      JLabel localJLabel1 = new JLabel(Messages.getMessage("PdfPreferences.ExtensionName"));
      Font localFont = localJLabel1.getFont().deriveFont(1, localJLabel1.getFont().getSize2D());
      localJLabel1.setFont(localFont);
      localGridBagConstraints.weightx = 0.0D;
      localGridBagConstraints.gridx = 0;
      localJPanel2.add(localJLabel1, localGridBagConstraints);
      JLabel localJLabel2 = new JLabel(Messages.getMessage("PdfPreferences.ExtensionDescription"));
      localJLabel2.setFont(localFont);
      localGridBagConstraints.weightx = 1.0D;
      localGridBagConstraints.gridx = 1;
      localJPanel2.add(localJLabel2, localGridBagConstraints);
      JLabel localJLabel3 = new JLabel(Messages.getMessage("PdfPreferences.ExtensionVersion"));
      localJLabel3.setFont(localFont);
      localGridBagConstraints.weightx = 0.0D;
      localGridBagConstraints.gridx = 2;
      localJPanel2.add(localJLabel3, localGridBagConstraints);
      localGridBagConstraints.insets = new Insets(2, 2, 2, 2);
      String str1 = "java: " + System.getProperty("java.vendor") + ' ' + System.getProperty("java.version") + '\n';
      str1 = str1 + "os: " + System.getProperty("os.name") + ' ' + System.getProperty("os.version") + ' ' + System.getProperty("os.arch") + '\n';
      str1 = str1 + "jpedal: 5.06b04\n";
      localGridBagConstraints.gridy += 1;
      localGridBagConstraints.gridx = 0;
      localJPanel2.add(new JLabel("BCMail"), localGridBagConstraints);
      localGridBagConstraints.gridx = 1;
      localJPanel2.add(new JLabel("<html>" + Messages.getMessage("PdfExtensions.BCMail.text")), localGridBagConstraints);
      localGridBagConstraints.gridx = 2;
      String str2 = "Unknown version";
      Object localObject1;
      Object localObject2;
      Object localObject4;
      JLabel localJLabel4;
      try
      {
        Class localClass = Class.forName("org.bouncycastle.jcajce.JcaJceHelper");
        String str3 = localClass.getName().replace('.', '/');
        localObject1 = localClass.getResource('/' + str3 + ".class").getPath().split("!");
        localObject2 = new URL(localObject1[0]);
        localObject4 = new JarFile(((URL)localObject2).getFile());
        if (!((JarFile)localObject4).getManifest().getMainAttributes().getValue("Implementation-Version").isEmpty())
          str2 = ((JarFile)localObject4).getManifest().getMainAttributes().getValue("Implementation-Version");
        localJLabel4 = new JLabel("<html>" + str2);
        str1 = str1 + "bcmail: " + str2 + '\n';
      }
      catch (Exception localException1)
      {
        localJLabel4 = new JLabel("<html><u>" + Messages.getMessage("PdfExtensions.getText") + "</u></html>");
        localJLabel4.setForeground(Color.BLUE);
        localJLabel4.addMouseListener(new MouseAdapter()
        {
          private String url;

          public void mouseEntered(MouseEvent paramAnonymousMouseEvent)
          {
            if (SingleDisplay.allowChangeCursor)
              localJPanel2.setCursor(Cursor.getPredefinedCursor(12));
          }

          public void mouseExited(MouseEvent paramAnonymousMouseEvent)
          {
            if (SingleDisplay.allowChangeCursor)
              localJPanel2.setCursor(Cursor.getDefaultCursor());
          }

          public void mouseClicked(MouseEvent paramAnonymousMouseEvent)
          {
            try
            {
              BrowserLauncher.openURL(this.url);
            }
            catch (IOException localIOException)
            {
              JOptionPane.showMessageDialog(localJPanel2, Messages.getMessage("PdfViewer.ErrorWebsite"));
            }
          }
        });
      }
      catch (Error localError)
      {
        localJLabel4 = new JLabel("<html><u>" + Messages.getMessage("PdfExtensions.getText") + "</u></html>");
        localJLabel4.setForeground(Color.BLUE);
        localJLabel4.addMouseListener(new MouseAdapter()
        {
          private String url;

          public void mouseEntered(MouseEvent paramAnonymousMouseEvent)
          {
            if (SingleDisplay.allowChangeCursor)
              localJPanel2.setCursor(Cursor.getPredefinedCursor(12));
          }

          public void mouseExited(MouseEvent paramAnonymousMouseEvent)
          {
            if (SingleDisplay.allowChangeCursor)
              localJPanel2.setCursor(Cursor.getDefaultCursor());
          }

          public void mouseClicked(MouseEvent paramAnonymousMouseEvent)
          {
            try
            {
              BrowserLauncher.openURL(this.url);
            }
            catch (IOException localIOException)
            {
              JOptionPane.showMessageDialog(localJPanel2, Messages.getMessage("PdfViewer.ErrorWebsite"));
            }
          }
        });
      }
      localJPanel2.add(localJLabel4, localGridBagConstraints);
      localGridBagConstraints.gridy += 1;
      localGridBagConstraints.gridx = 0;
      localJPanel2.add(new JLabel("CID"), localGridBagConstraints);
      localGridBagConstraints.gridx = 1;
      localJPanel2.add(new JLabel("<html>" + Messages.getMessage("PdfExtensions.CID.text")), localGridBagConstraints);
      localGridBagConstraints.gridx = 2;
      try
      {
        JLabel localJLabel5;
        if (SwingProperties.class.getResourceAsStream("/org/jpedal/res/cid/00_ReadMe.pdf") != null)
        {
          localJLabel5 = new JLabel("<html>1.0");
          str1 = str1 + "cid: 1.0\n";
        }
        else
        {
          localJLabel5 = new JLabel("<html><u>" + Messages.getMessage("PdfExtensions.getText") + "</u></html>");
          localJLabel5.setForeground(Color.BLUE);
          localJLabel5.addMouseListener(new MouseAdapter()
          {
            private String url;

            public void mouseEntered(MouseEvent paramAnonymousMouseEvent)
            {
              if (SingleDisplay.allowChangeCursor)
                localJPanel2.setCursor(Cursor.getPredefinedCursor(12));
            }

            public void mouseExited(MouseEvent paramAnonymousMouseEvent)
            {
              if (SingleDisplay.allowChangeCursor)
                localJPanel2.setCursor(Cursor.getDefaultCursor());
            }

            public void mouseClicked(MouseEvent paramAnonymousMouseEvent)
            {
              try
              {
                BrowserLauncher.openURL(this.url);
              }
              catch (IOException localIOException)
              {
                JOptionPane.showMessageDialog(localJPanel2, Messages.getMessage("PdfViewer.ErrorWebsite"));
              }
            }
          });
        }
        localJPanel2.add(localJLabel5, localGridBagConstraints);
      }
      catch (Exception localException2)
      {
        localException2.printStackTrace();
      }
      localGridBagConstraints.gridy += 1;
      localGridBagConstraints.gridx = 0;
      localJPanel2.add(new JLabel("FreeTTS"), localGridBagConstraints);
      localGridBagConstraints.gridx = 1;
      localJPanel2.add(new JLabel("<html>" + Messages.getMessage("PdfExtensions.FreeTTS.text")), localGridBagConstraints);
      localGridBagConstraints.gridx = 2;
      JLabel localJLabel6;
      if (SwingProperties.access$200())
      {
        str2 = "FreeTTS 1.2.2".replaceAll("FreeTTS ", "");
        localJLabel6 = new JLabel("<html>" + str2);
        str1 = str1 + "FreeTTS: FreeTTS 1.2.2\n";
      }
      else
      {
        localJLabel6 = new JLabel("<html><u>" + Messages.getMessage("PdfExtensions.getText") + "</u></html>");
        localJLabel6.setForeground(Color.BLUE);
        localJLabel6.addMouseListener(new MouseAdapter()
        {
          private String url;

          public void mouseEntered(MouseEvent paramAnonymousMouseEvent)
          {
            if (SingleDisplay.allowChangeCursor)
              localJPanel2.setCursor(Cursor.getPredefinedCursor(12));
          }

          public void mouseExited(MouseEvent paramAnonymousMouseEvent)
          {
            if (SingleDisplay.allowChangeCursor)
              localJPanel2.setCursor(Cursor.getDefaultCursor());
          }

          public void mouseClicked(MouseEvent paramAnonymousMouseEvent)
          {
            try
            {
              BrowserLauncher.openURL(this.url);
            }
            catch (IOException localIOException)
            {
              JOptionPane.showMessageDialog(localJPanel2, Messages.getMessage("PdfViewer.ErrorWebsite"));
            }
          }
        });
      }
      localJPanel2.add(localJLabel6, localGridBagConstraints);
      localGridBagConstraints.gridy += 1;
      localGridBagConstraints.gridx = 0;
      localJPanel2.add(new JLabel("JAI"), localGridBagConstraints);
      localGridBagConstraints.gridx = 1;
      localJPanel2.add(new JLabel("<html>" + Messages.getMessage("PdfExtensions.JAI.text")), localGridBagConstraints);
      localGridBagConstraints.gridx = 2;
      Object localObject5;
      Object localObject3;
      if (JAIHelper.isJAIOnClasspath())
      {
        str2 = "Unknown version";
        try
        {
          localObject2 = Class.forName("javax.media.jai.JAI");
          localObject4 = ((Class)localObject2).getName().replace('.', '/');
          localObject5 = ((Class)localObject2).getResource('/' + (String)localObject4 + ".class").getPath().split("!");
          localObject6 = new URL(localObject5[0]);
          localObject7 = new JarFile(((URL)localObject6).getFile());
          if (!((JarFile)localObject7).getManifest().getMainAttributes().getValue("Implementation-Version").isEmpty())
            str2 = ((JarFile)localObject7).getManifest().getMainAttributes().getValue("Implementation-Version");
        }
        catch (Exception localException3)
        {
        }
        localObject3 = JAI.getBuildVersion();
        str1 = str1 + "jai: " + str2 + ' ' + (String)localObject3 + '\n';
        localObject1 = new JLabel("<html>" + str2);
      }
      else
      {
        localObject1 = new JLabel("<html><u>" + Messages.getMessage("PdfExtensions.getText") + "</u></html>");
        ((JLabel)localObject1).setForeground(Color.BLUE);
        ((JLabel)localObject1).addMouseListener(new MouseAdapter()
        {
          private String url;

          public void mouseEntered(MouseEvent paramAnonymousMouseEvent)
          {
            if (SingleDisplay.allowChangeCursor)
              localJPanel2.setCursor(Cursor.getPredefinedCursor(12));
          }

          public void mouseExited(MouseEvent paramAnonymousMouseEvent)
          {
            if (SingleDisplay.allowChangeCursor)
              localJPanel2.setCursor(Cursor.getDefaultCursor());
          }

          public void mouseClicked(MouseEvent paramAnonymousMouseEvent)
          {
            try
            {
              BrowserLauncher.openURL(this.url);
            }
            catch (IOException localIOException)
            {
              JOptionPane.showMessageDialog(localJPanel2, Messages.getMessage("PdfViewer.ErrorWebsite"));
            }
          }
        });
      }
      localJPanel2.add((Component)localObject1, localGridBagConstraints);
      localGridBagConstraints.gridy += 1;
      localGridBagConstraints.gridx = 0;
      localJPanel2.add(new JLabel("JavaFX"), localGridBagConstraints);
      localGridBagConstraints.gridx = 1;
      localJPanel2.add(new JLabel("<html>" + Messages.getMessage("PdfExtensions.JavaFX.text")), localGridBagConstraints);
      localGridBagConstraints.gridx = 2;
      if (JavaFXHelper.isJavaFXAvailable())
      {
        str2 = JavaFXHelper.getVersion();
        localObject3 = new JLabel("<html>" + str2.replaceAll("build", "b").replaceAll("[(|)]", ""));
        str1 = str1 + "javafx: " + str2 + '\n';
      }
      else
      {
        localObject3 = new JLabel("<html><u>" + Messages.getMessage("PdfExtensions.getText") + "</u></html>");
        ((JLabel)localObject3).setForeground(Color.BLUE);
        ((JLabel)localObject3).addMouseListener(new MouseAdapter()
        {
          private String url;

          public void mouseEntered(MouseEvent paramAnonymousMouseEvent)
          {
            if (SingleDisplay.allowChangeCursor)
              localJPanel2.setCursor(Cursor.getPredefinedCursor(12));
          }

          public void mouseExited(MouseEvent paramAnonymousMouseEvent)
          {
            if (SingleDisplay.allowChangeCursor)
              localJPanel2.setCursor(Cursor.getDefaultCursor());
          }

          public void mouseClicked(MouseEvent paramAnonymousMouseEvent)
          {
            try
            {
              BrowserLauncher.openURL(this.url);
            }
            catch (IOException localIOException)
            {
              JOptionPane.showMessageDialog(localJPanel2, Messages.getMessage("PdfViewer.ErrorWebsite"));
            }
          }
        });
      }
      localJPanel2.add((Component)localObject3, localGridBagConstraints);
      localGridBagConstraints.gridy += 1;
      localGridBagConstraints.gridx = 0;
      localJPanel2.add(new JLabel("JCE"), localGridBagConstraints);
      localGridBagConstraints.gridx = 1;
      localJPanel2.add(new JLabel("<html>" + Messages.getMessage("PdfExtensions.JCE.text")), localGridBagConstraints);
      localGridBagConstraints.gridx = 2;
      str2 = "Unknown version";
      try
      {
        localObject5 = Class.forName("org.bouncycastle.jce.provider.BouncyCastleProvider");
        localObject6 = ((Class)localObject5).getName().replace('.', '/');
        localObject7 = ((Class)localObject5).getResource('/' + (String)localObject6 + ".class").getPath().split("!");
        localObject8 = new URL(localObject7[0]);
        JarFile localJarFile = new JarFile(((URL)localObject8).getFile());
        if (!localJarFile.getManifest().getMainAttributes().getValue("Implementation-Version").isEmpty())
          str2 = localJarFile.getManifest().getMainAttributes().getValue("Implementation-Version");
        localObject4 = new JLabel("<html>" + str2);
        str1 = str1 + "jce: " + str2 + '\n';
      }
      catch (Exception localException4)
      {
        localObject4 = new JLabel("<html><u>" + Messages.getMessage("PdfExtensions.getText") + "</u></html>");
        ((JLabel)localObject4).setForeground(Color.BLUE);
        ((JLabel)localObject4).addMouseListener(new MouseAdapter()
        {
          private String url;

          public void mouseEntered(MouseEvent paramAnonymousMouseEvent)
          {
            if (SingleDisplay.allowChangeCursor)
              localJPanel2.setCursor(Cursor.getPredefinedCursor(12));
          }

          public void mouseExited(MouseEvent paramAnonymousMouseEvent)
          {
            if (SingleDisplay.allowChangeCursor)
              localJPanel2.setCursor(Cursor.getDefaultCursor());
          }

          public void mouseClicked(MouseEvent paramAnonymousMouseEvent)
          {
            try
            {
              BrowserLauncher.openURL(this.url);
            }
            catch (IOException localIOException)
            {
              JOptionPane.showMessageDialog(localJPanel2, Messages.getMessage("PdfViewer.ErrorWebsite"));
            }
          }
        });
      }
      localJPanel2.add((Component)localObject4, localGridBagConstraints);
      localGridBagConstraints.gridy += 1;
      localGridBagConstraints.gridx = 0;
      localJPanel2.add(new JLabel("Rhino"), localGridBagConstraints);
      localGridBagConstraints.gridx = 1;
      localJPanel2.add(new JLabel("<html>" + Messages.getMessage("PdfExtensions.Rhino.text")), localGridBagConstraints);
      localGridBagConstraints.gridx = 2;
      Object localObject6 = DefaultParser.class.getClassLoader().getResourceAsStream("org/mozilla/javascript/Context.class");
      JLabel localJLabel7;
      if (localObject6 != null)
      {
        str2 = ScriptRuntime.getMessage0("implementation.version");
        str1 = str1 + "rhino: " + str2 + '\n';
        localObject7 = "";
        if (!str2.replaceAll("release 1", "").equals(str2))
          localObject7 = " R1";
        if (!str2.replaceAll("release 2", "").equals(str2))
          localObject7 = " R2";
        str2 = str2.substring(0, 12).replaceAll("[^0-9|.]", "");
        localJLabel7 = new JLabel("<html>" + str2 + (String)localObject7);
      }
      else
      {
        localJLabel7 = new JLabel("<html><u>" + Messages.getMessage("PdfExtensions.getText") + "</u></html>");
        localJLabel7.setForeground(Color.BLUE);
        localJLabel7.addMouseListener(new MouseAdapter()
        {
          private String url;

          public void mouseEntered(MouseEvent paramAnonymousMouseEvent)
          {
            if (SingleDisplay.allowChangeCursor)
              localJPanel2.setCursor(Cursor.getPredefinedCursor(12));
          }

          public void mouseExited(MouseEvent paramAnonymousMouseEvent)
          {
            if (SingleDisplay.allowChangeCursor)
              localJPanel2.setCursor(Cursor.getDefaultCursor());
          }

          public void mouseClicked(MouseEvent paramAnonymousMouseEvent)
          {
            try
            {
              BrowserLauncher.openURL(this.url);
            }
            catch (IOException localIOException)
            {
              JOptionPane.showMessageDialog(localJPanel2, Messages.getMessage("PdfViewer.ErrorWebsite"));
            }
          }
        });
      }
      localJPanel2.add(localJLabel7, localGridBagConstraints);
      localGridBagConstraints.gridy += 1;
      localGridBagConstraints.weighty = 1.0D;
      localJPanel2.add(Box.createVerticalGlue(), localGridBagConstraints);
      localGridBagConstraints.gridy += 1;
      localGridBagConstraints.gridx = 0;
      localGridBagConstraints.gridwidth = 3;
      localGridBagConstraints.anchor = 26;
      localGridBagConstraints.weighty = 0.0D;
      localGridBagConstraints.fill = 13;
      Object localObject7 = new JButton(Messages.getMessage("PdfPreferences.CopyToClipboard"));
      Object localObject8 = str1;
      ((JButton)localObject7).addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent paramAnonymousActionEvent)
        {
          Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(this.val$finalDetails), null);
          JOptionPane.showMessageDialog(localJPanel2, Messages.getMessage("PdfExtensions.clipboard"));
        }
      });
      localJPanel2.add((Component)localObject7, localGridBagConstraints);
      localJPanel1.add(localJPanel2, "Center");
      return localJPanel1;
    }

    private void show(Component paramComponent)
    {
      if (this.currentComponent != null)
        remove(this.currentComponent);
      add("Center", this.currentComponent = paramComponent);
      revalidate();
      repaint();
    }

    private void addButton(String paramString1, String paramString2, final Component paramComponent, JPanel paramJPanel, ButtonGroup paramButtonGroup)
    {
      AbstractAction local12 = new AbstractAction(paramString1, new ImageIcon(getClass().getResource(paramString2)))
      {
        public void actionPerformed(ActionEvent paramAnonymousActionEvent)
        {
          SwingProperties.ButtonBarPanel.this.show(paramComponent);
        }
      };
      JToggleButton localJToggleButton = new JToggleButton(local12);
      localJToggleButton.setVerticalTextPosition(3);
      localJToggleButton.setHorizontalTextPosition(0);
      localJToggleButton.setContentAreaFilled(false);
      if (DecoderOptions.isRunningOnMac)
        localJToggleButton.setHorizontalAlignment(2);
      localJToggleButton.setAlignmentX(0.5F);
      paramJPanel.add(localJToggleButton);
      paramButtonGroup.add(localJToggleButton);
      if (paramButtonGroup.getSelection() == null)
      {
        localJToggleButton.setSelected(true);
        show(paramComponent);
      }
    }
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.gui.popups.SwingProperties
 * JD-Core Version:    0.6.2
 */