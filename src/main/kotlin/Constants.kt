enum class DataType(var exposedType: String, val kotlinType: String) {
    STRING("varchar", "String"),
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
    DURATION("duration", "java.time.Duration");

    companion object {
        fun varchar(length: Int): DataType {
            STRING.exposedType = "varchar($length)"
            return STRING
        }
    }
}

/**
 * Reference is used to pass a reference to the builder
 * for example Reference(name = "example", type = DataType.INTEGER)
 */
data class Attribute(
    val name: String,
    val type: DataType,
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
    val attribute: Attribute,
    val referece: String
)

/**
 * enables the triple "to" assign
 */
infix fun Attribute.to(reference: String): Reference {
    return Reference(this, reference)
}