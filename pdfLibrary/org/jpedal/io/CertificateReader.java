package org.jpedal.io;

import java.security.Key;
import java.security.cert.Certificate;
import java.util.Collection;
import org.bouncycastle.cms.CMSEnvelopedData;
import org.bouncycastle.cms.RecipientId;
import org.bouncycastle.cms.RecipientInformation;
import org.bouncycastle.cms.RecipientInformationStore;
import org.jpedal.utils.LogWriter;

public class CertificateReader
{
  public static byte[] readCertificate(byte[][] paramArrayOfByte, Certificate paramCertificate, Key paramKey)
  {
    byte[] arrayOfByte1 = null;
    String str = "BC";
    for (byte[] arrayOfByte2 : paramArrayOfByte)
      try
      {
        CMSEnvelopedData localCMSEnvelopedData = new CMSEnvelopedData(arrayOfByte2);
        Object[] arrayOfObject = localCMSEnvelopedData.getRecipientInfos().getRecipients().toArray();
        int k = arrayOfObject.length;
        for (int m = 0; m < k; m++)
        {
          RecipientInformation localRecipientInformation = (RecipientInformation)arrayOfObject[m];
          if (localRecipientInformation.getRID().match(paramCertificate))
          {
            arrayOfByte1 = localRecipientInformation.getContent(paramKey, str);
            m = k;
          }
        }
      }
      catch (Exception localException)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog("Exception: " + localException.getMessage());
      }
    return arrayOfByte1;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.io.CertificateReader
 * JD-Core Version:    0.6.2
 */