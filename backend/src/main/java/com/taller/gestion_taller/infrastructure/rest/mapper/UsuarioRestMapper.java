package com.taller.gestion_taller.infrastructure.rest.mapper;

import com.taller.gestion_taller.application.command.usuario.CambiarPasswordCommand;
import com.taller.gestion_taller.application.command.usuario.ModificarPerfilPropioCommand;
import com.taller.gestion_taller.application.command.usuario.ModificarUsuarioCommand;
import com.taller.gestion_taller.application.command.usuario.RegistrarUsuarioCommand;
import com.taller.gestion_taller.domain.model.Rol;
import com.taller.gestion_taller.domain.model.Usuario;
import com.taller.gestion_taller.infrastructure.rest.dto.usuario.request.CambiarPasswordRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.usuario.request.ModificarPerfilPropioRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.usuario.request.ModificarUsuarioRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.usuario.request.UsuarioRequest;
import com.taller.gestion_taller.infrastructure.rest.dto.usuario.response.UsuarioResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UsuarioRestMapper {

    @Mapping(target = "rol", expression = "java(Rol.valueOf(request.rol()))")
    RegistrarUsuarioCommand requestToCommand(UsuarioRequest request);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "rol", expression = "java(Rol.valueOf(request.rol()))")
    ModificarUsuarioCommand requestToModificarCommand(Long id, ModificarUsuarioRequest request);

    @Mapping(target = "username", source = "username")
    ModificarPerfilPropioCommand requestToModificarPerfilCommand(String username, ModificarPerfilPropioRequest request);

    @Mapping(target = "username", source = "username")
    CambiarPasswordCommand requestToCambiarPasswordCommand(String username, CambiarPasswordRequest request);

    UsuarioResponse domainToResponse(Usuario usuario);

    default Rol stringToRol(String rol) {
        if (rol == null) return null;
        return Rol.valueOf(rol);
    }
}