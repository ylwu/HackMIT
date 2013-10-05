package org.jpedal.sun;

import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

public class PrintfFormat
{
  private ArrayList vFmt = new ArrayList();
  private int cPos = 0;
  private DecimalFormatSymbols dfs = null;

  public PrintfFormat(String paramString)
    throws IllegalArgumentException
  {
    this(Locale.getDefault(), paramString);
  }

  public PrintfFormat(Locale paramLocale, String paramString)
    throws IllegalArgumentException
  {
    this.dfs = new DecimalFormatSymbols(paramLocale);
    String str = nonControl(paramString, 0);
    ConversionSpecification localConversionSpecification;
    if (str != null)
    {
      localConversionSpecification = new ConversionSpecification();
      localConversionSpecification.setLiteral(str);
      this.vFmt.add(localConversionSpecification);
    }
    while ((this.cPos != -1) && (this.cPos < paramString.length()))
    {
      for (int i = this.cPos + 1; i < paramString.length(); i++)
      {
        int j = paramString.charAt(i);
        if ((j == 105) || (j == 100) || (j == 102) || (j == 103) || (j == 71) || (j == 111) || (j == 120) || (j == 88) || (j == 101) || (j == 69) || (j == 99) || (j == 115) || (j == 37))
          break;
      }
      i = Math.min(i + 1, paramString.length());
      localConversionSpecification = new ConversionSpecification(paramString.substring(this.cPos, i));
      this.vFmt.add(localConversionSpecification);
      str = nonControl(paramString, i);
      if (str != null)
      {
        localConversionSpecification = new ConversionSpecification();
        localConversionSpecification.setLiteral(str);
        this.vFmt.add(localConversionSpecification);
      }
    }
  }

  private String nonControl(String paramString, int paramInt)
  {
    this.cPos = paramString.indexOf(37, paramInt);
    if (this.cPos == -1)
      this.cPos = paramString.length();
    return paramString.substring(paramInt, this.cPos);
  }

  public String sprintf(Object[] paramArrayOfObject)
  {
    Iterator localIterator = this.vFmt.iterator();
    int j = 0;
    StringBuilder localStringBuilder = new StringBuilder();
    while (localIterator.hasNext())
    {
      ConversionSpecification localConversionSpecification = (ConversionSpecification)localIterator.next();
      int i = localConversionSpecification.getConversionCharacter();
      if (i == 0)
      {
        localStringBuilder.append(localConversionSpecification.getLiteral());
      }
      else if (i == 37)
      {
        localStringBuilder.append('%');
      }
      else
      {
        if (localConversionSpecification.isPositionalSpecification())
        {
          j = localConversionSpecification.getArgumentPosition() - 1;
          int k;
          if (localConversionSpecification.isPositionalFieldWidth())
          {
            k = localConversionSpecification.getArgumentPositionForFieldWidth() - 1;
            localConversionSpecification.setFieldWidthWithArg(((Integer)paramArrayOfObject[k]).intValue());
          }
          if (localConversionSpecification.isPositionalPrecision())
          {
            k = localConversionSpecification.getArgumentPositionForPrecision() - 1;
            localConversionSpecification.setPrecisionWithArg(((Integer)paramArrayOfObject[k]).intValue());
          }
        }
        else
        {
          if (localConversionSpecification.isVariableFieldWidth())
          {
            localConversionSpecification.setFieldWidthWithArg(((Integer)paramArrayOfObject[j]).intValue());
            j++;
          }
          if (localConversionSpecification.isVariablePrecision())
          {
            localConversionSpecification.setPrecisionWithArg(((Integer)paramArrayOfObject[j]).intValue());
            j++;
          }
        }
        if ((paramArrayOfObject[j] instanceof Byte))
          localStringBuilder.append(localConversionSpecification.internalsprintf(((Byte)paramArrayOfObject[j]).byteValue()));
        else if ((paramArrayOfObject[j] instanceof Short))
          localStringBuilder.append(localConversionSpecification.internalsprintf(((Short)paramArrayOfObject[j]).shortValue()));
        else if ((paramArrayOfObject[j] instanceof Integer))
          localStringBuilder.append(localConversionSpecification.internalsprintf(((Integer)paramArrayOfObject[j]).intValue()));
        else if ((paramArrayOfObject[j] instanceof Long))
          localStringBuilder.append(localConversionSpecification.internalsprintf(((Long)paramArrayOfObject[j]).longValue()));
        else if ((paramArrayOfObject[j] instanceof Float))
          localStringBuilder.append(localConversionSpecification.internalsprintf(((Float)paramArrayOfObject[j]).floatValue()));
        else if ((paramArrayOfObject[j] instanceof Double))
          localStringBuilder.append(localConversionSpecification.internalsprintf(((Double)paramArrayOfObject[j]).doubleValue()));
        else if ((paramArrayOfObject[j] instanceof Character))
          localStringBuilder.append(localConversionSpecification.internalsprintf(((Character)paramArrayOfObject[j]).charValue()));
        else if ((paramArrayOfObject[j] instanceof String))
          localStringBuilder.append(localConversionSpecification.internalsprintf((String)paramArrayOfObject[j]));
        else
          localStringBuilder.append(localConversionSpecification.internalsprintf(paramArrayOfObject[j]));
        if (!localConversionSpecification.isPositionalSpecification())
          j++;
      }
    }
    return localStringBuilder.toString();
  }

  public String sprintf()
  {
    Iterator localIterator = this.vFmt.iterator();
    StringBuilder localStringBuilder = new StringBuilder();
    while (localIterator.hasNext())
    {
      ConversionSpecification localConversionSpecification = (ConversionSpecification)localIterator.next();
      int i = localConversionSpecification.getConversionCharacter();
      if (i == 0)
        localStringBuilder.append(localConversionSpecification.getLiteral());
      else if (i == 37)
        localStringBuilder.append('%');
    }
    return localStringBuilder.toString();
  }

  public String sprintf(int paramInt)
    throws IllegalArgumentException
  {
    Iterator localIterator = this.vFmt.iterator();
    StringBuilder localStringBuilder = new StringBuilder();
    while (localIterator.hasNext())
    {
      ConversionSpecification localConversionSpecification = (ConversionSpecification)localIterator.next();
      int i = localConversionSpecification.getConversionCharacter();
      if (i == 0)
        localStringBuilder.append(localConversionSpecification.getLiteral());
      else if (i == 37)
        localStringBuilder.append('%');
      else
        localStringBuilder.append(localConversionSpecification.internalsprintf(paramInt));
    }
    return localStringBuilder.toString();
  }

  public String sprintf(long paramLong)
    throws IllegalArgumentException
  {
    Iterator localIterator = this.vFmt.iterator();
    StringBuilder localStringBuilder = new StringBuilder();
    while (localIterator.hasNext())
    {
      ConversionSpecification localConversionSpecification = (ConversionSpecification)localIterator.next();
      int i = localConversionSpecification.getConversionCharacter();
      if (i == 0)
        localStringBuilder.append(localConversionSpecification.getLiteral());
      else if (i == 37)
        localStringBuilder.append('%');
      else
        localStringBuilder.append(localConversionSpecification.internalsprintf(paramLong));
    }
    return localStringBuilder.toString();
  }

  public String sprintf(double paramDouble)
    throws IllegalArgumentException
  {
    Iterator localIterator = this.vFmt.iterator();
    StringBuilder localStringBuilder = new StringBuilder();
    while (localIterator.hasNext())
    {
      ConversionSpecification localConversionSpecification = (ConversionSpecification)localIterator.next();
      int i = localConversionSpecification.getConversionCharacter();
      if (i == 0)
        localStringBuilder.append(localConversionSpecification.getLiteral());
      else if (i == 37)
        localStringBuilder.append('%');
      else
        localStringBuilder.append(localConversionSpecification.internalsprintf(paramDouble));
    }
    return localStringBuilder.toString();
  }

  public String sprintf(String paramString)
    throws IllegalArgumentException
  {
    Iterator localIterator = this.vFmt.iterator();
    StringBuilder localStringBuilder = new StringBuilder();
    while (localIterator.hasNext())
    {
      ConversionSpecification localConversionSpecification = (ConversionSpecification)localIterator.next();
      int i = localConversionSpecification.getConversionCharacter();
      if (i == 0)
        localStringBuilder.append(localConversionSpecification.getLiteral());
      else if (i == 37)
        localStringBuilder.append('%');
      else
        localStringBuilder.append(localConversionSpecification.internalsprintf(paramString));
    }
    return localStringBuilder.toString();
  }

  public String sprintf(Object paramObject)
    throws IllegalArgumentException
  {
    Iterator localIterator = this.vFmt.iterator();
    StringBuilder localStringBuilder = new StringBuilder();
    while (localIterator.hasNext())
    {
      ConversionSpecification localConversionSpecification = (ConversionSpecification)localIterator.next();
      int i = localConversionSpecification.getConversionCharacter();
      if (i == 0)
        localStringBuilder.append(localConversionSpecification.getLiteral());
      else if (i == 37)
        localStringBuilder.append('%');
      else if ((paramObject instanceof Byte))
        localStringBuilder.append(localConversionSpecification.internalsprintf(((Byte)paramObject).byteValue()));
      else if ((paramObject instanceof Short))
        localStringBuilder.append(localConversionSpecification.internalsprintf(((Short)paramObject).shortValue()));
      else if ((paramObject instanceof Integer))
        localStringBuilder.append(localConversionSpecification.internalsprintf(((Integer)paramObject).intValue()));
      else if ((paramObject instanceof Long))
        localStringBuilder.append(localConversionSpecification.internalsprintf(((Long)paramObject).longValue()));
      else if ((paramObject instanceof Float))
        localStringBuilder.append(localConversionSpecification.internalsprintf(((Float)paramObject).floatValue()));
      else if ((paramObject instanceof Double))
        localStringBuilder.append(localConversionSpecification.internalsprintf(((Double)paramObject).doubleValue()));
      else if ((paramObject instanceof Character))
        localStringBuilder.append(localConversionSpecification.internalsprintf(((Character)paramObject).charValue()));
      else if ((paramObject instanceof String))
        localStringBuilder.append(localConversionSpecification.internalsprintf((String)paramObject));
      else
        localStringBuilder.append(localConversionSpecification.internalsprintf(paramObject));
    }
    return localStringBuilder.toString();
  }

  private class ConversionSpecification
  {
    private boolean thousands = false;
    private boolean leftJustify = false;
    private boolean leadingSign = false;
    private boolean leadingSpace = false;
    private boolean alternateForm = false;
    private boolean leadingZeros = false;
    private boolean variableFieldWidth = false;
    private int fieldWidth = 0;
    private boolean fieldWidthSet = false;
    private int precision = 0;
    private static final int defaultDigits = 6;
    private boolean variablePrecision = false;
    private boolean precisionSet = false;
    private boolean positionalSpecification = false;
    private int argumentPosition = 0;
    private boolean positionalFieldWidth = false;
    private int argumentPositionForFieldWidth = 0;
    private boolean positionalPrecision = false;
    private int argumentPositionForPrecision = 0;
    private boolean optionalh = false;
    private boolean optionall = false;
    private boolean optionalL = false;
    private char conversionCharacter = '\000';
    private int pos = 0;
    private String fmt;

    ConversionSpecification()
    {
    }

    ConversionSpecification(String arg2)
      throws IllegalArgumentException
    {
      String str;
      if (str == null)
        throw new NullPointerException();
      if (str.isEmpty())
        throw new IllegalArgumentException("Control strings must have positive lengths.");
      if (str.charAt(0) == '%')
      {
        this.fmt = str;
        this.pos = 1;
        setArgPosition();
        setFlagCharacters();
        setFieldWidth();
        setPrecision();
        setOptionalHL();
        if (setConversionCharacter())
        {
          if (this.pos == str.length())
          {
            if ((this.leadingZeros) && (this.leftJustify))
              this.leadingZeros = false;
            if ((this.precisionSet) && (this.leadingZeros) && ((this.conversionCharacter == 'd') || (this.conversionCharacter == 'i') || (this.conversionCharacter == 'o') || (this.conversionCharacter == 'x')))
              this.leadingZeros = false;
          }
          else
          {
            throw new IllegalArgumentException("Malformed conversion specification=" + str);
          }
        }
        else
          throw new IllegalArgumentException("Malformed conversion specification=" + str);
      }
      else
      {
        throw new IllegalArgumentException("Control strings must begin with %.");
      }
    }

    void setLiteral(String paramString)
    {
      this.fmt = paramString;
    }

    String getLiteral()
    {
      return this.fmt;
    }

    char getConversionCharacter()
    {
      return this.conversionCharacter;
    }

    boolean isVariableFieldWidth()
    {
      return this.variableFieldWidth;
    }

    void setFieldWidthWithArg(int paramInt)
    {
      if (paramInt < 0)
        this.leftJustify = true;
      this.fieldWidthSet = true;
      this.fieldWidth = Math.abs(paramInt);
    }

    boolean isVariablePrecision()
    {
      return this.variablePrecision;
    }

    void setPrecisionWithArg(int paramInt)
    {
      this.precisionSet = true;
      this.precision = Math.max(paramInt, 0);
    }

    String internalsprintf(int paramInt)
      throws IllegalArgumentException
    {
      String str;
      switch (this.conversionCharacter)
      {
      case 'd':
      case 'i':
        if (this.optionalh)
          str = printDFormat((short)paramInt);
        else if (this.optionall)
          str = printDFormat(paramInt);
        else
          str = printDFormat(paramInt);
        break;
      case 'X':
      case 'x':
        if (this.optionalh)
          str = printXFormat((short)paramInt);
        else if (this.optionall)
          str = printXFormat(paramInt);
        else
          str = printXFormat(paramInt);
        break;
      case 'o':
        if (this.optionalh)
          str = printOFormat((short)paramInt);
        else if (this.optionall)
          str = printOFormat(paramInt);
        else
          str = printOFormat(paramInt);
        break;
      case 'C':
      case 'c':
        str = printCFormat((char)paramInt);
        break;
      default:
        throw new IllegalArgumentException("Cannot format a int with a format using a " + this.conversionCharacter + " conversion character.");
      }
      return str;
    }

    String internalsprintf(long paramLong)
      throws IllegalArgumentException
    {
      String str;
      switch (this.conversionCharacter)
      {
      case 'd':
      case 'i':
        if (this.optionalh)
          str = printDFormat((short)(int)paramLong);
        else if (this.optionall)
          str = printDFormat(paramLong);
        else
          str = printDFormat((int)paramLong);
        break;
      case 'X':
      case 'x':
        if (this.optionalh)
          str = printXFormat((short)(int)paramLong);
        else if (this.optionall)
          str = printXFormat(paramLong);
        else
          str = printXFormat((int)paramLong);
        break;
      case 'o':
        if (this.optionalh)
          str = printOFormat((short)(int)paramLong);
        else if (this.optionall)
          str = printOFormat(paramLong);
        else
          str = printOFormat((int)paramLong);
        break;
      case 'C':
      case 'c':
        str = printCFormat((char)(int)paramLong);
        break;
      default:
        throw new IllegalArgumentException("Cannot format a long with a format using a " + this.conversionCharacter + " conversion character.");
      }
      return str;
    }

    String internalsprintf(double paramDouble)
      throws IllegalArgumentException
    {
      String str;
      switch (this.conversionCharacter)
      {
      case 'f':
        str = printFFormat(paramDouble);
        break;
      case 'E':
      case 'e':
        str = printEFormat(paramDouble);
        break;
      case 'G':
      case 'g':
        str = printGFormat(paramDouble);
        break;
      default:
        throw new IllegalArgumentException("Cannot format a double with a format using a " + this.conversionCharacter + " conversion character.");
      }
      return str;
    }

    String internalsprintf(String paramString)
      throws IllegalArgumentException
    {
      String str;
      if ((this.conversionCharacter == 's') || (this.conversionCharacter == 'S'))
        str = printSFormat(paramString);
      else
        throw new IllegalArgumentException("Cannot format a String with a format using a " + this.conversionCharacter + " conversion character.");
      return str;
    }

    String internalsprintf(Object paramObject)
    {
      String str;
      if ((this.conversionCharacter == 's') || (this.conversionCharacter == 'S'))
        str = printSFormat(paramObject.toString());
      else
        throw new IllegalArgumentException("Cannot format a String with a format using a " + this.conversionCharacter + " conversion character.");
      return str;
    }

    private char[] fFormatDigits(double paramDouble)
    {
      int i1 = 0;
      int i2 = 0;
      String str;
      if (paramDouble > 0.0D)
      {
        str = Double.toString(paramDouble);
      }
      else if (paramDouble < 0.0D)
      {
        str = Double.toString(-paramDouble);
        i2 = 1;
      }
      else
      {
        str = Double.toString(paramDouble);
        if (str.charAt(0) == '-')
        {
          i2 = 1;
          str = str.substring(1);
        }
      }
      int i3 = str.indexOf(69);
      int i4 = str.indexOf(46);
      int m;
      if (i4 != -1)
        m = i4;
      else if (i3 != -1)
        m = i3;
      else
        m = str.length();
      int n;
      if (i4 != -1)
      {
        if (i3 != -1)
          n = i3 - i4 - 1;
        else
          n = str.length() - i4 - 1;
      }
      else
        n = 0;
      int i5;
      if (i3 != -1)
      {
        i5 = i3 + 1;
        i1 = 0;
        if (str.charAt(i5) == '-')
        {
          i5++;
          while ((i5 < str.length()) && (str.charAt(i5) == '0'))
            i5++;
          if (i5 < str.length())
            i1 = -Integer.parseInt(str.substring(i5));
        }
        else
        {
          if (str.charAt(i5) == '+')
            i5++;
          while ((i5 < str.length()) && (str.charAt(i5) == '0'))
            i5++;
          if (i5 < str.length())
            i1 = Integer.parseInt(str.substring(i5));
        }
      }
      if (this.precisionSet)
        i5 = this.precision;
      else
        i5 = 5;
      char[] arrayOfChar1 = str.toCharArray();
      char[] arrayOfChar2 = new char[m + n];
      for (int j = 0; j < m; j++)
        arrayOfChar2[j] = arrayOfChar1[j];
      int i = j + 1;
      for (int k = 0; k < n; k++)
      {
        arrayOfChar2[j] = arrayOfChar1[i];
        j++;
        i++;
      }
      if (m + i1 <= 0)
      {
        arrayOfChar3 = new char[-i1 + n];
        j = 0;
        k = 0;
        while (k < -m - i1)
        {
          arrayOfChar3[j] = '0';
          k++;
          j++;
        }
        i = 0;
        while (i < m + n)
        {
          arrayOfChar3[j] = arrayOfChar2[i];
          i++;
          j++;
        }
      }
      char[] arrayOfChar3 = arrayOfChar2;
      boolean bool = false;
      if (i5 < -i1 + n)
      {
        if (i1 < 0)
          i = i5;
        else
          i = i5 + m;
        bool = checkForCarry(arrayOfChar3, i);
        if (bool)
          bool = startSymbolicCarry(arrayOfChar3, i - 1, 0);
      }
      char[] arrayOfChar4;
      if (m + i1 <= 0)
      {
        arrayOfChar4 = new char[2 + i5];
        if (!bool)
          arrayOfChar4[0] = '0';
        else
          arrayOfChar4[0] = '1';
        if ((this.alternateForm) || (!this.precisionSet) || (this.precision != 0))
        {
          arrayOfChar4[1] = '.';
          i = 0;
          for (j = 2; i < Math.min(i5, arrayOfChar3.length); j++)
          {
            arrayOfChar4[j] = arrayOfChar3[i];
            i++;
          }
          while (j < arrayOfChar4.length)
          {
            arrayOfChar4[j] = '0';
            j++;
          }
        }
      }
      else
      {
        if (!bool)
        {
          if ((this.alternateForm) || (!this.precisionSet) || (this.precision != 0))
            arrayOfChar4 = new char[m + i1 + i5 + 1];
          else
            arrayOfChar4 = new char[m + i1];
          j = 0;
        }
        else
        {
          if ((this.alternateForm) || (!this.precisionSet) || (this.precision != 0))
            arrayOfChar4 = new char[m + i1 + i5 + 2];
          else
            arrayOfChar4 = new char[m + i1 + 1];
          arrayOfChar4[0] = '1';
          j = 1;
        }
        i = 0;
        while (i < Math.min(m + i1, arrayOfChar3.length))
        {
          arrayOfChar4[j] = arrayOfChar3[i];
          i++;
          j++;
        }
        while (i < m + i1)
        {
          arrayOfChar4[j] = '0';
          i++;
          j++;
        }
        if ((this.alternateForm) || (!this.precisionSet) || (this.precision != 0))
        {
          arrayOfChar4[j] = '.';
          j++;
          for (k = 0; (i < arrayOfChar3.length) && (k < i5); k++)
          {
            arrayOfChar4[j] = arrayOfChar3[i];
            i++;
            j++;
          }
          while (j < arrayOfChar4.length)
          {
            arrayOfChar4[j] = '0';
            j++;
          }
        }
      }
      int i6 = 0;
      if ((!this.leftJustify) && (this.leadingZeros))
      {
        i7 = 0;
        if (this.thousands)
        {
          i8 = 0;
          if ((arrayOfChar4[0] == '+') || (arrayOfChar4[0] == '-') || (arrayOfChar4[0] == ' '))
            i8 = 1;
          for (i9 = i8; (i9 < arrayOfChar4.length) && (arrayOfChar4[i9] != '.'); i9++);
          i7 = (i9 - i8) / 3;
        }
        if (this.fieldWidthSet)
          i6 = this.fieldWidth - arrayOfChar4.length;
        if (((i2 == 0) && ((this.leadingSign) || (this.leadingSpace))) || (i2 != 0))
          i6--;
        i6 -= i7;
        if (i6 < 0)
          i6 = 0;
      }
      j = 0;
      char[] arrayOfChar5;
      if (((i2 == 0) && ((this.leadingSign) || (this.leadingSpace))) || (i2 != 0))
      {
        arrayOfChar5 = new char[arrayOfChar4.length + i6 + 1];
        j++;
      }
      else
      {
        arrayOfChar5 = new char[arrayOfChar4.length + i6];
      }
      if (i2 == 0)
      {
        if (this.leadingSign)
          arrayOfChar5[0] = '+';
        if (this.leadingSpace)
          arrayOfChar5[0] = ' ';
      }
      else
      {
        arrayOfChar5[0] = '-';
      }
      i = 0;
      while (i < i6)
      {
        arrayOfChar5[j] = '0';
        i++;
        j++;
      }
      i = 0;
      while (i < arrayOfChar4.length)
      {
        arrayOfChar5[j] = arrayOfChar4[i];
        i++;
        j++;
      }
      int i7 = 0;
      if ((arrayOfChar5[0] == '+') || (arrayOfChar5[0] == '-') || (arrayOfChar5[0] == ' '))
        i7 = 1;
      for (int i8 = i7; (i8 < arrayOfChar5.length) && (arrayOfChar5[i8] != '.'); i8++);
      int i9 = (i8 - i7) / 3;
      if (i8 < arrayOfChar5.length)
        arrayOfChar5[i8] = PrintfFormat.this.dfs.getDecimalSeparator();
      char[] arrayOfChar6 = arrayOfChar5;
      if ((this.thousands) && (i9 > 0))
      {
        arrayOfChar6 = new char[arrayOfChar5.length + i9 + i7];
        arrayOfChar6[0] = arrayOfChar5[0];
        i = i7;
        k = i7;
        while (i < i8)
        {
          if ((i > 0) && ((i8 - i) % 3 == 0))
          {
            arrayOfChar6[k] = PrintfFormat.this.dfs.getGroupingSeparator();
            arrayOfChar6[(k + 1)] = arrayOfChar5[i];
            k += 2;
          }
          else
          {
            arrayOfChar6[k] = arrayOfChar5[i];
            k++;
          }
          i++;
        }
        while (i < arrayOfChar5.length)
        {
          arrayOfChar6[k] = arrayOfChar5[i];
          i++;
          k++;
        }
      }
      return arrayOfChar6;
    }

    private String fFormatString(double paramDouble)
    {
      char[] arrayOfChar1;
      if (Double.isInfinite(paramDouble))
      {
        if (paramDouble == (1.0D / 0.0D))
        {
          if (this.leadingSign)
            arrayOfChar1 = "+Inf".toCharArray();
          else if (this.leadingSpace)
            arrayOfChar1 = " Inf".toCharArray();
          else
            arrayOfChar1 = "Inf".toCharArray();
        }
        else
          arrayOfChar1 = "-Inf".toCharArray();
      }
      else if (Double.isNaN(paramDouble))
      {
        if (this.leadingSign)
          arrayOfChar1 = "+NaN".toCharArray();
        else if (this.leadingSpace)
          arrayOfChar1 = " NaN".toCharArray();
        else
          arrayOfChar1 = "NaN".toCharArray();
      }
      else
        arrayOfChar1 = fFormatDigits(paramDouble);
      char[] arrayOfChar2 = applyFloatPadding(arrayOfChar1, false);
      return new String(arrayOfChar2);
    }

    private char[] eFormatDigits(double paramDouble, char paramChar)
    {
      int n = 0;
      int i4 = 0;
      String str;
      if (paramDouble > 0.0D)
      {
        str = Double.toString(paramDouble);
      }
      else if (paramDouble < 0.0D)
      {
        str = Double.toString(-paramDouble);
        i4 = 1;
      }
      else
      {
        str = Double.toString(paramDouble);
        if (str.charAt(0) == '-')
        {
          i4 = 1;
          str = str.substring(1);
        }
      }
      int i1 = str.indexOf(69);
      if (i1 == -1)
        i1 = str.indexOf(101);
      int i2 = str.indexOf(46);
      if (i1 != -1)
      {
        int i5 = i1 + 1;
        n = 0;
        if (str.charAt(i5) == '-')
        {
          i5++;
          while ((i5 < str.length()) && (str.charAt(i5) == '0'))
            i5++;
          if (i5 < str.length())
            n = -Integer.parseInt(str.substring(i5));
        }
        else
        {
          if (str.charAt(i5) == '+')
            i5++;
          while ((i5 < str.length()) && (str.charAt(i5) == '0'))
            i5++;
          if (i5 < str.length())
            n = Integer.parseInt(str.substring(i5));
        }
      }
      if (i2 != -1)
        n += i2 - 1;
      int m;
      if (this.precisionSet)
        m = this.precision;
      else
        m = 5;
      Object localObject;
      if ((i2 != -1) && (i1 != -1))
        localObject = (str.substring(0, i2) + str.substring(i2 + 1, i1)).toCharArray();
      else if (i2 != -1)
        localObject = (str.substring(0, i2) + str.substring(i2 + 1)).toCharArray();
      else if (i1 != -1)
        localObject = str.substring(0, i1).toCharArray();
      else
        localObject = str.toCharArray();
      int i6;
      if (localObject[0] != '0')
        i6 = 0;
      else
        for (i6 = 0; (i6 < localObject.length) && (localObject[i6] == '0'); i6++);
      char[] arrayOfChar1;
      if (i6 + m < localObject.length - 1)
      {
        boolean bool = checkForCarry((char[])localObject, i6 + m + 1);
        if (bool)
          bool = startSymbolicCarry((char[])localObject, i6 + m, i6);
        if (bool)
        {
          arrayOfChar1 = new char[i6 + m + 1];
          arrayOfChar1[i6] = '1';
          for (j = 0; j < i6; j++)
            arrayOfChar1[j] = '0';
          i = i6;
          for (j = i6 + 1; j < m + 1; j++)
          {
            arrayOfChar1[j] = localObject[i];
            i++;
          }
          n++;
          localObject = arrayOfChar1;
        }
      }
      int i3;
      if ((Math.abs(n) < 100) && (!this.optionalL))
        i3 = 4;
      else
        i3 = 5;
      if ((this.alternateForm) || (!this.precisionSet) || (this.precision != 0))
        arrayOfChar1 = new char[2 + m + i3];
      else
        arrayOfChar1 = new char[1 + i3];
      if (localObject[0] != '0')
      {
        arrayOfChar1[0] = localObject[0];
        j = 1;
      }
      else
      {
        for (j = 1; j < (i1 == -1 ? localObject.length : i1); j++)
          if (localObject[j] != '0')
            break;
        if (((i1 != -1) && (j < i1)) || ((i1 == -1) && (j < localObject.length)))
        {
          arrayOfChar1[0] = localObject[j];
          n -= j;
          j++;
        }
        else
        {
          arrayOfChar1[0] = '0';
          j = 2;
        }
      }
      if ((this.alternateForm) || (!this.precisionSet) || (this.precision != 0))
      {
        arrayOfChar1[1] = '.';
        i = 2;
      }
      else
      {
        i = 1;
      }
      for (int k = 0; (k < m) && (j < localObject.length); k++)
      {
        arrayOfChar1[i] = localObject[j];
        j++;
        i++;
      }
      while (i < arrayOfChar1.length - i3)
      {
        arrayOfChar1[i] = '0';
        i++;
      }
      arrayOfChar1[(i++)] = paramChar;
      if (n < 0)
        arrayOfChar1[(i++)] = '-';
      else
        arrayOfChar1[(i++)] = '+';
      n = Math.abs(n);
      if (n >= 100)
      {
        switch (n / 100)
        {
        case 1:
          arrayOfChar1[i] = '1';
          break;
        case 2:
          arrayOfChar1[i] = '2';
          break;
        case 3:
          arrayOfChar1[i] = '3';
          break;
        case 4:
          arrayOfChar1[i] = '4';
          break;
        case 5:
          arrayOfChar1[i] = '5';
          break;
        case 6:
          arrayOfChar1[i] = '6';
          break;
        case 7:
          arrayOfChar1[i] = '7';
          break;
        case 8:
          arrayOfChar1[i] = '8';
          break;
        case 9:
          arrayOfChar1[i] = '9';
        }
        i++;
      }
      switch (n % 100 / 10)
      {
      case 0:
        arrayOfChar1[i] = '0';
        break;
      case 1:
        arrayOfChar1[i] = '1';
        break;
      case 2:
        arrayOfChar1[i] = '2';
        break;
      case 3:
        arrayOfChar1[i] = '3';
        break;
      case 4:
        arrayOfChar1[i] = '4';
        break;
      case 5:
        arrayOfChar1[i] = '5';
        break;
      case 6:
        arrayOfChar1[i] = '6';
        break;
      case 7:
        arrayOfChar1[i] = '7';
        break;
      case 8:
        arrayOfChar1[i] = '8';
        break;
      case 9:
        arrayOfChar1[i] = '9';
      }
      i++;
      switch (n % 10)
      {
      case 0:
        arrayOfChar1[i] = '0';
        break;
      case 1:
        arrayOfChar1[i] = '1';
        break;
      case 2:
        arrayOfChar1[i] = '2';
        break;
      case 3:
        arrayOfChar1[i] = '3';
        break;
      case 4:
        arrayOfChar1[i] = '4';
        break;
      case 5:
        arrayOfChar1[i] = '5';
        break;
      case 6:
        arrayOfChar1[i] = '6';
        break;
      case 7:
        arrayOfChar1[i] = '7';
        break;
      case 8:
        arrayOfChar1[i] = '8';
        break;
      case 9:
        arrayOfChar1[i] = '9';
      }
      int i7 = 0;
      if ((!this.leftJustify) && (this.leadingZeros))
      {
        i8 = 0;
        if (this.thousands)
        {
          i9 = 0;
          if ((arrayOfChar1[0] == '+') || (arrayOfChar1[0] == '-') || (arrayOfChar1[0] == ' '))
            i9 = 1;
          for (i10 = i9; (i10 < arrayOfChar1.length) && (arrayOfChar1[i10] != '.'); i10++);
          i8 = (i10 - i9) / 3;
        }
        if (this.fieldWidthSet)
          i7 = this.fieldWidth - arrayOfChar1.length;
        if (((i4 == 0) && ((this.leadingSign) || (this.leadingSpace))) || (i4 != 0))
          i7--;
        i7 -= i8;
        if (i7 < 0)
          i7 = 0;
      }
      int j = 0;
      char[] arrayOfChar2;
      if (((i4 == 0) && ((this.leadingSign) || (this.leadingSpace))) || (i4 != 0))
      {
        arrayOfChar2 = new char[arrayOfChar1.length + i7 + 1];
        j++;
      }
      else
      {
        arrayOfChar2 = new char[arrayOfChar1.length + i7];
      }
      if (i4 == 0)
      {
        if (this.leadingSign)
          arrayOfChar2[0] = '+';
        if (this.leadingSpace)
          arrayOfChar2[0] = ' ';
      }
      else
      {
        arrayOfChar2[0] = '-';
      }
      for (k = 0; k < i7; k++)
      {
        arrayOfChar2[j] = '0';
        j++;
      }
      int i = 0;
      while ((i < arrayOfChar1.length) && (j < arrayOfChar2.length))
      {
        arrayOfChar2[j] = arrayOfChar1[i];
        i++;
        j++;
      }
      int i8 = 0;
      if ((arrayOfChar2[0] == '+') || (arrayOfChar2[0] == '-') || (arrayOfChar2[0] == ' '))
        i8 = 1;
      for (int i9 = i8; (i9 < arrayOfChar2.length) && (arrayOfChar2[i9] != '.'); i9++);
      int i10 = i9 / 3;
      if (i9 < arrayOfChar2.length)
        arrayOfChar2[i9] = PrintfFormat.this.dfs.getDecimalSeparator();
      char[] arrayOfChar3 = arrayOfChar2;
      if ((this.thousands) && (i10 > 0))
      {
        arrayOfChar3 = new char[arrayOfChar2.length + i10 + i8];
        arrayOfChar3[0] = arrayOfChar2[0];
        i = i8;
        k = i8;
        while (i < i9)
        {
          if ((i > 0) && ((i9 - i) % 3 == 0))
          {
            arrayOfChar3[k] = PrintfFormat.this.dfs.getGroupingSeparator();
            arrayOfChar3[(k + 1)] = arrayOfChar2[i];
            k += 2;
          }
          else
          {
            arrayOfChar3[k] = arrayOfChar2[i];
            k++;
          }
          i++;
        }
        while (i < arrayOfChar2.length)
        {
          arrayOfChar3[k] = arrayOfChar2[i];
          i++;
          k++;
        }
      }
      return arrayOfChar3;
    }

    private boolean checkForCarry(char[] paramArrayOfChar, int paramInt)
    {
      boolean bool = false;
      if (paramInt < paramArrayOfChar.length)
        if ((paramArrayOfChar[paramInt] == '6') || (paramArrayOfChar[paramInt] == '7') || (paramArrayOfChar[paramInt] == '8') || (paramArrayOfChar[paramInt] == '9'))
        {
          bool = true;
        }
        else if (paramArrayOfChar[paramInt] == '5')
        {
          for (int i = paramInt + 1; (i < paramArrayOfChar.length) && (paramArrayOfChar[i] == '0'); i++);
          bool = i < paramArrayOfChar.length;
          if ((!bool) && (paramInt > 0))
            bool = (paramArrayOfChar[(paramInt - 1)] == '1') || (paramArrayOfChar[(paramInt - 1)] == '3') || (paramArrayOfChar[(paramInt - 1)] == '5') || (paramArrayOfChar[(paramInt - 1)] == '7') || (paramArrayOfChar[(paramInt - 1)] == '9');
        }
      return bool;
    }

    private boolean startSymbolicCarry(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    {
      boolean bool = true;
      for (int i = paramInt1; (bool) && (i >= paramInt2); i--)
      {
        bool = false;
        switch (paramArrayOfChar[i])
        {
        case '0':
          paramArrayOfChar[i] = '1';
          break;
        case '1':
          paramArrayOfChar[i] = '2';
          break;
        case '2':
          paramArrayOfChar[i] = '3';
          break;
        case '3':
          paramArrayOfChar[i] = '4';
          break;
        case '4':
          paramArrayOfChar[i] = '5';
          break;
        case '5':
          paramArrayOfChar[i] = '6';
          break;
        case '6':
          paramArrayOfChar[i] = '7';
          break;
        case '7':
          paramArrayOfChar[i] = '8';
          break;
        case '8':
          paramArrayOfChar[i] = '9';
          break;
        case '9':
          paramArrayOfChar[i] = '0';
          bool = true;
        }
      }
      return bool;
    }

    private String eFormatString(double paramDouble, char paramChar)
    {
      char[] arrayOfChar1;
      if (Double.isInfinite(paramDouble))
      {
        if (paramDouble == (1.0D / 0.0D))
        {
          if (this.leadingSign)
            arrayOfChar1 = "+Inf".toCharArray();
          else if (this.leadingSpace)
            arrayOfChar1 = " Inf".toCharArray();
          else
            arrayOfChar1 = "Inf".toCharArray();
        }
        else
          arrayOfChar1 = "-Inf".toCharArray();
      }
      else if (Double.isNaN(paramDouble))
      {
        if (this.leadingSign)
          arrayOfChar1 = "+NaN".toCharArray();
        else if (this.leadingSpace)
          arrayOfChar1 = " NaN".toCharArray();
        else
          arrayOfChar1 = "NaN".toCharArray();
      }
      else
        arrayOfChar1 = eFormatDigits(paramDouble, paramChar);
      char[] arrayOfChar2 = applyFloatPadding(arrayOfChar1, false);
      return new String(arrayOfChar2);
    }

    private char[] applyFloatPadding(char[] paramArrayOfChar, boolean paramBoolean)
    {
      char[] arrayOfChar = paramArrayOfChar;
      if (this.fieldWidthSet)
      {
        int k;
        int i;
        int j;
        if (this.leftJustify)
        {
          k = this.fieldWidth - paramArrayOfChar.length;
          if (k > 0)
          {
            arrayOfChar = new char[paramArrayOfChar.length + k];
            for (i = 0; i < paramArrayOfChar.length; i++)
              arrayOfChar[i] = paramArrayOfChar[i];
            j = 0;
            while (j < k)
            {
              arrayOfChar[i] = ' ';
              j++;
              i++;
            }
          }
        }
        else if ((!this.leadingZeros) || (paramBoolean))
        {
          k = this.fieldWidth - paramArrayOfChar.length;
          if (k > 0)
          {
            arrayOfChar = new char[paramArrayOfChar.length + k];
            for (i = 0; i < k; i++)
              arrayOfChar[i] = ' ';
            j = 0;
          }
        }
        else
        {
          while (j < paramArrayOfChar.length)
          {
            arrayOfChar[i] = paramArrayOfChar[j];
            i++;
            j++;
            continue;
            if (this.leadingZeros)
            {
              k = this.fieldWidth - paramArrayOfChar.length;
              if (k > 0)
              {
                arrayOfChar = new char[paramArrayOfChar.length + k];
                i = 0;
                j = 0;
                if (paramArrayOfChar[0] == '-')
                {
                  arrayOfChar[0] = '-';
                  i++;
                  j++;
                }
                for (int m = 0; m < k; m++)
                {
                  arrayOfChar[i] = '0';
                  i++;
                }
                while (j < paramArrayOfChar.length)
                {
                  arrayOfChar[i] = paramArrayOfChar[j];
                  i++;
                  j++;
                }
              }
            }
          }
        }
      }
      return arrayOfChar;
    }

    private String printFFormat(double paramDouble)
    {
      return fFormatString(paramDouble);
    }

    private String printEFormat(double paramDouble)
    {
      if (this.conversionCharacter == 'e')
        return eFormatString(paramDouble, 'e');
      return eFormatString(paramDouble, 'E');
    }

    private String printGFormat(double paramDouble)
    {
      int i = this.precision;
      char[] arrayOfChar1;
      if (Double.isInfinite(paramDouble))
      {
        if (paramDouble == (1.0D / 0.0D))
        {
          if (this.leadingSign)
            arrayOfChar1 = "+Inf".toCharArray();
          else if (this.leadingSpace)
            arrayOfChar1 = " Inf".toCharArray();
          else
            arrayOfChar1 = "Inf".toCharArray();
        }
        else
          arrayOfChar1 = "-Inf".toCharArray();
      }
      else if (Double.isNaN(paramDouble))
      {
        if (this.leadingSign)
          arrayOfChar1 = "+NaN".toCharArray();
        else if (this.leadingSpace)
          arrayOfChar1 = " NaN".toCharArray();
        else
          arrayOfChar1 = "NaN".toCharArray();
      }
      else
      {
        if (!this.precisionSet)
          this.precision = 6;
        if (this.precision == 0)
          this.precision = 1;
        String str1;
        int k;
        if (this.conversionCharacter == 'g')
        {
          str1 = eFormatString(paramDouble, 'e').trim();
          k = str1.indexOf(101);
        }
        else
        {
          str1 = eFormatString(paramDouble, 'E').trim();
          k = str1.indexOf(69);
        }
        int j = k + 1;
        int m = 0;
        if (str1.charAt(j) == '-')
        {
          j++;
          while ((j < str1.length()) && (str1.charAt(j) == '0'))
            j++;
          if (j < str1.length())
            m = -Integer.parseInt(str1.substring(j));
        }
        else
        {
          if (str1.charAt(j) == '+')
            j++;
          while ((j < str1.length()) && (str1.charAt(j) == '0'))
            j++;
          if (j < str1.length())
            m = Integer.parseInt(str1.substring(j));
        }
        String str4;
        if (!this.alternateForm)
        {
          String str2;
          if ((m >= -4) && (m < this.precision))
            str2 = fFormatString(paramDouble).trim();
          else
            str2 = str1.substring(0, k);
          for (j = str2.length() - 1; (j >= 0) && (str2.charAt(j) == '0'); j--);
          if ((j >= 0) && (str2.charAt(j) == '.'))
            j--;
          String str3;
          if (j == -1)
            str3 = "0";
          else if (!Character.isDigit(str2.charAt(j)))
            str3 = str2.substring(0, j + 1) + '0';
          else
            str3 = str2.substring(0, j + 1);
          if ((m >= -4) && (m < this.precision))
            str4 = str3;
          else
            str4 = str3 + str1.substring(k);
        }
        else if ((m >= -4) && (m < this.precision))
        {
          str4 = fFormatString(paramDouble).trim();
        }
        else
        {
          str4 = str1;
        }
        if ((this.leadingSpace) && (paramDouble >= 0.0D))
          str4 = ' ' + str4;
        arrayOfChar1 = str4.toCharArray();
      }
      char[] arrayOfChar2 = applyFloatPadding(arrayOfChar1, false);
      this.precision = i;
      return new String(arrayOfChar2);
    }

    private String printDFormat(short paramShort)
    {
      return printDFormat(Short.toString(paramShort));
    }

    private String printDFormat(long paramLong)
    {
      return printDFormat(Long.toString(paramLong));
    }

    private String printDFormat(int paramInt)
    {
      return printDFormat(Integer.toString(paramInt));
    }

    private String printDFormat(String paramString)
    {
      int i = 0;
      int j = 0;
      int k = 0;
      int m = 0;
      int i1 = paramString.charAt(0) == '-' ? 1 : 0;
      if ((paramString.equals("0")) && (this.precisionSet) && (this.precision == 0))
        paramString = "";
      if (i1 == 0)
      {
        if ((this.precisionSet) && (paramString.length() < this.precision))
          i = this.precision - paramString.length();
      }
      else if ((this.precisionSet) && (paramString.length() - 1 < this.precision))
        i = this.precision - paramString.length() + 1;
      if (i < 0)
        i = 0;
      if (this.fieldWidthSet)
      {
        j = this.fieldWidth - i - paramString.length();
        if ((i1 == 0) && ((this.leadingSign) || (this.leadingSpace)))
          j--;
      }
      if (j < 0)
        j = 0;
      if (this.leadingSign)
        k++;
      else if (this.leadingSpace)
        k++;
      k += j;
      k += i;
      k += paramString.length();
      char[] arrayOfChar1 = new char[k];
      int n;
      int i3;
      if (this.leftJustify)
      {
        if (i1 != 0)
          arrayOfChar1[(m++)] = '-';
        else if (this.leadingSign)
          arrayOfChar1[(m++)] = '+';
        else if (this.leadingSpace)
          arrayOfChar1[(m++)] = ' ';
        char[] arrayOfChar2 = paramString.toCharArray();
        n = i1 != 0 ? 1 : 0;
        for (i3 = 0; i3 < i; i3++)
        {
          arrayOfChar1[m] = '0';
          m++;
        }
        i3 = n;
        while (i3 < arrayOfChar2.length)
        {
          arrayOfChar1[m] = arrayOfChar2[i3];
          i3++;
          m++;
        }
        for (i3 = 0; i3 < j; i3++)
        {
          arrayOfChar1[m] = ' ';
          m++;
        }
      }
      else
      {
        if (!this.leadingZeros)
        {
          for (m = 0; m < j; m++)
            arrayOfChar1[m] = ' ';
          if (i1 != 0)
            arrayOfChar1[(m++)] = '-';
          else if (this.leadingSign)
            arrayOfChar1[(m++)] = '+';
          else if (this.leadingSpace)
            arrayOfChar1[(m++)] = ' ';
        }
        else
        {
          if (i1 != 0)
            arrayOfChar1[(m++)] = '-';
          else if (this.leadingSign)
            arrayOfChar1[(m++)] = '+';
          else if (this.leadingSpace)
            arrayOfChar1[(m++)] = ' ';
          i2 = 0;
          while (i2 < j)
          {
            arrayOfChar1[m] = '0';
            i2++;
            m++;
          }
        }
        int i2 = 0;
        while (i2 < i)
        {
          arrayOfChar1[m] = '0';
          i2++;
          m++;
        }
        char[] arrayOfChar3 = paramString.toCharArray();
        n = i1 != 0 ? 1 : 0;
        i3 = n;
        while (i3 < arrayOfChar3.length)
        {
          arrayOfChar1[m] = arrayOfChar3[i3];
          i3++;
          m++;
        }
      }
      return new String(arrayOfChar1);
    }

    private String printXFormat(short paramShort)
    {
      String str1 = null;
      if (paramShort == -32768)
      {
        str1 = "8000";
      }
      else if (paramShort < 0)
      {
        String str2;
        if (paramShort == -32768)
        {
          str2 = "0";
        }
        else
        {
          str2 = Integer.toString(-paramShort - 1 ^ 0xFFFFFFFF ^ 0xFFFF8000, 16);
          if ((str2.charAt(0) == 'F') || (str2.charAt(0) == 'f'))
            str2 = str2.substring(16, 32);
        }
        switch (str2.length())
        {
        case 1:
          str1 = "800" + str2;
          break;
        case 2:
          str1 = "80" + str2;
          break;
        case 3:
          str1 = '8' + str2;
          break;
        case 4:
          switch (str2.charAt(0))
          {
          case '1':
            str1 = '9' + str2.substring(1, 4);
            break;
          case '2':
            str1 = 'a' + str2.substring(1, 4);
            break;
          case '3':
            str1 = 'b' + str2.substring(1, 4);
            break;
          case '4':
            str1 = 'c' + str2.substring(1, 4);
            break;
          case '5':
            str1 = 'd' + str2.substring(1, 4);
            break;
          case '6':
            str1 = 'e' + str2.substring(1, 4);
            break;
          case '7':
            str1 = 'f' + str2.substring(1, 4);
          }
          break;
        }
      }
      else
      {
        str1 = Integer.toString(paramShort, 16);
      }
      return printXFormat(str1);
    }

    private String printXFormat(long paramLong)
    {
      String str1 = null;
      if (paramLong == -9223372036854775808L)
      {
        str1 = "8000000000000000";
      }
      else if (paramLong < 0L)
      {
        String str2 = Long.toString(-paramLong - 1L ^ 0xFFFFFFFF ^ 0x0, 16);
        switch (str2.length())
        {
        case 1:
          str1 = "800000000000000" + str2;
          break;
        case 2:
          str1 = "80000000000000" + str2;
          break;
        case 3:
          str1 = "8000000000000" + str2;
          break;
        case 4:
          str1 = "800000000000" + str2;
          break;
        case 5:
          str1 = "80000000000" + str2;
          break;
        case 6:
          str1 = "8000000000" + str2;
          break;
        case 7:
          str1 = "800000000" + str2;
          break;
        case 8:
          str1 = "80000000" + str2;
          break;
        case 9:
          str1 = "8000000" + str2;
          break;
        case 10:
          str1 = "800000" + str2;
          break;
        case 11:
          str1 = "80000" + str2;
          break;
        case 12:
          str1 = "8000" + str2;
          break;
        case 13:
          str1 = "800" + str2;
          break;
        case 14:
          str1 = "80" + str2;
          break;
        case 15:
          str1 = '8' + str2;
          break;
        case 16:
          switch (str2.charAt(0))
          {
          case '1':
            str1 = '9' + str2.substring(1, 16);
            break;
          case '2':
            str1 = 'a' + str2.substring(1, 16);
            break;
          case '3':
            str1 = 'b' + str2.substring(1, 16);
            break;
          case '4':
            str1 = 'c' + str2.substring(1, 16);
            break;
          case '5':
            str1 = 'd' + str2.substring(1, 16);
            break;
          case '6':
            str1 = 'e' + str2.substring(1, 16);
            break;
          case '7':
            str1 = 'f' + str2.substring(1, 16);
          }
          break;
        }
      }
      else
      {
        str1 = Long.toString(paramLong, 16);
      }
      return printXFormat(str1);
    }

    private String printXFormat(int paramInt)
    {
      String str1 = null;
      if (paramInt == -2147483648)
      {
        str1 = "80000000";
      }
      else if (paramInt < 0)
      {
        String str2 = Integer.toString(-paramInt - 1 ^ 0xFFFFFFFF ^ 0x80000000, 16);
        switch (str2.length())
        {
        case 1:
          str1 = "8000000" + str2;
          break;
        case 2:
          str1 = "800000" + str2;
          break;
        case 3:
          str1 = "80000" + str2;
          break;
        case 4:
          str1 = "8000" + str2;
          break;
        case 5:
          str1 = "800" + str2;
          break;
        case 6:
          str1 = "80" + str2;
          break;
        case 7:
          str1 = '8' + str2;
          break;
        case 8:
          switch (str2.charAt(0))
          {
          case '1':
            str1 = '9' + str2.substring(1, 8);
            break;
          case '2':
            str1 = 'a' + str2.substring(1, 8);
            break;
          case '3':
            str1 = 'b' + str2.substring(1, 8);
            break;
          case '4':
            str1 = 'c' + str2.substring(1, 8);
            break;
          case '5':
            str1 = 'd' + str2.substring(1, 8);
            break;
          case '6':
            str1 = 'e' + str2.substring(1, 8);
            break;
          case '7':
            str1 = 'f' + str2.substring(1, 8);
          }
          break;
        }
      }
      else
      {
        str1 = Integer.toString(paramInt, 16);
      }
      return printXFormat(str1);
    }

    private String printXFormat(String paramString)
    {
      int i = 0;
      int j = 0;
      if ((paramString.equals("0")) && (this.precisionSet) && (this.precision == 0))
        paramString = "";
      if (this.precisionSet)
        i = this.precision - paramString.length();
      if (i < 0)
        i = 0;
      if (this.fieldWidthSet)
      {
        j = this.fieldWidth - i - paramString.length();
        if (this.alternateForm)
          j -= 2;
      }
      if (j < 0)
        j = 0;
      int k = 0;
      if (this.alternateForm)
        k += 2;
      k += i;
      k += paramString.length();
      k += j;
      char[] arrayOfChar1 = new char[k];
      int m = 0;
      int i2;
      if (this.leftJustify)
      {
        if (this.alternateForm)
        {
          arrayOfChar1[(m++)] = '0';
          arrayOfChar1[(m++)] = 'x';
        }
        int n = 0;
        while (n < i)
        {
          arrayOfChar1[m] = '0';
          n++;
          m++;
        }
        char[] arrayOfChar2 = paramString.toCharArray();
        i2 = 0;
        while (i2 < arrayOfChar2.length)
        {
          arrayOfChar1[m] = arrayOfChar2[i2];
          i2++;
          m++;
        }
        i2 = 0;
        while (i2 < j)
        {
          arrayOfChar1[m] = ' ';
          i2++;
          m++;
        }
      }
      else
      {
        if (!this.leadingZeros)
        {
          i1 = 0;
          while (i1 < j)
          {
            arrayOfChar1[m] = ' ';
            i1++;
            m++;
          }
        }
        if (this.alternateForm)
        {
          arrayOfChar1[(m++)] = '0';
          arrayOfChar1[(m++)] = 'x';
        }
        if (this.leadingZeros)
        {
          i1 = 0;
          while (i1 < j)
          {
            arrayOfChar1[m] = '0';
            i1++;
            m++;
          }
        }
        int i1 = 0;
        while (i1 < i)
        {
          arrayOfChar1[m] = '0';
          i1++;
          m++;
        }
        localObject = paramString.toCharArray();
        i2 = 0;
        while (i2 < localObject.length)
        {
          arrayOfChar1[m] = localObject[i2];
          i2++;
          m++;
        }
      }
      Object localObject = new String(arrayOfChar1);
      if (this.conversionCharacter == 'X')
        localObject = ((String)localObject).toUpperCase();
      return localObject;
    }

    private String printOFormat(short paramShort)
    {
      String str1 = null;
      if (paramShort == -32768)
      {
        str1 = "100000";
      }
      else if (paramShort < 0)
      {
        String str2 = Integer.toString(-paramShort - 1 ^ 0xFFFFFFFF ^ 0xFFFF8000, 8);
        switch (str2.length())
        {
        case 1:
          str1 = "10000" + str2;
          break;
        case 2:
          str1 = "1000" + str2;
          break;
        case 3:
          str1 = "100" + str2;
          break;
        case 4:
          str1 = "10" + str2;
          break;
        case 5:
          str1 = '1' + str2;
        }
      }
      else
      {
        str1 = Integer.toString(paramShort, 8);
      }
      return printOFormat(str1);
    }

    private String printOFormat(long paramLong)
    {
      String str1 = null;
      if (paramLong == -9223372036854775808L)
      {
        str1 = "1000000000000000000000";
      }
      else if (paramLong < 0L)
      {
        String str2 = Long.toString(-paramLong - 1L ^ 0xFFFFFFFF ^ 0x0, 8);
        switch (str2.length())
        {
        case 1:
          str1 = "100000000000000000000" + str2;
          break;
        case 2:
          str1 = "10000000000000000000" + str2;
          break;
        case 3:
          str1 = "1000000000000000000" + str2;
          break;
        case 4:
          str1 = "100000000000000000" + str2;
          break;
        case 5:
          str1 = "10000000000000000" + str2;
          break;
        case 6:
          str1 = "1000000000000000" + str2;
          break;
        case 7:
          str1 = "100000000000000" + str2;
          break;
        case 8:
          str1 = "10000000000000" + str2;
          break;
        case 9:
          str1 = "1000000000000" + str2;
          break;
        case 10:
          str1 = "100000000000" + str2;
          break;
        case 11:
          str1 = "10000000000" + str2;
          break;
        case 12:
          str1 = "1000000000" + str2;
          break;
        case 13:
          str1 = "100000000" + str2;
          break;
        case 14:
          str1 = "10000000" + str2;
          break;
        case 15:
          str1 = "1000000" + str2;
          break;
        case 16:
          str1 = "100000" + str2;
          break;
        case 17:
          str1 = "10000" + str2;
          break;
        case 18:
          str1 = "1000" + str2;
          break;
        case 19:
          str1 = "100" + str2;
          break;
        case 20:
          str1 = "10" + str2;
          break;
        case 21:
          str1 = '1' + str2;
        }
      }
      else
      {
        str1 = Long.toString(paramLong, 8);
      }
      return printOFormat(str1);
    }

    private String printOFormat(int paramInt)
    {
      String str1 = null;
      if (paramInt == -2147483648)
      {
        str1 = "20000000000";
      }
      else if (paramInt < 0)
      {
        String str2 = Integer.toString(-paramInt - 1 ^ 0xFFFFFFFF ^ 0x80000000, 8);
        switch (str2.length())
        {
        case 1:
          str1 = "2000000000" + str2;
          break;
        case 2:
          str1 = "200000000" + str2;
          break;
        case 3:
          str1 = "20000000" + str2;
          break;
        case 4:
          str1 = "2000000" + str2;
          break;
        case 5:
          str1 = "200000" + str2;
          break;
        case 6:
          str1 = "20000" + str2;
          break;
        case 7:
          str1 = "2000" + str2;
          break;
        case 8:
          str1 = "200" + str2;
          break;
        case 9:
          str1 = "20" + str2;
          break;
        case 10:
          str1 = '2' + str2;
          break;
        case 11:
          str1 = '3' + str2.substring(1);
        }
      }
      else
      {
        str1 = Integer.toString(paramInt, 8);
      }
      return printOFormat(str1);
    }

    private String printOFormat(String paramString)
    {
      int i = 0;
      int j = 0;
      if ((paramString.equals("0")) && (this.precisionSet) && (this.precision == 0))
        paramString = "";
      if (this.precisionSet)
        i = this.precision - paramString.length();
      if (this.alternateForm)
        i++;
      if (i < 0)
        i = 0;
      if (this.fieldWidthSet)
        j = this.fieldWidth - i - paramString.length();
      if (j < 0)
        j = 0;
      int k = i + paramString.length() + j;
      char[] arrayOfChar1 = new char[k];
      int m;
      int i1;
      if (this.leftJustify)
      {
        for (m = 0; m < i; m++)
          arrayOfChar1[m] = '0';
        char[] arrayOfChar2 = paramString.toCharArray();
        i1 = 0;
        while (i1 < arrayOfChar2.length)
        {
          arrayOfChar1[m] = arrayOfChar2[i1];
          i1++;
          m++;
        }
        i1 = 0;
        while (i1 < j)
        {
          arrayOfChar1[m] = ' ';
          i1++;
          m++;
        }
      }
      else
      {
        if (this.leadingZeros)
          for (m = 0; m < j; m++)
            arrayOfChar1[m] = '0';
        for (m = 0; m < j; m++)
          arrayOfChar1[m] = ' ';
        int n = 0;
        while (n < i)
        {
          arrayOfChar1[m] = '0';
          n++;
          m++;
        }
        char[] arrayOfChar3 = paramString.toCharArray();
        i1 = 0;
        while (i1 < arrayOfChar3.length)
        {
          arrayOfChar1[m] = arrayOfChar3[i1];
          i1++;
          m++;
        }
      }
      return new String(arrayOfChar1);
    }

    private String printCFormat(char paramChar)
    {
      int i = 1;
      int j = this.fieldWidth;
      if (!this.fieldWidthSet)
        j = i;
      char[] arrayOfChar = new char[j];
      if (this.leftJustify)
      {
        arrayOfChar[0] = paramChar;
        for (k = 1; k <= j - i; k++)
          arrayOfChar[k] = ' ';
      }
      for (int k = 0; k < j - i; k++)
        arrayOfChar[k] = ' ';
      arrayOfChar[k] = paramChar;
      return new String(arrayOfChar);
    }

    private String printSFormat(String paramString)
    {
      int i = paramString.length();
      int j = this.fieldWidth;
      if ((this.precisionSet) && (i > this.precision))
        i = this.precision;
      if (!this.fieldWidthSet)
        j = i;
      int k = 0;
      if (j > i)
        k += j - i;
      if (i >= paramString.length())
        k += paramString.length();
      else
        k += i;
      char[] arrayOfChar1 = new char[k];
      int m;
      if (this.leftJustify)
      {
        char[] arrayOfChar2;
        if (i >= paramString.length())
        {
          arrayOfChar2 = paramString.toCharArray();
          for (m = 0; m < paramString.length(); m++)
            arrayOfChar1[m] = arrayOfChar2[m];
        }
        else
        {
          arrayOfChar2 = paramString.substring(0, i).toCharArray();
          for (m = 0; m < i; m++)
            arrayOfChar1[m] = arrayOfChar2[m];
        }
        int n = 0;
        while (n < j - i)
        {
          arrayOfChar1[m] = ' ';
          n++;
          m++;
        }
      }
      else
      {
        for (m = 0; m < j - i; m++)
          arrayOfChar1[m] = ' ';
        char[] arrayOfChar3;
        int i1;
        if (i >= paramString.length())
        {
          arrayOfChar3 = paramString.toCharArray();
          for (i1 = 0; i1 < paramString.length(); i1++)
          {
            arrayOfChar1[m] = arrayOfChar3[i1];
            m++;
          }
        }
        else
        {
          arrayOfChar3 = paramString.substring(0, i).toCharArray();
          for (i1 = 0; i1 < i; i1++)
          {
            arrayOfChar1[m] = arrayOfChar3[i1];
            m++;
          }
        }
      }
      return new String(arrayOfChar1);
    }

    private boolean setConversionCharacter()
    {
      boolean bool = false;
      this.conversionCharacter = '\000';
      if (this.pos < this.fmt.length())
      {
        char c = this.fmt.charAt(this.pos);
        if ((c == 'i') || (c == 'd') || (c == 'f') || (c == 'g') || (c == 'G') || (c == 'o') || (c == 'x') || (c == 'X') || (c == 'e') || (c == 'E') || (c == 'c') || (c == 's') || (c == '%'))
        {
          this.conversionCharacter = c;
          this.pos += 1;
          bool = true;
        }
      }
      return bool;
    }

    private void setOptionalHL()
    {
      this.optionalh = false;
      this.optionall = false;
      this.optionalL = false;
      if (this.pos < this.fmt.length())
      {
        int i = this.fmt.charAt(this.pos);
        if (i == 104)
        {
          this.optionalh = true;
          this.pos += 1;
        }
        else if (i == 108)
        {
          this.optionall = true;
          this.pos += 1;
        }
        else if (i == 76)
        {
          this.optionalL = true;
          this.pos += 1;
        }
      }
    }

    private void setPrecision()
    {
      int i = this.pos;
      this.precisionSet = false;
      if ((this.pos < this.fmt.length()) && (this.fmt.charAt(this.pos) == '.'))
      {
        this.pos += 1;
        if ((this.pos < this.fmt.length()) && (this.fmt.charAt(this.pos) == '*'))
        {
          this.pos += 1;
          if (!setPrecisionArgPosition())
          {
            this.variablePrecision = true;
            this.precisionSet = true;
          }
          return;
        }
        while (this.pos < this.fmt.length())
        {
          char c = this.fmt.charAt(this.pos);
          if (!Character.isDigit(c))
            break;
          this.pos += 1;
        }
        if (this.pos > i + 1)
        {
          String str = this.fmt.substring(i + 1, this.pos);
          this.precision = Integer.parseInt(str);
          this.precisionSet = true;
        }
      }
    }

    private void setFieldWidth()
    {
      int i = this.pos;
      this.fieldWidth = 0;
      this.fieldWidthSet = false;
      if ((this.pos < this.fmt.length()) && (this.fmt.charAt(this.pos) == '*'))
      {
        this.pos += 1;
        if (!setFieldWidthArgPosition())
        {
          this.variableFieldWidth = true;
          this.fieldWidthSet = true;
        }
      }
      else
      {
        while (this.pos < this.fmt.length())
        {
          char c = this.fmt.charAt(this.pos);
          if (!Character.isDigit(c))
            break;
          this.pos += 1;
        }
        if ((i < this.pos) && (i < this.fmt.length()))
        {
          String str = this.fmt.substring(i, this.pos);
          this.fieldWidth = Integer.parseInt(str);
          this.fieldWidthSet = true;
        }
      }
    }

    private void setArgPosition()
    {
      for (int i = this.pos; (i < this.fmt.length()) && (Character.isDigit(this.fmt.charAt(i))); i++);
      if ((i > this.pos) && (i < this.fmt.length()) && (this.fmt.charAt(i) == '$'))
      {
        this.positionalSpecification = true;
        this.argumentPosition = Integer.parseInt(this.fmt.substring(this.pos, i));
        this.pos = (i + 1);
      }
    }

    private boolean setFieldWidthArgPosition()
    {
      boolean bool = false;
      for (int i = this.pos; (i < this.fmt.length()) && (Character.isDigit(this.fmt.charAt(i))); i++);
      if ((i > this.pos) && (i < this.fmt.length()) && (this.fmt.charAt(i) == '$'))
      {
        this.positionalFieldWidth = true;
        this.argumentPositionForFieldWidth = Integer.parseInt(this.fmt.substring(this.pos, i));
        this.pos = (i + 1);
        bool = true;
      }
      return bool;
    }

    private boolean setPrecisionArgPosition()
    {
      boolean bool = false;
      for (int i = this.pos; (i < this.fmt.length()) && (Character.isDigit(this.fmt.charAt(i))); i++);
      if ((i > this.pos) && (i < this.fmt.length()) && (this.fmt.charAt(i) == '$'))
      {
        this.positionalPrecision = true;
        this.argumentPositionForPrecision = Integer.parseInt(this.fmt.substring(this.pos, i));
        this.pos = (i + 1);
        bool = true;
      }
      return bool;
    }

    boolean isPositionalSpecification()
    {
      return this.positionalSpecification;
    }

    int getArgumentPosition()
    {
      return this.argumentPosition;
    }

    boolean isPositionalFieldWidth()
    {
      return this.positionalFieldWidth;
    }

    int getArgumentPositionForFieldWidth()
    {
      return this.argumentPositionForFieldWidth;
    }

    boolean isPositionalPrecision()
    {
      return this.positionalPrecision;
    }

    int getArgumentPositionForPrecision()
    {
      return this.argumentPositionForPrecision;
    }

    private void setFlagCharacters()
    {
      this.thousands = false;
      this.leftJustify = false;
      this.leadingSign = false;
      this.leadingSpace = false;
      this.alternateForm = false;
      this.leadingZeros = false;
      while (this.pos < this.fmt.length())
      {
        int i = this.fmt.charAt(this.pos);
        if (i == 39)
        {
          this.thousands = true;
        }
        else if (i == 45)
        {
          this.leftJustify = true;
          this.leadingZeros = false;
        }
        else if (i == 43)
        {
          this.leadingSign = true;
          this.leadingSpace = false;
        }
        else if (i == 32)
        {
          if (!this.leadingSign)
            this.leadingSpace = true;
        }
        else if (i == 35)
        {
          this.alternateForm = true;
        }
        else
        {
          if (i != 48)
            break;
          if (!this.leftJustify)
            this.leadingZeros = true;
        }
        this.pos += 1;
      }
    }
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.sun.PrintfFormat
 * JD-Core Version:    0.6.2
 */