package org.coms.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*

@Serializable
data class Azienda(
    var id: Int, 
		var nome: String, 
		
)


object Aziende : Table() {
    val id = integer("id").autoIncrement()
		val nome = varchar("nome", 100)
		override val primaryKey = PrimaryKey(id)
}


interface DAOFacadeAziende {
    suspend fun all(): List<Azienda>
    suspend fun get(id: Int): Azienda?
    suspend fun add(nome: String, 
		): Azienda?
    suspend fun edit(id: Int,
        nome: String, 
		): Boolean
    suspend fun delete(id: Int): Boolean
}