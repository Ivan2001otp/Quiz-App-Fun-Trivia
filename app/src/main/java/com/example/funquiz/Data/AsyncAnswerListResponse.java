package com.example.funquiz.Data;

import com.example.funquiz.Model.Questions;

import java.util.List;

public interface AsyncAnswerListResponse {
    void processFinished(List<Questions>questionListResponse);
}
