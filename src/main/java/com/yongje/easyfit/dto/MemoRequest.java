package com.yongje.easyfit.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemoRequest {
	private String memo;
	private List<String> bodyParts;
}
