package kozv.fs.service;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Component;

@Setter
@Component
@Profile("prod")
@AllArgsConstructor(onConstructor = @__({@Autowired}))
public class FileStorageMongoConfiguration extends AbstractMongoConfiguration {

    private final DataSourceConfigurationProperties dsConfigProps;

    @Override
    protected String getDatabaseName() {
        return dsConfigProps.getDatabaseName();
    }

    @Override
    public Mongo mongo() throws Exception {
        return new MongoClient(dsConfigProps.getDatabaseHost(), dsConfigProps.getDatabasePort());
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongo(), getDatabaseName());
    }

    /**
     * {@link GridFsOperations} implementation to easily interact with database
     */
    @Bean
    public GridFsOperations gridFsTemplate() throws Exception {
        return new GridFsTemplate(mongoDbFactory(), mappingMongoConverter());
    }
}