package com.yongje.easyfit.service;

import java.util.Map;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.yongje.easyfit.entity.User;
import com.yongje.easyfit.repository.UserRepository;
import com.yongje.easyfit.security.PrincipalDetails;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

	private final UserRepository userRepository;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(request);

		String provider = request.getClientRegistration().getRegistrationId(); // "google" 또는 "kakao"
		Map<String, Object> attributes = oAuth2User.getAttributes();

		String providerId;
		String email;
		String nickname;
		String nameAttributeKey;

		if ("kakao".equals(provider)) {
			providerId = String.valueOf(attributes.get("id"));
			nameAttributeKey = "id";

			@SuppressWarnings("unchecked")
			Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
			@SuppressWarnings("unchecked")
			Map<String, Object> profile = kakaoAccount != null
					? (Map<String, Object>) kakaoAccount.get("profile")
					: null;

			email = kakaoAccount != null ? (String) kakaoAccount.get("email") : null;
			nickname = profile != null ? (String) profile.get("nickname") : "카카오사용자";
		} else { // google
			providerId = (String) attributes.get("sub");
			nameAttributeKey = "sub";
			email = (String) attributes.get("email");
			nickname = (String) attributes.get("name");
		}

		User user = userRepository.findByProviderAndProviderId(provider, providerId)
				.map(existing -> {
					existing.setEmail(email);
					existing.setNickname(nickname);
					return userRepository.save(existing);
				})
				.orElseGet(() -> userRepository.save(
						User.builder()
								.provider(provider)
								.providerId(providerId)
								.email(email)
								.nickname(nickname)
								.build()));

		return new PrincipalDetails(user, attributes, nameAttributeKey);
	}
}