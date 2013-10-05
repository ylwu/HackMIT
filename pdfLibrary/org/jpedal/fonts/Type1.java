package org.jpedal.fonts;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.Map;
import java.util.StringTokenizer;
import org.jpedal.fonts.glyph.PdfJavaGlyphs;
import org.jpedal.utils.LogWriter;

public class Type1 extends PdfFont
{
  protected boolean isCID = false;
  private static final int c1 = 52845;
  private static final int c2 = 22719;
  private int skipBytes = 4;
  public static final String[] T1CcharCodes1Byte = { "-Reserved-", "hstem", "-Reserved-", "vstem", "vmoveto", "rlineto", "hlineto", "vlineto", "rrcurveto", "closePathT1", "callsubr", "return", "escape", "hsbwT1", "endchar", "-Reserved-", "blend", "-Reserved-", "hstemhm", "hintmask", "cntrmask", "rmoveto", "hmoveto", "vstemhm", "rcurveline", "rlinecurve", "vvcurveto", "hhcurveto", "intint", "callgsubr", "vhcurveto", "hvcurveto" };
  public static final String[] T1C = { "dotSection", "vstem3", "hstem3", "and", "or", "not", "seacT1", "swbT1", "store", "abs", "add", "sub", "div", "load", "neg", "eq", "callothersubT1", "pop", "drop", "-Reserved-", "put", "get", "ifelse", "random", "mul", "-Reserved-", "sqrt", "dup", "exch", "index", "roll", "-Reserved-", "-Reserved-", "setcurrentpointT1", "hflex", "flex", "hflex1", "flex1" };
  boolean trackIndices = false;

  protected final void readType1FontFile(byte[] paramArrayOfByte)
    throws Exception
  {
    if (LogWriter.isOutput())
      LogWriter.writeLog(new StringBuilder().append("Embedded Type1 font used ").append(getBaseFontName()).toString());
    BufferedReader localBufferedReader = new BufferedReader(new StringReader(new String(paramArrayOfByte)));
    while (true)
    {
      String str1 = localBufferedReader.readLine();
      if (str1 == null)
        break;
      if (str1.startsWith("/Encoding 256 array"))
      {
        readDiffEncoding(localBufferedReader);
      }
      else if (str1.startsWith("/lenIV"))
      {
        StringTokenizer localStringTokenizer1 = new StringTokenizer(str1);
        localStringTokenizer1.nextToken();
        this.skipBytes = Integer.parseInt(localStringTokenizer1.nextToken());
      }
      else if (str1.contains("/FontMatrix"))
      {
        String str2 = "";
        int i = str1.indexOf(91);
        int k;
        if (i != -1)
        {
          k = str1.indexOf(93);
          str2 = str1.substring(i + 1, k);
        }
        else
        {
          i = str1.indexOf(123);
          if (i != -1)
          {
            k = str1.indexOf(125);
            str2 = str1.substring(i + 1, k);
          }
        }
        StringTokenizer localStringTokenizer2 = new StringTokenizer(str2);
        for (int m = 0; m < 6; m++)
          this.FontMatrix[m] = Double.parseDouble(localStringTokenizer2.nextToken());
      }
    }
    if (localBufferedReader != null)
      try
      {
        localBufferedReader.close();
      }
      catch (Exception localException)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog(new StringBuilder().append("Exception ").append(localException).append(" closing stream").toString());
      }
    int j = 0;
    if (this.renderPage)
      j = readEncodedContent(paramArrayOfByte);
    this.glyphs.setGlyphCount(j);
    if ((!this.renderPage) || (j > 0))
      this.isFontEmbedded = true;
    this.glyphs.setFontEmbedded(true);
  }

  private void readDiffEncoding(BufferedReader paramBufferedReader)
    throws Exception
  {
    String str1;
    while ((str1 = paramBufferedReader.readLine()) != null)
    {
      str1 = str1.trim();
      if (str1.startsWith("readonly"))
        break;
      if ((str1.startsWith("dup")) && (str1.contains("/")))
      {
        StringTokenizer localStringTokenizer = new StringTokenizer(str1, " /");
        if (localStringTokenizer.countTokens() >= 3)
        {
          localStringTokenizer.nextToken();
          String str3 = localStringTokenizer.nextToken();
          int j = str3.indexOf(35);
          int i;
          if (j == -1)
          {
            i = Integer.parseInt(str3);
          }
          else
          {
            String str4 = str3.substring(0, j);
            String str5 = str3.substring(j + 1, str3.length());
            i = Integer.parseInt(str5, Integer.parseInt(str4));
          }
          String str2 = localStringTokenizer.nextToken();
          putChar(i, str2);
          int k = str2.charAt(0);
          if ((k == 66) || (k == 67) || (k == 99) || (k == 71))
          {
            int m = 1;
            int n = str2.length();
            while ((!this.isHex) && (m < n))
              this.isHex = Character.isLetter(str2.charAt(m++));
          }
        }
      }
    }
  }

  protected final void putChar(int paramInt, String paramString)
  {
    if (this.diffs == null)
      this.diffs = new String[this.maxCharCount];
    this.diffs[paramInt] = paramString;
    if ((!this.hasEncoding) && (!this.isCID))
      if (StandardFonts.getUnicodeName(paramString) != null)
        putMappedChar(paramInt, paramString);
      else
        this.nonStandardMappings.put(paramString, Integer.valueOf(paramInt));
  }

  private int readEncodedContent(byte[] paramArrayOfByte)
    throws Exception
  {
    int i = 0;
    String str2 = "rd";
    String str3 = "nd";
    int j = paramArrayOfByte.length;
    int k = -1;
    int m = -1;
    for (int n = 4; n < j; n++)
      if ((paramArrayOfByte[(n - 3)] == 101) && (paramArrayOfByte[(n - 2)] == 120) && (paramArrayOfByte[(n - 1)] == 101) && (paramArrayOfByte[n] == 99))
      {
        for (k = n + 1; (paramArrayOfByte[k] == 10) || (paramArrayOfByte[k] == 13); k++);
        n = j;
      }
    if (k != -1)
      for (n = k; n < j - 10; n++)
        if ((paramArrayOfByte[n] == 99) && (paramArrayOfByte[(n + 1)] == 108) && (paramArrayOfByte[(n + 2)] == 101) && (paramArrayOfByte[(n + 3)] == 97) && (paramArrayOfByte[(n + 4)] == 114) && (paramArrayOfByte[(n + 5)] == 116) && (paramArrayOfByte[(n + 6)] == 111) && (paramArrayOfByte[(n + 7)] == 109) && (paramArrayOfByte[(n + 8)] == 97) && (paramArrayOfByte[(n + 9)] == 114) && (paramArrayOfByte[(n + 10)] == 107))
        {
          for (m = n - 1; (paramArrayOfByte[m] == 10) || (paramArrayOfByte[m] == 13); m--);
          n = j;
        }
    if (m == -1)
      m = j;
    int i3 = 55665;
    int i4 = 4;
    int i5 = 1;
    for (n = k; n < k + i4 * 2; n++)
    {
      int i6 = (char)paramArrayOfByte[n];
      if (((i6 < 48) || (i6 > 57)) && ((i6 < 65) || (i6 > 70)) && ((i6 < 97) || (i6 > 102)))
      {
        i5 = 0;
        break;
      }
    }
    if (k != -1)
    {
      localObject = new ByteArrayOutputStream(m - k);
      if ((this.isFontSubstituted) && (i5 == 0))
        k = k + 2 + this.skipBytes;
      for (n = k; n < m; n++)
      {
        int i1;
        if (i5 == 0)
        {
          i1 = paramArrayOfByte[n] & 0xFF;
        }
        else
        {
          int i7 = 0;
          StringBuilder localStringBuilder = new StringBuilder();
          while (i7 < 2)
          {
            i1 = paramArrayOfByte[n] & 0xFF;
            n++;
            if ((i1 != 10) && (i1 != 13) && (i1 != 9) && (i1 != 32))
            {
              localStringBuilder.append((char)i1);
              i7++;
            }
          }
          n--;
          i1 = Integer.parseInt(localStringBuilder.toString(), 16);
        }
        int i2 = i1 ^ i3 >> 8;
        i3 = (i1 + i3) * 52845 + 22719 & 0xFFFF;
        if (n > k + i4)
          ((ByteArrayOutputStream)localObject).write(i2);
      }
      ((ByteArrayOutputStream)localObject).close();
      paramArrayOfByte = ((ByteArrayOutputStream)localObject).toByteArray();
    }
    Object localObject = new BufferedReader(new StringReader(new String(paramArrayOfByte)));
    while (true)
    {
      String str1 = ((BufferedReader)localObject).readLine();
      if (str1 == null)
        break;
      if (str1.startsWith("/lenIV"))
      {
        StringTokenizer localStringTokenizer = new StringTokenizer(str1);
        localStringTokenizer.nextToken();
        this.skipBytes = Integer.parseInt(localStringTokenizer.nextToken());
      }
    }
    ((BufferedReader)localObject).close();
    int i8 = paramArrayOfByte.length;
    int i9 = 0;
    int i10 = -1;
    k = -1;
    while ((i9 < i8) && (i9 != i8))
    {
      if ((i9 + 11 < i8) && (paramArrayOfByte[i9] == 47) && (paramArrayOfByte[(i9 + 1)] == 67) && (paramArrayOfByte[(i9 + 2)] == 104) && (paramArrayOfByte[(i9 + 3)] == 97) && (paramArrayOfByte[(i9 + 4)] == 114) && (paramArrayOfByte[(i9 + 5)] == 83) && (paramArrayOfByte[(i9 + 6)] == 116) && (paramArrayOfByte[(i9 + 7)] == 114) && (paramArrayOfByte[(i9 + 8)] == 105) && (paramArrayOfByte[(i9 + 9)] == 110) && (paramArrayOfByte[(i9 + 10)] == 103) && (paramArrayOfByte[(i9 + 11)] == 115))
        k = i9 + 11;
      else if ((i9 + 5 < i8) && (paramArrayOfByte[i9] == 47) && (paramArrayOfByte[(i9 + 1)] == 83) && (paramArrayOfByte[(i9 + 2)] == 117) && (paramArrayOfByte[(i9 + 3)] == 98) && (paramArrayOfByte[(i9 + 4)] == 114) && (paramArrayOfByte[(i9 + 5)] == 115))
        i10 = i9 + 6;
      if ((i10 > -1) && (k > -1))
        break;
      i9++;
    }
    if (k == -1)
    {
      this.isFontSubstituted = false;
      if (LogWriter.isOutput())
        LogWriter.writeLog("No glyph data found");
    }
    else
    {
      i = extractFontData(this.skipBytes, paramArrayOfByte, k, str2, i8, str3);
    }
    if (i10 > -1)
      extractSubroutineData(this.skipBytes, paramArrayOfByte, i10, k, str2, i8, str3);
    return i;
  }

  private void extractSubroutineData(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3, String paramString1, int paramInt4, String paramString2)
    throws IOException
  {
    while ((paramArrayOfByte[paramInt2] == 32) || (paramArrayOfByte[paramInt2] == 10) || (paramArrayOfByte[paramInt2] == 13))
      paramInt2++;
    StringBuilder localStringBuilder1 = new StringBuilder();
    while (true)
    {
      j = (char)paramArrayOfByte[paramInt2];
      if (j == 32)
        break;
      localStringBuilder1.append(j);
      paramInt2++;
    }
    int i = Integer.parseInt(localStringBuilder1.toString());
    for (int j = 0; j < i; j++)
    {
      while (paramInt2 < paramInt4)
      {
        if ((((paramArrayOfByte[(paramInt2 - 2)] == 100) && (paramArrayOfByte[(paramInt2 - 1)] == 117) && (paramArrayOfByte[paramInt2] == 112) ? 1 : 0) | (paramInt2 == paramInt3 ? 1 : 0)) != 0)
          break;
        paramInt2++;
      }
      if (paramInt2 == paramInt3)
      {
        j = i;
      }
      else
      {
        while (paramArrayOfByte[(paramInt2 + 1)] == 32)
          paramInt2++;
        StringBuilder localStringBuilder2 = new StringBuilder("subrs");
        char c;
        while (true)
        {
          paramInt2++;
          c = (char)paramArrayOfByte[paramInt2];
          if (c == ' ')
            break;
          localStringBuilder2.append(c);
        }
        localStringBuilder1 = new StringBuilder();
        while (true)
        {
          paramInt2++;
          c = (char)paramArrayOfByte[paramInt2];
          if (c == ' ')
            break;
          localStringBuilder1.append(c);
        }
        int k = Integer.parseInt(localStringBuilder1.toString());
        while (paramArrayOfByte[paramInt2] == 32)
          paramInt2++;
        paramInt2 = paramInt2 + paramString1.length() + 1;
        byte[] arrayOfByte = getStream(paramInt1, paramInt2, k, paramArrayOfByte);
        this.glyphs.setCharString(localStringBuilder2.toString(), arrayOfByte, j);
        paramInt2 = paramInt2 + k + paramString2.length();
      }
    }
  }

  private int extractFontData(int paramInt1, byte[] paramArrayOfByte, int paramInt2, String paramString1, int paramInt3, String paramString2)
    throws IOException
  {
    int i = paramArrayOfByte.length;
    int j = 0;
    while ((paramInt2 < i) && (paramArrayOfByte[paramInt2] != 47))
      paramInt2++;
    for (int k = paramInt2; paramInt2 < paramInt3; k++)
    {
      if (paramArrayOfByte[k] == 47)
      {
        k += 2;
        while ((k < i) && ((paramArrayOfByte[(k - 1)] != 124) || ((paramArrayOfByte[k] != 45) && (paramArrayOfByte[k] != 48)) || ((paramArrayOfByte[(k + 1)] != 10) && (paramArrayOfByte[(k + 1)] != 13))) && ((paramArrayOfByte[(k - 1)] != 78) || (paramArrayOfByte[k] != 68)))
          k++;
      }
      if ((i - k < 3) || ((paramArrayOfByte[(k - 1)] != 47) && (paramArrayOfByte[k] == 101) && (paramArrayOfByte[(k + 1)] == 110) && (paramArrayOfByte[(k + 2)] == 100)))
        break;
    }
    while (paramInt2 <= k)
    {
      StringBuilder localStringBuilder1 = new StringBuilder(20);
      while (true)
      {
        paramInt2++;
        char c3 = (char)paramArrayOfByte[paramInt2];
        if (c3 == ' ')
          break;
        localStringBuilder1.append(c3);
      }
      if (this.trackIndices)
        this.glyphs.setIndexForCharString(j, localStringBuilder1.toString());
      paramInt2++;
      StringBuilder localStringBuilder2 = new StringBuilder();
      while (true)
      {
        char c4 = (char)paramArrayOfByte[paramInt2];
        if (c4 == ' ')
          break;
        localStringBuilder2.append(c4);
        paramInt2++;
      }
      int m = Integer.parseInt(localStringBuilder2.toString());
      while (paramArrayOfByte[paramInt2] == 32)
        paramInt2++;
      paramInt2 = paramInt2 + paramString1.length() + 1;
      byte[] arrayOfByte = getStream(paramInt1, paramInt2, m, paramArrayOfByte);
      this.glyphs.setCharString(localStringBuilder1.toString(), arrayOfByte, j);
      j++;
      for (paramInt2 = paramInt2 + m + paramString2.length(); (paramInt2 <= k) && (paramArrayOfByte[paramInt2] != 47); paramInt2++);
    }
    return j;
  }

  private static final byte[] getStream(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte)
    throws IOException
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    int i = 4330;
    for (int m = 0; m < paramInt3; m++)
    {
      int j = paramArrayOfByte[(paramInt2 + m)] & 0xFF;
      int k = j ^ i >> 8;
      i = (j + i) * 52845 + 22719 & 0xFFFF;
      if (m >= paramInt1)
        localByteArrayOutputStream.write(k);
    }
    localByteArrayOutputStream.close();
    return localByteArrayOutputStream.toByteArray();
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.fonts.Type1
 * JD-Core Version:    0.6.2
 */