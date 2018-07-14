    /*
     * General Declaration
     */
    var C_EVENT_LOAD = 1;
    var C_EVENT_RELOAD = 2;
    
    var C_EVENT_NEW = 3;
    var C_EVENT_EDIT = 4;
    var C_EVENT_INSERT = 5;
    var C_EVENT_UPDATE = 6;
    var C_EVENT_DELETE = 7;
    var C_EVENT_FILTER = 8;    
    var C_EVENT_IMPORT = 9;
    var C_EVENT_EXPORT = 10;
    var C_EVENT_RECONCILE = 11;
    var C_EVENT_SETUP = 12;
    var C_EVENT_DUPLICATE = 13;
    var C_EVENT_REPROCESS = 14;
    var C_EVENT_LOGIN = 15;    
    
    function Reload(Page)
    {
        var strParam = "";
        var strUrl = "";

        strParam = GetData(C_EVENT_RELOAD, 0) + ";"; 
        strUrl = Page + "_list.jsp";
        
        document.frmDefault.method = 'Post';
        document.frmDefault.action = strUrl;
        document.frmDefault.submit();        

    }
    
    function BackToGrid(Page)
    {
        var strUrl = "";

        strUrl = Page + "_list.jsp";
        
        document.frmDefault.method = 'Post';
        document.frmDefault.action = strUrl;
        document.frmDefault.submit();        

    }    
        
    function FormLoad(Page)
    {
        var strParam = "";
        var strUrl = "";

        strParam = "[Generic][Action]" + C_EVENT_NEW + "[/Action][Id]0[/Id][/Generic]" + ";"; 
        strUrl = Page + "_ajax.jsp?Param=" + strParam;
        
        ajaxFunctionAsync(strUrl, 'div_field');
    }    

    function Back(Page)
    {        
        strUrl = Page + "_list.jsp";

        document.frmDefault.method = 'Post';
        document.frmDefault.action = strUrl;
        document.frmDefault.submit();
    }

    function New(Page)
    {        
        strParam = GetData(C_EVENT_NEW, 0);
        strUrl = Page + "_form.jsp?Param=" + strParam;

        document.frmDefault.method = 'Post';
        document.frmDefault.action = strUrl;
        document.frmDefault.submit();
    }

    function Edit(Page)
    {          
        var i = 0;
        var strParam = "";
        var strUrl = "";
        var checkbox_selected = null;
        var intRecordCount = 0;

        if (this.IsAnyItemSelected() == false)
        {
            var strMessage = "";
            strMessage = document.getElementById("txtMessageSelect").value;
            alert(strMessage);
            return;
        }

        intRecordCount = document.frmDefault.txtRecordCount.value-1;        
        for (i=1; i<=intRecordCount; i++)
        {
            checkbox_selected = document.getElementById("chk_" + i);
            if (checkbox_selected != null)
            {
                if (checkbox_selected.checked)
                {    
                    strParam = GetData(C_EVENT_EDIT, checkbox_selected.value);
                    strUrl = Page + "_form.jsp?Param=" + strParam;

                    document.frmDefault.method = 'Post';
                    document.frmDefault.action = strUrl;
                    document.frmDefault.submit();

                    break;
                }    
            }
        }
    }

    function Delete(Page)
    {                
        var i = 0;
        var strParam = "";
        var strUrl = "";
        var checkbox_selected = null;
        var intRecordCount = 0;

        if (this.IsAnyItemSelected() == false)
        {
            var strMessage = "";
            strMessage = document.getElementById("txtMessageSelect").value;
            alert(strMessage);
            return;
        }               

        var blnRet = confirm(document.frmDefault.txtMessageDelete.value);
        if (blnRet == true) 
        {                  
            strParam = "";
            intRecordCount = document.frmDefault.txtRecordCount.value-1;

            for (i=1; i<=intRecordCount; i++)
            {
                checkbox_selected = document.getElementById("chk_" + i);
                
                if (checkbox_selected != null)
                {                    
                    if (checkbox_selected.checked)
                    {    
                        if (checkbox_selected.checked)
                        {
                            strParam += GetData(C_EVENT_DELETE, checkbox_selected.value) + "/lb/";
                        }    
                    }
                }
            }

            strUrl = Page + "_ajax.jsp?Param=" + strParam;            
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
                alert(GetValueFromParamTag(strParam, "Message"));
                BackToGrid(PAGE_NAME);
            }
        }
    }

    function Save(Page)
    {        
        var strParam = "";
        var strField = "";        
        var blnRet = confirm(document.frmDefault.txtMessageSave.value);
    
        if (blnRet == true) 
        {
            strParam = GetData(C_EVENT_INSERT);
            strUrl = Page + "_ajax.jsp?Param=" + strParam;

            /*
             * When analysing error here, remember that the ajax page contains erros and cannot be compiled
             */
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
                alert(GetValueFromParamTag(strParam, "Message"));
                BackToGrid(PAGE_NAME);
            }
        }        
    }

    function FilterForm(Page)
    {
        strParam = GetData(C_EVENT_FILTER, 0);
        strUrl = Page + "_form.jsp?Param=" + strParam;
        document.frmDefault.method = 'Post';
        document.frmDefault.action = strUrl;
        document.frmDefault.submit();
    }
    
    function FilterList(Page)
    {   
        strParam = GetData(C_EVENT_FILTER);
        strUrl = Page + "_list.jsp?Param=" + strParam;

        document.frmDefault.method = 'Post';
        document.frmDefault.action = strUrl;
        document.frmDefault.submit();

    }
    
    function Import(Page)
    {                
        var i = 0;
        var strParam = "";
        var strUrl = "";
        var checkbox_selected = null;
        var intRecordCount = 0;
        var strMessage = "";

        if (this.IsAnyItemSelected() == false)
        {
            strMessage = document.getElementById("txtMessageSelect").value;
            alert(strMessage);
            return;
        }        
        
        var blnRet = confirm(document.frmDefault.txtMessageImport.value);
        if (blnRet == true) 
        {                                                      
            strParam = "";
            intRecordCount = document.frmDefault.txtRecordCount.value-1;
                    
            for (i=1; i<=intRecordCount; i++)
            {
                checkbox_selected = document.getElementById("chk_" + i);
                
                if (checkbox_selected != null)
                {
                    if (checkbox_selected.checked)
                    {                                    
                        strParam += GetData(C_EVENT_IMPORT, checkbox_selected.value) + "/lb/";
                    }
                }
            }

            strUrl = Page + "_ajax.jsp?Param=" + strParam;            
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
                alert(GetValueFromParamTag(strParam, "Message"));
                BackToGrid(PAGE_NAME);
            }
        }
    }
    
    function Export(Page)
    {                
        var i = 0;
        var strParam = "";
        var strUrl = "";

        var blnRet = confirm(document.frmDefault.txtMessageExport.value);

        if (blnRet == true) 
        {         
            strParam += GetData(C_EVENT_EXPORT, 0) + "/lb/";
            strUrl = Page + "_ajax.jsp?Param=" + strParam;
            
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
                alert(GetValueFromParamTag(strParam, "Message"));
                BackToGrid(PAGE_NAME);
            }
        }
    }
    
    function Reconcile(Page)
    {                
        var i = 0;
        var strParam = "";
        var strUrl = "";
        var checkbox_selected = null;
        var intRecordCount = 0;

        if (this.IsAnyItemSelected() == false)
        {
            var strMessage = "";
            strMessage = document.getElementById("txtMessageSelect").value;
            alert(strMessage);
            return;
        }        

        var blnRet = confirm(document.frmDefault.txtMessageReconcile.value);
        if (blnRet == true) 
        {                  
            strParam = "";
            intRecordCount = document.frmDefault.txtRecordCount.value-1;
            for (i=1; i<=intRecordCount; i++)
            {
                checkbox_selected = document.getElementById("chk_" + i);
                if (checkbox_selected.checked)
                {                    
                    if (checkbox_selected.checked)
                    {
                        strParam += GetData(C_EVENT_RECONCILE, checkbox_selected.value) + "/lb/";
                    }    
                }
            }            

            strUrl = Page + "_ajax.jsp?Param=" + strParam;            
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
                alert(GetValueFromParamTag(strParam, "Message"));
                BackToGrid(PAGE_NAME);
            }
        }
    }    

    function Setup(Page)
    {                
        var i = 0;
        var strParam = "";
        var strUrl = "";
        var checkbox_selected = null;
        var intRecordCount = 0;

        if (this.IsAnyItemSelected() == false)
        {
            var strMessage = "";
            strMessage = document.getElementById("txtMessageSelect").value;
            alert(strMessage);
            return;
        }        

        var blnRet = confirm(document.frmDefault.txtMessageMapView.value);
        if (blnRet == true) 
        {                  
            strParam = "";
            intRecordCount = document.frmDefault.txtRecordCount.value-1;
            for (i=1; i<=intRecordCount; i++)
            {
                checkbox_selected = document.getElementById("chk_" + i);
                if (checkbox_selected.checked)
                {                    
                    if (checkbox_selected.checked)
                    {
                        strParam += GetData(C_EVENT_SETUP, checkbox_selected.value) + "/lb/";
                    }    
                }
            }            

            strUrl = Page + "_ajax.jsp?Param=" + strParam;            
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
                alert(GetValueFromParamTag(strParam, "Message"));
                BackToGrid(PAGE_NAME);
            }
        }
    }        
    
    function Duplicate(Page)
    {                
        var i = 0;
        var strParam = "";
        var strUrl = "";
        var checkbox_selected = null;
        var intRecordCount = 0;

        if (this.IsAnyItemSelected() == false)
        {
            var strMessage = "";
            strMessage = document.getElementById("txtMessageSelect").value;
            alert(strMessage);
            return;
        }        

        var blnRet = confirm(document.frmDefault.txtMessageDuplicate.value);
        if (blnRet == true) 
        {                  
            strParam = "";
            intRecordCount = document.frmDefault.txtRecordCount.value-1;
            for (i=1; i<=intRecordCount; i++)
            {
                checkbox_selected = document.getElementById("chk_" + i);
                if (checkbox_selected.checked)
                {                    
                    if (checkbox_selected.checked)
                    {
                        strParam += GetData(C_EVENT_DUPLICATE, checkbox_selected.value) + "/lb/";
                    }    
                }
            }            

            strUrl = Page + "_ajax.jsp?Param=" + strParam;            
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
                alert(GetValueFromParamTag(strParam, "Message"));
                BackToGrid(PAGE_NAME);
            }
        }
    }
    
    function Reprocess(Page)
    {                
        var i = 0;
        var strParam = "";
        var strUrl = "";
        var checkbox_selected = null;
        var intRecordCount = 0;

        if (this.IsAnyItemSelected() == false)
        {
            var strMessage = "";
            strMessage = document.getElementById("txtMessageSelect").value;
            alert(strMessage);
            return;
        }        

        var blnRet = confirm(document.frmDefault.txtMessageReprocess.value);
        if (blnRet == true) 
        {                  
            strParam = "";
            intRecordCount = document.frmDefault.txtRecordCount.value-1;
            for (i=1; i<=intRecordCount; i++)
            {
                checkbox_selected = document.getElementById("chk_" + i);
                if (checkbox_selected.checked)
                {                    
                    if (checkbox_selected.checked)
                    {
                        strParam += GetData(C_EVENT_REPROCESS, checkbox_selected.value) + "/lb/";
                    }    
                }
            }            

            strUrl = Page + "_ajax.jsp?Param=" + strParam;            
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
                alert(GetValueFromParamTag(strParam, "Message"));
                BackToGrid(PAGE_NAME);
            }
        }
    }            


    function Navigate(page, id_transaction, name_transaction, field_name, id_view)
    {          
        var i = 0;
        var strParam = "";
        var strUrl = "";
        var checkbox_selected = null;
        var intRecordCount = 0;

        if (this.IsAnyItemSelected() == false)
        {
            var strMessage = "";
            strMessage = document.getElementById("txtMessageSelect").value;
            alert(strMessage);
            return;
        }

        intRecordCount = document.frmDefault.txtRecordCount.value-1;        
        for (i=1; i<=intRecordCount; i++)
        {
            checkbox_selected = document.getElementById("chk_" + i);
            
            if (checkbox_selected != null)
            {
                if (checkbox_selected.checked)
                {    
                    strUrl = page + "_list.jsp?id_transaction=" + id_transaction + "&name_transaction=" + name_transaction + "&field_name=" + field_name + "&field_value=" + checkbox_selected.value + "&cboIdView=" + id_view;

                    document.frmDefault.method = 'Post';
                    document.frmDefault.action = strUrl;
                    document.frmDefault.submit();

                    break;
                }    
            }
        }
    }


    
    /*
     * Check whether at least one item is selected
     */
    function IsAnyItemSelected()
    {
        var i = 0;
        var intRecordCount = 0;
        var checkbox_selected = null;
        
        intRecordCount = document.frmDefault.txtRecordCount.value-1;

        for (i=1; i<=intRecordCount; i++)
        {
            checkbox_selected = document.getElementById("chk_" + i);            

            if (checkbox_selected != null)
            {
                if (checkbox_selected.checked)
                {
                    return true;
                }
            }
        }

        return false;
    }

    /*
     * Select or unselect all check boxes
     */
    function MarkAllAsCheckOrUnCheck()
    {          
        var i = 0;
        var checkbox_select_all = null;
        var intRecordCount = 0;

        intRecordCount = document.frmDefault.txtRecordCount.value -1;

        checkbox_select_all = document.getElementById("chk_all");

        for (i=1; i<=intRecordCount; i++)
        {
            checkbox_selected = document.getElementById("chk_" + i);
            checkbox_selected.checked = checkbox_select_all.checked;
        }
    }    

    /*
     * Select current item in the available list and move to the selected list
     */
    function Select(list1, list2) 
    {
        /*
         * General Declaration
         */ 
       var objAvailable =  list1;
       var objSelected = list2;

        /*
        * Add selected item
        */
       var optItem = new Option(objAvailable.options[objAvailable.selectedIndex].text, objAvailable.options[objAvailable.selectedIndex].value);
       objSelected.options.add(optItem);

        /*
        * Remove selected item
        */
        objAvailable.options.remove(objAvailable.selectedIndex);

    }

    /*
     * Select all item in the available list and move to the selected list
     */
    function SelectAll(list1, list2) 
    {
        /*
         * General Declaration
         */ 
       var objAvailable =  list1;
       var objSelected = list2;

       for (var i = (objAvailable.length-1); i >= 0; i--)	
       {
            /*
             * Add selected item
             */
            var optItem = new Option(objAvailable.options[i].text, objAvailable.options[i].value);
            objSelected.options.add(optItem);

            /*
             * Remove selected item
             */	
             objAvailable.options.remove(i);
        }

    }

    /*
     * Select current item in the selected list and move to the available list
     */
    function UnSelect(list1, list2) 
    {
        /*
         * General Declaration
         */ 
       var objAvailable =  list1;
       var objSelected = list2;

        /*
        * Add selected item
        */
       var optItem = new Option(objSelected.options[objSelected.selectedIndex].text, objSelected.options[objSelected.selectedIndex].value);
       objAvailable.options.add(optItem);

        /*
        * Remove selected item
        */	
        objSelected.options.remove(objSelected.selectedIndex);
        
    }

    /*
     * Select all items in the selected list and move to the available list
     */
    function UnSelectAll(list1, list2) 
    {
        /*
         * General Declaration
         */ 
       var objAvailable =  list1;
       var objSelected = list2;

       for (var i = (objSelected.length-1); i >= 0; i--)	
       {	
            /*
             * Add selected item
             */
            var optItem = new Option(objSelected.options[i].text, objSelected.options[i].value);
            objAvailable.options.add(optItem);

            /*
             * Remove selected item
             */	
             objSelected.options.remove(i);
        }
    }
    
    /*
     * Read the XML tags
     */    
    function GetValueFromParamTag(strParam, strTagName)
    {   
        var strRet = "";
        var strTag1 = "[" + strTagName.trim() + "]";
        var strTag2 = "[/" + strTagName.trim() + "]";
        var intPos1 = 0;
        var intPos2 = 0;

        intPos1 = strParam.indexOf(strTag1) + strTag1.length;
        intPos2 = strParam.indexOf(strTag2);

        strRet = strParam.substring(intPos1, intPos2);

        return strRet;

    }