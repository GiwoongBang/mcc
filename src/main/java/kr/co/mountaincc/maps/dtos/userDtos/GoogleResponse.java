package kr.co.mountaincc.maps.dtos.userDtos;

import java.util.Map;

public class GoogleResponse implements OAuth2Response {

    private final Map<String, Object> attribute;

    public GoogleResponse(Map<String, Object> attribute) {
        this.attribute = attribute;
    }

    @Override
    public String getProvider() {

        return "google";
    }

    @Override
    public String getProviderId() {
        Object sub = attribute.get("sub");

        return sub != null ? sub.toString() : "";
    }

    @Override
    public String getNickname() {
        Object name = attribute.get("name");

        return name != null ? name.toString() : "";
    }

    @Override
    public String getProfileImage() {
        Object picture = attribute.get("picture");

        return picture != null ? picture.toString() : "";
    }

}
