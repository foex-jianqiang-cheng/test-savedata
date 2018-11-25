/*
 * Copyright 2012-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package sbitest;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cc.bitbank.Bitbankcc;
import cc.bitbank.entity.Depth;
import cc.bitbank.entity.enums.CurrencyPair;
import sbitest.entity.db.Price;
import sbitest.service.GetpriceService;

@Component
public class ScheduledTasks {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    private static final Bitbankcc bbs = new Bitbankcc();

    @Autowired
    private GetpriceService getpriceService;

    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
        log.info("The time is now {}", dateFormat.format(new Date()));
        // //////////////purchase price on bitbank//////////////
        try {
            Depth depth = bbs.getDepth(CurrencyPair.XRP_JPY);
            BigDecimal purchasePrice = calcPurchasePrice(depth);
            log.info("The price is {}", purchasePrice);
            Price price = new Price(purchasePrice, new Date());
            getpriceService.create(price);

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        
    }
    private BigDecimal calcPurchasePrice(Depth depth) {
        BigDecimal avgPrice = depth.getAsks()[0][0];
        // purchase price
        return avgPrice;
    }

}
