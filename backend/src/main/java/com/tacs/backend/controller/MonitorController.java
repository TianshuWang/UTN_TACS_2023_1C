package com.tacs.backend.controller;

import com.tacs.backend.dto.EventOptionReportDto;
import com.tacs.backend.dto.MarketingReportDto;
import com.tacs.backend.dto.ExceptionResponse;
import com.tacs.backend.service.MonitorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/monitor")
public class MonitorController {
    private final MonitorService monitorService;

    @GetMapping("/ratios")
    @Operation(summary = "Get events report", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Report ready"),
            @ApiResponse(responseCode = "400", description = "Report querying failed", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "504", description = "Timeout", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
    })
    public ResponseEntity<MarketingReportDto> getCounterReport() {
        return ResponseEntity.ok(this.monitorService.getMarketingReport());
    }

    @GetMapping("/options")
    @Operation(summary = "Get events options report", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Report ready"),
            @ApiResponse(responseCode = "400", description = "Report querying failed", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "504", description = "Timeout", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
    })
    public ResponseEntity<Map<String, List<EventOptionReportDto>>> getOptionsReport() {
        Map<String, List<EventOptionReportDto>> response = new HashMap<>(1);
        response.put("options_report", this.monitorService.getLastVotedEventOptions());
        return ResponseEntity.ok(response);
    }

}
