package com.paulovfe.agendavoting.dto;

import com.paulovfe.agendavoting.entity.Option;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class ResultDTO implements Serializable {
    private static final long serialVersionUID = -1349016389156491635L;
    private final Map<Option, Long> result = new HashMap<>();
}
