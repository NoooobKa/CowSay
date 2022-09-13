package ru.nbk.cowsay.bukkit.nms

import net.minecraft.server.v1_12_R1.Entity
import net.minecraft.server.v1_12_R1.EntityTypes
import net.minecraft.server.v1_12_R1.MinecraftKey
import net.minecraft.server.v1_12_R1.PathfinderGoalSelector
import org.bukkit.Location
import java.lang.reflect.Field


enum class EntityMap {

	CUSTOM_COW("CustomCow", 92, CustomCow::class.java);

	private var entityName: String? = null
	private var id = 0
	private var clazz: Class<out Entity?>? = null

	 constructor(entityName: String, id: Int, clazz: Class<out Entity?>) {
		this.entityName = entityName
		this.id = id
		this.clazz = clazz
	}

	fun register(){
		val key: MinecraftKey = MinecraftKey(entityName)
		EntityTypes.b.a(id, key, clazz)
		//Для summon ?
//		if (!EntityTypes.d.contains(key)){
//			EntityTypes.d.add(key)
//		}
	}

}