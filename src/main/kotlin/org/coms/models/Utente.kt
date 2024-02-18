package org.coms.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*

@Serializable
data class Utente(
    var id: Int, 
		var mail: String, 
		
)


object Utenti : Table() {
    val id = integer("id").autoIncrement()
		val mail = varchar("mail", 100)
		override val primaryKey = PrimaryKey(id)
}


interface DAOFacadeUtenti {
    suspend fun all(): List<Utente>
    suspend fun get(id: Int): Utente?
    suspend fun add(mail: String, 
		): Utente?
    suspend fun edit(id: Int,
        mail: String, 
		): Boolean
    suspend fun delete(id: Int): Boolean
}