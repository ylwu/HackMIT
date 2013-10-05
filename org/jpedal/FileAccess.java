package org.jpedal;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.stream.ImageInputStream;
import org.jpedal.display.PageOffsets;
import org.jpedal.exception.PdfException;
import org.jpedal.external.ExternalHandlers;
import org.jpedal.io.DecryptionFactory;
import org.jpedal.io.ObjectStore;
import org.jpedal.io.PdfFileReader;
import org.jpedal.io.PdfObjectReader;
import org.jpedal.linear.LinearParser;
import org.jpedal.linear.LinearThread;
import org.jpedal.objects.Javascript;
import org.jpedal.objects.PdfPageData;
import org.jpedal.objects.PdfResources;
import org.jpedal.objects.acroforms.AcroRenderer;
import org.jpedal.objects.raw.PageObject;
import org.jpedal.objects.raw.PdfObject;
import org.jpedal.parser.DecoderOptions;
import org.jpedal.parser.PdfStreamDecoder;
import org.jpedal.render.DynamicVectorRenderer;
import org.jpedal.render.SwingDisplay;
import org.jpedal.utils.LogWriter;

public class FileAccess
{
  Certificate certificate;
  private PageOffsets currentOffset;
  private DynamicVectorRenderer currentDisplay;
  private int lastPageDecoded = -1;
  PrivateKey key;
  private PdfPageData pageData = new PdfPageData();
  private ObjectStore objectStoreRef = new ObjectStore();
  public boolean isOpen = false;
  int minimumCacheSize = -1;
  private static final Calendar cal = Calendar.getInstance();
  static int bb = 30 - bb;
  private boolean closeOnExit = true;
  private String filename;
  private PdfObjectReader currentPdfFile;
  private int pageCount = 0;
  private int pageNumber = 1;
  private boolean isDecoding = false;
  LinearParser linearParser = new LinearParser();
  ExternalHandlers externalHandlers;
  private static final String flag = "11706076047632756";
  private PdfResources res;
  private DecoderOptions options;
  boolean isFX = false;

  public FileAccess(ExternalHandlers paramExternalHandlers, PdfResources paramPdfResources, DecoderOptions paramDecoderOptions)
  {
    this.externalHandlers = paramExternalHandlers;
    this.res = paramPdfResources;
    this.options = paramDecoderOptions;
  }

  public boolean isFileViewable(PdfObjectReader paramPdfObjectReader)
  {
    if (paramPdfObjectReader != null)
    {
      PdfFileReader localPdfFileReader = paramPdfObjectReader.getObjectReader();
      DecryptionFactory localDecryptionFactory = localPdfFileReader.getDecryptionObject();
      return (localDecryptionFactory == null) || (localDecryptionFactory.getBooleanValue(100)) || (this.certificate != null);
    }
    return false;
  }

  void openPdfArray(byte[] paramArrayOfByte)
    throws PdfException
  {
    if (paramArrayOfByte == null)
      throw new RuntimeException("Attempting to open null byte stream");
    if (this.isOpen)
      closePdfFile();
    this.isOpen = false;
    this.res.flush();
    this.res.flushObjects();
    try
    {
      this.currentPdfFile = new PdfObjectReader();
      this.currentPdfFile.openPdfFile(paramArrayOfByte);
      openPdfFile();
      this.objectStoreRef.storeFileName("r" + System.currentTimeMillis());
    }
    catch (Exception localException)
    {
      throw new PdfException("[PDF] OpenPdfArray generated exception " + localException.getMessage());
    }
  }

  public final boolean PDFContainsEmbeddedFonts()
    throws Exception
  {
    boolean bool = false;
    PdfStreamDecoder localPdfStreamDecoder = new PdfStreamDecoder(this.currentPdfFile);
    for (int i = 1; i < this.pageCount + 1; i++)
    {
      String str = this.currentPdfFile.getReferenceforPage(i);
      if (str != null)
      {
        PageObject localPageObject = new PageObject(str);
        localPageObject.ignoreStream(true);
        this.currentPdfFile.readObject(localPageObject);
        byte[][] arrayOfByte = localPageObject.getKeyArray(1216184967);
        if (arrayOfByte != null)
        {
          this.res.setupResources(localPdfStreamDecoder, false, localPageObject.getDictionary(2004251818), this.pageNumber, this.currentPdfFile);
          bool = localPdfStreamDecoder.getBooleanValue(-1);
          if (bool)
            i = this.pageCount;
        }
      }
    }
    return bool;
  }

  DynamicVectorRenderer getDynamicRenderer()
  {
    return this.currentDisplay;
  }

  public PdfResources getRes()
  {
    return this.res;
  }

  DynamicVectorRenderer getDynamicRenderer(boolean paramBoolean)
  {
    DynamicVectorRenderer localDynamicVectorRenderer = this.currentDisplay;
    if (paramBoolean)
      this.currentDisplay = new SwingDisplay(0, this.objectStoreRef, false);
    return localDynamicVectorRenderer;
  }

  public final void openPdfFileFromStream(Object paramObject, String paramString)
    throws PdfException
  {
    this.closeOnExit = false;
    if ((paramObject instanceof ImageInputStream))
    {
      ImageInputStream localImageInputStream = (ImageInputStream)paramObject;
      if (this.isOpen)
        closePdfFile();
      this.isOpen = false;
      this.filename = ("ImageInputStream" + System.currentTimeMillis());
      this.res.flush();
      this.res.flushObjects();
      this.objectStoreRef.storeFileName(this.filename);
      this.currentPdfFile = new PdfObjectReader(paramString);
      this.currentPdfFile.openPdfFile(localImageInputStream);
      openPdfFile();
    }
    else
    {
      throw new RuntimeException(paramObject + " not currently an option");
    }
  }

  void openPdfFile(String paramString1, String paramString2)
    throws PdfException
  {
    if (this.isOpen)
      closePdfFile();
    this.isOpen = false;
    this.filename = paramString1;
    this.res.flush();
    this.res.flushObjects();
    this.objectStoreRef.storeFileName(paramString1);
    this.currentPdfFile = new PdfObjectReader(paramString2);
    this.currentPdfFile.openPdfFile(paramString1);
    openPdfFile();
  }

  int getPageNumber()
  {
    return this.pageNumber;
  }

  public void setPageNumber(int paramInt)
  {
    this.pageNumber = paramInt;
  }

  int getPageCount()
  {
    return this.pageCount;
  }

  void setPageCount(int paramInt)
  {
    this.pageCount = paramInt;
  }

  boolean isDecoding()
  {
    return this.isDecoding;
  }

  void setDecoding(boolean paramBoolean)
  {
    this.isDecoding = paramBoolean;
  }

  public boolean isPasswordSupplied(PdfObjectReader paramPdfObjectReader)
  {
    if (paramPdfObjectReader != null)
    {
      PdfFileReader localPdfFileReader = paramPdfObjectReader.getObjectReader();
      DecryptionFactory localDecryptionFactory = localPdfFileReader.getDecryptionObject();
      return (localDecryptionFactory != null) && ((localDecryptionFactory.getBooleanValue(104)) || (this.certificate != null));
    }
    return false;
  }

  public ObjectStore getObjectStore()
  {
    return this.objectStoreRef;
  }

  void setObjectStore(ObjectStore paramObjectStore)
  {
    this.objectStoreRef = paramObjectStore;
  }

  public void setUserEncryption(Certificate paramCertificate, PrivateKey paramPrivateKey)
  {
    this.certificate = paramCertificate;
    this.key = paramPrivateKey;
  }

  public PdfObjectReader getNewReader()
  {
    PdfObjectReader localPdfObjectReader;
    if (this.certificate != null)
      localPdfObjectReader = new PdfObjectReader(this.certificate, this.key);
    else
      localPdfObjectReader = new PdfObjectReader();
    return localPdfObjectReader;
  }

  public boolean isOpen()
  {
    return this.isOpen;
  }

  public PdfObjectReader getIO()
  {
    return this.currentPdfFile;
  }

  public void setIO(PdfObjectReader paramPdfObjectReader)
  {
    this.currentPdfFile = paramPdfObjectReader;
  }

  public final boolean isEncrypted()
  {
    if (this.currentPdfFile != null)
    {
      PdfFileReader localPdfFileReader = this.currentPdfFile.getObjectReader();
      DecryptionFactory localDecryptionFactory = localPdfFileReader.getDecryptionObject();
      return (localDecryptionFactory != null) && (localDecryptionFactory.getBooleanValue(101));
    }
    return false;
  }

  public void dispose()
  {
    if (this.currentPdfFile != null)
      this.currentPdfFile.dispose();
    this.currentPdfFile = null;
    if (this.currentDisplay != null)
      this.currentDisplay.dispose();
    this.currentDisplay = null;
  }

  public String getFilename()
  {
    return this.filename;
  }

  public void setFilename(String paramString)
  {
    this.filename = paramString;
  }

  public int readAllPageReferences(boolean paramBoolean, PdfObject paramPdfObject, Map paramMap1, Map paramMap2, int paramInt1, AcroRenderer paramAcroRenderer, PdfResources paramPdfResources, int paramInt2, int paramInt3)
  {
    String str1 = paramPdfObject.getObjectRefAsString();
    int i = 0;
    int j = paramPdfObject.getParameterConstant(608780341);
    if (j == -1)
      j = 825701731;
    int k = paramPdfObject.getInt(1144088180);
    String str2 = paramPdfObject.getStringKey(1110793845);
    if (k == -1)
    {
      while ((str2 != null) && (k == -1))
      {
        if (str2 != null)
        {
          localObject = paramMap1.get(str2);
          if (localObject != null)
            k = ((Integer)localObject).intValue();
        }
        if (k == -1)
          str2 = (String)paramMap2.get(str2);
      }
      if (k != -1)
      {
        paramMap1.put(str1, Integer.valueOf(k));
        paramMap2.put(str1, str2);
      }
    }
    else
    {
      paramMap1.put(str1, Integer.valueOf(k));
      paramMap2.put(str1, str2);
    }
    if (k != -1)
      i = k;
    Object localObject = this.pageData;
    ((PdfPageData)localObject).setPageRotation(i, paramInt1);
    float[] arrayOfFloat1 = paramPdfObject.getFloatArray(1313305473);
    float[] arrayOfFloat2 = paramPdfObject.getFloatArray(1076199815);
    if (arrayOfFloat1 != null)
      ((PdfPageData)localObject).setMediaBox(arrayOfFloat1);
    if (arrayOfFloat2 != null)
      ((PdfPageData)localObject).setCropBox(arrayOfFloat2);
    byte[][] arrayOfByte;
    if (j == 825701731)
    {
      paramPdfResources.setPdfObject(2, paramPdfObject.getDictionary(2004251818));
      arrayOfByte = paramPdfObject.getKeyArray(456733763);
      int m = 0;
      if (arrayOfByte != null)
        m = arrayOfByte.length;
      if (m > 0)
        for (int n = 0; n < m; n++)
        {
          PageObject localPageObject = new PageObject(new String(arrayOfByte[n]));
          localPageObject.ignoreRecursion(paramBoolean);
          localPageObject.ignoreStream(true);
          this.currentPdfFile.readObject(localPageObject);
          paramInt1 = readAllPageReferences(paramBoolean, localPageObject, paramMap1, paramMap2, paramInt1, paramAcroRenderer, paramPdfResources, paramInt2, paramInt3);
        }
    }
    else if (j == 540096309)
    {
      this.currentPdfFile.setLookup(str1, paramInt1);
      ((PdfPageData)localObject).checkSizeSet(paramInt1);
      if (paramAcroRenderer != null)
      {
        arrayOfByte = paramPdfObject.getKeyArray(1044338049);
        if ((arrayOfByte != null) && (arrayOfByte.length == 1) && (arrayOfByte[0] == null))
          arrayOfByte = (byte[][])null;
        if (arrayOfByte != null)
          paramAcroRenderer.resetAnnotData(paramInt2, paramInt3, this.pageData, paramInt1, this.currentPdfFile, arrayOfByte);
      }
      paramInt1++;
    }
    return paramInt1;
  }

  PdfPageData getPdfPageData()
  {
    return this.pageData;
  }

  void setPageData(PdfPageData paramPdfPageData)
  {
    this.pageData = paramPdfPageData;
  }

  public void waitForDecodingToFinish()
  {
    while (this.isDecoding)
      try
      {
        Thread.sleep(100L);
      }
      catch (InterruptedException localInterruptedException)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog("Exception: " + localInterruptedException.getMessage());
        this.isDecoding = false;
      }
  }

  boolean readFile(boolean paramBoolean, InputStream paramInputStream, String paramString1, String paramString2)
    throws PdfException
  {
    this.objectStoreRef.setFileToDeleteOnFlush(ObjectStore.temp_dir + paramString1);
    this.objectStoreRef.setFileToDeleteOnFlush(paramString1);
    this.res.flush();
    this.res.flushObjects();
    if (paramString2 == null)
      this.currentPdfFile = new PdfObjectReader();
    else
      this.currentPdfFile = new PdfObjectReader(paramString2);
    if (paramInputStream != null)
      try
      {
        File localFile;
        if (paramString1.startsWith("inputstream"))
        {
          localFile = new File(ObjectStore.temp_dir + paramString1);
          this.filename = localFile.getAbsolutePath();
        }
        else
        {
          localFile = ObjectStore.createTempFile(paramString1);
        }
        this.objectStoreRef.storeFileName(localFile.getName().substring(0, localFile.getName().lastIndexOf(46)));
        if (paramBoolean)
        {
          byte[] arrayOfByte = this.linearParser.readLinearData(this.currentPdfFile, localFile, paramInputStream, this);
          if (arrayOfByte != null)
          {
            this.currentPdfFile.openPdfFile(arrayOfByte);
            openPdfFile();
            if (this.pageCount < 2)
              this.options.setDisplayView(1);
            else
              this.options.setDisplayView(this.options.getPageMode());
            this.linearParser.linearizedBackgroundReaderer.start();
            return true;
          }
        }
        else
        {
          this.currentPdfFile.openPdfFile(paramInputStream);
          openPdfFile();
          if (this.pageCount < 2)
            this.options.setDisplayView(1);
          else
            this.options.setDisplayView(this.options.getPageMode());
        }
        if (paramBoolean)
        {
          openPdfFile(localFile.getAbsolutePath());
          this.objectStoreRef.storeFileName(localFile.getName().substring(0, localFile.getName().lastIndexOf(46)));
        }
      }
      catch (IOException localIOException)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog("[PDF] Exception " + localIOException + " opening URL ");
        localIOException.printStackTrace();
      }
    return false;
  }

  public void closePdfFile()
  {
    waitForDecodingToFinish();
    if (bb < 1)
    {
      System.out.println("JPedal Trial has now expired");
      LogWriter.writeLog("JPedal Trial has now expired");
      System.exit(1);
    }
    else if (bb < 15)
    {
      System.out.println("Trial expires in " + bb + " days");
      LogWriter.writeLog("Trial expires in " + bb + " days");
    }
    if (!this.isOpen)
      return;
    this.isOpen = false;
    this.linearParser.closePdfFile();
    Javascript localJavascript = this.externalHandlers.getJavaScript();
    if (localJavascript != null)
      localJavascript.closeFile();
    AcroRenderer localAcroRenderer = this.externalHandlers.getFormRenderer();
    if (localAcroRenderer != null)
    {
      localAcroRenderer.openFile(this.pageCount, 0, 0, this.pageData, this.currentPdfFile, null);
      localAcroRenderer.removeDisplayComponentsFromScreen();
    }
    this.objectStoreRef.flush();
    ObjectStore.flushPages();
    this.pageCount = 0;
    if ((this.currentPdfFile != null) && (this.closeOnExit))
    {
      this.currentPdfFile.closePdfFile();
      this.currentPdfFile = null;
    }
    this.lastPageDecoded = -1;
    this.currentDisplay.flush();
  }

  public void setStreamCacheSize(int paramInt)
  {
    this.minimumCacheSize = paramInt;
  }

  int getLastPageDecoded()
  {
    return this.lastPageDecoded;
  }

  void setLastPageDecoded(int paramInt)
  {
    this.lastPageDecoded = paramInt;
  }

  public void setDVR(DynamicVectorRenderer paramDynamicVectorRenderer)
  {
    this.currentDisplay = paramDynamicVectorRenderer;
  }

  public PageOffsets getOffset()
  {
    return this.currentOffset;
  }

  public void setOffset(PageOffsets paramPageOffsets)
  {
    this.currentOffset = paramPageOffsets;
  }

  public void openPdfFile()
    throws PdfException
  {
    this.currentPdfFile.setJavaScriptObject(this.externalHandlers.getJavaScript());
    if (bb < 1)
      throw new RuntimeException("JPedal Trial has now expired");
    this.pageNumber = 1;
    this.isDecoding = true;
    AcroRenderer localAcroRenderer = this.externalHandlers.getFormRenderer();
    try
    {
      this.currentPdfFile.getObjectReader().setCacheSize(this.minimumCacheSize);
      this.pageData = new PdfPageData();
      PdfPageData localPdfPageData = this.pageData;
      String str = this.currentPdfFile.getObjectReader().getType();
      if (LogWriter.isOutput())
        LogWriter.writeLog("Pdf version : " + str);
      if (str == null)
      {
        this.currentPdfFile = null;
        throw new PdfException("No version on first line ");
      }
      int i = -1;
      Object localObject1;
      if (this.linearParser.hasLinearData())
      {
        localObject1 = this.linearParser.readHintTable(this.currentPdfFile);
        i = this.linearParser.getPageCount();
      }
      else
      {
        localObject1 = this.currentPdfFile.getObjectReader().readReferenceTable(null);
      }
      if ((!isEncrypted()) || (isPasswordSupplied(this.currentPdfFile)))
      {
        if (localObject1 != null)
        {
          ((PdfObject)localObject1).ignoreRecursion(true);
          this.res.setValues((PdfObject)localObject1, this.currentPdfFile);
          PdfObject localPdfObject1 = ((PdfObject)localObject1).getDictionary(826094945);
          if (localPdfObject1 != null)
            this.currentPdfFile.readNames(localPdfObject1, this.externalHandlers.getJavaScript(), false);
        }
        int j = ((PdfObject)localObject1).getParameterConstant(608780341);
        if (j != 540096309)
        {
          PdfObject localPdfObject2 = ((PdfObject)localObject1).getDictionary(825701731);
          if (localPdfObject2 != null)
          {
            localObject1 = new PageObject(localPdfObject2.getObjectRefAsString());
            this.currentPdfFile.readObject((PdfObject)localObject1);
            if (((PdfObject)localObject1).getParameterConstant(608780341) == -1)
              localObject1 = localPdfObject2;
          }
        }
        if (localObject1 != null)
        {
          if (LogWriter.isOutput())
            LogWriter.writeLog("Pages being read from " + localObject1 + ' ' + ((PdfObject)localObject1).getObjectRefAsString());
          this.pageNumber = 1;
          if (localAcroRenderer != null)
            localAcroRenderer.resetAnnotData(this.options.getInsetW(), this.options.getInsetW(), localPdfPageData, 1, this.currentPdfFile, (byte[][])null);
          int k = readAllPageReferences(true, (PdfObject)localObject1, new HashMap(1000), new HashMap(1000), 1, localAcroRenderer, this.res, this.options.getInsetW(), this.options.getInsetH());
          if (i > 0)
            this.pageCount = i;
          else
            this.pageCount = (k - 1);
          this.pageNumber = 0;
          if ((this.pageCount == 0) && (LogWriter.isOutput()))
            LogWriter.writeLog("No pages found");
        }
        if (localAcroRenderer != null)
          localAcroRenderer.openFile(this.pageCount, this.options.getInsetW(), this.options.getInsetH(), localPdfPageData, this.currentPdfFile, this.res.getPdfObject(1));
      }
      this.currentOffset = null;
      this.isOpen = true;
    }
    catch (PdfException localPdfException)
    {
      this.isDecoding = false;
      this.isOpen = true;
      closePdfFile();
      throw new PdfException(localPdfException.getMessage() + " opening file");
    }
    finally
    {
      this.isDecoding = false;
    }
  }

  public void openPdfFile(String paramString)
    throws PdfException
  {
    this.isOpen = false;
    this.filename = paramString;
    this.res.flush();
    this.res.flushObjects();
    this.objectStoreRef.storeFileName(paramString);
    this.currentPdfFile = getNewReader();
    this.currentPdfFile.openPdfFile(paramString);
    openPdfFile();
    if (this.pageCount < 2)
      this.options.setDisplayView(1);
    else
      this.options.setDisplayView(this.options.getPageMode());
  }

  static
  {
    int i = 1;
    int j = Integer.parseInt("11706076047632756".substring(3, 5));
    int k = Integer.parseInt("11706076047632756".substring(0, i + i));
    int m = Integer.parseInt("11706076047632756".substring(2, 3));
    int n = Integer.parseInt("11706076047632756".substring(5, 6));
    int i1 = Integer.parseInt("11706076047632756".substring(6, 7 * i));
    int i2 = Integer.parseInt("11706076047632756".substring(7, 8));
    int i3 = Integer.parseInt("11706076047632756".substring(8, 10));
    int i4 = Integer.parseInt("11706076047632756".substring(10, 11));
    int i5 = Integer.parseInt("11706076047632756".substring(11, 15));
    Calendar localCalendar = Calendar.getInstance();
    int i6 = i3 - i - i2;
    if (i6 < 0)
      i6 = 12 + i6;
    localCalendar.set(2, i6);
    localCalendar.set(1, 1000 * (i + i) + j + i1);
    int i7 = k - m;
    if (i7 < 1)
      i7 = 31 + i7;
    localCalendar.set(5, i7);
    long l = 86400000L;
    bb = (int)((Calendar.getInstance().getTime().getTime() - localCalendar.getTime().getTime()) / l);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.FileAccess
 * JD-Core Version:    0.6.2
 */