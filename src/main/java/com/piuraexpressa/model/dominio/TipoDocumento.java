package com.piuraexpressa.model.dominio;

public enum TipoDocumento {
    DNI,
    CARNET_EXTRANJERIA,
    PASAPORTE;

    public static TipoDocumento convertirTipo(String tipo) {
        for (TipoDocumento td : values()) {
            if (td.name().equalsIgnoreCase(tipo)) {
                return td;
            }
        }
        throw new IllegalArgumentException("Tipo de documento inválido: " + tipo);
    }
}
