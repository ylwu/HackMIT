package org.jpedal.objects.acroforms.creation;

import java.util.EnumSet;
import java.util.Map;
import org.jpedal.io.PdfObjectReader;
import org.jpedal.objects.Javascript;
import org.jpedal.objects.PdfPageData;
import org.jpedal.objects.acroforms.GUIData;
import org.jpedal.objects.acroforms.actions.ActionHandler;
import org.jpedal.objects.raw.FormObject;
import org.jpedal.render.DynamicVectorRenderer;

public abstract interface FormFactory
{
  public static final Integer UNKNOWN = Integer.valueOf(-1);
  public static final Integer LIST = Integer.valueOf(1);
  public static final Integer COMBOBOX = Integer.valueOf(2);
  public static final Integer SINGLELINETEXT = Integer.valueOf(3);
  public static final Integer SINGLELINEPASSWORD = Integer.valueOf(4);
  public static final Integer MULTILINETEXT = Integer.valueOf(5);
  public static final Integer MULTILINEPASSWORD = Integer.valueOf(6);
  public static final Integer PUSHBUTTON = Integer.valueOf(7);
  public static final Integer RADIOBUTTON = Integer.valueOf(8);
  public static final Integer CHECKBOXBUTTON = Integer.valueOf(9);
  public static final Integer ANNOTATION = Integer.valueOf(10);
  public static final Integer SIGNATURE = Integer.valueOf(11);
  public static final int unknown = -1;
  public static final int list = 1;
  public static final int combobox = 2;
  public static final int singlelinetext = 3;
  public static final int singlelinepassword = 4;
  public static final int multilinetext = 5;
  public static final int multilinepassword = 6;
  public static final int pushbutton = 7;
  public static final int radiobutton = 8;
  public static final int checkboxbutton = 9;
  public static final int annotation = 10;
  public static final int signature = 11;
  public static final int SWING = 1;
  public static final int ULC = 2;
  public static final int HTML = 3;
  public static final int SVG = 4;

  public abstract Object listField(FormObject paramFormObject);

  public abstract Object comboBox(FormObject paramFormObject);

  public abstract Object singleLineText(FormObject paramFormObject);

  public abstract Object singleLinePassword(FormObject paramFormObject);

  public abstract Object multiLineText(FormObject paramFormObject);

  public abstract Object multiLinePassword(FormObject paramFormObject);

  public abstract Object pushBut(FormObject paramFormObject);

  public abstract Object radioBut(FormObject paramFormObject);

  public abstract Object checkBoxBut(FormObject paramFormObject);

  public abstract Object annotationButton(FormObject paramFormObject);

  public abstract Object signature(FormObject paramFormObject);

  public abstract void reset(Object[] paramArrayOfObject, ActionHandler paramActionHandler, PdfPageData paramPdfPageData, PdfObjectReader paramPdfObjectReader);

  public abstract GUIData getCustomCompData();

  public abstract int getType();

  public abstract void indexAllKids();

  public abstract void setAnnotOrder(Map paramMap);

  public abstract void setOptions(EnumSet paramEnumSet);

  public abstract void setDVR(DynamicVectorRenderer paramDynamicVectorRenderer, Javascript paramJavascript);

  public abstract void syncValues(SwingFormFactory paramSwingFormFactory);
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.acroforms.creation.FormFactory
 * JD-Core Version:    0.6.2
 */