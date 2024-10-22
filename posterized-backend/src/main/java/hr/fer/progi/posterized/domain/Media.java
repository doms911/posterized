package hr.fer.progi.posterized.domain;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class Media {
    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String upload(MultipartFile slika, String naziv, String folder){
        String URL="";
        try {
            fileName = slika.getOriginalFilename();
            fileName = naziv.concat(fileName.substring(fileName.lastIndexOf(".")));

            File file = convertToFile(slika, fileName);
            URL = uploadFile(file, fileName, folder);
            file.delete();
        } catch (Exception e) {
            Assert.hasText("", "Error");
        }
        return URL;
    }
    public String uploadFile(File file, String name, String folder) throws IOException {
        String contentType;
        if (name.endsWith(".pdf")) {
            contentType = "application/pdf";
        } else if (name.endsWith(".pptx")) {
            contentType = "application/vnd.openxmlformats-officedocument.presentationml.presentation";
        } else if (name.endsWith(".ppt")) {
            contentType = "application/vnd.ms-powerpoint";
        } else {
            contentType = "media";
        }
        String fullPath = folder + "/" + name;

        BlobId blobId = BlobId.of("posterized-8e1c4.appspot.com", fullPath);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(contentType).build();
        InputStream inputStream = Media.class.getClassLoader().getResourceAsStream("posterized-8e1c4-firebase-adminsdk-irr4i-050355aaff.json");
        Credentials credentials = GoogleCredentials.fromStream(inputStream);
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.create(blobInfo, Files.readAllBytes(file.toPath()));

        String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/posterized-8e1c4.appspot.com/o/%s?alt=media";
        return String.format(DOWNLOAD_URL, URLEncoder.encode(fullPath, StandardCharsets.UTF_8));
    }

    public File convertToFile(MultipartFile multipartFile, String name) throws IOException {
        File tempFile = new File(name);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
        }
        return tempFile;
    }
    public void deleteFolder(String folder) {
        try {
            InputStream inputStream = Media.class.getClassLoader().getResourceAsStream("posterized-8e1c4-firebase-adminsdk-irr4i-050355aaff.json");
            Credentials credentials = GoogleCredentials.fromStream(inputStream);
            Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();

            Iterable<Blob> blobs = storage.list("posterized-8e1c4.appspot.com", Storage.BlobListOption.prefix(folder)).iterateAll();

            for (Blob blob : blobs) {
                blob.delete();
            }
            storage.delete("posterized-8e1c4.appspot.com", folder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteFile(String name, String folder) {
        try {
            InputStream inputStream = Media.class.getClassLoader().getResourceAsStream("posterized-8e1c4-firebase-adminsdk-irr4i-050355aaff.json");
            Credentials credentials = GoogleCredentials.fromStream(inputStream);
            Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();

            String fullPath = folder + "/" + name;
            BlobId blobId = BlobId.of("posterized-8e1c4.appspot.com", fullPath);

            Blob blob = storage.get(blobId);
            if (blob != null) {
                blob.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String changeFileNamePdf(String name, String newName, String folder){
        try {
            InputStream inputStream = Media.class.getClassLoader().getResourceAsStream("posterized-8e1c4-firebase-adminsdk-irr4i-050355aaff.json");
            Credentials credentials = GoogleCredentials.fromStream(inputStream);
            Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();

            String fullPath = folder + "/" + name + ".pdf";
            BlobId blobId = BlobId.of("posterized-8e1c4.appspot.com", fullPath);

            Blob blob = storage.get(blobId);
            String fullPath2 = folder + "/" + newName + ".pdf";
            BlobId blobId2 = BlobId.of("posterized-8e1c4.appspot.com", fullPath2);

            if (blob != null) {
                Blob newBlob = blob.toBuilder().setBlobId(blobId2).build();
                storage.create(newBlob, blob.getContent());
                blob.delete();

                String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/posterized-8e1c4.appspot.com/o/%s?alt=media";
                return String.format(DOWNLOAD_URL, URLEncoder.encode(fullPath2, StandardCharsets.UTF_8));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String changeFileNamePptx(String name, String newName, String folder){
        try {
            InputStream inputStream = Media.class.getClassLoader().getResourceAsStream("posterized-8e1c4-firebase-adminsdk-irr4i-050355aaff.json");
            Credentials credentials = GoogleCredentials.fromStream(inputStream);
            Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();

            String fullPath = folder + "/" + name + ".pptx";
            BlobId blobId = BlobId.of("posterized-8e1c4.appspot.com", fullPath);

            Blob blob = storage.get(blobId);
            if (blob != null) {
                String fullPath2 = folder + "/" + newName + ".pptx";
                BlobId blobId2 = BlobId.of("posterized-8e1c4.appspot.com", fullPath2);
                Blob newBlob = blob.toBuilder().setBlobId(blobId2).build();
                storage.create(newBlob, blob.getContent());
                blob.delete();
                String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/posterized-8e1c4.appspot.com/o/%s?alt=media";
                return String.format(DOWNLOAD_URL, URLEncoder.encode(fullPath2, StandardCharsets.UTF_8));
            } else {
                fullPath = folder + "/" + name + ".ppt";
                blobId = BlobId.of("posterized-8e1c4.appspot.com", fullPath);
                blob = storage.get(blobId);
                if (blob != null) {
                    String fullPath2 = folder + "/" + newName + ".ppt";
                    BlobId blobId2 = BlobId.of("posterized-8e1c4.appspot.com", fullPath2);
                    Blob newBlob = blob.toBuilder().setBlobId(blobId2).build();
                    storage.create(newBlob, blob.getContent());
                    blob.delete();
                    String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/posterized-8e1c4.appspot.com/o/%s?alt=media";
                    return String.format(DOWNLOAD_URL, URLEncoder.encode(fullPath2, StandardCharsets.UTF_8));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String changeFilePlace(String name, String folder, String newFolder) {
        try {
            InputStream inputStream = Media.class.getClassLoader().getResourceAsStream("posterized-8e1c4-firebase-adminsdk-irr4i-050355aaff.json");
            Credentials credentials = GoogleCredentials.fromStream(inputStream);
            Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();

            String fullPath = folder + "/" + name;
            BlobId blobId = BlobId.of("posterized-8e1c4.appspot.com", fullPath);

            Blob blob = storage.get(blobId);
            if (blob != null){
                String fullPath2 = newFolder + "/" + name;
                BlobId blobId2 = BlobId.of("posterized-8e1c4.appspot.com", fullPath2);
                Blob newBlob = blob.toBuilder().setBlobId(blobId2).build();
                storage.create(newBlob, blob.getContent());
                blob.delete();
                String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/posterized-8e1c4.appspot.com/o/%s?alt=media";
                return String.format(DOWNLOAD_URL, URLEncoder.encode(fullPath2, StandardCharsets.UTF_8));
            } else{
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
