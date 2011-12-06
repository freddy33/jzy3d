package org.jzy3d.demos.overlay;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jzy3d.chart.Chart;
import org.jzy3d.chart.controllers.ControllerType;
import org.jzy3d.chart.controllers.mouse.ChartMouseController;
import org.jzy3d.chart.controllers.thread.ChartThreadController;
import org.jzy3d.colors.Color;
import org.jzy3d.events.ControllerEvent;
import org.jzy3d.events.ControllerEventListener;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.Scatter;
import org.jzy3d.plot3d.rendering.view.Renderer2d;


/**
 * Example showing the use of 2D renders via Overlay.
 * <p/>
 * Draws a fixed rectangle over a scatter plot.
 *
 * @author Rajarshi Guha
 */
public class OverlayDemo extends JFrame {
   private static final long serialVersionUID = -6593841806164643667L;
	int npt = 500;
    Coord3d[] points;
    Color[] colors;
    Chart chart;
    Scatter dots;

    private ChartMouseController mouseMotion;

    public static void main(String[] args) {
        OverlayDemo gt = new OverlayDemo();
        gt.setVisible(true);
    }

    public OverlayDemo() {
        super("Overlay Demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        generateData();
        chart = new Chart("swing");
        chart.getScene().add(dots);
        chart.addRenderer(new Renderer2d() {
            public void paint(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setStroke(new BasicStroke(4.0f));
                g2d.setColor(java.awt.Color.BLACK);
                g2d.drawRect(10, 50, 100, 100);
            }
        });
        
        mouseMotion = new ChartMouseController();
        ChartThreadController thread = new ChartThreadController();
        mouseMotion.addSlaveThreadController(thread);
        mouseMotion.addControllerEventListener(new ControllerEventListener() {
            public void controllerEventFired(ControllerEvent e) {
                if (e.getType() == ControllerType.ROTATE) {
                }
            }
        });
        chart.addController(mouseMotion);
        chart.addController(thread);        

        JPanel panel = new JPanel(new BorderLayout());
        panel.add((Component) chart.getCanvas(), BorderLayout.CENTER);
        setContentPane(panel);

        pack();
        setBounds(0, 0, 400, 400);
        
        chart.getView().getCamera().setScreenGridDisplayed(true);
    }
    
    protected void generateData(){
    	Random rng = new Random();

        points = new Coord3d[npt];
        colors = new Color[npt];
        for (int i = 0; i < npt; i++) {
            colors[i] = new Color(0f, 0f, 0f, 0.5f);

            float x = rng.nextFloat();
            float y = rng.nextFloat();
            float z = rng.nextFloat();

            points[i] = new Coord3d(x, y, z);
        }
        dots = new Scatter(points, colors);
        dots.setWidth(4);
    }
    
}
