package org.jpedal.io;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import java.io.PrintStream;

public class Speech
{
  public static String selectedVoice = "kevin16";

  public static boolean speechAvailible()
  {
    String str = System.getProperty("java.class.path");
    return str.contains("freetts");
  }

  public static String[] listVoices()
  {
    VoiceManager localVoiceManager = VoiceManager.getInstance();
    Voice[] arrayOfVoice = localVoiceManager.getVoices();
    String[] arrayOfString = new String[arrayOfVoice.length];
    for (int i = 0; i < arrayOfVoice.length; i++)
      arrayOfString[i] = (arrayOfVoice[i].getName() + '(' + arrayOfVoice[i].getDomain() + " domain)");
    return arrayOfString;
  }

  public static void speakText(String paramString)
  {
    if (speechAvailible())
    {
      VoiceManager localVoiceManager = VoiceManager.getInstance();
      Voice localVoice = localVoiceManager.getVoice(selectedVoice);
      if (localVoice == null)
      {
        System.err.println("Cannot find voice kevin16.\nExiting.");
        throw new RuntimeException("Cannot find voice kevin16.\nExiting.");
      }
      localVoice.allocate();
      localVoice.speak(paramString);
      localVoice.deallocate();
    }
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.io.Speech
 * JD-Core Version:    0.6.2
 */