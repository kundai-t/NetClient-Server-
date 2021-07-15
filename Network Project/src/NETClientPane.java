import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Base64;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;


/**@author KR Tambo
 * Network Project 
 * 
 *  
 * */


public class NETClientPane extends GridPane {	
	
	
	private static Socket clientSocket;
	private static final int port = 5000;
	private BufferedReader inBuff;	
	private DataOutputStream dos;
	private BufferedOutputStream bos;	
	private GridPane thePane;
	
	private String grayURL= "/api/GrayScale";
	private String rotateURL="/api/Rotate";
	private String dilationURL="/api/Dilation";
	private String cannyURL ="/api/Canny";
		
	private ImageView topimage;	
	private Button Connect;
	private Button GrayScale;
	private Button Canny;
	private Button Rotate;
	private Button Dilation;	
	private TextArea textArea;
	private ImageView imageview;
	private String  ImageURL;
	
	
	public NETClientPane() 
	
	{
		thePane = new GridPane();
		thePane.setVgap(15);
    	thePane.setHgap(15);
    	thePane.setAlignment(Pos.CENTER);
    	
    	topimage = new ImageView();
    	imageview = new ImageView();   
    	Connect = new Button("CONNECT");
    	Connect.setTextFill(Color.ORANGERED);
    	GrayScale = new Button("GRAYSCALE");
    	Canny = new Button("CANNY");
    	Rotate = new Button("ROTATE");
    	Dilation = new Button("DILATION");    
    	textArea = new TextArea();  
    	textArea.setEditable(false);
    	textArea.setPrefSize(300,200);    	
    	        	
    	thePane.add(topimage,6,0);
    	thePane.add(Connect, 2,6);
		thePane.add(GrayScale, 2,2);
		thePane.add(Rotate,2,3);
		thePane.add(Canny,2,4);
		thePane.add(Dilation,2,5);		
		thePane.add(textArea,1,1,1,1);
		thePane.add(imageview,0,0);			
		getChildren().add(thePane);		
		
		Connect.setOnAction(e->{
			textArea.clear();
			Connect("localhost", port);
			textArea.appendText("***Connected to Server: Werkzeug/0.15.4 Python/3.7.3***");
			System.out.println("You are now ready to go");
			
		});
		
		imageview.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {			
			
			 textArea.clear();	         
	         imageview.setImage(new Image(ImageURL));	         
	 	     event.consume();
	     });
		
			Rotate.setOnAction(e-> Rotate());
			Canny.setOnAction(e->Canny());
			Dilation.setOnAction(e->Dilation());			
			GrayScale.setOnAction(e->Greyscale());				
						
	}
	
	
	private void Connect (String host,int port)
	{
		try 
		{
			clientSocket = new Socket(host,port);
			
			
			inBuff = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));		
			bos= new BufferedOutputStream(clientSocket.getOutputStream());
			dos= new DataOutputStream(bos);	
			
			
		} catch (UnknownHostException e) {
			
			System.out.println("Could not find the Host");
			e.printStackTrace();
		} catch (IOException e) {
			
			System.out.println("IO Exception");
			e.printStackTrace();
		}
	}
	
	private void Rotate()
	{
		Connect("localhost", port);
		String encodedFile= null;
		try
		{
			final FileChooser fc = new FileChooser();
        	fc.setInitialDirectory(new File("./data"));
        	File file = fc.showOpenDialog(null); 
        	
        	
        	if (file!=null)
        	{
        		String imageName = file.getName();
        		Image theImage = new Image("file:data/" + imageName);
        		topimage.setImage(theImage);
        		
        		if (imageName.equals("Xmen.jpg")) 
        		{
				 ImageURL = "https://townsquare.media/site/442/files/2017/10/hugh-jackman-the-wolverine.jpg?w=980&q=75";
				 System.out.println("Hugh Michael Jackman" + "Born: 12 October 1968, Sydney, Australia \n" + 
					 		"Height 1.88 m");
				}
        		
        		else if (imageName.equals("Spiderman.jpg"))
        		{
        			 ImageURL = "https://www.biography.com/.image/ar_1:1%2Cc_fill%2Ccs_srgb%2Cg_face%2Cq_auto:good%2Cw_300/MTIwNjA4NjM0MDU3MzYwOTA4/tobey-maguire-9542472-1-402.jpg";
        			 System.out.println("Tobias Vincent Maguire" + "Born 27 June 1975,Santa Monica, California, United States \n" + 
         			 		"Height 1.73 m");
        		}
        		
        		else if (imageName.equals("Aqua.jpg"))
        		{
        			ImageURL = "https://images.indianexpress.com/2017/01/jason-momoa-759.jpg";
        			System.out.println("Jason Momoa \n" + "Born: 1 August 1979 \n" + 
        					"Height 1.93 \n" + 
        					"Nationality American");
				}
        		
        		
        		
        		
        		File imageFile= new File("data",imageName);        		
        		
    			@SuppressWarnings("resource")
    			FileInputStream fileInputStreamReader= new FileInputStream(imageFile);
    			
    			byte[] bytes= new byte[(int)imageFile.length()];
    			fileInputStreamReader.read(bytes);
    			
    			 encodedFile= new String(Base64.getEncoder().encodeToString(bytes));
    			
    			 byte[]  bytesToSend= encodedFile.getBytes(); 
    			
    			 dos.write(("POST "+ rotateURL+" HTTP/1.1\r\n").getBytes());
    			 dos.write(("Content-Type: "+"application/text\r\n").getBytes());
    			 dos.write(("Content-Length: "+ encodedFile.length() +"\r\n").getBytes());			 
    			 dos.write(("\r\n").getBytes());
    			 dos.write(bytesToSend);
    			 dos.write(("\r\n").getBytes());
    			 dos.flush();			 
    			 
    			 textArea.clear();
    			 textArea.appendText("Image has been rotated");
    			
    			 String response= "";
    			 String line= "";
    			 while(!(line= inBuff.readLine()).equals(""))
    			 {
    				 response+= line+"\n";
    			 }
    			 System.out.println(response);
    			 
    			 String imgData= "";
    			 while((line= inBuff.readLine())!=null)
    			 {
    				 imgData+= line;
    			 }
    			 String base64Str= imgData.substring(imgData.indexOf('\'')+1,imgData.lastIndexOf('}')-1);
    			 byte[] decodedString= Base64.getDecoder().decode(base64Str);
    			 
    			//Display the image
    			 Image grayImg= new Image(new ByteArrayInputStream(decodedString));
    			 imageview.setImage(grayImg);
    			 
	
			}
						
			
			 
		} catch (FileNotFoundException e) {
			
			System.out.println("File Not Found in the specified path");
			e.printStackTrace();
		} catch (IOException e) {
			
			System.out.println("File input stream compatibility issue");
			e.printStackTrace();
		}		
		
	}

	
	private void Canny ()
	{
		Connect("localhost", port);
		String encodedFile= null;
		try
		{

        	final FileChooser fc = new FileChooser();
        	fc.setInitialDirectory(new File("./data"));
        	File file = fc.showOpenDialog(null);
        	
        	if (file!=null)
        	{
        		String imageName = file.getName();
        		Image theImage = new Image("file:data/" + imageName);
        		topimage.setImage(theImage);
        		

        		if (imageName.equals("Xmen.jpg")) 
        		{
				 ImageURL = "https://townsquare.media/site/442/files/2017/10/hugh-jackman-the-wolverine.jpg?w=980&q=75";
				 System.out.println("Hugh Michael Jackman" + "Born: 12 October 1968, Sydney, Australia \n" + 
					 		"Height 1.88 m");
				}
        		
        		else if (imageName.equals("Spiderman.jpg"))
        		{
        			 ImageURL = "https://www.biography.com/.image/ar_1:1%2Cc_fill%2Ccs_srgb%2Cg_face%2Cq_auto:good%2Cw_300/MTIwNjA4NjM0MDU3MzYwOTA4/tobey-maguire-9542472-1-402.jpg";
        			 System.out.println("Tobias Vincent Maguire" + "Born 27 June 1975,Santa Monica, California, United States \n" + 
         			 		"Height 1.73 m");
        		}
        		
        		else if (imageName.equals("Aqua.jpg"))
        		{
        			ImageURL = "https://images.indianexpress.com/2017/01/jason-momoa-759.jpg";
        			System.out.println("Jason Momoa \n" + "Born: 1 August 1979 \n" + 
        					"Height 1.93 \n" + 
        					"Nationality American");
				}
        		
        		

    			File imageFile= new File("data", imageName);
    			
    			@SuppressWarnings("resource")
    			FileInputStream fileInputStreamReader= new FileInputStream(imageFile);
    			
    			byte[] bytes= new byte[(int)imageFile.length()];
    			fileInputStreamReader.read(bytes);
    			
    			 encodedFile= new String(Base64.getEncoder().encodeToString(bytes));
    			
    			 byte[]  bytesToSend= encodedFile.getBytes(); 
    			
    			 dos.write(("POST "+ cannyURL +" HTTP/1.1\r\n").getBytes());
    			 dos.write(("Content-Type: "+"application/text\r\n").getBytes());
    			 dos.write(("Content-Length: "+ encodedFile.length() +"\r\n").getBytes());
    			 dos.write(("\r\n").getBytes());
    			 dos.write(bytesToSend);
    			 dos.write(("\r\n").getBytes());
    			 dos.flush();
    			 
    			 textArea.clear();
    			 textArea.appendText("Image has been Canned");
    			
    			 String response= "";
    			 String line= "";
    			 while(!(line= inBuff.readLine()).equals(""))
    			 {
    				 response+= line+"\n";
    			 }
    			 System.out.println(response);
    			 
    			 String imgData= "";
    			 while((line= inBuff.readLine())!=null)
    			 {
    				 imgData+= line;
    			 }
    			 String base64Str= imgData.substring(imgData.indexOf('\'')+1,imgData.lastIndexOf('}')-1);
    			 byte[] decodedString= Base64.getDecoder().decode(base64Str);
    			 
    			//Display the image
    			 Image grayImg= new Image(new ByteArrayInputStream(decodedString));
    			 grayImg.getPixelReader();
    			 imageview.setImage(grayImg);   			 
				
			}
			
			 
	}catch(IOException e) {
		
		e.printStackTrace();
	}
	
	}
	
	private void Dilation()
	{
		Connect("localhost", port);
		String encodedFile= null;
		try
		{
			
			final FileChooser fc = new FileChooser();
        	fc.setInitialDirectory(new File("./data"));
        	File file = fc.showOpenDialog(null);
        	
        	if (file!=null)
        	{
        		String imageName = file.getName();
        		Image theImage = new Image("file:data/" + imageName);
        		topimage.setImage(theImage);
        		

        		if (imageName.equals("Xmen.jpg")) 
        		{
				 ImageURL = "https://townsquare.media/site/442/files/2017/10/hugh-jackman-the-wolverine.jpg?w=980&q=75";
				 System.out.println("Hugh Michael Jackman" + "Born: 12 October 1968, Sydney, Australia \n" + 
					 		"Height 1.88 m");
				}
        		
        		else if (imageName.equals("Spiderman.jpg"))
        		{
        			 ImageURL = "https://www.biography.com/.image/ar_1:1%2Cc_fill%2Ccs_srgb%2Cg_face%2Cq_auto:good%2Cw_300/MTIwNjA4NjM0MDU3MzYwOTA4/tobey-maguire-9542472-1-402.jpg";
        			 System.out.println("Tobias Vincent Maguire" + "Born 27 June 1975,Santa Monica, California, United States \n" + 
         			 		"Height 1.73 m");
        		}
        		
        		else if (imageName.equals("Aqua.jpg"))
        		{
        			ImageURL = "https://images.indianexpress.com/2017/01/jason-momoa-759.jpg";
        			System.out.println("Jason Momoa \n" + "Born: 1 August 1979 \n" + 
        					"Height 1.93 \n" + 
        					"Nationality American");
				}
        		
        		
        		File imageFile= new File("data", imageName);
        		
        		@SuppressWarnings("resource")
    			FileInputStream fileInputStreamReader= new FileInputStream(imageFile);
    			
    			byte[] bytes= new byte[(int)imageFile.length()];
    			fileInputStreamReader.read(bytes);
    			
    			 encodedFile= new String(Base64.getEncoder().encodeToString(bytes));
    			 
    			 byte[]  bytesToSend= encodedFile.getBytes(); 
    			
    			 dos.write(("POST "+ dilationURL+" HTTP/1.1\r\n").getBytes());
    			 dos.write(("Content-Type: "+"application/text\r\n").getBytes());
    			 dos.write(("Content-Length: "+ encodedFile.length() +"\r\n").getBytes());
    			 dos.write(("\r\n").getBytes());
    			 dos.write(bytesToSend);
    			 dos.write(("\r\n").getBytes());
    			 dos.flush();
    			 
    			 textArea.clear();
    			 textArea.appendText("Image has been Dilated");
    			 
    			//read text response
    			 String response= "";
    			 String line= "";
    			 while(!(line=inBuff.readLine()).equals(""))
    			 {
    				 response+= line+"\n";
    			 }
    			 System.out.println(response);
    			 
    			 String imgData= "";
    			 while((line= inBuff.readLine())!=null)
    			 {
    				 imgData+= line;
    			 }
    			 String base64Str= imgData.substring(imgData.indexOf('\'')+1,imgData.lastIndexOf('}')-1);
    			 byte[] decodedString= Base64.getDecoder().decode(base64Str);
    			//Display the image
    			 Image grayImg= new Image(new ByteArrayInputStream(decodedString));
    			 imageview.setImage(grayImg);
        	}
			
			
			
			 
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
	}

	
	private void Greyscale()
	{
		
		Connect("localhost", port);
		String encodedFile= null;
		try
		{
			final FileChooser fc = new FileChooser();
        	fc.setInitialDirectory(new File("./data"));
        	File file = fc.showOpenDialog(null);
        	
        	if (file!=null)
        	{
        		String imageName = file.getName();
        		Image theImage = new Image("file:data/" + imageName);
        		topimage.setImage(theImage);
        		

        		if (imageName.equals("Xmen.jpg")) 
        		{
				 ImageURL = "https://townsquare.media/site/442/files/2017/10/hugh-jackman-the-wolverine.jpg?w=980&q=75";
				 System.out.println("Hugh Michael Jackman" + "Born: 12 October 1968, Sydney, Australia \n" + 
				 		"Height 1.88 m");
				}
        		
        		else if (imageName.equals("Spiderman.jpg"))
        		{
        			 ImageURL = "https://www.biography.com/.image/ar_1:1%2Cc_fill%2Ccs_srgb%2Cg_face%2Cq_auto:good%2Cw_300/MTIwNjA4NjM0MDU3MzYwOTA4/tobey-maguire-9542472-1-402.jpg";
        			 System.out.println("Tobias Vincent Maguire" + "Born 27 June 1975,Santa Monica, California, United States \n" + 
        			 		"Height 1.73 m");
        		}
        		
        		else if (imageName.equals("Aqua.jpg"))
        		{
        			ImageURL = "https://images.indianexpress.com/2017/01/jason-momoa-759.jpg";
        			System.out.println("Jason Momoa \n" + "Born: 1 August 1979 \n" + 
        					"Height 1.93 \n" + 
        					"Nationality American");
				}
        		
        		
        		File imageFile= new File("data", imageName);
    			
    			@SuppressWarnings("resource")
    			FileInputStream fileInputStreamReader= new FileInputStream(imageFile);
    			
    			byte[] bytes= new byte[(int)imageFile.length()];
    			fileInputStreamReader.read(bytes);
    			
    			 encodedFile= new String(Base64.getEncoder().encodeToString(bytes));
    			 
    			 byte[]  bytesToSend= encodedFile.getBytes(); 
    			
    			 dos.write(("POST "+ grayURL+" HTTP/1.1\r\n").getBytes());
    			 dos.write(("Content-Type: "+"application/text\r\n").getBytes());
    			 dos.write(("Content-Length: "+ encodedFile.length() +"\r\n").getBytes());
    			 dos.write(("\r\n").getBytes());
    			 dos.write(bytesToSend);
    			 dos.write(("\r\n").getBytes());
    			 dos.flush();
    			 
    			 textArea.clear();
    			 textArea.appendText("Iamge has been Grayscaled");
    			 
    			//read text response
    			 String response= "";
    			 String line= "";
    			 while(!(line= inBuff.readLine()).equals(""))
    			 {
    				 response+= line+"\n";
    			 }
    			 System.out.println(response);
    			 
    			 String imgData= "";
    			 while((line=inBuff.readLine())!=null)
    			 {
    				 imgData+= line;
    			 }
    			 String base64Str= imgData.substring(imgData.indexOf('\'')+1,imgData.lastIndexOf('}')-1);
    			 byte[] decodedString= Base64.getDecoder().decode(base64Str);
    			 
    			//Display the image
    			 Image grayImg= new Image(new ByteArrayInputStream(decodedString));
    			 imageview.setImage(grayImg);
        	}
			
			
			 
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}	
	
}
