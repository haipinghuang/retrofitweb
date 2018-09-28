package com.hai.base;

import org.slf4j.LoggerFactory;

/**
 * BaseController,provider only logger member for every Controller
 * Created by huanghp on 2018/9/28.
 * Email h1132760021@sina.com
 */
public abstract class BaseController {
    protected org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
}
