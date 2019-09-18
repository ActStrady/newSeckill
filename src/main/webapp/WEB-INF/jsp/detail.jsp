<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>秒杀详情页</title>
    <%-- 静态包含 --%>
    <%@include file="common/head.jsp" %>
</head>
<body>
<div class="container">
    <div class="panel panel-default text-center">
        <div class="panel-heading">
            <h1>${seckill.name}</h1>
        </div>
        <div class="panel-body">
            <h2 class="text-danger">
                <span class="glyphicon glyphicon-time"></span>
                <%-- 各种信息 --%>
                <span class="glyphicon" id="seckill-box"></span>
            </h2>
        </div>
    </div>
</div>
<%-- 弹出层，输入电话号码 --%>
<div class="modal fade" id="killPhoneModal">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h3 class="modal-title text-center">
                    <span class="glyphicon glyphicon-phone"></span>

                    <label for="killPhoneKey">秒杀电话：</label>
                </h3>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-xs-8 col-xs-offset-2">
                        <input type="text" class="form-control" id="killPhoneKey"
                               name="killPhone" placeholder="填写手机号">
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <span class="glyphicon" id="killPhoneMessage"></span>
                <button class="btn btn-success" id="killPhoneBtn" type="button">
                    <span class="glyphicon glyphicon-phone">Submit</span>
                </button>
            </div>
        </div>
    </div>
</div>
</body>
<%-- 秒杀交互逻辑 --%>
<script src="${pageContext.request.contextPath}/resources/script/seckill.js" type="text/javascript"></script>
<script type="text/javascript">
    $(function () {
        // 使用el表达式子传入到js里
        seckill.detail.init({
            seckillId: ${seckill.seckillId},
            startTime: ${seckill.startTime.time},
            endTime: ${seckill.endTime.time}
        });
    });
</script>
<%-- cdn加速的jq和bootstrap --%>
<script src="https://cdn.bootcss.com/jquery/3.4.1/jquery.min.js"></script>
<script src="https://cdn.bootcss.com/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>
<%-- jq cookie插件 --%>
<script src="https://cdn.bootcss.com/jquery-cookie/1.4.1/jquery.cookie.min.js"></script>
<%-- jq 倒计时插件 --%>
<script src="https://cdn.bootcss.com/jquery.countdown/2.2.0/jquery.countdown.min.js"></script>
</html>