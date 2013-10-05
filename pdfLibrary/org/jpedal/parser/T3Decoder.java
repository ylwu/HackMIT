package org.jpedal.parser;

public class T3Decoder extends BaseDecoder
{
  int T3maxWidth;
  int T3maxHeight;
  boolean ignoreColors = false;

  private void d1(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    this.ignoreColors = true;
    this.T3maxWidth = ((int)paramFloat3);
    if (paramFloat3 == 0.0F)
      this.T3maxWidth = ((int)(paramFloat2 - paramFloat1));
    else
      this.T3maxWidth = ((int)paramFloat3);
    this.T3maxHeight = ((int)paramFloat6);
    if (paramFloat6 == 0.0F)
      this.T3maxHeight = ((int)(paramFloat4 - paramFloat5));
    else
      this.T3maxHeight = ((int)paramFloat6);
  }

  private void d0(int paramInt1, int paramInt2)
  {
    this.ignoreColors = false;
    this.T3maxWidth = paramInt1;
    this.T3maxHeight = paramInt2;
  }

  public void processToken(int paramInt)
  {
    switch (paramInt)
    {
    case 25648:
      d0((int)this.parser.parseFloat(0), (int)this.parser.parseFloat(1));
      break;
    case 25649:
      d1(this.parser.parseFloat(1), this.parser.parseFloat(3), this.parser.parseFloat(5), this.parser.parseFloat(0), this.parser.parseFloat(2), this.parser.parseFloat(4));
    }
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.parser.T3Decoder
 * JD-Core Version:    0.6.2
 */