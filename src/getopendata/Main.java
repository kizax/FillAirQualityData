package getopendata;

import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public class Main {

    private static final String logFileName = "./record/log.txt";

    public static void main(String[] args) {

        //建立log file
        FileWriter logFileWriter = Step.createFileWriter(logFileName, true);
        LogUtils.log(logFileWriter, String.format("%1$s\tStart fill air quality data!", TimestampUtils.getTimestampStr()));

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

        ArrayList<String> fileNameList = Step.listFilesForFolder("./");

        for (String fileName : fileNameList) {

            String csvResultFileName = "./record/" + stripExtension(fileName) + "_afterFillValue.csv";

            //讀檔        
            ArrayList<AirQualityRecordData> airQualityDataList = Step.readFile(fileName, logFileWriter);

            //建立hashMap<String,AirQualityData>   測站 日期 測項 -> airQualityData
            Map<String, AirQualityRecordData> airQualityDataMap = Step.generateAirQualityDataMap(airQualityDataList, logFileWriter);

            //開始補值
            int numOfNotFilledValueLastTime = Integer.MAX_VALUE;
            int numOfNotFilledValue;
            int roundCount = 0;
            while (true) {
                roundCount++;
                LogUtils.log(logFileWriter, String.format("%1$s\tStart filling data, round %2$d", TimestampUtils.getTimestampStr(), roundCount));
                numOfNotFilledValue = Step.fillUpAirQualityData(airQualityDataMap, airQualityDataList, logFileWriter);
                LogUtils.log(logFileWriter, String.format("%1$s\tRound %2$d still have %3$d not filled value", TimestampUtils.getTimestampStr(), roundCount, numOfNotFilledValue));

                if (numOfNotFilledValue != numOfNotFilledValueLastTime) {
                    numOfNotFilledValueLastTime = numOfNotFilledValue;
                } else {
                     LogUtils.log(logFileWriter, String.format("%1$s\tCannot fill sothing more values, break", TimestampUtils.getTimestampStr()));
                    break;
                }
            }

            //建立紀錄檔
            LogUtils.log(logFileWriter, String.format("%1$s\tNow start writing data into file", TimestampUtils.getTimestampStr()));
            FileWriter csvResultFileWriter = Step.createFileWriter(csvResultFileName, false);

            //寫檔
            Step.writeFile(csvResultFileWriter, airQualityDataList, logFileWriter);
        }
    }

    private static String stripExtension(String str) {
        // Handle null case specially.

        if (str == null) {
            return null;
        }

        // Get position of last '.'.
        int pos = str.lastIndexOf(".");

        // If there wasn't any '.' just return the string as is.
        if (pos == -1) {
            return str;
        }

        // Otherwise return the string, up to the dot.
        return str.substring(0, pos);
    }

}
