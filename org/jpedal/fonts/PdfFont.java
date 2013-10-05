package org.jpedal.fonts;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.PrintStream;
import java.io.Serializable;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import org.jpedal.exception.PdfFontException;
import org.jpedal.external.JPedalHelper;
import org.jpedal.fonts.glyph.PdfJavaGlyphs;
import org.jpedal.io.ObjectStore;
import org.jpedal.io.PdfFileReader;
import org.jpedal.io.PdfObjectReader;
import org.jpedal.objects.raw.CIDEncodings;
import org.jpedal.objects.raw.FontObject;
import org.jpedal.objects.raw.PdfArrayIterator;
import org.jpedal.objects.raw.PdfObject;
import org.jpedal.parser.DecoderOptions;
import org.jpedal.utils.LogWriter;

public class PdfFont
  implements Serializable
{
  PdfObject ToUnicode;
  public Font javaFont = null;
  private Rectangle BBox = null;
  public String CMapName = null;
  boolean handleOddSapFontMapping = false;
  boolean isFirstScan = true;
  int isDouble = -1;
  private static final int noWidth = -1;
  protected boolean containsHexNumbers = false;
  protected boolean allNumbers = false;
  protected String embeddedFontName = null;
  protected String embeddedFamilyName = null;
  protected String copyright = null;
  private float missingWidth = -1.0F;
  boolean isSingleByte = false;
  protected boolean isFontVertical = false;
  private float lastWidth = -1.0F;
  public PdfJavaGlyphs glyphs = new PdfJavaGlyphs();
  private String[] cachedValue = new String[256];
  private String rawFontName = null;
  private float currentWidth;
  Map nonStandardMappings = new HashMap(256);
  public boolean hasDoubleBytes;
  protected String substituteFont = null;
  protected boolean renderPage = false;
  private static final float xscale = 0.001F;
  protected int embeddedEnc = 1;
  protected String[] diffs;
  public boolean isFontEmbedded = false;
  protected boolean TTstreamisCID = false;
  protected String fontID = "";
  protected int maxCharCount = 256;
  protected boolean hasEncoding = true;
  protected int fontTypes;
  protected String substituteFontFile = null;
  private int spaceChar = -1;
  String[] diffTable;
  private int[] diffCharTable;
  protected Map diffLookup = null;
  private float[] widthTable;
  private float possibleSpaceWidth = -1.0F;
  protected PdfObjectReader currentPdfFile;
  protected ClassLoader loader = getClass().getClassLoader();
  public double[] FontMatrix = { 0.001D, 0.0D, 0.0D, 0.001D, 0.0D, 0.0D };
  public float[] FontBBox = { 0.0F, 0.0F, 1000.0F, 1000.0F };
  float ascent = 0.0F;
  float descent = 0.0F;
  protected boolean isHex = false;
  private String[] unicodeMappings;
  protected int fontEnc = -1;
  protected boolean isCIDFont = false;
  private String[] CMAP;
  private String CIDfontEncoding;
  private float defaultWidth = 1.0F;
  protected boolean isFontSubstituted = false;
  protected int italicAngle = 0;
  boolean hasMatrixSet = false;
  boolean hasFBoxSet = false;
  private byte[] stream;
  private int[] CIDToGIDMap;
  private static boolean isCidJarPresent;

  public PdfFont()
  {
  }

  public PdfFont(PdfObjectReader paramPdfObjectReader)
  {
    init(paramPdfObjectReader);
  }

  private static void setStandardFontMappings()
  {
    int i = StandardFonts.files_names.length;
    String str1;
    String str2;
    for (int j = 0; j < i; j++)
    {
      str1 = StandardFonts.files_names_bis[j].toLowerCase();
      str2 = StandardFonts.javaFonts[j].toLowerCase();
      if ((!str1.equals(str2)) && (!FontMappings.fontSubstitutionAliasTable.containsKey(str1)))
        FontMappings.fontSubstitutionAliasTable.put(str1, str2);
    }
    for (j = 0; j < i; j++)
    {
      str1 = StandardFonts.files_names[j].toLowerCase();
      str2 = StandardFonts.javaFonts[j].toLowerCase();
      if ((!str1.equals(str2)) && (!FontMappings.fontSubstitutionAliasTable.containsKey(str1)))
        FontMappings.fontSubstitutionAliasTable.put(str1, str2);
      StandardFonts.javaFontList.put(StandardFonts.files_names[j], "x");
    }
  }

  public boolean isFontSubstituted()
  {
    return this.isFontSubstituted;
  }

  private void setCIDFontWidths(String paramString)
  {
    paramString = paramString.substring(1, paramString.length() - 1).trim();
    if (paramString.isEmpty())
      return;
    this.widthTable = new float[65536];
    for (int i = 0; i < 65536; i++)
      this.widthTable[i] = -1.0F;
    StringTokenizer localStringTokenizer1 = new StringTokenizer(paramString, " []", true);
    Object localObject1 = "";
    Object localObject2 = "";
    String str1 = "";
    int j = 0;
    int k = 0;
    while (localStringTokenizer1.hasMoreTokens())
    {
      str1 = localStringTokenizer1.nextToken();
      if (!str1.equals(" "))
        break;
    }
    int m = 0;
    while (true)
    {
      if (m != 0)
        return;
      int i1;
      if (str1.equals("R"))
      {
        String str2 = new StringBuilder().append((String)localObject2).append(' ').append((String)localObject1).append(" R").toString();
        i1 = Integer.parseInt((String)localObject2);
        int i2 = Integer.parseInt((String)localObject1);
        j = k;
        FontObject localFontObject = new FontObject(str2);
        byte[] arrayOfByte = this.currentPdfFile.getObjectReader().readObjectAsByteArray(localFontObject, this.currentPdfFile.getObjectReader().isCompressed(i1, i2), i1, i2);
        String str3 = new String(arrayOfByte);
        int i3 = str3.indexOf(91);
        int i4 = str3.lastIndexOf(93);
        if ((i3 > -1) && (i4 > -1) && (i3 < i4))
          str3 = str3.substring(i3, i4);
        StringTokenizer localStringTokenizer2 = new StringTokenizer(str3, " []");
        while (localStringTokenizer2.hasMoreTokens())
        {
          String str4 = localStringTokenizer2.nextToken();
          this.widthTable[j] = (Float.parseFloat(str4) / 1000.0F);
          j++;
        }
        if (localStringTokenizer1.hasMoreTokens())
          do
          {
            if (!localStringTokenizer1.hasMoreTokens())
              break;
            str1 = localStringTokenizer1.nextToken();
          }
          while (str1.equals(" "));
        else
          m = 1;
      }
      else
      {
        localObject2 = localObject1;
        localObject1 = str1;
        k = j;
        j = Integer.parseInt((String)localObject1);
        while (true)
        {
          localObject1 = localStringTokenizer1.nextToken();
          if (!((String)localObject1).equals(" "))
            break;
        }
        if (((String)localObject1).equals("["))
        {
          while (true)
          {
            localObject1 = localStringTokenizer1.nextToken();
            if (!((String)localObject1).equals(" "))
            {
              if (((String)localObject1).equals("]"))
                break;
              this.widthTable[j] = (Float.parseFloat((String)localObject1) / 1000.0F);
              j++;
            }
          }
          if (localStringTokenizer1.hasMoreTokens())
          {
            if (!localStringTokenizer1.hasMoreTokens())
              continue;
            str1 = localStringTokenizer1.nextToken();
            if (str1.equals(" "))
              break;
            continue;
          }
          m = 1;
          continue;
        }
        int n = 1 + Integer.parseInt((String)localObject1);
        localObject2 = localObject1;
        while (true)
        {
          localObject1 = localStringTokenizer1.nextToken();
          if (!((String)localObject1).equals(" "))
            break;
        }
        if (localStringTokenizer1.hasMoreTokens())
          do
          {
            if (!localStringTokenizer1.hasMoreTokens())
              break;
            str1 = localStringTokenizer1.nextToken();
          }
          while (str1.equals(" "));
        else
          m = 1;
        if (!str1.equals("R"))
          for (i1 = j; i1 < n; i1++)
            this.widthTable[i1] = (Float.parseFloat((String)localObject1) / 1000.0F);
      }
    }
  }

  public final boolean isCIDFont()
  {
    return this.isCIDFont;
  }

  protected final void init(PdfObjectReader paramPdfObjectReader)
  {
    this.currentPdfFile = paramPdfObjectReader;
    if (this.isCIDFont)
      this.maxCharCount = 65536;
    this.glyphs.init(this.maxCharCount, this.isCIDFont);
  }

  public final String getUnicodeMapping(int paramInt)
  {
    if (this.unicodeMappings == null)
      return null;
    return this.unicodeMappings[paramInt];
  }

  public final void putFontEncoding(int paramInt)
  {
    if ((paramInt == 2) && (getBaseFontName().equals("Symbol")))
    {
      putFontEncoding(4);
      paramInt = 4;
    }
    this.fontEnc = paramInt;
    StandardFonts.checkLoaded(paramInt);
  }

  public final String getUnicodeValue(String paramString, int paramInt)
  {
    String str = getUnicodeMapping(paramInt);
    if (str == null)
      str = paramString;
    if (!paramString.isEmpty())
    {
      int i = paramString.charAt(0);
      switch (i)
      {
      case 173:
        if ((this.fontEnc == 2) || (this.fontEnc == 1))
          str = "-";
        break;
      case 64256:
        str = "ff";
        break;
      case 64257:
        str = "fi";
        break;
      case 64260:
        str = "ffl";
      }
    }
    return str;
  }

  public final String getGlyphValue(int paramInt)
  {
    if (this.cachedValue[paramInt] != null)
      return this.cachedValue[paramInt];
    Object localObject = null;
    if (this.isCIDFont)
    {
      String str1 = getUnicodeMapping(paramInt);
      if (str1 != null)
        localObject = str1;
      if (localObject == null)
      {
        String str2 = this.CIDfontEncoding;
        if (this.diffTable != null)
        {
          localObject = this.diffTable[paramInt];
        }
        else
        {
          String str3;
          if (str2 != null)
          {
            if (str2.startsWith("Identity-"))
            {
              localObject = String.valueOf((char)paramInt);
            }
            else if (this.CMAP != null)
            {
              str3 = this.CMAP[paramInt];
              if (str3 != null)
                localObject = str3;
            }
          }
          else if ((str2 == null) && (this.CMAP != null) && (this.CMapName != null) && (!this.CMapName.endsWith("-V")) && (!this.CMapName.endsWith("-H")))
          {
            str3 = this.CMAP[paramInt];
            if (str3 != null)
              localObject = str3;
          }
        }
        if (localObject == null)
          localObject = String.valueOf((char)paramInt);
      }
    }
    else
    {
      localObject = getStandardGlyphValue(paramInt);
    }
    this.cachedValue[paramInt] = localObject;
    return localObject;
  }

  private final String handleCIDEncoding(PdfObject paramPdfObject1, PdfObject paramPdfObject2)
    throws PdfFontException
  {
    BufferedReader localBufferedReader = null;
    int i = paramPdfObject1.getGeneralType(1232564598);
    String str1 = CIDEncodings.getNameForEncoding(i);
    if (str1 == null)
      if (i == 2038913669)
      {
        str1 = "Identity-H";
      }
      else if (i == 2038913683)
      {
        str1 = "Identity-V";
        this.isFontVertical = true;
      }
      else
      {
        str1 = paramPdfObject1.getGeneralStringValue();
      }
    this.CMapName = paramPdfObject1.getName(827223669);
    if (this.CMapName != null)
    {
      this.stream = this.currentPdfFile.readStream(paramPdfObject1, true, true, false, false, false, paramPdfObject1.getCacheName(this.currentPdfFile.getObjectReader()));
      str1 = this.CMapName;
      localBufferedReader = new BufferedReader(new StringReader(new String(this.stream)));
    }
    int j = (i == 2038913669) || (i == 2038913683) ? 1 : 0;
    if ((j == 0) && (paramPdfObject2 != null))
    {
      String str2 = paramPdfObject2.getName(678461817);
      if ((i != -1) && (str2 != null) && (str2.contains("Identity")))
      {
        j = 1;
        this.handleOddSapFontMapping = true;
        this.isSingleByte = true;
      }
    }
    if (localBufferedReader == null)
      this.CIDfontEncoding = str1;
    if (j != 0)
    {
      this.glyphs.setIsIdentity(true);
    }
    else
    {
      this.glyphs.setIsIdentity(false);
      this.CMAP = new String[65536];
      this.glyphs.CMAP_Translate = new int[65536];
      if (localBufferedReader != null)
        readCIDCMap(localBufferedReader);
    }
    if (localBufferedReader != null)
      try
      {
        localBufferedReader.close();
      }
      catch (Exception localException)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog(new StringBuilder().append("2.Problem reading encoding for CID font ").append(this.fontID).append(' ').append(str1).append(" Check CID.jar installed").toString());
      }
    return this.CMapName;
  }

  private void readCIDCMap(BufferedReader paramBufferedReader)
  {
    String str1 = "";
    int m = 0;
    int n = 0;
    while (true)
    {
      try
      {
        str1 = paramBufferedReader.readLine();
      }
      catch (Exception localException1)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog("[PDF] Error reading line from font");
      }
      if (str1 == null)
        break;
      if (str1.contains("endcidrange"))
        m = 0;
      else if (str1.contains("endcidchar"))
        n = 0;
      StringTokenizer localStringTokenizer;
      int i;
      int j;
      if (m != 0)
      {
        localStringTokenizer = new StringTokenizer(str1, " <>[]");
        int i1 = 0;
        if (str1.indexOf(91) != -1)
          i1 = 1;
        i = Integer.parseInt(localStringTokenizer.nextToken(), 16);
        j = Integer.parseInt(localStringTokenizer.nextToken(), 16);
        String str2 = localStringTokenizer.nextToken();
        int i2 = str2.length();
        int i3 = 0;
        for (int i4 = 0; i4 < i2; i4++)
        {
          int i5 = str2.charAt(i4);
          if ((i5 < 48) || (i5 > 57))
          {
            i3 = 1;
            i4 = i2;
          }
        }
        int k;
        if (i3 != 0)
          k = Integer.parseInt(str2, 16);
        else
          k = Integer.parseInt(str2, 10);
        for (i4 = i; i4 < j + 1; i4++)
          if (i1 != 0)
          {
            k = Integer.parseInt(localStringTokenizer.nextToken(), 16);
            this.CMAP[i4] = String.valueOf((char)k);
          }
          else
          {
            this.CMAP[i4] = String.valueOf((char)k);
            k++;
          }
      }
      else if (n != 0)
      {
        try
        {
          localStringTokenizer = new StringTokenizer(str1, " <>[]");
          if (localStringTokenizer.countTokens() == 2)
          {
            i = Integer.parseInt(localStringTokenizer.nextToken(), 16);
            j = Integer.parseInt(localStringTokenizer.nextToken());
            this.glyphs.CMAP_Translate[i] = j;
          }
        }
        catch (Exception localException2)
        {
          localException2.getStackTrace();
        }
      }
      if (str1.contains("begincidrange"))
        m = 1;
      else if (str1.contains("begincidchar"))
        n = 1;
    }
  }

  private String getStandardGlyphValue(int paramInt)
  {
    String str1 = getUnicodeMapping(paramInt);
    if (str1 != null)
      return str1;
    Object localObject = "";
    int i = getFontEncoding(true);
    String str2 = getMappedChar(paramInt, true);
    if (str2 != null)
    {
      String str3 = StandardFonts.getUnicodeName(new StringBuilder().append(this.fontEnc).append(str2).toString());
      if (str3 != null)
      {
        localObject = str3;
      }
      else
      {
        str3 = StandardFonts.getUnicodeName(str2);
        if (str3 != null)
        {
          localObject = str3;
        }
        else if (str2.length() == 1)
        {
          localObject = str2;
        }
        else if (str2.length() > 1)
        {
          int j = str2.charAt(0);
          int k = str2.charAt(1);
          if ((j == 66) || (j == 67) || (j == 99) || (j == 71))
          {
            str2 = str2.substring(1);
            try
            {
              int m = this.isHex ? Integer.valueOf(str2, 16).intValue() : Integer.parseInt(str2);
              localObject = String.valueOf((char)m);
            }
            catch (Exception localException)
            {
              localObject = "";
            }
          }
          else
          {
            localObject = "";
          }
          int n = ((j >= 48) && (j <= 57)) || ((j >= 97) && (j <= 102)) || ((j >= 65) && (j <= 70) && (((k >= 48) && (k <= 57)) || ((k >= 97) && (k <= 102)) || ((k >= 65) && (k <= 70)))) ? 1 : 0;
          if ((((String)localObject).isEmpty()) && (this.fontTypes == 1228944679) && (str2.length() == 2) && (n != 0))
            localObject = String.valueOf((char)Integer.parseInt(str2, 16));
          if (((String)localObject).isEmpty())
            if (this.fontTypes == 1228944679)
            {
              localObject = String.valueOf((char)paramInt);
            }
            else if ((this.diffTable != null) && (this.diffTable[paramInt] != null) && (this.fontEnc == 2))
            {
              localObject = this.diffTable[paramInt];
              if (((String)localObject).indexOf(95) != -1)
                localObject = ((String)localObject).replaceAll("_", "");
            }
        }
        else
        {
          localObject = "";
        }
      }
    }
    else if (i > -1)
    {
      localObject = StandardFonts.getEncodedChar(i, paramInt);
    }
    return localObject;
  }

  public final Font getJavaFont(int paramInt)
  {
    int i = 0;
    int j = 0;
    String str1 = null;
    Object localObject1 = null;
    Object localObject2 = this.glyphs.fontName;
    Object localObject3 = localObject2;
    if (localObject2 != null)
      localObject3 = ((String)localObject2).toLowerCase();
    if (((String)localObject3).equals("arialmt"))
    {
      localObject3 = "arial";
      localObject2 = localObject3;
    }
    else if (((String)localObject3).equals("arial-boldmt"))
    {
      localObject3 = "arial Bold";
      localObject2 = localObject3;
    }
    if (localObject1 != null)
    {
      localObject2 = localObject1;
      localObject3 = ((String)localObject2).toLowerCase();
    }
    if (PdfJavaGlyphs.fontList != null)
    {
      int k = PdfJavaGlyphs.fontList.length;
      for (int m = 0; m < k; m++)
      {
        System.out.println(new StringBuilder().append(PdfJavaGlyphs.fontList[m]).append("<>").append((String)localObject3).toString());
        if (PdfJavaGlyphs.fontList[m].contains((CharSequence)localObject3))
        {
          j = 1;
          localObject2 = PdfJavaGlyphs.fontList[m];
          m = k;
        }
      }
    }
    if ((j == 0) && (str1 == null))
    {
      String str2 = ((String)localObject2).toLowerCase();
      if (str2.contains("heavy"))
        i = 1;
      else if (str2.contains("bold"))
        i = 1;
      else if (str2.contains("roman"))
        i = 0;
      if (str2.contains("italic"))
        i += 2;
      else if (str2.contains("oblique"))
        i += 2;
    }
    if (j != 0)
      return new Font((String)localObject2, i, paramInt);
    if (LogWriter.isOutput())
      LogWriter.writeLog(new StringBuilder().append("No match with ").append(this.glyphs.getBaseFontName()).append(' ').append(' ').append((String)localObject3).append(' ').append(str1).append(' ').append(i).toString());
    return null;
  }

  public final void setDefaultDisplayFont(String paramString)
  {
    this.glyphs.defaultFont = paramString;
  }

  public final Font getJavaFontX(int paramInt)
  {
    if (DecoderOptions.Helper != null)
    {
      Font localFont = DecoderOptions.Helper.getJavaFontX(this, paramInt);
      if (localFont != null)
        return localFont;
    }
    return new Font(this.glyphs.font_family_name, this.glyphs.style, paramInt);
  }

  public final String getFontName()
  {
    StandardFonts.loadStandardFontWidth(this.glyphs.fontName);
    return this.glyphs.fontName;
  }

  public final String getCopyright()
  {
    return this.copyright;
  }

  public final String getBaseFontName()
  {
    return this.glyphs.getBaseFontName();
  }

  public final void setBaseFontName(String paramString)
  {
    this.glyphs.setBaseFontName(paramString);
  }

  public final float getCurrentFontSpaceWidth()
  {
    int i = this.spaceChar;
    float f;
    if (i != -1)
      f = getWidth(i);
    else
      f = this.possibleSpaceWidth;
    if ((f == -1.0F) || (f == 0.0F))
      f = 0.2F;
    return f;
  }

  protected final int getFontEncoding(boolean paramBoolean)
  {
    int i = this.fontEnc;
    if ((i == -1) && (paramBoolean))
      i = 1;
    return i;
  }

  public final float getWidth(int paramInt)
  {
    if (paramInt == -1)
      return this.lastWidth;
    float f = -1.0F;
    if ((this.widthTable != null) && (paramInt != -1))
      f = this.widthTable[paramInt];
    if (f == -1.0F)
      if (this.isCIDFont)
      {
        f = this.defaultWidth;
      }
      else
      {
        String str = getMappedChar(paramInt, false);
        if ((str != null) && (str.equals(".notdef")))
          str = StandardFonts.getUnicodeChar(getFontEncoding(true), paramInt);
        Float localFloat = StandardFonts.getStandardWidth(this.glyphs.logicalfontName, str);
        if ((localFloat == null) && (this.rawFontName != null))
        {
          StandardFonts.loadStandardFontWidth(this.rawFontName);
          localFloat = StandardFonts.getStandardWidth(this.rawFontName, str);
        }
        if (localFloat != null)
          f = localFloat.floatValue();
        else if (this.missingWidth != -1.0F)
          f = this.missingWidth * 0.001F;
        else
          f = 0.0F;
      }
    this.lastWidth = f;
    return f;
  }

  public void createCIDFont(PdfObject paramPdfObject1, PdfObject paramPdfObject2)
    throws PdfFontException
  {
    this.cachedValue = new String[65536];
    String str1 = null;
    PdfObject localPdfObject1 = paramPdfObject1.getDictionary(1232564598);
    if (localPdfObject1 != null)
      str1 = handleCIDEncoding(localPdfObject1, paramPdfObject1);
    this.ToUnicode = paramPdfObject1.getDictionary(1919185554);
    if (this.ToUnicode != null)
    {
      localObject1 = new UnicodeReader(this.currentPdfFile.readStream(this.ToUnicode, true, true, false, false, false, this.ToUnicode.getCacheName(this.currentPdfFile.getObjectReader())));
      this.unicodeMappings = ((UnicodeReader)localObject1).readUnicode();
      this.hasDoubleBytes = ((UnicodeReader)localObject1).hasDoubleByteValues();
    }
    Object localObject1 = paramPdfObject2.getName(39);
    String str2 = paramPdfObject2.getName(9986);
    if (str2 != null)
      localObject1 = str2;
    if (localObject1 != null)
      setCIDFontWidths((String)localObject1);
    int i = paramPdfObject2.getInt(5159);
    if (i >= 0)
      this.defaultWidth = (i / 1000.0F);
    if (this.handleOddSapFontMapping)
      this.defaultWidth = 0.5F;
    PdfObject localPdfObject2 = paramPdfObject2.getDictionary(946823533);
    if (localPdfObject2 != null)
    {
      localObject2 = this.currentPdfFile.readStream(localPdfObject2, true, true, false, false, false, null);
      if (localObject2 != null)
      {
        int j = 0;
        int k = localObject2.length;
        this.CIDToGIDMap = new int[k / 2];
        int m = 0;
        while (m < k)
        {
          this.CIDToGIDMap[j] = (((localObject2[m] & 0xFF) << 8) + (localObject2[(m + 1)] & 0xFF));
          j++;
          m += 2;
        }
        this.glyphs.setGIDtoCID(this.CIDToGIDMap);
      }
      else if (localPdfObject2.getParameterConstant(946823533) == -1)
      {
        str1 = handleCIDEncoding(new FontObject(2038913669), null);
      }
    }
    Object localObject2 = null;
    PdfObject localPdfObject3 = paramPdfObject2.getDictionary(1972801240);
    if (localPdfObject3 != null)
      localObject2 = localPdfObject3.getTextStreamValue(1635480172);
    if (localObject2 != null)
    {
      if ((localPdfObject2 != null) || (!((String)localObject2).contains("Identity")))
        if (((String)localObject2).contains("Japan"))
          this.TTstreamisCID = false;
        else if ((!((String)localObject2).contains("Korean")) && (!((String)localObject2).contains("Chinese")) && (((String)localObject2).equals("Identity")) && ((str1 == null) || (str1.contains("Ident"))))
          this.glyphs.setIsIdentity(true);
      if ((this.substituteFontFile != null) && (LogWriter.isOutput()))
        LogWriter.writeLog(new StringBuilder().append("Using font ").append(this.substituteFontFile).append(" for ").append((String)localObject2).toString());
    }
    if (paramPdfObject2 != null)
    {
      PdfObject localPdfObject4 = paramPdfObject2.getDictionary(-1044665361);
      if (localPdfObject4 != null)
      {
        setBoundsAndMatrix(localPdfObject4);
        setName(localPdfObject4);
      }
    }
  }

  protected final void selectDefaultFont()
  {
  }

  public void readWidths(PdfObject paramPdfObject, boolean paramBoolean)
    throws Exception
  {
    float f1 = 0.0F;
    int i = 0;
    float[] arrayOfFloat = paramPdfObject.getFloatArray(876896124);
    if ((arrayOfFloat != null) && (arrayOfFloat.length > 0))
    {
      this.widthTable = new float[this.maxCharCount];
      for (int j = 0; j < this.maxCharCount; j++)
        this.widthTable[j] = -1.0F;
      j = paramPdfObject.getInt(1283093660);
      int k = paramPdfObject.getInt(795440262);
      float f2 = (float)(1.0D / this.FontMatrix[0]);
      if (f2 < 0.0F)
        f2 = -f2;
      int m = 0;
      int n = arrayOfFloat.length;
      for (int i1 = j; i1 < k + 1; i1++)
      {
        if (m < n)
        {
          float f3 = arrayOfFloat[m];
          float f4;
          if (this.fontTypes == 1228944679)
            f4 = f3 / f2;
          else
            f4 = f3 * 0.001F;
          if (f4 > 0.0F)
          {
            f1 += f4;
            i++;
          }
          this.widthTable[i1] = f4;
        }
        else
        {
          this.widthTable[i1] = 0.0F;
        }
        m++;
      }
    }
    if ((paramBoolean) && (i > 0))
      this.possibleSpaceWidth = (f1 / (2 * i));
  }

  public void createFont(PdfObject paramPdfObject, String paramString, boolean paramBoolean, ObjectStore paramObjectStore, Map paramMap)
    throws Exception
  {
    init(paramString, paramBoolean);
    PdfObject localPdfObject = paramPdfObject.getDictionary(-1044665361);
    setName(paramPdfObject);
    setEncoding(paramPdfObject, localPdfObject);
  }

  protected void setName(PdfObject paramPdfObject)
  {
    String str1 = paramPdfObject.getName(678461817);
    if (str1 == null)
      str1 = paramPdfObject.getName(879786873);
    if (str1 == null)
      str1 = this.fontID;
    if (str1.contains("#20"))
      str1 = cleanupFontName(str1);
    this.glyphs.setBaseFontName(str1);
    String str2 = paramPdfObject.getStringValue(678461817, 2);
    if (str2 == null)
      str2 = paramPdfObject.getStringValue(879786873, 2);
    if (str2 == null)
      str2 = this.fontID;
    if ((str2.contains("#20")) || (str2.contains("#2D")))
      str2 = cleanupFontName(str2);
    this.glyphs.fontName = str2;
    if (str2.equals("Arial-BoldMT"))
    {
      this.glyphs.logicalfontName = "Arial,Bold";
      StandardFonts.loadStandardFontWidth(this.glyphs.logicalfontName);
    }
    else if (str2.equals("ArialMT"))
    {
      this.glyphs.logicalfontName = "Arial";
      StandardFonts.loadStandardFontWidth(this.glyphs.logicalfontName);
    }
    else
    {
      this.glyphs.logicalfontName = str2;
    }
  }

  public void resetNameForHTML(String paramString)
  {
    this.glyphs.fontName = paramString;
    this.glyphs.baseFontName = paramString;
  }

  protected void setEncoding(PdfObject paramPdfObject1, PdfObject paramPdfObject2)
  {
    PdfObject localPdfObject1 = paramPdfObject1.getDictionary(1919185554);
    if (localPdfObject1 != null)
      this.unicodeMappings = new UnicodeReader(this.currentPdfFile.readStream(localPdfObject1, true, true, false, false, false, localPdfObject1.getCacheName(this.currentPdfFile.getObjectReader()))).readUnicode();
    PdfObject localPdfObject2 = paramPdfObject1.getDictionary(1232564598);
    if (localPdfObject2 != null)
      handleFontEncoding(paramPdfObject1, localPdfObject2);
    else
      handleNoEncoding(0, paramPdfObject1);
    if (paramPdfObject2 != null)
    {
      int i = 0;
      if (paramPdfObject2 != null)
        i = paramPdfObject2.getInt(1009858393);
      this.glyphs.remapFont = false;
      int j = i;
      if ((j & 0x4) == 4)
        this.glyphs.remapFont = true;
      this.missingWidth = paramPdfObject2.getInt(-1884569950);
    }
  }

  protected void setBoundsAndMatrix(PdfObject paramPdfObject)
  {
    if (paramPdfObject != null)
    {
      double[] arrayOfDouble = paramPdfObject.getDoubleArray(-2105119560);
      if (arrayOfDouble != null)
        this.FontMatrix = arrayOfDouble;
      float[] arrayOfFloat = paramPdfObject.getFloatArray(676429196);
      if (arrayOfFloat != null)
        this.FontBBox = arrayOfFloat;
      float f = paramPdfObject.getFloatNumber(859131783);
      if (f != 0.0F)
        this.ascent = f;
      f = paramPdfObject.getFloatNumber(860451719);
      if (f != 0.0F)
        this.descent = f;
    }
  }

  protected void init(String paramString, boolean paramBoolean)
  {
    if ((paramBoolean) && (PdfJavaGlyphs.fontList == null) && (paramBoolean) && (PdfJavaGlyphs.fontList == null))
    {
      String[] arrayOfString = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
      for (int i = 0; i < arrayOfString.length; i++)
        arrayOfString[i] = arrayOfString[i].toLowerCase();
      PdfJavaGlyphs.fontList = arrayOfString;
    }
    this.fontID = paramString;
    this.renderPage = paramBoolean;
  }

  private int handleNoEncoding(int paramInt, PdfObject paramPdfObject)
  {
    int i = paramPdfObject.getGeneralType(1232564598);
    if (i == 5)
    {
      putFontEncoding(5);
      this.glyphs.defaultFont = "Zapf Dingbats";
      StandardFonts.checkLoaded(5);
      paramInt = 5;
    }
    else if (i == 4)
    {
      putFontEncoding(4);
      paramInt = 4;
    }
    else
    {
      putFontEncoding(1);
    }
    this.hasEncoding = false;
    return paramInt;
  }

  private void handleFontEncoding(PdfObject paramPdfObject1, PdfObject paramPdfObject2)
  {
    int i = paramPdfObject1.getParameterConstant(1147962727);
    int j = getFontEncoding(false);
    if (j == -1)
      if (i == 1217103210)
        j = 0;
      else
        j = 1;
    PdfArrayIterator localPdfArrayIterator = paramPdfObject2.getMixedArray(1954328750);
    int m;
    if ((localPdfArrayIterator != null) && (localPdfArrayIterator.getTokenCount() > 0))
    {
      this.glyphs.setIsSubsetted(true);
      byte[][] arrayOfByte1 = (byte[][])null;
      if (paramPdfObject2 != null)
        arrayOfByte1 = paramPdfObject2.getByteArray(1954328750);
      if (arrayOfByte1 != null)
      {
        this.containsHexNumbers = true;
        this.allNumbers = true;
        for (byte[] arrayOfByte3 : arrayOfByte1)
          if ((arrayOfByte3 != null) && (arrayOfByte3[0] == 47))
          {
            int i2 = arrayOfByte3.length;
            byte[] arrayOfByte4 = arrayOfByte3;
            int i4 = 0;
            int i5;
            int i3;
            if ((i2 == 3) && (this.containsHexNumbers))
              for (i5 = 1; i5 < 3; i5++)
              {
                i3 = (char)arrayOfByte4[i5];
                if (((i3 >= 48) && (i3 <= 57)) || ((i3 >= 65) && (i3 <= 70)))
                  i4 = (char)(i4 + 1);
              }
            if (i4 != 2)
              this.containsHexNumbers = false;
            if ((this.allNumbers) && (i2 < 4))
              for (i5 = 2; i5 < i2; i5++)
              {
                i3 = (char)arrayOfByte4[i5];
                if ((i3 < 48) || (i3 > 57))
                {
                  this.allNumbers = false;
                  i5 = i2;
                }
              }
          }
      }
      m = 0;
      while (localPdfArrayIterator.hasMoreTokens())
      {
        ??? = localPdfArrayIterator.getNextValueType();
        if (??? == 1)
        {
          m = localPdfArrayIterator.getNextValueAsInteger();
        }
        else
        {
          if (??? == 2)
          {
            if (this.diffCharTable == null)
              this.diffCharTable = new int[this.maxCharCount];
            this.diffCharTable[m] = localPdfArrayIterator.getNextValueAsInteger(false);
          }
          putMappedChar(m, localPdfArrayIterator.getNextValueAsFontChar(m, this.containsHexNumbers, this.allNumbers));
          m++;
        }
        if (m == 256)
          break;
      }
      this.isHex = localPdfArrayIterator.hasHexChars();
      ??? = localPdfArrayIterator.getSpaceChar();
      if (??? != -1)
        this.spaceChar = ???;
    }
    int k = -1;
    if (paramPdfObject2 != null)
    {
      this.hasEncoding = true;
      m = paramPdfObject2.getGeneralType(1232564598);
      if (m == -1)
        if (getBaseFontName().equals("ZapfDingbats"))
          m = 5;
        else
          m = paramPdfObject2.getParameterConstant(1537782955);
      if (m != -1)
        k = m;
      else
        k = handleNoEncoding(j, paramPdfObject1);
    }
    putFontEncoding(k);
  }

  public final int getDiffChar(String paramString)
  {
    int i = -1;
    Integer localInteger = (Integer)this.nonStandardMappings.get(paramString);
    if (localInteger != null)
      i = localInteger.intValue();
    return i;
  }

  protected final void putMappedChar(int paramInt, String paramString)
  {
    if (this.diffTable == null)
    {
      this.diffTable = new String[this.maxCharCount];
      this.diffLookup = new HashMap();
    }
    if (((paramInt <= 255) || (this.maxCharCount != 256)) && (this.diffTable[paramInt] == null) && (paramString != null) && (!paramString.startsWith("glyph")))
    {
      this.diffTable[paramInt] = paramString;
      this.diffLookup.put(paramString, Integer.valueOf(paramInt));
    }
  }

  public String getDiffMapping(int paramInt)
  {
    if (this.diffTable == null)
      return null;
    return this.diffTable[paramInt];
  }

  public final String getMappedChar(int paramInt, boolean paramBoolean)
  {
    String str = null;
    if (this.diffTable != null)
      str = this.diffTable[paramInt];
    if ((paramBoolean) && (str != null) && (str.equals(".notdef")))
      str = " ";
    if ((str == null) && (paramInt < 335))
      str = StandardFonts.getUnicodeChar(getFontEncoding(true), paramInt);
    if ((str == null) && (paramInt > 40) && (getFontEncoding(true) == 2))
      if (paramInt == 173)
        str = "hyphen";
      else
        str = "bullet";
    if ((this.isFontEmbedded) && (str == null))
    {
      if (this.diffs != null)
        str = this.diffs[paramInt];
      if ((str == null) && (paramInt < 335))
        str = StandardFonts.getUnicodeChar(this.embeddedEnc, paramInt);
    }
    return str;
  }

  public final String getEmbeddedChar(int paramInt)
  {
    String str = null;
    if (this.isFontEmbedded)
    {
      if (this.diffs != null)
        str = this.diffs[paramInt];
      if ((str == null) && (paramInt < 256))
        str = StandardFonts.getUnicodeChar(this.embeddedEnc, paramInt);
    }
    return str;
  }

  public final int getFontType()
  {
    return this.fontTypes;
  }

  public String getSubstituteFont()
  {
    return this.substituteFontFile;
  }

  public boolean isValidCodeRange(int paramInt)
  {
    if (this.CMAP == null)
      return false;
    return this.CMAP[paramInt] != null;
  }

  public float getGlyphWidth(String paramString1, int paramInt, String paramString2)
  {
    if (this.fontTypes == 1217103210)
      return this.glyphs.getTTWidth(paramString1, paramInt, paramString2, false);
    return 0.0F;
  }

  public void setSubtype(int paramInt)
  {
    this.fontTypes = paramInt;
  }

  public void setSubstituted(boolean paramBoolean)
  {
    this.isFontSubstituted = paramBoolean;
  }

  public PdfJavaGlyphs getGlyphData()
  {
    this.glyphs.setHasWidths(true);
    return this.glyphs;
  }

  public Font setFont(String paramString, int paramInt)
  {
    return this.glyphs.setFont(paramString, paramInt);
  }

  public boolean is1C()
  {
    return this.glyphs.is1C();
  }

  public boolean isFontSubsetted()
  {
    return this.glyphs.isSubsetted;
  }

  public void setValuesForGlyph(int paramInt, String paramString1, String paramString2, String paramString3)
  {
    this.glyphs.setValuesForGlyph(paramInt, paramString1, paramString2, paramString3);
  }

  private static String cleanupFontName(String paramString)
  {
    int i = paramString.length();
    StringBuilder localStringBuilder = new StringBuilder(i);
    for (int j = 0; j < i; j++)
    {
      char c = paramString.charAt(j);
      if ((c != ' ') && (c != '-'))
        if ((c == '#') && (paramString.charAt(j + 1) == '2') && ((paramString.charAt(j + 2) == '0') || (paramString.charAt(j + 2) == 'D')))
        {
          if (paramString.charAt(j + 2) == 'D')
            localStringBuilder.append('-');
          j += 2;
        }
        else
        {
          localStringBuilder.append(c);
        }
    }
    return localStringBuilder.toString();
  }

  public int getItalicAngle()
  {
    return this.italicAngle;
  }

  public Rectangle getBoundingBox()
  {
    if (this.BBox == null)
    {
      float[] arrayOfFloat = StandardFonts.getFontBounds(getFontName());
      if (arrayOfFloat == null)
      {
        if (!this.isFontEmbedded)
          this.BBox = new Rectangle(0, 0, 1000, 1000);
        else
          this.BBox = new Rectangle((int)this.FontBBox[0], (int)this.FontBBox[1], (int)(this.FontBBox[2] - this.FontBBox[0]), (int)(this.FontBBox[3] - this.FontBBox[1]));
      }
      else
        this.BBox = new Rectangle((int)arrayOfFloat[0], (int)arrayOfFloat[1], (int)(arrayOfFloat[2] - arrayOfFloat[0]), (int)(arrayOfFloat[3] - arrayOfFloat[1]));
    }
    return this.BBox;
  }

  public void setRawFontName(String paramString)
  {
    this.rawFontName = paramString;
  }

  public static String getSpaces(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    String str = "";
    if (paramFloat2 > 0.0F)
    {
      if ((paramFloat1 > paramFloat2) && ((paramFloat3 < 1.0F) || (paramFloat1 > paramFloat2 * paramFloat3)));
      while (paramFloat1 >= paramFloat2)
      {
        str = new StringBuilder().append(' ').append(str).toString();
        paramFloat1 -= paramFloat2;
        continue;
        if (paramFloat1 > paramFloat2 * paramFloat3)
          str = new StringBuilder().append(str).append(' ').toString();
      }
    }
    return str;
  }

  public int getDiffChar(int paramInt)
  {
    if (this.diffCharTable == null)
      return 0;
    return this.diffCharTable[paramInt];
  }

  public float[] getFontBounds()
  {
    return this.FontBBox;
  }

  public boolean isFontVertical()
  {
    return this.isFontVertical;
  }

  public void setCurrentWidth(float paramFloat)
  {
    this.currentWidth = paramFloat;
  }

  public float getCurrentWidth()
  {
    return this.currentWidth;
  }

  public Object getFontID()
  {
    return this.fontID;
  }

  public boolean isSingleByte()
  {
    return this.isSingleByte;
  }

  public boolean isBrokenFont()
  {
    return this.handleOddSapFontMapping;
  }

  public boolean hasToUnicode()
  {
    return this.unicodeMappings != null;
  }

  public float getDefaultWidth(int paramInt)
  {
    if (paramInt == -1)
      return this.defaultWidth;
    if (this.widthTable == null)
      return -1.0F;
    return this.widthTable[paramInt];
  }

  public float getAscent()
  {
    return this.ascent;
  }

  public float getDescent()
  {
    return this.descent;
  }

  public int isDoubleBytes(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    if (this.hasDoubleBytes)
      return 1;
    if (this.isFirstScan)
    {
      if (((paramInt1 == paramInt2) && (paramInt1 > 0)) || ((paramInt2 == 41) && (!paramBoolean)) || ((getMappedChar(paramInt1, true) != null) && (getMappedChar(paramInt2, true) != null)))
        this.isDouble = 0;
      else
        this.isDouble = 1;
      this.isFirstScan = false;
    }
    return this.isDouble;
  }

  public int isDoubleBytes()
  {
    return this.isDouble;
  }

  public PdfObject getToUnicode()
  {
    return this.ToUnicode;
  }

  public int[] getCIDToGIDMap()
  {
    return this.CIDToGIDMap;
  }

  public String[] getCMAP()
  {
    return this.CMAP;
  }

  static
  {
    setStandardFontMappings();
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.fonts.PdfFont
 * JD-Core Version:    0.6.2
 */