package org.jpedal.render.output;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.jpedal.fonts.StandardFonts;

public class GenericFontMapper
  implements FontMapper
{
  private static final String DEFAULT_FONT = "DEFAULT_FONT";
  String style = "normal";
  String weight = "normal";
  String family = null;
  private String fontID;
  private int fontMode = 3;
  private boolean isFontEmbedded = false;
  public static Map<String, String> fontMappings = new HashMap();
  public static Map<String, Integer> fontSizeAdjustments = new HashMap();
  private static Map<String, String> fontStyle = new HashMap();
  private static Map<String, String> fontWeight = new HashMap();
  private static String defaultFonts = "/org/jpedal/render/output/defaultFontMap.xml";
  private String rawFont;

  public GenericFontMapper(String paramString)
  {
    init(paramString);
    this.rawFont = paramString;
  }

  public GenericFontMapper(String paramString, int paramInt, boolean paramBoolean)
  {
    this.fontMode = paramInt;
    this.isFontEmbedded = paramBoolean;
    this.rawFont = paramString;
    init(paramString);
  }

  private void init(String paramString)
  {
    if ((this.fontMode == 6) || (this.fontMode == 7))
    {
      this.fontID = paramString;
      if ((!this.isFontEmbedded) || (StandardFonts.isStandardFont(paramString, true)))
      {
        int i = paramString.indexOf(44);
        if (i == -1)
          i = paramString.indexOf(45);
        if (i == -1)
          for (int j = paramString.length() - 1; j >= 0; j--)
          {
            int k = paramString.codePointAt(j);
            if ((k >= 48) && (k <= 57))
            {
              if (j >= paramString.length() - 1)
                break;
              i = j - 1;
              break;
            }
          }
        if (i > 0)
          findAttributes(paramString);
      }
    }
    else if (!directMapFont(paramString))
    {
      String str = findAttributes(paramString);
      if ((!mapFont(str)) && (!hasSimiliarMapping(str)))
        switch (this.fontMode)
        {
        case 3:
          this.fontID = "DEFAULT_FONT";
          break;
        case 2:
          throw new RuntimeException("Font " + paramString + " not mapped");
        }
    }
  }

  private String findAttributes(String paramString)
  {
    String str1 = paramString;
    int i = paramString.indexOf(44);
    if (i == -1)
      i = paramString.indexOf(45);
    int k;
    if (i == -1)
      for (int j = paramString.length() - 1; j >= 0; j--)
      {
        k = paramString.codePointAt(j);
        if ((k >= 48) && (k <= 57))
        {
          if (j >= paramString.length() - 1)
            break;
          i = j - 1;
          break;
        }
      }
    if (i != -1)
    {
      String str2 = paramString.substring(i + 1, paramString.length()).toLowerCase();
      str1 = paramString.substring(0, i);
      this.family = str1;
      k = 0;
      Iterator localIterator = fontMappings.keySet().iterator();
      while (localIterator.hasNext())
      {
        String str3 = (String)localIterator.next();
        if (str3.startsWith(this.family))
          k = 1;
      }
      if ((k != 0) || (!this.isFontEmbedded))
      {
        if (str2.contains("heavy"))
          this.weight = "900";
        else if (str2.endsWith("black"))
          this.weight = "bolder";
        else if (str2.contains("light"))
          this.weight = "lighter";
        else if (str2.contains("condensed"))
          this.weight = "100";
        else if (str2.contains("bold"))
          this.weight = "bold";
        if ((str2.equals("it")) || (str2.contains("italic")) || (str2.contains("kursiv")) || (str2.contains("oblique")))
          this.style = "italic";
      }
    }
    return str1;
  }

  private boolean mapFont(String paramString)
  {
    if (fontMappings.get(paramString) != null)
    {
      this.fontID = paramString;
      return true;
    }
    return false;
  }

  private boolean directMapFont(String paramString)
  {
    boolean bool = mapFont(paramString);
    if (!bool)
      return false;
    if (fontStyle.containsKey(paramString))
      this.style = ((String)fontStyle.get(paramString));
    if (fontWeight.containsKey(paramString))
      this.weight = ((String)fontWeight.get(paramString));
    return true;
  }

  private boolean hasSimiliarMapping(String paramString)
  {
    Set localSet = fontMappings.keySet();
    HashSet localHashSet = new HashSet();
    Object localObject = localSet.iterator();
    while (((Iterator)localObject).hasNext())
    {
      String str1 = (String)((Iterator)localObject).next();
      String str2 = str1.toLowerCase();
      String str3 = paramString.toLowerCase();
      if (str2.equals(str3))
      {
        this.fontID = str1;
        return true;
      }
      if ((str2.contains(str3)) || (str3.contains(str2)))
        localHashSet.add(str1);
    }
    if (!localHashSet.isEmpty())
    {
      localObject = new String[localHashSet.size()];
      localObject = (String[])localHashSet.toArray((Object[])localObject);
      this.fontID = localObject[0];
      if (localHashSet.size() > 1)
        for (int i = 1; i < localObject.length; i++)
          if (localObject[i].length() < this.fontID.length())
            this.fontID = localObject[i];
      return true;
    }
    return false;
  }

  public String getFont(boolean paramBoolean)
  {
    String str = (String)fontMappings.get(this.fontID);
    if ((str == null) && (this.family != null) && (paramBoolean))
      str = (String)fontMappings.get(this.family);
    if ((this.fontMode == 6) && (str == null))
    {
      this.rawFont = this.rawFont.replaceAll("[.,*#]", "-");
      return this.rawFont;
    }
    if ((this.isFontEmbedded) && (this.fontMode == 7) && (str == null))
    {
      this.rawFont = this.rawFont.replaceAll("[.,*#]", "-");
      return this.rawFont;
    }
    if ((str == null) && ((this.fontMode == 6) || (this.fontMode == 7)))
    {
      this.rawFont = this.rawFont.replaceAll("[.,*#]", "-");
      return this.rawFont;
    }
    return str == null ? "" : str;
  }

  public String getStyle()
  {
    return this.style;
  }

  public String getWeight()
  {
    return this.weight;
  }

  static
  {
    String str1 = "Arial, Helvetica, sans-serif";
    String str2 = "'Arial Black', Gadget, sans-serif";
    String str3 = "'Comic Sans MS', Textile, cursive";
    String str4 = "'Courier New', Courier, monospace";
    String str5 = "Georgia, 'Times New Roman', Times, serif";
    String str6 = "Impact, Charcoal, sans-serif";
    String str7 = "'Lucida Console', Monaco, monospace";
    String str8 = "'Lucida Sans Unicode', 'Lucida Grande', sans-serif";
    String str9 = "'Palatino Linotype', 'Book Antiqua', Palatino, serif";
    String str10 = "Tahoma, Geneva, sans-serif";
    String str11 = "'Times New Roman', Times, serif";
    String str12 = "'Trebuchet MS', Helvetica, sans-serif";
    String str13 = "Verdana, Geneva, sans-serif";
    String str14 = "Symbol";
    String str15 = "Webdings";
    String str16 = "Wingdings, 'Zapf Dingbats'";
    String str17 = "'MS Sans Serif', Geneva, sans-serif";
    String str18 = "'MS Serif', 'New York', serif";
    String str19 = "Helvetica, Arial, sans-serif";
    if (fontMappings.keySet().isEmpty())
    {
      fontMappings.put("Arial", str1);
      fontMappings.put("ArialMT", str1);
      fontMappings.put("ArialBlack", str2);
      fontMappings.put("ComicSansMS", str3);
      fontMappings.put("CourierNew", str4);
      fontMappings.put("Georgia", str5);
      fontMappings.put("Impact", str6);
      fontMappings.put("LucidaConsole", str7);
      fontMappings.put("LucidaSansUnicode", str8);
      fontMappings.put("PalatinoLinotype", str9);
      fontMappings.put("Tahoma", str10);
      fontMappings.put("TimesNewRoman", str11);
      fontMappings.put("Times", str11);
      fontMappings.put("Trebuchet", str12);
      fontMappings.put("Verdana", str13);
      fontMappings.put("Symbol", str14);
      fontMappings.put("Webdings", str15);
      fontMappings.put("Wingdings", str16);
      fontMappings.put("MSSansSerif", str17);
      fontMappings.put("MSSerif", str18);
      fontMappings.put("Helvetica", str19);
      fontMappings.put("ZapfDingbats", str16);
      fontMappings.put("DEFAULT_FONT", str11);
      fontSizeAdjustments.put("DEFAULT_FONT", Integer.valueOf(-1));
    }
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.render.output.GenericFontMapper
 * JD-Core Version:    0.6.2
 */