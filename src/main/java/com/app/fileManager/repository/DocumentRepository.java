package com.app.fileManager.repository;

import com.app.fileManager.domain.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    @Modifying
    @Query("delete from Document d where d.path like concat( ?1,'/%')")
    public void deleteAllUserLogin(String bucketName);
}
