package org.jpedal.objects.raw;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import org.jpedal.external.ExternalHandlers;
import org.jpedal.fonts.PdfFont;
import org.jpedal.fonts.glyph.T3Glyph;
import org.jpedal.io.ObjectStore;
import org.jpedal.io.PdfObjectReader;
import org.jpedal.parser.PdfStreamDecoder;
import org.jpedal.render.T3Display;
import org.jpedal.render.T3Renderer;
import org.jpedal.utils.LogWriter;
import org.w3c.dom.Node;

public class FormStream
{
  public static final boolean debugUnimplemented = false;
  public static final boolean debug = false;
  private static boolean showFontMessage = false;
  public static final boolean exitOnError = false;
  protected PdfObjectReader currentPdfFile;
  public boolean isXFA = false;
  public static final int[] id = { 17, 4866, 4668, 21, 40, 20, 37, 5695, 8223, 8211, 8230, 8217, 31, 4865, 27, 22, 38, 4866, 5139, 10019, 5155, 10016, 5152 };

  private static BufferedImage decodeApperanceObject(FormAppearanceObject paramFormAppearanceObject)
  {
    int i = 4 * paramFormAppearanceObject.getWidth();
    int j = 4 * paramFormAppearanceObject.getHeight();
    BufferedImage localBufferedImage = new BufferedImage(i, j, 2);
    Graphics2D localGraphics2D = localBufferedImage.createGraphics();
    localGraphics2D.setTransform(AffineTransform.getScaleInstance(i / 80.0F, j / 80.0F));
    if (paramFormAppearanceObject.isRadio())
      XFAImages.drawRadio(localGraphics2D, localBufferedImage, paramFormAppearanceObject);
    if (paramFormAppearanceObject.isCheckBox())
      XFAImages.drawCheckbox(localGraphics2D, localBufferedImage, paramFormAppearanceObject);
    if (paramFormAppearanceObject.isRollover())
      XFAImages.drawRollover(localGraphics2D, localBufferedImage, paramFormAppearanceObject);
    return localBufferedImage;
  }

  public void createAppearanceString(FormObject paramFormObject, PdfObjectReader paramPdfObjectReader)
  {
    this.currentPdfFile = paramPdfObjectReader;
    init(paramFormObject);
  }

  private void init(FormObject paramFormObject)
  {
    int i = paramFormObject.getInt(5686);
    if (i != -1)
      paramFormObject.commandFf(i);
    resolveAdditionalAction(paramFormObject);
    setupAPimages(paramFormObject);
    int j = paramFormObject.getNameAsConstant(24);
    if (j != -1)
      if ((j == 36) || (j == 32))
      {
        if (!paramFormObject.hasDownImage())
          paramFormObject.setOffsetDownApp();
      }
      else if (j == 30)
        paramFormObject.setNoDownIcon();
      else if (j == 25)
        paramFormObject.setInvertForDownIcon();
    String str = paramFormObject.getTextStreamValue(5137);
    if (str != null)
      decodeFontCommandObj(str, paramFormObject);
  }

  public static void setupAPimages(FormObject paramFormObject)
  {
    PdfObject localPdfObject = paramFormObject.getDictionary(4384).getDictionary(30);
    if (localPdfObject != null)
    {
      paramFormObject.setAppreancesUsed(true);
      paramFormObject.setNormalOnState(paramFormObject.getName(4387));
      if (localPdfObject.getDictionary(7998) != null)
      {
        paramFormObject.setNormalOnState("On");
      }
      else
      {
        localObject = localPdfObject.getOtherDictionaries();
        if ((localObject != null) && (!((Map)localObject).isEmpty()))
        {
          Iterator localIterator = ((Map)localObject).keySet().iterator();
          while (localIterator.hasNext())
          {
            String str = (String)localIterator.next();
            paramFormObject.setNormalOnState(str);
          }
        }
      }
      Object localObject = paramFormObject.getName(4387);
      if ((localObject != null) && (((String)localObject).equals(paramFormObject.getNormalOnState())))
        paramFormObject.setSelected(true);
    }
  }

  private void resolveAdditionalAction(FormObject paramFormObject)
  {
    for (int m : id)
    {
      int i = m;
      this.currentPdfFile.setJavascriptForObject(paramFormObject, 4369, i);
      this.currentPdfFile.setJavascriptForObject(paramFormObject, 17, i);
    }
  }

  public static BufferedImage decode(PdfObjectReader paramPdfObjectReader, PdfObject paramPdfObject, int paramInt1, int paramInt2, int paramInt3, int paramInt4, float paramFloat)
  {
    if (paramPdfObject.getObjectType() == 129)
      return decodeApperanceObject((FormAppearanceObject)paramPdfObject);
    boolean bool1 = true;
    paramPdfObjectReader.checkResolved(paramPdfObject);
    try
    {
      ObjectStore localObjectStore = new ObjectStore();
      T3Display localT3Display = new T3Display(0, false, 20, localObjectStore);
      if (!bool1)
        localT3Display.setOptimisedRotation(false);
      else
        localT3Display.setHiResImageForDisplayMode(bool1);
      PdfStreamDecoder localPdfStreamDecoder = new PdfStreamDecoder(paramPdfObjectReader, bool1, null);
      localPdfStreamDecoder.setParameters(false, true, 15, 0, false);
      localPdfStreamDecoder.setIntValue(-12, 1);
      localPdfStreamDecoder.setBooleanValue(-16, true);
      localPdfStreamDecoder.setObjectValue(-8, localObjectStore);
      localPdfStreamDecoder.setObjectValue(23, localT3Display);
      try
      {
        PdfObject localPdfObject = paramPdfObject.getDictionary(2004251818);
        if (localPdfObject != null)
          localPdfStreamDecoder.readResources(localPdfObject, false);
      }
      catch (Exception localException2)
      {
        System.out.println("Exception " + localException2 + " reading resources in XForm");
        if (LogWriter.isOutput())
          LogWriter.writeLog("Exception: " + localException2.getMessage());
      }
      byte[] arrayOfByte = paramPdfObject.getDecodedStream();
      if (arrayOfByte != null)
        localPdfStreamDecoder.decodeStreamIntoObjects(arrayOfByte, false);
      boolean bool2 = localPdfStreamDecoder.ignoreColors;
      localObjectStore.flush();
      T3Glyph localT3Glyph = new T3Glyph(localT3Display, 0, 0, bool2, "");
      float[] arrayOfFloat1 = paramPdfObject.getFloatArray(303185736);
      float f2 = 0.0F;
      float f3 = 0.0F;
      float f6;
      float f7;
      float f8;
      float f1;
      if (arrayOfFloat1 != null)
      {
        for (int i = 0; i < 4; i++)
          arrayOfFloat1[i] *= paramFloat;
        f2 = arrayOfFloat1[0];
        f3 = arrayOfFloat1[1];
        i = (int)(arrayOfFloat1[2] + 0.5F - arrayOfFloat1[0]);
        if (i < 0)
          i = -i;
        int j = (int)(arrayOfFloat1[3] + 0.5F - arrayOfFloat1[1]);
        if (j < 0)
          j = -j;
        if ((i == 0) && (j > 0))
          i = 1;
        if ((i > 0) && (j == 0))
          j = 1;
        f6 = paramInt2 / i;
        f7 = paramInt3 / j;
        f8 = f6 - f7;
        int k = (int)f8;
        if (k != 0)
        {
          int m = paramInt2;
          paramInt2 = paramInt3;
          paramInt3 = m;
          f6 = paramInt2 / i;
          f7 = paramInt3 / j;
        }
        if ((f6 < 1.0F) || (f7 < 1.0F))
        {
          f1 = 1.0F;
          paramInt2 = i;
          paramInt3 = j;
        }
        else
        {
          if (f6 > f7)
          {
            f1 = f6;
            paramInt3 = (int)(j * f1);
          }
          else
          {
            f1 = f7;
            paramInt2 = (int)(i * f1);
          }
          f2 *= f1;
          f3 *= f1;
        }
      }
      else
      {
        float f4 = 20.0F;
        if (paramInt3 < f4)
          paramInt3 = (int)f4;
        if (paramInt2 < f4)
          paramInt2 = (int)f4;
        float f5 = paramInt2 / f4;
        f6 = paramInt3 / f4;
        if (f5 > f6)
        {
          f1 = f5;
          paramInt3 = (int)(f4 * f1);
        }
        else
        {
          f1 = f6;
          paramInt2 = (int)(f4 * f1);
        }
        f2 *= f1;
        f3 *= f1;
      }
      if ((paramInt2 == 0) || (paramInt3 == 0))
        return null;
      if (paramInt4 == 1)
      {
        paramInt2 += 2;
        paramInt3 += 2;
      }
      int n = paramInt3;
      float[] arrayOfFloat2 = paramPdfObject.getFloatArray(1145198201);
      BufferedImage localBufferedImage;
      Graphics2D localGraphics2D;
      AffineTransform localAffineTransform1;
      if (arrayOfFloat2 != null)
      {
        f6 = arrayOfFloat2[0];
        f7 = arrayOfFloat2[1];
        f8 = arrayOfFloat2[2];
        float f9 = arrayOfFloat2[3];
        float f10 = arrayOfFloat2[4] * f1 * paramFloat;
        float f11 = arrayOfFloat2[5] * f1 * paramFloat;
        if (f8 != 0.0F)
        {
          localBufferedImage = new BufferedImage(paramInt3, paramInt2, 2);
          n = paramInt2;
        }
        else
        {
          localBufferedImage = new BufferedImage(paramInt2, paramInt3, 2);
          if (f7 >= 0.0F)
          {
            if (f10 != 0.0F)
              f10 = -f2;
            if (f11 != 0.0F)
              f11 = -f3;
          }
        }
        localGraphics2D = (Graphics2D)localBufferedImage.getGraphics();
        localAffineTransform1 = new AffineTransform();
        localAffineTransform1.translate(0.0D, n);
        localAffineTransform1.scale(1.0D, -1.0D);
        localGraphics2D.setTransform(localAffineTransform1);
        AffineTransform localAffineTransform2 = new AffineTransform(f6, f7, f8, f9, f10, f11);
        localGraphics2D.transform(localAffineTransform2);
      }
      else
      {
        localBufferedImage = new BufferedImage(paramInt2, paramInt3, 2);
        localGraphics2D = (Graphics2D)localBufferedImage.getGraphics();
        localAffineTransform1 = new AffineTransform();
        localAffineTransform1.translate(0.0D, n);
        localAffineTransform1.scale(1.0D, -1.0D);
        localGraphics2D.setTransform(localAffineTransform1);
      }
      if (paramInt4 == 2)
        localGraphics2D.scale(-1.0D, -1.0D);
      else if (paramInt4 == 1)
        localGraphics2D.translate(1, 1);
      if (paramInt1 == 1919840408)
        localGraphics2D.setComposite(AlphaComposite.getInstance(3, 0.5F));
      localT3Glyph.render(0, localGraphics2D, f1 * paramFloat, true);
      localGraphics2D.dispose();
      return localBufferedImage;
    }
    catch (Exception localException1)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception: " + localException1.getMessage());
      return null;
    }
    catch (Error localError)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Error: " + localError.getMessage());
      if ((ExternalHandlers.throwMissingCIDError) && (localError.getMessage().contains("kochi")))
        throw localError;
    }
    return null;
  }

  public static String decipherTextFromAP(PdfObjectReader paramPdfObjectReader, PdfObject paramPdfObject)
  {
    try
    {
      ObjectStore localObjectStore = new ObjectStore();
      T3Display localT3Display = new T3Display(0, false, 20, localObjectStore);
      PdfStreamDecoder localPdfStreamDecoder = new PdfStreamDecoder(paramPdfObjectReader, false, null);
      localPdfStreamDecoder.setParameters(false, true, 15, 0, false);
      localPdfStreamDecoder.setObjectValue(-8, localObjectStore);
      localT3Display.setOptimisedRotation(false);
      localPdfStreamDecoder.setObjectValue(23, localT3Display);
      try
      {
        PdfObject localPdfObject = paramPdfObject.getDictionary(2004251818);
        if (localPdfObject != null)
          localPdfStreamDecoder.readResources(localPdfObject, false);
      }
      catch (Exception localException2)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog("Exception: " + localException2.getMessage());
      }
      byte[] arrayOfByte = paramPdfObject.getDecodedStream();
      String str = "";
      if (arrayOfByte != null)
        str = localPdfStreamDecoder.decodeStreamIntoObjects(arrayOfByte, true);
      if ((str == null) || (str.isEmpty()))
        str = null;
      localObjectStore.flush();
      return str;
    }
    catch (Exception localException1)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception: " + localException1.getMessage());
      return null;
    }
    catch (Error localError)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Error: " + localError.getMessage());
      if ((ExternalHandlers.throwMissingCIDError) && (localError.getMessage().contains("kochi")))
        throw localError;
    }
    return null;
  }

  public static BufferedImage rotate(BufferedImage paramBufferedImage, int paramInt)
  {
    if (paramBufferedImage == null)
      return null;
    if (paramInt == 0)
      return paramBufferedImage;
    BufferedImage localBufferedImage;
    try
    {
      double d = paramInt * 3.141592653589793D / 180.0D;
      int i = paramBufferedImage.getWidth();
      int j = paramBufferedImage.getHeight();
      int k = (int)Math.round(j * Math.abs(Math.sin(d)) + i * Math.abs(Math.cos(d)));
      int m = (int)Math.round(j * Math.abs(Math.cos(d)) + i * Math.abs(Math.sin(d)));
      AffineTransform localAffineTransform = AffineTransform.getTranslateInstance((k - i) / 2, (m - j) / 2);
      localAffineTransform.rotate(d, i / 2, j / 2);
      localBufferedImage = new BufferedImage(k, m, 2);
      Graphics2D localGraphics2D = localBufferedImage.createGraphics();
      localGraphics2D.drawRenderedImage(paramBufferedImage, localAffineTransform);
      localGraphics2D.dispose();
    }
    catch (Error localError)
    {
      localBufferedImage = null;
    }
    return localBufferedImage;
  }

  public boolean hasXFADataSet()
  {
    return false;
  }

  public static void decodeFontCommandObj(String paramString, FormObject paramFormObject)
  {
    StringTokenizer localStringTokenizer = new StringTokenizer(paramString, "() []");
    int i = localStringTokenizer.countTokens();
    String[] arrayOfString = new String[i];
    for (int j = 0; localStringTokenizer.hasMoreTokens(); j++)
      arrayOfString[j] = localStringTokenizer.nextToken();
    for (j = i - 1; j > -1; j--)
      if (arrayOfString[j].equals("g"))
      {
        j--;
        float f1 = 0.0F;
        try
        {
          f1 = Float.parseFloat(handleComma(arrayOfString[j]));
        }
        catch (Exception localException1)
        {
          LogWriter.writeLog("Error in generating g value " + arrayOfString[j]);
        }
        paramFormObject.setTextColor(new float[] { f1 });
      }
      else if (arrayOfString[j].equals("Tf"))
      {
        j--;
        int k = 8;
        try
        {
          k = (int)Float.parseFloat(handleComma(arrayOfString[j]));
        }
        catch (Exception localException2)
        {
          LogWriter.writeLog("Error in generating Tf size " + arrayOfString[j]);
        }
        j--;
        String str = null;
        try
        {
          str = arrayOfString[j];
          if (str.startsWith("/"))
            str = str.substring(1);
        }
        catch (Exception localException3)
        {
          LogWriter.writeLog("Error in generating Tf font " + arrayOfString[j]);
        }
        PdfFont localPdfFont = new PdfFont();
        paramFormObject.setTextFont(localPdfFont.setFont(str, k));
        paramFormObject.setTextSize(k);
      }
      else if ((arrayOfString[j].equals("rg")) || (arrayOfString[j].equals("r")))
      {
        j--;
        float f2 = Float.parseFloat(handleComma(arrayOfString[j]));
        j--;
        float f3 = Float.parseFloat(handleComma(arrayOfString[j]));
        j--;
        float f4 = Float.parseFloat(handleComma(arrayOfString[j]));
        paramFormObject.setTextColor(new float[] { f4, f3, f2 });
      }
      else if (arrayOfString[j].equals("Sig"))
      {
        LogWriter.writeFormLog("Sig-  UNIMPLEMENTED=" + paramString + "< " + j, false);
      }
      else if ((!arrayOfString[j].equals("\\n")) && (!showFontMessage))
      {
        showFontMessage = true;
        LogWriter.writeFormLog("{stream} Unknown FONT command " + arrayOfString[j] + ' ' + j + " string=" + paramString, false);
      }
  }

  private static String handleComma(String paramString)
  {
    int i = paramString.indexOf(44);
    if (i != -1)
      paramString = paramString.substring(0, i);
    return paramString;
  }

  public Node getXFA(int paramInt)
  {
    throw new RuntimeException("getXFA Should never be called in base class");
  }

  public boolean isXFA()
  {
    return this.isXFA;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.raw.FormStream
 * JD-Core Version:    0.6.2
 */