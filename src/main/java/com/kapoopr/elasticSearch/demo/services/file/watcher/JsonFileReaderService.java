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

import com.kapoopr.elasticSearch.demo.services.elastic.writer.ElasticWriter;
import com.kapoopr.elasticSearch.demo.util.FileUtil;

/**
 * This class is java Wtach Service. It reads a file from a location.
 * Process the JSON files and push it into elastic search server using bulk api
 */
@Component
public class JsonFileReaderService extends FileReaderService {

    final static Logger LOGGER = Logger.getLogger(JsonFileReaderService.class);

    @Value(value = "${file.processed.json.path:D:\\study\\jsonReader}")
    private String path;

    @Value(value = "${file.processed.path:D:\\study\\processedFiles}")
    private String destination;
    @Autowired
    private FileUtil fileUtil;
    @Autowired
    private ElasticWriter elasticWriter;

    public ElasticWriter getElasticWriter() {
        return elasticWriter;
    }

    public void setElasticWriter(ElasticWriter elasticWriter) {
        this.elasticWriter = elasticWriter;
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

                if (kind == ENTRY_CREATE && fileUtil.isJsonFile(fileName.toString())) {
                    try {
                        elasticWriter.writeGenericDataModel(path + "/" + fileName, "bmcs");
                    } catch (Exception e) {
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
