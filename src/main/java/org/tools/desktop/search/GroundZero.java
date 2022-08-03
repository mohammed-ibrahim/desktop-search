package org.tools.desktop.search;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

import java.nio.file.Path;
import java.nio.file.Paths;

public class GroundZero {

  private static final String F1 = "field";
  private static final String V1 = "value";
  private static final String V2 = "value2";
  private static final String V3 = "value3";

  public static void main(String[] args) throws Exception {
    //1. create index
    //2. search

    Path tempPath = Paths.get(System.getProperty("java.io.tmpdir"), String.valueOf(System.currentTimeMillis()));
    FileUtils.forceMkdir(tempPath.toFile());

    createIndex(tempPath);
    searchIndex(tempPath);
  }

  private static void searchIndex(Path indexPath) throws Exception {
    try (IndexReader reader = DirectoryReader.open(FSDirectory.open(indexPath))) {
      TermQuery termQuery = new TermQuery(new Term(F1, V3));
      BooleanQuery.Builder builder = new BooleanQuery.Builder();
      builder.add(new BooleanClause(termQuery, BooleanClause.Occur.MUST));

      builder.add(new BooleanClause(new TermQuery(new Term(F1, V1)), BooleanClause.Occur.MUST));

      IndexSearcher searcher = new IndexSearcher(reader);
      TopDocs topDocs = searcher.search(builder.build(), 10);

      System.out.println("Total: " + topDocs.totalHits + " for: " + builder.build().toString());
      for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
        System.out.println(reader.document(scoreDoc.doc));
      }
    }
  }

  private static void createIndex(Path indexPath) throws Exception {
    IndexWriterConfig indexWriterConfig = new IndexWriterConfig(new StandardAnalyzer())
//        .setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        .setOpenMode(IndexWriterConfig.OpenMode.CREATE);

    FSDirectory indexDir = FSDirectory.open(indexPath);
    try (IndexWriter indexWriter = new IndexWriter(indexDir, indexWriterConfig)) {
      indexWriter.addDocument(getDocumentForTextFields());
    }
  }

  private static Document getDocumentForStringFields() {
    Document document = new Document();
    StringField typeField = new StringField(F1, V1, Field.Store.YES);
    StringField typeField1 = new StringField(F1, V2, Field.Store.YES);
    StringField typeField2 = new StringField(F1, V3, Field.Store.YES);

    document.add(typeField);
    document.add(typeField1);
    document.add(typeField2);

    return document;
  }

  private static Document getDocumentForTextFields() {
    Document document = new Document();
    TextField typeField = new TextField(F1, V1 + " " + V2 + " " + V3, Field.Store.YES);

    document.add(typeField);
    return document;
  }
}
