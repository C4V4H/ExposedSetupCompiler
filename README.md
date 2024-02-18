## Exposed Setup Compiler

A Kotlin library for setting up a KTor Server using Exposed.

Ideally, given a JSON file, this project will "compile" it, creating three types of files:

    One containing a data class, a Table (Exposed) class, and an interface (DAOFacade) for basic database interaction methods.
    One with the implementation of the interface (DAOFacadeImpl).
    Lastly, one that handles the routing for that entity.

### NOT COMPLETE YET
