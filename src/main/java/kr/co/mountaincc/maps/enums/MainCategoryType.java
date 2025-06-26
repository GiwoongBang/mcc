package kr.co.mountaincc.maps.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum MainCategoryType {

    MOUNTAIN("산"),
    BACKPACKING("백패킹"),
    VALLEY("계곡");

    private final String type;

    MainCategoryType(String type) {
        this.type = type;
    }

    public static MainCategoryType fromType(String type) {
        return Arrays.stream(values())
                .filter(mainCategoryType -> mainCategoryType.type.equals(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid type: " + type));
    }

}