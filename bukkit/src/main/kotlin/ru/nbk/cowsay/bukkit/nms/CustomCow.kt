package ru.nbk.cowsay.bukkit.nms

import net.minecraft.server.v1_12_R1.EntityCow
import org.bukkit.*
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import kotlin.math.cos
import kotlin.math.sin

open class CustomCow : EntityCow {

    private var source: Player
    private var radius: Double
    private var speed: Double
    private var radialSpeed: Double
    private var lifeTime: Int
    private var tick: Int = 0
    private var isSpawned: Boolean = false
    private var lastMoo: Long = System.currentTimeMillis()
    private var spawnTime: Long = System.currentTimeMillis()

    constructor(source: Player, customName: String, speed: Double, radius: Double, lifeTime: Int) : super((source.world as CraftWorld).handle){
        this.source = source
        this.speed = speed
        this.radius = radius
        this.radialSpeed = Math.PI * 2 * speed / 1.72 / radius
        this.lifeTime = lifeTime

        val loc: Location = source.location

        setLocation(loc.x, loc.y, loc.z, loc.yaw, loc.pitch)
        this.customName = customName
        this.customNameVisible = true
    }

    //selectors
    override fun r(){

    }

    //onTick
    override fun n(){
        if (!isSpawned) {
            super.n()
            return;
        }

        val loc: Location = source.location
        val bukkitWorld: World = loc.world
        val cowLoc: Location = getLocation()

        //Вычисляем координаты для движения животного
        val angle = radialSpeed * tick / 20.0
        val x: Double = loc.x + radius * cos(angle)
        val z: Double = loc.z + radius * sin(angle)
        val y: Double = loc.y

        val locTo = Location(world.world, x, y , z)
        val direction: Vector = locTo.toVector().subtract(cowLoc.toVector())
        locTo.direction = direction

        //Двигаем животное
        bukkitEntity.teleport(locTo)

        //Мычим и пускаем партиклы
        if (lastMoo + 1000 < System.currentTimeMillis()){
            bukkitWorld.playSound(cowLoc, Sound.ENTITY_COW_HURT, 1f, 1f)
            lastMoo = System.currentTimeMillis()
        }
        bukkitWorld.spawnParticle(Particle.VILLAGER_HAPPY, cowLoc.clone().add(0.0, headHeight.toDouble(), 0.0), 1, 0.0, 0.0, 0.0, 0.0)

        tick++
        super.n()

        //Умираем
        if (spawnTime + lifeTime * 1000 < System.currentTimeMillis()){
            bukkitWorld.spawnParticle(Particle.EXPLOSION_LARGE, cowLoc.clone().add(0.0, headHeight.toDouble(), 0.0), 1)
            bukkitWorld.playSound(cowLoc, Sound.ENTITY_GENERIC_EXPLODE, 1f, 1f)
            die()
            isSpawned = false
        }
    }

    open fun spawn(){
        getWorld().addEntity(this)
        spawnTime = System.currentTimeMillis()
        isSpawned = true
    }

    open fun getLocation() : Location {
        return Location(world.world, locX, locY, locZ)
    }

}