package ru.nbk.cowsay.bukkit.configuration.impl

import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import ru.nbk.cowsay.bukkit.configuration.CowSayBukkitConfig
import java.io.File

class CowSayBukkitConfigImpl(private val plugin: JavaPlugin) : CowSayBukkitConfig {

    init {
        if (!plugin.dataFolder.exists()) plugin.dataFolder.mkdirs()
    }

    private val rawConfig: File = File(plugin.dataFolder, "MainConfig.yml")
    private val config: YamlConfiguration = YamlConfiguration.loadConfiguration(rawConfig)

    init {
        setIfNotExists("Cow.Speed", 2.5)
        setIfNotExists("Cow.Radius", 5.0)
        setIfNotExists("Cow.Lifetime", 10)

        config.save(rawConfig)
    }

    private fun setIfNotExists(path: String, any: Any){
        if (!config.contains(path)) config.set(path, any)
    }

    override fun getCowSpeed(): Double {
        return config.getDouble("Cow.Speed")
    }

    override fun getCowRadius(): Double {
        return config.getDouble("Cow.Radius")
    }

    override fun getCowLifeTime(): Int {
        return config.getInt("Cow.Lifetime")
    }
}