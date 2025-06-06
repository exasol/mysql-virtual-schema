# MySQL Dialect User Guide

[MySQL](https://www.mysql.com/) is an open-source relational database management system.

## Uploading the JDBC Driver to Exasol BucketFS

1. Download the [MySQL JDBC driver](https://dev.mysql.com/downloads/connector/j/). Select Operating System -> Platform Independent -> Download.
2. Upload the driver to BucketFS, see the [BucketFS documentation](https://docs.exasol.com/db/latest/administration/on-premise/bucketfs/accessfiles.htm) for details.

    Hint: Put the driver into folder `default/drivers/jdbc/` to register it for [ExaLoader](#registering-the-jdbc-driver-for-exaloader), too.

## Registering the JDBC driver for ExaLoader

In order to enable the ExaLoader to fetch data from the external database you must register the driver for ExaLoader as described in the [Installation procedure for JDBC drivers](https://github.com/exasol/docker-db/#installing-custom-jdbc-drivers).
1. ExaLoader expects the driver in BucketFS folder `default/drivers/jdbc`.

    If you uploaded the driver for UDF to a different folder, then you need to [upload](#uploading-the-jdbc-driver-to-exasol-bucketfs) the driver again.
2. Additionally you need to create file `settings.cfg` and [upload](#uploading-the-jdbc-driver-to-exasol-bucketfs) it to the same folder in BucketFS:

```properties
DRIVERNAME=MYSQL
JAR=mysql-connector-j.jar
DRIVERMAIN=com.mysql.jdbc.Driver
PREFIX=jdbc:mysql:
NOSECURITY=YES
FETCHSIZE=100000
INSERTSIZE=-1
```

## Installing the Adapter Script

Upload the latest available release of [MySQL Virtual Schema](https://github.com/exasol/mysql-virtual-schema/releases) to Bucket FS.

Then create a schema to hold the adapter script.

```sql
CREATE SCHEMA SCHEMA_FOR_VS_SCRIPT;
```

The SQL statement below creates the adapter script, defines the Java class that serves as entry point and tells the UDF framework where to find the libraries (JAR files) for Virtual Schema and JDBC database driver.

```sql
--/
CREATE OR REPLACE JAVA ADAPTER SCRIPT SCHEMA_FOR_VS_SCRIPT.ADAPTER_SCRIPT_MYSQL AS
    %scriptclass com.exasol.adapter.RequestDispatcher;
    %jar /buckets/bfsdefault/default/virtual-schema-dist-13.0.0-mysql-5.1.0.jar;
    %jar /buckets/bfsdefault/default/mysql-connector-java-<version>.jar;
/
;
```

Please note
* The example uses BucketFS service `bfsdefault` and bucket name `default`.
  * If your installation is configured differently or you did create a different bucket then please replace the substring `/bfsdefault/default/` accordingly.
* If you notice any issue with your SQL client not identifying where a script ends or truncating scripts, see the [SQL Client Troubleshooting section](https://docs.exasol.com/db/latest/database_concepts/virtual_schema/user_guide.htm#SQL_Client_Troubleshooting) for solutions.

## Defining a Named Connection

Define the connection to MySQL as shown below.

```sql
CREATE OR REPLACE CONNECTION MYSQL_JDBC_CONNECTION
TO 'jdbc:mysql://<host>:<port>/'
USER '<user>'
IDENTIFIED BY '<password>';
```

## Creating a Virtual Schema

Below you see how a MySQL Virtual Schema is created. Use CATALOG_NAME property to select a database.

```sql
CREATE VIRTUAL SCHEMA <virtual schema name>
    USING SCHEMA_FOR_VS_SCRIPT.ADAPTER_SCRIPT_MYSQL
    WITH
    CONNECTION_NAME = 'MYSQL_JDBC_CONNECTION'
    CATALOG_NAME = '<database name>';
```

See also [Adapter Properties for JDBC-Based Virtual Schemas](https://github.com/exasol/virtual-schema-common-jdbc#adapter-properties-for-jdbc-based-virtual-schemas).

## Data Types Conversion

| MySQL Data Type | Supported | Converted Exasol Data Type| Known limitations                                              |
|-----------------|-----------|---------------------------|----------------------------------------------------------------|
| BOOLEAN         |  ✓        | BOOLEAN                   |                                                                |
| BIGINT          |  ✓        | DECIMAL                   |                                                                |
| BINARY          |  ×        |                           |                                                                |
| BIT             |  ✓        | BOOLEAN                   |                                                                |
| BLOB            |  ×        |                           |                                                                |
| CHAR            |  ✓        | CHAR                      |                                                                |
| DATE            |  ✓        | DATE                      |                                                                |
| DATETIME        |  ✓        | TIMESTAMP                 | (1)                                                            |
| DECIMAL         |  ✓        | DECIMAL                   |                                                                |
| DOUBLE          |  ✓        | DOUBLE PRECISION          |                                                                |
| ENUM            |  ✓        | CHAR                      |                                                                |
| FLOAT           |  ✓        | DOUBLE PRECISION          |                                                                |
| INT             |  ✓        | DECIMAL                   |                                                                |
| LONGBLOB        |  ×        |                           |                                                                |
| LONGTEXT        |  ✓        | VARCHAR(2000000)          |                                                                |
| MEDIUMBLOB      |  ×        |                           |                                                                |
| MEDIUMINT       |  ✓        | DECIMAL                   |                                                                |
| MEDIUMTEXT      |  ✓        | VARCHAR(2000000)          |                                                                |
| SET             |  ✓        | CHAR                      |                                                                |
| SMALLINT        |  ✓        | DECIMAL                   |                                                                |
| TEXT            |  ✓        | VARCHAR(65535)            | The size of the column is always 65535.*                       |
| TINYBLOB        |  ×        |                           |                                                                |
| TINYINT         |  ✓        | DECIMAL                   |                                                                |
| TINYTEXT        |  ✓        | VARCHAR                   |                                                                |
| TIME            |  ✓        | TIMESTAMP                 | (1) Casted to `TIMESTAMP` with a format `1970-01-01 hh:mm:ss`. |
| TIMESTAMP       |  ✓        | TIMESTAMP                 | (1)                                                            |
| VARBINARY       |  ×        |                           |                                                                |
| VARCHAR         |  ✓        | VARCHAR                   |                                                                |
| YEAR            |  ✓        | DATE                      |                                                                |

(1) Types TIME, DATETIME and TIMESTAMP with fractional second precision are mapped to TIMESTAMP with milliseconds precision
for Exasol versions up to 8.31. Starting with Exasol 8.32 they are mapped with the same specified precision as in MySQL. 

* The tested versions of MySQL Connector JDBC Driver return the column's size depending on the charset and its collation.
As the real data in a MySQL table can sometimes exceed the size that we get from the JDBC driver, we set the size for all TEXT columns to 65535 characters.

If you need to use currently unsupported data types or find a way around known limitations, please, create a github issue in the [VS repository](https://github.com/exasol/virtual-schemas/issues).

## Testing information

In the following matrix you find combinations of JDBC driver and dialect version that we tested.

| Virtual Schema Version | MySQL Version | Driver Name     | Driver Version |
|------------------------|---------------|-----------------|----------------|
| 4.1.3                  | MySQL 8.0.23  | MySQL Connector | 8.0.23         |
| 5.0.1                  | MySQL 8.1.0   | MySQL Connector | 8.1.0          |
| 5.0.2                  | MySQL 9.0.1   | MySQL Connector | 9.0.1          |
| Latest                 | MySQL 9.2.0   | MySQL Connector | 9.3.0          |
