package org.jpedal.linear;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;
import org.jpedal.FileAccess;
import org.jpedal.exception.PdfException;
import org.jpedal.io.LinearizedHintTable;
import org.jpedal.io.ObjectDecoder;
import org.jpedal.io.PdfFileReader;
import org.jpedal.io.PdfObjectReader;
import org.jpedal.objects.raw.LinearizedObject;
import org.jpedal.objects.raw.PageObject;
import org.jpedal.objects.raw.PdfObject;
import org.jpedal.parser.PdfStreamDecoder;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.NumberUtils;

public class LinearParser
{
  public boolean isLinearizationTested = false;
  private PageObject linObject = null;
  private Map linObjects = new HashMap();
  private int linearPageCount = -1;
  private PdfObject linearObj = null;
  private LinearizedHintTable linHintTable = null;
  private int E = -1;
  public LinearThread linearizedBackgroundReaderer = null;

  public void closePdfFile()
  {
    this.E = -1;
    this.linearObj = null;
    this.isLinearizationTested = false;
    this.linObjects.clear();
    if ((this.linearizedBackgroundReaderer != null) && (this.linearizedBackgroundReaderer.isAlive()))
      this.linearizedBackgroundReaderer.interrupt();
    while ((this.linearizedBackgroundReaderer != null) && (this.linearizedBackgroundReaderer.isAlive()) && (!this.linearizedBackgroundReaderer.isInterrupted()))
      try
      {
        Thread.sleep(500L);
      }
      catch (Exception localException)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog("Exception: " + localException.getMessage());
      }
    this.linHintTable = null;
  }

  private void testForLinearlized(byte[] paramArrayOfByte, PdfObjectReader paramPdfObjectReader)
  {
    int i = 0;
    int j = 0;
    int k = 0;
    this.isLinearizationTested = true;
    int m = paramArrayOfByte.length;
    for (int n = 0; n < paramArrayOfByte.length; n++)
      if ((i == 0) && (n + 2 < m) && (paramArrayOfByte[n] == 111) && (paramArrayOfByte[(n + 1)] == 98) && (paramArrayOfByte[(n + 2)] == 106))
        i = n + 3;
      else if ((j == 0) && (n + 5 < m) && (paramArrayOfByte[n] == 101) && (paramArrayOfByte[(n + 1)] == 110) && (paramArrayOfByte[(n + 2)] == 100) && (paramArrayOfByte[(n + 3)] == 111) && (paramArrayOfByte[(n + 4)] == 98) && (paramArrayOfByte[(n + 5)] == 106))
        j = n + 7;
      else if ((k == 0) && (n + 6 < m) && (paramArrayOfByte[n] == 47) && (paramArrayOfByte[(n + 1)] == 76) && (paramArrayOfByte[(n + 2)] == 105) && (paramArrayOfByte[(n + 3)] == 110) && (paramArrayOfByte[(n + 4)] == 101) && (paramArrayOfByte[(n + 5)] == 97) && (paramArrayOfByte[(n + 6)] == 114))
        k = 1;
    if (k != 0)
    {
      n = j - i;
      byte[] arrayOfByte = new byte[n + 1];
      System.arraycopy(paramArrayOfByte, i, arrayOfByte, 0, n);
      this.linearObj = new LinearizedObject("1 0 R");
      this.linearObj.setStatus(2);
      this.linearObj.setUnresolvedData(arrayOfByte, 2004845231);
      paramPdfObjectReader.checkResolved(this.linearObj);
    }
    else
    {
      this.linearObj = null;
    }
  }

  public boolean isPageAvailable(int paramInt, PdfObjectReader paramPdfObjectReader)
  {
    boolean bool = true;
    try
    {
      if ((this.linearizedBackgroundReaderer != null) && (this.linearizedBackgroundReaderer.isAlive()) && (paramInt > 1) && (this.linHintTable != null))
      {
        Integer localInteger = Integer.valueOf(paramInt);
        if (this.linObjects.containsKey(localInteger))
        {
          this.linObject = ((PageObject)this.linObjects.get(localInteger));
          return true;
        }
        int i = this.linHintTable.getPageObjectRef(paramInt);
        byte[] arrayOfByte1 = this.linHintTable.getObjData(i);
        if (arrayOfByte1 != null)
        {
          this.linObject = new PageObject(i + " 0 R");
          this.linObject.setStatus(2);
          this.linObject.setUnresolvedData(arrayOfByte1, 540096309);
          this.linObject.isDataExternal(true);
          ObjectDecoder localObjectDecoder = new ObjectDecoder(paramPdfObjectReader.getObjectReader());
          if (!localObjectDecoder.resolveFully(this.linObject))
          {
            bool = false;
          }
          else
          {
            if (this.linObject != null)
            {
              byte[] arrayOfByte2 = paramPdfObjectReader.getObjectReader().readPageIntoStream(this.linObject);
              if (arrayOfByte2 == null)
              {
                bool = false;
              }
              else
              {
                PdfObject localPdfObject = this.linObject.getDictionary(2004251818);
                if (localPdfObject == null)
                {
                  this.linObject = null;
                  bool = false;
                }
                else if (!localObjectDecoder.resolveFully(localPdfObject))
                {
                  this.linObject = null;
                  bool = false;
                }
                else
                {
                  localPdfObject.isDataExternal(true);
                  new PdfStreamDecoder(paramPdfObjectReader).readResources(localPdfObject, true);
                  if (!localPdfObject.isFullyResolved())
                  {
                    this.linObject = null;
                    bool = false;
                  }
                }
              }
            }
            if ((bool) && (this.linObject != null))
              this.linObjects.put(localInteger, this.linObject);
          }
        }
        else
        {
          bool = false;
        }
      }
      else
      {
        this.linObject = null;
      }
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception: " + localException.getMessage());
      bool = false;
    }
    return bool;
  }

  public byte[] readLinearData(PdfObjectReader paramPdfObjectReader, File paramFile, InputStream paramInputStream, FileAccess paramFileAccess)
    throws IOException
  {
    FileChannel localFileChannel = new RandomAccessFile(paramFile, "rws").getChannel();
    localFileChannel.force(true);
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream(8192);
    byte[] arrayOfByte1 = new byte[4096];
    int j = 0;
    int i;
    while ((i = paramInputStream.read(arrayOfByte1)) != -1)
    {
      if (i > 0)
        synchronized (localFileChannel)
        {
          byte[] arrayOfByte2 = new byte[i];
          System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, i);
          ByteBuffer localByteBuffer = ByteBuffer.wrap(arrayOfByte2);
          localFileChannel.write(localByteBuffer);
        }
      j += i;
      if (this.E != -1)
      {
        localByteArrayOutputStream.write(arrayOfByte1, 0, i);
        if (this.E < j)
        {
          localByteArrayOutputStream.flush();
          localByteArrayOutputStream.close();
          ??? = localByteArrayOutputStream.toByteArray();
          this.linHintTable = new LinearizedHintTable(localFileChannel);
          paramPdfObjectReader.getObjectReader().storeLinearizedTables(this.linHintTable);
          this.linearizedBackgroundReaderer = new LinearThread(paramInputStream, localFileChannel, paramFile, this.linearObj, ???, this.linHintTable, paramFileAccess);
          return ???;
        }
      }
      else if (!this.isLinearizationTested)
      {
        testForLinearlized(arrayOfByte1, paramPdfObjectReader);
        if (this.linearObj != null)
        {
          this.E = this.linearObj.getInt(21);
          localByteArrayOutputStream.write(arrayOfByte1, 0, i);
        }
      }
    }
    paramInputStream.close();
    synchronized (localFileChannel)
    {
      localFileChannel.close();
    }
    return null;
  }

  public PdfObject readHintTable(PdfObjectReader paramPdfObjectReader)
    throws PdfException
  {
    long l = -1L;
    this.linearPageCount = -1;
    int i = this.linearObj.getInt(31);
    Object localObject;
    if (i != -1)
    {
      this.linearObj.setIntNumber(31, -1);
      paramPdfObjectReader.getObjectReader().readReferenceTable(this.linearObj);
      localObject = new PageObject(i, 0);
      paramPdfObjectReader.readObject((PdfObject)localObject);
      this.linearPageCount = this.linearObj.getInt(30);
      l = paramPdfObjectReader.getObjectReader().getOffset(i);
    }
    else
    {
      localObject = paramPdfObjectReader.getObjectReader().readReferenceTable(null);
    }
    int[] arrayOfInt = this.linearObj.getIntArray(24);
    byte[] arrayOfByte1 = paramPdfObjectReader.getObjectReader().getBytes(arrayOfInt[0], arrayOfInt[1]);
    int j = arrayOfByte1.length;
    int k = 0;
    int m = 0;
    int n = 0;
    int i1 = m;
    while ((arrayOfByte1[m] != 10) && (arrayOfByte1[m] != 13) && (arrayOfByte1[m] != 32) && (arrayOfByte1[m] != 47) && (arrayOfByte1[m] != 60) && (arrayOfByte1[m] != 62))
    {
      if ((arrayOfByte1[m] < 48) || (arrayOfByte1[m] > 57))
        n = 1;
      m++;
    }
    if (n == 0)
    {
      int i2 = NumberUtils.parseInt(i1, m, arrayOfByte1);
      while ((arrayOfByte1[m] == 10) || (arrayOfByte1[m] == 13) || (arrayOfByte1[m] == 32) || (arrayOfByte1[m] == 47) || (arrayOfByte1[m] == 60))
        m++;
      i1 = m;
      while ((m < 10) && (arrayOfByte1[m] != 10) && (arrayOfByte1[m] != 13) && (arrayOfByte1[m] != 32) && (arrayOfByte1[m] != 47) && (arrayOfByte1[m] != 60) && (arrayOfByte1[m] != 62))
        m++;
      int i3 = NumberUtils.parseInt(i1, m, arrayOfByte1);
      while (m < j - 1)
      {
        if ((arrayOfByte1[m] == 60) && (arrayOfByte1[(m + 1)] == 60))
        {
          k = m;
          m = j;
        }
        m++;
      }
      byte[] arrayOfByte2 = new byte[j - k];
      System.arraycopy(arrayOfByte1, k, arrayOfByte2, 0, arrayOfByte2.length);
      LinearizedObject localLinearizedObject = new LinearizedObject(i2, i3);
      localLinearizedObject.setStatus(2);
      localLinearizedObject.setUnresolvedData(arrayOfByte2, 2004845231);
      paramPdfObjectReader.checkResolved(localLinearizedObject);
      this.linHintTable.readTable(localLinearizedObject, this.linearObj, i, l);
    }
    return localObject;
  }

  public int getPageCount()
  {
    return this.linearPageCount;
  }

  public boolean hasLinearData()
  {
    return (this.linearObj != null) && (this.E != -1);
  }

  public PdfObject getLinearPageObject()
  {
    return this.linObject;
  }

  public PdfObject getLinearObject(boolean paramBoolean, PdfObjectReader paramPdfObjectReader)
  {
    if ((!this.isLinearizationTested) && (paramBoolean))
      testForLinearlized(paramPdfObjectReader.getObjectReader().getBytes(0L, 400), paramPdfObjectReader);
    return this.linearObj;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.linear.LinearParser
 * JD-Core Version:    0.6.2
 */