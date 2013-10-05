package org.jpedal.objects.acroforms.actions;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.SourceDataLine;
import org.jpedal.utils.LogWriter;

public class SoundHandler
{
  private static final int EXTERNAL_BUFFER_SIZE = 128000;
  private static int frameSize;
  private static float sampleRate = 44100.0F;
  private static int sampleSizeInBits = 16;
  private static int channels = 2;
  private static AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;

  public static void setAudioFormat(int paramInt1, int paramInt2, float paramFloat, int paramInt3)
  {
    sampleSizeInBits = paramInt2;
    sampleRate = paramFloat;
    channels = paramInt3;
    if (paramInt1 != 926832749)
      if (paramInt1 == 1551661165)
        encoding = AudioFormat.Encoding.PCM_UNSIGNED;
      else
        throw new RuntimeException("AudioFormat currently unsupported! - ");
  }

  private static AudioFormat getAudioFormat()
  {
    frameSize = sampleSizeInBits / 8 * channels;
    int i = (int)sampleRate;
    AudioFormat localAudioFormat = new AudioFormat(encoding, sampleRate, sampleSizeInBits, channels, frameSize, i, true);
    return localAudioFormat;
  }

  public static AudioInputStream getAudioInputStream(byte[] paramArrayOfByte)
  {
    AudioFormat localAudioFormat = getAudioFormat();
    long l = paramArrayOfByte.length / frameSize;
    AudioInputStream localAudioInputStream = new AudioInputStream(new ByteArrayInputStream(paramArrayOfByte), localAudioFormat, l);
    return localAudioInputStream;
  }

  public static void PlaySound(byte[] paramArrayOfByte)
    throws Exception
  {
    AudioFormat localAudioFormat = getAudioFormat();
    long l = paramArrayOfByte.length / frameSize;
    AudioInputStream localAudioInputStream = new AudioInputStream(new ByteArrayInputStream(paramArrayOfByte), localAudioFormat, l);
    playSoundFromStream(localAudioInputStream);
  }

  private static void playSoundFromStream(AudioInputStream paramAudioInputStream)
  {
    SourceDataLine localSourceDataLine = null;
    DataLine.Info localInfo = new DataLine.Info(SourceDataLine.class, paramAudioInputStream.getFormat());
    try
    {
      localSourceDataLine = (SourceDataLine)AudioSystem.getLine(localInfo);
      localSourceDataLine.open(paramAudioInputStream.getFormat());
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Exception: " + localException.getMessage());
    }
    localSourceDataLine.start();
    int i = 0;
    byte[] arrayOfByte = new byte[128000];
    while (i != -1)
    {
      try
      {
        i = paramAudioInputStream.read(arrayOfByte, 0, arrayOfByte.length);
      }
      catch (IOException localIOException)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog("Exception: " + localIOException.getMessage());
      }
      if (i >= 0)
        localSourceDataLine.write(arrayOfByte, 0, i);
    }
    localSourceDataLine.drain();
    localSourceDataLine.close();
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.acroforms.actions.SoundHandler
 * JD-Core Version:    0.6.2
 */