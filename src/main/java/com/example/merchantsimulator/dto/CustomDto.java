package com.example.merchantsimulator.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomDto {
    public static CustomDto from(String imageName, String title, String description){
        return CustomDto.builder()
                .imageName(imageName)
                .title(title)
                .description(description)
                .build();
    }

    private String imageName;
    private String title;
    private String description;
}
