package project.domain.service;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.stereotype.Service;
import project.domain.dto.FilaImportacionDto;

import java.io.FileReader;
import java.io.Reader;
import java.util.List;

@Service
public class LectorCsvService {

    public List<FilaImportacionDto> leerCsv(String rutaFichero) throws Exception {
        try (Reader reader = new FileReader(rutaFichero)) {
            CsvToBean<FilaImportacionDto> csvToBean = new CsvToBeanBuilder<FilaImportacionDto>(reader)
                    .withType(FilaImportacionDto.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withSeparator(';')
                    .build();

            return csvToBean.parse();
        }
    }
}
