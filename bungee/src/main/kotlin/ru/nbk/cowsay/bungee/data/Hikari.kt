package ru.nbk.cowsay.bungee.data

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import ru.nbk.cowsay.bungee.configuration.CowSayConfigBungee
import java.sql.Connection
import java.sql.ResultSet
import java.sql.Statement

class Hikari(private val config: CowSayConfigBungee) {

    private var hikariConfig: HikariConfig = HikariConfig()

    init {
        hikariConfig.jdbcUrl = "jdbc:mysql://" + config.getMySQLHostname() + ":" + config.getMySQLPort() + "/" + config.getMySQLDatabase() + "?characterEncoding=utf8&serverTimezone=UTC";
        hikariConfig.username = config.getMySQLUsername()
        hikariConfig.password = config.getMySQLPassword();
        hikariConfig.addDataSourceProperty( "cachePrepStmts" , "true" );
        hikariConfig.addDataSourceProperty( "prepStmtCacheSize" , "250" );
        hikariConfig.addDataSourceProperty( "prepStmtCacheSqlLimit" , "2048" );
        hikariConfig.driverClassName = "com.mysql.jdbc.Driver";
    }

    private var hikari: HikariDataSource = HikariDataSource(hikariConfig)

    init {
        hikari.idleTimeout = 870000000;
        hikari.maxLifetime = 870000000;
        hikari.connectionTimeout = 870000000;
        hikari.minimumIdle = 20;
        hikari.maximumPoolSize = 10;
    }

    open fun getHikari() : HikariDataSource {
        return hikari;
    }

    open fun closeConnection(connection: Connection?, statement: Statement?, resultSet: ResultSet?){
        if (!connection?.isClosed!!){
            if (resultSet !== null){
                resultSet.close()
            }
            if (statement !== null){
                statement.close()
            }
            connection?.close()
        }
    }

}