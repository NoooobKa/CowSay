package ru.nbk.cowsay.bungee.data.impl

import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.connection.ProxiedPlayer
import ru.nbk.cowsay.bungee.configuration.CowSayConfigBungee
import ru.nbk.cowsay.bungee.data.DatabaseManager
import ru.nbk.cowsay.bungee.data.Hikari
import ua.i0xhex.database.engine.MySQL
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

class DatabaseManagerImpl: DatabaseManager{

    private var config: CowSayConfigBungee
    private var hikari: Hikari

    constructor(config: CowSayConfigBungee){
        this.config = config
        this.hikari = Hikari(config)

        init()
    }

    private fun init() {
        val sql = "CREATE TABLE IF NOT EXISTS `${config.getMySQLDatabase()}`.`cowsay` (\n" +
                "  `id` INT NOT NULL AUTO_INCREMENT,\n" +
                "  `username` TEXT NOT NULL,\n" +
                "  `lastSay` TEXT NOT NULL,\n" +
                "  `sayCount` INT NOT NULL,\n" +
                "  PRIMARY KEY (`id`))\n" +
                "ENGINE = InnoDB\n" +
                "DEFAULT CHARACTER SET = utf8mb4;\n"

        var c: Connection? = null
        var ps: PreparedStatement? = null
        var rs: ResultSet? = null

        try {
            c = hikari.getHikari().connection
            ps = c.prepareStatement(sql)
            ps.executeUpdate()
        }catch (e: SQLException){
            e.printStackTrace()
        }finally {
            hikari.closeConnection(c, ps, rs)
        }
    }

    override fun onDisable() {
        hikari.getHikari().close()
    }

    override fun onCowSayCommand(username: String, text: String) {
        var exists: Boolean = false
        val sqlCheck = "SELECT * FROM cowsay WHERE username = ?"

        var c: Connection? = null
        var ps: PreparedStatement? = null
        var rs: ResultSet? = null

        try {
            c = hikari.getHikari().connection
            ps = c.prepareStatement(sqlCheck)
            ps.setString(1, username)
            rs = ps.executeQuery()
            if (rs.next()) exists = true
        }catch (e: SQLException){
            e.printStackTrace()
        }finally {
            hikari.closeConnection(c, ps, rs)
        }

        if (!exists) {
            val sqlInsert = "INSERT INTO cowsay(username,lastSay,sayCount) VALUE(?,?,?)"
            try {
                c = hikari.getHikari().connection
                ps = c.prepareStatement(sqlInsert)
                ps.setString(1, username)
                ps.setString(2, text)
                ps.setInt(3, 0)
                ps.executeUpdate()
            }catch (e: SQLException){
                e.printStackTrace()
            }finally {
                hikari.closeConnection(c, ps, rs)
            }
        }else{
            val sqlUpdate = "UPDATE cowsay SET lastSay = ? WHERE username = ?"
            try {
                c = hikari.getHikari().connection
                ps = c.prepareStatement(sqlUpdate)
                ps.setString(1, text)
                ps.setString(2, username)
                ps.executeUpdate()
            }catch (e: SQLException){
                e.printStackTrace()
            }finally {
                hikari.closeConnection(c, ps, rs)
            }
        }

        val inc = "UPDATE cowsay SET sayCount = sayCount + 1 WHERE username = ?"
        try {
            c = hikari.getHikari().connection
            ps = c.prepareStatement(inc)
            ps.setString(1, username)
            ps.executeUpdate()
        }catch (e: SQLException){
            e.printStackTrace()
        }finally {
            hikari.closeConnection(c, ps, rs)
        }
    }

    override fun getPlayerSayCount(username: String): Int {
        val sql = "SELECT * FROM cowsay WHERE username = ?"
        var count = 0

        var c: Connection? = null
        var ps: PreparedStatement? = null
        var rs: ResultSet? = null

        try {
            c = hikari.getHikari().connection
            ps = c.prepareStatement(sql)
            ps.setString(1, username)
            rs = ps.executeQuery()
            if (rs.next()) {
                count = rs.getInt("sayCount")
            }
        }catch (e: SQLException){
            e.printStackTrace()
        }finally {
            hikari.closeConnection(c, ps, rs)
        }

        return count
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////

    //    private var config: CowSayConfigBungee
//    private var database: MySQL
//
//    constructor(config: CowSayConfigBungee){
//        this.config = config;
//        this.database = MySQL(config.getMySQLHostname() + ":" + config.getMySQLPort() + "/" + config.getMySQLDatabase() + "?autoReconnect=true&useSSL=false&characterEncoding=utf8&serverTimezone=UTC",
//            config.getMySQLUsername(),
//            config.getMySQLPassword())
//        database.open()
//
//        init()
//    }
//
//    private fun init() {
//        val sql = "CREATE TABLE IF NOT EXISTS `${config.getMySQLDatabase()}`.`cowsay` (\n" +
//                "  `id` INT NOT NULL AUTO_INCREMENT,\n" +
//                "  `username` TEXT NOT NULL,\n" +
//                "  `lastSay` TEXT NOT NULL,\n" +
//                "  `sayCount` INT NOT NULL,\n" +
//                "  PRIMARY KEY (`id`))\n" +
//                "ENGINE = InnoDB\n" +
//                "DEFAULT CHARACTER SET = utf8mb4;\n"
//        database.executeUpdate(sql).join()
//    }
//
//    override fun onDisable() {
//        database.close()
//    }
//
//    override fun onCowSayCommand(username: String, text: String) {
//        var exists: Boolean = false
//        val sqlCheck = "SELECT * FROM cowsay WHERE username = ?"
//
//        database.executeQuery({rs -> exists = rs.next()}, sqlCheck, username).join()
//
//        if (!exists) {
//            val sqlInsert = "INSERT INTO cowsay(username,lastSay,sayCount) VALUE(?,?,?)"
//            database.executeUpdate(sqlInsert, mutableListOf<Any>(username, text, 0)).join()
//        }else{
//            val sqlUpdate = "UPDATE cowsay SET lastSay = ? WHERE username = ?"
//            database.executeUpdate(sqlUpdate, mutableListOf(text, username)).join()
//        }
//
//        val inc = "UPDATE cowsay SET sayCount = sayCount + 1 WHERE username = ?"
//        database.executeUpdate(inc, username).join()
//    }
//
//    override fun getPlayerSayCount(username: String): Int {
//        val sql = "SELECT * FROM cowsay WHERE username = ?"
//        var count = 0
//        database.executeQuery({rs -> if (rs.next()) count = rs.getInt("sayCount")}, sql, username).join()
//        return count
//    }

}

