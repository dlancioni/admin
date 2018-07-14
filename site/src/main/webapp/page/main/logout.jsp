<%
    /*
     * General Declaration
     */  


    try
    {
        /*
         * Redirect to login page
         */                        
        session.invalidate();
    }
    catch (Exception Exception1)
    {
        out.print(Exception1.getMessage());
    }
    finally
    {
        /*
         * Kill current session
         */          
        session = null;
    }        
    
%>

<html>
<body>    
<form name="form1">    
    
    <script>

        document.form1.method = "Post";
        document.form1.action = "/" + location.pathname.split('/')[1] + "/index.jsp";
        document.form1.submit();
    </script>
    
</form>    
</body>    
</html>
