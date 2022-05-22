package mk.vedmak.avtobusi.popara

import mu.KotlinLogging
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories

@SpringBootApplication
class PoparaApplication:CommandLineRunner {

    private val logger = KotlinLogging.logger {}

    override fun run(vararg args: String?) {
        logger.info("Popara starts running.")
        args.forEach {
            logger.info("command line arg = $it")
        }
        logger.info("Popara stops running.")
    }
}

fun main(args: Array<String>) {
    runApplication<PoparaApplication>(*args)
}
