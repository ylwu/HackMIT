package org.jpedal.examples.viewer.gui.popups;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jpedal.PdfDecoder;
import org.jpedal.examples.viewer.objects.SignData;
import org.jpedal.examples.viewer.utils.FileFilterer;
import org.jpedal.exception.PdfException;
import org.jpedal.objects.PdfPageData;
import org.jpedal.objects.acroforms.AcroRenderer;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.Messages;

public class SignWizardModel
  implements WizardPanelModel
{
  private static final String MODE_SELECT = "0";
  private static final String PFX_PANEL = "1";
  private static final String KEYSTORE_PANEL = "3";
  private static final String COMMON_PANEL = "4";
  private static final String ENCRYPTION_PANEL = "5";
  private static final String VISIBLE_SIGNATURE_PANEL = "6";
  public static final String NO_FILE_SELECTED = Messages.getMessage("PdfSigner.NoFileSelected");
  private static final int MAXIMUM_PANELS = 5;
  private SignData signData;
  private PdfDecoder pdfDecoder;
  private String rootDir;
  private ModeSelect modeSelect;
  private PFXPanel pFXPanel;
  private KeystorePanel keystorePanel;
  private CommonPanel commonPanel;
  private EncryptionPanel encryptionPanel;
  private SignaturePanel signaturePanel;
  private Map panels;
  private String currentPanel;

  public SignWizardModel(SignData paramSignData, String paramString1, String paramString2)
  {
    this.signData = paramSignData;
    this.rootDir = paramString2;
    this.pdfDecoder = new PdfDecoder();
    try
    {
      this.pdfDecoder.openPdfFile(paramString1);
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    if (this.pdfDecoder.isEncrypted())
    {
      String str = System.getProperty("org.jpedal.password");
      if (str != null)
        try
        {
          this.pdfDecoder.setEncryptionPassword(str);
        }
        catch (PdfException localPdfException)
        {
          localPdfException.printStackTrace();
        }
    }
    testForSignedPDF();
    this.panels = new HashMap();
    this.modeSelect = new ModeSelect();
    this.pFXPanel = new PFXPanel();
    this.keystorePanel = new KeystorePanel();
    this.commonPanel = new CommonPanel();
    this.encryptionPanel = new EncryptionPanel();
    this.signaturePanel = new SignaturePanel();
    this.panels.put("0", this.modeSelect);
    this.panels.put("1", this.pFXPanel);
    this.panels.put("3", this.keystorePanel);
    this.panels.put("4", this.commonPanel);
    this.panels.put("5", this.encryptionPanel);
    this.panels.put("6", this.signaturePanel);
    this.currentPanel = "0";
  }

  public Map getJPanels()
  {
    return this.panels;
  }

  public String next()
  {
    updateSignData();
    if (this.currentPanel.equals("0"))
    {
      if (!this.signData.isKeystoreSign())
        return this.currentPanel = "1";
      return this.currentPanel = "3";
    }
    if (this.currentPanel.equals("1"))
      return this.currentPanel = "6";
    if (this.currentPanel.equals("3"))
      return this.currentPanel = "6";
    if (this.currentPanel.equals("6"))
      return this.currentPanel = "5";
    if (this.currentPanel.equals("5"))
      return this.currentPanel = "4";
    throw new NullPointerException("Whoops! Tried to move to a nextID where there is no nextID to be had");
  }

  public String previous()
  {
    updateSignData();
    if ((this.currentPanel.equals("1")) || (this.currentPanel.equals("3")))
      return this.currentPanel = "0";
    if (this.currentPanel.equals("5"))
      return this.currentPanel = "6";
    if (this.currentPanel.equals("6"))
    {
      if (this.signData.isKeystoreSign())
        return this.currentPanel = "3";
      return this.currentPanel = "1";
    }
    if (this.currentPanel.equals("4"))
      return this.currentPanel = "5";
    throw new NullPointerException("Tried to move to get a previousID where there is no previous");
  }

  public boolean hasPrevious()
  {
    return !this.currentPanel.equals("0");
  }

  public String getStartPanelID()
  {
    return "0";
  }

  public boolean isFinishPanel()
  {
    return this.currentPanel == null ? false : "4" == null ? true : this.currentPanel.equals("4");
  }

  public boolean canAdvance()
  {
    if (this.currentPanel.equals("4"))
      return this.commonPanel.canFinish();
    if (this.currentPanel.equals("1"))
      return this.pFXPanel.canAdvance();
    if (this.currentPanel.equals("3"))
      return this.keystorePanel.canAdvance();
    if (this.currentPanel.equals("5"))
      return this.encryptionPanel.canAdvance();
    return true;
  }

  public void updateSignData()
  {
    if (this.currentPanel.equals("1"))
      this.pFXPanel.collectData();
    else if (this.currentPanel.equals("3"))
      this.keystorePanel.collectData();
    else if (this.currentPanel.equals("4"))
      this.commonPanel.collectData();
    else if (this.currentPanel.equals("5"))
      this.encryptionPanel.collectData();
    else if (this.currentPanel.equals("0"))
      this.modeSelect.collectData();
    else if (this.currentPanel.equals("6"))
      this.signaturePanel.collectData();
    else
      throw new NullPointerException("Tried to update a panel which doesnt exist");
  }

  public void registerNextChangeListeners(ChangeListener paramChangeListener)
  {
    this.commonPanel.registerChange(paramChangeListener);
    this.pFXPanel.registerChange(paramChangeListener);
    this.keystorePanel.registerChange(paramChangeListener);
    this.encryptionPanel.registerChange(paramChangeListener);
  }

  public void registerNextKeyListeners(KeyListener paramKeyListener)
  {
    this.pFXPanel.registerListener(paramKeyListener);
    this.keystorePanel.registerNextKeyListeners(paramKeyListener);
    this.encryptionPanel.registerNextKeyListeners(paramKeyListener);
  }

  public void close()
  {
    updateSignData();
    this.pdfDecoder.closePdfFile();
  }

  private void testForSignedPDF()
  {
    this.signData.setAppend(false);
    for (int i = 1; i <= this.pdfDecoder.getPageCount(); i++)
      try
      {
        this.pdfDecoder.decodePage(i);
        this.pdfDecoder.waitForDecodingToFinish();
        AcroRenderer localAcroRenderer = this.pdfDecoder.getFormRenderer();
        Iterator localIterator = localAcroRenderer.getSignatureObjects();
        if (localIterator != null)
        {
          this.signData.setAppend(true);
          break;
        }
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
      }
  }

  private boolean isPdfSigned()
  {
    return this.signData.isAppendMode();
  }

  private static class TitlePanel extends JPanel
  {
    public TitlePanel(String paramString)
    {
      setBackground(Color.gray);
      setBorder(BorderFactory.createEtchedBorder(0));
      JLabel localJLabel = new JLabel();
      localJLabel.setBackground(Color.gray);
      localJLabel.setFont(new Font("Dialog", 1, 14));
      localJLabel.setText(paramString);
      localJLabel.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));
      localJLabel.setOpaque(true);
      add(localJLabel);
    }
  }

  private static class ProgressPanel extends JPanel
  {
    public ProgressPanel(int paramInt)
    {
      setBorder(new EtchedBorder());
      JLabel localJLabel = new JLabel("Step " + paramInt + " of " + 5);
      localJLabel.setAlignmentX(1.0F);
      add(localJLabel);
    }
  }

  private class SignaturePanel extends JPanel
  {
    private JCheckBox visibleCheck = new JCheckBox(Messages.getMessage("PdfSigner.VisibleSignature"));
    private JComponent sigPreviewComp;
    private JSlider pageSlider;
    private JLabel pageNumberLabel;
    private int currentPage = 1;
    private Point signRectOrigin;
    private Point signRectEnd;
    private int offsetX;
    private int offsetY;
    private float scale;
    private int previewWidth;
    private int previewHeight;
    private volatile boolean drawRect = false;
    private boolean signAreaUndefined = true;
    private BufferedImage previewImage;

    public SignaturePanel()
    {
      try
      {
        this.previewImage = SignWizardModel.this.pdfDecoder.getPageAsImage(this.currentPage);
      }
      catch (Exception localException)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog("Exception: " + localException.getMessage());
      }
      int i = 0;
      setLayout(new BorderLayout());
      add(new SignWizardModel.TitlePanel(Messages.getMessage("PdfSigner.VisibleSignature") + ' ' + Messages.getMessage("PdfViewerMenu.options")), "North");
      JPanel localJPanel = new JPanel();
      localJPanel.setLayout(new GridBagLayout());
      GridBagConstraints localGridBagConstraints = new GridBagConstraints();
      localGridBagConstraints.gridx = 0;
      localGridBagConstraints.gridy = i;
      localGridBagConstraints.insets = new Insets(5, 0, 0, 0);
      localGridBagConstraints.fill = 2;
      localGridBagConstraints.anchor = 19;
      this.visibleCheck.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent paramAnonymousActionEvent)
        {
          SignWizardModel.SignaturePanel.this.sigPreviewComp.repaint();
          if (SignWizardModel.this.pdfDecoder.getPageCount() > 1)
            SignWizardModel.SignaturePanel.this.pageSlider.setEnabled(SignWizardModel.SignaturePanel.this.visibleCheck.isSelected());
        }
      });
      localJPanel.add(this.visibleCheck, localGridBagConstraints);
      localGridBagConstraints = new GridBagConstraints();
      localGridBagConstraints.gridx = 0;
      localGridBagConstraints.gridy = (++i);
      localGridBagConstraints.fill = 2;
      localGridBagConstraints.insets = new Insets(10, 0, 10, 0);
      localJPanel.add(new JSeparator(0), localGridBagConstraints);
      localGridBagConstraints = new GridBagConstraints();
      localGridBagConstraints.gridx = 0;
      localGridBagConstraints.gridy = (++i);
      localGridBagConstraints.fill = 2;
      localJPanel.add(previewPanel(), localGridBagConstraints);
      add(localJPanel, "Center");
      add(new SignWizardModel.ProgressPanel(3), "South");
    }

    public void collectData()
    {
      SignWizardModel.this.signData.setVisibleSignature(this.visibleCheck.isSelected());
      if (this.visibleCheck.isSelected())
      {
        int i = this.previewImage.getHeight();
        int j = (int)((this.signRectOrigin.getX() - this.offsetX) / this.scale);
        int k = (int)(i - (this.signRectOrigin.getY() - this.offsetY) / this.scale);
        int m = (int)((this.signRectEnd.getX() - this.offsetX) / this.scale);
        int n = (int)(i - (this.signRectEnd.getY() - this.offsetY) / this.scale);
        PdfPageData localPdfPageData = SignWizardModel.this.pdfDecoder.getPdfPageData();
        int i1 = localPdfPageData.getCropBoxX(this.currentPage);
        int i2 = localPdfPageData.getCropBoxY(this.currentPage);
        j += i1;
        k += i2;
        m += i1;
        n += i2;
        SignWizardModel.this.signData.setRectangle(j, k, m, n);
        SignWizardModel.this.signData.setSignPage(this.currentPage);
      }
    }

    private JPanel previewPanel()
    {
      JPanel localJPanel = new JPanel(new BorderLayout());
      this.sigPreviewComp = new JComponent()
      {
        public void paintComponent(Graphics paramAnonymousGraphics)
        {
          SignWizardModel.SignaturePanel.this.sigPreview(paramAnonymousGraphics);
        }
      };
      this.sigPreviewComp.setPreferredSize(new Dimension(200, 200));
      this.sigPreviewComp.setToolTipText(Messages.getMessage("PdfSigner.ClickAndDrag"));
      this.sigPreviewComp.addMouseListener(new MouseListener()
      {
        public void mouseClicked(MouseEvent paramAnonymousMouseEvent)
        {
        }

        public void mouseEntered(MouseEvent paramAnonymousMouseEvent)
        {
        }

        public void mouseExited(MouseEvent paramAnonymousMouseEvent)
        {
        }

        public void mousePressed(MouseEvent paramAnonymousMouseEvent)
        {
          if (SignWizardModel.SignaturePanel.this.visibleCheck.isSelected())
          {
            SignWizardModel.SignaturePanel.this.signRectOrigin.setLocation(paramAnonymousMouseEvent.getX(), paramAnonymousMouseEvent.getY());
            SignWizardModel.SignaturePanel.this.drawRect = true;
            Thread localThread = new Thread(SignWizardModel.SignaturePanel.this.signAreaThread());
            localThread.setDaemon(true);
            localThread.start();
          }
        }

        public void mouseReleased(MouseEvent paramAnonymousMouseEvent)
        {
          if (SignWizardModel.SignaturePanel.this.visibleCheck.isSelected())
          {
            SignWizardModel.SignaturePanel.this.drawRect = false;
            SignWizardModel.SignaturePanel.this.sigPreviewComp.repaint();
          }
        }
      });
      localJPanel.add(this.sigPreviewComp, "Center");
      if (SignWizardModel.this.pdfDecoder.getPageCount() > 1)
      {
        this.pageNumberLabel = new JLabel(Messages.getMessage("PdfSigner.PageNumber") + ' ' + this.currentPage);
        this.pageNumberLabel.setHorizontalAlignment(0);
        localJPanel.add(this.pageNumberLabel, "North");
        this.pageSlider = new JSlider(0, 1, SignWizardModel.this.pdfDecoder.getPageCount(), this.currentPage);
        this.pageSlider.setMajorTickSpacing(SignWizardModel.this.pdfDecoder.getPageCount() - 1);
        this.pageSlider.setPaintLabels(true);
        this.pageSlider.addChangeListener(new ChangeListener()
        {
          public void stateChanged(ChangeEvent paramAnonymousChangeEvent)
          {
            if (SignWizardModel.SignaturePanel.this.pageSlider.getValueIsAdjusting())
            {
              SignWizardModel.SignaturePanel.this.currentPage = SignWizardModel.SignaturePanel.this.pageSlider.getValue();
              try
              {
                SignWizardModel.SignaturePanel.this.previewImage = SignWizardModel.this.pdfDecoder.getPageAsImage(SignWizardModel.SignaturePanel.this.currentPage);
                SignWizardModel.SignaturePanel.this.sigPreviewComp.repaint();
                SignWizardModel.SignaturePanel.this.pageNumberLabel.setText(Messages.getMessage("PdfSigner.PageNumber") + ' ' + SignWizardModel.SignaturePanel.this.currentPage);
              }
              catch (Exception localException)
              {
                if (LogWriter.isOutput())
                  LogWriter.writeLog("Exception: " + localException.getMessage());
              }
            }
          }
        });
        localJPanel.add(this.pageSlider, "South");
        this.pageSlider.setEnabled(false);
      }
      return localJPanel;
    }

    private void sigPreview(Graphics paramGraphics)
    {
      int i = this.sigPreviewComp.getWidth();
      int j = this.sigPreviewComp.getHeight();
      this.previewWidth = this.previewImage.getWidth();
      this.previewHeight = this.previewImage.getHeight();
      this.scale = (this.previewWidth > this.previewHeight ? i / this.previewWidth : j / this.previewHeight);
      this.previewWidth = ((int)(this.previewWidth * this.scale));
      this.previewHeight = ((int)(this.previewHeight * this.scale));
      this.offsetX = ((i - this.previewWidth) / 2);
      this.offsetY = ((j - this.previewHeight) / 2);
      paramGraphics.drawImage(this.previewImage, this.offsetX, this.offsetY, this.previewWidth, this.previewHeight, null);
      if (this.visibleCheck.isSelected())
      {
        paramGraphics.clipRect(this.offsetX, this.offsetY, this.previewWidth, this.previewHeight);
        drawSignBox(paramGraphics);
      }
    }

    private void drawSignBox(Graphics paramGraphics)
    {
      if (this.signAreaUndefined)
      {
        PdfPageData localPdfPageData = SignWizardModel.this.pdfDecoder.getPdfPageData();
        this.signRectOrigin = new Point(this.offsetX, this.offsetY);
        this.signRectEnd = new Point((int)(localPdfPageData.getCropBoxWidth(this.currentPage) * this.scale) - 1 + this.offsetX, (int)(localPdfPageData.getCropBoxHeight(this.currentPage) * this.scale) - 1 + this.offsetY);
        this.signAreaUndefined = false;
      }
      int i = (int)this.signRectOrigin.getX();
      int j = (int)this.signRectOrigin.getY();
      int k = (int)this.signRectEnd.getX();
      int m = (int)this.signRectEnd.getY();
      int n;
      if (i > k)
      {
        n = k;
        k = i;
        i = n;
      }
      if (j > m)
      {
        n = j;
        j = m;
        m = n;
      }
      paramGraphics.drawRect(i, j, k - i, m - j);
      paramGraphics.drawLine(i, j, k, m);
      paramGraphics.drawLine(i, m, k, j);
    }

    private Runnable signAreaThread()
    {
      return new Runnable()
      {
        public void run()
        {
          Point localPoint = SignWizardModel.SignaturePanel.this.sigPreviewComp.getLocationOnScreen();
          while (SignWizardModel.SignaturePanel.this.drawRect)
          {
            try
            {
              Thread.sleep(100L);
            }
            catch (Exception localException)
            {
              if (LogWriter.isOutput())
                LogWriter.writeLog("Exception: " + localException.getMessage());
            }
            double d1 = MouseInfo.getPointerInfo().getLocation().getX() - localPoint.getX();
            double d2 = MouseInfo.getPointerInfo().getLocation().getY() - localPoint.getY();
            SignWizardModel.SignaturePanel.this.signRectEnd.setLocation(d1, d2);
            SignWizardModel.SignaturePanel.this.sigPreviewComp.repaint();
          }
        }
      };
    }
  }

  private class EncryptionPanel extends JPanel
  {
    private JCheckBox encryptionCheck = new JCheckBox("Encrypt");
    private JCheckBox allowPrinting = new JCheckBox("Allow Printing");
    private JCheckBox allowModifyContent = new JCheckBox("Allow Content Modification");
    private JCheckBox allowCopy = new JCheckBox("Allow Copy");
    private JCheckBox allowModifyAnnotation = new JCheckBox("Allow Annotation Modification");
    private JCheckBox allowFillIn = new JCheckBox("Allow Fill In");
    private JCheckBox allowScreenReader = new JCheckBox("Allow Screen Reader");
    private JCheckBox allowAssembly = new JCheckBox("Allow Assembly");
    private JCheckBox allowDegradedPrinting = new JCheckBox("Allow Degraded Printing");
    private JPasswordField userPassword = new JPasswordField();
    private JPasswordField ownerPassword = new JPasswordField();
    private JCheckBox flatten = new JCheckBox("Flatten PDF");
    private JCheckBox visiblePassUserCheck = new JCheckBox();
    private JCheckBox visiblePassOwnerCheck = new JCheckBox();
    private boolean ownerAdvance = false;
    private volatile boolean canAdvance = true;

    public EncryptionPanel()
    {
      int i = 0;
      setLayout(new BorderLayout());
      add(new SignWizardModel.TitlePanel(Messages.getMessage("PdfSigner.EncryptionOptions")), "North");
      JPanel localJPanel = new JPanel();
      localJPanel.setLayout(new GridBagLayout());
      GridBagConstraints localGridBagConstraints = new GridBagConstraints();
      localGridBagConstraints.gridx = 0;
      localGridBagConstraints.gridy = i;
      localGridBagConstraints.fill = 2;
      localGridBagConstraints.anchor = 19;
      this.encryptionCheck.setEnabled(!SignWizardModel.this.isPdfSigned());
      this.encryptionCheck.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent paramAnonymousActionEvent)
        {
          SignWizardModel.EncryptionPanel.this.canAdvance = ((!SignWizardModel.EncryptionPanel.this.encryptionCheck.isSelected()) || (SignWizardModel.EncryptionPanel.this.ownerAdvance));
        }
      });
      localJPanel.add(this.encryptionCheck, localGridBagConstraints);
      this.encryptionCheck.setSelected(false);
      localGridBagConstraints = new GridBagConstraints();
      localGridBagConstraints.gridx = 2;
      localGridBagConstraints.gridy = i;
      localGridBagConstraints.fill = 2;
      localGridBagConstraints.anchor = 24;
      this.flatten.setEnabled(!SignWizardModel.this.isPdfSigned());
      localJPanel.add(this.flatten, localGridBagConstraints);
      localGridBagConstraints = new GridBagConstraints();
      localGridBagConstraints.gridx = 0;
      localGridBagConstraints.gridy = (++i);
      localGridBagConstraints.gridwidth = 3;
      localGridBagConstraints.fill = 2;
      localJPanel.add(new JSeparator(0), localGridBagConstraints);
      this.allowPrinting.setEnabled(false);
      localGridBagConstraints = new GridBagConstraints();
      localGridBagConstraints.gridx = 0;
      localGridBagConstraints.gridy = (++i);
      localGridBagConstraints.fill = 2;
      localJPanel.add(this.allowPrinting, localGridBagConstraints);
      this.allowModifyContent.setEnabled(false);
      localGridBagConstraints = new GridBagConstraints();
      localGridBagConstraints.gridx = 1;
      localGridBagConstraints.gridy = i;
      localGridBagConstraints.fill = 2;
      localGridBagConstraints.gridwidth = 2;
      localJPanel.add(this.allowModifyContent, localGridBagConstraints);
      this.allowCopy.setEnabled(false);
      localGridBagConstraints = new GridBagConstraints();
      localGridBagConstraints.gridx = 0;
      localGridBagConstraints.gridy = (++i);
      localGridBagConstraints.fill = 2;
      localJPanel.add(this.allowCopy, localGridBagConstraints);
      this.allowModifyAnnotation.setEnabled(false);
      localGridBagConstraints = new GridBagConstraints();
      localGridBagConstraints.gridx = 1;
      localGridBagConstraints.gridy = i;
      localGridBagConstraints.fill = 2;
      localGridBagConstraints.gridwidth = 2;
      localJPanel.add(this.allowModifyAnnotation, localGridBagConstraints);
      this.allowFillIn.setEnabled(false);
      localGridBagConstraints = new GridBagConstraints();
      localGridBagConstraints.gridx = 0;
      localGridBagConstraints.gridy = (++i);
      localGridBagConstraints.fill = 2;
      localJPanel.add(this.allowFillIn, localGridBagConstraints);
      this.allowScreenReader.setEnabled(false);
      localGridBagConstraints = new GridBagConstraints();
      localGridBagConstraints.gridx = 1;
      localGridBagConstraints.gridy = i;
      localGridBagConstraints.fill = 2;
      localGridBagConstraints.gridwidth = 2;
      localJPanel.add(this.allowScreenReader, localGridBagConstraints);
      this.allowAssembly.setEnabled(false);
      localGridBagConstraints = new GridBagConstraints();
      localGridBagConstraints.gridx = 0;
      localGridBagConstraints.gridy = (++i);
      localGridBagConstraints.fill = 2;
      localJPanel.add(this.allowAssembly, localGridBagConstraints);
      this.allowDegradedPrinting.setEnabled(false);
      localGridBagConstraints = new GridBagConstraints();
      localGridBagConstraints.gridx = 1;
      localGridBagConstraints.gridy = i;
      localGridBagConstraints.fill = 2;
      localGridBagConstraints.gridwidth = 2;
      localJPanel.add(this.allowDegradedPrinting, localGridBagConstraints);
      localGridBagConstraints = new GridBagConstraints();
      localGridBagConstraints.gridx = 0;
      localGridBagConstraints.gridy = (++i);
      localGridBagConstraints.gridwidth = 3;
      localGridBagConstraints.fill = 2;
      localJPanel.add(new JSeparator(0), localGridBagConstraints);
      localGridBagConstraints = new GridBagConstraints();
      localGridBagConstraints.gridx = 0;
      localGridBagConstraints.gridy = (++i);
      localGridBagConstraints.fill = 2;
      localGridBagConstraints.insets = new Insets(5, 0, 0, 0);
      localJPanel.add(new JLabel(Messages.getMessage("PdfSigner.UserPassword")), localGridBagConstraints);
      localGridBagConstraints = new GridBagConstraints();
      localGridBagConstraints.gridx = 1;
      localGridBagConstraints.gridy = i;
      localGridBagConstraints.fill = 2;
      localGridBagConstraints.insets = new Insets(5, 0, 0, 0);
      this.userPassword.setEnabled(false);
      this.userPassword.setPreferredSize(new Dimension(100, 20));
      this.userPassword.addKeyListener(new KeyListener()
      {
        public void keyReleased(KeyEvent paramAnonymousKeyEvent)
        {
        }

        public void keyPressed(KeyEvent paramAnonymousKeyEvent)
        {
        }

        public void keyTyped(KeyEvent paramAnonymousKeyEvent)
        {
          SignWizardModel.EncryptionPanel.this.ownerAdvance = true;
          SignWizardModel.EncryptionPanel.this.canAdvance = true;
        }
      });
      localJPanel.add(this.userPassword, localGridBagConstraints);
      localGridBagConstraints = new GridBagConstraints();
      localGridBagConstraints.fill = 2;
      localGridBagConstraints.gridx = 2;
      localGridBagConstraints.gridy = i;
      localGridBagConstraints.insets = new Insets(0, 0, 0, 0);
      this.visiblePassUserCheck.setToolTipText(Messages.getMessage("PdfSigner.ShowPassword"));
      this.visiblePassUserCheck.addActionListener(new ActionListener()
      {
        private char defaultChar;

        public void actionPerformed(ActionEvent paramAnonymousActionEvent)
        {
          if (SignWizardModel.EncryptionPanel.this.visiblePassUserCheck.isSelected())
          {
            this.defaultChar = SignWizardModel.EncryptionPanel.this.userPassword.getEchoChar();
            SignWizardModel.EncryptionPanel.this.userPassword.setEchoChar('\000');
          }
          else
          {
            SignWizardModel.EncryptionPanel.this.userPassword.setEchoChar(this.defaultChar);
          }
        }
      });
      this.visiblePassUserCheck.setEnabled(false);
      localJPanel.add(this.visiblePassUserCheck, localGridBagConstraints);
      localGridBagConstraints = new GridBagConstraints();
      localGridBagConstraints.gridx = 0;
      localGridBagConstraints.gridy = (++i);
      localGridBagConstraints.fill = 2;
      localGridBagConstraints.insets = new Insets(5, 0, 0, 0);
      localJPanel.add(new JLabel(Messages.getMessage("PdfSigner.OwnerPassword")), localGridBagConstraints);
      localGridBagConstraints = new GridBagConstraints();
      localGridBagConstraints.gridx = 1;
      localGridBagConstraints.gridy = i;
      localGridBagConstraints.fill = 2;
      localGridBagConstraints.insets = new Insets(5, 0, 0, 0);
      this.ownerPassword.setEnabled(false);
      this.ownerPassword.setPreferredSize(new Dimension(100, 20));
      localJPanel.add(this.ownerPassword, localGridBagConstraints);
      localGridBagConstraints = new GridBagConstraints();
      localGridBagConstraints.fill = 2;
      localGridBagConstraints.gridx = 2;
      localGridBagConstraints.gridy = i;
      localGridBagConstraints.insets = new Insets(0, 0, 0, 0);
      this.visiblePassOwnerCheck.setToolTipText(Messages.getMessage("PdfSigner.ShowPassword"));
      this.visiblePassOwnerCheck.addActionListener(new ActionListener()
      {
        private char defaultChar;

        public void actionPerformed(ActionEvent paramAnonymousActionEvent)
        {
          if (SignWizardModel.EncryptionPanel.this.visiblePassOwnerCheck.isSelected())
          {
            this.defaultChar = SignWizardModel.EncryptionPanel.this.ownerPassword.getEchoChar();
            SignWizardModel.EncryptionPanel.this.ownerPassword.setEchoChar('\000');
          }
          else
          {
            SignWizardModel.EncryptionPanel.this.ownerPassword.setEchoChar(this.defaultChar);
          }
        }
      });
      this.visiblePassOwnerCheck.setEnabled(false);
      localJPanel.add(this.visiblePassOwnerCheck, localGridBagConstraints);
      if (SignWizardModel.this.isPdfSigned())
      {
        localGridBagConstraints = new GridBagConstraints();
        localGridBagConstraints.fill = 2;
        localGridBagConstraints.gridx = 0;
        localGridBagConstraints.gridy = (++i);
        localGridBagConstraints.gridwidth = 3;
        localGridBagConstraints.insets = new Insets(25, 0, 0, 0);
        JLabel localJLabel = new JLabel(Messages.getMessage("PdfSigner.DisabledSigned"), 0);
        localJLabel.setForeground(Color.red);
        localJPanel.add(localJLabel, localGridBagConstraints);
      }
      this.encryptionCheck.addItemListener(new ItemListener()
      {
        public void itemStateChanged(ItemEvent paramAnonymousItemEvent)
        {
          boolean bool = paramAnonymousItemEvent.getStateChange() == 1;
          SignWizardModel.EncryptionPanel.this.allowPrinting.setEnabled(bool);
          SignWizardModel.EncryptionPanel.this.allowModifyContent.setEnabled(bool);
          SignWizardModel.EncryptionPanel.this.allowCopy.setEnabled(bool);
          SignWizardModel.EncryptionPanel.this.allowModifyAnnotation.setEnabled(bool);
          SignWizardModel.EncryptionPanel.this.allowFillIn.setEnabled(bool);
          SignWizardModel.EncryptionPanel.this.allowScreenReader.setEnabled(bool);
          SignWizardModel.EncryptionPanel.this.allowAssembly.setEnabled(bool);
          SignWizardModel.EncryptionPanel.this.allowDegradedPrinting.setEnabled(bool);
          SignWizardModel.EncryptionPanel.this.userPassword.setEnabled(bool);
          SignWizardModel.EncryptionPanel.this.ownerPassword.setEnabled(bool);
          SignWizardModel.EncryptionPanel.this.visiblePassUserCheck.setEnabled(bool);
          SignWizardModel.EncryptionPanel.this.visiblePassOwnerCheck.setEnabled(bool);
        }
      });
      add(localJPanel, "Center");
      add(new SignWizardModel.ProgressPanel(4), "South");
    }

    public void registerChange(ChangeListener paramChangeListener)
    {
      this.encryptionCheck.addChangeListener(paramChangeListener);
    }

    public void registerNextKeyListeners(KeyListener paramKeyListener)
    {
      this.userPassword.addKeyListener(paramKeyListener);
    }

    public boolean canAdvance()
    {
      return this.canAdvance;
    }

    public void collectData()
    {
      SignWizardModel.this.signData.setFlatten(this.flatten.isSelected());
      SignWizardModel.this.signData.setEncrypt(this.encryptionCheck.isSelected());
      if (this.encryptionCheck.isSelected())
      {
        SignWizardModel.this.signData.setEncryptUserPass(this.userPassword.getPassword());
        SignWizardModel.this.signData.setEncryptOwnerPass(this.ownerPassword.getPassword());
        int i = 0;
        if (this.allowPrinting.isSelected())
          i |= -1;
        if (this.allowModifyContent.isSelected())
          i |= -1;
        if (this.allowCopy.isSelected())
          i |= -1;
        if (this.allowModifyAnnotation.isSelected())
          i |= -1;
        if (this.allowFillIn.isSelected())
          i |= -1;
        if (this.allowScreenReader.isSelected())
          i |= -1;
        if (this.allowAssembly.isSelected())
          i |= -1;
        if (this.allowDegradedPrinting.isSelected())
          i |= -1;
        SignWizardModel.this.signData.setEncryptPermissions(i);
      }
    }
  }

  private class CommonPanel extends JPanel
  {
    private JLabel reasonLabel = new JLabel();
    private JTextField signerReasonArea = new JTextField();
    private JLabel locationLabel = new JLabel();
    private JTextField signerLocationField = new JTextField();
    private JLabel outputFileLabel = new JLabel();
    private JLabel currentOutputFilePath = new JLabel();
    private JButton browseOutputButton = new JButton();
    private volatile boolean canAdvance = false;

    public CommonPanel()
    {
      try
      {
        init();
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
      }
    }

    private void init()
    {
      setLayout(new BorderLayout());
      add(new SignWizardModel.TitlePanel(Messages.getMessage("PdfSigner.ReasonAndLocation")), "North");
      JPanel localJPanel = new JPanel(new GridBagLayout());
      localJPanel.setBorder(BorderFactory.createEtchedBorder(1));
      GridBagConstraints localGridBagConstraints = new GridBagConstraints();
      this.reasonLabel.setText(Messages.getMessage("PdfSigner.Reason") + ':');
      this.reasonLabel.setFont(new Font("Dialog", 1, 14));
      localGridBagConstraints.anchor = 23;
      localGridBagConstraints.fill = 2;
      localGridBagConstraints.gridx = (localGridBagConstraints.gridy = 0);
      localGridBagConstraints.insets = new Insets(10, 0, 0, 0);
      localJPanel.add(this.reasonLabel, localGridBagConstraints);
      localGridBagConstraints = new GridBagConstraints();
      localGridBagConstraints.fill = 2;
      localGridBagConstraints.gridx = 0;
      localGridBagConstraints.gridy = 1;
      localGridBagConstraints.gridwidth = 3;
      localGridBagConstraints.insets = new Insets(10, 0, 10, 0);
      this.signerReasonArea.setPreferredSize(new Dimension(200, 20));
      localJPanel.add(this.signerReasonArea, localGridBagConstraints);
      this.locationLabel.setText(Messages.getMessage("PdfSigner.Location") + ':');
      this.locationLabel.setFont(new Font("Dialog", 1, 14));
      localGridBagConstraints = new GridBagConstraints();
      localGridBagConstraints.fill = 2;
      localGridBagConstraints.gridx = 0;
      localGridBagConstraints.gridy = 2;
      localJPanel.add(this.locationLabel, localGridBagConstraints);
      localGridBagConstraints = new GridBagConstraints();
      localGridBagConstraints.fill = 2;
      localGridBagConstraints.gridx = 0;
      localGridBagConstraints.gridy = 3;
      localGridBagConstraints.insets = new Insets(10, 0, 0, 0);
      localGridBagConstraints.gridwidth = 3;
      this.signerLocationField.setPreferredSize(new Dimension(200, 20));
      localJPanel.add(this.signerLocationField, localGridBagConstraints);
      localGridBagConstraints = new GridBagConstraints();
      localGridBagConstraints.fill = 2;
      localGridBagConstraints.gridx = 0;
      localGridBagConstraints.gridy = 4;
      localGridBagConstraints.gridwidth = 3;
      localGridBagConstraints.insets = new Insets(10, 0, 0, 0);
      localJPanel.add(new JSeparator(0), localGridBagConstraints);
      this.outputFileLabel.setText(Messages.getMessage("PdfSigner.OutputFile"));
      this.outputFileLabel.setFont(new Font("Dialog", 1, 14));
      localGridBagConstraints = new GridBagConstraints();
      localGridBagConstraints.fill = 2;
      localGridBagConstraints.gridx = 0;
      localGridBagConstraints.gridy = 5;
      localGridBagConstraints.insets = new Insets(5, 10, 0, 0);
      localJPanel.add(this.outputFileLabel, localGridBagConstraints);
      this.currentOutputFilePath.setText(SignWizardModel.NO_FILE_SELECTED);
      localGridBagConstraints = new GridBagConstraints();
      localGridBagConstraints.fill = 2;
      localGridBagConstraints.gridx = 0;
      localGridBagConstraints.gridy = 6;
      localGridBagConstraints.insets = new Insets(10, 0, 0, 0);
      localGridBagConstraints.gridwidth = 3;
      this.currentOutputFilePath.setPreferredSize(new Dimension(100, 20));
      localJPanel.add(this.currentOutputFilePath, localGridBagConstraints);
      this.browseOutputButton.setText(Messages.getMessage("PdfViewerOption.Browse"));
      this.browseOutputButton.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent paramAnonymousActionEvent)
        {
          JFileChooser localJFileChooser = new JFileChooser(SignWizardModel.this.rootDir);
          int i = localJFileChooser.showSaveDialog(null);
          File localFile = localJFileChooser.getSelectedFile();
          if ((localFile != null) && (i == 0))
            if (localFile.exists())
            {
              JOptionPane.showMessageDialog(null, Messages.getMessage("PdfSigner.PleaseChooseAnotherFile"), Messages.getMessage("PdfViewerGeneralError.message"), 0);
              SignWizardModel.CommonPanel.this.canAdvance = false;
              SignWizardModel.CommonPanel.this.currentOutputFilePath.setText(SignWizardModel.NO_FILE_SELECTED);
              SignWizardModel.this.signData.setOutputFilePath(null);
            }
            else if (localFile.isDirectory())
            {
              JOptionPane.showMessageDialog(null, Messages.getMessage("PdfSigner.NoFileSelected"), Messages.getMessage("PdfViewerGeneralError.message"), 0);
              SignWizardModel.CommonPanel.this.canAdvance = false;
              SignWizardModel.CommonPanel.this.currentOutputFilePath.setText(SignWizardModel.NO_FILE_SELECTED);
            }
            else
            {
              SignWizardModel.CommonPanel.this.currentOutputFilePath.setText(localFile.getAbsolutePath());
              SignWizardModel.CommonPanel.this.canAdvance = true;
            }
        }
      });
      localGridBagConstraints = new GridBagConstraints();
      localGridBagConstraints.fill = 2;
      localGridBagConstraints.gridx = 2;
      localGridBagConstraints.gridy = 5;
      localGridBagConstraints.insets = new Insets(5, 25, 0, 25);
      localGridBagConstraints.anchor = 26;
      localJPanel.add(this.browseOutputButton, localGridBagConstraints);
      add(localJPanel, "Center");
      add(new SignWizardModel.ProgressPanel(5), "South");
    }

    public boolean canFinish()
    {
      return this.canAdvance;
    }

    public void registerChange(ChangeListener paramChangeListener)
    {
      this.browseOutputButton.addChangeListener(paramChangeListener);
    }

    public void collectData()
    {
      SignWizardModel.this.signData.setReason(this.signerReasonArea.getText());
      SignWizardModel.this.signData.setLocation(this.signerLocationField.getText());
      SignWizardModel.this.signData.setOutputFilePath(this.currentOutputFilePath.getText());
    }
  }

  private class KeystorePanel extends JPanel
  {
    private JLabel keyStoreLabel = new JLabel();
    private JLabel currentKeyStorePath = new JLabel(SignWizardModel.NO_FILE_SELECTED);
    private JButton browseKeyStoreButton = new JButton();
    private JLabel passwordKeyStoreLabel = new JLabel();
    private JPasswordField passwordKeyStoreField = new JPasswordField();
    private JCheckBox visiblePassKeyCheck = new JCheckBox();
    private JLabel aliasNameLabel = new JLabel();
    private JTextField aliasNameField = new JTextField();
    private JLabel aliasPasswordLabel = new JLabel();
    private JPasswordField aliasPasswordField = new JPasswordField();
    private JCheckBox visiblePassAliasCheck = new JCheckBox();
    private volatile boolean storeAdvance;
    private volatile boolean storePassAdvance;
    private volatile boolean aliasAdvance;
    private volatile boolean aliasPassAdvance = false;

    public KeystorePanel()
    {
      try
      {
        init();
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
      }
    }

    private void init()
    {
      setLayout(new BorderLayout());
      add(new SignWizardModel.TitlePanel(Messages.getMessage("PdfSigner.KeyStoreMode")), "North");
      JPanel localJPanel = new JPanel(new GridBagLayout());
      localJPanel.setBorder(BorderFactory.createEtchedBorder(1));
      GridBagConstraints localGridBagConstraints = new GridBagConstraints();
      this.keyStoreLabel.setText(Messages.getMessage("PdfSigner.SelectKeyStore"));
      this.keyStoreLabel.setFont(new Font("Dialog", 1, 14));
      localGridBagConstraints.anchor = 23;
      localGridBagConstraints.fill = 2;
      localGridBagConstraints.gridx = (localGridBagConstraints.gridy = 0);
      localGridBagConstraints.insets = new Insets(0, 10, 10, 0);
      localJPanel.add(this.keyStoreLabel, localGridBagConstraints);
      localGridBagConstraints = new GridBagConstraints();
      localGridBagConstraints.fill = 2;
      localGridBagConstraints.gridx = 0;
      localGridBagConstraints.gridy = 1;
      localGridBagConstraints.gridwidth = 3;
      localGridBagConstraints.insets = new Insets(0, 20, 0, 10);
      this.currentKeyStorePath.setPreferredSize(new Dimension(250, 20));
      localJPanel.add(this.currentKeyStorePath, localGridBagConstraints);
      this.browseKeyStoreButton.setText(Messages.getMessage("PdfViewerOption.Browse"));
      this.browseKeyStoreButton.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent paramAnonymousActionEvent)
        {
          JFileChooser localJFileChooser = new JFileChooser(SignWizardModel.this.rootDir);
          localJFileChooser.setFileHidingEnabled(false);
          int i = localJFileChooser.showOpenDialog(null);
          File localFile = localJFileChooser.getSelectedFile();
          if ((localFile != null) && (i == 0))
          {
            SignWizardModel.KeystorePanel.this.currentKeyStorePath.setText(localFile.getAbsolutePath());
            SignWizardModel.KeystorePanel.this.storeAdvance = true;
          }
        }
      });
      localGridBagConstraints = new GridBagConstraints();
      localGridBagConstraints.fill = 2;
      localGridBagConstraints.gridx = 1;
      localGridBagConstraints.gridy = 0;
      localGridBagConstraints.insets = new Insets(0, 30, 0, 0);
      localJPanel.add(this.browseKeyStoreButton, localGridBagConstraints);
      this.passwordKeyStoreLabel.setText(Messages.getMessage("PdfSigner.Password"));
      this.passwordKeyStoreLabel.setFont(new Font("Dialog", 1, 14));
      localGridBagConstraints = new GridBagConstraints();
      localGridBagConstraints.fill = 2;
      localGridBagConstraints.gridx = 0;
      localGridBagConstraints.gridy = 2;
      localGridBagConstraints.insets = new Insets(30, 10, 0, 10);
      localJPanel.add(this.passwordKeyStoreLabel, localGridBagConstraints);
      this.passwordKeyStoreField.addKeyListener(new KeyListener()
      {
        public void keyReleased(KeyEvent paramAnonymousKeyEvent)
        {
        }

        public void keyPressed(KeyEvent paramAnonymousKeyEvent)
        {
        }

        public void keyTyped(KeyEvent paramAnonymousKeyEvent)
        {
          SignWizardModel.KeystorePanel.this.storePassAdvance = true;
        }
      });
      localGridBagConstraints = new GridBagConstraints();
      localGridBagConstraints.fill = 2;
      localGridBagConstraints.gridx = 1;
      localGridBagConstraints.gridy = 2;
      localGridBagConstraints.gridwidth = 1;
      localGridBagConstraints.insets = new Insets(30, 10, 0, 10);
      localJPanel.add(this.passwordKeyStoreField, localGridBagConstraints);
      localGridBagConstraints = new GridBagConstraints();
      localGridBagConstraints.fill = 2;
      localGridBagConstraints.gridx = 2;
      localGridBagConstraints.gridy = 2;
      localGridBagConstraints.insets = new Insets(30, 0, 0, 0);
      this.visiblePassKeyCheck.setToolTipText(Messages.getMessage("PdfSigner.ShowPassword"));
      this.visiblePassKeyCheck.addActionListener(new ActionListener()
      {
        private char defaultChar;

        public void actionPerformed(ActionEvent paramAnonymousActionEvent)
        {
          if (SignWizardModel.KeystorePanel.this.visiblePassKeyCheck.isSelected())
          {
            this.defaultChar = SignWizardModel.KeystorePanel.this.passwordKeyStoreField.getEchoChar();
            SignWizardModel.KeystorePanel.this.passwordKeyStoreField.setEchoChar('\000');
          }
          else
          {
            SignWizardModel.KeystorePanel.this.passwordKeyStoreField.setEchoChar(this.defaultChar);
          }
        }
      });
      localJPanel.add(this.visiblePassKeyCheck, localGridBagConstraints);
      localGridBagConstraints = new GridBagConstraints();
      localGridBagConstraints.fill = 2;
      localGridBagConstraints.gridx = 0;
      localGridBagConstraints.gridy = 4;
      localGridBagConstraints.gridwidth = 4;
      localGridBagConstraints.insets = new Insets(10, 0, 10, 0);
      localJPanel.add(new JSeparator(0), localGridBagConstraints);
      this.aliasNameLabel.setText(Messages.getMessage("PdfSigner.AliasName"));
      this.aliasNameLabel.setFont(new Font("Dialog", 1, 14));
      localGridBagConstraints = new GridBagConstraints();
      localGridBagConstraints.fill = 2;
      localGridBagConstraints.gridx = 0;
      localGridBagConstraints.gridy = 5;
      localGridBagConstraints.insets = new Insets(0, 10, 10, 0);
      localJPanel.add(this.aliasNameLabel, localGridBagConstraints);
      this.aliasNameField.addKeyListener(new KeyListener()
      {
        public void keyReleased(KeyEvent paramAnonymousKeyEvent)
        {
        }

        public void keyPressed(KeyEvent paramAnonymousKeyEvent)
        {
        }

        public void keyTyped(KeyEvent paramAnonymousKeyEvent)
        {
          SignWizardModel.KeystorePanel.this.aliasAdvance = true;
        }
      });
      localGridBagConstraints = new GridBagConstraints();
      localGridBagConstraints.fill = 2;
      localGridBagConstraints.gridx = 1;
      localGridBagConstraints.gridy = 5;
      localGridBagConstraints.gridwidth = 2;
      localGridBagConstraints.insets = new Insets(0, 10, 0, 10);
      this.aliasNameField.setPreferredSize(new Dimension(150, 20));
      localJPanel.add(this.aliasNameField, localGridBagConstraints);
      this.aliasPasswordLabel.setText(Messages.getMessage("PdfSigner.AliasPassword"));
      this.aliasPasswordLabel.setFont(new Font("Dialog", 1, 14));
      localGridBagConstraints = new GridBagConstraints();
      localGridBagConstraints.fill = 2;
      localGridBagConstraints.gridx = 0;
      localGridBagConstraints.gridy = 7;
      localGridBagConstraints.insets = new Insets(10, 10, 0, 10);
      localJPanel.add(this.aliasPasswordLabel, localGridBagConstraints);
      this.aliasPasswordField.addKeyListener(new KeyListener()
      {
        public void keyReleased(KeyEvent paramAnonymousKeyEvent)
        {
        }

        public void keyPressed(KeyEvent paramAnonymousKeyEvent)
        {
        }

        public void keyTyped(KeyEvent paramAnonymousKeyEvent)
        {
          SignWizardModel.KeystorePanel.this.aliasPassAdvance = true;
        }
      });
      localGridBagConstraints = new GridBagConstraints();
      localGridBagConstraints.fill = 2;
      localGridBagConstraints.gridx = 1;
      localGridBagConstraints.gridy = 7;
      localGridBagConstraints.insets = new Insets(0, 10, 0, 10);
      localGridBagConstraints.anchor = 20;
      this.aliasPasswordField.setPreferredSize(new Dimension(100, 20));
      localJPanel.add(this.aliasPasswordField, localGridBagConstraints);
      localGridBagConstraints = new GridBagConstraints();
      localGridBagConstraints.fill = 2;
      localGridBagConstraints.gridx = 2;
      localGridBagConstraints.gridy = 7;
      localGridBagConstraints.insets = new Insets(10, 0, 0, 0);
      this.visiblePassAliasCheck.setToolTipText(Messages.getMessage("PdfSigner.ShowPassword"));
      this.visiblePassAliasCheck.addActionListener(new ActionListener()
      {
        private char defaultChar;

        public void actionPerformed(ActionEvent paramAnonymousActionEvent)
        {
          if (SignWizardModel.KeystorePanel.this.visiblePassAliasCheck.isSelected())
          {
            this.defaultChar = SignWizardModel.KeystorePanel.this.aliasPasswordField.getEchoChar();
            SignWizardModel.KeystorePanel.this.aliasPasswordField.setEchoChar('\000');
          }
          else
          {
            SignWizardModel.KeystorePanel.this.aliasPasswordField.setEchoChar(this.defaultChar);
          }
        }
      });
      localJPanel.add(this.visiblePassAliasCheck, localGridBagConstraints);
      add(localJPanel, "Center");
      add(new SignWizardModel.ProgressPanel(2), "South");
    }

    public void registerChange(ChangeListener paramChangeListener)
    {
      this.browseKeyStoreButton.addChangeListener(paramChangeListener);
    }

    public void registerNextKeyListeners(KeyListener paramKeyListener)
    {
      this.passwordKeyStoreField.addKeyListener(paramKeyListener);
      this.aliasNameField.addKeyListener(paramKeyListener);
      this.aliasPasswordField.addKeyListener(paramKeyListener);
    }

    public boolean canAdvance()
    {
      return (this.storeAdvance) && (this.storePassAdvance) && (this.aliasAdvance) && (this.aliasPassAdvance);
    }

    public void collectData()
    {
      SignWizardModel.this.signData.setKeyStorePath(this.currentKeyStorePath.getText());
      SignWizardModel.this.signData.setKeystorePassword(this.passwordKeyStoreField.getPassword());
      SignWizardModel.this.signData.setAlias(this.aliasNameField.getText());
      SignWizardModel.this.signData.setAliasPassword(this.aliasPasswordField.getPassword());
    }
  }

  private class ModeSelect extends JPanel
  {
    private String selfString = Messages.getMessage("PdfSigner.HaveKeystore");
    private String otherString = Messages.getMessage("PdfSigner.HavePfx");
    private int y = 0;
    private JRadioButton selfButton = new JRadioButton(this.selfString);
    private String[] certifyOptions = { Messages.getMessage("PdfSigner.NotCertified"), Messages.getMessage("PdfSigner.NoChangesAllowed"), Messages.getMessage("PdfSigner.FormFilling"), Messages.getMessage("PdfSigner.FormFillingAndAnnotations") };
    private JComboBox certifyCombo = new JComboBox(this.certifyOptions);
    private int certifyMode = -1;

    public ModeSelect()
    {
      if (!SignWizardModel.this.signData.isAppendMode())
      {
        this.certifyCombo = new JComboBox(this.certifyOptions);
      }
      else
      {
        localObject = new String[] { "Not Allowed..." };
        this.certifyCombo = new JComboBox((Object[])localObject);
      }
      setLayout(new BorderLayout());
      add(new SignWizardModel.TitlePanel(Messages.getMessage("PdfSigner.SelectSigningMode")), "North");
      Object localObject = new JPanel();
      ((JPanel)localObject).setLayout(new GridBagLayout());
      GridBagConstraints localGridBagConstraints = new GridBagConstraints();
      this.selfButton.setActionCommand(this.selfString);
      localGridBagConstraints.gridx = 0;
      localGridBagConstraints.gridy = this.y;
      localGridBagConstraints.anchor = 23;
      localGridBagConstraints.fill = 2;
      localGridBagConstraints.insets = new Insets(10, 0, 20, 0);
      this.selfButton.setFont(new Font("Dialog", 1, 12));
      ((JPanel)localObject).add(this.selfButton, localGridBagConstraints);
      JRadioButton localJRadioButton = new JRadioButton(this.otherString);
      localJRadioButton.setActionCommand(this.otherString);
      localJRadioButton.setSelected(true);
      SignWizardModel.this.signData.setSignMode(false);
      localGridBagConstraints = new GridBagConstraints();
      localGridBagConstraints.gridx = 0;
      localGridBagConstraints.gridy = (++this.y);
      localGridBagConstraints.fill = 2;
      localJRadioButton.setFont(new Font("Dialog", 1, 12));
      ((JPanel)localObject).add(localJRadioButton, localGridBagConstraints);
      localGridBagConstraints = new GridBagConstraints();
      localGridBagConstraints.gridx = 0;
      localGridBagConstraints.gridy = (++this.y);
      localGridBagConstraints.fill = 2;
      localGridBagConstraints.insets = new Insets(30, 0, 30, 0);
      ((JPanel)localObject).add(new JSeparator(0), localGridBagConstraints);
      JLabel localJLabel = new JLabel(Messages.getMessage("PdfSigner.CertificationAuthor"));
      localJLabel.setFont(new Font("Dialog", 1, 12));
      localGridBagConstraints = new GridBagConstraints();
      localGridBagConstraints.gridx = 0;
      localGridBagConstraints.gridy = (++this.y);
      localGridBagConstraints.fill = 10;
      ((JPanel)localObject).add(localJLabel, localGridBagConstraints);
      localGridBagConstraints = new GridBagConstraints();
      localGridBagConstraints.fill = 2;
      localGridBagConstraints.gridx = 0;
      localGridBagConstraints.gridy = (++this.y);
      localGridBagConstraints.insets = new Insets(10, 0, 0, 0);
      localGridBagConstraints.anchor = 20;
      this.certifyCombo.setEnabled(!SignWizardModel.this.isPdfSigned());
      this.certifyCombo.setSelectedIndex(0);
      this.certifyCombo.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent paramAnonymousActionEvent)
        {
          String str = (String)SignWizardModel.ModeSelect.this.certifyCombo.getSelectedItem();
          if (str.equals(Messages.getMessage("PdfSigner.NotCertified")))
            SignWizardModel.ModeSelect.this.certifyMode = -1;
          else if (str.equals(Messages.getMessage("PdfSigner.NoChangesAllowed")))
            SignWizardModel.ModeSelect.this.certifyMode = -1;
          else if (str.equals(Messages.getMessage("PdfSigner.FormFilling")))
            SignWizardModel.ModeSelect.this.certifyMode = -1;
          else if (str.equals(Messages.getMessage("PdfSigner.FormFillingAndAnnotations")))
            SignWizardModel.ModeSelect.this.certifyMode = -1;
          else
            throw new NullPointerException("The certifyCombo box is sending a string that is not recognised.");
        }
      });
      ((JPanel)localObject).add(this.certifyCombo, localGridBagConstraints);
      if (SignWizardModel.this.isPdfSigned())
        this.certifyCombo.setToolTipText(Messages.getMessage("PdfSigner.NotPermittedOnSigned"));
      add((Component)localObject, "Center");
      ButtonGroup localButtonGroup = new ButtonGroup();
      localButtonGroup.add(this.selfButton);
      localButtonGroup.add(localJRadioButton);
      add(new SignWizardModel.ProgressPanel(1), "South");
    }

    public void collectData()
    {
      SignWizardModel.this.signData.setSignMode(this.selfButton.isSelected());
      SignWizardModel.this.signData.setCertifyMode(this.certifyMode);
    }
  }

  private class PFXPanel extends JPanel
  {
    private JLabel keyFileLabel = new JLabel();
    private JButton browseKeyButton = new JButton();
    private JLabel currentKeyFilePath = new JLabel(SignWizardModel.NO_FILE_SELECTED);
    private JCheckBox visiblePassCheck = new JCheckBox();
    private JLabel passwordLabel = new JLabel();
    private JPasswordField passwordField = new JPasswordField();
    private volatile boolean keyNext = false;
    private volatile boolean passNext = false;
    private int y = 0;

    public PFXPanel()
    {
      try
      {
        init();
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
      }
    }

    private void init()
      throws Exception
    {
      setLayout(new BorderLayout());
      add(new SignWizardModel.TitlePanel(Messages.getMessage("PdfSigner.PfxSignMode")), "North");
      JPanel localJPanel = new JPanel(new GridBagLayout());
      localJPanel.setBorder(BorderFactory.createEtchedBorder(1));
      GridBagConstraints localGridBagConstraints = new GridBagConstraints();
      this.keyFileLabel.setText(Messages.getMessage("PdfSigner.KeyFile"));
      this.keyFileLabel.setFont(new Font("Dialog", 1, 14));
      localGridBagConstraints.anchor = 23;
      localGridBagConstraints.fill = 2;
      localGridBagConstraints.gridx = (localGridBagConstraints.gridy = 0);
      localGridBagConstraints.insets = new Insets(0, 10, 10, 0);
      localJPanel.add(this.keyFileLabel, localGridBagConstraints);
      localGridBagConstraints = new GridBagConstraints();
      localGridBagConstraints.fill = 2;
      localGridBagConstraints.gridx = 0;
      localGridBagConstraints.gridy = (++this.y);
      localGridBagConstraints.gridwidth = 3;
      this.currentKeyFilePath.setPreferredSize(new Dimension(250, 20));
      localGridBagConstraints.insets = new Insets(10, 10, 10, 10);
      localJPanel.add(this.currentKeyFilePath, localGridBagConstraints);
      this.browseKeyButton.setText(Messages.getMessage("PdfViewerOption.Browse"));
      this.browseKeyButton.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent paramAnonymousActionEvent)
        {
          JFileChooser localJFileChooser = new JFileChooser(SignWizardModel.this.rootDir);
          String[] arrayOfString = { "pfx" };
          localJFileChooser.addChoosableFileFilter(new FileFilterer(arrayOfString, "Key (pfx)"));
          int i = localJFileChooser.showOpenDialog(null);
          File localFile = localJFileChooser.getSelectedFile();
          if ((localFile != null) && (i == 0))
          {
            SignWizardModel.PFXPanel.this.currentKeyFilePath.setText(localFile.getAbsolutePath());
            SignWizardModel.PFXPanel.this.keyNext = true;
          }
        }
      });
      localGridBagConstraints = new GridBagConstraints();
      localGridBagConstraints.fill = 2;
      localGridBagConstraints.gridx = 2;
      localGridBagConstraints.gridy = 0;
      localGridBagConstraints.insets = new Insets(0, 25, 0, 10);
      localJPanel.add(this.browseKeyButton, localGridBagConstraints);
      this.passwordLabel.setText(Messages.getMessage("PdfSigner.Password"));
      this.passwordLabel.setFont(new Font("Dialog", 1, 14));
      localGridBagConstraints.fill = 2;
      localGridBagConstraints.gridx = 0;
      localGridBagConstraints.gridy = (++this.y);
      localGridBagConstraints.insets = new Insets(20, 10, 10, 10);
      localJPanel.add(this.passwordLabel, localGridBagConstraints);
      this.passwordField.addKeyListener(new KeyListener()
      {
        public void keyReleased(KeyEvent paramAnonymousKeyEvent)
        {
        }

        public void keyPressed(KeyEvent paramAnonymousKeyEvent)
        {
          SignWizardModel.PFXPanel.this.passNext = true;
        }

        public void keyTyped(KeyEvent paramAnonymousKeyEvent)
        {
        }
      });
      localGridBagConstraints = new GridBagConstraints();
      localGridBagConstraints.fill = 2;
      localGridBagConstraints.gridx = 1;
      localGridBagConstraints.gridy = this.y;
      localGridBagConstraints.gridwidth = 1;
      localGridBagConstraints.insets = new Insets(20, 10, 0, 10);
      this.passwordField.setPreferredSize(new Dimension(100, 20));
      localJPanel.add(this.passwordField, localGridBagConstraints);
      localGridBagConstraints = new GridBagConstraints();
      localGridBagConstraints.fill = 2;
      localGridBagConstraints.gridx = 2;
      localGridBagConstraints.gridy = this.y;
      localGridBagConstraints.insets = new Insets(20, 0, 0, 0);
      this.visiblePassCheck.setToolTipText(Messages.getMessage("PdfSigner.ShowPassword"));
      this.visiblePassCheck.addActionListener(new ActionListener()
      {
        private char defaultChar;

        public void actionPerformed(ActionEvent paramAnonymousActionEvent)
        {
          if (SignWizardModel.PFXPanel.this.visiblePassCheck.isSelected())
          {
            this.defaultChar = SignWizardModel.PFXPanel.this.passwordField.getEchoChar();
            SignWizardModel.PFXPanel.this.passwordField.setEchoChar('\000');
          }
          else
          {
            SignWizardModel.PFXPanel.this.passwordField.setEchoChar(this.defaultChar);
          }
        }
      });
      localJPanel.add(this.visiblePassCheck, localGridBagConstraints);
      add(localJPanel, "Center");
      add(new SignWizardModel.ProgressPanel(2), "South");
    }

    public void registerChange(ChangeListener paramChangeListener)
    {
      this.browseKeyButton.addChangeListener(paramChangeListener);
    }

    public void registerListener(KeyListener paramKeyListener)
    {
      this.passwordField.addKeyListener(paramKeyListener);
    }

    public boolean canAdvance()
    {
      return (this.passNext) && (this.keyNext);
    }

    public void collectData()
    {
      SignWizardModel.this.signData.setKeyFilePassword(this.passwordField.getPassword());
      SignWizardModel.this.signData.setKeyFilePath(this.currentKeyFilePath.getText());
    }
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.gui.popups.SignWizardModel
 * JD-Core Version:    0.6.2
 */