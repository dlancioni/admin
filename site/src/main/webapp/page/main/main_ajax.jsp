
<%@include file="../common/declare.jsp"%>  

<%
    /*
     * General Declaration
     */           
    int intId = 0;
    int intIdEvent = 0;
    int intFieldType = 0;    
    int intIdView = 0;
    int intIdLayout = 0;
    
    String strParam = "";
    String strMessage = "";
    String strValue = "";    
    String strFieldName = "";
    String strFieldObject = "";

    CBUSCore CBUSCore1 = null;
    CENTData CENTData1 = null;
    List<CENTData> ListCENTCatalog1 = null;
    List<CENTData> ListCENTData1 = null;
    
    /*
     * Receive the parameters
     */
    if (request.getParameter("Param") != null)
    {    
        strParam = request.getParameter("Param");
    }

    String arrParam[] = strParam.split("/lb/");
    
    try
    {        
        /*
         * Create the Objects
         */        
        CENTData1 = new CENTData();        
        ListCENTData1 = new ArrayList<CENTData>();
        CBUSCore1 = new CBUSCore(Connection1, CENTSession1);

        /*
         * Get the catalog
         */        
        ListCENTCatalog1 = CBUSCore1.GetCatalog(intIdTransaction);
        if (ListCENTCatalog1 == null)
        {    
            throw new CENTException("EXCEPTION_CATALOG_NOT_FOUND");
        }        
        
        /*
         * Keep the action
         */    
        for (int i=0; i<=arrParam.length-1; i++)
        {
            strParam = arrParam[i];
            
            /*
             * Get the action and the key
             */            
            if (!GetValueFromParamTag(strParam, "view").equals(""))
            {
                intIdView = CBUSCore1.StringToInteger(GetValueFromParamTag(strParam, "view"));
            }
            
            if (!GetValueFromParamTag(strParam, "layout").equals(""))
            {
                intIdLayout = CBUSCore1.StringToInteger(GetValueFromParamTag(strParam, "layout"));
            }            
            
            if (!GetValueFromParamTag(strParam, "event").equals(""))                    
            {
                intIdEvent = CBUSCore1.StringToInteger(GetValueFromParamTag(strParam, "event"));
            }
            
            /*
             * For list (Filter) sometimes we have joins, fields must be unique
             */
            if (intIdEvent == Global1.EVENT_FILTER)
            {
                if (!GetValueFromParamTag(strParam, "id" + "_" + intIdTransaction).equals(""))
                {
                    intId = CBUSCore1.StringToInteger(GetValueFromParamTag(strParam, "id" + "_" + intIdTransaction));
                    CENTData1 = new CENTData();
                    CENTData1.SetInt(Global1.SYSTEM_FIELD_ID, intId);
                }
            }    
            else
            {
                if (!GetValueFromParamTag(strParam, "id").equals(""))
                {
                    intId = CBUSCore1.StringToInteger(GetValueFromParamTag(strParam, "id"));
                    CENTData1 = new CENTData();
                    CENTData1.SetInt(Global1.SYSTEM_FIELD_ID, intId);
                }
            }
            
            /*
             * Persist according to the event
             */            
            if (intIdEvent == Global1.EVENT_INSERT || intIdEvent == Global1.EVENT_UPDATE || intIdEvent == Global1.EVENT_FILTER)
            {
                /*
                 * If the key is found, switch from insert up update
                 */
                if (intIdEvent == Global1.EVENT_INSERT && intId != 0)
                {    
                    intIdEvent = Global1.EVENT_UPDATE;
                }

                /*
                 * Read the fields
                 */                
                for (CENTData CENTCatalog1 : ListCENTCatalog1)
                {                    
                    
                    if (intIdEvent == Global1.EVENT_FILTER)
                        strFieldName = CENTCatalog1.GetText(Global1.FIELD_NAME) + "_" + intIdTransaction;
                    else
                        strFieldName = CENTCatalog1.GetText(Global1.FIELD_NAME);
                    
                    strFieldObject = CENTCatalog1.GetText(Global1.FIELD_OBJECT);
                    intFieldType = CENTCatalog1.GetInt(Global1.FIELD_TYPE);

                    /*
                     * Integer values, in this case handles the ID that is considered a special field
                     */
                    if (intFieldType == Global1.TYPE_INT)
                    {    
                        strValue = GetValueFromParamTag(strParam, strFieldName).trim();

                        if (strValue.equals(""))
                        {
                            strValue = "0";
                        }
                        
                        CENTData1.SetInt(strFieldObject, CBUSCore1.StringToInteger(strValue));
                    }
                    
                    /*
                     * Text values, no special rule
                     */
                    if (intFieldType == Global1.TYPE_TEXT)
                    {                        
                        strValue = GetValueFromParamTag(strParam, strFieldName).trim();                        
                        
                        if (!strValue.equals(""))
                        {
                            CENTData1.SetText(strFieldObject, CBUSCore1.StringToString(strValue).trim());
                        }
                    }                        

                    /*
                     * Date values, in this case the string is converted to date
                     */
                    if (intFieldType == Global1.TYPE_DATE)
                    {    
                        strValue = GetValueFromParamTag(strParam, strFieldName).trim();
                        
                        if (!strValue.equals(""))
                        {
                            CENTData1.SetDate(strFieldObject, CBUSCore1.StringToDate(strValue));
                        }
                    }    

                    /*
                     * Double values, in this case the string is converted to double
                     */                    
                    if (intFieldType == Global1.TYPE_DOUBLE)
                    {    
                        strValue = GetValueFromParamTag(strParam, strFieldName).trim();                        
                        
                        if (strValue.equals(""))
                        {
                            strValue = "0";
                        }
                        
                        CENTData1.SetDouble(strFieldObject, CBUSCore1.StringToDouble(strValue));                        
                    }                    
                    
                    /*
                     * Boolean values, must double check
                     */
                    if (intFieldType == Global1.TYPE_BOOLEAN)
                    {    
                        strValue = GetValueFromParamTag(strParam, strFieldName).trim();                        
                        
                        if (!strValue.equals(""))
                        {
                            CENTData1.SetBoolean(strFieldObject, CBUSCore1.StringToInteger(strValue));
                        }
                    }
                }
            }
            
            ListCENTData1.add(CENTData1);
        }    
        
        /*
         * Insert new record
         */
        if (intIdEvent == Global1.EVENT_INSERT)
        {    
            CBUSCore1.SetIdTransaction(intIdTransaction);
            CBUSCore1.SetIdView(Global1.DO_NOT_USE_VIEW);
            CBUSCore1.ExecuteEvent(Global1.EVENT_INSERT, ListCENTData1);
            strMessage = CBUSCore1.Translate("MESSAGE_RECORD_INSERT_SUCCESS");
        }

        /*
         * Update current record
         */        
        if (intIdEvent == Global1.EVENT_UPDATE)
        {    
            CBUSCore1.SetIdTransaction(intIdTransaction);
            CBUSCore1.SetIdView(Global1.DO_NOT_USE_VIEW);
            CBUSCore1.ExecuteEvent(Global1.EVENT_UPDATE, ListCENTData1);
            strMessage = CBUSCore1.Translate("MESSAGE_RECORD_UPDATE_SUCCESS");
        }
        
        /*
         * Delete current record
         */        
        if (intIdEvent == Global1.EVENT_DELETE)
        {    
            CBUSCore1.SetIdTransaction(intIdTransaction);
            CBUSCore1.SetIdView(Global1.DO_NOT_USE_VIEW);
            CBUSCore1.ExecuteEvent(Global1.EVENT_DELETE, ListCENTData1);
            strMessage = CBUSCore1.Translate("MESSAGE_RECORD_DELETE_SUCCESS");
        }        

        /*
         * Import data from files
         */
        if (intIdEvent == Global1.EVENT_IMPORT)
        {
            CBUSCore1.SetIdTransaction(intIdTransaction);
            CBUSCore1.SetIdView(Global1.DO_NOT_USE_VIEW);
            CBUSCore1.ExecuteEvent(intIdEvent, ListCENTData1);
            strMessage = CBUSCore1.Translate("MESSAGE_FILE_IMPORT_SCHEDULED_SUCCESS");            
        }

        if (intIdEvent == Global1.EVENT_EXPORT)
        {
            CBUSCore1.SetIdTransaction(intIdTransaction);
            CBUSCore1.SetIdView(Global1.DO_NOT_USE_VIEW);
            CBUSCore1.ExecuteEvent(intIdEvent, ListCENTData1);
            strMessage = CBUSCore1.Translate("MESSAGE_FILE_EXPORT_SCHEDULED_SUCCESS");            
        }

        /*
         * Execute selected reconciliation
         */
        if (intIdEvent == Global1.EVENT_RECONCILE)
        {
            CENTData1.Clear();
            CENTData1.SetInt(1, intId);
            CBUSCore1.SetIdTransaction(intIdTransaction);
            CBUSCore1.SetIdView(Global1.DO_NOT_USE_VIEW);
            CBUSCore1.ExecuteEvent(intIdEvent, ListCENTData1);
            
            strMessage = CBUSCore1.Translate("MESSAGE_RECONCILE_SCHEDULED_SUCCESS");
        }
        
        /*
         * Map selected view
         */
        if (intIdEvent == Global1.EVENT_SETUP)
        {
            CENTData1.Clear();
            CENTData1.SetInt(1, intId);
            CBUSCore1.SetIdTransaction(intIdTransaction);
            CBUSCore1.SetIdView(Global1.DO_NOT_USE_VIEW);
            CBUSCore1.ExecuteEvent(intIdEvent, ListCENTData1);
            
            strMessage = CBUSCore1.Translate("MESSAGE_SETUP_SUCCESS");
        }        

        /*
         * Copy selected view
         */        
        if (intIdEvent == Global1.EVENT_DUPLICATE)        
        {
            CENTData1.Clear();
            CENTData1.SetInt(1, intId);
            CBUSCore1.SetIdTransaction(intIdTransaction);
            CBUSCore1.SetIdView(Global1.DO_NOT_USE_VIEW);
            CBUSCore1.ExecuteEvent(intIdEvent, ListCENTData1);
            
            strMessage = CBUSCore1.Translate("MESSAGE_DUPLICATE_SUCCESS");
        }

        /*
         * Copy selected reconciliation
         */        
        if (intIdEvent == Global1.EVENT_REPROCESS)        
        {
            CENTData1.Clear();
            CENTData1.SetInt(1, intId);
            CBUSCore1.SetIdTransaction(intIdTransaction);
            CBUSCore1.SetIdView(Global1.DO_NOT_USE_VIEW);
            CBUSCore1.ExecuteEvent(intIdEvent, ListCENTData1);
            
            strMessage = CBUSCore1.Translate("MESSAGE_REPROCESS_SUCCESS");
        }        
        
        /*
         * Return the status
         */
        out.print(MessageSuccess(strMessage));

    }
    catch (CENTException CENTException1)
    {
        CENTException1.SetCode(CBUSCore1.Translate(CENTException1.GetCode()));
        CENTException1.SetMessage(CBUSCore1.Translate(CENTException1.GetMessage()));
        out.print(MessageException(CENTException1));
    }    
    catch (Exception Exception1)
    {
        out.print(MessageException(Exception1));
    }
    finally
    {
        CBUSCore1 = null;
        CENTData1 = null;
        ListCENTCatalog1 = null;
        ListCENTData1 = null;
        Connection1 = null;
        ConnectionFactory1 = null;        
    }    
%>