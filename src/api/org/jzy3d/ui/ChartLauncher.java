package org.jzy3d.ui;

import java.awt.Container;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jzy3d.chart.Chart;
import org.jzy3d.chart.controllers.mouse.ChartMouseController;
import org.jzy3d.chart.controllers.thread.ChartThreadController;
import org.jzy3d.ui.views.ImagePanel;


public class ChartLauncher {
	public static ChartMouseController openChart(Chart chart){
		return openChart(chart, new Rectangle(0,0,800,600), "Jzy3d", true);
	}
	
	public static ChartMouseController openChart(Chart chart, String title){
		return openChart(chart, new Rectangle(0,0,800,600), title, true);
	}
	
	public static ChartMouseController openChart(Chart chart, Rectangle bounds, String title){
		return openChart(chart, bounds, title, true);
	}
	public static ChartMouseController openChart(Chart chart, Rectangle bounds, String title, boolean allowSlaveThreadOnDoubleClick){
		return openChart(chart, bounds, title, allowSlaveThreadOnDoubleClick, false);
	}
	
	public static ChartMouseController openChart(final Chart chart, Rectangle bounds, final String title, boolean allowSlaveThreadOnDoubleClick, boolean startThreadImmediatly){
		// Setup chart controllers and listeners
		ChartMouseController mouse   = new ChartMouseController();
		/*mouse.addControllerEventListener(new ControllerEventListener(){
			public void controllerEventFired(ControllerEvent e) {
				if(e.getType()==ControllerType.ROTATE){
					//System.out.println("Mouse[ROTATE]:" + (Coord3d)e.getValue());
				}
			}
		});*/
		chart.addController(mouse);
		
		if( allowSlaveThreadOnDoubleClick ){
			ChartThreadController thread = new ChartThreadController();
			mouse.addSlaveThreadController(thread);
			chart.addController(thread);
			if( startThreadImmediatly )
				thread.start();
		}
		
		// trigger screenshot on 's' letter
		chart.getCanvas().addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				switch(e.getKeyChar()){
	    		case 's':
	    			try {
						ChartLauncher.screenshot(chart, "./data/screenshots/"+title+".png");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
	    		default:
	    			break;
	    		}
			}
			@Override
			public void keyReleased(KeyEvent e) {
			}
			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
		
		chart.render();
		
		// Open it in a window
		Plugs.frame(chart, bounds, title);
		//thread.start();
		return mouse;
	}
	
	public static void openStaticChart(Chart chart, Rectangle bounds, String title){
		chart.render();
		Plugs.frame(chart, bounds, title);
	}
	
	
	public static void instructions(){
		System.out.println("Rotate     : Left click and drag mouse");
		System.out.println("Scale      : Roll mouse wheel");
		System.out.println("Z Shift    : Right click and drag mouse");
		System.out.println("Animate    : Double left click");
		System.out.println("Screenshot : Press 's'");
		System.out.println("------------------------------------");
	}
	
	/*******************************************************/
	
	public static void openImagePanel(Image image){
		openImagePanel(image, new Rectangle(0,800,600,600));
	}
	
	public static void openImagePanel(Image image, Rectangle bounds){
		ImagePanel panel = new ImagePanel( image );
		JFrame frame = new JFrame();
		frame.getContentPane().add(panel);
		frame.pack();
		frame.setBounds(bounds);
		frame.setVisible(true);
	}
	
	public static void openPanel(JPanel panel, Rectangle bounds, String title){
		JFrame frame = new JFrame(title);
		Container content = frame.getContentPane();
		//content.setBackground(Color.white);
		content.add(panel);
		frame.pack();
		frame.setVisible(true);
		frame.setBounds(bounds);
		frame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent event) {
			    System.exit(0);
			  }
		});
	}
	
	/*******************************************************/
	
	public static void screenshot(Chart chart, String filename) throws IOException{
		File output = new File(filename);
		if(!output.getParentFile().exists())
			output.mkdirs();
		ImageIO.write(chart.screenshot(), "png", output);
		System.out.println("Dumped screenshot in: " + filename);
	}
}
