package kozv.fs.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@PropertySource(value = "classpath:database.properties")
public class DataSourceConfigurationProperties {

    @Value("${database.host}")
    private String databaseHost;
    @Value("${database.port}")
    private int databasePort;
    @Value("${database.name}")
    private String databaseName;
}