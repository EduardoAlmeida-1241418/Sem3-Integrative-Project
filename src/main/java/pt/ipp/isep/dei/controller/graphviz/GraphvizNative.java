package pt.ipp.isep.dei.controller.graphviz;

import java.io.File;
import java.io.IOException;

/**
 * Classe responsável pela geração de ficheiros SVG a partir
 * de ficheiros DOT utilizando o Graphviz (neato).
 *
 * Executa o processo nativo de conversão e gere a limpeza
 * de ficheiros antigos no diretório de saída.
 */
public class GraphvizNative {

    /**
     * Gera um ficheiro SVG a partir de um ficheiro DOT
     * utilizando a configuração original do grafo.
     *
     * @param dotPath caminho para o ficheiro DOT
     * @return caminho absoluto do ficheiro SVG gerado
     * @throws IOException caso ocorra um erro de execução ou I/O
     */
    public static String generateSvgOriginal(String dotPath) throws IOException {
        File neato = new File("src/main/resources/graphviz/bin/neato.exe");

        if (!neato.exists()) {
            throw new RuntimeException("neato.exe not found at: " + neato.getAbsolutePath());
        }

        File dotFile = new File(dotPath);
        if (!dotFile.exists()) {
            throw new RuntimeException("dot file not found at: " + dotFile.getAbsolutePath());
        }

        String filePath = dotPath.replace("dot","svg");

        File svgFile = new File(filePath);

        ProcessBuilder pb = new ProcessBuilder(
                neato.getAbsolutePath(),
                "-n2",
                "-Tsvg",
                dotFile.getAbsolutePath(),
                "-o",
                svgFile.getAbsolutePath()
        );

        pb.redirectErrorStream(true);
        Process p = pb.start();

        try {
            p.waitFor();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        deleteOldFiles(filePath);

        return svgFile.getAbsolutePath();
    }

    /**
     * Gera um ficheiro SVG a partir de um ficheiro DOT
     * utilizando a configuração adaptada do grafo.
     *
     * @param dotPath caminho para o ficheiro DOT
     * @return caminho absoluto do ficheiro SVG gerado
     * @throws IOException caso ocorra um erro de execução ou I/O
     */
    public static String generateSvgAdapted(String dotPath) throws IOException {
        File neato = new File("src/main/resources/graphviz/bin/neato.exe");

        if (!neato.exists()) {
            throw new RuntimeException("neato.exe not found at: " + neato.getAbsolutePath());
        }

        File dotFile = new File(dotPath);
        if (!dotFile.exists()) {
            throw new RuntimeException("dot file not found at: " + dotFile.getAbsolutePath());
        }

        String filePath = dotPath.replace(".dot", ".svg");
        File svgFile = new File(filePath);

        ProcessBuilder pb = new ProcessBuilder(
                neato.getAbsolutePath(),
                "-Tsvg",
                dotFile.getAbsolutePath(),
                "-o",
                svgFile.getAbsolutePath()
        );

        pb.redirectErrorStream(true);
        Process p = pb.start();

        try {
            p.waitFor();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        deleteOldFiles(filePath);

        return svgFile.getAbsolutePath();
    }


    /**
     * Remove ficheiros antigos do diretório, mantendo apenas
     * o ficheiro SVG atualmente gerado e os seus equivalentes.
     *
     * @param currentPath caminho do ficheiro SVG atual
     */
    private static void deleteOldFiles(String currentPath) {
        String dirPath = currentPath.substring(0, currentPath.lastIndexOf("\\") + 1);
        File dir = new File(dirPath);
        File currentFile = new File(currentPath);
        String currentName = currentFile.getName();
        currentName = currentName.replace(".svg","");

        File[] files = dir.listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            if (!file.isFile()) {
                continue;
            }

            String name = file.getName();
            name = name.replace(".svg","");
            name = name.replace(".dot","");

            if (!currentName.equals(name) && (currentName.contains("_") && currentName.replace("_","").equals(name) || !currentName.contains("_") && name.replace("_","").equals(currentName))) {
                file.delete();
            }
        }
    }

}
