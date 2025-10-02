package project.common.constants;

public class ConstantesCSV {
    // Cabeceras normalizadas (en minúsculas, porque el LectorCsvService hace toLowerCase())
    public static final String H_NUMERO_PEDIDO = "numeropedido";
    public static final String H_ALMACEN       = "almacen";
    public static final String H_ETIQUETA      = "etiqueta";

    // Mensajes de error y debug
    public static final String ERR_CSV_VACIO          = "CSV vacío";
    public static final String ERR_FALTA_CABECERA     = "Falta cabecera '";
    public static final String ERR_CABECERAS_PREFIX   = "'. Cabeceras: ";
    public static final String ERR_NUMERO_INVALIDO    = "numeroPedido inválido en fila ";
    public static final String DBG_HEADER_NORMALIZADO = "HEADER NORMALIZADO: ";
    public static final String DBG_FILA_PREFIX        = "CSV-> num=%d, almacen=%s, etiqueta=%s%n";

    // Separadores fallback
    public static final char SEP_FALLBACK_SEMI = ';';
    public static final char SEP_FALLBACK_COMA = ',';

    private ConstantesCSV() {}
}
