package org.jpedal.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.engines.AESFastEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.jpedal.constants.PDFflags;
import org.jpedal.exception.PdfSecurityException;
import org.jpedal.objects.raw.PdfArrayIterator;
import org.jpedal.objects.raw.PdfKeyPairsIterator;
import org.jpedal.objects.raw.PdfObject;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.ObjectCloneFactory;

public class DecryptionFactory
{
  private Map cachedObjects = new HashMap();
  private boolean extractionIsAllowed = true;
  private boolean isInitialised = false;
  private boolean isMetaDataEncypted = true;
  private boolean isPasswordSupplied = false;
  private boolean stringsEncoded = false;
  private boolean isEncrypted = false;
  private byte[] encryptionKey = null;
  private int rev = 0;
  private int P = 0;
  private byte[] O = null;
  private byte[] U = null;
  private byte[] OE = null;
  private byte[] Perms = null;
  private byte[] UE = null;
  Cipher cipher = null;
  private boolean isAES = false;
  private PdfObject StmFObj;
  private PdfObject StrFObj;
  private static boolean alwaysReinitCipher = false;
  private final String[] pad = { "28", "BF", "4E", "5E", "4E", "75", "8A", "41", "64", "00", "4E", "56", "FF", "FA", "01", "08", "2E", "2E", "00", "B6", "D0", "68", "3E", "80", "2F", "0C", "A9", "FE", "64", "53", "69", "7A" };
  private boolean isAESIdentity = false;
  private int keyLength = 5;
  private boolean isFileViewable = true;
  private int passwordStatus = 0;
  private byte[] ID = null;
  private byte[] encryptionPassword = null;
  private Certificate certificate;
  private Key key;

  public DecryptionFactory(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
  {
    this.ID = paramArrayOfByte1;
    this.encryptionPassword = paramArrayOfByte2;
  }

  public DecryptionFactory(byte[] paramArrayOfByte, Certificate paramCertificate, PrivateKey paramPrivateKey)
  {
    this.ID = paramArrayOfByte;
    this.certificate = paramCertificate;
    this.key = paramPrivateKey;
  }

  private boolean testPassword()
    throws PdfSecurityException
  {
    int i = 32;
    byte[] arrayOfByte1 = new byte[32];
    for (int j = 0; j < 32; j++)
      arrayOfByte1[j] = ((byte)Integer.parseInt(this.pad[j], 16));
    byte[] arrayOfByte3 = ObjectCloneFactory.cloneArray(arrayOfByte1);
    if (this.rev == 2)
    {
      this.encryptionKey = calculateKey(this.O, this.P, this.ID);
      arrayOfByte3 = decrypt(arrayOfByte3, "", true, null, false, false);
    }
    else if (this.rev >= 3)
    {
      int k = this.keyLength;
      if ((this.rev == 4) && (this.StmFObj != null))
      {
        int m = this.StmFObj.getInt(1043816557);
        if (m != -1)
          k = m;
      }
      i = 16;
      this.encryptionKey = calculateKey(this.O, this.P, this.ID);
      byte[] arrayOfByte4 = ObjectCloneFactory.cloneArray(this.encryptionKey);
      MessageDigest localMessageDigest = null;
      try
      {
        localMessageDigest = MessageDigest.getInstance("MD5");
      }
      catch (Exception localException)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog("Exception " + localException + " with digest");
      }
      localMessageDigest.update(arrayOfByte3);
      byte[] arrayOfByte2 = localMessageDigest.digest(this.ID);
      arrayOfByte2 = decrypt(arrayOfByte2, "", true, null, true, false);
      byte[] arrayOfByte5 = new byte[k];
      for (int n = 1; n <= 19; n++)
      {
        for (int i1 = 0; i1 < k; i1++)
          arrayOfByte5[i1] = ((byte)(arrayOfByte4[i1] ^ n));
        this.encryptionKey = arrayOfByte5;
        arrayOfByte2 = decrypt(arrayOfByte2, "", true, null, true, false);
      }
      this.encryptionKey = arrayOfByte4;
      arrayOfByte3 = new byte[32];
      System.arraycopy(arrayOfByte2, 0, arrayOfByte3, 0, 16);
      System.arraycopy(arrayOfByte1, 0, arrayOfByte3, 16, 16);
    }
    return compareKeys(this.U, arrayOfByte3, i);
  }

  private static boolean compareKeys(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt)
  {
    boolean bool = true;
    for (int i = 0; i < paramInt; i++)
      if (paramArrayOfByte1[i] != paramArrayOfByte2[i])
      {
        bool = false;
        i = paramArrayOfByte1.length;
      }
    return bool;
  }

  private void computeEncryptionKey()
    throws PdfSecurityException
  {
    byte[] arrayOfByte = getPaddedKey(this.encryptionPassword);
    try
    {
      MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
      this.encryptionKey = localMessageDigest.digest(arrayOfByte);
      if (this.rev >= 3)
        for (int i = 0; i < 50; i++)
          this.encryptionKey = localMessageDigest.digest(this.encryptionKey);
    }
    catch (Exception localException)
    {
      throw new PdfSecurityException("Exception " + localException + " generating encryption key");
    }
  }

  private boolean testOwnerPassword()
    throws PdfSecurityException
  {
    byte[] arrayOfByte1 = this.encryptionPassword;
    Object localObject = new byte[this.keyLength];
    byte[] arrayOfByte2 = ObjectCloneFactory.cloneArray(this.O);
    computeEncryptionKey();
    byte[] arrayOfByte3 = ObjectCloneFactory.cloneArray(this.encryptionKey);
    if (this.rev == 2)
    {
      localObject = decrypt(ObjectCloneFactory.cloneArray(this.O), "", false, null, false, false);
    }
    else if (this.rev >= 3)
    {
      int i = this.keyLength;
      if ((this.rev == 4) && (this.StmFObj != null))
      {
        int j = this.StmFObj.getInt(1043816557);
        if (j != -1)
          i = j;
      }
      localObject = arrayOfByte2;
      byte[] arrayOfByte4 = new byte[i];
      for (int k = 19; k >= 0; k--)
      {
        for (int m = 0; m < i; m++)
          arrayOfByte4[m] = ((byte)(arrayOfByte3[m] ^ k));
        this.encryptionKey = arrayOfByte4;
        localObject = decrypt((byte[])localObject, "", false, null, true, false);
      }
    }
    this.encryptionPassword = ((byte[])localObject);
    computeEncryptionKey();
    boolean bool = testPassword();
    if (!bool)
    {
      this.encryptionPassword = arrayOfByte1;
      computeEncryptionKey();
    }
    return bool;
  }

  private void verifyAccess()
    throws PdfSecurityException
  {
    this.isPasswordSupplied = false;
    this.extractionIsAllowed = false;
    this.passwordStatus = 0;
    boolean bool1 = false;
    boolean bool2 = false;
    if (this.rev < 5)
    {
      bool1 = testOwnerPassword();
      bool2 = testPassword();
    }
    else
    {
      try
      {
        bool1 = compareKeys(this.O, getV5Key(true, 32), 32);
        if (bool1)
        {
          this.encryptionKey = v5Decrypt(this.OE, getV5Key(true, 32));
        }
        else
        {
          bool2 = compareKeys(this.U, getV5Key(false, 32), 32);
          if (bool2)
            this.encryptionKey = v5Decrypt(this.UE, getV5Key(false, 40));
        }
      }
      catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog("Exception: " + localNoSuchAlgorithmException.getMessage());
      }
    }
    if (bool1)
      this.passwordStatus = 2;
    if (bool2)
      this.passwordStatus += 1;
    if (!bool1)
    {
      if (bool2)
      {
        if ((this.encryptionPassword != null) && (this.encryptionPassword.length > 0) && (LogWriter.isOutput()))
          LogWriter.writeLog("Correct user password supplied ");
        this.isFileViewable = true;
        this.isPasswordSupplied = true;
        if ((this.P & 0x10) == 16)
          this.extractionIsAllowed = true;
      }
      else
      {
        throw new PdfSecurityException("No valid password supplied");
      }
    }
    else
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Correct owner password supplied");
      this.isFileViewable = true;
      this.isPasswordSupplied = true;
      this.extractionIsAllowed = true;
    }
  }

  private static byte[] v5Decrypt(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    throws PdfSecurityException
  {
    int i = paramArrayOfByte1.length;
    byte[] arrayOfByte = new byte[i];
    try
    {
      CBCBlockCipher localCBCBlockCipher = new CBCBlockCipher(new AESFastEngine());
      localCBCBlockCipher.init(false, new KeyParameter(paramArrayOfByte2));
      int k = 0;
      while (k < i)
      {
        localCBCBlockCipher.processBlock(paramArrayOfByte1, k, arrayOfByte, k);
        int j = localCBCBlockCipher.getBlockSize();
        k += j;
      }
    }
    catch (Exception localException)
    {
      throw new PdfSecurityException("Exception " + localException.getMessage() + " with v5 encoding");
    }
    return arrayOfByte;
  }

  private byte[] getV5Key(boolean paramBoolean, int paramInt)
    throws NoSuchAlgorithmException
  {
    byte[] arrayOfByte = this.encryptionPassword;
    if (arrayOfByte == null)
      arrayOfByte = new byte[0];
    int i = arrayOfByte.length;
    if (i > 127)
      i = 127;
    MessageDigest localMessageDigest = MessageDigest.getInstance("SHA-256");
    localMessageDigest.update(arrayOfByte, 0, i);
    if (paramBoolean)
    {
      localMessageDigest.update(this.O, paramInt, 8);
      localMessageDigest.update(this.U, 0, 48);
    }
    else
    {
      localMessageDigest.update(this.U, paramInt, 8);
    }
    return localMessageDigest.digest();
  }

  private byte[] getPaddedKey(byte[] paramArrayOfByte)
  {
    byte[] arrayOfByte = new byte[32];
    int i = 0;
    if (paramArrayOfByte != null)
    {
      i = paramArrayOfByte.length;
      if (i > 32)
        i = 32;
    }
    if (this.encryptionPassword != null)
      System.arraycopy(this.encryptionPassword, 0, arrayOfByte, 0, i);
    for (int j = i; j < 32; j++)
      arrayOfByte[j] = ((byte)Integer.parseInt(this.pad[(j - i)], 16));
    return arrayOfByte;
  }

  private byte[] calculateKey(byte[] paramArrayOfByte1, int paramInt, byte[] paramArrayOfByte2)
    throws PdfSecurityException
  {
    byte[] arrayOfByte1 = getPaddedKey(this.encryptionPassword);
    byte[] arrayOfByte2;
    try
    {
      MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
      localMessageDigest.update(arrayOfByte1);
      localMessageDigest.update(paramArrayOfByte1);
      localMessageDigest.update(new byte[] { (byte)(paramInt & 0xFF), (byte)(paramInt >> 8 & 0xFF), (byte)(paramInt >> 16 & 0xFF), (byte)(paramInt >> 24 & 0xFF) });
      if (paramArrayOfByte2 != null)
        localMessageDigest.update(paramArrayOfByte2);
      if ((this.rev == 4) && (!this.isMetaDataEncypted))
        localMessageDigest.update(new byte[] { -1, -1, -1, -1 });
      byte[] arrayOfByte4 = new byte[this.keyLength];
      System.arraycopy(localMessageDigest.digest(), 0, arrayOfByte4, 0, this.keyLength);
      if (this.rev >= 3)
        for (int i = 0; i < 50; i++)
          System.arraycopy(localMessageDigest.digest(arrayOfByte4), 0, arrayOfByte4, 0, this.keyLength);
      arrayOfByte2 = new byte[this.keyLength];
      System.arraycopy(arrayOfByte4, 0, arrayOfByte2, 0, this.keyLength);
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception: " + localException.getMessage());
      throw new PdfSecurityException("Exception " + localException + " generating encryption key");
    }
    byte[] arrayOfByte3 = new byte[this.keyLength];
    System.arraycopy(arrayOfByte2, 0, arrayOfByte3, 0, this.keyLength);
    return arrayOfByte3;
  }

  public void readEncryptionObject(PdfObject paramPdfObject)
    throws PdfSecurityException
  {
    this.stringsEncoded = false;
    this.isMetaDataEncypted = true;
    this.StmFObj = null;
    this.StrFObj = null;
    this.isAES = false;
    if (!this.isInitialised)
    {
      this.isInitialised = true;
      SetSecurity.init();
    }
    int i = paramPdfObject.getInt(38);
    PdfArrayIterator localPdfArrayIterator = paramPdfObject.getMixedArray(1011108731);
    int j = 1467315058;
    if ((localPdfArrayIterator != null) && (localPdfArrayIterator.hasMoreTokens()))
      j = localPdfArrayIterator.getNextValueAsConstant(false);
    if (i == 3)
      throw new PdfSecurityException("Unsupported Custom Adobe Encryption method");
    if ((i > 4) && (j != 1467315058))
      throw new PdfSecurityException("Unsupported Encryption method");
    int k = paramPdfObject.getInt(1043816557) >> 3;
    if (k != -1)
      this.keyLength = k;
    this.rev = paramPdfObject.getInt(34);
    this.P = paramPdfObject.getInt(32);
    this.O = paramPdfObject.getTextStreamValueAsByte(31);
    this.U = paramPdfObject.getTextStreamValueAsByte(37);
    this.OE = paramPdfObject.getTextStreamValueAsByte(7957);
    this.UE = paramPdfObject.getTextStreamValueAsByte(9493);
    this.Perms = paramPdfObject.getTextStreamValueAsByte(893533539);
    if (i >= 4)
    {
      this.isAES = true;
      PdfObject localPdfObject = paramPdfObject.getDictionary(4886);
      if (i == 4)
        this.isMetaDataEncypted = paramPdfObject.getBoolean(-1815804199);
      this.isAESIdentity = false;
      String str2 = paramPdfObject.getName(591675926);
      PdfKeyPairsIterator localPdfKeyPairsIterator;
      String str1;
      if (str2 != null)
      {
        this.isAESIdentity = str2.equals("Identity");
        this.stringsEncoded = true;
        localPdfKeyPairsIterator = localPdfObject.getKeyPairsIterator();
        while (localPdfKeyPairsIterator.hasMorePairs())
        {
          str1 = localPdfKeyPairsIterator.getNextKeyAsString();
          if (str1.equals(str2))
            this.StrFObj = localPdfKeyPairsIterator.getNextValueAsDictionary();
          localPdfKeyPairsIterator.nextPair();
        }
      }
      str2 = paramPdfObject.getName(591674646);
      if (str2 != null)
      {
        this.isAESIdentity = str2.equals("Identity");
        localPdfKeyPairsIterator = localPdfObject.getKeyPairsIterator();
        while (localPdfKeyPairsIterator.hasMorePairs())
        {
          str1 = localPdfKeyPairsIterator.getNextKeyAsString();
          if (str1.equals(str2))
            this.StmFObj = localPdfKeyPairsIterator.getNextValueAsDictionary();
          localPdfKeyPairsIterator.nextPair();
        }
      }
    }
    this.isEncrypted = true;
    this.isFileViewable = false;
    if (LogWriter.isOutput())
      LogWriter.writeLog("File has encryption settings");
    if (j == 1467315058)
    {
      try
      {
        verifyAccess();
      }
      catch (PdfSecurityException localPdfSecurityException)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog("File requires password");
      }
    }
    else if (this.certificate != null)
    {
      this.isFileViewable = true;
      this.isPasswordSupplied = true;
      this.extractionIsAllowed = true;
      this.passwordStatus = 2;
    }
    if (this.rev == 5)
    {
      this.Perms = v5Decrypt(this.Perms, this.encryptionKey);
      this.isMetaDataEncypted = (this.Perms[8] == 84);
      this.P = (this.Perms[0] & 0xFF | (this.Perms[1] & 0xFF) << 8 | (this.Perms[2] & 0xFF) << 16 | (this.Perms[2] & 0xFF) << 24);
    }
  }

  private void setPasswordFromCertificate(PdfObject paramPdfObject)
  {
    byte[][] arrayOfByte1 = paramPdfObject.getStringArray(1752671921);
    if (arrayOfByte1 != null)
    {
      byte[] arrayOfByte2 = SetSecurity.extractCertificateData(arrayOfByte1, this.certificate, this.key);
      if (arrayOfByte2 != null)
        try
        {
          MessageDigest localMessageDigest = MessageDigest.getInstance("SHA-1");
          localMessageDigest.update(arrayOfByte2, 0, 20);
          for (byte[] arrayOfByte4 : arrayOfByte1)
            localMessageDigest.update(arrayOfByte4);
          if (!this.isMetaDataEncypted)
            localMessageDigest.update(new byte[] { -1, -1, -1, -1 });
          this.encryptionKey = localMessageDigest.digest();
        }
        catch (Exception localException)
        {
          if (LogWriter.isOutput())
            LogWriter.writeLog("Exception: " + localException.getMessage());
        }
    }
  }

  public byte[] decrypt(byte[] paramArrayOfByte, String paramString1, boolean paramBoolean1, String paramString2, boolean paramBoolean2, boolean paramBoolean3)
    throws PdfSecurityException
  {
    if ((getBooleanValue(101)) || (paramBoolean1))
    {
      BufferedOutputStream localBufferedOutputStream = null;
      BufferedInputStream localBufferedInputStream = null;
      int i = 0;
      byte[] arrayOfByte1 = null;
      if (paramString2 != null)
        try
        {
          if (paramArrayOfByte == null)
          {
            arrayOfByte1 = new byte[16];
            localObject1 = new FileInputStream(paramString2);
            ((FileInputStream)localObject1).read(arrayOfByte1);
            ((FileInputStream)localObject1).close();
          }
          Object localObject1 = File.createTempFile("jpedal", ".raw", new File(ObjectStore.temp_dir));
          this.cachedObjects.put(((File)localObject1).getAbsolutePath(), "x");
          ObjectStore.copy(paramString2, ((File)localObject1).getAbsolutePath());
          localObject2 = new File(paramString2);
          ((File)localObject2).delete();
          localBufferedOutputStream = new BufferedOutputStream(new FileOutputStream(paramString2));
          localBufferedInputStream = new BufferedInputStream(new FileInputStream((File)localObject1));
        }
        catch (IOException localIOException1)
        {
          if (LogWriter.isOutput())
            LogWriter.writeLog("Exception " + localIOException1 + " in decrypt");
        }
      int j = this.keyLength;
      Object localObject2 = "RC4";
      String str = "RC4";
      IvParameterSpec localIvParameterSpec = null;
      PdfObject localPdfObject;
      if (!paramBoolean3)
        localPdfObject = this.StmFObj;
      else
        localPdfObject = this.StrFObj;
      if (this.certificate != null)
      {
        setPasswordFromCertificate(localPdfObject);
        localPdfObject.setIntNumber(1043816557, 16);
      }
      if ((!paramBoolean2) && (localPdfObject == null) && (this.isAESIdentity))
        return paramArrayOfByte;
      Object localObject3;
      int n;
      int i1;
      if (localPdfObject != null)
      {
        int k = localPdfObject.getInt(1043816557);
        if (k != -1)
          j = k;
        localObject3 = localPdfObject.getName(1250845);
        if ((localObject3 != null) && (!paramBoolean2) && ((((String)localObject3).equals("AESV2")) || (((String)localObject3).equals("AESV3"))))
        {
          this.cipher = null;
          localObject2 = "AES/CBC/PKCS5Padding";
          str = "AES";
          i = 1;
          byte[] arrayOfByte3 = new byte[16];
          if (arrayOfByte1 != null)
            System.arraycopy(arrayOfByte1, 0, arrayOfByte3, 0, 16);
          else
            System.arraycopy(paramArrayOfByte, 0, arrayOfByte3, 0, 16);
          localIvParameterSpec = new IvParameterSpec(arrayOfByte3);
          if (paramArrayOfByte == null)
          {
            try
            {
              localBufferedInputStream.skip(16L);
            }
            catch (IOException localIOException2)
            {
              if (LogWriter.isOutput())
                LogWriter.writeLog("Exception: " + localIOException2.getMessage());
            }
          }
          else
          {
            n = paramArrayOfByte.length;
            i1 = n - 16;
            byte[] arrayOfByte4 = new byte[i1];
            System.arraycopy(paramArrayOfByte, 16, arrayOfByte4, 0, i1);
            paramArrayOfByte = arrayOfByte4;
            int i3 = paramArrayOfByte.length & 0xF;
            int i4 = paramArrayOfByte.length;
            if (i3 > 0)
            {
              i4 = i4 + 16 - i3;
              arrayOfByte4 = new byte[i4];
              System.arraycopy(paramArrayOfByte, 0, arrayOfByte4, 0, paramArrayOfByte.length);
              paramArrayOfByte = arrayOfByte4;
            }
          }
        }
      }
      byte[] arrayOfByte2 = new byte[j];
      if (!paramString1.isEmpty())
        arrayOfByte2 = new byte[j + 5];
      System.arraycopy(this.encryptionKey, 0, arrayOfByte2, 0, j);
      try
      {
        Object localObject5;
        if (this.rev == 5)
        {
          localObject3 = new byte[32];
          System.arraycopy(arrayOfByte2, 0, localObject3, 0, localObject3.length);
        }
        else
        {
          if (!paramString1.isEmpty())
          {
            int m = paramString1.indexOf(32);
            n = paramString1.indexOf(32, m + 1);
            i1 = Integer.parseInt(paramString1.substring(0, m));
            int i2 = Integer.parseInt(paramString1.substring(m + 1, n));
            arrayOfByte2[j] = ((byte)(i1 & 0xFF));
            arrayOfByte2[(j + 1)] = ((byte)(i1 >> 8 & 0xFF));
            arrayOfByte2[(j + 2)] = ((byte)(i1 >> 16 & 0xFF));
            arrayOfByte2[(j + 3)] = ((byte)(i2 & 0xFF));
            arrayOfByte2[(j + 4)] = ((byte)(i2 >> 8 & 0xFF));
          }
          localObject3 = new byte[Math.min(arrayOfByte2.length, 16)];
          if (!paramString1.isEmpty())
          {
            localObject4 = MessageDigest.getInstance("MD5");
            ((MessageDigest)localObject4).update(arrayOfByte2);
            if ((i != 0) && (j >= 16))
            {
              localObject5 = new byte[] { 115, 65, 108, 84 };
              ((MessageDigest)localObject4).update((byte[])localObject5);
            }
            System.arraycopy(((MessageDigest)localObject4).digest(), 0, localObject3, 0, localObject3.length);
          }
          else
          {
            System.arraycopy(arrayOfByte2, 0, localObject3, 0, localObject3.length);
          }
        }
        if (this.cipher == null)
          this.cipher = Cipher.getInstance((String)localObject2);
        Object localObject4 = new SecretKeySpec((byte[])localObject3, str);
        if (paramBoolean1)
          this.cipher.init(1, (Key)localObject4);
        else if (localIvParameterSpec == null)
          this.cipher.init(2, (Key)localObject4);
        else
          this.cipher.init(2, (Key)localObject4, localIvParameterSpec);
        if (localBufferedOutputStream != null)
        {
          localObject5 = new CipherInputStream(localBufferedInputStream, this.cipher);
          while (true)
          {
            i1 = ((CipherInputStream)localObject5).read();
            if (i1 == -1)
              break;
            localBufferedOutputStream.write(i1);
          }
          ((CipherInputStream)localObject5).close();
          localBufferedOutputStream.close();
          localBufferedInputStream.close();
        }
        if (paramArrayOfByte != null)
          paramArrayOfByte = this.cipher.doFinal(paramArrayOfByte);
      }
      catch (Exception localException)
      {
        throw new PdfSecurityException("Exception " + localException + " decrypting content");
      }
    }
    if (alwaysReinitCipher)
      this.cipher = null;
    return paramArrayOfByte;
  }

  public boolean getBooleanValue(int paramInt)
  {
    switch (paramInt)
    {
    case 100:
      return this.isFileViewable;
    case 101:
      return this.isEncrypted;
    case 102:
      return this.isMetaDataEncypted;
    case 103:
      return this.extractionIsAllowed;
    case 104:
      return this.isPasswordSupplied;
    }
    return false;
  }

  public byte[] decryptString(byte[] paramArrayOfByte, String paramString)
    throws PdfSecurityException
  {
    try
    {
      if ((!this.isAES) || (this.stringsEncoded) || (this.isMetaDataEncypted))
        paramArrayOfByte = decrypt(paramArrayOfByte, paramString, false, null, false, true);
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Unable to decrypt string in Object " + paramString + ' ' + new String(paramArrayOfByte));
    }
    return paramArrayOfByte;
  }

  public int getPDFflag(Integer paramInteger)
  {
    if (paramInteger.equals(PDFflags.USER_ACCESS_PERMISSIONS))
      return this.P;
    if (paramInteger.equals(PDFflags.VALID_PASSWORD_SUPPLIED))
      return this.passwordStatus;
    return -1;
  }

  public void reset(byte[] paramArrayOfByte)
  {
    this.encryptionPassword = paramArrayOfByte;
    this.cipher = null;
  }

  public void flush()
  {
    if (this.cachedObjects != null)
    {
      Iterator localIterator = this.cachedObjects.keySet().iterator();
      while (localIterator.hasNext())
      {
        Object localObject = localIterator.next();
        String str = (String)localObject;
        File localFile = new File(str);
        localFile.delete();
        if ((LogWriter.isOutput()) && (localFile.exists()))
          LogWriter.writeLog("Unable to delete temp file " + str);
      }
    }
  }

  public void dispose()
  {
    this.cachedObjects = null;
  }

  public boolean hasPassword()
  {
    return (this.O != null) || (this.U != null);
  }

  static
  {
    String str = System.getProperty("org.jpedal.cipher.reinit");
    if ((str != null) && (str.toLowerCase().equals("true")))
      alwaysReinitCipher = true;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.io.DecryptionFactory
 * JD-Core Version:    0.6.2
 */