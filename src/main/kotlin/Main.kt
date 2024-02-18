import DataType.*
import DataType.Companion.varchar

fun main() {
    val dao = DAOCompiler()

    dao.setUpEnviroment(
        dataClassName = "Cliente",
        tableName = "Clienti",
        attributes = arrayOf(
            "cognome" to varchar(100),
            "nome" to varchar(100),
            "indirizzo" to varchar(100),
            "citta" to varchar(100),
            "provincia" to varchar(100),
            "cap" to varchar(100),
            "nazione" to varchar(100),
            "mail" to varchar(100),
            "telefono" to varchar(100),
            "partitaIva" to varchar(100),
            "codiceFiscale" to varchar(100)
        ),
        references = arrayOf(
            "idUtente" to INTEGER to "Utenti.id",
            "idAzienda" to INTEGER to "Aziende.id"
        ),
        primaryKey = "id" to INTEGER
    )


    dao.setUpEnviroment(
        dataClassName = "Azienda",
        tableName = "Aziende",
        attributes = arrayOf(
            "nome" to varchar(100)
        ),
        references = arrayOf(),
        primaryKey = "id" to INTEGER
    )


    dao.setUpEnviroment(
        dataClassName = "Utente",
        tableName = "Utenti",
        attributes = arrayOf(
            "mail" to varchar(100),
        ),
        references = arrayOf(),
        primaryKey = "id" to INTEGER
    )
}

/*
        varchar($lenght)
        integer
        short
        long
        float
        double         
        bool
        char
        date
        time
        datetime
        timestamp
        duration
val dao = DAOCompiler()

    dao.setUpEnviroment(
        dataClassName = "Cliente",
        tableName = "Clienti",
        attributes = hashMapOf(
            "cognome" to varchar(100),
            "nome" to varchar(100),
            "indirizzo" to varchar(100),
            "citta" to varchar(100),
            "provincia" to varchar(100),
            "cap" to varchar(100),
            "nazione" to varchar(100),
            "mail" to varchar(100),
            "telefono" to varchar(100),
            "partitaIva" to varchar(100),
            "codiceFiscale" to varchar(100)
        ),
        references = hashMapOf(
            "idUtente" to arrayOf(INTEGER, "Utenti.id"),
            "idAzienda" to arrayOf(INTEGER, "Aziende.id")
        ),
        primaryKey = arrayOf("id", INTEGER)
    )


    dao.setUpEnviroment(
        dataClassName = "Azienda",
        tableName = "Aziende",
        attributes = hashMapOf(
            "nome" to varchar(100)
        ),
        references = hashMapOf(),
        primaryKey = arrayOf("id", INTEGER)
    )


    dao.setUpEnviroment(
        dataClassName = "Utente",
        tableName = "Utenti",
        attributes = hashMapOf(
            "mail" to varchar(100),
        ),
        references = hashMapOf(),
        primaryKey = arrayOf("id", INTEGER)
    )
*/