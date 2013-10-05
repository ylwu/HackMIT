package org.jpedal.utils;

import java.io.FileWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class LogWriter
{
  public static LogScanner logScanner = null;
  public static boolean debug = false;
  public static String log_name = null;
  public static boolean testing = false;
  private static boolean verbose = false;
  private static Set<String> filterValues = getFilterSet();

  public static final void resetLogFile()
  {
    if (log_name != null)
      try
      {
        PrintWriter localPrintWriter = new PrintWriter(new FileWriter(log_name, false));
        localPrintWriter.println(TimeNow.getTimeNow() + " Running Storypad");
        localPrintWriter.flush();
        localPrintWriter.close();
      }
      catch (Exception localException)
      {
        System.err.println("Exception " + localException + " attempting to write to log file " + log_name);
      }
  }

  private static final Set getFilterSet()
  {
    String str = System.getProperty("org.jpedal.inclusiveLogFilters");
    HashSet localHashSet = null;
    if (str != null)
      localHashSet = new HashSet(Arrays.asList(str.toLowerCase().split("[,]")));
    return localHashSet;
  }

  public static final boolean isOutput()
  {
    return (verbose) || (logScanner != null);
  }

  public static final void writeLog(String paramString)
  {
    if ((filterValues != null) && (paramString != null))
    {
      int i = 0;
      Iterator localIterator = filterValues.iterator();
      while (localIterator.hasNext())
      {
        String str = (String)localIterator.next();
        if (paramString.toLowerCase().contains(str))
        {
          i = 1;
          break;
        }
      }
      if (i == 0)
        return;
    }
    if (logScanner != null)
      logScanner.message(paramString);
    if (verbose)
      System.out.println(paramString);
    if (log_name != null)
      try
      {
        PrintWriter localPrintWriter = new PrintWriter(new FileWriter(log_name, true));
        if (!testing)
          localPrintWriter.println(TimeNow.getTimeNow() + ' ' + paramString);
        localPrintWriter.println(paramString);
        localPrintWriter.flush();
        localPrintWriter.close();
      }
      catch (Exception localException)
      {
        System.err.println("Exception " + localException + " attempting to write to log file " + log_name);
      }
  }

  public static final void setupLogFile(String paramString)
  {
    if (paramString != null)
      if (paramString.indexOf(118) != -1)
      {
        verbose = true;
        writeLog("Verbose on");
      }
      else
      {
        verbose = false;
      }
    if (!testing)
    {
      writeLog("Software version - 5.06b04");
      writeLog("Software started - " + TimeNow.getTimeNow());
    }
    writeLog("=======================================================");
  }

  public static void writeFormLog(String paramString, boolean paramBoolean)
  {
    if (paramBoolean)
      System.out.println("[forms] " + paramString);
    writeLog("[forms] " + paramString);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.utils.LogWriter
 * JD-Core Version:    0.6.2
 */