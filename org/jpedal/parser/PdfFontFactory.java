package org.jpedal.parser;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.jpedal.exception.PdfException;
import org.jpedal.exception.PdfFontException;
import org.jpedal.fonts.FontMappings;
import org.jpedal.fonts.PdfFont;
import org.jpedal.fonts.StandardFonts;
import org.jpedal.io.ErrorTracker;
import org.jpedal.io.ObjectStore;
import org.jpedal.io.PdfObjectReader;
import org.jpedal.objects.raw.PdfObject;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.StringUtils;

public class PdfFontFactory
{
  private boolean hasEmbeddedFonts = false;
  private boolean hasNonEmbeddedCIDFonts = false;
  private String fontsInFile;
  private StringBuilder nonEmbeddedCIDFonts = new StringBuilder(200);
  private String baseFont = "";
  private String rawFontName = null;
  private String subFont = null;
  private int origfontType;
  private HashMap fontsLoaded = new HashMap(50);
  PdfObjectReader currentPdfFile;

  public PdfFontFactory(PdfObjectReader paramPdfObjectReader)
  {
    this.currentPdfFile = paramPdfObjectReader;
  }

  public PdfFont createFont(PdfObject paramPdfObject, String paramString, ObjectStore paramObjectStore, boolean paramBoolean1, ErrorTracker paramErrorTracker, boolean paramBoolean2)
    throws PdfException
  {
    PdfFont localPdfFont = null;
    this.baseFont = "";
    this.rawFontName = null;
    this.subFont = null;
    int i = -1;
    this.origfontType = -1;
    PdfObject localPdfObject1 = paramPdfObject.getDictionary(-1547306032);
    boolean bool = isFontEmbedded(paramPdfObject);
    int j = 1;
    while (j != 0)
    {
      j = 0;
      if ((FontMappings.fontSubstitutionTable != null) && (!bool) && (paramPdfObject.getParameterConstant(1147962727) != 1228944679))
        i = getFontMapping(paramPdfObject, paramString, i, localPdfObject1);
      if (i == -1)
      {
        i = paramPdfObject.getParameterConstant(1147962727);
        if (i == 1228944676)
        {
          PdfObject localPdfObject2 = paramPdfObject.getDictionary(-1547306032);
          i = localPdfObject2.getParameterConstant(1147962727);
          this.origfontType = i;
        }
      }
      if (i == -1)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog("Font type not supported");
        localPdfFont = new PdfFont(this.currentPdfFile);
      }
      if ((i == 1228944677) || (i == -1684566724))
        i = scanForOpenType(paramPdfObject, this.currentPdfFile, i);
      try
      {
        localPdfFont = FontFactory.createFont(i, this.currentPdfFile, this.subFont, paramBoolean2);
        if (FontMappings.defaultFont != null)
          localPdfFont.setDefaultDisplayFont(FontMappings.defaultFont);
        localPdfFont.createFont(paramPdfObject, paramString, paramBoolean1, paramObjectStore, this.fontsLoaded);
        if (((i == -1684566726) || (i == -1684566724)) && (!bool) && (this.subFont == null))
        {
          this.subFont = localPdfFont.getSubstituteFont();
          if (this.subFont == null)
          {
            this.hasNonEmbeddedCIDFonts = true;
            if (this.nonEmbeddedCIDFonts.length() > 0)
              this.nonEmbeddedCIDFonts.append(',');
            this.nonEmbeddedCIDFonts.append(this.baseFont);
          }
        }
        localPdfFont.setRawFontName(this.rawFontName);
        if ((i == 1228944677) && (localPdfFont.is1C()) && (paramPdfObject.getInt(1283093660) == 32) && (paramPdfObject.getInt(1283093660) == paramPdfObject.getInt(795440262)))
          if (bool)
          {
            j = 1;
            bool = false;
          }
          else
          {
            localPdfFont.isFontEmbedded = false;
          }
        if ((!localPdfFont.isFontEmbedded) && (bool))
        {
          j = 1;
          bool = false;
        }
      }
      catch (Exception localException)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog(new StringBuilder().append("[PDF] Problem ").append(localException).append(" reading Font  type ").append(StandardFonts.getFontypeAsString(i)).toString());
        paramErrorTracker.addPageFailureMessage(new StringBuilder().append("Problem ").append(localException).append(" reading Font type ").append(StandardFonts.getFontypeAsString(i)).toString());
      }
    }
    setDetails(paramString, localPdfFont, i, localPdfObject1);
    return localPdfFont;
  }

  private void setDetails(String paramString, PdfFont paramPdfFont, int paramInt, PdfObject paramPdfObject)
  {
    String str1 = paramPdfFont.getFontName();
    if (str1.indexOf(35) != -1)
      str1 = StringUtils.convertHexChars(str1);
    String str2;
    if (paramPdfFont.isFontSubstituted())
    {
      str2 = new StringBuilder().append(paramString).append("  ").append(str1).append("  ").append(StandardFonts.getFontypeAsString(this.origfontType)).append("  Substituted (").append(this.subFont).append(' ').append(StandardFonts.getFontypeAsString(paramInt)).append(')').toString();
    }
    else if (paramPdfFont.isFontEmbedded)
    {
      this.hasEmbeddedFonts = true;
      if ((paramPdfFont.is1C()) && (paramPdfObject == null))
        str2 = new StringBuilder().append(paramString).append("  ").append(str1).append(" Type1C  Embedded").toString();
      else
        str2 = new StringBuilder().append(paramString).append("  ").append(str1).append("  ").append(StandardFonts.getFontypeAsString(paramInt)).append("  Embedded").toString();
    }
    else
    {
      str2 = new StringBuilder().append(paramString).append("  ").append(str1).append("  ").append(StandardFonts.getFontypeAsString(paramInt)).toString();
    }
    if (this.fontsInFile == null)
      this.fontsInFile = str2;
    else
      this.fontsInFile = new StringBuilder().append(str2).append('\n').append(this.fontsInFile).toString();
  }

  private static int scanForOpenType(PdfObject paramPdfObject, PdfObjectReader paramPdfObjectReader, int paramInt)
  {
    PdfObject localPdfObject1;
    PdfObject localPdfObject2;
    Object localObject;
    if (paramInt == -1684566724)
    {
      localPdfObject1 = paramPdfObject.getDictionary(-1547306032);
      if (paramPdfObject != null)
      {
        localPdfObject2 = localPdfObject1.getDictionary(-1044665361);
        if (localPdfObject2 != null)
        {
          localObject = localPdfObject2.getDictionary(2021292334);
          if (localObject != null)
          {
            byte[] arrayOfByte = paramPdfObjectReader.readStream((PdfObject)localObject, true, true, false, false, false, ((PdfObject)localObject).getCacheName(paramPdfObjectReader.getObjectReader()));
            if ((arrayOfByte != null) && (arrayOfByte.length > 3) && (arrayOfByte[0] == 79) && (arrayOfByte[1] == 84) && (arrayOfByte[2] == 84) && (arrayOfByte[3] == 79))
              paramInt = -1684566726;
          }
        }
      }
    }
    else
    {
      localPdfObject1 = paramPdfObject.getDictionary(-1044665361);
      if (localPdfObject1 != null)
      {
        localPdfObject2 = localPdfObject1.getDictionary(2021292335);
        if (localPdfObject2 != null)
        {
          localObject = paramPdfObjectReader.readStream(localPdfObject2, true, true, false, false, false, localPdfObject2.getCacheName(paramPdfObjectReader.getObjectReader()));
          if ((localObject != null) && (localObject.length > 3) && (localObject[0] == 79) && (localObject[1] == 84) && (localObject[2] == 84) && (localObject[3] == 79))
            paramInt = 1217103210;
        }
      }
    }
    return paramInt;
  }

  private int getFontMapping(PdfObject paramPdfObject1, String paramString, int paramInt, PdfObject paramPdfObject2)
    throws PdfException
  {
    String str1;
    if (paramPdfObject2 == null)
      str1 = paramPdfObject1.getName(678461817);
    else
      str1 = paramPdfObject2.getName(678461817);
    if (str1 == null)
      str1 = paramPdfObject1.getName(506543413);
    if (str1 == null)
      str1 = paramString;
    String str2 = getFontSub(str1);
    if ((str2 != null) && (paramPdfObject2 == null))
    {
      if ((str2.equals("/Type1")) || (str2.equals("/Type1C")) || (str2.equals("/MMType1")))
        paramInt = 1228944677;
      else if (str2.equals("/TrueType"))
        paramInt = 1217103210;
      else if (str2.equals("/Type3"))
        paramInt = 1228944679;
      else
        throw new RuntimeException(new StringBuilder().append("Unknown font type ").append(str2).append(" used for font substitution").toString());
      this.origfontType = paramPdfObject1.getParameterConstant(1147962727);
    }
    else if (FontMappings.enforceFontSubstitution)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog(new StringBuilder().append("baseFont=").append(this.baseFont).append(" fonts added= ").append(FontMappings.fontSubstitutionTable).toString());
      throw new PdfFontException(new StringBuilder().append("No substitute Font found for font=").append(this.baseFont).append('<').toString());
    }
    return paramInt;
  }

  public String getFontSub(String paramString)
    throws PdfException
  {
    if (paramString.indexOf(35) != -1)
      paramString = StringUtils.convertHexChars(paramString);
    this.rawFontName = paramString;
    this.baseFont = paramString.toLowerCase();
    int i = this.baseFont.indexOf(43);
    if (i == 6)
      this.baseFont = this.baseFont.substring(7);
    Object localObject1 = this.baseFont;
    this.subFont = ((String)FontMappings.fontSubstitutionLocation.get(localObject1));
    Object localObject2 = (String)FontMappings.fontSubstitutionTable.get(localObject1);
    if ((DecoderOptions.isRunningOnMac) && (((String)localObject1).equals("zapfdingbats")))
      localObject1 = "No match found";
    if (localObject2 == null)
    {
      HashMap localHashMap = new HashMap(50);
      while (true)
      {
        String str2 = (String)FontMappings.fontSubstitutionAliasTable.get(localObject1);
        if (str2 == null)
          break;
        localObject1 = str2;
        String str1 = (String)FontMappings.fontSubstitutionTable.get(localObject1);
        if (str1 != null)
        {
          localObject2 = str1;
          this.subFont = ((String)FontMappings.fontSubstitutionLocation.get(localObject1));
        }
        if (localHashMap.containsKey(localObject1))
        {
          StringBuilder localStringBuilder = new StringBuilder("[PDF] Circular font mapping for fonts");
          Iterator localIterator = localHashMap.keySet().iterator();
          while (localIterator.hasNext())
          {
            Object localObject3 = localIterator.next();
            localStringBuilder.append(' ');
            localStringBuilder.append(localObject3);
          }
          throw new PdfException(localStringBuilder.toString());
        }
        localHashMap.put(str2, "x");
      }
    }
    return localObject2;
  }

  private static boolean isFontEmbedded(PdfObject paramPdfObject)
  {
    int i = paramPdfObject.getParameterConstant(1147962727);
    if (i == 1228944676)
      paramPdfObject = paramPdfObject.getDictionary(-1547306032);
    PdfObject localPdfObject = paramPdfObject.getDictionary(-1044665361);
    if (localPdfObject == null)
      return false;
    return localPdfObject.hasStream();
  }

  public String getnonEmbeddedCIDFonts()
  {
    return this.nonEmbeddedCIDFonts.toString();
  }

  public String getFontsInFile()
  {
    return this.fontsInFile;
  }

  public void resetfontsInFile()
  {
    this.fontsInFile = "";
  }

  public boolean hasEmbeddedFonts()
  {
    return this.hasEmbeddedFonts;
  }

  public boolean hasNonEmbeddedCIDFonts()
  {
    return this.hasNonEmbeddedCIDFonts;
  }

  public String getMapFont()
  {
    return this.subFont;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.parser.PdfFontFactory
 * JD-Core Version:    0.6.2
 */