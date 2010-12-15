import java.awt.Canvas;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;


import com.flotandroidchart.flot.FlotDraw;
import com.flotandroidchart.global.FlotEvent;
import com.flotandroidchart.global.FlotEventListener;

public class FlotFrame extends Container {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private FlotDraw _fd;

	public FlotFrame(FlotDraw fd) {
		_fd = fd;
		this.setLayout(new GridLayout(1, 1));
		FlotOverlay fol = new FlotOverlay(_fd.getEventHolder());
		add(fol);
	}

	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		_fd.draw(g2, this.getWidth(), this.getHeight(), new Font(
				Font.SANS_SERIF, Font.PLAIN, 12));
	}

}
