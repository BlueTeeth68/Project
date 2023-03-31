package com.mangareader.web.rest;

import com.mangareader.domain.History;
import com.mangareader.service.IHistoryService;
import com.mangareader.service.dto.PagingReturnDTO;
import com.mangareader.service.dto.ReturnHistoryDTO;
import com.mangareader.service.mapper.HistoryMapper;
import com.mangareader.service.util.APIUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/account/history")
@RequiredArgsConstructor
@SecurityRequirement(name = "authorize")
@SuppressWarnings("unused")
public class HistoryResource {

    private final IHistoryService historyService;
    private final HistoryMapper historyMapper;
    private final HttpServletRequest request;

    @GetMapping()
    public ResponseEntity<PagingReturnDTO<ReturnHistoryDTO>> getHistoriesOfCurrentUser(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "30") int size
    ) {
        Page<History> histories = historyService.getAllHistoryOfCurrentUser(page, size);
        String serverName = APIUtil.getServerName(request);
        List<ReturnHistoryDTO> historyDTOs = historyMapper.toListReturnHistoryDTO(histories.getContent(), serverName);
        PagingReturnDTO<ReturnHistoryDTO> result = new PagingReturnDTO<>();
        result.setTotalElements(histories.getTotalElements());
        result.setTotalPages(histories.getTotalPages());
        result.setContent(historyDTOs);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
