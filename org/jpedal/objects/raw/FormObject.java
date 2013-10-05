package org.jpedal.objects.raw;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;
import org.jpedal.color.DeviceCMYKColorSpace;
import org.jpedal.objects.acroforms.creation.GenericFormFactory;
import org.jpedal.objects.javascript.defaultactions.DisplayJavascriptActions;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.ObjectCloneFactory;
import org.jpedal.utils.StringUtils;

public class FormObject extends PdfObject
{
  private int formType = -1;
  Object guiComp = null;
  private int rawRotation = 0;
  private String EOPROPtype;
  private String Filter = null;
  private String Location = null;
  private String M;
  private String Reason;
  private String SubFilter;
  private byte[] rawEOPROPtype;
  private byte[] rawFilter;
  private byte[] rawLocation;
  private byte[] rawM;
  private byte[] rawReason;
  private byte[] rawSubFilter;
  private Color cColor;
  private String contents;
  private boolean show = false;
  private Map OptValues = null;
  private String validValue;
  public static final int READONLY_ID = 1;
  public static final int REQUIRED_ID = 2;
  public static final int NOEXPORT_ID = 3;
  public static final int MULTILINE_ID = 13;
  public static final int PASSWORD_ID = 14;
  public static final int NOTOGGLETOOFF_ID = 15;
  public static final int RADIO_ID = 16;
  public static final int PUSHBUTTON_ID = 17;
  public static final int COMBO_ID = 18;
  public static final int EDIT_ID = 19;
  public static final int SORT_ID = 20;
  public static final int FILESELECT_ID = 21;
  public static final int MULTISELECT_ID = 22;
  public static final int DONOTSPELLCHECK_ID = 23;
  public static final int DONOTSCROLL_ID = 24;
  public static final int COMB_ID = 25;
  public static final int RICHTEXT_ID = 26;
  public static final int RADIOINUNISON_ID = 26;
  public static final int COMMITONSELCHANGE_ID = 27;
  private static final int READONLY_BIT = 1;
  private static final int REQUIRED_BIT = 2;
  private static final int NOEXPORT_BIT = 4;
  private static final int MULTILINE_BIT = 4096;
  private static final int PASSWORD_BIT = 8192;
  private static final int NOTOGGLETOOFF_BIT = 16384;
  private static final int RADIO_BIT = 32768;
  private static final int PUSHBUTTON_BIT = 65536;
  private static final int COMBO_BIT = 131072;
  private static final int EDIT_BIT = 262144;
  private static final int SORT_BIT = 524288;
  private static final int FILESELECT_BIT = 1048576;
  private static final int MULTISELECT_BIT = 2097152;
  private static final int DONOTSPELLCHECK_BIT = 4194304;
  private static final int DONOTSCROLL_BIT = 8388608;
  private static final int COMB_BIT = 16777216;
  private static final int RADIOINUNISON_BIT = 33554432;
  private static final int RICHTEXT_BIT = 33554432;
  private static final int COMMITONSELCHANGE_BIT = 67108864;
  protected String[] OptString = null;
  protected boolean isXFAObject = false;
  private String parentRef;
  private PdfObject parentPdfObj;
  private String selectedItem;
  private Object[] selectedValues;
  private float[] textColor;
  private Font textFont;
  private int textSize = -1;
  private String textString = null;
  private String lastTextString = null;
  private boolean lastIsSelected = false;
  private boolean isSelected = false;
  private int selectionIndex = -1;
  private boolean appearancesUsed = false;
  private boolean offsetDownIcon = false;
  private boolean noDownIcon = false;
  private boolean invertDownIcon = false;
  private String normalOnState;
  private BufferedImage normalOffImage = null;
  private BufferedImage normalOnImage;
  private BufferedImage rolloverOffImage = null;
  private BufferedImage rolloverOnImage;
  private BufferedImage downOffImage = null;
  private BufferedImage downOnImage;
  public static final int POPUP = 1;
  private String layerName = null;
  private boolean[] Farray = null;
  protected Rectangle BBox = null;
  protected float[] C;
  protected float[] QuadPoints;
  protected float[] RD;
  protected float[] Rect;
  protected boolean[] flags = null;
  boolean Open = true;
  boolean H_Boolean = true;
  boolean NeedAppearances = false;
  protected int F = -1;
  protected int Ff = -1;
  protected int MaxLen = -1;
  protected int W = -1;
  protected int Q = -1;
  int SigFlags = -1;
  int StructParent = -1;
  protected int TI = -1;
  protected PdfObject A;
  private int popupFlag = 0;
  protected PdfObject AA;
  protected PdfObject AP = null;
  protected PdfObject Cdict;
  private PdfObject BI;
  protected PdfObject BS;
  protected PdfObject D;
  protected PdfObject IF;
  protected PdfObject RichMediaContent;
  protected int Flags = 0;
  private PdfObject MK;
  private PdfObject DC;
  private PdfObject DP;
  private PdfObject DR;
  private PdfObject DS;
  private PdfObject E;
  private PdfObject Fdict;
  private PdfObject Fo;
  private PdfObject FS;
  private PdfObject JS;
  private PdfObject K;
  private PdfObject Nobj;
  private PdfObject Next;
  private PdfObject O;
  private PdfObject PC;
  private PdfObject PI;
  private PdfObject PO;
  private PdfObject Popup;
  private PdfObject PV;
  private PdfObject R;
  private PdfObject Sig;
  private PdfObject Sound;
  private PdfObject U;
  private PdfObject V;
  private PdfObject Win;
  private PdfObject WP;
  private PdfObject WS;
  private PdfObject X;
  protected int[] ByteRange;
  protected int[] selectionIndices;
  protected byte[] rawAS;
  protected byte[] rawCert;
  protected byte[] rawContactInfo;
  protected byte[] rawContents;
  protected byte[] rawDstring;
  protected byte[] rawDA;
  protected byte[] rawDV;
  protected byte[] rawFstring;
  protected byte[] rawJS;
  protected byte[] rawH;
  protected byte[] rawN;
  protected byte[] rawNM;
  protected byte[] rawPstring;
  protected byte[] rawRC;
  protected byte[] rawS;
  protected byte[] rawSubj;
  protected byte[] rawT;
  protected byte[] rawTM;
  protected byte[] rawTU;
  protected byte[] rawURI;
  protected byte[] rawV;
  protected byte[] rawX;
  protected int FT = -1;
  protected String AS;
  protected String Cert;
  protected String ContactInfo;
  protected String Contents;
  protected String Dstring;
  protected String DA;
  protected String DV;
  protected String Fstring;
  protected String JSString;
  protected String H;
  protected String N;
  protected String NM;
  protected String Pstring;
  protected String RC;
  protected String S;
  protected String Subj;
  protected String T;
  protected String TM;
  protected String TU;
  protected String URI;
  protected String Vstring;
  private byte[][] Border;
  private byte[][] DmixedArray;
  private byte[][] Fields;
  private byte[][] State;
  private byte[][] rawXFAasArray;
  protected PdfObject Bl;
  protected PdfObject OC;
  protected PdfObject Off;
  protected PdfObject On;
  protected PdfObject P;
  private PdfObject XFAasStream;
  protected Object[] CO;
  protected Object[] InkList;
  protected Object[] Opt;
  protected Object[] Reference;
  protected byte[][] Kids;
  private String htmlName = null;

  public void setHTMLName(String paramString)
  {
    this.htmlName = paramString;
  }

  public String getHTMLName()
  {
    return this.htmlName;
  }

  public FormObject(String paramString)
  {
    super(paramString);
    this.objType = 373244477;
  }

  public FormObject(String paramString, boolean paramBoolean)
  {
    super(paramString);
    this.objType = 373244477;
    this.includeParent = paramBoolean;
  }

  public FormObject(int paramInt1, int paramInt2)
  {
    super(paramInt1, paramInt2);
    this.objType = 373244477;
  }

  public FormObject(int paramInt)
  {
    super(paramInt);
    this.objType = 373244477;
  }

  public FormObject()
  {
    this.objType = 373244477;
  }

  public FormObject(String paramString, int paramInt)
  {
    super(paramString);
    this.objType = 373244477;
    this.parentType = paramInt;
  }

  public boolean getBoolean(int paramInt)
  {
    switch (paramInt)
    {
    case 24:
      return this.H_Boolean;
    case -1483477783:
      return this.NeedAppearances;
    case 524301630:
      return this.Open;
    }
    return super.getBoolean(paramInt);
  }

  public void setBoolean(int paramInt, boolean paramBoolean)
  {
    switch (paramInt)
    {
    case 24:
      this.H_Boolean = paramBoolean;
      break;
    case -1483477783:
      this.NeedAppearances = paramBoolean;
      break;
    case 524301630:
      this.Open = paramBoolean;
      break;
    default:
      super.setBoolean(paramInt, paramBoolean);
    }
  }

  public void setActionFlag(int paramInt)
  {
    this.popupFlag = paramInt;
  }

  public int getActionFlag()
  {
    return this.popupFlag;
  }

  public PdfObject getDictionary(int paramInt)
  {
    PdfObject localPdfObject;
    switch (paramInt)
    {
    case 17:
      return this.A;
    case 4369:
      return this.AA;
    case 4384:
      if (this.AP == null)
        this.AP = new FormObject();
      return this.AP;
    case 4633:
      return this.BI;
    case 4668:
      return this.Bl;
    case 4643:
      if (this.BS == null)
      {
        if (this.parentPdfObj != null)
        {
          localPdfObject = this.parentPdfObj.getDictionary(4643);
          if (localPdfObject != null)
            return (PdfObject)localPdfObject.clone();
        }
        this.BS = new FormObject();
      }
      return this.BS;
    case 19:
      return this.Cdict;
    case 20:
      return this.D;
    case 5139:
      return this.DC;
    case 5152:
      return this.DP;
    case 5154:
      return this.DR;
    case 5155:
      return this.DS;
    case 21:
      return this.E;
    case 22:
      return this.Fdict;
    case 5695:
      return this.Fo;
    case 5667:
      return this.FS;
    case 6691:
      return this.JS;
    case 6422:
      return this.IF;
    case 27:
      return this.K;
    case 7451:
      if (this.MK == null)
      {
        if (this.parentPdfObj != null)
        {
          localPdfObject = this.parentPdfObj.getDictionary(7451);
          if (localPdfObject != null)
            return (PdfObject)localPdfObject.clone();
        }
        this.MK = new MKObject();
      }
      return this.MK;
    case 30:
      return this.Nobj;
    case 506808388:
      return this.Next;
    case 31:
      return this.O;
    case 7955:
      return this.OC;
    case 2045494:
      return this.Off;
    case 7998:
      return this.On;
    case 32:
      return this.P;
    case 8211:
      return this.PC;
    case 8217:
      return this.PI;
    case 8223:
      return this.PO;
    case 1061176672:
      return this.Popup;
    case 8230:
      return this.PV;
    case 34:
      return this.R;
    case -1263082253:
      return this.RichMediaContent;
    case 2308407:
      return this.Sig;
    case 1061502534:
      return this.Sound;
    case 37:
      return this.U;
    case 38:
      return this.V;
    case 2570558:
      return this.Win;
    case 10016:
      return this.WP;
    case 10019:
      return this.WS;
    case 40:
      return this.X;
    case 2627089:
      return this.XFAasStream;
    }
    return super.getDictionary(paramInt);
  }

  public void setIntNumber(int paramInt1, int paramInt2)
  {
    switch (paramInt1)
    {
    case 22:
      this.F = paramInt2;
      break;
    case 5686:
      this.Ff = paramInt2;
      commandFf(this.Ff);
      break;
    case 33:
      switch (paramInt2)
      {
      case 0:
        this.Q = 2;
        break;
      case 1:
        this.Q = 0;
        break;
      case 2:
        this.Q = 4;
        break;
      default:
        this.Q = 2;
      }
      break;
    case 1209815663:
      this.MaxLen = paramInt2;
      break;
    case 1144088180:
      if (this.MK == null)
        this.MK = new MKObject();
      if (this.rawRotation == 0)
      {
        this.MK.setIntNumber(34, paramInt2);
      }
      else
      {
        int i = this.rawRotation - paramInt2;
        if (i < 0)
          i = 360 + i;
        this.MK.setIntNumber(34, i);
      }
      break;
    case 1600810585:
      this.SigFlags = paramInt2;
      break;
    case -1732403014:
      this.StructParent = paramInt2;
      break;
    case 9241:
      this.TI = paramInt2;
      break;
    case 39:
      this.W = paramInt2;
      break;
    case 1009858393:
      this.Flags = paramInt2;
      break;
    default:
      super.setIntNumber(paramInt1, paramInt2);
    }
  }

  public int getInt(int paramInt)
  {
    switch (paramInt)
    {
    case 22:
      return this.F;
    case 5686:
      return this.Ff;
    case 1209815663:
      return this.MaxLen;
    case 33:
      return this.Q;
    case 1600810585:
      return this.SigFlags;
    case -1732403014:
      return this.StructParent;
    case 9241:
      return this.TI;
    case 39:
      return this.W;
    case 1009858393:
      return this.Flags;
    }
    return super.getInt(paramInt);
  }

  public void setDictionary(int paramInt, PdfObject paramPdfObject)
  {
    paramPdfObject.setID(paramInt);
    if (this.currentKey != null)
    {
      setOtherValues(paramPdfObject);
      return;
    }
    switch (paramInt)
    {
    case 17:
      this.A = paramPdfObject;
      break;
    case 4369:
      this.AA = paramPdfObject;
      break;
    case 4384:
      this.AP = paramPdfObject;
      if ((this.MK == null) && (this.AP != null) && (this.AP.getDictionary(30) != null))
        this.MK = this.AP.getDictionary(30).getDictionary(7451);
      break;
    case 4633:
      this.BI = paramPdfObject;
      break;
    case 4668:
      this.Bl = paramPdfObject;
      break;
    case 4643:
      this.BS = paramPdfObject;
      break;
    case 19:
      this.Cdict = paramPdfObject;
      break;
    case 20:
      this.D = paramPdfObject;
      break;
    case 5139:
      this.DC = paramPdfObject;
      break;
    case 5152:
      this.DP = paramPdfObject;
      break;
    case 5154:
      this.DR = paramPdfObject;
      break;
    case 5155:
      this.DS = paramPdfObject;
      break;
    case 21:
      this.E = paramPdfObject;
      break;
    case 22:
      this.Fdict = paramPdfObject;
      break;
    case 5695:
      this.Fo = paramPdfObject;
      break;
    case 5667:
      this.FS = paramPdfObject;
      break;
    case 6422:
      this.IF = paramPdfObject;
      break;
    case 6691:
      this.JS = paramPdfObject;
      break;
    case 27:
      this.K = paramPdfObject;
      break;
    case 7451:
      this.MK = paramPdfObject;
      break;
    case 30:
      this.Nobj = paramPdfObject;
      break;
    case 506808388:
      this.Next = paramPdfObject;
      break;
    case 31:
      this.O = paramPdfObject;
      break;
    case 7955:
      this.OC = paramPdfObject;
      break;
    case 2045494:
      this.Off = paramPdfObject;
      break;
    case 7998:
      this.On = paramPdfObject;
      break;
    case 32:
      this.P = paramPdfObject;
      break;
    case 8211:
      this.PC = paramPdfObject;
      break;
    case 8217:
      this.PI = paramPdfObject;
      break;
    case 8223:
      this.PO = paramPdfObject;
      break;
    case 1061176672:
      this.Popup = paramPdfObject;
      break;
    case 8230:
      this.PV = paramPdfObject;
      break;
    case 34:
      this.R = paramPdfObject;
      break;
    case -1263082253:
      this.RichMediaContent = paramPdfObject;
      break;
    case 2308407:
      this.Sig = paramPdfObject;
      break;
    case 1061502534:
      this.Sound = paramPdfObject;
      break;
    case 37:
      this.U = paramPdfObject;
      break;
    case 38:
      this.V = paramPdfObject;
      break;
    case 2570558:
      this.Win = paramPdfObject;
      break;
    case 10016:
      this.WP = paramPdfObject;
      break;
    case 10019:
      this.WS = paramPdfObject;
      break;
    case 40:
      this.X = paramPdfObject;
      break;
    case 2627089:
      this.XFAasStream = paramPdfObject;
      break;
    default:
      super.setDictionary(paramInt, paramPdfObject);
    }
  }

  public int setConstant(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte)
  {
    int i = -1;
    int j = 0;
    int k = 0;
    try
    {
      for (int n = paramInt3 - 1; n > -1; n--)
      {
        int m = paramArrayOfByte[(paramInt2 + n)];
        m -= 48;
        j += (m << k);
        k += 8;
      }
      switch (j)
      {
      }
      i = super.setConstant(paramInt1, j);
      if ((i == -1) && (debug))
      {
        byte[] arrayOfByte = new byte[paramInt3];
        System.arraycopy(paramArrayOfByte, paramInt2, arrayOfByte, 0, paramInt3);
        System.out.println("key=" + new String(arrayOfByte) + ' ' + j + " not implemented in setConstant in " + this);
        System.out.println("final public static int " + new String(arrayOfByte) + '=' + j + ';');
      }
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception: " + localException.getMessage());
    }
    switch (paramInt1)
    {
    }
    super.setConstant(paramInt1, j);
    return i;
  }

  public int getNameAsConstant(int paramInt)
  {
    byte[] arrayOfByte;
    switch (paramInt)
    {
    case 5668:
      return this.FT;
    case 24:
      arrayOfByte = this.rawH;
      break;
    case 30:
      arrayOfByte = this.rawN;
      break;
    case 35:
      arrayOfByte = this.rawS;
      break;
    case 40:
      arrayOfByte = this.rawX;
      break;
    default:
      return super.getNameAsConstant(paramInt);
    }
    if (arrayOfByte == null)
      return super.getNameAsConstant(paramInt);
    return PdfDictionary.generateChecksum(0, arrayOfByte.length, arrayOfByte);
  }

  public int getParameterConstant(int paramInt)
  {
    switch (paramInt)
    {
    case 1147962727:
      if (this.FT != -1)
        return this.FT;
      return super.getParameterConstant(paramInt);
    }
    return super.getParameterConstant(paramInt);
  }

  public PdfArrayIterator getMixedArray(int paramInt)
  {
    switch (paramInt)
    {
    case 1110722433:
      return new PdfArrayIterator(this.Border);
    case 20:
      return new PdfArrayIterator(this.DmixedArray);
    case 339034948:
      return new PdfArrayIterator(this.DmixedArray);
    case 893143676:
      return new PdfArrayIterator(this.Fields);
    case 1144079448:
      return new PdfArrayIterator(this.State);
    case 2627089:
      return new PdfArrayIterator(this.rawXFAasArray);
    }
    return super.getMixedArray(paramInt);
  }

  public byte[] getTextStreamValueAsByte(int paramInt)
  {
    switch (paramInt)
    {
    case 322257476:
      return this.rawCert;
    case 1568843969:
      return this.rawContactInfo;
    case 1216184967:
      return this.rawContents;
    case 5137:
      return this.rawDA;
    }
    return super.getTextStreamValueAsByte(paramInt);
  }

  public double[] getDoubleArray(int paramInt)
  {
    switch (paramInt)
    {
    }
    return super.getDoubleArray(paramInt);
  }

  public void setDoubleArray(int paramInt, double[] paramArrayOfDouble)
  {
    switch (paramInt)
    {
    }
    super.setDoubleArray(paramInt, paramArrayOfDouble);
  }

  public int[] getIntArray(int paramInt)
  {
    switch (paramInt)
    {
    case 25:
      return deepCopy(this.selectionIndices);
    case 2055367785:
      return deepCopy(this.ByteRange);
    }
    return super.getIntArray(paramInt);
  }

  public void setIntArray(int paramInt, int[] paramArrayOfInt)
  {
    switch (paramInt)
    {
    case 25:
      this.selectionIndices = paramArrayOfInt;
      break;
    case 2055367785:
      this.ByteRange = paramArrayOfInt;
      break;
    default:
      super.setIntArray(paramInt, paramArrayOfInt);
    }
  }

  public void setMixedArray(int paramInt, byte[][] paramArrayOfByte)
  {
    switch (paramInt)
    {
    case 1110722433:
      this.Border = paramArrayOfByte;
      break;
    case 339034948:
      this.DmixedArray = paramArrayOfByte;
      break;
    case 893143676:
      this.Fields = paramArrayOfByte;
      break;
    case 1144079448:
      this.State = paramArrayOfByte;
      break;
    case 2627089:
      this.rawXFAasArray = paramArrayOfByte;
      break;
    default:
      super.setMixedArray(paramInt, paramArrayOfByte);
    }
  }

  public float[] getFloatArray(int paramInt)
  {
    switch (paramInt)
    {
    case 19:
      return this.C;
    case 1785890247:
      return this.QuadPoints;
    case 573911876:
      return this.Rect;
    case 8724:
      return this.RD;
    }
    return super.getFloatArray(paramInt);
  }

  public void setFloatArray(int paramInt, float[] paramArrayOfFloat)
  {
    switch (paramInt)
    {
    case 19:
      this.C = paramArrayOfFloat;
      break;
    case 1785890247:
      this.QuadPoints = paramArrayOfFloat;
      break;
    case 8724:
      this.RD = paramArrayOfFloat;
      break;
    case 573911876:
      this.Rect = paramArrayOfFloat;
      break;
    default:
      super.setFloatArray(paramInt, paramArrayOfFloat);
    }
  }

  public void setName(int paramInt, byte[] paramArrayOfByte)
  {
    switch (paramInt)
    {
    case 4387:
      this.rawAS = paramArrayOfByte;
      break;
    case 5158:
      this.rawDV = paramArrayOfByte;
      break;
    case 1011108731:
      this.rawFilter = paramArrayOfByte;
      break;
    case -2122953826:
      this.rawSubFilter = paramArrayOfByte;
      break;
    case 5668:
      this.FT = PdfDictionary.generateChecksum(0, paramArrayOfByte.length, paramArrayOfByte);
      break;
    case 24:
      this.rawH = paramArrayOfByte;
      break;
    case 30:
      this.rawN = paramArrayOfByte;
      break;
    case 35:
      this.rawS = paramArrayOfByte;
      break;
    case 40:
      this.rawX = paramArrayOfByte;
      break;
    default:
      super.setName(paramInt, paramArrayOfByte);
    }
  }

  public void setObjectArray(int paramInt, Object[] paramArrayOfObject)
  {
    switch (paramInt)
    {
    case 4895:
      this.CO = paramArrayOfObject;
      break;
    case 475169151:
      this.InkList = paramArrayOfObject;
      break;
    case 2048068:
      this.Opt = paramArrayOfObject;
      break;
    case 1786013849:
      this.Reference = paramArrayOfObject;
      break;
    default:
      super.setObjectArray(paramInt, paramArrayOfObject);
    }
  }

  public Object[] getObjectArray(int paramInt)
  {
    switch (paramInt)
    {
    case 4895:
      return this.CO;
    case 475169151:
      return this.InkList;
    case 2048068:
      return this.Opt;
    case 1786013849:
      return this.Reference;
    }
    return super.getObjectArray(paramInt);
  }

  public byte[][] getStringArray(int paramInt)
  {
    switch (paramInt)
    {
    }
    return super.getStringArray(paramInt);
  }

  public void setStringArray(int paramInt, byte[][] paramArrayOfByte)
  {
    switch (paramInt)
    {
    }
    super.setStringArray(paramInt, paramArrayOfByte);
  }

  public void setTextStreamValue(int paramInt, byte[] paramArrayOfByte)
  {
    switch (paramInt)
    {
    case 322257476:
      this.rawCert = paramArrayOfByte;
      break;
    case 1568843969:
      this.rawContactInfo = paramArrayOfByte;
      break;
    case 1216184967:
      this.rawContents = paramArrayOfByte;
      break;
    case 20:
      this.rawDstring = paramArrayOfByte;
      break;
    case 5137:
      this.rawDA = paramArrayOfByte;
      break;
    case 5158:
      this.rawDV = paramArrayOfByte;
      break;
    case 1684763764:
      this.rawEOPROPtype = paramArrayOfByte;
      break;
    case 22:
      this.rawFstring = paramArrayOfByte;
      break;
    case 6691:
      this.rawJS = paramArrayOfByte;
      break;
    case 1618506351:
      this.rawLocation = paramArrayOfByte;
      break;
    case 29:
      this.rawM = paramArrayOfByte;
      break;
    case 32:
      this.rawPstring = paramArrayOfByte;
      break;
    case 8723:
      this.rawRC = paramArrayOfByte;
      break;
    case 826499443:
      this.rawReason = paramArrayOfByte;
      break;
    case 7709:
      this.rawNM = paramArrayOfByte;
      break;
    case 591737402:
      this.rawSubj = paramArrayOfByte;
      break;
    case 36:
      this.rawT = paramArrayOfByte;
      this.T = null;
      break;
    case 9245:
      this.rawTM = paramArrayOfByte;
      break;
    case 9253:
      this.rawTU = paramArrayOfByte;
      break;
    case 2433561:
      this.rawURI = paramArrayOfByte;
      break;
    case 38:
      this.rawV = paramArrayOfByte;
      this.Vstring = null;
      break;
    default:
      super.setTextStreamValue(paramInt, paramArrayOfByte);
    }
  }

  public void setTextStreamValue(int paramInt, String paramString)
  {
    switch (paramInt)
    {
    case 38:
      this.Vstring = paramString;
      break;
    case 36:
      setTextStreamValue(paramInt, StringUtils.toBytes(paramString));
      break;
    default:
      super.setTextStreamValue(paramInt, paramString);
    }
  }

  public String getName(int paramInt)
  {
    switch (paramInt)
    {
    case 4387:
      if ((this.AS == null) && (this.rawAS != null))
        this.AS = new String(this.rawAS);
      return this.AS;
    case 5668:
      return null;
    case 24:
      if ((this.H == null) && (this.rawH != null))
        this.H = new String(this.rawH);
      return this.H;
    case 1011108731:
      if ((this.Filter == null) && (this.rawFilter != null))
        this.Filter = new String(this.rawFilter);
      return this.Filter;
    case -2122953826:
      if ((this.SubFilter == null) && (this.rawSubFilter != null))
        this.SubFilter = new String(this.rawSubFilter);
      return this.SubFilter;
    case 30:
      if ((this.N == null) && (this.rawN != null))
        this.N = new String(this.rawN);
      return this.N;
    case 35:
      if ((this.S == null) && (this.rawS != null))
        this.S = new String(this.rawS);
      return this.S;
    case 40:
      if (this.rawX != null)
        return new String(this.rawX);
      break;
    }
    return super.getName(paramInt);
  }

  public String getTextStreamValue(int paramInt)
  {
    switch (paramInt)
    {
    case 322257476:
      if ((this.Cert == null) && (this.rawCert != null))
        this.Cert = StringUtils.getTextString(this.rawCert, false);
      return this.Cert;
    case 1568843969:
      if ((this.ContactInfo == null) && (this.rawContactInfo != null))
        this.ContactInfo = StringUtils.getTextString(this.rawContactInfo, false);
      return this.ContactInfo;
    case 1216184967:
      if ((this.Contents == null) && (this.rawContents != null))
        this.Contents = StringUtils.getTextString(this.rawContents, true);
      return this.Contents;
    case 20:
      if ((this.Dstring == null) && (this.rawDstring != null))
        this.Dstring = StringUtils.getTextString(this.rawDstring, false);
      return this.Dstring;
    case 5137:
      if ((this.DA == null) && (this.rawDA != null))
        this.DA = StringUtils.getTextString(this.rawDA, false);
      return this.DA;
    case 5158:
      if ((this.DV == null) && (this.rawDV != null))
        this.DV = StringUtils.getTextString(this.rawDV, true);
      return this.DV;
    case 1684763764:
      if ((this.EOPROPtype == null) && (this.rawEOPROPtype != null))
        this.EOPROPtype = new String(this.rawEOPROPtype);
      return this.EOPROPtype;
    case 22:
      if ((this.Fstring == null) && (this.rawFstring != null))
        this.Fstring = StringUtils.getTextString(this.rawFstring, false);
      return this.Fstring;
    case 6691:
      if ((this.JSString == null) && (this.rawJS != null))
        this.JSString = StringUtils.getTextString(this.rawJS, true);
      return this.JSString;
    case 7709:
      if ((this.NM == null) && (this.rawNM != null))
        this.NM = StringUtils.getTextString(this.rawNM, false);
      return this.NM;
    case 1618506351:
      if ((this.Location == null) && (this.rawLocation != null))
        this.Location = new String(this.rawLocation);
      return this.Location;
    case 29:
      if ((this.M == null) && (this.rawM != null))
        this.M = new String(this.rawM);
      return this.M;
    case 32:
      if ((this.Pstring == null) && (this.rawPstring != null))
        this.Pstring = StringUtils.getTextString(this.rawPstring, false);
      return this.Pstring;
    case 8723:
      if ((this.RC == null) && (this.rawRC != null))
        this.RC = new String(this.rawRC);
      return this.RC;
    case 826499443:
      if ((this.Reason == null) && (this.rawReason != null))
        this.Reason = new String(this.rawReason);
      return this.Reason;
    case 591737402:
      if ((this.Subj == null) && (this.rawSubj != null))
        this.Subj = StringUtils.getTextString(this.rawSubj, false);
      return this.Subj;
    case 36:
      if ((this.T == null) && (this.rawT != null))
        this.T = StringUtils.getTextString(this.rawT, false);
      if ((this.T == null) && (this.parentPdfObj != null))
        return this.parentPdfObj.getTextStreamValue(36);
      return this.T;
    case 9245:
      if ((this.TM == null) && (this.rawTM != null))
        this.TM = StringUtils.getTextString(this.rawTM, false);
      return this.TM;
    case 9253:
      if ((this.TU == null) && (this.rawTU != null))
        this.TU = StringUtils.getTextString(this.rawTU, false);
      return this.TU;
    case 2433561:
      if ((this.URI == null) && (this.rawURI != null))
        this.URI = StringUtils.getTextString(this.rawURI, false);
      return this.URI;
    case 38:
      if ((this.Vstring == null) && (this.rawV != null))
        this.Vstring = StringUtils.getTextString(this.rawV, true);
      return this.Vstring;
    }
    return super.getTextStreamValue(paramInt);
  }

  public String getStringValue(int paramInt1, int paramInt2)
  {
    byte[] arrayOfByte1 = null;
    switch (paramInt2)
    {
    case 0:
      if (arrayOfByte1 != null)
        return new String(arrayOfByte1);
      return null;
    case 1:
      if (arrayOfByte1 != null)
        return new String(arrayOfByte1);
      return null;
    case 2:
      if (arrayOfByte1 != null)
      {
        int i = arrayOfByte1.length;
        if ((i > 6) && (arrayOfByte1[6] == 43))
        {
          int j = i - 7;
          byte[] arrayOfByte2 = new byte[j];
          System.arraycopy(arrayOfByte1, 7, arrayOfByte2, 0, j);
          return new String(arrayOfByte2);
        }
        return new String(arrayOfByte1);
      }
      return null;
    }
    throw new RuntimeException("Value not defined in getName(int,mode) in " + this);
  }

  public boolean hasKeyArray(int paramInt)
  {
    switch (paramInt)
    {
    case 456733763:
      return (this.Kids != null) && (this.Kids.length > 0);
    }
    return false;
  }

  public byte[][] getKeyArray(int paramInt)
  {
    switch (paramInt)
    {
    case 456733763:
      return deepCopy(this.Kids);
    }
    return super.getKeyArray(paramInt);
  }

  public void setKeyArray(int paramInt, byte[][] paramArrayOfByte)
  {
    switch (paramInt)
    {
    case 456733763:
      this.Kids = paramArrayOfByte;
      break;
    default:
      super.setKeyArray(paramInt, paramArrayOfByte);
    }
  }

  public boolean decompressStreamWhenRead()
  {
    return true;
  }

  protected void commandFf(int paramInt)
  {
    this.flags = new boolean[32];
    this.flags[1] = ((paramInt & 0x1) == 1 ? 1 : false);
    this.flags[2] = ((paramInt & 0x2) == 2 ? 1 : false);
    this.flags[3] = ((paramInt & 0x4) == 4 ? 1 : false);
    this.flags[13] = ((paramInt & 0x1000) == 4096 ? 1 : false);
    this.flags[14] = ((paramInt & 0x2000) == 8192 ? 1 : false);
    this.flags[15] = ((paramInt & 0x4000) == 16384 ? 1 : false);
    this.flags[16] = ((paramInt & 0x8000) == 32768 ? 1 : false);
    this.flags[17] = ((paramInt & 0x10000) == 65536 ? 1 : false);
    this.flags[18] = ((paramInt & 0x20000) == 131072 ? 1 : false);
    this.flags[19] = ((paramInt & 0x40000) == 262144 ? 1 : false);
    this.flags[20] = ((paramInt & 0x80000) == 524288 ? 1 : false);
    this.flags[21] = ((paramInt & 0x100000) == 1048576 ? 1 : false);
    this.flags[22] = ((paramInt & 0x200000) == 2097152 ? 1 : false);
    this.flags[23] = ((paramInt & 0x400000) == 4194304 ? 1 : false);
    this.flags[24] = ((paramInt & 0x800000) == 8388608 ? 1 : false);
    this.flags[25] = ((paramInt & 0x1000000) == 16777216 ? 1 : false);
    this.flags[26] = ((paramInt & 0x2000000) == 33554432 ? 1 : false);
    this.flags[26] = ((paramInt & 0x2000000) == 33554432 ? 1 : false);
    this.flags[27] = ((paramInt & 0x4000000) == 67108864 ? 1 : false);
  }

  public static Color generateColor(float[] paramArrayOfFloat)
  {
    int i = -1;
    if (paramArrayOfFloat != null)
      i = paramArrayOfFloat.length;
    Color localColor = null;
    if (i == 0)
    {
      localColor = new Color(0, 0, 0, 0);
    }
    else
    {
      float f1;
      if (i == 1)
      {
        f1 = paramArrayOfFloat[0];
        if (f1 <= 1.0F)
          localColor = new Color(f1, f1, f1);
        else
          localColor = new Color((int)f1, (int)f1, (int)f1);
      }
      else if (i == 3)
      {
        if (debug)
          System.out.println("rgb color=" + paramArrayOfFloat[0] + ' ' + paramArrayOfFloat[1] + ' ' + paramArrayOfFloat[2]);
        f1 = paramArrayOfFloat[0];
        float f2 = paramArrayOfFloat[1];
        float f3 = paramArrayOfFloat[2];
        if ((f1 <= 1.0F) && (f2 <= 1.0F) && (f3 <= 1.0F))
          localColor = new Color(f1, f2, f3);
        else
          localColor = new Color((int)f1, (int)f2, (int)f3);
      }
      else if (i == 4)
      {
        DeviceCMYKColorSpace localDeviceCMYKColorSpace = new DeviceCMYKColorSpace();
        localDeviceCMYKColorSpace.setColor(new float[] { paramArrayOfFloat[3], paramArrayOfFloat[2], paramArrayOfFloat[1], paramArrayOfFloat[0] }, 4);
        localColor = (Color)localDeviceCMYKColorSpace.getColor();
      }
    }
    return localColor;
  }

  public boolean isXFAObject()
  {
    return this.isXFAObject;
  }

  public PdfObject duplicate()
  {
    FormObject localFormObject = new FormObject();
    localFormObject.AS = this.AS;
    localFormObject.contents = this.contents;
    localFormObject.Cert = this.Cert;
    localFormObject.ContactInfo = this.ContactInfo;
    localFormObject.contents = this.contents;
    localFormObject.Contents = this.Contents;
    localFormObject.DA = this.DA;
    localFormObject.Dstring = this.Dstring;
    localFormObject.DV = this.DV;
    localFormObject.Filter = this.Filter;
    localFormObject.Fstring = this.Fstring;
    localFormObject.H = this.H;
    localFormObject.JSString = this.JSString;
    localFormObject.layerName = this.layerName;
    localFormObject.Location = this.Location;
    localFormObject.M = this.M;
    localFormObject.N = this.N;
    localFormObject.NM = this.NM;
    localFormObject.normalOnState = this.normalOnState;
    localFormObject.Pstring = this.Pstring;
    localFormObject.ref = this.ref;
    localFormObject.RC = this.RC;
    localFormObject.Reason = this.Reason;
    localFormObject.S = this.S;
    localFormObject.selectedItem = this.selectedItem;
    localFormObject.SubFilter = this.SubFilter;
    localFormObject.Subj = this.Subj;
    localFormObject.T = this.T;
    localFormObject.TM = this.TM;
    localFormObject.TU = this.TU;
    localFormObject.textString = this.textString;
    localFormObject.URI = this.URI;
    localFormObject.Vstring = this.Vstring;
    localFormObject.F = this.F;
    localFormObject.Ff = this.Ff;
    localFormObject.formType = this.formType;
    localFormObject.FT = this.FT;
    localFormObject.MaxLen = this.MaxLen;
    localFormObject.pageNumber = this.pageNumber;
    localFormObject.popupFlag = this.popupFlag;
    localFormObject.Q = this.Q;
    localFormObject.rawRotation = this.rawRotation;
    localFormObject.SigFlags = this.SigFlags;
    localFormObject.StructParent = this.StructParent;
    localFormObject.textSize = this.textSize;
    localFormObject.TI = this.TI;
    localFormObject.W = this.W;
    localFormObject.appearancesUsed = this.appearancesUsed;
    localFormObject.offsetDownIcon = this.offsetDownIcon;
    localFormObject.noDownIcon = this.noDownIcon;
    localFormObject.invertDownIcon = this.invertDownIcon;
    localFormObject.show = this.show;
    localFormObject.H_Boolean = this.H_Boolean;
    localFormObject.NeedAppearances = this.NeedAppearances;
    localFormObject.isXFAObject = this.isXFAObject;
    localFormObject.Open = this.Open;
    localFormObject.textFont = this.textFont;
    localFormObject.cColor = this.cColor;
    localFormObject.OptString = (this.OptString == null ? null : (String[])this.OptString.clone());
    localFormObject.flags = (this.flags == null ? null : (boolean[])this.flags.clone());
    localFormObject.Farray = (this.Farray == null ? null : (boolean[])this.Farray.clone());
    localFormObject.selectionIndices = (this.selectionIndices == null ? null : (int[])this.selectionIndices.clone());
    localFormObject.C = (this.C == null ? null : (float[])this.C.clone());
    localFormObject.InkList = (this.InkList == null ? null : (Object[])this.InkList.clone());
    localFormObject.QuadPoints = (this.QuadPoints == null ? null : (float[])this.QuadPoints.clone());
    localFormObject.Rect = (this.Rect == null ? null : (float[])this.Rect.clone());
    localFormObject.RD = (this.RD == null ? null : (float[])this.RD.clone());
    localFormObject.textColor = (this.textColor == null ? null : (float[])this.textColor.clone());
    localFormObject.A = this.A.duplicate();
    localFormObject.AA = this.AA.duplicate();
    localFormObject.AP = this.AP.duplicate();
    localFormObject.BS = this.BS.duplicate();
    localFormObject.BI = this.BI.duplicate();
    localFormObject.Bl = this.Bl.duplicate();
    localFormObject.Cdict = this.Cdict.duplicate();
    localFormObject.D = this.D.duplicate();
    localFormObject.DC = this.DC.duplicate();
    localFormObject.DP = this.DP.duplicate();
    localFormObject.DS = this.DS.duplicate();
    localFormObject.E = this.E.duplicate();
    localFormObject.Fdict = this.Fdict.duplicate();
    localFormObject.Fo = this.Fo.duplicate();
    localFormObject.FS = this.FS.duplicate();
    localFormObject.IF = this.IF.duplicate();
    localFormObject.JS = this.JS.duplicate();
    localFormObject.K = this.K.duplicate();
    localFormObject.MK = this.MK.duplicate();
    localFormObject.Next = this.Next.duplicate();
    localFormObject.Nobj = this.Nobj.duplicate();
    localFormObject.O = this.O.duplicate();
    localFormObject.OC = this.OC.duplicate();
    localFormObject.Off = this.Off.duplicate();
    localFormObject.On = this.On.duplicate();
    localFormObject.P = this.P.duplicate();
    localFormObject.PC = this.PC.duplicate();
    localFormObject.PI = this.PI.duplicate();
    localFormObject.PO = this.PO.duplicate();
    localFormObject.Popup = this.Popup.duplicate();
    localFormObject.PV = this.PV.duplicate();
    localFormObject.R = this.R.duplicate();
    localFormObject.Sig = this.Sig.duplicate();
    localFormObject.Sound = this.Sound.duplicate();
    localFormObject.U = this.U.duplicate();
    localFormObject.V = this.V.duplicate();
    localFormObject.Win = this.Win.duplicate();
    localFormObject.WP = this.WP.duplicate();
    localFormObject.WS = this.WS.duplicate();
    localFormObject.X = this.X.duplicate();
    localFormObject.XFAasStream = this.XFAasStream.duplicate();
    localFormObject.CO = (this.CO == null ? null : (Object[])this.CO.clone());
    localFormObject.Opt = (this.Opt == null ? null : (Object[])this.Opt.clone());
    localFormObject.Reference = (this.Reference == null ? null : (Object[])this.Reference.clone());
    localFormObject.rawAS = (this.rawAS == null ? null : (byte[])this.rawAS.clone());
    localFormObject.rawCert = (this.rawCert == null ? null : (byte[])this.rawCert.clone());
    localFormObject.rawContactInfo = (this.rawContactInfo == null ? null : (byte[])this.rawContactInfo.clone());
    localFormObject.rawContents = (this.rawContents == null ? null : (byte[])this.rawContents.clone());
    localFormObject.rawDA = (this.rawDA == null ? null : (byte[])this.rawDA.clone());
    localFormObject.rawDstring = (this.rawDstring == null ? null : (byte[])this.rawDstring.clone());
    localFormObject.rawDV = (this.rawDV == null ? null : (byte[])this.rawDV.clone());
    localFormObject.rawEOPROPtype = (this.rawEOPROPtype == null ? null : (byte[])this.rawEOPROPtype.clone());
    localFormObject.rawFilter = (this.rawFilter == null ? null : (byte[])this.rawFilter.clone());
    localFormObject.rawFstring = (this.rawFstring == null ? null : (byte[])this.rawFstring.clone());
    localFormObject.rawH = (this.rawH == null ? null : (byte[])this.rawH.clone());
    localFormObject.rawJS = (this.rawJS == null ? null : (byte[])this.rawJS.clone());
    localFormObject.rawLocation = (this.rawLocation == null ? null : (byte[])this.rawLocation.clone());
    localFormObject.rawM = (this.rawM == null ? null : (byte[])this.rawM.clone());
    localFormObject.rawN = (this.rawN == null ? null : (byte[])this.rawN.clone());
    localFormObject.rawNM = (this.rawNM == null ? null : (byte[])this.rawNM.clone());
    localFormObject.rawPstring = (this.rawPstring == null ? null : (byte[])this.rawPstring.clone());
    localFormObject.rawRC = (this.rawRC == null ? null : (byte[])this.rawRC.clone());
    localFormObject.rawReason = (this.rawReason == null ? null : (byte[])this.rawReason.clone());
    localFormObject.rawS = (this.rawS == null ? null : (byte[])this.rawS.clone());
    localFormObject.rawSubFilter = (this.rawSubFilter == null ? null : (byte[])this.rawSubFilter.clone());
    localFormObject.rawSubj = (this.rawSubj == null ? null : (byte[])this.rawSubj.clone());
    localFormObject.rawT = (this.rawT == null ? null : (byte[])this.rawT.clone());
    localFormObject.rawTM = (this.rawTM == null ? null : (byte[])this.rawTM.clone());
    localFormObject.rawTU = (this.rawTU == null ? null : (byte[])this.rawTU.clone());
    localFormObject.rawURI = (this.rawURI == null ? null : (byte[])this.rawURI.clone());
    localFormObject.rawV = (this.rawV == null ? null : (byte[])this.rawV.clone());
    localFormObject.rawX = (this.rawX == null ? null : (byte[])this.rawX.clone());
    localFormObject.Border = (this.Border == null ? (byte[][])null : ObjectCloneFactory.cloneDoubleArray(this.Border));
    localFormObject.DmixedArray = (this.DmixedArray == null ? (byte[][])null : ObjectCloneFactory.cloneDoubleArray(this.DmixedArray));
    localFormObject.Fields = (this.Fields == null ? (byte[][])null : ObjectCloneFactory.cloneDoubleArray(this.Fields));
    localFormObject.rawXFAasArray = (this.rawXFAasArray == null ? (byte[][])null : ObjectCloneFactory.cloneDoubleArray(this.rawXFAasArray));
    localFormObject.State = (this.State == null ? (byte[][])null : ObjectCloneFactory.cloneDoubleArray(this.State));
    localFormObject.normalOffImage = ObjectCloneFactory.deepCopy(this.normalOffImage);
    localFormObject.normalOnImage = ObjectCloneFactory.deepCopy(this.normalOnImage);
    localFormObject.rolloverOffImage = ObjectCloneFactory.deepCopy(this.rolloverOffImage);
    localFormObject.rolloverOnImage = ObjectCloneFactory.deepCopy(this.rolloverOnImage);
    localFormObject.downOffImage = ObjectCloneFactory.deepCopy(this.downOffImage);
    localFormObject.downOnImage = ObjectCloneFactory.deepCopy(this.downOnImage);
    localFormObject.OptValues = ObjectCloneFactory.cloneMap(this.OptValues);
    return localFormObject;
  }

  public void copyInheritedValuesFromParent(FormObject paramFormObject)
  {
    if (paramFormObject == null)
      return;
    if ((this.pageNumber == -1) && (paramFormObject.pageNumber != -1))
      this.pageNumber = paramFormObject.pageNumber;
    if (this.rawAS == null)
      this.rawAS = paramFormObject.rawAS;
    if (this.rawDA == null)
      this.rawDA = paramFormObject.rawDA;
    if (this.rawDV == null)
      this.rawDV = paramFormObject.rawDV;
    if (this.rawJS == null)
      this.rawJS = paramFormObject.rawJS;
    if (this.rawNM == null)
      this.rawNM = paramFormObject.rawNM;
    if (this.rawTM == null)
      this.rawTM = paramFormObject.rawTM;
    if (this.rawTU == null)
      this.rawTU = paramFormObject.rawTU;
    if (this.rawV == null)
      this.rawV = paramFormObject.rawV;
    if ((paramFormObject.T == null) && (paramFormObject.rawT != null))
      paramFormObject.T = StringUtils.getTextString(paramFormObject.rawT, false);
    if (paramFormObject.T != null)
    {
      if ((this.T == null) && (this.rawT != null))
        this.T = StringUtils.getTextString(this.rawT, false);
      if ((this.T != null) && (!this.T.contains(paramFormObject.T)))
      {
        this.T = (paramFormObject.T + '.' + this.T);
        this.rawT = StringUtils.toBytes(this.T);
      }
    }
    if (this.A == null)
      this.A = paramFormObject.A;
    if (this.AA == null)
      this.AA = paramFormObject.AA;
    if (this.AP == null)
      this.AP = paramFormObject.AP;
    if (this.D == null)
      this.D = paramFormObject.D;
    if (this.OC == null)
      this.OC = paramFormObject.OC;
    if (this.C == null)
      this.C = (paramFormObject.C == null ? null : (float[])paramFormObject.C.clone());
    if (this.QuadPoints == null)
      this.QuadPoints = (paramFormObject.QuadPoints == null ? null : (float[])paramFormObject.QuadPoints.clone());
    if (this.InkList == null)
      this.InkList = (paramFormObject.InkList == null ? null : (Object[])paramFormObject.InkList.clone());
    if (this.Rect == null)
      this.Rect = (paramFormObject.Rect == null ? null : (float[])paramFormObject.Rect.clone());
    if (this.F == -1)
      this.F = paramFormObject.F;
    if (this.Ff == -1)
      this.Ff = paramFormObject.Ff;
    if (this.Q == -1)
      this.Q = paramFormObject.Q;
    if (this.MaxLen == -1)
      this.MaxLen = paramFormObject.MaxLen;
    if (this.FT == -1)
      this.FT = paramFormObject.FT;
    if (this.TI == -1)
      this.TI = paramFormObject.TI;
    if (this.flags == null)
      this.flags = (paramFormObject.flags == null ? null : (boolean[])paramFormObject.flags.clone());
    if (this.Opt == null)
      this.Opt = (paramFormObject.Opt == null ? null : (Object[])paramFormObject.Opt.clone());
    if (this.CO == null)
      this.CO = (paramFormObject.CO == null ? null : (Object[])paramFormObject.CO.clone());
    if (this.textString == null)
      this.textString = paramFormObject.textString;
    if (this.OptString == null)
      this.OptString = paramFormObject.OptString;
    if (this.selectedItem == null)
      this.selectedItem = paramFormObject.selectedItem;
  }

  public int getAlignment()
  {
    if (this.Q == -1)
      this.Q = 2;
    return this.Q;
  }

  public void setTextColor(float[] paramArrayOfFloat)
  {
    if ((paramArrayOfFloat.length > 0) && (Float.isNaN(paramArrayOfFloat[0])))
    {
      float[] arrayOfFloat = new float[paramArrayOfFloat.length - 1];
      System.arraycopy(paramArrayOfFloat, 1, arrayOfFloat, 0, paramArrayOfFloat.length - 1);
      paramArrayOfFloat = arrayOfFloat;
    }
    this.textColor = paramArrayOfFloat;
  }

  private void setTextColor(Object paramObject)
  {
    if (paramObject != null)
    {
      float[] arrayOfFloat = DisplayJavascriptActions.convertToColorFloatArray((String)paramObject);
      this.textColor = arrayOfFloat;
    }
  }

  public void setTextFont(Font paramFont)
  {
    this.textFont = paramFont;
  }

  public void setTextSize(int paramInt)
  {
    this.textSize = paramInt;
  }

  public void setTextValue(String paramString)
  {
    if (paramString == null)
      paramString = "";
    updateValue(paramString, false, true);
  }

  public void setSelection(Object[] paramArrayOfObject, String paramString, int[] paramArrayOfInt, int paramInt)
  {
    if (paramInt != this.selectionIndex)
    {
      this.selectedValues = paramArrayOfObject;
      this.selectedItem = paramString;
      this.selectionIndex = paramInt;
      this.selectionIndices = paramArrayOfInt;
      if (this.guiComp != null)
        updateCombo();
    }
  }

  public void setFieldName(String paramString)
  {
    this.T = null;
    setTextStreamValue(36, StringUtils.toBytes(paramString));
  }

  public void setParent(String paramString)
  {
    setParent(paramString, null, false);
  }

  public void setParent(String paramString, FormObject paramFormObject, boolean paramBoolean)
  {
    if (paramBoolean)
      copyInheritedValuesFromParent(paramFormObject);
    this.parentRef = paramString;
    if (paramFormObject != null)
      this.parentPdfObj = paramFormObject;
  }

  public PdfObject getParentPdfObj()
  {
    return this.parentPdfObj;
  }

  public String getParentRef()
  {
    if ((this.parentRef == null) && (this.includeParent))
      return getStringKey(1110793845);
    return this.parentRef;
  }

  private static boolean[] calcFarray(int paramInt)
  {
    if (paramInt == 0)
      return new boolean[10];
    boolean[] arrayOfBoolean = new boolean[10];
    int[] arrayOfInt = { 0, 1, 2, 4, 8, 16, 32, 64, 128, 256, 512 };
    for (int i = 1; i < 10; i++)
      if ((paramInt & arrayOfInt[i]) == arrayOfInt[i])
        arrayOfBoolean[(i - 1)] = true;
    return arrayOfBoolean;
  }

  public Rectangle getBoundingRectangle()
  {
    float[] arrayOfFloat = getFloatArray(573911876);
    if (arrayOfFloat != null)
    {
      float f1 = arrayOfFloat[0];
      float f2 = arrayOfFloat[1];
      float f3 = arrayOfFloat[2];
      float f4 = arrayOfFloat[3];
      float f5;
      if (f1 > f3)
      {
        f5 = f1;
        f1 = f3;
        f3 = f5;
      }
      if (f2 > f4)
      {
        f5 = f2;
        f2 = f4;
        f4 = f5;
      }
      int i = (int)f1;
      int j = (int)f2;
      int k = (int)f3 + (f3 - (int)f3 > 0.0F ? 1 : 0);
      int m = (int)f4 + (f4 - (int)f4 > 0.0F ? 1 : 0);
      this.BBox = new Rectangle(i, j, k - i, m - j);
    }
    else
    {
      return this.BBox;
    }
    return this.BBox;
  }

  public void setType(int paramInt, boolean paramBoolean)
  {
    if (paramBoolean)
      this.FT = paramInt;
  }

  public void setFlag(int paramInt, boolean paramBoolean)
  {
    if (this.flags == null)
      this.flags = new boolean[32];
    this.flags[paramInt] = paramBoolean;
  }

  public boolean[] getFieldFlags()
  {
    if (this.flags == null)
      this.flags = new boolean[32];
    return this.flags;
  }

  public void setNormalOnState(String paramString)
  {
    this.normalOnState = paramString;
  }

  public boolean isAppearanceUsed()
  {
    return this.appearancesUsed;
  }

  public void setAppreancesUsed(boolean paramBoolean)
  {
    this.appearancesUsed = paramBoolean;
  }

  public void setAppearanceImage(BufferedImage paramBufferedImage, int paramInt1, int paramInt2)
  {
    if (paramBufferedImage == null)
      paramBufferedImage = getOpaqueImage();
    switch (paramInt1)
    {
    case 20:
      if (paramInt2 == 7998)
        this.downOnImage = paramBufferedImage;
      else if (paramInt2 == 2045494)
        this.downOffImage = paramBufferedImage;
      else
        throw new RuntimeException("Unknown status use PdfDictionary.On or PdfDictionary.Off");
      break;
    case 30:
      if (paramInt2 == 7998)
        this.normalOnImage = paramBufferedImage;
      else if (paramInt2 == 2045494)
        this.normalOffImage = paramBufferedImage;
      else
        throw new RuntimeException("Unknown status use PdfDictionary.On or PdfDictionary.Off");
      break;
    case 34:
      if (paramInt2 == 7998)
        this.rolloverOnImage = paramBufferedImage;
      else if (paramInt2 == 2045494)
        this.rolloverOffImage = paramBufferedImage;
      else
        throw new RuntimeException("Unknown status use PdfDictionary.On or PdfDictionary.Off");
      break;
    default:
      throw new RuntimeException("Unknown type use PdfDictionary.D, PdfDictionary.N or PdfDictionary.R");
    }
    this.appearancesUsed = true;
  }

  public void setBorderColor(String paramString)
  {
    if (paramString != null)
      getDictionary(7451).setFloatArray(4627, generateFloatFromString(paramString));
  }

  public void setBackgroundColor(String paramString)
  {
    if (paramString != null)
      getDictionary(7451).setFloatArray(4631, generateFloatFromString(paramString));
  }

  private static float[] generateFloatFromString(String paramString)
  {
    if (debug)
      System.out.println("CHECK generateColorFromString=" + paramString);
    StringTokenizer localStringTokenizer = new StringTokenizer(paramString, "[()] ,");
    float[] arrayOfFloat = new float[localStringTokenizer.countTokens()];
    for (int i = 0; localStringTokenizer.hasMoreTokens(); i++)
    {
      String str = localStringTokenizer.nextToken();
      if (debug)
        System.out.println("token" + (i + 1) + '=' + str + ' ' + paramString);
      arrayOfFloat[i] = Float.parseFloat(str);
    }
    if (i == 0)
      return null;
    return arrayOfFloat;
  }

  public void setNormalCaption(String paramString)
  {
    if (paramString != null)
      getDictionary(7451).setTextStreamValue(4881, StringUtils.toBytes(paramString));
  }

  protected void setOffsetDownApp()
  {
    this.offsetDownIcon = true;
  }

  protected void setNoDownIcon()
  {
    this.noDownIcon = true;
  }

  protected void setInvertForDownIcon()
  {
    this.invertDownIcon = true;
  }

  public boolean hasNormalOff()
  {
    return this.normalOffImage != null;
  }

  public boolean hasRolloverOff()
  {
    return this.rolloverOffImage != null;
  }

  public boolean hasDownOff()
  {
    return this.downOffImage != null;
  }

  public boolean hasDownImage()
  {
    return (this.downOnImage != null) || (hasDownOff());
  }

  public void overwriteWith(FormObject paramFormObject)
  {
    if (paramFormObject == null)
      return;
    if (paramFormObject.parentRef != null)
      this.parentRef = paramFormObject.parentRef;
    if (paramFormObject.flags != null)
      this.flags = ((boolean[])paramFormObject.flags.clone());
    if (paramFormObject.selectionIndices != null)
      this.selectionIndices = ((int[])paramFormObject.selectionIndices.clone());
    if (paramFormObject.selectedItem != null)
      this.selectedItem = paramFormObject.selectedItem;
    if (paramFormObject.ref != null)
      this.ref = paramFormObject.ref;
    if (paramFormObject.textColor != null)
      this.textColor = ((float[])paramFormObject.textColor.clone());
    if (paramFormObject.textFont != null)
      this.textFont = paramFormObject.textFont;
    if (paramFormObject.textSize != -1)
      this.textSize = paramFormObject.textSize;
    if (paramFormObject.textString != null)
      this.textString = paramFormObject.textString;
    if (paramFormObject.appearancesUsed)
      this.appearancesUsed = paramFormObject.appearancesUsed;
    if (paramFormObject.offsetDownIcon)
      this.offsetDownIcon = paramFormObject.offsetDownIcon;
    if (paramFormObject.noDownIcon)
      this.noDownIcon = paramFormObject.noDownIcon;
    if (paramFormObject.invertDownIcon)
      this.invertDownIcon = paramFormObject.invertDownIcon;
    if (paramFormObject.normalOffImage != null)
      this.normalOffImage = paramFormObject.normalOffImage;
    if (paramFormObject.normalOnImage != null)
      this.normalOnImage = paramFormObject.normalOnImage;
    if (paramFormObject.rolloverOffImage != null)
      this.rolloverOffImage = paramFormObject.rolloverOffImage;
    if (paramFormObject.rolloverOnImage != null)
      this.rolloverOnImage = paramFormObject.rolloverOnImage;
    if (paramFormObject.downOffImage != null)
      this.downOffImage = paramFormObject.downOffImage;
    if (paramFormObject.downOnImage != null)
      this.downOnImage = paramFormObject.downOnImage;
    if (paramFormObject.pageNumber != -1)
      this.pageNumber = paramFormObject.pageNumber;
    if (paramFormObject.cColor != null)
      this.cColor = paramFormObject.cColor;
    if (paramFormObject.contents != null)
      this.contents = paramFormObject.contents;
    if (paramFormObject.show)
      this.show = paramFormObject.show;
    this.AA = paramFormObject.AA;
    this.AP = paramFormObject.AP;
    this.BS = paramFormObject.BS;
    this.D = paramFormObject.D;
    this.OC = paramFormObject.OC;
    this.C = (paramFormObject.C == null ? null : (float[])paramFormObject.C.clone());
    this.QuadPoints = (paramFormObject.QuadPoints == null ? null : (float[])paramFormObject.QuadPoints.clone());
    this.InkList = (paramFormObject.InkList == null ? null : (Object[])paramFormObject.InkList.clone());
    this.F = paramFormObject.F;
    this.Ff = paramFormObject.Ff;
    this.CO = (paramFormObject.CO == null ? null : (Object[])paramFormObject.CO.clone());
    this.Opt = (paramFormObject.Opt == null ? null : (Object[])paramFormObject.Opt.clone());
    this.Q = paramFormObject.Q;
    this.MaxLen = paramFormObject.MaxLen;
    this.FT = paramFormObject.FT;
    this.rawAS = (paramFormObject.rawAS == null ? null : (byte[])paramFormObject.rawAS.clone());
    this.rawDA = (paramFormObject.rawDA == null ? null : (byte[])paramFormObject.rawDA.clone());
    this.rawDV = (paramFormObject.rawDV == null ? null : (byte[])paramFormObject.rawDV.clone());
    this.rawJS = (paramFormObject.rawJS == null ? null : (byte[])paramFormObject.rawJS.clone());
    this.rawNM = (paramFormObject.rawNM == null ? null : (byte[])paramFormObject.rawNM.clone());
    this.rawTM = (paramFormObject.rawTM == null ? null : (byte[])paramFormObject.rawTM.clone());
    this.rawTU = (paramFormObject.rawTU == null ? null : (byte[])paramFormObject.rawTU.clone());
    this.rawV = (paramFormObject.rawV == null ? null : (byte[])paramFormObject.rawV.clone());
    this.T = paramFormObject.T;
    this.rawT = (paramFormObject.rawT == null ? null : (byte[])paramFormObject.rawT.clone());
    this.Rect = (paramFormObject.Rect == null ? null : (float[])paramFormObject.Rect.clone());
    this.TI = paramFormObject.TI;
    this.MK = (paramFormObject.MK == null ? null : (PdfObject)paramFormObject.MK.clone());
    setSelected(paramFormObject.isSelected);
  }

  public boolean[] getCharacteristics()
  {
    if (this.Farray == null)
      if (this.F == -1)
        this.Farray = new boolean[10];
      else
        this.Farray = calcFarray(this.F);
    return this.Farray;
  }

  public int getTextSize()
  {
    return this.textSize;
  }

  public Map getValuesMap(boolean paramBoolean)
  {
    if ((this.Opt != null) && (this.OptValues == null))
    {
      Object[] arrayOfObject1 = getObjectArray(2048068);
      if (arrayOfObject1 != null)
        for (Object localObject : arrayOfObject1)
          if ((localObject instanceof Object[]))
          {
            Object[] arrayOfObject2 = (Object[])localObject;
            String str1;
            String str2;
            if (paramBoolean)
            {
              str1 = StringUtils.getTextString((byte[])arrayOfObject2[0], false);
              str2 = StringUtils.getTextString((byte[])arrayOfObject2[1], false);
            }
            else
            {
              str1 = StringUtils.getTextString((byte[])arrayOfObject2[1], false);
              str2 = StringUtils.getTextString((byte[])arrayOfObject2[0], false);
            }
            if (this.OptValues == null)
              this.OptValues = new HashMap();
            this.OptValues.put(str1, str2);
          }
    }
    return this.OptValues;
  }

  public String[] getItemsList()
  {
    if (this.OptString == null)
    {
      Object[] arrayOfObject1 = getObjectArray(2048068);
      if (arrayOfObject1 != null)
      {
        int i = arrayOfObject1.length;
        this.OptString = new String[i];
        for (int j = 0; j < i; j++)
          if ((arrayOfObject1[j] instanceof Object[]))
          {
            Object[] arrayOfObject2 = (Object[])arrayOfObject1[j];
            this.OptString[j] = StringUtils.getTextString((byte[])(byte[])arrayOfObject2[1], false);
          }
          else if ((arrayOfObject1[j] instanceof byte[]))
          {
            this.OptString[j] = StringUtils.getTextString((byte[])(byte[])arrayOfObject1[j], false);
          }
          else if (arrayOfObject1[j] == null);
      }
    }
    return this.OptString;
  }

  public String getSelectedItem()
  {
    if (this.selectedItem == null)
      this.selectedItem = getTextStreamValue(38);
    if ((this.selectedItem == null) && (this.selectionIndices != null))
    {
      String[] arrayOfString = getItemsList();
      int i = this.selectionIndices[0];
      if ((arrayOfString != null) && (i > -1) && (i < arrayOfString.length))
        this.selectedItem = arrayOfString[i];
    }
    return this.selectedItem;
  }

  public int[] getSelectionIndices()
  {
    if ((this.selectionIndices == null) && (this.TI != -1))
    {
      this.selectionIndices = new int[1];
      this.selectionIndices[0] = this.TI;
      this.selectionIndex = this.TI;
    }
    return this.selectionIndices;
  }

  public String getTextString()
  {
    if (this.textString == null)
      this.textString = getTextStreamValue(38);
    if ((this.textString == null) && (getTextStreamValue(5158) != null))
      return getTextStreamValue(5158);
    if (this.textString != null)
      this.textString = this.textString.replaceAll("\r", "\n").trim();
    else
      this.textString = "";
    return this.textString;
  }

  public int getTextPosition()
  {
    return getDictionary(7451).getInt(9248);
  }

  public String getNormalOnState()
  {
    return this.normalOnState;
  }

  public BufferedImage getNormalOffImage()
  {
    return this.normalOffImage;
  }

  public BufferedImage getNormalOnImage()
  {
    return this.normalOnImage;
  }

  public BufferedImage getAPImage(int paramInt1, int paramInt2)
  {
    BufferedImage localBufferedImage = null;
    switch (paramInt1)
    {
    case 7998:
      switch (paramInt2)
      {
      case 20:
        localBufferedImage = this.downOnImage;
        break;
      case 30:
        localBufferedImage = this.normalOnImage;
        break;
      case 34:
        localBufferedImage = this.rolloverOnImage;
      }
      break;
    case 2045494:
      switch (paramInt2)
      {
      case 20:
        localBufferedImage = this.downOffImage;
        break;
      case 30:
        localBufferedImage = this.normalOffImage;
        break;
      case 34:
        localBufferedImage = this.rolloverOffImage;
      }
      break;
    }
    return localBufferedImage;
  }

  public boolean hasNoDownIcon()
  {
    return this.noDownIcon;
  }

  public boolean hasOffsetDownIcon()
  {
    return this.offsetDownIcon;
  }

  public boolean hasInvertDownIcon()
  {
    return this.invertDownIcon;
  }

  public BufferedImage getDownOffImage()
  {
    return this.downOffImage;
  }

  public BufferedImage getDownOnImage()
  {
    return this.downOnImage;
  }

  public BufferedImage getRolloverOffImage()
  {
    return this.rolloverOffImage;
  }

  public BufferedImage getRolloverOnImage()
  {
    return this.rolloverOnImage;
  }

  public Font getTextFont()
  {
    if (this.textFont == null)
      this.textFont = new Font("Arial", 0, 8);
    return this.textFont;
  }

  public Color getTextColor()
  {
    return generateColor(this.textColor);
  }

  public String getLayerName()
  {
    if (this.layerName == null)
    {
      PdfObject localPdfObject = getDictionary(7955);
      if (localPdfObject != null)
        this.layerName = localPdfObject.getName(506543413);
    }
    return this.layerName;
  }

  public String getValue()
  {
    int i = getParameterConstant(1147962727);
    switch (i)
    {
    case 9288:
      return getTextString();
    case 4920:
      if (this.selectedItem == null)
        this.selectedItem = getTextStreamValue(38);
      return this.selectedItem;
    case 1197118:
      return getDictionary(7451).getTextStreamValue(4881);
    case 2308407:
      return getDictionary(7451).getTextStreamValue(4881);
    }
    return getDictionary(7451).getTextStreamValue(4881);
  }

  public void setValue(String paramString)
  {
    int i = getParameterConstant(1147962727);
    String str1;
    switch (i)
    {
    case 9288:
      String str2 = getTextStreamValue(38);
      if (((str2 == null) || (!str2.equals(paramString))) && ((this.textString == null) || (!this.textString.equals(paramString))))
      {
        if (paramString == null)
          paramString = "";
        this.textString = paramString;
      }
      break;
    case 4920:
      if (this.selectedItem == null)
        this.selectedItem = getTextStreamValue(38);
      if ((this.selectedItem == null) || (!this.selectedItem.equals(paramString)))
        this.selectedItem = paramString;
      break;
    case 1197118:
      str1 = getDictionary(7451).getTextStreamValue(4881);
      if ((str1 == null) || (!str1.equals(paramString)))
        getDictionary(7451).setTextStreamValue(4881, StringUtils.toBytes(paramString));
      break;
    default:
      str1 = getDictionary(7451).getTextStreamValue(4881);
      if ((str1 == null) || (!str1.equals(paramString)))
        getDictionary(7451).setTextStreamValue(4881, StringUtils.toBytes(paramString));
      break;
    }
  }

  public void setLineWidth(int paramInt)
  {
    if (this.BS == null)
      this.BS = new FormObject();
    this.BS.setIntNumber(39, paramInt);
  }

  public void setBorderWidth(int paramInt)
  {
    setLineWidth(paramInt);
  }

  public String buttonGetCaption()
  {
    return buttonGetCaption(0);
  }

  public String buttonGetCaption(int paramInt)
  {
    switch (paramInt)
    {
    case 1:
      return getDictionary(7451).getTextStreamValue(4371);
    case 2:
      return getDictionary(7451).getTextStreamValue(8723);
    }
    return getDictionary(7451).getTextStreamValue(4881);
  }

  public void buttonSetCaption(String paramString)
  {
    buttonSetCaption(paramString, 0);
  }

  public void buttonSetCaption(String paramString, int paramInt)
  {
    switch (paramInt)
    {
    case 1:
      getDictionary(7451).setTextStreamValue(4371, StringUtils.toBytes(paramString));
      break;
    case 2:
      getDictionary(7451).setTextStreamValue(8723, StringUtils.toBytes(paramString));
      break;
    default:
      getDictionary(7451).setTextStreamValue(4881, StringUtils.toBytes(paramString));
    }
  }

  public Object getfillColor()
  {
    return generateColor(getDictionary(7451).getFloatArray(4631));
  }

  public void setfillColor(float[] paramArrayOfFloat)
  {
    if (paramArrayOfFloat != null)
    {
      if ((paramArrayOfFloat.length > 0) && (Float.isNaN(paramArrayOfFloat[0])))
      {
        float[] arrayOfFloat = new float[paramArrayOfFloat.length - 1];
        System.arraycopy(paramArrayOfFloat, 1, arrayOfFloat, 0, paramArrayOfFloat.length - 1);
        paramArrayOfFloat = arrayOfFloat;
      }
      getDictionary(7451).setFloatArray(4631, paramArrayOfFloat);
    }
  }

  public void setfillColor(Object paramObject)
  {
    if (paramObject != null)
    {
      float[] arrayOfFloat = DisplayJavascriptActions.convertToColorFloatArray((String)paramObject);
      getDictionary(7451).setFloatArray(4631, arrayOfFloat);
    }
  }

  public void setFormType(int paramInt)
  {
    this.formType = paramInt;
  }

  public int getFormType()
  {
    return this.formType;
  }

  public static BufferedImage getOpaqueImage()
  {
    return new BufferedImage(20, 20, 2);
  }

  public void setGUIComponent(Object paramObject)
  {
    this.guiComp = paramObject;
  }

  public Object getGUIComponent()
  {
    return this.guiComp;
  }

  public boolean isReadOnly()
  {
    boolean bool = false;
    boolean[] arrayOfBoolean1 = getFieldFlags();
    boolean[] arrayOfBoolean2 = getCharacteristics();
    if (((arrayOfBoolean1 != null) && (arrayOfBoolean1[1] != 0)) || ((arrayOfBoolean2 != null) && (arrayOfBoolean2[9] != 0)))
      bool = true;
    return bool;
  }

  public void updateValue(Object paramObject, boolean paramBoolean1, boolean paramBoolean2)
  {
    if (isReadOnly())
      return;
    if (GenericFormFactory.isTextForm(this.formType))
    {
      this.textString = ((String)paramObject);
      if ((this.lastTextString != null) && (this.textString != null) && (this.textString.equals(this.lastTextString)))
        paramBoolean2 = false;
      this.lastTextString = this.textString;
    }
    else
    {
      this.textString = ((String)paramObject);
      this.isSelected = paramBoolean1;
      if ((paramBoolean1 != this.lastIsSelected) && (this.lastTextString != null) && (this.textString != null) && (this.textString.equals(this.lastTextString)))
        paramBoolean2 = false;
      this.lastTextString = this.textString;
      this.lastIsSelected = paramBoolean1;
    }
    if ((paramBoolean2) && ((this.guiComp instanceof JComponent)) && (this.guiComp != null))
      syncGUI(paramObject);
  }

  private void syncGUI(final Object paramObject)
  {
    if (SwingUtilities.isEventDispatchThread())
    {
      setGUI(this.formType, paramObject, this.guiComp);
    }
    else
    {
      Runnable local1 = new Runnable()
      {
        public void run()
        {
          FormObject.setGUI(FormObject.this.formType, paramObject, FormObject.this.guiComp);
        }
      };
      SwingUtilities.invokeLater(local1);
    }
  }

  private static void setGUI(int paramInt, Object paramObject1, Object paramObject2)
  {
    try
    {
      if (GenericFormFactory.isTextForm(paramInt))
      {
        ((JTextComponent)paramObject2).setText((String)paramObject1);
      }
      else if (paramInt == 9)
      {
        ((JCheckBox)paramObject2).setSelected(Boolean.valueOf((String)paramObject1).booleanValue());
      }
      else if (GenericFormFactory.isButtonForm(paramInt))
      {
        ((JRadioButton)paramObject2).setText((String)paramObject1);
        ((JRadioButton)paramObject2).setSelected(Boolean.valueOf((String)paramObject1).booleanValue());
      }
      else if ((paramInt == 10) && ((paramObject2 instanceof JButton)))
      {
        ((JButton)paramObject2).setSelected(Boolean.valueOf((String)paramObject1).booleanValue());
      }
    }
    catch (Exception localException)
    {
    }
  }

  public boolean isSelected()
  {
    return this.isSelected;
  }

  public void setSelected(boolean paramBoolean)
  {
    this.isSelected = paramBoolean;
    if (this.guiComp != null)
      syncGUI(Boolean.valueOf(paramBoolean));
  }

  public int getSelectedIndex()
  {
    return this.selectionIndex;
  }

  public void setSelectedIndex(int paramInt)
  {
    if (paramInt != this.selectionIndex)
    {
      this.selectionIndex = paramInt;
      this.selectionIndices = new int[1];
      this.selectionIndices[0] = paramInt;
      if (this.guiComp != null)
        if (SwingUtilities.isEventDispatchThread())
        {
          if (this.formType == 2)
          {
            ((JComboBox)this.guiComp).setSelectedIndex(this.selectionIndex);
            this.selectedItem = ((String)((JComboBox)this.guiComp).getSelectedItem());
          }
          else
          {
            ((JList)this.guiComp).setSelectedIndex(this.selectionIndex);
            this.selectedItem = ((String)((JList)this.guiComp).getSelectedValue());
          }
        }
        else
        {
          Runnable local2 = new Runnable()
          {
            public void run()
            {
              if (FormObject.this.formType == 2)
              {
                ((JComboBox)FormObject.this.guiComp).setSelectedIndex(FormObject.this.selectionIndex);
                FormObject.this.selectedItem = ((String)((JComboBox)FormObject.this.guiComp).getSelectedItem());
              }
              else
              {
                ((JList)FormObject.this.guiComp).setSelectedIndex(FormObject.this.selectionIndex);
                FormObject.this.selectedItem = ((String)((JList)FormObject.this.guiComp).getSelectedValue());
              }
            }
          };
          SwingUtilities.invokeLater(local2);
        }
    }
  }

  public void setSelectedItem(String paramString)
  {
    this.selectedItem = paramString;
    if (this.guiComp != null)
      updateCombo();
  }

  private void updateCombo()
  {
    if ((this.formType == 2) || (this.formType == 1))
    {
      if (SwingUtilities.isEventDispatchThread())
      {
        if (this.formType == 2)
          ((JComboBox)this.guiComp).setSelectedItem(this.selectedItem);
        else
          ((JList)this.guiComp).setSelectedValue(this.selectedItem, true);
      }
      else
      {
        Runnable local3 = new Runnable()
        {
          public void run()
          {
            if (FormObject.this.formType == 2)
              ((JComboBox)FormObject.this.guiComp).setSelectedItem(FormObject.this.selectedItem);
            else
              ((JList)FormObject.this.guiComp).setSelectedValue(FormObject.this.selectedItem, true);
          }
        };
        SwingUtilities.invokeLater(local3);
      }
    }
    else
      throw new RuntimeException("Unexpected type must be FormFactory.combobox or FormFactory.list");
  }

  public Object getFormValue()
  {
    Object localObject;
    switch (this.formType)
    {
    case 9:
      localObject = Boolean.valueOf(this.isSelected);
      break;
    case 2:
      localObject = Boolean.valueOf(this.isSelected);
      break;
    case 1:
      localObject = this.selectedValues;
      break;
    case 8:
      localObject = Boolean.valueOf(this.isSelected);
      break;
    case 3:
    case 4:
    case 5:
    case 6:
    case 7:
    default:
      localObject = getTextString();
    }
    return localObject;
  }

  public String getLastValidValue()
  {
    return this.validValue;
  }

  public void setLastValidValue(String paramString)
  {
    this.validValue = paramString;
  }

  public void setPageRotation(int paramInt)
  {
    this.rawRotation = paramInt;
  }

  public void setVisible(boolean paramBoolean)
  {
    if (this.guiComp != null)
      ((JComponent)this.guiComp).setVisible(paramBoolean);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.raw.FormObject
 * JD-Core Version:    0.6.2
 */