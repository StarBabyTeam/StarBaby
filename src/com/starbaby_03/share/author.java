package com.starbaby_03.share;

import java.awt.List;
import java.util.ArrayList;

import javax.swing.text.LayeredHighlighter.LayerPainter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.example.starbaby_03.R;
import com.starbaby_03.aboutUs.infoCenter;
import com.starbaby_03.main.MainActivity;
import com.starbaby_03.main.PLA_AdapterView;
import com.starbaby_03.net.AsyncHttpGet;
import com.starbaby_03.net.AsyncHttpPost;
import com.starbaby_03.net.DefaultThreadPool;
import com.starbaby_03.net.RequestParameter;
import com.starbaby_03.net.RequestResultCallback;
import com.starbaby_03.utils.JsonObject;
import com.starbaby_03.utils.ScrollUtils;
import com.starbaby_03.utils.aboutUsUtils;
import com.starbaby_03.utils.contentUtils;
import com.starbaby_03.utils.meshImgUrl;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

public class author extends Activity implements OnClickListener{
	private ListView lv;
	private ArrayList<author_list> list =  new ArrayList<author_list>();
	private MyAdapter adapter;
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
	public  int totalPage;
	private int mCurPage;
	private Bitmap headBit,Bit;
	private final int mPageCount = 10;
	private int mSelection;
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what == 3)
			tv1.setText(author);
			iv1.setImageBitmap(headBit);
			headIv.setImageBitmap(Bit);
			repliesTv.setText(replies+" "+"条评论");
			lv.addHeaderView(picView );
			mCurPage = 1;//初始化页数为1，可返回回复数为0
			float onePage = 10;
			totalPage = (int) Math.ceil(((float)Integer.parseInt(replies))/onePage);//一共返回几页
			loginReply(Integer.parseInt(ScrollUtils.picId),mCurPage);
		}
		
	};
	public int mLastItem;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.share_authorlist);
		init();
		json(contentUtils.getPicIdUrl,Integer.parseInt(ScrollUtils.picId));
	}
	void loginReply(int picid,int page){
		final ArrayList<String> authorList = new ArrayList<String>();
		final ArrayList<String> avatarList = new ArrayList<String>();
		final ArrayList<String> msgList  = new ArrayList<String>();
		final ArrayList<String> timeList = new ArrayList<String>();
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
										Log.e("result3=", obj.getString("author")+":"+obj.getString("avatar")+":"+obj.getString("message")+":"+obj.getString("dataline"));
									}
									for (int j = 0; j < authorList.size(); j++) {
										author_list aList = new author_list(
												avatarList.get(j).toString(),
												authorList.get(j).toString(),
												msgList.get(j).toString(),
												timeList.get(j).toString());
										list.add(aList);
									}
									adapter = new MyAdapter(author.this,list);
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
	
	void loginReplyMore(int picid,int page){
		final ArrayList<String> authorList = new ArrayList<String>();
		final ArrayList<String> avatarList = new ArrayList<String>();
		final ArrayList<String> msgList  = new ArrayList<String>();
		final ArrayList<String> timeList = new ArrayList<String>();
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
										Log.e("result3=", obj.getString("author")+":"+obj.getString("avatar")+":"+obj.getString("message")+":"+obj.getString("dataline"));
									}
									for (int j = 0; j < authorList.size(); j++) {
										author_list aList = new author_list(
												avatarList.get(j).toString(),
												authorList.get(j).toString(),
												msgList.get(j).toString(),
												timeList.get(j).toString());
										list.add(aList);
									}
									adapter.notifyDataSetChanged();
									//lv.setSelection(mSelection);
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
				// mLoadLayout.setVisibility(View.GONE);
				lv.removeFooterView(loadingView);
			}
			
		}
    }
    
    
    
	private void init() {
		// TODO Auto-generated method stub
		iBnt1 = (ImageButton) findViewById(R.id.share_authorlist_ibnt1);
		iBnt2 = (ImageButton) findViewById(R.id.share_authorlist_ibnt3);
		tv1 = (TextView) findViewById(R.id.share_authorlist_tv1);
		iv1 = (ImageView) findViewById(R.id.share_authorlist_iv1);
		lv = (ListView) findViewById(R.id.share_authorlist_lv1);
		et1 = (EditText) findViewById(R.id.share_authorlist_et1);
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
	 * 加载评论概述
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
							author = datalist.getString("author");//发布人
							avatar = datalist.getString("avatar"); //发布人头像
							picurl = datalist.getString("picurl"); //照片URL
							replies = datalist.getString("replies");//评论数
							Log.e("json", author+":"+avatar+":"+picurl+":"+replies);
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
	 * 回复布局
	 * @author Administrator
	 *
	 */
	class MyAdapter extends BaseAdapter{
		private Context mContext;
		private ArrayList<author_list> totalList;
		private ImageView headIv;
		private Bitmap bitmap;
		private TextView noteTv;
		private TextView timeTv;
		private Handler newHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if(msg.what == 2)
					headIv.setImageBitmap(bitmap);
			}
			
		};
		public MyAdapter (Context mContext, ArrayList<author_list> totalList){
			this.mContext = mContext;
			this.totalList = totalList;
		}
		@Override
		public int getCount() {
			return totalList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			convertView = getLayoutInflater().inflate(R.layout.share_authorlist_view, null);
			headIv = (ImageView)convertView. findViewById(R.id.share_authorlist_view_iv1);
			noteTv = (TextView)convertView. findViewById(R.id.share_authorlist_view_tv1);
			timeTv = (TextView)convertView. findViewById(R.id.share_authorlist_view_tv2);
			Thread thread =new Thread(){

				@Override
				public void run() {
					super.run();
					bitmap = new meshImgUrl().returnBitMap(totalList.get(position).url);
					Message msg = new Message();
					msg.what = 2;
					newHandler.sendMessage(msg);
				}
				
			};
			thread.start();
			noteTv.setText(totalList.get(position).name+":"+totalList.get(position).msg);
			timeTv.setText(totalList.get(position).time + "          " + position);
			return convertView;
		}
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.share_authorlist_ibnt1:
			this.finish();
			break;
		case R.id.share_authorlist_ibnt3:
			if(et1.getText().toString() == null && et1.getText().toString() ==""){
				Toast.makeText(this, "请输入内容", 1000).show();
			}else{
				loginNote(contentUtils.spGetInfo.getInt("uid", 0),contentUtils.spGetInfo.getString("psw", ""),ScrollUtils.picId,et1.getText().toString(),2,0);
				et1.setText("");
			}
			break;
		}
	}
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
						Log.e("result=", result);
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
}
