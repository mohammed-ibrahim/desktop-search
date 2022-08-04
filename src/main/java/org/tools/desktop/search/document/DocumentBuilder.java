package org.tools.desktop.search.document;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.util.CollectionUtil;
import org.tools.desktop.search.Fields;
import org.tools.desktop.search.Tokenizer.Tokenizer;
import org.tools.desktop.search.bookmark.BookMark;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class DocumentBuilder {

  private static final String SINGLE_SPACE = " ";

  public static Document fromBookMark(BookMark bookMark) {
    Document document = new Document();

    StringField typeField = new StringField(Fields.DOCUMENT_TYPE, Fields.DOCUMENT_TYPE_BOOKMARK, Field.Store.YES);
    document.add(typeField);

    TextField linkField = new TextField(Fields.FIELD_LINK, bookMark.getLink(), Field.Store.YES);
    document.add(linkField);

    TextField titleField = new TextField(Fields.FIELD_TITLE, bookMark.getTitle(), Field.Store.YES);
    document.add(titleField);

    String allTags = StringUtils.join(getTags(bookMark), SINGLE_SPACE);

    System.out.println("Adding tags: " + allTags + " lnk: " + bookMark.getLink());

    TextField tagField = new TextField(
        Fields.FIELD_TAGS, allTags, Field.Store.YES);
    document.add(tagField);
    return document;
  }

  private static List<String> getTags(BookMark bookMark) {
    List<String> tags = new ArrayList<>();

    Tokenizer.addTokens(tags, bookMark.getTitle());

    try {
      URIBuilder uriBuilder = new URIBuilder(bookMark.getLink());
      Tokenizer.addTokens(tags, uriBuilder.getHost());
    } catch (Exception e) {
      e.printStackTrace();
    }

    return new ArrayList<>(new HashSet<>(CommonTermsCleaner.cleanCommonTerms(tags)));
  }


}
