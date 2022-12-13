package com.edutech.api.storage.repository;

import com.edutech.api.storage.model.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface NewsRepository  extends JpaRepository<News, Long>, JpaSpecificationExecutor<News> {
}
