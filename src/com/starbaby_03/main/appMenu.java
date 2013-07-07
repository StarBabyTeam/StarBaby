package com.starbaby_03.main;

import java.util.Timer;
import java.util.TimerTask;

import com.example.starbaby_03.R;
import com.googlecode.javacpp.Builder.UserClassLoader;
import com.starbaby_03.aboutUs.infoCenter;
import com.starbaby_03.info.user_enter;
import com.starbaby_03.share.author;
import com.starbaby_03.share.author_list;
import com.starbaby_03.utils.aboutUsUtils;
import com.starbaby_03.utils.beautyUtils;
import com.starbaby_03.utils.contentUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class appMenu extends Activity{
	private ImageView imageView;
	@Override
	/**
	 *  commit:1
	 */
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_appmenu);
		imageView=(ImageView) findViewById(R.id.imageview);
		beautyUtils.spPic=getSharedPreferences("spPic", MODE_WORLD_READABLE);//������Ƭ������
		contentUtils.sp = this.getSharedPreferences("enter", MODE_WORLD_READABLE);//  ��ס����Ĳ���
		contentUtils.spinfo = this.getSharedPreferences("userInfo",MODE_WORLD_READABLE);// ͨ���жϸ�app��ǰ���û�����������ͷ��ӿڡ���ʹ�����룬Ҳ�����ڱ�����ʾͷ��
		contentUtils.spGetInfo	= this.getSharedPreferences("enterRecive", MODE_WORLD_READABLE)	;//�����ȡ�ķ�����Ϣ�����ں�������
		final Intent intent=new Intent(this,MainActivity.class);
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
}
