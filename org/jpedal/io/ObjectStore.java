package org.jpedal.io;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.jpedal.examples.handlers.DefaultImageHelper;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.Strip;

public class ObjectStore
{
  private static final Map undeletedFiles = new HashMap();
  public static final boolean isMultiThreaded = false;
  private static final boolean debugCache = false;
  private static boolean checkedThisSession = false;
  private static final String separator = System.getProperty("file.separator");
  private String currentFilename = "";
  private String currentFilePath = "";
  public static String temp_dir = "";
  public static final String multiThreaded_root_dir = null;
  private static final String cmyk_dir = temp_dir + "cmyk" + separator;
  private String key = "jpedal" + Math.random() + '_';
  private final Map image_type = new HashMap();
  private final Map tempFileNames = new HashMap();
  public static final Integer IMAGE_WIDTH = Integer.valueOf(1);
  public static final Integer IMAGE_HEIGHT = Integer.valueOf(2);
  public static final Integer IMAGE_pX = Integer.valueOf(3);
  public static final Integer IMAGE_pY = Integer.valueOf(4);
  public static final Integer IMAGE_MASKCOL = Integer.valueOf(5);
  public static final Integer IMAGE_COLORSPACE = Integer.valueOf(6);
  public static final long time = 14400000L;
  public String fullFileName;
  private static final Map pagesOnDisk = new HashMap();
  private static final Map pagesOnDiskAsBytes = new HashMap();
  private final Map imagesOnDiskAsBytes = new HashMap();
  private final Map imagesOnDiskAsBytesW = new HashMap();
  private final Map imagesOnDiskAsBytesH = new HashMap();
  private final Map imagesOnDiskAsBytespX = new HashMap();
  private final Map imagesOnDiskAsBytespY = new HashMap();
  private final Map imagesOnDiskMask = new HashMap();
  private final Map imagesOnDiskColSpaceID = new HashMap();

  public ObjectStore()
  {
    init();
  }

  private static void init()
  {
    try
    {
      if (temp_dir.isEmpty())
        temp_dir = System.getProperty("java.io.tmpdir");
      if (temp_dir.isEmpty())
        temp_dir = temp_dir + separator + "jpedal" + separator;
      else if (!temp_dir.endsWith(separator))
        temp_dir += separator;
      File localFile = new File(temp_dir);
      if (!localFile.exists())
        localFile.mkdirs();
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Unable to create temp dir at " + temp_dir);
    }
  }

  public String getCurrentFilename()
  {
    return this.currentFilename;
  }

  public String getCurrentFilepath()
  {
    return this.currentFilePath;
  }

  public final void storeFileName(String paramString)
  {
    this.fullFileName = paramString;
    int i = this.fullFileName.lastIndexOf(47);
    int j = this.fullFileName.lastIndexOf(92);
    if (j > i)
      i = j;
    if (i > 0)
      this.currentFilePath = this.fullFileName.substring(0, i + 1);
    else
      this.currentFilePath = "";
    int k = paramString.indexOf(92);
    if (k == -1)
      k = paramString.indexOf(47);
    while (k != -1)
    {
      paramString = paramString.substring(k + 1);
      k = paramString.indexOf(92);
      if (k == -1)
        k = paramString.indexOf(47);
    }
    int m = paramString.lastIndexOf(46);
    if (m != -1)
      paramString = paramString.substring(0, m);
    paramString = Strip.stripAllSpaces(paramString);
    this.currentFilename = paramString.toLowerCase();
  }

  public boolean saveRawCMYKImage(byte[] paramArrayOfByte, String paramString)
  {
    boolean bool = true;
    paramString = removeIllegalFileNameCharacters(paramString);
    File localFile = new File(cmyk_dir);
    if (!localFile.exists())
      localFile.mkdirs();
    try
    {
      FileOutputStream localFileOutputStream = new FileOutputStream(cmyk_dir + paramString + ".jpg");
      this.tempFileNames.put(cmyk_dir + paramString + ".jpg", "#");
      localFileOutputStream.write(paramArrayOfByte);
      localFileOutputStream.flush();
      localFileOutputStream.close();
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Unable to save CMYK jpeg " + paramString);
      bool = false;
    }
    return bool;
  }

  public final synchronized boolean saveStoredImage(String paramString1, BufferedImage paramBufferedImage, boolean paramBoolean1, boolean paramBoolean2, String paramString2)
  {
    boolean bool = false;
    paramString1 = removeIllegalFileNameCharacters(paramString1);
    int i = paramBufferedImage.getType();
    File localFile = new File(temp_dir);
    if (!localFile.exists())
      localFile.mkdirs();
    if (paramString2.contains("tif"))
    {
      if (((i == 1) || (i == 2)) && (!paramString1.contains("HIRES_")))
        paramBufferedImage = ColorSpaceConvertor.convertColorspace(paramBufferedImage, 5);
      if (!paramBoolean1)
        this.image_type.put(paramString1, "tif");
      bool = saveStoredImage("TIFF", ".tif", ".tiff", paramString1, paramBufferedImage, paramBoolean1, paramBoolean2);
    }
    else if (paramString2.contains("jpg"))
    {
      if (!paramBoolean1)
        this.image_type.put(paramString1, "jpg");
      bool = saveStoredJPEGImage(paramString1, paramBufferedImage, paramBoolean1, paramBoolean2);
    }
    else if (paramString2.contains("png"))
    {
      if (!paramBoolean1)
        this.image_type.put(paramString1, "png");
      bool = saveStoredImage("PNG", ".png", ".png", paramString1, paramBufferedImage, paramBoolean1, paramBoolean2);
    }
    return bool;
  }

  public final String getImageType(String paramString)
  {
    return (String)this.image_type.get(paramString);
  }

  public final void init(String paramString)
  {
    this.key = (paramString + System.currentTimeMillis());
    File localFile = new File(temp_dir);
    if (!localFile.exists())
      localFile.mkdirs();
  }

  public final synchronized BufferedImage loadStoredImage(String paramString)
  {
    if (paramString == null)
      return null;
    paramString = removeIllegalFileNameCharacters(paramString);
    String str = (String)this.image_type.get(paramString);
    BufferedImage localBufferedImage = null;
    if (str == null)
      return null;
    if (str.equals("tif"))
      localBufferedImage = loadStoredImage(paramString, ".tif");
    else if (str.equals("jpg"))
      localBufferedImage = loadStoredJPEGImage(paramString);
    else if (str.equals("png"))
      localBufferedImage = loadStoredImage(paramString, ".png");
    return localBufferedImage;
  }

  public final synchronized void flush()
  {
    Iterator localIterator = this.imagesOnDiskAsBytes.keySet().iterator();
    Object localObject1;
    Object localObject2;
    while (localIterator.hasNext())
    {
      localObject1 = localIterator.next();
      if (localObject1 != null)
      {
        localObject2 = new File((String)this.imagesOnDiskAsBytes.get(localObject1));
        if (((File)localObject2).exists())
          ((File)localObject2).delete();
      }
    }
    this.imagesOnDiskAsBytes.clear();
    this.imagesOnDiskAsBytesW.clear();
    this.imagesOnDiskAsBytesH.clear();
    this.imagesOnDiskAsBytespX.clear();
    this.imagesOnDiskAsBytespY.clear();
    this.imagesOnDiskMask.clear();
    this.imagesOnDiskColSpaceID.clear();
    localIterator = this.tempFileNames.keySet().iterator();
    while (localIterator.hasNext())
    {
      localObject1 = (String)localIterator.next();
      if (((String)localObject1).contains(this.key))
      {
        localObject2 = new File((String)localObject1);
        if (((File)localObject2).delete())
          localIterator.remove();
        else
          undeletedFiles.put(this.key, "x");
      }
    }
    try
    {
      if ((!checkedThisSession) && (temp_dir.length() > 2))
      {
        checkedThisSession = true;
        localObject1 = new File(temp_dir);
        localObject2 = ((File)localObject1).list();
        File[] arrayOfFile = ((File)localObject1).listFiles();
        if (localObject2 != null)
          for (int i = 0; i < localObject2.length; i++)
          {
            if (localObject2[i].contains(this.key))
            {
              File localFile = new File(temp_dir + localObject2[i]);
              localFile.delete();
            }
            int j = 1;
            if ((j != 0) && (!localObject2[i].endsWith(".pdf")) && (System.currentTimeMillis() - arrayOfFile[i].lastModified() >= 14400000L))
              arrayOfFile[i].delete();
          }
      }
      localObject1 = new File(cmyk_dir);
      if (((File)localObject1).exists())
        ((File)localObject1).delete();
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception " + localException + " flushing files");
    }
  }

  public static void copyCMYKimages(String paramString)
  {
    File localFile1 = new File(cmyk_dir);
    if (localFile1.exists())
    {
      String[] arrayOfString = localFile1.list();
      Object localObject;
      if (arrayOfString.length > 0)
      {
        if (!paramString.endsWith(separator))
          paramString = paramString + separator;
        localObject = new File(paramString);
        if (!((File)localObject).exists())
          ((File)localObject).mkdirs();
      }
      for (String str : arrayOfString)
      {
        File localFile2 = new File(cmyk_dir + str);
        File localFile3 = new File(paramString + str);
        localFile2.renameTo(localFile3);
      }
    }
  }

  private synchronized boolean saveStoredJPEGImage(String paramString, BufferedImage paramBufferedImage, boolean paramBoolean1, boolean paramBoolean2)
  {
    boolean bool = false;
    String str1 = paramString;
    String str2 = "";
    if (!paramBoolean1)
    {
      str1 = temp_dir + this.key + paramString;
      str2 = temp_dir + this.key + 'R' + paramString;
      this.image_type.put('R' + paramString, this.image_type.get(paramString));
    }
    if (((!str1.toLowerCase().endsWith(".jpg") ? 1 : 0) & (!str1.toLowerCase().endsWith(".jpeg") ? 1 : 0)) != 0)
    {
      str1 = str1 + ".jpg";
      str2 = str2 + ".jpg";
    }
    try
    {
      DefaultImageHelper.write(paramBufferedImage, "jpg", str1);
      this.tempFileNames.put(str1, "#");
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception " + localException + " writing image " + paramBufferedImage + " as " + str1);
    }
    if (paramBoolean2)
    {
      saveCopy(str1, str2);
      this.tempFileNames.put(str2, "#");
    }
    return bool;
  }

  public String getFileForCachedImage(String paramString)
  {
    return temp_dir + this.key + paramString + '.' + this.image_type.get(paramString);
  }

  private synchronized BufferedImage loadStoredImage(String paramString1, String paramString2)
  {
    paramString1 = removeIllegalFileNameCharacters(paramString1);
    String str = temp_dir + this.key + paramString1 + paramString2;
    BufferedImage localBufferedImage = DefaultImageHelper.read(str);
    return localBufferedImage;
  }

  private static void saveCopy(String paramString1, String paramString2)
  {
    BufferedInputStream localBufferedInputStream = null;
    BufferedOutputStream localBufferedOutputStream = null;
    try
    {
      localBufferedInputStream = new BufferedInputStream(new FileInputStream(paramString1));
      localBufferedOutputStream = new BufferedOutputStream(new FileOutputStream(paramString2));
      byte[] arrayOfByte = new byte[65535];
      int i;
      while ((i = localBufferedInputStream.read(arrayOfByte)) != -1)
        localBufferedOutputStream.write(arrayOfByte, 0, i);
    }
    catch (Exception localException1)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception " + localException1 + " copying file");
    }
    try
    {
      localBufferedOutputStream.close();
      localBufferedInputStream.close();
    }
    catch (Exception localException2)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception " + localException2 + " closing files");
    }
  }

  public final void saveAsCopy(String paramString1, String paramString2)
  {
    BufferedInputStream localBufferedInputStream = null;
    BufferedOutputStream localBufferedOutputStream = null;
    paramString1 = removeIllegalFileNameCharacters(paramString1);
    String str = temp_dir + this.key + paramString1;
    try
    {
      localBufferedInputStream = new BufferedInputStream(new FileInputStream(str));
      localBufferedOutputStream = new BufferedOutputStream(new FileOutputStream(paramString2));
      byte[] arrayOfByte = new byte[65535];
      int i;
      while ((i = localBufferedInputStream.read(arrayOfByte)) != -1)
        localBufferedOutputStream.write(arrayOfByte, 0, i);
    }
    catch (Exception localException1)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception " + localException1 + " copying file");
    }
    try
    {
      localBufferedOutputStream.close();
      localBufferedInputStream.close();
    }
    catch (Exception localException2)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception " + localException2 + " closing files");
    }
  }

  public static void copy(String paramString1, String paramString2)
  {
    BufferedInputStream localBufferedInputStream = null;
    BufferedOutputStream localBufferedOutputStream = null;
    try
    {
      localBufferedInputStream = new BufferedInputStream(new FileInputStream(paramString1));
      localBufferedOutputStream = new BufferedOutputStream(new FileOutputStream(paramString2));
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception " + localException + " copying file");
    }
    copy(localBufferedInputStream, localBufferedOutputStream);
  }

  public static void copy(BufferedInputStream paramBufferedInputStream, BufferedOutputStream paramBufferedOutputStream)
  {
    try
    {
      byte[] arrayOfByte = new byte[65535];
      int i;
      while ((i = paramBufferedInputStream.read(arrayOfByte)) != -1)
        paramBufferedOutputStream.write(arrayOfByte, 0, i);
    }
    catch (Exception localException1)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception " + localException1 + " copying file");
    }
    try
    {
      paramBufferedOutputStream.close();
      paramBufferedInputStream.close();
    }
    catch (Exception localException2)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception " + localException2 + " closing files");
    }
  }

  private synchronized BufferedImage loadStoredJPEGImage(String paramString)
  {
    String str = temp_dir + this.key + paramString + ".jpg";
    BufferedImage localBufferedImage = null;
    File localFile = new File(str);
    if (localFile.exists())
      try
      {
        localBufferedImage = DefaultImageHelper.read(str);
      }
      catch (Exception localException)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog("Exception " + localException + " loading " + paramString);
      }
    else
      localBufferedImage = new BufferedImage(100, 100, 1);
    return localBufferedImage;
  }

  private synchronized boolean saveStoredImage(String paramString1, String paramString2, String paramString3, String paramString4, BufferedImage paramBufferedImage, boolean paramBoolean1, boolean paramBoolean2)
  {
    boolean bool = false;
    paramString4 = removeIllegalFileNameCharacters(paramString4);
    String str1 = paramString4;
    String str2 = "";
    if (!paramBoolean1)
    {
      str1 = temp_dir + this.key + paramString4;
      str2 = temp_dir + this.key + 'R' + paramString4;
      this.image_type.put('R' + paramString4, this.image_type.get(paramString4));
    }
    if (((!str1.toLowerCase().endsWith(paramString2) ? 1 : 0) & (!str1.toLowerCase().endsWith(paramString3) ? 1 : 0)) != 0)
    {
      str1 = str1 + paramString2;
      str2 = str2 + paramString2;
    }
    try
    {
      if ((!JAIHelper.isJAIused()) && (paramString1.equals("TIFF")))
        DefaultImageHelper.write(paramBufferedImage, "png", str1);
      else
        DefaultImageHelper.write(paramBufferedImage, paramString1, str1);
      File localFile = new File(str1);
      if (localFile.length() == 0L)
      {
        paramBufferedImage = ColorSpaceConvertor.convertToRGB(paramBufferedImage);
        if ((!JAIHelper.isJAIused()) && (paramString1.equals("TIFF")))
          DefaultImageHelper.write(paramBufferedImage, "png", str1);
        else
          DefaultImageHelper.write(paramBufferedImage, paramString1, str1);
      }
      this.tempFileNames.put(str1, "#");
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
      if (LogWriter.isOutput())
        LogWriter.writeLog(" Exception " + localException + " writing image " + paramBufferedImage + " with type " + paramBufferedImage.getType());
      bool = true;
    }
    catch (Error localError)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Error " + localError + " writing image " + paramBufferedImage + " with type " + paramBufferedImage.getType());
      bool = true;
    }
    if (paramBoolean2)
    {
      saveCopy(str1, str2);
      this.tempFileNames.put(str2, "#");
    }
    return bool;
  }

  public static synchronized void flushPages()
  {
    try
    {
      Iterator localIterator = pagesOnDisk.keySet().iterator();
      Object localObject;
      File localFile;
      while (localIterator.hasNext())
      {
        localObject = localIterator.next();
        if (localObject != null)
        {
          localFile = new File((String)pagesOnDisk.get(localObject));
          if (localFile.exists())
            localFile.delete();
        }
      }
      pagesOnDisk.clear();
      localIterator = pagesOnDiskAsBytes.keySet().iterator();
      while (localIterator.hasNext())
      {
        localObject = localIterator.next();
        if (localObject != null)
        {
          localFile = new File((String)pagesOnDiskAsBytes.get(localObject));
          if (localFile.exists())
            localFile.delete();
        }
      }
      pagesOnDiskAsBytes.clear();
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception " + localException + " flushing files");
    }
  }

  public void finalize()
  {
    try
    {
      super.finalize();
    }
    catch (Throwable localThrowable)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception: " + localThrowable.getMessage());
    }
    flush();
    Iterator localIterator = undeletedFiles.keySet().iterator();
    while (localIterator.hasNext())
    {
      Object localObject = localIterator.next();
      String str = (String)localObject;
      File localFile = new File(str);
      if (localFile.delete())
        undeletedFiles.remove(str);
    }
  }

  public static byte[] getCachedPageAsBytes(String paramString)
  {
    byte[] arrayOfByte = null;
    Object localObject = pagesOnDiskAsBytes.get(paramString);
    if (localObject != null)
      try
      {
        File localFile = new File((String)localObject);
        BufferedInputStream localBufferedInputStream = new BufferedInputStream(new FileInputStream(localFile));
        arrayOfByte = new byte[(int)localFile.length()];
        localBufferedInputStream.read(arrayOfByte);
        localBufferedInputStream.close();
      }
      catch (Exception localException)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog("Exception: " + localException.getMessage());
      }
    return arrayOfByte;
  }

  public static void cachePageAsBytes(String paramString, byte[] paramArrayOfByte)
  {
    try
    {
      if (pagesOnDiskAsBytes.containsKey(paramString))
      {
        localFile = new File((String)pagesOnDiskAsBytes.get(paramString));
        if (localFile.exists())
          localFile.delete();
      }
      File localFile = File.createTempFile("bytes", ".bin", new File(temp_dir));
      BufferedOutputStream localBufferedOutputStream = new BufferedOutputStream(new FileOutputStream(localFile));
      localBufferedOutputStream.write(paramArrayOfByte);
      localBufferedOutputStream.flush();
      localBufferedOutputStream.close();
      pagesOnDiskAsBytes.put(paramString, localFile.getAbsolutePath());
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception: " + localException.getMessage());
    }
  }

  public void saveRawImageData(String paramString, byte[] paramArrayOfByte1, int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte2, int paramInt5)
  {
    try
    {
      File localFile = File.createTempFile("image", ".bin", new File(temp_dir));
      BufferedOutputStream localBufferedOutputStream = new BufferedOutputStream(new FileOutputStream(localFile));
      localBufferedOutputStream.write(paramArrayOfByte1);
      localBufferedOutputStream.flush();
      localBufferedOutputStream.close();
      Integer localInteger = new Integer(paramString);
      this.imagesOnDiskAsBytes.put(localInteger, localFile.getAbsolutePath());
      this.imagesOnDiskAsBytesW.put(localInteger, Integer.valueOf(paramInt1));
      this.imagesOnDiskAsBytesH.put(localInteger, Integer.valueOf(paramInt2));
      this.imagesOnDiskAsBytespX.put(localInteger, Integer.valueOf(paramInt3));
      this.imagesOnDiskAsBytespY.put(localInteger, Integer.valueOf(paramInt4));
      this.imagesOnDiskMask.put(localInteger, paramArrayOfByte2);
      this.imagesOnDiskColSpaceID.put(localInteger, Integer.valueOf(paramInt5));
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception: " + localException.getMessage());
    }
  }

  public boolean isRawImageDataSaved(String paramString)
  {
    return this.imagesOnDiskAsBytes.get(new Integer(paramString)) != null;
  }

  public byte[] getRawImageData(String paramString)
  {
    byte[] arrayOfByte = null;
    Object localObject = this.imagesOnDiskAsBytes.get(new Integer(paramString));
    if (localObject != null)
      try
      {
        File localFile = new File((String)localObject);
        BufferedInputStream localBufferedInputStream = new BufferedInputStream(new FileInputStream(localFile));
        arrayOfByte = new byte[(int)localFile.length()];
        localBufferedInputStream.read(arrayOfByte);
        localBufferedInputStream.close();
      }
      catch (Exception localException)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog("Exception: " + localException.getMessage());
      }
    return arrayOfByte;
  }

  public Object getRawImageDataParameter(String paramString, Integer paramInteger)
  {
    if (paramInteger.equals(IMAGE_WIDTH))
      return this.imagesOnDiskAsBytesW.get(new Integer(paramString));
    if (paramInteger.equals(IMAGE_HEIGHT))
      return this.imagesOnDiskAsBytesH.get(new Integer(paramString));
    if (paramInteger.equals(IMAGE_pX))
      return this.imagesOnDiskAsBytespX.get(new Integer(paramString));
    if (paramInteger.equals(IMAGE_pY))
      return this.imagesOnDiskAsBytespY.get(new Integer(paramString));
    if (paramInteger.equals(IMAGE_MASKCOL))
      return this.imagesOnDiskMask.get(new Integer(paramString));
    if (paramInteger.equals(IMAGE_COLORSPACE))
      return this.imagesOnDiskColSpaceID.get(new Integer(paramString));
    return null;
  }

  public static File createTempFile(String paramString)
    throws IOException
  {
    for (String str1 = paramString.substring(0, paramString.lastIndexOf(46)); str1.length() < 3; str1 = str1 + 'a');
    String str2 = paramString.substring(paramString.lastIndexOf(46));
    if (str2.length() < 3)
      str2 = "pdf";
    File localFile = File.createTempFile(str1, str2, new File(temp_dir));
    return localFile;
  }

  public static String removeIllegalFileNameCharacters(String paramString)
  {
    return paramString;
  }

  public void setFileToDeleteOnFlush(String paramString)
  {
    this.tempFileNames.put(paramString, "#");
  }

  public String getKey()
  {
    return this.key;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.io.ObjectStore
 * JD-Core Version:    0.6.2
 */