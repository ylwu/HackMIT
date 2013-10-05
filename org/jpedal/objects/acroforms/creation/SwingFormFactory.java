package org.jpedal.objects.acroforms.creation;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseListener;
import java.awt.geom.CubicCurve2D.Double;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.border.Border;
import javax.swing.text.Document;
import org.jpedal.color.DeviceCMYKColorSpace;
import org.jpedal.color.PdfPaint;
import org.jpedal.objects.PdfPageData;
import org.jpedal.objects.acroforms.GUIData;
import org.jpedal.objects.acroforms.SwingData;
import org.jpedal.objects.acroforms.actions.ActionHandler;
import org.jpedal.objects.acroforms.actions.SwingDownIconListener;
import org.jpedal.objects.acroforms.actions.SwingFormButtonListener;
import org.jpedal.objects.acroforms.actions.SwingListener;
import org.jpedal.objects.acroforms.overridingImplementations.FixImageIcon;
import org.jpedal.objects.acroforms.overridingImplementations.PdfSwingPopup;
import org.jpedal.objects.acroforms.overridingImplementations.ReadOnlyTextIcon;
import org.jpedal.objects.raw.FormObject;
import org.jpedal.objects.raw.PdfObject;
import org.jpedal.parser.DecoderOptions;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.Strip;

public class SwingFormFactory extends GenericFormFactory
  implements FormFactory
{
  public Object annotationButton(FormObject paramFormObject)
  {
    JButton localJButton = new JButton();
    JComponent localJComponent = null;
    setupButton(localJButton, paramFormObject);
    setupUniversalFeatures(localJButton, paramFormObject);
    int i = paramFormObject.getParameterConstant(1147962727);
    int j = 1;
    Object localObject1;
    Object localObject2;
    Object localObject3;
    BufferedImage localBufferedImage2;
    Graphics localGraphics;
    int k;
    int m;
    int n;
    int i1;
    int i2;
    Rectangle localRectangle;
    switch (i)
    {
    case 1061176672:
      localJComponent = (JComponent)getPopupComponent(paramFormObject, this.pageData.getCropBoxWidth(paramFormObject.getPageNumber()));
      paramFormObject.setGUIComponent(localJComponent);
      localJComponent.setVisible(paramFormObject.getBoolean(524301630));
      break;
    case 607471684:
      String str = paramFormObject.getTextStreamValue(506543413);
      if ((str != null) && (str.equals("Comment")))
        try
        {
          BufferedImage localBufferedImage1 = ImageIO.read(getClass().getResource("/org/jpedal/objects/acroforms/res/comment.png"));
          localJButton.setIcon(new FixImageIcon(localBufferedImage1, 0));
        }
        catch (Exception localException1)
        {
          if (LogWriter.isOutput())
            LogWriter.writeLog("Exception: " + localException1.getMessage());
        }
      break;
    case 980909433:
      localJButton.setText("<html>" + paramFormObject.getTextStreamValue(1216184967) + "</html>");
      Font localFont = new Font("TimesRoman", 0, 12);
      paramFormObject.setTextSize(12);
      localJButton.setFont(localFont);
      paramFormObject.setTextFont(localFont);
      break;
    case 1919840408:
      if (j != 0)
      {
        localObject1 = paramFormObject.getFloatArray(19);
        localObject2 = new Color(0);
        if (localObject1 != null)
          switch (localObject1.length)
          {
          case 0:
            break;
          case 1:
            localObject2 = new Color(localObject1[0], localObject1[0], localObject1[0], 0.5F);
            break;
          case 3:
            localObject2 = new Color(localObject1[0], localObject1[1], localObject1[2], 0.5F);
            break;
          case 4:
            localObject3 = new DeviceCMYKColorSpace();
            ((DeviceCMYKColorSpace)localObject3).setColor((float[])localObject1, 4);
            localObject2 = new Color(((DeviceCMYKColorSpace)localObject3).getColor().getRGB());
            localObject2 = new Color(((Color)localObject2).getRed(), ((Color)localObject2).getGreen(), ((Color)localObject2).getBlue(), 0.5F);
            break;
          case 2:
          }
        localObject3 = paramFormObject.getFloatArray(1785890247);
        if (localObject3 == null)
          localObject3 = paramFormObject.getFloatArray(573911876);
        localBufferedImage2 = new BufferedImage(paramFormObject.getBoundingRectangle().width, paramFormObject.getBoundingRectangle().height, 6);
        localGraphics = localBufferedImage2.getGraphics();
        if (localObject3.length >= 8)
          for (k = 0; k != localObject3.length; k += 8)
          {
            m = (int)localObject3[k] - paramFormObject.getBoundingRectangle().x;
            n = (int)localObject3[(k + 5)] - paramFormObject.getBoundingRectangle().y;
            n = paramFormObject.getBoundingRectangle().height - n - (int)(localObject3[(k + 1)] - localObject3[(k + 5)]);
            i1 = (int)(localObject3[(k + 2)] - localObject3[k]);
            i2 = (int)(localObject3[(k + 1)] - localObject3[(k + 5)]);
            localRectangle = new Rectangle(m, n, i1, i2);
            try
            {
              localGraphics.setColor((Color)localObject2);
              localGraphics.fillRect(localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height);
              localJButton.setBackground(new Color(0, 0, 0, 0));
              localJButton.setIcon(new FixImageIcon(localBufferedImage2, 0));
            }
            catch (Exception localException2)
            {
              if (LogWriter.isOutput())
                LogWriter.writeLog("Exception: " + localException2.getMessage());
            }
          }
      }
      break;
    case 2053993372:
      if (j != 0)
      {
        localJButton.setBounds(paramFormObject.getBoundingRectangle());
        localObject1 = paramFormObject.getFloatArray(19);
        localObject2 = new Color(0);
        if (localObject1 != null)
          switch (localObject1.length)
          {
          case 0:
            break;
          case 1:
            localObject2 = new Color(localObject1[0], localObject1[0], localObject1[0], 1.0F);
            break;
          case 3:
            localObject2 = new Color(localObject1[0], localObject1[1], localObject1[2], 1.0F);
            break;
          case 4:
            localObject3 = new DeviceCMYKColorSpace();
            ((DeviceCMYKColorSpace)localObject3).setColor((float[])localObject1, 4);
            localObject2 = new Color(((DeviceCMYKColorSpace)localObject3).getColor().getRGB());
            break;
          case 2:
          }
        localObject3 = paramFormObject.getFloatArray(1785890247);
        if (localObject3 == null)
          localObject3 = paramFormObject.getFloatArray(573911876);
        localBufferedImage2 = new BufferedImage(paramFormObject.getBoundingRectangle().width, paramFormObject.getBoundingRectangle().height, 6);
        localGraphics = localBufferedImage2.getGraphics();
        if (localObject3.length >= 8)
          for (k = 0; k != localObject3.length; k += 8)
          {
            m = (int)localObject3[k] - paramFormObject.getBoundingRectangle().x;
            n = (int)localObject3[(k + 5)] - paramFormObject.getBoundingRectangle().y;
            n = paramFormObject.getBoundingRectangle().height - n - (int)(localObject3[(k + 1)] - localObject3[(k + 5)]);
            i1 = (int)(localObject3[(k + 2)] - localObject3[k]);
            i2 = (int)(localObject3[(k + 1)] - localObject3[(k + 5)]);
            localRectangle = new Rectangle(m, n, i1, i2);
            try
            {
              localGraphics.setColor(new Color(0.0F, 0.0F, 0.0F, 0.0F));
              localGraphics.fillRect(localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height);
              localGraphics.setColor((Color)localObject2);
              localGraphics.fillRect(localRectangle.x, localRectangle.y + localRectangle.height - 1, localRectangle.width, 1);
              localJButton.setBackground(new Color(0, 0, 0, 0));
              localJButton.setIcon(new FixImageIcon(localBufferedImage2, 0));
            }
            catch (Exception localException3)
            {
              if (LogWriter.isOutput())
                LogWriter.writeLog("Exception: " + localException3.getMessage());
            }
          }
      }
      break;
    case 1654331:
      if (!paramFormObject.isAppearanceUsed())
      {
        localJButton.setToolTipText(paramFormObject.getTextStreamValue(1216184967));
        localObject1 = paramFormObject.getObjectArray(475169151);
        localObject2 = scanInkListTree((Object[])localObject1, paramFormObject, null);
        paramFormObject.setFloatArray(573911876, new float[] { localObject2[0], localObject2[1], localObject2[2], localObject2[3] });
        localObject3 = new BufferedImage(paramFormObject.getBoundingRectangle().width, paramFormObject.getBoundingRectangle().height, 6);
        scanInkListTree((Object[])localObject1, paramFormObject, ((BufferedImage)localObject3).getGraphics());
        localJButton.setBackground(new Color(0, 0, 0, 0));
        localJButton.setIcon(new FixImageIcon((BufferedImage)localObject3, 0));
      }
      break;
    case 2036432546:
      if (j != 0)
      {
        localObject1 = paramFormObject.getFloatArray(19);
        localObject2 = new Color(0);
        if (localObject1 != null)
          switch (localObject1.length)
          {
          case 0:
            break;
          case 1:
            localObject2 = new Color(localObject1[0], localObject1[0], localObject1[0], 1.0F);
            break;
          case 3:
            localObject2 = new Color(localObject1[0], localObject1[1], localObject1[2], 1.0F);
            break;
          case 4:
            localObject3 = new DeviceCMYKColorSpace();
            ((DeviceCMYKColorSpace)localObject3).setColor((float[])localObject1, 4);
            localObject2 = new Color(((DeviceCMYKColorSpace)localObject3).getColor().getRGB());
            break;
          case 2:
          }
        localObject3 = paramFormObject.getFloatArray(1785890247);
        if (localObject3 == null)
          localObject3 = paramFormObject.getFloatArray(573911876);
        localBufferedImage2 = new BufferedImage(paramFormObject.getBoundingRectangle().width, paramFormObject.getBoundingRectangle().height, 6);
        localGraphics = localBufferedImage2.getGraphics();
        if (localObject3.length >= 8)
          for (k = 0; k != localObject3.length; k += 8)
          {
            m = (int)localObject3[k] - paramFormObject.getBoundingRectangle().x;
            n = (int)localObject3[(k + 5)] - paramFormObject.getBoundingRectangle().y;
            n = paramFormObject.getBoundingRectangle().height - n - (int)(localObject3[(k + 1)] - localObject3[(k + 5)]);
            i1 = (int)(localObject3[(k + 2)] - localObject3[k]);
            i2 = (int)(localObject3[(k + 1)] - localObject3[(k + 5)]);
            localRectangle = new Rectangle(m, n, i1, i2);
            try
            {
              localGraphics.setColor(new Color(0.0F, 0.0F, 0.0F, 0.0F));
              localGraphics.fillRect(0, 0, localRectangle.width, localRectangle.height);
              localGraphics.setColor((Color)localObject2);
              localGraphics.fillRect(localRectangle.x, localRectangle.y + localRectangle.height / 2, localRectangle.width, 1);
              localJButton.setBackground(new Color(0, 0, 0, 0));
              localJButton.setIcon(new FixImageIcon(localBufferedImage2, 0));
            }
            catch (Exception localException4)
            {
              if (LogWriter.isOutput())
                LogWriter.writeLog("Exception: " + localException4.getMessage());
            }
          }
      }
      break;
    }
    if (localJComponent == null)
      return localJButton;
    return localJComponent;
  }

  private float[] curveInk(float[] paramArrayOfFloat)
  {
    double d9 = 1.0D;
    int i = 0;
    float[] arrayOfFloat = new float[(paramArrayOfFloat.length - 2) / 2 * 8];
    for (int j = 0; j < paramArrayOfFloat.length; j += 2)
    {
      if (j == 0)
      {
        d1 = paramArrayOfFloat[j];
        d2 = paramArrayOfFloat[(j + 1)];
        d3 = paramArrayOfFloat[j];
        d4 = paramArrayOfFloat[(j + 1)];
        d5 = paramArrayOfFloat[(j + 2)];
        d6 = paramArrayOfFloat[(j + 3)];
        d7 = paramArrayOfFloat[(j + 4)];
        d8 = paramArrayOfFloat[(j + 5)];
        arrayOfDouble = findControlPoint(d1, d2, d3, d4, d5, d6, d7, d8, d9);
        arrayOfFloat[i] = ((float)d3);
        i++;
        arrayOfFloat[i] = ((float)d4);
        i++;
        arrayOfFloat[i] = ((float)arrayOfDouble[0]);
        i++;
        arrayOfFloat[i] = ((float)arrayOfDouble[1]);
        i++;
        arrayOfFloat[i] = ((float)arrayOfDouble[2]);
        i++;
        arrayOfFloat[i] = ((float)arrayOfDouble[3]);
        i++;
        arrayOfFloat[i] = ((float)d5);
        i++;
        arrayOfFloat[i] = ((float)d6);
        i++;
      }
      if (j + 6 >= paramArrayOfFloat.length)
      {
        d1 = paramArrayOfFloat[j];
        d2 = paramArrayOfFloat[(j + 1)];
        d3 = paramArrayOfFloat[(j + 2)];
        d4 = paramArrayOfFloat[(j + 3)];
        d5 = paramArrayOfFloat[(j + 4)];
        d6 = paramArrayOfFloat[(j + 5)];
        d7 = paramArrayOfFloat[(j + 4)];
        d8 = paramArrayOfFloat[(j + 5)];
        arrayOfDouble = findControlPoint(d1, d2, d3, d4, d5, d6, d7, d8, d9);
        arrayOfFloat[i] = ((float)d3);
        i++;
        arrayOfFloat[i] = ((float)d4);
        i++;
        arrayOfFloat[i] = ((float)arrayOfDouble[0]);
        i++;
        arrayOfFloat[i] = ((float)arrayOfDouble[1]);
        i++;
        arrayOfFloat[i] = ((float)arrayOfDouble[2]);
        i++;
        arrayOfFloat[i] = ((float)arrayOfDouble[3]);
        i++;
        arrayOfFloat[i] = ((float)d5);
        i++;
        arrayOfFloat[i] = ((float)d6);
        break;
      }
      double d1 = paramArrayOfFloat[j];
      double d2 = paramArrayOfFloat[(j + 1)];
      double d3 = paramArrayOfFloat[(j + 2)];
      double d4 = paramArrayOfFloat[(j + 3)];
      double d5 = paramArrayOfFloat[(j + 4)];
      double d6 = paramArrayOfFloat[(j + 5)];
      double d7 = paramArrayOfFloat[(j + 6)];
      double d8 = paramArrayOfFloat[(j + 7)];
      double[] arrayOfDouble = findControlPoint(d1, d2, d3, d4, d5, d6, d7, d8, d9);
      arrayOfFloat[i] = ((float)d3);
      i++;
      arrayOfFloat[i] = ((float)d4);
      i++;
      arrayOfFloat[i] = ((float)arrayOfDouble[0]);
      i++;
      arrayOfFloat[i] = ((float)arrayOfDouble[1]);
      i++;
      arrayOfFloat[i] = ((float)arrayOfDouble[2]);
      i++;
      arrayOfFloat[i] = ((float)arrayOfDouble[3]);
      i++;
      arrayOfFloat[i] = ((float)d5);
      i++;
      arrayOfFloat[i] = ((float)d6);
      i++;
    }
    return arrayOfFloat;
  }

  private static double[] findControlPoint(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6, double paramDouble7, double paramDouble8, double paramDouble9)
  {
    double d1 = (paramDouble1 + paramDouble3) / 2.0D;
    double d2 = (paramDouble2 + paramDouble4) / 2.0D;
    double d3 = (paramDouble3 + paramDouble5) / 2.0D;
    double d4 = (paramDouble4 + paramDouble6) / 2.0D;
    double d5 = (paramDouble5 + paramDouble7) / 2.0D;
    double d6 = (paramDouble6 + paramDouble8) / 2.0D;
    double d7 = Math.sqrt((paramDouble3 - paramDouble1) * (paramDouble3 - paramDouble1) + (paramDouble4 - paramDouble2) * (paramDouble4 - paramDouble2));
    double d8 = Math.sqrt((paramDouble5 - paramDouble3) * (paramDouble5 - paramDouble3) + (paramDouble6 - paramDouble4) * (paramDouble6 - paramDouble4));
    double d9 = Math.sqrt((paramDouble7 - paramDouble5) * (paramDouble7 - paramDouble5) + (paramDouble8 - paramDouble6) * (paramDouble8 - paramDouble6));
    double d10 = d7 / (d7 + d8);
    double d11 = d8 / (d8 + d9);
    double d12 = d1 + (d3 - d1) * d10;
    double d13 = d2 + (d4 - d2) * d10;
    double d14 = d3 + (d5 - d3) * d11;
    double d15 = d4 + (d6 - d4) * d11;
    double d16 = d12 + (d3 - d12) * paramDouble9 + paramDouble3 - d12;
    double d17 = d13 + (d4 - d13) * paramDouble9 + paramDouble4 - d13;
    double d18 = d14 + (d3 - d14) * paramDouble9 + paramDouble5 - d14;
    double d19 = d15 + (d4 - d15) * paramDouble9 + paramDouble6 - d15;
    return new double[] { d16, d17, d18, d19 };
  }

  private float[] scanInkListTree(Object[] paramArrayOfObject, FormObject paramFormObject, Graphics paramGraphics)
  {
    float f1 = 0.0F;
    float f2 = 0.0F;
    float f3 = 0.0F;
    float f4 = 0.0F;
    float[] arrayOfFloat1 = null;
    Graphics2D localGraphics2D = (Graphics2D)paramGraphics;
    int k;
    float f7;
    if (paramArrayOfObject != null)
    {
      int i = paramArrayOfObject.length;
      k = 1;
      Object localObject1;
      Object localObject2;
      if (paramGraphics != null)
      {
        float[] arrayOfFloat3 = paramFormObject.getFloatArray(19);
        localObject1 = new Color(0);
        if (arrayOfFloat3 != null)
          switch (arrayOfFloat3.length)
          {
          case 0:
            break;
          case 1:
            localObject1 = new Color(arrayOfFloat3[0], arrayOfFloat3[0], arrayOfFloat3[0], 1.0F);
            break;
          case 3:
            localObject1 = new Color(arrayOfFloat3[0], arrayOfFloat3[1], arrayOfFloat3[2], 1.0F);
            break;
          case 4:
            localObject2 = new DeviceCMYKColorSpace();
            ((DeviceCMYKColorSpace)localObject2).setColor(arrayOfFloat3, 4);
            localObject1 = new Color(((DeviceCMYKColorSpace)localObject2).getColor().getRGB());
            break;
          case 2:
          }
        localGraphics2D.setColor(new Color(0.0F, 0.0F, 0.0F, 0.0F));
        localGraphics2D.fillRect(0, 0, paramFormObject.getBoundingRectangle().width, paramFormObject.getBoundingRectangle().height);
        localGraphics2D.setColor((Color)localObject1);
        localGraphics2D.setPaint((Paint)localObject1);
      }
      for (int m = 0; m < i; m++)
        if ((paramArrayOfObject[m] instanceof byte[]))
        {
          localObject1 = (byte[])paramArrayOfObject[m];
          if (arrayOfFloat1 == null)
            arrayOfFloat1 = new float[i];
          if (localObject1 != null)
          {
            localObject2 = new String((byte[])localObject1);
            float f8 = Float.parseFloat((String)localObject2);
            switch (m % 2)
            {
            case 0:
              if (k != 0)
              {
                f1 = f8;
                f3 = f8;
              }
              else
              {
                if (f8 < f1)
                  f1 = f8;
                if (f8 > f3)
                  f3 = f8;
              }
              float f5 = f8 - paramFormObject.getBoundingRectangle().x;
              arrayOfFloat1[m] = f5;
              break;
            case 1:
              if (k != 0)
              {
                f2 = f8;
                f4 = f8;
                k = 0;
              }
              else
              {
                if (f8 < f2)
                  f2 = f8;
                if (f8 > f4)
                  f4 = f8;
              }
              f7 = paramFormObject.getBoundingRectangle().height - (f8 - paramFormObject.getBoundingRectangle().y);
              arrayOfFloat1[m] = f7;
            }
          }
        }
        else
        {
          localObject1 = scanInkListTree((Object[])paramArrayOfObject[m], paramFormObject, paramGraphics);
          if (k != 0)
          {
            f1 = localObject1[0];
            f3 = localObject1[2];
            f2 = localObject1[1];
            f4 = localObject1[3];
          }
          else
          {
            if (localObject1[0] < f1)
              f1 = localObject1[0];
            if (localObject1[2] > f3)
              f3 = localObject1[2];
            if (localObject1[1] < f2)
              f2 = localObject1[1];
            if (localObject1[3] > f4)
              f4 = localObject1[3];
          }
        }
    }
    if (arrayOfFloat1 != null)
    {
      float[] arrayOfFloat2 = curveInk(arrayOfFloat1);
      for (int j = 0; j < arrayOfFloat2.length; j++)
        if (j % 2 == 0)
        {
          if (arrayOfFloat2[j] < f1)
            f1 = arrayOfFloat2[j];
          if (arrayOfFloat2[j] > f3)
            f3 = arrayOfFloat2[j];
        }
        else
        {
          if (arrayOfFloat2[j] < f2)
            f2 = arrayOfFloat2[j];
          if (arrayOfFloat2[j] > f4)
            f4 = arrayOfFloat2[j];
        }
      float f6 = 0.0F;
      f7 = 0.0F;
      if (f1 < 0.0F)
        f6 = Math.abs(f1);
      if (f2 < 0.0F)
        f7 = Math.abs(f2);
      f1 += f6;
      f3 += f6;
      f2 += f7;
      f4 += f7;
      if (localGraphics2D != null)
      {
        localGraphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        localGraphics2D.setStroke(new BasicStroke(1.52F, 1, 1));
        for (k = 0; k < arrayOfFloat2.length; k += 8)
        {
          CubicCurve2D.Double localDouble = new CubicCurve2D.Double(arrayOfFloat2[k] + f6, arrayOfFloat2[(k + 1)] + f7, arrayOfFloat2[(k + 2)] + f6, arrayOfFloat2[(k + 3)] + f7, arrayOfFloat2[(k + 4)] + f6, arrayOfFloat2[(k + 5)] + f7, arrayOfFloat2[(k + 6)] + f6, arrayOfFloat2[(k + 7)] + f7);
          localGraphics2D.draw(localDouble);
        }
      }
    }
    return new float[] { f1, f2, f3, f4 };
  }

  public Object comboBox(FormObject paramFormObject)
  {
    String[] arrayOfString = paramFormObject.getItemsList();
    JComboBox localJComboBox;
    if (arrayOfString == null)
    {
      localJComboBox = new JComboBox();
    }
    else
    {
      localJComboBox = new JComboBox(arrayOfString);
      localObject = FormObject.generateColor(paramFormObject.getDictionary(7451).getFloatArray(4631));
      if (localObject != null)
      {
        ComboColorRenderer localComboColorRenderer = new ComboColorRenderer((Color)localObject);
        localJComboBox.setRenderer(localComboColorRenderer);
      }
    }
    Object localObject = paramFormObject.getSelectedItem();
    if (paramFormObject.getValuesMap(true) != null)
      localJComboBox.setSelectedItem(paramFormObject.getValuesMap(true).get(localObject));
    else
      localJComboBox.setSelectedItem(localObject);
    int i = localJComboBox.getSelectedIndex();
    paramFormObject.setSelection(localJComboBox.getSelectedObjects(), (String)localJComboBox.getSelectedItem(), new int[] { i }, i);
    boolean[] arrayOfBoolean = paramFormObject.getFieldFlags();
    if (arrayOfBoolean[19] != 0)
      localJComboBox.setEditable(true);
    else
      localJComboBox.setEditable(false);
    setupUniversalFeatures(localJComboBox, paramFormObject);
    if (arrayOfBoolean[1] != 0)
    {
      localJComboBox.setEditable(false);
      localJComboBox.setEnabled(false);
    }
    localJComboBox.addItemListener(new ComboListener(localJComboBox, paramFormObject));
    return localJComboBox;
  }

  public Object checkBoxBut(FormObject paramFormObject)
  {
    JCheckBox localJCheckBox = new JCheckBox();
    setupButton(localJCheckBox, paramFormObject);
    setupUniversalFeatures(localJCheckBox, paramFormObject);
    if (localJCheckBox.getBorder() != null)
      localJCheckBox.setBorderPainted(true);
    boolean[] arrayOfBoolean = paramFormObject.getFieldFlags();
    if ((arrayOfBoolean != null) && (arrayOfBoolean[1] != 0))
    {
      localJCheckBox.setEnabled(false);
      localJCheckBox.setDisabledIcon(localJCheckBox.getIcon());
      localJCheckBox.setDisabledSelectedIcon(localJCheckBox.getSelectedIcon());
    }
    return localJCheckBox;
  }

  public Object listField(FormObject paramFormObject)
  {
    String[] arrayOfString = paramFormObject.getItemsList();
    JList localJList;
    if (arrayOfString != null)
      localJList = new JList(arrayOfString);
    else
      localJList = new JList();
    if (paramFormObject.getFieldFlags()[22] == 0)
      localJList.setSelectionMode(0);
    if (paramFormObject.getSelectionIndices() != null)
      localJList.setSelectedIndices(paramFormObject.getSelectionIndices());
    else if (paramFormObject.getValuesMap(true) != null)
      localJList.setSelectedValue(paramFormObject.getValuesMap(true).get(paramFormObject.getSelectedItem()), true);
    else
      localJList.setSelectedValue(paramFormObject.getSelectedItem(), true);
    paramFormObject.setSelection(localJList.getSelectedValues(), (String)localJList.getSelectedValue(), localJList.getSelectedIndices(), localJList.getSelectedIndex());
    setupUniversalFeatures(localJList, paramFormObject);
    localJList.addListSelectionListener(new ListListener(localJList, paramFormObject));
    return localJList;
  }

  public Object multiLinePassword(FormObject paramFormObject)
  {
    String str = paramFormObject.getTextString();
    int i = paramFormObject.getInt(1209815663);
    JPasswordField localJPasswordField;
    if (i != -1)
      localJPasswordField = new JPasswordField(str, i);
    else
      localJPasswordField = new JPasswordField(str);
    localJPasswordField.setEchoChar('*');
    setupUniversalFeatures(localJPasswordField, paramFormObject);
    setupTextFeatures(localJPasswordField, paramFormObject);
    boolean[] arrayOfBoolean = paramFormObject.getFieldFlags();
    if ((arrayOfBoolean != null) && (arrayOfBoolean[1] != 0))
      localJPasswordField.setEditable(false);
    setToolTip(paramFormObject, localJPasswordField);
    return localJPasswordField;
  }

  public Object multiLineText(FormObject paramFormObject)
  {
    boolean[] arrayOfBoolean1 = paramFormObject.getFieldFlags();
    boolean[] arrayOfBoolean2 = paramFormObject.getCharacteristics();
    JTextArea localJTextArea2;
    JTextArea localJTextArea1;
    if (((arrayOfBoolean1 != null) && (arrayOfBoolean1[1] != 0)) || ((arrayOfBoolean2 != null) && (arrayOfBoolean2[9] != 0)))
    {
      localJTextArea2 = new JTextArea(paramFormObject.getTextString());
      localJTextArea2.setLineWrap(true);
      localJTextArea2.setWrapStyleWord(true);
      localJTextArea2.setEditable(false);
      localJTextArea2.getDocument().addDocumentListener(new TextDocumentListener(localJTextArea2, paramFormObject));
      localJTextArea1 = localJTextArea2;
    }
    else
    {
      localJTextArea2 = new JTextArea(paramFormObject.getTextString());
      localJTextArea2.setLineWrap(true);
      localJTextArea2.setWrapStyleWord(true);
      localJTextArea2.getDocument().addDocumentListener(new TextDocumentListener(localJTextArea2, paramFormObject));
      localJTextArea1 = localJTextArea2;
    }
    setToolTip(paramFormObject, localJTextArea1);
    setupUniversalFeatures(localJTextArea1, paramFormObject);
    return localJTextArea1;
  }

  public Object signature(FormObject paramFormObject)
  {
    JButton localJButton = new JButton();
    setupButton(localJButton, paramFormObject);
    setupUniversalFeatures(localJButton, paramFormObject);
    boolean[] arrayOfBoolean = paramFormObject.getFieldFlags();
    if ((arrayOfBoolean != null) && (arrayOfBoolean[1] != 0))
    {
      localJButton.setEnabled(false);
      localJButton.setDisabledIcon(localJButton.getIcon());
      localJButton.setDisabledSelectedIcon(localJButton.getSelectedIcon());
    }
    if (!paramFormObject.isAppearanceUsed())
    {
      localJButton.setOpaque(false);
      BufferedImage localBufferedImage = new BufferedImage(1, 1, 2);
      Graphics2D localGraphics2D = localBufferedImage.createGraphics();
      localGraphics2D.setPaint(new Color(221, 228, 255, 175));
      localGraphics2D.fillRect(0, 0, 1, 1);
      localJButton.setIcon(new FixImageIcon(localBufferedImage, 0));
    }
    return localJButton;
  }

  public Object pushBut(FormObject paramFormObject)
  {
    JButton localJButton = new JButton();
    setupButton(localJButton, paramFormObject);
    setupUniversalFeatures(localJButton, paramFormObject);
    boolean[] arrayOfBoolean = paramFormObject.getFieldFlags();
    if ((arrayOfBoolean != null) && (arrayOfBoolean[1] != 0))
    {
      localJButton.setEnabled(false);
      localJButton.setDisabledIcon(localJButton.getIcon());
      localJButton.setDisabledSelectedIcon(localJButton.getSelectedIcon());
    }
    return localJButton;
  }

  public Object radioBut(FormObject paramFormObject)
  {
    JRadioButton localJRadioButton = new JRadioButton();
    setupButton(localJRadioButton, paramFormObject);
    setupUniversalFeatures(localJRadioButton, paramFormObject);
    boolean[] arrayOfBoolean = paramFormObject.getFieldFlags();
    if ((arrayOfBoolean != null) && (arrayOfBoolean[1] != 0))
    {
      localJRadioButton.setEnabled(false);
      localJRadioButton.setDisabledIcon(localJRadioButton.getIcon());
      localJRadioButton.setDisabledSelectedIcon(localJRadioButton.getSelectedIcon());
    }
    return localJRadioButton;
  }

  public Object singleLinePassword(FormObject paramFormObject)
  {
    JPasswordField localJPasswordField = new JPasswordField(paramFormObject.getTextString());
    localJPasswordField.setEchoChar('*');
    int i = paramFormObject.getInt(1209815663);
    if (i != -1)
      localJPasswordField.setColumns(i);
    setupUniversalFeatures(localJPasswordField, paramFormObject);
    setupTextFeatures(localJPasswordField, paramFormObject);
    boolean[] arrayOfBoolean = paramFormObject.getFieldFlags();
    if ((arrayOfBoolean != null) && (arrayOfBoolean[1] != 0))
      localJPasswordField.setEditable(false);
    setToolTip(paramFormObject, localJPasswordField);
    return localJPasswordField;
  }

  public Object singleLineText(FormObject paramFormObject)
  {
    boolean[] arrayOfBoolean1 = paramFormObject.getFieldFlags();
    boolean[] arrayOfBoolean2 = paramFormObject.getCharacteristics();
    String str1 = readAPimagesForText(paramFormObject);
    if ((str1 != null) && (str1.contains("&#")))
      str1 = Strip.stripXML(str1, true).toString();
    if ((str1 != null) && (!str1.equals(paramFormObject.getTextStreamValue(38))))
      paramFormObject.setTextStreamValue(38, str1);
    Object localObject2;
    Object localObject1;
    if (((arrayOfBoolean1 != null) && (arrayOfBoolean1[1] != 0)) || ((arrayOfBoolean2 != null) && (arrayOfBoolean2[9] != 0)))
    {
      localObject2 = new ReadOnlyTextIcon(0, this.currentPdfFile, this.AcroRes);
      ((ReadOnlyTextIcon)localObject2).setAlignment(paramFormObject.getAlignment());
      Object localObject3;
      if (!((ReadOnlyTextIcon)localObject2).decipherAppObject(paramFormObject))
      {
        localObject3 = new JTextField(paramFormObject.getTextString());
        setupTextFeatures((JTextField)localObject3, paramFormObject);
        setToolTip(paramFormObject, (JComponent)localObject3);
        ((JTextField)localObject3).setEditable(false);
        localObject1 = localObject3;
      }
      else
      {
        localObject3 = new JButton();
        paramFormObject.setAppreancesUsed(false);
        setupButton((AbstractButton)localObject3, paramFormObject);
        paramFormObject.setAppreancesUsed(true);
        ((JButton)localObject3).setText(null);
        String str2 = paramFormObject.getTextStreamValue(38);
        if (str2 != null)
          ((ReadOnlyTextIcon)localObject2).setText(str2);
        ((JButton)localObject3).setIcon((Icon)localObject2);
        localObject1 = localObject3;
      }
    }
    else
    {
      localObject2 = new JTextField(paramFormObject.getTextString());
      setupTextFeatures((JTextField)localObject2, paramFormObject);
      setToolTip(paramFormObject, (JComponent)localObject2);
      localObject1 = localObject2;
    }
    setupUniversalFeatures(localObject1, paramFormObject);
    return localObject1;
  }

  private void setupTextFeatures(JTextField paramJTextField, FormObject paramFormObject)
  {
    if (paramFormObject.getAlignment() != -1)
      paramJTextField.setHorizontalAlignment(paramFormObject.getAlignment());
    paramJTextField.getDocument().addDocumentListener(new TextDocumentListener(paramJTextField, paramFormObject));
  }

  private void setupButton(AbstractButton paramAbstractButton, FormObject paramFormObject)
  {
    String str1 = paramFormObject.getTextStreamValue(36);
    if (str1 == null)
      str1 = "";
    ButtonGroup localButtonGroup = (ButtonGroup)this.groups.get(str1);
    if (localButtonGroup == null)
    {
      localButtonGroup = new ButtonGroup();
      this.groups.put(str1, localButtonGroup);
      this.firstButtons.put(str1, paramAbstractButton);
    }
    else
    {
      localObject = (AbstractButton)this.firstButtons.get(str1);
      if (localObject != null)
      {
        this.firstButtons.remove(str1);
        localButtonGroup.add((AbstractButton)localObject);
      }
      localButtonGroup.add(paramAbstractButton);
    }
    Object localObject = paramFormObject.getDictionary(7451).getTextStreamValue(4881);
    paramAbstractButton.setText((String)localObject);
    paramAbstractButton.setContentAreaFilled(false);
    String str2 = paramFormObject.getDictionary(7451).getTextStreamValue(4371);
    String str3 = paramFormObject.getDictionary(7451).getTextStreamValue(8723);
    if (((str2 != null) && (!str2.isEmpty())) || ((str3 != null) && (!str3.isEmpty())))
      paramAbstractButton.addMouseListener(new SwingFormButtonListener((String)localObject, str3, str2));
    if (paramFormObject.isAppearanceUsed())
      setAPImages(paramFormObject, paramAbstractButton);
    int i = paramFormObject.getTextPosition();
    if (i != -1)
      switch (i)
      {
      case 0:
        paramAbstractButton.setIcon(null);
        paramAbstractButton.setText((String)localObject);
        break;
      case 1:
        paramAbstractButton.setText(null);
        break;
      case 2:
        paramAbstractButton.setVerticalTextPosition(3);
        break;
      case 3:
        paramAbstractButton.setVerticalTextPosition(1);
        break;
      case 4:
        paramAbstractButton.setHorizontalTextPosition(4);
        break;
      case 5:
        paramAbstractButton.setHorizontalTextPosition(2);
        break;
      case 6:
        paramAbstractButton.setText(null);
      }
    Insets localInsets = new Insets(0, 0, 0, 0);
    paramAbstractButton.setMargin(localInsets);
    paramAbstractButton.addMouseListener((MouseListener)this.formsActionHandler.setHoverCursor());
    paramAbstractButton.addChangeListener(new RadioListener(paramAbstractButton, paramFormObject));
  }

  private void setAPImages(FormObject paramFormObject, Object paramObject)
  {
    AbstractButton localAbstractButton = (AbstractButton)paramObject;
    PdfObject localPdfObject1 = paramFormObject.getDictionary(4384).getDictionary(30);
    PdfObject localPdfObject2 = paramFormObject.getDictionary(4384).getDictionary(20);
    PdfObject localPdfObject3 = paramFormObject.getDictionary(4384).getDictionary(34);
    PdfObject localPdfObject4 = null;
    Object localObject1 = null;
    Object localObject2 = null;
    Object localObject3 = null;
    PdfObject localPdfObject5 = null;
    Object localObject4 = null;
    int i = 0;
    int j = paramFormObject.getParameterConstant(1147962727);
    Object localObject5;
    Iterator localIterator;
    String str;
    PdfObject localPdfObject6;
    if ((localPdfObject1 != null) || (paramFormObject.getDictionary(7451).getDictionary(25) != null))
    {
      if (localPdfObject1.getDictionary(2045494) != null)
        localPdfObject4 = localPdfObject1.getDictionary(2045494);
      else if ((paramFormObject.getDictionary(7451).getDictionary(25) != null) && (paramFormObject.getDictionary(7451).getDictionary(6422) == null))
        localPdfObject4 = paramFormObject.getDictionary(7451).getDictionary(25);
      else if (localPdfObject1.getDecodedStream() != null)
        localPdfObject4 = localPdfObject1;
      if (localPdfObject4 != null)
      {
        localAbstractButton.setText(null);
        localAbstractButton.setIcon(new FixImageIcon(localPdfObject4, paramFormObject.getDictionary(7451).getInt(34), this.currentPdfFile, j, 0));
      }
      if (localPdfObject1.getDictionary(7998) != null)
      {
        localObject1 = localPdfObject1.getDictionary(7998);
        paramFormObject.setNormalOnState("On");
      }
      else
      {
        localObject5 = localPdfObject1.getOtherDictionaries();
        if ((localObject5 != null) && (!((Map)localObject5).isEmpty()))
        {
          localIterator = ((Map)localObject5).keySet().iterator();
          while (localIterator.hasNext())
          {
            str = (String)localIterator.next();
            localPdfObject6 = (PdfObject)((Map)localObject5).get(str);
            localObject1 = localPdfObject6;
            paramFormObject.setNormalOnState(str);
          }
        }
      }
      if (localObject1 != null)
      {
        localAbstractButton.setText(null);
        localAbstractButton.setSelectedIcon(new FixImageIcon((PdfObject)localObject1, paramFormObject.getDictionary(7451).getInt(34), this.currentPdfFile, j, 0));
        if (localAbstractButton.getIcon() == null)
          localAbstractButton.setIcon(new FixImageIcon(null, paramFormObject.getDictionary(7451).getInt(34)));
      }
    }
    if (paramFormObject.hasNoDownIcon())
    {
      localAbstractButton.setPressedIcon(localAbstractButton.getIcon());
    }
    else
    {
      if (paramFormObject.hasOffsetDownIcon())
        i = 1;
      else if (paramFormObject.hasInvertDownIcon())
        i = 2;
      if (i != 0)
        if (localPdfObject4 != null)
        {
          if (localObject1 != null)
          {
            localObject2 = localPdfObject4;
            localObject3 = localObject1;
          }
          else
          {
            localObject2 = localPdfObject4;
          }
        }
        else if (localObject1 != null)
          localObject2 = localObject1;
      if (localPdfObject2 != null)
      {
        if (localPdfObject2.getDecodedStream() != null)
          localObject2 = localPdfObject2;
        else if (localPdfObject2.getDictionary(2045494) != null)
          localObject2 = localPdfObject2.getDictionary(2045494);
        if (localPdfObject2.getDictionary(7998) != null)
        {
          localObject3 = localPdfObject2.getDictionary(7998);
        }
        else
        {
          localObject5 = localPdfObject2.getOtherDictionaries();
          if ((localObject5 != null) && (!((Map)localObject5).isEmpty()))
          {
            localIterator = ((Map)localObject5).keySet().iterator();
            while (localIterator.hasNext())
            {
              str = (String)localIterator.next();
              localPdfObject6 = (PdfObject)((Map)localObject5).get(str);
              localObject3 = localPdfObject6;
            }
          }
        }
      }
      if ((localObject2 == null) || (localObject3 == null))
      {
        if (localObject2 != null)
        {
          localAbstractButton.setText(null);
          localAbstractButton.setPressedIcon(new FixImageIcon((PdfObject)localObject2, paramFormObject.getDictionary(7451).getInt(34), this.currentPdfFile, j, i));
        }
        else if (localObject3 != null)
        {
          localAbstractButton.setText(null);
          localAbstractButton.setPressedIcon(new FixImageIcon((PdfObject)localObject3, paramFormObject.getDictionary(7451).getInt(34), this.currentPdfFile, j, i));
        }
      }
      else
      {
        localAbstractButton.setPressedIcon(new FixImageIcon((PdfObject)localObject3, (PdfObject)localObject2, paramFormObject.getDictionary(7451).getInt(34), localAbstractButton.isSelected() ? 1 : 0, this.currentPdfFile, j, i));
        localAbstractButton.addActionListener(new SwingDownIconListener());
      }
    }
    if (localPdfObject3 != null)
    {
      if (localPdfObject3.getDecodedStream() != null)
        localPdfObject5 = localPdfObject3;
      else if (localPdfObject3.getDictionary(2045494) != null)
        localPdfObject5 = localPdfObject3.getDictionary(2045494);
      if (localPdfObject5 != null)
      {
        localAbstractButton.setRolloverEnabled(true);
        localAbstractButton.setText(null);
        localAbstractButton.setRolloverIcon(new FixImageIcon(localPdfObject5, paramFormObject.getDictionary(7451).getInt(34), this.currentPdfFile, j, 0));
      }
      if (localPdfObject3.getDictionary(7998) != null)
      {
        localObject4 = localPdfObject3.getDictionary(7998);
      }
      else
      {
        localObject5 = localPdfObject3.getOtherDictionaries();
        if ((localObject5 != null) && (!((Map)localObject5).isEmpty()))
        {
          localIterator = ((Map)localObject5).keySet().iterator();
          while (localIterator.hasNext())
          {
            str = (String)localIterator.next();
            localPdfObject6 = (PdfObject)((Map)localObject5).get(str);
            localObject4 = localPdfObject6;
          }
        }
      }
      if (localObject4 != null)
      {
        localAbstractButton.setRolloverEnabled(true);
        localAbstractButton.setText(null);
        localAbstractButton.setRolloverSelectedIcon(new FixImageIcon((PdfObject)localObject4, paramFormObject.getDictionary(7451).getInt(34), this.currentPdfFile, j, 0));
        if (localAbstractButton.getRolloverIcon() == null)
          localAbstractButton.setRolloverIcon(new FixImageIcon(null, paramFormObject.getDictionary(7451).getInt(34)));
      }
    }
    if (paramFormObject.isSelected())
    {
      localAbstractButton.setSelected(true);
      if ((localAbstractButton instanceof JToggleButton))
      {
        localObject5 = localAbstractButton.getPressedIcon();
        if ((localObject5 instanceof FixImageIcon))
          ((FixImageIcon)localObject5).swapImage(true);
      }
    }
  }

  private void setupUniversalFeatures(JComponent paramJComponent, FormObject paramFormObject)
  {
    paramJComponent.setOpaque(false);
    Font localFont = paramFormObject.getTextFont();
    if (localFont != null)
      paramJComponent.setFont(localFont);
    paramJComponent.setForeground(paramFormObject.getTextColor());
    Border localBorder = (Border)SwingData.generateBorderfromForm(paramFormObject, 1.0F);
    paramJComponent.setBorder(localBorder);
    Color localColor = FormObject.generateColor(paramFormObject.getDictionary(7451).getFloatArray(4631));
    if (localColor != null)
    {
      paramJComponent.setBackground(localColor);
      paramJComponent.setOpaque(true);
    }
    else if ((DecoderOptions.isRunningOnMac) && ((paramJComponent instanceof JButton)))
    {
      ((JButton)paramJComponent).setBorderPainted(false);
      paramJComponent.setBorder(null);
    }
    setupMouseListener(paramJComponent, paramFormObject);
  }

  private void setupMouseListener(Component paramComponent, FormObject paramFormObject)
  {
    boolean[] arrayOfBoolean = paramFormObject.getCharacteristics();
    if ((arrayOfBoolean[0] != 0) || (arrayOfBoolean[1] != 0) || (arrayOfBoolean[5] != 0))
      paramComponent.setVisible(false);
    SwingListener localSwingListener = new SwingListener(paramFormObject, this.formsActionHandler);
    if ((paramComponent instanceof JComboBox))
    {
      ((JComboBox)paramComponent).getComponent(0).addMouseListener(localSwingListener);
      ((JComboBox)paramComponent).getComponent(0).addKeyListener(localSwingListener);
      ((JComboBox)paramComponent).getComponent(0).addFocusListener(localSwingListener);
      ((JComboBox)paramComponent).addActionListener(localSwingListener);
    }
    if ((paramComponent instanceof JList))
      ((JList)paramComponent).addListSelectionListener(localSwingListener);
    paramComponent.addMouseListener(localSwingListener);
    paramComponent.addMouseMotionListener(localSwingListener);
    paramComponent.addKeyListener(localSwingListener);
    paramComponent.addFocusListener(localSwingListener);
    PdfObject localPdfObject = paramFormObject.getDictionary(17);
    if ((localPdfObject != null) && (localPdfObject.getNameAsConstant(35) == 2433561))
    {
      String str1 = System.getProperty("org.jpedal.noURLaccess");
      if ((str1 == null) || (!str1.equals("true")))
      {
        String str2 = localPdfObject.getTextStreamValue(2433561);
        ((JComponent)paramComponent).setToolTipText(str2);
      }
    }
  }

  private static void setToolTip(FormObject paramFormObject, JComponent paramJComponent)
  {
    String str = paramFormObject.getTextStreamValue(9253);
    if (str != null)
      paramJComponent.setToolTipText(str);
  }

  public GUIData getCustomCompData()
  {
    return new SwingData();
  }

  public int getType()
  {
    return 1;
  }

  public void setAnnotOrder(Map paramMap)
  {
  }

  public Object getPopupComponent(FormObject paramFormObject, int paramInt)
  {
    return new PdfSwingPopup(paramFormObject, paramInt);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.acroforms.creation.SwingFormFactory
 * JD-Core Version:    0.6.2
 */