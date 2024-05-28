package com.bookstore.service;
import org.springframework.web.multipart.MultipartFile;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.stereotype.Service;

/**
 * Service phụ trách việc lưu file ảnh vào server
 */
@Service
public class FileStorageService {
    /**
     * Thư mục ảnh mặc định
     */
    static final String directory = "src/main/resources/static/images"; 
    /**
     * Lưu file vào server
     * @param file file cần lưu
     * @throws IOException
     */
    public void saveFile(MultipartFile file) throws IOException{
        String filename = file.getOriginalFilename();
        String filepath = Paths.get(directory, filename).toString();
        BufferedOutputStream stream = 
            new BufferedOutputStream(new FileOutputStream(new File(filepath)));
        stream.write(file.getBytes());
        stream.close();
    }
    /**
     * Xóa file khỏi server
     * @param filename tên file cần xóa
     * @throws IOException
     */
    public void deleteFile(String filename) throws IOException{
        Path filepath = Paths.get(directory, filename);
        Files.delete(filepath);;
    }
}
