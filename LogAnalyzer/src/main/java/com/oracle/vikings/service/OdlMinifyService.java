package com.oracle.vikings.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Service
@PropertySource("classpath:custom.properties")
public class OdlMinifyService {

	@Autowired
	FileService fileService;

	@Value("${saveDir}")
	String SAVE_DIR;
	@Value("${unzippedDir}")
	String UNZIPPED_DIR;
	@Value("${processDir}")
	String processDir;
	@Value("${not.req.list.minify}")
	List<String> lstNotRec;

	List<String> notReqLines = new ArrayList<String>();

	@PostConstruct
	public void createDirIfNotExists() {
		System.out.println(SAVE_DIR + "..." + UNZIPPED_DIR + "..." + processDir);
	}

	public Map<String, Object> processFile(File f) {
		
		fileService.increaseCounter();
		
		Map<String, Object> resContent = new HashMap<String, Object>(); 
		
		fileService.createDirIfNotExists();
		
		List<String> lstErrors = new ArrayList<String>();
		Map<String, Object> incError = new HashMap<String, Object>();
		
		File file = null;
		try (BufferedReader b = new BufferedReader(new FileReader(f))) {
			File dir = new File(processDir + File.separator);
			if (!(dir.exists() && dir.isDirectory())) {
				new File(processDir).mkdir();
			}

//			System.out.println("Absolute Path: " + f.getAbsolutePath());

			BufferedWriter write = new BufferedWriter(new FileWriter(processDir + File.separator + f.getName()));
			Date startTime = new Date();
			String str;

			int counter = 0;
			boolean skipLine = false;
			while ((str = b.readLine()) != null) {
				int len = str.length();
				if (len > 0) {
					// Don't add blank line if previous line was skipped
					if (!skipLine && (str.charAt(0) != '[' || (!str.contains("]")))) {
						write.append(str);
					} else {
						// Check if line contains any unnecessary data
						skipLine = skipLine(str);
						if (!skipLine) {
							// Add Time Stamp
							write.append(str.substring(0, str.indexOf(']') + 1));

							// If Execute Query
							if (str.contains("Execute query") && str.contains("ADF_MESSAGE_CONTEXT_DATA")) {
								int startIndex = str.lastIndexOf("ADF_MESSAGE_CONTEXT_DATA");
								int endIndex = str.lastIndexOf("Component");
								write.append(str.substring(startIndex, endIndex));
							}
							
							int index = str.lastIndexOf(']');
							if(len-index<4)
								index = str.lastIndexOf("log]")+3;
							
							String tempStr = str.substring(index + 1, len);
							
							//Generate List of error message
							if(tempStr.toLowerCase().contains(" error")) {
								lstErrors.add(tempStr);
							}
							
							if(str.contains("INCIDENT_ERROR")) {
								String logFile = str.substring(str.lastIndexOf("LOG_FILE"),str.lastIndexOf(".log")+4);
								//incError
								incError.put(tempStr,logFile);
							}							
							write.append(tempStr);
						}
					}
				}
				counter++;
//				System.out.println(counter);
				if (!skipLine)
					write.append("\n");
			}
			Date endTime = new Date();
			System.out.println("\n\nLine processed: " + counter + "\n=========\nTotal time(Type 2): "
					+ (endTime.getTime() - startTime.getTime()) / 1000);
			/*
			 * =========Total time(Type 1): 79
			 */
			file = new File(processDir + File.separator + f.getName());
			
			File tempFile = fileService.zipFile(file);
			lstErrors.add(tempFile.getName());
			
			resContent.put("lstError", lstErrors);
			resContent.put("fileName", tempFile.getName());
			resContent.put("incError", incError);
			
			return resContent;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	private boolean skipLine(String line) {
		return lstNotRec.parallelStream().anyMatch(line::contains);
	}
}
