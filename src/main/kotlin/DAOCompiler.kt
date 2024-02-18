import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File
import java.io.FileWriter

class DAOCompiler(private var packageName: String = "org/example") {

    fun build(jsonPath: String) {
        val tablesJsonFile = File(jsonPath)
        val objectMapper = ObjectMapper()
        val module = SimpleModule()
        module.addDeserializer(DataType::class.java, DataTypeDeserializer())
        objectMapper.registerModule(module)

        val tableInfos: Array<TableInfo> = objectMapper.readValue(tablesJsonFile)
        // Passaggio dei dati alla funzione
        processTableInfos(tableInfos)

        tableInfos.forEach {
            setUpEnviroment(it)
        }
    }

    private fun processTableInfos(tableInfos: Array<TableInfo>) {
        // Qui puoi implementare la logica per elaborare le informazioni della tabella
        tableInfos.forEach { tableInfo ->
            println("Data Class Name: ${tableInfo.dataClassName}")
            println("Table Name: ${tableInfo.tableName}")
            println("Attributes:")
            tableInfo.attributes.forEach { attribute ->
                println("  ${attribute.name}: ${attribute.type.kotlin}")
            }
            println(if (tableInfo.references.isNotEmpty()) "References:" else "")
            tableInfo.references.forEach { reference ->
                println("  Attribute: ${reference.name}, Reference: ${reference.reference}")
            }
            println()
        }
    }


    private fun createDir(path: String, nome: String) {
        val folder = File(path, nome)
        if (!folder.exists()) {
            folder.mkdirs()
            println("Cartella creata con successo: ${folder.absolutePath}")
        } else
            println("La cartella esiste già: ${folder.absolutePath}")
    }

    private fun createAndWriteFile(path: String, nome: String, content: String) {
        val file = File(path, nome)
        FileWriter(file).use { writer ->
            writer.write(content)
        }
        println("File creato e scritto con successo: ${file.absolutePath}")
    }

    private fun setUpEnviroment(
        tableInfo: TableInfo
    ) {
        val path = "src/main/kotlin/$packageName"
        createDir(path, "models")
        createDir(path, "plugins")
        createDir(path, "dao")

        val modelClass = ModelClassGenerator(
            dataClassName = tableInfo.dataClassName,
            tableName = tableInfo.tableName,
            attributes = tableInfo.attributes,
            references = tableInfo.references,
            primaryKey = tableInfo.primaryKey
        )
        val daoClass = DaoMethodGenerator(
            dataClassName = tableInfo.dataClassName,
            tableName = tableInfo.tableName,
            attributes = tableInfo.attributes,
            references = tableInfo.references,
            primaryKey = tableInfo.primaryKey
        )
        val routing = RoutingGenerator(
            dataClassName = tableInfo.dataClassName,
            tableName = tableInfo.tableName,
            attributes = tableInfo.attributes,
            references = tableInfo.references,
            primaryKey = tableInfo.primaryKey
        )

        createAndWriteFile(
            "src/main/kotlin/org/coms/models",
            "${tableInfo.dataClassName}.kt",
            modelClass.generateClassContent()
        )
        createAndWriteFile(
            "src/main/kotlin/org/coms/dao",
            "DAOFacade${tableInfo.tableName}Impl.kt",
            daoClass.generateClassContent()
        )
        createAndWriteFile(
            "src/main/kotlin/org/coms/plugins",
            "Routing${tableInfo.tableName}.kt",
            routing.generateClassContent()
        )
    }

}
