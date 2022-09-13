package ru.nbk.cowsay.bukkit

import org.bukkit.plugin.java.JavaPlugin
import ru.nbk.cowsay.bukkit.listener.MessageListener
import ru.nbk.cowsay.bukkit.nms.EntityMap

class CowSayBukkit: JavaPlugin() {

    override fun onEnable() {
        EntityMap.CUSTOM_COW.register()
        server.messenger.registerIncomingPluginChannel(this, "cowsay", MessageListener(this))
    }

    override fun onDisable() {
        server.messenger.unregisterIncomingPluginChannel(this)
    }

}