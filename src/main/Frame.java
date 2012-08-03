package main;

import java.io.File;
import java.util.ArrayList;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

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
	
	public ArrayList<Frame> getFrames(String fileName, double duration, double overlapping) throws Exception {
		ArrayList<Frame> frames = new ArrayList<Frame>();
		File file = new File(fileName);
		AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
		AudioFormat audioFormat = audioStream.getFormat();
		
		double sampleRate = audioFormat.getSampleRate();
		int dur = (int) Math.round(duration * sampleRate);
		int over = (int) Math.round(overlapping * sampleRate);
		
		byte[] buffer = new byte[(int) (audioFormat.getFrameRate() * audioFormat.getSampleSizeInBits())];
		audioStream.read(buffer, 0, buffer.length);

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
