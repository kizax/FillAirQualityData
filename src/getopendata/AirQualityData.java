/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package getopendata;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kizax
 */
public class AirQualityData {

    public final static float NOT_SET = -99999;

    private String siteName;
    private Date monitorDate;
    private String itemName;

    private float monitorValue00 = NOT_SET;
    private float monitorValue01 = NOT_SET;
    private float monitorValue02 = NOT_SET;
    private float monitorValue03 = NOT_SET;
    private float monitorValue04 = NOT_SET;
    private float monitorValue05 = NOT_SET;
    private float monitorValue06 = NOT_SET;
    private float monitorValue07 = NOT_SET;
    private float monitorValue08 = NOT_SET;
    private float monitorValue09 = NOT_SET;
    private float monitorValue10 = NOT_SET;
    private float monitorValue11 = NOT_SET;
    private float monitorValue12 = NOT_SET;
    private float monitorValue13 = NOT_SET;
    private float monitorValue14 = NOT_SET;
    private float monitorValue15 = NOT_SET;
    private float monitorValue16 = NOT_SET;
    private float monitorValue17 = NOT_SET;
    private float monitorValue18 = NOT_SET;
    private float monitorValue19 = NOT_SET;
    private float monitorValue20 = NOT_SET;
    private float monitorValue21 = NOT_SET;
    private float monitorValue22 = NOT_SET;
    private float monitorValue23 = NOT_SET;

    public AirQualityData(String recordStr) throws ParseException, ArrayIndexOutOfBoundsException {
        String[] recordStrArray = recordStr.split(",");

        this.siteName = recordStrArray[0];

        String dataTimeStr = recordStrArray[1];
        DateFormat dataFromat = new SimpleDateFormat("yyyy/M/d");
        this.monitorDate = dataFromat.parse(dataTimeStr);

        this.itemName = recordStrArray[2];

        try {
            this.monitorValue00 = strToFloat(recordStrArray[3]);
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            this.monitorValue01 = strToFloat(recordStrArray[4]);
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            this.monitorValue02 = strToFloat(recordStrArray[5]);
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            this.monitorValue03 = strToFloat(recordStrArray[6]);
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            this.monitorValue04 = strToFloat(recordStrArray[7]);
        } catch (ArrayIndexOutOfBoundsException e) {
        }

        try {
            this.monitorValue05 = strToFloat(recordStrArray[8]);
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            this.monitorValue06 = strToFloat(recordStrArray[9]);
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            this.monitorValue07 = strToFloat(recordStrArray[10]);
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            this.monitorValue08 = strToFloat(recordStrArray[11]);
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            this.monitorValue09 = strToFloat(recordStrArray[12]);
        } catch (ArrayIndexOutOfBoundsException e) {
        }

        try {
            this.monitorValue10 = strToFloat(recordStrArray[13]);
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            this.monitorValue11 = strToFloat(recordStrArray[14]);
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            this.monitorValue12 = strToFloat(recordStrArray[15]);
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            this.monitorValue13 = strToFloat(recordStrArray[16]);
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            this.monitorValue14 = strToFloat(recordStrArray[17]);
        } catch (ArrayIndexOutOfBoundsException e) {
        }

        try {
            this.monitorValue15 = strToFloat(recordStrArray[18]);
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            this.monitorValue16 = strToFloat(recordStrArray[19]);
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            this.monitorValue17 = strToFloat(recordStrArray[20]);
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            this.monitorValue18 = strToFloat(recordStrArray[21]);
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            this.monitorValue19 = strToFloat(recordStrArray[22]);
        } catch (ArrayIndexOutOfBoundsException e) {
        }

        try {
            this.monitorValue20 = strToFloat(recordStrArray[23]);
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            this.monitorValue21 = strToFloat(recordStrArray[24]);
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            this.monitorValue22 = strToFloat(recordStrArray[25]);
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            this.monitorValue23 = strToFloat(recordStrArray[26]);
        } catch (ArrayIndexOutOfBoundsException e) {
        }
    }

    @Override
    public String toString() {
        String airQualityDataStr = String.format("%1$s, %2$s, %3$s, "
                + "%4$f, %5$f, %6$f, %7$f, %8$f, "
                + "%9$f, %10$f, %11$f, %12$f, %13$f, "
                + "%14$f, %15$f, %16$f, %17$f, %18$f, "
                + "%19$f, %20$f, %21$f, %22$f, %23$f, "
                + "%24$f, %25$f, %26$f, %27$f", getSiteName(), getMonitorDateStr(), getItemName(), getMonitorValue00(), getMonitorValue01(), getMonitorValue02(), getMonitorValue03(), getMonitorValue04(), getMonitorValue05(), getMonitorValue06(), getMonitorValue07(), getMonitorValue08(), getMonitorValue09(), getMonitorValue10(), getMonitorValue11(), getMonitorValue12(), getMonitorValue13(), getMonitorValue14(), getMonitorValue15(), getMonitorValue16(), getMonitorValue17(), getMonitorValue18(), getMonitorValue19(), getMonitorValue20(), getMonitorValue21(), getMonitorValue22(), getMonitorValue23());
        return airQualityDataStr;
    }

    public String getRecordStr() {
        String recordStr = String.format("%1$s, %2$s, %3$s, "
                + "%4$s, %5$s, %6$s, %7$s, %8$s, "
                + "%9$s, %10$s, %11$s, %12$s, %13$s, "
                + "%14$s, %15$s, %16$s, %17$s, %18$s, "
                + "%19$s, %20$s, %21$s, %22$s, %23$s, "
                + "%24$s, %25$s, %26$s, %27$s", getSiteName(), getMonitorDateStr(), getItemName(),
                getMonitorValueStr(monitorValue00), getMonitorValueStr(monitorValue01), getMonitorValueStr(monitorValue02),
                getMonitorValueStr(monitorValue03), getMonitorValueStr(monitorValue04), getMonitorValueStr(monitorValue05),
                getMonitorValueStr(monitorValue06), getMonitorValueStr(monitorValue07), getMonitorValueStr(monitorValue08),
                getMonitorValueStr(monitorValue09), getMonitorValueStr(monitorValue10), getMonitorValueStr(monitorValue11),
                getMonitorValueStr(monitorValue12), getMonitorValueStr(monitorValue13), getMonitorValueStr(monitorValue14),
                getMonitorValueStr(monitorValue15), getMonitorValueStr(monitorValue16), getMonitorValueStr(monitorValue17),
                getMonitorValueStr(monitorValue18), getMonitorValueStr(monitorValue19), getMonitorValueStr(monitorValue20),
                getMonitorValueStr(monitorValue21), getMonitorValueStr(monitorValue22), getMonitorValueStr(monitorValue23));
        return recordStr;
    }

    String getMonitorDateStr() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy/M/d"); //2016/1/15
        String timeStr = timeFormat.format(getMonitorDate());
        return timeStr;
    }

    private float strToFloat(String str) {
        float value = NOT_SET;
        try {
            value = Float.valueOf(str.trim());
        } catch (NumberFormatException e) {
        };
        return value;
    }

    private String getMonitorValueStr(float monitorValue) {
        String monitorValueStr = "X";
        if (monitorValue != NOT_SET) {
            monitorValueStr = Float.toString(monitorValue);
        }
        return monitorValueStr;
    }

    /**
     * @return the siteName
     */
    public String getSiteName() {
        return siteName;
    }

    /**
     * @param siteName the siteName to set
     */
    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    /**
     * @return the monitorDate
     */
    public Date getMonitorDate() {
        return monitorDate;
    }

    /**
     * @param monitorDate the monitorDate to set
     */
    public void setMonitorDate(Date monitorDate) {
        this.monitorDate = monitorDate;
    }

    /**
     * @return the itemName
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * @param itemName the itemName to set
     */
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    /**
     * @return the monitorValue00
     */
    public float getMonitorValue00() {
        return monitorValue00;
    }

    /**
     * @param monitorValue00 the monitorValue00 to set
     */
    public void setMonitorValue00(float monitorValue00) {
        this.monitorValue00 = monitorValue00;
    }

    /**
     * @return the monitorValue01
     */
    public float getMonitorValue01() {
        return monitorValue01;
    }

    /**
     * @param monitorValue01 the monitorValue01 to set
     */
    public void setMonitorValue01(float monitorValue01) {
        this.monitorValue01 = monitorValue01;
    }

    /**
     * @return the monitorValue02
     */
    public float getMonitorValue02() {
        return monitorValue02;
    }

    /**
     * @param monitorValue02 the monitorValue02 to set
     */
    public void setMonitorValue02(float monitorValue02) {
        this.monitorValue02 = monitorValue02;
    }

    /**
     * @return the monitorValue03
     */
    public float getMonitorValue03() {
        return monitorValue03;
    }

    /**
     * @param monitorValue03 the monitorValue03 to set
     */
    public void setMonitorValue03(float monitorValue03) {
        this.monitorValue03 = monitorValue03;
    }

    /**
     * @return the monitorValue04
     */
    public float getMonitorValue04() {
        return monitorValue04;
    }

    /**
     * @param monitorValue04 the monitorValue04 to set
     */
    public void setMonitorValue04(float monitorValue04) {
        this.monitorValue04 = monitorValue04;
    }

    /**
     * @return the monitorValue05
     */
    public float getMonitorValue05() {
        return monitorValue05;
    }

    /**
     * @param monitorValue05 the monitorValue05 to set
     */
    public void setMonitorValue05(float monitorValue05) {
        this.monitorValue05 = monitorValue05;
    }

    /**
     * @return the monitorValue06
     */
    public float getMonitorValue06() {
        return monitorValue06;
    }

    /**
     * @param monitorValue06 the monitorValue06 to set
     */
    public void setMonitorValue06(float monitorValue06) {
        this.monitorValue06 = monitorValue06;
    }

    /**
     * @return the monitorValue07
     */
    public float getMonitorValue07() {
        return monitorValue07;
    }

    /**
     * @param monitorValue07 the monitorValue07 to set
     */
    public void setMonitorValue07(float monitorValue07) {
        this.monitorValue07 = monitorValue07;
    }

    /**
     * @return the monitorValue08
     */
    public float getMonitorValue08() {
        return monitorValue08;
    }

    /**
     * @param monitorValue08 the monitorValue08 to set
     */
    public void setMonitorValue08(float monitorValue08) {
        this.monitorValue08 = monitorValue08;
    }

    /**
     * @return the monitorValue09
     */
    public float getMonitorValue09() {
        return monitorValue09;
    }

    /**
     * @param monitorValue09 the monitorValue09 to set
     */
    public void setMonitorValue09(float monitorValue09) {
        this.monitorValue09 = monitorValue09;
    }

    /**
     * @return the monitorValue10
     */
    public float getMonitorValue10() {
        return monitorValue10;
    }

    /**
     * @param monitorValue10 the monitorValue10 to set
     */
    public void setMonitorValue10(float monitorValue10) {
        this.monitorValue10 = monitorValue10;
    }

    /**
     * @return the monitorValue11
     */
    public float getMonitorValue11() {
        return monitorValue11;
    }

    /**
     * @param monitorValue11 the monitorValue11 to set
     */
    public void setMonitorValue11(float monitorValue11) {
        this.monitorValue11 = monitorValue11;
    }

    /**
     * @return the monitorValue12
     */
    public float getMonitorValue12() {
        return monitorValue12;
    }

    /**
     * @param monitorValue12 the monitorValue12 to set
     */
    public void setMonitorValue12(float monitorValue12) {
        this.monitorValue12 = monitorValue12;
    }

    /**
     * @return the monitorValue13
     */
    public float getMonitorValue13() {
        return monitorValue13;
    }

    /**
     * @param monitorValue13 the monitorValue13 to set
     */
    public void setMonitorValue13(float monitorValue13) {
        this.monitorValue13 = monitorValue13;
    }

    /**
     * @return the monitorValue14
     */
    public float getMonitorValue14() {
        return monitorValue14;
    }

    /**
     * @param monitorValue14 the monitorValue14 to set
     */
    public void setMonitorValue14(float monitorValue14) {
        this.monitorValue14 = monitorValue14;
    }

    /**
     * @return the monitorValue15
     */
    public float getMonitorValue15() {
        return monitorValue15;
    }

    /**
     * @param monitorValue15 the monitorValue15 to set
     */
    public void setMonitorValue15(float monitorValue15) {
        this.monitorValue15 = monitorValue15;
    }

    /**
     * @return the monitorValue16
     */
    public float getMonitorValue16() {
        return monitorValue16;
    }

    /**
     * @param monitorValue16 the monitorValue16 to set
     */
    public void setMonitorValue16(float monitorValue16) {
        this.monitorValue16 = monitorValue16;
    }

    /**
     * @return the monitorValue17
     */
    public float getMonitorValue17() {
        return monitorValue17;
    }

    /**
     * @param monitorValue17 the monitorValue17 to set
     */
    public void setMonitorValue17(float monitorValue17) {
        this.monitorValue17 = monitorValue17;
    }

    /**
     * @return the monitorValue18
     */
    public float getMonitorValue18() {
        return monitorValue18;
    }

    /**
     * @param monitorValue18 the monitorValue18 to set
     */
    public void setMonitorValue18(float monitorValue18) {
        this.monitorValue18 = monitorValue18;
    }

    /**
     * @return the monitorValue19
     */
    public float getMonitorValue19() {
        return monitorValue19;
    }

    /**
     * @param monitorValue19 the monitorValue19 to set
     */
    public void setMonitorValue19(float monitorValue19) {
        this.monitorValue19 = monitorValue19;
    }

    /**
     * @return the monitorValue20
     */
    public float getMonitorValue20() {
        return monitorValue20;
    }

    /**
     * @param monitorValue20 the monitorValue20 to set
     */
    public void setMonitorValue20(float monitorValue20) {
        this.monitorValue20 = monitorValue20;
    }

    /**
     * @return the monitorValue21
     */
    public float getMonitorValue21() {
        return monitorValue21;
    }

    /**
     * @param monitorValue21 the monitorValue21 to set
     */
    public void setMonitorValue21(float monitorValue21) {
        this.monitorValue21 = monitorValue21;
    }

    /**
     * @return the monitorValue22
     */
    public float getMonitorValue22() {
        return monitorValue22;
    }

    /**
     * @param monitorValue22 the monitorValue22 to set
     */
    public void setMonitorValue22(float monitorValue22) {
        this.monitorValue22 = monitorValue22;
    }

    /**
     * @return the monitorValue23
     */
    public float getMonitorValue23() {
        return monitorValue23;
    }

    /**
     * @param monitorValue23 the monitorValue23 to set
     */
    public void setMonitorValue23(float monitorValue23) {
        this.monitorValue23 = monitorValue23;
    }

}
