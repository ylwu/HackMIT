package org.jpedal.objects.acroforms.creation;

import java.awt.BasicStroke;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import org.jpedal.objects.raw.PdfArrayIterator;
import org.jpedal.objects.raw.PdfObject;

public class JPedalBorderFactory
{
  private static final boolean printouts = false;

  public static Border createBorderStyle(PdfObject paramPdfObject, Color paramColor1, Color paramColor2, float paramFloat)
  {
    if ((paramColor2 != null) || (paramColor1 == null))
      return null;
    Object localObject1 = null;
    int i = -1;
    if (paramPdfObject != null)
      i = paramPdfObject.getInt(39);
    if (i < 0)
      i = 1;
    float f = i * paramFloat;
    i = (int)(f + 0.5D);
    int j = 35;
    if (paramPdfObject != null)
    {
      j = paramPdfObject.getNameAsConstant(35);
      if (j == -1)
        j = 35;
      int k = paramPdfObject.getNameAsConstant(40);
      if (k != -1)
        i /= 2;
    }
    int m;
    Object localObject3;
    switch (j)
    {
    case 37:
      localObject1 = BorderFactory.createMatteBorder(0, 0, i, 0, paramColor1);
      break;
    case 25:
      localObject1 = BorderFactory.createEtchedBorder(paramColor1, paramColor2);
      break;
    case 18:
      localObject1 = BorderFactory.createBevelBorder(1, paramColor1, paramColor2);
      break;
    case 35:
      localObject1 = BorderFactory.createLineBorder(paramColor1, i);
      break;
    case 20:
      localObject2 = paramPdfObject.getMixedArray(20);
      m = 0;
      localObject3 = new float[1];
      int n = ((PdfArrayIterator)localObject2).getTokenCount();
      if (n > 0)
        localObject3 = ((PdfArrayIterator)localObject2).getNextValueAsFloatArray();
      if (n > 1)
        m = ((PdfArrayIterator)localObject2).getNextValueAsInteger();
      if (i < 0)
        i = 1;
      BasicStroke localBasicStroke = new BasicStroke(i, 0, 0, 1.0F, (float[])localObject3, m);
      localObject1 = new DashBorder(localBasicStroke, paramColor1);
    }
    Object localObject2 = new EmptyBorder(0, 0, 0, 0);
    if (paramPdfObject != null)
    {
      m = paramPdfObject.getNameAsConstant(40);
      if (m != -1)
      {
        localObject3 = BorderFactory.createLineBorder(paramColor1, i);
        switch (m)
        {
        case 34:
          localObject2 = localObject3;
        }
      }
    }
    return new CompoundBorder((Border)localObject2, (Border)localObject1);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.acroforms.creation.JPedalBorderFactory
 * JD-Core Version:    0.6.2
 */