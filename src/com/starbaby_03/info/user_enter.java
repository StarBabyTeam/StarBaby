package com.starbaby_03.info;

/**
 * info:登入账号
 */
import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import com.example.starbaby_03.R;
import com.starbaby_03.aboutUs.DBSharedPreference;
import com.starbaby_03.aboutUs.center;
import com.starbaby_03.aboutUs.infoCenter;
import com.starbaby_03.main.MainActivity;
import com.starbaby_03.net.AsyncHttpGet;
import com.starbaby_03.net.AsyncHttpPost;
import com.starbaby_03.net.DefaultThreadPool;
import com.starbaby_03.net.RequestParameter;
import com.starbaby_03.net.RequestResultCallback;
import com.starbaby_03.utils.EncodeUtil;
import com.starbaby_03.utils.JsonObject;
import com.starbaby_03.utils.MyData;
import com.starbaby_03.utils.ScrollUtils;
import com.starbaby_03.utils.contentUtils;
import com.starbaby_03.utils.weiboUtils;
import com.starbaby_03.view.CircleImageView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.Color;
import android.graphics.Paint;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class user_enter extends Activity implements OnClickListener {
	private ImageButton imgBnt;
	private Button Bnt1, Bnt2;
	private EditText editText1, editText2;
	private String customid;
	private TextView info_enter_textview5, info_enter_textview3;
	private ProgressBar progressbar;
	private String ename;
	private String epsw;
	private Boolean nameFlag = false;
	private Boolean pswFlag = false;
	private ImageView info_enter_imageview1;
	private String avatar = null;
	private RelativeLayout rl1;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.info_enter);
		init();
		listener();
	}

	private void listener() {
		Bnt1.setOnClickListener(this);
		Bnt2.setOnClickListener(this);
		info_enter_textview5.setOnClickListener(this);
	}

	void init() {
		Bnt1 = (Button) findViewById(R.id.info_enter_imagebutton2);
		Bnt2 = (Button) findViewById(R.id.info_enter_imagebutton3);
		editText1 = (EditText) findViewById(R.id.info_enter_edittext1);
		editText2 = (EditText) findViewById(R.id.info_enter_edittext2);
		rl1 = (RelativeLayout) findViewById(R.id.info_enter_relativelayout7);
		info_enter_imageview1 = (ImageView) findViewById(R.id.info_enter_imageview1);
		progressbar = (ProgressBar) findViewById(R.id.info_enter_progressbar);
		progressbar.setVisibility(View.GONE);
		info_enter_textview5 = (TextView) findViewById(R.id.info_enter_textview);
		Checked();
	}

	// 判断：是否记住密码，记住登入状态
	public void Checked() {
		//重新登入
		if(contentUtils.sp.getString("psw", "") ==null || contentUtils.sp.getString("psw", "") ==""){
			rl1.setVisibility(View.VISIBLE);
		}else{//已经登入过。自动登入
			progressbar.setVisibility(View.VISIBLE);
			rl1.setVisibility(View.GONE);
			Log.e("contentUtils.sp.getStringName", contentUtils.sp.getString("username", ""));
			Log.e("contentUtils.sp.getStringPsw", contentUtils.sp.getString("psw", ""));
			login(contentUtils.sp.getString("username", ""), contentUtils.sp.getString("psw", ""));
		}
	}

	void enter() {
		ename = editText1.getText().toString();
		epsw = editText2.getText().toString();
		if(ename != null && epsw != null){
			// 登录成功和记住密码框为选中状态才保存用户信息
			progressbar.setVisibility(View.VISIBLE);
			// 密码MD5一次
			contentUtils.psw = EncodeUtil.getMD5(epsw.getBytes());
			login(ename, contentUtils.psw);
		}else{
			Toast.makeText(this, "请输入完整信息", 1000).show();
		}
		
	}

	// 登入判断
	public void login(String userName, String userPasword) {

		List<RequestParameter> parameterList = new ArrayList<RequestParameter>();
		parameterList.add(new RequestParameter("username", userName));
		parameterList.add(new RequestParameter("pwd", userPasword));
		AsyncHttpPost httpost = new AsyncHttpPost(null, contentUtils.enterUrl,
				parameterList, new RequestResultCallback() {

					@Override
					public void onSuccess(Object o) {
						// TODO Auto-generated method stub
						final String result = (String) o;
						user_enter.this.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								progressbar.setVisibility(View.GONE);
								try {
									contentUtils.msg = new JsonObject().getMSG(result);
									if (contentUtils.msg == 1) {
//										contentUtils.psw = editText2.getText().toString();
										contentUtils.uid = new JsonObject().getUID(result);
										contentUtils.authorId = new JsonObject().getUID(result)+"";
										contentUtils.username = new JsonObject().getUSERNAME(result);
										contentUtils.avatar = new JsonObject().getAVATAR(result);
										contentUtils.authorUrl = new JsonObject().getAVATAR(result);
										contentUtils.authorName = new JsonObject().getUSERNAME(result);
										
										contentUtils.sp.edit().putString("username",contentUtils.username).commit();
										contentUtils.sp.edit().putString("psw",contentUtils.psw).commit();
										
										contentUtils.spinfo.edit().putString("username",contentUtils.username).commit();
										contentUtils.spinfo.edit().putString("avatar",contentUtils.avatar).commit();
										
										contentUtils.spGetInfo.edit().putInt("uid", contentUtils.uid).commit();
										contentUtils.spGetInfo.edit().putString("username", contentUtils.username).commit();
										contentUtils.spGetInfo.edit().putString("avatar", contentUtils.avatar).commit();
										contentUtils.spGetInfo.edit().putString("psw", contentUtils.psw).commit();
										
										Intent intent = new Intent(user_enter.this,infoCenter.class);
										intent.putExtra(weiboUtils.weibo_sharePicSucc_key,weiboUtils.weibo_return_Flag3);
										weiboUtils.FLAG = 1;
										startActivity(intent);
										contentUtils.Visiable = 1;
										user_enter.this.finish();
									} else if (contentUtils.msg == -4) {
										Toast.makeText(user_enter.this,
												"用户信息不存在", 1000).show();
									} else if (contentUtils.msg == -5) {
										Toast.makeText(user_enter.this, "密码错误",
												1000).show();
									} else {
										Toast.makeText(user_enter.this, "未知错误",
												1000).show();
									}

								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								Log.e("result", result);
							}
						});
					}

					@Override
					public void onFail(Exception e) {
						// TODO Auto-generated method stub

					}
				});
		DefaultThreadPool.getInstance().execute(httpost);
	}

	// get方法获取头像
	public void getHeadImg(int uid, int size, int choose) {

		List<RequestParameter> parameterList = new ArrayList<RequestParameter>();
		AsyncHttpPost httpget = new AsyncHttpPost(null, contentUtils.getImgUrl
				+ "/" + uid + "/" + size + "/" + choose, parameterList,
				new RequestResultCallback() {

					@Override
					public void onSuccess(Object o) {
						final String result = (String) o;
						customid = result.replace("\"", " ");
						MyData.getInstance().setCustomerid(customid);
						user_enter.this.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								contentUtils.headImgUrl = result;
								Log.e("headUrl=", result);
							}
						});
					}

					@Override
					public void onFail(Exception e) {
						// TODO Auto-generated method stub

					}
				});
		DefaultThreadPool.getInstance().execute(httpget);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.info_enter_imagebutton2:
			enter();
			break;
		case R.id.info_enter_imagebutton3:
			Intent intent2 = new Intent(this, user_register.class);
			startActivity(intent2);
			break;
		}
	}

	public void onBackPressed() {
		super.onBackPressed();
		startActivity(new Intent(user_enter.this, MainActivity.class));
		user_enter.this.finish();
	}
}
