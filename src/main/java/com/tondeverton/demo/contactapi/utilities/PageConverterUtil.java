package com.tondeverton.demo.contactapi.utilities;

import org.springframework.data.domain.Page;

import java.util.Collection;

public interface PageConverterUtil {

    <T> Page<T> collectionToPage(Collection<T> collection, int page, int pageSize);
}
