package uz.project.runner;

import uz.project.event.Event;
import uz.project.event.EventParser;
import uz.project.filter.EventFilter;
import uz.project.filter.FilterParser;
import uz.project.input.InputHandler;
import uz.project.operators.LogicalOperator;
import uz.project.parser.UniversalParser;

import java.util.List;

// Runner class to instantiate all members
public class HeadRunner {

    // Method that runs the program
    public void run(){
        //* 1) Parse yaml file
        UniversalParser parser = new UniversalParser("settings.yaml");
        String logFilePath = parser.getInputFilePath();
        String filterRequest = parser.getFilterRequest();

        //* 2) Save log lines
        InputHandler inputHandler = new InputHandler();
        List<String> logLines = inputHandler.saveToList(logFilePath);

        //* 3) Tokenize log lines to event class
        EventParser eventHandler = new EventParser();
        List<Event> events = eventHandler.handleEvents(logLines);

        //* 4) Parse filter request
        FilterParser filterParser = new FilterParser();
        List<LogicalOperator> filterConditions = filterParser.parseFilterRequest(filterRequest);

        //* 5) Apply filter request logic
        EventFilter eventFilter = new EventFilter();
        //result list
        List<Event> filteredEvents = eventFilter.applyFilters(events, filterConditions);

        //* 6) Display results
        int c = eventFilter.countLogs(filteredEvents);
        filteredEvents.forEach(System.out::println);
        System.out.println("Total matching logs: " + c);
    }
}
