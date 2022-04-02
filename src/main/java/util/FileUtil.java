package util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 文件操作的工具类
 */
public class FileUtil {


	/**
	 * 将数据写入到文件中
	 * @param data
	 * @param fileName
	 */
	public static void writeBytesToFile(byte[] data, String fileName) {
		File file = new File(fileName);
		if (file.exists()) {
			file.delete();
		}
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		try (FileOutputStream out = new FileOutputStream(file)) {
			for (int i = 0; i < data.length; i++) {
				out.write(data[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
