package project.domain.dto;

import lombok.Data;
import lombok.ToString;

import static project.common.constants.ConstantesCSV.*;

@Data
@ToString
public class FilaImportacionDto {
    @com.opencsv.bean.CsvBindByName(column = H_NUMERO_PEDIDO)
    private int numeroPedido;

    @com.opencsv.bean.CsvBindByName(column = H_ALMACEN)
    private String almacen;

    @com.opencsv.bean.CsvBindByName(column = H_ETIQUETA)
    private String etiqueta;
}
