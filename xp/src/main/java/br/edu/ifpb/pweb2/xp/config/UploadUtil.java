package br.edu.ifpb.pweb2.xp.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public class UploadUtil {

    // Define onde as imagens vão ficar salvas de forma centralizada no computador
    private static final String DIRETORIO_UPLOADS = System.getProperty("user.home") + "/xp_uploads";

    public static String salvarImagem(MultipartFile arquivo) {
        // Se o utilizador não enviou nenhum ficheiro, retorna nulo para não quebrar o fluxo
        if (arquivo == null || arquivo.isEmpty()) {
            return null;
        }

        try {
            // 1. Cria a pasta física no computador caso ela ainda não exista
            Path pastaDefinitiva = Paths.get(DIRETORIO_UPLOADS);
            if (!Files.exists(pastaDefinitiva)) {
                Files.createDirectories(pastaDefinitiva);
            }

            // 2. Gera um nome Universalmente Único (UUID) para evitar que ficheiros com o mesmo nome se apaguem
            String nomeOriginal = arquivo.getOriginalFilename();
            String extensao = nomeOriginal != null && nomeOriginal.contains(".") 
                    ? nomeOriginal.substring(nomeOriginal.lastIndexOf(".")) 
                    : ".jpg";
            
            String nomeUnicoFicheiro = UUID.randomUUID().toString() + extensao;

            // 3. Guarda os bytes do ficheiro físico na pasta de destino
            Path caminhoCompletoFicheiro = pastaDefinitiva.resolve(nomeUnicoFicheiro);
            Files.write(caminhoCompletoFicheiro, arquivo.getBytes());

            // Retorna o nome aleatório gerado para podermos salvá-lo no banco de dados
            return nomeUnicoFicheiro;

        } catch (IOException e) {
            throw new RuntimeException("Erro ao processar e salvar o upload do ficheiro físico.", e);
        }
    }

    public static String getDiretorioUploads() {
        return DIRETORIO_UPLOADS;
    }
}
