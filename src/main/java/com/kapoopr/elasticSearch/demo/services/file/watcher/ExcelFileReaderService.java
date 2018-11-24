package com.kapoopr.elasticSearch.demo.services.file.watcher;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.kapoopr.elasticSearch.demo.exceptions.FileParserException;
import com.kapoopr.elasticSearch.demo.services.file.parser.ExcelFileParser;
import com.kapoopr.elasticSearch.demo.util.FileUtil;

/**
 * This class is java Wtach Service. It reads a file from a location.
 * Process the files and convert it into a JSON file.
 *
 * @author pkapo7
 */

@Component
public class ExcelFileReaderService extends FileReaderService {

    final static Logger LOGGER = Logger.getLogger(ExcelFileReaderService.class);

    @Value(value = "${file.reader.excel.path:D:\\study\\excelReader}")
    private String path;
    @Value(value = "${file.processed.path:D:\\study\\processedFiles}")
    private String destination;
    @Value(value = "${file.processed.json.path:D:\\study\\jsonReader}")
    private String jsonDestination;
    @Value(value = "${json.root.name:employee}")
    private String jsonRootName;

    public String getJsonRootName() {
        return jsonRootName;
    }

    public void setJsonRootName(String jsonRootName) {
        this.jsonRootName = jsonRootName;
    }

    @Autowired
    private FileUtil fileUtil;
    @Autowired
    private ExcelFileParser fileParser;

    public ExcelFileParser getFileParser() {
        return fileParser;
    }

    public void setFileParser(ExcelFileParser fileParser) {
        this.fileParser = fileParser;
    }

    public FileUtil getFileUtil() {
        return fileUtil;
    }

    public void setFileUtil(FileUtil fileUtil) {
        this.fileUtil = fileUtil;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    @PostConstruct
    public void startUP() throws IOException {
        init(path);
        startReaderFile();
    }

    public void init(String path) throws IOException {
        super.init(path);
    }

    public ExcelFileReaderService() throws IOException {
        super();
    }

    @Override
    public void processFile() {
        Path fileName = null;

        while (true) {
            WatchKey key;
            try {
                key = getWatcher().take();
            } catch (InterruptedException ex) {
                return;
            }

            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();

                @SuppressWarnings("unchecked")
                WatchEvent<Path> ev = (WatchEvent<Path>) event;
                fileName = ev.context();

                LOGGER.debug("FileCreated:" + kind.name() + ": " + fileName);
                if (kind == ENTRY_CREATE && fileUtil.isExcelFile(fileName.toString())) {
                    try {
                        String json = fileParser.excelToJson(path + "/" + fileName, jsonRootName);
                        String fileStr = fileName.toString();
                        fileUtil.stringtoFile(
                                jsonDestination + "/" + fileStr.substring(0, fileStr.lastIndexOf('.')) + ".json", json);
                    } catch (IOException e) {
                        LOGGER.error(e.getMessage(), e);
                    } catch (FileParserException e) {
                        LOGGER.error(e.getMessage(), e);
                    } finally {
                        fileUtil.moveFile(path + "/" + fileName, destination);
                    }
                }

            }

            boolean valid = key.reset();
            if (!valid) {
                break;
            }
        }

    }
}
