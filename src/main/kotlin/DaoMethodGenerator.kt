import java.lang.StringBuilder

class DaoMethodGenerator(
    private val dataClassName: String,
    private val tableName: String,
    private val attributes: Array<Attribute>,
    private val references: Array<Reference>,
    private val primaryKey: Attribute = "id" to DataType.INTEGER
) {
    fun generateClassContent(): String {
        val str = StringBuilder(
            """
            package org.coms.dao

import kotlinx.coroutines.runBlocking
import org.coms.models.$dataClassName
import org.coms.models.$tableName
import org.coms.models.DAOFacade$tableName
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class DAOFacade${tableName}Impl : DAOFacade$tableName {

    private fun resultRowToModel(row: ResultRow) = $dataClassName(
        ${listAttributesForResultRow()}
    )
    
    override suspend fun all(): List<$dataClassName> = org.coms.dao.DatabaseSingleton.dbQuery {
        ${tableName}.selectAll().map(::resultRowToModel)
    }
    
    override suspend fun get(${primaryKey.name}: ${primaryKey.type.kotlin}): $dataClassName? = org.coms.dao.DatabaseSingleton.dbQuery {
        $tableName
            .selectAll().where {  $tableName.${primaryKey.name} eq ${primaryKey.name} }
            .map(::resultRowToModel)
            .singleOrNull()
    }
    
    override suspend fun add(
        ${getAttributesForParam()}
    ): ${dataClassName}? = org.coms.dao.DatabaseSingleton.dbQuery {
        val insertStatement = $tableName.insert {
            ${listAttributesForQuery()}
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToModel)
    }
    
    
    override suspend fun edit(
        ${primaryKey.name}: ${primaryKey.type.kotlin},
        ${getAttributesForParam()}
    ): Boolean = org.coms.dao.DatabaseSingleton.dbQuery {
        $tableName.update({ $tableName.${primaryKey.name} eq ${primaryKey.name} }) {
            it[$tableName.${primaryKey.name}] = ${primaryKey.name}
            ${listAttributesForQuery()}
        } > 0
    }
    
    override suspend fun delete(${primaryKey.name}: ${primaryKey.type.kotlin}): Boolean = org.coms.dao.DatabaseSingleton.dbQuery {
        $tableName.deleteWhere { $tableName.${primaryKey.name} eq ${primaryKey.name} } > 0
    }
}


val dao$tableName: DAOFacade$tableName = DAOFacade${tableName}Impl().apply {
    runBlocking {
        if (all().isEmpty()) {
            print("tabella $tableName vuota")
        }
    }
}

""".trimIndent()
        )


        return str.toString()
    }

    private fun listAttributesForQuery(): String {
        val str = StringBuilder("")

        attributes.forEach { (name, _) ->
            str.append("it[$tableName.$name] = $name\n\t\t")
        }
        references.forEach { (name, _, _) ->
            str.append("it[$tableName.${name}] = ${name}\n\t\t")
        }

        return str.toString()
    }

    private fun getAttributesForParam(): String {
        val str = StringBuilder("")

        attributes.forEach { (name, type) ->
            str.append("$name: ${type.kotlin},\n\t\t")
        }
        references.forEach { (name, type, _) ->
            str.append("${name}: ${type.kotlin},\n\t\t")
        }

        return str.toString()
    }


    private fun listAttributesForResultRow(): String {
        val str = StringBuilder("")

        str.append(
            "${primaryKey.name} = row[$tableName.${primaryKey.name}], \n\t\t"
        )
        attributes.forEach { (name, _) ->
            str.append("$name = row[$tableName.$name], \n\t\t")
        }
        references.forEach { (name, _, _) ->
            str.append("$name = row[$tableName.${name}], \n\t\t")
        }

        return str.toString()
    }
}