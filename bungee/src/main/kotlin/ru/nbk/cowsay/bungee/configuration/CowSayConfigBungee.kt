package ru.nbk.cowsay.bungee.configuration

interface CowSayConfigBungee {

    fun getMySQLHostname(): String

    fun getMySQLPort() : Int

    fun getMySQLDatabase(): String

    fun getMySQLUsername(): String

    fun getMySQLPassword(): String
}