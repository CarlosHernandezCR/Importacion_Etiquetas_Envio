package project.domain.service;

import org.springframework.stereotype.Service;
import project.common.constants.ConstantesService;
import project.domain.dto.FilaImportacionDto;
import project.domain.errors.CustomedException;

@Service
public class ImportacionService {

    public void procesarFila(FilaImportacionDto fila) {
        if (fila.getCiudad() == null || fila.getCiudad().isBlank()) {
            throw new CustomedException(ConstantesService.ERROR_CIUDAD_INVALIDA + fila.getNumeroPedido());
        }


    }
}
