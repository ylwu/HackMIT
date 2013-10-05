package org.jpedal.grouping;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jpedal.exception.PdfException;
import org.jpedal.objects.PdfData;
import org.jpedal.objects.PdfPageData;
import org.jpedal.utils.Fonts;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.Sorts;
import org.jpedal.utils.Strip;
import org.jpedal.utils.repositories.Vector_Float;
import org.jpedal.utils.repositories.Vector_Int;
import org.jpedal.utils.repositories.Vector_Object;
import org.jpedal.utils.repositories.Vector_Rectangle;
import org.jpedal.utils.repositories.Vector_String;

public class PdfGroupingAlgorithms
{
  private boolean includeHTMLtags = false;
  public static final int USER_DEFINED_LIST_ONLY = 0;
  public static final int SURROUND_BY_ANY_PUNCTUATION = 1;
  private static String SystemSeparator = System.getProperty("line.separator");
  private boolean[] isUsed;
  private float[] f_x1;
  private float[] f_x2;
  private float[] f_y1;
  private float[] f_y2;
  private boolean[] hadSpace;
  private String[] f_colorTag;
  private int[] writingMode;
  private int[] moveType;
  private int[] fontSize;
  private float[] spaceWidth;
  private StringBuilder[] content;
  private int[] textLength;
  private PdfData pdf_data;
  private boolean isXHTML = true;
  private int nextSlot;
  private Vector_Int lineBreaks = new Vector_Int();
  private Vector_Object lines;
  private Vector_Int lineY2;
  private static final String MARKER = PdfData.marker;
  public static char MARKER2 = MARKER.charAt(0);
  private int max_rows = 0;
  private int master = 0;
  private boolean colorExtracted = false;
  private int[] line_order;
  private static final int increment = 100;
  public static boolean useUnrotatedCoords;
  private float[] endPoints;
  private boolean includeTease;
  private String[] teasers;
  private List multipleTermTeasers = new ArrayList();
  private boolean usingMultipleTerms = false;
  private boolean isXMLExtraction = true;
  private int linkedSearchAreas = -101;

  public PdfGroupingAlgorithms(PdfData paramPdfData, PdfPageData paramPdfPageData, boolean paramBoolean)
  {
    this.pdf_data = paramPdfData;
    this.isXMLExtraction = paramBoolean;
    this.colorExtracted = paramPdfData.isColorExtracted();
  }

  public static void setSeparator(String paramString)
  {
    SystemSeparator = paramString;
  }

  private static final String getLineDownSeparator(StringBuilder paramStringBuilder1, StringBuilder paramStringBuilder2, boolean paramBoolean)
  {
    String str1 = " ";
    int i = 0;
    StringBuilder localStringBuilder1;
    StringBuilder localStringBuilder2;
    if (paramBoolean)
    {
      localStringBuilder1 = Strip.stripXML(paramStringBuilder1, paramBoolean);
      localStringBuilder2 = Strip.stripXML(paramStringBuilder2, paramBoolean);
    }
    else
    {
      localStringBuilder1 = Strip.trim(paramStringBuilder1);
      localStringBuilder2 = Strip.trim(paramStringBuilder2);
    }
    int j = localStringBuilder1.length();
    int k = localStringBuilder2.length();
    if ((j > 1) && (k > 1))
    {
      int m = localStringBuilder1.charAt(j - 1);
      int n = localStringBuilder1.charAt(j - 2);
      char c1 = localStringBuilder2.charAt(0);
      char c2 = localStringBuilder2.charAt(1);
      String str2 = "";
      if (str2.indexOf(m) != -1)
      {
        str1 = "";
        if (n == 58)
          str1 = "\n";
        if (m == 32)
          str1 = " ";
      }
      else if ((((n == 46 ? 1 : 0) | (m == 46 ? 1 : 0)) & (Character.isUpperCase(c1) | c1 == '&' | Character.isUpperCase(c2) | c2 == '&')) != 0)
      {
        if (paramBoolean)
          str1 = "<p></p>\n";
        else
          str1 = "\n";
      }
    }
    if (i != 0)
      if (paramBoolean)
        str1 = new StringBuilder().append(str1).append("<p></p>\n").toString();
      else
        str1 = new StringBuilder().append(str1).append('\n').toString();
    return str1;
  }

  private final void cleanupShadowsAndDrownedObjects(boolean paramBoolean)
  {
    for (int j : getUnusedFragments())
      if (this.isUsed[j] == 0)
      {
        float f2 = (this.f_x1[j] + this.f_x2[j]) / 2.0F;
        float f3 = (this.f_y1[j] + this.f_y2[j]) / 2.0F;
        for (int n = ??? + 1; n < ???; n++)
        {
          int k = ???[n];
          if ((this.isUsed[k] == 0) && (this.isUsed[j] == 0))
          {
            float f4 = this.fontSize[k] - this.fontSize[j];
            if (f4 < 0.0F)
              f4 = -f4;
            float f1 = this.f_x2[k] - this.f_x1[k] - (this.f_x2[j] - this.f_x1[j]);
            if (f1 < 0.0F)
              f1 = -f1;
            if ((f4 == 0.0F) && (f2 > this.f_x1[k]) && (f2 < this.f_x2[k]) && (f1 < 10.0F) && (f3 < this.f_y1[k]) && (f3 > this.f_y2[k]))
            {
              this.isUsed[k] = true;
            }
            else
            {
              int i1 = (this.f_x1[k] > this.f_x1[j]) && (this.f_x2[k] < this.f_x2[j]) && (this.f_y1[k] < this.f_y1[j]) && (this.f_y2[k] > this.f_y2[j]) ? 1 : 0;
              int i2 = (this.f_x1[j] > this.f_x1[k]) && (this.f_x2[j] < this.f_x2[k]) && (this.f_y1[j] < this.f_y1[k]) && (this.f_y2[j] > this.f_y2[k]) ? 1 : 0;
              if ((i1 != 0) || (i2 != 0))
              {
                String str;
                if (this.f_y2[j] > this.f_y2[k])
                {
                  str = getLineDownSeparator(this.content[j], this.content[k], this.isXMLExtraction);
                  if ((!paramBoolean) || (str.indexOf(32) == -1))
                    merge(j, k, str, true);
                }
                else
                {
                  str = getLineDownSeparator(this.content[k], this.content[j], this.isXMLExtraction);
                  if ((!paramBoolean) || (str.indexOf(32) == -1))
                    merge(k, j, str, true);
                }
                f2 = (this.f_x1[j] + this.f_x2[j]) / 2.0F;
                f3 = (this.f_y1[j] + this.f_y2[j]) / 2.0F;
              }
            }
          }
        }
      }
  }

  private final String isGapASpace(int paramInt1, int paramInt2, float paramFloat, boolean paramBoolean, int paramInt3)
  {
    String str = "";
    float f2 = this.spaceWidth[paramInt1] * this.fontSize[paramInt1];
    float f3 = this.spaceWidth[paramInt2] * this.fontSize[paramInt2];
    if (f2 > f3)
      f1 = f3;
    else
      f1 = f2;
    float f1 = paramFloat / (f1 / 1000.0F);
    if ((f1 > 0.51F) && (f1 < 1.0F))
      f1 = 1.0F;
    int i = (int)f1;
    if (i > 0)
      str = " ";
    if ((i > 1) && (paramBoolean) && (paramInt3 == 0))
      str = new StringBuilder().append(" <SpaceCount space=\"").append(i).append("\" />").toString();
    return str;
  }

  private final void merge(int paramInt1, int paramInt2, String paramString, boolean paramBoolean)
  {
    if (this.f_x1[paramInt1] > this.f_x1[paramInt2])
      this.f_x1[paramInt1] = this.f_x1[paramInt2];
    if (this.f_y1[paramInt1] < this.f_y1[paramInt2])
      this.f_y1[paramInt1] = this.f_y1[paramInt2];
    if (this.f_x2[paramInt1] < this.f_x2[paramInt2])
      this.f_x2[paramInt1] = this.f_x2[paramInt2];
    if (this.f_y2[paramInt1] > this.f_y2[paramInt2])
      this.f_y2[paramInt1] = this.f_y2[paramInt2];
    if (this.isXMLExtraction)
    {
      String str1 = Fonts.fe;
      if (this.colorExtracted)
        str1 = new StringBuilder().append(Fonts.fe).append("</color>").toString();
      if ((paramBoolean) && (this.content[paramInt1].toString().lastIndexOf(str1) != -1))
      {
        String str2 = this.content[paramInt1].toString();
        this.content[paramInt1] = new StringBuilder(str2.substring(0, str2.lastIndexOf(str1)));
        this.content[paramInt1].append(paramString);
        this.content[paramInt1].append(str2.substring(str2.lastIndexOf(str1)));
      }
      else
      {
        this.content[paramInt1].append(paramString);
      }
      if ((this.textLength[paramInt2] > 1) && (this.content[paramInt1].toString().endsWith(" ")))
        this.content[paramInt1].deleteCharAt(this.content[paramInt1].lastIndexOf(" "));
      this.fontSize[paramInt1] = this.fontSize[paramInt2];
      if ((this.content[paramInt2].indexOf("<color") != -1) && (this.content[paramInt1].indexOf("<color") != -1) && (this.content[paramInt2].toString().startsWith(this.content[paramInt1].substring(this.content[paramInt1].lastIndexOf("<color"), this.content[paramInt1].indexOf(">", this.content[paramInt1].lastIndexOf("<color"))))) && (this.content[paramInt1].lastIndexOf("</color>") + 7 == this.content[paramInt1].lastIndexOf(">")))
      {
        this.content[paramInt2].replace(this.content[paramInt2].indexOf("<color"), this.content[paramInt2].indexOf(">") + 1, "");
        this.content[paramInt1].replace(this.content[paramInt1].lastIndexOf("</color>"), this.content[paramInt1].lastIndexOf("</color>") + 8, "");
      }
      if ((this.content[paramInt2].indexOf("<font") != -1) && (this.content[paramInt1].indexOf("<font") != -1) && (this.content[paramInt2].toString().startsWith(this.content[paramInt1].substring(this.content[paramInt1].lastIndexOf("<font"), this.content[paramInt1].indexOf(">", this.content[paramInt1].lastIndexOf("<font"))))) && (this.content[paramInt1].lastIndexOf("</font>") + 6 == this.content[paramInt1].lastIndexOf(">")))
      {
        this.content[paramInt2].replace(this.content[paramInt2].indexOf("<font"), this.content[paramInt2].indexOf(">") + 1, "");
        this.content[paramInt1].replace(this.content[paramInt1].lastIndexOf("</font>"), this.content[paramInt1].lastIndexOf("</font>") + 7, "");
      }
      this.content[paramInt1] = this.content[paramInt1].append(this.content[paramInt2]);
      this.textLength[paramInt1] += this.textLength[paramInt2];
      this.isUsed[paramInt2] = true;
      this.content[paramInt2] = null;
    }
    else
    {
      this.fontSize[paramInt1] = this.fontSize[paramInt2];
      this.content[paramInt1] = this.content[paramInt1].append(paramString).append(this.content[paramInt2]);
      this.textLength[paramInt1] += this.textLength[paramInt2];
      this.isUsed[paramInt2] = true;
      this.content[paramInt2] = null;
    }
  }

  private final void removeEncoding()
  {
    int[] arrayOfInt1 = getUnusedFragments();
    for (int m : arrayOfInt1)
    {
      int i = m;
      if (this.isUsed[i] == 0)
        this.content[i] = removeHiddenMarkers(i);
    }
  }

  private final void copyToArrays()
  {
    this.colorExtracted = this.pdf_data.isColorExtracted();
    int i = this.pdf_data.getRawTextElementCount();
    this.isUsed = new boolean[i];
    this.fontSize = new int[i];
    this.writingMode = new int[i];
    this.spaceWidth = new float[i];
    this.content = new StringBuilder[i];
    this.textLength = new int[i];
    this.f_x1 = new float[i];
    this.f_colorTag = new String[i];
    this.f_x2 = new float[i];
    this.f_y1 = new float[i];
    this.f_y2 = new float[i];
    this.moveType = new int[i];
    for (int j = 0; j < i; j++)
    {
      this.content[j] = new StringBuilder(this.pdf_data.contents[j]);
      this.fontSize[j] = this.pdf_data.f_end_font_size[j];
      this.writingMode[j] = this.pdf_data.f_writingMode[j];
      this.f_x1[j] = this.pdf_data.f_x1[j];
      this.f_colorTag[j] = this.pdf_data.colorTag[j];
      this.f_x2[j] = this.pdf_data.f_x2[j];
      this.f_y1[j] = this.pdf_data.f_y1[j];
      this.f_y2[j] = this.pdf_data.f_y2[j];
      this.moveType[j] = this.pdf_data.move_command[j];
      this.spaceWidth[j] = this.pdf_data.space_width[j];
      this.textLength[j] = this.pdf_data.text_length[j];
    }
  }

  private int[] getUnusedFragments()
  {
    int i = this.isUsed.length;
    int j = 0;
    int[] arrayOfInt1 = new int[i];
    for (int k = 0; k < i; k++)
      if (this.isUsed[k] == 0)
      {
        arrayOfInt1[j] = k;
        j++;
      }
    int[] arrayOfInt2 = new int[j];
    System.arraycopy(arrayOfInt1, 0, arrayOfInt2, 0, j);
    return arrayOfInt2;
  }

  private StringBuilder removeHiddenMarkers(int paramInt)
  {
    if (this.content[paramInt].indexOf(MARKER) == -1)
      return this.content[paramInt];
    StringTokenizer localStringTokenizer = new StringTokenizer(this.content[paramInt].toString(), MARKER, true);
    StringBuilder localStringBuilder = new StringBuilder();
    while (localStringTokenizer.hasMoreTokens())
    {
      String str = localStringTokenizer.nextToken();
      if (str.equals(MARKER))
      {
        localStringTokenizer.nextToken();
        localStringTokenizer.nextToken();
        localStringTokenizer.nextToken();
        localStringTokenizer.nextToken();
        localStringBuilder = localStringBuilder.append(localStringTokenizer.nextToken());
      }
      else
      {
        localStringBuilder = localStringBuilder.append(str);
      }
    }
    return localStringBuilder;
  }

  public void setIncludeHTML(boolean paramBoolean)
  {
    this.includeHTMLtags = paramBoolean;
  }

  public static String removeHiddenMarkers(String paramString)
  {
    if (paramString == null)
      return null;
    if (!paramString.contains(MARKER))
      return paramString;
    StringTokenizer localStringTokenizer = new StringTokenizer(paramString, MARKER, true);
    StringBuilder localStringBuilder = new StringBuilder();
    while (localStringTokenizer.hasMoreTokens())
    {
      String str = localStringTokenizer.nextToken();
      if (str.equals(MARKER))
      {
        localStringTokenizer.nextToken();
        localStringTokenizer.nextToken();
        localStringTokenizer.nextToken();
        localStringTokenizer.nextToken();
        localStringBuilder = localStringBuilder.append(localStringTokenizer.nextToken());
      }
      else
      {
        localStringBuilder = localStringBuilder.append(str);
      }
    }
    return localStringBuilder.toString();
  }

  private void findVerticalLines(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, int paramInt)
    throws PdfException
  {
    HashMap localHashMap = new HashMap();
    int i = 0;
    int j = this.pdf_data.getRawTextElementCount();
    for (int k = 0; k < j; k++)
    {
      float f5 = 0.0F;
      String str1 = this.pdf_data.contents[k];
      float f1;
      float f2;
      float f3;
      float f4;
      if (paramInt == 0)
      {
        f1 = this.f_x1[k];
        f2 = this.f_x2[k];
        f3 = this.f_y1[k];
        f4 = this.f_y2[k];
      }
      else if (paramInt == 1)
      {
        f2 = this.f_x1[k];
        f1 = this.f_x2[k];
        f3 = this.f_y1[k];
        f4 = this.f_y2[k];
      }
      else if (paramInt == 3)
      {
        f1 = this.f_y1[k];
        f2 = this.f_y2[k];
        f3 = this.f_x2[k];
        f4 = this.f_x1[k];
      }
      else if (paramInt == 2)
      {
        f1 = this.f_y2[k];
        f2 = this.f_y1[k];
        f4 = this.f_x1[k];
        f3 = this.f_x2[k];
      }
      else
      {
        throw new PdfException(new StringBuilder().append("Illegal value ").append(paramInt).append("for currentWritingMode").toString());
      }
      if ((f1 > paramFloat1 - 0.5D) && (f2 < paramFloat3 + 0.5D) && (f4 > paramFloat2 - 0.5D) && (f3 < paramFloat4 + 0.5D))
      {
        StringTokenizer localStringTokenizer = new StringTokenizer(str1, MARKER, true);
        Object localObject1 = "";
        while (localStringTokenizer.hasMoreTokens())
        {
          String str2 = localStringTokenizer.nextToken();
          if (str2.equals(MARKER))
          {
            str2 = localStringTokenizer.nextToken();
            if (!str2.isEmpty())
            {
              float f6 = f5;
              f5 = Float.parseFloat(str2);
              try
              {
                if ((((String)localObject1).isEmpty()) || (((String)localObject1).indexOf(32) != -1))
                {
                  Integer localInteger2 = Integer.valueOf((int)f5);
                  Object localObject2 = localHashMap.get(localInteger2);
                  if (localObject2 == null)
                  {
                    localHashMap.put(localInteger2, Integer.valueOf(1));
                  }
                  else
                  {
                    i1 = ((Integer)localObject2).intValue();
                    i1++;
                    if (i1 > i)
                      i = i1;
                    localHashMap.put(localInteger2, Integer.valueOf(i1));
                  }
                  int i1 = (int)(f6 + (f5 - f6) / 2.0F);
                  if (f6 != 0.0F)
                  {
                    localInteger2 = Integer.valueOf(i1);
                    localObject2 = localHashMap.get(localInteger2);
                    if (localObject2 == null)
                    {
                      localHashMap.put(localInteger2, Integer.valueOf(1));
                    }
                    else
                    {
                      int i2 = ((Integer)localObject2).intValue();
                      i2++;
                      if (i2 > i)
                        i = i2;
                      localHashMap.put(localInteger2, Integer.valueOf(i2));
                    }
                  }
                }
              }
              catch (Exception localException)
              {
                LogWriter.writeLog(new StringBuilder().append("Exception ").append(localException).append(" stripping x values").toString());
              }
            }
            localStringTokenizer.nextToken();
            localStringTokenizer.nextToken();
            localStringTokenizer.nextToken();
            str2 = localStringTokenizer.nextToken();
            localObject1 = str2;
          }
        }
      }
    }
    Iterator localIterator = localHashMap.keySet().iterator();
    int m = i / 2;
    while (localIterator.hasNext())
    {
      Integer localInteger1 = (Integer)localIterator.next();
      int n = ((Integer)localHashMap.get(localInteger1)).intValue();
      if (n > m)
        this.lineBreaks.addElement(localInteger1.intValue());
    }
  }

  private void copyToArrays(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, String paramString, boolean paramBoolean4)
    throws PdfException
  {
    int i = this.pdf_data.getRawTextElementCount() + 100;
    this.f_x1 = new float[i];
    this.f_colorTag = new String[i];
    this.hadSpace = new boolean[i];
    this.f_x2 = new float[i];
    this.f_y1 = new float[i];
    this.f_y2 = new float[i];
    this.spaceWidth = new float[i];
    this.content = new StringBuilder[i];
    this.fontSize = new int[i];
    this.textLength = new int[i];
    this.writingMode = new int[i];
    this.isUsed = new boolean[i];
    this.moveType = new int[i];
    int j = 0;
    i -= 100;
    String str2 = "";
    StringBuilder localStringBuilder = new StringBuilder();
    for (int m = 0; m < i; m++)
    {
      float f10 = this.pdf_data.f_character_spacing[m];
      String str1 = this.pdf_data.contents[m];
      float f5 = this.pdf_data.f_x1[m];
      String str3 = this.pdf_data.colorTag[m];
      float f6 = this.pdf_data.f_x2[m];
      float f7 = this.pdf_data.f_y1[m];
      float f8 = this.pdf_data.f_y2[m];
      int k = this.pdf_data.text_length[m];
      int n = this.pdf_data.f_writingMode[m];
      int i1 = this.pdf_data.move_command[m];
      int i2 = 0;
      if ((n == 0) || (n == 1))
      {
        float f11 = f7 - f8;
        if (((f8 > paramFloat2) && (f7 < paramFloat4)) || ((f8 > paramFloat2) && (paramFloat4 - f8 > f11 * 0.5D)) || ((f7 < paramFloat4) && (f7 - paramFloat2 > f11 * 0.5D) && (paramFloat1 + paramFloat3 > 0.0F) && (f6 >= paramFloat1) && (f5 <= paramFloat3)))
          i2 = 1;
      }
      else if (((n == 3) || (n == 2)) && (f5 > paramFloat1) && (f6 < paramFloat3) && (f7 > paramFloat2) && (f8 < paramFloat4))
      {
        i2 = 1;
      }
      if (i2 != 0)
      {
        if ((j == 0) && (paramBoolean3))
        {
          findVerticalLines(paramFloat1, paramFloat2, paramFloat3, paramFloat4, n);
          j = 1;
        }
        float f4;
        float f1;
        float f2;
        float f3;
        if ((n == 0) || (n == 1))
        {
          f4 = f5;
          f1 = f5;
          f2 = paramFloat1;
          f3 = paramFloat3;
        }
        else
        {
          f4 = f8;
          f1 = f8;
          f2 = paramFloat2;
          f3 = paramFloat4;
        }
        float f9 = -1.0F;
        char[] arrayOfChar = str1.toCharArray();
        int i3 = arrayOfChar.length;
        int i4 = 0;
        Object localObject = "";
        if (!str1.contains(MARKER))
          localStringBuilder = new StringBuilder(str1);
        int i5 = 1;
        int i6 = 0;
        while (i4 < i3)
        {
          String str4;
          while (true)
          {
            int i7;
            if (arrayOfChar[i4] != MARKER2)
            {
              i7 = i4;
              while ((i4 < i3) && (arrayOfChar[i4] != MARKER2))
                i4++;
              str4 = str1.substring(i7, i4);
            }
            else
            {
              while ((i4 < i3) && (arrayOfChar[i4] != MARKER2))
                i4++;
              i4++;
              i7 = i4;
              while ((i4 < i3) && (arrayOfChar[i4] != MARKER2))
                i4++;
              String str5 = str1.substring(i7, i4);
              i4++;
              i7 = i4;
              while ((i4 < i3) && (arrayOfChar[i4] != MARKER2))
                i4++;
              str2 = str1.substring(i7, i4);
              i4++;
              i7 = i4;
              while ((i4 < i3) && (arrayOfChar[i4] != MARKER2))
                i4++;
              str4 = str1.substring(i7, i4);
              localObject = str4;
              if (!str5.isEmpty())
              {
                f1 = f4;
                f4 = Float.parseFloat(str5);
                if (i6 != 0)
                {
                  if (n == 0)
                    f5 = f4;
                  else if (n == 1)
                    f6 = f4;
                  else if (n == 3)
                    f8 = f4;
                  else if (n == 2)
                    f7 = f4;
                  i6 = 0;
                }
              }
              if ((this.isXMLExtraction) && (f1 < f2) && (f4 > f2) && (!str4.startsWith(Fonts.fb)))
                str4 = new StringBuilder().append(Fonts.getActiveFontTag(str1, "")).append(str4).toString();
            }
            if (!str2.isEmpty())
            {
              float f12 = f4 + Float.parseFloat(str2) * 0.3F;
              if (((f12 > f2 ? 1 : 0) & (f12 < f3 ? 1 : 0)) != 0)
              {
                if (n != 0)
                  break;
                if (((f5 < f2) || (f5 > f3)) && (f4 >= f2))
                {
                  f5 = f4;
                  break;
                }
                if (n != 1)
                  break;
                if (((f6 > f3) || (f6 < f2)) && (f4 <= f3))
                {
                  f6 = f4;
                  break;
                }
                if (n != 3)
                  break;
                if (((f8 < f2) || (f8 > f3)) && (f4 >= f2))
                {
                  f8 = f4;
                  break;
                }
                if ((n != 2) || ((f7 >= f2) && (f7 <= f3)) || (f4 > f2))
                  break;
                f7 = f4;
                break;
              }
            }
            str4 = "";
            localObject = "";
            if (i4 >= i3)
              break;
          }
          if (i5 != 0)
          {
            i5 = 0;
            if ((this.isXMLExtraction) && (paramBoolean1) && (!str4.startsWith(Fonts.fb)) && (!str4.startsWith("<color ")))
              localStringBuilder.append(Fonts.getActiveFontTag(localStringBuilder.toString(), str1));
          }
          int i8 = 0;
          if ((paramBoolean3) && (f10 > 0.0F) && (localStringBuilder.toString().endsWith(" ")))
          {
            int i9 = this.lineBreaks.size();
            for (int i10 = 0; i10 < i9; i10++)
            {
              int i12 = this.lineBreaks.elementAt(i10);
              if (((f1 < i12 ? 1 : 0) & (f4 > i12 ? 1 : 0)) != 0)
              {
                i10 = i9;
                i8 = 1;
              }
            }
          }
          boolean bool = checkForPunctuation((String)localObject, paramString);
          if (i8 != 0)
          {
            float f13 = f5;
            float f14 = f6;
            float f15 = f7;
            float f16 = f8;
            if (n == 0)
              f14 = f1 + Float.parseFloat(str2);
            else if (n == 1)
              f13 = f1 + Float.parseFloat(str2);
            else if (n == 3)
              f15 = f1 + Float.parseFloat(str2);
            else if (n == 2)
              f16 = f1 + Float.parseFloat(str2);
            addFragment(i1, m, localStringBuilder, f13, f14, f15, f16, k, paramBoolean1, str3, paramBoolean4);
            localStringBuilder = new StringBuilder(Fonts.getActiveFontTag(localStringBuilder.toString(), str1));
            localStringBuilder.append(str4);
            if (n == 0)
              f5 = f4;
            else if (n == 1)
              f6 = f4;
            else if (n == 3)
              f8 = f4;
            else if (n == 2)
              f7 = f4;
          }
          else if ((bool | ((paramBoolean2) && ((((String)localObject).indexOf(32) != -1) || (str4.endsWith(" ")))) | ((String)localObject).contains("   ")))
          {
            int i11;
            if ((((String)localObject).length() > 1) && (((String)localObject).indexOf(32) != -1))
            {
              i11 = ((String)localObject).indexOf(32);
              if (i11 > 0)
                f4 += i11 * (Float.parseFloat(str2) / ((String)localObject).length());
            }
            if (!bool)
              localStringBuilder.append(str4.trim());
            if (n == 0)
              addFragment(i1, m, localStringBuilder, f5, f4, f7, f8, k, paramBoolean1, str3, paramBoolean4);
            else if (n == 1)
              addFragment(i1, m, localStringBuilder, f4, f6, f7, f8, k, paramBoolean1, str3, paramBoolean4);
            else if (n == 3)
              addFragment(i1, m, localStringBuilder, f5, f6, f4, f8, k, paramBoolean1, str3, paramBoolean4);
            else if (n == 2)
              addFragment(i1, m, localStringBuilder, f5, f6, f7, f4, k, paramBoolean1, str3, paramBoolean4);
            if (!str2.isEmpty())
            {
              i11 = 0;
              if (((String)localObject).indexOf(32) != -1)
                i11 = ((String)localObject).indexOf(32);
              if (paramBoolean4)
              {
                int i13 = ((String)localObject).length();
                while ((i11 < i13) && (((String)localObject).charAt(i11) == ' '))
                  i11++;
              }
              if (i11 > 0)
                f4 += i11 * Float.parseFloat(str2);
              else
                f4 += Float.parseFloat(str2);
              i6 = i11 > 0 ? 1 : 0;
            }
            if ((paramBoolean2 & this.nextSlot > 0))
              this.hadSpace[(this.nextSlot - 1)] = true;
            localStringBuilder = new StringBuilder(Fonts.getActiveFontTag(localStringBuilder.toString(), str1));
            if (n == 0)
              f5 = f4;
            else if (n == 1)
              f6 = f4;
            else if (n == 3)
              f8 = f4;
            else if (n == 2)
              f7 = f4;
          }
          else if (((f9 != -1.0F ? 1 : 0) & (f4 > f9 ? 1 : 0)) != 0)
          {
            if (n == 0)
              addFragment(i1, m, localStringBuilder, f5, f9, f7, f8, k, paramBoolean1, str3, paramBoolean4);
            else if (n == 1)
              addFragment(i1, m, localStringBuilder, f9, f6, f7, f8, k, paramBoolean1, str3, paramBoolean4);
            else if (n == 3)
              addFragment(i1, m, localStringBuilder, f5, f6, f9, f8, k, paramBoolean1, str3, paramBoolean4);
            else if (n == 2)
              addFragment(i1, m, localStringBuilder, f5, f6, f7, f9, k, paramBoolean1, str3, paramBoolean4);
            localStringBuilder = new StringBuilder(Fonts.getActiveFontTag(localStringBuilder.toString(), str1));
            localStringBuilder.append(str4);
            if (n == 0)
              f5 = f9;
            else if (n == 1)
              f6 = f9;
            else if (n == 3)
              f8 = f9;
            else if (n == 2)
              f7 = f9;
            f9 = -1.0F;
          }
          else
          {
            if ((this.isXMLExtraction) && (str4.endsWith(new StringBuilder().append(' ').append(Fonts.fe).toString())))
            {
              str4 = Fonts.fe;
              localObject = "";
              if (n == 0)
                f6 = f1;
              else if (n == 1)
                f5 = f1;
              else if (n == 3)
                f7 = f1;
              else if (n == 2)
                f8 = f1;
            }
            localStringBuilder.append(str4);
          }
        }
        if ((paramBoolean1) && (this.isXMLExtraction) && (!localStringBuilder.toString().endsWith(Fonts.fe)) && (!localStringBuilder.toString().endsWith("</color>")))
          localStringBuilder.append(Fonts.fe);
        if ((n == 0) || (n == 1))
        {
          if (f5 < f6)
            addFragment(i1, m, localStringBuilder, f5, f6, f7, f8, k, paramBoolean1, str3, paramBoolean4);
        }
        else if (((n == 3) || (n == 2)) && (f7 > f8))
          addFragment(i1, m, localStringBuilder, f5, f6, f7, f8, k, paramBoolean1, str3, paramBoolean4);
        localStringBuilder = new StringBuilder();
      }
    }
    this.isUsed = new boolean[this.nextSlot];
  }

  private static boolean checkForPunctuation(String paramString1, String paramString2)
  {
    if (paramString2 == null)
      return false;
    boolean bool = false;
    int i = paramString1.length();
    int j = i - 1;
    if (i > 0)
    {
      char c = paramString1.charAt(j);
      int k = c == '>' ? 1 : 0;
      while (((k | (c == ' ' ? 1 : 0)) & (j > 0 ? 1 : 0)) != 0)
      {
        if (c == '<')
          k = 0;
        j--;
        c = paramString1.charAt(j);
        if (c == '>')
          k = 1;
      }
      if (c == ';')
      {
        bool = true;
        j--;
        while (j > -1)
        {
          c = paramString1.charAt(j);
          if ((c == '&') || (c == '#'))
          {
            bool = false;
            j = 0;
          }
          if ((j == 0) || (c == ' ') || (!Character.isLetterOrDigit(c)))
            break;
          j--;
        }
      }
      if (paramString2.indexOf(c) != -1)
        bool = true;
    }
    return bool;
  }

  private void addFragment(int paramInt1, int paramInt2, StringBuilder paramStringBuilder, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, int paramInt3, boolean paramBoolean1, String paramString, boolean paramBoolean2)
  {
    StringBuilder localStringBuilder = paramStringBuilder;
    String str = localStringBuilder.toString();
    if (paramBoolean2)
    {
      if (str.contains("&#"))
        localStringBuilder = Strip.stripAmpHash(localStringBuilder);
      if ((this.isXMLExtraction) && ((str.contains("&lt;")) || (str.contains("&gt;"))))
        localStringBuilder = Strip.stripXMLArrows(localStringBuilder, true);
      else if ((!this.isXMLExtraction) && ((str.indexOf(60) != -1) || (str.indexOf(62) != -1)))
        localStringBuilder = Strip.stripArrows(localStringBuilder);
    }
    if (getFirstChar(localStringBuilder) != -1)
    {
      if (!paramBoolean1)
        localStringBuilder = Strip.stripXML(localStringBuilder, this.isXMLExtraction);
      else if (this.isXMLExtraction)
        if ((this.pdf_data.isColorExtracted()) && (!localStringBuilder.toString().endsWith("</color>")))
        {
          if (!localStringBuilder.toString().endsWith(Fonts.fe))
            localStringBuilder = localStringBuilder.append(Fonts.fe);
          localStringBuilder = localStringBuilder.append("</color>");
        }
        else if ((!this.pdf_data.isColorExtracted()) && (!localStringBuilder.toString().endsWith(Fonts.fe)))
        {
          localStringBuilder = localStringBuilder.append(Fonts.fe);
        }
      int i = this.f_x1.length;
      if (this.nextSlot < i)
      {
        this.f_x1[this.nextSlot] = paramFloat1;
        this.f_colorTag[this.nextSlot] = paramString;
        this.f_x2[this.nextSlot] = paramFloat2;
        this.f_y1[this.nextSlot] = paramFloat3;
        this.f_y2[this.nextSlot] = paramFloat4;
        this.moveType[this.nextSlot] = paramInt1;
        this.fontSize[this.nextSlot] = this.pdf_data.f_end_font_size[paramInt2];
        this.writingMode[this.nextSlot] = this.pdf_data.f_writingMode[paramInt2];
        this.textLength[this.nextSlot] = paramInt3;
        this.spaceWidth[this.nextSlot] = this.pdf_data.space_width[paramInt2];
        this.content[this.nextSlot] = localStringBuilder;
        this.nextSlot += 1;
      }
      else
      {
        i += 100;
        float[] arrayOfFloat1 = new float[i];
        String[] arrayOfString = new String[i];
        float[] arrayOfFloat2 = new float[i];
        float[] arrayOfFloat3 = new float[i];
        float[] arrayOfFloat4 = new float[i];
        float[] arrayOfFloat5 = new float[i];
        StringBuilder[] arrayOfStringBuilder = new StringBuilder[i];
        int[] arrayOfInt1 = new int[i];
        int[] arrayOfInt2 = new int[i];
        int[] arrayOfInt3 = new int[i];
        int[] arrayOfInt4 = new int[i];
        boolean[] arrayOfBoolean1 = new boolean[i];
        boolean[] arrayOfBoolean2 = new boolean[i];
        for (int j = 0; j < i - 100; j++)
        {
          arrayOfFloat1[j] = this.f_x1[j];
          arrayOfString[j] = this.f_colorTag[j];
          arrayOfFloat2[j] = this.f_x2[j];
          arrayOfFloat3[j] = this.f_y1[j];
          arrayOfFloat4[j] = this.f_y2[j];
          arrayOfBoolean2[j] = this.hadSpace[j];
          arrayOfFloat5[j] = this.spaceWidth[j];
          arrayOfStringBuilder[j] = this.content[j];
          arrayOfInt1[j] = this.fontSize[j];
          arrayOfInt3[j] = this.writingMode[j];
          arrayOfInt2[j] = this.textLength[j];
          arrayOfBoolean1[j] = this.isUsed[j];
          arrayOfInt4[j] = this.moveType[j];
        }
        this.f_x1 = arrayOfFloat1;
        this.f_colorTag = arrayOfString;
        this.hadSpace = arrayOfBoolean2;
        this.f_x2 = arrayOfFloat2;
        this.f_y1 = arrayOfFloat3;
        this.f_y2 = arrayOfFloat4;
        this.isUsed = arrayOfBoolean1;
        this.fontSize = arrayOfInt1;
        this.writingMode = arrayOfInt3;
        this.textLength = arrayOfInt2;
        this.spaceWidth = arrayOfFloat5;
        this.content = arrayOfStringBuilder;
        this.moveType = arrayOfInt4;
        this.f_x1[this.nextSlot] = paramFloat1;
        this.f_colorTag[this.nextSlot] = paramString;
        this.f_x2[this.nextSlot] = paramFloat2;
        this.f_y1[this.nextSlot] = paramFloat3;
        this.f_y2[this.nextSlot] = paramFloat4;
        this.fontSize[this.nextSlot] = this.pdf_data.f_end_font_size[paramInt2];
        this.writingMode[this.nextSlot] = this.pdf_data.f_writingMode[paramInt2];
        arrayOfInt2[this.nextSlot] = paramInt3;
        this.content[this.nextSlot] = localStringBuilder;
        this.spaceWidth[this.nextSlot] = this.pdf_data.space_width[paramInt2];
        this.moveType[this.nextSlot] = paramInt1;
        this.nextSlot += 1;
      }
    }
  }

  private void mergeTableRows(int paramInt)
  {
    String str = "</tr>\n<tr>";
    if (!this.isXHTML)
      str = "\n";
    this.master = ((Vector_Int)this.lines.elementAt(this.line_order[0])).elementAt(0);
    for (int j = 1; j < this.max_rows; j++)
    {
      int i = ((Vector_Int)this.lines.elementAt(this.line_order[j])).elementAt(0);
      if (this.content[this.master] == null)
        this.master = i;
      else if (this.content[i] != null)
        merge(this.master, i, str, false);
    }
    if (this.isXHTML)
      if (paramInt == 0)
      {
        this.content[this.master].insert(0, "<TABLE>\n<tr>");
        this.content[this.master].append("</tr>\n</TABLE>\n");
      }
      else
      {
        StringBuilder localStringBuilder = new StringBuilder("<TABLE border='");
        localStringBuilder.append(String.valueOf(paramInt));
        localStringBuilder.append("'>\n<tr>");
        localStringBuilder.append(this.content[this.master]);
        this.content[this.master] = localStringBuilder;
        this.content[this.master].append("</tr>\n</TABLE>\n");
      }
  }

  private final int[] getsortedUnusedFragments(boolean paramBoolean1, boolean paramBoolean2)
  {
    int i = this.isUsed.length;
    int j = 0;
    int[] arrayOfInt1 = new int[i];
    for (int k = 0; k < i; k++)
      if (this.isUsed[k] == 0)
      {
        arrayOfInt1[j] = k;
        j++;
      }
    int[] arrayOfInt2 = new int[j];
    int[] arrayOfInt4 = new int[j];
    int[] arrayOfInt5 = new int[j];
    int[] arrayOfInt6 = new int[j];
    for (int m = 0; m < j; m++)
    {
      int n = arrayOfInt1[m];
      arrayOfInt2[m] = n;
      arrayOfInt4[m] = ((int)this.f_x1[n]);
      arrayOfInt5[m] = ((int)this.f_y1[n]);
      arrayOfInt6[m] = ((int)this.f_y2[n]);
    }
    int[] arrayOfInt3;
    if (!paramBoolean1)
    {
      if (paramBoolean2)
        arrayOfInt3 = Sorts.quicksort(arrayOfInt5, arrayOfInt4, arrayOfInt2);
      else
        arrayOfInt3 = Sorts.quicksort(arrayOfInt6, arrayOfInt4, arrayOfInt2);
    }
    else
      arrayOfInt3 = Sorts.quicksort(arrayOfInt4, arrayOfInt5, arrayOfInt2);
    return arrayOfInt3;
  }

  private void createTableRows(boolean paramBoolean1, boolean paramBoolean2, int paramInt)
    throws PdfException
  {
    float[] arrayOfFloat1;
    float[] arrayOfFloat2;
    int i;
    if (paramInt == 0)
    {
      arrayOfFloat1 = this.f_x1;
      arrayOfFloat2 = this.f_x2;
    }
    else if (paramInt == 1)
    {
      arrayOfFloat2 = this.f_x1;
      arrayOfFloat1 = this.f_x2;
    }
    else if (paramInt == 3)
    {
      arrayOfFloat1 = this.f_y2;
      arrayOfFloat2 = this.f_y1;
    }
    else if (paramInt == 2)
    {
      arrayOfFloat1 = this.f_y1;
      arrayOfFloat2 = this.f_y2;
      i = 0;
      for (float f1 : arrayOfFloat1)
        if (i < f1)
          i = (int)f1;
      i++;
      for (j = 0; j < arrayOfFloat2.length; j++)
      {
        arrayOfFloat1[j] = (i - arrayOfFloat1[j]);
        arrayOfFloat2[j] = (i - arrayOfFloat2[j]);
      }
    }
    else
    {
      throw new PdfException(new StringBuilder().append("Illegal value ").append(paramInt).append("for currentWritingMode").toString());
    }
    ??? = 0;
    ??? = 0;
    int[] arrayOfInt1 = new int[this.max_rows];
    Vector_Int[] arrayOfVector_Int = new Vector_Int[this.max_rows];
    Vector_String localVector_String = new Vector_String();
    Vector_Float localVector_Float1 = new Vector_Float();
    Vector_Float localVector_Float2 = new Vector_Float();
    String str1 = "";
    String str2 = "&nbsp;";
    if (!this.isXHTML)
    {
      str1 = "\",\"";
      str2 = "";
    }
    int[] arrayOfInt2 = new int[this.max_rows];
    for (int j = 0; j < this.max_rows; j++)
    {
      arrayOfInt2[j] = (((Vector_Int)this.lines.elementAt(j)).size() - 1);
      ??? += arrayOfInt2[j];
      arrayOfInt1[j] = 0;
      arrayOfVector_Int[j] = new Vector_Int(20);
    }
    float f5;
    while (true)
    {
      float f2 = 9999.0F;
      float f3 = 9999.0F;
      float f8 = 9999.0F;
      float f10 = 0.0F;
      int i5 = 1;
      float f11 = 0.0F;
      float f12 = 0.0F;
      float f13 = 0.0F;
      String str4 = "center";
      if (??? < ???)
      {
        for (j = 0; j < this.max_rows; j++)
          if (arrayOfInt2[j] > arrayOfInt1[j])
          {
            i = ((Vector_Int)this.lines.elementAt(j)).elementAt(arrayOfInt1[j]);
            f5 = arrayOfFloat1[i];
            float f6 = arrayOfFloat2[i];
            if (f5 < f2)
              f2 = f5;
            if (f6 < f3)
              f3 = f6;
          }
        localVector_Float2.addElement(f2);
        float f4 = f3;
        float f7;
        float f9;
        for (j = 0; j < this.max_rows; j++)
        {
          i = ((Vector_Int)this.lines.elementAt(j)).elementAt(arrayOfInt1[j]);
          f7 = arrayOfFloat1[i];
          f9 = arrayOfFloat2[i];
          if (((f7 >= f2 ? 1 : 0) & (f7 < f3 ? 1 : 0) & (f9 > f4 ? 1 : 0)) != 0)
            f4 = f9;
          if (arrayOfInt1[j] < arrayOfInt2[j])
          {
            i = ((Vector_Int)this.lines.elementAt(j)).elementAt(arrayOfInt1[j] + 1);
            f5 = arrayOfFloat1[i];
            if (((f5 > f3 ? 1 : 0) & (f5 < f8 ? 1 : 0)) != 0)
              f8 = f5;
          }
        }
        if (f2 != f4)
        {
          if (f8 == 9999.0F)
            f8 = f4;
          for (j = 0; j < this.max_rows; j++)
          {
            i = ((Vector_Int)this.lines.elementAt(j)).elementAt(arrayOfInt1[j]);
            f7 = arrayOfFloat1[i];
            f9 = arrayOfFloat2[i];
            if (((f7 >= f2 ? 1 : 0) & (f7 < f3 ? 1 : 0) & (f9 <= f8 ? 1 : 0)) != 0)
            {
              f11 += f7;
              f12 += f9;
              f10 += 1.0F;
            }
          }
          if (j == 0)
            f13 = f2;
          float f14;
          if (f8 == -1.0F)
            f14 = 0.0F;
          else
            f14 = (int)((f8 - f4) / 2.0F);
          int i6 = (int)(f4 - f2 + f14 + f13);
          f13 = f14;
          localVector_Float1.addElement(i6);
          float f15 = f11 / f10 - f2;
          float f16 = f4 - f12 / f10;
          if (f15 < 1.0F)
            str4 = "left";
          else if (f16 < 1.0F)
            str4 = "right";
          localVector_String.addElement(str4);
          for (j = 0; j < this.max_rows; j++)
          {
            this.master = ((Vector_Int)this.lines.elementAt(j)).elementAt(0);
            if (arrayOfInt2[j] > arrayOfInt1[j])
            {
              i = ((Vector_Int)this.lines.elementAt(j)).elementAt(arrayOfInt1[j]);
              f7 = arrayOfFloat1[i];
              i5 = 0;
            }
            else
            {
              i = -1;
              f7 = -1.0F;
            }
            if (((i == -1 ? 1 : 0) & (??? <= ??? ? 1 : 0)) != 0)
            {
              arrayOfVector_Int[j].addElement(-1);
            }
            else if (((f7 >= f2 ? 1 : 0) & (f7 < f4 ? 1 : 0)) != 0)
            {
              arrayOfVector_Int[j].addElement(i);
              arrayOfInt1[j] += 1;
              ???++;
            }
            else if (f7 > f4)
            {
              arrayOfVector_Int[j].addElement(-1);
            }
          }
        }
      }
      else
      {
        if (i5 != 0)
          break;
      }
    }
    for (int n = 0; n < this.max_rows; n++)
    {
      StringBuilder localStringBuilder = new StringBuilder(100);
      int i1 = arrayOfVector_Int[n].size() - 1;
      this.master = ((Vector_Int)this.lines.elementAt(n)).elementAt(0);
      for (j = 0; j < i1; j++)
      {
        i = arrayOfVector_Int[n].elementAt(j);
        if (this.isXHTML)
        {
          f5 = localVector_Float1.elementAt(j);
          String str3 = localVector_String.elementAt(j);
          int i3 = 1;
          int i4 = j + 1;
          if (i != -1)
            while (true)
            {
              int i2 = arrayOfVector_Int[n].elementAt(j + 1);
              if (((i2 != -1 ? 1 : 0) | (i1 == j + 1 ? 1 : 0)) != 0)
                break;
              if (((arrayOfInt2[n] > 1 ? 1 : 0) & (localVector_Float2.elementAt(j + 1) > arrayOfFloat2[i] ? 1 : 0)) != 0)
                break;
              i1--;
              arrayOfVector_Int[n].removeElementAt(j + 1);
              i3++;
              f5 += localVector_Float1.elementAt(i4);
              i4++;
            }
          localStringBuilder.append("<td");
          if (paramBoolean1)
          {
            localStringBuilder.append(" align='");
            localStringBuilder.append(str3);
            localStringBuilder.append('\'');
            if (i3 > 1)
              localStringBuilder.append(" colspan='").append(i3).append('\'');
          }
          if (paramBoolean2)
            localStringBuilder.append(" width='").append((int)f5).append('\'');
          localStringBuilder.append(" nowrap>");
          if (i == -1)
            localStringBuilder.append(str2);
          else
            localStringBuilder.append(this.content[i]);
          localStringBuilder.append("</td>");
        }
        else if (i == -1)
        {
          localStringBuilder.append("\"\",");
        }
        else
        {
          localStringBuilder.append('"');
          localStringBuilder.append(this.content[i]);
          localStringBuilder.append("\",");
        }
        if ((i != -1) && (this.master != i))
          merge(this.master, i, str1, false);
      }
      this.content[this.master] = localStringBuilder;
    }
  }

  private void createLinesInTable(int paramInt1, int[] paramArrayOfInt, boolean paramBoolean, int paramInt2)
    throws PdfException
  {
    if (paramInt2 == 1)
      paramArrayOfInt = reverse(paramArrayOfInt);
    float[] arrayOfFloat1;
    float[] arrayOfFloat2;
    float[] arrayOfFloat3;
    float[] arrayOfFloat4;
    switch (paramInt2)
    {
    case 0:
      arrayOfFloat1 = this.f_x1;
      arrayOfFloat2 = this.f_x2;
      arrayOfFloat3 = this.f_y1;
      arrayOfFloat4 = this.f_y2;
      break;
    case 1:
      arrayOfFloat2 = this.f_x1;
      arrayOfFloat1 = this.f_x2;
      arrayOfFloat3 = this.f_y1;
      arrayOfFloat4 = this.f_y2;
      break;
    case 3:
      arrayOfFloat1 = this.f_y1;
      arrayOfFloat2 = this.f_y2;
      arrayOfFloat3 = this.f_x2;
      arrayOfFloat4 = this.f_x1;
      break;
    case 2:
      arrayOfFloat1 = this.f_y2;
      arrayOfFloat2 = this.f_y1;
      arrayOfFloat4 = this.f_x1;
      arrayOfFloat3 = this.f_x2;
      paramArrayOfInt = getsortedUnusedFragments(false, true);
      paramArrayOfInt = reverse(paramArrayOfInt);
      break;
    default:
      throw new PdfException(new StringBuilder().append("Illegal value ").append(paramInt2).append("for currentWritingMode").toString());
    }
    for (int i = 0; i < paramInt1; i++)
    {
      int j = paramArrayOfInt[i];
      int k = -1;
      int n = j;
      float f1 = -1.0F;
      if ((this.isUsed[j] == 0) && (this.writingMode[j] == paramInt2))
      {
        Vector_Int localVector_Int = new Vector_Int(20);
        localVector_Int.addElement(j);
        this.lineY2.addElement((int)arrayOfFloat4[j]);
        while (true)
        {
          for (int i1 = 0; i1 < paramInt1; i1++)
          {
            int m = paramArrayOfInt[i1];
            if ((this.isUsed[m] == 0) && (m != j) && (this.writingMode[j] == paramInt2) && (((arrayOfFloat1[m] > arrayOfFloat1[j]) && (paramInt2 != 2)) || ((arrayOfFloat1[m] < arrayOfFloat1[j]) && (paramInt2 == 2))))
            {
              float f2 = arrayOfFloat1[m] - arrayOfFloat2[j];
              if ((paramInt2 == 1) || (paramInt2 == 2))
                f2 = -f2;
              if ((f2 < 0.0F) && (f2 > -2.0F))
                f2 = 0.0F;
              float f3 = (arrayOfFloat3[m] + arrayOfFloat4[m]) / 2.0F;
              if ((f3 < arrayOfFloat3[j]) && (f3 > arrayOfFloat4[j]) && ((f1 < 0.0F) || (f2 < f1)))
              {
                f1 = f2;
                k = m;
              }
            }
          }
          if (k == -1)
            break;
          float f4 = arrayOfFloat1[k] - arrayOfFloat2[n];
          float f5 = arrayOfFloat1[k] - arrayOfFloat2[j];
          float f6 = 1.5F * ((arrayOfFloat2[k] - arrayOfFloat1[k]) / this.textLength[k]);
          float f7 = 1.5F * ((arrayOfFloat2[n] - arrayOfFloat1[n]) / this.textLength[n]);
          if ((paramInt2 == 1) || (paramInt2 == 2))
          {
            f5 = -f5;
            f4 = -f4;
            f6 = -f6;
            f7 = -f7;
          }
          if ((f4 < f6) && (f4 < f7))
          {
            merge(n, k, isGapASpace(k, n, f5, paramBoolean, paramInt2), true);
          }
          else
          {
            localVector_Int.addElement(k);
            n = k;
          }
          this.isUsed[k] = true;
          k = -1;
          f1 = 1000000.0F;
        }
        this.lines.addElement(localVector_Int);
        this.max_rows += 1;
      }
    }
  }

  public final Map extractTextAsTable(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, int paramInt6)
    throws PdfException
  {
    int[] arrayOfInt1 = validateCoordinates(paramInt1, paramInt2, paramInt3, paramInt4);
    paramInt1 = arrayOfInt1[0];
    paramInt2 = arrayOfInt1[1];
    paramInt3 = arrayOfInt1[2];
    paramInt4 = arrayOfInt1[3];
    HashMap localHashMap = new HashMap();
    LogWriter.writeLog("extracting Text As Table");
    this.isXHTML = (!paramBoolean1);
    this.lines = new Vector_Object(20);
    this.lineY2 = new Vector_Int(20);
    this.max_rows = 0;
    copyToArrays(paramInt1, paramInt4, paramInt3, paramInt2, paramBoolean2, false, true, null, false);
    removeEncoding();
    cleanupShadowsAndDrownedObjects(false);
    int[] arrayOfInt2 = getsortedUnusedFragments(true, false);
    int i = arrayOfInt2.length;
    if (i == 0)
      return localHashMap;
    int j = getWritingMode(arrayOfInt2, i);
    String str1 = new StringBuilder().append("Table Merging algorithm being applied ").append(i).append(" items").toString();
    LogWriter.writeLog(str1);
    if (i > 1)
    {
      createLinesInTable(i, arrayOfInt2, this.isXHTML, j);
      int k = 1;
      if ((j == 0) || (j == 2))
        k = -1;
      this.line_order = new int[this.max_rows];
      int[] arrayOfInt3 = new int[this.max_rows];
      for (int m = 0; m < this.max_rows; m++)
      {
        arrayOfInt3[m] = (k * this.lineY2.elementAt(m));
        this.line_order[m] = m;
      }
      this.line_order = Sorts.quicksort(arrayOfInt3, this.line_order);
      createTableRows(paramBoolean4, paramBoolean3, j);
      mergeTableRows(paramInt6);
    }
    this.content[this.master] = cleanup(this.content[this.master]);
    String str2 = this.content[this.master].toString();
    if (str2 != null)
    {
      if (!paramBoolean1)
        str2 = Fonts.cleanupTokens(str2);
      localHashMap.put("content", str2);
      localHashMap.put("x1", String.valueOf(paramInt1));
      localHashMap.put("x2", String.valueOf(paramInt3));
      localHashMap.put("y1", String.valueOf(paramInt2));
      localHashMap.put("y2", String.valueOf(paramInt4));
    }
    return localHashMap;
  }

  private static int[] validateCoordinates(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    throws PdfException
  {
    if (((paramInt1 > paramInt3 ? 1 : 0) | (paramInt2 < paramInt4 ? 1 : 0)) != 0)
    {
      int i;
      if (paramInt1 > paramInt3)
      {
        i = paramInt1;
        paramInt1 = paramInt3;
        paramInt3 = i;
        LogWriter.writeLog("x1 > x2, coordinates were swapped to validate");
      }
      if (paramInt2 < paramInt4)
      {
        i = paramInt2;
        paramInt2 = paramInt4;
        paramInt4 = i;
        LogWriter.writeLog("y1 < y2, coordinates were swapped to validate");
      }
    }
    return new int[] { paramInt1, paramInt2, paramInt3, paramInt4 };
  }

  public final List extractTextAsWordlist(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, boolean paramBoolean, String paramString)
    throws PdfException
  {
    int[] arrayOfInt1 = validateCoordinates(paramInt1, paramInt2, paramInt3, paramInt4);
    paramInt1 = arrayOfInt1[0];
    paramInt2 = arrayOfInt1[1];
    paramInt3 = arrayOfInt1[2];
    paramInt4 = arrayOfInt1[3];
    if (paramBoolean)
      copyToArrays(paramInt1, paramInt4, paramInt3, paramInt2, true, true, false, paramString, true);
    else
      copyToArrays();
    removeEncoding();
    cleanupShadowsAndDrownedObjects(true);
    int[] arrayOfInt2 = getsortedUnusedFragments(true, false);
    int i = arrayOfInt2.length;
    if (i == 0)
    {
      LogWriter.writeLog("Less than 1 text item on page");
      return null;
    }
    int j = getWritingMode(arrayOfInt2, i);
    createLines(i, arrayOfInt2, j, true, false, false);
    float[] arrayOfFloat1 = null;
    float[] arrayOfFloat2 = null;
    float[] arrayOfFloat3 = null;
    float[] arrayOfFloat4 = null;
    if ((useUnrotatedCoords) || (j == 0))
    {
      arrayOfFloat1 = this.f_x1;
      arrayOfFloat2 = this.f_x2;
      arrayOfFloat3 = this.f_y1;
      arrayOfFloat4 = this.f_y2;
    }
    else if (j == 1)
    {
      arrayOfFloat2 = this.f_x1;
      arrayOfFloat1 = this.f_x2;
      arrayOfFloat3 = this.f_y1;
      arrayOfFloat4 = this.f_y2;
    }
    else if (j == 3)
    {
      arrayOfFloat1 = this.f_y2;
      arrayOfFloat2 = this.f_y1;
      arrayOfFloat3 = this.f_x2;
      arrayOfFloat4 = this.f_x1;
    }
    else if (j == 2)
    {
      arrayOfFloat1 = this.f_y1;
      arrayOfFloat2 = this.f_y2;
      arrayOfFloat4 = this.f_x1;
      arrayOfFloat3 = this.f_x2;
    }
    ArrayList localArrayList = new ArrayList();
    for (int k = 0; k < this.content.length; k++)
      if (this.content[k] != null)
      {
        if ((this.colorExtracted) && (this.isXMLExtraction))
        {
          if (!this.content[k].toString().toLowerCase().startsWith("<color "))
            this.content[k].insert(0, this.f_colorTag[this.master]);
          if (!this.content[k].toString().toLowerCase().endsWith("</color>"))
            this.content[k].append("</color>");
        }
        if (this.isXMLExtraction)
          localArrayList.add(this.content[k].toString());
        else
          localArrayList.add(Strip.convertToText(this.content[k].toString(), this.isXMLExtraction));
        if ((!useUnrotatedCoords) && (j == 2))
        {
          localArrayList.add(String.valueOf(arrayOfFloat1[k]));
          localArrayList.add(String.valueOf(arrayOfFloat3[k]));
          localArrayList.add(String.valueOf(arrayOfFloat2[k]));
          localArrayList.add(String.valueOf(arrayOfFloat4[k]));
        }
        else if ((!useUnrotatedCoords) && (j == 3))
        {
          localArrayList.add(String.valueOf(arrayOfFloat1[k]));
          localArrayList.add(String.valueOf(arrayOfFloat4[k]));
          localArrayList.add(String.valueOf(arrayOfFloat2[k]));
          localArrayList.add(String.valueOf(arrayOfFloat3[k]));
        }
        else
        {
          localArrayList.add(String.valueOf(arrayOfFloat1[k]));
          localArrayList.add(String.valueOf(arrayOfFloat3[k]));
          localArrayList.add(String.valueOf(arrayOfFloat2[k]));
          localArrayList.add(String.valueOf(arrayOfFloat4[k]));
        }
      }
    LogWriter.writeLog("Text extraction as wordlist completed");
    return localArrayList;
  }

  private void reset()
  {
    this.isXHTML = true;
    this.nextSlot = 0;
    this.lineBreaks = new Vector_Int();
    this.max_rows = 0;
    this.master = 0;
    this.colorExtracted = false;
  }

  public final String extractTextInRectangle(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, boolean paramBoolean1, boolean paramBoolean2)
    throws PdfException
  {
    reset();
    if ((paramBoolean2) && (!this.pdf_data.IsEmbedded()))
      throw new PdfException("[PDF] Request to breakfragments and width not added. Please add call to init(true) of PdfDecoder to your code.");
    int[] arrayOfInt1 = validateCoordinates(paramInt1, paramInt2, paramInt3, paramInt4);
    paramInt1 = arrayOfInt1[0];
    paramInt2 = arrayOfInt1[1];
    paramInt3 = arrayOfInt1[2];
    paramInt4 = arrayOfInt1[3];
    if (paramBoolean2)
      copyToArrays(paramInt1, paramInt4, paramInt3, paramInt2, this.isXMLExtraction, false, false, null, false);
    else
      copyToArrays();
    removeEncoding();
    cleanupShadowsAndDrownedObjects(false);
    int[] arrayOfInt2 = getsortedUnusedFragments(true, false);
    int j = arrayOfInt2.length;
    if (j == 0)
    {
      LogWriter.writeLog("Less than 1 text item on page");
      return null;
    }
    int k = getWritingMode(arrayOfInt2, j);
    createLines(j, arrayOfInt2, k, false, this.isXMLExtraction, false);
    int i = mergeLinesTogether(k, paramBoolean1, paramInt1, paramInt3, paramInt2, paramInt4);
    if (this.isXMLExtraction)
    {
      this.content[i] = new StringBuilder(Fonts.cleanupTokens(this.content[i].toString()));
      this.content[i].insert(0, "<p>");
      this.content[i].append("</p>");
    }
    LogWriter.writeLog("Text extraction completed");
    return cleanup(this.content[i]).toString();
  }

  private StringBuilder cleanup(StringBuilder paramStringBuilder)
  {
    if (paramStringBuilder == null)
      return paramStringBuilder;
    if (this.isXMLExtraction)
    {
      String str1 = paramStringBuilder.toString();
      str1 = str1.replaceAll("&#", "XX#");
      str1 = str1.replaceAll("&lt", "XXlt");
      str1 = str1.replaceAll("&gt", "XXgt");
      str1 = str1.replaceAll("&", "&amp;");
      str1 = str1.replaceAll("XX#", "&#");
      str1 = str1.replaceAll("XXlt", "&lt");
      str1 = str1.replaceAll("XXgt", "&gt");
      int i = 1;
      if (i != 0)
      {
        HashMap localHashMap = new HashMap();
        for (int j = 1; j <= 8; j++)
          localHashMap.put(new StringBuilder().append("&#").append(j).append(';').toString(), "");
        for (j = 11; j <= 12; j++)
          localHashMap.put(new StringBuilder().append("&#").append(j).append(';').toString(), "");
        for (j = 14; j <= 31; j++)
          localHashMap.put(new StringBuilder().append("&#").append(j).append(';').toString(), "");
        Iterator localIterator = localHashMap.keySet().iterator();
        while (localIterator.hasNext())
        {
          Object localObject = localIterator.next();
          String str2 = (String)localObject;
          String str3 = (String)localHashMap.get(str2);
          str1 = str1.replace(str2, str3);
        }
      }
      paramStringBuilder = new StringBuilder(str1);
    }
    return paramStringBuilder;
  }

  private int getWritingMode(int[] paramArrayOfInt, int paramInt)
  {
    int i = this.writingMode[paramArrayOfInt[0]];
    if ((i == 0) || (i == 1))
      return i;
    for (int j = 1; j < paramInt; j++)
    {
      int k = paramArrayOfInt[j];
      if ((this.isUsed[k] == 0) && ((this.writingMode[k] == 0) || (this.writingMode[k] == 1)))
      {
        i = this.writingMode[k];
        j = paramInt;
        LogWriter.writeLog("Text of multiple orientations found. Only horizontal text used.");
      }
    }
    return i;
  }

  private int mergeLinesTogether(int paramInt1, boolean paramBoolean, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
    throws PdfException
  {
    float[] arrayOfFloat1;
    float[] arrayOfFloat2;
    float[] arrayOfFloat3;
    float[] arrayOfFloat4;
    int[] arrayOfInt;
    int i;
    if (paramInt1 == 0)
    {
      arrayOfFloat1 = this.f_x1;
      arrayOfFloat2 = this.f_x2;
      arrayOfFloat3 = this.f_y1;
      arrayOfFloat4 = this.f_y2;
      arrayOfInt = getsortedUnusedFragments(false, true);
      i = (paramInt2 + paramInt3) / 2;
    }
    else if (paramInt1 == 1)
    {
      arrayOfFloat2 = this.f_x1;
      arrayOfFloat1 = this.f_x2;
      arrayOfFloat3 = this.f_y1;
      arrayOfFloat4 = this.f_y2;
      arrayOfInt = getsortedUnusedFragments(false, true);
      i = (paramInt2 + paramInt3) / 2;
    }
    else if (paramInt1 == 3)
    {
      arrayOfFloat1 = this.f_y1;
      arrayOfFloat2 = this.f_y2;
      arrayOfFloat3 = this.f_x2;
      arrayOfFloat4 = this.f_x1;
      arrayOfInt = getsortedUnusedFragments(true, true);
      arrayOfInt = reverse(arrayOfInt);
      i = (paramInt4 + paramInt5) / 2;
    }
    else if (paramInt1 == 2)
    {
      arrayOfFloat1 = this.f_y2;
      arrayOfFloat2 = this.f_y1;
      arrayOfFloat4 = this.f_x2;
      arrayOfFloat3 = this.f_x1;
      arrayOfInt = getsortedUnusedFragments(true, true);
      i = (paramInt4 + paramInt5) / 2;
    }
    else
    {
      throw new PdfException(new StringBuilder().append("Illegal value ").append(paramInt1).append("for currentWritingMode").toString());
    }
    int j = i / 2;
    int k = arrayOfInt.length;
    int m = arrayOfInt[(k - 1)];
    for (int i3 = k - 2; i3 > -1; i3--)
    {
      int i4 = arrayOfInt[i3];
      String str1 = "";
      int n = getLastChar(this.content[i4]);
      if (n != -1)
      {
        addAlignmentFormatting(paramBoolean, i, arrayOfFloat1, arrayOfFloat2, j, i4);
        String str2 = new StringBuilder().append("</p>").append(SystemSeparator).append("<p>").toString();
        if (this.isXMLExtraction)
          str2 = SystemSeparator;
        float f1 = arrayOfFloat4[m] - arrayOfFloat3[i4];
        float f2 = arrayOfFloat3[i4] - arrayOfFloat4[i4];
        if (paramInt1 == 3)
        {
          f1 = -f1;
          f2 = -f2;
        }
        if (((f1 > f2 ? 1 : 0) & (f2 > 0.0F ? 1 : 0)) != 0)
        {
          while (f1 > f2)
          {
            str1 = new StringBuilder().append(str1).append(str2).toString();
            f1 -= f2;
          }
          if (this.isXMLExtraction)
            str1 = new StringBuilder().append(str1).append("</p>").append(SystemSeparator).append("<p>").toString();
          else
            str1 = SystemSeparator;
        }
        else if (paramBoolean)
        {
          int i2 = getFirstChar(this.content[i4]);
          int i1 = getLastChar(this.content[m]);
          if (((i1 == 46) || (i1 == 34)) && (i2 >= 65) && (i2 <= 90))
            if (this.isXMLExtraction)
              str1 = new StringBuilder().append("</p>").append(SystemSeparator).append("<p>").toString();
            else
              str1 = SystemSeparator;
        }
        else if (this.isXMLExtraction)
        {
          this.content[i4].insert(0, new StringBuilder().append("</p>").append(SystemSeparator).append("<p>").toString());
        }
        else
        {
          this.content[m].append(SystemSeparator);
        }
        merge(m, i4, str1, false);
      }
    }
    return m;
  }

  private int getFirstChar(StringBuilder paramStringBuilder)
  {
    int i = -1;
    int j = 0;
    int k = paramStringBuilder.length();
    int m = 32;
    for (int n = 0; n < k; n++)
    {
      int i1 = paramStringBuilder.charAt(n);
      if ((j == 0) && ((i1 == 60) || ((this.isXMLExtraction) && (i1 == 38))))
      {
        j = 1;
        m = i1;
        if (m == 38)
          if (n + 1 == k)
          {
            i = 38;
            n = k;
          }
          else
          {
            int i2 = paramStringBuilder.charAt(n + 1);
            if ((i2 != 35) && (i2 != 103) && (i2 != 108))
            {
              i = 38;
              n = k;
            }
          }
      }
      if ((j == 0) && (i1 != 32))
      {
        i = i1;
        n = k;
      }
      if ((j != 0) && (m == 38) && (i1 == 32))
      {
        i = m;
        n = k;
      }
      else if ((j != 0) && ((i1 == 62) || ((this.isXMLExtraction) && (m == 38) && (i1 == 59))))
      {
        if ((i1 == 59) && (m == 38))
          if (((n > 2 ? 1 : 0) & (paramStringBuilder.charAt(n - 1) == 't' ? 1 : 0)) != 0)
            if (paramStringBuilder.charAt(n - 2) == 'l')
            {
              i = 60;
              n = k;
            }
            else if (paramStringBuilder.charAt(n - 2) == 'g')
            {
              i = 62;
              n = k;
            }
        j = 0;
      }
    }
    return i;
  }

  private int getLastChar(StringBuilder paramStringBuilder)
  {
    int i = -1;
    int j = 0;
    int k = paramStringBuilder.length();
    int m = k;
    int n = 32;
    k--;
    while (k > -1)
    {
      int i1 = paramStringBuilder.charAt(k);
      if ((j != 0) && (n == 59) && (i1 == 59))
      {
        i = 59;
        k = -1;
      }
      if ((j == 0) && ((i1 == 62) || ((this.isXMLExtraction) && (i1 == 59))))
      {
        j = 1;
        int i2 = paramStringBuilder.lastIndexOf("</");
        if (i2 == -1)
          j = 0;
        else
          for (int i4 = i2; i4 < k; i4++)
          {
            int i3 = paramStringBuilder.charAt(i4);
            if ((i3 == 32) || (i3 == 62))
            {
              j = 0;
              i4 = k;
            }
          }
        if (j != 0)
        {
          n = i1;
        }
        else
        {
          i = i1;
          k = -1;
        }
      }
      if ((j == 0) && (i1 != 32))
      {
        i = i1;
        k = -1;
      }
      if ((i1 == 60) || ((this.isXMLExtraction) && (n == 59) && (i1 == 38)))
      {
        j = 0;
        if (i1 == 38)
          if ((((k + 3 < m ? 1 : 0) & (paramStringBuilder.charAt(k + 2) == 't' ? 1 : 0)) != 0) && (paramStringBuilder.charAt(k + 3) == ';'))
            if (paramStringBuilder.charAt(k + 1) == 'l')
            {
              i = 60;
              k = -1;
            }
            else if (paramStringBuilder.charAt(k + 1) == 'g')
            {
              i = 62;
              k = -1;
            }
      }
      if ((j != 0) && (n == 59) && (i1 == 32))
      {
        k = -1;
        i = 59;
      }
      k--;
    }
    return i;
  }

  private static int[] reverse(int[] paramArrayOfInt)
  {
    int i = paramArrayOfInt.length;
    int[] arrayOfInt = new int[i];
    for (int j = 0; j < i; j++)
      arrayOfInt[j] = paramArrayOfInt[(i - j - 1)];
    return arrayOfInt;
  }

  private void addAlignmentFormatting(boolean paramBoolean, int paramInt1, float[] paramArrayOfFloat1, float[] paramArrayOfFloat2, int paramInt2, int paramInt3)
  {
    float f1 = paramInt1 - paramArrayOfFloat1[paramInt3];
    float f2 = paramArrayOfFloat2[paramInt3] - paramInt1;
    if ((!paramBoolean) && (this.isXMLExtraction) && (f1 > 0.0F) && (f2 > 0.0F) && (paramArrayOfFloat1[paramInt3] > paramInt2) && (paramArrayOfFloat1[paramInt3] < paramInt1 + paramInt2))
    {
      float f3 = f1 / f2;
      if (f3 > 1.0F)
        f3 = 1.0F / f3;
      if (f3 > 0.95D)
      {
        this.content[paramInt3] = new StringBuilder(Fonts.cleanupTokens(this.content[paramInt3].toString()));
        this.content[paramInt3].insert(0, "<center>");
        this.content[paramInt3].append("</center>\n");
      }
      else if (((f2 < 10.0F ? 1 : 0) & (f1 > 30.0F ? 1 : 0)) != 0)
      {
        this.content[paramInt3] = new StringBuilder(Fonts.cleanupTokens(this.content[paramInt3].toString()));
        this.content[paramInt3].insert(0, "<right>");
        this.content[paramInt3].append("</right>\n");
      }
    }
  }

  private void createLines(int paramInt1, int[] paramArrayOfInt, int paramInt2, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
    throws PdfException
  {
    if ((paramInt2 == 1) || (paramInt2 == 2))
      paramArrayOfInt = reverse(paramArrayOfInt);
    float[] arrayOfFloat1;
    float[] arrayOfFloat2;
    float[] arrayOfFloat3;
    float[] arrayOfFloat4;
    if (paramInt2 == 0)
    {
      arrayOfFloat1 = this.f_x1;
      arrayOfFloat2 = this.f_x2;
      arrayOfFloat3 = this.f_y1;
      arrayOfFloat4 = this.f_y2;
    }
    else if (paramInt2 == 1)
    {
      arrayOfFloat2 = this.f_x1;
      arrayOfFloat1 = this.f_x2;
      arrayOfFloat3 = this.f_y1;
      arrayOfFloat4 = this.f_y2;
    }
    else if (paramInt2 == 3)
    {
      arrayOfFloat1 = this.f_y1;
      arrayOfFloat2 = this.f_y2;
      arrayOfFloat3 = this.f_x2;
      arrayOfFloat4 = this.f_x1;
    }
    else if (paramInt2 == 2)
    {
      arrayOfFloat1 = this.f_y2;
      arrayOfFloat2 = this.f_y1;
      arrayOfFloat4 = this.f_x1;
      arrayOfFloat3 = this.f_x2;
    }
    else
    {
      throw new PdfException(new StringBuilder().append("Illegal value ").append(paramInt2).append("for currentWritingMode").toString());
    }
    for (int i = 0; i < paramInt1; i++)
    {
      int j = -1;
      int m = paramArrayOfInt[i];
      float f1 = -1.0F;
      if ((this.isUsed[m] == 0) && (this.writingMode[m] == paramInt2))
        while (true)
        {
          for (int n = 0; n < paramInt1; n++)
          {
            int k = paramArrayOfInt[n];
            if (this.isUsed[k] == 0)
            {
              int i1 = (int)(arrayOfFloat4[k] - arrayOfFloat4[m]);
              if (i1 < 0)
                i1 = -i1;
              int i2 = (int)(arrayOfFloat3[k] - arrayOfFloat3[m]);
              if (i2 < 0)
                i2 = -i2;
              int i3 = (int)(arrayOfFloat1[k] - arrayOfFloat2[m]);
              if (i3 > (int)(arrayOfFloat1[m] - arrayOfFloat2[k]))
                i3 = (int)(arrayOfFloat1[m] - arrayOfFloat2[k]);
              int i4 = this.fontSize[m] - this.fontSize[k];
              if (i4 < 0)
                i4 = -i4;
              if (((!paramBoolean3) || (i3 <= this.fontSize[m]) || (i3 <= 0)) && ((!paramBoolean3) || (i1 <= 1) || (i3 <= 2 * this.fontSize[m]) || (this.fontSize[m] != this.fontSize[k])) && ((!paramBoolean3) || (i1 <= 3)) && ((!paramBoolean3) || (i4 <= 2)) && (k != m) && (((arrayOfFloat1[k] > arrayOfFloat1[m]) && (paramInt2 != 2)) || ((arrayOfFloat1[k] < arrayOfFloat1[m]) && (paramInt2 == 2) && (this.writingMode[m] == paramInt2) && ((i4 <= 2) || ((i4 > 2) && (i2 < 3))))))
              {
                float f2 = arrayOfFloat1[k] - arrayOfFloat2[m];
                if ((paramInt2 == 1) || (paramInt2 == 2))
                  f2 = -f2;
                if ((f2 < 0.0F) && (f2 > -2.0F))
                  f2 = 0.0F;
                float f3 = (arrayOfFloat3[k] + arrayOfFloat4[k]) / 2.0F;
                if ((f3 < arrayOfFloat3[m]) && (f3 > arrayOfFloat4[m]) && ((f1 < 0.0F) || (f2 < f1)))
                {
                  f1 = f2;
                  j = k;
                }
              }
            }
          }
          if (j == -1)
            break;
          float f4 = arrayOfFloat1[j] - arrayOfFloat2[m];
          if ((paramInt2 == 1) || (paramInt2 == 2))
            f4 = -f4;
          else if (paramInt2 == 3)
            f4 = arrayOfFloat2[j] - arrayOfFloat1[m];
          String str = isGapASpace(m, j, f4, paramBoolean2, paramInt2);
          if ((paramBoolean1) && (this.hadSpace != null) && ((this.hadSpace[m] != 0) || (str.startsWith(" "))))
            break;
          merge(m, j, str, true);
          j = -1;
          f1 = 1000000.0F;
        }
    }
  }

  public SortedMap findMultipleTermsInRectangleWithMatchingTeasers(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, String[] paramArrayOfString, int paramInt7, SearchListener paramSearchListener)
    throws PdfException
  {
    this.usingMultipleTerms = true;
    this.multipleTermTeasers.clear();
    this.teasers = null;
    boolean bool = this.includeTease;
    this.includeTease = true;
    List localList = findMultipleTermsInRectangle(paramInt1, paramInt2, paramInt3, paramInt4, paramInt6, paramArrayOfString, paramInt7, paramSearchListener);
    TreeMap localTreeMap = new TreeMap(new ResultsComparator(paramInt5));
    for (int i = 0; i < localList.size(); i++)
      localTreeMap.put(localList.get(i), this.multipleTermTeasers.get(i));
    this.usingMultipleTerms = false;
    this.includeTease = bool;
    return localTreeMap;
  }

  public List findMultipleTermsInRectangle(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, String[] paramArrayOfString, boolean paramBoolean, int paramInt7, SearchListener paramSearchListener)
    throws PdfException
  {
    this.usingMultipleTerms = true;
    this.multipleTermTeasers.clear();
    this.teasers = null;
    List localList = findMultipleTermsInRectangle(paramInt1, paramInt2, paramInt3, paramInt4, paramInt6, paramArrayOfString, paramInt7, paramSearchListener);
    if (paramBoolean)
      Collections.sort(localList, new ResultsComparator(paramInt5));
    this.usingMultipleTerms = false;
    return localList;
  }

  private List findMultipleTermsInRectangle(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, String[] paramArrayOfString, int paramInt6, SearchListener paramSearchListener)
    throws PdfException
  {
    ArrayList localArrayList = new ArrayList();
    for (String str : paramArrayOfString)
    {
      if ((paramSearchListener != null) && (paramSearchListener.isCanceled()))
        break;
      float[] arrayOfFloat = findText(new Rectangle(paramInt1, paramInt2, paramInt3, paramInt4), paramInt5, new String[] { str }, paramInt6);
      if (arrayOfFloat != null)
      {
        int k = arrayOfFloat.length;
        int m = 0;
        while (m < k)
        {
          int n = (int)arrayOfFloat[m];
          int i1 = (int)arrayOfFloat[(m + 1)];
          int i2 = (int)arrayOfFloat[(m + 2)];
          int i3 = (int)arrayOfFloat[(m + 3)];
          Rectangle localRectangle = new Rectangle(n, i3, i2 - n, i1 - i3);
          int i4 = (int)arrayOfFloat[(m + 4)];
          if (i4 == this.linkedSearchAreas)
          {
            Vector_Rectangle localVector_Rectangle = new Vector_Rectangle();
            localVector_Rectangle.addElement(localRectangle);
            while (i4 == this.linkedSearchAreas)
            {
              m += 5;
              n = (int)arrayOfFloat[m];
              i1 = (int)arrayOfFloat[(m + 1)];
              i2 = (int)arrayOfFloat[(m + 2)];
              i3 = (int)arrayOfFloat[(m + 3)];
              i4 = (int)arrayOfFloat[(m + 4)];
              localRectangle = new Rectangle(n, i3, i2 - n, i1 - i3);
              localVector_Rectangle.addElement(localRectangle);
            }
            localVector_Rectangle.trim();
            localArrayList.add(localVector_Rectangle.get());
          }
          else
          {
            localArrayList.add(localRectangle);
          }
          m += 5;
        }
      }
    }
    return localArrayList;
  }

  public final float[] findText(Rectangle paramRectangle, int paramInt1, String[] paramArrayOfString, int paramInt2)
    throws PdfException
  {
    if (paramArrayOfString == null)
      return new float[0];
    int i = 0;
    int j = 0;
    int k = 0;
    int m = 0;
    Vector_Float localVector_Float = new Vector_Float(0);
    Vector_String localVector_String = new Vector_String(0);
    copyToArrays();
    cleanupShadowsAndDrownedObjects(false);
    int[] arrayOfInt1 = getsortedUnusedFragments(true, false);
    int n = 0;
    int i1 = 0;
    int i2 = 0;
    int i3 = 0;
    for (int i4 = 0; i4 != arrayOfInt1.length; i4++)
      switch (this.writingMode[arrayOfInt1[i4]])
      {
      case 0:
        n++;
        break;
      case 1:
        i1++;
        break;
      case 2:
        i2++;
        break;
      case 3:
        i3++;
      }
    int[] arrayOfInt2 = { n, i1, i2, i3 };
    int[] arrayOfInt3 = { n, i1, i2, i3 };
    int[] arrayOfInt4 = { -1, -1, -1, -1 };
    Arrays.sort(arrayOfInt3);
    int i6;
    int i7;
    for (int i5 = 0; i5 != arrayOfInt2.length; i5++)
      for (i6 = 0; i6 < arrayOfInt3.length; i6++)
        if (arrayOfInt2[i5] == arrayOfInt3[i6])
        {
          i7 = i6 - 3;
          if (i7 < 0)
            i7 = -i7;
          if (arrayOfInt4[i7] == -1)
          {
            arrayOfInt4[i7] = i5;
            i6 = arrayOfInt3.length;
          }
        }
    for (i5 = 0; i5 != arrayOfInt4.length; i5++)
    {
      i6 = arrayOfInt4[i5];
      if (arrayOfInt2[i6] != 0)
      {
        createLines(arrayOfInt1.length, arrayOfInt1, i6, true, false, true);
        i7 = 0;
        if ((paramInt2 & 0x2) != 2)
          i7 |= 2;
        if ((paramInt2 & 0x4) == 4)
          i = 1;
        if ((paramInt2 & 0x1) == 1)
          j = 1;
        if ((paramInt2 & 0x8) == 8)
          i7 = i7 | 0x8 | 0x20;
        if ((paramInt2 & 0x20) == 32)
          m = 1;
        float[] arrayOfFloat1 = this.f_y1;
        float[] arrayOfFloat2 = this.f_y2;
        int i8 = 0;
        if (i6 == 0)
        {
          arrayOfFloat1 = this.f_y1;
          arrayOfFloat2 = this.f_y2;
        }
        else if (i6 == 1)
        {
          arrayOfFloat1 = this.f_y1;
          arrayOfFloat2 = this.f_y2;
        }
        else if (i6 == 3)
        {
          arrayOfFloat1 = this.f_x2;
          arrayOfFloat2 = this.f_x1;
          i8 = 1;
        }
        else if (i6 == 2)
        {
          arrayOfFloat2 = this.f_x1;
          arrayOfFloat1 = this.f_x2;
          i8 = 1;
        }
        String str1 = "";
        String str2 = "";
        for (int i9 = 0; i9 != this.content.length; i9++)
          if ((this.content[i9] != null) && (i6 == this.writingMode[i9]))
          {
            str2 = new StringBuilder().append(str2).append(this.content[i9]).append("\n").toString();
            str1 = new StringBuilder().append(str1).append(this.content[i9]).append("\n").toString();
          }
        str2 = removeDuplicateSpaces(str2);
        str1 = removeDuplicateSpaces(str1);
        str2 = Strip.stripXML(str2, this.isXMLExtraction).toString();
        str1 = removeHiddenMarkers(str1);
        str1 = Strip.stripXML(str1, this.isXMLExtraction).toString();
        String[] arrayOfString1 = { str1 };
        String[] arrayOfString2 = { str2 };
        for (int i10 = 0; i10 != paramArrayOfString.length; i10++)
        {
          String str3 = paramArrayOfString[i10];
          String str4 = " ";
          if ((paramInt2 & 0x8) == 8)
            str4 = "[ \\\\n]";
          if (m == 0)
          {
            str3 = new StringBuilder().append("\\Q").append(str3).append("\\E").toString();
            str4 = new StringBuilder().append("\\\\E").append(str4).append("\\\\Q").toString();
          }
          if (!str4.equals(" "))
            str3 = str3.replaceAll(" ", str4);
          if (j != 0)
            str3 = new StringBuilder().append("\\b").append(str3).append("\\b").toString();
          Pattern localPattern1 = Pattern.compile(str3, i7);
          Pattern localPattern2 = Pattern.compile(new StringBuilder().append("(?:\\S+\\s)?\\S*(?:\\S+\\s)?\\S*").append(str3).append("\\S*(?:\\s\\S+)?\\S*(?:\\s\\S+)?").toString(), i7);
          for (int i11 = 0; i11 != arrayOfString1.length; i11++)
          {
            String str5 = arrayOfString1[i11];
            String str6 = arrayOfString2[i11];
            if (str5 != null)
            {
              Matcher localMatcher1 = localPattern1.matcher(str5);
              Matcher localMatcher2 = localPattern2.matcher(str5);
              int i12 = 1;
              while (localMatcher1.find())
              {
                Point localPoint = null;
                String str7 = localMatcher1.group();
                int i13 = localMatcher1.start();
                int i14 = localMatcher1.end() - 1;
                if (this.includeTease)
                {
                  String str8 = str7;
                  if (this.includeHTMLtags)
                    str8 = new StringBuilder().append("<b>").append(str8).append("</b>").toString();
                  boolean bool = false;
                  if (i12 != 0)
                    bool = localMatcher2.find();
                  if (bool)
                    if ((localMatcher2.start() < i13) && (localMatcher2.end() > i14))
                    {
                      str8 = localMatcher2.group();
                      if (this.includeHTMLtags)
                      {
                        i15 = i13 - localMatcher2.start();
                        i16 = i14 - localMatcher2.start() + 1;
                        str8 = new StringBuilder().append(str8.substring(0, i15)).append("<b>").append(str8.substring(i15, i16)).append("</b>").append(str8.substring(i16, str8.length())).toString();
                      }
                      i12 = 1;
                    }
                    else
                    {
                      i12 = 0;
                    }
                  localVector_String.addElement(str8);
                }
                int i15 = -1;
                for (int i16 = 0; (this.content[i16] == null) || (i6 != this.writingMode[i16]); i16++);
                int i17 = 0;
                int i18 = 0;
                for (int i19 = 1; i19 < str6.length(); i19++)
                {
                  int i20 = i19;
                  while ((i19 < str6.length()) && (str6.charAt(i19) != MARKER2))
                    i19++;
                  float f1 = Float.parseFloat(str6.substring(i20, i19));
                  i19++;
                  i20 = i19;
                  while ((i19 < str6.length()) && (str6.charAt(i19) != MARKER2))
                    i19++;
                  float f2 = Float.parseFloat(str6.substring(i20, i19));
                  i19++;
                  i20 = i19;
                  while ((i19 < str6.length()) && (str6.charAt(i19) != MARKER2))
                    i19++;
                  String str9 = str6.substring(i20, i19);
                  i15 += str9.length();
                  if ((i17 == 0) && (i15 >= i13))
                  {
                    localPoint = new Point((int)f1, (int)arrayOfFloat1[i16]);
                    i17 = 1;
                  }
                  if ((i18 == 0) && (i15 >= i14))
                  {
                    if (i8 != 0)
                    {
                      if (i6 == 3)
                      {
                        localVector_Float.addElement((int)arrayOfFloat2[i16]);
                        localVector_Float.addElement((int)f1 + f2);
                        localVector_Float.addElement(localPoint.y);
                        localVector_Float.addElement(localPoint.x);
                        localVector_Float.addElement(0.0F);
                      }
                      else
                      {
                        localVector_Float.addElement((int)arrayOfFloat2[i16]);
                        localVector_Float.addElement(localPoint.x);
                        localVector_Float.addElement(localPoint.y);
                        localVector_Float.addElement((int)f1 + f2);
                        localVector_Float.addElement(0.0F);
                      }
                    }
                    else
                    {
                      localVector_Float.addElement(localPoint.x);
                      localVector_Float.addElement(localPoint.y);
                      localVector_Float.addElement(f1 + f2);
                      localVector_Float.addElement(arrayOfFloat2[i16]);
                      localVector_Float.addElement(0.0F);
                    }
                    i18 = 1;
                  }
                  if ((i17 != 0) && (i18 == 0) && (str9.contains("\n")))
                  {
                    if (i8 != 0)
                    {
                      if (i6 == 3)
                      {
                        localVector_Float.addElement((int)arrayOfFloat2[i16]);
                        localVector_Float.addElement((int)f1 + f2);
                        localVector_Float.addElement(localPoint.y);
                        localVector_Float.addElement(localPoint.x);
                        localVector_Float.addElement(this.linkedSearchAreas);
                      }
                      else
                      {
                        localVector_Float.addElement((int)arrayOfFloat2[i16]);
                        localVector_Float.addElement(localPoint.x);
                        localVector_Float.addElement(localPoint.y);
                        localVector_Float.addElement((int)f1 + f2);
                        localVector_Float.addElement(this.linkedSearchAreas);
                      }
                    }
                    else
                    {
                      localVector_Float.addElement(localPoint.x);
                      localVector_Float.addElement(localPoint.y);
                      localVector_Float.addElement(f1 + f2);
                      localVector_Float.addElement(arrayOfFloat2[i16]);
                      localVector_Float.addElement(this.linkedSearchAreas);
                    }
                    i17 = 0;
                    i13 = i15;
                  }
                  if (str9.contains("\n"))
                  {
                    i16++;
                    while ((i16 < this.content.length) && ((this.content[i16] == null) || (i6 != this.writingMode[i16])))
                      i16++;
                  }
                }
                if (i != 0)
                {
                  k = 1;
                  break;
                }
              }
              if ((i != 0) && (k != 0))
                break;
            }
          }
        }
        localVector_Float.trim();
        if (this.includeTease)
        {
          localVector_String.trim();
          if (this.usingMultipleTerms)
            for (i10 = 0; i10 != localVector_String.size(); i10++)
              this.multipleTermTeasers.add(localVector_String.elementAt(i10));
          else
            this.teasers = localVector_String.get();
        }
      }
    }
    return localVector_Float.get();
  }

  private static String removeDuplicateSpaces(String paramString)
  {
    if (paramString.contains("  "))
      paramString = paramString.replace("  ", " ");
    return paramString;
  }

  public float[] getEndPoints()
  {
    return this.endPoints;
  }

  public String[] getTeasers()
  {
    return this.teasers;
  }

  public void generateTeasers()
  {
    this.includeTease = true;
  }

  static class ResultsComparator
    implements Comparator
  {
    private int rotation;

    public ResultsComparator(int paramInt)
    {
      this.rotation = paramInt;
    }

    public int compare(Object paramObject1, Object paramObject2)
    {
      Rectangle[] arrayOfRectangle1;
      if ((paramObject1 instanceof Rectangle[]))
        arrayOfRectangle1 = (Rectangle[])paramObject1;
      else
        arrayOfRectangle1 = new Rectangle[] { (Rectangle)paramObject1 };
      Rectangle[] arrayOfRectangle2;
      if ((paramObject2 instanceof Rectangle[]))
        arrayOfRectangle2 = (Rectangle[])paramObject2;
      else
        arrayOfRectangle2 = new Rectangle[] { (Rectangle)paramObject2 };
      for (int i = 0; i != arrayOfRectangle1.length; i++)
        for (int j = 0; j != arrayOfRectangle2.length; j++)
        {
          Rectangle localRectangle1 = arrayOfRectangle1[i];
          Rectangle localRectangle2 = arrayOfRectangle2[j];
          switch (this.rotation)
          {
          case 0:
            if (localRectangle1.y == localRectangle2.y)
            {
              if (localRectangle1.x > localRectangle2.x)
                return 1;
              return -1;
            }
            if (localRectangle1.y > localRectangle2.y)
              return -1;
            return 1;
          case 90:
            if (localRectangle1.x == localRectangle2.x)
            {
              if (localRectangle1.y > localRectangle2.y)
                return 1;
              return -1;
            }
            if (localRectangle1.x > localRectangle2.x)
              return 1;
            return -1;
          case 180:
            if (localRectangle1.y == localRectangle2.y)
            {
              if (localRectangle1.x > localRectangle2.x)
                return 1;
              return -1;
            }
            if (localRectangle1.y > localRectangle2.y)
              return -1;
            return 1;
          case 270:
            if (localRectangle1.x == localRectangle2.x)
            {
              if (localRectangle1.y > localRectangle2.y)
                return 1;
              return -1;
            }
            if (localRectangle1.x < localRectangle2.x)
              return 1;
            return -1;
          }
        }
      return -1;
    }
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.grouping.PdfGroupingAlgorithms
 * JD-Core Version:    0.6.2
 */