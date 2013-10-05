package org.jpedal.io;

public class TextTokens
{
  private final byte[] content;
  private int length = 0;
  private int currentCharPointer;

  public TextTokens(byte[] paramArrayOfByte)
  {
    this.content = paramArrayOfByte;
    this.length = paramArrayOfByte.length;
    this.currentCharPointer = 0;
  }

  public boolean hasMoreTokens()
  {
    return this.currentCharPointer < this.length;
  }

  public char nextUnicodeToken()
  {
    int j = 0;
    int i = nextToken();
    if ((i == 13) && (hasMoreTokens()))
      i = nextToken();
    if (hasMoreTokens())
    {
      j = nextToken();
      if ((j == 13) && (hasMoreTokens()))
        j = nextToken();
    }
    return (char)((i << 8) + j);
  }

  private char getChar(int paramInt)
  {
    int i = this.content[paramInt] & 0xFF;
    return (char)i;
  }

  public char nextToken()
  {
    char c = getChar(this.currentCharPointer);
    this.currentCharPointer += 1;
    return c;
  }

  public boolean isUnicode()
  {
    if ((this.length >= 2) && (nextToken() == 'þ') && (nextToken() == 'ÿ'))
      return true;
    this.currentCharPointer = 0;
    return false;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.io.TextTokens
 * JD-Core Version:    0.6.2
 */