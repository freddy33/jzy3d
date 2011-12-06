package org.jzy3d.trials.g2d;

import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import org.jzy3d.bridge.awt.DoubleBufferedPanelAWT;


public class FrameG2D extends java.awt.Frame{
	public FrameG2D(DoubleBufferedPanelAWT panel, Rectangle bounds, String title){
		this.panel = panel;
		this.setTitle(title + "[AWT]");
		this.add(panel);
		this.pack();
		this.setBounds(bounds);
		this.setVisible(true);
		
		this.addWindowListener(new WindowAdapter() { 
			public void windowClosing(WindowEvent e) {
				FrameG2D.this.remove(FrameG2D.this.panel);
				FrameG2D.this.dispose();
			}
		});
	}
	private DoubleBufferedPanelAWT panel;
	private static final long serialVersionUID = 1L;
}
