package org.jpedal.utils;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeNow
{
  public static final String getShortTimeNow()
  {
    Calendar localCalendar = Calendar.getInstance();
    String str = localCalendar.get(1) + format(1 + localCalendar.get(2)) + format(localCalendar.get(5)) + format(localCalendar.get(11)) + format(localCalendar.get(12)) + format(localCalendar.get(13));
    return str;
  }

  public static final String getTimeNow()
  {
    DateFormat localDateFormat1 = DateFormat.getDateInstance();
    DateFormat localDateFormat2 = DateFormat.getTimeInstance(3);
    return localDateFormat1.format(new Date()) + ' ' + localDateFormat2.format(new Date());
  }

  private static final String format(int paramInt)
  {
    String str = String.valueOf(paramInt);
    if (str.length() == 1)
      str = '0' + str;
    return str;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.utils.TimeNow
 * JD-Core Version:    0.6.2
 */