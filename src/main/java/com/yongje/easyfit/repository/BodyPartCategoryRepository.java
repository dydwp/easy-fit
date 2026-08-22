package com.yongje.easyfit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yongje.easyfit.entity.BodyPartCategory;

public interface BodyPartCategoryRepository extends JpaRepository<BodyPartCategory, Long> {

    List<BodyPartCategory> findAllByOrderBySortOrderAsc();

}