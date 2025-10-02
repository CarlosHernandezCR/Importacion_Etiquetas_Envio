package project.domain.service;

import org.springframework.stereotype.Service;
import project.common.constants.ConstantesError;
import project.common.constants.ConstantesService;
import project.domain.dto.FilaImportacionDto;
import project.domain.errors.CustomedException;
import project.domain.model.ShopifyOrder;

@Service
public class ImportacionService {
    private final ShopifyClient shopify;
    public ImportacionService(ShopifyClient shopify){ this.shopify = shopify; }

    public String procesarFila(FilaImportacionDto f) {
        ShopifyOrder o = shopify.buscarPedidoPorNumero(f.getNumeroPedido());
        if (o == null) throw new CustomedException(ConstantesError.PEDIDO_NO_ENCONTRADO + f.getNumeroPedido());
        String ciudad = o.getShipping_address() != null ? o.getShipping_address().getCity() : "";
        String dir    = o.getShipping_address() != null ? o.getShipping_address().getAddress1() : "";
        return String.format(ConstantesService.OK_PEDIDO, f.getNumeroPedido(), o.getId(), ciudad, dir);
    }
}
