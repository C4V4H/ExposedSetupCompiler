import java.lang.StringBuilder
import java.util.*
import DataType.*

class RoutingGenerator(
    private val dataClassName: String,
    private val tableName: String,
    private val attributes: Array<Attribute>,
    private val references: Array<Reference>,
    private val primaryKey: Attribute = Attribute("id", DataType.INTEGER)
) {
    private val path = tableName.lowercase(Locale.getDefault())
    private val pkType = primaryKey.type.kotlinType

    fun generateClassContent(): String {
        return """
package org.coms.plugins

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.coms.dao.dao$tableName

fun Application.$path() {
    val dao = dao$tableName
    routing {
        route("$path") {
            //Get per prendere tutti i $tableName
            get {
                call.respondText(Json.encodeToString(dao.all()))
            }
            //Get per mettere un nuovo $dataClassName
            post {
                val formParameters = call.receiveParameters()
                val ${dataClassName.toLower()} = dao.add(
                    ${getParam()}
                )
                call.respondRedirect("/$path/" + ${dataClassName.toLower()}?.${primaryKey.name})
            }
            //Get per prendere un $dataClassName specifico con la PK
            get("{${primaryKey.name}}") {
                val ${primaryKey.name} = call.parameters.getOrFail<$pkType>("${primaryKey.name}").to$pkType()
                call.respondText(Json.encodeToString(dao.get(${primaryKey.name})))
            }
            // post dando la pk per aggiornare o cancellare un $dataClassName (_action = "update" o "delete")
            post("{${primaryKey.name}}") {
                val ${primaryKey.name} = call.parameters.getOrFail<$pkType>("${primaryKey.name}").to$pkType()
                val formParameters = call.receiveParameters()
                when (formParameters.getOrFail("_action")) {
                    "update" -> {
                        dao.edit(
                        ${primaryKey.name} = formParameters.getOrFail("${primaryKey.name}")${if (primaryKey.type != STRING) ".to$pkType()" else ""},
                            ${getParam()}
                        )
                        call.respondRedirect("/$path/$${primaryKey.name}")
                    }
                    "delete" -> {
                        call.respondText(if (dao.delete(${primaryKey.name})) "succes" else "failed")
                    }
                }
            }
        }
    }
}
        """.trimIndent()
    }

    private fun getParam(): String {
        val str = StringBuilder("")

        attributes.forEach { (name, type) ->
            str.append("$name = formParameters.getOrFail(\"$name\")${if (type != STRING) ".to$pkType()" else ""}, \n\t\t")
        }
        references.forEach { (attribute, reference) ->
            str.append("${attribute.name} = formParameters.getOrFail(\"${attribute.name}\")${if (attribute.type != STRING) ".to$pkType()" else ""}, \n\t\t")
        }
        return str.toString()
    }

}

fun String.toLower(): String {
    return this.lowercase(Locale.getDefault())
}