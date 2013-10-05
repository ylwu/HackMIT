package org.jpedal.objects.acroforms.overridingImplementations;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.BorderFactory;
import javax.swing.JInternalFrame;
import javax.swing.JTextArea;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import org.jpedal.objects.raw.FormObject;
import org.jpedal.objects.raw.PdfObject;

public class PdfSwingPopup extends JInternalFrame
{
  private static final long serialVersionUID = 796302916236391896L;
  private int currentZIndex = 0;

  public PdfSwingPopup(FormObject paramFormObject, int paramInt)
  {
    PdfObject localPdfObject = paramFormObject.getParentPdfObj();
    if (localPdfObject == null)
      return;
    if (localPdfObject.getParameterConstant(1147962727) == 607471684)
    {
      str1 = localPdfObject.getTextStreamValue(506543413);
      if (str1 != null)
      {
        if ((str1.equals("Comment")) && (paramFormObject.getFloatArray(19) == null))
          paramFormObject.setFloatArray(19, new float[] { 255.0F, 255.0F, 0.0F });
      }
      else
      {
        localObject = localPdfObject.getFloatArray(573911876);
        if (localObject != null)
        {
          localObject[0] = localObject[0];
          localObject[2] += 160.0F;
          localObject[3] += 80.0F;
          localObject[1] = localObject[1];
          paramFormObject.setFloatArray(573911876, (float[])localObject);
        }
      }
    }
    String str1 = localPdfObject.getTextStreamValue(29);
    Object localObject = null;
    if (str1 != null)
    {
      localObject = new StringBuffer(str1);
      ((StringBuffer)localObject).delete(0, 2);
      ((StringBuffer)localObject).insert(10, ':');
      ((StringBuffer)localObject).insert(13, ':');
      ((StringBuffer)localObject).insert(16, ' ');
      str2 = ((StringBuffer)localObject).substring(0, 4);
      str3 = ((StringBuffer)localObject).substring(6, 8);
      ((StringBuffer)localObject).delete(6, 8);
      ((StringBuffer)localObject).delete(0, 4);
      ((StringBuffer)localObject).insert(0, str3);
      ((StringBuffer)localObject).insert(4, str2);
      ((StringBuffer)localObject).insert(2, '/');
      ((StringBuffer)localObject).insert(5, '/');
      ((StringBuffer)localObject).insert(10, ' ');
    }
    String str2 = localPdfObject.getTextStreamValue(591737402);
    String str3 = paramFormObject.getTextStreamValue(36);
    if (str3 == null)
      str3 = "";
    String str4 = "";
    if (str2 != null)
      str4 = str4 + str2 + '\t';
    if (localObject != null)
      str4 = str4 + localObject;
    str4 = str4 + '\n' + str3;
    String str5 = localPdfObject.getTextStreamValue(1216184967);
    if (str5 == null)
      str5 = "";
    if (str5.indexOf('\r') != -1)
      str5 = str5.replaceAll("\r", "\n");
    float[] arrayOfFloat = localPdfObject.getFloatArray(19);
    Color localColor = null;
    if (arrayOfFloat != null)
    {
      if ((arrayOfFloat[0] > 1.0F) || (arrayOfFloat[1] > 1.0F) || (arrayOfFloat[2] > 1.0F))
        localColor = new Color((int)arrayOfFloat[0], (int)arrayOfFloat[1], (int)arrayOfFloat[2]);
      else
        localColor = new Color(arrayOfFloat[0], arrayOfFloat[1], arrayOfFloat[2]);
      setBorder(BorderFactory.createLineBorder(localColor));
    }
    ((BasicInternalFrameUI)getUI()).setNorthPane(null);
    setLayout(new BorderLayout());
    JTextArea localJTextArea1 = new JTextArea(str4);
    localJTextArea1.setEditable(false);
    if (localColor != null)
      localJTextArea1.setBackground(localColor);
    add(localJTextArea1, "North");
    JTextArea localJTextArea2 = new JTextArea(str5);
    localJTextArea2.setWrapStyleWord(true);
    localJTextArea2.setLineWrap(true);
    add(localJTextArea2, "Center");
    Font localFont1 = localJTextArea1.getFont();
    localJTextArea1.setFont(new Font(localFont1.getName(), localFont1.getStyle(), localFont1.getSize() - 1));
    Font localFont2 = localJTextArea2.getFont();
    localJTextArea2.setFont(new Font(localFont2.getName(), localFont2.getStyle(), localFont2.getSize() - 2));
    MyMouseMotionAdapter localMyMouseMotionAdapter = new MyMouseMotionAdapter(null);
    localJTextArea1.addMouseMotionListener(localMyMouseMotionAdapter);
    addFocusListener(new FocusAdapter()
    {
      public void focusGained(FocusEvent paramAnonymousFocusEvent)
      {
        PdfSwingPopup.this.toFront();
        super.focusGained(paramAnonymousFocusEvent);
      }
    });
    addInternalFrameListener(new InternalFrameAdapter()
    {
      public void internalFrameActivated(InternalFrameEvent paramAnonymousInternalFrameEvent)
      {
        JInternalFrame localJInternalFrame = paramAnonymousInternalFrameEvent.getInternalFrame();
        localJInternalFrame.getParent().setComponentZOrder(localJInternalFrame, 0);
        PdfSwingPopup.this.currentZIndex = 0;
      }

      public void internalFrameDeactivated(InternalFrameEvent paramAnonymousInternalFrameEvent)
      {
        JInternalFrame localJInternalFrame = paramAnonymousInternalFrameEvent.getInternalFrame();
        localJInternalFrame.getParent().setComponentZOrder(localJInternalFrame, 1);
        PdfSwingPopup.this.currentZIndex = localJInternalFrame.getParent().getComponentZOrder(localJInternalFrame);
      }
    });
  }

  public void paint(Graphics paramGraphics)
  {
    super.paint(paramGraphics);
    getParent().setComponentZOrder(this, this.currentZIndex);
  }

  private class MyMouseMotionAdapter extends MouseMotionAdapter
  {
    private MyMouseMotionAdapter()
    {
    }

    public void mouseDragged(MouseEvent paramMouseEvent)
    {
      Point localPoint1 = paramMouseEvent.getPoint();
      Point localPoint2 = PdfSwingPopup.this.getLocation();
      localPoint2.translate(localPoint1.x, localPoint1.y);
      PdfSwingPopup.this.setLocation(localPoint2);
      super.mouseDragged(paramMouseEvent);
    }
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.acroforms.overridingImplementations.PdfSwingPopup
 * JD-Core Version:    0.6.2
 */