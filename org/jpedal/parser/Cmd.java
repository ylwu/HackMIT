package org.jpedal.parser;

public class Cmd
{
  protected static final int Tc = 21603;
  protected static final int Tw = 21623;
  protected static final int Tz = 21626;
  protected static final int TL = 21580;
  protected static final int Tf = 21606;
  protected static final int Tr = 21618;
  protected static final int Ts = 21619;
  protected static final int Td = 21604;
  protected static final int TD = 21572;
  protected static final int Tm = 21613;
  protected static final int Tstar = 21546;
  public static final int Tj = 21610;
  protected static final int TJ = 21578;
  protected static final int quote = 39;
  protected static final int doubleQuote = 34;
  protected static final int BI = 16969;
  private static final int ID = 18756;
  protected static final int m = 109;
  protected static final int l = 108;
  protected static final int c = 99;
  protected static final int d = 100;
  protected static final int v = 118;
  protected static final int y = 121;
  protected static final int h = 104;
  protected static final int re = 29285;
  public static final int S = 83;
  protected static final int s = 115;
  protected static final int f = 102;
  public static final int F = 70;
  protected static final int fstar = 26154;
  protected static final int Fstar = 17962;
  public static final int B = 66;
  protected static final int Bstar = 16938;
  protected static final int b = 98;
  protected static final int bstar = 25130;
  public static final int n = 110;
  protected static final int W = 87;
  protected static final int Wstar = 22314;
  public static final int BT = 16980;
  protected static final int ET = 17748;
  protected static final int Do = 17519;
  protected static final int w = 119;
  protected static final int j = 106;
  protected static final int J = 74;
  protected static final int M = 77;
  private static final int ri = 29289;
  protected static final int i = 105;
  protected static final int gs = 26483;
  protected static final int q = 113;
  protected static final int Q = 81;
  protected static final int cm = 25453;
  protected static final int d0 = 25648;
  protected static final int d1 = 25649;
  protected static final int cs = 25459;
  protected static final int CS = 17235;
  protected static final int sc = 29539;
  protected static final int scn = 7562094;
  protected static final int SC = 21315;
  protected static final int SCN = 5456718;
  protected static final int g = 103;
  protected static final int G = 71;
  protected static final int rg = 29287;
  protected static final int RG = 21063;
  protected static final int k = 107;
  protected static final int K = 75;
  private static final int sh = 29544;
  protected static final int BMC = 4345155;
  protected static final int BDC = 4342851;
  protected static final int EMC = 4541763;
  protected static final int MP = 19792;
  protected static final int DP = 17488;
  private static final int BX = 16984;
  private static final int EX = 17752;
  public static final int TEXT_COMMAND = 0;
  public static final int COLOR_COMMAND = 1;
  public static final int SHAPE_COMMAND = 2;
  public static final int SHADING_COMMAND = 3;
  public static final int GS_COMMAND = 4;
  public static final int IMAGE_COMMAND = 5;
  public static final int T3_COMMAND = 6;

  public static int getCommandType(int paramInt)
  {
    int i1 = -1;
    switch (paramInt)
    {
    case 4342851:
      i1 = 0;
      break;
    case 4345155:
      i1 = 0;
      break;
    case 16980:
      i1 = 0;
      break;
    case 17488:
      i1 = 0;
      break;
    case 4541763:
      i1 = 0;
      break;
    case 17748:
      i1 = 0;
      break;
    case 19792:
      i1 = 0;
      break;
    case 21603:
      i1 = 0;
      break;
    case 21623:
      i1 = 0;
      break;
    case 21626:
      i1 = 0;
      break;
    case 21580:
      i1 = 0;
      break;
    case 21606:
      i1 = 0;
      break;
    case 21618:
      i1 = 0;
      break;
    case 21619:
      i1 = 0;
      break;
    case 21572:
      i1 = 0;
      break;
    case 21604:
      i1 = 0;
      break;
    case 21613:
      i1 = 0;
      break;
    case 21546:
      i1 = 0;
      break;
    case 21610:
      i1 = 0;
      break;
    case 21578:
      i1 = 0;
      break;
    case 39:
      i1 = 0;
      break;
    case 34:
      i1 = 0;
      break;
    case 29287:
      i1 = 1;
      break;
    case 21063:
      i1 = 1;
      break;
    case 5456718:
      i1 = 1;
      break;
    case 7562094:
      i1 = 1;
      break;
    case 21315:
      i1 = 1;
      break;
    case 29539:
      i1 = 1;
      break;
    case 25459:
      i1 = 1;
      break;
    case 17235:
      i1 = 1;
      break;
    case 103:
      i1 = 1;
      break;
    case 71:
      i1 = 1;
      break;
    case 107:
      i1 = 1;
      break;
    case 75:
      i1 = 1;
      break;
    case 29544:
      i1 = 3;
      break;
    case 66:
      i1 = 2;
      break;
    case 98:
      i1 = 2;
      break;
    case 25130:
      i1 = 2;
      break;
    case 16938:
      i1 = 2;
      break;
    case 99:
      i1 = 2;
      break;
    case 100:
      i1 = 2;
      break;
    case 70:
      i1 = 2;
      break;
    case 102:
      i1 = 2;
      break;
    case 17962:
      i1 = 2;
      break;
    case 26154:
      i1 = 2;
      break;
    case 104:
      i1 = 2;
      break;
    case 105:
      i1 = 2;
      break;
    case 74:
      i1 = 2;
      break;
    case 106:
      i1 = 2;
      break;
    case 108:
      i1 = 2;
      break;
    case 77:
      i1 = 2;
      break;
    case 109:
      i1 = 2;
      break;
    case 110:
      i1 = 2;
      break;
    case 29285:
      i1 = 2;
      break;
    case 83:
      i1 = 2;
      break;
    case 115:
      i1 = 2;
      break;
    case 118:
      i1 = 2;
      break;
    case 119:
      i1 = 2;
      break;
    case 22314:
      i1 = 2;
      break;
    case 87:
      i1 = 2;
      break;
    case 121:
      i1 = 2;
      break;
    case 25453:
      i1 = 4;
      break;
    case 17519:
      i1 = 5;
      break;
    case 113:
      i1 = 4;
      break;
    case 81:
      i1 = 4;
      break;
    case 16969:
      i1 = 5;
      break;
    case 18756:
      i1 = 5;
      break;
    case 26483:
      i1 = 4;
      break;
    case 25648:
      i1 = 6;
      break;
    case 25649:
      i1 = 6;
    }
    return i1;
  }

  protected static int getCommandID(int paramInt)
  {
    int i1 = -1;
    switch (paramInt)
    {
    case 21603:
      i1 = 21603;
      break;
    case 21623:
      i1 = 21623;
      break;
    case 21626:
      i1 = 21626;
      break;
    case 21580:
      i1 = 21580;
      break;
    case 21606:
      i1 = 21606;
      break;
    case 21618:
      i1 = 21618;
      break;
    case 21619:
      i1 = 21619;
      break;
    case 21604:
      i1 = 21604;
      break;
    case 21572:
      i1 = 21572;
      break;
    case 21613:
      i1 = 21613;
      break;
    case 21546:
      i1 = 21546;
      break;
    case 21610:
      i1 = 21610;
      break;
    case 21578:
      i1 = 21578;
      break;
    case 39:
      i1 = 39;
      break;
    case 34:
      i1 = 34;
      break;
    case 16969:
      i1 = 16969;
      break;
    case 18756:
      i1 = 18756;
      break;
    case 109:
      i1 = 109;
      break;
    case 108:
      i1 = 108;
      break;
    case 99:
      i1 = 99;
      break;
    case 100:
      i1 = 100;
      break;
    case 118:
      i1 = 118;
      break;
    case 121:
      i1 = 121;
      break;
    case 104:
      i1 = 104;
      break;
    case 29285:
      i1 = 29285;
      break;
    case 83:
      i1 = 83;
      break;
    case 115:
      i1 = 115;
      break;
    case 102:
      i1 = 102;
      break;
    case 70:
      i1 = 70;
      break;
    case 26154:
      i1 = 26154;
      break;
    case 17962:
      i1 = 17962;
      break;
    case 66:
      i1 = 66;
      break;
    case 16938:
      i1 = 16938;
      break;
    case 98:
      i1 = 98;
      break;
    case 25130:
      i1 = 25130;
      break;
    case 110:
      i1 = 110;
      break;
    case 87:
      i1 = 87;
      break;
    case 22314:
      i1 = 22314;
      break;
    case 16980:
      i1 = 16980;
      break;
    case 17748:
      i1 = 17748;
      break;
    case 17519:
      i1 = 17519;
      break;
    case 119:
      i1 = 119;
      break;
    case 106:
      i1 = 106;
      break;
    case 74:
      i1 = 74;
      break;
    case 77:
      i1 = 77;
      break;
    case 29289:
      i1 = 29289;
      break;
    case 105:
      i1 = 105;
      break;
    case 26483:
      i1 = 26483;
      break;
    case 113:
      i1 = 113;
      break;
    case 81:
      i1 = 81;
      break;
    case 25453:
      i1 = 25453;
      break;
    case 25648:
      i1 = 25648;
      break;
    case 25649:
      i1 = 25649;
      break;
    case 25459:
      i1 = 25459;
      break;
    case 17235:
      i1 = 17235;
      break;
    case 29539:
      i1 = 29539;
      break;
    case 7562094:
      i1 = 7562094;
      break;
    case 21315:
      i1 = 21315;
      break;
    case 5456718:
      i1 = 5456718;
      break;
    case 103:
      i1 = 103;
      break;
    case 71:
      i1 = 71;
      break;
    case 29287:
      i1 = 29287;
      break;
    case 21063:
      i1 = 21063;
      break;
    case 107:
      i1 = 107;
      break;
    case 75:
      i1 = 75;
      break;
    case 29544:
      i1 = 29544;
      break;
    case 4345155:
      i1 = 4345155;
      break;
    case 4342851:
      i1 = 4342851;
      break;
    case 4541763:
      i1 = 4541763;
      break;
    case 19792:
      i1 = 19792;
      break;
    case 17488:
      i1 = 17488;
      break;
    case 16984:
      i1 = 16984;
      break;
    case 17752:
      i1 = 17752;
    }
    return i1;
  }

  protected static String getCommandAsString(int paramInt)
  {
    String str = "";
    switch (paramInt)
    {
    case 21603:
      str = "Tc";
      break;
    case 21623:
      str = "Tw";
      break;
    case 21626:
      str = "Tz";
      break;
    case 21580:
      str = "TL";
      break;
    case 21606:
      str = "Tf";
      break;
    case 21618:
      str = "Tr";
      break;
    case 21619:
      str = "Ts";
      break;
    case 21604:
      str = "Td";
      break;
    case 21572:
      str = "TD";
      break;
    case 21613:
      str = "Tm";
      break;
    case 21546:
      str = "Tstar";
      break;
    case 21610:
      str = "Tj";
      break;
    case 21578:
      str = "TJ";
      break;
    case 39:
      str = "'";
      break;
    case 34:
      str = "\"";
      break;
    case 16969:
      str = "BI";
      break;
    case 18756:
      str = "ID";
      break;
    case 109:
      str = "m";
      break;
    case 108:
      str = "l";
      break;
    case 99:
      str = "c";
      break;
    case 100:
      str = "d";
      break;
    case 118:
      str = "v";
      break;
    case 121:
      str = "y";
      break;
    case 104:
      str = "h";
      break;
    case 29285:
      str = "re";
      break;
    case 83:
      str = "S";
      break;
    case 115:
      str = "s";
      break;
    case 102:
      str = "f";
      break;
    case 70:
      str = "F";
      break;
    case 26154:
      str = "f*";
      break;
    case 17962:
      str = "F*";
      break;
    case 66:
      str = "B";
      break;
    case 16938:
      str = "B*";
      break;
    case 98:
      str = "b";
      break;
    case 25130:
      str = "b*";
      break;
    case 110:
      str = "n";
      break;
    case 87:
      str = "W";
      break;
    case 22314:
      str = "W*";
      break;
    case 16980:
      str = "BT";
      break;
    case 17748:
      str = "ET";
      break;
    case 17519:
      str = "Do";
      break;
    case 119:
      str = "w";
      break;
    case 106:
      str = "j";
      break;
    case 74:
      str = "J";
      break;
    case 77:
      str = "M";
      break;
    case 29289:
      str = "ri";
      break;
    case 105:
      str = "i";
      break;
    case 26483:
      str = "gs";
      break;
    case 113:
      str = "q";
      break;
    case 81:
      str = "Q";
      break;
    case 25453:
      str = "cm";
      break;
    case 25648:
      str = "d0";
      break;
    case 25649:
      str = "d1";
      break;
    case 25459:
      str = "cs";
      break;
    case 17235:
      str = "CS";
      break;
    case 29539:
      str = "sc";
      break;
    case 7562094:
      str = "scn";
      break;
    case 21315:
      str = "SC";
      break;
    case 5456718:
      str = "SCN";
      break;
    case 103:
      str = "g";
      break;
    case 71:
      str = "G";
      break;
    case 29287:
      str = "rg";
      break;
    case 21063:
      str = "RG";
      break;
    case 107:
      str = "k";
      break;
    case 75:
      str = "K";
      break;
    case 29544:
      str = "sh";
      break;
    case 4345155:
      str = "BMC";
      break;
    case 4342851:
      str = "BDC";
      break;
    case 4541763:
      str = "EMC";
      break;
    case 19792:
      str = "MP";
      break;
    case 17488:
      str = "DP";
      break;
    case 16984:
      str = "BX";
      break;
    case 17752:
      str = "EX";
    }
    return str;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.parser.Cmd
 * JD-Core Version:    0.6.2
 */