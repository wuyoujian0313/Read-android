package com.ngc.corelib.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringReader;
import java.util.Properties;

import android.content.Context;

/**
 * 文件工具类
 * 
 */
public class FileUtils {

	private static final int BUFFER_SIZE = 1024;

	/**
	 * 从输入流保存文件
	 * 
	 * @param filePath
	 * @param is
	 * @return
	 */
	public static boolean writeInputStreamFile(String filePath, InputStream is) {
		boolean isSuccess = false;
		File distFile = new File(filePath);
		if (!distFile.getParentFile().exists()) {
			distFile.getParentFile().mkdirs();
		}
		BufferedInputStream bis = new BufferedInputStream(is, BUFFER_SIZE);
		BufferedOutputStream bos = null;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(filePath), BUFFER_SIZE);
			byte[] buffer = new byte[BUFFER_SIZE];
			int num = -1;
			while (true) {
				num = bis.read(buffer);
				if (num == -1) {
					bos.flush();
					break;
				}
				bos.flush();
				bos.write(buffer, 0, num);
			}
			isSuccess = true;
		} catch (Exception e) {
			Logger.e("save " + filePath + " failed!");
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
				}
			}
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
				}
			}
		}
		return isSuccess;
	}

	/**
	 * 从输入流转换为二进制
	 * 
	 * @param inputstream
	 * @return
	 */
	public static byte[] readBytes(InputStream inputstream) {
		BufferedInputStream in = new BufferedInputStream(inputstream);
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		// 创建一个Buffer字符串
		byte[] buffer = new byte[1024];
		// 每次读取的字符串长度，如果为-1，代表全部读取完毕
		int len = 0;
		// 使用一个输入流从buffer里把数据读取出来
		try {
			while ((len = in.read(buffer)) != -1) {
				// 用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
				outStream.write(buffer, 0, len);
			}
		} catch (IOException e) {
		} finally {
			// 关闭输入流
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		// 把outStream里的数据写入内存
		return outStream.toByteArray();
	}

	/**
	 * 从二进制写到文件
	 * 
	 * @param filePath
	 * @param bytes
	 */
	public static void writeByteFile(String filePath, byte[] bytes) {
		File distFile = new File(filePath);
		if (!distFile.getParentFile().exists()) {
			distFile.getParentFile().mkdirs();
		}
		BufferedOutputStream bos = null;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(filePath), BUFFER_SIZE);
			bos.write(bytes);
		} catch (Exception e) {
			Logger.e("save " + filePath + " failed!");
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
				}
			}
		}
	}

	/**
	 * 把字符写到文件中
	 * 
	 * @param data
	 * @param filePath
	 */
	public static void writeTextFile(String data, String filePath) {
		File distFile = new File(filePath);
		if (!distFile.getParentFile().exists()) {
			distFile.getParentFile().mkdirs();
		}
		BufferedReader bufferedReader = null;
		BufferedWriter bufferedWriter = null;
		try {
			bufferedReader = new BufferedReader(new StringReader(data));
			bufferedWriter = new BufferedWriter(new FileWriter(distFile));
			int len = -1;
			char buf[] = new char[BUFFER_SIZE];
			while ((len = bufferedReader.read(buf)) != -1) {
				bufferedWriter.write(buf, 0, len);
			}
			bufferedWriter.flush();
		} catch (IOException e) {
			Logger.e("write " + filePath + " data failed!");
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
				}
			}
			if (bufferedWriter != null) {
				try {
					bufferedWriter.close();
				} catch (IOException e) {
				}
			}
		}
	}

	/**
	 * 从文件中读取字符
	 * 
	 * @param filePath
	 * @return
	 */
	public static String readTextFile(String filePath) {
		File file = new File(filePath);
		BufferedReader reader = null;
		StringBuilder sb = new StringBuilder();
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				sb.append(tempString);
			}
			reader.close();
		} catch (IOException e) {
			Logger.e("readTextFile " + filePath + " failed!");
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 把对象序列化到文件中
	 * 
	 * @param filePath
	 * @param obj
	 */
	public static void serialize(String filePath, Object obj) {
		File distFile = new File(filePath);
		if (!distFile.getParentFile().exists()) {
			distFile.getParentFile().mkdirs();
		}
		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(new FileOutputStream(filePath));
			out.writeObject(obj);
		} catch (Exception e) {
			Logger.e("serialize " + filePath + " failed!");
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 从文件中反序列化到对象
	 * 
	 * @param filePath
	 * @return
	 */
	public static Object deserialize(String filePath) {
		ObjectInputStream in = null;
		try {
			in = new ObjectInputStream(new FileInputStream(filePath));
			return in.readObject();
		} catch (Exception e) {
			Logger.e("deserialize " + filePath + " failed!");
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 * 递归取得文件夹大小
	 * 
	 * @param f
	 * @return
	 * @throws Exception
	 */
	public static long getFileSize(File f) {
		long size = 0;
		File flist[] = f.listFiles();
		if (flist != null) {
			for (int i = 0; i < flist.length; i++) {
				if (flist[i].isDirectory()) {
					size = size + getFileSize(flist[i]);
				} else {
					size = size + flist[i].length();
				}
			}
		}
		return size;
	}

	/**
	 * 把大小格式化输出
	 * 
	 * @param value
	 * @return "B", "KB", "MB", "GB", "TB"
	 */
	public static String prettyBytes(long value) {
		String args[] = { "B", "KB", "MB", "GB", "TB" };
		StringBuilder sb = new StringBuilder();
		int i;
		if (value < 1024L) {
			sb.append(String.valueOf(value));
			i = 0;
		} else if (value < 1048576L) {
			sb.append(String.format("%.1f", value / 1024.0));
			i = 1;
		} else if (value < 1073741824L) {
			sb.append(String.format("%.2f", value / 1048576.0));
			i = 2;
		} else if (value < 1099511627776L) {
			sb.append(String.format("%.3f", value / 1073741824.0));
			i = 3;
		} else {
			sb.append(String.format("%.4f", value / 1099511627776.0));
			i = 4;
		}
		sb.append(' ');
		sb.append(args[i]);
		return sb.toString();
	}

	/**
	 * 删除一个目录（可以是非空目录）
	 * 
	 * @param dir
	 * @param o
	 *            是否删除自己, true：删除; false：保留
	 */
	public static boolean delDir(File dir, boolean o) {
		boolean delFlag = true;
		if (dir == null || !dir.exists() || dir.isFile()) {
			return false;
		}
		for (File file : dir.listFiles()) {
			if (file.isFile()) {
				if (!file.delete()) {
					Logger.e("delete file error, file name is " + file.getName());
					if (delFlag) {
						delFlag = false;
					}
				}
			} else if (file.isDirectory()) {
				boolean childFlag = delDir(file, true);// 递归
				if (delFlag && !childFlag) {
					Logger.e("delete file error, file name is " + file.getName());
					delFlag = false;
				}
			}
		}
		if (o) {
			dir.delete();
		}
		return delFlag;
	}

	public static Properties getProperties(Context c) {
		Properties props = new Properties();
		try {
			// 方法一：通过activity中的context攻取setting.properties的FileInputStream
			InputStream in = c.getAssets().open("masterkey.properties ");
			// 方法二：通过class获取setting.properties的FileInputStream
			// InputStream in =
			// PropertiesUtill.class.getResourceAsStream("/assets/  setting.properties "));
			props.load(in);
			in.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return props;
	}

}
