/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import business.CBUSCore;
import connection.ConnectionFactory;
import entity.CENTData;
import entity.CENTException;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import project.Global;

/**
 *
 * @author David
 */
public class Job extends project.Global
{
    
    final String LINE_BREAK = "<br>";
    
    final int SUNDAY = 1;
    final int MONDAY = 2;
    final int TUESDAY = 3;
    final int WEDNESDAY = 4;
    final int THURSDAY = 5;
    final int FRIDAY = 6;
    final int SATURDAY = 7;
    final int DAILY = 8;  
    
    final int STEP_TYPE_LAYOUT = 1;   
    final int STEP_TYPE_RECON = 2;   
    
    /*
     * Queue type (domain_27)
     */ 
    final int SCHEDULE_STEP_IMPORT_LAYOUT = 1;
    final int SCHEDULE_STEP_RECONCILE = 2;
    final int SCHEDULE_STEP_SCHEDULE = 3;   
       
    final int PROCESS_BEGIN = 1;
    final int PROCESS_END = 2;
    
    
    /*
     * As the job doesn't start via web, we must create it's own session (via resource file)
     */
    protected CENTData GetJobSession(CENTData CENTCompany1) throws Exception
    {
        /*
         * General Declaration
         */
        final int DEFAULT_PROFILE = 1;
        final int DEFAULT_USER = 1;
        final int DEFAULT_AREA = 1;

        Global Global1 = new Global();
        CENTData CENTSession1 = new CENTData();

        /*
         * Set the company information to the job processing, itś critical to the process
         */
        CENTSession1.SetDate(Global1.SESSION_DATE, new Date());        
        CENTSession1.SetInt(Global1.SESSION_COMPANY, CENTCompany1.GetInt(SYSTEM_FIELD_ID));
        CENTSession1.SetText(Global1.SESSION_COUNTRY, this.GetPropertieValue("COMPANY_COUNTRY"));
        CENTSession1.SetText(Global1.SESSION_LANGUAGE, this.GetPropertieValue("COMPANY_LANGUAGE"));
        CENTSession1.SetInt(Global1.SESSION_PROFILE, DEFAULT_PROFILE);
        CENTSession1.SetInt(Global1.SESSION_USER, DEFAULT_USER);
        CENTSession1.SetInt(Global1.SESSION_AREA, DEFAULT_AREA);

        /*
         * Return it
         */
        return CENTSession1;
    }           
    
    protected void UpdateQueueStatus(CENTData CENTCompany1, int intId, int intProcess, int intIdStatus, String strWarning) throws Exception
    {
        /*
         * General declaration
         */
        CBUSCore CBUSCore1 = null;
        Date datDate = new Date();
        Global Global1 = new Global();
        List<CENTData> ListCENTData1 = new ArrayList<CENTData>();
        List<CENTData> ListCENTData2 = new ArrayList<CENTData>();
        List<CENTData> ListCENTCatalog1 = null;                
        SimpleDateFormat SimpleDateFormat1 = new SimpleDateFormat("hh:mm:ss");        
        ConnectionFactory ConnectionFactory1 = new ConnectionFactory();
        Connection Connection1 = null;
        
        try
        {
            /*
             * Start the connection
             */            
            Connection1 = ConnectionFactory1.GetConnection();            
            
            /*
             * Create the objects
             */
            CBUSCore1 = new CBUSCore(Connection1, this.GetJobSession(CENTCompany1));
            ListCENTCatalog1 = CBUSCore1.GetCatalog(TRN_QUEUE);            
            
            if (strWarning == null)
            {
                strWarning = "";
            }

            /*
             * Query the records not to lost details
             */            
            CENTData CENTFilter1 = new CENTData();
            CENTFilter1.SetInt(Global1.SYSTEM_FIELD_ID, intId);
            CBUSCore1.SetIdTransaction(Global1.TRN_QUEUE);
            CBUSCore1.SetIdView(Global1.DO_NOT_USE_VIEW);
            ListCENTData1 = CBUSCore1.GetList(CENTFilter1);                                 

            /*
             * Update the initial time and status for current process
             */
            if (intProcess == PROCESS_BEGIN)
            {
                for (CENTData CENTData1 : ListCENTData1)
                {
                    CENTData1.SetDate(this.GetFieldObject("date", ListCENTCatalog1), datDate);
                    CENTData1.SetText(this.GetFieldObject("start_date", ListCENTCatalog1), SimpleDateFormat1.format(datDate));
                    CENTData1.SetInt(this.GetFieldObject("id_status", ListCENTCatalog1), intIdStatus);
                    CENTData1.SetInt(this.GetFieldObject("id", ListCENTCatalog1), intId);
                    ListCENTData2.add(CENTData1);
                }
            }
            
            /*
             * Update the time and final status for current process
             */            
            if (intProcess == PROCESS_END)
            {                
                for (CENTData CENTData1 : ListCENTData1)
                {
                    CENTData1.SetText(this.GetFieldObject("end_date", ListCENTCatalog1), SimpleDateFormat1.format(datDate));
                    CENTData1.SetText(this.GetFieldObject("description", ListCENTCatalog1), strWarning);
                    CENTData1.SetInt(this.GetFieldObject("id_status", ListCENTCatalog1), intIdStatus);
                    ListCENTData2.add(CENTData1);                    
                }
            }            

            /*
             * Update the queue status
             */
            CBUSCore1.SetIdTransaction(Global1.TRN_QUEUE);
            CBUSCore1.SetIdView(Global1.DO_NOT_USE_VIEW);
            CBUSCore1.ExecuteEvent(Global1.EVENT_UPDATE, ListCENTData2);

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
             * Close the connection
             */                
            if (Connection1 != null)
            {
                if (!Connection1.isClosed())
                {
                    Connection1.close();
                }
            }                                        
            
            CBUSCore1 = null;
            datDate = null;
            Global1 = null;
            ListCENTData1 = null;
            ListCENTCatalog1 = null;                
            SimpleDateFormat1 = null;
            ConnectionFactory1 = null;
            Connection1 = null;            
        }
    }    

    protected String GetDescriptionById(CENTData CENTCompany1, Connection Connection1, int intIdTransaction, int intId, String strFieldName) throws CENTException, Exception
    {
        /*
         * General Declaration
         */
        String strInfo = null;
        CENTData CENTFilter1 = null;
        CBUSCore CBUSCore1 = null;
        List<CENTData> ListCENTData1 = null;        
        
        try
        {
            /*
             * Create the objects
             */
            CENTFilter1 = new CENTData();
            CBUSCore1 = new CBUSCore(Connection1, this.GetJobSession(CENTCompany1));
            
            /*
             * Filter the area and return the mail
             */            
            CENTFilter1.SetInt(SYSTEM_FIELD_ID, intId);
            CBUSCore1.SetIdTransaction(intIdTransaction);
            CBUSCore1.SetIdView(this.DO_NOT_USE_VIEW);
            ListCENTData1 = CBUSCore1.GetList(CENTFilter1);
            
            for (CENTData CENTData : ListCENTData1)
            {
                strInfo = CENTData.GetText(strFieldName);
            }
            
            /*
             * Return it
             */
            return strInfo;            
            
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
            strInfo = null;
            CENTFilter1 = null;
            CBUSCore1 = null;
            ListCENTData1 = null;
        }
    }            
    
}
