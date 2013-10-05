package org.jpedal.fonts;

import java.awt.Rectangle;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Map;
import org.jpedal.fonts.glyph.PdfJavaGlyphs;
import org.jpedal.fonts.glyph.T1Glyphs;
import org.jpedal.fonts.objects.FontData;
import org.jpedal.fonts.tt.conversion.FontWriter;
import org.jpedal.io.ObjectStore;
import org.jpedal.io.PdfObjectReader;
import org.jpedal.objects.raw.PdfObject;
import org.jpedal.utils.LogWriter;

public class Type1C extends Type1
{
  static final boolean debugFont = false;
  static final boolean debugDictionary = false;
  int ros = -1;
  int CIDFontVersion = 0;
  int CIDFontRevision = 0;
  int CIDFontType = 0;
  int CIDcount = 0;
  int UIDBase = -1;
  int FDArray = -1;
  int FDSelect = -1;
  int[] rosArr = null;
  static final String[] OneByteCCFDict = { "version", "Notice", "FullName", "FamilyName", "Weight", "FontBBox", "BlueValues", "OtherBlues", "FamilyBlues", "FamilyOtherBlues", "StdHW", "StdVW", "Escape", "UniqueID", "XUID", "charset", "Encoding", "CharStrings", "Private", "Subrs", "defaultWidthX", "nominalWidthX", "-reserved-", "-reserved-", "-reserved-", "-reserved-", "-reserved-", "-reserved-", "shortint", "longint", "BCD", "-reserved-" };
  static final String[] TwoByteCCFDict = { "Copyright", "isFixedPitch", "ItalicAngle", "UnderlinePosition", "UnderlineThickness", "PaintType", "CharstringType", "FontMatrix", "StrokeWidth", "BlueScale", "BlueShift", "BlueFuzz", "StemSnapH", "StemSnapV", "ForceBold", "-reserved-", "-reserved-", "LanguageGroup", "ExpansionFactor", "initialRandomSeed", "SyntheticBase", "PostScript", "BaseFontName", "BaseFontBlend", "-reserved-", "-reserved-", "-reserved-", "-reserved-", "-reserved-", "-reserved-", "ROS", "CIDFontVersion", "CIDFontRevision", "CIDFontType", "CIDCount", "UIDBase", "FDArray", "FDSelect", "FontName" };
  private int top = 0;
  private int charset = 0;
  private int enc = 0;
  private int charstrings = 0;
  private int stringIdx;
  private int stringStart;
  private int stringOffSize;
  private Rectangle BBox = null;
  private boolean hasFontMatrix = false;
  private int[] privateDict = { -1 };
  private int[] privateDictOffset = { -1 };
  private int currentFD = -1;
  private int[] defaultWidthX = { 0 };
  private int[] nominalWidthX = { 0 };
  private int[] fdSelect;
  private static final int[] ExpertSubCharset = { 0, 1, 231, 232, 235, 236, 237, 238, 13, 14, 15, 99, 239, 240, 241, 242, 243, 244, 245, 246, 247, 248, 27, 28, 249, 250, 251, 253, 254, 255, 256, 257, 258, 259, 260, 261, 262, 263, 264, 265, 266, 109, 110, 267, 268, 269, 270, 272, 300, 301, 302, 305, 314, 315, 158, 155, 163, 320, 321, 322, 323, 324, 325, 326, 150, 164, 169, 327, 328, 329, 330, 331, 332, 333, 334, 335, 336, 337, 338, 339, 340, 341, 342, 343, 344, 345, 346 };
  public static final String[] type1CStdStrings = { ".notdef", "space", "exclam", "quotedbl", "numbersign", "dollar", "percent", "ampersand", "quoteright", "parenleft", "parenright", "asterisk", "plus", "comma", "hyphen", "period", "slash", "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "colon", "semicolon", "less", "equal", "greater", "question", "at", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "bracketleft", "backslash", "bracketright", "asciicircum", "underscore", "quoteleft", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "braceleft", "bar", "braceright", "asciitilde", "exclamdown", "cent", "sterling", "fraction", "yen", "florin", "section", "currency", "quotesingle", "quotedblleft", "guillemotleft", "guilsinglleft", "guilsinglright", "fi", "fl", "endash", "dagger", "daggerdbl", "periodcentered", "paragraph", "bullet", "quotesinglbase", "quotedblbase", "quotedblright", "guillemotright", "ellipsis", "perthousand", "questiondown", "grave", "acute", "circumflex", "tilde", "macron", "breve", "dotaccent", "dieresis", "ring", "cedilla", "hungarumlaut", "ogonek", "caron", "emdash", "AE", "ordfeminine", "Lslash", "Oslash", "OE", "ordmasculine", "ae", "dotlessi", "lslash", "oslash", "oe", "germandbls", "onesuperior", "logicalnot", "mu", "trademark", "Eth", "onehalf", "plusminus", "Thorn", "onequarter", "divide", "brokenbar", "degree", "thorn", "threequarters", "twosuperior", "registered", "minus", "eth", "multiply", "threesuperior", "copyright", "Aacute", "Acircumflex", "Adieresis", "Agrave", "Aring", "Atilde", "Ccedilla", "Eacute", "Ecircumflex", "Edieresis", "Egrave", "Iacute", "Icircumflex", "Idieresis", "Igrave", "Ntilde", "Oacute", "Ocircumflex", "Odieresis", "Ograve", "Otilde", "Scaron", "Uacute", "Ucircumflex", "Udieresis", "Ugrave", "Yacute", "Ydieresis", "Zcaron", "aacute", "acircumflex", "adieresis", "agrave", "aring", "atilde", "ccedilla", "eacute", "ecircumflex", "edieresis", "egrave", "iacute", "icircumflex", "idieresis", "igrave", "ntilde", "oacute", "ocircumflex", "odieresis", "ograve", "otilde", "scaron", "uacute", "ucircumflex", "udieresis", "ugrave", "yacute", "ydieresis", "zcaron", "exclamsmall", "Hungarumlautsmall", "dollaroldstyle", "dollarsuperior", "ampersandsmall", "Acutesmall", "parenleftsuperior", "parenrightsuperior", "twodotenleader", "onedotenleader", "zerooldstyle", "oneoldstyle", "twooldstyle", "threeoldstyle", "fouroldstyle", "fiveoldstyle", "sixoldstyle", "sevenoldstyle", "eightoldstyle", "nineoldstyle", "commasuperior", "threequartersemdash", "periodsuperior", "questionsmall", "asuperior", "bsuperior", "centsuperior", "dsuperior", "esuperior", "isuperior", "lsuperior", "msuperior", "nsuperior", "osuperior", "rsuperior", "ssuperior", "tsuperior", "ff", "ffi", "ffl", "parenleftinferior", "parenrightinferior", "Circumflexsmall", "hyphensuperior", "Gravesmall", "Asmall", "Bsmall", "Csmall", "Dsmall", "Esmall", "Fsmall", "Gsmall", "Hsmall", "Ismall", "Jsmall", "Ksmall", "Lsmall", "Msmall", "Nsmall", "Osmall", "Psmall", "Qsmall", "Rsmall", "Ssmall", "Tsmall", "Usmall", "Vsmall", "Wsmall", "Xsmall", "Ysmall", "Zsmall", "colonmonetary", "onefitted", "rupiah", "Tildesmall", "exclamdownsmall", "centoldstyle", "Lslashsmall", "Scaronsmall", "Zcaronsmall", "Dieresissmall", "Brevesmall", "Caronsmall", "Dotaccentsmall", "Macronsmall", "figuredash", "hypheninferior", "Ogoneksmall", "Ringsmall", "Cedillasmall", "questiondownsmall", "oneeighth", "threeeighths", "fiveeighths", "seveneighths", "onethird", "twothirds", "zerosuperior", "foursuperior", "fivesuperior", "sixsuperior", "sevensuperior", "eightsuperior", "ninesuperior", "zeroinferior", "oneinferior", "twoinferior", "threeinferior", "fourinferior", "fiveinferior", "sixinferior", "seveninferior", "eightinferior", "nineinferior", "centinferior", "dollarinferior", "periodinferior", "commainferior", "Agravesmall", "Aacutesmall", "Acircumflexsmall", "Atildesmall", "Adieresissmall", "Aringsmall", "AEsmall", "Ccedillasmall", "Egravesmall", "Eacutesmall", "Ecircumflexsmall", "Edieresissmall", "Igravesmall", "Iacutesmall", "Icircumflexsmall", "Idieresissmall", "Ethsmall", "Ntildesmall", "Ogravesmall", "Oacutesmall", "Ocircumflexsmall", "Otildesmall", "Odieresissmall", "OEsmall", "Oslashsmall", "Ugravesmall", "Uacutesmall", "Ucircumflexsmall", "Udieresissmall", "Yacutesmall", "Thornsmall", "Ydieresissmall", "001.000", "001.001", "001.002", "001.003", "Black", "Bold", "Book", "Light", "Medium", "Regular", "Roman", "Semibold" };
  private static final int[] ISOAdobeCharset = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126, 127, 128, 129, 130, 131, 132, 133, 134, 135, 136, 137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 147, 148, 149, 150, 151, 152, 153, 154, 155, 156, 157, 158, 159, 160, 161, 162, 163, 164, 165, 166, 167, 168, 169, 170, 171, 172, 173, 174, 175, 176, 177, 178, 179, 180, 181, 182, 183, 184, 185, 186, 187, 188, 189, 190, 191, 192, 193, 194, 195, 196, 197, 198, 199, 200, 201, 202, 203, 204, 205, 206, 207, 208, 209, 210, 211, 212, 213, 214, 215, 216, 217, 218, 219, 220, 221, 222, 223, 224, 225, 226, 227, 228 };
  private static final int[] ExpertCharset = { 0, 1, 229, 230, 231, 232, 233, 234, 235, 236, 237, 238, 13, 14, 15, 99, 239, 240, 241, 242, 243, 244, 245, 246, 247, 248, 27, 28, 249, 250, 251, 252, 253, 254, 255, 256, 257, 258, 259, 260, 261, 262, 263, 264, 265, 266, 109, 110, 267, 268, 269, 270, 271, 272, 273, 274, 275, 276, 277, 278, 279, 280, 281, 282, 283, 284, 285, 286, 287, 288, 289, 290, 291, 292, 293, 294, 295, 296, 297, 298, 299, 300, 301, 302, 303, 304, 305, 306, 307, 308, 309, 310, 311, 312, 313, 314, 315, 316, 317, 318, 158, 155, 163, 319, 320, 321, 322, 323, 324, 325, 326, 150, 164, 169, 327, 328, 329, 330, 331, 332, 333, 334, 335, 336, 337, 338, 339, 340, 341, 342, 343, 344, 345, 346, 347, 348, 349, 350, 351, 352, 353, 354, 355, 356, 357, 358, 359, 360, 361, 362, 363, 364, 365, 366, 367, 368, 369, 370, 371, 372, 373, 374, 375, 376, 377, 378 };
  public static final int VERSION = 0;
  public static final int NOTICE = 1;
  public static final int FULLNAME = 2;
  public static final int FAMILYNAME = 3;
  public static final int WEIGHT = 4;
  public static final int FONTBBOX = 5;
  public static final int BLUEVALUES = 6;
  public static final int OTHERBLUES = 7;
  public static final int FAMILYBLUES = 8;
  public static final int FAMILYOTHERBLUES = 9;
  public static final int STDHW = 10;
  public static final int STDVW = 11;
  public static final int ESCAPE = 12;
  public static final int UNIQUEID = 13;
  public static final int XUID = 14;
  public static final int CHARSET = 15;
  public static final int ENCODING = 16;
  public static final int CHARSTRINGS = 17;
  public static final int PRIVATE = 18;
  public static final int SUBRS = 19;
  public static final int DEFAULTWIDTHX = 20;
  public static final int NOMINALWIDTHX = 21;
  public static final int RESERVED = 22;
  public static final int SHORTINT = 28;
  public static final int LONGINT = 29;
  public static final int BCD = 30;
  public static final int COPYRIGHT = 3072;
  public static final int ISFIXEDPITCH = 3073;
  public static final int ITALICANGLE = 3074;
  public static final int UNDERLINEPOSITION = 3075;
  public static final int UNDERLINETHICKNESS = 3076;
  public static final int PAINTTYPE = 3077;
  public static final int CHARSTRINGTYPE = 3078;
  public static final int FONTMATRIX = 3079;
  public static final int STROKEWIDTH = 3080;
  public static final int BLUESCALE = 3081;
  public static final int BLUESHIFT = 3082;
  public static final int BLUEFUZZ = 3083;
  public static final int STEMSNAPH = 3084;
  public static final int STEMSNAPV = 3085;
  public static final int FORCEBOLD = 3086;
  public static final int LANGUAGEGROUP = 3089;
  public static final int EXPANSIONFACTOR = 3090;
  public static final int INITIALRANDOMSEED = 3091;
  public static final int SYNTHETICBASE = 3092;
  public static final int POSTSCRIPT = 3093;
  public static final int BASEFONTNAME = 3094;
  public static final int BASEFONTBLEND = 3095;
  public static final int ROS = 3102;
  public static final int CIDFONTVERSION = 3103;
  public static final int CIDFONTREVISION = 3104;
  public static final int CIDFONTTYPE = 3105;
  public static final int CIDCOUNT = 3106;
  public static final int UIDBASE = 3107;
  public static final int FDARRAY = 3108;
  public static final int FDSELECT = 3109;
  public static final int FONTNAME = 3110;
  private int weight = 388;
  private int[] blueValues = null;
  private int[] otherBlues = null;
  private int[] familyBlues = null;
  private int[] familyOtherBlues = null;
  private int stdHW = -1;
  private int stdVW = -1;
  private int subrs = -1;
  private byte[] encodingDataBytes = null;
  private String[] stringIndexData = null;
  private int[] charsetGlyphCodes = null;
  private int charsetGlyphFormat = 0;
  private int[] rosArray = new int[3];

  public Type1C()
  {
  }

  public Type1C(PdfObjectReader paramPdfObjectReader, String paramString)
  {
    this.glyphs = new T1Glyphs(false);
    init(paramPdfObjectReader);
    this.substituteFont = paramString;
  }

  protected void readEmbeddedFont(PdfObject paramPdfObject)
    throws Exception
  {
    Object localObject3;
    Object localObject1;
    Object localObject2;
    if (this.substituteFont != null)
    {
      localObject3 = new ByteArrayOutputStream();
      InputStream localInputStream = null;
      try
      {
        if ((this.substituteFont.startsWith("jar:")) || (this.substituteFont.startsWith("http:")))
          localInputStream = this.loader.getResourceAsStream(this.substituteFont);
        else
          localInputStream = this.loader.getResourceAsStream(new StringBuilder().append("file:///").append(this.substituteFont).toString());
      }
      catch (Exception localException2)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog(new StringBuilder().append("3.Unable to open ").append(this.substituteFont).toString());
      }
      catch (Error localError)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog(new StringBuilder().append("3.Unable to open ").append(this.substituteFont).toString());
      }
      Object localObject4;
      if (localInputStream == null)
      {
        localObject4 = new File(this.substituteFont);
        FileInputStream localFileInputStream = new FileInputStream((File)localObject4);
        long l = ((File)localObject4).length();
        if (l > 2147483647L)
        {
          System.out.println("Sorry! Your given file is too large.");
          return;
        }
        localObject1 = new byte[(int)l];
        int j = 0;
        int k;
        while ((j < localObject1.length) && ((k = localFileInputStream.read((byte[])localObject1, j, localObject1.length - j)) >= 0))
          j += k;
        if (j < localObject1.length)
          throw new IOException(new StringBuilder().append("Could not completely read file ").append(((File)localObject4).getName()).toString());
        localFileInputStream.close();
      }
      else
      {
        localObject2 = new BufferedInputStream(localInputStream);
        localObject4 = new byte[65535];
        int i;
        while ((i = ((BufferedInputStream)localObject2).read((byte[])localObject4)) != -1)
          ((ByteArrayOutputStream)localObject3).write((byte[])localObject4, 0, i);
        ((ByteArrayOutputStream)localObject3).close();
        ((BufferedInputStream)localObject2).close();
        localObject1 = ((ByteArrayOutputStream)localObject3).toByteArray();
      }
      try
      {
        this.isFontSubstituted = true;
        readType1FontFile((byte[])localObject1);
      }
      catch (Exception localException3)
      {
        localException3.printStackTrace(System.out);
        if (LogWriter.isOutput())
          LogWriter.writeLog(new StringBuilder().append("[PDF]Substitute font=").append(this.substituteFont).append("Type 1 exception=").append(localException3).toString());
      }
      if ((this.isFontSubstituted) && (this.glyphs.remapFont))
        this.glyphs.remapFont = false;
    }
    else if (paramPdfObject != null)
    {
      localObject1 = paramPdfObject.getDictionary(746093177);
      if (localObject1 != null)
      {
        try
        {
          localObject2 = this.currentPdfFile.readStream((PdfObject)localObject1, true, true, false, false, false, ((PdfObject)localObject1).getCacheName(this.currentPdfFile.getObjectReader()));
          if (localObject2 != null)
            readType1FontFile((byte[])localObject2);
        }
        catch (Exception localException1)
        {
          if (LogWriter.isOutput())
            LogWriter.writeLog(new StringBuilder().append("Exception: ").append(localException1.getMessage()).toString());
        }
      }
      else
      {
        PdfObject localPdfObject = paramPdfObject.getDictionary(2021292335);
        if (localPdfObject != null)
        {
          localObject3 = this.currentPdfFile.readStream(localPdfObject, true, true, false, false, false, localPdfObject.getCacheName(this.currentPdfFile.getObjectReader()));
          if ((localObject3 != null) && ((localObject3.length <= 3) || (localObject3[0] != 719) || (localObject3[1] != 84) || (localObject3[2] != 84) || (localObject3[3] != 79)))
            readType1CFontFile((byte[])localObject3, null);
        }
      }
    }
  }

  public void createFont(PdfObject paramPdfObject, String paramString, boolean paramBoolean, ObjectStore paramObjectStore, Map paramMap)
    throws Exception
  {
    this.fontTypes = 1228944677;
    init(paramString, paramBoolean);
    PdfObject localPdfObject = paramPdfObject.getDictionary(-1044665361);
    setBoundsAndMatrix(localPdfObject);
    setName(paramPdfObject);
    setEncoding(paramPdfObject, localPdfObject);
    try
    {
      readEmbeddedFont(localPdfObject);
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog(new StringBuilder().append("Exception: ").append(localException.getMessage()).toString());
    }
    readWidths(paramPdfObject, true);
    if (paramBoolean)
      setFont(getBaseFontName(), 1);
  }

  public Type1C(byte[] paramArrayOfByte, PdfJavaGlyphs paramPdfJavaGlyphs, boolean paramBoolean)
    throws Exception
  {
    this.glyphs = paramPdfJavaGlyphs;
    this.trackIndices = true;
    this.renderPage = true;
    if (paramBoolean)
      readType1CFontFile(paramArrayOfByte, null);
    else
      readType1FontFile(paramArrayOfByte);
  }

  public Type1C(byte[] paramArrayOfByte, FontData paramFontData, PdfJavaGlyphs paramPdfJavaGlyphs)
    throws Exception
  {
    this.glyphs = paramPdfJavaGlyphs;
    readType1CFontFile(paramArrayOfByte, paramFontData);
  }

  private final void readType1CFontFile(byte[] paramArrayOfByte, FontData paramFontData)
    throws Exception
  {
    if (LogWriter.isOutput())
      LogWriter.writeLog("Embedded Type1C font used");
    this.glyphs.setis1C(true);
    int i = paramArrayOfByte != null ? 1 : 0;
    int k = 2;
    int m;
    int n;
    if (i != 0)
    {
      m = paramArrayOfByte[0];
      n = paramArrayOfByte[1];
    }
    else
    {
      m = paramFontData.getByte(0);
      n = paramFontData.getByte(1);
    }
    if (((m != 1) || (n != 0)) && (LogWriter.isOutput()))
      LogWriter.writeLog(new StringBuilder().append("1C  format ").append(m).append(':').append(n).append(" not fully supported").toString());
    if (i != 0)
      this.top = paramArrayOfByte[2];
    else
      this.top = paramFontData.getByte(2);
    int i1;
    int i2;
    if (i != 0)
    {
      i1 = getWord(paramArrayOfByte, this.top, k);
      i2 = paramArrayOfByte[(this.top + k)];
    }
    else
    {
      i1 = getWord(paramFontData, this.top, k);
      i2 = paramFontData.getByte(this.top + k);
    }
    this.top += k + 1;
    int j = this.top + (i1 + 1) * i2 - 1;
    if (i != 0)
      this.top = (j + getWord(paramArrayOfByte, this.top + i1 * i2, i2));
    else
      this.top = (j + getWord(paramFontData, this.top + i1 * i2, i2));
    if (i != 0)
    {
      i1 = getWord(paramArrayOfByte, this.top, k);
      i2 = paramArrayOfByte[(this.top + k)];
    }
    else
    {
      i1 = getWord(paramFontData, this.top, k);
      i2 = paramFontData.getByte(this.top + k);
    }
    this.top += k + 1;
    j = this.top + (i1 + 1) * i2 - 1;
    int i3;
    int i4;
    if (i != 0)
    {
      i3 = j + getWord(paramArrayOfByte, this.top, i2);
      i4 = j + getWord(paramArrayOfByte, this.top + i2, i2);
    }
    else
    {
      i3 = j + getWord(paramFontData, this.top, i2);
      i4 = j + getWord(paramFontData, this.top + i2, i2);
    }
    String[] arrayOfString = readStringIndex(paramArrayOfByte, paramFontData, j, i2, i1);
    readGlobalSubRoutines(paramArrayOfByte, paramFontData);
    decodeDictionary(paramArrayOfByte, paramFontData, i3, i4, arrayOfString);
    int i5;
    int i8;
    int i11;
    if (this.FDSelect != -1)
    {
      i5 = this.FDSelect;
      int i6;
      if (i != 0)
        i6 = getWord(paramArrayOfByte, i5, 1);
      else
        i6 = getWord(paramFontData, i5, 1);
      if (i != 0)
        i7 = getWord(paramArrayOfByte, this.charstrings, 2);
      else
        i7 = getWord(paramFontData, this.charstrings, 2);
      this.fdSelect = new int[i7];
      if (i6 == 0)
      {
        for (i8 = 0; i8 < i7; i8++)
          if (i != 0)
            this.fdSelect[i8] = getWord(paramArrayOfByte, i5 + 1 + i8, 1);
          else
            this.fdSelect[i8] = getWord(paramFontData, i5 + 1 + i8, 1);
      }
      else if (i6 == 3)
      {
        if (i != 0)
          i8 = getWord(paramArrayOfByte, i5 + 1, 2);
        else
          i8 = getWord(paramFontData, i5 + 1, 2);
        int[] arrayOfInt2 = new int[i8 + 1];
        int[] arrayOfInt3 = new int[i8];
        for (i11 = 0; i11 < i8; i11++)
          if (i != 0)
          {
            arrayOfInt2[i11] = getWord(paramArrayOfByte, i5 + 3 + 3 * i11, 2);
            arrayOfInt3[i11] = getWord(paramArrayOfByte, i5 + 5 + 3 * i11, 1);
          }
          else
          {
            arrayOfInt2[i11] = getWord(paramFontData, i5 + 3 + 3 * i11, 2);
            arrayOfInt3[i11] = getWord(paramFontData, i5 + 5 + 3 * i11, 1);
          }
        arrayOfInt2[(arrayOfInt2.length - 1)] = i7;
        for (i11 = 0; i11 < i8; i11++)
          for (int i12 = arrayOfInt2[i11]; i12 < arrayOfInt2[(i11 + 1)]; i12++)
            this.fdSelect[i12] = arrayOfInt3[i11];
      }
      ((T1Glyphs)this.glyphs).setFDSelect(this.fdSelect);
      i5 = this.FDArray;
      if (i != 0)
      {
        i1 = getWord(paramArrayOfByte, i5, k);
        i2 = paramArrayOfByte[(i5 + k)];
      }
      else
      {
        i1 = getWord(paramFontData, i5, k);
        i2 = paramFontData.getByte(i5 + k);
      }
      i5 += k + 1;
      j = i5 + (i1 + 1) * i2 - 1;
      this.privateDict = new int[i1];
      this.privateDictOffset = new int[i1];
      this.defaultWidthX = new int[i1];
      this.nominalWidthX = new int[i1];
      for (i8 = 0; i8 < i1; i8++)
      {
        this.currentFD = i8;
        this.privateDict[i8] = -1;
        this.privateDictOffset[i8] = -1;
        if (i != 0)
        {
          i3 = j + getWord(paramArrayOfByte, i5 + i8 * i2, i2);
          i4 = j + getWord(paramArrayOfByte, i5 + (i8 + 1) * i2, i2);
        }
        else
        {
          i3 = j + getWord(paramFontData, i5 + i8 * i2, i2);
          i4 = j + getWord(paramFontData, i5 + (i8 + 1) * i2, i2);
        }
        decodeDictionary(paramArrayOfByte, paramFontData, i3, i4, arrayOfString);
      }
      this.currentFD = -1;
    }
    this.top = this.charstrings;
    if (i != 0)
      i5 = getWord(paramArrayOfByte, this.top, k);
    else
      i5 = getWord(paramFontData, this.top, k);
    this.glyphs.setGlyphCount(i5);
    int[] arrayOfInt1 = readCharset(this.charset, i5, paramFontData, paramArrayOfByte);
    setEncoding(paramArrayOfByte, paramFontData, i5, arrayOfInt1);
    this.top = this.charstrings;
    readGlyphs(paramArrayOfByte, paramFontData, i5, arrayOfInt1);
    for (int i7 = 0; i7 < this.privateDict.length; i7++)
    {
      this.currentFD = i7;
      i8 = this.privateDict[i7];
      if (i8 != -1)
      {
        int i9 = this.privateDictOffset[i7];
        decodeDictionary(paramArrayOfByte, paramFontData, i8, i9 + i8, arrayOfString);
        this.top = (i8 + i9);
        int i10;
        if (i != 0)
          i10 = paramArrayOfByte.length;
        else
          i10 = paramFontData.length();
        if (this.top + 2 < i10)
        {
          if (i != 0)
            i11 = getWord(paramArrayOfByte, this.top, k);
          else
            i11 = getWord(paramFontData, this.top, k);
          if (i11 > 0)
            readSubrs(paramArrayOfByte, paramFontData, i11);
        }
      }
    }
    this.currentFD = -1;
    this.isFontEmbedded = true;
    this.glyphs.setFontEmbedded(true);
  }

  private final void setEncoding(byte[] paramArrayOfByte, FontData paramFontData, int paramInt, int[] paramArrayOfInt)
  {
    int i = paramArrayOfByte != null ? 1 : 0;
    int k;
    if (this.enc == 0)
    {
      this.embeddedEnc = 1;
      if (this.fontEnc == -1)
        putFontEncoding(1);
      if (this.isCID)
        try
        {
          for (k = 1; k < paramInt; k++)
            if (paramArrayOfInt[k] < 391)
            {
              String str1;
              if (i != 0)
                str1 = getString(paramArrayOfByte, paramArrayOfInt[k], this.stringIdx, this.stringStart, this.stringOffSize);
              else
                str1 = getString(paramFontData, paramArrayOfInt[k], this.stringIdx, this.stringStart, this.stringOffSize);
              putMappedChar(paramArrayOfInt[k], StandardFonts.getUnicodeName(str1));
            }
        }
        catch (Exception localException)
        {
          if (LogWriter.isOutput())
            LogWriter.writeLog(new StringBuilder().append("Exception: ").append(localException.getMessage()).toString());
        }
    }
    else if (this.enc == 1)
    {
      this.embeddedEnc = 3;
      if (this.fontEnc == -1)
        putFontEncoding(3);
    }
    else
    {
      this.top = this.enc;
      int j;
      if (i != 0)
        j = paramArrayOfByte[(this.top++)] & 0xFF;
      else
        j = paramFontData.getByte(this.top++) & 0xFF;
      int m;
      int n;
      String str2;
      int i1;
      if ((j & 0x7F) == 0)
      {
        if (i != 0)
          m = 1 + (paramArrayOfByte[(this.top++)] & 0xFF);
        else
          m = 1 + (paramFontData.getByte(this.top++) & 0xFF);
        if (m > paramInt)
          m = paramInt;
        for (n = 1; n < m; n++)
        {
          if (i != 0)
          {
            k = paramArrayOfByte[(this.top++)] & 0xFF;
            str2 = getString(paramArrayOfByte, paramArrayOfInt[n], this.stringIdx, this.stringStart, this.stringOffSize);
          }
          else
          {
            k = paramFontData.getByte(this.top++) & 0xFF;
            str2 = getString(paramFontData, paramArrayOfInt[n], this.stringIdx, this.stringStart, this.stringOffSize);
          }
          putChar(k, str2);
        }
      }
      else if ((j & 0x7F) == 1)
      {
        if (i != 0)
          m = paramArrayOfByte[(this.top++)] & 0xFF;
        else
          m = paramFontData.getByte(this.top++) & 0xFF;
        n = 1;
        for (i1 = 0; i1 < m; i1++)
        {
          int i2;
          if (i != 0)
          {
            k = paramArrayOfByte[(this.top++)] & 0xFF;
            i2 = paramArrayOfByte[(this.top++)] & 0xFF;
          }
          else
          {
            k = paramFontData.getByte(this.top++) & 0xFF;
            i2 = paramFontData.getByte(this.top++) & 0xFF;
          }
          for (int i3 = 0; (i3 <= i2) && (n < paramInt); i3++)
          {
            if (i != 0)
              str2 = getString(paramArrayOfByte, paramArrayOfInt[n], this.stringIdx, this.stringStart, this.stringOffSize);
            else
              str2 = getString(paramFontData, paramArrayOfInt[n], this.stringIdx, this.stringStart, this.stringOffSize);
            putChar(k, str2);
            n++;
            k++;
          }
        }
      }
      if ((j & 0x80) != 0)
      {
        if (i != 0)
          m = paramArrayOfByte[(this.top++)] & 0xFF;
        else
          m = paramFontData.getByte(this.top++) & 0xFF;
        for (n = 0; n < m; n++)
        {
          if (i != 0)
            k = paramArrayOfByte[(this.top++)] & 0xFF;
          else
            k = paramFontData.getByte(this.top++) & 0xFF;
          if (i != 0)
            i1 = getWord(paramArrayOfByte, this.top, 2);
          else
            i1 = getWord(paramFontData, this.top, 2);
          this.top += 2;
          if (i != 0)
            str2 = getString(paramArrayOfByte, i1, this.stringIdx, this.stringStart, this.stringOffSize);
          else
            str2 = getString(paramFontData, i1, this.stringIdx, this.stringStart, this.stringOffSize);
          putChar(k, str2);
        }
      }
    }
  }

  private final void readSubrs(byte[] paramArrayOfByte, FontData paramFontData, int paramInt)
    throws Exception
  {
    int i = paramArrayOfByte != null ? 1 : 0;
    int j;
    if (i != 0)
      j = paramArrayOfByte[(this.top + 2)];
    else
      j = paramFontData.getByte(this.top + 2);
    this.top += 3;
    int k = this.top;
    int m = this.top + (paramInt + 1) * j - 1;
    int n = this.top + paramInt * j;
    if (i != 0)
    {
      if (n < paramArrayOfByte.length)
        this.top = (m + getWord(paramArrayOfByte, n, j));
      else
        this.top = (paramArrayOfByte.length - 1);
    }
    else if (n < paramArrayOfByte.length)
      this.top = (m + getWord(paramFontData, n, j));
    else
      this.top = (paramFontData.length() - 1);
    int[] arrayOfInt = new int[paramInt + 2];
    int i1 = k;
    for (int i2 = 0; i2 < paramInt + 1; i2++)
    {
      if (i != 0)
      {
        if (i1 + j < paramArrayOfByte.length)
          arrayOfInt[i2] = (m + getWord(paramArrayOfByte, i1, j));
      }
      else if (i1 + j < paramFontData.length())
        arrayOfInt[i2] = (m + getWord(paramFontData, i1, j));
      i1 += j;
    }
    arrayOfInt[(paramInt + 1)] = this.top;
    this.glyphs.setLocalBias(calculateSubroutineBias(paramInt));
    i2 = arrayOfInt[0];
    for (int i3 = 1; i3 < paramInt + 1; i3++)
      if ((i2 != 0) && (arrayOfInt[i3] <= paramArrayOfByte.length) && (arrayOfInt[i3] >= 0) && (arrayOfInt[i3] != 0))
      {
        ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
        for (int i4 = i2; i4 < arrayOfInt[i3]; i4++)
          if ((i == 0) && (i4 < paramFontData.length()))
            localByteArrayOutputStream.write(paramFontData.getByte(i4));
        if (i != 0)
        {
          i4 = arrayOfInt[i3] - i2;
          if (i4 > 0)
          {
            byte[] arrayOfByte = new byte[i4];
            System.arraycopy(paramArrayOfByte, i2, arrayOfByte, 0, i4);
            this.glyphs.setCharString(new StringBuilder().append("subrs").append(i3 - 1).toString(), arrayOfByte, i3);
          }
        }
        else
        {
          localByteArrayOutputStream.close();
          this.glyphs.setCharString(new StringBuilder().append("subrs").append(i3 - 1).toString(), localByteArrayOutputStream.toByteArray(), i3);
        }
        i2 = arrayOfInt[i3];
      }
  }

  private final void readGlyphs(byte[] paramArrayOfByte, FontData paramFontData, int paramInt, int[] paramArrayOfInt)
    throws Exception
  {
    int i = paramArrayOfByte != null ? 1 : 0;
    int j;
    if (i != 0)
      j = paramArrayOfByte[(this.top + 2)];
    else
      j = paramFontData.getByte(this.top + 2);
    this.top += 3;
    int k = this.top;
    int m = this.top + (paramInt + 1) * j - 1;
    if (i != 0)
      this.top = (m + getWord(paramArrayOfByte, this.top + paramInt * j, j));
    else
      this.top = (m + getWord(paramFontData, this.top + paramInt * j, j));
    int[] arrayOfInt = new int[paramInt + 2];
    int n = k;
    for (int i1 = 0; i1 < paramInt + 1; i1++)
    {
      if (i != 0)
        arrayOfInt[i1] = (m + getWord(paramArrayOfByte, n, j));
      else
        arrayOfInt[i1] = (m + getWord(paramFontData, n, j));
      n += j;
    }
    arrayOfInt[(paramInt + 1)] = this.top;
    i1 = arrayOfInt[0];
    for (int i2 = 1; i2 < paramInt + 1; i2++)
    {
      byte[] arrayOfByte = new byte[arrayOfInt[i2] - i1];
      for (int i3 = i1; i3 < arrayOfInt[i2]; i3++)
        if (i != 0)
          arrayOfByte[(i3 - i1)] = paramArrayOfByte[i3];
        else
          arrayOfByte[(i3 - i1)] = paramFontData.getByte(i3);
      String str;
      if (this.isCID)
        str = String.valueOf(paramArrayOfInt[(i2 - 1)]);
      else if (i != 0)
        str = getString(paramArrayOfByte, paramArrayOfInt[(i2 - 1)], this.stringIdx, this.stringStart, this.stringOffSize);
      else
        str = getString(paramFontData, paramArrayOfInt[(i2 - 1)], this.stringIdx, this.stringStart, this.stringOffSize);
      this.glyphs.setCharString(str, arrayOfByte, i2);
      i1 = arrayOfInt[i2];
      if (this.trackIndices)
        this.glyphs.setIndexForCharString(i2, str);
    }
  }

  private static final int calculateSubroutineBias(int paramInt)
  {
    int i;
    if (paramInt < 1240)
      i = 107;
    else if (paramInt < 33900)
      i = 1131;
    else
      i = 32768;
    return i;
  }

  private final void readGlobalSubRoutines(byte[] paramArrayOfByte, FontData paramFontData)
    throws Exception
  {
    int i = paramArrayOfByte != null ? 1 : 0;
    int j;
    int k;
    if (i != 0)
    {
      j = paramArrayOfByte[(this.top + 2)] & 0xFF;
      k = getWord(paramArrayOfByte, this.top, 2);
    }
    else
    {
      j = paramFontData.getByte(this.top + 2) & 0xFF;
      k = getWord(paramFontData, this.top, 2);
    }
    this.top += 3;
    if (k > 0)
    {
      int m = this.top;
      int n = this.top + (k + 1) * j - 1;
      if (i != 0)
        this.top = (n + getWord(paramArrayOfByte, this.top + k * j, j));
      else
        this.top = (n + getWord(paramFontData, this.top + k * j, j));
      int[] arrayOfInt = new int[k + 2];
      int i1 = m;
      for (int i2 = 0; i2 < k + 1; i2++)
      {
        if (i != 0)
          arrayOfInt[i2] = (n + getWord(paramArrayOfByte, i1, j));
        else
          arrayOfInt[i2] = (n + getWord(paramFontData, i1, j));
        i1 += j;
      }
      arrayOfInt[(k + 1)] = this.top;
      this.glyphs.setGlobalBias(calculateSubroutineBias(k));
      i2 = arrayOfInt[0];
      for (int i3 = 1; i3 < k + 1; i3++)
      {
        ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
        for (int i4 = i2; i4 < arrayOfInt[i3]; i4++)
          if (i != 0)
            localByteArrayOutputStream.write(paramArrayOfByte[i4]);
          else
            localByteArrayOutputStream.write(paramFontData.getByte(i4));
        localByteArrayOutputStream.close();
        this.glyphs.setCharString(new StringBuilder().append("global").append(i3 - 1).toString(), localByteArrayOutputStream.toByteArray(), i3);
        i2 = arrayOfInt[i3];
      }
    }
  }

  private void decodeDictionary(byte[] paramArrayOfByte, FontData paramFontData, int paramInt1, int paramInt2, String[] paramArrayOfString)
  {
    int i = 0;
    int j = paramArrayOfByte != null ? 1 : 0;
    int k = paramInt1;
    int i1 = 0;
    double[] arrayOfDouble = new double[48];
    while (k < paramInt2)
    {
      int m;
      if (j != 0)
        m = paramArrayOfByte[k] & 0xFF;
      else
        m = paramFontData.getByte(k) & 0xFF;
      if ((m <= 27) || (m == 31))
      {
        int n = m;
        k++;
        int i2;
        if (n == 12)
        {
          if (j != 0)
            n = paramArrayOfByte[k] & 0xFF;
          else
            n = paramFontData.getByte(k) & 0xFF;
          k++;
          if ((n == 36) || (n == 37) || (n == 7) || (this.FDSelect == -1))
            if (n == 2)
            {
              this.italicAngle = ((int)arrayOfDouble[0]);
            }
            else if (n == 7)
            {
              if (!this.hasFontMatrix)
                System.arraycopy(arrayOfDouble, 0, this.FontMatrix, 0, 6);
              this.hasFontMatrix = true;
            }
            else if (n == 6)
            {
              if (arrayOfDouble[0] > 0.0D)
              {
                this.blueValues = new int[6];
                for (i2 = 0; i2 < this.blueValues.length; i2++)
                  this.blueValues[i2] = ((int)arrayOfDouble[i2]);
              }
            }
            else if (n == 8)
            {
              this.familyBlues = new int[6];
              for (i2 = 0; i2 < this.familyBlues.length; i2++)
                this.familyBlues[i2] = ((int)arrayOfDouble[i2]);
            }
            else if (n == 9)
            {
              if (arrayOfDouble[0] > 0.0D)
              {
                this.familyOtherBlues = new int[6];
                for (i2 = 0; i2 < this.familyOtherBlues.length; i2++)
                  this.familyOtherBlues[i2] = ((int)arrayOfDouble[i2]);
              }
            }
            else if (n == 10)
            {
              this.stdHW = ((int)arrayOfDouble[0]);
            }
            else if (n == 11)
            {
              this.stdVW = ((int)arrayOfDouble[0]);
            }
            else if (n == 30)
            {
              this.ros = ((int)arrayOfDouble[0]);
              this.isCID = true;
            }
            else if (n == 31)
            {
              this.CIDFontVersion = ((int)arrayOfDouble[0]);
            }
            else if (n == 32)
            {
              this.CIDFontRevision = ((int)arrayOfDouble[0]);
            }
            else if (n == 33)
            {
              this.CIDFontType = ((int)arrayOfDouble[0]);
            }
            else if (n == 34)
            {
              this.CIDcount = ((int)arrayOfDouble[0]);
            }
            else if (n == 35)
            {
              this.UIDBase = ((int)arrayOfDouble[0]);
            }
            else if (n == 36)
            {
              this.FDArray = ((int)arrayOfDouble[0]);
            }
            else if (n == 37)
            {
              this.FDSelect = ((int)arrayOfDouble[0]);
              i = 1;
            }
            else if (n == 0)
            {
              i2 = (int)arrayOfDouble[0];
              if (i2 > 390)
                i2 -= 390;
              this.copyright = paramArrayOfString[i2];
            }
            else if ((n == 21) || (n == 22) || (n != 38));
        }
        else if (n == 2)
        {
          i2 = (int)arrayOfDouble[0];
          if (i2 > 390)
            i2 -= 390;
          this.embeddedFontName = paramArrayOfString[i2];
        }
        else if (n != 3)
        {
          if (n == 5)
          {
            for (i2 = 0; i2 < 4; i2++)
              this.FontBBox[i2] = ((float)arrayOfDouble[i2]);
          }
          else if (n == 15)
          {
            this.charset = ((int)arrayOfDouble[0]);
          }
          else if (n == 16)
          {
            this.enc = ((int)arrayOfDouble[0]);
          }
          else if (n == 17)
          {
            this.charstrings = ((int)arrayOfDouble[0]);
          }
          else if ((n == 18) && (this.glyphs.is1C()))
          {
            i2 = this.currentFD;
            if (i2 == -1)
              i2 = 0;
            this.privateDict[i2] = ((int)arrayOfDouble[1]);
            this.privateDictOffset[i2] = ((int)arrayOfDouble[0]);
          }
          else if (n == 20)
          {
            i2 = this.currentFD;
            if (i2 == -1)
              i2 = 0;
            this.defaultWidthX[i2] = ((int)arrayOfDouble[0]);
            if ((this.glyphs instanceof T1Glyphs))
              ((T1Glyphs)this.glyphs).setWidthValues(this.defaultWidthX, this.nominalWidthX);
          }
          else if (n == 21)
          {
            i2 = this.currentFD;
            if (i2 == -1)
              i2 = 0;
            this.nominalWidthX[i2] = ((int)arrayOfDouble[0]);
            if ((this.glyphs instanceof T1Glyphs))
              ((T1Glyphs)this.glyphs).setWidthValues(this.defaultWidthX, this.nominalWidthX);
          }
        }
        i1 = 0;
      }
      else
      {
        if (j != 0)
          k = this.glyphs.getNumber(paramArrayOfByte, k, arrayOfDouble, i1, false);
        else
          k = this.glyphs.getNumber(paramFontData, k, arrayOfDouble, i1, false);
        i1++;
      }
    }
    if (i == 0)
      this.FDSelect = -1;
  }

  private String[] readStringIndex(byte[] paramArrayOfByte, FontData paramFontData, int paramInt1, int paramInt2, int paramInt3)
  {
    int j = paramArrayOfByte != null ? 1 : 0;
    int i;
    if (j != 0)
    {
      this.top = (paramInt1 + getWord(paramArrayOfByte, this.top + paramInt3 * paramInt2, paramInt2));
      i = getWord(paramArrayOfByte, this.top, 2);
      this.stringOffSize = paramArrayOfByte[(this.top + 2)];
    }
    else
    {
      this.top = (paramInt1 + getWord(paramFontData, this.top + paramInt3 * paramInt2, paramInt2));
      i = getWord(paramFontData, this.top, 2);
      this.stringOffSize = paramFontData.getByte(this.top + 2);
    }
    this.top += 3;
    this.stringIdx = this.top;
    this.stringStart = (this.top + (i + 1) * this.stringOffSize - 1);
    if (j != 0)
      this.top = (this.stringStart + getWord(paramArrayOfByte, this.top + i * this.stringOffSize, this.stringOffSize));
    else
      this.top = (this.stringStart + getWord(paramFontData, this.top + i * this.stringOffSize, this.stringOffSize));
    int[] arrayOfInt = new int[i + 2];
    String[] arrayOfString = new String[i + 2];
    int k = this.stringIdx;
    for (int m = 0; m < i + 1; m++)
    {
      if (j != 0)
        arrayOfInt[m] = getWord(paramArrayOfByte, k, this.stringOffSize);
      else
        arrayOfInt[m] = getWord(paramFontData, k, this.stringOffSize);
      k += this.stringOffSize;
    }
    arrayOfInt[(i + 1)] = (this.top - this.stringStart);
    m = 0;
    for (int n = 0; n < i + 1; n++)
    {
      StringBuilder localStringBuilder = new StringBuilder(arrayOfInt[n] - m);
      for (int i1 = m; i1 < arrayOfInt[n]; i1++)
        if (j != 0)
          localStringBuilder.append((char)paramArrayOfByte[(this.stringStart + i1)]);
        else
          localStringBuilder.append((char)paramFontData.getByte(this.stringStart + i1));
      arrayOfString[n] = localStringBuilder.toString();
      m = arrayOfInt[n];
    }
    return arrayOfString;
  }

  private static final String getString(FontData paramFontData, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    String str;
    if (paramInt1 < 391)
    {
      str = type1CStdStrings[paramInt1];
    }
    else
    {
      paramInt1 -= 391;
      int j = paramInt3 + getWord(paramFontData, paramInt2 + paramInt1 * paramInt4, paramInt4);
      int k = paramInt3 + getWord(paramFontData, paramInt2 + (paramInt1 + 1) * paramInt4, paramInt4);
      int i;
      if ((i = k - j) > 255)
        i = 255;
      str = new String(paramFontData.getBytes(j, i));
    }
    return str;
  }

  private static final String getString(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    String str;
    if (paramInt1 < 391)
    {
      str = type1CStdStrings[paramInt1];
    }
    else
    {
      paramInt1 -= 391;
      int j = paramInt3 + getWord(paramArrayOfByte, paramInt2 + paramInt1 * paramInt4, paramInt4);
      int k = paramInt3 + getWord(paramArrayOfByte, paramInt2 + (paramInt1 + 1) * paramInt4, paramInt4);
      int i;
      if ((i = k - j) > 255)
        i = 255;
      str = new String(paramArrayOfByte, j, i);
    }
    return str;
  }

  private static final int[] readCharset(int paramInt1, int paramInt2, FontData paramFontData, byte[] paramArrayOfByte)
  {
    int i = paramArrayOfByte != null ? 1 : 0;
    int[] arrayOfInt;
    if (paramInt1 == 0)
    {
      arrayOfInt = ISOAdobeCharset;
    }
    else if (paramInt1 == 1)
    {
      arrayOfInt = ExpertCharset;
    }
    else if (paramInt1 == 2)
    {
      arrayOfInt = ExpertSubCharset;
    }
    else
    {
      arrayOfInt = new int[paramInt2 + 1];
      arrayOfInt[0] = 0;
      int m = paramInt1;
      int n;
      if (i != 0)
        n = paramArrayOfByte[(m++)] & 0xFF;
      else
        n = paramFontData.getByte(m++) & 0xFF;
      int j;
      if (n == 0)
        for (j = 1; j < paramInt2; j++)
        {
          if (i != 0)
            arrayOfInt[j] = getWord(paramArrayOfByte, m, 2);
          else
            arrayOfInt[j] = getWord(paramFontData, m, 2);
          m += 2;
        }
      int i1;
      int i2;
      int k;
      if (n == 1)
      {
        j = 1;
        while (j < paramInt2)
        {
          if (i != 0)
            i1 = getWord(paramArrayOfByte, m, 2);
          else
            i1 = getWord(paramFontData, m, 2);
          m += 2;
          if (i != 0)
            i2 = paramArrayOfByte[(m++)] & 0xFF;
          else
            i2 = paramFontData.getByte(m++) & 0xFF;
          for (k = 0; k <= i2; k++)
            arrayOfInt[(j++)] = (i1++);
        }
      }
      else if (n == 2)
      {
        j = 1;
        while (j < paramInt2)
        {
          if (i != 0)
            i1 = getWord(paramArrayOfByte, m, 2);
          else
            i1 = getWord(paramFontData, m, 2);
          m += 2;
          if (i != 0)
            i2 = getWord(paramArrayOfByte, m, 2);
          else
            i2 = getWord(paramFontData, m, 2);
          m += 2;
          for (k = 0; k <= i2; k++)
            arrayOfInt[(j++)] = (i1++);
        }
      }
    }
    return arrayOfInt;
  }

  private static final int getWord(FontData paramFontData, int paramInt1, int paramInt2)
  {
    int i = 0;
    for (int j = 0; j < paramInt2; j++)
      i = (i << 8) + (paramFontData.getByte(paramInt1 + j) & 0xFF);
    return i;
  }

  private static final int getWord(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    int i = 0;
    for (int j = 0; j < paramInt2; j++)
      i = (i << 8) + (paramArrayOfByte[(paramInt1 + j)] & 0xFF);
    return i;
  }

  public Rectangle getBoundingBox()
  {
    if (this.BBox == null)
      if (this.isFontEmbedded)
        this.BBox = new Rectangle((int)this.FontBBox[0], (int)this.FontBBox[1], (int)(this.FontBBox[2] - this.FontBBox[0]), (int)(this.FontBBox[3] - this.FontBBox[1]));
      else
        this.BBox = super.getBoundingBox();
    return this.BBox;
  }

  public int[] getFDSelect()
  {
    return this.fdSelect;
  }

  public int[] getRosArray()
  {
    return this.rosArray;
  }

  public static byte[] getOperatorBytes(int paramInt)
  {
    byte[] arrayOfByte = null;
    if ((paramInt <= 30) && (paramInt >= 0))
    {
      arrayOfByte = new byte[] { (byte)paramInt };
      return arrayOfByte;
    }
    if ((paramInt >= 3072) && (paramInt <= 3110))
    {
      arrayOfByte = FontWriter.setNextInt16(paramInt);
      return arrayOfByte;
    }
    return arrayOfByte;
  }

  public Object getKeyValue(int paramInt)
  {
    switch (paramInt)
    {
    case 4:
      return Integer.valueOf(this.weight);
    case 3074:
      return Integer.valueOf(this.italicAngle);
    case 3079:
      return this.FontMatrix;
    case 5:
      return this.FontBBox;
    case 16:
      return Integer.valueOf(this.enc);
    case 20:
      return Integer.valueOf(this.defaultWidthX[0]);
    case 21:
      return Integer.valueOf(this.nominalWidthX[0]);
    case 6:
      return this.blueValues;
    case 7:
      return this.otherBlues;
    case 8:
      return this.familyBlues;
    case 9:
      return this.familyOtherBlues;
    case 10:
      return Integer.valueOf(this.stdHW);
    case 11:
      return Integer.valueOf(this.stdVW);
    case 19:
      return Integer.valueOf(this.subrs);
    case 3102:
      return Integer.valueOf(this.ros);
    case 3106:
      return Integer.valueOf(this.CIDcount);
    case 3104:
      return Integer.valueOf(this.CIDFontRevision);
    case 3103:
      return Integer.valueOf(this.CIDFontVersion);
    case 3105:
      return Integer.valueOf(this.CIDFontType);
    case 3108:
      return Integer.valueOf(this.FDArray);
    case 3109:
      return Integer.valueOf(this.FDSelect);
    }
    throw new RuntimeException(new StringBuilder().append("Key is unknown or value is not yet assigned ").append(paramInt).toString());
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.fonts.Type1C
 * JD-Core Version:    0.6.2
 */