package com.jbk.exception;

public class CustomExceptionResponse {
private String path;
private String timestamp;
private String msg;
public CustomExceptionResponse() {
	super();
	// TODO Auto-generated constructor stub
}
public CustomExceptionResponse(String path, String timestamp, String msg) {
	super();
	this.path = path;
	this.timestamp = timestamp;
	this.msg = msg;
}
public String getPath() {
	return path;
}
public void setPath(String path) {
	this.path = path;
}
public String getTimestamp() {
	return timestamp;
}
public void setTimestamp(String timestamp) {
	this.timestamp = timestamp;
}
public String getMsg() {
	return msg;
}
public void setMsg(String msg) {
	this.msg = msg;
}
@Override
public String toString() {
	return "CustomExceptionResponse [path=" + path + ", timestamp=" + timestamp + ", msg=" + msg + "]";
}

}
