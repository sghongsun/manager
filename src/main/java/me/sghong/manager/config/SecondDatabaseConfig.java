//package me.sghong.manager.config;
//
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.mybatis.spring.SqlSessionFactoryBean;
//import org.mybatis.spring.SqlSessionTemplate;
//import org.mybatis.spring.annotation.MapperScan;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
//import org.springframework.jdbc.datasource.DataSourceTransactionManager;
//import org.springframework.transaction.PlatformTransactionManager;
//
//import javax.sql.DataSource;
//
//@Configuration
//@MapperScan(value="me.sghong.manager.mapper.second", sqlSessionFactoryRef = "sqlSessionFactory2")
//public class SecondDatabaseConfig {
//    private final String SecondSource = "dataSource2";
//    private final String SecondFactory = "sqlSessionFactory2";
//
//    @Bean
//    @ConfigurationProperties("app.datasource2")
//    public DataSourceProperties dataSourceProperties2() {
//        return new DataSourceProperties();
//    }
//
//    @Bean(SecondSource)
//    public DataSource dataSource2() {
//        return dataSourceProperties2().initializeDataSourceBuilder().build();
//    }
//
//    @Bean(SecondFactory)
//    public SqlSessionFactory sqlSessionFactory2(@Qualifier(SecondSource) DataSource dataSource, ApplicationContext applicationContext) throws Exception {
//        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
//        sessionFactory.setDataSource(dataSource);
//        Resource[] res = new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/second/**/*.xml");
//        sessionFactory.setMapperLocations(res);
//        sessionFactory.setTypeAliasesPackage("me.sghong.manager");
//        sessionFactory.setConfigLocation(applicationContext.getResource("classpath:mybatis-config.xml"));
//
//        return sessionFactory.getObject();
//    }
//
//    @Bean
//    public SqlSessionTemplate sqlSessionTemplate2(@Qualifier(SecondFactory) SqlSessionFactory sqlSessionFactory) {
//        return new SqlSessionTemplate(sqlSessionFactory);
//    }
//
//    @Bean
//    public PlatformTransactionManager transactionManager2(@Qualifier(SecondSource) DataSource dataSource) {
//        return new DataSourceTransactionManager(dataSource);
//    }
//}
