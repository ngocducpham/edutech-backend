package com.edutech.api.storage.repository;

import com.edutech.api.storage.model.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ProvinceRepository extends JpaRepository<Province, Long>, JpaSpecificationExecutor<Province> {
    public Province findProvinceByProvinceName(String name);

    public Optional<Province> findByProvinceNameAndKind(String name, String kind);
}
