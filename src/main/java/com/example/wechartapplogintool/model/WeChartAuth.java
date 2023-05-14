package com.example.wechartapplogintool.model;

import lombok.Data;

@Data
public class WeChartAuth {
    private String encryptedData;
    private String iv;
    private String sessionId;
}
