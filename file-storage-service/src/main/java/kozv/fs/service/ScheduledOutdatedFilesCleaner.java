package kozv.fs.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * This class is responsible for cleaning the old / expired files.
 */
@Slf4j
@Component
@EnableScheduling
@AllArgsConstructor(onConstructor = @__({@Autowired}))
public class ScheduledOutdatedFilesCleaner {
    static final String CRON_SCHEDULING_EXPRESSION = "0 0 0 * * ?";

    private final GridFsOperations gridFsOperations;
    private final FileCleanupProperties cleanupProperties;

    @Scheduled(cron = CRON_SCHEDULING_EXPRESSION)
    public void cleanOutdatedFiles() throws ParseException {
        log.info("Outdated files cleanup was triggered.");

        final Date dateThreshold = getDate(cleanupProperties.getFileExpirationPeriodVal());

        final Query deleteQuery = new Query(Criteria.where("uploadDate").lte(dateThreshold));
        gridFsOperations.delete(deleteQuery);

        log.info("Cleanup of outdated files is done.");
    }

    Date getDate(String fileExpirationPeriodVal) throws ParseException {
        final long duration = Duration.parse(fileExpirationPeriodVal).toMillis();

        ZoneId zoneId = ZoneId.systemDefault();
        long epoch = LocalDate.now().atStartOfDay(zoneId).toInstant().toEpochMilli();

        return new Date(epoch - duration);
    }

    String getCronExpression() {
        return CRON_SCHEDULING_EXPRESSION;
    }
}