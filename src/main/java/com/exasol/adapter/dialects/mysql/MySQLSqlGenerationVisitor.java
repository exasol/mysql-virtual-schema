package com.exasol.adapter.dialects.mysql;

import static java.util.stream.Collectors.toList;

import java.util.List;

import com.exasol.adapter.AdapterException;
import com.exasol.adapter.dialects.SqlDialect;
import com.exasol.adapter.dialects.rewriting.SqlGenerationContext;
import com.exasol.adapter.dialects.rewriting.SqlGenerationVisitor;
import com.exasol.adapter.metadata.DataType.ExaDataType;
import com.exasol.adapter.sql.*;
import com.exasol.errorreporting.ExaError;

public class MySQLSqlGenerationVisitor extends SqlGenerationVisitor {
    public MySQLSqlGenerationVisitor(final SqlDialect dialect, final SqlGenerationContext context) {
        super(dialect, context);
    }

    @Override
    public String visit(final SqlFunctionScalar function) throws AdapterException {
        if (isDateAddFunction(function.getFunction())) {
            return generateDateAdd(function);
        } else if (function.getFunction() == ScalarFunction.WEEK) {
            return generateWeek(function);
        } else if (function.getFunction() == ScalarFunction.YEAR) {
            return generateYear(function);
        } else if (function.getFunction() == ScalarFunction.ROUND && function.getArguments().size() == 1) {
            return generateRound(function);
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

    @Override
    public String visit(final SqlFunctionScalarCast function) throws AdapterException {
        final ExaDataType dataType = function.getDataType().getExaDataType();
        if (dataType == ExaDataType.CHAR || dataType == ExaDataType.VARCHAR) {
            return "CAST(" + function.getArgument().accept(this) + " AS CHAR(" + function.getDataType().getSize()
                    + "))";
        }
        return super.visit(function);
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

    private String generateWeek(final SqlFunctionScalar function) throws AdapterException {
        return "WEEK(" + function.getArguments().get(0).accept(this) + ", 3)";
    }

    private String generateYear(final SqlFunctionScalar function) throws AdapterException {
        // MySQL reports YEAR() with its YEAR result type, which the ETL transformer cannot map:
        // ETL-1202: Not implemented - Transformation for this combination of column types is not possible in this version.
        return "CAST(YEAR(" + function.getArguments().get(0).accept(this) + ") AS SIGNED)";
    }

    private String generateRound(final SqlFunctionScalar function) throws AdapterException {
        final String argument = function.getArguments().get(0).accept(this);
        return "SIGN(" + argument + ") * FLOOR(ABS(" + argument + ") + 0.5)";
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
                throw new IllegalArgumentException(ExaError.messageBuilder("E-VSMYSQL-57")
                        .message("Unsupported date-add function: {{function}}", function)
                        .ticketMitigation().toString());
        }
    }

    private String generateBinaryFunction(final SqlFunctionScalar function, final String operator) {
        final List<String> argumentsSql = function.getArguments()
                .stream()
                .map(this::acceptNode)
                .collect(toList());
        return argumentsSql.get(0) + " " + operator + " " + argumentsSql.get(1);
    }

    private String acceptNode(final SqlNode node) {
        try {
            return node.accept(this);
        } catch (final AdapterException e) {
            throw new IllegalStateException(e);
        }
    }
}
