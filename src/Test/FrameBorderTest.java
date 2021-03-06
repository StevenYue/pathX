package Test;

import java.awt.*;
import javax.swing.*;

public class FrameBorderTest {
	public static void main(String[] args) {
		GraphicsEnvironment env = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		Rectangle bounds = env.getMaximumWindowBounds();
		System.out.println("Screen Bounds: " + bounds);

		GraphicsDevice screen = env.getDefaultScreenDevice();
		GraphicsConfiguration config = screen.getDefaultConfiguration();
		System.out.println("Screen Size  : " + config.getBounds());

		JFrame frame = new JFrame("Frame Info");
		System.out.println("Frame Insets : " + frame.getInsets());

		frame.setSize(200, 200);
		System.out.println("Frame Insets : " + frame.getInsets());
		frame.setVisible(true);

		System.out.println("Frame Size   : " + frame.getSize());
		System.out.println("Frame Insets : " + frame.getInsets());
		System.out.println("Content Size : " + frame.getContentPane().getSize());
	}
}
