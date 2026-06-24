package com.specification.service.domain.entity.function;

import com.specification.service.domain.BlogStatus;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.specification.service.domain.base.impl.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "blog_details")
@CompoundIndexes({
        @CompoundIndex(name = "status_category_idx", def = "{'status': 1, 'category': 1}"),
        @CompoundIndex(name = "author_idx", def = "{'authorId': 1}")
})
public class HomeDetails extends BaseEntity {

    @Indexed
    private String title;
    @Indexed(unique = true)
    private String slug;
    private String summary;
    private String content;
    private String authorId;
    private String authorName;
    private String category;
    private List<String> tags;
    private String featuredImageUrl;
    private BlogStatus status;
    private boolean isPublished;
    private LocalDateTime publishedAt;
    private String metaTitle;
    private String metaDescription;
    private String metaKeywords;
    private long views;
    private long likes;
    private long shares;
    private List<Comment> comments;
    private boolean allowComments = true;
}
