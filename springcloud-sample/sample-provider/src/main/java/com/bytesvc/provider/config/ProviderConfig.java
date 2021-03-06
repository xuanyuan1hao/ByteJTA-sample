package com.bytesvc.provider.config;

import javax.sql.DataSource;
import javax.sql.XADataSource;
import javax.transaction.TransactionManager;

import org.apache.commons.dbcp2.managed.BasicManagedDataSource;
import org.bytesoft.bytejta.supports.jdbc.XADataSourceImpl;
import org.bytesoft.bytejta.supports.springcloud.SpringCloudConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

import com.mysql.jdbc.jdbc2.optional.MysqlXADataSource;

@Import(SpringCloudConfiguration.class)
@Configuration
public class ProviderConfig {

	@Bean(name = "dataSource")
	public DataSource getDataSource(@Autowired XADataSource xaDataSource,
			@Qualifier("bytejtaTransactionManager") @Autowired TransactionManager transactionManager) {
		BasicManagedDataSource bds = new BasicManagedDataSource();
		bds.setXaDataSourceInstance(xaDataSource);
		bds.setTransactionManager(transactionManager);

		bds.setMaxTotal(50);
		bds.setInitialSize(20);
		bds.setMaxWaitMillis(60000);
		bds.setMinIdle(6);
		bds.setLogAbandoned(true);
		bds.setRemoveAbandonedOnBorrow(true);
		bds.setRemoveAbandonedOnMaintenance(true);
		bds.setRemoveAbandonedTimeout(1800);
		bds.setTestWhileIdle(true);
		bds.setTestOnBorrow(false);
		bds.setTestOnReturn(false);
		bds.setValidationQuery("select 'x' ");
		bds.setValidationQueryTimeout(1);
		bds.setTimeBetweenEvictionRunsMillis(30000);
		bds.setNumTestsPerEvictionRun(20);
		return bds;
	}

	@Bean
	public XADataSource wrapDataSource() {
		MysqlXADataSource xaDataSource = new MysqlXADataSource();
		xaDataSource.setUrl("jdbc:mysql://127.0.0.1:3306/inst01");
		xaDataSource.setUser("root");
		xaDataSource.setPassword("123456");

		XADataSourceImpl wrapDataSource = new XADataSourceImpl();
		wrapDataSource.setXaDataSource(xaDataSource);
		return wrapDataSource;
	}

	@Bean
	public JdbcTemplate getJdbcTemplate(@Qualifier("dataSource") @Autowired DataSource dataSource) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate();
		jdbcTemplate.setDataSource(dataSource);
		return jdbcTemplate;
	}

}
