package persistence;

import entity.CENTData;
import entity.CENTException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 *
 * @author David
 */
public class CDALReconcile extends CDAL
{
    public CDALReconcile(Connection Connection1, CENTData CENTSession1) throws SQLException
    {
        super(Connection1, CENTSession1);
    }
    
    public void CreateTemporaryTable() throws Exception
    {
        /*
         * General Declaration
         */
        int intNumberOfTemp = 10;
        String strDB = "";
        String strSql = "";
        PreparedStatement PreparedStatement1 = null;

        try
        {      
            /*
             * Get current database engine
             */
            strDB = this.GetPropertieValue("CONNECTION_DB_SERVER");

            /*
             * Create temporary table
             */            
            for (int i=1; i<=intNumberOfTemp; i++)
            {    
                switch (strDB)
                {
                    case SERVER_ORACLE:
                        
                        /*
                         * Drop temporary table
                         */                 
                        try
                        {
                            strSql = "drop table " + Temp("TMP" + i);
                            PreparedStatement1 = this.GetConnection().prepareStatement(strSql);
                            PreparedStatement1.execute();                
                            PreparedStatement1.close();
                        }
                        catch (Exception Exception1)
                        {
                            PreparedStatement1.close();                    
                            System.out.println(Exception1.getMessage());
                        }
                        
                        /*
                         * Create it
                         */                        
                        strSql = "";
                        strSql += " create global temporary table " + Temp("TMP" + i);
                        strSql += " as select * from system_1 where 1 = 2";

                        PreparedStatement1 = this.GetConnection().prepareStatement(strSql);
                        PreparedStatement1.execute();
                        PreparedStatement1.close();                                    
                        
                        break;

                    case SERVER_SQL_SERVER:

                        /*
                         * Drop temporary table
                         */
                        try
                        {
                            strSql = "drop table " + Temp("TMP" + i);
                            PreparedStatement1 = this.GetConnection().prepareStatement(strSql);
                            PreparedStatement1.execute();                
                            PreparedStatement1.close();
                        }
                        catch (Exception Exception1)
                        {
                            PreparedStatement1.close();                    
                            System.out.println(Exception1.getMessage());
                        }

                        /*
                         * Create it
                         */                        
                        strSql = "";
                        strSql += " select *";
                        strSql += " into " + Temp("TMP" + i);
                        strSql += " from system_1 where 1 = 2";

                        PreparedStatement1 = this.GetConnection().prepareStatement(strSql);
                        PreparedStatement1.execute();
                        PreparedStatement1.close();                          
                        
                        break;
                        
                    case SERVER_POSTGRE:
                        /*
                         * Drop temporary table
                         */                 
                        strSql = "drop table if exists " + Temp("TMP" + i);
                        PreparedStatement1 = this.GetConnection().prepareStatement(strSql);
                        PreparedStatement1.execute();                
                        PreparedStatement1.close();
                        
                        /*
                         * Create it
                         */                        
                        strSql = "";
                        strSql += " create temporary table " + Temp("TMP" + i) + " as select * from system_1 where 1 = 2";
                        PreparedStatement1 = this.GetConnection().prepareStatement(strSql);
                        PreparedStatement1.execute();
                        PreparedStatement1.close();                                    
                        
                        break;                                                
                }
            }

            
            
        }
        catch (SQLException SQLException1)
        {
            throw SQLException1;
        }        
        catch (Exception Exception1)
        {
            throw Exception1;
        }
        finally
        {
            strDB = null;
            strSql = null;
            PreparedStatement1 = null;
        }
    }        

    public void CreateIndex() throws Exception
    {
        /*
         * General Declaration
         */
        String strDB = "";
        String strSql = "";
        PreparedStatement PreparedStatement1 = null;
        int intNumberOfTemp = 8;
                            
        try
        {      
            /*
             * Get current database engine
             */
            strDB = this.GetPropertieValue("CONNECTION_DB_SERVER");

            /*
             * Create temporary table
             */            
            switch (strDB)
            {
                case SERVER_ORACLE:

                    break;

                case SERVER_SQL_SERVER:
                
                    break;

                case SERVER_POSTGRE:
                    
                    
                    for (int i=1; i<=intNumberOfTemp; i++)
                    {
                        /*
                         * Drop index
                         */                 
                        strSql = "drop index if exists " + Temp("idxid" + i);
                        PreparedStatement1 = this.GetConnection().prepareStatement(strSql);
                        PreparedStatement1.execute();
                        PreparedStatement1.close();
                        
                        /*
                         * Create index
                         */                 
                        strSql = "create index idxid" + i + " on " + Temp("TMP" + i) + " (" + SYSTEM_FIELD_ID + ")";
                        PreparedStatement1 = this.GetConnection().prepareStatement(strSql);
                        PreparedStatement1.execute();
                        PreparedStatement1.close();        
                        
                    }
    
                    break;                                                
            }            
            
        }
        catch (SQLException SQLException1)
        {
            throw SQLException1;
        }        
        catch (Exception Exception1)
        {
            throw Exception1;
        }
        finally
        {
            strDB = null;
            strSql = null;
            PreparedStatement1 = null;
        }
    }        
    
    public int GetStepData(int intIdProcess, int intIdTransaction, String strCode, int intIdView) throws Exception
    {
        /*
         * General Declaration
         */
        int intRowsAffected = 0;
        
        String strSql = "";
        String strAnd = "";
        String strDB = "";

        PreparedStatement PreparedStatement1 = null;
        List<CENTData> ListCENTView1 = null;        
        
        try
        {
            /*
             * Specific database commands
             */            
            strDB = this.GetPropertieValue("CONNECTION_DB_SERVER");
            
            /*
             * Get the view definition to prepare data
             */
            ListCENTView1 = this.GetView(intIdView);

            if (ListCENTView1.isEmpty())
            {
                throw new CENTException("EXCEPTION_VIEW_NOT_FOUND", "View Id: " + intIdView + ", Side: " + strCode);                
            }            

            for (CENTData CENTView1 : ListCENTView1)
            {               
                strAnd += GetCondition(CENTView1.GetInt(FIELD_ID_TRN), CENTView1.GetInt(FIELD_ID_COMMAND), CENTView1.GetInt(FIELD_TYPE), CENTView1.GetInt(FIELD_ID_OPERATOR), CENTView1.GetText(FIELD_OBJECT), "", CENTView1.GetText(FIELD_DOMAIN_NAME), CENTView1.GetText(FIELD_CONDITION_VALUE)) + lb;
            }           

            /*
             * Isolate data to avoid concurrence during multithreading
             */
            strSql = "";
            strSql += "insert into " + Temp(TEMPORARY_1) + lb;
            strSql += "(" + lb;
                strSql += sf(SYSTEM_FIELD_ID_COMPANY) + lb;
                strSql += "," + sf(SYSTEM_FIELD_ID_AREA) + lb;
                strSql += "," + sf(SYSTEM_FIELD_ID_PROFILE) + lb;
                strSql += "," + sf(SYSTEM_FIELD_ID_USER) + lb;
                strSql += "," + sf(SYSTEM_FIELD_ID_TRANSACTION) + lb;
                strSql += "," + sf(SYSTEM_FIELD_SYSTEM_DATE) + lb;
                strSql += "," + sf(SYSTEM_FIELD_DATA_SOURCE) + lb;
                strSql += "," + sf(SYSTEM_FIELD_ID_PROCESS) + lb;
                strSql += "," + sf(SYSTEM_FIELD_ID_MATCH) + lb;
                strSql += "," + sf(SYSTEM_FIELD_ID) + lb;
                strSql += "," + sf(SYSTEM_FIELD_PAGING_START) + lb;
                strSql += "," + this.GetFieldList(intIdTransaction, ListCENTView1, false);            
            strSql += ")" + lb;            

            /*
             * Field list
             */            
            strSql += Select() + lb;                    
            strSql += sf(SYSTEM_FIELD_ID_COMPANY) + lb;
            strSql += "," + sf(SYSTEM_FIELD_ID_AREA) + lb;
            strSql += "," + sf(SYSTEM_FIELD_ID_PROFILE) + lb;
            strSql += "," + sf(SYSTEM_FIELD_ID_USER) + lb;
            strSql += "," + sf(SYSTEM_FIELD_ID_TRANSACTION) + lb;
            strSql += "," + sf(SYSTEM_FIELD_SYSTEM_DATE) + lb;
            strSql += "," + sf(SYSTEM_FIELD_DATA_SOURCE) + lb;
            strSql += "," + sf(SYSTEM_FIELD_ID_PROCESS) + lb;
            strSql += "," + sf(SYSTEM_FIELD_ID_MATCH) + lb;
            strSql += "," + sf(SYSTEM_FIELD_ID) + lb;
            strSql += "," + RowId() + lb;                    
            strSql += "," + this.GetFieldList(intIdTransaction, ListCENTView1, false);

            /*
             * Data origin
             */            
            strSql += "from " + GetTable(intIdTransaction) + lb;
            
            /*
             * Basic condition to read from imported data
             */            
            strSql += "where " + sf(SYSTEM_FIELD_ID_COMPANY) + " = " + this.PrepareNumber(this.GetSession().GetInt(SESSION_COMPANY)) + lb;            
            strSql += "and " + sf(SYSTEM_FIELD_DATA_SOURCE) + " = " + this.PrepareString(strCode) + lb;
            strSql += "and (" + sf(SYSTEM_FIELD_ID_PROCESS) + " is null or " + sf(SYSTEM_FIELD_ID_PROCESS) + " = " + PrepareNumber(intIdProcess) + ")";                    
            
            /*
             * Condition when found
             */
            if (!strAnd.trim().equals(""))
            {
                strSql += strAnd.trim();
            }

            PreparedStatement1 = this.GetConnection().prepareStatement(strSql);
            intRowsAffected = PreparedStatement1.executeUpdate();
            PreparedStatement1.close();
            System.out.println("Row to reconcile on side " + strCode + ": " + intRowsAffected);
            
            /*
             * It's the number of records to be paged and reconciled
             */
            return intRowsAffected;

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
            strSql = null;
            strAnd = null;
            strDB = null;

            PreparedStatement1 = null;
            ListCENTView1 = null;
        }        
    }

    public int Match(int intIdProcess, int intIdReconcile, int intIdStep, int intIdRule, int intIdTransaction, int intIdMatchStatus, String strDataSource1, String strDataSource2, List<CENTData> ListCENTField1, List<String> ListFieldKey1, String strReconcile, String strStep, String strRule) throws Exception
    {
        /*
         * General Declaration
         */
        int i = 0;
        int j = 0;
        int intRowsAffected = 0;
        
        String strDB = "";
        String strSql = "";
        String strKey = "";
        String strMatchId = "";
        String strSide = "";
        
        PreparedStatement PreparedStatement1 = null;        
        List<CENTData> ListCENTCatalogMatch1 = null;
        List<CENTData> ListCENTCatalogMatchItem1 = null;
        
        try
        {
            /*
             * Keep current DB
             */
            strDB = this.GetPropertieValue("CONNECTION_DB_SERVER");
            
            /*
             * Get the field catalog
             */
            ListCENTCatalogMatch1 = this.GetCatalog(TRN_MATCH);
            ListCENTCatalogMatchItem1 = this.GetCatalog(TRN_MATCH_ITEM);
            
            /*
             * Trick the match id and orfan has no rule
             */
            if (intIdMatchStatus == MATCH_STATUS_ORFAN)
            {
                strMatchId = "99";
                intIdRule = 0;
                strSide = sf(SYSTEM_FIELD_DATA_SOURCE);
                strReconcile = "";
                strStep = "";
                strRule = "";                
            }
            else
            {
                strMatchId = String.valueOf(intIdRule);                
                strSide = "null";
            }            
            
            /*
             * Key used in rank
             */
            for (String strKeyField : ListFieldKey1)
            {
                if (j > 0)
                {
                    strKey += ", ";
                }
                
                strKey += strKeyField;
                j ++;
            }                        

            /*
             * Load data only at first rule, when matching. Handle the orphans at the end
             */            
            if (intIdMatchStatus == MATCH_STATUS_MATCHED)
            {
                /*
                 * Prepare data side 1
                 */
                strSql = "";
                strSql += "delete from " + Temp(TEMPORARY_7) + lb;                    
                PreparedStatement1 = this.GetConnection().prepareStatement(strSql);
                intRowsAffected = PreparedStatement1.executeUpdate();   
                PreparedStatement1.close();   
                System.out.println("Match found: " + intRowsAffected);

                strSql = "";
                strSql += " insert into " + Temp(TEMPORARY_7) + lb;
                strSql += " select *" + lb;
                strSql += " from " + Temp(TEMPORARY_1) + lb;
                strSql += " where " + sf(SYSTEM_FIELD_DATA_SOURCE) + " = " + this.PrepareString(strDataSource1) + lb;
                strSql += " order by " + lb;
                strSql += " " + sf(SYSTEM_FIELD_DATA_SOURCE) + lb;

                for (String strkeyField1 : ListFieldKey1)
                {
                    strSql += " , " + strkeyField1 + lb;
                }

                PreparedStatement1 = this.GetConnection().prepareStatement(strSql);
                intRowsAffected = PreparedStatement1.executeUpdate();   
                PreparedStatement1.close();   
                System.out.println("Match found: " + intRowsAffected);

                /*
                 * Prepare data side 2
                 */
                strSql = "";
                strSql += "delete from " + Temp(TEMPORARY_8) + lb;                    
                PreparedStatement1 = this.GetConnection().prepareStatement(strSql);
                intRowsAffected = PreparedStatement1.executeUpdate();   
                PreparedStatement1.close();   
                System.out.println("Match found: " + intRowsAffected);

                strSql = "";
                strSql += "insert into " + Temp(TEMPORARY_8) + lb;                    
                strSql += "select *" + lb;
                strSql += "from " + Temp(TEMPORARY_1) + lb;
                strSql += "where " + sf(SYSTEM_FIELD_DATA_SOURCE) + " = " + this.PrepareString(strDataSource2) + lb; 
                strSql += " order by " + lb;
                strSql += " " + sf(SYSTEM_FIELD_DATA_SOURCE) + lb;

                for (String strkeyField1 : ListFieldKey1)
                {
                    strSql += " , " + strkeyField1 + lb;
                }

                PreparedStatement1 = this.GetConnection().prepareStatement(strSql);
                intRowsAffected = PreparedStatement1.executeUpdate();   
                PreparedStatement1.close();   
                System.out.println("Match found: " + intRowsAffected);         


                /*
                 * Create index to speed up process
                 */                                            
                strSql = "";
                strSql += " create index idx1 on " + Temp(TEMPORARY_7) + lb;
                strSql += " (" + lb;
                strSql += " " + SYSTEM_FIELD_ID_COMPANY + lb;
                
                for (String strkeyField1 : ListFieldKey1)
                {
                    strSql += ", " + strkeyField1 + lb;
                }
                
                strSql += " ," + SYSTEM_FIELD_DATA_SOURCE + lb;                
                
                strSql += ")";
                
                PreparedStatement1 = this.GetConnection().prepareStatement(strSql);
                PreparedStatement1.execute();
                PreparedStatement1.close();        
                System.out.println("Index created at " + Temp(TEMPORARY_7));
                
                /*
                 * Create index to speed up process
                 */                
                strSql = "";
                strSql += " create index idx2 on " + Temp(TEMPORARY_8) + lb;
                strSql += " (" + lb;
                strSql += " " + SYSTEM_FIELD_ID_COMPANY + lb;
                
                for (String strkeyField1 : ListFieldKey1)
                {
                    strSql += ", " + strkeyField1 + lb;
                }
                
                strSql += " ," + SYSTEM_FIELD_DATA_SOURCE + lb;                
                
                strSql += ")";
                
                PreparedStatement1 = this.GetConnection().prepareStatement(strSql);
                PreparedStatement1.execute();
                PreparedStatement1.close();        
                System.out.println("Index created at " + Temp(TEMPORARY_8));                

                /*
                 * Join records
                 */
                for (i=1; i<=2; i++)
                {
                    strSql = "";                
                    strSql += " insert into " + Temp(TEMPORARY_5) + lb;
                    strSql += " select distinct " + lb;  // (distinct mandatory, query returns both side)

                    if (i == 1)
                        strSql += " tb_1.*" + lb;
                    else
                        strSql += " tb_2.*" + lb;

                    /*
                     * from clause
                     */
                    strSql += " from " + lb;

                    /*
                     * Join temps
                     */
                    strSql += " " + Temp(TEMPORARY_7) + " tb_1" + lb;
                    strSql += " inner join " + Temp(TEMPORARY_8) + " tb_2" + lb;                    

                    /*
                     * Add match keys to join
                     */                    
                    strSql += " on tb_1." + sf(SYSTEM_FIELD_ID_COMPANY) + " = " + "tb_2." + sf(SYSTEM_FIELD_ID_COMPANY) + lb;
                    
                    /*
                     * Add the key to match
                     */                    
                    for (String strKeyField : ListFieldKey1)
                    {
                        strSql += " and tb_1." + strKeyField + " = tb_2." + strKeyField + lb;
                    }
                    
                    /*
                     * Choose the datasource to get information
                     */
                    if (i == 1)
                        strSql += " and tb_1." + sf(SYSTEM_FIELD_DATA_SOURCE) + " = " + this.PrepareString(strDataSource1) + lb;
                    else
                        strSql += " and tb_2." + sf(SYSTEM_FIELD_DATA_SOURCE) + " = " + this.PrepareString(strDataSource2) + lb;                    
                    
                    /*
                     * Avoid records already computed
                     */                    
                    if (i == 1)
                        strSql += " where tb_1." + lb;
                    else
                        strSql += " where tb_2." + lb;

                    strSql += sf(SYSTEM_FIELD_ID) + " not in " + lb;
                    strSql += " ( " + lb;
                    strSql += "     select " + sf(SYSTEM_FIELD_ID) + " from " + Temp(TEMPORARY_4) + lb;
                    strSql += " ) " + lb;           
                    
                    PreparedStatement1 = this.GetConnection().prepareStatement(strSql);
                    intRowsAffected = PreparedStatement1.executeUpdate();   
                    PreparedStatement1.close();   
                    System.out.println("Match found at side: " + intRowsAffected);                    
                }
                
                /*
                 * Isolate the keys
                 */   
                strSql = "";
                strSql += "insert into " + Temp(TEMPORARY_6) + lb; 
                strSql += "(" + lb; 
                strSql += strKey + "," + lb;
                strSql += sf(SYSTEM_FIELD_ID_MATCH) + lb;
                strSql += ")" + lb;                 

                strSql += "select distinct" + lb;
                strSql += strKey + "," + lb;                
                
                switch (strDB)
                {
                    case SERVER_ORACLE:
                        strSql += strMatchId + " || rank() over (order by " + strKey + " asc, " + strKey + ")" + " id_match" + lb;
                        break;

                    case SERVER_SQL_SERVER:
                        strSql += "concat(" + strMatchId + ", rank() over (order by " + strKey + " asc, " + strKey + "))" + " id_match" + lb;
                        break;
                        
                    case SERVER_POSTGRE:
                        strSql += "cast(" + PrepareString(strMatchId) + " || rank() over (order by " + strKey + " asc, " + strKey + ") as integer)" + " id_match" + lb;
                        break;
                }

                strSql += "from " + Temp(TEMPORARY_5) + lb;                
                strSql += "order by " + lb;
                strSql += strKey + lb;

                PreparedStatement1 = this.GetConnection().prepareStatement(strSql);
                intRowsAffected = PreparedStatement1.executeUpdate();   
                PreparedStatement1.close();   
                System.out.println("Match item inserted: " + intRowsAffected);           

            }
            
            if (intIdMatchStatus == MATCH_STATUS_ORFAN)
            {
                /*
                 * After all rules, if the records exists in temp 1 and not in temp 5, it's  orphan
                 */
                strSql = "";                
                strSql += "insert into " + Temp(TEMPORARY_5) + lb;
                strSql += "select * from " + Temp(TEMPORARY_1) + lb;
                strSql += "where " + sf(SYSTEM_FIELD_ID) + " not in " + lb;
                strSql += "( " + lb;
                strSql += "     select " + sf(SYSTEM_FIELD_ID) + " from " + Temp(TEMPORARY_4) + lb;
                strSql += ") " + lb;                

                PreparedStatement1 = this.GetConnection().prepareStatement(strSql);
                intRowsAffected = PreparedStatement1.executeUpdate();   
                PreparedStatement1.close();   
                System.out.println("Orphan found: " + intRowsAffected);                
            }
            
            /*
             * Create the matches
             */
            strSql = "";            
            strSql += "insert into " + Temp(TEMPORARY_3) + lb; 
            strSql += "(" + lb;                         
            strSql += sf(SYSTEM_FIELD_ID_COMPANY) + "," + lb;
            strSql += sf(SYSTEM_FIELD_ID_AREA) + "," + lb;
            strSql += sf(SYSTEM_FIELD_ID_PROFILE) + "," + lb;
            strSql += sf(SYSTEM_FIELD_ID_USER) + "," + lb;
            strSql += sf(SYSTEM_FIELD_ID_TRANSACTION) + "," + lb;
            strSql += sf(SYSTEM_FIELD_SYSTEM_DATE) + "," + lb;
            strSql += sf(GetFieldObject("id", ListCENTCatalogMatch1)) + "," + lb;
            strSql += GetFieldObject("id_process", ListCENTCatalogMatch1) + "," + lb;
            strSql += GetFieldObject("date", ListCENTCatalogMatch1) + "," + lb;
            strSql += GetFieldObject("id_reconcile", ListCENTCatalogMatch1) + "," + lb;
            strSql += GetFieldObject("id_step", ListCENTCatalogMatch1) + "," + lb;
            strSql += GetFieldObject("id_rule", ListCENTCatalogMatch1) + "," + lb;
            strSql += GetFieldObject("id_status", ListCENTCatalogMatch1) + "," + lb;
            strSql += GetFieldObject("id_transaction", ListCENTCatalogMatch1) + "," + lb;
            strSql += GetFieldObject("id_field", ListCENTCatalogMatch1) + "," + lb;
            strSql += GetFieldObject("difference", ListCENTCatalogMatch1) + "," + lb;
            strSql += GetFieldObject("side", ListCENTCatalogMatch1) + "," + lb;
            strSql += GetFieldObject("note", ListCENTCatalogMatch1) + "," + lb;            
            strSql += sf(SYSTEM_FIELD_MATCH_RECONCILE) + "," + lb;            
            strSql += sf(SYSTEM_FIELD_MATCH_STEP) + "," + lb;            
            strSql += sf(SYSTEM_FIELD_MATCH_RULE) + lb;

            strSql += ")" + lb;
            
            strSql += "select * from" + lb;
            strSql += "(" + lb;
            strSql += "select distinct" + lb;
            
            strSql += sf(SYSTEM_FIELD_ID_COMPANY) + "," + lb;
            strSql += sf(SYSTEM_FIELD_ID_AREA) + "," + lb;
            strSql += sf(SYSTEM_FIELD_ID_PROFILE) + "," + lb;
            strSql += sf(SYSTEM_FIELD_ID_USER) + "," + lb;
            strSql += TRN_MATCH + "id_trn," + lb;
            strSql += sf(SYSTEM_FIELD_SYSTEM_DATE) + "," + lb;
            
            switch (strDB)
            {
                case SERVER_ORACLE:
                    strSql += strMatchId + " || rank() over (order by " + strKey + " asc, " + strKey + ")" + " id_match," + lb;
                    break;
                    
                case SERVER_SQL_SERVER:
                    strSql += "concat(" + strMatchId + ", rank() over (order by " + strKey + " asc, " + strKey + "))" + " id_match," + lb;
                    break;
                    
                case SERVER_POSTGRE:
                    strSql += "cast(" + PrepareString(strMatchId) + " || rank() over (order by " + strKey + " asc, " + strKey + ") as integer)" + " id_match," + lb;                    
                    break;

            }

            strSql += intIdProcess + " as id_process," + lb;
            strSql += this.PrepareDate(this.GetSession().GetDate(SESSION_DATE)) + " as match_date," + lb;
            strSql += intIdReconcile + " as id_reconcile," + lb;
            strSql += intIdStep + " as id_step," + lb;
            strSql += intIdRule + " as id_rule," + lb;
            strSql += intIdMatchStatus + " as id_status," + lb;
            strSql += intIdTransaction + " as id_transaction," + lb;
            strSql += "0 as id_field," + lb;
            strSql += "null as diff," + lb;
            strSql += strSide + " as side," + lb; // it's a field, not a value
            strSql += "null as note" + "," + lb;
            strSql += this.PrepareString(strReconcile) + " as reconcile," + lb;
            strSql += this.PrepareString(strStep) + " as step," + lb;
            strSql += this.PrepareString(strRule) + " as rules" + lb;

            strSql += "from " + Temp(TEMPORARY_5) + lb;
            strSql += ") tb" + lb;

            PreparedStatement1 = this.GetConnection().prepareStatement(strSql);
            intRowsAffected = PreparedStatement1.executeUpdate();   
            PreparedStatement1.close();   
            System.out.println("Match item inserted: " + intRowsAffected);            
            
            /*
             * Create the match items
             */   
            strSql = "";                
            strSql += "insert into " + Temp(TEMPORARY_4) + lb; 
            strSql += "(" + lb; 
            strSql += sf(SYSTEM_FIELD_ID_COMPANY) + "," + lb;
            strSql += sf(SYSTEM_FIELD_ID_AREA) + "," + lb;
            strSql += sf(SYSTEM_FIELD_ID_PROFILE) + "," + lb;
            strSql += sf(SYSTEM_FIELD_ID_USER) + "," + lb;
            strSql += sf(SYSTEM_FIELD_ID_TRANSACTION) + "," + lb;
            strSql += sf(SYSTEM_FIELD_SYSTEM_DATE) + "," + lb;
            strSql += sf(GetFieldObject("id", ListCENTCatalogMatchItem1)) + "," + lb;
            strSql += GetFieldObject("id_process", ListCENTCatalogMatchItem1) + "," + lb;
            strSql += GetFieldObject("id_match", ListCENTCatalogMatchItem1) + "," + lb;
            strSql += GetFieldObject("id_record", ListCENTCatalogMatchItem1) + "," + lb;
            strSql += GetFieldObject("id_rule", ListCENTCatalogMatchItem1) + "," + lb;
            strSql += GetFieldObject("note", ListCENTCatalogMatchItem1) + lb;
            strSql += ")" + lb;                 
            
            strSql += "select" + lb;            
            strSql += sf(SYSTEM_FIELD_ID_COMPANY) + "," + lb;
            strSql += sf(SYSTEM_FIELD_ID_AREA) + "," + lb;
            strSql += sf(SYSTEM_FIELD_ID_PROFILE) + "," + lb;
            strSql += sf(SYSTEM_FIELD_ID_USER) + "," + lb;
            strSql += TRN_MATCH_ITEM + "id_trn, " + lb;
            strSql += sf(SYSTEM_FIELD_SYSTEM_DATE) + "," + lb;
            strSql += sf(SYSTEM_FIELD_ID) + " id," + lb;
            strSql += intIdProcess + " id_process," + lb;
            
            switch (strDB)
            {
                case SERVER_ORACLE:
                    strSql += strMatchId + " || rank() over (order by " + strKey + " asc, " + strKey + ")" + " id_match," + lb;
                    break;
                    
                case SERVER_SQL_SERVER:
                    strSql += "concat(" + strMatchId + ", rank() over (order by " + strKey + " asc, " + strKey + "))" + " id_match," + lb;
                    break;
                    
                case SERVER_POSTGRE:
                    strSql += "cast(" + PrepareString(strMatchId) + " || rank() over (order by " + strKey + " asc, " + strKey + ") as integer)" + " id_match," + lb;
                    break;

            }
            
            strSql += sf(SYSTEM_FIELD_ID) + " id_record," + lb;
            strSql += intIdRule + " as id_rule," + lb;
            strSql += "null as note" + lb;
            strSql += "from " + Temp(TEMPORARY_5) + lb;

            PreparedStatement1 = this.GetConnection().prepareStatement(strSql);
            intRowsAffected = PreparedStatement1.executeUpdate();   
            PreparedStatement1.close();   
            System.out.println("Match item inserted: " + intRowsAffected);           

            /*
             * Update match status
             */            
            strSql = "";
            strSql += "update " + Temp(TEMPORARY_1) + " set" + lb;
            strSql += sf(SYSTEM_FIELD_ID_MATCH_STATUS) + " = " + intIdMatchStatus + lb;
            strSql += "where " + sf(SYSTEM_FIELD_ID) + " in " + lb;
            strSql += "(" + lb;
            strSql += "select " + sf(SYSTEM_FIELD_ID) + " from " + Temp(TEMPORARY_5) + lb;
            strSql += ")" + lb;
            
            PreparedStatement1 = this.GetConnection().prepareStatement(strSql);
            intRowsAffected = PreparedStatement1.executeUpdate();   
            PreparedStatement1.close();   
            System.out.println("Delete records already processed: " + intRowsAffected);              
            
            
            /*
             * Delete deprecated information (used to control the rules)
             */            
            strSql = "";            
            strSql += "delete from " + Temp(TEMPORARY_5) + lb;
            
            PreparedStatement1 = this.GetConnection().prepareStatement(strSql);
            intRowsAffected = PreparedStatement1.executeUpdate();   
            PreparedStatement1.close();   
            System.out.println("Delete records already processed: " + intRowsAffected);                        
            
            /*
             * Future use
             */
            return intRowsAffected;

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
            strDB = null;
            strSql = null;
            strKey = null;

            PreparedStatement1 = null;

            ListFieldKey1 = null;
            ListCENTCatalogMatch1 = null;
            ListCENTCatalogMatchItem1 = null;
        }        
    }      
    
    public int GroupData(int intIdProcess, int intIdTransaction, String strCode, int intIdView, List<String> ListFieldKey1) throws Exception
    {
        /*
         * General Declaration
         */
        int intRowsAffected = 0;
        
        String strSql = "";
        String strDB = "";

        PreparedStatement PreparedStatement1 = null;
        List<CENTData> ListCENTView1 = null;        
        
        try
        {
            /*
             * Specific database commands
             */            
            strDB = this.GetPropertieValue("CONNECTION_DB_SERVER");
            
            /*
             * Get the view definition to prepare data
             */
            ListCENTView1 = this.GetView(intIdView);

            if (ListCENTView1.isEmpty())
            {
                throw new CENTException("EXCEPTION_VIEW_NOT_FOUND", "View Id: " + intIdView + ", Side: " + strCode);                
            }            

            /*
             * Isolate data to avoid concurrence during multithreading
             */
            strSql = "";
            strSql += "insert into " + Temp(TEMPORARY_2) + lb;
            strSql += "(" + lb;
                strSql += sf(SYSTEM_FIELD_PAGING_START) + lb;            
                strSql += "," + sf(SYSTEM_FIELD_ID_COMPANY) + lb;
                strSql += "," + sf(SYSTEM_FIELD_ID_AREA) + lb;
                strSql += "," + sf(SYSTEM_FIELD_ID_PROFILE) + lb;
                strSql += "," + sf(SYSTEM_FIELD_ID_USER) + lb;
                strSql += "," + sf(SYSTEM_FIELD_ID_TRANSACTION) + lb;
                strSql += "," + sf(SYSTEM_FIELD_SYSTEM_DATE) + lb;
                strSql += "," + sf(SYSTEM_FIELD_DATA_SOURCE) + lb;
                strSql += "," + sf(SYSTEM_FIELD_ID_PROCESS) + lb;
                strSql += "," + sf(SYSTEM_FIELD_ID_MATCH) + lb;
                strSql += "," + this.GetFieldList(intIdTransaction, ListCENTView1, false);
            strSql += ")" + lb;            

            /*
             * Field list
             */            
            strSql += Select() + lb;
            
            switch (strDB)
            {
                case SERVER_ORACLE:
                    strSql += "row_number() over(order by " + sf(SYSTEM_FIELD_ID_COMPANY) + " asc) as row_id" + lb;
                    break;

                case SERVER_SQL_SERVER:
                    strSql += "row_number() over(order by " + sf(SYSTEM_FIELD_ID_COMPANY) + " asc) as row_id" + lb;
                    break;
                                        
                case SERVER_POSTGRE:
                    strSql += "row_number() over(order by " + sf(SYSTEM_FIELD_ID_COMPANY) + " asc) as row_id" + lb;
                    break;

            }            
            
            strSql += ", t1.*" + lb;
            strSql += "from" + lb;
            strSql += "(" + lb;
            
                strSql += Select() + lb;                    
                strSql += sf(SYSTEM_FIELD_ID_COMPANY) + lb;
                strSql += "," + sf(SYSTEM_FIELD_ID_AREA) + lb;
                strSql += "," + sf(SYSTEM_FIELD_ID_PROFILE) + lb;
                strSql += "," + sf(SYSTEM_FIELD_ID_USER) + lb;
                strSql += "," + sf(SYSTEM_FIELD_ID_TRANSACTION) + lb;
                strSql += "," + sf(SYSTEM_FIELD_SYSTEM_DATE) + lb;
                strSql += "," + sf(SYSTEM_FIELD_DATA_SOURCE) + lb;
                strSql += "," + sf(SYSTEM_FIELD_ID_PROCESS) + lb;
                strSql += "," + sf(SYSTEM_FIELD_ID_MATCH) + lb;                                    
                strSql += "," + this.GetFieldList(intIdTransaction, ListCENTView1, true);

                /*
                 * Data origin
                 */            
                strSql += "from " + Temp(TEMPORARY_1) + lb;

                /*
                 * Basic condition to read from imported data
                 */
                strSql += "where " + sf(SYSTEM_FIELD_DATA_SOURCE) + " = " + this.PrepareString(strCode) + lb;
                strSql += "and " + sf(SYSTEM_FIELD_ID_MATCH_STATUS) + " = " + this.PrepareNumber(MATCH_STATUS_MATCHED) + lb;                

                /*
                 * Order by key (same as group)
                 */
                strSql += "group by " + lb;            
                strSql += sf(SYSTEM_FIELD_ID_COMPANY) + lb;
                strSql += "," + sf(SYSTEM_FIELD_ID_AREA) + lb;
                strSql += "," + sf(SYSTEM_FIELD_ID_PROFILE) + lb;
                strSql += "," + sf(SYSTEM_FIELD_ID_USER) + lb;
                strSql += "," + sf(SYSTEM_FIELD_ID_TRANSACTION) + lb;
                strSql += "," + sf(SYSTEM_FIELD_SYSTEM_DATE) + lb;
                strSql += "," + sf(SYSTEM_FIELD_DATA_SOURCE) + lb;
                strSql += "," + sf(SYSTEM_FIELD_ID_PROCESS) + lb;
                strSql += "," + sf(SYSTEM_FIELD_ID_MATCH) + lb;            
                strSql += "," + this.GetGroupBy(ListCENTView1);            

                /*
                 * Order by key (same as group)
                 */
                strSql += "order by " + lb;            
                strSql += sf(SYSTEM_FIELD_DATA_SOURCE) + lb;
                
                for (String strkeyField1 : ListFieldKey1)
                {
                    strSql += ", " + strkeyField1 + lb;
                }
            
            strSql += ") t1" + lb;            

            PreparedStatement1 = this.GetConnection().prepareStatement(strSql);
            intRowsAffected = PreparedStatement1.executeUpdate();
            PreparedStatement1.close();
            System.out.println("Number of records after grouping: " + strCode + ": " + intRowsAffected);
            
            /*
             * It's the number of records to be paged and reconciled
             */
            return intRowsAffected;

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
            strSql = null;
            strDB = null;

            PreparedStatement1 = null;
            ListCENTView1 = null;
        }        
    }        

    public int OrderData(int intIdProcess, int intIdTransaction, String strCode, int intIdView, List<String> ListFieldKey1) throws Exception
    {
        /*
         * General Declaration
         */
        int intRowsAffected = 0;
        
        String strSql = "";
        String strDB = "";

        PreparedStatement PreparedStatement1 = null;
        List<CENTData> ListCENTView1 = null;        
        
        try
        {
            /*
             * Specific database commands
             */            
            strDB = this.GetPropertieValue("CONNECTION_DB_SERVER");
            
            /*
             * Get the view definition to prepare data
             */
            ListCENTView1 = this.GetView(intIdView);

            if (ListCENTView1.isEmpty())
            {
                throw new CENTException("EXCEPTION_VIEW_NOT_FOUND", "View Id: " + intIdView + ", Side: " + strCode);                
            }            

            /*
             * Isolate data to avoid concurrence during multithreading
             */
            strSql = "";
            strSql += "insert into " + Temp(TEMPORARY_2) + lb;
            strSql += "(" + lb;
                strSql += sf(SYSTEM_FIELD_PAGING_START) + lb;            
                strSql += "," + sf(SYSTEM_FIELD_ID_COMPANY) + lb;
                strSql += "," + sf(SYSTEM_FIELD_ID_AREA) + lb;
                strSql += "," + sf(SYSTEM_FIELD_ID_PROFILE) + lb;
                strSql += "," + sf(SYSTEM_FIELD_ID_USER) + lb;
                strSql += "," + sf(SYSTEM_FIELD_ID_TRANSACTION) + lb;
                strSql += "," + sf(SYSTEM_FIELD_SYSTEM_DATE) + lb;
                strSql += "," + sf(SYSTEM_FIELD_DATA_SOURCE) + lb;
                strSql += "," + sf(SYSTEM_FIELD_ID_PROCESS) + lb;
                strSql += "," + sf(SYSTEM_FIELD_ID_MATCH) + lb;
                strSql += "," + this.GetFieldList(intIdTransaction, ListCENTView1, false);
            strSql += ")" + lb;            

            /*
             * Field list
             */            
            strSql += Select() + lb;
            
            switch (strDB)
            {
                case SERVER_ORACLE:
                    strSql += "row_number() over(order by " + sf(SYSTEM_FIELD_ID_COMPANY) + " asc) as row_id" + lb;
                    break;

                case SERVER_SQL_SERVER:
                    strSql += "row_number() over(order by " + sf(SYSTEM_FIELD_ID_COMPANY) + " asc) as row_id" + lb;
                    break;
                    
                case SERVER_POSTGRE:
                    strSql += "row_number() over(order by " + sf(SYSTEM_FIELD_ID_COMPANY) + " asc) as row_id" + lb;
                    break;
            }            
            
            strSql += ", t1.*" + lb;
            strSql += "from" + lb;
            strSql += "(" + lb;
            
                strSql += Select() + lb;                    
                strSql += sf(SYSTEM_FIELD_ID_COMPANY) + lb;
                strSql += "," + sf(SYSTEM_FIELD_ID_AREA) + lb;
                strSql += "," + sf(SYSTEM_FIELD_ID_PROFILE) + lb;
                strSql += "," + sf(SYSTEM_FIELD_ID_USER) + lb;
                strSql += "," + sf(SYSTEM_FIELD_ID_TRANSACTION) + lb;
                strSql += "," + sf(SYSTEM_FIELD_SYSTEM_DATE) + lb;
                strSql += "," + sf(SYSTEM_FIELD_DATA_SOURCE) + lb;
                strSql += "," + sf(SYSTEM_FIELD_ID_PROCESS) + lb;
                strSql += "," + sf(SYSTEM_FIELD_ID_MATCH) + lb;                                    
                strSql += "," + this.GetFieldList(intIdTransaction, ListCENTView1, false);

                /*
                 * Data origin
                 */            
                strSql += "from " + Temp(TEMPORARY_1) + lb;

                /*
                 * Basic condition to read from imported data
                 */
                strSql += "where " + sf(SYSTEM_FIELD_DATA_SOURCE) + " = " + this.PrepareString(strCode) + lb;       
                strSql += "and " + sf(SYSTEM_FIELD_ID_MATCH_STATUS) + " = " + this.PrepareNumber(MATCH_STATUS_MATCHED) + lb;

                /*
                 * Order by key (same as group)
                 */
                strSql += "order by " + lb;            
                strSql += sf(SYSTEM_FIELD_DATA_SOURCE) + lb;
                
                for (String strkeyField1 : ListFieldKey1)
                {
                    strSql += ", " + strkeyField1 + lb;
                }
            
            strSql += ") t1" + lb;            

            PreparedStatement1 = this.GetConnection().prepareStatement(strSql);
            intRowsAffected = PreparedStatement1.executeUpdate();
            PreparedStatement1.close();
            System.out.println("Number of records after grouping: " + strCode + ": " + intRowsAffected);
            
            /*
             * It's the number of records to be paged and reconciled
             */
            return intRowsAffected;

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
            strSql = null;
            strDB = null;

            PreparedStatement1 = null;
            ListCENTView1 = null;
        }        
    }          
    
    public ResultSet GetData(int intIdProcess, int intIdTransaction, String strPage, String strCode, int intIdView, int intIdRule, int intIdType, List<String> ListFieldKey1) throws Exception
    {
        /*
         * General Declaration
         */        
        int intMin = 0;
        int intMax = 0;        

        String strSql = "";        
        String arrPage[] = null;
        Statement Statement1 = null;
        ResultSet ResultSet1 = null;
        
        List<CENTData> ListCENTData1 = null;

        try
        {
            /*
             * Get the id range to do paging
             */
            arrPage = strPage.split(":");                
            intMin = Integer.parseInt(arrPage[0]);
            intMax = Integer.parseInt(arrPage[1]);
            
            /*
             * Get the view definition
             */            
            ListCENTData1 = this.GetView(intIdView);
            
            /*
             * Generate the final sql 
             */
            strSql = "";
            strSql += "select" + lb;                        

            /*
             * Add system fields
             */            
            strSql += this.GetFieldList(intIdTransaction, ListCENTData1, false);
            strSql += "," + sf(SYSTEM_FIELD_ID) + lb;
            strSql += "," + sf(SYSTEM_FIELD_ID_COMPANY) + lb;
            strSql += "," + sf(SYSTEM_FIELD_SYSTEM_DATE)+ lb;
            strSql += "," + sf(SYSTEM_FIELD_DATA_SOURCE) + lb;
            strSql += "," + sf(SYSTEM_FIELD_ID_TRANSACTION) + lb;
            
            /*
             * From
             */
            strSql += "from " + Temp(TEMPORARY_2) + lb;            
            
            /*
             * Extremelly important, it's the index when querying: 
             */            
            strSql += "where " + sf(SYSTEM_FIELD_DATA_SOURCE) + " = " + this.PrepareString(strCode) + lb;
            strSql += "and " + sf(SYSTEM_FIELD_PAGING_START) + " between " + intMin + " and " + intMax + lb;  
            
            /*
             * Order by key (same as group)
             */
            strSql += "order by " + lb;            
            strSql += sf(SYSTEM_FIELD_DATA_SOURCE) + lb;

            for (String strkeyField1 : ListFieldKey1)
            {
                strSql += ", " + strkeyField1 + lb;
            }

            /*
             * Prepare the statement
             */
            Statement1 = this.GetConnection().createStatement();
            ResultSet1 = Statement1.executeQuery(strSql);

            /*
             * Return it
             */         
            return ResultSet1;            
            
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
            /*
             * Close sql related objects
             */            
            strSql = null;
            arrPage = null;
            Statement1 = null;
            ListCENTData1 = null;
        }        
    }

    public int MatchedToProposed(int intNextMatchId, List<CENTData> ListCENTMatchKey1, List<CENTData> ListCENTMatchValue1, List<CENTData> ListCENTCatalogMatch1, List<CENTData> ListCENTCatalogMatchItem1, int intIdTransaction, String FIELD_ID_FIELD, String FIELD_MATCH_DIFF, int intIdMatchAs) throws Exception
    {
        /*
         * General Declaration
         */
        int intMatchCount = 0;
        int intIdField = 0;
        int intMatchId = 0;
        int intIdProcess = 0;
        int intIdRule = 0;
        int intRowsAffected = 0;
        int intType = 0;
        
        String strSql = "";
        String strSqlKey = "";
        String strDifference = "";
        Statement Statement1 = null;        
        PreparedStatement PreparedStatement1 = null;
        ResultSet ResultSet1 = null;
        List<CENTData> ListCENTCatalogTransaction1 = null;
        
        try
        {

            /*
             * Prepare the key and query the match id
             */
            strSql = "";
            strSql += "select" + lb;
            strSql += sf(SYSTEM_FIELD_ID_MATCH) + lb;
            strSql += "from " + Temp(TEMPORARY_6) + lb;
            
            strSqlKey = " where 1 = 1" + lb;

            for (CENTData CENTKeyToUpdateMatch1 : ListCENTMatchKey1)
            {
                switch (CENTKeyToUpdateMatch1.GetInt(MATCH_FIELD_KEY_TYPE))
                {
                    case TYPE_INT:
                        strSqlKey += " and " + CENTKeyToUpdateMatch1.GetText(MATCH_FIELD_KEY_NAME) + " = " + this.PrepareNumber(CENTKeyToUpdateMatch1.GetInt(MATCH_FIELD_KEY_VALUE)) + lb;
                        break;

                    case TYPE_TEXT:
                        strSqlKey += " and " + CENTKeyToUpdateMatch1.GetText(MATCH_FIELD_KEY_NAME) + " = " + this.PrepareString(CENTKeyToUpdateMatch1.GetText(MATCH_FIELD_KEY_VALUE)) + lb;
                        break;    

                    case TYPE_DATE:
                        strSqlKey += " and " + CENTKeyToUpdateMatch1.GetText(MATCH_FIELD_KEY_NAME) + " = " + this.PrepareDate(CENTKeyToUpdateMatch1.GetDate(MATCH_FIELD_KEY_VALUE)) + lb;    
                        break;    

                    case TYPE_DOUBLE:
                        strSqlKey += " and " + CENTKeyToUpdateMatch1.GetText(MATCH_FIELD_KEY_NAME) + " = " + this.PrepareNumber(CENTKeyToUpdateMatch1.GetDouble(MATCH_FIELD_KEY_VALUE)) + lb;    
                        break;
                }
            }

            /*
             * Prepare the statement
             */
            Statement1 = this.GetConnection().createStatement();
            ResultSet1 = Statement1.executeQuery(strSql + strSqlKey);

            while (ResultSet1.next())
            {
                intMatchId = ResultSet1.getInt(1);
            }            

            Statement1.close();            
            ResultSet1.close();
            
            /*
             * Decide if the match is create by single line or multiple lines
             */            
            if (intIdMatchAs == MATCH_AS_ROW) // Match by line
            {            
                /*
                 * Update or create match related to this key
                 */
                for (CENTData CENTMatchValue1 : ListCENTMatchValue1)
                {                
                    intMatchCount ++;
                    intIdField = CENTMatchValue1.GetInt(FIELD_ID_FIELD);
                    strDifference = CENTMatchValue1.GetText(FIELD_MATCH_DIFF);

                    if (intMatchCount == 1)
                    {
                        strSql = "";
                        strSql += "update " + Temp(TEMPORARY_3) + " set" + lb;            
                        strSql += GetFieldObject("id_status", ListCENTCatalogMatch1) + " = " + MATCH_STATUS_PROPOSED + "," + lb;
                        strSql += GetFieldObject("id_field", ListCENTCatalogMatch1) + " = " + intIdField + "," + lb;
                        strSql += GetFieldObject("difference", ListCENTCatalogMatch1) + " = " + this.PrepareString(strDifference) + lb;
                        strSql += "where " + GetFieldObject("id", ListCENTCatalogMatch1) + " = " + intMatchId;

                        PreparedStatement1 = this.GetConnection().prepareStatement(strSql);
                        intRowsAffected = PreparedStatement1.executeUpdate();
                        PreparedStatement1.close();
                        System.out.println("Match relegated to proposed: " + intMatchId);

                        /*
                         * More than one field, must keep some information
                         */
                        if (ListCENTMatchValue1.size() > 1)
                        {
                            strSql = "";
                            strSql += "select" + lb;
                            strSql += GetFieldObject("id_process", ListCENTCatalogMatch1) + "," + lb;
                            strSql += GetFieldObject("id_rule", ListCENTCatalogMatch1) + "," + lb;
                            strSql += GetFieldObject("note", ListCENTCatalogMatch1) + lb;
                            strSql += "from " + Temp(TEMPORARY_3) + lb;            
                            strSql += "where " + GetFieldObject("id", ListCENTCatalogMatch1) + " = " + intMatchId;                        

                            Statement1 = this.GetConnection().createStatement();
                            ResultSet1 = Statement1.executeQuery(strSql);

                            while (ResultSet1.next())
                            {
                                intIdProcess = ResultSet1.getInt(1);
                                intIdRule = ResultSet1.getInt(2);
                            }                                    

                            Statement1.close();            
                            ResultSet1.close();
                        }

                    }
                    else
                    {
                        /*
                         * Calculate next match id
                         */                    
                        intNextMatchId ++;

                        /*
                         * Duplicate the match
                         */
                        strSql = "";
                        strSql += "insert into " + Temp(TEMPORARY_3) + lb;
                        strSql += "(";                    
                        strSql += sf(SYSTEM_FIELD_ID_COMPANY) + "," + lb;
                        strSql += sf(SYSTEM_FIELD_ID_AREA) + "," + lb;
                        strSql += sf(SYSTEM_FIELD_ID_PROFILE) + "," + lb;
                        strSql += sf(SYSTEM_FIELD_ID_USER) + "," + lb;
                        strSql += sf(SYSTEM_FIELD_ID_TRANSACTION) + "," + lb;
                        strSql += sf(SYSTEM_FIELD_SYSTEM_DATE) + "," + lb;                     
                        strSql += GetFieldObject("id", ListCENTCatalogMatch1) + "," + lb;
                        strSql += GetFieldObject("id_process", ListCENTCatalogMatch1) + "," + lb;
                        strSql += GetFieldObject("date", ListCENTCatalogMatch1) + "," + lb;
                        strSql += GetFieldObject("id_reconcile", ListCENTCatalogMatch1) + "," + lb;
                        strSql += GetFieldObject("id_step", ListCENTCatalogMatch1) + "," + lb;
                        strSql += GetFieldObject("id_rule", ListCENTCatalogMatch1) + "," + lb;
                        strSql += GetFieldObject("id_status", ListCENTCatalogMatch1) + "," + lb;
                        strSql += GetFieldObject("id_transaction", ListCENTCatalogMatch1) + "," + lb;
                        strSql += GetFieldObject("id_field", ListCENTCatalogMatch1) + "," + lb;
                        strSql += GetFieldObject("difference", ListCENTCatalogMatch1) + "," + lb;
                        strSql += GetFieldObject("side", ListCENTCatalogMatch1) + "," + lb;
                        strSql += GetFieldObject("note", ListCENTCatalogMatch1) + lb;
                        strSql += ")" + lb;
                        strSql += "select" + lb;                    
                        strSql += sf(SYSTEM_FIELD_ID_COMPANY) + "," + lb;
                        strSql += sf(SYSTEM_FIELD_ID_AREA) + "," + lb;
                        strSql += sf(SYSTEM_FIELD_ID_PROFILE) + "," + lb;
                        strSql += sf(SYSTEM_FIELD_ID_USER) + "," + lb;
                        strSql += sf(SYSTEM_FIELD_ID_TRANSACTION) + "," + lb;
                        strSql += sf(SYSTEM_FIELD_SYSTEM_DATE) + "," + lb;                    
                        strSql += intNextMatchId + " " + "id" + "," + lb;
                        strSql += GetFieldObject("id_process", ListCENTCatalogMatch1) + "," + lb;
                        strSql += GetFieldObject("date", ListCENTCatalogMatch1) + "," + lb;
                        strSql += GetFieldObject("id_reconcile", ListCENTCatalogMatch1) + "," + lb;
                        strSql += GetFieldObject("id_step", ListCENTCatalogMatch1) + "," + lb;
                        strSql += GetFieldObject("id_rule", ListCENTCatalogMatch1) + "," + lb;
                        strSql += GetFieldObject("id_status", ListCENTCatalogMatch1) + "," + lb;
                        strSql += GetFieldObject("id_transaction", ListCENTCatalogMatch1) + "," + lb;
                        strSql += intIdField + " " + "id_field" + "," + lb;
                        strSql += this.PrepareString(strDifference) + " " + "difference" + "," + lb;
                        strSql += GetFieldObject("side", ListCENTCatalogMatch1) + "," + lb;
                        strSql += GetFieldObject("note", ListCENTCatalogMatch1) + lb;
                        strSql += "from " + Temp(TEMPORARY_3) + lb;
                        strSql += "where " + GetFieldObject("id", ListCENTCatalogMatch1) + " = " + intMatchId;

                        PreparedStatement1 = this.GetConnection().prepareStatement(strSql);
                        intRowsAffected = PreparedStatement1.executeUpdate();
                        PreparedStatement1.close();
                        System.out.println("Duplicate the match: " + intMatchId);                    

                        /*
                         * Duplicate the match items
                         */
                        strSql = "";
                        strSql += "insert into " + Temp(TEMPORARY_4) + lb;
                        strSql += "(";
                        strSql += sf(SYSTEM_FIELD_ID_COMPANY) + "," + lb;
                        strSql += sf(SYSTEM_FIELD_ID_AREA) + "," + lb;
                        strSql += sf(SYSTEM_FIELD_ID_PROFILE) + "," + lb;
                        strSql += sf(SYSTEM_FIELD_ID_USER) + "," + lb;
                        strSql += sf(SYSTEM_FIELD_ID_TRANSACTION) + "," + lb;
                        strSql += sf(SYSTEM_FIELD_SYSTEM_DATE) + "," + lb;                    
                        strSql += GetFieldObject("id", ListCENTCatalogMatchItem1) + "," + lb;
                        strSql += GetFieldObject("id_process", ListCENTCatalogMatchItem1) + "," + lb;
                        strSql += GetFieldObject("id_match", ListCENTCatalogMatchItem1) + "," + lb;
                        strSql += GetFieldObject("id_record", ListCENTCatalogMatchItem1) + "," + lb;
                        strSql += GetFieldObject("id_rule", ListCENTCatalogMatchItem1) + "," + lb;
                        strSql += GetFieldObject("note", ListCENTCatalogMatchItem1) + lb;
                        strSql += ")" + lb;                    
                        strSql += "select" + lb;                    
                        strSql += sf(SYSTEM_FIELD_ID_COMPANY) + "," + lb;
                        strSql += sf(SYSTEM_FIELD_ID_AREA) + "," + lb;
                        strSql += sf(SYSTEM_FIELD_ID_PROFILE) + "," + lb;
                        strSql += sf(SYSTEM_FIELD_ID_USER) + "," + lb;
                        strSql += sf(SYSTEM_FIELD_ID_TRANSACTION) + "," + lb;
                        strSql += sf(SYSTEM_FIELD_SYSTEM_DATE) + "," + lb;                    
                        strSql += intNextMatchId + " " + "id" + "," + lb;
                        strSql += intIdProcess + " " + "id_process" + "," + lb;
                        strSql += intNextMatchId + " " + "id_match" + "," + lb;
                        strSql += SYSTEM_FIELD_ID + " " + "id_record" + "," + lb;
                        strSql += intIdRule + " " + "id_rule" + "," + lb;
                        strSql += "null" + " " + "note" + lb;
                        strSql += "from " + Temp(TEMPORARY_1) + lb;
                        strSql += strSqlKey;

                        PreparedStatement1 = this.GetConnection().prepareStatement(strSql);
                        intRowsAffected = PreparedStatement1.executeUpdate();
                        PreparedStatement1.close();
                        System.out.println("Match relegated to proposed: " + intMatchId);                    
                    }
                }            
            }
            else
            {
                /*
                 * Get the transaction catalog to identify the fields labels
                 */
                ListCENTCatalogTransaction1 = this.GetCatalog(intIdTransaction);
                
                for (CENTData CENTMatchValue1 : ListCENTMatchValue1)
                {
                    /*
                     * Prepare the difference list
                     */
                    intIdField = CENTMatchValue1.GetInt(FIELD_ID_FIELD);

                    strDifference += this.Translate(GetFieldLabel(intIdField, ListCENTCatalogTransaction1));
                    strDifference += ": ";
                    strDifference += CENTMatchValue1.GetText(FIELD_MATCH_DIFF);
                    strDifference += "; ";
                }

                /*
                 * Field id must be zero (must disregard this columns)
                 */                
                intIdField = 0;
                
                /*
                 * Update the match with the differences
                 */
                strSql = "";
                strSql += "update " + Temp(TEMPORARY_3) + " set" + lb;            
                strSql += GetFieldObject("id_status", ListCENTCatalogMatch1) + " = " + MATCH_STATUS_PROPOSED + "," + lb;
                strSql += GetFieldObject("id_field", ListCENTCatalogMatch1) + " = " + intIdField + "," + lb;
                strSql += GetFieldObject("difference", ListCENTCatalogMatch1) + " = " + this.PrepareString(strDifference) + lb;
                strSql += "where " + sf(GetFieldObject("id", ListCENTCatalogMatch1)) + " = " + intMatchId;

                PreparedStatement1 = this.GetConnection().prepareStatement(strSql);
                intRowsAffected = PreparedStatement1.executeUpdate();
                PreparedStatement1.close();
                System.out.println("Match relegated to proposed: " + intMatchId);    
                
            }
            
            /*
             * Return new match id to control the flow
             */
            return intNextMatchId;

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
            strSql = null;
            strSqlKey = null;
            strDifference = null;
            Statement1 = null;        
            PreparedStatement1 = null;
            ResultSet1 = null;
            ListCENTCatalogTransaction1 = null;
        }
    }
        
    public int SaveMatchAndMatchItem(int intIdProcess, int intIdArea, int intIdResult, List<CENTData> ListCENTCatalogMatch1, List<CENTData> ListCENTCatalogMatchItem1) throws Exception
    {
        /*
         * General Declaration
         */
        int intRowsAffected = 0;
        String strSql = "";
        PreparedStatement PreparedStatement1 = null;
        
        try
        {
            /*
             * Delete old matches
             */
            strSql = "";
            strSql += "delete from " + this.GetTable(TRN_MATCH) + lb;            
            strSql += "where " + GetFieldObject("id_process", ListCENTCatalogMatch1) + " = " + this.PrepareNumber(intIdProcess) + lb;

            PreparedStatement1 = this.GetConnection().prepareStatement(strSql);
            intRowsAffected = PreparedStatement1.executeUpdate();   
            PreparedStatement1.close();
            System.out.println("Deprecated matches deleted: " + intRowsAffected);            
            
            /*
             * Delete old matche items
             */
            strSql = "";
            strSql += "delete from " + this.GetTable(TRN_MATCH_ITEM) + lb;
            strSql += "where " + GetFieldObject("id_process", ListCENTCatalogMatchItem1) + " = " + this.PrepareNumber(intIdProcess) + lb;

            PreparedStatement1 = this.GetConnection().prepareStatement(strSql);
            intRowsAffected = PreparedStatement1.executeUpdate();   
            PreparedStatement1.close();
            System.out.println("Old matche items deleted: " + intRowsAffected);
            
            /*
             * Apply result definition rules, maybe the user doesn't need to see every records (see domain_9)
             */            
            switch (intIdResult)
            {
                case 1:
                    
                    /*
                     * Do nothing, see all situations  
                     */                    
                    break ;
                    
                case 2:
                    
                    /*
                     * Delete the matched
                     */
                    strSql = "";
                    strSql += "delete from " + Temp(TEMPORARY_3) + lb;
                    strSql += "where " + GetFieldObject("id_status", ListCENTCatalogMatch1) + " = " + MATCH_STATUS_MATCHED + lb;
                    
                    PreparedStatement1 = this.GetConnection().prepareStatement(strSql);
                    intRowsAffected = PreparedStatement1.executeUpdate();   
                    PreparedStatement1.close();
                    System.out.println("Delete matched items: " + intRowsAffected);                    

                    /*
                     * Delete the matched
                     */                    
                    strSql = "";
                    strSql += "delete from " + Temp(TEMPORARY_4) + lb;
                    strSql += "where " + GetFieldObject("id_match", ListCENTCatalogMatchItem1) + " not in " + lb;
                    strSql += "(" + lb;
                    strSql += "select " + sf(GetFieldObject("id", ListCENTCatalogMatch1)) + " from " + Temp(TEMPORARY_3) + lb;
                    strSql += ")" + lb;
                    
                    PreparedStatement1 = this.GetConnection().prepareStatement(strSql);
                    intRowsAffected = PreparedStatement1.executeUpdate();   
                    PreparedStatement1.close();
                    System.out.println("Delete matched items: " + intRowsAffected);
                    
                    break;
                    
                case 3:
                    
                    /*
                     * Delete not matched
                     */
                    strSql = "";
                    strSql += "delete from " + Temp(TEMPORARY_3) + lb;
                    strSql += "where " + sf(GetFieldObject("id_status", ListCENTCatalogMatch1)) + " <> " + MATCH_STATUS_MATCHED + lb;
                    
                    PreparedStatement1 = this.GetConnection().prepareStatement(strSql);
                    intRowsAffected = PreparedStatement1.executeUpdate();   
                    PreparedStatement1.close();
                    System.out.println("Delete matched items: " + intRowsAffected);                    

                    /*
                     * Delete not matched
                     */                    
                    strSql = "";
                    strSql += "delete from " + Temp(TEMPORARY_4) + lb;
                    strSql += "where " + GetFieldObject("id_match", ListCENTCatalogMatchItem1) + " not in " + lb;
                    strSql += "(" + lb;
                    strSql += "select " + GetFieldObject("id", ListCENTCatalogMatch1) + " from " + Temp(TEMPORARY_3) + lb;
                    strSql += ")" + lb;
                    
                    PreparedStatement1 = this.GetConnection().prepareStatement(strSql);
                    intRowsAffected = PreparedStatement1.executeUpdate();   
                    PreparedStatement1.close();
                    System.out.println("Delete matched items: " + intRowsAffected);
                    
                    break;                    
                    
            }

            /*
             * Save new matches
             */
            strSql = "";
            strSql += "insert into " + this.GetTable(TRN_MATCH) + " select * from " + Temp(TEMPORARY_3) + " order by " + sf(SYSTEM_FIELD_ID);
            PreparedStatement1 = this.GetConnection().prepareStatement(strSql);
            intRowsAffected = PreparedStatement1.executeUpdate();   
            PreparedStatement1.close();
            System.out.println("New match saved: " + intRowsAffected);            
            
            /*
             * Save new matches item
             */
            strSql = "";
            strSql += "insert into " + this.GetTable(TRN_MATCH_ITEM) + " select * from " + Temp(TEMPORARY_4) + " order by " + sf(SYSTEM_FIELD_ID);
            PreparedStatement1 = this.GetConnection().prepareStatement(strSql);
            intRowsAffected = PreparedStatement1.executeUpdate();
            PreparedStatement1.close();
            System.out.println("New match items saved: " + intRowsAffected);

            
            /*
             * It's the number of records to be paged and reconciled
             */
            return intRowsAffected;
            
        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }
        finally
        {
            strSql = "";
            PreparedStatement1 = null;
            ListCENTCatalogMatch1 = null;
            ListCENTCatalogMatchItem1 = null;
        }        
    }    

    public int GetLastMatchId() throws Exception
    {
        /*
         * General Declaration
         */        
        int intCount = 0;
        String strSql = "";        
        Statement Statement1 = null;
        ResultSet ResultSet1 = null;

        try
        {           
            /*
             * Generate the final sql 
             */
            strSql = "";
            strSql += "select max(" + sf(SYSTEM_FIELD_ID) + ")" + lb;
            strSql += "from " + Temp(TEMPORARY_3) + lb;            

            /*
             * Prepare the statement
             */
            Statement1 = this.GetConnection().createStatement();
            ResultSet1 = Statement1.executeQuery(strSql);
            
            while (ResultSet1.next())
            {
                intCount = ResultSet1.getInt(1);
            }

            /*
             * Return it
             */         
            return intCount;            
            
        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }
        finally
        {           
            /*
             * Close sql related objects
             */            
            strSql = null;
            Statement1 = null;
        }        
    }    


    public int UpdateReconArea(int intIdProcess, int intIdTransaction, String strCode1, String strCode2, List<CENTData> ListCENTCatalogMatch1, List<CENTData> ListCENTCatalogMatchItem1, List<String> ListFields1) throws Exception
    {
        /*
         * General Declaration
         */
        int intRowsAffected = 0;
        String strSql = "";
        String strDB = "";
        PreparedStatement PreparedStatement1 = null;
        
        try
        {            
            /*
             * Get current database engine
             */
            strDB = this.GetPropertieValue("CONNECTION_DB_SERVER");            
         
            /*
             * Stamp the id_process, what finalize the process
             */
            strSql = "";
            strSql += "update " + this.GetTable(intIdTransaction) + " set" + lb;
            strSql += sf(SYSTEM_FIELD_ID_PROCESS) + " = " + PrepareNumber(intIdProcess) + lb;            
            strSql += "where " + sf(SYSTEM_FIELD_ID_COMPANY) + " = " + this.PrepareNumber(this.GetSession().GetInt(SESSION_COMPANY)) + lb;
            strSql += "and (" + sf(SYSTEM_FIELD_ID_PROCESS) + " is null or " + sf(SYSTEM_FIELD_ID_PROCESS) + " = " + PrepareNumber(intIdProcess) + ")";
            strSql += "and (" + sf(SYSTEM_FIELD_DATA_SOURCE) + " = " + this.PrepareString(strCode1) + " or " + sf(SYSTEM_FIELD_DATA_SOURCE) + " = " + this.PrepareString(strCode2) + ")";
            
            PreparedStatement1 = this.GetConnection().prepareStatement(strSql);
            intRowsAffected = PreparedStatement1.executeUpdate();   
            PreparedStatement1.close();

            /*
             * Write match information into the reconciliation area, it improves performance avoiding joins when querying
             */            
            if (this.GetPropertieValue("MATCH_AS").trim().equals("2"))
            {
                /*
                 * Isolate data before marge it
                 */
                strSql = "";
                strSql += "insert into " + Temp(TEMPORARY_10) + lb;       
                strSql += "select * from " + this.GetTable(intIdTransaction) + lb;
                strSql += "where " + sf(SYSTEM_FIELD_ID_PROCESS) + " = " + PrepareNumber(intIdProcess);
                PreparedStatement1 = this.GetConnection().prepareStatement(strSql);
                intRowsAffected = PreparedStatement1.executeUpdate();   
                PreparedStatement1.close(); 

                /*
                 * Create index to speed up process
                 */
                strSql = "";                
                strSql += "create index idx10" + " on " + Temp(TEMPORARY_10) + " (" + SYSTEM_FIELD_ID + ")";
                PreparedStatement1 = this.GetConnection().prepareStatement(strSql);
                PreparedStatement1.execute();
                PreparedStatement1.close();                                            

                /*
                 * Index to calculate final result
                 */
                strSql = "";
                strSql += "create index idx3 on " + Temp(TEMPORARY_3);
                strSql += "(";
                strSql += sf(this.GetFieldObject("id", ListCENTCatalogMatch1)) + ",";
                strSql += this.GetFieldObject("id_process", ListCENTCatalogMatch1);
                strSql += ")";
                PreparedStatement1 = this.GetConnection().prepareStatement(strSql);
                PreparedStatement1.execute();
                PreparedStatement1.close();                        

                strSql = "";
                strSql += "create index idx41 on " + Temp(TEMPORARY_4);
                strSql += "(";
                strSql += this.GetFieldObject("id_match", ListCENTCatalogMatchItem1) + ",";
                strSql += this.GetFieldObject("id_process", ListCENTCatalogMatchItem1);
                strSql += ")";
                PreparedStatement1 = this.GetConnection().prepareStatement(strSql);
                PreparedStatement1.execute();
                PreparedStatement1.close();

                strSql = "";
                strSql += "create index idx42 on " + Temp(TEMPORARY_4);
                strSql += "(";
                strSql += this.GetFieldObject("id_record", ListCENTCatalogMatchItem1);
                strSql += ")";
                PreparedStatement1 = this.GetConnection().prepareStatement(strSql);
                PreparedStatement1.execute();
                PreparedStatement1.close();                
                
                /*
                 * Prepare final result
                 */                                                
                strSql = "";
                strSql += "insert into " + Temp(TEMPORARY_9) + lb;
                strSql += "( " + lb;                                                                        
                strSql += sf(SYSTEM_FIELD_ID_PROCESS_ETL) + ", " + lb;
                strSql += sf(SYSTEM_FIELD_ID_COMPANY) + ", " + lb;
                strSql += sf(SYSTEM_FIELD_ID_AREA) + ", " + lb;
                strSql += sf(SYSTEM_FIELD_ID_PROFILE) + ", " + lb;
                strSql += sf(SYSTEM_FIELD_ID_USER) + ", " + lb;                
                strSql += sf(SYSTEM_FIELD_ID_TRANSACTION) + ", " + lb;                
                strSql += sf(SYSTEM_FIELD_MATCH_RECONCILE) + ", " + lb;
                strSql += sf(SYSTEM_FIELD_MATCH_STEP) + ", " + lb;
                strSql += sf(SYSTEM_FIELD_MATCH_RULE) + ", " + lb;
                strSql += sf(SYSTEM_FIELD_MATCH_ID) + ", " + lb;
                strSql += sf(SYSTEM_FIELD_MATCH_ID_PROCESS) + ", " + lb;
                strSql += sf(SYSTEM_FIELD_MATCH_DATE) + ", " + lb;
                strSql += sf(SYSTEM_FIELD_MATCH_ID_STATUS) + ", " + lb;
                strSql += sf(SYSTEM_FIELD_MATCH_FIELD) + ", " + lb;
                strSql += sf(SYSTEM_FIELD_MATCH_DIFF) + ", " + lb;                
                strSql += sf(SYSTEM_FIELD_MATCH_SIDE) + ", " + lb;
                strSql += sf(SYSTEM_FIELD_DATA_SOURCE) + ", " + lb;
                strSql += sf(SYSTEM_FIELD_ID) + lb;
                
                for (String strField1 : ListFields1)
                {
                    strSql += ", " + strField1 + lb;
                }

                strSql += ") " + lb;
                
                strSql += "select " + lb;
                                
                strSql += "tb_3." + sf(SYSTEM_FIELD_ID_PROCESS_ETL) + ", " + lb;
                strSql += "tb_1." + sf(SYSTEM_FIELD_ID_COMPANY) + ", " + lb;
                strSql += "tb_1." + sf(SYSTEM_FIELD_ID_AREA) + ", " + lb;
                strSql += "tb_1." + sf(SYSTEM_FIELD_ID_PROFILE) + ", " + lb;
                strSql += "tb_1." + sf(SYSTEM_FIELD_ID_USER) + ", " + lb;
                strSql += "tb_1." + this.GetFieldObject("id_transaction", ListCENTCatalogMatch1) + ", " + lb;                
                strSql += "tb_1." + sf(SYSTEM_FIELD_MATCH_RECONCILE) + ", " + lb;
                strSql += "tb_1." + sf(SYSTEM_FIELD_MATCH_STEP) + ", " + lb;
                strSql += "tb_1." + sf(SYSTEM_FIELD_MATCH_RULE) + ", " + lb;
                strSql += "tb_1." + sf(this.GetFieldObject("id", ListCENTCatalogMatch1)) + ", " + lb;
                strSql += "tb_1." + this.GetFieldObject("id_process", ListCENTCatalogMatch1) + ", " + lb;
                strSql += "tb_1." + this.GetFieldObject("date", ListCENTCatalogMatch1) + ", " + lb;
                strSql += "tb_1." + this.GetFieldObject("id_status", ListCENTCatalogMatch1) + ", " + lb;
                strSql += "tb_1." + this.GetFieldObject("id_field", ListCENTCatalogMatch1) + ", " + lb;
                strSql += "tb_1." + this.GetFieldObject("difference", ListCENTCatalogMatch1) + ", " + lb;                
                strSql += "tb_1." + this.GetFieldObject("side", ListCENTCatalogMatch1) + ", " + lb;
                strSql += "tb_3." + sf(SYSTEM_FIELD_DATA_SOURCE) + ", " + lb;
                strSql += "tb_2." + this.GetFieldObject("id_record", ListCENTCatalogMatchItem1) + lb;
                
                for (String strField2 : ListFields1)
                {
                    strSql += ", tb_3." + strField2 + lb;
                }

                strSql += " from " + Temp(TEMPORARY_3) + " tb_1" + lb;

                strSql += " inner join " + Temp(TEMPORARY_4) + " tb_2 on" + lb;
                strSql += " tb_1" + "." + sf(this.GetFieldObject("id", ListCENTCatalogMatch1)) + " = " + "tb_2" + "." + this.GetFieldObject("id_match", ListCENTCatalogMatchItem1) + " and " + lb;
                strSql += " tb_1" + "." + this.GetFieldObject("id_process", ListCENTCatalogMatch1) + " = " + "tb_2" + "." + this.GetFieldObject("id_process", ListCENTCatalogMatchItem1) + lb;

                strSql += " inner join " + Temp(TEMPORARY_10) + " tb_3 on" + lb;
                strSql += " tb_2" + "." + this.GetFieldObject("id_record", ListCENTCatalogMatchItem1) + " = " + "tb_3" + "." + sf(SYSTEM_FIELD_ID) + lb;

                PreparedStatement1 = this.GetConnection().prepareStatement(strSql);
                intRowsAffected = PreparedStatement1.executeUpdate();   
                System.out.println("intRowsAffected: " + intRowsAffected);
                PreparedStatement1.close();                                 
                
                /*
                 * Update final results
                 */                     
                strSql = "";
                strSql += "delete from " + this.GetTable(intIdTransaction) + lb;
                strSql += "where " + sf(SYSTEM_FIELD_ID_PROCESS) + " = " + PrepareNumber(intIdProcess);

                PreparedStatement1 = this.GetConnection().prepareStatement(strSql);
                intRowsAffected = PreparedStatement1.executeUpdate();   
                PreparedStatement1.close(); 
                
                /*
                 * Copy final results
                 */
                strSql = "";
                strSql += " insert into " + this.GetTable(intIdTransaction) + lb;
                strSql += " select * from " + Temp(TEMPORARY_9);

                PreparedStatement1 = this.GetConnection().prepareStatement(strSql);
                intRowsAffected = PreparedStatement1.executeUpdate();   
                PreparedStatement1.close(); 

            }
            
            /*
             * Delete records with no status associated (see conciliation setup)
             */
            strSql = "";
            strSql += "delete from " + this.GetTable(intIdTransaction) + lb;
            strSql += "where " + sf(SYSTEM_FIELD_ID_COMPANY) + " = " + this.PrepareNumber(this.GetSession().GetInt(SESSION_COMPANY)) + lb;
            strSql += "and " + sf(SYSTEM_FIELD_ID_PROCESS) + " = " + PrepareNumber(intIdProcess) + lb;            
            strSql += "and " + sf(SYSTEM_FIELD_MATCH_ID_STATUS) + " is null" + lb;
            
            PreparedStatement1 = this.GetConnection().prepareStatement(strSql);
            intRowsAffected = PreparedStatement1.executeUpdate();
            PreparedStatement1.close();            


            
            /*
             * End of all
             */
            return intRowsAffected;
            
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
            PreparedStatement1 = null;
            strSql = null;
            PreparedStatement1 = null;
        }        
    }
    
    
    /*
     * Private methods
     */        
    private String GetFieldList(int intIdTransaction, List<CENTData> ListCENTView1, boolean blnAggregate) throws Exception
    {
        int i = 0;
        int intIdCommand = 0;
        int intIdTransactionView = 0;
        String strFieldObject = "";
        String strFieldName = "";
        String strFieldList = "";
        
        /*
         * Start preparing the sql query
         */            
        for (CENTData CENTView1 : ListCENTView1)
        {
            intIdCommand = CENTView1.GetInt(FIELD_ID_COMMAND);
            strFieldObject = CENTView1.GetText(FIELD_OBJECT);
            strFieldName = CENTView1.GetText(FIELD_NAME);
            intIdTransactionView = CENTView1.GetInt(FIELD_ID_TRN);

            if (intIdTransaction == intIdTransactionView) // Discard join from reconciliation area with trn_match
            {            
                if (!strFieldName.equals("id"))
                {
                    if (isSystemField(strFieldObject) == false)
                    {
                        if (intIdCommand <=SELECTABLE_FIELD)
                        {                        
                            if (i != 0)
                            {
                                strFieldList += ",";
                            }

                            if (blnAggregate)
                            {
                                switch (intIdCommand)
                                {
                                    case COMMAND_SELECT_FIELD:
                                        strFieldList += strFieldObject + lb;
                                        break;

                                    case COMMAND_SELECT_COUNT:
                                        strFieldList += "count(" + strFieldObject + ") " + strFieldObject + lb;
                                        break;

                                    case COMMAND_SELECT_SUM:
                                        strFieldList += "sum(" + strFieldObject + ") " + strFieldObject + lb;
                                        break;

                                    case COMMAND_SELECT_MAX:
                                        strFieldList += "max(" + strFieldObject + ") " + strFieldObject + lb;
                                        break;

                                    case COMMAND_SELECT_MIN:
                                        strFieldList += "min(" + strFieldObject + ") " + strFieldObject + lb;
                                        break;              

                                    case COMMAND_SELECT_AVG:
                                        strFieldList += "avg(" + strFieldObject + ") " + strFieldObject + lb;
                                        break;                                                                
                                }
                            }
                            else
                            {
                                strFieldList += strFieldObject + lb;                            
                            }

                            i = 1;
                        }
                    }
                }
            }
        }       
        
        return strFieldList;
    }
            
    private String GetGroupBy(List<CENTData> ListCENTView1) throws Exception
    {
        int i = 0;
        int intIdCommand = 0;
        String strFieldObject = "";
        String strFieldName = "";
        String strFieldList = "";
                
        /*
         * Start preparing the sql query
         */            
        for (CENTData CENTView1 : ListCENTView1)
        {
            intIdCommand = CENTView1.GetInt(FIELD_ID_COMMAND);
            strFieldObject = CENTView1.GetText(FIELD_OBJECT);
            strFieldName = CENTView1.GetText(FIELD_NAME);

            if (!strFieldName.equals("id"))
            {
                if (isSystemField(strFieldObject) == false)
                {
                    if (intIdCommand <=COMMAND_SELECT_FIELD)
                    {
                        if (i != 0)
                        {
                            strFieldList += ",";
                        }
                        
                        strFieldList += strFieldObject + lb;
                        
                        i = 1;
                    }
                }
            }
        }       
        
        return strFieldList;
    }        
    
}
