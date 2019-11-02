$(function () {
    loadCustomerGc();
})

function loadCustomerGc() {
    $.ajax({
        url: ctx + "/customer/queryCustomerGC",
        dataType: 'json',
        success: function (data) {
            if (data.code == 200) {
                var data_x = data.levels;
                var data_y = data.counts;
            }
            // 基于准备好的dom，初始化echarts实例
            var myChart = echarts.init(document.getElementById('main'));

            // 指定图表的配置项和数据
            var option = {
                title: {
                    text: '客户构成分析图'
                },
                tooltip: {},
                legend: {
                    data: ['数量']
                },
                xAxis: {
                    data: data_x
                },
                yAxis: {},
                series: [{
                    name: '数量',
                    type: 'bar',
                    data: data_y
                }]
            };
            // 使用刚指定的配置项和数据显示图表。
            myChart.setOption(option);
        }
    })
}

