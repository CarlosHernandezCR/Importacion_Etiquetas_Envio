package project.domain.service;

import com.opencsv.*;
import org.springframework.stereotype.Service;
import project.common.constants.ConstantesCSV;
import project.domain.dto.FilaImportacionDto;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class LectorCsvService {

    public List<FilaImportacionDto> leerCsv(String ruta) throws Exception {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(ruta), StandardCharsets.UTF_8))) {

            String first = br.readLine();
            if (first == null) throw new IllegalArgumentException(ConstantesCSV.ERR_CSV_VACIO);
            first = stripBom(first);

            char sep = detectSep(first);
            CSVParser parser = new CSVParserBuilder().withSeparator(sep).withIgnoreQuotations(false).build();

            try (CSVReader reader = new CSVReaderBuilder(br).withCSVParser(parser).build()) {
                String[] header = parseHeader(first, sep);
                Map<String,Integer> idx = index(header);

                require(idx, header,
                        ConstantesCSV.H_NUMERO_PEDIDO,
                        ConstantesCSV.H_CIUDAD,
                        ConstantesCSV.H_DIRECCION1,
                        ConstantesCSV.H_ETIQUETA);

                List<FilaImportacionDto> out = new ArrayList<>();
                String[] row; int filaN = 1;

                while ((row = reader.readNext()) != null) {
                    filaN++;
                    if (isBlankRow(row)) continue;

                    FilaImportacionDto d = new FilaImportacionDto();
                    d.setNumeroPedido(parseIntSafe(val(row, idx.get(ConstantesCSV.H_NUMERO_PEDIDO)), filaN));
                    d.setCiudad(     val(row, idx.get(ConstantesCSV.H_CIUDAD)));
                    d.setDireccion1( val(row, idx.get(ConstantesCSV.H_DIRECCION1)));
                    d.setEtiqueta(   val(row, idx.get(ConstantesCSV.H_ETIQUETA)));
                    out.add(d);
                }
                return out;
            }
        }
    }

    private static String[] parseHeader(String first, char sep) {
        String regex = Pattern.quote(String.valueOf(sep));
        return Arrays.stream(first.split(regex, -1)).map(String::trim).toArray(String[]::new);
    }

    private static char detectSep(String first){
        int sc = count(first, ConstantesCSV.SEP_FALLBACK_SEMI);
        int cc = count(first, ConstantesCSV.SEP_FALLBACK_COMA);
        return sc >= cc ? ConstantesCSV.SEP_FALLBACK_SEMI : ConstantesCSV.SEP_FALLBACK_COMA;
    }

    private static int count(String s, char c){ int n=0; for(char ch: s.toCharArray()) if(ch==c) n++; return n; }

    private static Map<String,Integer> index(String[] header){
        Map<String,Integer> m = new HashMap<>();
        for (int i=0;i<header.length;i++)
            m.put(stripBom(header[i]).trim().toLowerCase(), i);
        return m;
    }

    private static void require(Map<String,Integer> idx, String[] header, String... keys){
        for (String k: keys)
            if (!idx.containsKey(k))
                throw new IllegalArgumentException(ConstantesCSV.ERR_FALTA_CABECERA + k
                        + ConstantesCSV.ERR_CABECERAS_PREFIX + String.join(",", header));
    }

    private static String stripBom(String s){ return s!=null && s.startsWith("\uFEFF") ? s.substring(1) : s; }

    private static boolean isBlankRow(String[] r){ for (String s: r) if (s!=null && !s.trim().isEmpty()) return false; return true; }

    private static String val(String[] row, int i){ return i<row.length ? Objects.toString(row[i],"").trim() : ""; }

    private static int parseIntSafe(String s, int fila){
        try { return Integer.parseInt(s); }
        catch(Exception e){ throw new IllegalArgumentException(ConstantesCSV.ERR_NUMERO_INVALIDO + fila + ": '" + s + "'"); }
    }
}
