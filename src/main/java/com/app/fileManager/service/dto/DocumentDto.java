package com.app.fileManager.service.dto;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Size;
import java.security.PrivateKey;
import java.util.PrimitiveIterator;

public class DocumentDto {
    @Size(min = 1, max = 150)
    private String title;
    @Size(min = 1, max = 255)
    private String path;
    private Long size;
    @Size(min = 1, max = 150)
    private String mimeType;

    private MultipartFile file;

    public DocumentDto() {
    }

    public DocumentDto(String title, String path, Long size, String mimeType, MultipartFile file) {
        this.title = title;
        this.path = path;
        this.size = size;
        this.mimeType = mimeType;
        this.file = file;
    }

    public DocumentDto(String title, String path, Long size, String mimeType){
        this.title = title;
        this.path = path;
        this.size = size;
        this.mimeType = mimeType;
    }

    @Override
    public String toString() {
        return "DocumentDto{" +
                "title='" + title + '\'' +
                ", path='" + path + '\'' +
                ", size=" + size +
                ", mimeType='" + mimeType + '\'' +
                ", file=" + file +
                '}';
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
}
