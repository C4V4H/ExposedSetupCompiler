package org.coms.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*

@Serializable
data class Cliente(
    var id: Int, 
		var cognome: String, 
		var nome: String, 
		var indirizzo: String, 
		var citta: String, 
		var provincia: String, 
		var cap: String, 
		var nazione: String, 
		var mail: String, 
		var telefono: String, 
		var partitaIva: String, 
		var codiceFiscale: String, 
		var idUtente: Int, 
		var idAzienda: Int, 
		
)


object Clienti : Table() {
    val id = integer("id").autoIncrement()
		val cognome = varchar("cognome", 100)
		val nome = varchar("nome", 100)
		val indirizzo = varchar("indirizzo", 100)
		val citta = varchar("citta", 100)
		val provincia = varchar("provincia", 100)
		val cap = varchar("cap", 100)
		val nazione = varchar("nazione", 100)
		val mail = varchar("mail", 100)
		val telefono = varchar("telefono", 100)
		val partitaIva = varchar("partitaIva", 100)
		val codiceFiscale = varchar("codiceFiscale", 100)
		val idUtente = reference("idUtente", Utenti.id)
		val idAzienda = reference("idAzienda", Aziende.id)
		override val primaryKey = PrimaryKey(id)
}


interface DAOFacadeClienti {
    suspend fun all(): List<Cliente>
    suspend fun get(id: Int): Cliente?
    suspend fun add(cognome: String, 
		nome: String, 
		indirizzo: String, 
		citta: String, 
		provincia: String, 
		cap: String, 
		nazione: String, 
		mail: String, 
		telefono: String, 
		partitaIva: String, 
		codiceFiscale: String, 
		idUtente: Int, 
		idAzienda: Int, 
		): Cliente?
    suspend fun edit(id: Int,
        cognome: String, 
		nome: String, 
		indirizzo: String, 
		citta: String, 
		provincia: String, 
		cap: String, 
		nazione: String, 
		mail: String, 
		telefono: String, 
		partitaIva: String, 
		codiceFiscale: String, 
		idUtente: Int, 
		idAzienda: Int, 
		): Boolean
    suspend fun delete(id: Int): Boolean
}