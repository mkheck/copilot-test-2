package com.thehecklers.copilottest2;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public record Aircraft(@Id String adshex, String reg, String type) {}
