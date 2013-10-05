package org.jpedal.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.jpedal.io.filter.ASCII85;
import org.jpedal.io.filter.ASCIIHex;
import org.jpedal.io.filter.CCITT;
import org.jpedal.io.filter.Flate;
import org.jpedal.io.filter.LZW;
import org.jpedal.io.filter.PdfFilter;
import org.jpedal.io.filter.RunLength;
import org.jpedal.objects.raw.PdfArrayIterator;
import org.jpedal.objects.raw.PdfObject;
import org.jpedal.utils.LogWriter;

public class PdfFilteredReader
{
  private static final int A85 = 1116165;
  private static final int AHx = 1120328;
  private static final int ASCII85Decode = 1582784916;
  private static final int ASCIIHexDecode = 2074112677;
  public static final int CCITTFaxDecode = 2108391315;
  private static final int CCF = 1250070;
  private static final int Crypt = 1112096855;
  public static final int DCTDecode = 1180911742;
  public static final int Fl = 5692;
  public static final int FlateDecode = 2005566619;
  public static final int JBIG2Decode = 1247500931;
  public static final int JPXDecode = 1399277700;
  private static final int LZW = 1845799;
  private static final int LZWDecode = 1566984326;
  private static final int RL = 8732;
  private static final int RunLengthDecode = -1815163937;
  private final Map cachedObjects = new HashMap();
  private BufferedOutputStream streamCache = null;
  private BufferedInputStream bis = null;
  private boolean hasError = false;

  public byte[] decodeFilters(PdfObject[] paramArrayOfPdfObject, byte[] paramArrayOfByte, PdfArrayIterator paramPdfArrayIterator, int paramInt1, int paramInt2, String paramString)
    throws Exception
  {
    this.streamCache = null;
    this.bis = null;
    int i = paramArrayOfPdfObject.length;
    PdfObject localPdfObject1 = paramArrayOfPdfObject[0];
    byte[] arrayOfByte1 = null;
    if (localPdfObject1 != null)
    {
      PdfObject localPdfObject2 = localPdfObject1.getDictionary(1314558361);
      if (localPdfObject2 != null)
        arrayOfByte1 = localPdfObject2.getDecodedStream();
    }
    int j = paramString != null ? 1 : 0;
    int m = paramPdfArrayIterator.getTokenCount();
    int n = 0;
    if (m > 0)
      while (paramPdfArrayIterator.hasMoreTokens())
      {
        int k = paramPdfArrayIterator.getNextValueAsConstant(true);
        int i1 = 0;
        if (i > 1)
        {
          localPdfObject1 = paramArrayOfPdfObject[n];
          arrayOfByte1 = null;
          if (localPdfObject1 != null)
          {
            PdfObject localPdfObject3 = localPdfObject1.getDictionary(1314558361);
            if (localPdfObject3 != null)
              arrayOfByte1 = localPdfObject3.getDecodedStream();
          }
        }
        if ((j != 0) && (paramString != null) && ((k == 1112096855) || (k == 1180911742) || (k == 1399277700)))
        {
          n++;
        }
        else
        {
          if ((j != 0) && (paramString != null))
            setupCachedObjectForDecoding(paramString);
          Object localObject;
          if ((k == 2005566619) || (k == 5692))
          {
            localObject = new Flate(localPdfObject1);
          }
          else if ((k == 1582784916) || (k == 1116165))
          {
            localObject = new ASCII85(localPdfObject1);
          }
          else if ((k == 2108391315) || (k == 1250070))
          {
            localObject = new CCITT(localPdfObject1, paramInt1, paramInt2);
          }
          else if ((k == 1566984326) || (k == 1845799))
          {
            localObject = new LZW(localPdfObject1, paramInt1, paramInt2);
            i1 = 1;
          }
          else if ((k == -1815163937) || (k == 8732))
          {
            localObject = new RunLength(localPdfObject1);
          }
          else if (k == 1247500931)
          {
            if (paramArrayOfByte == null)
            {
              paramArrayOfByte = new byte[this.bis.available()];
              this.bis.read(paramArrayOfByte);
              int i2 = -1;
              for (int i3 = paramArrayOfByte.length - 1; i3 > 9; i3--)
                if ((paramArrayOfByte[i3] == 101) && (paramArrayOfByte[(i3 + 1)] == 110) && (paramArrayOfByte[(i3 + 2)] == 100) && (paramArrayOfByte[(i3 + 3)] == 115) && (paramArrayOfByte[(i3 + 4)] == 116) && (paramArrayOfByte[(i3 + 5)] == 114) && (paramArrayOfByte[(i3 + 6)] == 101) && (paramArrayOfByte[(i3 + 7)] == 97) && (paramArrayOfByte[(i3 + 8)] == 109))
                {
                  i2 = i3 - 1;
                  i3 = -1;
                }
              if (i2 != -1)
              {
                if ((paramArrayOfByte[i2] == 10) && (paramArrayOfByte[(i2 - 1)] == 13))
                  i2--;
                byte[] arrayOfByte2 = paramArrayOfByte;
                paramArrayOfByte = new byte[i2];
                System.arraycopy(arrayOfByte2, 0, paramArrayOfByte, 0, i2);
              }
              paramArrayOfByte = JBIG2.JBIGDecode(paramArrayOfByte, arrayOfByte1);
              this.streamCache.write(paramArrayOfByte);
              paramArrayOfByte = null;
            }
            else
            {
              paramArrayOfByte = JBIG2.JBIGDecode(paramArrayOfByte, arrayOfByte1);
            }
            localObject = null;
          }
          else if ((k == 2074112677) || (k == 1120328))
          {
            localObject = new ASCIIHex(localPdfObject1);
          }
          else if (k == 1112096855)
          {
            localObject = null;
          }
          else
          {
            localObject = null;
          }
          if (localObject != null)
            try
            {
              if (paramArrayOfByte != null)
                paramArrayOfByte = ((PdfFilter)localObject).decode(paramArrayOfByte);
              else if (this.bis != null)
                ((PdfFilter)localObject).decode(this.bis, this.streamCache, paramString, this.cachedObjects);
              if ((!this.hasError) && (((PdfFilter)localObject).hasError()))
                this.hasError = true;
            }
            catch (Exception localException)
            {
              if (LogWriter.isOutput())
                LogWriter.writeLog("Exception " + localException + " in " + getFilterName(k) + " decompression");
              if (i1 != 0)
                paramArrayOfByte = null;
            }
          if (j != 0)
          {
            if (this.bis != null)
              this.bis.close();
            if (this.streamCache != null)
            {
              this.streamCache.flush();
              this.streamCache.close();
            }
          }
          n++;
        }
      }
    return paramArrayOfByte;
  }

  private static String getFilterName(int paramInt)
  {
    switch (paramInt)
    {
    case 1116165:
      return "A85";
    case 1120328:
      return "AHx";
    case 1582784916:
      return "ASCII85Decode";
    case 2074112677:
      return "ASCIIHexDecode";
    case 2108391315:
      return "CCITTFaxDecode";
    case 1250070:
      return "CCF";
    case 1112096855:
      return "Crypt";
    case 1180911742:
      return "DCTDecode";
    case 5692:
      return "Fl";
    case 2005566619:
      return "FlateDecode";
    case 1247500931:
      return "JBIG2Decode";
    case 1399277700:
      return "";
    case 1845799:
      return "";
    case 1566984326:
      return "";
    case 8732:
      return "";
    case -1815163937:
      return "";
    }
    return "Unknown";
  }

  private void setupCachedObjectForDecoding(String paramString)
    throws IOException
  {
    File localFile1 = File.createTempFile("jpedal", ".raw", new File(ObjectStore.temp_dir));
    this.cachedObjects.put(localFile1.getAbsolutePath(), "x");
    ObjectStore.copy(paramString, localFile1.getAbsolutePath());
    File localFile2 = new File(paramString);
    localFile2.delete();
    this.streamCache = new BufferedOutputStream(new FileOutputStream(paramString));
    this.bis = new BufferedInputStream(new FileInputStream(localFile1));
  }

  public boolean hasError()
  {
    return this.hasError;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.io.PdfFilteredReader
 * JD-Core Version:    0.6.2
 */