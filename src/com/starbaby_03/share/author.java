package com.starbaby_03.share;

import java.awt.List;
import java.util.ArrayList;

import javax.swing.text.LayeredHighlighter.LayerPainter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.example.starbaby_03.R;
import com.starbaby_03.aboutUs.infoCenter;
import com.starbaby_03.camera.mCamera;
import com.starbaby_03.info.user_enter;
import com.starbaby_03.info.user_register;
import com.starbaby_03.main.MainActivity;
import com.starbaby_03.main.PLA_AdapterView;
import com.starbaby_03.net.AsyncHttpGet;
import com.starbaby_03.net.AsyncHttpPost;
import com.starbaby_03.net.DefaultThreadPool;
import com.starbaby_03.net.RequestParameter;
import com.starbaby_03.net.RequestResultCallback;
import com.starbaby_03.scroll.scrollOperate;
import com.starbaby_03.utils.EncodeUtil;
import com.starbaby_03.utils.JsonObject;
import com.starbaby_03.utils.ScrollUtils;
import com.starbaby_03.utils.aboutUsUtils;
import com.starbaby_03.utils.contentUtils;
import com.starbaby_03.utils.meshImgUrl;
import com.starbaby_03.utils.weiboUtils;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Gallery.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

public class author extends Activity implements OnClickListener{
	private ListView lv;
	private ArrayList<author_list> list =  new ArrayList<author_list>();
	private lazyAdapter adapter;
	View loadingView,picView;
	private ImageButton iBnt1,iBnt2;
	private TextView tv1,repliesTv;
	private ImageView iv1,headIv;
	private EditText et1 ;
	private String note = "";
	private String author;
	private String avatar;
	private String picurl;
	private String replies;//总回复数
	private String authorId;//	发帖人的uid
	public  int totalPage;
	private int mCurPage;
	private Bitmap headBit,Bit;
	private final int mPageCount = 10;
	float onePage = 10;
	private int mSelection;
	public int mLastItem;
	private EditText etView1;
	private EditText etView2;
	private AlertDialog alert;
	private ProgressBar pb;
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what == 3)
			tv1.setText(author);
			iv1.setImageBitmap(headBit);
			headIv.setImageBitmap(Bit);
			
			lv.addHeaderView(picView );
			mCurPage = 1;//初始化页数为1，可返回回复数为0
			
			totalPage = (int) Math.ceil(((float)Integer.parseInt(replies))/onePage);//一共返回几页
			loginReply(Integer.parseInt(ScrollUtils.picId),mCurPage);
		}
		
	};
	private ArrayList<String> authorList;
	private ArrayList<String> avatarList;
	private ArrayList<String> msgList;
	private ArrayList<String> timeList;
	private ArrayList<String> authorIdList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.share_authorlist);
		init();
		json(contentUtils.getPicIdUrl,Integer.parseInt(ScrollUtils.picId));
	}
	/**
	 * @param picid
	 * @param page
	 */
	void loginReply(int picid,int page){
		authorList = new ArrayList<String>();
		avatarList = new ArrayList<String>();
		msgList  = new ArrayList<String>();
		timeList = new ArrayList<String>();
		authorIdList = new ArrayList<String>();
		ArrayList<RequestParameter> parameterList = new ArrayList<RequestParameter>();
		AsyncHttpGet get = new AsyncHttpGet(null, contentUtils.replyPicUrl + picid
				+ "/" + page, parameterList, new RequestResultCallback() {
			public void onSuccess(Object o) {
				final String result = (String) o;
				author.this.runOnUiThread(new Runnable() {
					public void run() {
								try {
									JSONObject json = new JSONObject(result);
									int pagesize = json.getInt("pagesize");// 当前返回页面的评论条数
									repliesTv.setText(pagesize+" "+"条评论");
									totalPage = (int) Math.ceil(((float)pagesize)/onePage);//一共返回几页
									Log.e("pagesize=", pagesize+"");
									JSONArray datalist = json
											.getJSONArray("datalist");
									for (int i = 0; i < datalist.length(); i++) {
										JSONObject obj = datalist
												.getJSONObject(i);
										authorList.add(obj.getString("author"));
										avatarList.add(obj.getString("avatar"));
										msgList.add(obj.getString("message"));
										timeList.add(obj.getString("dataline"));
										authorIdList.add(obj.getString("authorid"));
										Log.e("result3=", obj.getString("author")+":"+obj.getString("avatar")+":"+obj.getString("message")+":"+obj.getString("dataline"));
									}
									for (int j = 0; j < authorList.size(); j++) {
										author_list aList = new author_list(
												avatarList.get(j).toString(),
												authorList.get(j).toString(),
												msgList.get(j).toString(),
												timeList.get(j).toString(),
												authorIdList.get(j).toString());
										list.add(aList);
									}
									adapter = new lazyAdapter(author.this,list);
									lv.setAdapter(adapter);
								    lv.setOnScrollListener(new ScrollListener());
								    if(datalist.length() == mPageCount)
								    lv.addFooterView(loadingView);
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
				});
			}

			@Override
			public void onFail(Exception e) {

			}
		});
		DefaultThreadPool.getInstance().execute(get);
	}
	/**
	 * 回复信息  上拉更多
	 * @param picid
	 * @param page
	 */
	void loginReplyMore(int picid,int page){
		final ArrayList<String> authorList = new ArrayList<String>();
		final ArrayList<String> avatarList = new ArrayList<String>();
		final ArrayList<String> msgList  = new ArrayList<String>();
		final ArrayList<String> timeList = new ArrayList<String>();
		final ArrayList<String> authorIdList = new ArrayList<String>();
		ArrayList<RequestParameter> parameterList = new ArrayList<RequestParameter>();
		AsyncHttpGet get = new AsyncHttpGet(null, contentUtils.replyPicUrl + picid
				+ "/" + page, parameterList, new RequestResultCallback() {
			public void onSuccess(Object o) {
				final String result = (String) o;
				author.this.runOnUiThread(new Runnable() {
					public void run() {
								try {
									JSONObject json = new JSONObject(result);
									int pagesize = json.getInt("pagesize");// 当前返回页面的评论条数
									Log.e("pagesize=", pagesize+"");
									JSONArray datalist = json
											.getJSONArray("datalist");
									for (int i = 0; i < datalist.length(); i++) {
										JSONObject obj = datalist
												.getJSONObject(i);
										authorList.add(obj.getString("author"));
										avatarList.add(obj.getString("avatar"));
										msgList.add(obj.getString("message"));
										timeList.add(obj.getString("dataline"));
										authorIdList.add(obj.getString("authorid"));
										Log.e("result3=", obj.getString("author")+":"+obj.getString("avatar")+":"+obj.getString("message")+":"+obj.getString("dataline"));
									}
									for (int j = 0; j < authorList.size(); j++) {
										author_list aList = new author_list(
												avatarList.get(j).toString(),
												authorList.get(j).toString(),
												msgList.get(j).toString(),
												timeList.get(j).toString(),
												authorIdList.get(j).toString());
										list.add(aList);
									}
									adapter.notifyDataSetChanged();
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
				});
			}

			@Override
			public void onFail(Exception e) {

			}
		});
		DefaultThreadPool.getInstance().execute(get);
	}
    private final class ScrollListener implements OnScrollListener{
    	private int number = 10;//每次请求发回回复数最大条目
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if (mLastItem == adapter.getCount()
					&& scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
				if (mCurPage != totalPage) {
					loginReplyMore(Integer.parseInt(ScrollUtils.picId),++mCurPage);
				}
			}
		}
		
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			mSelection = firstVisibleItem;
			mLastItem = firstVisibleItem + visibleItemCount - 2;
			Log.i("author","firstVisibleItem = " + firstVisibleItem + "   visibleItemCount = " + visibleItemCount);
			// 判断当下载下来的数据大小小于15条时
			if (mCurPage == totalPage) {
				lv.removeFooterView(loadingView);
			}
			
		}
    }
	private void init() {
		// TODO Auto-generated method stub
		iBnt1 = (ImageButton) findViewById(R.id.share_authorlist_ibnt1);
		iBnt2 = (ImageButton) findViewById(R.id.share_authorlist_ibnt3);
		tv1 = (TextView) findViewById(R.id.share_authorlist_tv1);
		iv1 = (ImageView) findViewById(R.id.share_authorlist_iv1);//发帖人头像
		lv = (ListView) findViewById(R.id.share_authorlist_lv1);
		et1 = (EditText) findViewById(R.id.share_authorlist_et1);
		pb = (ProgressBar) findViewById(R.id.share_aboutlist__progressbar);
		pb.setVisibility(View.GONE);
		//LOAD动画
		loadingView = getLayoutInflater().inflate(R.layout.share_loading, null);
		//表头
		picView = getLayoutInflater().inflate(R.layout.weibo_pulldown_item2,null);
		headIv = (ImageView) picView.findViewById(R.id.pulldown_item2_image2); 
		repliesTv = (TextView) picView.findViewById(R.id.pulldown_item2_title);
		//JSON解析照片信息
		iBnt1.setOnClickListener(this);
		iBnt2.setOnClickListener(this);
		iv1.setOnClickListener(this);
	}
	/**
	 * 发帖信息 JSON解析
	 * @param shortUrl
	 * @param picId
	 */
	void json(String shortUrl,int picId){
		ArrayList<RequestParameter> parameterList = new ArrayList<RequestParameter>();
		AsyncHttpGet get = new AsyncHttpGet(null, shortUrl+picId, parameterList, new RequestResultCallback() {
			
			@Override
			public void onSuccess(Object o) {
				final String result = (String)o;
				author.this.runOnUiThread(new Runnable() {
				
					

					@Override
					public void run() {
						try {
							JSONObject json = new JSONObject(result);
							JSONObject datalist = json.getJSONObject("datalist");
							author = datalist.getString("author");//发布人name
							avatar = datalist.getString("avatar"); //发布人头像
							picurl = datalist.getString("picurl"); //照片URL
							replies = datalist.getString("replies");//评论数
							authorId = datalist.getString("authorid");//发帖人uid
							
							Thread thread = new Thread(){

								@Override
								public void run() {
									super.run();
									Message msg = new Message();
									msg.what = 3;
									headBit = new meshImgUrl().returnBitMap(avatar);
									Bit = new meshImgUrl().returnBitMap(picurl);
									mHandler.sendMessage(msg);
								}
								
							};
							thread.start();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
			}
			
			@Override
			public void onFail(Exception e) {
				// TODO Auto-generated method stub
				
			}
		});
		DefaultThreadPool.getInstance().execute(get);
	}
	/**
	 * 回复LIST的布局
	 * @author Administrator
	 *
	 */
	class lazyAdapter extends BaseAdapter{
		private Context mContext;
		private ArrayList<author_list> totalList;
		private LayoutInflater inflater=null;
	    public ImageLoader imageLoader; 
	    private Bitmap bitmap;
	    private TextView time;
		private ImageView image;
		private TextView note;
		
		public lazyAdapter(Context mContext ,ArrayList<author_list> totalList ){
			this.mContext = mContext;
			this.totalList = totalList;
			inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        imageLoader=new ImageLoader(mContext.getApplicationContext());
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return totalList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup arg2) {
			View vi = convertView;
			if (convertView == null)
				vi = inflater.inflate(R.layout.share_authorlist_view, null);
			note = (TextView) vi.findViewById(R.id.share_authorlist_view_tv1);
			image = (ImageView) vi.findViewById(R.id.share_authorlist_view_iv1);//回复人的头像
			time = (TextView) vi.findViewById(R.id.share_authorlist_view_tv2);
			SpannableString spStr = new SpannableString(totalList.get(position).name + ":"
					+ totalList.get(position).msg);
			spStr.setSpan(new ForegroundColorSpan(Color.BLUE), 0, totalList.get(position).name.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
			note.setText(spStr);
			time.setText(totalList.get(position).time + "          " + position);
			imageLoader.DisplayImage(totalList.get(position).url, image);
			image.setOnClickListener(author.this);
			addListener(vi,position);
			return vi;
		}
		private void addListener(View view,final int position) {
			image.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Toast.makeText(author.this,"这是点击的第"+position , 1000).show();
					if(contentUtils.spGetInfo.getString("psw", "") ==null ||contentUtils.spGetInfo.getString("psw", "") == ""){	
						//	没登入的状态下。点击自己或者别人的头像，都默认进入个人的线上相册，不显示私密相册 
						contentUtils.Visiable =2;
						startActivity(new Intent(author.this,infoCenter.class));
					}else if((contentUtils.spGetInfo.getString("psw", "") !=null ||contentUtils.spGetInfo.getString("psw", "") != "") && contentUtils.spGetInfo.getInt("uid",0) != Integer.parseInt(list.get(position).authorId)){
						//登入的状态下。查看他人的信息
						contentUtils.Visiable =2;
						startActivity(new Intent(author.this,infoCenter.class));
					}else if((contentUtils.spGetInfo.getString("psw", "") !=null ||contentUtils.spGetInfo.getString("psw", "") != "") && contentUtils.spGetInfo.getInt("uid",0) == Integer.parseInt(list.get(position).authorId)){
						//登入状态下，查看本人头像信息
						contentUtils.Visiable =1;
						startActivity(new Intent(author.this,infoCenter.class));
					}
					contentUtils.authorId = list.get(position).authorId;
					contentUtils.authorUrl = list.get(position).url;
					contentUtils.authorName = list.get(position).name;
				}
			});
		}

	}
	/**
	 * 按钮监听事件
	 */
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.share_authorlist_ibnt1:
			this.finish();
			break;
		case R.id.share_authorlist_ibnt3://回复监听
			if(et1.getText().toString() == null && et1.getText().toString() ==""){
				Toast.makeText(this, "请输入内容", 1000).show();
			}else{
				//发飙回复时 对登入进行判断
				if (contentUtils.sp.getString("psw", "") == "" || contentUtils.sp.getString("psw", "") == null) {
					showEnter();
				}else{
					if(et1.getText().toString() == null || et1.getText().toString() == ""){
						Toast.makeText(this, "请输入内容", 1000).show();
					}else{
						pb.setVisibility(View.VISIBLE);
						loginNote(contentUtils.spGetInfo.getInt("uid", 0),contentUtils.spGetInfo.getString("psw", ""),ScrollUtils.picId,et1.getText().toString(),2,0);
						et1.setText("");
						CloseKeyBoard();
					}
				}
			}
			break;
		case R.id.info_enter_view_ibnt1://登入
			String name = etView1.getText().toString();
			String pwd = etView2.getText().toString();
			if(name != null && pwd != null){
				contentUtils.psw = EncodeUtil.getMD5(pwd.getBytes());
				if(contentUtils.psw != null && contentUtils.psw !=""){
					contentUtils.spGetInfo.edit().putString("psw", contentUtils.psw).commit();
					login(name,contentUtils.psw);
				}
			}
			break;
		case R.id.info_enter_view_ibnt2://注册
			startActivity(new Intent(this,user_register.class));
			break;
		case R.id.share_authorlist_iv1://点击发帖人头像
			Toast.makeText(this, "u touch me", 1000).show();
			Log.e("contentUtils.spGetInfo.getInt=", contentUtils.spGetInfo.getInt("uid",0)+"");
			Log.e("Integer.parseInt(authorId)", Integer.parseInt(authorId)+"");
			if(contentUtils.spGetInfo.getString("psw", "") ==null ||contentUtils.spGetInfo.getString("psw", "") == ""){	
				//	没登入的状态下。点击自己或者别人的头像，都默认进入个人的线上相册，不显示私密相册 
				startActivity(new Intent(author.this,infoCenter.class));
				contentUtils.Visiable =2;
			}else if((contentUtils.spGetInfo.getString("psw", "") !=null ||contentUtils.spGetInfo.getString("psw", "") != "") && contentUtils.spGetInfo.getInt("uid",0) != Integer.parseInt(authorId)){
				//登入的状态下。查看他人的信息
				startActivity(new Intent(author.this,infoCenter.class));
				contentUtils.Visiable =2;
			}else if((contentUtils.spGetInfo.getString("psw", "") !=null ||contentUtils.spGetInfo.getString("psw", "") != "") && contentUtils.spGetInfo.getInt("uid",0) == Integer.parseInt(authorId)){
				//登入状态下，查看本人头像信息
				startActivity(new Intent(author.this,infoCenter.class));
				contentUtils.Visiable =1;
			}
			contentUtils.authorId = authorId;
			contentUtils.authorUrl = avatar;
			contentUtils.authorName = author;
			break;
		}
	}
	/**
	 * 展示登入（发回复，未登入）
	 */
	void showEnter(){
		AlertDialog.Builder builder = new AlertDialog.Builder(author.this);
		View view = getLayoutInflater().inflate(R.layout.info_enter_view, null);
		Button iBntView1 = (Button)view.findViewById(R.id.info_enter_view_ibnt1);
		Button iBntView2 = (Button)view.findViewById(R.id.info_enter_view_ibnt2);
		etView1 =  (EditText)view. findViewById(R.id.info_center_view_et1);
		etView2 =  (EditText)view. findViewById(R.id.info_center_view_et2);
		builder.setView(view);
		alert = builder.create();
		alert.show();
		iBntView1.setOnClickListener(this);
		iBntView2.setOnClickListener(this);
	}
	/**
	 * 回复主题
	 * 
	 * @param uid
	 * @param pwd
	 * @param picid
	 * @param txt
	 * @param sys
	 * @param cid
	 */
	void loginNote(int uid,String pwd,String picid,String txt,int sys,int cid){
		ArrayList<RequestParameter> parameterList = new ArrayList<RequestParameter>();
		parameterList.add(new RequestParameter("uid",uid+""));
		parameterList.add(new RequestParameter("pwd",pwd));
		parameterList.add(new RequestParameter("picid",picid));
		parameterList.add(new RequestParameter("txt",txt));
		parameterList.add(new RequestParameter("sys",sys+""));
		parameterList.add(new RequestParameter("cid",cid+""));
		AsyncHttpPost post = new AsyncHttpPost(null, contentUtils.replyListUrl,parameterList, new RequestResultCallback() {
				
			public void onSuccess(Object o) {
				final String result = (String) o;
				author.this.runOnUiThread(new Runnable() {
					public void run() {
						Log.e("resultNote=", result);
						list.clear();
						mCurPage = 1;//初始化页数为1，可返回回复数为0
//						float onePage = 10;
//						int reply = Integer.parseInt(replies) + 1;
//						totalPage = (int) Math.ceil(((float)reply)/onePage);//一共返回几页
						loginReply(Integer.parseInt(ScrollUtils.picId),mCurPage);
						pb.setVisibility(View.GONE);
					}
				});
			}

			@Override
			public void onFail(Exception e) {
				// TODO Auto-generated method stub
			}
		});
		DefaultThreadPool.getInstance().execute(post);
	}
	/**
	 * 回复主题时
	 * 登入判断
	 * @param userName
	 * @param userPasword
	 */
	public void login(String userName, String userPasword) {

		ArrayList<RequestParameter> parameterList = new ArrayList<RequestParameter>();
		parameterList.add(new RequestParameter("username", userName));
		parameterList.add(new RequestParameter("pwd", userPasword));
		AsyncHttpPost httpost = new AsyncHttpPost(null, contentUtils.enterUrl,
				parameterList, new RequestResultCallback() {

					@Override
					public void onSuccess(Object o) {
						// TODO Auto-generated method stub
						final String result = (String) o;
						author.this.runOnUiThread(new Runnable() {

							@Override
							public void run() {
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
										
										contentUtils.Visiable = 1;
									} else if (contentUtils.msg == -4) {
										Toast.makeText(author.this,
												"用户信息不存在", 1000).show();
									} else if (contentUtils.msg == -5) {
										Toast.makeText(author.this, "密码错误",
												1000).show();
									} else {
										Toast.makeText(author.this, "未知错误",
												1000).show();
									}

								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								alert.dismiss();
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
	
	/**
	 * (non-Javadoc)点击空白处 关闭软件盘
	 * @see android.app.Activity#onTouchEvent(android.view.MotionEvent)
	 */
	// 监听点击屏幕上任何位置软键盘消失
    public void CloseKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et1.getWindowToken(), 0);

    }

    // 监听点击屏幕上任何位置软键盘消失
    public boolean onTouchEvent(MotionEvent event) {
        CloseKeyBoard();
        return super.onTouchEvent(event);
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
