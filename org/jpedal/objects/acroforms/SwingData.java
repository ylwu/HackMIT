package org.jpedal.objects.acroforms;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.text.JTextComponent;
import org.jpedal.external.CustomFormPrint;
import org.jpedal.objects.PdfPageData;
import org.jpedal.objects.acroforms.creation.FormFactory;
import org.jpedal.objects.acroforms.creation.JPedalBorderFactory;
import org.jpedal.objects.acroforms.overridingImplementations.FixImageIcon;
import org.jpedal.objects.acroforms.overridingImplementations.ReadOnlyTextIcon;
import org.jpedal.objects.layers.PdfLayerList;
import org.jpedal.objects.raw.FormObject;
import org.jpedal.objects.raw.PdfArrayIterator;
import org.jpedal.objects.raw.PdfObject;
import org.jpedal.parser.DecoderOptions;

public class SwingData extends GUIData
{
  public static boolean JVMBugRightAlignFix = false;
  CustomFormPrint customFormPrint = null;
  JFrame dummyPanel;
  private JPanel panel;
  public static int readOnlyScaling = -1;
  int maxLengthForTextOnPage = 0;

  public SwingData()
  {
  }

  public SwingData(int paramInt)
  {
  }

  public void dispose()
  {
    super.dispose();
    if (this.dummyPanel != null)
      if (SwingUtilities.isEventDispatchThread())
        this.dummyPanel.dispose();
      else
        try
        {
          SwingUtilities.invokeAndWait(new Runnable()
          {
            public void run()
            {
              SwingData.this.dummyPanel.dispose();
            }
          });
        }
        catch (InvocationTargetException localInvocationTargetException)
        {
          localInvocationTargetException.printStackTrace();
        }
        catch (InterruptedException localInterruptedException)
        {
          localInterruptedException.printStackTrace();
        }
  }

  private void renderComponent(Graphics2D paramGraphics2D, FormObject paramFormObject, Component paramComponent, int paramInt1, boolean paramBoolean1, int paramInt2, boolean paramBoolean2)
  {
    if (paramComponent != null)
    {
      int i = 0;
      int j = paramFormObject.getPageNumber();
      Object localObject1;
      Object localObject2;
      if ((!paramBoolean1) && ((paramComponent instanceof JComboBox)))
      {
        localObject1 = (JComboBox)paramComponent;
        if (((JComboBox)localObject1).isEditable())
        {
          i = 1;
          ((JComboBox)localObject1).setEditable(false);
        }
        if (((JComboBox)localObject1).getComponentCount() > 0)
        {
          localObject2 = ((JComboBox)localObject1).getSelectedItem();
          if (localObject2 != null)
          {
            JTextField localJTextField1 = new JTextField();
            localJTextField1.setText(((Object)localObject2).toString());
            localJTextField1.setBackground(((JComboBox)localObject1).getBackground());
            localJTextField1.setForeground(((JComboBox)localObject1).getForeground());
            localJTextField1.setFont(((JComboBox)localObject1).getFont());
            localJTextField1.setBorder(((JComboBox)localObject1).getBorder());
            renderComponent(paramGraphics2D, paramFormObject, localJTextField1, paramInt1, false, paramInt2, paramBoolean2);
          }
        }
        paramBoolean1 = true;
      }
      if (!paramBoolean1)
      {
        localObject1 = paramGraphics2D.getTransform();
        Object localObject3;
        if (paramBoolean2)
        {
          scaleComponent(paramFormObject, 1.0F, paramInt1, paramComponent, false, paramInt2);
          switch (360 - paramInt1)
          {
          case 270:
            localObject2 = AffineTransform.getRotateInstance(4.71238898038469D, 0.0D, 0.0D);
            paramGraphics2D.translate(paramComponent.getBounds().y + this.cropOtherY[j] - this.insetH, this.pageData.getCropBoxHeight(j) - paramComponent.getBounds().x + this.insetW);
            paramGraphics2D.transform((AffineTransform)localObject2);
            paramGraphics2D.translate(-this.insetW, 0);
            break;
          case 90:
            localObject2 = AffineTransform.getRotateInstance(1.570796326794897D, 0.0D, 0.0D);
            paramGraphics2D.translate(paramComponent.getBounds().y + this.cropOtherY[j] - this.insetH, paramComponent.getBounds().x + this.insetW);
            paramGraphics2D.transform((AffineTransform)localObject2);
            paramGraphics2D.translate(0, -this.insetH);
            break;
          case 180:
            localObject2 = AffineTransform.getRotateInstance(3.141592653589793D, 0.0D, 0.0D);
            paramGraphics2D.translate(paramComponent.getBounds().x - this.insetW, paramComponent.getBounds().y + this.cropOtherY[j]);
            paramGraphics2D.transform((AffineTransform)localObject2);
            paramGraphics2D.translate(-this.insetW, -this.insetH);
            break;
          default:
            paramGraphics2D.translate(paramComponent.getBounds().x - this.insetW, paramComponent.getBounds().y + this.cropOtherY[j]);
          }
        }
        else
        {
          scaleComponent(paramFormObject, 1.0F, paramInt1, paramComponent, false, paramInt2);
          localObject2 = paramComponent.getBounds();
          paramGraphics2D.translate(((Rectangle)localObject2).x - this.insetW, ((Rectangle)localObject2).y + this.cropOtherY[j]);
          if ((paramComponent instanceof JTextComponent))
          {
            if ((this.pageData.getRotation(j) == 90) || (this.pageData.getRotation(j) == 270))
            {
              paramComponent.setBounds(((Rectangle)localObject2).x, ((Rectangle)localObject2).y, ((Rectangle)localObject2).height, ((Rectangle)localObject2).width);
              localObject2 = paramComponent.getBounds();
            }
            int m = paramInt1 - this.pageData.getRotation(0);
            if (m < 0)
              m = 360 + m;
            switch (m)
            {
            case 270:
              localObject3 = AffineTransform.getRotateInstance(m * 3.141592653589793D / 180.0D, 0.0D, 0.0D);
              paramGraphics2D.transform((AffineTransform)localObject3);
              paramGraphics2D.translate(-((Rectangle)localObject2).width, 0);
              break;
            case 90:
              localObject3 = AffineTransform.getRotateInstance(m * 3.141592653589793D / 180.0D, 0.0D, 0.0D);
              paramGraphics2D.transform((AffineTransform)localObject3);
              paramGraphics2D.translate(0, -((Rectangle)localObject2).height);
              break;
            case 180:
              localObject3 = AffineTransform.getRotateInstance(m * 3.141592653589793D / 180.0D, 0.0D, 0.0D);
              paramGraphics2D.transform((AffineTransform)localObject3);
              paramGraphics2D.translate(-((Rectangle)localObject2).width, -((Rectangle)localObject2).height);
            }
          }
        }
        int k = 0;
        if ((JVMBugRightAlignFix) && ((paramComponent instanceof JTextField)))
        {
          JTextField localJTextField2 = new JTextField();
          localObject3 = (JTextField)paramComponent;
          if (((JTextField)localObject3).getHorizontalAlignment() == 4)
          {
            localJTextField2.setFont(((JTextField)localObject3).getFont());
            localJTextField2.setLocation(((JTextField)localObject3).getLocation());
            localJTextField2.setSize(((JTextField)localObject3).getSize());
            localJTextField2.setBorder(((JTextField)localObject3).getBorder());
            localJTextField2.setHorizontalAlignment(4);
            int n = 0;
            int i1 = paramGraphics2D.getFontMetrics(paramComponent.getFont()).stringWidth(new String(createCharArray(' ', this.maxLengthForTextOnPage - ((JTextField)localObject3).getText().length())) + ((JTextField)localObject3).getText());
            int i2 = paramGraphics2D.getFontMetrics(paramComponent.getFont().deriveFont(7.0F)).stringWidth(new String(createCharArray(' ', this.maxLengthForTextOnPage - ((JTextField)localObject3).getText().length())) + ((JTextField)localObject3).getText());
            int i3 = i1 - i2;
            if (i3 > 0)
              n = i3 / paramGraphics2D.getFontMetrics(paramComponent.getFont().deriveFont(7.0F)).stringWidth(" ");
            String str1 = ((JTextField)localObject3).getText();
            int i4 = this.maxLengthForTextOnPage + n - ((JTextField)localObject3).getText().length();
            localJTextField2.setText(new String(createCharArray(' ', i4)) + str1);
            i1 = paramGraphics2D.getFontMetrics(paramComponent.getFont()).stringWidth(localJTextField2.getText());
            int i5 = 0;
            if (localJTextField2.getBorder() != null)
              i5 = localJTextField2.getBorder().getBorderInsets(localJTextField2).left + localJTextField2.getBorder().getBorderInsets(localJTextField2).right;
            String str2;
            for (int i6 = 0; (i4 > 0) && (i1 > localJTextField2.getWidth() - i5); i6 = 1)
            {
              i4 = this.maxLengthForTextOnPage + n - ((JTextField)localObject3).getText().length();
              str2 = new String(createCharArray(' ', i4)) + str1;
              localJTextField2.setText(str2);
              n--;
              i1 = paramGraphics2D.getFontMetrics(paramComponent.getFont().deriveFont(7.0F)).stringWidth(localJTextField2.getText());
            }
            if (i6 != 0)
            {
              n--;
              i4 = this.maxLengthForTextOnPage + n - ((JTextField)localObject3).getText().length();
              str2 = new String(createCharArray(' ', i4)) + str1;
              localJTextField2.setText(str2);
            }
            localJTextField2.paint(paramGraphics2D);
            k = 1;
          }
        }
        if (k == 0)
          paramComponent.paint(paramGraphics2D);
        paramGraphics2D.setTransform((AffineTransform)localObject1);
      }
      if (i != 0)
        ((JComboBox)paramComponent).setEditable(true);
    }
  }

  private void renderComponent(Graphics2D paramGraphics2D, FormObject paramFormObject, int paramInt1, boolean paramBoolean1, int paramInt2, boolean paramBoolean2)
  {
    int i = 0;
    int j = paramFormObject.getPageNumber();
    if (!paramBoolean1)
    {
      AffineTransform localAffineTransform1 = paramGraphics2D.getTransform();
      Font localFont = paramGraphics2D.getFont();
      Stroke localStroke = paramGraphics2D.getStroke();
      Color localColor1 = paramGraphics2D.getColor();
      if (paramBoolean2)
      {
        AffineTransform localAffineTransform2;
        switch (360 - paramInt1)
        {
        case 270:
          localAffineTransform2 = AffineTransform.getRotateInstance(4.71238898038469D, 0.0D, 0.0D);
          paramGraphics2D.translate(paramFormObject.getBoundingRectangle().y + this.cropOtherY[j] - this.insetH, this.pageData.getCropBoxHeight(j) - paramFormObject.getBoundingRectangle().x + this.insetW);
          paramGraphics2D.transform(localAffineTransform2);
          paramGraphics2D.translate(-this.insetW, 0);
          break;
        case 90:
          localAffineTransform2 = AffineTransform.getRotateInstance(1.570796326794897D, 0.0D, 0.0D);
          paramGraphics2D.translate(paramFormObject.getBoundingRectangle().y + this.cropOtherY[j] - this.insetH, paramFormObject.getBoundingRectangle().x + this.insetW);
          paramGraphics2D.transform(localAffineTransform2);
          paramGraphics2D.translate(0, -this.insetH);
          break;
        case 180:
          localAffineTransform2 = AffineTransform.getRotateInstance(3.141592653589793D, 0.0D, 0.0D);
          paramGraphics2D.translate(paramFormObject.getBoundingRectangle().x - this.insetW, paramFormObject.getBoundingRectangle().y + this.cropOtherY[j]);
          paramGraphics2D.transform(localAffineTransform2);
          paramGraphics2D.translate(-this.insetW, -this.insetH);
          break;
        default:
          paramGraphics2D.translate(paramFormObject.getBoundingRectangle().x - this.insetW, paramFormObject.getBoundingRectangle().y + this.cropOtherY[j]);
        }
      }
      int k = 0;
      if (k == 0)
      {
        Object localObject1 = paramGraphics2D.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        paramGraphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        int m = 0;
        Color localColor2 = new Color(0, 0, 0, 0);
        Color localColor3 = new Color(0, 0, 0, 0);
        Object localObject4;
        if (paramFormObject.getDictionary(7451) != null)
        {
          localObject2 = paramFormObject.getDictionary(7451);
          localObject3 = ((PdfObject)localObject2).getFloatArray(4627);
          localObject4 = ((PdfObject)localObject2).getFloatArray(4631);
          if ((localObject3 != null) && (localObject3.length > 0))
            switch (localObject3.length)
            {
            case 1:
              localColor3 = new Color(localObject3[0], localObject3[0], localObject3[0], 1.0F);
              break;
            case 3:
              localColor3 = new Color(localObject3[0], localObject3[1], localObject3[2], 1.0F);
              break;
            case 2:
            case 4:
            }
          if ((localObject4 != null) && (localObject4.length > 0))
          {
            switch (localObject4.length)
            {
            case 1:
              localColor2 = new Color(localObject4[0], localObject4[0], localObject4[0], 1.0F);
              break;
            case 3:
              localColor2 = new Color(localObject4[0], localObject4[1], localObject4[2], 1.0F);
              break;
            case 2:
            case 4:
            }
            paramGraphics2D.setColor(localColor2);
            paramGraphics2D.fillRect(paramFormObject.getBoundingRectangle().x + m, this.pageData.getCropBoxHeight(j) - (paramFormObject.getBoundingRectangle().y + paramFormObject.getBoundingRectangle().height) + m, paramFormObject.getBoundingRectangle().width - m * 2, paramFormObject.getBoundingRectangle().height - m * 2);
          }
        }
        Color localColor4;
        Color localColor6;
        if ((paramFormObject.getDictionary(4643) != null) && (paramFormObject.getDictionary(4643).getName(35) != null))
        {
          localObject2 = paramFormObject.getDictionary(4643);
          localObject3 = ((PdfObject)localObject2).getName(35);
          m = ((PdfObject)localObject2).getInt(39);
          localObject4 = ((PdfObject)localObject2).getMixedArray(20);
          if (m > 0)
          {
            paramGraphics2D.setColor(localColor3);
            if ((localObject3 == null) || (((String)localObject3).equals("S")))
            {
              paramGraphics2D.setStroke(new BasicStroke(m));
              paramGraphics2D.drawRect(paramFormObject.getBoundingRectangle().x + m - 1, this.pageData.getCropBoxHeight(j) - (paramFormObject.getBoundingRectangle().y + paramFormObject.getBoundingRectangle().height) + m - 1, paramFormObject.getBoundingRectangle().width - m * 2 + 2, paramFormObject.getBoundingRectangle().height - m * 2 + 2);
            }
            else
            {
              Object localObject5;
              int i1;
              int i3;
              if (((String)localObject3).equals("D"))
              {
                localObject5 = new float[] { 3.0F };
                i1 = 0;
                if (((PdfArrayIterator)localObject4).getTokenCount() > 0)
                {
                  i3 = ((PdfArrayIterator)localObject4).getTokenCount();
                  if (i3 > 0)
                    localObject5 = ((PdfArrayIterator)localObject4).getNextValueAsFloatArray();
                  if (i3 > 1)
                    i1 = ((PdfArrayIterator)localObject4).getNextValueAsInteger();
                }
                if (localObject5.length == 0)
                  paramGraphics2D.setStroke(new BasicStroke(m));
                else if (localObject5.length > 0)
                  paramGraphics2D.setStroke(new BasicStroke(m, 0, 0, 10.0F, (float[])localObject5, i1));
                paramGraphics2D.drawRect(paramFormObject.getBoundingRectangle().x + m - 1, this.pageData.getCropBoxHeight(j) - (paramFormObject.getBoundingRectangle().y + paramFormObject.getBoundingRectangle().height) + m - 1, paramFormObject.getBoundingRectangle().width - m * 2 + 2, paramFormObject.getBoundingRectangle().height - m * 2 + 2);
              }
              else
              {
                int i5;
                int i7;
                if (((String)localObject3).equals("B"))
                {
                  localObject5 = paramGraphics2D.getColor();
                  i1 = paramFormObject.getBoundingRectangle().x;
                  i3 = this.pageData.getCropBoxHeight(j) - (paramFormObject.getBoundingRectangle().y + paramFormObject.getBoundingRectangle().height);
                  i5 = paramFormObject.getBoundingRectangle().width;
                  i7 = paramFormObject.getBoundingRectangle().height;
                  paramGraphics2D.setStroke(new BasicStroke(m, 0, 0, 10.0F));
                  paramGraphics2D.drawRect(i1, i3, i5, i7);
                  paramGraphics2D.setStroke(new BasicStroke(1.0F));
                  i1++;
                  i3++;
                  i5 -= 2;
                  i7 -= 2;
                  paramGraphics2D.setColor(localColor2.darker());
                  paramGraphics2D.fillPolygon(new int[] { i1 + i5, i1 + i5, i1 + i5 - m, i1 + i5 - m, i1 + m, i1 }, new int[] { i3 + i7, i3, i3 + m, i3 + i7 - m, i3 + i7 - m, i3 + i7 }, 6);
                  paramGraphics2D.setColor(localColor2.brighter());
                  paramGraphics2D.fillPolygon(new int[] { i1, i1, i1 + m, i1 + m, i1 + i5 - m, i1 + i5 }, new int[] { i3, i3 + i7, i3 + i7 - m, i3 + m, i3 + m, i3 }, 6);
                  paramGraphics2D.setColor((Color)localObject5);
                }
                else if (((String)localObject3).equals("I"))
                {
                  localObject5 = paramGraphics2D.getColor();
                  i1 = ((Color)localObject5).getRed();
                  i3 = ((Color)localObject5).getGreen();
                  i5 = ((Color)localObject5).getBlue();
                  i7 = paramFormObject.getBoundingRectangle().x;
                  localColor4 = this.pageData.getCropBoxHeight(j) - (paramFormObject.getBoundingRectangle().y + paramFormObject.getBoundingRectangle().height);
                  int i9 = paramFormObject.getBoundingRectangle().width;
                  localColor6 = paramFormObject.getBoundingRectangle().height;
                  paramGraphics2D.setStroke(new BasicStroke(m));
                  paramGraphics2D.drawRect(i7, localColor4, i9, localColor6);
                  paramGraphics2D.setStroke(new BasicStroke(1.0F));
                  i7++;
                  localColor4++;
                  i9 -= 2;
                  localColor6 -= 2;
                  paramGraphics2D.setColor(Color.LIGHT_GRAY);
                  paramGraphics2D.fillPolygon(new int[] { i7 + i9, i7 + i9, i7 + i9 - m, i7 + i9 - m, i7 + m, i7 }, new int[] { localColor4 + localColor6, localColor4, localColor4 + m, localColor4 + localColor6 - m, localColor4 + localColor6 - m, localColor4 + localColor6 }, 6);
                  paramGraphics2D.setColor(Color.GRAY);
                  paramGraphics2D.fillPolygon(new int[] { i7, i7, i7 + m, i7 + m, i7 + i9 - m, i7 + i9 }, new int[] { localColor4, localColor4 + localColor6, localColor4 + localColor6 - m, localColor4 + m, localColor4 + m, localColor4 }, 6);
                  paramGraphics2D.setColor((Color)localObject5);
                }
                else if (((String)localObject3).equals("U"))
                {
                  paramGraphics2D.setStroke(new BasicStroke(m));
                  paramGraphics2D.drawLine(paramFormObject.getBoundingRectangle().x + m - 1, this.pageData.getCropBoxHeight(j) - paramFormObject.getBoundingRectangle().y + m - 1, paramFormObject.getBoundingRectangle().x + paramFormObject.getBoundingRectangle().width - 1, this.pageData.getCropBoxHeight(j) - paramFormObject.getBoundingRectangle().y + m - 1);
                }
              }
            }
          }
        }
        else if (paramFormObject.getObjectArray(1110722433) != null)
        {
          throw new RuntimeException("Border Array not implemented yet");
        }
        paramGraphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, localObject1);
        Object localObject2 = paramGraphics2D.getFontMetrics(paramFormObject.getTextFont());
        Object localObject3 = paramFormObject.getValue();
        if (localObject3 != null)
        {
          localObject4 = ((FontMetrics)localObject2).getStringBounds((String)localObject3, paramGraphics2D);
          int n = ((FontMetrics)localObject2).getHeight();
          if (paramFormObject.getTextSize() <= 0)
          {
            if (paramFormObject.getFieldFlags()[13] == 0)
            {
              float f1 = paramFormObject.getBoundingRectangle().height - m * 2;
              paramGraphics2D.setFont(paramFormObject.getTextFont().deriveFont(f1));
              localObject2 = paramGraphics2D.getFontMetrics(paramFormObject.getTextFont().deriveFont(f1));
              localObject4 = ((FontMetrics)localObject2).getStringBounds((String)localObject3, paramGraphics2D);
              float f2 = (float)(paramFormObject.getBoundingRectangle().width / ((Rectangle2D)localObject4).getWidth());
              if (f2 < 1.0F)
              {
                paramGraphics2D.setFont(paramFormObject.getTextFont().deriveFont(f1 * f2));
                localObject2 = paramGraphics2D.getFontMetrics(paramFormObject.getTextFont().deriveFont(f1 * f2));
                localObject4 = ((FontMetrics)localObject2).getStringBounds((String)localObject3, paramGraphics2D);
              }
              n = ((FontMetrics)localObject2).getHeight();
            }
            else
            {
              paramGraphics2D.setFont(paramFormObject.getTextFont().deriveFont(12.0F));
              n = 12;
            }
          }
          else
            paramGraphics2D.setFont(paramFormObject.getTextFont());
          if (paramFormObject.getTextColor() != null)
            paramGraphics2D.setColor(paramFormObject.getTextColor());
          else
            paramGraphics2D.setColor(Color.BLACK);
          int i2 = ((FontMetrics)localObject2).getDescent();
          int i4 = paramFormObject.getAlignment();
          if (paramFormObject.getFieldFlags()[17] != 0)
            i4 = 0;
          if ((paramFormObject.getObjectArray(2048068) != null) && (paramFormObject.getFieldFlags()[18] == 0))
          {
            String[] arrayOfString = paramFormObject.getItemsList();
            if (arrayOfString != null)
            {
              int[] arrayOfInt = paramFormObject.getIntArray(25);
              localColor4 = paramFormObject.getInt(9241);
              if (localColor4 < 0)
                localColor4 = 0;
              int i11;
              if (arrayOfInt != null)
              {
                localColor5 = paramGraphics2D.getColor();
                localColor6 = new Color(DecoderOptions.highlightColor.getRed() / 255, DecoderOptions.highlightColor.getGreen() / 255, DecoderOptions.highlightColor.getBlue() / 255, DecoderOptions.highlightComposite);
                paramGraphics2D.setColor(localColor6);
                for (i11 = 0; i11 != arrayOfInt.length; i11++)
                {
                  int i12 = paramFormObject.getBoundingRectangle().x + m;
                  int i13 = this.pageData.getCropBoxHeight(j) - (paramFormObject.getBoundingRectangle().y + paramFormObject.getBoundingRectangle().height) - m;
                  i13 += i2;
                  i13 += n * arrayOfInt[i11];
                  paramGraphics2D.fillRect(i12, i13 + m, paramFormObject.getBoundingRectangle().width - m * 2 + 2, n);
                }
                paramGraphics2D.setColor(localColor5);
              }
              for (Color localColor5 = localColor4; localColor5 != arrayOfString.length; localColor5++)
              {
                int i10 = paramFormObject.getBoundingRectangle().x + m;
                i11 = this.pageData.getCropBoxHeight(j) - (paramFormObject.getBoundingRectangle().y + paramFormObject.getBoundingRectangle().height) - m + n;
                i11 += n * (localColor5 - localColor4);
                switch (i4)
                {
                case 0:
                  paramGraphics2D.drawString(arrayOfString[localColor5], (int)(i10 + (paramFormObject.getBoundingRectangle().width - m * 2 - ((Rectangle2D)localObject4).getWidth()) / 2.0D), i11);
                  break;
                case 4:
                  paramGraphics2D.drawString(arrayOfString[localColor5], (int)(i10 + paramFormObject.getBoundingRectangle().width - m * 2 - ((Rectangle2D)localObject4).getWidth()), i11);
                  break;
                default:
                  paramGraphics2D.drawString(arrayOfString[localColor5], i10, i11);
                }
              }
            }
          }
          else if ((localObject3 != null) && (!((String)localObject3).isEmpty()))
          {
            int i6;
            int i8;
            if (paramFormObject.getFieldFlags()[13] != 0)
            {
              i6 = paramFormObject.getBoundingRectangle().x + m;
              i8 = this.pageData.getCropBoxHeight(j) - (paramFormObject.getBoundingRectangle().y + paramFormObject.getBoundingRectangle().height) - m;
              StringTokenizer localStringTokenizer = new StringTokenizer((String)localObject3, "\n");
              while (localStringTokenizer.hasMoreTokens())
              {
                i8 += n;
                switch (i4)
                {
                case 0:
                  paramGraphics2D.drawString(localStringTokenizer.nextToken(), (int)(i6 + (paramFormObject.getBoundingRectangle().width - m * 2 - ((Rectangle2D)localObject4).getWidth()) / 2.0D), i8);
                  break;
                case 4:
                  paramGraphics2D.drawString(localStringTokenizer.nextToken(), (int)(i6 + paramFormObject.getBoundingRectangle().width - m * 2 - ((Rectangle2D)localObject4).getWidth()), i8);
                  break;
                default:
                  paramGraphics2D.drawString(localStringTokenizer.nextToken(), i6, i8);
                }
              }
            }
            else
            {
              i6 = paramFormObject.getBoundingRectangle().x + m;
              i8 = this.pageData.getCropBoxHeight(j) - (paramFormObject.getBoundingRectangle().y + paramFormObject.getBoundingRectangle().height / 2) - m + n / 2;
              switch (i4)
              {
              case 0:
                paramGraphics2D.drawString((String)localObject3, (int)(i6 + (paramFormObject.getBoundingRectangle().width - m * 2 - ((Rectangle2D)localObject4).getWidth()) / 2.0D), i8);
                break;
              case 4:
                paramGraphics2D.drawString((String)localObject3, (int)(i6 + paramFormObject.getBoundingRectangle().width - m * 2 - ((Rectangle2D)localObject4).getWidth()), i8);
                break;
              default:
                paramGraphics2D.drawString((String)localObject3, i6, i8);
              }
            }
          }
        }
      }
      paramGraphics2D.setTransform(localAffineTransform1);
      paramGraphics2D.setFont(localFont);
      paramGraphics2D.setStroke(localStroke);
      paramGraphics2D.setColor(localColor1);
    }
  }

  private static char[] createCharArray(char paramChar, int paramInt)
  {
    if (paramInt <= 0)
      return new char[0];
    char[] arrayOfChar = new char[paramInt];
    Arrays.fill(arrayOfChar, 0, arrayOfChar.length, paramChar);
    return arrayOfChar;
  }

  public void renderFormsOntoG2(Object paramObject, int paramInt1, int paramInt2, int paramInt3, Map paramMap, FormFactory paramFormFactory, int paramInt4)
  {
    if ((this.formsUnordered == null) || (this.rasterizeForms))
      return;
    if ((this.formFactory.getType() == 3) || (this.formFactory.getType() == 4))
      renderFormsOntoG2WithHTML(paramObject, paramInt1, paramInt2, paramInt3, paramMap, paramFormFactory, paramInt4);
    else
      renderFormsOntoG2WithSwing(paramObject, paramInt1, paramInt2, paramInt3, paramMap, paramFormFactory, paramInt4);
  }

  private void renderFormsOntoG2WithSwing(Object paramObject, int paramInt1, int paramInt2, int paramInt3, Map paramMap, FormFactory paramFormFactory, int paramInt4)
  {
    this.componentsToIgnore = paramMap;
    boolean bool1 = paramFormFactory != null;
    FormObject localFormObject;
    Component localComponent;
    if ((JVMBugRightAlignFix) && (bool1))
    {
      this.maxLengthForTextOnPage = 0;
      localObject1 = this.formsUnordered[paramInt1].iterator();
      while (((Iterator)localObject1).hasNext())
      {
        localObject2 = ((Iterator)localObject1).next();
        if (localObject2 != null)
        {
          localFormObject = (FormObject)localObject2;
          localComponent = (Component)checkGUIObjectResolved(localFormObject);
          if ((localComponent instanceof JTextField))
          {
            localObject3 = (JTextField)localComponent;
            int i = ((JTextField)localObject3).getText().length();
            if ((i > this.maxLengthForTextOnPage) && (((JTextField)localObject3).getHorizontalAlignment() == 4))
              this.maxLengthForTextOnPage = i;
          }
        }
      }
    }
    Object localObject1 = (Graphics2D)paramObject;
    Object localObject2 = ((Graphics2D)localObject1).getTransform();
    Object localObject3 = ((Graphics2D)localObject1).getTransform();
    ((AffineTransform)localObject3).scale(1.0D, -1.0D);
    ((AffineTransform)localObject3).translate(0.0D, -paramInt4 - this.insetH);
    ((Graphics2D)localObject1).setTransform((AffineTransform)localObject3);
    if (this.dummyPanel == null)
    {
      this.dummyPanel = new JFrame();
      this.dummyPanel.setDefaultCloseOperation(2);
      this.dummyPanel.pack();
    }
    Iterator localIterator = this.formsUnordered[paramInt1].iterator();
    while (localIterator.hasNext())
    {
      Object localObject4 = localIterator.next();
      if (localObject4 != null)
      {
        localFormObject = (FormObject)localObject4;
        boolean[] arrayOfBoolean = localFormObject.getCharacteristics();
        if ((arrayOfBoolean[1] == 0) && ((!bool1) || (arrayOfBoolean[2] != 0)))
        {
          checkGUIObjectResolved(localFormObject);
          localComponent = (Component)localFormObject.getGUIComponent();
          if ((localComponent != null) && (localComponent.isVisible()))
          {
            Rectangle localRectangle = localFormObject.getBoundingRectangle();
            float f = localRectangle.height;
            int j = localComponent.getPreferredSize().height + 6;
            if ((this.componentsToIgnore == null) || ((!this.componentsToIgnore.containsKey(Integer.valueOf(localFormObject.getParameterConstant(1147962727)))) && (!this.componentsToIgnore.containsKey(Integer.valueOf(localFormObject.getParameterConstant(608780341))))))
            {
              Object localObject5;
              if (((localComponent instanceof JList)) && (((JList)localComponent).getSelectedIndex() != -1) && (f < j))
              {
                JList localJList = (JList)localComponent;
                this.dummyPanel.add(localComponent);
                localObject5 = localJList.getModel();
                Object[] arrayOfObject = new Object[((ListModel)localObject5).getSize()];
                int k = localJList.getSelectedIndex();
                int m = 0;
                arrayOfObject[(m++)] = ((ListModel)localObject5).getElementAt(k);
                for (int n = 0; n < arrayOfObject.length; n++)
                  if (n != k)
                    arrayOfObject[(m++)] = ((ListModel)localObject5).getElementAt(n);
                localJList.setListData(arrayOfObject);
                localJList.setSelectedIndex(0);
                renderComponent((Graphics2D)localObject1, localFormObject, localJList, paramInt3, false, paramInt2, bool1);
                this.dummyPanel.remove(localJList);
              }
              else
              {
                boolean bool2 = false;
                if (this.customFormPrint != null)
                {
                  scaleComponent(localFormObject, 1.0F, this.rotation, localComponent, false, this.indent);
                  bool2 = this.customFormPrint.print((Graphics2D)localObject1, localFormObject, this);
                }
                if (!bool2)
                {
                  if ((localComponent instanceof AbstractButton))
                  {
                    localObject5 = ((AbstractButton)localComponent).getIcon();
                    if (localObject5 != null)
                      if ((localObject5 instanceof FixImageIcon))
                        ((FixImageIcon)localObject5).setPrinting(true, 1);
                      else if ((readOnlyScaling > 0) && ((localObject5 instanceof ReadOnlyTextIcon)))
                        ((ReadOnlyTextIcon)localObject5).setPrinting(true, readOnlyScaling);
                  }
                  this.dummyPanel.add(localComponent);
                  renderComponent((Graphics2D)localObject1, localFormObject, localComponent, paramInt3, false, paramInt2, bool1);
                  this.dummyPanel.remove(localComponent);
                  if ((localComponent instanceof AbstractButton))
                  {
                    localObject5 = ((AbstractButton)localComponent).getIcon();
                    if ((localObject5 instanceof FixImageIcon))
                      ((FixImageIcon)localObject5).setPrinting(false, 1);
                    else if ((localObject5 instanceof ReadOnlyTextIcon))
                      ((ReadOnlyTextIcon)localObject5).setPrinting(false, 1);
                  }
                }
              }
            }
          }
        }
      }
    }
    ((Graphics2D)localObject1).setTransform((AffineTransform)localObject2);
    if ((this.currentPage == paramInt1) && (this.panel != null))
      resetScaledLocation(this.displayScaling, this.rotation, this.indent);
  }

  private void renderFormsOntoG2WithHTML(Object paramObject, int paramInt1, int paramInt2, int paramInt3, Map paramMap, FormFactory paramFormFactory, int paramInt4)
  {
    this.componentsToIgnore = paramMap;
    Iterator localIterator = this.formsOrdered[paramInt1].iterator();
    while (localIterator.hasNext())
    {
      Object localObject = localIterator.next();
      if (localObject != null)
      {
        FormObject localFormObject = (FormObject)localObject;
        checkGUIObjectResolved(localFormObject);
      }
    }
  }

  private void scaleComponent(FormObject paramFormObject, float paramFloat, int paramInt1, Component paramComponent, boolean paramBoolean, int paramInt2)
  {
    if ((paramComponent == null) || (paramFormObject.getPageNumber() == -1))
      return;
    int i = paramFormObject.getPageNumber();
    Object localObject;
    if (this.layers != null)
    {
      localObject = paramFormObject.getLayerName();
      if ((localObject != null) && (this.layers.isLayerName((String)localObject)))
      {
        boolean bool1 = this.layers.isVisible((String)localObject);
        paramComponent.setVisible(bool1);
      }
    }
    if ((paramComponent instanceof JInternalFrame))
      localObject = cropComponent(paramFormObject, paramFloat, paramInt1, paramBoolean, true);
    else
      localObject = cropComponent(paramFormObject, paramFloat, paramInt1, paramBoolean, false);
    Font localFont = paramComponent.getFont();
    if (localFont != null)
      recalcFontSize(paramFloat, paramInt1, paramFormObject, paramComponent);
    if (((paramComponent instanceof JComponent)) && (((JComponent)paramComponent).getBorder() != null) && (paramFormObject != null))
      ((JComponent)paramComponent).setBorder((Border)generateBorderfromForm(paramFormObject, paramFloat));
    if (this.xReached != null)
    {
      localObject[0] += this.xReached[i];
      localObject[1] += this.yReached[i];
    }
    int j;
    if ((this.pageData.getRotation(i) + paramInt1) % 180 == 90)
      j = this.pageData.getCropBoxHeight(i);
    else
      j = this.pageData.getCropBoxWidth(i);
    if (this.displayView == 2)
    {
      double d;
      if ((paramInt1 == 0) || (paramInt1 == 180))
        d = (this.widestPageNR - j) / 2;
      else
        d = (this.widestPageR - j) / 2;
      paramInt2 = (int)(paramInt2 + d * paramFloat);
    }
    int k = this.userX + paramInt2 + this.insetW;
    int m = this.userY + this.insetH;
    Rectangle localRectangle = new Rectangle(k + localObject[0], m + localObject[1], localObject[2], localObject[3]);
    paramComponent.setBounds(localRectangle);
    if ((paramComponent instanceof AbstractButton))
    {
      AbstractButton localAbstractButton = (AbstractButton)paramComponent;
      Icon localIcon = localAbstractButton.getIcon();
      boolean bool2 = false;
      if ((this.displayView == 1) || (this.displayView == 0))
        bool2 = true;
      int n = paramInt1;
      if ((localIcon instanceof FixImageIcon))
        ((FixImageIcon)localIcon).setAttributes(paramComponent.getWidth(), paramComponent.getHeight(), n, bool2);
      else if ((localIcon instanceof ReadOnlyTextIcon))
        ((ReadOnlyTextIcon)localIcon).setAttributes(paramComponent.getWidth(), paramComponent.getHeight(), n, bool2);
      localIcon = localAbstractButton.getPressedIcon();
      if ((localIcon instanceof FixImageIcon))
        ((FixImageIcon)localIcon).setAttributes(paramComponent.getWidth(), paramComponent.getHeight(), n, bool2);
      localIcon = localAbstractButton.getSelectedIcon();
      if ((localIcon instanceof FixImageIcon))
        ((FixImageIcon)localIcon).setAttributes(paramComponent.getWidth(), paramComponent.getHeight(), n, bool2);
      localIcon = localAbstractButton.getRolloverIcon();
      if ((localIcon instanceof FixImageIcon))
        ((FixImageIcon)localIcon).setAttributes(paramComponent.getWidth(), paramComponent.getHeight(), n, bool2);
      localIcon = localAbstractButton.getRolloverSelectedIcon();
      if ((localIcon instanceof FixImageIcon))
        ((FixImageIcon)localIcon).setAttributes(paramComponent.getWidth(), paramComponent.getHeight(), n, bool2);
    }
  }

  private static void recalcFontSize(float paramFloat, int paramInt, FormObject paramFormObject, Component paramComponent)
  {
    int i = GUIData.getFontSize(paramFormObject, paramInt, paramFloat);
    Font localFont1 = paramComponent.getFont();
    Font localFont2 = new Font(localFont1.getFontName(), localFont1.getStyle(), i);
    paramComponent.setFont(localFont2);
  }

  public void setAutoFontSize(FormObject paramFormObject)
  {
    recalcFontSize(this.displayScaling, this.rotation, paramFormObject, (Component)paramFormObject.getGUIComponent());
  }

  public static Object generateBorderfromForm(FormObject paramFormObject, float paramFloat)
  {
    float[] arrayOfFloat = paramFormObject.getDictionary(7451).getFloatArray(4627);
    if ((arrayOfFloat == null) && (paramFormObject.getParameterConstant(1147962727) == 1110792305))
      arrayOfFloat = paramFormObject.getFloatArray(19);
    Border localBorder = JPedalBorderFactory.createBorderStyle(paramFormObject.getDictionary(4643), FormObject.generateColor(arrayOfFloat), Color.white, paramFloat);
    return localBorder;
  }

  private int[] cropComponent(FormObject paramFormObject, float paramFloat, int paramInt, boolean paramBoolean1, boolean paramBoolean2)
  {
    Rectangle localRectangle = paramFormObject.getBoundingRectangle();
    int i = paramFormObject.getPageNumber();
    float[] arrayOfFloat = { localRectangle.x, localRectangle.y, localRectangle.width + localRectangle.x, localRectangle.height + localRectangle.y };
    if ((this.displayView != 1) && (this.displayView != 0))
      paramInt = (paramInt + this.pageData.getRotation(i)) % 360;
    int j = this.pageData.getCropBoxX(i);
    int k = this.pageData.getCropBoxY(i);
    int m = this.pageData.getCropBoxWidth(i);
    int n = this.pageData.getMediaBoxWidth(i);
    int i1 = this.pageData.getMediaBoxHeight(i);
    int i2 = n - m - j;
    float f1 = 0.0F;
    float f2 = 0.0F;
    float f3 = 0.0F;
    float f4 = 0.0F;
    switch (paramInt)
    {
    case 0:
      f1 = arrayOfFloat[0];
      if (paramBoolean1)
        f1 -= j;
      f2 = i1 - arrayOfFloat[3] - this.cropOtherY[i];
      f3 = arrayOfFloat[2] - arrayOfFloat[0];
      f4 = arrayOfFloat[3] - arrayOfFloat[1];
      break;
    case 90:
      f1 = arrayOfFloat[1] - k;
      f2 = arrayOfFloat[0] - j;
      f3 = arrayOfFloat[3] - arrayOfFloat[1];
      f4 = arrayOfFloat[2] - arrayOfFloat[0];
      break;
    case 180:
      f3 = arrayOfFloat[2] - arrayOfFloat[0];
      f4 = arrayOfFloat[3] - arrayOfFloat[1];
      f2 = arrayOfFloat[1] - k;
      f1 = n - arrayOfFloat[2] - i2;
      break;
    case 270:
      f3 = arrayOfFloat[3] - arrayOfFloat[1];
      f4 = arrayOfFloat[2] - arrayOfFloat[0];
      f1 = i1 - arrayOfFloat[3] - this.cropOtherY[i];
      f2 = n - arrayOfFloat[2] - i2;
    }
    int i3 = (int)(f1 * paramFloat);
    int i4 = (int)(f2 * paramFloat);
    int i5;
    int i6;
    if (!paramBoolean2)
    {
      i5 = (int)(f3 * paramFloat);
      i6 = (int)(f4 * paramFloat);
    }
    else
    {
      i5 = (int)(f3 * this.dpi / 72.0F);
      i6 = (int)(f4 * this.dpi / 72.0F);
    }
    return new int[] { i3, i4, i5, i6 };
  }

  protected void removeAllComponentsFromScreen()
  {
    if (this.panel != null)
      if (SwingUtilities.isEventDispatchThread())
      {
        this.panel.removeAll();
      }
      else
      {
        Runnable local2 = new Runnable()
        {
          public void run()
          {
            SwingData.this.panel.removeAll();
          }
        };
        SwingUtilities.invokeLater(local2);
      }
  }

  public void setRootDisplayComponent(final Object paramObject)
  {
    if (SwingUtilities.isEventDispatchThread())
    {
      this.panel = ((JPanel)paramObject);
    }
    else
    {
      Runnable local3 = new Runnable()
      {
        public void run()
        {
          SwingData.this.panel = ((JPanel)paramObject);
        }
      };
      SwingUtilities.invokeLater(local3);
    }
  }

  public void setGUIComp(FormObject paramFormObject, Object paramObject)
  {
    Component localComponent = (Component)paramObject;
    String str1 = paramFormObject.getTextStreamValue(36);
    if (str1 != null)
    {
      String str2 = paramFormObject.getNormalOnState();
      if ((str2 != null) && (!str2.isEmpty()))
        str1 = str1 + "-(" + str2 + ')';
      localComponent.setName(str1);
    }
    scaleComponent(paramFormObject, this.displayScaling, this.rotation, localComponent, true, this.indent);
  }

  public void resetScaledLocation(final float paramFloat, final int paramInt1, int paramInt2)
  {
    if ((this.formsUnordered == null) || (this.formsUnordered[this.startPage] == null) || (this.panel == null))
      return;
    if ((this.forceRedraw) || (paramFloat != this.lastScaling) || (paramInt1 != this.oldRotation) || (paramInt2 != this.oldIndent))
    {
      this.oldRotation = paramInt1;
      this.lastScaling = paramFloat;
      this.oldIndent = paramInt2;
      this.forceRedraw = false;
      int i = this.formsUnordered[this.startPage].size();
      for (int j = 0; j < i; j++)
      {
        FormObject localFormObject1 = (FormObject)this.formsUnordered[this.startPage].get(i - 1 - j);
        Component localComponent = (Component)localFormObject1.getGUIComponent();
        Object localObject;
        if ((localComponent != null) && (localFormObject1.getBoundingRectangle().height < localComponent.getPreferredSize().height) && ((localComponent instanceof JList)))
        {
          localObject = (JList)localComponent;
          localComponent = wrapComponentInScrollPane((JList)localObject);
          localFormObject1.setGUIComponent(localObject);
          int k = ((JList)localObject).getSelectedIndex();
          if (k > -1)
            ((JList)localObject).ensureIndexIsVisible(k);
        }
        if (SwingUtilities.isEventDispatchThread())
        {
          if (localComponent != null)
          {
            this.panel.remove(localComponent);
            scaleComponent(localFormObject1, paramFloat, paramInt1, localComponent, true, this.indent);
            this.panel.add(localComponent);
          }
        }
        else
        {
          localObject = localComponent;
          final FormObject localFormObject2 = localFormObject1;
          Runnable local4 = new Runnable()
          {
            public void run()
            {
              SwingData.this.panel.remove(this.val$finalComp);
              SwingData.this.scaleComponent(localFormObject2, paramFloat, paramInt1, this.val$finalComp, true, SwingData.this.indent);
              SwingData.this.panel.add(this.val$finalComp);
            }
          };
          SwingUtilities.invokeLater(local4);
        }
      }
    }
  }

  private static Component wrapComponentInScrollPane(JList paramJList)
  {
    JScrollPane localJScrollPane = new JScrollPane(paramJList);
    localJScrollPane.setVerticalScrollBarPolicy(20);
    localJScrollPane.setHorizontalScrollBarPolicy(30);
    localJScrollPane.setLocation(paramJList.getLocation());
    localJScrollPane.setPreferredSize(paramJList.getPreferredSize());
    localJScrollPane.setSize(paramJList.getSize());
    return localJScrollPane;
  }

  protected void displayComponent(final FormObject paramFormObject, final Object paramObject)
  {
    if (SwingUtilities.isEventDispatchThread())
    {
      scaleComponent(paramFormObject, this.displayScaling, this.rotation, (Component)paramObject, true, this.indent);
    }
    else
    {
      Runnable local5 = new Runnable()
      {
        public void run()
        {
          SwingData.this.scaleComponent(paramFormObject, SwingData.this.displayScaling, SwingData.this.rotation, (Component)paramObject, true, SwingData.this.indent);
        }
      };
      SwingUtilities.invokeLater(local5);
    }
  }

  public void setCustomPrintInterface(CustomFormPrint paramCustomFormPrint)
  {
    this.customFormPrint = paramCustomFormPrint;
  }

  public List[] getFormList(boolean paramBoolean)
  {
    if (paramBoolean)
      return this.formsOrdered;
    return this.formsUnordered;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.acroforms.SwingData
 * JD-Core Version:    0.6.2
 */