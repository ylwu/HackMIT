package org.jpedal.io;

import java.security.Key;
import java.security.Provider;
import java.security.Security;
import java.security.cert.Certificate;
import org.jpedal.utils.LogWriter;

public class SetSecurity
{
  private static String altSP = null;

  public static void init()
  {
    altSP = System.getProperty("org.jpedal.securityprovider");
    if (altSP == null)
      altSP = "org.bouncycastle.jce.provider.BouncyCastleProvider";
    try
    {
      Class localClass = Class.forName(altSP);
      Provider localProvider = (Provider)localClass.newInstance();
      Security.addProvider(localProvider);
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Unable to run custom security provider " + altSP + " Exception " + localException);
      throw new RuntimeException("This PDF file is encrypted and JPedal needs an additional library to \ndecode on the classpath (we recommend bouncycastle library).\nThere is additional explanation at http://www.idrsolutions.com/additional-jars\n");
    }
  }

  public static byte[] extractCertificateData(byte[][] paramArrayOfByte, Certificate paramCertificate, Key paramKey)
  {
    if ((altSP == null) || (!altSP.equals("org.bouncycastle.jce.provider.BouncyCastleProvider")))
      throw new RuntimeException("only Bouncy castle currently supported with certificates");
    byte[] arrayOfByte = null;
    arrayOfByte = CertificateReader.readCertificate(paramArrayOfByte, paramCertificate, paramKey);
    return arrayOfByte;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.io.SetSecurity
 * JD-Core Version:    0.6.2
 */