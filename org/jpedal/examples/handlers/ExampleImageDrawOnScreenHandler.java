package org.jpedal.examples.handlers;

import com.sun.media.jai.codec.FileSeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecodeParam;
import com.sun.media.jai.codec.ImageDecoder;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.PrintStream;
import javax.media.jai.ImageLayout;
import javax.media.jai.InterpolationBicubic;
import javax.media.jai.JAI;
import javax.media.jai.KernelJAI;
import javax.media.jai.NullOpImage;
import javax.media.jai.RenderedOp;
import javax.media.jai.TiledImage;
import javax.media.jai.operator.AbsoluteDescriptor;
import javax.media.jai.operator.CropDescriptor;
import org.jpedal.color.ColorSpaces;
import org.jpedal.external.ImageHandler;
import org.jpedal.io.JAIHelper;
import org.jpedal.io.ObjectStore;
import org.jpedal.objects.GraphicsState;
import org.jpedal.objects.raw.PdfObject;
import org.jpedal.utils.LogWriter;

public class ExampleImageDrawOnScreenHandler
  implements ImageHandler
{
  public boolean alwaysIgnoreGenericHandler()
  {
    return false;
  }

  public BufferedImage processImageData(GraphicsState paramGraphicsState, PdfObject paramPdfObject)
  {
    return null;
  }

  public boolean imageHasBeenScaled()
  {
    return false;
  }

  public boolean drawImageOnscreen(BufferedImage paramBufferedImage, int paramInt, AffineTransform paramAffineTransform, String paramString, Graphics2D paramGraphics2D, boolean paramBoolean1, ObjectStore paramObjectStore, boolean paramBoolean2)
  {
    double[] arrayOfDouble = new double[6];
    paramAffineTransform.getMatrix(arrayOfDouble);
    int i = (arrayOfDouble[0] * arrayOfDouble[1] != 0.0D) || (arrayOfDouble[2] * arrayOfDouble[3] != 0.0D) ? 1 : 0;
    if ((i != 0) || (paramBufferedImage.getWidth() < 800) || (paramBoolean1))
    {
      paramGraphics2D.drawImage(paramBufferedImage, paramAffineTransform, null);
    }
    else
    {
      double d1 = 0.0D;
      double d2 = 0.0D;
      if (paramInt != 0)
      {
        double d3;
        if ((paramInt & 0x2) == 2)
        {
          if ((arrayOfDouble[0] > 0.0D) && (arrayOfDouble[3] < 0.0D) && ((paramInt & 0x1) == 1))
          {
            d3 = arrayOfDouble[0] * paramBufferedImage.getWidth();
            double d4 = -(arrayOfDouble[3] * paramBufferedImage.getHeight());
            d1 = arrayOfDouble[5] - d4;
            arrayOfDouble[5] = d4;
            if (d3 - (int)d3 > 0.5D)
              d2 -= 1.0D;
          }
          else if ((arrayOfDouble[0] < 0.0D) && (arrayOfDouble[3] > 0.0D))
          {
            d3 = arrayOfDouble[0];
            arrayOfDouble[0] = arrayOfDouble[3];
            arrayOfDouble[3] = d3;
            arrayOfDouble[4] = 0.0D;
            arrayOfDouble[5] = ((int)(arrayOfDouble[4] * paramBufferedImage.getHeight() / paramBufferedImage.getWidth()));
          }
        }
        else if ((arrayOfDouble[0] > 0.0D) && (arrayOfDouble[3] > 0.0D) && ((paramInt & 0x1) == 1))
        {
          d1 = arrayOfDouble[5];
          d3 = arrayOfDouble[0];
          arrayOfDouble[0] = arrayOfDouble[3];
          arrayOfDouble[3] = d3;
          arrayOfDouble[4] = 0.0D;
          arrayOfDouble[5] = ((int)(arrayOfDouble[4] * paramBufferedImage.getHeight() / paramBufferedImage.getWidth()));
        }
        paramAffineTransform = new AffineTransform(arrayOfDouble);
      }
      int j = 0;
      Object localObject;
      if (JAIHelper.isJAIused())
      {
        if ((j == 0) && (paramString != null))
        {
          j = 1;
          try
          {
            Dimension localDimension = new Dimension(512, 512);
            FileSeekableStream localFileSeekableStream = new FileSeekableStream(new File(paramObjectStore.getFileForCachedImage(paramString)));
            ImageDecodeParam localImageDecodeParam = null;
            ImageDecoder localImageDecoder = ImageCodec.createImageDecoder("tiff", localFileSeekableStream, localImageDecodeParam);
            int n = 0;
            localObject = new NullOpImage(localImageDecoder.decodeAsRenderedImage(n), null, null, 2);
            if (((RenderedImage)localObject).getTileWidth() * ((RenderedImage)localObject).getTileHeight() > localDimension.width * localDimension.height)
            {
              ColorModel localColorModel = ((RenderedImage)localObject).getColorModel();
              SampleModel localSampleModel = ((RenderedImage)localObject).getSampleModel().createCompatibleSampleModel(localDimension.width, localDimension.height);
              ImageLayout localImageLayout = new ImageLayout(0, 0, localDimension.width, localDimension.height, localSampleModel, localColorModel);
              RenderingHints localRenderingHints = new RenderingHints(JAI.KEY_IMAGE_LAYOUT, localImageLayout);
              localObject = AbsoluteDescriptor.create((RenderedImage)localObject, localRenderingHints);
            }
            paramBufferedImage = JAI.create("affine", (RenderedImage)localObject, paramAffineTransform, new InterpolationBicubic(1)).getAsBufferedImage();
          }
          catch (Exception localException1)
          {
            j = 0;
            localException1.printStackTrace();
          }
          catch (Error localError1)
          {
            j = 0;
          }
        }
        if (j == 0)
          LogWriter.writeLog("Unable to use JAI for image inversion");
      }
      else
      {
        j = 1;
      }
      if (j == 0)
      {
        j = 1;
        try
        {
          AffineTransformOp localAffineTransformOp = new AffineTransformOp(paramAffineTransform, ColorSpaces.hints);
          paramBufferedImage = localAffineTransformOp.filter(paramBufferedImage, null);
        }
        catch (Exception localException2)
        {
          j = 0;
          localException2.printStackTrace();
        }
        catch (Error localError2)
        {
          j = 0;
        }
      }
      if (j != 0)
      {
        Shape localShape = null;
        if ((paramBoolean2) && (d1 == 0.0D))
        {
          localObject = new double[6];
          paramGraphics2D.getTransform().getMatrix((double[])localObject);
          d2 = localObject[4] / localObject[0];
          if (d2 > 0.0D)
            d2 = -d2;
          d1 = localObject[5] / localObject[3];
          if (d1 > 0.0D)
            d1 = -d1;
          d1 = -(d1 + paramBufferedImage.getHeight());
        }
        if (d1 != 0.0D)
        {
          localShape = paramGraphics2D.getClip();
          double d5 = paramGraphics2D.getTransform().getScaleX();
          int k;
          if (d5 < 1.0D)
            k = (int)(1.0D / d5);
          else
            k = (int)(d5 + 0.5D);
          double d6 = paramGraphics2D.getTransform().getScaleY();
          int m;
          if (d6 < 1.0D)
            m = (int)(1.0D / d6);
          else
            m = (int)(d6 + 0.5D);
          paramGraphics2D.clipRect((int)d2, (int)(d1 + 1.5D), paramBufferedImage.getWidth() - k, paramBufferedImage.getHeight() - m);
        }
        paramGraphics2D.drawImage(paramBufferedImage, (int)d2, (int)d1, null);
        if (localShape != null)
          paramGraphics2D.setClip(localShape);
      }
      else
      {
        paramGraphics2D.drawImage(paramBufferedImage, paramAffineTransform, null);
      }
    }
    return true;
  }

  public static synchronized BufferedImage getAffineTransform(RenderedImage paramRenderedImage, AffineTransform paramAffineTransform, RenderingHints paramRenderingHints, float paramFloat)
  {
    double[] arrayOfDouble = new double[6];
    paramAffineTransform.getMatrix(arrayOfDouble);
    Dimension localDimension = new Dimension((int)(arrayOfDouble[0] * paramRenderedImage.getWidth()), (int)(arrayOfDouble[3] * paramRenderedImage.getHeight()));
    Object localObject1 = new Point2D.Double(paramRenderedImage.getTileGridXOffset(), paramRenderedImage.getTileGridYOffset());
    localObject1 = paramAffineTransform.transform((Point2D)localObject1, null);
    Rectangle localRectangle1 = new Rectangle((int)((Point2D)localObject1).getX(), (int)((Point2D)localObject1).getY(), paramRenderedImage.getWidth(), paramRenderedImage.getHeight());
    Rectangle localRectangle2 = paramAffineTransform.createTransformedShape(localRectangle1).getBounds();
    System.out.println(">>" + paramRenderedImage);
    System.out.println(">>" + localRectangle2);
    ColorModel localColorModel = paramRenderedImage.getColorModel();
    SampleModel localSampleModel = localColorModel.createCompatibleSampleModel(paramRenderedImage.getTileWidth(), paramRenderedImage.getTileHeight());
    TiledImage localTiledImage = new TiledImage((int)localRectangle2.getMinX(), (int)localRectangle2.getMinY(), (int)localRectangle2.getWidth(), (int)localRectangle2.getHeight(), (int)((Point2D)localObject1).getX(), (int)((Point2D)localObject1).getY(), localSampleModel, localColorModel);
    KernelJAI localKernelJAI = createBlurKernel((float)paramAffineTransform.getScaleX(), (float)paramAffineTransform.getScaleY(), paramFloat);
    int i = localKernelJAI.getWidth();
    int j = localKernelJAI.getHeight();
    AffineTransform localAffineTransform1 = (AffineTransform)paramAffineTransform.clone();
    localAffineTransform1.translate(((Point2D)localObject1).getX(), ((Point2D)localObject1).getY());
    double d1 = paramRenderedImage.getWidth() / localDimension.getWidth();
    double d2 = paramRenderedImage.getHeight() / localDimension.getHeight();
    if (d1 % 2.0D > 0.0D)
      d1 += 1.0D;
    if (d2 % 2.0D > 0.0D)
      d2 += 1.0D;
    int k = (int)d1;
    int m = (int)d2;
    for (int n = 0; n < k; n++)
      for (int i1 = 0; i1 < m; i1++)
      {
        int i2 = (int)(n * localDimension.getWidth());
        int i3 = (int)(i1 * localDimension.getWidth());
        int i4 = (int)localDimension.getWidth();
        int i5 = (int)localDimension.getHeight();
        if (i2 < 0)
          i2 = 0;
        if (i3 < 0)
          i3 = 0;
        if (i2 + i4 > paramRenderedImage.getWidth())
          i4 = paramRenderedImage.getWidth() - i2;
        if (i3 + i5 > paramRenderedImage.getHeight())
          i5 = paramRenderedImage.getHeight() - i3;
        Rectangle localRectangle3 = new Rectangle(i2, i3, i4, i5);
        i2 = localRectangle3.x - i;
        i3 = localRectangle3.y - j;
        i4 = localRectangle3.width + i * 2;
        i5 = localRectangle3.height + j * 2;
        if (i2 < 0)
          i2 = 0;
        if (i3 < 0)
          i3 = 0;
        if (i2 + i4 > paramRenderedImage.getWidth())
          i4 = paramRenderedImage.getWidth() - i2;
        if (i3 + i5 > paramRenderedImage.getHeight())
          i5 = paramRenderedImage.getHeight() - i3;
        Rectangle localRectangle4 = new Rectangle(i2, i3, i4, i5);
        WritableRaster localWritableRaster;
        try
        {
          localWritableRaster = (WritableRaster)paramRenderedImage.getData(localRectangle4);
        }
        catch (Exception localException)
        {
          localWritableRaster = null;
        }
        if (localWritableRaster == null)
        {
          System.out.println(">>>");
        }
        else
        {
          localWritableRaster = localWritableRaster.createWritableTranslatedChild(0, 0);
          BufferedImage localBufferedImage = new BufferedImage(paramRenderedImage.getColorModel(), localWritableRaster, paramRenderedImage.getColorModel().isAlphaPremultiplied(), null);
          Object localObject2 = localBufferedImage;
          if (paramFloat > 0.0F)
          {
            localObject2 = JAI.create("convolve", localBufferedImage, localKernelJAI);
            if ((localWritableRaster.getWidth() - i * 2 > 0) && (localWritableRaster.getHeight() - j * 2 > 0))
              localObject2 = CropDescriptor.create((RenderedImage)localObject2, Float.valueOf(i), Float.valueOf(j), Float.valueOf(localWritableRaster.getWidth() - i * 2), Float.valueOf(localWritableRaster.getHeight() - j * 2), paramRenderingHints);
          }
          AffineTransform localAffineTransform2 = (AffineTransform)localAffineTransform1.clone();
          localAffineTransform2.translate(localRectangle3.getX(), localRectangle3.getY());
          Graphics2D localGraphics2D = localTiledImage.createGraphics();
          localGraphics2D.drawRenderedImage((RenderedImage)localObject2, localAffineTransform2);
          localGraphics2D.dispose();
        }
      }
    return localTiledImage.getAsBufferedImage();
  }

  public static KernelJAI createBlurKernel(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    paramFloat1 = Math.abs(paramFloat1);
    paramFloat2 = Math.abs(paramFloat2);
    int i = 1 + Math.round(paramFloat3 / paramFloat1);
    int j = 1 + Math.round(paramFloat3 / paramFloat2);
    if ((i == 4) && (j == 4))
    {
      i = 3;
      j = 3;
    }
    if ((i == 5) && (j == 5))
    {
      i = 3;
      j = 3;
    }
    if ((i == 6) && (j == 6))
    {
      i = 7;
      j = 7;
    }
    float[] arrayOfFloat = new float[i * j];
    float f = 1.0F / arrayOfFloat.length;
    for (int k = 0; k < arrayOfFloat.length; k++)
      arrayOfFloat[k] = f;
    return new KernelJAI(i, j, arrayOfFloat);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.handlers.ExampleImageDrawOnScreenHandler
 * JD-Core Version:    0.6.2
 */