package com.pw.schoolknow.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class VideoUtil {
	
	/**
	 * 播放在线视频
	 * @param context
	 * @param videoPath
	 */
	public static void play(Context context, String videoPath)
	  {
	    Intent localIntent;
	    (localIntent = new Intent("android.intent.action.VIEW"))
	      .setFlags(268435456);
	    localIntent.setDataAndType(Uri.parse(videoPath), "video/mp4");
	    context.startActivity(localIntent);
	  }

}
