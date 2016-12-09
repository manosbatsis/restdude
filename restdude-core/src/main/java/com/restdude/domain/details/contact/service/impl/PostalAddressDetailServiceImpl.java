package com.restdude.domain.details.contact.service.impl;

import com.restdude.domain.details.contact.model.PostalAddressDetail;
import com.restdude.domain.details.contact.repository.PostalAddressDetailRepository;
import com.restdude.domain.details.contact.service.PostalAddressDetailService;

import javax.inject.Named;

@Named(PostalAddressDetailService.BEAN_ID)
public class PostalAddressDetailServiceImpl extends AbstractContactDetailServiceImpl<PostalAddressDetail, String, PostalAddressDetailRepository> implements PostalAddressDetailService {

}
