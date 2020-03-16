package org.fibo.seo.autocomplete.dto;

public class ScoredKeywordDto {
    public final String Keyword;
    public final long score;

    public ScoredKeywordDto(String keyword, long score) {
        Keyword = keyword;
        this.score = score;
    }
}
