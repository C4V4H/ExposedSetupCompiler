package org.coms.dao

import kotlinx.coroutines.runBlocking
import org.coms.models.Azienda
import org.coms.models.Aziende
import org.coms.models.DAOFacadeAziende
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class DAOFacadeAziendeImpl : DAOFacadeAziende {

    private fun resultRowToModel(row: ResultRow) = Azienda(
        id = row[Aziende.id],
        nome = row[Aziende.nome],

        )

    override suspend fun all(): List<Azienda> = DatabaseSingleton.dbQuery {
        Aziende.selectAll().map(::resultRowToModel)
    }

    override suspend fun get(id: Int): Azienda? = DatabaseSingleton.dbQuery {
        Aziende
            .selectAll().where { Aziende.id eq id }
            .map(::resultRowToModel)
            .singleOrNull()
    }

    override suspend fun add(
        nome: String,

        ): Azienda? = DatabaseSingleton.dbQuery {
        val insertStatement = Aziende.insert {
            it[Aziende.nome] = nome

        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToModel)
    }


    override suspend fun edit(
        id: Int,
        nome: String,

        ): Boolean = DatabaseSingleton.dbQuery {
        Aziende.update({ Aziende.id eq id }) {
            it[Aziende.id] = id
            it[Aziende.nome] = nome

        } > 0
    }

    override suspend fun delete(id: Int): Boolean = DatabaseSingleton.dbQuery {
        Aziende.deleteWhere { Aziende.id eq id } > 0
    }
}


val daoAziende: DAOFacadeAziende = DAOFacadeAziendeImpl().apply {
    runBlocking {
        if (all().isEmpty()) {
            print("tabella Aziende vuota")
        }
    }
}
