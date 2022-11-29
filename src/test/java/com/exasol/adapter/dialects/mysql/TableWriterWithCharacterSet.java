package com.exasol.adapter.dialects.mysql;

import java.sql.Connection;

import com.exasol.dbbuilder.dialects.Column;
import com.exasol.dbbuilder.dialects.Table;
import com.exasol.dbbuilder.dialects.mysql.MySqlImmediateDatabaseObjectWriter;

public class TableWriterWithCharacterSet extends MySqlImmediateDatabaseObjectWriter {

    private final String characterSet;

    public TableWriterWithCharacterSet(final Connection connection, final String characterSet) {
        super(connection);
        this.characterSet = characterSet;
    }

    @Override
    public void write(final Table table) {
        final StringBuilder builder = new StringBuilder("CREATE TABLE ");
        builder.append(table.getFullyQualifiedName()).append(" (");
        int i = 0;
        for (final Column column : table.getColumns()) {
            if (i++ > 0) {
                builder.append(", ");
            }
            builder.append(getQuotedColumnName(column.getName())) //
                    .append(" ") //
                    .append(column.getType());
        }
        builder.append(")") //
                .append(" CHARACTER SET ") //
                .append(this.characterSet);
        writeToObject(table, builder.toString());
    }
}
