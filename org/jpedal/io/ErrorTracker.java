package org.jpedal.io;

public class ErrorTracker
{
  public boolean pageSuccessful = true;
  private String pageErrorMessages = "";

  public String getPageFailureMessage()
  {
    return this.pageErrorMessages;
  }

  public void addPageFailureMessage(String paramString)
  {
    this.pageSuccessful = false;
    this.pageErrorMessages = (this.pageErrorMessages + paramString + '\n');
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.io.ErrorTracker
 * JD-Core Version:    0.6.2
 */