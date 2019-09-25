package org.seckill.web;

import lombok.extern.slf4j.Slf4j;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.dto.SeckillResult;
import org.seckill.entity.Seckill;
import org.seckill.enums.SeckillStatEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.RewriteException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SoldOutException;
import org.seckill.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author : ActStrady@tom.com
 * @date : 2019/6/16 15:42
 * @fileName : SeckillController.java
 * @gitHub : https://github.com/ActStrady/newSeckill
 */
@Slf4j
@Controller
@RequestMapping("/seckill")
public class SeckillController {
    private final SeckillService seckillService;

    /**
     * 使用构造器注入
     *
     * @param seckillService 秒杀service
     */
    @Autowired
    public SeckillController(SeckillService seckillService) {
        this.seckillService = seckillService;
    }

    /**
     * 获取列表页
     *
     * @param model 服务器向页面传值
     * @return 秒杀列表页
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model) {
        List<Seckill> seckillList = seckillService.getSeckillList(0, 5);
        model.addAttribute("list", seckillList);
        log.info("seckillList:" + seckillList);
        return "list";
    }

    /**
     * 详细页
     *
     * @param seckillId 秒杀商品id
     * @param model     服务器向页面传值
     * @return 详细页
     */
    @RequestMapping(value = "/{seckillId}/detail", method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId") Long seckillId, Model model) {
        if (seckillId == null) {
            // 重定向
            return "redirect:/seckill/list";
        }
        Seckill seckill = seckillService.getById(seckillId);
        if (seckill == null) {
            // 请求转发
            return "forward:/seckill/list";
        }
        model.addAttribute("seckill", seckill);
        return "detail";
    }

    /**
     * 秒杀地址, 其中@ResponseBody表示的就是返回的是json信息
     *
     * @param seckillId 秒杀id
     * @return json数据，秒杀地址
     */
    @RequestMapping(value = "/{seckillId}/exposer",
            method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    @ResponseBody
    public SeckillResult<Exposer> exposer(@PathVariable("seckillId") Long seckillId) {
        try {
            Exposer exposer = seckillService.exportSeckillUrl(seckillId);
            return new SeckillResult<>(true, exposer);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new SeckillResult<>(false, e.getMessage());
        }
    }

    /**
     * 执行秒杀
     *
     * @param seckillId 秒杀 id
     * @param md5       加密值
     * @param killPhone 秒杀手机
     * @return 秒杀结果
     */
    @RequestMapping(value = "/{seckillId}/{md5}/execution",
            method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    @ResponseBody
    public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId") Long seckillId,
                                                   @PathVariable("md5") String md5,
                                                   @CookieValue(value = "killPhone", required = false) Long killPhone) {
        if (killPhone == null) {
            return new SeckillResult<>(false, "未注册");
        }
        try {
            SeckillExecution execution = seckillService.executeSeckill(seckillId, killPhone, md5);
            return new SeckillResult<>(true, execution);
        } catch (RepeatKillException e) {
            // 重复秒杀
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.REPEAT_KILL);
            return new SeckillResult<>(false, execution);
        } catch (SeckillCloseException e) {
            // 秒杀关闭
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.END);
            return new SeckillResult<>(false, execution);
        } catch (RewriteException e) {
            // 数据篡改
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.DATA_REWRITE);
            return new SeckillResult<>(false, execution);
        } catch (SoldOutException e) {
            // 数据篡改
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.SOLD_OUT);
            return new SeckillResult<>(false, execution);
        } catch (Exception e) {
            // 其他异常
            log.error(e.getMessage(), e);
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.INNER_ERROR);
            return new SeckillResult<>(false, execution);
        }
    }

    /**
     * 当前时间
     *
     * @return 当前时间
     */
    @RequestMapping(value = "/time/now",
            method = RequestMethod.GET,
            produces = "application/json;charset=UTF-8")
    @ResponseBody
    public SeckillResult<Long> time() {
        Date now = new Date();
        return new SeckillResult<>(true, now.getTime());
    }
}
