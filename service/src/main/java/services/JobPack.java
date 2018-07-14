/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import business.CBUSCore;
import business.CBUSEtl;
import business.CBUSReconcile;
import connection.ConnectionFactory;
import entity.CENTData;
import entity.CENTException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import project.Global;

/**
 *
 * @author David
 */
public class JobPack extends Job implements Runnable
{
    private CENTData CENTCompany1;    
    private int intIdQueue = 0;
    private int intIdService = 0;
    
    public JobPack(CENTData CENTCompany1, int intQueue, int intService)
    {
        this.CENTCompany1 = CENTCompany1;
        this.intIdQueue = intQueue;
        this.intIdService = intService;
    }    
    
    @Override
    public void run()
    {
        /*
         * General Declartion
         */
        String strMailTo = "";
        String strMailSubject = "";
        String strMailBody = "";
        String strMessage = "";            
        String strServiceName = "";
        
        int intIdProcess = 0;
        
        CENTData CENTLayout1 = null;
        CENTData CENTReconcile1 = null;
        CENTData CENTFilter1 = null;

        CBUSCore CBUSCore1 = null;        
        CBUSEtl CBUSEtl1 = null;
        Global Global1 = new Global();        
        CBUSReconcile CBUSReconcile1 = null;
        ConnectionFactory ConnectionFactory1 = new ConnectionFactory();
                
        List<CENTData> ListCENTCatalogScheduleJob1 = null;                
        List<CENTData> ListCENTReconcile1 = new ArrayList<CENTData>();
        List<CENTData> ListCENTLayout1 = new ArrayList<CENTData>();
        List<CENTData> ListCENTJob1 = new ArrayList<CENTData>();
        
        Connection Connection1 = null;

        try
        {                
            /*
             * Get the connection
             */
            Connection1 = ConnectionFactory1.GetConnection();
            
            /*
             * Create the objects
             */
            CBUSCore1 = new CBUSCore(Connection1, this.GetJobSession(CENTCompany1));
            CBUSEtl1 = new CBUSEtl(Connection1, this.GetJobSession(CENTCompany1));
            CBUSReconcile1 = new CBUSReconcile(Connection1, this.GetJobSession(CENTCompany1));
            

            /*
             * Mail information to report the users
             */
            strMailTo = this.GetDescriptionById(CENTCompany1, Connection1, TRN_AREA, 1, GetFieldObject("email", CBUSCore1.GetCatalog(TRN_AREA)));
            strServiceName = this.GetDescriptionById(CENTCompany1, Connection1, TRN_LAYOUT, intIdService, GetFieldObject("name", CBUSCore1.GetCatalog(TRN_LAYOUT)));                        
            
            /*
             * Get the catalogs
             */            
            ListCENTCatalogScheduleJob1 = CBUSCore1.GetCatalog(TRN_SCHEDULE_JOB);            

            /*
             * Get the list of pending schedules for today
             */
            CENTFilter1 = new CENTData();
            CENTFilter1.SetInt(this.GetFieldObject("id_scheduler", ListCENTCatalogScheduleJob1), intIdService);
            CBUSCore1.SetIdTransaction(Global1.TRN_SCHEDULE_JOB);
            CBUSCore1.SetIdView(Global1.DO_NOT_USE_VIEW);
            ListCENTJob1 = CBUSCore1.GetList(CENTFilter1);            

            if (!ListCENTJob1.isEmpty())
            {
                /*
                 * Update queue status (must be outside transaction)
                 */                
                UpdateQueueStatus(CENTCompany1, intIdQueue, PROCESS_BEGIN, Global1.QUEUE_STATUS_RUNNING, "");                

                /*
                 * Create a new connection
                 */
                Connection1.setAutoCommit(false);

                for (CENTData CENTJob1 : ListCENTJob1)
                {                 
                    /*
                     * Create a unique Id process for the group or for individual execution
                     */
                    intIdProcess = Integer.valueOf(String.valueOf(intIdQueue) + String.valueOf(CENTJob1.GetInt(this.GetFieldObject("id_service", CBUSCore1.GetCatalog(TRN_SCHEDULE_JOB)))));
                    
                    /*
                     * Execute related process
                     */                    
                    switch (CENTJob1.GetInt(this.GetFieldObject("id_type", ListCENTCatalogScheduleJob1)))
                    {
                        case SCHEDULE_STEP_IMPORT_LAYOUT:

                            CENTLayout1 = new CENTData();
                            CENTLayout1.SetInt(Global1.SYSTEM_FIELD_ID, CENTJob1.GetInt(this.GetFieldObject("id_service", ListCENTCatalogScheduleJob1)));
                            CENTLayout1.SetInt(Global1.SYSTEM_FIELD_ID_PROCESS_ETL, intIdProcess);
                            ListCENTLayout1.add(CENTLayout1);
                            CBUSEtl1.Import(ListCENTLayout1);

                            break;

                        case SCHEDULE_STEP_RECONCILE:

                            CENTReconcile1 = new CENTData();
                            CENTReconcile1.SetInt(Global1.SYSTEM_FIELD_ID, CENTJob1.GetInt(this.GetFieldObject("id_service", ListCENTCatalogScheduleJob1)));
                            CENTReconcile1.SetInt(Global1.SYSTEM_FIELD_ID_PROCESS, intIdProcess);
                            ListCENTReconcile1.add(CENTReconcile1);
                            CBUSReconcile1.Reconciliation(ListCENTReconcile1);

                            break;
                    }
                }            

                /*
                 * Commit the transaction
                 */
                Connection1.commit();

                /*
                 * Update queue status (SUCCESS)
                 */
                UpdateQueueStatus(CENTCompany1, intIdQueue, PROCESS_END,  Global1.QUEUE_STATUS_SUCCESS, "");

                /*
                 * Mail information for success
                 */
                strMailSubject = CBUSCore1.Translate("MESSAGE_MAIL_SUBJECT_SCHEDULE_SUCCESS");
                strMailBody = CBUSCore1.Translate("MESSAGE_MAIL_BODY_SCHEDULE_SUCCESS");
                strMailBody += LINE_BREAK;
                strMailBody += LINE_BREAK;
                strMailBody += strServiceName;

                /*
                 * Log exception
                 */            
                Log(strMailBody);            

                /*
                 * Report on terminal
                 */            
                System.out.println(strMailBody);
            }
        }            
        catch (CENTException CENTException1)
        {
           /*
            * Rollback the transaction
            */                  
            try
            {
                /*
                 * Fail, undo the job
                 */                  
                Connection1.rollback();

                /*
                 * Update queue status (FAIL)
                 */
                strMessage = CBUSCore1.Translate(CENTException1.GetCode());
                
                if (CENTException1.GetMessage() != null)
                {
                    if (!CENTException1.GetMessage().equals(""))
                    {
                        strMessage += " : " + CBUSCore1.Translate(CENTException1.GetMessage());        
                    }
                }
                        
                UpdateQueueStatus(CENTCompany1, intIdQueue, PROCESS_END,  Global1.QUEUE_STATUS_FAIL, strMessage);
                
                /*
                 * Mail information for fail
                 */            
                strMailSubject = CBUSCore1.Translate("MESSAGE_MAIL_SUBJECT_SCHEDULE_FAIL");
                strMailBody = CBUSCore1.Translate("MESSAGE_MAIL_BODY_SCHEDULE_FAIL");
                strMailBody += strServiceName;
                strMailBody += LINE_BREAK;
                strMailBody += LINE_BREAK;                
                strMailBody +=  strMessage;
                strMailBody += LINE_BREAK;
                strMailBody += LINE_BREAK;                

                /*
                 * Log exception
                 */            
                Log(strMailBody);
                
                /*
                 * Report on terminal
                 */            
                System.out.println(strMailBody);
                
            }                 
            catch (Exception Exception1)
            {
                System.out.println(Exception1.getMessage());
            }
        }                    
        catch (Exception Exception1)
        {
           /*
            * Rollback the transaction
            */                
            try
            {
                /*
                 * Fail, undo the job
                 */                  
                Connection1.rollback();

                /*
                 * Update queue status (FAIL)
                 */                
                strMessage = Exception1.getMessage();
                UpdateQueueStatus(CENTCompany1, intIdQueue, PROCESS_END,  Global1.QUEUE_STATUS_FAIL, strMessage);
                
                /*
                 * Mail information for fail
                 */            
                strMailSubject = CBUSCore1.Translate("MESSAGE_MAIL_SUBJECT_SCHEDULE_FAIL");
                strMailBody = CBUSCore1.Translate("MESSAGE_MAIL_BODY_SCHEDULE_FAIL");
                strMailBody += LINE_BREAK;
                strMailBody += LINE_BREAK;
                strMailBody += strServiceName;
                strMailBody += LINE_BREAK;
                strMailBody += LINE_BREAK;                
                strMailBody += strMessage;
                
                /*
                 * Log exception
                 */            
                Log(strMailBody);                
                
                /*
                 * Report on terminal
                 */            
                System.out.println(strMailBody);

            }                 
            catch (Exception Exception2)
            {
                System.out.println(Exception2.getMessage());
            }                                
        }
        finally
        {
            /*
             * Report situation by e-mail
             */
            try
            {
                SendMail(strMailTo, strMailSubject, strMailBody);
            }
            catch (Exception Exception1)
            {
                System.out.println(Exception1.getMessage());
            }
            
            /*
             * Close the connection
             */            
            try
            {
                if (Connection1 != null)
                {
                    if (!Connection1.isClosed())
                    {
                        Connection1.close();
                    }
                }
            }
            catch (Exception Exception1)
            {
                System.out.println(Exception1.getMessage());
            }
            
            /*
             * Finalize
             */
            strMailTo = null;
            strMailSubject = null;
            strMailBody = null;
            strMessage = null;            
            strServiceName = null;
            CENTReconcile1 = null;
            Global1 = null;        
            CBUSReconcile1 = null;
            ListCENTReconcile1 = null;
            ConnectionFactory1 = null;
            Connection1 = null;
        }
    }
}
