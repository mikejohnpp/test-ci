package org.social.Dtos.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductGetRequest {
    private Integer page = 0;
    private Integer size = 10;
    private String keyword;
    private String column;
    private Integer quantitySell;


    public ProductGetRequest() {}

    public ProductGetRequest(Integer page, Integer size, String keyword, String column, Integer quantitySell) {
        this.page = page;
        this.size = size;
        this.keyword = keyword;
        this.column = column;
        this.quantitySell = quantitySell;
    }

}
