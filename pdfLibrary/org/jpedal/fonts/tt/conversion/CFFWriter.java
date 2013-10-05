package org.jpedal.fonts.tt.conversion;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.jpedal.fonts.StandardFonts;
import org.jpedal.fonts.Type1;
import org.jpedal.fonts.Type1C;
import org.jpedal.fonts.glyph.PdfJavaGlyphs;
import org.jpedal.utils.LogWriter;

public class CFFWriter extends Type1
  implements FontTableWriter
{
  private static final boolean debugTopDictOffsets = false;
  private String name;
  private byte[][] subrs;
  private final String[] glyphNames;
  private byte[][] charstrings;
  private int[] charstringXDisplacement;
  private int[] charstringYDisplacement;
  private byte[] header;
  private byte[] nameIndex;
  private byte[] topDictIndex;
  private byte[] globalSubrIndex;
  private byte[] encodings;
  private byte[] charsets;
  private byte[] charStringsIndex;
  private byte[] privateDict;
  private byte[] localSubrIndex;
  private byte[] stringIndex;
  private final ArrayList<String> strings = new ArrayList();
  private int[] widthX;
  private int[] widthY;
  private int[] lsbX;
  private int[] lsbY;
  private int defaultWidthX;
  private int nominalWidthX;
  private ArrayList<CharstringElement> currentCharString;
  private int currentCharStringID;
  private float[] bbox = new float[4];
  private boolean inFlex = false;
  private CharstringElement currentFlexCommand;
  private boolean firstArgsAdded = false;
  private double emSquareSize = 1000.0D;
  private double scale = 1.0D;
  private boolean inSeac = false;

  public CFFWriter(PdfJavaGlyphs paramPdfJavaGlyphs, String paramString)
  {
    this.glyphs = paramPdfJavaGlyphs;
    this.name = paramString;
    Map localMap = paramPdfJavaGlyphs.getCharStrings();
    Object[] arrayOfObject = localMap.keySet().toArray();
    Arrays.sort(arrayOfObject);
    int i = 0;
    int j = 0;
    int k = 0;
    String str;
    for (int m = 0; m < localMap.size(); m++)
    {
      str = (String)arrayOfObject[m];
      if (str.startsWith("subrs"))
      {
        int n = Integer.parseInt(str.replaceAll("[^0-9]", ""));
        if (n > i)
          i = n;
        int i1 = ((byte[])localMap.get(str)).length;
        if (i1 > j)
          j = i1;
      }
      else
      {
        k++;
      }
    }
    this.subrs = new byte[i + 1][];
    this.glyphNames = new String[k];
    this.charstrings = new byte[k][];
    this.charstringXDisplacement = new int[k];
    this.charstringYDisplacement = new int[k];
    k = 0;
    for (m = 0; m < localMap.size(); m++)
    {
      str = (String)arrayOfObject[m];
      Object localObject = localMap.get(str);
      byte[] arrayOfByte = (byte[])localObject;
      if (str.startsWith("subrs"))
      {
        int i2 = Integer.parseInt(str.replaceAll("[^0-9]", ""));
        this.subrs[i2] = arrayOfByte;
      }
      else
      {
        this.glyphNames[k] = str;
        this.charstrings[k] = arrayOfByte;
        k++;
      }
    }
    convertCharstrings();
  }

  private void convertCharstrings()
  {
    try
    {
      this.widthX = new int[this.charstrings.length];
      this.widthY = new int[this.charstrings.length];
      this.lsbX = new int[this.charstrings.length];
      this.lsbY = new int[this.charstrings.length];
      byte[][] arrayOfByte = new byte[this.charstrings.length][];
      for (i = 0; i < this.charstrings.length; i++)
        arrayOfByte[i] = convertCharstring(this.charstrings[i], i);
      if (this.bbox[2] - this.bbox[0] > 1100.0F)
      {
        this.emSquareSize = (this.bbox[2] - this.bbox[0]);
        this.scale = (1.0D / (this.emSquareSize / 1000.0D));
        this.charstringXDisplacement = new int[this.charstringXDisplacement.length];
        this.charstringYDisplacement = new int[this.charstringYDisplacement.length];
        this.bbox = new float[4];
        for (i = 0; i < this.charstrings.length; i++)
          arrayOfByte[i] = convertCharstring(this.charstrings[i], i);
        this.charstrings = arrayOfByte;
      }
      else
      {
        this.charstrings = arrayOfByte;
      }
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception: " + localException.getMessage());
    }
    HashMap localHashMap = new HashMap();
    for (int i = 0; i < this.charstrings.length; i++)
    {
      Integer localInteger = (Integer)localHashMap.get(Integer.valueOf(this.widthX[i]));
      if (localInteger == null)
        localInteger = Integer.valueOf(1);
      else
        localInteger = Integer.valueOf(localInteger.intValue() + 1);
      localHashMap.put(Integer.valueOf(this.widthX[i]), localInteger);
    }
    Object[] arrayOfObject1 = localHashMap.keySet().toArray();
    int j = 0;
    this.defaultWidthX = 0;
    int i3;
    for (Object localObject1 : arrayOfObject1)
    {
      i3 = ((Integer)localHashMap.get(localObject1)).intValue();
      if (i3 > j)
      {
        j = i3;
        this.defaultWidthX = ((Integer)localObject1).intValue();
      }
    }
    int k = 0;
    ??? = 0;
    for (Object localObject2 : arrayOfObject1)
      if (((Integer)localObject2).intValue() != this.defaultWidthX)
      {
        ???++;
        k += ((Integer)localObject2).intValue();
      }
    if (??? != 0)
      this.nominalWidthX = (k / ???);
    else
      this.nominalWidthX = 0;
    for (int i1 = 0; i1 < this.widthX.length; i1++)
      if (this.widthX[i1] == this.defaultWidthX)
        this.widthX[i1] = -2147483648;
      else
        this.widthX[i1] -= this.nominalWidthX;
    byte[] arrayOfByte1;
    for (i1 = 0; i1 < this.widthX.length; i1++)
      if (this.widthX[i1] != -2147483648)
      {
        arrayOfByte1 = CFFUtils.storeCharstringType2Integer(this.widthX[i1]);
        byte[] arrayOfByte2 = new byte[arrayOfByte1.length + this.charstrings[i1].length];
        System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, arrayOfByte1.length);
        System.arraycopy(this.charstrings[i1], 0, arrayOfByte2, arrayOfByte1.length, this.charstrings[i1].length);
        this.charstrings[i1] = arrayOfByte2;
      }
    for (i1 = 0; i1 < this.charstrings.length; i1++)
      if (this.charstrings[i1][(this.charstrings[i1].length - 1)] != 14)
      {
        arrayOfByte1 = new byte[this.charstrings[i1].length + 1];
        System.arraycopy(this.charstrings[i1], 0, arrayOfByte1, 0, this.charstrings[i1].length);
        arrayOfByte1[(arrayOfByte1.length - 1)] = 14;
        this.charstrings[i1] = arrayOfByte1;
      }
  }

  private byte[] convertCharstring(byte[] paramArrayOfByte, int paramInt)
  {
    int[] arrayOfInt1 = new int[paramArrayOfByte.length];
    for (int i = 0; i < paramArrayOfByte.length; i++)
    {
      arrayOfInt1[i] = paramArrayOfByte[i];
      if (arrayOfInt1[i] < 0)
        arrayOfInt1[i] += 256;
    }
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    this.currentCharString = new ArrayList();
    this.currentCharStringID = paramInt;
    int j = 0;
    while (j < arrayOfInt1.length)
    {
      CharstringElement localCharstringElement1 = new CharstringElement(arrayOfInt1, j);
      j += localCharstringElement1.getLength();
    }
    CharstringElement localCharstringElement2;
    if ((this.emSquareSize != 1000.0D) && (!this.inSeac))
    {
      localIterator = this.currentCharString.iterator();
      while (localIterator.hasNext())
      {
        localCharstringElement2 = (CharstringElement)localIterator.next();
        localCharstringElement2.scale();
      }
      this.widthX[paramInt] = ((int)(this.scale * this.widthX[paramInt]));
      this.widthY[paramInt] = ((int)(this.scale * this.widthY[paramInt]));
      this.lsbX[paramInt] = ((int)(this.scale * this.lsbX[paramInt]));
      this.lsbY[paramInt] = ((int)(this.scale * this.lsbY[paramInt]));
    }
    Iterator localIterator = this.currentCharString.iterator();
    while (localIterator.hasNext())
    {
      localCharstringElement2 = (CharstringElement)localIterator.next();
      int[] arrayOfInt2 = localCharstringElement2.getDisplacement();
      this.charstringXDisplacement[paramInt] += arrayOfInt2[0];
      this.charstringYDisplacement[paramInt] += arrayOfInt2[1];
      this.bbox[0] = (this.charstringXDisplacement[paramInt] < this.bbox[0] ? this.charstringXDisplacement[paramInt] : this.bbox[0]);
      this.bbox[1] = (this.charstringYDisplacement[paramInt] < this.bbox[1] ? this.charstringYDisplacement[paramInt] : this.bbox[1]);
      this.bbox[2] = (this.charstringXDisplacement[paramInt] > this.bbox[2] ? this.charstringXDisplacement[paramInt] : this.bbox[2]);
      this.bbox[3] = (this.charstringYDisplacement[paramInt] > this.bbox[3] ? this.charstringYDisplacement[paramInt] : this.bbox[3]);
    }
    try
    {
      localIterator = this.currentCharString.iterator();
      while (localIterator.hasNext())
      {
        localCharstringElement2 = (CharstringElement)localIterator.next();
        localByteArrayOutputStream.write(localCharstringElement2.getType2Bytes());
      }
    }
    catch (IOException localIOException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception: " + localIOException.getMessage());
    }
    return localByteArrayOutputStream.toByteArray();
  }

  public int getSIDForString(String paramString)
  {
    for (int i = 0; i < Type1C.type1CStdStrings.length; i++)
      if (paramString.equals(Type1C.type1CStdStrings[i]))
        return i;
    for (i = 0; i < this.strings.size(); i++)
      if (paramString.equals(this.strings.get(i)))
        return 391 + i;
    this.strings.add(paramString);
    return 390 + this.strings.size();
  }

  public byte[] writeTable()
    throws IOException
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    this.topDictIndex = (this.globalSubrIndex = this.stringIndex = this.encodings = this.charsets = this.charStringsIndex = this.privateDict = this.localSubrIndex = new byte[0]);
    this.header = new byte[] { TTFontWriter.setNextUint8(1), TTFontWriter.setNextUint8(0), TTFontWriter.setNextUint8(4), TTFontWriter.setNextUint8(2) };
    this.nameIndex = CFFUtils.createIndex(new byte[][] { this.name.getBytes() });
    this.topDictIndex = CFFUtils.createIndex(new byte[][] { createTopDict() });
    this.globalSubrIndex = CFFUtils.createIndex(new byte[0][]);
    this.encodings = createEncodings();
    this.charsets = createCharsets();
    this.charStringsIndex = CFFUtils.createIndex(this.charstrings);
    this.privateDict = createPrivateDict();
    this.stringIndex = CFFUtils.createIndex(createStrings());
    byte[] arrayOfByte1;
    do
    {
      arrayOfByte1 = new byte[this.privateDict.length];
      System.arraycopy(this.privateDict, 0, arrayOfByte1, 0, this.privateDict.length);
      this.privateDict = createPrivateDict();
    }
    while (!Arrays.equals(this.privateDict, arrayOfByte1));
    byte[] arrayOfByte2;
    do
    {
      arrayOfByte2 = new byte[this.topDictIndex.length];
      System.arraycopy(this.topDictIndex, 0, arrayOfByte2, 0, this.topDictIndex.length);
      this.topDictIndex = CFFUtils.createIndex(new byte[][] { createTopDict() });
    }
    while (!Arrays.equals(arrayOfByte2, this.topDictIndex));
    localByteArrayOutputStream.write(this.header);
    localByteArrayOutputStream.write(this.nameIndex);
    localByteArrayOutputStream.write(this.topDictIndex);
    localByteArrayOutputStream.write(this.stringIndex);
    localByteArrayOutputStream.write(this.globalSubrIndex);
    localByteArrayOutputStream.write(this.encodings);
    localByteArrayOutputStream.write(this.charsets);
    localByteArrayOutputStream.write(this.charStringsIndex);
    localByteArrayOutputStream.write(this.privateDict);
    return localByteArrayOutputStream.toByteArray();
  }

  private byte[] createTopDict()
    throws IOException
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    localByteArrayOutputStream.write(CFFUtils.storeInteger(getSIDForString("1")));
    localByteArrayOutputStream.write(0);
    if (this.copyright != null)
    {
      localByteArrayOutputStream.write(CFFUtils.storeInteger(getSIDForString(this.copyright)));
      localByteArrayOutputStream.write(1);
    }
    localByteArrayOutputStream.write(CFFUtils.storeInteger((int)this.bbox[0]));
    localByteArrayOutputStream.write(CFFUtils.storeInteger((int)this.bbox[1]));
    localByteArrayOutputStream.write(CFFUtils.storeInteger((int)this.bbox[2]));
    localByteArrayOutputStream.write(CFFUtils.storeInteger((int)this.bbox[3]));
    localByteArrayOutputStream.write(5);
    int i = this.header.length + this.nameIndex.length + this.topDictIndex.length + this.stringIndex.length + this.globalSubrIndex.length;
    if (this.encodings.length != 0)
    {
      localByteArrayOutputStream.write(CFFUtils.storeInteger(i));
      localByteArrayOutputStream.write(16);
    }
    i += this.encodings.length;
    localByteArrayOutputStream.write(CFFUtils.storeInteger(i));
    localByteArrayOutputStream.write(15);
    i += this.charsets.length;
    localByteArrayOutputStream.write(CFFUtils.storeInteger(i));
    localByteArrayOutputStream.write(17);
    i += this.charStringsIndex.length;
    localByteArrayOutputStream.write(CFFUtils.storeInteger(this.privateDict.length));
    localByteArrayOutputStream.write(CFFUtils.storeInteger(i));
    localByteArrayOutputStream.write(18);
    return localByteArrayOutputStream.toByteArray();
  }

  private byte[][] createStrings()
  {
    byte[][] arrayOfByte = new byte[this.strings.size()][];
    for (int i = 0; i < this.strings.size(); i++)
      arrayOfByte[i] = ((String)this.strings.get(i)).getBytes();
    return arrayOfByte;
  }

  private byte[] createCharsets()
  {
    String[] arrayOfString = null;
    for (int i = 0; i < this.glyphNames.length; i++)
      if (".notdef".equals(this.glyphNames[i]))
      {
        arrayOfString = new String[this.glyphNames.length - 1];
        System.arraycopy(this.glyphNames, 0, arrayOfString, 0, i);
        System.arraycopy(this.glyphNames, i + 1, arrayOfString, i, arrayOfString.length - i);
      }
    if (arrayOfString == null)
      arrayOfString = this.glyphNames;
    byte[] arrayOfByte1 = new byte[arrayOfString.length * 2 + 1];
    for (int j = 0; j < arrayOfString.length; j++)
    {
      byte[] arrayOfByte2 = FontWriter.setUintAsBytes(getSIDForString(arrayOfString[j]), 2);
      arrayOfByte1[(1 + j * 2)] = arrayOfByte2[0];
      arrayOfByte1[(2 + j * 2)] = arrayOfByte2[1];
    }
    return arrayOfByte1;
  }

  private static byte[] createEncodings()
  {
    return new byte[0];
  }

  private byte[] createPrivateDict()
    throws IOException
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    localByteArrayOutputStream.write(CFFUtils.storeInteger(this.defaultWidthX));
    localByteArrayOutputStream.write(20);
    localByteArrayOutputStream.write(CFFUtils.storeInteger(this.nominalWidthX));
    localByteArrayOutputStream.write(21);
    return localByteArrayOutputStream.toByteArray();
  }

  public int getIntValue(int paramInt)
  {
    return -1;
  }

  public String[] getGlyphList()
  {
    return this.glyphNames;
  }

  public int[] getWidths()
  {
    int[] arrayOfInt = new int[this.widthX.length];
    for (int i = 0; i < this.widthX.length; i++)
      if (this.widthX[i] == -2147483648)
        arrayOfInt[i] = this.defaultWidthX;
      else
        arrayOfInt[i] = (this.widthX[i] + this.nominalWidthX);
    return arrayOfInt;
  }

  public int[] getBearings()
  {
    return this.lsbX;
  }

  public float[] getBBox()
  {
    return this.bbox;
  }

  public double getEmSquareSize()
  {
    return this.emSquareSize;
  }

  private class CharstringElement
  {
    private boolean isCommand = true;
    private String commandName;
    private int numberValue;
    private int length = 1;
    private ArrayList<CharstringElement> args = new ArrayList();
    private final boolean isResult;
    private CharstringElement parent;

    public CharstringElement(int arg2)
    {
      this.isResult = false;
      this.isCommand = false;
      int i;
      this.numberValue = i;
    }

    public CharstringElement(CharstringElement arg2)
    {
      this.isResult = true;
      this.isCommand = false;
      Object localObject;
      this.parent = localObject;
      CFFWriter.this.currentCharString.add(this);
    }

    public CharstringElement(int[] paramInt, int arg3)
    {
      this.isResult = false;
      CFFWriter.this.currentCharString.add(this);
      int i;
      int j = paramInt[i];
      if ((j >= 32) && (j <= 246))
      {
        this.numberValue = (j - 139);
        this.isCommand = false;
      }
      else if (((j >= 247) && (j <= 250)) || ((j >= 251) && (j <= 254)))
      {
        if (j < 251)
          this.numberValue = ((j - 247) * 256 + paramInt[(i + 1)] + 108);
        else
          this.numberValue = (-((j - 251) * 256) - paramInt[(i + 1)] - 108);
        this.isCommand = false;
        this.length = 2;
      }
      else
      {
        int k = 0;
        int m;
        int i1;
        int i2;
        switch (j)
        {
        case 1:
          this.commandName = "hstem";
          claimArguments(2, true, true);
          break;
        case 3:
          this.commandName = "vstem";
          claimArguments(2, true, true);
          break;
        case 4:
          this.commandName = "vmoveto";
          claimArguments(1, true, true);
          if (CFFWriter.this.inFlex)
          {
            if ((CFFWriter.this.currentFlexCommand.args.size() == 2) && (!CFFWriter.this.firstArgsAdded))
            {
              m = ((CharstringElement)CFFWriter.this.currentFlexCommand.args.get(0)).numberValue;
              int n = ((CharstringElement)this.args.get(0)).numberValue + ((CharstringElement)CFFWriter.this.currentFlexCommand.args.get(1)).numberValue;
              CFFWriter.this.currentFlexCommand.args.clear();
              CFFWriter.this.currentFlexCommand.args.add(new CharstringElement(CFFWriter.this, m));
              CFFWriter.this.currentFlexCommand.args.add(new CharstringElement(CFFWriter.this, n));
              CFFWriter.this.firstArgsAdded = true;
            }
            else
            {
              CFFWriter.this.currentFlexCommand.args.add(new CharstringElement(CFFWriter.this, 0));
              CFFWriter.this.currentFlexCommand.args.add(this.args.get(0));
            }
            this.commandName = "";
          }
          break;
        case 5:
          this.commandName = "rlineto";
          claimArguments(2, true, true);
          k = 1;
          break;
        case 6:
          this.commandName = "hlineto";
          claimArguments(1, true, true);
          break;
        case 7:
          this.commandName = "vlineto";
          claimArguments(1, true, true);
          break;
        case 8:
          this.commandName = "rrcurveto";
          claimArguments(6, true, true);
          k = 1;
          break;
        case 9:
          this.commandName = "closepath";
          claimArguments(0, false, true);
          break;
        case 10:
          this.commandName = "callsubr";
          claimArguments(1, false, false);
          m = ((CharstringElement)this.args.get(0)).numberValue;
          if ((!CFFWriter.this.inFlex) && (m == 1))
          {
            this.args.clear();
            this.commandName = "flex";
            CFFWriter.this.currentFlexCommand = this;
            CFFWriter.this.inFlex = true;
          }
          if ((CFFWriter.this.inFlex) && (m >= 0) && (m <= 2))
          {
            if (m == 0)
            {
              claimArguments(3, false, false);
              if (this.args.size() >= 4)
                CFFWriter.this.currentFlexCommand.args.add(this.args.get(3));
              else
                CFFWriter.this.currentFlexCommand.args.add(new CharstringElement(CFFWriter.this, 0));
              CFFWriter.this.inFlex = false;
              CFFWriter.this.firstArgsAdded = false;
            }
          }
          else
          {
            byte[] arrayOfByte = CFFWriter.this.subrs[m];
            int[] arrayOfInt = new int[arrayOfByte.length];
            for (int i3 = 0; i3 < arrayOfByte.length; i3++)
            {
              arrayOfInt[i3] = arrayOfByte[i3];
              if (arrayOfInt[i3] < 0)
                arrayOfInt[i3] += 256;
            }
            int i5 = 0;
            while (i5 < arrayOfInt.length)
            {
              CharstringElement localCharstringElement3 = new CharstringElement(CFFWriter.this, arrayOfInt, i5);
              i5 += localCharstringElement3.length;
            }
          }
          break;
        case 11:
          this.commandName = "return";
          break;
        case 12:
          this.length = 2;
          switch (paramInt[(i + 1)])
          {
          case 0:
            this.commandName = "dotsection";
            claimArguments(0, false, true);
            break;
          case 1:
            this.commandName = "vstem3";
            claimArguments(6, true, true);
            break;
          case 2:
            this.commandName = "hstem3";
            claimArguments(6, true, true);
            break;
          case 6:
            this.commandName = "seac";
            claimArguments(5, true, true);
            break;
          case 7:
            this.commandName = "sbw";
            claimArguments(4, true, true);
            CFFWriter.this.lsbX[CFFWriter.this.currentCharStringID] = ((CharstringElement)this.args.get(0)).evaluate();
            CFFWriter.this.lsbY[CFFWriter.this.currentCharStringID] = ((CharstringElement)this.args.get(1)).evaluate();
            CFFWriter.this.widthX[CFFWriter.this.currentCharStringID] = ((CharstringElement)this.args.get(2)).evaluate();
            CFFWriter.this.widthY[CFFWriter.this.currentCharStringID] = ((CharstringElement)this.args.get(3)).evaluate();
            if (CFFWriter.this.lsbX[CFFWriter.this.currentCharStringID] != 0)
            {
              this.commandName = "rmoveto";
              this.args.clear();
              this.args.add(new CharstringElement(CFFWriter.this, CFFWriter.this.lsbX[CFFWriter.this.currentCharStringID]));
              this.args.add(new CharstringElement(CFFWriter.this, CFFWriter.this.lsbY[CFFWriter.this.currentCharStringID]));
            }
            break;
          case 12:
            this.commandName = "div";
            claimArguments(2, false, false);
            new CharstringElement(CFFWriter.this, this);
            break;
          case 16:
            this.commandName = "callothersubr";
            claimArguments(2, false, false);
            if (this.args.size() > 1)
            {
              i1 = ((CharstringElement)this.args.get(1)).numberValue;
              boolean bool = claimArguments(i1, false, false);
              if (!bool)
              {
                CFFWriter.this.currentCharString.remove(this);
                return;
              }
              for (int i4 = 0; i4 < i1; i4++)
                new CharstringElement(CFFWriter.this, ((CharstringElement)this.args.get(1 + i1 - i4)).numberValue);
            }
            break;
          case 17:
            this.commandName = "pop";
            new CharstringElement(CFFWriter.this, this);
            break;
          case 33:
            this.commandName = "setcurrentpoint";
            claimArguments(2, true, true);
          case 3:
          case 4:
          case 5:
          case 8:
          case 9:
          case 10:
          case 11:
          case 13:
          case 14:
          case 15:
          case 18:
          case 19:
          case 20:
          case 21:
          case 22:
          case 23:
          case 24:
          case 25:
          case 26:
          case 27:
          case 28:
          case 29:
          case 30:
          case 31:
          case 32:
          }
          break;
        case 13:
          this.commandName = "hsbw";
          claimArguments(2, true, true);
          CFFWriter.this.lsbX[CFFWriter.this.currentCharStringID] = ((CharstringElement)this.args.get(0)).evaluate();
          CFFWriter.this.widthX[CFFWriter.this.currentCharStringID] = ((CharstringElement)this.args.get(1)).evaluate();
          if (CFFWriter.this.lsbX[CFFWriter.this.currentCharStringID] != 0)
          {
            this.commandName = "rmoveto";
            this.args.set(1, new CharstringElement(CFFWriter.this, 0));
          }
          break;
        case 14:
          this.commandName = "endchar";
          claimArguments(0, false, true);
          break;
        case 21:
          this.commandName = "rmoveto";
          claimArguments(2, true, true);
          if (CFFWriter.this.inFlex)
          {
            if ((CFFWriter.this.currentFlexCommand.args.size() == 2) && (!CFFWriter.this.firstArgsAdded))
            {
              i1 = ((CharstringElement)this.args.get(0)).numberValue + ((CharstringElement)CFFWriter.this.currentFlexCommand.args.get(0)).numberValue;
              i2 = ((CharstringElement)this.args.get(1)).numberValue + ((CharstringElement)CFFWriter.this.currentFlexCommand.args.get(1)).numberValue;
              CFFWriter.this.currentFlexCommand.args.clear();
              CFFWriter.this.currentFlexCommand.args.add(new CharstringElement(CFFWriter.this, i1));
              CFFWriter.this.currentFlexCommand.args.add(new CharstringElement(CFFWriter.this, i2));
              CFFWriter.this.firstArgsAdded = true;
            }
            else
            {
              CFFWriter.this.currentFlexCommand.args.add(this.args.get(0));
              CFFWriter.this.currentFlexCommand.args.add(this.args.get(1));
            }
            this.commandName = "";
          }
          break;
        case 22:
          this.commandName = "hmoveto";
          claimArguments(1, true, true);
          if (CFFWriter.this.inFlex)
          {
            if ((CFFWriter.this.currentFlexCommand.args.size() == 2) && (!CFFWriter.this.firstArgsAdded))
            {
              i1 = ((CharstringElement)this.args.get(0)).numberValue + ((CharstringElement)CFFWriter.this.currentFlexCommand.args.get(0)).numberValue;
              i2 = ((CharstringElement)CFFWriter.this.currentFlexCommand.args.get(1)).numberValue;
              CFFWriter.this.currentFlexCommand.args.clear();
              CFFWriter.this.currentFlexCommand.args.add(new CharstringElement(CFFWriter.this, i1));
              CFFWriter.this.currentFlexCommand.args.add(new CharstringElement(CFFWriter.this, i2));
              CFFWriter.this.firstArgsAdded = true;
            }
            else
            {
              CFFWriter.this.currentFlexCommand.args.add(this.args.get(0));
              CFFWriter.this.currentFlexCommand.args.add(new CharstringElement(CFFWriter.this, 0));
            }
            this.commandName = "";
          }
          break;
        case 30:
          this.commandName = "vhcurveto";
          claimArguments(4, true, true);
          break;
        case 31:
          this.commandName = "hvcurveto";
          claimArguments(4, true, true);
          break;
        case 255:
          this.length = 5;
          this.isCommand = false;
          this.numberValue = ((paramInt[(i + 4)] & 0xFF) + ((paramInt[(i + 3)] & 0xFF) << 8) + ((paramInt[(i + 2)] & 0xFF) << 16) + ((paramInt[(i + 1)] & 0xFF) << 24));
          break;
        }
        if (k != 0)
        {
          CharstringElement localCharstringElement1 = (CharstringElement)CFFWriter.this.currentCharString.get(CFFWriter.this.currentCharString.indexOf(this) - 1);
          if ((this.commandName.equals(localCharstringElement1.commandName)) && (localCharstringElement1.args.size() <= 39 - this.args.size()))
          {
            CFFWriter.this.currentCharString.remove(localCharstringElement1);
            Iterator localIterator = this.args.iterator();
            while (localIterator.hasNext())
            {
              CharstringElement localCharstringElement2 = (CharstringElement)localIterator.next();
              localCharstringElement1.args.add(localCharstringElement2);
            }
            this.args = localCharstringElement1.args;
          }
        }
      }
    }

    private int evaluate()
    {
      if (this.isResult)
        return this.parent.evaluate();
      if ((this.isCommand) && ("div".equals(this.commandName)))
        return ((CharstringElement)this.args.get(1)).evaluate() / ((CharstringElement)this.args.get(0)).evaluate();
      return this.numberValue;
    }

    public int getLength()
    {
      return this.length;
    }

    public int[] getDisplacement()
    {
      if (!this.isCommand)
        return new int[] { 0, 0 };
      if ((!"hstem".equals(this.commandName)) && (!"vstem".equals(this.commandName)))
      {
        if ("vmoveto".equals(this.commandName))
          return new int[] { 0, ((CharstringElement)this.args.get(0)).evaluate() };
        int i;
        int j;
        int k;
        if ("rlineto".equals(this.commandName))
        {
          i = 0;
          j = 0;
          for (k = 0; k < this.args.size() / 2; k++)
          {
            i += ((CharstringElement)this.args.get(k * 2)).evaluate();
            j += ((CharstringElement)this.args.get(1 + k * 2)).evaluate();
          }
          return new int[] { i, j };
        }
        if ("hlineto".equals(this.commandName))
          return new int[] { ((CharstringElement)this.args.get(0)).evaluate(), 0 };
        if ("vlineto".equals(this.commandName))
          return new int[] { 0, ((CharstringElement)this.args.get(0)).evaluate() };
        if ("rrcurveto".equals(this.commandName))
        {
          i = 0;
          j = 0;
          for (k = 0; k < this.args.size() / 2; k++)
          {
            i += ((CharstringElement)this.args.get(k * 2)).evaluate();
            j += ((CharstringElement)this.args.get(1 + k * 2)).evaluate();
          }
          return new int[] { i, j };
        }
        if ((!"closepath".equals(this.commandName)) && (!"callsubr".equals(this.commandName)) && (!"return".equals(this.commandName)) && (!"dotsection".equals(this.commandName)) && (!"vstem3".equals(this.commandName)) && (!"hstem3".equals(this.commandName)) && (!"seac".equals(this.commandName)) && (!"sbw".equals(this.commandName)) && (!"div".equals(this.commandName)) && (!"callothersubr".equals(this.commandName)) && (!"pop".equals(this.commandName)) && (!"setcurrentpoint".equals(this.commandName)) && (!"hsbw".equals(this.commandName)) && (!"endchar".equals(this.commandName)))
        {
          if ("rmoveto".equals(this.commandName))
            return new int[] { ((CharstringElement)this.args.get(0)).evaluate(), ((CharstringElement)this.args.get(1)).evaluate() };
          if ("hmoveto".equals(this.commandName))
            return new int[] { ((CharstringElement)this.args.get(0)).evaluate(), 0 };
          if ("vhcurveto".equals(this.commandName))
            return new int[] { ((CharstringElement)this.args.get(1)).evaluate() + ((CharstringElement)this.args.get(3)).evaluate(), ((CharstringElement)this.args.get(0)).evaluate() + ((CharstringElement)this.args.get(2)).evaluate() };
          if ("hvcurveto".equals(this.commandName))
            return new int[] { ((CharstringElement)this.args.get(0)).evaluate() + ((CharstringElement)this.args.get(1)).evaluate(), ((CharstringElement)this.args.get(2)).evaluate() + ((CharstringElement)this.args.get(3)).evaluate() };
          if ("flex".equals(this.commandName))
          {
            i = 0;
            j = 0;
            for (k = 0; k < 6; k++)
            {
              i += ((CharstringElement)this.args.get(k * 2)).evaluate();
              j += ((CharstringElement)this.args.get(1 + k * 2)).evaluate();
            }
            return new int[] { i, j };
          }
          if (this.commandName.isEmpty())
            return new int[] { 0, 0 };
        }
      }
      return new int[] { 0, 0 };
    }

    public void scale()
    {
      if (this.isResult)
        return;
      if (!this.isCommand)
      {
        this.numberValue = ((int)(this.numberValue * CFFWriter.this.scale));
        return;
      }
      int i = 0;
      if ("hstem".equals(this.commandName))
        i = 1;
      else if ("vstem".equals(this.commandName))
        i = 1;
      else if ("vmoveto".equals(this.commandName))
        i = 1;
      else if ("rlineto".equals(this.commandName))
        i = 1;
      else if ("hlineto".equals(this.commandName))
        i = 1;
      else if ("vlineto".equals(this.commandName))
        i = 1;
      else if ("rrcurveto".equals(this.commandName))
        i = 1;
      else if ((!"closepath".equals(this.commandName)) && (!"callsubr".equals(this.commandName)) && (!"return".equals(this.commandName)) && (!"dotsection".equals(this.commandName)))
        if ("vstem3".equals(this.commandName))
          i = 1;
        else if ("hstem3".equals(this.commandName))
          i = 1;
        else if ("seac".equals(this.commandName))
          for (int j = 0; j < 3; j++)
            ((CharstringElement)this.args.get(j)).scale();
        else if (!"sbw".equals(this.commandName))
          if ("div".equals(this.commandName))
            i = 1;
          else if ((!"callothersubr".equals(this.commandName)) && (!"pop".equals(this.commandName)))
            if ("setcurrentpoint".equals(this.commandName))
              i = 1;
            else if ((!"hsbw".equals(this.commandName)) && (!"endchar".equals(this.commandName)))
              if ("rmoveto".equals(this.commandName))
                i = 1;
              else if ("hmoveto".equals(this.commandName))
                i = 1;
              else if ("vhcurveto".equals(this.commandName))
                i = 1;
              else if ("hvcurveto".equals(this.commandName))
                i = 1;
              else if ("flex".equals(this.commandName))
                i = 1;
              else if (!this.commandName.isEmpty());
      if (i != 0)
      {
        Iterator localIterator = this.args.iterator();
        while (localIterator.hasNext())
        {
          CharstringElement localCharstringElement = (CharstringElement)localIterator.next();
          localCharstringElement.scale();
        }
      }
    }

    public byte[] getType2Bytes()
    {
      if (!this.isCommand)
      {
        if (this.isResult)
          return new byte[0];
        return CFFUtils.storeCharstringType2Integer(this.numberValue);
      }
      int i = 0;
      byte[] arrayOfByte1 = new byte[0];
      if ("hstem".equals(this.commandName))
      {
        i = 1;
        arrayOfByte1 = new byte[] { 1 };
      }
      else if ("vstem".equals(this.commandName))
      {
        i = 1;
        arrayOfByte1 = new byte[] { 3 };
      }
      else if ("vmoveto".equals(this.commandName))
      {
        i = 1;
        arrayOfByte1 = new byte[] { 4 };
      }
      else if ("rlineto".equals(this.commandName))
      {
        i = 1;
        arrayOfByte1 = new byte[] { 5 };
      }
      else if ("hlineto".equals(this.commandName))
      {
        i = 1;
        arrayOfByte1 = new byte[] { 6 };
      }
      else if ("vlineto".equals(this.commandName))
      {
        i = 1;
        arrayOfByte1 = new byte[] { 7 };
      }
      else if ("rrcurveto".equals(this.commandName))
      {
        i = 1;
        arrayOfByte1 = new byte[] { 8 };
      }
      else
      {
        if ("closepath".equals(this.commandName))
          return new byte[0];
        if ("callsubr".equals(this.commandName))
          return new byte[0];
        if ("return".equals(this.commandName))
          return new byte[0];
        if ("dotsection".equals(this.commandName))
          return new byte[0];
        if ((!"vstem3".equals(this.commandName)) && (!"hstem3".equals(this.commandName)))
          if ("seac".equals(this.commandName))
          {
            int j = ((CharstringElement)this.args.get(1)).numberValue;
            int k = ((CharstringElement)this.args.get(2)).numberValue;
            int m = ((CharstringElement)this.args.get(3)).numberValue;
            int n = ((CharstringElement)this.args.get(4)).numberValue;
            int i1 = StandardFonts.getEncodedChar(1, n).charAt(0);
            int i2 = StandardFonts.getEncodedChar(1, m).charAt(0);
            int i3 = -1;
            int i4 = -1;
            int i6;
            for (int i5 = 0; i5 < CFFWriter.this.glyphNames.length; i5++)
            {
              i6 = StandardFonts.getAdobeMap(CFFWriter.this.glyphNames[i5]);
              if ((i6 >= 0) && (i6 < 512))
              {
                if (i6 == i1)
                  i3 = i5;
                if (i6 == i2)
                  i4 = i5;
              }
            }
            if ((i3 == -1) || (i4 == -1))
              return new byte[0];
            try
            {
              ByteArrayOutputStream localByteArrayOutputStream2 = new ByteArrayOutputStream();
              i6 = CFFWriter.this.currentCharStringID;
              CFFWriter.this.charstringXDisplacement[i4] = 0;
              CFFWriter.this.charstringYDisplacement[i4] = 0;
              CFFWriter.this.inSeac = true;
              byte[] arrayOfByte2 = CFFWriter.this.convertCharstring(CFFWriter.this.charstrings[i4], i4);
              CFFWriter.this.inSeac = false;
              CFFWriter.this.currentCharStringID = i6;
              byte[] arrayOfByte3 = new byte[arrayOfByte2.length - 1];
              System.arraycopy(arrayOfByte2, 0, arrayOfByte3, 0, arrayOfByte3.length);
              localByteArrayOutputStream2.write(arrayOfByte3);
              localByteArrayOutputStream2.write(CFFUtils.storeCharstringType2Integer(-CFFWriter.this.charstringXDisplacement[i4] + j));
              localByteArrayOutputStream2.write(CFFUtils.storeCharstringType2Integer(-CFFWriter.this.charstringYDisplacement[i4] + k));
              localByteArrayOutputStream2.write(21);
              CFFWriter.this.charstringXDisplacement[i3] = 0;
              CFFWriter.this.charstringYDisplacement[i3] = 0;
              byte[] arrayOfByte4 = CFFWriter.this.convertCharstring(CFFWriter.this.charstrings[i3], i3);
              CFFWriter.this.currentCharStringID = i6;
              localByteArrayOutputStream2.write(arrayOfByte4);
              return localByteArrayOutputStream2.toByteArray();
            }
            catch (IOException localIOException2)
            {
              if (LogWriter.isOutput())
                LogWriter.writeLog("Exception: " + localIOException2.getMessage());
            }
          }
          else
          {
            if ("sbw".equals(this.commandName))
              return new byte[0];
            if ("div".equals(this.commandName))
            {
              i = 1;
              arrayOfByte1 = new byte[] { 12, 12 };
            }
            else if ((!"callothersubr".equals(this.commandName)) && (!"pop".equals(this.commandName)) && (!"setcurrentpoint".equals(this.commandName)))
            {
              if ("hsbw".equals(this.commandName))
                return new byte[0];
              if ("endchar".equals(this.commandName))
              {
                i = 1;
                arrayOfByte1 = new byte[] { 14 };
              }
              else if ("rmoveto".equals(this.commandName))
              {
                i = 1;
                arrayOfByte1 = new byte[] { 21 };
              }
              else if ("hmoveto".equals(this.commandName))
              {
                i = 1;
                arrayOfByte1 = new byte[] { 22 };
              }
              else if ("vhcurveto".equals(this.commandName))
              {
                i = 1;
                arrayOfByte1 = new byte[] { 30 };
              }
              else if ("hvcurveto".equals(this.commandName))
              {
                i = 1;
                arrayOfByte1 = new byte[] { 31 };
              }
              else if ("flex".equals(this.commandName))
              {
                i = 1;
                arrayOfByte1 = new byte[] { 12, 35 };
              }
              else if (this.commandName.isEmpty())
              {
                return new byte[0];
              }
            }
          }
      }
      if (i != 0)
      {
        ByteArrayOutputStream localByteArrayOutputStream1 = getStreamWithArgs();
        try
        {
          localByteArrayOutputStream1.write(arrayOfByte1);
        }
        catch (IOException localIOException1)
        {
          if (LogWriter.isOutput())
            LogWriter.writeLog("Exception: " + localIOException1.getMessage());
        }
        return localByteArrayOutputStream1.toByteArray();
      }
      return new byte[0];
    }

    private ByteArrayOutputStream getStreamWithArgs()
    {
      ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
      try
      {
        Iterator localIterator = this.args.iterator();
        while (localIterator.hasNext())
        {
          CharstringElement localCharstringElement = (CharstringElement)localIterator.next();
          localByteArrayOutputStream.write(localCharstringElement.getType2Bytes());
        }
      }
      catch (IOException localIOException)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog("Exception: " + localIOException.getMessage());
      }
      return localByteArrayOutputStream;
    }

    public String toString()
    {
      if (this.isCommand)
        return this.commandName + this.args.toString();
      if (this.isResult)
        return "result of " + this.parent;
      return String.valueOf(this.numberValue);
    }

    private void printStack()
    {
      System.out.println("Stack bottom");
      Iterator localIterator = CFFWriter.this.currentCharString.iterator();
      while (localIterator.hasNext())
      {
        CharstringElement localCharstringElement = (CharstringElement)localIterator.next();
        if (!localCharstringElement.isCommand)
          System.out.println(localCharstringElement);
      }
      System.out.println("Stack top");
    }

    private boolean claimArguments(int paramInt, boolean paramBoolean1, boolean paramBoolean2)
    {
      int i;
      if (paramInt > 0)
      {
        i = CFFWriter.this.currentCharString.indexOf(this);
        if (i == -1)
          throw new RuntimeException("Not in list!");
        int j = 0;
        int k = 0;
        while ((j < paramInt) && (k == 0))
        {
          int m = 0;
          int n;
          CharstringElement localCharstringElement2;
          if (paramBoolean1)
            for (n = 0; (m == 0) && (n <= i); n++)
            {
              localCharstringElement2 = (CharstringElement)CFFWriter.this.currentCharString.get(n);
              if (!localCharstringElement2.isCommand)
              {
                j++;
                this.args.add(localCharstringElement2);
                CFFWriter.this.currentCharString.remove(localCharstringElement2);
                m = 1;
              }
            }
          else
            for (n = i; (m == 0) && (n >= 0); n--)
            {
              localCharstringElement2 = (CharstringElement)CFFWriter.this.currentCharString.get(n);
              if (!localCharstringElement2.isCommand)
              {
                j++;
                this.args.add(localCharstringElement2);
                CFFWriter.this.currentCharString.remove(localCharstringElement2);
                m = 1;
                i--;
              }
            }
          if (m == 0)
            k = 1;
        }
        if (j < paramInt)
          return false;
      }
      if (paramBoolean2)
        for (i = 0; i < CFFWriter.this.currentCharString.size(); i++)
        {
          CharstringElement localCharstringElement1 = (CharstringElement)CFFWriter.this.currentCharString.get(i);
          if (!localCharstringElement1.isCommand)
            CFFWriter.this.currentCharString.remove(localCharstringElement1);
        }
      return true;
    }
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.fonts.tt.conversion.CFFWriter
 * JD-Core Version:    0.6.2
 */