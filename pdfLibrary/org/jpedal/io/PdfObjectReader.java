package org.jpedal.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.stream.ImageInputStream;
import org.jpedal.exception.PdfException;
import org.jpedal.objects.Javascript;
import org.jpedal.objects.PageLookup;
import org.jpedal.objects.raw.FDFObject;
import org.jpedal.objects.raw.FormObject;
import org.jpedal.objects.raw.PageObject;
import org.jpedal.objects.raw.PdfObject;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.StringUtils;

public class PdfObjectReader
{
  private PdfFileReader objectReader = new PdfFileReader();
  Map pagesReferences = new HashMap();
  private PageLookup pageLookup = new PageLookup();
  private long eof = 0L;
  private String tempFileName = null;
  private NameLookup nameLookup = null;
  RandomAccessBuffer pdf_datafile = null;
  private Javascript javascript;

  public PdfObjectReader()
  {
  }

  public PdfObjectReader(String paramString)
  {
    if (paramString == null)
      paramString = "";
    this.objectReader.setPassword(paramString);
  }

  public PdfObjectReader(Certificate paramCertificate, PrivateKey paramPrivateKey)
  {
    this.objectReader.setCertificate(paramCertificate, paramPrivateKey);
  }

  public String getReferenceforPage(int paramInt)
  {
    return (String)this.pagesReferences.get(Integer.valueOf(paramInt));
  }

  public final void closePdfFile()
  {
    try
    {
      this.objectReader.closeFile();
      if (this.pdf_datafile != null)
        this.pdf_datafile.close();
      if (this.tempFileName != null)
      {
        File localFile = new File(this.tempFileName);
        localFile.delete();
        this.tempFileName = null;
      }
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception " + localException + " closing file");
    }
  }

  public PdfObject getPDFObject(int paramInt)
  {
    if (paramInt == 1113489015)
      return this.objectReader.encyptionObj;
    throw new RuntimeException("Access to " + paramInt + " not supported");
  }

  public PdfFileReader getObjectReader()
  {
    return this.objectReader;
  }

  public String convertNameToRef(String paramString)
  {
    if (this.nameLookup == null)
      return null;
    return (String)this.nameLookup.get(paramString);
  }

  public final PdfObject readFDF()
    throws PdfException
  {
    FDFObject localFDFObject;
    try
    {
      byte[] arrayOfByte = this.objectReader.readFDFData();
      localFDFObject = new FDFObject("1 0 R");
      for (int i = 0; (i < this.eof) && ((arrayOfByte[i] != 47) || (arrayOfByte[(i + 1)] != 70) || (arrayOfByte[(i + 2)] != 68) || (arrayOfByte[(i + 3)] != 70)); i++);
      i += 4;
      while ((i < this.eof) && ((arrayOfByte[i] != 60) || (arrayOfByte[(i + 1)] != 60)))
        i++;
      i += 2;
      ObjectDecoder localObjectDecoder = new ObjectDecoder(this.objectReader);
      localObjectDecoder.readDictionaryAsObject(localFDFObject, i, arrayOfByte);
    }
    catch (Exception localException)
    {
      try
      {
        this.objectReader.closeFile();
      }
      catch (IOException localIOException)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog("Exception " + localException + " closing file");
      }
      throw new PdfException("Exception " + localException + " reading trailer");
    }
    return localFDFObject;
  }

  public void readNames(PdfObject paramPdfObject, Javascript paramJavascript, boolean paramBoolean)
  {
    this.nameLookup = new NameLookup(this.objectReader);
    this.nameLookup.readNames(paramPdfObject, paramJavascript, paramBoolean);
  }

  public int convertObjectToPageNumber(String paramString)
  {
    return this.pageLookup.convertObjectToPageNumber(paramString);
  }

  public void setLookup(String paramString, int paramInt)
  {
    this.pageLookup.put(paramString, paramInt);
    this.pagesReferences.put(Integer.valueOf(paramInt), paramString);
  }

  public void dispose()
  {
    this.nameLookup = null;
    if (this.objectReader != null)
      this.objectReader.dispose();
    this.objectReader = null;
    if (this.pageLookup != null)
      this.pageLookup.dispose();
    this.pageLookup = null;
  }

  public final void openPdfFile(InputStream paramInputStream)
    throws PdfException
  {
    try
    {
      this.pdf_datafile = new RandomAccessMemoryMapBuffer(paramInputStream);
      this.objectReader.init(this.pdf_datafile);
      this.eof = this.pdf_datafile.length();
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception " + localException + " accessing file");
      throw new PdfException("Exception " + localException + " accessing file");
    }
  }

  public final void openPdfFile(ImageInputStream paramImageInputStream)
    throws PdfException
  {
    try
    {
      ImageInputStreamFileBuffer localImageInputStreamFileBuffer = new ImageInputStreamFileBuffer(paramImageInputStream);
      this.objectReader.init(localImageInputStreamFileBuffer);
      this.eof = localImageInputStreamFileBuffer.length();
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception " + localException + " accessing file");
      throw new PdfException("Exception " + localException + " accessing file");
    }
  }

  public void checkParentForResources(PdfObject paramPdfObject)
  {
    if (paramPdfObject.getDictionary(2004251818) == null)
    {
      String str = paramPdfObject.getStringKey(1110793845);
      if (str != null)
      {
        PageObject localPageObject = new PageObject(str);
        readObject(localPageObject);
        PdfObject localPdfObject = localPageObject.getDictionary(2004251818);
        if (localPdfObject != null)
          paramPdfObject.setDictionary(2004251818, localPdfObject);
      }
    }
  }

  public final void openPdfFile(String paramString)
    throws PdfException
  {
    try
    {
      RandomAccessFileBuffer localRandomAccessFileBuffer = new RandomAccessFileBuffer(paramString, "r");
      this.objectReader.init(localRandomAccessFileBuffer);
      this.eof = localRandomAccessFileBuffer.length();
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception " + localException + " accessing file");
      throw new PdfException("Exception " + localException + " accessing file");
    }
  }

  public final void openPdfFile(byte[] paramArrayOfByte)
    throws PdfException
  {
    try
    {
      Object localObject;
      if ((PdfFileReader.alwaysCacheInMemory == -1) || (paramArrayOfByte.length < PdfFileReader.alwaysCacheInMemory))
        localObject = new RandomAccessDataBuffer(paramArrayOfByte);
      else
        try
        {
          File localFile = File.createTempFile("page", ".bin", new File(ObjectStore.temp_dir));
          this.tempFileName = localFile.getAbsolutePath();
          FileOutputStream localFileOutputStream = new FileOutputStream(localFile);
          localFileOutputStream.write(paramArrayOfByte);
          localFileOutputStream.flush();
          localFileOutputStream.close();
          localObject = new RandomAccessFileBuffer(this.tempFileName, "r");
        }
        catch (Exception localException1)
        {
          throw new RuntimeException("Unable to create temporary file in " + ObjectStore.temp_dir);
        }
      this.objectReader.init((RandomAccessBuffer)localObject);
      this.eof = ((RandomAccessBuffer)localObject).length();
    }
    catch (Exception localException2)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception " + localException2 + " accessing file");
      throw new PdfException("Exception " + localException2 + " accessing file");
    }
  }

  public void setJavaScriptObject(Javascript paramJavascript)
  {
    this.javascript = paramJavascript;
  }

  public void checkResolved(PdfObject paramPdfObject)
  {
    ObjectDecoder localObjectDecoder = new ObjectDecoder(this.objectReader);
    localObjectDecoder.checkResolved(paramPdfObject);
  }

  public void setJavascriptForObject(FormObject paramFormObject, int paramInt1, int paramInt2)
  {
    PdfObject localPdfObject3 = paramFormObject.getDictionary(paramInt1);
    ObjectDecoder localObjectDecoder = new ObjectDecoder(this.objectReader);
    localObjectDecoder.checkResolved(localPdfObject3);
    if (localPdfObject3 == null)
      return;
    PdfObject localPdfObject1;
    if (paramInt2 == paramInt1)
      localPdfObject1 = localPdfObject3;
    else if (paramInt2 == 4866)
      localPdfObject1 = localPdfObject3.getDictionary(19);
    else
      localPdfObject1 = localPdfObject3.getDictionary(paramInt2);
    if (localPdfObject1 != null)
    {
      localObjectDecoder.checkResolved(localPdfObject1);
      PdfObject localPdfObject2 = localPdfObject1.getDictionary(6691);
      Object localObject;
      String str;
      if (localPdfObject2 != null)
      {
        localObject = localPdfObject2.getDecodedStream();
        str = StringUtils.getTextString((byte[])localObject, true);
      }
      else
      {
        str = localPdfObject1.getTextStreamValue(6691);
      }
      if (str != null)
      {
        localObject = paramFormObject.getTextStreamValue(36);
        if (localObject == null)
          localObject = paramFormObject.getObjectRefAsString();
        this.javascript.storeJavascript((String)localObject, str, paramInt2);
      }
    }
  }

  public byte[] readStream(PdfObject paramPdfObject, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, boolean paramBoolean5, String paramString)
  {
    return this.objectReader.readStream(paramPdfObject, paramBoolean1, paramBoolean2, paramBoolean3, paramBoolean4, paramBoolean5, paramString);
  }

  public void readObject(PdfObject paramPdfObject)
  {
    this.objectReader.readObject(paramPdfObject);
  }

  public EncryptionUsed getEncryptionType()
  {
    PdfFileReader localPdfFileReader = this.objectReader;
    DecryptionFactory localDecryptionFactory = localPdfFileReader.getDecryptionObject();
    if (localDecryptionFactory == null)
      return EncryptionUsed.NO_ENCRYPTION;
    if (localDecryptionFactory.hasPassword())
      return EncryptionUsed.PASSWORD;
    return EncryptionUsed.CERTIFICATE;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.io.PdfObjectReader
 * JD-Core Version:    0.6.2
 */