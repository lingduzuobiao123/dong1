package com.dongframe.demo.infos;

import java.io.Serializable;

public class Software implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8965806097168761951L;
	private String title = "";
	private String info = "";
	private String versionName = "";
	private int versionCode = 0;
	private String packName = "";
	private long apkSize = 0;
	private String updateUrl = "";
	private int forceUpd = 0;//1表示强制升级
	private int upgradeMode = 0;//升级方式

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public long getApkSize() {
		return apkSize;
	}

	public void setApkSize(long apkSize) {
		this.apkSize = apkSize;
	}

	public String getUpdateUrl() {
		return updateUrl;
	}

	public void setUpdateUrl(String updateUrl) {
		this.updateUrl = updateUrl;
	}

	public String getPackName() {
		return packName;
	}

	public void setPackName(String packName) {
		this.packName = packName;
	}

	public int getForceUpd() {
		return forceUpd;
	}

	public void setForceUpd(int forceUpd) {
		this.forceUpd = forceUpd;
	}

	public int getUpgradeMode() {
		return upgradeMode;
	}

	public void setUpgradeMode(int upgradeMode) {
		this.upgradeMode = upgradeMode;
	}

}
