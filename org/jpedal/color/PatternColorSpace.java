package org.jpedal.color;

import com.idrsolutions.pdf.color.shading.ShadingFactory;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import org.jpedal.exception.PdfException;
import org.jpedal.io.ColorSpaceConvertor;
import org.jpedal.io.ObjectStore;
import org.jpedal.io.PdfObjectReader;
import org.jpedal.objects.GraphicsState;
import org.jpedal.objects.raw.PatternObject;
import org.jpedal.objects.raw.PdfObject;
import org.jpedal.parser.PdfStreamDecoder;
import org.jpedal.render.DynamicVectorRenderer;
import org.jpedal.render.T3Renderer;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.Matrix;

public class PatternColorSpace extends GenericColorSpace
{
  private Map cachedPaints = new HashMap();
  private PdfObjectReader currentPdfFile = null;
  private boolean colorsReversed;

  public PatternColorSpace(PdfObjectReader paramPdfObjectReader)
  {
    this.value = 1146450818;
    this.currentPdfFile = paramPdfObjectReader;
    this.currentColor = new PdfColor(1.0F, 1.0F, 1.0F);
  }

  public void setColor(String[] paramArrayOfString, int paramInt)
  {
    PatternObject localPatternObject = (PatternObject)this.patterns.get(paramArrayOfString[0]);
    String str = localPatternObject.getObjectRefAsString();
    if ((str != null) && (this.cachedPaints.containsKey(str)))
    {
      this.currentColor = ((PdfPaint)this.cachedPaints.get(str));
      return;
    }
    this.currentPdfFile.checkResolved(localPatternObject);
    byte[] arrayOfByte = this.currentPdfFile.readStream(localPatternObject, true, true, true, false, false, localPatternObject.getCacheName(this.currentPdfFile.getObjectReader()));
    int i = localPatternObject.getInt(1755231159);
    Object localObject = (float[][])null;
    float[] arrayOfFloat = localPatternObject.getFloatArray(1145198201);
    if (arrayOfFloat != null)
    {
      float[][] arrayOfFloat1;
      if (i == 1)
      {
        arrayOfFloat1 = new float[][] { { arrayOfFloat[0], arrayOfFloat[1], 0.0F }, { arrayOfFloat[2], arrayOfFloat[3], 0.0F }, { 0.0F, 0.0F, 1.0F } };
        if (arrayOfFloat[5] < 0.0F)
        {
          arrayOfFloat[4] = 0.0F;
          arrayOfFloat[5] = 0.0F;
        }
        localObject = arrayOfFloat1;
      }
      else
      {
        arrayOfFloat1 = new float[][] { { arrayOfFloat[0], arrayOfFloat[1], 0.0F }, { arrayOfFloat[2], arrayOfFloat[3], 0.0F }, { arrayOfFloat[4], arrayOfFloat[5], 1.0F } };
        this.colorsReversed = (arrayOfFloat1[2][0] < 0.0F);
        localObject = Matrix.multiply(arrayOfFloat1, this.CTM);
      }
    }
    if (i == 1)
      this.currentColor = setupTiling(localPatternObject, arrayOfFloat, (float[][])localObject, arrayOfByte);
    else if (i == 2)
      this.currentColor = setupShading(localPatternObject, (float[][])localObject);
  }

  private PdfPaint setupTiling(PdfObject paramPdfObject, float[] paramArrayOfFloat, float[][] paramArrayOfFloat1, byte[] paramArrayOfByte)
  {
    int i = (paramArrayOfFloat != null) && (paramArrayOfFloat[5] == this.pageHeight) && (paramArrayOfFloat[4] == 0.0F) ? 1 : 0;
    Object localObject1 = null;
    boolean bool1 = true;
    BufferedImage localBufferedImage1 = null;
    AffineTransform localAffineTransform1 = null;
    float[][] arrayOfFloat = (float[][])null;
    if (paramArrayOfFloat1 != null)
    {
      arrayOfFloat = new float[3][3];
      for (j = 0; j < 3; j++)
        for (k = 0; k < 3; k++)
          arrayOfFloat[k][j] = paramArrayOfFloat1[k][j];
    }
    if ((paramArrayOfFloat1 != null) && (paramArrayOfFloat1[0][0] != 0.0F) && (paramArrayOfFloat1[0][1] < 0.001D) && (paramArrayOfFloat1[0][1] > -0.001D))
      paramArrayOfFloat1[0][1] = 0.0F;
    if ((paramArrayOfFloat1 != null) && (paramArrayOfFloat1[1][1] != 0.0F) && (paramArrayOfFloat1[1][0] < 0.001D) && (paramArrayOfFloat1[1][0] > -0.001D))
      paramArrayOfFloat1[1][0] = 0.0F;
    if (paramArrayOfFloat != null)
      for (j = 0; j < 6; j++)
        if ((paramArrayOfFloat[j] != 0.0F) && (paramArrayOfFloat[j] < 0.001D) && (paramArrayOfFloat[j] > -0.001D))
          paramArrayOfFloat[j] = 0.0F;
    if ((paramArrayOfFloat1 != null) && (paramArrayOfFloat1[0][0] < 0.0F) && (paramArrayOfFloat1[1][1] < 0.0F))
    {
      paramArrayOfFloat1[0][0] = (-paramArrayOfFloat1[0][0]);
      paramArrayOfFloat1[1][1] = (-paramArrayOfFloat1[1][1]);
      if (paramArrayOfFloat != null)
      {
        paramArrayOfFloat[0] = (-paramArrayOfFloat[0]);
        paramArrayOfFloat[3] = (-paramArrayOfFloat[3]);
      }
    }
    int j = 0;
    int k = 0;
    boolean bool2 = false;
    if (paramArrayOfFloat1 != null)
    {
      j = (paramArrayOfFloat1[1][0] != 0.0F) && (paramArrayOfFloat1[0][1] != 0.0F) && (paramArrayOfFloat1[0][0] != 0.0F) && (paramArrayOfFloat1[1][1] != 0.0F) ? 1 : 0;
      if ((j != 0) && (paramArrayOfFloat1[0][0] != 0.0F) && (paramArrayOfFloat1[0][0] < 0.001D) && (paramArrayOfFloat1[1][1] != 0.0F) && (paramArrayOfFloat1[1][1] < 0.001D))
      {
        j = 0;
        paramArrayOfFloat1[0][0] = (-paramArrayOfFloat1[0][1]);
        paramArrayOfFloat1[1][1] = (-paramArrayOfFloat1[1][0]);
        paramArrayOfFloat1[1][0] = 0.0F;
        paramArrayOfFloat1[0][1] = 0.0F;
      }
      if (((j == 0) || (paramArrayOfFloat1[0][0] <= 0.0F) || (paramArrayOfFloat1[0][1] >= 0.0F) || (paramArrayOfFloat1[1][0] <= 0.0F) || (paramArrayOfFloat1[1][1] <= 0.0F)) && (j != 0) && (paramArrayOfFloat1[0][0] < 0.0F) && (paramArrayOfFloat1[0][1] < 0.0F) && (paramArrayOfFloat1[1][0] < 0.0F) && (paramArrayOfFloat1[1][1] > 0.0F))
      {
        paramArrayOfFloat1[0][0] = (-paramArrayOfFloat1[0][0]);
        paramArrayOfFloat1[1][0] = (-paramArrayOfFloat1[1][0]);
        bool2 = true;
      }
      k = (paramArrayOfFloat1[1][1] < 0.0F) || (paramArrayOfFloat1[0][1] < 0.0F) ? 1 : 0;
      if ((paramArrayOfFloat1[0][0] > 0.0F) && (paramArrayOfFloat1[0][1] < 0.0F) && (paramArrayOfFloat1[1][0] > 0.0F) && (paramArrayOfFloat1[1][1] > 0.0F))
        k = 0;
      if ((paramArrayOfFloat1[0][0] > 0.1F) && ((j != 0) || (k != 0)))
        bool1 = false;
      if ((k != 0) && (paramArrayOfFloat1[0][1] > 0.0F) && (paramArrayOfFloat1[1][0] > 0.0F))
        k = 0;
    }
    int m = (int)paramPdfObject.getFloatNumber(591672680);
    int n = (int)paramPdfObject.getFloatNumber(591672681);
    int i1 = m;
    int i2 = n;
    if (i1 == 0)
      i1 = 1;
    if (i2 == 0)
      i2 = 1;
    if (i1 < 0)
      i1 = -i1;
    if (i2 < 0)
      i2 = -i2;
    float f1 = 0.0F;
    float f2 = 0.0F;
    int i3 = 0;
    int i4 = 0;
    if (paramArrayOfFloat1 != null)
    {
      if (paramArrayOfFloat1[1][1] < 0.0F)
        paramArrayOfFloat1[2][1] = i2;
      if (paramArrayOfFloat1[1][0] != 0.0D)
        paramArrayOfFloat1[2][1] = (-paramArrayOfFloat1[1][0]);
    }
    ObjectStore localObjectStore = new ObjectStore();
    DynamicVectorRenderer localDynamicVectorRenderer = decodePatternContent(paramPdfObject, paramArrayOfFloat1, paramArrayOfByte, localObjectStore);
    BufferedImage localBufferedImage2 = localDynamicVectorRenderer.getSingleImagePattern();
    if ((paramArrayOfFloat != null) && (paramArrayOfFloat[0] < 1.5D) && (paramArrayOfFloat[3] < 1.5D) && (paramArrayOfFloat[1] == 0.0F) && (paramArrayOfFloat[2] == 0.0F) && (((localBufferedImage2 != null) && ((paramArrayOfFloat[0] >= 1.0F) || (m * paramArrayOfFloat[0] < 1600.0F))) || ((m == -32768) && (n == -32768))))
    {
      AffineTransform localAffineTransform2 = new AffineTransform();
      localAffineTransform2.scale(1.0D, -1.0D);
      localAffineTransform2.translate(0.0D, -localBufferedImage2.getHeight());
      AffineTransformOp localAffineTransformOp1 = new AffineTransformOp(localAffineTransform2, null);
      localBufferedImage2 = localAffineTransformOp1.filter(localBufferedImage2, null);
      if (localBufferedImage2.getType() != 2)
        localBufferedImage2 = ColorSpaceConvertor.convertToARGB(localBufferedImage2);
      return new PdfTexturePaint(localBufferedImage2, new Rectangle((int)(paramArrayOfFloat[4] - localBufferedImage2.getWidth()), (int)(paramArrayOfFloat[5] - localBufferedImage2.getHeight()), localBufferedImage2.getWidth(), localBufferedImage2.getHeight()));
    }
    float f3 = 0.0F;
    float f4 = 0.0F;
    int i5 = 0;
    if (paramArrayOfFloat1 != null)
    {
      f3 = paramArrayOfFloat1[0][0];
      if (f3 == 0.0F)
        f3 = paramArrayOfFloat1[0][1];
      if (f3 < 0.0F)
        f3 = -f3;
      f4 = paramArrayOfFloat1[1][1];
      if (f4 == 0.0F)
        f4 = paramArrayOfFloat1[1][0];
      if (f4 < 0.0F)
        f4 = -f4;
    }
    Object localObject2;
    if (paramArrayOfFloat1 != null)
    {
      if ((paramArrayOfFloat != null) && (paramArrayOfFloat[0] > 1.0F) && (paramArrayOfFloat[3] > 1.0F) && (j == 0))
      {
        localBufferedImage1 = new BufferedImage((int)(i1 * f3), (int)(i2 * f4), 2);
        localObject2 = localBufferedImage1.createGraphics();
        ((Graphics2D)localObject2).setClip(new Rectangle(0, 0, localBufferedImage1.getWidth(), localBufferedImage1.getHeight()));
        localDynamicVectorRenderer.setG2((Graphics2D)localObject2);
        localDynamicVectorRenderer.paint(null, new AffineTransform(paramArrayOfFloat1[0][0], paramArrayOfFloat1[0][1], paramArrayOfFloat1[1][0], paramArrayOfFloat1[1][1], paramArrayOfFloat[4], -paramArrayOfFloat[5] / 2.0F), null);
      }
      else
      {
        localObject2 = null;
        f1 = paramArrayOfFloat1[0][0];
        f2 = paramArrayOfFloat1[1][1];
        if (f1 == 0.0F)
          f1 = paramArrayOfFloat1[0][1];
        if (f1 < 0.0F)
          f1 = -f1;
        if (f2 == 0.0F)
          f2 = paramArrayOfFloat1[1][0];
        if (f2 < 0.0F)
          f2 = -f2;
        f1 *= i1;
        f2 *= i2;
        if ((f1 == 0.0F) && (f2 == 0.0F))
          return null;
        int i6 = i1;
        int i7 = i2;
        if (j == 0)
        {
          if (k != 0)
          {
            int i8 = (int)(i1 / f1);
            int i9 = (int)(i2 / f2);
            if ((i8 > 0) && (i9 > 0))
            {
              i6 = (int)((i8 + 1) * f1);
              i7 = (int)((i9 + 1) * f2);
              i1 = i6;
              i2 = i7;
            }
          }
          else if ((paramArrayOfFloat != null) && (paramArrayOfFloat[0] > 0.0F) && (paramArrayOfFloat[0] < 1.0F) && (paramArrayOfFloat[3] > 0.0F) && (paramArrayOfFloat[3] < 1.0F))
          {
            i6 = (int)f1;
            i7 = (int)f2;
            if (i7 == 0)
              i7 = 1;
            i3 = (int)paramArrayOfFloat[4];
            if (i3 > i1)
            {
              while (i3 > 0)
              {
                i3 -= i1;
                if (i3 == 0)
                  break;
              }
              i3 /= 2;
            }
            i4 = (int)paramArrayOfFloat[5];
            if (i4 > i7)
              while (i4 > 0)
                i4 -= i7;
          }
          if (i5 != 0)
          {
            localBufferedImage1 = new BufferedImage((int)(f3 + 0.5F), (int)(f4 + 0.5F), 2);
            localAffineTransform1 = AffineTransform.getScaleInstance(i1 / f3, i2 / f4);
          }
          else if ((i6 < 1) || (i7 < 1))
          {
            localBufferedImage1 = new BufferedImage(1, 1, 2);
          }
          else
          {
            localBufferedImage1 = new BufferedImage(i6, i7, 2);
          }
          Graphics2D localGraphics2D1 = localBufferedImage1.createGraphics();
          AffineTransform localAffineTransform4 = localGraphics2D1.getTransform();
          localGraphics2D1.setClip(new Rectangle(0, 0, localBufferedImage1.getWidth(), localBufferedImage1.getHeight()));
          int i10 = 0;
          Rectangle localRectangle = localDynamicVectorRenderer.getOccupiedArea().getBounds();
          int i13 = 0;
          int i14 = 0;
          int i11;
          if (localRectangle.x < 0)
          {
            i11 = localRectangle.width - localRectangle.x;
            i13 = localRectangle.x;
          }
          else
          {
            i11 = localRectangle.width + localRectangle.x;
          }
          int i12;
          if (localRectangle.y < 0)
          {
            i12 = localRectangle.height - localRectangle.y;
            i14 = localRectangle.y;
          }
          else
          {
            i12 = localRectangle.height + localRectangle.y;
          }
          if (i12 == 0)
            i12 = 1;
          if (bool1)
          {
            if ((i11 < 1) || (i12 < 1))
              localObject2 = new BufferedImage(1, 1, 2);
            else
              localObject2 = new BufferedImage(i11, i12, 2);
            Graphics2D localGraphics2D2 = ((BufferedImage)localObject2).createGraphics();
            localGraphics2D2.translate(-i13, -i14);
            localDynamicVectorRenderer.setG2(localGraphics2D2);
            localDynamicVectorRenderer.paint(null, null, null);
          }
          int i15 = localRectangle.x;
          if ((i15 < 0) && (!bool1))
            i10 = (int)(-i15 * paramArrayOfFloat1[0][0]);
          float f5 = i2;
          if (bool1)
            f5 = i2 + ((BufferedImage)localObject2).getHeight() * 2;
          int i16 = 0;
          if (paramArrayOfFloat1[1][1] < 0.0F)
            i16 = (int)(-f5 / 2.0F);
          if ((i != 0) && (paramArrayOfFloat1[0][0] > 0.0F) && (paramArrayOfFloat1[1][1] < 0.0F))
            i16 += this.pageHeight - this.pageWidth;
          for (float f6 = i16; f6 < f5; f6 += f2)
            for (float f7 = i10; f7 < i1; f7 += f1)
            {
              if (k != 0)
                localGraphics2D1.translate(f7, -f6);
              else
                localGraphics2D1.translate(f7, f6);
              if (bool1)
              {
                AffineTransform localAffineTransform5 = new AffineTransform();
                ColorSpaceConvertor.drawImage(localGraphics2D1, (BufferedImage)localObject2, localAffineTransform5, null);
              }
              else
              {
                localDynamicVectorRenderer.setG2(localGraphics2D1);
                localDynamicVectorRenderer.paint(null, localAffineTransform1, null);
              }
              localGraphics2D1.setTransform(localAffineTransform4);
            }
        }
      }
    }
    else
    {
      if (i5 != 0)
      {
        localBufferedImage1 = new BufferedImage((int)(f3 + 0.5F), (int)(f4 + 0.5F), 2);
        localAffineTransform1 = AffineTransform.getScaleInstance(i1 / f3, i2 / f4);
      }
      else
      {
        localBufferedImage1 = new BufferedImage(i1, i2, 2);
      }
      localObject2 = localBufferedImage1.createGraphics();
      localDynamicVectorRenderer.setG2((Graphics2D)localObject2);
      localDynamicVectorRenderer.paint(null, null, null);
      if ((k != 0) && (localBufferedImage1.getHeight() > 1))
      {
        AffineTransform localAffineTransform3 = new AffineTransform();
        localAffineTransform3.translate(0.0D, localBufferedImage1.getHeight());
        localAffineTransform3.scale(1.0D, -1.0D);
        AffineTransformOp localAffineTransformOp2 = new AffineTransformOp(localAffineTransform3, ColorSpaces.hints);
        localBufferedImage1 = localAffineTransformOp2.filter(localBufferedImage1, null);
      }
    }
    localObjectStore.flush();
    if (localBufferedImage1 != null)
      localObject1 = new PdfTexturePaint(localBufferedImage1, new Rectangle(i3, i4, localBufferedImage1.getWidth(), localBufferedImage1.getHeight()));
    if (j != 0)
      localObject1 = new RotatedTexturePaint(bool2, arrayOfFloat, paramPdfObject, bool1, localDynamicVectorRenderer, paramArrayOfFloat1, i1, i2, f1, f2, localAffineTransform1);
    if (localObject1 != null)
      this.cachedPaints.put(paramPdfObject.getObjectRefAsString(), localObject1);
    return localObject1;
  }

  private DynamicVectorRenderer decodePatternContent(PdfObject paramPdfObject, float[][] paramArrayOfFloat, byte[] paramArrayOfByte, ObjectStore paramObjectStore)
  {
    PdfObject localPdfObject = paramPdfObject.getDictionary(2004251818);
    PdfStreamDecoder localPdfStreamDecoder = new PdfStreamDecoder(this.currentPdfFile);
    localPdfStreamDecoder.setParameters(false, true, 7, 0, false);
    localPdfStreamDecoder.setIntValue(-12, 1);
    localPdfStreamDecoder.setObjectValue(-8, paramObjectStore);
    PatternDisplay localPatternDisplay = new PatternDisplay(0, false, 20, paramObjectStore);
    localPatternDisplay.setOptimisedRotation(false);
    try
    {
      localPdfStreamDecoder.setObjectValue(23, localPatternDisplay);
      if (localPdfObject != null)
        localPdfStreamDecoder.readResources(localPdfObject, true);
      localPdfStreamDecoder.setDefaultColors(this.gs.getStrokeColor(), this.gs.getNonstrokeColor());
      GraphicsState localGraphicsState = new GraphicsState(false, 0, 0);
      if (paramArrayOfFloat != null)
        localGraphicsState.CTM = paramArrayOfFloat;
      localPdfStreamDecoder.decodePageContent(localGraphicsState, paramArrayOfByte);
    }
    catch (PdfException localPdfException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception: " + localPdfException.getMessage());
    }
    return localPatternDisplay;
  }

  private PdfPaint setupShading(PdfObject paramPdfObject, float[][] paramArrayOfFloat)
  {
    PdfObject localPdfObject1 = paramPdfObject.getDictionary(878474856);
    PdfObject localPdfObject2 = localPdfObject1.getDictionary(2087749783);
    Object localObject = ColorspaceFactory.getColorSpaceInstance(this.currentPdfFile, localPdfObject2);
    if ((((GenericColorSpace)localObject).getID() == 1247168582) && (localPdfObject2.getParameterConstant(2054519176) == 1498837125))
      localObject = new DeviceCMYKColorSpace();
    if (localPdfObject1 == null)
      return null;
    return ShadingFactory.createShading(localPdfObject1, this.isPrinting, (GenericColorSpace)localObject, this.currentPdfFile, paramArrayOfFloat, this.pageHeight, this.colorsReversed, this.gs.CTM);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.color.PatternColorSpace
 * JD-Core Version:    0.6.2
 */