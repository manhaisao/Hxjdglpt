package com.xzz.hxjdglpt.model;

import java.util.Comparator;

import net.sourceforge.pinyin4j.PinyinHelper;
import android.os.Parcel;
import android.os.Parcelable;


public class Contacts implements Parcelable {

	private String name;

	private String email;

	private String telephone;

	private String identity;

	private String picName;

	private String qq;

	private char tag;

	public String getName() {
		return name;
	}

	public Contacts setName(String name) {
		this.name = name;
		return this;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public char getTag() {
		return tag;
	}

	public void setTag(char tag) {
		this.tag = tag;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public String getPicName() {
		return picName;
	}

	public void setPicName(String picName) {
		this.picName = picName;
	}

	public String getTelephone() {
		return telephone;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public static class ContactsComparactor implements Comparator<Contacts> {

		@Override
		public int compare(Contacts contacts1, Contacts contacts2) {
			String name1 = contacts1.getName();
			String name2 = contacts2.getName();
			char char1 = upChar(getChar(name1.charAt(0)));
			char char2 = upChar(getChar(name2.charAt(0)));
			contacts1.setTag(char1);
			contacts2.setTag(char2);
			return char1 - char2;
		}

		private char getChar(char chineseCharacter) {

			String[] str = PinyinHelper
					.toHanyuPinyinStringArray(chineseCharacter);
			if (str == null) {
				return chineseCharacter;
			}
			// Log.i("whw", "str[0]="+str[0]);
			return str[0].charAt(0);
		}

		private char upChar(char c) {
			String str = String.valueOf(c).toUpperCase();
			return str.charAt(0);
		}

	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flag) {
		out.writeString(name);
		out.writeString(email);
		out.writeString(telephone);
		out.writeString(identity);
		out.writeString(picName);
		out.writeString(qq);

	}

	public static final Creator<Contacts> CREATOR = new Creator<Contacts>() {

		@Override
		public Contacts createFromParcel(Parcel in) {
			Contacts contacts = new Contacts();
			contacts.setName(in.readString());
			contacts.setEmail(in.readString());
			contacts.setTelephone(in.readString());
			contacts.setIdentity(in.readString());
			contacts.setPicName(in.readString());
			contacts.setQq(in.readString());
			return contacts;
		}

		@Override
		public Contacts[] newArray(int size) {
			// TODO Auto-generated method stub
			return new Contacts[size];
		}
	};

}
