package com.piuraexpressa.servicio;

public interface UsuarioEventoServicio {
    void participarEnEvento(Long eventoId, String username);
    void cancelarParticipacion(Long eventoId, String username);
    boolean yaEstaParticipando(Long eventoId, Long usuarioId);
    int contarParticipantes(Long eventoId);
    boolean yaResenoEvento(Long eventoId, Long usuarioId);

}
