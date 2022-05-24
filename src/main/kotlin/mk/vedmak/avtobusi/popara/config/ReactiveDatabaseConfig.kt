package mk.vedmak.avtobusi.popara.config

import org.neo4j.driver.Driver
import org.springframework.data.neo4j.core.transaction.ReactiveNeo4jTransactionManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.neo4j.core.ReactiveDatabaseSelectionProvider
import org.springframework.transaction.ReactiveTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@EnableTransactionManagement
class ReactiveDatabaseConfig {
    @Bean
    fun reactiveTransactionManager(driver: Driver): ReactiveTransactionManager {
        return ReactiveNeo4jTransactionManager(driver,
            ReactiveDatabaseSelectionProvider.createStaticDatabaseSelectionProvider("neo4j"))
    }
}