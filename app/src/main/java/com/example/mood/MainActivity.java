package com.example.mood;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
/*import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.Graphics2D; */
import android.graphics.Bitmap;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "prefs";
    private static final String PREF_DARK_THEME = "dark_theme";
    static final int REQUEST_IMAGE_CAPTURE = 1;
    Interpreter tflite;
    private Button btnForward; //button object created for the forwardBtn on activity_main
    private Button btnForward1; //click listener for tflite
    private Button toMusic;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //use the chosen theme
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean useDarkTheme = preferences.getBoolean(PREF_DARK_THEME, false);

        if (useDarkTheme) {
            setTheme(R.style.DarkTheme);
        }

        ////////////////////////////////////////


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //for Switch Theme Toggle
        Switch toggle = findViewById(R.id.themeSwitch);
        toggle.setChecked(useDarkTheme);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                toggleTheme(isChecked);
            }
        });

        btnForward = findViewById(R.id.forwardBtn); //finds the forwardBtn id & assigns it to btnForward
        btnForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToActivityTwo();
            }
        }); //handles the event for when you click the settings button on activity_main

        toMusic = findViewById(R.id.musicBtn);
        toMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToMusic();
            }
        });

        btnForward1 = findViewById(R.id.submitBtn);
        btnForward1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCameraApp();

            }

        });

        try {
            tflite = new Interpreter(loadModelFile());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void openCameraApp() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageTmp = (Bitmap) extras.get("data");

            Bitmap imageBitmap = Bitmap.createScaledBitmap(imageTmp, 48, 48, true);
            imageBitmap = toGrayscale(imageBitmap);
            //imageView.setImageBitmap(imageBitmap);
            Integer reeeeee = null;
            tflite.run(imageBitmap, reeeeee); //code to bite the image
            System.out.write(reeeeee);
        }
    }
    

    public Bitmap toGrayscale(Bitmap bmpOriginal)
    {        
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();
        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }

    //Used to Save User's Theme choice
    public void toggleTheme(boolean darkTheme){
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putBoolean(PREF_DARK_THEME, darkTheme);
        editor.apply();

        Intent intent = getIntent();
        finish();

        startActivity(intent);
    }

    /////////////////////////////////////////////////

    private void moveToActivityTwo(){ //creates the event when you click the settings button on
        //activity main
        Intent intent = new Intent(MainActivity.this, settings.class );
        startActivity(intent);
    }

    private void moveToMusic(){
        Intent moveMusicIntent = new Intent(MainActivity.this, music.class);
        startActivity(moveMusicIntent);
    }

    public MappedByteBuffer loadModelFile() throws IOException {
        AssetFileDescriptor fileDescriptor=this.getAssets().openFd("converted_ERCNN.tflite");
        FileInputStream inputStream=new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel=inputStream.getChannel();
        long startOffset =fileDescriptor.getStartOffset();
        long declaredLength=fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY,startOffset,declaredLength);
    }
}
/*
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

*/