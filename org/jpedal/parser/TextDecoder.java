package org.jpedal.parser;

import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import org.jpedal.color.GenericColorSpace;
import org.jpedal.external.GlyphTracker;
import org.jpedal.fonts.PdfFont;
import org.jpedal.fonts.StandardFonts;
import org.jpedal.fonts.glyph.GlyphFactory;
import org.jpedal.fonts.glyph.PdfGlyph;
import org.jpedal.fonts.glyph.PdfJavaGlyphs;
import org.jpedal.fonts.glyph.T1GlyphFactory;
import org.jpedal.fonts.tt.TTGlyph;
import org.jpedal.io.ErrorTracker;
import org.jpedal.io.ObjectDecoder;
import org.jpedal.io.PdfObjectReader;
import org.jpedal.objects.GraphicsState;
import org.jpedal.objects.PdfData;
import org.jpedal.objects.TextState;
import org.jpedal.objects.raw.MCObject;
import org.jpedal.objects.raw.PdfObject;
import org.jpedal.objects.structuredtext.StructuredContentHandler;
import org.jpedal.render.DynamicVectorRenderer;
import org.jpedal.utils.Fonts;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.Matrix;
import org.jpedal.utils.NumberUtils;
import org.jpedal.utils.repositories.Vector_Int;
import org.jpedal.utils.repositories.Vector_Rectangle;

public class TextDecoder extends BaseDecoder
  implements Decoder
{
  public static boolean showInvisibleText = false;
  private boolean isXFA = false;
  private Map lines = new HashMap(1000);
  private PdfFont currentFontData;
  private int fontSize = 0;
  private int currentRotation = 0;
  private PdfData pdfData;
  boolean markedContentExtracted = false;
  private Vector_Rectangle textAreas = new Vector_Rectangle();
  private Vector_Int textDirections = new Vector_Int();
  private TextState currentTextState = new TextState();
  private boolean isPrinting = false;
  private static final double radiansToDegrees = 57.295779513082323D;
  private float unRotatedY = -1.0F;
  private boolean textExtracted = true;
  private boolean renderText = false;
  private boolean textColorExtracted = false;
  private boolean highlightCoords = true;
  private boolean multipleTJs = false;
  private int moveCommand = 0;
  private boolean ttHintingRequired = false;
  private String tjTextValue = "";
  private double rotationAsRadians = 0.0D;
  private int textLength = 0;
  private static final String[] hex = { "&#0;", "&#1;", "&#2;", "&#3;", "&#4;", "&#5;", "&#6;", "&#7;", "&#8;", "&#9;", "&#10;", "&#11;", "&#12;", "&#13;", "&#14;", "&#15;", "&#16;", "&#17;", "&#18;", "&#19;", "&#20;", "&#21;", "&#22;", "&#23;", "&#24;", "&#25;", "&#26;", "&#27;", "&#28;", "&#29;", "&#30;", "&#31;" };
  private static final float THOUSAND = 1000.0F;
  private float charSpacing = 0.0F;
  private GlyphFactory factory;
  private boolean returnText = false;
  private static final int[] multiply8 = { 0, 3, 6, 9, 12, 15 };
  private static final int[] multiply16 = { 0, 4, 8, 12, 16, 20, 24, 28, 32, 36, 40 };
  private static final int NONE = 0;
  private static final int RIGHT = 1;
  float x1;
  float y1;
  float x2;
  float y2;
  boolean isXMLExtraction = true;
  LayerDecoder layerDecoder;
  private String actualText;

  public TextDecoder(PdfData paramPdfData, boolean paramBoolean1, LayerDecoder paramLayerDecoder, boolean paramBoolean2)
  {
    this.pdfData = paramPdfData;
    this.isXMLExtraction = paramBoolean1;
    this.layerDecoder = paramLayerDecoder;
    this.factory = new T1GlyphFactory(paramBoolean2);
  }

  public TextDecoder(LayerDecoder paramLayerDecoder, boolean paramBoolean)
  {
    this.layerDecoder = paramLayerDecoder;
    this.markedContentExtracted = true;
    this.factory = new T1GlyphFactory(paramBoolean);
  }

  private void calcCoordinates(float paramFloat1, float[][] paramArrayOfFloat, boolean paramBoolean, float paramFloat2, int paramInt, float paramFloat3)
  {
    float[][] arrayOfFloat = new float[3][3];
    for (int i = 0; i < 3; i++)
      System.arraycopy(paramArrayOfFloat[i], 0, arrayOfFloat[i], 0, 3);
    this.x1 = paramFloat1;
    this.x2 = (arrayOfFloat[2][0] - this.charSpacing * arrayOfFloat[0][0]);
    if (paramBoolean)
    {
      if (arrayOfFloat[1][0] < 0.0F)
      {
        this.x1 = (paramFloat1 + arrayOfFloat[1][0] - this.charSpacing * arrayOfFloat[0][0]);
        this.x2 = arrayOfFloat[2][0];
      }
      else if (arrayOfFloat[1][0] > 0.0F)
      {
        this.x1 = paramFloat1;
        this.x2 = arrayOfFloat[2][0];
      }
    }
    else if (arrayOfFloat[1][0] > 0.0F)
    {
      this.x1 = arrayOfFloat[2][0];
      this.x2 = (paramFloat1 + arrayOfFloat[1][0] - this.charSpacing * arrayOfFloat[0][0]);
    }
    else if (arrayOfFloat[1][0] < 0.0F)
    {
      this.x2 = arrayOfFloat[2][0];
      this.x1 = (paramFloat1 + arrayOfFloat[1][0] - this.charSpacing * arrayOfFloat[0][0]);
    }
    if (!this.highlightCoords)
      if (paramBoolean)
      {
        float f = 1.0F;
        if (this.currentFontData.getFontType() == 1228944679)
          f = paramFloat2 / paramInt;
        if (arrayOfFloat[0][1] != 0.0F)
        {
          this.y1 = (arrayOfFloat[2][1] - arrayOfFloat[0][1] + (arrayOfFloat[0][1] + arrayOfFloat[1][1]) * f);
          this.y2 = paramFloat3;
        }
        else
        {
          this.y1 = (paramFloat3 + arrayOfFloat[1][1] * f);
          this.y2 = arrayOfFloat[2][1];
        }
      }
      else if (arrayOfFloat[0][1] <= 0.0F)
      {
        this.y2 = arrayOfFloat[2][1];
        this.y1 = paramFloat3;
      }
      else if (arrayOfFloat[0][1] > 0.0F)
      {
        this.y1 = arrayOfFloat[2][1];
        this.y2 = paramFloat3;
      }
  }

  public void setFont(PdfFont paramPdfFont)
  {
    this.currentFontData = paramPdfFont;
  }

  public void setTextState(TextState paramTextState)
  {
    this.currentTextState = paramTextState;
  }

  public int processToken(TextState paramTextState, int paramInt1, int paramInt2, int paramInt3)
  {
    this.currentTextState = paramTextState;
    switch (paramInt1)
    {
    case 4342851:
      PdfObject localPdfObject = BDC(paramInt2, paramInt3, this.parser.getStream(), this.parser.generateOpAsString(0, false), this.layerDecoder, this.gs, this.currentPdfFile, this.current, this.markedContentExtracted);
      this.actualText = localPdfObject.getTextStreamValue(1752861363);
      if (this.contentHandler != null)
        this.contentHandler.BDC(localPdfObject);
      break;
    case 4345155:
      this.layerDecoder.BMC();
      if (this.contentHandler != null)
        this.contentHandler.BMC(this.parser.generateOpAsString(0, false));
      break;
    case 16980:
      paramTextState.resetTm();
      break;
    case 4541763:
      this.actualText = null;
      if (this.contentHandler != null)
        this.contentHandler.EMC();
      this.layerDecoder.EMC(this.current, this.gs);
      break;
    case 17748:
      this.current.resetOnColorspaceChange();
      break;
    case 17488:
      if (this.contentHandler != null)
      {
        MCObject localMCObject = new MCObject(this.parser.generateOpAsString(0, false));
        this.currentPdfFile.readObject(localMCObject);
        this.contentHandler.DP(localMCObject);
      }
      break;
    case 19792:
      if (this.contentHandler != null)
        this.contentHandler.MP();
      break;
    case 21606:
      paramTextState.TF(this.parser.parseFloat(0), this.parser.generateOpAsString(1, true));
      break;
    case 21603:
      paramTextState.setCharacterSpacing(this.parser.parseFloat(0));
      break;
    case 21572:
      TD(false, this.parser.parseFloat(1), this.parser.parseFloat(0), paramTextState);
      break;
    case 21604:
      TD(true, this.parser.parseFloat(1), this.parser.parseFloat(0), paramTextState);
      break;
    case 21610:
      TJ(this.parser.getStream(), paramInt2, paramInt3);
      break;
    case 21578:
      TJ(this.parser.getStream(), paramInt2, paramInt3);
      break;
    case 39:
      TSTAR();
      TJ(this.parser.getStream(), paramInt2, paramInt3);
      break;
    case 34:
      double_quote(this.parser.getStream(), paramInt2, paramInt3, this.parser.parseFloat(1), this.parser.parseFloat(2));
      break;
    case 21613:
      paramTextState.Tm[0][0] = this.parser.parseFloat(5);
      paramTextState.Tm[0][1] = this.parser.parseFloat(4);
      paramTextState.Tm[0][2] = 0.0F;
      paramTextState.Tm[1][0] = this.parser.parseFloat(3);
      paramTextState.Tm[1][1] = this.parser.parseFloat(2);
      paramTextState.Tm[1][2] = 0.0F;
      paramTextState.Tm[2][0] = this.parser.parseFloat(1);
      paramTextState.Tm[2][1] = this.parser.parseFloat(0);
      paramTextState.Tm[2][2] = 1.0F;
      paramTextState.TmNoRotation[0][0] = paramTextState.Tm[0][0];
      paramTextState.TmNoRotation[0][1] = paramTextState.Tm[0][1];
      paramTextState.TmNoRotation[0][2] = 0.0F;
      paramTextState.TmNoRotation[1][0] = paramTextState.Tm[1][0];
      paramTextState.TmNoRotation[1][1] = paramTextState.Tm[1][1];
      paramTextState.TmNoRotation[1][2] = 0.0F;
      paramTextState.TmNoRotation[2][0] = paramTextState.Tm[2][0];
      paramTextState.TmNoRotation[2][1] = paramTextState.Tm[2][1];
      paramTextState.TmNoRotation[2][2] = 1.0F;
      TM();
      break;
    case 21546:
      TSTAR();
      break;
    case 21618:
      TR(this.parser.parseInt(0), this.gs);
      break;
    case 21619:
      paramTextState.setTextRise(this.parser.parseFloat(0));
      break;
    case 21623:
      paramTextState.setWordSpacing(this.parser.parseFloat(0));
      break;
    case 21626:
      paramTextState.setHorizontalScaling(this.parser.parseFloat(0) / 100.0F);
      break;
    case 21580:
      paramTextState.setLeading(this.parser.parseFloat(0));
    }
    return paramInt3;
  }

  private static PdfObject BDC(int paramInt1, int paramInt2, byte[] paramArrayOfByte, String paramString, LayerDecoder paramLayerDecoder, GraphicsState paramGraphicsState, PdfObjectReader paramPdfObjectReader, DynamicVectorRenderer paramDynamicVectorRenderer, boolean paramBoolean)
  {
    MCObject localMCObject = new MCObject(paramString);
    localMCObject.setID(1184787);
    int i = paramInt1;
    if (paramInt1 < 1)
      paramInt1 = 1;
    boolean bool = true;
    while ((paramInt1 < paramArrayOfByte.length) && (paramArrayOfByte[paramInt1] != 60) && (paramArrayOfByte[(paramInt1 - 1)] != 60))
    {
      paramInt1++;
      if ((paramArrayOfByte[paramInt1] == 66) && (paramArrayOfByte[(paramInt1 + 1)] == 68) && (paramArrayOfByte[(paramInt1 + 2)] == 67))
        bool = false;
    }
    if ((bool) && ((paramBoolean) || ((paramLayerDecoder.getPdfLayerList() != null) && (paramLayerDecoder.isLayerVisible()))))
    {
      ObjectDecoder localObjectDecoder = new ObjectDecoder(paramPdfObjectReader.getObjectReader());
      localObjectDecoder.setEndPt(paramInt2);
      localObjectDecoder.readDictionaryAsObject(localMCObject, paramInt1 + 1, paramArrayOfByte);
    }
    paramLayerDecoder.BDC(localMCObject, paramGraphicsState, paramDynamicVectorRenderer, paramInt2, paramArrayOfByte, bool, i);
    return localMCObject;
  }

  private void double_quote(byte[] paramArrayOfByte, int paramInt1, int paramInt2, float paramFloat1, float paramFloat2)
  {
    this.currentTextState.setCharacterSpacing(paramFloat1);
    this.currentTextState.setWordSpacing(paramFloat2);
    TSTAR();
    while ((paramArrayOfByte[paramInt1] != 40) && (paramArrayOfByte[paramInt1] != 60) && (paramArrayOfByte[paramInt1] != 91))
      paramInt1++;
    TJ(paramArrayOfByte, paramInt1, paramInt2);
  }

  private void TD(boolean paramBoolean, float paramFloat1, float paramFloat2, TextState paramTextState)
  {
    relativeMove(paramFloat1, paramFloat2, paramTextState);
    if (!paramBoolean)
    {
      float f = -paramFloat2;
      paramTextState.setLeading(f);
    }
    reset();
  }

  private void TM()
  {
    int i = 0;
    if (i != 0)
    {
      float[][] arrayOfFloat = this.currentTextState.Tm;
      if ((arrayOfFloat[1][0] == 0.0F) && (arrayOfFloat[0][1] == 0.0F))
      {
        this.currentRotation = 0;
        this.unRotatedY = -1.0F;
      }
      else if ((arrayOfFloat[0][1] == 0.0F) || (arrayOfFloat[1][0] == 0.0F))
      {
        this.currentRotation = 0;
        this.unRotatedY = -1.0F;
      }
      else
      {
        this.rotationAsRadians = (-Math.asin(arrayOfFloat[1][0] / arrayOfFloat[0][0]));
        int j = (int)(this.rotationAsRadians * 57.295779513082323D);
        if (j == 0)
        {
          this.currentRotation = 0;
          this.unRotatedY = -1.0F;
        }
        else
        {
          this.currentRotation = j;
          convertToUnrotated(arrayOfFloat);
        }
      }
    }
    this.currentTextState.setTMAtLineStart();
    this.currentTextState.setTMAtLineStartNoRotation();
    reset();
    this.moveCommand = 1;
  }

  public void TJ(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    if ((this.renderText) && (this.gs.getTextRenderType() != 4))
    {
      this.gs.setStrokeColor(this.gs.strokeColorSpace.getColor());
      this.gs.setNonstrokeColor(this.gs.nonstrokeColorSpace.getColor());
    }
    StringBuffer localStringBuffer = processTextArray(paramArrayOfByte, paramInt1, paramInt2, this.multiplyer);
    int i = this.fontSize;
    if (i == 0)
      i = (int)this.currentTextState.getTfs();
    if (i < 0)
      i = -i;
    if ((localStringBuffer != null) && (this.isPageContent))
    {
      String str = null;
      if (this.textColorExtracted)
        if ((this.gs.getTextRenderType() & 0x2) == 2)
          str = this.gs.nonstrokeColorSpace.getXMLColorToken();
        else
          str = this.gs.strokeColorSpace.getXMLColorToken();
      if (this.contentHandler != null)
        this.contentHandler.setText(localStringBuffer, this.x1, this.y1, this.x2, this.y2);
      else
        storeExtractedText(str, localStringBuffer, i, this.currentFontData.getFontName());
    }
    this.moveCommand = -1;
  }

  private void TR(int paramInt, GraphicsState paramGraphicsState)
  {
    if (paramInt == 0)
    {
      paramInt = 2;
    }
    else if (paramInt == 1)
    {
      paramInt = 1;
    }
    else if (paramInt == 2)
    {
      paramInt = 3;
    }
    else if (paramInt == 3)
    {
      paramInt = 4;
      if (showInvisibleText)
        paramInt = 2;
    }
    else if (paramInt == 7)
    {
      paramInt = 7;
    }
    paramGraphicsState.setTextRenderType(paramInt);
    if ((this.renderPage) && (!this.renderDirectly))
      this.current.drawTR(paramInt);
  }

  private void TSTAR()
  {
    relativeMove(0.0F, -this.currentTextState.getLeading(), this.currentTextState);
    this.moveCommand = 0;
    reset();
  }

  private void convertToUnrotated(float[][] paramArrayOfFloat)
  {
    if ((paramArrayOfFloat[0][1] == 0.0F) || (paramArrayOfFloat[1][0] == 0.0F))
      return;
    this.rotationAsRadians = (-Math.asin(paramArrayOfFloat[1][0] / paramArrayOfFloat[0][0]));
    float[][] arrayOfFloat1 = new float[3][3];
    arrayOfFloat1[0][0] = ((float)Math.cos(-this.rotationAsRadians));
    arrayOfFloat1[0][1] = ((float)Math.sin(-this.rotationAsRadians));
    arrayOfFloat1[0][2] = 0.0F;
    arrayOfFloat1[1][0] = ((float)-Math.sin(-this.rotationAsRadians));
    arrayOfFloat1[1][1] = ((float)Math.cos(-this.rotationAsRadians));
    arrayOfFloat1[1][2] = 0.0F;
    arrayOfFloat1[2][0] = 0.0F;
    arrayOfFloat1[2][1] = 0.0F;
    arrayOfFloat1[2][2] = 1.0F;
    for (int i = 0; i < 3; i++)
      for (int j = 0; j < 3; j++)
        if (((arrayOfFloat1[j][i] > 0.99D ? 1 : 0) & (arrayOfFloat1[j][i] < 1.0F ? 1 : 0)) != 0)
          arrayOfFloat1[j][i] = 1.0F;
    float[][] arrayOfFloat2 = new float[3][3];
    arrayOfFloat2[0][0] = paramArrayOfFloat[2][0];
    arrayOfFloat2[1][1] = paramArrayOfFloat[2][1];
    arrayOfFloat2[2][2] = 1.0F;
    arrayOfFloat2 = Matrix.multiply(arrayOfFloat1, arrayOfFloat2);
    float[][] arrayOfFloat3 = Matrix.multiply(arrayOfFloat1, paramArrayOfFloat);
    float f1 = arrayOfFloat2[1][0];
    float f2 = arrayOfFloat2[1][1] - f1;
    float f3 = this.currentTextState.Tm[2][1];
    Integer localInteger = Integer.valueOf((int)(f2 + 0.5D));
    Float localFloat = (Float)this.lines.get(localInteger);
    if (localFloat == null)
      localFloat = (Float)this.lines.get(Integer.valueOf((int)(f2 + 1.0F)));
    if (localFloat == null)
      this.lines.put(localInteger, Float.valueOf(this.currentTextState.Tm[2][1]));
    else
      f3 = localFloat.floatValue();
    arrayOfFloat3[2][1] = f3;
    this.currentTextState.TmNoRotation = arrayOfFloat3;
    if (this.unRotatedY == -1.0F)
      this.unRotatedY = this.currentTextState.TmNoRotation[2][1];
    this.currentTextState.TmNoRotation[0][1] = 0.0F;
    this.currentTextState.TmNoRotation[1][0] = 0.0F;
  }

  private void relativeMove(float paramFloat1, float paramFloat2, TextState paramTextState)
  {
    float[][] arrayOfFloat1 = new float[3][3];
    paramTextState.Tm = paramTextState.getTMAtLineStart();
    arrayOfFloat1[0][0] = 1.0F;
    arrayOfFloat1[0][1] = 0.0F;
    arrayOfFloat1[0][2] = 0.0F;
    arrayOfFloat1[1][0] = 0.0F;
    arrayOfFloat1[1][1] = 1.0F;
    arrayOfFloat1[1][2] = 0.0F;
    arrayOfFloat1[2][0] = paramFloat1;
    arrayOfFloat1[2][1] = paramFloat2;
    arrayOfFloat1[2][2] = 1.0F;
    paramTextState.Tm = Matrix.multiply(arrayOfFloat1, paramTextState.Tm);
    paramTextState.setTMAtLineStart();
    if (this.currentRotation != 0)
    {
      float[][] arrayOfFloat2 = new float[3][3];
      paramTextState.TmNoRotation = paramTextState.getTMAtLineStartNoRotation();
      arrayOfFloat2[0][0] = 1.0F;
      arrayOfFloat2[0][1] = 0.0F;
      arrayOfFloat2[0][2] = 0.0F;
      arrayOfFloat2[1][0] = 0.0F;
      arrayOfFloat2[1][1] = 1.0F;
      arrayOfFloat2[1][2] = 0.0F;
      arrayOfFloat2[2][0] = paramFloat1;
      arrayOfFloat2[2][1] = paramFloat2;
      arrayOfFloat2[2][2] = 1.0F;
      paramTextState.TmNoRotation = Matrix.multiply(arrayOfFloat2, paramTextState.TmNoRotation);
      float f1 = paramFloat1;
      float f2 = paramFloat2;
      if (f1 < 0.0F)
        f1 = -paramFloat1;
      if (f2 < 0.0F)
        f2 = -paramFloat2;
      if ((f1 > paramTextState.Tm[0][0]) && (f2 > paramTextState.Tm[1][1]))
        convertToUnrotated(paramTextState.Tm);
      paramTextState.setTMAtLineStartNoRotation();
    }
    this.moveCommand = 2;
  }

  private StringBuffer processTextArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2, float paramFloat)
  {
    boolean bool1 = false;
    int i = 0;
    boolean bool2 = true;
    int j = this.currentFontData.getDiffMapping(9) != null ? 1 : 0;
    int k = this.currentFontData.getDiffMapping(10) != null ? 1 : 0;
    int m = this.currentFontData.getDiffMapping(13) != null ? 1 : 0;
    int n = 0;
    int i1 = paramArrayOfByte.length;
    while ((paramArrayOfByte[paramInt1] == 91) || (paramArrayOfByte[paramInt1] == 10) || (paramArrayOfByte[paramInt1] == 13) || (paramArrayOfByte[paramInt1] == 32))
    {
      if (paramArrayOfByte[paramInt1] == 91)
        i = 1;
      paramInt1++;
    }
    this.textLength = 0;
    int i2 = this.gs.getTextRenderType();
    int i4 = 0;
    Object localObject1 = new float[3][3];
    float[][] arrayOfFloat = new float[3][3];
    Object localObject3 = new float[3][3];
    Object localObject4 = new float[3][3];
    int i5 = 32;
    int i7 = 32;
    int i8 = 32;
    int i9 = 120;
    float f2 = 0.0F;
    float f4 = 0.0F;
    float f5 = 0.0F;
    float f6 = 0.0F;
    float f7 = 0.0F;
    Object localObject5 = "";
    float f8 = this.currentTextState.getTfs();
    int i11 = -1;
    float f9 = f8;
    if (f8 < 0.0F)
      f8 = -f8;
    int i12 = this.currentFontData.getFontType();
    float f10 = this.currentFontData.getCurrentFontSpaceWidth();
    Object localObject6 = "";
    StringBuffer localStringBuffer1 = null;
    if (this.textExtracted)
      localStringBuffer1 = new StringBuffer(50);
    boolean bool4 = this.currentFontData.isCIDFont();
    int i13 = this.currentFontData.isDoubleBytes();
    boolean bool5 = this.currentFontData.isFontVertical();
    boolean bool6 = false;
    int i14 = 2;
    if ((bool4) && (!this.currentFontData.isSingleByte()))
      i14 = 4;
    Object localObject2 = Matrix.multiply(this.currentTextState.Tm, this.gs.CTM);
    if (this.currentRotation != 0)
      localObject1 = Matrix.multiply(this.currentTextState.TmNoRotation, this.gs.CTM);
    this.charSpacing = (this.currentTextState.getCharacterSpacing() / f8);
    float f12 = this.currentTextState.getWordSpacing() / f8;
    if (this.multipleTJs)
    {
      localObject2[2][0] = this.currentTextState.Tm[2][0];
      localObject2[2][1] = this.currentTextState.Tm[2][1];
      if (this.currentRotation != 0)
      {
        localObject1[2][0] = this.currentTextState.TmNoRotation[2][0];
        localObject1[2][1] = this.currentTextState.TmNoRotation[2][1];
      }
    }
    arrayOfFloat[0][0] = (f9 * this.currentTextState.getHorizontalScaling());
    arrayOfFloat[1][1] = f9;
    arrayOfFloat[2][1] = this.currentTextState.getTextRise();
    arrayOfFloat[2][2] = 1.0F;
    localObject2 = Matrix.multiply(arrayOfFloat, (float[][])localObject2);
    if (this.currentRotation != 0)
      localObject1 = Matrix.multiply(arrayOfFloat, (float[][])localObject1);
    if ((i != 0) && (paramArrayOfByte[paramInt1] != 60) && (paramArrayOfByte[paramInt1] != 40) && (paramArrayOfByte[paramInt1] != 93))
    {
      float f13 = 0.0F;
      while ((paramArrayOfByte[paramInt1] != 40) && (paramArrayOfByte[paramInt1] != 60) && (paramArrayOfByte[paramInt1] != 93))
      {
        StringBuilder localStringBuilder = new StringBuilder(10);
        while ((paramArrayOfByte[paramInt1] != 60) && (paramArrayOfByte[paramInt1] != 40) && (paramArrayOfByte[paramInt1] != 93) && (paramArrayOfByte[paramInt1] != 32))
        {
          localStringBuilder.append((char)paramArrayOfByte[paramInt1]);
          paramInt1++;
        }
        f13 += Float.parseFloat(localStringBuilder.toString());
        while (paramArrayOfByte[paramInt1] == 32)
          paramInt1++;
      }
      if ((localObject2[0][0] == 0.0F) && (localObject2[1][1] == 0.0F) && (localObject2[0][1] > 0.0F) && (localObject2[1][0] < 0.0F))
      {
        f13 = localObject2[0][1] * f13 / 1000.0F;
        localObject2[2][1] -= f13;
        if (this.currentRotation != 0)
          localObject1[2][1] -= f13;
      }
      else
      {
        f13 = localObject2[0][0] * f13 / 1000.0F;
        localObject2[2][0] -= f13;
        if (this.currentRotation != 0)
          localObject1[2][0] -= f13;
      }
    }
    this.multipleTJs = true;
    boolean bool3;
    int i3;
    float f3;
    if (localObject2[1][1] != 0.0F)
    {
      bool3 = true;
      i3 = 0;
      if (localObject2[1][1] < 0.0F)
        this.fontSize = ((int)(localObject2[1][1] - 0.5F));
      else
        this.fontSize = ((int)(localObject2[1][1] + 0.5F));
      if (this.fontSize == 0)
        if (localObject2[0][1] < 0.0F)
          this.fontSize = ((int)(localObject2[0][1] - 0.5F));
        else
          this.fontSize = ((int)(localObject2[0][1] + 0.5F));
      f3 = localObject2[0][0];
      if ((localObject2[0][0] == 0.0F) && (localObject2[0][1] > 0.0F) && (localObject2[1][0] < 0.0F) && (localObject2[1][1] > 0.0F))
        i3 = 3;
    }
    else
    {
      bool3 = false;
      if (localObject2[1][0] < 0.0F)
        this.fontSize = ((int)(localObject2[1][0] - 0.5F));
      else
        this.fontSize = ((int)(localObject2[1][0] + 0.5F));
      if (this.fontSize == 0)
        if (localObject2[0][0] < 0.0F)
          this.fontSize = ((int)(localObject2[0][0] - 0.5F));
        else
          this.fontSize = ((int)(localObject2[0][0] + 0.5F));
      if (this.fontSize < 0)
      {
        this.fontSize = (-this.fontSize);
        i3 = 3;
      }
      else
      {
        i3 = 2;
      }
      f3 = localObject2[0][1];
    }
    this.currentTextState.writingMode = i3;
    if (this.fontSize == 0)
      this.fontSize = 1;
    Font localFont = null;
    if ((this.isPrinting) && (this.textPrint == 3) && (StandardFonts.isStandardFont(this.currentFontData.getFontName(), true)))
      localFont = this.currentFontData.getJavaFontX(this.fontSize);
    else if ((this.currentFontData.isFontEmbedded) && (!this.currentFontData.isFontSubstituted()))
      localFont = null;
    else if (((PdfStreamDecoder.useTextPrintingForNonEmbeddedFonts) || (this.textPrint != 0)) && (this.isPrinting))
      localFont = this.currentFontData.getJavaFontX(this.fontSize);
    float f14;
    float f15;
    if (this.currentRotation == 0)
    {
      f14 = localObject2[2][0];
      f15 = localObject2[2][1];
    }
    else
    {
      f14 = localObject1[2][0];
      f15 = localObject1[2][1];
    }
    if ((localObject2[1][0] < 0.0F) && (localObject2[0][1] > 0.0F) && (localObject2[1][1] == 0.0F) && (localObject2[0][0] == 0.0F))
      bool6 = true;
    float f16 = this.fontSize;
    if ((i12 == 1228944679) && (this.fontSize > 10))
      f16 = 10.0F;
    if (bool4)
      f16 = localObject2[1][1];
    int i15 = paramInt1;
    int i16 = 0;
    StringBuffer localStringBuffer2 = null;
    if (this.returnText)
      localStringBuffer2 = new StringBuffer(i1);
    boolean bool7 = true;
    while (i15 < paramInt2)
    {
      float f1 = -1.0F;
      int i10;
      while (true)
      {
        if ((i7 == 92) && (i5 == 92))
          i7 = 120;
        else
          i7 = i5;
        i10 = paramArrayOfByte[i15];
        if (i10 < 0)
          i10 = 256 + i10;
        i5 = (char)i10;
        if ((i5 == 92) && ((paramArrayOfByte[(i15 + 1)] == 13) || (paramArrayOfByte[(i15 + 1)] == 10)))
        {
          i15++;
          i10 = paramArrayOfByte[i15];
          if (i10 < 0)
            i10 = 256 + i10;
          i5 = (char)i10;
        }
        if ((i5 != 10) && (i5 != 13))
          break;
        i15++;
      }
      if (i4 != 0)
        if ((i7 != 92) && ((i5 == 40) || (i5 == 41)))
        {
          if (i5 == 40)
            i16++;
          else if (i5 == 41)
            if (i16 <= 0)
              i4 = 0;
            else
              i16--;
        }
        else if ((i8 == 60) && (i5 == 62))
          i4 = 0;
      int i21;
      if (i4 != 0)
      {
        i9 = i5;
        int i17;
        int i20;
        int i27;
        int i28;
        Object localObject7;
        int i31;
        int i18;
        int i23;
        if (i8 == 60)
        {
          i17 = 0;
          i20 = 0;
          for (int i25 = 1; i25 < i14; i25++)
          {
            int i22 = paramArrayOfByte[(i15 + i25)];
            if (i22 == 62)
            {
              i25 = 4;
              i14 = 2;
            }
            else if ((i22 == 10) || (i22 == 13))
            {
              i15++;
              i25--;
            }
            else
            {
              i20++;
            }
          }
          i27 = 0;
          for (i28 = 0; i28 < i20 + 1; i28++)
          {
            i25 = paramArrayOfByte[(i15 + i20 - i28)];
            if ((i25 >= 65) && (i25 <= 70))
            {
              i25 -= 55;
            }
            else if ((i25 >= 97) && (i25 <= 102))
            {
              i25 -= 87;
            }
            else
            {
              if ((i25 < 48) || (i25 > 57))
                continue;
              i25 -= 48;
            }
            i17 += (i25 << multiply16[i27]);
            i27++;
          }
          i10 = i17;
          i15 = i15 + i14 - 1;
          i5 = (char)i10;
          localObject5 = this.currentFontData.getGlyphValue(i10);
          if ((bool4) && (this.currentFontData.getCMAP() != null) && (this.currentFontData.getUnicodeMapping(i10) == null))
          {
            i5 = ((String)localObject5).charAt(0);
            i10 = i5;
          }
          if (this.textExtracted)
            localObject6 = this.currentFontData.getUnicodeValue((String)localObject5, i10);
        }
        else if ((i5 == 92) && (!bool4))
        {
          i15++;
          i7 = i5;
          if ((i1 > i15 + 2) && (Character.isDigit((char)paramArrayOfByte[i15])))
          {
            i17 = 1;
            if (Character.isDigit((char)paramArrayOfByte[(i15 + 1)]))
            {
              i17++;
              if (Character.isDigit((char)paramArrayOfByte[(i15 + 2)]))
                i17++;
            }
            i10 = readEscapeValue(i15, i17, 8, paramArrayOfByte);
            i15 = i15 + i17 - 1;
            if (i10 > 255)
              i10 -= 256;
            i5 = (char)i10;
            localObject5 = this.currentFontData.getGlyphValue(i10);
            if (this.textExtracted)
              localObject6 = this.currentFontData.getUnicodeValue((String)localObject5, i10);
            if (i5 == 92)
              i5 = 120;
          }
          else
          {
            i10 = paramArrayOfByte[i15] & 0xFF;
            i5 = (char)i10;
            if (i5 == 117)
            {
              i10 = readEscapeValue(i15 + 1, 4, 16, paramArrayOfByte);
              i15 += 4;
              localObject5 = this.currentFontData.getGlyphValue(i10);
              if (this.textExtracted)
                localObject6 = this.currentFontData.getUnicodeValue((String)localObject5, i10);
            }
            else
            {
              if (i5 == 110)
              {
                i10 = 10;
                i5 = 10;
              }
              else if (i5 == 98)
              {
                i10 = 8;
                i5 = 8;
              }
              else if (i5 == 116)
              {
                i10 = 9;
                i5 = 9;
              }
              else if (i5 == 114)
              {
                i10 = 13;
                i5 = 13;
              }
              else if (i5 == 102)
              {
                i10 = 12;
                i5 = 12;
              }
              localObject5 = this.currentFontData.getGlyphValue(i10);
              if (this.textExtracted)
                localObject6 = this.currentFontData.getUnicodeValue((String)localObject5, i10);
              if (!((String)localObject5).isEmpty())
                i5 = ((String)localObject5).charAt(0);
            }
          }
          if ((this.currentFontData.getFontType() == 1228944677) && ((this.current.getType() == 4) || (this.current.getType() == 6) || (this.current.getType() == 5) || (this.current.getType() == 7)))
          {
            String str1 = this.currentFontData.getMappedChar(i10, true);
            if ((str1 != null) && (str1.length() == 1) && (str1.toLowerCase().equals(((String)localObject6).toLowerCase())))
            {
              localObject5 = str1;
              localObject6 = localObject5;
            }
          }
        }
        else if (bool4)
        {
          if (StandardFonts.CMAP == null)
            StandardFonts.readCMAP();
          localObject7 = null;
          if (paramArrayOfByte[i15] == 92)
          {
            i15++;
            i20 = paramArrayOfByte[i15] & 0xFF;
            if ((i1 > i15 + 2) && (Character.isDigit((char)paramArrayOfByte[i15])))
            {
              i27 = 1;
              if (Character.isDigit((char)paramArrayOfByte[(i15 + 1)]))
              {
                i27++;
                if (Character.isDigit((char)paramArrayOfByte[(i15 + 2)]))
                  i27++;
              }
              i20 = readEscapeValue(i15, i27, 8, paramArrayOfByte);
              i15 = i15 + i27 - 1;
              if (i20 > 255)
                i20 -= 256;
            }
            else if (i20 == 117)
            {
              i20 = readEscapeValue(i15 + 1, 4, 16, paramArrayOfByte);
              i15 += 4;
            }
            else if (i20 == 110)
            {
              i20 = 10;
            }
            else if (i20 == 98)
            {
              i20 = 8;
            }
            else if (i20 == 116)
            {
              i20 = 9;
            }
            else if (i20 == 114)
            {
              i20 = 13;
            }
            else if (i20 == 102)
            {
              i20 = 12;
            }
            i5 = (char)i20;
            i10 = i20;
          }
          else
          {
            i20 = i5;
          }
          String str3 = StandardFonts.CMAP[i5];
          boolean bool9 = this.currentFontData.isFontEmbedded;
          i28 = (i10 > 0) && (this.currentFontData.CMapName != null) && (this.currentFontData.getFontType() == -1684566726) && (this.currentFontData.getGlyphData().getCharStrings().containsKey(String.valueOf(i10))) ? 1 : 0;
          if ((i28 == 0) && ((this.currentFontData.hasDoubleBytes) || (str3 == null) || (i13 != 0) || (i10 > 128)))
          {
            int i30 = i15;
            i15++;
            i31 = paramArrayOfByte[i15] & 0xFF;
            boolean bool11 = false;
            if (paramArrayOfByte[i15] == 92)
            {
              i15++;
              bool11 = true;
              i31 = paramArrayOfByte[i15] & 0xFF;
              if ((i1 > i15 + 2) && (Character.isDigit((char)paramArrayOfByte[i15])))
              {
                i32 = 1;
                if (Character.isDigit((char)paramArrayOfByte[(i15 + 1)]))
                {
                  i32++;
                  if (Character.isDigit((char)paramArrayOfByte[(i15 + 2)]))
                    i32++;
                }
                i31 = readEscapeValue(i15, i32, 8, paramArrayOfByte);
                i15 = i15 + i32 - 1;
                if (i31 > 255)
                  i31 -= 256;
              }
              else if (i31 == 117)
              {
                i31 = readEscapeValue(i15 + 1, 4, 16, paramArrayOfByte);
                i15 += 4;
              }
              else if (i31 == 110)
              {
                i31 = 10;
              }
              else if (i31 == 98)
              {
                i31 = 8;
              }
              else if (i31 == 116)
              {
                i31 = 9;
              }
              else if (i31 == 114)
              {
                i31 = 13;
              }
              else if (i31 == 102)
              {
                i31 = 12;
              }
            }
            int i32 = i31;
            int i33 = (char)((i5 << 8) + i31);
            localObject7 = StandardFonts.CMAP[i33];
            i13 = this.currentFontData.isDoubleBytes(i20, i32, bool11);
            if ((bool9) && ((i13 == 1) || (i33 < 256) || (localObject7 != null)))
            {
              i10 = i33;
              i5 = (char)i10;
            }
            else if ((!bool9) && (i13 == 1) && ((localObject7 != null) || (i33 < 256) || ((!bool11) && (i32 != 41))))
            {
              i10 = i33;
              i5 = (char)i10;
            }
            else if ((i13 == 0) && (!bool9) && (i20 > 128) && (localObject7 != null) && (str3 == null))
            {
              i10 = i33;
              i5 = (char)i10;
            }
            else if ((i13 == 0) && (!bool9) && (i20 > 128) && (localObject7 == null) && (str3 != null))
            {
              i15 = i30;
              localObject7 = str3;
            }
            else
            {
              i15 = i30;
            }
            if (!bool9)
            {
              f1 = this.currentFontData.getDefaultWidth(i10);
              if (f1 == -1.0F)
                f1 = this.currentFontData.getDefaultWidth(-1);
            }
          }
          else
          {
            f1 = -1.0F;
            if ((!bool9) && ((this.currentFontData.getFontType() == -1684566726) || (this.currentFontData.getFontType() == -1684566724)))
            {
              f1 = this.currentFontData.getDefaultWidth(i10);
              if (f1 == -1.0F)
                f1 = this.currentFontData.getDefaultWidth(-1) / 2.0F;
            }
          }
          if (localObject7 != null)
            localObject5 = localObject7;
          else
            localObject5 = String.valueOf(i5);
          if (this.textExtracted)
            localObject6 = this.currentFontData.getUnicodeValue((String)localObject5, i5);
          if (i5 == 92)
          {
            i11 = 92;
            i5 = 120;
          }
        }
        else
        {
          localObject5 = this.currentFontData.getGlyphValue(i10);
          i18 = 0;
          String str2;
          if (((((String)localObject5).isEmpty()) || ((((String)localObject5).length() > 0) && (((String)localObject5).charAt(0) < ' '))) && ((this.current.getType() == 4) || (this.current.getType() == 6) || (this.current.getType() == 5) || (this.current.getType() == 7)) && (!this.currentFontData.isCIDFont()))
          {
            str2 = this.currentFontData.getMappedChar(i10, false);
            if (str2 != null)
            {
              i23 = this.currentFontData.getDiffChar(str2);
              if (i23 != -1)
              {
                i10 = i23;
                localObject5 = String.valueOf((char)i10);
              }
              else if ((((String)localObject5).length() > 0) && (((String)localObject5).charAt(0) < ' '))
              {
                i10 = StandardFonts.getAdobeMap(str2);
                localObject5 = String.valueOf((char)i10);
                localObject6 = localObject5;
                i18 = 1;
              }
            }
          }
          if ((i10 == 32) && (!((String)localObject5).equals(" ")))
            i9 = 90;
          if ((this.textExtracted) && (i18 == 0))
            localObject6 = this.currentFontData.getUnicodeValue((String)localObject5, i10);
          if ((this.currentFontData.getFontType() == 1228944677) && ((this.current.getType() == 4) || (this.current.getType() == 6) || (this.current.getType() == 5) || (this.current.getType() == 7)))
          {
            str2 = this.currentFontData.getMappedChar(i10, true);
            if ((str2 != null) && (str2.length() == 1) && (str2.toLowerCase().equals(((String)localObject6).toLowerCase())))
            {
              localObject5 = str2;
              localObject6 = localObject5;
            }
          }
        }
        if ((!this.currentFontData.hasToUnicode()) && (this.currentFontData.getFontType() == -1684566726) && (this.currentFontData.getGlyphData().isIdentity()) && ((this.current.getType() == 6) || (this.current.getType() == 4) || (this.current.getType() == 5) || (this.current.getType() == 7)))
        {
          i18 = i5;
          if (i11 != -1)
          {
            i18 = i11;
            i11 = -1;
          }
          i21 = StandardFonts.mapCIDToValidUnicode(this.currentFontData.getBaseFontName(), i18);
          localObject6 = String.valueOf((char)i21);
        }
        if ((i10 == 9) && (j == 0) && (this.currentFontData.isFontSubstituted()))
        {
          i10 = 32;
          localObject5 = " ";
          localObject6 = " ";
        }
        arrayOfFloat[0][0] = 1.0F;
        arrayOfFloat[0][1] = 0.0F;
        arrayOfFloat[0][2] = 0.0F;
        arrayOfFloat[1][0] = 0.0F;
        arrayOfFloat[1][1] = 1.0F;
        arrayOfFloat[1][2] = 0.0F;
        if (bool5)
        {
          arrayOfFloat[2][1] = (-(f5 + f6));
          arrayOfFloat[2][0] = 0.0F;
        }
        else
        {
          arrayOfFloat[2][0] = (f5 + f6);
          arrayOfFloat[2][1] = 0.0F;
        }
        arrayOfFloat[2][2] = 1.0F;
        localObject2 = Matrix.multiply(arrayOfFloat, (float[][])localObject2);
        if (this.currentRotation != 0)
          localObject1 = Matrix.multiply(arrayOfFloat, (float[][])localObject1);
        if ((i5 == 32) && (i7 != 32))
        {
          localObject3 = localObject2;
          if (this.currentRotation != 0)
            localObject4 = localObject1;
        }
        f6 = 0.0F;
        PdfJavaGlyphs localPdfJavaGlyphs = this.currentFontData.getGlyphData();
        if ((this.currentFontData.isCIDFont()) && (localPdfJavaGlyphs.is1C()) && (!localPdfJavaGlyphs.isIdentity()))
        {
          i21 = localPdfJavaGlyphs.getCMAPValue(i10);
          if (i21 > 0)
            i10 = i21;
        }
        i21 = i10;
        if (!localPdfJavaGlyphs.isCorrupted())
          if ((this.currentFontData.isCIDFont()) && (!localPdfJavaGlyphs.isIdentity()))
          {
            i23 = localPdfJavaGlyphs.getConvertedGlyph(i10);
            if (i23 != -1)
              i21 = i23;
          }
          else if (this.currentFontData.getFontType() != 1228944679)
          {
            i23 = this.currentFontData.getDiffChar(i10);
            if (i23 > 0)
              i10 = i23;
          }
        if (f1 > 0.0F)
          f5 = f1;
        else
          f5 = this.currentFontData.getWidth(i21);
        if ((this.current.getType() == 4) || (this.current.getType() == 6) || (this.current.getType() == 5) || (this.current.getType() == 7))
          this.currentFontData.setCurrentWidth(f5);
        if ((f12 < 0.0F) && (i10 == 32) && ((this.current.getType() == 4) || (this.current.getType() == 5) || (this.current.getType() == 7) || (this.current.getType() == 6)))
        {
          float f18 = Math.abs(f12 + f5);
          if (f18 < 0.01D)
            localObject6 = "";
        }
        if ((f5 == 0.0F) && (this.isXFA))
        {
          String str4 = "";
          if (i10 > 255)
            str4 = String.valueOf(i10);
          else
            str4 = StandardFonts.getUnicodeChar(2, i10);
          f5 = this.currentFontData.getGlyphWidth(str4, i10, (String)localObject5);
        }
        if (this.returnText)
          localStringBuffer2.append((String)localObject5);
        this.currentTextState.setLastKerningAdded(f7);
        f7 = 0.0F;
        if ((this.renderText) && (i2 != 4))
          if ((this.isPrinting) && (localFont != null) && ((this.textPrint == 3) || (this.textPrint == 2) || ((PdfStreamDecoder.useTextPrintingForNonEmbeddedFonts) && ((!this.currentFontData.isFontEmbedded) || (this.currentFontData.isFontSubstituted())))))
          {
            if (i2 == 7)
            {
              boolean bool8 = (DecoderOptions.isRunningOnMac) || (StandardFonts.isStandardFont(this.currentFontData.getBaseFontName(), false));
              localObject7 = localPdfJavaGlyphs.getStandardGlyph((float[][])localObject2, i10, (String)localObject5, f5, bool8);
              if (localObject7 != null)
                this.gs.addClip((Area)localObject7);
              this.current.drawClip(this.gs, null, true);
            }
            if ((localObject5 != null) && (!((String)localObject5).startsWith("&#")))
              if ((this.current.getType() == 6) || (this.current.getType() == 4) || (this.current.getType() == 5) || (this.current.getType() == 7))
                this.current.drawEmbeddedText((float[][])localObject2, this.fontSize, null, null, 1, this.gs, null, (String)localObject5, this.currentFontData, -100.0F);
              else
                this.current.drawText((float[][])localObject2, (String)localObject5, this.gs, localObject2[2][0], -localObject2[2][1], localFont);
          }
          else if (((this.textPrint == 1) && (localFont != null)) || (((!this.currentFontData.isFontEmbedded) || (!this.currentFontData.isFontSubstituted()) || (((i10 != 9) || (j != 0)) && ((i10 != 10) || (k != 0)) && ((i10 != 13) || (m != 0)))) && (((this.textPrint == 1) && (localFont != null)) || (!this.currentFontData.isFontSubstituted()) || (f5 != 0.0F) || (((String)localObject5).charAt(0) != '\r'))))
          {
            if (((this.textPrint != 1) || (localFont == null)) && (this.currentFontData.isFontEmbedded))
            {
              str5 = "notdef";
              try
              {
                if (!this.currentFontData.isCIDFont())
                  str5 = this.currentFontData.getMappedChar(i10, false);
                localObject7 = localPdfJavaGlyphs.getEmbeddedGlyph(this.factory, str5, (float[][])localObject2, i10, (String)localObject5, f5, this.currentFontData.getEmbeddedChar(i10));
                if ((localObject7 instanceof TTGlyph))
                  if (((PdfGlyph)localObject7).containsBrokenData())
                  {
                    if ((localObject5 != null) && (!((String)localObject5).startsWith("&#")))
                      if ((this.current.getType() == 4) || (this.current.getType() == 5) || (this.current.getType() == 7) || (this.current.getType() == 6))
                        this.current.drawEmbeddedText((float[][])localObject2, this.fontSize, null, null, 1, this.gs, null, (String)localObject5, this.currentFontData, -100.0F);
                      else
                        this.current.drawText((float[][])localObject2, (String)localObject5, this.gs, localObject2[2][0], -localObject2[2][1], this.currentFontData.getJavaFontX(this.fontSize));
                    localObject7 = null;
                  }
                  else
                  {
                    this.ttHintingRequired = ((this.ttHintingRequired) || (((TTGlyph)localObject7).isTTHintingRequired()));
                  }
                if (i12 == 1228944679)
                  if ((localObject7 != null) && (((PdfGlyph)localObject7).getmaxWidth() == 0.0F))
                    localObject7 = null;
                  else if ((localObject7 != null) && (((PdfGlyph)localObject7).ignoreColors()))
                    ((PdfGlyph)localObject7).setT3Colors(this.gs.getNonstrokeColor(), this.gs.getNonstrokeColor(), true);
                if (localObject7 != null)
                {
                  if ((localObject7 != null) && (i12 == 1228944677))
                    ((PdfGlyph)localObject7).setWidth(f5 * 1000.0F);
                  Object localObject8 = { { localObject2[0][0], localObject2[0][1], 0.0F }, { localObject2[1][0], localObject2[1][1], 0.0F }, { localObject2[2][0], localObject2[2][1], 1.0F } };
                  float[][] arrayOfFloat1 = { { (float)this.currentFontData.FontMatrix[0], (float)this.currentFontData.FontMatrix[1], 0.0F }, { (float)this.currentFontData.FontMatrix[2], (float)this.currentFontData.FontMatrix[3], 0.0F }, { 0.0F, 0.0F, 1.0F } };
                  localObject8 = Matrix.multiply((float[][])localObject8, arrayOfFloat1);
                  localObject8[2][0] = localObject2[2][0];
                  localObject8[2][1] = localObject2[2][1];
                  if ((localObject8[1][0] < 0.0F) && (localObject8[0][1] < 0.0F))
                  {
                    localObject8[1][0] = (-localObject8[1][0]);
                    localObject8[0][1] = (-localObject8[0][1]);
                  }
                  if (i12 == 1228944679)
                  {
                    float f19 = 0.0F;
                    if (localObject8[1][1] != 0.0F)
                      f19 = this.fontSize * localObject8[1][1];
                    else if (localObject8[0][0] != 0.0F)
                      f19 = this.fontSize * localObject8[0][0];
                    else if (localObject8[1][0] != 0.0F)
                      f19 = this.fontSize * localObject8[1][0];
                    if (f19 < 0.0F)
                      f19 = -f19;
                    if (f19 > f16)
                      f16 = f19;
                  }
                  AffineTransform localAffineTransform = new AffineTransform(localObject8[0][0], localObject8[0][1], localObject8[1][0], localObject8[1][1], localObject8[2][0], localObject8[2][1]);
                  i31 = 5;
                  if (i12 == 6)
                  {
                    i31 = 5;
                    float f20 = 1000.0F / ((PdfGlyph)localObject7).getmaxWidth();
                    localAffineTransform.scale(f5 * f20, 1.0D);
                  }
                  else if ((i12 == 1217103210) || (i12 == -1684566724) || ((this.currentFontData.isFontSubstituted()) && (i12 != 1228944677)))
                  {
                    i31 = 4;
                  }
                  else if (i12 == 1228944679)
                  {
                    i31 = 6;
                  }
                  if (this.generateGlyphOnRender)
                    i31 = -i31;
                  if ((i2 == 7) && (((PdfGlyph)localObject7).getShape() != null))
                  {
                    Area localArea = (Area)((PdfGlyph)localObject7).getShape().clone();
                    if ((TTGlyph.useHinting) && ((localObject7 instanceof TTGlyph)))
                      localArea.transform(AffineTransform.getScaleInstance(0.01D, 0.01D));
                    localArea.transform(localAffineTransform);
                    if ((localArea.getBounds().getWidth() > 0.0D) && (localArea.getBounds().getHeight() > 0.0D))
                    {
                      this.gs.addClip(localArea);
                      this.current.drawClip(this.gs, null, false);
                    }
                  }
                  float f21 = this.gs.getLineWidth();
                  float f22 = 0.0F;
                  if (paramFloat > 0.0F)
                    f22 = 1.0F / paramFloat;
                  this.gs.setLineWidth(f22);
                  if (bool6)
                    this.current.drawEmbeddedText((float[][])localObject2, -this.fontSize, (PdfGlyph)localObject7, null, i31, this.gs, localAffineTransform, (String)localObject6, this.currentFontData, -100.0F);
                  else
                    this.current.drawEmbeddedText((float[][])localObject2, this.fontSize, (PdfGlyph)localObject7, null, i31, this.gs, localAffineTransform, (String)localObject6, this.currentFontData, -100.0F);
                  this.gs.setLineWidth(f21);
                }
                else
                {
                  localObject5 = " ";
                  localObject6 = " ";
                }
              }
              catch (Exception localException)
              {
                if (LogWriter.isOutput())
                  LogWriter.writeLog(new StringBuilder().append("Exception: ").append(localException.getMessage()).toString());
                this.errorTracker.addPageFailureMessage(new StringBuilder().append("Exception ").append(localException).append(" on embedded font renderer").toString());
              }
            }
            else if ((!((String)localObject5).isEmpty()) && (!((String)localObject5).startsWith("&#")))
            {
              bool2 = renderTextWithJavaFonts(f1, bool2, i2, this.fontSize, i10, f5, (String)localObject5, (String)localObject6, bool6, localPdfJavaGlyphs, (float[][])localObject2);
            }
          }
        f5 += this.charSpacing;
        if (i5 == 32)
          f5 += f12;
        float f11 = f2 + this.charSpacing - f4;
        String str5 = "";
        if ((f11 > 0.0F) && (f4 > 0.0F))
          str5 = PdfFont.getSpaces(f11, f10, PdfStreamDecoder.currentThreshold);
        this.textLength += 1;
        f2 += f5;
        f4 = f2;
        if (this.customGlyphTracker != null)
          this.customGlyphTracker.addGlyph((float[][])localObject2, i10, (String)localObject5, (String)localObject6);
        if (this.textExtracted)
          bool1 = writeOutText(bool1, bool3, (float[][])localObject1, (float[][])localObject2, f3, f5, (String)localObject6, localStringBuffer1, str5, this.isXMLExtraction, this.currentRotation);
      }
      else if ((i5 == 40) || (i5 == 60))
      {
        i4 = 1;
        i8 = i5;
      }
      else if ((i5 == 41) || ((i5 == 62) && (i8 == 60)) || ((i4 == 0) && ((i5 == 45) || ((i5 >= 48) && (i5 <= 57)))))
      {
        float f17 = 0.0F;
        i15++;
        while ((paramArrayOfByte[i15] == 32) || (paramArrayOfByte[i15] == 13) || (paramArrayOfByte[i15] == 10))
          i15++;
        int i6 = (char)paramArrayOfByte[i15];
        if ((i6 == 40) || (i6 == 60))
        {
          i15--;
        }
        else if ((i6 != 39) && (i6 != 34) && (i6 != 40) && (i6 != 93) && (i6 != 60))
        {
          i21 = 0;
          int i24 = i15;
          int i26 = 0;
          boolean bool10 = false;
          int i29 = 0;
          while (i26 == 0)
          {
            i5 = i6;
            if ((i5 != 10) && (i5 != 13))
              i21++;
            i6 = (char)paramArrayOfByte[(i15 + 1)];
            if (i6 == 32)
              bool10 = true;
            if (i6 == 93)
              i29 = 1;
            if ((i6 == 40) || (i6 == 60) || (i6 == 93) || (i6 == 10))
              break;
            if ((i6 != 45) && (i6 != 46) && (i6 != 32) && ((i6 < 48) || (i6 > 57)))
              i26 = 1;
            i15++;
          }
          if (i26 != 0)
            i15 = i24;
          else
            f17 = getLeading(paramArrayOfByte, f17, i21, i24, bool10);
          if ((i29 != 0) && (f17 == -f2))
            f6 -= f17;
        }
        f2 += f17;
        f6 += f17;
        f7 += f6;
      }
      if (this.textExtracted)
        bool7 = setExtractedText(n, (float[][])localObject2, f3, f5, (String)localObject5, bool7);
      i15++;
    }
    if (this.returnText)
      if (!this.tjTextValue.isEmpty())
        this.tjTextValue = new StringBuilder().append(this.tjTextValue).append(' ').append(localStringBuffer2.toString()).toString();
      else
        this.tjTextValue = localStringBuffer2.toString();
    arrayOfFloat[0][0] = 1.0F;
    arrayOfFloat[0][1] = 0.0F;
    arrayOfFloat[0][2] = 0.0F;
    arrayOfFloat[1][0] = 0.0F;
    arrayOfFloat[1][1] = 1.0F;
    arrayOfFloat[1][2] = 0.0F;
    if (f6 < 0.0F)
      arrayOfFloat[2][0] = f5;
    else
      arrayOfFloat[2][0] = (f5 + f6);
    arrayOfFloat[2][1] = 0.0F;
    arrayOfFloat[2][2] = 1.0F;
    localObject2 = Matrix.multiply(arrayOfFloat, (float[][])localObject2);
    this.currentTextState.Tm[2][0] = localObject2[2][0];
    this.currentTextState.Tm[2][1] = (localObject2[2][1] - this.currentTextState.getTextRise());
    if (this.currentRotation != 0)
    {
      localObject1 = Matrix.multiply(arrayOfFloat, (float[][])localObject1);
      this.currentTextState.TmNoRotation[2][0] = localObject1[2][0];
      this.currentTextState.TmNoRotation[2][1] = localObject1[2][1];
    }
    if (this.textExtracted)
    {
      if (i9 == 32)
      {
        localObject2 = localObject3;
        if (this.currentRotation != 0)
          localObject1 = localObject4;
      }
      if (this.currentRotation == 0)
        calcCoordinates(f14, (float[][])localObject2, bool3, f16, this.fontSize, f15);
      else
        calcCoordinates(f14, (float[][])localObject1, bool3, f16, this.fontSize, f15);
      if ((localStringBuffer1 != null) && (this.actualText != null))
      {
        int i19 = localStringBuffer1.indexOf(PdfData.marker, 2);
        if (i19 > 0)
          i19 = localStringBuffer1.indexOf(PdfData.marker, i19 + 1);
        if (i19 > 0)
        {
          localStringBuffer1.setLength(i19 + 1);
          localStringBuffer1.append(this.actualText);
        }
        this.actualText = null;
      }
      if ((localStringBuffer1.length() == 0) || (!bool1))
        localStringBuffer1 = null;
      return localStringBuffer1;
    }
    return null;
  }

  private boolean renderTextWithJavaFonts(float paramFloat1, boolean paramBoolean1, int paramInt1, int paramInt2, int paramInt3, float paramFloat2, String paramString1, String paramString2, boolean paramBoolean2, PdfJavaGlyphs paramPdfJavaGlyphs, float[][] paramArrayOfFloat)
  {
    AffineTransform localAffineTransform1 = null;
    int i = (paramFloat1 > 0.0F) || (DecoderOptions.isRunningOnMac) || (StandardFonts.isStandardFont(this.currentFontData.getBaseFontName(), false)) || (this.currentFontData.isBrokenFont()) ? 1 : 0;
    if ((paramPdfJavaGlyphs.lastTrm[0][0] != paramArrayOfFloat[0][0]) || (paramPdfJavaGlyphs.lastTrm[1][0] != paramArrayOfFloat[1][0]) || (paramPdfJavaGlyphs.lastTrm[0][1] != paramArrayOfFloat[0][1]) || (paramPdfJavaGlyphs.lastTrm[1][1] != paramArrayOfFloat[1][1]))
    {
      paramPdfJavaGlyphs.lastTrm = paramArrayOfFloat;
      paramPdfJavaGlyphs.flush();
    }
    Area localArea2 = paramPdfJavaGlyphs.getCachedShape(paramInt3);
    localAffineTransform1 = paramPdfJavaGlyphs.getCachedTransform(paramInt3);
    if (localArea2 == null)
    {
      double d1 = -1.0D;
      double d2 = 1.0D;
      double d3 = 0.0D;
      double d4 = 0.0D;
      if (((paramArrayOfFloat[1][0] < 0.0F) && (paramArrayOfFloat[0][1] >= 0.0F)) || ((paramArrayOfFloat[0][1] < 0.0F) && (paramArrayOfFloat[1][0] >= 0.0F)))
      {
        d2 = 1.0D;
        d1 = -1.0D;
      }
      if (i != 0)
      {
        localArea2 = paramPdfJavaGlyphs.getGlyph(paramInt3, paramString1, paramFloat2);
        if ((localArea2 != null) && (paramInt3 == 146) && (paramPdfJavaGlyphs.isArialInstalledLocally))
          d4 = -(localArea2.getBounds().height - localArea2.getBounds().y);
      }
      else
      {
        String str = paramString1;
        if ((paramPdfJavaGlyphs.remapFont) && (!paramPdfJavaGlyphs.getUnscaledFont().canDisplay(str.charAt(0))))
          str = String.valueOf((char)(paramInt3 + 61440));
        GlyphVector localGlyphVector = null;
        if ((!paramPdfJavaGlyphs.isCIDFont) || (paramPdfJavaGlyphs.isFontInstalled))
          localGlyphVector = paramPdfJavaGlyphs.getUnscaledFont().createGlyphVector(PdfJavaGlyphs.frc, str);
        if (localGlyphVector != null)
        {
          localArea2 = new Area(localGlyphVector.getOutline());
          double d5 = localGlyphVector.getOutline().getBounds2D().getX();
          d3 = 0.0D;
          if (d5 < 0.0D)
          {
            d5 = -d5;
            d3 = d5 * 2.0D;
            if (paramArrayOfFloat[0][0] > paramArrayOfFloat[0][1])
              d3 *= paramArrayOfFloat[0][0];
            else
              d3 *= paramArrayOfFloat[0][1];
          }
          double d6 = localGlyphVector.getVisualBounds().getWidth() + d5 * 2.0D;
          double d7 = paramFloat2 / d6;
          if (d7 < 1.0D)
            d2 *= d7;
          if (d3 > 0.0D)
            d3 *= d2;
        }
      }
      localAffineTransform1 = new AffineTransform(d2 * paramArrayOfFloat[0][0], d2 * paramArrayOfFloat[0][1], d1 * paramArrayOfFloat[1][0], d1 * paramArrayOfFloat[1][1], d3, d4);
      paramPdfJavaGlyphs.setCachedShape(paramInt3, localArea2, localAffineTransform1);
    }
    if ((localArea2 != null) && (paramInt1 == 7) && (localArea2.getBounds().width > 0))
    {
      Area localArea3 = (Area)localArea2.clone();
      localArea3.transform(localAffineTransform1);
      if (this.renderDirectly)
      {
        AffineTransform localAffineTransform2 = AffineTransform.getTranslateInstance(paramArrayOfFloat[2][0], paramArrayOfFloat[2][1]);
        localArea3.transform(localAffineTransform2);
      }
      this.gs.addClip(localArea3);
      this.current.drawClip(this.gs, null, false);
      if (this.renderDirectly)
        localArea2 = null;
    }
    Area localArea1 = localArea2;
    if (localArea1 != null)
      if (this.useJavaFX)
        this.current.drawEmbeddedText(paramArrayOfFloat, paramInt2, null, null, 1, this.gs, localAffineTransform1, paramString2, this.currentFontData, -100.0F);
      else if (this.renderDirectly)
        this.current.drawEmbeddedText(paramArrayOfFloat, paramInt2, null, localArea1, 1, this.gs, localAffineTransform1, paramString2, this.currentFontData, -100.0F);
      else if (paramBoolean2)
        this.current.drawEmbeddedText(paramArrayOfFloat, -paramInt2, null, localArea1, 1, this.gs, null, paramString2, this.currentFontData, -100.0F);
      else
        this.current.drawEmbeddedText(paramArrayOfFloat, paramInt2, null, localArea1, 1, this.gs, null, paramString2, this.currentFontData, -100.0F);
    return paramBoolean1;
  }

  private static boolean writeOutText(boolean paramBoolean1, boolean paramBoolean2, float[][] paramArrayOfFloat1, float[][] paramArrayOfFloat2, float paramFloat1, float paramFloat2, String paramString1, StringBuffer paramStringBuffer, String paramString2, boolean paramBoolean3, int paramInt)
  {
    if (!paramString1.isEmpty())
    {
      if (DecoderOptions.embedWidthData)
      {
        float f1 = paramArrayOfFloat2[2][0];
        float f2 = paramArrayOfFloat2[2][1];
        if (paramInt != 0)
        {
          f1 = paramArrayOfFloat1[2][0];
          f2 = paramArrayOfFloat1[2][1];
        }
        paramStringBuffer.append(paramString2);
        if (paramBoolean2)
        {
          paramStringBuffer.append(PdfData.marker);
          paramStringBuffer.append(f1);
          paramStringBuffer.append(PdfData.marker);
        }
        else
        {
          paramStringBuffer.append(PdfData.marker);
          paramStringBuffer.append(f2);
          paramStringBuffer.append(PdfData.marker);
        }
        paramStringBuffer.append(paramFloat2 * paramFloat1);
        paramStringBuffer.append(PdfData.marker);
      }
      else
      {
        paramStringBuffer.append(paramString2);
      }
      int i = paramString1.length();
      for (int k = 0; k < i; k++)
      {
        int j = paramString1.charAt(k);
        paramBoolean1 = true;
        if (j == 9)
          j = 32;
        if ((j == 60) && (paramBoolean3))
          paramStringBuffer.append("&lt;");
        else if ((j == 62) && (paramBoolean3))
          paramStringBuffer.append("&gt;");
        else if (j == 64258)
          paramStringBuffer.append("fl");
        else if (j > 31)
          paramStringBuffer.append(j);
        else
          paramStringBuffer.append(hex[j]);
      }
    }
    else
    {
      paramStringBuffer.append(paramString2);
    }
    return paramBoolean1;
  }

  public Object getObjectValue(int paramInt)
  {
    switch (paramInt)
    {
    case -21:
      return this.textAreas;
    case 22:
      return this.textDirections;
    }
    return null;
  }

  private static String getString(int paramInt1, int paramInt2, byte[] paramArrayOfByte)
  {
    while ((paramArrayOfByte[paramInt2] == 32) || (paramArrayOfByte[paramInt2] == 13) || (paramArrayOfByte[paramInt2] == 10))
      paramInt2--;
    int i = paramInt2 - paramInt1 + 1;
    int j = 0;
    for (int k = 0; k < i; k++)
      if ((k > 0) && ((paramArrayOfByte[(paramInt1 + k)] == 32) || (paramArrayOfByte[(paramInt1 + k)] == 13) || (paramArrayOfByte[(paramInt1 + k)] == 10)) && ((paramArrayOfByte[(paramInt1 + k - 1)] == 32) || (paramArrayOfByte[(paramInt1 + k - 1)] == 13) || (paramArrayOfByte[(paramInt1 + k - 1)] == 10)))
        j++;
    char[] arrayOfChar = new char[i - j];
    int m = 0;
    for (int n = 0; n < i; n++)
      if ((n <= 0) || ((paramArrayOfByte[(paramInt1 + n)] != 32) && (paramArrayOfByte[(paramInt1 + n)] != 13) && (paramArrayOfByte[(paramInt1 + n)] != 10)) || ((paramArrayOfByte[(paramInt1 + n - 1)] != 32) && (paramArrayOfByte[(paramInt1 + n - 1)] != 13) && (paramArrayOfByte[(paramInt1 + n - 1)] != 10)))
      {
        if ((paramArrayOfByte[(paramInt1 + n)] == 10) || (paramArrayOfByte[(paramInt1 + n)] == 13))
          arrayOfChar[m] = ' ';
        else
          arrayOfChar[m] = ((char)paramArrayOfByte[(paramInt1 + n)]);
        m++;
      }
    String str = String.copyValueOf(arrayOfChar);
    return str;
  }

  private static float getLeading(byte[] paramArrayOfByte, float paramFloat, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    int i;
    if (paramBoolean)
    {
      for (i = paramInt2; (paramArrayOfByte[i] == 10) || (paramArrayOfByte[i] == 9) || (paramArrayOfByte[i] == 32) || (paramArrayOfByte[i] == 13); i++);
      String str = getString(i, i + paramInt1 - 1, paramArrayOfByte);
      StringTokenizer localStringTokenizer = new StringTokenizer(str);
      for (paramFloat = 0.0F; localStringTokenizer.hasMoreTokens(); paramFloat += Float.parseFloat(localStringTokenizer.nextToken()));
      paramFloat = -paramFloat / 1000.0F;
    }
    else if (paramInt1 > 0)
    {
      for (i = paramInt2; (paramArrayOfByte[i] == 10) || (paramArrayOfByte[i] == 9) || (paramArrayOfByte[i] == 32) || (paramArrayOfByte[i] == 13); i++);
      paramFloat = -NumberUtils.parseFloat(i, i + paramInt1, paramArrayOfByte) / 1000.0F;
    }
    return paramFloat;
  }

  public boolean setExtractedText(int paramInt, float[][] paramArrayOfFloat, float paramFloat1, float paramFloat2, String paramString, boolean paramBoolean)
  {
    if ((!paramString.isEmpty()) && (!paramString.equals(" ")))
    {
      float f1 = (int)paramArrayOfFloat[2][0];
      if (paramInt == 1)
        f1 -= paramArrayOfFloat[1][0];
      float f2 = (int)paramArrayOfFloat[2][1];
      float f3 = paramFloat2 * paramFloat1;
      float f4 = paramArrayOfFloat[1][1];
      if (f4 == 0.0F)
        f4 = paramArrayOfFloat[0][1];
      if ((this.currentFontData.getFontType() == 1228944679) && (f4 != 0.0F) && ((int)f4 == 0) && (this.currentFontData.FontMatrix[3] == -1.0D))
      {
        f4 *= (this.currentFontData.FontBBox[3] - this.currentFontData.FontBBox[1]);
        f4 = -f4;
      }
      f4 = (int)f4;
      if (f3 < 0.0F)
      {
        f3 = -f3;
        f1 -= f3;
      }
      if (f4 < 0.0F)
      {
        f4 = -f4;
        f2 -= f4;
      }
      Rectangle localRectangle = this.currentFontData.getBoundingBox();
      if (localRectangle.y < 0)
      {
        localRectangle.height -= localRectangle.y;
        localRectangle.y = 0;
      }
      float f5 = localRectangle.y;
      if (f5 == 0.0F)
        f5 = 100.0F;
      if (f5 < 0.0F)
        f5 = -f5;
      float f6 = 1000.0F + f5;
      f6 = 1000.0F / f6;
      float f7;
      switch (this.currentTextState.writingMode)
      {
      case 0:
        f7 = f4 / f6;
        f2 -= f7 - f4;
        f4 = f7;
        break;
      case 1:
        break;
      case 2:
        f7 = f3 / f6;
        f1 -= f7 - f3;
        f3 = f7;
        break;
      case 3:
        f7 = f3 / f6;
        f1 -= f7;
        f3 = f7;
      }
      f1 -= 1.0F;
      f3 += 2.0F;
      if (this.highlightCoords)
      {
        if (paramBoolean)
        {
          this.y2 = f2;
          this.y1 = (f2 + f4);
          paramBoolean = false;
        }
        if (f2 < this.y2)
          this.y2 = f2;
        if (f2 + f4 > this.y1)
          this.y1 = (f2 + f4);
      }
      if (this.renderText)
      {
        this.textAreas.addElement(new Rectangle((int)f1, (int)f2, (int)f3, (int)f4));
        this.textDirections.addElement(this.currentTextState.writingMode);
      }
    }
    return paramBoolean;
  }

  private static int readEscapeValue(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte)
  {
    int i = 0;
    int k;
    int m;
    int j;
    if (paramInt3 == 8)
    {
      k = 0;
      for (m = 1; m < paramInt2 + 1; m++)
      {
        j = paramArrayOfByte[(paramInt1 + paramInt2 - m)];
        if ((j >= 48) && (j <= 55))
        {
          j -= 48;
          i += (j << multiply8[k]);
          k++;
        }
      }
    }
    else if (paramInt3 == 16)
    {
      k = 0;
      for (m = 1; m < paramInt2 + 1; m++)
      {
        j = paramArrayOfByte[(paramInt1 + paramInt2 - m)];
        if ((j >= 65) && (j <= 70))
        {
          j -= 55;
        }
        else if ((j >= 97) && (j <= 102))
        {
          j -= 87;
        }
        else
        {
          if ((j < 48) || (j > 57))
            continue;
          j -= 48;
        }
        i += (j << multiply16[k]);
        k++;
      }
    }
    else
    {
      StringBuilder localStringBuilder = new StringBuilder(10);
      for (k = 0; k < paramInt2; k++)
        localStringBuilder.append((char)paramArrayOfByte[(paramInt1 + k)]);
      i = Integer.parseInt(localStringBuilder.toString(), paramInt3);
    }
    return i;
  }

  void storeExtractedText(String paramString1, StringBuffer paramStringBuffer, int paramInt, String paramString2)
  {
    if (this.textExtracted)
      this.pdfData.addRawTextElement(this.charSpacing * 1000.0F, this.currentTextState.writingMode, Fonts.createFontToken(paramString2, paramInt), this.currentFontData.getCurrentFontSpaceWidth(), paramInt, this.x1, this.y1, this.x2, this.y2, this.moveCommand, paramStringBuffer, this.textLength, paramString1, this.isXMLExtraction);
  }

  public String getLastTextValue()
  {
    this.returnText = false;
    return this.tjTextValue;
  }

  public boolean isTTHintingRequired()
  {
    return this.ttHintingRequired;
  }

  public void reset()
  {
    this.multipleTJs = false;
  }

  public void setReturnText(boolean paramBoolean)
  {
    this.returnText = paramBoolean;
  }

  public void setParameters(boolean paramBoolean1, boolean paramBoolean2, int paramInt1, int paramInt2, boolean paramBoolean3, boolean paramBoolean4)
  {
    super.setParameters(paramBoolean1, paramBoolean2, paramInt1, paramInt2, paramBoolean4);
    this.isPrinting = paramBoolean3;
    this.textExtracted = ((paramInt2 & 0x1) == 1);
    this.renderText = ((paramBoolean2) && ((paramInt1 & 0x1) == 1));
    this.textColorExtracted = ((paramInt2 & 0x40) == 64);
  }

  public void setBooleanValue(int paramInt, boolean paramBoolean)
  {
    switch (paramInt)
    {
    case 21:
      this.generateGlyphOnRender = paramBoolean;
      break;
    default:
      super.setBooleanValue(paramInt, paramBoolean);
    }
  }

  public void setIntValue(int paramInt1, int paramInt2)
  {
    switch (paramInt1)
    {
    case 20:
      this.textPrint = paramInt2;
      break;
    default:
      super.setIntValue(paramInt1, paramInt2);
    }
  }

  void setXFA(boolean paramBoolean)
  {
    this.isXFA = paramBoolean;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.parser.TextDecoder
 * JD-Core Version:    0.6.2
 */