package org.jpedal.function;

import org.jpedal.io.ObjectDecoder;
import org.jpedal.io.PdfObjectReader;
import org.jpedal.objects.raw.FunctionObject;
import org.jpedal.objects.raw.PdfObject;

public class FunctionFactory
{
  public static PDFFunction getFunction(PdfObject paramPdfObject, PdfObjectReader paramPdfObjectReader)
  {
    Object localObject = null;
    byte[] arrayOfByte = paramPdfObject.getDecodedStream();
    float[] arrayOfFloat1 = paramPdfObject.getFloatArray(1026641277);
    float[] arrayOfFloat2 = paramPdfObject.getFloatArray(826160983);
    int i = paramPdfObject.getInt(2127019430);
    int j = paramPdfObject.getInt(-1413045608);
    float[] arrayOfFloat3 = paramPdfObject.getFloatArray(859785322);
    float[] arrayOfFloat4 = paramPdfObject.getFloatArray(4864);
    float[] arrayOfFloat5 = paramPdfObject.getFloatArray(4865);
    int[] arrayOfInt = paramPdfObject.getIntArray(590957109);
    float[] arrayOfFloat6 = paramPdfObject.getFloatArray(859785587);
    float[] arrayOfFloat7 = paramPdfObject.getFloatArray(1161709186);
    float f1 = 0.0F;
    float f2 = paramPdfObject.getFloatNumber(30);
    if (f2 != -1.0F)
      f1 = f2;
    byte[][] arrayOfByte1 = paramPdfObject.getKeyArray(2122150301);
    int k = 0;
    if (arrayOfByte1 != null)
      k = arrayOfByte1.length;
    PDFFunction[] arrayOfPDFFunction = null;
    if (arrayOfByte1 != null)
    {
      PdfObject[] arrayOfPdfObject = new PdfObject[k];
      for (int m = 0; m < k; m++)
      {
        String str = new String(arrayOfByte1[m]);
        FunctionObject localFunctionObject;
        if (str.startsWith("<<"))
        {
          localFunctionObject = new FunctionObject(1);
          ObjectDecoder localObjectDecoder = new ObjectDecoder(paramPdfObjectReader.getObjectReader());
          localObjectDecoder.readDictionaryAsObject(localFunctionObject, 0, arrayOfByte1[m]);
        }
        else
        {
          localFunctionObject = new FunctionObject(str);
          paramPdfObjectReader.readObject(localFunctionObject);
        }
        arrayOfPdfObject[m] = localFunctionObject;
      }
      arrayOfPDFFunction = new PDFFunction[arrayOfPdfObject.length];
      m = 0;
      int n = arrayOfPdfObject.length;
      while (m < n)
      {
        arrayOfPDFFunction[m] = getFunction(arrayOfPdfObject[m], paramPdfObjectReader);
        m++;
      }
    }
    switch (i)
    {
    case 0:
      localObject = new PDFSampled(arrayOfByte, j, arrayOfFloat1, arrayOfFloat2, arrayOfFloat6, arrayOfFloat3, arrayOfInt);
      break;
    case 2:
      localObject = new PDFExponential(f1, arrayOfFloat4, arrayOfFloat5, arrayOfFloat1, arrayOfFloat2);
      break;
    case 3:
      localObject = new PDFStitching(arrayOfPDFFunction, arrayOfFloat6, arrayOfFloat7, arrayOfFloat1, arrayOfFloat2);
      break;
    case 4:
      localObject = new PDFCalculator(arrayOfByte, arrayOfFloat1, arrayOfFloat2);
    case 1:
    }
    return localObject;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.function.FunctionFactory
 * JD-Core Version:    0.6.2
 */