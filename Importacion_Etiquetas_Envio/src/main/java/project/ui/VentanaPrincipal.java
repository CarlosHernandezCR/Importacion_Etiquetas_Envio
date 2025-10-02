package project.ui;

import project.common.constants.ConstantesUI;
import project.domain.dto.FilaImportacionDto;
import project.domain.service.ImportacionService;
import project.domain.service.LectorCsvService;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.List;

public class VentanaPrincipal extends JFrame {

    private final JButton botonSubir = new JButton(ConstantesUI.TEXTO_BOTON_SUBIR);
    private final JTextArea areaLog = new JTextArea();
    private final LectorCsvService lectorCsv;
    private final ImportacionService importador;

    public VentanaPrincipal(LectorCsvService lectorCsv, ImportacionService importador) {
        this.lectorCsv = lectorCsv;
        this.importador = importador;

        setTitle(ConstantesUI.TITULO_APP);
        setSize(ConstantesUI.ANCHO_VENTANA, ConstantesUI.ALTO_VENTANA);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        areaLog.setEditable(false);
        areaLog.setLineWrap(true);

        JScrollPane scroll = new JScrollPane(areaLog);
        scroll.setBorder(BorderFactory.createTitledBorder(ConstantesUI.TEXTO_ESTADO));

        botonSubir.setFont(new Font(ConstantesUI.FUENTE_NOMBRE, Font.BOLD, ConstantesUI.FUENTE_TAMANO));
        botonSubir.addActionListener(e -> seleccionarArchivo());

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(botonSubir, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        add(panel);
    }


    private void seleccionarArchivo() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle(ConstantesUI.TITULO_DIALOGO);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        FileNameExtensionFilter filtroCsv =
                new FileNameExtensionFilter(ConstantesUI.FILTRO_DESCRIPCION_CSV, ConstantesUI.EXTENSION_CSV);
        chooser.setFileFilter(filtroCsv);

        int opcion = chooser.showOpenDialog(this);
        if (opcion == JFileChooser.APPROVE_OPTION) {
            File archivo = chooser.getSelectedFile();
            procesarArchivo(archivo);
        }
    }



    private void procesarArchivo(File archivo) {
        botonSubir.setEnabled(false);
        areaLog.setText(ConstantesUI.MSG_CARGANDO + "\n");

        SwingWorker<Void, String> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                try {
                    List<FilaImportacionDto> filas = lectorCsv.leerCsv(archivo.getAbsolutePath());
                    int correctos = 0;
                    for (FilaImportacionDto fila : filas) {
                        try {
                            importador.procesarFila(fila);
                            correctos++;
                            publish(ConstantesUI.MSG_OK_PEDIDO
                                    + fila.getNumeroPedido() + " -> " + fila.getEtiqueta());
                        } catch (RuntimeException ex) {
                            publish(ConstantesUI.MSG_ERROR_PEDIDO
                                    + fila.getNumeroPedido() + ": " + ex.getMessage());
                        }
                    }
                    publish("\n" + ConstantesUI.MSG_FINALIZADO
                            + correctos + "/" + filas.size());
                } catch (Exception e) {
                    publish(ConstantesUI.MSG_ERROR_LEER + e.getMessage());
                }
                return null;
            }

            @Override
            protected void process(List<String> mensajes) {
                for (String msg : mensajes) {
                    areaLog.append(msg + "\n");
                }
            }

            @Override
            protected void done() {
                botonSubir.setEnabled(true);
            }
        };
        worker.execute();
    }
}
