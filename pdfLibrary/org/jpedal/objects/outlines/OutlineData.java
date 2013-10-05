package org.jpedal.objects.outlines;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.jpedal.io.ArrayDecoder;
import org.jpedal.io.PdfObjectReader;
import org.jpedal.objects.raw.OutlineObject;
import org.jpedal.objects.raw.PdfArrayIterator;
import org.jpedal.objects.raw.PdfObject;
import org.jpedal.utils.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class OutlineData
{
  private Document OutlineDataXML;
  private Map DestObjs = new HashMap();

  public OutlineData()
  {
    DocumentBuilderFactory localDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
    try
    {
      this.OutlineDataXML = localDocumentBuilderFactory.newDocumentBuilder().newDocument();
    }
    catch (ParserConfigurationException localParserConfigurationException)
    {
      System.err.println("Exception " + localParserConfigurationException + " generating XML document");
    }
  }

  public Document getList()
  {
    return this.OutlineDataXML;
  }

  public int readOutlineFileMetadata(PdfObject paramPdfObject, PdfObjectReader paramPdfObjectReader)
  {
    int i = paramPdfObject.getInt(1061502502);
    PdfObject localPdfObject = paramPdfObject.getDictionary(960643930);
    paramPdfObjectReader.checkResolved(localPdfObject);
    if (localPdfObject != null)
    {
      Element localElement = this.OutlineDataXML.createElement("root");
      this.OutlineDataXML.appendChild(localElement);
      int j = 0;
      readOutlineLevel(localElement, paramPdfObjectReader, localPdfObject, j, false);
    }
    return i;
  }

  private void readOutlineLevel(Element paramElement, PdfObjectReader paramPdfObjectReader, PdfObject paramPdfObject, int paramInt, boolean paramBoolean)
  {
    Element localElement = this.OutlineDataXML.createElement("title");
    PdfObject localPdfObject;
    for (Object localObject1 = null; ; localObject1 = localPdfObject)
    {
      if (localObject1 != null)
        paramPdfObject = (PdfObject)localObject1;
      String str1 = paramPdfObject.getObjectRefAsString();
      int i = -1;
      localObject1 = paramPdfObject.getDictionary(960643930);
      paramPdfObjectReader.checkResolved((PdfObject)localObject1);
      localPdfObject = paramPdfObject.getDictionary(506808388);
      paramPdfObjectReader.checkResolved(localPdfObject);
      int j = paramPdfObject.getInt(1061502502);
      if (j != 0)
        paramBoolean = j < 0;
      PdfArrayIterator localPdfArrayIterator = paramPdfObject.getMixedArray(339034948);
      Object localObject2 = paramPdfObject;
      if ((localPdfArrayIterator == null) || (localPdfArrayIterator.getTokenCount() == 0))
      {
        localObject2 = paramPdfObject.getDictionary(17);
        if (localObject2 != null)
          localPdfArrayIterator = ((PdfObject)localObject2).getMixedArray(339034948);
      }
      String str2 = null;
      String str3;
      if ((localPdfArrayIterator != null) && (localPdfArrayIterator.getTokenCount() > 0))
      {
        int k = localPdfArrayIterator.getTokenCount();
        if (k > 0)
          if (localPdfArrayIterator.isNextValueRef())
          {
            str2 = localPdfArrayIterator.getNextValueAsString(true);
          }
          else
          {
            str3 = localPdfArrayIterator.getNextValueAsString(true);
            if (str3 != null)
            {
              str2 = paramPdfObjectReader.convertNameToRef(str3);
              if ((str2 != null) && (str2.startsWith("[")))
              {
                byte[] arrayOfByte2 = StringUtils.toBytes(str2);
                ArrayDecoder localArrayDecoder = new ArrayDecoder(paramPdfObjectReader.getObjectReader(), 0, arrayOfByte2.length, 18, null, 826094945);
                localArrayDecoder.readArray(false, arrayOfByte2, (PdfObject)localObject2, 339034948);
                localPdfArrayIterator = ((PdfObject)localObject2).getMixedArray(339034948);
              }
              else if (str2 != null)
              {
                localObject2 = new OutlineObject(str2);
                paramPdfObjectReader.readObject((PdfObject)localObject2);
                localPdfArrayIterator = ((PdfObject)localObject2).getMixedArray(339034948);
              }
              if (localPdfArrayIterator != null)
              {
                k = localPdfArrayIterator.getTokenCount();
                if ((k > 0) && (localPdfArrayIterator.hasMoreTokens()) && (localPdfArrayIterator.isNextValueRef()))
                  str2 = localPdfArrayIterator.getNextValueAsString(true);
              }
            }
          }
      }
      if (str2 != null)
        i = paramPdfObjectReader.convertObjectToPageNumber(str2);
      byte[] arrayOfByte1 = paramPdfObject.getTextStreamValueAsByte(960773209);
      if (arrayOfByte1 != null)
      {
        str3 = StringUtils.getTextString(arrayOfByte1, false);
        localElement = this.OutlineDataXML.createElement("title");
        paramElement.appendChild(localElement);
        localElement.setAttribute("title", str3);
      }
      localElement.setAttribute("isClosed", String.valueOf(paramBoolean));
      if (localObject2 != null)
        this.DestObjs.put(str1, localObject2);
      if (i != -1)
        localElement.setAttribute("page", String.valueOf(i));
      localElement.setAttribute("level", String.valueOf(paramInt));
      localElement.setAttribute("objectRef", str1);
      if (localObject1 != null)
        readOutlineLevel(localElement, paramPdfObjectReader, (PdfObject)localObject1, paramInt + 1, paramBoolean);
      if (localPdfObject == null)
        break;
    }
  }

  public PdfObject getAobj(String paramString)
  {
    return (PdfObject)this.DestObjs.get(paramString);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.outlines.OutlineData
 * JD-Core Version:    0.6.2
 */