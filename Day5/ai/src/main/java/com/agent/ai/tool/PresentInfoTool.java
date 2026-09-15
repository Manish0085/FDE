package com.agent.ai.tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class PresentInfoTool {

    private final ZoneId zoneId = ZoneId.of("Asia/Kolkata");

    @Tool(description = """
            Get the current date and time.
            Returns the current day of week, date, time, month, year,
            timezone, UTC offset, and formatted date-time.
            Use this tool whenever the user asks for the current time,
            today's date, current day, current month, current year,
            or any other present date/time information.
            """)
    public String getCurrentDateTime() {

        System.out.println("Current data tool");
        ZonedDateTime now = ZonedDateTime.now(zoneId);

        DateTimeFormatter dateFormatter =
                DateTimeFormatter.ofPattern("dd MMMM yyyy");

        DateTimeFormatter timeFormatter =
                DateTimeFormatter.ofPattern("hh:mm:ss a");

        DateTimeFormatter dateTimeFormatter =
                DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy, hh:mm:ss a");

        return """
                Current Date & Time Information:
                
                Day: %s
                Date: %s
                Time: %s
                Month: %s
                Year: %d
                Timezone: %s
                UTC Offset: %s
                Week of Year: %d
                Full Date & Time: %s
                """.formatted(
                now.getDayOfWeek(),
                now.format(dateFormatter),
                now.format(timeFormatter),
                now.getMonth(),
                now.getYear(),
                now.getZone(),
                now.getOffset(),
                now.get(java.time.temporal.WeekFields.ISO.weekOfWeekBasedYear()),
                now.format(dateTimeFormatter)
        );
    }
}