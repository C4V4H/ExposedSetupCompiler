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
    var idAzienda: Int
)

object Clienti : Table() {
    val id = integer("id").autoIncrement()
    val cognome = varchar("cognome", 128)
    val nome = varchar("nome", 128)
    val indirizzo = varchar("indirizzo", 128)
    val citta = varchar("citta", 128)
    val provincia = varchar("provincia", 128)
    val cap = varchar("cap", 128)
    val nazione = varchar("nazione", 128)
    val mail = varchar("mail", 128)
    val telefono = varchar("telefono", 128)
    val partitaIva = varchar("partitaIva", 128)
    val codiceFiscale = varchar("codiceFiscale", 128)
    val idUtente = reference("idUtente", Articles.id)
    val idAzienda = reference("idAzienda", Articles.id)

    override val primaryKey = PrimaryKey(id)

}

interface DAOFacadeClienti {
    suspend fun all(): List<Cliente>
    suspend fun get(id: Int): Cliente?
    suspend fun add(
        id: Int,
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
        idAzienda: Int
    ): Cliente?

    suspend fun edit(
        id: Int,
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
        idAzienda: Int
    ): Boolean

    suspend fun delete(id: Int): Boolean
}