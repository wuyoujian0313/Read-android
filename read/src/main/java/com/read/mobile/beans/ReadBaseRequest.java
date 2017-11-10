package com.read.mobile.beans;

import com.ngc.corelib.http.bean.BaseRequest;
import com.read.mobile.utils.SaveUtils;

public class ReadBaseRequest extends BaseRequest {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4307438188012823504L;
	protected String sign;
	protected String u = SaveUtils.getUserId();

	public String getU() {
		return u;
	}

	public void setU(String u) {
		this.u = u;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

}
