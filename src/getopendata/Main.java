package getopendata;

import java.io.File;
import java.io.FileWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static final String logFileName = "./record/log.txt";

    public static void main(String[] args) {

        //建立log file
        File logFile = new File(logFileName);

        if (!logFile.getParentFile().exists()) {
            logFile.getParentFile().mkdirs();
        }

        FileWriter logFileWriter = null;
        try {
            if (!logFile.exists()) {
                logFile.createNewFile();
                logFileWriter = new FileWriter(logFile, true);

            } else {
                logFileWriter = new FileWriter(logFile, true);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        LogUtils.log(logFileWriter, String.format("%1$s\tStart air quality open data downloader!", TimestampUtils.getTimestampStr()));

        //建立itemMap
        Map<Integer, String> itemMap = new HashMap();
        itemMap.put(1, "SO2");
        itemMap.put(2, "CO");
        itemMap.put(3, "O3");
        itemMap.put(4, "PM10");
        itemMap.put(5, "NOx");

        itemMap.put(6, "NO");
        itemMap.put(7, "NO2");
        itemMap.put(14, "AMB_TEMP");
        itemMap.put(23, "RAINFALL");
        itemMap.put(33, "PM2.5");

        itemMap.put(38, "RH");

        //讀檔        
        ArrayList<AirQualityData> airQualityDataList = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader("./epa_air_2015.csv");

            BufferedReader bufferedReader = new BufferedReader(fileReader);

            int count = 0;
            while (bufferedReader.ready()) {
                String recordStr = bufferedReader.readLine();
                count++;
                try {
                    AirQualityData airQualityData = new AirQualityData(recordStr);
                    airQualityDataList.add(airQualityData);

                } catch (ParseException ex) {
                    System.out.println("Count: " + count + ", " + recordStr);
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            fileReader.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        Date yesterday = calendar.getTime();

    }
}
