package com.piuraexpressa.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioLikePublicacionDTO {
    private Long usuarioId;
    private Long publicacionId;
    private LocalDateTime fechaLike;
}
