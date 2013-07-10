package com.starbaby_03.main;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.baidu.mobstat.StatService;
import com.example.starbaby_03.R;
import com.googlecode.javacpp.Builder.UserClassLoader;
import com.starbaby_03.Gallery.mapStorage.Filter;
import com.starbaby_03.aboutUs.infoCenter;
import com.starbaby_03.info.user_enter;
import com.starbaby_03.saveAndSearch.serach;
import com.starbaby_03.share.author;
import com.starbaby_03.share.author_list;
import com.starbaby_03.utils.EncodeUtil;
import com.starbaby_03.utils.aboutUsUtils;
import com.starbaby_03.utils.beautyUtils;
import com.starbaby_03.utils.contentUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class appMenu extends Activity{
	private ImageView imageView;
	@Override
	/**
	 *  commit:4
	 */
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_appmenu);
		imageView=(ImageView) findViewById(R.id.imageview);
		File file = new File("/mnt/sdcard/");
		if(!file.exists()){
			file.mkdir();
		}
		File file2 = new File("/mnt/sdcard/starbaby");
		if(!file2.exists()){
			file2.mkdir();
		}
		//获取app目录下 私密相册的个数
		contentUtils.numSecretAlbum = new serach().getFile3(new File("/sdcard/starbaby"));
		beautyUtils.spPic=getSharedPreferences("spPic", MODE_WORLD_READABLE);//本地照片的描述
		contentUtils.sp = this.getSharedPreferences("enter", MODE_WORLD_READABLE);//  登入后默认记住密码的操作,下次进入app也直接登入。只有从退出登入那里取消操作
		contentUtils.spinfo = this.getSharedPreferences("userInfo",MODE_WORLD_READABLE);// 通过判断该app当前的用户，用来调用头像接口。即使不登入，也可以在本地显示头像
		contentUtils.spGetInfo	= this.getSharedPreferences("enterRecive", MODE_WORLD_READABLE)	;//登入获取的返回信息，用于后续操作
		final Intent intent=new Intent(this,MainActivity.class);
		beautyUtils.spPic.edit().clear().commit();
		contentUtils.sp.edit().clear().commit();
		contentUtils.spinfo.edit().clear().commit();
		contentUtils.spGetInfo.edit().clear().commit();
		Timer timer=new Timer();
		TimerTask timerTask=new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				startActivity(intent);
				finish();
			}
		};
		timer.schedule(timerTask, 1000*2);
	}
	 /**
     * 百度统计模块
     */
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		StatService.onPause(this);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		StatService.onResume(this);
	}
}
