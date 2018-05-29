package com.skv.controller;

import com.skv.services.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("files")
public class UploadController {

    public static final Logger logger = LoggerFactory.getLogger(AccountController.class);

	@Autowired
	StorageService storageService;

	List<String> files = new ArrayList<>();

    @CrossOrigin
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
		String message;
		try {
			String filename = storageService.store(file);
			files.add(filename);

			//message = "You successfully uploaded " + file.getOriginalFilename() + "!";
			return ResponseEntity.status(HttpStatus.OK).body(filename);
		} catch (Exception e) {
			message = "FAIL to upload " + file.getOriginalFilename() + "!";
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
		}
	}

	@CrossOrigin
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<String>> getListFiles(Model model) {
		List<String> fileNames = files
				.stream().map(fileName -> MvcUriComponentsBuilder
						.fromMethodName(UploadController.class, "getFile", fileName).build().toString())
				.collect(Collectors.toList());

		return ResponseEntity.ok().body(fileNames);
	}

    @CrossOrigin
    @RequestMapping(value = "/{filename}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> getFilePath(@PathVariable String filename) {
        String filePath = MvcUriComponentsBuilder
                .fromMethodName(UploadController.class, "getFile", filename).build().toString();
        return ResponseEntity.ok().body(filePath);
    }

//	@CrossOrigin
//	@RequestMapping(value = "/{filename}", method = RequestMethod.GET)
//	@ResponseBody
	public ResponseEntity<Resource> getFile(@PathVariable String filename) {
		Resource file = storageService.loadFile(filename);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}
}
