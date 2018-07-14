/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connection;

import entity.CENTException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author David
 */
public class ConnectionFactory extends project.Global
{   
    
    public Connection GetConnection() throws CENTException, Exception
    {
        /*
         * General Declaration
         */
        String strServer = "";        
        
        try
        {            
            /*
             * Figure out and return a new connection
             */
            strServer = this.GetPropertieValue("CONNECTION_DB_SERVER");
            
            /*
             * Get connection according to the properties file
             */
            switch (strServer)
            {                                        
                case SERVER_ORACLE:
                    Connection1 = this.GetOracleConnection();
                    break;

                case SERVER_SQL_SERVER:
                    Connection1 = this.GetMSSQLConnection();
                    break;
                    
                case SERVER_POSTGRE:
                    Connection1 = this.GetPostGreConnection();
                    break;
            }
            
            /*
             * Return it
             */
            return Connection1;
            
            
        }
        catch (SQLException SQLException1)
        {
            throw new CENTException("EXCEPTION_MSSQL_DATABASE_CONNECTION", SQLException1.getMessage());
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
            strServer = null;
        }
    }
       
    public String GetCurrentSGDB() throws Exception
    {
        String strDB = "";
        
        try
        {
            strDB = this.GetPropertieValue("CONNECTION_DB_SERVER");
        }               
        catch (Exception Exception1) 
        {
            throw Exception1;
        }
        
        return strDB;    
    }    
                
    private Connection GetOracleConnection() throws Exception
    {
        /*
         * General Declaration
         */
        String strUrl = "";
        String strServerName = "";
        String strPort = "";
        String strUsername = "";
        String strPassword = "";
        String strDatabase = "";

        try
        {
            /*
             * Connection information
             */
            strServerName = this.GetPropertieValue("CONNECTION_ORACLE_SERVER");
            strPort = this.GetPropertieValue("CONNECTION_ORACLE_PORT");
            strUsername = this.GetPropertieValue("CONNECTION_ORACLE_USERNAME");
            strPassword = this.GetPropertieValue("CONNECTION_ORACLE_PASSWORD");
            strDatabase = this.GetPropertieValue("CONNECTION_ORACLE_DATABASE");
            
            /*
             * Prepare the connection string
             */
            //strUrl = "jdbc:oracle:thin:@" + strServerName + ":" + strPort + ":xe";            
            strUrl = "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS_LIST=(ADDRESS=(PROTOCOL=TCP)(HOST=" + strServerName + ")(PORT=" + strPort + ")))(CONNECT_DATA=(SERVICE_NAME=" + strDatabase + ")))";

            /*
             * Connect to Oracle
             */            
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection1 = DriverManager.getConnection(strUrl, strUsername, strPassword);            

            /*
             * Return it
             */            
            return Connection1;
            
        } 
        catch (SQLException SQLException1)
        {
            throw new CENTException("EXCEPTION_ORACLE_DATABASE_CONNECTION", SQLException1.getMessage());
        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }
        finally
        {
            strUrl = null;
            strServerName = null;
            strPort = null;
            strUsername = null;
            strPassword = null;
            strDatabase = null;
        }
    }    

    private Connection GetMSSQLConnection() throws Exception
    {
        /*
         * General Declaration
         */
        String strUrl = "";
        String strServerName = "";
        String strPort = "";
        String strUsername = "";
        String strPassword = "";
        String strDatabase = "";

        try
        {
            /*
             * Connection information
             */
            strServerName = this.GetPropertieValue("CONNECTION_MSSQL_SERVER");
            strPort = this.GetPropertieValue("CONNECTION_MSSQL_PORT");
            strUsername = this.GetPropertieValue("CONNECTION_MSSQL_USERNAME");
            strPassword = this.GetPropertieValue("CONNECTION_MSSQL_PASSWORD");
            strDatabase = this.GetPropertieValue("CONNECTION_MSSQL_DATABASE");
            
            /*
             * Prepare the connection string
             */
	    strUrl = "jdbc:sqlserver://" + strServerName + ":" + strPort + ";" + "databaseName=" + strDatabase;

            /*
             * Connect to Oracle
             */            
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection1 = DriverManager.getConnection(strUrl, strUsername, strPassword);
            
            /*
             * Must read commited information only
             */            
            Connection1.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);            

            /*
             * Return it
             */            
            return Connection1;
            
        } 
        catch (SQLException SQLException1)
        {
            throw new CENTException("EXCEPTION_MSSQL_DATABASE_CONNECTION", SQLException1.getMessage());
        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }
        finally
        {
            strUrl = null;
            strServerName = null;
            strPort = null;
            strUsername = null;
            strPassword = null;
            strDatabase = null;
        }
    }
    
    private Connection GetPostGreConnection() throws Exception
    {
        /*
         * General Declaration
         */
        String strUrl = "";
        String strServerName = "";
        String strPort = "";
        String strUsername = "";
        String strPassword = "";
        String strDatabase = "";

        try
        {
            /*
             * Connection information
             */
            strServerName = this.GetPropertieValue("CONNECTION_POSTGRE_SERVER");
            strPort = this.GetPropertieValue("CONNECTION_POSTGRE_PORT");
            strUsername = this.GetPropertieValue("CONNECTION_POSTGRE_USERNAME");
            strPassword = this.GetPropertieValue("CONNECTION_POSTGRE_PASSWORD");
            strDatabase = this.GetPropertieValue("CONNECTION_POSTGRE_DATABASE");
            
            /*
             * Prepare the connection string
             */
	    strUrl = "jdbc:postgresql://" + strServerName + ":" + strPort + "/" + strDatabase;
            
            /*
             * Connect to Oracle
             */
            Class.forName("org.postgresql.Driver");
            Connection1 = DriverManager.getConnection(strUrl, strUsername, strPassword);

            /*
             * Must read commited information only
             */            
            Connection1.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);

            /*
             * Return it
             */            
            return Connection1;
            
        } 
        catch (SQLException SQLException1)
        {
            throw new CENTException("EXCEPTION_POSTGRE_DATABASE_CONNECTION", SQLException1.getMessage());
        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }
        finally
        {
            strUrl = null;
            strServerName = null;
            strPort = null;
            strUsername = null;
            strPassword = null;
            strDatabase = null;
        }
    }    
    
    private Connection GetSQLiteConnection() throws Exception
    {
        /*
         * General Declaration
         */
        String strUrl = "";
        String strDatabase = "";

        try
        {
            /*
             * Connection information
             */
            strDatabase = this.GetPropertieValue("CONNECTION_SQLITE_DATABASE");
            
            /*
             * Prepare the connection string
             */
	    strUrl = "jdbc:sqlite:" + strDatabase;
            
            /*
             * Connect to Oracle
             */
            Class.forName("org.sqlite.JDBC");
            Connection1 = DriverManager.getConnection(strUrl);

            /*
             * Must read commited information only
             */            
            //Connection1.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

            /*
             * Return it
             */            
            return Connection1;
            
        } 
        catch (SQLException SQLException1)
        {
            throw new CENTException("EXCEPTION_SQLITE_DATABASE_CONNECTION", SQLException1.getMessage());
        }
        catch (Exception Exception1)
        {
            throw Exception1;
        }
        finally
        {
            strUrl = null;
            strDatabase = null;
        }
    }        
}
