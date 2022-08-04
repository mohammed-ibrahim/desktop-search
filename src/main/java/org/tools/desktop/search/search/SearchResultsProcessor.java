package org.tools.desktop.search.search;

import org.apache.commons.collections4.CollectionUtils;
import org.tools.desktop.search.exec.LinkImpl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.List;

public class SearchResultsProcessor {

  public static void processBookMarkSearch(List<String> results) {

    if (CollectionUtils.isEmpty(results)) {
      System.out.println("No Results found");
      return;
    }

    if (results.size() == 1) {
      LinkImpl.openLink(results.get(0));
      return;
    }

    LinkedHashMap<Integer, String> linkedHashMap = new LinkedHashMap<>();

    for (int i=1; i<=results.size(); i++) {
      linkedHashMap.put(new Integer(i), results.get(i-1));
    }

    System.out.println("\n\nChoose from list below");
    linkedHashMap.forEach((k, v) -> {
      System.out.println(String.format("%d - %s", k, v));
    });

    System.out.print("\n\nEnter 0 to exit\nEnter Choice: ");
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    try {
      Integer choice = Integer.parseInt(br.readLine());

      if (choice == 0) {
        return;
      }

      if (linkedHashMap.containsKey(choice)) {
        LinkImpl.openLink(linkedHashMap.get(choice));
      } else {
        System.out.println("Invalid choice");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
