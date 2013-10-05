package org.jpedal.fonts.tt;

import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.io.PrintStream;
import java.util.HashSet;
import javafx.scene.shape.Path;
import org.jpedal.color.PdfPaint;
import org.jpedal.fonts.tt.hinting.TTVM;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.repositories.Vector_Double;
import org.jpedal.utils.repositories.Vector_Int;
import org.jpedal.utils.repositories.Vector_Object;
import org.jpedal.utils.repositories.Vector_Path;
import org.jpedal.utils.repositories.Vector_Short;

public abstract class BaseTTGlyph
{
  transient Vector_Path paths = new Vector_Path(10);
  transient Path pathsFX = null;
  protected boolean ttHintingRequired = false;
  public static boolean useHinting = true;
  protected boolean containsBrokenGlyfData = false;
  protected short compMinX;
  protected short compMinY;
  protected short compMaxX;
  protected short compMaxY;
  protected short minX;
  protected short minY;
  protected short maxX;
  protected short maxY;
  protected int[] scaledX;
  protected int[] scaledY;
  protected int BPoint1;
  protected int BPoint2;
  protected Vector_Int xtranslateValues = new Vector_Int(5);
  protected Vector_Int ytranslateValues = new Vector_Int(5);
  protected short leftSideBearing;
  protected Vector_Double xscaleValues = new Vector_Double(5);
  protected Vector_Double yscaleValues = new Vector_Double(5);
  protected Vector_Double scale01Values = new Vector_Double(5);
  protected Vector_Double scale10Values = new Vector_Double(5);
  protected double xscale = 1.0D;
  protected double yscale = 1.0D;
  protected double scale01 = 0.0D;
  protected double scale10 = 0.0D;
  protected int[] instructions;
  protected int xtranslate;
  protected int ytranslate;
  protected int currentInstructionDepth = 2147483647;
  protected Vector_Object glyfX = new Vector_Object(5);
  protected Vector_Object glyfY = new Vector_Object(5);
  protected Vector_Object curves = new Vector_Object(5);
  protected Vector_Object contours = new Vector_Object(5);
  protected Vector_Int endPtIndices = new Vector_Int(5);
  protected int contourCount = 0;
  protected float unitsPerEm = 64.0F;
  public static boolean debug = false;
  protected int glyphNumber = -1;
  protected int compCount = 1;
  protected boolean isComposite = false;
  protected double pixelSize;
  private static HashSet testedFonts = new HashSet();
  int BP1x = -1;
  int BP2x = -1;
  int BP1y = -1;
  int BP2y = -1;
  int existingXTranslate = 0;
  int existingYTranslate = 0;
  int depth = 0;
  String glyfName = "";
  public static boolean redecodePage = false;
  boolean isHinted;
  private TTVM vm;
  private String baseFontName;
  int id = 0;

  public BaseTTGlyph(String paramString, Glyf paramGlyf, FontFile2 paramFontFile2, Hmtx paramHmtx, int paramInt, float paramFloat, TTVM paramTTVM, boolean paramBoolean)
  {
    this.glyphNumber = (paramInt + 1);
    this.isHinted = true;
    this.glyfName = paramString;
    this.leftSideBearing = paramHmtx.getLeftSideBearing(paramInt);
    this.unitsPerEm = paramFloat;
    int i = paramGlyf.getCharString(paramInt);
    paramFontFile2.setPointer(i);
    readGlyph(paramGlyf, paramFontFile2);
    this.vm = paramTTVM;
    createGlyph(this.isHinted);
  }

  public BaseTTGlyph(String paramString1, Glyf paramGlyf, FontFile2 paramFontFile2, Hmtx paramHmtx, int paramInt, float paramFloat, String paramString2, boolean paramBoolean)
  {
    this.glyphNumber = (paramInt + 1);
    this.glyfName = paramString1;
    this.leftSideBearing = paramHmtx.getLeftSideBearing(paramInt);
    this.unitsPerEm = paramFloat;
    this.baseFontName = paramString2;
    int i = paramGlyf.getCharString(paramInt);
    paramFontFile2.setPointer(i);
    readGlyph(paramGlyf, paramFontFile2);
    createGlyph(false);
  }

  void createGlyph(boolean paramBoolean)
  {
    if (paramBoolean)
      createHintedGlyph();
    else
      createUnhintedGlyph();
  }

  void createUnhintedGlyph()
  {
    for (int i = 0; i < this.compCount; i++)
    {
      int[] arrayOfInt1 = (int[])this.glyfX.elementAt(i);
      int[] arrayOfInt2 = (int[])this.glyfY.elementAt(i);
      boolean[] arrayOfBoolean1 = (boolean[])this.curves.elementAt(i);
      boolean[] arrayOfBoolean2 = (boolean[])this.contours.elementAt(i);
      int j = this.endPtIndices.elementAt(i);
      if (this.isComposite)
      {
        this.xtranslate = this.xtranslateValues.elementAt(i);
        this.ytranslate = this.ytranslateValues.elementAt(i);
        this.xscale = this.xscaleValues.elementAt(i);
        this.yscale = this.yscaleValues.elementAt(i);
        this.scale01 = this.scale01Values.elementAt(i);
        this.scale10 = this.scale10Values.elementAt(i);
        if ((this.BPoint1 != -1) && (this.BPoint2 != -1))
          if ((this.BP1x == -1) && (this.BP2x == -1) && (this.BP1y == -1) && (this.BP2y == -1))
          {
            this.BP1x = arrayOfInt1[this.BPoint1];
            this.BP1y = arrayOfInt2[this.BPoint1];
          }
          else
          {
            this.BP2x = arrayOfInt1[this.BPoint2];
            this.BP2y = arrayOfInt2[this.BPoint2];
            int k = this.BP1x - this.BP2x;
            int m = this.BP1y - this.BP2y;
            int n = arrayOfInt1.length;
            for (int i1 = 0; i1 < n; i1++)
            {
              arrayOfInt1[i1] += k;
              if (debug)
                System.out.println(arrayOfInt2[i1] + " " + m + " BP1y=" + this.BP1y + " BP1y=" + this.BP1y);
              arrayOfInt2[i1] += m;
            }
            this.BP1x = -1;
            this.BP2x = -1;
            this.BP1y = -1;
            this.BP2y = -1;
          }
      }
      if ((this.baseFontName != null) && (this.instructions != null) && (!testedFonts.contains(this.baseFontName)))
      {
        testedFonts.add(this.baseFontName);
        this.baseFontName = this.baseFontName.toLowerCase();
        if ((this.baseFontName.contains("mingli")) || (this.baseFontName.contains("kai")) || (this.baseFontName.contains("huatian")))
        {
          this.ttHintingRequired = true;
          if (LogWriter.isOutput())
            LogWriter.writeLog("TrueType hinting probably required for font " + this.baseFontName);
        }
      }
      createPaths(arrayOfInt1, arrayOfInt2, arrayOfBoolean1, arrayOfBoolean2, j);
    }
  }

  static int midPt(int paramInt1, int paramInt2)
  {
    return paramInt1 + (paramInt2 - paramInt1) / 2;
  }

  public float getmaxWidth()
  {
    return 0.0F;
  }

  public float getmaxHeight()
  {
    return 0.0F;
  }

  public void setT3Colors(PdfPaint paramPdfPaint1, PdfPaint paramPdfPaint2, boolean paramBoolean)
  {
  }

  public boolean ignoreColors()
  {
    return false;
  }

  public String getGlyphName()
  {
    return null;
  }

  public void scaler(int[] paramArrayOfInt1, int[] paramArrayOfInt2)
  {
    this.scaledX = new int[paramArrayOfInt1.length];
    this.scaledY = new int[paramArrayOfInt2.length];
    double d = this.pixelSize / (this.unitsPerEm * 1000.0F) * 64.0D;
    for (int i = 0; i < paramArrayOfInt1.length; i++)
    {
      this.scaledX[i] = ((int)(d * paramArrayOfInt1[i] + 0.5D));
      this.scaledY[i] = ((int)(d * paramArrayOfInt2[i] + 0.5D));
    }
    this.scaledX[(paramArrayOfInt1.length - 2)] = 0;
    this.scaledY[(paramArrayOfInt2.length - 2)] = 0;
    this.scaledX[(paramArrayOfInt1.length - 1)] = ((int)(d * this.leftSideBearing + 0.5D));
    this.scaledY[(paramArrayOfInt2.length - 1)] = 0;
  }

  public final void readComplexGlyph(Glyf paramGlyf, FontFile2 paramFontFile2)
  {
    this.isComposite = true;
    this.xtranslateValues.pull();
    this.ytranslateValues.pull();
    this.xscaleValues.pull();
    this.yscaleValues.pull();
    this.scale01Values.pull();
    this.scale10Values.pull();
    this.BPoint1 = -1;
    this.BPoint2 = -1;
    int i = 0;
    int j = paramGlyf.getGlypfCount();
    while (true)
    {
      int k = paramFontFile2.getNextUint16();
      int m = paramFontFile2.getNextUint16();
      if (debug)
        System.err.println("Index=" + m + " flag=" + k + ' ' + j);
      if (m >= j)
      {
        this.containsBrokenGlyfData = true;
        break;
      }
      int n = (k & 0x1) == 1 ? 1 : 0;
      int i1 = (k & 0x2) == 2 ? 1 : 0;
      int i2 = (k & 0x8) == 8 ? 1 : 0;
      int i3 = (k & 0x40) == 64 ? 1 : 0;
      int i4 = (k & 0x80) == 128 ? 1 : 0;
      i = (i != 0) || ((k & 0x100) == 256) ? 1 : 0;
      if ((n != 0) && (i1 != 0))
      {
        this.xtranslate = paramFontFile2.getNextInt16();
        this.ytranslate = paramFontFile2.getNextInt16();
      }
      else if ((n == 0) && (i1 != 0))
      {
        this.xtranslate = paramFontFile2.getNextint8();
        this.ytranslate = paramFontFile2.getNextint8();
      }
      else if ((n != 0) && (i1 == 0))
      {
        this.BPoint1 = paramFontFile2.getNextUint16();
        this.BPoint2 = paramFontFile2.getNextUint16();
        this.xtranslate = 0;
        this.ytranslate = 0;
      }
      else if ((n == 0) && (i1 == 0))
      {
        this.BPoint1 = paramFontFile2.getNextUint8();
        this.BPoint2 = paramFontFile2.getNextUint8();
        this.xtranslate = 0;
        this.ytranslate = 0;
      }
      this.xscale = 1.0D;
      this.scale01 = 0.0D;
      this.scale10 = 0.0D;
      this.yscale = 1.0D;
      if ((i2 != 0) || (i3 != 0) || (i4 != 0))
        if ((i2 != 0) && (i3 == 0) && (i4 == 0))
        {
          this.xscale = paramFontFile2.getF2Dot14();
          this.scale01 = 0.0D;
          this.scale10 = 0.0D;
          this.yscale = this.xscale;
        }
        else if ((i2 == 0) && (i3 != 0) && (i4 == 0))
        {
          this.xscale = paramFontFile2.getF2Dot14();
          this.scale01 = 0.0D;
          this.scale10 = 0.0D;
          this.yscale = paramFontFile2.getF2Dot14();
        }
        else if ((i2 == 0) && (i3 == 0) && (i4 != 0))
        {
          this.xscale = paramFontFile2.getF2Dot14();
          this.scale01 = paramFontFile2.getF2Dot14();
          this.scale10 = paramFontFile2.getF2Dot14();
          this.yscale = paramFontFile2.getF2Dot14();
        }
      int i5 = this.xtranslate;
      int i6 = this.ytranslate;
      this.xtranslate += this.existingXTranslate;
      this.ytranslate += this.existingYTranslate;
      this.xtranslateValues.addElement(this.xtranslate);
      this.ytranslateValues.addElement(this.ytranslate);
      this.xscaleValues.addElement(this.xscale);
      this.yscaleValues.addElement(this.yscale);
      this.scale01Values.addElement(this.scale01);
      this.scale10Values.addElement(this.scale10);
      int i7 = paramFontFile2.getPointer();
      int i8 = paramGlyf.getCharString(m);
      if (i8 != -1)
      {
        if (i8 < 0)
          i8 = -i8;
        paramFontFile2.setPointer(i8);
        this.existingXTranslate = this.xtranslate;
        this.existingYTranslate = this.ytranslate;
        this.depth += 1;
        readGlyph(paramGlyf, paramFontFile2);
        this.depth -= 1;
        this.existingXTranslate -= i5;
        this.existingYTranslate -= i6;
      }
      else
      {
        System.err.println("Wrong value in complex");
      }
      paramFontFile2.setPointer(i7);
      if ((k & 0x20) == 0)
      {
        if (i != 0)
        {
          int i9 = paramFontFile2.getNextUint16();
          int[] arrayOfInt = new int[i9];
          for (int i10 = 0; i10 < i9; i10++)
            arrayOfInt[i10] = paramFontFile2.getNextUint8();
          if (this.depth <= this.currentInstructionDepth)
          {
            this.instructions = arrayOfInt;
            this.currentInstructionDepth = this.depth;
          }
          break;
        }
        if (this.depth > this.currentInstructionDepth)
          break;
        this.instructions = new int[0];
        this.currentInstructionDepth = this.depth;
        break;
      }
      this.compCount += 1;
    }
  }

  public void readSimpleGlyph(FontFile2 paramFontFile2)
  {
    int i = 1;
    Vector_Int localVector_Int1 = new Vector_Int(50);
    Vector_Int localVector_Int2 = new Vector_Int(50);
    Vector_Short localVector_Short1 = new Vector_Short(50);
    Vector_Short localVector_Short2 = new Vector_Short(50);
    if (debug)
    {
      System.out.println("endPoints");
      System.out.println("---------");
    }
    try
    {
      int j = 0;
      for (int k = 0; k < this.contourCount; k++)
      {
        j = paramFontFile2.getNextUint16();
        if (debug)
          System.out.println(k + " " + j);
        localVector_Int2.addElement(j);
      }
      if (paramFontFile2.hasValuesLeft())
      {
        k = paramFontFile2.getNextUint16();
        int[] arrayOfInt1 = new int[k];
        for (int m = 0; m < k; m++)
          arrayOfInt1[m] = paramFontFile2.getNextUint8();
        if (this.depth < this.currentInstructionDepth)
          this.instructions = arrayOfInt1;
        if (debug)
        {
          System.out.println("Instructions");
          System.out.println("------------");
          System.out.println("count=" + k);
        }
        m = j + 1;
        int n;
        int i2;
        for (int i1 = 0; i1 < m; i1++)
        {
          if (paramFontFile2.getBytesLeft() < 1)
            return;
          n = paramFontFile2.getNextUint8();
          localVector_Int1.addElement(n);
          i++;
          if ((n & 0x8) == 8)
          {
            i2 = paramFontFile2.getNextUint8();
            for (int i4 = 1; i4 <= i2; i4++)
            {
              localVector_Int1.addElement(n);
              i++;
            }
            i1 += i2;
          }
        }
        for (i1 = 0; i1 < m; i1++)
        {
          n = localVector_Int1.elementAt(i1);
          short s;
          if ((n & 0x10) != 0)
          {
            if ((n & 0x2) != 0)
            {
              s = (short)paramFontFile2.getNextUint8();
              localVector_Short1.addElement(s);
            }
            else
            {
              localVector_Short1.addElement((short)0);
            }
          }
          else if ((n & 0x2) != 0)
          {
            s = (short)-paramFontFile2.getNextUint8();
            localVector_Short1.addElement(s);
          }
          else
          {
            s = paramFontFile2.getNextSignedInt16();
            localVector_Short1.addElement(s);
          }
        }
        for (i1 = 0; i1 < m; i1++)
        {
          n = localVector_Int1.elementAt(i1);
          if ((n & 0x20) != 0)
          {
            if ((n & 0x4) != 0)
            {
              if (paramFontFile2.getBytesLeft() < 1)
                return;
              localVector_Short2.addElement((short)paramFontFile2.getNextUint8());
            }
            else
            {
              localVector_Short2.addElement((short)0);
            }
          }
          else if ((n & 0x4) != 0)
          {
            localVector_Short2.addElement((short)-paramFontFile2.getNextUint8());
          }
          else
          {
            i2 = paramFontFile2.getNextSignedInt16();
            localVector_Short2.addElement(i2);
          }
        }
        i1 = 0;
        int i3 = 0;
        int i5 = 0;
        int[] arrayOfInt2 = localVector_Int1.get();
        int[] arrayOfInt3 = localVector_Int2.get();
        short[] arrayOfShort1 = localVector_Short1.get();
        short[] arrayOfShort2 = localVector_Short2.get();
        m = arrayOfShort1.length;
        int[] arrayOfInt4 = new int[m + 2];
        int[] arrayOfInt5 = new int[m + 2];
        boolean[] arrayOfBoolean1 = new boolean[m + 2];
        boolean[] arrayOfBoolean2 = new boolean[m + 2];
        int i6 = 0;
        if (debug)
        {
          System.out.println("Points");
          System.out.println("------");
        }
        int i8;
        for (int i7 = 0; i7 < m; i7++)
        {
          i8 = arrayOfInt3[i1] == i7 ? 1 : 0;
          if (i8 != 0)
          {
            i1++;
            i6 = i7 + 1;
          }
          i3 += arrayOfShort1[i7];
          i5 += arrayOfShort2[i7];
          arrayOfInt4[i7] = i3;
          arrayOfInt5[i7] = i5;
          arrayOfBoolean1[i7] = ((i7 < i) && ((arrayOfInt2[i7] & 0x1) != 0) ? 1 : false);
          arrayOfBoolean2[i7] = i8;
          if (debug)
            System.out.println(i7 + " " + arrayOfInt4[i7] + ' ' + arrayOfInt5[i7] + " on curve=" + arrayOfBoolean1[i7] + " endOfContour[i]=" + arrayOfBoolean2[i7]);
        }
        for (i7 = 0; i7 < arrayOfInt4.length; i7++)
        {
          i8 = arrayOfInt4[i7];
          int i9 = arrayOfInt5[i7];
          if (!this.isComposite)
          {
            if (!useHinting)
              arrayOfInt4[i7] = ((int)(i8 / this.unitsPerEm));
            else
              arrayOfInt4[i7] = i8;
          }
          else if (!useHinting)
            arrayOfInt4[i7] = ((int)((i8 * this.xscale + i9 * this.scale10 + this.xtranslate) / this.unitsPerEm));
          else
            arrayOfInt4[i7] = ((int)(i8 * this.xscale + i9 * this.scale10 + this.xtranslate));
          if (!this.isComposite)
          {
            if (!useHinting)
              arrayOfInt5[i7] = ((int)(i9 / this.unitsPerEm));
            else
              arrayOfInt5[i7] = i9;
          }
          else if (!useHinting)
            arrayOfInt5[i7] = ((int)((i8 * this.scale01 + i9 * this.yscale + this.ytranslate) / this.unitsPerEm));
          else
            arrayOfInt5[i7] = ((int)(i8 * this.scale01 + i9 * this.yscale + this.ytranslate));
        }
        this.glyfX.addElement(arrayOfInt4);
        this.glyfY.addElement(arrayOfInt5);
        this.curves.addElement(arrayOfBoolean1);
        this.contours.addElement(arrayOfBoolean2);
        this.endPtIndices.addElement(i6);
      }
    }
    catch (Exception localException)
    {
    }
  }

  public void readGlyph(Glyf paramGlyf, FontFile2 paramFontFile2)
  {
    this.contourCount = paramFontFile2.getNextUint16();
    this.minX = ((short)paramFontFile2.getNextUint16());
    this.minY = ((short)paramFontFile2.getNextUint16());
    this.maxX = ((short)paramFontFile2.getNextUint16());
    this.maxY = ((short)paramFontFile2.getNextUint16());
    if ((this.minX > this.maxX) || (this.minY > this.maxY))
      return;
    if (debug)
    {
      System.out.println("------------------------------------------------------------");
      System.out.println("min=" + this.minX + ' ' + this.minY + " max=" + this.maxX + ' ' + this.maxY + " contourCount=" + this.contourCount);
    }
    if (this.contourCount != 65535)
    {
      if (this.contourCount > 0)
        readSimpleGlyph(paramFontFile2);
    }
    else
    {
      this.compMinX = this.minX;
      this.compMinY = this.minY;
      this.compMaxX = this.maxX;
      this.compMaxY = this.maxY;
      if (debug)
        System.out.println("XXmain=" + this.minX + ' ' + this.minY + ' ' + this.maxX + ' ' + this.maxY);
      readComplexGlyph(paramGlyf, paramFontFile2);
    }
  }

  public boolean isTTHintingRequired()
  {
    return this.ttHintingRequired;
  }

  public void createPaths(int[] paramArrayOfInt1, int[] paramArrayOfInt2, boolean[] paramArrayOfBoolean1, boolean[] paramArrayOfBoolean2, int paramInt)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  void clearPaths()
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void render(int paramInt, Graphics2D paramGraphics2D, float paramFloat, boolean paramBoolean)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void setWidth(float paramFloat)
  {
  }

  public int getFontBB(int paramInt)
  {
    if (this.isComposite)
    {
      if (paramInt == 1)
        return this.compMinX;
      if (paramInt == 2)
        return this.compMinY;
      if (paramInt == 3)
        return this.compMaxX;
      if (paramInt == 4)
        return this.compMaxY;
      return 0;
    }
    if (paramInt == 1)
      return this.minX;
    if (paramInt == 2)
      return this.minY;
    if (paramInt == 3)
      return this.maxX;
    if (paramInt == 4)
      return this.maxY;
    return 0;
  }

  public void setStrokedOnly(boolean paramBoolean)
  {
  }

  public boolean containsBrokenData()
  {
    return this.containsBrokenGlyfData;
  }

  public Path getPath()
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public int getID()
  {
    return this.id;
  }

  public void setID(int paramInt)
  {
    this.id = paramInt;
  }

  public int getGlyphNumber()
  {
    return this.glyphNumber;
  }

  public void setGlyphNumber(int paramInt)
  {
    this.glyphNumber = paramInt;
  }

  public Area getShape()
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  void createHintedGlyph()
  {
    for (int i = 0; i < this.compCount; i++)
    {
      arrayOfInt1 = (int[])this.glyfX.elementAt(i);
      int[] arrayOfInt2 = (int[])this.glyfY.elementAt(i);
      if (this.isComposite)
      {
        this.xtranslate = this.xtranslateValues.elementAt(i);
        this.ytranslate = this.ytranslateValues.elementAt(i);
        this.xscale = this.xscaleValues.elementAt(i);
        this.yscale = this.yscaleValues.elementAt(i);
        this.scale01 = this.scale01Values.elementAt(i);
        this.scale10 = this.scale10Values.elementAt(i);
        if ((this.BPoint1 != -1) && (this.BPoint2 != -1))
          if ((this.BP1x == -1) && (this.BP2x == -1) && (this.BP1y == -1) && (this.BP2y == -1))
          {
            this.BP1x = arrayOfInt1[this.BPoint1];
            this.BP1y = arrayOfInt2[this.BPoint1];
          }
          else
          {
            this.BP2x = arrayOfInt1[this.BPoint2];
            this.BP2y = arrayOfInt2[this.BPoint2];
            int k = this.BP1x - this.BP2x;
            int m = this.BP1y - this.BP2y;
            int n = arrayOfInt1.length;
            for (int i1 = 0; i1 < n; i1++)
            {
              arrayOfInt1[i1] += k;
              if (debug)
                System.out.println(arrayOfInt2[i1] + " " + m + " BP1y=" + this.BP1y + " BP1y=" + this.BP1y);
              arrayOfInt2[i1] += m;
            }
            this.BP1x = -1;
            this.BP2x = -1;
            this.BP1y = -1;
            this.BP2y = -1;
          }
      }
    }
    this.pixelSize = 1562.5D;
    i = 2;
    int[] arrayOfInt1 = new int[this.compCount];
    for (int j = 0; j < this.compCount; j++)
    {
      i += this.endPtIndices.elementAt(j);
      arrayOfInt1[j] = this.endPtIndices.elementAt(j);
    }
    int[] arrayOfInt3 = new int[i];
    int[] arrayOfInt4 = new int[i];
    int[] arrayOfInt5 = new int[i];
    int[] arrayOfInt6 = new int[i];
    boolean[] arrayOfBoolean1 = new boolean[i];
    boolean[] arrayOfBoolean2 = new boolean[i];
    int[] arrayOfInt7 = new int[this.compCount];
    int i2 = 0;
    Object localObject1;
    Object localObject2;
    for (int i3 = 0; i3 < this.compCount; i3++)
    {
      int[] arrayOfInt8 = (int[])this.glyfX.elementAt(i3);
      int[] arrayOfInt9 = (int[])this.glyfY.elementAt(i3);
      localObject1 = (boolean[])this.curves.elementAt(i3);
      localObject2 = (boolean[])this.contours.elementAt(i3);
      int i5 = this.endPtIndices.elementAt(i3);
      if (arrayOfInt8 != null)
        scaler(arrayOfInt8, arrayOfInt9);
      for (int i6 = 0; i6 < arrayOfInt1[i3]; i6++)
      {
        arrayOfInt3[(i2 + i6)] = this.scaledX[i6];
        arrayOfInt4[(i2 + i6)] = this.scaledY[i6];
        arrayOfInt5[(i2 + i6)] = arrayOfInt8[i6];
        arrayOfInt6[(i2 + i6)] = arrayOfInt9[i6];
        arrayOfBoolean1[(i2 + i6)] = localObject1[i6];
        arrayOfBoolean2[(i2 + i6)] = localObject2[i6];
        arrayOfInt7[i3] = i5;
      }
      i2 += arrayOfInt1[i3];
    }
    double d = this.pixelSize / (this.unitsPerEm * 1000.0F) * 64.0D;
    arrayOfInt3[(arrayOfInt3.length - 1)] = ((int)(d * this.leftSideBearing + 0.5D));
    this.vm.setScaleVars(d, this.pixelSize, this.pixelSize * 72.0D / 96.0D);
    this.vm.processGlyph(this.instructions, arrayOfInt3, arrayOfInt4, arrayOfBoolean1, arrayOfBoolean2);
    clearPaths();
    i2 = 0;
    for (int i4 = 0; i4 < this.compCount; i4++)
    {
      localObject1 = (int[])this.glyfX.elementAt(i4);
      localObject2 = (int[])this.glyfY.elementAt(i4);
      boolean[] arrayOfBoolean3 = (boolean[])this.curves.elementAt(i4);
      boolean[] arrayOfBoolean4 = (boolean[])this.contours.elementAt(i4);
      int i7 = arrayOfInt7[i4];
      for (int i8 = 0; i8 < arrayOfInt1[i4]; i8++)
      {
        localObject1[i8] = arrayOfInt3[(i2 + i8)];
        localObject2[i8] = arrayOfInt4[(i2 + i8)];
        arrayOfBoolean3[i8] = arrayOfBoolean1[(i2 + i8)];
        arrayOfBoolean4[i8] = arrayOfBoolean2[(i2 + i8)];
      }
      createPaths((int[])localObject1, (int[])localObject2, arrayOfBoolean3, arrayOfBoolean4, i7);
      i2 += arrayOfInt1[i4];
    }
  }

  static
  {
    String str = System.getProperty("org.jpedal.useTTFontHinting");
    if (str != null)
      useHinting = str.toLowerCase().equals("true");
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.fonts.tt.BaseTTGlyph
 * JD-Core Version:    0.6.2
 */