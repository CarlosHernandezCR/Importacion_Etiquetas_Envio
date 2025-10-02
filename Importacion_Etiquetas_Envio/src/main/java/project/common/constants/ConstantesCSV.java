package project.common.constants;

public class ConstantesCSV {
    public static final String H_NUMERO_PEDIDO = "numeropedido";
    public static final String H_CIUDAD        = "ciudad";
    public static final String H_DIRECCION1    = "direccion1";
    public static final String H_ETIQUETA      = "etiqueta";

    public static final String ERR_CSV_VACIO         = "CSV vacío";
    public static final String ERR_FALTA_CABECERA    = "Falta cabecera '";
    public static final String ERR_CABECERAS_PREFIX  = "'. Cabeceras: ";
    public static final String ERR_NUMERO_INVALIDO   = "numeroPedido inválido en fila ";
    public static final String DBG_HEADER_NORMALIZADO= "HEADER NORMALIZADO: ";
    public static final String DBG_FILA_PREFIX       = "CSV-> num=%d, ciudad=%s, dir=%s, etiqueta=%s%n";

    public static final char SEP_FALLBACK_SEMI = ';';
    public static final char SEP_FALLBACK_COMA = ',';

    private ConstantesCSV() {}
}
