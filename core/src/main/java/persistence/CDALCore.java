package persistence;

/*
 * Developed by David Lancioni - 07/2015
 * Target: Create the system catalog, this schema is used to generated dinamic sql functions
 */
import entity.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author David
 */
public class CDALCore extends CDAL
{
    public CDALCore(Connection Connection1, CENTData CENTSession1) throws SQLException
    {
        super(Connection1, CENTSession1);
    }
    
    /*
     * Generic Methods
     */
    public void Persist(CENTData CENTData1, int intIdEvent) throws CENTException, SQLException, Exception
    {
        /*
         * General Declaration
         */
        int i = 0;
        int intId = 0;
        String strSql = "";        
        String strException = "";
        String strTableName = "";
        PreparedStatement PreparedStatement1 = null;
        List<CENTData> ListCENTCatalog1 = null;

        try
        {                        
            /*
             * Get the catalog related to the current transaction
             */            
            strTableName = this.GetTable(this.GetIdTransaction());
            ListCENTCatalog1 = this.GetCatalog(this.GetIdTransaction());                        
            
            /*
             * Persist according to the action
             */                        
            switch (intIdEvent)
            {
                case EVENT_INSERT:
                    
                    strException = "EXCEPTION_FAIL_TO_INSERT_RECORD";                    

                    /*
                     * Get the next id
                     */
                    intId = this.GetLastId(this.GetIdTransaction());
                    
                    /*
                     * Persist the batch
                     */
                    CENTData1.SetInt(SYSTEM_FIELD_ID, ++intId);                    
                    strSql = super.PrepareStatementToInsert(this.GetIdTransaction(), ListCENTCatalog1, CENTData1, strTableName);
                    PreparedStatement1 = this.GetConnection().prepareStatement(strSql);                        
                    PreparedStatement1.executeUpdate();
                    PreparedStatement1.close();

                    break;    

                case EVENT_UPDATE:      
                    
                    strException = "EXCEPTION_FAIL_TO_UPDATE_RECORD";

                    strSql = super.PrepareStatementToUpdate(CENTData1);
                    PreparedStatement1 = this.GetConnection().prepareStatement(strSql);
                    PreparedStatement1.executeUpdate();
                    PreparedStatement1.close();
                    
                    break;                    
                    
                case EVENT_DELETE:
                    
                    strException = "EXCEPTION_FAIL_TO_DELETE_RECORD";                                        
                    
                    strSql = super.PrepareStatementToDelete(CENTData1);
                    PreparedStatement1 = this.GetConnection().prepareStatement(strSql);
                    PreparedStatement1.executeUpdate();
                    PreparedStatement1.close();
                    
                    break;                                        
            }

        } 
        catch (SQLException SQLException1)
        {            
            throw new CENTException(strException, SQLException1.getMessage());
        }
        catch (Exception Exception1)
        {
            throw Exception1;
        } 
        finally
        {
            PreparedStatement1 = null;
            strSql = null;
            strException = null;
            strTableName = null;
            PreparedStatement1 = null;
            ListCENTCatalog1 = null;
            
        }
    }

    public void Persist(CENTData CENTData1, int intIdEvent, List<CENTData> ListCENTCatalog1) throws CENTException, SQLException
    {
        /*
         * General Declaration
         */
        int i = 0;
        int intId = 0;
        String strSql = "";        
        String strException = "";
        String strTableName = "";
        PreparedStatement PreparedStatement1 = null;

        try
        {                        
            /*
             * Get the catalog related to the current transaction
             */            
            strTableName = this.GetTable(this.GetIdTransaction());
            
            /*
             * Persist according to the action
             */                        
            switch (intIdEvent)
            {
                case EVENT_INSERT:
                    
                    strException = "EXCEPTION_FAIL_TO_INSERT_RECORD";                    

                    /*
                     * Get the next id
                     */
                    if (CENTData1.GetInt(SYSTEM_FIELD_ID) > 0)
                    {
                        // do nothing, use the id
                    }
                    else
                    {
                        intId = this.GetLastId(this.GetIdTransaction());
                        CENTData1.SetInt(SYSTEM_FIELD_ID, ++intId);
                    }
                    /*
                     * Persist the batch
                     */
                    strSql = super.PrepareStatementToInsert(this.GetIdTransaction(), ListCENTCatalog1, CENTData1, strTableName);
                    PreparedStatement1 = this.GetConnection().prepareStatement(strSql);                        
                    PreparedStatement1.executeUpdate();
                    PreparedStatement1.close();

                    break;    

                case EVENT_UPDATE:      
                    
                    strException = "EXCEPTION_FAIL_TO_UPDATE_RECORD";

                    strSql = super.PrepareStatementToUpdate(CENTData1);
                    PreparedStatement1 = this.GetConnection().prepareStatement(strSql);
                    PreparedStatement1.executeUpdate();
                    PreparedStatement1.close();
                    
                    break;                    
                    
                case EVENT_DELETE:
                    
                    strException = "EXCEPTION_FAIL_TO_DELETE_RECORD";                                        
                    
                    strSql = super.PrepareStatementToDelete(CENTData1);
                    PreparedStatement1 = this.GetConnection().prepareStatement(strSql);
                    PreparedStatement1.executeUpdate();
                    PreparedStatement1.close();
                    
                    break;                                        
            }

        } 
        catch (SQLException SQLException1)
        {            
            throw new CENTException(strException, SQLException1.getMessage());
        }
        catch (Exception Exception1)
        {
            throw new CENTException(strException, Exception1.getMessage());
        } 
        finally
        {
            PreparedStatement1 = null;
            strSql = null;
            strException = null;
            strTableName = null;
            PreparedStatement1 = null;
            ListCENTCatalog1 = null;
            
        }
    }    
    
    public List<CENTData> GetList(CENTData CENTData1) throws CENTException, SQLException, Exception
    {
        /*
         * General Declaration
         */
        int intCountInt = 0;
        int intCountText = 0;
        int intCountDouble = 0;
        int intCountDate = 0;
        int intCountBoolean = 0;
        
        int intIdFk = 0;
        int intFieldType = 0;

        String strSql = "";        
        String strFieldName = "";
        String strFieldObject = "";

        CENTData CENTData2 = null;
        ResultSet ResultSet1 = null;
        Statement Statement1 = null;
        
        List<CENTData> ListCENTData1 = new ArrayList<CENTData>();
        List<CENTData> ListCENTData2 = new ArrayList<CENTData>();

        try
        {            
            /*
             * Generate the query based on view or catalog
             */            
            strSql = this.PrepareStatementToGetList(CENTData1);
            Statement1 = this.GetConnection().createStatement();
            ResultSet1 = Statement1.executeQuery(strSql);            

            /*
             * Get the current view or catalog (1 - Catalog / 2 - View)
             */         
            if (this.GetIdEvent() == EVENT_EDIT || this.GetIdView() == DO_NOT_USE_VIEW)
            {
                /*
                 * Get the catalog information
                 */
                ListCENTData1 = this.GetCatalog(this.GetIdTransaction());
                
                /*
                 * Just return based on the field name
                 */
                while (ResultSet1.next())
                {
                    CENTData2 = new CENTData();

                    for (CENTData CENTData3 : ListCENTData1)
                    {
                        if (CENTData3.GetInt(FIELD_ID_COMMAND) <= SELECTABLE_FIELD)
                        {    
                            strFieldName = CENTData3.GetText(FIELD_NAME);
                            strFieldObject = CENTData3.GetText(FIELD_OBJECT);
                            intFieldType = CENTData3.GetInt(FIELD_TYPE);                        

                            /*
                             * Distribute the information according to the type
                             */ 
                            switch (intFieldType)
                            {
                                case TYPE_INT:

                                    CENTData2.SetInt(strFieldObject, ResultSet1.getInt(strFieldObject));
                                    break;

                                case TYPE_TEXT:

                                    if (ResultSet1.getString(strFieldObject) != null)
                                    {
                                        CENTData2.SetText(strFieldObject, ResultSet1.getString(strFieldObject));
                                    }
                                    break;                                

                                case TYPE_DATE:

                                    if (DateToDate(ResultSet1, strFieldObject) != null)
                                    {                            
                                        CENTData2.SetDate(strFieldObject, DateToDate(ResultSet1, strFieldObject));
                                    }
                                    break;

                                case TYPE_DOUBLE:

                                    CENTData2.SetDouble(strFieldObject, ResultSet1.getDouble(strFieldObject));
                                    break;      

                                case TYPE_BOOLEAN:

                                    if (ResultSet1.getInt(strFieldObject) != Null)
                                    {                            
                                        CENTData2.SetBoolean(strFieldObject, ResultSet1.getInt(strFieldObject));
                                    }
                                    break;  

                            } // Column type                        

                        } // Selectable

                    } // Data

                    ListCENTData2.add(CENTData2);                
                }                
            }
            else
            {
                
                /*
                 * Get the view definition
                 */                
                ListCENTData1 = this.GetView(this.GetIdView());
                
                /*
                 * Fill the list according to the view. Here we have specific rules to handle with fields and joins
                 */
                while (ResultSet1.next())
                {
                    intCountInt = 0;
                    intCountText = 0;
                    intCountDouble = 0;
                    intCountDate = 0;
                    intCountBoolean = 0;                    
                    
                    CENTData2 = new CENTData();

                    for (CENTData CENTData3 : ListCENTData1)
                    {
                        if (CENTData3.GetInt(FIELD_ID_COMMAND) <= SELECTABLE_FIELD)
                        {    
                            strFieldName = CENTData3.GetText(FIELD_NAME);
                            intFieldType = CENTData3.GetInt(FIELD_TYPE);
                            intIdFk = CENTData3.GetInt(FIELD_ID_FK);

                            if (intIdFk != 0)
                            {
                                intFieldType = TYPE_TEXT;
                            }

                            switch (intFieldType)
                            {
                                case TYPE_INT:

                                    intCountInt ++;
                                    strFieldObject = "int_" + intCountInt;                                    
                                                                                                            
                                    if (strFieldName.equals("id"))
                                        CENTData2.SetInt(SYSTEM_FIELD_ID, ResultSet1.getInt(strFieldObject));
                                    else            
                                        CENTData2.SetInt(strFieldObject, ResultSet1.getInt(strFieldObject));
                                    
                                    break;

                                case TYPE_TEXT:

                                    intCountText ++;
                                    strFieldObject = "text_" + intCountText;

                                    if (ResultSet1.getString(strFieldObject) != null)
                                    {
                                        CENTData2.SetText(strFieldObject, ResultSet1.getString(strFieldObject));
                                    }                                

                                    break;

                                case TYPE_DATE:

                                    intCountDate ++;
                                    strFieldObject = "date_" + intCountDate;

                                    if (DateToDate(ResultSet1, strFieldObject) != null)
                                    {
                                        CENTData2.SetDate(strFieldObject, DateToDate(ResultSet1, strFieldObject));
                                    }

                                    break;

                                case TYPE_DOUBLE:

                                    intCountDouble ++;
                                    strFieldObject = "double_" + intCountDouble;                                
                                    CENTData2.SetDouble(strFieldObject, ResultSet1.getDouble(strFieldObject));                                
                                    break;

                                case TYPE_BOOLEAN:

                                    intCountBoolean ++;
                                    strFieldObject = "double_" + intCountBoolean;                                
                                    CENTData2.SetInt(strFieldObject, ResultSet1.getInt(strFieldObject));                                
                                    break;                        
                            }
                        } // Selectable                       
                    } // Data
                    
                    ListCENTData2.add(CENTData2);                    
                }
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
            return ListCENTData2;

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
            strFieldName = null;

            CENTData2 = null;
        
            ListCENTData1 = null;
            ListCENTData2 = null;
        }
    }

    public List<CENTData> GetList(List<CENTData> ListCENTView1) throws CENTException, SQLException, Exception
    {
        /*
         * General Declaration
         */    
        int intIdCommand = 0;
        int intRecordCount = 0;
        int intPageBegin = 0;
        int intPageEnd = 0;        
        int intCountInt = 0;
        int intCountText = 0;
        int intCountDouble = 0;
        int intCountDate = 0;
        int intCountBoolean = 0;

        int intIdFk = 0;
        int intFieldType = 0;

        String strSql = "";
        String strSqlCount = "";
        String strFieldName = "";
        String strFieldObject = "";
        String strDB = "";

        CENTData CENTData2 = null;
        ResultSet ResultSet1 = null;
        Statement Statement1 = null;
        
        List<CENTData> ListCENTData2 = new ArrayList<CENTData>();

        try
        {                        
            /*
             * Query the records
             */
            strSql = GetSqlQueryFromView(ListCENTView1, new CENTData());
            
            /*
             * Keep the sql without paging references and apply count
             */            
            strSqlCount = "";
            strSqlCount += "select count(*) total " + lb;
            strSqlCount += "from " + lb;
            strSqlCount += "(" + lb;
            strSqlCount += strSql;
            strSqlCount += ") tb_count" + lb;            
            
            /*
             * Control paging
             */
            for (CENTData CENTView1 : ListCENTView1)
            {
                if (CENTView1.GetText(FIELD_OBJECT).equals(SYSTEM_FIELD_PAGING_START))
                {
                    intPageBegin = StringToInteger(CENTView1.GetText(FIELD_CONDITION_VALUE));
                }
                
                if (CENTView1.GetText(FIELD_OBJECT).equals(SYSTEM_FIELD_PAGING_END))
                {
                    intPageEnd = StringToInteger(CENTView1.GetText(FIELD_CONDITION_VALUE));
                }                
            }
         
            /*
             * Paging control is different for MySQL
             */
            strSql += " where t1.row_id between " + intPageBegin + " and " + intPageEnd + lb;

            
            /*
             * Count the records
             */
            Statement1 = this.GetConnection().createStatement();
            ResultSet1 = Statement1.executeQuery(strSqlCount);

            while (ResultSet1.next())
            {
                intRecordCount = ResultSet1.getInt("total");
            }            

            /*
             * Fill the list according to the view. Here we have specific rules to handle with fields and joins
             */                        
            Statement1 = this.GetConnection().createStatement();
            ResultSet1 = Statement1.executeQuery(strSql);

            /*
             * Fill the list according to the view. Here we have specific rules to handle with fields and joins
             */
            while (ResultSet1.next())
            {
                intCountInt = 0;
                intCountText = 0;
                intCountDouble = 0;
                intCountDate = 0;
                intCountBoolean = 0;                    

                CENTData2 = new CENTData();

                for (CENTData CENTView1 : ListCENTView1)
                {
                    if (CENTView1.GetInt(FIELD_ID_COMMAND) <= SELECTABLE_FIELD)
                    {    
                        strFieldName = CENTView1.GetText(FIELD_NAME);
                        intFieldType = CENTView1.GetInt(FIELD_TYPE);
                        intIdFk = CENTView1.GetInt(FIELD_ID_FK);
                        intIdCommand = CENTView1.GetInt(FIELD_ID_COMMAND);

                        if (intIdFk != 0)
                        {
                            intFieldType = TYPE_TEXT;
                        }                        
                        
                        CENTData2.SetInt(SYSTEM_FIELD_RECORD_COUNT, intRecordCount);

                        switch (intFieldType)
                        {
                            case TYPE_INT:

                                intCountInt ++;
                                strFieldObject = "int_" + intCountInt;                                
                                CENTData2.SetInt(strFieldObject, ResultSet1.getInt(strFieldObject));
                                break;

                            case TYPE_TEXT:

                                intCountText ++;
                                strFieldObject = "text_" + intCountText;                                
                                
                                if (intIdCommand == COMMAND_SELECT_COUNT) // count(date) return int or double
                                {                                    
                                    if (ResultSet1.getString(strFieldObject) != null)
                                    {
                                        CENTData2.SetDouble(String.valueOf("double_" + (++intCountDouble)), ResultSet1.getDouble(strFieldObject));
                                    }
                                }
                                else
                                {                                    
                                    if (ResultSet1.getString(strFieldObject) != null)
                                    {
                                        CENTData2.SetText(strFieldObject, ResultSet1.getString(strFieldObject));
                                    }
                                }                                

                                break;

                            case TYPE_DATE:

                                intCountDate ++;
                                strFieldObject = "date_" + intCountDate;

                                if (intIdCommand == COMMAND_SELECT_COUNT) // count(date) return int or double
                                {
                                    if (ResultSet1.getString(strFieldObject) != null)
                                    {
                                        CENTData2.SetDouble(String.valueOf("double_" + (++intCountDouble)), ResultSet1.getDouble(strFieldObject));
                                    }
                                }
                                else
                                {
                                    if (DateToDate(ResultSet1, strFieldObject) != null)
                                    {
                                        CENTData2.SetDate(strFieldObject, DateToDate(ResultSet1, strFieldObject));
                                    }
                                }                                

                                break;

                            case TYPE_DOUBLE:

                                intCountDouble ++;
                                strFieldObject = "double_" + intCountDouble;                                
                                CENTData2.SetDouble(strFieldObject, ResultSet1.getDouble(strFieldObject));                                
                                break;

                            case TYPE_BOOLEAN:

                                intCountBoolean ++;
                                strFieldObject = "double_" + intCountBoolean;                                
                                CENTData2.SetInt(strFieldObject, ResultSet1.getInt(strFieldObject));                                
                                break;                        
                        }
                        
                    } // Selectable
                    
                } // Data

                ListCENTData2.add(CENTData2);
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
            return ListCENTData2;

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
            strFieldName = null;
            strFieldObject = null;

            CENTData2 = null;
            ResultSet1 = null;
            Statement1 = null;

            ListCENTData2 = null;
        }
    }    
    
    public List<CENTData> GetCount(CENTData CENTData1) throws CENTException, SQLException, Exception
    {
        /*
         * General Declaration
         */        
        String strSql = "";
        
        CENTData CENTData2 = null;
        
        ResultSet ResultSet1 = null;
        Statement Statement1 = null;
        
        List<CENTData> ListCENTData1 = new ArrayList<CENTData>();

        try
        {                        
            /*
             * Generate the query, if cannot do using the view, use the catalog
             */            
            strSql = this.PrepareStatementToGetCount(CENTData1);
            Statement1 = this.GetConnection().createStatement();
            ResultSet1 = Statement1.executeQuery(strSql);            

            /*
             * Get the view dictionary
             */
            while (ResultSet1.next())
            {
                CENTData2 = new CENTData();
                
                CENTData2.SetInt(1, ResultSet1.getInt(1));                          // Min
                CENTData2.SetInt(2, ResultSet1.getInt(2));                          // Max
                CENTData2.SetInt(3, ResultSet1.getInt(3));                          // Count
                
                ListCENTData1.add(CENTData2);                
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
            Statement1 = null;
            ResultSet1 = null;            
            strSql = null;
            CENTData2 = null;
            ListCENTData1 = null;
        }
    }
    
    public int DeleteAll(int intIdTransaction, String strObjectFk, int intValue) throws CENTException, SQLException
    {
        /*
         * General Declaration
         */
        int i = 0;
        int intRowsAffected = 0;
        String strSql = "";        
        String strException = "";
        PreparedStatement PreparedStatement1 = null;

        try
        {                        
            /*
             * Delete all information related to the parameters
             */            
            strSql += "delete from " + this.GetTable(intIdTransaction) + lb;
            strSql += "where " + sf(SYSTEM_FIELD_ID_COMPANY) + " = " + this.GetSession().GetInt(SESSION_COMPANY) + lb;
            
            if (!strObjectFk.equals(""))
            {
                if (strObjectFk.equals(SYSTEM_FIELD_ID))
                {
                    strObjectFk = sf(strObjectFk);
                }
                strSql += "and " + strObjectFk + " = " + intValue + lb;
            }
                        
            PreparedStatement1 = this.GetConnection().prepareStatement(strSql);
            intRowsAffected = PreparedStatement1.executeUpdate();
            PreparedStatement1.close();
            
            /*
             * Return it
             */
            return intRowsAffected;

        } 
        catch (SQLException SQLException1)
        {            
            throw new CENTException(strException, SQLException1.getMessage());
        }
        catch (Exception Exception1)
        {
            throw new CENTException(strException, Exception1.getMessage());
        } 
        finally
        {
            strSql = null;
            strException = null;            
            PreparedStatement1 = null;            
            PreparedStatement1 = null;            
        }
    }    
    
    
    /*
     * Access Control methods
     */
    public void UndoDataImport(int intIdProcess, int intIdTransaction) throws CENTException, SQLException
    {
        /*
         * General Declaration
         */
        int intRowsAffected = 0;
        
        String strSql = "";        
        String strException = "";
        String strTableName = "";
        PreparedStatement PreparedStatement1 = null;

        try
        {                        
            strTableName = this.GetTable(intIdTransaction);
            
            strSql = "";
            strSql += "delete from " + strTableName + lb;
            strSql += "where " + sf(SYSTEM_FIELD_ID_COMPANY) + " = " + this.GetSession().GetInt(SESSION_COMPANY) + lb;            
            strSql += "and " + sf(SYSTEM_FIELD_ID_PROCESS_ETL) + " = " + this.PrepareNumber(intIdProcess) + lb;
                        
            PreparedStatement1 = this.GetConnection().prepareStatement(strSql);
            intRowsAffected = PreparedStatement1.executeUpdate();
            PreparedStatement1.close();

        } 
        catch (SQLException SQLException1)
        {            
            throw new CENTException(strException, SQLException1.getMessage());
        }
        catch (Exception Exception1)
        {
            throw new CENTException(strException, Exception1.getMessage());
        } 
        finally
        {
            PreparedStatement1 = null;            
            strSql = null;
            strException = null;
            strTableName = null;
            PreparedStatement1 = null;            
        }
    }    

    public void UndoRecon(int intIdProcess) throws CENTException, SQLException
    {
        /*
         * General Declaration
         */
        int intRowsAffected = 0;
        
        String strSql = "";        
        String strException = "";
        String strTableName = "";
        PreparedStatement PreparedStatement1 = null;

        try
        {                                    
            /*
             * Delete the match items
             */
            strTableName = this.GetTable(TRN_MATCH_ITEM);
            
            strSql = "";
            strSql += "delete from " + strTableName + lb;
            strSql += "where " + sf(SYSTEM_FIELD_ID_COMPANY) + " = " + this.GetSession().GetInt(SESSION_COMPANY) + lb;            
            strSql += "and " + GetFieldObject("id_process", GetCatalog(TRN_MATCH_ITEM)) + " = " + this.PrepareNumber(intIdProcess) + lb;
                        
            PreparedStatement1 = this.GetConnection().prepareStatement(strSql);
            intRowsAffected = PreparedStatement1.executeUpdate();
            PreparedStatement1.close();
            
            /*
             * Delete the matches
             */
            strTableName = this.GetTable(TRN_MATCH);
            
            strSql = "";
            strSql += "delete from " + strTableName + lb;
            strSql += "where " + sf(SYSTEM_FIELD_ID_COMPANY) + " = " + this.GetSession().GetInt(SESSION_COMPANY) + lb;            
            strSql += "and " + GetFieldObject("id_process", GetCatalog(TRN_MATCH)) + " = " + this.PrepareNumber(intIdProcess) + lb;
                        
            PreparedStatement1 = this.GetConnection().prepareStatement(strSql);
            intRowsAffected = PreparedStatement1.executeUpdate();
            PreparedStatement1.close();            

        } 
        catch (SQLException SQLException1)
        {            
            throw new CENTException(strException, SQLException1.getMessage());
        }
        catch (Exception Exception1)
        {
            throw new CENTException(strException, Exception1.getMessage());
        } 
        finally
        {
            PreparedStatement1 = null;            
            strSql = null;
            strException = null;
            strTableName = null;
            PreparedStatement1 = null;            
        }
    }        
    
}
