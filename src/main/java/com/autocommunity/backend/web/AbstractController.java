package com.autocommunity.backend.web;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class AbstractController {


    @Getter(onMethod_ = @JsonProperty)
    @AllArgsConstructor
    public static class ReplyBase {
        private static final String STATUS_SUCCESS = "SUCCESS";
        private static final String STATUS_FAILURE = "FAILURE";

        private String message;

        private String status;

        public static ReplyBase success(String message) {
            return new ReplyBase(message, STATUS_SUCCESS);
        }
    }
}
