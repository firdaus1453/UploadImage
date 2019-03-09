package com.hakiki95.uploadimage.Model;

import com.google.gson.annotations.SerializedName;

public class MakananResponse{

	@SerializedName("result")
	private int result;

	@SerializedName("name")
	private String name;

	@SerializedName("message")
	private String message;

	@SerializedName("url")
	private String url;

	public void setResult(int result){
		this.result = result;
	}

	public int getResult(){
		return result;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setUrl(String url){
		this.url = url;
	}

	public String getUrl(){
		return url;
	}
}