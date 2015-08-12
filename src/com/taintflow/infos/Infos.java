package com.taintflow.infos;

import android.graphics.drawable.Drawable;

public class Infos {
	private Drawable Icon = null;
	private String Name = "";
	private boolean IsEnable;
	private int Number;

	public Infos() {
	}

	public Infos(Drawable icon, String name) {
		this(icon, name, false);
	}
	Infos(Drawable icon, String name,boolean isEnable){
		this(icon, name, isEnable, 0);
	}	
	Infos(Drawable icon, String name,boolean isEnable,int number){
		this.Icon = icon;
		this.Name = name;
		this.IsEnable = isEnable;
		this.Number = number;
	}	
	
	public void setNumber(int number) {
		Number = number;
	}
	
	public void setEnable(boolean isEnable) {
		this.IsEnable = isEnable;
	}
	
	public void setIcon(Drawable icon) {
		Icon = icon;
	}

	public void setName(String name) {
		Name = name;
	}

	public Drawable getIcon() {
		return Icon;
	}
	
	public boolean getIsEnable() {
		return IsEnable;
	}

	public String getName() {
		return Name;
	}
	public int getNumber() {
		return Number;
	}
}