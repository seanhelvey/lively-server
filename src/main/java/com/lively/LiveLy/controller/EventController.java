package com.lively.LiveLy.controller;

import com.lively.LiveLy.model.DeleteEventResponse;
import com.lively.LiveLy.model.Event;
import com.lively.LiveLy.repo.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

@CrossOrigin
@RestController
public class EventController {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Autowired
    EventRepository eventRepository;

    @GetMapping("/events")
    public Iterable<Event> getEvents() {
        return eventRepository.findByFinishAfter(LocalDateTime.now().minusHours(6));
    }

    @PostMapping("/events")
    public Event addEvent(
            @RequestBody Map<String, String> details,
            @RequestParam("startYear") int startYear,
            @RequestParam("startMonth") int startMonth,
            @RequestParam("startDate") int startDate,
            @RequestParam("startHour") int startHour,
            @RequestParam("startMinute") int startMinute,
            @RequestParam("finishYear") int finishYear,
            @RequestParam("finishMonth") int finishMonth,
            @RequestParam("finishDate") int finishDate,
            @RequestParam("finishHour") int finishHour,
            @RequestParam("finishMinute") int finishMinute,
            HttpServletResponse response) {
        LocalDateTime start = LocalDateTime.of(startYear, startMonth, startDate, startHour, startMinute);
        LocalDateTime finish = LocalDateTime.of(finishYear, finishMonth, finishDate, finishHour, finishMinute);

        Event event = new Event(start, finish, details.get("name"), details.get("location"), details.get("description"));
        eventRepository.save(event);
        response.setStatus(201);
        return event;
    }

    @DeleteMapping("/events/{id}")
    public DeleteEventResponse deleteEvent(@PathVariable("id") long id) {
        eventRepository.deleteById(id);
        return new DeleteEventResponse(id, 200, "OK");
    }

    @DeleteMapping("/events/all")
    public String deleteAllEvents() {
        eventRepository.deleteAll();
        return "Successfully deleted all events!";
    }

}
