import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.Calendar;
import java.text.DateFormat;

public class CalendarEditor {
    public static void main(String[] args) throws Exception {
        // arg[0] must be the name of the ics file to be edited
        String original = args[0];

        int extensionIndex = args[0].indexOf(".ics");
        String fileName = args[0].substring(0, extensionIndex);
        BufferedWriter bw = new BufferedWriter(new FileWriter(fileName + "_edited.ics"));

        System.out.println("... Creating new ICS file with your desired changes ...");
        readOriginal(original, bw);
        bw.close();
        System.out.println("\nDone! Your new ICS file has been created.");
        return;
    }

    public static String genUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public static String changeDate(String line, int skip) throws ParseException {
        String dateString = line.substring(skip, line.length());
        Date date = new SimpleDateFormat("yyyyMMdd").parse(dateString);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, 1);
        date = c.getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        dateString = dateFormat.format(date);
        return dateString;
    }

    public static void readOriginal(String originalString, BufferedWriter bw) throws IOException, ParseException {
        BufferedReader original = new BufferedReader(new FileReader(originalString));
        String line;
        String startTime = "DTSTART;VALUE=DATE:"; // 19
        String endTime = "DTEND;VALUE=DATE:"; // 17
        String summary = "SUMMARY;LANGUAGE=de:"; // 20
        String uid = "UID:";
        String description = "DESCRIPTION;LANGUAGE=de:";
        String dateString;
        while ((line = original.readLine()) != null) {
            if (line.startsWith(startTime)) {
                dateString = changeDate(line, 19);
                bw.write(startTime + dateString);
                bw.newLine();
                bw.flush();
            } else if (line.startsWith(endTime)) {
                dateString = changeDate(line, 17);
                bw.write(endTime + dateString);
                bw.newLine();
                bw.flush();
            } else if (line.startsWith(summary)) {
                bw.write(line + "rein holen");
                bw.newLine();
                bw.flush();
            } else if (line.startsWith(description)) {
                bw.write(description.substring(0, 24) + "Tonne bitte wieder rein stellen.");
                bw.newLine();
                bw.flush();
            } else if (line.startsWith(uid)) {
                String uuid = genUUID();
                bw.write("UID:" + uuid);
                bw.newLine();
                bw.flush();
            } else {
                bw.write(line);
                bw.newLine();
                bw.flush();
            }
        }
        original.close();
    }
}