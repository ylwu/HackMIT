package org.jpedal.objects;

import org.jpedal.io.DecryptionFactory;
import org.jpedal.io.ObjectDecoder;
import org.jpedal.io.PdfFileReader;
import org.jpedal.io.PdfObjectReader;
import org.jpedal.objects.raw.MetadataObject;
import org.jpedal.objects.raw.PdfObject;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.StringUtils;

public class PdfFileInformation
{
  private static final String[] information_fields = { "Title", "Author", "Subject", "Keywords", "Creator", "Producer", "CreationDate", "ModDate", "Trapped" };
  public static final int[] information_field_IDs = { 960773209, 1144541319, 978876534, 1517780362, 827818359, 1702196342, 1806481572, 340689769, 1080325989 };
  private String[] information_values = { "", "", "", "", "", "", "", "", "" };
  private String XMLmetadata = null;
  private byte[] rawData = null;

  public static String[] getFieldNames()
  {
    return information_fields;
  }

  public String getFileXMLMetaData()
  {
    if (this.rawData == null)
      return "";
    if (this.XMLmetadata == null)
    {
      int i = this.rawData.length;
      Object localObject = new byte[i];
      System.arraycopy(this.rawData, 0, localObject, 0, i);
      int j = localObject.length;
      int k = 0;
      int m = 0;
      for (int n = 0; n < j; n++)
        if ((m == 13) && (localObject[n] == 10))
        {
          localObject[(k - 1)] = 10;
        }
        else if (((m != 10) && (m != 32)) || ((localObject[n] != 32) && (localObject[n] != 10)))
        {
          localObject[k] = localObject[n];
          m = localObject[n];
          k++;
        }
      if (k != j)
      {
        byte[] arrayOfByte = new byte[k];
        System.arraycopy(localObject, 0, arrayOfByte, 0, k);
        localObject = arrayOfByte;
      }
      this.XMLmetadata = new String((byte[])localObject);
    }
    return this.XMLmetadata;
  }

  public void setFileXMLMetaData(byte[] paramArrayOfByte)
  {
    this.rawData = paramArrayOfByte;
  }

  public String[] getFieldValues()
  {
    return this.information_values;
  }

  public void setFieldValue(int paramInt, String paramString)
  {
    this.information_values[paramInt] = paramString;
  }

  public void readInformationObject(PdfObject paramPdfObject, ObjectDecoder paramObjectDecoder)
  {
    try
    {
      paramObjectDecoder.checkResolved(paramPdfObject);
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception: " + localException.getMessage());
    }
    int j = information_field_IDs.length;
    for (int k = 0; k < j; k++)
    {
      int i = information_field_IDs[k];
      String str;
      if (i == 1080325989)
      {
        str = paramPdfObject.getName(i);
        if (str == null)
          str = "";
      }
      else
      {
        byte[] arrayOfByte = paramPdfObject.getTextStreamValueAsByte(i);
        if (arrayOfByte == null)
          str = "";
        else
          str = StringUtils.getTextString(arrayOfByte, false);
      }
      setFieldValue(k, str);
    }
  }

  public final PdfFileInformation readPdfFileMetadata(PdfObject paramPdfObject, PdfObjectReader paramPdfObjectReader)
  {
    PdfFileReader localPdfFileReader = paramPdfObjectReader.getObjectReader();
    ObjectDecoder localObjectDecoder = new ObjectDecoder(paramPdfObjectReader.getObjectReader());
    DecryptionFactory localDecryptionFactory = localPdfFileReader.getDecryptionObject();
    PdfObject localPdfObject = localPdfFileReader.getInfoObject();
    if ((localPdfObject != null) && ((localDecryptionFactory == null) || ((!localDecryptionFactory.getBooleanValue(101)) && (!localDecryptionFactory.getBooleanValue(104)))))
      readInformationObject(localPdfObject, localObjectDecoder);
    if (paramPdfObject != null)
    {
      String str = new String(paramPdfObject.getUnresolvedData());
      MetadataObject localMetadataObject = new MetadataObject(str);
      localPdfFileReader.readObject(localMetadataObject);
      byte[] arrayOfByte1 = localMetadataObject.getDecodedStream();
      this.rawData = arrayOfByte1;
      if (this.rawData != null)
      {
        for (int i = this.rawData.length; (i > 1) && (this.rawData[(i - 1)] != 62); i--);
        if (i > 0)
        {
          byte[] arrayOfByte2 = new byte[i];
          System.arraycopy(this.rawData, 0, arrayOfByte2, 0, i);
          this.rawData = arrayOfByte2;
        }
      }
    }
    return this;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.PdfFileInformation
 * JD-Core Version:    0.6.2
 */