package org.jpedal.examples.api;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.io.InputStream;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.Iterator;
import java.util.Map;
import javax.print.attribute.SetOfIntegerSyntax;
import javax.swing.border.Border;
import org.jpedal.display.Display;
import org.jpedal.grouping.PdfGroupingAlgorithms;
import org.jpedal.gui.UIViewerInt;
import org.jpedal.gui.ViewerInt;
import org.jpedal.io.ObjectStore;
import org.jpedal.io.PdfObjectReader;
import org.jpedal.io.StatusBar;
import org.jpedal.objects.Javascript;
import org.jpedal.objects.PageOrigins;
import org.jpedal.objects.PdfData;
import org.jpedal.objects.PdfFileInformation;
import org.jpedal.objects.PdfImageData;
import org.jpedal.objects.PdfPageData;
import org.jpedal.objects.acroforms.AcroRenderer;
import org.jpedal.objects.outlines.OutlineData;
import org.jpedal.parser.DecoderOptions;
import org.jpedal.parser.SwingPrinter;
import org.jpedal.render.DynamicVectorRenderer;
import org.jpedal.text.TextLines;
import org.jpedal.utils.DPIFactory;
import org.w3c.dom.Document;

public class ApiParams
{
  boolean booleanValue = false;
  Document documentValue = null;
  int intValue = 0;
  int[] intArrayValue = { 0 };
  Display displayValue = null;
  Iterator iteratorValue = null;
  OutlineData outlineDataValue = null;
  PdfData pdfDataValue = null;
  PdfPageData pdfPageDataValue = null;
  BufferedImage bufferedImageValue = null;
  Map mapValue = null;
  float floatValue = 0.0F;
  float[] floatArrayValue = { 0.0F };
  PdfImageData pdfImageDataValue = null;
  PdfFileInformation pdfFileInformationValue = null;
  DPIFactory dPIFactoryValue = null;
  DynamicVectorRenderer dynamicVectorRendererValue = null;
  PdfObjectReader pdfObjectReaderValue = null;
  Color colorValue = null;
  Color[] colorArrayValue = null;
  Object objectValue = null;
  Object[] objectArrayValue = null;
  String stringValue = null;
  byte byteValue = 0;
  byte[] byteArrayValue = null;
  Certificate certificateValue = null;
  PrivateKey privateKeyValue = null;
  InputStream inputStreamValue = null;
  AcroRenderer acroRendererValue = null;
  Javascript javascriptValue = null;
  ObjectStore objectStoreValue = null;
  DecoderOptions decoderOptionsValue = null;
  PdfGroupingAlgorithms pdfGroupingAlgorithmsValue = null;
  TextLines textLinesValue = null;
  UIViewerInt uIViewerIntValue = null;
  ViewerInt viewerIntValue = null;
  SwingPrinter swingPrinterValue = null;
  AffineTransform affineTransformValue = null;
  Rectangle rectangleValue = null;
  Graphics graphicsValue = null;
  PageFormat pageFormatValue = null;
  Graphics2D graphics2DValue = null;
  StatusBar statusBarValue = null;
  Cursor cursorValue = null;
  Printable printableValue = null;
  Point pointValue = null;
  Dimension dimensionValue = null;
  Border borderValue = null;
  SetOfIntegerSyntax setOfIntegerSyntaxValue = null;
  StringBuffer stringBufferValue = null;
  PageOrigins pageOriginsValue = null;
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.api.ApiParams
 * JD-Core Version:    0.6.2
 */