package com.taoy3.db;

import android.app.Activity;
import android.os.SystemClock;

public class Person {
	private int age;
	private String name;
	private String sex;
	private int id;
	private static String[] names;
	private static String[] ages;
	private static String[] sexes;
	public Person(Activity activity) {
		if(names==null){
			names = activity.getResources().getStringArray(R.array.names);
			ages = activity.getResources().getStringArray(R.array.age);
			sexes = activity.getResources().getStringArray(R.array.sex);
		}
		this.age = Integer.parseInt(ages[(int) (SystemClock.currentThreadTimeMillis()%ages.length)]);
		this.name = names[(int) (SystemClock.currentThreadTimeMillis()%names.length)];
		this.sex = sexes[(int) (SystemClock.currentThreadTimeMillis()%sexes.length)];
	}
	public Person(int id,int age, String name, String sex) {
		super();
		this.id = id;
		this.age = age;
		this.name = name;
		this.sex = sex;
	}
	public int getId(){
		return id;
	}
	public void setId(int id){
		this.id = id;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	@Override
	public String toString() {
		return "Person [age=" + age + ", name=" + name + ", sex=" + sex + "]";
	}
	
}
