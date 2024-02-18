import java.io.File
import java.io.FileWriter

class DAOCompiler(private var packageName: String = "org/coms") {

    fun createDir(path: String, nome: String) {
        val folder = File(path, nome)
        if (!folder.exists()) {
            folder.mkdirs()
            println("Cartella creata con successo: ${folder.absolutePath}")
        } else
            println("La cartella esiste già: ${folder.absolutePath}")
    }

    fun createAndWriteFile(path: String, nome: String, content: String) {
        val file = File(path, nome)
        FileWriter(file).use { writer ->
            writer.write(content)
        }
        println("File creato e scritto con successo: ${file.absolutePath}")
    }

    fun setUpEnviroment(
        dataClassName: String,
        tableName: String,
        attributes: Array<Attribute>,
        references: Array<Reference>,
        primaryKey: Attribute = Attribute("id", DataType.INTEGER)
    ) {
        val path = "src/main/kotlin/$packageName"
        createDir(path, "models")
        createDir(path, "plugins")
        createDir(path, "dao")

        val modelClass = ModelClassGenerator(
            dataClassName = dataClassName,
            tableName = tableName,
            attributes = attributes,
            references = references,
            primaryKey = primaryKey
        )
        val daoClass = DaoMethodGenerator(
            dataClassName = dataClassName,
            tableName = tableName,
            attributes = attributes,
            references = references,
            primaryKey = primaryKey
        )
        val routing = RoutingGenerator(
            dataClassName = dataClassName,
            tableName = tableName,
            attributes = attributes,
            references = references,
            primaryKey = primaryKey
        )

        createAndWriteFile(
            "src/main/kotlin/org/coms/models",
            "$dataClassName.kt",
            modelClass.generateClassContent()
        )
        createAndWriteFile(
            "src/main/kotlin/org/coms/dao",
            "DAOFacade${tableName}Impl.kt",
            daoClass.generateClassContent()
        )
        createAndWriteFile(
            "src/main/kotlin/org/coms/plugins",
            "Routing${tableName}.kt",
            routing.generateClassContent()
        )
    }

}
