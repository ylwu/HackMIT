package org.jpedal.objects.raw;

public class ObjectFactory
{
  public static PdfObject createObject(int paramInt1, String paramString, int paramInt2, int paramInt3)
  {
    switch (paramInt1)
    {
    case 17:
      if (paramInt2 == 373244477)
        return new FormObject(paramString);
      if (paramInt2 == 487790868)
        return new MCObject(paramString);
      return new OutlineObject(paramString);
    case 4369:
      return new FormObject(paramString);
    case 4384:
      return new FormObject(paramString);
    case 1127568774:
      return new FSObject(paramString);
    case 4633:
      return new FormObject(paramString);
    case 4668:
      return new FormObject(paramString);
    case 4643:
      return new FormObject(paramString);
    case 4886:
      return new EncryptionObject(paramString);
    case 661816444:
      return new FormObject(paramString);
    case 19:
      return new FormObject(paramString);
    case 2054190454:
      return new FontObject(paramString);
    case 1972801240:
      return new FontObject(paramString);
    case 946823533:
      return new FontObject(paramString);
    case 2087749783:
      return new ColorSpaceObject(paramString);
    case 1448698499:
      return new MCObject(paramString);
    case 20:
      if (paramInt2 == 373244477)
        return new FormObject(paramString, paramInt3);
      return new OCObject(paramString);
    case 5139:
      return new FormObject(paramString);
    case 1888135062:
      return new DecodeParmsObject(paramString);
    case -1547306032:
      return new FontObject(paramString);
    case 893600855:
      return new NamesObject(paramString);
    case 5152:
      return new FormObject(paramString);
    case 5154:
      return new ResourcesObject(paramString);
    case 5155:
      return new FormObject(paramString);
    case 21:
      return new FormObject(paramString);
    case 5398:
      return new FSObject(paramString);
    case 1232564598:
      return new FontObject(paramString);
    case 894663815:
      return new CompressedObject(paramString);
    case -1938465939:
      return new ExtGStateObject(paramString);
    case 22:
      return new FormObject(paramString);
    case 960643930:
      return new OutlineObject(paramString);
    case 5695:
      return new FormObject(paramString);
    case 373243460:
      return new FontObject(paramString);
    case -1044665361:
      return new FontObject(paramString);
    case 746093177:
      return new FontObject(paramString);
    case 2021292334:
      return new FontObject(paramString);
    case 2021292335:
      return new FontObject(paramString);
    case 5667:
      return new FSObject(paramString);
    case 1518239089:
      return new FunctionObject(paramString);
    case 23:
      return new XObject(paramString);
    case 1111442775:
      return new GroupingObject(paramString);
    case 25:
      return new FormObject(paramString);
    case 6422:
      return new FormObject(paramString);
    case 423507519:
      return new InfoObject(paramString);
    case -2006286978:
      return new NamesObject(paramString);
    case 1314558361:
      return new DecodeParmsObject(paramString);
    case 6691:
      if (paramInt2 == 373244477)
        return new FormObject(paramString);
      return new NamesObject(paramString);
    case 27:
      if (paramInt2 == 487790868)
        return new MCObject(paramString);
      return new FormObject(paramString);
    case 826881374:
      return new OCObject(paramString);
    case 913275002:
      return new MCObject(paramString);
    case 489767739:
      return new MaskObject(paramString);
    case 1365674082:
      return new MetadataObject(paramString);
    case 7451:
      return new MKObject(paramString);
    case 30:
      return new FormObject(paramString, paramInt3);
    case 826094945:
      return new NamesObject(paramString);
    case 506808388:
      if (paramInt2 == 373244477)
        return new FormObject(paramString);
      return new OutlineObject(paramString);
    case 31:
      return new FormObject(paramString);
    case 7955:
      return new OCObject(paramString);
    case -1567847737:
      return new OCObject(paramString);
    case 2037870513:
      return new FormObject(paramString);
    case 2039833:
      return new XObject(paramString);
    case 1485011327:
      return new OutlineObject(paramString);
    case 825701731:
      return new PageObject(paramString);
    case 1719112618:
      return new MCObject(paramString);
    case 1146450818:
      return new PatternObject(paramString);
    case 8211:
      return new FormObject(paramString);
    case 8247:
      return new PageObject(paramString);
    case 8217:
      return new FormObject(paramString);
    case 8223:
      return new FormObject(paramString);
    case 1383295380:
      return new PdfObject(paramString);
    case 1061176672:
      FormObject localFormObject = new FormObject(paramString);
      localFormObject.setBoolean(524301630, false);
      return localFormObject;
    case -2089186617:
      return new OCObject(paramString);
    case 8230:
      return new FormObject(paramString);
    case 34:
      return new FormObject(paramString, paramInt3);
    case 2004251818:
      return new ResourcesObject(paramString);
    case -1263082253:
      return new FSObject(paramString);
    case 893350012:
      return new MCObject(paramString);
    case 574570308:
      return new PageObject(paramString);
    case 878474856:
      return new ShadingObject(paramString);
    case 489767774:
      return new MaskObject(paramString);
    case 1061502534:
      return new SoundObject(paramString);
    case -2000237823:
      return new MCObject(paramString);
    case 1145650264:
      return new FontObject(paramString);
    case 1919185554:
      return new FontObject(paramString);
    case 9250:
      return new MaskObject(paramString);
    case 37:
      return new FormObject(paramString);
    case 1127298906:
      return new OCObject(paramString);
    case 38:
      return new FormObject(paramString);
    case 2570558:
      return new FormObject(paramString);
    case 10016:
      return new FormObject(paramString);
    case 10019:
      return new FormObject(paramString);
    case 40:
      return new FormObject(paramString);
    case 979194486:
      return new XObject(paramString);
    case 708788029:
      return new OCObject(paramString);
    }
    if (paramInt2 == 373244477)
      return new FormObject(paramString);
    return new PdfObject(paramString);
  }

  public static PdfObject createObject(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    switch (paramInt1)
    {
    case 17:
      if (paramInt4 == 373244477)
        return new FormObject(paramInt2, paramInt3);
      if (paramInt4 == 487790868)
        return new MCObject(paramInt2, paramInt3);
      return new OutlineObject(paramInt2, paramInt3);
    case 4369:
      return new FormObject(paramInt2, paramInt3);
    case 4384:
      return new FormObject(paramInt2, paramInt3);
    case 1127568774:
      return new FSObject(paramInt2, paramInt3);
    case 4633:
      return new FormObject(paramInt2, paramInt3);
    case 4668:
      return new FormObject(paramInt2, paramInt3);
    case 4643:
      return new FormObject(paramInt2, paramInt3);
    case 19:
      return new FormObject(paramInt2, paramInt3);
    case 4886:
      return new EncryptionObject(paramInt2, paramInt3);
    case 2054190454:
      return new FontObject(paramInt2, paramInt3);
    case 1972801240:
      return new FontObject(paramInt2, paramInt3);
    case 946823533:
      return new FontObject(paramInt2, paramInt3);
    case 2087749783:
      return new ColorSpaceObject(paramInt2, paramInt3);
    case 1448698499:
      return new MCObject(paramInt2, paramInt3);
    case 20:
      if (paramInt4 == 373244477)
        return new FormObject(paramInt2, paramInt3);
      return new OCObject(paramInt2, paramInt3);
    case 5139:
      return new FormObject(paramInt2, paramInt3);
    case 1888135062:
      return new DecodeParmsObject(paramInt2, paramInt3);
    case -1547306032:
      return new FontObject(paramInt2, paramInt3);
    case 893600855:
      return new NamesObject(paramInt2, paramInt3);
    case 5152:
      return new FormObject(paramInt2, paramInt3);
    case 5154:
      return new ResourcesObject(paramInt2, paramInt3);
    case 5155:
      return new FormObject(paramInt2, paramInt3);
    case 21:
      return new FormObject(paramInt2, paramInt3);
    case 5398:
      return new FSObject(paramInt2, paramInt3);
    case 1232564598:
      return new FontObject(paramInt2, paramInt3);
    case 894663815:
      return new CompressedObject(paramInt2, paramInt3);
    case -1938465939:
      return new ExtGStateObject(paramInt2, paramInt3);
    case 22:
      return new FormObject(paramInt2, paramInt3);
    case 960643930:
      return new OutlineObject(paramInt2, paramInt3);
    case 5695:
      return new FormObject(paramInt2, paramInt3);
    case -1044665361:
      return new FontObject(paramInt2, paramInt3);
    case 746093177:
      return new FontObject(paramInt2, paramInt3);
    case 2021292334:
      return new FontObject(paramInt2, paramInt3);
    case 2021292335:
      return new FontObject(paramInt2, paramInt3);
    case 5667:
      return new FSObject(paramInt2, paramInt3);
    case 1518239089:
      return new FunctionObject(paramInt2, paramInt3);
    case 23:
      return new XObject(paramInt2, paramInt3);
    case 1111442775:
      return new GroupingObject(paramInt2, paramInt3);
    case 25:
      return new FormObject(paramInt2, paramInt3);
    case 6422:
      return new FormObject(paramInt2, paramInt3);
    case 423507519:
      return new InfoObject(paramInt2, paramInt3);
    case -2006286978:
      return new NamesObject(paramInt2, paramInt3);
    case 1314558361:
      return new DecodeParmsObject(paramInt2, paramInt3);
    case 6691:
      if (paramInt4 == 373244477)
        return new FormObject(paramInt2, paramInt3);
      return new NamesObject(paramInt2, paramInt3);
    case 27:
      if (paramInt4 == 487790868)
        return new MCObject(paramInt2, paramInt3);
      return new FormObject(paramInt2, paramInt3);
    case 826881374:
      return new OCObject(paramInt2, paramInt3);
    case 913275002:
      return new MCObject(paramInt2, paramInt3);
    case 489767739:
      return new MaskObject(paramInt2, paramInt3);
    case 1365674082:
      return new MetadataObject(paramInt2, paramInt3);
    case 7451:
      return new MKObject(paramInt2, paramInt3);
    case 30:
      return new FormObject(paramInt2, paramInt3);
    case 826094945:
      return new NamesObject(paramInt2, paramInt3);
    case 506808388:
      if (paramInt4 == 373244477)
        return new FormObject(paramInt2, paramInt3);
      return new OutlineObject(paramInt2, paramInt3);
    case 31:
      return new FormObject(paramInt2, paramInt3);
    case 2037870513:
      return new FormObject(paramInt2, paramInt3);
    case 7955:
      return new OCObject(paramInt2, paramInt3);
    case -1567847737:
      return new OCObject(paramInt2, paramInt3);
    case 2039833:
      return new XObject(paramInt2, paramInt3);
    case 1485011327:
      return new OutlineObject(paramInt2, paramInt3);
    case 825701731:
      return new PageObject(paramInt2, paramInt3);
    case 1719112618:
      return new MCObject(paramInt2, paramInt3);
    case 1146450818:
      return new PatternObject(paramInt2, paramInt3);
    case 8211:
      return new FormObject(paramInt2, paramInt3);
    case 8247:
      return new PageObject(paramInt2, paramInt3);
    case 8217:
      return new FormObject(paramInt2, paramInt3);
    case 1383295380:
      return new PdfObject(paramInt2, paramInt3);
    case 8223:
      return new FormObject(paramInt2, paramInt3);
    case 1061176672:
      FormObject localFormObject = new FormObject(paramInt2, paramInt3);
      localFormObject.setBoolean(524301630, false);
      return localFormObject;
    case -2089186617:
      return new OCObject(paramInt2, paramInt3);
    case 8230:
      return new FormObject(paramInt2, paramInt3);
    case 34:
      return new FormObject(paramInt2, paramInt3);
    case 2004251818:
      return new ResourcesObject(paramInt2, paramInt3);
    case -1263082253:
      return new FSObject(paramInt2, paramInt3);
    case 893350012:
      return new MCObject(paramInt2, paramInt3);
    case 574570308:
      return new PageObject(paramInt2, paramInt3);
    case 878474856:
      return new ShadingObject(paramInt2, paramInt3);
    case 489767774:
      return new MaskObject(paramInt2, paramInt3);
    case 1061502534:
      return new SoundObject(paramInt2, paramInt3);
    case -2000237823:
      return new MCObject(paramInt2, paramInt3);
    case 9250:
      return new MaskObject(paramInt2, paramInt3);
    case 1919185554:
      return new FontObject(paramInt2, paramInt3);
    case 37:
      return new FormObject(paramInt2, paramInt3);
    case 1127298906:
      return new OCObject(paramInt2, paramInt3);
    case 38:
      return new FormObject(paramInt2, paramInt3);
    case 2570558:
      return new FormObject(paramInt2, paramInt3);
    case 10016:
      return new FormObject(paramInt2, paramInt3);
    case 10019:
      return new FormObject(paramInt2, paramInt3);
    case 40:
      return new FormObject(paramInt2, paramInt3);
    case 979194486:
      return new XObject(paramInt2, paramInt3);
    case 708788029:
      return new OCObject(paramInt2, paramInt3);
    }
    return new PdfObject(paramInt2, paramInt3);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.raw.ObjectFactory
 * JD-Core Version:    0.6.2
 */