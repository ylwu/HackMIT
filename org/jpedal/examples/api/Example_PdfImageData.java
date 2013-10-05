package org.jpedal.examples.api;

import org.jpedal.objects.PdfImageData;

public class Example_PdfImageData
{
  ApiParams params = new ApiParams();

  public void Example_PdfData()
  {
    PdfImageData localPdfImageData = new PdfImageData();
    this.params.intValue = localPdfImageData.getImagePageID(this.params.intValue);
    this.params.stringValue = localPdfImageData.getImageName(this.params.intValue);
    this.params.floatValue = localPdfImageData.getImageXCoord(this.params.intValue);
    this.params.floatValue = localPdfImageData.getImageYCoord(this.params.intValue);
    this.params.floatValue = localPdfImageData.getImageWidth(this.params.intValue);
    this.params.floatValue = localPdfImageData.getImageHeight(this.params.intValue);
    this.params.intValue = localPdfImageData.getImageCount();
    localPdfImageData.clearImageData();
    localPdfImageData.setImageInfo(this.params.stringValue, this.params.intValue, this.params.floatValue, this.params.floatValue, this.params.floatValue, this.params.floatValue);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.api.Example_PdfImageData
 * JD-Core Version:    0.6.2
 */