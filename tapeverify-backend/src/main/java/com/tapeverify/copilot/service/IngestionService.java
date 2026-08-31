package com.tapeverify.copilot.service;
import com.tapeverify.copilot.dto.BatchSummaryDto; import org.springframework.web.multipart.MultipartFile;
public interface IngestionService { BatchSummaryDto ingest(MultipartFile file); BatchSummaryDto ingest(MultipartFile file,String sourceType); }
