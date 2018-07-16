
<!DOCTYPE html>

<html>
<%@include file="../common/head.jsp"%>    
<body>
     
<%@include file="../common/menu_login.jsp"%>

<%@include file="../common/bar.jsp"%>


    
<div id="chartContainer" style="height: 300px; width: 100%;">

</div>

    
    
    
<script type="text/javascript">
    
    window.onload = function () 
    {
        var chart = new CanvasJS.Chart("chartContainer",
        {
            theme: "theme1", animationEnabled: true,
                    title:
                    {
                        text: "Titulo do gráfico",
                        fontSize: 30
                    },

            toolTip: 
            {
                shared: true
            },			

            axisY: 
            {
                title: ""
            },

            axisY2: 
            {
                title: ""
            },
            data:
            [ 
                {
                    type: "column",	
                    name: "Batido",
                    legendText: "Batido",
                    showInLegend: true, 
                    dataPoints:
                    [
                        {label: "Fundo Soberano I", y: 10},
                        {label: "Conta Corrente", y: 5}
                    ]
                }
                ,     
                {
                    type: "column",	
                    name: "Proposto",
                    legendText: "Proposto",
                    showInLegend: true, 
                    dataPoints:
                    [
                        {label: "Fundo Soberano I", y: 15},
                        {label: "Conta Corrente", y: 10}
                    ]
                }
            ],

        legend:
        {
            cursor:"pointer",
            itemclick: function(e)
            {
                if (typeof(e.dataSeries.visible) === "undefined" || e.dataSeries.visible) 
                {
                    e.dataSeries.visible = false;
                }
                else 
                {
                    e.dataSeries.visible = true;
                }
                    chart.render();
            }
        },
    });

    chart.render();
}
</script>


</body>
</html>
