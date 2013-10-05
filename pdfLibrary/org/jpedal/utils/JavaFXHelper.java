package org.jpedal.utils;

import com.sun.javafx.runtime.VersionInfo;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;

public class JavaFXHelper
{
  private static boolean javaFXAvailable = true;
  private static boolean javaFXTested = false;

  public static boolean isJavaFXAvailable()
  {
    if (!javaFXTested)
    {
      try
      {
        Class.forName("javafx.scene.image.WritableImage");
        javaFXAvailable = true;
      }
      catch (Exception localException)
      {
        if (tryToLoadFX())
        {
          javaFXAvailable = true;
        }
        else
        {
          if (LogWriter.isOutput())
            LogWriter.writeLog("JavaFX Unavailable. Exception " + localException);
          javaFXAvailable = false;
        }
      }
      catch (Error localError)
      {
        if (tryToLoadFX())
        {
          javaFXAvailable = true;
        }
        else
        {
          if (LogWriter.isOutput())
            LogWriter.writeLog("JavaFX Unavailable. Exception " + localError);
          javaFXAvailable = false;
        }
      }
      javaFXTested = true;
    }
    return javaFXAvailable;
  }

  private static boolean tryToLoadFX()
  {
    try
    {
      File localFile = new File(System.getProperty("java.home") + "/lib/jfxrt.jar");
      if (!localFile.exists())
        throw new Exception("jfxrt.jar not found.");
      URL localURL = localFile.toURI().toURL();
      URLClassLoader localURLClassLoader1 = (URLClassLoader)ClassLoader.getSystemClassLoader();
      URLClassLoader localURLClassLoader2 = URLClassLoader.class;
      try
      {
        Method localMethod = localURLClassLoader2.getDeclaredMethod("addURL", new Class[] { URL.class });
        localMethod.setAccessible(true);
        localMethod.invoke(localURLClassLoader1, new Object[] { localURL });
      }
      catch (Throwable localThrowable)
      {
        throw new IOException("Error, could not add URL to system classloader");
      }
      return true;
    }
    catch (Exception localException)
    {
    }
    return false;
  }

  public static String getVersion()
  {
    return VersionInfo.getRuntimeVersion();
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.utils.JavaFXHelper
 * JD-Core Version:    0.6.2
 */