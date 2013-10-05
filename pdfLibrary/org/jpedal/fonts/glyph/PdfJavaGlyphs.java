package org.jpedal.fonts.glyph;

import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.jpedal.external.JPedalHelper;
import org.jpedal.fonts.FontMappings;
import org.jpedal.fonts.StandardFonts;
import org.jpedal.fonts.objects.FontData;
import org.jpedal.fonts.tt.Table;
import org.jpedal.parser.DecoderOptions;
import org.jpedal.render.DynamicVectorRenderer;
import org.jpedal.utils.StringUtils;

public class PdfJavaGlyphs
  implements PdfGlyphs, Serializable
{
  private Area[] cachedShapes = null;
  private AffineTransform[] cachedAt = null;
  public int[] CMAP_Translate;
  protected int glyphCount = 0;
  public boolean isFontInstalled = false;
  public String defaultFont = "Lucida Sans";
  public String fontName = "default";
  public String logicalfontName = "default";
  Map chars = new HashMap();
  Map displayValues = new HashMap();
  Map embeddedChars = new HashMap();
  private boolean isIdentity = false;
  private boolean isFontEmbedded = false;
  private boolean hasWidths = true;
  public String baseFontName = "";
  public boolean isSubsetted;
  public float[][] lastTrm = new float[3][3];
  private Font unscaledFont = null;
  public boolean isArialInstalledLocally;
  private int maxCharCount = 255;
  public boolean isCIDFont;
  public boolean remapFont = false;
  public String font_family_name;
  public int style;
  public static FontRenderContext frc = new FontRenderContext(null, true, true);
  public static String[] fontList;
  protected PdfGlyph[] cachedEmbeddedShapes = null;
  protected int localBias = 0;
  protected int globalBias = 0;

  public void flush()
  {
    this.cachedShapes = null;
    this.cachedAt = null;
  }

  public String getBaseFontName()
  {
    return this.baseFontName;
  }

  public void setBaseFontName(String paramString)
  {
    this.baseFontName = paramString;
  }

  public Area getStandardGlyph(float[][] paramArrayOfFloat, int paramInt, String paramString, float paramFloat, boolean paramBoolean)
  {
    Area localArea = getCachedShape(paramInt);
    if (localArea == null)
    {
      double d1 = -1.0D;
      double d2 = 1.0D;
      double d3 = 0.0D;
      if (((paramArrayOfFloat[1][0] < 0.0F) && (paramArrayOfFloat[0][1] >= 0.0F)) || ((paramArrayOfFloat[0][1] < 0.0F) && (paramArrayOfFloat[1][0] >= 0.0F)))
      {
        d2 = 1.0D;
        d1 = -1.0D;
      }
      if (paramBoolean)
      {
        localArea = getGlyph(paramInt, paramString, paramFloat);
        if ((localArea != null) && (paramInt == 146) && (this.isArialInstalledLocally))
          d3 = -(localArea.getBounds().height - localArea.getBounds().y);
      }
      else
      {
        String str = paramString;
        if ((this.remapFont) && (!getUnscaledFont().canDisplay(str.charAt(0))))
          str = String.valueOf((char)(paramInt + 61440));
        GlyphVector localGlyphVector = null;
        if ((!this.isCIDFont) || (this.isFontInstalled))
          localGlyphVector = getUnscaledFont().createGlyphVector(frc, str);
        if (localGlyphVector != null)
        {
          localArea = new Area(localGlyphVector.getOutline());
          double d4 = localGlyphVector.getOutline().getBounds2D().getX();
          if (d4 < 0.0D)
          {
            d4 = -d4;
            localAffineTransform = AffineTransform.getTranslateInstance(d4 * 2.0D, 0.0D);
            localArea.transform(localAffineTransform);
          }
          double d5 = localGlyphVector.getVisualBounds().getWidth() + d4 * 2.0D;
          double d6 = paramFloat / d5;
          if (d6 < 1.0D)
            d2 *= d6;
        }
      }
      AffineTransform localAffineTransform = new AffineTransform(d2 * paramArrayOfFloat[0][0], d2 * paramArrayOfFloat[0][1], d1 * paramArrayOfFloat[1][0], d1 * paramArrayOfFloat[1][1], 0.0D, d3);
      if (localArea != null)
        localArea.transform(localAffineTransform);
      setCachedShape(paramInt, localArea, localAffineTransform);
    }
    return localArea;
  }

  public Area getGlyph(int paramInt, String paramString, float paramFloat)
  {
    int i = 1;
    GlyphVector localGlyphVector = null;
    String str = paramString;
    if ((this.remapFont) && (!getUnscaledFont().canDisplay(str.charAt(0))))
      str = String.valueOf((char)(paramInt + 61440));
    if (!getUnscaledFont().canDisplay(str.charAt(0)))
    {
      str = paramString;
      i = 0;
    }
    if ((this.isCIDFont) && (this.isFontEmbedded) && (i != 0))
    {
      localGlyphVector = null;
    }
    else if (i != 0)
    {
      localGlyphVector = getUnscaledFont().createGlyphVector(frc, str);
    }
    else
    {
      localObject = new Font(this.defaultFont, 0, 1);
      if (!((Font)localObject).canDisplay(str.charAt(0)))
        localObject = new Font("lucida", 0, 1);
      if (((Font)localObject).canDisplay(str.charAt(0)))
        localGlyphVector = ((Font)localObject).createGlyphVector(frc, str);
    }
    Object localObject = null;
    if (localGlyphVector != null)
    {
      localObject = new Area(localGlyphVector.getOutline());
      double d1 = localGlyphVector.getOutline().getBounds2D().getX();
      double d2 = localGlyphVector.getOutline().getBounds2D().getWidth();
      AffineTransform localAffineTransform;
      if (!this.hasWidths)
      {
        float f = (float)(paramFloat - (d2 + d1 + d1)) / 2.0F;
        if (f > 0.0F)
        {
          localAffineTransform = AffineTransform.getTranslateInstance(f, 0.0D);
          ((Area)localObject).transform(localAffineTransform);
        }
      }
      else
      {
        if (d1 < 0.0D)
        {
          d1 = -d1;
          localAffineTransform = AffineTransform.getTranslateInstance(d1, 0.0D);
          ((Area)localObject).transform(localAffineTransform);
        }
        double d3 = paramFloat / ((Area)localObject).getBounds2D().getWidth();
        if (d3 < 1.0D)
        {
          localAffineTransform = AffineTransform.getScaleInstance(d3, 1.0D);
          ((Area)localObject).transform(localAffineTransform);
        }
      }
    }
    return localObject;
  }

  public final void setCachedShape(int paramInt, Area paramArea, AffineTransform paramAffineTransform)
  {
    Area[] arrayOfArea = this.cachedShapes;
    AffineTransform[] arrayOfAffineTransform = this.cachedAt;
    if (arrayOfArea == null)
    {
      this.cachedShapes = (arrayOfArea = new Area[this.maxCharCount]);
      this.cachedAt = (arrayOfAffineTransform = new AffineTransform[this.maxCharCount]);
    }
    if (paramArea == null)
      arrayOfArea[paramInt] = null;
    else
      arrayOfArea[paramInt] = paramArea;
    if ((paramArea != null) && (paramAffineTransform != null))
      arrayOfAffineTransform[paramInt] = paramAffineTransform;
  }

  public final AffineTransform getCachedTransform(int paramInt)
  {
    AffineTransform[] arrayOfAffineTransform = this.cachedAt;
    if (arrayOfAffineTransform == null)
      return null;
    return arrayOfAffineTransform[paramInt];
  }

  public final Area getCachedShape(int paramInt)
  {
    Area[] arrayOfArea = this.cachedShapes;
    if (arrayOfArea == null)
      return null;
    Area localArea = arrayOfArea[paramInt];
    if (localArea == null)
      return null;
    return localArea;
  }

  public void init(int paramInt, boolean paramBoolean)
  {
    this.maxCharCount = paramInt;
    this.isCIDFont = paramBoolean;
  }

  public final Font setFont(String paramString, int paramInt)
  {
    if (DecoderOptions.Helper != null)
    {
      localObject = DecoderOptions.Helper.setFont(this, StringUtils.convertHexChars(paramString), paramInt);
      if (localObject != null)
      {
        this.style = ((Font)localObject).getStyle();
        this.font_family_name = ((Font)localObject).getFamily();
        this.unscaledFont = ((Font)localObject);
        return localObject;
      }
    }
    paramString = StandardFonts.expandName(paramString);
    this.font_family_name = paramString;
    this.style = 0;
    Object localObject = null;
    String str1 = null;
    if (this.font_family_name == null)
      this.font_family_name = this.fontName;
    String str2 = this.font_family_name;
    if (this.font_family_name != null)
      str2 = this.font_family_name.toLowerCase();
    int i = this.font_family_name.indexOf(44);
    if (i == -1)
      i = this.font_family_name.indexOf(45);
    if (i != -1)
    {
      str1 = (String)FontMappings.fontSubstitutionAliasTable.get(str2);
      localObject = str2.substring(i + 1, str2.length());
      this.style = getWeight((String)localObject);
      this.font_family_name = this.font_family_name.substring(0, i).toLowerCase();
      str2 = this.font_family_name;
      if (str2.endsWith("mt"))
        str2 = str2.substring(0, str2.length() - 2);
    }
    if (str1 == null)
      str1 = (String)FontMappings.fontSubstitutionAliasTable.get(str2);
    if ((str1 != null) && (str1.equals("arialbd")))
      str1 = "arial-bold";
    if (str1 != null)
    {
      this.font_family_name = str1;
      i = this.font_family_name.indexOf(45);
      if (i != -1)
      {
        this.font_family_name = this.font_family_name.toLowerCase();
        localObject = this.font_family_name.substring(i + 1, this.font_family_name.length());
        this.style = getWeight((String)localObject);
        this.font_family_name = this.font_family_name.substring(0, i);
      }
      str2 = this.font_family_name.toLowerCase();
      if (str2.endsWith("mt"))
        str2 = str2.substring(0, str2.length() - 2);
    }
    if (fontList != null)
    {
      int j = 0;
      int k = fontList.length;
      for (int m = 0; m < k; m++)
        if ((fontList[m].equals(str2)) || ((localObject == null) && (str2.startsWith(fontList[m]))))
        {
          this.isFontInstalled = true;
          this.font_family_name = fontList[m];
          m = k;
          j = 1;
        }
      if (j == 0)
      {
        k = fontList.length;
        for (m = 0; m < k; m++)
          if ((fontList[m].equals(str2)) || ((localObject == null) && (str2.startsWith(fontList[m]))))
          {
            this.isFontInstalled = true;
            this.font_family_name = fontList[m];
            m = k;
          }
      }
      if ((this.isFontInstalled) && (this.font_family_name.equals("arial")))
        this.isArialInstalledLocally = true;
    }
    if (!this.isFontInstalled)
    {
      if (localObject == null)
      {
        String str3 = this.font_family_name.toLowerCase();
        this.style = getWeight(str3);
      }
      this.font_family_name = this.defaultFont;
    }
    this.unscaledFont = new Font(this.font_family_name, this.style, paramInt);
    return this.unscaledFont;
  }

  private static int getWeight(String paramString)
  {
    int i = 0;
    if (paramString.endsWith("mt"))
      paramString = paramString.substring(0, paramString.length() - 2);
    if (paramString.contains("heavy"))
      i = 1;
    else if (paramString.contains("bold"))
      i = 1;
    else if (paramString.contains("roman"))
      i = 0;
    if (paramString.contains("italic"))
      i += 2;
    else if (paramString.contains("oblique"))
      i += 2;
    return i;
  }

  public final Font getUnscaledFont()
  {
    if (this.unscaledFont == null)
      this.unscaledFont = new Font(this.defaultFont, 0, 1);
    return this.unscaledFont;
  }

  public final void setEmbeddedCachedShape(int paramInt, PdfGlyph paramPdfGlyph)
  {
    PdfGlyph[] arrayOfPdfGlyph = this.cachedEmbeddedShapes;
    if (arrayOfPdfGlyph == null)
      this.cachedEmbeddedShapes = (arrayOfPdfGlyph = new PdfGlyph[this.maxCharCount]);
    if (paramInt < arrayOfPdfGlyph.length)
      arrayOfPdfGlyph[paramInt] = paramPdfGlyph;
  }

  public final PdfGlyph getEmbeddedCachedShape(int paramInt)
  {
    PdfGlyph[] arrayOfPdfGlyph = this.cachedEmbeddedShapes;
    if (arrayOfPdfGlyph == null)
      return null;
    if (paramInt < arrayOfPdfGlyph.length)
    {
      PdfGlyph localPdfGlyph = arrayOfPdfGlyph[paramInt];
      if (localPdfGlyph == null)
        return null;
      return localPdfGlyph;
    }
    return null;
  }

  public PdfGlyph getEmbeddedGlyph(GlyphFactory paramGlyphFactory, String paramString1, float[][] paramArrayOfFloat, int paramInt, String paramString2, float paramFloat, String paramString3)
  {
    return null;
  }

  public void setGIDtoCID(int[] paramArrayOfInt)
  {
  }

  public void setEncodingToUse(boolean paramBoolean1, int paramInt, boolean paramBoolean2, boolean paramBoolean3)
  {
  }

  public int readEmbeddedFont(boolean paramBoolean, byte[] paramArrayOfByte, FontData paramFontData)
  {
    return 0;
  }

  public void setIsSubsetted(boolean paramBoolean)
  {
    this.isSubsetted = paramBoolean;
  }

  public void setT3Glyph(int paramInt1, int paramInt2, PdfGlyph paramPdfGlyph)
  {
  }

  public void setCharString(String paramString, byte[] paramArrayOfByte, int paramInt)
  {
  }

  public int getNumber(FontData paramFontData, int paramInt1, double[] paramArrayOfDouble, int paramInt2, boolean paramBoolean)
  {
    return 0;
  }

  public int getNumber(byte[] paramArrayOfByte, int paramInt1, double[] paramArrayOfDouble, int paramInt2, boolean paramBoolean)
  {
    return 0;
  }

  public boolean is1C()
  {
    return false;
  }

  public void setis1C(boolean paramBoolean)
  {
  }

  public void setValuesForGlyph(int paramInt, String paramString1, String paramString2, String paramString3)
  {
    Integer localInteger = Integer.valueOf(paramInt);
    this.chars.put(localInteger, paramString1);
    this.displayValues.put(localInteger, paramString2);
    this.embeddedChars.put(localInteger, paramString3);
  }

  public String getDisplayValue(Integer paramInteger)
  {
    return (String)this.displayValues.get(paramInteger);
  }

  public String getCharGlyph(Integer paramInteger)
  {
    return (String)this.chars.get(paramInteger);
  }

  public String getEmbeddedEnc(Integer paramInteger)
  {
    return (String)this.embeddedChars.get(paramInteger);
  }

  public Map getDisplayValues()
  {
    return this.displayValues;
  }

  public Map getCharGlyphs()
  {
    return this.chars;
  }

  public Map getEmbeddedEncs()
  {
    return this.embeddedChars;
  }

  public void setDisplayValues(Map paramMap)
  {
    this.displayValues = paramMap;
  }

  public void setCharGlyphs(Map paramMap)
  {
    this.chars = paramMap;
  }

  public void setEmbeddedEncs(Map paramMap)
  {
    this.embeddedChars = paramMap;
  }

  public void setLocalBias(int paramInt)
  {
    this.localBias = paramInt;
  }

  public void setGlobalBias(int paramInt)
  {
    this.globalBias = paramInt;
  }

  public float getTTWidth(String paramString1, int paramInt, String paramString2, boolean paramBoolean)
  {
    return 0.0F;
  }

  public static String getPostName(int paramInt)
  {
    return "notdef";
  }

  public int getConvertedGlyph(int paramInt)
  {
    return -1;
  }

  public void setIsIdentity(boolean paramBoolean)
  {
    this.isIdentity = paramBoolean;
  }

  public boolean isIdentity()
  {
    return this.isIdentity;
  }

  public float[] getFontBoundingBox()
  {
    return new float[] { 0.0F, 0.0F, 1000.0F, 1000.0F };
  }

  public void setFontEmbedded(boolean paramBoolean)
  {
    this.isFontEmbedded = paramBoolean;
  }

  public int getType()
  {
    return 0;
  }

  public void setHasWidths(boolean paramBoolean)
  {
    this.hasWidths = paramBoolean;
  }

  public void setDiffValues(String[] paramArrayOfString)
  {
  }

  public int getCMAPValue(int paramInt)
  {
    if (this.CMAP_Translate == null)
      return -1;
    return this.CMAP_Translate[paramInt];
  }

  public boolean isCorrupted()
  {
    return false;
  }

  public void setCorrupted(boolean paramBoolean)
  {
  }

  public void setIndexForCharString(int paramInt, String paramString)
  {
  }

  public String getIndexForCharString(int paramInt)
  {
    return null;
  }

  public Map getCharStrings()
  {
    return null;
  }

  public void setGlyphCount(int paramInt)
  {
    this.glyphCount = paramInt;
  }

  public int getGlyphCount()
  {
    return this.glyphCount;
  }

  public void setRenderer(DynamicVectorRenderer paramDynamicVectorRenderer)
  {
  }

  public Table getTable(int paramInt)
  {
    throw new UnsupportedOperationException("Not yet implemented");
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.fonts.glyph.PdfJavaGlyphs
 * JD-Core Version:    0.6.2
 */