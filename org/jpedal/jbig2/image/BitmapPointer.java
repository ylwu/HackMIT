package org.jpedal.jbig2.image;

class BitmapPointer
{
  private int x;
  private final int width;
  private final int height;
  private final int line;
  private int lineOffset;
  private final byte[] data;

  public BitmapPointer(JBIG2Bitmap paramJBIG2Bitmap)
  {
    this.height = paramJBIG2Bitmap.getHeight();
    this.width = paramJBIG2Bitmap.getWidth();
    this.line = paramJBIG2Bitmap.getLine();
    this.data = paramJBIG2Bitmap.getData();
  }

  public void setPointer(int paramInt1, int paramInt2)
  {
    this.x = paramInt1;
    if ((paramInt2 < 0) || (paramInt2 >= this.height))
      this.lineOffset = -1;
    else
      this.lineOffset = (paramInt2 * this.line);
  }

  public int nextPixel()
  {
    if ((this.x >= this.width) || (this.lineOffset == -1))
      return 0;
    if (this.x < 0)
    {
      this.x += 1;
      return 0;
    }
    int i = (this.data[(this.lineOffset + (this.x >> 3))] & 1 << 7 - (this.x & 0x7)) != 0 ? 1 : 0;
    this.x += 1;
    return i;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.jbig2.image.BitmapPointer
 * JD-Core Version:    0.6.2
 */