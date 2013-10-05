package org.jpedal.jbig2.decoders;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import org.jpedal.jbig2.JBIG2Exception;
import org.jpedal.jbig2.image.JBIG2Bitmap;
import org.jpedal.jbig2.io.StreamReader;
import org.jpedal.jbig2.segment.Segment;
import org.jpedal.jbig2.segment.SegmentHeader;
import org.jpedal.jbig2.segment.extensions.ExtensionSegment;
import org.jpedal.jbig2.segment.pageinformation.PageInformationSegment;
import org.jpedal.jbig2.segment.pattern.PatternDictionarySegment;
import org.jpedal.jbig2.segment.region.generic.GenericRegionSegment;
import org.jpedal.jbig2.segment.region.halftone.HalftoneRegionSegment;
import org.jpedal.jbig2.segment.region.refinement.RefinementRegionSegment;
import org.jpedal.jbig2.segment.region.text.TextRegionSegment;
import org.jpedal.jbig2.segment.stripes.EndOfStripeSegment;
import org.jpedal.jbig2.segment.symboldictionary.SymbolDictionarySegment;
import org.jpedal.jbig2.segment.tables.CodeTableSegment;
import org.jpedal.jbig2.util.BinaryOperation;

public class JBIG2StreamDecoder
{
  private StreamReader reader;
  private boolean noOfPagesKnown;
  private boolean randomAccessOrganisation;
  private int noOfPages = -1;
  private final ArrayList<Segment> segments = new ArrayList();
  private final ArrayList<JBIG2Bitmap> bitmaps = new ArrayList();
  private byte[] globalData;
  private ArithmeticDecoder arithmeticDecoder;
  private HuffmanDecoder huffmanDecoder;
  private MMRDecoder mmrDecoder;
  public static final boolean debug = false;

  public void movePointer(int paramInt)
  {
    this.reader.movePointer(paramInt);
  }

  public void setGlobalData(byte[] paramArrayOfByte)
  {
    this.globalData = paramArrayOfByte;
  }

  public void decodeJBIG2(byte[] paramArrayOfByte)
    throws IOException, JBIG2Exception
  {
    this.reader = new StreamReader(paramArrayOfByte);
    resetDecoder();
    boolean bool = checkHeader();
    if (!bool)
    {
      this.noOfPagesKnown = true;
      this.randomAccessOrganisation = false;
      this.noOfPages = 1;
      if (this.globalData != null)
      {
        this.reader = new StreamReader(this.globalData);
        this.huffmanDecoder = new HuffmanDecoder(this.reader);
        this.mmrDecoder = new MMRDecoder(this.reader);
        this.arithmeticDecoder = new ArithmeticDecoder(this.reader);
        readSegments();
        this.reader = new StreamReader(paramArrayOfByte);
      }
      else
      {
        this.reader.movePointer(-8);
      }
    }
    else
    {
      setFileHeaderFlags();
      if (this.noOfPagesKnown)
        this.noOfPages = getNoOfPages();
    }
    this.huffmanDecoder = new HuffmanDecoder(this.reader);
    this.mmrDecoder = new MMRDecoder(this.reader);
    this.arithmeticDecoder = new ArithmeticDecoder(this.reader);
    readSegments();
  }

  public HuffmanDecoder getHuffmanDecoder()
  {
    return this.huffmanDecoder;
  }

  public MMRDecoder getMMRDecoder()
  {
    return this.mmrDecoder;
  }

  public ArithmeticDecoder getArithmeticDecoder()
  {
    return this.arithmeticDecoder;
  }

  private void resetDecoder()
  {
    this.noOfPagesKnown = false;
    this.randomAccessOrganisation = false;
    this.noOfPages = -1;
    this.segments.clear();
    this.bitmaps.clear();
  }

  private void readSegments()
    throws IOException, JBIG2Exception
  {
    int i = 0;
    Object localObject1;
    Object localObject2;
    while ((this.reader.bytesRemaining()) && (i == 0))
    {
      localObject1 = new SegmentHeader();
      readSegmentHeader((SegmentHeader)localObject1);
      localObject2 = null;
      int j = ((SegmentHeader)localObject1).getSegmentType();
      int[] arrayOfInt = ((SegmentHeader)localObject1).getReferredToSegments();
      int k = ((SegmentHeader)localObject1).getReferredToSegmentCount();
      switch (j)
      {
      case 0:
        localObject2 = new SymbolDictionarySegment(this);
        ((Segment)localObject2).setSegmentHeader((SegmentHeader)localObject1);
        break;
      case 4:
        localObject2 = new TextRegionSegment(this, false);
        ((Segment)localObject2).setSegmentHeader((SegmentHeader)localObject1);
        break;
      case 6:
        localObject2 = new TextRegionSegment(this, true);
        ((Segment)localObject2).setSegmentHeader((SegmentHeader)localObject1);
        break;
      case 7:
        localObject2 = new TextRegionSegment(this, true);
        ((Segment)localObject2).setSegmentHeader((SegmentHeader)localObject1);
        break;
      case 16:
        localObject2 = new PatternDictionarySegment(this);
        ((Segment)localObject2).setSegmentHeader((SegmentHeader)localObject1);
        break;
      case 20:
        localObject2 = new HalftoneRegionSegment(this, false);
        ((Segment)localObject2).setSegmentHeader((SegmentHeader)localObject1);
        break;
      case 22:
        localObject2 = new HalftoneRegionSegment(this, true);
        ((Segment)localObject2).setSegmentHeader((SegmentHeader)localObject1);
        break;
      case 23:
        localObject2 = new HalftoneRegionSegment(this, true);
        ((Segment)localObject2).setSegmentHeader((SegmentHeader)localObject1);
        break;
      case 36:
        localObject2 = new GenericRegionSegment(this, false);
        ((Segment)localObject2).setSegmentHeader((SegmentHeader)localObject1);
        break;
      case 38:
        localObject2 = new GenericRegionSegment(this, true);
        ((Segment)localObject2).setSegmentHeader((SegmentHeader)localObject1);
        break;
      case 39:
        localObject2 = new GenericRegionSegment(this, true);
        ((Segment)localObject2).setSegmentHeader((SegmentHeader)localObject1);
        break;
      case 40:
        localObject2 = new RefinementRegionSegment(this, false, arrayOfInt, k);
        ((Segment)localObject2).setSegmentHeader((SegmentHeader)localObject1);
        break;
      case 42:
        localObject2 = new RefinementRegionSegment(this, true, arrayOfInt, k);
        ((Segment)localObject2).setSegmentHeader((SegmentHeader)localObject1);
        break;
      case 43:
        localObject2 = new RefinementRegionSegment(this, true, arrayOfInt, k);
        ((Segment)localObject2).setSegmentHeader((SegmentHeader)localObject1);
        break;
      case 48:
        localObject2 = new PageInformationSegment(this);
        ((Segment)localObject2).setSegmentHeader((SegmentHeader)localObject1);
        break;
      case 49:
        break;
      case 50:
        localObject2 = new EndOfStripeSegment(this);
        ((Segment)localObject2).setSegmentHeader((SegmentHeader)localObject1);
        break;
      case 51:
        i = 1;
        break;
      case 52:
        break;
      case 53:
        localObject2 = new CodeTableSegment(this);
        ((Segment)localObject2).setSegmentHeader((SegmentHeader)localObject1);
        break;
      case 62:
        localObject2 = new ExtensionSegment(this);
        ((Segment)localObject2).setSegmentHeader((SegmentHeader)localObject1);
        break;
      case 1:
      case 2:
      case 3:
      case 5:
      case 8:
      case 9:
      case 10:
      case 11:
      case 12:
      case 13:
      case 14:
      case 15:
      case 17:
      case 18:
      case 19:
      case 21:
      case 24:
      case 25:
      case 26:
      case 27:
      case 28:
      case 29:
      case 30:
      case 31:
      case 32:
      case 33:
      case 34:
      case 35:
      case 37:
      case 41:
      case 44:
      case 45:
      case 46:
      case 47:
      case 54:
      case 55:
      case 56:
      case 57:
      case 58:
      case 59:
      case 60:
      case 61:
      default:
        System.out.println("Unknown Segment type in JBIG2 stream");
        if (!this.randomAccessOrganisation)
          ((Segment)localObject2).readSegment();
        this.segments.add(localObject2);
      }
    }
    if (this.randomAccessOrganisation)
    {
      localObject1 = this.segments.iterator();
      while (((Iterator)localObject1).hasNext())
      {
        localObject2 = (Segment)((Iterator)localObject1).next();
        ((Segment)localObject2).readSegment();
      }
    }
  }

  public PageInformationSegment findPageSegment(int paramInt)
  {
    Iterator localIterator = this.segments.iterator();
    while (localIterator.hasNext())
    {
      Segment localSegment = (Segment)localIterator.next();
      SegmentHeader localSegmentHeader = localSegment.getSegmentHeader();
      if ((localSegmentHeader.getSegmentType() == 48) && (localSegmentHeader.getPageAssociation() == paramInt))
        return (PageInformationSegment)localSegment;
    }
    return null;
  }

  public Segment findSegment(int paramInt)
  {
    Iterator localIterator = this.segments.iterator();
    while (localIterator.hasNext())
    {
      Segment localSegment = (Segment)localIterator.next();
      if (localSegment.getSegmentHeader().getSegmentNumber() == paramInt)
        return localSegment;
    }
    return null;
  }

  private void readSegmentHeader(SegmentHeader paramSegmentHeader)
    throws JBIG2Exception
  {
    handleSegmentNumber(paramSegmentHeader);
    handleSegmentHeaderFlags(paramSegmentHeader);
    handleSegmentReferredToCountAndRetentionFlags(paramSegmentHeader);
    handleReferredToSegmentNumbers(paramSegmentHeader);
    handlePageAssociation(paramSegmentHeader);
    if (paramSegmentHeader.getSegmentType() != 51)
      handleSegmentDataLength(paramSegmentHeader);
  }

  private void handlePageAssociation(SegmentHeader paramSegmentHeader)
  {
    boolean bool = paramSegmentHeader.isPageAssociationSizeSet();
    int i;
    if (bool)
    {
      short[] arrayOfShort = new short[4];
      this.reader.readByte(arrayOfShort);
      i = BinaryOperation.getInt32(arrayOfShort);
    }
    else
    {
      i = this.reader.readByte();
    }
    paramSegmentHeader.setPageAssociation(i);
  }

  private void handleSegmentNumber(SegmentHeader paramSegmentHeader)
  {
    short[] arrayOfShort = new short[4];
    this.reader.readByte(arrayOfShort);
    int i = BinaryOperation.getInt32(arrayOfShort);
    paramSegmentHeader.setSegmentNumber(i);
  }

  private void handleSegmentHeaderFlags(SegmentHeader paramSegmentHeader)
  {
    short s = this.reader.readByte();
    paramSegmentHeader.setSegmentHeaderFlags(s);
  }

  private void handleSegmentReferredToCountAndRetentionFlags(SegmentHeader paramSegmentHeader)
    throws JBIG2Exception
  {
    int i = this.reader.readByte();
    int j = (i & 0xE0) >> 5;
    int k = (short)(i & 0x1F);
    short[] arrayOfShort1;
    if (j <= 4)
    {
      arrayOfShort1 = new short[1];
      arrayOfShort1[0] = k;
    }
    else if (j == 7)
    {
      short[] arrayOfShort2 = new short[4];
      arrayOfShort2[0] = k;
      for (int m = 1; m < 4; m++)
        arrayOfShort2[m] = this.reader.readByte();
      j = BinaryOperation.getInt32(arrayOfShort2);
      m = (int)Math.ceil(4.0D + (j + 1) / 8.0D);
      int n = m - 4;
      arrayOfShort1 = new short[n];
      this.reader.readByte(arrayOfShort1);
    }
    else
    {
      throw new JBIG2Exception("Error, 3 bit Segment count field = " + j);
    }
    paramSegmentHeader.setReferredToSegmentCount(j);
  }

  private void handleReferredToSegmentNumbers(SegmentHeader paramSegmentHeader)
  {
    int i = paramSegmentHeader.getReferredToSegmentCount();
    int[] arrayOfInt = new int[i];
    int j = paramSegmentHeader.getSegmentNumber();
    if (j <= 256)
    {
      for (int k = 0; k < i; k++)
        arrayOfInt[k] = this.reader.readByte();
    }
    else
    {
      short[] arrayOfShort;
      int m;
      if (j <= 65536)
      {
        arrayOfShort = new short[2];
        for (m = 0; m < i; m++)
        {
          this.reader.readByte(arrayOfShort);
          arrayOfInt[m] = BinaryOperation.getInt16(arrayOfShort);
        }
      }
      else
      {
        arrayOfShort = new short[4];
        for (m = 0; m < i; m++)
        {
          this.reader.readByte(arrayOfShort);
          arrayOfInt[m] = BinaryOperation.getInt32(arrayOfShort);
        }
      }
    }
    paramSegmentHeader.setReferredToSegments(arrayOfInt);
  }

  private int getNoOfPages()
  {
    short[] arrayOfShort = new short[4];
    this.reader.readByte(arrayOfShort);
    return BinaryOperation.getInt32(arrayOfShort);
  }

  private void handleSegmentDataLength(SegmentHeader paramSegmentHeader)
  {
    short[] arrayOfShort = new short[4];
    this.reader.readByte(arrayOfShort);
    int i = BinaryOperation.getInt32(arrayOfShort);
    paramSegmentHeader.setDataLength(i);
  }

  private void setFileHeaderFlags()
  {
    int i = this.reader.readByte();
    if ((i & 0xFC) != 0)
      System.out.println("Warning, reserved bits (2-7) of file header flags are not zero " + i);
    int j = i & 0x1;
    this.randomAccessOrganisation = (j == 0);
    int k = i & 0x2;
    this.noOfPagesKnown = (k == 0);
  }

  private boolean checkHeader()
  {
    short[] arrayOfShort1 = { 151, 74, 66, 50, 13, 10, 26, 10 };
    short[] arrayOfShort2 = new short[8];
    this.reader.readByte(arrayOfShort2);
    return Arrays.equals(arrayOfShort1, arrayOfShort2);
  }

  public int readBits(int paramInt)
  {
    return this.reader.readBits(paramInt);
  }

  public int readBit()
  {
    return this.reader.readBit();
  }

  public void readByte(short[] paramArrayOfShort)
  {
    this.reader.readByte(paramArrayOfShort);
  }

  public void consumeRemainingBits()
  {
    this.reader.consumeRemainingBits();
  }

  public short readByte()
  {
    return this.reader.readByte();
  }

  public void appendBitmap(JBIG2Bitmap paramJBIG2Bitmap)
  {
    this.bitmaps.add(paramJBIG2Bitmap);
  }

  public JBIG2Bitmap findBitmap(int paramInt)
  {
    Iterator localIterator = this.bitmaps.iterator();
    while (localIterator.hasNext())
    {
      JBIG2Bitmap localJBIG2Bitmap = (JBIG2Bitmap)localIterator.next();
      if (localJBIG2Bitmap.getBitmapNumber() == paramInt)
        return localJBIG2Bitmap;
    }
    return null;
  }

  public boolean isNumberOfPagesKnown()
  {
    return this.noOfPagesKnown;
  }

  public int getNumberOfPages()
  {
    return this.noOfPages;
  }

  public ArrayList<Segment> getAllSegments()
  {
    return this.segments;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.jbig2.decoders.JBIG2StreamDecoder
 * JD-Core Version:    0.6.2
 */