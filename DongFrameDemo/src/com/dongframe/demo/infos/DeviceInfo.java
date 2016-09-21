package com.dongframe.demo.infos;

import java.io.Serializable;

public class DeviceInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6750553736029771803L;

	private int id = -1;

	/**
	 * 备注
	 */
	private String remark;
	
	/**
	 * 生厂商
	 */
	private String vendor;

	private String ipAddr;

	private String macAddr;

	private String device;

	private String brand;

	private String type;

	private String flags;

	private String mask;

	/**
	 * 是否mac与device匹配过，1:匹配
	 */
	private int isMatch = 0;

	/**
	 * 设备类型 1,手机 2,pad 3,电脑
	 */
	private String devicety;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getIpAddr() {
		return ipAddr;
	}

	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}

	public String getMacAddr() {
		return macAddr;
	}

	public void setMacAddr(String macAddr) {
		this.macAddr = macAddr;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFlags() {
		return flags;
	}

	public void setFlags(String flags) {
		this.flags = flags;
	}

	public String getMask() {
		return mask;
	}

	public void setMask(String mask) {
		this.mask = mask;
	}

	public int getIsMatch() {
		return isMatch;
	}

	public void setIsMatch(int isMatch) {
		this.isMatch = isMatch;
	}

	public String getDevicety() {
		return devicety;
	}

	public void setDevicety(String devicety) {
		this.devicety = devicety;
	}

}
