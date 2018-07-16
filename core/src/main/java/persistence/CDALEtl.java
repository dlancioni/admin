package persistence;

import entity.CENTData;
import entity.CENTException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 *
 * @author David
 */
public class CDALEtl extends CDAL
{
    public CDALEtl(Connection Connection1, CENTData CENTSession1) throws SQLException
    {
        super(Connection1, CENTSession1);
    }

    public void Persist(int intIdTransaction, List<CENTData> ListCENTCatalog1, CENTData CENTData1, String strTable) throws CENTException, Exception
    {
        /*
         * General Declaration
         */
        String strSql = "";
        PreparedStatement PreparedStatement1 = null;
        
        try
        {
            /*
             * Persist the record
             */            
            if (CENTData1.GetInt(SYSTEM_FIELD_ID_LAYOUT_LINE_STATE) == LINE_STATE_IMPORT)
            {
                strSql = this.PrepareStatementToInsert(intIdTransaction, ListCENTCatalog1, CENTData1, strTable);

                PreparedStatement1 = this.GetConnection().prepareStatement(strSql);                        
                PreparedStatement1.execute();
                PreparedStatement1.close();                
            }
            
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
        }
    }
    
    public void Delete(int intIdProcess, int intIdTransaction) throws Exception
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
             * Delete current position to avoid duplicated import
             */
            strSql = "";
            strSql += " delete from " + this.GetTable(intIdTransaction);
            strSql += " where " + sf(SYSTEM_FIELD_ID_COMPANY) + " = " + this.GetSession().GetInt(SESSION_COMPANY);
            strSql += " and " + sf(SYSTEM_FIELD_ID_PROCESS_ETL) + " = " + intIdProcess;

            PreparedStatement1 = this.GetConnection().prepareStatement(strSql);
            intRowsAffected = PreparedStatement1.executeUpdate();
            PreparedStatement1.close();
            
        }
        catch (Exception Exception1)
        {
            /*
             * Throw it
             */
            throw Exception1;
        }
        finally
        {
            PreparedStatement1 = null;            
            strSql = null;            
        }
    }

    public void UpdateProcessId(int intIdTransaction, int intIdLayout, int intIdProcess, int intIdArea) throws Exception
    {
        /*
         * General Declaration
         */
        String strSql = "";
        PreparedStatement PreparedStatement1 = null;
        int intRowsAffected = 0;

        try
        {
            /*
             * Delete current position to avoid duplicated import
             */            
            strSql = "";
            strSql += " update " + this.GetTable(intIdTransaction) + " set" + lb;
            
            strSql += sf(SYSTEM_FIELD_ID_AREA) + " = " + intIdArea + "," + lb;
            strSql += sf(SYSTEM_FIELD_ID_PROCESS_ETL) + " = " + intIdProcess + lb;

            strSql += " where " + sf(SYSTEM_FIELD_ID_COMPANY) + " = " + this.GetSession().GetInt(SESSION_COMPANY) + lb;
            strSql += " and " + sf(SYSTEM_FIELD_ID_LAYOUT) + " = " + intIdLayout + lb;
            strSql += " and " + sf(SYSTEM_FIELD_ID_PROCESS_ETL) + " is null" + lb;

            PreparedStatement1 = this.GetConnection().prepareStatement(strSql);
            intRowsAffected = PreparedStatement1.executeUpdate();
            PreparedStatement1.close();
            
        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }
        finally
        {
            /*
             * Destroy the objects (Oracle cursor issue)
             */
            PreparedStatement1 = null;            
            strSql = null;            
        }
    }    
 
    public String UpdateHeaderTrailer(CENTData CENTData1)  throws CENTException, SQLException, Exception
    {
        /*
         * General Declaration
         */        
        double dblValue = 0;        
        
        int blnValue = Null; 
        int intValue = 0;
        int intFieldType = 0;
        int intRowsAffected = 0;
        
        Date datValue = null;
        
        String strSql = "";        
        String strFieldObject = "";
        String strFieldValue = "";
        String strTableName = "";

        PreparedStatement PreparedStatement1 = null;        
        List<CENTData> ListCENTCatalog1 = null;

        try
        {
            /*
             * Get the catalog to generate the SQL
             */
            ListCENTCatalog1 = this.GetCatalog(this.GetIdTransaction());
            strTableName = this.GetTable(this.GetIdTransaction());

            strSql += "update " + strTableName + " set" + lb;              
            strSql += sf(SYSTEM_FIELD_ID_COMPANY) + " = " + this.GetSession().GetInt(SESSION_COMPANY) + lb; // just used to fix concatenation           
            
            /*
             * Generate the SQL
             */
            for (CENTData CENTCatalog1 : ListCENTCatalog1)
            {
                intFieldType = CENTCatalog1.GetInt(FIELD_TYPE); 
                strFieldObject = CENTCatalog1.GetText(FIELD_OBJECT);
                                
                switch (intFieldType)
                {
                    case TYPE_INT:

                        intValue = CENTData1.GetInt(strFieldObject);

                        if (intValue != Integer.MIN_VALUE)
                        {                
                            if (isSystemField(strFieldObject))
                            {
                                strFieldObject = sf(strFieldObject);
                            }

                            strSql += ", " + strFieldObject + " = " + this.PrepareNumber(intValue) + lb;;
                        }

                        break;

                    case TYPE_TEXT:

                        strFieldValue = CENTData1.GetText(strFieldObject);

                        if (!strFieldValue.trim().equals("") && !strFieldValue.trim().equals("''"))
                        {
                            if (isSystemField(strFieldObject))
                            {
                                strFieldObject = sf(strFieldObject);
                            }

                            strSql += ", " + strFieldObject + " = " + this.PrepareString(strFieldValue) + lb;;
                        }

                        break;

                    case TYPE_DATE:

                        datValue = CENTData1.GetDate(strFieldObject);

                        if (datValue != null)
                        {
                            if (isSystemField(strFieldObject))
                            {
                                strFieldObject = sf(strFieldObject);
                            }

                            strSql += ", " + strFieldObject + " = " + this.PrepareDate(datValue) + lb;
                        }                        

                        break;

                    case TYPE_DOUBLE:

                        dblValue = CENTData1.GetDouble(strFieldObject);

                        if (dblValue != Double.MIN_VALUE)
                        {
                            if (isSystemField(strFieldObject))
                            {
                                strFieldObject = sf(strFieldObject);
                            }

                            strSql += ", " + strFieldObject + " = " + this.PrepareNumber(dblValue) + lb;;
                        }

                        break;

                    case TYPE_BOOLEAN:

                        blnValue = CENTData1.GetBoolean(strFieldObject);                        

                        if (blnValue != Null)
                        {
                            if (isSystemField(strFieldObject))
                            {
                                strFieldObject = sf(strFieldObject);
                            }

                            strSql += ", " + strFieldObject + " = " + this.PrepareNumber(blnValue);
                        }                        

                        break;
                }
            }
                
            /*
             * Prepare the SQL Statement - Start with where clause for system fields
             */
            strSql += " where " + sf(SYSTEM_FIELD_ID_COMPANY) + " = " + this.PrepareNumber(this.GetSession().GetInt(SESSION_COMPANY)) + lb;;
            strSql += " and " + sf(SYSTEM_FIELD_ID_TRANSACTION) + " = " + this.PrepareNumber(this.GetIdTransaction()) + lb;;
            strSql += " and " + sf(SYSTEM_FIELD_ID_LAYOUT) + " = " + this.PrepareNumber(CENTData1.GetInt(SYSTEM_FIELD_ID_LAYOUT)) + lb;;
            strSql += " and " + sf(SYSTEM_FIELD_ID_PROCESS_ETL) + " is null ";            

            PreparedStatement1 = this.GetConnection().prepareStatement(strSql);                        
            intRowsAffected = PreparedStatement1.executeUpdate();
            PreparedStatement1.close();            
            
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
            strFieldObject = null;
            strFieldValue = null;
            strTableName = null;            
            ListCENTCatalog1 = null;
        }
    }    
    
}
