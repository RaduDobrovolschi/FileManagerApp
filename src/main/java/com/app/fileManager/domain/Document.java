package com.app.fileManager.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "document")
public class Document implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 1, max = 150)
    @Column(length = 150, nullable = false, name = "title")
    private String title;

    @NotNull
    @Size(min = 1, max = 255)
    @Column(length = 255, nullable = false, name = "path")
    private String path;

    @NotNull
    @Column(nullable = false, name = "size")
    private Long size;

    @NotNull
    @Size(min = 1, max = 150)
    @Column(length = 150, nullable = false, name = "mime_type")
    private String mimeType;

    public Document() {
    }

    public Document(String title, String path, Long size, String mimeType) {
        this.title = title;
        this.path = path;
        this.size = size;
        this.mimeType = mimeType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Document document = (Document) o;
        return id.equals(document.id) && title.equals(document.title) && path.equals(document.path) && size.equals(document.size) && mimeType.equals(document.mimeType);
    }

    @Override
    public String toString() {
        return "Document{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", path='" + path + '\'' +
                ", size=" + size +
                ", mimeType='" + mimeType + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, path, size, mimeType);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
