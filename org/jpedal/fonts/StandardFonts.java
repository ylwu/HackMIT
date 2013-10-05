package org.jpedal.fonts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.StringTokenizer;
import org.jpedal.fonts.glyph.T1Glyphs;
import org.jpedal.fonts.objects.FontData;
import org.jpedal.fonts.tt.TTGlyphs;
import org.jpedal.utils.LogWriter;

public class StandardFonts
{
  private static Map unicode_name_mapping_table = new HashMap();
  private static String[][] unicode_char_decoding_table = new String[7][335];
  public static final int PDF = 6;
  public static final int ZAPF = 5;
  public static final int SYMBOL = 4;
  public static final int MACEXPERT = 3;
  public static final int WIN = 2;
  public static final int STD = 1;
  public static final int MAC = 0;
  public static final int TYPE0 = 1228944676;
  public static final int TYPE1 = 1228944677;
  public static final int TRUETYPE = 1217103210;
  public static final int TYPE3 = 1228944679;
  public static final int CIDTYPE0 = -1684566726;
  public static final int CIDTYPE2 = -1684566724;
  public static final int OPENTYPE = 6;
  public static final int TRUETYPE_COLLECTION = 7;
  public static final int FONT_UNSUPPORTED = 8;
  private static String ellipsis = String.valueOf((char)Integer.parseInt("2026", 16));
  private static final String enc = "Cp1252";
  private static Map uniqueValues = null;
  private static Map[] glyphToChar = new HashMap[7];
  private static String[] MAC_char_encoding_table;
  private static String[] WIN_char_encoding_table;
  private static String[] STD_char_encoding_table;
  private static String[] PDF_char_encoding_table;
  private static String[] ZAPF_char_encoding_table;
  private static String[] SYMBOL_char_encoding_table;
  private static String[] MACEXPERT_char_encoding_table;
  private static Map<String, String> unicodeToName = new HashMap();
  private static ClassLoader loader = StandardFonts.class.getClassLoader();
  private static Map standardFileList = new HashMap();
  private static Map standardFontLoaded = new HashMap();
  private static Map widthTableStandard = new Hashtable();
  private static Map adobeCMAPS = null;
  private static String[] CIDFonts = { "83pv-RKSJ-H", "90msp-RKSJ-H", "90msp-RKSJ-V", "90ms-RKSJ-H", "90ms-RKSJ-UCS2", "90ms-RKSJ-V", "90pv-RKSJ-H", "90pv-RKSJ-UCS2", "90pv-RKSJ-UCS2C", "Add-RKSJ-H", "Add-RKSJ-V", "Adobe-CNS1-3", "Adobe-CNS1-UCS2", "Adobe-GB1-4", "Adobe-GB1-UCS2", "Adobe-Japan1-4", "Adobe-Japan1-UCS2", "Adobe-Korea1-2", "Adobe-Korea1-UCS2", "B5pc-H", "B5pc-UCS2", "B5pc-UCS2C", "B5pc-V", "CNS-EUC-H", "CNS-EUC-V", "ETen-B5-H", "ETen-B5-UCS2", "ETen-B5-V", "euc-h", "euc-v", "Ext-RKSJ-H", "Ext-RKSJ-V", "gb-euc-h", "gb-euc-v", "gbk2k-h", "gbk2k-v", "GBK-EUC-H", "GBK-EUC-UCS2", "GBK-EUC-V", "GBKp-EUC-H", "GBKp-EUC-V", "GBpc-EUC-H", "GBpc-EUC-UCS2", "GBpc-EUC-UCS2C", "GBpc-EUC-V", "GBT-EUC-H", "GBT-EUC-V", "h", "HKscs-B5-H", "HKscs-B5-V", "KSC-EUC-H", "KSC-EUC-V", "KSCms-UHC-H", "KSCms-UHC-HW-H", "KSCms-UHC-HW-V", "KSCms-UHC-UCS2", "KSCms-UHC-V", "KSCpc-EUC-H", "KSCpc-EUC-UCS2", "KSCpc-EUC-UCS2C", "UniCNS-UCS2-H", "UniCNS-UCS2-V", "UniGB-UCS2-H", "UniGB-UCS2-V", "UniJIS-UCS2-H", "UniJIS-UCS2-HW-H", "UniJIS-UCS2-HW-V", "UniJIS-UCS2-V", "UniKS-UCS2-H", "UniKS-UCS2-V", "v" };
  protected static Map javaFontList = new HashMap();
  protected static String[] javaFonts = { "Courier", "Courier-Bold", "Courier", "Courier-Bold", "Arial", "Arial-Bold", "Arial", "Arial-Italic", "Symbol", "Times New Roman", "Times New Roman", "Times New Roman", "Times New Roman", "Wingdings" };
  protected static String[] files_names = { "Courier", "Courier-Bold", "Courier-BoldOblique", "Courier-Oblique", "Helvetica", "Helvetica-Bold", "Helvetica-BoldOblique", "Helvetica-Oblique", "Symbol", "Times-Bold", "Times-BoldItalic", "Times-Italic", "Times-Roman", "ZapfDingbats" };
  protected static String[] files_names_bis = { "CourierNew", "CourierNew,Bold", "CourierNew,BoldItalic", "CourierNew,Italic", "Arial", "Arial,Bold", "Arial,BoldItalic", "Arial,Italic", "Symbol", "TimesNewRoman,Bold", "TimesNewRoman,BoldItalic", "TimesNewRoman,Italic", "TimesNewRoman", "ZapfDingbats" };
  private static HashMap adobeMap = null;
  private static Map fontBounds = new Hashtable();
  public static boolean usesGlyphlist = false;
  private static HashMap<String, HashMap<Integer, Integer>> mappedCharacters = new HashMap();
  private static HashMap<String, ArrayList<Integer>> takenChars = new HashMap();
  private static final int MAX_CHAR_CODE = 55296;
  public static String[] CMAP = null;

  public static void dispose()
  {
    unicode_name_mapping_table = null;
    unicode_char_decoding_table = (String[][])null;
    uniqueValues = null;
    glyphToChar = null;
    MAC_char_encoding_table = null;
    WIN_char_encoding_table = null;
    STD_char_encoding_table = null;
    PDF_char_encoding_table = null;
    ZAPF_char_encoding_table = null;
    SYMBOL_char_encoding_table = null;
    MACEXPERT_char_encoding_table = null;
    loader = null;
    standardFileList = null;
    standardFontLoaded = null;
    widthTableStandard = null;
    javaFontList = null;
    javaFonts = null;
    files_names = null;
    files_names_bis = null;
    adobeMap = null;
    fontBounds = null;
  }

  public static int getFontType(String paramString)
  {
    int i = 8;
    if (paramString.endsWith(".ttf"))
      i = 1217103210;
    else if (paramString.endsWith(".otf"))
      i = 6;
    else if (paramString.endsWith(".ttc"))
      i = 7;
    else if (paramString.endsWith(".pfb"))
      i = 1228944677;
    return i;
  }

  public static float[] getFontBounds(String paramString)
  {
    return (float[])fontBounds.get(paramString);
  }

  public static String getUnicodeName(String paramString)
  {
    return (String)unicode_name_mapping_table.get(paramString);
  }

  public static String getUnicodeChar(int paramInt1, int paramInt2)
  {
    return unicode_char_decoding_table[paramInt1][paramInt2];
  }

  public static Map getUniqueMappings()
  {
    if (uniqueValues == null)
    {
      uniqueValues = new HashMap();
      for (int i = 0; i < 256; i++)
      {
        if ((WIN_char_encoding_table[i] == null) && (MAC_char_encoding_table[i] != null))
          uniqueValues.put(Integer.valueOf(i), Integer.valueOf(1));
        if ((WIN_char_encoding_table[i] != null) && (MAC_char_encoding_table[i] == null))
          uniqueValues.put(Integer.valueOf(i), Integer.valueOf(-1));
      }
    }
    return uniqueValues;
  }

  public static Float getStandardWidth(String paramString1, String paramString2)
  {
    Object localObject = widthTableStandard.get(new StringBuilder().append(paramString1).append(paramString2).toString());
    if (localObject == null)
    {
      String str = paramString1;
      int i = str.indexOf(44);
      if (i != -1)
      {
        str = str.substring(0, i);
        localObject = widthTableStandard.get(new StringBuilder().append(str).append(paramString2).toString());
      }
    }
    return (Float)localObject;
  }

  private static final void readStandardMappingTable(int paramInt, String paramString)
  {
    BufferedReader localBufferedReader = null;
    glyphToChar[paramInt] = new HashMap();
    try
    {
      localBufferedReader = paramString.equals("symbol.cfg") ? new BufferedReader(new InputStreamReader(loader.getResourceAsStream(new StringBuilder().append("org/jpedal/res/pdf/").append(paramString).toString()), "Cp1252")) : new BufferedReader(new InputStreamReader(loader.getResourceAsStream(new StringBuilder().append("org/jpedal/res/pdf/").append(paramString).toString()), "UTF-16"));
      if ((localBufferedReader == null) && (LogWriter.isOutput()))
        LogWriter.writeLog(new StringBuilder().append("Unable to open ").append(paramString).append(" to read standard encoding").toString());
      while (true)
      {
        String str4 = localBufferedReader.readLine();
        if (str4 == null)
          break;
        StringTokenizer localStringTokenizer = new StringTokenizer(str4);
        if ((!str4.contains("space")) && (localStringTokenizer.countTokens() > 1))
        {
          String str1;
          String str2;
          String str3;
          if (localStringTokenizer.countTokens() == 3)
          {
            str1 = localStringTokenizer.nextToken();
            str2 = localStringTokenizer.nextToken();
            str3 = localStringTokenizer.nextToken();
          }
          else if (localStringTokenizer.countTokens() == 4)
          {
            String str5 = localStringTokenizer.nextToken();
            str1 = localStringTokenizer.nextToken();
            str2 = localStringTokenizer.nextToken();
            str3 = localStringTokenizer.nextToken();
            str1 = Character.toString((char)Integer.parseInt(str5, 16));
          }
          else if (localStringTokenizer.countTokens() == 2)
          {
            str1 = " ";
            str2 = localStringTokenizer.nextToken();
            str3 = localStringTokenizer.nextToken();
          }
          else
          {
            str1 = localStringTokenizer.nextToken();
            str2 = localStringTokenizer.nextToken();
            str3 = localStringTokenizer.nextToken();
          }
          unicode_name_mapping_table.put(new StringBuilder().append(paramInt).append(str2).toString(), str1);
          glyphToChar[paramInt].put(str2, Integer.valueOf(Integer.parseInt(str3)));
          unicode_name_mapping_table.put(str2, str1);
          if (Character.isDigit(str3.charAt(0)))
          {
            int i = Integer.parseInt(str3, 8);
            if (paramInt == 5)
              ZAPF_char_encoding_table[i] = str1;
            else if (paramInt == 4)
              SYMBOL_char_encoding_table[i] = str1;
            else if (paramInt == 3)
              MACEXPERT_char_encoding_table[i] = str1;
            unicode_char_decoding_table[paramInt][i] = str2;
          }
        }
      }
    }
    catch (Exception localException1)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog(new StringBuilder().append("Exception ").append(localException1).append(" reading lookup table for pdf").toString());
    }
    if (localBufferedReader != null)
      try
      {
        localBufferedReader.close();
      }
      catch (Exception localException2)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog(new StringBuilder().append("Exception ").append(localException2).append(" reading lookup table for pdf  for abobe map").toString());
      }
  }

  private static final void readStandardMappingTable(int paramInt)
  {
    BufferedReader localBufferedReader = null;
    if (paramInt == 0)
      checkLoaded(2);
    try
    {
      glyphToChar[paramInt] = new HashMap();
      localBufferedReader = new BufferedReader(new InputStreamReader(loader.getResourceAsStream("org/jpedal/res/pdf/standard_encoding.cfg"), "Cp1252"));
      usesGlyphlist = true;
      if ((localBufferedReader == null) && (LogWriter.isOutput()))
        LogWriter.writeLog("Unable to open standard_encoding.cfg from jar");
      while (true)
      {
        String str8 = localBufferedReader.readLine();
        if (str8 == null)
          break;
        StringTokenizer localStringTokenizer = new StringTokenizer(str8);
        int m = localStringTokenizer.countTokens();
        String str2 = localStringTokenizer.nextToken();
        String str3 = localStringTokenizer.nextToken();
        String str4 = localStringTokenizer.nextToken();
        String str5 = localStringTokenizer.nextToken();
        String str6 = localStringTokenizer.nextToken();
        String str7 = localStringTokenizer.nextToken();
        String str1;
        if (m == 7)
          str1 = Character.toString((char)Integer.parseInt(str7, 16));
        else
          str1 = str7;
        unicodeToName.put(str1, str2);
        if ((paramInt == 0) && (Character.isDigit(str4.charAt(0))))
        {
          int i = Integer.parseInt(str4, 8);
          if (i == 201)
            str1 = ellipsis;
          MAC_char_encoding_table[i] = str1;
          unicode_char_decoding_table[0][i] = str2;
          glyphToChar[0].put(str2, Integer.valueOf(i));
        }
        else
        {
          int k;
          if ((paramInt == 1) && (Character.isDigit(str3.charAt(0))))
          {
            k = Integer.parseInt(str3, 8);
            if (k == 188)
              str1 = ellipsis;
            STD_char_encoding_table[k] = str1;
            unicode_char_decoding_table[1][k] = str2;
            glyphToChar[1].put(str2, Integer.valueOf(k));
          }
          else if ((paramInt == 6) && (Character.isDigit(str6.charAt(0))))
          {
            k = Integer.parseInt(str6, 8);
            if (k == 131)
              str1 = ellipsis;
            PDF_char_encoding_table[k] = str1;
            unicode_char_decoding_table[6][k] = str2;
          }
          else if ((paramInt == 2) && (Character.isDigit(str5.charAt(0))))
          {
            int j = Integer.parseInt(str5, 8);
            if (j == 133)
              str1 = ellipsis;
            WIN_char_encoding_table[j] = str1;
            unicode_char_decoding_table[2][j] = str2;
            glyphToChar[2].put(str2, Integer.valueOf(j));
          }
        }
        unicode_name_mapping_table.put(str2, str1);
      }
      if (paramInt == 0)
        MAC_char_encoding_table['Ê'] = " ";
      if (paramInt == 2)
      {
        WIN_char_encoding_table[' '] = " ";
        WIN_char_encoding_table['ÿ'] = "-";
        unicode_char_decoding_table[2][' '] = "space";
      }
    }
    catch (Exception localException1)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog(new StringBuilder().append("Exception ").append(localException1).append(" reading lookup table for pdf  for ").append(paramInt).toString());
    }
    if (localBufferedReader != null)
      try
      {
        localBufferedReader.close();
      }
      catch (Exception localException2)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog(new StringBuilder().append("Exception ").append(localException2).append(" reading lookup table for pdf  for abobe map").toString());
      }
  }

  public static String getNameFromUnicode(String paramString)
  {
    return (String)unicodeToName.get(paramString);
  }

  public static String getEncodedChar(int paramInt1, int paramInt2)
  {
    String str = null;
    if (paramInt1 == 2)
      str = WIN_char_encoding_table[paramInt2];
    else if (paramInt1 == 1)
      str = STD_char_encoding_table[paramInt2];
    else if (paramInt1 == 0)
      str = MAC_char_encoding_table[paramInt2];
    else if (paramInt1 == 6)
      str = PDF_char_encoding_table[paramInt2];
    else if (paramInt1 == 5)
      str = ZAPF_char_encoding_table[paramInt2];
    else if (paramInt1 == 4)
      str = SYMBOL_char_encoding_table[paramInt2];
    else if (paramInt1 == 3)
      str = MACEXPERT_char_encoding_table[paramInt2];
    if (str == null)
      str = new StringBuilder().append("&#").append(paramInt2).append(';').toString();
    return str;
  }

  public static boolean isValidMacEncoding(int paramInt)
  {
    return MAC_char_encoding_table[paramInt] != null;
  }

  public static boolean isValidWinEncoding(int paramInt)
  {
    return WIN_char_encoding_table[paramInt] != null;
  }

  public static void checkLoaded(int paramInt)
  {
    if ((paramInt == 0) && (MAC_char_encoding_table == null))
    {
      MAC_char_encoding_table = new String[335];
      readStandardMappingTable(paramInt);
    }
    else if ((paramInt == 2) && (WIN_char_encoding_table == null))
    {
      WIN_char_encoding_table = new String[335];
      readStandardMappingTable(paramInt);
    }
    else if ((paramInt == 1) && (STD_char_encoding_table == null))
    {
      STD_char_encoding_table = new String[335];
      readStandardMappingTable(paramInt);
    }
    else if ((paramInt == 6) && (PDF_char_encoding_table == null))
    {
      PDF_char_encoding_table = new String[335];
      readStandardMappingTable(paramInt);
    }
    else if ((paramInt == 4) && (SYMBOL_char_encoding_table == null))
    {
      SYMBOL_char_encoding_table = new String[335];
      readStandardMappingTable(4, "symbol.cfg");
    }
    else if ((paramInt == 5) && (ZAPF_char_encoding_table == null))
    {
      ZAPF_char_encoding_table = new String[335];
      readStandardMappingTable(5, "zapf.cfg");
    }
    else if ((paramInt == 3) && (MACEXPERT_char_encoding_table == null))
    {
      MACEXPERT_char_encoding_table = new String[335];
      readStandardMappingTable(3, "mac_expert.cfg");
    }
  }

  static final synchronized void loadStandardFont(int paramInt)
    throws IOException
  {
    String str3 = "";
    float f = 200.0F;
    BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(loader.getResourceAsStream(new StringBuilder().append("org/jpedal/res/pdf/defaults/").append(files_names[paramInt]).append(".afm").toString()), "Cp1252"));
    int i = 0;
    while (true)
    {
      String str1 = localBufferedReader.readLine();
      if (str1 == null)
        break;
      if (str1.startsWith("EndCharMetrics"))
        i = 0;
      Object localObject;
      if (str1.startsWith("FontBBox"))
      {
        localObject = new float[4];
        StringTokenizer localStringTokenizer = new StringTokenizer(str1);
        localStringTokenizer.nextToken();
        for (int j = 0; j < 4; j++)
          localObject[j] = Integer.parseInt(localStringTokenizer.nextToken());
        fontBounds.put(files_names[paramInt], localObject);
      }
      if (i != 0)
      {
        localObject = new StringTokenizer(str1, " ;");
        while (((StringTokenizer)localObject).hasMoreTokens())
        {
          String str2 = ((StringTokenizer)localObject).nextToken();
          if (str2.equals("WX"))
            f = Float.parseFloat(((StringTokenizer)localObject).nextToken()) / 1000.0F;
          else if (str2.equals("N"))
            str3 = ((StringTokenizer)localObject).nextToken();
        }
        widthTableStandard.put(new StringBuilder().append(files_names_bis[paramInt]).append(str3).toString(), Float.valueOf(f));
        widthTableStandard.put(new StringBuilder().append(files_names[paramInt]).append(str3).toString(), Float.valueOf(f));
      }
      if (str1.startsWith("StartCharMetrics"))
        i = 1;
    }
    if (localBufferedReader != null)
      try
      {
        localBufferedReader.close();
      }
      catch (Exception localException)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog(new StringBuilder().append("Exception ").append(localException).append(" reading lookup table for pdf  for abobe map").toString());
      }
  }

  protected static void loadStandardFontWidth(String paramString)
  {
    Integer localInteger = (Integer)standardFileList.get(paramString);
    if ((localInteger != null) && (standardFontLoaded.get(localInteger) == null))
    {
      standardFontLoaded.put(localInteger, "x");
      try
      {
        loadStandardFont(localInteger.intValue());
      }
      catch (Exception localException)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog(new StringBuilder().append("[PDF] ").append(localException).append(" problem reading lookup table for pdf font ").append(paramString).append(' ').append(paramString).toString());
      }
    }
  }

  public static int lookupCharacterIndex(String paramString, int paramInt)
  {
    Object localObject = glyphToChar[paramInt].get(paramString);
    if (localObject == null)
      return 0;
    return ((Integer)localObject).intValue();
  }

  private static void loadAdobeMap()
  {
    BufferedReader localBufferedReader = null;
    if (adobeMap == null)
      try
      {
        adobeMap = new HashMap();
        localBufferedReader = new BufferedReader(new InputStreamReader(loader.getResourceAsStream("org/jpedal/res/pdf/glyphlist.cfg"), "Cp1252"));
        if ((localBufferedReader == null) && (LogWriter.isOutput()))
          LogWriter.writeLog("Unable to open glyphlist.cfg from jar");
        while (true)
        {
          String str1 = localBufferedReader.readLine();
          if (str1 == null)
            break;
          if ((!str1.startsWith("#")) && (str1.indexOf(59) != -1))
          {
            StringTokenizer localStringTokenizer = new StringTokenizer(str1, ";");
            String str2 = localStringTokenizer.nextToken();
            String str3 = localStringTokenizer.nextToken();
            int i = str3.indexOf(32);
            if (i != -1)
              str3 = str3.substring(0, i);
            int j = Integer.parseInt(str3, 16);
            adobeMap.put(str2, Integer.valueOf(j));
            unicode_name_mapping_table.put(str2, Character.toString((char)j));
          }
        }
      }
      catch (Exception localException1)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog(new StringBuilder().append("Exception ").append(localException1).append(" reading lookup table for pdf  for abobe map").toString());
        localException1.printStackTrace(System.out);
      }
    if (localBufferedReader != null)
      try
      {
        localBufferedReader.close();
      }
      catch (Exception localException2)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog(new StringBuilder().append("Exception ").append(localException2).append(" reading lookup table for pdf  for abobe map").toString());
      }
  }

  public static int getAdobeMap(String paramString)
  {
    Object localObject = adobeMap.get(paramString);
    if (localObject == null)
      return -1;
    return ((Integer)localObject).intValue();
  }

  public static boolean isValidGlyphName(String paramString)
  {
    if (paramString == null)
      return false;
    return adobeMap.get(paramString) != null;
  }

  public static boolean isStandardFont(String paramString, boolean paramBoolean)
  {
    boolean bool = standardFileList.get(paramString) != null;
    if ((!bool) && (paramBoolean))
    {
      int i = paramString.indexOf(45);
      if (i != -1)
      {
        String str = paramString.substring(0, i);
        bool = standardFileList.get(str) != null;
      }
    }
    return bool;
  }

  public static Map getFontDetails(int paramInt, String paramString)
  {
    HashMap localHashMap = new HashMap();
    if ((paramInt == 1217103210) || (paramInt == 7))
      TTGlyphs.addStringValues(new FontData(paramString), localHashMap);
    return localHashMap;
  }

  public static String[] readNamesFromFont(int paramInt1, String paramString, int paramInt2)
    throws Exception
  {
    String[] arrayOfString = new String[1];
    arrayOfString[0] = "";
    if ((paramInt1 == 1217103210) || (paramInt1 == 7))
      arrayOfString = TTGlyphs.readFontNames(new FontData(paramString), paramInt2);
    else if (paramInt1 == 1228944677)
      arrayOfString = T1Glyphs.readFontNames(new FontData(paramString));
    return arrayOfString;
  }

  public static String convertNumberToGlyph(String paramString, boolean paramBoolean1, boolean paramBoolean2)
  {
    int i = paramString.length();
    int j = 1;
    int k = 0;
    int m;
    int n;
    if ((i == 2) || (i == 3) || ((paramBoolean2) && (i == 1)))
    {
      for (m = 0; m < i; m++)
      {
        n = paramString.charAt(m);
        if ((n < 48) || (n > 57))
          if ((paramBoolean1) && (n >= 65) && (n <= 70))
          {
            k = 1;
          }
          else
          {
            j = 0;
            m = i;
          }
      }
      if (j != 0)
        if ((i == 3) || (!paramBoolean1))
        {
          if (k == 0)
            paramString = String.valueOf((char)Integer.parseInt(paramString));
        }
        else
          paramString = String.valueOf((char)Integer.parseInt(paramString, 16));
    }
    else
    {
      m = 0;
      n = 0;
      int i2;
      for (int i1 = 0; i1 < i; i1++)
      {
        i2 = paramString.charAt(i1);
        if (i2 == 35)
          m = 1;
        if ((i2 >= 65) && (i2 <= 90))
          n = 1;
      }
      if (m != 0)
      {
        StringBuilder localStringBuilder = new StringBuilder(paramString);
        int i4;
        int i5;
        if (n != 0)
          try
          {
            for (i2 = 0; i2 < i; i2++)
            {
              i4 = localStringBuilder.charAt(i2);
              if ((i4 == 35) && (i2 < i))
                for (i5 = 0; i5 < 2; i5++)
                {
                  localStringBuilder.deleteCharAt(i2 + 1);
                  i--;
                }
            }
          }
          catch (Exception localException)
          {
            if (LogWriter.isOutput())
              LogWriter.writeLog(new StringBuilder().append("Exception: ").append(localException.getMessage()).toString());
          }
        else
          for (int i3 = 0; i3 < i; i3++)
          {
            i4 = localStringBuilder.charAt(i3);
            if (i4 == 35)
            {
              i3 += 3;
              if (i3 < i)
                for (i5 = localStringBuilder.charAt(i3); (i5 >= 48) && (i5 <= 57); i5 = localStringBuilder.charAt(i3))
                {
                  localStringBuilder.deleteCharAt(i3);
                  i--;
                  if (i3 >= i)
                    break;
                }
              i3--;
            }
          }
        paramString = localStringBuilder.toString();
      }
    }
    return paramString;
  }

  public static String getFontypeAsString(int paramInt)
  {
    switch (paramInt)
    {
    case 1217103210:
      return "TrueType";
    case 1228944677:
      return "Type1";
    case 1228944679:
      return "Type3";
    case -1684566726:
      return "CIDFontType0";
    case -1684566724:
      return "CIDFontType2";
    }
    return "Unknown";
  }

  private static void blockForbiddenRanges(ArrayList<Integer> paramArrayList)
  {
    for (int i = 0; i <= 31; i++)
      paramArrayList.add(Integer.valueOf(i));
    for (i = 127; i <= 160; i++)
      paramArrayList.add(Integer.valueOf(i));
    for (i = 8204; i <= 8207; i++)
      paramArrayList.add(Integer.valueOf(i));
  }

  public static int mapCIDToValidUnicode(String paramString, int paramInt)
  {
    HashMap localHashMap = (HashMap)mappedCharacters.get(paramString);
    ArrayList localArrayList = (ArrayList)takenChars.get(paramString);
    if (localHashMap == null)
    {
      localHashMap = new HashMap();
      mappedCharacters.put(paramString, localHashMap);
      localArrayList = new ArrayList();
      takenChars.put(paramString, localArrayList);
      blockForbiddenRanges(localArrayList);
    }
    Integer localInteger = (Integer)localHashMap.get(Integer.valueOf(paramInt));
    if (localInteger != null)
      return localInteger.intValue();
    int i = paramInt;
    if (paramInt < 32)
      i += 32;
    while ((localArrayList.contains(Integer.valueOf(i))) && (localArrayList.size() < 55296))
      i = (i + 1) % 55296;
    localHashMap.put(Integer.valueOf(paramInt), Integer.valueOf(i));
    localArrayList.add(Integer.valueOf(i));
    return i;
  }

  public static int getIDForGlyphName(String paramString1, String paramString2)
  {
    int i = 0;
    int j = getAdobeMap(paramString2);
    if (j >= 0)
      return j;
    if (paramString2.startsWith("uni"))
      paramString2 = paramString2.substring(3);
    else if ((paramString2.charAt(0) == 'u') || (paramString2.charAt(0) == 'G'))
      paramString2 = paramString2.substring(1);
    else
      i = 1;
    try
    {
      int k;
      if (i != 0)
      {
        k = Integer.parseInt(paramString2, 10);
        k = mapCIDToValidUnicode(paramString1, k);
      }
      else
      {
        k = Integer.parseInt(paramString2, 16);
      }
      return k;
    }
    catch (NumberFormatException localNumberFormatException)
    {
    }
    return -1;
  }

  public static String expandName(String paramString)
  {
    if (paramString.equals("Cour"))
      paramString = "Courier";
    else if (paramString.equals("Helv"))
      paramString = "Helvetica";
    else if (paramString.equals("HeBo"))
      paramString = "Helvetica-BOLD";
    else if (paramString.equals("ZaDb"))
      paramString = "ZapfDingbats";
    else if (paramString.equals("TiRo"))
      paramString = "Times";
    return paramString;
  }

  public static void readCMAP()
  {
    CMAP = new String[65536];
    try
    {
      BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(loader.getResourceAsStream("org/jpedal/res/pdf/jis.cfg")));
      while (true)
      {
        String str1 = localBufferedReader.readLine();
        if (str1 == null)
          break;
        if ((str1.startsWith("0")) && (str1.contains("#")))
        {
          StringTokenizer localStringTokenizer = new StringTokenizer(str1);
          String str2 = localStringTokenizer.nextToken().substring(2);
          int i = Integer.parseInt(str2, 16);
          int j = Integer.parseInt(localStringTokenizer.nextToken().substring(2), 16);
          CMAP[i] = String.valueOf((char)j);
        }
      }
      localBufferedReader.close();
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog(new StringBuilder().append("Exception: ").append(localException.getMessage()).toString());
    }
  }

  static boolean isAdobeCMAP(String paramString)
  {
    if (adobeCMAPS == null)
    {
      adobeCMAPS = new HashMap();
      for (String str : CIDFonts)
        adobeCMAPS.put(str, "x");
    }
    return adobeCMAPS.containsKey(paramString);
  }

  static
  {
    for (int i = 0; i < files_names.length; i++)
    {
      standardFileList.put(files_names_bis[i], Integer.valueOf(i));
      standardFileList.put(files_names[i], Integer.valueOf(i));
    }
    loadAdobeMap();
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.fonts.StandardFonts
 * JD-Core Version:    0.6.2
 */