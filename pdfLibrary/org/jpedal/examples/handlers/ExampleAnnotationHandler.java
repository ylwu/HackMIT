package org.jpedal.examples.handlers;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.swing.JFileChooser;
import org.jpedal.PdfDecoder;
import org.jpedal.examples.viewer.Values;
import org.jpedal.examples.viewer.gui.SwingGUI;
import org.jpedal.exception.PdfException;
import org.jpedal.external.AnnotationHandler;
import org.jpedal.io.ObjectStore;
import org.jpedal.io.PdfObjectReader;
import org.jpedal.objects.PdfPageData;
import org.jpedal.objects.acroforms.AcroRenderer;
import org.jpedal.objects.acroforms.ReturnValues;
import org.jpedal.objects.raw.FormObject;
import org.jpedal.objects.raw.PdfArrayIterator;
import org.jpedal.objects.raw.PdfObject;
import org.jpedal.render.ImageObject;

public class ExampleAnnotationHandler
  implements AnnotationHandler
{
  public void handleAnnotations(PdfDecoder paramPdfDecoder, Map paramMap, int paramInt)
  {
    PdfArrayIterator localPdfArrayIterator = paramPdfDecoder.getFormRenderer().getAnnotsOnPage(paramInt);
    if ((localPdfArrayIterator != null) && (localPdfArrayIterator.getTokenCount() > 0))
    {
      int i = 0;
      int j = localPdfArrayIterator.getTokenCount();
      int[] arrayOfInt = new int[j];
      Color[] arrayOfColor = new Color[j];
      Object[] arrayOfObject1 = new Object[j];
      while (localPdfArrayIterator.hasMoreTokens())
      {
        int k = paramPdfDecoder.getPdfPageData().getMediaBoxX(paramInt);
        int m = paramPdfDecoder.getPdfPageData().getMediaBoxY(paramInt);
        String str = localPdfArrayIterator.getNextValueAsString(true);
        Object[] arrayOfObject2 = paramPdfDecoder.getFormRenderer().getFormComponents(str, ReturnValues.FORMOBJECTS_FROM_NAME, -1);
        for (Object localObject : arrayOfObject2)
          if (localObject != null)
          {
            FormObject localFormObject = (FormObject)localObject;
            int i2 = localFormObject.getParameterConstant(1147962727);
            if (i2 == 473513531)
            {
              paramMap.put(localFormObject, "x");
              Color localColor = Color.BLUE;
              Rectangle localRectangle = localFormObject.getBoundingRectangle();
              arrayOfInt[i] = 3;
              ImageObject localImageObject = new ImageObject();
              localImageObject.x = (localRectangle.x + k);
              localImageObject.y = (localRectangle.y + m);
              localImageObject.image = createUniqueImage(16, String.valueOf(i + 1), localColor);
              arrayOfObject1[i] = localImageObject;
              i++;
            }
          }
      }
      try
      {
        paramPdfDecoder.drawAdditionalObjectsOverPage(paramInt, arrayOfInt, arrayOfColor, arrayOfObject1);
      }
      catch (PdfException localPdfException)
      {
        localPdfException.printStackTrace();
      }
    }
  }

  public void checkLinks(Map paramMap, boolean paramBoolean, PdfObjectReader paramPdfObjectReader, int paramInt1, int paramInt2, SwingGUI paramSwingGUI, Values paramValues)
  {
    Iterator localIterator = paramMap.keySet().iterator();
    for (FormObject localFormObject = null; localIterator.hasNext(); localFormObject = null)
    {
      localFormObject = (FormObject)localIterator.next();
      if (localFormObject.getBoundingRectangle().contains(paramInt1, paramInt2))
        break;
    }
    if (localFormObject != null)
    {
      System.out.println("clicked on=" + paramBoolean + " obj=" + localFormObject + ' ' + localFormObject.getObjectRefAsString() + ' ' + localFormObject.getBoundingRectangle());
      if (!paramBoolean)
        return;
      PdfObject localPdfObject1 = null;
      PdfObject localPdfObject2 = localFormObject.getDictionary(5667);
      if (localPdfObject2 != null)
        localPdfObject1 = localPdfObject2.getDictionary(5398);
      JFileChooser localJFileChooser = new JFileChooser(paramValues.getInputDir());
      localJFileChooser.setFileSelectionMode(0);
      int i = localJFileChooser.showSaveDialog(paramSwingGUI.getFrame());
      if (i == 0)
      {
        File localFile = localJFileChooser.getSelectedFile();
        if (localPdfObject1 != null)
          paramPdfObjectReader.checkResolved(localPdfObject1);
        PdfObject localPdfObject3 = localPdfObject1.getDictionary(22);
        String str = localPdfObject3.getCachedStreamFile(paramPdfObjectReader.getObjectReader());
        System.out.println("file=" + str);
        if (str != null)
        {
          ObjectStore.copy(str, localFile.toString());
        }
        else
        {
          byte[] arrayOfByte = localPdfObject3.getDecodedStream();
          if (arrayOfByte != null)
            try
            {
              FileOutputStream localFileOutputStream = new FileOutputStream(localFile);
              localFileOutputStream.write(arrayOfByte);
              localFileOutputStream.close();
            }
            catch (Exception localException)
            {
              localException.printStackTrace();
            }
        }
      }
    }
  }

  private static BufferedImage createUniqueImage(int paramInt, String paramString, Color paramColor)
  {
    BufferedImage localBufferedImage = new BufferedImage(paramInt, paramInt, 2);
    Graphics2D localGraphics2D = (Graphics2D)localBufferedImage.getGraphics();
    localGraphics2D.setColor(paramColor);
    localGraphics2D.fill(new Rectangle(0, 0, paramInt, paramInt));
    localGraphics2D.setColor(Color.BLACK);
    localGraphics2D.draw(new Rectangle(0, 0, paramInt - 1, paramInt - 1));
    localGraphics2D.setColor(Color.white);
    localGraphics2D.drawString(paramString, 2, 12);
    return localBufferedImage;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.handlers.ExampleAnnotationHandler
 * JD-Core Version:    0.6.2
 */