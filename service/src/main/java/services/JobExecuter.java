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
import java.util.List;
import project.Global;

/**
 *
 * @author David
 */
public class JobExecuter extends Job implements Runnable
{
    @Override
    public void run()
    {
        /*
         * General Declaration
         */
        int intIdQueue = 0;
        int intIdService = 0;
              
        Global Global1 = new Global();
        CENTData CENTFilter1 = null;
        CBUSCore CBUSCore1 = null;        
        Connection Connection1 = null;
        ConnectionFactory ConnectionFactory1 = new ConnectionFactory();        
        
        List<CENTData> ListCENTData1 = null;        
        List<CENTData> ListCENTCatalogQueue1 = null;
        List<CENTData> ListCENTCompany1 = null;        
        
        try
        {
            /*
             * Read the scheduled jobs
             */
            while (true)
            {                
                /*
                 * Start the connection
                 */            
                Connection1 = ConnectionFactory1.GetConnection();
                
                /*
                 * Get the list of companies to be processed
                 */
                ListCENTCompany1 = new CBUSCore(Connection1, this.GetJobSession(new CENTData())).GetListOfCompany();                

                /*
                 * Execute pending requests for each company
                 */                
                for (CENTData CENTCompany1 : ListCENTCompany1)
                {                
                    /*
                     * Update the session with new company
                     */
                    CENTSession1 = this.GetJobSession(CENTCompany1);
                    
                    /*
                     * Create the objects
                     */
                    CBUSCore1 = new CBUSCore(Connection1, CENTSession1);

                    /*
                     * Read the catalogs
                     */            
                    ListCENTCatalogQueue1 = CBUSCore1.GetCatalog(TRN_QUEUE);

                    /*
                     * Get the list of pending schedules for today
                     */
                    CENTFilter1 = new CENTData();
                    CENTFilter1.SetInt(this.GetFieldObject("id_status", ListCENTCatalogQueue1), Global1.QUEUE_STATUS_PENDING);
                    CBUSCore1.SetIdTransaction(Global1.TRN_QUEUE);
                    CBUSCore1.SetIdView(Global1.DO_NOT_USE_VIEW);
                    ListCENTData1 = CBUSCore1.GetList(CENTFilter1);

                    for (CENTData CENTData1 : ListCENTData1)
                    {
                        /*
                         * Get related id's to execute the process
                         */
                        intIdQueue = CENTData1.GetInt(this.GetFieldObject("id", ListCENTCatalogQueue1));
                        intIdService = CENTData1.GetInt(this.GetFieldObject("id_service", ListCENTCatalogQueue1));

                        /*
                         * Execute according to the service
                         */                    
                        switch (CENTData1.GetInt(this.GetFieldObject("id_type", ListCENTCatalogQueue1)))
                        {
                            case SCHEDULE_STEP_IMPORT_LAYOUT:

                                Thread ThreadJobEtl1 = new Thread(new JobEtl(CENTCompany1, intIdQueue, intIdService));
                                ThreadJobEtl1.start();

                                break;

                            case SCHEDULE_STEP_RECONCILE:

                                Thread ThreadJobRecon1 = new Thread(new JobRecon(CENTCompany1, intIdQueue, intIdService));
                                ThreadJobRecon1.start();

                                break;

                            case SCHEDULE_STEP_SCHEDULE:

                                Thread ThreadJobPack3 = new Thread(new JobPack(CENTCompany1, intIdQueue, intIdService));
                                ThreadJobPack3.start();

                                break;
                        }                    
                    }    
                }
                
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
                
                /*
                 * Heartbeat
                 */
                Thread.sleep(5000);
            }
                
            
        }
        catch (CENTException CENTException1)
        {           
            System.out.println(CENTException1.GetCode() + " - " + CENTException1.GetMessage());
        }        
        catch (Exception Exception1)
        {
            System.out.println(Exception1.getMessage());            
        }
        finally
        {            
            Connection1 = null;
            Global1 = null;        
            CENTFilter1 = null;
            CBUSCore1 = null;        
            ListCENTData1 = null;        
            ListCENTCatalogQueue1 = null;
            ConnectionFactory1 = null;
        }
    }    
    
}
