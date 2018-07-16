
<%@page import="java.sql.Connection"%>
<%@page import="project.*"%>
<%@page import="entity.*"%>
<%@page import="connection.*"%>
<%@page import="business.*"%>
<%@page import="java.util.*"%>

<%         
    int intIdLanguage = 0;
    
    String strLanguage = "";
    String strLanguageCode = "";
    String strCountry = "";
    String strUsername = "";
    String strPassword = "";
    String strCompany = "";
    String strLogin = "";
    String strDate = "";
    String strSystemDate = "";

    String strText1 = "";
    String strText2 = "";    
    String strText3 = "";    
    String strText4 = "";    
    String strText5 = "";    
    String strText6 = "";    
    String strText7 = "";    
    String strText8 = "";    
    
    CENTData CENTFilter1 = null;    
    Global Global1 = new Global();       
    CENTData CENTSession1 = null;
    CBUSCore CBUSCore1 = null;
    Connection Connection1 = null;
    List<CENTData> ListCENTData1 = null;        
    ConnectionFactory ConnectionFactory1 = null;        
    
    try
    {        
        
        /*
         * Get runnings parameters
         */        
        if (request.getParameter("txtLanguage") != null)
        {
            strLanguageCode = request.getParameter("txtLanguage").toString();
        }    
        else
        {
            strLanguageCode = "pt";
        }


        
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
         * Create the objects
         */
        CENTSession1 = new CENTData();
        CBUSCore1 = new CBUSCore(Connection1, CENTSession1);

        
        /*
         * Keep information related to culture
         */        
        if (strLanguageCode.equals("pt"))
        {
            strCountry = "br";
            CENTSession1.SetText(Global1.SESSION_MASK_DATE, "dd/MM/yyyy");
        }
        
        if (strLanguageCode.equals("en"))
        {    
            strCountry = "us";            
            CENTSession1.SetText(Global1.SESSION_MASK_DATE, "MM/dd/yyyy");
        }            

        if (strLanguageCode.equals("sp"))
        {
            strCountry = "sp";
            CENTSession1.SetText(Global1.SESSION_MASK_DATE, "dd/MM/yyyy");            
        }            

        session.setAttribute("language_code", strLanguageCode);        
        session.setAttribute("country_code", strCountry);
        
        CENTSession1.SetText(Global1.SESSION_COUNTRY, strCountry);
        CENTSession1.SetText(Global1.SESSION_LANGUAGE, strLanguageCode);
        CENTSession1.SetDate(Global1.SESSION_DATE, new Date());
        CENTSession1.SetInt(Global1.SESSION_COMPANY, 1);
        CENTSession1.SetInt(Global1.SESSION_PROFILE, 1);
        CENTSession1.SetInt(Global1.SESSION_USER, 1);
        CENTSession1.SetInt(Global1.SESSION_AREA, 1);
        
        /*
         * Initialize the session (mandatory)
         */
        session.setAttribute("Login", CENTSession1);
        session.setAttribute("Filter", "");
        
        if (strSystemDate.trim().equals(""))
        {
            strSystemDate = CBUSCore1.FormatDate(new Date());
        }

        /*
         * According to the culture, keep the dictionary into the session
         */
        CENTFilter1 = new CENTData();
        CENTFilter1.SetText(2, strLanguageCode);
        CBUSCore1.SetIdTransaction(Global1.TRN_LANGUAGE);
        CBUSCore1.SetIdView(-1);
        ListCENTData1 = CBUSCore1.GetList(CENTFilter1);
               
        for (CENTData CENTData2 : ListCENTData1)
        {
            intIdLanguage = CENTData2.GetInt(Global1.SYSTEM_FIELD_ID);
        }
        
        /*
         * According to the culture, keep the dictionary into the session
         */
        CENTFilter1 = new CENTData();
        CENTFilter1.SetInt(2, intIdLanguage);
        CBUSCore1.SetIdTransaction(Global1.TRN_DICTIONARY);
        CBUSCore1.SetIdView(-1);
        ListCENTData1 = CBUSCore1.GetList(CENTFilter1);
        session.setAttribute("Dictionary", ListCENTData1);

        /*
         * Translate the items
         */                        
        strUsername = CBUSCore1.Translate(ListCENTData1, "Login.Username");
        strPassword = CBUSCore1.Translate(ListCENTData1, "Login.Password");
        strCompany = CBUSCore1.Translate(ListCENTData1, "Login.Company");
        strLanguage = CBUSCore1.Translate(ListCENTData1, "Login.Language");
        strLogin = CBUSCore1.Translate(ListCENTData1, "Login.Login");
        strDate = CBUSCore1.Translate(ListCENTData1, "Login.Date");
        
        /*
         * Home page information
         */        
        strText1 = CBUSCore1.Translate(ListCENTData1, "Homepage.Text1");
        strText2 = CBUSCore1.Translate(ListCENTData1, "Homepage.Text2");        
        strText3 = CBUSCore1.Translate(ListCENTData1, "Homepage.Text3");
        strText4 = CBUSCore1.Translate(ListCENTData1, "Homepage.Text4");        
        strText5 = CBUSCore1.Translate(ListCENTData1, "Homepage.Text5");
        strText6 = CBUSCore1.Translate(ListCENTData1, "Homepage.Text6");
        strText7 = CBUSCore1.Translate(ListCENTData1, "Homepage.Text7");        
        strText8 = CBUSCore1.Translate(ListCENTData1, "Homepage.Text8");        
        
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
        out.print(CENTException1.GetMessage() + " - " + CENTException1.GetCode());
    }    
    catch (Exception Exception1)
    {
        out.print(Exception1.getMessage());
    }
    finally
    {
        CENTFilter1 = null;    
        ListCENTData1 = null;    
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
        
    
    <div class="container">
              
        <label for="txtLanguage"><%out.print(strLanguage);%></label>
        <select class=" " id="txtLanguage" name="txtLanguage"  onchange="JavaScript:ChangeLanguage(this.options[this.selectedIndex].value);">
            <option value="pt" <%if (strLanguageCode.equals("pt")) {out.print("selected");}%> >Português</option>
            <option value="en" <%if (strLanguageCode.equals("en")) {out.print("selected");}%> >English</option>
            <option value="sp" <%if (strLanguageCode.equals("sp")) {out.print("selected");}%> >Español</option>
        </select>
        
        <div class="jumbotron">            
            <table>
                <tr>
                    <td>
                        <div class="jumbotron">
                            <h1><%out.println(strText1);%></h1> 
                            <p><%out.println(strText2);%></p>                                                         
                        </div>
                    </td>
                </tr>    
            </table>    

            <div class="col-sm-12">
                 <div class="row">
                     <div class="col-sm-4">
                         <div class="col-sm-12 col-xs-6">
                            <h4><span class="glyphicon glyphicon-upload" aria-hidden="true"></span>&nbsp;<%out.println(strText3);%></h4>                         
                         </div>
                     </div>
                     <div class="col-sm-4">
                         <div class="col-sm-12 col-xs-6">
                            <h4><span class="glyphicon glyphicon-refresh" aria-hidden="true"></span>&nbsp;<%out.println(strText4);%></h4>
                         </div>
                     </div>
                     <div class="col-sm-4">
                         <div class="col-sm-12 col-xs-6">
                         <div class="col-sm-12 col-xs-6">
                            <h4><span class="glyphicon glyphicon-check" aria-hidden="true"></span>&nbsp;<%out.println(strText5);%></h4>
                         </div>
                         </div>
                     </div>
                 </div>
             </div>                            
        &nbsp;            
        </div>

        <p align="right">                 
        <input class="" size="10" type="text" id="txtIdCompany" placeholder="<%out.print(strCompany);%>" value="1">        
        <input class="" size="10" type="text" id="txtUsername" placeholder="<%out.print(strUsername);%>" value="admin">
        <input class="" size="10" type="password" id="txtPassword" placeholder="<%out.print(strPassword);%>" value="admin">
        <button onClick="JavaScript:Login()" type="Button" class="btn btn-default"><%out.print(strLogin);%></button>
        <input class="" type="hidden" id="txtDate" placeholder="" value="<%out.print(strSystemDate);%>">                        
        </p>                         
                         
</form>
                            
<script language="JavaScript">

    function Login()
    {
        var strUrl = "";
        var strParam = "";
        var strUsername = "";
        var strPassword = "";
        var intIdCpny = "";
        var strDate = "";

        strUsername = document.getElementById("txtUsername").value;
        strPassword = document.getElementById("txtPassword").value;
        intIdCpny = document.getElementById("txtIdCompany").value;
        strDate = document.getElementById("txtDate").value;

        strUrl = "login.jsp?Username=" + strUsername + "&Password=" + strPassword + "&Company=" + intIdCpny + "&SystemDate=" + strDate;
        strParam = ajaxFunctionSync(strUrl.trim());                

        if (GetValueFromParamTag(strParam, "Status") == 'EXCEPTION')
        {
            alert(GetValueFromParamTag(strParam, "Code") + ": " + GetValueFromParamTag(strParam, "Message"));

            strField = GetValueFromParamTag(strParam, "Field").trim();         

            if (strField.trim() != "")
            {
                document.getElementById(strField).focus();
            }                                
        }           
        else
        {   
            window.self.location.href = "main.jsp";
        }
    }

    function ChangeLanguage(strLanguage)
    {
        document.frmDefault.method = "post";
        document.frmDefault.action = "homepage.jsp?txtLanguage=" + strLanguage;
        document.frmDefault.submit();
    }

</script>

<%@include file="../common/footer.jsp"%>    

</body>
</html>