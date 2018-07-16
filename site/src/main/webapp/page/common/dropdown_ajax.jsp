<%@include file="../common/declare.jsp"%>  

<%
    
    /*
     * General Declaration
     */
    int intId = 0;
    int intIdTrn = 0;
    
    String strText = "";
    String strValue = "";
    
    CENTData CENTFilter1 = null;
    CBUSCore CBUSCore1 = null;
    List<CENTData> ListCENTData1 = null;
    
    if (request.getParameter("id_trn") != null)
    {    
        intIdTrn = Integer.parseInt(request.getParameter("id_trn"));
    }

    if (request.getParameter("id") != null)
    {    
        intId = Integer.parseInt(request.getParameter("id"));
    }
    
    try
    {
        /*
         * Create the Objects
         */        
        CENTFilter1 = new CENTData();        
        CBUSCore1 = new CBUSCore(Connection1, CENTSession1);        
        
        /*
         * Call the methods to return the values
         */                
        if (intIdTrn == Global1.TRN_RECONCILIATION_STEP_RULE)
        {
            CENTFilter1.SetInt(3, intId);
        }
        else
        {
            CENTFilter1.SetInt(2, intId);            
        }
        
        CBUSCore1.SetIdTransaction(intIdTrn);
        CBUSCore1.SetIdView(-1);
        ListCENTData1 = CBUSCore1.GetList(CENTFilter1);        
        
        /*
         * Prepare the list to return
         */
        strText += "0|" + CBUSCore1.Translate("Label.Select") + ";";
        for (CENTData CENTData1 : ListCENTData1)
        {
            strValue = CBUSCore1.Translate(ListCENTDictionary1, CENTData1.GetText(1).trim());           
            strText += CENTData1.GetInt(Global1.SYSTEM_FIELD_ID) + "|" + strValue + ";";
        }        
        
        out.print(strText);
    }
    catch (Exception Exception1)
    {
        strText += "-1," + Exception1.getMessage() + ";";
        out.print(strText);
    }
    finally
    {
        CENTFilter1 = null;
        CBUSCore1 = null;
        ListCENTData1 = null;
        Connection1 = null;
        ConnectionFactory1 = null;        
    }

%>
