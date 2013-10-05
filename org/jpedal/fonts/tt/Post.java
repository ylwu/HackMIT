package org.jpedal.fonts.tt;

import java.util.HashMap;
import java.util.Map;
import org.jpedal.utils.LogWriter;

public class Post extends Table
{
  private Map translateToID = new HashMap();
  private final String[] macEncoding = { ".notdef", ".null", "nonmarkingreturn", "space", "exclam", "quotedbl", "numbersign", "dollar", "percent", "ampersand", "quotesingle", "parenleft", "parenright", "asterisk", "plus", "comma", "hyphen", "period", "slash", "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "colon", "semicolon", "less", "equal", "greater", "question", "at", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "bracketleft", "backslash", "bracketright", "asciicircum", "underscore", "grave", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "braceleft", "bar", "braceright", "asciitilde", "Adieresis", "Aring", "Ccedilla", "Eacute", "Ntilde", "Odieresis", "Udieresis", "aacute", "agrave", "acircumflex", "adieresis", "atilde", "aring", "ccedilla", "eacute", "egrave", "ecircumflex", "edieresis", "iacute", "igrave", "icircumflex", "idieresis", "ntilde", "oacute", "ograve", "ocircumflex", "odieresis", "otilde", "uacute", "ugrave", "ucircumflex", "udieresis", "dagger", "degree", "cent", "sterling", "section", "bullet", "paragraph", "germandbls", "registered", "copyright", "trademark", "acute", "dieresis", "notequal", "AE", "Oslash", "infinity", "plusminus", "lessequal", "greaterequal", "yen", "mu", "partialdiff", "summation", "product", "pi", "integral", "ordfeminine", "ordmasculine", "Omega", "ae", "oslash", "questiondown", "exclamdown", "logicalnot", "radical", "florin", "approxequal", "Delta", "guillemotleft", "guillemotright", "ellipsis", "nonbreakingspace", "Agrave", "Atilde", "Otilde", "OE", "oe", "endash", "emdash", "quotedblleft", "quotedblright", "quoteleft", "quoteright", "divide", "lozenge", "ydieresis", "Ydieresis", "fraction", "currency", "guilsinglleft", "guilsinglright", "fi", "fl", "daggerdbl", "periodcentered", "quotesinglbase", "quotedblbase", "perthousand", "Acircumflex", "Ecircumflex", "Aacute", "Edieresis", "Egrave", "Iacute", "Icircumflex", "Idieresis", "Igrave", "Oacute", "Ocircumflex", "apple", "Ograve", "Uacute", "Ucircumflex", "Ugrave", "dotlessi", "circumflex", "tilde", "macron", "breve", "dotaccent", "ring", "cedilla", "hungarumlaut", "ogonek", "caron", "Lslash", "lslash", "Scaron", "scaron", "Zcaron", "zcaron", "brokenbar", "Eth", "eth", "Yacute", "yacute", "Thorn", "thorn", "minus", "multiply", "onesuperior", "twosuperior", "threesuperior", "onehalf", "onequarter", "threequarters", "franc", "Gbreve", "gbreve", "Idotaccent", "Scedilla", "scedilla", "Cacute", "cacute", "Ccaron", "ccaron", "dcroat" };

  public Post(FontFile2 paramFontFile2)
  {
    int i = paramFontFile2.selectTable(8);
    if (i == 0)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("No Post table found");
    }
    else
    {
      int j = (int)(10.0F * paramFontFile2.getFixed());
      paramFontFile2.getFixed();
      paramFontFile2.getFWord();
      paramFontFile2.getFWord();
      paramFontFile2.getNextUint16();
      paramFontFile2.getNextUint16();
      paramFontFile2.getNextUint32();
      paramFontFile2.getNextUint32();
      paramFontFile2.getNextUint32();
      paramFontFile2.getNextUint32();
      if (j != 30)
        for (int m = 0; m < 258; m++)
          this.translateToID.put(this.macEncoding[m], Integer.valueOf(m));
      int k;
      switch (j)
      {
      case 20:
        k = paramFontFile2.getNextUint16();
        int[] arrayOfInt1 = new int[k];
        int n = 0;
        for (int i1 = 0; i1 < k; i1++)
        {
          arrayOfInt1[i1] = paramFontFile2.getNextUint16();
          if ((arrayOfInt1[i1] > 257) && (arrayOfInt1[i1] < 32768))
            n++;
        }
        String[] arrayOfString = new String[n];
        for (int i2 = 0; i2 < n; i2++)
          arrayOfString[i2] = paramFontFile2.getString();
        for (i2 = 0; i2 < k; i2++)
          if ((arrayOfInt1[i2] > 257) && (arrayOfInt1[i2] < 32768))
            this.translateToID.put(arrayOfString[(arrayOfInt1[i2] - 258)], Integer.valueOf(i2));
        break;
      case 25:
        k = paramFontFile2.getNextUint16();
        int[] arrayOfInt2 = new int[k];
        for (int i3 = 0; i3 < k; i3++)
        {
          arrayOfInt2[i3] = paramFontFile2.getNextint8();
          this.translateToID.put(this.macEncoding[(arrayOfInt2[i3] + i3)], Integer.valueOf(arrayOfInt2[i3]));
        }
      }
    }
  }

  public Post()
  {
  }

  public int convertGlyphToCharacterCode(String paramString)
  {
    Integer localInteger = (Integer)this.translateToID.get(paramString);
    if (localInteger == null)
      return 0;
    return localInteger.intValue();
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.fonts.tt.Post
 * JD-Core Version:    0.6.2
 */