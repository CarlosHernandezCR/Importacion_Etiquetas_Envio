package project.common.constants;

public class ConstantesCsvReader {
    public static final String ERR_CSV_VACIO = "CSV vacío";
    public static final String ERR_FALTA_CABECERA = "Falta cabecera '";
    public static final String ERR_NUM_INVALIDO = "numeroPedido inválido en fila ";
    public static final String HEADER_NORMALIZADO = "HEADER NORMALIZADO: ";
    public static final String CSV_DEBUG_FMT = "CSV-> num=%d, almacen=%s, etiqueta=%s%n";

    public static final char SEP_PUNTO_COMA = ';';
    public static final char SEP_COMA       = ',';
    public static final String REGEX_ESCAPE = "\\Q%s\\E"; // usado en String.format
}
