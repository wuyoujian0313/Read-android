package com.read.mobile.observer;

import java.util.ArrayList;
import java.util.List;

public class ReadAgent {

	private static ReadAgent agent;

	/** 修改名称 */
	private static List<ReadObserver.MondifyUserInfoSeccessObserver> mondifyUserInfoSeccessObservers;
	private static List<ReadObserver.UploadImgSeccessObserver> addAnswerObservers;
	private static List<ReadObserver.UpdateNote> updateNotes;

	public synchronized static ReadAgent geUshareAgent() {
		if (agent == null) {
			agent = new ReadAgent();
		}
		return agent;
	}

	private ReadAgent() {
		mondifyUserInfoSeccessObservers = new ArrayList<ReadObserver.MondifyUserInfoSeccessObserver>();
		addAnswerObservers = new ArrayList<ReadObserver.UploadImgSeccessObserver>();
		updateNotes = new ArrayList<ReadObserver.UpdateNote>();
	}

	// ***********************************修改名称********************************************

	public void addMondifyNameSeccessObservers(ReadObserver.MondifyUserInfoSeccessObserver observer) {
		mondifyUserInfoSeccessObservers.add(observer);
	}

	public void removeMondifyNameSeccessObserver(ReadObserver.MondifyUserInfoSeccessObserver observer) {
		if (mondifyUserInfoSeccessObservers != null) {
			mondifyUserInfoSeccessObservers.remove(observer);
		}
	}

	/**
	 * 
	 * @param result
	 */
	public void notifyMondifyNameSeccessObservers(String name, String signStr) {
		if (mondifyUserInfoSeccessObservers != null) {
			for (int i = 0; i < mondifyUserInfoSeccessObservers.size(); i++) {
				mondifyUserInfoSeccessObservers.get(i).handleUserInfo(name, signStr);
			}
		}
	}

	// ***********************************设置账号成功********************************************

	public void addUploadImgSeccessObserverObserver(ReadObserver.UploadImgSeccessObserver observer) {
		addAnswerObservers.add(observer);
	}

	public void removeUploadImgSeccessObserverObserver(ReadObserver.UploadImgSeccessObserver observer) {
		if (addAnswerObservers != null) {
			addAnswerObservers.remove(observer);
		}
	}

	/**
	 * 
	 * @param result
	 */
	public void notifyUploadImgSeccessObserverObserver(String avatar) {
		if (addAnswerObservers != null) {
			for (int i = 0; i < addAnswerObservers.size(); i++) {
				addAnswerObservers.get(i).handleUserInfo(avatar);
				;
			}
		}
	}

	// ***********************************设置账号成功********************************************

	public void addUpdateNoteObserver(ReadObserver.UpdateNote observer) {
		updateNotes.add(observer);
	}

	public void removeUpdateNoteObserver(ReadObserver.UpdateNote observer) {
		if (updateNotes != null) {
			updateNotes.remove(observer);
		}
	}

	/**
	 * 
	 * @param result
	 */
	public void notifyUpdateNoteObserver(boolean isVoice) {
		if (updateNotes != null) {
			for (int i = 0; i < updateNotes.size(); i++) {
				updateNotes.get(i).handle(isVoice);
			}
		}
	}
}
