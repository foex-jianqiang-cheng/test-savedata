/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sbitest.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sbitest.dao.PriceRepository;
import sbitest.entity.db.Price;

@Service
@Transactional
public class GetpriceService {

    @Autowired
    private PriceRepository piceRepository;

    public Price create(Price price) {
        return piceRepository.save(price);
    }
}
