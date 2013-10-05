package org.jpedal.fonts;

import java.awt.Rectangle;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import org.jpedal.exception.PdfFontException;
import org.jpedal.external.ExternalHandlers;
import org.jpedal.fonts.glyph.PdfJavaGlyphs;
import org.jpedal.fonts.objects.FontData;
import org.jpedal.fonts.tt.TTGlyphs;
import org.jpedal.io.ObjectStore;
import org.jpedal.io.PdfObjectReader;
import org.jpedal.objects.raw.PdfObject;
import org.jpedal.utils.LogWriter;

public class TrueType extends PdfFont
{
  private boolean subfontAlreadyLoaded;
  private Map fontsLoaded;
  private Rectangle BBox = null;

  public TrueType()
  {
    this.fontsLoaded = new HashMap();
    init(null);
  }

  private void readFontData(byte[] paramArrayOfByte, FontData paramFontData)
  {
    if (this.subfontAlreadyLoaded)
    {
      this.glyphs = ((PdfJavaGlyphs)this.fontsLoaded.get(this.substituteFont + '_' + this.glyphs.getBaseFontName() + ' ' + paramArrayOfByte.length));
      this.fontTypes = this.glyphs.getType();
    }
    else
    {
      if ((!this.isCIDFont) && (paramArrayOfByte != null))
        this.fontsLoaded.put(this.substituteFont + '_' + this.glyphs.getBaseFontName() + ' ' + paramArrayOfByte.length, this.glyphs);
      this.fontTypes = this.glyphs.readEmbeddedFont(this.TTstreamisCID, paramArrayOfByte, paramFontData);
    }
  }

  protected void substituteFontUsed(String paramString)
    throws PdfFontException
  {
    Object localObject = null;
    try
    {
      localObject = this.loader.getResourceAsStream("org/jpedal/res/fonts/" + paramString);
      if (localObject == null)
        localObject = new FileInputStream(paramString);
    }
    catch (Exception localException1)
    {
      System.err.println("Exception " + localException1 + " reading " + paramString + " Check cid  jar installed");
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception " + localException1 + " reading " + paramString + " Check cid  jar installed");
      if ((ExternalHandlers.throwMissingCIDError) && (localException1.getMessage().contains("kochi")))
        throw new Error(localException1);
    }
    if (localObject == null)
      throw new PdfFontException("Unable to load font " + paramString);
    try
    {
      ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
      byte[] arrayOfByte = new byte[65535];
      int i;
      while ((i = ((InputStream)localObject).read(arrayOfByte)) != -1)
        localByteArrayOutputStream.write(arrayOfByte, 0, i);
      localByteArrayOutputStream.close();
      ((InputStream)localObject).close();
      FontData localFontData = null;
      readFontData(localByteArrayOutputStream.toByteArray(), localFontData);
      this.glyphs.setEncodingToUse(this.hasEncoding, getFontEncoding(false), true, this.isCIDFont);
      this.isFontEmbedded = true;
    }
    catch (Exception localException2)
    {
      System.err.println("Exception " + localException2 + " reading " + paramString + " Check cid  jar installed");
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception " + localException2 + " reading " + paramString + " Check cid  jar installed");
      if ((ExternalHandlers.throwMissingCIDError) && (localException2.getMessage().contains("kochi")))
        throw new Error(localException2);
    }
  }

  public TrueType(PdfObjectReader paramPdfObjectReader, String paramString)
  {
    this.glyphs = new TTGlyphs();
    init(paramPdfObjectReader);
    this.substituteFont = paramString;
  }

  public void createFont(PdfObject paramPdfObject, String paramString, boolean paramBoolean, ObjectStore paramObjectStore, Map paramMap)
    throws Exception
  {
    this.fontTypes = 1217103210;
    this.fontsLoaded = paramMap;
    init(paramString, paramBoolean);
    PdfObject localPdfObject = paramPdfObject.getDictionary(-1044665361);
    setBoundsAndMatrix(localPdfObject);
    setName(paramPdfObject);
    setEncoding(paramPdfObject, localPdfObject);
    if (paramBoolean)
    {
      Object localObject1;
      Object localObject2;
      if ((localPdfObject != null) && (this.substituteFont == null))
      {
        localObject1 = null;
        localObject2 = localPdfObject.getDictionary(2021292334);
        if (localObject2 == null)
        {
          localObject2 = localPdfObject.getDictionary(746093177);
          if (localObject2 == null)
            localObject2 = localPdfObject.getDictionary(2021292335);
        }
        if (localObject2 != null)
          localObject1 = this.currentPdfFile.readStream((PdfObject)localObject2, true, true, false, false, false, ((PdfObject)localObject2).getCacheName(this.currentPdfFile.getObjectReader()));
        if (localObject1 != null)
          readEmbeddedFont((byte[])localObject1, null, this.hasEncoding, false);
      }
      if ((!this.isFontEmbedded) && (this.substituteFont != null))
      {
        if (this.glyphs.remapFont)
          this.glyphs.remapFont = false;
        this.subfontAlreadyLoaded = ((!this.isCIDFont) && (this.fontsLoaded.containsKey(this.substituteFont + '_' + this.glyphs.getBaseFontName())));
        localObject2 = null;
        int i = 0;
        if (!this.subfontAlreadyLoaded)
        {
          localObject1 = new File(this.substituteFont);
          i = (int)((File)localObject1).length();
        }
        if ((FontData.maxSizeAllowedInMemory >= 0) && (i > FontData.maxSizeAllowedInMemory))
        {
          if (!this.subfontAlreadyLoaded)
            localObject2 = new FontData(this.substituteFont);
          readEmbeddedFont(null, (FontData)localObject2, false, true);
        }
        else if (this.subfontAlreadyLoaded)
        {
          readEmbeddedFont(null, null, false, true);
        }
        else
        {
          InputStream localInputStream = null;
          try
          {
            if ((this.substituteFont.startsWith("jar:")) || (this.substituteFont.startsWith("http:")))
              localInputStream = this.loader.getResourceAsStream(this.substituteFont);
            else
              localInputStream = this.loader.getResourceAsStream("file:///" + this.substituteFont);
          }
          catch (Exception localException)
          {
            if (LogWriter.isOutput())
              LogWriter.writeLog("1.Unable to open " + this.substituteFont);
          }
          catch (Error localError)
          {
            if (LogWriter.isOutput())
              LogWriter.writeLog("1.Unable to open " + this.substituteFont);
          }
          BufferedInputStream localBufferedInputStream;
          if (localInputStream == null)
            localBufferedInputStream = new BufferedInputStream(new FileInputStream(this.substituteFont));
          else
            localBufferedInputStream = new BufferedInputStream(localInputStream);
          ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
          byte[] arrayOfByte = new byte[65535];
          int j;
          while ((j = localBufferedInputStream.read(arrayOfByte)) != -1)
            localByteArrayOutputStream.write(arrayOfByte, 0, j);
          localByteArrayOutputStream.close();
          localBufferedInputStream.close();
          readEmbeddedFont(localByteArrayOutputStream.toByteArray(), null, false, true);
        }
        this.isFontSubstituted = true;
      }
    }
    readWidths(paramPdfObject, true);
    if (paramBoolean)
      setFont(this.glyphs.fontName, 1);
    this.glyphs.setDiffValues(this.diffTable);
  }

  protected final void readEmbeddedFont(byte[] paramArrayOfByte, FontData paramFontData, boolean paramBoolean1, boolean paramBoolean2)
  {
    try
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Embedded TrueType font used");
      readFontData(paramArrayOfByte, paramFontData);
      this.isFontEmbedded = true;
      this.glyphs.setFontEmbedded(true);
      this.glyphs.setEncodingToUse(paramBoolean1, getFontEncoding(false), paramBoolean2, this.TTstreamisCID);
    }
    catch (Exception localException)
    {
      this.isFontEmbedded = false;
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception " + localException + " processing TrueType font");
    }
  }

  public Rectangle getBoundingBox()
  {
    if (this.BBox == null)
      if ((this.isFontEmbedded) && (!this.isFontSubstituted))
        this.BBox = new Rectangle((int)this.FontBBox[0], (int)this.FontBBox[1], (int)(this.FontBBox[2] - this.FontBBox[0]), (int)(this.FontBBox[3] - this.FontBBox[1]));
      else
        this.BBox = super.getBoundingBox();
    return this.BBox;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.fonts.TrueType
 * JD-Core Version:    0.6.2
 */