package org.jpedal.objects.raw;

import java.io.PrintStream;
import java.lang.reflect.Field;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.StringUtils;

public class PdfDictionary
{
  public static final int Unknown = -1;
  public static final int URI = 2433561;
  public static final int A = 17;
  public static final int AA = 4369;
  public static final int AC = 4371;
  public static final int AcroForm = 661816444;
  public static final int AcroForm_FormsJSGuide = 286725562;
  public static final int ActualText = 1752861363;
  public static final int Adobe_PubSec = 2018874538;
  public static final int AIS = 1120547;
  public static final int Alternate = 2054519176;
  public static final int AlternateSpace = -1247101998;
  public static final int Annot = 1044266837;
  public static final int Annots = 1044338049;
  public static final int AntiAlias = 2055039589;
  public static final int AP = 4384;
  public static final int Array = 1111634266;
  public static final int ArtBox = 1142050954;
  public static final int AS = 4387;
  public static final int Asset = 1128478037;
  public static final int Assets = 1127568774;
  public static final int Ascent = 859131783;
  public static final int Author = 1144541319;
  public static final int AvgWidth = 1249540959;
  public static final int B = 18;
  public static final int BlackPoint = 1886161824;
  public static final int Background = 1921025959;
  public static final int Base = 305218357;
  public static final int BaseEncoding = 1537782955;
  public static final int BaseFont = 678461817;
  public static final int BaseState = 1970567530;
  public static final int BBox = 303185736;
  public static final int BC = 4627;
  public static final int BDC = 1184787;
  public static final int BG = 4631;
  public static final int BI = 4633;
  public static final int BitsPerComponent = -1344207655;
  public static final int BitsPerCoordinate = -335950113;
  public static final int BitsPerFlag = 1500422077;
  public static final int BitsPerSample = -1413045608;
  public static final int Bl = 4668;
  public static final int BlackIs1 = 1297445940;
  public static final int BleedBox = 1179546749;
  public static final int Blend = 1010122310;
  public static final int Bounds = 1161709186;
  public static final int Border = 1110722433;
  public static final int BOTTOMINSET = -2102087263;
  public static final int BM = 4637;
  public static final int BPC = 1187859;
  public static final int BS = 4643;
  public static final int Btn = 1197118;
  public static final int ByteRange = 2055367785;
  public static final int C = 19;
  public static final int C0 = 4864;
  public static final int C1 = 4865;
  public static final int C2 = 4866;
  public static final int CA = 4881;
  public static final int ca = 13105;
  public static final int Calculate = 1835890573;
  public static final int CapHeight = 1786204300;
  public static final int Category = 1248888446;
  public static final int Catalog = 827289723;
  public static final int Cert = 322257476;
  public static final int CF = 4886;
  public static final int CFM = 1250845;
  public static final int Ch = 4920;
  public static final int CIDSet = 337856605;
  public static final int CIDSystemInfo = 1972801240;
  public static final int CharProcs = 2054190454;
  public static final int CharSet = 1110863221;
  public static final int CheckSum = 1314617968;
  public static final int CIDFontType0C = -1752352082;
  public static final int CIDToGIDMap = 946823533;
  public static final int ClassMap = 1448698499;
  public static final int CMap = 320680256;
  public static final int CMapName = 827223669;
  public static final int CMYK = 320678171;
  public static final int CO = 4895;
  public static final int Colors = 1010783618;
  public static final int ColorSpace = 2087749783;
  public static final int ColorTransform = -1263544861;
  public static final int Columns = 1162902911;
  public static final int Components = 1920898752;
  public static final int CompressedObject = 23;
  public static final int Configurations = -1128809475;
  public static final int Configs = 910980737;
  public static final int ContactInfo = 1568843969;
  public static final int Contents = 1216184967;
  public static final int Coords = 1061308290;
  public static final int Count = 1061502502;
  public static final int CreationDate = 1806481572;
  public static final int Creator = 827818359;
  public static final int CropBox = 1076199815;
  public static final int CS = 4899;
  public static final int CVMRC = 639443494;
  public static final int D = 20;
  public static final int DA = 5137;
  public static final int DamagedRowsBeforeError = 904541242;
  public static final int DC = 5139;
  public static final int DCT = 1315620;
  public static final int Decode = 859785322;
  public static final int DecodeParms = 1888135062;
  public static final int DescendantFonts = -1547306032;
  public static final int Descent = 860451719;
  public static final int Dest = 339034948;
  public static final int Dests = 893600855;
  public static final int Differences = 1954328750;
  public static final int Domain = 1026641277;
  public static final int DP = 5152;
  public static final int DR = 5154;
  public static final int DS = 5155;
  public static final int DV = 5158;
  public static final int DW = 5159;
  public static final int E = 21;
  public static final int EarlyChange = 1838971823;
  public static final int EF = 5398;
  public static final int EFF = 1381910;
  public static final int EOPROPtype = 1684763764;
  public static final int Encode = 859785587;
  public static final int EncodedByteAlign = -823077984;
  public static final int Encoding = 1232564598;
  public static final int Encrypt = 1113489015;
  public static final int EncryptMetadata = -1815804199;
  public static final int EndOfBlock = 1885240971;
  public static final int EndOfLine = 1517116800;
  public static final int Export = 1077893004;
  public static final int Extend = 1144345468;
  public static final int Extends = 894663815;
  public static final int ExtGState = -1938465939;
  public static final int Event = 1177894489;
  public static final int F = 22;
  public static final int FDF = 1446934;
  public static final int Ff = 5686;
  public static final int Fields = 893143676;
  public static final int FileAccess = 1869245103;
  public static final int FileAttachment = -1113876231;
  public static final int Filter = 1011108731;
  public static final int First = 960643930;
  public static final int FirstChar = 1283093660;
  public static final int FirstPage = 1500740239;
  public static final int Fit = 1456452;
  public static final int FitB = 372851730;
  public static final int FitBH = 960762414;
  public static final int FitBV = 960762428;
  public static final int FitH = 372851736;
  public static final int FitHeight = 1920684175;
  public static final int FitR = 372851746;
  public static final int FitV = 372851750;
  public static final int FitWidth = 1332578399;
  public static final int Flags = 1009858393;
  public static final int Fo = 5695;
  public static final int Font = 373243460;
  public static final int FontBBox = 676429196;
  public static final int FontDescriptor = -1044665361;
  public static final int FontFamily = 2071816377;
  public static final int FontFile = 746093177;
  public static final int FontFile2 = 2021292334;
  public static final int FontFile3 = 2021292335;
  public static final int FontMatrix = -2105119560;
  public static final int FontName = 879786873;
  public static final int FontStretch = 2038281912;
  public static final int FontWeight = 2004579768;
  public static final int Form = 373244477;
  public static final int Format = 1111312259;
  public static final int FormType = 982024818;
  public static final int FreeText = 980909433;
  public static final int FS = 5667;
  public static final int FT = 5668;
  public static final int FullScreen = 2121363126;
  public static final int Function = 1518239089;
  public static final int Functions = 2122150301;
  public static final int FunctionType = 2127019430;
  public static final int G = 23;
  public static final int Gamma = 826096968;
  public static final int GoBack = 305220218;
  public static final int GoTo = 390014015;
  public static final int GoToR = 1059340089;
  public static final int Group = 1111442775;
  public static final int H = 24;
  public static final int Height = 959926393;
  public static final int Hide = 406402101;
  public static final int Highlight = 1919840408;
  public static final int hival = 960901492;
  public static final int I = 25;
  public static final int ID = 6420;
  public static final int Identity = 1567455623;
  public static final int Identity_H = 2038913669;
  public static final int Identity_V = 2038913683;
  public static final int IDTree = 608325193;
  public static final int IF = 6422;
  public static final int IM = 6429;
  public static final int Image = 1026635598;
  public static final int ImageMask = 1516403337;
  public static final int Index = 1043608929;
  public static final int Indexed = 895578984;
  public static final int Info = 423507519;
  public static final int Ink = 1654331;
  public static final int InkList = 475169151;
  public static final int Instances = 2088139149;
  public static final int Intent = 1144346498;
  public static final int InvisibleRect = -1716672299;
  public static final int IT = 6436;
  public static final int ItalicAngle = 2055844727;
  public static final int JavaScript = -2006286978;
  public static final int JS = 6691;
  public static final int JT = 6692;
  public static final int JBIG2Globals = 1314558361;
  public static final int K = 27;
  public static final int Keywords = 1517780362;
  public static final int Keystroke = 2005434004;
  public static final int Kids = 456733763;
  public static final int L = 28;
  public static final int Lang = 472989239;
  public static final int Last = 472990532;
  public static final int LastChar = 795440262;
  public static final int LastModified = 1873390769;
  public static final int LastPage = 1013086841;
  public static final int Launch = 1161711465;
  public static final int Layer = 826881374;
  public static final int Leading = 878015336;
  public static final int LEFTINSET = 1937340825;
  public static final int Length = 1043816557;
  public static final int Length1 = 929066303;
  public static final int Length2 = 929066304;
  public static final int Length3 = 929066305;
  public static final int Linearized = 2004845231;
  public static final int LinearizedReader = -1276915978;
  public static final int Link = 473513531;
  public static final int ListMode = 964196217;
  public static final int Location = 1618506351;
  public static final int Lock = 473903931;
  public static final int Locked = 859525491;
  public static final int Lookup = 1060856191;
  public static final int LW = 7207;
  public static final int M = 29;
  static final int MacExpertEncoding = -1159739105;
  static final int MacRomanEncoding = -1511664170;
  public static final int MARGIN = 1110931055;
  public static final int MarkInfo = 913275002;
  public static final int Mask = 489767739;
  public static final int Matrix = 1145198201;
  public static final int max = 4010312;
  public static final int MaxLen = 1209815663;
  public static final int MaxWidth = 1449495647;
  public static final int MCID = 487790868;
  public static final int MediaBox = 1313305473;
  public static final int Metadata = 1365674082;
  public static final int min = 4012350;
  public static final int MissingWidth = -1884569950;
  public static final int MK = 7451;
  public static final int ModDate = 340689769;
  public static final int MouseDown = 1401195152;
  public static final int MouseEnter = -2088269930;
  public static final int MouseExit = 1418558614;
  public static final int MouseUp = 1129473157;
  public static final int Multiply = 1451587725;
  public static final int N = 30;
  public static final int Name = 506543413;
  public static final int Named = 826094930;
  public static final int Names = 826094945;
  public static final int NeedAppearances = -1483477783;
  public static final int Next = 506808388;
  public static final int NextPage = 1046904697;
  public static final int NM = 7709;
  public static final int None = 507461173;
  public static final int Normal = 1111314299;
  public static final int Nums = 507854147;
  public static final int O = 31;
  public static final int OC = 7955;
  public static final int OCGs = 521344835;
  public static final int OCProperties = -1567847737;
  public static final int OE = 7957;
  public static final int OFF = 2037270;
  public static final int Off = 2045494;
  public static final int ON = 7966;
  public static final int On = 7998;
  public static final int OnBlur = 305947776;
  public static final int OnFocus = 1062372185;
  public static final int OP = 7968;
  public static final int op = 16192;
  public static final int Open = 524301630;
  public static final int OpenAction = 2037870513;
  public static final int OPI = 2039833;
  public static final int OPM = 2039837;
  public static final int Opt = 2048068;
  public static final int Order = 1110717793;
  public static final int Ordering = 1635480172;
  public static final int Outlines = 1485011327;
  public static final int P = 32;
  public static final int PaintType = 1434615449;
  public static final int Page = 540096309;
  public static final int PageLabels = 1768585381;
  public static final int PageMode = 1030777706;
  public static final int Pages = 825701731;
  public static final int Params = 1110531444;
  public static final int Parent = 1110793845;
  public static final int ParentTree = 1719112618;
  public static final int Pattern = 1146450818;
  public static final int PatternType = 1755231159;
  public static final int PC = 8211;
  static final int PDFDocEncoding = 1602998461;
  public static final int Perms = 893533539;
  public static final int Pg = 8247;
  public static final int PI = 8217;
  public static final int PieceInfo = 1383295380;
  public static final int PO = 8223;
  public static final int Popup = 1061176672;
  public static final int Predictor = 1970893723;
  public static final int Prev = 541209926;
  public static final int PrevPage = 1081306235;
  public static final int Print = 1111047780;
  public static final int PrintState = 2104469658;
  public static final int Process = 861242754;
  public static final int ProcSet = 860059523;
  public static final int Producer = 1702196342;
  public static final int Properties = -2089186617;
  public static final int PV = 8230;
  public static final int Q = 33;
  public static final int QFactor = 862279027;
  public static final int QuadPoints = 1785890247;
  public static final int R = 34;
  public static final int Range = 826160983;
  public static final int RBGroups = 1633113989;
  public static final int RC = 8723;
  public static final int Reason = 826499443;
  public static final int Recipients = 1752671921;
  public static final int Rect = 573911876;
  public static final int Reference = 1786013849;
  public static final int Registry = 1702459778;
  public static final int ResetForm = 1266841507;
  public static final int Resources = 2004251818;
  public static final int RGB = 2234130;
  public static final int RichMediaContent = -1263082253;
  public static final int RIGHTINSET = 1971043222;
  public static final int RD = 8724;
  public static final int Root = 574570308;
  public static final int RoleMap = 893350012;
  public static final int Rotate = 1144088180;
  public static final int Rows = 574572355;
  public static final int RV = 8742;
  public static final int S = 35;
  public static final int SA = 8977;
  public static final int SaveAs = 1177891956;
  public static final int Screen = 1110792305;
  public static final int SetOCGState = 1667731612;
  public static final int Square = 1160865142;
  public static final int Shading = 878474856;
  public static final int ShadingType = 1487255197;
  public static final int Sig = 2308407;
  public static final int SigFlags = 1600810585;
  public static final int Signed = 926832749;
  public static final int Size = 590957109;
  public static final int SM = 8989;
  public static final int SMask = 489767774;
  public static final int Sound = 1061502534;
  public static final int Stamp = 1144077667;
  public static final int Standard = 1467315058;
  static final int StandardEncoding = -1595087640;
  public static final int State = 1144079448;
  public static final int StemH = 1144339771;
  public static final int StemV = 1144339785;
  public static final int StmF = 591674646;
  public static final int StrF = 591675926;
  public static final int StrickOut = 2036432546;
  public static final int StructElem = 1468107717;
  public static final int StructParent = -1732403014;
  public static final int StructParents = -1113539877;
  public static final int StructTreeRoot = -2000237823;
  public static final int Style = 1145650264;
  public static final int SubFilter = -2122953826;
  public static final int Subj = 591737402;
  public static final int Subject = 978876534;
  public static final int SubmitForm = 1216126662;
  public static final int Subtype = 1147962727;
  public static final int Supplement = 2104860094;
  public static final int T = 36;
  public static final int Tabs = 607203907;
  public static final int TagSuspect = 2002295992;
  public static final int Text = 607471684;
  public static final int TI = 9241;
  public static final int TilingType = 1619174053;
  public static final int tintTransform = -1313946392;
  public static final int Title = 960773209;
  public static final int TM = 9245;
  public static final int Toggle = 926376052;
  public static final int TOPINSET = -2105379491;
  public static final int ToUnicode = 1919185554;
  public static final int TP = 9248;
  public static final int TR = 9250;
  public static final int Trapped = 1080325989;
  public static final int TrimBox = 1026982273;
  public static final int Tx = 9288;
  public static final int TxFontSize = 964209857;
  public static final int TxOutline = -2074573923;
  public static final int TU = 9253;
  public static final int Type = 608780341;
  public static final int U = 37;
  public static final int UE = 9493;
  public static final int UF = 9494;
  public static final int Uncompressed = -1514034520;
  public static final int Unsigned = 1551661165;
  public static final int Usage = 1127298906;
  public static final int V = 38;
  public static final int Validate = 1516404846;
  public static final int Widget = 876043389;
  public static final int View = 641283399;
  public static final int VP = 9760;
  public static final int W = 39;
  public static final int W2 = 9986;
  public static final int WhitePoint = 2021497500;
  public static final int Win = 2570558;
  static final int WinAnsiEncoding = 1524428269;
  public static final int Width = 959726687;
  public static final int Widths = 876896124;
  public static final int WP = 10016;
  public static final int WS = 10019;
  public static final int X = 40;
  public static final int XFA = 2627089;
  public static final int XHeight = 962547833;
  public static final int XObject = 979194486;
  public static final int XRefStm = 910911090;
  public static final int XStep = 591672680;
  public static final int XYZ = 2631978;
  public static final int YStep = 591672681;
  public static final int Zoom = 708788029;
  public static final int ZoomTo = 1060982398;
  public static final int Unchanged = 2087349642;
  public static final int Underline = 2053993372;
  public static final int VALUE_IS_DICTIONARY = 1;
  public static final int VALUE_IS_DICTIONARY_PAIRS = 2;
  public static final int VALUE_IS_STRING_CONSTANT = 3;
  public static final int VALUE_IS_STRING_KEY = 4;
  public static final int VALUE_IS_UNREAD_DICTIONARY = 5;
  public static final int VALUE_IS_INT = 6;
  public static final int VALUE_IS_FLOAT = 7;
  public static final int VALUE_IS_BOOLEAN = 8;
  public static final int VALUE_IS_INT_ARRAY = 9;
  public static final int VALUE_IS_FLOAT_ARRAY = 10;
  public static final int VALUE_IS_BOOLEAN_ARRAY = 12;
  public static final int VALUE_IS_KEY_ARRAY = 14;
  public static final int VALUE_IS_DOUBLE_ARRAY = 16;
  public static final int VALUE_IS_MIXED_ARRAY = 18;
  public static final int VALUE_IS_STRING_ARRAY = 20;
  public static final int VALUE_IS_OBJECT_ARRAY = 22;
  public static final int VALUE_IS_TEXTSTREAM = 25;
  public static final int VALUE_IS_NAME = 30;
  public static final int VALUE_IS_NAMETREE = 35;
  public static final int VALUE_IS_VARIOUS = 40;
  public static final int XFA_TEMPLATE = 1013350773;
  public static final int XFA_DATASET = 1130793076;
  public static final int XFA_CONFIG = 1043741046;
  public static final int XFA_PREAMBLE = 1031041382;
  public static final int XFA_LOCALESET = 1951819392;
  public static final int XFA_PDFSECURITY = 1701743524;
  public static final int XFA_XMPMETA = 1026916721;
  public static final int XFA_XDP = 172517504;
  public static final int XFA_XFDF = 3552310;
  public static final int XFA_POSTAMBLE = 2088075366;
  public static final int STANDARD = 0;
  public static final int LOWERCASE = 1;
  public static final int REMOVEPOSTSCRIPTPREFIX = 2;
  public static final int XFA_APPEARANCE = 129;

  public static Object getKey(int paramInt1, int paramInt2, byte[] paramArrayOfByte)
  {
    byte[] arrayOfByte = new byte[paramInt2];
    System.arraycopy(paramArrayOfByte, paramInt1, arrayOfByte, 0, paramInt2);
    return new String(arrayOfByte);
  }

  public static int getIntKey(int paramInt1, int paramInt2, byte[] paramArrayOfByte)
  {
    int i = generateChecksum(paramInt1, paramInt2, paramArrayOfByte);
    int j = i;
    switch (i)
    {
    case 1187859:
      j = -1344207655;
      break;
    case 320678171:
      j = 1498837125;
      break;
    case 4899:
      j = 2087749783;
      break;
    case 1315620:
      return 1180911742;
    case 5152:
      j = 1888135062;
      break;
    case 5692:
      j = 2005566619;
      break;
    case 6429:
      j = 1516403337;
      break;
    case 25:
      j = 895578984;
      break;
    case -1159739105:
      j = 3;
      break;
    case -1511664170:
      j = 0;
      break;
    case 1110531444:
      j = 1888135062;
      break;
    case 1602998461:
      j = 6;
      break;
    case 2234130:
      j = 1785221209;
      break;
    case -1595087640:
      j = 1;
      break;
    case 1524428269:
      j = 2;
    }
    return j;
  }

  public static int generateChecksum(int paramInt1, int paramInt2, byte[] paramArrayOfByte)
  {
    int i = 0;
    int j = 0;
    for (int m = paramInt2 - 1; m > -1; m--)
    {
      int k = paramArrayOfByte[(paramInt1 + m)];
      k -= 48;
      i += (k << j);
      j += 8;
    }
    if ((i == 1061502551) || (i == -2006286936))
      i = i + paramArrayOfByte[paramInt1] - paramArrayOfByte[(paramInt1 + paramInt2 - 1)];
    return i;
  }

  public static int getKeyType(int paramInt1, int paramInt2)
  {
    int i = -1;
    switch (paramInt1)
    {
    case 17:
      if (paramInt2 == 2004845231)
        return 6;
      return 1;
    case 4369:
      return 5;
    case 4371:
      return 25;
    case 661816444:
      return 5;
    case 1752861363:
      return 25;
    case 2054519176:
      return 3;
    case 1120547:
      return 8;
    case 1044338049:
      return 14;
    case 2055039589:
      return 8;
    case 4384:
      return 1;
    case 1111634266:
      return 10;
    case 1142050954:
      return 10;
    case 4387:
      if (paramInt2 == -1567847737)
        return 14;
      return 30;
    case 859131783:
      return 7;
    case 1128478037:
      return 5;
    case 1127568774:
      return 5;
    case 1144541319:
      return 25;
    case 18:
      if ((paramInt2 == 1061502534) || (paramInt2 == 2004845231))
        return 6;
      break;
    case 1921025959:
      return 10;
    case 305218357:
      return 25;
    case 1537782955:
      return 3;
    case 678461817:
      return 30;
    case 1970567530:
      return 30;
    case 303185736:
      return 10;
    case 4627:
      return 10;
    case 4631:
      if (paramInt2 == 7451)
        return 10;
      return 5;
    case 4633:
      return 1;
    case -1344207655:
      return 6;
    case -335950113:
      return 6;
    case 1500422077:
      return 6;
    case -1413045608:
      return 6;
    case 4668:
      return 1;
    case 1297445940:
      return 8;
    case 1886161824:
      return 10;
    case 1179546749:
      return 10;
    case 1010122310:
      return 6;
    case 1110722433:
      return 18;
    case 1161709186:
      return 10;
    case 4637:
      return 18;
    case 4643:
      return 1;
    case 2055367785:
      return 9;
    case 19:
      if ((paramInt2 == 373244477) || (paramInt2 == 487790868))
        return 40;
      if ((paramInt2 == 1061502534) || (paramInt2 == 2004845231))
        return 6;
      return 10;
    case 4864:
      return 10;
    case 4865:
      return 10;
    case 4881:
      if ((paramInt2 == 373244477) || (paramInt2 == 7451))
        return 40;
      return 7;
    case 13105:
      return 7;
    case 1248888446:
      if (paramInt2 == -1567847737)
        return 14;
      break;
    case 322257476:
      return 40;
    case 4886:
      return 2;
    case 1250845:
      return 30;
    case 2054190454:
      return 2;
    case 1110863221:
      return 25;
    case 1314617968:
      return 25;
    case 337856605:
      return 1;
    case 1448698499:
      return 1;
    case 827223669:
      return 30;
    case 4895:
      return 22;
    case 1010783618:
      return 6;
    case -1263544861:
      return 6;
    case 2087749783:
      if (paramInt2 == 979194486)
        return 5;
      return 5;
    case 1162902911:
      return 6;
    case 910980737:
      return 14;
    case -1128809475:
      return 5;
    case 1568843969:
      return 25;
    case 1216184967:
      if (paramInt2 == 373244477)
        return 40;
      return 14;
    case 1061308290:
      return 10;
    case 1061502502:
      return 6;
    case 1806481572:
      return 25;
    case 827818359:
      return 25;
    case 1076199815:
      return 10;
    case 1972801240:
      return 1;
    case 946823533:
      return 1;
    case 639443494:
      return 3;
    case 20:
      return 1;
    case 5137:
      return 25;
    case 904541242:
      return 6;
    case 5139:
      return 1;
    case 859785322:
      return 10;
    case 1888135062:
      return 1;
    case 860451719:
      return 7;
    case -1547306032:
      return 1;
    case 339034948:
      return 18;
    case 893600855:
      return 1;
    case 1954328750:
      return 18;
    case 1026641277:
      return 10;
    case 5152:
      return 1;
    case 5154:
      return 5;
    case 5158:
      return 40;
    case 5155:
      if (paramInt2 == 373244477)
        return 40;
      return 25;
    case 5159:
      return 6;
    case 21:
      if (paramInt2 == 2004845231)
        return 6;
      return 40;
    case 5398:
      return 5;
    case 1838971823:
      return 6;
    case -823077984:
      return 8;
    case 859785587:
      return 10;
    case 1232564598:
      return 1;
    case 1113489015:
      return 5;
    case -1815804199:
      return 8;
    case 1885240971:
      return 8;
    case 1517116800:
      return 8;
    case 1684763764:
      return 25;
    case 1177894489:
      return 3;
    case 1144345468:
      return 12;
    case 894663815:
      return 1;
    case -1938465939:
      return 5;
    case 22:
      if ((paramInt2 == 373244477) || (paramInt2 == 1485011327) || (paramInt2 == 5667) || (paramInt2 == 826094945))
        return 40;
      if (paramInt2 == 1446934)
        return 25;
      return 6;
    case 5686:
      return 6;
    case 893143676:
      if (paramInt2 == 1446934)
        return 14;
      return 18;
    case 1011108731:
      return 18;
    case 960643930:
      if (paramInt2 == 5667)
        return 40;
      if (paramInt2 == 1485011327)
        return 5;
      return 6;
    case 1283093660:
      return 6;
    case 1009858393:
      return 6;
    case 5695:
      return 1;
    case 373243460:
      return 5;
    case 676429196:
      return 10;
    case -1044665361:
      return 1;
    case 746093177:
      return 1;
    case 2021292334:
      return 1;
    case 2021292335:
      return 1;
    case -2105119560:
      return 16;
    case 879786873:
      return 30;
    case 2038281912:
      return 30;
    case 982024818:
      return 6;
    case 5667:
      return 40;
    case 5668:
      return 30;
    case 1518239089:
      return 1;
    case 2122150301:
      return 14;
    case 2127019430:
      return 6;
    case 23:
      if (paramInt2 == 979194486)
        return 5;
      return i;
    case 826096968:
      return 10;
    case 1111442775:
      return 5;
    case 24:
      if (paramInt2 == 2004845231)
        return 9;
      if (paramInt2 == 373244477)
        return 40;
      if (paramInt2 == 1485011327)
        return 8;
      return i;
    case 959926393:
      return 6;
    case 25:
      if (paramInt2 == 373244477)
        return 9;
      if (paramInt2 == 7451)
        return 5;
      if (paramInt2 == 540096309)
        return 8;
      if (paramInt2 == 2004845231)
        return 6;
      return 8;
    case 6420:
      if (paramInt2 == 487790868)
        return 6;
      if (paramInt2 == 23)
        return 20;
      return i;
    case 6422:
      return 5;
    case 608325193:
      return 35;
    case 1043608929:
      return 9;
    case 423507519:
      return 5;
    case 475169151:
      return 22;
    case 1516403337:
      return 8;
    case 1144346498:
      return 30;
    case 6436:
      return 30;
    case -1716672299:
      return 3;
    case -2006286978:
      return 1;
    case 6691:
      return 40;
    case 27:
      if (paramInt2 == 979194486)
        return 40;
      if (paramInt2 == 373244477)
        return 1;
      if (paramInt2 == 487790868)
        return 40;
      if (paramInt2 == -1567847737)
        return 40;
      return 6;
    case 1517780362:
      return 25;
    case 456733763:
      return 14;
    case 1314558361:
      return 1;
    case 6692:
      return 5;
    case 28:
      if (paramInt2 == 2004845231)
        return 6;
      return i;
    case 472989239:
      if ((paramInt2 == 487790868) || (paramInt2 == 540096309))
        return 25;
      return i;
    case 472990532:
      return 5;
    case 795440262:
      return 6;
    case 1873390769:
      return 25;
    case 826881374:
      if (paramInt2 == 373244477)
        return 6;
      return 1;
    case 1043816557:
      return 6;
    case 929066303:
      return 6;
    case 929066304:
      return 6;
    case 929066305:
      return 6;
    case 2004845231:
      if (paramInt2 == 2004845231)
        return 7;
      return i;
    case 1618506351:
      return 25;
    case 473903931:
      return 5;
    case 859525491:
      return 14;
    case 7207:
      return 7;
    case 29:
      if (paramInt2 == 373244477)
        return 40;
      if (paramInt2 == 2308407)
        return 25;
      return 25;
    case 913275002:
      return 5;
    case 489767739:
      return 1;
    case 1145198201:
      return 10;
    case 4010312:
      return 7;
    case 1209815663:
      return 6;
    case 487790868:
      return 6;
    case 1313305473:
      return 10;
    case 1365674082:
      return 5;
    case -1884569950:
      return 6;
    case 7451:
      return 1;
    case 340689769:
      return 25;
    case 30:
      if (paramInt2 == 2004845231)
        return 6;
      if (paramInt2 == 23)
        return 6;
      if ((paramInt2 == 373244477) || (paramInt2 == 7451))
        return 40;
      return 30;
    case 506543413:
      return 30;
    case 826094945:
      if ((paramInt2 == 826094945) || (paramInt2 == 5667))
        return 18;
      return 5;
    case -1483477783:
      return 8;
    case 506808388:
      if (paramInt2 == 373244477)
        return 1;
      return 5;
    case 7709:
      return 25;
    case 507854147:
      return 14;
    case 4012350:
      return 7;
    case 31:
      if ((paramInt2 == 2004845231) || (paramInt2 == 23))
        return 6;
      if (paramInt2 == 373244477)
        return 1;
      return 25;
    case 7955:
      if ((paramInt2 == 373244477) || (paramInt2 == 979194486))
        return 1;
      return 30;
    case 521344835:
      return 14;
    case -1567847737:
      return 5;
    case 7957:
      return 25;
    case 2037270:
      return 14;
    case 2045494:
      return 5;
    case 7966:
      return 14;
    case 7998:
      return 5;
    case 7968:
      if (paramInt2 == 373244477)
        return 40;
      return 8;
    case 16192:
      return 8;
    case 524301630:
      return 8;
    case 2037870513:
      return 40;
    case 2039833:
      return 1;
    case 2039837:
      return 7;
    case 2048068:
      return 22;
    case 1635480172:
      return 25;
    case 1485011327:
      return 5;
    case 32:
      if ((paramInt2 == 373244477) || (paramInt2 == 487790868) || (paramInt2 == 5667) || (paramInt2 == 7451))
        return 5;
      if (paramInt2 == 1365674082)
        return 1;
      return 6;
    case 1768585381:
      return 5;
    case 1030777706:
      return 3;
    case 825701731:
      return 1;
    case 1434615449:
      return 6;
    case 1719112618:
      return 1;
    case 1146450818:
      return 1;
    case 1755231159:
      return 6;
    case 1110793845:
      return 4;
    case 8211:
      return 1;
    case 893533539:
      return 40;
    case 541209926:
      if (paramInt2 == 1485011327)
        return 5;
      return 6;
    case 8247:
      return 5;
    case 8217:
      return 1;
    case 1383295380:
      return 5;
    case 8223:
      return 1;
    case 861242754:
      return 1;
    case 1061176672:
      return 5;
    case 1970893723:
      return 6;
    case 1111047780:
      return 1;
    case 2104469658:
      return 3;
    case 860059523:
      return 18;
    case 1702196342:
      return 25;
    case -2089186617:
      return 2;
    case 8230:
      return 1;
    case 33:
      return 6;
    case 862279027:
      return 6;
    case 1785890247:
      return 10;
    case 34:
      if ((paramInt2 == 373244477) || (paramInt2 == 7451))
        return 40;
      return 6;
    case 826160983:
      return 10;
    case 1633113989:
      return 14;
    case 8723:
      return 25;
    case 8724:
      return 10;
    case 826499443:
      return 25;
    case 1752671921:
      return 20;
    case 1786013849:
      return 22;
    case 1702459778:
      return 25;
    case -1263082253:
      return 5;
    case 2004251818:
      return 5;
    case 893350012:
      return 1;
    case 1144088180:
      return 6;
    case 573911876:
      return 10;
    case 574570308:
      return 5;
    case 574572355:
      return 6;
    case 8742:
      return 25;
    case 878474856:
      return 1;
    case 35:
      if (paramInt2 == 2004845231)
        return 6;
      return 30;
    case 8977:
      return 8;
    case 1487255197:
      return 6;
    case 1600810585:
      return 6;
    case 590957109:
      if ((paramInt2 == 23) || (paramInt2 == -1))
        return 6;
      return 9;
    case 489767774:
      return 1;
    case 2308407:
      return 5;
    case 1061502534:
      return 5;
    case 1144079448:
      return 18;
    case 1144339785:
      return 6;
    case 591674646:
      return 30;
    case 591675926:
      return 30;
    case -1732403014:
      return 6;
    case -1113539877:
      return 6;
    case -2000237823:
      return 5;
    case 1145650264:
      if (paramInt2 == 373243460)
        return 1;
      return 25;
    case -2122953826:
      return 30;
    case 591737402:
      return 25;
    case 978876534:
      return 25;
    case 1147962727:
      return 3;
    case 2104860094:
      return 6;
    case 36:
      if ((paramInt2 == 373244477) || (paramInt2 == 487790868))
        return 25;
      return 6;
    case 607203907:
      return 30;
    case 2002295992:
      return 3;
    case 9241:
      return 6;
    case 9248:
      return 6;
    case 1619174053:
      return 6;
    case 960773209:
      return 25;
    case 9245:
      return 25;
    case 1919185554:
      return 1;
    case 9250:
      return 1;
    case 1080325989:
      return 30;
    case 1026982273:
      return 10;
    case 9253:
      return 25;
    case -2074573923:
      return 8;
    case 964209857:
      return 7;
    case 608780341:
      return 3;
    case 37:
      if (paramInt2 == 373244477)
        return 1;
      return 25;
    case 9493:
      return 25;
    case 9494:
      return 40;
    case -1514034520:
      return 8;
    case 2433561:
      return 25;
    case 1127298906:
      return 1;
    case 38:
      if (paramInt2 == 373244477)
        return 40;
      return 6;
    case 9760:
      return 22;
    case 39:
      if (paramInt2 == 23)
        return 9;
      if (paramInt2 == 373244477)
        return 40;
      return 25;
    case 9986:
      return 25;
    case 2570558:
      return 1;
    case 2021497500:
      return 10;
    case 959726687:
      return 6;
    case 876896124:
      return 10;
    case 10016:
      return 1;
    case 10019:
      return 1;
    case 40:
      return 1;
    case 2627089:
      return 40;
    case 979194486:
      return 5;
    case 910911090:
      return 6;
    case 591672680:
      return 7;
    case 591672681:
      return 7;
    case 708788029:
      return 1;
    default:
      if (PdfObject.debug)
        System.out.println("No type value set for " + paramInt1 + " getKeyType(int id) in PdfDictionay");
      break;
    }
    return i;
  }

  public static String showAsConstant(int paramInt)
  {
    Field[] arrayOfField = PdfDictionary.class.getFields();
    int i = arrayOfField.length;
    String str = null;
    for (int j = 0; j < i; j++)
      try
      {
        int k = arrayOfField[j].getInt(new PdfDictionary());
        if (k == paramInt)
        {
          str = "PdfDictionary." + arrayOfField[j].getName();
          i = j;
        }
      }
      catch (Exception localException)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog("Exception: " + localException.getMessage());
      }
    return str;
  }

  public static String showArrayType(int paramInt)
  {
    switch (paramInt)
    {
    case 9:
      return "VALUE_IS_INT_ARRAY";
    case 12:
      return "VALUE_IS_BOOLEAN_ARRAY";
    case 14:
      return "VALUE_IS_KEY_ARRAY";
    case 16:
      return "VALUE_IS_DOUBLE_ARRAY";
    case 18:
      return "VALUE_IS_MIXED_ARRAY";
    case 20:
      return "VALUE_IS_STRING_ARRAY";
    case 22:
      return "VALUE_IS_OBJECT_ARRAY";
    case 10:
    case 11:
    case 13:
    case 15:
    case 17:
    case 19:
    case 21:
    }
    return "not set";
  }

  public static int stringToInt(String paramString)
  {
    byte[] arrayOfByte = StringUtils.toBytes(paramString);
    return generateChecksum(0, arrayOfByte.length, arrayOfByte);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.raw.PdfDictionary
 * JD-Core Version:    0.6.2
 */