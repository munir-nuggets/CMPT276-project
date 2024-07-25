package cmpt276.project.marketmimic.model;

import java.util.List;

public class NewsResponse {
    private Meta meta;
    private List<NewsArticle> data;

    // Getters and Setters

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<NewsArticle> getData() {
        return data;
    }

    public void setData(List<NewsArticle> data) {
        this.data = data;
    }

    public static class Meta {
        private int found;
        private int returned;
        public int getFound() {
            return found;
        }
        public void setFound(int found) {
            this.found = found;
        }
        public int getReturned() {
            return returned;
        }
        public void setReturned(int returned) {
            this.returned = returned;
        }
        public int getLimit() {
            return limit;
        }
        public void setLimit(int limit) {
            this.limit = limit;
        }
        public int getPage() {
            return page;
        }
        public void setPage(int page) {
            this.page = page;
        }
        private int limit;
        private int page;

        // Getters and Setters
        //...
    }

    public static class NewsArticle {
        private String uuid;
        private String title;
        private String description;
        private String snippet;
        private String url;
        private String image_url;
        private String language;
        private String published_at;
        private String source;
        public String getUuid() {
            return uuid;
        }
        public void setUuid(String uuid) {
            this.uuid = uuid;
        }
        public String getTitle() {
            return title;
        }
        public void setTitle(String title) {
            this.title = title;
        }
        public String getDescription() {
            return description;
        }
        public void setDescription(String description) {
            this.description = description;
        }
        public String getSnippet() {
            return snippet;
        }
        public void setSnippet(String snippet) {
            this.snippet = snippet;
        }
        public String getUrl() {
            return url;
        }
        public void setUrl(String url) {
            this.url = url;
        }
        public String getImage_url() {
            return image_url;
        }
        public void setImage_url(String image_url) {
            this.image_url = image_url;
        }
        public String getLanguage() {
            return language;
        }
        public void setLanguage(String language) {
            this.language = language;
        }
        public String getPublished_at() {
            return published_at;
        }
        public void setPublished_at(String published_at) {
            this.published_at = published_at;
        }
        public String getSource() {
            return source;
        }
        public void setSource(String source) {
            this.source = source;
        }

        // Getters and Setters
        //...
    }
}
