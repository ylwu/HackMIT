package org.jpedal.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.swing.JOptionPane;

public class BrowserLauncher
{
  private static int jvm;
  private static Object browser;
  private static boolean loadedWithoutErrors = true;
  private static Class mrjFileUtilsClass;
  private static Class mrjOSTypeClass;
  private static Class aeDescClass;
  private static Constructor aeTargetConstructor;
  private static Constructor appleEventConstructor;
  private static Constructor aeDescConstructor;
  private static Method findFolder;
  private static Method getFileCreator;
  private static Method getFileType;
  private static Method openURL;
  private static Method makeOSType;
  private static Method putParameter;
  private static Method sendNoReply;
  private static Object kSystemFolderType;
  private static Integer keyDirectObject;
  private static Integer kAutoGenerateReturnID;
  private static Integer kAnyTransactionID;
  private static final int MRJ_2_0 = 0;
  private static final int MRJ_2_1 = 1;
  private static final int MRJ_3_0 = 3;
  private static final int MRJ_3_1 = 4;
  private static final int WINDOWS_NT = 5;
  private static final int WINDOWS_9x = 6;
  private static final int OTHER = -1;
  private static final String FINDER_TYPE = "FNDR";
  private static final String FINDER_CREATOR = "MACS";
  private static final String GURL_EVENT = "GURL";
  private static final String FIRST_WINDOWS_PARAMETER = "/c";
  private static final String SECOND_WINDOWS_PARAMETER = "start";
  private static final String THIRD_WINDOWS_PARAMETER = "\"\"";
  private static final String NETSCAPE_REMOTE_PARAMETER = "-remote";
  private static final String NETSCAPE_OPEN_PARAMETER_START = "'openURL(";
  private static final String NETSCAPE_OPEN_PARAMETER_END = ")'";
  private static String errorMessage;

  private static boolean loadClasses()
  {
    switch (jvm)
    {
    case 0:
      try
      {
        Class localClass1 = Class.forName("com.apple.MacOS.AETarget");
        Class localClass3 = Class.forName("com.apple.MacOS.OSUtils");
        Class localClass4 = Class.forName("com.apple.MacOS.AppleEvent");
        Class localClass5 = Class.forName("com.apple.MacOS.ae");
        aeDescClass = Class.forName("com.apple.MacOS.AEDesc");
        aeTargetConstructor = localClass1.getDeclaredConstructor(new Class[] { Integer.TYPE });
        appleEventConstructor = localClass4.getDeclaredConstructor(new Class[] { Integer.TYPE, Integer.TYPE, localClass1, Integer.TYPE, Integer.TYPE });
        aeDescConstructor = aeDescClass.getDeclaredConstructor(new Class[] { String.class });
        makeOSType = localClass3.getDeclaredMethod("makeOSType", new Class[] { String.class });
        putParameter = localClass4.getDeclaredMethod("putParameter", new Class[] { Integer.TYPE, aeDescClass });
        sendNoReply = localClass4.getDeclaredMethod("sendNoReply", new Class[0]);
        Field localField2 = localClass5.getDeclaredField("keyDirectObject");
        keyDirectObject = (Integer)localField2.get(null);
        Field localField3 = localClass4.getDeclaredField("kAutoGenerateReturnID");
        kAutoGenerateReturnID = (Integer)localField3.get(null);
        Field localField4 = localClass4.getDeclaredField("kAnyTransactionID");
        kAnyTransactionID = (Integer)localField4.get(null);
      }
      catch (ClassNotFoundException localClassNotFoundException1)
      {
        errorMessage = localClassNotFoundException1.getMessage();
        return false;
      }
      catch (NoSuchMethodException localNoSuchMethodException1)
      {
        errorMessage = localNoSuchMethodException1.getMessage();
        return false;
      }
      catch (NoSuchFieldException localNoSuchFieldException1)
      {
        errorMessage = localNoSuchFieldException1.getMessage();
        return false;
      }
      catch (IllegalAccessException localIllegalAccessException1)
      {
        errorMessage = localIllegalAccessException1.getMessage();
        return false;
      }
    case 1:
      try
      {
        mrjFileUtilsClass = Class.forName("com.apple.mrj.MRJFileUtils");
        mrjOSTypeClass = Class.forName("com.apple.mrj.MRJOSType");
        Field localField1 = mrjFileUtilsClass.getDeclaredField("kSystemFolderType");
        kSystemFolderType = localField1.get(null);
        findFolder = mrjFileUtilsClass.getDeclaredMethod("findFolder", new Class[] { mrjOSTypeClass });
        getFileCreator = mrjFileUtilsClass.getDeclaredMethod("getFileCreator", new Class[] { File.class });
        getFileType = mrjFileUtilsClass.getDeclaredMethod("getFileType", new Class[] { File.class });
      }
      catch (ClassNotFoundException localClassNotFoundException2)
      {
        errorMessage = localClassNotFoundException2.getMessage();
        return false;
      }
      catch (NoSuchFieldException localNoSuchFieldException2)
      {
        errorMessage = localNoSuchFieldException2.getMessage();
        return false;
      }
      catch (NoSuchMethodException localNoSuchMethodException2)
      {
        errorMessage = localNoSuchMethodException2.getMessage();
        return false;
      }
      catch (SecurityException localSecurityException)
      {
        errorMessage = localSecurityException.getMessage();
        return false;
      }
      catch (IllegalAccessException localIllegalAccessException2)
      {
        errorMessage = localIllegalAccessException2.getMessage();
        return false;
      }
    case 3:
      try
      {
        Class localClass2 = Class.forName("com.apple.mrj.jdirect.Linker");
        localClass2.getConstructor(new Class[] { Class.class }).newInstance(new Object[] { BrowserLauncher.class });
      }
      catch (ClassNotFoundException localClassNotFoundException3)
      {
        errorMessage = localClassNotFoundException3.getMessage();
        return false;
      }
      catch (NoSuchMethodException localNoSuchMethodException3)
      {
        errorMessage = localNoSuchMethodException3.getMessage();
        return false;
      }
      catch (InvocationTargetException localInvocationTargetException)
      {
        errorMessage = localInvocationTargetException.getMessage();
        return false;
      }
      catch (InstantiationException localInstantiationException)
      {
        errorMessage = localInstantiationException.getMessage();
        return false;
      }
      catch (IllegalAccessException localIllegalAccessException3)
      {
        errorMessage = localIllegalAccessException3.getMessage();
        return false;
      }
    case 4:
      try
      {
        mrjFileUtilsClass = Class.forName("com.apple.mrj.MRJFileUtils");
        openURL = mrjFileUtilsClass.getDeclaredMethod("openURL", new Class[] { String.class });
      }
      catch (ClassNotFoundException localClassNotFoundException4)
      {
        errorMessage = localClassNotFoundException4.getMessage();
        return false;
      }
      catch (NoSuchMethodException localNoSuchMethodException4)
      {
        errorMessage = localNoSuchMethodException4.getMessage();
        return false;
      }
    case 2:
    }
    return true;
  }

  private static Object locateBrowser()
  {
    if (browser != null)
      return browser;
    Object localObject2;
    switch (jvm)
    {
    case 0:
      try
      {
        Integer localInteger = (Integer)makeOSType.invoke(null, new Object[] { "MACS" });
        Object localObject1 = aeTargetConstructor.newInstance(new Object[] { localInteger });
        localObject2 = (Integer)makeOSType.invoke(null, new Object[] { "GURL" });
        Object localObject3 = appleEventConstructor.newInstance(new Object[] { localObject2, localObject2, localObject1, kAutoGenerateReturnID, kAnyTransactionID });
        return localObject3;
      }
      catch (IllegalAccessException localIllegalAccessException1)
      {
        browser = null;
        errorMessage = localIllegalAccessException1.getMessage();
        return browser;
      }
      catch (InstantiationException localInstantiationException)
      {
        browser = null;
        errorMessage = localInstantiationException.getMessage();
        return browser;
      }
      catch (InvocationTargetException localInvocationTargetException1)
      {
        browser = null;
        errorMessage = localInvocationTargetException1.getMessage();
        return browser;
      }
    case 1:
      File localFile1;
      try
      {
        localFile1 = (File)findFolder.invoke(null, new Object[] { kSystemFolderType });
      }
      catch (IllegalArgumentException localIllegalArgumentException1)
      {
        browser = null;
        errorMessage = localIllegalArgumentException1.getMessage();
        return browser;
      }
      catch (IllegalAccessException localIllegalAccessException2)
      {
        browser = null;
        errorMessage = localIllegalAccessException2.getMessage();
        return browser;
      }
      catch (InvocationTargetException localInvocationTargetException2)
      {
        browser = null;
        errorMessage = localInvocationTargetException2.getTargetException().getClass() + ": " + localInvocationTargetException2.getTargetException().getMessage();
        return browser;
      }
      String[] arrayOfString = localFile1.list();
      for (String str : arrayOfString)
        try
        {
          File localFile2 = new File(localFile1, str);
          if (localFile2.isFile())
          {
            Object localObject4 = getFileType.invoke(null, new Object[] { localFile2 });
            if ("FNDR".equals(localObject4.toString()))
            {
              Object localObject5 = getFileCreator.invoke(null, new Object[] { localFile2 });
              if ("MACS".equals(localObject5.toString()))
              {
                browser = localFile2.toString();
                return browser;
              }
            }
          }
        }
        catch (IllegalArgumentException localIllegalArgumentException2)
        {
          errorMessage = localIllegalArgumentException2.getMessage();
          return null;
        }
        catch (IllegalAccessException localIllegalAccessException3)
        {
          browser = null;
          errorMessage = localIllegalAccessException3.getMessage();
          return browser;
        }
        catch (InvocationTargetException localInvocationTargetException3)
        {
          browser = null;
          errorMessage = localInvocationTargetException3.getTargetException().getClass() + ": " + localInvocationTargetException3.getTargetException().getMessage();
          return browser;
        }
      browser = null;
      break;
    case 3:
    case 4:
      browser = "";
      break;
    case 5:
      browser = "cmd.exe";
      break;
    case 6:
      browser = "command.com";
      break;
    case -1:
    case 2:
    }
    browser = "netscape";
    return browser;
  }

  public static void openURL(String paramString)
    throws IOException
  {
    if (System.getProperty("org.jpedal.noURLaccess") != null)
    {
      localObject1 = System.getProperty("org.jpedal.noURLaccess").toLowerCase().trim();
      if (((String)localObject1).equals("true"))
        return;
      if (!((String)localObject1).equals("false"))
      {
        JOptionPane.showMessageDialog(null, System.getProperty("org.jpedal.noURLaccess"));
        return;
      }
    }
    if (!loadedWithoutErrors)
      throw new IOException("Exception in finding browser: " + errorMessage);
    Object localObject1 = locateBrowser();
    if (localObject1 == null)
      throw new IOException("Unable to locate browser: " + errorMessage);
    Process localProcess;
    switch (jvm)
    {
    case 0:
      try
      {
        Object localObject2 = aeDescConstructor.newInstance(new Object[] { paramString });
        putParameter.invoke(localObject1, new Object[] { keyDirectObject, localObject2 });
        sendNoReply.invoke(localObject1, new Object[0]);
      }
      catch (InvocationTargetException localInvocationTargetException1)
      {
        localInvocationTargetException1 = localInvocationTargetException1;
        throw new IOException("InvocationTargetException while creating AEDesc: " + localInvocationTargetException1.getMessage());
      }
      catch (IllegalAccessException localIllegalAccessException1)
      {
        localIllegalAccessException1 = localIllegalAccessException1;
        throw new IOException("IllegalAccessException while building AppleEvent: " + localIllegalAccessException1.getMessage());
      }
      catch (InstantiationException localInstantiationException)
      {
        localInstantiationException = localInstantiationException;
        throw new IOException("InstantiationException while creating AEDesc: " + localInstantiationException.getMessage());
      }
      finally
      {
      }
      break;
    case 1:
      Runtime.getRuntime().exec(new String[] { (String)localObject1, paramString });
      break;
    case 3:
      int[] arrayOfInt1 = new int[1];
      int i = ICStart(arrayOfInt1, 0);
      if (i == 0)
      {
        int[] arrayOfInt2 = { 0 };
        byte[] arrayOfByte = StringUtils.toBytes(paramString);
        int[] arrayOfInt3 = { arrayOfByte.length };
        i = ICLaunchURL(arrayOfInt1[0], new byte[] { 0 }, arrayOfByte, arrayOfByte.length, arrayOfInt2, arrayOfInt3);
        if (i == 0)
          ICStop(arrayOfInt1);
        else
          throw new IOException("Unable to launch URL: " + i);
      }
      else
      {
        throw new IOException("Unable to create an Internet Config instance: " + i);
      }
      break;
    case 4:
      try
      {
        openURL.invoke(null, new Object[] { paramString });
      }
      catch (InvocationTargetException localInvocationTargetException2)
      {
        throw new IOException("InvocationTargetException while calling openURL: " + localInvocationTargetException2.getMessage());
      }
      catch (IllegalAccessException localIllegalAccessException2)
      {
        throw new IOException("IllegalAccessException while calling openURL: " + localIllegalAccessException2.getMessage());
      }
    case 5:
    case 6:
      localProcess = Runtime.getRuntime().exec(new String[] { (String)localObject1, "/c", "start", "\"\"", '"' + paramString + '"' });
      try
      {
        localProcess.waitFor();
        localProcess.exitValue();
      }
      catch (InterruptedException localInterruptedException1)
      {
        throw new IOException("InterruptedException while launching browser: " + localInterruptedException1.getMessage());
      }
    case -1:
      localProcess = Runtime.getRuntime().exec(new String[] { (String)localObject1, "-remote", "'openURL(" + paramString + ")'" });
      try
      {
        int j = localProcess.waitFor();
        if (j != 0)
          Runtime.getRuntime().exec(new String[] { (String)localObject1, paramString });
      }
      catch (InterruptedException localInterruptedException2)
      {
        throw new IOException("InterruptedException while launching browser: " + localInterruptedException2.getMessage());
      }
    case 2:
    default:
      Runtime.getRuntime().exec(new String[] { (String)localObject1, paramString });
    }
  }

  private static native int ICStart(int[] paramArrayOfInt, int paramInt);

  private static native int ICStop(int[] paramArrayOfInt);

  private static native int ICLaunchURL(int paramInt1, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2);

  static
  {
    String str1 = System.getProperty("os.name");
    if (str1.startsWith("Mac OS"))
    {
      String str2 = System.getProperty("mrj.version");
      String str3 = str2.substring(0, 3);
      try
      {
        double d = Double.valueOf(str3).doubleValue();
        if (d == 2.0D)
        {
          jvm = 0;
        }
        else if ((d >= 2.1D) && (d < 3.0D))
        {
          jvm = 1;
        }
        else if (d == 3.0D)
        {
          jvm = 3;
        }
        else if (d >= 3.1D)
        {
          jvm = 4;
        }
        else
        {
          loadedWithoutErrors = false;
          errorMessage = "Unsupported MRJ version: " + d;
        }
      }
      catch (NumberFormatException localNumberFormatException)
      {
        loadedWithoutErrors = false;
        errorMessage = "Invalid MRJ version: " + str2;
      }
    }
    else if (str1.startsWith("Windows"))
    {
      if (str1.indexOf(57) != -1)
        jvm = 6;
      else
        jvm = 5;
    }
    else
    {
      jvm = -1;
    }
    if (loadedWithoutErrors)
      loadedWithoutErrors = loadClasses();
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.utils.BrowserLauncher
 * JD-Core Version:    0.6.2
 */