package kozv.fs.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.Date;

/**
 * This class is responsible for cleaning the old / expired files.
 */
@Slf4j
@Component
@EnableScheduling
@AllArgsConstructor(onConstructor = @__({@Autowired}))
public class ScheduledOutdatedFilesCleaner {
    private static final String CRON_SCHEDULING_EXPRESSION = "0 0 0 * * ?";
    private static final String DEFAULT_FILE_EXPERAtION_PERIOD = "P2D";

    private final GridFsOperations gridFsOperations;
    private final FileCleanupProperties cleanupProperties;

    @Scheduled(cron = CRON_SCHEDULING_EXPRESSION)
    public void cleanOutdatedFiles() {
        final String fileExpirationPeriod = cleanupProperties.getFileExpirationPeriod();
        log.info("Outdated files cleanup was triggered. File older than {} will be removed.", fileExpirationPeriod);

        final Date dateThreshold = getDate(getDuration(fileExpirationPeriod));

        final Query deleteQuery = new Query(Criteria.where("uploadDate").lte(dateThreshold));
        gridFsOperations.delete(deleteQuery);

        log.info("Cleanup of outdated files is done.");
    }

    long getDuration(String fileExpirationPeriodVal) {
        try {
            return Duration.parse(fileExpirationPeriodVal).toMillis();
        } catch (DateTimeParseException e) {
            log.error("{} expression could not be parsed.", fileExpirationPeriodVal, e);
            // fallback to default value
            return getDuration(DEFAULT_FILE_EXPERAtION_PERIOD);
        }
    }

    private Date getDate(long duration) {
        ZoneId zoneId = ZoneId.systemDefault();
        long epoch = LocalDate.now().atStartOfDay(zoneId).toInstant().toEpochMilli();

        return new Date(epoch - duration);
    }

    String getCronExpression() {
        return CRON_SCHEDULING_EXPRESSION;
    }
}