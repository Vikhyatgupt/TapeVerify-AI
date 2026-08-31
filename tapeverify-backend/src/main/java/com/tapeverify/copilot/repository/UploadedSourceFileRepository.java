package com.tapeverify.copilot.repository;

import com.tapeverify.copilot.entity.UploadedSourceFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadedSourceFileRepository extends JpaRepository<UploadedSourceFile, String> {}
