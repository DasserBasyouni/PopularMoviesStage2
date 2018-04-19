package com.example.dasser.popular.movies.stage2.model;

import java.util.List;

import static com.example.dasser.popular.movies.stage2.utils.Utils.ArrayStringMovieDataType.author;
import static com.example.dasser.popular.movies.stage2.utils.Utils.ArrayStringMovieDataType.content;
import static com.example.dasser.popular.movies.stage2.utils.Utils.ArrayStringMovieDataType.key;
import static com.example.dasser.popular.movies.stage2.utils.Utils.ArrayStringMovieDataType.name;
import static com.example.dasser.popular.movies.stage2.utils.Utils.ArrayStringMovieDataType.site;

public class MovieMoreDetails {
    private int runtime;
    private ResultModel<List<Review>> reviews;
    private ResultModel<List<Video>> videos;

    public class Review {
        private String author, content;

        public String getAuthor() { return author; }
        public String getContent() { return content; }
    }

    public class Video {
        private String key, name, site;

        public String getKey() { return key; }
        public String getName() { return name; }
        public String getSite() { return site; }
    }

    public int getRuntime() {
        return runtime;
    }

    public ResultModel<List<Review>> getReview() {
        return reviews;
    }

    public ResultModel<List<Video>> getTrailer() {
        return videos;
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

    private String convertArrayStringToString(Object rList, int type) {
        StringBuilder value = new StringBuilder();
        for(int i=0 ; i< ( (List) ((ResultModel)rList).getResults()).size() ; i++) {
            if (i > 0)
                value.append("__,__");
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
