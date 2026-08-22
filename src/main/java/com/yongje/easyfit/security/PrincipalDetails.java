package com.yongje.easyfit.security;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.yongje.easyfit.entity.User;

import lombok.Getter;

/**
 * 스프링 시큐리티가 세션에 들고 있는 로그인 주체.
 * 우리 DB의 User 엔티티를 같이 들고 있어서, 컨트롤러에서
 * @AuthenticationPrincipal PrincipalDetails principal 로 바로 꺼내 쓴다.
 */
@Getter
public class PrincipalDetails implements OAuth2User {

	private final User user;
	private final Map<String, Object> attributes;
	private final String nameAttributeKey;

	public PrincipalDetails(User user, Map<String, Object> attributes, String nameAttributeKey) {
		this.user = user;
		this.attributes = attributes;
		this.nameAttributeKey = nameAttributeKey;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return java.util.List.of(() -> "ROLE_USER");
	}

	@Override
	public String getName() {
		return String.valueOf(attributes.get(nameAttributeKey));
	}
}