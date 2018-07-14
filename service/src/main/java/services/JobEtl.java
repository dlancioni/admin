/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import business.CBUSCore;
import business.CBUSEtl;
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
public class JobEtl extends Job implements Runnable
{
    private int intIdQueue = 0;
    private int intIdService = 0;
    private CENTData CENTCompany1;
    
    public JobEtl(CENTData CENTCompany1, int intQueue, int intService)
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
        CENTData CENTLayout1 = null;
        Global Global1 = new Global();        
        CBUSEtl CBUSIntegration1 = null;
        CBUSCore CBUSCore1 = null;
        List<CENTData> ListCENTLayout1 = new ArrayList<CENTData>();
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
            CBUSIntegration1 = new CBUSEtl(Connection1, this.GetJobSession(CENTCompany1));

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
             * Start the transaction
             */
            Connection1.setAutoCommit(false);

            /*
             * Import data
             */           
            CENTLayout1 = new CENTData();
            CENTLayout1.SetInt(Global1.SYSTEM_FIELD_ID, intIdService);            // Layout id that is being imported  
            CENTLayout1.SetInt(Global1.SYSTEM_FIELD_ID_PROCESS_ETL, intIdQueue);  // Unique id to allow multiple processes  
            ListCENTLayout1.add(CENTLayout1);
            CBUSIntegration1.Import(ListCENTLayout1);

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
            strMailSubject = CBUSCore1.Translate("MESSAGE_MAIL_SUBJECT_IMPORT_LAYOUT_SUCCESS");
            strMailBody = CBUSCore1.Translate("MESSAGE_MAIL_BODY_IMPORT_LAYOUT_SUCCESS");
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
                strMailSubject = CBUSCore1.Translate("MESSAGE_MAIL_SUBJECT_IMPORT_LAYOUT_FAIL");
                strMailBody = CBUSCore1.Translate("MESSAGE_MAIL_BODY_IMPORT_LAYOUT_FAIL");
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
                strMailSubject = CBUSCore1.Translate("MESSAGE_MAIL_SUBJECT_IMPORT_LAYOUT_FAIL");                
                strMailBody = CBUSCore1.Translate("MESSAGE_MAIL_BODY_IMPORT_LAYOUT_FAIL");
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
            CENTLayout1 = null;
            Global1 = null;        
            CBUSIntegration1 = null;
            CBUSCore1 = null;
            ListCENTLayout1 = null;
            ConnectionFactory1 = null;
        }
    }
}
