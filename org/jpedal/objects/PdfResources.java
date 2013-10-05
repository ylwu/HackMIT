package org.jpedal.objects;

import org.jpedal.exception.PdfException;
import org.jpedal.io.PdfObjectReader;
import org.jpedal.objects.layers.PdfLayerList;
import org.jpedal.objects.outlines.OutlineData;
import org.jpedal.objects.raw.PdfObject;
import org.jpedal.parser.PdfStreamDecoder;
import org.jpedal.utils.LogWriter;
import org.w3c.dom.Document;

public class PdfResources
{
  public static final int AcroFormObj = 1;
  public static final int GlobalResources = 2;
  public static final int StructTreeRootObj = 3;
  public static final int MarkInfoObj = 4;
  PdfLayerList layers;
  private PdfObject metadataObj = null;
  private PdfObject acroFormObj = null;
  private PdfObject globalResources;
  private PdfObject PropertiesObj = null;
  private PdfObject structTreeRootObj = null;
  private PdfObject OCProperties = null;
  private PdfObject markInfoObj = null;
  private PdfObject OutlinesObj = null;
  private OutlineData outlineData = null;

  public void setupResources(PdfStreamDecoder paramPdfStreamDecoder, boolean paramBoolean, PdfObject paramPdfObject, int paramInt, PdfObjectReader paramPdfObjectReader)
    throws PdfException
  {
    PdfObject localPdfObject;
    if (this.globalResources != null)
    {
      paramPdfStreamDecoder.readResources(this.globalResources, true);
      localPdfObject = this.globalResources.getDictionary(-2089186617);
      if (localPdfObject != null)
        this.PropertiesObj = localPdfObject;
    }
    if (paramPdfObject != null)
    {
      paramPdfStreamDecoder.readResources(paramPdfObject, true);
      localPdfObject = paramPdfObject.getDictionary(-2089186617);
      if (localPdfObject != null)
        this.PropertiesObj = localPdfObject;
    }
    if ((this.OCProperties != null) && ((this.layers == null) || (paramInt != this.layers.getOCpageNumber()) || (paramBoolean)))
    {
      paramPdfObjectReader.checkResolved(this.OCProperties);
      if (this.layers == null)
        this.layers = new PdfLayerList();
      this.layers.init(this.OCProperties, this.PropertiesObj, paramPdfObjectReader, paramInt);
    }
    paramPdfStreamDecoder.setObjectValue(-4, this.layers);
  }

  public PdfObject getPdfObject(int paramInt)
  {
    PdfObject localPdfObject = null;
    switch (paramInt)
    {
    case 1:
      localPdfObject = this.acroFormObj;
      break;
    case 2:
      localPdfObject = this.globalResources;
      break;
    case 4:
      localPdfObject = this.markInfoObj;
      break;
    case 3:
      localPdfObject = this.structTreeRootObj;
    }
    return localPdfObject;
  }

  public void setPdfObject(int paramInt, PdfObject paramPdfObject)
  {
    switch (paramInt)
    {
    case 2:
      this.globalResources = paramPdfObject;
    }
  }

  public void flush()
  {
    this.globalResources = null;
  }

  public void flushObjects()
  {
    this.metadataObj = null;
    this.acroFormObj = null;
    this.markInfoObj = null;
    this.PropertiesObj = null;
    this.OCProperties = null;
    this.structTreeRootObj = null;
    this.OutlinesObj = null;
    this.layers = null;
  }

  public final boolean hasOutline()
  {
    return this.OutlinesObj != null;
  }

  public void setValues(PdfObject paramPdfObject, PdfObjectReader paramPdfObjectReader)
  {
    paramPdfObjectReader.checkResolved(paramPdfObject);
    this.metadataObj = paramPdfObject.getDictionary(1365674082);
    this.acroFormObj = paramPdfObject.getDictionary(661816444);
    paramPdfObjectReader.checkResolved(this.acroFormObj);
    this.markInfoObj = paramPdfObject.getDictionary(913275002);
    this.structTreeRootObj = paramPdfObject.getDictionary(-2000237823);
    this.OCProperties = paramPdfObject.getDictionary(-1567847737);
    this.OutlinesObj = paramPdfObject.getDictionary(1485011327);
    this.outlineData = null;
  }

  public OutlineData getOutlineData()
  {
    return this.outlineData;
  }

  public Document getOutlineAsXML(PdfObjectReader paramPdfObjectReader)
  {
    if ((this.outlineData == null) && (this.OutlinesObj != null))
      try
      {
        paramPdfObjectReader.checkResolved(this.OutlinesObj);
        this.outlineData = new OutlineData();
        this.outlineData.readOutlineFileMetadata(this.OutlinesObj, paramPdfObjectReader);
      }
      catch (Exception localException)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog("Exception " + localException + " accessing outline ");
        this.outlineData = null;
      }
    if (this.outlineData != null)
      return this.outlineData.getList();
    return null;
  }

  public PdfFileInformation getMetaData(PdfObjectReader paramPdfObjectReader)
  {
    if (paramPdfObjectReader != null)
      return new PdfFileInformation().readPdfFileMetadata(this.metadataObj, paramPdfObjectReader);
    return null;
  }

  public boolean isForm()
  {
    return this.acroFormObj != null;
  }

  public PdfLayerList getPdfLayerList()
  {
    return this.layers;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.PdfResources
 * JD-Core Version:    0.6.2
 */