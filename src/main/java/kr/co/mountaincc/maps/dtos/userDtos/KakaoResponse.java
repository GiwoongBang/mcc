package kr.co.mountaincc.maps.dtos.userDtos;

import java.util.Map;

public class KakaoResponse implements OAuth2Response {

    private final Map<String, Object> attributes;
    private final Map<String, Object> kakaoAccount;
    private final Map<String, Object> profile;

    public KakaoResponse(Map<String, Object> attributes) {
        this.attributes = attributes;
        this.kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        this.profile = kakaoAccount != null ? (Map<String, Object>) kakaoAccount.get("profile") : null;
    }

    @Override
    public String getProvider() {

        return "kakao";
    }

    @Override
    public String getProviderId() {
        Object id = attributes.get("id");

        return id != null ? id.toString() : "";
    }

    @Override
    public String getNickname() {
        if (profile != null) {
            Object nickname = profile.get("nickname");

            return nickname != null ? nickname.toString() : "";
        }
        return "";
    }

    @Override
    public String getProfileImage() {
        if (profile != null) {
            Object profileImage = profile.get("profile_image_url");

            return profileImage != null ? profileImage.toString() : "";
        }
        return "";
    }
}