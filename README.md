# Log4Life

## Overview

Log4Life is a Java-based project designed to read, parse, and filter log files based on complex filter conditions defined by users. The project supports a descriptive language for filtering logs, including logical operators (AND, OR), sequence filtering with then, and special syntax like prev.FIELD to reference previous events’ fields. Log files should have strict structure.

> Structure of logs: **TIMESTAMP [THREAD_NAME] [LOG_LEVEL] CLASS_NAME - PAYLOAD**

> Example log line: **2024-24-24 12:00:00,000 [thread-3] [INFO ] DemoClass - Demo logging started**

## Project Structure

The project consists of the following main classes and files:

### 1. Main Class (Main.java)

• The entry point of the application.

•	Simply calls the run method of the Test class to execute the log filtering logic.

### 2. Runner Class (HeadRunner.java)

•	Contains the run method which encapsulates the core logic of the application.

•	The run method reads the configuration, processes the log file, applies the filters, and outputs the results.

### 3. UniversalParser (UniversalParser.java)

•	Responsible for reading the configuration file (settings.yaml).

•	Extracts the log file path and the filter request.

### 4. InputHandler (InputHandler.java)

•	Reads the log file and stores each line as a string in a list.

•	Prepares the log data for further processing.

### 5. Event Class (Event.java)

•	Represents a structured log event.

•	Contains fields like timestamp, threadName, logLevel, className, and payload.

•	Provides methods to access these fields.

### 6. EventHandler (EventHandler.java)

•	Converts raw log lines into Event objects.

•	Parses the log lines based on the expected log format and extracts relevant fields.

### 7. FilterParser (FilterParser.java)

•	Parses the filter request string into an abstract syntax tree (AST).

•	Handles logical operators (AND, OR), sequences (then), and special conditions like prev.FIELD.

•	Returns a list of LogicalOperator objects representing the filter logic.

### 8. LogicalOperator (Abstract Class)

•	Base class for all logical operators used in filtering.

•	Defines the structure for evaluating conditions.

### 9. AndOperator (AndOperator.java)

•	Represents a logical AND operation.

•	Evaluates to true if both left and right conditions are true.

### 10. OrOperator (OrOperator.java)

•	Represents a logical OR operation.

•	Evaluates to true if at least one of the left or right conditions is true.

### 11. ConditionNode (ConditionNode.java)

•	Represents a leaf node in the AST.

•	Evaluates a specific condition against an Event object.

•	Handles prev.FIELD by comparing the current event’s field with the previous matching event’s field.

### 12. EventFilter (EventFilter.java)

•	Applies the parsed filter conditions to the list of Event objects.

•	Handles complex filtering logic, including sequences with then and special conditions like prev.FIELD.

•	Returns a list of events that match the filter conditions.

### 13. LogLevel (LogLevel.java)

•	Enum representing different log levels (e.g., INFO, DEBUG, ERROR).

•	Used in the Event class to categorize log messages.

# Configuration File (settings.yaml)

The settings.yaml file contains the configuration for the Log4Life processor. It includes:

	•	input_file: Path to the log file.
	•	filter_request: The filter request string defining the conditions which should be in brackets to filter the logs.

Example settings.yaml:

```bash

input_file: "logs/application.log"
filter_request: "sequence as
                 (PAYLOAD ~ 'RestoreRequest') then (LOG_LEVEL == prev.LOG_LEVEL)"

```

# Usage

	1. Setup:
        •   Having Java 17 SDK.
        •	Ensure that the settings.yaml file is configured with the correct log file path and filter request.
        •	Compile the project and make sure all dependencies are properly included.
	2. Running the Application:
	    •	Execute the Main class. This will:
	    • Instantiate the Test class.
	    • Invoke the run method of the HeadRunner class.
	    • The HeadRunner class will handle reading the log file, parsing the log lines, applying the filter request, and outputting the filtered log events and their count.
	3. Filter Request Language:
        •	The filter request language allows you to define complex conditions to filter logs.
        •	Logical Operators: Use 'and' and 'or' keywords to combine conditions.
        •	Sequences: Use 'then' keyword to define sequences of conditions that must be matched in order.
        •	Special Conditions: Use prev.FIELD keyword to reference the value of a field from a previous event.