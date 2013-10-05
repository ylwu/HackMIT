package org.jpedal.jbig2.segment.symboldictionary;

import java.io.IOException;
import java.util.ArrayList;
import org.jpedal.jbig2.JBIG2Exception;
import org.jpedal.jbig2.decoders.ArithmeticDecoder;
import org.jpedal.jbig2.decoders.ArithmeticDecoderStats;
import org.jpedal.jbig2.decoders.DecodeIntResult;
import org.jpedal.jbig2.decoders.HuffmanDecoder;
import org.jpedal.jbig2.decoders.JBIG2StreamDecoder;
import org.jpedal.jbig2.image.JBIG2Bitmap;
import org.jpedal.jbig2.segment.Segment;
import org.jpedal.jbig2.segment.SegmentHeader;
import org.jpedal.jbig2.segment.tables.CodeTableSegment;
import org.jpedal.jbig2.util.BinaryOperation;

public class SymbolDictionarySegment extends Segment
{
  private int noOfExportedSymbols;
  private int noOfNewSymbols;
  private final short[] symbolDictionaryAdaptiveTemplateX = new short[4];
  private final short[] symbolDictionaryAdaptiveTemplateY = new short[4];
  private final short[] symbolDictionaryRAdaptiveTemplateX = new short[2];
  private final short[] symbolDictionaryRAdaptiveTemplateY = new short[2];
  private JBIG2Bitmap[] bitmaps;
  private final SymbolDictionaryFlags symbolDictionaryFlags = new SymbolDictionaryFlags();
  private ArithmeticDecoderStats genericRegionStats;
  private ArithmeticDecoderStats refinementRegionStats;

  public SymbolDictionarySegment(JBIG2StreamDecoder paramJBIG2StreamDecoder)
  {
    super(paramJBIG2StreamDecoder);
  }

  public void readSegment()
    throws IOException, JBIG2Exception
  {
    readSymbolDictionaryFlags();
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    int j = this.segmentHeader.getReferredToSegmentCount();
    int[] arrayOfInt1 = this.segmentHeader.getReferredToSegments();
    for (int k = 0; k < j; k++)
    {
      Segment localSegment = this.decoder.findSegment(arrayOfInt1[k]);
      int n = localSegment.getSegmentHeader().getSegmentType();
      if (n == 0)
        i += ((SymbolDictionarySegment)localSegment).noOfExportedSymbols;
      else if (n == 53)
        localArrayList.add(localSegment);
    }
    k = 0;
    int m = 1;
    while (m < i + this.noOfNewSymbols)
    {
      k++;
      m <<= 1;
    }
    JBIG2Bitmap[] arrayOfJBIG2Bitmap = new JBIG2Bitmap[i + this.noOfNewSymbols];
    int i1 = 0;
    SymbolDictionarySegment localSymbolDictionarySegment = null;
    for (m = 0; m < j; m++)
    {
      localObject1 = this.decoder.findSegment(arrayOfInt1[m]);
      if (((Segment)localObject1).getSegmentHeader().getSegmentType() == 0)
      {
        localSymbolDictionarySegment = (SymbolDictionarySegment)localObject1;
        for (int i2 = 0; i2 < localSymbolDictionarySegment.noOfExportedSymbols; i2++)
          arrayOfJBIG2Bitmap[(i1++)] = localSymbolDictionarySegment.bitmaps[i2];
      }
    }
    Object localObject1 = (int[][])null;
    int[][] arrayOfInt2 = (int[][])null;
    int[][] arrayOfInt3 = (int[][])null;
    int[][] arrayOfInt4 = (int[][])null;
    boolean bool = this.symbolDictionaryFlags.getFlagValue("SD_HUFF") != 0;
    int i3 = this.symbolDictionaryFlags.getFlagValue("SD_HUFF_DH");
    int i4 = this.symbolDictionaryFlags.getFlagValue("SD_HUFF_DW");
    int i5 = this.symbolDictionaryFlags.getFlagValue("SD_HUFF_BM_SIZE");
    int i6 = this.symbolDictionaryFlags.getFlagValue("SD_HUFF_AGG_INST");
    m = 0;
    if (bool)
    {
      if (i3 == 0)
        localObject1 = HuffmanDecoder.huffmanTableD;
      else if (i3 == 1)
        localObject1 = HuffmanDecoder.huffmanTableE;
      else
        localObject1 = ((CodeTableSegment)localArrayList.get(m++)).getHuffTable();
      if (i4 == 0)
        arrayOfInt2 = HuffmanDecoder.huffmanTableB;
      else if (i4 == 1)
        arrayOfInt2 = HuffmanDecoder.huffmanTableC;
      else
        arrayOfInt2 = ((CodeTableSegment)localArrayList.get(m++)).getHuffTable();
      if (i5 == 0)
        arrayOfInt3 = HuffmanDecoder.huffmanTableA;
      else
        arrayOfInt3 = ((CodeTableSegment)localArrayList.get(m++)).getHuffTable();
      if (i6 == 0)
        arrayOfInt4 = HuffmanDecoder.huffmanTableA;
      else
        arrayOfInt4 = ((CodeTableSegment)localArrayList.get(m)).getHuffTable();
    }
    int i7 = this.symbolDictionaryFlags.getFlagValue("BITMAP_CC_USED");
    int i8 = this.symbolDictionaryFlags.getFlagValue("SD_TEMPLATE");
    if (!bool)
    {
      if ((i7 != 0) && (localSymbolDictionarySegment != null))
        this.arithmeticDecoder.resetGenericStats(i8, localSymbolDictionarySegment.genericRegionStats);
      else
        this.arithmeticDecoder.resetGenericStats(i8, null);
      this.arithmeticDecoder.resetIntStats(k);
      this.arithmeticDecoder.start();
    }
    int i9 = this.symbolDictionaryFlags.getFlagValue("SD_REF_AGG");
    int i10 = this.symbolDictionaryFlags.getFlagValue("SD_R_TEMPLATE");
    if (i9 != 0)
      if ((i7 != 0) && (localSymbolDictionarySegment != null))
        this.arithmeticDecoder.resetRefinementStats(i10, localSymbolDictionarySegment.refinementRegionStats);
      else
        this.arithmeticDecoder.resetRefinementStats(i10, null);
    int[] arrayOfInt5 = new int[this.noOfNewSymbols];
    int i11 = 0;
    m = 0;
    int i15;
    while (m < this.noOfNewSymbols)
    {
      if (bool)
        i12 = this.huffmanDecoder.decodeInt((int[][])localObject1).intResult();
      else
        i12 = this.arithmeticDecoder.decodeInt(this.arithmeticDecoder.iadhStats).intResult();
      if ((i12 < 0) && (-i12 >= i11));
      i11 += i12;
      i13 = 0;
      i14 = 0;
      i15 = m;
      Object localObject2;
      int i16;
      int i21;
      Object localObject3;
      while (true)
      {
        if (bool)
          localObject2 = this.huffmanDecoder.decodeInt(arrayOfInt2);
        else
          localObject2 = this.arithmeticDecoder.decodeInt(this.arithmeticDecoder.iadwStats);
        if (!((DecodeIntResult)localObject2).booleanResult())
          break;
        i16 = ((DecodeIntResult)localObject2).intResult();
        if ((i16 < 0) && (-i16 >= i13));
        i13 += i16;
        if ((bool) && (i9 == 0))
        {
          arrayOfInt5[m] = i13;
          i14 += i13;
        }
        else if (i9 == 1)
        {
          int i17;
          if (bool)
            i17 = this.huffmanDecoder.decodeInt(arrayOfInt4).intResult();
          else
            i17 = this.arithmeticDecoder.decodeInt(this.arithmeticDecoder.iaaiStats).intResult();
          if (i17 == 1)
          {
            int i19;
            int i22;
            if (bool)
            {
              i19 = this.decoder.readBits(k);
              i21 = this.huffmanDecoder.decodeInt(HuffmanDecoder.huffmanTableO).intResult();
              i22 = this.huffmanDecoder.decodeInt(HuffmanDecoder.huffmanTableO).intResult();
              this.decoder.consumeRemainingBits();
              this.arithmeticDecoder.start();
            }
            else
            {
              i19 = (int)this.arithmeticDecoder.decodeIAID(k, this.arithmeticDecoder.iaidStats);
              i21 = this.arithmeticDecoder.decodeInt(this.arithmeticDecoder.iardxStats).intResult();
              i22 = this.arithmeticDecoder.decodeInt(this.arithmeticDecoder.iardyStats).intResult();
            }
            localObject3 = arrayOfJBIG2Bitmap[i19];
            JBIG2Bitmap localJBIG2Bitmap3 = new JBIG2Bitmap(i13, i11, this.arithmeticDecoder, this.huffmanDecoder, this.mmrDecoder);
            localJBIG2Bitmap3.readGenericRefinementRegion(i10, false, (JBIG2Bitmap)localObject3, i21, i22, this.symbolDictionaryRAdaptiveTemplateX, this.symbolDictionaryRAdaptiveTemplateY);
            arrayOfJBIG2Bitmap[(i + m)] = localJBIG2Bitmap3;
          }
          else
          {
            JBIG2Bitmap localJBIG2Bitmap2 = new JBIG2Bitmap(i13, i11, this.arithmeticDecoder, this.huffmanDecoder, this.mmrDecoder);
            localJBIG2Bitmap2.readTextRegion(bool, true, i17, 0, i + m, (int[][])null, k, arrayOfJBIG2Bitmap, 0, 0, false, 1, 0, HuffmanDecoder.huffmanTableF, HuffmanDecoder.huffmanTableH, HuffmanDecoder.huffmanTableK, HuffmanDecoder.huffmanTableO, HuffmanDecoder.huffmanTableO, HuffmanDecoder.huffmanTableO, HuffmanDecoder.huffmanTableO, i10, this.symbolDictionaryRAdaptiveTemplateX, this.symbolDictionaryRAdaptiveTemplateY, this.decoder);
            arrayOfJBIG2Bitmap[(i + m)] = localJBIG2Bitmap2;
          }
        }
        else
        {
          JBIG2Bitmap localJBIG2Bitmap1 = new JBIG2Bitmap(i13, i11, this.arithmeticDecoder, this.huffmanDecoder, this.mmrDecoder);
          localJBIG2Bitmap1.readBitmap(false, i8, false, false, null, this.symbolDictionaryAdaptiveTemplateX, this.symbolDictionaryAdaptiveTemplateY, 0);
          arrayOfJBIG2Bitmap[(i + m)] = localJBIG2Bitmap1;
        }
        m++;
      }
      if ((bool) && (i9 == 0))
      {
        i16 = this.huffmanDecoder.decodeInt(arrayOfInt3).intResult();
        this.decoder.consumeRemainingBits();
        localObject2 = new JBIG2Bitmap(i14, i11, this.arithmeticDecoder, this.huffmanDecoder, this.mmrDecoder);
        if (i16 == 0)
        {
          i18 = i14 % 8;
          int i20 = (int)Math.ceil(i14 / 8.0D);
          i21 = i11 * (i14 + 7 >> 3);
          short[] arrayOfShort = new short[i21];
          this.decoder.readByte(arrayOfShort);
          localObject3 = new short[i11][i20];
          int i23 = 0;
          for (int i24 = 0; i24 < i11; i24++)
            for (i25 = 0; i25 < i20; i25++)
            {
              localObject3[i24][i25] = arrayOfShort[i23];
              i23++;
            }
          i24 = 0;
          int i25 = 0;
          for (int i26 = 0; i26 < i11; i26++)
            for (int i27 = 0; i27 < i20; i27++)
            {
              int i28;
              int i29;
              int i30;
              int i31;
              if (i27 == i20 - 1)
              {
                i28 = localObject3[i26][i27];
                for (i29 = 7; i29 >= i18; i29--)
                {
                  i30 = (short)(1 << i29);
                  i31 = (i28 & i30) >> i29;
                  ((JBIG2Bitmap)localObject2).setPixel(i25, i24, i31);
                  i25++;
                }
                i24++;
                i25 = 0;
              }
              else
              {
                i28 = localObject3[i26][i27];
                for (i29 = 7; i29 >= 0; i29--)
                {
                  i30 = (short)(1 << i29);
                  i31 = (i28 & i30) >> i29;
                  ((JBIG2Bitmap)localObject2).setPixel(i25, i24, i31);
                  i25++;
                }
              }
            }
        }
        else
        {
          ((JBIG2Bitmap)localObject2).readBitmap(true, 0, false, false, null, null, null, i16);
        }
        int i18 = 0;
        while (i15 < m)
        {
          arrayOfJBIG2Bitmap[(i + i15)] = ((JBIG2Bitmap)localObject2).getSlice(i18, 0, arrayOfInt5[i15], i11);
          i18 += arrayOfInt5[i15];
          i15++;
        }
      }
    }
    this.bitmaps = new JBIG2Bitmap[this.noOfExportedSymbols];
    int i12 = m = 0;
    for (int i13 = 0; m < i + this.noOfNewSymbols; i13 = i13 == 0 ? 1 : 0)
    {
      if (bool)
        i14 = this.huffmanDecoder.decodeInt(HuffmanDecoder.huffmanTableA).intResult();
      else
        i14 = this.arithmeticDecoder.decodeInt(this.arithmeticDecoder.iaexStats).intResult();
      if (i13 != 0)
        for (i15 = 0; i15 < i14; i15++)
          this.bitmaps[(i12++)] = arrayOfJBIG2Bitmap[(m++)];
      else
        m += i14;
    }
    int i14 = this.symbolDictionaryFlags.getFlagValue("BITMAP_CC_RETAINED");
    if ((!bool) && (i14 == 1))
    {
      this.genericRegionStats = this.genericRegionStats.copy();
      if (i9 == 1)
        this.refinementRegionStats = this.refinementRegionStats.copy();
    }
    this.decoder.consumeRemainingBits();
  }

  private void readSymbolDictionaryFlags()
  {
    short[] arrayOfShort1 = new short[2];
    this.decoder.readByte(arrayOfShort1);
    int i = BinaryOperation.getInt16(arrayOfShort1);
    this.symbolDictionaryFlags.setFlags(i);
    int j = this.symbolDictionaryFlags.getFlagValue("SD_HUFF");
    int k = this.symbolDictionaryFlags.getFlagValue("SD_TEMPLATE");
    if (j == 0)
      if (k == 0)
      {
        this.symbolDictionaryAdaptiveTemplateX[0] = readATValue();
        this.symbolDictionaryAdaptiveTemplateY[0] = readATValue();
        this.symbolDictionaryAdaptiveTemplateX[1] = readATValue();
        this.symbolDictionaryAdaptiveTemplateY[1] = readATValue();
        this.symbolDictionaryAdaptiveTemplateX[2] = readATValue();
        this.symbolDictionaryAdaptiveTemplateY[2] = readATValue();
        this.symbolDictionaryAdaptiveTemplateX[3] = readATValue();
        this.symbolDictionaryAdaptiveTemplateY[3] = readATValue();
      }
      else
      {
        this.symbolDictionaryAdaptiveTemplateX[0] = readATValue();
        this.symbolDictionaryAdaptiveTemplateY[0] = readATValue();
      }
    int m = this.symbolDictionaryFlags.getFlagValue("SD_REF_AGG");
    int n = this.symbolDictionaryFlags.getFlagValue("SD_R_TEMPLATE");
    if ((m != 0) && (n == 0))
    {
      this.symbolDictionaryRAdaptiveTemplateX[0] = readATValue();
      this.symbolDictionaryRAdaptiveTemplateY[0] = readATValue();
      this.symbolDictionaryRAdaptiveTemplateX[1] = readATValue();
      this.symbolDictionaryRAdaptiveTemplateY[1] = readATValue();
    }
    short[] arrayOfShort2 = new short[4];
    this.decoder.readByte(arrayOfShort2);
    int i1 = BinaryOperation.getInt32(arrayOfShort2);
    this.noOfExportedSymbols = i1;
    short[] arrayOfShort3 = new short[4];
    this.decoder.readByte(arrayOfShort3);
    int i2 = BinaryOperation.getInt32(arrayOfShort3);
    this.noOfNewSymbols = i2;
  }

  public int getNoOfExportedSymbols()
  {
    return this.noOfExportedSymbols;
  }

  public JBIG2Bitmap[] getBitmaps()
  {
    return this.bitmaps;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.jbig2.segment.symboldictionary.SymbolDictionarySegment
 * JD-Core Version:    0.6.2
 */