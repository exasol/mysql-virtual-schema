package com.exasol.adapter.dialects.mysql;

import com.exasol.adapter.AdapterProperties;
import com.exasol.adapter.dialects.SqlDialect;
import com.exasol.adapter.dialects.SqlDialectFactory;
import com.exasol.adapter.jdbc.ConnectionFactory;
import com.exasol.logging.VersionCollector;

/**
 * Factory for the MySql SQL dialect.
 */
public class MySQLSqlDialectFactory  implements SqlDialectFactory {
    @Override
    public SqlDialect createSqlDialect(final ConnectionFactory connectionFactory, final AdapterProperties properties) {
        return new MySQLSqlDialect(connectionFactory, properties);
    }

    @Override
    public String getSqlDialectName() {
        return MySQLSqlDialect.NAME;
    }

	@Override
	public String getSqlDialectVersion() {
		final VersionCollector versionCollector = new VersionCollector(
                "META-INF/maven/com.exasol/mysql-virtual-schema/pom.properties");
        return versionCollector.getVersionNumber();
	}
}