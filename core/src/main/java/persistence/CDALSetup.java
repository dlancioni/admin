package persistence;

import connection.ConnectionFactory;
import entity.CENTData;
import entity.CENTException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/*
 * Create the system structure, table and all field definiton
 */
public class CDALSetup extends CDAL
{
    public CDALSetup(Connection Connection1, CENTData CENTSession1) throws SQLException
    {
        super(Connection1, CENTSession1);
    }
    
    /*
     * Public Methods
     */
    public void CreateSystemTable() throws CENTException, SQLException
    {
        /*
         * General Declaration
         */
        int i = 0;
        
        String strSql = "";
        String strTable = "";

        List<String> ListString1 = null;
        PreparedStatement PreparedStatement1 = null;
        ConnectionFactory ConnectionFactory1 = new ConnectionFactory();

        try
        {
            /*
             * Create system tables
             */            
            for (i=1; i<=TRN_COUNT; i++)
            {
                strTable = "system_" + i;

                try
                {                
                    strSql = "";                    
                    
                    switch (ConnectionFactory1.GetCurrentSGDB())
                    {                                
                        case SERVER_ORACLE:
                            strSql = "drop table " + strTable;                            
                            break;
                            
                        case SERVER_SQL_SERVER:
                            strSql = "if exists (select name from sysobjects where name = '"  + strTable + "') drop table " + strTable + ";";
                            break;                            
                            
                        case SERVER_POSTGRE:                            
                            strSql = "drop table if exists " + strTable + ";";
                            break;
                    }

                    PreparedStatement1 = this.GetConnection().prepareStatement(strSql);
                    PreparedStatement1.executeUpdate();    
                    PreparedStatement1.close();
                    
                }
                catch (Exception Exception1)
                {
                    System.out.println(Exception1.getMessage());
                }        

                
                strSql = "";
                strSql += "create table " + strTable + lb;
                strSql += "(" + lb;

                /*
                 * Mapping fields
                 */                                
                for (int j=1; j<=20; j++)
                {
                    strSql += "int_" + j + " int," + lb;
                    strSql += "text_" + j + " varchar(500)," + lb;
                    strSql += "date_" + j + " date," + lb;
                    strSql += "double_" + j + " float," + lb;
                    strSql += "boolean_" + j + " int," + lb;
                }
                
                /*
                 * System fields
                 */                                
                for (int j=1; j<=20; j++)
                {
                    strSql += sf("_int_" + j) + " int," + lb;
                    strSql += sf("_text_" + j) + " varchar(500)," + lb;
                    strSql += sf("_date_" + j) + " date," + lb;
                    strSql += sf("_double_" + j) + " decimal," + lb;
                    strSql += sf("_boolean_" + j) + " int," + lb;
                }
                
                strSql += sf("temp_1") + " int" + lb;

                strSql += ")";

                strSql = strSql.toLowerCase();

                PreparedStatement1 = this.GetConnection().prepareStatement(strSql);
                PreparedStatement1.executeUpdate();                
                PreparedStatement1.close();

            }

            /*
             * Create user tables
             */            
            for (i=1; i<=10; i++)
            {
                strTable = "user_" + i;

                try
                {                
                    strSql = "";                    
                    
                    switch (ConnectionFactory1.GetCurrentSGDB())
                    {                                
                        case SERVER_ORACLE:
                            strSql = "drop table " + strTable;                            
                            break;
                            
                        case SERVER_SQL_SERVER:
                            strSql = "if exists (select name from sysobjects where name = '"  + strTable + "') drop table " + strTable + ";";
                            break;                            
                            
                        case SERVER_POSTGRE:
                            strSql = "drop table if exists " + strTable + ";";
                            break;
                    }

                    PreparedStatement1 = this.GetConnection().prepareStatement(strSql);
                    PreparedStatement1.executeUpdate();    
                    PreparedStatement1.close();
                    
                }
                catch (Exception Exception1)
                {
                    System.out.println(Exception1.getMessage());
                }        

                
                strSql = "";
                strSql += "create table " + strTable + lb;
                strSql += "(" + lb;

                /*
                 * Mapping fields
                 */                                
                for (int j=1; j<=20; j++)
                {
                    strSql += "int_" + j + " int," + lb;
                    strSql += "text_" + j + " varchar(500)," + lb;
                    strSql += "date_" + j + " date," + lb;
                    strSql += "double_" + j + " float," + lb;
                    strSql += "boolean_" + j + " int," + lb;
                }
                
                /*
                 * System fields
                 */                                
                for (int j=1; j<=20; j++)
                {
                    strSql += sf("_int_" + j) + " int," + lb;
                    strSql += sf("_text_" + j) + " varchar(500)," + lb;
                    strSql += sf("_date_" + j) + " date," + lb;
                    strSql += sf("_double_" + j) + " decimal," + lb;
                    strSql += sf("_boolean_" + j) + " int," + lb;
                }
                
                strSql += sf("temp_1") + " int" + lb;

                strSql += ")";

                strSql = strSql.toLowerCase();

                PreparedStatement1 = this.GetConnection().prepareStatement(strSql);
                PreparedStatement1.executeUpdate();                
                PreparedStatement1.close();

            }            
            
            
            
            
            
            
            

        } 
        catch (SQLException SQLException1)
        {                
            throw new CENTException("EXCEPTION_FAIL_TO_IMPLEMENT_SYSTEM_INSTANCE", SQLException1.getMessage());
        }         
        catch (Exception Exception1)
        {           
            throw Exception1;
        } 
        finally
        {
            PreparedStatement1 = null;
            ConnectionFactory1 = null;
        }        
    }   
    
    /*
     * Setup scenarios
     */               
    public List<CENTData> GetMenu(int intId, int intIdParent) throws CENTException, SQLException
    {
        /*
         * General Declaration
         */
        String strSql = "";
        ResultSet ResultSet1 = null;
        Statement Statement1 = null;
        CENTData CENTData1 = null;
        List<CENTData> ListCENTData1 = null;
        List<CENTData> ListCENTCatalogMenu1 = null;

        try
        {
            /*
             * Get related catalogs
             */            
            
            ListCENTCatalogMenu1 = this.GetCatalog(TRN_MENU);
            
            /*
             * Prepare the statement
             */
            Statement1 = this.GetConnection().createStatement();
            
            /*
             * Prepare the query
             */            
            strSql += " select distinct " + lb;

            strSql += " tb_2." + sf(GetFieldObject("id", ListCENTCatalogMenu1)) + "," + lb;    // id_menu
            strSql += " tb_2." + GetFieldObject("id_parent", ListCENTCatalogMenu1) + "," + lb;
            strSql += " tb_2." + GetFieldObject("name", ListCENTCatalogMenu1) + "," + lb;
            strSql += " tb_2." + GetFieldObject("position", ListCENTCatalogMenu1) + lb;

            strSql += " from" + lb; 
            strSql += " " + this.GetTable(TRN_TRANSACTION) + " tb_1" + lb;
            
            strSql += " inner join " + this.GetTable(TRN_MENU) + " tb_2" + lb;
            strSql += " on tb_1." + sf(SYSTEM_FIELD_ID_COMPANY) + " = tb_2." + sf(SYSTEM_FIELD_ID_COMPANY) + lb;
    
            strSql += " inner join " + this.GetTable(TRN_PROFILE_TRANSACTION) + " tb_3" + lb;
            strSql += " on tb_1." + sf(SYSTEM_FIELD_ID_COMPANY) + " = tb_3." + sf(SYSTEM_FIELD_ID_COMPANY) + lb;
            strSql += " and tb_1." + sf(SYSTEM_FIELD_ID) + " = tb_3.int_3" + lb;
   
            strSql += " inner join " + this.GetTable(TRN_USER) + " tb_4" + lb;
            strSql += " on tb_3." + sf(SYSTEM_FIELD_ID_COMPANY) + " = tb_4." + sf(SYSTEM_FIELD_ID_COMPANY) + lb;    
            strSql += " and tb_3.int_2 = tb_4.int_2" + lb;

            strSql += " and tb_4.text_1 = " + this.PrepareString(this.GetSession().GetText(SESSION_USER)) + lb;

            strSql += " where" + lb;
            strSql += " tb_1." + sf(SYSTEM_FIELD_ID_COMPANY) + " = " + this.GetSession().GetInt(SESSION_COMPANY) + lb;
            
            if (intId > 0)
            {
                strSql += " and" + lb;
                strSql += " tb_2." + GetFieldObject("id", ListCENTCatalogMenu1) + " = " + intId + lb;                
            }

            if (intIdParent > 0)
            {
                strSql += " and" + lb;
                strSql += " tb_2." + GetFieldObject("id_parent", ListCENTCatalogMenu1) + " = " + intIdParent + lb;
            }            
            
            strSql += " order by tb_2.int_3" + lb; // Position

            /*
             * Execute the statement
             */
            ListCENTData1 = new ArrayList<CENTData>();
            ResultSet1 = Statement1.executeQuery(strSql);
                        
            while (ResultSet1.next())
            {            
                CENTData1 = new CENTData();

                CENTData1.SetInt(GetFieldObject("id", ListCENTCatalogMenu1), ResultSet1.getInt(1));
                CENTData1.SetInt(GetFieldObject("id_parent", ListCENTCatalogMenu1), ResultSet1.getInt(2));
                CENTData1.SetText(GetFieldObject("name", ListCENTCatalogMenu1), ResultSet1.getString(3));

                ListCENTData1.add(CENTData1);                    

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
            return ListCENTData1;
            
        } 
        catch (Exception Exception1)
        {
            throw new CENTException("EXCEPTION_FAIL_GETDATA", Exception1.getMessage());
        } 
        finally
        {
            Statement1 = null;
            ResultSet1 = null;            
            strSql = null;
            ListCENTData1 = null;
            ListCENTCatalogMenu1 = null;
            CENTData1 = null;
        }
    } 
    
    public List<CENTData> GetTransaction(int intIdMenu) throws CENTException, SQLException
    {
        /*
         * General Declaration
         */
        String strSql = "";
        ResultSet ResultSet1 = null;
        Statement Statement1 = null;
        List<CENTData> ListCENTData1 = null;
        List<CENTData> ListCENTCatalogTransaction1 = null;
        List<CENTData> ListCENTCatalogProfileTransaction1 = null;
        List<CENTData> ListCENTCatalogUser1 = null;

        try
        {           
            ListCENTCatalogTransaction1 = this.GetCatalog(TRN_TRANSACTION);
            ListCENTCatalogProfileTransaction1 = this.GetCatalog(TRN_PROFILE_TRANSACTION);
            ListCENTCatalogUser1 = this.GetCatalog(TRN_USER);
            
            /*
             * Prepare the statement
             */
            Statement1 = this.GetConnection().createStatement();
            
            /*
             * Prepare the query
             */   
            strSql = "";
            strSql += " select distinct " + lb;

            strSql += " tb_1." + sf(GetFieldObject("id", ListCENTCatalogTransaction1)) + "," + lb; 
            strSql += " tb_1." + GetFieldObject("id_menu", ListCENTCatalogTransaction1) + "," + lb; 
            strSql += " tb_1." + GetFieldObject("name", ListCENTCatalogTransaction1) + "," + lb; 
            strSql += " tb_1." + GetFieldObject("page", ListCENTCatalogTransaction1) + "," + lb; 
            strSql += " tb_1." + GetFieldObject("id_type", ListCENTCatalogTransaction1) + lb; 

            strSql += " from" + lb; 
            strSql += " " + this.GetTable(TRN_TRANSACTION) + " tb_1" + lb;
    
            strSql += " inner join " + this.GetTable(TRN_PROFILE_TRANSACTION) + " tb_2" + lb;
            strSql += " on tb_1." + sf(SYSTEM_FIELD_ID_COMPANY) + " = tb_2." + sf(SYSTEM_FIELD_ID_COMPANY) + lb;
            strSql += " and tb_1." + sf(SYSTEM_FIELD_ID) + " = tb_2." + GetFieldObject("id_transaction", ListCENTCatalogProfileTransaction1) + lb;

            strSql += " inner join " + this.GetTable(TRN_PROFILE) + " tb_3" + lb;
            strSql += " on tb_2." + sf(SYSTEM_FIELD_ID_COMPANY) + " = tb_3." + sf(SYSTEM_FIELD_ID_COMPANY) + lb;    
            strSql += " and tb_2.int_2 = tb_3." + sf(SYSTEM_FIELD_ID) + lb;
            strSql += " and tb_3." + sf(SYSTEM_FIELD_ID) + " = " + this.GetSession().GetInt(SESSION_PROFILE) + lb;

            strSql += " inner join " + this.GetTable(TRN_USER) + " tb_4" + lb;
            strSql += " on tb_3." + sf(SYSTEM_FIELD_ID_COMPANY) + " = tb_4." + sf(SYSTEM_FIELD_ID_COMPANY) + lb;    
            strSql += " and tb_3." + sf(SYSTEM_FIELD_ID) + " = tb_4." + GetFieldObject("id_profile", ListCENTCatalogUser1) + lb;

            strSql += " and tb_4.text_1 = " + this.PrepareString(this.GetSession().GetText(SESSION_USER)) + lb;

            strSql += " where" + lb;
            strSql += " tb_1." + sf(SYSTEM_FIELD_ID_COMPANY) + " = " + this.GetSession().GetInt(SESSION_COMPANY) + lb;

            strSql += " and" + lb;
            strSql += " tb_1." + GetFieldObject("id_menu", ListCENTCatalogTransaction1) + " = " + intIdMenu + lb;
            
            /*
             * Access control by area
             */
            if (this.GetSession().GetInt(SESSION_AREA) != AREA_IT)
            {
                if (this.GetSession().GetInt(SESSION_AREA) != 0)
                {                
                    strSql += "and (" + "tb_1" + "." + sf(SYSTEM_FIELD_ID_AREA) + " = " + this.GetSession().GetInt(SESSION_AREA) + " or " + "tb_1" +  "." + sf(SYSTEM_FIELD_ID_AREA) + " = " + AREA_IT + ")" + lb;
                }            
            }
            
            strSql += " order by tb_1." + sf(SYSTEM_FIELD_ID) + lb;            
            
            /*
             * Execute the statement
             */
            ListCENTData1 = new ArrayList<CENTData>();
            ResultSet1 = Statement1.executeQuery(strSql);
                        
            while (ResultSet1.next())
            {            
                CENTSession1 = new CENTData();
                
                CENTSession1.SetInt(GetFieldObject("id", ListCENTCatalogTransaction1), ResultSet1.getInt(1));
                CENTSession1.SetInt(GetFieldObject("id_menu", ListCENTCatalogTransaction1), ResultSet1.getInt(2));
                CENTSession1.SetText(GetFieldObject("name", ListCENTCatalogTransaction1), ResultSet1.getString(3));
                CENTSession1.SetText(GetFieldObject("page", ListCENTCatalogTransaction1), ResultSet1.getString(4));
                CENTSession1.SetInt(GetFieldObject("id_type", ListCENTCatalogTransaction1), ResultSet1.getInt(5));

                ListCENTData1.add(CENTSession1);
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
            return ListCENTData1;
            
        } 
        catch (Exception Exception1)
        {
            throw new CENTException("EXCEPTION_FAIL_GETDATA", Exception1.getMessage());
        } 
        finally
        {
            strSql = null;
            ResultSet1 = null;
            Statement1 = null;
            ListCENTData1 = null;
            ListCENTData1 = null;
            ListCENTCatalogTransaction1 = null;
            ListCENTCatalogProfileTransaction1 = null;
            ListCENTCatalogUser1 = null;
        }
    }            

    public List<CENTData> GetListOfCompany() throws CENTException, SQLException
    {
        /*
         * General Declaration
         */
        String strSql = "";
        ResultSet ResultSet1 = null;
        Statement Statement1 = null;
        CENTData CENTData1 = null;
        List<CENTData> ListCENTData1 = null;

        try
        {           
            /*
             * Prepare the statement
             */
            Statement1 = this.GetConnection().createStatement();
            
            /*
             * Prepare the query
             */
            strSql += " select" + lb;
            strSql += " tb_1." + sf(SYSTEM_FIELD_ID) + "," + lb; 
            strSql += " tb_1.text_1" + lb; 
            strSql += " from" + lb; 
            strSql += " " + this.GetTable(TRN_COMPANY) + " tb_1" + lb;

            /*
             * Execute the statement
             */
            ListCENTData1 = new ArrayList<CENTData>();
            ResultSet1 = Statement1.executeQuery(strSql);
            
            while (ResultSet1.next())
            {            
                CENTData1 = new CENTData();
                
                CENTData1.SetInt(SYSTEM_FIELD_ID, ResultSet1.getInt(1));
                CENTData1.SetText(1, ResultSet1.getString(2));
                
                ListCENTData1.add(CENTData1);
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
            return ListCENTData1;
            
        } 
        catch (Exception Exception1)
        {
            throw new CENTException("EXCEPTION_FAIL_GETDATA", Exception1.getMessage());
        } 
        finally
        {
            Statement1 = null;
            ResultSet1 = null;            
            strSql = "";
            CENTData1 = null;
            ListCENTData1 = null;
        }
    }     

    public List<CENTData> GetPermission(CENTData CENTSession1) throws CENTException, SQLException
    {
        /*
         * General Declaration
         */
        String strSql = "";
        ResultSet ResultSet1 = null;
        Statement Statement1 = null;
        List<CENTData> ListCENTData1 = null;

        try
        {           
            /*
             * Prepare the statement
             */
            Statement1 = this.GetConnection().createStatement();
            
            /*
             * Prepare the query
             */            
            strSql += " select " + lb;

            strSql += " tb_3." + sf(SYSTEM_FIELD_ID) + "," + lb;
            strSql += " tb_3.text_1" + lb;

            strSql += " from" + lb;
            strSql += " " + this.GetTable(TRN_TRANSACTION_FUNCTION) + " tb_1" + lb;
    
            strSql += " inner join " + this.GetTable(TRN_TRANSACTION) + " tb_2" + lb;
            strSql += " on tb_1." + sf(SYSTEM_FIELD_ID_COMPANY) + " = tb_2." + sf(SYSTEM_FIELD_ID_COMPANY) + lb;
            strSql += " and tb_1.int_3 = tb_2." + sf(SYSTEM_FIELD_ID) + lb;
            strSql += " and tb_2." + sf(SYSTEM_FIELD_ID) + " = " + this.GetIdTransaction() + lb;

            strSql += " inner join " + this.GetTable(TRN_FUNCTION) + " tb_3" + lb;
            strSql += " on tb_1." + sf(SYSTEM_FIELD_ID_COMPANY) + " = tb_3." + sf(SYSTEM_FIELD_ID_COMPANY) + lb;
            strSql += " and tb_1.int_4 = tb_3." + sf(SYSTEM_FIELD_ID) + lb;   

            strSql += " inner join " + this.GetTable(TRN_PROFILE_TRANSACTION) + " tb_4" + lb;
            strSql += " on tb_1." + sf(SYSTEM_FIELD_ID_COMPANY) + " = tb_4." + sf(SYSTEM_FIELD_ID_COMPANY) + lb;
            strSql += " and tb_1.int_3 = tb_4.int_3" + lb;    // dvd

            strSql += " inner join " + this.GetTable(TRN_PROFILE) + " tb_5" + lb;
            strSql += " on tb_4." + sf(SYSTEM_FIELD_ID_COMPANY) + " = tb_5." + sf(SYSTEM_FIELD_ID_COMPANY) + lb;
            strSql += " and tb_4.int_2 = tb_5." + sf(SYSTEM_FIELD_ID) + lb;     
            strSql += " and tb_1.int_2 = tb_5." + sf(SYSTEM_FIELD_ID) + lb;     
            strSql += " and tb_4.int_2 = " + CENTSession1.GetInt(SESSION_PROFILE) + lb;

            strSql += " where" + lb;
            strSql += " tb_2." + sf(SYSTEM_FIELD_ID_COMPANY) + " = " + this.GetSession().GetInt(SESSION_COMPANY) + lb;

            /*
             * Execute the statement
             */
            ListCENTData1 = new ArrayList<CENTData>();
            ResultSet1 = Statement1.executeQuery(strSql);
                        
            while (ResultSet1.next())
            {            
                CENTSession1 = new CENTData();
                
                CENTSession1.SetInt(1, ResultSet1.getInt(1));
                CENTSession1.SetText(1, ResultSet1.getString(2));

                ListCENTData1.add(CENTSession1);
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
            return ListCENTData1;
            
        } 
        catch (Exception Exception1)
        {
            throw new CENTException("EXCEPTION_FAIL_GETDATA", Exception1.getMessage());
        } 
        finally
        {
            Statement1 = null;
            ResultSet1 = null;            
            strSql = null;
            ListCENTData1 = null;
        }
    }    
    
}
