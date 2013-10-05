package org.jpedal.examples.api;

import org.jpedal.objects.PdfData;

public class Example_PdfData
{
  ApiParams params = new ApiParams();

  public void Example_PdfData()
  {
    PdfData localPdfData = new PdfData();
    this.params.intValue = localPdfData.getRawTextElementCount();
    localPdfData.addRawTextElement(this.params.floatValue, this.params.intValue, this.params.stringValue, this.params.floatValue, this.params.intValue, this.params.floatValue, this.params.floatValue, this.params.floatValue, this.params.floatValue, this.params.intValue, this.params.stringBufferValue, this.params.intValue, this.params.stringValue, this.params.booleanValue);
    localPdfData.widthIsEmbedded();
    this.params.booleanValue = localPdfData.IsEmbedded();
    localPdfData.enableTextColorDataExtraction();
    this.params.booleanValue = localPdfData.isColorExtracted();
    localPdfData.dispose();
    localPdfData.flushTextList(this.params.booleanValue);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.api.Example_PdfData
 * JD-Core Version:    0.6.2
 */