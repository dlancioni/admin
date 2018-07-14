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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import project.Global;
    
/*
 * Class to handle schedule job as a new thread
 */
public class JobScheduler extends Job implements Runnable
{        
    @Override
    public void run()
    {
        /*
         * General Declaration
         */       
        int intIdArea = 0;
        int intDayOfWeek = 0;

        boolean boolExecute = false;        

        Date datDate = null;
        String strNow = "";
        String strBefore = "";
        String strScheduleName = "";

        Global Global1 = null;                
        CENTData CENTFilter1 = null;
        CENTData CENTData1 = null;
        CENTData CENTSession1 = null;
        CBUSCore CBUSCore1 = null;                

        SimpleDateFormat SimpleDateFormat1 = null;        
        ConnectionFactory ConnectionFactory1 = new ConnectionFactory();

        List<CENTData> ListCENTData1 = null;        
        List<CENTData> ListCENTSchedule1 = null;        
        List<CENTData> ListCENTCatalogQueue1 = null;
        List<CENTData> ListCENTCatalogSchedule1 = null;
        List<CENTData> ListCENTCompany1 = null;
        Connection Connection1 = null;

        try
        {
            /*
             * Create the objects
             */
            Global1 = new Global();        
            Calendar Calendar1 = Calendar.getInstance();
            SimpleDateFormat1 = new SimpleDateFormat("HH:mm");           

            while (true)
            {
            
                /*
                 * Get the connection
                 */            
                Connection1 = ConnectionFactory1.GetConnection();                            
                
                /*
                 * Get current date and time
                 */
                datDate = new Date();
                Calendar1.setTime(datDate);                
                strNow = SimpleDateFormat1.format(datDate.getTime());
                                
                /*
                 * Query scheduled tasks for this minute
                 */
                if (!strNow.equals(strBefore) && !strBefore.equals("") )
                {
                    /*
                     * Get the list of companies to be processed
                     */                    
                    ListCENTCompany1 = new CBUSCore(Connection1, this.GetJobSession(new CENTData())).GetListOfCompany();

                    /*
                     * Schedule the jobs for each companie
                     */                    
                    for (CENTData CENTCompany1 : ListCENTCompany1)
                    {                    
                        /*
                         * Get catalogs
                         */                    
                        CBUSCore1 = new CBUSCore(Connection1, this.GetJobSession(CENTCompany1));

                        /*
                         * Get catalogs
                         */
                        ListCENTData1 = new ArrayList<CENTData>();                    
                        ListCENTCatalogQueue1 = CBUSCore1.GetCatalog(TRN_QUEUE);
                        ListCENTCatalogSchedule1 = CBUSCore1.GetCatalog(TRN_SCHEDULE);                    

                        /*
                         * Query scheduled tasks for this minute
                         */
                        CENTFilter1 = new CENTData();
                        CENTFilter1.SetText(this.GetFieldObject("time", ListCENTCatalogSchedule1), strNow);

                        CBUSCore1.SetIdTransaction(Global1.TRN_SCHEDULE);
                        CBUSCore1.SetIdView(Global1.DO_NOT_USE_VIEW);
                        ListCENTSchedule1 = CBUSCore1.GetList(CENTFilter1);

                        for (CENTData CENTSchedule1 : ListCENTSchedule1)
                        {
                            intIdArea = CENTSchedule1.GetInt(this.GetFieldObject("id_area", ListCENTCatalogSchedule1));
                            strScheduleName = CENTSchedule1.GetText(this.GetFieldObject("name", ListCENTCatalogSchedule1));
                            intDayOfWeek = Calendar1.get(Calendar.DAY_OF_WEEK);                        

                            switch (CENTSchedule1.GetInt(this.GetFieldObject("id_recurrence", ListCENTCatalogSchedule1)))
                            {
                                case MONDAY:

                                    if (intDayOfWeek == MONDAY)
                                    {
                                        boolExecute = true;
                                    }
                                    break;

                                case TUESDAY:

                                    if (intDayOfWeek == TUESDAY)
                                    {
                                        boolExecute = true;
                                    }
                                    break;

                                case WEDNESDAY:

                                    if (intDayOfWeek == WEDNESDAY)
                                    {
                                        boolExecute = true;
                                    }
                                    break;

                                case THURSDAY:

                                    if (intDayOfWeek == THURSDAY)
                                    {
                                        boolExecute = true;
                                    }
                                    break;

                                case FRIDAY:

                                    if (intDayOfWeek == FRIDAY)
                                    {
                                        boolExecute = true;
                                    }
                                    break;

                                case SATURDAY:

                                    if (intDayOfWeek == SATURDAY)
                                    {
                                        boolExecute = true;
                                    }
                                    break;                   

                                case DAILY:    
                                        boolExecute = true;                                                    
                            }

                            if (boolExecute)
                            {
                                /*
                                 * Create new pack
                                 */
                                CENTData1 = new CENTData();
                                CENTData1.SetInt(this.GetFieldObject("id", ListCENTCatalogQueue1), 0);
                                CENTData1.SetDate(this.GetFieldObject("date", ListCENTCatalogQueue1), datDate);
                                CENTData1.SetInt(this.GetFieldObject("id_type", ListCENTCatalogQueue1), SCHEDULE_STEP_SCHEDULE);
                                CENTData1.SetInt(this.GetFieldObject("id_service", ListCENTCatalogQueue1), CENTSchedule1.GetInt(this.GetFieldObject("id", CBUSCore1.GetCatalog(TRN_SCHEDULE_JOB))));
                                CENTData1.SetText(this.GetFieldObject("name_service", ListCENTCatalogQueue1), strScheduleName);
                                CENTData1.SetInt(this.GetFieldObject("id_status", ListCENTCatalogQueue1), Global1.QUEUE_STATUS_PENDING);
                                CENTData1.SetText(this.GetFieldObject("note", ListCENTCatalogQueue1), "");

                                ListCENTData1.add(CENTData1);
                            }
                        }

                        /*
                         * Persist it
                         */                            
                        if (!ListCENTData1.isEmpty())
                        {
                            /*
                             * Set the new session information (area), very importart
                             */                                               
                            CENTSession1 = this.GetJobSession(CENTCompany1);
                            CENTSession1.SetInt(SESSION_AREA, intIdArea);                                                
                            CBUSCore1 = new CBUSCore(Connection1, CENTSession1);

                            /*
                             * Persist it
                             */
                            CBUSCore1.SetIdTransaction(Global1.TRN_QUEUE);
                            CBUSCore1.SetIdView(Global1.DO_NOT_USE_VIEW);                            
                            CBUSCore1.ExecuteEvent(Global1.EVENT_INSERT, ListCENTData1);
                        }

                        /*
                         * Control the break in minutes 
                         */
                        strBefore = strNow;                    
                    }
                }    

                /*
                 * Control the break in minutes
                 */
                strBefore = strNow;
                
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
                Thread.sleep(10000);

            } // end while

        }
        catch (CENTException CENTException1)
        {   
            System.out.println("EXCEPTION: Schedule()");
            System.out.println(CENTException1.GetMessage());
            System.exit(0);
        }        
        catch (Exception Exception1)
        {           
            System.out.println("EXCEPTION: Schedule()");            
            System.out.println(Exception1.getMessage());
            System.exit(0);
        }
        finally
        {
            datDate = null;

            strNow = null;
            strBefore = null;

            Global1 = null;                
            CENTFilter1 = null;
            CENTData1 = null;
            CENTSession1 = null;
            CBUSCore1 = null;                

            SimpleDateFormat1 = null;        
            ConnectionFactory1 = null;

            ListCENTData1 = null;        
            ListCENTSchedule1 = null;        
            ListCENTCatalogQueue1 = null;
            ListCENTCatalogSchedule1 = null;
            ListCENTCompany1 = null;

            Connection1 = null;
        }        
    }
}