
<%@page import="java.sql.Connection"%>
<%@page import="project.*"%>
<%@page import="entity.*"%>
<%@page import="connection.*"%>
<%@page import="business.*"%>
<%@page import="java.util.*"%>


<%         
    String strOutput = "";
    String strSetup = "";   
    CENTData CENTFilter1 = null;    
    Global Global1 = new Global();       
    CENTData CENTSession1 = null;
    CBUSCore CBUSCore1 = null;
    Connection Connection1 = null;
    ConnectionFactory ConnectionFactory1 = null;
    List<CENTData> ListCENTCompany1 = null;
        
    try
    {
        /*
         * Create the objects
         */
        CENTSession1 = new CENTData();
        CBUSCore1 = new CBUSCore(Connection1, CENTSession1);
        
        /*
         * Get runnings parameters
         */        
        if (request.getParameter("setup") != null)
        {
            strSetup = request.getParameter("setup").toString();
        }
        
        /*
         * Keep information related to culture
         */
        session.setAttribute("language_code", CBUSCore1.GetPropertieValue("COMPANY_LANGUAGE"));
        session.setAttribute("country_code", CBUSCore1.GetPropertieValue("COMPANY_COUNTRY"));

        CENTSession1.SetText(Global1.SESSION_MASK_DATE, CBUSCore1.GetPropertieValue("COMPANY_MASK"));        
        CENTSession1.SetText(Global1.SESSION_COUNTRY, CBUSCore1.GetPropertieValue("COMPANY_COUNTRY"));
        CENTSession1.SetText(Global1.SESSION_LANGUAGE, CBUSCore1.GetPropertieValue("COMPANY_LANGUAGE"));
        CENTSession1.SetDate(Global1.SESSION_DATE, new Date());
        CENTSession1.SetInt(Global1.SESSION_COMPANY, 1);
        CENTSession1.SetInt(Global1.SESSION_PROFILE, Integer.valueOf(CBUSCore1.GetPropertieValue("COMPANY_PROFILE")));
        CENTSession1.SetInt(Global1.SESSION_USER, Integer.valueOf(CBUSCore1.GetPropertieValue("COMPANY_USER")));
        CENTSession1.SetInt(Global1.SESSION_AREA, Global1.AREA_IT);
        
        /*
         * Initialize the session (mandatory)
         */
        session.setAttribute("Login", CENTSession1);
        session.setAttribute("Filter", "");
        
        /*
         * Create the system catalog - close attetion here
         */                
        if (strSetup.equals("1"))
        {        
            /*
             * Kill current connection
             */
            Connection1 = (Connection) session.getAttribute("Connection");
            if (Connection1 != null)
            {
                if (!Connection1.isClosed())
                {
                    Connection1.close();
                    session.setAttribute("Connection", null);
                }
            }        

            /*
             * Create a connection
             */  
            ConnectionFactory1 = new ConnectionFactory();
            Connection1 = ConnectionFactory1.GetConnection();
                        
            /*
             * Setup the POCs
             */            
            strOutput = "Connection success to " + CBUSCore1.GetPropertieValue("CONNECTION_DB_SERVER");
        }        
        
        /*
         * Create the system catalog - close attetion here
         */                
        if (strSetup.equals("2"))
        {        
            /*
             * Kill current connection
             */
            Connection1 = (Connection) session.getAttribute("Connection");
            if (Connection1 != null)
            {
                if (!Connection1.isClosed())
                {
                    Connection1.close();
                    session.setAttribute("Connection", null);
                }
            }        

            /*
             * Create a connection
             */  
            ConnectionFactory1 = new ConnectionFactory();
            Connection1 = ConnectionFactory1.GetConnection();            
            CBUSCore1 = new CBUSCore(Connection1, CENTSession1);
                    
            /*
             * Create system instance
             */
            CBUSCore1.ImplementSystemInstance();    
                        
            /*
             * Setup the POCs
             */            
            strOutput = "Tables created";
        }
        
        /*
         * Create the system catalog - close attetion here
         */                
        if (strSetup.equals("3"))
        {
            /*
             * Kill current connection
             */
            Connection1 = (Connection) session.getAttribute("Connection");
            if (Connection1 != null)
            {
                if (!Connection1.isClosed())
                {
                    Connection1.close();
                    session.setAttribute("Connection", null);
                }
            }        

            /*
             * Create a connection
             */  
            ConnectionFactory1 = new ConnectionFactory();
            Connection1 = ConnectionFactory1.GetConnection();            
            CBUSCore1 = new CBUSCore(Connection1, CENTSession1);
            
            /*
             * Create demo company number 1
             */
            ListCENTCompany1 = new ArrayList<CENTData>();
            
            CENTFilter1 = new CENTData();
            CENTFilter1.SetText(1, "COMPANY_1");
            CENTFilter1.SetText(2, "01.001.001/0001-01");
            CENTFilter1.SetText(3, "bank1@bank1.com");
            CENTFilter1.SetInt(1, 1);                                            // Country
            CENTFilter1.SetInt(2, 1);                                            // Language
            CENTFilter1.SetDate(1, new Date());                                  // Expire date
            CENTFilter1.SetInt(3, 3);                                            // Recon limit
            CENTFilter1.SetInt(4, 30);                                           // History   
            CENTFilter1.SetInt(5, 0);                                            // Line limit
            CENTFilter1.SetInt(6, 1);                                            // Solution Type - Entry manager
            CENTFilter1.SetText(4, "");
            ListCENTCompany1.add(CENTFilter1);

            CBUSCore1.SetIdView(Global1.DO_NOT_USE_VIEW);
            CBUSCore1.SetIdTransaction(Global1.TRN_COMPANY);
            CBUSCore1.ExecuteEvent(Global1.EVENT_INSERT, ListCENTCompany1); 

            /*
             * Create demo company number 2
             */
            ListCENTCompany1 = new ArrayList<CENTData>();
            
            CENTFilter1 = new CENTData();
            CENTFilter1.SetText(1, "COMPANY_2");
            CENTFilter1.SetText(2, "01.001.001/0001-02");
            CENTFilter1.SetText(3, "bank2@bank2.com");
            CENTFilter1.SetInt(1, 1);                                            // Country
            CENTFilter1.SetInt(2, 1);                                            // Language
            CENTFilter1.SetDate(1, new Date());                                  // Expire date
            CENTFilter1.SetInt(3, 3);                                            // Recon limit
            CENTFilter1.SetInt(4, 30);                                           // History   
            CENTFilter1.SetInt(5, 0);                                            // Line limit
            CENTFilter1.SetInt(6, 2);                                            // Solution Type - Reconciliation            
            CENTFilter1.SetText(4, "");
            ListCENTCompany1.add(CENTFilter1);

            CBUSCore1.SetIdView(Global1.DO_NOT_USE_VIEW);
            CBUSCore1.SetIdTransaction(Global1.TRN_COMPANY);
            CBUSCore1.ExecuteEvent(Global1.EVENT_INSERT, ListCENTCompany1);             
            
            /*
             * Setup the POCs
             */            
            strOutput = "Company created";
            
        }                
        
        /*
         * Close current connection
         */
        if (Connection1 != null)
        {
            if (!Connection1.isClosed())
            {
                Connection1.close();
            }    
        }

    }
    catch (CENTException CENTException1)
    {
        //strOutput = CBUSCore1.Translate(CENTException1.GetMessage()) + " - " + CBUSCore1.Translate(CENTException1.GetCode());        
        strOutput = CENTException1.GetMessage() + " - " + CENTException1.GetCode();
    }    
    catch (Exception Exception1)
    {
        strOutput = Exception1.getMessage();
    }
    finally
    {
        CENTFilter1 = null;
        Global1 = null;       
        CENTSession1 = null;
        CBUSCore1 = null;
        
        Connection1 = null;
        ConnectionFactory1 = null;
    }

%>


<!DOCTYPE html>

<html>
<%@include file="../common/head.jsp"%>    
<body>
<form name="frmDefault">     
    
<br>
<br>
<br>
<br>
   
<div class="site-margin">
    

    <table align="center">
        <tr>
            <td>
                <h1><b>Setup do sistema</b></h1>
            </td>    
        </tr>                           
    </table>    

    <table align="center" cellspacing="10" cellpadding="10">    

        <tr>
            <td></td>
            <td align="right">
                <button onClick="JavaScript:Setup(1)" type="Button" class="btn btn-default">Test Connection</button>
                <button onClick="JavaScript:Setup(2)" type="Button" class="btn btn-default">Create Tables</button>
                <button onClick="JavaScript:Setup(3)" type="Button" class="btn btn-default">Create Company</button>                
            </td>    
        </tr>            

    </table>    

    <br>
    <br>
    <br>
    
    <table align="center">
        <tr>
            <td>
                <h1><%out.print(strOutput);%></h1>                
            </td>    
        </tr>                           
    </table>        
    
<script language='JavaScript'>
    
    function Setup(param)
    {
        var msg = 0;
        
        if (param == 1)
        {
            msg = confirm("Are you sure to test connection?");

            if (msg == true) 
            {
                 document.frmDefault.method = "post";
                 document.frmDefault.action = "setup.jsp?setup=1";
                 document.frmDefault.submit();  
            }
        }        
                
        if (param == 2)
        {
            msg = confirm("Are you sure to recreate all tables?");

            if (msg == true) 
            {
                 document.frmDefault.method = "post";
                 document.frmDefault.action = "setup.jsp?setup=2";
                 document.frmDefault.submit();  
            }
        }
        
        if (param == 3)
        {
            msg = confirm("Are you sure to create company?");

            if (msg == true) 
            {
                 document.frmDefault.method = "post";
                 document.frmDefault.action = "setup.jsp?setup=3";
                 document.frmDefault.submit();  
            }            
        }
        

    }
   
</script>

</body>
</html>