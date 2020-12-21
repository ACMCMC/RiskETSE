package risk;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public abstract class FileCreatorFallback {

    private static final Charset ENCODING = StandardCharsets.UTF_8;

    private static final String NEW_LINE = System.getProperty("line.separator");
    private static final String archivoPaisesCoordenadas[] = {"Alaska;Alaska;América del Norte;AméricaNorte;0;0","Territorios del Noroeste;TNoroeste;América del Norte;AméricaNorte;1;0","Groenlandia;Groenlan;América del Norte;AméricaNorte;2;0","Islandia;Islandia;Europa;Europa;4;0","Escandinavia;Escandina;Europa;Europa;5;0","Siberia;Siberia;Asia;Asia;6;0","Yakustsk;Yakustsk;Asia;Asia;7;0","Kamchatka;Kamchatka;Asia;Asia;8;0","Alberta;Alberta;América del Norte;AméricaNorte;0;1","Ontario;Ontario;América del Norte;AméricaNorte;1;1","Quebec;Quebec;América del Norte;AméricaNorte;2;1","Gran Bretaña;GBretaña;Europa;Europa;5;1","Europa del Norte;EurNorte;Europa;Europa;6;1","Rusia;Rusia;Europa;Europa;7;1","Irkutsk;Irkutsk;Asia;Asia;8;1","Estados Unidos del Oeste;USAOeste;América del Norte;AméricaNorte;0;2","Estados Unidos del Este;USAEste;América del Norte;AméricaNorte;1;2","Europa Occidental;EurOcc;Europa;Europa;5;2","Europa del Sur;EurSur;Europa;Europa;6;2","Urales;Urales;Asia;Asia;7;2","Mongolia;Mongolia;Asia;Asia;8;2","Japón;Japón;Asia;Asia;9;2","América Central;AmeCentra;América del Norte;AméricaNorte;1;3","Afganistán;Afgan;Asia;Asia;7;3","China;China;Asia;Asia;8;3","Venezuela;Venezuela;América del Sur;AméricaSur;1;4","África del Norte;ANorte;África;África;5;4","Egipto;Egipto;África;África;6;4","Oriente Medio;OMedio;Asia;Asia;7;4","India;India;Asia;Asia;8;4","Sureste Asiático;SAsiático;Asia;Asia;9;4","Perú;Perú;América del Sur;AméricaSur;1;5","Brasil;Brasil;América del Sur;AméricaSur;2;5","Congo;Congo;África;África;5;5","África Oriental;AOriental;África;África;6;5","Argentina;Argentina;América del Sur;AméricaSur;1;6","Sudáfrica;Sudáfrica;África;África;6;6","Madagascar;Madagasca;África;África;7;6","Indonesia;Indonesia;Oceanía;Oceanía;9;6","Nueva Guinea;NGuinea;Oceanía;Oceanía;10;6","Australia Occidental;AusOccid;Oceanía;Oceanía;9;7","Australia Oriental;AusOrient;Oceanía;Oceanía;10;7"};
    private static final String archivoColoresContinentes[] = {"Asia;Cyan","África;Verde","Europa;Amarillo","AméricaNorte;Violeta","AméricaSur;Rojo","Oceanía;Azul"};

    /**
     * Convierte un conjunto de líneas de un archivo a un archivo completo. Esto lo hacemos así porque el separador de línea puede variar según el sistema.
     */
    private static String getWholeFile(String[] fileLines) {
        List<String> lines = Arrays.asList(fileLines);
        return lines.stream().reduce("", (String accum, String line) -> {return(accum+line+NEW_LINE);});
    }

    /**
     * Crea el archivo de las coordenadas de los países
     */
    public static void crearArchivoPaisesCoordenadas(File archivo) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(archivo), ENCODING);
        writer.append(getWholeFile(archivoPaisesCoordenadas));
        writer.close();
    }
    
    /**
     * Crea el archivo de los colores de los continentes
     */
    public static void crearArchivoColoresContinentes(File archivo) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(archivo), ENCODING);
        writer.append(getWholeFile(archivoColoresContinentes));
        writer.close();
    }
}
