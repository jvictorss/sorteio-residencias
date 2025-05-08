package gov.sorteio.controller;

import gov.sorteio.bo.BaseBO;
import gov.sorteio.entity.LotteryEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/v1/lottery")
public class LotteryController extends BaseController<LotteryEntity, Long> {
    protected LotteryController(BaseBO<LotteryEntity, Long> baseBO) {
        super(baseBO);
    }
}
