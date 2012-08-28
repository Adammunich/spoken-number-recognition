package dsp;

import java.util.ArrayList;
import java.util.List;

import main.Constants;
import dsp.endpointdetection.EnergyEndPointDetection;
import dsp.endpointdetection.ThresholdEndPointDetection;
import dsp.model.Frame;
import dsp.transformation.DCT;
import dsp.transformation.DeltaFeatures;
import dsp.transformation.FFT;
import dsp.transformation.HammingWindow;
import dsp.transformation.MelFilter;
import dsp.transformation.PreEmphasisFilter;
import dsp.transformation.SubRange;
import dsp.transformation.Transformation;
import dsp.transformation.WeightedMFCC;
import dsp.util.AudioFileUtil;

/**
 * @author niktrk
 * 
 */
public class SignalProcessor {

	private static List<Transformation> firstPart;
	private static List<Transformation> secondPart;

	{
		firstPart = new ArrayList<>();
		firstPart.add(new PreEmphasisFilter());
		firstPart.add(new HammingWindow());
		firstPart.add(new FFT());
		secondPart = new ArrayList<>();
		secondPart.add(new MelFilter(Constants.MEL_NUMBER_OF_FILTERS));
		secondPart.add(new DCT());
		secondPart.add(new SubRange(1, 1 + Constants.MFCC_LENGTH));
		secondPart.add(new DeltaFeatures(2));
		secondPart.add(new WeightedMFCC());
	}

	public static double[] process(byte[] input) {
		List<Frame> frames = Frame.getFramesFromByteArray(input, Constants.FRAME_SAMPLE_LENGTH, 0.5);
		for (Frame frame : frames) {
			for (Transformation transformation : firstPart) {
				frame.applyTransformation(transformation);
			}
		}
		ThresholdEndPointDetection endPointDetection = new EnergyEndPointDetection(frames);
		List<Frame> removeStartAndEnd = endPointDetection.removeStartAndEnd();
		double[] result = new double[removeStartAndEnd.size() * Constants.MFCC_LENGTH];
		int k = 0;
		for (Frame frame : removeStartAndEnd) {
			for (Transformation transformation : secondPart) {
				frame.applyTransformation(transformation);
			}
			System.arraycopy(frame.getBuffer(), 0, result, k * Constants.MFCC_LENGTH, Constants.MFCC_LENGTH);
			k++;
		}
		return result;
	}

	public static double[] process(String inputFilename) {
		return process(AudioFileUtil.readFromFileToByteArray(inputFilename));
	}

}
