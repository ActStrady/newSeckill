// 存放主要的交互逻辑
// 利用json模拟java分包
const seckill = {
    // 封装所有的URL
    URL: {
        now: function () {
            return '/seckill/time/now';
        },
        exposer: function (seckillId) {
            return '/seckill/' + seckillId + '/exposer';
        },
        execution: function (seckillId, md5) {
            return '/seckill/' + seckillId + '/' + md5 + '/execution'
        }
    },
    handleSeckill: function (seckillId, node) {
        // 添加一个隐藏的秒杀按钮
        node.hide().html('<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>');
        // 获取秒杀地址
        $.post(seckill.URL.exposer(seckillId), {}, function (result) {
            if (result && result.success) {
                const exposer = result.data;
                // 秒杀地址获取成功
                if (exposer['exposed']) {
                    // 获取md5
                    const md5 = exposer['md5'];
                    // 按钮只能点击一次
                    $('#killBtn').one('click', function () {
                        // 按钮设置为不可点
                        $(this).addClass('disabled');
                        // 发送秒杀请求
                        $.post(seckill.URL.execution(seckillId, md5), {}, function (result) {
                            const killResult = result.data;
                            const stateInfo = killResult['stateInfo'];
                            // 秒杀成功
                            if (result && result.success) {
                                node.html('<span class="label label-success">' + stateInfo + '</span>');
                            } else if (result) {
                                node.html('<span class="label label-warning">' + stateInfo + '</span>');
                            } else {
                                console.log('result:' + result);
                            }
                        });
                    });
                } else {
                    // 处理服务器与客户端出现时间偏差，以服务器时间为准
                    const nowTime = exposer['nowTime'];
                    const startTime = exposer['startTime'];
                    const endTime = exposer['endTime'];
                    seckill.countdown(seckillId, nowTime, startTime, endTime);
                }
            } else {
                console.log('result:' + result);
            }
        });
        node.show();
    },
    // 验证手机号isNaN判断是否数字
    validatePhone: function (phone) {
        // 正则匹配手机号
        const reg = /^1[3-9]\d{9}$/;
        return phone && phone.length === 11 && reg.test(phone);
    },
    // 抽取的倒计时以及显示秒杀状态的函数
    countdown: function (seckillId, nowTime, startTime, endTime) {
        const seckillBox = $('#seckill-box');
        if (nowTime > endTime) {
            // 秒杀结束
            seckillBox.html('<span class="label label-warning">秒杀结束!</span>');
        } else if (nowTime < startTime) {
            // 秒杀未开启，倒计时
            // 倒计时开始时间，加一秒来控制网络等问题
            const killTime = new Date(startTime);
            seckillBox.countdown(killTime, function (event) {
                const format = event.strftime('秒杀倒计时: %D天: %H时: %M分: %S秒');
                seckillBox.html(format);
                // 倒计时结束的回调函数
            }).on('finish.countdown', function () {
                // 获取秒杀地址，执行秒杀逻辑
                seckill.handleSeckill(seckillId, seckillBox);
            });
        } else {
            // 执行秒杀
            seckill.handleSeckill(seckillId, seckillBox);
        }
    },
    detail: {
        // 详情页初始化
        init: function (params) {
            // 手机验证和登录， 计时
            // 从cookie里取手机号
            const killPhone = $.cookie('killPhone');
            // 手机号不在cookie里, 弹出框显示记录手机号，然后刷新页面
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
            // 登录成功， 显示倒计时或者其他信息
            // ajax 获取后台数据
            const seckillId = params.seckillId;
            const startTime = params.startTime;
            const endTime = params.endTime;
            $.get(seckill.URL.now(), {}, function (result) {
                if (result && result.success) {
                    const nowTime = result.data;
                    seckill.countdown(seckillId, nowTime, startTime, endTime);
                } else {
                    console.log('result:' + result);
                }
            });
        }
    }
};