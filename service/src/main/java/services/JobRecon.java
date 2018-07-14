/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import business.CBUSCore;
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
public class JobRecon extends Job implements Runnable
{
    private CENTData CENTCompany1 = null;
    private int intIdQueue = 0;
    private int intIdService = 0;
    
    public JobRecon(CENTData CENTCompany1, int intQueue, int intService)
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
        CENTData CENTReconcile1 = null;
        Global Global1 = new Global();        
        CBUSCore CBUSCore1 = null;
        CBUSReconcile CBUSReconcile1 = null;
        List<CENTData> ListCENTReconcile1 = new ArrayList<CENTData>();
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
            CBUSReconcile1 = new CBUSReconcile(Connection1, this.GetJobSession(CENTCompany1));

            /*
             * Mail information to report the users
             */
            strMailTo = this.GetDescriptionById(CENTCompany1, Connection1, TRN_AREA, 1, GetFieldObject("email", CBUSCore1.GetCatalog(TRN_AREA)));
            strServiceName = this.GetDescriptionById(CENTCompany1, Connection1, TRN_LAYOUT, intIdService, GetFieldObject("name", CBUSCore1.GetCatalog(TRN_LAYOUT)));            

            /*
             * Update queue status (begin)
             */                
            UpdateQueueStatus(CENTCompany1, intIdQueue, PROCESS_BEGIN, Global1.QUEUE_STATUS_RUNNING, "");

            /*
             * Create a new connection
             */
            Connection1.setAutoCommit(false);

            /*
             * Import data
             */           
            CENTReconcile1 = new CENTData();            
            CENTReconcile1.SetInt(Global1.SYSTEM_FIELD_ID, intIdService);                  // Reconcile to be executed
            CENTReconcile1.SetInt(Global1.SYSTEM_FIELD_ID_PROCESS, intIdQueue);  // Unique id to allow multiple processes
            ListCENTReconcile1.add(CENTReconcile1);
            CBUSReconcile1.Reconciliation(ListCENTReconcile1);

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
            strMailSubject = CBUSCore1.Translate("MESSAGE_MAIL_SUBJECT_RECON_SUCCESS");
            strMailBody = CBUSCore1.Translate("MESSAGE_MAIL_BODY_RECON_SUCCESS");
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
                strMailSubject = CBUSCore1.Translate("MESSAGE_MAIL_SUBJECT_RECON_FAIL");
                strMailBody = CBUSCore1.Translate("MESSAGE_MAIL_BODY_RECON_FAIL");
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
                strMailSubject = CBUSCore1.Translate("MESSAGE_MAIL_SUBJECT_RECON_FAIL");
                strMailBody = CBUSCore1.Translate("MESSAGE_MAIL_BODY_RECON_FAIL");
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
            CBUSCore1 = null;
            CBUSReconcile1 = null;
            ListCENTReconcile1 = null;
            ConnectionFactory1 = null;
            Connection1 = null;
        }
    }
}
