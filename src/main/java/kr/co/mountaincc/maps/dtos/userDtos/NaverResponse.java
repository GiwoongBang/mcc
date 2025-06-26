package kr.co.mountaincc.maps.dtos.userDtos;

import java.util.Map;

public class NaverResponse implements OAuth2Response {

    private final Map<String, Object> attribute;

    public NaverResponse(Map<String, Object> attribute) {
        this.attribute = (Map<String, Object>) attribute.get("response");
    }


    @Override
    public String getProvider() {

        return "naver";
    }

    @Override
    public String getProviderId() {
        Object id = attribute.get("id");

        return id != null ? id.toString() : "";
    }

    @Override
    public String getNickname() {
        Object nickname = attribute.get("nickname");

        return nickname != null ? nickname.toString() : "";
    }

    @Override
    public String getProfileImage() {
        Object profileImage = attribute.get("profile_image");

        return profileImage != null ? profileImage.toString() : "";
    }

}
