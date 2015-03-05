package com.amazon.example.snake.helpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.graphics.Bitmap;
import android.view.View;

public class Screenshotter {

	
	public static String getScreen(View v1){
		
	    String mPath = v1.getContext().getExternalFilesDir(null) + "/screenshots/";
	    View v = v1.getRootView();
	    v.setDrawingCacheEnabled(true);
	    Bitmap b = v.getDrawingCache();
	    
	    File myPath = new File(mPath);
	    myPath.mkdirs();
	    
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
	    String currentDateandTime = sdf.format(new Date());
	    
	    myPath = new File(mPath +"screenshot-" + currentDateandTime + ".png");
	    FileOutputStream fos = null;
	    try {
	        fos = new FileOutputStream(myPath);
	        b.compress(Bitmap.CompressFormat.JPEG, 100, fos);
	        fos.flush();
	        fos.close();
	    }catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    
	    return myPath.getPath();
	    
	}
}
