package ru.nbk.cowsay.bungee.command

import com.google.common.io.ByteArrayDataOutput
import com.google.common.io.ByteStreams
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.plugin.Command
import ru.nbk.cowsay.bungee.data.DatabaseManager
import java.util.concurrent.CompletableFuture

class CowSayCommand(name: String?, databaseManager: DatabaseManager) : Command(name) {

    var databaseManager: DatabaseManager

    init{
        this.databaseManager = databaseManager;
    }

    override fun execute(sender: CommandSender?, args: Array<out String>?) {
        if (sender !is ProxiedPlayer) return

        val player: ProxiedPlayer = sender
        val label: String? = args?.joinToString(separator = " ")

        label?.let {
            CompletableFuture.runAsync {
                databaseManager.onCowSayCommand(player.name, it)
                sendData(player, it)
            }
        }
    }

    private fun sendData(proxiedPlayer: ProxiedPlayer, label: String){
        val data: ByteArrayDataOutput = ByteStreams.newDataOutput()

        data.writeUTF(proxiedPlayer.uniqueId.toString())
        data.writeUTF(label)
        data.writeInt(databaseManager.getPlayerSayCount(proxiedPlayer.name))

        proxiedPlayer.server.info.sendData("cowsay", data.toByteArray())
    }
}