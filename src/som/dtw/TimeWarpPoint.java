package som.dtw;

/**
 * @author niktrk
 * 
 */
public class TimeWarpPoint {

	private final int x, y;

	public TimeWarpPoint(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public String toString() {
		return "[" + x + "," + y + "]";
	}

}
