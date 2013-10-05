package org.jpedal.objects.javascript;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import org.jpedal.parser.DecoderOptions;
import org.jpedal.utils.Messages;

public class JSApp
{
  private static final int ICON_ERROR = 0;
  private static final int ICON_WARNING = 1;
  private static final int ICON_QUESTION = 2;
  private static final int ICON_STATUS = 3;
  private static final int BUTTONTYPE_OK = 0;
  private static final int BUTTONTYPE_OK_CANCEL = 1;
  private static final int BUTTONTYPE_YES_NO = 2;
  private static final int BUTTONTYPE_YES_NO_CANCEL = 3;
  private static final int BUTTON_OK = 1;
  private static final int BUTTON_CANCEL = 2;
  private static final int BUTTON_NO = 3;
  private static final int BUTTON_YES = 4;
  public static final boolean showOutput = false;
  private int count;
  public String viewerType = "Exchange-Pro";
  public String viewerVariation = "Reader";
  public int viewerVersion = 10;
  public String platform = "UNIX";

  private void setPlatform()
  {
    if (DecoderOptions.isRunningOnWindows)
      this.platform = "WIN";
    else if (DecoderOptions.isRunningOnMac)
      this.platform = "MAC";
    else
      this.platform = "UNIX";
  }

  public void alert()
  {
  }

  public void alert(Object paramObject1, Object paramObject2, Object paramObject3)
  {
  }

  public int alert(String paramString1, int paramInt1, int paramInt2, String paramString2)
  {
    int i;
    switch (paramInt1)
    {
    case 0:
      i = 0;
      break;
    case 1:
      i = 2;
      break;
    case 2:
      i = 3;
      break;
    case 3:
      i = -1;
      break;
    default:
      i = -1;
    }
    int j;
    switch (paramInt2)
    {
    case 0:
      j = -1;
      break;
    case 1:
      j = 2;
      break;
    case 2:
      j = 0;
      break;
    case 3:
      j = 1;
      break;
    default:
      j = 2;
    }
    if ((paramString2 == null) || (paramString2.length() <= 0))
      paramString2 = "Jpedal JavaScript Window";
    int k = JOptionPane.showConfirmDialog(null, paramString1, paramString2, j, i);
    switch (k)
    {
    case 0:
      if ((paramInt2 == 2) || (paramInt2 == 3))
        k = 4;
      else
        k = 1;
      break;
    case 2:
      k = 2;
      break;
    case 1:
      k = 3;
      break;
    case -1:
      if (j == -1)
        k = 1;
      else if (j == 0)
        k = 3;
      else
        k = 2;
      break;
    }
    return k;
  }

  public int alert(String paramString, int paramInt1, int paramInt2)
  {
    return alert(paramString, paramInt1, paramInt2, null);
  }

  public int alert(String paramString, int paramInt)
  {
    return alert(paramString, paramInt, -1, null);
  }

  public int alert(String paramString1, int paramInt1, int paramInt2, String paramString2, Object paramObject1, Object paramObject2)
  {
    return alert(paramString1, paramInt1, paramInt2, paramString2);
  }

  public int alert(String paramString1, int paramInt1, int paramInt2, String paramString2, Object paramObject)
  {
    return alert(paramString1, paramInt1, paramInt2, paramString2);
  }

  public String response(String paramString1, String paramString2, String paramString3, boolean paramBoolean, String paramString4)
  {
    BorderLayout localBorderLayout = new BorderLayout();
    localBorderLayout.setHgap(5);
    JPanel localJPanel = new JPanel(localBorderLayout);
    JLabel localJLabel = new JLabel(paramString1);
    Object localObject1;
    if (paramBoolean)
      localObject1 = new JPasswordField();
    else
      localObject1 = new JTextField();
    if (paramString3 != null)
      ((JTextField)localObject1).setText(paramString3);
    String str;
    if (paramString2 != null)
      str = paramString2;
    else
      str = "Input";
    localJPanel.add(localJLabel, "North");
    if (paramString4 != null)
    {
      localObject2 = new JLabel(paramString4);
      localJPanel.add((Component)localObject2, "West");
    }
    localJPanel.add((Component)localObject1, "Center");
    Object localObject2 = { "Ok", "Cancel" };
    int i = JOptionPane.showOptionDialog(null, localJPanel, str, 1, -1, null, (Object[])localObject2, localObject2[0]);
    if (i == 0)
      return ((JTextField)localObject1).getText();
    return null;
  }

  public String response(String paramString1, String paramString2, String paramString3, boolean paramBoolean)
  {
    return response(paramString1, paramString2, paramString3, paramBoolean, null);
  }

  public String response(String paramString1, String paramString2, String paramString3)
  {
    return response(paramString1, paramString2, paramString3, false, null);
  }

  public String response(String paramString1, String paramString2)
  {
    return response(paramString1, paramString2, null, false, null);
  }

  public String response(String paramString)
  {
    return response(paramString, null, null, false, null);
  }

  public int alert(String paramString)
  {
    int i = JOptionPane.showConfirmDialog(null, paramString, "JavaScript", -1, 0);
    return i;
  }

  public void beep()
  {
    Toolkit.getDefaultToolkit().beep();
  }

  public void beep(int paramInt)
  {
    Toolkit.getDefaultToolkit().beep();
  }

  public void launchURL(String paramString)
  {
    try
    {
      Desktop.getDesktop().browse(URI.create(paramString));
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
  }

  public void launchURL(String paramString, boolean paramBoolean)
  {
    launchURL(paramString);
  }

  public void popUpMenuEx(String[] paramArrayOfString)
  {
  }

  public void mailMsg(boolean paramBoolean, String paramString1, String paramString2, String paramString3, String paramString4, String paramString5)
  {
    System.out.println("mailMsg(boolean bUI, String cTo)");
    paramBoolean = true;
    if ((paramString1 != null) && (paramString1.length() > 0))
      if ((Desktop.isDesktopSupported()) && (Desktop.getDesktop().isSupported(Desktop.Action.MAIL)))
        try
        {
          paramString1 = paramString1.replace(';', ',');
          String str = "mailto:" + paramString1;
          int i = 0;
          if ((paramString2 != null) && (paramString2.length() > 0))
          {
            i = 1;
            str = str + "?";
            str = str + "cc=" + paramString2.replace(';', ',');
          }
          if ((paramString3 != null) && (paramString3.length() > 0))
          {
            if (i == 0)
            {
              i = 1;
              str = str + "?";
            }
            else
            {
              str = str + "&";
            }
            str = str + "bcc=" + paramString3.replace(';', ',');
          }
          if ((paramString4 != null) && (paramString4.length() > 0))
          {
            if (i == 0)
            {
              i = 1;
              str = str + "?";
            }
            else
            {
              str = str + "&";
            }
            str = str + "subject=" + paramString4.replace(" ", "%20");
          }
          if ((paramString5 != null) && (paramString5.length() > 0))
          {
            if (i == 0)
              str = str + "?";
            else
              str = str + "&";
            str = str + "body=" + paramString5.replace(" ", "%20");
          }
          URI localURI = URI.create(str);
          Desktop.getDesktop().mail(localURI);
        }
        catch (IOException localIOException)
        {
          localIOException.printStackTrace();
        }
      else
        alert("Emailing is not supported on your platform.");
  }

  public void mailMsg(boolean paramBoolean, String paramString1, String paramString2, String paramString3, String paramString4)
  {
    mailMsg(paramBoolean, paramString1, paramString2, paramString3, paramString4, null);
  }

  public void mailMsg(boolean paramBoolean, String paramString1, String paramString2, String paramString3)
  {
    mailMsg(paramBoolean, paramString1, paramString2, paramString3, null, null);
  }

  public void mailMsg(boolean paramBoolean, String paramString1, String paramString2)
  {
    mailMsg(paramBoolean, paramString1, paramString2, null, null, null);
  }

  public void mailMsg(boolean paramBoolean, String paramString)
  {
    mailMsg(paramBoolean, paramString, null, null, null, null);
  }

  public void mailMsg(Object paramObject)
  {
    System.out.println("mailMsg(Object o)");
    System.out.println(paramObject.getClass().getSimpleName());
  }

  public JSApp()
  {
    setPlatform();
  }

  public JSApp(int paramInt)
  {
    this.count = paramInt;
    setPlatform();
  }

  public int getCount()
  {
    return this.count++;
  }

  public void resetCount()
  {
    this.count = 0;
  }

  public String getString(String paramString1, String paramString2)
  {
    String str = "";
    if (paramString1.equals("EScript"))
      if (paramString2.equals("IDS_GREATER_THAN"))
      {
        str = Messages.getMessage("PdfJavaScriptMessage.IDS_GREATER_THAN");
      }
      else if (paramString2.equals("IDS_GT_AND_LT"))
      {
        str = Messages.getMessage("PdfJavaScriptMessage.IDS_GT_AND_LT");
      }
      else if (paramString2.equals("IDS_LESS_THAN"))
      {
        str = Messages.getMessage("PdfJavaScriptMessage.IDS_LESS_THAN");
      }
      else if (paramString2.equals("IDS_INVALID_MONTH"))
      {
        str = Messages.getMessage("PdfJavaScriptMessage.IDS_INVALID_MONTH");
      }
      else if (paramString2.equals("IDS_INVALID_DATE"))
      {
        str = Messages.getMessage("PdfJavaScriptMessage.IDS_INVALID_DATE");
      }
      else if (paramString2.equals("IDS_INVALID_VALUE"))
      {
        str = Messages.getMessage("PdfJavaScriptMessage.IDS_INVALID_VALUE");
      }
      else if (paramString2.equals("IDS_AM"))
      {
        str = Messages.getMessage("PdfJavaScriptMessage.IDS_AM");
      }
      else if (paramString2.equals("IDS_PM"))
      {
        str = Messages.getMessage("PdfJavaScriptMessage.IDS_PM");
      }
      else
      {
        if (paramString2.equals("IDS_MONTH_INFO"))
          return "\"\"";
        if (paramString2.equals("IDS_STARTUP_CONSOLE_MSG"))
          str = Messages.getMessage("PdfJavaScriptMessage.IDS_STARTUP_CONSOLE_MSG");
      }
    return "\"" + str + "\"";
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.javascript.JSApp
 * JD-Core Version:    0.6.2
 */