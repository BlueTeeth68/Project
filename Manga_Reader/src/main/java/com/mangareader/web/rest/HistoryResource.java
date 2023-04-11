package com.mangareader.web.rest;

import com.mangareader.domain.History;
import com.mangareader.service.IHistoryService;
import com.mangareader.service.dto.PagingReturnDTO;
import com.mangareader.service.dto.ReturnHistoryDTO;
import com.mangareader.service.mapper.HistoryMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/account/history")
@RequiredArgsConstructor
@SuppressWarnings("unused")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Query successfully"),
        @ApiResponse(responseCode = "201", description = "Created successfully"),
        @ApiResponse(responseCode = "400", description = "Bad request for input parameters", content = @Content),
        @ApiResponse(responseCode = "401", description = "Unauthorized, missing or invalid JWT", content = @Content),
        @ApiResponse(responseCode = "403", description = "Access denied, do not have permission to access this resource", content = @Content),
})
@Tag(name = "11. History")
public class HistoryResource {

    private final IHistoryService historyService;
    private final HistoryMapper historyMapper;
    private final HttpServletRequest request;

    @Operation(
            summary = "View reading history",
            description = "Logged in user can view their reading history.",
            security = @SecurityRequirement(name = "authorize")
    )
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagingReturnDTO<ReturnHistoryDTO>> getHistoriesOfCurrentUser(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        Page<History> histories = historyService.getAllHistoryOfCurrentUser(page, size);
        List<ReturnHistoryDTO> historyDTOs = historyMapper.toListReturnHistoryDTO(histories.getContent());
        PagingReturnDTO<ReturnHistoryDTO> result = new PagingReturnDTO<>();
        result.setTotalElements(histories.getTotalElements());
        result.setTotalPages(histories.getTotalPages());
        result.setContent(historyDTOs);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
