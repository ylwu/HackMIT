package org.jpedal.examples.api;

import java.awt.print.PrinterException;
import org.jpedal.PdfDecoder;
import org.jpedal.exception.PdfException;

public class Example_PdfDecoder
{
  ApiParams params = new ApiParams();

  public void Example_PdfDecoder()
  {
    PdfDecoder localPdfDecoder = new PdfDecoder();
    localPdfDecoder = new PdfDecoder(this.params.booleanValue);
    this.params.intValue = localPdfDecoder.getPageNumber();
    localPdfDecoder.setDisplayRotation(this.params.intValue);
    this.params.intValue = localPdfDecoder.getlastPageDecoded();
    this.params.iteratorValue = localPdfDecoder.getPageInfo(this.params.intValue);
    this.params.outlineDataValue = localPdfDecoder.getOutlineData();
    this.params.booleanValue = localPdfDecoder.isLoadingLinearizedPDF();
    this.params.intValue = localPdfDecoder.getPageAlignment();
    PdfDecoder.init(this.params.booleanValue);
    PdfDecoder.disposeAllStatic();
    localPdfDecoder.dispose();
    localPdfDecoder.closePdfFile();
    this.params.booleanValue = localPdfDecoder.hasOutline();
    this.params.documentValue = localPdfDecoder.getOutlineAsXML();
    this.params.pdfPageDataValue = localPdfDecoder.getPdfPageData();
    try
    {
      localPdfDecoder.setPagePrintRange(this.params.intValue, this.params.intValue);
    }
    catch (PdfException localPdfException1)
    {
    }
    localPdfDecoder.setTextPrint(this.params.intValue);
    localPdfDecoder.useLogicalPrintOffset(this.params.intValue);
    try
    {
      this.params.intValue = localPdfDecoder.print(this.params.graphicsValue, this.params.pageFormatValue, this.params.intValue);
    }
    catch (PrinterException localPrinterException)
    {
    }
    try
    {
      this.params.bufferedImageValue = localPdfDecoder.getPageAsHiRes(this.params.intValue);
    }
    catch (PdfException localPdfException2)
    {
    }
    try
    {
      this.params.bufferedImageValue = localPdfDecoder.getPageAsHiRes(this.params.intValue, this.params.mapValue);
    }
    catch (PdfException localPdfException3)
    {
    }
    try
    {
      this.params.bufferedImageValue = localPdfDecoder.getPageAsHiRes(this.params.intValue, this.params.mapValue, this.params.booleanValue);
    }
    catch (PdfException localPdfException4)
    {
    }
    try
    {
      this.params.bufferedImageValue = localPdfDecoder.getPageAsHiRes(this.params.intValue, this.params.booleanValue);
    }
    catch (PdfException localPdfException5)
    {
    }
    try
    {
      this.params.bufferedImageValue = localPdfDecoder.getPageAsImage(this.params.intValue);
    }
    catch (PdfException localPdfException6)
    {
    }
    try
    {
      this.params.bufferedImageValue = localPdfDecoder.getPageAsTransparentImage(this.params.intValue);
    }
    catch (PdfException localPdfException7)
    {
    }
    try
    {
      localPdfDecoder.renderPageOntoGraphics2D(this.params.floatValue, this.params.intValue, this.params.graphics2DValue, this.params.booleanValue);
    }
    catch (Exception localException1)
    {
    }
    this.params.floatValue = localPdfDecoder.getHiResUpscaleFactor();
    localPdfDecoder.flushObjectValues(this.params.booleanValue);
    this.params.pdfImageDataValue = localPdfDecoder.getPdfImageData();
    this.params.pdfImageDataValue = localPdfDecoder.getPdfBackgroundImageData();
    localPdfDecoder.setRenderMode(this.params.intValue);
    localPdfDecoder.setExtractionMode(this.params.intValue);
    try
    {
      localPdfDecoder.modifyNonstaticJPedalParameters(this.params.mapValue);
    }
    catch (PdfException localPdfException8)
    {
    }
    try
    {
      PdfDecoder.modifyJPedalParameters(this.params.mapValue);
    }
    catch (PdfException localPdfException9)
    {
    }
    this.params.pdfFileInformationValue = localPdfDecoder.getFileInformationData();
    this.params.dPIFactoryValue = localPdfDecoder.getDPIFactory();
    localPdfDecoder.setPageParameters(this.params.floatValue, this.params.intValue);
    localPdfDecoder.setPageParameters(this.params.floatValue, this.params.intValue, this.params.intValue);
    try
    {
      localPdfDecoder.decodePage(this.params.intValue);
    }
    catch (Exception localException2)
    {
    }
    this.params.booleanValue = localPdfDecoder.isPageAvailable(this.params.intValue);
    try
    {
      localPdfDecoder.printAdditionalObjectsOverPage(this.params.intValue, this.params.intArrayValue, this.params.colorArrayValue, this.params.objectArrayValue);
    }
    catch (PdfException localPdfException10)
    {
    }
    try
    {
      localPdfDecoder.printAdditionalObjectsOverAllPages(this.params.intArrayValue, this.params.colorArrayValue, this.params.objectArrayValue);
    }
    catch (PdfException localPdfException11)
    {
    }
    try
    {
      localPdfDecoder.drawAdditionalObjectsOverPage(this.params.intValue, this.params.intArrayValue, this.params.colorArrayValue, this.params.objectArrayValue);
    }
    catch (PdfException localPdfException12)
    {
    }
    try
    {
      localPdfDecoder.flushAdditionalObjectsOnPage(this.params.intValue);
    }
    catch (PdfException localPdfException13)
    {
    }
    this.params.booleanValue = localPdfDecoder.isHiResScreenDisplay();
    localPdfDecoder.useHiResScreenDisplay(this.params.booleanValue);
    this.params.intValue = localPdfDecoder.getPageCount();
    this.params.booleanValue = localPdfDecoder.isEncrypted();
    this.params.booleanValue = localPdfDecoder.isPasswordSupplied();
    this.params.booleanValue = localPdfDecoder.isFileViewable();
    this.params.booleanValue = localPdfDecoder.isExtractionAllowed();
    try
    {
      localPdfDecoder.setEncryptionPassword(this.params.stringValue);
    }
    catch (PdfException localPdfException14)
    {
    }
    try
    {
      localPdfDecoder.openPdfArray(this.params.byteArrayValue);
    }
    catch (PdfException localPdfException15)
    {
    }
    try
    {
      localPdfDecoder.openPdfFile(this.params.stringValue, this.params.certificateValue, this.params.privateKeyValue);
    }
    catch (PdfException localPdfException16)
    {
    }
    try
    {
      localPdfDecoder.openPdfFileFromStream(this.params.objectValue, this.params.stringValue);
    }
    catch (PdfException localPdfException17)
    {
    }
    try
    {
      localPdfDecoder.openPdfFile(this.params.stringValue);
    }
    catch (PdfException localPdfException18)
    {
    }
    try
    {
      localPdfDecoder.openPdfFile(this.params.stringValue, this.params.stringValue);
    }
    catch (PdfException localPdfException19)
    {
    }
    try
    {
      this.params.booleanValue = localPdfDecoder.openPdfFileFromURL(this.params.stringValue, this.params.booleanValue);
    }
    catch (PdfException localPdfException20)
    {
    }
    try
    {
      this.params.booleanValue = localPdfDecoder.openPdfFileFromURL(this.params.stringValue, this.params.booleanValue, this.params.stringValue);
    }
    catch (PdfException localPdfException21)
    {
    }
    try
    {
      this.params.booleanValue = localPdfDecoder.openPdfFileFromInputStream(this.params.inputStreamValue, this.params.booleanValue);
    }
    catch (PdfException localPdfException22)
    {
    }
    try
    {
      this.params.booleanValue = localPdfDecoder.openPdfFileFromInputStream(this.params.inputStreamValue, this.params.booleanValue, this.params.stringValue);
    }
    catch (PdfException localPdfException23)
    {
    }
    this.params.booleanValue = localPdfDecoder.isXMLExtraction();
    localPdfDecoder.useTextExtraction();
    localPdfDecoder.useXMLExtraction();
    localPdfDecoder.clearScreen();
    localPdfDecoder.setStreamCacheSize(this.params.intValue);
    this.params.booleanValue = localPdfDecoder.hasEmbeddedFonts();
    try
    {
      this.params.booleanValue = localPdfDecoder.PDFContainsEmbeddedFonts();
    }
    catch (Exception localException3)
    {
    }
    this.params.intValue = localPdfDecoder.getPageFromObjectRef(this.params.stringValue);
    this.params.stringValue = localPdfDecoder.getInfo(this.params.intValue);
    this.params.acroRendererValue = localPdfDecoder.getFormRenderer();
    this.params.booleanValue = localPdfDecoder.isPageSuccessful();
    this.params.stringValue = localPdfDecoder.getPageDecodeReport();
    this.params.stringValue = localPdfDecoder.getPageFailureMessage();
    this.params.bufferedImageValue = localPdfDecoder.getSelectedRectangleOnscreen(this.params.floatValue, this.params.floatValue, this.params.floatValue, this.params.floatValue, this.params.floatValue);
    this.params.objectStoreValue = localPdfDecoder.getObjectStore();
    this.params.decoderOptionsValue = localPdfDecoder.getDecoderOptions();
    try
    {
      this.params.pdfGroupingAlgorithmsValue = localPdfDecoder.getGroupingObject();
    }
    catch (PdfException localPdfException24)
    {
    }
    this.params.pdfGroupingAlgorithmsValue = localPdfDecoder.getBackgroundGroupingObject();
    this.params.stringValue = localPdfDecoder.getPDFVersion();
    localPdfDecoder.resetForNonPDFPage(this.params.intValue);
    localPdfDecoder.setDisplayView(this.params.intValue, this.params.intValue);
    this.params.booleanValue = localPdfDecoder.hasAllImages();
    this.params.booleanValue = localPdfDecoder.getPageDecodeStatus(this.params.intValue);
    this.params.stringValue = localPdfDecoder.getPageDecodeStatusReport(this.params.intValue);
    localPdfDecoder.setPrintAutoRotateAndCenter(this.params.booleanValue);
    localPdfDecoder.setPrintCurrentView(this.params.booleanValue);
    this.params.pdfObjectReaderValue = localPdfDecoder.getIO();
    this.params.stringValue = localPdfDecoder.getFileName();
    this.params.booleanValue = localPdfDecoder.isForm();
    localPdfDecoder.setAllowDifferentPrintPageSizes(this.params.booleanValue);
    localPdfDecoder.setInset(this.params.intValue, this.params.intValue);
    localPdfDecoder.ensurePointIsVisible(this.params.pointValue);
    localPdfDecoder.setPrintIndent(this.params.intValue, this.params.intValue);
    this.params.dimensionValue = localPdfDecoder.getMaximumSize();
    this.params.dimensionValue = localPdfDecoder.getMinimumSize();
    this.params.dimensionValue = localPdfDecoder.getPreferredSize();
    localPdfDecoder.updateCursorBoxOnScreen(this.params.rectangleValue, this.params.colorValue);
    localPdfDecoder.paint(this.params.graphicsValue);
    localPdfDecoder.paintComponent(this.params.graphicsValue);
    this.params.intValue = localPdfDecoder.getPDFWidth();
    this.params.intValue = localPdfDecoder.getPDFHeight();
    localPdfDecoder.setPDFBorder(this.params.borderValue);
    localPdfDecoder.setHardwareAccelerationforScreen(this.params.booleanValue);
    this.params.intValue = localPdfDecoder.getScrollInterval();
    localPdfDecoder.setScrollInterval(this.params.intValue);
    this.params.intValue = localPdfDecoder.getDisplayView();
    localPdfDecoder.setPrintPageScalingMode(this.params.intValue);
    localPdfDecoder.setUsePDFPaperSize(this.params.booleanValue);
    this.params.intValue = localPdfDecoder.getInsetH();
    this.params.intValue = localPdfDecoder.getInsetW();
    localPdfDecoder.setPrintAutoRotate(this.params.booleanValue);
    try
    {
      localPdfDecoder.setPagePrintRange(this.params.setOfIntegerSyntaxValue);
    }
    catch (PdfException localPdfException25)
    {
    }
    localPdfDecoder.setPrintPageMode(this.params.intValue);
    localPdfDecoder.stopPrinting();
    this.params.intValue = localPdfDecoder.getCurrentPrintPage();
    localPdfDecoder.resetCurrentPrintPage();
    this.params.documentValue = localPdfDecoder.getMarkedContent();
    this.params.displayValue = localPdfDecoder.getPages();
    localPdfDecoder.resetViewableArea();
    try
    {
      this.params.affineTransformValue = localPdfDecoder.setViewableArea(this.params.rectangleValue);
    }
    catch (PdfException localPdfException26)
    {
    }
    this.params.pdfDataValue = localPdfDecoder.getPdfBackgroundData();
    try
    {
      this.params.pdfDataValue = localPdfDecoder.getPdfData();
    }
    catch (PdfException localPdfException27)
    {
    }
    localPdfDecoder.setExtractionMode(this.params.intValue, this.params.floatValue);
    localPdfDecoder.setStatusBarObject(this.params.statusBarValue);
    localPdfDecoder.waitForDecodingToFinish();
    localPdfDecoder.updatePageNumberDisplayed(this.params.intValue);
    this.params.dynamicVectorRendererValue = localPdfDecoder.getDynamicRenderer();
    this.params.dynamicVectorRendererValue = localPdfDecoder.getDynamicRenderer(this.params.booleanValue);
    localPdfDecoder.setPDFCursor(this.params.cursorValue);
    localPdfDecoder.setDefaultCursor(this.params.cursorValue);
    localPdfDecoder.setCursor(this.params.cursorValue);
    try
    {
      localPdfDecoder.decodePageInBackground(this.params.intValue);
    }
    catch (Exception localException4)
    {
    }
    this.params.objectValue = localPdfDecoder.getJPedalObject(this.params.intValue);
    localPdfDecoder.setPageMode(this.params.intValue);
    this.params.javascriptValue = localPdfDecoder.getJavaScript();
    localPdfDecoder.setObjectStore(this.params.objectStoreValue);
    localPdfDecoder.addExternalHandler(this.params.objectValue, this.params.intValue);
    this.params.objectValue = localPdfDecoder.getExternalHandler(this.params.intValue);
    this.params.printableValue = localPdfDecoder.getPrintable(this.params.intValue);
    this.params.textLinesValue = localPdfDecoder.getTextLines();
    localPdfDecoder.setUserOffsets(this.params.intValue, this.params.intValue, this.params.intValue);
    this.params.pointValue = localPdfDecoder.getUserOffsets(this.params.intValue);
    this.params.floatValue = localPdfDecoder.getScaling();
    this.params.rectangleValue = localPdfDecoder.getCursorBoxOnScreen();
    this.params.intValue = localPdfDecoder.getNumberOfPages();
    this.params.pageFormatValue = localPdfDecoder.getPageFormat(this.params.intValue);
    this.params.pageFormatValue = localPdfDecoder.getUserSetPageFormat(this.params.intValue);
    localPdfDecoder.setCenterOnScaling(this.params.booleanValue);
    localPdfDecoder.setPageFormat(this.params.intValue, this.params.pageFormatValue);
    localPdfDecoder.setPageFormat(this.params.pageFormatValue);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.api.Example_PdfDecoder
 * JD-Core Version:    0.6.2
 */