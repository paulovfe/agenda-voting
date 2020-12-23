package com.paulovfe.agendavoting.api.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class OpenSessionRequest {
    private Integer minutesToExpiration;
}
