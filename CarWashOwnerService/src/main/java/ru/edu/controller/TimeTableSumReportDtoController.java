package ru.edu.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.edu.entity.*;
import ru.edu.entity.dto.CarWashForOwnerDto;
import ru.edu.entity.enums.StatusFree;
import ru.edu.entity.enums.StatusShedule;
import ru.edu.entity.enums.StatusWork;
import ru.edu.entity.mapper.CarWashForOwnerMapperManual;
import ru.edu.entity.mapper.CarWashListMapper;
import ru.edu.service.*;
import ru.edu.util.DateConverter;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/CarWash-service/timeTableSumReport", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class TimeTableSumReportDtoController {

    private final TimeTableSumReportDtoService service;
    private final TimeTableService timeTableService;

    @Operation(summary = "Получить суммарный отчет по услугам моек владельца за период")
    @GetMapping("/sum-report")
    public ResponseEntity<List<TimeTableSumReportDto>> getSumReportFromAllTimeTableByOwner(@RequestParam("idUser") Long idUser, @RequestParam("dateBegin") String dateBegin, @RequestParam("dateEnd") String dateEnd) {
        try {

            try {
                // проставляем status_work=COMPLETED в заказах > now
                timeTableService.setStatuWorkCompletedForDateLowNow();
            } catch (Exception ex) {
                log.info("Exception={}",ex);
            }

            Instant iBegin = DateConverter.StringToInstant(dateBegin);
            Instant iEnd = DateConverter.StringToInstant(dateEnd);

            List<TimeTableSumReportDto> timeTableSumReportDtos = service.getSumReportFromAllTimeTableByOwner(idUser, iBegin, iEnd);
            log.info("getting timeTableSumReportDtos: {}", timeTableSumReportDtos);

            return new ResponseEntity<>(timeTableSumReportDtos, HttpStatus.OK);

        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Operation(summary = "Создать информацию об отчете")
    @PostMapping
    public ResponseEntity<TimeTableSumReportDto> save(@RequestBody TimeTableSumReportDto timeTableSumReportDto) {
        service.update(timeTableSumReportDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Обновить информацию об отчете")
    @PutMapping
    public ResponseEntity<TimeTableSumReportDto> updateCarWash(@RequestBody TimeTableSumReportDto timeTableSumReportDto) {
        service.update(timeTableSumReportDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Удалить отчет по Id")
    @DeleteMapping("/{carWashId}")
    public ResponseEntity<TimeTableSumReportDto> deleteCarWashById(@PathVariable long carWashId) {
        service.deleteById(carWashId);
        return ResponseEntity.ok().build();
    }

}
