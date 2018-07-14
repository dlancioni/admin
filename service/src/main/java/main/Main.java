package main;

import services.JobExecuter;
import services.JobScheduler;

        
/**
 *
 * @author David Lancioni
 */
public class Main extends project.Global
{     
    public static void main(String[] args) 
    {
        /*
         * General Declaration
         */
        try
        {
            Thread t1 = new Thread(new JobScheduler());
            t1.setName("Scheduler");
            t1.start();
            System.out.println("Service started: " + t1.getName());
                                    
            Thread t2 = new Thread(new JobExecuter());
            t2.setName("Executer");            
            t2.start();            
            System.out.println("Service started: " + t2.getName()); 

        }
        catch (Exception Exception1)
        {
            System.out.println(Exception1.getMessage());
        }
        finally
        {

        }
    }        
    



    

    
}
