package org.jpedal.io;

import java.util.HashMap;
import org.jpedal.objects.Javascript;
import org.jpedal.objects.raw.NamesObject;
import org.jpedal.objects.raw.PdfArrayIterator;
import org.jpedal.objects.raw.PdfObject;
import org.jpedal.utils.StringUtils;

public class NameLookup extends HashMap
{
  private final PdfFileReader objectReader;

  public NameLookup(PdfFileReader paramPdfFileReader)
  {
    this.objectReader = paramPdfFileReader;
  }

  public void readNames(PdfObject paramPdfObject, Javascript paramJavascript, boolean paramBoolean)
  {
    ObjectDecoder localObjectDecoder = new ObjectDecoder(this.objectReader);
    localObjectDecoder.checkResolved(paramPdfObject);
    int[] arrayOfInt = { 893600855, -2006286978 };
    int i = arrayOfInt.length;
    if (paramBoolean)
      i = 1;
    for (int j = 0; j < i; j++)
    {
      PdfObject localPdfObject1;
      if (paramBoolean)
        localPdfObject1 = paramPdfObject;
      else
        localPdfObject1 = paramPdfObject.getDictionary(arrayOfInt[j]);
      if (localPdfObject1 != null)
      {
        byte[][] arrayOfByte = localPdfObject1.getKeyArray(456733763);
        if (arrayOfByte != null)
        {
          int k = arrayOfByte.length;
          if (k > 0)
            for (byte[] arrayOfByte2 : arrayOfByte)
            {
              String str3 = new String(arrayOfByte2);
              NamesObject localNamesObject = new NamesObject(str3);
              localNamesObject.ignoreRecursion(false);
              this.objectReader.readObject(localNamesObject);
              readNames(localNamesObject, paramJavascript, true);
            }
        }
        PdfArrayIterator localPdfArrayIterator = localPdfObject1.getMixedArray(826094945);
        if ((localPdfArrayIterator != null) && (localPdfArrayIterator.getTokenCount() > 0))
          while (localPdfArrayIterator.hasMoreTokens())
          {
            String str1 = localPdfArrayIterator.getNextValueAsString(true);
            if (localPdfArrayIterator.hasMoreTokens())
            {
              String str2 = localPdfArrayIterator.getNextValueAsString(true);
              if (arrayOfInt[j] == -2006286978)
              {
                ??? = new NamesObject(str2);
                byte[] arrayOfByte1 = StringUtils.toBytes(str2);
                if (arrayOfByte1[0] == 60)
                  ((PdfObject)???).setStatus(2);
                else
                  ((PdfObject)???).setStatus(1);
                Object localObject1;
                if ((str2.contains(" ")) || (str2.contains("<")))
                {
                  ((PdfObject)???).setUnresolvedData(arrayOfByte1, 6691);
                  localObjectDecoder.checkResolved((PdfObject)???);
                  PdfObject localPdfObject2 = ((PdfObject)???).getDictionary(6691);
                  if (localPdfObject2 != null)
                    localObject1 = new String(localPdfObject2.getDecodedStream());
                  else
                    localObject1 = ((PdfObject)???).getTextStreamValue(6691);
                }
                else
                {
                  localObject1 = str2;
                }
                if (localObject1 != null)
                  paramJavascript.setCode(str1, (String)localObject1);
              }
              else
              {
                put(str1, str2);
              }
            }
          }
      }
    }
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.io.NameLookup
 * JD-Core Version:    0.6.2
 */