package org.jpedal.objects.acroforms.actions;

import java.awt.Component;
import java.awt.Container;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;
import org.jpedal.PdfDecoder;
import org.jpedal.PdfDecoderInt;
import org.jpedal.display.Display;
import org.jpedal.display.swing.SingleDisplay;
import org.jpedal.examples.viewer.Commands;
import org.jpedal.examples.viewer.Viewer;
import org.jpedal.examples.viewer.gui.SwingGUI;
import org.jpedal.io.ArrayDecoder;
import org.jpedal.io.ObjectStore;
import org.jpedal.io.PdfObjectReader;
import org.jpedal.objects.Javascript;
import org.jpedal.objects.PdfPageData;
import org.jpedal.objects.acroforms.AcroRenderer;
import org.jpedal.objects.acroforms.GUIData;
import org.jpedal.objects.acroforms.ReturnValues;
import org.jpedal.objects.acroforms.actions.privateclasses.FieldsHideObject;
import org.jpedal.objects.acroforms.gui.Summary;
import org.jpedal.objects.layers.PdfLayerList;
import org.jpedal.objects.raw.FormObject;
import org.jpedal.objects.raw.OutlineObject;
import org.jpedal.objects.raw.PdfArrayIterator;
import org.jpedal.objects.raw.PdfObject;
import org.jpedal.parser.DecoderOptions;
import org.jpedal.utils.BrowserLauncher;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.Messages;
import org.jpedal.utils.StringUtils;

public class DefaultActionHandler
  implements ActionHandler
{
  private static final boolean showMethods = false;
  private PdfObjectReader currentPdfFile;
  private Javascript javascript;
  private AcroRenderer acrorend;
  private PdfDecoderInt decode_pdf;
  Map Ccalled = new HashMap();

  public void init(PdfDecoderInt paramPdfDecoderInt, Javascript paramJavascript, AcroRenderer paramAcroRenderer)
  {
    if (paramPdfDecoderInt != null)
      this.currentPdfFile = paramPdfDecoderInt.getIO();
    this.javascript = paramJavascript;
    this.acrorend = paramAcroRenderer;
    this.decode_pdf = paramPdfDecoderInt;
  }

  public Object setHoverCursor()
  {
    return new MouseListener()
    {
      public void mouseEntered(MouseEvent paramAnonymousMouseEvent)
      {
        DefaultActionHandler.this.setCursor(4);
      }

      public void mouseExited(MouseEvent paramAnonymousMouseEvent)
      {
        DefaultActionHandler.this.setCursor(5);
      }

      public void mouseClicked(MouseEvent paramAnonymousMouseEvent)
      {
      }

      public void mousePressed(MouseEvent paramAnonymousMouseEvent)
      {
      }

      public void mouseReleased(MouseEvent paramAnonymousMouseEvent)
      {
      }
    };
  }

  public void A(Object paramObject, FormObject paramFormObject, int paramInt)
  {
    if (paramInt == 4)
    {
      this.javascript.execute(paramFormObject, 21, -1, ' ');
    }
    else if (paramInt == 5)
    {
      this.javascript.execute(paramFormObject, 40, -1, ' ');
    }
    else if (paramInt == 1)
    {
      this.javascript.execute(paramFormObject, 20, -1, ' ');
    }
    else if (paramInt == 2)
    {
      this.javascript.execute(paramFormObject, 17, -1, ' ');
      this.javascript.execute(paramFormObject, 37, -1, ' ');
    }
    PdfObject localPdfObject1 = null;
    if (paramInt == 2)
      localPdfObject1 = paramFormObject.getDictionary(17);
    if (localPdfObject1 == null)
    {
      localPdfObject1 = paramFormObject.getDictionary(4369);
      if (localPdfObject1 != null)
        if (paramInt == 4)
          localPdfObject1 = localPdfObject1.getDictionary(21);
        else if (paramInt == 5)
          localPdfObject1 = localPdfObject1.getDictionary(40);
        else if (paramInt == 1)
          localPdfObject1 = localPdfObject1.getDictionary(20);
        else if (paramInt == 2)
          localPdfObject1 = localPdfObject1.getDictionary(37);
    }
    setCursor(paramInt);
    gotoDest(paramFormObject, paramInt, 339034948);
    int i = paramFormObject.getParameterConstant(1147962727);
    int j = paramFormObject.getActionFlag();
    if (i == 2308407)
    {
      additionalAction_Signature(paramFormObject, paramInt);
    }
    else if ((paramInt == 3) && ((j == 1) || (i == 607471684)))
    {
      popup(paramObject, paramFormObject, this.currentPdfFile);
    }
    else
    {
      if (localPdfObject1 == null)
        return;
      int k = localPdfObject1.getNameAsConstant(35);
      if (k != -1)
      {
        if (k == 826094930)
          additionalAction_Named(paramInt, localPdfObject1);
        else if ((k == 390014015) || (k == 1059340089))
        {
          if (localPdfObject1 != null)
            gotoDest(localPdfObject1, paramInt, k);
        }
        else if (k == 1266841507)
          additionalAction_ResetForm(localPdfObject1);
        else if (k == 1216126662)
          additionalAction_SubmitForm(localPdfObject1);
        else if (k != -2006286978)
          if (k == 406402101)
          {
            additionalAction_Hide(localPdfObject1);
          }
          else if (k == 2433561)
          {
            additionalAction_URI(localPdfObject1.getTextStreamValue(2433561));
          }
          else if (k == 1161711465)
          {
            try
            {
              PdfObject localPdfObject2 = localPdfObject1.getDictionary(22);
              if (localPdfObject2 != null)
              {
                String str = localPdfObject2.getTextStreamValue(22);
                InputStream localInputStream = getClass().getResourceAsStream(new StringBuilder().append("/org/jpedal/res/").append(str).toString());
                if (localInputStream == null)
                {
                  JOptionPane.showMessageDialog(null, new StringBuilder().append("Unable to locate ").append(str).toString());
                }
                else
                {
                  int i1 = str.lastIndexOf(47);
                  if (i1 != -1)
                    str = str.substring(i1 + 1);
                  File localFile = new File(new StringBuilder().append(ObjectStore.temp_dir).append(str).toString());
                  localFile.deleteOnExit();
                  ObjectStore.copy(new BufferedInputStream(localInputStream), new BufferedOutputStream(new FileOutputStream(localFile)));
                  if (str.endsWith(".pdf"))
                  {
                    try
                    {
                      Viewer localViewer = new Viewer(0);
                      Viewer.exitOnClose = false;
                      localViewer.setupViewer();
                      localViewer.openDefaultFile(new StringBuilder().append(ObjectStore.temp_dir).append(str).toString());
                    }
                    catch (Exception localException3)
                    {
                      if (LogWriter.isOutput())
                        LogWriter.writeLog(new StringBuilder().append("Exception: ").append(localException3.getMessage()).toString());
                    }
                  }
                  else if (DecoderOptions.isRunningOnMac)
                  {
                    str = new StringBuilder().append("open ").append(ObjectStore.temp_dir).append(str).toString();
                    Runtime.getRuntime().exec(str);
                  }
                }
              }
            }
            catch (Exception localException1)
            {
              if (LogWriter.isOutput())
                LogWriter.writeLog(new StringBuilder().append("Exception: ").append(localException1.getMessage()).toString());
            }
            catch (Error localError)
            {
              if (LogWriter.isOutput())
                LogWriter.writeLog(new StringBuilder().append("Error: ").append(localError.getMessage()).toString());
            }
            LogWriter.writeFormLog("{stream} launch activate action NOT IMPLEMENTED", false);
          }
          else if (k == 1667731612)
          {
            additionalAction_OCState(paramInt, localPdfObject1);
          }
          else if (k == 1061502534)
          {
            if ((paramInt == 3) || (paramInt == 2))
            {
              PdfObject localPdfObject3 = localPdfObject1.getDictionary(1061502534);
              this.currentPdfFile.checkResolved(localPdfObject3);
              try
              {
                int m = localPdfObject3.getInt(19);
                if (m == -1)
                  m = 1;
                int n = localPdfObject3.getInt(18);
                if (n == -1)
                  n = 8;
                float f = localPdfObject3.getInt(34);
                int i2 = localPdfObject3.getNameAsConstant(21);
                if (i2 == -1)
                  i2 = 1551661165;
                SoundHandler.setAudioFormat(i2, n, f, m);
                SoundHandler.PlaySound(localPdfObject3.getDecodedStream());
              }
              catch (Exception localException2)
              {
                if (LogWriter.isOutput())
                  LogWriter.writeLog(new StringBuilder().append("Exception: ").append(localException2.getMessage()).toString());
              }
            }
          }
          else
          {
            LogWriter.writeFormLog(new StringBuilder().append("{stream} UNKNOWN Command ").append(localPdfObject1.getName(35)).append(" Action").toString(), false);
          }
      }
      else if (k != -1)
        LogWriter.writeFormLog(new StringBuilder().append("{stream} Activate Action UNKNOWN command ").append(localPdfObject1.getName(35)).append(' ').append(paramFormObject.getObjectRefAsString()).toString(), false);
    }
  }

  private void additionalAction_OCState(int paramInt, PdfObject paramPdfObject)
  {
    if (paramInt == 3)
    {
      PdfArrayIterator localPdfArrayIterator = paramPdfObject.getMixedArray(1144079448);
      if ((localPdfArrayIterator != null) && (localPdfArrayIterator.getTokenCount() > 0))
      {
        final PdfLayerList localPdfLayerList = (PdfLayerList)this.decode_pdf.getJPedalObject(826881374);
        int i = localPdfArrayIterator.getTokenCount();
        final int j = localPdfArrayIterator.getNextValueAsConstant(true);
        for (int k = 1; k < i; k++)
        {
          String str1 = localPdfArrayIterator.getNextValueAsString(true);
          final String str2 = localPdfLayerList.getNameFromRef(str1);
          Runnable local2 = new Runnable()
          {
            public void run()
            {
              PdfDecoder localPdfDecoder = (PdfDecoder)DefaultActionHandler.this.decode_pdf;
              localPdfDecoder.invalidate();
              localPdfDecoder.validate();
              boolean bool;
              if (j == 926376052)
                bool = !localPdfLayerList.isVisible(str2);
              else
                bool = j != 2037270;
              localPdfLayerList.setVisiblity(str2, bool);
              try
              {
                DefaultActionHandler.this.decode_pdf.decodePage(-1);
              }
              catch (Exception localException)
              {
                if (LogWriter.isOutput())
                  LogWriter.writeLog("Exception: " + localException.getMessage());
              }
            }
          };
          SwingUtilities.invokeLater(local2);
        }
      }
    }
  }

  private void additionalAction_Named(int paramInt, PdfObject paramPdfObject)
  {
    int i = paramPdfObject.getNameAsConstant(30);
    if (i == 1111047780)
    {
      additionalAction_Print(paramInt);
    }
    else if (i == 1177891956)
    {
      additionalAction_SaveAs();
    }
    else if (i == 1046904697)
    {
      changeTo(null, this.decode_pdf.getlastPageDecoded() + 1, null, null, true);
    }
    else if (i == 1081306235)
    {
      changeTo(null, this.decode_pdf.getlastPageDecoded() - 1, null, null, true);
    }
    else if (i == 1500740239)
    {
      changeTo(null, 1, null, null, true);
    }
    else
    {
      Object localObject;
      if (i == 305220218)
      {
        localObject = (SwingGUI)this.decode_pdf.getExternalHandler(11);
        if (localObject != null)
          ((SwingGUI)localObject).currentCommands.executeCommand(702, null);
      }
      else if (i == 1013086841)
      {
        changeTo(null, this.decode_pdf.getPageCount(), null, null, true);
      }
      else
      {
        int j;
        if (i == 1060982398)
        {
          localObject = new JComboBox(new String[] { Messages.getMessage("PdfViewerScaleWindow.text"), Messages.getMessage("PdfViewerScaleHeight.text"), Messages.getMessage("PdfViewerScaleWidth.text"), "25", "50", "75", "100", "125", "150", "200", "250", "500", "750", "1000" });
          j = JOptionPane.showConfirmDialog(null, localObject, new StringBuilder().append(Messages.getMessage("PdfViewerToolbarScaling.text")).append(':').toString(), -1);
          if (j != -1)
          {
            int k = ((JComboBox)localObject).getSelectedIndex();
            if (k != -1)
            {
              SwingGUI localSwingGUI = (SwingGUI)this.decode_pdf.getExternalHandler(11);
              if (localSwingGUI != null)
              {
                localSwingGUI.setSelectedComboIndex(252, k);
                localSwingGUI.zoom();
              }
            }
          }
        }
        else if (i == 2121363126)
        {
          localObject = (SwingGUI)this.decode_pdf.getExternalHandler(11);
          if (localObject != null)
            ((SwingGUI)localObject).currentCommands.executeCommand(61, null);
        }
        else if (i == 286725562)
        {
          localObject = "http://www.adobe.com/devnet/acrobat/pdfs/Acro6JSGuide.pdf";
          j = JOptionPane.showConfirmDialog(null, new StringBuilder().append(Messages.getMessage("AcroForm_FormsJSGuide.urlQuestion")).append('\n').append((String)localObject).append(" ?\n\n").append(Messages.getMessage("AcroForm_FormsJSGuide.urlFail")).toString(), Messages.getMessage("AcroForm_FormsJSGuide.Title"), 0);
          if (j == 0)
          {
            Viewer localViewer = new Viewer(0);
            Viewer.exitOnClose = false;
            localViewer.setupViewer();
            localViewer.openDefaultFile((String)localObject);
          }
        }
      }
    }
  }

  private void additionalAction_SaveAs()
  {
    SwingGUI localSwingGUI = (SwingGUI)this.decode_pdf.getExternalHandler(11);
    if (localSwingGUI != null)
      localSwingGUI.currentCommands.executeCommand(500, null);
  }

  private void additionalAction_URI(String paramString)
  {
    try
    {
      BrowserLauncher.openURL(paramString);
    }
    catch (IOException localIOException)
    {
      showMessageDialog(Messages.getMessage("PdfViewer.ErrorWebsite"));
      if (LogWriter.isOutput())
        LogWriter.writeLog(new StringBuilder().append("Exception: ").append(localIOException.getMessage()).toString());
    }
  }

  private void additionalAction_Hide(PdfObject paramPdfObject)
  {
    FieldsHideObject localFieldsHideObject = new FieldsHideObject();
    getHideMap(paramPdfObject, localFieldsHideObject);
    setFieldVisibility(localFieldsHideObject);
  }

  private void additionalAction_SubmitForm(PdfObject paramPdfObject)
  {
    boolean bool = false;
    String str1 = null;
    String[] arrayOfString = null;
    PdfObject localPdfObject = paramPdfObject.getDictionary(22);
    if (localPdfObject != null)
      str1 = localPdfObject.getTextStreamValue(22);
    PdfArrayIterator localPdfArrayIterator = paramPdfObject.getMixedArray(893143676);
    int i;
    if (localPdfArrayIterator != null)
    {
      if (localPdfArrayIterator.getTokenCount() < 1)
        localPdfArrayIterator = null;
      if (localPdfArrayIterator != null)
      {
        i = 0;
        arrayOfString = new String[localPdfArrayIterator.getTokenCount()];
        String str4 = null;
        StringBuilder localStringBuilder = new StringBuilder();
        while (localPdfArrayIterator.hasMoreTokens())
        {
          String str2 = localPdfArrayIterator.getNextValueAsString(true);
          if (str2.contains(".x"))
            str4 = str2.substring(str2.indexOf(46) + 1, str2.indexOf(".x") + 1);
          if (str2.contains(" R"))
          {
            FormObject localFormObject = new FormObject(str2);
            this.currentPdfFile.readObject(localFormObject);
            String str3 = localFormObject.getTextStreamValue(36);
            if (str4 != null)
              localStringBuilder.append(str4);
            localStringBuilder.append(str3);
            localStringBuilder.append(',');
          }
        }
        arrayOfString[i] = localStringBuilder.toString();
      }
    }
    if (arrayOfString != null)
    {
      i = paramPdfObject.getInt(1009858393);
      if ((i & 0x1) == 1)
        bool = true;
    }
    submitURL(arrayOfString, bool, str1);
  }

  private void additionalAction_ResetForm(PdfObject paramPdfObject)
  {
    int i = paramPdfObject.getInt(1009858393);
    int j = 0;
    if ((i & 0x1) == 1)
      j = 1;
    Object[] arrayOfObject = this.acrorend.getFormComponents(null, ReturnValues.FORMOBJECTS_FROM_REF, -1);
    PdfArrayIterator localPdfArrayIterator = paramPdfObject.getMixedArray(893143676);
    String[] arrayOfString = null;
    int k;
    Object localObject1;
    if ((localPdfArrayIterator != null) && (localPdfArrayIterator.getTokenCount() > 0))
    {
      arrayOfString = new String[localPdfArrayIterator.getTokenCount()];
      for (k = 0; localPdfArrayIterator.hasMoreTokens(); k++)
      {
        localObject1 = localPdfArrayIterator.getNextValueAsString(true);
        arrayOfString[k] = localObject1;
      }
    }
    String str3;
    Object localObject2;
    if (j != 0)
    {
      if ((arrayOfString != null) && (arrayOfString.length > 0))
        for (k = 0; k < arrayOfObject.length; k++)
        {
          localObject1 = (FormObject)arrayOfObject[k];
          int i1 = 0;
          for (int i2 = 0; i2 < arrayOfString.length; i2++)
          {
            str3 = arrayOfString[i2];
            if ((str3 != null) && (((FormObject)localObject1).getTextStreamValue(36).equals(str3)))
            {
              i1 = 1;
              break;
            }
          }
          if (i1 == 0)
          {
            String str1 = ((FormObject)localObject1).getTextStreamValue(5158);
            ((FormObject)localObject1).updateValue(str1, false, true);
          }
        }
      else
        for (k = 0; k < arrayOfObject.length; k++)
        {
          localObject1 = (FormObject)arrayOfObject[k];
          localObject2 = ((FormObject)localObject1).getTextStreamValue(5158);
          ((FormObject)localObject1).updateValue(localObject2, false, true);
        }
    }
    else if ((arrayOfString != null) && (arrayOfString.length > 0))
    {
      ArrayList localArrayList = new ArrayList();
      for (int n = 0; n < arrayOfObject.length; n++)
      {
        localObject2 = (FormObject)arrayOfObject[n];
        for (int i3 = 0; i3 < arrayOfString.length; i3++)
        {
          str3 = arrayOfString[i3];
          if ((str3 != null) && (((FormObject)localObject2).getTextStreamValue(36).equals(str3)))
            localArrayList.add(localObject2);
        }
      }
      for (n = 0; n < localArrayList.size(); n++)
      {
        localObject2 = (FormObject)localArrayList.get(n);
        String str2 = ((FormObject)localObject2).getTextStreamValue(5158);
        ((FormObject)localObject2).updateValue(str2, false, true);
      }
    }
    else
    {
      for (int m = 0; m < arrayOfObject.length; m++)
      {
        FormObject localFormObject = (FormObject)arrayOfObject[m];
        localObject2 = localFormObject.getTextStreamValue(5158);
        localFormObject.updateValue(localObject2, false, true);
      }
    }
  }

  public int gotoDest(PdfObject paramPdfObject, int paramInt1, int paramInt2)
  {
    PdfObject localPdfObject = paramPdfObject.getDictionary(17);
    if (localPdfObject != null)
      paramPdfObject = localPdfObject;
    int i = -1;
    PdfArrayIterator localPdfArrayIterator = paramPdfObject.getMixedArray(339034948);
    if (localPdfArrayIterator != null)
      if (paramInt1 == 3)
      {
        Object localObject1;
        if (localPdfArrayIterator.getTokenCount() == 1)
        {
          str1 = this.currentPdfFile.convertNameToRef(localPdfArrayIterator.getNextValueAsString(false));
          if (str1 != null)
            if (str1.charAt(0) == '[')
            {
              localObject1 = StringUtils.toBytes(str1);
              localObject1[0] = 0;
              ArrayDecoder localArrayDecoder = new ArrayDecoder(this.currentPdfFile.getObjectReader(), 0, localObject1.length, 18, null, 826094945);
              localArrayDecoder.readArray(false, (byte[])localObject1, paramPdfObject, 339034948);
              localPdfArrayIterator = paramPdfObject.getMixedArray(339034948);
            }
            else
            {
              paramPdfObject = new OutlineObject(str1);
              this.currentPdfFile.readObject(paramPdfObject);
              localPdfArrayIterator = paramPdfObject.getMixedArray(339034948);
            }
        }
        String str1 = paramPdfObject.getTextStreamValue(22);
        if (str1 == null)
        {
          localObject1 = paramPdfObject.getDictionary(22);
          if (localObject1 != null)
            str1 = ((PdfObject)localObject1).getTextStreamValue(22);
        }
        if ((str1 != null) && (str1.indexOf(47) == -1) && (str1.indexOf(92) == -1))
          str1 = new StringBuilder().append(this.decode_pdf.getObjectStore().getCurrentFilepath()).append(str1).toString();
        int k;
        if (str1 != null)
        {
          for (int j = str1.indexOf(92); j != -1; j = str1.indexOf(92))
            str1 = new StringBuilder().append(str1.substring(0, j)).append('/').append(str1.substring(j + "\\".length(), str1.length())).toString();
          k = str1.indexOf(":/");
          if (((k == -1) || (k > 1)) && (!str1.startsWith("/")))
          {
            File localFile = new File(this.decode_pdf.getFileName());
            str1 = new StringBuilder().append(localFile.getParent()).append('/').append(str1).toString();
          }
          j = str1.indexOf("/../");
          if (j != -1)
          {
            for (int m = j - 1; (m > 0) && (str1.charAt(m) != '/') && (m != 0); m--);
            if (m > 0)
              str1 = new StringBuilder().append(str1.substring(0, m)).append(str1.substring(j + 3, str1.length())).toString();
          }
        }
        String str2 = "";
        Object localObject2;
        if (localPdfArrayIterator.getTokenCount() > 0)
        {
          if (localPdfArrayIterator.isNextValueNull())
            return -1;
          k = localPdfArrayIterator.getNextValueAsInteger(false) + 1;
          str2 = localPdfArrayIterator.getNextValueAsString(true);
          if (str2.endsWith(" R"))
            i = this.decode_pdf.getPageFromObjectRef(str2);
          else if (k > 0)
            i = k;
          if (i == -1)
          {
            localObject2 = this.currentPdfFile.convertNameToRef(str2);
            if ((localObject2 != null) && (((String)localObject2).endsWith(" R")))
              i = this.decode_pdf.getPageFromObjectRef((String)localObject2);
          }
        }
        if ((localPdfArrayIterator.getTokenCount() == 0) && (paramPdfObject.getNameAsConstant(35) == 1059340089))
          paramInt2 = 1059340089;
        switch (paramInt2)
        {
        case 339034948:
          if (localPdfArrayIterator.getTokenCount() > 1)
          {
            k = localPdfArrayIterator.getNextValueAsConstant(true);
            localObject2 = null;
            Rectangle localRectangle = null;
            switch (k)
            {
            case 2631978:
              float f1 = localPdfArrayIterator.getNextValueAsFloat();
              float f2 = localPdfArrayIterator.getNextValueAsFloat();
              localRectangle = new Rectangle((int)f1, (int)f2, 10, 10);
              break;
            case 1456452:
              localObject2 = Integer.valueOf(-3);
              break;
            case 372851730:
              localObject2 = Integer.valueOf(-3);
              break;
            case 372851736:
              localObject2 = Integer.valueOf(-1);
              float f3 = localPdfArrayIterator.getNextValueAsFloat();
              localRectangle = new Rectangle(10, (int)f3, 10, 10);
              break;
            }
            changeTo(str1, i, localRectangle, (Integer)localObject2, true);
          }
          break;
        case 390014015:
          if (i != -1)
            changeTo(null, i, null, null, true);
          break;
        case 1059340089:
          k = str2.indexOf("P.");
          if (k != -1)
          {
            str2 = str2.substring(k + 2, str2.length());
            i = Integer.parseInt(str2);
          }
          else if (str2.equals("F"))
          {
            i = 1;
          }
          else
          {
            i = 1;
          }
          if (new File(str1).exists())
          {
            if (i != -1)
              changeTo(str1, i, null, null, true);
            LogWriter.writeFormLog("{DefaultActionHamdler.A} Form has GoToR command, needs methods for opening new file on page specified", false);
          }
          else
          {
            showMessageDialog(new StringBuilder().append("The file specified ").append(str1).append(" Does Not Exist!").toString());
          }
          break;
        }
      }
      else
      {
        setCursor(paramInt1);
      }
    return i;
  }

  private void additionalAction_Print(int paramInt)
  {
    if (paramInt == 2)
      print();
  }

  private void additionalAction_Signature(FormObject paramFormObject, int paramInt)
  {
    if (paramInt == 3)
    {
      PdfObject localPdfObject = paramFormObject.getDictionary(38);
      if (localPdfObject == null)
        return;
      showSig(localPdfObject);
    }
    else
    {
      setCursor(paramInt);
    }
  }

  public void changeTo(String paramString, int paramInt, Object paramObject, Integer paramInteger, boolean paramBoolean)
  {
    if (paramString != null)
      try
      {
        SwingGUI localSwingGUI = (SwingGUI)this.decode_pdf.getExternalHandler(11);
        if (localSwingGUI != null)
          localSwingGUI.stopThumbnails();
        if ((paramString.startsWith("http://")) || (paramString.startsWith("ftp://")) || (paramString.startsWith("https:")))
        {
          if (localSwingGUI != null)
            localSwingGUI.currentCommands.executeCommand(14, new Object[] { paramString });
          else
            this.decode_pdf.openPdfFileFromURL(paramString, true);
        }
        else if (localSwingGUI != null)
          localSwingGUI.currentCommands.executeCommand(10, new Object[] { paramString });
        else
          this.decode_pdf.openPdfFile(paramString);
        if (paramInt == -1)
          paramInt = 1;
      }
      catch (Exception localException1)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog(new StringBuilder().append("Exception: ").append(localException1.getMessage()).toString());
      }
    if ((paramInt != -1) && (this.decode_pdf.getPageCount() != 1) && (this.decode_pdf.getlastPageDecoded() != paramInt) && (paramInt > 0) && (paramInt < this.decode_pdf.getPageCount() + 1))
    {
      try
      {
        PdfDecoder localPdfDecoder = (PdfDecoder)this.decode_pdf;
        if ((localPdfDecoder.getDisplayView() == 2) || (localPdfDecoder.getDisplayView() == 4))
        {
          localObject2 = localPdfDecoder.getPages();
          localPdfDecoder.scrollRectToVisible(new Rectangle(((Display)localObject2).getXCordForPage(paramInt), ((Display)localObject2).getYCordForPage(paramInt), localPdfDecoder.getPdfPageData().getScaledCropBoxWidth(paramInt), localPdfDecoder.getPdfPageData().getScaledCropBoxHeight(paramInt)));
        }
        this.decode_pdf.decodePage(paramInt);
        if (paramInt != -1)
          localPdfDecoder.updatePageNumberDisplayed(paramInt);
      }
      catch (Exception localException2)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog(new StringBuilder().append("Exception: ").append(localException2.getMessage()).toString());
      }
      this.decode_pdf.setPageParameters(-1.0F, paramInt);
    }
    if (paramInteger != null)
    {
      localObject1 = this.decode_pdf.getExternalHandler(11);
      if (localObject1 != null)
        if (paramInteger.intValue() < 0)
          ((SwingGUI)localObject1).setSelectedComboIndex(252, paramInteger.intValue() + 3);
        else
          ((SwingGUI)localObject1).setSelectedComboItem(252, paramInteger.toString());
    }
    Object localObject1 = (PdfDecoder)this.decode_pdf;
    if (paramObject != null)
    {
      localObject2 = (SingleDisplay)((PdfDecoder)localObject1).getExternalHandler(16);
      double d1 = ((PdfDecoder)localObject1).getScaling();
      double d2 = (((PdfDecoder)localObject1).getPdfPageData().getMediaBoxWidth(paramInt) - ((Rectangle)paramObject).getX()) * d1 + ((Display)localObject2).getXCordForPage(paramInt);
      double d3 = (((PdfDecoder)localObject1).getPdfPageData().getCropBoxHeight(paramInt) - ((Rectangle)paramObject).getY()) * d1 + ((Display)localObject2).getYCordForPage(paramInt);
      paramObject = new Rectangle((int)d2, (int)d3, (int)((PdfDecoder)localObject1).getVisibleRect().getWidth(), (int)((PdfDecoder)localObject1).getVisibleRect().getHeight());
      ((PdfDecoder)localObject1).scrollRectToVisible((Rectangle)paramObject);
    }
    Object localObject2 = (SwingGUI)((PdfDecoder)localObject1).getExternalHandler(11);
    if (localObject2 != null)
    {
      ((SwingGUI)localObject2).zoom();
      if (paramBoolean)
        ((SwingGUI)localObject2).currentCommands.executeCommand(700, new Object[] { Integer.valueOf(paramInt), paramObject, paramInteger });
    }
    ((PdfDecoder)localObject1).revalidate();
    ((PdfDecoder)localObject1).repaint();
  }

  public PdfDecoderInt getPDFDecoder()
  {
    return this.decode_pdf;
  }

  public void E(Object paramObject, FormObject paramFormObject)
  {
    this.javascript.execute(paramFormObject, 21, 6, ' ');
  }

  public void X(Object paramObject, FormObject paramFormObject)
  {
    this.javascript.execute(paramFormObject, 40, 6, ' ');
  }

  public void D(Object paramObject, FormObject paramFormObject)
  {
    this.javascript.execute(paramFormObject, 20, 6, ' ');
  }

  public void U(Object paramObject, FormObject paramFormObject)
  {
    this.javascript.execute(paramFormObject, 37, 6, ' ');
  }

  public void Fo(Object paramObject, FormObject paramFormObject)
  {
    this.javascript.execute(paramFormObject, 5695, 6, ' ');
  }

  public void Bl(Object paramObject, FormObject paramFormObject)
  {
    this.javascript.execute(paramFormObject, 4668, 6, ' ');
  }

  public void O(PdfObject paramPdfObject, int paramInt)
  {
    if (this.currentPdfFile == null)
      return;
    FormObject localFormObject1 = (FormObject)paramPdfObject.getDictionary(paramInt);
    this.currentPdfFile.checkResolved(localFormObject1);
    if (localFormObject1 != null)
    {
      FormObject localFormObject2 = (FormObject)localFormObject1.getDictionary(31);
      this.currentPdfFile.checkResolved(localFormObject2);
      if (localFormObject2 != null)
      {
        String str = localFormObject2.getTextStreamValue(6691);
        if (str == null)
        {
          PdfObject localPdfObject = localFormObject2.getDictionary(6691);
          if (localPdfObject != null)
            str = new String(localPdfObject.getDecodedStream());
        }
        this.javascript.executeAction(str);
      }
    }
  }

  public void PO(PdfObject paramPdfObject, int paramInt)
  {
    if (this.currentPdfFile == null)
      return;
    FormObject localFormObject1 = (FormObject)paramPdfObject.getDictionary(paramInt);
    this.currentPdfFile.checkResolved(localFormObject1);
    if (localFormObject1 != null)
    {
      FormObject localFormObject2 = (FormObject)localFormObject1.getDictionary(8223);
      this.currentPdfFile.checkResolved(localFormObject2);
      if (localFormObject2 != null)
      {
        String str = localFormObject2.getTextStreamValue(6691);
        this.javascript.executeAction(str);
      }
    }
  }

  public void PC(PdfObject paramPdfObject, int paramInt)
  {
    FormObject localFormObject1 = (FormObject)paramPdfObject.getDictionary(paramInt);
    this.currentPdfFile.checkResolved(localFormObject1);
    if (localFormObject1 != null)
    {
      FormObject localFormObject2 = (FormObject)localFormObject1.getDictionary(8211);
      this.currentPdfFile.checkResolved(localFormObject2);
      if (localFormObject2 != null)
      {
        String str = localFormObject2.getTextStreamValue(6691);
        this.javascript.executeAction(str);
      }
    }
  }

  public void PV(PdfObject paramPdfObject, int paramInt)
  {
    FormObject localFormObject1 = (FormObject)paramPdfObject.getDictionary(paramInt);
    this.currentPdfFile.checkResolved(localFormObject1);
    if (localFormObject1 != null)
    {
      FormObject localFormObject2 = (FormObject)localFormObject1.getDictionary(8230);
      this.currentPdfFile.checkResolved(localFormObject2);
      if (localFormObject2 != null)
      {
        String str = localFormObject2.getTextStreamValue(6691);
        this.javascript.executeAction(str);
      }
    }
  }

  public void PI(PdfObject paramPdfObject, int paramInt)
  {
    FormObject localFormObject1 = (FormObject)paramPdfObject.getDictionary(paramInt);
    this.currentPdfFile.checkResolved(localFormObject1);
    if (localFormObject1 != null)
    {
      FormObject localFormObject2 = (FormObject)localFormObject1.getDictionary(8217);
      this.currentPdfFile.checkResolved(localFormObject2);
      if (localFormObject2 != null)
      {
        String str = localFormObject2.getTextStreamValue(6691);
        this.javascript.executeAction(str);
      }
    }
  }

  public int K(Object paramObject, FormObject paramFormObject, int paramInt)
  {
    int i = this.javascript.execute(paramFormObject, 27, paramInt, getKeyPressed(paramObject));
    int j = paramFormObject.getTextSize();
    if ((this.acrorend.getCompData() != null) && ((j == 0) || (j == -1)))
      this.acrorend.getCompData().setAutoFontSize(paramFormObject);
    return i;
  }

  public void F(FormObject paramFormObject)
  {
    this.javascript.execute(paramFormObject, 22, 6, ' ');
  }

  public void V(Object paramObject, FormObject paramFormObject, int paramInt)
  {
    this.javascript.execute(paramFormObject, 38, paramInt, getKeyPressed(paramObject));
  }

  public void C(FormObject paramFormObject)
  {
    if (this.Ccalled.get(paramFormObject.getObjectRefAsString()) != null)
      return;
    this.Ccalled.put(paramFormObject.getObjectRefAsString(), "1");
    this.javascript.execute(paramFormObject, 4866, 6, ' ');
    this.Ccalled.remove(paramFormObject.getObjectRefAsString());
  }

  private static void getHideMap(PdfObject paramPdfObject, FieldsHideObject paramFieldsHideObject)
  {
    Object localObject1 = paramFieldsHideObject.getFieldArray();
    Object localObject2 = paramFieldsHideObject.getHideArray();
    Object localObject3;
    if (paramPdfObject.getTextStreamValue(36) != null)
    {
      String str = paramPdfObject.getTextStreamValue(36);
      if (str != null)
      {
        if (localObject1.length > 0)
        {
          localObject3 = new String[localObject1.length + 1];
          System.arraycopy(localObject1, 0, localObject3, 0, localObject1.length);
          localObject3[(localObject3.length - 1)] = str;
        }
        else
        {
          localObject3 = new String[] { str };
        }
        localObject1 = localObject3;
      }
    }
    boolean bool = paramPdfObject.getBoolean(24);
    if (localObject2.length > 0)
    {
      localObject3 = new boolean[localObject2.length + 1];
      System.arraycopy(localObject2, 0, localObject3, 0, localObject2.length);
      localObject3[(localObject3.length - 1)] = bool;
    }
    else
    {
      localObject3 = new boolean[] { bool };
    }
    localObject2 = localObject3;
    paramFieldsHideObject.setFieldArray((String[])localObject1);
    paramFieldsHideObject.setHideArray((boolean[])localObject2);
    if (paramPdfObject.getDictionary(506808388) != null)
    {
      PdfObject localPdfObject = paramPdfObject.getDictionary(506808388);
      getHideMap(localPdfObject, paramFieldsHideObject);
    }
  }

  public void showMessageDialog(String paramString)
  {
    JOptionPane.showMessageDialog(null, paramString);
  }

  public char getKeyPressed(Object paramObject)
  {
    try
    {
      ComponentEvent localComponentEvent = (ComponentEvent)paramObject;
      if ((localComponentEvent instanceof KeyEvent))
        return ((KeyEvent)localComponentEvent).getKeyChar();
      return ' ';
    }
    catch (Exception localException)
    {
      System.out.println(new StringBuilder().append("Exception ").append(localException).toString());
    }
    return ' ';
  }

  public void setFieldVisibility(FieldsHideObject paramFieldsHideObject)
  {
    String[] arrayOfString = paramFieldsHideObject.getFieldArray();
    boolean[] arrayOfBoolean = paramFieldsHideObject.getHideArray();
    if (arrayOfString.length != arrayOfBoolean.length)
    {
      LogWriter.writeFormLog("{custommouselistener} number of fields and nuber of hides or not the same", false);
      return;
    }
    for (int i = 0; i < arrayOfString.length; i++)
      hideComp(arrayOfString[i], arrayOfBoolean[i] == 0);
  }

  private void hideComp(String paramString, boolean paramBoolean)
  {
    Object[] arrayOfObject1 = this.acrorend.getFormComponents(paramString, ReturnValues.FORMOBJECTS_FROM_NAME, -1);
    Object[] arrayOfObject2 = this.acrorend.getFormComponents(paramString, ReturnValues.FORMOBJECTS_FROM_NAME, -1);
    if (arrayOfObject1 != null)
      for (Object localObject1 : arrayOfObject2)
      {
        FormObject localFormObject1 = (FormObject)localObject1;
        Rectangle localRectangle1 = localFormObject1.getBoundingRectangle();
        if (localRectangle1 != null)
        {
          float f1 = localRectangle1.x;
          float f2 = localRectangle1.y;
          float f3 = localRectangle1.width;
          float f4 = localRectangle1.height;
          Rectangle localRectangle2 = new Rectangle((int)f1, (int)f2, (int)f3, (int)f4);
          for (Object localObject2 : arrayOfObject2)
          {
            FormObject localFormObject2 = (FormObject)localObject2;
            if ((localFormObject2 != null) && (localRectangle2 != null) && (localFormObject2.getBoundingRectangle() != null) && (localRectangle2.contains(localFormObject2.getBoundingRectangle())))
              localFormObject2.setVisible(!paramBoolean);
          }
          localFormObject1.setVisible(paramBoolean);
        }
      }
  }

  public void print()
  {
    PdfDecoder localPdfDecoder = (PdfDecoder)this.decode_pdf;
    if (JOptionPane.showConfirmDialog(localPdfDecoder, Messages.getMessage("PdfViewerPrinting.ConfirmPrint"), Messages.getMessage("PdfViewerPrint.Printing"), 0) == 0)
    {
      PrinterJob localPrinterJob = PrinterJob.getPrinterJob();
      PageFormat localPageFormat = localPrinterJob.defaultPage();
      int i = localPdfDecoder.getPDFWidth() < localPdfDecoder.getPDFHeight() ? 1 : 0;
      localPageFormat.setOrientation(i);
      Paper localPaper = new Paper();
      localPaper.setSize(595.0D, 842.0D);
      localPaper.setImageableArea(43.0D, 43.0D, 509.0D, 756.0D);
      localPageFormat.setPaper(localPaper);
      localPrinterJob.setPrintable(localPdfDecoder, localPageFormat);
      try
      {
        localPrinterJob.print();
      }
      catch (PrinterException localPrinterException)
      {
      }
    }
  }

  private void setCursor(int paramInt)
  {
  }

  private void showSig(PdfObject paramPdfObject)
  {
    PdfDecoder localPdfDecoder = (PdfDecoder)this.decode_pdf;
    JDialog localJDialog = new JDialog(getParentJFrame(localPdfDecoder), "Signature Properties", true);
    Summary localSummary = new Summary(localJDialog, paramPdfObject);
    localSummary.setValues(paramPdfObject.getTextStreamValue(506543413), paramPdfObject.getTextStreamValue(826499443), paramPdfObject.getTextStreamValue(1618506351));
    localJDialog.getContentPane().add(localSummary);
    localJDialog.setSize(550, 220);
    localJDialog.setLocationRelativeTo(null);
    localJDialog.setVisible(true);
  }

  private static JFrame getParentJFrame(Component paramComponent)
  {
    while (true)
    {
      if (paramComponent.getParent() == null)
        return null;
      if ((paramComponent.getParent() instanceof JFrame))
        return (JFrame)paramComponent.getParent();
      paramComponent = paramComponent.getParent();
    }
  }

  private void submitURL(String[] paramArrayOfString, boolean paramBoolean, String paramString)
  {
    if (paramString != null)
    {
      Object localObject1 = new Component[0];
      String[] arrayOfString = new String[0];
      int i;
      Object localObject3;
      int j;
      if (paramArrayOfString != null)
      {
        if (!paramBoolean)
          arrayOfString = paramArrayOfString;
        for (i = 0; i < arrayOfString.length; i++)
        {
          localObject2 = (Component[])this.acrorend.getFormComponents(arrayOfString[i], ReturnValues.GUI_FORMS_FROM_NAME, -1);
          if (localObject2 != null)
          {
            localObject3 = new Component[localObject1.length + localObject2.length];
            if (localObject2.length > 1)
              LogWriter.writeFormLog("(internal only) SubmitForm multipul components with same name", false);
            for (j = 0; i < localObject3.length; j++)
              if (j < localObject1.length)
                localObject3[j] = localObject1[j];
              else if (j - localObject1.length < localObject2.length)
                localObject3[j] = localObject2[(j - localObject1.length)];
            localObject1 = localObject3;
          }
        }
      }
      else
      {
        localObject1 = (Component[])this.acrorend.getFormComponents(null, ReturnValues.GUI_FORMS_FROM_NAME, -1);
      }
      Object localObject2 = "";
      if ((localObject1 != null) && (localObject1.length > 0))
        for (Object localObject4 : localObject1)
          if ((localObject4 instanceof JTextComponent))
            localObject2 = new StringBuilder().append((String)localObject2).append(((JTextComponent)localObject4).getText()).toString();
          else if ((localObject4 instanceof AbstractButton))
            localObject2 = new StringBuilder().append((String)localObject2).append(((AbstractButton)localObject4).getText()).toString();
          else if (localObject4 != null)
            LogWriter.writeFormLog("(internal only) SubmitForm field form type not accounted for", false);
      try
      {
        BrowserLauncher.openURL(new StringBuilder().append(paramString).append("?en&q=").append((String)localObject2).toString());
      }
      catch (IOException localIOException)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog(new StringBuilder().append("Exception: ").append(localIOException.getMessage()).toString());
      }
    }
  }

  private void popup(Object paramObject, FormObject paramFormObject, PdfObjectReader paramPdfObjectReader)
  {
    if (((MouseEvent)paramObject).getClickCount() == 2)
    {
      FormObject localFormObject1 = (FormObject)paramFormObject.getDictionary(1061176672);
      paramPdfObjectReader.checkResolved(localFormObject1);
      FormObject localFormObject2 = this.acrorend.getFormObject(localFormObject1.getObjectRefAsString());
      Object localObject = localFormObject2.getGUIComponent();
      if (localObject != null)
      {
        JComponent localJComponent = (JComponent)localObject;
        if (localJComponent.isVisible())
          localJComponent.setVisible(false);
        else
          localJComponent.setVisible(true);
      }
      ((JButton)((MouseEvent)paramObject).getSource()).setFocusable(false);
    }
  }

  public PdfLayerList getLayerHandler()
  {
    if (this.decode_pdf == null)
      return null;
    Object localObject = this.decode_pdf.getJPedalObject(826881374);
    if (localObject == null)
      return null;
    return (PdfLayerList)localObject;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.acroforms.actions.DefaultActionHandler
 * JD-Core Version:    0.6.2
 */