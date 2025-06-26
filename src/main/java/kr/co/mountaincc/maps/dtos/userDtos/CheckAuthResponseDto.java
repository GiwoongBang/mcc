package kr.co.mountaincc.maps.dtos.userDtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CheckAuthResponseDto {

    private String username;

    private String nickname;

    private String role;

    private String profileImg;

}