package com.kapoopr.elasticSearch.demo.services.file.watcher;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchService;

import org.apache.log4j.Logger;

abstract class FileReaderService {

	final static Logger LOGGER = Logger.getLogger(FileReaderService.class);
	private WatchService watcher;

	public void init(String source) throws IOException {
		watcher = FileSystems.getDefault().newWatchService();
		Path dir = Paths.get(source);
		dir.register(watcher, ENTRY_CREATE);
		LOGGER.info("Watch Service registered for dir: " + dir.getFileName());
	}

	abstract public void processFile();

	public WatchService getWatcher() {
		return watcher;
	}

	public void setWatcher(WatchService watcher) {
		this.watcher = watcher;
	}

	public void startReaderFile() {
		Thread thread = new Thread(new Runnable() {
			public void run() {
				processFile();

			}
		});
		thread.setDaemon(true);
		thread.start();

	}

}
