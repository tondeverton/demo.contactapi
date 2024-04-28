package com.tondeverton.demo.contactapi.utilities;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collection;

public class PageConverter implements PageConverterUtil {
    @Override
    public <T> Page<T> collectionToPage(Collection<T> items, int page, int pageSize) {
        var start = page * pageSize;
        var end = Math.min(start + pageSize, items.size());

        var sublist = items.stream().toList().subList(start, end);

        return new PageImpl<>(sublist, PageRequest.of(page, pageSize), items.size());
    }
}
