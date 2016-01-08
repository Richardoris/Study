package com.acmeair.service.jpa;

import org.springframework.stereotype.Service;
import com.acmeair.service.KeyGenerator;

@Service("keyGenerator")
public class DefaultKeyGeneratorImpl implements KeyGenerator {

	@Override
	public Object generate() {
		return java.util.UUID.randomUUID().toString();
	}
}