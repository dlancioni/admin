<!DOCTYPE html>

<html>
<%@include file="../common/head.jsp"%>    
<body onunload="">      
     
<%@include file="../common/menu_login.jsp"%>

<%@include file="../common/bar.jsp"%>

        
    <%
    int intIdControl = 1;
    String strMessage = "";
    String strErrorCode = "";
    String strErrorMessage = "";
    String strFolder = "";
    String strAction = "";
    String strFile = "";
    
    String strLabel1 = "";
    String strLabel2 = "";
    String strLabel3 = "";

    String strLink = "";
    
    String strMessageDelete = "";
    
    List<CENTData> ListCENTData1 = null;
    
    try
    {    
        /*
         * Receive the parameters
         */   
        if (request.getParameter("message") != null)
        {
            strMessage = request.getParameter("message");
        }

        if (request.getParameter("error_code") != null)
        {
            strErrorCode = request.getParameter("error_code");
        }

        if (request.getParameter("error_message") != null)
        {
            strErrorMessage = request.getParameter("error_message");
        }    

        if (request.getParameter("action") != null)
        {
            strAction = request.getParameter("action");
        }    

        if (request.getParameter("file") != null)
        {
            strFile = request.getParameter("file");
        }    
        
        /*
         * Create the objects
         */
        CBUSCore CBUSCore1 = new CBUSCore(Connection1, CENTSession1);    
        CBUSEtl CBUSEtl1 = new CBUSEtl(Connection1, CENTSession1);    

        
        /*
         * Translate the messages
         */       
        strMessage = CBUSCore1.Translate(strMessage);
        strErrorCode = CBUSCore1.Translate(strErrorCode);
        strErrorMessage = CBUSCore1.Translate(strErrorMessage);
        strMessageDelete = CBUSCore1.Translate(ListCENTDictionary1, "MESSAGE_CONFIRM_DELETE_FILE");
        
        strLabel1 = CBUSCore1.Translate(ListCENTDictionary1, "FileManager.File");
        strLabel2 = CBUSCore1.Translate(ListCENTDictionary1, "FileManager.Action");
        strLabel3 = CBUSCore1.Translate(ListCENTDictionary1, "Button.Delete");

        /*
         * Set the rror message to print
         */

        if (!strErrorCode.trim().equals(""))
        {
            strMessage += ": ";
            strMessage += strErrorCode;
        }

        if (!strErrorMessage.trim().equals(""))
        {
            strMessage += ": " + strErrorMessage;
        }        

        /*
         * Get working folder
         */
        strFolder = CBUSCore1.GetPathFileToImport("");
        
        /*
         * Delete the file    
         */
        if (strAction.equals("delete"))
        {
            CBUSEtl1.DeleteFile(CBUSCore1.GetPathFileToImport(strFile));
        }        
        
        /*
         * Load the files
         */
        ListCENTData1 = CBUSCore1.GetListOfFilesInFolder(strFolder);
    
    }
    catch (Exception Exception1)
    {
        
    }
    finally
    {
        
    }

    
    %>

    <div class="container">
        
        <%=SetTitle(1, strNameTransaction, "", "")%>
        
        <br>

        
        <%
            if (!strErrorMessage.trim().equals(""))
            {
                out.println("<h4>" + strMessage + "</h4>");  
            }
            else
            {
                out.println("<h4>" + strMessage + "</h4>");  
            }
        %>        
        

        <form role="form" method="post" action="../../Upload" enctype="multipart/form-data">

            <br>
            <input class="" type="file" id="file1" name="file1" value="abcde">
            </br>
            <input type="submit" value="Upload" name="upload" id="upload" />
            
        </form>    
        
        <br>
        <br>
        
        <form name="frmDefault">
        
            <input type="hidden" id="txtMessageDelete" value="<%out.print(strMessageDelete);%>">            
            
            <div class="table-responsive">
                <table class="table table-bordered" id="table1">
                    <tbody>
                        <tr>
                            <td><b><%out.print(strLabel1);%></b></td>
                            <td><b><%out.print(strLabel2);%></b></td>                        
                        </tr>       

                        <%
                        intIdControl++;

                        for (CENTData CENTData1 : ListCENTData1)
                        {
                            strLink = "http://www.reconapp.com.br/files/" + CENTSession1.GetInt(Global1.SESSION_COMPANY) + "/" + CENTSession1.GetInt(Global1.SESSION_AREA) + "/" + CENTData1.GetText(1);
                            
                            
                            %>    
                            <tr class='<%out.println(TRColor(intIdControl));%>'>                                                        
                                <td><a href="<%out.print(strLink);%>" download target="_blank"><%out.println(CENTData1.GetText(1));%></a></td>                                                        
                                <td><input type="button" value="<%out.print(strLabel3);%>" onClick="JavaScript:Delete('<%out.print(CENTData1.GetText(1));%>')"></td>
                            </tr>       
                            <%
                        }
                        %>
                    </tbody>
                </table> 
            </div>       

        </form>        
    </div>            
        
                
    <script language="JavaScript">                

        function Delete(FileName)
        {
            var Message = document.frmDefault.txtMessageDelete.value;
            
            if (confirm(Message))
            {
                document.frmDefault.method = "post";
                document.frmDefault.action = "filemanager.jsp?action=delete&file=" + FileName;
                document.frmDefault.submit();
            }
        }

    </script>    
                
</body>
</html>