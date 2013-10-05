package org.jpedal.examples.viewer.objects;

import java.io.File;

public class SignData
{
  private boolean signMode;
  private boolean canEncrypt;
  private boolean flatten;
  private boolean isVisibleSignature;
  private String outputPath;
  private String keyFilePath;
  private String keyStorePath;
  private String alias;
  private String reason;
  private String location;
  private char[] keyFilePassword;
  private char[] keyStorePassword;
  private char[] aliasPassword;
  private char[] encryptUserPassword;
  private char[] encryptOwnerPassword;
  private int certifyMode;
  private int encryptPermissions;
  float x1;
  float y1;
  float x2;
  float y2;
  private File outputFile;
  private File keyFile;
  private boolean valid = false;
  private String invalidMessage;
  private int signaturePage;
  private boolean appendMode;

  public boolean isKeystoreSign()
  {
    return this.signMode;
  }

  public void setSignMode(boolean paramBoolean)
  {
    this.signMode = paramBoolean;
  }

  public void setOutputFilePath(String paramString)
  {
    this.outputPath = paramString;
  }

  public String getOutputFilePath()
  {
    return this.outputPath;
  }

  public File getOutput()
  {
    return this.outputFile;
  }

  public void setKeyFilePath(String paramString)
  {
    this.keyFilePath = paramString;
  }

  public String getKeyFilePath()
  {
    return this.keyFilePath;
  }

  public File getKeyFile()
  {
    return this.keyFile;
  }

  public void setKeyStorePath(String paramString)
  {
    this.keyStorePath = paramString;
  }

  public String getKeyStorePath()
  {
    return this.keyStorePath;
  }

  public char[] getKeystorePassword()
  {
    return this.keyStorePassword;
  }

  public void setKeystorePassword(char[] paramArrayOfChar)
  {
    this.keyStorePassword = paramArrayOfChar;
  }

  public String getAlias()
  {
    return this.alias;
  }

  public void setAlias(String paramString)
  {
    this.alias = paramString;
  }

  public char[] getAliasPassword()
  {
    return this.aliasPassword;
  }

  public void setAliasPassword(char[] paramArrayOfChar)
  {
    this.aliasPassword = paramArrayOfChar;
  }

  public void setKeyFilePassword(char[] paramArrayOfChar)
  {
    this.keyFilePassword = paramArrayOfChar;
  }

  public char[] getKeyFilePassword()
  {
    return this.keyFilePassword;
  }

  public boolean canEncrypt()
  {
    return this.canEncrypt;
  }

  public void setEncrypt(boolean paramBoolean)
  {
    this.canEncrypt = paramBoolean;
  }

  public String getReason()
  {
    return this.reason;
  }

  public void setReason(String paramString)
  {
    this.reason = paramString;
  }

  public void setCertifyMode(int paramInt)
  {
    this.certifyMode = paramInt;
  }

  public int getCertifyMode()
  {
    return this.certifyMode;
  }

  public void setFlatten(boolean paramBoolean)
  {
    this.flatten = paramBoolean;
  }

  public boolean canFlatten()
  {
    return this.flatten;
  }

  public void setEncryptUserPass(char[] paramArrayOfChar)
  {
    this.encryptUserPassword = paramArrayOfChar;
  }

  public char[] getEncryptUserPass()
  {
    return this.encryptUserPassword;
  }

  public void setEncryptOwnerPass(char[] paramArrayOfChar)
  {
    this.encryptOwnerPassword = paramArrayOfChar;
  }

  public char[] getEncryptOwnerPass()
  {
    return this.encryptOwnerPassword;
  }

  public void setLocation(String paramString)
  {
    this.location = paramString;
  }

  public String getLocation()
  {
    return this.location;
  }

  public void setEncryptPermissions(int paramInt)
  {
    this.encryptPermissions = paramInt;
  }

  public int getEncryptPermissions()
  {
    return this.encryptPermissions;
  }

  public String toString()
  {
    if (this.valid)
    {
      str = "Output File: " + this.outputFile.getAbsolutePath() + '\n';
      if (this.signMode)
        str = str + "Keystore: " + this.keyStorePath + '\n' + "Alias: " + this.alias + '\n';
      else
        str = str + ".pfx File:" + this.keyFilePath + '\n';
    }
    else
    {
      return this.invalidMessage;
    }
    String str = str + "Reason: \"" + this.reason + "\"\n" + "Location: " + this.location + '\n';
    if (canEncrypt())
      str = str + "Encrypt PDF\n";
    if (canFlatten())
      str = str + "Flatten PDF\n";
    if (this.certifyMode != -1)
      str = str + "Certify PDF\n";
    return str;
  }

  public boolean validate()
  {
    this.outputFile = new File(this.outputPath);
    if ((this.outputFile.exists()) || (this.outputFile.isDirectory()))
    {
      this.invalidMessage = "Output file already exists.";
      return this.valid = 0;
    }
    if (!this.signMode)
    {
      this.keyFile = new File(this.keyFilePath);
      if ((!this.keyFile.exists()) || (this.keyFile.isDirectory()))
      {
        this.invalidMessage = "Key file not found.";
        return this.valid = 0;
      }
    }
    return this.valid = 1;
  }

  public boolean isVisibleSignature()
  {
    return this.isVisibleSignature;
  }

  public void setVisibleSignature(boolean paramBoolean)
  {
    this.isVisibleSignature = paramBoolean;
  }

  public void setRectangle(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    if (paramFloat1 < paramFloat3)
    {
      this.x1 = paramFloat1;
      this.x2 = paramFloat3;
    }
    else
    {
      this.x2 = paramFloat1;
      this.x1 = paramFloat3;
    }
    if (paramFloat2 < paramFloat4)
    {
      this.y1 = paramFloat2;
      this.y2 = paramFloat4;
    }
    else
    {
      this.y2 = paramFloat2;
      this.y1 = paramFloat4;
    }
  }

  public float[] getRectangle()
  {
    float[] arrayOfFloat = { this.x1, this.y1, this.x2, this.y2 };
    return arrayOfFloat;
  }

  public int getSignPage()
  {
    return this.signaturePage;
  }

  public void setSignPage(int paramInt)
  {
    this.signaturePage = paramInt;
  }

  public void setAppend(boolean paramBoolean)
  {
    this.appendMode = paramBoolean;
  }

  public boolean isAppendMode()
  {
    return this.appendMode;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.objects.SignData
 * JD-Core Version:    0.6.2
 */