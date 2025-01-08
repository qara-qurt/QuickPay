package kz.iitu.quick_pay.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    private LocalDateTime timestamp; // Время возникновения ошибки
    private int status;              // HTTP статус ошибки
    private String error;            // Название ошибки
    private String message;          // Сообщение об ошибке
    private String path;             // Путь, где произошла ошибка
}
