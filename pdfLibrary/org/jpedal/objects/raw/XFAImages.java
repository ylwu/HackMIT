package org.jpedal.objects.raw;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D.Double;
import java.awt.geom.Ellipse2D.Double;
import java.awt.geom.Rectangle2D.Double;
import java.awt.image.BufferedImage;

public class XFAImages
{
  public static final int BORDER_DASHED = 1;
  public static final int BORDER_DOTTED = 2;
  public static final int BORDER_DASHDOT = 3;
  public static final int BORDER_DASHDOTDOT = 4;
  public static final int BORDER_SOLID = 5;
  public static final int BORDER_LOWERED = 6;
  public static final int BORDER_RAISED = 7;
  public static final int BORDER_ETCHED = 8;
  public static final int BORDER_EMBOSSED = 9;
  private static int[] chkXPoints1 = { 2, 68, 2 };
  private static int[] chkYPoints1 = { 3, 3, 68 };
  private static int[] chkXPoints2 = { 2, 69, 69 };
  private static int[] chkYPoints2 = { 70, 70, 3 };
  private static int[] chkXPoints3 = { 5, 65, 5 };
  private static int[] chkYPoints3 = { 6, 6, 65 };
  private static int[] chkXPoints4 = { 4, 65, 66 };
  private static int[] chkYPoints4 = { 66, 66, 6 };

  static void drawCheckbox(Graphics2D paramGraphics2D, BufferedImage paramBufferedImage, FormAppearanceObject paramFormAppearanceObject)
  {
    int i = paramFormAppearanceObject.getBorderStroke();
    switch (i)
    {
    case 1:
      drawCheckboxDashed(paramGraphics2D, paramFormAppearanceObject);
      break;
    case 2:
      drawCheckboxDotted(paramGraphics2D, paramFormAppearanceObject);
      break;
    case 3:
      drawCheckboxDashDot(paramGraphics2D, paramFormAppearanceObject);
      break;
    case 4:
      drawCheckboxDashDotDot(paramGraphics2D, paramFormAppearanceObject);
      break;
    case 5:
      drawCheckboxSolid(paramGraphics2D, paramFormAppearanceObject);
      break;
    case 6:
      drawCheckboxLowered(paramGraphics2D, paramFormAppearanceObject);
      break;
    case 7:
      drawCheckboxRaised(paramGraphics2D, paramFormAppearanceObject);
      break;
    case 8:
      drawCheckboxEtched(paramGraphics2D, paramFormAppearanceObject);
      break;
    case 9:
      drawCheckboxEmbossed(paramGraphics2D, paramFormAppearanceObject);
      break;
    }
  }

  static void drawRadio(Graphics2D paramGraphics2D, BufferedImage paramBufferedImage, FormAppearanceObject paramFormAppearanceObject)
  {
    int i = paramFormAppearanceObject.getBorderStroke();
    switch (i)
    {
    case 1:
      drawRadioDashed(paramGraphics2D, paramFormAppearanceObject);
      break;
    case 2:
      drawRadioDotted(paramGraphics2D, paramFormAppearanceObject);
      break;
    case 3:
      drawRadioDashDot(paramGraphics2D, paramFormAppearanceObject);
      break;
    case 4:
      drawRadioDashDotDot(paramGraphics2D, paramFormAppearanceObject);
      break;
    case 5:
      drawRadioSolid(paramGraphics2D, paramFormAppearanceObject);
      break;
    case 6:
    case 7:
    case 8:
    case 9:
      drawRadioLowered(paramGraphics2D, paramFormAppearanceObject);
      break;
    }
  }

  private static void drawCheckboxDashed(Graphics2D paramGraphics2D, FormAppearanceObject paramFormAppearanceObject)
  {
    paramGraphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    paramGraphics2D.setStroke(new BasicStroke(3.0F, 2, 0));
    drawDashedCheckbox(paramGraphics2D, paramFormAppearanceObject, new float[] { 15.0F, 3.0F, 15.0F, 3.0F });
  }

  private static void drawCheckboxDotted(Graphics2D paramGraphics2D, FormAppearanceObject paramFormAppearanceObject)
  {
    paramGraphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    paramGraphics2D.setStroke(new BasicStroke(3.0F, 2, 0));
    drawDashedCheckbox(paramGraphics2D, paramFormAppearanceObject, new float[] { 8.0F, 3.0F, 8.0F, 3.0F });
  }

  private static void drawCheckboxDashDot(Graphics2D paramGraphics2D, FormAppearanceObject paramFormAppearanceObject)
  {
    paramGraphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    paramGraphics2D.setStroke(new BasicStroke(3.0F, 2, 0));
    drawDashedCheckbox(paramGraphics2D, paramFormAppearanceObject, new float[] { 15.0F, 3.0F, 8.0F, 3.0F });
  }

  private static void drawCheckboxDashDotDot(Graphics2D paramGraphics2D, FormAppearanceObject paramFormAppearanceObject)
  {
    paramGraphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    paramGraphics2D.setStroke(new BasicStroke(3.0F, 2, 0));
    drawDashedCheckbox(paramGraphics2D, paramFormAppearanceObject, new float[] { 15.0F, 3.0F, 8.0F, 3.0F, 8.0F, 3.0F });
  }

  private static void drawCheckboxSolid(Graphics2D paramGraphics2D, FormAppearanceObject paramFormAppearanceObject)
  {
    paramGraphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    paramGraphics2D.setStroke(new BasicStroke(3.0F, 2, 0));
    drawDashedCheckbox(paramGraphics2D, paramFormAppearanceObject, new float[] { 1.0F, 0.0F });
  }

  private static void drawCheckboxLowered(Graphics2D paramGraphics2D, FormAppearanceObject paramFormAppearanceObject)
  {
    paramGraphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    paramGraphics2D.setStroke(new BasicStroke(3.0F, 2, 0));
    paramGraphics2D.setColor(Color.BLACK);
    paramGraphics2D.fillRect(2, 3, 67, 67);
    paramGraphics2D.setColor(new Color(77, 80, 89));
    paramGraphics2D.fillPolygon(chkXPoints1, chkYPoints1, 3);
    paramGraphics2D.fillPolygon(chkXPoints2, chkYPoints2, 3);
    paramGraphics2D.setColor(new Color(161, 163, 172));
    paramGraphics2D.fillPolygon(chkXPoints3, chkYPoints3, 3);
    paramGraphics2D.setColor(new Color(215, 215, 219));
    paramGraphics2D.fillPolygon(chkXPoints4, chkYPoints4, 3);
    paramGraphics2D.setColor(new Color(222, 229, 255));
    paramGraphics2D.fill(new Rectangle2D.Double(9.0D, 10.0D, 53.0D, 53.0D));
    drawCheckboxCrossSmall(paramGraphics2D, paramFormAppearanceObject);
  }

  private static void drawCheckboxRaised(Graphics2D paramGraphics2D, FormAppearanceObject paramFormAppearanceObject)
  {
    paramGraphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    paramGraphics2D.setStroke(new BasicStroke(3.0F, 2, 0));
    paramGraphics2D.setColor(Color.BLACK);
    paramGraphics2D.fillRect(2, 3, 67, 67);
    paramGraphics2D.setColor(new Color(77, 80, 89));
    paramGraphics2D.fillPolygon(chkXPoints1, chkYPoints1, 3);
    paramGraphics2D.fillPolygon(chkXPoints2, chkYPoints2, 3);
    paramGraphics2D.setColor(new Color(243, 246, 255));
    paramGraphics2D.fillPolygon(chkXPoints3, chkYPoints3, 3);
    paramGraphics2D.setColor(new Color(161, 163, 172));
    paramGraphics2D.fillPolygon(chkXPoints4, chkYPoints4, 3);
    paramGraphics2D.setColor(new Color(222, 229, 255));
    paramGraphics2D.fill(new Rectangle2D.Double(9.0D, 10.0D, 53.0D, 53.0D));
    drawCheckboxCrossSmall(paramGraphics2D, paramFormAppearanceObject);
  }

  private static void drawCheckboxEtched(Graphics2D paramGraphics2D, FormAppearanceObject paramFormAppearanceObject)
  {
    paramGraphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    paramGraphics2D.setStroke(new BasicStroke(3.0F, 2, 0));
    paramGraphics2D.setColor(new Color(161, 163, 172));
    paramGraphics2D.fillRect(2, 3, 67, 67);
    paramGraphics2D.setColor(new Color(161, 163, 172));
    paramGraphics2D.fillPolygon(chkXPoints1, chkYPoints1, 3);
    paramGraphics2D.setColor(new Color(243, 246, 255));
    paramGraphics2D.fillPolygon(chkXPoints2, chkYPoints2, 3);
    paramGraphics2D.setColor(new Color(243, 246, 255));
    paramGraphics2D.fillPolygon(chkXPoints3, chkYPoints3, 3);
    paramGraphics2D.setColor(new Color(161, 163, 172));
    paramGraphics2D.fillPolygon(chkXPoints4, chkYPoints4, 3);
    paramGraphics2D.setColor(new Color(222, 229, 255));
    paramGraphics2D.fill(new Rectangle2D.Double(9.0D, 10.0D, 53.0D, 53.0D));
    drawCheckboxCrossSmall(paramGraphics2D, paramFormAppearanceObject);
  }

  private static void drawCheckboxEmbossed(Graphics2D paramGraphics2D, FormAppearanceObject paramFormAppearanceObject)
  {
    paramGraphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    paramGraphics2D.setStroke(new BasicStroke(3.0F, 2, 0));
    paramGraphics2D.setColor(new Color(161, 163, 172));
    paramGraphics2D.fillRect(2, 3, 67, 67);
    paramGraphics2D.setColor(new Color(161, 163, 172));
    paramGraphics2D.fillPolygon(chkXPoints1, chkYPoints1, 3);
    paramGraphics2D.setColor(new Color(77, 80, 89));
    paramGraphics2D.fillPolygon(chkXPoints2, chkYPoints2, 3);
    paramGraphics2D.setColor(new Color(77, 80, 89));
    paramGraphics2D.fillPolygon(chkXPoints3, chkYPoints3, 3);
    paramGraphics2D.setColor(new Color(161, 163, 172));
    paramGraphics2D.fillPolygon(chkXPoints4, chkYPoints4, 3);
    paramGraphics2D.setColor(new Color(222, 229, 255));
    paramGraphics2D.fill(new Rectangle2D.Double(9.0D, 10.0D, 53.0D, 53.0D));
    drawCheckboxCrossSmall(paramGraphics2D, paramFormAppearanceObject);
  }

  private static void drawRadioDashed(Graphics2D paramGraphics2D, FormAppearanceObject paramFormAppearanceObject)
  {
    float[] arrayOfFloat = { 20.0F, 4.0F };
    drawRadios(paramGraphics2D, paramFormAppearanceObject, arrayOfFloat);
  }

  private static void drawRadioDotted(Graphics2D paramGraphics2D, FormAppearanceObject paramFormAppearanceObject)
  {
    float[] arrayOfFloat = { 10.0F, 4.0F };
    drawRadios(paramGraphics2D, paramFormAppearanceObject, arrayOfFloat);
  }

  private static void drawRadioDashDot(Graphics2D paramGraphics2D, FormAppearanceObject paramFormAppearanceObject)
  {
    float[] arrayOfFloat = { 20.0F, 4.0F, 10.0F, 4.0F };
    drawRadios(paramGraphics2D, paramFormAppearanceObject, arrayOfFloat);
  }

  private static void drawRadioDashDotDot(Graphics2D paramGraphics2D, FormAppearanceObject paramFormAppearanceObject)
  {
    float[] arrayOfFloat = { 20.0F, 4.0F, 10.0F, 4.0F, 10.0F, 4.0F };
    drawRadios(paramGraphics2D, paramFormAppearanceObject, arrayOfFloat);
  }

  private static void drawRadioSolid(Graphics2D paramGraphics2D, FormAppearanceObject paramFormAppearanceObject)
  {
    float[] arrayOfFloat = { 1.0F, 0.0F };
    drawRadios(paramGraphics2D, paramFormAppearanceObject, arrayOfFloat);
  }

  private static void drawRadioLowered(Graphics2D paramGraphics2D, FormAppearanceObject paramFormAppearanceObject)
  {
    paramGraphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    paramGraphics2D.setColor(new Color(215, 215, 219));
    paramGraphics2D.setStroke(new BasicStroke(3.0F));
    paramGraphics2D.drawOval(8, 8, 54, 54);
    paramGraphics2D.setColor(new Color(222, 229, 255));
    paramGraphics2D.fill(new Ellipse2D.Double(10.0D, 10.0D, 51.0D, 51.0D));
    paramGraphics2D.setColor(new Color(117, 117, 118));
    paramGraphics2D.draw(new Arc2D.Double(8.0D, 8.0D, 54.0D, 54.0D, 45.0D, 180.0D, 0));
    paramGraphics2D.setColor(new Color(119, 122, 131));
    paramGraphics2D.draw(new Arc2D.Double(8.0D, 8.0D, 54.0D, 54.0D, 50.0D, 170.0D, 0));
    paramGraphics2D.setColor(new Color(243, 246, 255));
    paramGraphics2D.draw(new Ellipse2D.Double(4.0D, 4.0D, 61.5D, 61.5D));
    paramGraphics2D.setColor(new Color(222, 223, 226));
    paramGraphics2D.draw(new Arc2D.Double(5.0D, 5.0D, 60.5D, 60.5D, 44.0D, 181.0D, 0));
    paramGraphics2D.setColor(new Color(161, 163, 172));
    paramGraphics2D.draw(new Arc2D.Double(5.0D, 5.0D, 60.5D, 60.5D, 49.0D, 171.0D, 0));
    paramGraphics2D.setColor(new Color(226, 226, 226));
    paramGraphics2D.draw(new Arc2D.Double(4.0D, 4.0D, 62.0D, 62.0D, 44.0D, 182.0D, 0));
    paramGraphics2D.setColor(new Color(172, 172, 172));
    paramGraphics2D.draw(new Arc2D.Double(4.0D, 4.0D, 62.0D, 62.0D, 49.0D, 172.0D, 0));
    if (paramFormAppearanceObject.isSelected)
      paramGraphics2D.setColor(Color.BLACK);
    else
      paramGraphics2D.setColor(new Color(222, 229, 255));
    paramGraphics2D.fill(new Ellipse2D.Double(22.0D, 21.0D, 28.0D, 28.0D));
  }

  static void drawRollover(Graphics2D paramGraphics2D, BufferedImage paramBufferedImage, FormAppearanceObject paramFormAppearanceObject)
  {
    paramGraphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    paramGraphics2D.setStroke(new BasicStroke(6.0F, 0, 0));
    paramGraphics2D.setColor(Color.BLACK);
    paramGraphics2D.drawRect(6, 5, 60, 60);
  }

  private static void drawDashedCheckbox(Graphics2D paramGraphics2D, FormAppearanceObject paramFormAppearanceObject, float[] paramArrayOfFloat)
  {
    paramGraphics2D.setColor(new Color(222, 229, 255));
    paramGraphics2D.fillRect(3, 3, 66, 65);
    paramGraphics2D.setColor(new Color(21, 21, 21));
    paramGraphics2D.setComposite(AlphaComposite.getInstance(3, 0.6F));
    paramGraphics2D.setStroke(new BasicStroke(6.0F, 0, 0, 10.0F, paramArrayOfFloat, 0.0F));
    paramGraphics2D.drawRect(3, 3, 66, 64);
    paramGraphics2D.setComposite(AlphaComposite.getInstance(3, 1.0F));
    if (paramFormAppearanceObject.isSelected)
      paramGraphics2D.setColor(Color.BLACK);
    else
      paramGraphics2D.setColor(new Color(222, 229, 255));
    paramGraphics2D.setStroke(new BasicStroke(6.0F, 0, 0));
    paramGraphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    paramGraphics2D.drawLine(8, 9, 64, 62);
    paramGraphics2D.drawLine(64, 9, 8, 62);
  }

  private static void drawCheckboxCrossSmall(Graphics2D paramGraphics2D, FormAppearanceObject paramFormAppearanceObject)
  {
    if (paramFormAppearanceObject.isSelected)
      paramGraphics2D.setColor(Color.BLACK);
    else
      paramGraphics2D.setColor(new Color(222, 229, 255));
    paramGraphics2D.setStroke(new BasicStroke(6.0F, 0, 0));
    paramGraphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    paramGraphics2D.drawLine(10, 12, 61, 59);
    paramGraphics2D.drawLine(10, 59, 61, 12);
  }

  private static void drawRadios(Graphics2D paramGraphics2D, FormAppearanceObject paramFormAppearanceObject, float[] paramArrayOfFloat)
  {
    paramGraphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    paramGraphics2D.setStroke(new BasicStroke(3.0F));
    paramGraphics2D.setColor(new Color(222, 229, 255));
    paramGraphics2D.fill(new Ellipse2D.Double(5.0D, 4.0D, 62.0D, 62.0D));
    paramGraphics2D.setComposite(AlphaComposite.getInstance(3, 0.5F));
    paramGraphics2D.setStroke(new BasicStroke(6.0F, 0, 0, 10.0F, paramArrayOfFloat, 0.0F));
    paramGraphics2D.setColor(new Color(30, 30, 30));
    paramGraphics2D.draw(new Ellipse2D.Double(5.0D, 4.0D, 62.0D, 62.0D));
    paramGraphics2D.setComposite(AlphaComposite.getInstance(3, 1.0F));
    if (paramFormAppearanceObject.isSelected)
      paramGraphics2D.setColor(Color.BLACK);
    else
      paramGraphics2D.setColor(new Color(222, 229, 255));
    paramGraphics2D.fill(new Ellipse2D.Double(22.0D, 21.0D, 28.0D, 28.0D));
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.raw.XFAImages
 * JD-Core Version:    0.6.2
 */