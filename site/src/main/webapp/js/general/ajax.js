
function ajaxFunctionSync(strUrl)
{
    try
    {
        // Opera 8.0+, Firefox, Safari
        ajaxRequest = new XMLHttpRequest();
    } 
    catch (e)
    {
        // Internet Explorer Browsers
        try
        {
            ajaxRequest = new ActiveXObject("Msxml2.XMLHTTP");
        } 
        catch (e) 
        {
            try
            {
                ajaxRequest = new ActiveXObject("Microsoft.XMLHTTP");
            } 
            catch (e)
            {
                // Something went wrong
                alert("Your browser broke!");
                return false;
            }
        }
    }

    ajaxRequest.open('POST', strUrl, false);  // `false` makes the request synchronous
    ajaxRequest.setRequestHeader("Content-Type", "text/plain, charset=UTF-8");
    ajaxRequest.send(null);

    if (ajaxRequest.status == 200) 
    {
      return ajaxRequest.responseText;
    }
}

function ajaxFunctionAsync(strUrl, Id)
{
    var ajaxRequest;  // The variable that makes Ajax possible!

    try
    {
        // Opera 8.0+, Firefox, Safari
        ajaxRequest = new XMLHttpRequest();
    } 
    catch (e)
    {
        // Internet Explorer Browsers
        try
        {
            ajaxRequest = new ActiveXObject("Msxml2.XMLHTTP");
        } 
        catch (e) 
        {
            try
            {
                ajaxRequest = new ActiveXObject("Microsoft.XMLHTTP");
            } 
            catch (e)
            {
                // Something went wrong
                alert("Your browser broke!");
                return false;
            }
        }
    }
    
    // Create a function that will receive data sent from the server
    ajaxRequest.onreadystatechange = function()
    {
        if (ajaxRequest.readyState == 4)
        {
            document.getElementById(Id).innerHTML = ajaxRequest.responseText;
        }
    }

    ajaxRequest.open("POST", strUrl, true);
    ajaxRequest.setRequestHeader("Content-Type", "text/plain, charset=UTF-8");
    ajaxRequest.send(null); 
}
