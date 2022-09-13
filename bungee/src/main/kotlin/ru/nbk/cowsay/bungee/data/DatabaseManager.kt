package ru.nbk.cowsay.bungee.data

interface DatabaseManager {

    fun onDisable();

    fun onCowSayCommand(username: String, text: String)

    fun getPlayerSayCount(username: String): Int
}