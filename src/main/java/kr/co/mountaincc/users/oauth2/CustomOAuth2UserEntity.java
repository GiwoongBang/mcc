package kr.co.mountaincc.users.oauth2;

import kr.co.mountaincc.maps.dtos.userDtos.UserDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CustomOAuth2UserEntity implements OAuth2User {

    private final UserDto userDto;

    public CustomOAuth2UserEntity(UserDto userDto) {
        this.userDto = userDto;
    }

    @Override
    public Map<String, Object> getAttributes() {

        Map<String, Object> attributes = new HashMap<>();

        attributes.put("username", userDto.getUsername());
        attributes.put("nickname", userDto.getNickname());
        attributes.put("profileImg", userDto.getProfileImg());
        attributes.put("role", userDto.getRole());

        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                String role = userDto.getRole();

                return role;
            }
        });

        return collection;
    }

    @Override
    public String getName() {
        String nickname = userDto.getNickname();

        return nickname != null ? nickname : "";
    }

    public String getUsername() {
        String username = userDto.getUsername();

        return username != null ? username : "";
    }


    public String getProfileImg() {
        String profileImg = userDto.getProfileImg();

        return profileImg != null ? profileImg : "";
    }

}
