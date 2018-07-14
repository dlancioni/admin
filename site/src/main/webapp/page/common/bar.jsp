<%@page import="business.CBUSCore"%>
<%@page import="java.util.Date"%>
<%@page import="entity.*"%>
<%@page import="project.*"%>

<%                
    
    /*
     * General Declaration
     */
    Global Global2 = null;
    CENTData CENTSession2 = null;
    String strLanguageCode = "";
    String strCountryCode = "";
    String strSystemDate = "";
    String strCulture = "";
    String strUsername = "";
    String strProfile = "";
    String strArea = "";
    String strCompany = "";
    
    try
    {
        /*
         * Get the object with login information from the session
         */
        Global2 = new Global();           
        CENTSession2 = (CENTData) session.getAttribute("Login");
        CBUSCore CBUSCore1 = new CBUSCore(Connection1, CENTSession2);
        
        strUsername = CENTSession2.GetText(Global2.SESSION_USER);
        strProfile = CBUSCore1.Translate(CENTSession2.GetText(Global2.SESSION_PROFILE));
        strArea = CBUSCore1.Translate(CENTSession2.GetText(Global2.SESSION_AREA));
        strCompany = CBUSCore1.Translate(CENTSession2.GetText(Global2.SESSION_COMPANY));
                
        strLanguageCode = CENTSession2.GetText(Global2.SESSION_LANGUAGE);
        strCountryCode = CENTSession2.GetText(Global2.SESSION_COUNTRY);
        strCulture = strLanguageCode + "-" + strCountryCode;
        
        strSystemDate = CBUSCore1.FormatDate(CENTSession2.GetDate(Global2.SESSION_DATE));

    }
    catch (Exception Exception1)
    {
        CENTSession2 = new CENTData();
    }
    finally
    {
        CENTSession2 = null;
        Global2 = null;
    }    
%>
              
<div class="w3-container">                                
    <div class="w3-row">
      <div class="w3-container w3-quarter" >
          <img width='25' height='25' src='../../images/company.jpg'>
        <%out.print(strCompany);%>
      </div>
      <div class="w3-container w3-quarter">
        <img width='25' height='25' src='../../images/user.jpg'>&nbsp;
        <%out.print(strUsername);%>
      </div>
      <div class="w3-container w3-quarter">
        <img width='25' height='25' src='../../images/calendar.png'>
        <%out.print(strSystemDate);%>         
      </div>
      <div class="w3-container w3-quarter">
         <img width='25' height='25' src='../../images/language.jpg'>
        <%out.print(strCulture);%>
      </div>
    </div>  
</div>        