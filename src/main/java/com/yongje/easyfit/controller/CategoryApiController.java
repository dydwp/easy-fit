package com.yongje.easyfit.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yongje.easyfit.entity.BodyPartCategory;
import com.yongje.easyfit.service.CategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryApiController {

	private final CategoryService categoryService;
	
	@GetMapping
	public List<BodyPartCategory> getAllCategories() {
		return categoryService.getAllCategories();
	}
}
