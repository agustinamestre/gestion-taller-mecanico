package com.taller.gestion_taller.application.usecases.cliente;

import com.taller.gestion_taller.application.command.RegistrarClienteCommand;
import com.taller.gestion_taller.application.mapper.ClienteApplicationMapper;
import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
import com.taller.gestion_taller.domain.model.Cliente;
import com.taller.gestion_taller.domain.repositories.ClienteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class RegistrarClienteUseCase implements RegistrarCliente {

    private final ClienteRepository clienteRepository;
    private final ClienteApplicationMapper clienteApplicationMapper;

    @Override
    public Cliente registrarCliente(RegistrarClienteCommand command) {
        // 1. Command -> Dominio
        Cliente cliente = clienteApplicationMapper.commandToDomain(command);

        clienteRepository.findByDni(cliente.getDni()).ifPresent(existente -> {
            throw new BusinessRunTimeException(BusinessErrors.clienteDniDuplicado(cliente.getDni()));
        });

        // 2. Persistencia
        Cliente guardado = clienteRepository.save(cliente);

        log.info("Cliente registrado exitosamente - ID: {}, DNI: {}", guardado.getId(), guardado.getDni());

        // 3. Retorna dominio (la conversión a Response la hace el Controller)
        return guardado;
    }
}
