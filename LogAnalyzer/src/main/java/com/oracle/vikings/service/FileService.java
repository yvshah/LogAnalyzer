package com.oracle.vikings.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.annotation.PostConstruct;
import javax.servlet.http.Part;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@PropertySource("classpath:custom.properties")
public class FileService {

	@Value("${saveDir}")
	String SAVE_DIR;
	@Value("${unzippedDir}")
	String UNZIPPED_DIR;
	@Value("${processDir}")
	String processDir;
	@Value("${request.log.dir}")
	String reqLogDir;

	@PostConstruct
	public void createDirIfNotExists() {
		createDir(SAVE_DIR);
		createDir(UNZIPPED_DIR);
		createDir(processDir);
		createDir(reqLogDir);
	}

	public void increaseCounter() {
		String fileName = reqLogDir + File.separator+"reqCounter.properties";
		FileOutputStream out;
		FileInputStream in;
		try {
			File file = new File(fileName);
			if (!file.exists()) {

				file.createNewFile();

			}
			in = new FileInputStream(fileName);
			Properties props = new Properties();
			props.load(in);
			in.close();

			int counter = 0;
			out = new FileOutputStream(fileName);
			if (props.containsKey("counter"))
				counter = Integer.parseInt(props.get("counter").toString());
			props.setProperty("counter", String.valueOf(++counter));
			props.store(out, null);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	};

	public boolean uploadFile(MultipartFile fileIn) throws IOException {

		File convertFile = new File(SAVE_DIR + File.separator + fileIn.getOriginalFilename());
		convertFile.createNewFile();
		FileOutputStream fout = new FileOutputStream(convertFile);
		fout.write(fileIn.getBytes());
		fout.close();

		return true;
	}

	public String getDownloadFilePath() {
		return processDir;
	}

	/**
	 * Extracts file name from HTTP header content-disposition
	 */
	public String extractFileName(Part part) {
		String contentDisp = part.getHeader("content-disposition");
		String[] items = contentDisp.split(";");
		for (String s : items) {
			if (s.trim().startsWith("filename")) {
				return s.substring(s.indexOf("=") + 2, s.length() - 1);
			}
		}
		return "";
	}

	public void createDir(String filePath) {
		File fileSaveDir = new File(filePath);
		System.out.println(fileSaveDir.getAbsolutePath());
		if (!fileSaveDir.exists()) {
			fileSaveDir.mkdir();
			System.out.println("Directory created!");
		}
		System.out.println(fileSaveDir.exists());
	}

	public File zipFile(File file) {
		if (null == file) {
			return null;
		}
		String fileName = processDir + File.separator + file.getName();
//		File processedFile = new File(fileName);
		FileOutputStream fos;
		File zippedFile = null;
		try {
			fos = new FileOutputStream(fileName + ".zip");
			ZipOutputStream zos = new ZipOutputStream(fos);

			ZipEntry entry = new ZipEntry(file.getName());
			zos.putNextEntry(entry);

			FileInputStream fis = new FileInputStream(file);
			byte[] buffer = new byte[1024];
			int len;
			while ((len = fis.read(buffer)) > 0) {
				zos.write(buffer, 0, len);
			}
			zippedFile = new File(fileName + ".zip");
			zos.closeEntry();
			// Close resources
			zos.close();
			fis.close();
			fos.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return zippedFile; // Needs to change
	}

	public File extractFile(String fileName, String savePath) {

//		fileName = SAVE_DIR+File.separator+fileName;

		savePath = SAVE_DIR;

		File file = new File(savePath + File.separator + fileName);
		String absolutePath = file.getAbsolutePath();
		File newFile = null;
		FileInputStream fis;
		int index = file.getName().lastIndexOf('.');
		String fileType = null;
		if (index > 0)
			fileType = file.getName().substring(index + 1);
		if (null != fileType) {
			if (fileType.equals("zip")) {
				try {
					fis = new FileInputStream(absolutePath);
					ZipInputStream zis = new ZipInputStream(fis);
					ZipEntry entry = zis.getNextEntry();
					if (entry != null) {
						String unzippedFileName = entry.getName();
						FileOutputStream fos = new FileOutputStream(UNZIPPED_DIR + File.separator + unzippedFileName);
						int len;
						byte[] buffer = new byte[1024];
						while ((len = zis.read(buffer)) > 0) {
							fos.write(buffer, 0, len);
						}
						fos.close();
						newFile = new File(UNZIPPED_DIR + File.separator + unzippedFileName);
					}

					zis.closeEntry();
					zis.close();
					fis.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		if (null != newFile) {
			return newFile;
		}
		return file;
	}

}
