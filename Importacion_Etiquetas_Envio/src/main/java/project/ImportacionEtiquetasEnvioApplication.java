package project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import project.domain.service.ImportacionService;
import project.domain.service.LectorCsvService;
import project.ui.VentanaPrincipal;

import javax.swing.*;

import com.formdev.flatlaf.FlatLightLaf;

@SpringBootApplication
public class ImportacionEtiquetasEnvioApplication {
    public static void main(String[] args) {
        FlatLightLaf.setup();
        var ctx = SpringApplication.run(ImportacionEtiquetasEnvioApplication.class, args);
        var lector = ctx.getBean(LectorCsvService.class);
        var importador = ctx.getBean(ImportacionService.class);
        SwingUtilities.invokeLater(() ->
                new VentanaPrincipal(lector, importador).setVisible(true)
        );
    }
}


