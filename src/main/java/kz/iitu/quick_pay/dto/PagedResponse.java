package kz.iitu.quick_pay.dto;

import java.util.List;

public class PagedResponse<T> {
    private long totalCount;
    private List<T> data;

    public PagedResponse(long totalCount, List<T> data) {
        this.totalCount = totalCount;
        this.data = data;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public List<T> getData() {
        return data;
    }
}
