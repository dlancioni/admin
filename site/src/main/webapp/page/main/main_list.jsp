<!DOCTYPE html>

<html>
<%@include file="../common/head.jsp"%>    
<body onunload="">      
     
<%@include file="../common/menu_login.jsp"%>

<%@include file="../common/bar.jsp"%>


<%
    boolean blnFirst = true;
    boolean blnPermissionNew = false;
    boolean blnPermissionEdit = false;    
    boolean blnPermissionDelete = false;    
    boolean blnPermissionFilter = false;        
    boolean blnPermissionImport = false;        
    boolean blnPermissionExport = false;
    boolean blnPermissionReconcile = false;        
    boolean blnPermissionSetup = false;
    boolean blnPermissionDuplicate = false;        
    boolean blnPermissionReprocess = false;        

    int intTotalPage = 0;
    int intDiff = 0;    
    int intRecordCount = 0;
    int intCountInt = 0;
    int intCountText = 0;
    int intCountDate = 0;
    int intCountDouble = 0;
    int intCountBoolean = 0;
    int intFlag = 0;
    int intFieldType = 0;    
    int intIdView = 0;
    int intIdControl = 1; 
    int intRowCount = 1;           
    int intPage = 1;
    int intPageSize = 10;
    int intPageStart = 0;
    int intPageEnd = 0;
    int intIdFk = 0;
    int intIdTrn = 0;
    int intIdCommand = 0;
    
    String strFieldName = "";
    String strFieldValue = "";    
    String strNavigationButton = "";    
    String strUrl = "";
    String strParam = "";
    String arrParam[] = null;
    String strTHead = "";
    String strTBody = "";
    String strTableFooter = "";
    String strValue = "";    
    String strLabel = "";
    String strFieldObject = "";
    String strOptionView = "";
    String strViewName = "";
    String strViewType = "";
    String strChart = "";
    String strChartDesc = "";
    String strChartValue = "";                
    String strFieldObjectChartDesc = "";
    String strFieldObjectChartValue = "";
    String strHtmlObjectName = "";
    String strChartNoInfo = "";    
    
    String strLabelView = "";
    String strLabelRecordCount = "";
    String strLabelPages = "";
    String strLabelLinePage = "";
    String strLabelGoPages = "";    
    
    String strButtonNew = "";
    String strButtonEdit = "";
    String strButtonDelete = "";
    String strButtonFilter = "";
    String strButtonImport = "";
    String strButtonExport = "";
    String strButtonReconcile = "";
    String strButtonSetup = "";
    String strButtonDuplicate = "";
    String strButtonReprocess = "";
    String strButtonBack = "";
    
    String strMessageSelect = "";
    String strMessageSave = "";
    String strMessageDelete = "";    
    String strMessageImport = "";
    String strMessageExport = "";
    String strMessageReconcile = "";        
    String strMessageSetup = "";        
    String strMessageDuplicate = "";        
    String strMessageReprocess = "";
    String strButtonStyle = "w3-button w3-blue";        

    CENTData CENTFilter1 = null;
    CBUSCore CBUSCore1 = null;
    
    List<CENTData> ListCENTData1 = null;   
    List<CENTData> ListCENTData2 = null;   
    List<CENTData> ListCENTData3 = null;
    List<CENTData> ListCENTCatalog1 = null;
    List<CENTData> ListCENTView1 = null;    
    List<CENTData> ListCENTPermission1 = null;
    List<CENTData> ListCENTView2 = null;

    arrParam = new String[0];

    try
    {
        /*
         * Request the parameters
         */    
        if (request.getParameter("cboIdView") != null)
        {
            intIdView = Integer.parseInt(request.getParameter("cboIdView"));
        }

        if (request.getParameter("ViewChanged") != null)
        {
            session.setAttribute("PageNumber", 1);
        }

        /*
         * Change page, must set page 1
         */
        if (request.getParameter("txtPageNumber") != null)
        {
            intPage = Integer.parseInt(request.getParameter("txtPageNumber"));
        }

        /*
         * Control page size
         */
        if (session.getAttribute("PageSize") != null)
        {
            if (request.getParameter("txtPageSize") != null)
            {
                intPageSize = Integer.parseInt(request.getParameter("txtPageSize"));
                session.setAttribute("PageSize", intPageSize);
            }
            else
            {
                intPageSize = Integer.parseInt(session.getAttribute("PageSize").toString());            
            }
        }
        else
        {
            if (request.getParameter("txtPageSize") != null)
            {
                intPageSize = Integer.parseInt(request.getParameter("txtPageSize"));
                session.setAttribute("PageSize", intPageSize);
            }
        }

        /*
         * Control XML
         */
        if (request.getParameter("Param") != null)
        {            
            strParam = request.getParameter("Param");
            session.setAttribute("Filter", strParam);
        }

        /*
         * Control filtering
         */        
        strParam = session.getAttribute("Filter").toString();
        if (!strParam.trim().equals(""))
            arrParam = strParam.split("/lb/");
        
        /*
         * Create the objects
         */
        CENTFilter1 = new CENTData();
        CBUSCore1 = new CBUSCore(Connection1, CENTSession1);
        CBUSCore1.SetIdTransaction(intIdTransaction);
                
        /*
         * Drop down to load the available views
         */
        ListCENTData1 = new ArrayList<CENTData>();
        CENTFilter1 = new CENTData();
        CENTFilter1.SetInt(2, intIdTransaction);        
        CBUSCore1.SetIdTransaction(Global1.TRN_VIEW);
        CBUSCore1.SetIdView(Global1.TRN_VIEW);        
        ListCENTData1 = CBUSCore1.GetList(CENTFilter1);
        
        if (ListCENTData1.isEmpty())
        {
            throw new CENTException("EXCEPTION_VIEW_NOT_FOUND");
        }

        /*
         * The first view in the list must be the main view
         */
        if (intIdView == 0)
        {
            intIdView = ListCENTData1.get(0).GetInt(Global1.SYSTEM_FIELD_ID);
        }
        
        for (CENTData CENTData1 : ListCENTData1)
        {
            strOptionView += "<option ";            
            if (CENTData1.GetInt(Global1.SYSTEM_FIELD_ID) == intIdView) 
            {    
                strOptionView += "selected";                
                strViewName = CENTData1.GetText(1);
                strViewType = CENTData1.GetText(4); // pie, bar, etc
            }    
            strOptionView += " value='" + CENTData1.GetInt(Global1.SYSTEM_FIELD_ID) + "'>" + CBUSCore1.Translate(ListCENTDictionary1, CENTData1.GetText(1)) + "</option>";
        }        
        
        
        /*
         * Translate the labels
         */
        strChartNoInfo = CBUSCore1.Translate("MESSAGE_NO_INFORMATION_TO_DISPLAY");
        strLabelView = CBUSCore1.Translate(ListCENTDictionary1, "Label.View");
        strLabelRecordCount = CBUSCore1.Translate(ListCENTDictionary1, "Label.RecordCount");
        strLabelPages = CBUSCore1.Translate(ListCENTDictionary1, "Label.Pages");
        strLabelLinePage = CBUSCore1.Translate(ListCENTDictionary1, "Label.LinePage");
        strLabelGoPages = CBUSCore1.Translate(ListCENTDictionary1, "Label.GoPage");
        
        strButtonNew = CBUSCore1.Translate(ListCENTDictionary1, "Button.New");
        strButtonEdit = CBUSCore1.Translate(ListCENTDictionary1, "Button.Edit");
        strButtonDelete = CBUSCore1.Translate(ListCENTDictionary1, "Button.Delete");
        strButtonFilter = CBUSCore1.Translate(ListCENTDictionary1, "Button.Filter");
        strButtonExport = CBUSCore1.Translate(ListCENTDictionary1, "Button.Export");
        strButtonImport = CBUSCore1.Translate(ListCENTDictionary1, "Button.Import");
        strButtonExport = CBUSCore1.Translate(ListCENTDictionary1, "Button.Export");
        strButtonReconcile = CBUSCore1.Translate(ListCENTDictionary1, "Button.Reconciliation");
        strButtonSetup = CBUSCore1.Translate(ListCENTDictionary1, "Button.Setup");
        strButtonDuplicate = CBUSCore1.Translate(ListCENTDictionary1, "Button.Duplicate");
        strButtonReprocess = CBUSCore1.Translate(ListCENTDictionary1, "Button.Reprocess");
        strButtonBack = CBUSCore1.Translate(ListCENTDictionary1, "Button.Back");
        
        strMessageSelect = CBUSCore1.Translate(ListCENTDictionary1, "MESSAGE_SELECT_AN_ITEM");
        strMessageSave = CBUSCore1.Translate(ListCENTDictionary1, "MESSAGE_CONFIRM_SAVE_RECORD");
        strMessageDelete = CBUSCore1.Translate(ListCENTDictionary1, "MESSAGE_CONFIRM_DELETE_RECORD");
        strMessageImport = CBUSCore1.Translate(ListCENTDictionary1, "MESSAGE_CONFIRM_IMPORT_RECORD");
        strMessageExport = CBUSCore1.Translate(ListCENTDictionary1, "MESSAGE_CONFIRM_EXPORT_RECORD");
        strMessageReconcile = CBUSCore1.Translate(ListCENTDictionary1, "MESSAGE_CONFIRM_RECONCILE_RECORD");   
        strMessageSetup = CBUSCore1.Translate(ListCENTDictionary1, "MESSAGE_CONFIRM_SETUP");   
        strMessageDuplicate = CBUSCore1.Translate(ListCENTDictionary1, "MESSAGE_CONFIRM_DUPLICATE");   
        strMessageReprocess = CBUSCore1.Translate(ListCENTDictionary1, "MESSAGE_CONFIRM_REPROCESS");   
        
        /*
         * Get the permissions
         */
        CBUSCore1.SetIdTransaction(intIdTransaction);
        ListCENTPermission1 = CBUSCore1.GetPermission(CENTSession1);
               
        blnPermissionNew = CBUSCore1.isPermissioned("FUNCTION_NEW", ListCENTPermission1);
        blnPermissionEdit = CBUSCore1.isPermissioned("FUNCTION_EDIT", ListCENTPermission1);        
        blnPermissionDelete = CBUSCore1.isPermissioned("FUNCTION_DELETE", ListCENTPermission1);        
        blnPermissionFilter = CBUSCore1.isPermissioned("FUNCTION_FILTER", ListCENTPermission1);        
        blnPermissionImport = CBUSCore1.isPermissioned("FUNCTION_IMPORT", ListCENTPermission1);        
        blnPermissionExport = CBUSCore1.isPermissioned("FUNCTION_EXPORT", ListCENTPermission1);        
        blnPermissionReconcile = CBUSCore1.isPermissioned("FUNCTION_RECONCILE", ListCENTPermission1);
        blnPermissionSetup = CBUSCore1.isPermissioned("FUNCTION_SETUP", ListCENTPermission1);
        blnPermissionDuplicate = CBUSCore1.isPermissioned("FUNCTION_DUPLICATE", ListCENTPermission1);        
        blnPermissionReprocess = CBUSCore1.isPermissioned("FUNCTION_REPROCESS", ListCENTPermission1);        
        
        /*
         * Get catalog for current transaction
         */
        ListCENTCatalog1 = CBUSCore1.GetCatalog(intIdTransaction);
        if (ListCENTCatalog1 == null)
        {    
            throw new CENTException("EXCEPTION_CATALOG_NOT_FOUND");
        }        

        /*
         * Get default view for current transaction
         */
        ListCENTView1 = CBUSCore1.GetView(intIdView);        
        if (ListCENTView1 == null)
        {    
            throw new CENTException("EXCEPTION_VIEW_NOT_FOUND");
        }            

        /*
         * Use the view data to generate the header
         */        
        for (CENTData CENTView1 : ListCENTView1)
        {
            if (CENTView1.GetInt(Global1.FIELD_ID_COMMAND) <= Global1.SELECTABLE_FIELD) // Discard group 
            {
                strLabel = CENTView1.GetText(Global1.FIELD_LABEL);
                strTHead += "<th scope='col'><span style='cursor:pointer'>" + CBUSCore1.Translate(ListCENTDictionary1, strLabel) + "</span></th>";
            }
        }                

        /*
         * Control the paging
         */
        intPageStart = (intPage * intPageSize) - (intPageSize-1);
        intPageEnd = (intPage * intPageSize);

        /*
         * Apply all filters
         */
        CENTFilter1 = new CENTData();
        ListCENTView2 = new ArrayList<CENTData>(ListCENTView1);
        
        for (int i=0; i<=arrParam.length-1; i++)
        {
            strParam = arrParam[i];        
            
            for (CENTData CENTView1 : ListCENTView2)
            {   
                strFieldObject = CENTView1.GetText(Global1.FIELD_OBJECT);
                strFieldName = CENTView1.GetText(Global1.FIELD_NAME);
                intFieldType = CENTView1.GetInt(Global1.FIELD_TYPE);
                intIdTrn = CENTView1.GetInt(Global1.FIELD_ID_TRN);
                
                strHtmlObjectName = strFieldName + "_" + intIdTrn;

                strValue = GetValueFromParamTag(strParam, strHtmlObjectName).trim();                

                if ((!strValue.equals("")) && (!strValue.equals("0")))
                {
                    ListCENTView1.add(CBUSCore1.FilterCriteria(intIdTrn, strFieldObject, intFieldType, Global1.OPERATOR_EQUALS, strValue));
                }
            }
        }        
        
        /*
         * Touch this point when click navigation buttons related to fks only
         */
        if (!strFieldNameNav.trim().equals(""))
        {
            intFieldType = Global1.TYPE_INT;
            strFieldObject = strFieldNameNav;
            strValue = strFieldValueNav;
            ListCENTView1.add(CBUSCore1.FilterCriteria(intIdTransaction, strFieldObject, intFieldType, Global1.OPERATOR_EQUALS, strValue));
        }    
        
        /*
         * Paging control related code
         */
        ListCENTView1.add(CBUSCore1.FilterCriteria(intIdTransaction, Global1.SYSTEM_FIELD_PAGING_START, Global1.TYPE_INT, Global1.OPERATOR_EQUALS, intPageStart));
        ListCENTView1.add(CBUSCore1.FilterCriteria(intIdTransaction, Global1.SYSTEM_FIELD_PAGING_END, Global1.TYPE_INT, Global1.OPERATOR_EQUALS, intPageEnd));

        /*
         * Query the system 
         */
        CBUSCore1.SetIdView(intIdView);
        CBUSCore1.SetIdTransaction(intIdTransaction);
        CBUSCore1.SetIdEvent(Global1.EVENT_FILTER);
        ListCENTData1 = CBUSCore1.GetList(ListCENTView1);
        
        if (!ListCENTData1.isEmpty())
        {
            intRecordCount = ListCENTData1.get(0).GetInt(Global1.SYSTEM_FIELD_RECORD_COUNT);            
        }
    
        if (strViewType.toUpperCase().equals("GRID"))
        {
            for (CENTData CENTData1 : ListCENTData1)
            {
                intCountInt = 0;
                intCountText = 0;
                intCountDate = 0;
                intCountDouble = 0;
                intCountBoolean = 0;                
                
                strTBody += "<tr class='" + TRColor(intIdControl) + "'>";              
                strTBody += "<td><input type='checkbox' id='chk_" + intRowCount + "' value='" + CENTData1.GetInt(1) + "'></td>";                

                for (CENTData CENTView1 : ListCENTView1)
                {                                    
                    strValue = "";
                    strFieldName = CENTView1.GetText(Global1.FIELD_NAME);
                    intFieldType = CENTView1.GetInt(Global1.FIELD_TYPE);
                    intIdFk = CENTView1.GetInt(Global1.FIELD_ID_FK);
                    intIdCommand = CENTView1.GetInt(Global1.FIELD_ID_COMMAND);
                    
                    if (intIdFk != 0)
                    {
                        intFieldType = Global1.TYPE_TEXT;
                    }

                    if (intIdCommand == Global1.COMMAND_SELECT_COUNT)
                    {
                        intFieldType = Global1.TYPE_DOUBLE;
                    }                    
                                                            
                    if (intIdCommand <= Global1.SELECTABLE_FIELD) // Discard group 
                    {   
                        if (intFieldType == Global1.TYPE_INT)
                        {    
                            intCountInt ++;
                            strFieldObject = "int_" + intCountInt;
                            strValue = CBUSCore1.FormatInt(CENTData1.GetInt(strFieldObject));
                            strTBody += "<td>" + strValue + "</td>";    
                        }

                        if (intFieldType == Global1.TYPE_TEXT)
                        {
                            intCountText ++;
                            strFieldObject = "text_" + intCountText;
                            strValue = CBUSCore1.FormatString(CENTData1.GetText(strFieldObject));
                            
                            if (intIdTransaction != Global1.TRN_DICTIONARY)
                            {    
                                strValue = CBUSCore1.Translate(ListCENTDictionary1, strValue);                                
                            }

                            if (intIdTransaction == Global1.TRN_FILE_MANAGER)
                            {   
                                if (strFieldName.equals("file"))
                                {
                                    strValue = "<a href='" + request.getContextPath() + "/upload/" + CENTSession1.GetInt(Global1.SESSION_COMPANY) + "/" + CENTData1.GetText(strFieldObject) + "' download>" + CENTData1.GetText(strFieldObject) + "</a>";
                                }
                            }
                            
                            strTBody += "<td>" + strValue + "</td>";
                            
                        }                        

                        if (intFieldType == Global1.TYPE_DATE)
                        {                                
                            intCountDate ++;
                            strFieldObject = "date_" + intCountDate;
                            
                            if (CENTData1.GetDate(strFieldObject) != null)
                            {
                                strValue = CBUSCore1.FormatDate(CENTData1.GetDate(strFieldObject));
                            }    
                            else
                            {
                                strValue = "";
                            }
                            
                            if (strValue.trim().equals("01/01/1900"))
                            {
                                strValue = "";
                            }

                            strTBody += "<td>" + strValue + "</td>"; 
                        }    

                        if (intFieldType == Global1.TYPE_DOUBLE)
                        {
                            intCountDouble ++;
                            strFieldObject = "double_" + intCountDouble;
                            
                            strValue = CBUSCore1.FormatNumber(CENTData1.GetDouble(strFieldObject), 4);
                            strTBody += "<td>" + strValue + "</td>"; 
                        }

                        if (intFieldType == Global1.TYPE_BOOLEAN)
                        {                                
                            intCountBoolean ++;
                            strFieldObject = "boolean_" + intCountBoolean;                            
                            strValue = CBUSCore1.FormatInt(CENTData1.GetBoolean(strFieldObject));

                            if (!strValue.trim().equals(""))
                            {                        
                                strTBody += "<td>" + strValue + "</td>";                                                
                            }
                        }
                    }
                }

                strTBody += "</tr>";                     
                intIdControl++;    
                intRowCount ++;
            }

        }
        else
        {
            strFieldObjectChartDesc = "text_1";
            strFieldObjectChartValue = "double_1";

            for (CENTData CENTData1 : ListCENTData1)
            {
                if (blnFirst == false)
                {
                    strChart += ",";
                }                
                
                if (CENTData1.GetDate("date_1") != null)
                {
                    strFieldObjectChartDesc = "date_1";
                }
                else if (CENTData1.GetInt("int_1") != Integer.MIN_VALUE)
                {
                    strFieldObjectChartDesc = "int_1";
                }
                else
                {
                    strFieldObjectChartDesc = "text_1";        
                }
                
                strChartDesc = GetValueToChart(CENTData1, strFieldObjectChartDesc);
                strChartValue = GetValueToChart(CENTData1, strFieldObjectChartValue);                
                strChart += "{ label: " + "\"" + CBUSCore1.Translate(strChartDesc) + "\"" + "," + " y: " + CBUSCore1.Translate(strChartValue) + "}";

                blnFirst = false;

            }
        }
                
        /*
         * Get the navigation buttons
         */
        CENTFilter1 = new CENTData();
        CENTFilter1.SetInt(Global1.FIELD_ID_FK, intIdTransaction);
        CBUSCore1.SetIdTransaction(Global1.TRN_CATALOG);
        CBUSCore1.SetIdView(Global1.DO_NOT_USE_VIEW);                                
        ListCENTData1 = CBUSCore1.GetList(CENTFilter1);
        
        for (CENTData CENTData2 : ListCENTData1)
        {
            /*
             * Figure out transaction name
             */
            CENTFilter1 = new CENTData();
            CENTFilter1.SetInt(Global1.SYSTEM_FIELD_ID, CENTData2.GetInt(Global1.FIELD_ID_TRN));
            CBUSCore1.SetIdTransaction(Global1.TRN_TRANSACTION);
            CBUSCore1.SetIdView(Global1.DO_NOT_USE_VIEW);
            ListCENTData2 = CBUSCore1.GetList(CENTFilter1);            

            for (CENTData CENTData3 : ListCENTData2)
            {                
                /*
                 * Figure out default view
                 */
                CENTFilter1 = new CENTData();
                CENTFilter1.SetInt("int_2", CENTData3.GetInt(Global1.SYSTEM_FIELD_ID)); // id_transaction for view
                CBUSCore1.SetIdTransaction(Global1.TRN_VIEW);
                CBUSCore1.SetIdView(Global1.DO_NOT_USE_VIEW);
                ListCENTData3 = CBUSCore1.GetList(CENTFilter1);
                
                for (CENTData CENTData4 : ListCENTData3)
                {
                    strNavigationButton += "<button type='button' id='btn" + CENTData2.GetText(Global1.FIELD_OBJECT) + "' OnClick=" + "\"" + "JavaScript:Navigate(PAGE_NAME, " + CENTData3.GetInt(Global1.SYSTEM_FIELD_ID) + ",'" + CBUSCore1.Translate(CENTData3.GetText(1)) + "','" + CENTData2.GetText(Global1.FIELD_OBJECT) + "', " + CENTData4.GetInt(Global1.SYSTEM_FIELD_ID) + ")" + "\"" + " class='" + strButtonStyle + "'>" + CBUSCore1.Translate(CENTData3.GetText(1)) + " </button>&nbsp;";
                    break;
                }                                
            }
        }
        
    }    
    catch (CENTException CENTException1)
    {
        strUrl += "../common/exception.jsp";
        strUrl += "?source=" + "main_list.jsp" ;
        strUrl += "&code=" + CENTException1.GetCode();
        strUrl += "&message=" + CENTException1.GetMessage();
        
        response.sendRedirect(strUrl);        
    }    
    catch (Exception Exception1)
    {
        strUrl += "../common/exception.jsp";
        strUrl += "?source=" + "main_list.jsp" ;
        strUrl += "&code=";
        strUrl += "&message=" + Exception1.getMessage();
        
        response.sendRedirect(strUrl);
    }
    finally
    {
        CBUSCore1 = null;        
        ListCENTData1 = null;
        ListCENTData2 = null;
        ListCENTData3 = null;
        ListCENTCatalog1 = null;
        ListCENTView1 = null;
        Connection1 = null;
        ConnectionFactory1 = null;        
    }        
    
%>


<div class="site-margin">    
    
    <form name="frmDefault">
    <input type="hidden" name="txtRecordCount" value="<%out.print(intRowCount);%>">
    <input type="hidden" id="txtMessageSelect" value="<%out.print(strMessageSelect);%>">
    <input type="hidden" id="txtMessageSave" value="<%out.print(strMessageSave);%>">
    <input type="hidden" id="txtMessageDelete" value="<%out.print(strMessageDelete);%>">
    <input type="hidden" id="txtMessageImport" value="<%out.print(strMessageImport);%>">
    <input type="hidden" id="txtMessageExport" value="<%out.print(strMessageExport);%>">
    <input type="hidden" id="txtMessageReconcile" value="<%out.print(strMessageReconcile);%>">    
    <input type="hidden" id="txtMessageMapView" value="<%out.print(strMessageSetup);%>">    
    <input type="hidden" id="txtMessageDuplicate" value="<%out.print(strMessageDuplicate);%>">    
    <input type="hidden" id="txtMessageReprocess" value="<%out.print(strMessageReprocess);%>">    
        
    <div id="table2" class="w3-responsive">            
        <table>
            <tr>
                <td align="left">                                        
                    <%
                        out.print(SetTitle(1, strNameTransaction, "", ""));                       
                    %>                    
                </td>
                
                <td align="left">                                        
                    <%
                        if (!strParam.trim().equals(""))
                        {
                            out.print("&nbsp;&nbsp;<span style='cursor:pointer'><img alt='' src='../../images/no_filter.jpg' width='25%' onClick='JavaScript:ClearFilter()' alt='Limpar filtro'></span>");
                        }
                    %>                    
                </td>                
                
            </tr>    
        </table>    
                
    </div>

    <%
    if (strViewType.toUpperCase().equals("GRID"))
    {
        intTotalPage = (intRecordCount / intPageSize);
        intDiff = (intRecordCount % intPageSize);

        if (intDiff > 0)
        {
            intTotalPage = 1 + intTotalPage;
        }        
        
    %>           
                
    <%            
    if (strViewType.toUpperCase().equals("GRID"))  
    {                        
    %>            
        
    <div class="w3-responsive">
        <table id="table1" class="w3-table-all w3-hoverable">
            <thead>
                <tr class="w3-light-grey">
                    <th><input type="checkbox" id="chk_all" onclick="JavaScript:MarkAllAsCheckOrUnCheck()"></th>                    
                    <%
                    out.print(strTHead);
                    %>                    
                </tr>
            </thead>  
            
            <tbody>
                <%
                out.print(strTBody);
                %>
            </tbody>              
            
        </table> 

        <table id="table1" class="w3-table-all w3-hoverable">    
            <tr>
                <td align="right">
                    <%out.print(strLabelRecordCount);%>: <%out.println(intRecordCount);%>                     
                </td>

                <td align="right">
                    <%out.print(strLabelPages);%>: <%out.println(intTotalPage);%>                     
                </td>
                
                <td align="right">
                    <%out.print(strLabelGoPages);%>:
                    &nbsp;<input type="text" id="txtPage" size="3" value="<%out.print(intPage);%>" size="1" onBlur="JavaScript:Paging()">                     
                </td>                
                
                <td align="right">
                    <%out.print(strLabelLinePage);%>:
                    <input type="text" size="3" id="txtPageSize" name="txtPageSize" value="<%out.println(intPageSize);%>" onBlur="JavaScript:ChangePageSize(this.value)">                            
                </td>                

            </tr>

        </table>     

    </div>

    <%            
    }
    else
    {                        
    %>        
        
    <div class="site-chart">    
        <div id="chartContainer" style="height: 300px; width: 100%;"></div>
    </div>        
        
    <%            
    }
    %>          
        
    <br>   
    <br>
    
    <%            
    if (strViewType.toUpperCase().equals("GRID"))
    {
    %>
    
        <%if (blnPermissionNew == true) {%><button type="button" name="btnNew" OnClick="JavaScript:New(PAGE_NAME, 'txtDescription')" class="<%out.print(strButtonStyle);%>"><%out.print(strButtonNew);%> </button><%}%>        
        <%if (blnPermissionEdit == true) {%><button type="button" name="btnEdit" OnClick="JavaScript:Edit(PAGE_NAME)" class="<%out.print(strButtonStyle);%>"><%out.print(strButtonEdit);%></button><%}%>
        <%if (blnPermissionDelete == true) {%><button type="button" name="btnDelete" OnClick="JavaScript:Delete(PAGE_NAME)" class="<%out.print(strButtonStyle);%>"><%out.print(strButtonDelete);%></button><%}%>
        <%if (blnPermissionFilter == true) {%><button type="button" name="btnFilter" OnClick="JavaScript:FilterForm(PAGE_NAME)" class="<%out.print(strButtonStyle);%>"><%out.print(strButtonFilter);%></button><%}%>        
        <%if (blnPermissionDuplicate == true) {%><button type="button" name="btnDuplicate" OnClick="JavaScript:Duplicate(PAGE_NAME)" class="<%out.print(strButtonStyle);%>"><%out.print(strButtonDuplicate);%> </button><%}%>    
        <%if (blnPermissionImport == true) {%><button type="button" name="btnImport" OnClick="JavaScript:Import(PAGE_NAME)" class="<%out.print(strButtonStyle);%>"><%out.print(strButtonImport);%> </button><%}%>
        <%if (blnPermissionExport == true) {%><button type="button" name="btnExport" OnClick="JavaScript:Export(PAGE_NAME)" class="<%out.print(strButtonStyle);%>"><%out.print(strButtonExport);%> </button><%}%>
        <%if (blnPermissionReconcile == true) {%><button type="button" name="btnReconcile" OnClick="Reconcile(PAGE_NAME)" class="<%out.print(strButtonStyle);%>"><%out.print(strButtonReconcile);%> </button><%}%>            
        <%if (blnPermissionSetup == true) {%><button type="button" name="btnSetup" OnClick="JavaScript:Setup(PAGE_NAME)" class="<%out.print(strButtonStyle);%>"><%out.print(strButtonSetup);%> </button><%}%>        
        <%if (blnPermissionReprocess == true) {%><button type="button" name="btnReprocess" OnClick="JavaScript:Reprocess(PAGE_NAME)" class="<%out.print(strButtonStyle);%>"><%out.print(strButtonReprocess);%> </button><%}%>
        
        <%
            if (intIdTransaction == Global1.TRN_TRANSACTION)
            {
                out.println("</br></br>");
            }
        %>
        
        <%
        out.println(strNavigationButton);
        %>

        <%
            if (intIdTransaction == Global1.TRN_TRANSACTION)
            {
                out.println("</br></br>");
            }
        %>
        
        <button type="button" name="btnBack" OnClick="JavaScript:history.go(-1);" class="<%out.print(strButtonStyle);%>"><%out.print(strButtonBack);%> </button>

    <%            
    }
    %>               

      &nbsp;  
      <div class="w3-dropdown-hover">
            <%//out.print(strLabelView);%>
            <select class="w3-select" id="cboIdView" name="cboIdView" onchange="JavaScript:ChangeViewSelection();">
              <%out.print(strOptionView);%>        
            </select>         
      </div>    

    <%
    }        
    else
    {
    %>
        <!-- data here -->
    <%    
    }
    %>            

    
</div>                
</form>    

<%@include file="../common/footer.jsp"%>        
    
<script type="text/javascript">    

    var PAGE_NAME = 'main'; // must come from transaction
    
    function ChangeViewSelection()
    {
        document.frmDefault.method = "Post";
        document.frmDefault.action = PAGE_NAME + "_list.jsp?ViewChanged=1";
        document.frmDefault.submit();
    }   

    function ChangePageSize(size)
    {
        if (isNaN(size))
        {
            size = 0;
        }
        
        document.frmDefault.method = "Post";
        document.frmDefault.action = PAGE_NAME + "_list.jsp?txtPageSize=" + size;
        document.frmDefault.submit();
    }  

    function Paging()
    {                
        var page_number = parseInt(document.getElementById("txtPage").value);

        document.frmDefault.method = "Post";
        document.frmDefault.action = "main_list.jsp?txtPageNumber=" + page_number;
        document.frmDefault.submit();
    }  

    function ClearFilter()
    {                
        document.frmDefault.method = "Post";
        document.frmDefault.action = "main_list.jsp?Param=";
        document.frmDefault.submit();
    }

    /*
     * Sortable plug in
     */        
    $(document).ready(function() 
    { 
        $("#table1").tablesorter(); 
    } 
    ); 	    

    /*
     * Read the form
     */    
    function GetData(intEvent, intId)
    {
        var strParam = "";

        strParam += "[datas]";
        
            strParam += "[data]";
                strParam += "[view]" + document.getElementById("txtPageSize").value + "[/view]";
                strParam += "[view]" + document.getElementById("cboIdView").value + "[/view]";
                strParam += "[event]" + intEvent + "[/event]";
                strParam += "[id]" + intId + "[/id]";
            strParam += "[/data]";
            
        strParam += "[/datas]";

        return strParam;
    }    

</script>

<%            
if (!strViewType.toUpperCase().equals("GRID"))  
{
    if (!strChart.trim().equals(""))
    {
%>                
    <script type="text/javascript">
    window.onload = function () 
    {
        var chart = new CanvasJS.Chart("chartContainer",
        {
            animationEnabled: true,
            title:
            {
                text: "<%out.print(strViewName);%>"
            },
            data: 
            [
            {
                type: "<%out.print(strViewType);%>", //change type to bar, line, area, pie, etc
                dataPoints: 
                [
                    <%
                        out.println(strChart);
                    %>
                ]
            }
            ]
        });

        chart.render();
    }
    </script>        
<%
    }
    else
    {
        out.println(strChartNoInfo);
    }

}                       
%>     

<%
    strFieldNameNav = null;
    strFieldValueNav = null;    
    
    strFieldName = null;
    strFieldValue = null;    
    strNavigationButton = null;    
    strUrl = null;
    strParam = null;
    arrParam = null;
    strTHead = null;
    strTBody = null;
    strValue = null;    
    strLabel = null;
    strFieldObject = null;
    strOptionView = null;
    strViewName = null;
    strViewType = null;
    strChart = null;
    strChartDesc = null;
    strChartValue = null;                
    strFieldObjectChartDesc = null;
    strFieldObjectChartValue = null;
    strHtmlObjectName = null;
    strChartNoInfo = null;    
    
    strLabelView = null;
    strLabelRecordCount = null;
    strLabelPages = null;
    strLabelLinePage = null;
    strLabelGoPages = null;    
    
    strButtonNew = null;
    strButtonEdit = null;
    strButtonDelete = null;
    strButtonFilter = null;
    strButtonImport = null;
    strButtonExport = null;
    strButtonReconcile = null;
    strButtonSetup = null;
    strButtonDuplicate = null;
    strButtonReprocess = null;
    
    strMessageSelect = null;
    strMessageSave = null;
    strMessageDelete = null;    
    strMessageImport = null;
    strMessageExport = null;
    strMessageReconcile = null;        
    strMessageSetup = null;        
    strMessageDuplicate = null;        
    strMessageReprocess = null;
%>

</body>
</html>
