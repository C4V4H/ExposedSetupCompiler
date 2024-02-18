package org.coms.plugins

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.coms.dao.daoClienti

fun Application.clienti() {
    val dao = daoClienti
    routing {
        route("clienti") {
            //Get per prendere tutti i Clienti
            get {
                call.respondText(Json.encodeToString(dao.all()))
            }
            //Get per mettere un nuovo Cliente
            post {
                val formParameters = call.receiveParameters()
                val cliente = dao.add(
                    cognome = formParameters.getOrFail("cognome"), 
		nome = formParameters.getOrFail("nome"), 
		indirizzo = formParameters.getOrFail("indirizzo"), 
		citta = formParameters.getOrFail("citta"), 
		provincia = formParameters.getOrFail("provincia"), 
		cap = formParameters.getOrFail("cap"), 
		nazione = formParameters.getOrFail("nazione"), 
		mail = formParameters.getOrFail("mail"), 
		telefono = formParameters.getOrFail("telefono"), 
		partitaIva = formParameters.getOrFail("partitaIva"), 
		codiceFiscale = formParameters.getOrFail("codiceFiscale"), 
		idUtente = formParameters.getOrFail("idUtente").toInt(), 
		idAzienda = formParameters.getOrFail("idAzienda").toInt(), 
		
                )
                call.respondRedirect("/clienti/" + cliente?.id)
            }
            //Get per prendere un Cliente specifico con la PK
            get("{id}") {
                val id = call.parameters.getOrFail<Int>("id").toInt()
                call.respondText(Json.encodeToString(dao.get(id)))
            }
            // post dando la pk per aggiornare o cancellare un Cliente (_action = "update" o "delete")
            post("{id}") {
                val id = call.parameters.getOrFail<Int>("id").toInt()
                val formParameters = call.receiveParameters()
                when (formParameters.getOrFail("_action")) {
                    "update" -> {
                        dao.edit(
                        id = formParameters.getOrFail("id").toInt(),
                            cognome = formParameters.getOrFail("cognome"), 
		nome = formParameters.getOrFail("nome"), 
		indirizzo = formParameters.getOrFail("indirizzo"), 
		citta = formParameters.getOrFail("citta"), 
		provincia = formParameters.getOrFail("provincia"), 
		cap = formParameters.getOrFail("cap"), 
		nazione = formParameters.getOrFail("nazione"), 
		mail = formParameters.getOrFail("mail"), 
		telefono = formParameters.getOrFail("telefono"), 
		partitaIva = formParameters.getOrFail("partitaIva"), 
		codiceFiscale = formParameters.getOrFail("codiceFiscale"), 
		idUtente = formParameters.getOrFail("idUtente").toInt(), 
		idAzienda = formParameters.getOrFail("idAzienda").toInt(), 
		
                        )
                        call.respondRedirect("/clienti/$id")
                    }
                    "delete" -> {
                        call.respondText(if (dao.delete(id)) "succes" else "failed")
                    }
                }
            }
        }
    }
}