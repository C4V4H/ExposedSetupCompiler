package org.coms.plugins

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.coms.dao.daoUtenti

fun Application.utenti() {
    val dao = daoUtenti
    routing {
        route("utenti") {
            //Get per prendere tutti i Utenti
            get {
                call.respondText(Json.encodeToString(dao.all()))
            }
            //Get per mettere un nuovo Utente
            post {
                val formParameters = call.receiveParameters()
                val utente = dao.add(
                    mail = formParameters.getOrFail("mail"), 
		
                )
                call.respondRedirect("/utenti/" + utente?.id)
            }
            //Get per prendere un Utente specifico con la PK
            get("{id}") {
                val id = call.parameters.getOrFail<Int>("id").toInt()
                call.respondText(Json.encodeToString(dao.get(id)))
            }
            // post dando la pk per aggiornare o cancellare un Utente (_action = "update" o "delete")
            post("{id}") {
                val id = call.parameters.getOrFail<Int>("id").toInt()
                val formParameters = call.receiveParameters()
                when (formParameters.getOrFail("_action")) {
                    "update" -> {
                        dao.edit(
                        id = formParameters.getOrFail("id").toInt(),
                            mail = formParameters.getOrFail("mail"), 
		
                        )
                        call.respondRedirect("/utenti/$id")
                    }
                    "delete" -> {
                        call.respondText(if (dao.delete(id)) "succes" else "failed")
                    }
                }
            }
        }
    }
}