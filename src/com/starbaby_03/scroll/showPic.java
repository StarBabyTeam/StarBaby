package com.starbaby_03.scroll;

import com.example.starbaby_03.R;
import com.starbaby_03.camera.mCamera;
import com.starbaby_03.main.MainActivity;
import com.starbaby_03.utils.ScrollUtils;
import com.starbaby_03.utils.beautyUtils;
import com.starbaby_03.utils.saveFile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
/***
 * 
 * 展示本地单张图片以及图片描述
 * @author Administrator
 *
 */
public class showPic extends Activity implements OnClickListener {
	private ImageButton iBnt1, iBnt2;
	private ImageView iv1;
	private TextView tv1;
	private String path;//单张图片的路径

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scroll_showpic);
		init();
		listener();
	}

	private void listener() {
		iBnt1.setOnClickListener(this);
		iBnt2.setOnClickListener(this);
	}

	private void init() {                                          
		iBnt1 = (ImageButton) findViewById(R.id.scroll_showpic_ibnt1);
		iBnt2 = (ImageButton) findViewById(R.id.scroll_showpic_ibnt2);
		iv1 = (ImageView) findViewById(R.id.srcoll_showpic_iv1);
		tv1 = (TextView) findViewById(R.id.scroll_showpic_tv1);
		getBitmap();
	}
	private void getBitmap(){
		Intent intent= getIntent();
		path = intent.getStringExtra("mapShowMin2_url");
		Bitmap bitmap = BitmapFactory.decodeFile(path);
		iv1.setImageBitmap(bitmap);
		String text = "";
		if(beautyUtils.spPic.contains("/mnt"+path)){
			text = beautyUtils.spPic.getString("/mnt"+path, "");
		}
		tv1.setText(text);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.scroll_showpic_ibnt1://返回
			this.finish();
			break;
		case R.id.scroll_showpic_ibnt2://分享,情况为从本地分享到线上
			Intent intent = new Intent(this,scrollOperate.class);
			intent.putExtra("scroll", "release");
			ScrollUtils.sharePath = path;
			saveFile.operateName = path;
			startActivity(intent);
			break;
		}
	}
	/**
	 * 按BACK键
	 */
	@Override
	public void onBackPressed()
	// 无意中按返回键时要释放内存
	{
		super.onBackPressed();
		this.finish();
	}
}
