import java.lang.StringBuilder
import DataType.*

class ModelClassGenerator(
    private val dataClassName: String,
    private val tableName: String,
    private val attributes: Array<Attribute>,
    private val references: Array<Reference>,
    private val primaryKey: Attribute = "id" to DataType.INTEGER
) {

    fun generateClassContent(): String {
        val attributi = getAttributesForParam()
        val str = StringBuilder(
            """
package org.coms.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*

@Serializable
data class $dataClassName(
    ${getAttributesForDataClass()}
)


object $tableName : Table() {
    ${getAttributesForTable()}
}


interface DAOFacade$tableName {
    suspend fun all(): List<$dataClassName>
    suspend fun get(${primaryKey.name}: ${primaryKey.type.kotlinType}): $dataClassName?
    suspend fun add($attributi): $dataClassName?
    suspend fun edit(${primaryKey.name}: ${primaryKey.type.kotlinType},
        $attributi): Boolean
    suspend fun delete(${primaryKey.name}: ${primaryKey.type.kotlinType}): Boolean
}
""".trimIndent()
        )

        return str.toString()

    }

    private fun getAttributesForDataClass(): String {
        val str = StringBuilder("")

        str.append(
            "var ${primaryKey.name}: ${
                primaryKey.type.kotlinType
            }, \n\t\t"
        )
        attributes.forEach { (name, type) ->
            str.append("var $name: ${type.kotlinType}, \n\t\t")
        }
        references.forEach { (attribute, reference) ->
            str.append("var ${attribute.name}: ${attribute.type.kotlinType}, \n\t\t")
        }

        return str.toString()
    }

    private fun getAttributesForParam(): String {
        val str = StringBuilder("")

        attributes.forEach { (name, type) ->
            str.append("$name: ${type.kotlinType}, \n\t\t")
        }
        references.forEach { (attribute, reference) ->
            str.append("${attribute.name}: ${attribute.type.kotlinType}, \n\t\t")
        }

        return str.toString()
    }

    private fun getAttributesForTable(): String {
        val str = StringBuilder("")

        str.append(
            "val ${primaryKey.name} = ${getExposedType(primaryKey)}" +
                    "${if (primaryKey.type == INTEGER) ".autoIncrement()" else ""}\n\t\t"
        )
        attributes.forEach {
            str.append("val ${it.name} = ${getExposedType(it)}\n\t\t")
        }
        references.forEach { (attribute, reference) ->
            str.append("val ${attribute.name} = reference(\"${attribute.name}\", ${reference})\n\t\t")
        }

        return str.append("override val primaryKey = PrimaryKey(${primaryKey.name})").toString()
    }

    private fun getExposedType(attr: Attribute): String {
        if (attr.type == STRING)
            return "varchar(\"${attr.name}\", ${attr.type.exposedType.replace("varchar(", "").replace(")", "")})"

        return "${attr.type.exposedType}(\"${attr.name}\")"
    }
}

/*
private fun getAttributeType(attr: String): String {
        if (attr.contains("varchar"))
            return "String"

        return when (attr) {
            "integer" -> "Int"
            "short" -> "Short"
            "long" -> "Long"
            "float" -> "Float"
            "double" -> "Double"
            "bool" -> "Boolean"
            "char" -> "Char"
            "date" -> "java.time.LocalDate"
            "datetime" -> "java.time.LocalDateTime"
            "timestamp" -> "java.sql.Timestamp"
            "duration" -> "java.time.Duration"
            else -> throw Exception("Make sure the types in the json are correct")
        }
    }

    private fun getAttributeTypeForTable(attr: String, name: String): String {
        if (attr.contains("varchar"))
            return "varchar(\"$name\", ${attr.replace("varchar(", "").replace(")", "")})"

        return when (attr) {
            "integer" -> "integer(\"$name\")"
            "short" -> "short(\"$name\")"
            "long" -> "long(\"$name\")"
            "float" -> "float(\"$name\")"
            "double" -> "double(\"$name\")"
            "bool" -> "bool(\"$name\")"
            "char" -> "char(\"$name\")"
            "date" -> "date(\"$name\")"
            "time" -> "time(\"$name\")"
            "datetime" -> "datetime(\"$name\")"
            "timestamp" -> "timestamp(\"$name\")"
            "duration" -> "duration(\"$name\")"
            else -> throw Exception("Make sure the types in the json are correct")
        }
    }
 */