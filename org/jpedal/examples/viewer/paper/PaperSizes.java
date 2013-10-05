package org.jpedal.examples.viewer.paper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.print.DocFlavor.BYTE_ARRAY;
import javax.print.PrintService;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;

public class PaperSizes
{
  Map paperDefinitions = new HashMap();
  ArrayList paperList = new ArrayList();
  private static final double mmToSubInch = 2.834645669291339D;
  Map paperNames = new HashMap();
  private int defaultPageIndex = 0;
  private String defaultSize;
  private PrintService printService;

  public PaperSizes(PrintService paramPrintService)
  {
    this.defaultSize = null;
    populateNameMap();
    addCustomPaperSizes();
    setPrintService(paramPrintService);
  }

  public PaperSizes(String paramString)
  {
    this.defaultSize = paramString;
    populateNameMap();
    addCustomPaperSizes();
  }

  public String[] getAvailablePaperSizes()
  {
    Object[] arrayOfObject = this.paperList.toArray();
    String[] arrayOfString = new String[arrayOfObject.length];
    for (int i = 0; i < arrayOfObject.length; i++)
      arrayOfString[i] = ((String)arrayOfObject[i]);
    return arrayOfString;
  }

  public MarginPaper getSelectedPaper(Object paramObject)
  {
    return (MarginPaper)this.paperDefinitions.get(paramObject);
  }

  private static void addCustomPaperSizes()
  {
  }

  private void setDefault()
  {
    if (this.paperList == null)
      return;
    this.defaultPageIndex = -1;
    String str1 = System.getProperty("org.jpedal.printPaperSize");
    int i;
    if (str1 != null)
      for (i = 0; i < this.paperList.size(); i++)
        if (this.paperList.get(i).equals(str1))
          this.defaultPageIndex = i;
    if ((this.defaultPageIndex == -1) && (this.defaultSize != null) && (!this.defaultSize.isEmpty()))
      for (i = 0; i < this.paperList.size(); i++)
        if (this.defaultSize.equals(this.paperList.get(i)))
          this.defaultPageIndex = i;
    if (this.defaultPageIndex == -1)
    {
      this.defaultSize = "A4";
      String[] arrayOfString1 = { "US", "CA", "MX", "CO", "VE", "AR", "CL", "PH" };
      String str2 = Locale.getDefault().getCountry();
      for (String str3 : arrayOfString1)
        if (str2.equals(str3))
          this.defaultSize = "North American Letter";
      for (int j = 0; j < this.paperList.size(); j++)
        if (this.defaultSize.equals(this.paperList.get(j)))
          this.defaultPageIndex = j;
      if (this.defaultPageIndex == -1)
        this.defaultPageIndex = 0;
    }
  }

  public synchronized void setPrintService(PrintService paramPrintService)
  {
    this.printService = paramPrintService;
    this.paperDefinitions = new HashMap();
    this.paperList = new ArrayList();
    checkAndAddSize(MediaSizeName.ISO_A4);
    checkAndAddSize(MediaSizeName.NA_LETTER);
    checkAndAddSize(MediaSizeName.ISO_A0);
    checkAndAddSize(MediaSizeName.ISO_A1);
    checkAndAddSize(MediaSizeName.ISO_A2);
    checkAndAddSize(MediaSizeName.ISO_A3);
    checkAndAddSize(MediaSizeName.ISO_A5);
    checkAndAddSize(MediaSizeName.ISO_A6);
    checkAndAddSize(MediaSizeName.ISO_A7);
    checkAndAddSize(MediaSizeName.ISO_A8);
    checkAndAddSize(MediaSizeName.ISO_A9);
    checkAndAddSize(MediaSizeName.ISO_A10);
    checkAndAddSize(MediaSizeName.ISO_B0);
    checkAndAddSize(MediaSizeName.ISO_B1);
    checkAndAddSize(MediaSizeName.ISO_B2);
    checkAndAddSize(MediaSizeName.ISO_B3);
    checkAndAddSize(MediaSizeName.ISO_B4);
    checkAndAddSize(MediaSizeName.ISO_B5);
    checkAndAddSize(MediaSizeName.ISO_B6);
    checkAndAddSize(MediaSizeName.ISO_B7);
    checkAndAddSize(MediaSizeName.ISO_B8);
    checkAndAddSize(MediaSizeName.ISO_B9);
    checkAndAddSize(MediaSizeName.ISO_B10);
    checkAndAddSize(MediaSizeName.JIS_B0);
    checkAndAddSize(MediaSizeName.JIS_B1);
    checkAndAddSize(MediaSizeName.JIS_B2);
    checkAndAddSize(MediaSizeName.JIS_B3);
    checkAndAddSize(MediaSizeName.JIS_B4);
    checkAndAddSize(MediaSizeName.JIS_B5);
    checkAndAddSize(MediaSizeName.JIS_B6);
    checkAndAddSize(MediaSizeName.JIS_B7);
    checkAndAddSize(MediaSizeName.JIS_B8);
    checkAndAddSize(MediaSizeName.JIS_B9);
    checkAndAddSize(MediaSizeName.JIS_B10);
    checkAndAddSize(MediaSizeName.ISO_C0);
    checkAndAddSize(MediaSizeName.ISO_C1);
    checkAndAddSize(MediaSizeName.ISO_C2);
    checkAndAddSize(MediaSizeName.ISO_C3);
    checkAndAddSize(MediaSizeName.ISO_C4);
    checkAndAddSize(MediaSizeName.ISO_C5);
    checkAndAddSize(MediaSizeName.ISO_C6);
    checkAndAddSize(MediaSizeName.NA_LEGAL);
    checkAndAddSize(MediaSizeName.EXECUTIVE);
    checkAndAddSize(MediaSizeName.LEDGER);
    checkAndAddSize(MediaSizeName.TABLOID);
    checkAndAddSize(MediaSizeName.INVOICE);
    checkAndAddSize(MediaSizeName.FOLIO);
    checkAndAddSize(MediaSizeName.QUARTO);
    checkAndAddSize(MediaSizeName.JAPANESE_POSTCARD);
    checkAndAddSize(MediaSizeName.JAPANESE_DOUBLE_POSTCARD);
    checkAndAddSize(MediaSizeName.A);
    checkAndAddSize(MediaSizeName.B);
    checkAndAddSize(MediaSizeName.C);
    checkAndAddSize(MediaSizeName.D);
    checkAndAddSize(MediaSizeName.E);
    checkAndAddSize(MediaSizeName.ISO_DESIGNATED_LONG);
    checkAndAddSize(MediaSizeName.ITALY_ENVELOPE);
    checkAndAddSize(MediaSizeName.MONARCH_ENVELOPE);
    checkAndAddSize(MediaSizeName.PERSONAL_ENVELOPE);
    checkAndAddSize(MediaSizeName.NA_NUMBER_9_ENVELOPE);
    checkAndAddSize(MediaSizeName.NA_NUMBER_10_ENVELOPE);
    checkAndAddSize(MediaSizeName.NA_NUMBER_11_ENVELOPE);
    checkAndAddSize(MediaSizeName.NA_NUMBER_12_ENVELOPE);
    checkAndAddSize(MediaSizeName.NA_NUMBER_14_ENVELOPE);
    checkAndAddSize(MediaSizeName.NA_6X9_ENVELOPE);
    checkAndAddSize(MediaSizeName.NA_7X9_ENVELOPE);
    checkAndAddSize(MediaSizeName.NA_9X11_ENVELOPE);
    checkAndAddSize(MediaSizeName.NA_9X12_ENVELOPE);
    checkAndAddSize(MediaSizeName.NA_10X13_ENVELOPE);
    checkAndAddSize(MediaSizeName.NA_10X14_ENVELOPE);
    checkAndAddSize(MediaSizeName.NA_10X15_ENVELOPE);
    checkAndAddSize(MediaSizeName.NA_5X7);
    checkAndAddSize(MediaSizeName.NA_8X10);
    addCustomPaperSizes();
    setDefault();
  }

  public String[] getPaperSizes()
  {
    String[] arrayOfString = { (String)this.paperNames.get(MediaSizeName.ISO_A4.toString()), (String)this.paperNames.get(MediaSizeName.NA_LETTER.toString()), (String)this.paperNames.get(MediaSizeName.ISO_A0.toString()), (String)this.paperNames.get(MediaSizeName.ISO_A1.toString()), (String)this.paperNames.get(MediaSizeName.ISO_A2.toString()), (String)this.paperNames.get(MediaSizeName.ISO_A3.toString()), (String)this.paperNames.get(MediaSizeName.ISO_A5.toString()), (String)this.paperNames.get(MediaSizeName.ISO_A6.toString()), (String)this.paperNames.get(MediaSizeName.ISO_A7.toString()), (String)this.paperNames.get(MediaSizeName.ISO_A8.toString()), (String)this.paperNames.get(MediaSizeName.ISO_A9.toString()), (String)this.paperNames.get(MediaSizeName.ISO_A10.toString()), (String)this.paperNames.get(MediaSizeName.ISO_B0.toString()), (String)this.paperNames.get(MediaSizeName.ISO_B1.toString()), (String)this.paperNames.get(MediaSizeName.ISO_B2.toString()), (String)this.paperNames.get(MediaSizeName.ISO_B3.toString()), (String)this.paperNames.get(MediaSizeName.ISO_B4.toString()), (String)this.paperNames.get(MediaSizeName.ISO_B5.toString()), (String)this.paperNames.get(MediaSizeName.ISO_B6.toString()), (String)this.paperNames.get(MediaSizeName.ISO_B7.toString()), (String)this.paperNames.get(MediaSizeName.ISO_B8.toString()), (String)this.paperNames.get(MediaSizeName.ISO_B9.toString()), (String)this.paperNames.get(MediaSizeName.ISO_B10.toString()), (String)this.paperNames.get(MediaSizeName.JIS_B0.toString()), (String)this.paperNames.get(MediaSizeName.JIS_B1.toString()), (String)this.paperNames.get(MediaSizeName.JIS_B2.toString()), (String)this.paperNames.get(MediaSizeName.JIS_B3.toString()), (String)this.paperNames.get(MediaSizeName.JIS_B4.toString()), (String)this.paperNames.get(MediaSizeName.JIS_B5.toString()), (String)this.paperNames.get(MediaSizeName.JIS_B6.toString()), (String)this.paperNames.get(MediaSizeName.JIS_B7.toString()), (String)this.paperNames.get(MediaSizeName.JIS_B8.toString()), (String)this.paperNames.get(MediaSizeName.JIS_B9.toString()), (String)this.paperNames.get(MediaSizeName.JIS_B10.toString()), (String)this.paperNames.get(MediaSizeName.ISO_C0.toString()), (String)this.paperNames.get(MediaSizeName.ISO_C1.toString()), (String)this.paperNames.get(MediaSizeName.ISO_C2.toString()), (String)this.paperNames.get(MediaSizeName.ISO_C3.toString()), (String)this.paperNames.get(MediaSizeName.ISO_C4.toString()), (String)this.paperNames.get(MediaSizeName.ISO_C5.toString()), (String)this.paperNames.get(MediaSizeName.ISO_C6.toString()), (String)this.paperNames.get(MediaSizeName.NA_LEGAL.toString()), (String)this.paperNames.get(MediaSizeName.EXECUTIVE.toString()), (String)this.paperNames.get(MediaSizeName.LEDGER.toString()), (String)this.paperNames.get(MediaSizeName.TABLOID.toString()), (String)this.paperNames.get(MediaSizeName.INVOICE.toString()), (String)this.paperNames.get(MediaSizeName.FOLIO.toString()), (String)this.paperNames.get(MediaSizeName.QUARTO.toString()), (String)this.paperNames.get(MediaSizeName.JAPANESE_POSTCARD.toString()), (String)this.paperNames.get(MediaSizeName.JAPANESE_DOUBLE_POSTCARD.toString()), (String)this.paperNames.get(MediaSizeName.A.toString()), (String)this.paperNames.get(MediaSizeName.B.toString()), (String)this.paperNames.get(MediaSizeName.C.toString()), (String)this.paperNames.get(MediaSizeName.D.toString()), (String)this.paperNames.get(MediaSizeName.E.toString()), (String)this.paperNames.get(MediaSizeName.ISO_DESIGNATED_LONG.toString()), (String)this.paperNames.get(MediaSizeName.ITALY_ENVELOPE.toString()), (String)this.paperNames.get(MediaSizeName.MONARCH_ENVELOPE.toString()), (String)this.paperNames.get(MediaSizeName.PERSONAL_ENVELOPE.toString()), (String)this.paperNames.get(MediaSizeName.NA_NUMBER_9_ENVELOPE.toString()), (String)this.paperNames.get(MediaSizeName.NA_NUMBER_10_ENVELOPE.toString()), (String)this.paperNames.get(MediaSizeName.NA_NUMBER_11_ENVELOPE.toString()), (String)this.paperNames.get(MediaSizeName.NA_NUMBER_12_ENVELOPE.toString()), (String)this.paperNames.get(MediaSizeName.NA_NUMBER_14_ENVELOPE.toString()), (String)this.paperNames.get(MediaSizeName.NA_6X9_ENVELOPE.toString()), (String)this.paperNames.get(MediaSizeName.NA_7X9_ENVELOPE.toString()), (String)this.paperNames.get(MediaSizeName.NA_9X11_ENVELOPE.toString()), (String)this.paperNames.get(MediaSizeName.NA_9X12_ENVELOPE.toString()), (String)this.paperNames.get(MediaSizeName.NA_10X13_ENVELOPE.toString()), (String)this.paperNames.get(MediaSizeName.NA_10X14_ENVELOPE.toString()), (String)this.paperNames.get(MediaSizeName.NA_10X15_ENVELOPE.toString()), (String)this.paperNames.get(MediaSizeName.NA_5X7.toString()), (String)this.paperNames.get(MediaSizeName.NA_8X10.toString()) };
    return arrayOfString;
  }

  private void checkAndAddSize(MediaSizeName paramMediaSizeName)
  {
    HashPrintRequestAttributeSet localHashPrintRequestAttributeSet = new HashPrintRequestAttributeSet();
    if (!this.printService.isAttributeValueSupported(paramMediaSizeName, new DocFlavor.BYTE_ARRAY(DocFlavor.BYTE_ARRAY.PNG.getMimeType()), localHashPrintRequestAttributeSet))
      return;
    Object localObject = this.paperNames.get(paramMediaSizeName.toString());
    String str;
    if (localObject != null)
      str = localObject.toString();
    else
      str = paramMediaSizeName.toString();
    MediaSize localMediaSize = MediaSize.getMediaSizeForName(paramMediaSizeName);
    double d1 = localMediaSize.getX(1000);
    double d2 = localMediaSize.getY(1000);
    localHashPrintRequestAttributeSet.add(paramMediaSizeName);
    MediaPrintableArea[] arrayOfMediaPrintableArea = (MediaPrintableArea[])this.printService.getSupportedAttributeValues(MediaPrintableArea.class, null, localHashPrintRequestAttributeSet);
    if (arrayOfMediaPrintableArea.length == 0)
      return;
    int i = 0;
    if (arrayOfMediaPrintableArea[i] == null)
      for (int j = 0; (j != arrayOfMediaPrintableArea.length) && (arrayOfMediaPrintableArea[i] == null); j++)
        i = j;
    float[] arrayOfFloat = arrayOfMediaPrintableArea[i].getPrintableArea(1000);
    if ((arrayOfFloat[2] > d1 - 0.5D) && (arrayOfFloat[2] < d1 + 0.5D))
      arrayOfFloat[2] = ((float)d1);
    if ((arrayOfFloat[3] > d2 - 0.5D) && (arrayOfFloat[3] < d2 + 0.5D))
      arrayOfFloat[3] = ((float)d2);
    if (((arrayOfFloat[2] > d1 ? 1 : 0) ^ (arrayOfFloat[3] > d2 ? 1 : 0)) != 0)
    {
      double d3 = d1;
      d1 = d2;
      d2 = d3;
    }
    MarginPaper localMarginPaper = new MarginPaper();
    localMarginPaper.setSize(d1 * 2.834645669291339D, d2 * 2.834645669291339D);
    localMarginPaper.setMinImageableArea(arrayOfFloat[0] * 2.834645669291339D, arrayOfFloat[1] * 2.834645669291339D, arrayOfFloat[2] * 2.834645669291339D, arrayOfFloat[3] * 2.834645669291339D);
    this.paperDefinitions.put(str, localMarginPaper);
    this.paperList.add(str);
  }

  public int getDefaultPageIndex()
  {
    return this.defaultPageIndex;
  }

  public int getDefaultPageOrientation()
  {
    OrientationRequested localOrientationRequested = (OrientationRequested)this.printService.getDefaultAttributeValue(OrientationRequested.class);
    int i = 1;
    if (localOrientationRequested != null)
      switch (localOrientationRequested.getValue())
      {
      case 4:
        i = 0;
        break;
      case 5:
        i = 2;
      }
    return i;
  }

  private void populateNameMap()
  {
    this.paperNames.put("iso-a0", "A0");
    this.paperNames.put("iso-a1", "A1");
    this.paperNames.put("iso-a2", "A2");
    this.paperNames.put("iso-a3", "A3");
    this.paperNames.put("iso-a4", "A4");
    this.paperNames.put("iso-a5", "A5");
    this.paperNames.put("iso-a6", "A6");
    this.paperNames.put("iso-a7", "A7");
    this.paperNames.put("iso-a8", "A8");
    this.paperNames.put("iso-a9", "A9");
    this.paperNames.put("iso-a10", "A10");
    this.paperNames.put("iso-b0", "B0");
    this.paperNames.put("iso-b1", "B1");
    this.paperNames.put("iso-b2", "B2");
    this.paperNames.put("iso-b3", "B3");
    this.paperNames.put("iso-b4", "B4");
    this.paperNames.put("iso-b5", "B5");
    this.paperNames.put("iso-b6", "B6");
    this.paperNames.put("iso-b7", "B7");
    this.paperNames.put("iso-b8", "B8");
    this.paperNames.put("iso-b9", "B9");
    this.paperNames.put("iso-b10", "B10");
    this.paperNames.put("na-letter", "North American Letter");
    this.paperNames.put("na-legal", "North American Legal");
    this.paperNames.put("na-8x10", "North American 8x10 inch");
    this.paperNames.put("na-5x7", "North American 5x7 inch");
    this.paperNames.put("executive", "Executive");
    this.paperNames.put("folio", "Folio");
    this.paperNames.put("invoice", "Invoice");
    this.paperNames.put("tabloid", "Tabloid");
    this.paperNames.put("ledger", "Ledger");
    this.paperNames.put("quarto", "Quarto");
    this.paperNames.put("iso-c0", "C0");
    this.paperNames.put("iso-c1", "C1");
    this.paperNames.put("iso-c2", "C2");
    this.paperNames.put("iso-c3", "C3");
    this.paperNames.put("iso-c4", "C4");
    this.paperNames.put("iso-c5", "C5");
    this.paperNames.put("iso-c6", "C6");
    this.paperNames.put("iso-designated-long", "ISO Designated Long size");
    this.paperNames.put("na-10x13-envelope", "North American 10x13 inch");
    this.paperNames.put("na-9x12-envelope", "North American 9x12 inch");
    this.paperNames.put("na-number-10-envelope", "North American number 10 business envelope");
    this.paperNames.put("na-7x9-envelope", "North American 7x9 inch envelope");
    this.paperNames.put("na-9x11-envelope", "North American 9x11 inch envelope");
    this.paperNames.put("na-10x14-envelope", "North American 10x14 inch envelope");
    this.paperNames.put("na-number-9-envelope", "North American number 9 business envelope");
    this.paperNames.put("na-6x9-envelope", "North American 6x9 inch envelope");
    this.paperNames.put("na-10x15-envelope", "North American 10x15 inch envelope");
    this.paperNames.put("monarch-envelope", "Monarch envelope");
    this.paperNames.put("jis-b0", "Japanese B0");
    this.paperNames.put("jis-b1", "Japanese B1");
    this.paperNames.put("jis-b2", "Japanese B2");
    this.paperNames.put("jis-b3", "Japanese B3");
    this.paperNames.put("jis-b4", "Japanese B4");
    this.paperNames.put("jis-b5", "Japanese B5");
    this.paperNames.put("jis-b6", "Japanese B6");
    this.paperNames.put("jis-b7", "Japanese B7");
    this.paperNames.put("jis-b8", "Japanese B8");
    this.paperNames.put("jis-b9", "Japanese B9");
    this.paperNames.put("jis-b10", "Japanese B10");
    this.paperNames.put("a", "Engineering ANSI A");
    this.paperNames.put("b", "Engineering ANSI B");
    this.paperNames.put("c", "Engineering ANSI C");
    this.paperNames.put("d", "Engineering ANSI D");
    this.paperNames.put("e", "Engineering ANSI E");
    this.paperNames.put("arch-a", "Architectural A");
    this.paperNames.put("arch-b", "Architectural B");
    this.paperNames.put("arch-c", "Architectural C");
    this.paperNames.put("arch-d", "Architectural D");
    this.paperNames.put("arch-e", "Architectural E");
    this.paperNames.put("japanese-postcard", "Japanese Postcard");
    this.paperNames.put("oufuko-postcard", "Oufuko Postcard");
    this.paperNames.put("italian-envelope", "Italian Envelope");
    this.paperNames.put("personal-envelope", "Personal Envelope");
    this.paperNames.put("na-number-11-envelope", "North American Number 11 Envelope");
    this.paperNames.put("na-number-12-envelope", "North American Number 12 Envelope");
    this.paperNames.put("na-number-14-envelope", "North American Number 14 Envelope");
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.paper.PaperSizes
 * JD-Core Version:    0.6.2
 */