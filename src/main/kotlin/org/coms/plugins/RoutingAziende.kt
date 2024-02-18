package org.coms.plugins

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.coms.dao.daoAziende

fun Application.aziende() {
    val dao = daoAziende
    routing {
        route("aziende") {
            //Get per prendere tutti i Aziende
            get {
                call.respondText(Json.encodeToString(dao.all()))
            }
            //Get per mettere un nuovo Azienda
            post {
                val formParameters = call.receiveParameters()
                val azienda = dao.add(
                    nome = formParameters.getOrFail("nome"), 
		
                )
                call.respondRedirect("/aziende/" + azienda?.id)
            }
            //Get per prendere un Azienda specifico con la PK
            get("{id}") {
                val id = call.parameters.getOrFail<Int>("id").toInt()
                call.respondText(Json.encodeToString(dao.get(id)))
            }
            // post dando la pk per aggiornare o cancellare un Azienda (_action = "update" o "delete")
            post("{id}") {
                val id = call.parameters.getOrFail<Int>("id").toInt()
                val formParameters = call.receiveParameters()
                when (formParameters.getOrFail("_action")) {
                    "update" -> {
                        dao.edit(
                        id = formParameters.getOrFail("id").toInt(),
                            nome = formParameters.getOrFail("nome"), 
		
                        )
                        call.respondRedirect("/aziende/$id")
                    }
                    "delete" -> {
                        call.respondText(if (dao.delete(id)) "succes" else "failed")
                    }
                }
            }
        }
    }
}