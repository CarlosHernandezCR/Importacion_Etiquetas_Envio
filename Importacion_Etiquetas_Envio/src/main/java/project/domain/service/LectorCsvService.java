package project.domain.service;

import com.opencsv.*;
import org.springframework.stereotype.Service;
import project.domain.dto.FilaImportacionDto;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static project.common.constants.ConstantesCSV.*;
import static project.common.constants.ConstantesCSV.ERR_CSV_VACIO;
import static project.common.constants.ConstantesCSV.ERR_FALTA_CABECERA;
import static project.common.constants.ConstantesCsvReader.*;

@Service
public class LectorCsvService {

    public List<FilaImportacionDto> leerCsv(String ruta) throws Exception {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(ruta), StandardCharsets.UTF_8))) {

            String first = br.readLine();
            if (first == null) throw new IllegalArgumentException(ERR_CSV_VACIO);
            first = stripBom(first);

            char sep = detectSep(first);
            CSVParser parser = new CSVParserBuilder()
                    .withSeparator(sep)
                    .withIgnoreQuotations(false)
                    .build();

            try (CSVReader reader = new CSVReaderBuilder(br).withCSVParser(parser).build()) {
                String[] header = parseHeader(first, sep);
                Map<String,Integer> idx = index(header);

                require(idx, header, H_NUMERO_PEDIDO, H_ALMACEN, H_ETIQUETA);

                List<FilaImportacionDto> out = new ArrayList<>();
                String[] row;
                int filaN = 1;
                while ((row = reader.readNext()) != null) {
                    filaN++;
                    if (isBlankRow(row)) continue;

                    FilaImportacionDto d = new FilaImportacionDto();
                    d.setNumeroPedido(parseIntSafe(val(row, idx.get(H_NUMERO_PEDIDO)), filaN));
                    d.setAlmacen(val(row, idx.get(H_ALMACEN)));
                    d.setEtiqueta(val(row, idx.get(H_ETIQUETA)));
                    out.add(d);
                }

                System.out.println(HEADER_NORMALIZADO + Arrays.toString(header));
                out.forEach(d -> System.out.printf(CSV_DEBUG_FMT,
                        d.getNumeroPedido(), d.getAlmacen(), d.getEtiqueta()));
                return out;
            }
        }
    }

    private static String[] parseHeader(String first, char sep) {
        String regex = String.format(REGEX_ESCAPE, sep);
        return Arrays.stream(first.split(regex, -1))
                .map(String::trim)
                .toArray(String[]::new);
    }

    private static char detectSep(String first){
        int sc = count(first, SEP_PUNTO_COMA), cc = count(first, SEP_COMA);
        return sc >= cc ? SEP_PUNTO_COMA : SEP_COMA;
    }
    private static int count(String s, char c){
        int n=0; for(char ch: s.toCharArray()) if(ch==c) n++; return n;
    }
    private static Map<String,Integer> index(String[] header){
        Map<String,Integer> m = new HashMap<>();
        for (int i=0;i<header.length;i++) m.put(stripBom(header[i]).trim().toLowerCase(), i);
        return m;
    }
    private static void require(Map<String,Integer> idx, String[] header, String... keys){
        for (String k: keys)
            if (!idx.containsKey(k))
                throw new IllegalArgumentException(ERR_FALTA_CABECERA + k + "'. Cabeceras: " + String.join(",", header));
    }
    private static String stripBom(String s){ return s!=null && s.startsWith("\uFEFF") ? s.substring(1) : s; }
    private static boolean isBlankRow(String[] r){
        for (String s: r) if (s!=null && !s.trim().isEmpty()) return false; return true;
    }
    private static String val(String[] row, int i){ return i<row.length ? Objects.toString(row[i],"").trim() : ""; }
    private static int parseIntSafe(String s, int fila){
        try { return Integer.parseInt(s); }
        catch(Exception e){ throw new IllegalArgumentException(ERR_NUM_INVALIDO + fila + ": '" + s + "'"); }
    }
}
