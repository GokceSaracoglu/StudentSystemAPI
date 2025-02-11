package com.saracoglu.student.system.contoller;

import com.saracoglu.student.system.utils.PagerUtil;
import com.saracoglu.student.system.utils.RestPageableEntity;
import com.saracoglu.student.system.utils.RestPageableRequest;
import com.saracoglu.student.system.utils.RestRootEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class RestBaseController {

    public Pageable toPageable(RestPageableRequest request) {
        return PagerUtil.toPageable(request);
    }

    public <T> RestPageableEntity<T> toPageableResponse(Page<?> page , List<T> content) {
        return PagerUtil.toPageableResponse(page, content);
    }

    public <T> RestRootEntity<T> ok(T payload){
        return RestRootEntity.ok(payload);
    }
}
