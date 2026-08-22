package com.yongje.easyfit.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.yongje.easyfit.entity.BodyPartCategory;
import com.yongje.easyfit.repository.BodyPartCategoryRepository;
import com.yongje.easyfit.service.CategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
	
	private final BodyPartCategoryRepository bodyPartCategoryRepository;
	
	@Override
	public List<BodyPartCategory> getAllCategories() {
		return bodyPartCategoryRepository.findAllByOrderBySortOrderAsc();
	}
}
