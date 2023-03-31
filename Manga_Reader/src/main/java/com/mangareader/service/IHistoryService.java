package com.mangareader.service;

import com.mangareader.domain.History;
import com.mangareader.domain.HistoryId;
import org.springframework.data.domain.Page;

public interface IHistoryService {

    History getHistoryById(HistoryId historyId);

    void createNewHistory(Long chapterId);

    Page<History> getAllHistoryOfCurrentUser(Integer page, Integer size);

}
