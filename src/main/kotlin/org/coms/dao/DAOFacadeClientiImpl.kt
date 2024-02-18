package org.coms.dao

import kotlinx.coroutines.runBlocking
import org.coms.models.Cliente
import org.coms.models.Clienti
import org.coms.models.DAOFacadeClienti
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class DAOFacadeClientiImpl : DAOFacadeClienti {

    private fun resultRowToModel(row: ResultRow) = Cliente(
        id = row[Clienti.id],
        cognome = row[Clienti.cognome],
        nome = row[Clienti.nome],
        indirizzo = row[Clienti.indirizzo],
        citta = row[Clienti.citta],
        provincia = row[Clienti.provincia],
        cap = row[Clienti.cap],
        nazione = row[Clienti.nazione],
        mail = row[Clienti.mail],
        telefono = row[Clienti.telefono],
        partitaIva = row[Clienti.partitaIva],
        codiceFiscale = row[Clienti.codiceFiscale],
        idUtente = row[Clienti.idUtente],
        idAzienda = row[Clienti.idAzienda],

        )

    override suspend fun all(): List<Cliente> = DatabaseSingleton.dbQuery {
        Clienti.selectAll().map(::resultRowToModel)
    }

    override suspend fun get(id: Int): Cliente? = DatabaseSingleton.dbQuery {
        Clienti
            .selectAll().where { Clienti.id eq id }
            .map(::resultRowToModel)
            .singleOrNull()
    }

    override suspend fun add(
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

        ): Cliente? = DatabaseSingleton.dbQuery {
        val insertStatement = Clienti.insert {
            it[Clienti.cognome] = cognome
            it[Clienti.nome] = nome
            it[Clienti.indirizzo] = indirizzo
            it[Clienti.citta] = citta
            it[Clienti.provincia] = provincia
            it[Clienti.cap] = cap
            it[Clienti.nazione] = nazione
            it[Clienti.mail] = mail
            it[Clienti.telefono] = telefono
            it[Clienti.partitaIva] = partitaIva
            it[Clienti.codiceFiscale] = codiceFiscale
            it[Clienti.idUtente] = idUtente
            it[Clienti.idAzienda] = idAzienda

        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToModel)
    }


    override suspend fun edit(
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
        idAzienda: Int,

        ): Boolean = DatabaseSingleton.dbQuery {
        Clienti.update({ Clienti.id eq id }) {
            it[Clienti.id] = id
            it[Clienti.cognome] = cognome
            it[Clienti.nome] = nome
            it[Clienti.indirizzo] = indirizzo
            it[Clienti.citta] = citta
            it[Clienti.provincia] = provincia
            it[Clienti.cap] = cap
            it[Clienti.nazione] = nazione
            it[Clienti.mail] = mail
            it[Clienti.telefono] = telefono
            it[Clienti.partitaIva] = partitaIva
            it[Clienti.codiceFiscale] = codiceFiscale
            it[Clienti.idUtente] = idUtente
            it[Clienti.idAzienda] = idAzienda

        } > 0
    }

    override suspend fun delete(id: Int): Boolean = DatabaseSingleton.dbQuery {
        Clienti.deleteWhere { Clienti.id eq id } > 0
    }
}


val daoClienti: DAOFacadeClienti = DAOFacadeClientiImpl().apply {
    runBlocking {
        if (all().isEmpty()) {
            print("tabella Clienti vuota")
        }
    }
}
