package org.jpedal.examples.viewer.gui.popups;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Ellipse2D.Float;
import java.awt.image.BufferedImage;
import java.io.PrintStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import javax.print.DocFlavor.SERVICE_FORMATTED;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.SetOfIntegerSyntax;
import javax.print.attribute.standard.PageRanges;
import javax.print.attribute.standard.PrinterResolution;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jpedal.PdfDecoder;
import org.jpedal.examples.viewer.Viewer;
import org.jpedal.examples.viewer.paper.MarginPaper;
import org.jpedal.examples.viewer.paper.PaperSizes;
import org.jpedal.exception.PdfException;
import org.jpedal.io.ColorSpaceConvertor;
import org.jpedal.objects.PdfPageData;
import org.jpedal.objects.PrinterOptions;
import org.jpedal.utils.Messages;

public class PrintPanel extends JPanel
{
  private boolean debugPrinterChange = false;
  int pageCount;
  int currentPage;
  int currentPreviewedPage = 1;
  int pagePrintCount = 1;
  private boolean okClicked = false;
  private String[] printersList;
  private PaperSizes paperDefinitions;
  private int defaultResolution;
  private boolean isFirstTime = true;
  private static final double mmPerSubInch = 0.3527777777777778D;
  private PdfDecoder pdf;
  private JCheckBox autoRotateCenter;
  private ButtonGroup buttonGroup1;
  private JButton cancelButton;
  private JLabel jLabel1;
  private JLabel jLabel2;
  private JLabel jLabel4;
  private JLabel jLabel7;
  private JLabel jLabel8;
  private JLabel jLabel9;
  private JLabel jLabel10;
  private JLabel jLabel11;
  private JLabel jLabel12;
  private JLabel jLabel13;
  private JLabel jLabel14;
  private JLabel jLabel15;
  private JSpinner left;
  private JSpinner right;
  private JSpinner top;
  private JSpinner bottom;
  private JComboBox resolution;
  private JButton okButton;
  private JPanel pageHandlingPanel;
  private JPanel previewPanel;
  private JComponent previewComponent;
  private CustomSlider previewSlider;
  private JComboBox pageSize;
  private JTextField pagesFrom;
  private JTextField pagesTo;
  private JCheckBox paperSourceByPDF;
  private JCheckBox grayscale;
  private JSpinner printHandlingCopies;
  private JComboBox printHandlingScaling;
  private JRadioButton printRangeAll;
  private JRadioButton printRangeCurrentPage;
  private JRadioButton printRangeCurrentView;
  private JRadioButton printRangeFrom;
  private JPanel printRangePanel;
  private JCheckBox printRangeReversePages;
  private JComboBox printRangeSubset;
  private JComboBox printerName;
  private JPanel printerPanel;
  private JTabbedPane tabbedPane;

  public PrintPanel(String[] paramArrayOfString, String paramString, PaperSizes paramPaperSizes, int paramInt1, int paramInt2, PdfDecoder paramPdfDecoder)
  {
    this.pdf = paramPdfDecoder;
    this.pageCount = this.pdf.getPageCount();
    this.currentPage = paramInt2;
    this.defaultResolution = paramInt1;
    this.paperDefinitions = paramPaperSizes;
    resetDefaults(paramArrayOfString, paramString, this.pageCount, this.currentPage);
    this.isFirstTime = false;
  }

  public void resetDefaults(String[] paramArrayOfString, String paramString, int paramInt1, int paramInt2)
  {
    this.printersList = paramArrayOfString;
    this.pageCount = paramInt1;
    this.currentPage = paramInt2;
    initComponents();
    if (this.pageSize.getItemCount() == 0)
      return;
    int i = this.paperDefinitions.getDefaultPageIndex();
    if (i < this.pageSize.getModel().getSize())
      this.pageSize.setSelectedIndex(i);
    String str1 = System.getProperty("org.jpedal.defaultPrinter");
    if (str1 != null)
      for (String str2 : paramArrayOfString)
        if (str1.equals(str2))
          this.printerName.setSelectedItem(str2);
    else
      this.printerName.setSelectedItem(paramString);
  }

  private void initComponents()
  {
    if (this.isFirstTime)
    {
      this.buttonGroup1 = new ButtonGroup();
      this.printerPanel = new JPanel();
      this.jLabel1 = new JLabel();
      this.jLabel2 = new JLabel();
      this.tabbedPane = new JTabbedPane();
      this.printerName = new JComboBox();
      this.pageSize = new JComboBox();
      this.jLabel10 = new JLabel();
      this.jLabel11 = new JLabel();
      this.jLabel12 = new JLabel();
      this.jLabel13 = new JLabel();
      this.jLabel14 = new JLabel();
      this.jLabel15 = new JLabel();
      this.left = new JSpinner();
      this.right = new JSpinner();
      this.top = new JSpinner();
      this.bottom = new JSpinner();
      this.resolution = new JComboBox();
      this.printRangePanel = new JPanel();
      this.pagesFrom = new JTextField();
      this.printRangeCurrentPage = new JRadioButton();
      this.printRangeAll = new JRadioButton();
      this.printRangeCurrentView = new JRadioButton();
      this.printRangeFrom = new JRadioButton();
      this.jLabel4 = new JLabel();
      this.printRangeSubset = new JComboBox();
      this.printRangeReversePages = new JCheckBox();
      this.jLabel7 = new JLabel();
      this.pagesTo = new JTextField();
      this.pageHandlingPanel = new JPanel();
      this.previewPanel = new JPanel();
      this.previewComponent = new JComponent()
      {
        public void paintComponent(Graphics paramAnonymousGraphics)
        {
          PrintPanel.this.printPreview((Graphics2D)paramAnonymousGraphics);
        }
      };
      this.previewSlider = new CustomSlider();
      this.jLabel8 = new JLabel();
      this.printHandlingCopies = new JSpinner();
      this.jLabel9 = new JLabel();
      this.printHandlingScaling = new JComboBox();
      this.autoRotateCenter = new JCheckBox();
      this.paperSourceByPDF = new JCheckBox();
      this.grayscale = new JCheckBox();
      this.okButton = new JButton();
      this.cancelButton = new JButton();
    }
    else
    {
      removeAll();
    }
    setLayout(null);
    add(this.tabbedPane);
    this.tabbedPane.setBounds(10, 7, 400, 330);
    this.printerPanel.setLayout(new GridBagLayout());
    GridBagConstraints localGridBagConstraints = new GridBagConstraints();
    this.jLabel1.setText(Messages.getMessage("PdfViewerPrint.Name"));
    localGridBagConstraints.gridx = 0;
    localGridBagConstraints.gridy = 0;
    localGridBagConstraints.gridwidth = 1;
    localGridBagConstraints.fill = 2;
    localGridBagConstraints.insets = new Insets(5, 5, 5, 5);
    this.printerPanel.add(this.jLabel1, localGridBagConstraints);
    this.jLabel2.setText(Messages.getMessage("PdfViewerPrint.PageSize"));
    localGridBagConstraints.gridx = 0;
    localGridBagConstraints.gridy = 1;
    localGridBagConstraints.gridwidth = 1;
    this.printerPanel.add(this.jLabel2, localGridBagConstraints);
    this.printerName.setModel(new DefaultComboBoxModel(this.printersList));
    localGridBagConstraints.gridx = 1;
    localGridBagConstraints.gridy = 0;
    localGridBagConstraints.gridwidth = 5;
    this.printerPanel.add(this.printerName, localGridBagConstraints);
    this.printerName.addItemListener(new ItemListener()
    {
      public void itemStateChanged(ItemEvent paramAnonymousItemEvent)
      {
        if (PrintPanel.this.debugPrinterChange)
          System.out.println("itemStateChanged");
        PrintPanel.this.previewComponent.repaint();
        if (PrintPanel.this.debugPrinterChange)
          System.out.println("repainted preview component");
        PrintPanel.this.okButton.setEnabled(false);
        PrintPanel.this.pageSize.setEnabled(false);
        PrintPanel.this.pageSize.setModel(new DefaultComboBoxModel(new String[] { "Loading..." }));
        PrintPanel.this.resolution.setEnabled(false);
        PrintPanel.this.resolution.setModel(new DefaultComboBoxModel(new String[] { "Loading..." }));
        if (PrintPanel.this.debugPrinterChange)
          System.out.println("GUI options disabled");
        Thread local1 = new Thread()
        {
          public void run()
          {
            if (PrintPanel.this.debugPrinterChange)
              System.out.println("Thread invoked.");
            PrintService[] arrayOfPrintService1 = PrintServiceLookup.lookupPrintServices(null, null);
            if (PrintPanel.this.debugPrinterChange)
              System.out.println("Found print services.");
            Object localObject = null;
            for (PrintService localPrintService : arrayOfPrintService1)
            {
              if (PrintPanel.this.debugPrinterChange)
                System.out.println("checking " + localPrintService.getName());
              if (localPrintService.getName().equals(PrintPanel.this.printerName.getSelectedItem()))
              {
                localObject = localPrintService;
                if (PrintPanel.this.debugPrinterChange)
                  System.out.println("Match!");
              }
            }
            if (localObject != null)
            {
              if (PrintPanel.this.debugPrinterChange)
                System.out.println("Getting available resolutions...");
              PrintPanel.this.resolution.setModel(new DefaultComboBoxModel(PrintPanel.getAvailableResolutions(localObject)));
              if (PrintPanel.this.debugPrinterChange)
                System.out.println("Getting default resolution...");
              int i = PrintPanel.this.getDefaultResolutionIndex();
              if (PrintPanel.this.resolution.getModel().getSize() > i)
                PrintPanel.this.resolution.setSelectedIndex(i);
              PrintPanel.this.resolution.setEnabled(true);
              PrintPanel.this.paperDefinitions.setPrintService(localObject);
              if (PrintPanel.this.debugPrinterChange)
                System.out.println("Getting available paper sizes...");
              PrintPanel.this.pageSize.setModel(new DefaultComboBoxModel(PrintPanel.this.getAvailablePaperSizes()));
              if (PrintPanel.this.debugPrinterChange)
                System.out.println("Getting default pagesize...");
              ??? = PrintPanel.this.paperDefinitions.getDefaultPageIndex();
              if (PrintPanel.this.pageSize.getModel().getSize() > ???)
                PrintPanel.this.pageSize.setSelectedIndex(???);
              PrintPanel.this.pageSize.setEnabled(true);
              PrintPanel.this.okButton.setEnabled(true);
              if (PrintPanel.this.debugPrinterChange)
                System.out.println("Reenabled GUI");
            }
            if (PrintPanel.this.debugPrinterChange)
              System.out.println("Updating margins");
            PrintPanel.this.updateMargins();
          }
        };
        local1.setDaemon(true);
        if (PrintPanel.this.debugPrinterChange)
          System.out.println("Invoking update thread...");
        SwingUtilities.invokeLater(local1);
      }
    });
    Thread local3 = new Thread()
    {
      public void run()
      {
        PrintService[] arrayOfPrintService1 = PrintServiceLookup.lookupPrintServices(null, null);
        Object localObject = null;
        for (PrintService localPrintService : arrayOfPrintService1)
          if (localPrintService.getName().equals(PrintPanel.this.printerName.getSelectedItem()))
            localObject = localPrintService;
        if (localObject != null)
        {
          PrintPanel.this.paperDefinitions.setPrintService(localObject);
          PrintPanel.this.resolution.setModel(new DefaultComboBoxModel(PrintPanel.getAvailableResolutions(localObject)));
          int i = PrintPanel.this.getDefaultResolutionIndex();
          if (PrintPanel.this.resolution.getModel().getSize() > i)
            PrintPanel.this.resolution.setSelectedIndex(i);
          PrintPanel.this.resolution.setEnabled(true);
          PrintPanel.this.pageSize.setModel(new DefaultComboBoxModel(PrintPanel.this.getAvailablePaperSizes()));
          ??? = PrintPanel.this.paperDefinitions.getDefaultPageIndex();
          if (??? < PrintPanel.this.pageSize.getModel().getSize())
            PrintPanel.this.pageSize.setSelectedIndex(???);
          PrintPanel.this.pageSize.setEnabled(true);
          PrintPanel.this.okButton.setEnabled(true);
        }
        PrintPanel.this.updateMargins();
      }
    };
    local3.setDaemon(true);
    this.pageSize.setModel(new DefaultComboBoxModel(new String[] { "Loading..." }));
    this.pageSize.setEnabled(false);
    this.resolution.setModel(new DefaultComboBoxModel(new String[] { "Loading..." }));
    this.resolution.setEnabled(false);
    this.okButton.setEnabled(false);
    local3.start();
    this.pageSize.addItemListener(new ItemListener()
    {
      public void itemStateChanged(ItemEvent paramAnonymousItemEvent)
      {
        PrintPanel.this.updatePreview();
        PrintPanel.this.updateMargins();
      }
    });
    localGridBagConstraints.gridx = 1;
    localGridBagConstraints.gridy = 1;
    localGridBagConstraints.gridwidth = 5;
    this.printerPanel.add(this.pageSize, localGridBagConstraints);
    this.jLabel10.setText(Messages.getMessage("PdfViewerPrintMargins.margin"));
    localGridBagConstraints.gridx = 0;
    localGridBagConstraints.gridy = 2;
    localGridBagConstraints.gridwidth = 1;
    this.printerPanel.add(this.jLabel10, localGridBagConstraints);
    this.jLabel11.setText(Messages.getMessage("PdfViewerPrintMargins.left"));
    localGridBagConstraints.gridx = 1;
    localGridBagConstraints.gridy = 2;
    localGridBagConstraints.gridwidth = 1;
    this.printerPanel.add(this.jLabel11, localGridBagConstraints);
    this.left.setModel(new CustomSpinnerModel(null));
    localGridBagConstraints.gridx = 2;
    localGridBagConstraints.gridy = 2;
    localGridBagConstraints.gridwidth = 1;
    this.left.setMinimumSize(new Dimension(65, 22));
    this.left.setPreferredSize(new Dimension(65, 22));
    this.printerPanel.add(this.left, localGridBagConstraints);
    this.jLabel12.setText(Messages.getMessage("PdfViewerPrintMargins.right"));
    localGridBagConstraints.gridx = 3;
    localGridBagConstraints.gridy = 2;
    localGridBagConstraints.gridwidth = 1;
    this.printerPanel.add(this.jLabel12, localGridBagConstraints);
    this.right.setModel(new CustomSpinnerModel(null));
    localGridBagConstraints.gridx = 4;
    localGridBagConstraints.gridy = 2;
    localGridBagConstraints.gridwidth = 1;
    this.right.setMinimumSize(new Dimension(65, 22));
    this.right.setPreferredSize(new Dimension(65, 22));
    this.printerPanel.add(this.right, localGridBagConstraints);
    this.jLabel13.setText(Messages.getMessage("PdfViewerPrintMargins.top"));
    localGridBagConstraints.gridx = 1;
    localGridBagConstraints.gridy = 3;
    localGridBagConstraints.gridwidth = 1;
    this.printerPanel.add(this.jLabel13, localGridBagConstraints);
    this.top.setModel(new CustomSpinnerModel(null));
    localGridBagConstraints.gridx = 2;
    localGridBagConstraints.gridy = 3;
    localGridBagConstraints.gridwidth = 1;
    this.top.setMinimumSize(new Dimension(65, 22));
    this.top.setPreferredSize(new Dimension(65, 22));
    this.printerPanel.add(this.top, localGridBagConstraints);
    this.jLabel14.setText(Messages.getMessage("PdfViewerPrintMargins.bottom"));
    localGridBagConstraints.gridx = 3;
    localGridBagConstraints.gridy = 3;
    localGridBagConstraints.gridwidth = 1;
    this.printerPanel.add(this.jLabel14, localGridBagConstraints);
    this.bottom.setModel(new CustomSpinnerModel(null));
    localGridBagConstraints.gridx = 4;
    localGridBagConstraints.gridy = 3;
    localGridBagConstraints.gridwidth = 1;
    this.bottom.setMinimumSize(new Dimension(65, 22));
    this.bottom.setPreferredSize(new Dimension(65, 22));
    this.printerPanel.add(this.bottom, localGridBagConstraints);
    ChangeListener local5 = new ChangeListener()
    {
      static final double mmToSubInch = 2.834645669291339D;

      public void stateChanged(ChangeEvent paramAnonymousChangeEvent)
      {
        MarginPaper localMarginPaper = PrintPanel.this.getSelectedPaper();
        if (localMarginPaper == null)
          return;
        double d1 = ((Double)PrintPanel.this.left.getValue()).doubleValue() * 2.834645669291339D;
        double d2 = ((Double)PrintPanel.this.right.getValue()).doubleValue() * 2.834645669291339D;
        double d3 = ((Double)PrintPanel.this.top.getValue()).doubleValue() * 2.834645669291339D;
        double d4 = ((Double)PrintPanel.this.bottom.getValue()).doubleValue() * 2.834645669291339D;
        localMarginPaper.setImageableArea(d1, d3, localMarginPaper.getWidth() - d1 - d2, localMarginPaper.getHeight() - d3 - d4);
        PrintPanel.this.updatePreview();
      }
    };
    this.left.addChangeListener(local5);
    this.right.addChangeListener(local5);
    this.top.addChangeListener(local5);
    this.bottom.addChangeListener(local5);
    this.jLabel15.setText(Messages.getMessage("PdfViewerPrintResolution.text"));
    localGridBagConstraints.gridx = 0;
    localGridBagConstraints.gridy = 4;
    localGridBagConstraints.gridwidth = 1;
    this.printerPanel.add(this.jLabel15, localGridBagConstraints);
    localGridBagConstraints.gridx = 1;
    localGridBagConstraints.gridy = 4;
    localGridBagConstraints.gridwidth = 5;
    this.printerPanel.add(this.resolution, localGridBagConstraints);
    this.tabbedPane.addTab(Messages.getMessage("PdfViewerPrintTab.printer"), this.printerPanel);
    this.printRangePanel.setLayout(new GridBagLayout());
    this.pagesFrom.setText("1");
    this.pagesFrom.addMouseListener(new MouseAdapter()
    {
      public void mousePressed(MouseEvent paramAnonymousMouseEvent)
      {
        PrintPanel.this.pagesBoxPressed();
      }
    });
    localGridBagConstraints.gridx = 1;
    localGridBagConstraints.gridy = 3;
    localGridBagConstraints.gridwidth = 1;
    this.pagesFrom.setMinimumSize(new Dimension(65, 22));
    this.pagesFrom.setPreferredSize(new Dimension(65, 22));
    localGridBagConstraints.insets = new Insets(0, 0, 0, 0);
    this.printRangePanel.add(this.pagesFrom, localGridBagConstraints);
    localGridBagConstraints.insets = new Insets(5, 5, 5, 5);
    this.buttonGroup1.add(this.printRangeCurrentPage);
    this.printRangeCurrentPage.setText(Messages.getMessage("PdfViewerRadioButton.CurrentPage"));
    this.printRangeCurrentPage.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    this.printRangeCurrentPage.setMargin(new Insets(0, 0, 0, 0));
    this.printRangeCurrentPage.setOpaque(false);
    this.printRangeCurrentPage.addItemListener(new ItemListener()
    {
      public void itemStateChanged(ItemEvent paramAnonymousItemEvent)
      {
        PrintPanel.this.currentPageStateChanged();
      }
    });
    localGridBagConstraints.gridx = 0;
    localGridBagConstraints.gridy = 2;
    localGridBagConstraints.gridwidth = 1;
    this.printRangePanel.add(this.printRangeCurrentPage, localGridBagConstraints);
    this.buttonGroup1.add(this.printRangeAll);
    this.printRangeAll.setSelected(true);
    this.printRangeAll.setText(Messages.getMessage("PdfViewerRadioButton.All"));
    this.printRangeAll.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    this.printRangeAll.setMargin(new Insets(0, 0, 0, 0));
    this.printRangeAll.setOpaque(false);
    if (this.isFirstTime)
      this.printRangeAll.addItemListener(new ItemListener()
      {
        public void itemStateChanged(ItemEvent paramAnonymousItemEvent)
        {
          PrintPanel.this.allStateChanged();
        }
      });
    localGridBagConstraints.gridx = 0;
    localGridBagConstraints.gridy = 0;
    localGridBagConstraints.gridwidth = 1;
    this.printRangePanel.add(this.printRangeAll, localGridBagConstraints);
    this.buttonGroup1.add(this.printRangeCurrentView);
    this.printRangeCurrentView.setText(Messages.getMessage("PdfViewerPrint.CurrentView"));
    this.printRangeCurrentView.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    this.printRangeCurrentView.setEnabled(true);
    this.printRangeCurrentView.setMargin(new Insets(0, 0, 0, 0));
    this.printRangeCurrentView.setOpaque(false);
    localGridBagConstraints.gridx = 0;
    localGridBagConstraints.gridy = 1;
    localGridBagConstraints.gridwidth = 1;
    this.printRangePanel.add(this.printRangeCurrentView, localGridBagConstraints);
    if (this.pdf.getDisplayView() != 1)
    {
      this.printRangeCurrentView.setEnabled(false);
      this.printRangeCurrentView.setToolTipText(Messages.getMessage("PageLayoutMessage.SinglePageOnly"));
    }
    else
    {
      this.printRangeCurrentView.setEnabled(true);
      this.printRangeCurrentView.setToolTipText(null);
    }
    if (this.isFirstTime)
      this.printRangeCurrentView.addItemListener(new ItemListener()
      {
        public void itemStateChanged(ItemEvent paramAnonymousItemEvent)
        {
          PrintPanel.this.printRangeCurrentView();
        }
      });
    this.buttonGroup1.add(this.printRangeFrom);
    this.printRangeFrom.setText(Messages.getMessage("PdfViewerPrint.PagesFrom"));
    this.printRangeFrom.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    this.printRangeFrom.setMargin(new Insets(0, 0, 0, 0));
    this.printRangeFrom.setOpaque(false);
    if (this.isFirstTime)
      this.printRangeFrom.addItemListener(new ItemListener()
      {
        public void itemStateChanged(ItemEvent paramAnonymousItemEvent)
        {
          PrintPanel.this.pagesFromStateChanged();
        }
      });
    localGridBagConstraints.gridx = 0;
    localGridBagConstraints.gridy = 3;
    localGridBagConstraints.gridwidth = 1;
    this.printRangePanel.add(this.printRangeFrom, localGridBagConstraints);
    this.jLabel4.setText(Messages.getMessage("PdfViewerPrint.Subset"));
    localGridBagConstraints.gridx = 0;
    localGridBagConstraints.gridy = 4;
    localGridBagConstraints.gridwidth = 1;
    this.printRangePanel.add(this.jLabel4, localGridBagConstraints);
    this.printRangeSubset.setModel(new DefaultComboBoxModel(new String[] { Messages.getMessage("PdfViewerPrint.AllPagesInRange"), Messages.getMessage("PdfViewerPrint.OddPagesOnly"), Messages.getMessage("PdfViewerPrint.EvenPagesOnly") }));
    localGridBagConstraints.gridx = 1;
    localGridBagConstraints.gridy = 4;
    localGridBagConstraints.gridwidth = 4;
    localGridBagConstraints.insets = new Insets(5, 0, 5, 0);
    this.printRangePanel.add(this.printRangeSubset, localGridBagConstraints);
    localGridBagConstraints.insets = new Insets(5, 5, 5, 5);
    this.printRangeSubset.addItemListener(new ItemListener()
    {
      public void itemStateChanged(ItemEvent paramAnonymousItemEvent)
      {
        PrintPanel.this.updatePreview();
      }
    });
    this.printRangeReversePages.setText(Messages.getMessage("PdfViewerPrint.ReversePages"));
    this.printRangeReversePages.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    this.printRangeReversePages.setMargin(new Insets(0, 0, 0, 0));
    localGridBagConstraints.gridx = 0;
    localGridBagConstraints.gridy = 5;
    localGridBagConstraints.gridwidth = 3;
    this.printRangePanel.add(this.printRangeReversePages, localGridBagConstraints);
    this.printRangeReversePages.addItemListener(new ItemListener()
    {
      public void itemStateChanged(ItemEvent paramAnonymousItemEvent)
      {
        PrintPanel.this.updatePreview();
      }
    });
    this.jLabel7.setText(Messages.getMessage("PdfViewerPrint.PagesTo"));
    localGridBagConstraints.gridx = 2;
    localGridBagConstraints.gridy = 3;
    localGridBagConstraints.gridwidth = 1;
    this.printRangePanel.add(this.jLabel7, localGridBagConstraints);
    this.pagesTo.setText(String.valueOf(this.pageCount));
    if (this.isFirstTime)
      this.pagesTo.addMouseListener(new MouseAdapter()
      {
        public void mousePressed(MouseEvent paramAnonymousMouseEvent)
        {
          PrintPanel.this.pagesBoxPressed();
        }
      });
    localGridBagConstraints.gridx = 3;
    localGridBagConstraints.gridy = 3;
    localGridBagConstraints.gridwidth = 1;
    this.pagesTo.setMinimumSize(new Dimension(65, 22));
    this.pagesTo.setPreferredSize(new Dimension(65, 22));
    localGridBagConstraints.insets = new Insets(0, 0, 0, 0);
    this.printRangePanel.add(this.pagesTo, localGridBagConstraints);
    localGridBagConstraints.insets = new Insets(5, 5, 5, 5);
    this.tabbedPane.addTab(Messages.getMessage("PdfViewerPrintTab.range"), this.printRangePanel);
    this.pageHandlingPanel.setLayout(new GridBagLayout());
    this.jLabel8.setText(Messages.getMessage("PdfViewerPrint.Copies"));
    localGridBagConstraints.gridx = 0;
    localGridBagConstraints.gridy = 0;
    localGridBagConstraints.gridwidth = 1;
    this.pageHandlingPanel.add(this.jLabel8, localGridBagConstraints);
    this.printHandlingCopies.setModel(new SpinnerNumberModel(1, 1, 1000, 1));
    localGridBagConstraints.gridx = 1;
    localGridBagConstraints.gridy = 0;
    localGridBagConstraints.gridwidth = 1;
    localGridBagConstraints.fill = 3;
    localGridBagConstraints.insets = new Insets(0, 0, 0, 0);
    localGridBagConstraints.anchor = 17;
    this.printHandlingCopies.setMinimumSize(new Dimension(65, 22));
    this.printHandlingCopies.setPreferredSize(new Dimension(65, 22));
    this.pageHandlingPanel.add(this.printHandlingCopies, localGridBagConstraints);
    localGridBagConstraints.fill = 2;
    localGridBagConstraints.anchor = 10;
    localGridBagConstraints.insets = new Insets(5, 5, 5, 5);
    this.jLabel9.setText(Messages.getMessage("PdfViewerPrint.PageScaling"));
    localGridBagConstraints.gridx = 0;
    localGridBagConstraints.gridy = 1;
    localGridBagConstraints.gridwidth = 1;
    this.pageHandlingPanel.add(this.jLabel9, localGridBagConstraints);
    this.printHandlingScaling.setModel(new DefaultComboBoxModel(PrinterOptions.PRINT_SCALING_OPTIONS));
    this.printHandlingScaling.setSelectedIndex(PrinterOptions.LAST_SCALING_CHOICE);
    localGridBagConstraints.gridx = 1;
    localGridBagConstraints.gridy = 1;
    localGridBagConstraints.gridwidth = 1;
    localGridBagConstraints.insets = new Insets(5, 0, 5, 0);
    this.pageHandlingPanel.add(this.printHandlingScaling, localGridBagConstraints);
    localGridBagConstraints.insets = new Insets(5, 5, 5, 5);
    this.printHandlingScaling.addItemListener(new ItemListener()
    {
      public void itemStateChanged(ItemEvent paramAnonymousItemEvent)
      {
        PrintPanel.this.updatePreview();
      }
    });
    this.autoRotateCenter.setSelected(true);
    this.autoRotateCenter.setText(Messages.getMessage("PdfViewerPrint.AutoRotateAndCenter"));
    this.autoRotateCenter.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    this.autoRotateCenter.setMargin(new Insets(0, 0, 0, 0));
    localGridBagConstraints.gridx = 0;
    localGridBagConstraints.gridy = 2;
    localGridBagConstraints.gridwidth = 1;
    this.pageHandlingPanel.add(this.autoRotateCenter, localGridBagConstraints);
    this.autoRotateCenter.addItemListener(new ItemListener()
    {
      public void itemStateChanged(ItemEvent paramAnonymousItemEvent)
      {
        PrintPanel.this.updatePreview();
      }
    });
    this.paperSourceByPDF.setText(Messages.getMessage("PdfViewerPrint.ChoosePaperByPdfSize"));
    this.paperSourceByPDF.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    this.paperSourceByPDF.setMargin(new Insets(0, 0, 0, 0));
    localGridBagConstraints.gridx = 0;
    localGridBagConstraints.gridy = 3;
    localGridBagConstraints.gridwidth = 2;
    this.pageHandlingPanel.add(this.paperSourceByPDF, localGridBagConstraints);
    this.paperSourceByPDF.addItemListener(new ItemListener()
    {
      public void itemStateChanged(ItemEvent paramAnonymousItemEvent)
      {
        PrintPanel.this.updatePreview();
      }
    });
    this.grayscale.setText(Messages.getMessage("PdfViewerPrint.Grayscale"));
    this.grayscale.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    localGridBagConstraints.gridx = 0;
    localGridBagConstraints.gridy = 4;
    localGridBagConstraints.gridwidth = 2;
    this.pageHandlingPanel.add(this.grayscale, localGridBagConstraints);
    this.grayscale.setMargin(new Insets(0, 0, 0, 0));
    this.grayscale.addItemListener(new ItemListener()
    {
      public void itemStateChanged(ItemEvent paramAnonymousItemEvent)
      {
        PrintPanel.this.updatePreview();
      }
    });
    this.tabbedPane.addTab(Messages.getMessage("PdfViewerPrintTab.handling"), this.pageHandlingPanel);
    this.previewPanel.setLayout(null);
    this.previewPanel.setBorder(BorderFactory.createTitledBorder(null, Messages.getMessage("PdfViewerLabel.PrintPreview"), 0, 0, new Font("Tahoma", 0, 11), Color.BLACK));
    this.previewPanel.add(this.previewComponent);
    this.previewComponent.setBounds(5, 5, 220, 275);
    this.previewPanel.add(this.previewSlider);
    this.previewSlider.setBounds(5, 280, 220, 40);
    this.previewSlider.setValue(1);
    updatePreview();
    add(this.previewPanel);
    this.previewPanel.setBounds(420, 7, 230, 330);
    this.okButton.setText(Messages.getMessage("PdfMessage.Ok"));
    if (this.isFirstTime)
      this.okButton.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent paramAnonymousActionEvent)
        {
          PrintPanel.this.okEvent();
        }
      });
    add(this.okButton);
    this.okButton.setBounds(445, 348, 90, 23);
    this.cancelButton.setText(Messages.getMessage("PdfMessage.Cancel"));
    if (this.isFirstTime)
      this.cancelButton.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent paramAnonymousActionEvent)
        {
          PrintPanel.this.cancelEvent();
        }
      });
    add(this.cancelButton);
    this.cancelButton.setBounds(560, 348, 90, 23);
  }

  private static String[] getAvailableResolutions(PrintService paramPrintService)
  {
    PrinterResolution[] arrayOfPrinterResolution = (PrinterResolution[])paramPrintService.getSupportedAttributeValues(PrinterResolution.class, DocFlavor.SERVICE_FORMATTED.PAGEABLE, null);
    if (arrayOfPrinterResolution == null)
      return new String[] { "Default" };
    String[] arrayOfString = new String[arrayOfPrinterResolution.length];
    for (int i = 0; i < arrayOfPrinterResolution.length; i++)
    {
      PrinterResolution localPrinterResolution = arrayOfPrinterResolution[i];
      arrayOfString[i] = new StringBuilder().append(localPrinterResolution.getCrossFeedResolution(100)).append("x").append(localPrinterResolution.getFeedResolution(100)).append(" dpi").toString();
    }
    return arrayOfString;
  }

  public PrinterResolution getResolution()
  {
    PrintService[] arrayOfPrintService = PrintServiceLookup.lookupPrintServices(null, null);
    Object localObject1 = null;
    for (Object localObject3 : arrayOfPrintService)
      if (localObject3.getName().equals(this.printerName.getSelectedItem()))
        localObject1 = localObject3;
    ??? = (PrinterResolution[])localObject1.getSupportedAttributeValues(PrinterResolution.class, DocFlavor.SERVICE_FORMATTED.PAGEABLE, null);
    if (??? == null)
      return null;
    return ???[this.resolution.getSelectedIndex()];
  }

  private int getDefaultResolutionIndex()
  {
    PrintService[] arrayOfPrintService = PrintServiceLookup.lookupPrintServices(null, null);
    Object localObject1 = null;
    for (Object localObject3 : arrayOfPrintService)
      if (localObject3.getName().equals(this.printerName.getSelectedItem()))
        localObject1 = localObject3;
    ??? = (PrinterResolution[])localObject1.getSupportedAttributeValues(PrinterResolution.class, DocFlavor.SERVICE_FORMATTED.PAGEABLE, null);
    if (??? == null)
      return 0;
    if (this.defaultResolution == -1)
      this.defaultResolution = 600;
    ??? = 2147483647;
    ??? = 0;
    for (int k = 0; k < ???.length; k++)
    {
      Object localObject4 = ???[k];
      int m = localObject4.getCrossFeedResolution(100) - this.defaultResolution;
      if (m < 0)
        m = -m;
      int n = localObject4.getFeedResolution(100) - this.defaultResolution;
      if (n < 0)
        n = -n;
      if (m + n < ???)
      {
        ??? = m + n;
        ??? = k;
      }
    }
    return ???;
  }

  private void updateMargins()
  {
    MarginPaper localMarginPaper = getSelectedPaper();
    if (localMarginPaper == null)
      return;
    ((CustomSpinnerModel)this.left.getModel()).setMinValue(localMarginPaper.getMinX() * 0.3527777777777778D);
    ((CustomSpinnerModel)this.top.getModel()).setMinValue(localMarginPaper.getMinY() * 0.3527777777777778D);
    ((CustomSpinnerModel)this.right.getModel()).setMinValue((localMarginPaper.getWidth() - localMarginPaper.getMaxRX()) * 0.3527777777777778D);
    ((CustomSpinnerModel)this.bottom.getModel()).setMinValue((localMarginPaper.getHeight() - localMarginPaper.getMaxBY()) * 0.3527777777777778D);
    Double localDouble = Double.valueOf(0.0D);
    this.left.setValue(localDouble);
    this.top.setValue(localDouble);
    this.right.setValue(localDouble);
    this.bottom.setValue(localDouble);
  }

  private void printPreview(Graphics2D paramGraphics2D)
  {
    int i = this.previewComponent.getWidth();
    int j = this.previewComponent.getHeight();
    if (this.printRangeCurrentView.isSelected())
    {
      localMarginPaper7 = (int)(this.pdf.getVisibleRect().getWidth() / this.pdf.getScaling());
      localMarginPaper8 = (int)(this.pdf.getVisibleRect().getHeight() / this.pdf.getScaling());
    }
    else
    {
      localMarginPaper7 = this.pdf.getPdfPageData().getCropBoxWidth(this.currentPreviewedPage);
      localMarginPaper8 = this.pdf.getPdfPageData().getCropBoxHeight(this.currentPreviewedPage);
    }
    MarginPaper localMarginPaper9;
    if (this.paperSourceByPDF.isSelected())
    {
      localMarginPaper1 = localMarginPaper7;
      localMarginPaper2 = localMarginPaper8;
      localMarginPaper3 = localMarginPaper1;
      localMarginPaper4 = localMarginPaper2;
      localMarginPaper5 = 0;
      localMarginPaper6 = 0;
    }
    else
    {
      localMarginPaper9 = this.paperDefinitions.getSelectedPaper(this.pageSize.getSelectedItem());
      if (localMarginPaper9 == null)
      {
        paramGraphics2D.drawString(Messages.getMessage("PdfPrintPreview.Loading"), i / 2 - 25, j / 2 - 5);
        return;
      }
      localMarginPaper1 = (int)localMarginPaper9.getWidth();
      localMarginPaper2 = (int)localMarginPaper9.getHeight();
      localMarginPaper5 = (int)localMarginPaper9.getImageableX();
      localMarginPaper6 = (int)localMarginPaper9.getImageableY();
      localMarginPaper3 = (int)localMarginPaper9.getImageableWidth();
      localMarginPaper4 = (int)localMarginPaper9.getImageableHeight();
    }
    if ((this.autoRotateCenter.isSelected()) && (((localMarginPaper7 > localMarginPaper8) && (localMarginPaper3 < localMarginPaper4)) || ((localMarginPaper7 < localMarginPaper8) && (localMarginPaper3 > localMarginPaper4))))
    {
      localMarginPaper9 = localMarginPaper1;
      localMarginPaper1 = localMarginPaper2;
      localMarginPaper2 = localMarginPaper9;
      localMarginPaper9 = localMarginPaper3;
      localMarginPaper3 = localMarginPaper4;
      localMarginPaper4 = localMarginPaper9;
      localMarginPaper9 = localMarginPaper5;
      localMarginPaper5 = localMarginPaper6;
      localMarginPaper6 = localMarginPaper9;
      localMarginPaper6 = localMarginPaper2 - localMarginPaper6 - localMarginPaper4;
    }
    int k = 25;
    int m = 30;
    int n = 5;
    int i1 = 25;
    double d1 = localMarginPaper2 / 72.0D;
    double d2 = localMarginPaper1 / 72.0D;
    double d3 = (i - (k + n)) / localMarginPaper1;
    double d4 = (j - (m + i1)) / localMarginPaper2;
    double d5;
    if (d3 < d4)
      d5 = d3;
    else
      d5 = d4;
    MarginPaper localMarginPaper1 = (int)(d5 * localMarginPaper1);
    MarginPaper localMarginPaper2 = (int)(d5 * localMarginPaper2);
    MarginPaper localMarginPaper5 = (int)(d5 * localMarginPaper5);
    MarginPaper localMarginPaper6 = (int)(d5 * localMarginPaper6);
    MarginPaper localMarginPaper3 = (int)(d5 * localMarginPaper3);
    MarginPaper localMarginPaper4 = (int)(d5 * localMarginPaper4);
    MarginPaper localMarginPaper7 = (int)(d5 * localMarginPaper7);
    MarginPaper localMarginPaper8 = (int)(d5 * localMarginPaper8);
    double d6 = 1.0D;
    if ((this.printHandlingScaling.getSelectedIndex() == 1) || ((this.printHandlingScaling.getSelectedIndex() == 2) && ((localMarginPaper7 > localMarginPaper3) || (localMarginPaper8 > localMarginPaper4))))
    {
      d6 = localMarginPaper3 / localMarginPaper7;
      if (localMarginPaper4 / localMarginPaper8 < d6)
        d6 = localMarginPaper4 / localMarginPaper8;
      localMarginPaper7 = (int)(d6 * localMarginPaper7);
      localMarginPaper8 = (int)(d6 * localMarginPaper8);
    }
    k += (i - (k + n + localMarginPaper1)) / 2;
    m += (j - (m + i1 + localMarginPaper2)) / 2;
    paramGraphics2D.setPaint(Color.WHITE);
    paramGraphics2D.fillRect(k, m, localMarginPaper1, localMarginPaper2);
    paramGraphics2D.setPaint(Color.RED);
    paramGraphics2D.drawLine(k + localMarginPaper5, m, k + localMarginPaper5, m + localMarginPaper2);
    paramGraphics2D.drawLine(k, m + localMarginPaper6, k + localMarginPaper1, m + localMarginPaper6);
    paramGraphics2D.drawLine(k + localMarginPaper3 + localMarginPaper5, m, k + localMarginPaper3 + localMarginPaper5, m + localMarginPaper2);
    paramGraphics2D.drawLine(k, m + localMarginPaper4 + localMarginPaper6, k + localMarginPaper1, m + localMarginPaper4 + localMarginPaper6);
    localMarginPaper5++;
    localMarginPaper6++;
    localMarginPaper3--;
    localMarginPaper4--;
    paramGraphics2D.setPaint(Color.BLACK);
    NumberFormat localNumberFormat = NumberFormat.getNumberInstance();
    localNumberFormat.setMaximumFractionDigits(1);
    String str1 = localNumberFormat.format(d2);
    int i2 = str1.length() * 3;
    paramGraphics2D.drawString(str1, k + localMarginPaper1 / 2 - i2, m - 5);
    paramGraphics2D.drawLine(k, m - 15, k, m - 5);
    paramGraphics2D.drawLine(k + localMarginPaper1, m - 15, k + localMarginPaper1, m - 5);
    paramGraphics2D.drawLine(k, m - 10, k + localMarginPaper1 / 2 - i2 - 6, m - 10);
    paramGraphics2D.drawLine(k + localMarginPaper1 / 2 + i2 + 6, m - 10, k + localMarginPaper1, m - 10);
    paramGraphics2D.drawLine(k, m - 10, k + 5, m - 15);
    paramGraphics2D.drawLine(k, m - 10, k + 5, m - 5);
    paramGraphics2D.drawLine(k + localMarginPaper1, m - 10, k + localMarginPaper1 - 5, m - 15);
    paramGraphics2D.drawLine(k + localMarginPaper1, m - 10, k + localMarginPaper1 - 5, m - 5);
    String str2 = localNumberFormat.format(d1);
    i2 = str2.length() * 3;
    paramGraphics2D.drawString(str2, k - 12 - i2, m + localMarginPaper2 / 2 + 5);
    paramGraphics2D.drawLine(k - 15, m, k - 5, m);
    paramGraphics2D.drawLine(k - 15, m + localMarginPaper2, k - 5, m + localMarginPaper2);
    paramGraphics2D.drawLine(k - 10, m, k - 10, m + localMarginPaper2 / 2 - 8);
    paramGraphics2D.drawLine(k - 10, m + localMarginPaper2 / 2 + 8, k - 10, m + localMarginPaper2);
    paramGraphics2D.drawLine(k - 10, m, k - 5, m + 5);
    paramGraphics2D.drawLine(k - 10, m, k - 15, m + 5);
    paramGraphics2D.drawLine(k - 10, m + localMarginPaper2, k - 15, m + localMarginPaper2 - 5);
    paramGraphics2D.drawLine(k - 10, m + localMarginPaper2, k - 5, m + localMarginPaper2 - 5);
    try
    {
      BufferedImage localBufferedImage;
      if (this.printRangeCurrentView.isSelected())
      {
        localBufferedImage = new BufferedImage((int)this.pdf.getVisibleRect().getWidth(), (int)this.pdf.getVisibleRect().getHeight(), 2);
        Graphics localGraphics = localBufferedImage.getGraphics();
        localGraphics.translate((int)-this.pdf.getVisibleRect().getX(), (int)-this.pdf.getVisibleRect().getY());
        Border localBorder = this.pdf.getBorder();
        Color localColor = this.pdf.getBackground();
        this.pdf.setBorder(BorderFactory.createEmptyBorder());
        this.pdf.setBackground(Color.WHITE);
        this.pdf.paintComponent(localGraphics);
        this.pdf.setBorder(localBorder);
        this.pdf.setBackground(localColor);
      }
      else
      {
        localBufferedImage = this.pdf.getPageAsImage(this.currentPreviewedPage);
      }
      if (this.grayscale.isSelected())
        localBufferedImage = ColorSpaceConvertor.convertColorspace(localBufferedImage, 10);
      paramGraphics2D.setClip(k + localMarginPaper5, m + localMarginPaper6, localMarginPaper3, localMarginPaper4);
      int i3 = (int)((localMarginPaper3 - localMarginPaper7) / 2.0D);
      int i4 = (int)((localMarginPaper4 - localMarginPaper8) / 2.0D);
      paramGraphics2D.drawImage(localBufferedImage, k + localMarginPaper5 + i3, m + localMarginPaper6 + i4, localMarginPaper7, localMarginPaper8, null);
    }
    catch (PdfException localPdfException)
    {
    }
    paramGraphics2D.setClip(null);
    paramGraphics2D.drawRect(k, m, localMarginPaper1, localMarginPaper2);
    localNumberFormat.setMaximumFractionDigits(0);
    paramGraphics2D.drawString(new StringBuilder().append(Messages.getMessage("PdfPrintPreview.UnitScale")).append(localNumberFormat.format(d6 * 100.0D)).append('%').toString(), 5, j - 5);
  }

  private void updatePreview()
  {
    int i = this.previewSlider.getValue();
    SetOfIntegerSyntax localSetOfIntegerSyntax = getPrintRange();
    if (localSetOfIntegerSyntax == null)
    {
      this.currentPreviewedPage = 0;
      this.pagePrintCount = 0;
    }
    else
    {
      int[][] arrayOfInt1 = localSetOfIntegerSyntax.getMembers();
      int j = 0;
      for (Object localObject2 : arrayOfInt1)
        j += localObject2[1] - (localObject2[0] - 1);
      ??? = new int[j];
      this.pagePrintCount = ???.length;
      j = 0;
      for (int[] arrayOfInt : arrayOfInt1)
      {
        int i1 = arrayOfInt[1] - (arrayOfInt[0] - 1);
        for (int i2 = 0; i2 < i1; i2++)
        {
          ???[j] = (arrayOfInt[0] + i2);
          j++;
        }
      }
      if (i > this.pagePrintCount)
      {
        i = 1;
        this.previewSlider.setValue(1);
      }
      if (this.printRangeReversePages.isSelected())
        this.currentPreviewedPage = ???[(???.length - i)];
      else
        this.currentPreviewedPage = ???[(i - 1)];
    }
    this.previewSlider.setMaxValue(this.pagePrintCount);
    this.previewComponent.repaint();
  }

  private void pagesBoxPressed()
  {
    this.printRangeFrom.setSelected(true);
    this.printRangeSubset.setEnabled(true);
    this.printRangeReversePages.setEnabled(true);
    updatePreview();
  }

  private void pagesFromStateChanged()
  {
    if (this.printRangeFrom.isSelected())
    {
      this.printRangeSubset.setEnabled(true);
      this.printRangeReversePages.setEnabled(true);
      updatePreview();
    }
  }

  private void printRangeCurrentView()
  {
    if (this.printRangeCurrentView.isSelected())
    {
      this.printRangeSubset.setEnabled(false);
      this.printRangeReversePages.setEnabled(false);
      updatePreview();
    }
  }

  private void currentPageStateChanged()
  {
    if (this.printRangeCurrentPage.isSelected())
    {
      this.printRangeSubset.setEnabled(false);
      this.printRangeReversePages.setEnabled(false);
      updatePreview();
    }
  }

  private void allStateChanged()
  {
    if (this.printRangeAll.isSelected())
    {
      this.printRangeSubset.setEnabled(true);
      this.printRangeReversePages.setEnabled(true);
      updatePreview();
    }
  }

  private void cancelEvent()
  {
    this.okClicked = false;
    getTopLevelAncestor().setVisible(false);
  }

  private void okEvent()
  {
    this.okClicked = true;
    getTopLevelAncestor().setVisible(false);
  }

  public SetOfIntegerSyntax getPrintRange()
  {
    PageRanges localPageRanges = null;
    NumberFormatException localNumberFormatException2;
    if (this.printRangeAll.isSelected())
    {
      localPageRanges = new PageRanges(1, this.pageCount);
      if (this.printRangeSubset.getSelectedIndex() == 0)
        return localPageRanges;
      String str1;
      StringBuilder localStringBuilder1;
      if (this.printRangeSubset.getSelectedIndex() == 1)
      {
        str1 = "";
        localNumberFormatException2 = -1;
        while ((localNumberFormatException2 = localPageRanges.next(localNumberFormatException2)) != -1)
          if (localNumberFormatException2 % 2 == 1)
            str1 = new StringBuilder().append(str1).append(localNumberFormatException2).append(",").toString();
        localStringBuilder1 = new StringBuilder(str1);
        localStringBuilder1.deleteCharAt(str1.length() - 1);
        str1 = localStringBuilder1.toString();
        localPageRanges = new PageRanges(str1);
      }
      else if (this.printRangeSubset.getSelectedIndex() == 2)
      {
        str1 = "";
        localNumberFormatException2 = -1;
        while ((localNumberFormatException2 = localPageRanges.next(localNumberFormatException2)) != -1)
          if (localNumberFormatException2 % 2 == 0)
            str1 = new StringBuilder().append(str1).append(localNumberFormatException2).append(",").toString();
        localStringBuilder1 = new StringBuilder(str1);
        localStringBuilder1.deleteCharAt(str1.length() - 1);
        str1 = localStringBuilder1.toString();
        localPageRanges = new PageRanges(str1);
      }
    }
    else if (this.printRangeCurrentPage.isSelected())
    {
      localPageRanges = new PageRanges(this.currentPage);
    }
    else if (this.printRangeCurrentView.isSelected())
    {
      localPageRanges = new PageRanges(this.currentPage);
    }
    else if (this.printRangeFrom.isSelected())
    {
      NumberFormatException localNumberFormatException1;
      try
      {
        localNumberFormatException1 = Integer.parseInt(this.pagesFrom.getText());
      }
      catch (NumberFormatException localNumberFormatException3)
      {
        this.pagesFrom.setText("1");
        localNumberFormatException1 = 1;
      }
      try
      {
        localNumberFormatException2 = Integer.parseInt(this.pagesTo.getText());
      }
      catch (NumberFormatException localNumberFormatException4)
      {
        this.pagesTo.setText(String.valueOf(this.pageCount));
        localNumberFormatException2 = this.pageCount;
      }
      if (localNumberFormatException1 < 0)
      {
        localNumberFormatException1 = 1;
        this.pagesFrom.setText("1");
      }
      else if (localNumberFormatException1 > this.pageCount)
      {
        localNumberFormatException1 = this.pageCount;
        this.pagesFrom.setText(String.valueOf(this.pageCount));
      }
      if (localNumberFormatException2 < 0)
      {
        localNumberFormatException2 = 1;
        this.pagesTo.setText("1");
      }
      else if (localNumberFormatException2 > this.pageCount)
      {
        localNumberFormatException2 = this.pageCount;
        this.pagesTo.setText(String.valueOf(this.pageCount));
      }
      if (localNumberFormatException1 > localNumberFormatException2)
      {
        localNumberFormatException4 = localNumberFormatException2;
        localNumberFormatException2 = localNumberFormatException1;
        localNumberFormatException1 = localNumberFormatException4;
        if (Viewer.showMessages)
          JOptionPane.showMessageDialog(this, Messages.getMessage("PdfViewerPrint.SwapValues"));
      }
      localPageRanges = new PageRanges(localNumberFormatException1, localNumberFormatException2);
      if (this.printRangeSubset.getSelectedIndex() == 0)
        return localPageRanges;
      String str2;
      int i;
      StringBuilder localStringBuilder2;
      if (this.printRangeSubset.getSelectedIndex() == 1)
      {
        str2 = "";
        i = -1;
        while ((i = localPageRanges.next(i)) != -1)
          if (i % 2 == 1)
            str2 = new StringBuilder().append(str2).append(i).append(",").toString();
        localStringBuilder2 = new StringBuilder(str2);
        if (str2.isEmpty())
          return null;
        localStringBuilder2.deleteCharAt(str2.length() - 1);
        str2 = localStringBuilder2.toString();
        localPageRanges = new PageRanges(str2);
      }
      else if (this.printRangeSubset.getSelectedIndex() == 2)
      {
        str2 = "";
        i = -1;
        while ((i = localPageRanges.next(i)) != -1)
          if (i % 2 == 0)
            str2 = new StringBuilder().append(str2).append(i).append(",").toString();
        localStringBuilder2 = new StringBuilder(str2);
        int j = str2.length();
        if (j > 0)
          localStringBuilder2.deleteCharAt(j - 1);
        str2 = localStringBuilder2.toString();
        if (!str2.isEmpty())
          localPageRanges = new PageRanges(str2);
        else
          localPageRanges = null;
      }
    }
    return localPageRanges;
  }

  public int getCopies()
  {
    String str = this.printHandlingCopies.getValue().toString();
    return Integer.parseInt(str);
  }

  public int getPageScaling()
  {
    int i = this.printHandlingScaling.getSelectedIndex();
    int j = 0;
    switch (i)
    {
    case 0:
      j = 0;
      break;
    case 1:
      j = 1;
      break;
    case 2:
      j = 2;
    }
    PrinterOptions.LAST_SCALING_CHOICE = j;
    return j;
  }

  public String getPrinter()
  {
    if (this.printerName == null)
      return "";
    if (this.printerName.getSelectedItem() == null)
      return null;
    return this.printerName.getSelectedItem().toString();
  }

  public boolean okClicked()
  {
    return this.okClicked;
  }

  public boolean isAutoRotateAndCenter()
  {
    return this.autoRotateCenter.isSelected();
  }

  public boolean isPaperSourceByPDFSize()
  {
    return this.paperSourceByPDF.isSelected();
  }

  public boolean isPrintingCurrentView()
  {
    return this.printRangeCurrentView.isSelected();
  }

  public String[] getAvailablePaperSizes()
  {
    return this.paperDefinitions.getAvailablePaperSizes();
  }

  public MarginPaper getSelectedPaper()
  {
    return this.paperDefinitions.getSelectedPaper(this.pageSize.getSelectedItem());
  }

  public int getSelectedPrinterOrientation()
  {
    return this.paperDefinitions.getDefaultPageOrientation();
  }

  public boolean isPagesReversed()
  {
    return this.printRangeReversePages.isSelected();
  }

  public boolean isOddPagesOnly()
  {
    return this.printRangeSubset.getSelectedIndex() == 1;
  }

  public boolean isEvenPagesOnly()
  {
    return this.printRangeSubset.getSelectedIndex() == 2;
  }

  public boolean isMonochrome()
  {
    return this.grayscale.isSelected();
  }

  private static class CustomSpinnerModel extends SpinnerNumberModel
  {
    private double value = 0.0D;
    private ArrayList listeners = new ArrayList();
    private double minValue = 0.0D;

    public Object getPreviousValue()
    {
      if (this.value <= this.minValue)
        return null;
      if (this.value - 0.5D < this.minValue)
        return Double.valueOf(this.minValue);
      return Double.valueOf(this.value - 0.5D);
    }

    public Object getNextValue()
    {
      return Double.valueOf(this.value + 0.5D);
    }

    public Object getValue()
    {
      return Double.valueOf(this.value);
    }

    public void addChangeListener(ChangeListener paramChangeListener)
    {
      this.listeners.add(paramChangeListener);
    }

    public void removeChangeListener(ChangeListener paramChangeListener)
    {
      this.listeners.remove(paramChangeListener);
    }

    public void setValue(Object paramObject)
    {
      try
      {
        double d = ((Double)paramObject).doubleValue();
        if (d < this.minValue)
          this.value = this.minValue;
        else
          this.value = d;
      }
      catch (Exception localException)
      {
        throw new IllegalArgumentException();
      }
      ChangeEvent localChangeEvent = new ChangeEvent(this);
      Iterator localIterator = this.listeners.iterator();
      while (localIterator.hasNext())
      {
        Object localObject = localIterator.next();
        ((ChangeListener)localObject).stateChanged(localChangeEvent);
      }
    }

    public void setMinValue(double paramDouble)
    {
      this.minValue = paramDouble;
      setValue(getValue());
    }
  }

  private class CustomSlider extends JPanel
  {
    private int value = 1;
    private int maxValue = 100;
    private static final int rightMargin = 9;
    private static final int leftMargin = 9;
    private boolean dragging = false;

    public CustomSlider()
    {
      addMouseMotionListener(new MouseMotionAdapter()
      {
        public void mouseDragged(MouseEvent paramAnonymousMouseEvent)
        {
          if (PrintPanel.CustomSlider.this.dragging)
          {
            PrintPanel.CustomSlider.this.value = ((int)((paramAnonymousMouseEvent.getX() - 9) / (PrintPanel.CustomSlider.this.getWidth() - 18) * (PrintPanel.CustomSlider.this.maxValue - 1) + 1.5D));
            if (PrintPanel.CustomSlider.this.value > PrintPanel.CustomSlider.this.maxValue)
              PrintPanel.CustomSlider.this.value = PrintPanel.CustomSlider.this.maxValue;
            if (PrintPanel.CustomSlider.this.value < 1)
              PrintPanel.CustomSlider.this.value = 1;
            PrintPanel.this.updatePreview();
            PrintPanel.CustomSlider.this.repaint();
          }
        }
      });
      addMouseListener(new MouseAdapter()
      {
        public void mouseClicked(MouseEvent paramAnonymousMouseEvent)
        {
          if (paramAnonymousMouseEvent.getY() < 20)
          {
            double d = (paramAnonymousMouseEvent.getX() - 9) / (PrintPanel.CustomSlider.this.getWidth() - 18) * (PrintPanel.CustomSlider.this.maxValue - 1) + 1.0D;
            if (d > PrintPanel.CustomSlider.this.value)
              PrintPanel.CustomSlider.access$2508(PrintPanel.CustomSlider.this);
            else if (d < PrintPanel.CustomSlider.this.value)
              PrintPanel.CustomSlider.access$2510(PrintPanel.CustomSlider.this);
            if (PrintPanel.CustomSlider.this.value > PrintPanel.CustomSlider.this.maxValue)
              PrintPanel.CustomSlider.this.value = PrintPanel.CustomSlider.this.maxValue;
            if (PrintPanel.CustomSlider.this.value < 1)
              PrintPanel.CustomSlider.this.value = 1;
            PrintPanel.this.updatePreview();
            PrintPanel.CustomSlider.this.repaint();
          }
        }

        public void mousePressed(MouseEvent paramAnonymousMouseEvent)
        {
          if (paramAnonymousMouseEvent.getY() < 20)
            PrintPanel.CustomSlider.this.dragging = true;
        }

        public void mouseReleased(MouseEvent paramAnonymousMouseEvent)
        {
          PrintPanel.CustomSlider.this.dragging = false;
        }
      });
    }

    public void paint(Graphics paramGraphics)
    {
      Graphics2D localGraphics2D = (Graphics2D)paramGraphics;
      localGraphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      int i = getWidth();
      int j = getHeight();
      localGraphics2D.setPaint(new Color(240, 240, 240));
      localGraphics2D.fillRect(0, 0, i, j);
      localGraphics2D.setPaint(Color.BLACK);
      localGraphics2D.drawString(Messages.getMessage("PdfPrintPreview.Sheet") + this.value + Messages.getMessage("PdfPrintPreview.SheetOf") + this.maxValue, 2, j - 3);
      localGraphics2D.setPaint(Color.LIGHT_GRAY);
      localGraphics2D.fillRect(10, 11, i - 19, 3);
      localGraphics2D.setPaint(Color.GRAY);
      localGraphics2D.drawLine(9, 12, i - 9, 12);
      float f = 9.0F + (this.value - 1) / (this.maxValue - 1) * (i - 18);
      Ellipse2D.Float localFloat1 = new Ellipse2D.Float(f - 6.0F, 6.5F, 12.0F, 12.0F);
      Ellipse2D.Float localFloat2 = new Ellipse2D.Float(f - 4.0F, 8.5F, 8.0F, 8.0F);
      localGraphics2D.setPaint(Color.BLACK);
      localGraphics2D.fill(localFloat1);
      localGraphics2D.setPaint(Color.WHITE);
      localGraphics2D.fill(localFloat2);
    }

    public void setValue(int paramInt)
    {
      this.value = paramInt;
      repaint();
    }

    public void setMaxValue(int paramInt)
    {
      if (paramInt != this.maxValue)
      {
        this.value = 1;
        this.maxValue = paramInt;
        PrintPanel.this.updatePreview();
        return;
      }
      this.maxValue = paramInt;
      repaint();
    }

    public int getValue()
    {
      return this.value;
    }
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.gui.popups.PrintPanel
 * JD-Core Version:    0.6.2
 */