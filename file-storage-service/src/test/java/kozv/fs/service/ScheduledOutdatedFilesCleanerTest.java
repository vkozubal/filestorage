package kozv.fs.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.test.util.ReflectionTestUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.Map;

import static java.time.ZoneId.systemDefault;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ScheduledOutdatedFilesCleanerTest {

    @Captor
    private ArgumentCaptor<Query> captor;
    private ScheduledOutdatedFilesCleaner cleaner;
    @Mock
    private GridFsOperations gridFsOperationsMock;

    @Before
    public void setup() {
        cleaner = new ScheduledOutdatedFilesCleaner(gridFsOperationsMock);
    }

    @Test
    public void shouldMakeAQueryToDB() throws Exception {
        ReflectionTestUtils.setField(cleaner,
                "fileExpirationPeriodVal", "P2D");

        cleaner.cleanOutdatedFiles();
        verify(gridFsOperationsMock).delete(captor.capture());

        final Instant expected = LocalDate.now().minusDays(2).atStartOfDay(systemDefault()).toInstant();

        final Date actualQuery = ((Map<String, Date>) captor.getValue().getQueryObject().get("uploadDate"))
                .get("$lte");

        assertThat(actualQuery.toInstant()).isEqualTo(expected);
    }

    @Test
    public void shouldRespectISO_8601() throws ParseException {
        final long actualTime = cleaner.getDate("PT1H30M").getTime();
        assertThat(actualTime).isEqualTo(1492284600000L);
    }

    @Test
    public void CronExpressionShouldBeCorrect() {
        CronTrigger trigger = new CronTrigger(cleaner.getCronExpression());

        final long instant = LocalDate.now().atStartOfDay(systemDefault()).minusDays(1).toInstant().toEpochMilli();
        final Date yesterday = new Date(instant);

        Date nextExecutionTime = trigger.nextExecutionTime(
                new TriggerContext() {

                    @Override
                    public Date lastScheduledExecutionTime() {
                        return yesterday;
                    }

                    @Override
                    public Date lastActualExecutionTime() {
                        return yesterday;
                    }

                    @Override
                    public Date lastCompletionTime() {
                        return yesterday;
                    }
                });

        final Long expected = LocalDate.now().atStartOfDay(systemDefault()).toInstant().toEpochMilli();
        final Long actual = nextExecutionTime.getTime();
        assertThat(actual).isEqualTo(expected);
    }
}