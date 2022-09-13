package ru.nbk.cowsay.bungee.configuration.impl

import net.md_5.bungee.api.plugin.Plugin
import net.md_5.bungee.config.Configuration
import net.md_5.bungee.config.ConfigurationProvider
import net.md_5.bungee.config.YamlConfiguration
import ru.nbk.cowsay.bungee.configuration.CowSayConfigBungee
import java.io.File

class CowSayConfigBungeeImpl: CowSayConfigBungee{

    private val provider: ConfigurationProvider = ConfigurationProvider.getProvider(YamlConfiguration::class.java)
    private var rawConfig: File
    private var config: Configuration

    constructor(plugin: Plugin){
        if (!plugin.dataFolder.exists()) plugin.dataFolder.mkdirs()

        this.rawConfig = File(plugin.dataFolder, "MainConfig.yml")
        if (!rawConfig.exists()){
            rawConfig.createNewFile();
        }

        this.config = provider.load(rawConfig);
        init()
    }

    private fun init(){
        setIfNotExists("MySQL.Hostname", "localhost")
        setIfNotExists("MySQL.Port", 3306)
        setIfNotExists("MySQL.Database", "db")
        setIfNotExists("MySQL.Username", "root")
        setIfNotExists("MySQL.Password", "root")

        provider.save(config, rawConfig)
    }

    private fun setIfNotExists(path: String, any: Any){
        if (!config.contains(path)) config.set(path, any)
    }

    override fun getMySQLHostname(): String {
        return config.getString("MySQL.Hostname")
    }

    override fun getMySQLPort(): Int {
        return config.getInt("MySQL.Port")
    }

    override fun getMySQLDatabase(): String {
        return config.getString("MySQL.Database")
    }

    override fun getMySQLUsername(): String {
        return config.getString("MySQL.Username")
    }

    override fun getMySQLPassword(): String {
        return config.getString("MySQL.Password")
    }
}