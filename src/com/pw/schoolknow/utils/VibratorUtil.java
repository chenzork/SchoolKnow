package com.pw.schoolknow.utils;


import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.os.Vibrator;

/**
 * 
 * @author wei8888go
 * ’Ò∂Øπ‹¿Ì
 *
 */
public class VibratorUtil {
	
	public static void startVibrator(Context context){
		Vibrator vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);  
        long[] pattern = {1000, 2000, 1000, 3000};            
        vibrator.vibrate(pattern, -1); 
	}
	
	public static void Vibrate( final Activity activity, long milliseconds) {   
	      Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);   
	      vib.vibrate(milliseconds);   
	   }   
   public static void Vibrate( final Activity activity, long[] pattern,boolean isRepeat) {   
          Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);   
          vib.vibrate(pattern, isRepeat ? 1 : -1);   
   }   

}
