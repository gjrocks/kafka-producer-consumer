# Logback Configuration Documentation

This project has been configured with comprehensive Logback logging capabilities. Here's what has been added and how to use it.

## Dependencies Added

The following logback-related dependencies have been added to `pom.xml`:

```xml
<!-- Logback dependencies -->
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
</dependency>
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-core</artifactId>
</dependency>

<!-- SLF4J API and bridges -->
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-api</artifactId>
</dependency>
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>jcl-over-slf4j</artifactId>
</dependency>
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>jul-to-slf4j</artifactId>
</dependency>

<!-- Logstash encoder for structured logging -->
<dependency>
    <groupId>net.logstash.logback</groupId>
    <artifactId>logstash-logback-encoder</artifactId>
    <version>7.3</version>
</dependency>
```

## Logback Configuration Features

The `src/main/resources/logback.xml` configuration includes:

### 1. Multiple Appenders

- **CONSOLE**: Formatted console output for development
- **FILE**: Rolling file appender with time and size-based rotation
- **JSON_FILE**: Structured JSON logging for production analysis
- **NEWRELIC_ASYNC**: New Relic integration (preserved from original config)

### 2. Log File Management

- **Location**: Logs are stored in `logs/` directory
- **Rotation**: Daily rotation with 10MB size limit per file
- **Retention**: 30 days of log history, maximum 1GB total size
- **Files**:
  - `logs/kafka-app.log` - Standard formatted logs
  - `logs/kafka-app-json.log` - JSON structured logs
  - `myApp.log` - New Relic formatted logs

### 3. Logger Configuration

- **Root Level**: INFO (configurable)
- **Kafka Logs**: WARN level to reduce noise
- **Spring Boot**: INFO level
- **Application (com.gj)**: DEBUG level with all appenders

### 4. Log Pattern

```
%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n
```

## Usage Examples

### Basic Logging

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyClass {
    private static final Logger logger = LoggerFactory.getLogger(MyClass.class);
    
    public void myMethod() {
        logger.info("Processing started");
        logger.debug("Debug information");
        logger.warn("Warning message");
        logger.error("Error occurred", exception);
    }
}
```

### Structured Logging with MDC

```java
import org.slf4j.MDC;

// Add context
MDC.put("userId", "user123");
MDC.put("requestId", "req-456");
MDC.put("topic", "device-events");

logger.info("Processing user request");

// Clear context when done
MDC.clear();
```

## Log Levels

- **ERROR**: System errors, exceptions
- **WARN**: Warning conditions
- **INFO**: General information messages
- **DEBUG**: Detailed information for debugging
- **TRACE**: Most detailed level (disabled by default)

## Production Considerations

1. **JSON Logs**: Use `kafka-app-json.log` for log aggregation tools
2. **Log Rotation**: Files automatically rotate to prevent disk space issues
3. **Performance**: Async appenders reduce logging impact
4. **Monitoring**: New Relic integration maintained for APM

## Configuration Customization

To modify logging behavior, edit `src/main/resources/logback.xml`:

- Change log levels in `<logger>` elements
- Modify file paths in `<property>` elements
- Adjust retention policies in `<rollingPolicy>` sections

## Directory Structure

```
logs/
├── kafka-app.log              # Current log file
├── kafka-app-2024-12-05.0.log # Rotated files
├── kafka-app-json.log         # JSON formatted logs
└── kafka-app-json-2024-12-05.0.log
```

## Testing

See `src/main/java/com/gj/LoggingExample.java` for usage examples and testing the configuration.

## Notes

- The `logs/` directory is automatically created
- Log files are UTF-8 encoded
- Configuration supports hot-reloading (scan="true")
- All third-party libraries are bridged to use SLF4J
