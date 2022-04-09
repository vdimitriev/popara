//package mk.vedmak.avtobusi.popara.config
//
//import org.neo4j.ogm.session.SessionFactory
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.ComponentScan
//import org.springframework.context.annotation.Configuration
//import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories
//import org.springframework.data.neo4j.transaction.Neo4jTransactionManager
//import org.springframework.transaction.annotation.EnableTransactionManagement
//
//
//@Configuration
//@ComponentScan("mk.vedmak.avtobusi.popara")
//@EnableNeo4jRepositories(basePackages = ["mk.vedmak.avtobusi.popara.movies.repository"])
//@EnableTransactionManagement
//class Neo4JConfig {
//
//    @Bean
//    fun getConfiguration(): org.neo4j.ogm.config.Configuration {
//        return org.neo4j.ogm.config.Configuration.Builder().uri(BOLT_URL).build()
//    }
//
//    @Bean("sessionFactory")
//    fun getSessionFactory(): SessionFactory {
//        return SessionFactory(getConfiguration(), "mk.vedmak.avtobusi.popara.movies.entity")
//    }
//
//    @Bean
//    fun transactionManager(): Neo4jTransactionManager {
//        return Neo4jTransactionManager(getSessionFactory())
//    }
//
//    companion object {
//        const val BOLT_URL = "bolt://neo4j:secret@localhost:7687/neo4j"
//    }
//}