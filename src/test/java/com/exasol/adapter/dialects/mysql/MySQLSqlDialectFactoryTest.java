package com.exasol.adapter.dialects.mysql;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.exasol.adapter.AdapterProperties;
import com.exasol.adapter.dialects.JDBCAdapterContext;

class MySQLSqlDialectFactoryTest {
    private MySQLSqlDialectFactory factory;

    @BeforeEach
    void beforeEach() {
        this.factory = new MySQLSqlDialectFactory();
    }

    @Test
    void testGetName() {
        assertThat(this.factory.getSqlDialectName(), equalTo("MYSQL"));
    }

    @Test
    void testCreateDialect() {
        assertThat(this.factory.createSqlDialect(JDBCAdapterContext.builder().properties(AdapterProperties.emptyProperties()).build()),
                instanceOf(MySQLSqlDialect.class));
    }
}