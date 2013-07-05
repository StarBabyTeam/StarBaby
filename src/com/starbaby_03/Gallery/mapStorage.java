package com.starbaby_03.Gallery;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.starbaby_03.R;
import com.starbaby_03.Gallery.mapShowMin.Filter;
import com.starbaby_03.camera.mCamera;
import com.starbaby_03.main.MainActivity;
import com.starbaby_03.saveAndSearch.savePhoto;
import com.starbaby_03.saveAndSearch.serach;
import com.starbaby_03.utils.galleryUtils;
import com.starbaby_03.utils.scaleBitmapUtils;
import com.starbaby_03.utils.weiboUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.AlteredCharSequence;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class mapStorage extends Activity implements OnItemClickListener,
		android.view.View.OnClickListener, OnItemLongClickListener {
	private GridView info_grid;
	public static ArrayList<File> listFile;
	public static ArrayList<String> listword;
	private ImageButton ImgBnt1;
	private String path, name;
	public static int currentPostion;
	public static String path2;
	private ArrayList<Map<String, Object>> data ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.gallery_mapstorage);
		init();
	}

	void init() {
		ImgBnt1 = (ImageButton) findViewById(R.id.gallery_mapstorage_imagebutton1);
		info_grid = (GridView) findViewById(R.id.gallery_mapstorage_gridview);
		listFile = new ArrayList<File>();
		File dis = new File("/sdcard/starbaby");
		if (!dis.exists())
			dis.mkdir();
		new serach().getFile(new File("/sdcard/starbaby"));
		SimpleAdapter adapter = new SimpleAdapter(this, getMapData(listFile),
				R.layout.gallery_mapstorage_view, new String[] { "ItemTitle",
						"ItemText" }, new int[] {
						R.id.gallery_mapstorage_view_imageview,
						R.id.gallery_mapstorage_view_textview });
		info_grid.setAdapter(adapter);
		info_grid.setOnItemClickListener(this);
		info_grid.setOnItemLongClickListener(this);
		ImgBnt1.setOnClickListener(this);

	}

	public ArrayList<Map<String, Object>> getMapData(ArrayList<File> list) {
		data = new ArrayList<Map<String, Object>>();
		HashMap<String, Object> item = null;
		HashMap<String, Object> item2 = new HashMap<String, Object>();
		int i = 0;
		for (i = 0; i < list.size(); i++) {
			item = new HashMap<String, Object>();
			path = list.get(i).toString();
			System.out.print("path" + path);
			// 相册名称
			name = path.substring(path.lastIndexOf("/") + 1, path.length());
			System.out.print("name" + name);
			// 获取相册内所有图片
			File file = new File(path);
			getAllFiles(file);
			String word = null;
			// 获取第一张图片路径用作封面
			word = getWordData(listword);
			Log.e("word", "word");
			listword.clear();
			// 保存每一格list单元格的数据 ，
			item.put("ItemText", name);
			item.put("ItemTitle", word);
			data.add(item);
			Log.e("data=", data+"");
		}
		item2.put("ItemText", "添加新照片");
		item2.put("ItemTitle", R.drawable.appmain_takephoto);
		data.add(item2);
		Log.e("data2=", data+"");
		return data;
	}

	public ArrayList<String> getAllFiles(File root) {
		// fileList = new ArrayList<File>();
		Filter pf = new Filter();
		File files[] = root.listFiles(pf);
		if (files != null) {
			if (listword == null)
				listword = new ArrayList<String>();
			for (File f : files) {
				if (f.isDirectory()) {
					if (!f.getName().toString().matches("^\\..*")) {
						getAllFiles(f);
					}
				} else {
					listword.add(f.getAbsolutePath());
				}
			}
			return listword;
		} else {
			return null;
		}
	}

	public class Filter implements FileFilter {
		public boolean accept(File f) {
			return f.isDirectory()
					|| f.getName().matches("^.*?\\.(jpg|png|bmp|gif)$");
		}
	}

	private String getWordData(ArrayList<String> list) {
		String firstPhotoDir = "";
		if (list == null || list.size() == 0) {
			firstPhotoDir = "/sdcard/starbaby/Camera/" + "1370223214887"
					+ ".jpg";
		} else {
			firstPhotoDir = list.get(0).toString();
		}
		return firstPhotoDir;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int postion,
			long arg3) {
		if(postion== (data.size()-1)){//按住空白相册 创建新相册
			showDialog();
		}else{
			Intent intent = getIntent();
			currentPostion = postion;
			path2 = listFile.get(postion).toString();
			galleryUtils.picName = path2.substring(path2.lastIndexOf("/") + 1,
					path2.length());
			Intent intent2 = new Intent(this, mapShowMin2.class);
			intent2.putExtra("url", path2);
			if (getIntent().getExtras().getInt(weiboUtils.weibo_sharePic_key) == weiboUtils.weibo_sharePic_Flag) {
				intent2.putExtra(weiboUtils.weibo_sharePic_key,
						weiboUtils.weibo_sharePic_Flag);
			} else if (getIntent().getExtras()
					.getInt(weiboUtils.weibo_sharePic_key) == weiboUtils.weibo_showPic_Flag) {
				intent2.putExtra(weiboUtils.weibo_sharePic_key,
						weiboUtils.weibo_showPic_Flag);
			}
			startActivity(intent2);
			this.finish();
		}
	}

	// 长按删除文件夹
	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
			final int postion, long arg3) {
		if(postion!=(data.size()-1)){
			new AlertDialog.Builder(this).setTitle("是否删除相册")
			.setPositiveButton("确定", new OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					String path3 = listFile.get(postion).toString();
					deleteFile(path3);
					init();
				}
			}).setNegativeButton("取消", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
				}
			}).create().show();
		}
		return false;
	}
	// 递归删除文件夹（文件夹下有图片）
	public boolean deleteFile(String path3) {
		boolean bool = false;
		File file = new File(path3);
		if (file.exists() && file.isDirectory()) {
			if (file.listFiles().length == 0) {
				file.delete();
			} else {
				File[] f = file.listFiles();
				for (int i = 0; i < f.length; i++) {
					if (f[i].isDirectory()) {
						deleteFile(f[i].getAbsolutePath());
					}
					f[i].delete();
				}
			}
			file.delete();
			bool = true;
		}
		return bool;

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.gallery_mapstorage_imagebutton1:
			mapStorage.this.finish();
			Intent intent2 = new Intent(this, MainActivity.class);
			startActivity(intent2);
			break;
		}
	}

	// 新建相册
	void showDialog() {
		LayoutInflater inflater = LayoutInflater.from(this);
		final View view = inflater.inflate(R.layout.gallery_addpicstore, null);
		AlertDialog.Builder builder = new AlertDialog.Builder(mapStorage.this);
		builder.setTitle("新建相册");
		builder.setView(view);
		final TextView textview = (TextView) view
				.findViewById(R.id.gallery_dialog_view_textview2);
		final EditText editview = (EditText) view
				.findViewById(R.id.gallery_dialog_view_edittext);
		editview.setFilters(new InputFilter[] { new InputFilter.LengthFilter(5) });
		editview.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				int length = 0;
				length = editview.getText().length();
				textview.invalidate();
				textview.setText(length + "/" + "5");
			}
		});
		builder.setNegativeButton("取消", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

			}
		});
		builder.setPositiveButton("确定", new OnClickListener() {
			// 创建新相册
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String str = editview.getText().toString();
				File destDir = new File("/sdcard/starbaby/" + str);
				if (destDir.exists()) {
					Toast.makeText(mapStorage.this, "该相册已存在", 1000).show();
					// 按下按钮。alterdialog不消失
					try {
						Field field = dialog.getClass().getSuperclass()
								.getDeclaredField("mShowing");
						field.setAccessible(true);
						field.set(dialog, false);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					destDir.mkdir();
					// 按下按钮。alterdialog消失
					try {
						Field field = dialog.getClass().getSuperclass()
								.getDeclaredField("mShowing");
						field.setAccessible(true);
						field.set(dialog, true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				init();
			}
		});
		AlertDialog alter = builder.create();
		alter.show();
	}

	public void onBackPressed() {
		super.onBackPressed();
		mapStorage.this.finish();
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}

}
