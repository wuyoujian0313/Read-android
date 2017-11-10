package com.ngc.corelib.utils;

import android.content.Context;

/**
 * 获取资源Id帮助类
 * 
 * @author Hkrt_xLuo
 */

public class ResourceUtil {

	/**
	 * 获取layout资源Id
	 * 
	 * @param paramContext
	 *            Context
	 * @param paramString
	 *            资源名称
	 * @return 资源ID
	 */
	public static int getLayoutId(Context paramContext, String paramString) {
		return paramContext.getResources().getIdentifier(paramString, "layout", paramContext.getPackageName());
	}

	/**
	 * 获取string资源Id
	 * 
	 * @param paramContext
	 *            Context
	 * @param paramString
	 *            资源名称
	 * @return 资源ID
	 */
	public static int getStringId(Context paramContext, String paramString) {
		return paramContext.getResources().getIdentifier(paramString, "string", paramContext.getPackageName());
	}

	/**
	 * 获取drawable资源Id
	 * 
	 * @param paramContext
	 *            Context
	 * @param paramString
	 *            资源名称
	 * @return 资源ID
	 */
	public static int getDrawableId(Context paramContext, String paramString) {
		return paramContext.getResources().getIdentifier(paramString, "drawable", paramContext.getPackageName());
	}

	/**
	 * 获取style资源Id
	 * 
	 * @param paramContext
	 *            Context
	 * @param paramString
	 *            资源名称
	 * @return 资源ID
	 */
	public static int getStyleId(Context paramContext, String paramString) {
		return paramContext.getResources().getIdentifier(paramString, "style", paramContext.getPackageName());
	}

	/**
	 * 获取styleable资源Id
	 * 
	 * @param paramContext
	 *            Context
	 * @param paramString
	 *            资源名称
	 * @return 资源ID
	 */
	public static int getStyleableId(Context paramContext, String paramString) {
		return paramContext.getResources().getIdentifier(paramString, "styleable", paramContext.getPackageName());
	}

	/**
	 * 获取资源Id
	 * 
	 * @param paramContext
	 *            Context
	 * @param paramString
	 *            资源名称
	 * @return ID
	 */
	public static int getId(Context paramContext, String paramString) {
		return paramContext.getResources().getIdentifier(paramString, "id", paramContext.getPackageName());
	}

	/**
	 * 获取资源dimen
	 * 
	 * @param paramContext
	 *            Context
	 * @param paramString
	 *            资源名称
	 * @return ID
	 */
	public static int getDimenId(Context paramContext, String paramString) {
		return paramContext.getResources().getIdentifier(paramString, "dimen", paramContext.getPackageName());
	}

	/**
	 * 获取color资源Id
	 * 
	 * @param paramContext
	 *            Context
	 * @param paramString
	 *            资源名称
	 * @return 资源ID
	 */
	public static int getColorId(Context paramContext, String paramString) {
		return paramContext.getResources().getIdentifier(paramString, "color", paramContext.getPackageName());
	}

	/**
	 * 获取raw资源Id
	 * 
	 * @param paramContext
	 *            Context
	 * @param paramString
	 *            资源名称
	 * @return 资源ID
	 */
	public static int getRawId(Context paramContext, String paramString) {
		return paramContext.getResources().getIdentifier(paramString, "raw", paramContext.getPackageName());
	}

	/**
	 * 获取anim资源Id
	 * 
	 * @param paramContext
	 *            Context
	 * @param paramString
	 *            资源名称
	 * @return 资源ID
	 */
	public static int getAnimId(Context paramContext, String paramString) {
		return paramContext.getResources().getIdentifier(paramString, "anim", paramContext.getPackageName());
	}

	public static int getIdByName(Context context, String className, String name) {
		String packageName = context.getPackageName();
		Class r = null;
		int id = 0;
		try {
			r = Class.forName(packageName + ".R");

			Class[] classes = r.getClasses();
			Class desireClass = null;

			for (int i = 0; i < classes.length; ++i) {
				if (classes[i].getName().split("\\$")[1].equals(className)) {
					desireClass = classes[i];
					break;
				}
			}

			if (desireClass != null)
				id = desireClass.getField(name).getInt(desireClass);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}

		return id;
	}

	public static int[] getIdsByName(Context context, String className, String name) {
		String packageName = context.getPackageName();
		Class r = null;
		int[] ids = null;
		try {
			r = Class.forName(packageName + ".R");

			Class[] classes = r.getClasses();
			Class desireClass = null;

			for (int i = 0; i < classes.length; ++i) {
				if (classes[i].getName().split("\\$")[1].equals(className)) {
					desireClass = classes[i];
					break;
				}
			}

			if ((desireClass != null) && (desireClass.getField(name).get(desireClass) != null)
					&& (desireClass.getField(name).get(desireClass).getClass().isArray()))
				ids = (int[]) desireClass.getField(name).get(desireClass);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}

		return ids;
	}
}
