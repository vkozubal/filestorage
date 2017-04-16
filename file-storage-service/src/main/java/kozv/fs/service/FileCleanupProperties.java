package kozv.fs.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@PropertySource(value = "classpath:application.properties")
public class FileCleanupProperties {

    /**
     * @see <a href="https://en.wikipedia.org/wiki/ISO_8601#Durations">format</a>
     */
    @Value("${fs.file.expiration.period}")
    private String fileExpirationPeriodVal;
}