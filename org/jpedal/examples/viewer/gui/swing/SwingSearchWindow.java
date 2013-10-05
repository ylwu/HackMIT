package org.jpedal.examples.viewer.gui.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.jpedal.PdfDecoder;
import org.jpedal.display.swing.SingleDisplay;
import org.jpedal.examples.viewer.Commands;
import org.jpedal.examples.viewer.Values;
import org.jpedal.examples.viewer.Viewer;
import org.jpedal.examples.viewer.gui.SwingGUI;
import org.jpedal.examples.viewer.gui.generic.GUIButton;
import org.jpedal.examples.viewer.gui.generic.GUISearchWindow;
import org.jpedal.exception.PdfException;
import org.jpedal.external.JPedalActionHandler;
import org.jpedal.grouping.DefaultSearchListener;
import org.jpedal.grouping.PdfGroupingAlgorithms;
import org.jpedal.objects.PdfPageData;
import org.jpedal.text.TextLines;
import org.jpedal.utils.Messages;
import org.jpedal.utils.SwingWorker;
import org.jpedal.utils.repositories.Vector_Rectangle;

public class SwingSearchWindow extends JFrame
  implements GUISearchWindow
{
  public static int SEARCH_EXTERNAL_WINDOW = 0;
  public static int SEARCH_TABBED_PANE = 1;
  public static int SEARCH_MENU_BAR = 2;
  private boolean backGroundSearch = false;
  int style = 0;
  private boolean isSetup = false;
  boolean usingMenuBarSearch = false;
  int lastPage = -1;
  String defaultMessage = "Search PDF Here";
  JProgressBar progress = new JProgressBar(0, 100);
  JTextField searchText = null;
  JTextField searchCount;
  DefaultListModel listModel;
  SearchList resultsList;
  JLabel label = null;
  private JPanel advancedPanel;
  private JComboBox searchType;
  private JCheckBox wholeWordsOnlyBox;
  private JCheckBox caseSensitiveBox;
  private JCheckBox multiLineBox;
  private JCheckBox highlightAll;
  private JCheckBox searchAll;
  private JCheckBox useRegEx;
  SwingWorker searcher = null;
  public boolean isSearch = false;
  public boolean hasSearched = false;
  public boolean requestInterupt = false;
  JButton searchButton = null;
  private int itemFoundCount = 0;
  Map textPages = new HashMap();
  Map textRectangles = new HashMap();
  String[] searchTerms = { "" };
  boolean singlePageSearch = false;
  final JPanel nav = new JPanel();
  Values commonValues;
  SwingGUI currentGUI;
  PdfDecoder decode_pdf;
  int searchTypeParameters = 0;
  int firstPageWithResults = 0;
  private boolean deleteOnClick;

  public void setWholeWords(boolean paramBoolean)
  {
    this.wholeWordsOnlyBox.setSelected(paramBoolean);
  }

  public void setCaseSensitive(boolean paramBoolean)
  {
    this.caseSensitiveBox.setSelected(paramBoolean);
  }

  public void setMultiLine(boolean paramBoolean)
  {
    this.multiLineBox.setSelected(paramBoolean);
  }

  public SwingSearchWindow(SwingGUI paramSwingGUI)
  {
    this.currentGUI = paramSwingGUI;
    setName("searchFrame");
  }

  public Component getContentPanel()
  {
    return getContentPane();
  }

  public void init(PdfDecoder paramPdfDecoder, Values paramValues)
  {
    this.decode_pdf = paramPdfDecoder;
    this.commonValues = paramValues;
    if (this.isSetup)
    {
      this.searchCount.setText(Messages.getMessage("PdfViewerSearch.ItemsFound") + ' ' + this.itemFoundCount);
      this.searchText.selectAll();
      this.searchText.grabFocus();
    }
    else
    {
      this.isSetup = true;
      setTitle(Messages.getMessage("PdfViewerSearchGUITitle.DefaultMessage"));
      this.defaultMessage = Messages.getMessage("PdfViewerSearchGUI.DefaultMessage");
      this.searchText = new JTextField(10);
      this.searchText.setText(this.defaultMessage);
      this.searchText.setName("searchText");
      this.searchText.addFocusListener(new SearchTextFocusListener());
      this.searchButton = new JButton(Messages.getMessage("PdfViewerSearch.Button"));
      this.advancedPanel = new JPanel(new GridBagLayout());
      this.searchType = new JComboBox(new String[] { Messages.getMessage("PdfViewerSearch.MatchWhole"), Messages.getMessage("PdfViewerSearch.MatchAny") });
      this.wholeWordsOnlyBox = new JCheckBox(Messages.getMessage("PdfViewerSearch.WholeWords"));
      this.wholeWordsOnlyBox.setName("wholeWords");
      this.caseSensitiveBox = new JCheckBox(Messages.getMessage("PdfViewerSearch.CaseSense"));
      this.caseSensitiveBox.setName("caseSensitive");
      this.multiLineBox = new JCheckBox(Messages.getMessage("PdfViewerSearch.MultiLine"));
      this.multiLineBox.setName("multiLine");
      this.highlightAll = new JCheckBox(Messages.getMessage("PdfViewerSearch.HighlightsCheckBox"));
      this.highlightAll.setName("highlightAll");
      this.useRegEx = new JCheckBox(Messages.getMessage("PdfViewerSearch.RegExCheckBox"));
      this.useRegEx.setName("useregex");
      this.searchType.setName("combo");
      GridBagConstraints localGridBagConstraints = new GridBagConstraints();
      this.advancedPanel.setPreferredSize(new Dimension(this.advancedPanel.getPreferredSize().width, 150));
      localGridBagConstraints.gridx = 0;
      localGridBagConstraints.gridy = 0;
      localGridBagConstraints.anchor = 19;
      localGridBagConstraints.fill = 2;
      localGridBagConstraints.weightx = 1.0D;
      localGridBagConstraints.weighty = 0.0D;
      this.advancedPanel.add(new JLabel(Messages.getMessage("PdfViewerSearch.ReturnResultsAs")), localGridBagConstraints);
      localGridBagConstraints.insets = new Insets(5, 0, 0, 0);
      localGridBagConstraints.gridy = 1;
      this.advancedPanel.add(this.searchType, localGridBagConstraints);
      localGridBagConstraints.gridy = 2;
      this.advancedPanel.add(new JLabel(Messages.getMessage("PdfViewerSearch.AdditionalOptions")), localGridBagConstraints);
      localGridBagConstraints.insets = new Insets(0, 0, 0, 0);
      localGridBagConstraints.weighty = 1.0D;
      localGridBagConstraints.gridy = 3;
      this.advancedPanel.add(this.wholeWordsOnlyBox, localGridBagConstraints);
      localGridBagConstraints.weighty = 1.0D;
      localGridBagConstraints.gridy = 4;
      this.advancedPanel.add(this.caseSensitiveBox, localGridBagConstraints);
      localGridBagConstraints.weighty = 1.0D;
      localGridBagConstraints.gridy = 5;
      this.advancedPanel.add(this.multiLineBox, localGridBagConstraints);
      localGridBagConstraints.weighty = 1.0D;
      localGridBagConstraints.gridy = 6;
      this.advancedPanel.add(this.highlightAll, localGridBagConstraints);
      localGridBagConstraints.weighty = 1.0D;
      localGridBagConstraints.gridy = 7;
      this.advancedPanel.add(this.useRegEx, localGridBagConstraints);
      this.advancedPanel.setVisible(false);
      this.nav.setLayout(new BorderLayout());
      addWindowListener(new WindowListener()
      {
        public void windowOpened(WindowEvent paramAnonymousWindowEvent)
        {
        }

        public void windowClosing(WindowEvent paramAnonymousWindowEvent)
        {
          SwingSearchWindow.this.removeSearchWindow(true);
        }

        public void windowClosed(WindowEvent paramAnonymousWindowEvent)
        {
        }

        public void windowIconified(WindowEvent paramAnonymousWindowEvent)
        {
        }

        public void windowDeiconified(WindowEvent paramAnonymousWindowEvent)
        {
        }

        public void windowActivated(WindowEvent paramAnonymousWindowEvent)
        {
        }

        public void windowDeactivated(WindowEvent paramAnonymousWindowEvent)
        {
        }
      });
      this.nav.add(this.searchButton, "East");
      this.nav.add(this.searchText, "Center");
      this.searchAll = new JCheckBox();
      this.searchAll.setSelected(true);
      this.searchAll.setText(Messages.getMessage("PdfViewerSearch.CheckBox"));
      JPanel localJPanel = new JPanel();
      localJPanel.setLayout(new BorderLayout());
      localJPanel.add(this.searchAll, "North");
      this.label = new JLabel("<html><center> Show Advanced");
      this.label.setForeground(Color.blue);
      this.label.setName("advSearch");
      this.label.addMouseListener(new MouseListener()
      {
        boolean isVisible = false;
        String text = "Show Advanced";

        public void mouseEntered(MouseEvent paramAnonymousMouseEvent)
        {
          if (SingleDisplay.allowChangeCursor)
            SwingSearchWindow.this.nav.setCursor(new Cursor(12));
          SwingSearchWindow.this.label.setText("<html><center><a href=" + this.text + '>' + this.text + "</a></center>");
        }

        public void mouseExited(MouseEvent paramAnonymousMouseEvent)
        {
          if (SingleDisplay.allowChangeCursor)
            SwingSearchWindow.this.nav.setCursor(new Cursor(0));
          SwingSearchWindow.this.label.setText("<html><center>" + this.text);
        }

        public void mouseClicked(MouseEvent paramAnonymousMouseEvent)
        {
          if (this.isVisible)
          {
            this.text = Messages.getMessage("PdfViewerSearch.ShowOptions");
            SwingSearchWindow.this.label.setText("<html><center><a href=" + this.text + '>' + this.text + "</a></center>");
            SwingSearchWindow.this.advancedPanel.setVisible(false);
          }
          else
          {
            this.text = Messages.getMessage("PdfViewerSearch.HideOptions");
            SwingSearchWindow.this.label.setText("<html><center><a href=" + this.text + '>' + this.text + "</a></center>");
            SwingSearchWindow.this.advancedPanel.setVisible(true);
          }
          this.isVisible = (!this.isVisible);
        }

        public void mousePressed(MouseEvent paramAnonymousMouseEvent)
        {
        }

        public void mouseReleased(MouseEvent paramAnonymousMouseEvent)
        {
        }
      });
      this.label.setBorder(BorderFactory.createEmptyBorder(3, 4, 4, 4));
      localJPanel.add(this.label, "South");
      this.nav.add(localJPanel, "North");
      this.itemFoundCount = 0;
      this.textPages.clear();
      this.textRectangles.clear();
      this.listModel = null;
      this.searchCount = new JTextField(Messages.getMessage("PdfViewerSearch.ItemsFound") + ' ' + this.itemFoundCount);
      this.searchCount.setEditable(false);
      this.nav.add(this.searchCount, "South");
      this.listModel = new DefaultListModel();
      this.resultsList = new SearchList(this.listModel, this.textPages, this.textRectangles);
      this.resultsList.setName("results");
      this.resultsList.addListSelectionListener(new Object()
      {
        public void valueChanged(ListSelectionEvent paramAnonymousListSelectionEvent)
        {
          if (!paramAnonymousListSelectionEvent.getValueIsAdjusting())
          {
            if (!Values.isProcessing())
            {
              float f = SwingSearchWindow.this.currentGUI.getScaling();
              int i = SwingSearchWindow.this.resultsList.getSelectedIndex();
              SwingSearchWindow.this.decode_pdf.getTextLines().clearHighlights();
              if (i != -1)
              {
                Integer localInteger = Integer.valueOf(i);
                Object localObject1 = SwingSearchWindow.this.textPages.get(localInteger);
                if (localObject1 != null)
                {
                  int j = ((Integer)localObject1).intValue();
                  if (SwingSearchWindow.this.commonValues.getCurrentPage() != j)
                  {
                    SwingSearchWindow.this.commonValues.setCurrentPage(j);
                    SwingSearchWindow.this.currentGUI.resetStatusMessage(Messages.getMessage("PdfViewer.LoadingPage") + ' ' + SwingSearchWindow.this.commonValues.getCurrentPage());
                    SwingSearchWindow.this.decode_pdf.setPageParameters(f, SwingSearchWindow.this.commonValues.getCurrentPage());
                    SwingSearchWindow.this.currentGUI.decodePage();
                    SwingSearchWindow.this.decode_pdf.invalidate();
                  }
                  while (Values.isProcessing())
                    try
                    {
                      Thread.sleep(500L);
                    }
                    catch (InterruptedException localInterruptedException)
                    {
                      localInterruptedException.printStackTrace();
                    }
                  Object localObject3;
                  Object localObject2;
                  if ((SwingSearchWindow.this.searchTypeParameters & 0x10) == 16)
                  {
                    Vector_Rectangle localVector_Rectangle1 = new Vector_Rectangle();
                    int m = -1;
                    for (int n = 0; n != SwingSearchWindow.this.resultsList.getModel().getSize(); n++)
                    {
                      localObject3 = SwingSearchWindow.this.textPages.get(Integer.valueOf(n));
                      if (localObject3 != null)
                      {
                        int i1 = ((Integer)localObject3).intValue();
                        if (i1 != m)
                        {
                          localVector_Rectangle1.trim();
                          localObject2 = localVector_Rectangle1.get();
                          for (int i2 = 0; i2 != localObject2.length; i2++)
                            System.out.println(localObject2[i2]);
                          SwingSearchWindow.this.decode_pdf.getTextLines().addHighlights((Rectangle[])localObject2, true, m);
                          m = i1;
                          localVector_Rectangle1 = new Vector_Rectangle();
                        }
                        Object localObject4 = SwingSearchWindow.this.textRectangles.get(Integer.valueOf(n));
                        if ((localObject4 instanceof Rectangle))
                          localVector_Rectangle1.addElement((Rectangle)localObject4);
                        if ((localObject4 instanceof Rectangle[]))
                        {
                          Rectangle[] arrayOfRectangle2 = (Rectangle[])localObject4;
                          for (int i4 = 0; i4 != arrayOfRectangle2.length; i4++)
                            localVector_Rectangle1.addElement(arrayOfRectangle2[i4]);
                        }
                      }
                    }
                    localVector_Rectangle1.trim();
                    localObject2 = localVector_Rectangle1.get();
                    SwingSearchWindow.this.decode_pdf.getTextLines().addHighlights((Rectangle[])localObject2, true, m);
                  }
                  else
                  {
                    localObject2 = SwingSearchWindow.this.textPages.get(localInteger);
                    int k = ((Integer)localObject2).intValue();
                    Vector_Rectangle localVector_Rectangle2 = new Vector_Rectangle();
                    Rectangle localRectangle = null;
                    localObject3 = SwingSearchWindow.this.textRectangles.get(localInteger);
                    if ((localObject3 instanceof Rectangle))
                    {
                      localVector_Rectangle2.addElement((Rectangle)localObject3);
                      localRectangle = (Rectangle)localObject3;
                    }
                    if ((localObject3 instanceof Rectangle[]))
                    {
                      Rectangle[] arrayOfRectangle1 = (Rectangle[])localObject3;
                      localRectangle = arrayOfRectangle1[0];
                      for (int i3 = 0; i3 != arrayOfRectangle1.length; i3++)
                        localVector_Rectangle2.addElement(arrayOfRectangle1[i3]);
                    }
                    SwingSearchWindow.this.currentGUI.currentCommands.scrollRectToHighlight(localRectangle, k);
                    localVector_Rectangle2.trim();
                    SwingSearchWindow.this.decode_pdf.getTextLines().addHighlights(localVector_Rectangle2.get(), true, k);
                  }
                  SwingSearchWindow.this.decode_pdf.invalidate();
                  SwingSearchWindow.this.decode_pdf.repaint();
                  SwingSearchWindow.this.currentGUI.zoom();
                }
              }
            }
            if (SwingSearchWindow.this.commonValues.getCurrentPage() == 1)
              SwingSearchWindow.this.currentGUI.setBackNavigationButtonsEnabled(false);
            else
              SwingSearchWindow.this.currentGUI.setBackNavigationButtonsEnabled(true);
            if (SwingSearchWindow.this.commonValues.getCurrentPage() == SwingSearchWindow.this.decode_pdf.getPageCount())
              SwingSearchWindow.this.currentGUI.setForwardNavigationButtonsEnabled(false);
            else
              SwingSearchWindow.this.currentGUI.setForwardNavigationButtonsEnabled(true);
          }
          else
          {
            SwingSearchWindow.this.resultsList.repaint();
          }
        }
      });
      this.resultsList.setSelectionMode(1);
      this.searchButton.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent paramAnonymousActionEvent)
        {
          if (!SwingSearchWindow.this.isSearch)
          {
            try
            {
              SwingSearchWindow.this.searchTypeParameters = 0;
              if (SwingSearchWindow.this.wholeWordsOnlyBox.isSelected())
                SwingSearchWindow.this.searchTypeParameters |= 1;
              if (SwingSearchWindow.this.caseSensitiveBox.isSelected())
                SwingSearchWindow.this.searchTypeParameters |= 2;
              if (SwingSearchWindow.this.multiLineBox.isSelected())
                SwingSearchWindow.this.searchTypeParameters |= 8;
              if (SwingSearchWindow.this.highlightAll.isSelected())
                SwingSearchWindow.this.searchTypeParameters |= 16;
              if (SwingSearchWindow.this.useRegEx.isSelected())
                SwingSearchWindow.this.searchTypeParameters |= 32;
              String str = SwingSearchWindow.this.searchText.getText().trim();
              if (SwingSearchWindow.this.searchType.getSelectedIndex() == 0)
              {
                SwingSearchWindow.this.searchTerms = new String[] { str };
              }
              else
              {
                SwingSearchWindow.this.searchTerms = str.split(" ");
                for (int i = 0; i < SwingSearchWindow.this.searchTerms.length; i++)
                  SwingSearchWindow.this.searchTerms[i] = SwingSearchWindow.this.searchTerms[i].trim();
              }
              SwingSearchWindow.this.singlePageSearch = (!SwingSearchWindow.this.searchAll.isSelected());
              SwingSearchWindow.this.searchText();
            }
            catch (Exception localException)
            {
              localException.printStackTrace();
            }
          }
          else
          {
            SwingSearchWindow.this.requestInterupt = true;
            SwingSearchWindow.this.isSearch = false;
            SwingSearchWindow.this.searchButton.setText(Messages.getMessage("PdfViewerSearch.Button"));
          }
          SwingSearchWindow.this.currentGUI.getPdfDecoder().requestFocusInWindow();
        }
      });
      this.searchText.selectAll();
      this.deleteOnClick = true;
      this.searchText.addKeyListener(new KeyListener()
      {
        public void keyTyped(KeyEvent paramAnonymousKeyEvent)
        {
          int i = paramAnonymousKeyEvent.getID();
          if (i == 400)
          {
            int j = paramAnonymousKeyEvent.getKeyChar();
            if (j == 10)
              if (!SwingSearchWindow.this.decode_pdf.isOpen())
                SwingSearchWindow.this.currentGUI.showMessageDialog("File must be open before you can search.");
              else
                try
                {
                  SwingSearchWindow.this.isSearch = false;
                  SwingSearchWindow.this.searchTypeParameters = 0;
                  if (SwingSearchWindow.this.wholeWordsOnlyBox.isSelected())
                    SwingSearchWindow.this.searchTypeParameters |= 1;
                  if (SwingSearchWindow.this.caseSensitiveBox.isSelected())
                    SwingSearchWindow.this.searchTypeParameters |= 2;
                  if (SwingSearchWindow.this.multiLineBox.isSelected())
                    SwingSearchWindow.this.searchTypeParameters |= 8;
                  if (SwingSearchWindow.this.highlightAll.isSelected())
                    SwingSearchWindow.this.searchTypeParameters |= 16;
                  if (SwingSearchWindow.this.useRegEx.isSelected())
                    SwingSearchWindow.this.searchTypeParameters |= 32;
                  String str = SwingSearchWindow.this.searchText.getText().trim();
                  if (SwingSearchWindow.this.searchType.getSelectedIndex() == 0)
                  {
                    SwingSearchWindow.this.searchTerms = new String[] { str };
                  }
                  else
                  {
                    SwingSearchWindow.this.searchTerms = str.split(" ");
                    for (int k = 0; k < SwingSearchWindow.this.searchTerms.length; k++)
                      SwingSearchWindow.this.searchTerms[k] = SwingSearchWindow.this.searchTerms[k].trim();
                  }
                  SwingSearchWindow.this.singlePageSearch = (!SwingSearchWindow.this.searchAll.isSelected());
                  SwingSearchWindow.this.searchText();
                  SwingSearchWindow.this.currentGUI.getPdfDecoder().requestFocusInWindow();
                }
                catch (Exception localException)
                {
                  localException.printStackTrace();
                }
          }
        }

        public void keyPressed(KeyEvent paramAnonymousKeyEvent)
        {
        }

        public void keyReleased(KeyEvent paramAnonymousKeyEvent)
        {
          if (SwingSearchWindow.this.searchText.getText().isEmpty())
          {
            SwingSearchWindow.this.currentGUI.nextSearch.setEnabled(false);
            SwingSearchWindow.this.currentGUI.previousSearch.setEnabled(false);
          }
          else
          {
            SwingSearchWindow.this.currentGUI.nextSearch.setEnabled(true);
            SwingSearchWindow.this.currentGUI.previousSearch.setEnabled(true);
          }
        }
      });
      this.searchText.addFocusListener(new FocusListener()
      {
        public void focusLost(FocusEvent paramAnonymousFocusEvent)
        {
          if (SwingSearchWindow.this.searchText.getText().isEmpty())
          {
            SwingSearchWindow.this.searchText.setText(SwingSearchWindow.this.defaultMessage);
            SwingSearchWindow.this.deleteOnClick = true;
          }
        }

        public void focusGained(FocusEvent paramAnonymousFocusEvent)
        {
          if (SwingSearchWindow.this.deleteOnClick)
          {
            SwingSearchWindow.this.deleteOnClick = false;
            SwingSearchWindow.this.searchText.setText("");
          }
        }
      });
      if ((this.style == SEARCH_EXTERNAL_WINDOW) || (this.style == SEARCH_TABBED_PANE))
      {
        JScrollPane localJScrollPane = new JScrollPane();
        localJScrollPane.getViewport().add(this.resultsList);
        localJScrollPane.setVerticalScrollBarPolicy(20);
        localJScrollPane.setHorizontalScrollBarPolicy(30);
        localJScrollPane.getVerticalScrollBar().setUnitIncrement(80);
        localJScrollPane.getHorizontalScrollBar().setUnitIncrement(80);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(localJScrollPane, "Center");
        getContentPane().add(this.nav, "North");
        getContentPane().add(this.advancedPanel, "South");
        Container localContainer = this.currentGUI.getFrame();
        if ((this.commonValues.getModeOfOperation() == 1) && ((this.currentGUI.getFrame() instanceof JFrame)))
          localContainer = ((JFrame)this.currentGUI.getFrame()).getContentPane();
        if (this.style == SEARCH_EXTERNAL_WINDOW)
        {
          int i = 230;
          int j = localContainer.getHeight();
          int k = localContainer.getLocationOnScreen().x;
          int m = localContainer.getWidth() + k;
          int n = localContainer.getLocationOnScreen().y;
          Dimension localDimension = Toolkit.getDefaultToolkit().getScreenSize();
          int i1 = localDimension.width;
          if ((m + i > i1) && (this.style == SEARCH_EXTERNAL_WINDOW))
          {
            m = i1 - i;
            localContainer.setSize(m - k, localContainer.getHeight());
          }
          setSize(i, j);
          setLocation(m, n);
        }
        this.searchAll.setFocusable(false);
        this.searchText.grabFocus();
      }
      else
      {
        this.currentGUI.setSearchText(this.searchText);
      }
    }
  }

  public void findWithoutWindow(PdfDecoder paramPdfDecoder, Values paramValues, int paramInt, boolean paramBoolean1, boolean paramBoolean2, String paramString)
  {
    if (!this.isSearch)
    {
      this.backGroundSearch = true;
      this.isSearch = true;
      this.decode_pdf = paramPdfDecoder;
      this.commonValues = paramValues;
      this.decode_pdf.setLayout(new BorderLayout());
      this.decode_pdf.add(this.progress, "South");
      this.progress.setValue(0);
      this.progress.setMaximum(this.commonValues.getPageCount());
      this.progress.setVisible(true);
      this.decode_pdf.validate();
      String str = paramString;
      if (!paramBoolean1)
      {
        this.searchTerms = new String[] { str };
      }
      else
      {
        this.searchTerms = str.split(" ");
        for (int i = 0; i < this.searchTerms.length; i++)
          this.searchTerms[i] = this.searchTerms[i].trim();
      }
      this.searchTypeParameters = paramInt;
      this.singlePageSearch = paramBoolean2;
      find(paramPdfDecoder, paramValues);
    }
    else
    {
      this.currentGUI.showMessageDialog("Please wait for search to finish before starting another.");
    }
  }

  public void find(PdfDecoder paramPdfDecoder, Values paramValues)
  {
    if (!this.backGroundSearch)
    {
      init(paramPdfDecoder, paramValues);
      if (this.style == SEARCH_EXTERNAL_WINDOW)
        setVisible(true);
    }
    else
    {
      try
      {
        searchText();
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
      }
    }
  }

  public void removeSearchWindow(boolean paramBoolean)
  {
    setVisible(false);
    setVisible(false);
    if (this.searcher != null)
      this.searcher.interrupt();
    if ((this.isSetup) && (!paramBoolean))
    {
      if (this.listModel != null)
        this.listModel.clear();
      this.itemFoundCount = 0;
      this.isSearch = false;
    }
    if (this.decode_pdf != null)
    {
      this.decode_pdf.getTextLines().clearHighlights();
      this.decode_pdf.repaint();
    }
  }

  private void searchText()
    throws Exception
  {
    if (this.searcher != null)
      this.searcher.interrupt();
    this.usingMenuBarSearch = (this.style == SEARCH_MENU_BAR);
    this.lastPage = -1;
    if (this.listModel == null)
      this.listModel = new DefaultListModel();
    if (this.resultsList == null)
      this.resultsList = new SearchList(this.listModel, this.textPages, this.textRectangles);
    this.resultsList.setStatus(4);
    String str = "";
    for (int i = 0; i != this.searchTerms.length; i++)
      if (i < this.searchTerms.length - 1)
        str = str + this.searchTerms[i] + ", ";
      else
        str = str + this.searchTerms[i];
    this.resultsList.setSearchTerm(str);
    if (!this.backGroundSearch)
    {
      this.searchButton.setText(Messages.getMessage("PdfViewerSearchButton.Stop"));
      this.searchButton.invalidate();
      this.searchButton.repaint();
      this.searchCount.setText(Messages.getMessage("PdfViewerSearch.Scanning1"));
      this.searchCount.repaint();
    }
    this.searcher = new SwingWorker()
    {
      public Object construct()
      {
        SwingSearchWindow.this.isSearch = true;
        SwingSearchWindow.this.hasSearched = true;
        int i;
        try
        {
          SwingSearchWindow.this.listModel.removeAllElements();
          if (!SwingSearchWindow.this.backGroundSearch)
            SwingSearchWindow.this.resultsList.repaint();
          SwingSearchWindow.this.textPages.clear();
          SwingSearchWindow.this.textRectangles.clear();
          SwingSearchWindow.this.itemFoundCount = 0;
          SwingSearchWindow.this.decode_pdf.getTextLines().clearHighlights();
          PdfPageData localPdfPageData = SwingSearchWindow.this.decode_pdf.getPdfPageData();
          if ((SwingSearchWindow.this.singlePageSearch) || (SwingSearchWindow.this.usingMenuBarSearch))
          {
            if (SwingSearchWindow.this.singlePageSearch)
              SwingSearchWindow.this.searchPageRange(localPdfPageData, SwingSearchWindow.this.commonValues.getCurrentPage(), SwingSearchWindow.this.commonValues.getCurrentPage() + 1);
            else
              for (i = 0; (i != SwingSearchWindow.this.commonValues.getPageCount() + 1) && (SwingSearchWindow.this.resultsList.getResultCount() < 1); i++)
              {
                int j = SwingSearchWindow.this.commonValues.getCurrentPage() + i;
                if (j > SwingSearchWindow.this.commonValues.getPageCount())
                  j -= SwingSearchWindow.this.commonValues.getPageCount();
                SwingSearchWindow.this.searchPageRange(localPdfPageData, j, j + 1);
              }
          }
          else if ((!SwingSearchWindow.this.backGroundSearch) || (!SwingSearchWindow.this.usingMenuBarSearch))
            SwingSearchWindow.this.searchPageRange(localPdfPageData, 1, SwingSearchWindow.this.commonValues.getPageCount() + 1);
          if (!SwingSearchWindow.this.backGroundSearch)
          {
            SwingSearchWindow.this.searchCount.setText(Messages.getMessage("PdfViewerSearch.ItemsFound") + ' ' + SwingSearchWindow.this.itemFoundCount + "  " + Messages.getMessage("PdfViewerSearch.Done"));
            SwingSearchWindow.this.searchButton.setText(Messages.getMessage("PdfViewerSearch.Button"));
          }
          SwingSearchWindow.this.resultsList.invalidate();
          SwingSearchWindow.this.resultsList.repaint();
          SwingSearchWindow.this.resultsList.setSelectedIndex(0);
          SwingSearchWindow.this.resultsList.setLength(SwingSearchWindow.this.listModel.capacity());
          SwingSearchWindow.this.currentGUI.setResults(SwingSearchWindow.this.resultsList);
          SwingSearchWindow.this.currentGUI.nextSearch.setEnabled(true);
          SwingSearchWindow.this.currentGUI.previousSearch.setEnabled(true);
          SwingSearchWindow.this.isSearch = false;
          SwingSearchWindow.this.requestInterupt = false;
        }
        catch (InterruptedException localInterruptedException1)
        {
          SwingUtilities.invokeLater(new Runnable()
          {
            public void run()
            {
              SwingSearchWindow.this.requestInterupt = false;
              SwingSearchWindow.this.backGroundSearch = false;
              SwingSearchWindow.this.currentGUI.showMessageDialog("Search stopped by user.");
              if (!SwingSearchWindow.this.backGroundSearch)
              {
                SwingSearchWindow.this.currentGUI.nextSearch.setEnabled(true);
                SwingSearchWindow.this.currentGUI.previousSearch.setEnabled(true);
              }
            }
          });
        }
        catch (Exception localException)
        {
          SwingUtilities.invokeLater(new Runnable()
          {
            public void run()
            {
              SwingSearchWindow.this.requestInterupt = false;
              SwingSearchWindow.this.backGroundSearch = false;
              if (Viewer.showMessages)
                SwingSearchWindow.this.currentGUI.showMessageDialog("An error occured during search. Some results may be missing.\n\nPlease send the file to IDRSolutions for investigation.");
              if (!SwingSearchWindow.this.backGroundSearch)
              {
                SwingSearchWindow.this.currentGUI.nextSearch.setEnabled(true);
                SwingSearchWindow.this.currentGUI.previousSearch.setEnabled(true);
              }
            }
          });
        }
        if (!Values.isProcessing())
        {
          float f = SwingSearchWindow.this.currentGUI.getScaling();
          SwingSearchWindow.this.resultsList.setSelectedIndex(0);
          i = SwingSearchWindow.this.resultsList.getSelectedIndex();
          SwingSearchWindow.this.decode_pdf.getTextLines().clearHighlights();
          if ((i == -1) && (SwingSearchWindow.this.resultsList.getResultCount() > 0))
            i = 0;
          if (i != -1)
          {
            Integer localInteger = Integer.valueOf(i);
            Object localObject1 = SwingSearchWindow.this.textPages.get(localInteger);
            if (localObject1 != null)
            {
              int k = ((Integer)localObject1).intValue();
              if (SwingSearchWindow.this.commonValues.getCurrentPage() != k)
              {
                SwingSearchWindow.this.commonValues.setCurrentPage(k);
                SwingSearchWindow.this.currentGUI.resetStatusMessage(Messages.getMessage("PdfViewer.LoadingPage") + ' ' + SwingSearchWindow.this.commonValues.getCurrentPage());
                SwingSearchWindow.this.decode_pdf.setPageParameters(f, SwingSearchWindow.this.commonValues.getCurrentPage());
                SwingSearchWindow.this.currentGUI.decodePage();
                SwingSearchWindow.this.decode_pdf.invalidate();
              }
              while (Values.isProcessing())
                try
                {
                  Thread.sleep(500L);
                }
                catch (InterruptedException localInterruptedException2)
                {
                  localInterruptedException2.printStackTrace();
                }
              SwingSearchWindow.this.firstPageWithResults = SwingSearchWindow.this.commonValues.getCurrentPage();
              Object localObject2;
              if ((SwingSearchWindow.this.searchTypeParameters & 0x10) == 16)
              {
                Vector_Rectangle localVector_Rectangle = new Vector_Rectangle();
                int m = -1;
                int n = 0;
                for (int i1 = 0; i1 != SwingSearchWindow.this.textPages.size(); i1++)
                {
                  Object localObject3 = SwingSearchWindow.this.textPages.get(Integer.valueOf(i1));
                  if (localObject3 != null)
                  {
                    n = ((Integer)localObject3).intValue();
                    if ((n != m) && (m != -1))
                    {
                      localVector_Rectangle.trim();
                      localObject2 = localVector_Rectangle.get();
                      SwingSearchWindow.this.decode_pdf.getTextLines().addHighlights((Rectangle[])localObject2, true, m);
                      m = n;
                      localVector_Rectangle = new Vector_Rectangle();
                    }
                    Object localObject4 = SwingSearchWindow.this.textRectangles.get(Integer.valueOf(i1));
                    if ((localObject4 instanceof Rectangle))
                      localVector_Rectangle.addElement((Rectangle)localObject4);
                    if ((localObject4 instanceof Rectangle[]))
                    {
                      Rectangle[] arrayOfRectangle = (Rectangle[])localObject4;
                      for (int i2 = 0; i2 != arrayOfRectangle.length; i2++)
                        localVector_Rectangle.addElement(arrayOfRectangle[i2]);
                    }
                  }
                }
                localVector_Rectangle.trim();
                localObject2 = localVector_Rectangle.get();
                SwingSearchWindow.this.decode_pdf.getTextLines().addHighlights((Rectangle[])localObject2, true, n);
              }
              else
              {
                localObject2 = SwingSearchWindow.this.textRectangles.get(localInteger);
                if ((localObject2 instanceof Rectangle))
                {
                  SwingSearchWindow.this.currentGUI.currentCommands.scrollRectToHighlight((Rectangle)localObject2, SwingSearchWindow.this.commonValues.getCurrentPage());
                  SwingSearchWindow.this.decode_pdf.getTextLines().addHighlights(new Rectangle[] { (Rectangle)localObject2 }, true, SwingSearchWindow.this.commonValues.getCurrentPage());
                }
                if ((localObject2 instanceof Rectangle[]))
                {
                  SwingSearchWindow.this.currentGUI.currentCommands.scrollRectToHighlight(((Rectangle[])(Rectangle[])localObject2)[0], SwingSearchWindow.this.commonValues.getCurrentPage());
                  SwingSearchWindow.this.decode_pdf.getTextLines().addHighlights((Rectangle[])localObject2, true, SwingSearchWindow.this.commonValues.getCurrentPage());
                }
              }
              SwingSearchWindow.this.decode_pdf.invalidate();
              SwingSearchWindow.this.decode_pdf.repaint();
            }
          }
        }
        if (SwingSearchWindow.this.commonValues.getCurrentPage() == 1)
          SwingSearchWindow.this.currentGUI.setBackNavigationButtonsEnabled(false);
        else
          SwingSearchWindow.this.currentGUI.setBackNavigationButtonsEnabled(true);
        if (SwingSearchWindow.this.commonValues.getCurrentPage() == SwingSearchWindow.this.decode_pdf.getPageCount())
          SwingSearchWindow.this.currentGUI.setForwardNavigationButtonsEnabled(false);
        else
          SwingSearchWindow.this.currentGUI.setForwardNavigationButtonsEnabled(true);
        SwingSearchWindow.this.decode_pdf.remove(SwingSearchWindow.this.progress);
        SwingSearchWindow.this.decode_pdf.validate();
        SwingSearchWindow.this.backGroundSearch = false;
        SwingSearchWindow.this.resultsList.setStatus(2);
        return null;
      }
    };
    this.searcher.start();
  }

  public int getFirstPageWithResults()
  {
    return this.firstPageWithResults;
  }

  private void searchPageRange(PdfPageData paramPdfPageData, int paramInt1, int paramInt2)
    throws Exception
  {
    int n = 0;
    for (int i1 = paramInt1; (i1 < paramInt2) && (!this.requestInterupt); i1++)
      if (!Thread.interrupted())
      {
        this.progress.setValue(this.progress.getValue() + 1);
        this.decode_pdf.repaint();
        try
        {
          PdfGroupingAlgorithms localPdfGroupingAlgorithms;
          if (i1 == this.commonValues.getCurrentPage())
          {
            localPdfGroupingAlgorithms = this.decode_pdf.getGroupingObject();
          }
          else
          {
            this.decode_pdf.decodePageInBackground(i1);
            localPdfGroupingAlgorithms = this.decode_pdf.getBackgroundGroupingObject();
          }
          localPdfGroupingAlgorithms.generateTeasers();
          localPdfGroupingAlgorithms.setIncludeHTML(true);
          int i = paramPdfPageData.getCropBoxX(i1);
          int j = paramPdfPageData.getCropBoxWidth(i1);
          int k = paramPdfPageData.getCropBoxY(i1);
          int m = paramPdfPageData.getCropBoxHeight(i1);
          DefaultSearchListener localDefaultSearchListener = new DefaultSearchListener();
          SortedMap localSortedMap = localPdfGroupingAlgorithms.findMultipleTermsInRectangleWithMatchingTeasers(i, k, j, m, paramPdfPageData.getRotation(i1), i1, this.searchTerms, this.searchTypeParameters, localDefaultSearchListener);
          if (Thread.interrupted())
            continue;
          if (!localSortedMap.isEmpty())
          {
            if (!this.backGroundSearch)
            {
              this.currentGUI.nextSearch.setEnabled(true);
              this.currentGUI.previousSearch.setEnabled(true);
            }
            this.itemFoundCount += localSortedMap.size();
            Iterator localIterator = localSortedMap.entrySet().iterator();
            while (localIterator.hasNext())
            {
              Object localObject1 = localIterator.next();
              Map.Entry localEntry = (Map.Entry)localObject1;
              Object localObject2 = localEntry.getKey();
              final String str = (String)localEntry.getValue();
              if (!SwingUtilities.isEventDispatchThread())
              {
                localObject3 = new Runnable()
                {
                  public void run()
                  {
                    if (!str.contains("<b>"))
                      SwingSearchWindow.this.listModel.addElement(str);
                    else
                      SwingSearchWindow.this.listModel.addElement("<html>" + str + "</html>");
                  }
                };
                SwingUtilities.invokeLater((Runnable)localObject3);
              }
              else if (!str.contains("<b>"))
              {
                this.listModel.addElement(str);
              }
              else
              {
                this.listModel.addElement("<html>" + str + "</html>");
              }
              Object localObject3 = Integer.valueOf(n);
              n++;
              this.textRectangles.put(localObject3, localObject2);
              this.textPages.put(localObject3, Integer.valueOf(i1));
            }
          }
          if (!this.backGroundSearch)
            if (((!localSortedMap.isEmpty() ? 1 : 0) | (i1 % 16 == 0 ? 1 : 0)) != 0)
            {
              this.searchCount.setText(Messages.getMessage("PdfViewerSearch.ItemsFound") + ' ' + this.itemFoundCount + ' ' + Messages.getMessage("PdfViewerSearch.Scanning") + i1);
              this.searchCount.invalidate();
              this.searchCount.repaint();
            }
        }
        catch (PdfException localPdfException)
        {
          this.backGroundSearch = false;
          this.requestInterupt = false;
        }
        if (this.requestInterupt)
          this.currentGUI.showMessageDialog("Search stopped by user.");
        this.lastPage = i1;
      }
  }

  public void grabFocusInInput()
  {
    this.searchText.grabFocus();
  }

  public boolean isSearchVisible()
  {
    return isVisible();
  }

  public void setStyle(int paramInt)
  {
    this.style = paramInt;
  }

  public int getStyle()
  {
    return this.style;
  }

  public void setSearchText(String paramString)
  {
    this.deleteOnClick = false;
    this.searchText.setText(paramString);
  }

  public Map getTextRectangles()
  {
    return this.textRectangles;
  }

  public SearchList getResults()
  {
    return this.resultsList;
  }

  public SearchList getResults(int paramInt)
  {
    if ((this.usingMenuBarSearch) && (paramInt != this.lastPage) && (this.style == SEARCH_MENU_BAR))
    {
      this.listModel = new DefaultListModel();
      this.textPages.clear();
      this.textRectangles.clear();
      this.resultsList = new SearchList(this.listModel, this.textPages, this.textRectangles);
      this.resultsList.setStatus(4);
      try
      {
        searchPageRange(this.decode_pdf.getPdfPageData(), paramInt, paramInt + 1);
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
      }
      this.lastPage = paramInt;
    }
    String str = "";
    for (int i = 0; i != this.searchTerms.length; i++)
      if (i < this.searchTerms.length - 1)
        str = str + this.searchTerms[i] + ", ";
      else
        str = str + this.searchTerms[i];
    this.resultsList.setSearchTerm(str);
    return this.resultsList;
  }

  public void resetSearchWindow()
  {
    if (this.isSetup)
    {
      this.searchText.setText(this.defaultMessage);
      this.deleteOnClick = true;
      if (this.hasSearched)
      {
        this.currentGUI.nextSearch.setEnabled(false);
        this.currentGUI.previousSearch.setEnabled(false);
        this.hasSearched = false;
      }
      this.currentGUI.getPdfDecoder().requestFocusInWindow();
    }
  }

  class SearchTextFocusListener
    implements FocusListener
  {
    SearchTextFocusListener()
    {
    }

    public void focusGained(FocusEvent paramFocusEvent)
    {
      if (SwingSearchWindow.this.decode_pdf != null)
      {
        Object localObject = SwingSearchWindow.this.decode_pdf.getExternalHandler(30);
        if ((localObject != null) && ((localObject instanceof JPedalActionHandler)))
          ((JPedalActionHandler)localObject).actionPerformed(SwingSearchWindow.this.currentGUI, null);
      }
    }

    public void focusLost(FocusEvent paramFocusEvent)
    {
    }
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.gui.swing.SwingSearchWindow
 * JD-Core Version:    0.6.2
 */