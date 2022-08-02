package org.tools.desktop.search.document;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.tools.desktop.search.Fields;
import org.tools.desktop.search.bookmark.BookMark;

public class DocumentBuilder {

  public static Document fromBookMark(BookMark bookMark) {
    Document document = new Document();

    StringField typeField = new StringField(Fields.DOCUMENT_TYPE, Fields.DOCUMENT_TYPE_BOOKMARK, Field.Store.YES);
    document.add(typeField);

    TextField linkField = new TextField(Fields.FIELD_LINK, bookMark.getLink(), Field.Store.YES);
    document.add(linkField);

    TextField titleField = new TextField(Fields.FIELD_TITLE, bookMark.getTitle(), Field.Store.YES);
    document.add(titleField);

    TextField tagField = new TextField(Fields.FIELD_TAGS, bookMark.getTitle() + " " + bookMark.getLink(), Field.Store.YES);
    document.add(tagField);

    return document;
  }
}
