package org.jpedal.fonts;

import java.util.Map;
import org.jpedal.exception.PdfException;
import org.jpedal.fonts.glyph.PdfJavaGlyphs;
import org.jpedal.fonts.glyph.T3Glyph;
import org.jpedal.fonts.glyph.T3Glyphs;
import org.jpedal.fonts.glyph.T3Size;
import org.jpedal.io.ObjectStore;
import org.jpedal.io.PdfObjectReader;
import org.jpedal.objects.GraphicsState;
import org.jpedal.objects.raw.PdfKeyPairsIterator;
import org.jpedal.objects.raw.PdfObject;
import org.jpedal.parser.T3StreamDecoder;
import org.jpedal.render.T3Display;
import org.jpedal.render.T3Renderer;
import org.jpedal.utils.LogWriter;

public class Type3 extends PdfFont
{
  private GraphicsState currentGraphicsState = new GraphicsState(false);
  private boolean isPrinting;

  public Type3(PdfObjectReader paramPdfObjectReader, boolean paramBoolean)
  {
    this.glyphs = new T3Glyphs();
    this.isPrinting = paramBoolean;
    init(paramPdfObjectReader);
  }

  public final void createFont(PdfObject paramPdfObject, String paramString, boolean paramBoolean, ObjectStore paramObjectStore, Map paramMap)
    throws Exception
  {
    this.fontTypes = 1228944679;
    init(paramString, paramBoolean);
    PdfObject localPdfObject = paramPdfObject.getDictionary(-1044665361);
    setBoundsAndMatrix(paramPdfObject);
    setName(paramPdfObject);
    setEncoding(paramPdfObject, localPdfObject);
    readWidths(paramPdfObject, false);
    readEmbeddedFont(paramPdfObject, paramObjectStore);
    if (paramBoolean)
      setFont(getBaseFontName(), 1);
  }

  private void readEmbeddedFont(PdfObject paramPdfObject, ObjectStore paramObjectStore)
  {
    PdfObject localPdfObject1 = paramPdfObject.getDictionary(2054190454);
    if (localPdfObject1 != null)
    {
      T3StreamDecoder localT3StreamDecoder = new T3StreamDecoder(this.currentPdfFile, true, this.isPrinting);
      localT3StreamDecoder.setParameters(false, true, 7, 0, false);
      localT3StreamDecoder.setObjectValue(-8, paramObjectStore);
      PdfObject localPdfObject2 = paramPdfObject.getDictionary(2004251818);
      if (localPdfObject2 != null)
        try
        {
          localT3StreamDecoder.readResources(localPdfObject2, false);
        }
        catch (PdfException localPdfException)
        {
          if (LogWriter.isOutput())
            LogWriter.writeLog("Exception: " + localPdfException.getMessage());
        }
      PdfKeyPairsIterator localPdfKeyPairsIterator = localPdfObject1.getKeyPairsIterator();
      while (localPdfKeyPairsIterator.hasMorePairs())
      {
        String str1 = localPdfKeyPairsIterator.getNextKeyAsString();
        PdfObject localPdfObject3 = localPdfKeyPairsIterator.getNextValueAsDictionary();
        Object localObject = null;
        if (this.diffLookup != null)
        {
          String str2 = StandardFonts.convertNumberToGlyph(str1, this.containsHexNumbers, this.allNumbers);
          localObject = this.diffLookup.get(str2);
        }
        if ((localPdfObject3 != null) && (this.renderPage))
        {
          T3Display localT3Display = new T3Display(0, false, 20, paramObjectStore);
          localT3Display.setHiResImageForDisplayMode(true);
          localT3Display.setType3Glyph(str1);
          try
          {
            localT3StreamDecoder.setObjectValue(23, localT3Display);
            localT3StreamDecoder.setDefaultColors(this.currentGraphicsState.getNonstrokeColor(), this.currentGraphicsState.getNonstrokeColor());
            int n = 1;
            double[] arrayOfDouble = paramPdfObject.getDoubleArray(-2105119560);
            if ((arrayOfDouble != null) && (arrayOfDouble[0] == 1.0D) && ((arrayOfDouble[3] == 1.0D) || (arrayOfDouble[3] == -1.0D)))
              n = 10;
            GraphicsState localGraphicsState = new GraphicsState(false, 0, 0);
            localGraphicsState.CTM = new float[][] { { n, 0.0F, 0.0F }, { 0.0F, n, 0.0F }, { 0.0F, 0.0F, 1.0F } };
            T3Size localT3Size = localT3StreamDecoder.decodePageContent(localPdfObject3, localGraphicsState);
            int k = localT3Size.x;
            int m = localT3Size.y;
            if ((k == 0) && (m != 0))
            {
              k = localT3Size.y;
              m = localT3Size.x;
            }
            T3Glyph localT3Glyph = new T3Glyph(localT3Display, k, m, localT3StreamDecoder.ignoreColors, str1);
            localT3Glyph.setScaling(1.0F / n);
            int j = -1;
            int i;
            if (localObject != null)
            {
              i = ((Integer)localObject).intValue();
              if (localPdfKeyPairsIterator.isNextKeyANumber())
                j = localPdfKeyPairsIterator.getNextKeyAsNumber();
            }
            else
            {
              i = localPdfKeyPairsIterator.getNextKeyAsNumber();
            }
            this.glyphs.setT3Glyph(i, j, localT3Glyph);
          }
          catch (Exception localException)
          {
            localException.printStackTrace(System.out);
            if (LogWriter.isOutput())
              LogWriter.writeLog("Exception " + localException + " is Type3 font code");
          }
        }
        localPdfKeyPairsIterator.nextPair();
      }
      this.isFontEmbedded = true;
    }
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.fonts.Type3
 * JD-Core Version:    0.6.2
 */