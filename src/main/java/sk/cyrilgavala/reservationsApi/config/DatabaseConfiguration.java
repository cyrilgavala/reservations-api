package sk.cyrilgavala.reservationsApi.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableMongoRepositories(basePackages = "sk.cyrilgavala.reservationsApi.repository")
@EnableTransactionManagement
public class DatabaseConfiguration extends AbstractMongoClientConfiguration {

	@Value("${databaseUrl}")
	private transient String databaseUrl;

	@Bean
	MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
		return new MongoTransactionManager(dbFactory);
	}

	@Override
	protected String getDatabaseName() {
		return "reservations-api";
	}

	@Override
	public MongoClient mongoClient() {
		ConnectionString connectionString = new ConnectionString(databaseUrl);
		MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
			.applyConnectionString(connectionString)
			.build();
		return MongoClients.create(mongoClientSettings);
	}

}
