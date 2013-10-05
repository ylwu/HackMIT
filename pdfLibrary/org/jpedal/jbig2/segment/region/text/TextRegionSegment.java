package org.jpedal.jbig2.segment.region.text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.jpedal.jbig2.JBIG2Exception;
import org.jpedal.jbig2.decoders.ArithmeticDecoder;
import org.jpedal.jbig2.decoders.DecodeIntResult;
import org.jpedal.jbig2.decoders.HuffmanDecoder;
import org.jpedal.jbig2.decoders.JBIG2StreamDecoder;
import org.jpedal.jbig2.image.JBIG2Bitmap;
import org.jpedal.jbig2.segment.Segment;
import org.jpedal.jbig2.segment.SegmentHeader;
import org.jpedal.jbig2.segment.pageinformation.PageInformationSegment;
import org.jpedal.jbig2.segment.region.RegionFlags;
import org.jpedal.jbig2.segment.region.RegionSegment;
import org.jpedal.jbig2.segment.symboldictionary.SymbolDictionarySegment;
import org.jpedal.jbig2.segment.tables.CodeTableSegment;
import org.jpedal.jbig2.util.BinaryOperation;

public class TextRegionSegment extends RegionSegment
{
  private final TextRegionFlags textRegionFlags = new TextRegionFlags();
  private final TextRegionHuffmanFlags textRegionHuffmanFlags = new TextRegionHuffmanFlags();
  private final boolean inlineImage;
  private final short[] symbolRegionAdaptiveTemplateX = new short[2];
  private final short[] symbolRegionAdaptiveTemplateY = new short[2];

  public TextRegionSegment(JBIG2StreamDecoder paramJBIG2StreamDecoder, boolean paramBoolean)
  {
    super(paramJBIG2StreamDecoder);
    this.inlineImage = paramBoolean;
  }

  public void readSegment()
    throws IOException, JBIG2Exception
  {
    super.readSegment();
    readTextRegionFlags();
    short[] arrayOfShort = new short[4];
    this.decoder.readByte(arrayOfShort);
    int i = BinaryOperation.getInt32(arrayOfShort);
    int j = this.segmentHeader.getReferredToSegmentCount();
    int[] arrayOfInt = this.segmentHeader.getReferredToSegments();
    ArrayList localArrayList1 = new ArrayList();
    ArrayList localArrayList2 = new ArrayList();
    int k = 0;
    for (int m = 0; m < j; m++)
    {
      Segment localSegment = this.decoder.findSegment(arrayOfInt[m]);
      i1 = localSegment.getSegmentHeader().getSegmentType();
      if (i1 == 0)
      {
        localArrayList2.add(localSegment);
        k += ((SymbolDictionarySegment)localSegment).getNoOfExportedSymbols();
      }
      else if (i1 == 53)
      {
        localArrayList1.add(localSegment);
      }
    }
    m = 0;
    int n = 1;
    while (n < k)
    {
      m++;
      n <<= 1;
    }
    int i1 = 0;
    JBIG2Bitmap[] arrayOfJBIG2Bitmap = new JBIG2Bitmap[k];
    Object localObject1 = localArrayList2.iterator();
    while (((Iterator)localObject1).hasNext())
    {
      localObject2 = (Segment)((Iterator)localObject1).next();
      if (((Segment)localObject2).getSegmentHeader().getSegmentType() == 0)
      {
        localObject3 = ((SymbolDictionarySegment)localObject2).getBitmaps();
        for (arrayOfInt3 : localObject3)
        {
          arrayOfJBIG2Bitmap[i1] = arrayOfInt3;
          i1++;
        }
      }
    }
    localObject1 = (int[][])null;
    Object localObject2 = (int[][])null;
    Object localObject3 = (int[][])null;
    ??? = (int[][])null;
    int[][] arrayOfInt1 = (int[][])null;
    int[][] arrayOfInt2 = (int[][])null;
    int[][] arrayOfInt3 = (int[][])null;
    boolean bool = this.textRegionFlags.getFlagValue("SB_HUFF") != 0;
    if (bool)
    {
      int i5 = this.textRegionHuffmanFlags.getFlagValue("SB_HUFF_FS");
      if (i5 == 0)
      {
        localObject1 = HuffmanDecoder.huffmanTableF;
      }
      else if (i5 == 1)
      {
        localObject1 = HuffmanDecoder.huffmanTableG;
      }
      else if (i5 == 3)
      {
        localObject1 = ((CodeTableSegment)localArrayList1.get(0)).getHuffTable();
        localArrayList1.remove(0);
      }
      int i6 = this.textRegionHuffmanFlags.getFlagValue("SB_HUFF_DS");
      if (i6 == 0)
      {
        localObject2 = HuffmanDecoder.huffmanTableH;
      }
      else if (i6 == 1)
      {
        localObject2 = HuffmanDecoder.huffmanTableI;
      }
      else if (i6 == 2)
      {
        localObject2 = HuffmanDecoder.huffmanTableJ;
      }
      else if (i6 == 3)
      {
        localObject2 = ((CodeTableSegment)localArrayList1.get(0)).getHuffTable();
        localArrayList1.remove(0);
      }
      i7 = this.textRegionHuffmanFlags.getFlagValue("SB_HUFF_DT");
      if (i7 == 0)
      {
        localObject3 = HuffmanDecoder.huffmanTableK;
      }
      else if (i7 == 1)
      {
        localObject3 = HuffmanDecoder.huffmanTableL;
      }
      else if (i7 == 2)
      {
        localObject3 = HuffmanDecoder.huffmanTableM;
      }
      else if (i7 == 3)
      {
        localObject3 = ((CodeTableSegment)localArrayList1.get(0)).getHuffTable();
        localArrayList1.remove(0);
      }
      i8 = this.textRegionHuffmanFlags.getFlagValue("SB_HUFF_RDW");
      if (i8 == 0)
      {
        ??? = HuffmanDecoder.huffmanTableN;
      }
      else if (i8 == 1)
      {
        ??? = HuffmanDecoder.huffmanTableO;
      }
      else if (i8 == 3)
      {
        ??? = ((CodeTableSegment)localArrayList1.get(0)).getHuffTable();
        localArrayList1.remove(0);
      }
      i9 = this.textRegionHuffmanFlags.getFlagValue("SB_HUFF_RDH");
      if (i9 == 0)
      {
        arrayOfInt1 = HuffmanDecoder.huffmanTableN;
      }
      else if (i9 == 1)
      {
        arrayOfInt1 = HuffmanDecoder.huffmanTableO;
      }
      else if (i9 == 3)
      {
        arrayOfInt1 = ((CodeTableSegment)localArrayList1.get(0)).getHuffTable();
        localArrayList1.remove(0);
      }
      i10 = this.textRegionHuffmanFlags.getFlagValue("SB_HUFF_RDX");
      if (i10 == 0)
      {
        arrayOfInt2 = HuffmanDecoder.huffmanTableN;
      }
      else if (i10 == 1)
      {
        arrayOfInt2 = HuffmanDecoder.huffmanTableO;
      }
      else if (i10 == 3)
      {
        arrayOfInt2 = ((CodeTableSegment)localArrayList1.get(0)).getHuffTable();
        localArrayList1.remove(0);
      }
      i11 = this.textRegionHuffmanFlags.getFlagValue("SB_HUFF_RDY");
      if (i11 == 0)
      {
        arrayOfInt3 = HuffmanDecoder.huffmanTableN;
      }
      else if (i11 == 1)
      {
        arrayOfInt3 = HuffmanDecoder.huffmanTableO;
      }
      else if (i11 == 3)
      {
        arrayOfInt3 = ((CodeTableSegment)localArrayList1.get(0)).getHuffTable();
        localArrayList1.remove(0);
      }
      i12 = this.textRegionHuffmanFlags.getFlagValue("SB_HUFF_RSIZE");
      if (i12 == 1)
        localArrayList1.remove(0);
    }
    int[][] arrayOfInt4 = new int[36][4];
    int[][] arrayOfInt5 = new int[k + 1][4];
    if (bool)
    {
      this.decoder.consumeRemainingBits();
      for (int i4 = 0; i4 < 32; i4++)
        arrayOfInt4[i4] = { i4, this.decoder.readBits(4), 0, 0 };
      arrayOfInt4[32] = { 259, this.decoder.readBits(4), 2, 0 };
      arrayOfInt4[33] = { 515, this.decoder.readBits(4), 3, 0 };
      arrayOfInt4[34] = { 523, this.decoder.readBits(4), 7, 0 };
      arrayOfInt4[35] = { 0, 0, -1, 0 };
      arrayOfInt4 = HuffmanDecoder.buildTable(arrayOfInt4, 35);
      for (i4 = 0; i4 < k; i4++)
        arrayOfInt5[i4] = { i4, 0, 0, 0 };
      i4 = 0;
      while (i4 < k)
      {
        i7 = this.huffmanDecoder.decodeInt(arrayOfInt4).intResult();
        if (i7 > 512)
        {
          i7 -= 512;
          while ((i7 != 0) && (i4 < k))
          {
            arrayOfInt5[(i4++)][1] = 0;
            i7--;
          }
        }
        if (i7 > 256)
        {
          i7 -= 256;
          while ((i7 != 0) && (i4 < k))
          {
            arrayOfInt5[i4][1] = arrayOfInt5[(i4 - 1)][1];
            i4++;
            i7--;
          }
        }
        arrayOfInt5[(i4++)][1] = i7;
      }
      arrayOfInt5[k][1] = 0;
      arrayOfInt5[k][2] = -1;
      arrayOfInt5 = HuffmanDecoder.buildTable(arrayOfInt5, k);
      this.decoder.consumeRemainingBits();
    }
    else
    {
      arrayOfInt5 = (int[][])null;
      this.arithmeticDecoder.resetIntStats(m);
      this.arithmeticDecoder.start();
    }
    int i7 = this.textRegionFlags.getFlagValue("SB_REFINE") != 0 ? 1 : 0;
    int i8 = this.textRegionFlags.getFlagValue("LOG_SB_STRIPES");
    int i9 = this.textRegionFlags.getFlagValue("SB_DEF_PIXEL");
    int i10 = this.textRegionFlags.getFlagValue("SB_COMB_OP");
    int i11 = this.textRegionFlags.getFlagValue("TRANSPOSED") != 0 ? 1 : 0;
    int i12 = this.textRegionFlags.getFlagValue("REF_CORNER");
    int i13 = this.textRegionFlags.getFlagValue("SB_DS_OFFSET");
    int i14 = this.textRegionFlags.getFlagValue("SB_R_TEMPLATE");
    if (i7 != 0)
      this.arithmeticDecoder.resetRefinementStats(i14, null);
    JBIG2Bitmap localJBIG2Bitmap1 = new JBIG2Bitmap(this.regionBitmapWidth, this.regionBitmapHeight, this.arithmeticDecoder, this.huffmanDecoder, this.mmrDecoder);
    localJBIG2Bitmap1.readTextRegion(bool, i7, i, i8, k, arrayOfInt5, m, arrayOfJBIG2Bitmap, i9, i10, i11, i12, i13, (int[][])localObject1, (int[][])localObject2, (int[][])localObject3, (int[][])???, arrayOfInt1, arrayOfInt2, arrayOfInt3, i14, this.symbolRegionAdaptiveTemplateX, this.symbolRegionAdaptiveTemplateY, this.decoder);
    if (this.inlineImage)
    {
      PageInformationSegment localPageInformationSegment = this.decoder.findPageSegment(this.segmentHeader.getPageAssociation());
      JBIG2Bitmap localJBIG2Bitmap2 = localPageInformationSegment.getPageBitmap();
      int i15 = this.regionFlags.getFlagValue("EXTERNAL_COMBINATION_OPERATOR");
      localJBIG2Bitmap2.combine(localJBIG2Bitmap1, this.regionBitmapXLocation, this.regionBitmapYLocation, i15);
    }
    else
    {
      localJBIG2Bitmap1.setBitmapNumber(getSegmentHeader().getSegmentNumber());
      this.decoder.appendBitmap(localJBIG2Bitmap1);
    }
    this.decoder.consumeRemainingBits();
  }

  private void readTextRegionFlags()
  {
    short[] arrayOfShort1 = new short[2];
    this.decoder.readByte(arrayOfShort1);
    int i = BinaryOperation.getInt16(arrayOfShort1);
    this.textRegionFlags.setFlags(i);
    int j = this.textRegionFlags.getFlagValue("SB_HUFF") != 0 ? 1 : 0;
    if (j != 0)
    {
      short[] arrayOfShort2 = new short[2];
      this.decoder.readByte(arrayOfShort2);
      i = BinaryOperation.getInt16(arrayOfShort2);
      this.textRegionHuffmanFlags.setFlags(i);
    }
    int k = this.textRegionFlags.getFlagValue("SB_REFINE") != 0 ? 1 : 0;
    int m = this.textRegionFlags.getFlagValue("SB_R_TEMPLATE");
    if ((k != 0) && (m == 0))
    {
      this.symbolRegionAdaptiveTemplateX[0] = readATValue();
      this.symbolRegionAdaptiveTemplateY[0] = readATValue();
      this.symbolRegionAdaptiveTemplateX[1] = readATValue();
      this.symbolRegionAdaptiveTemplateY[1] = readATValue();
    }
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.jbig2.segment.region.text.TextRegionSegment
 * JD-Core Version:    0.6.2
 */