package samantha.com.sem6clase.config

import com.zaxxer.hikari.HikariDataSource
import jakarta.persistence.EntityManagerFactory
import org.flywaydb.core.Flyway
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import javax.sql.DataSource

@Configuration
@EnableJpaRepositories(
    basePackages = ["samantha.com.sem6clase.repository.producto"],
    entityManagerFactoryRef = "productosEntityManagerFactory",
    transactionManagerRef = "productosTransactionManager"
)
class ProductosDbConfig {

    @Bean
    @ConfigurationProperties("app.datasource.secondary")
    fun productosDataSource(): DataSource =
        DataSourceBuilder.create().type(HikariDataSource::class.java).build()

    @Bean(initMethod = "migrate")
    fun productosFlyway(@Qualifier("productosDataSource") ds: DataSource): Flyway =
        Flyway.configure()
            .dataSource(ds)
            .locations("classpath:db/migration/productos")
            .load()

    @Bean
    @DependsOn("productosFlyway")
    fun productosEntityManagerFactory(
        @Qualifier("productosDataSource") ds: DataSource
    ): LocalContainerEntityManagerFactoryBean {
        val em = LocalContainerEntityManagerFactoryBean()
        em.dataSource = ds
        em.setPackagesToScan("samantha.com.sem6clase.model.producto")
        em.jpaVendorAdapter = HibernateJpaVendorAdapter()
        em.setJpaPropertyMap(
            mapOf(
                "hibernate.hbm2ddl.auto" to "validate",
                "hibernate.show_sql" to "true",
                "hibernate.format_sql" to "true"
            )
        )
        return em
    }

    @Bean
    fun productosTransactionManager(
        @Qualifier("productosEntityManagerFactory") emf: EntityManagerFactory
    ) = JpaTransactionManager(emf)
}