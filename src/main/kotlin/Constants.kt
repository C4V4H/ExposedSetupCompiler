import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer

/**
 * Currently supported Types are:
 * string (default "varchar(0)", better call varchar function)
 * integer
 * short
 * long
 * float
 * double
 * bool
 * char
 * date
 * time
 * datetime
 * timestamp
 * duration
 */
enum class DataType(var exposed: String, val kotlin: String) {
    STRING("varchar(0)", "String"),
    INTEGER("integer", "Int"),
    SHORT("short", "Short"),
    LONG("long", "Long"),
    FLOAT("float", "Float"),
    DOUBLE("double", "Double"),
    BOOLEAN("bool", "Boolean"),
    CHAR("char", "Char"),
    DATE("date", "java.time.LocalDate"),
    DATETIME("datetime", "java.time.LocalDateTime"),
    TIMESTAMP("timestamp", "java.sql.Timestamp"),
    NONE("null", "null"),
    DURATION("duration", "java.time.Duration");

    companion object {
        private fun varchar(length: Int): DataType {
            STRING.exposed = "varchar($length)"
            return STRING
        }

        fun fromExposedType(exposedType: String): DataType {
            return entries.find { it.exposed == exposedType.toLower() } ?: varchar(
                exposedType.replace("varchar(", "").replace(")", "").toInt()
            )
        }
    }
}

/**
 * Reference is used to pass a reference to the builder
 * for example Reference(name = "example", type = DataType.INTEGER)
 */
data class Attribute(
    val name: String = "",
    val type: DataType = DataType.NONE,
)

/**
 * enables the to assign like "Pair"
 */
infix fun String.to(type: DataType): Attribute {
    return Attribute(this, type)
}

/**
 * Reference is used to pass a reference to the builder
 * for example Reference(name = "idExample", type = DataType.INTEGER, reference = "Examples.id")
 */
data class Reference(
    val name: String = "",
    val type: DataType = DataType.NONE,
    val reference: String = ""
)

data class TableInfo(
    val dataClassName: String = "",
    val tableName: String = "",
    val attributes: Array<Attribute> = emptyArray(),
    val references: Array<Reference> = emptyArray(),
    val primaryKey: Attribute = Attribute()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TableInfo

        if (dataClassName != other.dataClassName) return false
        if (tableName != other.tableName) return false
        if (!attributes.contentEquals(other.attributes)) return false
        if (!references.contentEquals(other.references)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = dataClassName.hashCode()
        result = 31 * result + tableName.hashCode()
        result = 31 * result + attributes.contentHashCode()
        result = 31 * result + references.contentHashCode()
        return result
    }
}

class DataTypeDeserializer : JsonDeserializer<DataType>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext?): DataType {
        val value = p.text.trim()
        return DataType.fromExposedType(value)
    }
}