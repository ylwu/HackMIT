package org.jpedal.examples.api;

import org.jpedal.PdfDecoderServer;
import org.jpedal.exception.PdfException;

public class Example_PdfDecoderServer
{
  ApiParams params = new ApiParams();

  public void Example_PdfDecoderServer()
  {
    PdfDecoderServer localPdfDecoderServer = new PdfDecoderServer();
    localPdfDecoderServer = new PdfDecoderServer(this.params.booleanValue);
    this.params.booleanValue = localPdfDecoderServer.isOpen();
    this.params.documentValue = localPdfDecoderServer.getMarkedContent();
    this.params.intValue = localPdfDecoderServer.getDisplayRotation();
    this.params.intValue = localPdfDecoderServer.getPageNumber();
    localPdfDecoderServer.setDisplayRotation(this.params.intValue);
    this.params.intValue = localPdfDecoderServer.getlastPageDecoded();
    this.params.iteratorValue = localPdfDecoderServer.getPageInfo(this.params.intValue);
    this.params.outlineDataValue = localPdfDecoderServer.getOutlineData();
    this.params.booleanValue = localPdfDecoderServer.isLoadingLinearizedPDF();
    this.params.intValue = localPdfDecoderServer.getPageAlignment();
    PdfDecoderServer.init(this.params.booleanValue);
    PdfDecoderServer.disposeAllStatic();
    localPdfDecoderServer.dispose();
    localPdfDecoderServer.closePdfFile();
    this.params.booleanValue = localPdfDecoderServer.hasOutline();
    this.params.documentValue = localPdfDecoderServer.getOutlineAsXML();
    this.params.pdfPageDataValue = localPdfDecoderServer.getPdfPageData();
    try
    {
      this.params.bufferedImageValue = localPdfDecoderServer.getPageAsHiRes(this.params.intValue);
    }
    catch (PdfException localPdfException1)
    {
    }
    try
    {
      this.params.bufferedImageValue = localPdfDecoderServer.getPageAsHiRes(this.params.intValue, this.params.mapValue);
    }
    catch (PdfException localPdfException2)
    {
    }
    try
    {
      this.params.bufferedImageValue = localPdfDecoderServer.getPageAsHiRes(this.params.intValue, this.params.mapValue, this.params.booleanValue);
    }
    catch (PdfException localPdfException3)
    {
    }
    try
    {
      this.params.bufferedImageValue = localPdfDecoderServer.getPageAsHiRes(this.params.intValue, this.params.booleanValue);
    }
    catch (PdfException localPdfException4)
    {
    }
    try
    {
      this.params.bufferedImageValue = localPdfDecoderServer.getPageAsImage(this.params.intValue);
    }
    catch (PdfException localPdfException5)
    {
    }
    try
    {
      this.params.bufferedImageValue = localPdfDecoderServer.getPageAsTransparentImage(this.params.intValue);
    }
    catch (PdfException localPdfException6)
    {
    }
    this.params.floatValue = localPdfDecoderServer.getHiResUpscaleFactor();
    localPdfDecoderServer.flushObjectValues(this.params.booleanValue);
    this.params.pdfImageDataValue = localPdfDecoderServer.getPdfImageData();
    this.params.pdfImageDataValue = localPdfDecoderServer.getPdfBackgroundImageData();
    localPdfDecoderServer.setRenderMode(this.params.intValue);
    localPdfDecoderServer.setExtractionMode(this.params.intValue);
    try
    {
      localPdfDecoderServer.modifyNonstaticJPedalParameters(this.params.mapValue);
    }
    catch (PdfException localPdfException7)
    {
    }
    try
    {
      PdfDecoderServer.modifyJPedalParameters(this.params.mapValue);
    }
    catch (PdfException localPdfException8)
    {
    }
    this.params.pdfFileInformationValue = localPdfDecoderServer.getFileInformationData();
    this.params.dPIFactoryValue = localPdfDecoderServer.getDPIFactory();
    localPdfDecoderServer.setPageParameters(this.params.floatValue, this.params.intValue);
    localPdfDecoderServer.setPageParameters(this.params.floatValue, this.params.intValue, this.params.intValue);
    this.params.pdfObjectReaderValue = localPdfDecoderServer.getIO();
    try
    {
      localPdfDecoderServer.decodePage(this.params.intValue);
    }
    catch (Exception localException1)
    {
    }
    this.params.booleanValue = localPdfDecoderServer.isPageAvailable(this.params.intValue);
    try
    {
      localPdfDecoderServer.drawAdditionalObjectsOverPage(this.params.intValue, this.params.intArrayValue, this.params.colorArrayValue, this.params.objectArrayValue);
    }
    catch (PdfException localPdfException9)
    {
    }
    try
    {
      localPdfDecoderServer.flushAdditionalObjectsOnPage(this.params.intValue);
    }
    catch (PdfException localPdfException10)
    {
    }
    this.params.booleanValue = localPdfDecoderServer.isHiResScreenDisplay();
    localPdfDecoderServer.useHiResScreenDisplay(this.params.booleanValue);
    this.params.intValue = localPdfDecoderServer.getPageCount();
    this.params.booleanValue = localPdfDecoderServer.isEncrypted();
    this.params.booleanValue = localPdfDecoderServer.isPasswordSupplied();
    this.params.booleanValue = localPdfDecoderServer.isFileViewable();
    this.params.booleanValue = localPdfDecoderServer.isExtractionAllowed();
    try
    {
      localPdfDecoderServer.setEncryptionPassword(this.params.stringValue);
    }
    catch (PdfException localPdfException11)
    {
    }
    try
    {
      localPdfDecoderServer.openPdfArray(this.params.byteArrayValue);
    }
    catch (PdfException localPdfException12)
    {
    }
    try
    {
      localPdfDecoderServer.openPdfFile(this.params.stringValue, this.params.certificateValue, this.params.privateKeyValue);
    }
    catch (PdfException localPdfException13)
    {
    }
    try
    {
      localPdfDecoderServer.openPdfFileFromStream(this.params.objectValue, this.params.stringValue);
    }
    catch (PdfException localPdfException14)
    {
    }
    try
    {
      localPdfDecoderServer.openPdfFile(this.params.stringValue);
    }
    catch (PdfException localPdfException15)
    {
    }
    try
    {
      localPdfDecoderServer.openPdfFile(this.params.stringValue, this.params.stringValue);
    }
    catch (PdfException localPdfException16)
    {
    }
    try
    {
      this.params.booleanValue = localPdfDecoderServer.openPdfFileFromURL(this.params.stringValue, this.params.booleanValue);
    }
    catch (PdfException localPdfException17)
    {
    }
    try
    {
      this.params.booleanValue = localPdfDecoderServer.openPdfFileFromURL(this.params.stringValue, this.params.booleanValue, this.params.stringValue);
    }
    catch (PdfException localPdfException18)
    {
    }
    try
    {
      this.params.booleanValue = localPdfDecoderServer.openPdfFileFromInputStream(this.params.inputStreamValue, this.params.booleanValue);
    }
    catch (PdfException localPdfException19)
    {
    }
    try
    {
      this.params.booleanValue = localPdfDecoderServer.openPdfFileFromURL(this.params.stringValue, this.params.booleanValue, this.params.stringValue);
    }
    catch (PdfException localPdfException20)
    {
    }
    this.params.booleanValue = localPdfDecoderServer.isXMLExtraction();
    localPdfDecoderServer.useTextExtraction();
    localPdfDecoderServer.useXMLExtraction();
    localPdfDecoderServer.clearScreen();
    localPdfDecoderServer.setStreamCacheSize(this.params.intValue);
    this.params.booleanValue = localPdfDecoderServer.hasEmbeddedFonts();
    try
    {
      this.params.booleanValue = localPdfDecoderServer.PDFContainsEmbeddedFonts();
    }
    catch (Exception localException2)
    {
    }
    this.params.intValue = localPdfDecoderServer.getPageFromObjectRef(this.params.stringValue);
    this.params.stringValue = localPdfDecoderServer.getInfo(this.params.intValue);
    this.params.acroRendererValue = localPdfDecoderServer.getFormRenderer();
    this.params.javascriptValue = localPdfDecoderServer.getJavaScript();
    this.params.stringValue = localPdfDecoderServer.getPageDecodeReport();
    this.params.objectStoreValue = localPdfDecoderServer.getObjectStore();
    this.params.decoderOptionsValue = localPdfDecoderServer.getDecoderOptions();
    try
    {
      this.params.pdfGroupingAlgorithmsValue = localPdfDecoderServer.getGroupingObject();
    }
    catch (PdfException localPdfException21)
    {
    }
    this.params.pdfGroupingAlgorithmsValue = localPdfDecoderServer.getBackgroundGroupingObject();
    this.params.stringValue = localPdfDecoderServer.getPDFVersion();
    this.params.booleanValue = localPdfDecoderServer.hasAllImages();
    this.params.booleanValue = localPdfDecoderServer.getPageDecodeStatus(this.params.intValue);
    this.params.stringValue = localPdfDecoderServer.getPageDecodeStatusReport(this.params.intValue);
    this.params.stringValue = localPdfDecoderServer.getFileName();
    this.params.booleanValue = localPdfDecoderServer.isForm();
    this.params.textLinesValue = localPdfDecoderServer.getTextLines();
    this.params.intValue = localPdfDecoderServer.getPDFWidth();
    this.params.intValue = localPdfDecoderServer.getPDFHeight();
    this.params.intValue = localPdfDecoderServer.getDisplayView();
    this.params.floatValue = localPdfDecoderServer.getScaling();
    this.params.displayValue = localPdfDecoderServer.getPages();
    localPdfDecoderServer.resetViewableArea();
    this.params.pdfDataValue = localPdfDecoderServer.getPdfBackgroundData();
    try
    {
      this.params.pdfDataValue = localPdfDecoderServer.getPdfData();
    }
    catch (PdfException localPdfException22)
    {
    }
    localPdfDecoderServer.setExtractionMode(this.params.intValue, this.params.floatValue);
    localPdfDecoderServer.waitForDecodingToFinish();
    this.params.dynamicVectorRendererValue = localPdfDecoderServer.getDynamicRenderer();
    try
    {
      localPdfDecoderServer.decodePageInBackground(this.params.intValue);
    }
    catch (Exception localException3)
    {
    }
    this.params.objectValue = localPdfDecoderServer.getJPedalObject(this.params.intValue);
    localPdfDecoderServer.setPageMode(this.params.intValue);
    localPdfDecoderServer.setObjectStore(this.params.objectStoreValue);
    localPdfDecoderServer.addExternalHandler(this.params.objectValue, this.params.intValue);
    this.params.objectValue = localPdfDecoderServer.getExternalHandler(this.params.intValue);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.api.Example_PdfDecoderServer
 * JD-Core Version:    0.6.2
 */