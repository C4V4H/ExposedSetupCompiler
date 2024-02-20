## Exposed Setup Compiler

A Kotlin library for setting up a KTor Server using Exposed.

Ideally, given a JSON file, this project will "compile" it, creating three types of files:

1. One containing a data class, a Table (Exposed) class, and an interface (DAOFacade) for basic database interaction
   methods.
2. One with the implementation of the interface (DAOFacadeImpl).
3. Lastly, one that handles the routing for that entity.

## Usage
First you need a Ktor project, you can create it [here](https://kmp.jetbrains.com/).
Then you need to create a json file contaning an Array of TableInfo, you can fund the class in the Constants.kt file.
So the given JSON file should look like this:

    [
      {
           "dataClassName": "John",
           "tableName": "Johns",
           "attributes": [
               {
                   "name": "name",
                   "type": "varchar(100)"
               },
               {
                   "name": "surname",
                   "type": "varchar(100)"
               }
           ],
           "references": [
               {
                   "name": "idAddress",
                   "type": "INTEGER"
                   "reference": "Addresses.id",
               }
           ],
           "primaryKey": {
               "name": "id",
               "type": "INTEGER"
           }
       },
       {
           "dataClassName": "Address",
           "tableName": "Addresses",
           "attributes": [
               {
                   "name": "city",
                   "type": "varchar(50)"
               }
           ],
           "references": [],
           "primaryKey": {
               "name": "id",
               "type": "INTEGER"
           }
       }
    ]

finally you can Compile the json:

    DAOCompiler("org/example").build("path/to/file.json")
