package org.tools.desktop.search;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.tools.desktop.search.bookmark.BookMark;
import org.tools.desktop.search.bookmark.BookMarkParser;
import org.tools.desktop.search.document.DocumentBuilder;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

public class IndexerMain {

  public static final String DROP_LOCATION = "index.drop.location";
  private static final String BOOKMARKS_HTML_FILE = "bookmark.html.file";
  public static void main(String[] args) throws Exception {
    /*
    1. Index the bookmark html
    2. Index the files
     */
    String indexDropLocation = System.getProperty(DROP_LOCATION);
    Validator.validateNotEmpty(indexDropLocation, DROP_LOCATION);

    String bookMarksHtmlFile = System.getProperty(BOOKMARKS_HTML_FILE);
    Validator.validateNotEmpty(bookMarksHtmlFile, BOOKMARKS_HTML_FILE);
    index(indexDropLocation, bookMarksHtmlFile);
  }

  public static void index(String indexDropLocation, String bookMarksHtmlFile) throws Exception {
    IndexWriterConfig indexWriterConfig = new IndexWriterConfig(new StandardAnalyzer())
//        .setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        .setOpenMode(IndexWriterConfig.OpenMode.CREATE);

    FSDirectory indexDir = FSDirectory.open(Paths.get(indexDropLocation));
    try (IndexWriter indexWriter = new IndexWriter(indexDir, indexWriterConfig)) {
      List<BookMark> bookMarks = BookMarkParser.loadBookMarks(new File(bookMarksHtmlFile));

      for (BookMark bookMark : bookMarks) {
        indexWriter.addDocument(DocumentBuilder.fromBookMark(bookMark));
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
