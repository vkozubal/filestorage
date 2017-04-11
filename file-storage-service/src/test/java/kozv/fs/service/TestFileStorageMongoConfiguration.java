package kozv.fs.service;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

@Configuration
@Profile("test")
@AllArgsConstructor(onConstructor = @__({@Autowired}))
public class TestFileStorageMongoConfiguration extends AbstractMongoConfiguration {

    private final DataSourceConfigurationProperties dsConfigProps;

    @Override
    public Mongo mongo() throws Exception {
        MongodStarter starter = MongodStarter.getDefaultInstance();

        String bindIp = dsConfigProps.getDatabaseHost();
        int port = dsConfigProps.getDatabasePort();
        IMongodConfig mongodConfig = new MongodConfigBuilder()
                .version(Version.Main.PRODUCTION)
                .net(new Net(bindIp, port, Network.localhostIsIPv6()))
                .build();

        MongodExecutable mongodExecutable = null;
        mongodExecutable = starter.prepare(mongodConfig);
        mongodExecutable.start();

        return new MongoClient("localhost", port);
    }

    @Override
    protected String getDatabaseName() {
        return dsConfigProps.getDatabaseName();
    }
}