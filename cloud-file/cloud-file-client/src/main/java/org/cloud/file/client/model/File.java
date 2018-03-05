package org.cloud.file.client.model;

public class File {
	
	private long claid;
	private String type;//文件类型
	private String code;//编码
	private String name;//名称
	private String dir;//路径
	private double size;//大小
	private String format;//格式
	private long time;//时间
	
	public long getClaid() {
		return claid;
	}
	public void setClaid(long claid) {
		this.claid = claid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDir() {
		return dir;
	}
	public void setDir(String dir) {
		this.dir = dir;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public double getSize() {
		return size;
	}
	public void setSize(double size) {
		this.size = size;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	
}
