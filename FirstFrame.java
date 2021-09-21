//code written by jeffrin daniel
import java.awt.BorderLayout;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.io.IOException;
import jssc.*;
import javax.swing.*;
import javax.swing.UIManager;
public class FirstFrame {
	
	public JButton okay = new JButton("Ok, I understand");

	public void addComponents(Container pane) {
		pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
		JLabel note = new JLabel("Establishing a connection with"
				+ " the Measurescope requires clicking the [Connect] button"
				+ " before functions can be accessed and movement can be seen");
		note.setAlignmentX(Component.CENTER_ALIGNMENT);
		okay.setAlignmentX(Component.CENTER_ALIGNMENT);
		pane.add(note);
		pane.add(okay);

	}
	
	private JFrame first = new JFrame("Note");
	
	
	public FirstFrame() {
	
		first.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		addComponents(first.getContentPane());
		
		okay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ac) {
				first.dispose();
				new MeasurescopeGUI();
			}
		});
		first.setSize(600, 400);
		first.pack();
		first.setLocationRelativeTo(null);
		first.setVisible(true);
	}
	public static void main(String[] args) {
		new FirstFrame();
	}
}
