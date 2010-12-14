import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;


import com.flotandroidchart.flot.FlotDraw;

public class FlotFrame extends Canvas {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private FlotDraw _fd;

	public FlotFrame(FlotDraw fd) {
		_fd = fd;
	}

	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		_fd.draw(g2, this.getWidth(), this.getHeight(), new Font(
				Font.SANS_SERIF, Font.PLAIN, 12));
	}

}
