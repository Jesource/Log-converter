import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static int totalMinutes = 0;
    private static String yourName = "Andrej Jegorov";
//    private static LocalDate firstDayOfTheProject = LocalDate.of(2022, 2, 17);    //To be done

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String userInput;
        ArrayList<String> convertedStrings = new ArrayList<String>();

        while (!(userInput = in.nextLine()).isEmpty()) {    //# This takes
            convertedStrings.add(callFormatter(userInput)); //# all the entered data
        }                                                   //# and converts it to MD table lines

        printFullMdFile(convertedStrings);
    }

    private static void printFullMdFile(ArrayList<String> logRecord) {

        System.out.println("# Personal activity:\n" +"> " + yourName + "\n");

        System.out.println("## " + formTimelogName(logRecord.get(0)) + "\n");

        System.out.println("| **Date**  | **Time**      | **Duration**  | **Activity** |");
        System.out.println("| --------  | ------------- | ------------  | ------------ |");

        for (String record : logRecord) {   //# It prints
            System.out.println(record);     //# the body of
        }                                   //# MD table

        calculateWorkedMinutes(logRecord);

        int hoursOfWork = totalMinutes / 60;
        int minutesOfWork = totalMinutes % 60;
        System.out.println("|  | **Total:** | **" + hoursOfWork + "h " + minutesOfWork + "min** | |");
    }

    private static void calculateWorkedMinutes(ArrayList<String> logRecord) {
        for (String record : logRecord) {
            var splitData = record.replace(" ", "").replace("min", "").split("\\|");
            totalMinutes += Integer.valueOf(splitData[3]);
        }
    }

    private static String callFormatter(String logRecord) {
//        var logRecord = "Andrej Jegorov,andrej.jegorov24@gmail.com,,PBL'as,,Thinking off presentation contents,No,2022-01-19,18:05:43,2022-01-19,18:33:28,00:27:45,,";

        logRecord = removeNameAndEmail(logRecord);

        var timeAndDates = getTimeAndDates(logRecord);
        var activityName = logRecord.replace(timeAndDates, "")
                                            .replace(isBillableAsSplitter(logRecord), "");

        var timeAndDatesAsStrings = timeAndDates.split(",");
//        System.out.println("Start date: " + timeAndDatesAsStrings[0]);
//        System.out.println("Start time: " + timeAndDatesAsStrings[1]);
//        System.out.println("End date: " + timeAndDatesAsStrings[2]);
//        System.out.println("End time: " + timeAndDatesAsStrings[3]);
        return formMdTableLine(timeAndDatesAsStrings[0], timeAndDatesAsStrings[1], timeAndDatesAsStrings[3], activityName);

//        LocalDate someDate = LocalDate.parse(timeAndDatesAsStrings[0], DateTimeFormatter.ofPattern("yyyy-MM-dd"));

//        formTimelogName(someDate);
//        return "| 2021-12-14 | 15:23-15:30 | 7 min | Updating UML diagrams |";
    }

    private static String formTimelogName(String record) {
//        var splitRecord = record.split(",");
        record = record.replace(" ", "");
        var splitRecord = record.split("\\|");

        var date = LocalDate.parse(splitRecord[1], DateTimeFormatter.ofPattern("yyyy-MM-dd"));;
        var weekDayAsNumber = (date.getDayOfWeek()).getValue();
        var firstWeekDay = date.minusDays(weekDayAsNumber - 1);
        var lastWeekDay = firstWeekDay.plusDays(6);

        var dateString = "(" + shortenedMonthName(firstWeekDay) + " "  + firstWeekDay.getDayOfMonth() + " - " + shortenedMonthName(lastWeekDay) + " "  + lastWeekDay.getDayOfMonth() + ")";
        calculateWeekNumber(date);
        var finName = calculateWeekNumber(date) + dateString;

        return finName;
    }

    private static String calculateWeekNumber(LocalDate date) {
        //TODO: implement this method

//        date = LocalDate.of(2022, 02, 25);
//        date = LocalDate.now();

//        System.out.println("X " + date);
//
//        System.out.println("Searching difference between " + date + " and " + firstDayOfTheProject);
//        System.out.println(ChronoUnit.WEEKS.between(firstDayOfTheProject, date));
        return "Week X ";
    }

    private static String shortenedMonthName(LocalDate date) {
        switch (date.getMonthValue()) {
            case 1:
                return "Jan.";
            case 2:
                return "Feb.";
            case 3:
                return "Mar.";
            case 4:
                return "Apr.";
            case 5:
                return "May";
            case 6:
                return "Jun.";
            case 7:
                return "Jul.";
            case 8:
                return "Aug.";
            case 9:
                return "Sep.";
            case 10:
                return "Oct.";
            case 11:
                return "Nov.";
            case 12:
                return "Dec.";
            default:
                return "Err.";
        }
    }

    private static String formMdTableLine(String date, String startTime, String endTime, String activityName) {
        return "| " + date + " | " + formatTime(startTime) + "-" + formatTime(endTime) + " | " + findDuration(startTime, endTime) + " | " + activityName + " |";
    }

    private static String findDuration(String startTime, String endTime) {
        var start = timeInMinutes(startTime);
        var end = timeInMinutes(endTime);

        if (start > end) {
            end += 60*24;
        }
        var duration = (end - start);

        return duration + " min";
    }

    private static int timeInMinutes(String time) {
        var numbers = time.split(":");
        var hours = Integer.valueOf(numbers[0]);
        var minutes = Integer.valueOf(numbers[1]);

        return hours * 60 + minutes;
    }

    private static String formatTime(String time_raw) {
        String[] timeSplit = time_raw.split(":");
        return timeSplit[0] + ":" + timeSplit[1];
    }

    private static String getTimeAndDates(String line) {
        String[] splitData = line.split(isBillableAsSplitter(line));

//        for (String s : splitData) {
//            System.out.println("|+| " + s);
//        }

        return splitData[1];
    }

    private static String isBillableAsSplitter(String line) {
        if (line.contains(",No,")) {
            return  ",No,";
        } else {
            return  ",Yes,";
        }
    }

    private static String removeNameAndEmail(String line) {
        String[] tempLine = line.split(",,");
        return tempLine[2];
    }
}
