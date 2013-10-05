package org.jpedal.parser;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.color.ColorSpace;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.DataBufferByte;
import java.awt.image.Kernel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.PrintStream;
import org.jpedal.color.ColorSpaces;
import org.jpedal.color.ColorspaceFactory;
import org.jpedal.color.DeviceCMYKColorSpace;
import org.jpedal.color.DeviceGrayColorSpace;
import org.jpedal.color.DeviceRGBColorSpace;
import org.jpedal.color.GenericColorSpace;
import org.jpedal.exception.PdfException;
import org.jpedal.external.ImageHandler;
import org.jpedal.images.ImageOps;
import org.jpedal.images.ImageTransformer;
import org.jpedal.images.ImageTransformerDouble;
import org.jpedal.images.SamplingFactory;
import org.jpedal.io.ColorSpaceConvertor;
import org.jpedal.io.ErrorTracker;
import org.jpedal.io.IDObjectDecoder;
import org.jpedal.io.JAIHelper;
import org.jpedal.io.ObjectStore;
import org.jpedal.io.ObjectUtils;
import org.jpedal.io.PdfFilteredReader;
import org.jpedal.io.PdfObjectReader;
import org.jpedal.objects.GraphicsState;
import org.jpedal.objects.PdfImageData;
import org.jpedal.objects.PdfPageData;
import org.jpedal.objects.raw.MaskObject;
import org.jpedal.objects.raw.PdfArrayIterator;
import org.jpedal.objects.raw.PdfObject;
import org.jpedal.objects.raw.XObject;
import org.jpedal.render.DynamicVectorRenderer;
import org.jpedal.render.RenderUtils;
import org.jpedal.utils.LogWriter;

public class ImageDecoder extends BaseDecoder
{
  PdfImageData pdfImages = null;
  private boolean getSamplingOnly = false;
  boolean isMask = true;
  String imagesInFile = null;
  boolean isPrinting;
  ImageHandler customImageHandler;
  boolean useHiResImageForDisplay;
  boolean isType3Font;
  boolean renderDirectly;
  int formLevel;
  PdfPageData pageData;
  ObjectStore objectStoreStreamRef;
  boolean clippedImagesExtracted = true;
  private boolean extractRawCMYK = false;
  boolean finalImagesExtracted = true;
  private boolean doNotRotate = false;
  boolean createScaledVersion = true;
  boolean renderImages = false;
  boolean rawImagesExtracted = true;
  private int optionsApplied = 0;
  private String currentImage = "";
  private String formName;

  public ImageDecoder(ImageHandler paramImageHandler, ObjectStore paramObjectStore, boolean paramBoolean, PdfImageData paramPdfImageData, int paramInt, PdfPageData paramPdfPageData, String paramString1, String paramString2)
  {
    this.formName = paramString2;
    this.customImageHandler = paramImageHandler;
    this.objectStoreStreamRef = paramObjectStore;
    this.renderDirectly = paramBoolean;
    this.pdfImages = paramPdfImageData;
    this.formLevel = paramInt;
    this.pageData = paramPdfPageData;
    this.imagesInFile = paramString1;
  }

  private GenericColorSpace setupXObjectColorspace(PdfObject paramPdfObject, int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte)
  {
    PdfObject localPdfObject1 = paramPdfObject.getDictionary(2087749783);
    Object localObject = new DeviceRGBColorSpace();
    if (localPdfObject1 != null)
    {
      localObject = ColorspaceFactory.getColorSpaceInstance(this.currentPdfFile, localPdfObject1, this.cache.XObjectColorspaces);
      ((GenericColorSpace)localObject).setPrinting(this.isPrinting);
      this.cache.put(1, ((GenericColorSpace)localObject).getID(), "x");
      if ((paramInt1 == 1) && (((GenericColorSpace)localObject).getID() == 1785221209) && (paramPdfObject.getDictionary(489767739) == null))
      {
        arrayOfByte = ((GenericColorSpace)localObject).getIndexedMap();
        if ((((GenericColorSpace)localObject).getIndexedMap() == null) || ((arrayOfByte.length == 6) && (arrayOfByte[0] == 0) && (arrayOfByte[1] == 0) && (arrayOfByte[2] == 0)))
          localObject = new DeviceGrayColorSpace();
      }
    }
    byte[] arrayOfByte = ((GenericColorSpace)localObject).getIndexedMap();
    if ((paramInt1 == 8) && (arrayOfByte != null) && (((GenericColorSpace)localObject).getID() == 1785221209) && (paramInt2 * paramInt3 == paramArrayOfByte.length))
    {
      localPdfObject2 = paramPdfObject.getDictionary(489767739);
      if (localPdfObject2 != null)
      {
        int[] arrayOfInt = localPdfObject2.getIntArray(489767739);
        if ((arrayOfInt != null) && (arrayOfInt.length == 2) && (arrayOfInt[0] == 255) && (arrayOfInt[0] == arrayOfInt[1]) && (((GenericColorSpace)localObject).getIndexedMap() != null) && (((GenericColorSpace)localObject).getIndexedMap().length == 768))
        {
          int i = 1;
          for (int j = 0; j < 768; j++)
            if (arrayOfByte[j] != 0)
            {
              i = 0;
              j = 768;
            }
          if (i != 0)
            localObject = new DeviceGrayColorSpace();
        }
      }
    }
    PdfObject localPdfObject2 = paramPdfObject.getDictionary(1888135062);
    if (localPdfObject2 != null)
      ((GenericColorSpace)localObject).setDecodeParms(localPdfObject2);
    ((GenericColorSpace)localObject).setIntent(paramPdfObject.getName(1144346498));
    return localObject;
  }

  public BufferedImage processImageXObject(PdfObject paramPdfObject, String paramString1, byte[] paramArrayOfByte, boolean paramBoolean, String paramString2)
    throws PdfException
  {
    BufferedImage localBufferedImage = null;
    paramString1 = new StringBuilder().append(this.fileName).append('-').append(paramString1).toString();
    int i = 1;
    int j = paramPdfObject.getInt(959726687);
    int k = paramPdfObject.getInt(959926393);
    int m = paramPdfObject.getInt(-1344207655);
    if (m != -1)
      i = m;
    this.isMask = paramPdfObject.getBoolean(1516403337);
    boolean bool = this.isMask;
    GenericColorSpace localGenericColorSpace = setupXObjectColorspace(paramPdfObject, i, j, k, paramArrayOfByte);
    if (LogWriter.isOutput())
      LogWriter.writeLog(new StringBuilder().append("Processing XObject: ").append(paramString1).append(' ').append(paramPdfObject.getObjectRefAsString()).append(" width=").append(j).append(" Height=").append(k).append(" Depth=").append(i).append(" colorspace=").append(localGenericColorSpace).toString());
    if (this.customImageHandler != null)
      localBufferedImage = this.customImageHandler.processImageData(this.gs, paramPdfObject);
    PdfObject localPdfObject = paramPdfObject.getDictionary(489767774);
    byte[] arrayOfByte1 = localGenericColorSpace.getIndexedMap();
    if ((localPdfObject != null) && (arrayOfByte1 != null) && (arrayOfByte1.length == 3) && (localGenericColorSpace.getID() != 1247168582))
    {
      paramPdfObject = localPdfObject;
      paramPdfObject.setFloatArray(859785322, new float[] { 1.0F, 0.0F });
      paramArrayOfByte = this.currentPdfFile.readStream(paramPdfObject, true, true, false, false, false, null);
      i = 1;
      j = paramPdfObject.getInt(959726687);
      k = paramPdfObject.getInt(959926393);
      m = paramPdfObject.getInt(-1344207655);
      if (m != -1)
        i = m;
      localGenericColorSpace = setupXObjectColorspace(paramPdfObject, i, j, k, paramArrayOfByte);
    }
    Object localObject;
    if ((localPdfObject != null) && (paramPdfObject.getInt(959726687) == 1) && (paramPdfObject.getInt(959926393) == 1) && (paramPdfObject.getInt(-1344207655) == 8))
    {
      int n = -1;
      paramPdfObject = localPdfObject;
      localObject = paramPdfObject.getFloatArray(859785322);
      if (localObject != null)
        n = (byte)(int)(localObject[0] * 255.0F);
      byte[] arrayOfByte2 = { 0, 0, 0, -1, -1, -1 };
      byte[] arrayOfByte3 = this.currentPdfFile.readStream(paramPdfObject, true, true, false, false, false, null);
      j = paramPdfObject.getInt(959726687);
      k = paramPdfObject.getInt(959926393);
      int i1 = j * k * 4;
      paramArrayOfByte = new byte[i1];
      ColorSpaceConvertor.flatten1bpc(j, arrayOfByte3, arrayOfByte2, true, i1, n, paramArrayOfByte);
      localBufferedImage = new BufferedImage(j, k, 2);
      DataBufferByte localDataBufferByte = new DataBufferByte(paramArrayOfByte, paramArrayOfByte.length);
      WritableRaster localWritableRaster = Raster.createInterleavedRaster(localDataBufferByte, j, k, j * 4, 4, new int[] { 0, 1, 2, 3 }, null);
      localBufferedImage.setData(localWritableRaster);
    }
    else if ((this.customImageHandler == null) || ((localBufferedImage == null) && (!this.customImageHandler.alwaysIgnoreGenericHandler())))
    {
      localBufferedImage = processImage(localGenericColorSpace, paramArrayOfByte, paramString1, j, k, i, bool, paramPdfObject, paramBoolean, 2);
    }
    if ((ImageCommands.trackImages) && (localBufferedImage != null) && (paramString2 != null))
    {
      float f = this.gs.CTM[0][0];
      if (f == 0.0F)
        f = this.gs.CTM[0][1];
      if (f < 0.0F)
        f = -f;
      f = (int)(j / f * 100.0F);
      localObject = new StringBuilder(paramString2);
      ((StringBuilder)localObject).append(" w=");
      ((StringBuilder)localObject).append(String.valueOf(j));
      ((StringBuilder)localObject).append(" h=");
      ((StringBuilder)localObject).append(String.valueOf(k));
      ((StringBuilder)localObject).append(' ');
      ((StringBuilder)localObject).append(String.valueOf((int)f));
      ((StringBuilder)localObject).append(' ');
      ((StringBuilder)localObject).append(ColorSpaces.IDtoString(localGenericColorSpace.getID()));
      ((StringBuilder)localObject).append(" (");
      ((StringBuilder)localObject).append(String.valueOf(localBufferedImage.getWidth()));
      ((StringBuilder)localObject).append(' ');
      ((StringBuilder)localObject).append(String.valueOf(localBufferedImage.getHeight()));
      ((StringBuilder)localObject).append(" type=");
      ((StringBuilder)localObject).append(String.valueOf(localBufferedImage.getType()));
      ((StringBuilder)localObject).append(')');
      if (this.imagesInFile.isEmpty())
      {
        this.imagesInFile = ((StringBuilder)localObject).toString();
      }
      else
      {
        ((StringBuilder)localObject).append('\n');
        ((StringBuilder)localObject).append(this.imagesInFile);
        this.imagesInFile = ((StringBuilder)localObject).toString();
      }
    }
    return localBufferedImage;
  }

  public int processDOImage(String paramString, int paramInt, PdfObject paramPdfObject)
    throws PdfException
  {
    if (this.formLevel > 0)
      paramString = new StringBuilder().append(this.formName).append('_').append(this.formLevel).append('_').append(paramString).toString();
    String str = null;
    if (ImageCommands.rejectSuperimposedImages)
      str = new StringBuilder().append((int)this.gs.CTM[2][0]).append("-").append((int)this.gs.CTM[2][1]).append('-').append((int)this.gs.CTM[0][0]).append('-').append((int)this.gs.CTM[1][1]).append('-').append((int)this.gs.CTM[0][1]).append('-').append((int)this.gs.CTM[1][0]).toString();
    try
    {
      processXImage(paramString, paramString, str, paramPdfObject);
    }
    catch (Error localError)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog(new StringBuilder().append("Error: ").append(localError.getMessage()).toString());
      this.imagesProcessedFully = false;
      this.errorTracker.addPageFailureMessage(new StringBuilder().append("Error ").append(localError).append(" in DO").toString());
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog(new StringBuilder().append("Exception ").append(localException).toString());
      this.imagesProcessedFully = false;
      this.errorTracker.addPageFailureMessage(new StringBuilder().append("Error ").append(localException).append(" in DO").toString());
      if (localException.getMessage().contains("JPeg 2000"))
        throw new RuntimeException("XX JPeg 2000 Images needs the VM parameter -Dorg.jpedal.jai=true switch turned on");
    }
    return paramInt;
  }

  private void processXImage(String paramString1, String paramString2, String paramString3, PdfObject paramPdfObject)
    throws PdfException
  {
    if (ImageCommands.trackImages)
    {
      paramString2 = new StringBuilder().append(paramString2).append(" Image").toString();
      if (this.imagesInFile == null)
        this.imagesInFile = "";
    }
    int i = (this.current.getType() == 4) || (this.current.getType() == 5) || (this.current.getType() == 7) || (this.current.getType() == 6) ? 1 : 0;
    if ((this.renderImages) || (this.finalImagesExtracted) || (this.clippedImagesExtracted) || (this.rawImagesExtracted))
    {
      byte[] arrayOfByte = this.currentPdfFile.readStream(paramPdfObject, true, true, false, false, false, paramPdfObject.getCacheName(this.currentPdfFile.getObjectReader()));
      if (arrayOfByte == null)
        this.imagesProcessedFully = false;
      if (arrayOfByte != null)
      {
        boolean bool = false;
        BufferedImage localBufferedImage = null;
        this.currentImage = new StringBuilder().append(this.fileName).append('-').append(paramString1).toString();
        if (!bool)
          localBufferedImage = processImageXObject(paramPdfObject, paramString1, arrayOfByte, true, paramString2);
        if ((localBufferedImage != null) && (localBufferedImage.getWidth() == 1) && (localBufferedImage.getHeight() == 1) && (this.isType3Font))
        {
          localBufferedImage.flush();
          localBufferedImage = null;
        }
        if ((localBufferedImage != null) || (bool))
        {
          float[][] arrayOfFloat = (float[][])null;
          if ((i == 0) && ((this.renderDirectly) || (this.useHiResImageForDisplay)))
          {
            this.gs.x = this.gs.CTM[2][0];
            this.gs.y = this.gs.CTM[2][1];
            int j;
            if ((this.finalImagesExtracted) || (this.rawImagesExtracted))
            {
              j = (int)Math.abs(this.gs.CTM[0][0]);
              if (j == 0)
                j = (int)Math.abs(this.gs.CTM[0][1]);
              int k = (int)Math.abs(this.gs.CTM[1][1]);
              if (k == 0)
                k = (int)Math.abs(this.gs.CTM[1][0]);
              this.pdfImages.setImageInfo(this.currentImage, this.pageNum, this.gs.x, this.gs.y, j, k);
            }
            if (this.renderDirectly)
            {
              this.current.drawImage(this.pageNum, localBufferedImage, this.gs, bool, paramString1, this.optionsApplied, -1);
            }
            else if ((localBufferedImage != null) || (bool))
            {
              j = this.current.drawImage(this.pageNum, localBufferedImage, this.gs, bool, paramString1, this.optionsApplied, -1);
              if ((ImageCommands.rejectSuperimposedImages) && (paramString3 != null))
                this.cache.setImposedKey(paramString3, j);
            }
          }
          else if ((this.clippedImagesExtracted) || (i != 0))
          {
            generateTransformedImage(localBufferedImage, paramString1);
          }
          else
          {
            try
            {
              generateTransformedImageSingle(localBufferedImage, paramString1);
            }
            catch (Exception localException)
            {
              if (LogWriter.isOutput())
                LogWriter.writeLog(new StringBuilder().append("Exception ").append(localException).append(" on transforming image in file").toString());
            }
          }
          if (localBufferedImage != null)
            localBufferedImage.flush();
          if (arrayOfFloat != null)
            this.gs.CTM = arrayOfFloat;
        }
      }
    }
  }

  public void setSamplingOnly(boolean paramBoolean)
  {
    this.getSamplingOnly = paramBoolean;
  }

  public String getImagesInFile()
  {
    return this.imagesInFile;
  }

  public void setParameters(boolean paramBoolean1, boolean paramBoolean2, int paramInt1, int paramInt2, boolean paramBoolean3, boolean paramBoolean4, boolean paramBoolean5)
  {
    this.isPageContent = paramBoolean1;
    this.renderPage = paramBoolean2;
    this.renderMode = paramInt1;
    this.extractionMode = paramInt2;
    this.isPrinting = paramBoolean3;
    this.isType3Font = paramBoolean4;
    this.useHiResImageForDisplay = paramBoolean5;
    this.renderImages = ((paramBoolean2) && ((paramInt1 & 0x2) == 2));
    this.finalImagesExtracted = ((paramInt2 & 0x4) == 4);
    this.extractRawCMYK = ((paramInt2 & 0x80) == 128);
    this.clippedImagesExtracted = ((paramInt2 & 0x20) == 32);
    this.rawImagesExtracted = ((paramInt2 & 0x2) == 2);
    this.createScaledVersion = ((this.finalImagesExtracted) || (this.renderImages));
  }

  public int processIDImage(int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3)
    throws Exception
  {
    XObject localXObject = new XObject(6420);
    IDObjectDecoder localIDObjectDecoder = new IDObjectDecoder(this.currentPdfFile.getObjectReader());
    localIDObjectDecoder.setEndPt(paramInt1 - 2);
    localIDObjectDecoder.readDictionaryAsObject(localXObject, paramInt2, paramArrayOfByte);
    BufferedImage localBufferedImage = null;
    int i = paramInt1 + 1;
    int n = i;
    int i1 = paramArrayOfByte.length;
    while (((i1 - n <= 3) || (paramArrayOfByte[(n + 1)] != 69) || (paramArrayOfByte[(n + 2)] != 73) || (paramArrayOfByte[(n + 3)] != 10)) && ((i1 - n <= 3) || ((paramArrayOfByte[n] != 32) && (paramArrayOfByte[n] != 10) && (paramArrayOfByte[n] != 13) && ((paramArrayOfByte[(n + 3)] != 32) || (paramArrayOfByte[(n + 4)] != 81))) || (paramArrayOfByte[(n + 1)] != 69) || (paramArrayOfByte[(n + 2)] != 73) || ((paramArrayOfByte[(n + 3)] != 32) && (paramArrayOfByte[(n + 3)] != 10) && (paramArrayOfByte[(n + 3)] != 13))))
    {
      n++;
      if (n == i1)
        break;
    }
    if ((this.renderImages) || (this.finalImagesExtracted) || (this.clippedImagesExtracted) || (this.rawImagesExtracted))
    {
      String str = new StringBuilder().append(this.fileName).append("-IN-").append(paramInt3).toString();
      int i2 = n;
      if ((n < paramArrayOfByte.length) && (paramArrayOfByte[i2] != 32) && (paramArrayOfByte[i2] != 10) && (paramArrayOfByte[i2] != 13))
        i2++;
      if (paramArrayOfByte[i] == 10)
        i++;
      byte[] arrayOfByte = new byte[i2 - i];
      System.arraycopy(paramArrayOfByte, i, arrayOfByte, 0, i2 - i);
      localXObject.setStream(arrayOfByte);
      PdfObject localPdfObject = localXObject.getDictionary(2087749783);
      if (localPdfObject != null)
      {
        localObject1 = localPdfObject.getGeneralStringValue();
        if (localObject1 != null)
        {
          Object localObject2 = this.cache.get(2, localObject1);
          if (localObject2 != null)
            localPdfObject = (PdfObject)localObject2;
        }
      }
      if (this.customImageHandler != null)
        localBufferedImage = this.customImageHandler.processImageData(this.gs, localXObject);
      Object localObject1 = localXObject.getMixedArray(1011108731);
      int i4 = 0;
      if ((localObject1 != null) && (((PdfArrayIterator)localObject1).hasMoreTokens()))
      {
        int i3 = ((PdfArrayIterator)localObject1).getNextValueAsConstant(false);
        i4 = (i3 != 1399277700) && (i3 != 1180911742) ? 1 : 0;
      }
      int j = localXObject.getInt(959726687);
      int k = localXObject.getInt(959926393);
      int m = localXObject.getInt(-1344207655);
      boolean bool = localXObject.getBoolean(1516403337);
      if (i4 != 0)
      {
        localObject3 = new PdfFilteredReader();
        arrayOfByte = ((PdfFilteredReader)localObject3).decodeFilters(ObjectUtils.setupDecodeParms(localXObject, this.currentPdfFile.getObjectReader()), arrayOfByte, (PdfArrayIterator)localObject1, j, k, null);
      }
      Object localObject3 = new DeviceRGBColorSpace();
      if (localPdfObject != null)
      {
        localObject3 = ColorspaceFactory.getColorSpaceInstance(this.currentPdfFile, localPdfObject);
        ((GenericColorSpace)localObject3).setPrinting(this.isPrinting);
        this.cache.put(1, ((GenericColorSpace)localObject3).getID(), "x");
      }
      if (arrayOfByte != null)
      {
        if ((this.customImageHandler == null) || ((localBufferedImage == null) && (!this.customImageHandler.alwaysIgnoreGenericHandler())))
        {
          localBufferedImage = processImage((GenericColorSpace)localObject3, arrayOfByte, str, j, k, m, bool, localXObject, false, 0);
          this.currentImage = str;
        }
        if ((this.isPrinting) && (localBufferedImage != null) && (this.gs != null) && (localBufferedImage.getHeight() == 1) && (this.gs.CTM[1][1] < 1.0F))
          localBufferedImage = null;
        if (localBufferedImage != null)
        {
          if ((this.current.getType() == 4) || (this.current.getType() == 5) || (this.current.getType() == 7) || (this.current.getType() == 6))
          {
            generateTransformedImage(localBufferedImage, str);
          }
          else if ((this.renderDirectly) || (this.useHiResImageForDisplay))
          {
            this.gs.x = this.gs.CTM[2][0];
            this.gs.y = this.gs.CTM[2][1];
            this.current.drawImage(this.pageNum, localBufferedImage, this.gs, false, str, this.optionsApplied, -1);
          }
          else if (this.clippedImagesExtracted)
          {
            generateTransformedImage(localBufferedImage, str);
          }
          else
          {
            generateTransformedImageSingle(localBufferedImage, str);
          }
          if (localBufferedImage != null)
            localBufferedImage.flush();
        }
      }
    }
    paramInt1 = n + 3;
    return paramInt1;
  }

  public void generateTransformedImage(BufferedImage paramBufferedImage, String paramString)
  {
    float f1 = 0.0F;
    float f2 = 0.0F;
    if (paramBufferedImage != null)
    {
      ImageTransformerDouble localImageTransformerDouble = new ImageTransformerDouble(this.gs, paramBufferedImage, this.createScaledVersion);
      localImageTransformerDouble.doubleScaleTransformShear();
      paramBufferedImage = localImageTransformerDouble.getImage();
      String str;
      if ((this.current.getType() != 4) && (this.current.getType() != 5) && (this.current.getType() != 7) && (this.current.getType() != 6))
      {
        str = this.objectStoreStreamRef.getImageType(this.currentImage);
        if (str == null)
          str = "tif";
        if (this.objectStoreStreamRef.saveStoredImage(new StringBuilder().append("CLIP_").append(this.currentImage).toString(), ImageCommands.addBackgroundToMask(paramBufferedImage, this.isMask), false, false, str))
          this.errorTracker.addPageFailureMessage(new StringBuilder().append("Problem saving ").append(paramBufferedImage).toString());
      }
      if ((this.current.getType() == 4) || (this.current.getType() == 5) || (this.current.getType() == 7) || (this.current.getType() == 6))
      {
        if (paramBufferedImage != null)
        {
          this.gs.x = f1;
          this.gs.y = f2;
          this.current.drawImage(this.pageNum, paramBufferedImage, this.gs, false, paramString, this.optionsApplied, -1);
        }
      }
      else
      {
        if ((this.finalImagesExtracted) || (this.renderImages))
          localImageTransformerDouble.doubleScaleTransformScale();
        localImageTransformerDouble.completeImage();
        f1 = localImageTransformerDouble.getImageX();
        f2 = localImageTransformerDouble.getImageY();
        float f3 = localImageTransformerDouble.getImageW();
        float f4 = localImageTransformerDouble.getImageH();
        paramBufferedImage = localImageTransformerDouble.getImage();
        if (paramBufferedImage != null)
        {
          if ((this.renderImages) || (this.finalImagesExtracted) || (this.clippedImagesExtracted) || (this.rawImagesExtracted))
            this.pdfImages.setImageInfo(this.currentImage, this.pageNum, f1, f2, f3, f4);
          if (((this.renderImages) || (!this.isPageContent)) && (paramBufferedImage != null))
          {
            this.gs.x = f1;
            this.gs.y = f2;
            this.current.drawImage(this.pageNum, paramBufferedImage, this.gs, false, paramString, this.optionsApplied, -1);
          }
          if ((!this.renderDirectly) && (this.isPageContent) && (this.finalImagesExtracted) && (ImageCommands.isExtractionAllowed(this.currentPdfFile)))
          {
            str = this.objectStoreStreamRef.getImageType(this.currentImage);
            this.objectStoreStreamRef.saveStoredImage(this.currentImage, ImageCommands.addBackgroundToMask(paramBufferedImage, this.isMask), false, false, str);
          }
        }
      }
    }
    else if (LogWriter.isOutput())
    {
      LogWriter.writeLog("NO image written");
    }
  }

  public void generateTransformedImageSingle(BufferedImage paramBufferedImage, String paramString)
  {
    if (paramBufferedImage != null)
    {
      Area localArea = this.gs.getClippingShape();
      ImageTransformer localImageTransformer = new ImageTransformer(this.gs, paramBufferedImage, true);
      float f1 = localImageTransformer.getImageX();
      float f2 = localImageTransformer.getImageY();
      float f3 = localImageTransformer.getImageW();
      float f4 = localImageTransformer.getImageH();
      paramBufferedImage = localImageTransformer.getImage();
      if ((paramBufferedImage != null) && (this.customImageHandler != null) && (localArea != null) && (localArea.getBounds().getWidth() > 1.0D) && (localArea.getBounds().getHeight() > 1.0D) && (!this.customImageHandler.imageHasBeenScaled()))
      {
        boolean bool = localArea.contains(f1, f2, f3, f4);
        if (!bool)
        {
          localImageTransformer.clipImage(localArea);
          f1 = localImageTransformer.getImageX();
          f2 = localImageTransformer.getImageY();
          f3 = localImageTransformer.getImageW();
          f4 = localImageTransformer.getImageH();
        }
      }
      paramBufferedImage = localImageTransformer.getImage();
      if (paramBufferedImage != null)
      {
        if ((this.finalImagesExtracted) || (this.rawImagesExtracted))
          this.pdfImages.setImageInfo(this.currentImage, this.pageNum, f1, f2, f3, f4);
        if (((this.renderImages) || (!this.isPageContent)) && (paramBufferedImage != null))
        {
          this.gs.x = f1;
          this.gs.y = f2;
          this.current.drawImage(this.pageNum, paramBufferedImage, this.gs, false, paramString, this.optionsApplied, -1);
        }
        if ((this.isPageContent) && (this.finalImagesExtracted) && (ImageCommands.isExtractionAllowed(this.currentPdfFile)))
        {
          String str = this.objectStoreStreamRef.getImageType(this.currentImage);
          this.objectStoreStreamRef.saveStoredImage(this.currentImage, ImageCommands.addBackgroundToMask(paramBufferedImage, this.isMask), false, false, str);
        }
      }
    }
    else if (LogWriter.isOutput())
    {
      LogWriter.writeLog("NO image written");
    }
  }

  BufferedImage processImage(GenericColorSpace paramGenericColorSpace, byte[] paramArrayOfByte, String paramString, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean1, PdfObject paramPdfObject, boolean paramBoolean2, int paramInt4)
    throws PdfException
  {
    this.cache.put(1, paramGenericColorSpace.getID(), "x");
    int i = paramInt3;
    int j = 1;
    float[] arrayOfFloat1 = paramPdfObject.getFloatArray(859785322);
    if ((LogWriter.debug) && (arrayOfFloat1 != null))
    {
      localObject1 = "";
      for (float f1 : arrayOfFloat1)
        localObject1 = new StringBuilder().append((String)localObject1).append(' ').append(f1).toString();
    }
    Object localObject1 = paramPdfObject.getMixedArray(1011108731);
    boolean bool1 = false;
    ??? = 0;
    if ((localObject1 != null) && (((PdfArrayIterator)localObject1).hasMoreTokens()));
    while (((PdfArrayIterator)localObject1).hasMoreTokens())
    {
      int i2 = ((PdfArrayIterator)localObject1).getNextValueAsConstant(true);
      bool1 = i2 == 1180911742;
      ??? = i2 == 1399277700 ? 1 : 0;
      continue;
      localObject1 = null;
    }
    int i3 = 0;
    int i4 = 0;
    Object localObject2 = null;
    String str1 = "jpg";
    int i5 = paramGenericColorSpace.getID();
    int i6 = paramGenericColorSpace.getColorSpace().getNumComponents();
    int i7 = 0;
    int i8 = 0;
    byte[] arrayOfByte1 = new byte[4];
    if (paramBoolean1)
      ImageCommands.getMaskColor(arrayOfByte1, this.gs);
    byte[] arrayOfByte2 = paramGenericColorSpace.getIndexedMap();
    if ((this.renderPage) && (this.streamType != 1) && (!this.current.avoidDownSamplingImage()))
      if ((this.isPrinting) && (SamplingFactory.isPrintDownsampleEnabled) && (paramInt1 < 4000))
      {
        i7 = this.pageData.getCropBoxWidth(this.pageNum) * 4;
        i8 = this.pageData.getCropBoxHeight(this.pageNum) * 4;
      }
      else if ((SamplingFactory.downsampleLevel == SamplingFactory.high) || (this.getSamplingOnly))
      {
        float[][] arrayOfFloat = new float[3][3];
        for (int i9 = 0; i9 < 3; i9++)
          for (int i10 = 0; i10 < 3; i10++)
            if (this.gs.CTM[i9][i10] < 0.0F)
              arrayOfFloat[i9][i10] = (-this.gs.CTM[i9][i10]);
            else
              arrayOfFloat[i9][i10] = this.gs.CTM[i9][i10];
        if ((arrayOfFloat[0][0] == 0.0F) || (arrayOfFloat[0][0] < arrayOfFloat[0][1]))
          i7 = (int)arrayOfFloat[0][1];
        else
          i7 = (int)arrayOfFloat[0][0];
        if ((arrayOfFloat[1][1] == 0.0F) || (arrayOfFloat[1][1] < arrayOfFloat[1][0]))
          i8 = (int)arrayOfFloat[1][0];
        else
          i8 = (int)arrayOfFloat[1][1];
        if ((!this.getSamplingOnly) && ((paramInt1 < 500) || ((paramInt2 < 600) && ((paramInt1 < 1000) || (??? != 0)))))
        {
          i7 = 0;
          i8 = 0;
        }
      }
      else if (SamplingFactory.downsampleLevel == SamplingFactory.medium)
      {
        i7 = this.pageData.getCropBoxWidth(this.pageNum);
        i8 = this.pageData.getCropBoxHeight(this.pageNum);
      }
    if ((this.current.avoidDownSamplingImage()) || ((paramInt1 < 4000) && (paramInt2 < 4000) && ((this.current.getType() == 4) || (this.current.getType() == 5) || (this.current.getType() == 7) || (this.current.getType() == 6))))
    {
      i7 = -1;
      i8 = -1;
    }
    if (((bool1) || (??? != 0)) && (this.multiplyer > 1.0F))
    {
      i7 = (int)(i7 * this.multiplyer);
      i8 = (int)(i8 * this.multiplyer);
    }
    PdfObject localPdfObject1 = paramPdfObject.getDictionary(1888135062);
    PdfObject localPdfObject2 = paramPdfObject.getDictionary(489767739);
    PdfObject localPdfObject3 = paramPdfObject.getDictionary(489767774);
    if ((paramInt3 == 1) && ((localPdfObject3 != null) || (paramPdfObject.getObjectType() != 489767739)) && (paramGenericColorSpace.getID() == 1568372915) && (paramInt2 < 300))
    {
      i7 = 0;
      i8 = 0;
    }
    if (((localPdfObject2 != null) || (localPdfObject3 != null)) && (LogWriter.isOutput()))
      LogWriter.writeLog(new StringBuilder().append("newMask= ").append(localPdfObject2).append(" newSMask=").append(localPdfObject3).toString());
    boolean bool2 = false;
    int i11;
    int i14;
    if (arrayOfFloat1 != null)
    {
      bool2 = true;
      i11 = arrayOfFloat1.length;
      i14 = 0;
      while (i14 < i11)
      {
        if ((arrayOfFloat1[i14] != 1.0F) || (arrayOfFloat1[(i14 + 1)] != 0.0F))
        {
          bool2 = false;
          i14 = i11;
        }
        i14 += 2;
      }
    }
    int k;
    int m;
    int i19;
    if ((this.renderPage) && (localPdfObject2 == null) && (paramGenericColorSpace.getID() != 1247168582) && ((bool2) || (arrayOfFloat1 == null) || (arrayOfFloat1.length == 0)) && ((paramInt3 == 1) || (paramInt3 == 8)) && (i7 > 0) && (i8 > 0) && ((SamplingFactory.isPrintDownsampleEnabled) || (!this.isPrinting)))
    {
      k = paramInt1;
      m = paramInt2;
      if ((this.multiplyer <= 1.0F) && (!this.isPrinting))
      {
        i11 = 1000;
        if (paramGenericColorSpace.getID() == 1568372915)
          i11 = 4000;
        if (i7 > i11)
          i7 = i11;
        if (i8 > i11)
          i8 = i11;
      }
      i11 = i8 << 2;
      i14 = i7 << 2;
      while ((k > i14) && (m > i11))
      {
        j <<= 1;
        k >>= 1;
        m >>= 1;
      }
      i19 = paramInt1 / i7;
      if (i19 < 1)
        i19 = 1;
      int i24 = paramInt2 / i8;
      if (i24 < 1)
        i24 = 1;
      j = i19;
      if (j > i24)
        j = i24;
    }
    int i28;
    int i30;
    int i33;
    int i34;
    if (this.getSamplingOnly)
    {
      float f2 = paramInt1 / i7;
      float f4 = paramInt2 / i8;
      if (f2 < f4)
        this.samplingUsed = f2;
      else
        this.samplingUsed = f4;
      i19 = 0;
      if (localPdfObject3 != null)
      {
        byte[] arrayOfByte4 = this.currentPdfFile.readStream(localPdfObject3, true, true, false, false, false, localPdfObject3.getCacheName(this.currentPdfFile.getObjectReader()));
        if (arrayOfByte4 != null)
        {
          if (localPdfObject1 == null)
            localPdfObject1 = localPdfObject3.getDictionary(1888135062);
          i28 = localPdfObject3.getInt(959726687);
          i30 = localPdfObject3.getInt(959926393);
          i33 = (i28 / 2 > paramInt1) && (i30 / 2 > paramInt2) ? 1 : 0;
          i34 = (i33 != 0) && (localPdfObject1 != null) && (localPdfObject1.getInt(1010783618) != -1) && (localPdfObject1.getInt(1970893723) != 15) ? 1 : 0;
          if (i34 == 0)
            i19 = 1;
        }
      }
      if (i19 == 0)
        return null;
    }
    if ((j > 1) && (this.multiplyer > 1.0F))
      j = (int)(j / this.multiplyer);
    int i36;
    int i37;
    if (j > 1)
    {
      i4 = 1;
      k = paramInt1 / j;
      m = paramInt2 / j;
      int i12 = 0;
      if ((paramBoolean1) && (paramInt1 > 2000) && (paramInt2 > 2000) && (paramInt3 == 1) && (paramGenericColorSpace.getID() == 1785221209) && (this.gs.CTM[0][0] > 0.0F) && (this.gs.CTM[1][1] > 0.0F))
        i12 = 1;
      int i15;
      int i31;
      int i35;
      int i40;
      int i41;
      int i42;
      int i46;
      int i43;
      int i47;
      if ((paramInt3 == 1) && ((paramGenericColorSpace.getID() != 1785221209) || (arrayOfByte2 == null)))
      {
        if ((this.formLevel < 2) && ((i12 != 0) || ((!paramBoolean1) && (paramBoolean2) && (paramGenericColorSpace.getID() == 1568372915))))
        {
          i15 = paramArrayOfByte.length;
          arrayOfByte3 = new byte[i15];
          System.arraycopy(paramArrayOfByte, 0, arrayOfByte3, 0, i15);
          int i25 = (i12 == 0) && (!this.doNotRotate) && ((this.renderDirectly) || (this.useHiResImageForDisplay)) && (RenderUtils.isInverted(this.gs.CTM)) ? 1 : 0;
          i28 = (i12 == 0) && (!this.doNotRotate) && ((this.renderDirectly) || (this.useHiResImageForDisplay)) && (RenderUtils.isRotated(this.gs.CTM)) ? 1 : 0;
          if (this.renderDirectly)
          {
            i25 = 0;
            i28 = 0;
          }
          if (i28 != 0)
          {
            arrayOfByte3 = ImageOps.rotateImage(arrayOfByte3, paramInt1, paramInt2, paramInt3, 1, arrayOfByte2);
            i30 = paramInt2;
            paramInt2 = paramInt1;
            paramInt1 = i30;
            i30 = i7;
            i7 = i8;
            i8 = i30;
          }
          if (i25 != 0)
            arrayOfByte3 = ImageOps.invertImage(arrayOfByte3, paramInt1, paramInt2, paramInt3, 1, arrayOfByte2);
          if (bool2)
            for (i30 = 0; i30 < i15; i30++)
              arrayOfByte3[i30] = ((byte)(arrayOfByte3[i30] ^ 0xFF));
          if (((paramGenericColorSpace.getID() != 1785221209) || (paramInt3 != 1) || (arrayOfByte1 == null)) && (((paramInt1 < 4000) && (paramInt2 < 4000)) || ((paramGenericColorSpace.getID() == 1568372915) && (!(paramPdfObject instanceof MaskObject)))))
          {
            String str2 = new StringBuilder().append(this.pageNum).append(String.valueOf(this.imageCount)).toString();
            if (i12 != 0)
              this.current.getObjectStore().saveRawImageData(str2, arrayOfByte3, paramInt1, paramInt2, i7, i8, arrayOfByte1, paramGenericColorSpace.getID());
            else
              this.current.getObjectStore().saveRawImageData(str2, arrayOfByte3, paramInt1, paramInt2, i7, i8, null, paramGenericColorSpace.getID());
          }
          if (i28 != 0)
          {
            i31 = paramInt2;
            paramInt2 = paramInt1;
            paramInt1 = i31;
            i31 = i7;
            i7 = i8;
            i8 = i31;
          }
        }
        if (arrayOfByte2 != null)
          arrayOfByte2 = paramGenericColorSpace.convertIndexToRGB(arrayOfByte2);
        i15 = k * m;
        if (paramBoolean1)
        {
          i15 *= 4;
          arrayOfByte1[3] = -1;
        }
        else if (arrayOfByte2 != null)
        {
          i15 *= 3;
        }
        byte[] arrayOfByte3 = new byte[i15];
        int[] arrayOfInt2 = { 1, 2, 4, 8, 16, 32, 64, 128 };
        i28 = paramInt1 + 7 >> 3;
        for (i34 = 0; i34 < m; i34++)
          for (i35 = 0; i35 < k; i35++)
          {
            i36 = 0;
            i37 = 0;
            int i38 = j;
            i40 = j;
            i41 = paramInt1 - i35;
            i42 = paramInt2 - i34;
            if (i38 > i41)
              i38 = i41;
            if (i40 > i42)
              i40 = i42;
            for (int i44 = 0; i44 < i40; i44++)
              for (i46 = 0; i46 < i38; i46++)
              {
                i43 = (i44 + i34 * j) * i28 + (i35 * j + i46 >> 3);
                if (i43 < paramArrayOfByte.length)
                  i33 = paramArrayOfByte[i43];
                else
                  i33 = 0;
                if ((paramBoolean1) && (!bool2))
                  i33 = (byte)(i33 ^ 0xFF);
                i31 = i33 & arrayOfInt2[(7 - (i35 * j + i46 & 0x7))];
                if (i31 != 0)
                  i36++;
                i37++;
              }
            i44 = i35 + k * i34;
            if (i37 > 0)
            {
              if (paramBoolean1)
                for (i46 = 0; i46 < 4; i46++)
                  if (bool2)
                    arrayOfByte3[(i44 * 4 + i46)] = ((byte)(255 - (arrayOfByte1[i46] & 0xFF) * i36 / i37));
                  else
                    arrayOfByte3[(i44 * 4 + i46)] = ((byte)((arrayOfByte1[i46] & 0xFF) * i36 / i37));
              else if ((arrayOfByte2 != null) && (paramInt3 == 1))
                for (i47 = 0; i47 < 3; i47++)
                  if ((arrayOfByte2[0] == -1) && (arrayOfByte2[1] == -1) && (arrayOfByte2[2] == -1))
                  {
                    i46 = (arrayOfByte2[i47] & 0xFF) + (arrayOfByte2[(i47 + 3)] & 0xFF);
                    arrayOfByte3[(i44 * 3 + i47)] = ((byte)(255 - i46 * i36 / i37));
                  }
                  else
                  {
                    float f12 = i36 / i37;
                    if (f12 > 0.5D)
                      arrayOfByte3[(i44 * 3 + i47)] = arrayOfByte2[(i47 + 3)];
                    else
                      arrayOfByte3[(i44 * 3 + i47)] = arrayOfByte2[i47];
                  }
              else if (arrayOfByte2 != null)
                for (i46 = 0; i46 < 3; i46++)
                  arrayOfByte3[(i44 * 3 + i46)] = ((byte)((arrayOfByte2[i46] & 0xFF) * i36 / i37));
              else
                arrayOfByte3[i44] = ((byte)(255 * i36 / i37));
            }
            else if (paramBoolean1)
              for (i46 = 0; i46 < 3; i46++)
                arrayOfByte3[(i44 * 4 + i46)] = 0;
            else if (arrayOfByte2 != null)
              for (i46 = 0; i46 < 3; i46++)
                arrayOfByte3[(i44 * 3 + i46)] = 0;
            else
              arrayOfByte3[i44] = -1;
          }
        paramArrayOfByte = arrayOfByte3;
        if (arrayOfByte2 != null)
          i6 = 3;
        paramInt2 = m;
        paramInt1 = k;
        paramGenericColorSpace.setIndex(null, 0);
        if (paramGenericColorSpace.getID() == -2073385820)
        {
          paramGenericColorSpace = new DeviceRGBColorSpace();
          if ((paramInt3 == 1) && (arrayOfByte2 == null))
          {
            i6 = 1;
            i34 = paramArrayOfByte.length;
            for (i35 = 0; i35 < i34; i35++)
              paramArrayOfByte[i35] = ((byte)(paramArrayOfByte[i35] ^ 0xFF));
          }
        }
        paramInt3 = 8;
      }
      else if ((paramInt3 == 8) && ((localObject1 == null) || ((!bool1) && (??? == 0))))
      {
        i15 = (paramGenericColorSpace.getIndexedMap() != null) && ((paramGenericColorSpace.getID() == 1785221209) || (paramGenericColorSpace.getID() == 1008872003) || (paramGenericColorSpace.getID() == 1498837125) || (paramGenericColorSpace.getID() == 1247168582)) ? 1 : 0;
        int i20 = paramArrayOfByte.length;
        i37 = 1;
        try
        {
          if (i15 != 0)
          {
            i35 = 1;
            i6 = 3;
            i37 = 3;
            arrayOfByte2 = paramGenericColorSpace.convertIndexToRGB(arrayOfByte2);
            paramGenericColorSpace.setIndex(null, 0);
          }
          else
          {
            i35 = paramGenericColorSpace.getColorComponentCount();
          }
          if ((paramInt1 * paramInt2 == i20) || (paramGenericColorSpace.getID() == 1568372915))
            i35 = 1;
          byte[] arrayOfByte5;
          if (i15 != 0)
          {
            arrayOfByte5 = new byte[k * m * i37];
            i36 = paramInt1;
          }
          else
          {
            arrayOfByte5 = new byte[k * m * i35];
            i36 = paramInt1 * i35;
          }
          for (i28 = 0; i28 < m; i28++)
            for (int i26 = 0; i26 < k; i26++)
            {
              i40 = j;
              i41 = j;
              i42 = paramInt1 - i26;
              i43 = paramInt2 - i28;
              if (i40 > i42)
                i40 = i42;
              if (i41 > i43)
                i41 = i43;
              for (i34 = 0; i34 < i35; i34++)
              {
                i46 = 0;
                i47 = 0;
                int[] arrayOfInt3 = new int[i37];
                int i50;
                for (i33 = 0; i33 < i41; i33++)
                  for (i31 = 0; i31 < i40; i31++)
                  {
                    int i48 = (i33 + i28 * j) * i36 + (i26 * j * i35 + i31 * i35 + i34);
                    if (i48 < i20)
                    {
                      if (i15 == 0)
                        i46 += (paramArrayOfByte[i48] & 0xFF);
                      else
                        for (i50 = 0; i50 < i37; i50++)
                          arrayOfInt3[i50] += (arrayOfByte2[((paramArrayOfByte[i48] & 0xFF) * i37 + i50)] & 0xFF);
                      i47++;
                    }
                  }
                int i49;
                if (i15 != 0)
                {
                  i49 = i34 + i26 * i37 + k * i28 * i37;
                  for (i50 = 0; i50 < i37; i50++)
                    arrayOfByte5[(i49 + i50)] = ((byte)(arrayOfInt3[i50] / i47));
                }
                else if (i47 > 0)
                {
                  i49 = i34 + i26 * i35 + k * i28 * i35;
                  arrayOfByte5[i49] = ((byte)(i46 / i47));
                }
              }
            }
          paramArrayOfByte = arrayOfByte5;
          paramInt2 = m;
          paramInt1 = k;
        }
        catch (Exception localException3)
        {
          if (LogWriter.isOutput())
            LogWriter.writeLog(new StringBuilder().append("Exception: ").append(localException3.getMessage()).toString());
        }
      }
      else if ((bool1) || (??? != 0) || (arrayOfByte2 != null));
    }
    if ((arrayOfFloat1 != null) && (arrayOfFloat1.length != 0) && ((localObject1 == null) || ((??? == 0) && (!bool1))) && (arrayOfByte2 == null))
      ImageCommands.applyDecodeArray(paramArrayOfByte, paramInt3, arrayOfFloat1, i5);
    int i13;
    if (paramBoolean1)
    {
      float f3 = paramInt2 / paramInt1;
      float f5;
      float f6;
      float f7;
      float f8;
      if (((this.isPrinting) && (f3 < 0.1F) && (paramInt1 > 4000) && (paramInt2 > 1)) || ((f3 < 0.001F) && (paramInt1 > 4000) && (paramInt2 > 1)) || ((paramInt1 == 1) && (paramInt2 == 1)))
      {
        f5 = this.gs.CTM[2][0];
        f6 = this.gs.CTM[2][1];
        f7 = this.gs.CTM[1][1];
        if (f7 == 0.0F)
          f7 = this.gs.CTM[1][0];
        if (f7 < 0.0F)
        {
          f6 += f7;
          f7 = -f7;
        }
        f8 = this.gs.CTM[0][0];
        if (f8 == 0.0F)
          f8 = this.gs.CTM[0][1];
        if (f8 < 0.0F)
        {
          f5 += f8;
          f8 = -f8;
        }
        if ((this.gs.CTM[0][0] == 0.0F) && (this.gs.CTM[0][1] > 0.0F) && (this.gs.CTM[1][0] != 0.0F) && (this.gs.CTM[1][1] == 0.0F))
        {
          float f9 = f7;
          f7 = f8;
          f8 = f9;
        }
        if (f8 < 1.0F)
          f8 = 1.0F;
        if (f7 < 1.0F)
          f7 = 1.0F;
        int i32 = -1;
        if (f7 < 3.0F)
        {
          i32 = (int)f7;
          f7 = 1.0F;
        }
        else if (f8 < 3.0F)
        {
          i32 = (int)f8;
          f8 = 1.0F;
        }
        GeneralPath localGeneralPath1 = new GeneralPath(1);
        localGeneralPath1.moveTo(f5, f6);
        localGeneralPath1.lineTo(f5, f6 + f7);
        localGeneralPath1.lineTo(f5 + f8, f6 + f7);
        localGeneralPath1.lineTo(f5 + f8, f6);
        localGeneralPath1.closePath();
        if ((this.renderPage) && (localGeneralPath1 != null))
        {
          float f11 = this.gs.getLineWidth();
          if (i32 > 0)
            this.gs.setLineWidth(i32);
          this.gs.setNonstrokeColor(this.gs.nonstrokeColorSpace.getColor());
          this.gs.setFillType(2);
          this.current.drawShape(localGeneralPath1, this.gs, 70);
          if (i32 > 0)
            this.gs.setLineWidth(f11);
        }
        return null;
      }
      if ((paramInt2 == 2) && (paramInt3 == 1) && (ImageCommands.isRepeatingLine(paramArrayOfByte, paramInt2)))
      {
        f5 = this.gs.CTM[2][0];
        f6 = this.gs.CTM[2][1];
        f7 = this.gs.CTM[1][1];
        f8 = this.gs.CTM[0][0];
        if ((this.gs.CTM[0][0] == 0.0F) && (this.gs.CTM[0][1] > 0.0F) && (this.gs.CTM[1][0] != 0.0F) && (this.gs.CTM[1][1] == 0.0F))
        {
          float f10 = f7;
          f7 = f8;
          f8 = f10;
        }
        double d1 = f8 / (paramArrayOfByte.length / paramInt2);
        double d2 = d1 / 8.0D;
        for (i36 = 0; i36 < paramArrayOfByte.length / paramInt2; i36++)
        {
          i37 = paramArrayOfByte[i36] & 0xFF;
          i37 = (i37 ^ 0xFFFFFFFF) & 0xFF;
          int i39 = 8;
          double d3 = 0.0D;
          int i45 = 0;
          while ((i37 != 0) || (i45 != 0))
          {
            i39--;
            if ((i37 & 0x1) == 1)
            {
              if (i45 == 0)
              {
                d3 = (i39 + 0.5D) * d2 + i36 * d1;
                i45 = 1;
              }
            }
            else if (i45 != 0)
            {
              i45 = 0;
              double d4 = (i39 + 0.5D) * d2 + i36 * d1;
              GeneralPath localGeneralPath2 = new GeneralPath(1);
              localGeneralPath2.moveTo((float)(f5 + d4), f6);
              localGeneralPath2.lineTo((float)(f5 + d4), f6 + f7);
              localGeneralPath2.lineTo((float)(f5 + d3), f6 + f7);
              localGeneralPath2.lineTo((float)(f5 + d3), f6);
              localGeneralPath2.closePath();
              if ((this.renderPage) && (localGeneralPath2 != null))
              {
                this.gs.setNonstrokeColor(this.gs.nonstrokeColorSpace.getColor());
                this.gs.setFillType(2);
                this.current.drawShape(localGeneralPath2, this.gs, 70);
              }
            }
            i37 >>>= 1;
          }
        }
        return null;
      }
      if (i4 != 0)
      {
        DataBufferByte localDataBufferByte = new DataBufferByte(paramArrayOfByte, paramArrayOfByte.length);
        int[] arrayOfInt1 = { 0, 1, 2, 3 };
        localObject2 = new BufferedImage(paramInt1, paramInt2, 2);
        WritableRaster localWritableRaster1 = Raster.createInterleavedRaster(localDataBufferByte, paramInt1, paramInt2, paramInt1 * 4, 4, arrayOfInt1, null);
        ((BufferedImage)localObject2).setData(localWritableRaster1);
      }
      else
      {
        boolean bool3 = true;
        if (paramInt2 < 20)
          bool3 = true;
        else if (paramInt4 != 0)
          bool3 = this.current.hasObjectsBehind(this.gs.CTM);
        int i21 = 0;
        int i27 = 0;
        Object localObject4;
        if ((paramBoolean1) && (paramInt3 == 1) && (paramGenericColorSpace.getID() == 1785221209) && (arrayOfByte1[0] == 0) && (arrayOfByte1[1] == 0) && (arrayOfByte1[2] == 0))
        {
          i21 = 1;
          for (int i29 = 0; i29 < paramArrayOfByte.length; i29++)
            if (paramArrayOfByte[i29] != -1)
            {
              i21 = 0;
              i29 = paramArrayOfByte.length;
            }
          if ((this.isPrinting) && ((paramInt4 == 0) || (this.isType3Font) || (paramInt3 == 1)))
          {
            localObject4 = Raster.createPackedRaster(new DataBufferByte(paramArrayOfByte, paramArrayOfByte.length), paramInt1, paramInt2, 1, null);
            localObject2 = new BufferedImage(paramInt1, paramInt2, 12);
            ((BufferedImage)localObject2).setData((Raster)localObject4);
            i27 = 1;
          }
          else if (i21 != 0)
          {
            localObject2 = null;
            i3 = 1;
          }
          else
          {
            localObject4 = new byte[] { arrayOfByte1[0], arrayOfByte1[1], arrayOfByte1[2], -1, -1, -1 };
            localObject2 = ColorSpaceConvertor.convertIndexedToFlat(paramInt3, paramInt1, paramInt2, paramArrayOfByte, (byte[])localObject4, true, true);
          }
        }
        if (i21 == 0)
          if ((!this.isPrinting) && (arrayOfByte1[0] == 0) && (arrayOfByte1[1] == 0) && (arrayOfByte1[2] == 0) && (!bool3) && (!this.isType3Font) && (paramGenericColorSpace.getID() != 1785221209))
          {
            if (paramInt3 == 1)
            {
              localObject4 = Raster.createPackedRaster(new DataBufferByte(paramArrayOfByte, paramArrayOfByte.length), paramInt1, paramInt2, 1, null);
              localObject2 = new BufferedImage(paramInt1, paramInt2, 12);
              ((BufferedImage)localObject2).setData((Raster)localObject4);
            }
            else
            {
              localObject4 = new int[] { 0 };
              WritableRaster localWritableRaster2 = Raster.createInterleavedRaster(new DataBufferByte(paramArrayOfByte, paramArrayOfByte.length), paramInt1, paramInt2, paramInt1, 1, (int[])localObject4, null);
              localObject2 = new BufferedImage(paramInt1, paramInt2, 10);
              ((BufferedImage)localObject2).setData(localWritableRaster2);
            }
          }
          else if (i27 == 0)
            if ((paramInt3 == 8) && (i4 != 0))
            {
              localObject4 = new byte[] { arrayOfByte1[0], arrayOfByte1[1], arrayOfByte1[2], -1, -1, -1 };
              localObject2 = ColorSpaceConvertor.convertIndexedToFlat(paramInt3, paramInt1, paramInt2, paramArrayOfByte, (byte[])localObject4, true, true);
            }
            else if (((paramInt1 < 4000) && (paramInt2 < 4000)) || (bool3))
            {
              localObject4 = new byte[] { arrayOfByte1[0], arrayOfByte1[1], arrayOfByte1[2], -1, -1, -1 };
              localObject2 = ColorSpaceConvertor.convertIndexedToFlat(1, paramInt1, paramInt2, paramArrayOfByte, (byte[])localObject4, true, false);
            }
      }
    }
    else if (localObject1 == null)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog(new StringBuilder().append("Image ").append(paramString).append(' ').append(paramInt1).append("W * ").append(paramInt2).append("H with No Compression at BPC ").append(paramInt3).toString());
      localObject2 = makeImage(paramGenericColorSpace, paramInt1, paramInt2, paramInt3, paramArrayOfByte, i6, paramPdfObject);
    }
    else if (bool1)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog(new StringBuilder().append("JPeg Image ").append(paramString).append(' ').append(paramInt1).append("W * ").append(paramInt2).append('H').append(" arrayInverted=").append(bool2).toString());
      if ((i5 == 1498837125) && (this.extractRawCMYK))
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog(new StringBuilder().append("Raw CMYK image ").append(paramString).append(" saved.").toString());
        if (!this.objectStoreStreamRef.saveRawCMYKImage(paramArrayOfByte, paramString))
          this.errorTracker.addPageFailureMessage(new StringBuilder().append("Problem saving Raw CMYK image ").append(paramString).toString());
      }
      i13 = 0;
      if (paramGenericColorSpace.getID() == 1247168582)
      {
        int i16 = paramGenericColorSpace.getAlternateColorSpace();
        Object localObject3 = null;
        if (i16 == 1785221209)
          localObject3 = new DeviceRGBColorSpace();
        else if (i16 == 1498837125)
          localObject3 = new DeviceCMYKColorSpace();
        if (localObject3 != null)
          try
          {
            localObject2 = ((GenericColorSpace)localObject3).JPEGToRGBImage(paramArrayOfByte, paramInt1, paramInt2, arrayOfFloat1, i7, i8, bool2, paramPdfObject);
            if (localObject2 != null)
            {
              i13 = 1;
              paramGenericColorSpace = (GenericColorSpace)localObject3;
              if (paramGenericColorSpace.isImageYCCK())
                this.hasYCCKimages = true;
            }
          }
          catch (Exception localException2)
          {
            this.errorTracker.addPageFailureMessage(new StringBuilder().append("Unable to use alt colorspace with ").append(paramString).append(" to JPEG").toString());
            if (LogWriter.isOutput())
              LogWriter.writeLog(new StringBuilder().append("Exception: ").append(localException2.getMessage()).toString());
            ((BufferedImage)localObject2).flush();
            localObject2 = null;
          }
      }
      if (i13 == 0)
        try
        {
          localObject2 = paramGenericColorSpace.JPEGToRGBImage(paramArrayOfByte, paramInt1, paramInt2, arrayOfFloat1, i7, i8, bool2, paramPdfObject);
          if (paramGenericColorSpace.isImageYCCK())
            this.hasYCCKimages = true;
        }
        catch (Exception localException1)
        {
          this.errorTracker.addPageFailureMessage(new StringBuilder().append("Problem converting ").append(paramString).append(" to JPEG").toString());
          if (LogWriter.isOutput())
            LogWriter.writeLog(new StringBuilder().append("Exception: ").append(localException1.getMessage()).toString());
          ((BufferedImage)localObject2).flush();
          localObject2 = null;
        }
      str1 = "jpg";
      setRotationOptionsOnJPEGImage();
    }
    else if (??? != 0)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog(new StringBuilder().append("JPeg 2000 Image ").append(paramString).append(' ').append(paramInt1).append("W * ").append(paramInt2).append('H').toString());
      if (JAIHelper.isJAIused())
      {
        localObject2 = paramGenericColorSpace.JPEG2000ToRGBImage(paramArrayOfByte, paramInt1, paramInt2, arrayOfFloat1, i7, i8);
        str1 = "jpg";
      }
      else
      {
        if ((System.getProperty("org.jpedal.jai") != null) && (System.getProperty("org.jpedal.jai").toLowerCase().equals("true")))
        {
          if (!ImageCommands.JAImessageShow)
          {
            ImageCommands.JAImessageShow = true;
            System.err.println("JPeg 2000 Images need both JAI and imageio.jar on classpath");
          }
          throw new RuntimeException("JPeg 2000 Images need both JAI and imageio.jar on classpath");
        }
        System.err.println("JPeg 2000 Images needs the VM parameter -Dorg.jpedal.jai=true switch turned on");
        throw new RuntimeException("JPeg 2000 Images needs the VM parameter -Dorg.jpedal.jai=true switch turned on");
      }
      setRotationOptionsOnJPEGImage();
    }
    else
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog(new StringBuilder().append(paramString).append(' ').append(paramInt1).append("W * ").append(paramInt2).append("H BPC=").append(paramInt3).append(' ').append(paramGenericColorSpace).toString());
      localObject2 = makeImage(paramGenericColorSpace, paramInt1, paramInt2, paramInt3, paramArrayOfByte, i6, paramPdfObject);
      if ((paramInt3 == 8) || (this.gs.nonstrokeColorSpace.getID() == 1785221209) || (this.gs.nonstrokeColorSpace.getID() == 1247168582))
        str1 = "jpg";
    }
    if (localObject2 != null)
    {
      if ((localPdfObject3 != null) && (localPdfObject1 == null))
      {
        localPdfObject1 = localPdfObject3.getDictionary(1888135062);
        if ((paramGenericColorSpace.getID() == 1568372915) && (paramInt1 < localPdfObject3.getInt(959726687)) && (paramInt2 < localPdfObject3.getInt(959926393)))
        {
          i13 = paramArrayOfByte.length;
          int i17 = 1;
          for (int i22 = 0; i22 < i13; i22++)
            if (paramArrayOfByte[i22] != 0)
            {
              i17 = 0;
              i22 = i13;
            }
          if (i17 != 0)
            localObject2 = new BufferedImage(localPdfObject3.getInt(959726687), localPdfObject3.getInt(959926393), 10);
        }
      }
      if (localPdfObject3 != null)
        localObject2 = addSMaskObject(paramGenericColorSpace, paramArrayOfByte, paramString, paramInt1, paramInt2, paramPdfObject, bool1, ???, (BufferedImage)localObject2, localPdfObject1, localPdfObject3);
      else if (localPdfObject2 != null)
        localObject2 = ImageCommands.addMaskObject(paramGenericColorSpace, paramInt3, bool1, ???, (BufferedImage)localObject2, i5, arrayOfByte2, localPdfObject2, this.optionsApplied, this.currentPdfFile, paramArrayOfByte);
      if (localObject2 != null)
        localObject2 = ImageCommands.simulateOverprint(paramGenericColorSpace, paramArrayOfByte, bool1, ???, (BufferedImage)localObject2, i5, localPdfObject2, localPdfObject3, this.current, this.gs);
      if (localObject2 == null)
        return null;
      if ((this.current.getType() != 4) && (this.current.getType() != 5) && (this.current.getType() != 7) && (this.current.getType() != 6) && (!this.renderDirectly) && ((this.finalImagesExtracted) || (this.rawImagesExtracted)) && (!this.cache.testIfImageAlreadySaved(paramPdfObject)))
        saveImage(paramString, this.createScaledVersion, (BufferedImage)localObject2, str1);
    }
    if ((localObject2 == null) && (i3 == 0))
      this.imagesProcessedFully = false;
    PdfObject localPdfObject4 = this.gs.getTR();
    if (localPdfObject4 != null)
      localObject2 = ImageCommands.applyTR((BufferedImage)localObject2, localPdfObject4, this.currentPdfFile);
    if ((localPdfObject1 != null) && (localPdfObject1.getInt(1010122310) != -1) && (this.current.hasObjectsBehind(this.gs.CTM)) && (localObject2 != null) && (((BufferedImage)localObject2).getType() != 2) && (((BufferedImage)localObject2).getType() != 1) && ((!bool1) || (localPdfObject1.getInt(862279027) == 0)))
      localObject2 = ImageCommands.makeBlackandWhiteTransparent((BufferedImage)localObject2);
    if ((i7 > 0) && (i8 > 0) && (i == 1) && (ImageCommands.sharpenDownsampledImages) && ((paramGenericColorSpace.getID() == 1568372915) || (paramGenericColorSpace.getID() == 1785221209)))
    {
      Kernel localKernel = new Kernel(3, 3, new float[] { -1.0F, -1.0F, -1.0F, -1.0F, 9.0F, -1.0F, -1.0F, -1.0F, -1.0F });
      ConvolveOp localConvolveOp = new ConvolveOp(localKernel);
      localObject2 = localConvolveOp.filter((BufferedImage)localObject2, null);
    }
    this.imageCount += 1;
    if ((paramInt4 == 0) && (this.isPrinting) && (localObject2 != null) && (paramInt3 == 1) && (arrayOfByte1 != null) && (arrayOfByte1[0] == 0) && (arrayOfByte1[1] == 0) && (arrayOfByte1[2] == 0) && (arrayOfByte1[3] == 0))
    {
      int i18 = ((BufferedImage)localObject2).getWidth();
      int i23 = ((BufferedImage)localObject2).getHeight();
      BufferedImage localBufferedImage = new BufferedImage(i18, i23, 10);
      localBufferedImage.getGraphics().setColor(Color.WHITE);
      localBufferedImage.getGraphics().fillRect(0, 0, i18, i23);
      localBufferedImage.getGraphics().drawImage((Image)localObject2, 0, 0, null);
      localObject2 = localBufferedImage;
    }
    return localObject2;
  }

  private void setRotationOptionsOnJPEGImage()
  {
    if ((this.imageStatus > 0) && (this.gs.CTM[0][0] > 0.0F) && (this.gs.CTM[0][1] > 0.0F) && (this.gs.CTM[1][1] > 0.0F) && (this.gs.CTM[1][0] < 0.0F))
    {
      if (this.imageStatus == 1)
      {
        this.gs.CTM[0][1] = (-this.gs.CTM[0][1]);
        this.gs.CTM[1][1] = (-this.gs.CTM[1][1]);
        this.gs.CTM[2][1] -= this.gs.CTM[1][1];
      }
      else if (this.imageStatus == 2)
      {
        this.optionsApplied += 1;
      }
    }
    else if ((this.optionsApplied > 0) && (this.gs.CTM[0][0] < 0.0F) && (this.gs.CTM[1][1] < 0.0F) && (this.gs.CTM[0][1] == 0.0F) && (this.gs.CTM[1][0] == 0.0F))
    {
      this.gs.CTM[1][1] = (-this.gs.CTM[1][1]);
      this.gs.CTM[2][1] = (this.gs.CTM[2][1] - this.gs.CTM[1][1] + this.gs.CTM[1][0]);
      this.gs.CTM[2][0] -= this.gs.CTM[0][1];
    }
  }

  private void saveImage(String paramString1, boolean paramBoolean, BufferedImage paramBufferedImage, String paramString2)
  {
    if ((paramBufferedImage != null) && (paramBufferedImage.getSampleModel().getNumBands() == 1))
      paramString2 = "tif";
    if ((this.isPageContent) && ((this.renderImages) || (this.finalImagesExtracted) || (this.clippedImagesExtracted) || (this.rawImagesExtracted)))
      this.objectStoreStreamRef.saveStoredImage(paramString1, ImageCommands.addBackgroundToMask(paramBufferedImage, this.isMask), false, paramBoolean, paramString2);
  }

  private BufferedImage makeImage(GenericColorSpace paramGenericColorSpace, int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte, int paramInt4, PdfObject paramPdfObject)
  {
    if (paramGenericColorSpace.getID() == 1568372915)
    {
      int i;
      if (paramInt3 == 1)
      {
        i = (paramInt1 + 7 >> 3) * paramInt2;
        j = paramArrayOfByte.length;
        if (j < i)
        {
          localObject1 = paramArrayOfByte;
          paramArrayOfByte = new byte[i];
          System.arraycopy(localObject1, 0, paramArrayOfByte, 0, j);
          for (int k = j; k < i; k++)
            paramArrayOfByte[k] = -1;
        }
      }
      else if (paramInt3 == 8)
      {
        i = paramInt1 * paramInt2;
        j = paramArrayOfByte.length;
        if (j < i)
        {
          localObject1 = paramArrayOfByte;
          paramArrayOfByte = new byte[i];
          System.arraycopy(localObject1, 0, paramArrayOfByte, 0, j);
        }
      }
    }
    ColorSpace localColorSpace = paramGenericColorSpace.getColorSpace();
    int j = paramGenericColorSpace.getID();
    Object localObject1 = null;
    byte[] arrayOfByte1 = paramGenericColorSpace.getIndexedMap();
    this.optionsApplied = 0;
    int m = (!this.doNotRotate) && (this.useHiResImageForDisplay) && (RenderUtils.isInverted(this.gs.CTM)) ? 1 : 0;
    int n = (!this.doNotRotate) && (this.useHiResImageForDisplay) && (RenderUtils.isRotated(this.gs.CTM)) ? 1 : 0;
    if ((paramPdfObject.getGeneralType(608780341) == 489767739) && (this.streamType != 1))
    {
      m = 0;
      n = 0;
    }
    if (m != 0)
    {
      int i1 = paramInt4;
      if (j == -2073385820)
        i1 = 1;
      else if (j == 960981604)
        i1 = paramGenericColorSpace.getColorComponentCount();
      byte[] arrayOfByte3 = ImageOps.invertImage(paramArrayOfByte, paramInt1, paramInt2, paramInt3, i1, arrayOfByte1);
      if (arrayOfByte3 != null)
      {
        paramArrayOfByte = arrayOfByte3;
        this.optionsApplied += 1;
      }
    }
    int i5;
    if (n != 0)
    {
      byte[] arrayOfByte2 = ImageOps.rotateImage(paramArrayOfByte, paramInt1, paramInt2, paramInt3, paramInt4, arrayOfByte1);
      if (arrayOfByte2 != null)
      {
        paramArrayOfByte = arrayOfByte2;
        this.optionsApplied += 2;
        i5 = paramInt2;
        paramInt2 = paramInt1;
        paramInt1 = i5;
      }
    }
    int i7;
    int i9;
    if (arrayOfByte1 != null)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog(new StringBuilder().append("Indexed ").append(paramInt1).append(' ').append(paramInt2).toString());
      if (!paramGenericColorSpace.isIndexConverted())
        arrayOfByte1 = paramGenericColorSpace.convertIndexToRGB(arrayOfByte1);
      if ((paramInt3 == 8) && (paramGenericColorSpace.getIndexSize() == 0) && (paramGenericColorSpace.getID() == 1785221209))
      {
        int i2 = 0;
        i5 = arrayOfByte1.length;
        for (i7 = 0; i7 < i5; i7++)
          if (arrayOfByte1[i7] != 0)
          {
            i2 = 1;
            i7 = i5;
          }
        if (i2 == 0)
        {
          i7 = paramArrayOfByte.length;
          for (i9 = 0; i9 < i7; i9++)
            if (paramArrayOfByte[i9] != 0)
            {
              i2 = 1;
              i9 = i7;
            }
        }
        if (i2 == 0)
          return new BufferedImage(1, 1, 2);
      }
      try
      {
        if ((paramInt3 == 1) && (arrayOfByte1.length == 6) && (arrayOfByte1[0] == arrayOfByte1[3]) && (arrayOfByte1[1] == arrayOfByte1[4]) && (arrayOfByte1[2] == arrayOfByte1[5]))
        {
          localObject1 = null;
        }
        else if ((paramInt3 == 8) && (paramInt1 == 1) && (paramInt2 == 1) && (arrayOfByte1[0] == -1) && (arrayOfByte1[1] == -1) && (arrayOfByte1[2] == -1) && (allBytesZero(paramArrayOfByte)))
        {
          localObject1 = new BufferedImage(1, 1, 1);
          ((BufferedImage)localObject1).createGraphics().setPaint(Color.CYAN);
          Raster localRaster1 = ColorSpaceConvertor.createInterleavedRaster(new byte[] { -1, -1, -1 }, 1, 1);
          ((BufferedImage)localObject1).setData(localRaster1);
        }
        else
        {
          localObject1 = ColorSpaceConvertor.convertIndexedToFlat(paramInt3, paramInt1, paramInt2, paramArrayOfByte, arrayOfByte1, false, false);
        }
      }
      catch (Exception localException)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog(new StringBuilder().append("Exception: ").append(localException.getMessage()).toString());
      }
    }
    else
    {
      DataBufferByte localDataBufferByte1;
      Object localObject2;
      if (paramInt3 == 1)
      {
        localObject1 = new BufferedImage(paramInt1, paramInt2, 12);
        localDataBufferByte1 = new DataBufferByte(paramArrayOfByte, paramArrayOfByte.length);
        if (paramGenericColorSpace.getID() == -2073385820)
        {
          i5 = paramArrayOfByte.length;
          for (i7 = 0; i7 < i5; i7++)
            paramArrayOfByte[i7] = ((byte)(paramArrayOfByte[i7] ^ 0xFF));
        }
        localObject2 = Raster.createPackedRaster(localDataBufferByte1, paramInt1, paramInt2, paramInt3, null);
        ((BufferedImage)localObject1).setData((Raster)localObject2);
      }
      else if ((j == -2073385820) || (j == 960981604))
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog("Converting Separation/DeviceN colorspace to sRGB ");
        localObject1 = paramGenericColorSpace.dataToRGB(paramArrayOfByte, paramInt1, paramInt2);
      }
      else if (paramInt4 == 4)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog("Converting ICC/CMYK colorspace to sRGB ");
        localObject1 = ColorSpaceConvertor.convertFromICCCMYK(paramInt1, paramInt2, paramArrayOfByte);
      }
      else if (paramInt4 == 3)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog(new StringBuilder().append("Converting 3 comp colorspace to sRGB index=").append(arrayOfByte1).toString());
        if (paramInt1 * paramInt2 == paramArrayOfByte.length)
        {
          if ((paramInt3 == 8) && (arrayOfByte1 != null))
          {
            localObject1 = ColorSpaceConvertor.convertIndexedToFlat(paramInt3, paramInt1, paramInt2, paramArrayOfByte, arrayOfByte1, false, false);
          }
          else
          {
            localDataBufferByte1 = new DataBufferByte(paramArrayOfByte, paramArrayOfByte.length);
            localObject2 = new int[] { 0 };
            localObject1 = new BufferedImage(paramInt1, paramInt2, 10);
            WritableRaster localWritableRaster1 = Raster.createInterleavedRaster(localDataBufferByte1, paramInt1, paramInt2, paramInt1, 1, (int[])localObject2, null);
            ((BufferedImage)localObject1).setData(localWritableRaster1);
          }
        }
        else
        {
          if (LogWriter.isOutput())
            LogWriter.writeLog("Converting data to sRGB ");
          if (paramInt3 == 4)
          {
            int i3 = paramArrayOfByte.length;
            int i6 = paramInt1 * paramInt2 * 3;
            int i8 = (paramInt1 & 0x1) == 1 ? 1 : 0;
            i9 = paramInt1 * 3 + 1 >> 1;
            byte[] arrayOfByte4 = new byte[i6];
            int i11 = 0;
            int i12 = 0;
            for (int i13 = 0; i13 < i3; i13++)
            {
              int i10 = paramArrayOfByte[i13];
              i12++;
              arrayOfByte4[i11] = ((byte)(i10 & 0xF0));
              if (arrayOfByte4[i11] == -16)
                arrayOfByte4[i11] = -1;
              i11++;
              if ((i12 == i9) && (i8 != 0))
              {
                i12 = 0;
              }
              else
              {
                arrayOfByte4[i11] = ((byte)((i10 & 0xF) << 4));
                if (arrayOfByte4[i11] == -16)
                  arrayOfByte4[i11] = -1;
                i11++;
              }
              if (i11 == i6)
                i13 = i3;
            }
            paramArrayOfByte = arrayOfByte4;
          }
          localObject1 = new BufferedImage(paramInt1, paramInt2, 1);
          paramArrayOfByte = ImageOps.checkSize(paramArrayOfByte, paramInt1, paramInt2, 3);
          Raster localRaster2 = ColorSpaceConvertor.createInterleavedRaster(paramArrayOfByte, paramInt1, paramInt2);
          ((BufferedImage)localObject1).setData(localRaster2);
        }
      }
      else if (paramInt4 == 1)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog(new StringBuilder().append("comp=1 and d= ").append(paramInt3).toString());
        if (paramInt3 != 8)
        {
          int i4 = paramInt1 * paramInt2;
          localObject3 = new byte[i4];
          switch (paramInt3)
          {
          case 2:
            ColorSpaceConvertor.flatten2bpc(paramInt1, paramArrayOfByte, null, false, i4, (byte[])localObject3);
            break;
          case 4:
            ColorSpaceConvertor.flatten4bpc(paramInt1, paramArrayOfByte, i4, (byte[])localObject3);
            break;
          default:
            if (LogWriter.isOutput())
              LogWriter.writeLog(new StringBuilder().append("unknown comp= ").append(paramInt3).toString());
            break;
          }
          paramArrayOfByte = (byte[])localObject3;
        }
        DataBufferByte localDataBufferByte2 = new DataBufferByte(paramArrayOfByte, paramArrayOfByte.length);
        Object localObject3 = { 0 };
        localObject1 = new BufferedImage(paramInt1, paramInt2, 10);
        WritableRaster localWritableRaster2 = Raster.createInterleavedRaster(localDataBufferByte2, paramInt1, paramInt2, paramInt1, 1, (int[])localObject3, null);
        ((BufferedImage)localObject1).setData(localWritableRaster2);
      }
      else if (LogWriter.isOutput())
      {
        LogWriter.writeLog(new StringBuilder().append("Image ").append(localColorSpace.getType()).append(" not currently supported with components ").append(paramInt4).toString());
      }
    }
    return localObject1;
  }

  private static boolean allBytesZero(byte[] paramArrayOfByte)
  {
    boolean bool = true;
    for (int k : paramArrayOfByte)
      if (k != 0)
      {
        bool = false;
        break;
      }
    return bool;
  }

  private BufferedImage addSMaskObject(GenericColorSpace paramGenericColorSpace, byte[] paramArrayOfByte, String paramString, int paramInt1, int paramInt2, PdfObject paramPdfObject1, boolean paramBoolean1, boolean paramBoolean2, BufferedImage paramBufferedImage, PdfObject paramPdfObject2, PdfObject paramPdfObject3)
    throws PdfException
  {
    byte[] arrayOfByte = this.currentPdfFile.readStream(paramPdfObject3, true, true, false, false, false, paramPdfObject3.getCacheName(this.currentPdfFile.getObjectReader()));
    if (arrayOfByte != null)
    {
      int i = (paramPdfObject2 != null) && (paramPdfObject2.getInt(1010783618) != -1) && (paramPdfObject2.getInt(1970893723) != 15) && (paramGenericColorSpace.getID() != 1247168582) ? 1 : 0;
      PdfObject localPdfObject1 = paramPdfObject3.getDictionary(2087749783);
      if ((i != 0) && ((paramGenericColorSpace.getID() == 1785221209) || (paramGenericColorSpace.getID() == 1498837125)) && (localPdfObject1.getParameterConstant(2087749783) == 1568372915))
        i = 0;
      int j;
      int k;
      if ((paramBoolean1) && (localPdfObject1.getParameterConstant(2087749783) == 1568372915))
      {
        j = arrayOfByte.length;
        i = 1;
        for (k = 0; k < j; k++)
          if (arrayOfByte[k] != -1)
          {
            i = 0;
            k = j;
          }
      }
      if (i == 0)
      {
        j = this.optionsApplied;
        if (this.optionsApplied == 0)
          this.doNotRotate = true;
        k = paramPdfObject3.getInt(959726687);
        int m = paramPdfObject3.getInt(959926393);
        int n = 0;
        n = (paramInt1 < 600) && (k / 2 > paramInt1) && (m / 2 > paramInt2) ? 1 : 0;
        int i1 = 0;
        int i5;
        if (n != 0)
        {
          PdfObject localPdfObject2 = paramPdfObject1.getDictionary(2087749783);
          PdfArrayIterator localPdfArrayIterator = paramPdfObject3.getMixedArray(1011108731);
          int i4 = 0;
          if ((localPdfObject2.getParameterConstant(2087749783) == 1785221209) && (localPdfArrayIterator != null) && (localPdfArrayIterator.hasMoreTokens()))
            while (localPdfArrayIterator.hasMoreTokens())
            {
              i5 = localPdfArrayIterator.getNextValueAsConstant(true);
              i4 = i5 == 1247500931 ? 1 : 0;
            }
          i1 = (paramArrayOfByte.length == 2) && (localPdfObject2.getParameterConstant(2087749783) == 895578984) ? 1 : 0;
          n = (localPdfObject2 != null) && (((localPdfObject2.getParameterConstant(2087749783) == 1785221209) && ((!paramBoolean1) || (i4 != 0))) || ((i1 != 0) && (localPdfObject2.getDictionary(895578984).getParameterConstant(2087749783) == 1785221209) && (localPdfObject1.getParameterConstant(2087749783) == 1568372915))) ? 1 : 0;
        }
        int i2;
        int i3;
        if ((n != 0) && ((paramBoolean1) || (paramBoolean2) || (i1 != 0)))
        {
          i2 = arrayOfByte.length;
          for (i3 = 0; i3 < i2; i3++)
            arrayOfByte[i3] = ((byte)(-1 - arrayOfByte[i3]));
          paramBufferedImage = processImageXObject(paramPdfObject3, paramString, arrayOfByte, true, null);
        }
        else if ((paramBoolean2) && (localPdfObject1.getParameterConstant(2087749783) == 1568372915) && (paramPdfObject3.getInt(-1344207655) == 1))
        {
          paramBufferedImage = ImageCommands.handle1bitSMask(paramBufferedImage, arrayOfByte, k, m);
        }
        else
        {
          BufferedImage localBufferedImage1 = processImageXObject(paramPdfObject3, paramString, arrayOfByte, true, null);
          if ((this.pageNum > 0) && (this.pageData.getRotation(this.pageNum) == 0) && (j == 2))
          {
            i2 = localBufferedImage1.getWidth();
            i3 = localBufferedImage1.getHeight();
            BufferedImage localBufferedImage2 = new BufferedImage(i3, i2, localBufferedImage1.getType());
            for (i5 = 0; i5 < i2; i5++)
              for (int i6 = 0; i6 < i3; i6++)
                localBufferedImage2.setRGB(i3 - 1 - i6, i5, localBufferedImage1.getRGB(i5, i6));
            localBufferedImage1 = localBufferedImage2;
          }
          this.doNotRotate = false;
          this.optionsApplied = j;
          if (localBufferedImage1 != null)
          {
            paramBufferedImage = ImageCommands.applySmask(paramBufferedImage, localBufferedImage1, paramPdfObject3, false, paramGenericColorSpace.getID() == 1785221209, paramPdfObject3.getDictionary(2087749783), paramPdfObject1, this.gs);
            localBufferedImage1.flush();
          }
        }
      }
    }
    return paramBufferedImage;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.parser.ImageDecoder
 * JD-Core Version:    0.6.2
 */