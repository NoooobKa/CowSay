package ru.nbk.cowsay.bukkit.listener

import com.google.common.io.ByteArrayDataInput
import com.google.common.io.ByteStreams
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.plugin.messaging.PluginMessageListener
import ru.nbk.cowsay.bukkit.configuration.CowSayBukkitConfig
import ru.nbk.cowsay.bukkit.configuration.impl.CowSayBukkitConfigImpl
import ru.nbk.cowsay.bukkit.nms.CustomCow
import java.util.*

class MessageListener(private val plugin: JavaPlugin) : PluginMessageListener{

    private val config: CowSayBukkitConfig = CowSayBukkitConfigImpl(plugin)

    override fun onPluginMessageReceived(channel: String?, player: Player?, message: ByteArray?) {
        val data: ByteArrayDataInput = ByteStreams.newDataInput(message)
        val playerUUID: String = data.readUTF()
        val line: String = data.readUTF()
        val says: Int = data.readInt()

        val player: Player = Bukkit.getPlayer(UUID.fromString(playerUUID))
        if (!player.isOnline){
            Bukkit.getLogger().info("Игрок ${player.name} не в сети!")
            return
        }

        val cow: CustomCow = CustomCow(player, "[$says] $line", config.getCowSpeed(), config.getCowRadius(), config.getCowLifeTime())
        cow.spawn()
    }
}