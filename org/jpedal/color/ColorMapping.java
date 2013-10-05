package org.jpedal.color;

import java.io.Serializable;
import org.jpedal.function.FunctionFactory;
import org.jpedal.function.PDFFunction;
import org.jpedal.io.PdfObjectReader;
import org.jpedal.objects.raw.PdfObject;

public class ColorMapping
  implements Serializable
{
  private static final long serialVersionUID = 2732809266903967232L;
  private PDFFunction function;

  public ColorMapping(PdfObjectReader paramPdfObjectReader, PdfObject paramPdfObject)
  {
    this.function = FunctionFactory.getFunction(paramPdfObject, paramPdfObjectReader);
  }

  public float[] getOperandFloat(float[] paramArrayOfFloat)
  {
    return this.function.compute(paramArrayOfFloat);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.color.ColorMapping
 * JD-Core Version:    0.6.2
 */