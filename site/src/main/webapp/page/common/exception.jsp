<%@page import="java.sql.Connection"%>
<%@page import="project.*"%>
<%@page import="entity.*"%>
<%@page import="connection.*"%>
<%@page import="business.*"%>
<%@page import="java.util.*"%>

<!DOCTYPE html>

<html>
<%@include file="../common/head.jsp"%>    
<body onunload="">      
     
<%@include file="../common/menu_logoff.jsp"%>    

        
    <%
    /*
     * General Declaration
     */
    String strSource = "";    
    String strCode = "";
    String strMessage = "";
    String strException = "";

    CENTData CENTSession1 = null;
    Connection Connection1 = null;    
    CBUSCore CBUSCore1 = null;
        
    /*
     * Receive the parameters
     */   
    if (request.getParameter("source") != null)
    {
        strSource = request.getParameter("source");
    }
    
    if (request.getParameter("code") != null)
    {
        strCode = request.getParameter("code");
    }
    
    if (request.getParameter("message") != null)
    {
        strMessage = request.getParameter("message");
    }    


    if (!strCode.equals("expire"))
    {
        CENTSession1 = (CENTData) session.getAttribute("Login");
        Connection1 = (Connection) session.getAttribute("Connection");    
        CBUSCore1 = new CBUSCore(Connection1, CENTSession1);    

        /*
         * Translate the messages
         */    
        strException = CBUSCore1.Translate("EXCEPTION_SITE");
        strCode = CBUSCore1.Translate(strCode);
        strMessage = CBUSCore1.Translate(strMessage);

        /*
         * Destroy current session
         */
        if (Connection1 != null)
        {
            if (!Connection1.isClosed())
            {
                Connection1.close();
            }        
        }

        session.setAttribute("login", null);
    }
    else
    {
        strException = "SESSION EXPIRED";
    }
    
    %>

    <div class="container">

        <form role="form" method="post" action="<%=request.getContextPath()%> " enctype="multipart/form-data">

            <h3>
            <%out.println(strException);%>
            </h3>
             
            <br>
            
            <h5>
            <%
                if (!strCode.equals("expire"))
                {
                    out.println(strCode + ": " + strMessage + "(" + strSource + ")");
                }
            %>
            </h5>
        </form>
        
    </div>    

    <%
    /*
     * General Declaration
     */
    strSource = null;    
    strCode = null;
    strMessage = null;
    strException = null;

    CENTSession1 = null;
    Connection1 = null;    
    CBUSCore1 = null;
    %>            
            
            
</body>
</html>