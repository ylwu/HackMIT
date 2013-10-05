package org.jpedal.fonts;

import java.awt.GraphicsEnvironment;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.jpedal.exception.PdfFontException;
import org.jpedal.parser.DecoderOptions;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.Strip;

public class FontMappings
{
  public static boolean fontsInitialised = false;
  public static String defaultFont = null;
  public static boolean enforceFontSubstitution = false;
  public static Map fontSubstitutionTable = null;
  public static Map fontPropertiesTable = null;
  public static Map fontPossDuplicates = null;
  public static Map fontSubstitutionFontID = null;
  public static Map fontSubstitutionLocation = new HashMap();
  public static Map fontSubstitutionAliasTable = new HashMap();
  private static boolean fontsSet = false;
  private static final String separator = System.getProperty("file.separator");
  public static String[] defaultFontDirs = { "C:/windows/fonts/", "C:/winNT/fonts/", "/Library/Fonts/", "/usr/share/fonts/truetype/msttcorefonts/" };
  private static int fontSubstitutionMode = 1;

  public static void initFonts()
  {
    Object localObject;
    try
    {
      String str1 = System.getProperty("org.jpedal.fontmaps");
      if (str1 != null)
      {
        localObject = new StringTokenizer(str1, ",");
        while (((StringTokenizer)localObject).hasMoreTokens())
        {
          String str3 = ((StringTokenizer)localObject).nextToken();
          StringTokenizer localStringTokenizer = new StringTokenizer(str3, "=:");
          int i = localStringTokenizer.countTokens() - 1;
          String[] arrayOfString = new String[i];
          String str4 = localStringTokenizer.nextToken();
          for (int j = 0; j < i; j++)
            arrayOfString[j] = localStringTokenizer.nextToken();
          setSubstitutedFontAliases(str4, arrayOfString);
        }
      }
    }
    catch (Exception localException1)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Unable to read org.jpedal.fontmaps " + localException1.getMessage());
    }
    try
    {
      String str2 = System.getProperty("org.jpedal.fontdirs");
      localObject = null;
      if (str2 != null)
        localObject = addFonts(str2, (String)localObject);
      if ((localObject != null) && (LogWriter.isOutput()))
        LogWriter.writeLog("Could not find " + (String)localObject);
    }
    catch (Exception localException2)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Unable to read FontDirs " + localException2.getMessage());
    }
  }

  public static void setFontSubstitutionMode(int paramInt)
  {
    fontSubstitutionMode = paramInt;
  }

  public static int getFontSubstitutionMode()
  {
    return fontSubstitutionMode;
  }

  public static void setSubstitutedFontAliases(String paramString, String[] paramArrayOfString)
  {
    if (paramArrayOfString != null)
    {
      String str1 = paramString.toLowerCase();
      for (String str3 : paramArrayOfString)
      {
        String str2 = str3.toLowerCase();
        if (!str2.equals(str1))
          fontSubstitutionAliasTable.put(str2, str1);
      }
    }
  }

  public static String addFonts(String paramString1, String paramString2)
  {
    StringTokenizer localStringTokenizer = new StringTokenizer(paramString1, ",");
    while (localStringTokenizer.hasMoreTokens())
    {
      String str = localStringTokenizer.nextToken();
      if (((!str.endsWith("/") ? 1 : 0) & (!str.endsWith("\\") ? 1 : 0)) != 0)
        str = str + separator;
      addTTDir(str, paramString2);
    }
    return paramString2;
  }

  public static void dispose()
  {
    fontSubstitutionTable = null;
    fontPropertiesTable = null;
    fontPossDuplicates = null;
    fontSubstitutionFontID = null;
    fontSubstitutionLocation = null;
    fontSubstitutionAliasTable = null;
  }

  public static String addTTDir(String paramString1, String paramString2)
  {
    if (fontSubstitutionTable == null)
    {
      fontSubstitutionTable = new HashMap();
      fontSubstitutionFontID = new HashMap();
      fontPossDuplicates = new HashMap();
      fontPropertiesTable = new HashMap();
    }
    File localFile = new File(paramString1);
    if ((localFile.exists()) && (localFile.isDirectory()))
    {
      String[] arrayOfString1 = localFile.list();
      if (arrayOfString1 != null)
        for (String str : arrayOfString1)
          addFontFile(str, paramString1);
    }
    else if (paramString2 == null)
    {
      paramString2 = paramString1;
    }
    else
    {
      paramString2 = paramString2 + ',' + paramString1;
    }
    return paramString2;
  }

  public static void setFontReplacements()
  {
    String[] arrayOfString1 = { "acarialunicodems__cn" };
    setSubstitutedFontAliases("adobeheitistd-regular", arrayOfString1);
    if (DecoderOptions.isRunningOnMac)
    {
      setSubstitutedFontAliases("Courier italic", new String[] { "Courier-Oblique" });
      setSubstitutedFontAliases("Courier bold", new String[] { "Courier-Bold" });
      setSubstitutedFontAliases("Courier bold italic", new String[] { "Courier-BoldOblique" });
      setSubstitutedFontAliases("Courier new italic", new String[] { "CourierNew,italic", "CourierStd-Oblique", "CourierNewPS-ItalicMT" });
      setSubstitutedFontAliases("Courier new bold", new String[] { "CourierNew,Bold", "Courier-Bold", "CourierStd-Bold" });
      setSubstitutedFontAliases("Courier new bold italic", new String[] { "CourierNew-BoldOblique", "CourierStd-BoldOblique" });
      setSubstitutedFontAliases("Courier new", new String[] { "CourierNew", "Courier", "CourierStd", "CourierNewPSMT" });
      setSubstitutedFontAliases("arial", new String[] { "Helvetica", "arialmt" });
      setSubstitutedFontAliases("arial italic", new String[] { "arial-italic", "arial-italicmt", "Helvetica-Oblique", "Arial,Italic" });
      setSubstitutedFontAliases("arial bold", new String[] { "arial-boldmt,bold", "arial-boldmt", "Helvetica-Bold", "Arial,bold" });
      setSubstitutedFontAliases("arial bold italic", new String[] { "Arial-BoldItalicMT", "Helvetica-BoldOblique" });
      setSubstitutedFontAliases("arial Narrow", new String[] { "ArialNarrow" });
      setSubstitutedFontAliases("arial Narrow italic", new String[] { "ArialNarrow-italic" });
      setSubstitutedFontAliases("arial Narrow bold", new String[] { "ArialNarrow-bold", "ArialNarrow,Bold" });
      setSubstitutedFontAliases("arial Narrow bold italic", new String[] { "ArialNarrow-bolditalic" });
      setSubstitutedFontAliases("times new roman bold", new String[] { "Times-Bold", "TimesNewRoman,Bold", "TimesNewRomanPS-BoldMT" });
      setSubstitutedFontAliases("times new roman bold italic", new String[] { "Times-BoldItalic", "TimesNewRoman,BoldItalic", "TimesNewRomanPS-BoldItalicMT" });
      setSubstitutedFontAliases("times new roman italic", new String[] { "Times-Italic", "TimesNewRoman,Italic", "TimesNewRomanPS-ItalicMT" });
      setSubstitutedFontAliases("times new roman", new String[] { "Times-Roman", "TimesNewRoman", "Times", "TimesNewRomanPSMT" });
      setSubstitutedFontAliases("wingdings", new String[] { "ZapfDingbats", "ZaDb" });
    }
    else
    {
      setSubstitutedFontAliases("Couri", new String[] { "Courier-Oblique", "CourierNew,italic", "CourierStd-Oblique", "CourierNewPS-ItalicMT" });
      setSubstitutedFontAliases("Courbd", new String[] { "Courier-Bold", "CourierNew,Bold", "CourierStd-Bold" });
      setSubstitutedFontAliases("Courbi", new String[] { "Courier-BoldOblique", "CourierNew-BoldOblique", "CourierStd-BoldOblique", "CourierNewPS-ItalicMT" });
      setSubstitutedFontAliases("Cour", new String[] { "CourierNew", "Courier", "CourierStd", "CourierNewPSMT", "CourierNewPSMT" });
      setSubstitutedFontAliases("arial", new String[] { "Helvetica", "arialmt" });
      setSubstitutedFontAliases("ariali", new String[] { "arial-italic", "arial-italicmt", "Helvetica-Oblique", "Arial,Italic" });
      setSubstitutedFontAliases("arialbd", new String[] { "arial-boldmt,bold", "arial-boldmt", "Helvetica-Bold", "Arial,bold", "arial bold" });
      setSubstitutedFontAliases("arialbdi", new String[] { "Arial-BoldItalicMT", "Helvetica-BoldOblique" });
      setSubstitutedFontAliases("arialn", new String[] { "ArialNarrow" });
      setSubstitutedFontAliases("arialni", new String[] { "ArialNarrow-italic" });
      setSubstitutedFontAliases("arialnb", new String[] { "ArialNarrow-bold", "ArialNarrow,Bold" });
      setSubstitutedFontAliases("arialnbi", new String[] { "ArialNarrow-bolditalic" });
      setSubstitutedFontAliases("timesbd", new String[] { "Times-Bold", "TimesNewRoman,Bold", "TimesNewRomanPS-BoldMT" });
      setSubstitutedFontAliases("timesi", new String[] { "Times-BoldItalic", "TimesNewRoman,BoldItalic" });
      setSubstitutedFontAliases("timesbi", new String[] { "Times-Italic", "TimesNewRoman,Italic" });
      setSubstitutedFontAliases("times", new String[] { "Times-Roman", "TimesNewRoman", "Times", "TimesNewRomanPSMT" });
      setSubstitutedFontAliases("wingdings", new String[] { "ZapfDingbats", "ZaDb" });
    }
    setSubstitutedFontAliases("AdobeSongStd-Light", new String[] { "STSong-Light" });
    if (!fontsSet)
    {
      fontsSet = true;
      setFontDirs(defaultFontDirs);
      if (DecoderOptions.isRunningOnWindows)
      {
        File localFile1 = new File("C:\\Program Files\\Adobe\\");
        if (localFile1.exists())
        {
          String[] arrayOfString2 = localFile1.list();
          for (String str1 : arrayOfString2)
          {
            String str2 = "C:\\Program Files\\Adobe\\" + str1 + "\\Resource\\CIDFont";
            File localFile2 = new File(str2);
            if (localFile2.exists())
              addTTDir(str2, "");
          }
        }
      }
    }
  }

  public static String setFontDirs(String[] paramArrayOfString)
  {
    String str1 = null;
    if (fontSubstitutionTable == null)
    {
      fontSubstitutionTable = new HashMap();
      fontSubstitutionFontID = new HashMap();
      fontPossDuplicates = new HashMap();
      fontPropertiesTable = new HashMap();
    }
    try
    {
      if (paramArrayOfString == null)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog("Null font parameter passed");
        fontSubstitutionAliasTable.clear();
        fontSubstitutionLocation.clear();
        fontSubstitutionTable.clear();
        fontSubstitutionFontID.clear();
        fontPossDuplicates.clear();
        fontPropertiesTable.clear();
        fontsSet = false;
      }
      else
      {
        for (String str2 : paramArrayOfString)
        {
          String str3 = str2;
          if ((!str3.endsWith("/")) && (!str3.endsWith("\\")))
            str3 = str3 + separator;
          str1 = addTTDir(str3, str1);
        }
      }
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Unable to run setFontDirs " + localException.getMessage());
    }
    return str1;
  }

  public static boolean addSubstituteFonts(String paramString, boolean paramBoolean)
  {
    boolean bool = false;
    InputStream localInputStream1 = null;
    InputStream localInputStream2 = null;
    try
    {
      String[] arrayOfString1 = { "tt", "t1c", "t1" };
      String[] arrayOfString2 = { "/TrueType", "/Type1C", "/Type1" };
      enforceFontSubstitution = paramBoolean;
      ClassLoader localClassLoader = FontMappings.class.getClass().getClassLoader();
      localInputStream2 = localClassLoader.getResourceAsStream(paramString);
      if (LogWriter.isOutput())
        LogWriter.writeLog("Looking for root " + paramString);
      if (localInputStream1 != null)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog("Adding fonts fonts found in  tt,t1c,t1 sub-directories of " + paramString);
        bool = true;
        for (int i = 0; i < arrayOfString1.length; i++)
        {
          if (!paramString.endsWith("/"))
            paramString = paramString + '/';
          String str1 = paramString + arrayOfString1[i] + '/';
          localInputStream1 = localClassLoader.getResourceAsStream(str1);
          if (localInputStream1 != null)
          {
            System.out.println("Found  " + str1 + ' ' + localInputStream1);
            try
            {
              ArrayList localArrayList;
              if ((localInputStream1 instanceof ByteArrayInputStream))
                localArrayList = readIndirectValues(localInputStream1);
              else
                localArrayList = getDirectoryMatches(str1);
              Iterator localIterator = localArrayList.iterator();
              while (localIterator.hasNext())
              {
                Object localObject1 = localIterator.next();
                String str2 = (String)localObject1;
                if (str2 == null)
                  break;
                int j = str2.indexOf(46);
                String str3;
                if (j == -1)
                  str3 = str2.toLowerCase();
                else
                  str3 = str2.substring(0, j).toLowerCase();
                fontSubstitutionTable.put(str3, arrayOfString2[i]);
                fontSubstitutionLocation.put(str3, str1 + str2);
              }
            }
            catch (Exception localException2)
            {
              if (LogWriter.isOutput())
                LogWriter.writeLog("Exception " + localException2 + " reading substitute fonts");
              System.out.println("Exception " + localException2 + " reading substitute fonts");
            }
            finally
            {
              if (localInputStream1 != null)
                try
                {
                  localInputStream1.close();
                }
                catch (IOException localIOException7)
                {
                  if (LogWriter.isOutput())
                    LogWriter.writeLog("Exception: " + localIOException7.getMessage());
                }
            }
          }
        }
      }
      else if (LogWriter.isOutput())
      {
        LogWriter.writeLog("No fonts found at " + paramString);
      }
    }
    catch (Exception localException1)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception adding substitute fonts " + localException1.getMessage());
    }
    finally
    {
      if (localInputStream1 != null)
        try
        {
          localInputStream1.close();
        }
        catch (IOException localIOException8)
        {
          if (LogWriter.isOutput())
            LogWriter.writeLog("Exception: " + localIOException8.getMessage());
        }
      if (localInputStream2 != null)
        try
        {
          localInputStream2.close();
        }
        catch (IOException localIOException9)
        {
          if (LogWriter.isOutput())
            LogWriter.writeLog("Exception: " + localIOException9.getMessage());
        }
    }
    return bool;
  }

  public static void addFontFile(String paramString1, String paramString2)
  {
    if (fontSubstitutionTable == null)
    {
      fontSubstitutionTable = new HashMap();
      fontSubstitutionFontID = new HashMap();
      fontPossDuplicates = new HashMap();
      fontPropertiesTable = new HashMap();
    }
    if ((paramString2 != null) && (!paramString2.endsWith("/")) && (!paramString2.endsWith("\\")))
      paramString2 = paramString2 + separator;
    String str1 = paramString1.toLowerCase();
    int i = StandardFonts.getFontType(str1);
    FileInputStream localFileInputStream = null;
    if ((i != 8) && (new File(paramString2 + paramString1).exists()))
    {
      int j = 0;
      try
      {
        localFileInputStream = new FileInputStream(paramString2 + paramString1);
      }
      catch (Exception localException1)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog("Exception: " + localException1.getMessage());
        j = 1;
      }
      catch (Error localError)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog("Error: " + localError.getMessage());
        localError.printStackTrace(System.out);
        j = 1;
      }
      if (j == 0)
      {
        int k = paramString1.indexOf(46);
        String str2;
        if (k == -1)
          str2 = paramString1.toLowerCase();
        else
          str2 = paramString1.substring(0, k).toLowerCase();
        if ((fontSubstitutionMode == 1) || (i == 6))
        {
          if (i == 1228944677)
            fontSubstitutionTable.put(str2, "/Type1");
          else
            fontSubstitutionTable.put(str2, "/TrueType");
          fontSubstitutionLocation.put(str2, paramString2 + paramString1);
          fontPropertiesTable.put(str2 + "_type", Integer.valueOf(i));
          fontPropertiesTable.put(str2 + "_path", paramString2 + paramString1);
        }
        else
        {
          String[] arrayOfString1;
          if ((i == 7) || (i == 1217103210))
          {
            if (fontSubstitutionMode == 5)
            {
              arrayOfString1 = null;
              try
              {
                arrayOfString1 = StandardFonts.readNamesFromFont(i, paramString2 + paramString1, 2);
              }
              catch (Exception localException2)
              {
                if (LogWriter.isOutput())
                  LogWriter.writeLog("Exception: " + localException2.getMessage());
              }
              String[] arrayOfString2 = null;
              try
              {
                arrayOfString2 = StandardFonts.readNamesFromFont(i, paramString2 + paramString1, 3);
              }
              catch (Exception localException5)
              {
                if (LogWriter.isOutput())
                  LogWriter.writeLog("Exception: " + localException5.getMessage());
              }
              int i1 = 0;
              if (arrayOfString1 != null)
                i1 = arrayOfString1.length;
              for (int i2 = 0; i2 < i1; i2++)
              {
                if (arrayOfString1[i2] == null)
                  arrayOfString1[i2] = Strip.stripAllSpaces(str2);
                if (arrayOfString2[i2] == null)
                  arrayOfString2[i2] = Strip.stripAllSpaces(str2);
                Object localObject1 = fontSubstitutionTable.get(arrayOfString1[i2]);
                Object localObject2 = fontPossDuplicates.get(arrayOfString1[i2]);
                if ((localObject1 == null) && (localObject2 == null))
                {
                  fontSubstitutionTable.put(arrayOfString1[i2], "/TrueType");
                  fontSubstitutionLocation.put(arrayOfString1[i2], paramString2 + paramString1);
                  fontSubstitutionFontID.put(arrayOfString1[i2], Integer.valueOf(i2));
                  fontPossDuplicates.put(arrayOfString1[i2], arrayOfString2[i2]);
                }
                else if (!arrayOfString2[i2].equals(arrayOfString1[i2]))
                {
                  fontSubstitutionTable.put(arrayOfString1[i2], "/TrueType");
                  fontSubstitutionLocation.put(arrayOfString1[i2], paramString2 + paramString1);
                  fontSubstitutionFontID.put(arrayOfString1[i2], Integer.valueOf(i2));
                  fontPropertiesTable.put(arrayOfString1[i2] + "_type", Integer.valueOf(i));
                  fontPropertiesTable.put(arrayOfString1[i2] + "_path", paramString2 + paramString1);
                  if (!localObject2.equals("DONE"))
                  {
                    fontPossDuplicates.put(arrayOfString1[i2], "DONE");
                    fontSubstitutionTable.remove(arrayOfString1[i2]);
                    fontSubstitutionTable.put(arrayOfString2[i2], "/TrueType");
                    String str3 = (String)fontSubstitutionLocation.get(arrayOfString1[i2]);
                    fontSubstitutionLocation.remove(arrayOfString1[i2]);
                    fontSubstitutionLocation.put(arrayOfString2[i2], str3);
                    fontSubstitutionFontID.remove(arrayOfString1[i2]);
                    fontSubstitutionFontID.put(arrayOfString2[i2], Integer.valueOf(i2));
                    fontPropertiesTable.remove(arrayOfString2[i2] + "_path");
                    fontPropertiesTable.remove(arrayOfString2[i2] + "_type");
                    fontPropertiesTable.put(arrayOfString2[i2] + "_type", Integer.valueOf(i));
                    fontPropertiesTable.put(arrayOfString2[i2] + "_path", paramString2 + paramString1);
                  }
                }
              }
            }
            else
            {
              arrayOfString1 = null;
              try
              {
                arrayOfString1 = StandardFonts.readNamesFromFont(i, paramString2 + paramString1, fontSubstitutionMode);
              }
              catch (Exception localException3)
              {
                if (LogWriter.isOutput())
                  LogWriter.writeLog("Exception: " + localException3.getMessage());
              }
              if (arrayOfString1 != null)
                for (int m = 0; m < arrayOfString1.length; m++)
                {
                  if (arrayOfString1[m] == null)
                    arrayOfString1[m] = Strip.stripAllSpaces(str2);
                  fontSubstitutionTable.put(arrayOfString1[m], "/TrueType");
                  fontSubstitutionLocation.put(arrayOfString1[m], paramString2 + paramString1);
                  fontSubstitutionFontID.put(arrayOfString1[m], Integer.valueOf(m));
                  fontPropertiesTable.put(arrayOfString1[m] + "_type", Integer.valueOf(i));
                  fontPropertiesTable.put(arrayOfString1[m] + "_path", paramString2 + paramString1);
                }
            }
          }
          else if (i == 1228944677)
          {
            arrayOfString1 = null;
            try
            {
              arrayOfString1 = StandardFonts.readNamesFromFont(i, paramString2 + paramString1, fontSubstitutionMode);
            }
            catch (Exception localException4)
            {
              if (LogWriter.isOutput())
                LogWriter.writeLog("Exception: " + localException4.getMessage());
            }
            if (arrayOfString1 != null)
              for (int n = 0; n < arrayOfString1.length; n++)
              {
                if (arrayOfString1[n] == null)
                  arrayOfString1[n] = Strip.stripAllSpaces(str2);
                fontSubstitutionTable.put(arrayOfString1[n], "/Type1");
                fontSubstitutionLocation.put(arrayOfString1[n], paramString2 + paramString1);
                fontSubstitutionFontID.put(arrayOfString1[n], Integer.valueOf(n));
                fontPropertiesTable.put(arrayOfString1[n] + "_type", Integer.valueOf(i));
                fontPropertiesTable.put(arrayOfString1[n] + "_path", paramString2 + paramString1);
              }
          }
        }
      }
      else if (LogWriter.isOutput())
      {
        LogWriter.writeLog("No fonts found at " + paramString2);
      }
    }
    if (localFileInputStream != null)
      try
      {
        localFileInputStream.close();
      }
      catch (IOException localIOException)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog("Exception: " + localIOException.getMessage());
      }
  }

  private static ArrayList getDirectoryMatches(String paramString)
    throws IOException
  {
    paramString.replaceAll("\\.", "/");
    URL localURL1 = Thread.currentThread().getContextClassLoader().getResource(paramString);
    ArrayList localArrayList = new ArrayList(0);
    String str1 = localURL1.toString();
    System.out.println("scanning " + str1);
    if ((str1.startsWith("jar:")) && (str1.endsWith(paramString)))
    {
      int i = str1.lastIndexOf(paramString);
      str1 = str1.substring(0, i);
      System.out.println("entry= " + str1);
      URL localURL2 = new URL(str1);
      JarURLConnection localJarURLConnection = (JarURLConnection)localURL2.openConnection();
      JarFile localJarFile = localJarURLConnection.getJarFile();
      Enumeration localEnumeration = localJarFile.entries();
      while (localEnumeration.hasMoreElements())
      {
        JarEntry localJarEntry = (JarEntry)localEnumeration.nextElement();
        if ((!localJarEntry.isDirectory() & localJarEntry.getName().startsWith(paramString)))
        {
          String str2 = localJarEntry.getName();
          int j = str2.lastIndexOf(47);
          str2 = str2.substring(j + 1);
          localArrayList.add(str2);
        }
      }
    }
    else if (LogWriter.isOutput())
    {
      LogWriter.writeLog("Path: " + str1);
    }
    return localArrayList;
  }

  private static ArrayList readIndirectValues(InputStream paramInputStream)
    throws IOException
  {
    BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(paramInputStream));
    ArrayList localArrayList = new ArrayList(0);
    while (true)
    {
      String str = localBufferedReader.readLine();
      if (str == null)
        break;
      localArrayList.add(str);
    }
    localBufferedReader.close();
    return localArrayList;
  }

  public static void setDefaultDisplayFont(String paramString)
    throws PdfFontException
  {
    int i = 0;
    String[] arrayOfString = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
    int j = arrayOfString.length;
    for (int k = 0; k < j; k++)
      if (arrayOfString[k].toLowerCase().equals(paramString.toLowerCase()))
      {
        i = 1;
        defaultFont = arrayOfString[k];
        k = j;
      }
    if (i == 0)
      throw new PdfFontException("Font " + paramString + " is not available.");
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.fonts.FontMappings
 * JD-Core Version:    0.6.2
 */