package ru.nbk.cowsay.bungee

import net.md_5.bungee.api.plugin.Plugin
import ru.nbk.cowsay.bungee.command.CowSayCommand
import ru.nbk.cowsay.bungee.configuration.CowSayConfigBungee
import ru.nbk.cowsay.bungee.configuration.impl.CowSayConfigBungeeImpl
import ru.nbk.cowsay.bungee.data.DatabaseManager
import ru.nbk.cowsay.bungee.data.impl.DatabaseManagerImpl

class CowSayBungee: Plugin(){

    private lateinit var databaseManager: DatabaseManager

    override fun onEnable(){
        val config: CowSayConfigBungee = CowSayConfigBungeeImpl(this)
        this.databaseManager = DatabaseManagerImpl(config)
        proxy.pluginManager.registerCommand(this, CowSayCommand("cowsay", databaseManager))
    }

    override fun onDisable() {
        databaseManager.onDisable()
    }

}