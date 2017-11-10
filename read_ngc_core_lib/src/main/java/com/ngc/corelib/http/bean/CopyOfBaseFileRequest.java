package com.ngc.corelib.http.bean;

import java.io.File;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

public class CopyOfBaseFileRequest extends BaseRequest {

	private File[] file;

	public File[] getFile() {
		return file;
	}

	public void setFile(File[] file) {
		this.file = file;
	}

	private List<BasicNameValuePair> paramsList;

	public List<BasicNameValuePair> getParamsList() {
		return paramsList;
	}

	public void setParamsList(List<BasicNameValuePair> paramsList) {
		this.paramsList = paramsList;
	}

}
