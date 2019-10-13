// This program will process frames to ensure they are 48x48 and greyscale, indicating
// they are ready to be sent to the facial detector.
// CSC 414 Group 7
// 10-13-2019 sun

import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;


public class ProcessImage {
	
	public static void main(String args[])throws IOException{
		
	BufferedImage inputImg = null; //holds input image file
	File f = null; //holds image path
	
	//read image
	try { 
		f = new File("C:\\Users\\asiaa\\Documents\\test.jpg");//insert input image path here
		inputImg = ImageIO.read(f);
	}catch(IOException e) {
	System.out.println(e);
	}
	
	//set image dimensions to 48x48
	BufferedImage outputImg = new BufferedImage(48, 48, inputImg.getType()); //creates output image
	Graphics2D g2d = outputImg.createGraphics();
	g2d.drawImage(inputImg,  0, 0, 48,  48,  null);
	g2d.dispose();
	
	//loop to process each pixel
	for(int y = 0; y < 48; y++) {
		for(int x = 0; x < 48; x++) {
			
			//variables to hold pixel, alpha, red, green, and blue value
			int p = outputImg.getRGB(x,y);
			int a = (p>>24)&0xff;
			int r = (p>>16)&0xff;
			int g = (p>>8)&0xff;
			int b = p&0xff;
			
			//average of RGB value
			int avg = (r+g+b)/3;
			
			//find new pixel value and set it to the pixel at (x,y)
			p = (a<<24) | (avg<<16) | (avg<<8) | avg;
			outputImg.setRGB(x, y, p);
		}
	}
		
		//writing the image file
		try {
			f = new File("C:\\Users\\asiaa\\Documents\\output.jpg"); //path for processed image goes here
			ImageIO.write(outputImg, "jpg", f);
		}catch(IOException g) {
			System.out.println(g);		
		}
	
	}//main ends

}//class ends
