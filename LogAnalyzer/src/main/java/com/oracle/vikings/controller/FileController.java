package com.oracle.vikings.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.oracle.vikings.service.FileService;
import com.oracle.vikings.service.OdlMinifyService;
import com.oracle.vikings.service.PlSqlService;

@Controller
@PropertySource("classpath:custom.properties")
public class FileController {

	@Autowired
	FileService fileService;
	@Autowired
	OdlMinifyService odlMinifyService;
	@Autowired
	PlSqlService plSqlService;

	@GetMapping("/upload")
	public String welcome(Model model) {
		model.addAttribute("message", "Welcome to logAnalyzer!");
		return "upload";
	}

	@GetMapping("/LogAnalyzer/download")
	public ResponseEntity<Object> downloadFile(@RequestParam("fileName") String fileName) throws FileNotFoundException {

		String filePath = fileService.getDownloadFilePath() + File.separator + fileName;

		File file = new File(filePath);
		InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
		HttpHeaders headers = new HttpHeaders();

		headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");

		ResponseEntity<Object> responseEntity = ResponseEntity.ok().headers(headers).contentLength(file.length())
				.contentType(MediaType.parseMediaType("application/txt")).body(resource);

		return responseEntity;

	}

	@RequestMapping(value = "/LogAnalyzer/minifyOdl", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String minifyOdl(@RequestParam("file") MultipartFile fileIn, Model model) throws IOException {

		fileService.uploadFile(fileIn);

		String fileName = fileIn.getOriginalFilename();

		File file = fileService.extractFile(fileName, "saveDir");

		// Processing ODL logs
		Map<String, Object> lstMsgs = odlMinifyService.processFile(file);

		model.addAttribute("filePath", lstMsgs.get("fileName"));
		model.addAttribute("lstError", lstMsgs.get("lstError"));
		model.addAttribute("incError", lstMsgs.get("incError"));

		return "minifyOdl";
	}

	@RequestMapping(value = "/LogAnalyzer/procPlSql", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String processPlSql(@RequestParam("file") MultipartFile fileIn, Model model) throws IOException {

		fileService.uploadFile(fileIn);

		String fileName = fileIn.getOriginalFilename();

		File file = fileService.extractFile(fileName, "saveDir");

		// Processing PLSQL logs returns list of procedure which are failed
		LinkedHashMap<String, String> listProc = plSqlService.processPlSqlData(file);

		model.addAttribute("listProc", listProc);

		return "plsql";
	}

}
