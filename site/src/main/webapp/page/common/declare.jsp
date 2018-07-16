
<%
   response.setHeader( "Pragma", "no-cache" );
   response.setHeader( "Cache-Control", "no-cache" );
   response.setDateHeader( "Expires", -1 );
   response.setCharacterEncoding("UTF-8");
%>
<%@page import="java.sql.Connection"%>

<%@page import="connection.*"%>
<%@page import="project.*"%>
<%@page import="entity.*"%>
<%@page import="business.*"%>

<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="javax.xml.*"%>

<%@include file="global.jsp"%> 

<%
   
    int intIdTransaction = 0;
    int intIdTypeTransaction = 0;
    String strNameTransaction = "";
    String strFieldNameNav = "";
    String strFieldValueNav = "";
    
    Global Global1 = null;
    CENTData CENTSession1 = null;
    List<CENTData> ListCENTDictionary1 = null;
    
    Connection Connection1 = null;
    ConnectionFactory ConnectionFactory1 = null;    
        
    try
    {  
        /*
         * Create a connection
         */  
        Connection1 = (Connection) session.getAttribute("Connection");
        
        if (Connection1 == null)
        {
            ConnectionFactory1 = new ConnectionFactory();
            Connection1 = ConnectionFactory1.GetConnection();        
            session.setAttribute("Connection", Connection1);                
        }
        else
        {
            if (Connection1.isClosed())
            {
                ConnectionFactory1 = new ConnectionFactory();
                Connection1 = ConnectionFactory1.GetConnection();        
                session.setAttribute("Connection", Connection1);                
            }
        }

        /*
         * Get the object with login information from the session
         */
        Global1 = new Global();                   
        CENTSession1 = (CENTData) session.getAttribute("Login");

        /*
         * Check for expired session
         */
        if ((request.getSession() == null) || (CENTSession1 == null)) 
        {
            request.getRequestDispatcher("../common/exception.jsp?code=expire").forward(request, response);
        }
        
        /*
         * Handle the transactions
         */        
        if (request.getParameter("id_transaction") != null)
        {
            intIdTransaction = Integer.parseInt(request.getParameter("id_transaction"));
            session.setAttribute("id_transaction", intIdTransaction);
        }

        if (request.getParameter("type_transaction") != null)
        {
            intIdTypeTransaction = Integer.parseInt(request.getParameter("type_transaction"));
            session.setAttribute("type_transaction", intIdTypeTransaction);
        }        
        
        
        if (request.getParameter("name_transaction") != null)
        {
            strNameTransaction = request.getParameter("name_transaction");
            session.setAttribute("name_transaction", strNameTransaction);
        }        
        else
        {
            if (request.getSession().getAttribute("name_transaction") != null)
            {
                strNameTransaction = request.getSession().getAttribute("name_transaction").toString();
            }    
        }
        
        if (request.getParameter("field_name") != null)
            strFieldNameNav = request.getParameter("field_name");        
        
        if (request.getParameter("field_value") != null)
            strFieldValueNav = request.getParameter("field_value");        

        if (request.getSession().getAttribute("id_transaction") != null)
            intIdTransaction = (Integer) request.getSession().getAttribute("id_transaction");        
        
        if (request.getSession().getAttribute("type_transaction") != null)
            intIdTypeTransaction = (Integer) request.getSession().getAttribute("type_transaction");
        
        /*
         * Get the dictionary from the memory to use in the current execution
         */        
        ListCENTDictionary1 = (ArrayList) request.getSession().getAttribute("Dictionary");

    }
    catch (Exception Exception1)
    {
        throw Exception1;
    }
    finally
    {

    }

%>