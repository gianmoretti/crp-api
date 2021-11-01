package com.gianmo.crp;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;

@SpringBootTest
@Rollback()
@Transactional
public abstract class PersistedItTest {

}
