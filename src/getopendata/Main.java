package getopendata;

import static getopendata.AirQualityData.NOT_SET;
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

    private static final String csvFileName = "./record/airQualityData.csv";
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
        Map<String, Integer> itemMap = new HashMap();
        itemMap.put("SO2", 1);
        itemMap.put("CO", 2);
        itemMap.put("O3", 3);
        itemMap.put("PM10", 4);
        itemMap.put("NOx", 5);

        itemMap.put("NO", 6);
        itemMap.put("NO2", 7);
        itemMap.put("AMB_TEMP", 14);
        itemMap.put("RAINFALL", 23);
        itemMap.put("PM2.5", 33);

        itemMap.put("RH", 38);

        //讀檔        
        ArrayList<AirQualityData> airQualityDataList = new ArrayList<>();
        try {
            LogUtils.log(logFileWriter, String.format("%1$s\tStart reading the record file", TimestampUtils.getTimestampStr()));

            FileReader fileReader = new FileReader("./epa_air_2015.csv");

            BufferedReader bufferedReader = new BufferedReader(fileReader);

            int lineCount = 0;
            while (bufferedReader.ready()) {
                String recordStr = bufferedReader.readLine();
                lineCount++;
                try {
                    AirQualityData airQualityData = new AirQualityData(recordStr);
                    airQualityData.setLineNum(lineCount);
                    airQualityDataList.add(airQualityData);

                } catch (ParseException ex) {
                    LogUtils.log(logFileWriter, String.format("%1$s\tLine %2$d has ParseException", TimestampUtils.getTimestampStr(), lineCount));
                }
            }

            fileReader.close();

            LogUtils.log(logFileWriter, String.format("%1$s\tSuccessfully reading the record file", TimestampUtils.getTimestampStr(), lineCount));

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        //建立hashMap<String,AirQualityData>   測站 日期 測項 -> airQualityData
        Map<String, AirQualityData> airQualityDataMap = new HashMap();
        for (AirQualityData airQualityData : airQualityDataList) {
            String key = String.format("%1$s %2$s %3$s",
                    airQualityData.getSiteName(), airQualityData.getMonitorDateStr(),
                    airQualityData.getItemName());
            airQualityDataMap.put(key, airQualityData);
        }

        //開始補值
        String key;
        for (AirQualityData airQualityData : airQualityDataList) {

            for (int index = 0; index < 24; index++) {
                if (airQualityData.getMonitorValue(index) == AirQualityData.NOT_SET) {

//                    LogUtils.log(logFileWriter, String.format(
//                            "%1$s\tLine %2$d air quality data %3$s / %4$s at %5$s %6$d o'clock has leaked value",
//                            TimestampUtils.getTimestampStr(), airQualityData.getLineNum(), airQualityData.getSiteName(),
//                            airQualityData.getItemName(), airQualityData.getMonitorDateStr(), index));
                    //初始化參考值
                    float[] refValues = new float[10];
                    for (int i = 0; i < 10; i++) {
                        refValues[i] = NOT_SET;
                    }

                    //   同一測站同日前一小時
                    try {
                        refValues[0] = airQualityData.getMonitorValue(index - 1);
                    } catch (ArrayIndexOutOfBoundsException e) {
                    }

                    //前一日同時、前一日前一小時、前一日後一小時
                    Calendar yesterdayCalendar = Calendar.getInstance();
                    yesterdayCalendar.setTime(airQualityData.getMonitorDate());
                    yesterdayCalendar.add(Calendar.DATE, -1);
                    Date yesterday = yesterdayCalendar.getTime();
                    key = String.format("%1$s %2$s %3$s",
                            airQualityData.getSiteName(), TimestampUtils.dateToStr(yesterday),
                            airQualityData.getItemName());

                    AirQualityData yesterdayAirQualityData = airQualityDataMap.get(key);
                    try {
                        refValues[1] = yesterdayAirQualityData.getMonitorValue(index - 1);
                    } catch (ArrayIndexOutOfBoundsException e) {
                    } catch (NullPointerException e) {
                    }

                    try {
                        refValues[2] = yesterdayAirQualityData.getMonitorValue(index);
                    } catch (ArrayIndexOutOfBoundsException e) {
                    } catch (NullPointerException e) {
                    }

                    try {
                        refValues[3] = yesterdayAirQualityData.getMonitorValue(index + 1);
                    } catch (ArrayIndexOutOfBoundsException e) {
                    } catch (NullPointerException e) {
                    }

                    //前一週同時、前一週前一小時、前一週後一小時
                    Calendar lastWeekCalendar = Calendar.getInstance();
                    lastWeekCalendar.setTime(airQualityData.getMonitorDate());
                    lastWeekCalendar.add(Calendar.DATE, -7);
                    Date lastWeek = lastWeekCalendar.getTime();
                    key = String.format("%1$s %2$s %3$s",
                            airQualityData.getSiteName(), TimestampUtils.dateToStr(lastWeek),
                            airQualityData.getItemName());

                    AirQualityData lastWeekAirQualityData = airQualityDataMap.get(key);
                    try {
                        refValues[4] = lastWeekAirQualityData.getMonitorValue(index - 1);
                    } catch (ArrayIndexOutOfBoundsException e) {
                    } catch (NullPointerException e) {
                    }

                    try {
                        refValues[5] = lastWeekAirQualityData.getMonitorValue(index);
                    } catch (ArrayIndexOutOfBoundsException e) {
                    } catch (NullPointerException e) {
                    }

                    try {
                        refValues[6] = lastWeekAirQualityData.getMonitorValue(index + 1);
                    } catch (ArrayIndexOutOfBoundsException e) {
                    } catch (NullPointerException e) {
                    }

                    //前一年同週同時、前一年同週前一小時、前一年同週後一小時
                    Calendar prevYearCalendar = Calendar.getInstance();
                    prevYearCalendar.setTime(airQualityData.getMonitorDate());
                    prevYearCalendar.add(Calendar.YEAR, -1);
                    Date prevYear = prevYearCalendar.getTime();
                    key = String.format("%1$s %2$s %3$s",
                            airQualityData.getSiteName(), TimestampUtils.dateToStr(prevYear),
                            airQualityData.getItemName());

                    AirQualityData prevYearAirQualityData = airQualityDataMap.get(key);
                    try {
                        refValues[7] = prevYearAirQualityData.getMonitorValue(index - 1);
                    } catch (ArrayIndexOutOfBoundsException e) {
                    } catch (NullPointerException e) {
                    }

                    try {
                        refValues[8] = prevYearAirQualityData.getMonitorValue(index);
                    } catch (ArrayIndexOutOfBoundsException e) {
                    } catch (NullPointerException e) {
                    }

                    try {
                        refValues[9] = prevYearAirQualityData.getMonitorValue(index + 1);
                    } catch (ArrayIndexOutOfBoundsException e) {
                    } catch (NullPointerException e) {
                    }

                    int validValueCount = 0;
                    float sum = 0;
                    for (int i = 0; i < 10; i++) {
                        if (refValues[i] != AirQualityData.NOT_SET) {
                            sum += refValues[i];
                            validValueCount++;
                        }
                    }

                    if (validValueCount >= 5) {
                        float avg = sum / validValueCount;
                        airQualityData.setMonitorValue(avg, index);
                        LogUtils.log(logFileWriter, String.format("%1$s\tLine %2$d air quality data %3$s / %4$s at %5$s %6$d o'clock filled whth %7$f",
                                TimestampUtils.getTimestampStr(), airQualityData.getLineNum(), airQualityData.getSiteName(), airQualityData.getItemName(), airQualityData.getMonitorDateStr(), index, avg));
                    } else {
                        LogUtils.log(logFileWriter, String.format("%1$s\tLine %2$d air quality data %3$s / %4$s at %5$s %6$d o'clock has leaked value bu not filled",
                                TimestampUtils.getTimestampStr(), airQualityData.getLineNum(), airQualityData.getSiteName(), airQualityData.getItemName(), airQualityData.getMonitorDateStr(), index));
                    }
                }
            }

        }
        try {
            //建立紀錄檔
            LogUtils.log(logFileWriter, String.format("%1$s\tNow start writing data into file", TimestampUtils.getTimestampStr()));

            File csvDataFile = new File(csvFileName);

            if (!csvDataFile.getParentFile().exists()) {
                csvDataFile.getParentFile().mkdirs();
            }
            FileWriter csvFileWriter;

            csvFileWriter = new FileWriter(csvDataFile, true);

            //寫入檔頭BOM，避免EXCEL開啟變成亂碼
            byte[] bom = new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
            csvFileWriter.write(new String(bom));

            //寫入紀錄檔
            int writingCount = 0;
            for (AirQualityData airQualityData : airQualityDataMap.values()) {
                writeCsvFile(csvFileWriter, airQualityData.getRecordStr());
                writingCount++;
            }

            LogUtils.log(logFileWriter, String.format("%1$s\tSuccessfully writing %2$d data into record file", TimestampUtils.getTimestampStr(), writingCount));
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void writeCsvFile(FileWriter csvFileWriter, String record) {
        WriteThread writerThread = new WriteThread(csvFileWriter, record);
        writerThread.start();
    }
}
