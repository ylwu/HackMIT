package org.jpedal.fonts.tt.hinting;

import java.io.PrintStream;
import java.io.Serializable;
import java.util.HashMap;
import org.jpedal.fonts.tt.FontFile2;
import org.jpedal.fonts.tt.Maxp;
import org.jpedal.fonts.tt.TTGlyph;
import org.jpedal.utils.LogWriter;

public class TTVM
  implements Serializable
{
  protected static final int TWILIGHT_ZONE = 0;
  protected static final int GLYPH_ZONE = 1;
  protected static final int ORIGINAL = 2;
  private boolean printOut = false;
  private final int[] preProgram;
  private final int[] fontProgram;
  private boolean fontProgramRun = false;
  private boolean scalerRun = false;
  private double ptSize;
  private double ppem;
  private double scaler;
  private int[][] x;
  private int[][] y;
  private boolean[][] curve;
  private boolean[][] contour;
  private boolean[][][] touched;
  Maxp maxp;
  private boolean useDefaultGS;
  private Stack stack = new Stack();
  private final Cvt cvt;
  private final TTGraphicsState graphicsState;
  private final int[] storage;
  private final HashMap<Integer, int[]> functions;
  private final HashMap<Integer, int[]> instructions;
  private static final int SVTCAy = 0;
  private static final int SVTCAx = 1;
  private static final int SPVTCAy = 2;
  private static final int SPVTCAx = 3;
  private static final int SFVTCAy = 4;
  private static final int SFVTCAx = 5;
  private static final int SPVTL0 = 6;
  private static final int SPVTL1 = 7;
  private static final int SFVTL0 = 8;
  private static final int SFVTL1 = 9;
  private static final int SPVFS = 10;
  private static final int SFVFS = 11;
  private static final int GPV = 12;
  private static final int GFV = 13;
  private static final int SFVTPV = 14;
  private static final int ISECT = 15;
  private static final int SRP0 = 16;
  private static final int SRP1 = 17;
  private static final int SRP2 = 18;
  private static final int SZP0 = 19;
  private static final int SZP1 = 20;
  private static final int SZP2 = 21;
  private static final int SZPS = 22;
  private static final int SLOOP = 23;
  private static final int RTG = 24;
  private static final int RTHG = 25;
  private static final int SMD = 26;
  private static final int ELSE = 27;
  private static final int JMPR = 28;
  private static final int SCVTCI = 29;
  private static final int SSWCI = 30;
  private static final int SSW = 31;
  private static final int DUP = 32;
  private static final int POP = 33;
  private static final int CLEAR = 34;
  private static final int SWAP = 35;
  private static final int DEPTH = 36;
  private static final int CINDEX = 37;
  private static final int MINDEX = 38;
  private static final int ALIGNPTS = 39;
  private static final int UTP = 41;
  private static final int LOOPCALL = 42;
  private static final int CALL = 43;
  private static final int FDEF = 44;
  private static final int ENDF = 45;
  private static final int MDAP0 = 46;
  private static final int MDAP1 = 47;
  private static final int IUPy = 48;
  private static final int IUPx = 49;
  private static final int SHP0 = 50;
  private static final int SHP1 = 51;
  private static final int SHC0 = 52;
  private static final int SHC1 = 53;
  private static final int SHZ0 = 54;
  private static final int SHZ1 = 55;
  private static final int SHPIX = 56;
  private static final int IP = 57;
  private static final int MSIRP0 = 58;
  private static final int MSIRP1 = 59;
  private static final int ALIGNRP = 60;
  private static final int RTDG = 61;
  private static final int MIAP0 = 62;
  private static final int MIAP1 = 63;
  private static final int NPUSHB = 64;
  private static final int NPUSHW = 65;
  private static final int WS = 66;
  private static final int RS = 67;
  private static final int WCVTP = 68;
  private static final int RCVT = 69;
  private static final int GC0 = 70;
  private static final int GC1 = 71;
  private static final int SCFS = 72;
  private static final int MD0 = 73;
  private static final int MD1 = 74;
  private static final int MPPEM = 75;
  private static final int MPS = 76;
  private static final int FLIPON = 77;
  private static final int FLIPOFF = 78;
  private static final int DEBUG = 79;
  private static final int LT = 80;
  private static final int LTEQ = 81;
  private static final int GT = 82;
  private static final int GTEQ = 83;
  private static final int EQ = 84;
  private static final int NEQ = 85;
  private static final int ODD = 86;
  private static final int EVEN = 87;
  private static final int IF = 88;
  private static final int EIF = 89;
  private static final int AND = 90;
  private static final int OR = 91;
  private static final int NOT = 92;
  private static final int DELTAP1 = 93;
  private static final int SDB = 94;
  private static final int SDS = 95;
  private static final int ADD = 96;
  private static final int SUB = 97;
  private static final int DIV = 98;
  private static final int MUL = 99;
  private static final int ABS = 100;
  private static final int NEG = 101;
  private static final int FLOOR = 102;
  private static final int CEILING = 103;
  private static final int ROUND00 = 104;
  private static final int ROUND01 = 105;
  private static final int ROUND10 = 106;
  private static final int ROUND11 = 107;
  private static final int NROUND00 = 108;
  private static final int NROUND01 = 109;
  private static final int NROUND10 = 110;
  private static final int NROUND11 = 111;
  private static final int WCVTF = 112;
  private static final int DELTAP2 = 113;
  private static final int DELTAP3 = 114;
  private static final int DELTAC1 = 115;
  private static final int DELTAC2 = 116;
  private static final int DELTAC3 = 117;
  private static final int SROUND = 118;
  private static final int S45ROUND = 119;
  private static final int JROT = 120;
  private static final int JROF = 121;
  private static final int ROFF = 122;
  private static final int RUTG = 124;
  private static final int RDTG = 125;
  private static final int SANGW = 126;
  private static final int AA = 127;
  private static final int FLIPPT = 128;
  private static final int FLIPRGON = 129;
  private static final int FLIPRGOFF = 130;
  private static final int SCANCTRL = 133;
  private static final int SDPVTL0 = 134;
  private static final int SDPVTL1 = 135;
  private static final int GETINFO = 136;
  private static final int IDEF = 137;
  private static final int ROLL = 138;
  private static final int MAX = 139;
  private static final int MIN = 140;
  private static final int SCANTYPE = 141;
  private static final int INSTCTRL = 142;
  private static final int PUSHB1 = 176;
  private static final int PUSHB2 = 177;
  private static final int PUSHB3 = 178;
  private static final int PUSHB4 = 179;
  private static final int PUSHB5 = 180;
  private static final int PUSHB6 = 181;
  private static final int PUSHB7 = 182;
  private static final int PUSHB8 = 183;
  private static final int PUSHW1 = 184;
  private static final int PUSHW2 = 185;
  private static final int PUSHW3 = 186;
  private static final int PUSHW4 = 187;
  private static final int PUSHW5 = 188;
  private static final int PUSHW6 = 189;
  private static final int PUSHW7 = 190;
  private static final int PUSHW8 = 191;
  private static final int MDRP = 192;
  private static final int MIRP = 224;
  private static final int paramRESETRP0 = 16;
  private static final int paramUSEMINDIST = 8;
  private static final int paramROUND = 4;

  public TTVM(FontFile2 paramFontFile2, Maxp paramMaxp)
  {
    this.cvt = new Cvt(paramFontFile2);
    this.graphicsState = new TTGraphicsState();
    this.preProgram = readProgramTable(paramFontFile2, 14);
    this.fontProgram = readProgramTable(paramFontFile2, 10);
    this.storage = new int[paramMaxp.getMaxStorage()];
    this.functions = new HashMap();
    this.instructions = new HashMap();
    this.maxp = paramMaxp;
    int i = paramMaxp.getMaxPoints();
    this.x = new int[4][i];
    this.y = new int[4][i];
    this.curve = new boolean[2][i];
    this.contour = new boolean[2][i];
    this.touched = new boolean[4][i][2];
    this.x[0] = new int[paramMaxp.getMaxTwilightPoints()];
    this.y[0] = new int[paramMaxp.getMaxTwilightPoints()];
  }

  public void setScaleVars(double paramDouble1, double paramDouble2, double paramDouble3)
  {
    this.scalerRun = false;
    this.ppem = ((int)(paramDouble2 + 0.5D));
    this.ptSize = paramDouble3;
    if (!this.fontProgramRun)
    {
      execute(this.fontProgram, this.graphicsState);
      this.fontProgramRun = true;
    }
    if (paramDouble1 != this.scaler)
    {
      this.scaler = paramDouble1;
      this.cvt.scale(paramDouble1);
      execute(this.preProgram, this.graphicsState);
      this.scalerRun = true;
    }
  }

  public void processGlyph(int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3, boolean[] paramArrayOfBoolean1, boolean[] paramArrayOfBoolean2)
  {
    this.x[1] = paramArrayOfInt2;
    this.x[3] = new int[paramArrayOfInt2.length];
    System.arraycopy(this.x[1], 0, this.x[3], 0, this.x[1].length);
    this.y[1] = paramArrayOfInt3;
    this.y[3] = new int[paramArrayOfInt3.length];
    System.arraycopy(this.y[1], 0, this.y[3], 0, this.y[1].length);
    this.curve[1] = paramArrayOfBoolean1;
    this.contour[1] = paramArrayOfBoolean2;
    int i = this.maxp.getMaxTwilightPoints();
    if (paramArrayOfInt2.length > i)
      i = paramArrayOfInt2.length;
    this.touched = new boolean[4][i][2];
    this.stack = new Stack();
    TTGraphicsState localTTGraphicsState;
    if (this.useDefaultGS)
      localTTGraphicsState = new TTGraphicsState();
    else
      try
      {
        localTTGraphicsState = (TTGraphicsState)this.graphicsState.clone();
        localTTGraphicsState.resetForGlyph();
      }
      catch (CloneNotSupportedException localCloneNotSupportedException)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog("Exception: " + localCloneNotSupportedException.getMessage());
        localCloneNotSupportedException.printStackTrace(System.out);
        localTTGraphicsState = new TTGraphicsState();
      }
    if (localTTGraphicsState.instructControl != 0)
      return;
    execute(paramArrayOfInt1, localTTGraphicsState);
  }

  private void execute(int[] paramArrayOfInt, TTGraphicsState paramTTGraphicsState)
  {
    if (paramArrayOfInt == null)
      return;
    for (int i = 0; i < paramArrayOfInt.length; i++)
    {
      if (this.printOut)
        System.out.print(i + "\t");
      i = process(paramArrayOfInt[i], i, paramArrayOfInt, paramTTGraphicsState);
      if (TTGlyph.redecodePage)
        return;
    }
  }

  private int process(int paramInt1, int paramInt2, int[] paramArrayOfInt, TTGraphicsState paramTTGraphicsState)
  {
    try
    {
      int i;
      int k;
      double d2;
      double d4;
      double d7;
      int[] arrayOfInt1;
      int j;
      int i3;
      int i9;
      int i12;
      int i26;
      int i31;
      int[] arrayOfInt19;
      int m;
      int i5;
      int i10;
      int i13;
      int[] arrayOfInt17;
      int[] arrayOfInt3;
      int[] arrayOfInt7;
      int i20;
      int i23;
      int[] arrayOfInt22;
      int n;
      int i6;
      int[] arrayOfInt12;
      int i1;
      int[] arrayOfInt27;
      int[] arrayOfInt8;
      int[] arrayOfInt10;
      int i7;
      int i11;
      int[] arrayOfInt18;
      int i15;
      int[] arrayOfInt20;
      int i16;
      int i21;
      int i24;
      int[] arrayOfInt21;
      int[] arrayOfInt23;
      double d1;
      int i2;
      int i27;
      double d3;
      double d5;
      double d8;
      double d9;
      double d10;
      double d11;
      int i8;
      switch (paramInt1)
      {
      case 0:
        if (this.printOut)
          System.out.println("SVTCAy    - Set both vectors to y");
        paramTTGraphicsState.freedomVector = 16384;
        paramTTGraphicsState.projectionVector = 16384;
        paramTTGraphicsState.dualProjectionVector = 16384;
        break;
      case 1:
        if (this.printOut)
          System.out.println("SVTCAx    - Set both vectors to x");
        paramTTGraphicsState.freedomVector = 1073741824;
        paramTTGraphicsState.projectionVector = 1073741824;
        paramTTGraphicsState.dualProjectionVector = 1073741824;
        break;
      case 2:
        if (this.printOut)
          System.out.println("SPVTCAy   - Sets projection vector to y");
        paramTTGraphicsState.projectionVector = 16384;
        paramTTGraphicsState.dualProjectionVector = 16384;
        break;
      case 3:
        if (this.printOut)
          System.out.println("SPVTCAx   - Sets projection vector to x");
        paramTTGraphicsState.projectionVector = 1073741824;
        paramTTGraphicsState.dualProjectionVector = 1073741824;
        break;
      case 4:
        if (this.printOut)
          System.out.println("SFVTCAy   - Sets freedom vector to y");
        paramTTGraphicsState.freedomVector = 16384;
        break;
      case 5:
        if (this.printOut)
          System.out.println("SFVTCAx   - Sets freedom vector to x");
        paramTTGraphicsState.freedomVector = 1073741824;
        break;
      case 6:
        if (this.printOut)
          System.out.println("SPVTL0    - Set projection vector to line");
        i = this.stack.pop();
        k = this.stack.pop();
        d2 = getDoubleFromF26Dot6(this.x[paramTTGraphicsState.zp2][k] - this.x[paramTTGraphicsState.zp1][i]);
        d4 = getDoubleFromF26Dot6(this.y[paramTTGraphicsState.zp2][k] - this.y[paramTTGraphicsState.zp1][i]);
        d7 = Math.sqrt(d2 * d2 + d4 * d4);
        d2 /= d7;
        d4 /= d7;
        paramTTGraphicsState.projectionVector = TTGraphicsState.createVector(storeDoubleAsF2Dot14(d2), storeDoubleAsF2Dot14(d4));
        paramTTGraphicsState.dualProjectionVector = paramTTGraphicsState.projectionVector;
        break;
      case 7:
        if (this.printOut)
          System.out.println("SPVTL1    - Set projection vector perpendicular to line");
        i = this.stack.pop();
        k = this.stack.pop();
        d2 = getDoubleFromF26Dot6(this.x[paramTTGraphicsState.zp2][k] - this.x[paramTTGraphicsState.zp1][i]);
        d4 = getDoubleFromF26Dot6(this.y[paramTTGraphicsState.zp2][k] - this.y[paramTTGraphicsState.zp1][i]);
        d7 = Math.sqrt(d2 * d2 + d4 * d4);
        d2 /= d7;
        d4 /= d7;
        paramTTGraphicsState.projectionVector = TTGraphicsState.createVector(storeDoubleAsF2Dot14(-d4), storeDoubleAsF2Dot14(d2));
        paramTTGraphicsState.dualProjectionVector = paramTTGraphicsState.projectionVector;
        break;
      case 8:
        if (this.printOut)
          System.out.println("SFVTL0    - Set freedom vector to line");
        i = this.stack.pop();
        k = this.stack.pop();
        d2 = getDoubleFromF26Dot6(this.x[paramTTGraphicsState.zp1][k] - this.x[paramTTGraphicsState.zp2][i]);
        d4 = getDoubleFromF26Dot6(this.y[paramTTGraphicsState.zp1][k] - this.y[paramTTGraphicsState.zp2][i]);
        d7 = Math.sqrt(d2 * d2 + d4 * d4);
        d2 /= d7;
        d4 /= d7;
        paramTTGraphicsState.freedomVector = TTGraphicsState.createVector(storeDoubleAsF2Dot14(d2), storeDoubleAsF2Dot14(d4));
        break;
      case 9:
        if (this.printOut)
          System.out.println("SFVTL1    - Set freedom vector perpendicular to line");
        i = this.stack.pop();
        k = this.stack.pop();
        d2 = getDoubleFromF26Dot6(this.x[paramTTGraphicsState.zp1][k] - this.x[paramTTGraphicsState.zp2][i]);
        d4 = getDoubleFromF26Dot6(this.y[paramTTGraphicsState.zp1][k] - this.y[paramTTGraphicsState.zp2][i]);
        d7 = Math.sqrt(d2 * d2 + d4 * d4);
        d2 /= d7;
        d4 /= d7;
        paramTTGraphicsState.freedomVector = TTGraphicsState.createVector(storeDoubleAsF2Dot14(-d4), storeDoubleAsF2Dot14(d2));
        break;
      case 10:
        if (this.printOut)
          System.out.println("SPVFS     - Sets the projection vector from the stack");
        i = this.stack.pop();
        k = this.stack.pop();
        paramTTGraphicsState.projectionVector = TTGraphicsState.createVector(k, i);
        paramTTGraphicsState.dualProjectionVector = paramTTGraphicsState.projectionVector;
        break;
      case 11:
        if (this.printOut)
          System.out.println("SFVFS     - Sets the freedom vector from the stack");
        i = this.stack.pop();
        k = this.stack.pop();
        paramTTGraphicsState.freedomVector = TTGraphicsState.createVector(k, i);
        break;
      case 12:
        if (this.printOut)
          System.out.println("GPV       - Gets the projection vector onto the stack");
        arrayOfInt1 = TTGraphicsState.getVectorComponents(paramTTGraphicsState.projectionVector);
        this.stack.push(arrayOfInt1[0]);
        this.stack.push(arrayOfInt1[1]);
        break;
      case 13:
        if (this.printOut)
          System.out.println("GFV       - Gets the freedom vector onto the stack");
        arrayOfInt1 = TTGraphicsState.getVectorComponents(paramTTGraphicsState.freedomVector);
        this.stack.push(arrayOfInt1[0]);
        this.stack.push(arrayOfInt1[1]);
        break;
      case 14:
        if (this.printOut)
          System.out.println("SFVTPV    - Sets freedom vector to projection vector");
        paramTTGraphicsState.freedomVector = paramTTGraphicsState.projectionVector;
        break;
      case 15:
        if (this.printOut)
          System.out.println("ISECT     - Set point to intersection of lines");
        j = this.stack.pop();
        k = this.stack.pop();
        i3 = this.stack.pop();
        i9 = this.stack.pop();
        i12 = this.x[paramTTGraphicsState.zp1][i9];
        int i18 = this.y[paramTTGraphicsState.zp1][i9];
        int i22 = this.x[paramTTGraphicsState.zp1][i3] - i12;
        i26 = this.y[paramTTGraphicsState.zp1][i3] - i18;
        int i28 = this.x[paramTTGraphicsState.zp0][k];
        i31 = this.y[paramTTGraphicsState.zp0][k];
        int i33 = this.x[paramTTGraphicsState.zp0][j] - i28;
        int i34 = this.y[paramTTGraphicsState.zp0][j] - i31;
        int i35;
        int i36;
        if ((i22 == 0) && (i33 == 0))
        {
          i35 = i12 + i28 / 2;
          i36 = (i18 + i31 + (i18 + i26) + (i31 + i34)) / 4;
        }
        else
        {
          double d12;
          double d13;
          if (i22 == 0)
          {
            d12 = getDoubleFromF26Dot6(i34) / getDoubleFromF26Dot6(i33);
            d13 = getDoubleFromF26Dot6(i31) - d12 * getDoubleFromF26Dot6(i28);
            i35 = i12;
            i36 = storeDoubleAsF26Dot6(d12 * getDoubleFromF26Dot6(i12) + d13);
          }
          else if (i33 == 0)
          {
            d12 = getDoubleFromF26Dot6(i26) / getDoubleFromF26Dot6(i22);
            d13 = getDoubleFromF26Dot6(i18) - d12 * getDoubleFromF26Dot6(i12);
            i35 = i28;
            i36 = storeDoubleAsF26Dot6(d12 * getDoubleFromF26Dot6(i28) + d13);
          }
          else
          {
            d12 = getDoubleFromF26Dot6(i26) / getDoubleFromF26Dot6(i22);
            d13 = getDoubleFromF26Dot6(i18) - d12 * getDoubleFromF26Dot6(i12);
            double d14 = getDoubleFromF26Dot6(i34) / getDoubleFromF26Dot6(i33);
            double d15 = getDoubleFromF26Dot6(i31) - d14 * getDoubleFromF26Dot6(i28);
            if (d12 == d14)
            {
              i35 = (i12 + i28 + (i12 + i22) + (i28 + i33)) / 4;
              i36 = (i18 + i31 + (i18 + i26) + (i31 + i34)) / 4;
            }
            else
            {
              double d16 = (d15 - d13) / (d12 - d14);
              i35 = storeDoubleAsF26Dot6(d16);
              i36 = storeDoubleAsF26Dot6(d12 * d16 + d13);
            }
          }
        }
        int i37 = this.stack.pop();
        this.x[paramTTGraphicsState.zp2][i37] = i35;
        this.y[paramTTGraphicsState.zp2][i37] = i36;
        break;
      case 16:
        if (this.printOut)
          System.out.println("SRP0      - Set rp0");
        paramTTGraphicsState.rp0 = this.stack.pop();
        break;
      case 17:
        if (this.printOut)
          System.out.println("SRP1      - Set rp1");
        paramTTGraphicsState.rp1 = this.stack.pop();
        break;
      case 18:
        if (this.printOut)
          System.out.println("SRP2      - Set rp2");
        paramTTGraphicsState.rp2 = this.stack.pop();
        break;
      case 19:
        if (this.printOut)
          System.out.println("SZP0      - Sets zp0");
        j = this.stack.pop();
        paramTTGraphicsState.zp0 = j;
        break;
      case 20:
        if (this.printOut)
          System.out.println("SZP1      - Sets zp1");
        j = this.stack.pop();
        paramTTGraphicsState.zp1 = j;
        break;
      case 21:
        if (this.printOut)
          System.out.println("SZP2      - Sets zp2");
        j = this.stack.pop();
        paramTTGraphicsState.zp2 = j;
        break;
      case 22:
        if (this.printOut)
          System.out.println("SZPS      - Sets all zone pointers");
        j = this.stack.pop();
        paramTTGraphicsState.zp0 = j;
        paramTTGraphicsState.zp1 = j;
        paramTTGraphicsState.zp2 = j;
        break;
      case 23:
        if (this.printOut)
          System.out.println("SLOOP     - Sets loop variable");
        paramTTGraphicsState.loop = this.stack.pop();
        break;
      case 24:
        if (this.printOut)
          System.out.println("RTG       - Sets round state to grid");
        paramTTGraphicsState.roundState = 72;
        paramTTGraphicsState.gridPeriod = 1.0D;
        break;
      case 25:
        if (this.printOut)
          System.out.println("RTHG      - Sets round state to half grid");
        paramTTGraphicsState.roundState = 104;
        paramTTGraphicsState.gridPeriod = 1.0D;
        break;
      case 26:
        if (this.printOut)
          System.out.println("SMD       - Sets minimum distance");
        paramTTGraphicsState.minimumDistance = this.stack.pop();
        break;
      case 27:
        if (this.printOut)
          System.out.println("ELSE      - ELSE");
        j = 0;
        k = 0;
        do
        {
          if ((j == 89) && (k != 0))
            k--;
          paramInt2++;
          j = paramArrayOfInt[paramInt2];
          if (j == 88)
            k++;
          if (j == 64)
          {
            paramInt2++;
            paramInt2 += paramArrayOfInt[paramInt2];
          }
          else if (j == 65)
          {
            paramInt2++;
            paramInt2 += paramArrayOfInt[paramInt2] * 2;
          }
          else if ((j >= 176) && (j <= 183))
          {
            paramInt2 += j + 1 - 176;
          }
          else if ((j >= 184) && (j <= 191))
          {
            paramInt2 += (j + 1 - 184) * 2;
          }
        }
        while ((j != 89) || (k != 0));
        break;
      case 28:
        if (this.printOut)
          System.out.println("JMPR      - Jump");
        j = this.stack.pop();
        paramInt2 = paramInt2 + j - 1;
        break;
      case 29:
        if (this.printOut)
          System.out.println("SCVTCI    - Set control value table cut in");
        paramTTGraphicsState.controlValueTableCutIn = this.stack.pop();
        break;
      case 30:
        if (this.printOut)
          System.out.println("SSWCI     - Set single width cut in");
        paramTTGraphicsState.singleWidthCutIn = this.stack.pop();
        break;
      case 31:
        if (this.printOut)
          System.out.println("SSW       - Set single width");
        paramTTGraphicsState.singleWidthValue = this.stack.pop();
        break;
      case 32:
        if (this.printOut)
          System.out.println("DUP       - Duplicate the top stack element");
        j = this.stack.pop();
        this.stack.push(j);
        this.stack.push(j);
        break;
      case 33:
        if (this.printOut)
          System.out.println("POP       - Remove the top stack element");
        this.stack.pop();
        break;
      case 34:
        if (this.printOut)
          System.out.println("CLEAR     - Clear the stack");
        this.stack = new Stack();
        break;
      case 35:
        if (this.printOut)
          System.out.println("SWAP      - Swap the top two stack elements");
        j = this.stack.pop();
        k = this.stack.pop();
        this.stack.push(j);
        this.stack.push(k);
        break;
      case 36:
        if (this.printOut)
          System.out.println("DEPTH     - Returns depth of stack");
        this.stack.push(this.stack.size());
        break;
      case 37:
        if (this.printOut)
          System.out.println("CINDEX    - Copy Indexed element to top of stack");
        j = this.stack.pop();
        k = this.stack.elementAt(j);
        this.stack.push(k);
        break;
      case 38:
        if (this.printOut)
          System.out.println("MINDEX    - Move Indexed element to top of stack");
        j = this.stack.pop();
        k = this.stack.remove(j);
        this.stack.push(k);
        break;
      case 39:
        if (this.printOut)
          System.out.println("ALIGNPTS  - Move points along fv to average of their pv positions");
        j = this.stack.pop();
        k = this.stack.pop();
        i3 = TTGraphicsState.getCoordsOnVector(paramTTGraphicsState.projectionVector, this.x[paramTTGraphicsState.zp1][j], this.y[paramTTGraphicsState.zp1][j]);
        i9 = TTGraphicsState.getCoordsOnVector(paramTTGraphicsState.projectionVector, this.x[paramTTGraphicsState.zp0][k], this.y[paramTTGraphicsState.zp0][k]);
        i12 = (i3 + i9) / 2;
        int[] arrayOfInt16 = paramTTGraphicsState.getFVMoveforPVDistance(i12 - i3);
        this.x[paramTTGraphicsState.zp1][j] += arrayOfInt16[0];
        this.y[paramTTGraphicsState.zp1][j] += arrayOfInt16[1];
        this.x[paramTTGraphicsState.zp0][k] -= arrayOfInt16[0];
        this.y[paramTTGraphicsState.zp0][k] -= arrayOfInt16[1];
        arrayOfInt19 = TTGraphicsState.getVectorComponents(paramTTGraphicsState.freedomVector);
        if (arrayOfInt19[0] != 0)
        {
          this.touched[paramTTGraphicsState.zp1][j][0] = 1;
          this.touched[paramTTGraphicsState.zp0][k][0] = 1;
        }
        if (arrayOfInt19[1] != 0)
        {
          this.touched[paramTTGraphicsState.zp1][j][1] = 1;
          this.touched[paramTTGraphicsState.zp0][k][1] = 1;
        }
        break;
      case 41:
        if (this.printOut)
          System.out.println("UTP       - Untouch point");
        j = this.stack.pop();
        if (paramTTGraphicsState.freedomVector == 1073741824)
        {
          this.touched[paramTTGraphicsState.zp0][j][0] = 0;
        }
        else if (paramTTGraphicsState.freedomVector == 16384)
        {
          this.touched[paramTTGraphicsState.zp0][j][1] = 0;
        }
        else
        {
          this.touched[paramTTGraphicsState.zp0][j][0] = 0;
          this.touched[paramTTGraphicsState.zp0][j][1] = 0;
        }
        break;
      case 42:
        if (this.printOut)
          System.out.println("LOOPCALL  - Call a function many times");
        j = this.stack.pop();
        k = this.stack.pop();
        int[] arrayOfInt5 = (int[])this.functions.get(Integer.valueOf(j));
        for (i9 = 0; i9 < k; i9++)
          execute(arrayOfInt5, paramTTGraphicsState);
        if (this.printOut)
          System.out.println("LOOPCALL finished");
        break;
      case 43:
        if (this.printOut)
          System.out.println("CALL      - Call a function");
        j = this.stack.pop();
        int[] arrayOfInt2 = (int[])this.functions.get(Integer.valueOf(j));
        execute(arrayOfInt2, paramTTGraphicsState);
        if (this.printOut)
          System.out.println("CALL finished");
        break;
      case 44:
        if (this.printOut)
          System.out.println("FDEF      - Define a function");
        j = this.stack.pop();
        m = paramInt2;
        int i4;
        do
        {
          paramInt2++;
          i4 = paramArrayOfInt[paramInt2];
          if (i4 == 64)
          {
            paramInt2++;
            paramInt2 += paramArrayOfInt[paramInt2];
          }
          else if (i4 == 65)
          {
            paramInt2++;
            paramInt2 += paramArrayOfInt[paramInt2] * 2;
          }
          else if ((i4 >= 176) && (i4 <= 183))
          {
            paramInt2 += i4 + 1 - 176;
          }
          else if ((i4 >= 184) && (i4 <= 191))
          {
            paramInt2 += (i4 + 1 - 184) * 2;
          }
        }
        while (i4 != 45);
        i9 = paramInt2 - m - 1;
        paramInt2 = m;
        int[] arrayOfInt11 = new int[i9];
        for (int i19 = 0; i19 < i9; i19++)
        {
          paramInt2++;
          arrayOfInt11[i19] = paramArrayOfInt[paramInt2];
        }
        this.functions.put(Integer.valueOf(j), arrayOfInt11);
        paramInt2++;
        break;
      case 45:
        if (this.printOut)
          System.out.println("ENDF      - End a function definition");
        break;
      case 46:
        if (this.printOut)
          System.out.println("MDAP0     - Sets a point as touched");
        j = this.stack.pop();
        paramTTGraphicsState.rp0 = j;
        paramTTGraphicsState.rp1 = j;
        if (paramTTGraphicsState.freedomVector == 1073741824)
        {
          this.touched[paramTTGraphicsState.zp0][j][0] = 1;
        }
        else if (paramTTGraphicsState.freedomVector == 16384)
        {
          this.touched[paramTTGraphicsState.zp0][j][1] = 1;
        }
        else
        {
          this.touched[paramTTGraphicsState.zp0][j][0] = 1;
          this.touched[paramTTGraphicsState.zp0][j][1] = 1;
        }
        break;
      case 47:
        if (this.printOut)
          System.out.println("MDAP1     - Rounds a point along the pV and marks as touched");
        j = this.stack.pop();
        paramTTGraphicsState.rp0 = j;
        paramTTGraphicsState.rp1 = j;
        m = TTGraphicsState.getCoordsOnVector(paramTTGraphicsState.projectionVector, this.x[paramTTGraphicsState.zp0][j], this.y[paramTTGraphicsState.zp0][j]);
        m = storeDoubleAsF26Dot6(paramTTGraphicsState.round(getDoubleFromF26Dot6(m))) - m;
        int[] arrayOfInt6 = paramTTGraphicsState.getFVMoveforPVDistance(m);
        this.x[paramTTGraphicsState.zp0][j] += arrayOfInt6[0];
        this.y[paramTTGraphicsState.zp0][j] += arrayOfInt6[1];
        int[] arrayOfInt9 = TTGraphicsState.getVectorComponents(paramTTGraphicsState.freedomVector);
        if (arrayOfInt9[0] != 0)
          this.touched[paramTTGraphicsState.zp0][j][0] = 1;
        if (arrayOfInt9[1] != 0)
          this.touched[paramTTGraphicsState.zp0][j][1] = 1;
        break;
      case 48:
        if (this.printOut)
          System.out.println("IUPy      - Interpolate untouched points in the y axis");
        interpolateUntouchedPoints(48);
        break;
      case 49:
        if (this.printOut)
          System.out.println("IUPx      - Interpolate untouched points on the x axis");
        interpolateUntouchedPoints(49);
        break;
      case 50:
        if (this.printOut)
          System.out.println("SHP0      - Shift point using RP2");
        for (j = 0; j < paramTTGraphicsState.loop; j++)
        {
          m = this.stack.pop();
          if ((m > this.x[paramTTGraphicsState.zp2].length) || (paramTTGraphicsState.rp2 > this.x[paramTTGraphicsState.zp1].length))
            break;
          i5 = TTGraphicsState.getCoordsOnVector(paramTTGraphicsState.projectionVector, this.x[paramTTGraphicsState.zp1][paramTTGraphicsState.rp2], this.y[paramTTGraphicsState.zp1][paramTTGraphicsState.rp2]);
          i10 = TTGraphicsState.getCoordsOnVector(paramTTGraphicsState.projectionVector, this.x[(2 + paramTTGraphicsState.zp1)][paramTTGraphicsState.rp2], this.y[(2 + paramTTGraphicsState.zp1)][paramTTGraphicsState.rp2]);
          i13 = i5 - i10;
          arrayOfInt17 = paramTTGraphicsState.getFVMoveforPVDistance(i13);
          this.x[paramTTGraphicsState.zp2][m] += arrayOfInt17[0];
          this.y[paramTTGraphicsState.zp2][m] += arrayOfInt17[1];
          arrayOfInt19 = TTGraphicsState.getVectorComponents(paramTTGraphicsState.freedomVector);
          if (arrayOfInt19[0] != 0)
            this.touched[paramTTGraphicsState.zp2][m][0] = 1;
          if (arrayOfInt19[1] != 0)
            this.touched[paramTTGraphicsState.zp2][m][1] = 1;
        }
        paramTTGraphicsState.loop = 1;
        break;
      case 51:
        if (this.printOut)
          System.out.println("SHP1      - Shift point using RP1");
        for (j = 0; j < paramTTGraphicsState.loop; j++)
        {
          m = this.stack.pop();
          i5 = TTGraphicsState.getCoordsOnVector(paramTTGraphicsState.projectionVector, this.x[paramTTGraphicsState.zp0][paramTTGraphicsState.rp1], this.y[paramTTGraphicsState.zp0][paramTTGraphicsState.rp1]);
          i10 = TTGraphicsState.getCoordsOnVector(paramTTGraphicsState.projectionVector, this.x[(2 + paramTTGraphicsState.zp0)][paramTTGraphicsState.rp1], this.y[(2 + paramTTGraphicsState.zp0)][paramTTGraphicsState.rp1]);
          i13 = i5 - i10;
          arrayOfInt17 = paramTTGraphicsState.getFVMoveforPVDistance(i13);
          this.x[paramTTGraphicsState.zp2][m] += arrayOfInt17[0];
          this.y[paramTTGraphicsState.zp2][m] += arrayOfInt17[1];
          if (arrayOfInt17[0] != 0)
            this.touched[paramTTGraphicsState.zp2][m][0] = 1;
          if (arrayOfInt17[1] != 0)
            this.touched[paramTTGraphicsState.zp2][m][1] = 1;
        }
        paramTTGraphicsState.loop = 1;
        break;
      case 52:
        if (this.printOut)
          System.out.println("SHC0      - Shift a contour using RP2");
        j = this.stack.pop();
        arrayOfInt3 = new int[this.contour[1].length];
        arrayOfInt7 = new int[this.contour[1].length];
        i10 = 0;
        i13 = 0;
        arrayOfInt7[0] = 0;
        for (i20 = 0; i20 < this.contour[1].length; i20++)
          if (this.contour[1][i20] != 0)
          {
            arrayOfInt7[(i10 + 1)] = (i20 + 1);
            arrayOfInt3[i10] = (i20 + 1 - i13);
            i13 = i20 + 1;
            i10++;
          }
        i20 = TTGraphicsState.getCoordsOnVector(paramTTGraphicsState.projectionVector, this.x[paramTTGraphicsState.zp1][paramTTGraphicsState.rp2], this.y[paramTTGraphicsState.zp1][paramTTGraphicsState.rp2]);
        i23 = TTGraphicsState.getCoordsOnVector(paramTTGraphicsState.projectionVector, this.x[(2 + paramTTGraphicsState.zp1)][paramTTGraphicsState.rp2], this.y[(2 + paramTTGraphicsState.zp1)][paramTTGraphicsState.rp2]);
        i26 = i20 - i23;
        arrayOfInt22 = paramTTGraphicsState.getFVMoveforPVDistance(i26);
        for (i31 = arrayOfInt7[j]; i31 < arrayOfInt7[j] + arrayOfInt3[j]; i31++)
          if ((paramTTGraphicsState.zp1 == paramTTGraphicsState.zp2) || (i31 != paramTTGraphicsState.rp2))
          {
            this.x[paramTTGraphicsState.zp2][i31] += arrayOfInt22[0];
            this.y[paramTTGraphicsState.zp2][i31] += arrayOfInt22[1];
          }
        break;
      case 53:
        if (this.printOut)
          System.out.println("SHC1      - Shift a contour using RP1");
        j = this.stack.pop();
        arrayOfInt3 = new int[this.contour[1].length];
        arrayOfInt7 = new int[this.contour[1].length];
        i10 = 0;
        i13 = 0;
        arrayOfInt7[0] = 0;
        for (i20 = 0; i20 < this.contour[1].length; i20++)
          if (this.contour[1][i20] != 0)
          {
            arrayOfInt7[(i10 + 1)] = (i20 + 1);
            arrayOfInt3[i10] = (i20 + 1 - i13);
            i13 = i20 + 1;
            i10++;
          }
        i20 = TTGraphicsState.getCoordsOnVector(paramTTGraphicsState.projectionVector, this.x[paramTTGraphicsState.zp0][paramTTGraphicsState.rp1], this.y[paramTTGraphicsState.zp0][paramTTGraphicsState.rp1]);
        i23 = TTGraphicsState.getCoordsOnVector(paramTTGraphicsState.projectionVector, this.x[(2 + paramTTGraphicsState.zp0)][paramTTGraphicsState.rp1], this.y[(2 + paramTTGraphicsState.zp0)][paramTTGraphicsState.rp1]);
        i26 = i20 - i23;
        arrayOfInt22 = paramTTGraphicsState.getFVMoveforPVDistance(i26);
        for (i31 = arrayOfInt7[j]; i31 < arrayOfInt7[j] + arrayOfInt3[j]; i31++)
          if ((paramTTGraphicsState.zp2 != paramTTGraphicsState.zp0) || (i31 != paramTTGraphicsState.rp1))
          {
            this.x[paramTTGraphicsState.zp2][i31] += arrayOfInt22[0];
            this.y[paramTTGraphicsState.zp2][i31] += arrayOfInt22[1];
          }
        break;
      case 54:
        if (this.printOut)
          System.out.println("SHZ0      - Shift a zone using RP2");
        j = this.stack.pop();
        n = TTGraphicsState.getCoordsOnVector(paramTTGraphicsState.projectionVector, this.x[paramTTGraphicsState.zp1][paramTTGraphicsState.rp2], this.y[paramTTGraphicsState.zp1][paramTTGraphicsState.rp2]);
        i6 = TTGraphicsState.getCoordsOnVector(paramTTGraphicsState.projectionVector, this.x[(2 + paramTTGraphicsState.zp1)][paramTTGraphicsState.rp2], this.y[(2 + paramTTGraphicsState.zp1)][paramTTGraphicsState.rp2]);
        i10 = n - i6;
        arrayOfInt12 = paramTTGraphicsState.getFVMoveforPVDistance(i10);
        for (i20 = 0; i20 < this.x[j].length; i20++)
          if ((j != paramTTGraphicsState.zp1) || (i20 != paramTTGraphicsState.rp2))
          {
            this.x[j][i20] += arrayOfInt12[0];
            this.y[j][i20] += arrayOfInt12[1];
          }
        break;
      case 55:
        if (this.printOut)
          System.out.println("SHZ1      - Shift a zone using RP1");
        j = this.stack.pop();
        n = TTGraphicsState.getCoordsOnVector(paramTTGraphicsState.projectionVector, this.x[paramTTGraphicsState.zp0][paramTTGraphicsState.rp1], this.y[paramTTGraphicsState.zp0][paramTTGraphicsState.rp1]);
        i6 = TTGraphicsState.getCoordsOnVector(paramTTGraphicsState.projectionVector, this.x[(2 + paramTTGraphicsState.zp0)][paramTTGraphicsState.rp1], this.y[(2 + paramTTGraphicsState.zp0)][paramTTGraphicsState.rp1]);
        i10 = n - i6;
        arrayOfInt12 = paramTTGraphicsState.getFVMoveforPVDistance(i10);
        for (i20 = 0; i20 < this.x[j].length; i20++)
          if ((j != paramTTGraphicsState.zp0) || (i20 != paramTTGraphicsState.rp1))
          {
            this.x[j][i20] += arrayOfInt12[0];
            this.y[j][i20] += arrayOfInt12[1];
          }
        break;
      case 56:
        if (this.printOut)
          System.out.println("SHPIX     - Move point along freedom vector");
        j = this.stack.pop();
        int[] arrayOfInt4 = TTGraphicsState.getVectorComponents(paramTTGraphicsState.freedomVector);
        for (i6 = 0; i6 < paramTTGraphicsState.loop; i6++)
        {
          i10 = this.stack.pop();
          int tmp6130_6128 = i10;
          int[] tmp6130_6127 = this.x[paramTTGraphicsState.zp2];
          tmp6130_6127[tmp6130_6128] = ((int)(tmp6130_6127[tmp6130_6128] + j * getDoubleFromF2Dot14(arrayOfInt4[0]) / 64.0D));
          int tmp6163_6161 = i10;
          int[] tmp6163_6160 = this.y[paramTTGraphicsState.zp2];
          tmp6163_6160[tmp6163_6161] = ((int)(tmp6163_6160[tmp6163_6161] + j * getDoubleFromF2Dot14(arrayOfInt4[1]) / 64.0D));
          if (arrayOfInt4[0] != 0)
            this.touched[paramTTGraphicsState.zp2][i10][0] = 1;
          if (arrayOfInt4[1] != 0)
            this.touched[paramTTGraphicsState.zp2][i10][1] = 1;
        }
        paramTTGraphicsState.loop = 1;
        break;
      case 57:
        if (this.printOut)
          System.out.println("IP        - Interpolate point");
        for (j = 0; j < paramTTGraphicsState.loop; j++)
        {
          i1 = this.stack.pop();
          if ((i1 < 0) || (i1 > this.x[paramTTGraphicsState.zp2].length) || (paramTTGraphicsState.rp1 > this.x[paramTTGraphicsState.zp0].length) || (paramTTGraphicsState.rp2 > this.x[paramTTGraphicsState.zp1].length))
            break;
          i6 = TTGraphicsState.getCoordsOnVector(paramTTGraphicsState.dualProjectionVector, this.x[(2 + paramTTGraphicsState.zp0)][paramTTGraphicsState.rp1], this.y[(2 + paramTTGraphicsState.zp0)][paramTTGraphicsState.rp1]);
          i10 = TTGraphicsState.getCoordsOnVector(paramTTGraphicsState.dualProjectionVector, this.x[(2 + paramTTGraphicsState.zp1)][paramTTGraphicsState.rp2], this.y[(2 + paramTTGraphicsState.zp1)][paramTTGraphicsState.rp2]);
          if (i6 != i10)
          {
            int i14 = TTGraphicsState.getCoordsOnVector(paramTTGraphicsState.dualProjectionVector, this.x[(2 + paramTTGraphicsState.zp2)][i1], this.y[(2 + paramTTGraphicsState.zp2)][i1]);
            double d6 = (i14 - i6) / (i10 - i6);
            i26 = TTGraphicsState.getCoordsOnVector(paramTTGraphicsState.projectionVector, this.x[paramTTGraphicsState.zp0][paramTTGraphicsState.rp1], this.y[paramTTGraphicsState.zp0][paramTTGraphicsState.rp1]);
            int i29 = TTGraphicsState.getCoordsOnVector(paramTTGraphicsState.projectionVector, this.x[paramTTGraphicsState.zp1][paramTTGraphicsState.rp2], this.y[paramTTGraphicsState.zp1][paramTTGraphicsState.rp2]);
            i31 = (int)(d6 * (i29 - i26) + i26 + 0.5D) - i14;
            int[] arrayOfInt25 = paramTTGraphicsState.getFVMoveforPVDistance(i31);
            this.x[paramTTGraphicsState.zp2][i1] += arrayOfInt25[0];
            this.y[paramTTGraphicsState.zp2][i1] += arrayOfInt25[1];
            arrayOfInt27 = TTGraphicsState.getVectorComponents(paramTTGraphicsState.freedomVector);
            if (arrayOfInt27[0] != 0)
              this.touched[paramTTGraphicsState.zp2][i1][0] = 1;
            if (arrayOfInt27[1] != 0)
              this.touched[paramTTGraphicsState.zp2][i1][1] = 1;
          }
        }
        paramTTGraphicsState.loop = 1;
        break;
      case 58:
        if (this.printOut)
          System.out.println("MSIRP0    - Move stack indirect relative point");
        j = this.stack.pop();
        i1 = this.stack.pop();
        arrayOfInt8 = paramTTGraphicsState.getFVMoveforPVDistance(j - (TTGraphicsState.getCoordsOnVector(paramTTGraphicsState.projectionVector, this.x[paramTTGraphicsState.zp1][i1], this.y[paramTTGraphicsState.zp1][i1]) - TTGraphicsState.getCoordsOnVector(paramTTGraphicsState.projectionVector, this.x[paramTTGraphicsState.zp0][paramTTGraphicsState.rp0], this.y[paramTTGraphicsState.zp0][paramTTGraphicsState.rp0])));
        this.x[paramTTGraphicsState.zp1][i1] += arrayOfInt8[0];
        this.y[paramTTGraphicsState.zp1][i1] += arrayOfInt8[1];
        arrayOfInt10 = TTGraphicsState.getVectorComponents(paramTTGraphicsState.freedomVector);
        if (arrayOfInt10[0] != 0)
          this.touched[paramTTGraphicsState.zp1][i1][0] = 1;
        if (arrayOfInt10[1] != 0)
          this.touched[paramTTGraphicsState.zp1][i1][1] = 1;
        paramTTGraphicsState.rp1 = paramTTGraphicsState.rp0;
        paramTTGraphicsState.rp2 = i1;
        break;
      case 59:
        if (this.printOut)
          System.out.println("MSIRP1    - Move stack indirect relative point");
        j = this.stack.pop();
        i1 = this.stack.pop();
        arrayOfInt8 = paramTTGraphicsState.getFVMoveforPVDistance(j - (TTGraphicsState.getCoordsOnVector(paramTTGraphicsState.projectionVector, this.x[paramTTGraphicsState.zp1][i1], this.y[paramTTGraphicsState.zp1][i1]) - TTGraphicsState.getCoordsOnVector(paramTTGraphicsState.projectionVector, this.x[paramTTGraphicsState.zp0][paramTTGraphicsState.rp0], this.y[paramTTGraphicsState.zp0][paramTTGraphicsState.rp0])));
        this.x[paramTTGraphicsState.zp1][i1] += arrayOfInt8[0];
        this.y[paramTTGraphicsState.zp1][i1] += arrayOfInt8[1];
        arrayOfInt10 = TTGraphicsState.getVectorComponents(paramTTGraphicsState.freedomVector);
        if (arrayOfInt10[0] != 0)
          this.touched[paramTTGraphicsState.zp1][i1][0] = 1;
        if (arrayOfInt10[1] != 0)
          this.touched[paramTTGraphicsState.zp1][i1][1] = 1;
        paramTTGraphicsState.rp1 = paramTTGraphicsState.rp0;
        paramTTGraphicsState.rp2 = i1;
        paramTTGraphicsState.rp0 = i1;
        break;
      case 60:
        if (this.printOut)
          System.out.println("ALIGNRP   - Align point to RP0");
        for (j = 0; j < paramTTGraphicsState.loop; j++)
        {
          i1 = this.stack.pop();
          i7 = TTGraphicsState.getCoordsOnVector(paramTTGraphicsState.projectionVector, this.x[paramTTGraphicsState.zp0][paramTTGraphicsState.rp0], this.y[paramTTGraphicsState.zp0][paramTTGraphicsState.rp0]);
          i11 = i7 - TTGraphicsState.getCoordsOnVector(paramTTGraphicsState.projectionVector, this.x[paramTTGraphicsState.zp1][i1], this.y[paramTTGraphicsState.zp1][i1]);
          int[] arrayOfInt13 = paramTTGraphicsState.getFVMoveforPVDistance(i11);
          this.x[paramTTGraphicsState.zp1][i1] += arrayOfInt13[0];
          this.y[paramTTGraphicsState.zp1][i1] += arrayOfInt13[1];
          arrayOfInt18 = TTGraphicsState.getVectorComponents(paramTTGraphicsState.freedomVector);
          if (arrayOfInt18[0] != 0)
            this.touched[paramTTGraphicsState.zp1][i1][0] = 1;
          if (arrayOfInt18[1] != 0)
            this.touched[paramTTGraphicsState.zp1][i1][1] = 1;
        }
        paramTTGraphicsState.loop = 1;
        break;
      case 61:
        if (this.printOut)
          System.out.println("RTDG      - Sets round state to double grid");
        paramTTGraphicsState.roundState = 8;
        paramTTGraphicsState.gridPeriod = 1.0D;
        break;
      case 62:
        if (this.printOut)
          System.out.println("MIAP0     - Move point to CVT value");
        j = this.stack.pop();
        i1 = this.stack.pop();
        i7 = this.cvt.get(j);
        i11 = TTGraphicsState.getCoordsOnVector(paramTTGraphicsState.projectionVector, this.x[paramTTGraphicsState.zp0][i1], this.y[paramTTGraphicsState.zp0][i1]);
        i15 = i7 - i11;
        arrayOfInt18 = paramTTGraphicsState.getFVMoveforPVDistance(i15);
        this.x[paramTTGraphicsState.zp0][i1] += arrayOfInt18[0];
        this.y[paramTTGraphicsState.zp0][i1] += arrayOfInt18[1];
        arrayOfInt20 = TTGraphicsState.getVectorComponents(paramTTGraphicsState.freedomVector);
        if (arrayOfInt20[0] != 0)
          this.touched[paramTTGraphicsState.zp0][i1][0] = 1;
        if (arrayOfInt20[1] != 0)
          this.touched[paramTTGraphicsState.zp0][i1][1] = 1;
        paramTTGraphicsState.rp0 = (paramTTGraphicsState.rp1 = i1);
        break;
      case 63:
        if (this.printOut)
          System.out.println("MIAP1     - Move point to CVT using cut in and round");
        j = this.stack.pop();
        i1 = this.stack.pop();
        i7 = this.cvt.get(j);
        i11 = TTGraphicsState.getCoordsOnVector(paramTTGraphicsState.projectionVector, this.x[paramTTGraphicsState.zp0][i1], this.y[paramTTGraphicsState.zp0][i1]);
        i15 = i7 - i11;
        if (Math.abs(i15) > paramTTGraphicsState.controlValueTableCutIn)
          i7 = i11;
        i7 = storeDoubleAsF26Dot6(paramTTGraphicsState.round(getDoubleFromF26Dot6(i7)));
        i15 = i7 - i11;
        arrayOfInt18 = paramTTGraphicsState.getFVMoveforPVDistance(i15);
        this.x[paramTTGraphicsState.zp0][i1] += arrayOfInt18[0];
        this.y[paramTTGraphicsState.zp0][i1] += arrayOfInt18[1];
        arrayOfInt20 = TTGraphicsState.getVectorComponents(paramTTGraphicsState.freedomVector);
        if (arrayOfInt20[0] != 0)
          this.touched[paramTTGraphicsState.zp0][i1][0] = 1;
        if (arrayOfInt20[1] != 0)
          this.touched[paramTTGraphicsState.zp0][i1][1] = 1;
        paramTTGraphicsState.rp0 = (paramTTGraphicsState.rp1 = i1);
        break;
      case 64:
        if (this.printOut)
          System.out.println("NPUSHB    - Push N bytes from IS to stack");
        paramInt2++;
        paramInt2 = readFromIS(paramArrayOfInt[paramInt2], false, paramInt2, paramArrayOfInt);
        break;
      case 65:
        if (this.printOut)
          System.out.println("NPUSHW    - Push N words from IS to stack");
        paramInt2++;
        paramInt2 = readFromIS(paramArrayOfInt[paramInt2], true, paramInt2, paramArrayOfInt);
        break;
      case 66:
        if (this.printOut)
          System.out.println("WS        - Write Store");
        j = this.stack.pop();
        i1 = this.stack.pop();
        this.storage[i1] = j;
        break;
      case 67:
        if (this.printOut)
          System.out.println("RS        - Read Store");
        j = this.stack.pop();
        this.stack.push(this.storage[j]);
        break;
      case 68:
        if (this.printOut)
          System.out.println("WCVTP     - Write Control Value Table in Pixels");
        j = this.stack.pop();
        i1 = this.stack.pop();
        this.cvt.putInPixels(i1, j);
        break;
      case 69:
        if (this.printOut)
          System.out.println("RCVT      - Read Control Value Table");
        j = this.stack.pop();
        this.stack.push(this.cvt.get(j));
        break;
      case 70:
        if (this.printOut)
          System.out.println("GC0       - Get coords on the pv");
        j = this.stack.pop();
        this.stack.push(TTGraphicsState.getCoordsOnVector(paramTTGraphicsState.projectionVector, this.x[paramTTGraphicsState.zp2][j], this.y[paramTTGraphicsState.zp2][j]));
        break;
      case 71:
        if (this.printOut)
          System.out.println("GC1       - Get original coords on the pv");
        j = this.stack.pop();
        this.stack.push(TTGraphicsState.getCoordsOnVector(paramTTGraphicsState.dualProjectionVector, this.x[(2 + paramTTGraphicsState.zp2)][j], this.y[(2 + paramTTGraphicsState.zp2)][j]));
        break;
      case 72:
        if (this.printOut)
          System.out.println("SCFS");
        j = this.stack.pop();
        i1 = this.stack.pop();
        i7 = TTGraphicsState.getCoordsOnVector(paramTTGraphicsState.projectionVector, this.x[paramTTGraphicsState.zp2][i1], this.y[paramTTGraphicsState.zp2][i1]);
        i11 = j - i7;
        int[] arrayOfInt14 = paramTTGraphicsState.getFVMoveforPVDistance(i11);
        this.x[paramTTGraphicsState.zp2][i1] += arrayOfInt14[0];
        this.y[paramTTGraphicsState.zp2][i1] += arrayOfInt14[1];
        arrayOfInt18 = TTGraphicsState.getVectorComponents(paramTTGraphicsState.freedomVector);
        if (arrayOfInt18[0] != 0)
          this.touched[paramTTGraphicsState.zp2][i1][0] = 1;
        if (arrayOfInt18[1] != 0)
          this.touched[paramTTGraphicsState.zp2][i1][1] = 1;
        break;
      case 73:
        if (this.printOut)
          System.out.println("MD0       - Measure current distance");
        j = this.stack.pop();
        i1 = this.stack.pop();
        i7 = TTGraphicsState.getCoordsOnVector(paramTTGraphicsState.projectionVector, this.x[paramTTGraphicsState.zp1][i1], this.y[paramTTGraphicsState.zp1][i1]) - TTGraphicsState.getCoordsOnVector(paramTTGraphicsState.projectionVector, this.x[paramTTGraphicsState.zp0][j], this.y[paramTTGraphicsState.zp0][j]);
        this.stack.push(i7);
        break;
      case 74:
        if (this.printOut)
          System.out.println("MD1       - Measure original distance");
        j = this.stack.pop();
        i1 = this.stack.pop();
        i7 = TTGraphicsState.getCoordsOnVector(paramTTGraphicsState.dualProjectionVector, this.x[(2 + paramTTGraphicsState.zp1)][i1], this.y[(2 + paramTTGraphicsState.zp1)][i1]) - TTGraphicsState.getCoordsOnVector(paramTTGraphicsState.dualProjectionVector, this.x[(2 + paramTTGraphicsState.zp0)][j], this.y[(2 + paramTTGraphicsState.zp0)][j]);
        this.stack.push(i7);
        break;
      case 75:
        if (this.printOut)
          System.out.println("MPPEM     - Measure pixels per em in the direction of the projection vector");
        j = TTGraphicsState.getCoordsOnVector(paramTTGraphicsState.projectionVector, (int)(this.ppem * 64.0D), (int)(this.ppem * 64.0D)) / 64;
        if (j < 0)
          j = -j;
        this.stack.push(j);
        break;
      case 76:
        if (this.printOut)
          System.out.println("MPS");
        this.stack.push((int)(this.ptSize * 64.0D));
        break;
      case 77:
        if (this.printOut)
          System.out.println("FLIPON    - Sets autoflip to true");
        paramTTGraphicsState.autoFlip = true;
        break;
      case 78:
        if (this.printOut)
          System.out.println("FLIPOFF   - Sets autoflip to false");
        paramTTGraphicsState.autoFlip = false;
        break;
      case 79:
        if (this.printOut)
          System.out.println("DEBUG     - Shouldn't be in live fonts");
        this.stack.pop();
        break;
      case 80:
        if (this.printOut)
          System.out.println("LT        - Less Than");
        j = this.stack.pop();
        i1 = this.stack.pop();
        if (i1 < j)
          this.stack.push(1);
        else
          this.stack.push(0);
        break;
      case 81:
        if (this.printOut)
          System.out.println("LTEQ      - Less Than or Equal");
        j = this.stack.pop();
        i1 = this.stack.pop();
        if (i1 <= j)
          this.stack.push(1);
        else
          this.stack.push(0);
        break;
      case 82:
        if (this.printOut)
          System.out.println("GT        - Greater Than");
        j = this.stack.pop();
        i1 = this.stack.pop();
        if (i1 > j)
          this.stack.push(1);
        else
          this.stack.push(0);
        break;
      case 83:
        if (this.printOut)
          System.out.println("GTEQ      - Greater Than or Equal");
        j = this.stack.pop();
        i1 = this.stack.pop();
        if (i1 >= j)
          this.stack.push(1);
        else
          this.stack.push(0);
        break;
      case 84:
        if (this.printOut)
          System.out.println("EQ        - Equal");
        j = this.stack.pop();
        i1 = this.stack.pop();
        if (i1 == j)
          this.stack.push(1);
        else
          this.stack.push(0);
        break;
      case 85:
        if (this.printOut)
          System.out.println("NEQ       - Not Equal");
        j = this.stack.pop();
        i1 = this.stack.pop();
        if (i1 != j)
          this.stack.push(1);
        else
          this.stack.push(0);
        break;
      case 86:
        if (this.printOut)
          System.out.println("ODD       - Rounds, truncates, and returns if odd.");
        j = this.stack.pop();
        j = storeDoubleAsF26Dot6(paramTTGraphicsState.round(getDoubleFromF26Dot6(j)));
        j = (j >> 6) % 2;
        this.stack.push(j);
        break;
      case 87:
        if (this.printOut)
          System.out.println("EVEN      - Rounds, truncates, and returns if even");
        j = this.stack.pop();
        j = storeDoubleAsF26Dot6(paramTTGraphicsState.round(getDoubleFromF26Dot6(j)));
        j = ((j >> 6) + 1) % 2;
        this.stack.push(j);
        break;
      case 88:
        if (this.printOut)
          System.out.println("IF        - IF");
        j = this.stack.pop() != 0 ? 1 : 0;
        if (j == 0)
        {
          i1 = 0;
          i7 = 0;
          do
          {
            if ((i1 == 89) && (i7 != 0))
              i7--;
            paramInt2++;
            i1 = paramArrayOfInt[paramInt2];
            if (i1 == 88)
              i7++;
            if (i1 == 64)
            {
              paramInt2++;
              paramInt2 += paramArrayOfInt[paramInt2];
            }
            else if (i1 == 65)
            {
              paramInt2++;
              paramInt2 += paramArrayOfInt[paramInt2] * 2;
            }
            else if ((i1 >= 176) && (i1 <= 183))
            {
              paramInt2 += i1 + 1 - 176;
            }
            else if ((i1 >= 184) && (i1 <= 191))
            {
              paramInt2 += (i1 + 1 - 184) * 2;
            }
          }
          while (((i1 != 27) && (i1 != 89)) || (i7 != 0));
        }
        break;
      case 89:
        if ((goto 15152) && (this.printOut))
          System.out.println("EIF       - End IF");
        break;
      case 90:
        if (this.printOut)
          System.out.println("AND       - Logical AND");
        j = this.stack.pop() != 0 ? 1 : 0;
        i1 = this.stack.pop() != 0 ? 1 : 0;
        if ((i1 != 0) && (j != 0))
          this.stack.push(1);
        else
          this.stack.push(0);
        break;
      case 91:
        if (this.printOut)
          System.out.println("OR        - Logical OR");
        j = this.stack.pop() != 0 ? 1 : 0;
        i1 = this.stack.pop() != 0 ? 1 : 0;
        if ((i1 != 0) || (j != 0))
          this.stack.push(1);
        else
          this.stack.push(0);
        break;
      case 92:
        if (this.printOut)
          System.out.println("NOT       - Logical NOT");
        j = this.stack.pop() != 0 ? 1 : 0;
        if (j == 0)
          this.stack.push(1);
        else
          this.stack.push(0);
        break;
      case 93:
        if (this.printOut)
          System.out.println("DELTAP1   - Delta exception p1");
        j = this.stack.pop();
        for (i1 = 0; i1 < j; i1++)
        {
          i7 = this.stack.pop();
          i11 = this.stack.pop();
          i16 = paramTTGraphicsState.deltaBase + (i11 >> 4);
          if (i16 == this.ppem)
          {
            i21 = (i11 & 0xF) - 7;
            if (i21 <= 0)
              i21 -= 1;
            i24 = storeDoubleAsF26Dot6(i21 * (1.0D / Math.pow(2.0D, paramTTGraphicsState.deltaShift)));
            arrayOfInt21 = paramTTGraphicsState.getFVMoveforPVDistance(i24);
            this.x[paramTTGraphicsState.zp0][i7] += arrayOfInt21[0];
            this.y[paramTTGraphicsState.zp0][i7] += arrayOfInt21[1];
            arrayOfInt23 = TTGraphicsState.getVectorComponents(paramTTGraphicsState.freedomVector);
            if (arrayOfInt23[0] != 0)
              this.touched[paramTTGraphicsState.zp0][i7][0] = 1;
            if (arrayOfInt23[1] != 0)
              this.touched[paramTTGraphicsState.zp0][i7][1] = 1;
          }
        }
        break;
      case 94:
        if (this.printOut)
          System.out.println("SDB       - Set delta base");
        paramTTGraphicsState.deltaBase = this.stack.pop();
        break;
      case 95:
        if (this.printOut)
          System.out.println("SDS       - Set delta shift");
        paramTTGraphicsState.deltaShift = this.stack.pop();
        break;
      case 96:
        if (this.printOut)
          System.out.println("ADD       - Add two F26Dot6 numbers");
        this.stack.push(this.stack.pop() + this.stack.pop());
        break;
      case 97:
        if (this.printOut)
          System.out.println("SUB       - Subtract a number from another");
        j = this.stack.pop();
        i1 = this.stack.pop();
        this.stack.push(i1 - j);
        break;
      case 98:
        if (this.printOut)
          System.out.println("DIV       - Divide two F26Dot6 numbers");
        j = this.stack.pop();
        i1 = this.stack.pop();
        if (j != 0)
          this.stack.push(i1 * 64 / j);
        else
          this.stack.push(0);
        break;
      case 99:
        if (this.printOut)
          System.out.println("MUL       - Multiply two F26Dot6 numbers");
        j = this.stack.pop();
        i1 = this.stack.pop();
        this.stack.push(j * i1 / 64);
        break;
      case 100:
        if (this.printOut)
          System.out.println("ABS       - Return the absolute value of a F26Dot6 number");
        j = this.stack.pop();
        if (j < 0)
          j = -j;
        this.stack.push(j);
        break;
      case 101:
        if (this.printOut)
          System.out.println("NEG       - Negate a number");
        this.stack.push(-this.stack.pop());
        break;
      case 102:
        if (this.printOut)
          System.out.println("FLOOR     - Round a number down if it has a fractional component");
        this.stack.push(this.stack.pop() >> 6 << 6);
        break;
      case 103:
        if (this.printOut)
          System.out.println("CEILING   - Round a number up if it has a fractional component");
        j = this.stack.pop();
        if ((j & 0x3F) != 0)
          j = (j >> 6) + 1 << 6;
        this.stack.push(j);
        break;
      case 104:
        if (this.printOut)
          System.out.println("ROUND00   - Round a number");
        j = this.stack.pop();
        j = engineCompensation(j, 0);
        d1 = getDoubleFromF26Dot6(j);
        this.stack.push(storeDoubleAsF26Dot6(paramTTGraphicsState.round(d1)));
        break;
      case 105:
        if (this.printOut)
          System.out.println("ROUND01   - Round a number");
        j = this.stack.pop();
        j = engineCompensation(j, 1);
        d1 = getDoubleFromF26Dot6(j);
        this.stack.push(storeDoubleAsF26Dot6(paramTTGraphicsState.round(d1)));
        break;
      case 106:
        if (this.printOut)
          System.out.println("ROUND10   - Round a number");
        j = this.stack.pop();
        j = engineCompensation(j, 2);
        d1 = getDoubleFromF26Dot6(j);
        this.stack.push(storeDoubleAsF26Dot6(paramTTGraphicsState.round(d1)));
        break;
      case 107:
        if (this.printOut)
          System.out.println("ROUND11   - Round a number");
        j = this.stack.pop();
        j = engineCompensation(j, 3);
        d1 = getDoubleFromF26Dot6(j);
        this.stack.push(storeDoubleAsF26Dot6(paramTTGraphicsState.round(d1)));
        break;
      case 108:
        if (this.printOut)
          System.out.println("NROUND00  - Compensate for engine characteristics");
        this.stack.push(engineCompensation(this.stack.pop(), 0));
        break;
      case 109:
        if (this.printOut)
          System.out.println("NROUND01  - Compensate for engine characteristics");
        this.stack.push(engineCompensation(this.stack.pop(), 1));
        break;
      case 110:
        if (this.printOut)
          System.out.println("NROUND10  - Compensate for engine characteristics");
        this.stack.push(engineCompensation(this.stack.pop(), 2));
        break;
      case 111:
        if (this.printOut)
          System.out.println("NROUND11  - Compensate for engine characteristics");
        this.stack.push(engineCompensation(this.stack.pop(), 3));
        break;
      case 112:
        if (this.printOut)
          System.out.println("WCVTF");
        j = this.stack.pop();
        i2 = this.stack.pop();
        this.cvt.putInFUnits(i2, j);
        break;
      case 113:
        if (this.printOut)
          System.out.println("DELTAP2   - Delta exception p2");
        j = this.stack.pop();
        for (i2 = 0; i2 < j; i2++)
        {
          i7 = this.stack.pop();
          i11 = this.stack.pop();
          i16 = paramTTGraphicsState.deltaBase + 16 + (i11 >> 4);
          if (i16 == this.ppem)
          {
            i21 = (i11 & 0xF) - 7;
            if (i21 <= 0)
              i21 -= 1;
            i24 = storeDoubleAsF26Dot6(i21 * (1.0D / Math.pow(2.0D, paramTTGraphicsState.deltaShift)));
            arrayOfInt21 = paramTTGraphicsState.getFVMoveforPVDistance(i24);
            this.x[paramTTGraphicsState.zp0][i7] += arrayOfInt21[0];
            this.y[paramTTGraphicsState.zp0][i7] += arrayOfInt21[1];
            arrayOfInt23 = TTGraphicsState.getVectorComponents(paramTTGraphicsState.freedomVector);
            if (arrayOfInt23[0] != 0)
              this.touched[paramTTGraphicsState.zp0][i7][0] = 1;
            if (arrayOfInt23[1] != 0)
              this.touched[paramTTGraphicsState.zp0][i7][1] = 1;
          }
        }
        break;
      case 114:
        if (this.printOut)
          System.out.println("DELTAP3   - Delta exception p3");
        j = this.stack.pop();
        for (i2 = 0; i2 < j; i2++)
        {
          i7 = this.stack.pop();
          i11 = this.stack.pop();
          i16 = paramTTGraphicsState.deltaBase + 32 + (i11 >> 4);
          if (i16 == this.ppem)
          {
            i21 = (i11 & 0xF) - 7;
            if (i21 <= 0)
              i21 -= 1;
            i24 = storeDoubleAsF26Dot6(i21 * (1.0D / Math.pow(2.0D, paramTTGraphicsState.deltaShift)));
            arrayOfInt21 = paramTTGraphicsState.getFVMoveforPVDistance(i24);
            this.x[paramTTGraphicsState.zp0][i7] += arrayOfInt21[0];
            this.y[paramTTGraphicsState.zp0][i7] += arrayOfInt21[1];
            arrayOfInt23 = TTGraphicsState.getVectorComponents(paramTTGraphicsState.freedomVector);
            if (arrayOfInt23[0] != 0)
              this.touched[paramTTGraphicsState.zp0][i7][0] = 1;
            if (arrayOfInt23[1] != 0)
              this.touched[paramTTGraphicsState.zp0][i7][1] = 1;
          }
        }
        break;
      case 115:
        if (this.printOut)
          System.out.println("DELTAC1   - Delta exception c1");
        j = this.stack.pop();
        for (i2 = 0; i2 < j; i2++)
        {
          i7 = this.stack.pop();
          i11 = this.stack.pop();
          i16 = paramTTGraphicsState.deltaBase + (i11 >> 4);
          if (i16 == this.ppem)
          {
            i21 = (i11 & 0xF) - 7;
            if (i21 <= 0)
              i21 -= 1;
            i24 = storeDoubleAsF26Dot6(i21 * (1.0D / Math.pow(2.0D, paramTTGraphicsState.deltaShift)));
            i27 = this.cvt.get(i7);
            i27 += i24;
            this.cvt.putInPixels(i7, i27);
          }
        }
        break;
      case 116:
        if (this.printOut)
          System.out.println("DELTAC2   - Delta exception c2");
        j = this.stack.pop();
        for (i2 = 0; i2 < j; i2++)
        {
          i7 = this.stack.pop();
          i11 = this.stack.pop();
          i16 = paramTTGraphicsState.deltaBase + 16 + (i11 >> 4);
          if (i16 == this.ppem)
          {
            i21 = (i11 & 0xF) - 7;
            if (i21 <= 0)
              i21 -= 1;
            i24 = storeDoubleAsF26Dot6(i21 * (1.0D / Math.pow(2.0D, paramTTGraphicsState.deltaShift)));
            i27 = this.cvt.get(i7);
            i27 += i24;
            this.cvt.putInPixels(i7, i27);
          }
        }
        break;
      case 117:
        if (this.printOut)
          System.out.println("DELTAC3   - Delta exception c3");
        j = this.stack.pop();
        for (i2 = 0; i2 < j; i2++)
        {
          i7 = this.stack.pop();
          i11 = this.stack.pop();
          i16 = paramTTGraphicsState.deltaBase + 32 + (i11 >> 4);
          if (i16 == this.ppem)
          {
            i21 = (i11 & 0xF) - 7;
            if (i21 <= 0)
              i21 -= 1;
            i24 = storeDoubleAsF26Dot6(i21 * (1.0D / Math.pow(2.0D, paramTTGraphicsState.deltaShift)));
            i27 = this.cvt.get(i7);
            i27 += i24;
            this.cvt.putInPixels(i7, i27);
          }
        }
        break;
      case 118:
        if (this.printOut)
          System.out.println("SROUND    - Sets the roundState specifically");
        paramTTGraphicsState.roundState = this.stack.pop();
        paramTTGraphicsState.gridPeriod = 1.0D;
        break;
      case 119:
        if (this.printOut)
          System.out.println("S45ROUND  - Sets the round state for working at 45degrees");
        paramTTGraphicsState.roundState = this.stack.pop();
        paramTTGraphicsState.gridPeriod = 0.7071067811865476D;
        break;
      case 120:
        if (this.printOut)
          System.out.println("JROT      - Jump Relative On True");
        j = this.stack.pop() != 0 ? 1 : 0;
        i2 = this.stack.pop();
        if (j != 0)
          paramInt2 = paramInt2 + i2 - 1;
        break;
      case 121:
        if (this.printOut)
          System.out.println("JROF      - Jump Relative On False");
        j = this.stack.pop() != 0 ? 1 : 0;
        i2 = this.stack.pop();
        if (j == 0)
          paramInt2 = paramInt2 + i2 - 1;
        break;
      case 122:
        if (this.printOut)
          System.out.println("ROFF      - Set round state to off");
        paramTTGraphicsState.roundState = -1;
        break;
      case 124:
        if (this.printOut)
          System.out.println("RUTG      - Set round state to up to grid");
        paramTTGraphicsState.roundState = 64;
        paramTTGraphicsState.gridPeriod = 1.0D;
        break;
      case 125:
        if (this.printOut)
          System.out.println("RDTG      - Set round state to down to grid");
        paramTTGraphicsState.roundState = 68;
        paramTTGraphicsState.gridPeriod = 1.0D;
        break;
      case 126:
        if (this.printOut)
          System.out.println("SANGW     - deprecated");
        this.stack.pop();
        break;
      case 127:
        if (this.printOut)
          System.out.println("AA        - deprecated");
        this.stack.pop();
        break;
      case 128:
        if (this.printOut)
          System.out.println("FLIPPT    - Flips a number of points on/off the curve");
        for (j = 0; j < paramTTGraphicsState.loop; j++)
        {
          i2 = this.stack.pop();
          this.curve[paramTTGraphicsState.zp0][i2] = (this.curve[paramTTGraphicsState.zp0][i2] == 0 ? 1 : 0);
        }
        paramTTGraphicsState.loop = 1;
        break;
      case 129:
        if (this.printOut)
          System.out.println("FLIPRGON  - Flips a range of points onto the curve");
        j = this.stack.pop();
        i2 = this.stack.pop();
        for (i7 = i2; i7 <= j; i7++)
          this.curve[paramTTGraphicsState.zp0][i7] = 1;
        break;
      case 130:
        if (this.printOut)
          System.out.println("FLIPRGOFF - Flips a range of points off the curve");
        j = this.stack.pop();
        i2 = this.stack.pop();
        for (i7 = i2; i7 <= j; i7++)
          this.curve[paramTTGraphicsState.zp0][i7] = 0;
        break;
      case 133:
        if (this.printOut)
          System.out.println("SCANCTRL  - We don't scan convert, so only pops a value");
        this.stack.pop();
        break;
      case 134:
        if (this.printOut)
          System.out.println("SDPVTL0   - Sets dual projection vector to line");
        j = this.stack.pop();
        i2 = this.stack.pop();
        d3 = getDoubleFromF26Dot6(this.x[paramTTGraphicsState.zp2][j] - this.x[paramTTGraphicsState.zp1][i2]);
        d5 = getDoubleFromF26Dot6(this.y[paramTTGraphicsState.zp2][j] - this.y[paramTTGraphicsState.zp1][i2]);
        d8 = getDoubleFromF26Dot6(this.x[(2 + paramTTGraphicsState.zp2)][j] - this.x[(2 + paramTTGraphicsState.zp1)][i2]);
        d9 = getDoubleFromF26Dot6(this.y[(2 + paramTTGraphicsState.zp2)][j] - this.y[(2 + paramTTGraphicsState.zp1)][i2]);
        d10 = Math.sqrt(d3 * d3 + d5 * d5);
        d11 = Math.sqrt(d8 * d8 + d9 * d9);
        d3 /= d10;
        d5 /= d10;
        d8 /= d11;
        d9 /= d11;
        paramTTGraphicsState.projectionVector = TTGraphicsState.createVector(storeDoubleAsF2Dot14(d3), storeDoubleAsF2Dot14(d5));
        paramTTGraphicsState.dualProjectionVector = TTGraphicsState.createVector(storeDoubleAsF2Dot14(d8), storeDoubleAsF2Dot14(d9));
        break;
      case 135:
        if (this.printOut)
          System.out.println("SDPVTL1   - Sets dual projection vector perpendicular to line");
        j = this.stack.pop();
        i2 = this.stack.pop();
        d3 = getDoubleFromF26Dot6(this.x[paramTTGraphicsState.zp2][j] - this.x[paramTTGraphicsState.zp1][i2]);
        d5 = getDoubleFromF26Dot6(this.y[paramTTGraphicsState.zp2][j] - this.y[paramTTGraphicsState.zp1][i2]);
        d8 = getDoubleFromF26Dot6(this.x[(2 + paramTTGraphicsState.zp2)][j] - this.x[(2 + paramTTGraphicsState.zp1)][i2]);
        d9 = getDoubleFromF26Dot6(this.y[(2 + paramTTGraphicsState.zp2)][j] - this.y[(2 + paramTTGraphicsState.zp1)][i2]);
        d10 = Math.sqrt(d3 * d3 + d5 * d5);
        d11 = Math.sqrt(d8 * d8 + d9 * d9);
        d3 /= d10;
        d5 /= d10;
        d8 /= d11;
        d9 /= d11;
        paramTTGraphicsState.projectionVector = TTGraphicsState.createVector(storeDoubleAsF2Dot14(d5), storeDoubleAsF2Dot14(-d3));
        paramTTGraphicsState.dualProjectionVector = TTGraphicsState.createVector(storeDoubleAsF2Dot14(d9), storeDoubleAsF2Dot14(-d8));
        break;
      case 136:
        if (this.printOut)
          System.out.println("GETINFO   - Gets info about current glyph & font engine");
        j = this.stack.pop();
        i2 = 0;
        if ((j & 0x1) == 1)
          i2 += 3;
        this.stack.push(i2);
        break;
      case 137:
        if (this.printOut)
          System.out.println("IDEF      - Define an instruction");
        j = this.stack.pop();
        i2 = paramInt2;
        do
        {
          paramInt2++;
          i8 = paramArrayOfInt[paramInt2];
        }
        while (i8 != 45);
        i11 = paramInt2 - i2 - 1;
        paramInt2 = i2;
        int[] arrayOfInt15 = new int[i11];
        for (i21 = 0; i21 < i11; i21++)
        {
          paramInt2++;
          arrayOfInt15[i21] = paramArrayOfInt[paramInt2];
        }
        this.instructions.put(Integer.valueOf(j), arrayOfInt15);
        paramInt2++;
        break;
      case 138:
        if (this.printOut)
          System.out.println("ROLL      - Roll the top three stack elements");
        j = this.stack.pop();
        i2 = this.stack.pop();
        i8 = this.stack.pop();
        this.stack.push(i2);
        this.stack.push(j);
        this.stack.push(i8);
        break;
      case 139:
        if (this.printOut)
          System.out.println("MAX       - Returns the maximum of two values");
        j = this.stack.pop();
        i2 = this.stack.pop();
        if (j > i2)
          this.stack.push(j);
        else
          this.stack.push(i2);
        break;
      case 140:
        if (this.printOut)
          System.out.println("MIN       - Returns the minimum of two values");
        j = this.stack.pop();
        i2 = this.stack.pop();
        if (j < i2)
          this.stack.push(j);
        else
          this.stack.push(i2);
        break;
      case 141:
        if (this.printOut)
          System.out.println("SCANTYPE  - We don't scan convert, so only pops a value");
        this.stack.pop();
        break;
      case 142:
        if (this.printOut)
          System.out.println("INSTCTRL  - Allows for setting flags to do with glyph execution");
        j = this.stack.pop();
        i2 = this.stack.pop();
        if (j == 1)
          paramTTGraphicsState.instructControl = i2;
        else if (j == 2)
          this.useDefaultGS = (i2 == 2);
        break;
      case 176:
        if (this.printOut)
          System.out.println("PUSHB1    - Push bytes from IS to stack");
        paramInt2 = readFromIS(1, false, paramInt2, paramArrayOfInt);
        break;
      case 177:
        if (this.printOut)
          System.out.println("PUSHB2    - Push bytes from IS to stack");
        paramInt2 = readFromIS(2, false, paramInt2, paramArrayOfInt);
        break;
      case 178:
        if (this.printOut)
          System.out.println("PUSHB3    - Push bytes from IS to stack");
        paramInt2 = readFromIS(3, false, paramInt2, paramArrayOfInt);
        break;
      case 179:
        if (this.printOut)
          System.out.println("PUSHB4    - Push bytes from IS to stack");
        paramInt2 = readFromIS(4, false, paramInt2, paramArrayOfInt);
        break;
      case 180:
        if (this.printOut)
          System.out.println("PUSHB5   - Push bytes from IS to stack");
        paramInt2 = readFromIS(5, false, paramInt2, paramArrayOfInt);
        break;
      case 181:
        if (this.printOut)
          System.out.println("PUSHB6   - Push bytes from IS to stack");
        paramInt2 = readFromIS(6, false, paramInt2, paramArrayOfInt);
        break;
      case 182:
        if (this.printOut)
          System.out.println("PUSHB7   - Push bytes from IS to stack");
        paramInt2 = readFromIS(7, false, paramInt2, paramArrayOfInt);
        break;
      case 183:
        if (this.printOut)
          System.out.println("PUSHB8   - Push bytes from IS to stack");
        paramInt2 = readFromIS(8, false, paramInt2, paramArrayOfInt);
        break;
      case 184:
        if (this.printOut)
          System.out.println("PUSHW1    - Push words from IS to stack");
        paramInt2 = readFromIS(1, true, paramInt2, paramArrayOfInt);
        break;
      case 185:
        if (this.printOut)
          System.out.println("PUSHW2    - Push words from IS to stack");
        paramInt2 = readFromIS(2, true, paramInt2, paramArrayOfInt);
        break;
      case 186:
        if (this.printOut)
          System.out.println("PUSHW3    - Push words from IS to stack");
        paramInt2 = readFromIS(3, true, paramInt2, paramArrayOfInt);
        break;
      case 187:
        if (this.printOut)
          System.out.println("PUSHW4    - Push words from IS to stack");
        paramInt2 = readFromIS(4, true, paramInt2, paramArrayOfInt);
        break;
      case 188:
        if (this.printOut)
          System.out.println("PUSHW5    - Push words from IS to stack");
        paramInt2 = readFromIS(5, true, paramInt2, paramArrayOfInt);
        break;
      case 189:
        if (this.printOut)
          System.out.println("PUSHW6    - Push words from IS to stack");
        paramInt2 = readFromIS(6, true, paramInt2, paramArrayOfInt);
        break;
      case 190:
        if (this.printOut)
          System.out.println("PUSHW7    - Push words from IS to stack");
        paramInt2 = readFromIS(7, true, paramInt2, paramArrayOfInt);
        break;
      case 191:
        if (this.printOut)
          System.out.println("PUSHW8    - Push words from IS to stack");
        paramInt2 = readFromIS(8, true, paramInt2, paramArrayOfInt);
        break;
      case 40:
      case 123:
      case 131:
      case 132:
      case 143:
      case 144:
      case 145:
      case 146:
      case 147:
      case 148:
      case 149:
      case 150:
      case 151:
      case 152:
      case 153:
      case 154:
      case 155:
      case 156:
      case 157:
      case 158:
      case 159:
      case 160:
      case 161:
      case 162:
      case 163:
      case 164:
      case 165:
      case 166:
      case 167:
      case 168:
      case 169:
      case 170:
      case 171:
      case 172:
      case 173:
      case 174:
      case 175:
      default:
        int i17;
        int i25;
        int i30;
        int[] arrayOfInt26;
        if ((paramInt1 >= 192) && (paramInt1 < 224))
        {
          j = paramInt1 - 192;
          if (this.printOut)
            System.out.println("MDRP      - Move direct relative point (" + Integer.toBinaryString(j) + ')');
          i2 = 0;
          i8 = 0;
          i11 = 0;
          if ((j & 0x10) == 16)
            i2 = 1;
          if ((j & 0x8) == 8)
            i8 = 1;
          if ((j & 0x4) == 4)
            i11 = 1;
          i17 = j & 0x3;
          i21 = this.stack.pop();
          i25 = TTGraphicsState.getCoordsOnVector(paramTTGraphicsState.dualProjectionVector, this.x[(2 + paramTTGraphicsState.zp1)][i21], this.y[(2 + paramTTGraphicsState.zp1)][i21]) - TTGraphicsState.getCoordsOnVector(paramTTGraphicsState.dualProjectionVector, this.x[(2 + paramTTGraphicsState.zp0)][paramTTGraphicsState.rp0], this.y[(2 + paramTTGraphicsState.zp0)][paramTTGraphicsState.rp0]);
          if (Math.abs(i25) < paramTTGraphicsState.singleWidthCutIn)
            if (i25 > 0)
              i25 = paramTTGraphicsState.singleWidthValue;
            else
              i25 = -paramTTGraphicsState.singleWidthValue;
          i25 = engineCompensation(i25, i17);
          if (i11 != 0)
            i25 = storeDoubleAsF26Dot6(paramTTGraphicsState.round(getDoubleFromF26Dot6(i25)));
          if ((i8 != 0) && (Math.abs(i25) < paramTTGraphicsState.minimumDistance))
            if (i25 < 0)
              i25 = -paramTTGraphicsState.minimumDistance;
            else
              i25 = paramTTGraphicsState.minimumDistance;
          i27 = TTGraphicsState.getCoordsOnVector(paramTTGraphicsState.projectionVector, this.x[paramTTGraphicsState.zp0][paramTTGraphicsState.rp0], this.y[paramTTGraphicsState.zp0][paramTTGraphicsState.rp0]) + i25;
          i30 = i27 - TTGraphicsState.getCoordsOnVector(paramTTGraphicsState.projectionVector, this.x[paramTTGraphicsState.zp1][i21], this.y[paramTTGraphicsState.zp1][i21]);
          int[] arrayOfInt24 = paramTTGraphicsState.getFVMoveforPVDistance(i30);
          this.x[paramTTGraphicsState.zp1][i21] += arrayOfInt24[0];
          this.y[paramTTGraphicsState.zp1][i21] += arrayOfInt24[1];
          arrayOfInt26 = TTGraphicsState.getVectorComponents(paramTTGraphicsState.freedomVector);
          if (arrayOfInt26[0] != 0)
            this.touched[paramTTGraphicsState.zp1][i21][0] = 1;
          if (arrayOfInt26[1] != 0)
            this.touched[paramTTGraphicsState.zp1][i21][1] = 1;
          paramTTGraphicsState.rp1 = paramTTGraphicsState.rp0;
          paramTTGraphicsState.rp2 = i21;
          if (i2 != 0)
            paramTTGraphicsState.rp0 = i21;
        }
        else if ((paramInt1 >= 224) && (paramInt1 <= 255))
        {
          j = paramInt1 - 224;
          if (this.printOut)
            System.out.println("MIRP      - Move Indirect Relative Point(" + Integer.toBinaryString(j) + ')');
          i2 = 0;
          i8 = 0;
          i11 = 0;
          if ((j & 0x10) == 16)
            i2 = 1;
          if ((j & 0x8) == 8)
            i8 = 1;
          if ((j & 0x4) == 4)
            i11 = 1;
          i17 = j & 0x3;
          i21 = this.cvt.get(this.stack.pop());
          i25 = this.stack.pop();
          i27 = TTGraphicsState.getCoordsOnVector(paramTTGraphicsState.dualProjectionVector, this.x[(2 + paramTTGraphicsState.zp1)][i25], this.y[(2 + paramTTGraphicsState.zp1)][i25]) - TTGraphicsState.getCoordsOnVector(paramTTGraphicsState.dualProjectionVector, this.x[(2 + paramTTGraphicsState.zp0)][paramTTGraphicsState.rp0], this.y[(2 + paramTTGraphicsState.zp0)][paramTTGraphicsState.rp0]);
          if (Math.abs(i27 - paramTTGraphicsState.singleWidthValue) < paramTTGraphicsState.singleWidthCutIn)
            i27 = paramTTGraphicsState.singleWidthValue;
          if (i11 != 0)
          {
            if ((paramTTGraphicsState.autoFlip) && (((i27 < 0) && (i21 > 0)) || ((i27 > 0) && (i21 < 0))))
              i21 = -i21;
            if (Math.abs(i27 - i21) < paramTTGraphicsState.controlValueTableCutIn)
              i27 = i21;
          }
          i27 = engineCompensation(i27, i17);
          if (i11 != 0)
            i27 = paramTTGraphicsState.round(i27);
          if ((i8 != 0) && (Math.abs(i27) < paramTTGraphicsState.minimumDistance))
            if (i27 > 0)
              i27 = paramTTGraphicsState.minimumDistance;
            else
              i27 = -paramTTGraphicsState.minimumDistance;
          i30 = TTGraphicsState.getCoordsOnVector(paramTTGraphicsState.projectionVector, this.x[paramTTGraphicsState.zp0][paramTTGraphicsState.rp0], this.y[paramTTGraphicsState.zp0][paramTTGraphicsState.rp0]) + i27;
          int i32 = i30 - TTGraphicsState.getCoordsOnVector(paramTTGraphicsState.projectionVector, this.x[paramTTGraphicsState.zp1][i25], this.y[paramTTGraphicsState.zp1][i25]);
          arrayOfInt26 = paramTTGraphicsState.getFVMoveforPVDistance(i32);
          this.x[paramTTGraphicsState.zp1][i25] += arrayOfInt26[0];
          this.y[paramTTGraphicsState.zp1][i25] += arrayOfInt26[1];
          arrayOfInt27 = TTGraphicsState.getVectorComponents(paramTTGraphicsState.freedomVector);
          if (arrayOfInt27[0] != 0)
            this.touched[paramTTGraphicsState.zp1][i25][0] = 1;
          if (arrayOfInt27[1] != 0)
            this.touched[paramTTGraphicsState.zp1][i25][1] = 1;
          paramTTGraphicsState.rp1 = paramTTGraphicsState.rp0;
          paramTTGraphicsState.rp2 = i25;
          if (i2 != 0)
            paramTTGraphicsState.rp0 = i25;
        }
        else if (this.instructions.containsKey(Integer.valueOf(paramInt1)))
        {
          if (this.printOut)
            System.out.println("I 0x" + Integer.toHexString(paramInt1) + "    - Custom Instruction");
          execute((int[])this.instructions.get(Integer.valueOf(paramInt1)), paramTTGraphicsState);
          if (this.printOut)
            System.out.println("I 0x" + Integer.toHexString(paramInt1) + " finished");
        }
        break;
      }
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception: " + localException.getMessage());
      TTGlyph.useHinting = false;
      TTGlyph.redecodePage = true;
    }
    return paramInt2;
  }

  private void interpolateUntouchedPoints(int paramInt)
  {
    boolean[] arrayOfBoolean = new boolean[this.touched[1].length];
    int[] arrayOfInt1;
    int[] arrayOfInt2;
    if (paramInt == 49)
    {
      arrayOfInt1 = this.x[1];
      arrayOfInt2 = this.x[3];
      for (i = 0; i < this.touched[1].length; i++)
        arrayOfBoolean[i] = this.touched[1][i][0];
    }
    else
    {
      arrayOfInt1 = this.y[1];
      arrayOfInt2 = this.y[3];
      for (i = 0; i < this.touched[1].length; i++)
        arrayOfBoolean[i] = this.touched[1][i][1];
    }
    int i = 0;
    while (i < arrayOfInt1.length)
    {
      int[] arrayOfInt3 = new int[arrayOfInt2.length];
      int j = 0;
      int k = 0;
      do
      {
        if (arrayOfBoolean[(i + k)] != 0)
        {
          arrayOfInt3[j] = (i + k);
          j++;
        }
        k++;
      }
      while ((this.contour[1][(i + k - 1)] == 0) && (i + k < this.contour[1].length));
      int m;
      if (j == 1)
      {
        m = arrayOfInt1[arrayOfInt3[0]] - arrayOfInt2[arrayOfInt3[0]];
        for (int n = i; n < i + k; n++)
          if (arrayOfBoolean[n] == 0)
            arrayOfInt1[n] += m;
      }
      else if (j > 1)
      {
        for (m = 0; m < j; m++)
          if (m + 1 >= j)
          {
            interpolateRange(arrayOfInt3[m] + 1, i + k - 1, arrayOfInt3[m], arrayOfInt3[0], arrayOfInt1, arrayOfInt2);
            interpolateRange(i, arrayOfInt3[0] - 1, arrayOfInt3[m], arrayOfInt3[0], arrayOfInt1, arrayOfInt2);
          }
          else
          {
            interpolateRange(arrayOfInt3[m] + 1, arrayOfInt3[(m + 1)] - 1, arrayOfInt3[m], arrayOfInt3[(m + 1)], arrayOfInt1, arrayOfInt2);
          }
      }
      i += k;
    }
  }

  private static void interpolateRange(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt1, int[] paramArrayOfInt2)
  {
    int i;
    int j;
    if (paramArrayOfInt2[paramInt4] < paramArrayOfInt2[paramInt3])
    {
      i = paramInt4;
      j = paramInt3;
    }
    else
    {
      i = paramInt3;
      j = paramInt4;
    }
    for (int k = paramInt1; k <= paramInt2; k++)
      if (paramArrayOfInt2[k] < paramArrayOfInt2[i])
      {
        paramArrayOfInt1[k] += paramArrayOfInt1[i] - paramArrayOfInt2[i];
      }
      else if (paramArrayOfInt2[k] > paramArrayOfInt2[j])
      {
        paramArrayOfInt1[k] += paramArrayOfInt1[j] - paramArrayOfInt2[j];
      }
      else
      {
        double d = (paramArrayOfInt2[k] - paramArrayOfInt2[i]) / (paramArrayOfInt2[j] - paramArrayOfInt2[i]);
        paramArrayOfInt1[i] += (int)(d * (paramArrayOfInt1[j] - paramArrayOfInt1[i]));
      }
  }

  private static int engineCompensation(int paramInt1, int paramInt2)
  {
    return paramInt1;
  }

  private int readFromIS(int paramInt1, boolean paramBoolean, int paramInt2, int[] paramArrayOfInt)
  {
    for (int i = 0; i < paramInt1; i++)
    {
      paramInt2++;
      int j;
      if (!paramBoolean)
      {
        j = paramArrayOfInt[paramInt2];
      }
      else
      {
        int k = paramArrayOfInt[paramInt2];
        paramInt2++;
        int m = paramArrayOfInt[paramInt2];
        j = getIntFrom2Uint8(k, m);
      }
      this.stack.push(j);
    }
    return paramInt2;
  }

  protected static int getIntFrom2Uint8(int paramInt1, int paramInt2)
  {
    return (paramInt1 << 8) + paramInt2 + (paramInt1 >> 7 & 0x1) * -65536;
  }

  protected static double getDoubleFromF26Dot6(int paramInt)
  {
    return paramInt / 64.0D;
  }

  protected static double getDoubleFromF2Dot14(int paramInt)
  {
    return paramInt / 16384.0D;
  }

  protected static int storeDoubleAsF26Dot6(double paramDouble)
  {
    return (int)(paramDouble * 64.0D + 0.5D);
  }

  protected static int storeDoubleAsF2Dot14(double paramDouble)
  {
    return (int)(paramDouble * 16384.0D + 0.5D);
  }

  private static int[] readProgramTable(FontFile2 paramFontFile2, int paramInt)
  {
    int[] arrayOfInt = new int[0];
    int i = paramFontFile2.selectTable(paramInt);
    if (i == 0)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("No program table found: " + paramInt);
    }
    else
    {
      int j = paramFontFile2.getOffset(paramInt);
      arrayOfInt = new int[j];
      for (int k = 0; k < j; k++)
        arrayOfInt[k] = paramFontFile2.getNextUint8();
    }
    return arrayOfInt;
  }

  private static class Stack
    implements Serializable
  {
    private int pointer;
    private int[] stack = new int[10];

    public void push(int paramInt)
    {
      if (this.pointer >= this.stack.length)
      {
        int[] arrayOfInt = new int[(int)(this.stack.length * 1.5D)];
        System.arraycopy(this.stack, 0, arrayOfInt, 0, this.stack.length);
        this.stack = arrayOfInt;
      }
      this.stack[this.pointer] = paramInt;
      this.pointer += 1;
    }

    public int pop()
    {
      this.pointer -= 1;
      if (this.pointer >= 0)
        return this.stack[this.pointer];
      this.pointer = 1;
      return 0;
    }

    public int size()
    {
      return this.pointer;
    }

    public int elementAt(int paramInt)
    {
      return this.stack[(this.pointer - paramInt)];
    }

    public int remove(int paramInt)
    {
      int i = this.pointer - paramInt;
      int j = this.stack[i];
      int[] arrayOfInt = new int[this.stack.length];
      System.arraycopy(this.stack, 0, arrayOfInt, 0, i);
      System.arraycopy(this.stack, i + 1, arrayOfInt, i, this.stack.length - i - 1);
      this.stack = arrayOfInt;
      this.pointer -= 1;
      return j;
    }
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.fonts.tt.hinting.TTVM
 * JD-Core Version:    0.6.2
 */