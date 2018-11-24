package com.kapoopr.elasticSearch.demo.job;

import java.io.File;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.AgeFileFilter;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * This class will delete the processed file after certain time based on file deletionTimePeriod parameter
 */
@Service
public class FileDeletionService {
    final static Logger LOGGER = Logger.getLogger(FileDeletionService.class);
    @Value(value = "${file.processed.path:D:\\study\\processedFiles}")
    private String directoryPath;
    @Value("${file.deletion.time.period:1}")
    private Integer deletionTimePeriod;

    public Integer getDeletionTimePeriod() {
        return deletionTimePeriod;
    }


    public void setDeletionTimePeriod(Integer deletionTimePeriod) {
        this.deletionTimePeriod = deletionTimePeriod;
    }

    public String getDirectoryPath() {
        return directoryPath;
    }

    public void setDirectoryPath(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    @Scheduled(cron = "${file.deletion.cron.expression}")
    public void scheduleTaskUsingCronExpression() {

        LOGGER.debug("FileDeletion Started");
        deleteFiles();
        LOGGER.debug("FileDeletion completed");
    }

    private void deleteFiles() {
        Date oldestAllowedFileDate = DateUtils.addDays(new Date(), deletionTimePeriod); // minus

        Iterator<File> filesToDelete = FileUtils.iterateFiles(FileUtils.getFile(directoryPath),
                new AgeFileFilter(oldestAllowedFileDate), null);
        while (filesToDelete.hasNext()) {
            FileUtils.deleteQuietly(filesToDelete.next());
        }
    }
}
