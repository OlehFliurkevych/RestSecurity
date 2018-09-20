package com.security.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RestMessageDTO<T> {
    private T message;
    private boolean status;

    public static RestMessageDTO createFailureMessage(String message){
        return new RestMessageDTO(message, false);
    }

    public static RestMessageDTO createCorrectMessage(String message){
        return new RestMessageDTO(message,true);
    }
}
