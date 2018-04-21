package com.example.dasser.popular.movies.stage2.model;

import java.util.List;

import static com.example.dasser.popular.movies.stage2.Constants.ArrayStringMovieDataType.name;
import static com.example.dasser.popular.movies.stage2.Constants.ArrayStringMovieDataType.key;
import static com.example.dasser.popular.movies.stage2.Constants.ArrayStringMovieDataType.content;
import static com.example.dasser.popular.movies.stage2.Constants.ArrayStringMovieDataType.site;
import static com.example.dasser.popular.movies.stage2.Constants.ArrayStringMovieDataType.author;
import static com.example.dasser.popular.movies.stage2.Constants.STRING_SEPARATOR;


@SuppressWarnings("unused")
public class MovieMoreDetails {
    private int runtime;
    private ResultModel<List<Review>> reviews;
    private ResultModel<List<Video>> videos;

    class Review {
        private String author, content;

        String getAuthor() { return author; }
        String getContent() { return content; }
    }

    class Video {
        private String key, name, site;

        String getKey() { return key; }
        String getName() { return name; }
        String getSite() { return site; }
    }

    public int getRuntime() {
        return runtime;
    }

    public String getAllVideosKeys() {
        return convertArrayStringToString(videos, key);
    }

    public String getAllVideosNames() {
        return convertArrayStringToString(videos, name);
    }

    public String getAllVideosSites() {
        return convertArrayStringToString(videos, site);
    }

    public String getAllReviewsAuthors() {
        return convertArrayStringToString(reviews, author);
    }

    public String getAllReviewsContents() {
        return convertArrayStringToString(reviews, content);
    }

    @SuppressWarnings("unchecked")
    private String convertArrayStringToString(Object rList, int type) {
        StringBuilder value = new StringBuilder();
        for(int i=0 ; i< ( (List) ((ResultModel)rList).getResults()).size() ; i++) {
            if (i > 0)
                value.append(STRING_SEPARATOR);
            switch (type){
                case author:
                    value.append( ((List<Review>) ((ResultModel)rList).getResults()).get(i).getAuthor());
                    break;
                case content:
                    value.append( ((List<Review>) ((ResultModel)rList).getResults()).get(i).getContent());
                    break;
                case key:
                    value.append( ((List<Video>) ((ResultModel)rList).getResults()).get(i).getKey());
                    break;
                case name:
                    value.append( ((List<Video>) ((ResultModel)rList).getResults()).get(i).getName());
                    break;
                case site:
                    value.append( ((List<Video>) ((ResultModel)rList).getResults()).get(i).getSite());
                    break;
            }
        }
        return value.toString();
    }
}
