package Test;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.Timer;

import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class JtextAreaTest extends JFrame {
	private JPanel jp;
	private JTextArea jt;

	

    		
//    		
//    		
//    		new ActionListener() {
//       public void actionPerformed(ActionEvent e) {
//          System.out.println("Button Pressed!");
//       }
//    });


	public JtextAreaTest(){
		final javax.swing.Timer timer = new javax.swing.Timer(100, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.out.println("ccc");
			}
		});
//		setLayout(null);
		setLocation(200, 200);
		setSize(500, 500);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Toolkit tk = Toolkit.getDefaultToolkit();
	    Image img = tk.getImage("./img/QuitButton.png");
	    ImageIcon imgIcon = new ImageIcon(img);
        // AND BUILD THE BUTTON
        JButton langButton = new JButton(imgIcon);
        langButton.setPreferredSize(new Dimension(30,30));
        final ButtonModel model =langButton.getModel();
        langButton.addMouseListener(new MouseListener() {
			boolean a = false;
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				if(timer.isRunning())
					timer.stop();
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				if(!timer.isRunning()) 
		               timer.start();
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
        
        langButton.setVisible(true);
//		jt = new JTextArea("Fuck");
		jp = new JPanel();
//		jp.add(jt);
		jp.add(langButton);
		this.add(jp);
		this.setVisible(true);
	}
	public static void main(String[] args) {
		new JtextAreaTest();
	}
}
