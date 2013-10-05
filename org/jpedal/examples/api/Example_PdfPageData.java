package org.jpedal.examples.api;

import org.jpedal.objects.PdfPageData;

public class Example_PdfPageData
{
  ApiParams params = new ApiParams();

  public void Example_PdfPageData()
  {
    PdfPageData localPdfPageData = new PdfPageData();
    this.params.stringValue = localPdfPageData.getMediaValue(this.params.intValue);
    this.params.intValue = localPdfPageData.getMediaBoxX(this.params.intValue);
    this.params.intValue = localPdfPageData.getScaledMediaBoxX(this.params.intValue);
    this.params.intValue = localPdfPageData.getMediaBoxY(this.params.intValue);
    this.params.intValue = localPdfPageData.getScaledMediaBoxY(this.params.intValue);
    this.params.intValue = localPdfPageData.getMediaBoxHeight(this.params.intValue);
    this.params.intValue = localPdfPageData.getScaledMediaBoxHeight(this.params.intValue);
    this.params.intValue = localPdfPageData.getMediaBoxWidth(this.params.intValue);
    this.params.intValue = localPdfPageData.getScaledMediaBoxWidth(this.params.intValue);
    this.params.stringValue = localPdfPageData.getCropValue(this.params.intValue);
    this.params.intValue = localPdfPageData.getCropBoxX(this.params.intValue);
    this.params.floatValue = localPdfPageData.getCropBoxX2D(this.params.intValue);
    this.params.intValue = localPdfPageData.getScaledCropBoxX(this.params.intValue);
    this.params.intValue = localPdfPageData.getCropBoxY(this.params.intValue);
    this.params.floatValue = localPdfPageData.getCropBoxY2D(this.params.intValue);
    this.params.intValue = localPdfPageData.getScaledCropBoxY(this.params.intValue);
    this.params.intValue = localPdfPageData.getCropBoxHeight(this.params.intValue);
    this.params.floatValue = localPdfPageData.getCropBoxHeight2D(this.params.intValue);
    this.params.intValue = localPdfPageData.getScaledCropBoxHeight(this.params.intValue);
    this.params.intValue = localPdfPageData.getCropBoxWidth(this.params.intValue);
    this.params.floatValue = localPdfPageData.getCropBoxWidth2D(this.params.intValue);
    this.params.intValue = localPdfPageData.getScaledCropBoxWidth(this.params.intValue);
    this.params.floatValue = localPdfPageData.getScalingValue();
    localPdfPageData.setScalingValue(this.params.floatValue);
    this.params.intValue = localPdfPageData.getPageCount();
    this.params.intValue = localPdfPageData.getRotation(this.params.intValue);
    localPdfPageData.setPageRotation(this.params.intValue, this.params.intValue);
    this.params.booleanValue = localPdfPageData.hasMultipleSizes();
    localPdfPageData.setOrigin(this.params.pageOriginsValue);
    this.params.pageOriginsValue = localPdfPageData.getOrigin();
    localPdfPageData.setCropBox(this.params.floatArrayValue);
    localPdfPageData.setMediaBox(this.params.floatArrayValue);
    localPdfPageData.checkSizeSet(this.params.intValue);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.api.Example_PdfPageData
 * JD-Core Version:    0.6.2
 */