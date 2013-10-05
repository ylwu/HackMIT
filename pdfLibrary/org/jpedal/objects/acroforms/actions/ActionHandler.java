package org.jpedal.objects.acroforms.actions;

import org.jpedal.PdfDecoderInt;
import org.jpedal.objects.Javascript;
import org.jpedal.objects.acroforms.AcroRenderer;
import org.jpedal.objects.layers.PdfLayerList;
import org.jpedal.objects.raw.FormObject;
import org.jpedal.objects.raw.PdfObject;

public abstract interface ActionHandler
{
  public static final int MOUSEPRESSED = 1;
  public static final int MOUSERELEASED = 2;
  public static final int MOUSECLICKED = 3;
  public static final int MOUSEENTERED = 4;
  public static final int MOUSEEXITED = 5;
  public static final int FOCUS_EVENT = 6;
  public static final int TODO = -1;
  public static final int NOMESSAGE = 0;
  public static final int REJECTKEY = 1;
  public static final int STOPPROCESSING = 2;
  public static final int VALUESCHANGED = 3;

  public abstract void A(Object paramObject, FormObject paramFormObject, int paramInt);

  public abstract void E(Object paramObject, FormObject paramFormObject);

  public abstract void X(Object paramObject, FormObject paramFormObject);

  public abstract void D(Object paramObject, FormObject paramFormObject);

  public abstract void U(Object paramObject, FormObject paramFormObject);

  public abstract void Fo(Object paramObject, FormObject paramFormObject);

  public abstract void Bl(Object paramObject, FormObject paramFormObject);

  public abstract void PO(PdfObject paramPdfObject, int paramInt);

  public abstract void O(PdfObject paramPdfObject, int paramInt);

  public abstract void PC(PdfObject paramPdfObject, int paramInt);

  public abstract void PV(PdfObject paramPdfObject, int paramInt);

  public abstract void PI(PdfObject paramPdfObject, int paramInt);

  public abstract int K(Object paramObject, FormObject paramFormObject, int paramInt);

  public abstract void F(FormObject paramFormObject);

  public abstract void V(Object paramObject, FormObject paramFormObject, int paramInt);

  public abstract void C(FormObject paramFormObject);

  public abstract PdfDecoderInt getPDFDecoder();

  public abstract Object setHoverCursor();

  public abstract void init(PdfDecoderInt paramPdfDecoderInt, Javascript paramJavascript, AcroRenderer paramAcroRenderer);

  public abstract PdfLayerList getLayerHandler();

  public abstract void changeTo(String paramString, int paramInt, Object paramObject, Integer paramInteger, boolean paramBoolean);

  public abstract int gotoDest(PdfObject paramPdfObject, int paramInt1, int paramInt2);
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.acroforms.actions.ActionHandler
 * JD-Core Version:    0.6.2
 */