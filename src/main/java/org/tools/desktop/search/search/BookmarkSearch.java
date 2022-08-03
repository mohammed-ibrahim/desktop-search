package org.tools.desktop.search.search;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.CollectionUtil;
import org.tools.desktop.search.Fields;
import org.tools.desktop.search.Validator;
import org.tools.desktop.search.document.CommonTermsCleaner;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.tools.desktop.search.IndexerMain.DROP_LOCATION;

public class BookmarkSearch {

  public static void main(String[] args1) throws Exception {
    String indexDropLocation = System.getProperty(DROP_LOCATION);
    Validator.validateNotEmpty(indexDropLocation, DROP_LOCATION);

    String[] arg0 = {"ss", "pre"};

    String[] args = arg0;
    if (arg0 == null || arg0.length < 1) {
      throw new RuntimeException("No terms to search");
    }

    List<String> terms = Arrays.stream(args)
        .filter(arg -> StringUtils.isNotBlank(arg))
        .filter(arg -> !CommonTermsCleaner.isCommonTerm(arg))
        .collect(Collectors.toList());

    if (CollectionUtils.isEmpty(terms)) {
      throw new RuntimeException("No terms to search");
    }

    search(indexDropLocation, terms);
  }

  public static void search(String indexDropLocation, List<String> terms) throws Exception {
    try (IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexDropLocation)))) {
      TermQuery documentTypeQuery = new TermQuery(new Term(Fields.DOCUMENT_TYPE, Fields.DOCUMENT_TYPE_BOOKMARK));
      BooleanQuery.Builder builder = new BooleanQuery.Builder();
      builder.add(new BooleanClause(documentTypeQuery, BooleanClause.Occur.MUST));

      terms.forEach(term -> {
        TermQuery leafQuery = new TermQuery(new Term(Fields.FIELD_TAGS, term));
        builder.add(new BooleanClause(leafQuery, BooleanClause.Occur.MUST));
      });

      IndexSearcher searcher = new IndexSearcher(reader);
      TopDocs topDocs = searcher.search(builder.build(), 10);

      System.out.println("Total: " + topDocs.totalHits + " for: " + builder.build().toString());
      for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
        System.out.println(reader.document(scoreDoc.doc));
      }
    }
  }

}
