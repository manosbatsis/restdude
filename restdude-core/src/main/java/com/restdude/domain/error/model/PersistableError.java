package com.restdude.domain.error.model;

import com.restdude.domain.base.model.CalipsoPersistable;
import com.restdude.domain.users.model.User;

import java.io.Serializable;
import java.util.Date;

/**
 * @param <ID>
 */
public interface PersistableError<ID extends Serializable> extends CalipsoPersistable<ID> {

    public static final int MAX_MSTACKTRACE_LENGTH = 20000;
    public static final int MAX_MESSAGE_LENGTH = 1000;

    String getMessage();

    void setMessage(String message);

    Date getCreatedDate();

    void setCreatedDate(Date createdDate);

    User getUser();

    void setUser(User user);

    UserAgent getUserAgent();

    void setUserAgent(UserAgent userAgent);

    ErrorLog getErrorLog();

    void setErrorLog(ErrorLog errorLog);
}
