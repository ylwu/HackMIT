package org.jpedal.examples.api;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.color.ColorSpace;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import org.jpedal.PdfDecoder;
import org.jpedal.exception.PdfException;
import org.jpedal.exception.PdfFontException;
import org.jpedal.exception.PdfSecurityException;
import org.jpedal.fonts.FontMappings;
import org.jpedal.grouping.PdfGroupingAlgorithms;
import org.jpedal.io.ColorSpaceConvertor;
import org.jpedal.io.ObjectStore;
import org.jpedal.io.StatusBar;
import org.jpedal.objects.PdfData;
import org.jpedal.objects.PdfFileInformation;
import org.jpedal.objects.PdfImageData;
import org.jpedal.objects.PdfPageData;
import org.jpedal.objects.acroforms.AcroRenderer;
import org.jpedal.objects.javascript.defaultactions.DisplayJavascriptActions;
import org.jpedal.objects.javascript.defaultactions.JpedalDefaultJavascript;
import org.jpedal.objects.layers.PdfLayerList;
import org.jpedal.objects.raw.FormObject;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.Strip;
import org.jpedal.utils.repositories.Vector_Int;
import org.jpedal.utils.repositories.Vector_Object;
import org.jpedal.utils.repositories.Vector_Shape;
import org.jpedal.utils.repositories.Vector_String;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.w3c.dom.Document;

public class ApiForJPedal
{
  int testa = 1;
  int testb = 2;
  int testc = 3;
  int testd = 4;
  byte[] byteArrayTest = new byte[8];
  int intTest = 0;
  long longTest = 0L;
  DataBuffer databufferTest = new DataBufferInt(1, 1);
  boolean booleanTest = true;
  BufferedImage bufferedImageTest = new BufferedImage(10, 10, 1);
  String stringTest = "test";
  byte[][] byteArray = new byte[1][1];
  ColorSpace colorSpaceTest = ColorSpace.getInstance(1);
  Map mapTest = new HashMap();
  String[] stringArrayTest = new String[8];
  Image[] imageArrayTest = new Image[8];
  float floatTest = 3.6F;
  Rectangle rectangleTest = new Rectangle();
  Graphics graphicsTest = this.bufferedImageTest.getGraphics();
  Graphics2D graphics2DTest = (Graphics2D)this.graphicsTest;
  PageFormat pageFormatTest = new PageFormat();
  Vector_Int vectorIntTest = new Vector_Int();
  Vector_Shape vectorShapeTest = new Vector_Shape();
  Color[] colorArrayTest = new Color[8];
  Color colorTest = Color.red;
  MouseEvent mouseEventTest = new MouseEvent(null, this.intTest, this.longTest, this.intTest, this.intTest, this.intTest, this.intTest, this.booleanTest);
  Point pointTest = new Point(0, 0);
  Rectangle[] rectangleArrayTest = new Rectangle[8];
  int[] intArrayTest = new int[8];
  Shape[] shapeArrayTest = new Shape[8];
  Object[] objectArrayTest = new Object[8];
  boolean[] booleanArrayTest = new boolean[8];
  Border borderTest = BorderFactory.createEmptyBorder();
  StringBuilder stringBufferTest = new StringBuilder();
  Object objectTest = new Object();
  AcroRenderer acroRendererTest = new AcroRenderer(true);
  PdfData pdfDataTest = new PdfData();
  boolean assignBoolean;
  String assignString;
  int assignInt;
  float assignFloat;
  double assignDouble;
  Map assignMap;
  BufferedImage assignBufferedImage;
  Component assignComponent;
  Color assignColor;
  Stroke assignStroke;
  PdfGroupingAlgorithms assignPdfGroupingAlgorithms;
  PdfFileInformation assignPdfFileInformation;
  ObjectStore assignObjectStore;
  Document assignDocument;
  PageFormat assignPageFormat;
  PdfData assignPdfData;
  PdfImageData assignPdfImageData;
  PdfPageData assignPdfPageData;
  byte[] assignByteArray;
  Printable assignPrintable;
  AffineTransform assignAffineTransform;
  String[] assignStringArray;
  List assignList;
  List assignVector;
  float[] assignFloatArray;
  Rectangle assignRectangle;
  Dimension assignDimension;
  Rectangle[] assignRectangleArray;
  int[] assignIntArray;
  Object assignObject;
  Object[] assignObjectArray;
  StringBuilder assignStringBuffer;
  ColorSpaceConvertor testColorSpaceConvertorMethods;
  ObjectStore testObjectStore;
  StatusBar testStatusBar;
  LogWriter testLogWriter;
  PdfDecoder testPdfDecoderMethods;
  PdfException testPdfException;
  PdfFileInformation testPdfFileInformation;
  PdfGroupingAlgorithms testPdfGroupingAlgorithms;
  PdfImageData testPdfImageData;
  PdfPageData testPdfPageData;
  PdfDecoder testPdfPanel;
  PdfSecurityException testPdfSecurityException;
  Strip testStrip;
  Vector_Int testVector_Int;
  Vector_Object testVector_Object;
  Vector_String testVector_String;

  public void Example_PdfDecoderServer()
  {
    Example_PdfDecoderServer localExample_PdfDecoderServer = new Example_PdfDecoderServer();
  }

  public void Example_PdfDecoder()
  {
    Example_PdfDecoder localExample_PdfDecoder = new Example_PdfDecoder();
  }

  public void Example_PdfData()
  {
    Example_PdfData localExample_PdfData = new Example_PdfData();
  }

  public void Example_PdfPageData()
  {
    Example_PdfPageData localExample_PdfPageData = new Example_PdfPageData();
  }

  public void constructors()
  {
    this.testColorSpaceConvertorMethods = new ColorSpaceConvertor();
    this.testObjectStore = new ObjectStore();
    this.testStatusBar = new StatusBar();
    this.testStatusBar = new StatusBar(Color.blue);
    this.testLogWriter = new LogWriter();
    this.testPdfDecoderMethods = new PdfDecoder();
    this.testPdfDecoderMethods = new PdfDecoder(this.booleanTest);
    this.testPdfException = new PdfException();
    this.testPdfException = new PdfException(this.stringTest);
    this.testPdfFileInformation = new PdfFileInformation();
    this.testPdfGroupingAlgorithms = new PdfGroupingAlgorithms(this.pdfDataTest, this.testPdfPageData, true);
    this.testPdfImageData = new PdfImageData();
    this.testPdfPageData = new PdfPageData();
    this.testPdfPanel = new PdfDecoder();
    this.testPdfSecurityException = new PdfSecurityException("test");
    this.testStrip = new Strip();
    this.testVector_Int = new Vector_Int();
    this.testVector_Int = new Vector_Int(this.intTest);
    this.testVector_Object = new Vector_Object();
    this.testVector_Object = new Vector_Object(this.intTest);
    this.testVector_String = new Vector_String();
    this.testVector_String = new Vector_String(this.intTest);
  }

  public void nonstaticfields()
  {
    this.assignFloat = this.testStatusBar.percentageDone;
    this.assignString = this.testObjectStore.fullFileName;
  }

  public void staticfields()
  {
    this.assignBoolean = LogWriter.debug;
    this.assignString = LogWriter.log_name;
    this.assignBoolean = LogWriter.testing;
    this.assignInt = 32;
    this.assignInt = 128;
    this.assignBoolean = FontMappings.enforceFontSubstitution;
    this.assignInt = 4;
    this.assignMap = FontMappings.fontSubstitutionAliasTable;
    this.assignMap = FontMappings.fontSubstitutionLocation;
    this.assignMap = FontMappings.fontSubstitutionTable;
    this.assignMap = FontMappings.fontPropertiesTable;
    this.assignInt = 16;
    this.assignInt = 2;
    this.assignInt = 2;
    this.assignInt = 1;
    this.assignInt = 1;
    this.assignInt = 64;
    this.assignString = "5.06b04";
    this.assignBoolean = PdfGroupingAlgorithms.useUnrotatedCoords;
    org.jpedal.objects.acroforms.SwingData.readOnlyScaling = -1;
  }

  public void nonstaticmethods()
    throws PdfException
  {
    ObjectStore.copy(this.stringTest, this.stringTest);
    ObjectStore.copyCMYKimages(this.stringTest);
    this.testObjectStore.flush();
    this.assignString = this.testObjectStore.getCurrentFilename();
    this.assignString = this.testObjectStore.getImageType(this.stringTest);
    this.testObjectStore.init(this.stringTest);
    this.assignBufferedImage = this.testObjectStore.loadStoredImage(this.stringTest);
    this.testObjectStore.saveAsCopy(this.stringTest, this.stringTest);
    this.assignBoolean = this.testObjectStore.saveRawCMYKImage(this.byteArrayTest, this.stringTest);
    this.assignBoolean = this.testObjectStore.saveStoredImage(this.stringTest, this.bufferedImageTest, this.booleanTest, this.booleanTest, this.stringTest);
    this.testObjectStore.storeFileName(this.stringTest);
    this.assignComponent = this.testStatusBar.getStatusObject();
    this.testStatusBar.initialiseStatus(this.stringTest);
    this.testStatusBar.resetStatus(this.stringTest);
    this.testStatusBar.setClientDisplay();
    this.testStatusBar.setProgress(this.intTest);
    this.testStatusBar.updateStatus(this.stringTest, this.intTest);
    this.assignBoolean = FontMappings.addSubstituteFonts(this.stringTest, this.booleanTest);
    this.testPdfDecoderMethods.closePdfFile();
    try
    {
      this.testPdfDecoderMethods.decodePage(this.intTest);
    }
    catch (Exception localException1)
    {
      localException1.printStackTrace();
    }
    try
    {
      this.testPdfDecoderMethods.decodePageInBackground(this.intTest);
    }
    catch (Exception localException2)
    {
      localException2.printStackTrace();
    }
    this.testPdfDecoderMethods.flushObjectValues(this.booleanTest);
    this.assignPdfGroupingAlgorithms = this.testPdfDecoderMethods.getBackgroundGroupingObject();
    this.assignPdfFileInformation = this.testPdfDecoderMethods.getFileInformationData();
    try
    {
      this.assignPdfGroupingAlgorithms = this.testPdfDecoderMethods.getGroupingObject();
    }
    catch (PdfException localPdfException1)
    {
      localPdfException1.printStackTrace();
    }
    this.assignInt = this.testPdfDecoderMethods.getNumberOfPages();
    this.assignObjectStore = this.testPdfDecoderMethods.getObjectStore();
    this.assignDocument = this.testPdfDecoderMethods.getOutlineAsXML();
    try
    {
      this.assignBufferedImage = this.testPdfDecoderMethods.getPageAsImage(this.intTest);
    }
    catch (PdfException localPdfException2)
    {
      localPdfException2.printStackTrace();
    }
    this.assignInt = this.testPdfDecoderMethods.getPageCount();
    this.assignString = this.testPdfDecoderMethods.getPageFailureMessage();
    this.assignPageFormat = this.testPdfDecoderMethods.getPageFormat(this.intTest);
    this.assignPdfData = this.testPdfDecoderMethods.getPdfBackgroundData();
    this.assignPdfImageData = this.testPdfDecoderMethods.getPdfBackgroundImageData();
    try
    {
      this.assignPdfData = this.testPdfDecoderMethods.getPdfData();
    }
    catch (PdfException localPdfException3)
    {
      localPdfException3.printStackTrace();
    }
    this.assignPdfImageData = this.testPdfDecoderMethods.getPdfImageData();
    this.assignPdfPageData = this.testPdfDecoderMethods.getPdfPageData();
    this.assignPrintable = this.testPdfDecoderMethods.getPrintable(this.intTest);
    this.assignBufferedImage = this.testPdfDecoderMethods.getSelectedRectangleOnscreen(this.intTest, this.intTest, this.intTest, this.intTest, this.intTest);
    this.assignBoolean = this.testPdfDecoderMethods.hasEmbeddedFonts();
    this.assignBoolean = this.testPdfDecoderMethods.hasOutline();
    PdfDecoder.init(this.booleanTest);
    this.assignBoolean = this.testPdfDecoderMethods.isEncrypted();
    this.assignBoolean = this.testPdfDecoderMethods.isExtractionAllowed();
    this.assignBoolean = this.testPdfDecoderMethods.isFileViewable();
    this.assignBoolean = this.testPdfDecoderMethods.isPageSuccessful();
    this.assignBoolean = this.testPdfDecoderMethods.isPasswordSupplied();
    try
    {
      this.testPdfDecoderMethods.openPdfArray(this.byteArrayTest);
    }
    catch (PdfException localPdfException4)
    {
      localPdfException4.printStackTrace();
    }
    try
    {
      this.testPdfDecoderMethods.openPdfFile(this.stringTest);
    }
    catch (PdfException localPdfException5)
    {
      localPdfException5.printStackTrace();
    }
    try
    {
      this.assignInt = this.testPdfDecoderMethods.print(this.graphicsTest, this.pageFormatTest, this.intTest);
    }
    catch (PrinterException localPrinterException)
    {
      localPrinterException.printStackTrace();
    }
    this.testPdfDecoderMethods.resetViewableArea();
    try
    {
      FontMappings.setDefaultDisplayFont(this.stringTest);
    }
    catch (PdfFontException localPdfFontException)
    {
      localPdfFontException.printStackTrace();
    }
    this.testPdfDecoderMethods.setEncryptionPassword(this.stringTest);
    this.testPdfDecoderMethods.setExtractionMode(this.intTest);
    this.testPdfDecoderMethods.setExtractionMode(this.intTest, this.floatTest);
    this.testPdfDecoderMethods.setPageFormat(this.intTest, this.pageFormatTest);
    this.testPdfDecoderMethods.setPagePrintRange(this.intTest, this.intTest);
    this.testPdfDecoderMethods.setRenderMode(this.intTest);
    this.testPdfDecoderMethods.setStatusBarObject(this.testStatusBar);
    this.testPdfDecoderMethods.waitForDecodingToFinish();
    FontMappings.addFontFile("", "");
    try
    {
      this.assignAffineTransform = this.testPdfDecoderMethods.setViewableArea(this.rectangleTest);
    }
    catch (PdfException localPdfException6)
    {
      localPdfException6.printStackTrace();
    }
    this.testPdfDecoderMethods.useHiResScreenDisplay(this.booleanTest);
    this.assignInt = this.testPdfDecoderMethods.getScrollInterval();
    this.testPdfDecoderMethods.setScrollInterval(this.intTest);
    this.assignString = this.testPdfException.getMessage();
    this.assignStringArray = PdfFileInformation.getFieldNames();
    this.assignStringArray = this.testPdfFileInformation.getFieldValues();
    this.assignString = this.testPdfFileInformation.getFileXMLMetaData();
    this.testPdfFileInformation.setFieldValue(this.intTest, this.stringTest);
    this.testPdfFileInformation.setFileXMLMetaData(new byte[10]);
    try
    {
      this.assignMap = this.testPdfGroupingAlgorithms.extractTextAsTable(this.intTest, this.intTest, this.intTest, this.intTest, this.intTest, this.booleanTest, this.booleanTest, this.booleanTest, this.booleanTest, this.intTest);
    }
    catch (PdfException localPdfException7)
    {
      localPdfException7.printStackTrace();
    }
    try
    {
      this.assignVector = this.testPdfGroupingAlgorithms.extractTextAsWordlist(this.intTest, this.intTest, this.intTest, this.intTest, this.intTest, this.booleanTest, this.stringTest);
    }
    catch (PdfException localPdfException8)
    {
      localPdfException8.printStackTrace();
    }
    try
    {
      this.assignString = this.testPdfGroupingAlgorithms.extractTextInRectangle(this.intTest, this.intTest, this.intTest, this.intTest, this.intTest, this.booleanTest, this.booleanTest);
    }
    catch (PdfException localPdfException9)
    {
      localPdfException9.printStackTrace();
    }
    this.testPdfImageData.clearImageData();
    this.assignInt = this.testPdfImageData.getImageCount();
    this.assignFloat = this.testPdfImageData.getImageHeight(this.intTest);
    this.assignString = this.testPdfImageData.getImageName(this.intTest);
    this.assignInt = this.testPdfImageData.getImagePageID(this.intTest);
    this.assignFloat = this.testPdfImageData.getImageWidth(this.intTest);
    this.assignFloat = this.testPdfImageData.getImageXCoord(this.intTest);
    this.assignFloat = this.testPdfImageData.getImageYCoord(this.intTest);
    this.testPdfImageData.setImageInfo(this.stringTest, this.intTest, this.floatTest, this.floatTest, this.floatTest, this.floatTest);
    this.testPdfPageData.checkSizeSet(this.intTest);
    this.assignInt = this.testPdfPageData.getCropBoxHeight(this.intTest);
    this.assignInt = this.testPdfPageData.getCropBoxWidth(this.intTest);
    this.assignInt = this.testPdfPageData.getCropBoxX(this.intTest);
    this.assignInt = this.testPdfPageData.getCropBoxY(this.intTest);
    this.assignInt = this.testPdfPageData.getScaledCropBoxHeight(this.intTest);
    this.assignInt = this.testPdfPageData.getScaledCropBoxWidth(this.intTest);
    this.assignInt = this.testPdfPageData.getScaledCropBoxX(this.intTest);
    this.assignInt = this.testPdfPageData.getScaledCropBoxY(this.intTest);
    this.assignString = this.testPdfPageData.getCropValue(this.intTest);
    this.assignInt = this.testPdfPageData.getMediaBoxHeight(this.intTest);
    this.assignInt = this.testPdfPageData.getMediaBoxWidth(this.intTest);
    this.assignInt = this.testPdfPageData.getMediaBoxX(this.intTest);
    this.assignInt = this.testPdfPageData.getMediaBoxY(this.intTest);
    this.assignInt = this.testPdfPageData.getScaledMediaBoxHeight(this.intTest);
    this.assignInt = this.testPdfPageData.getScaledMediaBoxWidth(this.intTest);
    this.assignInt = this.testPdfPageData.getScaledMediaBoxX(this.intTest);
    this.assignInt = this.testPdfPageData.getScaledMediaBoxY(this.intTest);
    this.assignString = this.testPdfPageData.getMediaValue(this.intTest);
    this.assignInt = this.testPdfPageData.getRotation(this.intTest);
    this.testPdfPageData.setCropBox(new float[] { 0.0F, 0.0F, 0.0F, 0.0F });
    this.testPdfPageData.setMediaBox(new float[] { 0.0F, 0.0F, 0.0F, 0.0F });
    this.testPdfPageData.setPageRotation(this.intTest, this.intTest);
    this.testPdfPanel.ensurePointIsVisible(this.pointTest);
    this.assignDimension = this.testPdfPanel.getMaximumSize();
    this.assignDimension = this.testPdfPanel.getMinimumSize();
    this.assignInt = this.testPdfPanel.getPDFHeight();
    this.assignInt = this.testPdfPanel.getPDFWidth();
    this.assignDimension = this.testPdfPanel.getPreferredSize();
    this.assignString = this.testPdfPanel.getToolTipText(this.mouseEventTest);
    this.testPdfPanel.paint(this.graphicsTest);
    this.testPdfPanel.paintComponent(this.graphicsTest);
    this.testPdfPanel.setHardwareAccelerationforScreen(this.booleanTest);
    this.testPdfPanel.setInset(this.intTest, this.intTest);
    this.testPdfDecoderMethods.setPageParameters(this.floatTest, this.intTest);
    this.testPdfDecoderMethods.setPageParameters(this.floatTest, this.intTest, this.intTest);
    this.testPdfPanel.setPDFBorder(this.borderTest);
    this.testPdfPanel.updateCursorBoxOnScreen(this.rectangleTest, this.colorTest);
    this.assignString = this.testPdfSecurityException.getMessage();
    this.testVector_Int.add_together(this.intTest, this.intTest);
    this.testVector_Int.addElement(this.intTest);
    this.testVector_Int.clear();
    this.assignBoolean = this.testVector_Int.contains(this.intTest);
    this.testVector_Int.deleteElementWithValue(this.intTest);
    this.assignInt = this.testVector_Int.elementAt(this.intTest);
    this.assignIntArray = this.testVector_Int.get();
    this.assignInt = this.testVector_Int.getCapacity();
    this.testVector_Int.keep_larger(this.intTest, this.intTest);
    this.testVector_Int.keep_smaller(this.intTest, this.intTest);
    this.assignInt = this.testVector_Int.pull();
    this.testVector_Int.push(this.intTest);
    this.testVector_Int.removeElementAt(this.intTest);
    this.testVector_Int.reuse();
    this.testVector_Int.set(this.intArrayTest);
    this.testVector_Int.setElementAt(this.intTest, this.intTest);
    this.assignInt = this.testVector_Int.size();
    this.assignString = this.testVector_Int.toString();
    this.testVector_Object.addElement(this.objectTest);
    this.testVector_Object.clear();
    this.assignBoolean = this.testVector_Object.contains(this.objectTest);
    this.assignObject = this.testVector_Object.elementAt(this.intTest);
    this.assignObjectArray = this.testVector_Object.get();
    this.assignObject = this.testVector_Object.pull();
    this.testVector_Object.push(this.objectTest);
    this.testVector_Object.removeElementAt(this.intTest);
    this.testVector_Object.set(this.objectArrayTest);
    this.testVector_Object.setElementAt(this.objectTest, this.intTest);
    this.assignInt = this.testVector_Object.size();
    this.testVector_String.addElement(this.stringTest);
    this.testVector_String.clear();
    this.assignBoolean = this.testVector_String.contains(this.stringTest);
    this.assignString = this.testVector_String.elementAt(this.intTest);
    this.assignStringArray = this.testVector_String.get();
    this.testVector_String.merge(this.intTest, this.intTest, this.stringTest);
    this.testVector_String.removeElementAt(this.intTest);
    this.testVector_String.set(this.stringArrayTest);
    this.testVector_String.setElementAt(this.stringTest, this.intTest);
    this.assignInt = this.testVector_String.size();
  }

  public static void javascriptMethods()
  {
    JpedalDefaultJavascript localJpedalDefaultJavascript = new JpedalDefaultJavascript((Scriptable)new Object(), new Context());
    Object localObject1 = localJpedalDefaultJavascript.printd("", (Scriptable)new Object());
    Object localObject2 = localJpedalDefaultJavascript.printd(0, (Scriptable)new Object());
    Map localMap = JpedalDefaultJavascript.crackURL("");
    localJpedalDefaultJavascript.runtimeHighlight = false;
    double d = JpedalDefaultJavascript.z("", 10.0D);
    JpedalDefaultJavascript.beep(1);
    JpedalDefaultJavascript.calculate = 0;
    String str = "Exchange-Pro";
    int i = 10;
    int j = 0;
    j = 1;
    j = 2;
    j = 3;
    float[] arrayOfFloat1 = DisplayJavascriptActions.transparent;
    arrayOfFloat1 = DisplayJavascriptActions.black;
    arrayOfFloat1 = DisplayJavascriptActions.white;
    arrayOfFloat1 = DisplayJavascriptActions.red;
    arrayOfFloat1 = DisplayJavascriptActions.green;
    arrayOfFloat1 = DisplayJavascriptActions.blue;
    arrayOfFloat1 = DisplayJavascriptActions.cyan;
    arrayOfFloat1 = DisplayJavascriptActions.magenta;
    arrayOfFloat1 = DisplayJavascriptActions.yellow;
    arrayOfFloat1 = DisplayJavascriptActions.dkGray;
    arrayOfFloat1 = DisplayJavascriptActions.gray;
    arrayOfFloat1 = DisplayJavascriptActions.ltGray;
    float[] arrayOfFloat2 = DisplayJavascriptActions.convertToColorFloatArray("");
    FormObject localFormObject = new FormObject();
    Object localObject3 = localFormObject.getValue();
    localFormObject.setValue("");
    localFormObject.setLineWidth(1);
    localFormObject.setBorderWidth(1);
    localObject3 = localFormObject.buttonGetCaption();
    localFormObject.buttonGetCaption(1);
    localFormObject.buttonSetCaption("");
    localFormObject.buttonSetCaption("", 1);
    localFormObject.setfillColor(new float[0]);
    localFormObject.setfillColor(new Object());
    localObject3 = localFormObject.getfillColor();
    Object[] arrayOfObject1 = { new FormObject(), new FormObject() };
    PdfLayerList localPdfLayerList = new PdfLayerList();
    Object[] arrayOfObject2 = localPdfLayerList.getOCGs();
  }

  public void staticMethods()
  {
    this.assignBufferedImage = ColorSpaceConvertor.algorithmicConvertCMYKImageToRGB(this.byteArrayTest, this.intTest, this.intTest);
    this.assignBufferedImage = ColorSpaceConvertor.convertColorspace(this.bufferedImageTest, this.intTest);
    this.assignBufferedImage = ColorSpaceConvertor.convertToARGB(this.bufferedImageTest);
    this.assignBufferedImage = ColorSpaceConvertor.convertToRGB(this.bufferedImageTest);
    LogWriter.resetLogFile();
    LogWriter.setupLogFile(this.stringTest);
    LogWriter.writeLog(this.stringTest);
    this.assignString = PdfGroupingAlgorithms.removeHiddenMarkers(this.stringTest);
    this.assignString = Strip.convertToText(this.stringTest, true);
    this.assignString = Strip.removeMultipleSpacesAndReturns(this.stringTest);
    this.assignString = Strip.stripAllSpaces(this.stringTest);
    this.assignStringBuffer = Strip.stripAllSpaces(this.stringBufferTest);
    this.assignString = Strip.stripComment(this.stringTest);
    this.assignString = Strip.stripSpaces(this.stringTest);
    this.assignStringBuffer = Strip.stripXML(this.stringTest, true);
    this.assignStringBuffer = Strip.trim(this.stringBufferTest);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.api.ApiForJPedal
 * JD-Core Version:    0.6.2
 */