<%@include file="../common/declare.jsp"%>

<%
    /*
     * General Declaration
     */  
    int intIdCompany = 0;
    int intIdTransactionType = 0;
        
    String strUsername = "";
    String strPassword = "";
    String strCountry = "";
    String strLanguage = "";
    String strSystemDate = "";

    String strHtmlMenu = "";
    String strTransactionName = "";
    String strPageName = "";
    String strLink = "";        
    
    CBUSCore CBUSCore1 = null;    
    CBUSSetup CBUSSetup1 = null;    
    List<CENTData> ListCENTMenu1 = null;
    List<CENTData> ListCENTMenu2 = null;
    List<CENTData> ListCENTTransaction1 = null;    
    List<CENTData> ListCENTCatalogTransaction1 = null;

    try
    {
        /*
         * Get user input
         */
        if (request.getParameter("Username") != null)
        {
            strUsername = request.getParameter("Username");
        }
        
        if (request.getParameter("Password") != null)
        {
            strPassword = request.getParameter("Password");        
        }
        
        if (request.getParameter("Company") != null)
        {
            if (!request.getParameter("Company").equals(""))
            {
                intIdCompany = Integer.valueOf(request.getParameter("Company"));
            }    
        }
        
        if (request.getParameter("SystemDate") != null)
        {
            strSystemDate = request.getParameter("SystemDate");
        }
        
        strCountry = (String) request.getSession().getAttribute("country_code");
        
        strLanguage = (String) request.getSession().getAttribute("language_code");        
        
        /*
         * Create the objects
         */  
        CENTSession1 = new CENTData();                
        CENTSession1.SetInt(Global1.SESSION_COMPANY, intIdCompany);        
        CENTSession1.SetText(1, strUsername);
        CENTSession1.SetText(2, strPassword);
        CENTSession1.SetText(Global1.SESSION_COUNTRY, strCountry);
        CENTSession1.SetText(Global1.SESSION_LANGUAGE, strLanguage);

        /*
         * To create the object we need the session
         */
        CBUSCore1 = new CBUSCore(Connection1, CENTSession1);
        CBUSSetup1 = new CBUSSetup(Connection1, CENTSession1);
        CENTSession1.SetDate(Global1.SESSION_DATE, CBUSCore1.StringToDate(strSystemDate));        
        
        /*
         * Try to log in
         */                  
        CENTSession1 = CBUSCore1.Login(CENTSession1);
        
        /*
         * Create the main menu and keep it in the session to avoid performance
         */
        ListCENTCatalogTransaction1 = CBUSCore1.GetCatalog(Global1.TRN_TRANSACTION);        
                
        
        
        
        
        /*
         * Query the menus
         */
        ListCENTMenu1 = CBUSSetup1.GetMenu(0, 0); // all        
        
        /*
         * Mount the menu using pervious information
         */
        for (CENTData CENTMenu1 : ListCENTMenu1)
        {        
            /*
             * Handle main menu only
             */
            if (CENTMenu1.GetInt(2) == 0) // ID_MENU_PARENT
            {        
                /*
                 * Begin menu
                 */
                strHtmlMenu += "<li>" + Global1.lb;
                strHtmlMenu += "<a>" + CBUSCore1.Translate(CENTMenu1.GetText(1)) + "</a>";
                strHtmlMenu += "<ul>" + Global1.lb;
                    
                /*
                 * Query the submenus
                 */
                ListCENTMenu2 = CBUSSetup1.GetMenu(0, CENTMenu1.GetInt(Global1.SYSTEM_FIELD_ID)); // Get the children
                
                /*
                 * List it
                 */                
                for (CENTData CENTMenu2 : ListCENTMenu2)
                {                    
                    /*
                     * Submenu items
                     */
                    ListCENTTransaction1 = CBUSSetup1.GetTransaction(CENTMenu2.GetInt(Global1.SYSTEM_FIELD_ID));                    
                    
                    if (!ListCENTTransaction1.isEmpty())
                    {                    
                        /*
                         * Submenu header
                         */
                        strHtmlMenu += "<li>";
                        strHtmlMenu += "<a>" + CBUSCore1.Translate(CENTMenu2.GetText(1)) + "</a>";
                        strHtmlMenu += "<ul>";

                        /*
                         * Fill the items
                         */
                        for (CENTData CENTTransaction1 : ListCENTTransaction1)
                        {
                            intIdTransaction = CENTTransaction1.GetInt(CBUSCore1.GetFieldObject("id", ListCENTCatalogTransaction1));
                            strTransactionName = CBUSCore1.Translate(CENTTransaction1.GetText(CBUSCore1.GetFieldObject("name", ListCENTCatalogTransaction1)));
                            intIdTransactionType = CENTTransaction1.GetInt(CBUSCore1.GetFieldObject("id_type", ListCENTCatalogTransaction1));
                            strPageName = CBUSCore1.Translate(CENTTransaction1.GetText(CBUSCore1.GetFieldObject("page", ListCENTCatalogTransaction1)));
                            strLink = GetLink(strPageName, intIdTransaction, intIdTransactionType, strTransactionName);
                            strHtmlMenu += "<li><a href='" + strLink + "'>" + strTransactionName + "</a></li>" + Global1.lb;
                        }

                        strHtmlMenu += "</ul>" + Global1.lb;
                        strHtmlMenu += "</li>" + Global1.lb;
                    }
                }

                /*
                 * Current menu items
                 */
                ListCENTTransaction1 = CBUSSetup1.GetTransaction(CENTMenu1.GetInt(Global1.SYSTEM_FIELD_ID));

                /*
                 * Fill the items
                 */
                for (CENTData CENTTransaction1 : ListCENTTransaction1)
                {
                    intIdTransaction = CENTTransaction1.GetInt(CBUSCore1.GetFieldObject("id", ListCENTCatalogTransaction1));
                    strTransactionName = CBUSCore1.Translate(CENTTransaction1.GetText(CBUSCore1.GetFieldObject("name", ListCENTCatalogTransaction1)));
                    intIdTransactionType = CENTTransaction1.GetInt(CBUSCore1.GetFieldObject("id_type", ListCENTCatalogTransaction1));
                    strPageName = CBUSCore1.Translate(CENTTransaction1.GetText(CBUSCore1.GetFieldObject("page", ListCENTCatalogTransaction1)));
                    strLink = GetLink(strPageName, intIdTransaction, intIdTransactionType, strTransactionName);
                    strHtmlMenu += "<li><a href='" + strLink + "'>" + strTransactionName + "</a></li>" + Global1.lb;
                }

                /*
                 * End menu
                 */                                  
                strHtmlMenu += "</ul>" + Global1.lb;
                strHtmlMenu += "</li>" + Global1.lb;
            }    
        }
        
        CENTSession1.SetText(Global1.SESSION_MENU, strHtmlMenu);        
        
        /*
         * Keep the login object into the session
         */          
        session.setAttribute("Login", CENTSession1);

        /*
         * Go to the system
         */                        
        out.print("AUTHORIZED");

    }
    catch (CENTException CENTException1)
    {
        CENTException1.SetCode(CBUSCore1.Translate(CENTException1.GetCode()));
        CENTException1.SetMessage(CBUSCore1.Translate(CENTException1.GetMessage()));
        CENTException1.SetFieldName(CBUSCore1.Translate(CENTException1.GetFieldName()));
        

        out.print(MessageException(CENTException1));
    }    
    catch (Exception Exception1)
    {
        out.print(MessageException(Exception1));
    }
    finally
    {
        strUsername = null;
        strPassword = null;
        strCountry = null;
        strLanguage = null;
        strSystemDate = null;

        strHtmlMenu = null;
        strTransactionName = null;
        strPageName = null;
        strLink = null;        
        strLink = null;    

        CBUSCore1 = null;    
        CBUSSetup1 = null;    
        ListCENTMenu1 = null;
        ListCENTTransaction1 = null;    
        ListCENTCatalogTransaction1 = null;
    }        
    
%>


