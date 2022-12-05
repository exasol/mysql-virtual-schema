package com.exasol.adapter.dialects.mysql.charset;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

/**
 * This class enables to display the name for each JDBC data type.
 */
public class JdbcType {

    private JdbcType() {
        // only static usage
    }

    public static String name(final int code) {
        return TYPES.get(code);
    }

    private static final Map<Integer, String> TYPES = new HashMap<>();

    static {
        TYPES.put(Types.BIT, "BIT");
        TYPES.put(Types.TINYINT, "TINYINT");
        TYPES.put(Types.SMALLINT, "SMALLINT");
        TYPES.put(Types.INTEGER, "INTEGER");
        TYPES.put(Types.BIGINT, "BIGINT");
        TYPES.put(Types.FLOAT, "FLOAT");
        TYPES.put(Types.REAL, "REAL");
        TYPES.put(Types.DOUBLE, "DOUBLE");
        TYPES.put(Types.NUMERIC, "NUMERIC");
        TYPES.put(Types.DECIMAL, "DECIMAL");
        TYPES.put(Types.CHAR, "CHAR");
        TYPES.put(Types.VARCHAR, "VARCHAR");
        TYPES.put(Types.LONGVARCHAR, "LONGVARCHAR");
        TYPES.put(Types.DATE, "DATE");
        TYPES.put(Types.TIME, "TIME");
        TYPES.put(Types.TIMESTAMP, "TIMESTAMP");
        TYPES.put(Types.BINARY, "BINARY");
        TYPES.put(Types.VARBINARY, "VARBINARY");
        TYPES.put(Types.LONGVARBINARY, "LONGVARBINARY");
        TYPES.put(Types.NULL, "NULL");
        TYPES.put(Types.OTHER, "OTHER");
        TYPES.put(Types.JAVA_OBJECT, "JAVA_OBJECT");
        TYPES.put(Types.DISTINCT, "DISTINCT");
        TYPES.put(Types.STRUCT, "STRUCT");
        TYPES.put(Types.ARRAY, "ARRAY");
        TYPES.put(Types.BLOB, "BLOB");
        TYPES.put(Types.CLOB, "CLOB");
        TYPES.put(Types.REF, "REF");
        TYPES.put(Types.DATALINK, "DATALINK");
        TYPES.put(Types.BOOLEAN, "BOOLEAN");
        TYPES.put(Types.ROWID, "ROWID");
        TYPES.put(Types.NCHAR, "NCHAR");
        TYPES.put(Types.NVARCHAR, "NVARCHAR");
        TYPES.put(Types.LONGNVARCHAR, "LONGNVARCHAR");
        TYPES.put(Types.NCLOB, "NCLOB");
        TYPES.put(Types.SQLXML, "SQLXML");
        TYPES.put(Types.REF_CURSOR, "REF_CURSOR");
        TYPES.put(Types.TIME_WITH_TIMEZONE, "TIME_WITH_TIMEZONE");
        TYPES.put(Types.TIMESTAMP_WITH_TIMEZONE, "TIMESTAMP_WITH_TIMEZONE");
    }
}
