package com.starbaby_03.main;

import java.util.ArrayList;

import com.starbaby_03.utils.beautyUtils;

public class HotInfo {

	private int height;
	private ArrayList<String> authorList = new ArrayList<String>();//评论人list
	private ArrayList<String> commentList = new ArrayList<String>();//评论list
	private String isrc = "";
	private String picId;//照片ID
	private String albumid;//相册id
	private String authorid;//发布人ID 
	private String author;//发布人
	private String avatar;//发布人头像
	private String dateline;//发布时间
	private String picurl;//照片url
	private String views;//查看数
	private String replies;//评论数

	// 图片展示的宽度 < 屏幕宽度/2 等比例缩放
	public int getWidth() {
		return beautyUtils.layoutWidth / 2 - 20;
	}

	public String getPicId() {
		return picId;
	}

	public void setPicId(String picId) {
		this.picId = picId;
	}

	public String getIsrc() {
		return isrc;
	}

	public ArrayList<String> getAuthorList() {
		return authorList;
	}

	public void setAuthorList(ArrayList<String> authorList) {
		this.authorList = authorList;
	}

	public ArrayList<String> getCommentList() {
		return commentList;
	}

	public void setCommentList(ArrayList<String> commentList) {
		this.commentList = commentList;
	}

	public void setIsrc(String isrc) {
		this.isrc = isrc;
	}

	// 图片展示的高度 等比例缩放
	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getAlbumid() {
		return albumid;
	}

	public void setAlbumid(String albumid) {
		this.albumid = albumid;
	}

	public String getAuthorid() {
		return authorid;
	}

	public void setAuthorid(String authorid) {
		this.authorid = authorid;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getDateline() {
		return dateline;
	}

	public void setDateline(String dateline) {
		this.dateline = dateline;
	}

	public String getPicurl() {
		return picurl;
	}

	public void setPicurl(String picurl) {
		this.picurl = picurl;
	}

	public String getViews() {
		return views;
	}

	public void setViews(String views) {
		this.views = views;
	}

	public String getReplies() {
		return replies;
	}

	public void setReplies(String replies) {
		this.replies = replies;
	}
	
}
