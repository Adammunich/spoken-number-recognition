package main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Frame {
	private AudioFormat format;
	private byte[] buffer;

	public Frame(byte[] buffer, AudioFormat format) {
		this.format = format;
		this.buffer = buffer;
	}

	public AudioFormat getFormat() {
		return format;
	}

	public void setFormat(AudioFormat format) {
		this.format = format;
	}

	public byte[] getBuffer() {
		return buffer;
	}

	public void setBuffer(byte[] buffer) {
		this.buffer = buffer;
	}
	
	public ArrayList<Frame> getFrames(String fileName, double duration, double overlapping) {
		ArrayList<Frame> frames = new ArrayList<Frame>();
		File file = new File(fileName);
		AudioInputStream audioStream = null;
		try {
			audioStream = AudioSystem.getAudioInputStream(file);
		} catch (UnsupportedAudioFileException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		AudioFormat audioFormat = audioStream.getFormat();
		
		double sampleRate = audioFormat.getSampleRate();
		int dur = (int) Math.round(duration * sampleRate);
		int over = (int) Math.round(overlapping * sampleRate);
		
		byte[] buffer = new byte[(int) (audioFormat.getFrameRate() * audioFormat.getSampleSizeInBits())];
		try {
			audioStream.read(buffer, 0, buffer.length);
		} catch (IOException e) {
			e.printStackTrace();
		}

		for(int curr = 0; curr <= buffer.length; curr += dur) {
			if (curr - over >= 0){
				curr -= over;
			}
			Frame frame = new Frame(getSubarray(buffer, curr, dur), audioFormat);
			System.out.println(curr + "  - " + (curr + dur));
			frames.add(frame);
		}

		return frames;
	}
	
	private byte[] getSubarray(byte[] array, int start, int length) {
		byte[] ret = new byte[length];
		for (int i = 0; i < length; i++) {
			if(array.length > start + i)
				ret[i] = array[start + i];
		}
		return ret;
	}

	public void window() {
		for(int i = 0; i < buffer.length; i++){
			buffer[i] *= 0.54 - 0.46 * Math.cos(2 * Math.PI * i / (buffer.length - 1) );
		}
	}
	
	

}
