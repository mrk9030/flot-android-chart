import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import com.flotandroidchart.global.EventHolder;
import com.flotandroidchart.global.FlotEvent;
import com.flotandroidchart.global.FlotEventListener;


public class FlotOverlay extends Component {
	public EventHolder eventHolder;
	public FlotOverlay(EventHolder evt) {
		eventHolder = evt;
		eventHolder.addEventListener(new FlotEventListener(){

			@Override
			public String Name() {
				// TODO Auto-generated method stub
				return FlotEvent.CANVAS_REPAINT;
			}

			@Override
			public void execute(FlotEvent event) {
				// TODO Auto-generated method stub
				repaint();
			}
			
		});

		this.addMouseMotionListener(new MouseMotionListener(){

			@Override
			public void mouseDragged(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseMoved(MouseEvent arg0) {
				// TODO Auto-generated method stub
				eventHolder.dispatchEvent(FlotEvent.MOUSE_HOVER, new FlotEvent(arg0));
			}
			
		});
	}
	
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
	}
}
