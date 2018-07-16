/*
 * Developed by David Lancioni - 07/2015
 * Dynamic solution to traffic data from presentation layer until persistence layer
 * The constructor is mandatory due to logging and formatting solution
 */
package entity;

import java.lang.reflect.Field;
import java.util.Date;

/*
 *
 * @author David Lancioni
 */
public class CENTData extends CENT implements Cloneable
{       

    public CENTData() throws Exception
    {
        this.Clear();
    }    

    /*
     * Used to clear the class instance
     */             
    public void Clear() throws Exception
    { 
        for (int i=1; i<= TABLE_FIELD_COUNT; i++)
        {
            this.SetInt(i, Integer.MIN_VALUE);
            this.SetText(i, "");            
            this.SetDate(i, null);            
            this.SetDouble(i, Double.MIN_VALUE);
            this.SetBoolean(i, Null);                        
        }
        
        this.SetInt(SYSTEM_FIELD_PAGING_START, Integer.MIN_VALUE);
        this.SetInt(SYSTEM_FIELD_PAGING_END, Integer.MIN_VALUE);        
    }    

    /*
     * Getter and Setter for INT
     */     
    public void SetInt(int intId, int intValue) throws Exception
    {                
        /*
         * General Declaration
         */
         Class<CENTData> ENTModel1;
         Field Field1 = null;
         
        try
        {
            ENTModel1 = CENTData.class;
            Field1 = ENTModel1.getField("int_" + intId);                        
            Field1.set(this, intValue);
        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }
    }
    
    public void SetInt(String strFieldName, int intValue) throws Exception
    {                
        /*
         * General Declaration
         */
         Class<CENTData> ENTModel1;
         Field Field1 = null;
         
        try
        {
            ENTModel1 = CENTData.class;
            Field1 = ENTModel1.getField(strFieldName);                        
            Field1.set(this, intValue);
        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }
    }    

    public int GetInt(int intId) throws Exception
    {
        /*
         * General Declaration
         */
         Object Object1 = null;
         Class<CENTData> ENTModel1;
         Field Field1 = null;                  
                 
        try
        {
            ENTModel1 = CENTData.class;
            
            Field1 = ENTModel1.getField("int_" + intId);
            
            Field1.setAccessible(true);
                                    
            Object1 = Field1.get(this);

            if (Object1 != null)
                return (Integer) Object1;
            else
                return 0;                  

        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }               
    }  

    public int GetInt(String strFieldName) throws Exception
    {
        /*
         * General Declaration
         */
         Object Object1 = null;
         Class<CENTData> ENTModel1;
         Field Field1 = null;                  
                 
        try
        {
            ENTModel1 = CENTData.class;
            
            Field1 = ENTModel1.getField(strFieldName);
            
            Field1.setAccessible(true);
                                    
            Object1 = Field1.get(this);

            if (Object1 != null)
                return (Integer) Object1;
            else
                return 0;                  

        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }               
    }
    
    /*
     * Getter and Setter for TEXT
     */
    public void SetText(int intId, String strValue) throws Exception
    {                
        /*
         * General Declaration
         */
         Class<CENTData> ENTModel1;
         Field Field1 = null;
         
        try
        {
            ENTModel1 = CENTData.class;
            Field1 = ENTModel1.getField("text_" + intId);                        
            Field1.set(this, strValue.trim());
        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }
    } 
    
    public void SetText(String strFieldName, String strValue) throws Exception
    {                
        /*
         * General Declaration
         */
         Class<CENTData> ENTModel1;
         Field Field1 = null;
         
        try
        {
            ENTModel1 = CENTData.class;
            Field1 = ENTModel1.getField(strFieldName);                        
            Field1.set(this, strValue.trim());
        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }
    }         
        
    public String GetText(int intId) throws Exception
    {
        /*
         * General Declaration
         */
         Object Object1 = null;
         Class<CENTData> ENTModel1;
         Field Field1 = null;                  
                 
        try
        {
            ENTModel1 = CENTData.class;
            
            Field1 = ENTModel1.getField("text_" + intId);
            
            Field1.setAccessible(true);
                                    
            Object1 = Field1.get(this);
            
            if (Object1 != null)
                return Object1.toString().trim();
            else
                return "";              

        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }               
    }   
    
    public String GetText(String strFieldName) throws Exception
    {
        /*
         * General Declaration
         */
         Object Object1 = null;
         Class<CENTData> ENTModel1;
         Field Field1 = null;                  
                 
        try
        {
            ENTModel1 = CENTData.class;
            
            Field1 = ENTModel1.getField(strFieldName);
            
            Field1.setAccessible(true);
                                    
            Object1 = Field1.get(this);
            
            if (Object1 != null)
                return Object1.toString().trim();
            else
                return "";              

        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }               
    }       
        
    
    /*
     * Getter and Setter for DATE
     */    
    public void SetDate(int intId, Date datValue) throws Exception
    {                
        /*
         * General Declaration
         */
         Class<CENTData> ENTModel1;
         Field Field1 = null;
         
        try
        {
            ENTModel1 = CENTData.class;
            Field1 = ENTModel1.getField("date_" + intId);                        
            Field1.set(this, datValue);
        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }
    }      
    
    public void SetDate(String strFieldName, Date datValue) throws Exception
    {                
        /*
         * General Declaration
         */
         Class<CENTData> ENTModel1;
         Field Field1 = null;
         
        try
        {
            ENTModel1 = CENTData.class;
            Field1 = ENTModel1.getField(strFieldName);                        
            Field1.set(this, datValue);
        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }
    }           
    
    public Date GetDate(int intId) throws Exception
    {
        /*
         * General Declaration
         */
         Object Object1 = null;
         Class<CENTData> ENTModel1;
         Field Field1 = null;                  
                 
        try
        {
            ENTModel1 = CENTData.class;
            
            Field1 = ENTModel1.getField("date_" + intId);
            
            Field1.setAccessible(true);
                                    
            Object1 = Field1.get(this);
            
            if (Object1 != null)
                return (Date) Object1;
            else
                return null;         
        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }               
    }
    
    public Date GetDate(String strFieldName) throws Exception
    {
        /*
         * General Declaration
         */
         Object Object1 = null;
         Class<CENTData> ENTModel1;
         Field Field1 = null;                  
                 
        try
        {
            ENTModel1 = CENTData.class;
            
            Field1 = ENTModel1.getField(strFieldName);
            
            Field1.setAccessible(true);
                                    
            Object1 = Field1.get(this);
            
            if (Object1 != null)
                return (Date) Object1;
            else
                return null;         
        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }               
    }    

    
    /*
     * Getter and Setter for DOUBLE
     */
    public void SetDouble(int intId, double dblValue) throws Exception
    {                
        /*
         * General Declaration
         */
         Class<CENTData> ENTModel1;
         Field Field1 = null;
         
        try
        {
            ENTModel1 = CENTData.class;
            Field1 = ENTModel1.getField("double_" + intId);                        
            Field1.set(this, dblValue);
        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }
    }    
    
    public void SetDouble(String strFieldName, double dblValue) throws Exception
    {                
        /*
         * General Declaration
         */
         Class<CENTData> ENTModel1;
         Field Field1 = null;
         
        try
        {
            ENTModel1 = CENTData.class;
            Field1 = ENTModel1.getField(strFieldName);                        
            Field1.set(this, dblValue);
        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }
    }                    
    
    public double GetDouble(int intId) throws Exception
    {
        /*
         * General Declaration
         */
         Object Object1 = null;
         Class<CENTData> ENTModel1;
         Field Field1 = null;                  
                 
        try
        {
            ENTModel1 = CENTData.class;
            
            Field1 = ENTModel1.getField("double_" + intId);
            
            Field1.setAccessible(true);
                                    
            Object1 = Field1.get(this);
            
            if (Object1 != null)
                return (double) Object1;
            else
                return 0;             

        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }               
    }
    
    public double GetDouble(String strFieldName) throws Exception
    {
        /*
         * General Declaration
         */
         Object Object1 = null;
         Class<CENTData> ENTModel1;
         Field Field1 = null;                  
                 
        try
        {
            ENTModel1 = CENTData.class;
            
            Field1 = ENTModel1.getField(strFieldName);
            
            Field1.setAccessible(true);
                                    
            Object1 = Field1.get(this);
            
            if (Object1 != null)
                return (double) Object1;
            else
                return 0;             

        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }               
    }           
        
    /*
     * Getters and Setters (Boolean)
     */        
    public void SetBoolean(int intId, int intValue) throws Exception
    {                
        /*
         * General Declaration
         */
         Class<CENTData> ENTModel1;
         Field Field1 = null;
         
        try
        {
            ENTModel1 = CENTData.class;
            Field1 = ENTModel1.getField("boolean_" + intId);                        
            Field1.set(this, intValue);
        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }
    }    
    
    public void SetBoolean(String strFieldName, int intValue) throws Exception
    {                
        /*
         * General Declaration
         */
         Class<CENTData> ENTModel1;
         Field Field1 = null;
         
        try
        {
            ENTModel1 = CENTData.class;
            Field1 = ENTModel1.getField(strFieldName);                        
            Field1.set(this, intValue);
        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }
    }        

    public int GetBoolean(int intId) throws Exception
    {
        /*
         * General Declaration
         */
         Object Object1 = null;
         Class<CENTData> ENTModel1;
         Field Field1 = null;                  
                 
        try
        {
            ENTModel1 = CENTData.class;
            
            Field1 = ENTModel1.getField("boolean_" + intId);
            
            Field1.setAccessible(true);
                                    
            Object1 = Field1.get(this);
            
            
            /*
             * 0-Null, 1-True, 2-False
             */
            return (Integer) Object1;

        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }               
    }  
    
    public int GetBoolean(String strFieldName) throws Exception
    {
        /*
         * General Declaration
         */
         Object Object1 = null;
         Class<CENTData> ENTModel1;
         Field Field1 = null;                  
                 
        try
        {
            ENTModel1 = CENTData.class;
            
            Field1 = ENTModel1.getField(strFieldName);
            
            Field1.setAccessible(true);
                                    
            Object1 = Field1.get(this);

            /*
             * 0-Null, 1-True, 2-False
             */
            return (Integer) Object1;

        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }               
    }      
          
    /*
     * Dynamic fields used for all data, lets start with 20 fields of each type
     */
    public int int_1;
    public int int_2;
    public int int_3;
    public int int_4;
    public int int_5;
    public int int_6;
    public int int_7;
    public int int_8;
    public int int_9;
    public int int_10;
    public int int_11;
    public int int_12;
    public int int_13;
    public int int_14;
    public int int_15;
    public int int_16;
    public int int_17;
    public int int_18;
    public int int_19;
    public int int_20;
    
    public String text_1;
    public String text_2;
    public String text_3;
    public String text_4;
    public String text_5;
    public String text_6;
    public String text_7;
    public String text_8;
    public String text_9;
    public String text_10;
    public String text_11;
    public String text_12;
    public String text_13;
    public String text_14;
    public String text_15;
    public String text_16;
    public String text_17;
    public String text_18;
    public String text_19;
    public String text_20;
    public String text_21;
    public String text_22;
    public String text_23;
    public String text_24;
    public String text_25;
    public String text_26;
    public String text_27;
    public String text_28;
    public String text_29;
    public String text_30;
    
    

    public Date date_1;
    public Date date_2;
    public Date date_3;
    public Date date_4;
    public Date date_5;
    public Date date_6;
    public Date date_7;
    public Date date_8;
    public Date date_9;
    public Date date_10;
    public Date date_11;
    public Date date_12;
    public Date date_13;
    public Date date_14;
    public Date date_15;
    public Date date_16;
    public Date date_17;
    public Date date_18;
    public Date date_19;
    public Date date_20;       
        
    public double double_1;
    public double double_2;
    public double double_3;
    public double double_4;
    public double double_5;
    public double double_6;
    public double double_7;
    public double double_8;
    public double double_9;
    public double double_10;
    public double double_11;
    public double double_12;
    public double double_13;
    public double double_14;
    public double double_15;
    public double double_16;
    public double double_17;
    public double double_18;
    public double double_19;
    public double double_20;
    
    public int boolean_1 = 0;
    public int boolean_2 = 0;
    public int boolean_3 = 0;
    public int boolean_4 = 0;
    public int boolean_5 = 0;
    public int boolean_6 = 0;
    public int boolean_7 = 0;
    public int boolean_8 = 0;
    public int boolean_9 = 0;
    public int boolean_10 = 0;
    public int boolean_11 = 0;
    public int boolean_12 = 0;
    public int boolean_13 = 0;
    public int boolean_14 = 0;
    public int boolean_15 = 0;
    public int boolean_16 = 0;
    public int boolean_17 = 0;
    public int boolean_18 = 0;
    public int boolean_19 = 0;
    public int boolean_20 = 0;
        
    /*
     * Clone the class instance
     */        
    public CENTData Clone() 
    {
        CENTData CENTData1 = null;
        
        try 
        {
            CENTData1 = (CENTData) super.clone();
        }
        catch (CloneNotSupportedException e) 
        {
            // This should never happen
        }
        
        return CENTData1;
    }    
    
    /*
     * System fields - Reserved to system process
     */    
    public int _int_1 = 0;  // SYSTEM_FIELD_ID_COMPANY
    public int _int_2 = 0;  // SYSTEM_FIELD_ID_AREA
    public int _int_3 = 0;  // SYSTEM_FIELD_ID_PROFILE
    public int _int_4 = 0;  // SYSTEM_FIELD_ID_USER
    public int _int_5 = 0;  // SYSTEM_FIELD_ID_TRANSACTION
    public int _int_6 = 0;  // SYSTEM_FIELD_ID_VIEW
    public int _int_7 = 0;  // SYSTEM_FIELD_ID_LAYOUT
    public int _int_8 = 0;  // SYSTEM_FIELD_ID_RECONCILE
    public int _int_9 = 0;  // SYSTEM_FIELD_ID_RECONCILE_STEP
    public int _int_10 = 0; // SYSTEM_FIELD_ID_RECONCILE_RULE
    public int _int_11 = 0; // SYSTEM_FIELD_ID_RECONCILE_FIELD        
    public int _int_12 = 0; // SYSTEM_FIELD_ID_MATCH
    public int _int_13 = 0; // SYSTEM_FIELD_ID_MATCH_STATUS
    public int _int_14 = 0; // SYSTEM_FIELD_ID_MATCH_SIDE
    public int _int_15 = 0; // SYSTEM_FIELD_PAGING_START
    public int _int_16 = 0; // SYSTEM_FIELD_PAGING_END
    public int _int_17 = 0; // 
    public int _int_18 = 0; // 
    public int _int_19 = 0; // 
    public int _int_20 = 0; // 
    
    // Text    
    public String _text_1 = null; // SYSTEM_FIELD_RECONCILIATION_CODE
    public String _text_2 = null; // not used yet
    public String _text_3 = null; // not used yet
    public String _text_4 = null; // not used yet
    public String _text_5 = null; // not used yet
    public String _text_6 = null; // not used yet
    public String _text_7 = null; // not used yet
    public String _text_8 = null; // not used yet
    public String _text_9 = null; // not used yet
    public String _text_10 = null; // not used yet
    public String _text_11 = null; // not used yet
    public String _text_12 = null; // not used yet
    public String _text_13 = null; // not used yet
    public String _text_14 = null; // not used yet
    public String _text_15 = null; // not used yet
    public String _text_16 = null; // not used yet
    public String _text_17 = null; // not used yet
    public String _text_18 = null; // not used yet
    public String _text_19 = null; // not used yet
    public String _text_20 = null; // not used yet

    public Date _date_1 = null; // SYSTEM_FIELD_SYSTEM_DATE
    public Date _date_2 = null; // not used yet
    public Date _date_3 = null; // not used yet
    public Date _date_4 = null; // not used yet
    public Date _date_5 = null; // not used yet
    public Date _date_6 = null; // not used yet
    public Date _date_7 = null; // not used yet
    public Date _date_8 = null; // not used yet
    public Date _date_9 = null; // not used yet
    public Date _date_11 = null; // not used yet
    public Date _date_12 = null; // not used yet
    public Date _date_13 = null; // not used yet
    public Date _date_14 = null; // not used yet
    public Date _date_15 = null; // not used yet
    public Date _date_16 = null; // not used yet
    public Date _date_17 = null; // not used yet
    public Date _date_18 = null; // not used yet
    public Date _date_19 = null; // not used yet
    public Date _date_20 = null; // not used yet
        
    public double _double_1 = 0; // not used yet
    public double _double_2 = 0; // not used yet
    public double _double_3 = 0; // not used yet
    public double _double_4 = 0; // not used yet
    public double _double_5 = 0; // not used yet
    public double _double_6 = 0; // not used yet
    public double _double_7 = 0; // not used yet
    public double _double_8 = 0; // not used yet
    public double _double_9 = 0; // not used yet
    public double _double_10 = 0; // not used yet
    public double _double_11 = 0; // not used yet
    public double _double_12 = 0; // not used yet
    public double _double_13 = 0; // not used yet
    public double _double_14 = 0; // not used yet
    public double _double_15 = 0; // not used yet
    public double _double_16 = 0; // not used yet
    public double _double_17 = 0; // not used yet
    public double _double_18 = 0; // not used yet
    public double _double_19 = 0; // not used yet
    public double _double_20 = 0; // not used yet
    
    public int _boolean_1 = Null; // not used yet
    public int _boolean_2 = Null; // not used yet
    public int _boolean_3 = Null; // not used yet
    public int _boolean_4 = Null; // not used yet
    public int _boolean_5 = Null; // not used yet
    public int _boolean_6 = Null; // not used yet
    public int _boolean_7 = Null; // not used yet
    public int _boolean_8 = Null; // not used yet
    public int _boolean_9 = Null; // not used yet
    public int _boolean_10 = Null; // not used yet
    public int _boolean_11 = Null; // not used yet
    public int _boolean_12 = Null; // not used yet
    public int _boolean_13 = Null; // not used yet
    public int _boolean_14 = Null; // not used yet
    public int _boolean_15 = Null; // not used yet
    public int _boolean_16 = Null; // not used yet
    public int _boolean_17 = Null; // not used yet
    public int _boolean_18 = Null; // not used yet
    public int _boolean_19 = Null; // not used yet
    public int _boolean_20 = Null; // not used yet
}
