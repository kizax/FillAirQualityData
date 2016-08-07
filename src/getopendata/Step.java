/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package getopendata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kizax
 */
public class Step {

    public static ArrayList<AirQualityData> readFile(String fileName, FileWriter logFileWriter) {
        ArrayList<AirQualityData> airQualityDataList = new ArrayList<>();
        try {
            LogUtils.log(logFileWriter, String.format("%1$s\tStart reading the record file", TimestampUtils.getTimestampStr()));

            FileReader fileReader = new FileReader(fileName);

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
            LogUtils.log(logFileWriter, String.format("%1$s\tThere is no file in %2$s", TimestampUtils.getTimestampStr(), fileName));
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        return airQualityDataList;
    }

    public static Map<String, AirQualityData> generateAirQualityDataMap(ArrayList<AirQualityData> airQualityDataList, FileWriter logFileWriter) {
        //建立hashMap<String,AirQualityData>   測站 日期 測項 -> airQualityData
        Map<String, AirQualityData> airQualityDataMap = new HashMap();
        for (AirQualityData airQualityData : airQualityDataList) {
            String key = String.format("%1$s %2$s %3$s",
                    airQualityData.getSiteName(), airQualityData.getMonitorDateStr(),
                    airQualityData.getItemName());
            airQualityDataMap.put(key, airQualityData);
        }

        return airQualityDataMap;
    }

    public static void fillUpAirQualityData(Map<String, AirQualityData> airQualityDataMap,
            ArrayList<AirQualityData> airQualityDataList, FileWriter logFileWriter) {

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
                        refValues[i] = AirQualityData.NOT_SET;
                    }

                    //   同一測站同日前一小時
                    refValues[0] = getMonitorValue(airQualityData, airQualityData, index - 1, "前一小時", logFileWriter);

                    //前一日同時、前一日前一小時、前一日後一小時
                    Calendar yesterdayCalendar = Calendar.getInstance();
                    yesterdayCalendar.setTime(airQualityData.getMonitorDate());
                    yesterdayCalendar.add(Calendar.DATE, -1);
                    Date yesterday = yesterdayCalendar.getTime();
                    key = String.format("%1$s %2$s %3$s",
                            airQualityData.getSiteName(), TimestampUtils.dateToStr(yesterday),
                            airQualityData.getItemName());

                    AirQualityData yesterdayAirQualityData = airQualityDataMap.get(key);
                    refValues[1] = getMonitorValue(airQualityData, yesterdayAirQualityData, index - 1, "前一天前一小時", logFileWriter);
                    refValues[2] = getMonitorValue(airQualityData, yesterdayAirQualityData, index, "前一天同一小時", logFileWriter);
                    refValues[3] = getMonitorValue(airQualityData, yesterdayAirQualityData, index + 1, "前一天後一小時", logFileWriter);

                    //前一週同時、前一週前一小時、前一週後一小時
                    Calendar lastWeekCalendar = Calendar.getInstance();
                    lastWeekCalendar.setTime(airQualityData.getMonitorDate());
                    lastWeekCalendar.add(Calendar.DATE, -7);
                    Date lastWeek = lastWeekCalendar.getTime();
                    key = String.format("%1$s %2$s %3$s",
                            airQualityData.getSiteName(), TimestampUtils.dateToStr(lastWeek),
                            airQualityData.getItemName());

                    AirQualityData lastWeekAirQualityData = airQualityDataMap.get(key);
                    refValues[4] = getMonitorValue(airQualityData, lastWeekAirQualityData, index - 1, "前一週前一小時", logFileWriter);
                    refValues[5] = getMonitorValue(airQualityData, lastWeekAirQualityData, index, "前一週同一小時", logFileWriter);
                    refValues[6] = getMonitorValue(airQualityData, lastWeekAirQualityData, index + 1, "前一週後一小時", logFileWriter);

                    //前一年同週同時、前一年同週前一小時、前一年同週後一小時
                    Calendar prevYearCalendar = Calendar.getInstance();
                    prevYearCalendar.setTime(airQualityData.getMonitorDate());
                    prevYearCalendar.add(Calendar.YEAR, -1);
                    Date prevYear = prevYearCalendar.getTime();
                    key = String.format("%1$s %2$s %3$s",
                            airQualityData.getSiteName(), TimestampUtils.dateToStr(prevYear),
                            airQualityData.getItemName());

                    AirQualityData prevYearAirQualityData = airQualityDataMap.get(key);
                    refValues[7] = getMonitorValue(airQualityData, prevYearAirQualityData, index - 1, "前一年同週前一小時", logFileWriter);
                    refValues[8] = getMonitorValue(airQualityData, prevYearAirQualityData, index, "前一年同週同一小時", logFileWriter);
                    refValues[9] = getMonitorValue(airQualityData, prevYearAirQualityData, index + 1, "前一年同週後一小時", logFileWriter);

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
                        LogUtils.log(logFileWriter, String.format("%1$s\tLine %2$d air quality data %3$s / %4$s at %5$s %6$d o'clock has leaked value but not filled",
                                TimestampUtils.getTimestampStr(), airQualityData.getLineNum(), airQualityData.getSiteName(), airQualityData.getItemName(), airQualityData.getMonitorDateStr(), index));
                        LogUtils.log(logFileWriter, String.format("%1$s\tValid value count: %2$d, %3$s",
                                TimestampUtils.getTimestampStr(), validValueCount, convertArrayToStr(refValues)));

                    }
                }
            }

        }

    }

    public static ArrayList<String> listFilesForFolder(String  folderName) {

        File folder = new File(folderName);
        
        ArrayList<String> fileNameList = new ArrayList();
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
            } else {
                String fileName = fileEntry.getName();
                String extension = "";

                int i = fileName.lastIndexOf('.');
                if (i > 0) {
                    extension = fileName.substring(i + 1);
                }

                if (extension.equals("csv")) {
                    fileNameList.add(fileName);
                }

            }
        }
        
        return fileNameList;
    }

    public static void writeFile(FileWriter resultFileWriter,
            ArrayList<AirQualityData> airQualityDataList, FileWriter logFileWriter) {
        try {
            //寫入檔頭BOM，避免EXCEL開啟變成亂碼
            byte[] bom = new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
            resultFileWriter.write(new String(bom));

            //寫入紀錄檔
            int writingCount = 0;
            for (AirQualityData airQualityData : airQualityDataList) {
                writeCsvFile(resultFileWriter, airQualityData.getRecordStr());
                writingCount++;
            }

            LogUtils.log(logFileWriter, String.format("%1$s\tSuccessfully writing %2$d data into record file", TimestampUtils.getTimestampStr(), writingCount));
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static FileWriter createFileWriter(String fileName, boolean append) {
        //建立log file
        File file = new File(fileName);

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        FileWriter fileWriter = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
                fileWriter = new FileWriter(file, append);
            } else {
                fileWriter = new FileWriter(file, append);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return fileWriter;
    }

    private static void writeCsvFile(FileWriter csvFileWriter, String record) {
        WriteThread writerThread = new WriteThread(csvFileWriter, record);
        writerThread.start();
    }

    private static float getMonitorValue(AirQualityData airQualityData, AirQualityData refAirQualityData, int index, String str, FileWriter logFileWriter) {

        float monitorValue = AirQualityData.NOT_SET;
        try {
            monitorValue = refAirQualityData.getMonitorValue(index);
        } catch (ArrayIndexOutOfBoundsException e) {
        } catch (NullPointerException e) {
        }
        if (monitorValue == AirQualityData.NOT_SET) {
//            LogUtils.log(logFileWriter, String.format("%1$s\tLine %2$d air quality data %3$s / %4$s at %5$s %6$d o'clock doesn't have %7$s value",
//                    TimestampUtils.getTimestampStr(), airQualityData.getLineNum(), airQualityData.getSiteName(),
//                    airQualityData.getItemName(), airQualityData.getMonitorDateStr(), index, str));
        }

        return monitorValue;
    }

    private static String convertArrayToStr(float[] floatArray) {
        String str = "";
        int count = 0;
        for (float f : floatArray) {
            str += ("[" + count + "] " + Float.toString(f) + " ");
            count++;
        }
        return str;
    }
}
