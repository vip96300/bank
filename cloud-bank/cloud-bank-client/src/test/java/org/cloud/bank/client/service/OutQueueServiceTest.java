package org.cloud.bank.client.service;

import org.cloud.bank.client.BaseTest;
import org.cloud.bank.client.model.OutQueue;
import org.cloud.bank.client.repository.OutQueueRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class OutQueueServiceTest extends BaseTest{

	@Autowired
	private OutQueueRepository outQueueRepository;
	
}
