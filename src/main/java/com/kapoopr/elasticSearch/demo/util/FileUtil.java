package com.kapoopr.elasticSearch.demo.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public final class FileUtil {

	final static Logger LOGGER = Logger.getLogger(FileUtil.class);

	public void moveFile(String source, String destination) {
		String fileName = FilenameUtils.getBaseName(source);
		String extension = FilenameUtils.getExtension(source);
		destination = destination + "/" + fileName + "-" + UUID.randomUUID() + "." + extension;
		try {
			FileUtils.moveFile(FileUtils.getFile(source), FileUtils.getFile(destination));
		} catch (IOException e) {
			LOGGER.debug("File failed to be moved");
		}
		LOGGER.debug("File is moved successful!");

	}

	public HashMap<String, List<Map<String, Object>>> fileToJson(String file)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		File from = new File(file);
		TypeReference<HashMap<String, List<Map<String, Object>>>> typeRef = new TypeReference<HashMap<String, List<Map<String, Object>>>>() {
		};

		HashMap<String, List<Map<String, Object>>> o = mapper.readValue(from, typeRef);
		return o;

	}

	public byte[] fileToBytes(String file) throws IOException {
		InputStream inputStream = new FileInputStream(file);
		return IOUtils.toByteArray(inputStream);

	}

	public void stringtoFile(String file, String data) throws IOException {
		FileUtils.writeStringToFile(FileUtils.getFile(file), data, "UTF-8");
	}

	public boolean isExcelFile(String fileName) {
		return FilenameUtils.isExtension(fileName, new String[] { "xlsx", "xls" });
	}

	public boolean isJsonFile(String fileName) {
		return FilenameUtils.isExtension(fileName, "json");

	}
}
