/*
 * Developed by David Lancioni - 07/2015
 * Dynamic solution to traffic data from presentation layer until persistence layer
 * The constructor is mandatory due to logging and formatting solution
 */
package entity;

import java.lang.reflect.Field;
import java.util.ArrayList;
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

    private ArrayList<CENTField> ListOfField = new ArrayList<CENTField>();    
    
    /*
     * Used to clear the class instance
     */             
    public void Clear() throws Exception
    { 
        /*       
        this.SetInt(SYSTEM_FIELD_PAGING_START, Integer.MIN_VALUE);
        this.SetInt(SYSTEM_FIELD_PAGING_END, Integer.MIN_VALUE);     
        */
        this.SetListOfField(new ArrayList<CENTField>());
    }    

    /*
     * Getter and Setter for INT
     */     
    public void SetInt(int intId, int intValue) throws Exception
    {                
        /*
         * General Declaration
         */
         CENTField CENTField1 = new CENTField();
         
        try
        {            
            CENTField1.setFieldName("int_" + intId);
            CENTField1.setFieldType(TYPE_INT);
            CENTField1.setFieldValueInt(intValue);            
            this.GetListOfField().add(CENTField1);
        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }
        finally
        {
            CENTField1 = null;
        }
    }
    
    public void SetInt(String strFieldName, int intValue) throws Exception
    {                
        /*
         * General Declaration
         */
         CENTField CENTField1 = new CENTField();         
         
        try
        {
            CENTField1.setFieldName(strFieldName);
            CENTField1.setFieldType(TYPE_INT);
            CENTField1.setFieldValueInt(intValue);            
            this.GetListOfField().add(CENTField1);
        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }
        finally
        {
            CENTField1 = null;
        }        
    }    

    public int GetInt(int intId) throws Exception
    {
        /*
         * General Declaration
         */
        int value = 0;

        try
        {
            for (CENTField field : this.GetListOfField())
            {
                if (field.getFieldName().equals("int_" + intId))
                {
                    value = field.getFieldValueInt();
                    break;
                }
            }
        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }               
        
        return value;
    }  

    public int GetInt(String strFieldName) throws Exception
    {
        /*
         * General Declaration
         */
        int value = 0;

        try
        {
            for (CENTField field : this.GetListOfField())
            {
                if (field.getFieldName().equals(strFieldName.trim().toLowerCase()))
                {
                    value = field.getFieldValueInt();
                    break;
                }
            }
        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }

        return value;
    }
    
    /*
     * Getter and Setter for TEXT
     */
    public void SetText(int intId, String strValue) throws Exception
    {                
        /*
         * General Declaration
         */
         CENTField CENTField1 = new CENTField();                 
         
        try
        {
            CENTField1.setFieldName("text_" + intId);
            CENTField1.setFieldType(TYPE_TEXT);
            CENTField1.setFieldValueText(strValue);            
            this.GetListOfField().add(CENTField1);               
        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }
        finally
        {
            CENTField1 = null;
        }
    } 
    
    public void SetText(String strFieldName, String strValue) throws Exception
    {                
        /*
         * General Declaration
         */
         CENTField CENTField1 = new CENTField();
         
        try
        {           
            CENTField1.setFieldName(strFieldName);
            CENTField1.setFieldType(TYPE_TEXT);
            CENTField1.setFieldValueText(strValue);            
            this.GetListOfField().add(CENTField1);     
        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }
        finally
        {
            CENTField1 = null;
        }
    }         
        
    public String GetText(int intId) throws Exception
    {
        /*
         * General Declaration
         */
        String value = "";

        try
        {
            for (CENTField field : this.GetListOfField())
            {
                if (field.getFieldName().equals("text_" + intId))
                {
                    value = field.getFieldValueText();
                    break;
                }
            }
        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }               
        
        return value;
    }   
    
    public String GetText(String strFieldName) throws Exception
    {
        /*
         * General Declaration
         */
        String value = "";

        try
        {
            for (CENTField field : this.GetListOfField())
            {
                if (field.getFieldName().equals(strFieldName.trim().toLowerCase()))
                {
                    value = field.getFieldValueText();
                    break;
                }
            }
        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }               
        
        return value;
    }       
        
    
    /*
     * Getter and Setter for DATE
     */    
    public void SetDate(int intId, Date datValue) throws Exception
    {                
        /*
         * General Declaration
         */
         CENTField CENTField1 = new CENTField();                 
         
        try
        {
            CENTField1.setFieldName("date_" + intId);
            CENTField1.setFieldType(TYPE_DATE);
            CENTField1.setFieldValueDate(datValue);
            this.GetListOfField().add(CENTField1);               
        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }
        finally
        {
            CENTField1 = null;
        }
    }      
    
    public void SetDate(String strFieldName, Date datValue) throws Exception
    {                
        /*
         * General Declaration
         */
         CENTField CENTField1 = new CENTField();
         
        try
        {           
            CENTField1.setFieldName(strFieldName);
            CENTField1.setFieldType(TYPE_DATE);
            CENTField1.setFieldValueDate(datValue);            
            this.GetListOfField().add(CENTField1);     
        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }
        finally
        {
            CENTField1 = null;
        }
    }           
    
    public Date GetDate(int intId) throws Exception
    {
        /*
         * General Declaration
         */
        Date value = null;

        try
        {
            for (CENTField field : this.GetListOfField())
            {
                if (field.getFieldName().equals("date_" + intId))
                {
                    value = field.getFieldValueDate();
                    break;
                }
            }
        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }               
        
        return value;
    }
    
    public Date GetDate(String strFieldName) throws Exception
    {
        /*
         * General Declaration
         */
        Date value = null;

        try
        {
            for (CENTField field : this.GetListOfField())
            {
                if (field.getFieldName().equals(strFieldName.trim().toLowerCase()))
                {
                    value = field.getFieldValueDate();
                    break;
                }
            }
        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }               
        
        return value;             
    }    
    
    /*
     * Getter and Setter for DOUBLE
     */
    public void SetDouble(int intId, double dblValue) throws Exception
    {                
        /*
         * General Declaration
         */
         CENTField CENTField1 = new CENTField();                 
         
        try
        {
            CENTField1.setFieldName("double_" + intId);
            CENTField1.setFieldType(TYPE_DOUBLE);
            CENTField1.setFieldValueDouble(dblValue);
            this.GetListOfField().add(CENTField1);           
        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }
        finally
        {
            CENTField1 = null;
        }
    }    
    
    public void SetDouble(String strFieldName, double dblValue) throws Exception
    {                
        /*
         * General Declaration
         */
         CENTField CENTField1 = new CENTField();                 
         
        try
        {
            CENTField1.setFieldName(strFieldName);
            CENTField1.setFieldType(TYPE_DOUBLE);
            CENTField1.setFieldValueDouble(dblValue);
            this.GetListOfField().add(CENTField1);           
        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }
        finally
        {
            CENTField1 = null;
        }
    }                    
    
    public double GetDouble(int intId) throws Exception
    {
        /*
         * General Declaration
         */
        double value = 0;

        try
        {
            for (CENTField field : this.GetListOfField())
            {
                if (field.getFieldName().equals("double_" + intId))
                {
                    value = field.getFieldValueDouble();
                    break;
                }
            }
        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }               
        
        return value;
    }
    
    public double GetDouble(String strFieldName) throws Exception
    {
        /*
         * General Declaration
         */
        double value = 0;

        try
        {
            for (CENTField field : this.GetListOfField())
            {
                if (field.getFieldName().equals(strFieldName.trim().toLowerCase()))
                {
                    value = field.getFieldValueDouble();
                    break;
                }
            }
        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }               
        
        return value;
    }           
        
    /*
     * Getter and Setter for INT
     */     
    public void SetBoolean(int intId, int intValue) throws Exception
    {                
        /*
         * General Declaration
         */
         CENTField CENTField1 = new CENTField();
         
        try
        {            
            CENTField1.setFieldName("boolean_" + intId);
            CENTField1.setFieldType(TYPE_BOOLEAN);
            CENTField1.setFieldValueBoolean(intValue);            
            this.GetListOfField().add(CENTField1);
        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }
        finally
        {
            CENTField1 = null;
        }
    }
    
    public void SetBoolean(String strFieldName, int intValue) throws Exception
    {                
        /*
         * General Declaration
         */
         CENTField CENTField1 = new CENTField();         
         
        try
        {
            CENTField1.setFieldName(strFieldName);
            CENTField1.setFieldType(TYPE_BOOLEAN);
            CENTField1.setFieldValueBoolean(intValue);            
            this.GetListOfField().add(CENTField1);
        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }
        finally
        {
            CENTField1 = null;
        }        
    }    

    public int GetBoolean(int intId) throws Exception
    {
        /*
         * General Declaration
         */
        int value = 0;

        try
        {
            for (CENTField field : this.GetListOfField())
            {
                if (field.getFieldName().equals("boolean_" + intId))
                {
                    value = field.getFieldValueBoolean();
                    break;
                }
            }
        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }               
        
        return value;
    }  

    public int GetBoolean(String strFieldName) throws Exception
    {
        /*
         * General Declaration
         */
        int value = 0;

        try
        {
            for (CENTField field : this.GetListOfField())
            {
                if (field.getFieldName().equals(strFieldName.trim().toLowerCase()))
                {
                    value = field.getFieldValueBoolean();
                    break;
                }
            }
        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }               
        
        return value;
    }
        
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

    /**
     * @return the ListOfField
     */
    public ArrayList<CENTField> GetListOfField() {
        return ListOfField;
    }

    /**
     * @param ListOfField the ListOfField to set
     */
    public void SetListOfField(ArrayList<CENTField> ListOfField) {
        this.ListOfField = ListOfField;
    }
}
