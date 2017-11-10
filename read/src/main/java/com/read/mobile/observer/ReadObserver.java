package com.read.mobile.observer;

public class ReadObserver {

	/**
	 * 用户修改信息
	 * 
	 * @Title: UshareObserver.java
	 */
	public interface MondifyUserInfoSeccessObserver {
		public void handleUserInfo(String name, String signStr);
	}

	/**
	 * 头像上传
	 * 
	 * @Title: ReadObserver.java
	 * @Package: com.read.mobile.observer
	 */
	public interface UploadImgSeccessObserver {
		public void handleUserInfo(String avatar);
	}

	/**
	 * 
	 * @Title: UpdateNote.java
	 * @Package: com.read.mobile.observer
	 */
	public interface UpdateNote {
		public void handle(boolean isVoice);
	}

}
