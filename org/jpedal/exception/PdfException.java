package org.jpedal.exception;

public class PdfException extends Exception
{
  protected String error_message = "";

  public String getMessage()
  {
    return this.error_message;
  }

  public PdfException()
  {
  }

  public PdfException(String paramString)
  {
    this.error_message = paramString;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.exception.PdfException
 * JD-Core Version:    0.6.2
 */