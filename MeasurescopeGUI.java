//code by nick cline
//todo: more buttons like connect button, joystick, add stuff

import java.awt.BorderLayout;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.io.IOException;
import jssc.*;


public class MeasurescopeGUI extends JFrame {
	private JFrame open = new JFrame("Measurescope GUI");
    private JButton connectButton = new JButton("Connect");

	private JButton jbtLeft = new JButton("Left");
    private JButton jbtRight = new JButton("Right");
    private JButton jbtUp = new JButton("Up");
    private JButton jbtDown = new JButton("Down");
    private BallPanel ballPanel = new BallPanel();
    private LabelPanel labelPanel = new LabelPanel();
    private JLabel labelX = new JLabel("(" + Integer.toString(ballPanel.xCoord));
    private JLabel labelY = new JLabel(Integer.toString(ballPanel.yCoord) + ")");
    private JLabel coordLabel = new JLabel("Coordinates (X,Y)");
    private JTextField xText = new JTextField(4);
    private JTextField yText = new JTextField(4);
    
    //customization to movement: step size, units, feed rate
    private JTextField xyStep = new JTextField(5);
    private JTextField zStep = new JTextField(5);
    private JTextField feedRate = new JTextField(5);
    private JButton appXY = new JButton("Set XY Step size");
    private JButton appZ = new JButton("Set Z Step size");
    private JButton appFR = new JButton("Set Feed Rate  ");
    private JButton inch = new JButton("inches");
    private JButton mm = new JButton("mm");
    private int units = 21;
    private JLabel curLabel = new JLabel("inches");
   
    private JLabel statusHead = new JLabel("Machine Status");
    private String con = "CONNECTED: Idle";
    private String discon = "DISCONNECTED";
    private String act = "ACTIVE";
    private JLabel status = new JLabel(discon);
    
    private JLabel workP = new JLabel("Work Position   ");
    private JLabel machP = new JLabel("Machine Position");
    private int Wx = 0;
    private int Mx = 0;
    private int Wy = 0;
    private int My = 0;
    private int Wz = 0;
    private int Mz = 0;
    private JLabel workx = new JLabel("X: 0 " + curLabel.getText() + "         ");
    private JLabel machx = new JLabel("X: 0 " + curLabel.getText() + "               ");
    private JLabel worky = new JLabel("Y: 0 " + curLabel.getText() + "         ");
    private JLabel machy = new JLabel("Y: 0 " + curLabel.getText() + "               ");
    private JLabel workz = new JLabel("Z: 0 " + curLabel.getText() + "         ");
    private JLabel machz = new JLabel("Z: 0 " + curLabel.getText() + "               ");

    private JButton coordPress = new JButton("Change Coords");
    private JTextField miscString = new JTextField(8);
    private JButton sendmsgButton = new JButton("Send Message");
    public static SerialPort serialPort = new SerialPort("COM4");
    
   
    
    private int xy_step = 1;
    private int z_step = 1;
    private int feed_rate = 5;
    private JLabel labelXY_step = new JLabel("[" + Integer.toString(xy_step) + "]");
    private JLabel labelZ_step = new JLabel("[" + Integer.toString(z_step) + "]");
    private JLabel label_FR = new JLabel("[" + Integer.toString(feed_rate) + "]");


    //instantiate buttons, labels, and panels
	
	public MeasurescopeGUI() {
		super("Measurescope GUI");
		open.setTitle("Measurescope GUI");
		open.pack();
		open.setLocationRelativeTo(null);
		open.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		open.setSize(950, 500);
		open.setVisible(true);
		
		JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
      //  JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        JPanel labelPanel2 = new JPanel();
        labelPanel2.setLayout(new FlowLayout(FlowLayout.RIGHT));
        JPanel labelPanel3 = new JPanel();
        labelPanel3.setLayout(new FlowLayout(FlowLayout.LEFT));
        //instantiate new panels

        buttonPanel.add(jbtLeft);
        buttonPanel.add(jbtRight);
        buttonPanel.add(jbtUp);
        buttonPanel.add(jbtDown);
        
        labelPanel.add(statusHead);
        labelPanel.add(status);
        labelPanel.add(connectButton);
        labelPanel.add(miscString);
        labelPanel.add(sendmsgButton);
     //   add(labelPanel, BorderLayout.EAST);
        
        labelPanel2.add(coordLabel);
        labelPanel2.add(labelX);
        labelPanel2.add(labelY);
        labelPanel2.add(xText);
        labelPanel2.add(yText);
        labelPanel2.add(coordPress);
        
      //  add(labelPanel2, BorderLayout.LINE_END);

        labelPanel3.add(xyStep);
        labelPanel3.add(appXY);
        labelPanel3.add(labelXY_step);

        labelPanel3.add(zStep);
        labelPanel3.add(appZ);
        labelPanel3.add(labelZ_step);
        labelPanel3.add(feedRate);
        labelPanel3.add(appFR);
        labelPanel3.add(label_FR);
        labelPanel3.add(inch);
        labelPanel3.add(mm);
        labelPanel3.add(curLabel);
        labelPanel3.add(workP);
        labelPanel3.add(machP);
        labelPanel3.add(workx);
        labelPanel3.add(machx);
        labelPanel3.add(worky);
        labelPanel3.add(machy);
        labelPanel3.add(workz);
        labelPanel3.add(machz);
     //   add(labelPanel3, BorderLayout.SOUTH);
        
        //add buttons/labels to respective panels
        
        ballPanel.setSize(400, 500);
        ballPanel.setBackground(Color.WHITE);
        ballPanel.setVisible(true);
        
        open.add(labelPanel, BorderLayout.PAGE_START);
        open.add(ballPanel, BorderLayout.LINE_START);
        open.add(labelPanel3, BorderLayout.CENTER);
        open.add(labelPanel2, BorderLayout.LINE_END);
        open.add(buttonPanel, BorderLayout.SOUTH);

        //add ball visualization to JFrame TestFrames, puts buttons on bottom and coordinate panel on right
        
        jbtLeft.addActionListener(new ButtonListener());
        jbtRight.addActionListener(new ButtonListener());
        jbtUp.addActionListener(new ButtonListener());
        jbtDown.addActionListener(new ButtonListener());
        coordPress.addActionListener(new ButtonListener());
        connectButton.addActionListener(new ButtonListener());
        sendmsgButton.addActionListener(new ButtonListener());
        appXY.addActionListener(new ButtonListener());
        appZ.addActionListener(new ButtonListener());
        appFR.addActionListener(new ButtonListener());
        inch.addActionListener(new ButtonListener());
        mm.addActionListener(new ButtonListener());
        //actionlisteners for button actions
    }
    
    public static void openWait(long ms) {
    	System.out.println("Waiting " +  ms +  " seconds.");
    	try{
    		Thread.sleep(ms*1000);
    	}catch (InterruptedException e) {
    		e.printStackTrace();
    	}
    }

    public static void main(String[] args) {

        TestFrames mainWindow = new TestFrames();
        //makes new window
        mainWindow.setTitle("TEST");
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.pack();
        //sets title and close operation, mainWindow.pack() sets window to size and layouts preferred
        mainWindow.setVisible(true);
        //sets window visible
        
    	//jssc part
    	// getting serial ports list into the array, printing available ports
    	String[] portNames = SerialPortList.getPortNames();  
    	if (portNames.length == 0) {
    	    System.out.println("There are no serialports  You can use an emulator, such ad VSPE, to create a virtual serial port.");
    	    System.out.println("Press Enter to exit");
    	    try {
    	        System.in.read();
    	    } catch (IOException e) {
    	          e.printStackTrace();
    	    }
    	    return;
    	}
    	for (int i = 0; i < portNames.length; i++){
    	    System.out.println(portNames[i]);
    	}

    	//serialport declaration is in instance vars
    }

    class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent buttonPressed) {
        	if(buttonPressed.getSource() == connectButton) {
            	System.out.println("Port Opening...");
            	try {
            	    serialPort.openPort();
            	    System.out.println("Port Opened");
            	    serialPort.setParams(SerialPort.BAUDRATE_115200,
            	                         SerialPort.DATABITS_8,
            	                         SerialPort.STOPBITS_1,
            	                         SerialPort.PARITY_NONE);
            	    System.out.println("Port parameters set...");
            	    serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN | 
            	                                  SerialPort.FLOWCONTROL_RTSCTS_OUT);
            	    System.out.println("Flow control mode set...");
            	    serialPort.addEventListener(new PortReader(), SerialPort.MASK_RXCHAR);
            	    System.out.println("Event listener added...");
            	    openWait(5);
            	    System.out.println("Done.");
            	    status.setText(con);
            	    serialPort.writeString("G28"); //this is to reset the work position
            	}
            	catch (SerialPortException ex) {
            	    System.out.println("There are an error on writing string to port T: " + ex);
            	}
            	
            }
            if (buttonPressed.getSource() == jbtLeft) {
            	ballPanel.left();
            	labelPanel.update(ballPanel.xCoord, ballPanel.yCoord);
            	int delay = 2000;
                Timer timer = new Timer(delay, new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        status.setText(con);
                    };
                });
                timer.setRepeats(false);
                timer.start();
            	//ballPanel.left() moves ball left, labelPanel.update() updates the coordinate labels after ball moves
            	//This is where you'd add some sort of other function that sends data to gcode, so maybe create a new function in labelPanel or ballPanel and call it here, 
            	//or you could put some code in ballPanel.left(), ballPanel.right(), etc. and that would do the job
                
                Wx -= Integer.parseInt(xyStep.getText());
        		workx.setText("X:" + Integer.toString(Wx) + " "  + curLabel.getText() + "         ");
        		Mx -= Integer.parseInt(xyStep.getText());
        		machx.setText("X:" + Integer.toString(Mx) + " "  + curLabel.getText() + "         ");
        		
            	try {
					//serialPort.writeString("G21G91X-1F10\n");
            		serialPort.writeString("G" + Integer.toString(units) +
        					"G91X-1F" + Integer.toString(feed_rate) + "\n");
            		
            		System.out.print("Sent " + "G" + Integer.toString(units) +
					"G91X-1F" + Integer.toString(feed_rate) + "\n");
            		
            		System.out.print("Done\n");
            		ballPanel.left();
				} catch (SerialPortException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		status.setText(act);
        		
        		System.out.println("Sent "+ miscString.getText());
        		
            } else if (buttonPressed.getSource() == jbtRight) {
            	int delay = 2000;
                Timer timer = new Timer(delay, new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        status.setText(con);
                    };
                });
                timer.setRepeats(false);
                timer.start();
                
                Wx += Integer.parseInt(xyStep.getText());
        		workx.setText("X:" + Integer.toString(Wx) + " "  + curLabel.getText() + "         ");
        		Mx += Integer.parseInt(xyStep.getText());
        		machx.setText("X:" + Integer.toString(Mx) + " "  + curLabel.getText() + "         ");
        		
            	try {
					serialPort.writeString("G" + Integer.toString(units) +
							"G91X+1F" + Integer.toString(feed_rate) + "\n");
				} catch (SerialPortException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		
        		System.out.print("Sent " + "G" + Integer.toString(units) +
				"G91X+1F" + Integer.toString(feed_rate) + "\n");
                ballPanel.right();
                labelPanel.update(ballPanel.xCoord, ballPanel.yCoord);
                
            }else if (buttonPressed.getSource() == jbtUp) {
            	int delay = 2000;
                Timer timer = new Timer(delay, new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        status.setText(con);
                    };
                });
                timer.setRepeats(false);
                timer.start();
                
                Wy += Integer.parseInt(xyStep.getText());
        		worky.setText("X:" + Integer.toString(Wx) + " "  + curLabel.getText() + "         ");
        		My += Integer.parseInt(xyStep.getText());
        		machy.setText("X:" + Integer.toString(Mx) + " "  + curLabel.getText() + "         ");
        		
            	try {
					serialPort.writeString("G" + Integer.toString(units) +
							"G91Y+1F" + Integer.toString(feed_rate) + "\n");
				} catch (SerialPortException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		
        		System.out.print("Sent " + "G" + Integer.toString(units) +
				"G91Y+1F" + Integer.toString(feed_rate) + "\n");
                ballPanel.up();
                labelPanel.update(ballPanel.xCoord, ballPanel.yCoord);
                
            }else if (buttonPressed.getSource() == jbtDown) {
            	int delay = 2000;
                Timer timer = new Timer(delay, new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        status.setText(con);
                    };
                });
                timer.setRepeats(false);
                timer.start();
                
                Wy -= Integer.parseInt(xyStep.getText());
        		worky.setText("X:" + Integer.toString(Wx) + " "  + curLabel.getText() + "         ");
        		My -= Integer.parseInt(xyStep.getText());
        		machy.setText("X:" + Integer.toString(Mx) + " "  + curLabel.getText() + "         ");
        		
            	try {
					serialPort.writeString("G" + Integer.toString(units) +
							"G91Y-1F" + Integer.toString(feed_rate) + "\n");
				} catch (SerialPortException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		
        		System.out.print("Sent " + "G" + Integer.toString(units) +
				"G91Y-1F" + Integer.toString(feed_rate) + "\n");
                ballPanel.down();
                labelPanel.update(ballPanel.xCoord, ballPanel.yCoord);
            }
            
            if(buttonPressed.getSource() == coordPress) {
            	ballPanel.replace(Integer.parseInt(xText.getText()),Integer.parseInt(yText.getText()));
            	try {
					serialPort.writeString("G" + Integer.toString(units) +
								"G90 G1 X" + xText.getText() + " Y" + yText.getText() + 
								"F" + Integer.toString(feed_rate) + "\n");
				} catch (SerialPortException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
            
            if(buttonPressed.getSource()==sendmsgButton) {
            	
            	try {
            		serialPort.writeString(miscString.getText()+"\n");
            		System.out.println("Sent "+ miscString.getText());
            	}catch (SerialPortException ex) {
                	System.out.println("There are an error on writing string to port T: " + ex);
            	}
            }
            
            //movement customization
            if(buttonPressed.getSource() == appXY) {
            	xy_step = Integer.parseInt(xyStep.getText());
            	labelXY_step.setText(Integer.toString(xy_step));
            }
            if(buttonPressed.getSource() == appZ) {
            	z_step = Integer.parseInt(zStep.getText());
            	labelZ_step.setText(Integer.toString(z_step));
            }
            if(buttonPressed.getSource() == appFR) {
            	feed_rate = Integer.parseInt(feedRate.getText());
            	label_FR.setText(Integer.toString(feed_rate));
            }
            
            if (buttonPressed.getSource() == inch) {
            	units = 20;
            	curLabel.setText("inches");
            }
            if (buttonPressed.getSource() == mm) {
            	units = 21;
            	curLabel.setText("mm");
            }
            //checks for where the buttonPressed action is coming from and does according action
        }
    }

    class BallPanel extends JPanel {
        private int xCoord = -1;
        private int yCoord = -1;
        private Dimension preferredSize = new Dimension(300,200);
        //sets coordinates to -1 so the if statement in paintComponent can be called to center the ball
        public void left() {
            xCoord-=5;
            repaint();
            //moving ball left and repainting, same functionality for the different directions
            
            /*
            try {
            	serialPort.writeString();
            }catch (SerialPortException ex){
            	System.out.println("There are an error on writing string to port T: " + ex);
            }
            */
        }

        public void right() {
            xCoord+=5;
            repaint();
            //remember to put sample gcode writing here
            /*
            try {
            	serialPort.writeString();
            }catch (SerialPortException ex){
            	System.out.println("There are an error on writing string to port T: " + ex);
            }
            */
        }
        public void up() {
            yCoord-=5;
            repaint();
            /*
            try {
            	serialPort.writeString();
            }catch (SerialPortException ex){
            	System.out.println("There are an error on writing string to port T: " + ex);
            }
            */
            
        }

        public void down() {
            yCoord+=5;
            repaint();
            /*
            try {
            	serialPort.writeString();
            }catch (SerialPortException ex){
            	System.out.println("There are an error on writing string to port T: " + ex);
            }
            */
        }
        
        public void replace(int x, int y) {
        	xCoord=x;
        	yCoord=y;
        	repaint();
        }

        public Dimension getPreferredSize() {
            return preferredSize;
        }

        protected void paintComponent(Graphics aBall) {
            super.paintComponent(aBall);

            if (xCoord<0 || yCoord<0) {
                xCoord = getWidth()/2;
                yCoord = getHeight()/2;
                //sets coordinates at half the height and width of the screen
            }
            //System.out.println("X" + getWidth());
            aBall.drawOval(xCoord, yCoord, 10, 10);
        }
    }
    
    class LabelPanel extends JPanel{
    	
    	public void update(int xpos, int ypos) {
    		labelX.setText(Integer.toString(xpos));
    		labelY.setText(Integer.toString(ypos));
    		//updates coordinates by setting label text 
    	}
    	

    }
    
    
    private static class PortReader implements SerialPortEventListener {

        @Override
        public void serialEvent(SerialPortEvent event) {
            if(event.isRXCHAR() && event.getEventValue() > 0) {
                try {
                    String receivedData = serialPort.readString(event.getEventValue());
                    System.out.println("Received response: " + receivedData);
                }
                catch (SerialPortException ex) {
                    System.out.println("Error in receiving string from COM-port: " + ex);
                }
            }
        }

    }
}
