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
    
    override suspend fun all(): List<$dataClassName> = DatabaseSingleton.dbQuery {
        ${tableName}.selectAll().map(::resultRowToModel)
    }
    
    override suspend fun get(${primaryKey.name}: ${primaryKey.type.kotlinType}): $dataClassName? = DatabaseSingleton.dbQuery {
        $tableName
            .select { $tableName.${primaryKey.name} eq ${primaryKey.name} }
            .map(::resultRowToModel)
            .singleOrNull()
    }
    
    override suspend fun add(
        ${getAttributesForParam()}
    ): ${dataClassName}? = DatabaseSingleton.dbQuery {
        val insertStatement = $tableName.insert {
            ${listAttributesForQuery()}
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToModel)
    }
    
    
    override suspend fun edit(
        ${primaryKey.name}: ${primaryKey.type.kotlinType},
        ${getAttributesForParam()}
    ): Boolean = DatabaseSingleton.dbQuery {
        $tableName.update({ $tableName.${primaryKey.name} eq ${primaryKey.name} }) {
            it[$tableName.${primaryKey.name}] = ${primaryKey.name}
            ${listAttributesForQuery()}
        } > 0
    }
    
    override suspend fun delete(${primaryKey.name}: ${primaryKey.type.kotlinType}): Boolean = DatabaseSingleton.dbQuery {
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

        attributes.forEach { (name, type) ->
            str.append("it[$tableName.$name] = $name\n\t\t")
        }
        references.forEach { (attribute, reference) ->
            str.append("it[$tableName.${attribute.name}] = ${attribute.name}\n\t\t")
        }

        return str.toString()
    }

    private fun getAttributesForParam(): String {
        val str = StringBuilder("")

        attributes.forEach { (name, type) ->
            str.append("$name: ${type.kotlinType},\n\t\t")
        }
        references.forEach { (name, type) ->
            str.append("${name.name}: ${name.type.kotlinType},\n\t\t")
        }

        return str.toString()
    }


    private fun listAttributesForResultRow(): String {
        val str = StringBuilder("")

        str.append(
            "${primaryKey.name} = row[$tableName.${primaryKey.name}], \n\t\t"
        )
        attributes.forEach { (name, type) ->
            str.append("$name = row[$tableName.$name], \n\t\t")
        }
        references.forEach { (attribute, type) ->
            str.append("${attribute.name} = row[$tableName.${attribute.name}], \n\t\t")
        }

        return str.toString()
    }
}