// 存放主要的交互逻辑
// 利用json模拟java分包
const seckill = {
    URL: {},
    // 验证手机号isNaN判断是否数字
    validatePhone: function (phone) {
        // 正则匹配手机号
        const reg = /^1[3-9]\d{9}$/;
        return phone && phone.length === 11 && reg.test(phone);
    },
    detail: {
        // 详情页初始化
        init: function (params) {
            // 手机验证和登录， 计时
            const seckillId = params.seckillId;
            const startTime = params.startTime;
            const endTime = params.endTime;
            // 从cookie里取手机号
            const killPhone = $.cookie('killPhone');
            // 手机号不在cookie里做处理
            if (!seckill.validatePhone(killPhone)) {
                const killPhoneModal = $('#killPhoneModal');
                // 显示弹窗并且设置其不能关掉
                killPhoneModal.modal({
                    show: true, // 显示
                    backdrop: 'static', // 禁止位置关闭，就是点击其他地方弹窗不关闭
                    keyboard: false // 关闭键盘事件，用户按esc弹窗不关闭
                });
                // 点击提交手机号按钮
                $('#killPhoneBtn').click(function () {
                    const inputPhone = $('#killPhoneKey').val();
                    if (seckill.validatePhone(inputPhone)) {
                        // 手机号写入cookie，设置时效7天，路径只是秒杀模块有效
                        $.cookie('killPhone', inputPhone, {expires: 7, path: '/seckill'});
                        // 刷新页面
                        window.location.reload();
                    } else if (!inputPhone) {
                        // 手机号为空，提示输入手机号
                        $('#killPhoneMessage').hide().html('<label class="label label-danger" ' +
                            'for="killPhoneKey">请输入手机号！</label>').show(300);
                    } else {
                        // 手机号错误，提示错误
                        $('#killPhoneMessage').hide().html('<label class="label label-danger" ' +
                            'for="killPhoneKey">请输入正确的手机号！</label>').show(300);
                    }
                });
                // 点击输入框，错误消息隐藏
                $('#killPhoneKey').click(function () {
                    $('#killPhoneMessage').hide(300);
                });
            }
        }
    }
};