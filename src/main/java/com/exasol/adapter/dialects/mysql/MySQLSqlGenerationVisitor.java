package com.exasol.adapter.dialects.mysql;

import java.util.ArrayList;
import java.util.List;

import com.exasol.adapter.AdapterException;
import com.exasol.adapter.dialects.SqlDialect;
import com.exasol.adapter.dialects.rewriting.SqlGenerationContext;
import com.exasol.adapter.dialects.rewriting.SqlGenerationVisitor;
import com.exasol.adapter.sql.*;

public class MySQLSqlGenerationVisitor extends SqlGenerationVisitor {
    public MySQLSqlGenerationVisitor(final SqlDialect dialect, final SqlGenerationContext context) {
        super(dialect, context);
    }

    @Override
    public String visit(final SqlFunctionScalar function) throws AdapterException {
        if (isDateAddFunction(function.getFunction())) {
            return generateDateAdd(function);
        } else if (function.getFunction() == ScalarFunction.DIV) {
            return generateBinaryFunction(function, "DIV");
        } else if (function.getFunction() == ScalarFunction.BIT_LSHIFT) {
            return generateBinaryFunction(function, "<<");
        } else if (function.getFunction() == ScalarFunction.BIT_RSHIFT) {
            return generateBinaryFunction(function, ">>");
        } else {
            return super.visit(function);
        }
    }

    private static boolean isDateAddFunction(final ScalarFunction function) {
        switch (function) {
            case ADD_DAYS:
            case ADD_HOURS:
            case ADD_MINUTES:
            case ADD_MONTHS:
            case ADD_SECONDS:
            case ADD_WEEKS:
            case ADD_YEARS:
                return true;
            default:
                return false;
        }
    }

    private String generateDateAdd(final SqlFunctionScalar function) throws AdapterException {
        final List<SqlNode> arguments = function.getArguments();
        return "DATE_ADD(" + arguments.get(0).accept(this) + ", INTERVAL " + arguments.get(1).accept(this) + " "
                + getIntervalUnit(function.getFunction()) + ")";
    }

    private static String getIntervalUnit(final ScalarFunction function) {
        switch (function) {
            case ADD_DAYS:
                return "DAY";
            case ADD_HOURS:
                return "HOUR";
            case ADD_MINUTES:
                return "MINUTE";
            case ADD_MONTHS:
                return "MONTH";
            case ADD_SECONDS:
                return "SECOND";
            case ADD_WEEKS:
                return "WEEK";
            case ADD_YEARS:
                return "YEAR";
            default:
                throw new IllegalArgumentException("Unsupported date-add function: " + function);
        }
    }

    private String generateBinaryFunction(final SqlFunctionScalar function, final String operator)
            throws AdapterException {
        final List<SqlNode> arguments = function.getArguments();
        final List<String> argumentsSql = new ArrayList<>(arguments.size());
        for (final SqlNode node : arguments) {
            argumentsSql.add(node.accept(this));
        }
        return argumentsSql.get(0) + " " + operator + " " + argumentsSql.get(1);
    }
}
