package persistence;

import connection.ConnectionFactory;
import entity.CENTData;
import java.util.Date;
import entity.CENTException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CDAL extends project.Global
{   
    public CDAL(Connection Connection1, CENTData CENTSession1) throws SQLException
    {
        this.SetSession(CENTSession1);
        this.SetConnection(Connection1);
    }        

    public CENTData GetSession() 
    {
        return CENTSession1;
    }    
    
    public void SetSession(CENTData CENTSession1) 
    {
        this.CENTSession1 = CENTSession1;
    }
    
    protected Connection GetConnection()
    {
        return Connection1;
    }
    
    protected void SetConnection(Connection Connection1) throws SQLException
    {
        if (this.GetConnection() == null)
        {
            this.Connection1 = Connection1;
        }
        else
        {
            if (this.GetConnection().isClosed())
            {
                this.Connection1 = Connection1;
            }            
        }
    }        

    protected String Temp(String strTable)
    {
        String strCommand = "";
        
        try
        {                
            String strDB = this.GetPropertieValue("CONNECTION_DB_SERVER");

            switch (strDB)
            {
                case SERVER_ORACLE:
                    strCommand = strTable;
                    break;  

                case SERVER_SQL_SERVER:
                    strCommand = "tempdb.." + strTable;
                    break;
                    
                case SERVER_POSTGRE:
                    strCommand = strTable;
                    break;

            }

        }
        catch (Exception Exception1)
        {
            System.out.println("Fail to prepare system field: " + Exception1.getMessage());
        }
        
        /*
         * Return it
         */
        return strCommand;        
    }        
    
    protected String Select()
    {
        String strCommand = "";
        
        try
        {                
            String strDB = this.GetPropertieValue("CONNECTION_DB_SERVER");

            switch (strDB)
            {
                case SERVER_ORACLE:
                    strCommand = "select ";
                    break;  

                case SERVER_SQL_SERVER:
                    strCommand = "select top 2147483647";
                    break;                
                    
                case SERVER_POSTGRE:
                    strCommand = "select ";
                    break;

            }

        }
        catch (Exception Exception1)
        {
            System.out.println("Fail to prepare system field: " + Exception1.getMessage());
        }
        
        /*
         * Return it
         */
        return strCommand;        
    }    
    
    
    protected String RowId()
    {
        String strCommand = "";
        
        try
        {                
            String strDB = this.GetPropertieValue("CONNECTION_DB_SERVER");

            switch (strDB)
            {                    
                case SERVER_ORACLE:
                    strCommand = "row_number() over(order by 1 asc) as row_id";
                    break;

                case SERVER_SQL_SERVER:
                    strCommand = "row_number() over(order by getdate() asc) as row_id";
                    break;

                case SERVER_POSTGRE:
                    strCommand = "row_number() over(order by 1 asc) as row_id";
                    break;

            }

        }
        catch (Exception Exception1)
        {
            System.out.println("Fail to prepare system field: " + Exception1.getMessage());
        }

        /*
         * Return it
         */
        return strCommand;        
    }    
    
    protected String sf(String strFieldName)
    {        
        try
        {                
            String strDB = this.GetPropertieValue("CONNECTION_DB_SERVER");

            switch (strDB)
            {
                case SERVER_ORACLE:
                    strFieldName = "\"" + strFieldName + "\"";                
                    break;  

                case SERVER_SQL_SERVER:
                    // do nothing
                    break;                

                case SERVER_POSTGRE:
                    // do nothing
                    break;                      

            }

        }
        catch (Exception Exception1)
        {
            System.out.println("Fail to prepare system field: " + Exception1.getMessage());
        }
        
        /*
         * Return it
         */
        return strFieldName;        
    }
    
    protected String dbqt(String strFieldName)
    {       
        strFieldName = "\"" + strFieldName + "\"";
                
        return strFieldName;
    }    
        
    /*
     * Prepare the value to query/persist strings
     */
    protected String PrepareString(String strValue)
    {
        /*
         * Remove single quotes
         */
        strValue = strValue.replaceAll("'", "");
        
        /*
         * Remove spaces and return
         */        
        return "'" + strValue.trim() + "'";
    }    

    protected String PrepareNumber(Object strValue)
    {
        return strValue.toString();
    }    

    protected String PrepareDate(Date datDate) throws Exception
    {
        /*
         * General Declaration
         */
        String strDate = "";                
        SimpleDateFormat SimpleDateFormat1 = null;
        
        try
        {            
            /*
             * Create the objects
             */
            SimpleDateFormat1 = new SimpleDateFormat(C_DEFAULT_MASK_TO_PERSIST_DATE);
            strDate = "'" + SimpleDateFormat1.format(datDate) + "'";
            String strDB = this.GetPropertieValue("CONNECTION_DB_SERVER");

            /*
             * Handle specific databases specification
             */            
            switch (strDB)
            {
                case SERVER_ORACLE:
                    strDate = "to_date(" + strDate + ", " + "'" + C_DEFAULT_MASK_TO_PERSIST_DATE + "')";
                    break;  

                case SERVER_SQL_SERVER:
                    // do nothing
                    break;                
                    
                case SERVER_POSTGRE:
                    strDate = "cast(" + strDate + " as date)";
                    break;         

            }            

            return strDate;
        }
        catch (Exception  Exception1)
        {
            throw Exception1;
        }
        finally
        {
            strDate = "";                
            SimpleDateFormat1 = null;
        }
    }    

    protected String PrepareDate(String strDate)
    {
        /*
         * General declaration
         */
        ConnectionFactory ConnectionFactory1 = new ConnectionFactory();
        
        try
        {            
            
            strDate = "'" + strDate + "'";
            
            switch (ConnectionFactory1.GetCurrentSGDB())
            {
                case SERVER_ORACLE:
                    strDate = "to_date(" + strDate + ", " + "'" + "dd/MM/yyyy" + "')";
                    break;

                case SERVER_SQL_SERVER:
                    // do nothing
                    break;
                    
                case SERVER_POSTGRE:                            
                    // do nothing
                    break;         

            }

        }
        catch (Exception  Exception1)
        {
            System.out.println(Exception1.getMessage());
        }
        finally
        {
            ConnectionFactory1 = null;
        }
        
        return strDate;        
    }        
        
    protected String PrepareStringToQueryLike(String strValue)
    {
        return "'%" + strValue.trim() + "%'";
    }
            
    protected java.sql.Date PrepareDateToPersist(Date date)
    {
        if (date != null)
        {    
            return new java.sql.Date(date.getTime());  
        }    
        else
        {    
            return null;
        }        
    }    

    protected Date DateToDate(ResultSet ResultSet1, String strFieldObject) throws SQLException, CENTException, Exception
    {
        /*
         * General Declaration
         */
        String strDate = "";
        Date datDate1 = null;
        String strDB = "";    
        strDB = this.GetPropertieValue("CONNECTION_DB_SERVER");

        /*
         * Handle null dates
         */        
        if (ResultSet1.getString(strFieldObject) == null)
        {
            return StringToDate("1900/01/01", "yyyy/mm/dd");
        }

        /*
         * SQLite doesn't have a date time, must get as string and parse to date
         */
        switch(strDB)
        {                        
            default:                            
                datDate1 = ResultSet1.getDate(strFieldObject);
                break;
        }

        /*
         * Return it
         */        
        return datDate1;
    }        
    
    /*
     * Specific Functions
     */    

    public String GetTable(int intIdTransaction) throws CENTException, SQLException
    {
        /*
         * General Declaration
         */
        String strSql = "";
        String strTable = "";
        ResultSet ResultSet1 = null;
        Statement Statement1 = null;

        try
        {

            /*
             * Setup purpose
             */            
            strTable = "system_" + intIdTransaction;            
            
            /*
             * Get a new connection
             */
            Statement1 = Connection1.createStatement();
            
            /*
             * Execute the statement
             */            
            strSql = "";            
            strSql += " select " + lb;
            strSql += " tb_1.int_1," + lb;   // id
            strSql += " tb_1.int_3," + lb;   // id_table    
            strSql += " tb_1.int_5," + lb;   // id_type    
            strSql += " tb_2.text_1" + lb;  // table_name
            
            strSql += " from " + (PREFIX_TABLE_SYSTEM + TRN_TRANSACTION) + " tb_1" + lb;
            
            strSql += " inner join " + (PREFIX_TABLE_SYSTEM + TRN_TABLE) + " tb_2 on" + lb;
            strSql += " tb_1.int_3 = tb_2." + sf(SYSTEM_FIELD_ID) + lb;                        
            
            strSql += " where tb_1." + sf(SYSTEM_FIELD_ID_COMPANY) + " = " + this.GetSession().GetInt(SESSION_COMPANY) + lb;
            strSql += " and tb_1." + sf(SYSTEM_FIELD_ID) + " = " + intIdTransaction;

            ResultSet1 = Statement1.executeQuery(strSql);            
            
            while (ResultSet1.next())
            {
                strTable = ResultSet1.getString(4);
            }
            
            /*
             * Close sql related objects
             */
            if (Statement1 != null)
                Statement1.close();

            if (ResultSet1 != null)
                ResultSet1.close();                                                
            
            /*
             * Return the list
             */
            return strTable;
            
        } 
        catch (Exception Exception1)
        {
            throw new CENTException("EXCEPTION_FAIL_GET_TABLE_NAME", Exception1.getMessage());
        } 
        finally
        {
            /*
             * Destroy the objects (Oracle cursor issue)
             */
            Statement1 = null;
            ResultSet1 = null;
            strSql = null;
            strTable = null;            
        }
    }    
    
    public String Translate(String strLabel) throws CENTException, SQLException
    {
        /*
         * General Declaration
         */
        int intIdLanguage = 0;
        String strSql = "";
        String strLanguage = "";
        
        ResultSet ResultSet1 = null;
        Statement Statement1 = null;

        try
        {
            /*
             * Define the language
             */            
            strLanguage = this.GetSession().GetText(SESSION_LANGUAGE);
            
            if (strLanguage.equals(LANGUAGE_PORTUGUESE))
            {
                intIdLanguage = 1;
            }

            if (strLanguage.equals(LANGUAGE_ENGLISH))
            {
                intIdLanguage = 2;
            }

            if (strLanguage.equals(LANGUAGE_SPANISH))
            {
                intIdLanguage = 3;
            }
            
            /*
             * Return the fields name to query the module catalog and data
             */
            strSql = "";
            strSql += " select";
            
            strSql += " text_2";
            
            strSql += " from "  + this.GetTable(TRN_DICTIONARY);
            
            if (this.GetSession().GetInt(SESSION_COMPANY) == 0)
                strSql += " where " + sf(SYSTEM_FIELD_ID_COMPANY) + " = " + "1";            
            else
                strSql += " where " + sf(SYSTEM_FIELD_ID_COMPANY) + " = " + this.GetSession().GetInt(SESSION_COMPANY);
            
            strSql += " and text_1 = " + "'" + strLabel + "'";
            
            strSql += " and int_2 = " + intIdLanguage;

            /*
             * Prepare the statement
             */
            Statement1 = Connection1.createStatement();
            ResultSet1 = Statement1.executeQuery(strSql);

            /*
             * Fill the List<CENTData>
             */
            while (ResultSet1.next())
            {
                strLabel = ResultSet1.getString(1);
            }            

            /*
             * Close sql related objects
             */
            if (Statement1 != null)
                Statement1.close();

            if (ResultSet1 != null)
                ResultSet1.close();                        
            
            /*
             * Return the list
             */
            return strLabel;
        } 
        catch (CENTException CENTException1)
        {
            throw CENTException1;
        }        
        catch (Exception Exception1)
        {
            throw new CENTException("EXCEPTION_FAIL_TO_GETLIST", Exception1.getMessage());
        } 
        finally
        {            
            ResultSet1 = null;            
            Statement1 = null;            
            strSql = null;
            strLanguage = null;
        }
    }    
    
    /*
     * Generate the next Id for each table, same effect of identity field
     */    
    protected int GetLastId(int intIdTransaction)  throws CENTException, SQLException
    {
        int intId = 0;
        
        String strSql = "";
        String strTable = "";
        
        ResultSet ResultSet1 = null;
        Statement Statement1 = null;

        try
        {
            /*
             * Get the table responsible for sequence control
             */            
            strTable = GetTable(intIdTransaction);

            /*
             * Prepare the SQL Statement
             */
            strSql = "";
            strSql += " select max(" + sf(SYSTEM_FIELD_ID) + ") " + dbqt("next_id") + " from " + strTable;
            strSql += " where " + sf(SYSTEM_FIELD_ID_COMPANY) + " = " + this.PrepareNumber(this.GetSession().GetInt(SESSION_COMPANY));

            Statement1 = Connection1.createStatement();
            
            ResultSet1 = Statement1.executeQuery(strSql);
            
            while (ResultSet1.next())
            {            
                intId = ResultSet1.getInt(1);
            }

            /*
             * Close sql related objects
             */
            if (Statement1 != null)
                Statement1.close();

            if (ResultSet1 != null)
                ResultSet1.close();                        
            
            /*
             * Return the Id
             */    
            return intId;
            
        } 
        catch (SQLException SQLException1)
        {
            throw new CENTException("EXCEPTION_FAIL_GET_NEXT_ID", SQLException1.getMessage());
        }
        catch (Exception Exception1)
        {
            throw new CENTException("EXCEPTION_FAIL_GET_NEXT_ID", Exception1.getMessage());
        } 
        finally
        {
            Statement1 = null;            
            ResultSet1 = null;                        
            strSql = null;
            strTable = null;            
        }
    }    

    
    /*
     * Generate the next Id for each table, same effect of identity field
     */    
    protected int GetSystemView(int intIdTransaction)  throws CENTException, SQLException
    {
        int intId = 0;
        
        String strSql = "";
        String strTable = "";
        List<CENTData> ListCENTCatalog1 = null;
        ResultSet ResultSet1 = null;
        Statement Statement1 = null;

        try
        {
            /*
             * Get the table responsible for sequence control
             */            
            strTable = GetTable(TRN_VIEW);

            /*
             * Keep view's catalog
             */              
            ListCENTCatalog1 = this.GetCatalog(TRN_VIEW);

            /*
             * Prepare the SQL Statement
             */
            strSql = "";
            strSql += " select " + sf(SYSTEM_FIELD_ID) + " from " + strTable;
            strSql += " where " + sf(SYSTEM_FIELD_ID_COMPANY) + " = " + this.PrepareNumber(this.GetSession().GetInt(SESSION_COMPANY));
            strSql += " and " + GetFieldObject("id_transaction", ListCENTCatalog1) + " = " + this.PrepareNumber(intIdTransaction);            
            Statement1 = Connection1.createStatement();
            
            ResultSet1 = Statement1.executeQuery(strSql);
            
            while (ResultSet1.next())
            {            
                intId = ResultSet1.getInt(1);
                break;
            }
            
            if (intId == 0)
            {
                intId = intIdTransaction;
            }

            /*
             * Close sql related objects
             */
            if (Statement1 != null)
                Statement1.close();

            if (ResultSet1 != null)
                ResultSet1.close();                        
            
            /*
             * Return the Id
             */    
            return intId;
            
        } 
        catch (SQLException SQLException1)
        {
            throw new CENTException("EXCEPTION_FAIL_GET_NEXT_ID", SQLException1.getMessage());
        }
        catch (Exception Exception1)
        {
            throw new CENTException("EXCEPTION_FAIL_GET_NEXT_ID", Exception1.getMessage());
        } 
        finally
        {
            Statement1 = null;            
            ResultSet1 = null;                        
            strSql = null;
            strTable = null;            
        }
    }    
    
    
    /*
     * Catalog Methods
     */               
    public List<CENTData> GetView(int intIdView) throws CENTException, SQLException
    {
        /*
         * General Declaration
         */
        int intTransactionView = 0;
        String strLabel = "";
        String strSql = "";
        
        CENTData CENTView1 = null;
        CENTData CENTFilter1 = null;
        List<CENTData> ListCENTView1 = null;
        List<CENTData> ListCENTCatalog1 = null;
        List<CENTData> ListCENTData1 = null;
        CDALCore CDALCore1 = null;
        
        ResultSet ResultSet1 = null;
        Statement Statement1 = null;

        try
        {

            intTransactionView = this.GetSystemView(this.GetIdTransaction());
            
            /*
             * Create the Objects
             */            
            ListCENTView1 = new ArrayList<CENTData>();
                        
            /*
             * Return the fields name to query the module catalog and data
             */            
            strSql = "  select" + lb; 
            
            strSql += "  tb_4." + sf(SYSTEM_FIELD_ID) + " " + dbqt("id_field") + "," + lb;
            strSql += "  tb_4.int_2      " + dbqt("id_transaction") + "," + lb;
            strSql += "  tb_4.text_1     " + dbqt("field_label") + "," + lb;
            strSql += "  tb_4.text_2     " + dbqt("field_name") + "," + lb;
            strSql += "  tb_4.text_3     " + dbqt("field_object") + "," + lb;
            strSql += "  tb_4.int_3      " + dbqt("field_type") + "," + lb;
            strSql += "  tb_4.int_4      " + dbqt("field_size") + "," + lb;
            strSql += "  tb_4.boolean_1  " + dbqt("id_nullable") + "," + lb;
            strSql += "  tb_4.boolean_2  " + dbqt("id_unique") + "," + lb;
            strSql += "  tb_4.boolean_3  " + dbqt("id_pk") + "," + lb;
            strSql += "  tb_4.int_5      " + dbqt("id_fk") + "," + lb;
            strSql += "  tb_4.text_4     " + dbqt("field_domain") + "," + lb;
            strSql += "  tb_4.text_5     " + dbqt("field_note") + "," + lb;
            strSql += "  tb_7.int_3      " + dbqt("id_command") + "," + lb;
            strSql += "  tb_7.int_6      " + dbqt("id_condition") + "," + lb;
            strSql += "  tb_7.text_1     " + dbqt("condition_value") + "," + lb;
            strSql += "  tb_6.text_2     " + dbqt("procedure") + "" + lb;
            
            strSql += "  from " + this.GetTable(TRN_VIEW_DEF) + " tb_7" + lb;
            
            strSql += "  inner join " + this.GetTable(TRN_CATALOG) + " tb_4 on tb_4." + sf(SYSTEM_FIELD_ID) + " = tb_7.int_5      and tb_4." + sf(SYSTEM_FIELD_ID_COMPANY) + " = tb_7." + sf(SYSTEM_FIELD_ID_COMPANY) + lb;
            strSql += "  inner join " + this.GetTable(TRN_VIEW) + " tb_6 on tb_7.int_2 = tb_6." + sf(SYSTEM_FIELD_ID) + "      and tb_6." + sf(SYSTEM_FIELD_ID_COMPANY) + " = tb_7." + sf(SYSTEM_FIELD_ID_COMPANY) + lb;
            strSql += "  left join " + this.GetTable(TRN_CATALOG) + " tb_41 on tb_41." + sf(SYSTEM_FIELD_ID) + " = tb_7.int_8     and tb_41." + sf(SYSTEM_FIELD_ID_COMPANY) + " = tb_7." + sf(SYSTEM_FIELD_ID_COMPANY) + lb;
            
            strSql += "  where tb_7." + sf(SYSTEM_FIELD_ID_COMPANY) + " = " + this.GetSession().GetInt(SESSION_COMPANY) + lb;
            strSql += "  and tb_6." + sf(SYSTEM_FIELD_ID) + " = " + intIdView + lb;

            //strSql += "  order by tb_7." + sf(SYSTEM_FIELD_ID) + lb;
            strSql += "  order by tb_7.int_7 asc" + lb; // Field position
            
            /*
             * Prepare the statement
             */
            Statement1 = Connection1.createStatement();
            ResultSet1 = Statement1.executeQuery(strSql);

            /*
             * Get the view dictionary
             */
            while (ResultSet1.next())
            {
                /*
                 * Create the object to handle view fields
                 */                
                CENTView1 = new CENTData();                
                
                /*
                 * Integer Fields
                 */
                CENTView1.SetInt(FIELD_ID, ResultSet1.getInt("id_field"));
                CENTView1.SetInt(FIELD_ID_TRN, ResultSet1.getInt("id_transaction"));                
                CENTView1.SetInt(FIELD_SIZE, ResultSet1.getInt("field_size"));
                CENTView1.SetInt(FIELD_ID_FK, ResultSet1.getInt("id_fk"));      
                CENTView1.SetInt(FIELD_ID_COMMAND, ResultSet1.getInt("id_command"));
                CENTView1.SetInt(FIELD_ID_OPERATOR, ResultSet1.getInt("id_condition"));                
                
                /*
                 * Boolean Fields
                 */                
                CENTView1.SetBoolean(FIELD_NULLABLE, ResultSet1.getInt("id_nullable"));
                CENTView1.SetBoolean(FIELD_ID_UNIQUE, ResultSet1.getInt("id_unique"));                
                CENTView1.SetBoolean(FIELD_ID_PK, ResultSet1.getInt("id_pk"));
                
                /*
                 * Text Fields
                 */
                if (ResultSet1.getString("field_label") != null)
                {                
                    strLabel = ResultSet1.getString("field_label");
                    
                    switch (CENTView1.GetInt(FIELD_ID_COMMAND))
                    {
                        case COMMAND_SELECT_COUNT:                            
                            strLabel = Translate("Label.select_count") + "(" + Translate(strLabel) + ")";
                            break;
                            
                        case COMMAND_SELECT_SUM:                            
                            strLabel = Translate("Label.select_sum") + "(" + Translate(strLabel) + ")";
                            break;
                            
                        case COMMAND_SELECT_MAX:
                            strLabel = Translate("Label.select_max") + "(" + Translate(strLabel) + ")";
                            break;
                            
                        case COMMAND_SELECT_MIN:
                            strLabel = Translate("Label.select_min") + "(" + Translate(strLabel) + ")";
                            break;
                            
                        case COMMAND_SELECT_AVG:
                            strLabel = Translate("Label.select_avg") + "(" + Translate(strLabel) + ")";
                            break;
                    }

                    CENTView1.SetText(FIELD_LABEL, strLabel);        // Label from catalog
                }
                
                if (ResultSet1.getString("field_name") != null)
                {
                    CENTView1.SetText(FIELD_NAME, ResultSet1.getString("field_name"));
                }
                
                if (ResultSet1.getString("field_object") != null)
                {
                    CENTView1.SetText(FIELD_OBJECT, ResultSet1.getString("field_object"));
                }
                
                if (ResultSet1.getInt("field_type") != 0)
                {
                    CENTView1.SetInt(FIELD_TYPE, ResultSet1.getInt("field_type"));
                }
                
                if (ResultSet1.getString("field_domain") != null)
                {                    
                    CENTView1.SetText(FIELD_DOMAIN_NAME, ResultSet1.getString("field_domain"));                
                }

                if (ResultSet1.getString("field_note") != null)
                {                    
                    CENTView1.SetText(FIELD_NOTE, ResultSet1.getString("field_note"));
                }
                
                if (ResultSet1.getString("condition_value") != null)
                {    
                    CENTView1.SetText(FIELD_CONDITION_VALUE, ResultSet1.getString("condition_value"));
                }
                
                if (ResultSet1.getString("procedure") != null)
                {    
                    CENTView1.SetText(FIELD_CUSTOM_QUERY, ResultSet1.getString("procedure"));
                }
                
                ListCENTView1.add(CENTView1);                
            }        
                
            /*
             * Close sql related objects
             */
            if (Statement1 != null)
                Statement1.close();

            if (ResultSet1 != null)
                ResultSet1.close();                        
            
            /*
             * Return the list
             */
            return ListCENTView1;

        } 
        catch (SQLException SQLException1)
        {
            throw new CENTException("EXCEPTION_FAIL_TO_PREPARE_VIEW", SQLException1.getMessage());
        }         
        catch (Exception Exception1)
        {
            throw new CENTException("EXCEPTION_FAIL_TO_PREPARE_VIEW", Exception1.getMessage());
        } 
        finally
        {
            ResultSet1 = null;
            Statement1 = null;
            strSql = null;            
            ListCENTView1 = null;
            CENTView1 = null;
            ListCENTView1 = null;
        }
    }
    
    public List<CENTData> GetCatalog(int intIdTransaction) throws CENTException, SQLException
    {
        /*
         * General Declaration
         */       
        String strSql = "";
        
        CENTData CENTCatalog1 = null;
        ResultSet ResultSet1 = null;
        Statement Statement1 = null;
        
        List<CENTData> ListCENTCatalog1 = null;        

        try
        {            
            /*
             * Create the Objects
             */            
            ListCENTCatalog1 = new ArrayList<CENTData>();
            
            /*
             * Select the catalog
             */            
            strSql += " select " + lb; 
            
            strSql += " tb_4." + sf(SYSTEM_FIELD_ID) + " " + dbqt("id_field") + "," + lb;
            strSql += " tb_4.int_2      " + dbqt("id_transaction") + "," + lb;
            strSql += " tb_4.text_1     " + dbqt("field_label") + "," + lb;
            strSql += " tb_4.text_2     " + dbqt("field_name") + "," + lb;
            strSql += " tb_4.text_3     " + dbqt("field_object") + "," + lb;
            strSql += " tb_4.int_3      " + dbqt("field_type") + "," + lb;
            strSql += " tb_4.int_4      " + dbqt("field_size") + "," + lb;
            strSql += " tb_4.boolean_1  " + dbqt("id_nullable") + "," + lb;
            strSql += " tb_4.boolean_2  " + dbqt("id_unique") + "," + lb;
            strSql += " tb_4.boolean_3  " + dbqt("id_pk") + "," + lb;
            strSql += " tb_4.int_5      " + dbqt("id_fk") + "," + lb;
            strSql += " tb_4.text_4     " + dbqt("field_domain") + "," + lb;
            strSql += " tb_4.int_6      " + dbqt("id_position") + "," + lb;
            strSql += " tb_4.boolean_4  " + dbqt("id_disabled") + "," + lb;
            strSql += " tb_4.text_5     " + dbqt("field_note") + "," + lb;
            strSql += " tb_1.int_8      " + dbqt("id_table") + "," + lb;
            strSql += " tb_1.text_1     " + dbqt("table_name") + "" + lb;

            strSql += " from " + this.GetTable(TRN_CATALOG) + " tb_4" + lb;
            
            strSql += " inner join " + this.GetTable(TRN_TRANSACTION) + " tb_3 on tb_4.int_2 = tb_3." + sf(SYSTEM_FIELD_ID) + " and tb_4." + sf(SYSTEM_FIELD_ID_COMPANY) + " = tb_3." + sf(SYSTEM_FIELD_ID_COMPANY) + lb;
            strSql += " inner join " + this.GetTable(TRN_TABLE) + " tb_1 on tb_3.int_3 = tb_1." + sf(SYSTEM_FIELD_ID) + " and tb_4." + sf(SYSTEM_FIELD_ID_COMPANY) + " = tb_1." + sf(SYSTEM_FIELD_ID_COMPANY) + lb;

            strSql += " where tb_4." + sf(SYSTEM_FIELD_ID_COMPANY) + " = " + this.GetSession().GetInt(SESSION_COMPANY) + lb;
            
            if (intIdTransaction > TRN_ALL)
            {
                strSql += " and tb_4.int_2 = " + intIdTransaction + lb;
            }
            
            //strSql += " order by tb_4." + sf(SYSTEM_FIELD_ID) + lb;
            strSql += " order by tb_4.int_6" + lb; // Position

            /*
             * Prepare the statement
             */
            Statement1 = Connection1.createStatement();
            ResultSet1 = Statement1.executeQuery(strSql);

            /*
             * Get the view dictionary
             */
            while (ResultSet1.next())
            {
                /*
                 * New instance to fill the catalog list
                 */                
                CENTCatalog1 = new CENTData();                
                
                /*
                 * Integer Fields
                 */
                CENTCatalog1.SetInt(FIELD_ID, ResultSet1.getInt("id_field"));
                CENTCatalog1.SetInt(FIELD_ID_TRN, ResultSet1.getInt("id_transaction"));                
                CENTCatalog1.SetInt(FIELD_SIZE, ResultSet1.getInt("field_size"));
                CENTCatalog1.SetInt(FIELD_ID_FK, ResultSet1.getInt("id_fk"));      
                
                /*
                 * Boolean Fields
                 */                
                CENTCatalog1.SetBoolean(FIELD_NULLABLE, ResultSet1.getInt("id_nullable"));
                CENTCatalog1.SetBoolean(FIELD_ID_UNIQUE, ResultSet1.getInt("id_unique"));                
                CENTCatalog1.SetBoolean(FIELD_ID_PK, ResultSet1.getInt("id_pk"));                    

                /*
                 * Text Fields
                 */
                if (ResultSet1.getString("field_label") != null)
                {
                    CENTCatalog1.SetText(FIELD_LABEL, ResultSet1.getString("field_label"));                
                }
                
                if (ResultSet1.getString("field_name") != null)
                {
                    CENTCatalog1.SetText(FIELD_NAME, ResultSet1.getString("field_name"));
                }
                
                if (ResultSet1.getInt("field_type") != 0)
                {
                    CENTCatalog1.SetInt(FIELD_TYPE, ResultSet1.getInt("field_type"));
                }

                if (ResultSet1.getString("field_object") != null)
                {
                    CENTCatalog1.SetText(FIELD_OBJECT, ResultSet1.getString("field_object"));                                    
                }
                
                if (ResultSet1.getString("field_domain") != null)
                {                    
                    CENTCatalog1.SetText(FIELD_DOMAIN_NAME, ResultSet1.getString("field_domain"));                
                }

                if (ResultSet1.getString("field_note") != null)
                {                    
                    CENTCatalog1.SetText(FIELD_NOTE, ResultSet1.getString("field_note"));
                }

                ListCENTCatalog1.add(CENTCatalog1);
            }

            /*
             * Close sql related objects
             */
            if (Statement1 != null)
                Statement1.close();

            if (ResultSet1 != null)
                ResultSet1.close();                        
            
            /*
             * Return the list
             */            
            return ListCENTCatalog1;

        } 
        catch (SQLException SQLException1)
        {
            throw new CENTException("EXCEPTION_FAIL_GET_CATALOG", SQLException1.getMessage());
        }         
        catch (Exception Exception1)
        {
            throw new CENTException("EXCEPTION_FAIL_GET_CATALOG", Exception1.getMessage());
        } 
        finally
        {            
            Statement1 = null;
            ResultSet1 = null;            
            strSql = null;
            CENTCatalog1 = null;
            ListCENTCatalog1 = null;    
        }
    }

    /*
     * Methods to generate CRUD command
     */
    protected String PrepareStatementToInsert(int intIdTransaction, List<CENTData> ListCENTCatalog1, CENTData CENTData1, String strTableName) throws CENTException, SQLException, Exception
    {
        /*
         * General Declaration
         */
        int intFieldType = 0;
        
        String strSql = "";
        String strValue = "";
        String strFieldName = "";
        String strFieldObject = "";

        try
        {
            /*
             * Start insert statement
             */            
            strSql += "insert into " + strTableName + " (";
                        
            /*
             * System Fields
             */            
            strSql += sf(SYSTEM_FIELD_ID_COMPANY)                                + ", ";
            strSql += sf(SYSTEM_FIELD_ID_AREA)                                   + ", ";
            strSql += sf(SYSTEM_FIELD_ID_PROFILE)                                + ", ";
            strSql += sf(SYSTEM_FIELD_ID_USER)                                   + ", ";
            strSql += sf(SYSTEM_FIELD_ID_TRANSACTION)                            + ", ";
            strSql += sf(SYSTEM_FIELD_SYSTEM_DATE)                               + ", ";
            strSql += sf(SYSTEM_FIELD_DATA_SOURCE)                            + ", ";
            strSql += sf(SYSTEM_FIELD_ID_LAYOUT);
            
            for (CENTData CENTCatalog1 : ListCENTCatalog1)
            {
                strFieldObject = CENTCatalog1.GetText(FIELD_OBJECT);
                
                if (!isSystemField(strFieldObject))
                {    
                    if (strFieldObject.equals(SYSTEM_FIELD_ID))
                        strSql += ", " + sf(strFieldObject);
                    else
                        strSql += ", " + strFieldObject;                        
                }    
            }
            
            strSql += ") values ";
                                    
            /*
             * Field Values
             */
            strValue = "";
            
            /*
             * Start new values line
             */                                    
            if (strValue.equals(""))
                strValue += "(";
            else
                strValue += ", (";       

            /*
             * System Fields
             */                    
            strValue += this.PrepareNumber(this.GetSession().GetInt(SESSION_COMPANY)) + ", ";
            strValue += this.PrepareNumber(this.GetSession().GetInt(SESSION_AREA)) + ", ";
            strValue += this.PrepareNumber(this.GetSession().GetInt(SESSION_PROFILE)) + ", ";
            strValue += this.PrepareNumber(this.GetSession().GetInt(SESSION_USER)) + ", ";
            strValue += this.PrepareNumber(intIdTransaction) + ", ";
            strValue += this.PrepareDate(this.GetSession().GetDate(SESSION_DATE)) + ", ";

            /*
             * Reconcile Identifier
             */
            if (!CENTData1.GetText(SYSTEM_FIELD_DATA_SOURCE).trim().equals("") && !CENTData1.GetText(SYSTEM_FIELD_DATA_SOURCE).equals(null))
                strValue += this.PrepareString(CENTData1.GetText(SYSTEM_FIELD_DATA_SOURCE)) + ", ";
            else
                strValue += "null" + ", ";

            /*
             * Id import process
             */
            if (CENTData1.GetInt(SYSTEM_FIELD_ID_LAYOUT) != Integer.MIN_VALUE && CENTData1.GetInt(SYSTEM_FIELD_ID_LAYOUT) != 0)
                strValue += this.PrepareNumber(CENTData1.GetInt(SYSTEM_FIELD_ID_LAYOUT));
            else
                strValue += "null";

            /*
             * Add regular fields from current view
             */
            for (CENTData CENTCatalog1 : ListCENTCatalog1)
            {                                       
                strFieldName = CENTCatalog1.GetText(FIELD_NAME);
                strFieldObject = CENTCatalog1.GetText(FIELD_OBJECT);
                intFieldType = CENTCatalog1.GetInt(FIELD_TYPE);
                
                if (!isSystemField(strFieldObject))
                {
                    switch (intFieldType)
                    {
                        case TYPE_INT:
                        case TYPE_BOOLEAN:

                            if (CENTData1.GetInt(strFieldObject) == Integer.MIN_VALUE)
                                strValue += ", " + "0";
                            else                                
                                strValue += ", " + this.PrepareNumber(CENTData1.GetInt(strFieldObject));
                            
                            break;

                        case TYPE_TEXT:
                            
                            if (CENTData1.GetText(strFieldObject) != null && CENTData1.GetText(strFieldObject).trim() != "")
                            {    
                                strValue += ", " + this.PrepareString(CENTData1.GetText(strFieldObject));
                            }    
                            else
                            {
                                strValue += ", " + "null";
                            }
                            break;                            

                        case TYPE_DATE:
                            
                            if (CENTData1.GetDate(strFieldObject) != null)
                                strValue += ", " + this.PrepareDate(CENTData1.GetDate(strFieldObject));
                            else
                                strValue += ", " + "null";
                            break;                            

                        case TYPE_DOUBLE:                            
                            strValue += ", " + this.PrepareNumber(CENTData1.GetDouble(strFieldObject));
                            break;                                                        
                    }  
                }
            }

            /*
             * End line insert
             */                
            strValue += ")";          
            strSql += strValue;
                        
            /*
             * Return the TO
             */   
            return strSql;
            
        } 
        catch (Exception Exception1)
        {
            throw Exception1;
        } 
        finally
        {
            strSql = null;
            strValue = null;
            strFieldName = null;
            strFieldObject = null;
        }
    }    
    
    protected String PrepareStatementToUpdate(CENTData CENTData1)  throws CENTException, SQLException, Exception
    {
        /*
         * General Declaration
         */        
        double dblValue = 0;        
        boolean blnNull = false;
        
        int blnValue = Null;        
        int intValue = 0;
        int intFieldType = 0;
        int blnIsPk = Null;        
        
        Date datValue = null;
        
        String strSql = "";        
        String strSet = "";
        String strWhere = "";
        String strFieldObject = "";
        String strFieldName = "";
        String strFieldValue = "";
        String strTableName = "";

        List<CENTData> ListCENTCatalog1 = null;

        try
        {           
            /*
             * Get the catalog to generate the SQL
             */
            ListCENTCatalog1 = this.GetCatalog(this.GetIdTransaction());
            strTableName = this.GetTable(this.GetIdTransaction());
                
            /*
             * Prepare the SQL Statement - Start with where clause for system fields
             */
            strWhere += " where " + sf(SYSTEM_FIELD_ID_COMPANY) + " = " + this.PrepareNumber(this.GetSession().GetInt(SESSION_COMPANY));
            
            strWhere += " and " + sf(SYSTEM_FIELD_ID_TRANSACTION) + " = " + this.PrepareNumber(this.GetIdTransaction());            
            
            if (CENTData1.GetDate(SYSTEM_FIELD_SYSTEM_DATE) != null)
            {
                strWhere += " and " + sf(SYSTEM_FIELD_SYSTEM_DATE) + " = " + this.PrepareDate(CENTData1.GetDate(SYSTEM_FIELD_SYSTEM_DATE));
            }
            
            if (!CENTData1.GetText(SYSTEM_FIELD_DATA_SOURCE).equals(""))
            {
                strWhere += " and " + sf(SYSTEM_FIELD_DATA_SOURCE) + " = " + this.PrepareString(CENTData1.GetText(SYSTEM_FIELD_DATA_SOURCE));
            }
            
            /*
             * Generate the SQL
             */
            for (CENTData CENTCatalog1 : ListCENTCatalog1)
            {
                intFieldType = CENTCatalog1.GetInt(FIELD_TYPE); 
                strFieldObject = CENTCatalog1.GetText(FIELD_OBJECT);
                blnIsPk = CENTCatalog1.GetBoolean(FIELD_ID_PK);
                strFieldName = CENTCatalog1.GetText(FIELD_NAME);
                blnNull = false;
                                
                if (!isSystemField(strFieldObject))
                {                    
                    switch (intFieldType)
                    {
                        case TYPE_INT:

                            intValue = CENTData1.GetInt(strFieldObject);
                
                            if (strFieldObject.equals(SYSTEM_FIELD_ID))
                            {
                                strFieldObject = sf(strFieldObject);
                            }                            
                            
                            if (intValue != Integer.MIN_VALUE)
                            {
                                if (blnIsPk == True)
                                {
                                    strWhere += " and " + strFieldObject + " = " + this.PrepareNumber(intValue);
                                }
                                else
                                {
                                    strSet += ", " + strFieldObject + " = " + this.PrepareNumber(intValue);
                                }
                            }

                            break;

                        case TYPE_TEXT:

                            strFieldValue = CENTData1.GetText(strFieldObject);

                            if (!strFieldValue.trim().equals("") && !strFieldValue.trim().equals("''"))
                            {
                                strSet += ", " + strFieldObject + " = " + this.PrepareString(strFieldValue);
                            }
                            else
                            {
                                strSet += ", " + strFieldObject + " = " + "null";
                            }

                            break;

                        case TYPE_DATE:

                            datValue = CENTData1.GetDate(strFieldObject);

                            if (datValue != null)
                            {
                                strSet += ", " + strFieldObject + " = " + this.PrepareDate(datValue);
                            }                        

                            break;

                        case TYPE_DOUBLE:

                            dblValue = CENTData1.GetDouble(strFieldObject);

                            if (dblValue != Double.MIN_VALUE)
                            {                            
                                strSet += ", " + strFieldObject + " = " + this.PrepareNumber(dblValue);
                            }
                            
                            break;

                        case TYPE_BOOLEAN:

                            blnValue = CENTData1.GetBoolean(strFieldObject);                        

                            if (blnValue != Null)
                            {
                                strSet += ", " + strFieldObject + " = " + this.PrepareNumber(blnValue);
                            }                        

                            break;
                    }

                }

            }

            /*
             * Generate final statement
             */                                
            strSet = this.RemoveCharOnTheRight(strSet, 1);
            strSql = "update " + strTableName + " set " + strSet + strWhere;

            /*
             * Return it
             */   
            return strSql;

        } 
        catch (Exception Exception1)
        {
            throw Exception1;
        } 
        finally
        {
            datValue = null;

            strSql = null;        
            strSet = null;
            strWhere = null;
            strFieldObject = null;
            strFieldValue = null;
            strTableName = null;
            
            ListCENTCatalog1 = null;
        }
    }
    
    protected String PrepareStatementToDelete(CENTData CENTData1)  throws CENTException, SQLException, Exception
    {
        /*
         * General Declaration
         */
        String strSql = "";        
        String strWhere = "";    
        String strTableName = "";        
        
        int intIdTransaction = 0;
        int intIdLayout = 0;
        int intIdReconcile = 0;
        int intIdView = 0;
        int intIdField = 0;
        
        String strIdTransaction = "";
        String strIdLayout = "";
        String strIdReconcile = "";
        String strIdView = "";
        String strIdField = "";
        
        List<CENTData> ListCENTCatalog1 = null;        

        try
        {              
            /*
             * Get the transaction
             */
            ListCENTCatalog1 = this.GetCatalog(this.GetIdTransaction());   
            
            /*
             * Batch delete control
             */            
            strIdTransaction = GetFieldObject("id_transaction", ListCENTCatalog1);            
            if (!strIdTransaction.equals(""))
            {
                intIdTransaction = CENTData1.GetInt(strIdTransaction);
            }
            
            strIdLayout = GetFieldObject("id_layout", ListCENTCatalog1);            
            if (!strIdLayout.equals(""))
            {
                intIdLayout = CENTData1.GetInt(strIdLayout);
            }
            
            strIdReconcile = GetFieldObject("id_reconcile", ListCENTCatalog1);            
            if (!strIdReconcile.equals(""))
            {
                intIdReconcile = CENTData1.GetInt(strIdReconcile);
            }
            
            strIdView = GetFieldObject("id_view", ListCENTCatalog1);            
            if (!strIdView.equals(""))
            {
                intIdView = CENTData1.GetInt(strIdView);
            }
            
            strIdField = GetFieldObject("id_field", ListCENTCatalog1);            
            if (!strIdField.equals(""))
            {
                intIdField = CENTData1.GetInt(strIdField);
            }            
            
            /*
             * Get the table name
             */
            strTableName = this.GetTable(this.GetIdTransaction());

            /*
             * Prepare the SQL Statement - Start with where clause for system fields
             */
            strWhere += " where " + sf(SYSTEM_FIELD_ID_COMPANY) + " = " + this.PrepareNumber(this.GetSession().GetInt(SESSION_COMPANY));
            strWhere += " and " + sf(SYSTEM_FIELD_ID_TRANSACTION) + " = " + this.PrepareNumber(this.GetIdTransaction());                        
            
            if (CENTData1.GetInt(SYSTEM_FIELD_ID) != Integer.MIN_VALUE && CENTData1.GetInt(SYSTEM_FIELD_ID) != 0)
            {
                strWhere += " and " + sf(SYSTEM_FIELD_ID) + " = " + this.PrepareNumber(CENTData1.GetInt(SYSTEM_FIELD_ID)); // Field ID
            }    
            /*
             * Delete according to the module
             */            
            if (intIdTransaction != Integer.MIN_VALUE && intIdTransaction != 0)
            {
                strWhere += " and " + GetFieldObject("id_transaction", ListCENTCatalog1) + " = " + this.PrepareNumber(intIdTransaction);
            }            
            
            if (intIdLayout != Integer.MIN_VALUE && intIdLayout != 0)
            {
                strWhere += " and " + GetFieldObject("id_layout", ListCENTCatalog1) + " = " + this.PrepareNumber(intIdLayout);
            }
            
            if (intIdReconcile != Integer.MIN_VALUE && intIdReconcile != 0)
            {
                strWhere += " and " + GetFieldObject("id_reconcile", ListCENTCatalog1) + " = " + this.PrepareNumber(intIdReconcile);
            }
                        
            if (intIdView != Integer.MIN_VALUE && intIdView != 0)
            {
                strWhere += " and " + GetFieldObject("id_view", ListCENTCatalog1) + " = " + this.PrepareNumber(intIdView);
            }
            
            if (intIdField != Integer.MIN_VALUE && intIdField != 0)
            {
                strWhere += " and " + GetFieldObject("id_field", ListCENTCatalog1) + " = " + this.PrepareNumber(intIdField);
            }

            /*
             * Generate final statement
             */
            strSql = "delete from " + strTableName + strWhere;

            /*
             * Return it
             */   
            return strSql;
            
        } 
        catch (Exception Exception1)
        {
            throw Exception1;
        } 
        finally
        {
            strIdTransaction = null;
            strIdLayout = null;
            strIdReconcile = null;
            strIdView = null;
            strIdField = null;

            ListCENTCatalog1 = null;        
        }
    }        
           
    protected String PrepareStatementToGetList(CENTData CENTData1) throws CENTException, SQLException
    {
        /*
         * General Declaration
         */        
        String strSql = "";

        try
        {                        
            /*
             * Generate the query, if cannot do using the view, use the catalog
             */            
            if (this.GetIdEvent() == EVENT_NEW || this.GetIdEvent() == EVENT_EDIT  ||  this.GetIdView() == -1)
            {
                strSql = this.GetSqlQueryFromCatalog(CENTData1);
            }
            else
            {
                strSql = this.GetSqlQueryFromView(null, CENTData1);
            }

            /*
             * Return the list
             */
            return strSql;

        }
        catch (CENTException CENTException1)
        {
            throw CENTException1;
        }
        catch (Exception Exception1)
        {
            throw new CENTException("EXCEPTION_FAIL_TO_GETLIST", Exception1.getMessage());
        } 
        finally
        {
            strSql = null;
        }
    }    

    protected String PrepareStatementToGetCount(CENTData CENTData1) throws CENTException
    {
        /*
         * General Declaration
         */
        String strSql = "";
        List<CENTData> ListCENTView1 = null;
        
        try
        {            
            /*
             * Create the objects
             */
            ListCENTView1 = new ArrayList<CENTData>();
            
            /*
             * Return the fields name to query the module catalog and data
             */
            ListCENTView1 = this.GetView(this.GetIdView());

            /*
             * If no view definition, try to use the catalog
             */            
            if (ListCENTView1.isEmpty())
            {
                return "";
            }

            /*
             * Prepare the query
             */
            strSql += "select ";

            strSql += "min(tb_1." + sf(SYSTEM_FIELD_ID) + ") min,";            
            strSql += "max(tb_1." + sf(SYSTEM_FIELD_ID) + ") max,";
            strSql += "count(tb_1." + sf(SYSTEM_FIELD_ID) + ") count";
            
            strSql += this.TransformViewToQuery(ListCENTView1, CENTData1, SQL_PART_FROM);
            strSql += this.TransformViewToQuery(ListCENTView1, CENTData1, SQL_PART_JOIN);
            strSql += this.TransformViewToQuery(ListCENTView1, CENTData1, SQL_PART_WHERE);
            strSql += this.TransformViewToQuery(ListCENTView1, CENTData1, SQL_PART_AND);            
            strSql += this.TransformViewToQuery(ListCENTView1, CENTData1, SQL_PART_GROUP_BY);

            /*
             * Return the list
             */
            return strSql;            

        } 
        catch (CENTException CENTException1)
        {
            throw CENTException1;
        }         
        catch (Exception Exception1)
        {
            throw new CENTException("EXCEPTION_FAIL_TO_GETLIST", Exception1.getMessage());
        } 
        finally
        {
            strSql = "";
            ListCENTView1 = null;
        }
    }        

    /*
     * Private methods
     */   
    protected String TransformViewToQuery(List<CENTData> ListCENTView1, CENTData CENTData1, int intIdSqlPart) throws CENTException
    {
        /*
         * General Declaration
         */        
        final int ALIAS_ID_TRANSACTION = 1;
        final int ALIAS_ID_FK = 2;                
        final int ALIAS_KEY = 1;
        final int ALIAS_ALIAS = 2;
        final int ALIAS_DOMAIN_NAME = 3;
        final int ALIAS_FIELD_OBJECT = 4;
        final int ALIAS_FIELD_NAME = 5;
        
        int x = 0;
        int y = 0;
        int z = 0;

        boolean blnControl = false;
        boolean blnJoinMatch = false;
        
        int intAliasMatch = 0;
        int intIntCount = 0;
        int intTextCount = 0;
        int intDoubleCount = 0;        
        int intDateCount = 0;                
        int intBooleanCount = 0;                
        int intIdTransaction = 0;
        int intFieldType = 0;
        int intIdCommand = 0;
        int intIdOperator = 0;
        int intCount = 0;
        int intIdFk = 0;
        
        String strTemp = "";
        String strAnd = "";
        String strSql = "";        
        String strFieldObject = "";
        String strFieldObjectDisplay = "";
        String strFieldList = "";
        String strFrom = "";
        String strJoin = "";
        String strJoinMatch = "";
        String strAlias = "";
        String strWhere = "";
        String strGroupBy = "";
        String strOrderBy = "";
        String strValue = "";        
        String strTableAlias = "";
        String strTableBase = "";
        String strField = "";
        String strDomainName = "";
        String strFieldName = "";
        String strKey = "";        
        String strTransactionInUse = "";
        
        CENTData CENTAlias1 = null;
        List<CENTData> ListCENTAlias1 = new ArrayList<CENTData>();
        List<CENTData> ListCENTCatalogMatch1 = null;
        List<CENTData> ListCENTCatalogMatchItem1 = null;
        List<CENTData> ListCENTCatalogDomain1 = null;
        List<CENTData> ListCENTReconcileStep1 = null;
        List<CENTData> ListCENTReconcileStepRule1 = null;
        
        try
        {
            /*
             * If no view definition, try to use the catalog
             */            
            if (ListCENTView1.isEmpty())
            {
                return "";
            }            

            /*
             * The alias for table base is always table 1
             */
            strTableBase = "tb_1";
            intCount = 1; // Cannot be smaller than 3 due to joins
            
            /*
             * Prepare the list of alias based on different transaction and fks
             */            
            for (CENTData CENTView1 : ListCENTView1)
            {
                strTransactionInUse = "";
                intIdCommand = CENTView1.GetInt(FIELD_ID_COMMAND);
                intIdTransaction = CENTView1.GetInt(FIELD_ID_TRN);
                intIdFk = CENTView1.GetInt(FIELD_ID_FK);
                strDomainName = CENTView1.GetText(FIELD_DOMAIN_NAME);
                strFieldObject = CENTView1.GetText(FIELD_OBJECT);
                strFieldName = CENTView1.GetText(FIELD_NAME);
                
                if (isSystemField(strFieldObject))
                {
                    strFieldObject = sf(strFieldObject);
                }
                
                if (intIdCommand > 0)
                {
                    if (intIdCommand <= SELECTABLE_FIELD)
                    {                
                        blnControl = false;
                       
                        /*
                         * Control the join betwwen transaction
                         */
                        strKey = "";
                        strKey += String.valueOf(CENTView1.GetInt(FIELD_ID_TRN));
                        strKey += String.valueOf(CENTView1.GetInt(FIELD_ID_FK));
                        strKey += String.valueOf(CENTView1.GetText(FIELD_DOMAIN_NAME));

                        /*
                         * Control the join betwwen transaction
                         */                        
                        if (intIdFk != 0)
                        {
                            intCount ++;    
                            strAlias = " tb_" + intCount;
                        }
                        else
                        {
                            if (intIdTransaction != this.GetIdTransaction())
                            {
                                for (CENTData CENTItem1 : ListCENTAlias1)
                                {       
                                    if (CENTItem1.GetText(FIELD_ID_TRN).equals(String.valueOf(intIdTransaction)) && CENTItem1.GetText(FIELD_ID_FK).equals(String.valueOf(intIdFk)))
                                    {                                        
                                        strTransactionInUse = CENTItem1.GetText(FIELD_NOTE); // Alias
                                    }
                                }
                                
                                if (strTransactionInUse.equals(""))
                                {
                                    intCount ++;
                                    strAlias = " tb_" + intCount;                                    
                                }
                                else
                                {
                                    strAlias = strTransactionInUse;
                                }
                            }
                            else
                            {
                                strAlias = " " + strTableBase;
                            }
                        }

                        /*
                         * Check if the alias is already defined
                         */                             
                        for (CENTData CENTItem1 : ListCENTAlias1)
                        {       
                            if (CENTItem1.GetText(FIELD_NAME).equals(strKey))
                            {
                                blnControl = true;
                            }
                        }

                        if (blnControl == false)
                        {
                            CENTAlias1 = new CENTData();
                            CENTAlias1.SetText(ALIAS_KEY, strKey);
                            CENTAlias1.SetText(ALIAS_ALIAS, strAlias);
                            CENTAlias1.SetInt(ALIAS_ID_FK, intIdFk);
                            CENTAlias1.SetInt(ALIAS_ID_TRANSACTION, intIdTransaction);
                            CENTAlias1.SetText(ALIAS_DOMAIN_NAME, strDomainName);
                            CENTAlias1.SetText(ALIAS_FIELD_OBJECT, strFieldObject);
                            CENTAlias1.SetText(ALIAS_FIELD_NAME, strFieldName);                                  
                            
                            ListCENTAlias1.add(CENTAlias1);                            
                        }
                    }
                }
            }
            
            /*
             * Prepare the select
             */
            intCount = 1;
            blnControl = false;
            intCount = 0;
            
            for (CENTData CENTView1 : ListCENTView1)
            {
                /*
                 * Get field name and alias
                 */
                intIdCommand = CENTView1.GetInt(FIELD_ID_COMMAND);
                intIdOperator = CENTView1.GetInt(FIELD_ID_OPERATOR);
                intIdTransaction = CENTView1.GetInt(FIELD_ID_TRN);
                strFieldObject = CENTView1.GetText(FIELD_OBJECT);
                intFieldType = CENTView1.GetInt(FIELD_TYPE);
                strValue = CENTView1.GetText(FIELD_CONDITION_VALUE);
                intIdFk = CENTView1.GetInt(FIELD_ID_FK);
                strDomainName = CENTView1.GetText(FIELD_DOMAIN_NAME);
                strFieldName = CENTView1.GetText(FIELD_NAME);                

                if (isSystemField(strFieldObject) || strFieldObject.equals(SYSTEM_FIELD_ID))
                {
                    strFieldObject = sf(strFieldObject);
                }
                
                /*
                 * Get the table alias based on transction or fk
                 */
                strKey = "";
                strKey += String.valueOf(CENTView1.GetInt(FIELD_ID_TRN));
                strKey += String.valueOf(CENTView1.GetInt(FIELD_ID_FK));
                strKey += String.valueOf(CENTView1.GetText(FIELD_DOMAIN_NAME));
                //strKey += String.valueOf(CENTView1.GetText(FIELD_NAME));

                for (CENTData CENTItem1 : ListCENTAlias1)
                {
                    if (CENTItem1.GetText(ALIAS_KEY).equals(strKey))
                    {
                        strTableAlias = CENTItem1.GetText(ALIAS_ALIAS);
                        break;
                    }
                }                
                
                /*
                 * For fks or joins, get the description
                 */                
                if (intIdFk != 0)
                {                
                    if (intIdFk == TRN_DOMAIN)
                        strFieldObjectDisplay = "text_2";
                    else
                        strFieldObjectDisplay = "text_1";

                    intFieldType = TYPE_TEXT;                    
                }
                else
                {
                    strFieldObjectDisplay = strFieldObject;
                }

                /*
                 * Prepare the field list
                 */
                if (intIdCommand <= SELECTABLE_FIELD)
                {
                    /*
                     * Build the alias based on the type
                     */
                    switch (intFieldType)
                    {
                        case TYPE_INT:
                            intIntCount ++;
                            strAlias = "int_" + intIntCount;
                            break;

                        case TYPE_TEXT:
                            intTextCount ++;
                            strAlias = "text_" + intTextCount;
                            break;    

                        case TYPE_DATE:
                            intDateCount ++;
                            strAlias = "date_" + intDateCount;
                            break;    

                        case TYPE_DOUBLE:
                            intDoubleCount ++;
                            strAlias = "double_" + intDoubleCount;
                            break;      

                        case TYPE_BOOLEAN:
                            intBooleanCount ++;
                            strAlias = "boolean_" + intBooleanCount;
                            break;
                    }

                    if (x == 1)
                    {
                        strFieldList += ",";
                        strField += ",";
                    }

                    /*
                     * Prepare field list
                     */
                    switch (intIdCommand) // COMMAND
                    {
                        case COMMAND_SELECT_FIELD:
                            
                            strFieldList += strTableAlias + "." + strFieldObjectDisplay + " " + dbqt(strAlias) + lb;
                            
                                if (!strFieldObject.equals(SYSTEM_FIELD_ID))
                                {        
                                    strField += strTableBase + "." + strFieldObject + lb;

                                    if (y == 1)
                                    {
                                        strGroupBy += ",";
                                    }

                                    strGroupBy += strTableAlias + "." + strFieldObjectDisplay + lb;

                                    y = 1;
                                }
                                
                            break;

                        case COMMAND_SELECT_COUNT:              

                            blnControl = true;
                            strFieldList += "count(" + strTableAlias + "." + strFieldObjectDisplay + ")" + " " + dbqt(strAlias) + lb;
                            strField += "count(" + strTableBase + "." + strFieldObject + ")" + lb;
                            break;

                        case COMMAND_SELECT_SUM:

                            blnControl = true;                            
                            strFieldList += "sum(" + strTableAlias + "." + strFieldObjectDisplay + ")" + " " + dbqt(strAlias) + lb;
                            strField += "sum(" + strTableBase + "." + strFieldObject + ")" + lb;
                            break;

                        case COMMAND_SELECT_MAX:      

                            blnControl = true;                            
                            strFieldList += "max(" + strTableAlias + "." + strFieldObjectDisplay + ")" + " " + dbqt(strAlias) + lb;
                            strField += "max(" + strTableBase + "." + strFieldObject + ")" + lb;
                            break;

                        case COMMAND_SELECT_MIN:      

                            blnControl = true;                            
                            strFieldList += "min(" + strTableAlias + "." + strFieldObjectDisplay + ")" + " " + dbqt(strAlias) + lb;
                            strField += "min(" + strTableBase + "." + strFieldObject + ")" + lb;
                            break;

                        case COMMAND_SELECT_AVG:        

                            blnControl = true;                            
                            strFieldList += "avg(" + strTableAlias + "." + strFieldObjectDisplay + ")" + " " + dbqt(strAlias) + lb;
                            strField += "avg(" + strTableBase + "." + strFieldObject + ")" + lb;
                            break;                        
                    }
                    
                    x = 1;
                }
                
                /*
                 * Predefined where/and clause
                 */
                strTemp = GetCondition(intIdTransaction, intIdCommand, intFieldType, intIdOperator, strFieldObject, strTableAlias, strDomainName, strValue);
                if (!strTemp.trim().equals(""))
                {
                    strAnd += strTemp;
                }    

                /*
                 * Ordering fields
                 */
                if (intIdCommand == COMMAND_ORDER_BY_ASC || intIdCommand == COMMAND_ORDER_BY_DESC)
                {
                    if (z == 1)
                    {
                        strOrderBy += ",";
                    }
                    
                    //strOrderBy += strTableAlias + "." + strFieldObject;
                                                            
                    if (intIdFk != 0)
                    {                
                        strOrderBy += strTableAlias + "." + strFieldObjectDisplay;
                    }
                    else
                    {
                        strOrderBy += strTableAlias + "." + strFieldObject;
                    }                    
                    
                    if (intIdCommand == COMMAND_ORDER_BY_ASC)
                    {
                        strOrderBy += " asc" + lb;
                    }                    
                    else
                    {
                        strOrderBy += " desc" + lb;                        
                    }
                    
                    z = 1;
                }                

            } // View for ends here

            
            /*
             * Create the joins
             */            
            for (CENTData CENTJoin1 : ListCENTAlias1)
            {
                intIdFk = CENTJoin1.GetInt(ALIAS_ID_FK);
                intIdTransaction = CENTJoin1.GetInt(ALIAS_ID_TRANSACTION);
                strTableAlias = CENTJoin1.GetText(ALIAS_ALIAS);
                strDomainName = CENTJoin1.GetText(ALIAS_DOMAIN_NAME);
                strFieldObject = CENTJoin1.GetText(ALIAS_FIELD_OBJECT);
                strFieldName = CENTJoin1.GetText(ALIAS_FIELD_NAME);
                
                /*
                 * Regular join inside the same transaction
                 */
                if (intIdTransaction == this.GetIdTransaction())
                {
                    if (intIdFk != 0)
                    {
                        if (intIdFk == TRN_DOMAIN)
                        {
                            ListCENTCatalogDomain1 = this.GetCatalog(TRN_DOMAIN);
                            strJoin += "left join " + this.GetTable(intIdFk) + " " + strTableAlias + lb;
                            strJoin += "on " + strTableBase + "." + sf(SYSTEM_FIELD_ID_COMPANY) + " = " + strTableAlias + "." + sf(SYSTEM_FIELD_ID_COMPANY) + lb;
                            strJoin += "and " + strTableBase + "." + strFieldObject + " = " + strTableAlias + "." + GetFieldObject("id_domain", ListCENTCatalogDomain1) + lb;
                            strJoin += "and " + strTableAlias + "." + "text_1" + " = " + PrepareString(strDomainName) + lb;                        
                        }
                        else
                        {
                            strJoin += "left join " + this.GetTable(intIdFk) + " " + strTableAlias + lb;
                            strJoin += "on " + strTableBase + "." + sf(SYSTEM_FIELD_ID_COMPANY) + " = " + strTableAlias + "." + sf(SYSTEM_FIELD_ID_COMPANY) + lb;
                            strJoin += "and " + strTableBase + "." + strFieldObject + " = " + strTableAlias + "." + sf(SYSTEM_FIELD_ID) + lb; // + CENTView1.GetText(FIELD_CONDITION_VALUE) + lb;
                        }
                    }
                }    
                
                /*
                 *Regular joins using different transactions without fk                
                 */
                else if (intIdTransaction != this.GetIdTransaction() && intIdFk == 0)
                {
                    switch (intIdTransaction)
                    {
                        case TRN_MATCH:

                            if (blnJoinMatch == false)
                            {
                                ListCENTCatalogMatch1 = this.GetCatalog(TRN_MATCH);
                                ListCENTCatalogMatchItem1 = this.GetCatalog(TRN_MATCH_ITEM);                                

                                intAliasMatch ++;                                                                              
                                strJoin += "left join " + this.GetTable(TRN_MATCH_ITEM) + " " + ("t" + intAliasMatch) + lb;
                                strJoin += "on " + strTableBase + "." + sf(SYSTEM_FIELD_ID_COMPANY) + " = " + ("t" + intAliasMatch) + "." + sf(SYSTEM_FIELD_ID_COMPANY) + lb;
                                strJoin += "and " + strTableBase + "." + sf(SYSTEM_FIELD_ID_PROCESS) + " = " + ("t" + intAliasMatch) + "." + GetFieldObject("id_process", ListCENTCatalogMatchItem1) + lb;
                                strJoin += "and " + strTableBase + "." + sf(SYSTEM_FIELD_ID) + " = " + ("t" + intAliasMatch) + "." + GetFieldObject("id_record", ListCENTCatalogMatchItem1) + lb;

                                strJoin += "left join " + this.GetTable(TRN_MATCH) + " " + strTableAlias + lb;
                                strJoin += "on " + ("t" + intAliasMatch) + "." + sf(SYSTEM_FIELD_ID_COMPANY) + " = " + strTableAlias + "." + sf(SYSTEM_FIELD_ID_COMPANY) + lb;
                                strJoin += "and " + ("t" + intAliasMatch) + "." + GetFieldObject("id_match", ListCENTCatalogMatchItem1) + " = " + strTableAlias + "." + sf(SYSTEM_FIELD_ID) + lb;
                                strJoin += "and " + ("t" + intAliasMatch) + "." + GetFieldObject("id_process", ListCENTCatalogMatchItem1) + " = " + strTableAlias + "." + GetFieldObject("id_process", ListCENTCatalogMatch1) + lb;

                                strJoinMatch = strTableAlias;
                                blnJoinMatch = true;
                            }
                    }
                }                    
                
                /*
                 *Regular joins using different transactions within fk
                 */
                else if (intIdTransaction != this.GetIdTransaction() && intIdFk != 0) 
                {                
                    switch (intIdTransaction)
                    {
                        case TRN_MATCH:
                            
                            switch (strFieldName)
                            {
                                case "id_reconcile":

                                    strJoin += "left join " + this.GetTable(TRN_RECONCILIATION) + " " + strTableAlias + lb;
                                    strJoin += "on " + strTableBase + "." + sf(SYSTEM_FIELD_ID_COMPANY) + " = " + strTableAlias + "." + sf(SYSTEM_FIELD_ID_COMPANY) + lb;
                                    strJoin += "and " + strJoinMatch + "." + GetFieldObject("id_reconcile", ListCENTCatalogMatch1) + " = " + strTableAlias + "." + sf(SYSTEM_FIELD_ID) + lb;

                                    break;

                                case "id_step":

                                    ListCENTReconcileStep1 = this.GetCatalog(TRN_RECONCILIATION_STEP);                                    

                                    strJoin += "left join " + this.GetTable(TRN_RECONCILIATION_STEP) + " " + strTableAlias + lb;
                                    strJoin += "on " + strTableBase + "." + sf(SYSTEM_FIELD_ID_COMPANY) + " = " + strTableAlias + "." + sf(SYSTEM_FIELD_ID_COMPANY) + lb;
                                    strJoin += "and " + strJoinMatch + "." + GetFieldObject("id_reconcile", ListCENTCatalogMatch1) + " = " + strTableAlias + "." + GetFieldObject("id_reconcile", ListCENTReconcileStep1) + lb;
                                    strJoin += "and " + strJoinMatch + "." + GetFieldObject("id_step", ListCENTCatalogMatch1) + " = " + strTableAlias + "." + GetFieldObject("id", ListCENTReconcileStep1) + lb;

                                    break;

                                case "id_rule":

                                    ListCENTReconcileStepRule1 = this.GetCatalog(TRN_RECONCILIATION_STEP_RULE);

                                    strJoin += "left join " + this.GetTable(TRN_RECONCILIATION_STEP_RULE) + " " + strTableAlias + lb;
                                    strJoin += "on " + strTableBase + "." + sf(SYSTEM_FIELD_ID_COMPANY) + " = " + strTableAlias + "." + sf(SYSTEM_FIELD_ID_COMPANY) + lb;
                                    strJoin += "and " + strJoinMatch + "." + GetFieldObject("id_reconcile", ListCENTCatalogMatch1) + " = " + strTableAlias + "." + GetFieldObject("id_reconcile", ListCENTReconcileStepRule1) + lb;
                                    strJoin += "and " + strJoinMatch + "." + GetFieldObject("id_step", ListCENTCatalogMatch1) + " = " + strTableAlias + "." + GetFieldObject("id_step", ListCENTReconcileStepRule1) + lb;
                                    strJoin += "and " + strJoinMatch + "." + GetFieldObject("id_rule", ListCENTCatalogMatch1) + " = " + strTableAlias + "." + GetFieldObject("id", ListCENTReconcileStepRule1) + lb;

                                    break;   

                                case "id_status":

                                    ListCENTCatalogDomain1 = this.GetCatalog(TRN_DOMAIN);

                                    strJoin += "left join " + this.GetTable(TRN_DOMAIN) + " " + strTableAlias + lb;
                                    strJoin += "on " + strTableBase + "." + sf(SYSTEM_FIELD_ID_COMPANY) + " = " + strTableAlias + "." + sf(SYSTEM_FIELD_ID_COMPANY) + lb;
                                    strJoin += "and " + strJoinMatch + "." + GetFieldObject("id_status", ListCENTCatalogMatch1) + " = " + strTableAlias + "." + GetFieldObject("id_domain", ListCENTCatalogDomain1) + lb;
                                    strJoin += "and " + strTableAlias + "." + GetFieldObject("name", ListCENTCatalogDomain1) + " = 'domain_10'"+ lb;

                                    break;                                    

                                case "id_transaction":                                    

                                    strJoin += "left join " + this.GetTable(TRN_TRANSACTION) + " " + strTableAlias + lb;
                                    strJoin += "on " + strTableBase + "." + sf(SYSTEM_FIELD_ID_COMPANY) + " = " + strTableAlias + "." + sf(SYSTEM_FIELD_ID_COMPANY) + lb;
                                    strJoin += "and " + strJoinMatch + "." + GetFieldObject("id_transaction", ListCENTCatalogMatch1) + " = " + strTableAlias + "." + sf(SYSTEM_FIELD_ID) + lb;

                                    break;

                                case "id_field":                                    

                                    strJoin += "left join " + this.GetTable(TRN_CATALOG) + " " + strTableAlias + lb;
                                    strJoin += "on " + strTableBase + "." + sf(SYSTEM_FIELD_ID_COMPANY) + " = " + strTableAlias + "." + sf(SYSTEM_FIELD_ID_COMPANY) + lb;
                                    strJoin += "and " + strJoinMatch + "." + GetFieldObject("id_field", ListCENTCatalogMatch1) + " = " + strTableAlias + "." + sf(SYSTEM_FIELD_ID) + lb;

                                    break;

                                default:    

                                    strJoin += "left join " + this.GetTable(TRN_MATCH) + " " + strTableAlias + lb;
                                    strJoin += "on " + strTableBase + "." + sf(SYSTEM_FIELD_ID_COMPANY) + " = " + strTableAlias + "." + sf(SYSTEM_FIELD_ID_COMPANY) + lb;
                                    strJoin += "and " + strJoinMatch + "." + GetFieldObject("id", ListCENTCatalogMatch1) + " = " + strTableAlias + "." + sf(SYSTEM_FIELD_ID) + lb;

                            }

                            break;

                        case TRN_DOMAIN:    

                            ListCENTCatalogDomain1 = this.GetCatalog(TRN_DOMAIN);
                            strJoin += "left join " + this.GetTable(intIdFk) + " " + strTableAlias + lb;
                            strJoin += "on " + strTableBase + "." + sf(SYSTEM_FIELD_ID_COMPANY) + " = " + strTableAlias + "." + sf(SYSTEM_FIELD_ID_COMPANY) + lb;
                            strJoin += "and " + strTableBase + "." + strFieldObject + " = " + strTableAlias + "." + GetFieldObject("id_domain", ListCENTCatalogDomain1) + lb;
                            strJoin += "and " + strTableAlias + "." + "text_1" + " = " + PrepareString(strDomainName) + lb;

                            break;    

                        default:

                            strJoin += "left join " + this.GetTable(intIdFk) + " " + strTableAlias + lb;
                            strJoin += "on " + strTableBase + "." + sf(SYSTEM_FIELD_ID_COMPANY) + " = " + strTableAlias + "." + sf(SYSTEM_FIELD_ID_COMPANY) + lb;
                            strJoin += "and " + strTableBase + "." + strFieldObject + " = " + strTableAlias + "." + sf(SYSTEM_FIELD_ID) + lb; // + CENTView1.GetText(FIELD_CONDITION_VALUE) + lb;

                    } // fk
                }
            }            

            
            /*
             * From clause
             */
            strFrom = "from " + this.GetTable(this.GetIdTransaction()) + " " + strTableBase + lb;
            
            /*
             * Mandatory where condition, administrators can see all companies as they are under the same group
             */
            strWhere += "where " + strTableBase + "." + sf(SYSTEM_FIELD_ID_COMPANY) + " = " + this.GetSession().GetInt(SESSION_COMPANY) + lb;
            strWhere += "and " + strTableBase + "." + sf(SYSTEM_FIELD_ID_TRANSACTION) + " = " + this.GetIdTransaction() + lb;            
                
            /*
             * Access control by area
             */
            if (this.GetIdTransaction() != TRN_VIEW) 
            {
                if (this.GetSession().GetInt(SESSION_AREA) != AREA_IT)
                {
                    strWhere += "and (" + strTableBase + "." + sf(SYSTEM_FIELD_ID_AREA) + " = " + this.GetSession().GetInt(SESSION_AREA) + " or " + strTableBase +  "." + sf(SYSTEM_FIELD_ID_AREA) + " = " + AREA_IT + ")" + lb;
                }
            }
            
            /*
             * Filter according to the screen
             */            
            if (CENTData1 != null)
            {    
                if (CENTData1.GetInt(SYSTEM_FIELD_ID) != Integer.MIN_VALUE && CENTData1.GetInt(SYSTEM_FIELD_ID) != 0)
                {
                    strWhere += " and " + strTableBase + "." + sf(SYSTEM_FIELD_ID) + " = " + this.PrepareNumber(CENTData1.GetInt(SYSTEM_FIELD_ID)) + lb;
                }

                if (CENTData1.GetInt(SYSTEM_FIELD_MATCH_ID_STATUS) != Integer.MIN_VALUE && CENTData1.GetInt(SYSTEM_FIELD_MATCH_ID_STATUS) != 0)
                {
                    strWhere += " and " + strTableBase + "." + sf(SYSTEM_FIELD_MATCH_ID_STATUS) + " = " + this.PrepareNumber(CENTData1.GetInt(SYSTEM_FIELD_MATCH_ID_STATUS)) + lb;
                }                
                
                for (int i=1; i<= TABLE_FIELD_COUNT; i++)
                {
                    if (CENTData1.GetInt(i) != Integer.MIN_VALUE)
                    {
                        strWhere += "and " + strTableBase + (".int_" + i) + " = " + CENTData1.GetInt(i) + lb;
                    }

                    if (!CENTData1.GetText(i).equals(""))
                    {
                        strWhere += "and " + strTableBase + ".text_" + i + " like " + this.PrepareString(CENTData1.GetText(i)) + lb;
                    }

                    if (CENTData1.GetDate(i) != null)
                    {
                        strWhere += "and " + strTableBase + ".date_" + i + " = " + this.PrepareDate(CENTData1.GetDate(i)) + lb;
                    }

                    if (CENTData1.GetDouble(i) != Double.MIN_VALUE)
                    {
                        strWhere += "and " + strTableBase + ".double_" + i + " = " + CENTData1.GetDouble(i) + lb;
                    }

                    if (CENTData1.GetBoolean(i) != Null)
                    {
                        strWhere += "and " + strTableBase + ".boolean_" + i + " = " + CENTData1.GetBoolean(i) + lb;
                    }
                }
            }            

            /*
             * System fields filtering
             */            
            if (!CENTData1.GetText(SYSTEM_FIELD_DATA_SOURCE).equals(""))
            {
                strWhere += "and " + strTableBase + "." + sf(SYSTEM_FIELD_DATA_SOURCE) + " = " + this.PrepareString(CENTData1.GetText(SYSTEM_FIELD_DATA_SOURCE)) + lb;
            }

            if (CENTData1.GetDate(SYSTEM_FIELD_SYSTEM_DATE) != null)
            {
                strWhere += "and " + strTableBase + "." + sf(SYSTEM_FIELD_SYSTEM_DATE) + " = " + this.PrepareDate(CENTData1.GetDate(SYSTEM_FIELD_SYSTEM_DATE)) + lb;
            }
                                
            /*
             * Finalize
             */           
            
            strSql = Select();
            
            switch (intIdSqlPart)
            {
                case SQL_PART_ALL:
                    
                    strSql += strFieldList;
                    strSql += strFrom;
                    strSql += strJoin;
                    strSql += strWhere;
                    strSql += strAnd;
                    
                    if (blnControl == true)
                    {
                        if (!strGroupBy.trim().equals(""))
                            strSql += "group by " + strGroupBy + lb;
                    }
                    
                    if (!strOrderBy.trim().equals(""))
                    {
                        if (!strOrderBy.trim().equals(""))                        
                            strSql += "order by " + strOrderBy + lb;
                    }
                    else
                    {
                        strSql += "order by 1" + lb;                        
                    }
                    
                    break;                

                case SQL_PART_FIELD_LIST_NO_ALIAS:
                    
                    strSql = strField;
                    break;
                    
                case SQL_PART_FIELD_LIST:
                    
                    strSql = strFieldList;
                    break;

                case SQL_PART_FROM:
                    
                    strSql = strFrom;
                    break;
                    
                case SQL_PART_JOIN:
                    
                    strSql = strJoin;
                    break;
                    
                case SQL_PART_WHERE:            
                    
                    strSql = strWhere;
                    break;

                case SQL_PART_AND:                
                    
                    strSql = strAnd;
                    break;                    
                    
                case SQL_PART_GROUP_BY:          
                    
                    if (!strGroupBy.trim().equals(""))
                    {
                        strSql += strGroupBy;
                    }
                    
                    break;
                    
                case SQL_PART_ORDER_BY:
                    
                    if (!strOrderBy.trim().equals(""))
                    {
                        strSql += strOrderBy;
                    }

                    break;
            }

            /*
             * Return the list
             */
            System.out.println("SQL Query: " + lb + strSql);
            return strSql;            

        }
        catch (CENTException CENTException1)
        {
            throw CENTException1;
        }         
        catch (Exception Exception1)
        {
            throw new CENTException("EXCEPTION_FAIL_TO_GETLIST", Exception1.getMessage());
        } 
        finally
        {
            strAnd = null;
            strSql = null;
            strFieldObject = null;
            strFieldObjectDisplay = null;
            strFieldList = null;
            strFrom = null;
            strJoin = null;
            strAlias = null;
            strWhere = null;
            strGroupBy = null;
            strOrderBy = null;
            strValue = null;
            strTableAlias = null;
            strTableBase = null;
            strField = null;
            strDomainName = null;
            strFieldName = null;
            strKey = null;        

            CENTAlias1 = null;
            ListCENTAlias1 = null;
            ListCENTCatalogMatch1 = null;
            ListCENTCatalogMatchItem1 = null;
            ListCENTCatalogDomain1 = null;
            ListCENTReconcileStep1 = null;
            ListCENTReconcileStepRule1 = null;            
        }
    }    

    protected String GetSqlQueryFromView(List<CENTData> ListCENTView1, CENTData CENTData1) throws CENTException
    {
        /*
         * General Declaration
         */
        String strSql = "";
                
        try
        {                       
            /*
             * Return the fields name to query the module catalog and data
             */
            if (ListCENTView1 == null)
            {
                ListCENTView1 = this.GetView(this.GetIdView());
            }

            strSql = "";
            strSql += "select" + lb; 
            strSql += "t1.*" + lb; 
            strSql += "from" + lb; 
            strSql += "(" + lb;                     
            strSql += Select() + lb; 
            strSql += "t2.*" + lb;
            strSql += ", " + RowId() + lb;
            strSql += "from" + lb;                                    
            strSql += "(" + lb;
            strSql += this.TransformViewToQuery(ListCENTView1, CENTData1, SQL_PART_ALL);
            strSql += ") t2" + lb;  
            strSql += ") t1" + lb;  
            
            /*
             * Return the list
             */
            return strSql;            

        } 
        catch (CENTException CENTException1)
        {
            throw CENTException1;
        }         
        catch (Exception Exception1)
        {
            throw new CENTException("EXCEPTION_FAIL_TO_GETLIST", Exception1.getMessage());
        } 
        finally
        {
            strSql = null;
            ListCENTView1 = null;
        }
    }
    
    private String GetSqlQueryFromCatalog(CENTData CENTData1) throws CENTException, SQLException
    {
        /*
         * General Declaration
         */       
        String strSql = "";        
        String strField = "";
        String strFieldList = "";
        String strFrom = "";
        String strTable = "";
        String strWhere = "";
        String strOrderBy = "";
        
        List<CENTData> ListCENTData1 = null;
                           
        try
        {
            /*
             * Create the Objects
             */            
            ListCENTData1 = new ArrayList<CENTData>();

            /*
             * Return the fields name to query the module catalog and data
             */
            ListCENTData1 = this.GetCatalog(this.GetIdTransaction());
            
            /*
             * Prepare the select
             */                
            for (CENTData CENTData2 : ListCENTData1)
            {                                                      
                strField = CENTData2.GetText(FIELD_OBJECT);

                if (isSystemField(strField) || strField.equals(SYSTEM_FIELD_ID))
                    strFieldList += "tb_1." + sf(strField) + "," + lb;
                else
                    strFieldList += "tb_1." + strField + "," + lb;
            }

            /*
             * Last selected field            
             */
            strFieldList += "tb_1." + sf(SYSTEM_FIELD_ID_COMPANY) + lb;
            
            /*
             * From clause
             */            
            strTable = this.GetTable(this.GetIdTransaction());
            strFrom = "from " + strTable + " tb_1" + lb;

            /*
             * Where clause
             */            
            strWhere += " where tb_1." + sf(SYSTEM_FIELD_ID_COMPANY) + " = " + this.GetSession().GetInt(SESSION_COMPANY) + lb;            

            if (CENTData1.GetInt(SYSTEM_FIELD_ID) != Integer.MIN_VALUE && CENTData1.GetInt(SYSTEM_FIELD_ID) != 0)
            {
                strWhere += " and tb_1." + sf(SYSTEM_FIELD_ID) + " = " + this.PrepareNumber(CENTData1.GetInt(SYSTEM_FIELD_ID)) + lb;
            }                        
            
            /*
             * Access control by area
            */
            if (this.GetIdTransaction() != TRN_VIEW) 
            {
                if (this.GetSession().GetInt(SESSION_AREA) != AREA_IT)
                {
                    if (this.GetSession().GetInt(SESSION_AREA) != Integer.MIN_VALUE) // Occurs on login
                    {
                        strWhere += "and (" + "tb_1" + "." + sf(SYSTEM_FIELD_ID_AREA) + " = " + this.GetSession().GetInt(SESSION_AREA) + " or " + "tb_1" +  "." + sf(SYSTEM_FIELD_ID_AREA) + " = " + AREA_IT + ")" + lb;
                    }        
                }
            }
            
            
            if (CENTData1 != null)
            {
                for (int i=1; i<= TABLE_FIELD_COUNT; i++)
                {
                    if (CENTData1.GetInt(i) != Integer.MIN_VALUE)
                    {
                        strWhere += " and tb_1." + ("int_" + i) + " = " + this.PrepareNumber(CENTData1.GetInt(i)) + lb;
                    }

                    if (!CENTData1.GetText(i).equals(""))
                    {
                        strWhere += " and tb_1." + ("text_" + i) + " = " + this.PrepareString(CENTData1.GetText(i)) + lb;
                    }                

                    if (CENTData1.GetDate(i) != null)
                    {
                        strWhere += " and tb_1." + ("date_" + i) + " = " + this.PrepareDate(CENTData1.GetDate(i)) + lb;
                    }                

                    if (CENTData1.GetDouble(i) != Double.MIN_VALUE)
                    {
                        strWhere += " and tb_1." + ("double_" + i) + " = " + this.PrepareNumber(CENTData1.GetDouble(i)) + lb;
                    }            

                    if (CENTData1.GetBoolean(i) != Null)
                    {
                        strWhere += " and tb_1." + ("boolean_" + i) + " = " + CENTData1.GetBoolean(i) + lb;
                    }                
                }
            }
            
            /*
             * Negative fields are specific for business rules
             */            
            if (!CENTData1.GetText(SYSTEM_FIELD_DATA_SOURCE).equals(""))
            {
                strWhere += " and tb_1." + sf(SYSTEM_FIELD_DATA_SOURCE) + " like " + this.PrepareString(CENTData1.GetText(SYSTEM_FIELD_DATA_SOURCE)) + lb;
            }

            if (CENTData1.GetDate(SYSTEM_FIELD_SYSTEM_DATE) != null)
            {
                strWhere += " and tb_1." + sf(SYSTEM_FIELD_SYSTEM_DATE) + " = " + this.PrepareDate(CENTData1.GetDate(SYSTEM_FIELD_SYSTEM_DATE)) + lb;
            }            

            /*
             * Order by
             */
            strOrderBy = "order by tb_1." + sf(SYSTEM_FIELD_ID);
            
            /*
             * Prepare the final sql statement
             */
            strSql = "select " + lb + strFieldList + strFrom + strWhere + strOrderBy;
            
            /*
             * Return the list
             */
            return strSql;

        } 
        catch (CENTException CENTException1)
        {
            throw CENTException1;
        }         
        catch (Exception Exception1)
        {
            throw new CENTException("EXCEPTION_FAIL_TO_GETLIST", Exception1.getMessage());
        } 
        finally
        {
            strSql = null;
            strField = null;
            strFieldList = null;
            strFrom = null;
            strTable = null;
            strWhere = null;
            strOrderBy = null;
        
            ListCENTData1 = null;
        }
    }                 

    public int GetLastIdByTransaction(int intIdTable) throws Exception
    {
        /*
         * General Declaration
         */        
        int intId = 0;
        String strSql = "";
        Statement Statement1 = null;
        ResultSet ResultSet1 = null;
        
        try
        {
            /*
             * Generate the final sql 
             */
            strSql = "";
            strSql += "select" + lb;            
            strSql += "max(" + sf(SYSTEM_FIELD_ID) + ")" + lb;
            strSql += "from " + this.GetTable(intIdTable) + lb;
            strSql += "where " + sf(SYSTEM_FIELD_ID_COMPANY) + " = " + this.PrepareNumber(this.GetSession().GetInt(SESSION_COMPANY)) + lb;

            /*
             * Prepare the statement
             */
            Statement1 = Connection1.createStatement();
            ResultSet1 = Statement1.executeQuery(strSql);
            
            /*
             * Get the view dictionary
             */
            while (ResultSet1.next())
            {
                intId = ResultSet1.getInt(1);
            }
            
            /*
             * Close sql related objects
             */
            if (Statement1 != null)
                Statement1.close();

            if (ResultSet1 != null)
                ResultSet1.close();                        
            
            return intId;
        }
        catch (CENTException CENTException1)
        {
            throw CENTException1;
        }        
        catch (Exception Exception1)
        {
            throw Exception1;
        }
        finally
        {
            strSql = "";
            Statement1 = null;
            ResultSet1 = null;
        }        
    }        

    protected String GetCondition(int intIdTransaction, int intIdCommand, int intFieldType, int intIdOperator, String strFieldObject, String strTableAlias, String strDomainName, String strValue) throws Exception
    {
        /*
         * General Declaration
         */        
        int intSize = 0;        
        Date datDate;
        String[] arrString;
        String strFirstChar = "";
        String strLastChar = "";        
        String strAnd = "";
        String strOperator = "";
        String strCondition = "";  
        String strFieldObjectCondition = "";
        
        try
        {
            /*
             * Predefined where/and clause
             */
            if (intIdCommand == COMMAND_CONDITION_AND || intIdCommand == COMMAND_CONDITION_OR)
            {                    
                if (!strFieldObject.contains(SYSTEM_FIELD_PAGING_START) && !strFieldObject.contains(SYSTEM_FIELD_PAGING_END)) // Discard paging at this level
                {
                    /*
                     * Define the condition (and/or)
                     */
                    switch (intIdCommand)
                    {                
                        case COMMAND_CONDITION_AND:
                            strOperator = "and ";
                            break;

                        case COMMAND_CONDITION_OR:
                            strOperator = "or ";
                            break;                            
                    }     

                    /*
                     * Define the operator
                     */                
                    if (intIdOperator >= OPERATOR_EQUALS && intIdOperator <= OPERATOR_NOT_LIKE)
                    {
                        switch (intIdOperator)
                        {
                            case OPERATOR_EQUALS:
                                strCondition = " = ";
                                break;

                            case OPERATOR_NOT_EQUALS:
                                strCondition = " <> ";
                                break;                                

                            case OPERATOR_GREATER:
                                strCondition = " > ";
                                break;      

                            case OPERATOR_GREATER_EQUALS:
                                strCondition = " >= ";
                                break;

                            case OPERATOR_SMALLER:
                                strCondition = " < ";
                                break;      

                            case OPERATOR_SMALLER_EQUALS:
                                strCondition = " <= ";
                                break;

                            case OPERATOR_IN:
                                strCondition = " in ";
                                break;

                            case OPERATOR_NOT_IN:
                                strCondition = " not in ";
                                break;                                                            
                                
                            case OPERATOR_LIKE:
                                strCondition = " like ";
                                break;
                                
                            case OPERATOR_NOT_LIKE:
                                strCondition = " not like ";
                                break;                                

                        }

                        strFieldObjectCondition = strFieldObject;
                        
                        /*
                         * Apply the condition
                         */
                        switch (intFieldType)
                        {
                            case TYPE_INT:
                            case TYPE_BOOLEAN:
                                
                                strAnd += strOperator;
                                
                                if (!strTableAlias.equals(""))
                                {
                                    strAnd += strTableAlias + ".";
                                }

                                switch (intIdOperator)
                                {
                                    case OPERATOR_IN:
                                    case OPERATOR_NOT_IN:

                                        strAnd += strFieldObjectCondition + strCondition + "(" + this.PrepareNumber(strValue) + ")";
                                        break;

                                    case OPERATOR_LIKE:
                                    case OPERATOR_NOT_LIKE:
                                        
                                        strValue = "%" + strValue + "%";
                                        strAnd += strFieldObjectCondition + strCondition + "'" + this.PrepareNumber(strValue) + "'";
                                        break;
                                        
                                    default:
                                        
                                        strAnd += strFieldObjectCondition + strCondition + this.PrepareNumber(strValue);                                        
                                }
                                
                                break;


                            case TYPE_TEXT:

                                if (!strDomainName.equals(""))
                                {
                                    strFieldObjectCondition = "int_2";
                                }

                                if (this.GetIdTransaction() != 0)
                                {
                                    if (intIdTransaction != this.GetIdTransaction())
                                    {
                                        //strFieldObjectCondition = SYSTEM_FIELD_ID;
                                    }
                                }

                                strAnd += strOperator;
                                
                                if (!strTableAlias.equals(""))
                                {
                                    strAnd += strTableAlias + ".";
                                }
                                
                                switch (intIdOperator)
                                {
                                    case OPERATOR_IN:
                                    case OPERATOR_NOT_IN:

                                        arrString = strValue.split(",");
                                        strValue = "";
                                        
                                        for (int i=0; i<=arrString.length-1; i++)
                                        {
                                            if (i > 0) 
                                                strValue += ",";
                                            
                                            strValue += this.PrepareString(arrString[i]);
                                        }
                                        
                                        strAnd += strFieldObjectCondition + strCondition + "(" + strValue + ")";
                                        break;
                                        
                                    case OPERATOR_LIKE:
                                    case OPERATOR_NOT_LIKE:

                                        strValue = "%" + strValue + "%";
                                        strAnd += strFieldObjectCondition + strCondition + "(" + this.PrepareString(strValue) + ")";
                                        break;
                                        
                                    default:
                                        
                                        /*
                                         * Star at the beggining or end of value indicate we must use like
                                         */                                      
                                        intSize = strValue.trim().length();                                        
                                        strFirstChar = strValue.substring(0, 1);
                                        strLastChar = strValue.substring(intSize-1, intSize);
                                        strValue = strValue.trim();

                                        if (strFirstChar.equals("*") || strLastChar.equals("*"))
                                        {
                                            strCondition = " like ";
                                            strValue = strValue.replace("*", "%");
                                        }                                        
                                                                                                                        
                                        strAnd += strFieldObjectCondition + strCondition + this.PrepareString(strValue);
                                }
                                
                                break;                                
                                
                            case TYPE_DOUBLE:
                                
                                strAnd += strOperator;
                                
                                if (!strTableAlias.equals(""))
                                {
                                    strAnd += strTableAlias + ".";
                                }
                                
                                switch (intIdOperator)
                                {
                                    case OPERATOR_IN:
                                    case OPERATOR_NOT_IN:
                                    case OPERATOR_LIKE:
                                    case OPERATOR_NOT_LIKE:

                                        strAnd += strFieldObjectCondition + strCondition + "(" + this.PrepareNumber(strValue) + ")";
                                        break;
                                        
                                    default:
                                        
                                        strValue = DoubleToString(StringToDouble(strValue), "0,00000000");
                                        strAnd += strFieldObjectCondition + strCondition + this.PrepareNumber(strValue);
                                }
                                
                                break;

                            case TYPE_DATE:                                        
                                
                                strAnd += strOperator;
                                
                                if (!strTableAlias.equals(""))
                                {
                                    strAnd += strTableAlias + ".";
                                }                                
                                
                                switch (intIdOperator)
                                {
                                    case OPERATOR_IN:
                                    case OPERATOR_NOT_IN:

                                        arrString = strValue.split(",");
                                        strValue = "";

                                        for (int i=0; i<=arrString.length-1; i++)
                                        {
                                            if (i > 0) 
                                                strValue += ",";
                                            
                                            strValue += this.PrepareDate(arrString[i]);
                                        }
                                        
                                        strAnd += strFieldObjectCondition + strCondition + "(" + strValue + ")";
                                        break;
                                        
                                    case OPERATOR_LIKE:
                                    case OPERATOR_NOT_LIKE:

                                        strValue = "%" + strValue + "%";
                                        strAnd += strFieldObjectCondition + strCondition + "(" + this.PrepareString(strValue) + ")";
                                        break;
                                        
                                    default:
                                        
                                        strAnd += strFieldObjectCondition + " " + strCondition + " " + this.PrepareDate(StringToDate(strValue));
                                }                                
                                
                                break;                                                                            
                        }
                    } 
                }
            }
    
            return strAnd + lb;

        }
        catch (CENTException CENTException1)
        {
            throw CENTException1;
        }        
        catch (Exception Exception1)
        {
            throw Exception1;
        }
        finally
        {
            datDate = null;
            arrString = null;
            strFirstChar = null;
            strLastChar = null;
            strAnd = null;
            strOperator = null;
            strCondition = null;
            strFieldObjectCondition = null;
        }
    }
    
    
}
