<!DOCTYPE html>

<html>
<%@include file="../common/head.jsp"%>    

<body>
    
<%@include file="../common/menu_login.jsp"%>     

<%@include file="../common/bar.jsp"%>


<%
    CENTData CENTFilter1 = null;
%>

<%
    /*
     * General Declaration
     */
    boolean blnPermissionSave = false;
    boolean blnPermissionFilter = false;
    int intIdTrn = 0;
    int intIdFk = 0;
    int intValue = 0;
    int intIdView = 0;
    int intIdField = 0;
    int intIdEvent = 0;
    int intFieldType = 0;
    int intIdToUpdate = 0;
    int intSelectedValue = 0;
    int intIdFk2 = 0;        
    int intPageSize = 0;
    
    String strCountry = "";
    String strLanguage = "";
    String strAux = "";
    String strHtm = "";
    String strSpam = "";
    String strParam = "";        
    String strUrl = "";    
    String strLabel = "";    
    String strValue = "";    
    String strDomain = "";   
    String strOutput = "";    
    String strNewLine = "";  
    String strPattern = "";        
    String strSelected = "";    
    String strFieldName = "";
    String strFieldObject = "";
    String strNote = "";
    String strButtonSave = "";
    String strButtonBack = "";
    String strMessageSave = "";        
    String strButtonFilter = "";    
    String strMessageSelect = "";
    String strMessageDelete = "";
    String strHtmlObjectName = "";
    String strButtonStyle = "w3-button w3-blue";           
    
    
    String arrParam[] = null;
    
    CBUSCore CBUSCore1 = null;
    
    List<CENTData> ListCENTData1 = null;
    List<CENTData> ListCENTData2 = null;
    List<CENTData> ListCENTCatalog1 = null;
    List<CENTData> ListCENTPermission1 = null;
    List<CENTData> ListCENTView1 = null;
    
    /*
     * Create the objects
     */
    CENTFilter1 = new CENTData();        
    CBUSCore1 = new CBUSCore(Connection1, CENTSession1);    
    
    /*
     * Page Requests
     */    
    if (request.getParameter("cboIdView") != null)
    {
        intIdView = Integer.parseInt(request.getParameter("cboIdView"));
    }
    
    if (request.getParameter("txtPageSize") != null)
    {
        intPageSize = Integer.parseInt(request.getParameter("txtPageSize"));
    }    
    
    if (request.getParameter("Param") != null)
    {
        strParam = request.getParameter("Param");
        arrParam = strParam.split("/lb/");
        
        for (int i=0; i<=arrParam.length-1; i++)
        {
            strParam = arrParam[i]; 
            
            if (!GetValueFromParamTag(strParam, "event").equals(""))
            {    
                strAux = GetValueFromParamTag(strParam, "event");
                
                intIdEvent = CBUSCore1.StringToInteger(strAux);
            }    
            
            if (!GetValueFromParamTag(strParam, "id").equals(""))            
            {    
                intIdToUpdate = CBUSCore1.StringToInteger(GetValueFromParamTag(strParam, "id"));
            }    
        }        
    }    
    
    try
    {       
        /*
         * Define important values
         */        
        strNewLine = System.getProperty("line.separator");
        strButtonSave = CBUSCore1.Translate(ListCENTDictionary1, "Button.Save");
        strButtonFilter = CBUSCore1.Translate(ListCENTDictionary1, "Button.Filter");
        strButtonBack = CBUSCore1.Translate(ListCENTDictionary1, "Button.Back");
        strMessageSelect = CBUSCore1.Translate(ListCENTDictionary1, "MESSAGE_SELECT_AN_ITEM");
        strMessageSave = CBUSCore1.Translate(ListCENTDictionary1, "MESSAGE_CONFIRM_SAVE_RECORD");
        strMessageDelete = CBUSCore1.Translate(ListCENTDictionary1, "MESSAGE_CONFIRM_DELETE_RECORD");               
        /*
         * Access Control
         */              
        CBUSCore1.SetIdTransaction(intIdTransaction);        
        ListCENTPermission1 = CBUSCore1.GetPermission(CENTSession1);
               
        blnPermissionSave = CBUSCore1.isPermissioned("FUNCTION_SAVE", ListCENTPermission1);
        blnPermissionFilter = CBUSCore1.isPermissioned("FUNCTION_FILTER", ListCENTPermission1);
        
        /*
         * Execute the event
         */ 
        if (intIdEvent == Global1.EVENT_FILTER)
        {    
            
            ListCENTView1 = CBUSCore1.GetView(intIdView);
            if (ListCENTView1 == null)
            {    
                throw new CENTException("EXCEPTION_CATALOG_NOT_FOUND");
            }            
            
            for (CENTData CENTView1 : ListCENTView1)
            {
                /*
                 * Write the form
                 */
                intIdTrn = CENTView1.GetInt(Global1.FIELD_ID_TRN);
                intIdField = CENTView1.GetInt(Global1.FIELD_ID);
                strDomain = CENTView1.GetText(Global1.FIELD_DOMAIN_NAME);
                intIdFk = CENTView1.GetInt(Global1.FIELD_ID_FK);
                strLabel = CENTView1.GetText(Global1.FIELD_LABEL);
                strFieldName = CENTView1.GetText(Global1.FIELD_NAME);
                intFieldType = CENTView1.GetInt(Global1.FIELD_TYPE);
                strFieldObject = CENTView1.GetText(Global1.FIELD_OBJECT);
                strNote = CENTView1.GetText(Global1.FIELD_NOTE);               
                strHtmlObjectName = CENTView1.GetText(Global1.FIELD_NAME) + "_" + String.valueOf(intIdTrn);
                
                if (CENTView1.GetInt(Global1.FIELD_ID_COMMAND) < Global1.SELECTABLE_FIELD) // Discard group 
                {
                    strHtm += "<div class='row'>";
                    strHtm += "<div class='form-group col-lg-6'>"; // Setup the field size, turn a parameter in the future
                    strHtm += "<label style='cursor:pointer' onClick='JavaScript:ShowHelp(" + intIdField + ");' for='" + strHtmlObjectName + "'>" + CBUSCore1.Translate(ListCENTDictionary1, strLabel) + "</label>";
                    strSpam += "<span id='" + intIdField + "' style='visibility:hidden'>&nbsp;" + strNote + "</span>";
                    /*
                     * Non zero FK means that we have to get the values from a dropdown
                     */                                        
                    if (intIdFk == 0)
                    {                    
                        strHtm += "<input " + strPattern + " type='text' class='w3-input w3-border' style='width:30%' id='" + strHtmlObjectName + "' value='" + strValue + "'>";
                    }
                    else
                    {
                        /*
                         * Create the dropdown and add the events when necessary
                         */                    
                        strHtm += "<select " + strPattern + " class='w3-input w3-border' style='width:30%' id='" + strHtmlObjectName + "'";
                        strHtm += GetEventFunction(CBUSCore1, intIdTransaction, strFieldName, Global1.EVENT_FILTER);
                        strHtm += ">";
                        /*
                         * Add new items to drop down
                         */
                        strHtm += "<option value=" + "'" + "0" + "'" + strSelected + ">" + "Selecionar" + "</option>";
                        CENTFilter1 = new CENTData();
                        if (intIdFk == Global1.TRN_CATALOG)
                        {
                            /*
                             * When filtering a recon area, use the current transaction to filter fields, else, do nothing
                             */
                            if (intIdTypeTransaction == Global1.TRANSACTION_TYPE_USER)
                            {    
                                CENTFilter1.SetInt(2, intIdTransaction);
                            }    
                            else
                            {
                                if (intIdTransaction == Global1.TRN_VIEW_DEF)
                                {
                                    CENTFilter1.SetInt(2, -1);
                                }
                                if (intIdTransaction == Global1.TRN_LAYOUT_LAYOUT_LOOKUP)
                                {
                                    CENTFilter1.SetInt(2, -1);
                                }                                
                                
                                if (intIdTransaction == Global1.TRN_LAYOUT_FUNCTION)
                                {
                                    CENTFilter1.SetInt(2, -1);
                                }
                                
                                if (intIdTransaction == Global1.TRN_LAYOUT_SESSION_DEFINITION)
                                {
                                    CENTFilter1.SetInt(2, -1);
                                }
                                
                                if (intIdTransaction == Global1.TRN_RECONCILIATION_STEP_RULE_FIELD)
                                {
                                    CENTFilter1.SetInt(2, -1);
                                }
                                if (intIdTransaction == Global1.TRN_MATCH)
                                {
                                    CENTFilter1.SetInt(2, -1);
                                }
                                
                            }
                            
                            CENTFilter1.SetText(1, "");
                            CBUSCore1.SetIdTransaction(Global1.TRN_CATALOG);
                            CBUSCore1.SetIdView(-1);                        
                        }                        
                        else if (intIdFk == Global1.TRN_DOMAIN)
                        {
                            CENTFilter1.SetText(1, strDomain);
                            CBUSCore1.SetIdTransaction(Global1.TRN_DOMAIN);
                            CBUSCore1.SetIdView(Global1.TRN_DOMAIN);                        
                        }                            
                        else
                        {
                            
                            /*
                             * When filtering a recon area, use the current transaction to filter fields, else, do nothing
                             */
                            if (intIdTransaction == Global1.TRN_LAYOUT_SESSION_DEFINITION)
                            {
                                if (strFieldName.equals("id_session"))
                                {
                                    CENTFilter1.SetInt(2, -1);                            
                                }
                            }
                            if (intIdTransaction == Global1.TRN_RECONCILIATION_STEP_RULE)
                            {
                                if (strFieldName.equals("id_step"))
                                {
                                    CENTFilter1.SetInt(2, -1);                            
                                }
                            }
                            
                            if (intIdTransaction == Global1.TRN_RECONCILIATION_STEP_RULE_FIELD)
                            {
                                if (strFieldName.equals("id_step") || strFieldName.equals("id_rule"))
                                {
                                    CENTFilter1.SetInt(2, -1);                            
                                }
                            }
                            if (intIdTransaction == Global1.TRN_MATCH)
                            {
                                if (strFieldName.equals("id_step") || strFieldName.equals("id_rule"))
                                {
                                    //CENTFilter1.SetInt(2, -1);                            
                                }
                            }
                            
                            CENTFilter1.SetText(1, "");
                            CBUSCore1.SetIdTransaction(intIdFk);
                            CBUSCore1.SetIdView(-1);                                                    
                        }
                        ListCENTData1 = CBUSCore1.GetList(CENTFilter1);
                        /*
                         * Default is text_1, but sometimes like domain have the main information in another field
                         */                        
                        for (CENTData CENTData1 : ListCENTData1)
                        {                       
                            if (intIdFk == Global1.TRN_CATALOG)
                            {
                                intValue = CENTData1.GetInt(Global1.SYSTEM_FIELD_ID);
                                strValue = CENTData1.GetText(Global1.FIELD_LABEL);
                            }                        
                            else if (intIdFk == Global1.TRN_DOMAIN)
                            {
                                intValue = CENTData1.GetInt("int_2");
                                strValue = CENTData1.GetText("text_2");
                            }                            
                            else
                            {
                                intValue = CENTData1.GetInt(Global1.SYSTEM_FIELD_ID);
                                strValue = CENTData1.GetText("text_1");
                            }
                            /*
                             * Write the option
                             */                                
                            strValue = CBUSCore1.Translate(ListCENTDictionary1, strValue);
                            strHtm += "<option value='" + intValue + "'" + strSelected + ">" + strValue + "</option>";
                        }
                        strHtm += "</select>";
                        intValue = 0;
                        strValue = "";                        
                    }
                    
                    strHtm += "</div>";   
                    strHtm += "</div>";                                           
                    /*
                     * Write javascript to read the form
                     */                        
                    strOutput += "strParam += '[" + strHtmlObjectName + "]' + "
                              + "document.getElementById(" + "\"" + strHtmlObjectName + "\"" + ").value + "
                              + "'[/" + strHtmlObjectName + "]';" + strNewLine;                    
                }
            }        
        }                
        
        if (intIdEvent == Global1.EVENT_NEW)
        {    
            /*
             * Get the catalog
             */        
            ListCENTCatalog1 = CBUSCore1.GetCatalog(intIdTransaction);           
            
            if (ListCENTCatalog1 == null)
            {    
                throw new CENTException("EXCEPTION_CATALOG_NOT_FOUND");
            }
            
            for (CENTData CENTCatalog1 : ListCENTCatalog1)
            {
                /*
                 * Disable indicated fields
                 */
                if (CENTCatalog1.GetText(Global1.FIELD_NAME).equals("id"))
                    strPattern = "disabled";
                else
                    strPattern = "";
                /*
                 * To filter, enable the Id
                 */                    
                if (intIdEvent == Global1.EVENT_FILTER)
                {    
                    strPattern = "";
                }    
                /*
                 * Write the form
                 */
                intIdField = CENTCatalog1.GetInt(Global1.FIELD_ID);
                strDomain = CENTCatalog1.GetText(Global1.FIELD_DOMAIN_NAME);
                intIdFk = CENTCatalog1.GetInt(Global1.FIELD_ID_FK);
                strLabel = CENTCatalog1.GetText(Global1.FIELD_LABEL);
                strFieldName = CENTCatalog1.GetText(Global1.FIELD_NAME);
                intFieldType = CENTCatalog1.GetInt(Global1.FIELD_TYPE);
                strFieldObject = CENTCatalog1.GetText(Global1.FIELD_OBJECT);
                strNote = CENTCatalog1.GetText(Global1.FIELD_NOTE);
                strHtm += "<div class=''>";
                strHtm += "<div class=''>"; // Setup the field size, turn a parameter in the future
                
                strHtm += "<label style='cursor:pointer' onClick='JavaScript:ShowHelp(" + intIdField + ");' for='" + strFieldName + "'>" + CBUSCore1.Translate(ListCENTDictionary1, strLabel) + "</label>";
                strSpam += "<span id='" + intIdField + "' style='visibility:hidden'>&nbsp;" + strNote + "</span>";                    
                /*
                 * Non zero FK means that we have to get the values from a dropdown
                 */                                        
                if (intIdFk == 0)
                {                    
                    strHtm += "<input " + strPattern + " type='text' class='w3-input w3-border' style='width:30%' id='" + strFieldName + "' value='" + strValue + "'>";
                }
                else
                {                    
                    /*
                     * Create the dropdown and add the events when necessary
                     */                    
                    strHtm += "<select " + strPattern + " class='w3-input w3-border' style='width:30%' id='" + strFieldName + "'";
                    strHtm += GetEventFunction(CBUSCore1, intIdTransaction, strFieldName, Global1.EVENT_NEW);
                    strHtm += ">";
                        
                    /*
                     * Add new items to drop down
                     */
                    strHtm += "<option value=" + "'" + "0" + "'" + strSelected + ">" + "Selecionar" + "</option>";
                    CENTFilter1 = new CENTData();
                    
                    if (intIdFk == Global1.TRN_CATALOG)
                    {                        
                        /*
                         * When filtering a recon area, use the current transaction to filter fields, else, do nothing
                         */
                        if (intIdTransaction == Global1.TRN_VIEW_DEF)
                        {
                            CENTFilter1.SetInt(2, -1);
                        }
                        if (intIdTransaction == Global1.TRN_LAYOUT_LAYOUT_LOOKUP)
                        {
                            CENTFilter1.SetInt(2, -1);
                        }                                
                        if (intIdTransaction == Global1.TRN_LAYOUT_FUNCTION)
                        {
                            CENTFilter1.SetInt(2, -1);
                        }
                        if (intIdTransaction == Global1.TRN_LAYOUT_SESSION_DEFINITION)
                        {
                            CENTFilter1.SetInt(2, -1);
                                                        
                        }
                        if (intIdTransaction == Global1.TRN_RECONCILIATION_STEP_RULE_FIELD)
                        {
                            CENTFilter1.SetInt(2, -1);
                        }
                        if (intIdTransaction == Global1.TRN_MATCH)
                        {
                            CENTFilter1.SetInt(2, -1);
                        }
                        
                        CENTFilter1.SetText(1, "");
                        CBUSCore1.SetIdTransaction(Global1.TRN_CATALOG);
                        CBUSCore1.SetIdView(-1);                        
                    }                        
                    else if (intIdFk == Global1.TRN_DOMAIN)
                    {
                        CENTFilter1.SetText(1, strDomain);
                        CBUSCore1.SetIdTransaction(Global1.TRN_DOMAIN);
                        CBUSCore1.SetIdView(Global1.TRN_DOMAIN);                        
                    }
                    else
                    {
                        /*
                         * User transactions only
                         */
                        if (
                                intIdTransaction == Global1.TRN_LAYOUT || 
                                intIdTransaction == Global1.TRN_LAYOUT_SESSION_DEFINITION || 
                                intIdTransaction == Global1.TRN_LAYOUT_LAYOUT_LOOKUP || 
                                intIdTransaction == Global1.TRN_LAYOUT_FUNCTION || 
                                intIdTransaction == Global1.TRN_RECONCILIATION || 
                                intIdTransaction == Global1.TRN_RECONCILIATION_STEP_RULE_FIELD || 
                                intIdTransaction == Global1.TRN_MATCH
                            )
                        {
                            if (strFieldName.equals("id_transaction"))
                            {
                                CENTFilter1.SetInt(4, Global1.TRANSACTION_TYPE_USER);                                
                            }
                        }                        
                        
                        /*
                         * Apply the filter
                         */
                        CENTFilter1.SetText(1, "");
                        CBUSCore1.SetIdTransaction(intIdFk);
                        CBUSCore1.SetIdView(Global1.DO_NOT_USE_VIEW);                                                    
                    }
                    ListCENTData1 = CBUSCore1.GetList(CENTFilter1);
                    /*
                     * Default is text_1, but sometimes like domain have the main information in another field
                     */                        
                    for (CENTData CENTData1 : ListCENTData1)
                    {                       
                        if (intIdFk == Global1.TRN_CATALOG)
                        {
                            intValue = CENTData1.GetInt(Global1.SYSTEM_FIELD_ID);
                            strValue = CENTData1.GetText(Global1.FIELD_LABEL);
                        }                        
                        else if (intIdFk == Global1.TRN_DOMAIN)
                        {
                            intValue = CENTData1.GetInt("int_2");
                            strValue = CENTData1.GetText("text_2");
                        }                            
                        else
                        {
                            intValue = CENTData1.GetInt(Global1.SYSTEM_FIELD_ID);
                            strValue = CENTData1.GetText("text_1");
                        }
                        /*
                         * Write the option
                         */                                
                        strValue = CBUSCore1.Translate(ListCENTDictionary1, strValue);
                        strHtm += "<option value='" + intValue + "'" + strSelected + ">" + strValue + "</option>";
                    }
                    strHtm += "</select>";
                    intValue = 0;
                    strValue = "";                        
                }
                strHtm += "</div>";   
                strHtm += "</div>";   
                
                strHtm += "<br>";   
                /*
                 * Write javascript to read the form
                 */                        
                 strOutput += "strParam += '[" + strFieldName + "]' + document.getElementById(" 
                           + "\"" + strFieldName 
                           + "\"" + ").value + '[/" + strFieldName + "]';" + strNewLine;                 
            }
        }
        
        if (intIdEvent == Global1.EVENT_EDIT)    
        {
            
            /*
             * Get the catalog
             */        
            ListCENTCatalog1 = CBUSCore1.GetCatalog(intIdTransaction);
            if (ListCENTCatalog1 == null)
            {    
                throw new CENTException("EXCEPTION_CATALOG_NOT_FOUND");
            }            
            
            /*
             * Query the record to edit
             */         
            CENTFilter1 = new CENTData();                                    
            CENTFilter1.SetInt(Global1.SYSTEM_FIELD_ID, intIdToUpdate);            
            CBUSCore1.SetIdTransaction(intIdTransaction);
            CBUSCore1.SetIdView(Global1.DO_NOT_USE_VIEW); // Catalog
            CBUSCore1.SetIdEvent(Global1.EVENT_EDIT);
            ListCENTData1 = CBUSCore1.GetList(CENTFilter1);
            intIdFk2 = ListCENTData1.get(0).GetInt(2);
            
            /*
             * Fill the value field - david
             */                
            for (CENTData CENTData1 : ListCENTData1)
            {                                
                for (CENTData CENTCatalog1 : ListCENTCatalog1)
                {
                    strFieldName = CENTCatalog1.GetText(Global1.FIELD_NAME);
                    strFieldObject = CENTCatalog1.GetText(Global1.FIELD_OBJECT);
                    intFieldType = CENTCatalog1.GetInt(Global1.FIELD_TYPE);
                    strLabel = CBUSCore1.Translate(ListCENTDictionary1, CENTCatalog1.GetText(Global1.FIELD_LABEL));           
                    strDomain = CENTCatalog1.GetText(Global1.FIELD_DOMAIN_NAME);
                    intIdFk = CENTCatalog1.GetInt(Global1.FIELD_ID_FK);
                    
                    if (CBUSCore1.isMappeableField(strFieldObject) || strFieldObject.equals(Global1.SYSTEM_FIELD_ID))
                    {
                        if (intFieldType == Global1.TYPE_INT)
                        {    
                            intValue = CENTData1.GetInt(strFieldObject);
                            strValue = CBUSCore1.FormatInt(CENTData1.GetInt(strFieldObject));
                            intSelectedValue = intValue;
                        }    
                        if (intFieldType == Global1.TYPE_TEXT)
                        {
                            if (CENTData1.GetText(strFieldObject) != null)
                            {
                                strValue = CENTData1.GetText(strFieldObject);
                            }    
                        }                        
                        if (intFieldType == Global1.TYPE_DATE)
                        {    
                            if (CENTData1.GetDate(strFieldObject) != null)
                            {
                                strValue = CBUSCore1.FormatDate(CENTData1.GetDate(strFieldObject));
                            }    
                        }    
                        if (intFieldType == Global1.TYPE_DOUBLE)
                        {    
                            strValue = CBUSCore1.FormatNumber(CENTData1.GetDouble(strFieldObject), 2);
                        }                        
                        if (intFieldType == Global1.TYPE_BOOLEAN)
                        {    
                            intValue = CENTData1.GetBoolean(strFieldObject);
                            intSelectedValue = intValue;
                        }                        
                        /*
                         * Disable indicated fields
                         */
                        if (CENTCatalog1.GetText(Global1.FIELD_NAME).equals("id"))
                            strPattern = "disabled";
                        else
                            strPattern = "";
                        /*
                         * Write the form
                         */
                        strHtm += "<div class='row'>";
                        strHtm += "    <div class='form-group col-lg-4'>";
                        strHtm += "        <label for='" + strFieldName + "'>" + strLabel + "</label>";
                        /*
                         * Non zero FK means that we have to get the values from a dropdown
                         */               
                        if (intIdFk == 0)
                        {
                            strHtm += "<input " + strPattern + " type='text' class='w3-input w3-border' style='width:30%' id='" + strFieldName + "' value='" + strValue + "'>";
                        }
                        else
                        {
                            /*
                             * Create the dropdown and add the events when necessary
                             */                    
                            strHtm += "<select " + strPattern + " class='w3-input w3-border' style='width:30%' id='" + strFieldName + "'";
                            strHtm += GetEventFunction(CBUSCore1, intIdTransaction, strFieldName, Global1.EVENT_EDIT);
                            strHtm += ">";
                            /*
                             * Filter according to the key
                             */
                            strHtm += "<option value=" + "'" + "0" + "'" + strSelected + ">" + "Selecionar" + "</option>";                        
                            CENTFilter1 = new CENTData();
                            
                            if (intIdFk == Global1.TRN_CATALOG)
                            {
                                /*
                                 * Filter the field by current transaction
                                 */
                                if (intIdTransaction == Global1.TRN_VIEW_DEF)
                                {
                                    CENTFilter1.SetInt(2, CENTData1.GetInt(4));
                                }
                                if (intIdTransaction == Global1.TRN_LAYOUT_SESSION_DEFINITION)
                                {
                                    CENTFilter1.SetInt(2, CENTData1.GetInt(6));
                                }                                
                                
                                if (intIdTransaction == Global1.TRN_LAYOUT_LAYOUT_LOOKUP)
                                {
                                    CENTFilter1.SetInt(2, CENTData1.GetInt(3));
                                }                                
                                if (intIdTransaction == Global1.TRN_LAYOUT_FUNCTION)
                                {
                                    CENTFilter1.SetInt(2, CENTData1.GetInt(3));
                                }
                                if (intIdTransaction == Global1.TRN_RECONCILIATION_STEP_RULE_FIELD)
                                {
                                    if (strFieldName.equals("id_field"))
                                    {
                                        CENTFilter1.SetInt(2, CENTData1.GetInt(5));
                                    }
                                }
                                if (intIdTransaction == Global1.TRN_MATCH)
                                {
                                    if (strFieldName.equals("id_field"))
                                    {
                                        CENTFilter1.SetInt(2, CENTData1.GetInt(6));
                                    }
                                }                            
                                CENTFilter1.SetText(1, "");                                
                                CBUSCore1.SetIdTransaction(Global1.TRN_CATALOG);
                                CBUSCore1.SetIdView(-1); // Access Catalog);
                            }                        
                            else if (intIdFk == Global1.TRN_DOMAIN)
                            {
                                CENTFilter1.SetText(1, strDomain);                                
                                CBUSCore1.SetIdTransaction(Global1.TRN_DOMAIN);
                                CBUSCore1.SetIdView(-1); // Access Catalog);                                
                            }                            
                            else
                            {
                                /*
                                 * Disable the possibility to change wrong fields
                                 */
                                if (intIdTransaction == Global1.TRN_VIEW_DEF)
                                {
                                    if (strFieldName.equals("id_view"))
                                    {
                                        CENTFilter1.SetInt(Global1.SYSTEM_FIELD_ID, CENTData1.GetInt(2));
                                    }
                                    if (strFieldName.equals("id_transaction"))
                                    {
                                        CENTFilter1.SetInt(Global1.SYSTEM_FIELD_ID, CENTData1.GetInt(4));
                                    }                                
                                }                            
                                if (intIdTransaction == Global1.TRN_LAYOUT)
                                {
                                    if (strFieldName.equals("id_transaction"))
                                    {
                                        CENTFilter1.SetInt(4, Global1.TRANSACTION_TYPE_USER);
                                    }
                                }                                
                                
                                if (intIdTransaction == Global1.TRN_LAYOUT_SESSION)
                                {
                                    if (strFieldName.equals("id_layout"))
                                    {
                                        CENTFilter1.SetInt(Global1.SYSTEM_FIELD_ID, CENTData1.GetInt(2));
                                    }
                                }
                                if (intIdTransaction == Global1.TRN_LAYOUT_SESSION_DEFINITION)
                                {                                                                        
                                    if (strFieldName.equals("id_layout"))
                                    {
                                        CENTFilter1.SetInt(Global1.SYSTEM_FIELD_ID, CENTData1.GetInt(2));
                                    }
                                    if (strFieldName.equals("id_session"))
                                    {
                                        CENTFilter1.SetInt(Global1.SYSTEM_FIELD_ID, CENTData1.GetInt(3));
                                    }
                                    
                                    if (strFieldName.equals("id_transaction"))
                                    {
                                        CENTFilter1.SetInt(4, Global1.TRANSACTION_TYPE_USER);
                                    }                                    
                                }                                
                                if (intIdTransaction == Global1.TRN_LAYOUT_LAYOUT_LOOKUP)
                                {
                                    if (strFieldName.equals("id_transaction"))
                                    {
                                        CENTFilter1.SetInt(4, Global1.TRANSACTION_TYPE_USER);
                                    }
                                }
                                
                                if (intIdTransaction == Global1.TRN_LAYOUT_FUNCTION)
                                {
                                    if (strFieldName.equals("id_transaction"))
                                    {
                                        CENTFilter1.SetInt(4, Global1.TRANSACTION_TYPE_USER);
                                    }
                                }                                
                                if (intIdTransaction == Global1.TRN_RECONCILIATION)
                                {
                                    if (strFieldName.equals("id_transaction"))
                                    {
                                        CENTFilter1.SetInt(4, Global1.TRANSACTION_TYPE_USER);
                                    }
                                }
                                
                                if (intIdTransaction == Global1.TRN_RECONCILIATION_STEP)
                                {
                                    if (strFieldName.equals("id_reconcile"))
                                    {
                                        CENTFilter1.SetInt(Global1.SYSTEM_FIELD_ID, CENTData1.GetInt(2));
                                    }
                                }                            
                                if (intIdTransaction == Global1.TRN_RECONCILIATION_STEP_RULE)
                                {
                                    if (strFieldName.equals("id_reconcile"))
                                    {
                                        CENTFilter1.SetInt(Global1.SYSTEM_FIELD_ID, CENTData1.GetInt(2));
                                    }
                                    if (strFieldName.equals("id_step"))
                                    {
                                        CENTFilter1.SetInt(2, CENTData1.GetInt(2));
                                    }
                                }
                                if (intIdTransaction == Global1.TRN_RECONCILIATION_STEP_RULE_FIELD)
                                {
                                    if (strFieldName.equals("id_reconcile"))
                                    {
                                        CENTFilter1.SetInt(Global1.SYSTEM_FIELD_ID, CENTData1.GetInt(2));
                                    }
                                    if (strFieldName.equals("id_step"))
                                    {
                                        CENTFilter1.SetInt(Global1.SYSTEM_FIELD_ID, CENTData1.GetInt(2));
                                    }
                                    if (strFieldName.equals("id_rule"))
                                    {
                                        CENTFilter1.SetInt(Global1.SYSTEM_FIELD_ID, CENTData1.GetInt(3));
                                    }
                                    if (strFieldName.equals("id_transaction"))
                                    {
                                        CENTFilter1.SetInt(Global1.SYSTEM_FIELD_ID, CENTData1.GetInt(5));
                                        CENTFilter1.SetInt(4, Global1.TRANSACTION_TYPE_USER);
                                    }
                                    
                                }
                                if (intIdTransaction == Global1.TRN_MATCH)
                                {                                
                                    if (strFieldName.equals("id_step"))
                                    {
                                        CENTFilter1.SetInt(4, CENTData1.GetInt(3));
                                    }
                                    if (strFieldName.equals("id_rule"))
                                    {
                                        CENTFilter1.SetInt(5, CENTData1.GetInt(4));
                                    }
                                    if (strFieldName.equals("id_transaction"))
                                    {
                                        CENTFilter1.SetInt(6, CENTData1.GetInt(5));
                                        CENTFilter1.SetInt(4, Global1.TRANSACTION_TYPE_USER);                                        
                                    }
                                }
                                /*
                                 * Apply the filter
                                 */
                                CENTFilter1.SetText(1, "");
                                CBUSCore1.SetIdTransaction(intIdFk);
                                CBUSCore1.SetIdView(Global1.DO_NOT_USE_VIEW); // Access Catalog);
                            }                        
                            
                            ListCENTData2 = CBUSCore1.GetList(CENTFilter1);
                            
                            for (CENTData CENTData2 : ListCENTData2)
                            {                                                 
                                if (intIdFk == Global1.TRN_DOMAIN)
                                {
                                    intValue = CENTData2.GetInt("int_2");
                                    strValue = CENTData2.GetText("text_2");
                                }                            
                                else
                                {
                                    intValue = CENTData2.GetInt(Global1.SYSTEM_FIELD_ID);
                                    strValue = CENTData2.GetText("text_1");
                                }                                                    
                                /*
                                 * Check if it is the selected item
                                 */
                                if (intSelectedValue == intValue)
                                    strSelected = "selected";
                                else
                                    strSelected = "";
                                /*
                                 * Write the option
                                 */
                                strValue = CBUSCore1.Translate(ListCENTDictionary1, strValue);
                                strHtm += "<option value='" + intValue + "'" + strSelected + ">" + strValue + "</option>";
                            }
                            strHtm += "</select>";
                            intValue = 0;
                            strValue = "";
                        }                        
                        strHtm += "</div>";
                        strHtm += "</div>";   
                        
                        strHtm += "<br>";                           
                        
                        /*
                         * Write javascript to read the form
                         */                                                        
                        strOutput += "strParam += '[" + strFieldName + "]' + document.getElementById(" + "\"" 
                                  + strFieldName + "\"" + ").value + '[/" + strFieldName + "]';" + strNewLine;
                    }                    
                }        
            }                
        }        
      
        
    }
    catch (CENTException CENTException1)
    {
        strUrl += "../common/exception.jsp";
        strUrl += "?source=" + "form1.jsp" ;
        strUrl += "&code=" + CENTException1.GetCode();
        strUrl += "&message=" + CENTException1.GetMessage();
        
        response.sendRedirect(strUrl);        
    }    
    catch (Exception Exception1)
    {
        strUrl += "../common/exception.jsp";
        strUrl += "?source=" + "form1.jsp" ;
        strUrl += "&code=";
        strUrl += "&message=" + Exception1.getMessage();
        
        response.sendRedirect(strUrl);
    }
    finally
    {
        CBUSCore1 = null;
        ListCENTCatalog1 = null;
        Connection1 = null;
        ConnectionFactory1 = null;        
    }   
    
    
    
%>
<div class="w3-container">
    <form name="frmDefault" class="w3-container">

        <input type="hidden" id="txtMessageSelect" value="<%out.print(strMessageSelect);%>">
        <input type="hidden" id="txtMessageSave" value="<%out.print(strMessageSave);%>">
        <input type="hidden" id="txtMessageDelete" value="<%out.print(strMessageDelete);%>">              
        <input type="hidden" id="cboIdView" name="cboIdView" value="<%out.print(intIdView);%>">      
        <input type="hidden" id="txtPageSize" name="txtPageSize" value="<%out.print(intPageSize);%>">      

        <%out.print(SetTitle(1, strNameTransaction, "", ""));%>

        <form role="form" class="w3-container">               
            <%
            out.print(strHtm.replaceAll("'", "\""));
            %>
                <br>    
                <br>    
                <%if ((intIdEvent != Global1.EVENT_FILTER) && (blnPermissionSave == true)) {%><button type="button" name="btnSave"  OnClick="JavaScript:Save(PAGE_NAME)" class="<%out.print(strButtonStyle);%>"><%out.print(strButtonSave);%> </button><%}%>
                <%if ((intIdEvent == Global1.EVENT_FILTER) && (blnPermissionFilter == true)) {%><button type="button" name="btnFilter" OnClick="JavaScript:FilterList(PAGE_NAME)" class="<%out.print(strButtonStyle);%>"><%out.print(strButtonFilter);%> </button><%}%>
                <button type="button" name="btnBack" OnClick="JavaScript:Back(PAGE_NAME)" class="<%out.print(strButtonStyle);%>"><%out.print(strButtonBack);%> </button>                            
            <%
            out.print(strSpam);
            %>                

        </form>                

    </form>
</div>              
            
       
<%@include file="../common/footer.jsp"%>
            
            
<script language="JavaScript">    
    
   var PAGE_NAME = 'main';
    /*
     * Read the form
     */    
    function GetData(intIdEvent)
    {        
        var strParam = "[?xml version='1.0' encoding='UTF-8'?]";
        
        strParam += '[forms]';
            strParam += '[form]';
            
            strParam += "[event]" + intIdEvent + "[/event]";
            <%out.print(strOutput);%>
            strParam += '[/form]';
        strParam += '[/forms]';
        
        strParam += '/lb/';
        
        return strParam;
    }    
    function ShowHelp(field) 
    {
        var element1 = document.getElementById(field);         
        var state1 = element1.style.visibility;
        var text1 = "";
        element1.style.visibility = "visible";             
        text1 = element1.innerText;
        element1.style.visibility = "hidden";
        alert(text1);
    }
    function SetDropDown(intIdEvent, ddw1, intId, intIdTrn)
    {                
        /*
         * General Declaration
         */
        var i = 0;
        var count = 0;
        var strText = "";
        var strUrl = "";
        var strUrl = "";
        var arrLine;
        var arrColumn;
        var blnSelect = false;
        
        try
        {
            if (intId == "")
            {
                intId = 0;
            }    
            /*
             * Add new item to dropdown
             */        
            strUrl = "../common/dropdown_ajax.jsp?id_trn=" + intIdTrn + "&id=" + intId;            
            strText = ajaxFunctionSync(strUrl);
            
            /*
             * Prepare to load the dropdow
             */        
            arrLine = strText.split(";");
            count = arrLine.length-1;
            /*
             * Fill the dropdown
             */
            ddw1.options.length = 0;    
            
            for (i=0; i<=count; i++)
            {
                arrColumn = arrLine[i].split("|");
                
                var option = document.createElement('option');
                option.value = arrColumn[0];
                option.text = arrColumn[1];
                if (intIdEvent == C_EVENT_UPDATE)
                {
                    if (parseInt(option.value) == parseInt(intId))
                    {
                        option.selected = true;
                    }
                }
                if (arrColumn[1] != null)
                {    
                    ddw1.add(option, i);
                }    
            } 
     
            
        }
        catch (Exception1)
        {
            alert(Exception1);
        }
    }  // end function    
</script>



</body>
</html>
