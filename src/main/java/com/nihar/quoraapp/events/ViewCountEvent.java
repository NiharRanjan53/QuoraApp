package com.nihar.quoraapp.events;

import com.nihar.quoraapp.enums.TargetType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViewCountEvent {
    private String targetId;
    private TargetType targetType;
    private LocalDateTime timestamp;
}
