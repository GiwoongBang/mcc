package kr.co.mountaincc.maps.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum DetailCategoryType {

    PARKING_LOT("주차장"),
    SHELTER("대피소"),
    CROSSROAD("갈림길"),
    TEMPLE("절"),
    PEAK("봉우리"),
    HELIPAD("헬기장"),
    START_POINT("시작점"),
    TRAILHEAD("들머리"),
    LANDMARK("명소"),
    SUMMIT("정상"),
    RESTROOM("화장실")
    ;

    private final String type;

    DetailCategoryType(String type) {
        this.type = type;
    }

    public static DetailCategoryType fromType(String type) {
        return Arrays.stream(values())
                .filter(detailCategoryType -> detailCategoryType.type.equals(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid type: " + type));
    }

}