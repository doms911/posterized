package hr.fer.progi.posterized.service.impl;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import hr.fer.progi.posterized.dao.PokroviteljRepository;
import hr.fer.progi.posterized.domain.Pokrovitelj;
import hr.fer.progi.posterized.service.PokroviteljService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Service
public class PokroviteljServiceJPA implements PokroviteljService {
    @Autowired
    private PokroviteljRepository pokroviteljRepo;

    @Override
    public Pokrovitelj createPokrovitelj(Pokrovitelj pokrovitelj, MultipartFile slika) throws IOException {
        Assert.notNull(pokrovitelj, "Pokrovitelj object must be given");
        String naziv = pokrovitelj.getNaziv();
        Assert.hasText(naziv, "Naziv must be given");
        String url = pokrovitelj.getUrl();
        Assert.hasText(url, "Url must be given");
        if(slika.isEmpty()) Assert.hasText("", "Slika must be given");
        if (pokroviteljRepo.countByNaziv(pokrovitelj.getNaziv()) > 0) {
            Assert.hasText("", "Pokrovitelj with naziv " + pokrovitelj.getNaziv() + " already exists");
        }

        try {
            String fileName = slika.getOriginalFilename();
            fileName = naziv.concat(fileName.substring(fileName.lastIndexOf(".")));

            File file = convertToFile(slika, fileName);
            String URL = uploadFile(file, fileName);
            pokrovitelj.setUrlSlike(URL);
            file.delete();
        } catch (Exception e) {
            Assert.hasText("", "Error");
        }
        return pokroviteljRepo.save(pokrovitelj);
    }

    private File convertToFile(MultipartFile multipartFile, String fileName) throws IOException {
        File tempFile = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
        }
        return tempFile;
    }

    private String uploadFile(File file, String fileName) throws IOException {
        BlobId blobId = BlobId.of("posterized-8e1c4.appspot.com", fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
        InputStream inputStream = PokroviteljServiceJPA.class.getClassLoader().getResourceAsStream("posterized-8e1c4-firebase-adminsdk-irr4i-050355aaff.json");
        Credentials credentials = GoogleCredentials.fromStream(inputStream);
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.create(blobInfo, Files.readAllBytes(file.toPath()));

        String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/posterized-8e1c4.appspot.com/o/%s?alt=media";
        return String.format(DOWNLOAD_URL, URLEncoder.encode(fileName, StandardCharsets.UTF_8));
    }
}
