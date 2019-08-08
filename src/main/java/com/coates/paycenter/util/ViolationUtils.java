package com.coates.paycenter.util;

import org.hibernate.validator.internal.engine.path.PathImpl;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import java.util.Iterator;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Created by wangjs on 2019/4/9.
 */
public abstract class ViolationUtils {


    // 取错误字段名集合
    public static <T> List<String> getFieldNames(List<ConstraintViolation<T>> violations) {
        return violations.stream().map(violation -> getFieldName(violation.getPropertyPath())).collect(toList());
    }

    // 取错误字段名
    public static String getFieldName(Path path) {
        if (path instanceof PathImpl) {
            return ((PathImpl) path).getLeafNode().getName();
        }
        Iterator<Path.Node> iterator = path.iterator();
        while (iterator.hasNext()) {
            return iterator.next().getName();
        }
        throw new IllegalStateException("never reach here");
    }
}
