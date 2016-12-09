package com.restdude.domain.details.contact.service.impl;

import com.restdude.domain.details.contact.model.CellphoneDetail;
import com.restdude.domain.details.contact.repository.CellphoneDetailRepository;
import com.restdude.domain.details.contact.service.CellphoneDetailService;

import javax.inject.Named;

@Named(CellphoneDetailService.BEAN_ID)
public class CellphoneDetailServiceImpl extends AbstractContactDetailServiceImpl<CellphoneDetail, String, CellphoneDetailRepository> implements CellphoneDetailService {

}
