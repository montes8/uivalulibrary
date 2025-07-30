package com.gb.vale.uivalulibrary.manager.finger


enum class UiTayFingerStatus(val description: String, val title: String, val btn : String) {
    CAN_AUTHENTICATE("Correcto", "CAN_AUTHENTICATE","Entiendo"),
    NO_HARDWARE("No cuenta con hardware de autenticación biometrica.", "NO_HARDWARE","Entiendo"),
    UNAVAILABLE("No se pudo reconocer la funcionalidad. Ingresa la clave para iniciar sesión."
        , "Ocurrió un error","Entiendo"),
    NO_FINGERPRINTS("Para usar tu huella digital es necesario que lo actives desde la configuración de tu celular",
        "Activar huella digital","Ir a Configuración"),
    UNKNOWN_STATUS("No se pudo reconocer la funcionalidad. Ingresa la clave para iniciar sesión."
        , "Ocurrió un error","Entiendo");

}