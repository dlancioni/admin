/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.util.Date;

/**
 *
 * @author david
 */
public class CENTField 
{
    
    private String fieldObject = "";
    private int fieldType = 0;
    
    private String fieldValueText = "";
    private Date fieldValueDate;
    private int fieldValueInt;
    private double fieldValueDouble;
    private int fieldValueBoolean;

    /**
     * @return the fieldName
     */
    public String getFieldObject() {
        return fieldObject.trim().toLowerCase();
    }

    /**
     * @param fieldName the fieldName to set
     */
    public void setFieldObject(String fieldName) {
        this.fieldObject = fieldName;
    }

    /**
     * @return the fieldType
     */
    public int getFieldType() {
        return fieldType;
    }

    /**
     * @param fieldType the fieldType to set
     */
    public void setFieldType(int fieldType) {
        this.fieldType = fieldType;
    }

    /**
     * @return the fieldValueText
     */
    public String getFieldValueText() {
        return fieldValueText;
    }

    /**
     * @param fieldValueText the fieldValueText to set
     */
    public void setFieldValueText(String fieldValueText) {
        this.fieldValueText = fieldValueText;
    }

    /**
     * @return the fieldValueDate
     */
    public Date getFieldValueDate() {
        return fieldValueDate;
    }

    /**
     * @param fieldValueDate the fieldValueDate to set
     */
    public void setFieldValueDate(Date fieldValueDate) {
        this.fieldValueDate = fieldValueDate;
    }

    /**
     * @return the fieldValueInt
     */
    public int getFieldValueInt() {
        return fieldValueInt;
    }

    /**
     * @param fieldValueInt the fieldValueInt to set
     */
    public void setFieldValueInt(int fieldValueInt) {
        this.fieldValueInt = fieldValueInt;
    }

    /**
     * @return the fieldValueDouble
     */
    public double getFieldValueDouble() {
        return fieldValueDouble;
    }

    /**
     * @param fieldValueDouble the fieldValueDouble to set
     */
    public void setFieldValueDouble(double fieldValueDouble) {
        this.fieldValueDouble = fieldValueDouble;
    }
    
    /**
     * @return the fieldValueBoolean
     */
    public int getFieldValueBoolean() {
        return fieldValueBoolean;
    }

    /**
     * @param fieldValueBoolean the fieldValueBoolean to set
     */
    public void setFieldValueBoolean(int fieldValueBoolean) {
        this.fieldValueBoolean = fieldValueBoolean;
    }    
}
