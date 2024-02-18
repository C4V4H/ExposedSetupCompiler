            package org.coms.dao

import kotlinx.coroutines.runBlocking
import org.coms.models.Utente
import org.coms.models.Utenti
import org.coms.models.DAOFacadeUtenti
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class DAOFacadeUtentiImpl : DAOFacadeUtenti {

    private fun resultRowToModel(row: ResultRow) = Utente(
        id = row[Utenti.id], 
		mail = row[Utenti.mail], 
		
    )
    
    override suspend fun all(): List<Utente> = DatabaseSingleton.dbQuery {
        Utenti.selectAll().map(::resultRowToModel)
    }
    
    override suspend fun get(id: Int): Utente? = DatabaseSingleton.dbQuery {
        Utenti
            .select { Utenti.id eq id }
            .map(::resultRowToModel)
            .singleOrNull()
    }
    
    override suspend fun add(
        mail: String,
		
    ): Utente? = DatabaseSingleton.dbQuery {
        val insertStatement = Utenti.insert {
            it[Utenti.mail] = mail
		
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToModel)
    }
    
    
    override suspend fun edit(
        id: Int,
        mail: String,
		
    ): Boolean = DatabaseSingleton.dbQuery {
        Utenti.update({ Utenti.id eq id }) {
            it[Utenti.id] = id
            it[Utenti.mail] = mail
		
        } > 0
    }
    
    override suspend fun delete(id: Int): Boolean = DatabaseSingleton.dbQuery {
        Utenti.deleteWhere { Utenti.id eq id } > 0
    }
}


val daoUtenti: DAOFacadeUtenti = DAOFacadeUtentiImpl().apply {
    runBlocking {
        if (all().isEmpty()) {
            print("tabella Utenti vuota")
        }
    }
}
