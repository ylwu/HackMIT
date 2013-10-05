package org.jpedal.linear;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import org.jpedal.FileAccess;
import org.jpedal.io.LinearizedHintTable;
import org.jpedal.io.ObjectStore;
import org.jpedal.io.PdfObjectReader;
import org.jpedal.objects.raw.PdfObject;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.NumberUtils;

public class LinearThread extends Thread
{
  public int percentageDone = 0;
  FileChannel fos;
  PdfObject linearObj;
  InputStream is;
  File tempURLFile;
  LinearizedHintTable linHintTable;
  final byte[] startObj = { 111, 98, 106 };
  final byte[] endObj = { 101, 110, 100, 111, 98, 106 };
  int startCharReached = 0;
  int endCharReached = 0;
  int startObjPtr = 0;
  int endObjPtr = 0;
  int bufSize = 8192;
  int lastBytes = 8192;
  int ref = 0;
  int firstObjLength = 0;
  FileAccess fileAccess;
  byte[] linearBytes;

  public LinearThread(InputStream paramInputStream, FileChannel paramFileChannel, File paramFile, PdfObject paramPdfObject, byte[] paramArrayOfByte, LinearizedHintTable paramLinearizedHintTable, FileAccess paramFileAccess)
  {
    this.fos = paramFileChannel;
    this.linearObj = paramPdfObject;
    this.is = paramInputStream;
    this.tempURLFile = paramFile;
    this.linHintTable = paramLinearizedHintTable;
    this.linearBytes = paramArrayOfByte;
    this.fileAccess = paramFileAccess;
    this.firstObjLength = paramArrayOfByte.length;
    scanStreamForObjects(0, null, paramArrayOfByte);
    setDaemon(true);
  }

  public int getPercentageLoaded()
  {
    return this.percentageDone;
  }

  public void run()
  {
    int i = this.linearObj.getInt(28);
    try
    {
      int k = 0;
      byte[] arrayOfByte1 = this.linearBytes;
      Object localObject1 = new byte[this.bufSize];
      int j;
      while (((j = this.is.read((byte[])localObject1)) != -1) && (!isInterrupted()) && (isAlive()))
      {
        if (j > 0)
          synchronized (this.fos)
          {
            byte[] arrayOfByte2 = new byte[j];
            System.arraycopy(localObject1, 0, arrayOfByte2, 0, j);
            localObject1 = arrayOfByte2;
            ByteBuffer localByteBuffer = ByteBuffer.wrap(arrayOfByte2);
            this.fos.write(localByteBuffer);
          }
        if (j > 0)
        {
          scanStreamForObjects(this.firstObjLength + k, arrayOfByte1, (byte[])localObject1);
          k += j;
          ??? = 30;
          Object localObject2 = localObject1.length;
          if (??? > localObject2 - 1)
            ??? = localObject2 - 1;
          arrayOfByte1 = new byte[???];
          System.arraycopy(localObject1, localObject2 - ???, arrayOfByte1, 0, ???);
        }
        this.percentageDone = ((int)(100.0F * (k / i)));
      }
      this.linHintTable.setFinishedReading();
    }
    catch (IOException localIOException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception: " + localIOException.getMessage());
    }
    finally
    {
      try
      {
        this.is.close();
        this.fileAccess.waitForDecodingToFinish();
        this.fileAccess.setIO(new PdfObjectReader());
        if ((isAlive()) && (!isInterrupted()))
        {
          this.fileAccess.openPdfFile(this.tempURLFile.getAbsolutePath());
          this.fileAccess.getObjectStore().storeFileName(this.tempURLFile.getName().substring(0, this.tempURLFile.getName().lastIndexOf('.')));
        }
      }
      catch (Exception localException3)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog("Exception: " + localException3.getMessage());
      }
    }
  }

  private void scanStreamForObjects(int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
  {
    int i = paramArrayOfByte2.length;
    for (int j = 0; j < i; j++)
      if (this.startCharReached == 0)
      {
        if ((paramArrayOfByte2[j] == 32) || (paramArrayOfByte2[j] == 0) || (paramArrayOfByte2[j] == 10) || (paramArrayOfByte2[j] == 32))
          this.startCharReached += 1;
      }
      else if (this.startCharReached < 4)
      {
        if (paramArrayOfByte2[j] == this.startObj[(this.startCharReached - 1)])
        {
          if (this.startCharReached == 3)
          {
            this.startObjPtr = (paramInt + j - 4);
            int k = j - 4;
            byte[] arrayOfByte;
            if ((paramArrayOfByte1 != null) && (k < 30))
            {
              m = paramArrayOfByte1.length;
              int n = paramArrayOfByte2.length;
              arrayOfByte = new byte[m + n];
              System.arraycopy(paramArrayOfByte1, 0, arrayOfByte, 0, m);
              System.arraycopy(paramArrayOfByte2, 0, arrayOfByte, m, n);
              k += m;
            }
            else
            {
              arrayOfByte = paramArrayOfByte2;
            }
            int m = k;
            while ((arrayOfByte[k] != 10) && (arrayOfByte[k] != 13) && (arrayOfByte[k] != 32) && (arrayOfByte[k] != 9))
            {
              k--;
              this.startObjPtr -= 1;
            }
            NumberUtils.parseInt(k + 1, m, arrayOfByte);
            while ((arrayOfByte[k] == 10) || (arrayOfByte[k] == 13) || (arrayOfByte[k] == 32) || (arrayOfByte[k] == 47) || (arrayOfByte[k] == 60))
            {
              k--;
              this.startObjPtr -= 1;
            }
            m = k + 1;
            while ((arrayOfByte[k] != 10) && (arrayOfByte[k] != 13) && (arrayOfByte[k] != 32) && (arrayOfByte[k] != 47) && (arrayOfByte[k] != 60) && (arrayOfByte[k] != 62))
            {
              k--;
              this.startObjPtr -= 1;
            }
            this.ref = NumberUtils.parseInt(k + 1, m, arrayOfByte);
          }
          this.startCharReached += 1;
        }
        else
        {
          this.startCharReached = 0;
        }
      }
      else if (paramArrayOfByte2[j] == this.endObj[this.endCharReached])
      {
        this.endCharReached += 1;
        if (this.endCharReached == 6)
        {
          this.endObjPtr = (paramInt + j);
          this.linHintTable.storeOffset(this.ref, this.startObjPtr, this.endObjPtr);
          this.startCharReached = 0;
          this.endCharReached = 0;
        }
      }
      else
      {
        this.endCharReached = 0;
      }
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.linear.LinearThread
 * JD-Core Version:    0.6.2
 */